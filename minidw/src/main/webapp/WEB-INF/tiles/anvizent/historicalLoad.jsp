<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<spring:message code="anvizent.package.label.executed" var="executedVar"/>
<spring:message code="anvizent.package.label.running" var="runningVar"/>
<spring:message code="anvizent.package.label.pending" var="pendingVar"/>


<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText">
			<spring:message code="anvizent.admin.button.historicalload" />
		</h4>
	</div>

	<div class="col-md-10">
		<div class="dummydiv"></div>
		<jsp:include page="_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	</div>

	<div class="col-md-12">
		
		 <c:url value="/adt/package/historicalLoad/save" var="saveUrl" />
         
         <c:url value="/adt/package/historicalLoad/edit" var="editUrl"/>
         <input type="hidden" value="<c:out value="${editUrl }"/>" id="editUrl">
		
		<c:url value="/adt/package/historicalLoad/run" var="runUrl"/>
         <input type="hidden" value="<c:out value="${runUrl }"/>" id="runUrl"> 
		
		<c:url value="/adt/package/historicalLoad/runMultiple" var="runMultipleUrl"/>
         <input type="hidden"  value="<c:out value="${runMultipleUrl }"/>"  id="runMultipleUrl"> 
		
		<c:url value="/adt/package/historicalLoad/stop" var="stopUrl"/>
         <input type="hidden" value="<c:out value="${stopUrl }"/>"  id="stopUrl"> 
		
		<c:url value="/adt/package/historicalLoad/clone" var="cloneUrl"/>
         <input type="hidden" value="<c:out value="${cloneUrl }"/>"  id="cloneUrl"> 
		
		 <c:url value="/adt/package/historicalLoad/update" var="updateUrl" />
		
		<div class='row form-group history' id="historicalLoadDiv">
			<form:form modelAttribute="historicalLoadForm"  action="${editUrl}" >
			<form:hidden path="pageMode"/>
			<form:hidden path="ids"/>
				<c:choose>
					<c:when test="${historicalLoadForm.pageMode == 'list' }">
						<div  style="width:100%;padding:0px 15px;">
		                <div class="row form-group" style="padding:5px;border-radius:4px;">
                           <c:url value="/adt/package/historicalLoad/add" var="addUrl" />
							<button type="submit" style="float: right;" class="btn btn-sm btn-success" data-addurl="<c:out value="${addUrl }"/>" id="addHistoricalLoad">
								<spring:message code="anvizent.package.button.add" />
							</button>
							<button type="submit" style="float: right; margin-right: 1.5em;display: none;" class="btn btn-sm btn-success" id="runMultipleLoads">
								<spring:message code="anvizent.package.label.runJobs" />
							</button>
		               </div>
	                  </div>
						<div class='row form-group'>
			              <div class="table-responsive">
							<table class="table table-striped table-bordered tablebg  dataTable no-footer" id="historicalLoadTable">
								<thead>
									<tr>
										<th><input type="checkbox" id="selectAll"></th>
										<th><spring:message code="anvizent.package.label.id"/></th>
										<th><spring:message code="anvizent.package.label.ilName"/></th>
										<th><spring:message code="anvizent.package.label.fromdate"/></th>
										<th><spring:message code="anvizent.package.label.todate"/></th>
										<th><spring:message code="anvizent.package.label.interval"/></th>
										<th><spring:message code="anvizent.admin.label.multiPartEnabled"/></th> 
										<th><spring:message code="anvizent.admin.label.noOfRecordsPerFile"/></th>
										<th><spring:message code="anvizent.package.label.edit"/></th> 
										<th><spring:message code="anvizent.package.label.runstatus"/></th>
										<th><spring:message code="anvizent.package.label.query"/></th>  
										<th><spring:message code="anvizent.package.label.run"/></th>
										<%-- <th><spring:message code="anvizent.package.label.view"/></th> --%>
										<th><spring:message code="anvizent.package.label.viewResults"/></th> 
										<th><spring:message code="anvizent.package.label.clone"/></th> 
									</tr>
								</thead>
								<tbody id="historicalLoadTableBody">
									<c:forEach items="${historicalLoadList}" var="historicalLoad">
										<tr id="historicalLoadRow">
											<td> <input type="checkbox" class="historyChkBox" data-isexecuted="${historicalLoad.executed }"  data-historicalloadid="${historicalLoad.id}"   ${ (historicalLoad.executed || historicalLoad.running) ? "disabled='true'":""} > </td>
											<td><c:out value="${historicalLoad.id}"/></td>
											<td><c:out value="${historicalLoad.ilName}"/></td>
											<td id="historicalLoadFromDate"><c:out value="${historicalLoad.historicalFromDate}"/></td>
											<td id="historicalLoadToDate"><c:out value="${historicalLoad.historicalToDate}"/></td>
											<td id="historicalLoadInterval"><c:out value="${historicalLoad.loadInterval}"/> <spring:message code="anvizent.package.label.days"/></td>
											<td id="historicalLoadMultipartEnabled"><c:out value="${historicalLoad.multipartEnabled}"/></td>
											<td id="historicalLoadNoOfRecordPerFile"><c:out value="${historicalLoad.noOfRecordsPerFile}"/></td>
                                            <td>
                                            	<button class="btn btn-primary btn-sm tablebuttons startLoader editHistoricalLoad" type="submit"  name="id" value="<c:out value="${historicalLoad.id}"/>"  ${ (historicalLoad.executed || historicalLoad.running) ? "disabled='true'":""} >
                                            		<i class="fa fa-pencil" title="<spring:message code="anvizent.package.label.edit"/>" aria-hidden="true" ></i>
                                            	</button>
                                            </td> 
											<td> 
											
												${ historicalLoad.executed ? executedVar  : historicalLoad.running ? runningVar : pendingVar}
												<c:if test="${ historicalLoad.running }">
													<button type="submit"  name="id" value="<c:out value="${historicalLoad.id}"/>" class="btn btn-primary  btn-sm terminateJob">
														<spring:message code="anvizent.package.label.terminate"/>
													</button>
												</c:if>
											</td>
											<td><button type="button" class='btn btn-primary btn-sm tablebuttons viewHistoryLoadQuery text-underline' data-historicalloadid="${historicalLoad.id}"><span class="glyphicon glyphicon-eye-open" title="<spring:message code="anvizent.package.label.view"/>"></span></button>
                                            </td> 
											<td>
											<button type="submit"  name="id" value="<c:out value="${historicalLoad.id}"/>"
												class="btn btn-primary  btn-sm runHistoricallLoad" 
												data-historicalloadid="<c:out value="${historicalLoad.id}"/>"
												data-historicalfromdate="<c:out value="${historicalLoad.historicalFromDate}"/>"
												data-historicaltodate="<c:out value="${historicalLoad.historicalToDate}"/>"
												data-historicallastupdatedtime="<c:out value="${historicalLoad.historicalLastUpdatedTime}"/>"
												data-loadinterval="<c:out value="${historicalLoad.loadInterval}"/>"
												data-multipartenabled="<c:out value="${historicalLoad.multipartEnabled}"/>"
												data-noofrecordsperfile="<c:out value="${historicalLoad.noOfRecordsPerFile}"/>"
												data-isexecuted="<c:out value="${historicalLoad.executed }"/>"
												data-ilname="<c:out value="${historicalLoad.ilName}"/>" ${ (historicalLoad.executed || historicalLoad.running) ? "disabled='true'":""} >
												<spring:message code="anvizent.package.label.run"/>
												</button>
											</td>
											<%-- <td><input type="button" value="<spring:message code="anvizent.package.label.viewExecutionStatus"/>" 
												class="btn btn-primary  btn-sm viewUploadStatus" 
												data-historicalloadid="<c:out value="${historicalLoad.id}"/>">
											</td> --%>
											 <td>
											 <input type="button" value="<spring:message code="anvizent.package.label.executionStatus"/>" 
												class="btn btn-primary  btn-sm viewUploadStatus" 
												data-historicalloadid="<c:out value="${historicalLoad.id}"/>">
												<br />
											 <a class="btn btn-primary btn-sm startLoader" href="<c:url value="/adt/package/historicalLoad/jobResultsForHistoricalLoad/${historicalLoad.ilId}/${historicalLoad.id}/${historicalLoad.ilName}"/>" ><spring:message code="anvizent.package.label.viewResults"/></a></td>
											  <td>
                                            	<button class="btn btn-primary btn-sm startLoader cloneHistoricalLoad" type="submit"  name="id" value="<c:out value="${historicalLoad.id}"/>" >
                                            		<spring:message code="anvizent.package.label.clone"/>
                                            	</button>
                                            </td> 
										</tr>
									</c:forEach>
								</tbody>
							</table>
							</div>
						</div>

						<div class="modal fade" tabindex="-1" role="dialog"
							style='overflow-y: auto; max-height: 90%;'
							id="viewDeatilsPreviewPopUp" data-backdrop="static"
							data-keyboard="false" aria-hidden='true'>
							<div class="modal-dialog" style="width: 90%;">
								<div class="modal-content">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal" aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
										<h2 class="modal-title" id="viewDeatilsPreviewPopUpHeader"><spring:message code="anvizent.package.label.historyLoadStatus" /></h2>
									</div>
									<div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto; overflow-x: auto;">
									<input type="hidden" id="hid" >
										<table class='table table-striped table-bordered tablebg historicalLoadInfo' >
											<thead>
												<tr>
													<th><spring:message code="anvizent.package.label.ilName"/></th>
													<th><spring:message code="anvizent.package.label.fromdate"/></th>
													<th><spring:message code="anvizent.package.label.todate"/></th>
													<th><spring:message code="anvizent.package.label.lastUpdatedDate"/></th>													
													<th><spring:message code="anvizent.package.label.interval"/></th>  

													
												</tr>
											</thead>
											<tbody>
												<tr>
													<td class="runIlName"> - </td>
													<td class="runFromDate"> - </td>
													<td class="runToDate"> - </td>
													<td class="runLastUpdatedTime"> - </td>
													<td class="runInterval"> - </td>
												</tr>
											</tbody>
										</table>
										<hr />
										<table class='viewDeatilsPreview table table-striped table-bordered tablebg'>
											<thead>
												<tr>
													<th><spring:message code="anvizent.package.label.sNo"/></th>
													<th><spring:message code="anvizent.package.label.startDate"/></th>
													<th><spring:message code="anvizent.package.label.endDate"/></th>
													<th><spring:message code="anvizent.package.label.noOfRecords"/></th>
													<th><spring:message code="anvizent.package.label.fileSize"/></th>
												</tr>
											</thead>
											<tbody>
												
											</tbody>
										</table>
									</div>
									<div class="modal-footer">
										<button type="button"
											class="btn btn-default initiateHistoricalDataLoading">
											<spring:message code="anvizent.package.label.start" />
										</button>
										<button type="button"
											class="btn btn-default"
											data-dismiss="modal">
											<spring:message code="anvizent.package.button.close" />
										</button>
									</div>
								</div>
							</div>
						</div>
						

					</c:when>

					<c:when test="${historicalLoadForm.pageMode == 'edit' || historicalLoadForm.pageMode == 'add' }">
						<div class='row form-group'>
						   <form:hidden  path="id"></form:hidden>
							<div class='col-sm-2'><spring:message code="anvizent.package.label.ils" /> : </div>  
							<div class='col-sm-9'>
								<form:select path="ilId" class="form-control">
									<form:option value="0"><spring:message code="anvizent.package.label.select" /></form:option>
									<c:if test="${not empty fetchAllIlInfo }">
										<form:options items="${fetchAllIlInfo }" />
									</c:if>
								</form:select>
							</div>
						</div>
						
						<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.dataSource"/>:
									</div>
									<div class='col-sm-9'>
										<form:select cssClass="form-control" path="dataSourceName">
										     <form:option value="0"><spring:message code="anvizent.package.label.selectDataSource"/></form:option>
											  <form:options items="${allDataSourceList}"/>
											  <form:option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></form:option>
										</form:select>   
									</div>
						</div>
							
						<div class='row form-group dataSourceOther' style="display:none">
								<div class='col-sm-2'>
								</div>
								<div class='col-sm-9'>
									<form:input path="dataSourceNameOther" cssClass="form-control" data-minlength="1" data-maxlength="45"/>
								</div>
						</div>
						
						<div class='row form-group'>
							<div class='col-sm-2'><spring:message code="anvizent.package.label.connections" /> :</div>
							<div class='col-sm-9'>
								<form:select path="connectorId" class="form-control">
									<form:option value="0"><spring:message code="anvizent.package.label.select" /></form:option>
									 <c:if test="${not empty ilConnections }">
										<form:options items="${ilConnections }" />
									</c:if>
								</form:select>
							</div>
						</div>
						<div class='row form-group'>
							<div class='col-sm-2'>
								<spring:message code="anvizent.package.label.fromdate" /> :
							</div>
							<div class='col-sm-9'>
								<spring:message code="anvizent.package.label.fromdate" var="fromDatePlaceholder" />
								<form:input path="historicalFromDate" placeholder="${fromDatePlaceholder}" class="form-control" />
							</div>
						</div>
						<div class='row form-group'>
							<div class='col-sm-2'>
								<spring:message code="anvizent.package.label.todate" /> :
							</div>
							<div class='col-sm-9'>
								<spring:message code="anvizent.package.label.todate" var="toDatePlaceholder" />  
								<form:input path="historicalToDate" placeholder="${toDatePlaceholder}" class="form-control" />
							</div>
						</div>
						<div class='row form-group'>
							<div class='col-sm-2'>
								<spring:message code="anvizent.package.label.loadinterval" /> :
							</div>
							<div class='col-sm-9'>
								<form:select path="loadInterval" class="form-control">
									<form:option value="0"><spring:message code="anvizent.package.label.select" /></form:option>
									<c:if test="${not empty loadInterVal }">
										<form:options items="${loadInterVal }" />
									</c:if>
								</form:select>
								<spring:message code="anvizent.package.label.days" />
							</div>
						</div>
						<div class='row form-group'>
							<div class='col-sm-2'>
								<spring:message code="anvizent.admin.label.multiPartEnabled" />
							</div>
							<div class='col-sm-9'>
							<form:checkbox path="multipartEnabled"/> 
							</div>
						</div>
						<div class='row form-group' id='noOfRecordsPerFileDiv' style="display: none">
							<div class='col-sm-2'>
								<spring:message code="anvizent.admin.label.noOfRecordsPerFile" /> :
							</div>
							<div class='col-sm-9'>
								<form:select path="noOfRecordsPerFile" class="form-control">
									<form:option value="0"><spring:message code="anvizent.package.label.select" /></form:option>
	                                <form:option value="25000">25000</form:option>
	                                <form:option value="50000">50000</form:option>
	                                <form:option value="75000">75000</form:option>
	                                <form:option value="100000">100000</form:option>     
								</form:select>
							</div>
						</div>
						<div id="defualtVariableDbSchema" style="display:none"></div>
			  			<div class='row form-group IL_queryCommand'>
							<div class='col-sm-2'>
							</div>
							<div class='col-sm-8' id="replaceDbSchema">
								<input type="button" value='<spring:message code="anvizent.package.label.replaceSchemas"/>' id='replaceShemas' style="display:none" class="btn btn-primary btn-sm"/>
						   </div>
		        		</div>
					        		
		        		<div class='row form-group' id='replace' style="display: none">
								<div class='col-sm-2 control-label'>
									<spring:message code="anvizent.package.label.find" /> :
								</div>
								<div class='col-sm-3 control-label'>
									<input type="text" class="form-control" id="replace_variable">
								</div>
								<div class='col-sm-2 control-label'>
									<spring:message code="anvizent.package.label.replaceWith" /> :
								</div>
								<div class='col-sm-3 control-label'>
									<input type="text" class="form-control" id="replace_with">
								</div>
						</div>
						
						<div class='row form-group' id='replaceAll' style="display: none">
								<div class="col-xs-12" style="padding-right: 160px;">	
				                     <div class="" id="replace_All" style="float: right;">
				                           <input type="button" value="<spring:message code="anvizent.package.label.replaceAll" />" id="replace_all" class="btn btn-primary btn-sm">
				                     </div>
		                       		<div class="" id="buttonUndo" style="float: right; margin: 0px 10px;">
				                    		<input type="button" value="<spring:message code="anvizent.package.label.undo"/>" id="undo" class="btn btn-primary btn-sm">
 						 			</div>
 								</div>
		        		</div>
						
						<div class='row form-group'>
							<div class='col-sm-2'><spring:message code="anvizent.package.label.historicalquery" /> :</div>
							<div class='col-sm-9'>
								<spring:message code="anvizent.package.label.queryScript" var="queryScriptPlaceholder" />
								<form:textarea path="histiricalQueryScript" placeholder="${queryScriptPlaceholder}" rows="6" class="form-control" />
								<input type="hidden" id="oldQueryScript">
								<input type="hidden" id="connector_Id">
								<input type="hidden" id="protocal">
							</div>
						</div>
                        <div class='row form-group'>
						<div class='col-sm-offset-1 col-sm-8 queryValidatemessageDiv'></div>
						</div>
						<div class='row form-group'>
							<div class='col-sm-2'></div>
							<div class='col-sm-9'>
							<input type="button" value="<spring:message code = "anvizent.package.button.validate"/>" id="validateHistoricalQuery"  class="btn btn-primary  btn-sm">
								<c:if test="${historicalLoadForm.pageMode == 'add' }">
								<input type="submit" value="<spring:message code = "anvizent.package.button.save"/>" id="saveHistoricalLoad" style="display:none"   data-saveurl="${saveUrl }" class="btn btn-primary  btn-sm">
								</c:if>
								<c:if test="${historicalLoadForm.pageMode == 'edit'}">
							    <input type="submit" value="<spring:message code = "anvizent.package.button.update"/>" id="updateHistoricalLoad" style="display:none" data-updateurl="${updateUrl }"  class="btn btn-primary  btn-sm">
							    </c:if>
								<a type="reset" href="<c:url value="/adt/package/historicalLoad"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back" /></a>
							</div>
						</div>
						
					</c:when>
				</c:choose>
			</form:form>
		</div>
		
  <div class="modal fade" tabindex="-1" role="dialog" style='overflow-y: auto; max-height: 90%;' id="viewHistoricalUploadStatusPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
	<div class="modal-dialog" style="width: 90%;">
		   <div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"> <span aria-hidden="true">&times;</span> </button>
				<h4 class="modal-title" id="viewHistoricalUploadStatusPopUpHeader"> <spring:message code="anvizent.package.label.historyLoadStatus" /></h4>
			</div>
			<div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto; overflow-x: auto;">
				<table class='table table-striped table-bordered tablebg historicalUploadStatus' id="historicalUploadStatusTable">
					<thead>
						<tr>
							 <th>S No</th>
							 <th><spring:message code="anvizent.package.label.startDate"/></th>
							 <th><spring:message code="anvizent.package.label.endDate"/></th>
							 <th><spring:message code="anvizent.package.label.loadinterval" /></th>
							 <%-- <th><spring:message code="anvizent.package.label.lastUpdatedDate"/></th> --%>
							 <th><spring:message code="anvizent.package.label.fileSize"/></th>
							 <th><spring:message code="anvizent.package.label.sourceRowsCount"/></th>
							 <th><spring:message code="anvizent.package.label.iLExecutionStatus"/></th>
							 <th><spring:message code="anvizent.package.label.comment"/></th>
							 <th><spring:message code="anvizent.package.label.createdDate"/></th>
							 <th><spring:message code="anvizent.package.label.modifiedDate"/></th>
						 </tr>
					</thead>
					<tbody id="historicalUploadStatusTbody">
					</tbody>
				</table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"> <spring:message code="anvizent.package.button.close" /> </button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" tabindex="-1" role="dialog" id="viewQueryPopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 60%;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title heading"><spring:message code="anvizent.package.label.viewHistoryLoadQuery"/></h4>
		      </div>
		      <div class="modal-body" style="max-height: 400px; overflow-y: auto;">
					<div  style='overflow-y: auto;max-height: 300px;'>
						<textarea class='view-Query' rows="10" cols="10" style="width: 100%;" readonly="readonly">
							
						</textarea>
					</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>	
		
		<div class="modal fade" tabindex="-1" role="dialog" id="replaceAllAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code = "anvizent.package.label.replaceVariable"/></h4>
		      </div>
		      <div class="modal-body">
                <p> <spring:message code="anvizent.package.label.areyousureyouwanttoReplaceAll"/></p>
		      </div>
		      <div class="modal-footer">
		       <button type="button" class="btn btn-primary" id="confirmReplaceAll"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		
 </div>

</div>

