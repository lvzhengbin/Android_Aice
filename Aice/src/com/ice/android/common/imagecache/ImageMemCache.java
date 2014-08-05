package com.ice.android.common.imagecache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;
import android.util.Log;


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
	private static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache = new ConcurrentHashMap<>(CACHE_CAPACITY);
	
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
					Log.d(TAG, "将数据从HardCache移到SoftCache，url="+eldest.getKey());
					return true;
				}
				return false;
			}
		};	
	}
	
	
	/**
	 * 从缓存中获取图片数据
	 * @param mImageParams
	 * @return
	 */
	@Override 
	public Bitmap getImageData(ImageParams mImageParams){
		String imageKey = mImageParams.getImageKey();
		// 先从 mHardBitmapCache 缓存中获取
		synchronized (mHardBitmapCache) {
			Bitmap bitmap = mHardBitmapCache.get(imageKey);
			if(bitmap != null){
				mHardBitmapCache.remove(imageKey);
				mHardBitmapCache.put(imageKey, bitmap);
				Log.d(TAG, "从HardCache中取到数据，url="+imageKey);
				return bitmap;
			}
		}
		// 如果mHardBitmapCache中找不到，到mSoftBitmapCache中找
		SoftReference<Bitmap> softReference = mSoftBitmapCache.get(imageKey);
		if(softReference != null){
			Bitmap bitmap = softReference.get();
			if(bitmap != null){
				// 将图片 移回硬缓存   从软缓存中移除
				mHardBitmapCache.put(imageKey, bitmap);
				mSoftBitmapCache.remove(imageKey);
				Log.d(TAG, "从SoftCache中取到数据，url="+ imageKey);
				return bitmap;
			}else{
				mSoftBitmapCache.remove(imageKey);
			}
		}
		
		return null;
	}
			
	/**
	 * 将图片添加到内存缓存		
	 * @param imageParams
	 * @param bitmap
	 */
	@Override 
	public void addBitmapToCache(ImageParams imageParams,Bitmap bitmap){
		if(bitmap != null){
			synchronized (mHardBitmapCache) {
				mHardBitmapCache.put(imageParams.getImageKey(), bitmap);
				Log.d(TAG, "将图片缓存到HardCache,url="+imageParams.getImageKey());
			}
		}
	}
	
}
