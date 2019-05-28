package com.anvizent.minidw.service.utils.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
	private final InputStream inputStream;
	private String output = "";

	public StreamGobbler(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getOutput() {
		return output;
	}

	public void run() {
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				output += line + "\n";
			}
		} catch (IOException e) {
			output = null;
		} finally {
			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}
	}

}
