<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText">Job Tags</h4>
 	</div>

 	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	<div class="col-md-12 message-class"></div>
	<div class="col-sm-12">
				
	<div id="configTagsTable">
		<div class='row form-group'>
			<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success addJobTagsBtn"><spring:message code="anvizent.package.label.create"/></a>
		</div>
		<div class="row form-group tblConfig">
			<div class="table-responsive">
				<table class="table table-striped table-bordered tablebg"
					id="configTagsTbl">
					<thead>
						<tr>
							<th><spring:message code="anvizent.package.label.Id"/></th>
							<th><spring:message code="anvizent.package.label.tagName"/></th>
							<th><spring:message code="anvizent.package.label.IsActive"/></th>
							<th><spring:message code="anvizent.package.label.Edit"/></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${eltJobsTagsList}" var="eltjobtag">
						<tr>
							<td>${eltjobtag.tagId}</td>
							<td>${eltjobtag.tagName}</td>
							<td>${eltjobtag.active ? 'Yes':'No'}</td>
							<td>
								<button class="btn btn-primary btn-sm tablebuttons jobTagById" value="${eltjobtag.tagId}" data-tagid="${eltjobtag.tagId}" title="<spring:message code="anvizent.package.label.edit"/>" >
										<i class="fa fa-pencil" aria-hidden="true"></i>
								</button>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	
	<div class="row form-group createTagsInfoDiv">
		<input type="hidden" id="selectedJobId">
			<div class="panel panel-default">
				<div class="panel-heading"> <spring:message code="anvizent.package.label.tagDetails"/></div>
					<div class="panel-body">
							<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.jobTagName"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="tagName" name="tagName" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
							</div>
								
								
							<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.globalConfigValues"/>
									</div>
									<div class='col-sm-5'>
										<select class="form-control" name="globalValues" id="globalValues">
												<option value='0'>
													<spring:message code="anvizent.package.label.selectGlobalValue"/>
												</option>
											<c:forEach items="${eltConfigTags}" var="eltTags">
												<option value="${eltTags.tagId}">
													${eltTags.tagName}
												</option>
											</c:forEach>
										</select>
									</div>
							</div>
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.active"/>
									</div>
									<div class='col-sm-5'>
										<label class="radio-inline"><input type="radio" name="active" value="true"><spring:message code="anvizent.package.button.yes"/></label> 
										<label class="radio-inline"><input type="radio" name="active" value="false"><spring:message code="anvizent.package.button.no"/></label>
									</div>
							</div>
								
							
							
							<div>
								<div class='col-sm-9'>
									<button type="button" class="btn btn-primary btn-sm saveKeyPairConfig" id = "saveTagInfo">
											<spring:message code="anvizent.package.button.save"/>
									</button>
									<button id="tagsBack" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Back"/></button>
								</div>
							</div>
						</div>
						
					</div>
	</div>
	 		
		<div class="row form-group tblJobMappingConfig" style="dispaly:none">
		<input type="hidden" id="selectedTblJobId">
					<div class='row form-group'>
					<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success updateSeq"><spring:message code="anvizent.package.label.updateSeq"/></a>
						<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success refreshBtn"><span class="glyphicon glyphicon-repeat"></span></a>
						<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success addJobMappingBtn"><spring:message code="anvizent.package.label.create"/></a>
					</div>
					<div class="table-responsive">
						<table class="table table-striped table-bordered tablebg"
							id="configJobMappingTagsTbl">
							<thead>
								<tr>
									<th><spring:message code="anvizent.package.label.Id"/></th>
									<th><spring:message code="anvizent.package.label.mappingID"/></th>
									<th><spring:message code="anvizent.package.label.jobName"/></th>
									<th><spring:message code="anvizent.package.label.configTagName"/></th>
									<th><spring:message code="anvizent.package.label.valueTagName"/></th>
									<th><spring:message code="anvizent.package.label.statsTagName"/></th>
									<th><spring:message code="anvizent.package.label.derivedComponent"/></th>
									<th><spring:message code="anvizent.package.label.active"/></th>
									<th><spring:message code="anvizent.package.label.Edit"/></th>
								</tr>
							</thead>
							<tbody>
							
							</tbody>
						</table>
					</div>
			</div>
			
			
	<div class="modal fade" role="dialog" id="jobDataMaippingEditPopUp" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog modal-lg">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.configInfo"/></h4>
			      </div>
			      <div class="modal-body"> 
			      <input type="hidden" id="selectedJobMappingId">
			       <input type="hidden" id="seqRowCount">
			        <input type="hidden" id="indexId">
			       
				 			<div class="row form-group jobDataMappingDiv">
				 			
				 			<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.jobName"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="jobName" name="jobName" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
							</div>
				 			
								<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.configTagName"/>
									</div>
									<div class='col-sm-5'>
										<select class="form-control" name="configProp" id="configProp">
											<option value='0'>
													<spring:message code="anvizent.package.label.selectTag"/>
											</option>
											<c:forEach items="${eltConfigTags}" var="eltTags">
												<option value="${eltTags.tagId}">
													${eltTags.tagName}
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.valueTagName"/>
									</div>
									<div class='col-sm-5'>
										<select class="form-control" name="valuesProp" id="valuesProp">
											<option value='0'>
													<spring:message code="anvizent.package.label.selectTag"/>
											</option>
											<c:forEach items="${eltConfigTags}" var="eltTags">
												<option value="${eltTags.tagId}">
													${eltTags.tagName}
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.statsTagName"/>
									</div>
									<div class='col-sm-5'>
										<select class="form-control" name="statsProp" id="statsProp">
											<option value='0'>
												<spring:message code="anvizent.package.label.selectTag"/>
											</option>
											<c:forEach items="${eltConfigTags}" var="eltTags">
												<option value="${eltTags.tagId}">
													${eltTags.tagName}
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class='row form-group addDerivedCompDiv'>
									<div class='col-sm-2 derived'>
										<spring:message code="anvizent.package.label.derivedComponent"/>
										<a href='#' class='btn btn-primary btn-sm' id='addDerivedCompDiv'> 
																	<span class='glyphicon glyphicon-plus'></span>
										</a>
									</div>
									
									<div class='col-sm-5 derivedCloneDiv'>
										<table class="table table-striped table-bordered tablebg" id="derivedCompTblTags">
												<tbody>
													
												</tbody>
												<tfoot>
													<tr class="clonedDerivedTr hidden">
														<td>
															<select class="form-control derivedComponent">
																<option value='0'>
																	<spring:message code="anvizent.package.label.selectTag"/>
																</option>
																<c:forEach items="${eltConfigTags}" var="eltTags">
																	<option value="${eltTags.tagId}">
																		${eltTags.tagName}
																	</option>
																</c:forEach>
															</select>
														</td>
														<td>
															<a href='#' class='btn btn-primary btn-sm addJobDerivedCompDiv' id='addStgKey'> 
																<span class='glyphicon glyphicon-plus'></span>
															</a>
															<a href='#' class='btn btn-primary btn-sm deleteJobDerivedCompDiv'>
																<span class='glyphicon glyphicon-trash'></span>
															</a>
														</td>
													</tr>
												</tfoot>
										</table>
									</div>
								</div>
								
								<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.active"/>
									</div>
									<div class='col-sm-5'>
										<label class="radio-inline"><input type="radio" name="activeStatus" class="active" value="true"><spring:message code="anvizent.package.button.yes"/></label> 
										<label class="radio-inline"><input type="radio" name="activeStatus" class="active" value="false"><spring:message code="anvizent.package.button.no"/></label>
									</div>
							</div>
								
							</div>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-primary btn-sm" id="saveJobMappingData"><spring:message code="anvizent.package.button.save"/></button>
			        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "anvizent.package.button.close"/></button>
			      </div>
			    </div>
			  </div>
	</div>	
		
</div>
</div>