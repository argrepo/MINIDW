<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">

	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.clientScheduler" /> </h4>
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
	
	<div id="scheduleTable" style="display:none">
		<div class='row form-group'></div>
		<div class="row form-group tblIntern">
			<div class="table-responsive">
			<!-- <h4>Schedule Job Info</h4> -->
				<table class="table table-striped table-bordered tablebg"
					id="scheduleTbl">
					<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.JobId"/></th>
								<th><spring:message code="anvizent.package.label.jobName"/></th>
								<th><spring:message code="anvizent.package.label.GroupName"/></th>
								<th><spring:message code="anvizent.package.label.StartTime"/></th>
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
							class="btn btn-primary btn-sm"><spring:message code="anvizent.admin.button.Back"/></button>
					</div>
					<table class="table table-striped table-bordered tablebg"
						id="triggerTbl" style="margin-top: 15px;">
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.TriggerID"/></th>
								<th><spring:message code="anvizent.package.label.Description"/></th>
								<th><spring:message code="anvizent.package.label.StartTime"/></th>
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

    <a href="<c:url value="/adt/package/schedule" />" class="btn btn-primary btn-sm backSchedulers" style="display:none"><spring:message code="anvizent.admin.button.Back"/></a>
<div id="datetime"></div>
</div>
