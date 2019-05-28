<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
<div class='row form-group'>
<h4 class="alignText"><spring:message code="anvizent.admin.label.createContextParameter"/></h4>
</div>
<div class="col-md-10">
<div class="dummydiv"></div>
		<div class="col-sm-10">
		</div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="${principal.userId}">
</div>
<div class="col-md-10">
<div class="createParamSucessFailure"></div>
</div>

<div class="col-md-6">	 
         <div class="panel " id='createContextParameters'>
		    <div class="panel-heading"><spring:message code="anvizent.admin.label.createContextParameter"/></div>
			<div class="panel-body">
			
				<div class='row form-group'>
				   <div class='col-sm-3'>
						<spring:message code="anvizent.admin.label.ParamName"/>:
				   </div>
				   <div class='col-sm-6'>
						<input type="text" class="form-control" id="parameterName"  placeholder="<spring:message code = "anvizent.package.label.paramName"/>" name="paramName">
				   </div>
				
				</div>
				<div class='row form-group'>
				   <div class='col-sm-3'>
				   </div>
				   <div class='col-sm-3' >
						<button id="createContextParameter" type="submit" class="btn btn-info btn-sm"><spring:message code="anvizent.admin.button.CreateParameter"/></button>
				   </div>
				</div>
			</div>
       </div>
       <a href='<c:url value="/admin/ETLAdmin"/>' class="btn btn-primary btn-sm"><spring:message code="anvizent.admin.button.Back"/></a>
</div>
<div class="col-md-6">	 
         <div class="panel " id='createContextParameters'>
		    <div class="panel-heading"><spring:message code="anvizent.admin.label.ExistingContextParameters"/></div>
			<div class="panel-body" style="overflow-y:scroll;max-height:425px">
			<table class="table table-striped table-bordered tablebg table-hover" id="contextParamTable">
                <thead>
                <tr>
                <th><spring:message code="anvizent.package.label.paramId"/></th>
                <th><spring:message code ="anvizent.package.label.paramName"/></th> 
                </tr> 
                </thead>
                <tbody id='addContextParams'>
                <c:forEach items="${contextParams}" var ="contextParam">
                	 <tr>
		                <td>${contextParam.param_id}</td>
		                <td>${contextParam.param_name}</td> 
                	</tr>
                </c:forEach>
                </tbody>
            </table>
			</div>
       </div>
</div>
</div>