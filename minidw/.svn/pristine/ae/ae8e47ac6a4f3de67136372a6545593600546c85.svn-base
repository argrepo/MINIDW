<%@page import="com.datamodel.anvizent.controller.errorController.ErrorController"%>
<%@page import="org.apache.commons.logging.LogFactory"%>
<%@page import="org.apache.commons.logging.Log"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
<div class="jumbotron">
	
	<h3>HTTP Error 403 - Forbidden</h3>
<% 
	Exception ex = (Exception) request.getAttribute("exception");
	if (ex != null) {
		Log log = LogFactory.getLog(ErrorController.class);
		log.error("Unexpected error", ex);
	}
%>
</div>