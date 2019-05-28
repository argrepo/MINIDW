<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText">AI Global Context Parameters</h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<%-- <jsp:include page="admin_error.jsp"></jsp:include> --%>
		<%-- <input type="hidden" id="userID"
			value="<c:out value="${principal.userId}"/>"> --%>
			<input type="hidden" id="userID" value="${principal.userId}">
	</div>

	<div class="col-sm-10">
		<div id="successOrErrorMessage"></div>
	</div>
	
	<div id="AIContextParametersInfoTable">
		<div class='row form-group'>
			<button style="float: right; margin-right: 1.5em;"
				class="btn btn-sm btn-success addAIContextParameters">Add</button>
		</div>
		<div class="row form-group tblIntern">
			<div class="table-responsive">
				<table class="table table-striped table-bordered tablebg"
					id="aiGlobalContextParamTbl">
					<thead>
						<tr>
							<th><spring:message code="anvizent.package.label.pid" /></th>
							<th><spring:message code="anvizent.package.label.paramName" /></th>
							<th><spring:message code="anvizent.package.label.paramValue" /></th>
							<th><spring:message code="anvizent.package.label.isActive" /></th>
							<th><spring:message code="anvizent.package.label.edit" /></th>
							<th><spring:message code="anvizent.admin.button.Delete" /></th>
						</tr>
					</thead>
					<tbody>
									<c:forEach items="${aiContextParameters}" var="aiContextParameter" varStatus="index">
										<tr>
											<td><c:out value="${aiContextParameter.pid}" /></td>
											<td><c:out value="${aiContextParameter.paramName}" /></td>
											<td><c:out value="${aiContextParameter.paramValue}" /></td>
											<td><c:out value="${aiContextParameter.active ? 'Yes': 'No'}" /></td>
											<td>
												<button class="btn btn-primary btn-sm editDetails" id="pid" value="<c:out value="${aiContextParameter.pid}" />" title="<spring:message code="anvizent.package.label.edit"/>" >
													<i class="fa fa-pencil" aria-hidden="true"></i>
												</button>
											</td>
											<td><button class="btn btn-primary btn-sm deleteAiContextParameters" id="pid" value="<c:out value="${aiContextParameter.pid}" />" title="<spring:message code="anvizent.package.label.edit"/>" >
													<i class="fa fa-trash" aria-hidden="true"></i>
												</button></td>								
										</tr>
									</c:forEach>
								</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div class="col-sm-10 addAIContextParametersDiv" style="display:none">
			<div class="panel panel-default">
				<div class="panel-heading"> Add AI Context Parameters</div>
					<div class="panel-body">
					<input type="hidden" name="pid" id="idValue">
							<div class='row form-group'>
									<div class='control-label col-sm-2'>
										<!-- Param Name -->
										<spring:message code="anvizent.package.label.paramName" />
									</div>
									<div class='col-sm-6'>
									    <input type="hidden" id="pid">
										<input type="text" id="paramName" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
						   </div>
								
							<div class='row form-group'>
									<div class='control-label col-sm-2'>
										<!-- ParamValue -->
										<spring:message code="anvizent.package.label.paramValue" />
									</div>
									<div class='col-sm-6'>
										<input type="text" id="paramValue" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
						   </div>
								
								
							<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.ActiveStatus"/>
									</div>
									<div id="active">
									<div class='col-sm-1'>
										<input type="radio" name="isActive" id="isActiveYes" value="true"><spring:message code="anvizent.package.label.YES"/>
									</div>
									<div class='col-sm-1'>
										<input type="radio" name="isActive" id="isActiveNo" value="false"> <spring:message code="anvizent.package.label.NO"/>
									</div>
								</div>
							</div>	
						   <div>
								<div class='col-sm-1'>
								<button id="SaveAiContextParameters" class="btn btn-primary btn-sm">Save</button>
								</div>
								<div class='col-sm-9'>
									<button id="back" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Back"/></button>
								</div>
							</div>
		
								
						</div>
						
						
					</div>
				</div>
<div class="modal fade" tabindex="-1" role="dialog" id="deleteAIContextParam" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title">
							Delete Context Parameter
						</h4>
					</div>
					<div class="modal-body">
						<p>
							Are you sure , do you want to delete the Context Parameter ?
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="confirmDeleteAIContextParam">
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
	
