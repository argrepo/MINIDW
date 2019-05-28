<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<meta http-equiv="Cache-control" content="no-cache">
<div class="col-sm-12 rightdiv">
	<div class="page-title-v1">
		<h4>
			<spring:message code="anvizent.package.label.addilSource" />
		</h4>
	</div>
	<div class="dummydiv"></div>
	<ol class="breadcrumb">
	</ol>
	<div class='row form-group'>
		<h4 class="alignText">
			<spring:message code="anvizent.package.label.addilSource" />
		</h4>
	</div>
	<jsp:include page="_error.jsp"></jsp:include>

	<form:form modelAttribute="standardPackageForm" method="POST" id="standardPackageForm">
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
		<input type="hidden" id="isTrialUser" value="<c:out value="${principal.isTrailUser}"/>">
		<input type="hidden" id="isSandBox" value="<c:out value="${principal.isSandBox}"/>">
		<form:hidden path="industryId" />
		<form:hidden path="packageId" value="${param['packageId']}" />
		<form:hidden path="dLId" />
		<c:set var="packageId_var" value="${standardPackageForm.packageId}" />
		<input type="hidden" id="packageId" value="<c:out value="${standardPackageForm.packageId}"/>">
		<input type="hidden" id="dlId" value="<c:out value="${standardPackageForm.dLId}"/>">
		<input type="hidden" id="ilId" value="<c:out value="${defaultILId}"/>">
		<div class='row form-group'>
			<div class='col-sm-4 col-lg-2'>
				<label class="control-label "><spring:message code="anvizent.package.label.packageName" /></label>
				<form:hidden path="packageName" class="form-control" disabled="true" />
				<div class="txt-break">${standardPackageForm.packageName  }</div>
			</div>
			<div class='col-sm-4 col-lg-3'>
				<label class="control-label "><spring:message code="anvizent.package.label.moduleName" /></label>
				<form:hidden path="dLName" class="form-control" disabled="true" />
				<div class="txt-break">${standardPackageForm.dLName  }</div>
			</div>
			<div class='col-sm-4 col-lg-2'>
				<label class="control-label "><spring:message code="anvizent.package.label.inputLayout" /></label>
				<select id="iLName" name="iLName" class="form-control">
					<c:forEach items="${iLList}" var="iLInfo">
						<option value="<c:out value="${iLInfo.iL_id}"/>" data-tableName="<c:out value="${iLInfo.iL_table_name}"/>"><c:out value="${iLInfo.iL_name}"/></option>
					</c:forEach>
				</select>
			</div>
			<br>
			<div class='form-group'>
				<div class="col-sm-12 col-lg-4">
					<a class="btn btn-primary btn-sm table_btn viewILSourceDetails" href="<c:url value="/adt/package/viewIlSource/${standardPackageForm.packageId}/${standardPackageForm.dLId}/${defaultILId}?from=addIl"/>">
					<spring:message code="anvizent.package.link.viewSourceDetails" /></a>
					<input type="button" class="btn btn-primary btn-sm table_btn viewILTableStructure" value="<spring:message code = "anvizent.package.button.viewTableStructure"/>" id="viewILTableStructure" />
					<button type="button" class="btn btn-primary btn-sm back_btn downloadSampleTemplate" id="downloadTemplate">
						<span title="<spring:message code = "anvizent.package.label.DownloadTemplate"/>" class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
					</button>
				</div>
			</div>
		</div>

	</form:form>

	<div class="col-sm-12">
		<div class="alert alert-danger message" style="display: none;">
			<p class="messageText"></p>
		</div>
		<div class="alert alert-success successMessage" style="display: none;">
			<p class="successMessageText"></p>
		</div>
		<div id='databaseSettings'>
			<div class='row form-group'>

				<div class='col-sm-2'>
					<label class="radio-inline"><input type="radio" name="typeSelection" id='flatFiles'> <spring:message code="anvizent.package.label.flatFile" /></label>
				</div>
				<c:choose>
					<c:when test="${principal.isTrailUser == true}">
						<div class='col-sm-2'>
							<label class="radio-inline disable-text"><input type="radio" name="typeSelection" id='database' disabled="disabled"> <spring:message code="anvizent.package.label.database" /> <img
								src="<c:url value="/resources/images/lock.png"/>" class="img-responsive lock-symbol" alt="Responsive image"
							></label>
						</div>
						<div class='col-sm-2'>
							<label class="radio-inline disable-text"><input type="radio" name="typeSelection" id='webService' disabled="disabled"> <spring:message code="anvizent.package.label.Webservice" /> <img
								src="<c:url value="/resources/images/lock.png"/>" class="img-responsive lock-symbol" alt="Responsive image"
							></label>
						</div>
					</c:when>
					<c:otherwise>
						<div class='col-sm-2'>
							<label class="radio-inline"><input type="radio" name="typeSelection" id='database' ${principal.isSandBox == false ? '' : 'disabled'}> <spring:message code="anvizent.package.label.database" /></label>
						</div>
						<div class="col-sm-2">
							<label class="radio-inline"><input type="radio" name="typeSelection" id="webService" ${principal.isSandBox == false ? '' : 'disabled'}> <spring:message code="anvizent.package.label.Webservice" /></label>
						</div>
					</c:otherwise>
				</c:choose>

			</div>

			<div class=''>
				<div id='databaseConnectionDetails' style="display: none;">
					<div class='row form-group '>
						<div class='col-sm-2'>
							<spring:message code="anvizent.package.label.existingConnections" />
						</div>
						<div class='col-sm-3'>
							<select class="form-control" id="existingConnections" name="existingConnections">
							</select>
						</div>
						<div class="col-md-6">
							<button type="button" class="btn btn-primary btn-sm" id="createNewConnection_dataBaseType" name='createNewConnection_dataBaseType'>
								<spring:message code="anvizent.package.button.createNewConnection" />
							</button>
							<button type="button" class="btn btn-primary btn-sm" id="deleteDatabaseTypeConnection" style="display: none" name='deleteDatabaseTypeConnection'>
								<spring:message code="anvizent.package.button.deleteConnection" />
							</button>
						</div>

					</div>
					<div class="panel panel-info" id="databaseConnectionPanel" style="display: none;">
						<div class="panel-heading">
							<spring:message code="anvizent.package.label.databaseConnectionDetails" />
						</div>
						<div class="panel-body">
							<div class='row form-group '>
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.connectionName" />
									:
								</div>
								<div class='col-sm-8'>
									<input type="text" id="IL_database_connectionName" class="form-control" data-minlength="1" data-maxlength="45"> 
								</div>
							</div>
							
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.dataSource"/>:
									</div>
									<div class='col-sm-8'>
										<select  class="form-control dataSourceName dataSource_name" id="dataSourceName">
										<option value='0'><spring:message code="anvizent.package.label.selectDataSource"/></option>
											<c:forEach items="${allDataSourceList}" var="dataSource">
												<option value="<c:out value="${dataSource.dataSourceName}"/>"><c:out value="${dataSource.dataSourceName}"/></option>
											</c:forEach>
											<option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></option>
										</select>
									</div>
							</div>
								
							<div class='row form-group dataSourceOther' style="display:none">
									<div class='col-sm-2'>
									</div>
									<div class='col-sm-8'>
										<input type="text" id="dataSourceOtherName" class="form-control" data-minlength="1" data-maxlength="45">
									</div>
							</div>
					
							<div class='row form-group '>
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.connectorType" /> :
								</div>
								<div class='col-sm-8'>
									<select class="form-control" id="IL_database_databaseType">
										<c:forEach items="${databseList}" var="database">
											<option value="<c:out value="${database.id}"/>"  data-protocal="<c:out value="${database.protocal}"/>" data-connection_string_params="<c:out value="${database.connectionStringParams}"/>" data-urlformat="<c:out value="${database.urlFormat}"/>" data-connectorId="<c:out value="${database.connector_id}"/>"><c:out value="${database.name}"></c:out></option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class='row form-group '>
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.connectionType" /> :
								</div>
								<div class='col-sm-8'>
									<select class="form-control" id="IL_database_connectionType">
										<option value="Direct"><spring:message code="anvizent.package.label.direct" /></option>
									</select>
								</div>
							</div>

							<div class='row form-group servername-div'>
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.serverName" /> :
								</div>
								<div class='col-sm-8'>
									<input type="text" class="form-control" id="IL_database_serverName" data-minlength="1" data-maxlength="150">
									<p class="help-block">
										<span class='serverIpWithPort'></span>
									</p>
								</div>
							</div>
							
							<div class='row form-group placeholders-div'>
								<div class='col-sm-10 database-placeholders'>
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
												<td class="col-sm-2"><span class="placeHolderLabelName"></span>
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
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.dbParams"/>:
								</div>
								<div class='col-sm-8'>
									<input type="text"  class="form-control" id="database_queryParam" placeholder = "[< ?  or  ; >propertyName1][=propertyValue1][< & or ; >propertyName2][=propertyValue2]"   value=""  data-minlength="1" data-maxlength="1000">
								</div>
							</div>
							 <div class='row form-group' id="sslEnableDiv" style="display:none">
											<div class='col-sm-2'>
												<spring:message code="anvizent.package.label.sslenable"/>:
											</div>
											<div class='col-sm-8'>
												<input type="checkbox" class="sslEnable" id="sslEnable">
											</div>
										</div>
											<div class="mysqlSslCertificateFileNamesDiv" id ="mysqlSslCertificateFileNamesDiv" style="display:none">
										<div class='row form-group'>
											<div class='col-sm-2'>
												 <spring:message code="anvizent.package.label.existingsslfiles"/>:
											</div>
											<div class='col-sm-8'>
												<span id="mysqlSslCertificateFileNames"> </span>
											</div>
										</div>
										</div>
										<div class="mysqlSslCertificateFilesDiv" id ="mysqlSslCertificateFilesDiv" style="display:none">
										<div class='row form-group'>
											<div class='col-sm-2'>
												 <spring:message code="anvizent.package.label.sslclientkeyfile"/>: 
											</div>
											<div class='col-sm-8'>
												<input type="file"  class="form-control sslClientKeyFile" id="sslClientKeyFile" >
											</div>
										</div>
											<div class='row form-group'>
											<div class='col-sm-2'>
											<spring:message code="anvizent.package.label.sslclientcerfile"/>:
											</div>
											<div class='col-sm-8'>
												<input type="file"  class="form-control sslClientCertFile"  id="sslClientCertFile">
											</div>
										</div>
											<div class='row form-group'>
											<div class='col-sm-2'>
												<spring:message code="anvizent.package.label.sslservercafile"/>:
											</div>
											<div class='col-sm-8'>
												<input type="file"  class="form-control sslServerCaFile" id="sslServerCaFile">
											</div>
										</div>
										</div>  
							<div class='row form-group '>
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.userName" /> :
								</div>
								<div class='col-sm-8'>
									<input type="text" class="form-control" id="IL_database_username" data-minlength="1" data-maxlength="45">
								</div>
							</div>
							<div class='row form-group ' id="IL_database_password_div">
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.password" /> :
								</div>
								<div class='col-sm-8'>
									<input type="password" class="form-control" id="IL_database_password" data-minlength="1" data-maxlength="100">
								</div>
							</div>
							 <div class='row form-group'>
						 			<div class='col-sm-2'>
												<spring:message code="anvizent.package.label.dateFormat" />( <spring:message code="anvizent.package.label.optional"/>) :
									 </div>
									<div class="col-sm-8">
									<input type="text" class="form-control" id="dateFormat" placeholder="Ex: YYYY-MM-DD"  data-minlength="1" data-maxlength="45">
								 	</div>
							 	 </div>
							 	 
							 	 <div class='row form-group'>
											<div class='col-sm-2'>
												<spring:message code="anvizent.package.label.timeZone" /> :
											</div>
											<div class='col-sm-8'>
												<select  class="form-control timesZone" id="timesZone" >
													<option value="select"><spring:message code = "anvizent.package.label.selectOption"/></option>
												</select>
											</div>
							     </div>
							      <div class='row form-group hidden db-variables-add-div'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.dataBasevariables" /> :
										<button class='btn btn-primary btn-sm' id='addDBVarDiv'><span class='glyphicon glyphicon-plus'></span>
										</button>
									</div>
									
									<div class='col-sm-8'>
										<table class="table tablebg" id="dbVariablesTbl">
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
													<tr class="displayDbVariable hidden">
														<th>
															<span class="dbVarKey txt-break"></span>
														</th>
														<td>
															<span class="dbVarValue txt-break"></span>
														</td>
													</tr>
												</tfoot>
										</table>
									</div>
								</div>
							<div class='row form-group '>
								<div class='col-sm-2'>
									<input type="button" value='<spring:message code = "anvizent.package.button.testConnection"/>' class="btn btn-primary btn-sm" id="testConnection" />
								</div>
								<div class='col-sm-8'>
									<input type="button" id="saveNewConnection_dataBaseType" value='<spring:message code="anvizent.package.button.saveConnection"/>' class="btn btn-primary btn-sm" />
								</div>
							</div>
							<div class='row form-group IL_queryCommand'>
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.typeOfCommand" />
									:
								</div>

								<div class='col-sm-8'>
									<select class="form-control" id="typeOfCommand">
										<option value="Query"><spring:message code="anvizent.package.label.query" /></option>
										<option value="Stored Procedure"><spring:message code="anvizent.package.label.storedProcedure" /></option>
									</select>
								</div>
							</div>
								 <div id="defualtVariableDbSchema" style="display:none"></div>
								  	<%-- <div class='row form-group IL_queryCommand'>
										<div class='col-sm-2'>
										</div>
										<div class='col-sm-3' id="replaceDbSchema">
											<input type="button" value='<spring:message code="anvizent.package.label.replaceSchemas"/>' id='replaceShemas' style="display:none" class="btn btn-primary btn-sm"/>
									   </div>
							        </div> --%>
							        
							 <div class='row form-group' id='replace' style="display: none">
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.find" /> :
								</div>
								<div class='col-sm-3'>
									<input type="text" class="form-control" id="replace_variable">
								</div>
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.replaceWith" /> :
								</div>
								<div class='col-sm-3'>
									<input type="text" class="form-control" id="replace_with">
								</div>
							</div>
							
							<div class='row form-group' id='replaceAll' style="display: none">
										<div class="col-xs-12" style="padding-right: 190px;">	
					                      <div class="" id="replace_All" style="float: right;">
					                           <input type="button" value="<spring:message code="anvizent.package.label.replaceAll" />" id="replace_all" class="btn btn-primary btn-sm">
					                      </div>
			                       		<div class="" id="buttonUndo" style="float: right; margin: 0px 10px;">
					                    		<input type="button" value="<spring:message code="anvizent.package.label.undo"/>" id="undo" class="btn btn-primary btn-sm">
	 						 			</div>
	  							</div>
				        	</div>
							    <div class='row form-group'>
									<div class='col-sm-2' id="historicalIncremental"><spring:message code="anvizent.package.label.incremental" /> : </div>
									<div class='col-sm-8' id="il_incremental_update_div">
										<label>
											<c:if test="${principal.isTrailUser == true}">
												<input type="checkbox" id='il_incremental_update' disabled="disabled">
												<spring:message code="anvizent.package.label.incrementalUpdate" />
												<img src="<c:url value="/resources/images/lock.png"/>" class="img-responsive lock-symbol" alt="Responsive image">
											</c:if>
											<c:if test="${principal.isTrailUser == false}">
												<input type="checkbox" id='il_incremental_update'>
												<b><spring:message code="anvizent.package.label.incrementalUpdate" /></b>
											</c:if>
										</label>
								</div>
							</div>
							<div class='row form-group' id="historicalLoadDiv" style="display:none;">
								<div class='col-sm-2'> <spring:message code="anvizent.package.label.historicalload"/> :</div>
								<div class='col-sm-4 col-lg-3'>
					                <label class="control-label "><spring:message code="anvizent.package.label.fromdate" /></label>
					                <input type="text" placeholder="YYYY-MM-DD" id="historicalFromDate" class="form-control"/>
			                   </div>
			                    <div class='col-sm-4 col-lg-3'>
					                <label class="control-label "><spring:message code="anvizent.package.label.todate" /></label>
					                <input type="text" placeholder="YYYY-MM-DD" id="historicalToDate"  class="form-control"/>	
			                   </div>
							 <div class='col-sm-4 col-lg-2'>
								 <label class="control-label "><spring:message code="anvizent.package.label.loadinterval" /></label>
								     <select id="loadInterval" name="loadInterval" class="form-control">
										 <option value="0"><spring:message code="anvizent.package.label.selectOption" /></option>
										 <c:forEach var="i" begin="30" step="30" end="90" >
				                        	<option value="<c:out value="${i}"/>"><c:out value="${i}"/></option>
	                                     </c:forEach>
							     	</select>  
							</div>
							</div>
                          <div class='row form-group'> </div>
							<div class='row form-group IL_queryCommand'>
								<div class='row form-group  col-sm-10  s-script'>
									<textarea class="form-control" rows="6" id="queryScript" placeholder="<spring:message code="anvizent.package.label.query"/>"></textarea>
									<input type="hidden" id="oldQueryScript" name="queryScript">
								</div>
								
								<div class='row form-group col-sm-10 s-script max_date_query' style="display:none;">
										<div class="row">
											<textarea class="form-control" rows="6" name="maxDateQuery" id="maxDatequery" placeholder="<spring:message code="anvizent.package.label.maxDateQuery"/>"></textarea>
											<input type="hidden" id="oldMaxDateQuery" name="queryScript">
										</div>
								</div>
								<div class="col-sm-10 s-script" style="display: none;">
									<div class="row">

										<input class="form-control" id="procedureName" placeholder="<spring:message code="anvizent.package.label.storedProcedureName"/>">

										<div class="col-sm-3 hide">
											<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.addParameters"/>" id="addparameters">
										</div>
									</div>
								</div>

							</div>
							<div class="row form-group param-hidden" style="display: none;">
								<div class="col-sm-2">
									<spring:message code="anvizent.package.label.name" />
								</div>
								<div class="col-sm-4">
									<input type="text" class="s-param-name form-control">
								</div>
								<div class="col-sm-2">
									<spring:message code="anvizent.package.label.value" />
								</div>
								<div class="col-sm-4">
									<input type="text" class="s-param-value form-control">
								</div>
							</div>
							<div class='col-sm-offset-1 col-sm-8 queryValidatemessageDiv'></div>
							<div class="row">
							 <div class="col-sm-10 pull-right" id="databasemessage" style="display: none;"></div>
							 </div>
							<div class='row form-group'>
								<div class='col-sm-12'>
									<c:if test="${principal.isTrailUser == false}">
										<form action="<c:url value="/adt/package/easyQueryBuilderForStandardPackage" />" method="POST" id="intiateBuildQuery">
											<input type="button" value='<spring:message code="anvizent.package.button.validateQuerySP"/>' id='checkQuerySyntax' class="btn btn-primary btn-sm" /> 
											<input type="button" value='<spring:message code="anvizent.package.button.preview"/>'
												id='checkTablePreview' class="btn btn-primary btn-sm" data-target='#tablePreviewPopUp' /> 
											<input type="hidden" name="schemaName" id="qbschemaName"> 
											<input type="hidden" name="packageId" id="packageId" value="<c:out value="${standardPackageForm.packageId}"/>"> 
											<input type="hidden" name="dlId" id="dlId" value="<c:out value="${standardPackageForm.dLId}" />" >
											<input type="hidden" name="iLId" id="qbiLId"> 
											<input type="hidden" name="connectionId" id="qbconnectionId">
											<input type="hidden" name="${_csrf.parameterName}" value="<c:out value="${_csrf.token}" />"/>
											<button type="button" class="btn btn-primary btn-sm buildQuery" value="<spring:message code = "anvizent.package.button.buildQuery"/>">
												<spring:message code="anvizent.package.button.buildQuery" />
												<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
											</button>
										</form>
									</c:if>
									<c:if test="${principal.isTrailUser == true}">
										<button type="button" class="btn btn-primary btn-sm" id="" disabled="disabled">
											<spring:message code="anvizent.package.button.buildQuery" />
											<img src="<c:url value="/resources/images/lock.png"/>" class="img-responsive lock-symbol" alt="Responsive image">
										</button>
									</c:if>
								</div>

								<!-- Table Preview PopUp window -->
								<div class="modal fade" tabindex="-1" role="dialog" id="tablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
									<div class="modal-dialog" style="width: 90%;">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal" aria-label="Close">
													<span aria-hidden="true">&times;</span>
												</button>
												<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
												<h4 class="modal-title custom-modal-title" id="tablePreviewPopUpHeader"></h4>
											</div>
											<div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto; overflow-x: auto;">
												<table class='tablePreview table table-striped table-bordered tablebg'></table>
											</div>
											<div class="modal-footer">
												<button type="button" class="btn btn-default" data-dismiss="modal">
													<spring:message code="anvizent.package.button.close" />
												</button>
											</div>
										</div>
									</div>
								</div>

							</div>

						</div>
						<div class="panel panel-default" id="IL_queryCommand" style="display: none;">
							<div class="panel-body"></div>
						</div>
					</div>
				</div>
			</div>
			<div id="flatFilesLocationDetails" style="display: none;">
					<div class="panel panel-info" id='flatFilesLocationPanel' style="display: none;">
						<div class="panel-heading">
							<spring:message code="anvizent.package.label.flatFileDetails" />
						</div>
						<div class="panel-body">
							<form:form method="POST" id="fileUploadForm" enctype="multipart/form-data">
								<input type="hidden" id="packageIdForFileUpload" name="packageId" />
								<input type="hidden" id="userIdForFileUpload" name="userId" />
								<input type="hidden" id="dLIdFileUpload" name="dL_Id" />
								<input type="hidden" id="iLIdForFileUpload" name="iL_Id" />
								<input type="hidden" id="industryIdForFileUpload" name="industryId" />
								<div class='row form-group '>
									<div class='col-sm-4'>
										<spring:message code="anvizent.package.label.fileType" />
									</div>
									<div class='col-sm-6'>
										<select class="form-control" id="flatFileType" name="flatFileType">
											<option value="csv" ${principal.isSandBox == false ? '' : 'disabled'}>csv</option>
											<option value="xls">xls</option>
											<option value="xlsx">xlsx</option>
										</select>
									</div>
								</div>

								<div class='row form-group delimeter-block'>
									<div class='col-sm-4'>
										<spring:message code="anvizent.package.label.delimiter" />
										:
									</div>
									<div class='col-sm-6'>
										<input type="text" class="form-control" id="delimeter" value="," name="delimeter" readonly="readonly">
									</div>
								</div>
								
								
								<div class='row form-group'>
									<div class='col-sm-4'>
										<spring:message code="anvizent.package.label.dataSource"/>:
									</div>
									<div class='col-sm-6'>
										<select  class="form-control" id="flatDataSourceName">
										<option value='0'><spring:message code="anvizent.package.label.selectDataSource"/></option>
											<c:forEach items="${allDataSourceList}" var="dataSource">
												<option value="<c:out value="${dataSource.dataSourceName}"/>"><c:out value="${dataSource.dataSourceName}"/></option>
											</c:forEach>
											<option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></option>
										</select>
									</div>
								</div>
								
								<div class='row form-group flatDataSourceOther' style="display:none">
										<div class='col-sm-4'>
										</div>
										<div class='col-sm-6'>
											<input type="text" id="flatDataSourceOtherName" class="form-control" data-minlength="1" data-maxlength="45">
										</div>
								</div>
									
								<div class='row form-group' style="display: none;">
									<div class='col-sm-4'>
										<spring:message code="anvizent.package.label.firstRowHasColumnNames" />
									</div>
									<div class='col-sm-6'>
										<div class='col-sm-2'>
											<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="true" checked="checked"> <spring:message code="anvizent.package.label.yes" /></label>
										</div>
										<div class='col-sm-2' id="firstrowcolsvalidation">
											<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="false"> <spring:message code="anvizent.package.button.no" /></label>
										</div>
									</div>
								</div>
								<div class='row form-group '>

									<div class='col-sm-4'>
										<spring:message code="anvizent.package.label.file" />

									</div>
									<div class='col-sm-6'>
										<input type="file" name="file" id="fileUpload">
									</div>
									<div class='col-sm-4'></div>
									<div class='col-sm-8'>
										<p class="help-block disclaimerNote">
											<em><spring:message code="anvizent.package.label.notePleaseMakeSureFileIsHavingHeaders" /></em>
										</p>
										<p class="help-block">
											<em><spring:message code="anvizent.package.label.noteDateTimeFormat" /> <b><c:out value="${'< yyyy-MM-dd HH:mm:ss >'}" /> </b></em>
										</p>
									</div>
								</div>
							</form:form>
						</div>
					</div>
				</div>

				<div id="webserviceNames" style="display: none;">
					<div class="row form-group">
						<div class="col-sm-2"><spring:message code="anvizent.package.label.webService"/>:</div>
						<div class="col-sm-3">
							<select class="form-control" id="existingWebServices" name="existingWebServices"> </select>
						</div>
						 <div class="col-md-1">File Type:</div>
				 			<div class="col-sm-2">
								<select id="wsfiletype" class="form-control" >
										<option value="csv">csv</option>
										<option value="json">json</option>   
										<!-- <option value="xml">xml</option> -->
								</select>
				 	      </div>
						<div class="col-sm-4">
							<button type="button" class="btn btn-primary btn-sm" id="createNewWebserviceConnection" name="createNewWebserviceConnection"><spring:message code="anvizent.package.label.CreateNewWebServiceConnection"/></button>
							<button type="button" class="btn btn-primary btn-sm" id="deleteWebserviceConnection" style="display: none" name="deleteWebserviceConnection">Delete Web Service Connection</button>
						</div>
					</div>
				</div>

                 <div class="panel panel-info" id="createNewWebservicePanel" style="display:none;">
						<div class="panel-heading">
							<spring:message code="anvizent.package.label.webserviceconnectiondetails"/> 
						</div>
						<div class="panel-body">
							
							<div class='row form-group '>
								<div class='col-sm-2'>
									 <spring:message code="anvizent.package.label.webServiceTemplate"/>:
								</div>
								<div class='col-sm-8 webServiceList'>
									<select class="form-control webServiceName" id="webServiceName">
									      <option value="0"><spring:message code="anvizent.package.label.selectWebServiceTemplate"/></option>
										  <c:forEach items="${webServiceList}" var="entry">
						                       <option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
					                      </c:forEach>
									</select>   
								</div>
							</div>	
							
							
							
							
							<div class='row form-group '>
								<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.webServiceConnectionName"/> :
								</div>
								<div class='col-sm-8'>
									<input type="text" id="webServiceConnectionName" name="webServiceConnectionName"  placeholder = "<spring:message code="anvizent.package.label.connectionName"/>" class="form-control" data-minlength="1" data-maxlength="255"> 
								</div>
							</div>
							
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.dataSource"/>:
									</div>
									<div class='col-sm-8'>
										<select  class="form-control" id="webserviceDataSourceName" name="webserviceDataSourceName">
										<option value='0'><spring:message code="anvizent.package.label.selectDataSource"/></option>
											<c:forEach items="${allDataSourceList}" var="dataSource">
												<option value="<c:out value="${dataSource.dataSourceName}"/>"><c:out value="${dataSource.dataSourceName}"/></option>
											</c:forEach>
											<option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></option>
										</select>
									</div>
							</div>
							<div class='row form-group wsDataSourceOther' style="display:none">
									<div class='col-sm-2'>
									</div>
									<div class='col-sm-8'>
										<input type="text" id="wsDataSourceOtherName" class="form-control" data-minlength="1" data-maxlength="45">
									</div>
							</div>
							<div class='row form-group '> 
								<div class='col-sm-2'>    
									 <span></span>
								</div>
								<div class='col-sm-8'>
									<input type="hidden" id="webServiceType" class="form-control">
								</div>
							</div>
							
							<div class='row form-group '> 
								<div class='col-sm-2'>    
									 <span id="authenticationTypeLable" style="display:none"><spring:message code="anvizent.package.label.authenticationType"/> :</span>
								</div>
								<div class='col-sm-8'>
									<span  id="webServiceAuthenticationType"  data-minlength="1" data-maxlength="45"> </span>
								</div>
							</div>
							<div class='row form-group' style="display:none" id="sslDisableDiv"> 
								<div class='col-sm-2'>    
									 <span id="sslDisableLabel"><spring:message code="anvizent.package.label.sslDisable"/> :</span>
								</div>
								<div class='col-sm-8'>
									<input type="checkbox"  name="sslDisable" id="sslDisable"/><span id="sslauthenticationdisable"  style="display:none;color:red"> <spring:message code="anvizent.package.message.sslauthenticationdisable"/></span>
								</div>
							</div>
							 <div class="row form-group" id="collapseLabel">
								  <label class='col-sm-2'></label>
								  <div class='col-sm-8'>
								  <div id="accordion-first" class="clearfix"><!-- standard start accordion -->
                              <div class="accordionWs" id="accordionWs2">
                           <div class="accordion-group">
							
							<div class="accordion-heading"  style="border: 1px solid #dce1e4;" style="display:none">
                              <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordionWs2" href="#wsConnDetails" aria-expanded="true">
                              <spring:message code = "anvizent.package.label.detailedInfo"/><span class="glyphicon glyphicon-plus-sign"></span>
                              </a>
                            </div>
                            
                            
                            <div style="height: 0px;margin-left:-12px;margin-top:10px" id="wsConnDetails"  class="accordion-body collapse" aria-expanded="true">
							 <div class='row form-group '>
									<div class='col-sm-2'> 
										<span id="webServiceBaseUrlLable"  style="display:none"><spring:message code="anvizent.package.label.baseUrl" /> :</span>
									</div>
									<div class='col-sm-8'>
										<input type="text" class="form-control" id="webServiceBaseUrl"  placeholder="<spring:message code="anvizent.package.label.baseUrl" />" name="wsBaseUrl" style="display:none">
									</div>
								</div>
							 <div class='row form-group sessionBasedAuthDiv' id="sessionBasedAuthDiv" style="display:none;">
							        <div class='row form-group'>
										   <div class='col-sm-2'>
											Login URL :
										   </div>                         
										   <div class='col-sm-8'>
												<input type="text" class="form-control" id="newloginUrl"  placeholder="Login URL" name="newloginUrl"  >
												<input type="hidden" id="apiAuthBodyParams">
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
											Logout URL :
										   </div>                         
										   <div class='col-sm-8'>
												<input type="text" class="form-control" id="newlogoutUrl"  placeholder="Logout URL" name="newloginUrl"  >
										   </div>
									 </div>
							 </div>
							<div id="basicAuthenticationDivs">
	                           <div class='row form-group authenticationUrl'>
									<div class='col-sm-2'> 
										<span id="webServiceAuthenticateUrlLable"  style="display:none"><spring:message code="anvizent.package.label.authenticationUrl" /> :</span>
									</div>
									<div class='col-sm-8'>
										<input type="text" class="form-control" id="webServiceAuthenticateUrl"  placeholder="<spring:message code="anvizent.package.label.authenticationUrl" />" name="wsAuthenticationUrl" style="display:none"><br>
										<input type="checkbox"  name="baseUrlRequired" id="baseUrlRequired"  style="display:none"/><span id="baseUrlRequiredLable"  style="display:none"> <spring:message code="anvizent.package.label.baseUrlRequired"/></span>
									</div>
								</div>
								<div class='row form-group'>
						 					<div class='col-sm-2'> 
												<span id="timeZoneLable"  style="display:none"><spring:message code="anvizent.package.label.timeZone" /> :</span>
											</div>
											<div class="col-sm-8">
												<select id="timeZone" name="timeZone" style="display:none" class="form-control">
													<spring:message code="anvizent.package.label.selectTimeZone" var="selectOption" />
														   <option value="0"><c:out value="${selectOption}"/></option>
															<c:forEach items="${timesZoneList}" var="timeZone">
															<option value="${timeZone.key}">${timeZone.value}</option>
															</c:forEach>
												</select>
								 			</div>
							 	 </div>
								<div class='row form-group '>
									<div class='col-sm-2'>
										 <span id="webServiceMethodTypeLable"   style="display:none"><spring:message code="anvizent.package.label.methodType" /> :</span>
									</div>
									<div class='col-sm-8'>
										<span id="webServiceMethodType" data-minlength="1" data-maxlength="150"></span>
									</div>
								</div>
							</div>
							 
							 <!--  oAuth1 start -->
							
						   <div class='row form-group oauth2AuthenticationDiv' id="oauth2AuthenticationDiv" style="display:none;">
							        <div class='row form-group'>
										   <div class='col-sm-2'>
											<spring:message code="anvizent.package.label.calBackUrl" /> :
										   </div>                         
										   <div class='col-sm-8'>
												<input type="text" class="form-control" id="callBackUrl"  placeholder="<spring:message code = "anvizent.package.label.calBackUrl"/>" name="callBackUrl"  >
												<input type="hidden" class="form-control" id="authCodeValue" name="authCodeValue"  >
												<input type="hidden" class="form-control" id="access_token" name="access_token"  >
												<input type="hidden" class="form-control" id="refresh_token" name="refresh_token"  >
										   </div>
									 </div>
								 
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code="anvizent.package.label.accessTokenUrl" /> :
										   </div>
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="accessTokenUrl"  placeholder="<spring:message code = "anvizent.package.label.accessTokenUrl"/>" name="paramName"  >
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code="anvizent.package.label.clientIdentifier" /> :
										   </div>
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="clientIdentifier"  placeholder="<spring:message code = "anvizent.package.label.clientIdentifier"/>" name="clientIdentifier"  >
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code="anvizent.package.label.clientSecret" />:
										   </div>
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="clientSecret"  placeholder="<spring:message code = "anvizent.package.label.clientSecret"/>" name="clientSecret"  >
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code="anvizent.package.label.scope" /> :
										   </div>
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="scope"  placeholder="<spring:message code = "anvizent.package.label.scope"/>" name="scope"  >
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code="anvizent.package.label.state" /> :
										   </div>
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="state"  placeholder="<spring:message code = "anvizent.package.label.state"/>" name="state"  >
										   </div>
									 </div>
									 
									  <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code="anvizent.package.label.grantType" /> :
										   </div>
										   <div class='col-sm-8' >
												<span id="grantType" data-minlength="1" data-maxlength="150"></span>
										   </div>
									 </div>
							 </div>
							
							<!--  oAuth1 end -->
								 
							<!--  oAuth1 Started -->
							
							<div class='row form-group oauth1AuthenticationDiv' id="oauth1AuthenticationDiv" style="display:none;">
							<input type="hidden" class="form-control" id="oauth1CodeValue" name="oauth1CodeValue"  >
							<input type="hidden" class="form-control" id="token" name="token">
							<input type="hidden" class="form-control" id="tokenSecret" name="tokenSecret">
							<input type="hidden" class="form-control" id="requestToken" name="requestToken">
							<input type="hidden" class="form-control" id="requestTokenSecret" name="requestTokenSecret">
							  <div class='row form-group oAuth1RequestURL'>
										  <label class='col-sm-2 control-label'><spring:message code="anvizent.package.label.requestURL"/>:</label>
										    <spring:message code="anvizent.package.label.requestURL" var="requestURL" /> 
										   <div class='col-sm-8' >
												<input type="text"  class="form-control" id="oAuth1RequestURL"  placeholder="${requestURL}"/>
										   </div>
							 </div>
							 <div class='row form-group oAuth1TokenURL'>
										  <label class='col-sm-2 control-label'><spring:message code="anvizent.package.label.tokenURL" /> :</label>
										    <spring:message code="anvizent.package.label.tokenURL" var="tokenURL" /> 
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="oAuth1TokenURL" placeholder="${tokenURL}" />
										   </div>
							 </div>
                                 <div class='row form-group oAuth1AuthURL'>
										  <label class='col-sm-2 control-label'><spring:message code="anvizent.package.label.authenticationUrl" /> :</label>
										     <spring:message code="anvizent.package.label.authenticationUrl" var="authenticationUrl" /> 
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="oAuth1AuthURL" placeholder="${authenticationUrl}"/>
										   </div>
							  </div>	
							   <div class='row form-group oAuth1CallbackURL'>
										  <label class='col-sm-2 control-label'><spring:message code="anvizent.package.label.callbackUrl" /> :</label>
										     <spring:message code="anvizent.package.label.callbackUrl" var="callbackUrl" /> 
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="oAuth1CallbackURL" placeholder="${callbackUrl}" />
										   </div>
							  </div>
							   <div class='row form-group oAuth1ConsumerKey'>
										  <label class='col-sm-2 control-label'><spring:message code="anvizent.package.label.consumerKey" /> :</label>
										  <spring:message code="anvizent.package.label.consumerKey" var="consumerKey" />  
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="oAuth1ConsumerKey" placeholder="${consumerKey}"/>
										   </div>
							  </div>
							   <div class='row form-group oAuth1ConsumerSecret'>
										  <label class='col-sm-2 control-label'><spring:message code="anvizent.package.label.consumerSecret" /> :</label>
										  <spring:message code="anvizent.package.label.consumerSecret" var="consumerSecret" /> 
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="oAuth1ConsumerSecret" placeholder="${consumerSecret}"/>
										   </div>
							  </div>
							   <div class='row form-group oAuth1SignatureMethod'>
										  <label class='col-sm-2 control-label'><spring:message code="anvizent.package.label.signatureMethod" /> :</label>
										   <div class='col-sm-8' >
												<select id="oAuth1SignatureMethod" class="form-control oAuth1SignatureMethod">
													<option value="0">Select Signature Method</option>
													<option value="HMAC-SHA1">HMAC-SHA1</option>
												</select>
										   </div>
							   </div>
							    <div class='row form-group oAuth1Version'>
										  <label class='col-sm-2 control-label'><spring:message code="anvizent.package.label.version" /> :</label>
										 <spring:message code="anvizent.package.label.version" var="oatuh1version" />  
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="oAuth1Version" placeholder="${oatuh1version}"/>
										   </div>
						   	 </div>
							    <div class='row form-group oAuth1Scope'>
										  <label class='col-sm-2 control-label'><spring:message code="anvizent.package.label.scope" /> :</label>
										 <spring:message code="anvizent.package.label.scope" var="oatuh1scope" />  
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="oAuth1Scope" placeholder="${oatuh1scope}(Optional)"/>
										   </div>
						   	 </div>
							    <div class='row form-group oAuth1Realm'>
										  <label class='col-sm-2 control-label'><spring:message code="anvizent.package.label.realm" /> :</label>
										  <spring:message code="anvizent.package.label.realm" var="realm" /> 
										   <div class='col-sm-8' >
												<input type="text" class="form-control" id="oAuth1Realm" placeholder="${realm}(Optional)"/>
										   </div>
							    </div>
							  
							  </div>
							 <!--  oAuth1 ended -->
							
							 </div>
							 </div>
							 </div>
							 </div>
							 </div>
							 </div><!-- for collapse -->
							 <div  class='row form-group basicAuthenticationDiv' id="basicAuthenticationDiv" style="display:none;">
							</div>
							 <div class='row form-group authenticationBodyParamsDiv' id="authenticationBodyParamsDiv"  style="display:none;">
							 </div>
							 <div class="row form-group paramNameValue" style="display:none;" id="authenticationParamsTemplateDiv">
								<div class="col-sm-2 requestParamHeader"></div>
								<div class="col-sm-2">
									<span class="paramName"></span>
									<span class="paramNameMandatory" style="position: inherit;">*</span>
								</div>
								<div class="col-sm-6">
									<input type="text" class="form-control paramValue" name="paramValue" placeholder="<spring:message code = "anvizent.package.label.value"/>"
									data-minlength="1" data-maxlength="45">
								</div>
							</div>
							 <div class="row form-group activeSatusBlock" style="display:none;">
								<div class='col-sm-2'><spring:message code="anvizent.package.label.activeStatus"/> :</div>
								<div class='col-sm-6'>
								 	<div class="activeStatus">
								 		 <label class="radio-inline">
											<input type="radio" name="active" value="true" checked="checked"/><spring:message code="anvizent.package.button.yes"/>
										 </label>	
										 <label class="radio-inline">
									    	<input type="radio" name="active" value="false"/><spring:message code="anvizent.package.button.no"/> 
									    </label>
								 	</div>
								</div>
							</div>
							<div class='row form-group '>
							<div class='col-sm-8'>
									<input type="button" value='<spring:message code="anvizent.package.button.validateandsave"/>' class="btn btn-primary btn-sm" id="testWebServiceAuthenticate" />
									<input type="button" id="saveNewWebserviceConnection" style="display:none;" value='<spring:message code="anvizent.package.label.saveWebServiceConnection"/>' class="btn btn-primary btn-sm" />
							</div>
							</div>
							</div>
							</div>
			<div id="webServiceDefaultMapingConnectionDetails" style="display:none">
		                <div class="panel panel-info">
							<div class="panel-heading">
								<spring:message code="anvizent.package.label.Webservice" />
							</div>
						<div class="panel-body">
				         <div id="accordion-first">
                           <div class="accordion" id="wsAuthAccordion2"  style="margin-left:15px;">
                             <div class="accordion-group">
                             <div class="accordion-heading" style="border: 1px solid #dce1e4;">
                              <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#wsAuthAccordion2" href="#wsAuthAccordion" style="font-size: 21px;">
                              <span class="glyphicon glyphicon-plus-sign"></span><spring:message code = "anvizent.package.label.viewAuthenticationDetails"/>
                              </a>
                            </div>
                            <div style="height: 0px;margin-left:-12px;margin-top:10px" id="wsAuthAccordion"  class="accordion-body collapse">
                           <div class="accordion-inner">  </div>
							<div class='row form-group'>
								<div class='col-sm-2'><spring:message code = "anvizent.package.label.authenticationType"/> :</div>
								<div class='col-sm-8 authenticationTypeDiv'>
									<input type="hidden" class="form-control" id="authenticationType" placeholder="<spring:message code = "anvizent.package.label.authenticationType"/>" name="authenticationType" disabled>
								</div>
							</div>
							
							
							<div class='row form-group'>
								<div class='col-sm-2'><spring:message code = "anvizent.package.label.dataSource"/> :</div>
								<div class='col-sm-8 dataSourceDiv'>
									<input type="hidden" class="form-control" id="dataSource_name" placeholder="<spring:message code = "anvizent.package.label.dataSource"/>" name="dataSourceName" disabled>
								</div>
							</div>
							<div class='row form-group'>
								<div class='col-sm-2'><spring:message code = "anvizent.package.label.baseUrl"/> :</div>
								<div class='col-sm-8'>
									<input type="text" class="form-control" id="baseUrlAuth" placeholder="<spring:message code = "anvizent.package.label.baseUrl"/>" name="baseUrlAuth" disabled>
								</div>
							</div>
							<div class='row form-group'>
								<div class='col-sm-2'><spring:message code = "anvizent.package.label.authenticationUrl"/> :</div>
								<div class='col-sm-8'>
									<input type="text" class="form-control" id="authenticationURL" placeholder="<spring:message code = "anvizent.package.label.authenticationUrl"/>" name="authenticationUrl" disabled>
								</div>
							</div>
							
						    <div class="row form-group loginurl" style="display:none;">
							  <div class='col-sm-2'> Login URL :</div>
							  <div class="col-sm-8">
							    <spring:message code="anvizent.package.label.enterUrl" var="enterUrl" />
								<input type="text" id="loginUrl" placeholder="<spring:message code="anvizent.package.label.enterUrl"/>" name="loginUrl" class="form-control" disabled/>
							  </div>
						    </div>
						    <div class="row form-group logouturl" style="display:none;">
							 <div class='col-sm-2'> Logout URL :</div>
							 <div class="col-sm-8">
							    <spring:message code="anvizent.package.label.enterUrl" var="enterUrl" />
								<input type="text" id="logoutUrl" placeholder="<spring:message code="anvizent.package.label.enterUrl"/>" name="logoutUrl" class="form-control" disabled/>
							 </div>
						    </div>
							 
							<div class='row form-group'>
								<div class='col-sm-2'><spring:message code = "anvizent.package.label.authenticationMethodType"/> :</div>
								<div class='col-sm-8'>
									<input type="text" class="form-control" id="authenticationMethodType" placeholder="<spring:message code = "anvizent.package.label.authenticationMethodType"/>" name="authenticationMethodType" disabled>
								</div>
							</div>
							 <div class='row form-group' id="wsOauth2Details" style="display:none">
										 <div class='row form-group'> 
										   <div class='col-sm-2'>
											<spring:message code = "anvizent.package.label.callbackUrl"/> :
										   </div>                         
										   <div class='col-sm-8'>
												<span id="wsCallBackUrl"></span>
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.accessTokenUrl"/> :
										   </div>
										   <div class='col-sm-8' >
												<span id="wsAccessTokenUrl"></span>
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.clientIdentifier"/> :
										   </div>
										   <div class='col-sm-8' >
												<span class="hideClientsSecret" >***********</span>
												<span id="wsClientIdentifier" class="showClientsSecret" style='display:none;'></span>
												<span><i class='fa fa-eye toggleClientSecrect'  aria-hidden='true'></i></span>
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.clientSecret"/> :
										   </div>
										   <div class='col-sm-8' >
												<span class="hideClientsSecret">***********</span>
												<span id="wsClientSecret" class="showClientsSecret" style='display:none;'></span>
												<span><i class='fa fa-eye toggleClientSecrect'  aria-hidden='true'></i></span>
										   </div>
									 </div>
									  <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.grantType"/> :
										   </div>
										   <div class='col-sm-8' >
												<span id="wsGrantType"></span>
										   </div>
									 </div>
									  <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.scope"/> :
										   </div>
										   <div class='col-sm-8' >
												<span id="wsScope"></span>
										   </div>
									 </div>
									  <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.state"/> :
										   </div>
										   <div class='col-sm-8' >
												<span id="wsState"></span>
										   </div>
									 </div>
									 </div>
								 <div class='row form-group' id="wsOauth1Details" style="display:none">
										 <div class='row form-group'> 
										   <div class='col-sm-2'>
											<spring:message code = "anvizent.package.label.requestURL"/> :
										   </div>                         
										   <div class='col-sm-8'>
												<span id="wsOauth1RequestURL"></span>
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.tokenURL"/> :
										   </div>
										   <div class='col-sm-8' >
												<span id="wsOauth1TokenURL"></span>
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.authenticationUrl"/> :
										   </div>
										   <div class='col-sm-8' >
												<span id="wsOauth1AuthenticationUrl"></span>
										   </div>
									 </div>
									  <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.callbackUrl"/> :
										   </div>
										   <div class='col-sm-8' >
												<span id="wsOauth1callbackURL"></span>
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.consumerKey"/> :
										   </div>
										   <div class='col-sm-8' >
												<span id="wsOauth1ConsumerKey"></span>
										   </div>
									 </div>
									 <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.consumerSecret"/> :
										   </div>
										   <div class='col-sm-8' >
												 <span id="wsOauth1ConsumerSecret"></span>
										   </div>
									 </div>
									  <div class='row form-group'>
										   <div class='col-sm-2'>
										   <spring:message code = "anvizent.package.label.signatureMethod"/> :
										   </div>
										   <div class='col-sm-8' >
												<span id="wsOauth1SignatureMethod"></span>
										   </div>
									 </div>
									 </div>	 
							<div class='row form-group authRequestParametersDiv' id="authRequestParametersDiv" style="display: none;">
									<div class="col-xs-2">
									<spring:message code = "anvizent.package.label.params"/> :</div>
									<div class="col-sm-3">
										<table class="table table-striped table-bordered tablebg " id="authenticationParamsList">
											<thead>
												<tr>
													<th><spring:message code = "anvizent.package.label.key"/></th>
													<th colspan="2"><spring:message code = "anvizent.package.label.value"/></th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
							</div>
							<div class='row form-group authRequestBodyParametersDiv' id="authRequestBodyParametersDiv" style="display: none;">
									<div class="col-xs-2">
									<spring:message code = "anvizent.package.label.requestBodyParameters"/> :</div>
									<div class="col-sm-3">
										<table class="table table-striped table-bordered tablebg " id="authenticationRequestBodyParamsList">
											<thead>
												<tr>
													<th><spring:message code = "anvizent.package.label.key"/></th>
													<th colspan="2"><spring:message code = "anvizent.package.label.value"/></th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
							</div>
							<div class='row form-group hidden'>
								<div class='col-sm-2'></div>
								<div class='col-sm-2'>
									<input type="button" value='Authenticate' class="btn btn-primary btn-sm" id="testAuthenticateWebService" />
								</div>
								<div class='col-sm-2'>
									<div id="responseStatuscode"></div>
								</div>
								</div>
							</div>
							</div>
							</div>
							</div>
							<div class='row form-group' id="requiredApiRequestParameters" style="display:none">
								<div class='col-sm-12'>
									   <fieldset id="requiredApiRequestParamsFieldSet">
										<legend> <spring:message code = "anvizent.package.label.requiredApiRequestParames"/></legend>
										<div id="requiredParamsForApiAuthenticationDiv">
											<table class="table table-striped table-bordered tablebg" id="requiredParamsForApiAuthenticationTbl">
												<thead>
													<tr>
														<th><spring:message code = "anvizent.package.label.paramName"/></th>
														<th><spring:message code = "anvizent.package.label.paramValue"/></th>
													</tr>
												</thead>
												<tbody>
													
												</tbody>
											</table>
											</div>
										</fieldset>
										<fieldset id="requiredApiBodyParamsFieldSet">
										<legend> <spring:message code = "anvizent.package.label.requiredApiBodyParames"/></legend>
										<div id="requiredBodyParamsForApiAuthenticationDiv">
											<table class="table table-striped table-bordered tablebg" id="requiredBodyParamsForApiAuthenticationTbl">
												<thead>
													<tr>
														<th><spring:message code = "anvizent.package.label.paramName"/></th>
														<th><spring:message code = "anvizent.package.label.paramValue"/></th>
													</tr>
												</thead>
												<tbody>
													
												</tbody>
											</table>
											</div>
										</fieldset>
										<fieldset id="requiredApiRequestHeadersFieldSet" style="display:none;">
										<legend> <spring:message code = "anvizent.package.label.requiredApiHeaderParams"/></legend>
										<div id="requiredHeaderParamsForApiAuthenticationDiv">
											<table class="table table-striped table-bordered tablebg " id="requiredHeaderParamsForApiAuthenticationTbl">
												<thead>
													<tr>
														<th><spring:message code = "anvizent.package.label.paramName"/></th>
														<th><spring:message code = "anvizent.package.label.paramValue"/></th>
													</tr>
												</thead>
												<tbody>
												</tbody>
											</table>
											</div>
										</fieldset>
										</div>
								</div>
								<div class="row form-group"  id="wsHeaderDetailsDiv" style="display:none">
								<div class='col-sm-2'><spring:message code="anvizent.package.label.headerDetails" /> </div>
								<div class='col-sm-10'>
									<textarea class="form-control" rows="4" id="wsHeaderDetails" placeholder="<spring:message code = "anvizent.package.label.rawHeaderExContentTypeApplicationjsonAuthorizationBasictoken"/>" disabled> </textarea>
								</div>
								
                               </div>
							 
							 <div class="row form-group">
								<div class="col-sm-offset-11">
									<button class="btn btn-primary btn-sm" id="addNewWsApi" style="display:none"><spring:message code = "anvizent.package.label.addNewApi"/></button>
								</div>
				            </div>
							 
							<div class='row form-group' id="wsdefaultApiDetails" style="display:none">
								<div class='col-sm-12'>
								<fieldset>
										<legend> <spring:message code = "anvizent.package.label.apiDetails"/></legend>
										 <div id="apiDetails" > </div>
								</fieldset>
								</div>
							</div>
							
							</div>
							</div>
							
							<div class='row form-group '>
							<div class='col-sm-2'></div> 
								<div class='col-sm-4'>
									<input type="button" id="saveWsApi" style="display: none;" value='<spring:message code = "anvizent.package.button.save"/>' class="btn btn-primary btn-sm" />
									<input type="button" id="wsFormatResponse" style="display: none;" value='<spring:message code = "anvizent.package.label.formatResponse"/>' class="btn btn-primary btn-sm" />
								</div>
							</div>
							<div class="row form-group">
								<div class="col-sm-offset-2 col-sm-10">
									 <input type="button" value="Join" id="joinWsApi" class="btn btn-primary  btn-sm joinWsApi" style="display:none" >
								</div>
							</div>
							
							</div>
			 
		<div class="row form-group">
			<div class="col-md-12">
				<input type="button" value='<spring:message code = "anvizent.package.button.mapFileHeadersUpload"/>' id='mapFileWithIL' class="btn btn-primary btn-sm" name="mapFileWithIL" style="display: none;" /> <input type="button"
					value='<spring:message code="anvizent.package.button.save"/>' id='saveILConnectionMapping' class="btn btn-primary btn-sm" style="display: none;" /> 
					<input type="button" value='<spring:message code = "anvizent.package.button.upload"/>' id='saveAndUpload' class="btn btn-primary btn-sm" style="display: none;" data-toggle="tooltip" data-placement="left"
					title="<spring:message code ="anvizent.package.label.noteSourceFileHeadersAndInputLayoutTableColumnsShouldHaveSamePositionalIdentity"/>" /> 
					<a href='<c:url value="/adt/standardpackage"/>' class="btn btn-primary back_btn btn-sm"><spring:message code="anvizent.package.link.back" /></a>
			</div>
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" id="viewSchema" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog" style="width:65%">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title" id="viewSchemaHeader"></h4>

					</div>
					<div class="modal-body" style='max-height: 500px; overflow-y: auto; overflow-x: auto;'>
						<div class="table-responsive">
							<table class="table table-striped table-bordered tablebg" id="viewSchemaTable">
								<thead>
									<tr>
										<th><spring:message code="anvizent.package.label.sNo" /></th>
										<th><spring:message code="anvizent.package.label.columnName" /></th>
										<th><spring:message code="anvizent.package.label.dataType" /></th>
										<th><spring:message code="anvizent.package.label.columnSize" /></th>
										<th><spring:message code="anvizent.package.label.pk" /></th>
										<th><spring:message code="anvizent.package.label.nn" /></th>
										<th><spring:message code="anvizent.package.label.ai" /></th>
										<th><spring:message code="anvizent.package.label.default"/></th>
									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" id="viewSourceDetailsPoUp" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="viewSourceDetailsPoUpHeader"></h4>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" id="fileMappingWithILPopUp" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title">
							<spring:message code="anvizent.package.button.mapFileHeadersUpload" />
						</h4>
					</div>
					<div class="modal-body">
						<div class="table-responsive" style="max-height: 400px;">
							<table class="table table-striped table-bordered tablebg" id="fileMappingWithILTable">
								<thead>
									<tr>
										<th><spring:message code="anvizent.package.label.sNo" /></th>
										<th class="iLName"></th>
										<th class=""><spring:message code="anvizent.package.label.dataType" /></th>
										<th><spring:message code ="anvizent.package.label.length"/></th>
										<th><spring:message code="anvizent.package.label.pk" /></th>
										<th><spring:message code="anvizent.package.label.nn" /></th>
										<th><spring:message code="anvizent.package.label.ai" /></th>
										<th class="originalFileName"></th>
										<th><spring:message code="anvizent.package.label.default" /></th>
										<%-- <th><spring:message code="anvizent.package.label.addOrDelete"/></th> --%>
									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>

						</div>
					</div>
					<div class="modal-footer">
						<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.saveMapping"/>" id="saveMappingWithIL" name='saveMappingWithIL' />
						<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.saveMapping"/>" id="saveMappingWithILForWebService" name='saveMappingWithILForWebService' style="display: none;"/>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" id="messagePopUp" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog" style="width: 500px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close close-popup" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title"></h4>
					</div>
					<div class="modal-body">
						<div id="popUpMessage" style="text-align: center;"></div>
					</div>
					<div class="modal-footer" style="text-align: center;">
						<%-- <button type="button" class="btn btn-primary btn-sm" id="addAnotherSource">
							<span class='glyphicon glyphicon-chevron-left' aria-hidden='true'></span>
							<spring:message code="anvizent.package.label.addSource" />
						</button> --%>
						<a href='<c:url value="/adt/standardpackage"/>' class="btn btn-primary btn-sm">
						<span class='glyphicon glyphicon-chevron-left' aria-hidden='true'></span>
						<spring:message code="anvizent.package.label.standardPackage" /></a> 
						<%-- <a href='<c:url value="/adt/package/schedule?packageId=${packageId_var}"/>'
							class="btn btn-primary btn-sm"
						><spring:message code="anvizent.package.label.schedule" /><span class='glyphicon glyphicon-chevron-right' aria-hidden='true'></span></a>
						 --%>
						<button type="button" class="btn btn-primary btn-sm close-popup" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" style='overflow-y: auto; max-height: 90%;' id="viewDeatilsPreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
			<div class="modal-dialog" style="width: 90%;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="viewDeatilsPreviewPopUpHeader"></h4>
					</div>
					<div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto; overflow-x: auto;">
						<table class='viewDeatilsPreview table table-striped table-bordered tablebg'></table>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default viewDetailsclosePreview" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
			</div>
		</div>
		<!-- delete IlConnection and IlConnectionMapping start-->
		<div class="modal fade" tabindex="-1" role="dialog" id="deleteIlConnection" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title">
							<spring:message code="anvizent.package.label.modalHeader.deleteIlConnection" />
						</h4>
					</div>
					<div class="modal-body">
						<p>
							<spring:message code="anvizent.package.message.deletePackage.alltheMappingsWithTheseConnectionWillBeDeleted" />
							<br>
							<spring:message code="anvizent.package.message.deletePackage.areYouSureYouWantToDeleteIlConnection" />
							<!-- &hellip; -->
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="confirmDeleteIlConnection">
							<spring:message code="anvizent.package.button.yes" />
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.no" />
						</button>
					</div>
				</div>
			</div>
		</div>
		<!-- delete IlConnection and IlConnectionMapping end-->
		<div class="modal fade" tabindex="-1" role="dialog" id="deleStandardSourceFileAlert" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">
							<spring:message code="anvizent.package.label.modalHeader.deleteSource" />
						</h4>
					</div>
					<div class="modal-body">
						<p>
							<spring:message code="anvizent.package.message.deleteSource.areYouSureYouWantToDeleteSource" />
							&hellip;
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="confirmDeleteStandardSource">
							<spring:message code="anvizent.package.button.yes" />
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.no" />
						</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>

		<div class="modal fade" tabindex="-1" role="dialog" id="downloadILTemplate" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title">
							<spring:message code="anvizent.package.label.template" />
						</h4>
					</div>
					<div class="modal-body">
						<div class="container">
							<label class="radio-inline"> <input type="radio" name="ilTemplate" id="ilCsv" checked>csv
							</label>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="confirmDownloadILTemplate">
							<spring:message code="anvizent.package.button.ok" />
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" style='overflow-y: auto; max-height: 90%;' id="viewDeatilsTablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
			<div class="modal-dialog" style="width: 90%;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="viewDeatilsTablePreviewPopUpHeader"></h4>
					</div>
					<div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto; overflow-x: auto;">
						<table class='viewDeatilsTablePreview table table-striped table-bordered tablebg'></table>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default viewDetailscloseTablePreview" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
			</div>
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="replaceAllAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code = "anvizent.package.label.replaceVariable"/></h4>
		      </div>
		      <div class="modal-body">
                <p> <spring:message code="anvizent.package.label.areyousureyouwanttoReplaceAll"/></p>
		      </div>
		      <div class="modal-footer">
		       <button type="button" class="btn btn-primary" id="confirmReplaceAll"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		
		  <div class="modal fade" tabindex="-1" role="dialog" id="joinWebServiceAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"> <spring:message code = "anvizent.package.label.webServiceConfirmation"/></h4>
		      </div>
		      <div class="modal-body">
                <p>  
	 	         <spring:message code="anvizent.package.message.editdetailswouldnotbepossiblepleaseverifythedetailsbeforeproceding"/> <br>
	 	           <spring:message code="anvizent.package.message.deletePackage.doyouwanttocontinue"/>
	 	         </p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary" id="confirmjoinWebService"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
	</div>
			
			<div class="row form-group " id="wsApiDetailsMainDiv" style="border-style: ridge;display:none;padding-top: 20px;" >
				<div class="row form-group">
					<div class="col-sm-2"><spring:message code = "anvizent.package.label.apiName"/> :</div>
					<div class="col-sm-8">
						<input type="text" class="form-control wsApiName" value="" placeholder="<spring:message code = "anvizent.package.label.apiName"/>">
					</div>
				</div>
				<%-- <div class="row form-group">
					<div class="col-sm-2"><spring:message code = "anvizent.package.label.baseUrl"/> :</div>
					<div class="col-sm-8">
						<input type="text" placeholder="Base Url" class="form-control wsApiBaseUrl">
					</div>
				</div> --%>
				<div class="row form-group">
					<div class="col-sm-2"><spring:message code = "anvizent.package.label.apiUrl"/> :</div>
					<div class="col-sm-8">
						<input type="text" placeholder="<spring:message code = "anvizent.package.label.apiUrl"/>" class="form-control wsApiUrl"><br>
						<input type="checkbox" class="wsApiBaseUrlRequired"><spring:message code = "anvizent.package.label.baseUrlRequired"/> 
					</div>
				</div>
						<div class="row-form-group">
								<div class="row form-group soapBodyElementDiv" style="display:none">
											<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.soapBodyElement"/> :</label>
											<div class="col-sm-8">
											 <textarea  name="soapBodyElement" id="soapBodyElement"  class="form-control soapBodyElement"></textarea> 
												 <%-- <form:textarea path="webServiceApis[${loop.index}].soapBodyElement" cssClass="form-control soapBodyElement"   value="${webServiceApis.soapBodyElement}"/>  --%>
												 
											</div>
										</div>
									  </div>
									  <!-- <input type="hidden" id="paginationSoapbody"> -->
									  <input type="hidden" id="defaultMapping">
				
				<div class="row form-group">
					<div class="col-sm-2"></div>
					<div class="col-sm-8">
					     <button class="btn btn-primary btn-sm getWsApiUrlPathParam" style="display:none"><spring:message code = "anvizent.package.label.getPathParam"/></button>
						 <button class="btn btn-primary btn-sm addWsRequestParams"><spring:message code = "anvizent.package.label.addRequsetParam"/></button> 
						 <button class="btn btn-primary btn-sm addWsRequestBodyParams" style="display:none" ><spring:message code = "anvizent.package.label.addRequsetBodyParam"/></button>  
					</div>
					 
				</div>
				<div class="row form-group wsApiRequestParamDiv" style="display:none"> 
					<div class="col-sm-2 requestparamsLable" ><spring:message code = "anvizent.package.label.requestparams"/> :</div>
					<div class="col-sm-10 wsApiRequestParamKeyValueDiv"  style="margin-left: -12px;display:none">
					</div>
				</div>
				<div class="row form-group wsApiBodyParamDiv" style="display:none"> 
					<div class="col-sm-2 requestparamsBodyLable" ><spring:message code = "anvizent.package.label.requestbodyparams"/> :</div>  
						<div class="col-sm-10 wsApiBodyParamKeyValueDiv" style="margin-left: -12px;display:none">
					   </div>
				</div>
				
				<div class="row form-group">
					<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.ispaginationrequired" /> :</label>
					<div class="col-sm-10">
						<label class="radio-inline"><input type="radio" name="paginationRequired"   class ="paginationRequired"  value="yes"><spring:message code="anvizent.package.button.yes"/> </label>  
						<label class="radio-inline"><input type="radio" name="paginationRequired"   class ="paginationRequired"  value="no" ><spring:message code="anvizent.package.button.no"/> </label>
					</div>
				</div>
			  <div class="row form-group paginationType" id="paginationType" style="display:none;">
						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.paginationType" />:</label>
						   <div class="col-sm-8">
						   <label class="radio-inline paginationOffsetLable">
								<input type="radio" name="paginationOffsetDateType" class="paginationOffsetDateType paginationOffset" value="offset"/>
								<spring:message code="anvizent.package.label.paginationoffset"/>
							</label>
							<label class="radio-inline paginationDateLable">
								<input type="radio"  name="paginationOffsetDateType" class="paginationOffsetDateType paginationDate" value="date"/>
								<spring:message code="anvizent.package.label.paginationdate"/>
							</label>
							<label class="radio-inline paginationIncrementalDateLable">
								<input type="radio"  name="paginationOffsetDateType" class="paginationOffsetDateType paginationIncrementalDate" value="incrementaldate"/>
								<spring:message code="anvizent.package.label.incrementalpaginationdate"/>
							</label>
							<label class="radio-inline paginationHypermediaLable">
								<input type="radio" name ="paginationOffsetDateType"  class="paginationOffsetDateType paginationHypermedia" value="hypermedia"/>
								<spring:message code="anvizent.package.label.pagination.nextpage"/>
							</label>
							<label class="radio-inline paginationConditionalDateLable">
								<input type="radio" name="paginationOffsetDateType" class="paginationOffsetDateType paginationConditionalDate" value="conditionaldate"/>
								<spring:message code="anvizent.package.label.pagination.conditionaldate"/>
							</label>
							<label class="radio-inline paginationPageNumberSizeLable">
								<input type="radio" name="paginationOffsetDateType" class="paginationOffsetDateType paginationPageNumberSize" value="page"/>
								<spring:message code="anvizent.package.label.pagination.page"/>
							</label>
							</div>
							<div class="col-sm-2">
							<select class="paginationParamType form-control">
								<option value="Request Parameter"><spring:message code="anvizent.package.message.RequestParameter" /></option>
								<option value="Body Parameter" class="paginationBodyParamType"> <spring:message code="anvizent.package.message.BodyParameter" /></option>
						    </select>
					        </div>
					</div>
					<div class="row form-group paginationHypermediaType" id="paginationHypermediaType" style="display:none">
							    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagination.nextpage.pattern.and.Limit"/> :</label>
								 <div class="col-sm-5">
									 <input type="text" placeholder="<spring:message code = "anvizent.package.label.placeholder.hyperlink.next.record.pattern"/>" title ="<spring:message code = "anvizent.package.label.placeholder.hyperlink.next.record.pattern"/>"  class="form-control paginationHyperLinkPattern"/>
								 </div>
								  <div class="col-sm-5">
									 <input type="text" placeholder="limit" title ="limit"  class="form-control paginationHypermediaPageLimit"/>
								 </div>
					</div>
					<div class="row form-group paginationDateType" id="paginationDateType" style="display:none">
						    <div class="row form-group">
						     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.datepagination"/> :</label>
							 <div class="col-sm-2">
								 <input type="text" placeholder="From Date Param Name" title ="From Date Param Name" class="form-control paginationStartDateParam"/>
							 </div>
							  <div class="col-sm-3">
						     	<input type="text" placeholder="Starts From" title ="start date" class="form-control paginationStartDate" >
						     </div>
							  <div class="col-sm-2">
								 <input type="text" placeholder="To Date Param Name" title ="To Date Param Name" class="form-control paginationEndDateParam"/>
							 </div>
						     <div class="col-sm-3">
						       <select class="paginationDateRange col-sm-4 form-control"  title="date range">
								  <c:forEach var="dr" begin="0" end="364">
                                       <option value="${dr+1}">${dr+1}</option>
                                   </c:forEach>
							   </select>
							</div>
							</div>
					  <div class="row form-group">
					     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
						 <div class="col-sm-3">
							 <input type="text" placeholder="Param Name(optional)" title ="Param Name(optional)" class="form-control paginationDatePageNumberRequestParamName"/>
						 </div>
						 <div class="col-sm-1">
						 :
						 </div>
					     <div class="col-sm-3">
					     	<input type="text" placeholder="Starts From(optional)" title ="Starts From(optional)" class="form-control paginationDatePageNumberRequestParamValue" >
					    </div>
			         </div>
					 <div class="row form-group">
					  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
						 <div class="col-sm-3">
							 <input type="text" placeholder="Param Name(optional)"  title ="Param Name(optional)" class="form-control paginationDatePageSizeRequestParamName"/>
						 </div>
						 <div class="col-sm-1">
						 :
						 </div>
					     <div class="col-sm-3">
					     	<input type="text" placeholder="Param Value(optional)" title ="Param Value(optional)" class="form-control paginationDatePageSizeRequestParamValue" >
					     </div>
					 </div>
				   </div>
				   <div class="row form-group paginationIncrementalDateType" id="paginationIncrementalDateType" style="display:none">
					     <div class="row form-group">
					     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.incrementalpaginationdatepagination"/> :</label>
						 <div class="col-sm-2">
							 <input type="text" placeholder="Start Date Param Name" title ="Start Date Param Name" class="form-control paginationIncrementalStartDateParam"/>
						 </div>
						  <div class="col-sm-2">
					     	<input type="text" placeholder="Start Date" title ="Start Date" class="form-control paginationIncrementalStartDate" >
					     </div>
						  <div class="col-sm-2">
							 <input type="text" placeholder="End Date Param Name" title ="End Date Param Name" class="form-control paginationIncrementalEndDateParam"/>
						 </div>
					     <div class="col-sm-2">
					     	<input type="text" placeholder="End Date(optional)" title ="End Date should be grater than Start date(Day range must be more than 7 days)." class="form-control paginationIncrementalEndDate" >
					     </div>
					      <div class="col-sm-2">
						     <select class="paginationIncrementalDateRange col-sm-4 form-control"  title="day range">
									  <c:forEach var="dr" begin="0" end="364">
					                     <option value="${dr+1}">${dr+1}</option>
					                  </c:forEach>
							 </select>
					     </div>
					     </div>
					     <div class="row form-group">
					     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
						 <div class="col-sm-3">
							 <input type="text" placeholder="Param Name(optional)" title ="Param Name(optional)" class="form-control paginationIncrementalDatePageNumberRequestParamName"/>
						 </div>
						 <div class="col-sm-1">
						 :
						 </div>
					     <div class="col-sm-3">
					     	<input type="text" placeholder="Starts From(optional)" title ="Starts From(optional)" class="form-control paginationIncrementalDatePageNumberRequestParamValue" >
					</div>
			        </div>
					 <div class="row form-group">
					  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
						 <div class="col-sm-3">
							 <input type="text" placeholder="Param Name(optional)"  title ="Param Name(optional)" class="form-control paginationIncrementalDatePageSizeRequestParamName"/>
						 </div>
						 <div class="col-sm-1">
						 :
						 </div>
					     <div class="col-sm-3">
					     	<input type="text" placeholder="Param Value(optional)" title ="Param Value(optional)" class="form-control paginationIncrementalDatePageSizeRequestParamValue" >
					     </div>
					  </div>
					</div>
					
					<div class="row form-group paginationConditionalType" id="paginationConditionalDate" style="display: none">
							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.pagination.filtercondition"/>:</label>
								<div class="col-sm-3">
									<input type="text" placeholder="{ $filter }" class="form-control paginationFilterName">
								</div>
								<div class="col-sm-3">
									<select class="paginationConditionalDayRange col-sm-4 form-control"  title="date range">
										 <option value="0"><spring:message code="anvizent.package.label.pagination.selectDayRange"/></option>
									  <c:forEach var="dr" begin="1" end="365">
			                             <option value="${dr}">${dr}</option>
			                           </c:forEach>
							  		</select>
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-2"></label>
								<div class="col-sm-3">
									<input type="text" placeholder="From date param" class="form-control paginationConditionalFromDateParam">
								</div>
								<div class="col-sm-2">
									<input type="text" placeholder="{EX: >, <, =, ge, le }" class="form-control paginationConditionalFromDateCondition">
								</div>
								<div class="col-sm-3">
									<input type="text" placeholder="From date" class="form-control paginationConditionalFromDate">
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-2"></label>
								<div class="col-sm-3">
									<input type="text" placeholder="{ $condition Ex: { and, or } }" class="form-control paginationConditionalParam">
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-2"></label>
								<div class="col-sm-3">
									<input type="text" placeholder="To date param" class="form-control paginationConditionalToDateParam">
								</div>
								<div class="col-sm-2">
									<input type="text" placeholder="{ EX: >, <, =, ge, le }" class="form-control paginationConditionalToDateCondition">
								</div>
								<div class="col-sm-3">
									<input type="text" placeholder="To date(optional)" class="form-control paginationConditionalToDate">
								</div>
							</div>
						</div>
					 
				<div class="row form-group paginationOffSetType" id="paginationOffSetType" style="display:none">
				  <div class="row form-group">
				     <div class="col-sm-2"><spring:message code = "anvizent.package.label.offsetparamnameandstartsfrom"/> :</div>
					 <div class="col-sm-3">
						 <input type="text" placeholder="<spring:message code="anvizent.package.label.paramName" />"  class="form-control paginationOffSetRequestParamName"/>
					 </div>
					 <div class="col-sm-1">
					 :
					 </div>
				     <div class="col-sm-3">
				     	<input type="text" placeholder="<spring:message code = "anvizent.package.label.startsFrom"/>" class="form-control paginationOffSetRequestParamValue" >
				     </div>
				  </div>
				  <div class="row form-group">
				  <div class="col-sm-2"><spring:message code = "anvizent.package.label.limitparamnameandvalue"/> :</div>
					 <div class="col-sm-3">
						 <input type="text" placeholder="<spring:message code="anvizent.package.label.paramName" />"  class="form-control paginationLimitRequestParamName"/>
					 </div>
					 <div class="col-sm-1">
					 :
					 </div>
				     <div class="col-sm-3">
				     	<input type="text" placeholder="<spring:message code="anvizent.package.label.paramValue" />" class="form-control paginationLimitRequestParamValue" >
				     </div>
				  </div>
				<div class="row form-group">
					<label class="control-label col-sm-2"><spring:message
							code="anvizent.package.label.paginationObjectName" /> :</label>
					<div class="col-sm-8">
						<input type="text" placeholder="Pagination Object Name"
							title="param name" class="form-control paginationObjectName" />
					</div>
				</div>

				<div class="row form-group">
					<label class="control-label col-sm-2"><spring:message
							code="anvizent.package.label.paginationSearchId" /> :</label>
					<div class="col-sm-8">
						<input type="text" placeholder="Pagination SearchId Param"
							title="param name" class="form-control paginationSearchId" />
					</div>
				</div>
				    <div class="row-form-group">
						  <label class="control-label col-sm-2"><spring:message code="anvizent.package.label.PaginationSoapBody"/> </label>
								<div class="col-sm-8">
									<textarea  class="form-control PaginationSoapBody"  placeholder="Enter Pagination Soap Body "></textarea> 
							</div>
				 </div>
				
			</div>
				 <div class="row form-group paginationPageNumberSizeType" id="paginationPageNumberSizeType" style="display:none">
				  <div class="row form-group">
				     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
					 <div class="col-sm-3">
						 <input type="text" placeholder="Param Name" title ="Param Name" class="form-control paginationPageNumberRequestParamName"/>
					 </div>
					 <div class="col-sm-1">
					 :
					 </div>
				     <div class="col-sm-3">
				     	<input type="text" placeholder="Starts From" title ="Starts From" class="form-control paginationPageNumberRequestParamValue" >
				     </div>
				  </div>
				  <div class="row form-group">
				  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
					 <div class="col-sm-3">
						 <input type="text" placeholder="Param Name"  title ="Param Name" class="form-control paginationPageSizeRequestParamName"/>
					 </div>
					 <div class="col-sm-1">
					 :
					 </div>
				     <div class="col-sm-3">
				     	<input type="text" placeholder="Param Value" title ="Param Value" class="form-control paginationPageSizeRequestParamValue" >
				     </div>
				  </div>
				</div>
				<div class="row form-group">
					<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.incrementalUpdate" /> :</label>
					<div class="col-sm-10">
						<input type="checkbox" class="incrementalUpdate">
					</div>
				</div>
				
				<div class="row form-group incrementalUpdateDetailsDiv" style="display: none;">
					<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.incrementalUpdateDetails" /> :</label>
					<div class="row form-group">
						 <div class="col-sm-3">
						 <input type="hidden" class="incrementalUpdateparamdata form-control">
						 <input type="hidden" class="incrementalUpdateParamColumnName form-control" placeholder="<spring:message code="anvizent.package.label.paramName" />">
						 <input type="text" class="incrementalUpdateParamName form-control" placeholder="<spring:message code="anvizent.package.label.paramName" />">
						 </div>
						 <div class="col-sm-3">
						 <input type="text" class="incrementalUpdateParamvalue form-control" placeholder="Ex: /* {date} */">
						 </div>	
						 <div class="col-sm-4">
						 <select class="incrementalUpdateParamType form-control">
									<option value="Request Parameter"><spring:message code="anvizent.package.label.requestParameter"/></option>
									<option class="incrementalUpdateBodyParamType" value="Body Parameter"><spring:message code="anvizent.package.label.bodyParameter"/></option>
								</select>
						 </div>	
					</div>	
				</div>
			 
				<div class="row form-group">
					<div class="col-sm-2"><spring:message code = "anvizent.package.label.apiMethodType"/> :</div>
					<div class="col-sm-8">
						<label class="radio-inline">
							<input type="radio" name="methodTypeAuthSelection" value="GET" class="wsApiMethodType" ><spring:message code = "anvizent.package.label.get"/> 
						 </label>
						 <label class="radio-inline">
						 	<input type="radio" name="methodTypeAuthSelection" value="POST" class="wsApiMethodType" ><spring:message code = "anvizent.package.label.post"/> 
						 </label>
					</div>
				</div>
				<div class="row form-group" style="display:none">
					<div class="col-sm-2"><spring:message code = "anvizent.package.label.apiResponseColumnObjectname"/> :</div>
					<div class="col-sm-8">
						<input type="text" value="" placeholder="<spring:message code = "anvizent.package.label.apiResponseColumnObjectname"/>" class="form-control wsApiResponseColumnObjName">
					</div>
				</div>
				<div class="row form-group">
					<div class="col-sm-2"><spring:message code = "anvizent.package.label.apiResponseObjectname"/> :</div>
					<div class="col-sm-8">
						<input type="text" value="" placeholder="<spring:message code = "anvizent.package.label.apiResponseObjectname"/>" class="form-control wsApiResponseObjName">
					</div>
				</div>
				<input type="hidden" id="webserviceType">
				<div class="row form-group wsManualSubUrlMainDiv" style="border: 1px solid rgba(16, 0, 255, 0.29);margin:2px;">
					<h3><spring:message code = "anvizent.package.label.pathParamDetails"/></h3>
				</div>
				<div class="row form-group multipleWebSerivceValidate" id="multipleWebSerivceValidate" >
					<div class="col-sm-offset-2 col-md-4">
						<input type="button" value="<spring:message code = "anvizent.package.button.validate"/>" class="btn btn-primary btn-sm validateWsApi" > 
						<input type="button" value="<spring:message code = "anvizent.package.button.preview"/>" class="btn btn-primary  btn-sm previewWsApi" style="display: none" >
					</div>
					<div class="col-md-3">
						 <div   class="alert alert-success verifiedStatus" style="display:none"></div>
						 <div   class="alert alert-danger verifiedStatusError" style="display:none"></div>
					</div>
					<div class="col-md-3 addDeleteButton">
						<button class="btn btn-primary btn-sm addNewApi" style="display: none" ><spring:message code = "anvizent.package.label.addNewApi"/></button>
						<button class="btn btn-primary btn-sm deleteAddedNewApi"><span class="glyphicon glyphicon-trash"></span></button>
					</div>
				</div>
			</div>
		</div>	 
			<div class="row form-group" id="wsManualSubUrlMainDivTemplate" style="display:none;margin: 10px;border: 1px solid rgba(154, 76, 9, 0.45)">
						<h3 class="pathVariableSno"></h3>
						<div class="row form-group" >
								<div class="col-sm-2"><spring:message code = "anvizent.package.label.paramName"/> :</div>
								<div class="col-sm-8">
									<span id="wsUrlPathParam" class="wsUrlPathParam"></span>
								</div>
						</div>
							
						<div class="row form-group" >
								<div class="col-sm-2"><spring:message code = "anvizent.package.label.paramValueFrom"/> :</div>
								<div class="col-sm-8">
									<label class="radio-inline">
										<input type="radio" name="typeSelection0" value="M" class="manualPathParamVal pathParamValType" ><spring:message code = "anvizent.package.label.manual"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name="typeSelection0" value="S" class="defaultSubUrl pathParamValType" ><spring:message code = "anvizent.package.label.subUrl"/>
									</label>
								</div>
						</div>
						
						<div class="row form-group wsManualParamValDiv" style="display:none;">
							<div class="row form-group" >
								<div class="col-sm-2"><spring:message code = "anvizent.package.label.paramValue"/> :</div>
								<div class="col-sm-8">
									<input type="text" placeholder="<spring:message code = "anvizent.package.label.paramValue"/>" class="form-control manualParamValue">
								</div>
							</div>
						</div>
						<div class="row form-group wsSubUrlApiDiv" style="display: none;" >
							<div class="row form-group">
								<div class="col-sm-2"><spring:message code = "anvizent.package.label.subApiUrl"/> :</div>
								<div class="col-sm-8">
									<input type="text" placeholder="<spring:message code = "anvizent.package.label.subApiUrl"/>" value="" class="form-control wsSubApiUrl" >
									<br>
						            <input type="checkbox" class="wsSubApiBaseUrlRequired"> <spring:message code = "anvizent.package.label.baseUrlRequired"/>
								</div>
							</div>
							<div class="row form-group">
								<div class="col-sm-2"><spring:message code = "anvizent.package.label.subApiMethodType"/> :</div>
								<div class="col-sm-8">
									<label class="radio-inline">
										<input type="radio" name="wsSubApiMethodType0" value="GET" class="wsSubApiMethodType" ><spring:message code = "anvizent.package.label.get"/> 
									</label>
									<label class="radio-inline">
										<input type="radio" name="wsSubApiMethodType0" value="POST" class="wsSubApiMethodType"><spring:message code = "anvizent.package.label.post"/>
									</label>
								</div>
							</div>
							
			                 <div class="row form-group">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.ispaginationrequired" /> :</label>
								<div class="col-sm-8">
									<label class="radio-inline"><input type="radio" name="subUrlPaginationRequired"   class ="subUrlPaginationRequired"  value="yes"><spring:message code="anvizent.package.button.yes"/> </label>  
									<label class="radio-inline"><input type="radio" name="subUrlPaginationRequired"   class ="subUrlPaginationRequired"  value="no" ><spring:message code="anvizent.package.button.no"/> </label>
								</div>
							 </div>
							
							<div class="row form-group subUrlPaginationType" style="display:none;">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.paginationType" />:</label>
							<div class="col-sm-8">
								   <label class="radio-inline">
										<input type="radio"  name = "subUrlPaginationOffsetDateType" class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationOffset" value="offset"/>
										<spring:message code="anvizent.package.label.paginationoffset"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name ="subUrlPaginationOffsetDateType"  class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationDate" value="date"/>
										<spring:message code="anvizent.package.label.paginationdate"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name ="subUrlPaginationOffsetDateType"  class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationIncrementalDate" value="incrementaldate"/>
										<spring:message code="anvizent.package.label.incrementalpaginationdate"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name ="subUrlPaginationOffsetDateType"  class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationHypermedia" value="hypermedia"/>
										<spring:message code="anvizent.package.label.pagination.nextpage"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name ="subUrlPaginationOffsetDateType" class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationConditionalDate" value="conditionaldate"/>
										<spring:message code="anvizent.package.label.pagination.conditionaldate"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name ="subUrlPaginationOffsetDateType" class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationPageNumberSize" value="page"/>
										<spring:message code="anvizent.package.label.pagination.page"/>
									</label>
									</div>
									 <div class="col-sm-2">
									<select class="subUrlPaginationParamType form-control">
										<option value="Request Parameter"><spring:message code="anvizent.package.message.RequestParameter" /></option>
										<option value="Body Parameter"><spring:message code="anvizent.package.message.BodyParameter" /></option>
								    </select>
							        </div>
	                        </div>
							<div class="row form-group subUrlPaginationHypermediaType" id="subUrlPaginationHypermediaType" style="display:none">
							    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagination.nextpage.pattern.and.Limit"/> :</label>
								 <div class="col-sm-5">
									 <input type="text" placeholder="<spring:message code = "anvizent.package.label.placeholder.hyperlink.next.record.pattern"/>" title ="<spring:message code = "anvizent.package.label.placeholder.hyperlink.next.record.pattern"/>"  class="form-control subUrlPaginationHyperLinkPattern"/>
								 </div>
								  <div class="col-sm-5">
									 <input type="text" placeholder="limit" title ="limit"  class="form-control subUrlPaginationHypermediaPageLimit"/>
								 </div>
							  </div>
							<div class="row form-group subUrlPaginationDateType" id="subUrlPaginationDateType" style="display:none">
							    <div class="row form-group">
							    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.datepagination"/> :</label>
								 <div class="col-sm-2">
									 <input type="text" placeholder="From Date Param Name" title ="From Date Param Name"  class="form-control subUrlPaginationStartDateParam"/>
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Starts From" title ="start date" class="form-control subUrlPaginationStartDate" >
							     </div>
							      <div class="col-sm-2">
									 <input type="text" placeholder="To Date Param Name" title ="To Date Param Name"  class="form-control subUrlPaginationEndDateParam"/>
								 </div>
							      <div class="col-sm-3">
							     <select class="subUrlPaginationDateRange col-sm-4 form-control" title="day range">
										<c:forEach var="dr" begin="0" end="364">
					                        <option value="${dr+1}">${dr+1}</option>
					                    </c:forEach>
								 </select>
								</div>
								</div>
								 <div class="row form-group">
								    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
									 <div class="col-sm-3">
										 <input type="text" placeholder="Param Name"  class="form-control subUrlDatePaginationPageNumberRequestParamName"/>
									 </div>
									 <div class="col-sm-1">
									 :
									 </div>
								     <div class="col-sm-3">
								     	<input type="text" placeholder="Starts From" class="form-control subUrlDatePaginationPageNumberRequestParamValue" >
								     </div>
						      </div>
							  <div class="row form-group">
							    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
								 <div class="col-sm-3">
									 <input type="text" placeholder="Param Name"  class="form-control subUrlDatePaginationPageSizeRequestParamName"/>
								 </div>
								 <div class="col-sm-1">
								 :
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Param Value" class="form-control subUrlDatePaginationPageSizeRequestParamValue" >
							     </div>
							  </div>
							  </div>
						  <div class="row form-group subUrlPaginationIncrementalDateType" id="subUrlPaginationIncrementalDateType" style="display:none">
							    <div class="row form-group">
							    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.incrementalpaginationdatepagination"/> :</label>
								 <div class="col-sm-2">
									 <input type="text" placeholder="From Date Param Name" title ="From Date Param Name"  class="form-control subUrlPaginationIncrementalStartDateParam"/>
								 </div>
								 <div class="col-sm-2">
							     	<input type="text" placeholder="Starts From" title ="start date" class="form-control subUrlPaginationIncrementalStartDate" >
							     </div>
								  <div class="col-sm-2">
									 <input type="text" placeholder="To Date Param Name" title ="To Date Param Name"  class="form-control subUrlPaginationIncrementalEndDateParam"/>
								 </div>
								  <div class="col-sm-2">
							     	<input type="text" placeholder="End Date(optional)" title ="End date" title ="End Date should be grater than Start date(Date range must be more than 7 days)." class="form-control subUrlPaginationIncrementalEndDate" >
							     </div>
							     <div class="col-sm-2">
							     <select class="subUrlPaginationIncrementalDateRange col-sm-4 form-control" title="day range">
										<c:forEach var="dr" begin="0" end="364">
					                       <option value="${dr+1}">${dr+1}</option>
					                     </c:forEach>
								 </select>
								</div>
								</div>
								<div class="row form-group">
								     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
									 <div class="col-sm-3">
										 <input type="text" placeholder="Param Name(optional)" title ="Param Name(optional)" class="form-control subUrlPaginationIncrementalDatePageNumberRequestParamName"/>
									 </div>
									 <div class="col-sm-1">
									 :
									 </div>
								     <div class="col-sm-3">
								     	<input type="text" placeholder="Starts From(optional)" title ="Starts From(optional)" class="form-control subUrlPaginationIncrementalDatePageNumberRequestParamValue" >
								</div>
				        </div>
						 <div class="row form-group">
						  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
							 <div class="col-sm-3">
								 <input type="text" placeholder="Param Name(optional)"  title ="Param Name(optional)" class="form-control subUrlPaginationIncrementalDatePageSizeRequestParamName"/>
							 </div>
							 <div class="col-sm-1">
							 :
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Param Value(optional)" title ="Param Value(optional)" class="form-control subUrlPaginationIncrementalDatePageSizeRequestParamValue" >
						     </div>
						  </div>
							</div>
							<div class="row form-group subUrlPaginationOffSetType" id="subUrlPaginationOffSetType" style="display:none">
							  <div class="row form-group">
							     <div class="col-sm-2"><spring:message code = "anvizent.package.label.offsetparamnameandstartsfrom"/> :</div>
								 <div class="col-sm-3">
									 <input type="text" placeholder="Param Name"  class="form-control subUrlPaginationOffSetRequestParamName"/>
								 </div>
								 <div class="col-sm-1">
								 :
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Starts From" class="form-control subUrlPaginationOffSetRequestParamValue" >
							     </div>
							  </div>
							  <div class="row form-group">
							  <div class="col-sm-2"><spring:message code = "anvizent.package.label.limitparamnameandvalue"/> :</div>
								 <div class="col-sm-3">
									 <input type="text" placeholder="Param Name"  class="form-control subUrlPaginationLimitRequestParamName"/>
								 </div>
								 <div class="col-sm-1">
								 :
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Param Value" class="form-control subUrlPaginationLimitRequestParamValue" >
							     </div>
							  </div>
							 </div>
					 <div class="row form-group subUrlPaginationPageNumberSizeType" id="subUrlPaginationPageNumberSizeType" style="display:none">
						  <div class="row form-group">
						    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
							 <div class="col-sm-3">
								 <input type="text" placeholder="Param Name(Optional)"  class="form-control subUrlPaginationPageNumberRequestParamName"/>
							 </div>
							 <div class="col-sm-1">
							 :
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Starts From" class="form-control subUrlPaginationPageNumberRequestParamValue" >
						     </div>
						  </div>
						  <div class="row form-group">
						    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
							 <div class="col-sm-3">
								 <input type="text" placeholder="Param Name"  class="form-control subUrlPaginationPageSizeRequestParamName"/>
							 </div>
							 <div class="col-sm-1">
							 :
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Param Value" class="form-control subUrlPaginationPageSizeRequestParamValue" >
						     </div>
						  </div>
						</div>
							 
							 <div class="row form-group subUrlPaginationConditionalType" id="subUrlPaginationConditionalDate" style="display: none">
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.pagination.filtercondition"/>:</label>
									<div class="col-sm-3">
										<input type="text" placeholder="{ $filter }" class="form-control subUrlPaginationFilterName">
									</div>
									<div class="col-sm-3">
										<select class="subUrlPaginationConditionalDayRange col-sm-4 form-control"  title="date range">
											 <option value="0"><spring:message code="anvizent.package.label.pagination.selectDayRange"/></option>
										  <c:forEach var="dr" begin="1" end="365">
				                             <option value="${dr}">${dr}</option>
				                           </c:forEach>
								  		</select>
									</div>
								</div>
								<div class="row form-group">
									<label class="control-label col-sm-2"></label>
									<div class="col-sm-3">
										<input type="text" placeholder="From date param" class="form-control subUrlPaginationConditionalFromDateParam">
									</div>
									<div class="col-sm-2">
										<input type="text" placeholder="{EX: >, <, =, ge, le }" class="form-control subUrlPaginationConditionalFromDateCondition">
									</div>
									<div class="col-sm-3">
										<input type="text" placeholder="From date" class="form-control subUrlPaginationConditionalFromDate">
									</div>
								</div>
								<div class="row form-group">
									<label class="control-label col-sm-2"></label>
									<div class="col-sm-3">
										<input type="text" placeholder="{ $condition Ex: { and, or } }" class="form-control subUrlPaginationConditionalParam">
									</div>
								</div>
								<div class="row form-group">
									<label class="control-label col-sm-2"></label>
									<div class="col-sm-3">
										<input type="text" placeholder="To date param" class="form-control subUrlPaginationConditionalToDateParam">
									</div>
									<div class="col-sm-2">
										<input type="text" placeholder="{ EX: >, <, =, ge, le }" class="form-control subUrlPaginationConditionalToDateCondition">
									</div>
									<div class="col-sm-3">
										<input type="text" placeholder="To date(optional)" class="form-control subUrlPaginationConditionalToDate">
									</div>
								</div>
							</div>
							
							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.incrementalUpdate" />:</label>
								<div class="col-sm-10">
									<input type="checkbox" name="subUrlIncrementalUpdate"  class="subUrlIncrementalUpdate">
								</div>
							</div>
				
							<div class="row form-group subUrlIncrementalUpdateDetailsDiv" style="display: none;">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.incrementalUpdateDetails" /> :</label>
								<div class="row form-group">
									 <div class="col-sm-3">
									 <input type="hidden" class="subUrlIncrementalUpdateParamColumnName form-control" placeholder="<spring:message code="anvizent.package.label.paramName" />">
									 <input type="text" class="subUrlIncrementalUpdateParamName form-control" placeholder="<spring:message code="anvizent.package.label.paramName" />">
									 </div>
									 <div class="col-sm-3">
									 <input type="text" class="subUrlIncrementalUpdateParamvalue form-control" placeholder="Ex: /* {date} */">
									 </div>	
									 <div class="col-sm-4">
									 <select class="subUrlIncrementalUpdateParamType form-control">
												<option value="Request Parameter"><spring:message code="anvizent.package.label.requestParameter"/></option>
												<option class="subUrlIncrementalUpdateBodyParamType" value="Body Parameter"><spring:message code="anvizent.package.label.bodyParameter"/></option>
											</select>
									 </div>	
								</div>	
							</div>
							
							<div class="row form-group">
								<div class="col-sm-2"><spring:message code = "anvizent.package.label.subApiResponseObjectName"/> :</div>
								<div class="col-sm-8">
									<input type="text" placeholder="<spring:message code = "anvizent.package.label.subApiResponseObjectName"/>" value="" class="form-control wsSubApiResponseObjName">
								</div>
							</div>
						</div>
					</div>
				 
				 <div class="row form-group" id="wsApiRequestParam" style="display:none">
				 <div class="col-sm-4">
				 <input type="text" placeholder="<spring:message code = "anvizent.package.label.paramName"/>"  class="form-control wsApiRequestParamName"/>
				 <span class="paramNameMandatory" >*</span>
				 </div>
				 <div class="col-sm-1">
				 :
				 </div>
			     <div class="col-sm-4">
			     <input type="text" placeholder="<spring:message code = "anvizent.package.label.paramValue"/>" class="form-control wsApiRequestParamValue" >
			     </div>
			     <div class="col-sm-2">
			      <button class="btn btn-primary btn-sm deleteAddWsRequestParams" id="deleteAddWsRequestParams"><span class="glyphicon glyphicon-trash"></span></button>
			     </div>
				 </div>
						 
				 <div class="row form-group dbSchemaSelection" id="dbSchemaSelectionDivForSqlServer" style="display:none"> 
				     <div class="col-sm-2 labelForDbAndSchemaName"><spring:message code = "anvizent.package.label.databaseandschema"/> :</div> 
					 <div class="col-sm-2"> 
						<select class="form-control dbVariable"  id="dbVariable"> 
						<option value="{db0}"><spring:message code = "anvizent.package.label.selectdbvariable"/></option>
					 </select>
					 </div> 
					 <div class="col-sm-2"> 
						 <select class="form-control dbName" id="dbName"> 
						 <option value="{dbName}"><spring:message code = "anvizent.package.label.selectdbname"/></option> 
					</select>
					</div> 
					<div class="col-sm-2"> 
						 <select class="form-control schemaVariable" id="schemaVariable"> 
						 <option value="{schema0}"><spring:message code = "anvizent.package.label.selectschemavariable"/></option> 
					    </select>
					</div> 
					<div class="col-sm-2"> 
						<select  class="form-control schemaName" id="schemaName"> 
						<option value="{schemaName}"><spring:message code = "anvizent.package.label.selectschemaname"/></option> 
					    </select> 
					</div>
					<div class="col-sm-2"> 
						<button class="btn btn-primary btn-sm addDbSchema"> <i class="fa fa-plus" aria-hidden="true"></i> </button>
						 &nbsp;<button  class="btn btn-primary btn-sm deleteDbSchema" style="display:none"><span class="glyphicon glyphicon-trash"  aria-hidden="true"></span></button>
					</div> 
				</div>
				<div class="row form-group dbSchemaSelection" id="dbSchemaSelectionDivForNotSqlServer"  style="display:none"> 
						<div class="col-sm-2 labelForDbAndSchemaName"><spring:message code = "anvizent.package.label.databaseandschema"/> :</div> 
						<div class="col-sm-2"> 
							<select class="form-control schemaVariable" id="schemaVariable"> 
							 <option value="{schema0}"><spring:message code = "anvizent.package.label.selectschemavariable"/></option> 
						   </select> 
						</div>
						<div class="col-sm-2"> 
						 <select class="form-control dbName" id="dbName"> 
						 <option value="{dbName}"><spring:message code = "anvizent.package.label.selectschemaname"/></option> 
					    </select> 
					    </div> 
						<div class="col-sm-2"> 
							<button class="btn btn-primary btn-sm addDbSchema"> <i class="fa fa-plus" aria-hidden="true"></i> </button> 
							&nbsp;<button  class="btn btn-primary btn-sm deleteDbSchema"  style="display:none"><span class="glyphicon glyphicon-trash"></span></button>
						</div>
			  </div>
               <div class='row form-group pathParamNameValue' id="pathParamNameValueDiv" style="display:none"> 
					 <div class='col-sm-2' id="pathParametersLable"><spring:message code = "anvizent.package.label.pathParameters"/></div>
					 <div class='col-sm-2'> 
					 <span class='pathParamName'></span>
					 </div> 
					 <div class='col-sm-6'> 
					 <input type="text" class="form-control pathParamValue"  placeholder = "<spring:message code = "anvizent.package.label.value"/>"  data-minlength="1" data-maxlength="45"> 
					 </div> 
			   </div>
			     <div class='row form-group headerKeyValue' id="headerKeyValueDiv" style="display:none" > 
	 		    	 <div class='col-sm-2' id="headerKeyValuesLable"><spring:message code = "anvizent.package.label.headerKeyValues"/> :</div> 
	 		    	 <div class='col-sm-2'> 
				     <span class='headerKey'></span> 
					 </div> 
					 <div class='col-sm-6'> 
					 <input type="text" class="form-control headerValue" placeholder = "<spring:message code = "anvizent.package.label.value"/>" data-minlength="1" data-maxlength="45"> 
					 </div> 
			    </div> 	              
</div>		     
