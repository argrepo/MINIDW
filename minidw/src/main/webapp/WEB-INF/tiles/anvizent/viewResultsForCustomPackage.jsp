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
		<form:hidden path="pageMode"/>	
		<div class="row form-group" style="padding:5px;border-radius:4px;">						
				<label class="col-sm-3 control-label labelsgroup">
						<spring:message code="anvizent.package.label.packageName"/></label>
				<div class='col-sm-6' >				    
					<form:input path="packageName" class="form-control" readonly="true"/>
					<form:input path="packageId" class="form-control package-id" disabled="true" type="hidden"/>
				</div>						
		</div>
		
		<div class="panel panel-default">
			  <div class="panel-heading">
			    <h3 class="panel-title"><spring:message code = "anvizent.package.label.searchResults"/></h3>
			  </div>
			  <div class="panel-body">
			  	<div class='row form-group'  style="margin-bottom: 5px;">		   
					<label class="control-label col-sm-3 labelsgroup"><spring:message code = "anvizent.package.label.jobStartDate"/></label>
					<label class="control-label col-sm-1 labelsgroup"><spring:message code = "anvizent.package.label.from"/></label>
					<div class='col-sm-2'>												
						<form:input path="fromDate" class="form-control fromdatepicker"/>				
					</div>
				 	<label class="control-label col-sm-1 labelsgroup"><spring:message code = "anvizent.package.label.to"/></label>
					<div class='col-sm-2'> 
						<form:input path="toDate" class="form-control todatepicker"/>					
					</div>			 
					<div class="pull-left col-sm-2" style="margin-top:4px;">		
						<input class="btn btn-primary btn-sm" type="button" name="searchJobResult" id="searchJobResult" value="<spring:message code = "anvizent.package.label.search"/>"  data-userid="${principal.userId}">		
						<input class="btn btn-primary btn-sm" type="button" id="resetCustomResults" value="<spring:message code = "anvizent.package.label.reset"/>">
					</div>			 
				</div>
			  </div>
		</div>
		
		
	</form:form>
		<div role="tabpanel" class="tab-pane fade active in tabpad" id="" aria-labelledby=""> 
		     <div class="row form-group">
		         <div class="table-responsive" >
					<table class="table table-striped table-bordered tablebg " id="viewResultsTable">
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.batchId"/></th>
								<th><spring:message code="anvizent.package.label.tableName"/></th>
								<th><spring:message code="anvizent.package.label.startDate"/></th>
								<th><spring:message code="anvizent.package.label.endDate"/></th>
								<th><spring:message code="anvizent.package.label.insertedRecords"/></th>
			                    <th class="smalltd"><spring:message code="anvizent.package.label.ViewErrorLog"/></th>							                    
							</tr>
						</thead>
						<tbody>							
							<c:forEach items="${viewResultList}" var="viewResult" varStatus="index">
									<tr>										
										<td><c:out value="${viewResult.batchId}"/></td>
										<td><c:out value="${viewResult.jobName}"/></td>
										<td><c:out value="${viewResult.startDate}"/></td>
										<td><c:out value="${viewResult.endDate}"/></td>
										<td><c:out value="${viewResult.insertedRecords}"/></td>										 
										<c:choose>
											<c:when test="${viewResult.errorRowsCount == 0}">
												<td class="smalltd"><spring:message code="anvizent.package.label.0errors"/></td>
											</c:when>
											<c:otherwise>
												<td class="smalltd"><a class="btn btn-primary btn-sm tablebuttons viewCustomErrors" data-batchId = "<c:out value="${viewResult.batchId}"/>"><b><c:out value="${viewResult.errorRowsCount}"/></b> <spring:message code = "anvizent.package.link.errors"/></a></td>
											</c:otherwise>
										</c:choose>	
									</tr>
						 	
							</c:forEach>							 
						</tbody>
					</table>
				</div>
			</div>
			<div class="pull-left col-sm-12">
				<c:choose>
					<c:when test="${jobResultForm.pageMode == 'schedule' }">
					  <a type="reset" href="<c:url value="/adt/package/schedule"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
					</c:when>
					<c:otherwise>
						<a href="<c:url value="/adt/package/custompackage" />" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
					</c:otherwise>
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
											<th><spring:message code="anvizent.package.label.errorMessage"/></th>
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