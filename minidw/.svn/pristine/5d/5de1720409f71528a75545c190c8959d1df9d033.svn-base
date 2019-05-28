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
	
	   <c:url value="/adt/package/historicalLoad/jobResultsForHistoricalLoad" var="getUrl"/>
	   <c:url value="/adt/package/searchCurrencyLodJobResults" var="currencyLoadUrl"/>
	   
	
	<form:form modelAttribute="jobResultForm" method="POST" id="jobResultForm"  style="padding:0px 15px;">
		<jsp:include page="_error.jsp"></jsp:include>
		<div class="col-md-12 message-class"></div>
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
		<form:hidden path="pageMode"/>	
		<div class="row form-group" style="padding:5px;border-radius:4px;">						
			 <c:choose>
			    <c:when test="${jobResultForm.pageMode == 'historical' || jobResultForm.pageMode == 'xref' }">
				     <label class="col-sm-3 control-label labelsgroup">Condition Name :</label>
				<div class='col-sm-6' >	
					<form:input path="xrefConditionName" class="form-control" readonly="true"/>
					<form:input path="xrefConditionId" class="form-control package-id" disabled="true" type="hidden"/>
					<form:input path="packageId" class="form-control" disabled="true" type="hidden"/>
				</div>
				</c:when>
				<c:when test="${jobResultForm.pageMode == 'hierarchical'}">
						<label class="col-sm-3 control-label labelsgroup">Hierarchy Name :</label>
			    	<div class='col-sm-6' >
				     	 <form:input path="hierarchyName" class="form-control" readonly="true"/>
						 <form:input path="hierarchyId" class="form-control package-id" disabled="true" type="hidden"/>
			        </div>
				</c:when>
				
				<c:when test="${jobResultForm.pageMode == 'CurrencyLoad' }">
			   <label class="col-sm-3 control-label labelsgroup"><spring:message code="anvizent.package.label.from"/> : </label>
				<div class='col-sm-6' >	
					<input  class="form-control" disabled="disabled" value="Currency Load"/>
				</div>
				</c:when>
				<c:when test="${jobResultForm.pageMode == 'DefaultCurrencyLoad' }">
			   <label class="col-sm-3 control-label labelsgroup">From : </label>
				<div class='col-sm-6' >	
					<input  class="form-control" disabled="disabled" value="Default Currency Load"/>
				</div>
				</c:when>
							    
			 <c:otherwise>
			   <label class="col-sm-3 col-md-3 col-lg-2 control-label labelsgroup"><spring:message code="anvizent.package.label.packageName"/></label>
				 <div class='col-sm-4' >		
					<form:hidden path="packageName" class="form-control" readonly="true"/>
				 	<form:hidden path="packageId" class="form-control package-id"/>
				 	<div class="txt-break">${jobResultForm.packageName}</div>
				 </div>
				 
				   <c:if test= "${not empty jobResultForm.dlName}">
				    <label class="col-sm-3 col-md-3 col-lg-2 control-label labelsgroup"><spring:message code="anvizent.package.label.moduleName"/></label>
					   <div class='col-sm-4' >	
					      <form:hidden path="dlName" class="form-control" readonly="true"/>
						  <form:hidden path="dlId" class="form-control dl-id" />
						  <div class="txt-break">${jobResultForm.dlName}</div>
					    </div>
					</c:if>
			 </c:otherwise>
			</c:choose>
										
		</div>
		
		<div class="panel panel-default">
			  <div class="panel-heading">
			    <h3 class="panel-title"><spring:message code = "anvizent.package.label.searchResults"/></h3>
			  </div>
			  <div class="panel-body">
			  	<div class='row form-group'  style="margin-bottom: 5px;">		   
					<label class="control-label col-md-2 labelsgroup no-pad text-right"><spring:message code = "anvizent.package.label.jobStartDate"/></label>
					<label class="control-label col-md-1 labelsgroup no-pad text-right"><spring:message code = "anvizent.package.label.from"/></label>
					<div class='col-sm-2'>												
						<form:input path="fromDate" class="form-control fromdatepicker"/>				
					</div>
				 	<label class="control-label col-md-1 labelsgroup no-pad text-right"><spring:message code = "anvizent.package.label.to"/></label>
					<div class='col-sm-2'> 
						<form:input path="toDate" class="form-control todatepicker"/>					
					</div>			 
					<div class="pull-left col-sm-2 no-pad" style="margin-top:4px;">		
						<c:choose>
			             <c:when test="${jobResultForm.pageMode == 'historical' }">
				         <input class="btn btn-primary btn-sm" type="button" name="searchJobResultForHistoricalLoad" id="searchJobResultForHistoricalLoad" data-getUrl = "<c:out value="${getUrl}"/>" value="<spring:message code = "anvizent.package.label.search"/>"  data-userid="<c:out value="${principal.userId}"/>">
				          </c:when>
				          
			            <c:when test="${jobResultForm.pageMode == 'CurrencyLoad' }">
			            	<input class="btn btn-primary btn-sm" type="button" name="searchCurrencyLodJobResults" id="searchCurrencyLodJobResults" data-currencyloadurl = "<c:out value="${currencyLoadUrl}"/>"  value="<spring:message code = "anvizent.package.label.search"/>"  data-userid="<c:out value="${principal.userId}"/>">
			            </c:when>
			            
			            <c:otherwise>
			            	<input class="btn btn-primary btn-sm" type="button" name="searchJobResult" id="searchJobResult" value="<spring:message code = "anvizent.package.label.search"/>"  data-userid="<c:out value="${principal.userId}"/>">
			            	<input class="btn btn-primary btn-sm" type="button" id="resetResults" value="<spring:message code = "anvizent.package.label.reset"/>">
			            </c:otherwise>
			           </c:choose>
								
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
								<th><spring:message code="anvizent.package.label.jobName"/></th>
								<th><spring:message code="anvizent.package.label.startDate"/></th>
								<th><spring:message code="anvizent.package.label.endDate"/></th>
								<th><spring:message code="anvizent.package.label.jobStatus"/></th>
								<th><spring:message code="anvizent.package.label.sourceRecords"/></th>
								<th><spring:message code="anvizent.package.label.insertedRecords"/></th>
								<th><spring:message code="anvizent.package.label.updatedRecords"/></th>
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
											<c:when test="${viewResult.errorRowsCount == 0}">
												<td class="smalltd"><spring:message code="anvizent.package.label.0errors"/></td>
											</c:when>
											<c:otherwise>
												<td class="smalltd"><a class="btn btn-primary btn-sm tablebuttons view-error-log" data-batchId = "<c:out value="${viewResult.batchId}"/>"><b><c:out value="${viewResult.errorRowsCount}"/></b> <spring:message code = "anvizent.package.link.errors"/></a></td>
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
			    <c:when test="${jobResultForm.pageMode == 'historical' }">
				  	<a type="reset" href="<c:url value="/adt/package/historicalLoad"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
				</c:when>
				<c:when test="${jobResultForm.pageMode == 'schedule' }">
				  	<a type="reset" href="<c:url value="/adt/package/schedule"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
				</c:when>			    
				<c:when test="${jobResultForm.pageMode == 'CurrencyLoad' }">
					<a type="reset" href="<c:url value="/adt/currencyLoad"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
				</c:when>			    
				<c:when test="${jobResultForm.pageMode == 'DefaultCurrencyLoad' }">
					<a type="reset" href="<c:url value="/admin/currencyIntegration"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
				</c:when>			    
				<c:when test="${jobResultForm.pageMode == 'xref' }">
					<a type="reset" href="<c:url value="/adt/crossReference/getIlList"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
				</c:when>
				<c:when test="${jobResultForm.pageMode == 'hierarchical' }">
					<a type="reset" href="<c:url value="/adt/hierarchical"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
				</c:when>
			 	<c:otherwise>
					<a href="<c:url value="/adt/standardpackage" />" class="btn btn-primary btn-sm startLoader back_btn"><spring:message code="anvizent.package.link.back"/></a>
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