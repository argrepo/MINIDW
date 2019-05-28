<%@page import="com.datamodel.anvizent.controller.errorController.ErrorController"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="jumbotron">
	<h1>500 Error</h1>
	<p>Internal Server Occured. Please contact System Administrator.</p>
<% 
	Exception ex = (Exception) request.getAttribute("exception");
	if (ex != null) {
		Log log = LogFactory.getLog(ErrorController.class);
		log.error("Unexpected error", ex);
	}
%>
</div>