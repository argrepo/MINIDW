/**
 * 
 */
package com.datamodel.anvizent.service.model;

import java.util.List;

/**
 * @author rakesh.gajula
 *
 */
public class Database {

	private int id;
	private String name;
	private int connector_id;
	private List<Integer> ids;
	private String schema;
	private List<String> schemas;
	private Table table;
	private Boolean isActive;
	private Modification modification;
	private String connectorName;
	private String driverName; 
	private String protocal;
	private String connectionStringParams;
	private String urlFormat; 
	private Boolean isDefault;
	private String connectorJars;
	private List<String> conJars;
	
	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	public Modification getModification() {
		return modification;
	}

	public void setModification(Modification modification) {
		this.modification = modification;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Database(){};
	
	public Database(int id){
		this.id=id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Integer> getIds() {
		return ids;
	}
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public List<String> getSchemas() {
		return schemas;
	}
	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}

	public int getConnector_id() {
		return connector_id;
	}

	public void setConnector_id(int connector_id) {
		this.connector_id = connector_id;
	}

	
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getProtocal() {
		return protocal;
	}

	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}

	public String getConnectionStringParams()
	{
		return connectionStringParams;
	}

	public void setConnectionStringParams(String connectionStringParams)
	{
		this.connectionStringParams = connectionStringParams;
	}

	public String getUrlFormat() {
		return urlFormat;
	}

	public void setUrlFormat(String urlFormat) {
		this.urlFormat = urlFormat;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getConnectorJars() {
		return connectorJars;
	}

	public void setConnectorJars(String connectorJars) {
		this.connectorJars = connectorJars;
	}

	public List<String> getConJars() {
		return conJars;
	}

	public void setConJars(List<String> conJars) {
		this.conJars = conJars;
	}

	@Override
	public String toString()
	{
		return "Database [id=" + id + ", name=" + name + ", connector_id=" + connector_id + ", ids=" + ids + ", schema=" + schema + ", schemas=" + schemas + ", table=" + table + ", isActive=" + isActive + ", modification=" + modification + ", connectorName=" + connectorName + ", driverName="
				+ driverName + ", protocal=" + protocal + ", connectionStringParams=" + connectionStringParams + ", urlFormat=" + urlFormat + ", isDefault=" + isDefault + ", connectorJars=" + connectorJars + ", conJars=" + conJars + "]";
	}

}
