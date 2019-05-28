package com.datamodel.anvizent.security;

import java.io.UnsupportedEncodingException;

final public class EncryptionServiceImpl extends AnvizentEncryptionServiceImpl2 {
	private static final String PRIVATE_KEY = "anvizent";
	private static final String IV = "AnvizentDMT IV16"; // must be a 16 bit
															// string(16
															// characters)

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

			String encrypted = EncryptionServiceImpl.getInstance().encrypt("");
			String decrypted = EncryptionServiceImpl.getInstance().decrypt(encrypted);
			System.out.println("encrypted>>>" + encrypted);
			System.out.println("decrypted>>>" + decrypted);

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Unexpected Error: " + ex.getMessage());
		}
	}

}
