<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.ExistingJars"/></h4>
 	</div>
	<jsp:include page="admin_error.jsp"></jsp:include>
 	<input type="hidden" id="userId" value="${principal.userId}">
 	
			<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg " id="existingJars">
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.sNo"/></th>
								<th><spring:message code="anvizent.package.label.fileName"/></th>
								<th><spring:message code="anvizent.package.label.fileSize"/></th>	
								<th><spring:message code="anvizent.package.label.modifiedDate"/></th>						
								
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${jarFilesList}" var="jarFile" varStatus="index">
								<tr>
									<td>${index.index+1}</td>
									<td><a href="<c:url value="/admin/downloadJar/${jarFile.fileName}"/>" target="_blank"><c:out value="${jarFile.fileName}" /></a> </td>
									<td><c:out value="${jarFile.fileSize}" /> </td>
									<td><c:out value="${jarFile.lastModified}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
			</div>
	</div>