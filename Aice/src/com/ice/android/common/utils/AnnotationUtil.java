package com.ice.android.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ice.android.common.annotation.Key;
import com.ice.android.common.annotation.Table;
import com.ice.android.common.annotation.mapping.KeyValue;
import com.ice.android.common.annotation.mapping.Property;
import com.ice.android.common.annotation.mapping.TableInfo;

/**
 * @description 注解工具类
 * @author ice
 * 
 */
public class AnnotationUtil {

	/**
	 * 根据实体类 获得 实体类对应的表名 <br>
	 * 当没有用 @Table 注解该实体类时   默认用类的名称作为表名,并把点（.）替换为下划线(_)
	 * @param clazz
	 * @return
	 */
	public static String getTableName(Class<?> clazz){
		Table table = clazz.getAnnotation(Table.class);
		if(table == null || table.name().trim().length() == 0){
			return clazz.getName().replace(".", "_");
		}
		return table.name();
	}
	
	
	/**
	 * 根据实体类 获得 实体类对应的表主键字段名称
	 * @param clazz
	 * @return
	 */
	public static String getPrimaryKeyColumn(Class<?> clazz){
		String primaryKey = null;
		Field[] fields = clazz.getDeclaredFields();
		if(fields == null){
			throw new RuntimeException("this model["+clazz+"] has no field");
		}else{
			Key keyAnnotation = null;
			Field keyField = null;
			// 获取key主键注解
			for(Field field:fields){
				keyAnnotation = field.getAnnotation(Key.class);
				if(keyAnnotation != null){
					keyField = field;
					break;
				}
			}
			
			if(keyAnnotation != null){  // 表示clazz实体类中有key主键注解
				primaryKey = keyAnnotation.name();
				if(primaryKey == null || primaryKey.trim().length() == 0){
					primaryKey = keyField.getName();
				}
			}else{    // 没有key注解,默认去找 _id 和 id 为主键，优先寻找 _id
				for(Field field : fields){
					if("_id".equals(field.getName()))
						return "_id";
				}
				
				for(Field field : fields){
					if("id".equals(field.getName()))
						return "id";
				}
			}
		}
		
		return primaryKey;
	}
	
	
	/**
	 * 根据实体类 获得 实体类对应的表主键字段Field对象
	 * @param clazz
	 * @return
	 */
	public static Field getPrimaryKeyField(Class<?> clazz){
		Field primaryKeyField = null;
		Field[] fields = clazz.getDeclaredFields();
		if(fields == null){
			throw new RuntimeException("this model["+clazz+"] has no field");
		}else{
			/*
			 * 获取key主键注解
			 */
			for(Field field:fields){
				if(field.getAnnotation(Key.class) != null){
					primaryKeyField = field;
					break;
				}
			}
			/*
			 * 如果没有Key主键注解字段  但是有实体类有 _id 或  id字段
			 */
			if(primaryKeyField == null){  
				for(Field field:fields){
					if("_id".equalsIgnoreCase(field.getName()) || "id".equalsIgnoreCase(field.getName())){
						primaryKeyField = field;
						break;
					}
				}
			}
			
		}
		
		return primaryKeyField;
	}
	
	
	/**
	 * 根据实体类 获得 实体类注解为主键字段的Field名称
	 * @param clazz
	 * @return
	 */
	public static String getPrimaryKeyFieldName(Class<?> clazz){
		Field f = getPrimaryKeyField(clazz);
		return f==null ? null : f.getName();
	}
	
	
	/**
	 * 根据实体类 获得 所有的属性集合[不包含主键]
	 * @param clazz
	 * @return
	 */
	public static List<Property> getPropertyList(Class<?> clazz){
		List<Property> plist = new ArrayList<Property>();
		
		Field[] fields = clazz.getDeclaredFields();
		String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
		for(Field field : fields){
			if(!FieldUtil.isTransient(field) && FieldUtil.isBaseDateType(field)){
				
				if(field.getName().equals(primaryKeyFieldName)){   // 过滤主键
					continue;
				}
				
				Property property = new Property();
				property.setField(field);
				property.setFieldName(field.getName());
				property.setDataType(field.getType());
				property.setColumnName(FieldUtil.getColumnByField(field));
				property.setDefaultValue(FieldUtil.getColumnDefaultValue(field));
				property.setSet(FieldUtil.getFieldSetMethod(clazz, field));
				property.setGet(FieldUtil.getFieldGetMethod(clazz, field));
				
				plist.add(property);
			}
		}
		
		return plist;
	}
	
	
	/**
	 * 根据指定的数据表实体类  获得他对应表的表字段和字段值的key-value集合
	 * @param entity
	 * @return
	 */
	public static List<KeyValue> getSaveKeyValueListByEntity(Object entity){
		List<KeyValue> keyValueList = new ArrayList<KeyValue>();
		TableInfo tableInfo = TableInfo.getTableInfo(entity.getClass());
		Object keyValue = tableInfo.getKeyProperty().getValue(entity);  // 获得主键值
		//用了非自增长,添加id , 采用自增长就不需要添加id了
		if(!(keyValue instanceof Integer)){
			if(keyValue instanceof String && keyValue != null){
				KeyValue kv = new KeyValue(tableInfo.getKeyProperty().getColumnName(), keyValue);
				keyValueList.add(kv);
			}
		}
		
		// 添加其他非主键属性
		Collection<Property> propertys = tableInfo.propertyMap.values();
		for(Property property : propertys){
			KeyValue property2KeyValue = property2KeyValue(property, entity);
			if(property2KeyValue != null){
				keyValueList.add(property2KeyValue);
			}
		}
		
		return keyValueList;
	}
	

	/**
	 * 将Property对象转化成KeyValue对象
	 * @param property
	 * @param entity
	 * @return
	 */
	public static KeyValue property2KeyValue(Property property,Object entity){
		KeyValue kv = null;
		String columnName = property.getColumnName();
		Object value = property.getValue(entity);
		if(value != null){
			kv = new KeyValue(columnName, value);
		}else{
			if(property.getDefaultValue() != null && property.getDefaultValue().trim().length()!=0){
				kv = new KeyValue(columnName, property.getDefaultValue());
			}
		}
		return kv;
	}
	
	
	
}
