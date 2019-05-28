<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.navLeftTabLink.label.tablescripts"/></h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	</div>
   
<c:url value="/admin/clientTableScripts/edit" var="editUrl"/>
<c:url value="/admin/clientTableScriptsList" var="getUrl"/>
<input type="hidden" value="${getUrl}" id="getUrl">
<c:url value="/admin/clientTableScripts/addForm" var="addForm"/>
<c:url value="/admin/clientTableScripts/update" var="updateUrl"/>
<input type="hidden" value="${updateUrl }" id="updateUrl">
<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">

<form:form modelAttribute="tableScripts" action="${editUrl }" enctype="multipart/form-data">   
	<c:choose>
		<c:when test="${tableScripts.pageMode == 'list' }">
		<div style="padding:0px 15px;">
		    <div  style="width:100%;padding:0px 15px;">
		    <div class="row form-group" style="padding:5px;border-radius:4px;">
		    <c:url value="/admin/clientTableScripts/add" var="addUrl" />
			<button style="float:right;" class="btn btn-sm btn-success" data-addurl="<c:out value="${addUrl }"/>" id="addTableScriptsMapping"> <spring:message code="anvizent.package.button.add"/> </button>
			</div>
			</div>
			</div>
			<div class="col-md-12">
				<table class="table table-striped table-bordered tablebg  dataTable no-footer"   id="tableScriptsMappingTable">
			 <thead>
                <tr>
                <th class="col-xs-1"><spring:message code="anvizent.package.label.scriptid"/></th>
                <%-- <th class="col-xs-1"><spring:message code="anvizent.package.label.scripttype"/></th>
                <th class="col-xs-1"><spring:message code="anvizent.package.label.clientId"/></th> --%>
                <th><spring:message code="anvizent.package.label.targetschema"/></th>
                <th><spring:message code="anvizent.package.label.scriptname"/></th>
                <th><spring:message code="anvizent.package.label.version"/></th>	
                <th  style="display:none"></th> 
                <th class="col-xs-1"><spring:message code="anvizent.package.label.sqlscript"/></th> 
                <th class="col-xs-1"><spring:message code="anvizent.package.label.edit"/></th> 
                <th class="col-xs-1"><spring:message code="anvizent.package.label.active"/></th>
                <th class="col-xs-3"><spring:message code="anvizent.package.label.createdDate"/></th>
                <th class="col-xs-3"><spring:message code="anvizent.package.label.modifiedDate"/></th>
                </tr> 
                </thead>
                <tbody id="tableScriptsTableBody">
                <c:forEach items="${tableScriptsList}" var ="tableScripts">
                   <tr id="tableScriptsRow" >
		                <td id="tableScriptsId"><c:out value="${tableScripts.id}"/></td>
		                <%-- <td id="tableScriptsId"><c:out value="${tableScripts.scriptTypeName}" ></c:out></td>
		                <td id="tableScriptsClientId"><c:if test="${tableScripts.clientId == 0}">-</c:if> <c:if test="${tableScripts.clientId != 0}"><c:out value="${tableScripts.clientId}"/></c:if></td> --%>
		                <td id="tableScriptsSchemaName"><c:out value="${tableScripts.schemaName}" /></td>
		                <td id="tableScriptsName" style="word-break: break-all;white-space: normal;width: 180px;"><c:out value="${tableScripts.scriptName}" /></td> 
		                <td>${not empty tableScripts.version ? tableScripts.version : '-'}</td>
		                <td style="display:none"><input type="hidden" id="viewSqlTableScripts"  data-sqlScriptName="<c:out value="${tableScripts.scriptName}"/>" data-sqlScript="<c:out value="${tableScripts.sqlScript}"/>"></td>
		                <td><input type="button" id="viewSqlScript" value="View" class="btn btn-primary btn-sm" data-sid="${tableScripts.id}"></td>
		                <td><button  class="btn btn-primary btn-xs editTableScript tablebuttons" type="submit" name="id" value="${tableScripts.id}">
												<i class="fa fa-pencil" aria-hidden="true"></i>
										</button> </td> 
		                 <td>
								        <c:if test="${tableScripts.is_Active == true}">
									        <spring:message code="anvizent.package.label.Yes"/>
								        </c:if>
								        <c:if test="${tableScripts.is_Active == false}">
								          <spring:message code="anvizent.package.label.No"/>
								        </c:if>
		                 </td>
		                 <td id="createdDate"><c:out value="${tableScripts.created_Date}"/></td> 
		                 <td id="modifiedDate"><c:if test="${empty tableScripts.modified_Date }">-</c:if><c:if test="${not empty tableScripts.modified_Date }">${ tableScripts.modified_Date }</c:if> </td>  	 
                	</tr>  
                </c:forEach>
                </tbody>
            </table>
			</div>
		</c:when>
		 <c:when test="${tableScripts.pageMode == 'edit' || tableScripts.pageMode =='add' }">
			<div class="col-md-12">
				<c:set var="updateMode" value="Update"/>
				<c:if test="${tableScripts.pageMode == 'add'  }">
					<c:set var="updateMode" value="Add" />
				</c:if>
				<div class="panel panel-default">
					<div class="panel-heading">${updateMode }<spring:message code="anvizent.package.label.TableScript"/></div>
					<div class="panel-body">
					     <div class="row form-group hidden">		 
                           <label class="control-label col-sm-3"><spring:message code="anvizent.package.label.ScriptType"/>:</label>
			                <div class="col-sm-6">
					       <form:select path="scriptTypeName" class="form-control">
									<c:if test="${not empty getScriptTypeList }">
										<form:options items="${getScriptTypeList }"/>
									</c:if>
								</form:select>
					 
			                </div>		
		                  </div>
                    <div class="row form-group" id="clientListId" >		 
                      <label class="control-label col-sm-3"><spring:message code="anvizent.package.label.Client"/>:</label>
		             	<div class="col-sm-6">
					       <form:select path="clientId" class="form-control" >
					                <spring:message code="anvizent.package.label.selectClient" var="selectOption" />
					                <option value="0">${selectOption}</option>
									<c:if test="${not empty getClientList }">
										<form:options items="${getClientList }"/>
									</c:if>
								</form:select>
					 
			                </div>		
		               </div>
					
					   <div class="row form-group">		 
                       <label class="control-label col-sm-3"><spring:message code="anvizent.package.label.TargetSchema"/>:</label>
			               <div class="col-sm-6">
					       <form:select path="schemaName" class="form-control">
									<c:if test="${not empty getTargetSchemaList }">
										<form:options items="${getTargetSchemaList }"/>
									</c:if>
								</form:select>
			                </div>		
	                   	</div>
					
					     <div class='row form-group'>
				              <label class="control-label col-sm-3">
						           <spring:message code="anvizent.admin.label.scriptName"/>:
				             </label>
				              <div class='col-sm-6'>
				                 <spring:message code="anvizent.admin.label.scriptName"  var="scriptNamePlaceholder" />
				                 <form:hidden path="id"/>
						          <form:input path="scriptName" cssClass="form-control" maxlength="45" data-minlength="1" data-maxlength="45" placeholder="${scriptNamePlaceholder}"></form:input>
				              </div>
				          </div>
				          
				          <div class="row form-group">
				          	<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.version" /></label>
				          	<div class="col-sm-6">
				          		<spring:message code="anvizent.package.label.version" var="versionPlaceholder"   />
				          		<form:input path="version" cssClass="form-control" placeholder="${versionPlaceholder}" maxlength="45" data-minlength="1" data-maxlength="45"/>
				          	</div>
				          </div>
				          
				           	<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.description"/> </label>
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
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.activeStatus"/> :</label>
								<div class='col-sm-6'>
								 	<div class="is_Active">
								 		 <label class="radio-inline">
											<form:radiobutton path="is_Active" value="true"/><spring:message code="anvizent.package.button.yes"/>
										 </label>	
										 <label class="radio-inline">
									    	<form:radiobutton path="is_Active" value="false"/><spring:message code="anvizent.package.button.no"/> 
									    </label>
								 	</div>
								</div>
							</div>
							
						 <div class="row form-group">
							<div class="col-sm-6">
								<c:choose>
									<c:when test="${tableScripts.pageMode == 'edit'}">
										<button id="updateTableScript" type="button"
									class="btn btn-primary btn-sm" ><spring:message code="anvizent.package.label.Update"/></button>
									 
									</c:when>
									<c:when test="${tableScripts.pageMode == 'add'}">
										<button id="addTableScript" value="${addForm}" type="button" 
									class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Add"/></button>
									</c:when>
								</c:choose>
								<button id="resetTableScript" type="button" 
									class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Reset"/></button>
									<a id="resetDlMaster" type="reset" href="<c:url value="/admin/clientTableScripts"/>"
									class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.label.Back"/></a>
							</div>
						</div>

					</div>
				</div>


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

</div>




