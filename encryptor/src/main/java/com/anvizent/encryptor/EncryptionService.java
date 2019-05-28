package com.anvizent.encryptor;

import java.io.File;
import java.io.InputStream;

/**
 * Encryption / Decryption services.
 * 
 * @author rakesh.gajula
 *
 */
public interface EncryptionService {
	String encrypt(String data) throws Exception;

	String decrypt(String encryptedData) throws Exception;

	File decryptFile(File encryptedFile, String destinationFilePath) throws Exception;

	File encryptFile(File file, String destinationFilePath) throws Exception;

	File decryptFileStream(InputStream inputStream) throws Exception;

	File encryptFileStream(InputStream inputStream) throws Exception;
}
