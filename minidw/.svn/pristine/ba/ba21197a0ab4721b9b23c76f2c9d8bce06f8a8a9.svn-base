<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">	
	<ol class="breadcrumb">
	</ol>
	<jsp:include page="admin_error.jsp"></jsp:include>
	 
	<input type="hidden" id="userID" value="${principal.userId}">
	
	<div class="row form-group">
      	 <h4 class="alignText"><spring:message code="anvizent.admin.button.webService" /></h4>
    </div>
	
	<div class="col-sm-12">
		<div id="successOrErrorMessage"></div>
	</div>	

	<div class="col-sm-12">
		<div class='row form-group'> 
			<label class="control-label col-sm-2"> <spring:message code="anvizent.admin.button.webService" /></label>
		    <div class="col-sm-6">
			   <select class="selectwebservice form-control" name="selectwebservice"> 
			   		<option value="0" selected><spring:message code="anvizent.package.label.SelectWebService" /></option>
				    <c:forEach var="webservice" items="${webservicelist}">
				      	<option value="${webservice.key}">${webservice.value}</option>
				    </c:forEach>
			    </select>
			</div>
		</div>
	    <div class="panel panel-info createWebServicePanel" style="display: none;">
			<div class="panel-body">
				<div class="row form-group">
					<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.webServicename" /></label>
					<div class="col-sm-10">
					<input type="text" class="form-control" id="webServiceName" placeholder="<spring:message code = "anvizent.package.label.enterWebServiceName"/>">
					</div>
				</div>
				<div class="row form-group" id="authentication">
					<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.authenticationUrl" /></label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="authenticationUrl" placeholder="<spring:message code = "anvizent.package.label.enterAuthenticationURL"/>"> <br /> 
					</div>
				</div>
				<div class="row form-group" id="methodTypeForAuthentication">
					<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.methodType" /></label>
					<div class="col-sm-10 methodTypeForAuthenticationValidation">
						<label class="radio-inline"><input type="radio" name="methodTypeAuthSelection" value="GET" id="authGetMethod"> <spring:message code="anvizent.package.label.get" /></label> <label class="radio-inline"><input
							type="radio" name="methodTypeAuthSelection" value="POST" id="authPostMethod" > <spring:message code="anvizent.package.label.post" /></label>
					</div>
				</div>
				<div id="authRequestParams">
					<div class="row form-group authRequestKeyValue" id="authRequestKeyValue">
						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.requsetparam" /></label>
						<div class="col-sm-4">
							<input type="text" class="form-control authRequestKey" id="authRequestKey"
								placeholder="<spring:message code = "anvizent.package.label.enterKey"/>">
						</div>
						<div class="col-sm-4">
							<input type="text" class="form-control authRequestValue" id="authRequestValue"
								placeholder="<spring:message code = "anvizent.package.label.enterValue"/>">
						</div>
						<div class="col-sm-2">
							<button class="btn btn-primary btn-sm addAuthRequestParam">
								<span class="glyphicon glyphicon-plus"></span>
							</button>
							<span><input type="checkbox" class = "viewOrShow"><spring:message code = "anvizent.package.label.View"/>/<spring:message code = "anvizent.package.label.Show"/></span>
						</div>
						
					</div>
				</div>
				<div class="row form-group" id="authentication">
					<div class="col-sm-10">
						 <input type="button" value="<spring:message code = "anvizent.package.button.validAuthenticationURL"/>" id="getAuthenticationObject" class="btn btn-primary btn-sm" disabled="disabled">
					</div>
				</div>
				<div class="row form-group authorisationObjectParams" style="display: none">
					<label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.StatusCode"/>:</label>
					<div class="col-sm-10">
						<div id="statuscode"></div>
	
					</div>
				</div>
				<div class="row form-group">
					<div class="col-sm-10">
						 <a href="<c:url value="/admin/addWebService"/>" class="btn btn-sm btn-primary startLoader save"><spring:message code = "anvizent.package.button.save"/></a>
						 <input type="button" value="<spring:message code = "anvizent.package.button.update"/>" id="update" class="btn btn-primary btn-sm update">
					</div>
				</div>
			</div>
		</div>
		<div class="row form-group" id="buttons">
			<div class="col-sm-10">	
			 	<input type="button" value="<spring:message code = "anvizent.package.button.add"/>" id="addwebservice" class="btn btn-primary btn-sm">
 			    <input type="button" value="<spring:message code = "anvizent.package.button.edit"/>" id="editwebservice" class="btn btn-primary btn-sm">
			 	<input type="button" value="<spring:message code = "anvizent.package.button.delete"/>" id="deletewebservice" class="btn btn-primary btn-sm">
			</div>			
		</div>
	</div>
</div>
