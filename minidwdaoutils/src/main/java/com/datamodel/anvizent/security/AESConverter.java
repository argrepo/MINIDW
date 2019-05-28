/**
 * 
 */
package com.datamodel.anvizent.security;

/**
 * @author rakesh.gajula
 *
 */

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AESConverter {
	
	private static final String ALGORITHM = "AES";

	//Allows a key size of 16,24,32 bytes
	private static final byte[] KEYVALUE = new byte[] { 'A', 'n', 'V', 'i', 'Z', 'e', 'N', 't', 'A', 'n', 'A','l','Y','t','I','c' };
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String test = "test@123";
		String enc="";
		String deCrypt="";
		try {
			enc = AESConverter.encrypt(test);
			deCrypt = AESConverter.decrypt("raA50VB/bjpKPxOqEZODi11z/GoXiz+p40kIF+qsc7c=");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Encrypted : "+enc+"\nDeCrypted : "+deCrypt);
	}
	
	
	 /**
	 * @param valueToEnc
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String valueToEnc) throws Exception {
	    Key key = generateKey();
	    Cipher c = Cipher.getInstance(ALGORITHM);
	    c.init(Cipher.ENCRYPT_MODE, key);
	    byte[] encValue = c.doFinal(valueToEnc.getBytes());
	    String encryptedValue = DatatypeConverter.printBase64Binary(encValue);
	    return encryptedValue;
	}
	
	 
	 /**
	 * @param encryptedValue
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String encryptedValue) throws Exception {
		    Key key = generateKey();
		    Cipher c = Cipher.getInstance(ALGORITHM);
		    c.init(Cipher.DECRYPT_MODE, key);
		    byte[] decordedValue = DatatypeConverter.parseBase64Binary(encryptedValue);
		    byte[] decValue = c.doFinal(decordedValue);
		    String decryptedValue = new String(decValue);
		    return decryptedValue;
		}
	 
	 
	/**
	 * @return
	 * @throws Exception
	 */
	private static Key generateKey() throws Exception {
	    Key key = new SecretKeySpec(KEYVALUE, ALGORITHM);
	    return key;
	}

}

