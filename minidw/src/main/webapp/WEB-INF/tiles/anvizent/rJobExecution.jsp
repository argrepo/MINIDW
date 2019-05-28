<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
        
    <div class='row form-group'>
		<h4 class="alignText"> <spring:message code="anvizent.package.label.aiJobExecutionStatus" /> </h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<%-- <jsp:include page="admin_error.jsp"></jsp:include> --%>
		<input type="hidden" id="userID"
			value="<c:out value="${principal.userId}"/>">
	</div>
	<div class="col-sm-10">
		<div id="successOrErrorMessage"></div>
	</div>
	<div class="row form-group">
	
					<label class="col-sm-3 control-label labelsgroup"><spring:message code="anvizent.package.label.businessModal"/></label>
					 <div class='col-sm-3' >	
						<input name="businessProblem" value="${businessName}" class="form-control" readonly="readonly"/>
						<input id="businessName" name="businessName" value="${businessName}" class="form-control busProblem" type="hidden"/>
					 </div>
	</div>
	
	<div class="row form-group">
		<div class="col-xs-12">
		<div class="row form-group tblIntern">
			
				<div class='row form-group'>
					<div class="table-responsive">
						<div class='row form-group'><button type="button" style="float:right;margin-right: 1.5em;" class="btn btn-primary aiJobRefresh" ><span class="glyphicon glyphicon-refresh"></span></button></div>
						<table class="table table-striped table-bordered tablebg " id="rJobExecutionTable">
							<thead>
								<tr>
									<th><spring:message code="anvizent.package.label.sNo"/></th>
									<th><spring:message code="anvizent.package.label.runId"/></th>
									<th><spring:message code="anvizent.package.label.businessModal"/></th>
									<th><spring:message code="anvizent.package.label.aiModel"/></th>
									<th><spring:message code="anvizent.package.label.sourceCount"/></th>
									<th><spring:message code="anvizent.package.label.stagingCount"/></th>
									<th><spring:message code="anvizent.package.label.aILCount"/></th>
									<th><spring:message code="anvizent.package.label.aOLCount"/></th>
									<th><spring:message code="anvizent.package.label.errorMessage"/></th>
									<th><spring:message code="anvizent.package.label.jobStartTime"/></th>
									<th><spring:message code="anvizent.package.label.jobEndTime"/></th>
									<th><spring:message code="anvizent.package.label.jobDuration"/></th>
									<th><spring:message code="anvizent.package.label.jobRunStatus"/></th>
									<th><spring:message code="anvizent.package.label.jobLogFileName"/></th>
								</tr>
							</thead>
							<tbody>
							    <c:choose>
					         <c:when test="${not empty rJobExecution}">
					            <c:forEach items="${rJobExecution}" var="jobExecution" varStatus="index">
														<tr>
														    <td>${index.index+1}</td>
														    <td>${jobExecution.runId}</td>
															 <td>${jobExecution.businessProblem}</td>
															  <td>${jobExecution.aiModel}</td>
															   <td>${jobExecution.sourceCount}</td>
															    <td>${jobExecution.stagingCount}</td>
															     <td>${jobExecution.aILCount}</td>
															      <td>${jobExecution.aOLCount}</td>
															       <td>${jobExecution.errorMsg}</td>
															        <td>${jobExecution.jobStartTime}</td>
															         <td>${jobExecution.jobEndTime}</td>
															         <td>${jobExecution.jobDuration}</td>
															          <td>${jobExecution.jobRunStatus}</td>
															          
															          <td><input type="button" id="viewErrorLog" value="<spring:message code="anvizent.package.button.aIerrorLog"/>" data-errorLog="${jobExecution.jobLogFileName}" class="btn btn-primary btn-sm"></td>
															          
															           <!-- <td data-errorLogFile="${jobExecution.jobLogFileName}">Error Log</td> -->
														</tr>
													</c:forEach>
					         </c:when>
                              <c:otherwise>
                              <tr><td rowspan="13"><spring:message code="anvizent.package.label.noDataFound"/></td>
                             </c:otherwise>
                            </c:choose>
							</tbody>
						</table>
					</div>
			    </div>
			    <div>
			    <c:url value="/adt/package/ai/businessModels" var="businessModel"></c:url>
			    <a href="${businessModel}" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.link.back"/></a>
			    </div>
     	</div>
		</div>
	</div>
</div>
		
		
