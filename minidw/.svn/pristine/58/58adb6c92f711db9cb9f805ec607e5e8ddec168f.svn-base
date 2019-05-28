<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.clientWebServiceMapping"/></h4>
 	</div>
	<jsp:include page="admin_error.jsp"></jsp:include>
 	<input type="hidden" id="userId" value="${principal.userId}">
 	<c:url value="/admin/clientWebserviceMapping" var="url"/>
 	<input type="hidden" value="${url }/save" id="saveUrl"/>
 	
 	<form:form modelAttribute="clientWebserviceMappingForm" action="${url}">
 			<form:hidden path="pageMode"/>
		 	<div class="row form-group">
		 		<label class="col-sm-2 labelsgroup control-label"><spring:message code="anvizent.package.label.clientId"/> :</label>
				<div class="col-sm-6">
				<spring:message code="anvizent.package.label.selectClient" var="selectOption" />
					<form:select path="clientId" cssClass="form-control">
					<form:option value="0">${selectOption }</form:option>
						<form:options items="${getClientList}" />
					</form:select>
				</div>
		 	</div>
		 	
			
			 <c:if test="${clientWebserviceMappingForm.clientId != null && clientWebserviceMappingForm.clientId != '0' }"> 
			 	<div class="row form-group">
					<label class="col-sm-2 labelsgroup control-label"><spring:message code="anvizent.package.label.Webservice"/> :</label>
					 <div class="col-sm-6">
					 	<form:select path="webservices" cssClass="form-control">
							<form:options items="${getAllWebServices}" />
					 	</form:select>
					</div>
				</div>						

				<div class="row form-group">
					<div class='col-sm-6'>
						<button type="button" class="btn btn-sm btn-primary" id="saveWebserviceMapping"><spring:message code="anvizent.package.label.submit"/></button>
					</div>
				</div>
			</c:if> 
 	</form:form>
 	
 	
</div> 	