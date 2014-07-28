package com.ice.android.common.imagecache;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * 异步加载图片缓存 类
 * 单例模式
 * @author ice
 *
 */
public class AsynImageLoader {

	private static final String TAG = "AsynImageLoader";
	
	/** 线程池数量  */
	private static final int THREAD_NUM = 3; 
	/** 线程池  */
	private ExecutorService executorService;
	
	/** 内存缓存对象  */
	private static ImageMemCache memCache;
	/** sd卡 文件缓存对象  */
	private static ImageFileCache fileCache;
	
	private static AsynImageLoader instance;
	
	
	/**
	 * 单例模式
	 * @param memCache
	 * @param fileCache
	 */
	private AsynImageLoader() {
		executorService = Executors.newFixedThreadPool(THREAD_NUM);
		this.memCache = new ImageMemCache();
		this.fileCache = new ImageFileCache();
	}


	/**
	 * 获取实例
	 * @return
	 */
	public static synchronized AsynImageLoader getInstance(){
		if(instance == null){
			instance = new AsynImageLoader();
		}
		return instance;
	}
	
	
	/**
	 * 异步加载网络图片数据
	 * @param mImageParams
	 * @param mHandler
	 */
	public void loadImageDataFromNet(ImageParams mImageParams,Handler mHandler){
		executorService.submit(new ImgLoadTask(mImageParams,mHandler));
	}
	
	
	/**
	 * 从网络上获取图片数据，并将图片缓存到内存或是sdcard上
	 * @param imageParams
	 * @return
	 */
	private Bitmap loadNetImage(ImageParams imageParams){
		Bitmap imageData = ImageHttpUtil.getImageData(imageParams.getUrl().trim());
		if(imageData != null){
			memCache.addBitmapToCache(imageParams, imageData);
			
			fileCache.addBitmapToCache(imageParams, imageData);
		}else{
			Log.d(TAG, "获取网络图片数据失败，url = "+imageParams.getUrl());
		}
		
		return imageData;
	}
	
	
	/**
	 * 从本地缓存中获取图片数据
	 * 获得一个图片的数据,从三个地方获取,首先是内存缓存,然后是文件（sdcard）缓存,最后从网络获取
	 * @param imageParams
	 * @return
	 */
	public Bitmap loadCacheImage(ImageParams imageParams){
		Bitmap imageData = null;
		// 从内存缓存中获取图片
		imageData = memCache.getImageData(imageParams);
		if(imageData == null){
			// 从文件（sdcard）缓存中获取图片
			imageData = fileCache.getImageData(imageParams);
		}
		return imageData;
	}

	
	
	/**
	 * 图片加载子线程任务  内部类
	 * @author ice
	 * 
	 * java.util.concurrent.Callable 接口类似于 Runnable，两者都是为那些其实例可能被另一个线程执行的类设计的。
	 * 但是 Runnable 不会返回结果
	 *
	 */
	private class ImgLoadTask implements Callable<Void>{

		private ImageParams imageParams;
		private Handler handler;
		
		public ImgLoadTask(ImageParams mImageParams, Handler mHandler) {
			this.imageParams = mImageParams;
			this.handler = mHandler;
		}

		@Override
		public Void call() throws Exception {
			Message msg = new Message();
			msg.obj = loadNetImage(imageParams);
			if(msg.obj != null){
				// 将加载得到的图片对象 通过Message承载发送给Handler处理
				handler.sendMessage(msg);
			}
			return null;
		}
	}
	
	
	
	
}
