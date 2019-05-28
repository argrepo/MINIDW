package com.datamodel.anvizent.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datamodel.anvizent.helper.CommonUtils;

public class AnvizentEncryptionServiceImpl implements EncryptionService {

	protected static final Log LOGGER = LogFactory.getLog(AnvizentEncryptionServiceImpl.class);

	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final String ENCODING = "UTF8";
	private static final String KEY_HASH = "PBKDF2WithHmacSHA1";
	private static final int ITERATION_COUNT = 65536;
	private static final int KEY_SIZE = 128;// 256;

	private String privateKey;
	private IvParameterSpec ivParmSpec;
	private String salt;

	public AnvizentEncryptionServiceImpl(String privateKey, String iv, String salt) throws UnsupportedEncodingException {
		this.privateKey = privateKey;
		ivParmSpec = new IvParameterSpec(iv.getBytes(ENCODING));
		this.salt = salt;
	}

	private SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_HASH);
		KeySpec keySpec = new PBEKeySpec(privateKey.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_SIZE);
		SecretKey secretKey = factory.generateSecret(keySpec);
		SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);
		return secret;
	}

	@Override
	public String encrypt(String data) throws Exception {
		String encryptedString = null;
		if (StringUtils.isNotBlank(data)) {
			SecretKey secretKey = generateKey();
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParmSpec);
			encryptedString = Base64.encodeBase64URLSafeString(cipher.doFinal(data.getBytes(ENCODING)));
		}
		return encryptedString;
	}

	@Override
	public String decrypt(String encryptedString) throws Exception {
		String decryptedString = null;
		if (StringUtils.isNotBlank(encryptedString)) {
			SecretKey secretKey = generateKey();
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParmSpec);
			decryptedString = new String(cipher.doFinal(Base64.decodeBase64(encryptedString)));
		}
		return decryptedString;
	}

	@Override
	public File decryptFile(File encryptedFile) throws Exception {
		File decryptedFile = null;
		if (encryptedFile != null) {
			FileInputStream fis = null;
			FileOutputStream fos = null;
			try {
				// initialize cipher
				SecretKey secretKey = generateKey();
				Cipher cipher = Cipher.getInstance(TRANSFORMATION);
				cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParmSpec);

				fis = new FileInputStream(encryptedFile);
				decryptedFile = new File(CommonUtils.getFileName(encryptedFile.getAbsolutePath()));
				fos = new FileOutputStream(decryptedFile);
				byte[] in = new byte[1024];
				int read;
				while ((read = fis.read(in)) != -1) {
					byte[] output = cipher.update(in, 0, read);
					if (output != null)
						fos.write(output);
				}

				byte[] output = cipher.doFinal();
				if (output != null)
					fos.write(output);

			} finally {
				if (fis!=null) {
					fis.close();
				}
				if (fos != null) {
					fos.flush();
					fos.close();
				}
			}
		}
		return decryptedFile;
	}

	public File encryptFile(File file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
