<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
 <div class="col-md-12 rightdiv"  >
   <div class='row form-group'>
<h4 class="alignText"><spring:message code="anvizent.admin.label.iLDLMapping"/></h4>
</div>
<div class="col-md-10">
<div class="dummydiv"></div>
		<div class="col-sm-10">
		</div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="${principal.userId}">
</div>
<div class="col-md-10 saveEtlDlIlMapping"></div>
<div class="col-md-8">
	<div class="form-group" >		 
			<label class="col-sm-2 control-label"><spring:message code="anvizent.admin.label.DLName"/>:</label>
			<div class="col-sm-6">
				<select class="form-control" id="dlName">
				    <option value="0"><spring:message code="anvizent.package.label.selectDL"/></option>
				    <c:forEach items="${modelObject.dLList}" var ="dLList">
		             <option value="${dLList.dL_id}"> ${dLList.dL_name} </option>
                    </c:forEach>
				</select>
			</div>		
		</div>
		 
 </div>
 <div class="col-md-12"><div class="form-group"></div></div>
 <div class="col-md-6" style='margin-left:27px'>
	<form class="form-horizontal" role="form">
		<div class="form-group" id="tablecontainer">
			<table class="table table-striped table-bordered tablebg table-hover" id="ilInfo">
				<thead>
				<tr>
					<th><input type="checkbox" id="selectall"/> <spring:message code="anvizent.package.label.dlMapped"/></th>
					<th><spring:message code="anvizent.package.label.ilId"/></th>
					<th><spring:message code="anvizent.package.label.ilName"/></th>
				</tr>
				</thead>	 
				  <tbody>
				    <c:forEach items="${modelObject.iLList}" var ="iLList">
				    <tr>
				    	<td> <input value='${iLList.iL_id}' type='checkbox' class='ilInfoByDlId checkCase'> </td>
		                <td class='iLId'>${iLList.iL_id}</td>
		                <td class='ilname'>${iLList.iL_name}</td> 
                	</tr>
                    </c:forEach>
				  </tbody>
			</table>
		</div>
		<div class='ilValidation'></div>
		<a href='<c:url value="/admin/ETLAdmin"/>' class="btn btn-primary btn-sm"><spring:message code="anvizent.admin.button.Back"/></a>
		<input id="addIls" type="button" value="<spring:message code="anvizent.package.button.submit"/>" class="btn btn-primary btn-sm"> 
	    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>
	
</div>
 
</div>
 
 