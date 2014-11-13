package com.ice.android.common.utils;

import android.content.Context;

/**
 * 像素单位转换工具类
 * @author ice
 *
 */
public class UnitConvertUtil {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		// 获取密度值
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	
	/** 
     * 将px值转换为sp值，保证文字大小不变 
     * @param pxValue 
     * @param fontScale 
     *        （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int px2sp(Context context, float pxValue) {  
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }  
  
    
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     * @param spValue 
     * @param fontScale 
     *       （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }  
    
}
