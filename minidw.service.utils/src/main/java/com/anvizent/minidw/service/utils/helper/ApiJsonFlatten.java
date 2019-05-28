package com.anvizent.minidw.service.utils.helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ApiJsonFlatten {

	private List<LinkedHashMap<String, Object>> rows = new ArrayList<LinkedHashMap<String, Object>>();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ApiJsonFlatten(List response) {
		Iterator<LinkedHashMap<String, Object>> iterator = response.iterator();
		while (iterator.hasNext()) {
			rows.addAll(addAll(iterator.next(), ""));
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ApiJsonFlatten(List response, String id) {
		Iterator<LinkedHashMap<String, Object>> iterator = response.iterator();
		while (iterator.hasNext()) {
			LinkedHashMap<String, Object> map = iterator.next();
			if (id != null) {
				((Map) map.get("values")).put(id, map.get(id));
			}
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (entry.getKey().equals("values")) {
					LinkedHashMap<String, Object> subMap = (LinkedHashMap<String, Object>) entry.getValue();
					rows.addAll(addAll(subMap, ""));
				}
			}

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ApiJsonFlatten(Map response, String id) {
		Iterator entries = response.entrySet().iterator();
		if (id != null) {
			((Map) response.get("values")).put(id, response.get(id));
		}

		while (entries.hasNext()) {
			Entry entry = (Entry) entries.next();
			if (entry.getKey().equals("values")) {
				LinkedHashMap<String, Object> subMap = (LinkedHashMap<String, Object>) entry.getValue();
				rows.addAll(addAll(subMap, ""));
			}

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<LinkedHashMap<String, Object>> addAll(LinkedHashMap<String, Object> map, String prefix) {

		List<LinkedHashMap<String, Object>> currentResultsList = new ArrayList();

		LinkedHashMap<String, List<LinkedHashMap<String, Object>>> childsWithList = new LinkedHashMap();

		LinkedHashMap<String, LinkedHashMap<String, Object>> childsWithOutList = new LinkedHashMap<String, LinkedHashMap<String, Object>>();

		LinkedHashMap<String, Object> currentResults = new LinkedHashMap<String, Object>();

		for (Map.Entry<String, Object> entry : map.entrySet()) {

			if (entry.getValue() instanceof List) {

				List<LinkedHashMap<String, Object>> listValue = (List<LinkedHashMap<String, Object>>) entry.getValue();
				if (listValue != null && listValue.size() != 0) {
					childsWithList.put(entry.getKey(), listValue);
				}

			} else if (entry.getValue() instanceof Map) {

				childsWithOutList.put(entry.getKey(), (LinkedHashMap<String, Object>) entry.getValue());

			} else {

				currentResults.put(((prefix != null && !prefix.isEmpty()) ? prefix + "_" + entry.getKey() : entry.getKey()), entry.getValue());

			}
		}
		currentResultsList.add(currentResults);

		for (Entry<String, LinkedHashMap<String, Object>> entry : childsWithOutList.entrySet()) {
			String newPrefix = ((prefix != null && !prefix.isEmpty()) ? prefix + "_" + entry.getKey() : entry.getKey());

			List<LinkedHashMap<String, Object>> list = addAll(entry.getValue(), newPrefix);

			currentResultsList = crossJoinMap(currentResultsList, list);

		}

		for (Entry<String, List<LinkedHashMap<String, Object>>> entry : childsWithList.entrySet()) {
			List<LinkedHashMap<String, Object>> currentResultsListClone = new ArrayList(currentResultsList);

			List<LinkedHashMap<String, Object>> currentResultsListTemp = new ArrayList();

			String newPrefix = ((prefix != null && !prefix.isEmpty()) ? prefix + "_" + entry.getKey() : entry.getKey());

			for (Object mapObj : entry.getValue()) {
				if (mapObj instanceof Map) {
					LinkedHashMap<String, Object> map1 = (LinkedHashMap<String, Object>) mapObj;
					List<LinkedHashMap<String, Object>> list = addAll(map1, newPrefix);
					currentResultsListTemp.addAll(crossJoinMap(currentResultsListClone, list));
				}

			}
			if ( currentResultsListTemp != null && !currentResultsListTemp.isEmpty() ) {
				currentResultsList = currentResultsListTemp;
			}

		}
		return currentResultsList;

	}

	public static List<LinkedHashMap<String, Object>> crossJoinMap(List<LinkedHashMap<String, Object>> list1, List<LinkedHashMap<String, Object>> list2) {

		List<LinkedHashMap<String, Object>> crossJoinlist = new ArrayList<LinkedHashMap<String, Object>>();

		for (int i = 0; i < list1.size(); i++) {
			for (int j = 0; j < list2.size(); j++) {
				LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>(list1.get(i));
				map.putAll(list2.get(j));
				crossJoinlist.add(map);
			}
		}
		return crossJoinlist;
	}

	public List<LinkedHashMap<String, Object>> getFlattenJson() {

		return rows;
	}

}
