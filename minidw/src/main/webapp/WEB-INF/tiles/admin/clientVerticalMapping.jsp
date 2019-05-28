<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.clientVerticalMapping"/></h4>
 	</div>
 	<jsp:include page="admin_error.jsp"></jsp:include> 	

 	<input type="hidden" id="userId" value="<c:out value="${principal.userId}"/>">
 	<c:url value="/admin/vertical/clientMapping" var="url"/>
 	<input type="hidden" value="${url }/save" id="saveUrl"/>
 	<form:form modelAttribute="clientVerticalMappingForm" action="${url}">
		 	<div class='row form-group'>
		 		<label class="col-sm-2 control-label labelsgroup"><spring:message code="anvizent.package.label.client"/> :</label>
				<div class="col-sm-6">
					<form:select path="clientId" cssClass="form-control">
						<spring:message code="anvizent.package.label.selectClient" var="selectOption" />
						<form:option value="0">${selectOption }</form:option>
						<form:options items="${allClients}" />
					</form:select>
				</div>
		 	</div>
		 	<c:if test="${clientVerticalMappingForm.clientId != null && clientVerticalMappingForm.clientId != '0' }">
			 	<div class="row form-group">
					<label class="control-label labelsgroup col-sm-2"><spring:message code="anvizent.package.label.verticals"/> :</label>
					<div class='col-sm-6'>
						<form:select path="verticals">
							<c:if test="${not empty getExistingVerticals}">
								<form:options items="${getExistingVerticals}"/>
							</c:if>
						</form:select>
					</div>
				</div>
				<div class="row form-group">
					<div class='col-sm-6'>
						<button type="button" class="btn btn-sm btn-primary" id="saveVertcalMapping" ><spring:message code="anvizent.package.label.submit"/></button>
					</div>
				</div>
				<c:if test="${not empty verticaldetails}">
					<div class='row form-group' style="padding: 0px 15px;">
						<div class="table-responsive">
							<table class="table table-striped table-bordered tablebg">
								<thead>
									<tr>
										<th><spring:message code="anvizent.package.label.sNo"/></th>
										<th><spring:message code="anvizent.package.label.verticalName"/></th>
										<th><spring:message code="anvizent.package.label.verticalDescription"/></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${verticaldetails}" var="vertical" varStatus="index">
										<tr>
											<td><c:out value="${index.index + 1}"/></td>
											<td><c:out value="${vertical.name}"/></td>
											<td><pre class="preTag"><c:out value="${vertical.description}"/></pre></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
				    </div>
			    </c:if>
			</c:if>
 	</form:form>
</div> 	