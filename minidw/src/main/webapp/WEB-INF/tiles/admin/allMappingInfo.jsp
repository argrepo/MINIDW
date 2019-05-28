<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.allMappingInfo"/></h4>
	</div>
	
	<jsp:include page="admin_error.jsp"></jsp:include>
	<input type="hidden" id="userID" value="${principal.userId}">
	
	<c:url value="/admin/allMappingInfo" var="url"/>
 	<input type="hidden" value="${url }/save" id="saveUrl"/>

	<form:form modelAttribute="allMappingInfoForm" action="${url}">
		 	<div class='row form-group'>
		 		<label class="col-sm-2 labelsgroup control-label"><spring:message code="anvizent.package.label.clientId"/> :</label>
				<div class="col-sm-6">
					<form:select path="clientId" cssClass="form-control">
						<spring:message code="anvizent.package.label.selectClient" var="selectOption" />
						<form:option value="0">${selectOption }</form:option>
						<form:options items="${allClients}" />
					</form:select>
				</div>
				<div class="col-sm-2">
					<button class="btn btn-success btn-sm" id="reload" title="Reload" style="margin-top: 4px;height: 28px;">
						<span class="glyphicon glyphicon-repeat"></span>
					</button>
				</div>
		 	</div>
		 	<spring:message code="anvizent.package.label.assignMore" var="assignMore"/>
		 	<spring:message code="anvizent.package.label.assign" var="assign"/>
		 	<spring:message code="anvizent.package.label.change" var="change"/>
		 	
		 	<c:if test="${allMappingInfoForm.clientId != null && allMappingInfoForm.clientId != '0' }">
			 	<div class="row form-group">
					<label class="control-label labelsgroup col-sm-2"><spring:message code="anvizent.package.label.numberOfVerticals"/> :</label>
					<div class='col-sm-6'>
						<span>${allMappingInfoForm.numberOfVerticals}</span>&nbsp;&nbsp;&nbsp;
						<button type="submit" class="clientMapping btn btn-primary btn-sm tablebuttons" data-formid="clientVerticalMappingForm" data-formaction="<c:url value="/admin/vertical/clientMapping"/>">
							${allMappingInfoForm.numberOfVerticals > 0 ? assignMore : assign}
						</button>
					</div>
				</div>	
				
				<div class="row form-group">
					<label class="control-label labelsgroup col-sm-2"><spring:message code="anvizent.package.label.numberOfTableScripts"/> :</label>
					<div class='col-sm-6'>
						<span>${allMappingInfoForm.numberOfTableScripts}</span>&nbsp;&nbsp;&nbsp;
						<button type="submit" class="clientMapping btn btn-primary btn-sm tablebuttons" data-formid="tableScriptsForm" data-formaction="<c:url value="/admin/clientTableScripts/mapping"/>">
							${allMappingInfoForm.numberOfTableScripts > 0 ? assignMore : assign}
						</button>
					</div>
				</div>	
				 
				<div class="row form-group">
					<label class="control-label labelsgroup col-sm-2"><spring:message code="anvizent.package.label.numberOfConnectors"/> :</label>
					<div class='col-sm-6'>
						<span>${allMappingInfoForm.numberOfConnectors}</span>&nbsp;&nbsp;&nbsp;
						<button type="submit" class="clientMapping btn btn-primary btn-sm tablebuttons" data-formid="clientConnectorMappingForm" data-formaction="<c:url value="/admin/connector/clientMapping"/>">
							${allMappingInfoForm.numberOfConnectors > 0 ? assignMore : assign}
						</button>
					</div>
				</div>	
				
				<div class="row form-group">
					<label class="control-label labelsgroup col-sm-2"><spring:message code="anvizent.package.label.numberOfDLs"/> :</label>
					<div class='col-sm-6'>
						<span>${allMappingInfoForm.numberOfDLs}</span>&nbsp;&nbsp;&nbsp;
						<button type="submit" class="clientMapping btn btn-primary btn-sm tablebuttons" data-formid="clientDLMappingForm" data-formaction="<c:url value="/admin/dashBoardLayoutMaster/clientMapping"/>">
							${allMappingInfoForm.numberOfDLs > 0 ? assignMore : assign}
						</button>
					</div>
				</div>						
			
				<div class="row form-group">
					<label class="control-label labelsgroup col-sm-2"><spring:message code="anvizent.package.label.numberOfWebSerices"/> :</label>
					<div class='col-sm-6'>
						<span>${allMappingInfoForm.numberOfWebServices}</span>&nbsp;&nbsp;&nbsp;
						<button type="submit" class="clientMapping btn btn-primary btn-sm tablebuttons" data-formid="clientWebserviceMappingForm" data-formaction="<c:url value="/admin/clientWebserviceMapping"/>">
							${allMappingInfoForm.numberOfWebServices > 0 ? assignMore : assign}
						</button>
					</div>
				</div>
				<div class="row form-group">
					<label class="control-label labelsgroup col-sm-2"><spring:message code="anvizent.package.label.currencies"/></label>
					<div class='col-sm-6'>
						<span>${allMappingInfoForm.numberOfCurrencies}</span>&nbsp;&nbsp;&nbsp;
						<c:if test="${allMappingInfoForm.numberOfCurrencies > 0 }">
						<button type="submit" class="clientMapping btn btn-primary btn-sm tablebuttons" data-formid="clientCurrencyMapping" data-formaction="<c:url value="/admin/currencyIntegration/clientCurrencyMapping/edit"/>">
							 <spring:message code="anvizent.package.label.change"/> 
						</button>
						</c:if>
						<c:if test="${allMappingInfoForm.numberOfCurrencies == 0 }">
					    <a class="btn btn-primary btn-sm tablebuttons"  href="<c:url value="/admin/currencyIntegration/clientCurrencyMapping/add"/>"> <spring:message code="anvizent.package.label.assign"/></a>
						</c:if>
					</div>
				</div>
				
				<div class="row form-group hidden">
					<label class="control-label labelsgroup col-sm-2"><spring:message code="anvizent.package.label.s3BucketMapping"/></label>
					<div class='col-sm-6'>
						<span>${allMappingInfoForm.numberOfS3BuckeMappings}</span>&nbsp;&nbsp;&nbsp;
						<a class="clientMapping btn btn-primary btn-sm tablebuttons" 
						href="<c:url value="/admin/clientS3Mapping"/>">${allMappingInfoForm.numberOfS3BuckeMappings > 0 ? change : assign}</a>
						
					</div>
				</div>
			
				<c:if test="${allMappingInfoForm.numberOfVerticals == 0 || allMappingInfoForm.numberOfConnectors == 0 || 
				allMappingInfoForm.numberOfDLs == 0 || allMappingInfoForm.numberOfTableScripts == 0 || allMappingInfoForm.numberOfWebServices == 0 || 
				allMappingInfoForm.numberOfCurrencies == 0 }">
					<div class="row form-group col-sm-6">
						<a href="<c:url value="/commonMappings/${allMappingInfoForm.clientId}?name=templateAssignment"/>" class="btn btn-sm btn-primary defaultMappings"><spring:message code="anvizent.package.label.DefaultMappings"/></a>
					</div>
				</c:if>
			</c:if>
	</form:form>
</div>
 
