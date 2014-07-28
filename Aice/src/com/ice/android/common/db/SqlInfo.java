package com.ice.android.common.db;

import java.util.LinkedList;

/**
 * 对sql语句的封装<br>
 * 封装了sql部分和参数部分
 * @author ice
 *
 */
public class SqlInfo {
	
	private String sql;  // 纯sql部分
	private LinkedList<Object> bindArgs;  // where 参数部分
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public LinkedList<Object> getBindArgs() {
		return bindArgs;
	}
	public void setBindArgs(LinkedList<Object> bindArgs) {
		this.bindArgs = bindArgs;
	}
	
	public Object[] getBindArgsAsArray(){
		if(bindArgs != null){
			return bindArgs.toArray();
		}
		return null;
	}

	public String[] getBindArgsAsStringArray(){
		if(bindArgs != null){
			String[] strings = new String[bindArgs.size()];
			for(int i = 0;i<bindArgs.size();i++){
				strings[i]=bindArgs.get(i).toString();
			}
			return strings;
		}
		return null;
	}
	
	/**
	 * 新增bindArgs参数值
	 * @param value
	 */
	public void addVaule(Object value){
		if(bindArgs == null){
			bindArgs = new LinkedList<Object>();
		}
		bindArgs.add(value);
	}
	
}
