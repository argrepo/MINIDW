package com.datamodel.anvizent.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ArrayListOfKeyValuePair<K, V> implements Serializable {
	private static final long serialVersionUID = 1L;

	private HashSet<K> keySet = new HashSet<>();

	public class Entry implements Serializable {
		private static final long serialVersionUID = 1L;

		private K key;
		private V value;

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public Entry() {
		}

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((key == null) ? 0 : key.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Entry other = (Entry) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (key == null) {
				if (other.key != null)
					return false;
			} else if (!key.equals(other.key))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		@SuppressWarnings("rawtypes")
		private ArrayListOfKeyValuePair getOuterType() {
			return ArrayListOfKeyValuePair.this;
		}
	}

	private ArrayList<Entry> data = new ArrayList<>(); 

	public ArrayList<ArrayListOfKeyValuePair<K, V>.Entry> getData() {
		return data;
	}

	public int size() {
		return data.size();
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public boolean contains(K k, V v) {
		return data.contains(new Entry(k, v));
	}

	public Iterator<ArrayListOfKeyValuePair<K, V>.Entry> iterator() {
		return data.iterator();
	}

	public Object[] toArray() {
		return data.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return data.toArray(a);
	}

	public boolean add(K k, V v) {
		keySet.add(k);
		return data.add(new Entry(k, v));
	}

	public boolean remove(K k, V v) {
		keySet.remove(k);
		return data.remove(new Entry(k, v));
	}

	public void clear() {
		keySet.clear();
		data.clear();
	}

	public ArrayListOfKeyValuePair<K, V>.Entry get(int index) {
		return data.get(index);
	}

	public ArrayListOfKeyValuePair<K, V>.Entry set(int index, K k, V v) {
		keySet.add(k);
		return data.set(index, new Entry(k, v));
	}

	public void add(int index, K k, V v) {
		keySet.add(k);
		data.add(index, new Entry(k, v));
	}

	public ArrayListOfKeyValuePair<K, V>.Entry remove(int index) {
		keySet.remove(data.get(index).getKey());
		return data.remove(index);
	}

	public int indexOf(K k, V v) {
		return data.indexOf(new Entry(k, v));
	}

	public int lastIndexOf(K k, V v) {
		return data.lastIndexOf(new Entry(k, v));
	}

	public ListIterator<ArrayListOfKeyValuePair<K, V>.Entry> listIterator() {
		return data.listIterator();
	}

	public ListIterator<ArrayListOfKeyValuePair<K, V>.Entry> listIterator(int index) {
		return data.listIterator(index);
	}

	public List<ArrayListOfKeyValuePair<K, V>.Entry> subList(int fromIndex, int toIndex) {
		return data.subList(fromIndex, toIndex);
	}
}