package com.ice.android;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ice.android.common.net.NetChangeObserver;
import com.ice.android.common.net.NetType;
import com.ice.android.common.net.NetWorkStateReceiver;
import com.ice.android.common.view.BaseActivity;
import com.ice.android.common.view.DialogManager;
import com.ice.demo.ui.DbTestActivity;
import com.ice.demo.ui.RefreshListViewActivity;

public class MainActivity extends BaseActivity implements NetChangeObserver{

	private static final String TAG = "MainActivity";
	private Button btn;
	private Button btn_dbtest;
	private ImageButton ibtn_edit;
	
	@Override
	public void initView() {
		// 默认标题风格
		/*setTitleStyle(TITLE_STYLE_DEFAULT);
		setTitleValue("首页");
		setTitleBackgroundResource(R.drawable.aice_title_bg_blue);
		setContentLayout(R.layout.main);*/
		
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
				Toast.makeText(MainActivity.this, "二维码扫描购物 功能正在开发中", Toast.LENGTH_SHORT).show();
			}
		});
		
		
		btn = (Button) findViewById(R.id.btn);
		btn_dbtest = (Button) findViewById(R.id.btn_dbtest);
		/*DialogManager.showLoadingDialog(this);
		
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(10000);
					DialogManager.dismissLoadingDialog();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();*/
		
		
		/*DialogManager dialog = new DialogManager(this);
		DialogManager.DialogOnclickLinstener leftClick = dialog.new DialogOnclickLinstener(){

			@Override
			public void clickEvent(View view, View dialogContent) {
				Toast.makeText(MainActivity.this, "你点击了取消按钮", Toast.LENGTH_SHORT).show();
			}
			
		};
		
		DialogManager.DialogOnclickLinstener rightClick = dialog.new DialogOnclickLinstener() {
			
			@Override
			public void clickEvent(View view, View dialogContent) {
				Toast.makeText(MainActivity.this, "你点击了确定按钮", Toast.LENGTH_SHORT).show();
				finish();
			}
		};
		
		dialog.showCommonDialog("退出", "确定退出系统？", leftClick, rightClick, "取消", "确认");*/
		
		
		// 自定义对话框显示
		/*DialogManager dialog = new DialogManager(this, 0);
		DialogManager.DialogOnclickLinstener leftClick = dialog.new DialogOnclickLinstener(){

			@Override
			public void clickEvent(View view, View dialogContent) {
				EditText name = (EditText) dialogContent.findViewById(R.id.et_name);
				Toast.makeText(MainActivity.this, name.getText().toString()+ " 取消了登陆", Toast.LENGTH_SHORT).show();
			}
			
		};
		
		DialogManager.DialogOnclickLinstener rightClick = dialog.new DialogOnclickLinstener() {
			
			@Override
			public void clickEvent(View view, View dialogContent) {
				EditText name = (EditText) dialogContent.findViewById(R.id.et_name);
				Toast.makeText(MainActivity.this, name.getText().toString()+" 正在登陆", Toast.LENGTH_SHORT).show();
			}
		};
		dialog.showCustomerDialog(this, R.layout.test_custom_dialog,leftClick,rightClick,"Cancel", "Login");*/
		
	}


	@Override
	public void bindEvents() {
		// 注册为网络监听者/观察者
		NetWorkStateReceiver.registerNetStateObserver(this);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, RefreshListViewActivity.class));
			}
		});
		
		btn_dbtest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this, DbTestActivity.class));
			}
		});
		
		
	}


	@Override
	public void initDatas() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		NetWorkStateReceiver.unRegisterNetStateObserver(this);
	}
	
	
	// 监听网络状态  当网络连接时调用该方法
	@Override
	public void OnConnect(NetType netType) {
		Log.d(TAG, "网络已连接...");
		Toast.makeText(this, "网络已连接...", Toast.LENGTH_LONG).show();
	}

	// 监听网络状态 当网络连接断开时调用该方法
	@Override
	public void OnDisConnect() {
		Log.d(TAG, "网络已断开...");
		Toast.makeText(this, "网络已断开...", Toast.LENGTH_LONG).show();
	}


	
	
}





/*@Override
public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	int id = item.getItemId();
	if (id == R.id.action_settings) {
		return true;
	}
	return super.onOptionsItemSelected(item);
}*/