package com.ice.android.common.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * 
 * @author ice<br>
 * 
 * Note:关于SQLiteOpenHelper的子类封装   我有两种想法<br>
 * 想法一： 在onCreate()方法里 将所有要创建的表在应用第一次启动时全部创建<br><br>
 * 
 * 想法二：不实现onCreate()方法，转而将表的创建放到应用需要时，<br>
 * 比如需要往某表插入数据时，先去检查下是否表已存在  若表还不存在 则执行建表语句<br>
 *
 */
public class SqliteDbHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "SqliteDbHelper";
	private DbUpdateListener mDbUpdateListener;
	
	public SqliteDbHelper(Context context, String name, int version, DbUpdateListener dbUpdateListener) {
		super(context, name, null, version);
		mDbUpdateListener = dbUpdateListener;
	}
	
	
	/**
	 * 如果按照我的想法一来处理，则这里执行所有建表语句
	 * 如果按照我的想法二来处理，则这里不需要实现
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "onCreate(SQLiteDatabase db)被调用,但是这里不执行建表语句");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(mDbUpdateListener != null){
			mDbUpdateListener.onUpgrade(db, oldVersion, newVersion);
		}else{
			dropDb(db);
		}
	}
	
	
	/**
	 * 删除所有表信息
	 */
	private void dropDb(SQLiteDatabase db){
		Cursor rawQuery = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
		if(rawQuery != null){
			while(rawQuery.moveToNext()){
				db.execSQL("DROP TABLE "+ rawQuery.getString(0));
				Log.d(TAG, "DROP TABLE "+ rawQuery.getString(0));
			}
			rawQuery.close();
			rawQuery =  null;
		}
	}
	

}
