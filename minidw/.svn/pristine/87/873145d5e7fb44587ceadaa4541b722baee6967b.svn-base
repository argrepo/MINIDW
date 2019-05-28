<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">      
      	<ol class="breadcrumb">	
		</ol>
    <%-- UI Changes --%> 
     <jsp:include page="admin_error.jsp"></jsp:include>
     <input type="hidden" id="userID" value="${principal.userId}">
	    <div class="row form-group">
	    	<h4 class="alignText"><spring:message code="anvizent.package.label.errorlog"/></h4>
	    </div>
		<div class='col-sm-12'>
	    
		<div id="successOrErrorMessage"></div>
			
    	  <div class="panel panel-default">
			  <div class="panel-heading">
			    <h3 class="panel-title"><spring:message code="anvizent.package.label.searchErrorLog"/></h3>
			  </div>
			  <div class="panel-body">
		      		<div class='row form-group'>
							<div class='col-sm-1'>
								<spring:message code="anvizent.package.label.errorCode"/>:
							</div>
							<div class='col-sm-3'>
								<input type="text" id="error_code" class="form-control">
							</div> 
							
							<div class='col-sm-1'>
								<spring:message code="anvizent.package.label.receivedParameters"/>:
							</div>
							<div class='col-sm-3'>
								<input type="text" id="request_param" class="form-control" value="">
							</div>
							
							<div class='col-sm-1'>
								<spring:message code="anvizent.package.label.clientDetails"/>:
							</div>
							<div class='col-sm-3'>
								<input type="text" id="client_Details" class="form-control" value="">
							</div>
							
					</div>
					<div class="row form-group">
						<div class='col-sm-6'>															
							<div id="searchCriteriaValidation"></div>
						</div>
					</div>
					<div class="row form-group">
							<div class='col-sm-2'>
								<button class="btn btn-sm btn-primary clientErrorLogs" id="searchErrorLog"><spring:message code="anvizent.package.label.search"/></button>
								<a href="<c:url value="/admin/errorLogs"/>" class="btn btn-sm btn-primary startLoader"><spring:message code="anvizent.package.label.clear"/></a>
							</div>
					</div>
				</div>
			</div>
			
			
			
			
			<div class="row form-group">
			<table  class="table table-striped table-bordered tablebg" id = "errorLogsTable" style ="table-layout: fixed;word-wrap: break-word;">
				<thead>
					<tr>
					    <th class="col-md-1"><spring:message code="anvizent.package.label.sNo"/></th>  
						<th class="col-md-3"><spring:message code="anvizent.package.label.errorCode"/></th>  
						<th class="col-md-3"><spring:message code="anvizent.package.label.errorDatetime"/></th>
						<th class="col-md-3"><spring:message code="anvizent.package.label.receivedParameters"/></th>
						<th class="col-md-2"><spring:message code="anvizent.package.label.clientDetails"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${errorLogs}" var="errorLog" varStatus="increment">
						<tr>
							<td ><c:out value="${increment.index+1}"/></td>
							<td><button class="getErrorBody btn btn-primary btn-sm tablebuttons" data-errorId="${errorLog.id}" style="white-space:normal;width:200px;cursor:auto;"><span style="cursor:pointer;">${errorLog.errorCode}</span></button></td>
	                       	<td><c:out value="${errorLog.errorDatetime}" escapeXml="true"/> </td>
	                       	<td><c:out value="${errorLog.receivedParameters}" escapeXml="true"/> </td>	                            	
	                       	<td><c:out value="${errorLog.clientDetails}" escapeXml="true"/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>			
		</div>
		
				
	<div class="modal fade" tabindex="-1" role="dialog" id="viewErrorBodyPopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 60%;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.errorBody"/></h4>
		      </div>
		      <div class="modal-body" style="max-height: 400px; overflow-y: auto;">
					<div class='error-body' style='overflow-y: auto;max-height: 300px;'>
					
					</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>												      
			      
</div>
 