<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
 <spring:htmlEscape defaultHtmlEscape="true" />

<div class="col-sm-12 rightdiv">
	<div class="page-title-v1">
		<h4>
			<spring:message code="anvizent.package.label.standardPackage" />
		</h4>
	</div>
	<div class="dummydiv"></div>
	<ol class="breadcrumb">
	</ol>
	<div class=''>
	<div class="col-sm-12">
		<div id="successOrErrorMessage"></div>
	</div>
		<form:form modelAttribute="standardPackageForm" method="POST"
			id="standardPackageForm">

			<div class="row form-group">
				<h4 class="alignText">
					<spring:message code="anvizent.package.label.standardPackage" />
				</h4>
			</div>
			<jsp:include page="_error.jsp"></jsp:include>
			<form:hidden path="packageId" />
			<form:hidden path="isStandard" />
			<form:hidden path="industryId" />
			<input type="hidden" id="accordianId" />
			<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
			<input type="hidden" id="reloadUrl" value="<c:out value="${tableLevelAccessUrl}"/>">
			<input type="hidden" id="iLId" value="">
			<input type="hidden" id="dLId" value="">
			<div class='col-sm-12'>
				<div class='row form-group packgaelist_standard'>
					<label class="col-sm-3 col-md-3 col-lg-2 control-label labelsgroup">
						<spring:message code="anvizent.package.label.packageName" />
					</label>
					<div class='col-sm-4'>
						<c:choose>
							<c:when test="${not empty standardPackageForm.packageId}">
								<form:input path="packageName" class="form-control"
									disabled="true" />
								<form:errors path="packageName" cssStyle="color: #ff0000" />
							</c:when>
							<c:otherwise>
								<form:input path="packageName" class="form-control" />
								<form:errors path="packageName" cssStyle="color: #ff0000" />
							</c:otherwise>
						</c:choose>

					</div>
					<div class='col-sm-4 hidden'>
					<label
						class="col-sm-3 col-md-3 col-lg-2 control-label labelsgroup ${not empty standardPackageForm.packageId && standardPackageForm.trailingMonths == 0 ? 'hidden':''}">
						<spring:message code="anvizent.package.label.trailingmonths" />
					</label>
					<div class='col-sm-4'>
						<c:choose>
							<c:when test="${not empty standardPackageForm.packageId}">

								<form:input path="trailingMonths"
									class="form-control ${not empty standardPackageForm.packageId && standardPackageForm.trailingMonths == 0 ? 'hidden':''}"
									disabled="true" />
								<form:errors path="trailingMonths" cssStyle="color: #ff0000" />
							</c:when>
							<c:otherwise>
								<select id="tralmonths" name="trailingMonths"
									class="form-control">
									<option value="0"><spring:message
											code="anvizent.package.label.trailingmonths" />
									</option>
									<c:forEach var="tm" begin="1" end="20">
										<option value="${tm*6}">${tm*6}</option>
									</c:forEach>
								</select>
								<form:errors path="trailingMonths" cssStyle="color: #ff0000" />
							</c:otherwise>
						</c:choose>

					</div>
				</div>

				</div>
				<div class="col-md-4">
					<c:choose>
						<c:when test="${empty standardPackageForm.packageId}">
							<input type="submit"
								class="btn btn-primary btn-sm btn-primary startLoader"
								value="<spring:message code="anvizent.package.label.createPackage"/>"
								id="createStandardPackage" name='createStandardPackage' />

							<a href='<c:url value="/adt/standardpackage"/>'
								class="btn btn-primary btn-sm back_btn startLoader"><spring:message
									code="anvizent.package.link.back" /></a>
						</c:when>
					</c:choose>
				</div>
			</div>
                                      		
			<div class="col-md-12 message-class">
				<div id="successorfailure"></div>
			</div>
			<c:if test="${not empty standardPackageForm.packageId}">
				<div class='row form-group'>
					<c:if test="${empty userDlList}">
						<div class="alert alert-warning" role="alert">
							<span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
							<spring:message
								code="anvizent.package.message.modulesAreNotAssignedPleaseContactSuperAdmin" />
						</div>
					</c:if>
					<c:if test="${not empty userDlList}">
						<c:choose>
							<c:when
								test="${empty mappedModuleSatandardPackage && packageCreation == false}">
								<div class="alert alert-warning" role="alert">
									<span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
									<spring:message
										code="anvizent.package.standardPackage.NoModuleMapped" />
								</div>
							</c:when>
							<c:when
								test="${not empty mappedModuleSatandardPackage && packageCreation == false}">
								<div class="row form-group">
									<div class='col-sm-12'>
										<div id="accordion-first" class="clearfix">
											<div class="accordion" id="accordion2">
												<div class="accordion-group">
													<div class="accordion-heading"
														style="border: 1px solid #dce1e4;">
														<a class="accordion-toggle collapsed"
															data-toggle="collapse" data-parent="#accordion2"
															href="#collapseOne"> <span
															class="glyphicon glyphicon-plus-sign"></span> <spring:message
																code="anvizent.package.label.mappedModulesDataSets" />
														</a>
													</div>
													<div style="height: 0px;" id="collapseOne"
														class="accordion-body collapse">
														<div class="accordion-inner"></div>
														<div class='row form-group'>

															<div class="table-responsive" style="overflow-y:overlay; max-height: 400px;">
																<table
																	class="table table-striped table-bordered tablebg" style="table-layout: fixed;">
																	<thead>
																		<tr>
																			<th><spring:message
																					code="anvizent.package.label.sNo" /></th>
																			<th><spring:message
																					code="anvizent.package.label.moduleName" /></th>
																			<th><spring:message
																					code="anvizent.package.label.inputLayout" /></th>
																			<th></th>
																		</tr>
																	</thead>
																	<tbody>

																		<c:forEach items="${mappedModuleSatandardPackage}"
																			var="mappedModuleSatandardPackage"
																			varStatus="increment">
																			<tr>
																				<td><c:out value="${increment.index+1}" /></td>
																				<td><c:out
																						value="${mappedModuleSatandardPackage.dlInfo.dL_name}" /></td>
																				<td><c:out
																						value="${mappedModuleSatandardPackage.ilInfo.iL_name}" /></td>
																				<td><a class="btn btn-primary btn-sm"
																					href='<c:url value="/adt/package/viewIlSource/${standardPackageForm.packageId}/${mappedModuleSatandardPackage.ilConnectionMapping.dLId}/${mappedModuleSatandardPackage.ilInfo.iL_id}?from=standard" />'><spring:message
																							code="anvizent.package.link.viewSourceDetails" />
																				</a></td>
																			</tr>

																		</c:forEach>
																	</tbody>
																</table>
															</div>


														</div>
													</div>
												</div>
											</div>

										</div>
										<!-- end accordion -->
									</div>

								</div>

							</c:when>
						</c:choose>
						<div class='col-sm-12'>
							<h3>
								<spring:message code="anvizent.package.label.modules" />
							</h3>
							<div class="table-responsive multi-tbl">
								<table
									class="table table-striped table-bordered tablebg table main-tbl"
									id="DLSectionTable">
									<thead class="multi-tbl-header">
										<tr>
											<th class="multi-tbl-cnts col-xs-1 text-center"><spring:message
													code="anvizent.package.label.sNo" /></th>
											<th class="multi-tbl-cnts col-xs-2"><spring:message
													code="anvizent.package.label.moduleName" /></th>
											<th class="multi-tbl-cnts col-xs-2"><spring:message
													code="anvizent.package.label.kPI" /></th>
											<th class="multi-tbl-cnts col-xs-2"><spring:message
													code="anvizent.package.label.tableName" /></th>
											<th class="multi-tbl-cnts col-xs-5">&nbsp;</th>
											
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${userDlList}" var="dLInfo" varStatus="loop">
											<c:choose>
												<c:when test="${dLInfo.isLocked == false}">
													<tr class="accordion-toggle dl-block">
														<td class="main-tbl-row col-xs-1"><c:out
																value="${dLInfo.dL_id}" /></td>
														<td class="main-tbl-row col-xs-2"><label
															class="radio-inline toolTip" data-toggle="tooltip"
															data-placement="bottom"
															title="<c:out value="${dLInfo.description}"/>"
															style="padding-left: 0px;">
																<button type="button" data-toggle="collapse"
																	data-target=".il-inner-block${loop.index}"
																	class="inner-tbl dlSelection module_expand"
																	data-dLId='<c:out value="${dLInfo.dL_id}"/>'
																	data-dLName='<c:out value="${dLInfo.dL_name}"/>'
																	data-scheduletype="<c:out value="${runWithSchedulerOrRunnow}"/>"
									 								data-scheduleid='<c:out value="${dLInfo.schedule.scheduleId}"/>'>
																	<i class="inner-tbls fa fa-caret-down"
																		aria-hidden="true"></i>
																</button> <c:out value="${dLInfo.dL_name}" />
														</label></td>
														<td class="main-tbl-row col-xs-2"><c:choose>
																<c:when test="${not empty dLInfo.kpi}">
																	<c:forEach var="kpi" items="${dLInfo.kpi}">
																		<label class="" for="${kpi}"> <c:if
																				test="${principal.isTrailUser == true}">
																				<c:if test="${kpi == 'Returns'}">
																					<img
																						src="<c:url value="/resources/images/lock.png"/>"
																						class="" alt="Responsive image"
																						style="opacity: 0.3;">
																				</c:if>
																			</c:if> <c:if test="${principal.isTrailUser == false}">
																				<c:if test="${kpi == 'Returns'}">
																					<input type="checkbox" checked="checked"
																						disabled="disabled">
																				</c:if>
																			</c:if> <c:if test="${kpi != 'Returns'}">
																				<input type="checkbox" checked="checked"
																					disabled="disabled">
																			</c:if> <span style="font-weight: normal;"><c:out
																					value="${kpi}" /></span>
																		</label>
																		<br>
																	</c:forEach>
																</c:when>
															</c:choose></td>
														<td class="main-tbl-row col-xs-2"><c:out
																value="${dLInfo.dL_table_name}" /></td>
														<td class="main-tbl-row col-xs-5">
															<div class="row">
																<div class="col-md-12">
																	<button type='button'
																class='runDL btn btn-primary btn-xs'
																data-dLId='<c:out value="${dLInfo.dL_id}"/>'
																data-dLName='<c:out value="${dLInfo.dL_name}"/>'
																data-scheduletype="<c:out value="${runWithSchedulerOrRunnow}"/>"
									 							data-scheduleid='<c:out value="${dLInfo.schedule.scheduleId}"/>'>
																<spring:message code="anvizent.package.label.run" />
															</button>
															
														<c:choose>
															   <c:when test="${dLInfo.schedule.scheduleId == 0}">
															     <button type='button'
																	class='scheduleDL  btn btn-primary btn-xs'
																	data-dLId='<c:out value="${dLInfo.dL_id}"/>' data-dLName='<c:out value="${dLInfo.dL_name}"/>'
																	data-packgeid="0" data-packagetype="standard" data-packgename="Standard Package" data-isclientdbtables="false" 
																	data-industryid="${dLInfo.industry.id}" data-scheduleId ='<c:out value="${dLInfo.schedule.scheduleId}"/>' data-reSchedule="reRun" ${principal.isSandBox == false ? '' : 'disabled'}>
																	<spring:message code="anvizent.package.label.schedule" /> 
															     </button>
															    </c:when>
															  <c:otherwise>
															  <c:if test="${principal.isSandBox == false}">
															  <c:if test="${not empty dLInfo.schedule.scheduleRecurrence}">
																    <c:set var ="recurrence">
																			<spring:message code="anvizent.package.label.recurrence"/>
																	</c:set>
																	<c:set var ="starttime">
																			<spring:message code="anvizent.package.label.startTime"/>
																	</c:set>
																	<c:set var ="endtime">
																			<spring:message code="anvizent.package.label.endTime"/>
																	</c:set>
																	<c:set var ="range">
																			<spring:message code="anvizent.package.label.range"/>
																	</c:set>
																	<c:set var ="timeZone">
																			<spring:message code="anvizent.package.label.timeZone"/>
																	</c:set>
																	<c:set var="test" value="<b>${recurrence}</b> ${dLInfo.schedule.scheduleRecurrence}<br>
																		<b>${starttime}</b> ${dLInfo.schedule.scheduleStartTime } <br>
																		<b>${range}</b> ${dLInfo.schedule.scheduleRange} <br>
																		<b>${timeZone }</b> ${dLInfo.schedule.timeZone }">
																	</c:set>
																</c:if>
															  </c:if>
															  
															  
															    <c:if test="${dLInfo.schedule.scheduleType == 'UNSCHEDULED' }">
															      <button type='button'
																	class='scheduleDL tool btn btn-primary btn-xs'
																	data-dLId='<c:out value="${dLInfo.dL_id}"/>' data-dLName='<c:out value="${dLInfo.dL_name}"/>'
																	data-packgeid="0" data-packagetype="standard" data-packgename="Standard Package" data-isclientdbtables="false" 
																	data-industryid="${dLInfo.industry.id}" data-scheduleId ='<c:out value="${dLInfo.schedule.scheduleId}"/>' data-reSchedule="reRun" ${principal.isSandBox == false ? '' : 'disabled'}>
																	<spring:message code="anvizent.package.label.schedule" />
															     </button>
															    </c:if>
															    <c:if test="${dLInfo.schedule.scheduleType != 'UNSCHEDULED' }">
																	 <button type='button'
																		 class='scheduleDL tool btn btn-primary btn-xs'
																		 data-dLId='<c:out value="${dLInfo.dL_id}"/>' data-dLName='<c:out value="${dLInfo.dL_name}"/>'
																		 data-packgeid="0" data-packagetype="standard" data-packgename="Standard Package" data-isclientdbtables="false" 
																		 data-industryid="${dLInfo.industry.id}" data-scheduleId ='<c:out value="${dLInfo.schedule.scheduleId}"/>' 
																		 data-reSchedule="reRun" title="<c:out value="${test}"/>" ${principal.isSandBox == false ? '' : 'disabled'}>
																		<spring:message code="anvizent.package.link.reSchedule"/>
																     </button>
																	 <c:if test="${dLInfo.schedule.scheduleRecurrence != 'once'}">
																		<button type='button'
																			class='unScheduleDL tool btn btn-primary btn-xs'
																			data-dLId='<c:out value="${dLInfo.dL_id}"/>' data-dLName='<c:out value="${dLInfo.dL_name}"/>'
																			data-packgeid="0" data-scheduleId ='<c:out value="${dLInfo.schedule.scheduleId}"/>' ${principal.isSandBox == false ? '' : 'disabled'}>
																		  <spring:message code="anvizent.package.link.unschedule" /> 
																	    </button>
																	 </c:if>
															     </c:if>
															  </c:otherwise>
														</c:choose>
															 <a class='btn btn-primary btn-xs' href="<c:url value="/adt/standardpackage/viewjobResults/${dLInfo.dL_id}?source=standardPacakge&dl_Name=${dLInfo.dL_name}"/>">
														       <spring:message code="anvizent.package.label.viewResults"/></a>
														     <a class='btn btn-primary btn-xs' href ="<c:url value="/adt/standardpackage/executionResultsByPagination/${dLInfo.dL_id}?dlName=${dLInfo.dL_name}&offset=0&limit=10"/>">
														       <spring:message code="anvizent.package.label.viewExecutionResults"/></a>
														       <button type='button'
																class='viewDLSchema btn btn-primary btn-xs'
																data-dLId='<c:out value="${dLInfo.dL_id}"/>'
																data-dL_name='<c:out value="${dLInfo.dL_table_name}"/>'>
																<spring:message	code="anvizent.package.button.viewTableStructure" />
															</button>
																</div>
																
															</div>
															<br />
															
															<div class="row form-group">
																<label class="col-sm-4 col-md-4 col-lg-4 control-label labelsgroup"><spring:message code="anvizent.package.label.trailingmonths" />
																</label>
																<div class='col-sm-4 col-md-4 col-lg-2 col-xs-3 no-pad'>
																	<select id="tralmonths" name="trailingMonths"
																			class="form-control trialmonths">
																			<option value="6">6</option>
																			<c:forEach var="tm" begin="2" end="20">
																				<c:choose>
																					<c:when test="${tm*6 == dLInfo.trailingMonths}">
																						<option value="${tm*6}" selected="selected">${dLInfo.trailingMonths}</option>
																					</c:when>
																					<c:otherwise>
																						<option value="${tm*6}">${tm*6}</option>
																					</c:otherwise>
																				</c:choose>
																			</c:forEach>
																	</select>
																</div>
																<div class='col-sm-2'>
																<c:choose>
																	<c:when test="${dLInfo.trailingMonths == '0'}">
																		<button type="button" data-dLId='<c:out value="${dLInfo.dL_id}"/>' class="btn btn-primary saveTrailingDlMap">
																			Save
																		</button>
																	</c:when>
																	<c:otherwise>
																		<button type="button" data-dLId='<c:out value="${dLInfo.dL_id}"/>' class="btn btn-primary updateTrailingDlMapId">
																			Update 
																		</button>
																	</c:otherwise>
																</c:choose>
																</div>
															</div>
														</td>
													</tr>
													<tr class="il-block">
														<td colspan="5" class="hiddentablerow col-xs-12">
															<div
																class="accordian-body collapse il-inner-block il-inner-block<c:out value="${loop.index}"/>">
																<button type="button" class="close close-accordian"
																	aria-label="Close">
																	<span aria-hidden="true">&times;</span>
																</button>
																<div
																	class="table-responsive table-inner-1 table-inner-arrow">
																	<table class="table table-inner-1">
																		<thead class="multi-tbl-inner-header">
																			<tr>
																				<th class="multi-tbl-cnts col-xs-1"><spring:message
																						code="anvizent.package.label.sNo" /></th>
																				<th class="multi-tbl-cnts col-xs-2"><spring:message
																						code="anvizent.package.label.inputLayout" /></th>
																				<th class="multi-tbl-cnts col-xs-2"><spring:message
																						code="anvizent.package.label.tableName" /></th>
																				<th class="multi-tbl-cnts col-xs-2"><spring:message
																						code="anvizent.package.label.status" /></th>
																				<th class="multi-tbl-cnts col-xs-1">&nbsp;</th>
																				<th class="multi-tbl-cnts col-xs-1">&nbsp;</th>
																				<th class="multi-tbl-cnts col-xs-1">&nbsp;</th>
																				<th class="multi-tbl-cnts col-xs-1">&nbsp;</th>
																			
																			</tr>
																		</thead>
																		<tbody class="multi-tbl-inner-body">

																		</tbody>
																	</table>
																</div>
															</div>
														</td>
													</tr>
												</c:when>
												<c:otherwise>
													<tr>
														<td><c:out value="${dLInfo.dL_id}" /></td>
														<td colspan="4"><label
															class="radio-inline toolTip disable-text"
															class='btn btn-primary btn-sm' data-toggle="tooltip"
															data-placement="bottom"
															title="<c:out value="${dLInfo.description}"/>"> <input
																type="radio" name="DLSelection" value=""
																class='dlSelection' disabled="disabled">
															<c:out value="${dLInfo.dL_name}" />
														</label> <img src="<c:url value="/resources/images/lock.png"/>"
															class="img-responsive lock-symbol" alt="Responsive image">
														</td>
													</tr>

												</c:otherwise>
											</c:choose>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</c:if>
					<div class="modal fade" tabindex="-1" role="dialog" id="iLPopUp"
						data-backdrop="static" data-keyboard="false">
						<div class="modal-dialog" style="width: 65%;">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close close-popup"
										data-dismiss="modal" aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
									<h4 class="modal-title custom-modal-title" id="dl_Name"></h4>
								</div>
								<div class="modal-body"
									style="max-height: 400px; overflow-x: auto;">
									<div class="table-responsive">
										<div class="alert alert-danger" role="alert"
											id="ILpopUpMesssage" style="display: none;"></div>
										<table class="table table-striped table-bordered tablebg"
											id="ILSectionTable">
											<thead>
												<tr>
													<th><spring:message code="anvizent.package.label.sNo" /></th>
													<th><spring:message
															code="anvizent.package.label.inputLayout" /></th>
													<th><spring:message
															code="anvizent.package.label.tableName" /></th>
													<th><spring:message
															code="anvizent.package.label.status" /></th>
													<th></th>
													<th></th>
													<th></th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default close-popup"
										data-dismiss="modal">
										<spring:message code="anvizent.package.button.close" />
									</button>
								</div>
							</div>
							<!-- /.modal-content -->
						</div>
						<!-- /.modal-dialog -->
					</div>
					<div class="pull-left col-sm-12">
						<c:choose>
							<c:when test="${standardPackageForm.pageMode == 'schedule'}">
								<a type="reset" href="<c:url value="/adt/package/schedule"/>"
									class="btn btn-primary btn-sm back_btn"><spring:message
										code="anvizent.package.link.back" /></a>
							</c:when>
							<c:otherwise>
								<a href="<c:url value="/adt/standardpackage" />"
									class="btn btn-primary btn-sm back_btn"><spring:message
										code="anvizent.package.link.back" /></a>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</c:if>

			<div class="row form-group"></div>

		</form:form>
		<div class="modal fade" tabindex="-1" role="dialog" id="viewSchema"
			data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog" style="width: 65%;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />"
							class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title" id="viewSchemaHeader"></h4>
					</div>
					<div class="modal-body"
						style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
						<div class="table-responsive">
							<table class="table table-striped table-bordered tablebg table-fixed"
								id="viewSchemaTable">
								<thead>
									<tr>
										<th class="col-xs-1"><spring:message code="anvizent.package.label.sNo" /></th>
										<th class="col-xs-2"><spring:message
												code="anvizent.package.label.columnName" /></th>
										<th class="col-xs-2"><spring:message
												code="anvizent.package.label.dataType" /></th>
										<th class="col-xs-2"><spring:message
												code="anvizent.package.label.columnSize" /></th>
										<th class="col-xs-1"><spring:message code="anvizent.package.label.pk" /></th>
										<th class="col-xs-1"><spring:message code="anvizent.package.label.nn" /></th>
										<th class="col-xs-2"><spring:message code="anvizent.package.label.ai" /></th>
										<th class="col-xs-1"><spring:message code="anvizent.package.label.default" /></th>
									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>

		<div class="modal fade" tabindex="-1" role="dialog"
			id="viewSourceDetailsPoUp" data-backdrop="static"
			data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="viewSourceDetailsPoUpHeader"></h4>
					</div>
					<div class="modal-body"></div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>

		<div class="modal fade" tabindex="-1" role="dialog"
			id="show_ILConnectionDataBaseInfo" data-backdrop="static"
			data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title"></h4>
					</div>
					<div class="modal-body">
						<div class='row form-group '>
							<div class='col-sm-4'>
								<spring:message code="anvizent.package.label.connectionName" />
								:
							</div>
							<div class='col-sm-8'>
								<input type="text" class="form-control" id="show_connectionName"
									disabled="disabled">
							</div>
						</div>
						<div class='row form-group '>
							<div class='col-sm-4'>
								<spring:message code="anvizent.package.label.connectorType" />
								:
							</div>
							<div class='col-sm-8'>
								<input type="text" class="form-control" id="show_databaseType"
									disabled="disabled">
							</div>
						</div>
						<div class='row form-group '>
							<div class='col-sm-4'>
								<spring:message code="anvizent.package.label.connectionType" />
								:
							</div>
							<div class='col-sm-8'>
								<input type="text" class="form-control" id="show_connectionType"
									disabled="disabled">
							</div>
						</div>
						<div class='row form-group '>
							<div class='col-sm-4'>
								<spring:message code="anvizent.package.label.serverName" />
								:
							</div>
							<div class='col-sm-8'>
								<input type="text" class="form-control" id="show_serverName"
									disabled="disabled">
							</div>
						</div>
						<div class='row form-group '>
							<div class='col-sm-4'>
								<spring:message code="anvizent.package.label.userName" />
								:
							</div>
							<div class='col-sm-8'>
								<input type="text" class="form-control" id="show_userName"
									disabled="disabled">
							</div>
						</div>
						<div class='row form-group '>
							<div class='col-sm-4'>
								<spring:message code="anvizent.package.label.typeOfCommand" />
								:
							</div>
							<div class='col-sm-8'>
								<input type="text" class="form-control" id="show_typeOfCommand"
									disabled="disabled">
							</div>
						</div>
						<div class='row form-group '>
							<div class='col-sm-4'></div>
							<div class='col-sm-8'>
								<textarea class="form-control" rows="6" id="show_queryScript"
									disabled="disabled"></textarea>
								<input type="text" class="form-control" id="show_procedureName"
									disabled="disabled" style="display: none;">
							</div>
						</div>
						<div class="row form-group" id="procedureParametersDiv">
							<div class="col-sm-12">
								<label style="font-weight: normal;"> <spring:message
										code="anvizent.package.label.Parameters" />
								</label>
							</div>
							<div class="col-sm-12">
								<div class="row form-group show-param-hidden"
									style="display: none;">
									<div class="col-sm-2">
										<spring:message code="anvizent.package.label.name" />
									</div>
									<div class="col-sm-4">
										<input type="text" class="show-param-name form-control"
											disabled="disabled">
									</div>
									<div class="col-sm-2">
										<spring:message code="anvizent.package.label.value" />
									</div>
									<div class="col-sm-4">
										<input type="text" class="show-param-value form-control"
											disabled="disabled">
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>

		<div class="modal fade" tabindex="-1" role="dialog"
			id="deleStandardSourceFileAlert" data-backdrop="static"
			data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">
							<spring:message
								code="anvizent.package.label.modalHeader.deleteSource" />
						</h4>
					</div>
					<div class="modal-body">
						<p>
							<spring:message
								code="anvizent.package.message.deleteSource.areYouSureYouWantToDeleteSource" />
							&hellip;
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary"
							id="confirmDeleteStandardSource">
							<spring:message code="anvizent.package.button.yes" />
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.no" />
						</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>

		<div class="modal fade" tabindex="-1" role="dialog"
			style='overflow-y: auto; max-height: 90%;'
			id="viewDeatilsPreviewPopUp" data-backdrop="static"
			data-keyboard="false" aria-hidden='true'>
			<div class="modal-dialog" style="width: 90%;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="viewDeatilsPreviewPopUpHeader"></h4>
					</div>
					<div class="modal-body table-responsive"
						style="max-height: 400px; overflow-y: auto; overflow-x: auto;">
						<table
							class='viewDeatilsPreview table table-striped table-bordered tablebg'></table>
					</div>
					<div class="modal-footer">
						<button type="button"
							class="btn btn-default viewDetailsclosePreview"
							data-dismiss="modal">
							<spring:message code="anvizent.package.button.close" />
						</button>
					</div>
				</div>
			</div>
		</div>

		<div class="modal fade" role="dialog"	id="runDLSourcesPopup" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						 <h4 class="modal-title custom-modal-title dlDialogHeader"></h4>
						 <input id= "hiddenDlId"  type="hidden">
					</div>
					<div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
					

					
					<div id="accordion-first11" class="clearfix">
											<div class="accordion" id="accordion22">
												
											</div>

										</div>

					
					 <div id="accordionadv" role="tablist" aria-multiselectable="true">
						<div class="panel panel-default" style="border:none;">
								
								<div id="collapseAdv" class="panel-collapse collapse in collapseAndExpandDiv" role="tabpanel">
								<div class="ils-accordian-outer acc-col" style='display: none;'>
			<input type="checkbox" name="dlSourceCheck" class="dlSourceCheck">
			<div id="accordion-first-clone" class="clearfix">
				<div class="accordion" id="accordionp">
				
				
				<div class="accordion-group">
													<div class="accordion-heading">
														<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion22" href="#collapseOneg" aria-expanded="false">
														<label class="ilName"></label>
														 <span class="glyphicon glyphicon-plus-sign pull-right"></span> 
													
														</a>
													</div>
												
													
												<div id="collapseg" class="row accordion-body collapse" style="height: 0px;" aria-expanded="false">
							<div class="panel panel-info col-md-12" style="border: none;">
								<div class="panel-body ilSourceParentDiv">
									
								</div>
							</div>
						</div>	
													
													
												</div>
				
				
				
				
				
				
				
					 
						
				</div>
			</div>
		</div>
								    <div class = "row form-group selectAllDiv col-md-8 div-margins">
								      <input type="checkbox" name="selectAllCheckBoxName" class="selectAllCheckBoxClass">
					  			      <span class="ilName">Select All</span>
								    </div>
									<div class='row  form-group runDlSourcesDiv col-md-8'>
									  
									</div>
									<div class='row form-group runDLDiv col-md-4'>
									    <div>
											<label class="control-label ">
												<spring:message code = "anvizent.package.label.isDlExecutionRequired"/>
											</label>
										</div>
										<div>
										    <label class="radio-inline">
										    	<input type="radio" name="dlexecutionRequired" value="true" class='dlexecutionRequired'>
										    	<spring:message code="anvizent.package.label.yes"/>
										    </label>
								    		<label class="radio-inline">
									    		<input type="radio" name="dlexecutionRequired" value="false" class='dlexecutionRequired'>
								    			<spring:message code="anvizent.package.button.no"/>
								    		</label>
										</div>
										<div class = "row form-group selectAllDDLDiv col-md-8 div-margins">
									      <input type="checkbox" name="selectAllDDlsname" class="selectAllDDls">
									      <span class="allddl">Select All</span>
									    </div>
										<div class="ddLayoutParentDiv div-margins col-md-12" style="display:none;">
										</div>
									</div>
								</div>
							 </div>
						</div>
					</div>
					<div class="modal-footer">
					   <button type="button" class="btn btn-primary" id="confirmDlRun"><spring:message code="anvizent.package.button.ok"/></button>
					   <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					</div>
				</div>
			</div>
		</div>
		
		
		
		
		
		
		
		
		<div class='row form-group ilSourceChild' style='display: none;'>
			<input type='checkbox' name='ilSourceCheck' class='ilSourceCheck' />
			<span id="ilSourceName"></span>
		</div>
		<div class="row form-group ddLayoutChild" style='display: none;'>
			<input type='checkbox' name='ddlnameCheck' class='ddlClassCheck' />
			<span id="ddlName"></span>
		</div>

		<div>
		</div>
		
		<div class="modal fade" role="dialog"	id="runIlPopup" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						 <h4 class="modal-title custom-modal-title runIlHeader"></h4>
						 <input id= "hiddenDlIdforRunIl"  type="hidden">
						 <input id= "hiddenIlIdforRunIl"  type="hidden">
					</div>
					<div class="modal-body" style="max-height: 300px; overflow-y: auto; overflow-x: hidden;">
						<div id="accordion" role="tablist" aria-multiselectable="true">
						   <div class="panel panel-default">
							  <div class="panel-heading accordion-heading" role="tab">
								<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse1" aria-expanded="true" class="tablebuttons" style="text-decoration: none;">
								   <spring:message code = "anvizent.package.label.advancedOptions"/>
								</a>
							  </div>
							  <div id="collapse1" class="panel-collapse collapse in collapseAndExpandDiv" role="tabpanel">
							    <input type="checkbox" class="selectAllILCheckBoxClass" ><span class="ilName">Select All</span>
							   <div class='row form-group runIlOptionsDiv div-margins'></div>
							    
							  </div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
					   <button type="button" class="btn btn-primary" id="confirmIlRun"><spring:message code="anvizent.package.button.ok"/></button>
					   <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row form-group ilSources-outer" style='display: none;'>
			<input type="checkbox" name="sourceCheckName" class="sourceCheckClass">
			<span class="sourceName"></span>
		</div>
		
		
		<!-- Schedule Popup div -->
		
		<div class="modal fade" role="dialog" id="scheduleStandardPackagePopUp" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog" style="width: 60%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title" id="schedulePackagePopUpHeader" style="word-break: break-all;white-space: normal;"></h4>
				</div>
				<div class="modal-body">
					<div class='row form-group hidden'>
						<div class='col-sm-3'>
							<label class="radio-inline"><input type="radio" name="runNowOrSchedule"  class="checkedFlaseTrue" value="runnow"  >
							<spring:message code="anvizent.package.label.runNow"/></label>
						</div>
						<c:if test="${principal.isTrailUser == false }">
							<div class='col-sm-3 scheduleRerun' >
								<label class="radio-inline"><input type="radio" name="runNowOrSchedule" class="checkedFlaseTrue" value="schedule">
								<spring:message code = "anvizent.package.label.schedule"/></label>
							</div>
						</c:if>
						<c:if test="${principal.isTrailUser == true }">
							<div class='col-sm-3 scheduleRerun' >
								<label class="radio-inline"><input type="radio" disabled="disabled" name="runNowOrSchedule" class="checkedFlaseTrue" value="schedule">
								<spring:message code = "anvizent.package.label.schedule"/> <img src="<c:url value="/resources/images/lock.png"/>" class="img-responsive lock-symbol" alt="Responsive image"></label>
							</div>
						</c:if>
						<div class='col-sm-3'>
							<label class="radio-inline"><input type="radio" name="runNowOrSchedule"  class="checkedFlaseTrue" value="runwithscheduler"  >
							<spring:message code="anvizent.package.lable.runwithScheduler"/></label>
						</div>
						<div class='col-sm-6'>
							 <div id='runNowOrSchedulevalidation'></div>
						</div>
					</div>
					
				<div id="scheduleOptionsDiv" style="display:none;"> 
					<div class="panel panel-info" id='RecurrencePattern'
						style="margin-bottom: 5px;">
						<div class="panel-heading"><spring:message code = "anvizent.package.label.recurrencePattern"/></div>
						<div class="panel-body">
							<div id='recurrencePatternValidation'>
							<div class="row">
								<div class="col-md-3">
								    <div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="minutes" value="minutes"> <spring:message code = "anvizent.package.label.minutes"/>
										</label>
									</div>
									<div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="hourly" value="hourly"> <spring:message code = "anvizent.package.label.hourly"/>
										</label>
									</div>
									<div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="daily" value="daily"> <spring:message code = "anvizent.package.label.daily"/>
										</label>
									</div>
									
									<div class="radio">
										<label> <input type="radio"  name="recurrencePattern"
											id="weekly" value="weekly"><spring:message code = "anvizent.package.label.weekly"/>
										</label>
									</div>
									<div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="monthly" value="monthly"> <spring:message code = "anvizent.package.label.monthly"/>
										</label>
									</div>
									<div class="radio">
										<label> <input type="radio"   name="recurrencePattern"
											id="yearly" value="yearly"> <spring:message code = "anvizent.package.label.yearly"/>
										</label>
									</div>
									
								</div>
								<div class="col-md-9" id="showRecurrenceOptions">
								
								   <div id='minutesPatternValidation' style="display: none;">
										<div class='row form-group minutesValidation'>
											<spring:message code = "anvizent.package.label.recurEvery"/> 
											<select id="minutesToRun">
												<c:forEach var="i" begin="15" end="30">
													<option value="${i}">${i}</option>
												</c:forEach>
											</select>
										</div>
									</div>
								
									<div id="hourlyRecurrencePattern" style="display: none;">
										<div class='row form-group'>
										    <div class="col-sm-10">
										    	<input type="radio" name="hourlyRadios" id="everyhourlyRadios" value="every"/>
												<spring:message code = "anvizent.package.label.every"/> <input type="text" id="everyhour" style="width: 50px;" id="hourlyToRun" value=""/>
												<spring:message code = "anvizent.package.label.hours"/>
										    </div>
										</div>
										
										<div class='row form-group'>
										    <div class="col-sm-3">
										    	<input type="radio" name="hourlyRadios" id="selectedhourlyRadios" value="selected"/>
											<spring:message code = "anvizent.package.label.selecthours"/>
										    </div>
										    <div class="col-sm-8"> 
											<select id="selectedhours"  multiple="multiple" style="white-space:pre-wrap;width: 300px;">
											<c:forEach var="tm" begin="0" end="23">
													<option  value="${tm}">${tm}</option>
											</c:forEach>
											</select>
											</div>
										</div>

										<div id='hourlyRecurrencePatternVaLidation'></div>
										
									</div>
								
									<div id="dailyRecurrencePattern">
									</div>
									<div id="weeklyRecurrencePattern" style="display: none;">
									<div id='weeklyRecurrencePatternValidation'>
										<div class='row form-group'>
											<spring:message code = "anvizent.package.label.recurEvery"/> <input type="text" style="width: 50px;" id="weeksToRun" value="1"/>
											<spring:message code = "anvizent.package.label.weeksOn"/>
										</div>
										<div class='row form-group'>
											<input type="hidden" id="daysToRun"/>
											<c:forEach items="${weekdays}" var="weekday">
												<label class="checkbox-inline"> <input type="checkbox"  name="weekDayCheckBox" value="${fn:toUpperCase(weekday)}"> ${weekday}
												</label>
											</c:forEach>
										</div>
										</div>
									</div>
									<div id="monthlyRecurrencePattern" style="display: none;">
										<div class="radio">
											<label> <input type="radio" name="monthlyRadios"
												id="" value="monthlyOption_first" checked>
												<spring:message code = "anvizent.package.label.day"/> 
												<select class="" id="dayOfMonth">
														<c:forEach items="${daysOfMonth}" var="entry">
															<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
														</c:forEach>
												</select>
												<spring:message code = "anvizent.package.label.ofEvery"/> 
												<select id="monthsToRun">
													<option value="1">1</option>
													<option value="2">2</option>
													<option value="3">3</option>
													<option value="4">4</option>
													<option value="5">5</option>
													<option value="6">6</option>
												</select> 
												<spring:message code = "anvizent.package.label.months"/>
											</label>
											<p class="help-block"><em><spring:message code = "anvizent.package.label.noteIfSelectedDayIsNotAvailableForAMonthLastDayOfThatMonthWillBeConsidered"/></em></p>
										</div>
									</div>
									<div id="yearlyRecurrencePattern" style="display: none;">
										<div class='row form-group hide'>
											<spring:message code = "anvizent.package.label.recurEvery"/> <input type="text" style="width: 50px;" id="yearsToRun" value="1"/>
											<spring:message code = "anvizent.package.label.years"/> 
										</div>
										<div class='row form-group'>
											<div class="radio">
												<label> <input type="radio" name="yearlyRadios"
													id="" value="yearlyOptions_first" checked>
													<spring:message code = "anvizent.package.label.on"/>    <select class="" id="monthOfYear">
																<c:forEach items="${monthsOfYear}" var="monthOfYear">
																	<option value="<c:out value="${monthOfYear.key}"/>"><c:out value="${monthOfYear.value}"/></option>
																</c:forEach>
															</select>                
													<select class="" id="dayOfYear">
														<c:forEach items="${daysOfMonth}" var="entry">
															<option value="<c:out value="${entry.key}"/>"><c:out value="${entry.value}"/></option>
														</c:forEach>
												</select>
												</label>
												<p class="help-block"><em><spring:message code = "anvizent.package.label.noteIfSelectedDayIsNotAvailableForAMonthLastDayOfThatMonthWillBeConsidered"/></em></p>
											</div>
										</div>
									</div>
								</div>
							</div>
							</div>
						</div>
					</div>
					<div class="panel panel-info" id='scheduleTime'>
						<div class="panel-heading"><spring:message code = "anvizent.package.label.scheduleTime"/></div>
						<div class="panel-body">
							<div class='row' style="margin-bottom: 5px;">
							    <div id='scheduleStartDateValidation'>
								<div class='col-sm-3'><spring:message code = "anvizent.package.label.startDate"/>:</div>
								<div class='col-sm-3'>
									<input type="text" class="datepicker" style="width: 100px;" id="scheduleStartDate"/>
								</div>
								</div>
							</div>
							<div class='row' style="margin-bottom: 5px;">
								<div class='col-sm-3'><spring:message code = "anvizent.package.label.startTime"/></div>
								<div class='col-sm-4'>
									<select class="" id="scheduleStartHours">
										<c:forEach items="${hours}" var="hour">
											<option value="<c:out value="${hour}"/>"><c:out value="${hour}"/></option>
										</c:forEach>
									</select>
									<select class="" id="scheduleStartMinutes">
										<c:forEach items="${minutes}" var="minute">
											<option value="<c:out value="${minute}"/>"><c:out value="${minute}"/></option>
										</c:forEach>
									</select>
								</div>
							</div>

						</div>
					</div>
					<div class="panel panel-info" id='RangeofRecurrence'	style="margin-top: -15px;">
						<div class="panel-heading"><spring:message code = "anvizent.package.label.rangeOfRecurrence"/></div>
						<div class="panel-body">
						<div id='rangeofRecurrenceValidation'>
							<div class="row">
								<div class="col-md-8" id="rangeOfRecurrence_options">
									<div>
										<div class="radio">
											<label> <input type="radio" value="NoEndDate"
												name="rangeOfRecurrence" id="isNoEndDate"> <spring:message code = "anvizent.package.label.noEndDate"/>
											</label>
										</div>
										<div class="radio hide">
											<label> <input type="radio" value="MaxOccurences"
												name="rangeOfRecurrence"> <spring:message code = "anvizent.package.label.endAfter"/> <input
												type="text" style="width: 50px;" id="noOfMaxOccurences"/> <spring:message code = "anvizent.package.label.occurrences"/>
											</label>
										</div>
										<div class="radio">
											<label> 
											<input type="radio" value="ScheduleEndDate"
												name="rangeOfRecurrence" class='endByDate'> <spring:message code = "anvizent.package.label.endBy"/> <input type="text"  class="datepicker" style="width: 100px;" id="scheduleEndDate" />
											</label>
											<div id='scheduleEndDateVaLidation'></div>
										</div>
									</div>
								</div>
							</div>
							</div> 
						</div>
					</div>
					<div class="panel panel-info" id='timeZoneDivPanel'	style="margin-top: -15px;">
						<div class="panel-heading"><spring:message code = "anvizent.package.label.timeZone"/></div>
						<div class="panel-body">
							<div class='row form-group' style="margin-bottom: 5px;">
								<div class='col-sm-2'><spring:message code = "anvizent.package.label.timeZone"/></div>
								<div class='col-sm-6'>
									<select class="form-control" id="timesZone">
										<option value="select"><spring:message code = "anvizent.package.label.selectOption"/></option>
										
									</select>
								</div>
								
								
							</div>
						</div>
					</div>
				</div>	
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="confirmSchedule"><spring:message code="anvizent.package.button.ok"/></button>
					<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="alertForUnScheduleDLPopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 500px;">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.link.unschedule"/></h4>
				      </div>
				      <div class="modal-body">
				        	<p><spring:message code="anvizent.package.message.text.areYouSureYouWantToUnscheduleTheModule"/></p>
				      </div>
				      <div class="modal-footer" style="text-align: center;">					        	
							<button type="button" class="btn btn-primary btn-sm confirmUnScheduleDL" id = "checked"><spring:message code="anvizent.package.button.ok"/></button>
							<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				      </div>
			    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
	 </div>
	 </div>
	 </div>
	 <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<script type="text/javascript">

$('.collapse2').on('shown.bs.collapse', function(){
	$(this).parent().find(".glyphicon-plus").removeClass("glyphicon-plus").addClass("glyphicon-minus");
	}).on('hidden.bs.collapse', function(){
	$(this).parent().find(".glyphicon-minus").removeClass("glyphicon-minus").addClass("glyphicon-plus");
	});


</script>
	 