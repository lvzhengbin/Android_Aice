package com.ice.android.common.excepion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.ice.android.common.utils.LogUtil;
import com.ice.android.common.utils.SdcardUtil;
/**
 * 线程未捕获异常处理器，用来处理未捕获的异常
 * 当程序发生Uncaught异常的时候，由该类来处理程序并记录/发送错误报告
 * @author ice
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";
	
	/** 系统默认的 UncaughtException 处理对象   */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	
	private Context mContext;

	/**
	 * 用于存储手机设备的参数信息
	 */
	private Map<String, String> infos = new HashMap<String, String>(); 
	
	private static CrashHandler mCrashHandler;
	
	private CrashHandler(){
		
	}
	
	/** 获取CrashHandler实例 ,单例模式   */
	public synchronized static CrashHandler getInstance(){
		if(mCrashHandler == null){ 
			mCrashHandler = new CrashHandler();
		}
		return mCrashHandler;
	}
	
	public void init(Context context){
		mContext = context;
		/** 获得系统默认的UncaughtException处理对象  */
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		/** 设置mCrashHandler为该程序默认的 UncaughtException处理对象 */
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时，会调用该方法 
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		LogUtil.d(TAG, "应用发生未知错误");
		if(!handleException(ex) && mDefaultHandler != null){
			// 如果用户自定义的mCrashHandler没有处理，则让系统默认的异常处理器来进行处理
			mDefaultHandler.uncaughtException(thread, ex);
		}else{
			// 3秒后退出程序
			try {  
                Thread.sleep(3000);  
            } catch (InterruptedException e) {  
            	LogUtil.e(TAG, "error : " + e);  
            }
			Process.killProcess(Process.myPid());
			System.exit(1);
		}
	}
	
	
	/**
	 * 自定义错误处理、收集错误信息、发送错误报告
	 * @param ex
	 * @return
	 */
	private boolean handleException(Throwable ex){
		if(ex ==  null){
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread(){
			@Override
			public void run() {
				/**
				 * 这里我们可以使用Toast简单的来显示异常后信息，也可以弹出对话框来显示  具体看项目的不同需求啦
				 */
				Looper.prepare();  
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出", Toast.LENGTH_LONG).show();
				Looper.loop();    
			}
		}.start();		
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfo2File(ex,mContext);
		return true;
	}

	
	/**
	 * 收集手机设备参数信息
	 * @param ctx
	 */
	private void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo packageInfo = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if(packageInfo != null){
				String versionName = packageInfo.versionName == null ? "null": packageInfo.versionName;
				String versionCode = packageInfo.versionCode + "";
				Log.d(TAG, "versionName:"+versionName+" ,versionCode:"+versionCode);
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e); 
		}
	}
	
	
	/**
	 * 保存错误信息到本地sdCard上指定目录的日志文件
	 * @param ex
	 * @param ctx
	 * @return 返回文件名 便于将文件上传至服务器
	 */
	private String saveCrashInfo2File(Throwable ex,Context ctx) {
		StringBuilder sb = new StringBuilder();
		for(String keySet:infos.keySet()){
			sb.append(keySet+"="+infos.get(keySet)+"\n");
		}
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		
		Throwable cause = ex.getCause();
		while(cause != null){
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		
		String result = writer.toString();
		sb.append(result);
		Log.e(TAG, sb.toString());
		
		long timestamp = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date(timestamp));
		String fileName = "crash_"+time+".log";
		if(SdcardUtil.isSdcardWritable()){
			String path = "/sdcard/"+ctx.getPackageName()+"/crash/";
			Log.d(TAG, "crash path: "+path);
			try {
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path+fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
				Log.d(TAG, "错误日志已保存在路径："+path+fileName);
				return fileName;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}


	/**
	 * 将错误日志信息上传到服务器
	 * @param file
	 * @param url
	 */
	private void upLoadCrashInfo2Service(File file,String url){
		
		
	}

}
