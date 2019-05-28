<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
<div class='row form-group'>
<h4 class="alignText"><spring:message code="anvizent.admin.label.tablescripts"/></h4>
</div>
<div class="col-md-10">
<div class="dummydiv"></div>
		<div class="col-sm-10">
		</div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="${principal.userId}">
</div>
<div class="col-md-10">
<div class="createParamSucessFailure"></div>
</div>


<div class="col-md-6">	 
			<table class="table table-striped table-bordered tablebg  dataTable no-footer"   id="tableScriptsTable">
			<c:choose>
			<c:when test="${not empty tableScripts}">
			 <thead>
                <tr>
                <th class="col-xs-1"><spring:message code="anvizent.package.label.S.No"/></th>
                <th class="col-xs-2"><spring:message code="anvizent.package.label.ExecutionOrder"/></th>
                <th class="col-xs-2"><spring:message code="anvizent.package.label.ScriptName"/></th>
                <th  style="display:none"></th> 
                <th class="col-xs-1"> <spring:message code="anvizent.package.label.SQLScript"/></th> 
                <th class="col-xs-1"><spring:message code="anvizent.package.label.Edit"/></th> 
                <th class="col-xs-3" ><spring:message code="anvizent.package.label.Active"/></th> 
                </tr> 
                </thead>
                <tbody id="tableScriptsTableBody">
                <c:forEach items="${tableScripts}" var ="tableScripts">
                   <tr id="tableScriptsRow" >
		                <td id="tableScriptsId">${tableScripts.id}</td>
		                <td id="tableScriptsPriority">${tableScripts.priority}</td>
		                <td id="tableScriptsName">${tableScripts.scriptName}</td> 
		                <td style="display:none"><input type="hidden" id="viewSqlTableScripts"  data-sqlScriptName="${tableScripts.scriptName}" data-sqlScript="${tableScripts.sqlScript}"></td>
		                <td><input id="viewSqlScript" value="View Sql Script" class="btn btn-primary btn-sm"></td>
		                <td><a href="#" class="btn btn-primary btn-xs editTableScript" ><span class="glyphicon glyphicon-edit"></span></a> </td> 
		                 <td>
								        <c:if test="${tableScripts.is_Active == true}">
									        <label>
										        <input type="radio" class="param-radio"   value="Yes" name="isActive${tableScripts.id}"  checked="checked">
										        <span><spring:message code="anvizent.package.label.Yes"/></span>
								           </label>  
								           <label>
									        	<input type="radio" class="param-radio" value="No" name="isActive${tableScripts.id}">
										        <span><spring:message code="anvizent.package.label.No"/></span>
								            </label>
								        </c:if>
								        <c:if test="${tableScripts.is_Active == false}">
								         <label>
										        <input type="radio" class="param-radio" value="Yes" name="isActive${tableScripts.id}" >
										        <span><spring:message code="anvizent.package.label.Yes"/></span>
								          </label>
									      <label>
								        	<input type="radio" class="param-radio" value="No" name="isActive${tableScripts.id}"  checked="checked">
									        <span><spring:message code="anvizent.package.label.No"/></span>
								           </label>
								        </c:if>
		                 </td>
                	</tr>  
                </c:forEach>
                </tbody>
			</c:when>
			<c:otherwise>
			  <spring:message code="anvizent.package.label.NoDataFound."/>
			</c:otherwise>
			</c:choose>
            </table>
			</div>
			

<div class="col-md-6">	 
         <div class="panel panel-info" id='createContextParameters'>
		    <div class="panel-heading"><spring:message code="anvizent.package.label.AddUpdateTableScripts"/></div>
			<div class="panel-body">
				<div class='row form-group'>
				   <div class='col-sm-3'>
						<spring:message code="anvizent.admin.label.scriptName"/>:
				   </div>
				   <div class='col-sm-6'>
				        <input type="hidden" class="form-control" id="tableScriptId">
						<input type="text" class="form-control" id="scriptNameFromForm"  placeholder="<spring:message code = "anvizent.admin.label.scriptName"/>" name="scriptNameFromForm">
				   </div>
				
				</div>
				
				<div class='row form-group'>
				   <div class='col-sm-3'>
						 <spring:message code="anvizent.package.label.ExecutionOrder"/>:
				   </div>
				   <div class='col-sm-6'>
				    <select class="form-control" id="priorityFromForm">
				      <option value="0"> <spring:message code="anvizent.package.label.SelectOrder"/></option>
				      <c:forEach var="i" begin="1" end="100">
                      <option value="${i}">${i}</option>
                      </c:forEach>
				   </select>
				</div>
				</div>
				
				<div class='row form-group'>
				<div class='col-sm-3'><spring:message code="anvizent.package.label.Active"/>: </div>
				 <div class='col-sm-6'>
					  <label>
					  <input type="radio" class="param-radio isActiveYesNo" id="isActiveYes" name="isActiveYesNo" value="Yes"  >
					  <span><spring:message code="anvizent.package.label.Yes"/></span>
					  </label>  
					 
					 <label>
					 <input type="radio" class="param-radio isActiveYesNo" id="isActiveNo" name="isActiveYesNo" value="No"  >
					 <span><spring:message code="anvizent.package.label.No"/></span>
					 </label>
				</div>
				
				</div>
				<div class='row form-group'>
				   <div class='col-sm-3'>
						<spring:message code="anvizent.admin.label.sqlScript"/>:
				   </div>
				   <div class='col-sm-6'>
				  <textarea class="form-control" rows="9" style="overflow-y: scroll;max-height: 425px;margin: 0px -147.75px 0px 0px;width: 368px;height: 195px;" id="sqlScriptFromForm" placeholder="<spring:message code = "anvizent.admin.label.sqlScript"/>" name="sqlScript"></textarea>
				   </div>
				
				</div>
				<div class='row form-group'>
				   <div class='col-sm-3'>
				   </div>
				   <div class='col-sm-6'>
				    <button id="addTableScript" type="submit" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Add"/></button>
					<button id="addTableScript" type="submit" class="btn btn-primary btn-sm updateTableScript" disabled><spring:message code="anvizent.package.label.Update"/></button>
					<button id="resetTableScript" type="submit" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Reset"/></button>
				   </div>
				 
				</div>
			</div>
       </div>
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
</div>
</div>