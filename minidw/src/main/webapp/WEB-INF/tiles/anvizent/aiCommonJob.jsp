<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.commonJob"/></h4>
 	</div>
 	
 	<div class="col-sm-10">
		<div id="successOrErrorMessage"></div>
	</div>	
 	<%-- <jsp:include page="admin_error.jsp"></jsp:include> 	 --%>

 	<input type="hidden" id="userId" value="<c:out value="${principal.userId}"/>">
 	
 	<form:form method= "POST" id="jobFileForm_direct" enctype="multipart/form-data">
		 		<div class="col-sm-8">
			    	<div class="panel panel-default">
			    		<div class="panel-heading">Add Common Job</div>
				    		<div class="panel-body">
							 	
						 	
						 	 <div class="job-files-div">
						    	<div class="row form-group fileContainer">
							    	<label class="control-label col-sm-4 jobfile-label"><spring:message code="anvizent.package.label.jobFile"/> : </label>
							    	<div class="col-sm-6">
							    		<input type="file" class="jobFile" name="jobFile" data-buttonText="Find file">
								    </div>
								    <div class="col-sm-2">
									    <a href="#"  class="btn btn-primary btn-sm addJobFile">
								      		<span class="glyphicon glyphicon-plus"></span>
								    	</a>
								    	<a href="#"  class="btn btn-primary btn-sm remove_field deleteJobFile" style="display:none;">
								      		<span class="glyphicon glyphicon-trash"></span>
								    	</a>
							    	</div>	
							    </div>	
						    </div>
						    
						    <div class="jobFilesDiv">
								<%-- <label class="control-label col-sm-4 jobfile-label"><spring:message code="anvizent.package.label.aiJobFile"/> : </label> --%>
					    		<div class="row form-group fileContainer">
						    		 <div class="row form-group fileTypeDiv"></div> 
								    <div class="row form-group jobFileData hidden"></div>
									</div>
							</div>
						    <div class="row form-group jobFileNameDiv">
								<spring:message code="anvizent.package.label.aiJobFileName" var="jobFileName"/>			 
								<label class="control-label col-sm-4"><c:out value="${jobFileName}"/> : </label>					
							   	<div class="col-sm-6">
							    	<input class="form-control uploadedJobFileNames" type="text" data-maxlength="500" placeholder="${jobFileName}" id="uploadedJobFileNames" name="uploadedJobFileNames" />
							    </div>			   
							</div>	
							<div class="row form-group">	
								<div class="col-sm-12">
									<button type="button" class="btn btn-primary btn-sm" id="saveAICommonJob"><spring:message code="anvizent.package.button.save"/></button>
								</div>
							</div>
			    		</div>
			    	</div>
			    </div>		
 	</form:form>
 	
 	
				<table class="table table-striped table-bordered tablebg" 
					id="CommmonJobTbl">
					<thead>
						<tr>
							<th><spring:message code="anvizent.package.label.sNo" /></th>
							<th><spring:message code="anvizent.package.label.aiJobFileName" /></th>
							<th><spring:message code="anvizent.package.label.delete" /></th>
						</tr>
					</thead>
					<tbody>
									<c:forEach items="${FileNames}" var="fileName" varStatus="index">
										<tr>
											<td>${index.index+1}</td>
											<td><c:out value="${fileName}" /></td>
											<td><button class="btn btn-primary btn-sm deleteCommonJobFile" value="${fileName}" title="<spring:message code="anvizent.package.label.delete"/>" >
									<i class="fa fa-trash" aria-hidden="true"></i>
								</button></td>						
										</tr>
									</c:forEach>
								</tbody>
				</table>
		<div class="modal fade" tabindex="-1" role="dialog" id="deleteAICommonJob" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title">
							Delete Common Job
						</h4>
					</div>
					<div class="modal-body">
						<p>
							Are you sure , do you want to delete the Common Job ?
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="confirmDeleteAICommonJob">
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
	