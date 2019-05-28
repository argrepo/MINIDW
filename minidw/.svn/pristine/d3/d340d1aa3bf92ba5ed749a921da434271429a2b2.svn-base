<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.ChangeClient"/></h4>
 	</div>
	<jsp:include page="admin_error.jsp"></jsp:include>
 	<input type="hidden" id="userId" value="${principal.userId}">
 	<c:url value="/admin/clientchange" var="url"/>
 	<form:form modelAttribute="clientConnectorMappingForm" action="${url}">
 			
 			<form:hidden path="pageMode"/>
		 	
		 	<div class="row form-group">
		 		<label class="col-sm-2 labelsgroup control-label"><spring:message code="anvizent.package.label.clientId"/> :</label>
				<div class="col-sm-6">
					<form:select path="clientId" cssClass="form-control">
						<spring:message code="anvizent.package.label.selectClient" var="selectClient" />
						<form:option value="-1"><spring:message code="anvizent.package.label.Master"/></form:option>
						<form:options items="${allClientsForChangeRequest}" />
					</form:select>
				</div>
		 	</div>
		 	
			<div class="row form-group">
				<div class='col-sm-6'>
					<button type="submit" class="btn btn-sm btn-primary" id="save"><spring:message code="anvizent.package.label.submit"/></button>
				</div>
			</div>
 	</form:form>
 	
 	
</div> 	