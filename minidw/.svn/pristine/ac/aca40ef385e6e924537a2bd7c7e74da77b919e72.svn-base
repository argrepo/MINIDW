<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="col-md-12 rightdiv">

	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.CurrencyIntegration" /> </h4>
	</div>
	<jsp:include page="admin_error.jsp"></jsp:include>

	<input type="hidden" id="userId"
		value="<c:out value="${principal.userId}"/>">

	<c:url value="/admin/currencyIntegration" var="url" />
	<input type="hidden" value="${url}/instantRun" id="urlRun"> <input type="hidden" value="${url}/add" id="save">

	<form:form modelAttribute="currencyIntegration"
		enctype="multipart/form-data">
		<div class="row form-group"></div>
		<div class="col-sm-10 currencyApiDetails">
			<div class="panel panel-default">
				<div class="panel-heading"><spring:message code="anvizent.package.label.CurrencyApiDetails" /> </div>
				<div class="panel-body">
					<form:hidden path="id" />
					<div class="row form-group">
						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.Apiurl" />: </label>
						<div class="col-sm-4">
							<form:input path="apiUrl" cssClass="form-control apiUrl"
								data-minlength="1" data-maxlength="255" readonly="true" />
						</div>
						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.AccessKey" /> :</label>
						<div class="col-sm-4">
							<form:input path="accessKey" cssClass="form-control accessKey"
								data-minlength="1" data-maxlength="45" readonly="true" />
						</div>
					</div>

					<div class="row form-group">
						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.Source" /> :</label>
						<div class="col-sm-4">
							<form:input path="source" cssClass="form-control source"
								data-minlength="1" data-maxlength="45" readonly="true" />
						</div>

						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.ScheduleRecurrence" /> :</label>
						<div class="col-sm-4">
							<input class="form-control currencies" value="Daily"
								data-minlength="1" data-maxlength="45" readonly="readonly" />
						</div>
					</div>

					<div class="row form-group">
						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.Time" />  :</label>
						<div class="col-sm-4">
							<form:select path="time_hours" disabled="true"
								cssClass="form-control" cssStyle="width:auto;display:initial;">
								<c:if test="${not empty hours}">
									<form:options items="${hours}" />
								</c:if>
							</form:select>
							:
							<form:select path="time_minutes" disabled="true"
								cssClass="form-control" cssStyle="width:auto;display:initial;">
								<c:if test="${not empty minutes}">
									<form:options items="${minutes}" />
								</c:if>
							</form:select>
						</div>
						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.timeZone"/> :</label>

						<div class='col-sm-4'>
							<form:select path="timeZone" disabled="true"
								cssClass="form-control">
								<spring:message code="anvizent.package.label.selectTimeZone"
									var="selectOption" />
								<form:option value="0">
									<c:out value="${selectOption}" />
								</form:option>
								<form:options items="${timesZoneList}" />
							</form:select>
						</div>
					</div>
					<div class="row form-group">
						<label><spring:message code="anvizent.package.label.Default" />  :</label>
						<div class="row form-group">
							<spring:message code="anvizent.package.label.jobName"
								var="jobName" />
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.Jobname" /> :</label>
							<div class="col-sm-10">
								<form:input path="jobName" cssClass="form-control jobName"
									data-minlength="1" data-maxlength="45" placeholder="${jobName}"
									readonly="true" />
							</div>

						</div>


						<div class="row form-group fileNames" style="display: none">
							<label class="control-label col-sm-2 jobfile-label"><spring:message
									code="anvizent.package.label.jobFileName" /> : </label>
							<div class="col-sm-10">
								<spring:message code="anvizent.package.label.jobFileName"
									var="jobFileNamePlaceholder" />
								<form:input path="jobfile_names"
									placeHolder="${jobFileNamePlaceholder}"
									cssClass="form-control uploadedJobFileNames" readonly="true" />
							</div>
						</div>



						<div class="jobFilesDiv">
							<c:forEach var="fileNames"
								items="${currencyIntegration.jobFileNames}" varStatus="loop">
								<div class="row form-group fileContainer">
									<c:if test="${loop.index == 0}">
										<label class="control-label col-sm-2 jobfile-label"><spring:message
												code="anvizent.package.label.jobFile" /> : </label>
									</c:if>
									<c:if test="${loop.index > 0}">
										<label class="control-label col-sm-2 jobfile-label"></label>
									</c:if>
									<div class="col-sm-8">
										<label class="checkbox" style="margin-left: 20px;"> <input
											type="checkbox" name="useOldJobFile" class="useOldJobFile"
											checked="checked">
										</label>
										<h5 class="jobFileName" style="margin-left: 20px;">
											<c:out value="${fileNames}" />
										</h5>
										<form:hidden path="jobFileNames[${loop.index}]"
											cssClass="jobFileNames" />
									</div>
									<div class="col-sm-2 add" style="display: none">
										<a href="#" class="btn btn-primary btn-sm addJobFile"> <span
											class="glyphicon glyphicon-plus"></span>
										</a>
									</div>
								</div>
							</c:forEach>
							<c:if test="${empty currencyIntegration.jobFileNames}">
								<div class="row form-group fileContainer">
									<label class="control-label col-sm-2 jobfile-label"><spring:message code="anvizent.package.label.jobFile" /> : </label>
									<div class="col-sm-8">
										<input type="file" class="jobFile" name="jarFiles"
											data-buttonText="Find file">
									</div>
									<div class="col-sm-2">
										<a href="#" class="btn btn-primary btn-sm addJobFile"> <span
											class="glyphicon glyphicon-plus"></span>
										</a>
									</div>
								</div>
							</c:if>
						</div>
					</div>


					<div class="row form-group">
						<label>Client Specific<spring:message code="anvizent.package.label.ClientSpecific" /> :</label>
						<div class="row form-group">
							<spring:message code="anvizent.package.label.jobName"
								var="jobName" />
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.Jobname" />:</label>
							<div class="col-sm-10">
								<form:input path="clientSpecificJobName"
									cssClass="form-control jobName" data-minlength="1"
									data-maxlength="45" placeholder="${jobName}" readonly="true" />
							</div>

						</div>


						<div class="row form-group fileNames" style="display: none">
							<label class="control-label col-sm-2 jobfile-label-CL"><spring:message
									code="anvizent.package.label.jobFileName" /> : </label>
							<div class="col-sm-10">
								<spring:message code="anvizent.package.label.jobFileName"
									var="jobFileNamePlaceholder" />
								<form:input path="clientSpecificJobfile_names"
									placeHolder="${jobFileNamePlaceholder}"
									cssClass="form-control ClientSpecificUploadedJobFileNames"
									readonly="true" />
							</div>
						</div>



						<div class="ClientSpecificJobFilesDiv">
							<c:forEach var="fileNames"
								items="${currencyIntegration.clientSpecificJobFileNames}"
								varStatus="loop">
								<div class="row form-group clientSpecificFileContainer">
									<c:if test="${loop.index == 0}">
										<label class="control-label col-sm-2 jobfile-label-CL"><spring:message
												code="anvizent.package.label.jobFile" /> : </label>
									</c:if>
									<c:if test="${loop.index > 0}">
										<label class="control-label col-sm-2 jobfile-label-CL"></label>
									</c:if>
									<div class="col-sm-8">
										<label class="checkbox" style="margin-left: 20px;"> <input
											type="checkbox" name="useOldJobFile" class="useOldCLJobFile"
											checked="checked">
										</label>
										<h5 class="jobFileName" style="margin-left: 20px;">
											<c:out value="${fileNames}" />
										</h5>
										<form:hidden path="clientSpecificJobFileNames[${loop.index}]"
											cssClass="clientSpecificJobFileNames" />
									</div>
									<div class="col-sm-2 add" style="display: none">
										<a href="#"
											class="btn btn-primary btn-sm clientSpecificAddJobFile">
											<span class="glyphicon glyphicon-plus"></span>
										</a>
									</div>
								</div>
							</c:forEach>
							<c:if
								test="${empty currencyIntegration.clientSpecificJobFileNames}">
								<div class="row form-group clientSpecificFileContainer">
									<label class="control-label col-sm-2 jobfile-label-CL"><spring:message
											code="anvizent.package.label.jobFile" /> : </label>
									<div class="col-sm-8">
										<input type="file" class="clientSpecificJobFile"
											name="clientSpecificJarFiles" data-buttonText="Find file">
									</div>
									<div class="col-sm-2">
										<a href="#"
											class="btn btn-primary btn-sm clientSpecificAddJobFile">
											<span class="glyphicon glyphicon-plus"></span>
										</a>
									</div>
								</div>
							</c:if>
						</div>
					</div>
					<div class="row form-group">
						<div class="col-sm-7">
							<button type="button" class="btn btn-primary btn-sm"
								id="saveCurrencyIntDetails" style="display: none"><spring:message code="anvizent.package.label.Save" /></button>
							<button type="button" class="btn btn-primary btn-sm edit"
								name="id" type="submit" value="${currencyIntList.id}"
								style="display: none"><spring:message code="anvizent.package.label.Edit" /></button>
							<button type="button" class="btn btn-primary btn-sm" id="cancel"
								style='display: none;'><spring:message code="anvizent.package.label.Cancel" /></button>
							</div>
							<div class="col-sm-3">
							<button type="button" class="btn btn-primary btn-sm"
								id="immediateRun" style='display: none; float: right'><spring:message code="anvizent.package.label.InstantRun" /></button>
								</div>
							<div class="col-sm-2">	
								<a href="<c:url value="/admin/currencyIntegration/jobResultsForDefaultCurrencyLoad" />" class="btn btn-primary btn-sm"
								 style=' float: right'> <spring:message code="anvizent.package.label.ViewResults" /></a>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="col-sm-10 runDetails" style="display: none">
			<div class="panel panel-default">
				<div class="panel-heading"><spring:message code="anvizent.package.label.Details" /></div>
				<div class="panel-body">
					<div class="row form-group">
						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.startDate" /></label>
						<div class="col-sm-4">
							<form:input path="StartDate" cssClass="datepicker" id="startDate" />
						</div>
					</div>

					<div class="row form-group">
						<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.EndDate" /></label>
						<div class="col-sm-4">
							<form:input path="endDate" cssClass="datepicker" id="endDate" />
						</div>
					</div>

					<div class="row form-group">
						<button class="btn btn-primary btn-sm" id="run"><spring:message code="anvizent.package.label.Run" /></button>
					</div>
				</div>
			</div>

			<div class="row form-group">
				<a href="<c:url value="/admin/currencyIntegration" var="url"/>"
					class="btn btn-primary back_btn" id="back"><spring:message code="anvizent.package.label.Back" /></a>
			</div>
		</div>
	</form:form>

	<div class="row form-group job-files-div" style='display: none;'>
		<div class="row form-group fileContainer">
			<label class="control-label col-sm-2 jobfile-label"><spring:message
					code="anvizent.package.label.jobFile" /> : </label>
			<div class="col-sm-8">
				<input type="file" class="jobFile" name="jarFiles"
					data-buttonText="Find file">
			</div>
			<div class="col-sm-2">
				<a href="#" class="btn btn-primary btn-sm addJobFile"> <span
					class="glyphicon glyphicon-plus"></span>
				</a> <a href="#"
					class="btn btn-primary btn-sm remove_field deleteJobFile"
					style="display: none;"> <span class="glyphicon glyphicon-trash"></span>
				</a>
			</div>
		</div>
	</div>
	<div class="row form-group job-files-div-CL" style='display: none;'>
		<div class="row form-group clientSpecificFileContainer">
			<label class="control-label col-sm-2 jobfile-label-CL"><spring:message
					code="anvizent.package.label.jobFile" /> : </label>
			<div class="col-sm-8">
				<input type="file" class="clientSpecificJobFile"
					name="clientSpecificJarFiles" data-buttonText="Find file">
			</div>
			<div class="col-sm-2">
				<a href="#" class="btn btn-primary btn-sm clientSpecificAddJobFile">
					<span class="glyphicon glyphicon-plus"></span>
				</a> <a href="#"
					class="btn btn-primary btn-sm remove_field clientSpecificDeleteJobFile"
					style="display: none;"> <span class="glyphicon glyphicon-trash"></span>
				</a>
			</div>
		</div>
	</div>
</div>
