<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.clientDatabaseMapping"/></h4>
 	</div>
    <jsp:include page="admin_error.jsp"></jsp:include>
 	<input type="hidden" id="userId" value="${principal.userId}">
 	<c:url value="/admin/database/clientMapping" var="url"/>
 	<input type="hidden" value="${url }/save" id="saveUrl"/>
 	
 	<form:form modelAttribute="clientDatabaseMappingForm" action="${url}">
		 	<div class="row form-group">
		 		<form:hidden path="pageMode"/>		 		
			</div>
 	
		 	<div class="tab-content">
				 	<div class="row form-group">
				 		<label class="col-sm-2 control-label"><spring:message code="anvizent.package.label.clientId"/> :</label>
						<div class="col-sm-6">
							<form:select path="clientId" cssClass="form-control">
								<spring:message code="anvizent.package.label.selectClient" var="selectClient" />
								<form:option value="0">${selectClient }</form:option>
								<form:options items="${allClients}" />
							</form:select>
						</div>
				 	</div>
				 	
				 	<c:if test="${clientDatabaseMappingForm.clientId != null && clientDatabaseMappingForm.clientId != '0' }">
					 	<div class="row form-group">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.databases"/> :</label>
							<div class='col-sm-6'>
								<form:select path="databases" class="databases">
									<c:if test="${not empty getAllDatabases}">
										<form:options items="${getAllDatabases}"/>
									</c:if>
								</form:select>
							</div>
						</div>	
						
						<div class="row form-group">
							<div class='col-sm-6'>
								<button type="button" class="btn btn-sm btn-primary" id="save"><spring:message code="anvizent.package.label.submit"/></button>
							</div>
						</div>
											
					</c:if>
			 	
			 </div>	
 	</form:form>
 	
 	
</div> 	