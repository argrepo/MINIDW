<%@page import="com.datamodel.anvizent.controller.errorController.ErrorController"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="jumbotron">
asas
	<h1><spring:message code="error.systen.error.heading"/></h1>
	<p><spring:message code="error.systen.error.text"/></p>
	
	<% 
		Exception ex = (Exception) request.getAttribute("exception");
		if (ex != null) {
			Log log = LogFactory.getLog(ErrorController.class);
			log.error("Unexpected error", ex);
		}
	%>
</div>