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
		<h4 class="alignText"><spring:message code="anvizent.admin.label.contextParameter"/></h4>
 	</div>

 	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	
 <c:url value="/admin/contextparameters/edit" var="editUrl"/>	
	<form:form modelAttribute="contextParameterForm" action="${editUrl}">
		<c:choose>
		  	<c:when test="${contextParameterForm.pageMode == 'list' }">
		  	<div class='row form-group'>
		  		<a style="float:right;margin-right: 1.5em;"  	class="btn btn-sm btn-success" href="<c:url value="/admin/contextparameters/add"/>"> Add </a>
		  	</div>
				<div class='row form-group'>
					<div class="table-responsive">
						<table class="table table-striped table-bordered tablebg " id="ConnectorTable">
							<thead>
								<tr>
									<th><spring:message code="anvizent.package.label.sNo"/></th>
									<th><spring:message code="anvizent.package.label.paramName"/></th>
									<th><spring:message code="anvizent.package.label.paramValue"/></th>
									<th><spring:message code="anvizent.package.label.description"/></th>
									<th><spring:message code="anvizent.package.label.isActive"/></th>
									<th><spring:message code="anvizent.package.label.edit"/></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${contextParams}" var="contextParam" varStatus="index">
									<tr>
										<td>${index.index+1}</td>
										<td>${contextParam.paramName}</td>
										<td>${contextParam.paramval}</td>
										<td>${contextParam.description}</td>
										<td>${contextParam.isActive == true ? yesVar : noVar}</td>
										<td>
											<button class="btn btn-primary btn-sm tablebuttons" name="paramId" value="${contextParam.paramId}" title="<spring:message code="anvizent.package.label.edit"/>" >
												<span class="glyphicon glyphicon-edit" aria-hidden="true"></span>
											</button>
										</td>								
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
			    </div>
			</c:when>
			
			<c:when test="${contextParameterForm.pageMode == 'edit'  || contextParameterForm.pageMode == 'add' }">
				
				<c:set var="updateMode" value="Update" />
				<c:if test="${contextParameterForm.pageMode == 'add'}">
					<c:set var="updateMode" value="Add" />
				</c:if>
					<div class="panel panel-info">
						<div class="panel-heading">${updateMode } <spring:message code="anvizent.admin.label.contextParameter"/> Details</div>
						<div class="panel-body">
						    
							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.paramName"/> :</label>
								<div class='col-sm-6'>
								    <spring:message code="anvizent.package.label.paramName" var="paramName" />
									<form:hidden path="paramId" class="form-control"/>
									<form:input path="paramName"  placeHolder = "${paramName}" data-minlength="1" data-maxlength="100" class="form-control"/>
								</div>
							</div>
						
							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.paramValue"/> :</label>
								<div class='col-sm-6'>
								   <spring:message code="anvizent.package.label.paramValue" var="paramValue" />
									<form:input path="paramval" placeHolder = "${paramValue}" data-minlength="1" data-maxlength="500" class="form-control"/>
								</div>
							</div>
							
							
							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.description"/> :</label>
								<div class='col-sm-6'>
									 <spring:message code="anvizent.package.label.description" var="description" />
									<form:textarea path="description" placeHolder = "${description}"  data-minlength="1" data-maxlength="225" class="form-control"/>
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
									<c:url value="/admin/contextparameters/add" var="addUrl"/>
									<input type="hidden" value="${addUrl}" id="addUrl">
									<c:url value="/admin/contextparameters/update" var="updateUrl"/>
									<input type="hidden" value="${updateUrl}" id="updateUrl">
									<form:hidden path="paramId" class="form-control"/>
								</div>
							</div>
							<div class="row form-group">
							<label class="control-label col-sm-3"></label>
							<div class="col-sm-6">								
								<c:choose>
									<c:when test="${contextParameterForm.pageMode == 'edit'}">
										<button id="updateContextParameter" type="button" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Update"/></button>
									 
									</c:when>
									<c:when test="${contextParameterForm.pageMode == 'add'}">
										<button id="addContextParameter" type="button" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Add"/></button>
									</c:when>
								</c:choose>
								<a href="<c:url value="/admin/contextparameters"/>" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Back"/></a>
							</div>
						</div>
						</div>
					</div>
			</c:when>
		</c:choose> 
	</form:form>	
</div>