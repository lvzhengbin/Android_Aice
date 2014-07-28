package com.ice.android.common.utils.encryption;

public class HexString {

	public static String bytes2Hex(byte[] bts) {
		StringBuilder des = new StringBuilder(bts.length);
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des.append("0");
			}
			des.append(tmp);
		}
		return des.toString();
	}
}
