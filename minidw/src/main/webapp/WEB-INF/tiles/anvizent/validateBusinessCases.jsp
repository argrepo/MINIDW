<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
    <div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.businesscases"/></h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<jsp:include page="_error.jsp"></jsp:include>
	    <input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>"> 
	</div>
	<c:url value="/adt/package/dataValidation/updateBusinessCasesValidation" var="updateUrl"/>
	<c:url value="/adt/package/dataValidation/addBusinessCasesValidation" var="addUrl"/>
	<c:url value="/adt/package/dataValidation/editBusinessCasesValidation" var="editUrl"/>
	<form:form modelAttribute="dataValidationForm" action="${editUrl}"
		enctype="multipart/form-data">
		<form:hidden path="pageMode" />
		<c:choose>
			<c:when test="${dataValidationForm.pageMode == 'list'}">
				<div class="row form-group">
				 <div class="col-sm-2 pull-right">
					<a class="btn btn-sm btn-success"
						href="<c:url value="/adt/package/dataValidation/addBusinessCasesValidation"/>">
						<spring:message code="anvizent.package.button.add" />
					</a>

					<button type="button" id=validatebutton
						class="btn btn-sm btn-primary">
						<spring:message code="anvizent.package.button.runselected" />
					</button>
				  </div>	
				</div>
				<div class='row form-group'>
					<div class="table-responsive">
						<table
							class="table table-striped table-bordered tablebg validationTbl"
							id="tvalidateBusinesscasesTable">
							<thead>
								<tr>
									<th class="col-xs-1"><input type="checkbox"
										class="selectAll"></th>
									<th style="display: none">Script Id</th>
									<th class="col-xs-4"><spring:message
											code="anvizent.package.label.validationname" /></th>
									<th class="col-xs-5"><spring:message
											code="anvizent.package.label.dlnames" /></th>
									<th class="col-xs-1"><spring:message code="anvizent.package.label.Edit"/></th>
									<th class="col-xs-3"><spring:message
											code="anvizent.package.label.results" /></th>
									<th class="col-xs-3" style="display: none">validationType Id</th>	
								</tr>
							</thead>
							<tbody>
								<c:if test="${not empty validationScriptInfo}">
									<c:forEach items="${validationScriptInfo}" var="scriptData"
										varStatus="index">
										<tr id="dlMasterRow">
											<td><input type="checkbox" class="jcolumn-check"
												data-scriptid="<c:out value="${scriptData.scriptId}"/>"></td>
											<td style="display: none">${scriptData.scriptId}</td>
											<td id="scriptName">${scriptData.validationName}</td>
											<td id="dlnames"><c:out value="${scriptData.dlNames}"></c:out>
											</td>
											<td>
												<button
													class="btn btn-primary btn-xs editBusinessValidation tablebuttons"
													type="submit" name="scriptId"
													value="${scriptData.scriptId}">
													<i class="fa fa-pencil" aria-hidden="true"></i>
												</button>
											</td>
											<td><c:url
													value="/adt/package/dataValidation/viewJobResults/${scriptData.scriptId}"
													var="viewUrl" /> <a href="${viewUrl}"> <spring:message
														code="anvizent.package.label.viewResults" />
											</a></td>
											<td><input id="datavalidationTypeid"  type="hidden" value = "${scriptData.validationTypeId}"/></td>
										</tr>
									</c:forEach>
								</c:if>
							</tbody>
						</table>
					</div>
				</div>
			</c:when>

			<c:when
				test="${dataValidationForm.pageMode == 'addBusinessCases' || dataValidationForm.pageMode =='editBusinessCases'}">
				<div class="col-md-12">
					<c:set var="updateMode" value="Add" />
					<c:if test="${dataValidationForm.pageMode == 'editBusinessCases'}">
						<c:set var="updateMode" value="Edit" />
					</c:if>
					<div class="panel panel-default">
						<div class="panel-heading">${updateMode}
							<spring:message
								code="anvizent.package.label.businesscasesvalidation" />
						</div>
						<div class="panel-body">
							<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.validationname" /> :</label>
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
										code="anvizent.package.label.selectmodule" /> : </label>
								<div class='col-sm-8'>
									<form:select path="dlList">
										<c:if test="${not empty clientDLInfo}">
											<form:options items="${clientDLInfo}" />
										</c:if>
									</form:select>
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.validationscripts" /> : </label>
								<div class="col-sm-8">
									<spring:message code="anvizent.package.label.validationscripts"
										var="validationscriptsPlaceholder" />
									<form:textarea path="validationScripts"
										placeHolder="${validationscriptsPlaceholder}"
										data-minlength="1" rows="8" cssClass="form-control" />
								</div>
							</div>

						<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.preparedStatement" /> :</label>
								<div class='col-sm-8'>
								<form:checkbox path="preparedStatement" cssClass="prepareStatement" value="false" />
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



					   
							<%-- <div class="row form-group">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.validationtype" /> : </label>
								<div class='col-sm-8'>
									<form:select path="validationType">
										<spring:message
											code="anvizent.package.label.selectvalidationtype"
											var="selectvalidationtype" />
										<form:option value="null">${selectvalidationtype}</form:option>
										<form:option value="Grain break(Duplicates on PK)">Grain break(Duplicates on PK)</form:option>
										<form:option value="Data Type Error">Data Type Error</form:option>
										<form:option value="Partially unmapped mapping">Partially unmapped mapping</form:option>
										<form:option value="Source Key mismatch across IL">Source Key mismatch across IL</form:option>
										<form:option
											value="Dimension name is mapped to source ID not the description of the ID">Dimension name is mapped to source ID not the description of the ID</form:option>
										<form:option
											value="Hard coding of the certain filter at the source level">Hard coding of the certain filter at the source level</form:option>
										<form:option
											value="Syntax error in mapping queries The DM team has fixed it post reporting">Syntax error in mapping queries The DM team has fixed it post reporting</form:option>
									</form:select>
								</div>
							</div> --%>

							<%-- <div class="row form-group contextParamsDiv">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.contextParameters" /> : </label>
								<div class="col-sm-8">
									<form:select path="contextParamsList"
										cssClass="datavalidationContextParams form-control"
										style="white-space:pre-wrap;">
										<form:options items="${contextParameters}" />
									</form:select>
								</div>
							</div>

							<div class="row form-group jobNameDiv">
								<spring:message code="anvizent.package.label.jobName"
									var="jobName" />
								<label class="control-label col-sm-3"><c:out
										value="${jobName}" /> : </label>
								<div class="col-sm-8">
									<form:input path="jobName" cssClass="form-control jobName"
										data-maxlength="500" placeholder="${jobName}" />
								</div>
							</div>

							<div class="row form-group jobFileNameDiv">
								<spring:message code="anvizent.package.label.jobFileName"
									var="jobFileName" />
								<label class="control-label col-sm-3 jobfile-label"><c:out
										value="${jobFileName}" /> : </label>
								<div class="col-sm-8">
									<form:hidden path="uploadedJobFileNames" />
									<input class="uploadedJobFileNames form-control"
										disabled="disabled"
										value="${dataValidationForm.uploadedJobFileNames}"
										placeholder="${jobFileName}" />
								</div>
							</div>

							<div class="jobFilesDiv">
								<c:forEach var="fileNames"
									items="${dataValidationForm.jobFileNames}" varStatus="loop">
									<div class="row form-group fileContainer">
										<c:if test="${loop.index == 0}">
											<label class="control-label col-sm-3 jobfile-label"><spring:message
													code="anvizent.package.label.jobFile" /> : </label>
										</c:if>
										<c:if test="${loop.index > 0}">
											<label class="control-label col-sm-3 jobfile-label"></label>
										</c:if>
										<div class="col-sm-7">
											<label class="checkbox" style="margin-left: 20px;"> <input
												type="checkbox" name="useOldJobFile" class="useOldJobFile"
												checked="checked">
											</label>
											<h5 class="jobFileName" style="margin-left: 20px;">
												<c:out value="${fileNames}" />
											</h5>
											<form:hidden path="jobFileNames[${loop.index}]"
												cssClass="jobFileNames" />
										</div>
										<div class="col-sm-2">
											<a href="#" class="btn btn-primary btn-sm addJobFile"> <span
												class="glyphicon glyphicon-plus"></span>
											</a>
										</div>
									</div>
								</c:forEach>
								<c:if test="${empty dataValidationForm.jobFileNames}">
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
											</a>
										</div>
									</div>
								</c:if>
							</div>

							<div class="row form-group existedJarFileDiv">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.ExistedJarFiles" />:</label>
								<div class="col-sm-8">
									<select class="existedJarFile form-control" multiple="multiple"
										style="white-space: pre-wrap;">
										<c:forEach var="jarFile" items="${jarFilesList}">
											<option value="<c:out value="${jarFile.fileName}"/>"><c:out
													value="${jarFile.fileName}" /></option>
										</c:forEach>
									</select>
								</div>
							</div> --%>

							<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.activeStatus" /> :</label>
								<div class='col-sm-8'>
									<div class="active">
										<label class="radio-inline"> <form:radiobutton
												path="active" value="true" />
											<spring:message code="anvizent.package.button.yes" />
										</label> <label class="radio-inline"> <form:radiobutton
												path="active" value="false" />
											<spring:message code="anvizent.package.button.no" />
										</label>
									</div>
								</div>
							</div>

							<div class="row form-group">
								<div class="col-sm-8">
									<c:choose>
										<c:when
											test="${dataValidationForm.pageMode == 'editBusinessCases'}">
											<button id="updateBusinessCasesValidation" type="button"
												class="btn btn-primary btn-sm">
												<spring:message code="anvizent.admin.button.Update" />
											</button>

										</c:when>
										<c:when
											test="${dataValidationForm.pageMode == 'addBusinessCases'}">
											<button id="addBusinessCasesValidation" type="button"
												class="btn btn-primary btn-sm">
												<spring:message code="anvizent.package.label.add" />
											</button>
										</c:when>
									</c:choose>
									<button id="resetBusinessCasesValidation" type="button"
										class="btn btn-primary btn-sm">
										<spring:message code="anvizent.package.button.reset" />
									</button>
									<a id="resetBusinessCasesValidation" type="reset"
										href="<c:url value="/adt/package/dataValidation/businessCasesInfo"/>"
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
					style="display: none;"> <span class="glyphicon glyphicon-trash"></span>
				</a>
			</div>
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