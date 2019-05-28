<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
      <div class="page-title-v1"><h4><spring:message code="anvizent.package.header"/></h4></div>
      <div class="dummydiv"></div>
       <ol class="breadcrumb"> </ol>
      <jsp:include page="_error.jsp"></jsp:include>
     	<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
     	
     	<div class="row form-group">
			<h4 class="alignText"><spring:message code = "anvizent.package.label.databaseConnectionDetails"/></h4>
		</div>
     	
     	<div class='row form-group'>
			<div class="table-responsive">
				<table class="table table-striped table-bordered tablebg " id="existingConnectionsTable">
					<thead>
						<tr>
						    <th><spring:message code="anvizent.package.label.sNo"/></th>
						    <th><spring:message code="anvizent.package.label.connectionId"/></th>
							<th><spring:message code="anvizent.package.label.connectionName"/></th>
							<th><spring:message code="anvizent.package.label.edit"/></th>
							<th><spring:message code="anvizent.package.label.delete"/></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${existingConnections}" var="connection" varStatus="index">
							<tr>
							    <td><c:out value="${index.index+1}" /></td>
							    <td><c:out value="${connection.connectionId}" /></td>
								<td><c:out value="${connection.connectionName} " />${!connection.availableInCloud && connection.webApp ? '( Local DB )' : ''}</td>
								<td><button class="btn btn-primary btn-sm tablebuttons editConnection" data-connectionId="<c:out value="${connection.connectionId}"/>" ${!connection.availableInCloud && connection.webApp ? 'disabled' : ''}> <i class="fa fa-pencil" aria-hidden="true" title="<spring:message code="anvizent.package.label.edit" />" aria-hidden="true"></i></button></td>
								<td><button type ="button" class="btn btn-primary btn-sm tablebuttons deleteConnection" data-connectionId="<c:out value="${connection.connectionId}"/>" title="<spring:message code="anvizent.package.label.delete"/>"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
     	</div>
     	<div class='row form-group col-sm-12'>
     		<button type="button" class="btn btn-primary btn-sm" id="createNewConnection_dataBaseType"><spring:message code="anvizent.package.button.createNewConnection"/></button>
     	</div>
     	<div class='row form-group '>
			<div class='col-sm-6'>
				<div class="alert alert-success successMessage" style="display:none;">
					 <p class="successMessageText"></p>
				</div>
				<div class="alert alert-danger message" style="display:none;">				
					 <p class="messageText"></p>
				</div>
			</div>
		</div>
     	<div class="modal fade" tabindex="-1" role="dialog" id="createNewConnectionPopUp" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog">
				    <div class="modal-content" style="width:115%;">
					      <div class="modal-header">
						        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        	<h4 class="modal-title custom-modal-title connectionTitle"><spring:message code="anvizent.package.button.createNewConnection"/></h4>
					      </div>
					      <div class="modal-body">
					    	  <div class="panel panel-info">
					    	  		<div class="panel-body">
							      		<div class='row form-group'>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.connectionName"/>:
												</div>
												<div class='col-sm-8'>
													<input type="text" id="database_connectionName" class="form-control" data-minlength="1" data-maxlength="45">
												</div>
										</div>
										 <input type="hidden" id="connectionStringParams" class="form-control"/>
										<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.dataSource"/>:
											</div>
											<div class='col-sm-8'>
												<select  class="form-control" id="dataSourceName">
												<option value='0'><spring:message code="anvizent.package.label.selectDataSource"/></option>
													<c:forEach items="${allDataSourceList}" var="dataSource">
														<option value="<c:out value="${dataSource.dataSourceName}"/>"><c:out value="${dataSource.dataSourceName}"/></option>
													</c:forEach>
													<option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></option>
												</select>
											</div>
										</div>
										
										<div class='row form-group dataSourceOther' style="display:none">
											<div class='col-sm-4'>
											</div>
											<div class='col-sm-8'>
												<input type="text" id="dataSourceOtherName" class="form-control" data-minlength="1" data-maxlength="45">
											</div>
										</div>
										
										<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.connectorType"/>:
											</div>
											<div class='col-sm-8'>
												<select  class="form-control" id="database_databaseType">
													<c:forEach items="${databseList}" var="database">
														<option value="<c:out value="${database.id}"/>" data-connection_string_params="<c:out value="${database.connectionStringParams}"/>" data-urlformat="<c:out value="${database.urlFormat}"/>" data-protocal="<c:out value="${database.protocal}"/>" data-connectorId = "<c:out value="${database.connector_id}"/>"><c:out value="${database.name}"/></option>
													</c:forEach>
												</select>
											</div>
										</div>
										
										<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.connectionType"/>:
											</div>
											<div class='col-sm-8'>
												<select  class="form-control" id="database_connectionType">
													<option value="Direct"><spring:message code ="anvizent.package.label.direct"/></option>
												</select>
											</div>
										</div>
										
										<div class='row form-group servername-div'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.serverName"/>:
											</div>
											<div class='col-sm-8'>
												<input type="text"  class="form-control" id="database_serverName" data-minlength="1" data-maxlength="150">
												<p class="help-block"><span class='serverIpWithPort'></span></p>
											</div>
										</div>
										
										<div class='row form-group placeholders-div'>
											<div class='col-sm-12 database-placeholders'>
												<table class="table tablebg "
													id="requestParamsTable">
													<thead>
														<tr class="hidden">
															<th class="col-xs-4"><spring:message
																	code="anvizent.package.label.paramName" /></th>
															<th><spring:message code="anvizent.package.label.values" /></th>
														</tr>
													</thead>
													<tbody>
					
													</tbody>
													<tfoot class="hidden">
														<tr>
															<td class="col-sm-4"><span class="placeHolderLabelName"></span>
																<span class="mandatorySpan hidden"
																style="color: red; font-size: 15;">*</span></td>
															<td class="col-sm-8"><input type="hidden"
																class="placeHolderKey"><input type="text"
																class="form-control placeHolderValue"></td>
														</tr>
													</tfoot>
												</table>
											</div>
										</div>

										<div class='row form-group query-parameters-div' style="display:none">
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.dbParams"/>:
											</div>
											<div class='col-sm-8'>
												<input type="text"  class="form-control" id="database_queryParam" value="" placeholder = "[< ?  or  ; >propertyName1][=propertyValue1][< & or ; >propertyName2][=propertyValue2]"   data-minlength="1" data-maxlength="1000">
											</div>
										</div>
										
									
								<div class='row form-group'>
									<div class='col-sm-4'>
									<spring:message code="anvizent.package.label.dataBasevariables"/> :
										<a href='#' class='btn btn-primary btn-sm' id='addDBVarDiv'> 
																	<span class='glyphicon glyphicon-plus'></span>
										</a>
									</div>
									
									<div class='col-sm-8'>
										<table class="table table-striped table-bordered tablebg" id="dbVariablesTbl">
												<tbody>
													
												</tbody>
												<tfoot>
													<tr class="clonedDbVariable hidden">
														<td class="dbVarPair">
															<div class='col-sm-6'>
																<input type='text' class='dbVarKey form-control' placeholder="{key}">
															</div>
															<div class='col-sm-6'>
																<input type='text' class='dbVarValue form-control' placeholder="Value">
															</div>
														</td>
														<td>
															<button class='btn btn-primary btn-sm addDbVariablePairDiv'> 
																<span class='glyphicon glyphicon-plus'></span>
															</button>
														</td>
														<td>
															<button class='btn btn-primary btn-sm deleteDbVariablePairDiv'>
																<span class='glyphicon glyphicon-trash'></span>
															</button>
														</td>
													</tr>
												</tfoot>
										</table>
									</div>
									</div>	 
										 <div class='row form-group' id="sslEnableDiv" style="display:none">
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.sslenable"/>:
											</div>
											<div class='col-sm-8'>
												<input type="checkbox" class="sslEnable" id="sslEnable">
											</div>
										</div>
											<div class="mysqlSslCertificateFileNamesDiv" id ="mysqlSslCertificateFileNamesDiv" style="display:none">
										<div class='row form-group'>
											<div class='col-sm-4'>
												 <spring:message code="anvizent.package.label.existingsslfiles"/>:
											</div>
											<div class='col-sm-8'>
												<span id="mysqlSslCertificateFileNames"> </span>
											</div>
										</div>
										</div>
										<div class="mysqlSslCertificateFilesDiv" id ="mysqlSslCertificateFilesDiv" style="display:none">
										<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.sslclientkeyfile"/>: 
											</div>
											<div class='col-sm-8'>
												<input type="file"  class="form-control sslClientKeyFile" id="sslClientKeyFile" >
											</div>
										</div>
											<div class='row form-group'>
											<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.sslclientcerfile"/>:
											</div>
											<div class='col-sm-8'>
												<input type="file"  class="form-control sslClientCertFile"  id="sslClientCertFile">
											</div>
										</div>
											<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.sslservercafile"/>:
											</div>
											<div class='col-sm-8'>
												<input type="file"  class="form-control sslServerCaFile" id="sslServerCaFile">
											</div>
										</div>
										</div>  
										<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.userName"/>:
											</div>
											<div class='col-sm-8'>
												<input type="text"  class="form-control" id="database_username" value="" disabled="disabled" data-minlength="1" data-maxlength="45">
											</div>
										</div>
										<div class='row form-group' id="IL_database_password_div">
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.password"/>:
											</div>
											<div class='col-sm-8' >
												<input type="password"  class="form-control" id="database_password" value="" disabled="disabled" data-minlength="1" data-maxlength="100">
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
									 
								</div>											      
					      </div>
					      <div class="modal-footer">
						        <button type="button"  id="testConnection" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.testConnection"/></button>
								<button type="button" id="saveNewConnection_dataBaseType" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.saveConnection"/></button>
						        <button type="button" class="btn btn-primary btn-sm" id="editConnection" style="display:none" name='editConnection'><spring:message code="anvizent.package.button.editConnection"/></button>
								<button type="button" class="btn btn-primary btn-sm" id="updateConnection" style="display:none" name='updateConnection'><spring:message code="anvizent.package.button.updateConnection"/></button>
								<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.cancel"/></button>
					      </div>
				    </div> 
			  </div> 
		</div>
     	
     	<div class="modal fade" tabindex="-1" role="dialog" id="deleteConnectionAlert" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog">
				    <div class="modal-content">
					      <div class="modal-header">
						        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.button.deleteConnection"/></h4>
					      </div>
					      <div class="modal-body">
						        <p><spring:message code="anvizent.package.message.deletePackage.alltheMappingsWithTheseConnectionWillBeDeleted"/><br>
						        <spring:message code="anvizent.package.message.deletePackage.areYouSureYouWantToDeleteIlConnection"/> 
					        </p>
					      </div>
					      <div class="modal-footer">
						        <button type="button" class="btn btn-primary" id="confirmDeleteConnection"><spring:message code="anvizent.package.button.yes"/></button>
						        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
					      </div>
				    </div> 
			  </div> 
		</div>
</div>      
</div>