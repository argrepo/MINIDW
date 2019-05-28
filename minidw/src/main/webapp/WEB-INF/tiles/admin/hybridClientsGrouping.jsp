<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.HybridClientsGrouping"/></h4>
 	</div>
 	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	<c:url value="/admin/hybridClientsGrouping/edit" var="editUrl"/>	
	<div class="col-sm-12">
		<form:form modelAttribute="hybridClientsGrouping" action="${editUrl}">
			<c:choose>
			  	<c:when test="${hybridClientsGrouping.pageMode == 'list'}">
			  	<div class='row form-group'>
			  		<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success" href="<c:url value="/admin/hybridClientsGrouping/add"/>"><spring:message code="anvizent.package.label.Add"/> </a>
			  	</div>
					<div class='row form-group'>
						<div class="table-responsive">
							<table class="table table-striped table-bordered tablebg " id="hybridClientsGroupingTable">
								<thead>
									<tr>
										<th><spring:message code="anvizent.package.label.sNo"/></th>
										<th><spring:message code="anvizent.package.label.Name"/></th>
										<th><spring:message code="anvizent.package.label.AccessKey"/></th>
										<th><spring:message code="anvizent.package.label.Clients"/></th>
										<th><spring:message code="anvizent.package.label.Active"/></th>
										<th><spring:message code="anvizent.package.label.edit"/></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${hybridClientMappingInfo}" var="hybridInfo" varStatus="index">
										<tr><td>${index.index+1}</td>
											<td><c:out value="${hybridInfo.name}" /></td>
											<td><c:out value="${hybridInfo.accessKey}" /></td>
											<td>
												<ul type="square">
													<c:forEach items="${hybridInfo.clientIds}" var="clientIds">
														<li>
															<c:out value="${clientIds.id}"/>-
															<c:out value="${clientIds.clientName}"/>
														</li>
													</c:forEach>
												</ul>
											</td>
											<td><c:out value="${hybridInfo.active ? 'YES':'NO'}" /></td>
											
											<td>
												<button class="btn btn-primary btn-sm tablebuttons" name="id" value="${hybridInfo.id}" title="<spring:message code="anvizent.package.label.edit"/>" >
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
				
				<c:when test="${hybridClientsGrouping.pageMode == 'edit' || hybridClientsGrouping.pageMode == 'add' }">
						<div class="panel panel-default">
							<div class="panel-heading">Hybrid Clients Grouping<spring:message code="anvizent.package.label.HybridClientsGrouping"/></div>
							<div class="panel-body">
							<input type="hidden" id="threadCount" value="${hybridClientsGrouping.packageThreadCount}">
							<input type="hidden" id="interval" value="${hybridClientsGrouping.intervalTime}">
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.Name"/> :</label>
									<div class='col-sm-6'>
										<form:input path="name" class="form-control" data-maxlength="45"/>
									</div>
								</div>
							
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.Description"/> :</label>
									<div class='col-sm-6'>
										<form:input path="description" class="form-control" data-maxlength="255"/>
									</div>
								</div>
								
								<div class="row form-group">			 
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.ClientId"/> : </label>					
								   	<div class="col-sm-6">
								    	<form:select path="clientId" cssClass="clientIds form-control" style="white-space:pre-wrap;">
								    		<form:options items="${allClients}"/>
								    	</form:select>
								    </div>			   
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.activeStatus"/> :</label>
									<div class='col-sm-6'>
									 	<div class="activeStatus">
									 		 <label class="radio-inline">
												<form:radiobutton path="active" value="true"/><spring:message code="anvizent.package.button.yes"/>
											 </label>	
											 <label class="radio-inline">
										    	<form:radiobutton path="active" value="false"/><spring:message code="anvizent.package.button.no"/> 
										    </label>
									 	</div>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"> <spring:message code="anvizent.package.label.ThreadCount"/> :</label>
										<div class="col-sm-6">
											<form:select path="packageThreadCount" cssClass="form-control threadCount">
											</form:select>
										</div>
						    	</div>
								
								
								<div class="row form-group">			 
									<label class="control-label col-sm-2">  <spring:message code="anvizent.package.label.PackageRetrievalInterval"/> :</label>					
								   	<div class="col-sm-3">
								    	<form:select path="intervalType" cssClass="form-control packageRetrievalInterval">
								    			<%-- <form:option value="seconds" selected="selected"><spring:message code="anvizent.package.label.Seconds"/></form:option> --%>
								    			<form:option  value="minute"><spring:message code="anvizent.package.label.Minute"/></form:option>
							    				<%-- <form:option  value="hour"><spring:message code="anvizent.package.label.Hour"/></form:option > --%>
						    			</form:select>
						    		</div>
						    			<div class='col-sm-2'>
											<form:select path="intervalTime" cssClass="form-control packageIntervalTime">
											</form:select>
								    	</div>	
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-3"></label>
									<div class="col-sm-6">
										<c:url value="/admin/hybridClientsGrouping/add" var="addUrl"/>
										<input type="hidden" value="${addUrl}" id="addUrl">
									</div>
								</div>
								<div class="row form-group">
									<div class="col-sm-6">								
										<c:choose>
											<c:when test="${hybridClientsGrouping.pageMode == 'edit'}">
												<button id="updateHybridClientGrouping" type="button" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.update"/></button>
											 <form:hidden path="id" class="form-control"/>
											  <form:hidden path="accessKey" class="form-control"/>
											</c:when>
											<c:when test="${hybridClientsGrouping.pageMode == 'add'}">
												<button id="addHybridClientGrouping" type="button" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.save"/></button>
											</c:when>
										</c:choose>
										<a href="<c:url value="/admin/hybridClientsGrouping"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
									</div>
								</div>
							</div>
						</div>
				</c:when>
			</c:choose> 
		</form:form>	
	</div>	
</div>