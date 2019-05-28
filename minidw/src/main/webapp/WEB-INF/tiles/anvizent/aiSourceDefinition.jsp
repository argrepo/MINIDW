<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText">AI Source Definition</h4>
 	</div>

 	<input type="hidden" id="userID" value="${principal.userId}">
	<%-- <jsp:include page="admin_error.jsp"></jsp:include> --%>
	<div class="col-md-12 message-class"></div>
	<div class="col-sm-12">
			<div class="col-sm-10">
		<div class="queryValidatemessageDiv"></div>
	</div>	
	<div id="aiSourceDefinitionTable">
		<div class='row form-group'>
			<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success addAISourceDefBtn"><spring:message code="anvizent.package.label.create"/></a>
		</div>
		<div class="row form-group">
			<div class="table-responsive">
				<table class="table table-striped table-bordered tablebg"
					id="aiSourceDefTbl">
					<thead>
						<tr>
							<th><spring:message code="anvizent.package.label.Id"/></th>
							<th> <spring:message code="anvizent.package.label.businessModal"/></th>
							<th> <spring:message code="anvizent.package.label.stagingTable"/></th>
							<th><spring:message code="anvizent.package.label.sourceQuery"/></th>
							<th><spring:message code="anvizent.package.label.active"/></th>
							<th><spring:message code="anvizent.package.label.Edit"/></th>
							<th><spring:message code="anvizent.package.label.delete"/></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${aiSourceDefList}" var="aiSource">
						<tr>
							<td>${aiSource.sourceDefId}</td>
							<td>${aiSource.bussinessName}</td>
							<td>${aiSource.stagingTbl}</td>
							<td>
								<button class="btn btn-primary btn-sm tablebuttons sourceQueryById" value="${aiSource.sourceDefId}" data-sourceid="${aiSource.sourceDefId}">
											View Query
								</button>
							</td>
							<td>${aiSource.isActive ? 'Yes': 'No'}</td>
							<td>
								<button class="btn btn-primary btn-sm editSourceQuery" value="${aiSource.sourceDefId}" data-sourceid="${aiSource.sourceDefId}" title="<spring:message code="anvizent.package.label.edit"/>" >
										<i class="fa fa-pencil" aria-hidden="true"></i>
								</button>
							</td>
							<td>
								<button class="btn btn-primary btn-sm deleteSourceQuery" value="${aiSource.sourceDefId}" data-sourceid="${aiSource.sourceDefId}" title="<spring:message code="anvizent.package.label.delete"/>" >
									<i class="fa fa-trash" aria-hidden="true"></i>
								</button>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	
	<div class="row form-group createAISourceDiv hidden">
		<input type="hidden" id="selectedSourceId">
			<div class="panel panel-default">
				<div class="panel-heading">  <spring:message code="anvizent.package.label.aiSourceDefinition"/> </div>
					<div class="panel-body">
							<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.businessModal"/>
									</div>
									<div class='col-sm-5'>
										<select class="selectConnector form-control" name="businessId" id="businessModalId"> 
									   		<option value="0" selected>Select Business Use Case</option>
										    <c:forEach items="${aiBussinessModalSet}" var="businessProblem">
												<option value="${businessProblem}"><c:out value="${businessProblem}"/></option>
											</c:forEach>
									    </select>
									</div>
							</div>
							
						<%-- 	
							<div class='row form-group aimodelDiv hidden'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.aiModelName" />
									</div>
									<div class='col-sm-5'>
										<select class="form-control" id="aiModalId"> 
										    <c:forEach items="${aiModalInfoList}" var="aiModal">
												<option value="${aiModal.id}"><c:out value="${aiModal.aIModelName}"/></option>
											</c:forEach>
									    </select>
									</div>
							</div> --%>
					
							<div class='row form-group bussinessUsecaseStagingTableDiv hidden'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.stagingTable"/>
									</div>
									<div class='col-sm-5'>
										<select class="selectConnector form-control" name="stagingTbl" id="stagingTable"> 
									   		<option value="0" selected>Select staging table</option>
										    <c:forEach items="${aiBussinessusecaseStagingTable}" var="aIStagingTable">
												<option value="${aIStagingTable}"><c:out value="${aIStagingTable}"/></option>
											</c:forEach>
									    </select>
									</div>
							</div>
							
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.sourceQuery"/>
									</div>
									<div class='col-sm-5'>
										<textarea rows="4" cols="50" id="sourceQuery" name="sourceQuery" class="form-control"></textarea> 
									</div>
										<div class='col-sm-2'>
									<input type="button" value='<spring:message code="anvizent.package.button.preview"/>'
												id='checkTablePreview' class="btn btn-primary btn-sm" data-target='#tablePreviewPopUp' /> 
									</div>			
							</div>
							
							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.active"/>:</label>
								<div class='col-sm-5 active'>
										<label class="radio-inline"><input type="radio" name="isActive" value="true"><spring:message code="anvizent.package.button.yes"/></label> 
										<label class="radio-inline"><input type="radio" name="isActive" value="false"><spring:message code="anvizent.package.button.no"/></label>
									</div>
							</div>
								
								
							
							<div>
								<div class='col-sm-9'>
									<button type="button" class="btn btn-primary btn-sm" id="saveAiSourceBtn">
											<spring:message code="anvizent.package.button.save"/>
									</button>
									<button id="aiSourceBack" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Back"/></button>
								</div>
							</div>
						</div>
						
					</div>
	</div>
	
	<!-- Table Preview PopUp window -->
								<div class="modal fade" tabindex="-1" role="dialog" id="tablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
									<div class="modal-dialog" style="width: 90%;">
										<div class="modal-content">
											<div class="modal-header">
												<button type="button" class="close" data-dismiss="modal" aria-label="Close">
													<span aria-hidden="true">&times;</span>
												</button>
												<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
												<h4 class="modal-title custom-modal-title" id="tablePreviewPopUpHeader"></h4>
											</div>
											<div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto; overflow-x: auto;">
												<table class='tablePreview table table-striped table-bordered tablebg'></table>
											</div>
											<div class="modal-footer">
												<button type="button" class="btn btn-default" data-dismiss="modal">
													<spring:message code="anvizent.package.button.close" />
												</button>
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
									        <h4 class="modal-title custom-modal-title heading">Source Query</h4>
									      </div>
									      <div class="modal-body" style="max-height: 400px; overflow-y: auto;">
												<div style='overflow-y: auto;max-height: 300px;'>
													<textarea class='view-Query' readonly="readonly" rows="10" cols="10" style="width:100%">
															
													</textarea>
												</div>
									      </div>
									      <div class="modal-footer">
									        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
									      </div>
									    </div><!-- /.modal-content -->
									  </div><!-- /.modal-dialog -->
									</div>
	   <div class="modal fade" tabindex="-1" role="dialog" id="deleteAISourceDefinition" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title">
							Delete AI Source Definition
						</h4>
					</div>
					<div class="modal-body">
						<p>
							Are you sure,do you want to delete the AI Source Definition?
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="confirmDeleteAISourceDefinition">
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
</div>