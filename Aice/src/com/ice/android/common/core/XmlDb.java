package com.ice.android.common.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 1.用于保存简单的key-value值到本地xml文件<br>
 * 2.用于从本地xml文件中取值<br>
 * 注意：只能允许本应用程序读写，不能读写其他程序的sharedPreferences内容，也不能被其他程序读写<br>
 * 使用方法：<br>
 * XmlDb xmlDb = XmlDb.getInstance(context);<br>
 * xmlDb.saveKeyStringValue("author","ice");  // 保存值 <br>
 * xmlDb.getKeyStringValue("author","ice");   // 从sharedPreferences中取值  <br>
 * xmlDb.clear();  // 清空sharedPreferences所有的值
 * @author ice
 *
 */
public class XmlDb {
	private Context mContext;
	private static SharedPreferences sharedPreferences = null;
	private SharedPreferences.Editor editor;
	private static final String Pref_Name = "AppShare"; // xml文件名   这里先写死在这吧 以后再修改为根据不同项目动态命名
	private static XmlDb mInstance;
	
	private XmlDb(Context context){
		this.mContext = context;
	}
	
	public synchronized static XmlDb getInstance(Context context){
		if(mInstance == null){
			mInstance = new XmlDb(context);
			mInstance.open();
		}
		return mInstance;
	}
	
	
	private void open(){
		// Context.MODE_PRIVATE 表示只能被本应用程序读写 sharedPreferences
		sharedPreferences = mContext.getSharedPreferences(Pref_Name, Context.MODE_PRIVATE);
	}
	
	
	/**
	 * 保存String类型的key-value值到本地SharedPreferences  xml文件
	 * @param key
	 * @param strValue
	 */
	public void saveKeyStringValue(String key,String strValue){
		if(sharedPreferences != null){
			editor = sharedPreferences.edit();
			editor.putString(key, strValue);
			editor.commit();
		}
	}
	
	/**
	 * 从SharedPreferences xml文件中获取key对应的String类型的值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getKeyStringValue(String key,String defaultValue){
		if(sharedPreferences != null){
			return sharedPreferences.getString(key, defaultValue);
		}
		return null;
	}
	
	
	/**
	 * 保存boolean类型的key-value值到本地SharedPreferences  xml文件
	 * @param key
	 * @param boolValue
	 */
	public void saveKeyBooleanValue(String key,boolean boolValue){
		if(sharedPreferences != null){
			editor = sharedPreferences.edit();
			editor.putBoolean(key, boolValue);
			editor.commit();
		}
	}
	
	/**
	 * 从SharedPreferences xml文件中获取key对应的boolean类型的值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean getKeyBooleanValue(String key, boolean defaultValue){
		if(sharedPreferences != null){
			return sharedPreferences.getBoolean(key, defaultValue);
		}
		return defaultValue;
	}
	
	
    /**
     * 保存int类型的key-value值到本地SharedPreferences  xml文件
     * @param key
     * @param intValue
     */
	public void saveKeyIntValue(String key,int intValue){
		if(sharedPreferences != null){
			editor = sharedPreferences.edit(); 
			editor.putInt(key, intValue);
			editor.commit();
		}
	}
	
	/**
	 * 从SharedPreferences xml文件中获取key对应的int类型的值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int getKeyIntValue(String key,int defaultValue){
		if(sharedPreferences != null){
			return sharedPreferences.getInt(key, defaultValue);
		}
		return defaultValue;
	}
	
	
	/**
	 * 保存long类型的key-value值到本地SharedPreferences  xml文件
	 * @param key
	 * @param longValue
	 */
	public void saveKeyLongValue(String key,long longValue){
		if(sharedPreferences != null){
			editor = sharedPreferences.edit(); 
			editor.putLong(key, longValue);
			editor.commit();
		}
	}
	
	/**
	 * 从SharedPreferences xml文件中获取key对应的long类型的值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public long getKeyLongValue(String key,long defaultValue){
		if(sharedPreferences != null){
			return sharedPreferences.getLong(key, defaultValue);
		}
		return defaultValue;
	}
	
	
	/**
	 * 保存float类型的key-value值到本地SharedPreferences  xml文件
	 * @param key
	 * @param floatValue
	 */
	public void saveKeyFloatValue(String key,float floatValue){
		if(sharedPreferences != null){
			editor = sharedPreferences.edit(); 
			editor.putFloat(key, floatValue);
			editor.commit();
		}
	}
	
	/**
	 * 从SharedPreferences xml文件中获取key对应的float类型的值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public float getKeyFloatValue(String key,float defaultValue){
		if(sharedPreferences != null){
			return sharedPreferences.getFloat(key, defaultValue);
		}
		return defaultValue;
	}
	
	
	/**
	 * 清空掉SharedPreferences  xml文件中所有的数据
	 */
	public void clear(){
		if(sharedPreferences != null){
			SharedPreferences.Editor edit = sharedPreferences.edit();
			edit.clear();
			edit.commit();
		}
	}
	
	
	/*public void saveValueToXml(String key){
		
	}*/
	
}
