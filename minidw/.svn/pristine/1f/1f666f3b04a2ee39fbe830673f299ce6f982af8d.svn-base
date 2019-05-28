<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
<div class='row form-group'>
<h4 class="alignText"><spring:message code="anvizent.navLeftTabLink.label.FileSettings"/></h4>
</div>
<div class="col-md-10">
<div class="dummydiv"></div>
        <div class="createFileSizeSucessFailure"></div>
		<div class="col-sm-10">
		</div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
</div>

     <div class="col-md-8">	 
				<div class='row form-group hidden'>
				   <label class="col-sm-3 control-label labelsgroup">
						<spring:message code="anvizent.admin.label.maxfilesize"/>:
				  </label>
				   <div class='col-sm-6'>
						<input type="text" class="form-control" id="maxFileSize" value="<c:out value="${fileSettingsInfo.maxFileSizeInMb}"/>" name="fileSize">
				   </div>
				   <div class='col-sm-3'>
						<spring:message code="anvizent.admin.label.mb"/>
				   </div>
				</div>
				
				
				<div class='row form-group'>
				   <label class="col-sm-3 control-label labelsgroup">
						<spring:message code="anvizent.admin.label.multiPartEnabled"/>:
				  </label>
				      <div class='col-sm-6'>
						<input type="checkbox"  id="multiPartEnabled" name="multiPartEnabled" ${fileSettingsInfo.multiPartEnabled == true ? 'checked':''}>
				   </div> 
				</div>
				
				
				<div class='row form-group noOfRecordsPerFilesDiv'>
				   <label class="col-sm-3 control-label labelsgroup">
						<spring:message code="anvizent.admin.label.noOfRecordsPerFile"/>:
				  </label>
				      <div class='col-sm-6'>
						<input type="text" class="form-control" id="noOfRecordsPerFile" value="<c:out value="${fileSettingsInfo.noOfRecordsPerFile}"/>" name="noOfRecordsPerFile">
				   </div>
				</div>
				<div class='row form-group'>
				   <label class="col-sm-3 control-label labelsgroup">
						<spring:message code="anvizent.admin.label.fileEncryptionRequired"/>:
				  </label>
				      <div class='col-sm-6'>
						<input type="checkbox"  id="fileEncryption" name="fileEncryption" ${fileSettingsInfo.fileEncryption == true ? 'checked':''}>
				   </div> 
				</div>
				
				
				<div class='row form-group '>
				   <div class='col-sm-3'>
				   </div>
				   <div class='col-sm-3' >
						<button id="updateMaxFileSize" type="submit" class="btn btn-primary btn-sm"><spring:message code="anvizent.admin.button.Submit"/></button>
				   </div>
				</div>
				
</div>
</div>