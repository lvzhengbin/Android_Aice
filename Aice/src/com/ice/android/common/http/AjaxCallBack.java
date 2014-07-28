package com.ice.android.common.http;
/**
 * @author ice
 * 网络请求 响应结果回调处理抽象类
 * @param <T> 泛型支持 String/File/JSONObject/Bitmap/byte[]/XmlDom
 */
public abstract class AjaxCallBack<T> {

	/** 是否启用进度在UI上显示  */
	private boolean progress = true;
	
	/** 进度更新频率  */
	private int rate = 1000 * 1;//每秒
	
	/**
	 * 设置进度: 而且只有设置了这个了以后，onLoading才能有效。
	 * @param mProgress  是否在UI上显示进度
	 * @param mRate  进度更新频率
	 * @return
	 */
	public AjaxCallBack<T> progress(boolean mProgress,int mRate){
		progress = mProgress;
		rate = mRate;
		return this;
	}
	
	/**
	 * 网络请求前  方法被调用
	 * 主要完成请求网络前的初始化工作
	 */
	public void onStart(){};
	
	/**
	 * 当progress为true时,用来显示进度加载情况
	 * @param count
	 * @param current
	 */
	public void onLoading(long count,long current){}
	
	/**
	 * 网络请求成功时 方法被调用
	 * @param t
	 */
	public void onSuccess(T t){}
	
	/**
	 * 当网络请求失败时 方法被调用
	 * @param t
	 * @param errorNo
	 * @param errorMsg
	 */
	public void onFailure(Throwable t,int errorNo,String errorMsg){}
	
	
	public boolean isProgress(){
		return progress;
	}
	
	public int getRate(){
		return rate;
	}
	
	
}
