<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="clearfix"></div>
<div id ="pageErrors">
	<c:if test="${not empty errors}">
		<c:choose>
			<c:when test="${not empty messagecode}">
				<c:if test="${messagecode == 'SUCCESS'}">
					<div class="alert alert-success alert-dismissible" role="alert">
				</c:if>
				<c:if test="${messagecode == 'FAILED' || messagecode == 'ERROR'}">
					<div class="alert alert-danger alert-dismissible" role="alert">
				</c:if>
			</c:when>
			<c:otherwise>
				<div class="alert alert-danger alert-dismissible" role="alert">
			</c:otherwise>
		</c:choose>
			<button type="button" class="close" data-dismiss="alert">
			    <span aria-hidden="true">&times;</span><span class="sr-only"><spring:message code="anvizent.package.Close"/></span>
		   	</button>
	    	<div>${errors}</div>
		</div>
	</c:if>

</div>