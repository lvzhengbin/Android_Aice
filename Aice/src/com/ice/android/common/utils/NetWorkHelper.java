package com.ice.android.common.utils;

import com.ice.android.common.net.NetType;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 手机网络连接情况帮助类-单例
 * @author ice
 *
 */
public class NetWorkHelper {

	private static NetWorkHelper mNetHelper;
	
	private ConnectivityManager mConnectivityManager;
	
	private NetWorkHelper(Context context){
		mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	
	/**
	 * 获得单例 NetWorkHelper对象
	 * @return
	 */
	public synchronized static NetWorkHelper getInstance(Context mContext){
		if(mNetHelper == null){
			mNetHelper = new NetWorkHelper(mContext);
		}
		return mNetHelper;
	}
	
	
	/**
	 * 网络是否可用
	 * @return
	 */
	public boolean isNetworkAvailable(){
		NetworkInfo[] allNetworkInfos = mConnectivityManager.getAllNetworkInfo();
		if(allNetworkInfos != null){
			for(NetworkInfo mNetworkInfo:allNetworkInfos){
				if(mNetworkInfo.getState() == NetworkInfo.State.CONNECTED){
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 当前手机是否有网络连接
	 * @return
	 */
	public boolean isNetWorkConnected(){
		NetworkInfo activeNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if(activeNetworkInfo != null){
			return activeNetworkInfo.isAvailable();
		}
		return false;
	}
	
	
	/**
	 * 判断WIFI网络是否可用/是否已连接至WIFI网络
	 * @return
	 */
	public boolean isWifiConnected(){
		NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(mWiFiNetworkInfo != null && mWiFiNetworkInfo.isAvailable()){
			return mWiFiNetworkInfo.isConnected();
		}
		return false;
	}
	
	
	/**
	 * 判断MOBILE网络是否可用
	 * @return
	 */
	public boolean isMobileConnected(){
		NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(mMobileNetworkInfo != null){
			return mMobileNetworkInfo.isAvailable();
		}
		return false;
	}
	
	
	/**
	 * 获取当前网络连接的类型信息
	 * @return
	 */
	public int getConnectedNetType(){
		NetworkInfo mNetInfo = mConnectivityManager.getActiveNetworkInfo();
		if(mNetInfo != null && mNetInfo.isAvailable()){
			return mNetInfo.getType();
		}
		return -1;
	}
	
	
	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap 网络3：net网络
	 * @return
	 */
	public NetType getAPNType(){
		NetworkInfo mNetInfo = mConnectivityManager.getActiveNetworkInfo();
		if(mNetInfo == null){
			return NetType.NONENET;
		}
		
		int nType = mNetInfo.getType();
		if(nType == ConnectivityManager.TYPE_MOBILE){
			if(mNetInfo.getExtraInfo().toLowerCase().equals("cmnet")){
				return NetType.CMNET;
			}else{
				return NetType.CMWAP;
			}
		}else if(nType == ConnectivityManager.TYPE_WIFI){
			return NetType.WIFI;
		}
		
		return NetType.NONENET;
	}
	
}
