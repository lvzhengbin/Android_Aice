package com.ice.android.common.core;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ice.android.R;
/**
 * 所有UI页面Activity抽象基类<br>
 * 可根据具体项目需求修改、扩展该类<br>
 * <b>备注:</b><br>
 * 已把该类移至com.ice.android.common.core包下
 * @author ice
 *
 */
public abstract class BaseActivity extends Activity {

	private static final String TAG = "BaseActivity";
	/** 标题栏  */
	private LinearLayout ll_title;
	/** 内容区域  */
	private LinearLayout ll_content;
	/** 标题名称   */
	protected TextView tv_title;
	/** 标题上 返回图片按钮  */
	protected ImageButton ibtn_back;
	/** 标题上 右侧编辑按钮  */
	protected ImageButton ibtn_edit;
	
	/** 标识Title是否有返回按钮  */
	private boolean isBackTitle = false;
	/** 标识Title是否有右侧编辑按钮  */
	private boolean isEditTitle = false;
	
	/** 无标题  */
	protected static final int TITLE_STYLE_NONE = -1;
	/** 默认风格   只有标题内容   */
	protected static final int TITLE_STYLE_DEFAULT = 0;
	/** 左侧返回按钮 + title标题  */
	protected static final int TITLE_STYLE_ONLY_BACK = TITLE_STYLE_DEFAULT + 1;
	/** title标题 + 右侧编辑按钮 */
	protected static final int TITLE_STYLE_ONLY_RIGHTEDIT = TITLE_STYLE_ONLY_BACK + 1;
	/** 左侧返回按钮 + title标题  + 右侧搜索图标按钮  */
	protected static final int TITLE_STYLE_BACK_AND_RIGHTSEARCH = TITLE_STYLE_ONLY_RIGHTEDIT + 1;
	/** 左侧返回按钮 + title标题  + 右侧编辑图标按钮/状态等各种菜单按钮  */
	protected static final int TITLE_STYLE_BACK_AND_RIGHTEDIT = TITLE_STYLE_BACK_AND_RIGHTSEARCH + 1;
	/** 自定义Title风格  */
	protected static final int TITLE_STYLE_CUSTOM = TITLE_STYLE_BACK_AND_RIGHTEDIT + 1;
	/** 二维码布局  */
	protected static final int TITLE_STYLE_BARCODER = TITLE_STYLE_CUSTOM + 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getParamterData(savedInstanceState);
		init();
		// 备注：是否注册全局的网络状态监听
	}


	/**
	 * 获取从Intent传递过来的参数
	 * @param savedInstanceState
	 */
	private void getParamterData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}


	private void init() {
		setTitleStyle(TITLE_STYLE_DEFAULT);   // 默认显示 title默认风格
		initView();
		bindEvents();
		initDatas();
	}


	/**
	 * 初始化控件   由BaseActivity的子类实现
	 */
	public abstract void initView();

	/**
	 * 绑定事件监听   由BaseActivity的子类实现
	 */
	public abstract void bindEvents();

	/**
	 * 初始化数据   由BaseActivity的子类实现
	 */
	public abstract void initDatas();
	
	
    /**
     * 设置页面布局风格<br>
     * 可根据实际项目需求进行扩展<br>
     * 如：能支持换肤功能|更改应用主题
     * @param layoutStyleId
     */
	protected void setActivityStyle(int layoutStyleId){
		// DOTO
	}
	
	
	/**
	 * 设置页面Title风格<br>
	 * 可根据实际项目需求进行扩展<br>
	 * @param styleId
	 */
	protected void setTitleStyle(int styleId) {
		switch (styleId) {
		case TITLE_STYLE_DEFAULT:  // 默认风格  只有标题名
			setContentView(R.layout.aice_layout_default);
			initBaseView();
			break;
		case TITLE_STYLE_ONLY_BACK:  // 左侧返回按钮 + title标题
			isBackTitle = true;
			setContentView(R.layout.aice_layout_back_title);
			initBaseView();
			break;
		case TITLE_STYLE_ONLY_RIGHTEDIT:   // title标题 + 右侧编辑按钮
			isEditTitle = true;
			setContentView(R.layout.aice_layout_rightedit_title);
			initBaseView();
			break;
		case TITLE_STYLE_BACK_AND_RIGHTSEARCH:  // 左侧返回按钮 + title标题  + 右侧搜索图标按钮
			
			
			break;
		case TITLE_STYLE_BACK_AND_RIGHTEDIT:  // 左侧返回按钮 + title标题  + 右侧编辑图标按钮/状态等各种菜单按钮
			isBackTitle = true;
			isEditTitle = true;
			setContentView(R.layout.aice_layout_back_and_rightedit);
			initBaseView();
			break;
		case TITLE_STYLE_CUSTOM:  // 自定义title风格
			setContentView(R.layout.aice_layout_default);
			// initBaseView();
			initCustomView();
			break;
		case TITLE_STYLE_BARCODER:    // 二维码布局
			 
			break;
		case TITLE_STYLE_NONE:   // 无标题
			
			break;
		default:
			break;
		}
	}
	
	
	private void initBaseView() {
		ll_title = (LinearLayout) findViewById(R.id.ll_title);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		
		if(isBackTitle){
			ibtn_back = (ImageButton) findViewById(R.id.ibtn_back);
			// 为返回按钮注册点击监听事件
			ibtn_back.setOnClickListener(new BackClickEvent());
		}
		
		if(isEditTitle){
			ibtn_edit = (ImageButton) findViewById(R.id.ibtn_edit);
			// ibtn_edit.setOnClickListener(new TitleEditBtnClickEvent());
		}
		
	}


	private void initCustomView(){
		ll_title = (LinearLayout) findViewById(R.id.ll_title);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setVisibility(View.GONE);
	}
	
	
	/**
	 * 设置自定义标题区域布局
	 * @param layoutTitleResId
	 */
	protected void setTitleLayout(int layoutTitleResId){
		View titleView = LayoutInflater.from(this).inflate(layoutTitleResId, null);
		titleView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		if(ll_title != null){
			ll_title.addView(titleView);
		}
	}
	
	
	/**
	 * 设置内容区域布局 
	 * @param layoutContentResId  布局文件Id
	 */
	protected void setContentLayout(int layoutContentResId){
		View contentView = LayoutInflater.from(this).inflate(layoutContentResId, null);
		contentView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		if(ll_content != null){
			ll_content.addView(contentView);
		}
	}
	
	
	/**
	 * 设置标题名称
	 * @param resId
	 */
	protected void setTitleValue(int resId){
		if(tv_title != null){
			tv_title.setVisibility(View.VISIBLE);
			tv_title.setText(resId);
		}
	}
	
	
	/**
	 * 设置标题名称
	 * @param titleName
	 */
	protected void setTitleValue(CharSequence titleName){
		if(tv_title != null){
			tv_title.setVisibility(View.VISIBLE);
			tv_title.setText(titleName);
		}
	}
	
	
	/**
	 * 设置标题背景资源
	 * @param resId
	 */
	protected void setTitleBackgroundResource(int resId){
		if(ll_title != null){
			ll_title.setBackgroundResource(resId);
		}
	}
	
	
	/**
	 * 设置标题上 右侧编辑按钮 Drawable资源 id<br>
	 * 注意:必须要在BaseActivity子类中  调用setTitleStyle()后  调用该方法设置编辑按钮图标才有效
	 * @param drawableId 
	 */
	protected void setEditButtonImageResource(int drawableId){
		if(ibtn_edit != null){
			ibtn_edit.setImageResource(drawableId);
		}
	}
	
	
	/**
	 * 返回按钮点击事件
	 * @author ice
	 *
	 */
	class BackClickEvent implements View.OnClickListener{

		@Override
		public void onClick(View arg0) {
			BaseActivity.this.finish();
		}
	}
	
	
	/**
	 * 标题上 右侧编辑按钮点击事件监听器<br>
	 * 子类中 通过继承TitleEditBtnClickEvent 实现不同功能的编辑按钮点击事件
	 * @author ice
	 *
	 */
	/*class TitleEditBtnClickEvent implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	}*/
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
}
