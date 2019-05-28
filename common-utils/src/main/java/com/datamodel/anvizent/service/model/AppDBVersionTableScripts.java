package com.datamodel.anvizent.service.model;

public class AppDBVersionTableScripts {
	private int id;
	private String version;
	private String appDbScript;
	private String minidwScript;
	private Boolean defaultScript;
	private String pageMode;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAppDbScript() {
		return appDbScript;
	}
	public void setAppDbScript(String appDbScript) {
		this.appDbScript = appDbScript;
	}
	public String getMinidwScript() {
		return minidwScript;
	}
	public void setMinidwScript(String minidwScript) {
		this.minidwScript = minidwScript;
	}
	public Boolean getDefaultScript() {
		return defaultScript;
	}
	public void setDefaultScript(Boolean defaultScript) {
		this.defaultScript = defaultScript;
	}
	public String getPageMode() {
		return pageMode;
	}
	public void setPageMode(String pageMode) {
		this.pageMode = pageMode;
	}
	@Override
	public String toString() {
		return "AppDBVersionTableScripts [id=" + id + ", version=" + version + ", appDbScript=" + appDbScript
				+ ", minidwScript=" + minidwScript + ", defaultScript=" + defaultScript + ", pageMode=" + pageMode
				+ "]";
	}
	
}
