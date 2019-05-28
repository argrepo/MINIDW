/**
 * 
 */
package com.datamodel.anvizent.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datamodel.anvizent.helper.minidw.CommonUtils;
import com.datamodel.anvizent.helper.minidw.Constants;

/**
 * @author rakesh.gajula
 *
 */
public class AnvizentEncryptionServiceImpl2 implements EncryptionService {

	protected static final Log LOGGER = LogFactory.getLog(AnvizentEncryptionServiceImpl2.class);

	private final String characterEncoding = "UTF-8";
	private final String cipherTransformation = "AES/CBC/PKCS5Padding";
	private final String aesEncryptionAlgorithm = "AES";

	private String privateKey;
	private String ivParmSpec;

	public AnvizentEncryptionServiceImpl2(String privateKey, String iv) throws UnsupportedEncodingException {
		this.privateKey = privateKey;
		this.ivParmSpec = iv;
	}

	private byte[] getKeyBytes(String key) throws UnsupportedEncodingException {
		byte[] keyBytes = new byte[16];
		byte[] parameterKeyBytes = key.getBytes(characterEncoding);
		System.arraycopy(parameterKeyBytes, 0, keyBytes, 0, Math.min(parameterKeyBytes.length, keyBytes.length));
		return keyBytes;
	}

	public byte[] decrypt(byte[] cipherText, byte[] key, byte[] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(cipherTransformation);
		SecretKeySpec secretKeySpecy = new SecretKeySpec(key, aesEncryptionAlgorithm);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);
		cipherText = cipher.doFinal(cipherText);
		return cipherText;
	}

	public byte[] encrypt(byte[] plainText, byte[] key, byte[] initialVector) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(cipherTransformation);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, aesEncryptionAlgorithm);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initialVector);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		plainText = cipher.doFinal(plainText);
		return plainText;
	}

	@Override
	public String encrypt(String data) throws Exception {
		byte[] plainTextbytes = data.getBytes(characterEncoding);
		byte[] keyBytes = getKeyBytes(privateKey);
		byte[] initIV = getKeyBytes(ivParmSpec);
		return Base64.encodeBase64URLSafeString(encrypt(plainTextbytes, keyBytes, initIV));
	}

	@Override
	public String decrypt(String encryptedData) throws Exception {
		byte[] cipheredBytes = Base64.decodeBase64(encryptedData);
		byte[] keyBytes = getKeyBytes(privateKey);
		byte[] initIV = getKeyBytes(ivParmSpec);
		return new String(decrypt(cipheredBytes, keyBytes, initIV));
	}

	@Override
	public File decryptFile(File encryptedFile) throws Exception {
		LOGGER.info("file decryption started...." + encryptedFile);
		File decryptedFile = null;
		if (encryptedFile != null) {
			FileInputStream fis = null;
			FileOutputStream fos = null;
			CipherInputStream cis = null;
			try {
				// initialize cipher
				byte[] keyBytes = getKeyBytes(privateKey);
				byte[] initIV = getKeyBytes(ivParmSpec);
				Cipher cipher = Cipher.getInstance(cipherTransformation);
				SecretKeySpec secretKeySpecy = new SecretKeySpec(keyBytes, aesEncryptionAlgorithm);
				IvParameterSpec ivParameterSpec = new IvParameterSpec(initIV);
				cipher.init(Cipher.DECRYPT_MODE, secretKeySpecy, ivParameterSpec);

				fis = new FileInputStream(encryptedFile);
				String dir = CommonUtils.createDir(Constants.Temp.getTempFileDir() + "downloadedFiles/");
				decryptedFile = new File(dir + CommonUtils.getFileName(encryptedFile.getAbsolutePath()));

				fos = new FileOutputStream(decryptedFile);
				cis = new CipherInputStream(fis, cipher);
				int read;
				byte buf[] = new byte[4096];
				while ((read = cis.read(buf)) != -1) // reading from file
					fos.write(buf, 0, read); // decrypting and writing to file
				LOGGER.info("decryption completed..." + encryptedFile);
			} finally {
				if (cis != null)
					cis.close();
				if (fis != null)
					fis.close();
				if (fos != null)
					fos.flush();
				if (fos != null)
					fos.close();
			}
		}
		return decryptedFile;
	}

	@Override
	public File encryptFile(File originalFile) throws Exception {
		LOGGER.info("file encryption started...." + originalFile);

		File encryptedFile = null;
		if (originalFile != null) {
			FileInputStream fis = null;
			FileOutputStream fos = null;
			CipherInputStream cis = null;
			try {
				// initialize cipher
				byte[] keyBytes = getKeyBytes(privateKey);
				byte[] initIV = getKeyBytes(ivParmSpec);
				Cipher cipher = Cipher.getInstance(cipherTransformation);
				SecretKeySpec secretKeySpecy = new SecretKeySpec(keyBytes, aesEncryptionAlgorithm);
				IvParameterSpec ivParameterSpec = new IvParameterSpec(initIV);
				cipher.init(Cipher.ENCRYPT_MODE, secretKeySpecy, ivParameterSpec);

				fis = new FileInputStream(originalFile);

				cis = new CipherInputStream(fis, cipher);
				String originalFileName = CommonUtils.getFileName(originalFile.getAbsolutePath());
				String dir = CommonUtils.createDir(Constants.Temp.getTempFileDir() + "encryptedFile/");
				encryptedFile = new File(dir + originalFileName);
				fos = new FileOutputStream(encryptedFile);
				int read;
				byte buf[] = new byte[4096];
				while ((read = cis.read(buf)) != -1) { // reading from file
					fos.write(buf, 0, read);

				} // encrypting and writing to file
			} finally {
				if (cis != null)
					cis.close();
				if (fis != null)
					fis.close();
				if (fos != null)
					fos.flush();
				if (fos != null)
					fos.close();
			}
		}
		return encryptedFile;
	}

}
