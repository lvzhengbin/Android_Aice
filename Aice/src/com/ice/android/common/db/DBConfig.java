package com.ice.android.common.db;

import android.content.Context;

/**
 * sqlite本地数据库配置类
 * @author ice
 *
 */
public class DBConfig {
	/** 上下文 */
	private Context context;
	/** 默认数据库名称为 cursys.db，我们也可通过setDbName()方法设置取名数据库名    */
	private String dbName = "aice.db";
	/** 数据库版本  */
	private int dbVersion = 2;
	/** 是否是调试模式（调试模式 增删改查的时候显示SQL语句） 默认开启调试模式　*/
	private boolean debug = true;
	/** 数据库版本更新监听  */
	private DbUpdateListener dbUpdateListener;

	
	// ----get/set方法----
	public Context getContext() {
		return context;
	}
	
	public void setContext(Context context) {
		this.context = context;
	}
	
	public String getDbName() {
		return dbName;
	}
	
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	public int getDbVersion() {
		return dbVersion;
	}
	
	public void setDbVersion(int dbVersion) {
		this.dbVersion = dbVersion;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public DbUpdateListener getDbUpdateListener() {
		return dbUpdateListener;
	}
	
	public void setDbUpdateListener(DbUpdateListener dbUpdateListener) {
		this.dbUpdateListener = dbUpdateListener;
	}
	
	
}
