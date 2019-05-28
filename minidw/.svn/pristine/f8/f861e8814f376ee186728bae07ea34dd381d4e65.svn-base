<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.MiddleLevelManager"/></h4>
 	</div>
 	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	<c:url value="/admin/middleLevelManager" var="url"/>
	<div class="col-sm-12">
		<form:form modelAttribute="middleLevelManager" action="${url}">
		<form:hidden path="id"/>
						<div class="panel panel-default">
							<div class="panel-heading"><spring:message code="anvizent.package.label.MiddleLevelManagerDetails"/></div>
							<div class="panel-body">
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.contextPath"/>:</label>
									<div class='col-sm-6'>
										<form:input path="contextPath" class="form-control" maxlength="150"/>
									</div>
								</div>
							
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.uploadListEndPoint"/>:</label>
									<div class='col-sm-6'>
										<form:input path="uploadListEndPoint" class="form-control" maxlength="150"/>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.writeEndPoint"/>:</label>
									<div class='col-sm-6'>
										<form:input path="writeEndPoint" class="form-control" maxlength="150"/>
									</div>
								</div>
								 
								<div class="row form-group hidden">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.deleteEndPoint"/>:</label>
									<div class='col-sm-6'>
										<form:input path="deleteEndPoint" class="form-control" maxlength="150"/>
									</div>
								</div>
								
								<div class="row form-group hidden">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.upgradeEndPoint"/> :</label>
									<div class='col-sm-6'>
										<form:input path="upgradeEndPoint" class="form-control" maxlength="150"/>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.userAuthToken"/>:</label>
									<div class='col-sm-6'>
										<form:input path="userAuthToken" class="form-control" maxlength="150"/>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.clientAuthToken"/>:</label>
									<div class='col-sm-6'>
										<form:input path="clientAuthToken" class="form-control" maxlength="150"/>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.encryptionPrivateKey"/>:</label>
									<div class='col-sm-6'>
										<form:input path="encryptionPrivateKey" class="form-control" maxlength="150"/>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.encryptionIV"/> :</label>
									<div class='col-sm-6'>
										<form:input path="encryptionIV" class="form-control" maxlength="150" />
										<br />
										<!-- <span style="color: red;font-weight: bold;">Note:</span>
										Encryption IV Must be a 16 -->
									</div>
								</div>
								
								<div class="row form-group">
										<div class="col-sm-7">
											<button id="updateMiddleLevelManager" type="submit" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.save"/></button>
										</div>
							</div>
								
							</div>
						</div>
		</form:form>	
	</div>	
</div>