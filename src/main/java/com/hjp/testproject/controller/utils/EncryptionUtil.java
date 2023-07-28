/**
 * 
 */
package com.hjp.testproject.controller.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 
 * 암호화 관련 기능을 모아 놓은 Util
 * 
 * <pre>
 * 해당 내부 기능은  Hash Type(MD5, SHA256)과 Symmetric Key Type(DES, TripleDES) 방식이 있다.
 * ------------------------ 
 * 개정이력
 * 2016. 8.  5. Raccoon : 최초작성
 * 2021. 6. 8. 김준곤 : Deprecated 된 클래스 변경
 * </pre>
 * 
 * @author 김준곤
 */
public class EncryptionUtil {
	private EncryptionUtil() {
		throw new IllegalStateException("Utility class");
	}

	// 로그 관리
	private static final Logger log = LoggerFactory.getLogger(EncryptionUtil.class);

	/**
	 * 개 발 자 : Raccoon
	 ** 개발 일시 : 2016.08.05
	 ** 기 능 : DES Secret Key
	 **/
	private static String key() {
		return "raccoon_2016_08_05";
	}

	/**
	 * 개 발 자 : Raccoon
	 ** 개발 일시 : 2016.08.05
	 ** 기 능 : Cipher의 instance 생성시 사용될 값 (DES, TripleDES 구분)
	 **/
	private static String getInstance() throws Exception {
		return (key().length() == 24) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
	}

	/**
	 * 개 발 자 : Raccoon
	 ** 개발 일시 : 2016.08.05
	 ** 기 능 : DES와 TripleDES의 구분을 위함
	 **/
	private static Key getKey() throws Exception {
		return (key().length() == 24) ? getKeyDESTriple(key()) : getKeyDES(key());
	}

	/**
	 * 개 발 자 : Raccoon
	 ** 개발 일시 : 2016.08.05
	 ** 기 능 : DES 방식의 지정된 비밀키를 가져오는 메서드
	 **/
	private static Key getKeyDES(String keyValue) throws Exception {
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		DESKeySpec desKey = new DESKeySpec(keyValue.getBytes());

		return keyFactory.generateSecret(desKey);
	}

	/**
	 * 개 발 자 : Raccoon
	 ** 개발 일시 : 2016.08.05
	 ** 기 능 : TripleDES 방식의 지정된 비밀키를 가져오는 메서드
	 **/
	private static Key getKeyDESTriple(String keyValue) throws Exception {
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		DESKeySpec desKey = new DESKeySpec(keyValue.getBytes());

		return keyFactory.generateSecret(desKey);
	}

	/**
	 * 개 발 자 : Raccoon </br>
	 * 개발 일시 : 2016.08.05 </br>
	 * 기 능 : DES 방식의 문자열 대칭 암호화 코드 </br>
	 **/
	public static String ENC_DES(String enc) {
		if (enc == null || enc.length() == 0)
			return null;

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(getInstance());
			cipher.init(Cipher.ENCRYPT_MODE, getKey());

			byte[] in = enc.getBytes("UTF-8");
			byte[] out = cipher.doFinal(in);

			return new String(Base64.getEncoder().encode(out));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 개 발 자 : Raccoon </br>
	 * 개발 일시 : 2016.08.05 </br>
	 * 기 능 : DES 방식의 문자열 대칭 복호화 코드 </br>
	 **/
	public static String DEC_DES(String dec) {
		if (dec == null || dec.length() == 0)
			return null;

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(getInstance());
			cipher.init(Cipher.DECRYPT_MODE, getKey());
			byte[] in = Base64.getDecoder().decode(dec);
			byte[] out = cipher.doFinal(in);

			return new String(out, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 개 발 자 : Raccoon </br>
	 * 개발 일시 : 2016.08.05 </br>
	 * 기 능 : MD5 방식의 Hash Type 암호화 코드 </br>
	 **/
	public static String ENC_MD5(String enc) {
		String result = "";

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(enc.getBytes());

			byte[] byteData = md.digest();
			StringBuffer str = new StringBuffer();

			for (int a = 0; a < byteData.length; a++) {
				str.append(Integer.toString((byteData[a] & 0xff) + 0x100, 16).substring(1));
			}

			result = str.toString();
		} catch (Exception e) {
			log.debug("암호화 MD5 로직 에러!!");

			result = null;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 개 발 자 : Raccoon </br>
	 * 개발 일시 : 2016.08.05 </br>
	 * 기 능 : SHA256 방식의 Hash Type 암호화 코드 </br>
	 **/
	public static String ENC_SHA256(String str) {
		String SHA = "";

		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(str.getBytes());

			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			SHA = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			log.debug("암호화 SHA256 로직 에러!!");
			SHA = null;
			e.printStackTrace();

		}

		return SHA;
	}

	private static String aesKey = "aes128nubizkeyaz";

	public static String encAES(String message) throws Exception {
		if (message == null) {
			return null;
		} else {
			SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] encrypted = cipher.doFinal(message.getBytes());
			return byteArrayToHex(encrypted);
		}
	}

	public static String decAES(String encrypted) throws Exception {
		if (encrypted == null) {
			return null;
		} else {
			SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] original = cipher.doFinal(hexToByteArray(encrypted));
			String originalStr = new String(original);
			return originalStr;
		}
	}

	private static String byteArrayToHex(byte[] encrypted) {
		if (encrypted == null || encrypted.length == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer(encrypted.length * 2);
		String hexNumber;
		for (int x = 0; x < encrypted.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & encrypted[x]);
			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		return sb.toString();
	}

	private static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		// 16진수 문자열을 byte로 변환
		byte[] byteArray = new byte[hex.length() / 2];

		for (int i = 0; i < byteArray.length; i++) {
			byteArray[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return byteArray;
	}
}
