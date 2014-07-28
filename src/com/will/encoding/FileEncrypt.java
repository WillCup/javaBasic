package com.will.encoding;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
/**
 * File encryption and decryption.
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-7-24
 */
public class FileEncrypt {
	/**
	 * Files to be encrypted.
	 */
	public static String[] filePath = { "src/test.xml" };
	/**
	 * The encrypted files.
	 */
	public static String[] outFilePath = new String[filePath.length];
	/**
	 * Custom key.
	 */
	private static final String KEY = "c4bd856163d50d953afca305345642c1";

	/**
	 * Encrypt key type.
	 */
	private String DES = "DES";
	

	/**
	 * Handler center.
	 */
	public FileEncrypt() {
		super();
		getKey(KEY);
		initCipher();
//		crateEncryptFile();
	}

	private Key key;

	private Cipher cipherDecrypt;
	
	private Cipher cipherEncrypt;

//	/**
//	 * Encrypt file and record the decrypted files' path
//	 * */
//	private void crateEncryptFile() {
//		String outPath = null;
//		for (int i = 0; i < filePath.length; i++) {
//			try {
//				outPath = filePath[i]
//						.substring(0, filePath[i].lastIndexOf(".")) + ".bin";
//				encrypt(filePath[i], outPath);
//				outFilePath[i] = outPath;
//				System.out
//						.println(filePath[i] + "加密完成，加密后的文件是:"+outFilePath[i]);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		System.out
//				.println("=========================加密完成=======================");
//
//	}

	/**
	 * Encrypt file.
	 * 
	 * @param file
	 * @param destFile
	 */
	public void encrypt(String file, String destFile) throws Exception {
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(destFile);

		CipherInputStream cis = new CipherInputStream(is, cipherEncrypt);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = cis.read(buffer)) > 0) {
			out.write(buffer, 0, r);
		}
		cis.close();
		is.close();
		out.close();
	}

	/***
	 * Decrypt file.
	 * 
	 * @param destFile
	 */
	public void decrypt(String destFile) {
		try {
			InputStream is = new FileInputStream(destFile);
			CipherInputStream cis = new CipherInputStream(is, cipherDecrypt);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					cis));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			reader.close();
			cis.close();
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initial encrypt cipher and decrypt.
	 *
	 * <br/>
	 */
	private void initCipher() {
		try {
			cipherEncrypt = Cipher.getInstance(DES);
			cipherEncrypt.init(Cipher.ENCRYPT_MODE, this.key);
			cipherDecrypt = Cipher.getInstance(DES);
			cipherDecrypt.init(Cipher.DECRYPT_MODE, this.key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Define a secret key.
	 * 
	 * @param string
	 */
	public Key getKey(String keyRule) {
		byte[] keyByte = keyRule.getBytes();
		byte[] byteTemp = new byte[8];	// create a byte array, the value is 0 by default.
		// convert the key specified by user into byte array we just created.
		for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
			byteTemp[i] = keyByte[i];
		}
		key = new SecretKeySpec(byteTemp, DES);
		return key;
	}
	
	/**
	 * Get the decrypt cipher
	 * 
	 * @return
	 */
	public Cipher getCipherEdcrypt() {
		return cipherDecrypt;
	}

	/**
	 * Get the encrypt cipher
	 * 
	 * @return
	 */
	public Cipher getCipherEncrypt() {
		return cipherEncrypt;
	}

	/***
	 * Test
	 */
	public static void main(String[] args) {
		FileEncrypt desFileEncrypt = new FileEncrypt();
		desFileEncrypt.decrypt(outFilePath[0]); // 解密第一个文件平且测试解密后的结果
	}
}