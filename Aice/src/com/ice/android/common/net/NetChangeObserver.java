package com.ice.android.common.net;
/**
 * 监听网络状态改变的观察者
 * @author ice
 *
 */
public interface NetChangeObserver {

	/**
	 * 网络状态连接时调用
	 */
	public void OnConnect(NetType netType);
	
	/**
	 * 网络状态断开时调用
	 */
	public void OnDisConnect();
	
}
