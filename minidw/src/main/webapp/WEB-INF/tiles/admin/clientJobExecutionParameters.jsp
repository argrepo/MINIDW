<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.ClienJobExecutionParameters" /></h4>
 	</div>
 	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	<c:url value="/admin/clientJobExecutionParameters" var="url"/>
	<div class="col-sm-12">
		<form:form modelAttribute="clientJobExecutionParameters" action="${url}">
		<form:hidden path="id"/>
						<div class="panel panel-default">
							<div class="panel-heading"><spring:message code="anvizent.package.label.ClienJobExecutionParameters" /></div>
							<div class="panel-body">
							
								<div class="row form-group">
									<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.SourceTimeZone"/> :</label>
									<div class='col-sm-6'>
										<form:select path="sourceTimeZone" cssClass="form-control timeZone">
										<spring:message code="anvizent.package.label.selectTimeZone" var="selectOption" />
											<form:option value="0">${selectOption}</form:option>
											<form:options items="${timesZoneList}" />
										</form:select>
									</div>
								</div>
							
								<div class="row form-group">
									<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.DestinationTimeZone"/> :</label>
									<div class='col-sm-6'>
										<form:select path="destTimeZone" cssClass="form-control timeZone">
										<spring:message code="anvizent.package.label.selectTimeZone" var="selectOption" />
											<form:option value="0">${selectOption}</form:option>
											<form:options items="${timesZoneList}" />
										</form:select>
									</div>
								</div>
								
								
								<div class="row form-group">
									<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.NullReplacementValues"/> :</label>
									<spring:message code="anvizent.package.label.NullReplacementValues" var="nullReplace"/>
									<div class='col-sm-6'>
										<form:input path="nullReplaceValues" class="form-control" placeholder="${nullReplace}" maxlength="150"/>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.CaseSensitive"/> :</label>
									<div class='col-sm-6'>
										<div class="activeStatus">
									 		 <label class="radio-inline">
												<form:radiobutton path="caseSensitive" value="1"/><spring:message code="anvizent.package.button.yes"/>
											 </label>	
											 <label class="radio-inline">
										    	<form:radiobutton path="caseSensitive" value="0"/><spring:message code="anvizent.package.button.no"/> 
										    </label>
									 	</div>
									</div>
								</div>
								 
								<div class="row form-group">
									<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.trailingMonths"/> :</label>
									<div class='col-sm-6'>
										<form:select path="interval" cssClass="form-control">
										<option value="0"><spring:message code="anvizent.package.label.selectInterval"/></option>
											<c:forEach var="i" begin="1" end="20">
												<c:if test="${clientJobExecutionParameters.interval == i*6}">
										            	<option value="${clientJobExecutionParameters.interval}" selected>${clientJobExecutionParameters.interval}</option>
								            	</c:if>
				                           		<c:if test="${clientJobExecutionParameters.interval != i*6}">
										            	<option value="${i*6}">${i*6}</option>
								            	</c:if>
											</c:forEach>
							           </form:select>
									</div>
								</div>
								
								<div class="row form-group">
								<label class="control-label col-sm-3"></label>
										<div class="col-sm-6">
											<button id="updateClientJobExecutionParams" type="submit" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.save"/></button>
										</div>
							</div>
								
							</div>
						</div>
		</form:form>	
	</div>	
</div>