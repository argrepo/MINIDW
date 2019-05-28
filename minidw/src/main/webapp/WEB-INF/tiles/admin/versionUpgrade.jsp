<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">

	<div class='row form-group'>
		<h4 class="alignText">
			<spring:message code="anvizent.package.label.VersionUpgrade" />
		</h4>
	</div>
	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	<c:url value="/admin/versionUpgrade/edit" var="editUrl" />
	<div class="col-sm-12">
		<form:form modelAttribute="versionUpgrade" action="${editUrl}">
			<c:choose>
				<c:when test="${versionUpgrade.pageMode == 'list'}">
					<div class='row form-group'>
						<a style="float: right; margin-right: 1.5em;"
							class="btn btn-sm btn-success"
							href="<c:url value="/admin/versionUpgrade/add"/>"><spring:message
								code="anvizent.package.label.Add " /></a>
					</div>
					<div class='row form-group'>
						<div class="table-responsive">
							<table class="table table-striped table-bordered tablebg "
								id="versionUpgradeTable">
								<thead>
									<tr>
										<th><spring:message code="anvizent.package.label.sNo" /></th>
										<th><spring:message
												code="anvizent.package.label.versionId" /></th>
										<th><spring:message
												code="anvizent.package.label.versionNumber" /></th>
										<th><spring:message
												code="anvizent.package.label.description" /></th>
										<th><spring:message
												code="anvizent.package.label.filePath" /></th>
										<th><spring:message
												code="anvizent.package.label.Latestversion" /></th>
										<th><spring:message code="anvizent.package.label.edit" /></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${versionUpgradeModel}" var="versionUpgList"
										varStatus="index">
										<tr class="${versionUpgList.latestVersion?'active':'' }">
											<td>${index.index+1}</td>
											<td><c:out value="${versionUpgList.versionId}" /></td>
											<td><c:out value="${versionUpgList.versionNumber}" /></td>
											<td><c:out value="${versionUpgList.description}" /></td>
											<td><c:out value="${versionUpgList.filePath}" /></td>
											<td><c:out
													value="${versionUpgList.latestVersion?'Yes':'No'}" /></td>
											<td>
												<button class="btn btn-primary btn-sm tablebuttons"
													name="versionId" value="${versionUpgList.versionId}"
													title="<spring:message code="anvizent.package.label.edit"/>">
													<i class="fa fa-pencil" aria-hidden="true"></i>
												</button>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</c:when>

				<c:when
					test="${versionUpgrade.pageMode == 'edit' || versionUpgrade.pageMode == 'add' }">
					<div class="panel panel-default">
						<div class="panel-heading">
							versionUpgrade Details
							<spring:message code="anvizent.package.label.sNo" />
						</div>
						<div class="panel-body">
							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message
										code="anvizent.package.label.versionNumber" />:</label>
								<div class='col-sm-6'>
									<form:input path="versionNumber" class="form-control"
										data-maxlength="255" />
								</div>
							</div>

							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message
										code="anvizent.package.label.description" />:</label>
								<div class='col-sm-6'>
									<form:textarea path="description" class="form-control" />
								</div>
							</div>

							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message
										code="anvizent.package.label.filePath" />:</label>
								<div class='col-sm-6'>
									<form:input path="filePath" class="form-control"
										data-maxlength="255" />
								</div>
							</div>

							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message
										code="anvizent.package.label.LatestVersionToUpgrade" />:</label>
								<div class='col-sm-6'>
									<form:checkbox path="latestVersion" />
								</div>
							</div>

							<div class="row form-group">
								<label class="control-label col-sm-3"></label>
								<div class="col-sm-6">
									<c:url value="/admin/versionUpgrade/add" var="addUrl" />
									<input type="hidden" value="${addUrl}" id="addUrl">
								</div>
							</div>
							<div class="row form-group">
								<div class="col-sm-6">
									<c:choose>
										<c:when test="${versionUpgrade.pageMode == 'edit'}">
											<button id="updateVersionUpgrade" type="button"
												class="btn btn-primary btn-sm">
												<spring:message code="anvizent.package.button.update" />
											</button>
											<form:hidden path="versionId" class="form-control" />
										</c:when>
										<c:when test="${versionUpgrade.pageMode == 'add'}">
											<button id="addVersionUpgrade" type="button"
												class="btn btn-primary btn-sm">
												<spring:message code="anvizent.package.button.save" />
											</button>
										</c:when>
									</c:choose>
									<a href="<c:url value="/admin/versionUpgrade"/>"
										class="btn btn-primary btn-sm back_btn"><spring:message
											code="anvizent.package.link.back" /></a>
								</div>
							</div>
						</div>
					</div>
				</c:when>
			</c:choose>
		</form:form>
	</div>
</div>