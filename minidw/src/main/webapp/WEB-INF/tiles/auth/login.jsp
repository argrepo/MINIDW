<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style>
html {
	width: 100%;
	float: left;
	background: url("resources/images/loginpage_bg.jpg") no-repeat center
		center fixed;
	-webkit-background-size: cover;
	-moz-background-size: cover;
	-o-background-size: cover;
	background-size: cover;
}

body.login {
	padding: 200px 0px;
	background-color: transparent;
}

.login-logo {
	margin-bottom: 5px;
}

.loginform_group {
	width: 100%;
	float: left;
	height: auto;
	padding: 0px;
	border: 1px solid #ccc;
	background-color: #fff;
	position: relative;
}

.form-control {
	padding-left: 30px;
	border: none;
	box-shadow: none;
	border-radius: 0px;
	border-bottom: 1px solid #ccc;
	padding: 15px 30px;
	float: left;
	height: auto;
}

.loginbox {
	width: 380px;
	margin: auto;
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

.login_btn {
	width: 100%;
	float: left;
	height: auto;
	padding: 10px;
	margin: 10px 0px;
	background-color: #065D8A;
	border-radius: 0px;
}

.emptydiv {
	width: 100%;
	float: left;
	height: 30px;
	background-color: #DDECF3;
}

@media only screen and (min-width : 1px) and (max-width : 600px) {
	body.login {
		padding: 80px 0px;
		background-color: transparent;
	}
	.loginbox {
		width: 280px;
		margin: auto;
	}
}
</style>
<div class="loginbox">
	<div class="login-logo">
		<div class="sprite-icons anvizent-logo"></div>
	</div>
	<c:if test="${not empty loginError}">
		<div class="alert alert-danger errorMsgForValidation">${loginError}</div>
	</c:if>
	<c:if test="${isWebApp == false}">
		<form:form modelAttribute="loginForm" method="POST" id="loginForm"
			action="./securityCheck">

			<c:choose>
				<c:when test="${ not empty errors}">
					<div class="alert alert-danger errorMsgForValidation">${errors }</div>
				</c:when>
				<c:when test="${ not empty showSuccess}">
					<div class="alert alert-success">${showSuccess }</div>
				</c:when>
				<c:when test="${not empty sessionTimeOutMessage }">
					<div class="alert alert-danger errorMsgForValidation">${sessionTimeOutMessage }</div>
				</c:when>
			</c:choose>
			<c:if test="${action == 'showLoginForm'}">
				<div class="loginform_group">
				   <div style="position: relative; float: left; width: 100%;">
						<div class="passwordblock"></div>
						<div class="form-group">
							<form:input path="clientId" class="form-control"
								autocomplete="off" value="" placeholder='Client Id'
								maxlength="50" />
							<form:errors path="clientId" cssStyle="color: #ff0000" />
						</div>
					</div>
					<div style="position: relative; float: left; width: 100%;">
						<div class="nameblock"></div>
						<div class="form-group">
							<input type="hidden" id="timeZone" name="timeZone">
							<form:input path="username" class="form-control"
								autocomplete="off" value="" placeholder='Username'
								maxlength="100" /> 
							<form:errors path="username" cssStyle="color: #ff0000" />
						</div>
					</div>
					<div style="position: relative; float: left; width: 100%;">
						<div class="passwordblock"></div>
						<div class="form-group">
							<form:password path="password" class="form-control"
								autocomplete="off" value="" placeholder='Password'
								maxlength="50" />
							<form:errors path="password" cssStyle="color: #ff0000" />
						</div>
					</div>
					<div style="position: relative; float: left; width: 100%;">
						<div class="form-group">
							<form:select path="locale" cssClass="form-control col-xs-5 ">
								<form:option value="en_US">English-United States</form:option>
								<form:option value="es_CO">Spanish-Colombia</form:option>
								<%-- <form:option value="de_CH">German-Switzerland</form:option>
								<form:option value="fr_BE">France-Belgium</form:option>  --%>
								<%-- <form:option value="en_US">English-United States</form:option> --%>
								<%-- <form:option value="es_ES">Spanish-Spain</form:option> --%>
							</form:select>
							<form:errors path="locale" cssStyle="color: #ff0000" />
						</div>
					</div>
					<div class="emptydiv"></div>
				</div>
				<button class="btn primary-btn login_btn" type="submit">
					<spring:message code="anvizent.login.button.login" />
				</button>
				
				<script>
				window.onload = function(){
					document.getElementById("timeZone").value = common.getTimezoneName();
				}	
				
				</script>
				

			</c:if>
		</form:form>
	</c:if>
	<c:if test="${isWebApp == true}">
		<div class='form-group' style="text-align: center;">
			<c:choose>
				<c:when test="${ not empty errors}">
					<h4 class="text-danger errorMsgForValidation">${errors }</h4>
				</c:when>
				<c:when test="${isSessionExpired == true}">
					<h4 class="text-danger errorMsgForValidation">Session Time Out</h4>
				</c:when>
			</c:choose>
			<p>Please login using the link MINI-DW in Anvizent</p>
		</div>
		<script>
			window.close();
		</script>
	</c:if>

</div>
