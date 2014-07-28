package com.ice.android.common.annotation.mapping;

import com.ice.android.common.utils.FieldUtil;

/**
 * 数据表实体entity对应的表字段名key和字段值value对象
 * @author ice
 *
 */
public class KeyValue {
	private String key;
	private Object value;
	
	public KeyValue() {
	}

	public KeyValue(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public Object getValue() {
		if(value instanceof java.util.Date || value instanceof java.sql.Date){
			return FieldUtil.SDF.format(value);
		}
		return value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
}
