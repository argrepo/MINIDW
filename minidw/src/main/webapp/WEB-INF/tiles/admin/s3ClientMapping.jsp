<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText">
			  
			<spring:message code="anvizent.package.label.ClientS3BucketMapping" />
		</h4>
	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID"
			value="<c:out value="${principal.userId}"/>">
	</div>

	<div class="col-sm-10">
		<div id="successOrErrorMessage"></div>
	</div>
	<div class='row form-group'>

		<div id="clientInfo" style="display: none">
			<div class="col-md-8 add">
				<div class='row form-group'>
					<label class="col-sm-3 control-label labelsgroup"> 
						<spring:message code="anvizent.package.label.SelectClient" />:
					</label>
					<div class='col-sm-6 clientList'></div>
				</div>

			</div>
		</div>

		<div id="bucketInfo" style="display: none">
			<div class="col-md-8 add">
				<div class='row form-group'>
					<label class="col-sm-3 control-label labelsgroup"> 
						 <spring:message code="anvizent.package.label.SelectBucket" />:
					</label>
					<div class='col-sm-6 bucketList'></div>
				</div>

			</div>
		</div>

	</div>

	<div class='row form-group saveBtn' style="display: none">
		<div class='col-sm-3'>
			<button id="save" class="btn btn-primary btn-sm">
				
				<spring:message code="anvizent.package.label.Save" />
			</button>
		</div>
	</div>

</div>

