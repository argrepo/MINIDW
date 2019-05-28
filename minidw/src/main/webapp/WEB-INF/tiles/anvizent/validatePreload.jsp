<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
    <div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.preload"/></h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<jsp:include page="_error.jsp"></jsp:include>
	    <input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>"> 
	</div>
	<c:url value="/adt/package/dataValidation/preloadInfoByConnectorId" var="url"/>
	<c:url value="/adt/package/dataValidation/addPreloadValidation" var="addUrl"/>
	<c:url value="/adt/package/dataValidation/editPreloadValidation" var="editUrl"/>
	<c:url value="/adt/package/dataValidation/updatePreloadValidation" var="updateUrl"/>
	<div class='row form-group history' id="preLoadDiv">
		<form:form modelAttribute="dataValidationForm" action="${url}" enctype="multipart/form-data">
			<form:hidden path="pageMode" />
			<c:choose>
				<c:when test="${dataValidationForm.pageMode == 'list'}">
					<div class="row form-group">
						<label class="col-sm-3 labelsgroup control-label"><spring:message
								code="anvizent.package.label.chooseconnector" /> :</label>
						<div class="col-sm-5">
							<form:select path="ilConnection.connectionId"
								cssClass="form-control" id="connectionId">
								<spring:message code="anvizent.package.label.selectConnector"
									var="selectConnector" />
								<form:option value="0" label="-- Please Select --" />
								<c:forEach var="ilconnection" items="${ilConnectionInfo}">
									<form:option value="${ilconnection.connectionId}"
										label="${ilconnection.connectionName} - ${ilconnection.database.name}"
										data-protocal="${ilconnection.database.protocal}"
										data-connectorId="${ilconnection.database.connector_id}"></form:option>
								</c:forEach>
							</form:select>
						</div>
					</div>

					<c:if
						test="${dataValidationForm.ilConnection.connectionId != null && dataValidationForm.ilConnection.connectionId != 0}">
						<div id="defualtVariableDbSchema"></div>
						
						<div style="width: 100%; padding: 0px 10px;">
							<div class="row form-group"
								style="padding: 10px; border-radius: 4px;">
							 <div class="col-sm-2 pull-right">	
								<a class="btn btn-sm btn-success"
									href="<c:url value="/adt/package/dataValidation/addPreload"/>">
									<spring:message code="anvizent.package.button.add" />
								</a>
								
								<button style="float: right;" type="button" id=validatebutton
									class="btn btn-sm btn-primary">
									<spring:message code="anvizent.package.button.runselected" />
								</button>
							 </div>
							</div>
						</div>
						
						<input style="visibility: hidden;" type="button"
							id="validateDbVaribleshiddenbtn" value="Replace">
						<div class='row form-group'>
							<div class="table-responsive">
								<table
									class="table table-striped table-bordered tablebg validationTbl"
									id="tvalidatePreloadTable">
									<thead>
										<tr>
											<th class="col-xs-1"><input type="checkbox"
												class="selectAll"></th>
											<th style="display: none">Script Id</th>
											<th><spring:message
													code="anvizent.package.label.validationname" /></th>
											<th><spring:message code="anvizent.package.label.ilname" /></th>
											<th><spring:message
													code="anvizent.package.label.connector" /></th>
											<th><spring:message code="anvizent.package.label.script" /></th>
											<th><spring:message
													code="anvizent.package.label.results" /></th>
											<th class="col-xs-1"><spring:message
													code="anvizent.package.label.Edit" /></th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty validationScriptInfo}">
											<c:forEach items="${validationScriptInfo}"
												var="preloadScriptData" varStatus="index">
												<tr id="ilRow">
													<td><input type="checkbox" class="jcolumn-check"
														data-scriptid="<c:out value="${preloadScriptData.scriptId}"/>"></td>
													<td style="display: none">${preloadScriptData.scriptId}</td>
													<td id="scriptName">${preloadScriptData.validationName}</td>
													<td id="ilname"><c:out
															value="${preloadScriptData.ilname}"></c:out></td>
													<td id="connectorname"><c:out
															value="${preloadScriptData.databaseConnectorName}"></c:out>
													</td>
													<td><input type="button" id="viewValidationscript"
														value="<spring:message code="anvizent.package.message.ViewScript"/>"
														class="btn btn-primary btn-sm"
														data-scriptId="${preloadScriptData.scriptId}"
														title="<spring:message code="anvizent.package.message.ViewScript" />">
													</td>
													<td><c:url
															value="/adt/package/dataValidation/viewJobResults/${preloadScriptData.scriptId}"
															var="viewUrl" /> <a href="${viewUrl}"> <spring:message
																code="anvizent.package.label.viewResults" />
													</a></td>
													<td>
													<input type="hidden" value="${editUrl}" id="editUrl">
													  <button class="btn btn-primary btn-xs editPreloadValidation tablebuttons" name="scriptId" type="submit" value="${preloadScriptData.scriptId}">
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
				</c:when>
 			<c:when test = "${dataValidationForm.pageMode == 'validationTypeJobsUpload'}">
	       <div class="col-md-12">
	        <c:set var="validationTypeJobsUpload" value="validationTypeUpload" />
	          <div class="panel panel-default">
	           <div class="panel-heading"><spring:message code="anvizent.package.label.validationTypeJobsUpload"/></div>
	           <div class="panel-body">
	           <div class="row form-group">
	           <input type="hidden" value="${addValidationTypeJobsUploadUrl}" id="addValidationTypeJobsUploadUrl">
						<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.validationtype" /></label>
							<div class='col-sm-8'>
								<form:select path="validationTypeId">
									<spring:message
											code="anvizent.package.label.selectvalidationtype"
											var="selectvalidationtype" />
									<c:if test="${not empty dataValidationTypes}">
											<form:option value="0">${selectvalidationtype}</form:option>
											<form:options items="${dataValidationTypes}" />
									</c:if>
									</form:select>
								</div>
						</div>
						<div class="row form-group jobNameDiv">
							<spring:message code="anvizent.package.label.jobName" var="jobName"/>			 
							<label class="control-label col-sm-2"><c:out value="${jobName}"/> : </label>					
						   	<div class="col-sm-8">
						    	<form:input path="jobName" cssClass="form-control jobName" data-maxlength="500" placeholder="${jobName}"/>
						    </div>			   
						</div>	
						
						<div class="row form-group jobFileNameDiv">
							<spring:message code="anvizent.package.label.jobFileName" var="jobFileName"/>
							<label class="control-label col-sm-2 jobfile-label"><c:out value="${jobFileName}"/> : </label>
					    	<div class="col-sm-8">
					    		<form:hidden path="uploadedJobFileNames"/>
					    		<input class="uploadedJobFileNames form-control" disabled="disabled" <%-- value="${ilInfoForm.uploadedJobFileNames}" placeholder="${jobFileName}" --%>/>
						    </div>
						</div>
						
						<div class="jobFilesDiv">
							<c:forEach var="fileNames" items="${jobFileNames}" varStatus="loop">
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
							<c:if test="${empty jobFileNames}">
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
						
						<div class="row form-group existedJarFileDiv">			 
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
						
						<div class="row form-group">
							<div class="col-sm-8">
								<button id="addValidationTypeJobsUpload" type="button"
								class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.addvalidationTypeJobsUpload"/></button>
							    <button id="resetPreloadValidation" type="button" 
								class="btn btn-primary btn-sm"> <spring:message code="anvizent.package.button.reset"/></button>
								<a id="resetPreloadValidation" type="reset" href="<c:url value="/admin/adminDataValidation/getPreloadValidations"/>"
								class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
						</div>
					</div>
	           </div>
	          </div>
	       </div>
	      </c:when>
				<c:when
					test="${dataValidationForm.pageMode == 'addpreload' || dataValidationForm.pageMode =='editpreload'}">
					<div class="col-md-12">
						<c:set var="updateMode" value="Add" />
						<c:if test="${dataValidationForm.pageMode == 'editpreload'}">
							<c:set var="updateMode" value="Edit" />
						</c:if>
						<div class="panel panel-default">
							<div class="panel-heading">${updateMode}
								<spring:message code="anvizent.package.label.preload" />
							</div>
							<div class="panel-body">
								<div class="row form-group">
									<label class="control-label col-sm-3"><spring:message
											code="anvizent.package.label.validationname" /> </label>
									<div class="col-sm-8">
										<input type="hidden" value="${addUrl}" id="addUrl"> <input
											type="hidden" value="${updateUrl}" id="updateUrl">
										<form:hidden path="scriptId" />
										<spring:message code="anvizent.package.label.validationname"
											var="validationNamePlaceholder" />
										<form:input path="validationName" htmlEscape="true"
											placeholder='${validationNamePlaceholder}' maxlength="255"
											data-minlength="1" data-maxlength="255"
											cssClass="form-control" />
									</div>
								</div>

								<div class="row form-group">
									<label class="control-label col-sm-3"><spring:message
											code="anvizent.package.label.validationscripts" /></label>
									<div class="col-sm-8">
										<spring:message
											code="anvizent.package.label.validationscripts"
											var="validationscriptsPlaceholder" />
										<form:textarea path="validationScripts"
											placeHolder="${validationscriptsPlaceholder}"
											data-minlength="1" rows="8" cssClass="form-control" />
									</div>
								</div>
								<div class="row form-group">
									<label class="control-label col-sm-3"><spring:message
											code="anvizent.package.label.connector" /></label>
									<div class='col-sm-8'>
										<form:select path="databaseConnectorId">
											<spring:message code="anvizent.package.label.selectConnector"
												var="selectConnector" />
											<c:if test="${not empty connectorsInfo}">
												<form:option value="0">${selectConnector}</form:option>
												<form:options items="${connectorsInfo}" />
											</c:if>
										</form:select>
									</div>
								</div>

								<div class="row form-group">
									<label class="control-label col-sm-3"><spring:message
											code="anvizent.package.label.iL" /></label>
									<div class='col-sm-8'>
										<form:select path="ilId" cssClass="ilId">
											<spring:message code="anvizent.package.label.selectIL"
												var="selectIL" />
											<c:if test="${not empty clientILInfo}">
												<form:option value="0">${selectIL}</form:option>
												<form:options items="${clientILInfo}" />
											</c:if>
										</form:select>
									</div>
								</div>

								<div class="row form-group">
									<label class="control-label col-sm-3"><spring:message
											code="anvizent.package.label.validationtype" /></label>
									<div class='col-sm-8'>
										<form:select path="validationTypeId">
											<spring:message
												code="anvizent.package.label.selectvalidationtype"
												var="selectvalidationtype" />
											<c:if test="${not empty dataValidationTypes}">
												<form:option value="0">${selectvalidationtype}</form:option>
												<form:options items="${dataValidationTypes}" />
											</c:if>
										</form:select>
									</div>
								</div>
								<div class="row form-group">
									<label class="control-label col-sm-3"><spring:message
											code="anvizent.package.label.activeStatus" /></label>
									<div class='col-sm-8'>
										<div class="active">
											<label class="radio-inline"> <form:radiobutton
													path="active" value="true" /> <spring:message
													code="anvizent.package.button.yes" />
											</label> <label class="radio-inline"> <form:radiobutton
													path="active" value="false" /> <spring:message
													code="anvizent.package.button.no" />
											</label>
										</div>
									</div>
								</div>

								<div class="row form-group">
									<div class="col-sm-8">
										<c:choose>
											<c:when
												test="${dataValidationForm.pageMode == 'editpreload'}">
												<button id="updatePreloadValidation" type="button"
													class="btn btn-primary btn-sm">
													<spring:message code="anvizent.admin.button.Update" />
												</button>

											</c:when>
											<c:when test="${dataValidationForm.pageMode == 'addpreload'}">
												<button id="addPreloadValidation" type="button"
													class="btn btn-primary btn-sm">
													<spring:message code="anvizent.package.label.add" />
												</button>
											</c:when>
										</c:choose>
										<button id="resetPreloadValidation" type="button"
											class="btn btn-primary btn-sm">
											<spring:message code="anvizent.package.button.reset" />
										</button>
										<a id="resetPreloadValidation" type="reset"
											href="<c:url value="/adt/package/dataValidation/preloadInfo"/>"
											class="btn btn-primary btn-sm back_btn"><spring:message
												code="anvizent.package.link.back" /></a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</c:when>
			</c:choose>
		</form:form>

		<div class="col-sm-6 job-files-div" style='display: none;'>
			<div class="row form-group fileContainer">
				<label class="control-label col-sm-3 jobfile-label"><spring:message
						code="anvizent.package.label.jobFile" /> : </label>
				<div class="col-sm-7">
					<input type="file" class="jobFile" name="jobFile"
						data-buttonText="Find file">
				</div>
				<div class="col-sm-2">
					<a href="#" class="btn btn-primary btn-sm addJobFile"> <span
						class="glyphicon glyphicon-plus"></span>
					</a> <a href="#"
						class="btn btn-primary btn-sm remove_field deleteJobFile"
						style="display: none;"> <span
						class="glyphicon glyphicon-trash"></span>
					</a>
				</div>
			</div>
		</div>
	</div>

	<div class="row form-group dbSchemaSelection"
			id="dbSchemaSelectionDivForSqlServer" style="display: none">
			<div class="col-sm-2 labelForDbAndSchemaName">
				<spring:message code="anvizent.package.label.databaseandschema" />
				:
			</div>
			<div class="col-sm-2">
				<select class="form-control dbVariable" id="dbVariable">
					<option value="{db0}"><spring:message
							code="anvizent.package.label.selectdbvariable" /></option>
				</select>
			</div>
			<div class="col-sm-2">
				<select class="form-control dbName" id="dbName">
					<option value="{dbName}"><spring:message
							code="anvizent.package.label.selectdbname" /></option>
				</select>
			</div>
			<div class="col-sm-2">
				<select class="form-control schemaVariable" id="schemaVariable">
					<option value="{schema0}"><spring:message
							code="anvizent.package.label.selectschemavariable" /></option>
				</select>
			</div>
			<div class="col-sm-2">
				<select class="form-control schemaName" id="schemaName">
					<option value="{schemaName}"><spring:message
							code="anvizent.package.label.selectschemaname" /></option>
				</select>
			</div>
			<div class="col-sm-2">
				<button class="btn btn-primary btn-sm addDbSchema">
					<i class="fa fa-plus" aria-hidden="true"></i>
				</button>
				&nbsp;
				<button class="btn btn-primary btn-sm deleteDbSchema"
					style="display: none">
					<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
				</button>
			</div>
		</div>
	
		<div class="modal fade" tabindex="-1" role="dialog" id="messagePopUp"
			data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog" style="width: 500px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />"
							class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title"></h4>
					</div>
					<div class="modal-body">
						<div class="row form-group">
							<div class="col-sm-12">
								<div id="popUpMessage" class="alert" style="text-align: center;"></div>
							</div>
						</div>
						<div class="row">
							<div class="col-sm-12" style="text-align: center;">
								<button type="button" class="btn btn-primary btn-sm"
									data-dismiss="modal">
									<spring:message code="anvizent.package.button.ok" />
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>