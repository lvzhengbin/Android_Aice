package com.ice.android.common.core;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ice.android.common.annotation.mapping.KeyValue;
import com.ice.android.common.annotation.mapping.TableInfo;
import com.ice.android.common.db.CursorUtils;
import com.ice.android.common.db.DBConfig;
import com.ice.android.common.db.DbUpdateListener;
import com.ice.android.common.db.SqlBuilder;
import com.ice.android.common.db.SqlInfo;
import com.ice.android.common.db.SqliteDbHelper;
import com.ice.android.common.utils.AnnotationUtil;
/**
 * 清爽的DB<br>
 * <b>实例用法:</b><br>
 * 第一步：创建/获取 AiceDb对象  这里有多种创建方式<br>
 * 如：AiceDb icedb = AiceDb.getIceDb(context);<br>
 * 第二步：创建需要操作的实体对象<br>
 * 如：Person person = new Person("ice", 24, "男", false, true);<br>
 * 第三步：调用AiceDb类中方法  对数据库进行CURD操作<br>
 * 如：保存实体对象到数据库 --->  icedb.save(person)
 * 
 * @author ice
 *
 */
public class AiceDb {

	private static final String TAG = "IceDb";
	
	//private static HashMap<String, AiceDb> daoMap = new HashMap<String, AiceDb>();
	private static AiceDb iceDb;
	private static DBConfig config;
	private static SqliteDbHelper dbHelper;
	private static final String RW = "rw";
	private static final String ASC = "ASC";
	private static final String DESC = "DESC";
	
	
	private AiceDb(DBConfig config){
		dbHelper = new SqliteDbHelper(config.getContext(), 
				       config.getDbName(), 
				       config.getDbVersion(), 
				       config.getDbUpdateListener());
		
		this.config = config;
	}
	
	
	private synchronized static AiceDb getInstance(DBConfig config){
		if(iceDb == null){
			iceDb = new AiceDb(config);
		}
		return iceDb;
	}
	
	
	/**
	 * 创建/返回默认的IceDb对象<br>
	 * 默认的数据库名:aice.db
	 * @param context
	 * @return
	 */
	public static AiceDb getIceDb(Context context){
		DBConfig config = new DBConfig();
		config.setContext(context);
		return getIceDb(config);
	}
	
	
	/**
	 * 创建/返回默认的IceDb对象  默认的数据库名:aice.db
	 * @param context
	 * @param isLogSql  是否启用log模式来控制sql语句的打印  true:打印sql语句 | false：不打印sql语句
	 * @return
	 */
	public static AiceDb getIceDb(Context context,boolean isLogSql){
		DBConfig config = new DBConfig();
		config.setContext(context);
		config.setDebug(isLogSql);
		return getIceDb(config);
	}
	
	
	/**
	 * 创建/返回自命名数据库名的IceDb对象
	 * @param context
	 * @param dbName  自命名数据库名称
	 * @param isLogSql 是否启用log模式来控制sql语句的打印  true:打印sql语句 | false：不打印sql语句
	 * @return
	 */
	public static AiceDb getIceDb(Context context,String dbName,boolean isLogSql){
		DBConfig config = new DBConfig();
		config.setContext(context);
		config.setDbName(dbName);
		config.setDebug(isLogSql);
		return getIceDb(config);
	}
	
	
	/**
	 * 创建/返回更新数据库名、数据库版本号的IceDb对象
	 * @param context
	 * @param dbName  数据库名称
	 * @param dbVersion  数据库版本号
	 * @param dbUpdateListener  数据库版本更新监听
	 * @param isLogSql  是否启用log模式来控制sql语句的打印  true:打印sql语句 | false：不打印sql语句
	 * @return
	 */
	public static AiceDb getIceDb(Context context,String dbName,int dbVersion,DbUpdateListener dbUpdateListener,boolean isLogSql){
		DBConfig config = new DBConfig();
		config.setContext(context);
		config.setDbName(dbName);
		config.setDbVersion(dbVersion);
		config.setDbUpdateListener(dbUpdateListener);
		config.setDebug(isLogSql);
		return getIceDb(config);
	}
	
	
	/**
	 * 通过DBConfig配置内容   创建/返回IceDb对象
	 * @param config  数据库配置对象
	 * @return
	 */
	public static AiceDb getIceDb(DBConfig config){
		return getInstance(config);
	}
	
	
	private SQLiteDatabase getWritableDB(){
		SQLiteDatabase wdb = dbHelper.getWritableDatabase();
		Log.d(TAG, "Writable数据库已打开");
		return wdb;
	}
	
	
	private SQLiteDatabase getReadableDB(){
		SQLiteDatabase rdb = dbHelper.getReadableDatabase();
		Log.d(TAG, "Readable数据库已打开");
		return rdb;
	}
	
	
	private void closeDb(SQLiteDatabase db){
		if(db != null && db.isOpen()){
			db.close();
		}
		Log.d(TAG, "数据库已关闭");
	}
	
	
	/**
	 * 保存/插入数据到实体entity对应的数据库表<br/>
	 * @param entity 对应数据库表的实体类
	 */
	public void save(Object entity){
		checkTableExist(entity.getClass());
		execSqlInfo(SqlBuilder.buildInsertSql(entity),RW);
	}
 
	
	/**
	 * 保存/插入数据到实体entity对应的数据库表<br/>
	 * <b>注意：</b><br/>
	 * 保存成功后，entity的主键将被赋值（或更新）为数据库的主键值， 只针对自增长的id有效
	 * @param entity 要保存的实体对象
	 * @return ture： 保存成功    false:保存失败
	 */
	public boolean saveBindId(Object entity){
		checkTableExist(entity.getClass());
		List<KeyValue> entityKvList = AnnotationUtil.getSaveKeyValueListByEntity(entity);
		if(entityKvList != null && entityKvList.size() > 0){
			TableInfo tableInfo = TableInfo.getTableInfo(entity.getClass());
			ContentValues cv = new ContentValues();
			insertContentValues(cv,entityKvList);
			SQLiteDatabase db = getWritableDB();
			long id = db.insert(tableInfo.getTableName(), null, cv);
			closeDb(db);
			if(id == -1){
				 return false;
			}
			// 保存成功后，将数据库表自增的_id主键的值赋给entity实体对象
			tableInfo.getKeyProperty().setValue(entity, id);
			return true;
		}
		return false;
	}
	
	
	/**
	 * 把List<KeyValue>数据存储到ContentValues
	 * @param cv
	 * @param entityKvList
	 */
	private void insertContentValues(ContentValues cv, List<KeyValue> entityKvList) {
		if(cv != null && entityKvList != null){
			for(KeyValue kv : entityKvList){
				cv.put(kv.getKey(), kv.getValue().toString());
			}
		}else{
			Log.w(TAG, "insertContentValues: List<KeyValue> is empty or ContentValues is empty!");
		}
	}


	/**
	 * 更新数据（主键ID不能为空）
	 * @param entity
	 */
	public void update(Object entity){
		checkTableExist(entity.getClass());
		execSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity), RW);
	}
	
	
	/**
	 * 根据where条件更新数据
	 * @param entity 需要更新数据的实体对象
	 * @param strWhere where条件
	 */
	public void update(Object entity, String strWhere){
		checkTableExist(entity.getClass());
		execSqlInfo(SqlBuilder.getUpdateSqlAsSqlInfo(entity, strWhere), RW);	
	}
	
	
	/**
	 * 删除数据   <br/>
	 * <b>注意:</b><br/>
	 * entity的主键不能为空
	 * @param entity
	 */
	public void delete(Object entity){
		checkTableExist(entity.getClass());
		execSqlInfo(SqlBuilder.buildDeleteSql(entity), RW);
	}
	
	
	/**
	 * 根据主键删除数据
	 * @param clazz
	 * @param id
	 */
	public void deleteById(Class<?> clazz,Object id){
		checkTableExist(clazz);
		execSqlInfo(SqlBuilder.buildDeleteSqlById(clazz, id), RW);
	}
	
	
	/**
	 * 根据条件删除数据
	 * @param entity
	 * @param strWhere
	 */
	public void deleteByWhere(Class<?> clazz,String strWhere){
		checkTableExist(clazz);
		String sql = SqlBuilder.buildDeleteSqlByWhere(clazz, strWhere);
		SQLiteDatabase db = getWritableDB();
		debugSql(sql);
		db.execSQL(sql);
		closeDb(db);
	}
	
	
	/**
	 * 删除表的所有数据
	 * @param clazz
	 */
	public void deleteAll(Class<?> clazz){
		checkTableExist(clazz);
		String sql = SqlBuilder.buildDeleteSqlByWhere(clazz, null);
		SQLiteDatabase db = getWritableDB();
		debugSql(sql);
		db.execSQL(sql);
		closeDb(db);
	}
	
	
	/**
	 * 删除指定的表
	 * @param clazz
	 */
	public void dropTable(Class<?> clazz){
		checkTableExist(clazz);
		String sql = SqlBuilder.buildDropTableSql(clazz);
		SQLiteDatabase db = getWritableDB();
		debugSql(sql);
		db.execSQL(sql);
		closeDb(db);
	}

	
	/**
	 * 查找clazz对应数据表的所有数据
	 * @param clazz
	 * @return
	 */
	public <T> List<T> queryAll(Class<T> clazz){
		return queryAllBySql(clazz, SqlBuilder.getSelectSql(clazz));
	}
	
	
	/**
	 * 查找clazz对应数据表的所有数据 -- 支持排序
	 * @param clazz
	 * @param orderBy   排序字段
	 * @param orderType   排序方式: DESC | ASC
	 * @return
	 */
	public <T> List<T> queryAll(Class<T> clazz,String orderBy,String orderType){
		return queryAllBySql(clazz, SqlBuilder.getSelectSql(clazz) + " ORDER BY " + orderBy + orderType);
	}
	
	
	/**
	 * 根据条件查找所有数据
	 * @param clazz
	 * @param strWhere
	 * @return
	 */
	public <T> List<T> queryAllByWhere(Class<T> clazz,String strWhere){
		return queryAllBySql(clazz, SqlBuilder.getSelectSqlByWhere(clazz, strWhere));
	}
	
	
	/**
	 * 根据条件查找所有数据 --支持排序
	 * @param clazz
	 * @param strWhere
	 * @param orderBy   排序字段
	 * @param orderType   排序方式: DESC | ASC
	 * @return
	 */
	public <T> List<T> queryAllByWhere(Class<T> clazz,String strWhere,String orderBy,String orderType){
		return queryAllBySql(clazz, SqlBuilder.getSelectSqlByWhere(clazz, strWhere) + " ORDER BY " + orderBy + orderType);
	}
	
	
	/**
	 * 根据指定sql语句查询clazz对应数据表的所有数据
	 * @param clazz
	 * @param sql
	 * @return
	 */
	public <T> List<T> queryAllBySql(Class<T> clazz, String sql){
		checkTableExist(clazz);
		SQLiteDatabase db = getReadableDB();
		debugSql(sql);
		Cursor cursor = db.rawQuery(sql, null);
		try {
			List<T> list = new ArrayList<T>();
			while(cursor.moveToNext()){
				T entity = CursorUtils.getEntity(cursor, clazz);
				list.add(entity);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor != null){
				cursor.close();
			}
			closeDb(db);
		}
		return null;
	}
	
	
	/**
	 * 根据主键查找数据
	 * @param id
	 * @param clazz
	 * @return
	 */
	public <T> T queryById(Object id, Class<T> clazz){
		checkTableExist(clazz);
		SqlInfo sqlInfo = SqlBuilder.getSelectSqlAsSqlInfo(clazz, id);
		if(sqlInfo != null){
			SQLiteDatabase db = getReadableDB();
			debugSql(sqlInfo.getSql());
			Cursor cursor = db.rawQuery(sqlInfo.getSql(), sqlInfo.getBindArgsAsStringArray());
			try {
				if(cursor.moveToNext()){
					return CursorUtils.getEntity(cursor, clazz);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(cursor != null){
					cursor.close();
				}
				closeDb(db);
			}
		}
		return null;
	}
	
	
	/**
	 * 删除所有的表数据
	 */
	public void dropDb(){
		SQLiteDatabase db = getWritableDB();
		Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
		if(cursor != null){
			while(cursor.moveToNext()){
				db.execSQL("DROP TABLE " + cursor.getString(0));
			}
		}
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
		closeDb(db);
	}
	
	
	/**
	 * 检查实体类clazz对应的数据库表是否存在<br/>
	 * 若不存在 则根据实体类在数据库中立即创建表
	 * @param clazz
	 */
	private void checkTableExist(Class<? extends Object> clazz) {
		if(!tableIsExist(TableInfo.getTableInfo(clazz))){
			String creatTableSQL = SqlBuilder.getCreatTableSQL(clazz);
			SQLiteDatabase db = getWritableDB();
			debugSql(creatTableSQL);
			db.execSQL(creatTableSQL);
			closeDb(db);
		}
	}


	/**
	 * 检查数据库中TableInfo对应的表信息是否存在
	 * @param tableInfo
	 * @return
	 */
	private boolean tableIsExist(TableInfo tableInfo) {
		if(tableInfo.isCheckDatabese()){
			return true;
		}
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='"
					+ tableInfo.getTableName() + "' ";
			db = getReadableDB();
			debugSql(sql);
			cursor = db.rawQuery(sql, null);
			if(cursor != null && cursor.moveToNext()){
				if(cursor.getInt(0) > 0){
					tableInfo.setCheckDatabese(true);  // 设置为已检查过     貌似不会起作用
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(cursor != null){
				cursor.close();  // 关闭游标
				cursor = null;
			}
			if(db != null){
				closeDb(db);     // 关闭数据库
			}
		}
		return false;
	}
	
	
	
	/**
	 * 执行sql语句
	 * @param sqlInfo  sql语句的封装对象
	 * @param rw  "rw"：表示打开可写数据库 执行sql语句  如：插入、更新、删除操作 <br/> 
	 *  该参数为null时表示 打开可读数据库执行sql语句  如：查询操作
	 */
	private void execSqlInfo(SqlInfo sqlInfo,String rw) {
		if(sqlInfo != null){
			SQLiteDatabase db = null;
			if("rw".equalsIgnoreCase(rw)){
				db = getWritableDB();
				db.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
			}else{
				db = getReadableDB();
				db.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgsAsArray());
			}
			debugSql(sqlInfo.getSql());
			closeDb(db);
		}else{
			Log.w(TAG, "sql exec error:sqlInfo is null");
		}
	}

	
	/**
	 * 用来输出debug模式的sql执行语句
	 * @param sql
	 */
	private void debugSql(String sql){
		if(config != null && config.isDebug()){
			Log.d("Debug SQL", ">>>> "+sql);
		}
	}
	
}
