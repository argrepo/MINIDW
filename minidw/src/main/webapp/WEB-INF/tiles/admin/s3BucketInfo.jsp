<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText"> <spring:message code="anvizent.package.label.S3BucketInfo"/></h4>
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
	
	<div id="s3InfoTable" style="display:none">
		<div class='row form-group'>
			<button style="float: right; margin-right: 1.5em;"
				class="btn btn-sm btn-success addS3Info"><spring:message code="anvizent.package.label.Add"/></button>
		</div>
		<div class="row form-group tblIntern">
			<div class="table-responsive">
				<table class="table table-striped table-bordered tablebg"
					id="s3InfoTbl">
					<thead>
						<tr>
							<th><spring:message code="anvizent.package.label.Id"/></th>
							<th> <spring:message code="anvizent.package.label.BucketName"/></th>
							<th><spring:message code="anvizent.package.label.AccessKey"/></th>
							<th><spring:message code="anvizent.package.label.SecretKey"/></th>
							<th><spring:message code="anvizent.package.label.IsActive"/></th>
							<th><spring:message code="anvizent.package.label.Edit"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div class="col-sm-10 s3Info" style="display:none">
			<div class="panel panel-default">
				<div class="panel-heading"> <spring:message code="anvizent.package.label.S3Details"/></div>
					<div class="panel-body">
					<input type="hidden" name="id" id="idValue">
							<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.BucketName"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="bucketName" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
							</div>
								
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.AccessKey"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="accessKey" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
							</div>
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.SecretKey"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="secretKey" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
							</div>
							
							<div class='row form-group'>
								<div id="active">
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.ActiveStatus"/>
									</div>
									<div class='col-sm-1'>
										<input type="radio" name="isActive" id="isActiveYes" value="yes"><spring:message code="anvizent.package.label.YES"/>
									</div>
									<div class='col-sm-2'>
										<input type="radio" name="isActive" id="isActiveNo" value="no"> <spring:message code="anvizent.package.label.NO"/>
									</div>
								</div>
							</div>
							<div>
								<div class='col-sm-1'>
										<button id="saveInfo" value="submit" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Submit"/></button>
								</div>
								<div class='col-sm-9'>
									<button id="back" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Back"/></button>
								</div>
							</div>
						</div>
						
						
					</div>
				</div>

</div>
	
