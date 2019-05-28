<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<form:form modelAttribute="userForm" method="POST">
	<div class="col-sm-12 rightdiv">
	
		<div class="page-title-v1"><h4><spring:message code="anvizent.userProfile.header"/></h4></div>
		<div class="dummydiv"></div>
		<ol class="breadcrumb">
		</ol>
		
			<div class=''>
				<h4 class="alignText"><spring:message code="anvizent.userProfile.header"/></h4>
			</div>
		
		
		<jsp:include page="_error.jsp"></jsp:include>
		<div class="col-xs-12">
			<div class='row form-group '>
				<div class='col-sm-3'>
					<label class="labelsgroup"><spring:message code="anvizent.package.label.clientId"/></label>
				</div>
				<div class='col-sm-9'>
					<form:input path="clientId" class="form-control detailbox" readonly="true" />
				</div>
			</div>
			
			<div class='row form-group '>
				<div class='col-sm-3'>
					<label class="labelsgroup"><spring:message code="anvizent.userProfile.label.userName"/></label>
				</div>
				<div class='col-sm-9'>
					<form:input path="userName" class="form-control detailbox" readonly="true" />
				</div>
			</div>
			<%-- <div class='row form-group '>
				<div class='col-sm-3'>
					<label class="labelsgroup"><spring:message code="anvizent.package.label.roleId"/></label>
				</div>
				<div class='col-sm-9'>
					<form:input path="roleId" class="form-control detailbox" readonly="true" />
				</div>
			</div> --%>
			<c:if test="${not empty userForm.roleName}">
				<div class='row form-group '>
					<div class='col-sm-3'>
						<label class="labelsgroup"><spring:message code="anvizent.package.label.roleName"/></label>
					</div>
					<div class='col-sm-9'>
						<form:input path="roleName" class="form-control detailbox" readonly="true" />
					</div>
				</div>
			</c:if>
		<c:if test="${principal.roleId == 7 }">
			<div class='row form-group '>
			<div class='col-sm-6'>
					<label class="databaselist"><spring:message code="anvizent.package.label.availableDatabases"/></label>
				</div>
			</div>
			<div class='row form-group '>
				<div class='col-sm-3'>
					<label class="labelsgroup"><spring:message code="anvizent.package.label.databaseName"/></label>
				</div>
				<div class='col-sm-3 databasename'>
					
					<c:forEach items="${schemaNames}" var="schemaName">
						<c:out value="${schemaName}"/>
						<br>
					</c:forEach>
				</div>
				
			</div>
		</c:if>
			 
	</div>
  </div>
</form:form>