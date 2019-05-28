<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />

<div class="col-md-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.Internalization"/></h4>
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
	<div id="pageInfo" style="display: none">
		<div class='row form-group'>
			<button style="float: right; margin-right: 1.5em;"
				class="btn btn-sm btn-success addInternalization"><spring:message code="anvizent.package.label.Add"/></button>
		</div>
		<div class="row form-group tblIntern">
			<div class="table-responsive">
				<table class="table table-striped table-bordered tablebg"
					id="internalizationTbl">
					<thead>
						<tr>
							<th><spring:message code="anvizent.package.label.Id"/></th>
							<th><spring:message code="anvizent.package.label.LocaleId"/></th>
							<th><spring:message code="anvizent.package.label.LocaleName"/></th>
							<th><spring:message code="anvizent.package.label.Edit"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div id="addInfo" style="display: none">
		<div class="col-md-8 add">
			<div class='row form-group'>
			<input type="hidden" name="id" id="idValue">
				<label class="col-sm-3 control-label labelsgroup"><spring:message code="anvizent.package.label.SelectLanguage"/></label>
				<div class='col-sm-6 localeList'>
				</div>
			</div>
			<div class='row form-group'>
				<div class='col-sm-3'></div>
				<div class='col-sm-2'>
					<button id="proceedForMapping" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.ProceedForMapping"/></button>
				</div>
				<div class='col-sm-3'>
						<button id="back" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Back"/></button>
				</div>
			</div>


		</div>
	</div>
	<div id="propertiesInfo" style="display: none">

		<div class="col-sm-12">
			<div id="propertiesTabs">
				<ul>
					<li><a href="#packageProperties"><spring:message code="anvizent.package.label.ApplicationProperties"/></a></li>
					<li><a href="#serviceProperties"><spring:message code="anvizent.package.label.ServiceProperties"/></a></li>
				</ul>
				<div id="packageProperties">
					<div class='row form-group'>
						<div class="table-responsive" style="max-height: 400px;">
							<table class="table table-striped table-bordered tablebg" 
								id="packageProTable" style="table-layout: fixed;word-wrap: break-word;">
								<thead>
									<tr id="fixedHeader">
										<th><spring:message code="anvizent.package.label.EnglishKey"/></th>
										<th><spring:message code="anvizent.package.label.EnglishValue"/></th>
										<th id="proLanguage"><spring:message code="anvizent.package.label.SelectedLanguage"/></th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div id="serviceProperties">
					<div class='row form-group'>
						<div class="table-responsive" style="max-height: 400px;">
							<table class="table table-striped table-bordered tablebg"
								id="serviceProTable" style="table-layout: fixed;word-wrap: break-word;">
								<thead>
									<tr id="fixedHeader">
										<th><spring:message code="anvizent.package.label.EnglishKey"/></th>
										<th><spring:message code="anvizent.package.label.EnglishValue"/></th>
										<th id="serviceLanguage"><spring:message code="anvizent.package.label.SelectedLanguage"/></th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>

			</div>
		</div>
		<div class="col-sm-10">
			<div id="errorMessage"></div>
		</div>
		<div class='col-sm-3'>
							<button id="savePropreties" value="submit" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Submit"/></button>
		</div>
	</div>
	
</div>