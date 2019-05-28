package com.anvizent.encryptor;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.anvizent.encryptor.AnvizentEncryptionServiceImpl2;

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

			//String encrypted = EncryptionServiceImpl.getInstance().encrypt("jG0IeaFw8hbhaBhtPkBFjw");
			System.out.println(new Date());
			File encryptedFile = EncryptionServiceImpl.getInstance().encryptFile(new File("C:/Users/usharani.konda/Desktop/netbeans-8.2-windows.exe"),"");
			//System.out.println(EncryptionServiceImpl.getInstance().encrypt("I_j4tSypkZVK-j4Sb-x7VzeyrY76f5hrwej9KjEd5zYYRBzhIZkO915FKsHe1b7m" + "#" + new Date()));
			
			//String decrypted = EncryptionServiceImpl.getInstance().decrypt(encrypted);
			//System.out.println("encrypted>>>" + encrypted);
			System.out.println(new Date());
			//System.out.println("decrypted>>>" + decrypted);
			System.out.println(encryptedFile.getAbsolutePath());
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Unexpected Error: " + ex.getMessage());
		}
	}

}
