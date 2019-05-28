package com.anvizent.minidw.service.utils.helper;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class CSVWriter {

	public void writeAsCSV(List<LinkedHashMap<String, Object>> flatJson, String fileName) throws FileNotFoundException {
		List<String> headers = collectHeaders(flatJson);
		String output = StringUtils.join(headers.toArray(), ",") + System.getProperty("line.separator");
		int i = 1;
		for (Map<String, Object> map : flatJson) {
			if (i > 1)
				output = output + getCommaSeperatedRow(headers, map) + System.getProperty("line.separator");
			i++;
		}
		writeToFile(output, fileName);
	}

	private void writeToFile(String output, String fileName) throws FileNotFoundException {
		BufferedWriter writer = null;
		try {
			System.out.println(fileName);

			writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(writer);
		}
	}

	private void close(BufferedWriter writer) {
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getCommaSeperatedRow(List<String> headers, Map<String, Object> map) {
		List<String> items = new ArrayList<String>();
		for (String header : headers) {
			String value = map.get(header) == null ? "" : CommonUtils.sanitizeForCsv(map.get(header).toString());
			items.add(value);
		}
		return StringUtils.join(items.toArray(), ",");
	}

	public static  List<String> collectHeaders(List<LinkedHashMap<String, Object>> flatJson) {
		List<String> headers = new ArrayList<String>();
		for (Map<String, Object> map : flatJson) {
			List<String> keyList = new ArrayList<String>(map.keySet());
			for (String key : keyList) {
				headers.add(key.replaceAll("\\s+", "_").replaceAll("\\W+", "_"));
			}
			break;
		}
		return headers;
	}
	
	public static List<LinkedHashMap<String, Object>> getFormattedData(List<String> headers , List<LinkedHashMap<String, Object>> data) {

		List<LinkedHashMap<String, Object>> finalData = new ArrayList<>();

		for (LinkedHashMap<String, Object> row : data) {
			LinkedHashMap<String, Object> map = new LinkedHashMap<>();
			for (String header : headers) {
				String value = row.get(header) == null ? "" : CommonUtils.sanitizeForCsv(row.get(header).toString());
				map.put(header, value);
			}
			finalData.add(map);
		}
		return finalData;
	}
}
