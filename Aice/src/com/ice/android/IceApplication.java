package com.ice.android;

import com.ice.android.common.excepion.CrashHandler;

import android.app.Application;
/**
 * 
 * @author ice
 * 参考博客：http://blog.csdn.net/liuhe688/article/details/6584143
 */
public class IceApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 获得自定义的 未捕获异常处理器
		CrashHandler mCrashHandler = CrashHandler.getInstance();
		mCrashHandler.init(getApplicationContext());
	}

	
}
