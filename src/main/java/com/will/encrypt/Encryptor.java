package com.will.encrypt;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.will.Consts;

public class Encryptor {
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

	public static final String IV = "62680693";
	private static Log log = LogFactory.getLog(Encryptor.class);
	public static final String KEY = "c4bd8561";

	/**
	 * DES + base64 encoding.
	 * 
	 * @param key
	 *            longer then 8
	 * @param data
	 *            to be encrypted
	 * @return String
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeySpecException
	 * @throws InvalidAlgorithmParameterException
	 * @throws UnsupportedEncodingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws CryptException
	 */
	public static synchronized String encode(String key, String data)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		DESKeySpec dks = new DESKeySpec(key.getBytes());

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		Key secretKey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
		AlgorithmParameterSpec paramSpec = iv;
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

		byte[] bytes = cipher.doFinal(data.getBytes(Consts.CHARSET));

		return new String(Base64.encodeBase64(bytes));
	}

	public static synchronized String encode(String data) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeySpecException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {
		return encode(KEY, data);
	}

	/**
	 * Called by decodeValue
	 * 
	 * @param key
	 *            length > 8
	 * @param data
	 * @return
	 * @throws InvalidKeyException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws Exception
	 */
	public static byte[] decode(String key, byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(data);
	}

	/**
	 * Base64 + decode.
	 * 
	 * @param key
	 * @param data
	 * @return
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws Exception
	 */
	public static synchronized String decodeValue(String key, String data) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		byte[] datas;
		String value = null;
		if (System.getProperty("os.name") != null
				&& (System.getProperty("os.name").equalsIgnoreCase("sunos") || System
						.getProperty("os.name").equalsIgnoreCase("linux"))) {
			datas = decode(key, Base64.decodeBase64(data));
		} else {
			datas = decode(key, Base64.decodeBase64(data));
		}
		value = new String(datas);
		return value;
	}

	/**
	 * Get the encrypted file's content, and decrypt it.
	 * 
	 * <br/>
	 * 
	 * @param file
	 * @return
	 * @throws IOException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException 
	 * @throws Exception 
	 */
	public static synchronized String getDESFileContent(String file) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		return decodeValue(KEY, FileUtils.readFileToString(new File(file)));
	}

	/**
	 * Encrypt content, and write it to file.
	 * 
	 * <br/>
	 * 
	 * @param file
	 * @param content
	 * @throws IOException
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public static synchronized void setDesFile(String file, String content)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		FileUtils.write(new File(file), encode(content), Consts.CHARSET);
	}
	
	
	public static synchronized void normalToDesFile (String normalFile, String desFile) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, IOException {
		String normalContent = FileUtils.readFileToString(new File(normalFile), Consts.CHARSET);
		FileUtils.write(new File(desFile), encode(normalContent), Consts.CHARSET);
	}

	public static void main(String[] args) throws Exception {
//		String data = "abc \n这";
//		String encode = Encryptor.encode(KEY, data);
//		File file = new File("src/main/java/smart_agent_dfd.cw");
//		FileWriter writer = new FileWriter(file);
//		writer.write(encode);
//		writer.close();
//		System.out.println(data + " \t --> 密：" + encode);
//
//		String decode = Encryptor.decodeValue(KEY,
//				FileUtils.readFileToString(file));
//		System.out.println("反明：" + decode);
//		normalToDesFile("src/main/java/cloudwise-host-agent.xml", "src/main/java/will_smart_agent_full.cw");
		normalToDesFile("src/main/java/cloudwise-host-agent-clean.xml", "src/main/java/smart_agent_without_redundancy.cw");
		normalToDesFile("src/main/java/cloudwise-host-agent-clean-full.xml", "src/main/java/smart_agent_with_all_redundancy.cw");
		
	}

	public static synchronized String decodeValue(String data) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return decodeValue(KEY, data);
	}
}
