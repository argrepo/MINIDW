package com.datamodel.anvizent.helper;

public enum CrossReferenceConstants {
	MERGER("merge"), SPLIT("split"),
	MANUALMERGE("manualmerge"), BULKMERGE("bulkmerge"), AUTOMERGE("automerge"),
	NEW_XREF("newXref"),EXISTING_XREF("existingXref");
	
	String value;
	public String toString() {
		return this.value;
	}

	private CrossReferenceConstants(String value) {
		this.value = value;
	}
	
}
