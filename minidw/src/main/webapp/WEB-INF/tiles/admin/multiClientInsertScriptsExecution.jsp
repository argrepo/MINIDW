<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<div class="col-md-12 rightdiv">

	<div class='row form-group'>
		<h4 class="alignText">
			<spring:message code="anvizent.package.label.clientDatabaseMapping" />
		</h4>
	</div>
	<jsp:include page="admin_error.jsp"></jsp:include>
	<div class="col-md-12 message-class"></div>
	<input type="hidden" id="userID" value="${principal.userId}">

	<div class='row form-group'>
		<div class='col-sm-8'>
			<label class="col-sm-3 control-label labelsgroup"> <spring:message
					code="anvizent.package.label.SelectClient" />:
			</label>
			<div class='col-sm-6 clientList'>
				<select id="clientId" name="clientId" multiple="multiple">
				</select>
			</div>
		</div>
	</div>

	<div class='row form-group'>
		<div class='col-sm-8'>
			<input type="radio" class="truncateTbl" name="truncateTbl"
				value="true"> <label
				class="col-sm-3 control-label labelsgroup"> Truncate and
				Insert </label> <input type="radio" class="truncateTbl" name="truncateTbl"
				value="false"> <label
				class="col-sm-3 control-label labelsgroup"> Insert </label>
		</div>
	</div>


	<div class='row form-group tablesList'>
		<div class="table-responsive">
			<table class="table table-striped table-bordered tablebg "
				id="listOfTables">
				<thead>
					<tr>
						<th><spring:message code="anvizent.package.label.selectAll" /><input
							type="checkbox" value="selectAll" class="selectAll"></th>
						<th>Table Name</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${tableNames}" var="tablename">
						<tr>
							<td><input type="checkbox" class="tableNameCheckbox"
								value="${tablename}"></td>
							<td><c:out value="${tablename}" escapeXml="true" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>



	<div class="row form-group">
		<div class='col-sm-6'>
			<button type="button" class="btn btn-primary btn-sm"
				id="executeScripts">
				<spring:message code="anvizent.package.button.save" />
			</button>
		</div>
	</div>


</div>
