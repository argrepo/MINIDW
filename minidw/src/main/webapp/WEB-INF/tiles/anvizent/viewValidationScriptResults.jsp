<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
       <div class="dummydiv"></div>
		<ol class="breadcrumb">
		</ol>
       <div class="row form-group"><h4 class="alignText"><spring:message code = "anvizent.package.label.viewResults"/></h4></div>
<div class='col-sm-12' style="padding:0px;">
  <form:form modelAttribute="jobResultForm" method="POST" id="jobResultForm"  style="padding:0px 15px;">
   <jsp:include page="_error.jsp"></jsp:include>
   <input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
    <div class="row form-group" style="padding:5px;border-radius:4px;">	
      <label class="col-sm-3 control-label labelsgroup"><spring:message code="anvizent.package.label.validationname"/></label>
			<div class='col-sm-6' >	
				<form:input path="validationScriptname" class="form-control" readonly="true"/>
				<form:input path="scriptId" class="form-control script-id" disabled="true" type="hidden"/>
			</div>
    </div>
  </form:form>
  
         <div role="tabpanel" class="tab-pane fade active in tabpad" id="" aria-labelledby=""> 
		     <div class="row form-group">
		         <div class="table-responsive" >
					<table class="table table-striped table-bordered tablebg " id="viewScriptResultsTable">
						<thead>
							<tr>
								<th class="col-xs-3"><spring:message code="anvizent.package.label.batchId"/></th>
								<th class="col-xs-3"><spring:message code="anvizent.package.label.jobName"/></th>
								<th class="col-xs-2"><spring:message code="anvizent.package.label.startDate"/></th>
								<th class="col-xs-2"><spring:message code="anvizent.package.label.endDate"/></th>
								<th class="col-xs-2"><spring:message code="anvizent.package.label.jobStatus"/></th>
								<th class="col-xs-1"><spring:message code="anvizent.package.label.sourceRecords"/></th>
								<th class="col-xs-1"><spring:message code="anvizent.package.label.insertedRecords"/></th>
								<th class="col-xs-1"><spring:message code="anvizent.package.label.updatedRecords"/></th>
			                    <th class="col-xs-2"><spring:message code="anvizent.package.label.ViewErrorLog"/></th>	
			                    <th class="col-xs-2"><spring:message code="anvizent.package.label.download"/></th>						                    
							</tr>
						</thead>
						<tbody>							
							<c:forEach items="${jobResultList}" var="viewResult" varStatus="index">
							
									<tr>										
										<td><c:out value="${viewResult.batchId}"/></td>
										<td><c:out value="${viewResult.jobName}"/></td>
										<td><c:out value="${viewResult.startDate}"/></td>
										<td><c:out value="${viewResult.endDate}"/></td>
										<%-- <td><c:out value="${viewResult.runStatus}"/></td> --%>
										<c:choose>
											<c:when test="${viewResult.runStatus == 'success'}">
												<td class="smalltd"><spring:message code="anvizent.package.label.success"/></td>
											</c:when>
											<c:otherwise>
												<td class="smalltd"><spring:message code="anvizent.package.label.failure"/></td>
											</c:otherwise>
										</c:choose>	
										
										<td><c:out value="${viewResult.totalRecordsFromSource}"/></td>										 
										<td><c:out value="${viewResult.insertedRecords}"/></td>										 
										<td><c:out value="${viewResult.updatedRecords}"/></td>										 
										<c:choose>
											<c:when test="${viewResult.errorRowsCount == 1}">
												<td class="smalltd"><spring:message code="anvizent.package.label.0errors"/></td>
											</c:when>
											<c:otherwise>
												<td class="smalltd"><a class="btn btn-primary btn-sm tablebuttons view-error-log" data-batchId = "<c:out value="${viewResult.batchId}"/>"><b><c:out value="${viewResult.errorRowsCount}"/></b> <spring:message code = "anvizent.package.link.errors"/></a></td>
											</c:otherwise>
										</c:choose>	
										 <td>
										 <button type="button" class="btn btn-primary btn-sm back_btn downloadSampleTemplate" id="downloadBatchFiles" data-batchId="${viewResult.batchId}" >
						                      <span title="<spring:message code = "anvizent.package.label.DownloadTemplate"/>" class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
					                     </button>
										 </td>
									</tr>
						 	
							</c:forEach>							 
						</tbody>
					</table>
				</div>
			</div>
			<div class="pull-left col-sm-12">
			  <c:choose>	
			    <c:when test="${jobResultForm.pageMode == 'preload'}">
					<a href="<c:url value="/adt/package/dataValidation/preloadInfo"/>" class="btn btn-primary btn-sm startLoader back_btn"><spring:message code="anvizent.package.link.back"/></a>
				</c:when>
				<c:when test="${jobResultForm.pageMode == 'postload'}">
				   <a href="<c:url value="/adt/package/dataValidation/businessCasesInfo"/>" class="btn btn-primary btn-sm startLoader back_btn"><spring:message code="anvizent.package.link.back"/></a>
				</c:when>	
		      </c:choose>
			</div>
    	</div>
    	
    	<div class="modal fade" tabindex="-1" role="dialog" id="viewErrorLogs" data-backdrop="static" data-keyboard="false">
     		<div class="modal-dialog" style="width: 75%">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.errorLog"/></h4>
		      </div>
		      <div class="modal-body">
		            <div role="tabpanel" class="tab-pane fade active in tabpad" id="" aria-labelledby=""> 
					     <div class="row form-group">
					         <div class="table-responsive" >
								<table class="table table-striped table-bordered tablebg " id="errorLogsTable">
									<thead>
										<tr>
											<th><spring:message code="anvizent.package.label.errorId"/></th> 
											<th><spring:message code="anvizent.package.label.batchId"/></th> 
											<th><spring:message code="anvizent.package.label.errorCode"/></th>
											<th><spring:message code="anvizent.package.label.errorType"/></th>
											<th><spring:message code="anvizent.package.label.errorMessage"/></th>
											<th><spring:message code="anvizent.package.label.errorSyntax"/></th>
										</tr>
									</thead>
									<tbody>							
																	 
									</tbody>
								</table>
							</div>
						</div>
    				</div>        	 
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div>
		  </div>
     </div>
</div>
</div>