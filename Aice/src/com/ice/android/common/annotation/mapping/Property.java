package com.ice.android.common.annotation.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import com.ice.android.common.utils.FieldUtil;

/**
 * 通过映射的表字段属性信息<br>
 * 【非主键】的【基本数据类型】 都是属性
 * @author ice
 *
 */
public class Property {

	/** Property所代表的 Field对象  */
	private Field field;
	
	/** 实体对应的字段属性名  */
	private String fieldName;
	
	/** 实体字段对应表的字段名  */
	private String columnName;
	
	/** 默认值  */
	private String defaultValue; 
	
	/** 表字段类型   <sqlite数据表的字段类型> */
	private Class<?> dataType; 
	
	private Method get;
	
	private Method set;

	/**
	 * 获得实体字段的值
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getValue(Object receiver){
		if(receiver != null && get != null){
			try {
				return (T) get.invoke(receiver);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 设置实体字段的值
	 * @param receiver
	 * @param value
	 */
	public void setValue(Object receiver , Object value){
		if(value != null && set != null){
			try {
				if(dataType == String.class){
					set.invoke(receiver, value.toString());
				}else if(dataType == int.class || dataType == Integer.class){
					set.invoke(receiver, value == null ? (Integer) null : Integer.parseInt(value.toString()));
				}else if(dataType == float.class || dataType == Float.class){
					set.invoke(receiver, value == null ? (Float) null: Float.parseFloat(value.toString()));
				} else if (dataType == double.class || dataType == Double.class) {
					set.invoke(receiver, value == null ? (Double) null: Double.parseDouble(value.toString()));
				} else if (dataType == long.class || dataType == Long.class) {
					set.invoke(receiver, value == null ? (Long) null: Long.parseLong(value.toString()));
				} else if (dataType == java.util.Date.class || dataType == java.sql.Date.class) {
					set.invoke(receiver, value == null ? (Date) null: FieldUtil.stringToDateTime(value.toString()));
				} else if (dataType == boolean.class || dataType == Boolean.class) {
					set.invoke(receiver, value == null ? (Boolean) null: "1".equals(value.toString()));
				} else {
					set.invoke(receiver, value);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
				field.setAccessible(true);  // 反射的对象在使用时应该取消 Java 语言访问检查  ?
				field.set(receiver, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/***********  get/set方法  ***************************/
	public Field getField() {
		return field;
	}
	
	public void setField(Field field) {
		this.field = field;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Class<?> getDataType() {
		return dataType;
	}

	public void setDataType(Class<?> dataType) {
		this.dataType = dataType;
	}

	public Method getGet() {
		return get;
	}

	public void setGet(Method get) {
		this.get = get;
	}

	public Method getSet() {
		return set;
	}

	public void setSet(Method set) {
		this.set = set;
	}

	
}
