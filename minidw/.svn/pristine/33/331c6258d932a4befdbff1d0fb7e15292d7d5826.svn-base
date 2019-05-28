<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText">
			<spring:message code="anvizent.admin.button.currencyLoad" />
		</h4>
	</div>

	<div>
		<div class="dummydiv"></div>
		<jsp:include page="_error.jsp"></jsp:include>
		<input type="hidden" id="userID"
			value="<c:out value="${principal.userId}"/>">
	</div>

	<div class="panel-body">
	<form method="POST" id="currencyLoadForm" enctype="multipart/form-data">
			<div class='row form-group '>
				<div class='col-sm-4'>
					<spring:message code="anvizent.package.label.fileType" />
				</div>
				<div class='col-sm-6'>
					<select class="form-control" id="flatFileType" name="flatFileType">
						<option value="csv">csv</option>
						<!-- <option value="xls">xls</option>
						<option value="xlsx">xlsx</option> -->
					</select>
				</div>
			</div>

			<div class='row form-group delimeter-block'>
				<div class='col-sm-4'>
					<spring:message code="anvizent.package.label.delimiter" />
					:
				</div>
				<div class='col-sm-6'>
					<input type="text" class="form-control" id="delimeter" value=","
						name="delimeter" readonly="readonly">
				</div>
			</div>

			<div class='row form-group flatDataSourceOther' style="display: none">
				<div class='col-sm-4'></div>
				<div class='col-sm-6'>
					<input type="text" id="flatDataSourceOtherName"
						class="form-control" data-minlength="1" data-maxlength="45">
				</div>
			</div>

			<div class='row form-group '>

				<div class='col-sm-4'>
					<spring:message code="anvizent.package.label.file" />

				</div>
				<div class='col-sm-6'>
					<input type="file" name="file" id="fileUpload">
				</div>
				<div class='col-sm-4'></div>
				<div class='col-sm-8'>
					<p class="help-block disclaimerNote">
						<em><spring:message
								code="anvizent.package.label.notePleaseMakeSureFileIsHavingHeaders" /></em>
					</p>
					<p class="help-block">
						<em><spring:message
								code="anvizent.package.label.noteDateTimeFormat" /> <c:out
								value="${'< yyyy-MM-dd HH:mm:ss >'}" /></em>
					</p>
				</div>
			</div>
			 <div class='row form-group '>
				<div class='col-sm-6'>
					<input type="button" value='<spring:message code="anvizent.package.button.saveUpload"/>' class="btn btn-primary btn-sm" id='saveAndUpload' />
				</div>
				<div class='col-sm-6'>
				<div class="pull-right">
				    <button type="button" class="btn btn-primary btn-sm back_btn downloadSampleTemplate" id="downloadTemplate">
						<span title="<spring:message code = "anvizent.package.label.DownloadTemplate"/>" class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>
					</button>
				    <a  class="btn btn-primary btn-sm " href="<c:url value="/adt/package/viewCurrencyLodJobResults"/>"> <spring:message code="anvizent.package.label.viewResults"/>  </a>
				</div>
				</div>
			</div>
			
		</form>
	</div>
	<div class="modal fade" tabindex="-1" role="dialog" id="downloadILTemplate" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title">
							<spring:message code="anvizent.package.label.template" />
						</h4>
					</div>
					<div class="modal-body">
						<div class="container">
							<label class="radio-inline"> <input type="radio" name="ilTemplate" id="ilCsv" checked>csv
							</label>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="confirmDownloadILTemplate">
							<spring:message code="anvizent.package.button.ok" />
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
			</div>
		</div>
</div>

