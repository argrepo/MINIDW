<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
        
    <div class='row form-group'>
		<h4 class="alignText"> <spring:message code="anvizent.package.label.packageExecutionStatus" /> </h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<jsp:include page="_error.jsp"></jsp:include>
		<input type="hidden" id="userID"
			value="<c:out value="${principal.userId}"/>">
	</div>
	<div class="col-sm-10">
		<div id="successOrErrorMessage"></div>
	</div>
	<div class="row form-group">
	
					<label class="col-sm-3 control-label labelsgroup"><spring:message code="anvizent.package.label.packageName"/></label>
					 <div class='col-sm-3' >	
						<input name="packageName" value="${packageName}" class="form-control" readonly="readonly"/>
						<input id="packageId" name="packageId" value="${packageId}" class="form-control package-id" type="hidden"/>
					 </div>
					<c:if test= "${packageId == 0}">
					   <label class="col-sm-3 control-label labelsgroup"><spring:message code="anvizent.package.label.moduleName"/></label> 
					    <div  class='col-sm-3'>
					      <input name="dlName" value="${dlName}" class="form-control" readonly="readonly"/>
						  <input id="dlId" name="dlId" value="${dlId}" class="form-control dl-id" type="hidden"/>
					    </div>
					</c:if>
	</div>
	   <div class='row form-group'>
					    <label class="col-sm-3 control-label labelsgroup"><spring:message code="anvizent.package.label.pagenumber"/> :</label>
					   <div class='col-sm-3' >	
						<select class="form-control" id="executionPaginationId"> 
											<c:forEach var = "i" begin = "1" end = "${totalCount}">
												<option value="${i}"><c:out value="${i}"/></option>
											</c:forEach>
						</select> 
					 </div>
	</div>
	<div class="row form-group">
		<div class="col-xs-12">
		<div class="row form-group tblIntern">
				<div class='row form-group'>
					<div class="table-responsive">
						<div class='row form-group'><button type="button" style="float:right;margin-right: 1.5em;" class="btn btn-primary refresh" ><span class="glyphicon glyphicon-refresh"></span></button></div>
						<table class="table table-striped table-bordered tablebg " id="packageExecutionTable">
							<thead>
								<tr>
									<th><spring:message code="anvizent.package.label.sNo"/></th>
									<th><spring:message code="anvizent.package.label.executionID"/></th>
									 
									<th><spring:message code="anvizent.package.label.runtype"/></th>
									<th><spring:message code="anvizent.package.label.initiatedFrom"/></th>
									 
									<th><spring:message code="anvizent.package.label.uploadStatus"/></th>
									<th><spring:message code="anvizent.package.label.executeStatus"/></th>
									<th><spring:message code="anvizent.package.label.druidStatus"/></th>
								</tr>
							</thead>
							<tbody id="packageExecutionTableTbody">
							    <c:choose>
					         <c:when test="${not empty packageExecutionList}">
					            <c:forEach items="${packageExecutionList}" var="packageExecutionMap" varStatus="index">
														<tr>
														    <td>${index.index+1}</td>
															<td>${packageExecutionMap.executionId}</td>
															 
															<td>${packageExecutionMap.runType}</td>
															<c:choose>
															  <c:when test="${packageExecutionMap.initiatedFrom == 'schedule'}">
															    <td><spring:message code="anvizent.package.label.schedule"/> </td>
															  </c:when>
															  <c:when test ="${packageExecutionMap.initiatedFrom == 'runwithscheduler'}">
															     <td> <spring:message code="anvizent.package.label.runNow"/></td>
															  </c:when>
															  <c:otherwise>
															  <td><spring:message code="anvizent.package.link.runNow"/></td>
															  </c:otherwise>
															 </c:choose>
															 
															  <td>${packageExecutionMap.uploadStatus}<br>
															    
															    <a href="#" data-executionId='${packageExecutionMap.executionId}' data-uploadOrExecution='upload' id="uploadExecutionStatusInfo" style="text-transform: capitalize;"><spring:message code="anvizent.package.label.info"/></a>  |
															     <a href="#" onClick="window.open('<c:url value="/adt/package/executionInfo/${packageExecutionMap.executionId}/upload"/>', 'Comments', 'width=1700,height=700')" data-executionId='${packageExecutionMap.executionId}' data-uploadOrExecution='upload' id="executionStatus" style="text-transform: capitalize;"><spring:message code="anvizent.package.label.detailedInfo"/></a>
															     <br>
														    	    <spring:message code="anvizent.package.label.startDate"/> : ${not empty packageExecutionMap.uploadStartDate ?packageExecutionMap.uploadStartDate :'-'}
														    	      <br>
														    	        <spring:message code="anvizent.package.label.endDate"/> : ${not empty packageExecutionMap.lastUploadedDate? packageExecutionMap.lastUploadedDate :'-'}
															     </td>
															 	
															 <c:choose>
															 <c:when test="${empty packageExecutionMap.executionStatus }">
														    	<td>-</td>
															  </c:when>
															  <c:when test="${packageExecutionMap.executionStatus != 'IGNORED'}">
														    	<td>${packageExecutionMap.executionStatus}<br>
														    		<a href="#" data-executionId='${packageExecutionMap.executionId}' data-uploadOrExecution='execution' id="executionStatusInfo" style="text-transform: capitalize;"><spring:message code="anvizent.package.label.info"/></a>  |
														    	    <a href="#" onClick="window.open('<c:url value="/adt/package/executionInfo/${packageExecutionMap.executionId}/execution"/>', 'newwindow', 'width=1700,height=700')" data-executionId='${packageExecutionMap.executionId}' data-uploadOrExecution='execution' id="executionStatus" style="text-transform: capitalize;"><spring:message code="anvizent.package.label.detailedInfo"/></a>
														    	    <br>
														    	     <spring:message code="anvizent.package.label.startDate"/> : ${packageExecutionMap.executionStartDate}
														    	      <br>
														    	        <spring:message code="anvizent.package.label.endDate"/> : ${packageExecutionMap.lastExecutedDate}
														    	 </td>
															  </c:when>
															  <c:otherwise>
															  <td> ${packageExecutionMap.executionStatus}</td>
															  </c:otherwise>
															 </c:choose>
															 
															  <c:choose>
															  <c:when test="${empty packageExecutionMap.druidStatus }">
														    	<td>-</td>
															  </c:when>
															  <c:when test="${packageExecutionMap.druidStatus != 'IGNORED'}">
														    	<td>${packageExecutionMap.druidStatus}<br> 
														    	    <a href="#" onClick="window.open('<c:url value="/adt/package/executionInfo/${packageExecutionMap.executionId}/druidStatus"/>', 'Comments', 'width=1700,height=700')" data-executionId='${packageExecutionMap.executionId}' data-uploadOrExecution='druidStatus' id="executionStatus" style="text-transform: capitalize;"><spring:message code="anvizent.package.label.detailedInfo"/></a>
														    	    <br>
														    	     <spring:message code="anvizent.package.label.startDate"/> : ${packageExecutionMap.druidStartDate}
														    	      <br>
														    	        <spring:message code="anvizent.package.label.endDate"/> : ${packageExecutionMap.druidEndDate}
														    	    </td>
														    	    
															  </c:when>
															  
															  <c:otherwise>
															  <td>${packageExecutionMap.druidStatus}</td>
															  </c:otherwise>
															 </c:choose>
														</tr>
													</c:forEach>
					         </c:when>
                              <c:otherwise>
                              <tr><td rowspan="8"><spring:message code="anvizent.package.label.noDataFound"/></td>
                             </c:otherwise>
                            </c:choose>
							</tbody>
						</table>
					</div>
			    </div>
			    <div>
			    <c:url value="/adt/standardpackage" var="standardpackage"></c:url>
			    <c:url value="/adt/package/schedule" var="schedule"></c:url>
			    <a href="${packageId == 0 ? standardpackage : schedule}" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.link.back"/></a>
			    </div>
     	</div>
		</div>
	</div>
	<div class="modal fade" tabindex="-1" role="dialog" id="viewUploaStatusInfoPopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog" style="width:85%"> 
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewUploaStatusInfoHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
					        	<div class="table-responsive" >
									<table class="table table-striped table-bordered tablebg" id="viewUploaStatusInfoResultsTable">
										<thead>
											<tr>
												<th><spring:message code="anvizent.package.label.sNo"/></th>
												<th><spring:message code="anvizent.package.label.executionID"/></th>
												<th><spring:message code="anvizent.package.label.uploadStatus"/></th>
												<th><spring:message code="anvizent.package.label.uploadComments"/></th>
												<th><spring:message code="anvizent.package.label.executeStatus"/></th>
												<th><spring:message code="anvizent.package.label.executionComments"/></th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div> 
					  </div> 
				</div>
				<div class="modal fade" tabindex="-1" role="dialog" id="viewExecutionStatusInfoPopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog" style="width:85%"> 
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewExecutionStatusInfoHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
					        	<div class="table-responsive" >
									<table class="table table-striped table-bordered tablebg" id="viewExecutionStatusInfoTable">
										<thead>
											<tr>
												<th><spring:message code="anvizent.package.label.sNo"/></th>
												<th><spring:message code="anvizent.package.label.executionID"/></th>
												<th><spring:message code="anvizent.package.label.targetTableName"/></th>
												<th><spring:message code="anvizent.package.label.executeStatus"/></th>
												<th><spring:message code="anvizent.package.label.executionComments"/></th>
												<th><spring:message code="anvizent.package.label.executionStartDate"/></th>
												<th><spring:message code="anvizent.package.label.lastExecutionDate"/></th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div> 
					  </div> 
				</div>
</div>
		
		
