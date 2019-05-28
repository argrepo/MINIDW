<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
 <spring:htmlEscape defaultHtmlEscape="true" />
 
<div class="col-sm-12 rightdiv">
	<div class="page-title-v1">
		<h4> <spring:message code = "anvizent.package.label.schedule"/></h4>
	</div>
	<div class="dummydiv"></div>
	<ol class="breadcrumb">
	</ol>
	<div class="row form-group" id='packageDetails'>
		<h4 class="alignText"><spring:message code = "anvizent.package.label.schedule"/></h4>
	</div>
	<div class="col-sm-12">
		<div id="successOrErrorMessage"></div>
	</div>
  <jsp:include page="_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
		<input type="hidden" id="reloadUrl" value="<c:out value="${tableLevelAccessUrl}"/>">
		<input type="hidden" id="packageId" value="<c:out value="${param['packageId'] }"/>" />
		<div id='schedulingProcess'>
			<div class="row form-group" style="padding:5px;border-radius:4px;">	   		 
		  			<label class="control-label col-sm-4 col-md-3 col-lg-2 labelsgroup hidden"><spring:message code="anvizent.package.label.filterPackagesBy"/></label>
		  			<div class="col-sm-4 col-md-3 col-lg-2 hidden">
			   			<select id="filterSchedulePackages" class="form-control">
			   				<option value="all" selected><spring:message code="anvizent.package.label.all"/></option>
			   				<option value="standard"><spring:message code="anvizent.package.label.standard"/></option>
			   				<option value="custom"><spring:message code="anvizent.package.label.custom"/></option>	
			   			</select>
		   			</div>
		   			<c:if test="${isWebApp == false}">
		   			<div class="col-sm-4">
		   				
		   				<a  href="<c:url value="/adt/package/scheduleJobInfo"/>" class="btn btn-primary"><spring:message code="anvizent.package.label.clientScheduler"/></a>
		   			</div>
		   			</c:if>
		   			<button class="btn btn-sm btn-primary schedulePackages" style='display:none;margin-top: 4px;'><spring:message code="anvizent.package.label.schedulePackages"/></button>
			</div>
		
			<div class='row form-group'>
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg" id="packageScheduleTable">
						<thead>
							<tr>
								<!-- <th><input type="checkbox" name="selectAllPackages"></th> -->
								<th><spring:message code="anvizent.package.label.packageId"/></th>
								<th><spring:message code="anvizent.package.label.packageType" /></th>
								<th><spring:message	code="anvizent.package.label.packageName" /></th>
								<%-- <th><spring:message code="anvizent.schedule.label.scheduleCurrentStatus" /></th> --%>
								<th><spring:message	code="anvizent.package.link.viewSourceDetails"/></th>
								<!-- <th>Run Now</th> -->
								<th><spring:message	code="anvizent.package.link.runNow"/></th>
								<%-- <th><spring:message	code="anvizent.package.link.runwithScheduler"/></th> --%>
								<th><spring:message code="anvizent.schedule.label.scheduleTime" /></th>
								<!-- <th>View Results</th> -->
								<th><spring:message	code="anvizent.package.link.viewResults"/></th>
								<th><spring:message code="anvizent.package.label.viewExecutionResults"/></th>
							</tr>
						</thead>
						 <tbody>
						
							<c:forEach items="${packageListForScheduling}" var="packageForScheduling" varStatus="index">
							 <tr>
								 <%-- <c:choose>
									<c:when test="${packageForScheduling.isScheduled == false}">
							 			<td><input type="checkbox" name="selectPackage" data-packgeId='<c:out value="${packageForScheduling.packageId}"/>' data-packgeName='<c:out value="${packageForScheduling.packageName}"/>'
										 			data-isClientDbTables='<c:out value="${packageForScheduling.isClientDbTables}"/>' data-industryId='<c:out value="${packageForScheduling.industry.id}"/>' data-reSchedule='firstRun'></td>
							 		</c:when>
									<c:otherwise>
										<td><input type="checkbox" name="selectPackage" data-packgeId='<c:out value="${packageForScheduling.packageId}"/>' data-scheduleId = '<c:out value="${packageForScheduling.scheduleId}"/>'
										 			data-packgeName='<c:out value="${packageForScheduling.packageName}"/>' data-reSchedule='reRun' data-isClientDbTables='<c:out value="${packageForScheduling.isClientDbTables}"/>'
										  			data-industryId='<c:out value="${packageForScheduling.industry.id}"/>'></td>
									</c:otherwise>
								</c:choose>	 --%>
								<td><c:out value="${packageForScheduling.packageId}"/></td>
								<td>
									<c:if test="${packageForScheduling.isStandard}">
										<spring:message code="anvizent.package.label.standard"/>
									</c:if>
									<c:if test="${!packageForScheduling.isStandard}">
										<spring:message code="anvizent.package.label.custom"/>
									</c:if>
								</td>
								<td class="packageName" style="word-break: break-all;white-space: normal;"><c:out value="${packageForScheduling.packageName}"/></td>
								<%-- <td><c:out value="${packageForScheduling.scheduleStatus}"/></td> --%>
								<td class="smalltd">
									<c:if test="${packageForScheduling.isStandard}">
										<a class="btn btn-primary btn-sm tablebuttons startLoader" href="<c:url value="/adt/standardpackage/edit/${packageForScheduling.packageId}?source=schedule"/>" >
											<span class="glyphicon glyphicon-eye-open" title="<spring:message code="anvizent.package.label.view"/>"></span>
										</a>
									</c:if>
									<c:if test="${!packageForScheduling.isStandard}">
										<c:choose>
					                		<c:when test="${packageForScheduling.isScheduled}">
					                			<!-- View -->
					                			<a class="btn btn-primary btn-sm tablebuttons startLoader" href="<c:url value="/adt/package/customPackage/edit/${packageForScheduling.packageId}?source=schedule"/>" >
					                				<span class="glyphicon glyphicon-eye-open" title="<spring:message code="anvizent.package.label.view"/>"></span>
					                			</a>
					                		</c:when>
					                		<c:otherwise>
					                			<!--Edit  -->
					                			<a class="btn btn-primary btn-sm tablebuttons startLoader" href="<c:url value="/adt/package/customPackage/edit/${packageForScheduling.packageId}?source=schedule"/>" >
					                				<span class="glyphicon glyphicon-eye-open" title="<spring:message code="anvizent.package.label.view"/>"></span>
					                			</a>
					                		</c:otherwise>
				                		</c:choose>
									</c:if>
								</td>
								
								<%-- <td><a href="#" class="runPackage"
								    data-packgeId='<c:out value="${packageForScheduling.packageId}"/>' data-packagetype="<c:out value="${packageForScheduling.isStandard ?'standard':'custom' }"/>" 
								    data-packgeName='<c:out value="${packageForScheduling.packageName}"/>'
									data-isClientDbTables='<c:out value="${packageForScheduling.isClientDbTables}"/>'
									data-industryId='<c:out value="${packageForScheduling.industry.id}"/>'
								    data-schedulerType="runnow"
								    data-scheduleId='<c:out value="${packageForScheduling.scheduleId}"/>'
								    ><spring:message	code="anvizent.package.link.runNow"/></a></td>
							     --%>
								<td>
								<a href="#" class="runwithSchedulerPackage"
								     data-packgeId='<c:out value="${packageForScheduling.packageId}"/>' data-packagetype="<c:out value="${packageForScheduling.isStandard ?'standard':'custom' }"/>" 
								     data-packgeName='<c:out value="${packageForScheduling.packageName}"/>'
									 data-isClientDbTables='<c:out value="${packageForScheduling.isClientDbTables}"/>'
									 data-industryId='<c:out value="${packageForScheduling.industry.id}"/>' 
									 data-schedulerType="<c:out value="${runWithSchedulerOrRunnow}"/>"
									 data-scheduleId='<c:out value="${packageForScheduling.scheduleId}"/>'>
									 <%-- <spring:message code="anvizent.package.link.runwithScheduler"/> --%>
									 <spring:message code="anvizent.package.link.runNow"/>
									 </a>
								</td>
								<c:choose>
									<c:when test="${packageForScheduling.isScheduled == false }">
										<td><a href="#" class='schedulePackage schedulePackageId${packageForScheduling.packageId} ${principal.isSandBox == false ? "" : "disable-links"}' data-packgeId='<c:out value="${packageForScheduling.packageId}"/>' data-packagetype="<c:out value="${packageForScheduling.isStandard ?'standard':'custom' }"/>"
										 data-packgeName='<c:out value="${packageForScheduling.packageName}"/>'
										 data-isClientDbTables='<c:out value="${packageForScheduling.isClientDbTables}"/>'										 
										  data-industryId='<c:out value="${packageForScheduling.industry.id}"/>'  ><spring:message code = "anvizent.package.label.schedule"/></a></td>
									</c:when>
									<c:otherwise>
										<c:if test="${packageForScheduling.scheduleRecurrence == 'Run Now'}">
											<c:set var ="recurrence">
													<spring:message code="anvizent.package.label.recurrence"/>
											</c:set>
											<c:set var ="starttime">
													<spring:message code="anvizent.package.label.startTime"/>
											</c:set>
											<c:set var ="endtime">
													<spring:message code="anvizent.package.label.endTime"/>
											</c:set>
											<c:set var ="range">
													<spring:message code="anvizent.package.label.range"/>
											</c:set>
											<c:set var="test" value="<b>${recurrence}</b> ${packageForScheduling.scheduleRecurrence}<br>										
												<b>${starttime}</b> ${packageForScheduling.scheduleStartTime} <br>
												<b>${endtime}</b> ${packageForScheduling.scheduleRange} ">
											</c:set>
										</c:if> 
										<c:if test="${packageForScheduling.scheduleRecurrence != 'Run Now'}">
										    <c:set var ="recurrence">
													<spring:message code="anvizent.package.label.recurrence"/>
											</c:set>
											<c:set var ="starttime">
													<spring:message code="anvizent.package.label.startTime"/>
											</c:set>
											<c:set var ="endtime">
													<spring:message code="anvizent.package.label.endTime"/>
											</c:set>
											<c:set var ="range">
													<spring:message code="anvizent.package.label.range"/>
											</c:set>
											<c:set var ="timeZone">
													<spring:message code="anvizent.package.label.timeZone"/>
											</c:set>
											<c:set var="test" value="<b>${recurrence}</b> ${packageForScheduling.scheduleRecurrence}<br>
												<b>${starttime}</b> ${packageForScheduling.scheduleStartTime } <br>
												<b>${range}</b> ${packageForScheduling.scheduleRange} <br>
												<b>${timeZone }</b> ${packageForScheduling.timeZone }">
											</c:set>
										</c:if>
										<td>
										<c:if test="${ packageForScheduling.scheduleType == 'UNSCHEDULED' }">
											<a href="#" class='schedulePackage tool schedulePackageId${packageForScheduling.packageId} ${principal.isSandBox == false ? "" : "disable-links"}' data-packgeId='<c:out value="${packageForScheduling.packageId}"/>' data-scheduleId = '<c:out value="${packageForScheduling.scheduleId}"/>' data-packagetype="<c:out value="${packageForScheduling.isStandard ?'standard':'custom' }"/>"
											 data-packgeName='<c:out value="${packageForScheduling.packageName}"/>' data-reSchedule='reRun'
											 data-isClientDbTables='<c:out value="${packageForScheduling.isClientDbTables}"/>'
											  data-industryId='<c:out value="${packageForScheduling.industry.id}"/>'
											 ><spring:message code="anvizent.package.label.schedule"/></a>
											  
										</c:if>
										<c:if test="${ packageForScheduling.scheduleType != 'UNSCHEDULED' }">
											<a href="#" class='schedulePackage tool schedulePackageId${packageForScheduling.packageId} ${principal.isSandBox == false ? "" : "disable-links"}' data-packgeId='<c:out value="${packageForScheduling.packageId}"/>' data-scheduleId = '<c:out value="${packageForScheduling.scheduleId}"/>' data-packagetype="<c:out value="${packageForScheduling.isStandard ?'standard':'custom' }"/>"
											 data-packgeName='<c:out value="${packageForScheduling.packageName}"/>' data-reSchedule='reRun'
											 data-isClientDbTables='<c:out value="${packageForScheduling.isClientDbTables}"/>'
											  data-industryId='<c:out value="${packageForScheduling.industry.id}"/>' title="<c:out value="${test}"/>"><spring:message code="anvizent.package.link.reSchedule"/></a>
											  
											  <c:if test="${packageForScheduling.scheduleRecurrence != 'once' }">
											  <a  href="#" 
											  class='unSchedulePackage tool schedulePackageId${packageForScheduling.packageId} ${principal.isSandBox == false ? "" : "disable-links"}'  
											  data-packageid='<c:out value="${packageForScheduling.packageId}"/>' 
											  data-scheduleid = '<c:out value="${packageForScheduling.scheduleId}"/>'>

											  <spring:message code="anvizent.package.link.unschedule" /> </a>
											  </c:if>
										</c:if>
										</td>
									</c:otherwise>
								</c:choose>
								
								
								<%-- <td>
								<c:if test="${packageForScheduling.scheduleRecurrence != 'Run Now' && packageForScheduling.isScheduled == true}">
									<a href ="<c:url value="/adt/package/scheduledResults/${packageForScheduling.packageId}"/>"> Schedule Results</a>
								</c:if>
								<c:if test="${packageForScheduling.scheduleRecurrence == 'Run Now' || packageForScheduling.isScheduled == false}">
									<spring:message code="anvizent.package.label.nA"/>
								</c:if>
								</td> --%>
								
								<td>
								<c:url value="/adt/package/jobResults/${packageForScheduling.packageId}?source=schedule" var="standardUrl"/>
										<c:url value="/adt/package/viewResultsForCustomPackage/${packageForScheduling.packageId}?source=schedule" var="customUrl"/>
										<a  
											href="${packageForScheduling.isStandard ? standardUrl : customUrl}">
											<spring:message code="anvizent.package.label.viewResults"/>
										</a>
								</td>
								<td>
										<a href ="<c:url value="/adt/package/packageExecutionByPagination/${packageForScheduling.packageId}?packageName=${fn:escapeXml(packageForScheduling.packageName)}&offset=0&limit=10"/>"><spring:message code="anvizent.package.label.viewExecutionResults"/></a>										
								</td>
							</tr>	
							</c:forEach>
							
						</tbody> 
					</table>

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
					<div class='row form-group hidden'>
						<div class='col-sm-3'>
							<label class="radio-inline"><input type="radio" name="runNowOrSchedule"  class="checkedFlaseTrue" value="runnow"  >
							<spring:message code="anvizent.package.label.runNow"/></label>
						</div>
						<c:if test="${principal.isTrailUser == false }">
							<div class='col-sm-3 scheduleRerun' >
								<label class="radio-inline"><input type="radio" name="runNowOrSchedule" class="checkedFlaseTrue" value="schedule">
								<spring:message code = "anvizent.package.label.schedule"/></label>
							</div>
						</c:if>
						<c:if test="${principal.isTrailUser == true }">
							<div class='col-sm-3 scheduleRerun' >
								<label class="radio-inline"><input type="radio" disabled="disabled" name="runNowOrSchedule" class="checkedFlaseTrue" value="schedule">
								<spring:message code = "anvizent.package.label.schedule"/> <img src="<c:url value="/resources/images/lock.png"/>" class="img-responsive lock-symbol" alt="Responsive image"></label>
							</div>
						</c:if>
						<div class='col-sm-3'>
							<label class="radio-inline"><input type="radio" name="runNowOrSchedule"  class="checkedFlaseTrue" value="runwithscheduler"  >
							<spring:message code="anvizent.package.lable.runwithScheduler"/></label>
						</div>
						<div class='col-sm-6'>
							 <div id='runNowOrSchedulevalidation'></div>
						</div>
					</div>
				
				<div id="runNowOptionsDiv" class="row form-group"> 
					<div id="accordion" role="tablist" aria-multiselectable="true">
						<div class="panel panel-default">
							<div class="panel-heading accordion-heading" role="tab">
								<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse1" aria-expanded="true" class="tablebuttons" style="text-decoration: none;">
									<spring:message code = "anvizent.package.label.advancedOptions"/>
								</a>
							</div>
							<div id="collapse1" class="panel-collapse collapse in collapseAndExpandDiv" role="tabpanel">
								<div class="panel-body">
									<div class="row jobRunTypeDiv">
										<label class="control-label col-sm-3"><spring:message code = "anvizent.package.label.jobRunType"/> : </label>					
									   	<div class="col-sm-8">
									   		<label class="radio-inline control-label"><input type="radio" name="runJobType" value="il" id="runOnlyIL"><spring:message code = "anvizent.package.label.runOnlyIL"/></label>
											<label class="radio-inline control-label"><input type="radio" name="runJobType" value="dl" id="runOnlyDL"><spring:message code = "anvizent.package.label.runOnlyModule"/></label>
											<label class="radio-inline control-label"><input type="radio" name="runJobType" value="all" id="runALL" checked="checked"><spring:message code = "anvizent.package.label.runBothIlandModule"/></label>
									    </div>
									</div>
									<div class="row fetchTypeDiv">
										<label class="control-label col-sm-3"><spring:message code = "anvizent.package.label.fetchFrom"/> : </label>					
									   	<div class="col-sm-8">
											<label class="radio-inline control-label"><input type="radio" name="fetchSourceType" value="source" id="source" checked="checked"> <spring:message code = "anvizent.package.label.source"/></label>
											<!-- <label class="radio-inline control-label"><input type="radio" name="fetchSourceType" value="archived" id="archived"> Archive</label> -->
									    </div>
									</div>
								</div>
							</div>	
						</div>
					</div>
				</div>	
					
				<div id="scheduleOptionsDiv" style="display:none;"> 
					<div class="panel panel-info" id='RecurrencePattern'
						style="margin-bottom: 5px;">
						<div class="panel-heading"><spring:message code = "anvizent.package.label.recurrencePattern"/></div>
						<div class="panel-body">
							<div id='recurrencePatternValidation'>
							<div class="row">
								<div class="col-md-3">
								   <%-- <div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="once" value="once"> <spring:message code = "anvizent.package.label.once"/>
										</label>
									</div> --%>
									<div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="minutes" value="minutes"> <spring:message code = "anvizent.package.label.minutes"/>
										</label>
									</div>
									
									<div class="radio">
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
									</div>
									
								</div>
								<div class="col-md-9" id="showRecurrenceOptions">
								
								   <div id='minutesPatternValidation' style="display: none;">
										<div class='row form-group minutesValidation'>
											<spring:message code = "anvizent.package.label.recurEvery"/> 
											<select id="minutesToRun">
												<c:forEach var="i" begin="15" end="30">
													<option value="${i}">${i}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									
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
												<label class="checkbox-inline"> <input type="checkbox"  name="weekDayCheckBox" value="${fn:toUpperCase(weekday)}"> ${weekday}
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
					<button type="button" class="btn btn-primary" id="confirmSchedule"><spring:message code="anvizent.package.button.ok"/></button>
					<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	
	
	<div class="modal fade" tabindex="-1" role="dialog"	id="schedulePackagesPopUp" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title" id="schedulePackagesPopUpHeader"><spring:message code="anvizent.package.label.schedulePackages"/></h4>
				</div>
				<div class="modal-body">
					<div class='row form-group'>
						<div class="table-responsive">
							<table class="table table-striped table-bordered tablebg" id="packagesScheduleTable">
								<thead>
									<tr>
										<th><spring:message code="anvizent.package.label.order"/></th>
										<th><spring:message code="anvizent.package.label.packageId"/></th>
										<th><spring:message code="anvizent.package.label.packageType" /></th>
										<th><spring:message	code="anvizent.package.label.packageName" /></th>
									 </tr>
								</thead>
								 <tbody>
								 
								 </tbody>
							</table>
						</div>		 
					</div>
					
					<div class='row form-group'>
						<div class='col-sm-3'>
							<label class="radio-inline"><input type="radio" name="runNowOrSchedule"  class="checkedFlaseTrue" value="runnow"  >
							<spring:message code="anvizent.package.label.runNow"/></label>
						</div>
						<div class='col-sm-6'>
							 <div id='runNowOrSchedulePackagesvalidation'></div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="confirmSchedulePackages"><spring:message code="anvizent.package.button.ok"/></button>
					<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>


	<div class="modal fade" tabindex="-1" role="dialog" id="showAddedSourcesPopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 90%;">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
				        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.sourceDetails"/></h4>
				      </div>
				      <div class="modal-body">
					        <div class="table-responsive">
								<table class="table table-striped table-bordered tablebg " id="addedSourceDetails">
									<thead>
										<tr>
											<th rowspan="2"><spring:message code="anvizent.package.label.sNo"/></th>
											<th rowspan="2" class="ilName"><spring:message code="anvizent.package.label.ilName"/></th>
											<th rowspan="2"><spring:message code="anvizent.package.label.type"/></th>
											<th><spring:message code="anvizent.package.label.Archived"/><spring:message code="anvizent.package.label.fileName"/></th>
											<th><spring:message code="anvizent.package.label.Archived"/><spring:message code="anvizent.package.label.fileSize"/></th>
											<th><spring:message code="anvizent.package.label.archivedRowCount"/></th>
											<th><spring:message code="anvizent.package.label.archivedUploadedTime"/></th>
											<th rowspan="2"><spring:message code="anvizent.package.label.sourceArchived"/></th>
											<th rowspan="2"><spring:message code="anvizent.package.label.uploadStatus"/></th>
										</tr>
									</thead>
									<tbody>
										
									</tbody>
								</table>
							</div>
				      </div>
				      <div class="modal-footer">
				      	<form method="POST" action="<c:url value="/admin/vertical/delete"/>">
					        <button type="button" class="btn btn-primary" id="confirmUploadSources"><spring:message code="anvizent.package.button.ok"/></button>
					        <button type="button" class="btn btn-default cancelUploadingSources" value="closeModal"><spring:message code="anvizent.package.button.cancel"/></button>
						    <input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
						</form>	   
				      </div>
			    </div> 
		  </div> 
	</div>
	
	  <div class="modal fade" tabindex="-1" role="dialog" id="packageExecutionAlreadyStartedPopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.packageExecution"/></h4>
		      </div>
		      <div class="modal-body">
                <p>  
	 	         <spring:message code="anvizent.package.label.thisPackageExecutionAlreadyStarted"/>
	 	         </p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary" style="float:left" id="terminateAndStart"><spring:message code="anvizent.package.label.terminateStart"/></button>
		        <button type="button" class="btn btn-primary" style="float:left" id="executionStatus"><spring:message code="anvizent.package.label.viewExecutionStatus"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.cancel"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
	
	<div class="sourceOrArchiveDiv" style="display:none;">
		<label class="radio-inline control-label"><input type="radio" name="sourceType" value="source" class="source sourceType"> <spring:message code="anvizent.package.label.source"/></label>
		<label class="radio-inline control-label"><input type="radio" name="sourceType" value="archived" class="archived sourceType" checked="checked"> <spring:message code="anvizent.package.label.archive"/></label>
	</div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="alertForUnSchedulePopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 500px;">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.link.unschedule"/></h4>
				      </div>
				      <div class="modal-body">
				        	<p><spring:message code="anvizent.package.message.text.areYouSureYouWantToUnscheduleThePackage"/></p>
				      </div>
				      <div class="modal-footer" style="text-align: center;">					        	
							<button type="button" class="btn btn-primary btn-sm confirmUnSchedulePackage" id = "checked"><spring:message code="anvizent.package.button.ok"/></button>
							<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				      </div>
			    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
	 </div> 
</div>
										