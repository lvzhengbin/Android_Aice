package com.ice.android.common.imagecache;

import android.graphics.Bitmap;

/**
 * 图片 缓存 接口类
 * @author ice
 *
 */
public interface ImageCache {
	
	/**
	 * 从缓存中拿图片
	 * @param imageParams  图片参数实体类
	 * @return
	 */
	public Bitmap getImageData(ImageParams imageParams);
	
	/**
	 * 保存图片至缓存
	 * @param imageParams 图片参数实体类
	 * @param mBitmap  
	 */
	public void addBitmapToCache(ImageParams imageParams,Bitmap mBitmap);
	
}
