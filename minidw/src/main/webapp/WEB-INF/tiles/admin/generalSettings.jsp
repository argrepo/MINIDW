<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.generalSettings"/></h4>
 	</div>

 	<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	<jsp:include page="admin_error.jsp"></jsp:include>
	<c:url value="/admin/generalSettings" var="url"/>
	<input type="hidden" value="${url}/save" id="save">

	<form:form modelAttribute="generalSettings" action="${url}" >
	 <form:hidden path="id"/>
		<div class="row form-group">
					 		<label class="col-sm-2 control-label"><spring:message code="anvizent.package.label.clientId"/> :</label>
							<div class="col-sm-6">
								<form:select path="clientId" cssClass="form-control">
									<spring:message code="anvizent.package.label.selectClient" var="selectClient" />
									<form:option value="0">${selectClient }</form:option>
									<form:options items="${allClients}" />
								</form:select>
							</div>
	 	</div>
	
	<c:if test="${generalSettings.clientId != null && generalSettings.clientId != '0' }">
	  <div class="col-sm-12" id="generalSettings">
				<div class="panel panel-default">
    				<div class="panel-heading"> <spring:message code="anvizent.package.label.GeneralSettings"/> </div>
		    		<div class="panel-body">
						<div class="row form-group">			 
							<label class="control-label col-sm-3">
						    	<spring:message code="anvizent.package.label.flatFile"/>		    	
						    </label>
						   	<div class="col-sm-6">
							   	<form:select path="flatFile" cssClass="form-control" >
											<form:option value="0"> <spring:message code="anvizent.package.label.selectoptions"/> </form:option>
											<form:option value="enable"><spring:message code="anvizent.package.label.Enable"/> </form:option>
											<form:option value="disable"><spring:message code="anvizent.package.label.Disable"/> </form:option>
											<form:option value="noDisplay"> <spring:message code="anvizent.package.label.NotDisplay"/> </form:option>
								</form:select>
						    </div>			   
						</div>
						<div class="row form-group">			 
							<label class="control-label col-sm-3">
						    	<spring:message code="anvizent.package.label.database"/>		    	
						    </label>
						   	<div class="col-sm-6">
							   	<form:select path="database" cssClass="form-control" >
											<form:option value="0"> <spring:message code="anvizent.package.label.selectoptions"/> </form:option>
											<form:option value="enable"><spring:message code="anvizent.package.label.Enable"/> </form:option>
											<form:option value="disable"><spring:message code="anvizent.package.label.Disable"/> </form:option>
											<form:option value="noDisplay"> <spring:message code="anvizent.package.label.NotDisplay"/> </form:option>
								</form:select>
					     </div>			   
						</div>
						<div class="row form-group">			 
								<label class="control-label col-sm-3">
							    	<spring:message code="anvizent.package.label.webService"/>		    	
							    </label>
							   	<div class="col-sm-6">
								   	<form:select path="webService" cssClass="form-control" >
											<form:option value="0">select <spring:message code="anvizent.package.label.selectoptions"/></form:option>
											<form:option value="enable"><spring:message code="anvizent.package.label.Enable"/>  </form:option>
											<form:option value="disable"><spring:message code="anvizent.package.label.Disable"/> </form:option>
											<form:option value="noDisplay"><spring:message code="anvizent.package.label.NotDisplay"/> </form:option>
									</form:select>
						     </div>			   
						</div>
						<div class="row form-group">			 
							<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.fileSize"/></label>
						   	<div class="col-sm-6">
						    	<form:input path="fileSize" cssClass="form-control"/> 	
						    </div>			   
						</div>
						
						<div class="row form-group">
							<div class="col-sm-12">
								<button type="button" class="btn btn-primary btn-sm" id="saveSettings"><spring:message code="anvizent.package.label.Save"/> </button>
							</div>
						</div>	
		    		</div>		
				</div>
 			</div>
		</c:if>
	</form:form>

</div>