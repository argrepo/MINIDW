/**
 * 
 */
package com.datamodel.anvizent.security;

import java.net.URLEncoder;

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

	// Allows a key size of 16,24,32 bytes
	private static final byte[] KEYVALUE = new byte[] { 'A', 'n', 'V', 'i', 'Z', 'e', 'N', 't', 'A', 'n', 'A', 'l', 'Y', 't', 'I', 'c' };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String enc = "superadmin@anvizent.com#$#test#$#-1#$#Sat Oct 21 21:55:20 IST 2017";
		String enc = "rajesh.anthari@anvizent.com#$#test@123#$#1009019#$#Sat Oct 21 21:55:20 IST 2017";
		//String enc = "1009007";
		String deCrypt = "";
		try {
			deCrypt = AESConverter.decrypt("I0p3Uycm9xWWmmdoWh1KgA==");
			enc = AESConverter.encrypt(enc);
			System.out.println(URLEncoder.encode(enc, "UTF-8"));;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Encrypted : " + enc + "\nDeCrypted : " + deCrypt);
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
