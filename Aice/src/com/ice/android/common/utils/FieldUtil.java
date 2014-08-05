package com.ice.android.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ice.android.common.annotation.Column;
import com.ice.android.common.annotation.Key;
import com.ice.android.common.annotation.Transient;

public class FieldUtil {

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 判断该字段是否是基本数据类型
	 * @param field
	 * @return
	 */
	public static boolean isBaseDateType(Field field){
		Class<?> type = field.getType();
		return type.equals(String.class) ||
			   type.equals(Integer.class)||  
			   type.equals(Byte.class) ||  
			   type.equals(Long.class) ||  
			   type.equals(Double.class) ||  
			   type.equals(Float.class) ||  
			   type.equals(Character.class) ||  
			   type.equals(Short.class) ||  
			   type.equals(Boolean.class) ||  
			   type.equals(java.util.Date.class) ||
			   type.equals(java.sql.Date.class) ||
			   type.isPrimitive();   // 判定指定的 对象是否表示一个基本类型
	}

	
	/**
	 * 判断该字段是否注解为 Transient<br>
	 * @param field
	 * @return
	 * @description 检测 字段是否已经被标注为 非数据库字段
	 */
	public static boolean isTransient(Field field){
		return field.getAnnotation(Transient.class) != null;
	}
	
	
	/**
	 * 获取某个属性对应的 表的列名
	 * @param field
	 * @return
	 */
	public static String getColumnByField(Field field){
		Column columnAnnotation = field.getAnnotation(Column.class);
		if(columnAnnotation != null && columnAnnotation.name().trim().length() != 0){
			return columnAnnotation.name();
		}
		
		/**
		 * 这里其实说明啦 如果有个字段没有注解为Column 但是注解为了 Key 则说明也是数据库字段啦
		 */
		Key keyAnnotation = field.getAnnotation(Key.class);
		if(keyAnnotation != null && keyAnnotation.name().trim().length() != 0){
			return keyAnnotation.name();
		}
		
		return field.getName();
	}
	
	
	/**
	 * 获取某个属性对应的 表的列的默认值
	 * @param field
	 * @return
	 */
	public static String getColumnDefaultValue(Field field){
		Column columnAnnotation = field.getAnnotation(Column.class);
		if(columnAnnotation !=null && columnAnnotation.defaultValue().trim().length() != 0){
			return columnAnnotation.defaultValue();
		}
		return null;
	}
	
	
	/**
	 * 获取指定class对象 指定filed对象的set方法Method对象
	 * @param clazz
	 * @param field
	 * @return
	 */
	public static Method getFieldSetMethod(Class<?> clazz,Field field){
		String fn = field.getName();
		String mn = null;
		try {
			if (field.getType() == boolean.class) { // boolean 类型得特殊处理
				if (isISStart(fn)) {
					mn = "set" + fn.substring(2, 3).toUpperCase() + fn.substring(3);
				} else {
					mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
				}
			} else {
				mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
			}
			return clazz.getDeclaredMethod(mn, field.getType());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("this model["+clazz+"], Field[" +field+"] has no set Method");
		}
		
	}

	
	/**
	 * 获取指定class对象 指定filed对象的get方法Method对象
	 * @param clazz
	 * @param field
	 * @return
	 */
	public static Method getFieldGetMethod(Class<?> clazz,Field field){
		String fn = field.getName();
		Method m = null;
		if(field.getType() == boolean.class){  // boolean 类型得特殊处理
			m = getBooleanFieldGetMethod(clazz, fn);
		}
		if(m == null ){
			m = getFieldGetMethod(clazz, fn);
		}
		return m;
	}
	
	
	public static Method getBooleanFieldGetMethod(Class<?> clazz, String fieldName) {
		String mn = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		if(isISStart(fieldName)){
			mn = fieldName;
		}
		try {
			return clazz.getDeclaredMethod(mn);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Method getFieldGetMethod(Class<?> clazz, String fieldName) {
		String mn = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			return clazz.getDeclaredMethod(mn);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 获取bolean类型字段的set方法 Method对象
	 * @param clazz
	 * @param f
	 * @return
	 */
	/*public static Method getBooleanFieldSetMethod(Class<?> clazz, Field f) {
		String fn = f.getName();
		String mn = "set" + fn.substring(0, 1).toUpperCase() + fn.substring(1);
		if(isISStart(f.getName())){
			mn = "set" + fn.substring(2, 3).toUpperCase() + fn.substring(3);
		}
		try {
			return clazz.getDeclaredMethod(mn, f.getType());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	private static boolean isISStart(String fieldName){
		if(fieldName==null || fieldName.trim().length()==0)
			return false;
		//is开头，并且is之后第一个字母是大写 比如 isAdmin
		return fieldName.startsWith("is") && !Character.isLowerCase(fieldName.charAt(2));
	}
	
	
	public static Date stringToDateTime(String strDate) {
		if (strDate != null) {
			try {
				return SDF.parse(strDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
}
