package com.ice.demo.ui;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import com.ice.android.R;
import com.ice.android.common.view.BaseActivity;
import com.ice.android.common.view.PullToRefreshListView;
import com.ice.android.common.view.PullToRefreshListView.OnRefreshListener;
/**
 * 微博列表主界面
 * @author ice
 *
 */
public class RefreshListViewActivity extends BaseActivity {

	private static final String TAG = "RefreshListViewActivity";
	private PullToRefreshListView weiboListView ;
	// private ListView weiboListView ;
	private LinkedList<String> mListItems; 
	ArrayAdapter<String> adapter;
	private ImageButton ibtn_edit;
	
	
	@Override
	public void initView() {
		setTitleStyle(TITLE_STYLE_BACK_AND_RIGHTEDIT);
		setTitleValue("自定义下拉刷新ListView");
		setTitleBackgroundResource(R.drawable.aice_title_bg_blue);
		setEditButtonImageResource(R.drawable.test_refresh);
		setContentLayout(R.layout.test_refresh_listview);
		// setContentLayout(R.layout.main);
		ibtn_edit = (ImageButton) findViewById(R.id.ibtn_edit);
		weiboListView = (PullToRefreshListView) findViewById(R.id.weibolist);
		
		
	}

	
	@Override
	public void bindEvents() {
		final MyOnRefreshListener onRefreshListener = new MyOnRefreshListener();
		weiboListView.setOnRefreshListener(onRefreshListener);
	
        ibtn_edit.setOnClickListener(new OnClickListener() {
    			
    		@Override
    		public void onClick(View arg0) {
    			weiboListView.setOnClickRefreshListener(onRefreshListener);
    		}
    	});   
	}
	
	
	@Override
	public void initDatas() {
		init();
	} 

	
    private void init() {
		String[] mStrings = new String[50];
    	for(int i=0;i<50;i++){
    		int j = i+1;
    		mStrings[i] = "第"+ j +"条微博信息 xxxxxxxx";
    	}
    	
		mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(mStrings));
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
		weiboListView.setAdapter(adapter);
	}
    
    
    /**
	 * 这里先为列表显示准备一些数据,仅测试使用
	 */
	public static String[] mRefreshStr1 = { 
		"下拉刷新第一条微博", "下拉刷新第两条微博", "下拉刷新第三条微博", "下拉刷新第四条微博", "下拉刷新第五条微博","下拉刷新第六条微博", 
		"下拉刷新第七条微博", "下拉刷新第八条微博", "下拉刷新第九条微博", "下拉刷新第十条微博", "下拉刷新第十一条微博", "下拉刷新第十二条微博" 
		};

   
    
    /**
	 * 一个异步任务子类
	 * 用于刷新、加载更多信息
	 * @author ice
	 *
	 */
	private class GetDataTaskFromNet extends AsyncTask<Void, Void, String[]>{

		private Context context;
		private int index;
		
		public GetDataTaskFromNet(Context context, int index) {
			this.context = context;
			this.index = index;
		}

		@Override
		protected String[] doInBackground(Void... params) {
			// 完成耗时操作：获取微博列表
			try {
				Log.d(TAG, "正在从后台服务获取数据...");
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return mRefreshStr1;
		}
		
		/**
		 * 当doInBackground()完成后，系统会自动调用该方法，
		 * 并将doInBackground()方法返回值作为参数传给该方法
		 */
		@Override
		protected void onPostExecute(String[] result) {
			Log.d(TAG, "已获得数据,正在努力返回中...");
			if(index == 0){
				// 将字符串“Added after refresh”添加到顶部  
				// mListItems.addFirst("Added after refresh...");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");  
				String data = sdf.format(new Date());
				mListItems.addAll(Arrays.asList(result));
				adapter.notifyDataSetChanged();
				// 刷新完成  更新UI上最近更新时间和重置ListHead视图
				weiboListView.onRefreshComplete(data);
			}else if(index == 1){
				// mListItems.addLast("Added after loadmore...");
				
				mListItems.addAll(Arrays.asList(result));
				adapter.notifyDataSetChanged();
				weiboListView.onLoadMoreComplete();
			}
			super.onPostExecute(result);
		}
	}
	
	
	class MyOnRefreshListener implements OnRefreshListener{

		@Override
		public void onRefresh() {
			// do work to refresh the list here
			Log.d(TAG, "RefreshListViewActivity中onRefresh()方法被回调  \n do work to refresh the list now...");
			new GetDataTaskFromNet(RefreshListViewActivity.this,0).execute();
		}
		
		@Override
		public void onLoadMore() {
			// do work to load more the list here
			Log.d(TAG, "RefreshListViewActivity中onLoadMore()方法被回调  \n do work to load more the list now...");
			new GetDataTaskFromNet(RefreshListViewActivity.this, 1).execute();
		}
		
	}
	
	
	
}
