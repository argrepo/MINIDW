<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="col-md-12 rightdiv">

	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.ClientsInstantScriptExecution"/></h4>
	</div>
	<jsp:include page="admin_error.jsp"></jsp:include>
	<input type="hidden" id="userId" value="<c:out value="${principal.userId}"/>">
	<c:url value="/admin/clientsInstantScriptExecution/scriptExecution" var="executeScripts"/>
   <input type="hidden" value="${executeScripts }" id="executeScriptsUrl">
	<form:form modelAttribute="tableScriptsForm">
		<form:hidden path="pageMode" />
		<div class="row form-group">
			<label class="col-sm-2  control-label"><spring:message code="anvizent.package.label.clientId" /> :</label>
			<div class="col-sm-6">
			<spring:message code="anvizent.package.label.clientId" 	var="clientId" />
			<form:select path="clientIdList" cssClass="clientId form-control" style="white-space:pre-wrap;">
						<form:options items="${getClientList}"/>
			</form:select>
			</div>
		</div>
		<div class="row form-group">
			<label class="control-label col-sm-2"><spring:message code="anvizent.admin.label.sqlScript" />:</label>
			<div class="col-sm-6">
				<spring:message code="anvizent.admin.label.sqlScript" var="sqlScriptPlaceholder" />
				<form:textarea path="sqlScript" rows="9" 	style="overflow-y: auto;max-height: 425px;margin: 0px -147.75px 0px 0px;height: 195px;" placeHolder="${sqlScriptPlaceholder}" cssClass="form-control" />
			</div>
		</div>
		<div class="row form-group">
		<label class="control-label col-sm-2"></label>
			<div class='col-sm-6'>
				<button type="button" class="btn btn-sm btn-primary" id="executeScripts">
					<spring:message code="anvizent.package.label.execution" />
				</button>
			</div>
		</div>
		   	<div class="col-md-12">
		   		<table class="table table-striped table-bordered tablebg  dataTable no-footer" id="clientInstanceScriptExecutionTable">
		   			<thead>
		   				<tr>
		   					<th class="col-xs-2"><spring:message code="anvizent.package.label.ScriptId" /></th>
		   					<th class="col-xs-6"> <spring:message code="anvizent.package.label.clientIds" /> </th>
		   					<th class="col-xs-2"> <spring:message code="anvizent.package.label.sqlscript" /> </th>
		   				    <th class="col-xs-2"> <spring:message code="anvizent.package.label.ViewResults" /> </th>
		   				</tr>
		   			</thead>
		   			<tbody>
		   			 		<c:forEach items="${clientInstantScriptExecution}" var="clientInstantScriptExecution">
										<tr>
											<td id="clientsScriptId"><c:out value="${clientInstantScriptExecution.clientsInstantScriptId}" /></td>
											<td id="clientIdList"><c:out value="${clientInstantScriptExecution.clientIds}" /></td>
											<td id="viewSqlScript">
											<input type="button" value="View Script" class="btn btn-primary btn-sm" id="viewClientInstantExecutionScript" data-clientsinstantscriptid="${clientInstantScriptExecution.clientsInstantScriptId}" title="<spring:message code="anvizent.package.message.ViewScript" />">
					    		  			</td>
											<td>
											<input type="button" value="View Results" class="btn btn-primary btn-sm" id="viewClientInstantExecutionResults" data-clientsinstantscriptid="${clientInstantScriptExecution.clientsInstantScriptId}" title="<spring:message code="anvizent.package.message.ViewResults" />">
					     					 </td>
										</tr>
									</c:forEach>
		   			</tbody>   
		   		</table>
		   	</div>
	</form:form>
	<div class="modal fade" tabindex="-1" role="dialog" id="viewCLientScriptExecutionPopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog" style="width:65%"> 
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewDDlTableResultsHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
					        	<div class="table-responsive" >
									<table class="table table-striped table-bordered tablebg" id="viewClientScriptTableResultsTable">
										<thead>
											<tr>
												<th><spring:message code="anvizent.package.label.sNo"/></th>
												<th><spring:message code="anvizent.package.label.InstantExecutionId"/></th>
												<th><spring:message code="anvizent.package.label.ClientId"/></th>
												<th><spring:message code="anvizent.package.label.ExecutionMessage"/></th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div> 
					  </div> 
				</div>
</div>