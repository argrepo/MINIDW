<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.navLeftTabLink.label.executeScripts"/></h4>
	</div>
	<jsp:include page="admin_error.jsp"></jsp:include>
	<div class='tableScriptsValidation'></div>
	<input type="hidden" id="userID" value="${principal.userId}">
   
 
<c:url value="/admin/clientTableScripts/mapping" var="getUrl"/>
<input type="hidden" value="${getUrl}" id="getUrl">
<c:url value="/admin/clientTableScripts/mapping/save" var="saveMapping"/>
<input type="hidden" value="${saveMapping }" id="saveMappingUrl">

<form:form modelAttribute="tableScriptsForm" action="${editUrl }"   enctype="multipart/form-data">   

                   <div class="row form-group">		 
                      <label class="control-label labelsgroup col-sm-2"><spring:message code="anvizent.package.label.client"/> :</label>
		             	<div class="col-sm-6">
					       <form:select path="clientId" class="form-control">
					                <spring:message code="anvizent.package.label.selectClient" var="selectOption" />
					                <option value="0">${selectOption}</option>
									<c:if test="${not empty getClientList }">
										<form:options items="${getClientList }"/>
									</c:if>
								</form:select>
					 
			                </div>	
			                   	
		               </div>
	<c:choose>
		<c:when test="${tableScriptsForm.pageMode == 'list'}">
		
		   <div class="row form-group">
		     <button  type="button" style="float:right; margin-right: 1.5em;" id="saveMapping" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.executescripts"/></button></div>
			<div class="col-md-12">
				<table class="table table-striped table-bordered tablebg  dataTable no-footer"   id="tableScriptsMappingTable">
				 <thead>
	                <tr>
		                <th><input type="checkbox" id="selectall"/> <spring:message code="anvizent.package.label.selectAll"/></th>
		                <th><spring:message code="anvizent.package.label.scriptid"/></th>
		                <th><spring:message code="anvizent.package.label.scripttype"/></th>
		                <th><spring:message code="anvizent.package.label.scriptname"/></th>
		                <th  style="display:none"></th> 
		                <th><spring:message code="anvizent.package.label.sqlscript"/></th> 
		                <th><spring:message code="anvizent.package.label.executionstatus"/></th>
		                <th><spring:message code="anvizent.package.label.executionorder"/></th>
		                <th class="col-xs-3"><spring:message code="anvizent.package.label.createdDate"/></th>
		                <th class="col-xs-3"><spring:message code="anvizent.package.label.modifiedDate"/></th>
		                <th class="col-xs-3"><spring:message code="anvizent.package.label.executedDate"/></th>
	                </tr> 
	                </thead>
                <tbody>
                <c:forEach items="${tableScriptsForm.tableScriptList}" var ="tableScriptList" varStatus="loop"  > 
                <c:if test="${!tableScriptList.executed}">
                   <tr id="tableScriptsRow" >
                        <td  style="width: 10px;">
                        	<input type="checkbox" name="tableScriptList[${loop.index}].checkedScript" id="tableScriptsInfoById${loop.index}" 
                        	${tableScriptList.checkedScript ? 'checked="checked"' : ''}
                        	${tableScriptList.executed ? 'disabled="disabled"' : '' } class="tableScriptsInfoById checkCase" >
                        </td>  
		                <td style="width:20px;">${tableScriptList.id}<input type="hidden" name="tableScriptList[${loop.index}].id" value="${tableScriptList.id}"/></td>
		                <td >${tableScriptList.scriptType}<input type="hidden" name="tableScriptList[${loop.index}].scriptType" value="${tableScriptList.scriptType}"/></td>
		                <td style="word-break: break-all;white-space: normal;width: 230px;">${tableScriptList.scriptName}<input type="hidden" name="tableScriptList[${loop.index}].scriptName" value="${tableScriptList.scriptName}"/></td> 
		                <td style="display:none"><input type="hidden" id="viewSqlTableScripts"  data-sqlScriptName="${tableScriptList.scriptName}" data-sqlScript="${tableScriptList.sqlScript}"></td>
		                <td><input id="viewSqlScript" value="View" class="btn btn-primary btn-sm" data-sid="${tableScriptList.id}" type="button"></td>
		                <td style="width:20px;">
	                	   <input type="hidden" name="tableScriptList[${loop.index}].id" value="${tableScriptList.id}"/>
                	   		${tableScriptList.executed ? "Completed" : tableScriptList.error ? "":"Pending"  }
                	   		<c:if test="${tableScriptList.executed == false && tableScriptList.error == true }"><input type="button" data-sid="${tableScriptList.id}"  data-sname="${tableScriptList.scriptName}" id="viewErrorScripts" value="Error" class="btn btn-primary  btn-sm viewErrorScripts"/> 
						  </c:if>
						  <input type="hidden" name="tableScriptList[${loop.index}].executed" value="${tableScriptList.executed}" class='tableScriptsExecutedOrNot checkCase'/>
						  </td>
		                <td> 
				           <select class="form-control tableScriptPriority" id="tableScriptPriority${loop.index}"   name="tableScriptList[${loop.index}].priority" <c:if test="${tableScriptList.executed == true}">disabled="true"</c:if> >
				           		<option value="0">Order</option>
				           	<c:forEach var="i" begin="1" end="${fn:length(tableScriptsForm.tableScriptList)}">
				            	<c:if test="${tableScriptList.priority == i}">
				            	<option value="${i}" selected>${i}</option>
				            	</c:if>
                           		<c:if test="${tableScriptList.priority != i}">
				            	<option value="${i}">${i}</option>
				            	</c:if>
                          </c:forEach>
				         </select>
				      </td>
				       <td id="createdDate">${tableScriptList.created_Date}</td> 
		               <td id="modifiedDate">
		               <c:if test="${empty tableScriptList.modified_Date }">-</c:if><c:if test="${not empty tableScriptList.modified_Date }">${ tableScriptList.modified_Date }</c:if> 
		               </td>
		               	<td>${not empty tableScriptList.modification.modifiedTime ? tableScriptList.modification.modifiedTime : '-'}</td>
                	
                	</tr>  
                	</c:if>
                </c:forEach>
                </tbody>
            </table>
			</div>
			
			
			<div class="col-md-12">
				<h3><spring:message code="anvizent.package.label.ExecutedScripts"/></h3>
				<table class="table table-striped table-bordered tablebg  dataTable no-footer"   id="tableScriptsExecutedTable">
				 <thead>
	                <tr>
		                <th><spring:message code="anvizent.package.label.scriptid"/></th>
		                <th><spring:message code="anvizent.package.label.scripttype"/></th>
		                <th><spring:message code="anvizent.package.label.scriptname"/></th>
		                <th  style="display:none"></th> 
		                <th><spring:message code="anvizent.package.label.sqlscript"/></th> 
		                <th><spring:message code="anvizent.package.label.executionstatus"/></th>
		                <th><spring:message code="anvizent.package.label.executionorder"/></th>
		                <th><spring:message code="anvizent.package.label.executedDate"/></th>
	                </tr> 
	                </thead>
                <tbody>
                <c:forEach items="${tableScriptsForm.tableScriptList}" var ="tableScriptList" varStatus="loop"  > 
                <c:if test="${tableScriptList.executed}">
                
                   <tr id="tableScriptsRow" >
                        
		                <td style="width: 10px;">${tableScriptList.id}<input type="hidden" name="tableScriptList[${loop.index}].id" value="${tableScriptList.id}"/></td>
		                <td style="width: 60px;">${tableScriptList.scriptType}<input type="hidden" name="tableScriptList[${loop.index}].scriptType" value="${tableScriptList.scriptType}"/></td>
		                <td style="word-break: break-all;white-space: normal;width: 180px;">${tableScriptList.scriptName}<input type="hidden" name="tableScriptList[${loop.index}].scriptName" value="${tableScriptList.scriptName}"/></td> 
		                <td style="display:none"><input type="hidden" id="viewSqlTableScripts"  data-sqlScriptName="${tableScriptList.scriptName}" data-sqlScript="${tableScriptList.sqlScript}"></td>
		                <td style="width: 50px;"><input id="viewSqlScript" value="View" class="btn btn-primary btn-sm" data-sid="${tableScriptList.id}" type="button"></td>
		                <td style="width: 50px;">
	                	   <input type="hidden" name="tableScriptList[${loop.index}].id" value="${tableScriptList.id}"/>
                	   		${tableScriptList.executed ? "Completed" : tableScriptList.error ? "":"Pending"  }
                	   		<c:if test="${tableScriptList.executed == false && tableScriptList.error == true }"><input type="button" data-sid="${tableScriptList.id}"  data-sname="${tableScriptList.scriptName}" id="viewErrorScripts" value="Error" class="btn btn-primary  btn-sm viewErrorScripts"/> 
						  </c:if>
						  <input type="hidden" name="tableScriptList[${loop.index}].executed" value="${tableScriptList.executed}" class='tableScriptsExecutedOrNot checkCase'/>
						  </td>
		                <td style="width: 30px;"> 
				           ${tableScriptList.priority }
				      	</td>
		               	<td style="width: 50px;">${not empty tableScriptList.modification.modifiedTime ? tableScriptList.modification.modifiedTime : '-'}</td>
                	
                	</tr>  
                	</c:if>
                </c:forEach>
                </tbody>
            </table>
			</div>
			
			
		</c:when>
	</c:choose>
	 
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
		        			<h4 class="modal-title custom-modal-title" id="viewTableScriptExecutionErrorHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
										 <div class="form-group">
                                         <label for="comment"><spring:message code="anvizent.package.ErrorMsg"/>:</label>
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




