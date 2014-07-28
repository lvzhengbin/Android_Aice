package com.ice.android.common.utils.encryption;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DES {

	private static final String TAG = "DES";

	public static byte[] encrypt(byte[] origin, String key) throws Exception {
		Cipher encryptCipher = Cipher.getInstance("DES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(key));
		return encryptCipher.doFinal(origin);
	}

	public static byte[] decrypt(byte[] encryptedData, String key)
			throws Exception {
		Cipher decryptCipher = Cipher.getInstance("DES");
		decryptCipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(key));
		return decryptCipher.doFinal(encryptedData);
	}

	private static SecretKeySpec getSecretKeySpec(String key) {

		try {
			byte[] keyBytes = key.getBytes("UTF-8");

			byte[] keyBytes8 = new byte[8];
			System.arraycopy(keyBytes, 0, keyBytes8, 0,
					Math.min(keyBytes.length, 8));

			SecretKeySpec sks = new SecretKeySpec(keyBytes8, "DES");
			return sks;
		} catch (UnsupportedEncodingException e) {
			// LogUtil.e(TAG, e, e.getMessage());
		}
		return null;
	}
}
