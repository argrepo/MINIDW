<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<style>
.smtppanel
{
   min-height: 367px;
} 
.panel-body {
    padding: 10px 0px;
}
#saveClientConfigSettings{
    background-color: #71BA44;color: #fff;padding: 5px 20px;border: 1px solid #71BA44;font-size: 12px;
}
#skipClientConfigSettings{
         background-color: #347AB8;color: #fff; padding: 5px 20px;font-size: 12px; border: 1px solid #347AB8;
}
.anvizent-logo {
	height: 50px;
}
</style>
<div class="header">
   <%-- UI Changes --%>
	<div class="col-xs-4 col-sm-2">
            <div class="anvizent-logo">
            <img src="<c:url value="/resources/images/anvizent-logo.png"/>">
            </div> 
     </div>
       <%--  UI Changes --%>
     <div class="col-xs-8 col-sm-10 login-status">
     <div class="dropdown userprofilelist">
     
    </div>
    </div>
</div>

	<div class='row form-group'>
		<h4 class="alignText"><i class="fa fa-cogs" aria-hidden="true"></i> Configuration Settings</h4>
	</div>

	<c:url value="/configurationSettings" var="url" />
	<input type="hidden" value="${url}/save" id="save">
<div class="col-sm-12 rightdiv" style="padding:0px 5px;">
	<form:form modelAttribute="hybridClientsGrouping" action="${url}">
			<c:if test="${not empty loginError}">
				<div class="alert alert-danger errorMsgForValidation">${loginError}</div>
			</c:if>
		<c:choose>
			<c:when test="${not empty hybridClientsGrouping.name	 }">
				<div class="row form-group"></div>
<div class="row">
				<div class="col-md-3">
				</div>
				<div class="col-md-6">
					<div class="panel panel-default">
						<div class="panel-heading">Hybrid Group Details</div>
						<div class="panel-body">
							<div class="row form-group">
								<label class="control-label labelsgroup col-md-5" style="margin:0px;">Name:</label>
								<div class="col-md-7">
									<form:hidden path="accessKey" />
									${hybridClientsGrouping.name }
								</div>
							</div>
							
							<div class="row">
								<label class="control-label labelsgroup col-md-5" style="margin:0px;">Client Ids:</label>
								<div class="col-md-7">
									<ul>
										<c:forEach items="${hybridClientsGrouping.clientIds}" var="clientIds">
											<li>
												<b><c:out value="${clientIds.id}"/></b> <c:out value="${clientIds.clientName}"/>
											</li>
										</c:forEach>
									</ul>
								</div>
							</div>
							
							<div class="row form-group">
								<div class="col-md-12 text-center">
									<button type="submit" class="btn btn-primary btn-sm" id="skipClientConfigSettings">Save</button>
								</div>
							</div>
							
						</div>
					</div>
				</div>
				<div class="col-md-3">
				</div>
	</div>		
				<div class="row form-group" style="padding: 0px 20px;">
					<div class="pull-right">
						<!-- <button type="submit" class="btn btn-primary btn-sm" id="skipClientConfigSettings">Save</button> -->
						<!-- <button type="submit" class="btn btn-primary btn-sm" id="saveClientConfigSettings">Save</button> -->
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<script type="text/javascript">
							<% 
								String url = "";
								if (request.getContextPath() != null && request.getContextPath().toString().equals("") ) {
									url = "/";
								} else {
									url = request.getContextPath();
								}
							
							%>
						location.href = "<%=url %>";
					</script>
			</c:otherwise>
		</c:choose>

	</form:form>

</div>