<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<style>
#eltStgConfigTable > .form-control {
	border: 0px solid #ccc;
}
#eltStgConfigTable td {
	padding: 0px 0px !important;
}
#eltStgConfigTable.table>tbody>tr>td {
	border-top : 0px solid #ddd;
}
</style>
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.configTags"/></h4>
 	</div>

 	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	<div class="col-md-12 message-class"></div>
	<div class="col-sm-12">
				
	<div id="configTagsTable">
		<div class='row form-group'>
			<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success addConfigKeysBtn"><spring:message code="anvizent.package.label.create"/></a>
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
							<th><spring:message code="anvizent.package.label.clone"/></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${eltConfigTags}" var="eltConfig">
						<tr>
							<td>${eltConfig.tagId}</td>
							<td>${eltConfig.tagName}</td>
							<td>${eltConfig.active ? 'Yes' : 'No'}</td>
							<td>
								<button class="btn btn-primary btn-sm tablebuttons tagsKeyPairs" value="${eltConfig.tagId}" data-tagid="${eltConfig.tagId}" title="<spring:message code="anvizent.package.label.edit"/>" >
										<i class="fa fa-pencil" aria-hidden="true"></i>
								</button>
							</td>
							<td>
								<button class="btn btn-primary btn-sm cloneTag" value="${eltConfig.tagId}" data-tagid="${eltConfig.tagId}" data-tagname="${eltConfig.tagName}" data-active="${eltConfig.active}" title="clone" >
										<spring:message code="anvizent.package.label.clone"/>
								</button>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	
	<div class="col-sm-12 createTagsInfoDiv">
			<div class="panel panel-default">
				<div class="panel-heading"> <spring:message code="anvizent.package.label.tagDetails"/></div>
					<div class="panel-body">
							<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.name"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="tagName" 
										placeholder="Tag name can be used while creating jobs under job tag"
										 name="tagName" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
							</div>
								
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.active"/>
									</div>
									<div class='col-sm-5 activeMsg'>
										<label class="radio-inline"><input type="radio" name="active" value="true"><spring:message code="anvizent.package.label.yes"/></label> 
										<label class="radio-inline"><input type="radio" name="active" value="false"><spring:message code="anvizent.package.label.no"/></label>
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
			
		<div class="keyValueConfigPairsDiv" >
		<input type="hidden" id="selectedTagId">
			<div class='row form-group'>
				<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success bulkData"><spring:message code="anvizent.package.label.uploadFile"/></a>
			</div>
						<div class="table-responsive scrollpanel" style="overflow-y:overlay; max-height: 400px;">
							<table class="table table-striped table-bordered tablebg" id="eltStgConfigTable" style="table-layout: fixed;">
								<thead>
									<tr id="fixedHeader">
										<!-- <th>Id</th> -->
										<th><spring:message code="anvizent.package.label.key"/></th>
										<th><spring:message code="anvizent.package.label.value"/></th>
										<th><spring:message code="anvizent.package.label.addOrDelete"/>
										 	<a href="#" class="btn btn-primary btn-sm" id="addStgKeyDiv"> <span class="glyphicon glyphicon-plus"></span></a>
										 	<a href="#" class="btn btn-primary btn-sm" id="deleteStgKeyDiv"> <span class="glyphicon glyphicon-trash"></span></a>
										</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
								<tfoot>
								<tr class='cloneCopystgRow hidden'>
										<td>
											<input type='text' class='stg_key form-control' placeholder="property key">
											<input type='hidden' class='keyId' name='id'>
										</td>
										<td>
											<input type='text' class='stg_value form-control' placeholder="property value">
										</td>
										<td class="move-cursor">
											<button class='btn btn-primary btn-sm addKeyPair' id='addStgKey'> 
												<span class='glyphicon glyphicon-plus'></span>
											</button>
											<button class='btn btn-primary btn-sm deleteKeyPair'>
												<span class='glyphicon glyphicon-trash'></span>
											</button>
											<button class='btn btn-primary btn-sm moveUpBtn'>
												<span class='glyphicon glyphicon-arrow-up'></span>
											</button>
											<button class='btn btn-primary btn-sm moveDownBtn'>
												<span class='glyphicon glyphicon-arrow-down'></span>
											</button>
										</td>
									</tr>
								</tfoot>
							</table>
						</div>
						<div class='col-sm-5'>
							<button type="button" class="btn btn-primary btn-sm saveStgPairConfig" id="saveConfigBtn" >
								<spring:message code="anvizent.package.button.save"/>
							</button>
						</div>
	 		</div>
	</div>	
	
	
	<div class="modal fade" tabindex="-1" role="dialog" id="bulkDataPopUp" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog modal-lg">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.uploadFile"/></h4>
			      </div>
			      <div class="modal-body"> 
				      <form:form method="POST"  id="fileUploadForm_direct" enctype="multipart/form-data">
				 			<div class="row form-group">
								<div class="col-sm-6">			    			
			    					<input type="file" class="configFile" name="file">
			   					 </div>
							</div>
					 </form:form>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-primary btn-sm" id="uploadBulkData"><spring:message code = "anvizent.package.button.upload"/></button>
			        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "anvizent.package.button.close"/></button>
			      </div>
			    </div>
			  </div>
	</div>	
	
	
	<div class="modal fade" tabindex="-1" role="dialog" id="CloneTagPopUp" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog modal-lg">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.clone"/></h4>
			      </div>
			      <div class="modal-body"> 
					<div class='row form-group'>
									<div class='col-sm-2'>
										  <spring:message code="anvizent.package.label.name"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="cloneTagName" name="tagName" class="form-control" data-minlength="1" data-maxlength="45"> 
										<input type="hidden" id="cloneTagId">
										<input type="hidden" id="activeTag">
									</div>
					</div>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-primary btn-sm" id="saveCloneTagData"><spring:message code="anvizent.package.label.Save"/></button>
			        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "anvizent.package.button.close"/></button>
			      </div>
			    </div>
			  </div>
	</div>				
</div>