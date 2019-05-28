<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
     <div class='row form-group'>
	     <h4 class="alignText"><spring:message code="anvizent.admin.label.webserviceClientMapping"/></h4>
	 </div>
	 
	<jsp:include page="admin_error.jsp"></jsp:include>
	<input type="hidden" id="userID" value="${principal.userId}">

	   <div class="row form-group" >		 
			<label class="col-sm-2 control-label">Web Service :</label>
			<div class="col-sm-6">
				    <select class="form-control" id="webserviceName"  name="webserviceName">
				   		<option value="0"><spring:message code="anvizent.package.label.select"/></option>
					 	<c:forEach items="${modelObject.webserviceList}" var ="webserviceList">
					 		<option value="${webserviceList.key}">${webserviceList.key} - ${webserviceList.value}</option>
                     	</c:forEach>		
					</select>
			</div>		
		</div>
			<div class="form-group" style="max-height:450px;overflow-y:scroll;">
			<table class="table table-striped table-bordered tablebg table-hover" id="webserviceInfo">
				<thead>
				<tr>
					<th><input type="checkbox" id="selectall"/> <spring:message code="anvizent.package.label.selectAll"/></th>
					<th> <spring:message code="anvizent.package.label.clientId"/></th> 
					<th> <spring:message code="anvizent.package.label.clientName"/></th>
				</tr>
				</thead>
				  <tbody>
				    <c:forEach items="${modelObject.clientList}" var ="clientList">
				    <tr class="data-row">
				    	<td> <input value='${clientList.id}' type='checkbox' class='clientInfoByWebserviceId checkCase'> </td>
				    	<td class='clientId'>${clientList.id}</td>
		                <td class='clientName'>${clientList.clientName}</td> 
                	</tr>
                    </c:forEach>
				  </tbody>
			</table>
			
		</div>
		<div class="form-group">
			<div class='webserviceValidation'></div>
		</div>
		<div class="form-group">
		<button id="addClientsAndWebserviceId" type="Button" class="btn btn-primary btn-sm"><spring:message code="anvizent.admin.button.Submit"/></button>
		</div>
  	
</div>
 
