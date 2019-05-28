<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
    <div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.businesscases"/></h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<jsp:include page="admin_error.jsp"></jsp:include>
	    <input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>"> 
	</div>
	<c:url value="/admin/adminDataValidation/editBusinessCasesValidation" var="editUrl"/>
	<form:form modelAttribute="dataValidationForm" action="${editUrl}" enctype="multipart/form-data">
	 <form:hidden path="pageMode"/>
	 <c:choose>
	   <c:when test="${dataValidationForm.pageMode == 'list'}">
	      <div  style="width:100%;padding:0px 15px;">
         	<div class="row form-group" style="padding:10px;border-radius:4px;">
			 <a style="float:right;" class="btn btn-sm btn-success" href="<c:url value="/admin/adminDataValidation/addBusinessCasesValidation"/>"> <spring:message code="anvizent.package.button.add"/> </a>
			</div>
		  </div>
		  <div class='row form-group'>
			 <div class="table-responsive">
				<table class="table table-striped table-bordered tablebg " id="tbusinessCasesValidationTable" >
							<thead>
								<tr>
								    <th class="col-xs-4" style="display:none">Script Id</th>
									<th class="col-xs-5"><spring:message code="anvizent.package.label.validationname"/></th>
									<th class="col-xs-6"><spring:message code="anvizent.package.label.dlnames"/></th>
									<th class="col-xs-2"><spring:message code="anvizent.package.label.script"/></th>
									<th class="col-xs-1"><spring:message code="anvizent.package.label.Edit"/></th>
								</tr>
							</thead>
								<tbody>
								<c:if test="${not empty validationScriptInfo}">
										<c:forEach items="${validationScriptInfo}" var="validationScriptList" varStatus="index">
											<tr id="dlMasterRow">
											    <td style="display:none">${validationScriptList.scriptId}</td>
												<td id="scriptName">${validationScriptList.validationName}</td>
												<td id="dlnames"><c:out value="${validationScriptList.dlNames}" ></c:out> </td>
												<td>
												 <input type="button" id="viewValidationscript" value="<spring:message code="anvizent.package.message.ViewScript"/>" class="btn btn-primary btn-sm" data-scriptId="${validationScriptList.scriptId}" title="<spring:message code="anvizent.package.message.ViewScript" />">
												</td>
												<td>
												  <button class="btn btn-primary btn-xs editBusinessValidation tablebuttons" type="submit" name="scriptId" value="${validationScriptList.scriptId}">
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
	   
	   <c:when test="${dataValidationForm.pageMode == 'addBusinessCases' || dataValidationForm.pageMode =='editBusinessCases'}">
	     <div class="col-md-12">
	      <c:set var="updateMode" value="Add" />
	       <c:if test="${dataValidationForm.pageMode == 'editBusinessCases'}">
			  <c:set var="updateMode" value="Edit" />
		   </c:if>
		   <div class="panel panel-default">
		     <div class="panel-heading">${updateMode} <spring:message code="anvizent.package.label.businesscasesvalidation"/></div>
		       <div class="panel-body">
					<div class="row form-group">
					  <label class="control-label col-sm-3"><spring:message code="anvizent.package.label.validationname"/> :</label> 
					  <div class="col-sm-8">
						<c:url value="/admin/adminDataValidation/addBusinessCasesValidation" var="addUrl"/>
						<input type="hidden" value="${addUrl}" id="addUrl">
						<c:url value="/admin/adminDataValidation/updateBusinessCasesValidation" var="updateUrl"/>
						<input type="hidden" value="${updateUrl}" id="updateUrl">
						<form:hidden path="scriptId"/>
					    <spring:message code="anvizent.package.label.validationname" var="validationNamePlaceholder"   />
						<form:input path="validationName" htmlEscape="true"  placeholder='${validationNamePlaceholder}' maxlength="255" data-minlength="1" data-maxlength="255" cssClass="form-control"/>
					  </div>
					</div>
					<div class="row form-group">
					  <label class="control-label col-sm-3"><spring:message code="anvizent.package.label.selectmodule"/> : </label>
					   <div class='col-sm-8'>
					    <form:select path="dlList">
					       <c:if test="${not empty clientDLInfo}">
					         <form:options items="${clientDLInfo}" />
					       </c:if>
					    </form:select>
					   </div>
					</div>
					<div class="row form-group">
					 <label class="control-label col-sm-3"><spring:message code="anvizent.package.label.validationscripts"/> : </label>
					   <div class="col-sm-8">
					     <spring:message code="anvizent.package.label.validationscripts" var="validationscriptsPlaceholder" />
					      <form:textarea path="validationScripts"  placeHolder = "${validationscriptsPlaceholder}" data-minlength="1" rows="8" cssClass="form-control"/>
					   </div>
					</div>
					<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.preparedStatement" /> :</label>
								<div class='col-sm-8'>
								<form:checkbox path="preparedStatement" cssClass="prepareStatement" value="false" />
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
						<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.activeStatus"/> :</label>
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
								<c:when test="${dataValidationForm.pageMode == 'editBusinessCases'}">
									<button id="updateBusinessCasesValidation" type="button"
								class="btn btn-primary btn-sm"><spring:message code="anvizent.admin.button.Update"/></button>
								 
								</c:when>
								<c:when test="${dataValidationForm.pageMode == 'addBusinessCases'}">
									<button id="addBusinessCasesValidation" type="button" 
								    class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.add"/></button>
								   </c:when>
							</c:choose>
							    <button id="resetBusinessCasesValidation" type="button" 
								class="btn btn-primary btn-sm"> <spring:message code="anvizent.package.button.reset"/></button>
								<a id="resetBusinessCasesValidation" type="reset" href="<c:url value="/admin/adminDataValidation/getBusinessCasesValidation"/>"
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