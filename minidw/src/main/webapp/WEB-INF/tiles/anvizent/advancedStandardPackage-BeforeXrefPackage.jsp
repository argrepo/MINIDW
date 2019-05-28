<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
        <div class="page-title-v1"><h4><spring:message code="anvizent.package.label.standardPackage"/></h4></div>
        <div class="dummydiv"></div>
		<ol class="breadcrumb">
			<!-- <li><a href="#">Home</a></li>
			<li><a href="#">Data Sets</a></li>
			 <li class="active">Standard Package</li> -->
		</ol>
<div class='col-sm-12'>
<form:form modelAttribute="standardPackageForm" method="POST" id="standardPackageForm">
		<jsp:include page="_error.jsp"></jsp:include>
		<form:hidden path="packageId"/>  
		<form:hidden path="isStandard"/>
		<form:hidden path="industryId"/>
		<%-- <sec:authentication var="principal" property="principal" /> --%>
		<input type="hidden" id="userID" value="${principal.userId}">
		<input type="hidden" id="iLId" value="">
		<input type="hidden" id="dLId" value="">
        <div class="panel panel-default">
			  <div class="panel-heading">
			    <h3 class="panel-title"><spring:message code="anvizent.package.label.advanced"/> <spring:message code="anvizent.package.label.standardPackage"/></h3>
			  </div>
			  <div class="panel-body">
			  	<%-- <div class="row form-group">
					  <label class="control-label col-md-2" for="industryId"><spring:message code="anvizent.package.standardPackage.label.selectIndustry"/></label>
					  <div class="col-sm-4">
						<c:choose>
							    <c:when test="${not empty standardPackageForm.packageId }">
									<form:select path="industryId" class="form-control" disabled="true">
									    <c:forEach items="${userIndustryList}" var="industry">
										    <form:option value="${industry.id}" >${industry.name}</form:option>
										</c:forEach>
									</form:select>
							    </c:when>
							    <c:otherwise>
							        <form:select path="industryId" class="form-control">
									    <c:forEach items="${userIndustryList}" var="industry">
										    <form:option value="${industry.id}" >${industry.name}</form:option>
										</c:forEach>
									</form:select>
							    </c:otherwise>
							</c:choose>
					  </div>
				</div> --%>
			    <div class='row form-group'>
						<label class="col-sm-3 control-label labelsgroup">
						<spring:message code="anvizent.package.label.packageName"/></label>
						<div class='col-sm-4' >
							<c:choose>
							    <c:when test="${not empty standardPackageForm.packageId}">
									<form:input path="packageName" class="form-control" disabled="true"/>
									<form:errors path="packageName" cssStyle="color: #ff0000"/>
							    </c:when>
							    <c:otherwise>
									<form:input path="packageName" class="form-control" />
									<form:errors path="packageName" cssStyle="color: #ff0000"/>
							    </c:otherwise>
							</c:choose>
						</div>
						 <div class="col-md-4">
						  			<c:choose>
									    <c:when test="${empty standardPackageForm.packageId}">
									       <input type="submit" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.label.createPackage"/>" 
																id="createStandardPackage" name='createStandardPackage'/>
									    </c:when>
									</c:choose>
						  </div>
			  			<!-- <div class="clearfix"></div> -->
					 
			  </div>
			  		<div class="row form-group">
				  		
					  </div>
			  </div>
		</div>
		<c:if test="${not empty standardPackageForm.packageId}">
			<%-- <div class='row form-group '>
					<c:forEach items="${userDlList}" var="dLInfo">
						<div class='col-sm-4'>
							    <label class="radio-inline"><input type="radio" name="DLSelection" value="${dLInfo.dL_id}" class='dlSelection' id='DL_${dLInfo.dL_id}'>${dLInfo.dL_name}
							    </label>
							    <button type='button' class='viewDLSchema btn btn-primary btn-xs' data-dLId='${dLInfo.dL_id}' data-dL_name='${dLInfo.dL_name}'>View Table Structure <!-- <span class='glyphicon glyphicon-expand' aria-hidden='true'></span> --></button>
							</div>
						</c:forEach>
				
			</div> --%>
			<div class='row form-group'>
				<c:if test="${empty userDlList}">
					<div class="alert alert-warning" role="alert"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>  <spring:message code="anvizent.package.message.dLsAreNotAssignedPleaseContactSuperAdmin"/></div>
				</c:if>
				<c:if test="${not empty userDlList}">
					<div class='col-sm-5'>
						<div class="table-responsive" >
							<table class="table table-striped table-bordered tablebg" id="DLSectionTable">
								<thead>
									<tr>
										<!--<th>DL_Id</th>   -->
										 <th><spring:message code = "anvizent.package.label.sNo"/></th>  
										<th><spring:message code = "anvizent.package.label.moduleName"/></th>
										<th><spring:message code = "anvizent.package.label.tableName"/></th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${userDlList}" var="dLInfo">
										<tr>
											<%-- <td>${dLInfo.dL_id}</td> --%>
											<td></td>
											<td> <label class="radio-inline toolTip" class='btn btn-primary btn-sm' data-toggle="tooltip" data-placement="bottom" title="${dLInfo.description}"><input type="radio" name="DLSelection" value="${dLInfo.dL_id}" class='dlSelection' id='DL_${dLInfo.dL_id}' >${dLInfo.dL_name} 
								    			</label></td>
											<td>${dLInfo.dL_table_name}</td>
											<td>
												 <button type='button' class='viewDLSchema btn btn-primary btn-xs' data-dLId='${dLInfo.dL_id}' data-dL_name='${dLInfo.dL_table_name}'>View Table Structure <!-- <span class='glyphicon glyphicon-expand' aria-hidden='true'></span> --></button>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</c:if>
			
			<div class='col-sm-7' style="display: none;" id="ILSection">
					<div class="table-responsive" >
						<table class="table table-striped table-bordered tablebg" id="ILSectionTable">
						<thead>
								<tr>
									<!-- <th>IL_Id</th> -->
									<th><spring:message code = "anvizent.package.label.sNo"/></th>
									<th><spring:message code = "anvizent.package.label.source"/></th>
									<th><spring:message code = "anvizent.package.label.tableName"/></th>
									<th><spring:message code = "anvizent.package.label.status"/></th>
									<th></th>
									<th></th>
									<th></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
				
				<div class="pull-left col-sm-12">		
					<a href="<c:url value="/adv/package/standardpackage" />" class="btn btn-primary btn-sm"><spring:message code = "anvizent.package.link.back"/></a>		
				</div>
				
			</div>
		</c:if>
		
		<div class="row form-group">
			
		</div>
		
</form:form>   


   
		<!-- <div class='row form-group hide' id='ILSection' >
			<div class='col-sm-6'>
				<div class="table-responsive" >
					<table class="table table-striped table-bordered tablebg" id="ILSectionTable">
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div> -->
		<div class="modal fade" tabindex="-1" role="dialog" id="viewSchema" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title" id="viewSchemaHeader"></h4>
		      </div>
		      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
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
		        <%-- <button type="button" class="btn btn-primary" id="confirmDeletePackage"><spring:message code="anvizent.package.button.yes"/></button> --%>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
	<%-- <div id="ILConnectionDetails" class="modal fade " role="dialog" data-backdrop="static" data-keyboard="false">
	  <div class="modal-dialog modal-lg" style="width:700px;">
	
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title" id="iLPopUpHeader"></h4>
			</div>
			<div class="modal-body">
				<div class="alert alert-danger message" style="display:none;">
					 <!-- <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> -->
					 <p class="messageText"></p>
				</div>
				<div class="alert alert-success successMessage" style="display:none;">
												 <!-- <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button> -->
												 <p class="successMessageText"></p>
									</div>
				<div id='databaseSettings' class="modalpad">
					<div class='row form-group'>
						<div class='col-sm-3'>
							<label class="radio-inline"><input type="radio" name="typeSelection" id = 'flatFiles'><spring:message code="anvizent.package.label.flatFile"/></label>
						</div>
						<div class='col-sm-3'>
							<label class="radio-inline"><input type="radio" name="typeSelection" id='database'><spring:message code="anvizent.package.label.database"/></label>
						</div>
					</div>
					
					<div class='row' >
					  <div id='databaseConnectionDetails' style="display:none;">
						<div class='row form-group '>
								<div class='col-sm-3'>
									<spring:message code="anvizent.package.label.existingConnections"/>
								</div>
								<div class='col-sm-4'>
									<select class="form-control" id="existingConnections">
										<!-- <option value="">Select Connection</option> -->
									</select>
								</div>
								<div class="col-md-3">
									    <button type="button" class="btn btn-primary btn-sm" 
																id="createNewConnection_dataBaseType" name='createNewConnection_dataBaseType'><spring:message code="anvizent.package.button.createNewConnection"/></button>
						  		</div>
							</div>
						<div class="panel panel-info" id="databaseConnectionPanel" style="display:none;">
							<div class="panel-heading"><spring:message code="anvizent.package.label.databaseConnectionDetails"/></div>
							<div class="panel-body">
								<div class='row form-group '>
										<div class='col-sm-3'>
											<spring:message code="anvizent.package.label.connectionName"/>:
										</div>
										<div class='col-sm-6'>
											<input type="text" id="IL_database_connectionName" class="form-control">
										</div>
									</div>
								<div class='row form-group '>
									<div class='col-sm-3'>
										<spring:message code="anvizent.package.label.databaseType"/>:
									</div>
									<div class='col-sm-6'>
										<select  class="form-control" id="IL_database_databaseType">
											<c:forEach items="${databseList}" var="database">
												<option value="${database.id}">${database.name}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class='row form-group '>
									<div class='col-sm-3'>
										<spring:message code="anvizent.package.label.connectionType"/>:
									</div>
									<div class='col-sm-6'>
										<select  class="form-control" id="IL_database_connectionType">
											<option value="Direct">Direct</option>
											<option value="Tunnel">Tunnel</option>
										</select>
									</div>
								</div>
								
								<div class='row form-group '>
									<div class='col-sm-3'>
										<spring:message code="anvizent.package.label.serverName"/>:
									</div>
									<div class='col-sm-6'>
										<input type="text"  class="form-control" id="IL_database_serverName">
									</div>
								</div>
								<div class='row form-group '>
									<div class='col-sm-3'>
										<spring:message code="anvizent.package.label.userName"/>:
									</div>
									<div class='col-sm-6'>
										<input type="text"  class="form-control" id="IL_database_username">
									</div>
								</div>
								<div class='row form-group ' id="IL_database_password_div">
									<div class='col-sm-3'>
										<spring:message code="anvizent.package.label.password"/>:
									</div>
									<div class='col-sm-6' >
										<input type="password"  class="form-control" id="IL_database_password">
									</div>
								</div>
								<div class='row form-group '>
									<div class='col-sm-3'>
										<input type="button" value='Test Connection' class="btn btn-primary btn-sm" id="testConnection"/>
									</div>
									<div class='col-sm-3'>
										<input type="button" id="saveNewConnection_dataBaseType" value='<spring:message code="anvizent.package.button.saveConnection"/>' class="btn btn-primary btn-sm"/>
									</div>
									<!-- <div class='col-sm-6'>
										<input type="button" value='Test connection' class="btn primary-btn"/>
									</div> -->
								</div>
								<div class='row form-group IL_queryCommand'>
									<div class='col-sm-3'>
										<spring:message code="anvizent.package.label.typeOfCommand"/>:
									</div>
									
									<div class='col-sm-6'>
										<select class="form-control" id="typeOfCommand">
											<option value="Query">Query</option>
											<option value="Stored Procedure">Stored Procedure</option>
										</select>
									</div>
									<!-- <div class='col-sm-3'>
										<input type="button" value='Check Syntax' id='checkQuerySyntax' class="btn btn-primary btn-sm"/>
									</div> -->
								</div>
								<div class='row form-group IL_queryCommand'>
									<div class='col-sm-12  s-script'>
										<textarea class="form-control" rows="6" id="queryScript" placeholder="Query"></textarea>
									</div>
									<div class="col-sm-12 s-script" style="display:none;">
										<div class="row">
											<div class="col-sm-9">
												<input class="form-control" id="procedureName" placeholder="Stored Procedure Name">
											</div>
											<div class="col-sm-3 hide">
												<input type="button" class="btn btn-primary btn-sm" value="Add Parameters" id="addparameters">
											</div>
										</div>
									</div>
								</div>
								<div class="row form-group param-hidden" style="display:none;">
									<div class="col-sm-2">
										Name
									</div>
									<div class="col-sm-4">
										<input type="text" class="s-param-name form-control"> 
									</div>
									<div class="col-sm-2">
										Value
									</div>
									<div class="col-sm-4">
										<input type="text" class="s-param-value form-control"> 
									</div>
								</div>
								<div class='row form-group'>
									<div class='col-sm-3'>
										<input type="button" value='Validate Query/SP' id='checkQuerySyntax' class="btn btn-primary btn-sm"/>
									</div>
									<div class='col-sm-9 queryValidatemessageDiv'>
										
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
									<input type="hidden"  id="industryIdForFileUpload" name="industry_Id"/>
									<div class='row form-group '>
										<div class='col-sm-3'>
											<spring:message code="anvizent.package.label.fileType"/>:
										</div>
										<div class='col-sm-6'>
											<select class="form-control" id="flatFileType" name="flatFileType">
												<option value="csv">csv</option>
												<option value="xls">xls</option>
												<option value="xlsx">xlsx</option>
											</select>
										</div>
									</div>
									
									<div class='row form-group'>
										<div class='col-sm-3'>
											<spring:message code="anvizent.package.label.delimiter"/>:
										</div>
										<div class='col-sm-6' >
											<input type="text" class="form-control" id="delimeter" value="," name="delimeter">
										</div>
									</div>
									<div class='row form-group'>
										<div class='col-sm-3'>
											<spring:message code="anvizent.package.label.firstRowHasColumnNames"/>:
										</div>
										<div class='col-sm-6'>
											<div class='col-sm-3'>
												<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="true" checked="checked">Yes</label>
											</div>
											<div class='col-sm-3'>
												<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="false">No</label>
											</div>
										</div>
									</div>
									<div class='row form-group '>
										
										<div class='col-sm-3'>
											<spring:message code="anvizent.package.label.file"/>:
											
										</div>
										<!-- <label class="btn btn-primary" for="my-file-selector">
										    <input id="my-file-selector" type="file" >
										    Browse
										</label> -->
										<div class='col-sm-6' >
											<input  type="file" name="file" id="fileUpload"> 
											</div>
									</div>
									<div class="checkbox">
									    <label class="col-sm-4">
									      <input type="checkbox" id="mapFileWithILCheckBox"> Map the file with IL table
									    </label>
									    <div class='col-sm-6' >
											<input type="submit" value='Map File' id='mapFileWithIL' class="btn btn-primary btn-sm" name="mapFileWithIL" style="display: none;"/>
											<a href='<c:url value="/adv/package/standardPackage/new"/>' class="btn btn-primary btn-sm">Map File with IL</a>
										</div>
									  </div>
									</form:form>
								</div>
						</div>
						</div>
						</div>
						</div>
					</div>
			
		            <div class="modal-footer">
		            	<input type="button" value='<spring:message code="anvizent.package.button.save"/>' id='saveILConnectionMapping' class="btn btn-primary" style="display: none;"/>
						<input type="button"  value='Save & Upload' id='saveAndUpload' class="btn btn-primary" style="display: none;"/>
						<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
						
				  </div>	
		</div>
        
		</div>
		
		</div> --%>
		
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
		        <%-- <button type="button" class="btn btn-primary" id="confirmDeletePackage"><spring:message code="anvizent.package.button.yes"/></button> --%>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="show_ILConnectionDataBaseInfo" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title"></h4>
		      </div>
		      <div class="modal-body">
		        	<div class='row form-group '>
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.connectionName"/>:
										</div>
										<div class='col-sm-8' >
											<input type="text" class="form-control" id="show_connectionName" disabled="disabled">
										</div>
					</div>
					<div class='row form-group '>
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.connectorType"/>:
										</div>
										<div class='col-sm-8' >
											<input type="text" class="form-control" id="show_databaseType" disabled="disabled">
										</div>
					</div>
					<div class='row form-group '>
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.connectionType"/>:
										</div>
										<div class='col-sm-8' >
											<input type="text" class="form-control" id="show_connectionType" disabled="disabled">
										</div>
					</div>
					<div class='row form-group '>
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.serverName"/>:
										</div>
										<div class='col-sm-8' >
											<input type="text" class="form-control" id="show_serverName" disabled="disabled">
										</div>
					</div>
					<div class='row form-group '>
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.userName"/>:
										</div>
										<div class='col-sm-8' >
											<input type="text" class="form-control" id="show_userName" disabled="disabled">
										</div>
					</div>
					<div class='row form-group '>
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.typeOfCommand"/>:
										</div>
										<div class='col-sm-8' >
											<input type="text" class="form-control" id="show_typeOfCommand" disabled="disabled">
										</div>
					</div>
					<div class='row form-group '>
										<div class='col-sm-4'>
											
										</div>
										<div class='col-sm-8' >
											<textarea class="form-control" rows="6" id="show_queryScript" disabled="disabled"></textarea>
											<input type="text" class="form-control" id="show_procedureName" disabled="disabled" style="display:none;">
										</div>
					</div>
					<div class="row form-group" id="procedureParametersDiv">
						<div class="col-sm-12">
							<label style="font-weight: normal;">
								<spring:message code = "anvizent.package.label.Parameters"/>
							</label>
						</div>
						<div class="col-sm-12">
							<div class="row form-group show-param-hidden" style="display:none;">
								<div class="col-sm-2">
									<spring:message code = "anvizent.package.label.name"/>
								</div>
								<div class="col-sm-4">
									<input type="text" class="show-param-name form-control" disabled="disabled">
								</div>
								<div class="col-sm-2">
									<spring:message code = "anvizent.package.label.value"/>
								</div>
								<div class="col-sm-4">
									<input type="text" class="show-param-value form-control" disabled="disabled">
								</div>
							</div>
						</div>	
					</div>
				</div>
		        	
		      <div class="modal-footer">
		        <%-- <button type="button" class="btn btn-primary" id="confirmDeletePackage"><spring:message code="anvizent.package.button.yes"/></button> --%>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
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
			        <button type="button" class="btn btn-default viewDetailsclosePreview" data-dismiss="modal"><spring:message code = "anvizent.package.button.close"/></button>
			      </div>
			    </div> 
			     </div> 
    </div>
</div>
</div>