<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.kPIs"/></h4>
 	</div>
 	
 	<input type="hidden" id="userId" value="${principal.userId}">
		
	<jsp:include page="admin_error.jsp"></jsp:include>
	<div class="col-md-12 message-class"></div>
	
	<div style="padding:0px 15px;">
	<div class="row form-group" style="padding:5px;border-radius:4px;">
		<button class="btn btn-primary btn-sm createpackage" id="createKpi" style="float:right;"><spring:message code="anvizent.package.label.create"/></button>			
	</div>
	</div>

	<div class='row form-group'>
		<div class="table-responsive">
			<table class="table table-striped table-bordered tablebg " id="existingKpisTable">
				<thead>
					<tr>
						<th><spring:message code="anvizent.package.label.kpiName"/></th>
						<th><spring:message code="anvizent.package.label.kpiDescription"/></th>
						<th><spring:message code="anvizent.package.label.isActive"/></th>
						<th><spring:message code="anvizent.package.label.edit"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${kpis}" var="kpi">
						<tr>
							<td><c:out value="${kpi.kpiName}" /></td>
							<td><c:out value="${not empty kpi.kpiDescription ? kpi.kpiDescription : '-'}" /></td>
							<td>${kpi.isActive == true ? 'Yes' : 'No'}</td>
							<td>
								<button class="btn btn-primary btn-sm tablebuttons editKpi" data-kpiid="<c:out value="${kpi.kpiId}" />" title="<spring:message code="anvizent.package.label.edit"/>" >
									<i class="fa fa-pencil" aria-hidden="true"></i>
								</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
    </div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="createNewKpiPanel" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
				        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.createKpi"/></h4>
				      </div>
				      <div class="modal-body">
					        <form method="POST" action="<c:url value="/admin/kpi/new"/>" id="createNewKpiForm">
								<div class="row form-group" >			 
									<div class="col-sm-12">
										<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.kpiName"/> : </label>					
									   	<div class="col-sm-6">
									    	<input type="text" name="kpiName" class="kpiName form-control" data-minlength="1" data-maxlength="255" placeholder="<spring:message code="anvizent.package.label.kpiName"/>"/>
									    </div>			   
									</div>		
								</div>	
								
								<div class="row form-group" >			 
									<div class="col-sm-12">
										<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.kpiDescription"/> : </label>					
									   	<div class="col-sm-6">
									    	<input type="text" name="kpiDescription" class="kpiDescription form-control" data-minlength="1" data-maxlength="500" placeholder="<spring:message code="anvizent.package.label.kpiDescription"/>"/>
									    </div>			   
									</div>		
								</div>
							 <input type="hidden" name="<c:out value="${_csrf.parameterName}" />" value="<c:out value="${_csrf.token}" />"/>					
							</form>
				      </div>
				      <div class="modal-footer">
				      		<button type="submit" class="btn btn-primary btn-sm" id="createNewKpi" name='createNewKpi'><spring:message code="anvizent.package.label.createNewKpi"/></button>
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.cancel"/></button>
				      </div>
			    </div> 
		  </div> 
	</div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="editKpiPanel" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
				        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.editKpi"/></h4>
				      </div>
				      <div class="modal-body">
					        <form method="POST" action="<c:url value="/admin/kpi/edit"/>" id="editKpiForm">
								<div class="row form-group" >			 
									<div class="col-sm-12">
										<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.kpiName"/> : </label>					
									   	<div class="col-sm-6">
									    	<input type="text" name="kpiName" class="editKpiName form-control" data-minlength="1" data-maxlength="255" placeholder="<spring:message code="anvizent.package.label.kpiName"/>"/>
									    	<input type="hidden" name="kpiId" class="kpiId">
									    </div>			   
									</div>		
								</div>
								
								<div class="row form-group" >			 
									<div class="col-sm-12">
										<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.kpiDescription"/> : </label>					
									   	<div class="col-sm-6">
									    	<input type="text" name="kpiDescription" class="editKpiDescription form-control" data-minlength="1" data-maxlength="500" placeholder="<spring:message code="anvizent.package.label.kpiDescription"/>"/>
									    </div>			   
									</div>		
								</div>
								
								<div class="row form-group">			 
									<div class="col-sm-12">
										<label class="control-label col-sm-3">
									    	<spring:message code="anvizent.package.label.activeStatus"/>		    	
									    </label>
									   	<div class="col-sm-6">
										   	<label class="radio-inline control-label">
										    	<input type="radio" name="isActive" value="true"><spring:message code="anvizent.package.button.yes"/>		    	
										    </label>
										    <label class="radio-inline control-label">
										    	<input type="radio" name="isActive" value="false"><spring:message code="anvizent.package.button.no"/>  	
										    </label>
									    </div>			   
									</div>		
								</div>		
								 <input type="hidden" name="<c:out value="${_csrf.parameterName}" />" value="<c:out value="${_csrf.token}" />"/>												
							</form>	
				      </div>
				      <div class="modal-footer">
				      		<button type="submit" class="btn btn-primary btn-sm" id="updateKpi" name='updateKpi'><spring:message code="anvizent.package.label.updateKpi"/></button>
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.cancel"/></button>
				      </div>
			    </div> 
		  </div> 
	</div>
</div>