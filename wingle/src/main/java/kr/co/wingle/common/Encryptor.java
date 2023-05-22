package kr.co.wingle.common;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Encryptor {
	public byte[] encryptMessage(byte[] message, byte[] keyBytes) throws InvalidKeyException, NoSuchPaddingException,
		NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
		// getInstance 메서드에 Cipher 객체를 AES 암호화, CBC operation mode, PKCS5 padding scheme로 초기화하라고 요청
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding","BC");
		//양방향 키 생성
		SecretKey secretKey = new SecretKeySpec(keyBytes,"AES");
		cipher.init(Cipher.ENCRYPT_MODE,secretKey);

		return cipher.doFinal(message);
	}
	public byte[] decryptMessage(byte[] encryptedmessage, byte[] keyBytes) throws
		InvalidKeyException,
		NoSuchPaddingException,
		NoSuchAlgorithmException,
		IllegalBlockSizeException,
		BadPaddingException,
		NoSuchProviderException {
		// getInstance 메서드에 Cipher 객체를 AES 암호화, CBC operation mode, PKCS5 padding scheme로 초기화하라고 요청
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding","BC");		//양방향 키 생성
		SecretKey secretKey = new SecretKeySpec(keyBytes,"AES");
		cipher.init(Cipher.DECRYPT_MODE,secretKey);

		return cipher.doFinal(encryptedmessage);
	}

	public static void main(String[] args) {

		Provider[] providers = Security.getProviders();

		for (Provider provider : providers) {
			System.out.println(provider.getName());
		}
	}
}
