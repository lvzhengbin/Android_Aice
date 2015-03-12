package com.ice.android.common.imagecache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;

import com.ice.android.common.utils.LogUtil;


/**
 * 图片内存缓存 对象
 * @author ice
 *
 */
public class ImageMemCache implements ImageCache{

	private static final String TAG = "ImageMemCache";
	/** 缓存容量  */
	private static final int CACHE_CAPACITY = 15;
	
	/** 存放图片的HashMap 对象 */
	private static HashMap<String, Bitmap> mHardBitmapCache;
	/** 使用软引用存放图片数据   放入ConcurrentHashMap对象    <这里是否使 WeakHashMap 会更好呢 ？> */
	private static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(CACHE_CAPACITY);
	
	public ImageMemCache(){
		/* 这里之所以选用 LinkedHashMap，我想主要是因为第三个参数：排序模式  <true表示为访问顺序 |false表示为插入顺序 >
		 * 第二个参数 为加载因子
		 */
		mHardBitmapCache = new LinkedHashMap<String,Bitmap>(CACHE_CAPACITY * 2, 0.5f, true){
			
		     private static final long serialVersionUID = 1L;

			/**
			 * 从映射移除最旧的条目  返回 true,否则返回false 
			 */
			@Override
			protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
				if(size() >= CACHE_CAPACITY){
					/*
					 * 将Bitmap从 mHardBitmapCache内存中 转移到  mSoftBitmapCache内存中
					 */
					mSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
					LogUtil.d(TAG, "将数据从HardCache移到SoftCache，url="+eldest.getKey());
					return true;
				}
				return false;
			}
		};	
	}
	
	
	/**
	 * 从缓存中获取图片数据
	 * @param url 图片url
	 * @return 图片数据
	 */
	@Override
	public Bitmap getImageData(String url) {
		// 先从mHardBitmapCache缓存中获取
		synchronized (mHardBitmapCache) {
			final Bitmap bitmap = mHardBitmapCache.get(url);
			if (bitmap != null) {
				// 如果找到的话，把元素移到linkedhashmap的最前面，从而保证在LRU算法中是最后被删除
				mHardBitmapCache.remove(url);
				mHardBitmapCache.put(url, bitmap);
				LogUtil.d(TAG, "从HardCache中取到数据，url=", url);
				return bitmap;
			}
		}
		// 如果mHardBitmapCache中找不到，到mSoftBitmapCache中找
		SoftReference<Bitmap> bitmapReference = mSoftBitmapCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();
			if (bitmap != null) {
				// 将图片移回硬缓存
				mHardBitmapCache.put(url, bitmap);
				mSoftBitmapCache.remove(url);
				LogUtil.d(TAG, "从SoftCache中取到数据，url=", url);
				return bitmap;
			} else {
				mSoftBitmapCache.remove(url);
			}
		}
		return null;
	}


	/**
	 * 将图片添加到内存缓存		
	 * @param url 图片url
	 * @param bitmap
	 */
	@Override
	public void addBitmapToCache(String url, Bitmap mBitmap) {
		if (mBitmap != null) {
			synchronized (mHardBitmapCache) {
				mHardBitmapCache.put(url, mBitmap);
				LogUtil.d(TAG, "将图片缓存到HardCache，url=", url);
			}
		}
	}
	
}
