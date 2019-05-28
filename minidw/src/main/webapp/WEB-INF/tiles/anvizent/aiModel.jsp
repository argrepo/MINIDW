<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText">AI Models</h4>
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
	
	<div id="aiModelInfoTable">
		<div class='row form-group'>
			<button style="float: right; margin-right: 1.5em;"
				class="btn btn-sm btn-success addAiModel">Add</button>
		</div>
		<div class="row form-group tblIntern">
			<div class="table-responsive">
				<table class="table table-striped table-bordered tablebg"
					id="aiModelInfoTbl">
					<thead>
						<tr>
							<th><spring:message code="anvizent.package.label.id"/></th>
							<th><spring:message code="anvizent.package.label.aiModelName"/></th>
							<th><spring:message code="anvizent.package.label.aiModelDescription"/></th>
							<th><spring:message code="anvizent.package.label.edit"/></th>
							<th><spring:message code="anvizent.package.label.delete"/></th>
						</tr>
					</thead>
					<tbody>
									<c:forEach items="${aiModels}" var="aiModel" varStatus="index">
										<tr>
											<td><c:out value="${aiModel.id}" /></td>
											<td><c:out value="${aiModel.aIModelName}" /></td>
											<td><c:out value="${aiModel.aiModelType}" /></td>
											<td>
												<button class="btn btn-primary btn-sm editDetails" id="id" value="<c:out value="${aiModel.id}" />" title="<spring:message code="anvizent.package.label.edit"/>" >
													<i class="fa fa-pencil" aria-hidden="true"></i>
												</button>
											</td>
											<td><button class="btn btn-primary btn-sm deleteAIModel" id="id" value="<c:out value="${aiModel.id}" />" title="<spring:message code="anvizent.package.label.edit"/>" >
													<i class="fa fa-trash" aria-hidden="true"></i>
												</button></td>								
										</tr>
									</c:forEach>
								</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div class="col-sm-10 addAImodelDiv" style="display:none">
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
									<button id="back" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Back"/></button>
								</div>
							</div>
		
								
						</div>
						
						
					</div>
				</div>
<div class="modal fade" tabindex="-1" role="dialog" id="deleteAIModel" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title">
							Delete AI Model
						</h4>
					</div>
					<div class="modal-body">
						<p>
							Are you sure , do you want to delete the  AI Model ?
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="confirmDeleteAIModel">
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
	
