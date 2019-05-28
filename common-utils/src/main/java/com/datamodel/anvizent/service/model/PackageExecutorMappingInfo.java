package com.datamodel.anvizent.service.model;

/**
 * @author mahender.alaveni
 *
 */
public class PackageExecutorMappingInfo {
	private long id;
	private long executionId;
	private PackageExecution packageExecution;
	private long ilConMappingId;
	private long sourceFileInfoId;
	private Modification modification;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getExecutionId() {
		return executionId;
	}

	public void setExecutionId(long executionId) {
		this.executionId = executionId;
	}

	public PackageExecution getPackageExecution() {
		return packageExecution;
	}

	public void setPackageExecution(PackageExecution packageExecution) {
		this.packageExecution = packageExecution;
	}

	public long getIlConMappingId() {
		return ilConMappingId;
	}

	public void setIlConMappingId(long ilConMappingId) {
		this.ilConMappingId = ilConMappingId;
	}

	public long getSourceFileInfoId() {
		return sourceFileInfoId;
	}

	public void setSourceFileInfoId(long sourceFileInfoId) {
		this.sourceFileInfoId = sourceFileInfoId;
	}

	public Modification getModification() {
		return modification;
	}

	public void setModification(Modification modification) {
		this.modification = modification;
	}

	@Override
	public String toString() {
		return "PackageExecutorMappingInfo [id=" + id + ", executionId=" + executionId + ", packageExecution="
				+ packageExecution + ", ilConMappingId=" + ilConMappingId + ", sourceFileInfoId=" + sourceFileInfoId
				+ ", modification=" + modification + "]";
	}

}
