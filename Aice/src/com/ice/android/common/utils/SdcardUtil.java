package com.ice.android.common.utils;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

/**
 * 关于SD卡操作的工具类
 * @author ice
 *
 */
public class SdcardUtil {

	/**
	 * SD卡 是否可写
	 * @return
	 */
	public static boolean isSdcardWritable(){
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)){
			// We can read and write the media
			mExternalStorageWriteable = true;
		}else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
			// We can only read the media
			mExternalStorageWriteable = false;
		}else{
			
			mExternalStorageWriteable = false;
		}
		return mExternalStorageWriteable;
	}
	
	/**
	 * SD卡 是否可读
	 * @return
	 */
	public static boolean isSdcardReadable(){
		boolean mExternalStorageReadable = false;
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)){
			// We can read and write the media
			mExternalStorageReadable = true;
		}else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
			mExternalStorageReadable = true;
		}else{
			mExternalStorageReadable = false;
		}
		return mExternalStorageReadable;
	}
	
	
	/**
	 * 获取sd卡的可用空间 及剩余空间
	 * @return
	 */
	public static long getSdcardAvailableSpace(){
		StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
		int blockSize = statFs.getBlockSize();
		int availableBlocks = statFs.getAvailableBlocks();
		
		long availableSpace = availableBlocks * 1l * blockSize;    // ?
		
		return availableSpace;
	}
	
	
	/**
	 * 获取指定目录所占SD卡空间
	 * @param dirPath
	 * @return
	 */
	public static long getDirOccupiedSpace(String dirPath){
		File dir =new File(dirPath);
		File[] files = dir.listFiles();
		if(files == null){
			return 0l;
		}
		long dirSize = 0l;
		for(int i=0;i<files.length;i++){
			dirSize += files[i].length();
		}
		return dirSize;
	}
	
	
}
