<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">

	<div class='row form-group'>
		<h4 class="alignText"> <spring:message code="anvizent.package.label.ClientScheduler"/></h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID"
			value="<c:out value="${principal.userId}"/>"> <input
			type="hidden" id="client_id"
			value="<c:out value="${PLAIN_CLIENT_ID}"/>">
	</div>

	<div class="col-sm-10">
		<div id="successOrErrorMessage"></div>
	</div>
	

	<div class="col-sm-12">
		<div class='row form-group'>
			<div class='col-sm-2'> <spring:message code="anvizent.package.label.SchedulerStatus"/></div>
			<input type="hidden" id="statusRunOrStop" value="${status}">
			${status}
			<div class='col-sm-3' id="runningDiv" style="display: none;">
				<label class="control-label"><spring:message code="anvizent.package.label.Running"/> </label>
				<button id="stop" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Stop"/></button>
			</div>
			<div class='col-sm-3' id="stoppedDiv" style="display: none;">
				<label class="control-label">  <spring:message code="anvizent.package.label.Stopped"/></label>
				<button id="start" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Start"/></button>
			</div>
		</div>
	</div>

	<div class="col-sm-10 hidden">
		<div class='col-sm-3'>
			<label class="col-sm-5 control-label labelsgroup"> <spring:message code="anvizent.package.label.FromDate"/></label>
			<div class="form-group">
				<div class='date'>
					<input type="text" class="datepicker" style="width: 100px;"
						id='fromDate' />
				</div>
			</div>
		</div>
		<div class='col-sm-3'>
			<label class="col-sm-5 control-label labelsgroup"> <spring:message code="anvizent.package.label.ToDate"/></label>
			<div class="form-group">
				<div class='date'>
					<input type="text" class="datepicker" style="width: 100px;"
						id='toDate' />
				</div>
			</div>
		</div>

		<div class='row form-group'>
			<div class="col-sm-6">
				<label class="col-sm-2 control-label labelsgroup"><spring:message code="anvizent.package.label.status"/></label>
				<div class='col-sm-6'>
					<select class="form-control" multiple="multiple"
						id="scheduleStatus" name="status">
						<option value="PAUSE"><spring:message code="anvizent.package.label.PAUSED"/></option>
						<option value="READY"><spring:message code="anvizent.package.label.READY"/></option>
					</select>
				</div>
			</div>
		</div>


		<div class='row form-group'>
			<div class="col-sm-6">
				<label class="col-sm-2 control-label labelsgroup"> <spring:message code="anvizent.package.label.FilterBy"/></label>
				<div class='col-sm-6 groupNamesList'>
					<select id="groupName" multiple="multiple">
					</select>
				</div>
			</div>

		</div>

		<div class='row form-group'>
			<div class='col-sm-2'>
				<button id="search" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Search"/></button>
			</div>
		</div>
	</div>
	
	<div class="col-md-12" id="schedulerRunningInfo" style="dispaly: none">
				<div class='row form-group'>
					<label class="col-sm-2 control-label labelsgroup"><spring:message code="anvizent.package.label.ClientId"/>:</label>
					<div class='col-sm-6 clientSelectedVal' style="dispaly: none">
						<span id="clientIdValue"> </span>
						<button id="editClientId" class="btn btn-primary btn-sm"
							style="dispaly: none"><spring:message code="anvizent.package.label.Change"/></button>
					</div>

					<div class='col-sm-6 clientList' style="dispaly: none">
						<select id="clientId">
						</select>
						<button id="refresh" style="dispaly: none"
							class="btn btn-primary btn-sm">
							<span class="glyphicon glyphicon-repeat"></span>
						</button>
						<button id="saveClientId" class="btn btn-primary btn-sm"
							style="dispaly: none"><spring:message code="anvizent.package.label.Submit"/></button>
						<c:if test="${not empty PLAIN_CLIENT_ID}">
							<button class="btn btn-primary btn-sm" style="dispaly: none"
								id="cancelBtn"><spring:message code="anvizent.package.label.Cancel"/></button>
						</c:if>
					</div>
				</div>

		<div id="scheduleTable" style="display: none">
			<div class='row form-group'></div>
			<div class="row form-group tblIntern">
				<div class="table-responsive">
					<button id="refreshJobData"
						class="btn btn-primary btn-sm pull-right">
						<span class="glyphicon glyphicon-repeat"></span>
					</button>
					<br>

					<table class="table table-striped table-bordered tablebg"
						id="scheduleTbl" style="margin-top: 15px;">
						<thead>
							<tr>
								<th> <spring:message code="anvizent.package.label.JobId"/></th>
								<th><spring:message code="anvizent.package.label.JobName"/></th>
								<th> <spring:message code="anvizent.package.label.GroupName"/></th>
								<th> <spring:message code="anvizent.package.label.StartTime"/></th>
								<th><spring:message code="anvizent.package.label.EndTime"/></th>
								<th><spring:message code="anvizent.package.label.status"/></th>
								<th><spring:message code="anvizent.package.label.NextFireTime"/></th>
								<th><spring:message code="anvizent.package.label.Action"/></th>
								<th><spring:message code="anvizent.package.label.ViewJobResults"/></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>

		<div id="scheduleTriggerTable" style="display: none">
			<div class='row form-group'></div>
			<div class="row form-group tblIntern">
				<div class="table-responsive">
					<div class='row form-group backBtn' style="display: none">
						<button type="button" id="back"
							style="float: right; margin-right: 1.5em;"
							class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Back"/></button>
					</div>
					<table class="table table-striped table-bordered tablebg"
						id="triggerTbl" style="margin-top: 15px;">
						<thead>
							<tr>
								<th> <spring:message code="anvizent.package.label.TriggerID"/></th>
								<th><spring:message code="anvizent.package.label.Description"/></th>
								<th> <spring:message code="anvizent.package.label.StartTime"/></th>
								<th><spring:message code="anvizent.package.label.EndTime"/></th>
								<th><spring:message code="anvizent.package.label.NextFireTime"/></th>
								<th><spring:message code="anvizent.package.label.status"/></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
