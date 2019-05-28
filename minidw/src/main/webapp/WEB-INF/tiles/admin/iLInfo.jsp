<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<spring:message code="anvizent.package.label.yes" var="yesVar"/>
<spring:message code="anvizent.package.label.no" var="noVar"/>
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.iLInfo"/></h4>
 	</div>
	<jsp:include page="admin_error.jsp"></jsp:include>
 	
 	<input type="hidden" id="userId" value="${principal.userId}">
	
	<c:url value="/admin/iLInfo" var="url"/>
	<input type="hidden" value="${url}/edit" id="editIL"/>
	<input type="hidden" value="${url}/update" id="updateIL"/>
	<input type="hidden" value="${url}/create" id="createIL"/>
	
	<form:form modelAttribute="ilInfoForm" enctype="multipart/form-data">
		<c:if test="${ilInfoForm.pageMode == 'iLsList'}">
			<form:hidden path="iLId"/>
			<div style="padding:0px 15px;">
				<div class="row form-group" style="padding:5px;border-radius:4px;">
					<a href='<c:url value="/admin/iLInfo/add"></c:url>' class="btn btn-primary btn-sm createpackage startLoader" id="addIL" style="float:right;"><spring:message code="anvizent.package.button.add"/></a>			
				</div>
			</div>
			
			<div class='row form-group'>
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg " id="existingILsTable">
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.ilId"/></th>
								<th><spring:message code="anvizent.package.label.ilName"/></th>
								<th><spring:message code="anvizent.package.label.version"/></th>	
								<th><spring:message code="anvizent.package.label.iLType"/></th>						
								<th><spring:message code="anvizent.package.label.iLTableName"/></th>
								<th><spring:message code="anvizent.package.label.isActive"/></th>
								<th><spring:message code="anvizent.package.label.createdDate"/></th>
								<th><spring:message code="anvizent.package.label.modifiedDate"/></th>
								<th><spring:message code="anvizent.package.label.viewDetails"/></th>
								<th><spring:message code="anvizent.package.label.edit"/></th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty iLInfoList}">					
								<c:forEach items="${iLInfoList}" var="iLInfo" varStatus="loop">
									<tr>
										<td><c:out value="${iLInfo.iL_id}"/></td>
										<td><c:out value="${iLInfo.iL_name}" escapeXml="true"/> </td>
										<td>${not empty iLInfo.version ? iLInfo.version : '-'}</td>
										<td><c:out value="${iLInfo.iLType == 'D'? 'Dimension' : 'Transaction'}" escapeXml="true"/></td>
										<td><c:out value="${iLInfo.iL_table_name}" escapeXml="true"/> </td>
										<td>${iLInfo.isActive == true ? yesVar : noVar}</td>
										<td>${not empty iLInfo.modification.createdTime ? iLInfo.modification.createdTime : '-'}</td>
										<td>${not empty iLInfo.modification.modifiedTime ? iLInfo.modification.modifiedTime : '-'}</td>
										<td><input type="button" class="btn btn-primary btn-sm tablebuttons viewILDetails" data-ilid="${iLInfo.iL_id}" value="<spring:message code="anvizent.package.label.viewDetails"/>"></td>
										<td>
											<button type="button" class="btn btn-primary btn-sm tablebuttons iLId startLoader" value="${iLInfo.iL_id}" title="<spring:message code="anvizent.package.label.edit"/>" >
												<i class="fa fa-pencil" aria-hidden="true"></i>
											</button>
										</td>
									</tr>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
		    </div>		
		</c:if>
		
		<c:if test="${ilInfoForm.pageMode == 'editIL' || ilInfoForm.pageMode == 'createIL'}">
			<div class="col-sm-12">
		    	<div class="panel panel-default">
		    		<div class="panel-heading">${ilInfoForm.pageMode == 'editIL' ? 'Update IL Info' : 'Add IL Info'}</div>
		    		<div class="panel-body">
		    			<div class="row form-group" >			 
							<spring:message code="anvizent.package.label.ilName" var="iLName"/>
							<label class="control-label col-sm-2"><c:out value="${iLName}"/> : </label>					
						   	<div class="col-sm-8">
						   		<form:hidden path="iLId"/>
						   		<form:input path="iLName" cssClass="form-control iLName" data-minlength="1" data-maxlength="255" placeholder="${iLName}"/>
						    </div>			   
						</div>
						
						<div class="row form-group">
							<spring:message code="anvizent.package.label.version" var="version"/>
			          		<label class="control-label col-sm-2"><c:out value="${version}"/> :</label>
			          		<div class="col-sm-8">
			          			<form:input path="version" cssClass="form-control version" data-minlength="1" data-maxlength="45" placeholder="${version}"/>
			          		</div>
			          	</div>
						
						<div class="row form-group">			 
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.iLType"/> :</label>
						   	<div class="col-sm-8">									    	
						    	<label class="radio-inline control-label">
							    	<form:radiobutton path="iLType" value="D" checked="checked"/><spring:message code="anvizent.package.label.dimension"/>
							    </label>
							    <label class="radio-inline control-label">
							    	<form:radiobutton path="iLType" value="T"/><spring:message code="anvizent.package.label.transaction"/>
							    </label>	
						    </div>			   
						</div>
						
						<div class="row form-group">			 
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.iLDescription"/> :</label>
						   	<div class="col-sm-8">
						   		<spring:message code="anvizent.package.label.description" var="desciption"/>
						    	<form:textarea path="iLDescription" cssClass="form-control iLDescription" placeholder="${desciption}"/>
						    </div>	 		   
						</div>
						
						<div class="row form-group">			 
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.iLTableName"/> : </label>					
						   	<div class="col-sm-8">
						   		<spring:message code="anvizent.package.label.tableName" var="tableName"/>
						    	<form:input path="iLTableName" cssClass="form-control iLTableName" data-maxlength="255" placeholder="${tableName}"/>
						    </div>			   
						</div>
						
						<div class="row form-group">			 
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.xrefILTableName"/> : </label>					
						   	<div class="col-sm-8">
						   		<form:input path="xrefILTableName" cssClass="form-control xrefILTableName" data-maxlength="255" 
						   			disabled="${ilInfoForm.iLType == 'T' ? 'true' : 'false'}" placeholder="Xref ${tableName}"/>
						    </div>			   
						</div>
						
						<div class="purgeScriptsDiv">	
							<div class="row form-group scriptContainer">
								<spring:message code="anvizent.package.label.purgeScript" var="purgeScript"/>	
								<label class="control-label col-sm-2 purgescript-label">${purgeScript} : </label>
								<div class="col-sm-8">
							    	<form:textarea path="iLPurgeScript" class="form-control iLPurgeScript" placeholder="${purgeScript}"/>
							    </div>
							</div>
						</div>	
						
					
						
						
						
						<div class="row form-group">			 
							<label class="control-label col-sm-2">Job Execution Type :</label>
						   	<div class="col-sm-8 jobExecution">									    	
						    	<label class="radio-inline control-label">
							    	<form:radiobutton path="jobExecutionType" value="E"/>ELT
							    </label>
							    <label class="radio-inline control-label">
							    	<form:radiobutton path="jobExecutionType" value="T"/>Talend
							    </label>	
						    </div>			   
						</div>
						
						
						
						<div class="row form-group jobTagsDiv hidden">			 
							<label class="control-label col-sm-2">Job Tags: </label>					
						   	<div class="col-sm-8">
						    	<form:select path="jobTagId" cssClass="jobTags form-control" style="white-space:pre-wrap;">
						    		<form:option value="0">Select</form:option>
						    		<form:options items="${getJobTags}"/>
						    	</form:select>
						    </div>			   
						</div>
						
						<div class="row form-group loadParametersDiv hidden">			 
							<label class="control-label col-sm-2">Master Configuration: </label>					
						   	<div class="col-sm-8">
						    	<form:select path="masterParameterId" cssClass="masterParameterIds form-control" style="white-space:pre-wrap;">
									<form:option value="0">default</form:option>
						    		<form:options items="${getMasterConfiguration}"/>
						    	</form:select>
						    </div>			   
						</div>
						
						<div class="row form-group loadParametersDiv hidden">			 
							<label class="control-label col-sm-2">Load Parameters: </label>					
						   	<div class="col-sm-8">
						    	<form:select path="loadParameterId" cssClass="loadParameters form-control" style="white-space:pre-wrap;">
									<form:option value="0">Select</form:option>
						    		<form:options items="${getLoadParameters}"/>
						    	</form:select>
						    </div>			   
						</div>
						
			
						<div class="row form-group contextParamsDiv hidden">			 
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.contextParameters"/> : </label>					
						   	<div class="col-sm-8">
						    	<form:select path="contextParamsList" cssClass="iLContextParams form-control" style="white-space:pre-wrap;">
						    		<form:options items="${getContextParameters}"/>
						    	</form:select>
						    </div>			   
						</div>

						<div class="row form-group jobNameDiv hidden">
							<spring:message code="anvizent.package.label.jobName" var="jobName"/>			 
							<label class="control-label col-sm-2"><c:out value="${jobName}"/> : </label>					
						   	<div class="col-sm-8">
						    	<form:input path="jobName" cssClass="form-control jobName" data-maxlength="500" placeholder="${jobName}"/>
						    </div>			   
						</div>	
						
						<div class="row form-group jobFileNameDiv hidden">
							<spring:message code="anvizent.package.label.jobFileName" var="jobFileName"/>
							<label class="control-label col-sm-2 jobfile-label"><c:out value="${jobFileName}"/> : </label>
					    	<div class="col-sm-8">
					    		<form:hidden path="uploadedJobFileNames"/>
					    		<input class="uploadedJobFileNames form-control" disabled="disabled" value="${ilInfoForm.uploadedJobFileNames}" placeholder="${jobFileName}"/>
						    </div>
						</div>
						
						<div class="jobFilesDiv hidden">
							<c:forEach var="fileNames" items="${ilInfoForm.jobFileNames}" varStatus="loop">
								<div class="row form-group fileContainer">
									<c:if test="${loop.index == 0}">
										<label class="control-label col-sm-2 jobfile-label"><spring:message code="anvizent.package.label.jobFile"/> : </label>
									</c:if>
									<c:if test="${loop.index > 0}">
										<label class="control-label col-sm-2 jobfile-label"></label>
									</c:if>
									<div class="col-sm-8">
										<label class="checkbox" style="margin-left: 20px;">
										  	<input type="checkbox" name="useOldJobFile" class="useOldJobFile" checked="checked">
										</label>
										<h5 class="jobFileName" style="margin-left: 20px;"><c:out value="${fileNames}"/></h5>
										<form:hidden path="jobFileNames[${loop.index}]" cssClass="jobFileNames"/>	
								    </div>
								    <div class="col-sm-2">
								    	<a href="#"  class="btn btn-primary btn-sm addJobFile">
								      		<span class="glyphicon glyphicon-plus"></span>
								    	</a>					    	
								    </div>
								</div>
							</c:forEach>
							<c:if test="${empty ilInfoForm.jobFileNames}">
					    		<div class="row form-group fileContainer">
									<label class="control-label col-sm-2 jobfile-label"><spring:message code="anvizent.package.label.jobFile"/> : </label>
						    		<div class="col-sm-8">			    			
								    	<input type="file" class="jobFile" name="jobFile" data-buttonText="Find file">
								    </div>
								    <div class="col-sm-2">
								    	<a href="#" class="btn btn-primary btn-sm addJobFile">
								      		<span class="glyphicon glyphicon-plus"></span>
								    	</a>
								    </div>
								</div>
					    	</c:if>			
						</div>
						
						<div class="row form-group existedJarFileDiv hidden">			 
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.ExistedJarFiles"/>:</label>					
						   	<div class="col-sm-8">
						    	<select class="existedJarFile form-control" multiple="multiple" style="white-space:pre-wrap;">
					    			<c:forEach var="jarFile" items="${jarFilesList}">
					    				<option value="<c:out value="${jarFile.fileName}"/>" ><c:out value="${jarFile.fileName}"/></option>
					    			</c:forEach>
						    	</select>
						    </div>			   
						</div>
						
						<div class="row form-group">			 
							<label class="control-label col-sm-2">
						    	<spring:message code="anvizent.package.label.activeStatus"/> :  	
						    </label>
						   	<div class="col-sm-8">
							   	<label class="radio-inline control-label">
							    	<form:radiobutton path="active" value="true" /><spring:message code="anvizent.package.button.yes"/>
							    </label>
							    <label class="radio-inline control-label">
							    	<form:radiobutton path="active" value="false"/><spring:message code="anvizent.package.button.no"/>  	
							    </label>
						    </div>			   
						</div>
						
						<div class="row form-group" >			 
							<div class="col-sm-12">
								<c:if test="${ilInfoForm.pageMode == 'editIL'}">
									<button type="button" class="btn btn-primary btn-sm" id="updateILDetails"><spring:message code="anvizent.package.button.update"/></button>								
								</c:if>
								<c:if test="${ilInfoForm.pageMode == 'createIL'}">
									<button type="submit" class="btn btn-primary btn-sm" id="addNewIL"><spring:message code="anvizent.package.button.add"/></button>								
								</c:if>
								<a class="btn btn-primary btn-sm back_btn startLoader" href='<c:url value="/admin/iLInfo"></c:url>'><spring:message code="anvizent.admin.button.Back"/></a>
							</div>					
						</div>
		    		</div>
		    	</div>
		    </div>
		</c:if>
	</form:form>
    
    <div class="modal fade" tabindex="-1" role="dialog" id="viewILDetailsPanel" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width:60%">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
				        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.iLDetails"/></h4>
				      </div>
				      <div class="modal-body">
							<div class="row form-group" >			 
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.ilName"/> : </label>					
							   	<div class="col-sm-10">
							    	<input type="text" name="iLName" class="form-control iLName" data-minlength="1" data-maxlength="255"/>
							    	<input type="hidden" name="iLId" class="iLId">
							    </div>			   
							</div>
							
							<div class="row form-group">			 
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.iLType"/> :</label>
							   	<div class="col-sm-10">									    	
							    	<label class="radio-inline control-label">
								    	<input type="radio" name="iLType" value="D"><spring:message code="anvizent.package.label.dimension"/>		    	
								    </label>
								    <label class="radio-inline control-label">
								    	<input type="radio" name="iLType" value="T"><spring:message code="anvizent.package.label.transaction"/>  	
								    </label>	
							    </div>			   
							</div>
							
							<div class="row form-group">			 
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.iLDescription"/> :</label>
							   	<div class="col-sm-10">
							    	<textarea name="iLDescription" class="form-control iLDescription"></textarea> 	
							    </div>			   
							</div>
							
							<div class="row form-group">			 
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.iLTableName"/> : </label>					
							   	<div class="col-sm-10">
							    	<input type="text" name="iLTableName" class="form-control iLTableName" data-minlength="1" data-maxlength="255"/>
							    </div>			   
							</div>
							
							<div class="row form-group">			 
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.xrefILTableName"/> : </label>					
							   	<div class="col-sm-10">
							    	<input type="text" name="xrefILTableName" class="form-control xrefILTableName" data-minlength="1" data-maxlength="255"/>
							    </div>			   
							</div>
							
							<div class="row form-group">			 
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.jobName"/> : </label>					
							   	<div class="col-sm-10">
							    	<input type="text" name="jobName" class="form-control jobName" data-minlength="1" data-maxlength="500"/>
							    </div>			   
							</div>								
							
							<div class="purgeScriptsDiv">
								<div class="row form-group scriptContainer">
									<label class="control-label col-sm-2 purgescript-label"><spring:message code="anvizent.package.label.purgeScript"/> : </label>
									<div class="col-sm-8">
								    	<textarea class="iLPurgeScript form-control" disabled="disabled"></textarea>
								    </div>
								</div>
							</div>
							
							
							<div class="row form-group jobExecutionTypeDiv">			 
								<label class="control-label col-sm-2">Job Execution Type :</label>
							   	<div class="col-sm-10">									    	
							    	<label class="radio-inline control-label">
								    	<input type="radio" name="jobExecutionType" value="E">ELT	    	
								    </label>
								    <label class="radio-inline control-label">
								    	<input type="radio" name="jobExecutionType" value="T">Talend 	
								    </label>	
							    </div>			   
							</div>
							
							
							<div class="row form-group jobTags_Div hidden">			 
								<label class="control-label col-sm-2">Job Tags: </label>					
							   	<div class="col-sm-8">
							    	<select  class="jobTagIdVal form-control" style="white-space:pre-wrap;">
							    		<c:if test="${not empty getJobTags }">
								    		<%-- <c:forEach items="${getJobTags}" varStatus="job">
								    			<option value="${job.key }">${job.value } </option>
								    		</c:forEach> --%>
							    		</c:if>
							    	</select>
							    </div>			   
							</div>
							
							<div class="row form-group loadParamters_Div hidden">			 
								<label class="control-label col-sm-2">Load parameters: </label>					
							   	<div class="col-sm-8">
							    	<select  class="loadParamtersVal form-control" style="white-space:pre-wrap;">
							    		<c:if test="${not empty getLoadParameters }">
								    		<%-- <c:forEach items="${getLoadParameters}" varStatus="job">
								    			<option value="${job.key }">${job.value } </option>
								    		</c:forEach> --%>
							    		</c:if>
							    	</select>
							    </div>			   
							</div>
							
							
							<div class="row form-group iLContextParams_div">			 
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.contextParameters"/> : </label>					
							   	<div class="col-sm-10">
							    	<select class="iLContextParams form-control" multiple="multiple" style="white-space:pre-wrap;"></select>
							    </div>			   
							</div>
							
							<div class="row form-group">			 
								<label class="control-label col-sm-2">
							    	<spring:message code="anvizent.package.label.activeStatus"/>		    	
							    </label>
							   	<div class="col-sm-10">
								   	<label class="radio-inline control-label">
								    	<input type="radio" name="isActive" value="true"><spring:message code="anvizent.package.button.yes"/>		    	
								    </label>
								    <label class="radio-inline control-label">
								    	<input type="radio" name="isActive" value="false"><spring:message code="anvizent.package.button.no"/>  	
								    </label>
							    </div>			   
							</div>															
				      </div>
				      <div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.cancel"/></button>
				      </div>
			    </div> 
		  </div> 
	</div>
	
	<div class="col-sm-6 purge-scripts-div" style='display:none;'>
    	<div class="row form-group scriptContainer">
	    	<label class="control-label col-sm-2 purgescript-label"><spring:message code="anvizent.package.label.purgeScript"/> : </label>
	    	<div class="col-sm-8">
		    	<textarea name="iLPurgeScript" class="form-control iLPurgeScript"></textarea>			
		    </div>
		    <div class="col-sm-2">
			    <a href="#"  class="btn btn-primary btn-sm addPurgeScript">
		      		<span class="glyphicon glyphicon-plus"></span>
		    	</a>
		    	<a href="#" class="btn btn-primary btn-sm deletePurgeScript" style="display:none;">
		      		<span class="glyphicon glyphicon-trash"></span>
		    	</a>
	    	</div>	
	    </div>	
    </div>	
    
    <div class="col-sm-6 job-files-div" style='display:none;'>
    	<div class="row form-group fileContainer">
	    	<label class="control-label col-sm-2 jobfile-label"><spring:message code="anvizent.package.label.jobFile"/> : </label>
	    	<div class="col-sm-8">
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