package com.ice.android.common.imagecache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.ice.android.common.utils.LogUtil;
import com.ice.android.common.utils.SdcardUtil;
import com.ice.android.common.utils.encryption.MD5;

/**
 * 文件缓存对象  缓存在sdcard上
 * @author ice
 *
 */
public class ImageFileCache implements ImageCache{

	private static final String TAG = "ImageFileCache";
	
	/** 文件默认缓存存放目录  */
	private static String CACHE_DIR = Environment.getExternalStorageDirectory().getPath() + "aice/cache/img/";
	
	// init config params from aiceConfig.properties
    static {
        InputStream is = null;
        try {
            Properties p = new Properties();
            is = ImageFileCache.class.getResourceAsStream("/assets/aiceConfig.properties");
            p.load(is);

            String cachedir = p.getProperty("ua.imagecache.cachdir");

            if (!TextUtils.isEmpty(cachedir)) {
                CACHE_DIR = Environment.getExternalStorageDirectory().getPath() + "/" + cachedir;
            }
            LogUtil.d(TAG, "CACHE_DIR = " + CACHE_DIR);

        } catch (Exception e) {
        	LogUtil.e(TAG, e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                	LogUtil.e(TAG, e.getMessage());
                }
            }
        }
    }
    
	/** 文件缓存后缀名    */
	private static final String CACHE_FILE_SUFFIX = ".cach";
	
	/** 允许最大缓存空间    30M */
	private static final long MAX_CACHE_SPACE = 30 * 1024 * 1024;
	
	/** 最小SD卡 剩余空间（预留给用户做其它的事情） */
	private static final long MIN_SDCARD_AVAILABLE_SPACE = 30 * 1024 * 1024;
	
	/** 每次清除缓存的百分比 */
	private static final float CACHE_REMOVE_FACTOR = 0.4f;
	
	public ImageFileCache(){
		// 根据sd卡内存情况 清理部分文件缓存
		removeCache();
	}

	
	/**
	 * 从sd卡上获取缓存图片
	 * @param imageParams
	 * @return
	 */
	@Override
	public Bitmap getImageData(String url){
		final String path = CACHE_DIR + convertUrlToFileName(url);
		File file = new File(path);
		if(file.exists()){
			/*Bitmap bitmap = null;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
	        // 获取这个图片的宽和高
	        int beHeight = (int) (options.outHeight / (float) 200);
	        int beWidth = (int) (options.outWidth/ (float) 200);
	        int scale = beHeight>beWidth?beHeight:beWidth; 
	        options.inSampleSize=scale==0?1:scale;
	        options.inJustDecodeBounds = false;	        
	        bitmap = BitmapFactory.decodeFile(path, options); */
			
			Bitmap bitmap = BitmapFactory.decodeFile(path);
	        if(bitmap == null){
	        	Log.w(TAG, "将文件转换成Bitmap失败，删除此文件，path = " + path);
	        	file.delete();
	        }else {
	        	Log.d(TAG, "将文件转换成Bitmap成功，path = " + path);
	        	// 修改 缓存文件的最后修改时间
	        	updateFileTime(path);
	        	return bitmap;
			}
		}	
		return null;
	}
	

	/**
	 * 将图片数据保存到sd卡
	 * @param url
	 * @param mBitmap
	 * @throws IOException 
	 */
	@Override 
	public void addBitmapToCache(String url, Bitmap mBitmap) {
		if(mBitmap == null){
			LogUtil.w(TAG, "Bitmap为null，持久化存储失败，url = "+url);
			return ; 
		}
		
		long sdcardAvailableSpace = SdcardUtil.getSdcardAvailableSpace();
		// 如果sd卡的可用空间 小于 SD卡最小预留空间
		if(sdcardAvailableSpace < MIN_SDCARD_AVAILABLE_SPACE){
			LogUtil.w(TAG, "SD卡空间不足，不做持持久化存储，sdcardAvailableSpace = " + sdcardAvailableSpace + ", url = "+url);
			return ;
		}
		
		File dir = new File(CACHE_DIR);
		if(!dir.exists()){
			dir.mkdirs();     // PS: 这里客串一下, 注意 mkdirs() 与  mkdir()方法的区别
		}
		
		String fileName = CACHE_DIR + convertUrlToFileName(url);
		File file = new File(fileName);
		
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			os.flush();
			os.close();
			LogUtil.d(TAG, "图片持久化存储成功，path = "+ fileName + ", url = " + url);
		} catch (IOException e) {
			LogUtil.d(TAG, "图片持久化存储失败:"+e.getMessage());
		}
		
	}
	
	
	
	/**
	 * 根据sd卡内存情况 清理部分文件缓存
	 * 1.计算存储目录下的文件大小
	 * 2.当文件总大小大于规定的最大缓存大小或者sdcard剩余空间小于最小SD卡可用空间的规定时，
	 *   删除特定数量的最近没有被使用的文件
	 */
	private boolean removeCache() {
		if(!SdcardUtil.isSdcardWritable()){
			LogUtil.d(TAG, "SD卡不可写，清理缓存失败!");
			return false;
		}
		
		File dir = new File(CACHE_DIR);
		File[] files = dir.listFiles();
		if(files == null || files.length == 0){
			LogUtil.d(TAG, "木有缓存，无需清理...");
			return true;
		}
		
		long dirSize = 0;
		for(int i = 0; i < files.length; i++){
			if(files[i].getName().endsWith(CACHE_FILE_SUFFIX)){
				dirSize += files[i].length();
			}
		}
		
		long sdcardAvailableSpace = SdcardUtil.getSdcardAvailableSpace();
		LogUtil.d(TAG, "缓存文件的已有总大小为：" + dirSize + "，SD卡可用空间为：" + sdcardAvailableSpace);
		
		if(dirSize >= MAX_CACHE_SPACE || sdcardAvailableSpace< MIN_SDCARD_AVAILABLE_SPACE){
			// 计算出需要删除的文件数量
			int removeNum = (int)((files.length * CACHE_REMOVE_FACTOR) + 1);
			LogUtil.d(TAG, "需要清除"+removeNum+"个缓存文件");
			
			// 根据文件的最后修改时间进行排序
			Arrays.sort(files, new FileLastModifiedComparator());
			
			for(int i = 0; i < removeNum; i++){
				if(files[i].getName().endsWith(CACHE_FILE_SUFFIX)){
					files[i].delete();
				}
			}
		}else{
			LogUtil.d(TAG, "缓存情况还木有超出指定情况，无需清理...");
		}
		
		return true;
	}
	
	
	/**
	 * 根据文件的最后修改时间进行排序  
	 * 把最后修改时间 越长的排到越前面
	 * @author ice
	 *
	 */
	private class FileLastModifiedComparator implements Comparator<File>{

		@Override
		public int compare(File arg0, File arg1) {
			if(arg0.lastModified() > arg1.lastModified()){
				 return 1;
			}else if(arg0.lastModified() == arg1.lastModified()){
				return 0;
			}
			return -1;
		}
	}
	
	
	/**
	 * 根据图片url生成文件缓存的名字
	 * @param url
	 * @return
	 */
	private String convertUrlToFileName(String url){
		/**
		 * 将url进行MD5加密一下吧
		 */
		return MD5.getMD5Str(url) + CACHE_FILE_SUFFIX;
	}
	
	/**
	 * 修改 缓存文件的最后修改时间
	 * @param path  文件绝对路径
	 */
	private void updateFileTime(String path) {
		File file = new File(path);
		file.setLastModified(System.currentTimeMillis());
	}

}
