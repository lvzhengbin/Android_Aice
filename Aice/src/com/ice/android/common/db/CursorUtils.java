package com.ice.android.common.db;

import com.ice.android.common.annotation.mapping.Property;
import com.ice.android.common.annotation.mapping.TableInfo;

import android.database.Cursor;

/**
 * 
 * @author ice
 *
 */
public class CursorUtils {
	
	/**
	 * 将数据库查询获得的游标对象转换成实体对象
	 * @param cursor
	 * @param clazz
	 * @return
	 */
	public static <T> T getEntity(Cursor cursor,Class<T> clazz){
		if(cursor != null){
			try {
				TableInfo tableInfo = TableInfo.getTableInfo(clazz);
				int columnCount = cursor.getColumnCount();
				if(columnCount > 0){
					T entity = clazz.newInstance();
					for(int i=0;i<columnCount;i++){
						String columnName = cursor.getColumnName(i);
						Property property = tableInfo.propertyMap.get(columnName);
						if(property != null){
							property.setValue(entity, cursor.getString(i));
						}else{
							if(tableInfo.getKeyProperty().getColumnName().equals(columnName)){
								tableInfo.getKeyProperty().setValue(entity, cursor.getString(i));
							}
						}
					}
					return entity;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	

}
