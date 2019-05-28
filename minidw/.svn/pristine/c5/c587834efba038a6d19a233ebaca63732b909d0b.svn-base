<!DOCTYPE HTML>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12" style="padding:0px;">
  	<div class='row form-group'>
		<h4 class="alignText"  style="position:static;"><spring:message code="anvizent.package.label.defaultMappingInformation"/></h4>
	</div>
	
	<input type="hidden" id="userID" value="${principal.userId}">
	<c:url value="/${urlVar}/${name}" var="url"/>
	
	<input type="hidden" value="/minidw/${urlVar}/save" id="save"/>
	

	<form:form modelAttribute="defaultMappingInfo" action="${url}">
			<c:if test="${defaultMappingInfo.pageMode == 'display' }">
				<input type="hidden" value="${url}/${defaultMappingInfo.clientId}" id="geturl">
				<input type="hidden" value="${url}/${defaultMappingInfo.clientId}/save" id="saveurl">
				<form:hidden path="clientId" />
				
				<div class="row form-group">
					<label class="control-label labelsgroup col-sm-2"><spring:message code="anvizent.package.label.template" /> :</label>
				   	<div class="col-sm-6">
				   		<form:select path="templateId" cssClass="form-control">
				   			<spring:message code="anvizent.package.label.selectTemplate" var="selectTemplate"/>
				   			<form:option value="0">${selectTemplate}</form:option>
				   			<form:options items="${defaultTemplates}"/>
				   		</form:select>
				    </div>
				    <div class="col-sm-4">
				    	<button type="button" class="btn btn-sm btn-primary back" data-formid="allMappingInfoForm" 
								data-formaction="<c:url value="/admin/allMappingInfo"/>" style="float: right;margin-left: 5px;">
							<spring:message code="anvizent.package.link.back"/>
						</button>
						<c:if test="${defaultMappingInfo.templateId != '0' && defaultMappingInfo.templateId != null
							 		&& !(empty verticals && empty connector && empty dlInfo && empty tableScriptsList && empty webServices)}">
							
							<button type="button" class="btn btn-sm btn-primary saveMapping" style="float: right;">
								<spring:message code="anvizent.package.label.saveDefaultMappings"/>
							</button>
							
							<!-- <input type="button" class="btn btn-sm btn-primary saveDefaultMapping" value="Save Mapping" style="float: right;"> -->
								 
						 
						</c:if>
				    </div>
				</div>
				
				<c:if test="${defaultMappingInfo.templateId != '0' && defaultMappingInfo.templateId != null}">
					<c:choose>
						<c:when test="${empty verticals && empty connector && empty dlInfo && empty tableScriptsList && empty webServices}">
							<p>No mappings are done for this template.</p>
						</c:when>
						<c:otherwise>
							<div class="col-sm-6 main-covering">
								<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfVerticals != 0 && not empty verticals}">
									<div class="loaderInner">
										<div style="background: #f2f2f2 none repeat scroll 0 0;opacity:0.5;width:100%;height:100%;"></div>
									</div>
									<input type="hidden" name="skipVerticals" value="1" class="skippedVertical skip">
								</c:if>
								
								<div class="row">
									<h3 style="float:left;"><spring:message code="anvizent.package.label.verticals"/></h3> 
									<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfVerticals == 0 && !(
												defaultMappingInfo.allMappingInfoForm.numberOfTableScripts != 0 &&
												defaultMappingInfo.allMappingInfoForm.numberOfCurrencies != 0 && 
												defaultMappingInfo.allMappingInfoForm.numberOfDLs != 0 && 
												defaultMappingInfo.allMappingInfoForm.numberOfConnectors != 0 &&
												defaultMappingInfo.allMappingInfoForm.numberOfS3BuckeMappings != 0 &&
												defaultMappingInfo.allMappingInfoForm.numberOfWebServices != 0) && not empty verticals}">
										<label class="skipDefaultMapping"><input type="checkbox" name="skipVerticals" class="check">
											<spring:message code="anvizent.package.label.skip"/>
										</label>
									</c:if>
									<div id="verticalValidation"></div>
								</div>
								
								<jsp:useBean id="verticalsList" class="java.util.HashMap"/>
								
								<div class="table-responsive scrollDefaultInfoTables row">
									<c:choose>
										<c:when test="${empty verticals}">
											<p>No vertical is mapped for this template. Please map verticals(s) for this template to assign it to client.</p>
											<input type="hidden" name="skipVerticals" class="skipMapping" value="1"> 
										</c:when>
										<c:otherwise>
											<table class="table table-striped table-bordered tablebg " id="existingVerticalsTable">
												<thead>
													<tr>
														<th><spring:message code="anvizent.package.label.id"/><input type="checkbox" name="selectAll" checked="checked" class="selectAllVerticals"></th>
														<th><spring:message code="anvizent.package.label.verticalName"/></th>
														<th><spring:message code="anvizent.package.label.verticalDescription"/></th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${verticals}" var="vertical">
														<tr>
															<td>
																<input type="checkbox" name="verticals" checked="checked" value="${vertical.id}">
															</td>
															<td>${vertical.name}</td>
															<td><pre class="preTag">${vertical.description}</pre></td>
														</tr>
														<c:set target="${verticalsList}" property="vertical_${vertical.id}" value="${vertical.name}"/>
													</c:forEach>
												</tbody>
											</table>
										</c:otherwise>
									</c:choose>
								</div>	
							</div>
							 
							<div class="col-sm-6 main-covering">	
								<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfConnectors != 0 && not empty connector}">
									<div class="loaderInner">
										<div style="background: #f2f2f2 none repeat scroll 0 0;opacity:0.5;width:100%;height:100%;"></div>
									</div>
									<input type="hidden" name="skipConnectors" value="1" class="skippedConnector skip">
								</c:if>
								
								<div class="row">
									<h3 style="float:left;"><spring:message code="anvizent.package.label.connectors"/></h3>
									<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfConnectors == 0 && !(
											defaultMappingInfo.allMappingInfoForm.numberOfTableScripts != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfCurrencies != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfDLs != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfVerticals != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfS3BuckeMappings != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfWebServices != 0) && not empty connector}">
										<label class="skipDefaultMapping"><input type="checkbox" name="skipConnectors" class="check">
											<spring:message code="anvizent.package.label.skip"/>
										</label>
									</c:if>
									<div id="connectorValidation"></div>
								</div>
								
								<div class="table-responsive scrollDefaultInfoTables">
									<c:choose>
										<c:when test="${empty connector}">
											<p>No connector is mapped to this template. Please map connector(s) for this template to assign it to client.</p>
											<input type="hidden" name="skipConnectors"  class="skipMapping" value="1">
										</c:when>
										<c:otherwise>
											<table class="table table-striped table-bordered tablebg row" id="ConnectorTable">
												<thead>
													<tr>
														<th><spring:message code="anvizent.package.label.id"/><input type="checkbox" name="selectAll" checked="checked" class="selectAllConnector"></th>
														<th><spring:message code="anvizent.package.label.connectorName"/></th>
														<th><spring:message code="anvizent.package.label.databaseName"/></th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${connector}" var="connector" varStatus="index">
														<tr>
															<td>
																<input type="checkbox" name="connectors" checked="checked" value="${connector.connector_id}">
															</td>
															<td>${connector.connectorName}</td>
															<td>${connector.name}</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</c:otherwise>
									</c:choose>
								</div>	
							</div>
								  
							<div class="col-sm-12 main-covering">
								<c:if test="${(defaultMappingInfo.allMappingInfoForm.numberOfDLs != 0 && not empty dlInfo)
									|| (empty verticals && not empty dlInfo)}">
									<div class="loaderInner">
										<div style="background: #f2f2f2 none repeat scroll 0 0;opacity:0.5;width:100%;height:100%;"></div>
									</div>
									<input type="hidden" name="skipDLs" value="1" class="skippedDL skip">
								</c:if>
								
								<div class="row">
									<h3 style="float:left;"><spring:message code="anvizent.package.label.dashboardLayouts"/></h3>
									<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfDLs == 0 && 
									 		!(defaultMappingInfo.allMappingInfoForm.numberOfTableScripts != 0 &&
									 		defaultMappingInfo.allMappingInfoForm.numberOfCurrencies != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfConnectors != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfVerticals != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfS3BuckeMappings != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfWebServices != 0) && not empty dlInfo && not empty verticals}">
										<label class="skipDefaultMapping"><input type="checkbox" name="skipDLs" class="check">
											<spring:message code="anvizent.package.label.skip"/>
										</label>
									</c:if>	
									<div id="dLValidation"></div>
								</div>
								
								<div class="row table-responsive scrollDefaultInfoTables">
									<c:choose>
										<c:when test="${empty dlInfo}">
											<p>No DL is mapped to this template. Please map DL(s) for this template to assign it to client.</p>
											<input type="hidden" name="skipDLs"  class="skipMapping" value="1">
										</c:when>
										<c:otherwise>
											<table class="table table-striped table-bordered tablebg " id="dlMasterTable">
												<thead>
													<tr>
														<th><spring:message code="anvizent.package.label.dlId"/><input type="checkbox" name="selectAll" class="selectAllDLs"></th>
														<th><spring:message code="anvizent.package.label.vertical"/></th>
														<th><spring:message code="anvizent.package.label.moduleName"/></th>
														<th><spring:message code="anvizent.package.label.tableName"/></th>	
														<th><spring:message code="anvizent.package.label.description"/></th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${dlInfo}" var="dLInfo" varStatus="loop">
														<tr>
															<td>
																<input type="checkbox" class="dLs" name="dLInfo[${loop.index}].dL_id" value="${dLInfo.dL_id}" data-industryid="${dLInfo.industry.id}" checked="checked">
																<input type="hidden" name="dLInfo[${loop.index}].isLocked" value="0"/>
																<input type="hidden" name="dLInfo[${loop.index}].isDefault" value="${dLInfo.isDefault}"/>
															</td>
															<td>
																<c:set value="vertical_${dLInfo.industry.id}" var="vName"> </c:set>
																<c:out value="${verticalsList[vName]}"></c:out>
															</td>
															<td>${dLInfo.dL_name}</td>
															<td>${dLInfo.dL_table_name}</td>
															<td>${dLInfo.description}</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</c:otherwise>
									</c:choose>
								</div>	
							</div>
							
							<div class="col-sm-12 main-covering" >	
								<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfTableScripts != 0 && not empty tableScriptsList}">
									<div class="loaderInner">
										<div style="background: #f2f2f2 none repeat scroll 0 0;opacity:0.5;width:100%;height:100%;"></div>
									</div>
									<input type="hidden" name="skipTableScripts" value="1" class="skippedTableScript skip">
								</c:if>
								
								<div class="row">
									<h3 style="float:left;"><spring:message code="anvizent.package.label.tableScripts"/></h3>
									<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfTableScripts == 0 && 
											!(defaultMappingInfo.allMappingInfoForm.numberOfDLs != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfCurrencies != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfConnectors != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfVerticals != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfS3BuckeMappings != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfWebServices != 0 ) && not empty tableScriptsList}">
										<label class="skipDefaultMapping"><input type="checkbox" name="skipTableScripts" class="check">
											<spring:message code="anvizent.package.label.skip"/>
										</label>
									</c:if>	
									<div id="tableScriptValidation"></div>
								</div>
													
								<div class="table-responsive scrollDefaultInfoTables">
									<c:choose>
										<c:when test="${empty tableScriptsList}">
											<p>	No table script is mapped to this template. Please map table script(s) for this template to assign it to client.</p>
											<input type="hidden" name="skipTableScripts" class="skipMapping" value="1">
										</c:when>
										<c:otherwise>
											<table class="table table-striped table-bordered tablebg"  id="tableScriptsMappingTableDefault">
												 <thead>
									                <tr>
										                <th><spring:message code="anvizent.package.label.scriptid"/><input type="checkbox" name="selectAll" checked="checked" class="selectAllScripts"></th>
										                <th><spring:message code="anvizent.package.label.scripttype"/></th>
										                <th><spring:message code="anvizent.package.label.targetschema"/></th>
										                <th><spring:message code="anvizent.package.label.scriptname"/></th>
					   					                <th><spring:message code="anvizent.package.label.sqlscript"/></th>
					   					                <th><spring:message code="anvizent.package.label.description"/></th>
									                </tr> 
								                </thead>
								                <tbody>
								                	<c:forEach items="${tableScriptsList}" var="tableScripts" varStatus="loop">
								                   		<tr>
											                <td>							                	
											                	<input type="checkbox" class="tablescript" name="tableScripts[${loop.index}].id" checked="checked" value="${tableScripts.id}">
											                	<input type="hidden" name="tableScripts[${loop.index}].priority" value="1"/>
											                	<input type="hidden" name="tableScripts[${loop.index}].checkedScript" value="1"/>
											                	<input type="hidden" name="tableScripts[${loop.index}].isDefault" value="${tableScripts.isDefault}"/>
											                </td>
											                <td>${tableScripts.scriptTypeName}</td>
											                <td>${tableScripts.schemaName}</td>
											                <td>${tableScripts.scriptName}</td>							               
												            <td><input type="button" id="viewSqlScript" value="View Sql Script" class="btn btn-primary btn-sm" data-sid="${tableScripts.id}"></td> 
												            <td>${tableScripts.scriptDescription}</td>
									                	</tr>  
									                </c:forEach>
								                </tbody>
								            </table>
										</c:otherwise>
									</c:choose>
					            </div>
							</div>
							
							<div class="col-sm-12 main-covering" >	
								<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfWebServices != 0 && not empty webServices}">
									<div class="loaderInner">
										<div style="background: #f2f2f2 none repeat scroll 0 0;opacity:0.5;width:100%;height:100%;"></div>
									</div>
									<input type="hidden" name="skipWebServices" value="1" class="skippedWebServices skip">
								</c:if>
								
								<div class="row">
									<h3 style="float:left;"><spring:message code="anvizent.package.label.webServices"/></h3>
									<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfWebServices == 0 && 
											!(defaultMappingInfo.allMappingInfoForm.numberOfTableScripts != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfCurrencies != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfDLs != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfConnectors != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfS3BuckeMappings != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfVerticals != 0) && not empty webServices}">
										<label class="skipDefaultMapping"><input type="checkbox" name="skipWebServices" class="check">
											<spring:message code="anvizent.package.label.skip"/>
										</label>
									</c:if>	
									<div id="webServicesValidation"></div>
								</div>
													
								<div class="table-responsive scrollDefaultInfoTables" style="padding-bottom:30px;">
									<c:choose>
										<c:when test="${empty webServices}">
											<p>	No web service is mapped to this template. Please map web service(s) for this template to assign it to client.</p>
											<input type="hidden" name="skipWebServices" class="skipMapping" value="1">
										</c:when>
										<c:otherwise>
											<table class="table table-striped table-bordered tablebg " id="webServicesTable">
												<thead>
													<tr>
													    <th><spring:message code = "anvizent.package.label.selectAll"/><input type="checkbox" name="selectAll" checked="checked" class="selectAllWs"></th>
														<th><spring:message code="anvizent.package.label.webserviceName"/></th>
														<th><spring:message code="anvizent.package.label.authenticationType"/></th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${webServices}" var="webService">
														<tr>
															<td>
															     <input type="checkbox" name="webServices" value="${webService.id}" checked="checked">
															</td>
															<td>${webService.webServiceName}</td>
															<td>${webService.webServiceAuthenticationTypes.authenticationType}</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</c:otherwise>
									</c:choose>
					            </div>
							</div>
							
							
							<div class="col-sm-6 main-covering">	
								<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfCurrencies != 0 && not empty currencies}">
									<div class="loaderInner">
										<div style="background: #f2f2f2 none repeat scroll 0 0;opacity:0.5;width:100%;height:100%;"></div>
									</div>
									<input type="hidden" name="skipCurrency" value="1" class="skippedCurrency skip">
								</c:if>
								
								<div class="row">
									<h3 style="float:left;">Currencies</h3>
									<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfCurrencies == 0 &&
											 !(defaultMappingInfo.allMappingInfoForm.numberOfConnectors != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfTableScripts != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfDLs != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfVerticals != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfS3BuckeMappings != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfWebServices != 0) && not empty currencies}">
										<label class="skipDefaultMapping"><input type="checkbox" name="skipCurrency" class="check">
											<spring:message code="anvizent.package.label.skip"/>
										</label>
									</c:if>
									<div id="connectorValidation"></div>
								</div>
								
								<div class="table-responsive scrollDefaultInfoTables">
									<c:choose>
										<c:when test="${empty currencies}">
											<p>No currency is mapped to this template. Please map currency(s) for this template to assign it to client.</p>
											<input type="hidden" name="skipCurrency"  class="skipMapping" value="1">
										</c:when>
										<c:otherwise>
											<table class="table table-striped table-bordered tablebg row" id="ConnectorTable">
												<thead>
													<tr>
														<th><spring:message code="anvizent.package.label.currencytype"/></th>
														<th><spring:message code="anvizent.package.label.dashboardcurrencycode"/></th>
														<th><spring:message code="anvizent.package.label.accountingcurrencycode"/></th>
														<th><spring:message code="anvizent.package.label.othercurrencycode"/></th>
													</tr>
												</thead>
												<tbody>
													<c:forEach items="${currencies}" var="clientCurrencyMap" varStatus="index">
														<tr>
														<form:hidden path="clientCurrencyMapping.currencyType" value="${clientCurrencyMap.currencyType}"/>
														<form:hidden path="clientCurrencyMapping.currencyName" value="${clientCurrencyMap.currencyName}"/>
														<form:hidden path="clientCurrencyMapping.basecurrencyCode" value="${clientCurrencyMap.accountingCurrencyCode}"/>
														<form:hidden path="clientCurrencyMapping.accountingCurrencyCode" value="${clientCurrencyMap.currencyType}"/>
															<td>${clientCurrencyMap.currencyType}</td>
															<td>${clientCurrencyMap.currencyName}</td>
															<td>${clientCurrencyMap.basecurrencyCode}</td>
															<td>${clientCurrencyMap.accountingCurrencyCode}</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</c:otherwise>
									</c:choose>
								</div>	
							</div>
							<div class="col-sm-6 main-covering">	
								<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfS3BuckeMappings != 0 && not empty s3bucketinfo}">
									<div class="loaderInner">
										<div style="background: #f2f2f2 none repeat scroll 0 0;opacity:0.5;width:100%;height:100%;"></div>
									</div>
									<input type="hidden" name="skipS3Bucket" value="1" class="skippedS3Bucket skip">
								</c:if>
								
								<div class="row">
									<h3 style="float:left;">S3 Bucket info</h3>
									<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfS3BuckeMappings == 0 &&
											 !(defaultMappingInfo.allMappingInfoForm.numberOfConnectors != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfTableScripts != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfDLs != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfVerticals != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfWebServices != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfCurrencies == 0 ) && not empty s3bucketinfo}">
										<label class="skipDefaultMapping"><input type="checkbox" name="skipS3Bucket" class="check">
											<spring:message code="anvizent.package.label.skip"/>
										</label>
									</c:if>
									<div id="connectorValidation"></div>
								</div>
								
								<div class="table-responsive scrollDefaultInfoTables">
									<c:choose>
										<c:when test="${empty s3bucketinfo}">
											<p>s3 bucket not mapped to this template. Please map s3 bucket for this template to assign it to client.</p>
											<input type="hidden" name="skipS3Bucket"  class="skipS3Bucket" value="1">
										</c:when>
										<c:otherwise>
											<table class="table table-striped table-bordered tablebg row" id="ConnectorTable">
												<thead>
													<tr>
														<th><spring:message code="anvizent.package.label.BucketName"/></th>
														<th><spring:message code="anvizent.package.label.SecretKey"/></th>
														<th><spring:message code="anvizent.package.label.AccessKey"/></th>
													</tr>
												</thead>
												<tbody>
														<tr>
														<form:hidden path="bucketId" value="${s3bucketinfo.id}"/>
															<td>${s3bucketinfo.bucketName}</td>
															<td>${s3bucketinfo.secretKey}</td>
															<td>${s3bucketinfo.accessKey}</td>
														</tr>
												</tbody>
											</table>
										</c:otherwise>
									</c:choose>
								</div>	
							</div>
							
							<div class="col-sm-6 main-covering">	
								<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfSchedulers != 0 && not empty schedularMaster}">
									<div class="loaderInner">
										<div style="background: #f2f2f2 none repeat scroll 0 0;opacity:0.5;width:100%;height:100%;"></div>
									</div>
									<input type="hidden" name="skipSchedulers" value="1" class="skippedschedularMaster skip">
								</c:if>
								
								<div class="row">
									<h3 style="float:left;">Scheduler</h3>
									<c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfSchedulers == 0 &&
											 !(defaultMappingInfo.allMappingInfoForm.numberOfConnectors != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfTableScripts != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfDLs != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfVerticals != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfWebServices != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfCurrencies == 0 ) && not empty schedularMaster}">
										<label class="skipDefaultMapping"><input type="checkbox" name="skipSchedulers" class="check">
											<spring:message code="anvizent.package.label.skip"/>
										</label>
									</c:if>
									<div id="connectorValidation"></div>
								</div>
								
								<div class="table-responsive scrollDefaultInfoTables">
									<c:choose>
										<c:when test="${empty schedularMaster}">
											<p>Scheduler not mapped to this template. Please map Scheduler for this template to assign it to client.</p>
											<input type="hidden" name="skipSchedulers"  class="skipSchedulers" value="1">
										</c:when>
										<c:otherwise>
											<table class="table table-striped table-bordered tablebg row" id="ConnectorTable">
												<thead>
													<tr>
														<th><spring:message code="anvizent.package.label.name"/></th>
														<th><spring:message code="anvizent.package.label.type"/></th>
														<th><spring:message code="anvizent.package.label.ipAddress"/></th>
													</tr>
												</thead>
												<tbody>
														<tr>
														<form:hidden path="schedulerId" value="${schedularMaster.id}"/>
															<td>${schedularMaster.name}</td>
															<td>${schedularMaster.type}</td>
															<td>${schedularMaster.ipAddress}</td>
														</tr>
												</tbody>
											</table>
										</c:otherwise>
									</c:choose>
								</div>	
							</div>
							
							<div class="col-sm-6 main-covering">	
								<%-- <c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfS3BuckeMappings != 0 && not empty s3bucketinfo}">
									<div class="loaderInner">
										<div style="background: #f2f2f2 none repeat scroll 0 0;opacity:0.5;width:100%;height:100%;"></div>
									</div>
									<input type="hidden" name="fileSettings" value="1" class="skippedfileSettings skip">
								</c:if> --%>
								
								<div class="row">
									<h3 style="float:left;">File Settings</h3>
									<%-- <c:if test="${defaultMappingInfo.allMappingInfoForm.numberOfS3BuckeMappings == 0 &&
											 !(defaultMappingInfo.allMappingInfoForm.numberOfConnectors != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfTableScripts != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfDLs != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfVerticals != 0 &&
											defaultMappingInfo.allMappingInfoForm.numberOfWebServices != 0 && 
											defaultMappingInfo.allMappingInfoForm.numberOfCurrencies == 0 ) && not empty s3bucketinfo}">
										<label class="skipDefaultMapping"><input type="checkbox" name="fileSettings" class="check">
											<spring:message code="anvizent.package.label.skip"/>
										</label>
									</c:if> --%>
									<div id="connectorValidation"></div>
								</div>
								
								<div class="table-responsive scrollDefaultInfoTables">
									<c:choose>
										<c:when test="${empty fileSettings}">
											<p>File Settings not mapped to this template. Please map file settings for this template to assign it to client.</p>
											<input type="hidden" name="skipS3Bucket"  class="skipS3Bucket" value="1">
										</c:when>
										<c:otherwise>
											<table class="table table-striped table-bordered tablebg row" id="ConnectorTable">
												<thead>
													<tr>
														<th><spring:message code="anvizent.package.label.maxFileSizeinMB"/></th>
														<th><spring:message code="anvizent.package.label.multiPartFileEnabled"/></th>
														<th><spring:message code="anvizent.package.label.noOfRecordsperFile"/></th>
													</tr>
												</thead>
												<tbody>
												<c:forEach items="${fileSettings}" var="fileSettingMap">
														<tr>
													       <form:hidden path="fileSettingId" value="${fileSettingMap.clientId}"/>
															<td>${fileSettingMap.maxFileSizeInMb}</td>
															<td>${fileSettingMap.multiPartEnabled}</td>
															<td>${fileSettingMap.noOfRecordsPerFile}</td>
														</tr>
														</c:forEach>
												</tbody>
											</table>
										</c:otherwise>
									</c:choose>
								</div>	
							</div>
						</c:otherwise>
					</c:choose>
				</c:if>
			</c:if>	
			
			<c:if test="${defaultMappingInfo.pageMode == 'closeWindow' }">
				<script type="text/javascript">
					window.close();
				</script>
			</c:if>
			
	</form:form>
</div>
 
