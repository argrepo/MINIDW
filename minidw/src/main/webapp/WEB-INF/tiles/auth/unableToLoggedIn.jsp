<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${not empty errors }">
<div class="alert alert-danger errorMsgForValidation">${errors }</div>
</c:if>
<c:if test="${empty errors }">

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
</c:if>


<style>
.errorMsgForValidation {
width: 100%;
color: red;
text-align: center;
}
</style>