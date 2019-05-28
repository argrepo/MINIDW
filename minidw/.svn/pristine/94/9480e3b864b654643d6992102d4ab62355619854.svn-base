<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv" >
    <div class="col-md-10">
    <div class="dummydiv"></div>
		<div class="col-sm-10">
		    <h4><spring:message code = "anvizent.package.label.ilSourceDetails"/></h4>
		</div>
		<jsp:include page="_error.jsp"></jsp:include>
		 
   </div>
   
  <div class="col-xs-12">

	<form:form modelAttribute="standardPackageForm" method="POST" id="standardPackageForm">
		<input type="hidden" id="userID" value="${principal.userId}">
		<form:hidden path="industryId"/>
		<form:hidden path="packageId"/>
		<form:hidden path="dLId"/>
		<form:hidden path="iLId"/>
		<input type="hidden" id="mappingId">
		<c:set var="packageId_var" value="${standardPackageForm.packageId}"/>
		<jsp:include page="_error.jsp"></jsp:include>
		 <div class='row form-group'>
			<div class='col-sm-4 col-lg-3' >
			    <label class="control-label "><spring:message code = "anvizent.package.label.ilName"/></label>
				<form:input path="iLName" class="form-control" disabled="true"/>
			</div>
			<div class='col-sm-4 col-lg-3' >
			<label class="control-label">&nbsp;</label>
			 <div>
			<a href="${referer}" class="btn btn-primary btn-sm"><spring:message code = "anvizent.package.link.back"/></a>
			<input type="button" class="btn btn-primary btn-sm" value="Add Source" id="addSource" name='addSource' />
			 </div>
			</div>
			
		</div>
     <div class="alert alert-danger Ilmessage" style="display:none;">
	  </div>
	<div class="alert alert-success successIlMessage" style="display:none;">
	</div>
	</form:form>  
	
	  <div id="viewIlSourceDetails">
	  <c:choose>
	  <c:when test="${not empty iLConnectionMapping}">
	  <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
	  
	  	<table class="table table-striped table-bordered tablebg" id="customPackageTable">
			<thead>
				<tr>
					<th><spring:message code = "anvizent.package.label.name"/></th>
					<th><spring:message code = "anvizent.package.label.mapping"/></th>
				</tr>
			</thead> 
		 <tbody>
		  	<c:forEach items="${iLConnectionMapping}" var="iLConnectionMapping" varStatus="increment">
		  		<tr>
		  			<td>${iLConnectionMapping.isFlatFile ? iLConnectionMapping.filePath : iLConnectionMapping.iLConnection.connectionName}</td>
		  			<td><button type="button" style="float:right;margin-top:-6px;" id="click" data-dlid="${iLConnectionMapping.dLId}" 
			      data-ilid="${iLConnectionMapping.iLId}" 
			      data-mappingCompleted="${iLConnectionMapping.xReferenceDetails.mappingCompleted}"
			      data-mergingCompleted="${iLConnectionMapping.xReferenceDetails.mergingCompleted}"
			      data-constraintsCompleted="${iLConnectionMapping.xReferenceDetails.constraintsCompleted}"
			      data-sequenceCompleted="${iLConnectionMapping.xReferenceDetails.sequenceCompleted}"
			      data-xRefId="${iLConnectionMapping.xReferenceDetails.xRefId}"
			      data-mappingid="${iLConnectionMapping.connectionMappingId}" class="btn btn-primary btn-sm  xRefMappingButton"><spring:message code = "anvizent.package.label.mapping"/></button></td>
		  		</tr>	
		  	</c:forEach>
		  </tbody>
	  	
	  	</table>
		
      </div>
      
      <div class="xReferenceDetailsDiv" style="border: 1px solid #000;padding: 10px;">
      <div class="pull-right"> <button type="button" class="btn btn-sm btn-danger" id="closexReferenceDetailsBtn" ><spring:message code = "anvizent.package.button.x"/></button></div>
      	<div>
	      	<button type="button" class="btn btn-primary btn-sm xrefButton mappingBtn hidden" data-divId="mappingDiv-Xref"><spring:message code = "anvizent.package.label.mapping"/></button>
	      	<button type="button" class="btn btn-primary btn-sm xrefButton mergingBtn hidden" data-divId="mergingDiv-Xref"><spring:message code = "anvizent.package.label.merging"/></button>
	      	<button type="button" class="btn btn-primary btn-sm xrefButton constraintBtn hidden" data-divId="constraintDiv-Xref"><spring:message code = "anvizent.package.label.constraints"/></button>
	      	<button type="button" class="btn btn-primary btn-sm xrefButton sequenceBtn hidden" data-divId="sequenceDiv-Xref"><spring:message code = "anvizent.package.button.sequence"/></button>
      	</div>
      
      <div class="clearfix" style="height:20px;"></div>
      
	      <div id="mappingDiv-Xref">
	      	<div class="table-responsive" style="max-height: 400px;">
	      			<input type="hidden" id="ilMappingId">
	      			<input type="hidden" id="xRefId">
					<table class="table table-striped table-bordered tablebg" id="fileMappingWithILTable">
						<thead>
							<tr>
								<th><spring:message code = "anvizent.package.label.sNo"/></th>
								<th class="iLName"></th>
								<th class=""><spring:message code = "anvizent.package.label.dataType"/></th>
								<th><spring:message code="anvizent.package.label.pk"/></th>
							    <th><spring:message code="anvizent.package.label.nn"/></th>
							    <th><spring:message code="anvizent.package.label.ai"/></th>
								<th class="originalFileName"></th>
								<th><spring:message code = "anvizent.package.label.default"/></th>
							</tr>
						</thead>
						 <tbody>
							
						</tbody> 
					</table>

				</div> 
				<div>
		         	<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.saveMapping"/>" id="saveMappingWithIL" name='saveMappingWithIL'/>
		      	</div>
	      </div>
	      <div id="mergingDiv-Xref">
	      
	      	<div>
		      	<button type="button" class="btn btn-primary xRefMergeButtons btn-sm" data-divId="columnsDiv-Xref"><spring:message code = "anvizent.package.label.xref.xrefbycondition"/></button>
		      	<button type="button" class="btn btn-primary xRefMergeButtons btn-sm" data-divId="valuesDiv-Xref"><spring:message code = "anvizent.package.label.xref.xrefbyvalues"/></button>
      		</div>
	      <div id="columnsDiv-Xref">
	      <div class="row hidden">
			<div class="col-sm-12">
				<div class="pull-right">
					<button class="btn btn-success addConditionGroupBtn" data-divId="columnsDiv-Xref"><spring:message code = "anvizent.package.button.addGroup"/></button>
				</div>
			</div>
		  </div>
		  <div style="height:10px;"></div>
	      <div class="row groupBoxTemplate hidden">
		    <div class="col-xs-12">
			<div class="pull-right addColumnBox">
				<button class="btn btn-success addConditionBtn" ><spring:message code = "anvizent.package.button.add"/></button>
				<button class="btn btn-danger removeGroupBtn hidden"><spring:message code = "anvizent.package.button.removeGroup"/></button>
			</div>
				<table class="table table-striped table-bordered tablebg tblForMergingConditions">
					<thead>
						<tr>
							<th class="iLName"></th>
							<th class="originalFileName"></th>
							<th>
								<spring:message code = "anvizent.package.label.delete"/>
							</th>
						</tr>
					</thead>
					<tbody>
						<tr class="hidden">
							<td>
								<select class="form-control selectIlColumnNameforMerging">
									
								</select>
							</td>
							<td>
								<select class="form-control selectSourceColumnNameforMerging">
									
								</select>
							</td>
							<td>
								<button type="button" class="btn btn-sm btn-danger removeConditionBtn hidden">
								<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
								</button>
							</td>
						</tr>
					</tbody>
					<tfoot style="background-color:#fff;">
						<tr>
							<td>
								<spring:message code = "anvizent.package.label.conditionName"/>
							</td>
							<td>
								<input type="text" class="mergingConditionName form-control">
							</td>
							<td>
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		  </div>
		  </div>
	      
	      <div id="valuesDiv-Xref">
	      <div class="row hidden">
			<div class="col-sm-12">
				<div class="pull-right">
					<button class="btn btn-success addConditionGroupBtn" data-divId="valuesDiv-Xref"><spring:message code = "anvizent.package.button.addGroup"/></button>
				</div>
			</div>
		  </div>
		  <div style="height:10px;"></div>
	      <div class="row groupBoxTemplate hidden">
		    <div class="col-xs-12">
			<div class="pull-right addColumnBox">
				<button class="btn btn-success addConditionBtn" ><spring:message code = "anvizent.package.button.add"/></button>
				<button class="btn btn-danger removeGroupBtn hidden" data-divId="valuesDiv-Xref"><spring:message code = "anvizent.package.button.removeGroup"/></button>
			</div>
				<table class="table table-striped table-bordered tablebg tblForMergingConditions">
					<thead>
						<tr>
							<th class="iLName"></th>
							<th><spring:message code = "anvizent.package.label.columnValues"/></th>
							<th class="originalFileName"></th>
							<th><spring:message code = "anvizent.package.label.columnValues"/></th>
							<th>
								<spring:message code = "anvizent.package.label.delete"/>
							</th>
						</tr>
					</thead>
					<tbody>
						<tr class="hidden">
							<td>
								<select class="form-control selectIlColumnNameforMerging" data-colType="il" data-targetSelectBox=".selectedIlColumnValue">
									
								</select>
							</td>
							<td>
								<select class="form-control selectedIlColumnValue">
									
								</select>
							</td>
							<td>
								<select class="form-control selectSourceColumnNameforMerging" data-colType="source"  data-targetSelectBox=".selectedSourceColumnValue">
									
								</select>
							</td>
							<td>
								<select class="form-control selectedSourceColumnValue">
									
								</select>
							</td>
							<td>
								<button type="button" class="btn btn-sm btn-danger removeConditionBtn hidden">
								<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
								</button>
							</td>
						</tr>
					</tbody>
					<tfoot style="background-color:#fff;">
						<tr>
							<td>
								<spring:message code = "anvizent.package.label.conditionName"/>
							</td>
							<td>
								<input type="text" class="mergingConditionName form-control">
							</td>
							<td colspan="3">
							</td>
						</tr>
					</tfoot>
				</table>
			</div>
		  </div>
		  </div>
	      
	      <div class="processMergingBlock">
	      	 <div>
		      	<input type="button" class="btn btn-primary btn-sm" value="Proceed" id="addMergingConstraint" name='addMergingConstraint'/>
		     </div>
	      </div>
	      	
	      </div>
	      <div id="constraintDiv-Xref">
	      <spring:message code = "anvizent.package.label.constraint"/>
	      </div>
	      <div id="sequenceDiv-Xref">
	      <spring:message code = "anvizent.package.label.sequence"/>
	      </div>
      </div>
      
	  </c:when>
	  <c:otherwise>
	   <div class="alert alert-danger Ilmessage"  ><spring:message code = "anvizent.package.label.noSourceFileIsAdded"/>
	  </div>
	  </c:otherwise>
	  </c:choose>
       
 
          </div>    
          
          <div class="modal fade" tabindex="-1" role="dialog" id="addSourcePopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog">
					    <div class="modal-content" style="width:700px">
					      <div class="modal-header">
					        <button type="button" class="close"  id='targetTablePopUpClose' ><span aria-hidden="true">&times;</span></button>
					       	<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="targetTableDirectPopUpHeader"><spring:message code = "anvizent.package.label.addSource"/></h4>
					      </div>
					      <div class="modal-body">
									<div class="alert alert-danger message" style="display:none;">
												 <p class="messageText"></p>
									</div>
									<div class="alert alert-success successMessage" style="display:none;">
												 <p class="successMessageText"></p>
									</div>
									<div class='row form-group' id="mappingTypeSelection">										 
										<div class='col-sm-2'>
											<label class="radio-inline"><input type="radio" name="typeSelection" id = 'flatFiles'><spring:message code="anvizent.package.label.flatFile"/></label>
										</div>
									 	<c:choose>
									 		<c:when test="${principal.isTrailUser == true}">
									 			<div class='col-sm-2'>
												</div>
									 		</c:when>
									 		<c:otherwise>
									 			<div class='col-sm-2'>
												</div>
									 		</c:otherwise>
									 	</c:choose>
									</div>
									<div id="flatFilesLocationDetails" style="display:none;">
						                   <div class="panel panel-info">
											<div class="panel-heading"><spring:message code="anvizent.package.label.flatFileDetails"/></div>
												<div class="panel-body">
												<form:form method="POST"  id="fileUploadForm_direct" enctype="multipart/form-data">
													<div class='row form-group '>
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.fileType"/>:
														</div>
														<div class='col-sm-8'>
															<select class="form-control" id="flatFileType_direct" name = "flatFileType">
																<option value="csv"><spring:message code="anvizent.package.label.csv"/></option>
																<option value="xls"><spring:message code="anvizent.package.label.xls"/></option>
																<option value="xlsx"><spring:message code="anvizent.package.label.xlsx"/></option>
															</select>
														</div>
													</div>
													<div class='row form-group delimeter-block'>
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.delimiter"/>:
														</div>
														<div class='col-sm-8' >
															<input type="text" class="form-control" id="delimeter_direct" name="delimeter" value="," readonly="readonly">
														</div>
													</div>
													<div class='row form-group' style="display: none;">
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.firstRowHasColumnNames"/>:
														</div>
														<div class='col-sm-8'>
																<div class='col-sm-3'>
																	<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="true" checked="checked"><spring:message code="anvizent.package.label.yes"/></label>
																</div>
																<div class='col-sm-3' id="firstrowcolsvalidation">
																	<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="false"><spring:message code="anvizent.package.label.no"/></label>
																</div>
														</div>
													</div>
													<input type="hidden"  id="packageIdForFileUpload_direct" name="packageId"/>
													<input type="hidden"  id="userIdForFileUpload_direct" name="userId"/>
													<div class='row form-group '>
														
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.filePath"/>:
														</div>
														<div class='col-sm-8' >
															<input  type="file" name="file" id="file_direct"> 
														</div>
														<div class='col-sm-4'>
														</div>
														<div class='col-sm-8' >
														 <p class="help-block disclaimerNote"><em><spring:message code ="anvizent.package.label.notePleaseMakeSureFileIsHavingHeaders"/></em></p>
													     <p class="help-block"><em><spring:message code = "anvizent.package.label.noteDateTimeFormat"/> < yyyy-MM-dd HH:mm:ss ></em></p>
														</div>	
													</div>
													</form:form>
													<div class="row">
														<div class="col-sm-10 pull-right" id="flatfilemessage" style="display: none;"></div>
													</div>
												</div>
										</div>
									</div>
									<div id='databaseConnectionDetails' style="display:none;">
										<div class='row form-group '>
												<div class='col-sm-3'>
													<spring:message code="anvizent.package.label.existingConnections"/>
												</div>
												<div class='col-sm-3'>
													<select class="form-control" id="existingConnections">
													</select>
												</div>
												<div class="col-sm-6">
													    <button type="button" class="btn btn-primary btn-sm" id="createNewConnection_dataBaseType" name='createNewConnection_dataBaseType'><spring:message code="anvizent.package.button.createNewConnection"/></button>
										  		        <button type="button" class="btn btn-primary btn-sm" id="deleteDatabaseTypeConnection" style="display:none"   name='deleteDatabaseTypeConnection'><spring:message code="anvizent.package.button.deleteConnection"/></button>
										  		</div>
										  			 
											</div>
										<div class="panel panel-info" id="databaseConnectionPanel" style="display:none;">
											<div class="panel-heading"><spring:message code="anvizent.package.label.databaseConnectionDetails"/></div>
											<div class="panel-body">
												<div class='row form-group '>
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.connectionName"/>:
														</div>
														<div class='col-sm-8'>
															<input type="text" id="IL_database_connectionName" class="form-control"  data-minlength="1" data-maxlength="45">
														</div>
													</div>
												<div class='row form-group '>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.connectorType"/>:
													</div>
													<div class='col-sm-8'>
														<select  class="form-control" id="IL_database_databaseType">
															<c:forEach items="${databseList}" var="database">
																<option value="${database.connector_id}">${database.name}</option>
															</c:forEach>
														</select>
													</div>
												</div>
												<div class='row form-group '>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.connectionType"/>:
													</div>
													<div class='col-sm-8'>
														<select  class="form-control" id="IL_database_connectionType">
															<option value="Direct"><spring:message code="anvizent.package.label.direct"/></option>
															<option value="Tunnel"><spring:message code="anvizent.package.label.tunnel"/></option>
														</select>
													</div>
												</div>
												
												<div class='row form-group '>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.serverName"/>:
													</div>
													<div class='col-sm-8'>
														<input type="text"  class="form-control" id="IL_database_serverName"  data-minlength="1" data-maxlength="150">
														<p class="help-block"><em class='serverIpWithPort'></em></p>
													</div>
												</div>
												<div class='row form-group '>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.userName"/>:
													</div>
													<div class='col-sm-8'>
														<input type="text"  class="form-control" id="IL_database_username"  data-minlength="1" data-maxlength="45">
													</div>
												</div>
												<div class='row form-group ' id="IL_database_password_div">
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.password"/>:
													</div>
													<div class='col-sm-8' >
														<input type="password"  class="form-control" id="IL_database_password" data-minlength="1" data-maxlength="100">
													</div>
												</div>
												<div class='row form-group '>
													<div class='col-sm-3'>
														<input type="button" value='Test Connection' class="btn btn-primary btn-sm" id="testConnection"/>
													</div>
													<div class='col-sm-3'>
														<input type="button" id="saveNewConnection_dataBaseType" value='<spring:message code="anvizent.package.button.saveConnection"/>' class="btn btn-primary btn-sm"/>
													</div>
												</div>
												<div class='row form-group IL_queryCommand'>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.typeOfCommand"/>:
													</div>
													
													<div class='col-sm-8'>
														<select class="form-control" id="typeOfCommand">
															<option value="Query"><spring:message code="anvizent.package.label.query"/></option>
															<option value="Stored Procedure"><spring:message code="anvizent.package.label.storedProcedure"/></option>
														</select>
													</div>
												</div>
												
												<div class='row form-group IL_queryCommand'>
													<div class='col-sm-12'>
														<textarea class="form-control" rows="6" id="queryScript" placeholder="<spring:message code="anvizent.package.label.query"/>"></textarea>
													</div>
												</div>
												<div class='row form-group IL_queryCommand' style="margin-top: -15px">
													<div class='col-sm-12'>
														<input class="form-control" id="procedureName" style="display:none;" placeholder="<spring:message code="anvizent.package.label.storedProcedureName"/>">
													</div>
												</div>
												<div class='col-sm-12 queryValidatemessageDiv'>
														
													</div>
												<div class="row">
														<div class="col-sm-10 pull-right" id="databasemessage" style="display: none;"></div>
													</div>
												<div class='row form-group'>
													<div class='col-sm-6'>
														<input type="button" value='Validate Query/SP' id='checkQuerySyntax' class="btn btn-primary btn-sm"/>
														<input type="button" value='Preview' id='checkTablePreview' class="btn btn-primary btn-sm" data-target='#tablePreviewPopUp'/>
													</div>
													<!-- Table Preview PopUp window for custom package-->
													
												</div>
											</div>
						                  </div>
									</div>
					      </div>
					      <div class="modal-footer">
					        <input type="button" value='<spring:message code="anvizent.package.button.save"/>' id='saveILConnectionMapping' class="btn btn-primary" style="display: none;"/>
					        <input type="button"  value='Save & Upload' id='saveAndUpload_direct' class="btn btn-primary btn-sm" style="display: none;"/>
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div><!-- /.modal-content -->
					  </div><!-- /.modal-dialog -->
				</div>
				
                           
	<div class="modal fade" tabindex="-1" role="dialog" id="deleStandardSourceFileAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.modalHeader.deleteSource"/></h4>
		      </div>
		      <div class="modal-body">
		        <p><spring:message code="anvizent.package.message.deleteSource.areYouSureYouWantToDeleteSource"/><!-- &hellip; --></p>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-primary" id="confirmDeleteStandardSource"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" style='overflow-y:auto;max-height:90%; 'id="viewDeatilsPreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
			      <div class="modal-dialog" style="width: 90%;">
			     <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title" id="viewDeatilsPreviewPopUpHeader"></h4>
			      </div>
			      <div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto;   overflow-x: auto;">
			      <table class='viewDeatilsPreview table table-striped table-bordered tablebg'></table>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default viewDetailsclosePreview" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
			      </div>
			    </div> 
			     </div> 
    </div>	 
    <!-- Table Preview PopUp window -->
	<div class="modal fade" tabindex="-1" role="dialog" id="tablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
      <div class="modal-dialog" style="width: 90%;">
     <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		<h4 class="modal-title custom-modal-title" id="tablePreviewPopUpHeader"></h4>
      </div>
      <div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto;   overflow-x: auto;">
      <table class='tablePreview table table-striped table-bordered tablebg'></table>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
      </div>
    </div> 
  </div> 
   </div>
   
<div class="modal fade" tabindex="-1" role="dialog" style='overflow-y:auto;max-height:90%; margin-top:200px;' id="tablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
   <div class="modal-dialog" style="width: 90%;">
  <div class="modal-content">
   <div class="modal-header">
     <button type="button" id="closePreview" data-dismiss="modal" aria-label="Close" class="close"><span aria-hidden="true">&times;</span></button>
     <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					<h4 class="modal-title custom-modal-title" id="tablePreviewPopUpHeader"></h4>
   </div>
   <div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto;   overflow-x: auto;">
   <table class='tablePreview table table-striped table-bordered tablebg'></table>
   </div>
   <div class="modal-footer">
     <button type="button" class="btn btn-default closeTablePreview" data-dismiss="modal" aria-label="Close"><spring:message code="anvizent.package.button.close"/></button>
   </div>
   </div> 
  </div> 
</div>
													
   
   </div>
   		
   </div>
 