<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.instantScriptExecution"/></h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		 <jsp:include page="_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="${principal.userId}">
	</div>
   
<input type="hidden" id="userID" value="${principal.userId}">
<c:url value="/adt/package/instantExecution" var="url"/>

<c:url value="/adt/package/instantExecution" var="executeScripts"/>
<input type="hidden" value="${executeScripts}" id="executeScriptsUrl">

<c:url value="/adt/package/instantExecution/previous" var="previousExecuteScripts"/>
<input type="hidden" value="${previousExecuteScripts}" id="previousExecuteScriptsUrl">

<form:form modelAttribute="tableScriptsForm" action="${Url}">   
<form:hidden path="is_Active" value="true"/>
			<div class="col-md-12">
				
				<div class="panel panel-info">
                   <div class="panel-body">
                    <div class="row form-group" id="clientListId" >		 
                      <label class="control-label col-xs-3"><spring:message code="anvizent.package.label.client"/>:</label>
		             	<div class="col-xs-6">
					       <form:select path="clientId" class="form-control">
					                <spring:message code="anvizent.package.label.selectClient" var="selectOption" />
					                <option value="0">${selectOption}</option>
									<c:if test="${not empty getClientList }">
										<form:options items="${getClientList }"/>
									</c:if>
								</form:select>
								
			                </div>
			               <div class="col-xs-3">
			               	<button type="button" class="btn btn-sm btn-primary hidden"  id="showPreviousExecutedScripts"><spring:message code="anvizent.package.label.showPreviousExecutedScripts"/></button>
			               </div>		
		               </div>
		        
					   <div class="row form-group">		 
                       <label class="control-label col-sm-3"><spring:message code="anvizent.package.label.targetschema"/>:</label>
			               <div class="col-sm-6">
					       <form:select path="schemaName" class="form-control">
									<c:if test="${not empty getTargetSchemaList }">
										<form:options items="${getTargetSchemaList }"/>
									</c:if>
								</form:select>
			                </div>		
	                   	</div>
					
				           	<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.description"/>: </label>
								<div class="col-sm-6">
								<spring:message
										code="anvizent.package.label.description" var="descriptionPlaceholder" />
									<form:textarea path="scriptDescription"  placeHolder = "${descriptionPlaceholder}" data-minlength="1" data-maxlength="500" cssClass="form-control"/>
								</div>
							</div>
						  	<div class="row form-group">
								   <label class="control-label col-sm-3"><spring:message code="anvizent.admin.label.sqlScript"/>:</label>
								<div class="col-sm-6">
								     <spring:message code="anvizent.admin.label.sqlScript"  var="sqlScriptPlaceholder" />
									 <form:textarea path="sqlScript" rows="9" style="overflow-y: auto;max-height: 425px;margin: 0px -147.75px 0px 0px;height: 195px;"   placeHolder = "${sqlScriptPlaceholder}"  cssClass="form-control"/>
								</div>
							</div>
						<div class="row form-group">
								<div class='col-sm-6'>
									<button type="button" class="btn btn-sm btn-primary" id="executeScripts"><spring:message code="anvizent.package.label.execution"/></button>
								</div>
				        </div>

					</div>
					</div>
				</div>
				
		<c:if test="${not empty displayPrevious }">		
		<div id="previousScripts">
			<div class='row form-group'>
				<div class="col-md-12" style="padding:0px;">
				<table class="table table-striped table-bordered tablebg  dataTable no-footer"   id="previousExecutedScripts" style ="table-layout: fixed;word-wrap: break-word;">
					 <thead>
		                <tr>
			                <th><spring:message code="anvizent.package.label.scriptid"/></th>
			                <th><spring:message code="anvizent.package.label.ScriptDescription"/></th>
			                <th><spring:message code="anvizent.package.label.sqlscript"/></th>
			                <th><spring:message code="anvizent.package.label.status"/></th>
			                <th><spring:message code="anvizent.package.label.ExecutionStatusMessage"/></th>
			                <th><spring:message code="anvizent.package.label.executedBy"/></th>
			                <th><spring:message code="anvizent.package.label.executedDate"/></th>
		                </tr> 
		              </thead>
			                <tbody id="tableScriptsTableBody">
			                <c:if test="${not empty prevoiusExcutetedScriptList }">		
			                <c:forEach items="${prevoiusExcutetedScriptList}" var ="tableScripts">
			                   <tr>
					                <td>${tableScripts.id}</td>
					                <td><c:out value="${tableScripts.scriptDescription}" escapeXml="true"/></td>
					                <td>
					                      <input type="button" id="viewScript" value="View" class="btn btn-primary btn-sm" data-sid="${tableScripts.id}">
				                    </td> 
					                <td>${tableScripts.execution_status?'Completed':'Failed'}</td>
					                 <td>
						                 <c:if test="${tableScripts.execution_status}">
						                 -
						                 </c:if>
						                 <c:if test="${!tableScripts.execution_status}">
							                <input type="button" data-sid="${tableScripts.id}" id="viewErrorScripts" value="View Error" class="btn btn-primary  btn-sm viewErrorScripts"/> 
						                 </c:if>
					                </td>
					                <td>${tableScripts.modification.createdBy}</td>
					                <td>${tableScripts.modification.createdTime}</td>
			                	</tr> 
			                </c:forEach>
			                </c:if>	
			                </tbody>
              </table>
			</div>
		  </div>
		</div>
		</c:if>
			
				

</form:form>

              <div class="modal fade" tabindex="-1" role="dialog" id="viewsqlScriptPopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog">
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewsqlScriptHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 600px; overflow-y: auto; overflow-x: hidden;">
										 <div class="form-group">
                                         <label for="comment"><spring:message code="anvizent.package.label.query"/>:</label>
                                         <textarea class="form-control" rows="5" id="viewsqlScripts" disabled="disabled"></textarea>
                                         </div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div> 
					  </div> 
				</div>
				
					<div class="modal fade" tabindex="-1" role="dialog" id="viewTableScriptExecutionErrorPopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog">
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewTableScriptExecutionErrorHeader"><spring:message code="anvizent.package.label.errorMessage"/></h4>
					      </div>
					      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
										 <div class="form-group">
                                         <label for="comment"></label>
                                         <textarea class="form-control" rows="5" id="tableScriptExecutionError" disabled="disabled"></textarea>
                                         </div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div><!-- /.modal-content -->
					  </div><!-- /.modal-dialog -->
				</div>	

</div>




