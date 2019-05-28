<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText">Business Use Case</h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
			<input type="hidden" id="userID" value="${principal.userId}">
	</div>
	<div class="col-sm-10">
		<div id="successOrErrorMessage"></div>
	</div>
	
	<div id="businessModelInfoTable">
		<div class='row form-group'>
			<button style="float: right; margin-right: 1.5em;"
				class="btn btn-sm btn-success addBussinessproblem">Add</button>
		</div>
		<div class="row form-group tblIntern">
			<div class="table-responsive">
				<table class="table table-striped table-bordered tablebg"
					id="businessModelTbl">
					<thead>
						<tr>
							<th><spring:message code="anvizent.package.label.bid" /></th>
							<th><spring:message code="anvizent.package.label.businessProblem" /></th>
							<th><spring:message code="anvizent.package.label.aiModelName" /></th>
							<th><spring:message code="anvizent.package.label.stagingTableName" /></th>
							<th><spring:message code="anvizent.package.label.aILTableName" /></th>
							<th><spring:message code="anvizent.package.label.aOLTableName" /></th>
							<th><spring:message code="anvizent.package.label.isActive" /></th>
							<th><spring:message code="anvizent.package.label.scheduleTime" /></th>
							<!-- <th>RunNow</th> -->
							<th><spring:message code="anvizent.package.label.executionstatus" /></th>
							<th><spring:message code="anvizent.package.link.viewResults" /></th>
							<th><spring:message code="anvizent.package.label.edit" /></th>
							<th><spring:message code="anvizent.admin.button.Delete" /></th>
						</tr>
					</thead>
					<tbody>
									<c:forEach items="${businessModel}" var="businessModel" varStatus="index">
										<tr>
											<td><c:out value="${businessModel.bmid}" /></td>
											<td><c:out value="${businessModel.businessProblem}" /></td>
											<td><c:out value="${businessModel.modelName}" /></td>
											<td><c:out value="${businessModel.aIStagingTable}" /></td>
											<td><c:out value="${businessModel.aIILTable}" /></td>
											<td><c:out value="${businessModel.aIOLTable}" /></td>
											<td><c:out value="${businessModel.isActive}" /></td>
											<td><button id="bmid"  value="<c:out value="${businessModel.bmid}" />" class="btn btn-primary btn-sm scheduleAi"><spring:message code="anvizent.package.label.schedule"/></button></td>
											<%-- <td><button id="bmid"  value="<c:out value="${businessModel.bmid}" />" class="btn btn-primary btn-sm runnow"><spring:message code="anvizent.package.link.runNow"/></button></td> --%>
											<td><button id="bmid" data-status="${businessModel.bmid}" value="<c:out value="${businessModel.bmid}" />" class="btn btn-primary btn-sm aiViewstatus"><spring:message code="anvizent.package.link.viewStatus"/></button></td>
											
											<td>
											<a class="btn btn-primary" href ="<c:url value="/adt/package/ai/getrJobExecutionInfo/${businessModel.businessProblem}/${businessModel.modelName}"/>">Execution Results</a>
											</td>
											
											<%-- <td><button data-businessProb="${businessModel.businessProblem}" value="<c:out value="${businessModel.bmid}" />" class="btn btn-primary btn-sm aiRJobExecution">R Job Execution</button></td> --%>
											<td>
												<button class="btn btn-primary btn-sm editDetails" id="bid" value="<c:out value="${businessModel.bmid}" />" title="<spring:message code="anvizent.package.label.edit"/>" >
													<i class="fa fa-pencil" aria-hidden="true"></i>
												</button>
											</td>
											<td><button class="btn btn-primary btn-sm deleteBusinessModel" id="bid" value="<c:out value="${businessModel.bmid}" />" title="<spring:message code="anvizent.package.label.delete"/>" >
													<i class="fa fa-trash" aria-hidden="true"></i>
												</button></td>								
										</tr>
									</c:forEach>
								</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div class="col-sm-10 addBusinessDiv" style="display:none">
			<div class="panel panel-default">
				<div class="panel-heading"> Add Business Model</div>
					<div class="panel-body">
					<input type="hidden" name="id" id="idValue">
							<div class='row form-group'>
									<div class='control-label col-sm-2'>
										<spring:message code="anvizent.package.label.businessProblem" />
									</div>
									
									<div class='col-sm-6'>
									    <input type="hidden" id="bmid">
										<input type="text" id="businessName" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
						   </div>
						   
						   
						   
						    <div class="row form-group aiModalDiv">
								<div class='control-label col-sm-2'><spring:message code="anvizent.package.label.aiModelName" /> : </div>
								<div class="col-sm-6">
										<select class="form-control" id="aiModel"> 
										    <%-- <c:forEach items="${aiModalInfoList}" var="aiModal">
												<option value="${aiModal.id}" data-modalName="${aiModal.aIModelName}"><c:out value="${aiModal.aIModelName}"/></option>
											</c:forEach> --%>
									    </select>
								</div>
								<div class="col-sm-2">
												<button type="button" class="btn btn-primary btn-sm addAiModel">
													<span class="glyphicon glyphicon-plus"></span>
												</button>
												
											</div>
							</div>
						   
								
							<div class='row form-group'>
									<div class='control-label col-sm-2'>
										<!-- Staging Table Name -->
										<spring:message code="anvizent.package.label.stagingTableName" /> 
										
									</div>
									
									<div class='col-sm-6'>
										<input type="text" id="aiStagingTable" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
						   </div>
								
							
							<div class='row form-group'>
									<div class='control-label col-sm-2'>
										<!-- Staging Table Script -->
										<spring:message code="anvizent.package.label.stagingTableScript" /> 
										
									 </div>
									
									<div class='col-sm-6'>
									<textarea   rows="3"   id="aiStagingTableScript" class="form-control" data-minlength="1" data-maxlength="45" ></textarea>
									</div>
						   </div>
								
							<div class='row form-group'>
									<div class='control-label col-sm-2'>
										<!-- AIL Table Name -->
										<spring:message code="anvizent.package.label.aILTableName" />
									 </div>
									
									<div class='col-sm-6'>
										<input type="text" id="aiIlTable" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
						   </div>
						   
						   <div class='row form-group'>
									<div class='control-label col-sm-2'>
										<!-- AIL Table Script -->
										<spring:message code="anvizent.package.label.aILTableScript" />
										
										 </div>
									
									<div class='col-sm-6'>
									<textarea  rows="3" id="aiIlTableScript" class="form-control" data-minlength="1" data-maxlength="45" ></textarea>
									</div>
						   </div>
						   
						   <div class='row form-group'>
									<div class='control-label col-sm-2'>
										<!-- AOL Table Name -->
										<spring:message code="anvizent.package.label.aOLTableName" />
										
										 </div>
									
									<div class='col-sm-6'>
										<input type="text" id="aiOlTable" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
						   </div>
						   <div class='row form-group'>
									<div class='control-label col-sm-2'>
										<!-- AOL Table Script -->
										<spring:message code="anvizent.package.label.aOLTableScript" /> 
									 </div>
									
									<div class='col-sm-6'>
									<textarea rows="3" class="form-control" id="aiOlTableScript" data-minlength="1" data-maxlength="45" ></textarea> 
									</div>
						   </div>
						   <div class='row form-group'>
									<div class='control-label col-sm-2'>
										<!-- IsActive -->
										<spring:message code="anvizent.package.label.active"/>
									</div>
									<div class='col-sm-6'>
										<div class='col-sm-1'>
										<input type="radio" name="isActive" id="isActiveYes" value="Yes">Yes
									</div>
									<div class='col-sm-2'>
										<input type="radio" name="isActive" id="isActiveNo" value="No"> No
									</div>
									</div>
						   </div>
						   <div>
								<div class='col-sm-1'>
										<input type="button" id="saveBusinessProblem" value="save"  class="btn btn-primary btn-sm">
								</div>
								<div class='col-sm-9'>
									<button id="back" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Back"/></button>
								</div>
							</div>
		
								
						</div>
				</div>
		</div>
						
						
					
			
				
	<div class="modal fade" role="dialog" id="schedulePackagePopUp" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" style="width: 60%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title" id="schedulePackagePopUpHeader" style="word-break: break-all;white-space: normal;"></h4>
				</div>
				<div class="modal-body">
				
				<div id="scheduleOptionsDiv"> 
					<div class="panel panel-info" id='RecurrencePattern'
						style="margin-bottom: 5px;">
						<div class="panel-heading"><spring:message code = "anvizent.package.label.recurrencePattern"/></div>
						<div class="panel-body">
							<div id='recurrencePatternValidation'>
							<div class="row">
								<div class="col-md-3">
									 <div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="once" value="once" checked="checked" > <spring:message code = "anvizent.package.label.once"/>
										</label>
									</div> 
									<%-- <div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="hourly" value="hourly"> <spring:message code = "anvizent.package.label.hourly"/>
										</label>
									</div>
									<div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="daily" value="daily"> <spring:message code = "anvizent.package.label.daily"/>
										</label>
									</div>
									
									<div class="radio">
										<label> <input type="radio"  name="recurrencePattern"
											id="weekly" value="weekly"><spring:message code = "anvizent.package.label.weekly"/>
										</label>
									</div>
									<div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="monthly" value="monthly"> <spring:message code = "anvizent.package.label.monthly"/>
										</label>
									</div>
									<div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="yearly" value="yearly"> <spring:message code = "anvizent.package.label.yearly"/>
										</label>
									</div> --%>
									
								</div>
								<div class="col-md-9" id="showRecurrenceOptions">
								
									<div id="hourlyRecurrencePattern" style="display: none;">
										<div class='row form-group'>
										    <div class="col-sm-10">
										    	<input type="radio" name="hourlyRadios" id="everyhourlyRadios" value="every"/>
												<spring:message code = "anvizent.package.label.every"/> <input type="text" id="everyhour" style="width: 50px;" id="hourlyToRun" value=""/>
												<spring:message code = "anvizent.package.label.hours"/>
										    </div>
										</div>
										
										<div class='row form-group'>
										    <div class="col-sm-3">
										    	<input type="radio" name="hourlyRadios" id="selectedhourlyRadios" value="selected"/>
											<spring:message code = "anvizent.package.label.selecthours"/>
										    </div>
										    <div class="col-sm-8"> 
											<select id="selectedhours"  multiple="multiple" style="white-space:pre-wrap;width: 300px;">
											<c:forEach var="tm" begin="0" end="23">
													<option  value="${tm}">${tm}</option>
											</c:forEach>
											</select>
											</div>
										</div>

										<div id='hourlyRecurrencePatternVaLidation'></div>
										
									</div>
									
									<div id="dailyRecurrencePattern">
									</div>
									<div id="weeklyRecurrencePattern" style="display: none;">
									<div id='weeklyRecurrencePatternValidation'>
										<div class='row form-group'>
											<spring:message code = "anvizent.package.label.recurEvery"/> <input type="text" style="width: 50px;" id="weeksToRun" value="1"/>
											<spring:message code = "anvizent.package.label.weeksOn"/>
										</div>
										<div class='row form-group'>
											<input type="hidden" id="daysToRun"/>
											<c:forEach items="${weekdays}" var="weekday">
												<label class="checkbox-inline"> <input type="checkbox"  name="weekDayCheckBox" value=""> ${weekday}
												</label>
											</c:forEach>
										</div>
										</div>
									</div>
									<div id="monthlyRecurrencePattern" style="display: none;">
										<div class="radio">
											<label> <input type="radio" name="monthlyRadios"
												id="" value="monthlyOption_first" checked>
												<spring:message code = "anvizent.package.label.day"/> 
												<select class="" id="dayOfMonth">
														<c:forEach items="${daysOfMonth}" var="entry">
															<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
														</c:forEach>
												</select>
												<spring:message code = "anvizent.package.label.ofEvery"/> 
												<select id="monthsToRun">
													<option value="1">1</option>
													<option value="2">2</option>
													<option value="3">3</option>
													<option value="4">4</option>
													<option value="5">5</option>
													<option value="6">6</option>
												</select> 
												<spring:message code = "anvizent.package.label.months"/>
											</label>
											<p class="help-block"><em><spring:message code = "anvizent.package.label.noteIfSelectedDayIsNotAvailableForAMonthLastDayOfThatMonthWillBeConsidered"/></em></p>
										</div>
									</div>
									<div id="yearlyRecurrencePattern" style="display: none;">
										<div class='row form-group hide'>
											<spring:message code = "anvizent.package.label.recurEvery"/> <input type="text" style="width: 50px;" id="yearsToRun" value="1"/>
											<spring:message code = "anvizent.package.label.years"/> 
										</div>
										<div class='row form-group'>
											<div class="radio">
												<label> <input type="radio" name="yearlyRadios"
													id="" value="yearlyOptions_first" checked>
													<spring:message code = "anvizent.package.label.on"/>    <select class="" id="monthOfYear">
																<c:forEach items="${monthsOfYear}" var="monthOfYear">
																	<option value="<c:out value="${monthOfYear.key}"/>"><c:out value="${monthOfYear.value}"/></option>
																</c:forEach>
															</select>                
													<select class="" id="dayOfYear">
														<c:forEach items="${daysOfMonth}" var="entry">
															<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
														</c:forEach>
												</select>
												</label>
												<p class="help-block"><em><spring:message code = "anvizent.package.label.noteIfSelectedDayIsNotAvailableForAMonthLastDayOfThatMonthWillBeConsidered"/></em></p>
											</div>
										</div>
									</div>
								</div>
							</div>
							</div>
						</div>
					</div>
					<div class="panel panel-info" id='scheduleTime'>
						<div class="panel-heading"><spring:message code = "anvizent.package.label.scheduleTime"/></div>
						<div class="panel-body">
							<div class='row' style="margin-bottom: 5px;">
							    <div id='scheduleStartDateValidation'>
								<div class='col-sm-3'><spring:message code = "anvizent.package.label.startDate"/>:</div>
								<div class='col-sm-3'>
									<input type="text" class="datepicker" style="width: 100px;" id="scheduleStartDate"/>
								</div>
								</div>
							</div>
							<div class='row' style="margin-bottom: 5px;">
								<div class='col-sm-3'><spring:message code = "anvizent.package.label.startTime"/></div>
								<div class='col-sm-4'>
									<select class="" id="scheduleStartHours">
										<c:forEach items="${hours}" var="hour">
											<option value="<c:out value="${hour}"/>"><c:out value="${hour}"/></option>
										</c:forEach>
									</select>
									<select class="" id="scheduleStartMinutes">
										<c:forEach items="${minutes}" var="minute">
											<option value="<c:out value="${minute}"/>"><c:out value="${minute}"/></option>
										</c:forEach>
									</select>
								</div>
							</div>

						</div>
					</div>
					<div class="panel panel-info" id='RangeofRecurrence'	style="margin-top: -15px;">
						<div class="panel-heading"><spring:message code = "anvizent.package.label.rangeOfRecurrence"/></div>
						<div class="panel-body">
						<div id='rangeofRecurrenceValidation'>
							<div class="row">
								<div class="col-md-8" id="rangeOfRecurrence_options">
									<div>
										<div class="radio">
											<label> <input type="radio" value="NoEndDate"
												name="rangeOfRecurrence" id="isNoEndDate"> <spring:message code = "anvizent.package.label.noEndDate"/>
											</label>
										</div>
										<div class="radio hide">
											<label> <input type="radio" value="MaxOccurences"
												name="rangeOfRecurrence"> <spring:message code = "anvizent.package.label.endAfter"/> <input
												type="text" style="width: 50px;" id="noOfMaxOccurences"/> <spring:message code = "anvizent.package.label.occurrences"/>
											</label>
										</div>
										<div class="radio">
											<label> 
											<input type="radio" value="ScheduleEndDate"
												name="rangeOfRecurrence" class='endByDate'> <spring:message code = "anvizent.package.label.endBy"/> <input type="text"  class="datepicker" style="width: 100px;" id="scheduleEndDate" />
											</label>
											<div id='scheduleEndDateVaLidation'></div>
										</div>
									</div>
								</div>
							</div>
							</div> 
						</div>
					</div>
					<div class="panel panel-info" id='timeZoneDivPanel'	style="margin-top: -15px;">
						<div class="panel-heading"><spring:message code = "anvizent.package.label.timeZone"/></div>
						<div class="panel-body">
							<div class='row form-group' style="margin-bottom: 5px;">
								<div class='col-sm-2'><spring:message code = "anvizent.package.label.timeZone"/></div>
								<div class='col-sm-6'>
									<select class="form-control" id="timeZone">
									<option value="select"><spring:message code = "anvizent.package.label.selectOption"/></option>
										<c:forEach items="${timeZonesList}" var="timeZone">
											<option value="${timeZone.key}">${timeZone.value}</option>
										</c:forEach>
									</select>
								</div>
							</div>
					 
						</div>
					</div>
				</div>	
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary confirmSchedule" id="bpid" ><spring:message code="anvizent.package.button.ok"/></button>
					<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="aiStausViewPopUp" data-backdrop="static" data-keyboard="false">
				  <div class="modal-dialog" style="width:85%"> 
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
	        			<h4 class="modal-title custom-modal-title" id="viewAITableResultsHeader"></h4>
				      </div>
				      <input type="hidden" id="statusById"> 
				      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
				        	<div class="table-responsive" >
								<table class="table table-striped table-bordered tablebg" id="viewAIStatusTableResultsTable">
									<thead>
										<tr>
											<th><spring:message code="anvizent.package.label.sNo"/></th>
											<th><spring:message code="anvizent.package.label.executionID"/></th>
											<th><spring:message code="anvizent.package.label.id"/></th>
											<th><spring:message code="anvizent.package.label.runtype"/></th>
											<th><spring:message code="anvizent.package.label.executionStatus"/></th>
											<th><spring:message code="anvizent.package.label.executionComments"/></th>
											<th><spring:message code="anvizent.package.label.startDate"/></th>
											<th><spring:message code="anvizent.package.label.endDate"/></th>
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
		
		
		<div class="modal fade" role="dialog" id="addAiModelPopUp" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" style="width: 60%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title" id="schedulePackagePopUpHeader" style="word-break: break-all;white-space: normal;"></h4>
				</div>
				<div class="modal-body">
				
				<div class="panel panel-default">
				<div class="panel-heading"> Add AI Model</div>
					<div class="panel-body">
					<input type="hidden" name="id" id="idValue">
							<div class='row form-group'>
									<div class='control-label col-sm-2'>
										<spring:message code="anvizent.package.label.aiModelName"/>
									</div>
									<div class='col-sm-6'>
									    <input type="hidden" id="aid">
										<input type="text" id="aiModelName" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
						   </div>
								
							<div class='row form-group'>
									<div class='control-label col-sm-2'>
										<spring:message code="anvizent.package.label.aiModelDescription"/>
									</div>
									<div class='col-sm-6'>
										<input type="text" id="aiModelType" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
						   </div>
						   
						   
						   
						   <div class="row form-group aiModalDiv">
								<div class='control-label col-sm-2'><spring:message code="anvizent.package.label.existedAIModal"/> </div>
								<div class="col-sm-6">
										<select class="form-control" id="aiModalId" multiple="multiple"> 
										    <c:forEach items="${aiModalInfoList}" var="aiModal">
												<option value="${aiModal.id}"><c:out value="${aiModal.aIModelName}"/></option>
											</c:forEach>
									    </select>
								</div>
								
							</div>
						    <div id="aIContextParametersDiv" style="margin-top: 5px;"></div>
						        <div class="row-form-group contextKeyValue" id="aicontextParameters" style="display: none">
										<div class='control-label col-sm-2'>
										<spring:message code="anvizent.package.label.modalParameters"/>
									</div>
										<div class="row form-group">
											<div class="col-sm-4">
												<spring:message code = "anvizent.package.label.enterKey" var="enterKey"/>
												<input class="form-control authBodyKey">
											</div>
											<div class="col-sm-4">
												<spring:message code = "anvizent.package.label.enterValue" var ="enterValue"/>
												<input class="form-control authBodyValue">
											</div>
											<div class="col-sm-2">
												<button type="button" class="btn btn-primary btn-sm addContextParams">
													<span class="glyphicon glyphicon-plus"></span>
												</button>
												<button type="button" class="btn btn-primary btn-sm deleteContextParams">
													<span class="glyphicon glyphicon-trash"></span>
												</button>
											</div>
										</div>
						        </div>	

						
						   <div>
								<div class='col-sm-1'>
										<input type="button" id="saveAIModel" value="save"  class="btn btn-primary btn-sm">
								</div>
								<div class='col-sm-9'>
									<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
								</div>
							</div>
		
								
						</div>
						
						
					</div>
				
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	</div>
		  <div class="modal fade" tabindex="-1" role="dialog" id="deleteAIBP" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title">
							Delete Business Use Case
						</h4>
					</div>
					<div class="modal-body">
						<p>
							Are you sure , do you want to delete the Business Use Case ?
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="confirmDeleteAIBP">
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
