package com.datamodel.anvizent.security;

import java.io.File;
import java.io.UnsupportedEncodingException;

final public class EncryptionServiceImpl extends AnvizentEncryptionServiceImpl2 {
	private static final String PRIVATE_KEY = "anvizent";
	// must be a 16 bit string(16 characters)
	private static final String IV = "AnvizentDMT IV16";

	private static EncryptionServiceImpl encryptionService;

	static {
		try {
			encryptionService = new EncryptionServiceImpl();
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException();
		}
	}

	private EncryptionServiceImpl() throws UnsupportedEncodingException {
		super(PRIVATE_KEY, IV);
	}

	static public EncryptionServiceImpl getInstance() {
		return encryptionService;
	}

	public static void main(String[] args) {
		try {

			String encrypted = EncryptionServiceImpl.getInstance().encrypt("1009053");
			String decrypted = EncryptionServiceImpl.getInstance().decrypt("bxu1CKOPNDlk-JB8-1iMSQ");
			//File file = EncryptionServiceImpl.getInstance().encryptFile(new File("C:/Users/rakesh.gajula/Desktop/R1.xls"));
			//EncryptionServiceImpl.getInstance().decryptFile(file);
			System.out.println("encrypted>>>" + encrypted);
			System.out.println("decrypted>>>" + decrypted);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Unexpected Error: " + ex.getMessage());
		}
	}

}
