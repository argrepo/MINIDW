<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.dLInfo"/></h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	</div>
<c:url value="/admin/dashBoardLayoutMaster/edit" var="editUrl"/>
<form:form modelAttribute="dashboardLayoutForm" action="${editUrl }" enctype="multipart/form-data">
<form:hidden path="pageMode"/>
	<c:choose>
		<c:when test="${dashboardLayoutForm.pageMode == 'list' }">
		    <div  style="width:100%;padding:0px 15px;">
         	<div class="row form-group" style="padding:5px;border-radius:4px;">
			<a style="float:right;" class="btn btn-sm btn-success" href="<c:url value="/admin/dashBoardLayoutMaster/add"/>"> <spring:message code="anvizent.package.button.add"/> </a>
			</div>
			</div>
			<div class='row form-group'>
			 <div class="table-responsive">
				<table class="table table-striped table-bordered tablebg " id="tdlMasterTable">
							<thead>
								<tr>
									<th class="col-xs-2"><spring:message code="anvizent.package.label.dlId"/></th>
									<th class="col-xs-4"><spring:message code="anvizent.package.label.moduleName"/></th>
									<th><spring:message code="anvizent.package.label.version"/></th>
									<th><spring:message code="anvizent.package.label.createdDate"/></th>
									<th><spring:message code="anvizent.package.label.modifiedDate"/></th>
									<th class="col-xs-1"><spring:message code="anvizent.package.label.Edit"/></th>
								</tr>
							</thead>
								<tbody>
								<c:if test="${not empty dlInfo}">
										<c:forEach items="${dlInfo}" var="dLList">
											<tr id="dlMasterRow">
												<td id="dlId">${dLList.dL_id}</td>
												<td id="dlName"><c:out value="${dLList.dL_name}" ></c:out> </td>
												<td>${not empty dLList.version? dLList.version : '-' }</td>
												<td>${not empty dLList.modification.createdTime ? dLList.modification.createdTime : '-'}</td>
												<td>${not empty dLList.modification.modifiedTime ? dLList.modification.modifiedTime : '-'}</td>
												<td>
													<button class="btn btn-primary btn-xs editDlMaster tablebuttons" type="submit" name="dlId" value="${dLList.dL_id}">
														<i class="fa fa-pencil" aria-hidden="true"></i>
													</button>
												</td>
											</tr>
										</c:forEach>
									
								</c:if>
							</tbody>
				</table>
				</div>
			</div>
		</c:when>
		<c:when test="${dashboardLayoutForm.pageMode == 'edit' || dashboardLayoutForm.pageMode =='add' }">
			<div class="col-md-12">
				<c:set var="updateMode" value="Update" />
				<c:if test="${dashboardLayoutForm.pageMode == 'add'  }">
					<c:set var="updateMode" value="Add" />
				</c:if>
				<div class="panel panel-default">
					<div class="panel-heading">${updateMode } <spring:message code="anvizent.package.label.dlmaster"/></div>
					<div class="panel-body">
						<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.verticalName"/> :</label> 
									<div class='col-sm-8'>
									<form:select path="verticalName" cssClass="form-control">
									<option value="0"><spring:message code="anvizent.package.label.selectvertical"/></option>
										<c:if test="${not empty getExistingVerticals}">
											<form:options items="${getExistingVerticals}"/>
										</c:if>
									</form:select>
							 
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.dlName" /> :</label>
								<div class="col-sm-8">
									<c:url value="/admin/dashBoardLayoutMaster/add" var="addUrl"/>
									<input type="hidden" value="${addUrl }" id="addUrl">
									<c:url value="/admin/dashBoardLayoutMaster/update" var="updateUrl"/>
									<input type="hidden" value="${updateUrl }" id="updateUrl">
									<form:hidden path="dlId"/>
								    <spring:message code="anvizent.package.label.dlName" var="dlNamePlaceholder"   />
									<form:input path="dlName" htmlEscape="true"  placeholder='${dlNamePlaceholder}' maxlength="255" data-minlength="1" data-maxlength="255" cssClass="form-control"/>
								</div>
							</div>
							<div class="row form-group">
				          		<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.version" /></label>
				          		<div class="col-sm-8">
				          		 	<spring:message code="anvizent.package.label.version" var="versionPlaceholder"   />
				          			<form:input path="version" class="form-control" placeholder='${versionPlaceholder}' maxlength="45" data-minlength="1" data-maxlength="45"/>
				          		</div>
				          	</div>
							<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.description"/> :</label>
								<div class="col-sm-8">
								<spring:message
										code="anvizent.package.label.description" var="descriptionPlaceholder" />
									<form:textarea path="dlDescription"  placeHolder = "${descriptionPlaceholder}" data-minlength="1" data-maxlength="500" cssClass="form-control"/>
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.tableName"  /> :</label>
								<div class="col-sm-8">
								<spring:message
										code="anvizent.package.label.tableName" var="dtableNamePlaceholder" />
									<form:input path="dlTableName"  placeHolder = "${dtableNamePlaceholder}" data-minlength="1" data-maxlength="255" cssClass="form-control"/>
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.ils"/> :</label>
								<div class='col-sm-8'>
								<form:select path="ilList">
									<c:if test="${not empty fetchAllIlInfo }">
										<form:options items="${fetchAllIlInfo }"/>
									</c:if>
								</form:select>
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.kpis"/> :</label>
								<div class='col-sm-8'>
									<form:select path="kpiList">
										<c:if test="${not empty getAllKpis }">
											<form:options items="${getAllKpis }"/>
										</c:if>
									</form:select>
								</div>
							</div>
							
							
							<div class="row form-group">			 
								<label class="control-label col-sm-3">Job Execution Type :</label>
							   	<div class="col-sm-8 jobExecution">									    	
							    	<label class="radio-inline control-label">
								    	<form:radiobutton path="jobExecutionType" value="E"/>ELT
								    </label>
								    <label class="radio-inline control-label">
								    	<form:radiobutton path="jobExecutionType" value="T"/>Talend
								    </label>	
							    </div>			   
							</div>
							
							
							
							<div class="row form-group jobTagsDiv hidden">			 
								<label class="control-label col-sm-3">Job Tags: </label>					
							   	<div class="col-sm-8">
							    	<form:select path="jobTagId" cssClass="jobTags form-control" style="white-space:pre-wrap;">
							    		<form:option value="0">Select</form:option>
							    		<form:options items="${getJobTags}"/>
							    	</form:select>
							    </div>			   
							</div>
							
								
							<div class="row form-group loadParametersDiv hidden">			 
								<label class="control-label col-sm-3">Master Configuration: </label>					
							   	<div class="col-sm-8">
							    	<form:select path="masterParameterId" cssClass="masterParameterIds form-control" style="white-space:pre-wrap;">
										<form:option value="0">default</form:option>
							    		<form:options items="${getMasterConfiguration}"/>
							    	</form:select>
							    </div>			   
							</div>
							
							
							<div class="row form-group loadParametersDiv hidden">			 
								<label class="control-label col-sm-3">Load Parameters: </label>					
							   	<div class="col-sm-8">
							    	<form:select path="loadParameterId" cssClass="loadParameters form-control" style="white-space:pre-wrap;">
										<form:option value="0">Select</form:option>
							    		<form:options items="${getLoadParameters}"/>
							    	</form:select>
							    </div>			   
							</div>
									
							
							<div class="row form-group contextParamsDiv hidden">
								<label class="control-label col-sm-3"><spring:message
										code="anvizent.package.label.contextparameters"/> :</label>
								<div class='col-sm-8'>
									<form:select path="contextParamList">
										<c:if test="${not empty getContextParameters }">
											<form:options items="${getContextParameters }"/>
										</c:if>
									</form:select>
								</div>
							</div>
							
							<div class="row form-group jobNameDiv hidden">
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.jobName" /> :</label>
								<div class="col-sm-8">
								    <spring:message code="anvizent.package.label.jobName" var="jobNamePlaceholder"/>
									<form:input path="jobName"  placeHolder = "${jobNamePlaceholder}"  data-minlength="1" data-maxlength="500" cssClass="form-control"/>
								</div>
							</div>
							<div class="row form-group jobFileNameDiv hidden">
					            <label class="control-label col-sm-3 jobfile-label"><spring:message code="anvizent.package.label.jobFileName"/> : </label>
			    	            <div class="col-sm-8">
			    		        <input type="hidden" id="uploadedJobFileNames" name="uploadedJobFileNames" value="${dashboardLayoutForm.jobfileNames }">
			    		        <spring:message  code="anvizent.package.label.jobFileName" var="jobFileNamePlaceholder"  />
				                <form:input path="jobfileNames"  placeHolder = "${jobFileNamePlaceholder}"  cssClass="form-control uploadedJobFileNames" readonly="true"/>
				               </div>
			                </div>
                     <div class="jobFilesDiv hidden">
				     <c:forEach var="fileNames" items="${dashboardLayoutForm.existingFileNames}" varStatus="loop">
					   <div class="row form-group fileContainer">
						<c:if test="${loop.index == 0}">
							<label class="control-label col-sm-3 jobfile-label"><spring:message code="anvizent.package.label.jobFile"/> : </label>
						</c:if>
						<c:if test="${loop.index > 0}">
							<label class="control-label col-sm-3 jobfile-label"></label>
						</c:if>
						<div class="col-sm-8">
							<label class="checkbox" style="margin-left: 20px;">
							  	<input type="checkbox" name="useOldJobFile" class="useOldJobFile" checked="checked">
							</label>
							<h5 class="jobFileName" style="margin-left: 20px;"><c:out value="${fileNames}"/></h5>
							<input type="hidden" name="jobFileNames" value="<c:out value="${fileNames}"/>">	
					    </div>
					    <div class="col-sm-1">
					    	<a href="#"  class="btn btn-primary btn-sm addJobFile">
					      		<span class="glyphicon glyphicon-plus"></span>
					    	</a>					    	
					    </div>
					   </div>
				    </c:forEach>
					   <c:if test="${empty dashboardLayoutForm.existingFileNames}">
			    		<div class="row form-group fileContainer">
							<label class="control-label col-sm-3 jobfile-label"><spring:message code="anvizent.package.label.jobFile"/> : </label>
				    		<div class="col-sm-8">			    			
						    	<input type="file" class="jobFile" name="jarFiles" data-buttonText="Find file">
						    </div>
						    <div class="col-sm-1">
						    	<a href="#" class="btn btn-primary btn-sm addJobFile">
						      		<span class="glyphicon glyphicon-plus"></span>
						    	</a>
						    </div>
						  </div>
			    	   </c:if>			
			    </div>
						 <div class="col-sm-6 job-files-div" style='display:none;'>
			    	        <div class="row form-group fileContainer">
				    	    <label class="control-label col-sm-3 jobfile-label"><spring:message code="anvizent.package.label.jobFile"/> : </label>
				    	    <div class="col-sm-8">
				    		<input type="file" class="jobFile" name="jarFiles" data-buttonText="Find file">
					        </div>
					        <div class="col-sm-1">
						    <a href="#"  class="btn btn-primary btn-sm addJobFile"> <span class="glyphicon glyphicon-plus"></span> </a>
					    	<a href="#"  class="btn btn-primary btn-sm remove_field deleteJobFile" style="display:none;"> <span class="glyphicon glyphicon-trash"></span> </a>
				    	   </div>	
				          </div>	
			           </div>
			           
			           <div class="row form-group existedJarFileDiv hidden">	
							<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.ExistedJarFiles"/>: </label>
						   	<div class="col-sm-8">
						    	<select class="existedJarFile form-control" multiple="multiple">
						    			<c:forEach var="jarFile" items="${jarFilesList}">
						    				<option value="<c:out value="${jarFile.fileName}"/>" ><c:out value="${jarFile.fileName}"/></option>
						    			</c:forEach>
						    	</select>
						    </div>			   
						</div>
						
			           <div class="row form-group">
							<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.activeStatus"/> :</label>
							<div class='col-sm-8'>
							 	<div class="active">
							 		 <label class="radio-inline">
										<form:radiobutton path="active" value="true"/><spring:message code="anvizent.package.button.yes"/>
									 </label>	
									 <label class="radio-inline">
								    	<form:radiobutton path="active" value="false"/><spring:message code="anvizent.package.button.no"/> 
								    </label>
							 	</div>
							</div>
						</div>
			           
						<div class="row form-group">
							<div class="col-sm-8">
								<c:choose>
									<c:when test="${dashboardLayoutForm.pageMode == 'edit'}">
										<button id="updateDlMaster" type="button"
									class="btn btn-primary btn-sm">Update</button>
									 
									</c:when>
									<c:when test="${dashboardLayoutForm.pageMode == 'add'}">
										<button id="addDlMaster" type="button" 
									    class="btn btn-primary btn-sm"> <spring:message code="anvizent.package.label.add"/></button>
									   </c:when>
								</c:choose>
								    <button id="resetDlMaster" type="button" 
									class="btn btn-primary btn-sm"> <spring:message code="anvizent.package.button.reset"/></button>
									<a id="resetDlMaster" type="reset" href="<c:url value="/admin/dashBoardLayoutMaster"/>"
									class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
							</div>
						</div>
					</div>
				</div>


			</div>
		</c:when>
	</c:choose>
</form:form>
</div>




