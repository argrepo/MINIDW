<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.ClientCurrencyMapping"/></h4>
 	</div>
 	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	<c:url value="/admin/currencyIntegration/clientCurrencyMapping" var="url"/>	
	<c:url value="/admin/currencyIntegration/clientCurrencyMapping/edit" var="editUrl"/>	
	<div class="col-sm-12">
		<form:form modelAttribute="clientCurrencyMapping" action="${editUrl}">
			<form:hidden path="pageMode"/>
			<c:choose>
			  	<c:when test="${clientCurrencyMapping.pageMode == 'list'}">
			   <c:if test="${clientCurrencyMapping1  == null ||  empty clientCurrencyMapping1 }">   
			  	<div class='row form-group'>
			  		<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success add" href="<c:url value="/admin/currencyIntegration/clientCurrencyMapping/add"/>"> <spring:message code="anvizent.package.label.Add"/> </a>
			  	</div>
			   </c:if>   
					<div class='row form-group'>
						<div class="table-responsive">
							<table class="table table-striped table-bordered tablebg " id="clientCurrencyMappingTable">
								<thead>
									<tr>
										<th><spring:message code="anvizent.package.label.sNo"/> </th>
										<th><spring:message code="anvizent.package.label.clientId"/></th>
										<th><spring:message code="anvizent.package.label.currencytype"/></th>
										<th><spring:message code="anvizent.package.label.dashboardcurrencycode"/></th>
										<th><spring:message code="anvizent.package.label.accountingcurrencycode"/></th>
										<th><spring:message code="anvizent.package.label.othercurrencycode"/></th>
										<th><spring:message code="anvizent.package.label.activeStatus"/></th>
										<th><spring:message code="anvizent.package.label.edit"/></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${clientCurrencyMapping1}" var="clientCurrencyMap" varStatus="index">
										<tr><td>${index.index+1}</td>
											<td><c:out value="${clientCurrencyMap.clientId}" /></td>
											<td>
												<c:out value="${clientCurrencyMap.currencyType == 'minidw' ? 'Default':clientCurrencyMap.currencyType}" />
											</td>
											<td><c:out value="${not empty clientCurrencyMap.currencyName ?clientCurrencyMap.currencyName:'-'}" /></td>
											<td><c:out value="${not empty clientCurrencyMap.basecurrencyCode ? clientCurrencyMap.basecurrencyCode:'-'}" /></td>
											<td><c:out value="${not empty clientCurrencyMap.accountingCurrencyCode?clientCurrencyMap.accountingCurrencyCode:'_'}" /></td>
											<td><c:out value="${clientCurrencyMap.isActive == true ? 'Yes' : 'No'}" /></td>
											<td>
												<button class="btn btn-primary btn-sm tablebuttons edit" name="id" value="${clientCurrencyMap.id}" title="<spring:message code="anvizent.package.label.edit"/>" >
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
				
				<c:when test="${clientCurrencyMapping.pageMode == 'add' ||  clientCurrencyMapping.pageMode == 'edit'}">
						<div class="panel panel-default">
							<div class="panel-heading"><spring:message code="anvizent.package.label.ClientMappingDetails"/></div>
							<div class="panel-body">
								<div class="row form-group">
								 		<label class="col-sm-2 control-label"><spring:message code="anvizent.package.label.clientId"/>:</label>
										<div class="col-sm-6">
										<c:choose>
											<c:when test="${clientCurrencyMapping.pageMode == 'edit'}">
											    <form:hidden path="id"/>
												<form:hidden path="clientId"/>
												<c:out value="${clientCurrencyMapping.clientId}" />
											</c:when>
											<c:otherwise>
												<form:select path="clientId"  cssClass="form-control">
													<spring:message code="anvizent.package.label.selectClient" var="selectClient" />
													<form:option value="0">${selectClient }</form:option>
													<form:options items="${allClients}" />
												</form:select>
											</c:otherwise>
										</c:choose>
										</div>
	 							</div>
							
								<div class="row form-group">
									<label class="col-sm-2 control-label"> <spring:message code="anvizent.package.label.currencytype"/> :</label>
									<div class='col-sm-6'>
										<div class="currencyType">
											<label class="radio-inline">
												<form:radiobutton path="currencyType" value="minidw"/><spring:message code="anvizent.package.label.default"/> 
											</label>
											<label class="radio-inline">
												<form:radiobutton path="currencyType" value="Client Specific"/> <spring:message code="anvizent.package.label.ClientSpecific"/> 
											</label>
										</div>
									</div>
								</div>
								<div class="row form-group" >
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.dashboardcurrencycode"/> :</label>
									<div class='col-sm-6'>
												<form:select path="currencyName"  cssClass="form-control">
													<form:option value=''><spring:message code="anvizent.package.label.SelectCurrency"/></form:option>
													<form:options items="${currencyList}" />
												</form:select>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.accountingcurrencycode"/> :</label>
									<div class='col-sm-6'>
												<form:select path="basecurrencyCode"  cssClass="form-control">
													<form:option value=''><spring:message code="anvizent.package.label.SelectCurrency"/></form:option>
													<form:options items="${currencyList}" />
												</form:select>
									</div>
								</div>
								
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.othercurrencycode"/> :</label>
									<div class='col-sm-6'>
											<form:select path="accountingCurrencyCode"  cssClass="form-control">
													<form:option value=''><spring:message code="anvizent.package.label.SelectCurrency"/></form:option>
													<form:options items="${currencyList}" />
											</form:select>
									</div>
								</div>
								
								<div class="row form-group">
									<div class='col-sm-8 messageForCurrency'></div>
								</div>
								<div class="row form-group">
									<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.activeStatus"/> :</label>
									<div class='col-sm-6'>
									 	<div class="activeStatus">
									 		 <label class="radio-inline">
												<form:radiobutton path="isActive" value="true"/><spring:message code="anvizent.package.button.yes"/>
											 </label>	
											 <label class="radio-inline">
										    	<form:radiobutton path="isActive" value="false"/><spring:message code="anvizent.package.button.no"/> 
										    </label>
									 	</div>
									</div>
								</div>
								
								
								<div class="row form-group">
									<label class="control-label col-sm-3"></label>
									<div class="col-sm-6">
										<c:url value="/admin/currencyIntegration/clientCurrencyMapping/add" var="addUrl"/>
										<input type="hidden" value="${addUrl}" id="addUrl">
									</div>
								</div>
								<div class="row form-group">
									<div class="col-sm-6">								
										<c:choose>
											<c:when test="${clientCurrencyMapping.pageMode == 'edit'}">
												<button id="updateClientCurrency" type="button" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.update"/></button>
											 <form:hidden path="id" cssClass="form-control"/>
											</c:when>
											<c:when test="${clientCurrencyMapping.pageMode == 'add'}">
												<button id="addclientCurrency" type="button" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.save"/></button>
											</c:when>
										</c:choose>
										<a href="<c:url value="/admin/currencyIntegration/clientCurrencyMapping"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
									</div>
								</div>
							</div>
						</div>
				</c:when>
			</c:choose> 
		</form:form>	
	</div>	
</div>