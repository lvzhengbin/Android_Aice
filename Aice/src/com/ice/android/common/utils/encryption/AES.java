package com.ice.android.common.utils.encryption;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	private static final String TAG = "AES";

	public static byte[] encrypt(byte[] origin, String key) {

		try {
			/**
			 * Create the KeySpec object by pirvate.key and the algorithm.
			 */
			SecretKeySpec sks = new SecretKeySpec(convertAESKey(key), "AES");
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] iv = new byte[16];
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(iv));
			// 正式执行加密操作
			return cipher.doFinal(origin);
		} catch (Exception e) {
			// LogUtil.e(TAG, e, e.getMessage());
		}
		return null;
	}

	public static byte[] decrypt(byte[] encryptedData, String key) {
		try {
			// 获得密匙数据
			byte rawKeyData[] = convertAESKey(key);
			// 从原始密匙数据创建一个KeySpec对象
			SecretKeySpec sks = new SecretKeySpec(rawKeyData, "AES");
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			byte[] iv = new byte[16];
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(iv));
			return cipher.doFinal(encryptedData);
		} catch (Exception e) {
			// LogUtil.e(TAG, e, e.getMessage());
		}
		return null;
	}

	private static byte[] convertAESKey(String key) {

		byte[] keyBytes;
		try {
			keyBytes = key.getBytes("UTF-8");
			// Use the first 16 bytes (or even less if key is shorter)
			byte[] keyBytes16 = new byte[16];
			System.arraycopy(keyBytes, 0, keyBytes16, 0,
					Math.min(keyBytes.length, 16));
			return keyBytes16;
		} catch (UnsupportedEncodingException e) {
			// LogUtil.e(TAG, e, e.getMessage());
		}
		return null;

	}

}
