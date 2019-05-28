<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
	 
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<input type="hidden" id="userID"
			value="<c:out value="${principal.userId}"/>">
	</div>
   <div class="col-sm-10">
		<div id="successOrErrorMessage"></div>
	</div>
	 
	<div class="row form-group">
		<h4 class="alignText">
			<spring:message
				code="anvizent.package.label.aiJobsUpload" />
		</h4>
	</div>
	<div class="col-sm-12">
			<div class='row form-group'>
				<button type="button" style="float: right; margin-right: 1.5em;"
					class="btn btn-sm btn-success" id="addAIJobsUpload"> <spring:message
						code="anvizent.package.label.Add" />
				</button>
			</div>
			<div class='row form-group'>
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg "
						id="aiJobsUploadTable">
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.sNo" /></th>
								<th><spring:message code="anvizent.package.label.id" /></th>
								<th><spring:message code="anvizent.package.label.businessModal"/></th>
								<th>Model Name</th>
								<th><spring:message code="anvizent.package.label.jobName" /></th>
								<th><spring:message code="anvizent.package.label.isActive" /></th>
								<th><spring:message code="anvizent.package.label.edit" /></th>
								<th><spring:message code="anvizent.package.label.delete" /></th>
							</tr>
						</thead>
						<tbody>
						  <c:forEach items="${aiJobsUploadList}" var="aiUploadedJobs" varStatus="index">
						  	<tr>
						  		<td>${index.index+1}</td>
						  		<td>${aiUploadedJobs.rid}</td>
						  		<td>${aiUploadedJobs.businessName}</td>
						  		<td>${aiUploadedJobs.modelName}</td>
						  		<td>${aiUploadedJobs.jobName}</td>
						  		<td>${aiUploadedJobs.isActive ? "Yes":"No"} </td>
						  		<td><button class="btn btn-primary btn-sm editAIUplodedJob" value="${aiUploadedJobs.rid}" data-sourceid="${aiUploadedJobs.rid}" title="<spring:message code="anvizent.package.label.edit"/>" >
										<i class="fa fa-pencil" aria-hidden="true"></i>
								</button></td>
								<td><button class="btn btn-primary btn-sm deleteAIUplodedJob" value="${aiUploadedJobs.rid}" data-sourceid="${aiUploadedJobs.rid}" title="<spring:message code="anvizent.package.label.delete"/>" >
									<i class="fa fa-trash" aria-hidden="true"></i>
								</button>
						  	</tr>
						  
						  </c:forEach>
						
						</tbody>
					</table>
				</div>
			</div>
	<div class='row form-group '>
		<div class='col-sm-6'>
			<div class="alert alert-success successMessage"
				style="display: none;">
				<p class="successMessageText"></p>
			</div>
			<div class="alert alert-danger message" style="display: none;">
				<p class="messageText"></p>
			</div>
		</div>
	</div>
		<div class="modal fade" tabindex="-1" role="dialog"
			id="aiJobsUploadDiv" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content"  style="width:125%;">
					<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        	<h4 class="modal-title custom-modal-title aiJobUploadTitle"><spring:message code="anvizent.package.label.aiJobsUpload"/></h4>
					      </div>
						<div class="modal-body">
						<input type="hidden" id="rJobId">
						   <form:form method= "POST" id="jobFileForm_direct" enctype="multipart/form-data">
						   
						<%--    	<div class="row form-group">
								<label class="control-label col-sm-4"><spring:message
										code="anvizent.package.label.businessModal" /> :</label>
										<div class="col-sm-8">
										<input type="hidden" id="businessName" name="businessName">
										<select class="form-control" name="businessId" id="businessModalId"> 
									   		<option value="0" selected>Select Business Use Case</option>
										    <c:forEach items="${aiBussinessModalSet}" var="aiBusinessModal">
												<option value="${aiBusinessModal}"><c:out value="${aiBusinessModal}"/></option>
											</c:forEach>
									    </select>
									</div>
							</div> --%>
						   
						<%--    <div class="row form-group contextParamsDiv">
								<label class="control-label col-sm-4"><spring:message
										code="anvizent.package.label.aiJobsUploadContextParameters" /> : </label>
								<div class="col-sm-8">
										<select class="form-control" name="pid" id="genericContextParam"> 
									   		<option value="0" selected>Select Context parameters</option>
										    <c:forEach items="${aiContextParamList}" var="aiContext">
												<option value="${aiContext.pid}"><c:out value="${aiContext.paramName}"/>-<c:out value="${aiContext.paramValue}"/></option>
											</c:forEach>
									    </select>
								</div>
							</div> --%>
						   
						   <div class="row form-group aiModalDiv">
								<label class="control-label col-sm-4"><spring:message code="anvizent.package.label.aiModelName" /> : </label>
								<div class="col-sm-8">
										<select class="form-control" id="aiModalId"> 
										    <c:forEach items="${aiModalInfoList}" var="aiModal">
												<option value="${aiModal.id}"><c:out value="${aiModal.aIModelName}"/></option>
											</c:forEach>
									    </select>
								</div>
							</div>
						   
						   
						   
							<%-- <div class="row form-group hidden">
								<label class="control-label col-sm-4"><spring:message
										code="anvizent.package.label.jobType" /> :</label>
									<div class="jobType">
										<label class="radio-inline"> <input type="radio"
											value="true" id="aiJobType" name="jobTypeName" checked="checked" /><spring:message
										code="anvizent.package.label.jobType_Transform" />
										</label> <label class="radio-inline"> <input type="radio"
											value="false" id="aiJobType" name="jobTypeName" /><spring:message
										code="anvizent.package.label.jobType_BackTransform" />
										</label>
									</div>
							</div> --%>
							
							
							 <div id="aINewContextParametersDiv" style="margin-top: 5px;"></div>
							 
							<div class="aiSpecificContextParamsDiv hidden" id="ai_contextParameters">
							
									<div class="row form-group context_specParamsDiv">
										<label class="control-label col-sm-4 specificParamName"> </label>
										<div class="col-sm-8">
												 <div id="accordion-first" class="clearfix newAccord"><!-- standard start accordion -->
						               <div class="accordion" id="accordion2">
						               	<div class="accordion-group">
							               <div class="accordion-heading" style="border: 1px solid #dce1e4;background:#1096eb;">
							                  <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion2" href="#collapse">
							                  <span class="glyphicon glyphicon-plus-sign"></span>
							                  </a>
							               </div>
								           <div style="height: 0px;" id="collapse" class="accordion-body collapse">		                        
								                <div class="panel panel-info">
									                 <div class="panel-body">
									                 <input type="hidden" id="aiModalSelectedId">
										                <div id="aIContextParametersDiv" style="margin-top: 5px;"></div>
													        <div class="row-form-group contextKeyValue hidden" id="aicontextParameters">
																	<!-- <label class="control-label col-sm-4"></label>	 -->
																	<div class="row form-group keyValuPair">
																		<div class="col-sm-4">
																			<spring:message code = "anvizent.package.label.enterKey" var="enterKey"/>
																			<input class="form-control authBodyKey">
																		</div>
																		<div class="col-sm-4">
																			<spring:message code = "anvizent.package.label.enterValue" var ="enterValue"/>
																			<input class="form-control authBodyValue">
																		</div>
																		<div class="col-sm-4">
																			<button type="button" class="btn btn-primary btn-sm addContextParams">
																				<span class="glyphicon glyphicon-plus"></span>
																			</button>
																			<button type="button" class="btn btn-primary btn-sm deleteContextParams">
																				<span class="glyphicon glyphicon-trash"></span>
																			</button>
																		</div>
																	</div>
													        	</div>	
														</div>
								                	</div>
								               </div>
								         	</div>
							         	   </div>
							         	</div>
										</div>
									</div>
									
					         </div>
									
									

							
						<div class="row form-group jobNameDiv">
							<spring:message code="anvizent.package.label.aijobName" var="jobName"/>			 
							<label class="control-label col-sm-4"><c:out value="${jobName}"/> : </label>					
						   	<div class="col-sm-8">
						    	<input class="form-control jobName" type="text" data-maxlength="500" placeholder="${jobName}" id="jobName" name="jobName" />
						    </div>			   
						</div>	
							
							
						<div class="row form-group jobFileNameDiv">
							<spring:message code="anvizent.package.label.aiJobFileName" var="jobFileName"/>			 
							<label class="control-label col-sm-4"><c:out value="${jobFileName}"/> : </label>					
						   	<div class="col-sm-8">
						    	<input class="form-control uploadedJobFileNames" type="text" data-maxlength="500" placeholder="${jobFileName}" id="uploadedJobFileNames" name="uploadedJobFileNames" />
						    </div>			   
						</div>	
							
						<div class="jobFilesDiv">
							<label class="control-label col-sm-4 jobfile-label"><spring:message code="anvizent.package.label.aiJobFile"/> : </label>
				    		<div class="row form-group fileContainer">
					    		 <div class="row form-group fileTypeDiv"></div> 
							    <div class="row form-group jobFileData hidden"></div>
								</div>
						</div>
						<div class="row form-group existedJarFileDiv hidden">			 
							<label class="control-label col-sm-4"><spring:message code="anvizent.package.label.aiJobExistedJarFiles"/>:</label>					
						   	<div class="col-sm-8">
						   	<select id='existedJarjobFileNames' name = "existedJarjobFileNames"  multiple="multiple"  class='existedJarFile form-control' style="white-space:pre-wrap;">
						    	</select>
						    </div>			   
						</div>
							
							<div class="row form-group">
								<label class="control-label col-sm-4"><spring:message
										code="anvizent.package.label.activeStatus" /> :</label>
								<div class='col-sm-6'>
									<div class="activeStatus">
										<label class="radio-inline"> <input type="radio"
											value="true" id="active" name="active" checked="checked" />Yes
										</label> 
										<label class="radio-inline"> <input type="radio"
											value="false" id="active" name="active" />No
										</label>
									</div>
								</div>
							</div>
							</form:form>
						</div>
						<div class="modal-footer">
								<button type="button" id="saveAIJobUpload"
									class="btn btn-primary btn-sm">
									<spring:message code="anvizent.package.button.save" />
								</button>
								<button type="button" class="btn btn-primary btn-sm"
									id="editAIJobUpload" style="display: none" name='editAIJobUpload'>
									<spring:message code="anvizent.package.button.edit" />
								</button>
								<button type="button" class="btn btn-primary btn-sm"
									id="updateAIJobUpload" style="display: none"
									name='updateAIJobUpload'>
									<spring:message code="anvizent.package.button.update" />
								</button>
								<button type="button" class="btn btn-default" data-dismiss="modal" id="btn_Cancel">
									<spring:message code="anvizent.package.button.cancel" />
								</button>
						</div>
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
						        <p><spring:message code="anvizent.package.message.deletePackage.aiSourceUploadDelete"/><br>
						        <spring:message code="anvizent.package.message.deletePackage.areYouSureYouWantToDeleteAISourceUpload"/> 
					        </p>
					      </div>
					      <div class="modal-footer">
						        <button type="button" class="btn btn-primary" id="confirmDeleteConnection"><spring:message code="anvizent.package.button.yes"/></button>
						        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
					      </div>
				    </div> 
			  </div> 
		</div>
		 <div class="col-sm-6 job-files-div" style='display:none;'>
    	<div class="row form-group fileContainer">
	    	<label class="control-label col-sm-4 jobfile-label"><spring:message code="anvizent.package.label.jobFile"/> : </label>
	    	<div class="col-sm-6">
	    		<input type="file" class="jobFile" name="jobFile" data-buttonText="Find file">
		    </div>
		    <div class="col-sm-2">
			    <!-- <a href="#"  class="btn btn-primary btn-sm addJobFile">
		      		<span class="glyphicon glyphicon-plus"></span>
		    	</a> -->
		    	<!-- <a href="#"  class="btn btn-primary btn-sm remove_field deleteJobFile" style="display:none;">
		      		<span class="glyphicon glyphicon-trash"></span>
		    	</a> -->
	    	</div>	
	    </div>
    </div>
     <div class="modal fade" tabindex="-1" role="dialog" id="deleteAIJobsUpload" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title">
							Delete Job
						</h4>
					</div>
					<div class="modal-body">
						<p>
							Are you sure , do you want to delete the Job ?
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="confirmDeleteAIJobsUpload">
							<spring:message code="anvizent.package.button.yes" />
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.no" />
						</button>
					</div>
				</div>
			</div>
		</div>	
	</div>
