<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<spring:message code="anvizent.package.label.yes" var="yesVar"/>
<spring:message code="anvizent.package.label.no" var="noVar"/>
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.verticals"/></h4>
 	</div>

	<jsp:include page="admin_error.jsp"></jsp:include>
	<div class="col-md-12 message-class"></div>
 	
 	<input type="hidden" id="userId" value="${principal.userId}">
	<div class="" style="width:100%;padding:0px 15px;">
		<div class="row form-group" style="padding:5px;border-radius:4px;">
			<button class="btn btn-success btn-sm" id="createVertical" style="float:right;"><spring:message code="anvizent.package.label.create"/></button>			
		</div>
	</div>
	<div class='row form-group'>
		<div class="table-responsive">
			<table class="table table-striped table-bordered tablebg " id="existingVerticalsTable">
				<thead>
					<tr>
						<th><spring:message code="anvizent.package.label.verticalName"/></th>
						<th><spring:message code="anvizent.package.label.verticalDescription"/></th>
						<th><spring:message code="anvizent.package.label.isActive"/></th>
						<th><spring:message code="anvizent.package.label.edit"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${verticals}" var="vertical">
						<tr>
							<td><c:out value="${vertical.name}"/></td>
							<td><pre class="preTag"><c:out value="${vertical.description}"/></pre></td>
							<td><c:out value="${vertical.isActive == true ? yesVar : noVar}"/></td>
							<td>
								<button class="btn btn-primary btn-sm tablebuttons editVertical" data-verticalId="${vertical.id}" title="<spring:message code="anvizent.package.label.edit"/>" >
									<i class="fa fa-pencil" aria-hidden="true"></i>
								</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
    </div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="createNewVerticalPanel" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
				        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.createVertical"/></h4>
				      </div>
				      <div class="modal-body">
					        <form method="POST" action="<c:url value="/admin/vertical/new"/>" id="createNewVerticalForm">
								<div class="row form-group" >			 
									<div class="col-sm-12">
										<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.verticalName"/> : </label>					
									   	<div class="col-sm-6">
									    	<input type="text" name="name" class="verticalName form-control" data-minlength="1" data-maxlength="45" onkeypress="return event.keyCode!=13" placeholder="<spring:message code="anvizent.package.label.verticalName"/>"/>
									    </div>			   
									</div>		
								</div>
								
								<div class="row form-group">			 
									<div class="col-sm-12">
										<label class="control-label col-sm-3">
									    	<spring:message code="anvizent.package.label.verticalDescription"/>	:	    	
									    </label>
									   	<div class="col-sm-6">
									    	<textarea name="description" class="verticalDescription form-control" data-minlength="1" data-maxlength="255" placeholder="<spring:message code="anvizent.package.label.verticalDescription"/>"> </textarea>	
									    </div>			   
									</div>		
								</div>	
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>							
							</form>
				      </div>
				      <div class="modal-footer">
				      		<button type="submit" class="btn btn-primary btn-sm" id="createNewVertical" name='createNewVertical'><spring:message code="anvizent.package.label.createNewVertical"/></button>
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.cancel"/></button>
				      </div>
			    </div> 
		  </div> 
	</div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="editVerticalPanel" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
				        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.editVertical"/></h4>
				      </div>
				      <div class="modal-body">
					        <form method="POST" action="<c:url value="/admin/vertical/edit"/>" id="editVerticalForm">
								<div class="row form-group" >			 
									<div class="col-sm-12">
										<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.verticalName"/> : </label>					
									   	<div class="col-sm-6">
									    	<input type="text" name="name" class="editVerticalName form-control" data-minlength="1" data-maxlength="45" placeholder="<spring:message code="anvizent.package.label.verticalName"/>"/>
									    	<input type="hidden" name="id" class="verticalId">
									    </div>			   
									</div>		
								</div>
								
								<div class="row form-group">			 
									<div class="col-sm-12">
										<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.verticalDescription"/> :</label>
									   	<div class="col-sm-6">
									    	<textarea name="description" class="editVerticalDescription form-control" data-minlength="1" data-maxlength="255" placeholder="<spring:message code="anvizent.package.label.verticalDescription"/>"></textarea> 	
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
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>						
							</form>	
				      </div>
				      <div class="modal-footer">
				      		<button type="submit" class="btn btn-primary btn-sm" id="updateVertical" name='updateVertical'><spring:message code="anvizent.package.label.updateVertical"/></button>
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.cancel"/></button>
				      </div>
			    </div> 
		  </div> 
	</div>

	<div class="modal fade" tabindex="-1" role="dialog" id="deleteVerticalAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
				        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.deleteVertical"/></h4>
				      </div>
				      <div class="modal-body">
					        <p>
					        	<spring:message code="anvizent.package.message.areYouSureYouWantToDeleteThisVertical"/>
				        	</p>
				      </div>
				      <div class="modal-footer">
				      	<form method="POST" action="<c:url value="/admin/vertical/delete"/>">
					        <button type="submit" name="id" value="" class="btn btn-primary startLoader" id="confirmDeleteVertical"><spring:message code="anvizent.package.button.yes"/></button>
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
						    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						</form>	   
				      </div>
			    </div> 
		  </div> 
	</div>
</div>