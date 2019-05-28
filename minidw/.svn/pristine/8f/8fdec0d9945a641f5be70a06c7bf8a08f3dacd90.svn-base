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
		<h4 class="alignText"><spring:message code="anvizent.package.label.connectors"/></h4>
 	</div>

 	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	
 	<c:url value="/admin/connector/edit" var="editUrl"/>	
	<div class="col-sm-12">
		<form:form modelAttribute="connectorForm" action="${editUrl}">
			<c:choose>
			  	<c:when test="${connectorForm.pageMode == 'list' }">
			  	<div class='row form-group'>
			  		<a style="float:right;margin-right: 1.5em;"  	class="btn btn-sm btn-success" href="<c:url value="/admin/connector/add"/>"> <spring:message code="anvizent.package.label.Add"/> </a>
			  	</div>
					<div class='row form-group'>
						<div class="table-responsive">
							<table class="table table-striped table-bordered tablebg " id="ConnectorTable">
								<thead>
									<tr>
										<th><spring:message code="anvizent.package.label.sNo"/></th>
										<th><spring:message code="anvizent.package.label.id"/></th>
										<th><spring:message code="anvizent.package.label.connectorType"/></th>
										<th><spring:message code="anvizent.package.label.databaseName"/></th>
										<th><spring:message code="anvizent.package.label.isActive"/></th>
										<th><spring:message code="anvizent.package.label.edit"/></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${connector}" var="connector" varStatus="index">
										<tr>
											<td><c:out value="${index.index+1}" /></td>
											<td><c:out value="${connector.connector_id}" /></td>
											<td><c:out value="${connector.connectorName}" /></td>
											<td><c:out value="${connector.name}" /></td>
											<td>${connector.isActive == true ? yesVar : noVar}</td>
											<td>
												<button class="btn btn-primary btn-sm tablebuttons" name="id" value="<c:out value="${connector.connector_id}" />" title="<spring:message code="anvizent.package.label.edit"/>" >
													<i class="fa fa-pencil" aria-hidden="true"></i>
												</button>
											</td>								
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
				    </div>
				</c:when>
				
				<c:when test="${connectorForm.pageMode == 'edit'  || connectorForm.pageMode == 'add' }">
					
					<c:set var="updateMode" value="Update" />
					<c:if test="${connectorForm.pageMode == 'add'}">
						<c:set var="updateMode" value="Add" />
					</c:if>
						<div class="panel panel-default">
							<div class="panel-heading">${updateMode } Connector Details</div>
							<div class="panel-body">
							    <div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.databaseName"/> :</label>
									<div class='col-sm-6'>
									   <form:select path="connectorId" cssClass="form-control">
											<spring:message code="anvizent.package.label.selectDatabase" var="selectDatabase" />
											<form:option value="0">${selectDatabase }</form:option>
											<form:options items="${getAllDatabases}"/>
										</form:select>
									</div>
								</div>
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.connectorName"/> :</label>
									<div class='col-sm-6'>
										<form:hidden path="id" class="form-control"/>
										<spring:message code="anvizent.package.label.connector" var="connector"/>
										<form:input path="connectorName" class="form-control" placeholder="${connector}" data-maxlength="45"/>
									</div>
								</div>
							
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.activeStatus"/> :</label>
									<div class='col-sm-6'>
									 	<div class="activeStatus">
									 		 <label class="radio-inline">
												<form:radiobutton path="isActive" value="true"/><spring:message code="anvizent.package.button.yes"/>
											 </label>	
											 <label class="radio-inline">
										    	<form:radiobutton path="isActive" value="false"/><spring:message code="anvizent.package.button.no"/> 
										    </label>
									 	</div>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-3"></label>
									<div class="col-sm-6">
										<c:url value="/admin/connector/add" var="addUrl"/>
										<input type="hidden" value="${addUrl}" id="addUrl">
										<c:url value="/admin/connector/update" var="updateUrl"/>
										<input type="hidden" value="${updateUrl}" id="updateUrl">
										<form:hidden path="id" class="form-control"/>
									</div>
								</div>
								<div class="row form-group">
								<div class="col-sm-6">								
									<c:choose>
										<c:when test="${connectorForm.pageMode == 'edit'}">
											<button id="updateConnectorMaster" type="button" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Update"/></button>
										 
										</c:when>
										<c:when test="${connectorForm.pageMode == 'add'}">
											<button id="addConnector" type="button" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Add"/></button>
										</c:when>
									</c:choose>
									<a href="<c:url value="/admin/connector"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.label.Back"/></a>
								</div>
							</div>
							</div>
						</div>
				</c:when>
			</c:choose> 
		</form:form>	
	</div>	
</div>