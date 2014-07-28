package com.ice.android.common.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;

import com.ice.android.common.annotation.mapping.KeyProperty;
import com.ice.android.common.annotation.mapping.KeyValue;
import com.ice.android.common.annotation.mapping.Property;
import com.ice.android.common.annotation.mapping.TableInfo;
import com.ice.android.common.utils.AnnotationUtil;

/**
 * sql语句组装类
 * @author ice
 *
 */
public class SqlBuilder {

	private static final String TAG = "SqlBuilder";
	/**
	 * 根据实体类 组装建表sql语句
	 * @param clazz
	 * @return
	 */
	public static String getCreatTableSQL(Class<?> clazz){
		TableInfo tableInfo = TableInfo.getTableInfo(clazz);
		KeyProperty keyProperty = tableInfo.getKeyProperty();
		
		StringBuffer strSql = new StringBuffer();
		strSql.append("CREATE TABLE IF NOT EXISTS ")
		      .append(tableInfo.getTableName())
		      .append(" ( ");
		
		Class<?> primaryClazz = keyProperty.getDataType();
		if(primaryClazz == int.class || primaryClazz==Integer.class 
				|| primaryClazz == long.class || primaryClazz == Long.class){
			strSql.append(keyProperty.getColumnName()).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
		}else{
			strSql.append(keyProperty.getColumnName()).append(" TEXT PRIMARY KEY,");
		}
		
		Collection<Property> propertys = tableInfo.propertyMap.values();
		for(Property property : propertys){
			strSql.append(property.getColumnName());
			Class<?> dataType = property.getDataType();
			if(dataType== int.class || dataType == Integer.class 
			   || dataType == long.class || dataType == Long.class){
				strSql.append(" INTEGER");
			}else if(dataType == float.class ||dataType == Float.class 
					||dataType == double.class || dataType == Double.class){
				strSql.append(" REAL");
			}else if(dataType == boolean.class || dataType == Boolean.class){
				strSql.append(" NUMERIC");
			}else{
				strSql.append(" TEXT");
			}
			strSql.append(",");
		}
		
		// 删除最后一个 ","
		strSql.deleteCharAt(strSql.length() - 1);
		strSql.append(" )");
		Log.d(TAG, "CreatTableSQL:"+strSql.toString());
		return strSql.toString();
	}
	
	
	/**
	 * 获取插入实体对象数据到对应数据表的sql语句
	 * @param entity
	 * @return
	 */
	public static SqlInfo buildInsertSql(Object entity){
		SqlInfo sqlInfo = null;
		StringBuffer strSQL = new StringBuffer();
		List<KeyValue> keyValueList = AnnotationUtil.getSaveKeyValueListByEntity(entity);
		if(keyValueList != null && keyValueList.size() >0){
			sqlInfo = new SqlInfo();
			strSQL.append("INSERT INTO ");
			strSQL.append(TableInfo.getTableInfo(entity.getClass()).getTableName());
			strSQL.append(" (");
			for(KeyValue kv : keyValueList){
				strSQL.append(kv.getKey()).append(",");
				sqlInfo.addVaule(kv.getValue());
			}
			strSQL.deleteCharAt(strSQL.length() - 1);
			strSQL.append(") VALUES ( ");
			int length = keyValueList.size();
			for(int i =0 ; i < length;i++){
				strSQL.append("?,");
			}
			strSQL.deleteCharAt(strSQL.length() - 1);
			strSQL.append(")");
			
			sqlInfo.setSql(strSQL.toString());
		}
		
		return sqlInfo;
	}
	
	
	/**
	 * where条件根据   _id主键   来获得更新语句的sqlinfo对象  
	 * @param entity
	 * @return
	 */
	public static SqlInfo getUpdateSqlAsSqlInfo(Object entity){
		TableInfo tableInfo = TableInfo.getTableInfo(entity.getClass());
		Object idValue = tableInfo.getKeyProperty().getValue(entity);
		if(idValue == null){
			throw new RuntimeException("this entity["+entity.getClass()+"]'s id value is null");
		}
		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		Collection<Property> propertys = tableInfo.propertyMap.values();
		for(Property property : propertys){
			KeyValue kv = AnnotationUtil.property2KeyValue(property, entity);
			if(kv != null){
				keyValueList.add(kv);
			}
		}
		
		if(keyValueList.size() == 0){ 
			return null;
		}
		SqlInfo sqlInfo = new SqlInfo();
		StringBuffer strSQL = new StringBuffer("UPDATE ");
		strSQL.append(tableInfo.getTableName());
		strSQL.append(" SET");
		for(KeyValue kValue : keyValueList){
			strSQL.append(kValue.getKey()).append("=?,");
			sqlInfo.addVaule(kValue.getValue());
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		strSQL.append(" WHERE ").append(tableInfo.getKeyProperty().getColumnName()).append("=?");
		sqlInfo.addVaule(idValue);
		sqlInfo.setSql(strSQL.toString());
		return sqlInfo;
	}
	
	
	/**
	 * 根据 where条件strWhere 来获得更新语句的sqlinfo对象  
	 * @param entity
	 * @param strWhere
	 * @return
	 */
	public static SqlInfo getUpdateSqlAsSqlInfo(Object entity,String strWhere){
		TableInfo tableInfo = TableInfo.getTableInfo(entity.getClass());
		Object idValue = tableInfo.getKeyProperty().getValue(entity);
		if(idValue == null){
			throw new RuntimeException("this entity["+entity.getClass()+"]'s id value is null");
		}
		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		Collection<Property> propertys = tableInfo.propertyMap.values();
		for(Property property : propertys){
			KeyValue kv = AnnotationUtil.property2KeyValue(property, entity);
			if(kv != null){
				keyValueList.add(kv);
			}
		}
		
		if(keyValueList.size() == 0){ 
			return null;
		}
		SqlInfo sqlInfo = new SqlInfo();
		StringBuffer strSQL = new StringBuffer("UPDATE ");
		strSQL.append(tableInfo.getTableName());
		strSQL.append(" SET");
		for(KeyValue kValue : keyValueList){
			strSQL.append(kValue.getKey()).append("=?,");
			sqlInfo.addVaule(kValue.getValue());
		}
		strSQL.deleteCharAt(strSQL.length() - 1);
		if(!TextUtils.isEmpty(strWhere)){
			strSQL.append(" WHERE ").append(strWhere);
		}
		sqlInfo.setSql(strSQL.toString());
		return sqlInfo;
	}
	
	
	private static String getDeleteSqlBytableName(String tableName){
		return "DELETE FROM "+ tableName;
	}
	
	
	/**
	 * 获取根据实体对象id主键 删除实体对应数据表的sqlInfo对象 
	 * @param entity
	 */
	public static SqlInfo buildDeleteSql(Object entity){
		TableInfo tableInfo = TableInfo.getTableInfo(entity.getClass());
		Object idValue = tableInfo.getKeyProperty().getValue(entity);
		if(idValue == null){
			throw new RuntimeException("getDeleteSQL:"+entity.getClass()+" id value is null");
		}
		StringBuffer SqlStr = new StringBuffer(getDeleteSqlBytableName(tableInfo.getTableName()));
		SqlStr.append(" WHERE ").append(tableInfo.getKeyProperty().getColumnName()).append("=?");
		SqlInfo sqlInfo = new SqlInfo();
		sqlInfo.addVaule(idValue);
		sqlInfo.setSql(SqlStr.toString());
		return sqlInfo;
	}
	
	
	/**
	 * 根据实体的class类名和id值来获取删除的sqlInfo对象
	 * @param clazz
	 * @param idValue
	 * @return
	 */
	public static SqlInfo buildDeleteSqlById(Class<?> clazz,Object idValue){
		TableInfo tableInfo = TableInfo.getTableInfo(clazz);
		KeyProperty keyProperty = tableInfo.getKeyProperty();
		if(idValue == null){
			throw new RuntimeException("getDeleteSQL:idValue is null");
		}
		StringBuffer SqlStr = new StringBuffer(getDeleteSqlBytableName(tableInfo.getTableName()));
		SqlStr.append(" WHERE ").append(keyProperty.getColumnName()).append("=?");
		
		SqlInfo sqlInfo = new SqlInfo();
		sqlInfo.addVaule(idValue);
		sqlInfo.setSql(SqlStr.toString());
		
		return sqlInfo;
	}
	
	
	/**
	 * 根据条件删除数据 ，条件为空的时候将会删除所有的数据
	 * @param clazz
	 * @param strWhere
	 * @return
	 */
	public static String buildDeleteSqlByWhere(Class<?> clazz,String strWhere){
		TableInfo tableInfo = TableInfo.getTableInfo(clazz);
		StringBuffer sqlStr = new StringBuffer(getDeleteSqlBytableName(tableInfo.getTableName()));
		if(!TextUtils.isEmpty(strWhere)){
			sqlStr.append(" WHERE ").append(strWhere);
		}
		return sqlStr.toString();
	}
	
	
	/**
	 * 删除指定表的sql语句
	 * @param clazz
	 * @return
	 */
	public static String buildDropTableSql(Class<?> clazz){
	    TableInfo tableInfo = TableInfo.getTableInfo(clazz);
		String sql = "DROP TABLE " + tableInfo.getTableName();
		return sql;
	}
	
	
	private static String getSelectSqlByTableName(String tableName){
		return new StringBuffer("SELECT * FROM ").append(tableName).toString();
	}
	
	
	/**
	 * 查询实体对象对应表的所有数据的sql语句
	 * @param clazz
	 * @return
	 */
	public static String getSelectSql(Class<?> clazz){
		return getSelectSqlByTableName(TableInfo.getTableInfo(clazz).getTableName());
	}
	
	
	/**
	 * 根据主键id查询条件的sql语句
	 * @param clazz
	 * @param idValue
	 * @return
	 */
	public static String getSelectSqlAsString(Class<?> clazz,Object idValue){
		TableInfo tableInfo = TableInfo.getTableInfo(clazz);
		StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(tableInfo.getTableName()));
		strSQL.append(" WHERE ");
		strSQL.append(getPropertyStrSql(tableInfo.getKeyProperty().getColumnName(), idValue));
		
		return strSQL.toString();
	}
	
	
	/**
	 * 根据主键查询条件的sqlInfo对象
	 * @param clazz
	 * @param idValue
	 * @return
	 */
	public static SqlInfo getSelectSqlAsSqlInfo(Class<?> clazz,Object idValue){
		TableInfo tableInfo = TableInfo.getTableInfo(clazz);
		StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(tableInfo.getTableName()));
		strSQL.append(" WHERE ")
		      .append(tableInfo.getKeyProperty().getColumnName())
		      .append("=?");
		SqlInfo sqlInfo = new SqlInfo();
		sqlInfo.setSql(strSQL.toString());
		sqlInfo.addVaule(idValue);
		return sqlInfo;
	}
	
	
	/**
	 * 根据指定查询条件的sql语句
	 * @param clazz
	 * @param strWhere
	 * @return
	 */
	public static String getSelectSqlByWhere(Class<?> clazz,String strWhere){
		TableInfo tableInfo = TableInfo.getTableInfo(clazz);
		StringBuffer strSQL = new StringBuffer(getSelectSqlByTableName(tableInfo.getTableName()));
		if(!TextUtils.isEmpty(strWhere)){
			strSQL.append(" WHERE ").append(strWhere);
		}
		return strSQL.toString();
	}
	
	
	/**
	 * @param key
	 * @param value
	 * @return eg1: name='ice'  eg2: age=24
	 */
	private static String getPropertyStrSql(String key,Object value){
		StringBuffer sbSQL = new StringBuffer(key).append("=");
		if(value instanceof String || value instanceof java.util.Date || value instanceof java.sql.Date){
			sbSQL.append("'").append(value).append("'");
		}else{
			sbSQL.append(value);
		}
		return sbSQL.toString();
	}
	
	
}
