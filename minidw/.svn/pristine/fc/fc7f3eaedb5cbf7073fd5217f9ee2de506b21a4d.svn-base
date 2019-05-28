<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html>
<html lang="en">
<head>
<tiles:insertAttribute name="head" />
<!-- Custom styles for this template -->
<tiles:insertAttribute name="pageStyles" ignore="true"/>
</head>
<body class="<tiles:getAsString name="app" />">
	<div class="<tiles:getAsString name="page" />">
		<tiles:insertAttribute name="header" />
		<div class="container-fluid">
			<div class="row">
				<div class="main">
					<div class="row mrn20">
						
						<!-- refacing sprint 1 - hide page-navigation
						<div class="col-lg-10 col-md-12 col-sm-12 content-right"> 
						--> 
						<div class="col-lg-12 col-md-12 col-sm-12 content-right">
							<div id="page-nav-small" class="btn-group hidden-lg clearfix secondary" role="group" aria-label="Secondary Navigation">
							</div>
							<section class="content">
								<tiles:insertAttribute name="content" ignore="true" />
							</section>
						</div>
					</div>
				</div>
			</div>
		</div>
	
	
		<tiles:insertAttribute name="footer" ignore="true" />
		
		<tiles:insertAttribute name="scripts" />
		<tiles:insertAttribute name="pageScripts" ignore="true"/>
	</div>
</body>

</html>