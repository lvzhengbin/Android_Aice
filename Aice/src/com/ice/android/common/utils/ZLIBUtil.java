package com.ice.android.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import android.util.Log;

/**
 * 压缩 工具类
 * 使用 ZLIB 压缩程序库为通用的压缩/解压提供支持
 * @author ice
 *
 */
public class ZLIBUtil {

	private static final String TAG = "ZipUtil";
	
	/**
	 * 解压
	 * @param input 待解压字节数组
	 * @return
	 */
	public static byte[] deCompress(byte[] input){
		if(input.length == 0){
			return input;
		}
		Log.d(TAG, "解压前大小："+input.length);
		
		byte[] output = null;
		ByteArrayOutputStream bao = null;
		/** 创建新的解压缩器对象  */
		Inflater decompresser = new Inflater(); 
		try {
			bao = new ByteArrayOutputStream(input.length);
			/** 为解压缩设置输入数据 */
			decompresser.setInput(input);
			/** 设置缓存区  每次解压1024字节长度  */
			byte[] buf = new byte[1024];
			int len;
			/** 是否已到达压缩数据流的结尾 */
			while (!decompresser.finished()) {
				/** 将字节解压到指定的缓存区  buf */
				len = decompresser.inflate(buf);
				bao.write(buf, 0, len);
			}
			output = bao.toByteArray();
			Log.d(TAG, "解压后的大小："+output.length+", 压缩率："+ (input.length * 100 / output.length) + "%");
		} catch (DataFormatException e) {
			Log.w(TAG, "解压失败... ... ");
			e.printStackTrace();
		}finally{
			try {
				if(bao != null){
					bao.close();
				}
				/** 关闭解压缩器并放弃所有未处理的输入   */
				decompresser.end();
			} catch (IOException e) {
				Log.w(TAG, "关闭流失败... ... ");
				e.printStackTrace();
			}
		}
		return output;
	}
	
	
	/**
	 * 加压
	 * @param input 待加压字节数组
	 * @return
	 */
	public static byte[] compress(byte[] input){
		if(input.length == 0){
			return input;
		}
		Log.d(TAG, "加压前大小："+input.length);
		
		byte[] output= null;
		ByteArrayOutputStream bao = null;
		
		/** 默认压缩级别创建新的压缩器  */
		Deflater compresser = new Deflater();
		try{
			/** 为压缩设置输入数据  */
			compresser.setInput(input);
			/** 指示压缩应当以输入缓冲区的当前内容结尾  */
			compresser.finish(); 
			bao = new ByteArrayOutputStream(input.length);
			byte[] buf = new byte[1024];
			int len;
			while (!compresser.finished()) {
				/** 使用压缩数据填充指定缓冲区   */
				len = compresser.deflate(buf);
				bao.write(buf, 0, len);
			} 
			output = bao.toByteArray();
		}catch(Exception e){
			Log.w(TAG, "加压失败... ... ");
		}finally{
			try {
				if (bao != null) {
					bao.close();
				}
				compresser.end();
			} catch (IOException e) {
				Log.w(TAG, "关闭流失败... ... ");
				e.printStackTrace();
			}
		}
		Log.d(TAG, "加压后的大小："+output.length+", 压缩率："+ (output.length * 100 / input.length) + "%");
		return output;
	}
	
}
