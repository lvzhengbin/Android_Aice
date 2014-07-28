package com.ice.android.common.net;

import java.util.ArrayList;

import com.ice.android.common.utils.NetWorkHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
/**
 * 检测网络状态改变的广播接收器  <br>
 * 在网络状态改变监听过程<观察者模式>中  我们可以把他看做是一个被观察者 <br>
 * @Description 是一个检测网络状态改变的，需要配置 
 *              <receiver android:name="com.ice.android.common.net.NetWorkStateReceiver" >
 *                 <intent-filter> 
 *                       <action android:name="android.net.conn.CONNECTIVITY_CHANGE" /> 
 *                 </intent-filter> 
 *              </receiver>
 *              需要开启的权限有：
 *              <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
 *              <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 *              <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 *              <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * @author ice
 *
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

	private static final String TAG = "NetWorkStateReceiver";
	private static final String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	
	/** 储存所有的网络状态观察者集合   */
	private static ArrayList<NetChangeObserver> netChangeObserverArrayList = new ArrayList<NetChangeObserver>();
	private static boolean networkAvailable = true;
	private NetType netType;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)){
			Log.d(TAG, "网络状态发生了改变...");
			NetWorkHelper netWorkHelper = NetWorkHelper.getInstance(context);
			if(!netWorkHelper.isNetworkAvailable()){
				networkAvailable = false;
				Log.d(TAG, "网络连接断开...");
			}else{
				netType = netWorkHelper.getAPNType();
				networkAvailable = true;
				Log.d(TAG, "网络连接成功..."+"| 当前的网络类型为: "+netType);
			}
			// 通知所有注册了的网络状态观察者
			notifyObserver();
		}
	}
	
	
	/**
	 * 添加/注册网络连接状态观察者
	 * @param observer
	 */
	public static void registerNetStateObserver(NetChangeObserver observer){
		if(netChangeObserverArrayList == null){
			netChangeObserverArrayList = new ArrayList<NetChangeObserver>();
		}
		netChangeObserverArrayList.add(observer);
	}
	
	
	/**
	 * 删除/注销网络连接状态观察者
	 * @param observer
	 */
	public static void unRegisterNetStateObserver(NetChangeObserver observer){
		if(netChangeObserverArrayList != null){
			netChangeObserverArrayList.remove(observer);
		}
	}
	

	/**
	 * 向所有的观察者发送通知：网络状态发生改变咯...
	 */
	private void notifyObserver(){
		if(netChangeObserverArrayList !=null && netChangeObserverArrayList.size() >0){
			for(NetChangeObserver observer : netChangeObserverArrayList){
				if(observer != null){
					if(networkAvailable){
						observer.OnConnect(netType);
					}else{
						observer.OnDisConnect();
					}
				}
			}
		}
	}
	
	
}
