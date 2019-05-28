<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
        <div class="page-title-v1"><h4><spring:message code = "anvizent.package.label.addilSource"/></h4></div>
        <div class="dummydiv"></div>
         <ol class="breadcrumb"> </ol>
<div class='col-sm-12'>
<form:form modelAttribute="standardPackageForm" method="POST" id="standardPackageForm">
		
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
		<form:hidden path="industryId"/>
		<form:hidden path="packageId"/>
		<form:hidden path="dLId"/>
		<c:set var="packageId_var" value="<c:out value="${standardPackageForm.packageId}"/>"/>
		<input type="hidden" id="packageId" value="<c:out value="${standardPackageForm.packageId}"/>">
		<input type="hidden" id="dlId" value="<c:out value="${standardPackageForm.dLId}"/>">
		<input type="hidden" id="ilId" value="<c:out value="${defaultILId}"/>">
		<div class='row form-group'>
			<h4><spring:message code = "anvizent.package.label.addilSource"/></h4>
		</div>
		<jsp:include page="_error.jsp"></jsp:include>
		 <div class='row form-group'>
			<div class='col-sm-4 col-lg-2' >
			    <label class="control-label "><spring:message code = "anvizent.package.label.packageName"/></label>
				<form:input path="packageName" class="form-control" disabled="true"/>
			</div>
			<div class='col-sm-4 col-lg-3' >
			    <label class="control-label "><spring:message code = "anvizent.package.label.dlName"/></label>
				<form:input path="dLName" class="form-control" disabled="true"/>
			</div>
			<div class='col-sm-4 col-lg-3' >
			    <label class="control-label "><spring:message code = "anvizent.package.label.ilName"/></label>
				<form:select path="iLName" class="form-control">
				    <c:forEach items="${iLList}" var="iLInfo">
					    <form:option value="<c:out value="${iLInfo.iL_id}"/>" ><c:out value="${iLInfo.iL_name}"/></form:option>
					</c:forEach>
				</form:select>
			</div>
				<br>   
				  <div class='form-group'>
					<div class="col-sm-12 col-lg-4">
					 
					 <a class="btn btn-primary btn-sm tablebuttons"  href="<c:url value="/adv/package/xRefDetails/${standardPackageForm.packageId}/${standardPackageForm.dLId}/${defaultILId}"/>" ><input type="button" class="btn btn-primary btn-sm" value="<spring:message code = "anvizent.package.button.xrefDetails"/>"/> </a>
					  <input type="button" class="btn btn-primary btn-sm viewILTableStructure" value="<spring:message code = "anvizent.package.button.viewTableStructure"/>" id="viewILTableStructure"/>
				  	</div> 
				  </div>
		</div>
		
</form:form>      
</div>
<div class="col-sm-12">
				<div class="alert alert-danger message" style="display:none;">
					 <p class="messageText"></p>
				</div>
				<div class="alert alert-success successMessage" style="display:none;">
												 <p class="successMessageText"></p>
				</div>
				<div id='databaseSettings'>
					<div class='row form-group'>
						<div class='col-sm-2'>
							<label class="radio-inline"><input type="radio" name="typeSelection" id = 'flatFiles'><spring:message code="anvizent.package.label.flatFile"/></label>
						</div>
						<div class='col-sm-2'>
							<label class="radio-inline"><input type="radio" name="typeSelection" id='database'><spring:message code="anvizent.package.label.database"/></label>
						</div>
					</div>
					
					<div class='' >
					  <div id='databaseConnectionDetails' style="display:none;">
						<div class='row form-group '>
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.existingConnections"/>
								</div>
								<div class='col-sm-3'>
									<select class="form-control" id="existingConnections" name="existingConnections">
									</select>
								</div>
								<div class="col-md-6">
								<button type="button" class="btn btn-primary btn-sm" id="createNewConnection_dataBaseType" name='createNewConnection_dataBaseType'><spring:message code="anvizent.package.button.createNewConnection"/></button>
						  		<button type="button" class="btn btn-primary btn-sm" id="deleteDatabaseTypeConnection" style="display:none" name='deleteDatabaseTypeConnection'><spring:message code="anvizent.package.button.deleteConnection"/></button>
						  		</div>
						  		 
							</div>
						<div class="panel panel-info" id="databaseConnectionPanel" style="display:none;">
							<div class="panel-heading"><spring:message code="anvizent.package.label.databaseConnectionDetails"/></div>
							<div class="panel-body">
								<div class='row form-group '>
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.connectionName"/>:
										</div>
										<div class='col-sm-6'>
											<input type="text" id="IL_database_connectionName" class="form-control" data-minlength="1" data-maxlength="45">
										</div>
									</div>
								<div class='row form-group '>
									<div class='col-sm-4'>
										<spring:message code="anvizent.package.label.connectorType"/>:
									</div>
									<div class='col-sm-6'>
										<select  class="form-control" id="IL_database_databaseType">
											<c:forEach items="${databseList}" var="database">
												<option value="<c:out value="${database.connector_id}"/>"><c:out value="${database.name}"/></option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class='row form-group '>
									<div class='col-sm-4'>
										<spring:message code="anvizent.package.label.connectionType"/>:
									</div>
									<div class='col-sm-6'>
										<select  class="form-control" id="IL_database_connectionType">
											<option value="<spring:message code = "anvizent.package.label.direct"/>"><spring:message code = "anvizent.package.label.direct"/></option>
											<option value="<spring:message code = "anvizent.package.label.tunnel"/>"><spring:message code = "anvizent.package.label.tunnel"/></option>
										</select>
									</div>
								</div>
								
								<div class='row form-group '>
									<div class='col-sm-4'>
										<spring:message code="anvizent.package.label.serverName"/>:
									</div>
									<div class='col-sm-6'>
										<input type="text"  class="form-control" id="IL_database_serverName" data-minlength="1" data-maxlength="150">
										<p class="help-block"><em class='serverIpWithPort'></em></p>
									</div>
								</div>
								<div class='row form-group '>
									<div class='col-sm-4'>
										<spring:message code="anvizent.package.label.userName"/>:
									</div>
									<div class='col-sm-6'>
										<input type="text"  class="form-control" id="IL_database_username" data-minlength="1" data-maxlength="45">
									</div>
								</div>
								<div class='row form-group ' id="IL_database_password_div">
									<div class='col-sm-4'>
										<spring:message code="anvizent.package.label.password"/>:
									</div>
									<div class='col-sm-6' >
										<input type="password"  class="form-control" id="IL_database_password" data-minlength="1" data-maxlength="100">
									</div>
								</div>
								<div class='row form-group '>
									<div class='col-sm-3'>
										<input type="button" value='<spring:message code = "anvizent.package.labelTestConnection"/>' class="btn btn-primary btn-sm" id="testConnection"/>
									</div>
									<div class='col-sm-3'>
										<input type="button" id="saveNewConnection_dataBaseType" value='<spring:message code="anvizent.package.button.saveConnection"/>' class="btn btn-primary btn-sm"/>
									</div>
								</div>
								<div class='row form-group IL_queryCommand'>
									<div class='col-sm-4'>
										<spring:message code="anvizent.package.label.typeOfCommand"/>:
									</div>
									
									<div class='col-sm-6'>
										<select class="form-control" id="typeOfCommand">
											<option value="Query"><spring:message code="anvizent.package.label.query"/></option>
											<option value="Stored Procedure"><spring:message code="anvizent.package.label.storedProcedure"/></option>
										</select>
									</div>
								</div>
								<div class='row form-group IL_queryCommand'>
									<div class='col-sm-10  s-script'>
										<textarea class="form-control" rows="6" id="queryScript" placeholder="<spring:message code="anvizent.package.label.query"/>"></textarea>
									</div>
									<div class="col-sm-10 s-script" style="display:none;">
										<div class="row">
											
												<input class="form-control" id="procedureName" placeholder="<spring:message code="anvizent.package.label.storedProcedure"/>">
											
											<div class="col-sm-3 hide">
												<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.addParameters"/>" id="addparameters">
											</div>
										</div>
									</div>
									
								</div>
								<div class="row form-group param-hidden" style="display:none;">
									<div class="col-sm-2">
										<spring:message code="anvizent.package.label.name"/>
									</div>
									<div class="col-sm-4">
										<input type="text" class="s-param-name form-control"> 
									</div>
									<div class="col-sm-2">
										<spring:message code="anvizent.package.label.value"/>
									</div>
									<div class="col-sm-4">
										<input type="text" class="s-param-value form-control"> 
									</div>
								</div>
								<div class='col-sm-offset-1 col-sm-8 queryValidatemessageDiv'>
										
									</div>
								<div class='row form-group'>
									<div class='col-sm-4'>
										<input type="button" value='<spring:message code="anvizent.package.button.validateQuerySP"/>' id='checkQuerySyntax' class="btn btn-primary btn-sm"/>
									    <input type="button" value='<spring:message code="anvizent.package.button.preview"/>' id='checkTablePreview' class="btn btn-primary btn-sm" data-target='#tablePreviewPopUp'/>
									</div>
									<!-- Table Preview PopUp window -->
									<div class="modal fade" tabindex="-1" role="dialog" id="tablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
								      <div class="modal-dialog" style="width: 90%;">
								     <div class="modal-content">
								      <div class="modal-header">
								        <button type="button" class="close" data-dismiss="#tablePreviewPopUp" aria-label="Close"><span aria-hidden="true">&times;</span></button>
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
									
								</div>
								
							</div>
							<div class="panel panel-default" id="IL_queryCommand" style="display:none;">
							  <div class="panel-body">
								
								
							  </div>
							</div>
	                        </div>
						</div>
					<div id="flatFilesLocationDetails" style="display:none;">
	                    <div class="panel panel-info" id='flatFilesLocationPanel' style="display:none;">
							<div class="panel-heading"><spring:message code="anvizent.package.label.flatFileDetails"/></div>
								<div class="panel-body">
								<form:form method="POST"  id="fileUploadForm" enctype="multipart/form-data">
									<input type="hidden"  id="packageIdForFileUpload" name="packageId"/>
									<input type="hidden"  id="userIdForFileUpload" name="userId"/>
									<input type="hidden"  id="dLIdFileUpload" name="dL_Id"/>
									<input type="hidden"  id="iLIdForFileUpload" name="iL_Id"/>
									<input type="hidden"  id="industryIdForFileUpload" name="industryId"/>
									<div class='row form-group '>
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.fileType"/>:
										</div>
										<div class='col-sm-6'>
											<select class="form-control" id="flatFileType" name="flatFileType">
												<option value="csv"><spring:message code="anvizent.package.label.csv"/></option>
											</select>
										</div>
									</div>
									
									<div class='row form-group'>
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.delimiter"/>:
										</div>
										<div class='col-sm-6' >
											<input type="text" class="form-control" id="delimeter" value="," name="delimeter">
										</div>
									</div>
									<div class='row form-group' style="display: none;">
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.firstRowHasColumnNames"/>:
										</div>
										<div class='col-sm-6'>
											<div class='col-sm-2'>
												<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="true" checked="checked"><spring:message code="anvizent.package.label.yes"/></label>
											</div>
											<div class='col-sm-2' id="firstrowcolsvalidation">
												<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="false"><spring:message code="anvizent.package.label.no"/></label>
											</div>
										</div>
									</div>
									<div class='row form-group '>
										
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.file"/>:
											
										</div>
										<div class='col-sm-6' >
											<input  type="file" name="file" id="fileUpload"> 
											</div>
											<div class='col-sm-4'>
										    </div>
										    <div class='col-sm-8' >
										    <p class="help-block disclaimerNote"><em><spring:message code = "anvizent.package.label.notePleaseMakeSureFileIsHavingHeaders"/></em></p>
                                            <p class="help-block"><em><spring:message code = "anvizent.package.label.noteDateTimeFormat"/>< yyyy-MM-dd HH:mm:ss ></em></p>
											</div>
									</div>
									</form:form>
								</div>
						</div>
						</div>
						</div>
						</div>
		<div class="row form-group">
				 <div class="col-md-4">
				 <input type="button" value='<spring:message code="anvizent.package.button.save"/>' id='saveILConnectionMapping' class="btn btn-primary btn-sm" style="display: none;"/>
				<input type="button"  value='<spring:message code = "anvizent.package.button.upload"/>' id='saveAndUpload' class="btn btn-primary btn-sm" style="display: none;"/>
			       <a href='<c:url value="/adv/package/standardPackage/edit/${packageId_var}"/>' class="btn btn-primary btn-sm"><spring:message code = "anvizent.package.link.back"/></a> 
		  </div>
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" id="viewSchema" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>		       
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title" id="viewSchemaHeader"></h4>
		        
		      </div>
		      <div class="modal-body" style='max-height: 500px; overflow-y: auto;   overflow-x: auto;'>
		        	<div class="table-responsive" >
						<table class="table table-striped table-bordered tablebg" id="viewSchemaTable">
							<thead>
								<tr>
									<th><spring:message code="anvizent.package.label.sNo"/></th>
									<th><spring:message code="anvizent.package.label.columnName"/></th>
									<th><spring:message code="anvizent.package.label.dataType"/></th>
									<th><spring:message code="anvizent.package.label.columnSize"/></th>
									<th><spring:message code="anvizent.package.label.pk"/></th>
									<th><spring:message code="anvizent.package.label.nn"/></th>
									<th><spring:message code="anvizent.package.label.ai"/></th>
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
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" id="viewSourceDetailsPoUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="viewSourceDetailsPoUpHeader"></h4>
		      </div>
		      <div class="modal-body">
		      	
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" id="fileMappingWithILPopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog modal-lg">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code = "anvizent.package.button.mapFileHeadersUpload"/></h4>
		      </div>
		      <div class="modal-body">
		        	<div class="table-responsive" style="max-height: 400px;">
					<table class="table table-striped table-bordered tablebg" id="fileMappingWithILTable">
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.sNo"/></th>
								<th class="iLName"></th>
								<th class=""><spring:message code="anvizent.package.label.dataType"/></th>
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
		      </div>
		      <div class="modal-footer">
		         <input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.saveMapping"/>" id="saveMappingWithIL" name='saveMappingWithIL'/>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" id="messagePopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 500px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close close-popup" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"></h4>
		      </div>
		      <div class="modal-body">
		        		<div id="popUpMessage" style="text-align: center;"></div>
		      </div>
		      <div class="modal-footer" style="text-align: center;">
	        	<button type="button" class="btn btn-primary btn-sm" id="addAnotherSource"><span class='glyphicon glyphicon-chevron-left' aria-hidden='true'></span> <spring:message code = "anvizent.package.label.addSource"/></button>
		        <a href='<c:url value="/adt/standardpackage/edit/${packageId_var}"/>' class="btn btn-primary btn-sm"><spring:message code = "anvizent.package.link.mapOneMoredL"/></a>
		        <a href='<c:url value="/adt/package/schedule"/>' class="btn btn-primary btn-sm"><spring:message code = "anvizent.package.label.schedule"/><span class='glyphicon glyphicon-chevron-right' aria-hidden='true'></span></a>
		        <button type="button" class="btn btn-primary btn-sm close-popup" data-dismiss="modal"><spring:message code = "anvizent.package.button.cancel"/></button> 
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
			<div class="modal fade" tabindex="-1" role="dialog" style='overflow-y:auto;max-height:90%; 'id="viewDeatilsPreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
			      <div class="modal-dialog" style="width: 90%;">
			     <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="viewDeatilsPreviewPopUpHeader"></h4>
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
		<!-- delete IlConnection and IlConnectionMapping -->
		  <div class="modal fade" tabindex="-1" role="dialog" id="deleteIlConnection" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.modalHeader.deleteIlConnection"/></h4>
		      </div>
		      <div class="modal-body">
		        <p><spring:message code="anvizent.package.message.deletePackage.alltheMappingsWithTheseConnectionWillBeDeleted"/><br>
		        <spring:message code="anvizent.package.message.deletePackage.areYouSureYouWantToDeleteIlConnection"/><!-- &hellip; -->
		        </p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary" id="confirmDeleteIlConnection"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="deleStandardSourceFileAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title"><spring:message code="anvizent.package.label.modalHeader.deleteSource"/></h4>
		      </div>
		      <div class="modal-body">
		        <p><spring:message code="anvizent.package.message.deleteSource.areYouSureYouWantToDeleteSource"/>&hellip;</p>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-primary" id="confirmDeleteStandardSource"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
	</div>
</div>