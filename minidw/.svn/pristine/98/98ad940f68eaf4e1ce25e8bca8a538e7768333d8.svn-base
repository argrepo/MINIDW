<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText">
			<spring:message code="anvizent.admin.label.createIL" />
		</h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="${principal.userId}">
	</div>
	<div class="col-md-10 ilCreationMsg"></div>
	<div class="col-md-6">
		<form:form method="POST" id="createIlForm"
			enctype="multipart/form-data" class="form-horizontal" role="form">
			<input type="hidden" id="existingNewParamVal" name="existingNewParam" />
			<div class="form-group">
				<label class="control-label col-sm-3"><spring:message
						code="anvizent.package.label.ilNameDisplayName" /></label>
				<div class="col-sm-6">
					<input type="text" class="form-control" id="ilName"
						placeholder="<spring:message code="anvizent.package.label.enterILName"/>">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3"><spring:message
						code="anvizent.package.label.tableName" /></label>
				<div class="col-sm-6">
					<input type="text" class="form-control" id="tableName"
						placeholder="<spring:message code="anvizent.package.label.enterTableName"/>">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3"><spring:message
						code="anvizent.package.label.description" /></label>
				<div class="col-sm-6">
					<textarea rows="4" cols="50" class="form-control" id="description"
						placeholder="<spring:message code="anvizent.package.label.enterILDescription"/>"></textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-3"><spring:message
						code="anvizent.package.label.jobName" /></label>
				<div class="col-sm-6 ">
					<input type="text" class="form-control" id="JobName"
						placeholder="<spring:message code="anvizent.package.label.enterJobName"/>">
				</div>
			</div>
			<div class="form-group" id="fileContainer0">

				<label class="control-label col-sm-3"><spring:message
						code="anvizent.package.label.file" /></label>
				<div class="col-sm-6">
					<input type="file" class="filePath0 fileName" name="files"
						id="inputFile" data-buttonText="Find file">
				</div>
				<a href="#" class="btn btn-primary btn-xs addFile"> <span
					class="glyphicon glyphicon-plus"></span></a>
			</div>
			<div class="addFilePath" style='display: none'></div>
		</form:form>
	</div>
	<div class="col-md-6">
		<div class="panel panel-info" id=''>
			<div class="panel-heading">
				<spring:message
					code="anvizent.admin.label.ExistingContextParameters" />
			</div>
			<div class="panel-body" style="overflow-y: scroll; max-height: 425px">
				<table
					class="table table-striped table-bordered tablebg table-hover"
					id="data-table">
					<thead>
						<tr>
							<th><input type="checkbox" name="selectAll"> <spring:message
									code="anvizent.package.label.selectAll" /></th>
							<th><spring:message code="anvizent.package.label.paramId" /></th>
							<th><spring:message code="anvizent.package.label.paramName" /></th>
							<th><spring:message code="anvizent.package.label.paramValue" /></th>
						</tr>
					</thead>
					<tbody id=''>
						<c:forEach items="${contextParams}" var="contextParam">
							<tr class="data-row">
								<td><input type="checkbox" value="${contextParam.param_id}"></td>
								<td class="paramId">${contextParam.param_id}</td>
								<td>${contextParam.param_name}</td>
								<td><input type="text" value="${contextParam.paramval}"
									class="paramVal"></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div id="context-params"></div>
	</div>
	<div class="col-md-10">
		<div class="form-group">
			<div class="col-sm-offset-5 col-sm-6">
				<a href='<c:url value="/admin/ETLAdmin"/>'
					class="btn btn-primary btn-sm"><spring:message
						code="anvizent.admin.button.Back" /></a>
				<button id='ilCreation' type="button" class="btn btn-primary btn-sm">
					<spring:message code="anvizent.admin.button.Submit" />
				</button>

			</div>
		</div>
	</div>
</div>