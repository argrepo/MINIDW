<%@page import="java.util.Calendar"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://tomcat.apache.org/date-taglib" prefix="date"%>

<div class="col-sm-12" style="border-top: 1px solid #ccc;">
<c:if test="${appType == '1' || appType == '0'}">
<p><spring:message code="footer.copyright"/>&copy; <%=Calendar.getInstance().get(Calendar.YEAR) %> &nbsp;&nbsp;&nbsp;V${version}&nbsp;&nbsp;&nbsp; <date:today/> </p>
</c:if>
<c:if test="${appType == '2'}">
<p> AI &copy; <%=Calendar.getInstance().get(Calendar.YEAR) %> &nbsp;&nbsp;&nbsp;V${version}&nbsp;&nbsp;&nbsp; <date:today/> </p>
</c:if>
	
</div>
