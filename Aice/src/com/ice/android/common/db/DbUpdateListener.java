package com.ice.android.common.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库版本更新监听接口
 * @author ice
 *
 */
public interface DbUpdateListener {

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
