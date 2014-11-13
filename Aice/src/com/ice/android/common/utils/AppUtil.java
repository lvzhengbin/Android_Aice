package com.ice.android.common.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * App工具类
 * @author ice
 *
 */
public class AppUtil {
	
	/**
	 * 获取应用程序的名称<br>
	 * 获取在AndroidManifest.xml 元素<application> 中 android:label 对应的名称
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * 返回当前应用版本信息
	 * @return
	 */
	public String getAppVersionInfo(Context context){
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * 根据包名判断应用程序是否安装
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstallAppByPackageName(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
		if (installedPackages != null) {
			for (PackageInfo pInfo : installedPackages) {
				if (pInfo.packageName.equalsIgnoreCase(packageName)) {
					return true;
				}
			}
		}
		return false;
	}

	
	/**
	 * 根据包名和类名打开应用程序
	 * @param context
	 * @param packs    [包名,类名]
	 * @return 判断是否打开，true为打开，false为未打开
	 */
	public static boolean openAppByPackageName(Context context, String[] packs) {
		if (packs == null || packs.length == 0) {
			return false;
		}
		String packageName = packs[0];
		String className = packs[1];
		PackageInfo packageInfo = null;
		try {
			PackageManager packageManager = context.getPackageManager();
			packageInfo = packageManager.getPackageInfo(packageName, 0);
			if (packageInfo != null) {
				Intent intent = new Intent();
				intent.setClassName(packageName, className);
				((Activity)context).startActivity(intent);
				packageInfo=null;
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			packageInfo=null;
			return false;
		}
		return false;
	}
	
	
	/**
	 * whether application is run in background
	 * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
	 * @param context
	 * @return  if application is in background return true, otherwise return false
	 */
	public static boolean isApplicationInBackground(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);
		if (runningTasks != null && !runningTasks.isEmpty()) {
			ComponentName topActivity = runningTasks.get(0).topActivity;
			if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}
	
	
	
}
