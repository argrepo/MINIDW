<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style>


html {
   width: 100%;  float: left;background: url("resources/images/loginpage_bg.jpg") no-repeat center center fixed;
 -webkit-background-size: cover;-moz-background-size: cover;-o-background-size: cover;background-size: cover;
}
body {
	background: transparent;
}
body.login {
    padding: 200px 0px;  background-color: transparent;
}
.login-logo{
    margin-bottom:5px;
}
.loginform_group {
    width: 100%; float: left;  height: auto;  padding: 0px;  border: 1px solid #ccc;background-color:#fff;position: relative;
}
.form-control
{
     padding-left: 30px;
    border: none;
    box-shadow: none;
    border-radius: 0px;
    border-bottom: 1px solid #ccc;
    padding: 15px 30px;
    float: left;
    height: auto;
}
.loginbox{
   width:380px; margin:auto;
   padding: 180px 0px;
   
}
.form-group {
    margin-bottom: 0px;
}
.nameblock {
      margin: 0px;
    width: 30px;
    height: 30px;
    background: url(resources/images/Username_bgimg.png) no-repeat 8px 8px;
    position: absolute;
    top: 8px;
    float: left;
    z-index: 10;
}
.passwordblock {
      margin: 0px;
    width: 30px;
    height: 30px;
    background: url(resources/images/Password_bgimg.png) no-repeat 8px 8px;
    position: absolute;
    top: 10px;
    float: left;
    z-index: 10;
}
.login_btn{
    width:100%; float:left; height:auto; padding:10px; margin:10px 0px;background-color:#065D8A; border-radius:0px;
}
.emptydiv{
width:100%; float:left; height:30px; background-color:#DDECF3;
}
@media only screen and (min-width : 1px) and (max-width : 600px) {
body.login {
    padding: 80px 0px;  background-color: transparent;
}
.loginbox{
   width:280px; margin:auto;
   padding: 10px 0px;
}
}


</style>
<div class="loginbox">
	<div class="login-logo">
		<div class="sprite-icons anvizent-logo"></div>
	</div>
	<c:if test="${isWebApp == false}">
		<form:form modelAttribute="loginForm" method="POST" id="loginForm"
			action="./authentication">
			<c:if test="${not empty loginError}">
				<div class="alert alert-danger errorMsgForValidation">${loginError}</div>
			</c:if>
			<c:choose>
				<c:when test="${ not empty errors}">
					<div class="alert alert-danger errorMsgForValidation">${errors }</div>
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${action == 'showClientAccessForm'}">
				<div class="loginform_group">
					<div class="nameblock"></div><div class="form-group">
						<form:input path="clientId" class="form-control" autocomplete="off"
							placeholder='Access Key' />
						<form:errors path="username" cssStyle="color: #ff0000" />
					</div>
					
					<div class="emptydiv"></div>
					</div>
					<button class="btn primary-btn login_btn" type="submit">
						Continue
					</button>
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
	</c:if>
</div>
