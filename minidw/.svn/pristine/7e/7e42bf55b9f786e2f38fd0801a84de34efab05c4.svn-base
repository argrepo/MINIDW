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
 	<jsp:include page="admin_error.jsp"></jsp:include> 	

 	<input type="hidden" id="userId" value="<c:out value="${principal.userId}"/>">
 	<c:url value="/admin/commonJob" var="url"/>
 	<c:url value="/admin/commonJob/add" var="addUrl"/>
 	<input type="hidden" value="${url }/edit" id="editUrl"/>
 	<input type="hidden" value="${url }/save" id="saveUrl"/>
 	<input type="hidden" value="${url }/update" id="updateUrl"/>
 	<form:form modelAttribute="commonJobForm" action="${url}" enctype="multipart/form-data">
		 
		 <c:if test="${commonJobForm.pageMode == 'list'}">
			<div class="row form-group">
				<a href="${addUrl}" class="btn btn-success btn-sm" id="createCommonJob" style="float:right; margin-right: 1.5em;">
					<spring:message code="anvizent.package.label.add"/>
				</a>			
			</div>
		 	
	 		<div class='row form-group'>
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg" id="commonJobsInfoTable">
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.sNo"/></th>
								<th><spring:message code="anvizent.package.label.jobType"/></th>
								<th><spring:message code="anvizent.package.label.jobFileName"/></th>
								<th><spring:message code="anvizent.package.label.isActive"/></th>
								<th><spring:message code="anvizent.package.label.edit"/></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${commonJobdetails}" var="commonJob" varStatus="index">
								<tr>
									<td>${index.index + 1}</td>
									<td><c:out value="${commonJob.jobType}" /></td>
									<td><c:out value="${commonJob.jobFileName}" /></td>
									<td>${commonJob.isActive ? 'Yes' : 'No'}</td>
									<td>
										<button class="btn btn-primary btn-sm tablebuttons edit" name="id" value="${commonJob.id}" title="<spring:message code="anvizent.package.label.edit"/>" >
											<i class="fa fa-pencil" aria-hidden="true"></i>
										</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
		    </div> 
		 </c:if>
		 <c:if test="${commonJobForm.pageMode == 'add'}">
		 		<div class="col-sm-12">
			    	<div class="panel panel-default">
			    		<div class="panel-heading">Add Common Job</div>
			    		<div class="panel-body">
			    			<div class='row form-group'>
						 		<label class="col-sm-2 control-label"><spring:message code="anvizent.package.label.jobType"/> :</label>
								<div class="col-sm-6">
									<form:select path="jobType" cssClass="form-control">
										<spring:message code="anvizent.package.label.selectJobType" var="selectJobType"/>
										<form:option value="0">${selectJobType}</form:option>
										<form:option value="IL">IL</form:option>
										<form:option value="DL">DL</form:option>
									</form:select>
								</div>
						 	</div>
						 	
						 	<div class='row form-group'>
						 		<label class="col-sm-2 control-label"><spring:message code="anvizent.package.label.jobFile"/> :</label>
								<div class="col-sm-6">
									<input type="file" name="jobFile" data-buttonText="Find file">
								</div>
						 	</div>
						 	
						 	<div class="row form-group">			 
								<label class="control-label col-sm-2">
							    	<spring:message code="anvizent.package.label.activeStatus"/>		    	
							    </label>
							   	<div class="col-sm-6">
								   	<div class="activeStatus">
								   		<label class="radio-inline control-label">
									   		<form:radiobutton path="isActive" value="true"/><spring:message code="anvizent.package.button.yes"/>
									    </label>
									    <label class="radio-inline control-label">
									   	 	<form:radiobutton path="isActive" value="false"/><spring:message code="anvizent.package.button.no"/>
									    </label>
								   	</div>
							    </div>			   
							</div>
							<input type="hidden">
							<div class="row form-group">	
								<div class="col-sm-12">
									<button type="button" class="btn btn-primary btn-sm" id="saveCommonJob"><spring:message code="anvizent.package.button.save"/></button>
									<a class="btn btn-primary btn-sm back_btn" href='${url}'><spring:message code="anvizent.admin.button.Back"/></a>
								</div>
							</div>
			    		</div>
			    	</div>
			    </div>		
		 </c:if>	
	 	
	 	 <c:if test="${commonJobForm.pageMode == 'edit'}">
		 		<div class="col-sm-12">
			    	<div class="panel panel-default">
			    		<div class="panel-heading">Update Common Job</div>
			    		<div class="panel-body">
			    			<div class='row form-group'>
						 		<label class="col-sm-2 control-label"><spring:message code="anvizent.package.label.jobType"/> :</label>
								<div class="col-sm-6">
									<form:select path="jobType" cssClass="form-control">
										<form:option value="IL">IL</form:option>
										<form:option value="DL">DL</form:option>
									</form:select>
								</div>
						 	</div>
						 	
						 	<div class='row form-group'>
						 		<label class="col-sm-2 control-label"><spring:message code="anvizent.package.label.jobFile"/> :</label>
								<div class="col-sm-6">
									<input type="hidden" name="id" value="<c:out value="${commonJobForm.id}"/>">
									<span><c:out value="${commonJobForm.jobFileName}"/></span>
								</div>
						 	</div>
						 	
						 	<div class="row form-group">			 
								<label class="control-label col-sm-2">
							    	<spring:message code="anvizent.package.label.activeStatus"/>		    	
							    </label>
							   	<div class="col-sm-6">
								   	<div class="activeStatus">
								   		<label class="radio-inline control-label">
									   		<form:radiobutton path="isActive" value="true"/><spring:message code="anvizent.package.button.yes"/>
									    </label>
									    <label class="radio-inline control-label">
									   	 	<form:radiobutton path="isActive" value="false"/><spring:message code="anvizent.package.button.no"/>
									    </label>
								   	</div>
							    </div>			   
							</div>
							<input type="hidden">
							<div class="row form-group">	
								<div class="col-sm-12">
									<button type="button" class="btn btn-primary btn-sm" id="updateCommonJob"><spring:message code="anvizent.package.button.update"/></button>
									<a class="btn btn-primary btn-sm back_btn" href='${url}'><spring:message code="anvizent.admin.button.Back"/></a>
								</div>
							</div>
			    		</div>
			    	</div>
			    </div>		
		 </c:if>
	 	
 	</form:form>
</div> 	