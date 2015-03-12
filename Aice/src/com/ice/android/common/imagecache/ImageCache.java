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
	 * @param url 图片的url
	 * @return
	 */
	public Bitmap getImageData(String url);
	
	/**
	 * 保存图片至缓存
	 * @param url 图片url
	 * @param mBitmap  
	 */
	public void addBitmapToCache(String url, Bitmap mBitmap);
	
}
