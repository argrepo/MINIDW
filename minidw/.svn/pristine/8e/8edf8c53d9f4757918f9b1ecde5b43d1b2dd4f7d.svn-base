<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
	<div class="page-title-v1">
		<h4>
			<spring:message code="anvizent.package.header" />
		</h4>
	</div>
	<div class="dummydiv"></div>
	<ol class="breadcrumb">
	</ol>
	<jsp:include page="admin_error.jsp"></jsp:include>
	<input type="hidden" id="userID"
		value="<c:out value="${principal.userId}"/>">

	<div class="row form-group">
		<h4 class="alignText">
			<spring:message
				code="anvizent.package.label.dataValidationType" />
		</h4>
	</div>
	<div class="col-sm-12">
			<div class='row form-group'>
				<button type="button" style="float: right; margin-right: 1.5em;"
					class="btn btn-sm btn-success" id="addDataValidationType"> <spring:message
						code="anvizent.package.label.Add" />
				</button>
			</div>
			<div class='row form-group'>
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg "
						id="dataValidationTypeTable">
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.sNo" /></th>
								<th><spring:message
										code="anvizent.package.label.validationTypeName" /></th>
								<th><spring:message
										code="anvizent.package.label.validationTypeDescription" /></th>
								<th><spring:message
										code="anvizent.package.label.validation" /></th>
								<th><spring:message code="anvizent.package.label.isActive" /></th>
								<th><spring:message code="anvizent.package.label.edit" /></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr  class="hidden">
								<td class="dataValidationTypeId" id="dataValidationTypeId"></td>
								<td class="dataValidationTypeName" id="dataValidationTypeName"></td>
								<td class="dataValidationTypeDesc" id="dataValidationTypeDesc"></td>
								<td class="dataValidation" id="dataValidation"></td>
								<td class="active" id="active"></td>
								<td>
									<button class="btn btn-primary btn-sm tablebuttons"
										name="validationTypeEdit" id="validationTypeEdit" data-dataValidationTypeId=""
										title="<spring:message code="anvizent.package.label.edit"/>">
										<i class="fa fa-pencil" aria-hidden="true"></i>
									</button>
								</td>
							</tr>
						</tfoot>
					</table>
				</div>
			</div>
	<div class='row form-group '>
		<div class='col-sm-6'>
			<div class="alert alert-success successMessage"
				style="display: none;">
				<p class="successMessageText"></p>
			</div>
			<div class="alert alert-danger message" style="display: none;">
				<p class="messageText"></p>
			</div>
		</div>
	</div>
		<div class="modal fade" tabindex="-1" role="dialog"
			id="dataValidationTypeDiv" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content"  style="width:125%;">
					<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        	<h4 class="modal-title custom-modal-title datavalidationTypeTitle"><spring:message code="anvizent.package.label.dataValidationType"/></h4>
					      </div>
						<div class="modal-body">
						   <form:form method= "POST" id="jobFileForm_direct" enctype="multipart/form-data">
							<div class="row form-group">
								<label class="control-label col-sm-4"><spring:message
										code="anvizent.package.label.validationTypeName" /> :</label>
								<div class='col-sm-6'>
										<input type="hidden" id="validationTypeID" name="validationTypeId">
									<input type="text" id="validationTypeName" class="form-control" name="validationTypeName"
										data-maxlength="45" />
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-4"><spring:message
										code="anvizent.package.label.validationTypeDescription" /> :</label>
								<div class='col-sm-6'>
									<input type="text" id="validationTypeDesc" class="form-control" name="validationTypeDesc"
										data-maxlength="45" />
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-4"><spring:message
										code="anvizent.package.label.validation" />:</label>
								<div class='col-sm-6 dataValidationSelectDiv'>
								</div>
							</div>

							<div class="row form-group contextParamsDiv">
								<label class="control-label col-sm-4"><spring:message
										code="anvizent.package.label.contextParameters" /> : </label>
								<div class="col-sm-8">
								<select id='contextParameters' name = "contextParameters"  multiple="multiple"  class='dataValidationTypeContextParams form-control' style="white-space:pre-wrap;">
									</select>
								</div>
							</div>
							<div class="row form-group jobNameDiv">
							<spring:message code="anvizent.package.label.jobName" var="jobName"/>			 
							<label class="control-label col-sm-4"><c:out value="${jobName}"/> : </label>					
						   	<div class="col-sm-8">
						    	<input class="form-control jobName" type="text" data-maxlength="500" placeholder="${jobName}" id="jobName" name="jobName" />
						    </div>			   
						</div>	
							
							
							<div class="row form-group jobFileNameDiv">
							<spring:message code="anvizent.package.label.jobFileName" var="jobFileName"/>			 
							<label class="control-label col-sm-4"><c:out value="${jobFileName}"/> : </label>					
						   	<div class="col-sm-8">
						    	<input class="form-control uploadedJobFileNames" type="text" data-maxlength="500" placeholder="${jobFileName}" id="uploadedJobFileNames" name="uploadedJobFileNames" />
						    </div>			   
						</div>	
							
							<div class="jobFilesDiv">
							<label class="control-label col-sm-4 jobfile-label"><spring:message code="anvizent.package.label.jobFile"/> : </label>
				    		<div class="row form-group fileContainer">
					    		 <div class="row form-group fileTypeDiv"></div> 
							    <div class="row form-group jobFileData hidden"></div>
								</div>
						</div>
							<div class="row form-group existedJarFileDiv ">			 
							<label class="control-label col-sm-4"><spring:message code="anvizent.package.label.ExistedJarFiles"/>:</label>					
						   	<div class="col-sm-8">
						   	<select id='existedJarjobFileNames' name = "existedJarjobFileNames"  multiple="multiple"  class='existedJarFile form-control' style="white-space:pre-wrap;">
						    	</select>
						    </div>			   
						</div>
							
							<div class="row form-group">
								<label class="control-label col-sm-4"><spring:message
										code="anvizent.package.label.activeStatus" /> :</label>
								<div class='col-sm-6'>
									<div class="activeStatus">
										<label class="radio-inline"> <input type="radio"
											value="true" id="active" name="active" checked="checked" />Yes
										</label> <label class="radio-inline"> <input type="radio"
											value="false" id="active" name="active" />No
										</label>
									</div>
								</div>
							</div>
							</form:form>
						</div>
						<div class="modal-footer">
					<button type="button" id="saveDataTypeValidation"
						class="btn btn-primary btn-sm">
						<spring:message code="anvizent.package.button.saveDataTypeValidation" />
					</button>
					<button type="button" class="btn btn-primary btn-sm"
						id="editDataTypeValidation" style="display: none" name='editDataTypeValidation'>
						<spring:message code="anvizent.package.button.editDataTypeValidation" />
					</button>
					<button type="button" class="btn btn-primary btn-sm"
						id="updateDataTypeValidation" style="display: none"
						name='updateDataTypeValidation'>
						<spring:message code="anvizent.package.button.updateDataTypeValidation" />
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal" id="btn_Cancel">
						<spring:message code="anvizent.package.button.cancel" />
					</button>
				</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="deleteConnectionAlert" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog">
				    <div class="modal-content">
					      <div class="modal-header">
						        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.button.deleteConnection"/></h4>
					      </div>
					      <div class="modal-body">
						        <p><spring:message code="anvizent.package.message.deletePackage.remoteConnectionWillbeDeleted"/><br>
						        <spring:message code="anvizent.package.message.deletePackage.areYouSureYouWantToDeleteRemoteConnection"/> 
					        </p>
					      </div>
					      <div class="modal-footer">
						        <button type="button" class="btn btn-primary" id="confirmDeleteConnection"><spring:message code="anvizent.package.button.yes"/></button>
						        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
					      </div>
				    </div> 
			  </div> 
		</div>
		 <div class="col-sm-6 job-files-div" style='display:none;'>
    	<div class="row form-group fileContainer">
	    	<label class="control-label col-sm-4 jobfile-label"><spring:message code="anvizent.package.label.jobFile"/> : </label>
	    	<div class="col-sm-6">
	    		<input type="file" class="jobFile" name="jobFile" data-buttonText="Find file">
		    </div>
		    <div class="col-sm-2">
			    <a href="#"  class="btn btn-primary btn-sm addJobFile">
		      		<span class="glyphicon glyphicon-plus"></span>
		    	</a>
		    	<a href="#"  class="btn btn-primary btn-sm remove_field deleteJobFile" style="display:none;">
		      		<span class="glyphicon glyphicon-trash"></span>
		    	</a>
	    	</div>	
	    </div>	
    </div>
	</div>
