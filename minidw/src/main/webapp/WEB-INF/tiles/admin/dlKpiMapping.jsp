<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
     <div class='row form-group'>
	     <h4 class="alignText"><spring:message code="anvizent.admin.label.dlKpiMapping"/></h4>
	 </div>
	 
    <div class="col-md-10">
    <div class="dummydiv"></div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="${principal.userId}">
    </div>

<div class="col-md-8">	
	   <div class="row form-group" >		 
			<label class="col-sm-2 control-label"><spring:message code="anvizent.admin.label.DLName"/>:</label>
			<div class="col-sm-6">
				<select class="form-control" id="dlName" name="dlName">
				    <option value="0"><spring:message code="anvizent.package.label.selectDL"/></option>
				    <c:forEach items="${modelObject.dLList}" var ="dLList">
		             <option value="${dLList.dL_id}"> ${dLList.dL_name} </option>
                    </c:forEach>
				</select>
			</div>			
		</div>
			<div class="form-group" style="max-height:450px;overflow-y:scroll;">
			<table class="table table-striped table-bordered tablebg table-hover" id="kpiInfo">
				<thead>
				<tr>
					<th><input type="checkbox" id="selectall"/> <spring:message code="anvizent.package.label.selectAll"/></th>
					<th><spring:message code="anvizent.package.label.KPIId"/></th> 
					<th><spring:message code="anvizent.package.label.KPIName"/></th>
				</tr>
				</thead>
				  <tbody>
				    <c:forEach items="${modelObject.KpiList}" var ="KpiList">
				    <tr class="data-row">
				    	<td> <input value='${KpiList.value}' type='checkbox' class='kpiInfoByDlId checkCase'> </td>
				    	<td class='kpiId'>${KpiList.key}</td>
		                <td class='kpiName'>${KpiList.value}</td> 
                	</tr>
                    </c:forEach>
				  </tbody>
			</table>
			
		</div>
		<div class="form-group">
			<div class='kpiValidation'></div>
		</div>
		<div class="form-group"> 
		<button id="addKpiInfoByDlId" type="Button" class="btn btn-primary btn-sm"><spring:message code="anvizent.admin.button.Submit"/></button>
		</div>
 </div>
  	
</div>
 
