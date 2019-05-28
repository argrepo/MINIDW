<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.clientConfigurationSettings"/> </h4>
 	</div>
	<jsp:include page="admin_error.jsp"></jsp:include>
 	
 	<input type="hidden" id="userId" value="<c:out value="${principal.userId}"/>">
	
	<c:url value="/admin/clientConfigurationSettings" var="url"/>
	<input type="hidden" value="${url}/save" id="save">
	
	<form:form modelAttribute="clientConfigurationSettings" action="${url}">
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
<c:if test="${clientConfigurationSettings.clientId != null && clientConfigurationSettings.clientId != 0}">
	<div class="row form-group"></div>
			<div class="col-sm-6">
		    	<div class="panel panel-default">
		    	<div class="panel-heading"><spring:message code="anvizent.package.label.EmailDetails"/></div>
		    		<div class="panel-body">
		    		
		    			<div class="row form-group" >			 
							<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.From"/>:</label>					
						   	<div class="col-sm-8">
						   		<form:input path="from" cssClass="form-control from" data-minlength="1" data-maxlength="255"/>
						    </div>			   
						</div>
						
						<div class="row form-group">
			          		<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.Password"/>:</label>
			          		<div class="col-sm-8">
			          			<form:input path="password" cssClass="form-control password" data-minlength="1" data-maxlength="45"/>
			          		</div>
			          	</div>
			          	
			          	
						<div class="row form-group">
			          		<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.To"/>:</label>
			          		<div class="col-sm-8">
			          			<form:input path="to" cssClass="form-control to" data-minlength="1" data-maxlength="45"/>
			          		</div>
			          	</div>
			          	
			          	
						<div class="row form-group">
			          		<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.CC"/> :</label>
			          		<div class="col-sm-8">
			          			<form:input path="cc" cssClass="form-control ccInfo" data-minlength="1" data-maxlength="45"/>
			          		</div>
			          	</div>
			          	
			          	
			          	<div class="row form-group">
			          		<label class="control-label col-sm-3"> <spring:message code="anvizent.package.label.BCC"/>:</label>
			          		<div class="col-sm-8">
			          			<form:input path="bcc" cssClass="form-control bccInfo" data-minlength="1" data-maxlength="45"/>
			          		</div>
			          	</div>
			          	
			          	<div class="row form-group">
			          		<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.ReplyTo"/> :</label>
			          		<div class="col-sm-8">
			          			<form:input path="replyTo" cssClass="form-control replyTo" data-minlength="1" data-maxlength="45"/>
			          		</div>
			          	</div>
			          	
	          		</div>
		    	</div>
		    </div>
		    
		    <div class="col-sm-6">
		    	<div class="panel panel-default">
		    	<div class="panel-heading"><spring:message code="anvizent.package.message.SMTPDetails"/></div>
		    		<div class="panel-body">
			          	<div class="row form-group">
			          		<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.SMTPHost"/>:</label>
			          		<div class="col-sm-8">
			          			<form:input path="smtpHost" cssClass="form-control smtpHost" data-minlength="1" data-maxlength="45"/>
			          		</div>
			          	</div>
			          	
			          	<div class="row form-group">
			          		<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.SocketFactoryPorts"/>:</label>
			          		<div class="col-sm-8">
			          			<form:input path="smtpFactoryPort" cssClass="form-control socketFactPort" data-minlength="0" data-maxlength="45"/>
			          		</div>
			          	</div>
			          	
			          	<div class="row form-group">
			          		<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.SMTPPort"/>:</label>
			          		<div class="col-sm-8">
			          			<form:input path="smtpPort" cssClass="form-control smtpPort" data-minlength="0" data-maxlength="45"/>
			          		</div>
			          	</div>
	          		</div>
		    	</div>
		    </div>
		   	<div class="row form-group">
					<div class="col-sm-12">
						<button type="button" class="btn btn-primary btn-sm" id="saveClientConfigSettings"><spring:message code="anvizent.package.label.Save"/></button>
					</div>
			</div>	
		</c:if>
	</form:form>
</div>	