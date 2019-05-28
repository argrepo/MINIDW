/**
 * 
 */
package com.anvizent.encryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
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

/**
 * @author rakesh.gajula
 *
 */
public class AnvizentEncryptionServiceImpl2 implements EncryptionService {

	private final String characterEncoding = "UTF-8";
	private final String cipherTransformation = "AES/CBC/PKCS5Padding";
	private final String aesEncryptionAlgorithm = "AES";

	private String privateKey;
	private String ivParmSpec;

	public String getPrivateKey() {
		return privateKey;
	}

	public String getIvParmSpec() {
		return ivParmSpec;
	}

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
	public File decryptFile(File encryptedFile, String destinationFilePath) throws Exception {
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
				decryptedFile = new File(destinationFilePath);

				fos = new FileOutputStream(decryptedFile);
				cis = new CipherInputStream(fis, cipher);
				int read;
				byte buf[] = new byte[4096];
				while ((read = cis.read(buf)) != -1) // reading from file
					fos.write(buf, 0, read); // decrypting and writing to file
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
	public File encryptFile(File originalFile, String destinationFilePath) throws Exception {

		if (destinationFilePath == null) {
			throw new IllegalAccessException("Destination file Path must not be empty");
		}
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
				encryptedFile = new File(destinationFilePath);
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

	@Override
	public File decryptFileStream(InputStream inputStream) throws Exception {
		File decryptedFile = null;
		if (inputStream != null) {
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

				decryptedFile = Files.createTempFile("decryptedTemp", ".properties").toFile();

				fos = new FileOutputStream(decryptedFile);
				cis = new CipherInputStream(inputStream, cipher);
				int read;
				byte buf[] = new byte[4096];
				while ((read = cis.read(buf)) != -1) // reading from file
					fos.write(buf, 0, read); // decrypting and writing to file
			} finally {
				if (cis != null)
					cis.close();
				if (inputStream != null)
					inputStream.close();
				if (fos != null)
					fos.flush();
				if (fos != null)
					fos.close();
			}
		}
		return decryptedFile;
	}

	@Override
	public File encryptFileStream(InputStream inputStream) throws Exception {

		if (inputStream == null) {
			throw new IllegalAccessException("Destination file Path must not be empty");
		}
		File encryptedFile = null;
		if (inputStream != null) {
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

				cis = new CipherInputStream(inputStream, cipher);
				encryptedFile = Files.createTempFile("encryptedTemp", ".properties").toFile();
				fos = new FileOutputStream(encryptedFile);
				int read;
				byte buf[] = new byte[4096];
				while ((read = cis.read(buf)) != -1) { // reading from file
					fos.write(buf, 0, read);
				} // encrypting and writing to file
			} finally {
				if (cis != null) {
					cis.close();
				}

				if (inputStream != null) {
					inputStream.close();
				}

				if (fos != null) {
					fos.flush();
				}

				if (fos != null) {
					fos.close();
				}
			}
		}

		return encryptedFile;
	}
}
