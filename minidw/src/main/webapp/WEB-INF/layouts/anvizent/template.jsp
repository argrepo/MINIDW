<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html lang="en">
<head>
<tiles:insertAttribute name="head" />
<tiles:insertAttribute name="pageStyles" ignore="true"/>
</head>
<body class="<tiles:getAsString name="app" />">
	<c:set var="isLoginPage"><tiles:getAsString name="app" /></c:set>
	<div class="loader">
		 <div class="img-holder">
		  <img src="<c:url value="/resources/images/loading.gif"/>">
		 </div>
		  <div style="background: #f2f2f2 none repeat scroll 0 0;opacity:0.5;width:100%;height:100%;"></div>
  	</div>

	<%-- Alert messages --%>
	<div class="modal fade messsage-popup" tabindex="-1" role="alert"
		id="messagecontanier" data-backdrop="static" data-keyboard="false" style="z-index: 1100;">
		<div class="modal-dialog modal-md">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>			       
			        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
			    </div>
				<div class="modal-body">
					<div class="row">
						<div class="col-sm-12" id="msgcontent"></div>
					</div>
					<div class="clear15"></div>
					<div class="row">
						<div class="col-sm-12">
							<button data-dismiss="modal" class="btn btn-primary btn-sm no-radius"><spring:message code ="anvizent.package.button.ok"/></button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<tiles:insertAttribute name="header" ignore="true"/>
	<tiles:insertAttribute name="nav-left" ignore="true" />
	<div class="<tiles:getAsString name="page" />">
		<!-- as login page has  different CSS-->
		<tiles:insertAttribute name="content" ignore="true" />
	</div>
	<footer>
		<tiles:insertAttribute name="footer"/>
	</footer>
	<tiles:insertAttribute name="scripts" />
	<tiles:insertAttribute name="pageScripts" ignore="true"/>
</body>

</html>