<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="col-md-12 rightdiv">
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.defaultMappingConfiguration"/></h4>
 	</div>
 	<div class="col-sm-12" id="successOrErrorMessage"></div>
    <jsp:include page="admin_error.jsp"></jsp:include>
 	<input type="hidden" id="userId" value="${principal.userId}">
 	<c:url value="/admin/defaultTemplates" var="url"/>
 	<input type="hidden" value="${url}/add" id="add">
 	<input type="hidden" value="${url}/edit" id="edit">
 	<input type="hidden" value="${url}/update" id="update">
 	<input type="hidden" value="${url}/generalSettings/save" id="save">
 	<input type="hidden" value="${url}/currencyMapping/save" id="saveCurrencyMap">
 	
 	<c:if test="${defaultTemplatesForm.pageMode == 'list'}">
	 	<div style="width:100%;padding:0px 15px;">
			<div class="row form-group" style="padding:5px;border-radius:4px;">
				<a href="<c:url value="/admin/defaultTemplates/add"/>" class="btn btn-success btn-sm" style="float:right;">
					<spring:message code="anvizent.package.label.create"/>
				</a>			
			</div>
		</div>
	</c:if>
	
	<form:form modelAttribute="defaultTemplatesForm" action="${url}" >
		<c:if test="${defaultTemplatesForm.pageMode == 'list'}">
			<div class='row form-group'>
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg" id="defaultTemplatesTable">
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.templateId"/></th>
								<th><spring:message code="anvizent.package.label.templateName"/></th>
								<th><spring:message code="anvizent.package.label.description"/></th>
								<th><spring:message code="anvizent.package.label.isActive"/></th>
								<th><spring:message code="anvizent.package.label.edit"/></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${defaultTemplates}" var="template">
								<tr>
									<td>${template.templateId}</td>
									<td>${template.templateName}</td>
									<td><pre class="preTag">${template.description}</pre></td>
									<td>${template.active? 'Yes' : 'No'}</td>
									<td>
										<a href='<c:url value="/admin/defaultTemplates/edit/${template.templateId}"/>' class="btn btn-primary btn-sm tablebuttons" 
											title="<spring:message code="anvizent.package.label.edit"/>" >
											<i class="fa fa-pencil" aria-hidden="true"></i>
										</a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
		    </div>
 		</c:if>
 		
 		
		<c:if test="${defaultTemplatesForm.pageMode == 'add' || defaultTemplatesForm.pageMode == 'edit'}">
			<c:set var="updateMode" value="Update" />
			<c:if test="${defaultTemplatesForm.pageMode == 'add'  }">
				<c:set var="updateMode" value="Create" />
			</c:if>
 			<div class="col-sm-12">
		    	<div class="panel panel-default">
		    		<div class="panel-heading">${updateMode} Default Template</div>
		    		<div class="panel-body">
		    			<div class='row form-group'>
							<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.templateName"/>: </label>					
						   	<div class="col-sm-6">	
						   		<form:hidden path="templateId"/>
						   		<spring:message code="anvizent.package.label.templateName" var="tempName"/>		   		
						    	<form:input path="templateName" cssClass="form-control" placeholder="${tempName}" maxlength="45" data-minlength="1" data-maxlength="45"/>
						    </div>			   
			 			</div>
			 			
			 			<div class="row form-group">			 
							<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.description"/> :</label>
						   	<div class="col-sm-6">
						   	<spring:message code="anvizent.package.label.description" var="description"/>	
						    	<form:textarea path="description" cssClass="form-control" maxlength="255" placeholder="${description}" data-minlength="1" data-maxlength="255" /> 	
						    </div>			   
						</div>
						
						<div class="row form-group">			 
							<label class="control-label col-sm-3">
						    	<spring:message code="anvizent.package.label.activeStatus"/>		    	
						    </label>
						   	<div class="col-sm-6">
							   	<label class="radio-inline control-label">
							    	<form:radiobutton path="active" value="1"/> <spring:message code="anvizent.package.button.yes"/>		    	
							    </label>
							    <label class="radio-inline control-label">
							    	<form:radiobutton path="active" value="0"/> <spring:message code="anvizent.package.button.no"/>  	
							    </label>
						    </div>			   
						</div>
						
						<%-- <div class="row form-group">			 
							<label class="control-label col-sm-3">
						    	<spring:message code="anvizent.package.label.trialTemplate"/>		    	
						    </label>
						   	<div class="col-sm-6">
							   	<label class="radio-inline control-label">
							    	<form:radiobutton path="trialTemplate" value="true"/> <spring:message code="anvizent.package.button.yes"/>		    	
							    </label>
							    <label class="radio-inline control-label">
							    	<form:radiobutton path="trialTemplate" value="false"/> <spring:message code="anvizent.package.button.no"/>  	
							    </label>
						    </div>			   
						</div> --%>
						
						
						<div class="row form-group hidden" >			 
							<label class="control-label col-sm-3">
						    	<spring:message code="anvizent.package.label.trialTemplate"/>		    	
						    </label>
						   	<div class="col-sm-8">
							    <form:checkbox path="trialTemplate" />		    	
						    </div>			   
						</div>
						
						<div class="row form-group">
							<div class="col-sm-12">
								<button type="button" class="btn btn-primary btn-sm" id="createDefaultTemplate"><spring:message code="anvizent.package.button.save"/></button>
								<a href="<c:url value="/admin/defaultTemplates"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.admin.button.Back"/></a>
							</div>
						</div>
		    		</div>
		    	</div>
		    </div>		
 		</c:if>
 		
		<c:if test="${defaultTemplatesForm.pageMode == 'edit'}">
			<div class="col-sm-12">
				<div id="defaultTabs" style="margin-bottom:20px;">
					<ul>
						<li><a href="#clientVericalMapping"><spring:message code="anvizent.package.label.verticals"/></a></li>
						<li><a href="#clientConnectorMapping"><spring:message code="anvizent.package.label.connectors"/></a></li>
						<li><a href="#clientDlMapping"><spring:message code="anvizent.package.label.dls"/></a></li>
						<li><a href="#clientTablescriptsMapping"><spring:message code="anvizent.package.label.tableScripts"/></a></li>
						<li><a href="#clientWebServiceMapping"><spring:message code="anvizent.package.label.webServices"/></a></li>
						<li><a href="#clientCurrencyMapping"><spring:message code="anvizent.package.label.currencyMapping"/></a></li>
						<li><a href="#clientS3BucketMapping"><spring:message code="anvizent.package.label.clientS3Bucket"/></a></li>
						<li><a href="#schedulerMasterClientMapping"><spring:message code="anvizent.package.label.schedulerMasterClientMapping"/></a></li>
						<li><a href="#fileMultipartMapping"><spring:message code="anvizent.package.label.fileMultiPart"/></a></li>
						<%-- <li><a href="#generalSettings"><spring:message code="anvizent.package.label.generalSettings"/></a></li> --%>
						<!-- <li><a href="#clientS3BucketMapping">Client S3 Bucket Mapping</a></li> -->
					</ul>
					<div id="clientVericalMapping">
				      	<div class='row form-group'>
							<div class="table-responsive">
								<table class="table table-striped table-bordered tablebg " id="existingVerticalsTable">
									<thead>
										<tr>
										    <th><spring:message code = "anvizent.package.label.selectAll"/><input type="checkbox" value="selectAll" class="selectAll"></th>
											<th><spring:message code="anvizent.package.label.vertical"/> <spring:message code="anvizent.package.label.id"/></th>
											<th><spring:message code="anvizent.package.label.verticalName"/></th>
											<th><spring:message code="anvizent.package.label.verticalDescription"/></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${verticals}" var="vertical">
											<tr>
												<td>
													<c:if test="${vertical.isDefault}">
												     	<input type="checkbox" class="verticalCheckbox" value="${vertical.id}" checked="checked">
												  	</c:if>
												  	<c:if test="${!vertical.isDefault}">
												     	<input type="checkbox" class="verticalCheckbox" value="${vertical.id}">
												  </c:if>
												</td>
												<td>${vertical.id}</td>
												<td><c:out value="${vertical.name}" escapeXml="true" /></td>
												<td><c:out value="${vertical.description}" escapeXml="true" /></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<button type="button" class="btn btn-primary btn-sm savescripts_btn" id = "saveVertical">
								<spring:message code="anvizent.package.button.save"/>
							</button>
		    			</div>
					</div>
					
					<div id="clientConnectorMapping">
						<div class='row form-group'>
							<div class="table-responsive">
								<table class="table table-striped table-bordered tablebg " id="ConnectorTable">
									<thead>
										<tr>
										    <th><spring:message code = "anvizent.package.label.selectAll"/><input type="checkbox" value="selectAll" class="selectAll"></th>
											<th><spring:message code="anvizent.package.label.connector"/> <spring:message code="anvizent.package.label.id"/></th>
											<th><spring:message code="anvizent.package.label.connectorName"/></th>
											<th><spring:message code="anvizent.package.label.databaseName"/></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${connector}" var="connector" varStatus="index">
											<tr>
												<td>
												  <c:if test="${connector.isDefault == true}"> 
												     <input type="checkbox" class="connectorCheckbox" value="${connector.connector_id}" checked="checked"> 
												  </c:if>
												  <c:if test="${connector.isDefault == false}"> 
												     <input type="checkbox" class="connectorCheckbox" value="${connector.connector_id}"> 
												  </c:if>
													
												</td>
												<td>${connector.connector_id}</td>
												<td><c:out value="${connector.connectorName}" escapeXml="true" /> </td>
												<td><c:out value="${connector.name}" escapeXml="true" /></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<button type="button" class="btn btn-primary btn-sm savescripts_btn" id = "saveConnector">
								<spring:message code="anvizent.package.button.save"/>
							</button>
					    </div>
					</div>
					
					<div id="clientDlMapping">
						<div class='row form-group'>
							<div class="col-md-12" style="padding:0px;">
								<table class="table table-striped table-bordered tablebg " id="tdlMasterTable">
									<thead>
										<tr>
										    <th><spring:message code = "anvizent.package.label.selectAll"/><input type="checkbox" value="selectAll" class="selectAll"></th>
											<th><spring:message code="anvizent.package.label.dlId"/></th>
											<th><spring:message code="anvizent.package.label.moduleName"/></th>
											<th><spring:message code="anvizent.package.label.tableName"/></th>
											<th><spring:message code="anvizent.package.label.description"/></th>
										</tr>
									</thead>
									<tbody>
									<c:if test="${not empty dlInfo}">
										<c:forEach items="${dlInfo}" var="dLList">
											<tr id="dlMasterRow">
												<td>
													<c:if test="${dLList.isDefault == true}">
														<input type="checkbox" class="dlsCheckbox" value="${dLList.dL_id}" checked="checked">
													</c:if>
													<c:if test="${dLList.isDefault == false}">
														<input type="checkbox" class="dlsCheckbox" value="${dLList.dL_id}">
													</c:if>
												</td>
												<td>${dLList.dL_id}</td>
												<td><c:out value="${dLList.dL_name}" escapeXml="true" /></td>
												<td><c:out value="${dLList.dL_table_name}" escapeXml="true" /></td>
												<td><c:out value="${dLList.description}" escapeXml="true" /></td>
											</tr>
										</c:forEach>
									</c:if>
									</tbody>
								</table>
							</div>
							<button type="button" class="btn btn-primary btn-sm savescripts_btn" id = "saveDls">
								<spring:message code="anvizent.package.button.save"/>
							</button>
						</div>
					</div>
					
					<div id="clientTablescriptsMapping">
						<div class="row form-group">			 
									<label class="control-label col-sm-3  text-right"><spring:message code="anvizent.package.label.version"/> :</label>
								   	<div class="col-sm-6 versionSelectBoxDiv">
								   		<spring:message code="anvizent.package.label.version" var="version"/>	
								    	
								    </div>			   
						</div>
						<div class='row form-group'>
							<div class="col-md-12" style="padding:0px;">
								<table class="table table-striped table-bordered tablebg  dataTable no-footer"   id="tableScriptsMappingTable">
									<thead>
						                <tr>
							                <th><spring:message code = "anvizent.package.label.selectAll"/><input type="checkbox" value="selectAll" class="selectAll"></th>
							                <th><spring:message code="anvizent.package.label.scriptid"/></th>
							                <th><spring:message code="anvizent.package.label.scriptname"/></th>
							                <th><spring:message code="anvizent.package.label.version"/></th>
							                <th><spring:message code="anvizent.package.label.description"/></th>
						                </tr> 
					              	</thead>
					                <tbody id="tableScriptsTableBody">
						                <c:forEach items="${tableScriptsList}" var ="tableScripts">
						                	<c:if test="${tableScripts.scriptTypeName == 'Default' }">
							                   <tr>
							                   		<td>
								                   		<c:if test="${tableScripts.isDefault == true}">
								                   		     <input type="checkbox" class="tableScriptsCheckbox" value="${tableScripts.id}" checked="checked">
							                   		     </c:if>
							                   		     <c:if test="${tableScripts.isDefault == false}">
								                   		     <input type="checkbox" class="tableScriptsCheckbox" value="${tableScripts.id}">
							                   		     </c:if>
							                   		</td>
									                <td>${tableScripts.id}</td>
									                <td><c:out value="${tableScripts.scriptName}" escapeXml="true" /></td>
									                <td>
										                <c:if test="${empty tableScripts.version}">
										                	-
										                </c:if>
										                <c:if test="${not empty tableScripts.version}">
										                	<c:out value="${tableScripts.version}" escapeXml="true" />
										                </c:if>
									                </td>
									                <td><c:out value="${tableScripts.scriptDescription}" escapeXml="true" /></td> 
							                	</tr> 
						                	</c:if>
						                </c:forEach>
					                </tbody>
					                
			              		</table>
							</div>
							<button type="button" class="btn btn-primary btn-sm savescripts_btn" id = "saveTableScripts">
		            			<spring:message code="anvizent.package.button.save"/>
		            		</button>
					  	</div>
					</div>
					
					<div id="clientWebServiceMapping">
				      	<div class='row form-group'>
							<div class="table-responsive">
								<table class="table table-striped table-bordered tablebg " id="webServicesTable">
									<thead>
										<tr>
										    <th><spring:message code = "anvizent.package.label.selectAll"/><input type="checkbox" value="selectAll" class="selectAll"></th>
											<th><spring:message code="anvizent.package.label.Webservice"/> <spring:message code="anvizent.package.label.id"/></th>
											<th><spring:message code="anvizent.package.label.webserviceName"/></th>
											<th><spring:message code="anvizent.package.label.authenticationType"/></th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${webServices}" var="webService">
											<tr>
												<td>
													<c:if test="${webService.isDefault}">
												     	<input type="checkbox" class="webServiceCheckbox" value="${webService.id}" checked="checked">
												  	</c:if>
												  	<c:if test="${!webService.isDefault}">
												     	<input type="checkbox" class="webServiceCheckbox" value="${webService.id}">
												  	</c:if>
												</td>
												<td>${webService.id}</td>
												<td><c:out value="${webService.webServiceName}" escapeXml="true" /></td>
												<td><c:out value="${webService.webServiceAuthenticationTypes.authenticationType}" escapeXml="true" /></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<button type="button" class="btn btn-primary btn-sm savescripts_btn" id = "saveWebServices">
								<spring:message code="anvizent.package.button.save"/>
							</button>
		    			</div>
					</div>
					
					<div id="clientCurrencyMapping" style="display:none;">
						<div class='row form-group'>
							<div class="row form-group">
									<label class="col-sm-2 control-label"><spring:message code="anvizent.package.label.CurrencyType"/></label>
									<div class='col-sm-6'>
										<div class="currencyType">
											<label class="radio-inline currency">  
												<input type="radio" name="currencyType" value="minidw"  ${currencyDetails.currencyType eq 'minidw' ? 'checked':''}>Default
											</label>
											<label class="radio-inline currency">
												<input type="radio" name="currencyType" value="Client Specific"  ${currencyDetails.currencyType eq 'Client Specific' ? 'checked':''}>Client Specific
											</label>
										</div>
									</div>
								</div>
								<div class="row form-group" >
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.DashBoardCurrency"/>:</label>
									<div class='col-sm-6'>
											<select class="dashCurrency form-control" name="currencyName">
												<option value=''><spring:message code="anvizent.package.label.SelectCurrency"/></option>
								    			<c:forEach var="currList" items="${currencyList}">
								    				<option value="<c:out value="${currList.key}"/>"  ${currencyDetails.currencyName == currList.key ? 'selected':'' }><c:out value="${currList.value}"/> </option>
								    			</c:forEach>
								    		</select>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.AccountingCurrencyCode"/>:</label>
									<div class='col-sm-6'>
											<select class="accountingCurrency form-control">
								    			<option value=''><spring:message code="anvizent.package.label.SelectCurrency"/></option>
								    			<c:forEach var="currList" items="${currencyList}">
								    				<option value="<c:out value="${currList.key}"/>" ${currencyDetails.basecurrencyCode == currList.key ? 'selected':'' }><c:out value="${currList.value}"/></option>
								    			</c:forEach>
								    		</select>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.OtherCurrency"/>:</label>
									<div class='col-sm-6'>
											<select class="otherCurrency form-control">
												<option value=''><spring:message code="anvizent.package.label.SelectCurrency"/></option>
								    			<c:forEach var="currList" items="${currencyList}">
								    				<option value="<c:out value="${currList.key}"/>" ${currencyDetails.accountingCurrencyCode == currList.key ? 'selected':'' } ><c:out value="${currList.value}" /></option>
								    			</c:forEach>
								    		</select>
									</div>
								</div>
								<div class="row form-group">
									<div class='col-sm-8 messageForCurrency'></div>
								</div>
								<button type="button" class="btn btn-primary btn-sm saveCurrencymapping" id = "saveCurrency">
									<spring:message code="anvizent.package.button.save"/>
								</button>
					    </div>
					</div>

					<div id="clientS3BucketMapping">
						<div class='row form-group'>
							<div class="table-responsive">
							<div id="bucketInfo">
								<div class="col-md-8 add">
									<div class='row form-group'>
										<label class="col-sm-3 control-label labelsgroup"> <spring:message
												code="anvizent.package.label.SelectBucket" />:
										</label>
										<div class='col-sm-6 bucketList'></div>
									</div>
								</div>
							</div>
							</div>
							<button type="button" class="btn btn-primary btn-sm saveClientS3BucketButton" id = "saveClientS3Bucket">
									<spring:message code="anvizent.package.button.save"/>
							</button>
						</div>
					</div>
					
					<div id="schedulerMasterClientMapping">
				      	<div class='row form-group'>
							<div class="table-responsive">
								<table class="table table-striped table-bordered tablebg " id="schedulerMasterTable">
									<thead>
										<tr>
										    <th><spring:message code = "anvizent.package.label.selectAll"/><input type="checkbox" value="selectAll" class="selectAll"></th>
											<th>ID</th>
											<th>Name</th>
											<th>Type</th>
											<th>IP Address</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${schedularMasters}" var="schedulerMaster">
											<tr>
												<td>
													<%-- <c:if test="${schedulerMaster.isDefault}"> --%>
												     	<input type="checkbox" id="masterIds" value="${schedulerMaster.id}" checked="checked">
												  	<%-- </c:if> --%>
												  <%-- 	<c:if test="${!schedulerMaster.isDefault}"> --%>
												     	<input type="checkbox" id="masterIds" value="${schedulerMaster.id}">
												  	<%-- </c:if> --%>
												</td>
												<td>${schedulerMaster.id}</td>
												<td><c:out value="${schedulerMaster.name}" escapeXml="true" /></td>
												<td><c:out value="${schedulerMaster.type}" escapeXml="true" /></td>
												<td><c:out value="${schedulerMaster.ipAddress}" escapeXml="true" /></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</div>
							<button type="button" class="btn btn-primary btn-sm saveschedulerMaster_btn" id = "saveSchedulerMaster">
								<spring:message code="anvizent.package.button.save"/>
							</button>
		    			</div>
					</div>
					
					<div id="fileMultipartMapping">
				      	<div class='row form-group'>
							<div class="table-responsive">
								<table class="table table-striped table-bordered tablebg " id="fileMultipartTable">
									<thead>
										<tr>
										    <th><spring:message code = "anvizent.package.label.selectAll"/><input type="checkbox" value="selectAll" class="selectAll"></th>
											<th>ID</th>
											<th>Max File Size in MB</th>
											<th>MultiPart File Enabled</th>
											<th>No of Records per File</th>
										</tr>
									</thead>
									<%-- <tbody>
										<c:forEach items="${fileMultiParts}" var="fileMultiPart">
											<tr>
												<td>
													<c:if test="${fileMultiPart.isDefault}">
												     	<input type="checkbox" class="fileMultipartCheckbox" value="${fileMultiPart.id}" checked="checked">
												  	</c:if>
												  	<c:if test="${!fileMultiPart.isDefault}">
												     	<input type="checkbox" class="fileMultipartCheckbox" value="${fileMultiPart.id}">
												  	</c:if>
												</td>
												<td>${fileMultiPart.id}</td>
												<td><c:out value="${fileMultiPart.bucketName}" escapeXml="true" /></td>
												<td><c:out value="${fileMultiPart.accessKey}" escapeXml="true" /></td>
												<td><c:out value="${fileMultiPart.secretKey}" escapeXml="true" /></td>
											</tr>
										</c:forEach>
									</tbody> --%>
								</table>
							</div>
							<button type="button" class="btn btn-primary btn-sm savefileMultipart_btn" id = "savefileMultipart">
								<spring:message code="anvizent.package.button.save"/>
							</button>
		    			</div>
					</div>
					
					<div id="clientS3BucketMapping" class="hidden">
				      	<div class='row form-group'>
							<input type="hidden" name="id" id="bucketId" value="${bucketId}">
								<div id="bucketInfo">
									<div class="col-md-8 add">
										<div class='row form-group'>
											<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.SelectBucket"/>:</label>
											<div class='col-sm-6 bucketList'>
											</div>
										</div>
							
									</div>
								</div>
								
								<div class="row form-group">
									<div class='col-sm-8 messageForS3Bucket'></div>
								</div>
								<button type="button" class="btn btn-primary btn-sm saveS3BucketInfo" id = "saveS3Bucket">
									<spring:message code="anvizent.package.button.save"/>
								</button>
					    </div>
					</div>
					
					
		 		</div>
 			</div>
 		</c:if>	
 	</form:form>
</div>

 
    
    