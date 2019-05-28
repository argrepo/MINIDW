<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
        <div class="page-title-v1"><h4><spring:message code="anvizent.package.label.customPackage"/></h4></div>
        <div class="dummydiv"></div>
        <div class='row form-group'>
					<h4 class="alignText"><spring:message code="anvizent.package.label.derivedTable"/></h4>
		</div>
		<jsp:include page="_error.jsp"></jsp:include>
	 <div class="alert alert-danger message" style="display:none;">
	  	<p class="messageText"></p>
	</div>
	<div class="alert alert-success successMessage" style="display:none;">
		<p class="successMessageText"></p>
	</div>
	<div class="alert alert-success successDeleteSourceMessage" style="display:none;"></div>
	<div class='col-sm-10 '>
		<form:form modelAttribute="customPackageForm" method="POST" id="customPackageForm">
				 
				
				<form:hidden path="industryId"/> 
				<form:hidden path="isStandard"/>
				<form:hidden path="targetTableId"/>
				<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
				<c:if test="${not empty customPackageForm.packageId}">
					<div class="row form-group">
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
					<div class="row form-group">
					<div class='col-sm-12'>
						<div class="col-sm-2">
							<label class="radio-inline"><input type="radio" name="typeSelection" id="flatFiles"><spring:message code="anvizent.package.label.flatFile"/></label>
						</div>
						<div class="col-sm-2">
							<label class="radio-inline"><input type="radio" name="typeSelection" id="database"><spring:message code="anvizent.package.label.database"/></label>
						</div>
						<div class="col-sm-5">
							<label class="radio-inline"><input type="radio" name="typeSelection" id="derivedTables"> <spring:message code="anvizent.package.label.existingTablesFromCloudDerivedTables"/></label>
						</div>
					</div>
					</div>
				</c:if>
			<c:if test="${empty customPackageForm.packageId}">
				<div class="panel panel-default">
				  <div class="panel-heading">
			    <h3 class="panel-title"><spring:message code="anvizent.package.label.customPackage"/></h3>
			  </div>
                 <div class="panel-body"> 
                 <div class="row form-group">
							<label class="col-sm-3 control-label ">
							<spring:message code="anvizent.package.label.packageName"/></label>
							    <div class='col-sm-4' >
										<form:input path="packageName" class="form-control" />
										<form:errors path="packageName" cssStyle="color: #ff0000"/>
								</div>
			  		        <div class="col-md-4">
								<input type="submit" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.label.createPackage"/>" id="createCustomPackage" name='createCustomPackage'/>
								    
					  </div>
				  </div>
		 </div>
      </div>
    </c:if>
    </form:form> 
    </div>
    <div class='col-sm-10'>
     <c:if test="${not empty customPackageForm.packageId}">
       <div id="derivedTableDisplay"  style="display:none;">
					<div class="row form-group">
								<div class='col-sm-6 col-md-4' >
									<select   multiple="multiple" id="targettables" >
									<c:forEach var="tableDataResponse" items="${tableDataResponse}" varStatus="sno" > 
									<option value="<c:out value="${tableDataResponse}"/>"> <c:out value="${tableDataResponse}" /> </option>
									</c:forEach>
									</select>
								</div>
								<div class='col-sm-6 col-md-4'>
								<input type="submit" id="checkAllBtn" class="btn btn-primary btn-sm" style="margin-left: 15px;display:none" value="CheckAll"> 
                                <input type="submit" id="uncheckAllBtn" class="btn btn-primary btn-sm" style="margin-left: 170px;" value="UncheckAll"> 
								<a href="#" style='float:right' class="btn btn-primary btn-xs getSelectsBtn" id="saveDerivedTableMapping">
					            <span class="glyphicon glyphicon-plus" title="Add To Below Table"></span></a>
						       </div>
					</div>
     
          </div>
         </c:if>  
		
	 </div>
	 
	 <div class='col-sm-12' style="padding:0px 30px">
        <div class="modal fade" tabindex="-1" role="dialog" id="viewTargetTableStructurePopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog">
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewTargetTableHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
					        	<div class="table-responsive" >
									<table class="table table-striped table-bordered tablebg" id="targetTableStructure">
										<thead>
											<tr>
												<th><spring:message code="anvizent.package.label.sNo"/></th>
												<th><spring:message code="anvizent.package.label.columnName"/></th>
												<th><spring:message code="anvizent.package.label.dataType"/></th>
												<th><spring:message code="anvizent.package.label.columnSize"/></th>
												<th><spring:message code="anvizent.package.label.pk"/></th>
									            <th><spring:message code="anvizent.package.label.nn"/></th>
									            <th><spring:message code="anvizent.package.label.ai"/></th>
									            <th><spring:message code="anvizent.package.label.default"/></th>
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
																<option value="csv">csv</option>
																<option value="xls">xls</option>
																<option value="xlsx">xlsx</option>
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
														 <p class="help-block disclaimerNote"><em><spring:message code = "anvizent.package.label.notePleaseMakeSureFileIsHavingHeaders"/></em></p>
													     <p class="help-block"><em><spring:message code = "anvizent.package.label.noteDateTimeFormat"/><c:out value="${'<b>< yyyy-MM-dd HH:mm:ss >'}" /></em></p>
														</div>	
													</div>
													</form:form>
													<div class="row">
														<div class="col-sm-10 pull-right" id="flatfilemessage" style="display: none;"></div>
													</div>
												</div>
										</div>
										  <div class='row form-group'>
					                       <input type="button"  value='<spring:message code = "anvizent.package.button.saveUpload"/>' id='saveAndUpload_direct' class="btn btn-primary btn-sm"/>
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
														    <option value="<c:out value="${database.id}"/>" data-protocal="<c:out value="${database.protocal}"/>" data-urlformat="<c:out value="${database.urlFormat}"/>" data-connectorId = "<c:out value="${database.connector_id}"/>"><c:out value="${database.name}" /></option>
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
														</select>
													</div>
												</div>
												
												<div class='row form-group '>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.serverName"/>:
													</div>
													<div class='col-sm-8'>
														<input type="text"  class="form-control" id="IL_database_serverName"  data-minlength="1" data-maxlength="150">
														<p class="help-block"><span class='serverIpWithPort'></span></p>
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
														<input type="password"  class="form-control" id="IL_database_password"  data-minlength="1" data-maxlength="100">
													</div>
												</div>
												<div class='row form-group'>
										 			<div class='col-sm-4'>
																<spring:message code="anvizent.package.label.dateFormat" />( <spring:message code="anvizent.package.label.optional"/>)  ) :
													 </div>
													<div class="col-sm-8">
														<input type="text" class="form-control" id="dateFormat" placeholder="Ex: YYYY-MM-DD" data-minlength="1" data-maxlength="45">
												 	</div>
											 	 </div>
											 	 
											 	 <div class='row form-group'>
															<div class='col-sm-4'>
																<spring:message code="anvizent.package.label.timeZone" /> :
															</div>
															<div class='col-sm-8'>
																<select  class="form-control timesZone" id="timesZone" >
																	<c:forEach items="${timesZoneList}" var="timesZoneList">
																		<option value="<c:out value="${timesZoneList.key}"/>"><c:out value="${timesZoneList.value}"/></option>
																	</c:forEach>
																</select>
															</div>
											     </div>
												<div class='row form-group '>
													<div class='col-sm-3'>
														<input type="button" value='<spring:message code="anvizent.package.button.testConnection"/>' class="btn btn-primary btn-sm" id="testConnection"/>
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
													    <input type="button" value="Save" id="saveILConnectionMapping" class="btn btn-primary" style="display:none;">
													    <a class="btn btn-primary btn-sm buildQuery"  href=""><spring:message code="anvizent.package.button.buildQuery"/> <span class='glyphicon glyphicon-chevron-right' aria-hidden='true'></span></a>
													</div>
													</div>  
													<!-- Table Preview PopUp window for custom package-->
													<div class="modal fade" tabindex="-1" role="dialog"  id="tablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
												      <div class="modal-dialog" style="width: 90%;">
												     <div class="modal-content">
												      <div class="modal-header">
												        <button type="button" data-dismiss="modal" class="close"><span aria-hidden="true">&times;</span></button>
												        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        										<h4 class="modal-title custom-modal-title" id="tablePreviewPopUpHeader"></h4>
												      </div>
												      <div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto;   overflow-x: auto;">
												      <table class='tablePreview table table-striped table-bordered tablebg'></table>
												      </div>
												      <div class="modal-footer">
												        <button type="button" class="btn btn-default" data-dismiss="modal" ><spring:message code="anvizent.package.button.close"/></button>
												      </div>
												    </div> 
												     </div> 
						                           </div>
													<!-- delete IlConnection and IlConnectionMapping -->
													  <div class="modal fade" tabindex="-1" role="dialog" id="deleteIlConnection" data-backdrop="static" data-keyboard="false">
													  <div class="modal-dialog">
													    <div class="modal-content">
													      <div class="modal-header">
													        <button type="button" class="close" id="closeDeleteConnection" ><span aria-hidden="true">&times;</span></button>
													        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
													        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.button.deleteConnection"/></h4>
													      </div>
													      <div class="modal-body">
													        <p><spring:message code="anvizent.package.message.deletePackage.alltheMappingsWithTheseConnectionWillBeDeleted"/><br>
													        <spring:message code="anvizent.package.message.deletePackage.areYouSureYouWantToDeleteIlConnection"/><!-- &hellip; -->
													        </p>
													      </div>
													      <div class="modal-footer">
													        <button type="button" class="btn btn-primary" id="confirmDeleteIlConnection"><spring:message code="anvizent.package.button.yes"/></button>
													        <button type="button" class="btn btn-default" id="closeDeleteConnectionMapping" ><spring:message code="anvizent.package.button.no"/></button>
													      </div>
													    </div> 
													  </div> 
													</div>
												 
											</div>
						                  </div>
						                 
									</div>
									
					<c:if test="${not empty customPackageForm.packageId}">
					<c:choose>
											<c:when test="${(customPackageForm.isClientDbTables && not empty targetTableDirectMappingInfo) ||
													(!customPackageForm.isClientDbTables && not empty targetTableDirectMappingInfo)}" >
											<div class="panel panel-info" id="targetTablePanel" >
								            <div class="panel-heading"><b><spring:message code="anvizent.package.label.source"/></b></div>
								         	<div class="panel-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
														<div class='row form-group' id="targetTableSourceInfoDiv">
														<div class="col-sm-12">
															<div class="table-responsive" style="overflow-y:overlay;">
																<table class="table table-striped table-bordered tablebg" id="targetTableDirectMappingInfoTable">
																	<thead>
																		    <tr>
																			<th><spring:message code = "anvizent.package.label.sNo"/></th>
																			<th><spring:message code = "anvizent.package.label.type"/></th>
																			<th><spring:message code = "anvizent.package.label.fileName"/></th>
																			<th><spring:message code = "anvizent.package.label.view"/></th>
																			<th><spring:message code = "anvizent.package.label.delete"/></th>
																		</tr>
																	</thead>
																	<tbody>
																	<c:forEach items="${targetTableDirectMappingInfo}" var="clientData" varStatus="index">
																				<tr class='sourceTable'>
																					<td><c:out value="${index.index+1}" /></td>
																					<c:if test="${clientData.ilConnectionMapping.isFlatFile == true}">
																						<td><spring:message code = "anvizent.package.label.flatFile"/></td>
																						<td><c:out value="${clientData.ilConnectionMapping.filePath}" /></td>
																						<td class="smalltd">
																								<a class="btn btn-primary btn-sm startLoader"  href="<c:url value="/adt/package/viewCustomPackageSource/${customPackageForm.packageId}/${clientData.ilConnectionMapping.connectionMappingId}"/>" ><spring:message code="anvizent.package.label.viewDetails"/></a>  
																						</td>
																					</c:if>
																					<c:if test="${clientData.ilConnectionMapping.isFlatFile == false && clientData.ilConnectionMapping.isHavingParentTable == false}">
																						<td><spring:message code = "anvizent.package.label.database"/></td>
																						<td><spring:message code = "anvizent.package.label.nA"/></td>
																						<td  class="smalltd">
																							  <a class="btn btn-primary btn-sm startLoader"  href="<c:url value="/adt/package/viewCustomPackageSource/${customPackageForm.packageId}/${clientData.ilConnectionMapping.connectionMappingId}"/>" ><spring:message code="anvizent.package.label.viewDetails"/> </a>  	
																								
																						</td>
																					</c:if>
																						<c:if test="${clientData.ilConnectionMapping.isFlatFile == false && clientData.ilConnectionMapping.isHavingParentTable == true}">
																						<td><spring:message code = "anvizent.package.label.derivedTable"/></td>
																						<td class='parentTable' data-table="<c:out value="${clientData.ilConnectionMapping.parent_table_name}"/>"><c:out value="${clientData.ilConnectionMapping.parent_table_name}"/></td>
																						<td  class="smalltd">
																							   <input type="button" class="btn btn-primary btn-sm viewDerivedTableStructure"  value="<spring:message code="anvizent.package.button.viewTableStructure"/>"/>  
																								
																						</td>
																					</c:if>
																					<c:if test="${customPackageForm.isScheduled == false}">
																				               <td class="smalltd">
																							<button type="button" class="btn btn-primary btn-sm delete-mapping" data-id="<c:out value="${clientData.ilConnectionMapping.connectionMappingId}"/>">
																							<span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>
																						</td>
																					</c:if>
																				</tr>
																		</c:forEach>
																	</tbody>
																	</table>
																	</div>
																	</div>
														</div>
											</div>
											</div>
											 </c:when>
											 <c:when test="${(customPackageForm.isClientDbTables && empty targetTableDirectMappingInfo) || 
											 	(!customPackageForm.isClientDbTables && empty targetTableDirectMappingInfo)}" >
											 <div class="panel panel-info" id="targetTablePanel" style="display:none">
								             <div class="panel-heading"><b>Source</b></div>
								         	 <div class="panel-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
														<div class='row form-group' id="targetTableSourceInfoDiv">
														<div class="col-sm-12">
															<div class="table-responsive" style="overflow-y:overlay;">
																<table class="table table-striped table-bordered tablebg" id="targetTableDirectMappingInfoTable">
																	<thead>
																		    <tr>
																			<th><spring:message code = "anvizent.package.label.sNo"/></th>
																			<th><spring:message code = "anvizent.package.label.type"/></th>
																			<th><spring:message code = "anvizent.package.label.fileName"/></th>
																			<th><spring:message code = "anvizent.package.label.view"/></th>
																			<th><spring:message code = "anvizent.package.label.delete"/></th>
																		</tr>
																	</thead>
																	<tbody>
																	 </tbody>
																	</table>
																	</div>
																	</div>
														</div>
											</div>
											</div>
											 </c:when>
											 <c:when test="${!customPackageForm.isClientDbTables}" >
											 <div class="panel panel-info" id="targetTablePanel" style="display:none" >
								             <div class="panel-heading"><b><spring:message code = "anvizent.package.label.source"/></b></div>
								         	 <div class="panel-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
														<div class='row form-group' id="targetTableSourceInfoDiv">
														<div class="col-sm-12">
															<div class="table-responsive" style="overflow-y:overlay;">
																<table class="table table-striped table-bordered tablebg" id="targetTableDirectMappingInfoTable">
																	<thead>
																		    <tr>
																			<th><spring:message code = "anvizent.package.label.sNo"/></th>
																			<th><spring:message code = "anvizent.package.label.type"/></th>
																			<th><spring:message code = "anvizent.package.label.fileName"/></th>
																			<th><spring:message code = "anvizent.package.label.view"/></th>
																			<th><spring:message code = "anvizent.package.label.delete"/></th>
																			
																		</tr>
																	</thead>
																	<tbody>
																	 </tbody>
																	</table>
																	</div>
																	</div>
														</div>
											</div>
											</div>
											 </c:when>
											</c:choose>
											 </c:if>
													<div class="modal fade" tabindex="-1" role="dialog" id="deleteIlConnectionSource" data-backdrop="static" data-keyboard="false">
													  <div class="modal-dialog">
													    <div class="modal-content">
													      <div class="modal-header">
													        <button type="button" class="close" data-dismiss="modal"  ><span aria-hidden="true">&times;</span></button>
													        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
													        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.modalHeader.deleteSource"/></h4>
													      </div>
													      <div class="modal-body" >
													        <p><spring:message code="anvizent.package.message.deleteSource.areYouSureYouWantToDeleteSource"/><br>
													      </div>
													      <div class="modal-footer">
													        <button type="button" class="btn btn-primary" id="confirmDeleteIlConnectionSource"><spring:message code="anvizent.package.button.yes"/></button>
													        <button type="button" class="btn btn-default" data-dismiss="modal" ><spring:message code="anvizent.package.button.no"/></button>
													      </div>
													    </div> 
													  </div> 
													</div>
																	
					 
				</div>
				<div class='row form-group'>
				<div class="col-sm-4" style="padding:0px 30px">
				<c:if test="${not empty customPackageForm.packageId}">
                <a href="<c:url value="/adt/package/customPackage/edit/${customPackageForm.packageId}"/>" class="btn btn-primary btn-sm back_btn"><spring:message code = "anvizent.package.link.back"/></a>		
                <c:choose>
                <c:when test="${customPackageForm.isClientDbTables && empty targetTableDirectMappingInfo}" >
                <input type="button" class="btn btn-primary btn-sm mapping" value="Proceed For Mapping" id="proceedForMapping" style="display:none" >
                </c:when>
                <c:when test="${customPackageForm.isClientDbTables && not empty targetTableDirectMappingInfo}">
                <input type="button" class="btn btn-primary btn-sm mapping" value="Proceed For Mapping" id="proceedForMapping" >
                </c:when>
                <c:when test="${!customPackageForm.isClientDbTables}" >
                 <input type="button" class="btn btn-primary btn-sm mapping" value="Proceed For Mapping" id="proceedForMapping" style="display:none" >
                </c:when>
                </c:choose>
                </c:if>
               
               
         </div>
         </div>
				 
</div>
    