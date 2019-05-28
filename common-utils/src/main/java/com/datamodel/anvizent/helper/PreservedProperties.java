package com.datamodel.anvizent.helper;

import java.util.Properties;
import java.util.Set;

public class PreservedProperties extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayListOfKeyValuePair<Object, Object> linkedMapProperties = new ArrayListOfKeyValuePair<>(); 

	@Override
	public synchronized Object put(Object key, Object value) {
		return linkedMapProperties.add(key, value);
	}

//	@Override
//	public Set<Object> keySet() {
//		return linkedMapProperties.getKeySet();
//	}

	public ArrayListOfKeyValuePair<Object, Object> getPropertiesMap() {
		return linkedMapProperties;
	}

}
