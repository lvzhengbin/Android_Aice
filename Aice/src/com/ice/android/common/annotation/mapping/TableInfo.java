package com.ice.android.common.annotation.mapping;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import com.ice.android.common.utils.AnnotationUtil;
import com.ice.android.common.utils.FieldUtil;

/**
 * 通过注解映射的表信息对象
 * @author ice
 * 
 */
public class TableInfo {

	/** 表信息所对应的实体类名  */
	private String className;
	/** 表信息所对应的表名  */
	private String tableName;
	/** 表信息对应的主键字段   */
	private KeyProperty keyProperty;
	/** 表信息所对应的属性集合  */
	// public static final HashMap<String, Property> propertyMap = new HashMap<String, Property>();
	public final HashMap<String, Property> propertyMap = new HashMap<String, Property>();
	
	public static final HashMap<String,TableInfo> tableInfoMap = new HashMap<String,TableInfo>();
	
	/** 在对实体进行数据库操作的时候查询是否已经有表了，只需查询一遍，用此标示  */
	private boolean checkDatabese;
	
	/**
	 * 根据实体类得到映射的表信息
	 * @param clazz
	 * @return
	 */
	public static TableInfo getTableInfo(Class<?> clazz){
		if(clazz == null){
			throw new RuntimeException("table info get error,because the clazz is null");
		}
		TableInfo tableInfo = tableInfoMap.get(clazz.getName());
		if(tableInfo == null){
			tableInfo = new TableInfo();
			
			tableInfo.setClassName(clazz.getName());
			tableInfo.setTableName(AnnotationUtil.getTableName(clazz));
			
			Field keyField = AnnotationUtil.getPrimaryKeyField(clazz); 
			if(keyField == null){
				throw new RuntimeException("the class["+clazz+"]'s keyField is null , \n you can define _id,id property or use annotation @Key to solution this exception");
			}else{
				KeyProperty key = new KeyProperty();
				key.setField(keyField);
				key.setFieldName(keyField.getName());
				key.setColumnName(FieldUtil.getColumnByField(keyField));
				key.setDataType(keyField.getType());
				key.setSet(FieldUtil.getFieldSetMethod(clazz, keyField));
				key.setGet(FieldUtil.getFieldGetMethod(clazz, keyField));
				
				tableInfo.setKeyProperty(key);
			}
			
			List<Property> propertyList = AnnotationUtil.getPropertyList(clazz);
			if(propertyList != null){
				for(Property property : propertyList){
					if(property != null){
						tableInfo.propertyMap.put(property.getColumnName(), property);
					}
				}
			}
			
			tableInfoMap.put(clazz.getName(), tableInfo);
		}
		
		return tableInfo;
	}
	
	
	/************** get/set方法  ******************/
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public KeyProperty getKeyProperty() {
		return keyProperty;
	}

	public void setKeyProperty(KeyProperty keyProperty) {
		this.keyProperty = keyProperty;
	}

	public boolean isCheckDatabese() {
		return checkDatabese;
	}

	public void setCheckDatabese(boolean checkDatabese) {
		this.checkDatabese = checkDatabese;
	}  
	
	
}
