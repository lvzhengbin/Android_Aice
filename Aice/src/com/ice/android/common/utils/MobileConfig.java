package com.ice.android.common.utils;

import com.ice.android.common.constants.AppConstants;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * 手机配置信息
 * @author ice
 *
 */
public class MobileConfig {

	private static MobileConfig mConfig;
	// private static Context mContext;
	
	private PackageManager mPackageManager;
	private PackageInfo mPackageInfo;
	/*
	 * 注意：使用TelephonyManager时，必须在AndroidManifest.xml中添加<uses-permission android:name="READ_PHONE_STATE" />
	 */
	private TelephonyManager mTelManager;
	private DisplayMetrics mDesMetrics;
	
	private MobileConfig(Context context){
		// mContext = context;
		mPackageManager = context.getPackageManager();
		try {
			mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		mTelManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		mDesMetrics = context.getResources().getDisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(metrics);
	}
	
	/**
	 * 单例实例
	 * @param context
	 * @return
	 */
	public static synchronized MobileConfig getInstance(Context context){
		if(mConfig == null){
			mConfig = new MobileConfig(context);
		}
		return mConfig;
	}
	
	/**
	 * 返回手机系统名称
	 * @return
	 */
	public String getOSName(){
		return AppConstants.OSNAME;
	}
	
	/**
	 * 返回手机系统的版本字符串
	 */
	public String getMobileOsVersion(){
		String mSdkVersion = Build.VERSION.RELEASE;
		return mSdkVersion;
	}
	
	/**
	 * 返回手机产品名称
	 */
	public String getMobileModel(){
		return Build.MODEL;
	}
	
	/**
	 * 返回手机设备名称
	 * @return
	 */
	public String getMobileDeviceName(){
		return Build.DEVICE;
	}
	
	/**
	 * 返回手机分辨率的高度
	 * @return
	 */
	public int getResolutionH(){
		return mDesMetrics.heightPixels;
	}
	
	/**
	 * 返回手机分辨率的宽度
	 * @return
	 */
	public int getResolutionW(){
		return mDesMetrics.widthPixels;
	}
	
	
	/**
	 * Get the number of phone,sometimes we can't get it because of sim operator's limit.
	 * @return
	 */
	public String getMobileNum() {
		return mTelManager.getLine1Number();
	}
	
	
	/**
	 * 返回手机所连的网络IP段信息
	 */
	
	
}
