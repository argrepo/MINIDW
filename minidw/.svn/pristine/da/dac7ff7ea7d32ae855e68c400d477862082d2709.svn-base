<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="col-md-12 rightdiv">
  	<div class="col-xs-12">
			<jsp:include page="admin_error.jsp"></jsp:include>
	</div>
	<c:if test="${not empty authcodeorizationCode }">
		<script>
			window.opener.document.getElementById("authCodeValue").value = '<%=request.getAttribute("authcodeorizationCode") %>';
			window.close();
		</script>
	</c:if>	
	<c:if test="${empty authcodeorizationCode }">
		<script>
			window.close();
		</script>
	</c:if>	
</div>