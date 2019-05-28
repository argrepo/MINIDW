<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.clientSpecificIL"/></h4>
 	</div>
	
	<jsp:include page="admin_error.jsp"></jsp:include>

 	<input type="hidden" id="userId" value="<c:out value="${principal.userId}"/>">
 	<c:url value="/admin/iLInfo/clientMapping/specificIL" var="url"/>
 	<input type="hidden" value="${url }/save" id="saveUrl"/>
 	
 	<form:form modelAttribute="clientSpecificILDLForm" action="${url}" enctype="multipart/form-data">
		 	<div class="row form-group">
		 		<label class="col-sm-3 labelsgroup control-label"><spring:message code="anvizent.package.label.client"/> :</label>
				<div class="col-sm-8">
					<form:select path="clientId" cssClass="form-control">
						<spring:message code="anvizent.package.label.selectClient" var="selectClient" />
						<form:option value="0">${selectClient }</form:option>
						<form:options items="${allClients}" />
					</form:select>
				</div>
		 	</div>
	
			<c:if test="${clientSpecificILDLForm.clientId != null && clientSpecificILDLForm.clientId != '0' }">
				<div class="row form-group">
			 		<label class="col-sm-3 labelsgroup control-label"><spring:message code="anvizent.package.label.iL"/> :</label>
					<div class="col-sm-8">
						<form:select path="iLId" cssClass="form-control">
							<spring:message code="anvizent.package.label.selectIL" var="selectIL" />
							<form:option value="0">${selectIL}</form:option>
							<form:options items="${iLsInfo}" />
						</form:select>
					</div>
			 	</div>
			 	
			 	<c:if test="${clientSpecificILDLForm.iLId != null && clientSpecificILDLForm.iLId != '0' }">
			 		<div class="col-sm-12">
				    	<div class="panel panel-default">
				    		<div class="panel-heading"><spring:message code="anvizent.package.label.AddUpdateClientSpecificILInfo" /></div>
				    		<div class="panel-body">
				    			<div class="row form-group">
				    				<label class="col-sm-3 labelsgroup control-label"><spring:message code="anvizent.package.label.DefaultJob" /> <spring:message code="anvizent.package.label.version" /> :</label>
				    				<div class="col-sm-8">
				    					<form:hidden path="defaultJobVersion"/>
				    					<span><c:out value="${not empty clientSpecificILDLForm.defaultJobVersion ? clientSpecificILDLForm.defaultJobVersion : '-'}"/></span>
				    				</div>
				    			</div>
				    			
				    			<div class="row form-group">
							 		<label class="col-sm-3 labelsgroup control-label"><spring:message code="anvizent.package.label.defaultJobName"/> :</label>
									<div class="col-sm-8">
										<form:hidden path="defaultJobName"/>
										<span><c:out value="${clientSpecificILDLForm.defaultJobName}"/></span>
									</div>
							 	</div>
							 	
							 	<div class="row form-group">
							 		<label class="col-sm-3 labelsgroup control-label"><spring:message code="anvizent.package.label.defaultJarFileNames"/> :</label>
									<div class="col-sm-8">
										<form:hidden path="defaultJobJarFileNames"/>
										<span><c:out value="${clientSpecificILDLForm.defaultJobJarFileNames}"/></span>
									</div>
							 	</div>
							 	
							 	<div class="row form-group">
							 		<label class="col-sm-3 labelsgroup control-label"><spring:message code="anvizent.package.label.useDefault"/> :</label>
									<div class="col-sm-8">
										<form:checkbox path="useDefault"/>
									</div>
							 	</div>
							 	
							 	<c:if test="${not empty clientSpecificILDLForm.currentClientJobVersion}">
									<div class="row form-group">
					    				<label class="col-sm-3 labelsgroup control-label">Current Client Specific Job <spring:message code="anvizent.package.label.version" /> :</label>
					    				<div class="col-sm-8">
					    					<form:hidden path="currentClientJobVersion"/>
					    					<span><c:out value="${not empty clientSpecificILDLForm.currentClientJobVersion ? clientSpecificILDLForm.currentClientJobVersion : '-'}"/></span>
					    				</div>
					    			</div>							 	
							 	</c:if>
							 	
							 	<c:if test="${clientSpecificILDLForm.currentClientJobName != null && not empty clientSpecificILDLForm.currentClientJobName}">
							 		<div class="row form-group">
								 		<label class="col-sm-3 labelsgroup control-label"><spring:message code="anvizent.package.label.currentClientSpecificJobName"/> :</label>
										<div class="col-sm-8">
											<form:hidden path="currentClientJobName"/>
											<span><c:out value="${clientSpecificILDLForm.currentClientJobName}"/></span>
										</div>
								 	</div>
							 	</c:if>
							 	
							 	<c:if test="${clientSpecificILDLForm.currentClientJarFileNames != null && not empty clientSpecificILDLForm.currentClientJarFileNames}">
							 		<div class="row form-group">
								 		<label class="col-sm-3 labelsgroup control-label"><spring:message code="anvizent.package.label.currentClientSpecificJarFileNames"/> :</label>
										<div class="col-sm-8">
											<form:hidden path="currentClientJarFileNames"/>
											<span><c:out value="${clientSpecificILDLForm.currentClientJarFileNames}"/></span>
										</div>
								 	</div>
								 	
								 	<div class="row form-group">
								 		<label class="col-sm-3 labelsgroup control-label"><spring:message code="anvizent.package.label.currentClientSpecificJarFiles"/> :</label>
								 		<div class="col-sm-8">
								 			<c:forEach items="${clientSpecificILDLForm.currentClientJarFileNameList}" var="jobFiles">
									 			<div class="currentJobFilesDiv">
									 				<label class="checkbox" style="margin-left: 20px;">
									 					<input type="checkbox" name="useCurrentJobFile" class="useCurrentJobFile">
									 				</label>	
									 				<span style="margin-left: 20px;"><c:out value="${jobFiles}"/></span>
									 				<input type="hidden" class="currentJobFileName" value="<c:out value="${jobFiles}"/>">
									 			</div>	
								 			</c:forEach>
								 		</div>
								 	</div>
							 	</c:if>
							 	
							 	<div class="row form-group">
							 		<spring:message code="anvizent.package.label.version" var="version"/>
				    				<label class="col-sm-3 labelsgroup control-label"><spring:message code="anvizent.package.label.ClientSpecificJob"/><c:out value="${version}"/> :</label>
				    				<div class="col-sm-8">
				    					<form:input path="clientSpecificJobVersion" cssClass="form-control" placeHolder="Client Specific Job  ${version}" data-minlength="1" data-maxlength="45"/>
				    				</div>
				    			</div>
							 	
							 	<div class="row form-group">
							 		<spring:message code="anvizent.package.label.clientSpecificJobName" var="clientSpecificJobName" />
							 		<label class="col-sm-3 labelsgroup control-label"><c:out value="${clientSpecificJobName}"/> :</label>
									<div class="col-sm-8">
										<form:input path="clientSpecificJobName" placeHolder="${clientSpecificJobName}" data-regex="/^[a-zA-Z0-9/_/-/./(/)]*$/" data-minlength="1" data-maxlength="500" cssClass="form-control"/>
									</div>
							 	</div>
							 	
							 	<div class="row form-group">
							 		<spring:message code="anvizent.package.label.clientSpecificJarFileNames" var="clientSpecificJarFileNames"/>
							 		<label class="col-sm-3 labelsgroup control-label"><c:out value="${clientSpecificJarFileNames}"/> :</label>
									<div class="col-sm-8">
										<form:hidden path="clientSpecificJobJarFileNames"/>
										<input type="text" disabled="disabled" class="form-control" id="jobFileNames" placeholder="${clientSpecificJarFileNames}">
									</div>
							 	</div>
							 	
							 	<div class="jobFilesDiv">					
					    			<div class="row form-group fileContainer">
								    	<label class="control-label labelsgroup col-sm-3 jobfile-label"><spring:message code="anvizent.package.label.clientSpecificJarFiles"/> :</label>
								    	<div class="col-sm-6">
								    		<input type="file" class="jobFile" name="jobJarFiles" data-buttonText="Find file">
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
								
								
								<div class="row form-group">			 
									<label class="col-sm-3 labelsgroup control-label"><spring:message code="anvizent.package.label.ExistedJarFiles"/>:</label>					
								   	<div class="col-sm-8">
								    	<select class="existedJarFile form-control" multiple="multiple" style="white-space:pre-wrap;">
								    			<c:forEach var="jarFile" items="${jarFilesList}">
								    				<option value="<c:out value="${jarFile.fileName}"/>" ><c:out value="${jarFile.fileName}"/></option>
								    			</c:forEach>
								    	</select>
								    </div>			   
								</div>
								
								<div class="row form-group">
							 		<div class='col-sm-6'>
										<button type="button" class="btn btn-sm btn-primary" id="save"><spring:message code="anvizent.package.label.submit"/></button>
									</div>
							 	</div>
				    		</div>
				    	</div>
					</div>
			 	</c:if>
		 	</c:if>
	
		 	
	</form:form>
	
	<div class="col-sm-6 job-files-div" style='display:none;'>
    	<div class="row form-group fileContainer">
	    	<label class="control-label labelsgroup col-sm-3 jobfile-label"><spring:message code="anvizent.package.label.jobFile"/> : </label>
	    	<div class="col-sm-6">
	    		<input type="file" class="jobFile" name="jobJarFiles" data-buttonText="Find file">
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
	
</div> 	