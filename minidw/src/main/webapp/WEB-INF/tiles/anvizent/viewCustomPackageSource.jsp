<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
 <div class="col-md-12 rightdiv" >
    <div class="col-md-10">
    <div class="dummydiv"></div>
		<div class="col-sm-10"></div>
   </div>
   <div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.admin.label.viewcustompackagesource"/></h4>
	</div>
	<jsp:include page="_error.jsp"></jsp:include>
  <div class="col-md-12">
 
 <form:form modelAttribute="customPackageForm" method="POST" id="customPackageForm">
				<form:hidden path="industryId"/> 
				<form:hidden path="isStandard"/>
				<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
				<input type="hidden" id="isMapped" value="<c:out value="${userPackage.isMapped}"/>">
				<c:if test="${not empty customPackageForm.packageId}">
					<div class="row form-group" style="background-color:#fff;padding:5px;border-radius:4px;">
						
							    <div class='col-sm-6 col-md-4' >
								    <label class="control-label "><spring:message code="anvizent.package.label.packageId"/></label>
									<form:input path="packageId" class="form-control" disabled="true"/>
								</div>
								<div class='col-sm-6 col-md-4' >
								    <label class="control-label ">
										<spring:message code="anvizent.package.label.packageName"/></label>
									<form:input path="packageName" class="form-control" disabled="true"/>
								</div>
						
					</div>
				</c:if>
 
 
  <div class="panel panel-default">
			  <div class="panel-heading">
			    
			    <c:choose>
			    <c:when test="${ilConnectionMapping.isFlatFile}">
			    <h3 class="panel-title"><c:out value="${ilConnectionMapping.filePath}"/></h3>
			   <button type="button" style="float:right;margin-top:-23px;" data-previewSourcetitle="<c:out value="${ilConnectionMapping.filePath}"/>" class="btn btn-primary btn-sm databasePreviewCustom" id='checkFlatPreviewViewDetails'><span class="glyphicon glyphicon-list-alt"  title="<spring:message code="anvizent.package.button.preview"/>" aria-hidden="true"></span></button>
			    </c:when>
			    <c:otherwise>
			    <h3 class="panel-title"><c:out value="${ilConnectionMapping.iLConnection.connectionName}"/></h3>
			     <button type="button" style="float:right;margin-top:-23px;" 
			     data-previewSourcetitle="<c:out value="${ilConnectionMapping.iLConnection.connectionName}"/>"
			      class="btn btn-primary btn-sm databasePreviewCustom" id='checkTablePreviewViewDetails' 
			       ${ilConnectionMapping.iLConnection.webApp && !ilConnectionMapping.iLConnection.availableInCloud ? 'disabled':''} >
			      <span class="glyphicon glyphicon-list-alt"  title="<spring:message code="anvizent.package.button.preview"/>" aria-hidden="true"></span>
			      </button>
			    </c:otherwise>
			    </c:choose>
			  </div>
			   <div class="panel-body">
                 <c:choose>
                 <c:when test="${ilConnectionMapping.isFirstRowHasColoumnNames == true}" >
                  <c:set var="isFirstRowHasColoumnNames" value="Yes"></c:set>
                 </c:when>
                 <c:otherwise>
                 <c:set var="isFirstRowHasColoumnNames" value="No"></c:set>
                 </c:otherwise>
                 </c:choose>
                 
			                 <c:choose>
			                   <c:when test="${ilConnectionMapping.isFlatFile}">
			  	               <div class='row form-group '>
									<label class="col-sm-4 control-label"><spring:message code="anvizent.package.label.fileType"/></label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" value="<c:out value="${ilConnectionMapping.fileType}"/>">
										<input type="hidden" class="form-control" id="show_mappingId" value="<c:out value="${ilConnectionMapping.connectionMappingId}"/>">
									</div>
								</div>
								<div class='row form-group '>
									<label class="col-sm-4 control-label"><spring:message code="anvizent.package.label.delimeter"/></label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" value="<c:out value="${ilConnectionMapping.delimeter}"/>">
									</div>
								</div>
								<div class='row form-group '>
									<label class="col-sm-4 control-label"><spring:message code="anvizent.package.label.file"/></label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" value="<c:out value="${ilConnectionMapping.filePath}"/>">
									</div>
								</div>
							</c:when>
						    <c:otherwise>
			                                 	<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.connectionName"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_connectionName" value="<c:out value="${ilConnectionMapping.iLConnection.connectionName}"/>" disabled="disabled">
													<input type="hidden" class="form-control" id="show_connectionId" value="<c:out value="${ilConnectionMapping.iLConnection.connectionId}"/>">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.connectorType"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_databaseType" value="<c:out value="${ilConnectionMapping.iLConnection.database.name}"/>" disabled="disabled">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.connectionType"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_connectionType" value="<c:out value="${ilConnectionMapping.iLConnection.connectionType}"/>" disabled="disabled">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.serverName"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_serverName" value="<c:out value="${ilConnectionMapping.iLConnection.server}"/>" disabled="disabled">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.userName"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_userName" value="<c:out value="${ilConnectionMapping.iLConnection.username}"/>" disabled="disabled">
												</div>
							</div>
							<c:if test="${not empty ilConnectionMapping.iLConnection.dbVariablesList}">
									 <c:forEach items="${ilConnectionMapping.iLConnection.dbVariablesList}" var="entry" varStatus="myIndex">
											<c:choose>
										 <c:when test="${myIndex.index == 0}">
										 <div class='row form-group'>
											<div class='col-sm-4'><spring:message code ="anvizent.package.label.dataBasevariables"/> :</div>
												<div class='col-sm-4'>
													<input type="text" class="form-control" disabled="disabled" value="${entry.key}">
												</div>
												<div class='col-sm-4'>
													<input type="text" class="form-control" disabled="disabled" value="${entry.value}">
												</div>
												<br>
									    </div>
										 </c:when>
										 <c:otherwise>
										  <div class='row form-group'>
										       <div class='col-sm-4'></div>
												<div class='col-sm-4'>
													<input type="text" class="form-control" disabled="disabled" value="${entry.key}">
												</div>
												<div class='col-sm-4'>
													<input type="text" class="form-control" disabled="disabled" value="${entry.value}">
												</div>
												<br>
									    </div>
										 </c:otherwise>
										 </c:choose>
									</c:forEach>
								</c:if>
							<div class='row form-group '>
									    <div class='col-sm-4'>
													<spring:message code="anvizent.package.label.typeOfCommand"/>:
												</div>
										<div class='col-sm-6'>
										<select class="form-control typeOfCommand"  id="show_typeOfCommand"  disabled="disabled">
											<spring:message code="anvizent.package.label.query" var="query"/>
			 								<spring:message code="anvizent.package.label.storedProcedure" var="storedProcedure"/>
											<option value="<c:out value="${ilConnectionMapping.typeOfCommand}"/>">
												${ilConnectionMapping.typeOfCommand == 'Query' ? query : storedProcedure}
											</option>
										</select>
										</div>
										<div class='col-sm-2' >
										 <button type="button" style="float:right;margin-top:-6px;" 
										  class="btn btn-primary btn-sm  editIlSourceDetails" 
										    ${ilConnectionMapping.iLConnection.webApp && !ilConnectionMapping.iLConnection.availableInCloud ? 'disabled':''} >
										  <span class="glyphicon glyphicon-edit" title="<spring:message code="anvizent.package.label.edit"/>" 
										  aria-hidden="true"></span>
										  </button>
										 
										</div>
								</div>
							<c:choose>
							<c:when test="${ilConnectionMapping.typeOfCommand == 'Query'}">
							<div class='row form-group '>
												<div class='col-sm-4'>
												</div>
												<div class='col-sm-8' >
													<textarea class="form-control" rows="6" id="show_queryScript" readonly="readonly"><c:out value="${ilConnectionMapping.iLquery}"/></textarea>
													<input type="text" class="form-control" id="show_procedureName"   disabled="disabled" style="display:none;">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													&nbsp;
												</div>
												<div class='col-sm-8' >
												    <input type="button" value="<spring:message code="anvizent.package.button.validateQuerySP"/>"  id="checkQuerySyntax" style="display:none" class="btn btn-primary btn-sm">
													 &nbsp; <input type="button" value='<spring:message code="anvizent.package.button.preview"/>' data-previewSourcetitle="<c:out value="${ilConnectionMapping.iLConnection.connectionName}"/>" id='checkTablePreviewViewDetails' class="btn btn-primary btn-sm checkTablePreview" style="display:none" />
										             &nbsp; <input type="button" value="<spring:message code="anvizent.package.button.update"/>" id="updateILConnectionMapping" data-mappingId="<c:out value="${ilConnectionMapping.connectionMappingId}"/>" style="display:none" class="btn btn-primary btn-sm updateILConnectionMapping"  >
										 
												</div>
							</div>
							</c:when>
							<c:otherwise>
							<div class='row form-group '>
												<div class='col-sm-4'>
												</div>
												<div class='col-sm-8' >
													<textarea class="form-control" rows="6" id="show_queryScript" style="display:none"  disabled="disabled"> </textarea>
													<input type="text" class="form-control" id="show_procedureName" value="<c:out value="${ilConnectionMapping.iLquery}"/>"  readonly="readonly" >
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													&nbsp;
												</div>
												<div class='col-sm-8' >
												    <input type="button" value="<spring:message code="anvizent.package.button.validateQuerySP"/>"  id="checkQuerySyntax" style="display:none" class="btn btn-primary btn-sm">
													 &nbsp; <input type="button" value='<spring:message code="anvizent.package.button.preview"/>' data-previewSourcetitle="<c:out value="${ilConnectionMapping.iLConnection.connectionName}"/>" id='checkTablePreviewViewDetails' class="btn btn-primary btn-sm checkTablePreview" style="display:none" />
										             &nbsp; <input type="button" value="<spring:message code="anvizent.package.button.update"/>" id="updateILConnectionMapping" data-mappingId="<c:out value="${ilConnectionMapping.connectionMappingId}"/>" style="display:none" class="btn btn-primary btn-sm updateILConnectionMapping"  >
										 
												</div>
							</div>
							</c:otherwise>
							</c:choose>
								</c:otherwise>
								</c:choose>
								 <div class='row form-group '>
								         <div class='col-sm-4'> </div>
										 <div class='col-sm-8'> 
										 <div class="alert alert-danger iLInputMessage" style="display:none;"></div>
										 <div class="alert alert-success successIlMessage" style="display:none;"></div>
									    </div> 
									</div>
						</div>
						
						
                     </div>
			     <div class='row form-group'>
		         <a href="<c:url value="/adt/package/customPackage/edit/${customPackageForm.packageId}"/>" class="btn back_btn btn-primary btn-sm"><spring:message code = "anvizent.package.link.back"/></a>
				 </div>
				 </form:form>
			  </div>
			  
			 <!-- Table Preview PopUp window -->
	        <div class="modal fade" tabindex="-1" role="dialog" style='overflow-y:auto;max-height:90%; 'id="viewDeatilsTablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
			      <div class="modal-dialog" style="width: 90%;">
			     <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			       <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		           <h4 class="modal-title custom-modal-title" id="viewDeatilsTablePreviewPopUpHeader"></h4>
			      </div>
			      <div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto;   overflow-x: auto;">
			      <table class='viewDeatilsTablePreview table table-striped table-bordered tablebg'></table>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default viewDetailscloseTablePreview"  data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
			      </div>
			    </div> 
			     </div> 
            </div>
            <div class="modal fade" tabindex="-1" role="dialog" style='overflow-y:auto;max-height:90%;' id="viewDeatilsFlatPreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
		      <div class="modal-dialog" style="width: 90%;">
		     <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		       <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		       <h4 class="modal-title custom-modal-title" id="viewDeatilsFlatPreviewPopUpHeader"></h4>
		      </div>
		      <div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto;   overflow-x: auto;">
		      <table class='viewDeatilsFlatPreview table table-striped table-bordered tablebg'></table>
		      </div>
		      <div class="modal-footer">
		        <button type="button" data-dismiss="modal"  class="btn btn-default viewDetailscloseFlatPreview" ><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div> 
		     </div> 
	           </div>
	           
	           
		<div class="modal fade" tabindex="-1" role="dialog" id="deleteTargetTableAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.editSource"/></h4>
		      </div>
		      <div class="modal-body">
		        <p><spring:message code="anvizent.package.message.deletePackage.areYouSureYouWantToEditSource" /> <!-- &hellip; --></p>
		        <div id="deleSourceFileAlertMessage" class="alert alert-danger">
		        	<b>Note: </b> <br />
		        	<spring:message code="anvizent.package.label.targetTableAlreadyCreatedForThisPackageIfyouEdittheSourceallmappingswillbeDestroyed" /> 
		        	
		        </div>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-primary" id="confirmDeleteTargetTable"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
		
		<div class="modal fade" tabindex="-1" role="dialog" id="changeColumnMappings" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.button.updateMapping"/></h4>
		      </div>
		      <div class="modal-body">
		      
		        <p><spring:message code="anvizent.package.message.structual.changes.custom.package.edit" /> <!-- &hellip; --></p>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-primary" id="confirmRedirectToQueryBuilder"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
	           
		</div>
  
  
  
  
  

 