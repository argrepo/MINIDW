package com.anvizent.encryptor;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;

final public class EncryptionUtility {

	private AnvizentEncryptionServiceImpl2 encryptionService;

	public EncryptionUtility(String privateKey, String iv) throws UnsupportedEncodingException {

		encryptionService = new AnvizentEncryptionServiceImpl2(privateKey, iv);
	}

	public String encrypt(String data) throws Exception {
		return encrypt(data, null);
	}

	public String encrypt(String data, Boolean console) throws Exception {
		if (console != null) {
			if (console) {
				System.out.println("privateKey: " + encryptionService.getPrivateKey() + ", iv: " + encryptionService.getIvParmSpec());
			} else {
				JOptionPane.showMessageDialog(null, "privateKey: " + encryptionService.getPrivateKey() + ", iv: " + encryptionService.getIvParmSpec());
			}
		}

		return encryptionService.encrypt(data);
	}

	public String decrypt(String data) throws Exception {
		return decrypt(data, null);
	}

	public String decrypt(String data, Boolean console) throws Exception {
		if (console != null) {
			if (console) {
				System.out.println("privateKey: " + encryptionService.getPrivateKey() + ", iv: " + encryptionService.getIvParmSpec());
			} else {
				JOptionPane.showMessageDialog(null, "privateKey: " + encryptionService.getPrivateKey() + ", iv: " + encryptionService.getIvParmSpec());
			}
		}

		return encryptionService.decrypt(data);
	}

	public File encryptFile(File originalFile, String destinationFilePath) throws Exception {
		return encryptionService.encryptFile(originalFile, destinationFilePath);
	}

	public File decryptFile(File encryptedFile, String destinationFilePath) throws Exception {
		return encryptionService.decryptFile(encryptedFile, destinationFilePath);
	}

	public File decryptFileStream(InputStream inputStream) throws Exception {
		return encryptionService.decryptFileStream(inputStream);
	}

	public File encryptFileStream(InputStream inputStream) throws Exception {
		return encryptionService.encryptFileStream(inputStream);
	}

}