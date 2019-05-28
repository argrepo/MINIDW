package com.anvizent.encryptor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class App {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		if (args != null && args.length > 1) {
			Scanner scanner = null;
			String text;
			if (args.length < 3) {
				System.out.print("Enter text to encrypt: ");
				scanner = new Scanner(System.in);
				text = scanner.nextLine();
			} else {
				text = args[2];
			}
			try {
				EncryptionUtility encryptionUtility = new EncryptionUtility(args[0], args[1]);
				System.out.println("Result: " + encryptionUtility.encrypt(text, true));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (scanner != null) {
					scanner.close();
				}
			}
		} else {
			String privateKey = JOptionPane.showInputDialog("Enter privateKey: ");
			if (privateKey == null || privateKey.isEmpty()) {
				JOptionPane.showMessageDialog(null, "privateKey not provided");
			} else {
				String iv = JOptionPane.showInputDialog("Enter iv: ");
				if (iv == null || iv.isEmpty()) {
					JOptionPane.showMessageDialog(null, "iv not provided");
				} else {
					EncryptionUtility encryptionUtility = new EncryptionUtility(privateKey, iv);
					String text = JOptionPane.showInputDialog("Enter text to encrypt");
					if (text == null || text.isEmpty()) {
						JOptionPane.showMessageDialog(null, "No message provided");
					} else {
						try {
							JTextArea ta = new JTextArea(4, 10);
							ta.setText(encryptionUtility.encrypt(text, false));
							ta.setWrapStyleWord(true);
							ta.setLineWrap(true);
							ta.setCaretPosition(0);
							ta.setEditable(false);

							JOptionPane.showMessageDialog(null, new JScrollPane(ta), "RESULT", JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		}
	}
}