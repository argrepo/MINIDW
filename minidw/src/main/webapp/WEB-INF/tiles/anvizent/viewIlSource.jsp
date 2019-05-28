<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
   
   <div class="dummydiv"></div>
   <div class='row form-group'>
   		<h4 class="alignText"><spring:message code="anvizent.admin.label.ViewSourceDetails"/></h4>
   </div>
   
   <jsp:include page="_error.jsp"></jsp:include>
   
   
  <div class="col-md-10 form" style="padding:10px 15px;">
	<form:form modelAttribute="standardPackageForm" method="POST" id="standardPackageForm">
			<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
			<input type="hidden" id="replaceVariable"/>
			<input type="hidden" id="replaceWith"/>
			<input type="hidden" id="maxDateQuerys"/>
			<input type="hidden" id="collapseId"/>
			<input type="hidden" id="from" value="<c:out value="${param['from'] }"/>" />
			<form:hidden path="industryId"/>
			<form:hidden path="packageId"/>
			<form:hidden path="dLId"/>
			<c:set var="packageId_var"  value="${standardPackageForm.packageId}"/>
			<input type="hidden" id="packageId" value="<c:out value="${standardPackageForm.packageId}"/>">
			<input type="hidden" id="dlId" value="<c:out value="${standardPackageForm.dLId}"/>">
			<input type="hidden" id="ilId" value="<c:out value="${standardPackageForm.iLId}"/>">
			 <div class='row form-group'>
				<div class='col-sm-4 col-md-3' >
				    <label class="control-label "><spring:message code="anvizent.package.label.packageName"/></label>
					<form:hidden path="packageName" class="form-control" disabled="true"/>
					<div class="txt-break">${standardPackageForm.packageName  }</div>
				</div>
				<div class='col-sm-4 col-lg-3' >
				    <label class="control-label"><spring:message code="anvizent.package.label.moduleName"/></label>
					<form:hidden path="dLName" class="form-control txt-break" disabled="true"/>
					<div  class="txt-break">${standardPackageForm.dLName  }</div> 
				</div>
				<div class='col-sm-4 col-lg-3' >
				    <label class="control-label "><spring:message code="anvizent.package.label.inputLayout"/></label>
					<form:hidden path="iLName" class="form-control" disabled="true"/>
					<div class="txt-break">${standardPackageForm.iLName  }</div>
				</div>
				<div class='col-sm-4 col-lg-3' >
				<label class="control-label">&nbsp;</label>
				
				<input type="hidden" id="back_btnStandard" value="/adt/standardpackage"/>
				
				<input type="hidden" id="back_btnAddil" value="/adt/standardpackage/addILSource/${standardPackageForm.packageId}/${standardPackageForm.dLId}/${standardPackageForm.iLId}"/>
				
				 <div> <a class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a></div>
				</div>
			</div>
	         <div class="alert alert-danger Ilmessage" style="display:none;"></div>
		     <div class="alert alert-success successIlMessage" style="display:none;"></div>
	</form:form>  
	  <div id="viewIlSourceDetails">
	  <c:choose>
	  <c:when test="${not empty iLConnectionMapping}">
	  <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
	  <c:forEach items="${iLConnectionMapping}" var="iLConnectionMapping" varStatus="increment">
       <c:if test="${standardPackageForm.iLId ==  iLConnectionMapping.iLId}">
       <div class="panel panel-default">
			  <div class="panel-heading" role="tab" id="heading<c:out value="${increment.index}"/>">
			    <h4 class="panel-title panelTitle">
			    <a role="button" class="pull-left" data-toggle="collapse" data-parent="#accordion" href="#collapse<c:out value="${increment.index}"/>" aria-expanded="true" aria-controls="collapse<c:out value="${increment.index}"/>">
			     <c:choose>
                 <c:when test="${iLConnectionMapping.isFlatFile && !iLConnectionMapping.isWebservice}" >
                 ${iLConnectionMapping.filePath}
                 <c:choose>
                 <c:when test="${iLConnectionMapping.isFirstRowHasColoumnNames == true}" >
                  <c:set var="isFirstRowHasColoumnNames" value="Yes"></c:set>
                 </c:when>
                 <c:otherwise>
                 <c:set var="isFirstRowHasColoumnNames" value="No"></c:set>
                 </c:otherwise>
                 </c:choose>
                 </c:when>
                  <c:when test="${!iLConnectionMapping.isWebservice && !iLConnectionMapping.isFlatFile}">
                  <c:out value="${iLConnectionMapping.iLConnection.connectionName}"/> 
                  </c:when>
                  <c:when test="${!iLConnectionMapping.isFlatFile && iLConnectionMapping.isWebservice}">
                 <c:out value="${iLConnectionMapping.webService.webserviceName}"/>
                  </c:when>
                 </c:choose>	
			    <c:choose>
                 <c:when test="${increment.index == 0}" >
                 <c:set var="collapseIn" value="in"></c:set>
                 </c:when>
                 <c:otherwise>
                 <c:set var="collapseIn" value=""></c:set>
                  </c:otherwise>
                 </c:choose>
			    </a>
			     <c:choose>
                  <c:when test="${iLConnectionMapping.isFlatFile && !iLConnectionMapping.isWebservice}" >
                  <c:set var="previewFileAndQuery" value="flatFilePreviewStandard"></c:set>
                  <div class="pull-right toggle-col"> 
	            	  <input type="checkbox" data-toggle="toggle" class="toggle-demo" data-on="Enabled" data-off="Disabled"  data-activeStatus="<c:out value="${iLConnectionMapping.activeRequired}"/>"  data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" ${iLConnectionMapping.clientId != principal.mobileNo? "disabled='disabled'":"" } >
				      <button type="button" style="margin-right:3px;" data-webserviceId="<c:out value="${iLConnectionMapping.webService.web_service_id}"/>" data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" data-connectionid="${iLConnectionMapping.iLConnection.connectionId}" data-typeofcommand="<c:out value="${iLConnectionMapping.typeOfCommand}"/>"  data-query="<c:out value="${iLConnectionMapping.iLquery}"/>" data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" class="btn btn-primary btn-sm pull-left <c:out value="${previewFileAndQuery}"/>" title="<spring:message code="anvizent.package.button.preview"/>" ${iLConnectionMapping.clientId != principal.mobileNo? "disabled='disabled'":"" }><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></button>
		    	 	  <button type="button" style="margin-right:3px;" id="click" data-webserviceId="<c:out value="${iLConnectionMapping.webService.web_service_id}"/>" data-dlid="<c:out value="${iLConnectionMapping.dLId}"/>" data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" class="btn btn-primary btn-sm pull-left deleteIlSource" title="<spring:message code="anvizent.package.label.delete"/>" ${iLConnectionMapping.clientId != principal.mobileNo? "disabled='disabled'":"" } ><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>
		    	 </div>
			      </c:when>
                  <c:when test="${!iLConnectionMapping.isWebservice && !iLConnectionMapping.isFlatFile}">
                  <c:set var="previewFileAndQuery" value="databasePreviewStandard"></c:set>
                  <div class="pull-right toggle-col"> 
                  	<input type="checkbox" data-toggle="toggle" class="toggle-demo" data-on="Enabled" data-off="Disabled" data-activeStatus="<c:out value="${iLConnectionMapping.activeRequired}"/>"  data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>"  ${iLConnectionMapping.clientId != principal.mobileNo? "disabled='disabled'":"" }>
	                 
	                   
				      <button type="button" style="margin-right:3px;" 
				       data-webserviceId="<c:out value="${iLConnectionMapping.webService.web_service_id}"/>" 
				       data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" 
				       data-connectionid="<c:out value="${iLConnectionMapping.iLConnection.connectionId}"/>"
				       data-typeofcommand="<c:out value="${iLConnectionMapping.typeOfCommand}"/>" 
				       data-query="<c:out value="${iLConnectionMapping.iLquery}"/>"
				       data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" 
				       class="btn btn-primary btn-sm pull-left <c:out value="${previewFileAndQuery}"/>" 
				       title="<spring:message code="anvizent.package.button.preview"/>"  ${ iLConnectionMapping.iLConnection.webApp && !iLConnectionMapping.iLConnection.availableInCloud  ? 'disabled' : '' } ${iLConnectionMapping.clientId != principal.mobileNo? "disabled='disabled'":"" }><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>
				       </button>
				       
				        <button type="button" style="margin-right:3px;" 
	                   id="click" data-webserviceId="<c:out value="${iLConnectionMapping.webService.web_service_id}"/>" 
	                   data-dlid="<c:out value="${iLConnectionMapping.dLId}"/>" 
	                   data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" 
	                   data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" 
	                   class="btn btn-primary btn-sm  deleteIlSource pull-left" 
	                   title="<spring:message code="anvizent.package.label.delete"/>" ${iLConnectionMapping.clientId != principal.mobileNo? "disabled='disabled'":"" } ><span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
	                   </button>
			    </div>
                  </c:when> 
                   <c:when test="${!iLConnectionMapping.isFlatFile && iLConnectionMapping.isWebservice}">
                   <c:set var="previewFileAndQuery" value="webservicePreviewStandard"></c:set>
                    <div class="pull-right toggle-col"> 
	                   <input type="checkbox" data-toggle="toggle" class="toggle-demo" data-on="Enabled" data-off="Disabled" data-activeStatus="<c:out value="${iLConnectionMapping.activeRequired}"/>"  data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>"  ${iLConnectionMapping.clientId != principal.mobileNo? "disabled='disabled'":"" }>
	                   <c:if test="${iLConnectionMapping.joinWebService}">
	                     <c:if test="${iLConnectionMapping.fileType == null || iLConnectionMapping.fileType == 'csv'}">
	                    <button type="button"  style="margin-right:3px;" data-isWebservice="<c:out value="${iLConnectionMapping.isWebservice}"/>" data-webserviceconId="<c:out value="${iLConnectionMapping.webService.wsConId}"/>" data-isJoinWebservice="<c:out value="${iLConnectionMapping.joinWebService}"/>" data-webserviceId="<c:out value="${iLConnectionMapping.webService.web_service_id}"/>" data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" data-connectionid="<c:out value="${iLConnectionMapping.iLConnection.connectionId}"/>"  data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" class="btn btn-primary btn-sm pull-left editMappedHeadersForWebService" title="<spring:message code="anvizent.message.text.editMappedHeaders" />" ${iLConnectionMapping.clientId != principal.mobileNo? "disabled='disabled'":"" }><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></button>
	                    </c:if>
	                   	<button type="button"  style="margin-right:3px;" id="click" data-webserviceconId="<c:out value="${iLConnectionMapping.webService.wsConId}"/>" data-webserviceId="<c:out value="${iLConnectionMapping.webService.web_service_id}"/>" data-dlid="<c:out value="${iLConnectionMapping.dLId}"/>" data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" class="btn btn-primary btn-sm  deleteIlSource pull-left" title="<spring:message code="anvizent.package.label.delete"/>" ${iLConnectionMapping.clientId != principal.mobileNo? "disabled='disabled'":"" } ><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>
	                   </c:if>
	                    <c:if test="${!iLConnectionMapping.joinWebService}">
				         <button type="button"  style="margin-right:3px;" data-webserviceconId="<c:out value="${iLConnectionMapping.webService.wsConId}"/>" data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" data-connectionid="<c:out value="${iLConnectionMapping.iLConnection.connectionId}"/>" data-typeofcommand="<c:out value="${iLConnectionMapping.typeOfCommand}"/>"  data-query="<c:out value="${iLConnectionMapping.iLquery}"/>" data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" class="btn btn-primary btn-sm pull-left <c:out value="${previewFileAndQuery}"/>" title="<spring:message code="anvizent.package.button.preview"/>"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></button>
	                      <c:if test="${iLConnectionMapping.fileType == null || iLConnectionMapping.fileType == 'csv'}">
	                     <button type="button"  style="margin-right:3px;" data-isWebservice="<c:out value="${iLConnectionMapping.isWebservice}"/>"  data-webserviceconId="<c:out value="${iLConnectionMapping.webService.wsConId}"/>" data-isJoinWebservice="<c:out value="${iLConnectionMapping.joinWebService}"/>" data-webserviceId="<c:out value="${iLConnectionMapping.webService.web_service_id}"/>" data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" data-connectionid="<c:out value="${iLConnectionMapping.iLConnection.connectionId}"/>"  data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" class="btn btn-primary btn-sm pull-left editMappedHeadersForWebService" title="<spring:message code="anvizent.message.text.editMappedHeaders" />" ${iLConnectionMapping.clientId != principal.mobileNo? "disabled='disabled'":"" }><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></button>
	                 	</c:if>
	                 	<button type="button"  style="margin-top:1px;" id="click" data-webserviceId="<c:out value="${iLConnectionMapping.webService.web_service_id}"/>" data-webserviceconId="<c:out value="${iLConnectionMapping.webService.wsConId}"/>" data-dlid="<c:out value="${iLConnectionMapping.dLId}"/>" data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" class="btn btn-primary btn-sm  deleteIlSource pull-left" title="<spring:message code="anvizent.package.label.delete"/>" ${iLConnectionMapping.clientId != principal.mobileNo? "disabled='disabled'":"" } ><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>
	                 </c:if>
                 	</div>
                   </c:when>
                 </c:choose>
			      </h4>
			  </div>
			  <div id="collapse${increment.index}" class="panel-collapse collapse  ${collapseIn}" role="tabpanel" aria-labelledby="heading${increment.index}">
			  <div class="panel-body">
			                   <c:choose>
			                   <c:when test="${iLConnectionMapping.isFlatFile && !iLConnectionMapping.isWebservice}">
			  	               <div class='row form-group '>
									<label class="col-sm-4 control-label"><spring:message code ="anvizent.package.label.fileType"/> :</label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" value="<c:out value="${iLConnectionMapping.fileType}"/>">
									</div>
								</div>
								<div class='row form-group '>
									<label class="col-sm-4 control-label"><spring:message code ="anvizent.package.label.delimeter"/> </label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" value="<c:out value="${iLConnectionMapping.delimeter}"/>">
									</div>
								</div>
								
								<div class='row form-group'>
									<label class='col-sm-4 control-label"'>
										<spring:message code="anvizent.package.label.dataSource"/>:
									</label>
									<div class='col-sm-4'>
										<select  class="form-control control-label ilSourceName" name="ilSourceName" disabled="disabled">
											<option value='0'><spring:message code="anvizent.package.label.selectDataSource"/></option>
											<c:forEach items="${allDataSourceList}" var="dataSource">
											<c:choose>
											<c:when test="${iLConnectionMapping.ilSourceName == dataSource.dataSourceName}">
												<option value="<c:out value="${dataSource.dataSourceName}"/>" selected><c:out value="${dataSource.dataSourceName}"/></option>
											</c:when>
											<c:otherwise>
												<option value="<c:out value="${dataSource.dataSourceName}"/>"><c:out value="${dataSource.dataSourceName}"/></option>
											</c:otherwise>
											</c:choose>
											</c:forEach>
											<option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></option>
										</select>
									</div>
									<div class='col-md-2 updateFlat' style="display:none; padding:0;">
										<button type="button" class="btn btn-primary btn-sm updateFlatDetails" data-mappingDbId="<c:out value="${iLConnectionMapping.connectionMappingId}"/>"><spring:message code = "anvizent.package.button.update"/></button>
									</div>
									<div class='col-md-2' style="padding:0;" >
										<button type="button" class="btn btn-primary btn-sm editFlatDetails"><spring:message code="anvizent.package.label.edit" /></button>
									</div>
								</div>
								
								<div class='row form-group dataSourceOther' style="display:none">
										<label class='col-sm-4 control-label'>
										</label>
										<div class='col-sm-8'>
											<input type="text" id="dataSourceOtherName" class="form-control" data-minlength="1" data-maxlength="45">
										</div>
								</div>
								
								<div class='row form-group '>
									<label class="col-sm-4 control-label"><spring:message code ="anvizent.package.label.file"/> :</label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" value="<c:out value="${iLConnectionMapping.filePath}"/>">
									</div>
								</div>
							</c:when>
						 
						    <c:when test="${!iLConnectionMapping.isWebservice && !iLConnectionMapping.isFlatFile}">
								<div class='row form-group '> 
			                  		<label class="col-sm-2 control-label"><spring:message code ="anvizent.package.label.connectionName"/> :</label>
									 <div class='col-sm-8' >
										 <input type="text" class="form-control" id='existingConnections' disabled="disabled" data-connectionId="<c:out value="${iLConnectionMapping.iLConnection.connectionId}"/>" value="<c:out value="${iLConnectionMapping.iLConnection.connectionName}"/>">
									 </div>
								 </div>
								 
								<div class='row form-group'>
									<label class='col-sm-2 control-label"'>
										<spring:message code="anvizent.package.label.dataSource"/>:
									</label>
									<div class='col-sm-4'>
										<select  class="form-control control-label ilSourceName" name="ilSourceName" disabled="disabled">
										<option value='0'><spring:message code="anvizent.package.label.selectDataSource"/></option>
											<c:forEach items="${allDataSourceList}" var="dataSource">
											<c:choose>
											<c:when test="${iLConnectionMapping.ilSourceName == dataSource.dataSourceName}">
												<option value="<c:out value="${dataSource.dataSourceName}"/>" selected><c:out value="${dataSource.dataSourceName}"/></option>
											</c:when>
											<c:otherwise>
												<option value="<c:out value="${dataSource.dataSourceName}"/>"><c:out value="${dataSource.dataSourceName}"/></option>
											</c:otherwise>
											</c:choose>
											</c:forEach>
											<option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></option>
										</select>
									</div>
									<div class='col-sm-1 updateDb' style="display:none">
										<button type="button" style="float:right;margin-top:-6px;" class="btn btn-primary btn-sm updateDbDetails" data-mappingDbId="<c:out value="${iLConnectionMapping.connectionMappingId}"/>"><spring:message code = "anvizent.package.button.update"/></button>
									</div>
									<div class='col-sm-1' >
										<button type="button" style="float:right;margin-top:-6px;"  class="btn btn-primary btn-sm editDbDetails"><span class="glyphicon glyphicon-edit" title="<spring:message code ="anvizent.package.label.edit"/>" aria-hidden="true"></span></button>
									</div>
								</div>
								
								<div class='row form-group dataSourceOther' style="display:none">
										<label class='col-sm-2 control-label'>
										</label>
										<div class='col-sm-8'>
											<input type="text" id="dataSourceOtherName" class="form-control" data-minlength="1" data-maxlength="45">
										</div>
								</div>
								 
								<div class='row form-group '>
								<label class="col-sm-2 control-label"><spring:message code ="anvizent.package.label.connectorType"/> :</label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" id="connectorType" disabled="disabled" data-connectorid="<c:out value="${iLConnectionMapping.iLConnection.database.connector_id}"/>"  data-protocal="<c:out value="${iLConnectionMapping.iLConnection.database.protocal}"/>" value="<c:out value="${iLConnectionMapping.iLConnection.database.name}"/>">
									</div>
								</div>
								<div class='row form-group '>
									<label class="col-sm-2 control-label"><spring:message code ="anvizent.package.label.connectionType"/>:</label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" value="<c:out value="${iLConnectionMapping.iLConnection.connectionType}"/>">
									</div>
								</div>
								<div class='row form-group '>
									<label class="col-sm-2 control-label"><spring:message code ="anvizent.package.label.serverName"/>:</label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" value="<c:out value="${iLConnectionMapping.iLConnection.server}"/>">
								</div>
								</div>
								<div class='row form-group '>
									<label class="col-sm-2 control-label"><spring:message code ="anvizent.package.label.userName"/> :</label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" value="<c:out value="${iLConnectionMapping.iLConnection.username}"/>">
									</div>
								</div>
								<c:if test="${not empty iLConnectionMapping.iLConnection.dbVariablesList}">
										<c:forEach items="${iLConnectionMapping.iLConnection.dbVariablesList}" var="entry"  varStatus="myIndex">
										 <c:choose>
										 <c:when test="${myIndex.index == 0}">
										 <div class='row form-group'>
											<label class="col-sm-2 control-label"><spring:message code ="anvizent.package.label.dataBasevariables"/> :</label>
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
										       <label class="col-sm-2 control-label"></label>
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
									    <label class="col-sm-2 control-label"><spring:message code ="anvizent.package.label.typeOfcommand"/> :</label>
										<div class='col-sm-6'>
										<select class="form-control typeOfCommand"   disabled="disabled">
											<option value="Query"><spring:message code ="anvizent.package.label.query"/></option>
										</select>
										</div>
										<div class='col-sm-2' >
										<button type="button" style="float:right;margin-top:-6px;"  class="btn btn-primary btn-sm  editIlSourceDetails" ${iLConnectionMapping.iLConnection.webApp && !iLConnectionMapping.iLConnection.availableInCloud ? 'disabled' : '' } ><span class="glyphicon glyphicon-edit" title="<spring:message code ="anvizent.package.label.edit"/>" aria-hidden="true"></span></button>
										</div>
								</div>
								
								<div id="defualtVariableDbSchema" style="display:none"></div>
							  		<%-- 	<div class='row form-group IL_queryCommand'>
											<div class='col-sm-2'>
											</div>
											<div class='col-sm-8' id="replaceDbSchema">
												<input type="button" value='<spring:message code="anvizent.package.label.replaceSchemas"/>' id='replaceShemas' style="display:none" class="btn btn-primary btn-sm"/>
										   </div>
						        		</div> --%>
						        		
			        		 	<div class='row form-group' id='replace' style="display: none">
										<div class='col-sm-2 control-label'>
											<b><spring:message code="anvizent.package.label.find" /> :</b>
										</div>
										<div class='col-sm-3 control-label'>
											<input type="text" class="form-control" id="replace_variable">
										</div>
										<div class='col-sm-2 control-label'>
											<spring:message code="anvizent.package.label.replaceWith" /> :
										</div>
										<div class='col-sm-3 control-label'>
											<input type="text" class="form-control" id="replace_with">
										</div>
								</div>
								
								<div class='row form-group' id='replaceAll' style="display: none">
										<div class="col-xs-12" style="padding-right: 160px;">	
						                     <div class="" id="replace_All" style="float: right;">
						                           <input type="button" value="<spring:message code="anvizent.package.label.replaceAll" />" id="replace_all" class="btn btn-primary btn-sm replaceAllVar">
						                     </div>
				                       		<div class="" id="buttonUndo" style="float: right; margin: 0px 10px;">
						                    		<input type="button" value="<spring:message code="anvizent.package.label.undo"/>" id="undo" class="btn btn-primary btn-sm">
		 						 			</div>
	  								</div>
				        		</div>
										 
								<c:choose>
								<c:when test="${iLConnectionMapping.typeOfCommand == 'Query'}">
								   <div class='row form-group view'>
										   <div class='row form-group incrQuery'>
													<label class="col-sm-2 control-label"><b><spring:message code="anvizent.package.label.incrementalUpdate" /></b>:</label>
													<div class='col-sm-8' >
													<input type="checkbox" id='il_incremental_update' disabled="disabled" name="isIncrementalUpdate"  ${iLConnectionMapping.isIncrementalUpdate? "checked='checked'" : ''}>
													<spring:message code="anvizent.package.label.incrementalUpdate" />
										   			<button type="button" style="float:right;margin-top:-6px;"  class="btn btn-primary btn-sm preBuildQuery"  disabled="disabled"><spring:message code="anvizent.package.label.loadPreBuildQuery" /></button>
										   </div>
									   </div>
										<div class="row form-group">
											 <div class='col-sm-2'></div> 
											 <div class='col-sm-8' >
												 <textarea class="form-control iLquery" id="iLqueryTextArea${increment.index}"  rows="6" readonly="readonly"><c:out value="${iLConnectionMapping.iLquery}"/></textarea>
											     <input class="form-control iLSp" id="iLqueryInput<c:out value="${increment.index}"/>" style="display:none">
											     <input type="hidden" id="oldQueryScript" value="<c:out value="${iLConnectionMapping.iLquery}"/>">
										 </div>
										</div>
										<div class="row form-group max_date_query" style="display:none;">
										 	<div class='col-sm-2 control-label'>
										 		<label class="col-sm-2 control-label"><b><spring:message code="anvizent.package.label.maxDateQuery"/></b>:</label> 
										 	</div> 
											 <div class='col-sm-8' >
												<textarea class="form-control" rows="6" id="maxDatequery" name="maxDateQuery" readonly="readonly" placeholder="<spring:message code="anvizent.package.label.maxDateQuery"/>"><c:out value="${iLConnectionMapping.maxDateQuery}"/></textarea>
												<input type="hidden" id="oldMaxDateQuery">
											</div>
										 <div class='col-sm-2'>
										 </div> 
										 </div>
										 
										<div class='row form-group'>
											 <div class='col-sm-2'> 
									 		</div> 
										 <div class='col-sm-8' >
											 <input type="button" value="<spring:message code="anvizent.package.button.validateQuerySP"/>"  id="checkQuerySyntax" style="display:none"  class="btn btn-primary btn-sm checkQuerySyntax">
											  &nbsp;<input type="button" value="<spring:message code="anvizent.package.button.preview"/>" id="checkTablePreview" style="display:none" class="btn btn-primary btn-sm checkTablePreview"    >
										      &nbsp; <input type="button" value="<spring:message code="anvizent.package.button.update"/>" id="updateILConnectionMapping" data-mappingId="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" style="display:none" class="btn btn-primary btn-sm updateILConnectionMapping"  >
										 </div>
									</div>
								</div>
								</c:when>
								<c:otherwise>
								 <div class='row form-group '>
										 <div class='col-sm-2'> 
									 </div> 
										 <div class='col-sm-8 ' >
											 <input class="form-control iLSp" id="iLqueryInput<c:out value="${increment.index}"/>"  readonly="readonly" value="<c:out value="${iLConnectionMapping.iLquery}"/>"> 
											 <textarea class="form-control iLquery" id="iLqueryTextArea<c:out value="${increment.index}"/>"  style="display:none" rows="6"   ></textarea>
										       
										 </div>
									</div>
									 
									<div class='row form-group '>
										 <div class='col-sm-2'> 
									 </div> 
										 <div class='col-sm-8' >
											 <input type="button" value="<spring:message code="anvizent.package.button.validateQuerySP"/>"  id="checkQuerySyntax"  style="display:none"  class="btn btn-primary btn-sm checkQuerySyntax">
											 &nbsp; <input type="button" value="<spring:message code="anvizent.package.button.preview"/>" id="checkTablePreview"  style="display:none" class="btn btn-primary btn-sm checkTablePreview"    >
										     &nbsp; <input type="button" value="<spring:message code="anvizent.package.button.update"/>" id="updateILConnectionMapping" data-mappingId="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" style="display:none" class="btn btn-primary btn-sm updateILConnectionMapping"  >
										 </div>
									</div>
									
								</c:otherwise>
								</c:choose>
								
							</c:when>
							 <c:when test="${!iLConnectionMapping.isFlatFile && iLConnectionMapping.isWebservice}">
							  <c:if  test="${!iLConnectionMapping.joinWebService}">
                               <div class='row form-group'>
									<label class="col-sm-4 control-label"><spring:message code="anvizent.package.label.webserviceName"/> :</label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" title="<c:out value="${iLConnectionMapping.webService.webserviceName}"/>" value="<c:out value="${iLConnectionMapping.webService.webserviceName}"/>">
									</div>  
								</div>
                                <div class='row form-group'>
									<label class="col-sm-4 control-label"><spring:message code="anvizent.package.label.apiname"/> :</label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" title="<c:out value="${iLConnectionMapping.webService.apiName}"/>" value="<c:out value="${iLConnectionMapping.webService.apiName}"/>">
									</div>
								</div>
								<div class='row form-group'>
									<label class='col-sm-4 control-label"'>
										<spring:message code="anvizent.package.label.dataSource"/>:
									</label>
									<div class='col-sm-6'>
										<select  class="form-control control-label ilSourceName" name="ilSourceName" disabled="disabled">
											<option value='0'><spring:message code="anvizent.package.label.selectDataSource"/></option>
											<c:forEach items="${allDataSourceList}" var="dataSource">
											<c:choose>
											<c:when test="${iLConnectionMapping.ilSourceName == dataSource.dataSourceName}">
												<option value="<c:out value="${dataSource.dataSourceName}"/>" selected><c:out value="${dataSource.dataSourceName}"/></option>
											</c:when>
											<c:otherwise>
												<option value="<c:out value="${dataSource.dataSourceName}"/>"><c:out value="${dataSource.dataSourceName}"/></option>
											</c:otherwise>
											</c:choose>
											</c:forEach>
											<option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></option>
										</select>
									</div>
									<div class='col-sm-1 updateWs' style="display:none">
										<button type="button" style="float:right;margin-top:-6px;" class="btn btn-primary btn-sm updateWsDetails" data-mappingDbId="<c:out value="${iLConnectionMapping.connectionMappingId}"/>"><spring:message code = "anvizent.package.button.update"/></button>
									</div>
									<div class='col-sm-1' >
										<button type="button" style="float:right;margin-top:-6px;"  class="btn btn-primary btn-sm editWsDetails"><span class="glyphicon glyphicon-edit" title="Edit" aria-hidden="true"></span></button>
									</div>
								</div>
								
								<div class='row form-group dataSourceOther' style="display:none">
										<label class='col-sm-4 control-label'>
										</label>
										<div class='col-sm-8'>
											<input type="text" id="dataSourceOtherName" class="form-control" data-minlength="1" data-maxlength="45">
										</div>
								</div>
								 
								<div class='row form-group '>
									<label class="col-sm-4 control-label"><spring:message code="anvizent.package.label.url"/> </label>
									<div class='col-sm-8' >
										<input type="text" class="form-control wsSourceUrl" disabled="disabled" title="<c:out value="${iLConnectionMapping.webService.url}"/>" value="<c:out value="${iLConnectionMapping.webService.url}"/>">
									</div>
								</div>
								 <div class='row form-group'>
									<label class="col-sm-4 control-label">File Type :</label>
									<div class='col-sm-8' >
										 <input type='text' class="form-control"   readonly="readonly" value='<c:out value="${iLConnectionMapping.fileType == null ? 'csv' : iLConnectionMapping.fileType}"/>'>
									</div>
								</div>
								 </c:if>
							  <c:if  test="${iLConnectionMapping.joinWebService}">
							  <div class="joinWsApiInfoDiv">
                               <div class='row form-group'>
									<label class="col-sm-4 control-label"><spring:message code="anvizent.package.label.webserviceName"/> :</label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" title="<c:out value="${iLConnectionMapping.webService.webserviceName}"/>" value="<c:out value="${iLConnectionMapping.webService.webserviceName}"/>">
									</div>  
								</div>
                                <div class='row form-group'> 
									<label class="col-sm-4 control-label"><spring:message code="anvizent.package.label.apiname"/> :</label>
									<div class='col-sm-8' >
										<input type="text" class="form-control" disabled="disabled" title="<c:out value="${iLConnectionMapping.webService.apiName}"/>" value="<c:out value="${iLConnectionMapping.webService.apiName}"/>">
									</div>
								</div>
								<div class='row form-group'>
									<label class='col-sm-4 control-label"'>
										<spring:message code="anvizent.package.label.dataSource"/>:
									</label>
									<div class='col-sm-6'>
										<select  class="form-control control-label ilSourceName" name="ilSourceName" disabled="disabled">
											<option value='0'><spring:message code="anvizent.package.label.selectDataSource"/></option>
											<c:forEach items="${allDataSourceList}" var="dataSource">
											<c:choose>
											<c:when test="${iLConnectionMapping.ilSourceName == dataSource.dataSourceName}">
												<option value="<c:out value="${dataSource.dataSourceName}"/>" selected><c:out value="${dataSource.dataSourceName}"/></option>
											</c:when>
											<c:otherwise>
												<option value="<c:out value="${dataSource.dataSourceName}"/>"><c:out value="${dataSource.dataSourceName}"/></option>
											</c:otherwise>
											</c:choose>
											</c:forEach>
											<option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></option>
										</select>
									</div>
									<div class='col-sm-1 updateWsJoin' style="display:none">
										<button type="button" style="float:right;margin-top:-6px;" class="btn btn-primary btn-sm updateWsJoinDetails" data-mappingDbId="<c:out value="${iLConnectionMapping.connectionMappingId}"/>"><spring:message code = "anvizent.package.button.update"/></button>
									</div>
									<div class='col-sm-1' >
										<button type="button" style="float:right;margin-top:-6px;"  class="btn btn-primary btn-sm editWsJoinDetails"><span class="glyphicon glyphicon-edit" title="Edit" aria-hidden="true"></span></button>
									</div>
								</div>
								
								<div class='row form-group dataSourceOther' style="display:none">
										<label class='col-sm-4 control-label'>
										</label>
										<div class='col-sm-8'>
											<input type="text" id="dataSourceOtherName" class="form-control" data-minlength="1" data-maxlength="45">
										</div>
								</div>
								<div class='row form-group '>
									<label class="col-sm-4 control-label"><spring:message code="anvizent.package.label.joinUrls"/> :</label>
									<div class='col-sm-8' >
										 <textarea class="form-control wsJoinUrls"  title="<c:out value="${iLConnectionMapping.webserviceJoinUrls}"/>" id="iLqueryTextArea<c:out value="${increment.index}"/>"  rows="6" disabled="disabled"><c:out value="${iLConnectionMapping.webserviceJoinUrls}"/></textarea>
									</div>
								</div>
								  <c:if  test="${iLConnectionMapping.fileType == null || iLConnectionMapping.fileType == 'csv'}">
								  <div class='row form-group'>
									<label class="col-sm-4 control-label">File Type :</label>
									<div class='col-sm-8' >
										 <input type='text' class="form-control" id="fileType<c:out value="${increment.index}"/>"  value="<c:out value="${iLConnectionMapping.fileType == null ? 'csv' : iLConnectionMapping.fileType}"/>"  readonly="readonly">
									</div>
								</div>
								<div class='row form-group'>
									<label class="col-sm-4 control-label"></label>
								    <div class='col-sm-8'>
					                 <button type="button" class="btn btn-primary btn-sm  editIlWsApiQuery" title="<spring:message code="anvizent.package.label.editQuery"/>" style="float: right;"><span class="glyphicon glyphicon-edit"  aria-hidden="true" ></span></button>
					                 </div>
					              </div>
								<div class='row form-group'>
									<label class="col-sm-4 control-label"><spring:message code="anvizent.package.label.joinQuery"/> :</label>
									<div class='col-sm-8' >
										 <textarea class="form-control wsApiILQuery" id="iLqueryTextArea<c:out value="${increment.index}"/>"  rows="6" readonly="readonly"><c:out value="${iLConnectionMapping.iLquery}"/></textarea>
									</div>
								</div>
								</c:if>
								<c:if  test="${iLConnectionMapping.fileType == 'xml' || iLConnectionMapping.fileType == 'json'}">
								<div class='row form-group'>
									<label class="col-sm-4 control-label">File Type :</label>
									<div class='col-sm-8' >
										 <input type='text' class="form-control" id="fileType<c:out value="${increment.index}"/>"  value='<c:out value="${iLConnectionMapping.fileType}"/>' readonly="readonly">
									</div>
								</div>
								</c:if>
								<div class='row form-group'>
									 <div class='col-sm-4'> </div> 
									 <div class='col-sm-8' >
										 <input type="button"  value="<spring:message code="anvizent.package.button.validate"/>"   style="display:none"  class="btn btn-primary btn-sm checkWsQuerySyntax">
									      &nbsp; <input type="button"  value="<spring:message code="anvizent.package.label.mapHeaders"/>" style="display:none" data-isWebservice="<c:out value="${iLConnectionMapping.isWebservice}"/>"  data-webserviceconId="<c:out value="${iLConnectionMapping.webService.wsConId}"/>" data-isJoinWebservice="<c:out value="${iLConnectionMapping.joinWebService}"/>" data-webserviceId="<c:out value="${iLConnectionMapping.webService.web_service_id}"/>" 
									      data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" data-connectionid="<c:out value="${iLConnectionMapping.iLConnection.connectionId}"/>"  data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" class="btn btn-primary btn-sm  editMappedQueryHeadersForWebService" title="<spring:message code="anvizent.message.text.editMappedHeaders" />"/> 
									 </div>
								</div>
								</div>
							</c:if>
								 
                              </c:when>
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
        </div>
        </c:if>
      </c:forEach>
      </div>
	  </c:when>
	  <c:otherwise>
	   <div class="alert alert-danger Ilmessage"  ><spring:message code="anvizent.package.label.noSourceIsAdded"/>
	  </div>
	  </c:otherwise>
	  </c:choose>
       
 
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
		       <button type="button" class="btn btn-primary confirm_replace_all" id="confirmReplaceAll"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="messagePopUpForDeleteIlSource" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 500px;">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close close-popup" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        <h4 class="modal-title custom-modal-title"></h4>
				      </div>
				      <div class="modal-body">
				        	<div id="popUpMessageForDeleteILSource" style="text-align: center;"></div>
				      </div>
				      <div class="modal-footer" style="text-align: center;">
				      <a href='<c:url value="/adt/package/viewIlSource/${standardPackageForm.packageId}/${standardPackageForm.dLId}/${standardPackageForm.iLId}" />' class="btn btn-primary btn-sm back"><spring:message code="anvizent.package.link.ok"/></a>					        	
							<%-- <a class="btn btn-primary btn-sm" href='<c:url value="/adt/package/viewIlSource/${standardPackageForm.packageId}/${mappedModuleSatandardPackage.ilConnectionMapping.dLId}/${mappedModuleSatandardPackage.ilInfo.iL_id}" />'> </a> --%>
							<button type="button" class="btn btn-default closeButton" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				      </div>
			    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
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
							<spring:message code="anvizent.package.button.mapFileHeadersUpdate" />
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
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>

						</div>
					</div>
					<div class="modal-footer">
						<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.updateMapping"/>" id="updateMappingWithILForWebService" name='updateMappingWithILForWebService'/>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
			</div>
		</div>
   </div>
   		
   </div>
 