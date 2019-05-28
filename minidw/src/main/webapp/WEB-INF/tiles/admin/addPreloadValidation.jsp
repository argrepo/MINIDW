<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
    <div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.preload"/></h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<jsp:include page="admin_error.jsp"></jsp:include>
	    <input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>"> 
	</div>
	<c:url value="/admin/adminDataValidation/addValidationTypeJobsUpload" var="addValidationTypeJobsUploadUrl"/>
	<c:url value="/admin/adminDataValidation/addPreloadValidation" var="addUrl"/>
	<c:url value="/admin/adminDataValidation/editPreloadValidation" var="editUrl"/>
	<c:url value="/admin/adminDataValidation/updatePreloadValidation" var="updateUrl"/>
	<form:form modelAttribute="dataValidationForm" action="${editUrl}" enctype="multipart/form-data">
	  <form:hidden path="pageMode"/>
	    <c:choose>
	      <c:when test="${dataValidationForm.pageMode == 'list'}">
	        <div  style="width:100%;padding:0px 15px;">
         	 <div class="row form-group" style="padding:10px;border-radius:4px;">
			  <a style="float:right;" class="btn btn-sm btn-success" href="<c:url value="/admin/adminDataValidation/addPreload"/>"> <spring:message code="anvizent.package.button.add"/> </a>
			 </div>
		   </div>
			   <div class='row form-group'>
				 <div class="table-responsive">
					<table class="table table-striped table-bordered tablebg " id="tpreloadValidationTable" >
								<thead>
									<tr>
									    <th class="col-xs-4" style="display:none">Script Id</th>
										<th class="col-xs-4"><spring:message code="anvizent.package.label.validationname"/></th>
										<th class="col-xs-3"><spring:message code="anvizent.package.label.ilname"/></th>
										<th class="col-xs-3"><spring:message code="anvizent.package.label.connector"/></th>
										<th class="col-xs-3"><spring:message code="anvizent.package.label.validationtype"/></th>
										<th class="col-xs-3"><spring:message code="anvizent.package.label.script"/></th>
										<th class="col-xs-1"><spring:message code="anvizent.package.label.Edit"/></th>
									</tr>
								</thead>
									<tbody>
									<c:if test="${not empty validationScriptInfo}">
											<c:forEach items="${validationScriptInfo}" var="validationScriptData" varStatus="index">
												<tr id="dlMasterRow">
												    <td style="display:none">${validationScriptData.scriptId}</td>
													<td id="scriptName">${validationScriptData.validationName}</td>
													<td id="ilname"><c:out value="${validationScriptData.ilname}" ></c:out> </td>
													<td id="connectorname"><c:out value="${validationScriptData.databaseConnectorName}" ></c:out> </td>
													<td id="validationName"><c:out value="${validationScriptData.validationTypeName}" ></c:out> </td>
													<td>
													 <input type="button" id="viewValidationscript" value="<spring:message code="anvizent.package.message.ViewScript"/>" class="btn btn-primary btn-sm" data-scriptId="${validationScriptData.scriptId}" title="<spring:message code="anvizent.package.message.ViewScript" />">
													</td>
													<td>
													  <button class="btn btn-primary btn-xs editPreloadValidation tablebuttons" type="submit" name="scriptId" value="${validationScriptData.scriptId}">
														<i class="fa fa-pencil" aria-hidden="true"></i>
													  </button>
													</td>
												</tr>
											</c:forEach>
									</c:if>
								</tbody>
					</table>
					</div>
				</div>
	      </c:when>
	      <c:when test="${dataValidationForm.pageMode == 'addpreload' || dataValidationForm.pageMode =='editpreload'}">
	        <div class="col-md-12">
	         <c:set var="updateMode" value="Add" />
		       <c:if test="${dataValidationForm.pageMode == 'editpreload'}">
				  <c:set var="updateMode" value="Edit" />
			   </c:if>
			   <div class="panel panel-default">
			     <div class="panel-heading">${updateMode} <spring:message code="anvizent.package.label.preload"/></div>
			      <div class="panel-body">
			         <div class="row form-group">
					  <label class="control-label col-sm-3"><spring:message code="anvizent.package.label.validationname"/> </label> 
					  <div class="col-sm-8">
						<input type="hidden" value="${addUrl}" id="addUrl">
						<input type="hidden" value="${updateUrl}" id="updateUrl">
						<form:hidden path="scriptId"/>
					    <spring:message code="anvizent.package.label.validationname" var="validationNamePlaceholder"   />
						<form:input path="validationName" htmlEscape="true"  placeholder='${validationNamePlaceholder}' maxlength="255" data-minlength="1" data-maxlength="255" cssClass="form-control"/>
					  </div>
					</div>
					
					<div class="row form-group">
					 <label class="control-label col-sm-3"><spring:message code="anvizent.package.label.validationscripts"/></label>
					   <div class="col-sm-8">
					     <spring:message code="anvizent.package.label.validationscripts" var="validationscriptsPlaceholder" />
					      <form:textarea path="validationScripts"  placeHolder = "${validationscriptsPlaceholder}" data-minlength="1" rows="8" cssClass="form-control"/>
					   </div>
					</div>
					
					<div class="row form-group">
					  <label class="control-label col-sm-3"><spring:message code="anvizent.package.label.connector"/></label>
					   <div class='col-sm-8'>
					    <form:select path="databaseConnectorId">
					       <spring:message code="anvizent.package.label.selectConnector" var="selectConnector" />
					       <c:if test="${not empty connectorsInfo}">
					         <form:option value="0">${selectConnector}</form:option>
					         <form:options items="${connectorsInfo}" />
					       </c:if>
					    </form:select>
					   </div>
					</div>
					
					<div class="row form-group">
					  <label class="control-label col-sm-3"><spring:message code="anvizent.package.label.iL"/></label>
					   <div class='col-sm-8'>
					    <form:select path="ilId" cssClass="ilId">
					       <spring:message code="anvizent.package.label.selectIL" var="selectIL" />
					       <c:if test="${not empty clientILInfo}">
					         <form:option value="0">${selectIL}</form:option>
					         <form:options items="${clientILInfo}" />
					       </c:if>
					    </form:select>
					   </div>
					</div>
					<div class="row form-group">
						<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.validationtype" /></label>
							<div class='col-sm-8'>
								<form:select path="validationTypeId">
									<spring:message
											code="anvizent.package.label.selectvalidationtype"
											var="selectvalidationtype" />
									<c:if test="${not empty dataValidationTypes}">
											<form:option value="0">${selectvalidationtype}</form:option>
											<form:options items="${dataValidationTypes}" />
									</c:if>
									</form:select>
								</div>
						</div>
					<div class="row form-group">
						<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.activeStatus"/></label>
						<div class='col-sm-8'>
						 	<div class="active">
						 		 <label class="radio-inline">
									<form:radiobutton path="active" value="true"/><spring:message code="anvizent.package.button.yes"/>
								 </label>	
								 <label class="radio-inline">
							    	<form:radiobutton path="active" value="false"/><spring:message code="anvizent.package.button.no"/> 
							    </label>
						 	</div>
						</div>
					</div>
					
					<div class="row form-group">
						<div class="col-sm-8">
							<c:choose>
								<c:when test="${dataValidationForm.pageMode == 'editpreload'}">
									<button id="updatePreloadValidation" type="button"
								class="btn btn-primary btn-sm"><spring:message code="anvizent.admin.button.Update"/></button>
								 
								</c:when>
								<c:when test="${dataValidationForm.pageMode == 'addpreload'}">
									<button id="addPreloadValidation" type="button" 
								    class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.add"/></button>
								   </c:when>
							</c:choose>
							    <button id="resetPreloadValidation" type="button" 
								class="btn btn-primary btn-sm"> <spring:message code="anvizent.package.button.reset"/></button>
								<a id="resetPreloadValidation" type="reset" href="<c:url value="/admin/adminDataValidation/getPreloadValidations"/>"
								class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
						</div>
					</div>
			      </div>
			   </div>
	        </div>
	      </c:when>
	    </c:choose>
    </form:form>
</div>