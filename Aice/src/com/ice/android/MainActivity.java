package com.ice.android;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.ice.android.common.core.BaseActivity;

public class MainActivity extends BaseActivity {

	private ImageButton ibtn_edit;
	
	@Override
	public void initView() {		
		// 右侧编辑按钮标题风格
		setTitleStyle(TITLE_STYLE_ONLY_RIGHTEDIT);
		setTitleValue("二维码购物");
		setTitleBackgroundResource(R.drawable.aice_title_bg_blue);
		setEditButtonImageResource(R.drawable.aice_barcode); // 设置右侧编辑按钮图片资源 
		setContentLayout(R.layout.main);
		ibtn_edit = (ImageButton) findViewById(R.id.ibtn_edit);
		ibtn_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
	}


	@Override
	public void bindEvents() {
		// TODO Auto-generated method stub
	}


	@Override
	public void initDatas() {
		// TODO Auto-generated method stub
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	
}
