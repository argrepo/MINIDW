<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.webServiceTemplate" /></h4>
 	</div>
	<div class="col-md-10">
			<div class="dummydiv"></div>
			<jsp:include page="admin_error.jsp"></jsp:include>
			<input type="hidden" id="userId" value="${principal.userId}">
	</div>
	<c:url value="/admin/webservice/webServiceAuthentication" var="url"/>
	<c:url value="/admin/webservice/saveWebServiceTemplate" var="url"/>
 	<input type="hidden" value="${url }/save" id="saveUrl"/>
	<form:form modelAttribute="webServiceTemplateMaster" action="${url}">
		<div class="col-md-12 message-class"></div>
			<div class="row form-group">
					<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.Webservice"/></label>
					<div class="col-sm-6">
						<spring:message code="anvizent.package.label.enterWebserviceNames" var="enterWebserviceNames" />
						<form:input path="webServiceName" placeholder="${enterWebserviceNames}" data-minlength="1" data-maxlength="255" cssClass="form-control"/>
					</div>
			</div>
		
			<div class='row form-group'>
		 			<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.authenticationType"/></label>
					<div class="col-sm-6">
						<form:select path="webServiceAuthenticationTypes.id" cssClass="form-control webServiceAuthenticationTypes" >
							<spring:message code="anvizent.package.label.selecAuthentication" var="selectOption" />
								<form:option value="0">${selectOption }</form:option>
								<form:options items="${webServiceAuthenticationTypes}"/>
						</form:select>
				 	</div>
	 	   	</div>
		 	
		 	<div class="col-sm-12">
		 		<div class="panel panel-info authView" style='display:none;'>
				<div class="panel-heading authName"></div>
				<div class="panel-body">
				
					<div class="row form-group url">
						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.url" /></label>
						<div class="col-sm-10">
						    <spring:message code="anvizent.package.label.enterUrl" var="enterUrl" />
							<form:input path="authenticationUrl" placeholder="${enterUrl}"  data-minlength="1" data-maxlength="255" cssClass="form-control"/>
						</div>
					</div>
					
					<div class="row form-group methodType">
						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.methodType" /></label>
						<div class="col-sm-10">
							<div class="methodTypeSelectionValidation">
							
									<label class="radio-inline">
											<form:radiobutton path="authenticationMethodType" value="GET"/>
											<spring:message code="anvizent.package.label.get" />
									 </label>	
									<label class="radio-inline">
											<form:radiobutton path="authenticationMethodType" value="POST"/>
											<spring:message code="anvizent.package.label.post" />
									</label>
									<label class="radio-inline">
											<form:radiobutton path="authenticationMethodType" value="PUT"/>
												<spring:message code="anvizent.package.label.put" />
									</label>
									<label class="radio-inline">
											<form:radiobutton path="authenticationMethodType" value="DELETE"/>
											<spring:message code="anvizent.package.label.DELETE" />
									</label>
									<label class="radio-inline">
											<form:radiobutton path="authenticationMethodType" value="OPTIONS"/>
											<spring:message code="anvizent.package.label.options" />
									</label>
							</div>
						</div>
					</div>
					<div class="row form-group calBackUrl" style='display:none;'>
					<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.calBackUrl" /></label>
						<div class="col-sm-10">
				   			<spring:message code="anvizent.package.label.enterCallBackUrl" var="enterCallBackUrlPlaceholder" />
							<form:input path="oAuth2.redirectUrl"  placeHolder = "${enterCallBackUrlPlaceholder}" data-minlength="1" data-maxlength="255" cssClass="form-control"/>
						</div>
					</div>
					
					<div class="row form-group accessTokenUrl" style='display:none;'>
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.accessTokenUrl" /></label>
							<div class="col-sm-10">
							   	<spring:message code="anvizent.package.label.enterAccessTokenUrl" var="enterAccessUrlPlaceholder" />
								<form:input path="oAuth2.accessTokenUrl"  placeHolder = "${enterAccessUrlPlaceholder}" data-minlength="1" data-maxlength="255" cssClass="form-control"/>
							</div>
					</div>
					
					<div class="row form-group webServicesCoveringDiv" style='display:none;'>
					  <div class='row form-group'>
				 			<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.grantType" /></label>
							<div class="col-sm-6">
								<form:select path="OAuth2.grantType" cssClass="form-control grantType" >
									<form:option value="0"><spring:message code="anvizent.package.label.selectWebservice"/></form:option>
										<form:option value="authorization_code"><spring:message code="anvizent.package.label.authorizationCode"/></form:option>
										<form:option value="client_credentials"><spring:message code="anvizent.package.label.clientCredentials"/></form:option>
								</form:select>
						 	</div>
				 	 </div>
		           </div>
					<div class="requestParams">
					     <label class="control-label col-sm-2"><spring:message code="anvizent.package.label.requestParameters" /></label>
					  
						<table class="table table-striped table-bordered tablebg " id="requestParamsTable">
							<thead>
									<tr>
										<th class="col-xs-4"><spring:message code = "anvizent.package.label.paramName"/></th>
										<th><spring:message code = "anvizent.package.label.isMandatory"/></th>
										<th><spring:message code = "anvizent.package.label.isPassword"/></th>
										<th> 
									        <button type ="button" class="btn btn-primary btn-sm addAuthRequestParam">
						      					<span class="glyphicon glyphicon-plus"></span>
						    				</button>
						    				<button  type="button" class="btn btn-primary btn-sm deleteAuthRequestParam remove_field" id="deleteAuthRequestParam">
	      										<span class="glyphicon glyphicon-trash"></span>
						    				</button>
						    	       </th>
									</tr>
							</thead>
							<tbody>
								<c:forEach items="${webServiceTemplateMaster.webServiceTemplateAuthRequestparams }" var="webServiceTemplateAuthRequestparams1" varStatus="index">
									<tr>
									     <td><form:input class="form-control requestParam" path="webServiceTemplateAuthRequestparams[${index.index}].paramName"/></td>
									     <td><form:checkbox class="isMandatory" path="webServiceTemplateAuthRequestparams[${index.index}].mandatory" /></td>
									     <td><form:checkbox class="isPassword" path="webServiceTemplateAuthRequestparams[${index.index}].passwordType"/></td>
									     <td></td>
									</tr>
								</c:forEach>
							</tbody>
							<tfoot>
							   <tr id="webserviceParamSample" style="display:none;">
									     <td><form:input class="form-control requestParam" path="webServiceTemplateAuthRequestparams[0].paramName"/></td>
									     <td><form:checkbox class="isMandatory" path="webServiceTemplateAuthRequestparams[0].mandatory" /></td>
									     <td><form:checkbox class="isPassword" path="webServiceTemplateAuthRequestparams[0].passwordType"/></td>
									     <td></td>
								</tr>
							</tfoot>
						</table>
					</div>
					
					<div id="authRequestParamsToApi">
						 <div class="row-form-group">
							<div class="row form-group authRequestKeyValue">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.requestparamsToApi"/></label>
								<div class="col-sm-4">
									<spring:message code = "anvizent.package.label.enterKey" var="enterKey"/>
									<input class="form-control authRequestKey" placeholder="${enterKey}">
								</div>
								<div class="col-sm-4">
									<spring:message code = "anvizent.package.label.enterValue" var ="enterValue"/>
									<input class="form-control authRequestValue" placeholder="${enterValue}">
								</div>
								<div class="col-sm-2">
									<button type="button" class="btn btn-primary btn-sm addRequestParam">
										<span class="glyphicon glyphicon-plus"></span>
									</button>
								</div>
							</div>
					     </div>
					</div>
						
					<div class="row-form-group authRequestKeyValue" id="addRequestParamsApi" style="display: none">
							<label class="control-label col-sm-2"></label>	
							<div class="row form-group">
								<div class="col-sm-4">
									<spring:message code = "anvizent.package.label.enterKey" var="enterKey"/>
									<input class="form-control authRequestKey" placeholder="${enterKey}">
									
								</div>
								<div class="col-sm-4">
									<spring:message code = "anvizent.package.label.enterValue" var ="enterValue"/>
									<input class="form-control authRequestValue" placeholder="${enterValue}">
								</div>
								<div class="col-sm-2">
									<button type="button" class="btn btn-primary btn-sm addRequestParam">
										<span class="glyphicon glyphicon-plus"></span>
									</button>
									<button type="button" class="btn btn-primary btn-sm deleteRequestParam remove_field">
										<span class="glyphicon glyphicon-trash"></span>
									</button>
								</div>
							</div>
			        </div>	
					<form:hidden path="apiAuthRequestParams" class="apiAuthRequestParam"/>
					 <div id="authRequestHeadersToApi">
						 <div class="row-form-group">
							<div class="row form-group authRequestHeaders">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.requestHeadersToApi"/></label>
								<div class="col-sm-4">
								    <spring:message code = "anvizent.package.label.enterHeaderDetails" var ="enterHeader"/>
									<form:textarea path="apiAuthRequestHeaders" cssClass="form-control authreqheaders" id="authrequestheaders" placeholder="${enterHeader}"/>
								</div>
							</div>
						  </div>
					</div>	
			      </div>
		     </div>
		</div>   	
       	<div class="row form-group ViewSave" style='display:none;'>
			<div class="col-sm-10">
				<input type="submit" value="<spring:message code = "anvizent.package.button.save"/>" id="saveWebserviceTemp" class="btn btn-primary  btn-sm">
			</div>
       	</div>
  </form:form>	
 </div>
