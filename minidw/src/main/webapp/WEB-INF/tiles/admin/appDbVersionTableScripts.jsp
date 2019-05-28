<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.AppDbVersionTableScripts"/></h4>
 	</div>
 	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	<c:url value="/admin/appDbVersionTableScripts/edit" var="editUrl"/>	
	<div class="col-sm-12">
		<form:form modelAttribute="appDBVersionTableScripts" action="${editUrl}">
		<form:hidden path="pageMode"/>
			<c:choose>
			  	<c:when test="${appDBVersionTableScripts.pageMode == 'list'}">
			  	<div class='row form-group'>
			  		<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success" href="<c:url value="/admin/appDbVersionTableScripts/add"/>"> Add </a>
			  	</div>
					<div class='row form-group'>
						<div class="table-responsive">
							<table class="table table-striped table-bordered tablebg " id="appDbVersionTableScriptTable">
								<thead>
									<tr>
										<th><spring:message code="anvizent.package.label.sNo"/></th>
										<th><spring:message code="anvizent.package.label.Id"/></th>
										<th><spring:message code="anvizent.package.label.versionNumber"/></th>
										<th><spring:message code="anvizent.package.label.AnvizentScript"/> </th>
										<th><spring:message code="anvizent.package.label.MinidwScript"/> </th>
										<th><spring:message code="anvizent.package.label.Default"/></th>
										<th><spring:message code="anvizent.package.label.edit"/></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${appDbVersionTableScriptsInfo}" var="appDbVersionList" varStatus="index">
										<tr class="${appDbVersionList.defaultScript?'active':''}"><td>${index.index+1}</td>
											<td><c:out value="${appDbVersionList.id}" /></td>
											<td><c:out value="${appDbVersionList.version}" /></td>
											<td>
											<button type="button" class="btn btn-primary btn-sm tablebuttons viewAppDBScript" value="${appDbVersionList.id}"><spring:message code="anvizent.package.label.View"/></button>
											</td>
											
											<td>
											<button type="button" class="btn btn-primary btn-sm tablebuttons viewMinidwDBScript" value="${appDbVersionList.id}"><spring:message code="anvizent.package.label.View"/></button>
											</td>
											<td><c:out value="${appDbVersionList.defaultScript? 'YES' : 'NO'}"/></td>
											<td>
												<button class="btn btn-primary btn-sm tablebuttons editVal" name="id" value="${appDbVersionList.id}" title="<spring:message code="anvizent.package.label.edit"/>" >
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
				
				<c:when test="${appDBVersionTableScripts.pageMode == 'edit' || appDBVersionTableScripts.pageMode == 'add' }">
						<div class="panel panel-default">
							<div class="panel-heading"><spring:message code="anvizent.package.label.AppDBVersionTableScriptsDetails"/></div>
							<form:hidden path="id"/>
							<div class="panel-body">
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.versionNumber"/>:</label>
									<div class='col-sm-6'>
										<form:input path="version" class="form-control" data-maxlength="255"/>
									</div>
								</div>
							
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.AppDBScript"/>:</label>
									<div class='col-sm-6'>
										<form:textarea path="appDbScript" class="form-control" rows="6"/>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.MinidwScript"/>:</label>
									<div class='col-sm-6'>
										<form:textarea path="minidwScript" class="form-control" rows="6"/>
									</div>
								</div>
								
								<div class="row form-group">
							 		<label class="col-sm-2 control-label"><spring:message code="anvizent.package.label.useDefault"/> :</label>
									<div class="col-sm-6">
										<form:checkbox path="defaultScript"/>
									</div>
							 	</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-3"></label>
									<div class="col-sm-6">
										<c:url value="/admin/appDbVersionTableScripts/add" var="addUrl"/>
										<input type="hidden" value="${addUrl}" id="addUrl">
									</div>
								</div>
								
								<div class="row form-group">
									<div class="col-sm-6">								
										<c:choose>
											<c:when test="${appDBVersionTableScripts.pageMode == 'edit'}">
												<button id="updateAppDBVersion" type="button" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.update"/></button>
											 <form:hidden path="id" class="form-control"/>
											</c:when>
											<c:when test="${appDBVersionTableScripts.pageMode == 'add'}">
												<button id="addAppDBVersion" type="button" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.save"/></button>
											</c:when>
										</c:choose>
										<a href="<c:url value="/admin/appDbVersionTableScripts"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
									</div>
								</div>
							</div>
						</div>
				</c:when>
			</c:choose> 
		</form:form>
		
			<div class="modal fade" tabindex="-1" role="dialog" id="viewQueryPopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 60%;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title heading"><spring:message code="anvizent.package.message.Script"/></h4>
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
	</div>	
	
		
</div>