<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText">
			<spring:message code="anvizent.package.label.ServerConfiguration" />
		</h4>
	</div>

	<jsp:include page="admin_error.jsp"></jsp:include>

	<input type="hidden" id="userId" value="${principal.userId}">
	<c:url value="/admin/serverConfigurations" var="url" />
	<input id="edit" type="hidden" value="${url}/edit"> <input
		id="save" type="hidden" value="${url}/save"> <input
		id="testConnection" type="hidden" value="${url}/testConnection">

	<form:form modelAttribute="serverConfigurationsForm" action="${url}">

		<c:if test="${serverConfigurationsForm.pageMode == 'list'}">
			<form:hidden path="id" />
			<div style="width: 100%; padding: 0px 15px;">
				<div class="row form-group"
					style="padding: 5px; border-radius: 4px;">
					<a href="<c:url value="/admin/serverConfigurations/add"/>"
						class="btn btn-success btn-sm" style="float: right;"> <spring:message
							code="anvizent.package.label.add" />
					</a>
				</div>
			</div>

			<div class='row form-group'>
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg "
						id="existingServerConfigurations">
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.Sr.No." /></th>
								<th><spring:message
										code="anvizent.package.label.ServerName" /></th>
								<th><spring:message
										code="anvizent.package.label.IPHostName" /></th>
								<th><spring:message
										code="anvizent.package.label.Description" /></th>
								<th><spring:message
										code="anvizent.package.label.activeStatus" /></th>
								<th><spring:message code="anvizent.package.label.edit" /></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${serverConfigList}" var="serverConfig"
								varStatus="loop">
								<tr>
									<td>${loop.index+1}</td>
									<td>${serverConfig.serverName}</td>
									<td>${serverConfig.ipAddress}</td>
									<td>${serverConfig.description}</td>
									<td>${serverConfig.activeStatus ? 'Yes' : 'No'}</td>
									<td>
										<button type="submit"
											class="btn btn-primary btn-sm tablebuttons editServerConfig"
											value="${serverConfig.id}">
											<i class="fa fa-pencil" aria-hidden="true"></i>
										</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:if>

		<c:if
			test="${serverConfigurationsForm.pageMode == 'add' || serverConfigurationsForm.pageMode == 'edit'}">
			<div class="col-sm-12">
				<div class="panel panel-default">
					<div class="panel-heading">${serverConfigurationsForm.pageMode == 'add' ? 'Add Server Configuration' : 'Edit Server Configuration' }</div>
					<div class="panel-body">

						<div class="row form-group">
							<label class="control-label col-sm-3"> <spring:message
									code="anvizent.package.label.ServerName" /> :
							</label>
							<div class="col-sm-8">
								<form:hidden path="id" />
								<form:input path="serverName" cssClass="form-control" />
							</div>
						</div>

						<div class="row form-group">
							<label class="control-label col-sm-3"> <spring:message
									code="anvizent.package.label.ShortName" />:
							</label>
							<div class="col-sm-8">
								<form:input path="shortName" cssClass="form-control" />
							</div>
						</div>

						<div class="row form-group">
							<label class="control-label col-sm-3"> <spring:message
									code="anvizent.package.label.Description" />:
							</label>
							<div class="col-sm-8">
								<form:input path="description" cssClass="form-control" />
							</div>
						</div>

						<div class="row form-group">
							<label class="control-label col-sm-3"> <spring:message
									code="anvizent.package.label.IPAddress" />:
							</label>
							<div class="col-sm-8">
								<form:input path="ipAddress" cssClass="form-control" />
							</div>
						</div>

						<div class="row form-group">
							<label class="control-label col-sm-3"><spring:message
									code="anvizent.package.label.PortNumber" />:</label>
							<div class="col-sm-8">
								<form:input path="portNumber" cssClass="form-control" />
							</div>
						</div>

						<div class="row form-group">
							<label class="control-label col-sm-3"><spring:message
									code="anvizent.package.label.MinidwSchemaName" />:</label>
							<div class="col-sm-8">
								<form:input path="minidwSchemaName" cssClass="form-control" />
							</div>
						</div>

						<div class="row form-group">
							<label class="control-label col-sm-3"> <spring:message
									code="anvizent.package.label.AnvizentSchemaName" /> :
							</label>
							<div class="col-sm-8">
								<form:input path="anvizentSchemaName" cssClass="form-control" />
							</div>
						</div>

						<div class="row form-group">
							<label class="control-label col-sm-3"> <spring:message
									code="anvizent.package.label.UserName" /> :
							</label>
							<div class="col-sm-8">
								<form:input path="userName" cssClass="form-control" />
							</div>
						</div>

						<div class="row form-group">
							<label class="control-label col-sm-3"><spring:message
									code="anvizent.package.label.Password" /> :</label>
							<div class="col-sm-8">
								<form:password path="serverPassword" cssClass="form-control"
									value="${serverConfigurationsForm.serverPassword}" />
							</div>
						</div>

						<div class="row form-group">
							<label class="control-label col-sm-3"><spring:message
									code="anvizent.package.label.Clientschemadetailsendpoint" />:</label>
							<div class="col-sm-8">
								<form:input path="clientDbDetailsEndPoint"
									cssClass="form-control"
									placeholder="http://anvizentdomainname.com/anvizentcorews/core/anvizentservice/getClientSchemaDetails/{clientId}" />
							</div>
						</div>

						<div class="row form-group">
							<label class="control-label col-sm-3"><spring:message
									code="anvizent.package.label.activeStatus" /> :</label>
							<div class="col-sm-8">
								<label class="radio-inline control-label"> <form:radiobutton
										path="activeStatus" value="true" />
									<spring:message code="anvizent.package.button.yes" />
								</label> <label class="radio-inline control-label"> <form:radiobutton
										path="activeStatus" value="false" />
									<spring:message code="anvizent.package.button.no" />
								</label>
							</div>
						</div>

						<div class="row form-group">
							<button type="button"
								class="btn btn-primary btn-sm testConnection">
								<spring:message code="anvizent.package.button.testConnection" />
							</button>
							<button type="button"
								class="btn btn-primary btn-sm saveServerConfigDetails">
								<spring:message code="anvizent.package.button.save" />
							</button>
							<a href="<c:url value="/admin/serverConfigurations"/>"
								class="btn btn-primary btn-sm back_btn back startLoader"><spring:message
									code="anvizent.package.link.back" /></a>
						</div>
					</div>

				</div>
			</div>
		</c:if>

	</form:form>

</div>
