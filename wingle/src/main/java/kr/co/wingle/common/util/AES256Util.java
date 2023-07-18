package kr.co.wingle.common.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import kr.co.wingle.common.constants.ErrorCode;
import kr.co.wingle.common.exception.InternalServerErrorException;

@Component
public class AES256Util {
	//initial vector 설정
	private static String iv = "0000000000000001";
	private static Key keySpec;

	public AES256Util() throws UnsupportedEncodingException {
		iv = iv.substring(0, 16);
		byte[] keyBytes = new byte[16];
		byte[] b = iv.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length) {
			len = keyBytes.length;
		}
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec _keySpec = new SecretKeySpec(keyBytes, "AES");
		keySpec = _keySpec;
	}

	//암호화
	public static String encrypt(String str) {
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec((iv.getBytes())));
			byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
			// String enStr = new String(Base64.encodeBase64(encrypted));
			return new java.math.BigInteger(encrypted).toString(16);
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			throw new InternalServerErrorException(ErrorCode.ENCRYPT_FAIL);
		}
	}

	//복호화
	public static String decrypt(String str) {
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			// byte[] byteStr = Base64.decodeBase64(str.getBytes());
			byte[] byteStr = new java.math.BigInteger(str, 16).toByteArray();
			return new String(c.doFinal(byteStr), "UTF-8");
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			throw new InternalServerErrorException(ErrorCode.DECRYPT_FAIL);
		}
	}

	public static Long userIdDecrypt(String userId) {
		return Long.parseLong(decrypt(userId));
	}
}
