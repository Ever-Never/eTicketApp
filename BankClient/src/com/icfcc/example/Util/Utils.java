//$Id: Utils.java,v 1.1.1.1 2007/10/06 13:47:03 benmoez Exp $

/**
 * Author : Moez Ben MBarka Moez
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

// $Id: Utils.java,v 1.1.1.1 2007/10/06 13:47:03 benmoez Exp $

package com.icfcc.example.Util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import android.app.Activity;
import android.view.View;

/**
 * 
 */
public class Utils {

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static int fromDigit(char ch) {
		if (ch >= '0' && ch <= '9')
			return ch - '0';
		if (ch >= 'A' && ch <= 'F')
			return ch - 'A' + 10;
		if (ch >= 'a' && ch <= 'f')
			return ch - 'a' + 10;
		throw new IllegalArgumentException("invalid hex digit '" + ch + "'");
	}

	/**
	 * Returns a hex string representing the byte array.
	 * 
	 * @param ba
	 *            The byte array to hexify.
	 * @return The hex string.
	 */
	public static String toHexString(byte[] ba) {
		int length = ba.length;
		char[] buf = new char[length * 3];
		for (int i = 0, j = 0, k; i < length;) {
			k = ba[i++];
			buf[j++] = HEX_DIGITS[(k >> 4) & 0x0F];
			buf[j++] = HEX_DIGITS[k & 0x0F];
			buf[j++] = ' ';
		}
		return new String(buf, 0, buf.length - 1);
	}

	public static String toHexStringNoBlank(byte[] ba) {
		int length = ba.length;
		char[] buf = new char[length * 2];
		for (int i = 0, j = 0, k; i < length;) {
			k = ba[i++];
			buf[j++] = HEX_DIGITS[(k >> 4) & 0x0F];
			buf[j++] = HEX_DIGITS[k & 0x0F];
		}
		return new String(buf, 0, buf.length);
	}

	public static byte[] bytesFromHexString(String hex)
			throws NumberFormatException {
		if (hex.length() == 0)
			return null;
		String myhex = hex + " ";
		int len = myhex.length();
		if ((len % 3) != 0)
			throw new NumberFormatException();
		byte[] buf = new byte[len / 3];
		int i = 0, j = 0;
		while (i < len) {
			try {
				buf[j++] = (byte) ((fromDigit(myhex.charAt(i++)) << 4) | fromDigit(myhex
						.charAt(i++)));
			} catch (IllegalArgumentException e) {
				throw new NumberFormatException();
			}
			if (myhex.charAt(i++) != ' ')
				throw new NumberFormatException();
		}
		return buf;
	}

	public static byte[] bytesFromHexStringNoBlank(String hex)
			throws NumberFormatException {
		if (hex.length() == 0)
			return null;
		String myhex = hex;
		int len = myhex.length();
		if ((len % 2) != 0)
			throw new NumberFormatException();
		byte[] buf = new byte[len / 2];
		int i = 0, j = 0;
		while (i < len) {
			try {
				buf[j++] = (byte) ((fromDigit(myhex.charAt(i++)) << 4) | fromDigit(myhex
						.charAt(i++)));
			} catch (IllegalArgumentException e) {
				throw new NumberFormatException();
			}
		}
		return buf;
	}

	public static byte[] buildHeader(byte cla, byte ins, byte p1, byte p2,
			byte lc) {
		byte[] header = { cla, ins, p1, p2, lc };
		return header;
	}

	public static byte[] rand_bytes(int size) {
		Random rand = new Random();
		byte[] result = new byte[size];
		rand.nextBytes(result);
		return result;
	}

	public static byte[] clone_array(byte[] src) {
		byte[] dest = new byte[src.length];
		System.arraycopy(src, 0, dest, 0, src.length);
		return dest;
	}

	public static byte[] SHA1(byte[] data) {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		md.update(data);

		return md.digest();
	}

	public static byte[] getBERLen(int len) {

		byte[] len_b = null;

		if (len >= 0x100) {
			len_b = new byte[3];
			len_b[0] = (byte) 0x82;
			len_b[1] = (byte) (len >> 8);
			len_b[2] = (byte) (len & 0xFF);
		} else if (len >= 0x80) {
			len_b = new byte[2];
			len_b[0] = (byte) 0x81;
			len_b[1] = (byte) len;
		} else {
			len_b = new byte[] { (byte) len };
		}

		return len_b;
	}

	public static byte[] getBytes(char[] chars) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(chars.length);
		cb.put(chars);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		return bb.array();
	}

//	public static char[] getChars(byte[] bytes) {
//		Charset cs = Charset.forName("UTF-8");
//		ByteBuffer bb = ByteBuffer.allocate(bytes.length);
//		bb.put(bytes);
//		bb.flip();
//		CharBuffer cb = cs.decode(bb);
//		return cb.array();
//	}
	
	public static char[] getChars2(byte[] bytes) {
		char[] chs = new char[bytes.length];
		for (int i = 0; i < chs.length; i++){
			chs[i] = (char)bytes[i];
		}
		return chs;
	}
	
	
	public static void batchSetClickListener(int[] ids, View.OnClickListener listener, Activity activity) {
		for (int i = 0; i < ids.length; i++) {
			View view = activity.findViewById(ids[i]);
			view.setOnClickListener(listener);
		}
	}

	public static int bytesToInt(byte[] intByte) {
		int fromByte = 0;
		for (int i = 0; i < 2; i++) {
			int n = (intByte[i] < 0 ? (int) intByte[i] + 256 : (int) intByte[i]) << (8 * i);
			System.out.println(n);
			fromByte += n;
		}
		return fromByte;
	}

}
