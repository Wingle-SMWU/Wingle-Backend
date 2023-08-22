package kr.co.wingle.common.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
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
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
			return Hex.encodeHexString(encrypted);
		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
			throw new InternalServerErrorException(ErrorCode.ENCRYPT_FAIL);
		}
	}

	//복호화
	public static String decrypt(String str) {
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			byte[] byteStr = Hex.decodeHex(str.toCharArray());
			return new String(c.doFinal(byteStr), "UTF-8");
		} catch (GeneralSecurityException | UnsupportedEncodingException | DecoderException e) {
			throw new InternalServerErrorException(ErrorCode.DECRYPT_FAIL);
		}
	}

	public static Long userIdDecrypt(String userId) {
		return Long.parseLong(decrypt(userId));
	}
}
