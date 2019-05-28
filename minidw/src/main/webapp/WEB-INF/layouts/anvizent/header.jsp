<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="header">
	<%-- UI Changes --%>
	<div class="col-xs-4 col-sm-2">
		<div class="anvizent-logo">
			<img src="<c:url value="/resources/images/anvizent-logo.png"/>">
		</div>
	</div>
	<%--  UI Changes --%>
	<div class="col-xs-8 col-sm-10 login-status">
		<div class="dropdown userprofilelist">
			<button class="btn btn-default dropdown-toggle" type="button"
				id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="true"></button>
			<ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
				<li class="user_image" style="text-align: center; font-size: 28px;"></li>
				<li><span class="welcome-text"><spring:message code="anvizent.package.label.welcome"/></span></li>
				<li>
					<p class="user-name">${principal.userName}</p>
				</li>
				<%-- <c:if test="${principal.roleId != -300}">
					<li><a class="navLeftTabLink changelogBtns"
					href="#"> <span>Change log</span></a></li>
				</c:if> --%>
				<li><a class="navLeftTabLink dwprofilelink"
					href="<c:url value="/userProfile"/>"> <span><spring:message
								code="anvizent.navLeftTabLink.label.userProfile" /></span></a></li>
				<li><a class="navLeftTabLink dwlogoutlink"
					href="<c:url value="/logout"/>"> <span><spring:message
								code="anvizent.navLeftTabLink.label.logOut" /></span></a></li>
			</ul>
			<ul class="">
				<li class="user-details"></li>
			</ul>
		</div>
	</div>
</div>