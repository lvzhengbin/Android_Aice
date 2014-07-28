package com.ice.android.common.view;

import com.ice.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义ListView 实现"下拉刷新"+"上拉加载更多"功能效果
 * @author ice
 *
 */
public class PullToRefreshListView extends ListView implements OnScrollListener{

	private static final String TAG = "PullToRefreshListView";
	private static final int TAP_TO_REFRESH = 1;      // 未刷新
	private static final int PULL_TO_REFRESH  =2;     // 下拉刷新
	private static final int RELEASE_TO_REFRESH = 3;  // 释放刷新  
    private static final int REFRESHING = 4;          // 正在刷新  
    private static final int TAP_TO_LOADMORE = 5;     // 未加载更多  
    private static final int LOADING = 6;             // 正在加载 
    
    private OnRefreshListener mOnRefreshListener;
    private OnScrollListener mOnScrollListener;    // 列表滚动监听器
    
    private RotateAnimation mFlipAnimation;   // 下拉动画
    private RotateAnimation mReverseFlipAnimation;  // 恢复动画
    
    private LayoutInflater mInflater;   // 用户加载布局文件
    private RelativeLayout mRefreshHeaderView ;  // 头部的刷新视图
	private RelativeLayout mLoadMoreFooterView;  // 底部的加载更多视图
	private TextView mRefreshViewText;  // 刷新提示文本
	private TextView mRefreshViewLastUpdated;  // 刷新更新时间提示文本
	private ImageView mRefreshViewImage;   // 刷新时向上向下的那个箭头指示图片
	private TextView mLoadMoreText;  // 加载更多提示文本
	private ProgressBar mLoadmoreProgress; // 加载更多进度条
	
	private int mCurrentScrollState;  // 当前滚动的状态
	private int mRefreshState;     // 刷新状态
	private int mLoadState;        // 加载状态
	
	private int mRefreshViewHeight;           // 刷新视图高度                     
	private int mRefreshOriginalTopPadding;   // 原始上部间隙  
	private int mLastMotionY;     // 记录列表滚动到哪个位置
    
	public PullToRefreshListView(Context context) {
		super(context);
		init(context);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	
	private void init(Context context) {
		/**
		 * 定义旋转动画
		 * 参数：1.旋转开始角度 。  2.旋转结束角度。  3.x轴旋转模式。  4.x坐标旋转值。  5.y轴的旋转模式。 6.y坐标旋转值
		 */
		mFlipAnimation = new RotateAnimation(0, 
				                             -180, 
				                             RotateAnimation.RELATIVE_TO_SELF, 
				                             0.5f, 
				                             RotateAnimation.RELATIVE_TO_SELF, 
				                             0.5f);
		// 设置动画以均匀速率改变
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		// 设置动画的持续时间
		mFlipAnimation.setDuration(300);
		// 保持动画结束后的状态
		mFlipAnimation.setFillAfter(true);
		
		mReverseFlipAnimation = new RotateAnimation(-180, 
                                                    0, 
                                                    RotateAnimation.RELATIVE_TO_SELF, 
                                                    0.5f, 
                                                    RotateAnimation.RELATIVE_TO_SELF, 
                                                    0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(300);
		mReverseFlipAnimation.setFillAfter(true);
		
		// 获取LayoutInflater实例对象
		// mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater = LayoutInflater.from(context);
		// 加载下拉刷新的头部视图
		mRefreshHeaderView = (RelativeLayout) mInflater.inflate(R.layout.pull_to_refresh_header,null); // 参数2、参数3的含义
		mRefreshViewText = (TextView) mRefreshHeaderView.findViewById(R.id.tv_pull_to_refresh);
		mRefreshViewLastUpdated = (TextView) mRefreshHeaderView.findViewById(R.id.tv_refresh_updated_at);
		mRefreshViewImage = (ImageView) mRefreshHeaderView.findViewById(R.id.iv_pull_to_refresh);
		// 上拉加载更多的底部视图
		mLoadMoreFooterView = (RelativeLayout) mInflater.inflate(R.layout.loadmore_footer, null);
		mLoadMoreText = (TextView) mLoadMoreFooterView.findViewById(R.id.tv_loadmore);
		mLoadmoreProgress = (ProgressBar) mLoadMoreFooterView.findViewById(R.id.pb_loadmore_progress);
		
		// 设置刷新箭头图片最小高度
		mRefreshViewImage.setMinimumHeight(100);
		// mRefreshViewImage.setOnClickListener(new OnClickRefreshListener());  // 这个其实没多大意义
		mLoadMoreFooterView.setOnClickListener(new OnClickLoadMoreListener());  // 添加点击加载更多事件监听
		
		mRefreshOriginalTopPadding = mRefreshHeaderView.getPaddingTop();
		mRefreshState = TAP_TO_REFRESH;   // 初始化刷新状态：未刷新
		mLoadState = TAP_TO_LOADMORE;     // 初始化加载状态：未加载更多
		
		// 增加头部视图
		addHeaderView(mRefreshHeaderView);
		// 增加尾部视图
		addFooterView(mLoadMoreFooterView);
		
		super.setOnScrollListener(this);
		// 测量视图  计算和分配顶部空间
		measureView(mRefreshHeaderView);    
		mRefreshViewHeight = mRefreshHeaderView.getMeasuredHeight();  // 头部刷新视图的高度
	}

	
	/**
	 * 测量视图  计算和分配空间
	 * @param refreshHeaderView
	 */
	private void measureView(View refreshHeaderView) {
		android.view.ViewGroup.LayoutParams p = refreshHeaderView.getLayoutParams();
		if(p == null){
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0+0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if(lpHeight >0){
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		}else{
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		refreshHeaderView.measure(childWidthSpec, childHeightSpec);
	}
	
	
	/**
	 * 列表下拉刷新完成后，调用该方法
	 * @param data
	 */
	public void onRefreshComplete(String data) {
		Toast.makeText(getContext(), "列表下拉刷新完成,最后更新时间为："+data,Toast.LENGTH_LONG).show();
		Log.d(TAG, "列表下拉刷新完成,最后更新时间为: "+data);
		// 显示更新时间
		setLastUpdated(data);
		onRefreshComplete();
	}

	/**
	 * 设置显示最后更新时间
	 * @param data
	 */
	private void setLastUpdated(String data) {
		if(data != null){
			mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
			mRefreshViewLastUpdated.setText("最近更新："+data);
		}else{
			mRefreshViewLastUpdated.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 刷新完成，恢复刷新前状态
	 */
	private void onRefreshComplete() { 
		Log.d(TAG, "列表刷新过程已完成...");
		resetHeader();
		if(mRefreshHeaderView.getBottom()>0){
			invalidateViews();
			setSelection(1);
		}
		
	}

	/**
	 * 列表加载更多完成后，调用该方法
	 */
	public void onLoadMoreComplete() {
		Toast.makeText(getContext(), "列表加载更多完成", Toast.LENGTH_LONG).show();
		Log.d(TAG, "列表加载更多完成, 当前滚动到的位置是："+mLastMotionY);
		setSelection(mLastMotionY+1);
		resetFooter();
	}
	

	/**
	 * 点击控件时触发
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// final int y = (int) event.getY();  // 获取点击位置的Y坐标
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:     // 手指抬起
			Log.d(TAG, "手指抬起过程...");
			if(!isVerticalScrollBarEnabled()){
				setVerticalScrollBarEnabled(true);
			}
			if(getFirstVisiblePosition() == 0 && mRefreshState != REFRESHING){
				if(mRefreshHeaderView.getBottom() < mRefreshViewHeight || mRefreshHeaderView.getTop()<0){
					resetHeader();
					setSelection(1);
				}else if((mRefreshHeaderView.getBottom() > mRefreshViewHeight
                        || mRefreshHeaderView.getTop() >= 0)
                        && mRefreshState == RELEASE_TO_REFRESH){
					// Initiate the refresh
					mRefreshState = REFRESHING;   // 状态更改为刷新状态
					prepareForRefresh();
					onRefresh();
				}
			}
			break;
		case MotionEvent.ACTION_DOWN:   // 刚点击
			// Log.d(TAG, "手指刚点击过程...");
			
			break;
		case MotionEvent.ACTION_MOVE:  // 手指移动
			// Log.d(TAG, "手指移动过程...");
			
			break;
		}
		return super.onTouchEvent(event);
	}


	/**
	 * ListView滑动时调用
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
        Log.d(TAG, "onScroll()方法正在被调用,\n firstVisibleItem = "+firstVisibleItem+
        		",mCurrentScrollState = "+mCurrentScrollState+
        		",mRefreshState:"+mRefreshState+
        		",mLoadState:"+mLoadState);
		if(mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL && mRefreshState != REFRESHING){
			Log.d(TAG, "正在下拉过程...");
			if(firstVisibleItem == 0){   // 如果第一个可见条目为0
				mRefreshViewImage.setVisibility(View.VISIBLE);    // 让指示箭头变得可见
				if(mRefreshHeaderView.getBottom() < mRefreshViewHeight+20 && mRefreshState != PULL_TO_REFRESH){
					mRefreshViewText.setText(R.string.ms_pull_to_refresh);
					if (mRefreshState != TAP_TO_REFRESH) {
						mRefreshViewImage.clearAnimation();
						mRefreshViewImage.startAnimation(mReverseFlipAnimation);
					}
					mRefreshState = PULL_TO_REFRESH;
				}else if((mRefreshHeaderView.getBottom() > mRefreshViewHeight + 20
                        || mRefreshHeaderView.getTop() >= 0)
                        && mRefreshState != RELEASE_TO_REFRESH){
					mRefreshViewText.setText(R.string.ms_release_to_refresh);
					mRefreshViewImage.clearAnimation();
					mRefreshViewImage.startAnimation(mFlipAnimation);
					mRefreshState = RELEASE_TO_REFRESH;  // 更改刷新状态为“释放以刷新"
				}
				
			}else {
                mRefreshViewImage.setVisibility(View.GONE);			// 让刷新箭头不可见
                resetHeader();	// 重新设置头部为原始状态
            }
			
		}else if(mCurrentScrollState == SCROLL_STATE_FLING && firstVisibleItem == 0 && mRefreshState != REFRESHING){
			setSelection(1);
			
		}else if(mCurrentScrollState == SCROLL_STATE_FLING && firstVisibleItem != 0){
			//Log.d(TAG, "小吕 正在下拉滚动  测试下拉滚动又返回到微博一的bug"); 
			// mLastMotionY = firstVisibleItem;
		}else if(mCurrentScrollState == SCROLL_STATE_IDLE && mLoadState == LOADING){
			
			
		}else{
			setSelection(1);
		}
		if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }
	
	}

	/**
	 * 滑动状态改变时调用
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		Log.d(TAG, "onScrollStateChanged()方法正在被调用,scrollState = "+scrollState);
		mCurrentScrollState = scrollState;
		mLastMotionY = view.getFirstVisiblePosition();
		if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
	}

	
	/**
	 * 为刷新做准备
	 */
    private void prepareForRefresh() {
    	Log.d(TAG, "刷新准备工作中...");
    	resetHeaderPadding();	
    	mRefreshViewImage.setVisibility(View.GONE);  // 隐藏刷新的箭头图片
    	// We need this hack, otherwise it will keep the previous drawable.
    	mRefreshViewImage.setImageResource(R.drawable.aice_loading);
    	mRefreshViewText.setText("正在卖命刷新中...");
    	mRefreshState = REFRESHING;
	}
    
    /**
     * 为加载更多做准备
     */
    public void prepareForLoadMore() {
    	Log.d(TAG, "正在准备加载工作中...");
    	mLoadmoreProgress.setVisibility(View.VISIBLE);
    	mLoadMoreText.setText("正在卖命加载中...");
    	mLoadState = LOADING;
	}

    /**
     *  刷新
     */
    private void onRefresh() {
    	Log.d(TAG, "正在卖命刷新中...");
    	if(mOnRefreshListener != null){
    		mOnRefreshListener.onRefresh();
    	}
	}
    
    /**
     * 加载更多
     */
    private void onLoadMore(){
    	Log.d(TAG, "正在卖命加载中...");
    	if(mOnRefreshListener != null){
    		mOnRefreshListener.onLoadMore();
    		setSelection(mLastMotionY+1);
    	}
    }
    
	
	/**
     *  重新设置头部为原始状态
     */
	private void resetHeader() {
		if(mRefreshState != TAP_TO_REFRESH){
			mRefreshState = TAP_TO_REFRESH;
			resetHeaderPadding();
			mRefreshViewText.setText(R.string.ms_pull_to_refresh);
			mRefreshViewImage.setImageResource(R.drawable.pulltorefresh_down_arrow);
			mRefreshViewImage.clearAnimation();
			mRefreshViewImage.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 *  重设ListView尾部视图为初始状态
	 */
	private void resetFooter() {
		if(mLoadState != TAP_TO_LOADMORE){
			mLoadState = TAP_TO_LOADMORE;
			// 进度条设置为不可见
			mLoadmoreProgress.setVisibility(View.GONE);
			// 按钮的文本替换为“加载更多”
			mLoadMoreText.setText(R.string.ms_load_more);
		}
	}
	
    /**
     * 设置头部填充到原始大小
     */
	private void resetHeaderPadding() {
		mRefreshHeaderView.setPadding(mRefreshHeaderView.getPaddingLeft(), 
				mRefreshOriginalTopPadding, 
				mRefreshHeaderView.getPaddingRight(), 
				mRefreshHeaderView.getPaddingBottom());
	}
	
	
	/**
	 * 注册列表滚动监听
	 */
	@Override
	public void setOnScrollListener(OnScrollListener scrollListener) {
		mOnScrollListener = scrollListener;
	}
	
	
	/**
	 * 注册 列表被下拉或上拉刷新 事件监听器
	 * @param onRefreshListener
	 */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener){
		this.mOnRefreshListener = onRefreshListener;
	}
	
	/**
	 * 定义一个回调接口,当列表被下拉刷新或加载更多刷新时被调用
	 * @author ice
	 */
	public interface OnRefreshListener{
		// 下拉刷新时被调用
		public void onRefresh();
		// 上拉加载更多时被调用
		public void onLoadMore();
	}
	
	
	/**
	 * 点击外部控件 刷新列表<br>
	 * 场景：比如Title标题上有个按钮点击刷新ListView（参考 QQ空间App）
	 * @author ice
	 *
	 */
	public void setOnClickRefreshListener(OnRefreshListener onRefreshListener){
		onRefreshListener.onRefresh();
	}
	
	
	/**
	 * 点击加载更多
	 * @author ice
	 *
	 */
	private class OnClickLoadMoreListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			if(mLoadState != LOADING){
				prepareForLoadMore();
				onLoadMore();
			}
		}
	}
	
	
	
}
