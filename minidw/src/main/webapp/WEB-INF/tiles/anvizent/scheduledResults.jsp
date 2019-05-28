<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">

	<div class="dummydiv"></div>
	<ol class="breadcrumb">
	</ol>
	<div class="row form-group">
		<h4 class="alignText">Scheduled Results</h4>
	</div>
	<div class='col-sm-12' style="padding: 0px;">

		<form method="POST" id="scheduleInfoFrom" action="<c:url value="/adt/package/scheduledInfo"/>">
			<input type="hidden" name="_csrf" value="${_csrf.token}"/>
			<input type="hidden" name="packageId" value="${packageId}"/>
			<input type="hidden" name="scheduleStartTime"/>
		</form>	

	
		<div role="tabpanel" class="tab-pane fade active in tabpad">
			<div class="row form-group">
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg "
						id="scheduledResultsTable">
						<thead>
							<tr>
								<th>ID</th>
								<th>Schedule Start Time</th>
								<th>Sources</th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${not empty scheduledResults}">
							<c:forEach items="${scheduledResults}" var="scheduledResult"
								varStatus="index">
								<c:set var="varDetailsToolTip" value="<b>Client scheduler details:</b> ${scheduledResult.clientSchedulerStatus}<br>
								<b>Server scheduler details:</b> ${scheduledResult.serverSchedulerStatus}<br>">
											</c:set>
								<tr>
									<td><c:out value="${index.index+1}" /></td>
									<td class = "scheduleDetails"><span class="tool" title="${varDetailsToolTip }">
									
									<button class="btn btn-primary btn-sm scheduleInfo" data-scheduleTime="${scheduledResult.scheduleStartTime}" >
									
									${scheduledResult.scheduleStartTime} </button></span></td>
									<td><span class="tool" title="${varDetailsToolTip }">${scheduledResult.count}</span></td>
								</tr>
							</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	
	
	<c:if test="${not empty scheduledInfo1}">
		<div role="tabpanel" class="tab-pane fade active in tabpad">
			<div class="row form-group">
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg "
						id="schedInfo">
						<thead>
							<tr>
								<th>S.NO</th>
								<th>Schedule Id</th>
								<th>DL Name</th>
								<th>IL Name</th>
								<th>Client scheduler status</th>
								<th>Client Scheduler Status Details</th>
								<th>Server Scheduler Status</th>
								<th>Server Scheduler Status Details</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${scheduledInfo1}" var="scheduleInfo"
								varStatus="index">
								<tr>
									<td><c:out value="${index.index+1}"/></td>
									<td>${scheduleInfo.id}</td>
									<td>${scheduleInfo.dlName}</td>
									<td>${scheduleInfo.ilName}</td>
									<td>${scheduleInfo.clientSchedulerStatus}</td>
									<td>${scheduleInfo.clientSchedulerStatusDetails}</td>
									<td>${scheduleInfo.serverSchedulerStatus}</td>
									<td>${scheduleInfo.serverSchedulerStatusDetails}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</c:if>
	</div>
</div>