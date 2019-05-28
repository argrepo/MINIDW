<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="col-md-12 rightdiv">
  	<div class="col-xs-12">
			<jsp:include page="admin_error.jsp"></jsp:include>
	</div>
	<c:if test="${not empty oauth_verifier }">
		<script>
			window.opener.document.getElementById("oauth1CodeValue").value = '<%=request.getAttribute("oauth_verifier") %>';
			window.close();
		</script>
	</c:if>	
	<c:if test="${empty oauth_verifier }">
		<script>
			window.close();
		</script>
	</c:if>	
</div>