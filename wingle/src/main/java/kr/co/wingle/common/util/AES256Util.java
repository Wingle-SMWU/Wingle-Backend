package kr.co.wingle.common.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
public class AES256Util {
	private String iv = "0000000000000001";
	private Key keySpec;

	public AES256Util() throws UnsupportedEncodingException {
		this.iv = iv.substring(0,16);
		byte[] keyBytes = new byte[16];
		byte[] b = iv.getBytes("UTF-8");
		int len = b.length;
		if(len> keyBytes.length){
			len = keyBytes.length;
		}
		System.arraycopy(b,0,keyBytes,0,len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes,"AES");
		this.keySpec = keySpec;
	}
	//암호화
	public String encrypt(String str) throws NoSuchAlgorithmException, GeneralSecurityException,UnsupportedEncodingException{
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE,keySpec,new IvParameterSpec((iv.getBytes())));
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));
		return enStr;
	}
	//복호화
	public String decrypt(String str) throws NoSuchAlgorithmException,GeneralSecurityException,UnsupportedEncodingException{
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE,keySpec,new IvParameterSpec(iv.getBytes()));
		byte[] byteStr = Base64.decodeBase64(str.getBytes());
		return new String(c.doFinal(byteStr),"UTF-8");
	}
}
