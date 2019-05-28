<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
		<div class="page-title-v1"><h4><spring:message code="anvizent.navLeftTabLink.label.ETLAdmin"/></h4></div>
		<div class="dummydiv"></div>
		<ol class="breadcrumb">
		</ol>
		 <div class='row form-group'>
		     <h4 class="alignText"><spring:message code="anvizent.navLeftTabLink.label.ETLAdmin"/></h4>
		    </div>
		<div class="col-sm-12">
		   
		</div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="${principal.userId}">
	<div class="col-xs-12">	
		<div class='welcomePageMenu' >
		<h3><spring:message code="anvizent.navLeftTabLink.label.quicklinks"/></h3>
					<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 padding-five">
				      <div class="panel panel-default  quicklinkspanel1">
                      <div class="panel-body">
 	<a class="navLeftTabLink startLoader" href="<c:url value="/admin/vertical/clientMapping"/>"><span>
					<spring:message code="anvizent.navLeftTabLink.label.clientVerticalMapping"/></span></a>
					<div class="quicklink_sprite verticalmapping_icon"></div>
  </div>
  <div class="panel-footer"><p><a href="<c:url value="/admin/vertical/clientMapping"/>"><spring:message code="anvizent.navLeftTabLink.label.viewDetails"/></a></p>
  <a href="<c:url value="/admin/vertical/clientMapping"/>"><i class="fa fa-chevron-right accessarrow" aria-hidden="true"></i></a>
  </div>
</div>
					</div>
					
				
					<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 padding-five">
						 <div class="panel panel-default quicklinkspanel3">
                      <div class="panel-body">
 <a class="navLeftTabLink startLoader" href="<c:url value="/admin/connector/clientMapping"/>">
						<spring:message code="anvizent.navLeftTabLink.label.clientConnectorMapping"/></a>
						<div class="quicklink_sprite clientconnectormapping_icon"></div>
  </div>
  <div class="panel-footer"><p><a href="<c:url value="/admin/connector/clientMapping"/>"><spring:message code="anvizent.navLeftTabLink.label.viewDetails"/></a></p>
  <a href="<c:url value="/admin/connector/clientMapping"/>"><i class="fa fa-chevron-right accessarrow" aria-hidden="true"></i></a>
  </div>
</div>
					</div>
					<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 padding-five">
					 <div class="panel panel-default quicklinkspanel4">
                      <div class="panel-body">
 <a class="navLeftTabLink startLoader" href="<c:url value="/admin/dashBoardLayoutMaster/clientMapping"/>">
						<spring:message code="anvizent.navLeftTabLink.label.clientDLMapping"/></a>
						<div class="quicklink_sprite clientDLmapping_icon"></div>
  </div>
  <div class="panel-footer"><p><a href="<c:url value="/admin/dashBoardLayoutMaster/clientMapping"/>"><spring:message code="anvizent.navLeftTabLink.label.viewDetails"/></a></p>
<a href="<c:url value="/admin/dashBoardLayoutMaster/clientMapping"/>"><i class="fa fa-chevron-right accessarrow" aria-hidden="true"></i></a>
  </div>
</div>	
					</div>
						<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 padding-five">
							      <div class="panel panel-default quicklinkspanel2">
                      <div class="panel-body">
 <a class="navLeftTabLink startLoader" href="<c:url value="/admin/clientTableScripts/mapping"/>">
						<spring:message code="anvizent.navLeftTabLink.label.clientTableScriptsExecution"/></a>
						<div class="quicklink_sprite tablescriptmapping_icon"></div>
  </div>
  <div class="panel-footer"><p><a href="<c:url value="/admin/clientTableScripts/mapping"/>"><spring:message code="anvizent.navLeftTabLink.label.viewDetails"/></a></p>
<a href="<c:url value="/admin/clientTableScripts/mapping"/>"><i class="fa fa-chevron-right accessarrow" aria-hidden="true"></i></a>
  </div>
</div>
					</div>
					<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 padding-five">
						 <div class="panel panel-default quicklinkspanel5">
                      <div class="panel-body">
 <a class="navLeftTabLink startLoader" href="<c:url value="/admin/defaultTemplates"/>">
						 <spring:message code="anvizent.navLeftTabLink.label.defaultMappingConfiguration"/></a>
							<div class="quicklink_sprite defaultmappingconfig_icon"></div>
  </div>
  <div class="panel-footer"><p><a href="<c:url value="/admin/defaultTemplates"/>"><spring:message code="anvizent.navLeftTabLink.label.viewDetails"/></a></p>
  <a href="<c:url value="/admin/defaultTemplates"/>"><i class="fa fa-chevron-right accessarrow" aria-hidden="true"></i></a>
  </div>
</div>	
					</div>
					<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 padding-five">
						 <div class="panel panel-default quicklinkspanel6">
                      <div class="panel-body">
 <a class="navLeftTabLink startLoader" href="<c:url value="/admin/clientWebserviceMapping"/>">
							<spring:message code="anvizent.navLeftTabLink.label.webServiceClientMapping"/></a>
							<div class="quicklink_sprite webservicemapping_icon"></div>
  </div>
  <div class="panel-footer"><p><a href="<c:url value="/admin/clientWebserviceMapping"/>"><spring:message code="anvizent.navLeftTabLink.label.viewDetails"/></a></p>
  <a href="<c:url value="/admin/clientWebserviceMapping"/>"><i class="fa fa-chevron-right accessarrow" aria-hidden="true"></i></a>
  </div>
</div>	
					</div>
					<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 padding-five">
					 <div class="panel panel-default quicklinkspanel7">
                      <div class="panel-body">
<a class="navLeftTabLink startLoader" href="<c:url value="/admin/errorLogs"/>">
						<spring:message code="anvizent.navLeftTabLink.label.errorlogs"/></a>
						<div class="quicklink_sprite errorlogs_icon"></div>
  </div>
  <div class="panel-footer"><p><a href="<c:url value="/admin/errorLogs"/>"><spring:message code="anvizent.navLeftTabLink.label.viewDetails"/>
						</a></p>
  <a href="<c:url value="/admin/errorLogs"/>"><i class="fa fa-chevron-right accessarrow" aria-hidden="true"></i></a>
  </div>
</div>	
					</div>
					<%-- <div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 padding-five">
					 <div class="panel panel-default quicklinkspanel8">
                      <div class="panel-body">
 	<a class="navLeftTabLink startLoader" href="<c:url value="/adt/package/s3AuditLogs"/>">
						<spring:message code="anvizent.navLeftTabLink.label.s3AuditLogs"/></a>
						<div class="quicklink_sprite s3auditlogs_icon"></div>
  </div>
  <div class="panel-footer"><p><a href="<c:url value="/adt/package/s3AuditLogs"/>"><spring:message code="anvizent.navLeftTabLink.label.viewDetails"/>
						</a></p>
  <a href="<c:url value="/adt/package/s3AuditLogs"/>"><i class="fa fa-chevron-right accessarrow" aria-hidden="true"></i></a>
  </div>
</div>
					</div> --%>	 
		</div>
	</div>
  </div>
