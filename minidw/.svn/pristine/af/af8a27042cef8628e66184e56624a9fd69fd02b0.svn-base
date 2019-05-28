<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.admin.label.webServiceILMapping"/></h4>
 	</div>
 	
	<jsp:include page="admin_error.jsp"></jsp:include>
	<div class="col-md-12 message-class"></div>
	
	<input type="hidden" id="userId" value="<c:out value="${principal.userId}"/>">
	<c:url value="/admin/webServiceILMapping" var="url"/>
	<input id="save" type="hidden" value="${url}/save">
	<input id="edit" type="hidden" value="${url}/edit">

	<form:form modelAttribute="webServiceILMapping" action="${url}">
		<form:hidden path="pageMode"/>
		<form:hidden path="wsILMappingId"/>
		<form:hidden path="iLId"/>
		
		<c:if test="${webServiceILMapping.pageMode != 'mapping'}">
			<div class="row form-group">
				<label class="col-sm-3"><spring:message code="anvizent.package.label.webServiceTemplateName"/> : </label>
				<div class="col-sm-5">
					<form:select path="wsTemplateId" cssClass="form-control">
						<form:option value="0"><spring:message code="anvizent.package.label.SelectWebServiceTemplate"/></form:option>
						<form:options items="${getAllWebServices}"></form:options>
					</form:select>
				</div>
			</div>		
		</c:if>
			
		<c:if test="${webServiceILMapping.wsTemplateId != '0' && webServiceILMapping.wsTemplateId != null && webServiceILMapping.pageMode == 'list'}">
			<div class="row form-group">
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg " id=iLsTable>
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.ilId"/></th>
								<th><spring:message code="anvizent.package.label.ilName"/></th>
								<th><spring:message code="anvizent.package.label.version"/></th>	
								<th><spring:message code="anvizent.package.label.iLType"/></th>						
								<th><spring:message code="anvizent.package.label.iLTableName"/></th>
								<th><spring:message code="anvizent.package.label.noOfApis"/></th>
								<th><spring:message code="anvizent.package.label.addEditView"/></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${iLInfoList}" var="iLInfo">
								<tr>
									<td><c:out value="${iLInfo.iL_id}"/></td>
									<td><c:out value="${iLInfo.iL_name}" /></td>
									<td><c:out value="${not empty iLInfo.version ? iLInfo.version : '-'}"/></td>
									<td><c:out value="${iLInfo.iLType == 'D'? 'Dimension' : 'Transaction'}"/></td>
									<td><c:out value="${iLInfo.iL_table_name}" /></td>
									<td><c:out value="${iLInfo.wsApiCount}"/></td>
									<td>
										<button type="submit" class="btn btn-primary btn-sm addOrEdit tablebuttons" value="<c:out value="${iLInfo.iL_id}"/>">
											<i class="fa fa-pencil" aria-hidden="true"></i>
										</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:if>
			
		<c:if test="${webServiceILMapping.iLId != '0' && webServiceILMapping.iLId != null && webServiceILMapping.pageMode == 'mapping'}">
			<div class="col-sm-12">
				<div class="panel panel-default">
					<div class="panel-heading"><spring:message code="anvizent.package.label.webServiceApiDetails"/>
						<button type="button" class="btn btn-primary btn-sm addNewWsApiBlock" style="float: right;">
							<span class="glyphicon glyphicon-plus"></span>
						</button>
					</div>
					<div class="panel-body">
						<div class="row form-group">
							<div style="float:right;">
								<button type="button" class="btn btn-primary btn-sm saveWebService"><spring:message code = "anvizent.package.button.save"/></button>
								<button type="submit" class="btn btn-primary btn-sm back_btn back startLoader"><spring:message code="anvizent.package.link.back"/></button>
							</div>
						</div>
						
						<div class="panel-group row form-group" id="accordion" role="tablist" aria-multiselectable="true">
							<div class="col-sm-12" id="wsApiMappingBlocks">
								<form:hidden path="wsTemplateId"/>
								<c:forEach items="${mappingDetails}" var="webServiceApis" varStatus="loop">
									<div class="panel panel-default wsApiMappingBlock">
										<div class="panel-heading accordion-heading" role="tab">
											<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse${loop.index}" aria-expanded="true" class="tablebuttons"
												style="text-decoration: none;">
												<spring:message code="anvizent.package.label.webServiceApi"/>
												<span class="glyphicon glyphicon-minus-sign"></span>
											</a>
											<button type="button" class="btn btn-primary btn-sm inactivateWsILMapping remove_field" style="float: right;">
												<span class="glyphicon glyphicon-trash"></span>
											</button>
										</div>
										<div id="collapse${loop.index}" class="panel-collapse collapse in collapseDiv" role="tabpanel">
											<div class="panel-body">
												<div class="row form-group">
													<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.apiName" /></label>
													<div class="col-sm-10">
														<form:hidden path="webServiceApis[${loop.index}].id" value="${webServiceApis.id}" class="wsApiField id"/>
														<form:input path="webServiceApis[${loop.index}].apiName" cssClass="form-control wsApiField apiName" value="${webServiceApis.apiName}"/>
													</div>
												</div>
												
												<div class="row form-group">
													<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.url" /></label>
													<div class="col-sm-8">
														<div><form:input path="webServiceApis[${loop.index}].apiUrl" cssClass="form-control wsApiField apiUrl" value="${webServiceApis.apiUrl}"/>
														<br>
														<form:checkbox path="webServiceApis[${loop.index}].baseUrlRequired"  cssClass="wsApiField baseUrlRequired" value="${webServiceApis.baseUrlRequired}" checked="${webServiceApis.baseUrlRequired == 'true' ? 'checked' : ''}" />
														Base URL Required   ${not empty webServiceApis.baseUrl  ?  [ webServiceApis.baseUrl ]   : '' } <form:hidden path="webServiceApis[${loop.index}].baseUrl" cssClass="form-control wsApiField baseUrl" value="${webServiceApis.baseUrl}"/>
														</div>
														<br>
														<p class="help-block">
															<em class="serverIpWithPort" title="Ex : http://host:IpAddress/serviceName/{#PathParamName}?requestParamKey=value&requestParamKey=value">
															 Ex : http://host:IpAddress/serviceName/{#PathParamName}?requestParamKey=value&amp;requestParamKey=value</em>
														</p>
													</div>
													<div class="col-sm-2">
														<button class="btn btn-sm btn-primary getPathParams" type="button" value="${loop.index}">
															<spring:message code="anvizent.package.label.getPathParams"/>
														</button>
													</div>
													<form:hidden path="webServiceApis[${loop.index}].apiPathParams" class="wsApiField apiPathParams" 
														value="${webServiceApis.apiPathParams}"/>
												</div>
												
												<div class="row form-group pathParamsDetailsBlocks">
												
												</div>
												
												<div class="row form-group">
													<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.methodType" /></label>
													<div class="col-sm-10">
														<div class="methodTypeValidation">
															<label class="radio-inline">
																<form:radiobutton path="webServiceApis[${loop.index}].apiMethodType" class="wsApiField apiMethodType" value="GET" checked="${webServiceApis.apiMethodType == 'GET' ? 'checked' : ''}"/>
																<spring:message code="anvizent.package.label.get"/>
															</label>
															<label class="radio-inline">
																<form:radiobutton path="webServiceApis[${loop.index}].apiMethodType" class="wsApiField apiMethodType" value="POST" checked="${webServiceApis.apiMethodType == 'POST' ? 'checked' : ''}"/>
																<spring:message code="anvizent.package.label.post"/>
															</label>
															<form:hidden path="webServiceApis[${loop.index}].apiRequestParams" class="wsApiField reqParams" value="${webServiceApis.apiRequestParams}"/>
															<form:hidden path="webServiceApis[${loop.index}].apiBodyParams" class="wsApiField bodyParams" value="${webServiceApis.apiBodyParams}"/>
															<form:hidden path="webServiceApis[${loop.index}].defaultMapping" class="wsApiField defaultMapping" value="${webServiceApis.defaultMapping}"/>
														</div>
													</div>
												</div>
												
												<div class="row form-group" id="requestParams">
													<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.requestParameters" />:</label>
													<div class="col-sm-12">
														<table class="table table-striped table-bordered tablebg" id="requestParamsTable">
															<thead>
																<tr>
																	<th class="col-xs-4"><spring:message code = "anvizent.package.label.paramName"/></th>
																	<th><spring:message code = "anvizent.package.label.isMandatory"/></th>
																	<th><spring:message code="anvizent.package.label.isPassword"/></th>
																	<th> 
																	   <button type ="button" class="btn btn-primary btn-sm addRequestParam">
																			<span class="glyphicon glyphicon-plus"></span>
																		</button>
																	</th>
																</tr>
															</thead>
															<tbody>
																<c:if test="${empty webServiceApis.apiRequestParams}">
																	<tr>
																		<td><input type="text" class="form-control paramValue"/></td>
																		<td><input type="checkbox" class="isMandatory"/></td>
																		<td><input type="checkbox" class="isPassword"/></td>
																		<td>
																			<button type="button" class="btn btn-primary btn-sm deleteRequestParam remove_field">
																				<span class="glyphicon glyphicon-trash"></span>
																			</button>
																		</td>
																	</tr>
																</c:if>
															</tbody>
															<tfoot>
																<tr class="data-row" style="display:none;">
																	<td><input type="text" class="form-control paramValue"/></td>
																	<td><input type="checkbox" class="isMandatory"/></td>
																	<td><input type="checkbox" class="isPassword"/></td>
																	<td>
																		<button type="button" class="btn btn-primary btn-sm deleteRequestParam remove_field">
																			<span class="glyphicon glyphicon-trash"></span>
																		</button>
																	</td>
																</tr>
															</tfoot>
														</table>
													</div>	
												</div>
												
												<div class="row form-group" id="apiBodyParams" ${webServiceApis.apiMethodType == 'POST' ? '' : 'style="display:none;"'}>
													<label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.BodyParameters"/> :</label>
													<div class="col-sm-12">
														<table class="table table-striped table-bordered tablebg" id="bodyParamsTable">
															<thead>
																<tr>
																	<th class="col-xs-4"><spring:message code = "anvizent.package.label.paramName"/></th>
																	<th><spring:message code = "anvizent.package.label.isMandatory"/></th>
																	<th><spring:message code="anvizent.package.label.isPassword"/></th>
																	<th> 
																	   <button type ="button" class="btn btn-primary btn-sm addBodyParam">
																			<span class="glyphicon glyphicon-plus"></span>
																		</button>
																	</th>
																</tr>
															</thead>
															<tbody>
																<c:if test="${empty webServiceApis.apiBodyParams}">
																	<tr>
																		<td><input type="text" class="form-control paramValue"/></td>
																		<td><input type="checkbox" class="isMandatory"/></td>
																		<td><input type="checkbox" class="isPassword"/></td>
																		<td>
																			<button type="button" class="btn btn-primary btn-sm deleteBodyParam remove_field">
																				<span class="glyphicon glyphicon-trash"></span>
																			</button>
																		</td>
																	</tr>
																</c:if>
															</tbody>
															<tfoot>
																<tr class="data-row" style="display:none;">
																	<td><input type="text" class="form-control paramValue"/></td>
																	<td><input type="checkbox" class="isMandatory"/></td>
																	<td><input type="checkbox" class="isPassword"/></td>
																	<td>
																		<button type="button" class="btn btn-primary btn-sm deleteBodyParam remove_field">
																			<span class="glyphicon glyphicon-trash"></span>
																		</button>
																	</td>
																</tr>
															</tfoot>
														</table>
													</div>	
												</div>
								                <div class="row form-group">
								                <form:hidden path="webServiceApis[${loop.index}].paginationRequestParamsData" cssClass="wsApiField paginationRequestParamsData" value="${webServiceApis.paginationRequestParamsData }"/>
													<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.ispaginationrequired" />:</label>
													<div class="col-sm-10">
														   <label class="radio-inline">
																<form:radiobutton path="webServiceApis[${loop.index}].paginationRequired" class="wsApiField paginationRequired"  value="yes"  checked = "${webServiceApis.paginationRequired ? 'true':'' }"/>
																<spring:message code="anvizent.package.button.yes"/> 
															</label>
															<label class="radio-inline">
																<form:radiobutton path="webServiceApis[${loop.index}].paginationRequired" class="wsApiField paginationRequired" value="no"  checked = "${!webServiceApis.paginationRequired ? 'true':'' }"/>
																<spring:message code="anvizent.package.button.no"/>
															</label>
													</div>
												</div>
												 <div class="row form-group paginationType" style="display: ${webServiceApis.paginationRequired ? '':'none' };">
													<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.paginationType" />:</label>
													<div class="col-sm-8">
														   <label class="radio-inline">
																<form:radiobutton path="webServiceApis[${loop.index}].paginationType" class="wsApiField paginationOffsetDateType paginationOffset" value="offset"  checked = "${webServiceApis.paginationType == 'offset' ? 'true':'' }"/>
																<spring:message code="anvizent.package.label.paginationoffset"/>
															</label>
															<label class="radio-inline">
																<form:radiobutton path="webServiceApis[${loop.index}].paginationType" class="wsApiField paginationOffsetDateType paginationDate" value="date"  checked = "${webServiceApis.paginationType == 'date' ? 'true':'' }"/>
																<spring:message code="anvizent.package.label.paginationdate"/>
															</label>
															<label class="radio-inline">
																<form:radiobutton path="webServiceApis[${loop.index}].paginationType" class="wsApiField paginationOffsetDateType paginationIncrementalDate" value="incrementaldate"  checked = "${webServiceApis.paginationType == 'incrementaldate' ? 'true':'' }"/>
																<spring:message code="anvizent.package.label.incrementalpaginationdate"/>
															</label>
															<label class="radio-inline">
																<form:radiobutton path="webServiceApis[${loop.index}].paginationType" class="wsApiField paginationOffsetDateType paginationOther" value="hypermedia"  checked = "${webServiceApis.paginationType == 'hypermedia' ? 'true':'' }"/>
																<spring:message code="anvizent.package.label.pagination.nextpage"/>
															</label>
															<label class="radio-inline">
																<form:radiobutton path="webServiceApis[${loop.index}].paginationType" class="wsApiField paginationOffsetDateType paginationConditionalDate" value="conditionaldate"  checked = "${webServiceApis.paginationType == 'conditionaldate' ? 'true':'' }"/>
																<spring:message code="anvizent.package.label.pagination.conditionaldate"/>
															</label>
															<label class="radio-inline">
																<form:radiobutton path="webServiceApis[${loop.index}].paginationType" class="wsApiField paginationOffsetDateType paginationPageNumberSize" value="page"  checked = "${webServiceApis.paginationType == 'page' ? 'true':'' }"/>
																<spring:message code="anvizent.package.label.pagination.page"/>
															</label>
															</div>
														    <div class="col-sm-2 paginationParamTypeDiv">
															<select class="paginationParamType form-control">
																<option value="Request Parameter"><spring:message code="anvizent.package.message.RequestParameter" /></option>
																<option value="Body Parameter"><spring:message code="anvizent.package.message.BodyParameter" /></option>
														    </select>
													        </div>
							                        </div>
													<div class="row form-group paginationDateType" id="paginationDateType" style="display: ${webServiceApis.paginationType == 'date' ? '':'none' };">
													   <div class="row form-group">
													    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.datepagination"/> :</label>
														 <div class="col-sm-2">
															 <input type="text" placeholder="From Date Param Name" title ="From Date Param Name" class="form-control paginationStartDateParam"/>
														 </div>
													     <div class="col-sm-3">
													     	<input type="text" placeholder="Starts From" title ="start date" class="form-control paginationStartDate" >
													     </div>
													     <div class="col-sm-2">
															 <input type="text" placeholder="To Date Param Name" title ="To Date Param Name" class="form-control paginationEndDateParam"/>
														 </div>
													      <div class="col-sm-3">
													     <select class="paginationDateRange col-sm-4 form-control" title="day range">
									                           	<c:forEach var="dr" begin="0" end="364">
					                                                 <option value="${dr+1}">${dr+1}</option>
					                                            </c:forEach>
														 </select>
														</div>
														</div>
														<div class="row form-group">
														     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
															 <div class="col-sm-3">
																 <input type="text" placeholder="Param Name(optional)" title ="Param Name(optional)" class="form-control paginationDatePageNumberRequestParamName"/>
															 </div>
															 <div class="col-sm-1">
															 :
															 </div>
														     <div class="col-sm-3">
														     	<input type="text" placeholder="Starts From(optional)" title ="Starts From(optional)" class="form-control paginationDatePageNumberRequestParamValue" >
														</div>
												        </div>
														 <div class="row form-group">
														  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
															 <div class="col-sm-3">
																 <input type="text" placeholder="Param Name(optional)"  title ="Param Name(optional)" class="form-control paginationDatePageSizeRequestParamName"/>
															 </div>
															 <div class="col-sm-1">
															 :
															 </div>
														     <div class="col-sm-3">
														     	<input type="text" placeholder="Param Value(optional)" title ="Param Value(optional)" class="form-control paginationDatePageSizeRequestParamValue" >
														     </div>
														  </div>
													  </div>
													  
													 <div class="row form-group paginationIncrementalDateType" id="paginationIncrementalDateType"  style="display: ${webServiceApis.paginationType == 'incrementaldate' ? '':'none' };">
													    <div class="row form-group">
													     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.incrementalpaginationdatepagination"/> :</label>
														 <div class="col-sm-2">
															 <input type="text" placeholder="Start Date Param Name" title ="Start Date Param Name" class="form-control paginationIncrementalStartDateParam"/>
														 </div>
														  <div class="col-sm-2">
													     	<input type="text" placeholder="Start Date" title ="Start Date" class="form-control paginationIncrementalStartDate" >
													     </div>
														  <div class="col-sm-2">
															 <input type="text" placeholder="End Date Param Name" title ="End Date Param Name" class="form-control paginationIncrementalEndDateParam"/>
														 </div>
													     <div class="col-sm-2">
													     	<input type="text" placeholder="End Date(optional)" title ="End Date should be grater than Start date(Date range must be more than 7 days)." class="form-control paginationIncrementalEndDate" >
													     </div>
													     <div class="col-sm-2">
														     <select class="paginationIncrementalDateRange col-sm-4 form-control" title="date range">
																	<c:forEach var="dr" begin="0" end="364">
					                                                 <option value="${dr+1}">${dr+1}</option>
					                                                </c:forEach>
															 </select>
														</div>
														</div>
														<div class="row form-group">
														     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
															 <div class="col-sm-3">
																 <input type="text" placeholder="Param Name(optional)" title ="Param Name(optional)" class="form-control paginationIncrementalDatePageNumberRequestParamName"/>
															 </div>
															 <div class="col-sm-1">
															 :
															 </div>
														     <div class="col-sm-3">
														     	<input type="text" placeholder="Starts From(optional)" title ="Starts From(optional)" class="form-control paginationIncrementalDatePageNumberRequestParamValue" >
														</div>
												        </div>
														 <div class="row form-group">
														  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
															 <div class="col-sm-3">
																 <input type="text" placeholder="Param Name(optional)"  title ="Param Name(optional)" class="form-control paginationIncrementalDatePageSizeRequestParamName"/>
															 </div>
															 <div class="col-sm-1">
															 :
															 </div>
														     <div class="col-sm-3">
														     	<input type="text" placeholder="Param Value(optional)" title ="Param Value(optional)" class="form-control paginationIncrementalDatePageSizeRequestParamValue" >
														     </div>
														  </div>
													</div>
													  
												<div class="row form-group paginationOffSetType" id="paginationOffSetType" style="display: ${webServiceApis.paginationType == 'offset' ? '':'none' };">
												  <div class="row form-group">
												     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.offsetparamnameandstartsfrom"/> :</label>
													 <div class="col-sm-3">
														 <input type="text" placeholder="Param Name"  class="form-control paginationOffSetRequestParamName"/>
													 </div>
													 <div class="col-sm-1">
													 :
													 </div>
												     <div class="col-sm-3">
												     	<input type="text" placeholder="Starts From" class="form-control paginationOffSetRequestParamValue" >
												     </div>
												  </div>
												  <div class="row form-group">
												  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.limitparamnameandvalue"/> :</label>
													 <div class="col-sm-3">
														 <input type="text" placeholder="Param Name"  class="form-control paginationLimitRequestParamName"/>
													 </div>
													 <div class="col-sm-1">
													 :
													 </div>
												     <div class="col-sm-3">
												     	<input type="text" placeholder="Param Value" class="form-control paginationLimitRequestParamValue" >
												     </div>
												  </div>
													<div class="row form-group">
														<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.paginationObjectName" /> :</label>
														<div class="col-sm-8">
															<input type="text" placeholder="Pagination Object Name" title="param name" class="form-control paginationObjectName" />
														</div>
													</div>

													<div class="row form-group">
														<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.paginationSearchId" /> :</label>
														<div class="col-sm-8">
															<input type="text"
																placeholder="Pagination SearchId Param"
																title="param name"
																class="form-control paginationSearchId" />
														</div>
								        	       </div>
													<div class="row-form-group">
														<label class="control-label col-sm-2"><spring:message
																code="anvizent.package.label.PaginationSoapBody" /> </label>
														<div class="col-sm-8">
															<textarea class="form-control PaginationSoapBody"
																placeholder="Enter Pagination Soap Body "></textarea>
														</div>
													</div>

												</div>
												<div class="row form-group paginationPageNumberSizeType" id="paginationPageNumberSizeType" style="display: ${webServiceApis.paginationType == 'page' ? '':'none' };">
												  <div class="row form-group">
												     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
													 <div class="col-sm-3">
														 <input type="text" placeholder="Param Name" title ="Param Name" class="form-control paginationPageNumberRequestParamName"/>
													 </div>
													 <div class="col-sm-1">
													 :
													 </div>
												     <div class="col-sm-3">
												     	<input type="text" placeholder="Starts From" title ="Starts From" class="form-control paginationPageNumberRequestParamValue" >
												     </div>
												  </div>
												  <div class="row form-group">
												  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
													 <div class="col-sm-3">
														 <input type="text" placeholder="Param Name"  title ="Param Name" class="form-control paginationPageSizeRequestParamName"/>
													 </div>
													 <div class="col-sm-1">
													 :
													 </div>
												     <div class="col-sm-3">
												     	<input type="text" placeholder="Param Value" title ="Param Value" class="form-control paginationPageSizeRequestParamValue" >
												     </div>
												  </div>
												</div>
												
												<div class="row form-group paginationOtherType" id = "paginationOther" style="display: ${webServiceApis.paginationType == 'hypermedia' ? '':'none' };">
												   <div class="row form-group">
												     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagination.nextpage.pattern.and.Limit"/> :</label>
													 <div class="col-sm-3">
														 <input type="text" placeholder="<spring:message code = "anvizent.package.label.placeholder.hyperlink.next.record.pattern"/>" class="form-control paginationOtherRequestKeyParam"/>
													 </div>
												     <div class="col-sm-3">
												     	<input type="text" placeholder="Limit" class="form-control paginationOtherRequestLimit" >
												     </div>
												   </div>
												</div>
												
												<div class="row form-group paginationConditionalType" id = "paginationConditionalDate" style="display: ${webServiceApis.paginationType == 'onditional' ? '':'none' };">
												   <div class="row form-group">
												     <label class="control-label col-sm-2">Filter & Condition :</label>
													 <div class="col-sm-3">
													    <input type="text" placeholder="{ $filter }" class="form-control paginationFilterName">
													 </div>
													 <div class="col-sm-3">
														<select class="paginationConditionalDayRange col-sm-4 form-control"  title="date range">
															 <option value="0"><spring:message code="anvizent.package.label.pagination.selectDayRange"/></option>
														  <c:forEach var="dr" begin="1" end="365">
								                             <option value="${dr}">${dr}</option>
								                           </c:forEach>
												  		</select>
													 </div>
												   </div>
												   <div class="row form-group">
												    <label class="control-label col-sm-2"></label>
												     <div class="col-sm-3">
													    <input type="text" placeholder="From date param" class="form-control paginationConditionalFromDateParam">
													 </div>
													 <div class="col-sm-2">
														<input type="text" placeholder="{EX: >, <, = , ge, le }" class="form-control paginationConditionalFromDateCondition">
													 </div>
													 <div class="col-sm-3">
													    <input type="text" placeholder="From date" class="form-control paginationConditionalFromDate">
													 </div>
												   </div>
												   <div class="row form-group">
													 <label class="control-label col-sm-2"></label>
													   <div class="col-sm-3">
													    <input type="text" placeholder="{ $condition Ex: { and, or } }" class="form-control paginationConditionalParam">
													   </div>
													</div>
													<div class="row form-group">
												    <label class="control-label col-sm-2"></label>
												     <div class="col-sm-3">
													    <input type="text" placeholder="To date param" class="form-control paginationConditionalToDateParam">
													 </div>
													 <div class="col-sm-2">
														<input type="text" placeholder="{ EX: >, <, =, gt, lt }" class="form-control paginationConditionalToDateCondition">
													 </div>
													 <div class="col-sm-3">
													    <input type="text" placeholder="To date(optional)" class="form-control paginationConditionalToDate">
													 </div>
												   </div>
												</div>
											<div class="row-form-group">
												<div class="row form-group paginationSoapBody"
													style='display: none;'>
													<label class="control-label col-sm-2"><spring:message
															code="anvizent.package.label.PaginationSoapBody" /> </label>
													<div class="col-sm-8">
														<textarea
															name="webServiceApis[${loop.index}].PaginationSoapBody"
															id="webServiceApis[${loop.index}].PaginationSoapBody"
															class="form-control PaginationSoapBody">${webServiceApis.paginationSoapBody}</textarea>
														<%-- <form:textarea path="webServiceApis[0].PaginationSoapBody" cssClass="form-control PaginationSoapBody" id="PaginationSoapBody" placeholder="Enter Pagination Soap Body "/> --%>
													</div>
												</div>
											</div>

											<div class="row form-group">
													<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.incrementalUpdate" /></label>
													<div class="col-sm-10">
														<form:checkbox path="webServiceApis[${loop.index}].incrementalUpdate" cssClass="wsApiField incrementalUpdate" checked="${webServiceApis.incrementalUpdate ?'checked':'' }"/>
													</div>
												</div>
												<div class="row form-group incrementalUpdateDetailsDiv" style="display: ${webServiceApis.incrementalUpdate ?'':'none' };">
													<label class="control-label col-sm-12"><spring:message code="anvizent.package.label.incrementalUpdate" /> Details: </label>
													<div>
														<form:hidden path="webServiceApis[${loop.index}].incrementalUpdateparamdata" cssClass="wsApiField incrementalUpdateparamdata" value="${webServiceApis.incrementalUpdateparamdata }"/>
														<input type="hidden" class="incrementalUpdateParamColumnName">
														<div class=" col-sm-4"><input type="text" class="incrementalUpdateParamName form-control" placeholder="Paramter Name"></div>
														<div class=" col-sm-4"><input type="text" class="incrementalUpdateParamvalue col-sm-4 form-control" placeholder="Ex: /* {date} */"></div>
														<div class=" col-sm-4">
															<select class="incrementalUpdateParamType col-sm-4 form-control">
																<option value="Request Parameter"><spring:message code="anvizent.package.label.RequestParameter" /></option>
																<option value="Body Parameter" class="incrementalUpdateBodyParamType" ${webServiceApis.apiMethodType == 'GET' ? 'style="display:none;"' : ''}><spring:message code = "anvizent.package.label.BodyParameter"/></option>
															</select>
														</div>	
													</div>
												</div>
												
									 <div class="row-form-group">
										<div class="row form-group soapBodyElement">
											<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.soapBodyElement"/> :</label>
											<div class="col-sm-8">
											 <textarea  name="webServiceApis[${loop.index}].soapBodyElement" id="webServiceApis[${loop.index}].soapBodyElement"  class="form-control soapBodyElement">${webServiceApis.soapBodyElement}</textarea> 
											</div>
										</div>
									  </div>
										<div class="row form-group">
											<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.responseobjectname" /></label>
											<div class="col-sm-10">
												<form:input path="webServiceApis[${loop.index}].responseObjectName" cssClass="form-control wsApiField responseObjectName" value="${webServiceApis.responseObjectName}"/>
											</div>
										</div>
										
										<div class="row form-group">
											<label class="control-label col-sm-2">Default Mapping :</label>
											<div class="col-sm-10">
											
											<div id="accordion-first" class="clearfix"><!-- standard start accordion -->
	                          <div class="accordionWs" id="accordion2Ws">
		                          <div class="accordion-group">
					                    <div class="accordion-heading" style="border: 1px solid #dce1e4;background:#1096eb;">
					                  <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion2WS" href="#collapseOneWS${loop.index}">
					                  <span class="glyphicon glyphicon-plus-sign"></span> Default Mapping
					                  </a>
					               </div>
						           <div style="height: 0px;" id="collapseOneWS${loop.index}" class="accordion-body collapse">	
							           <div class="panel panel-info">
							                 <div class="panel-body">
												
												 <div id="defaultMappingDiv" class="panel-collapse collapse1 in collapseDiv"  style="margin-top: 5px;"></div>
						        <div class="row-form-group columnsKeyValue" id="columnsParameters" style="display: none">
										<div class="row form-group">
											<div class="col-sm-4">
												<spring:message code = "anvizent.package.label.enterKey" var="enterKey"/>
												<input class="form-control columnKey">
											</div>
											<div class="col-sm-4">
												<spring:message code = "anvizent.package.label.enterValue" var ="enterValue"/>
												<input class="form-control columnValue">
											</div>
											<div class="col-sm-2">
												<button type="button" class="btn btn-primary btn-sm addMappingField">
													<span class="glyphicon glyphicon-plus"></span>
												</button>
												<button type="button" class="btn btn-primary btn-sm deleteMappingField">
													<span class="glyphicon glyphicon-trash"></span>
												</button>
											</div>
										</div>
						          </div> 
										    </div>
										</div>
									</div>
								  </div>
							</div>
						</div>
											
						 </div>
					</div>
			</div>
			</div>
										</div>	
									
										</c:forEach>
										
									</div>
							</div>	
						</div>
						
						<div class="row form-group">
							<div style="float:right;">
								<button type="button" class="btn btn-primary btn-sm saveWebService"><spring:message code = "anvizent.package.button.save"/></button>
								<button type="submit" class="btn btn-primary btn-sm back_btn back startLoader"><spring:message code="anvizent.package.link.back"/></button>
							</div>
						</div>
					</div>
				</div>	
				</c:if>
		
		<div id="wsApiBlock" style="display:none;">
			<div class="panel panel-default wsApiMappingBlock">
				<div class="panel-heading accordion-heading wsApiMappingBlockAccordian" role="tab">
					<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse0" aria-expanded="true" class="tablebuttons" style="text-decoration: none;">
						<spring:message code="anvizent.package.label.webServiceApi"/>
						<span class="glyphicon glyphicon-minus-sign"></span>
					</a>
					<button type="button" class="btn btn-primary btn-sm deleteWsApiBlock remove_field" style="float: right;">
						<span class="glyphicon glyphicon-trash"></span>
					</button>
				</div>
				<div id="collapse0" class="panel-collapse collapse in collapseDiv" role="tabpanel">
					<div class="panel-body">
						<div class="row form-group">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.apiName" /></label>
							<div class="col-sm-10">
								<form:input path="webServiceApis[0].apiName" cssClass="form-control wsApiField apiName"/>
							</div>
						</div>
						
						<div class="row form-group">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.url" /></label>
							<div class="col-sm-8">
								<div><form:input path="webServiceApis[0].apiUrl" cssClass="form-control wsApiField apiUrl"/>
								<br><form:checkbox path="webServiceApis[0].baseUrlRequired"  cssClass="wsApiField baseUrlRequired"/> Base URL Required
								<form:hidden path="webServiceApis[0].baseUrl" cssClass="form-control wsApiField baseUrl"/>
								</div>
								<br>
								<p class="help-block">
									<em class="serverIpWithPort" title="Ex : http://host:IpAddress/serviceName/{#PathParamName}?requestParamKey=value&requestParamKey=value">
									Ex : http://host:IpAddress/serviceName/{#PathParamName}?requestParamKey=value&amp;requestParamKey=value</em>
								</p>
							</div>
							<div class="col-sm-2">
								<button class="btn btn-sm btn-primary getPathParams" type="button" value="0">
									<spring:message code="anvizent.package.label.getPathParams"/>
								</button>
							</div>
							<form:hidden path="webServiceApis[0].apiPathParams" class="wsApiField apiPathParams"/>
						</div>
						
						<div class="row form-group pathParamsDetailsBlocks"></div>
						
						<div class="row form-group">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.methodType" /></label>
							<div class="col-sm-10">
								<div class="methodTypeValidation">
									<label class="radio-inline">
										<form:radiobutton path="webServiceApis[0].apiMethodType" value="GET" class="wsApiField apiMethodType"/>
										<spring:message code="anvizent.package.label.get"/>
									</label>
									<label class="radio-inline">
										<form:radiobutton path="webServiceApis[0].apiMethodType" value="POST" class="wsApiField apiMethodType"/>
										<spring:message code="anvizent.package.label.post"/>
									</label>
									<form:hidden path="webServiceApis[0].apiRequestParams" class="wsApiField reqParams"/>
									<form:hidden path="webServiceApis[0].apiBodyParams" class="wsApiField bodyParams" />
									<form:hidden path="webServiceApis[0].defaultMapping" class="wsApiField defaultMapping" />
									<%-- <form:hidden path="webServiceApis[0].apiBodyParams" class="wsApiField bodyParams" /> --%>
								</div>
							</div>
						</div>
						
						<div class="row form-group" id="requestParams">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.requestParameters" /> :</label>
							<div class="col-sm-12">
								<table class="table table-striped table-bordered tablebg" id="requestParamsTable">
									<thead>
										<tr>
											<th class="col-xs-4"><spring:message code = "anvizent.package.label.paramName"/></th>
											<th><spring:message code = "anvizent.package.label.isMandatory"/></th>
											<th><spring:message code="anvizent.package.label.isPassword"/></th>
											<th> 
											   <button type ="button" class="btn btn-primary btn-sm addRequestParam">
													<span class="glyphicon glyphicon-plus"></span>
												</button>
											</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><input type="text" class="form-control paramValue"/></td>
											<td><input type="checkbox" class="isMandatory"/></td>
											<td><input type="checkbox" class="isPassword"/></td>
											<td>
												<button type="button" class="btn btn-primary btn-sm deleteRequestParam remove_field">
													<span class="glyphicon glyphicon-trash"></span>
												</button>
											</td>
										</tr>
									</tbody>
									<tfoot>
										<tr class="data-row" style="display:none;">
											<td><input type="text" class="form-control paramValue"/></td>
											<td><input type="checkbox" class="isMandatory"/></td>
											<td><input type="checkbox" class="isPassword"/></td>
											<td>
												<button type="button" class="btn btn-primary btn-sm deleteRequestParam remove_field">
													<span class="glyphicon glyphicon-trash"></span>
												</button>
											</td>
										</tr>
									</tfoot>
								</table>
							</div>	
						</div>
						
						<div class="row form-group" id="apiBodyParams">
							<label class="control-label col-sm-2"><spring:message code = "anvizent.package.message.BodyParameters"/> :</label>
							<div class="col-sm-12">
								<table class="table table-striped table-bordered tablebg" id="bodyParamsTable">
									<thead>
										<tr>
											<th class="col-xs-4"><spring:message code = "anvizent.package.label.paramName"/></th>
											<th><spring:message code = "anvizent.package.label.isMandatory"/></th>
											<th><spring:message code="anvizent.package.label.isPassword"/></th>
											<th> 
											   <button type ="button" class="btn btn-primary btn-sm addBodyParam">
													<span class="glyphicon glyphicon-plus"></span>
												</button>
											</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td><input type="text" class="form-control paramValue"/></td>
											<td><input type="checkbox" class="isMandatory"/></td>
											<td><input type="checkbox" class="isPassword"/></td>
											<td>
												<button type="button" class="btn btn-primary btn-sm deleteBodyParam remove_field">
													<span class="glyphicon glyphicon-trash"></span>
												</button>
											</td>
										</tr>
									</tbody>
									<tfoot>
										<tr class="data-row" style="display:none;">
											<td><input type="text" class="form-control paramValue"/></td>
											<td><input type="checkbox" class="isMandatory"/></td>
											<td><input type="checkbox" class="isPassword"/></td>
											<td>
												<button type="button" class="btn btn-primary btn-sm deleteBodyParam remove_field">
													<span class="glyphicon glyphicon-trash"></span>
												</button>
											</td>
										</tr>
									</tfoot>
								</table>
							</div>	
						</div>
						
	                       <div class="row form-group">
	                           <form:hidden path="webServiceApis[0].paginationRequestParamsData" cssClass="wsApiField paginationRequestParamsData"/>
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.ispaginationrequired" />:</label>
								<div class="col-sm-10">
								  <div class="paginationValidation">
									   <label class="radio-inline">
											<form:radiobutton path="webServiceApis[0].paginationRequired" class="wsApiField paginationRequired"  value="yes" />
											<spring:message code="anvizent.package.button.yes"/>
										</label>
										<label class="radio-inline">
											<form:radiobutton path="webServiceApis[0].paginationRequired" class="wsApiField paginationRequired" value="no"/>
											<spring:message code="anvizent.package.button.no"/>
										</label>
								   </div>		
								</div>
							</div>
							  <div class="row form-group paginationType" style="display:none;">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.paginationType" />:</label>
								<div class="col-sm-8">
								   <label class="radio-inline">
										<form:radiobutton path="webServiceApis[0].paginationType" class="wsApiField paginationOffsetDateType paginationOffset" value="offset"/>
										<spring:message code="anvizent.package.label.paginationoffset"/>
									</label>
									<label class="radio-inline">
										<form:radiobutton path="webServiceApis[0].paginationType" class="wsApiField paginationOffsetDateType paginationDate" value="date"/>
										<spring:message code="anvizent.package.label.paginationdate"/>
									</label>
									<label class="radio-inline">
										<form:radiobutton path="webServiceApis[0].paginationType" class="wsApiField paginationOffsetDateType paginationIncrementalDate" value="incrementaldate"/>
										<spring:message code="anvizent.package.label.incrementalpaginationdate"/>
									</label>
									<label class="radio-inline">
											<form:radiobutton path="webServiceApis[0].paginationType" class="wsApiField paginationOffsetDateType paginationOther" value="hypermedia"/>
											<spring:message code="anvizent.package.label.pagination.nextpage"/>
									</label>
									<label class="radio-inline">
										<form:radiobutton path="webServiceApis[0].paginationType" class="wsApiField paginationOffsetDateType paginationConditionalDate" value="conditionaldate"/>
										<spring:message code="anvizent.package.label.pagination.conditionaldate"/>
									</label>
									<label class="radio-inline">
										<form:radiobutton path="webServiceApis[0].paginationType" class="wsApiField paginationOffsetDateType paginationPageNumberSize" value="page"/>
										<spring:message code="anvizent.package.label.pagination.page"/>
									</label>
									</div>
								 <div class="col-sm-2 paginationParamTypeDiv">
									<select class="paginationParamType form-control">
										<option value="Request Parameter"><spring:message code="anvizent.package.message.RequestParameter" /></option>
										<option value="Body Parameter" class="paginationBodyParamType"> <spring:message code="anvizent.package.message.BodyParameter" /></option>
								    </select>
							     </div>
							        
							</div>
						<div class="row form-group paginationDateType" id="paginationDateType" style="display:none">
						    <div class="row form-group">
						     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.datepagination"/> :</label>
							 <div class="col-sm-2">
								 <input type="text" placeholder="From Date Param Name" title ="From Date Param Name" class="form-control paginationStartDateParam"/>
							 </div>
							  <div class="col-sm-2">
								 <input type="text" placeholder="To Date Param Name" title ="To Date Param Name" class="form-control paginationEndDateParam"/>
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Starts From" title ="start date" class="form-control paginationStartDate" >
						     </div>
						      <div class="col-sm-3">
						      <select class="paginationDateRange col-sm-4 form-control"  title="day range">
									  <c:forEach var="dr" begin="0" end="364">
					                        <option value="${dr+1}">${dr+1}</option>
					                    </c:forEach>
									 
							  </select>
							</div>
							</div>
							<div class="row form-group">
							     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
								 <div class="col-sm-3">
									 <input type="text" placeholder="Param Name(optional)" title ="Param Name(optional)" class="form-control paginationDatePageNumberRequestParamName"/>
								 </div>
								 <div class="col-sm-1">
								 :
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Starts From(optional)" title ="Starts From(optional)" class="form-control paginationDatePageNumberRequestParamValue" >
							</div>
					        </div>
							 <div class="row form-group">
							  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
								 <div class="col-sm-3">
									 <input type="text" placeholder="Param Name(optional)"  title ="Param Name(optional)" class="form-control paginationDatePageSizeRequestParamName"/>
								 </div>
								 <div class="col-sm-1">
								 :
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Param Value(optional)" title ="Param Value(optional)" class="form-control paginationDatePageSizeRequestParamValue" >
							     </div>
							  </div>
						  </div>
						  <div class="row form-group paginationIncrementalDateType" id="paginationIncrementalDateType"  style="display: ${webServiceApis.paginationType == 'incrementaldate' ? '':'none' };">
							   <div class="row form-group">
							     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.incrementalpaginationdatepagination"/> :</label>
								 <div class="col-sm-2">
									 <input type="text" placeholder="Start Date Param Name" title ="Start Date Param Name" class="form-control paginationIncrementalStartDateParam"/>
								 </div>
								  <div class="col-sm-2">
							     	<input type="text" placeholder="Start Date" title ="Start Date" class="form-control paginationIncrementalStartDate" >
							     </div>
								  <div class="col-sm-2">
									 <input type="text" placeholder="End Date Param Name" title ="End Date Param Name" class="form-control paginationIncrementalEndDateParam"/>
								 </div>
							     <div class="col-sm-2">
							     	<input type="text" placeholder="End Date(optional)" title ="End Date should be grater than Start date(Day range must be more than 7 days)." class="form-control paginationIncrementalEndDate" >
							     </div>
							     <div class="col-sm-2">
								     <select class="paginationIncrementalDateRange col-sm-4 form-control" title="day range">
												<c:forEach var="dr" begin="0" end="364">
					                             <option value="${dr+1}">${dr+1}</option>
					                           </c:forEach>
									</select>
								</div>
								</div>
						<div class="row form-group">
						     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
							 <div class="col-sm-3">
								 <input type="text" placeholder="Param Name(optional)" title ="Param Name(optional)" class="form-control paginationIncrementalDatePageNumberRequestParamName"/>
							 </div>
							 <div class="col-sm-1">
							 :
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Starts From(optional)" title ="Starts From(optional)" class="form-control paginationIncrementalDatePageNumberRequestParamValue" >
						</div>
				        </div>
						 <div class="row form-group">
						  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
							 <div class="col-sm-3">
								 <input type="text" placeholder="Param Name(optional)"  title ="Param Name(optional)" class="form-control paginationIncrementalDatePageSizeRequestParamName"/>
							 </div>
							 <div class="col-sm-1">
							 :
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Param Value(optional)" title ="Param Value(optional)" class="form-control paginationIncrementalDatePageSizeRequestParamValue" >
						     </div>
						  </div>
						 </div>
						 
						<div class="row form-group paginationOffSetType" id="paginationOffSetType" style="display:none;">
							  <div class="row form-group">
							     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.offsetparamnameandstartsfrom"/> :</label>
								 <div class="col-sm-3">
									 <input type="text" placeholder="Param Name" title ="param name" class="form-control paginationOffSetRequestParamName"/>
								 </div>
								 <div class="col-sm-1">
								 :
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Starts From" title ="Starts From" class="form-control paginationOffSetRequestParamValue" >
							     </div>
							  </div>
							  <div class="row form-group">
							  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.limitparamnameandvalue"/> :</label>
								 <div class="col-sm-3">
									 <input type="text" placeholder="Param Name"  class="form-control paginationLimitRequestParamName"/>
								 </div>
								 <div class="col-sm-1">
								 :
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Param Value" class="form-control paginationLimitRequestParamValue" >
							     </div>
							  </div>
							  <div class="row form-group">
							     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.paginationObjectName"/> :</label>
								 <div class="col-sm-8">
									 <input type="text" placeholder="Pagination Object Name" title ="param name" class="form-control paginationObjectName"/>
								 </div>
							 </div>
							 
							 <div class="row form-group">
							     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.paginationSearchId"/> :</label>
								 <div class="col-sm-8">
									 <input type="text" placeholder="Pagination SearchId Param" title ="param name" class="form-control paginationSearchId"/>
								 </div>
							 </div>
							 
							 <div class="row-form-group">
										<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.PaginationSoapBody"/> </label>
											<div class="col-sm-8">
												<textarea  class="form-control PaginationSoapBody"  placeholder="Enter Pagination Soap Body "></textarea> 
											</div>
							 </div>
							  
							</div>
							<div class="row form-group paginationPageNumberSizeType" id="paginationPageNumberSizeType" style="display:none;">
							  <div class="row form-group">
							     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
								 <div class="col-sm-3">
									 <input type="text" placeholder="Param Name" title ="Param Name" class="form-control paginationPageNumberRequestParamName"/>
								 </div>
								 <div class="col-sm-1">
								 :
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Starts From" title ="Starts From" class="form-control paginationPageNumberRequestParamValue" >
							     </div>
							  </div>
							  <div class="row form-group">
							  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
								 <div class="col-sm-3">
									 <input type="text" placeholder="Param Name"  class="form-control paginationPageSizeRequestParamName"/>
								 </div>
								 <div class="col-sm-1">
								 :
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Param Value" class="form-control paginationPageSizeRequestParamValue" >
							     </div>
							  </div>
							</div>
							<div class="row form-group paginationOtherType" id = "paginationOtherType" style="display: none;">
							   <div class="row form-group">
							     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagination.nextpage.pattern.and.Limit"/> :</label>
								 <div class="col-sm-3">
									 <input type="text" placeholder="Key Param"  class="form-control paginationOtherRequestKeyParam"/>
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Limit" class="form-control paginationOtherRequestLimit" >
							     </div>
							   </div>
							</div>

						<div class="row form-group paginationConditionalType" id="paginationConditionalDate" style="display: none">
							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.pagination.filtercondition"/>:</label>
								<div class="col-sm-3">
									<input type="text" placeholder="{ $filter }" class="form-control paginationFilterName">
								</div>
								<div class="col-sm-3">
									<select class="paginationConditionalDayRange col-sm-4 form-control"  title="date range">
										 <option value="0"><spring:message code="anvizent.package.label.pagination.selectDayRange"/></option>
									  <c:forEach var="dr" begin="1" end="365">
			                             <option value="${dr}">${dr}</option>
			                           </c:forEach>
							  		</select>
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-2"></label>
								<div class="col-sm-3">
									<input type="text" placeholder="From date param" class="form-control paginationConditionalFromDateParam">
								</div>
								<div class="col-sm-2">
									<input type="text" placeholder="{EX: >, <, =, ge , le }" class="form-control paginationConditionalFromDateCondition">
								</div>
								<div class="col-sm-3">
									<input type="text" placeholder="From date" class="form-control paginationConditionalFromDate">
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-2"></label>
								<div class="col-sm-3">
									<input type="text" placeholder="{ $condition Ex: { and, or } }" class="form-control paginationConditionalParam">
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-2"></label>
								<div class="col-sm-3">
									<input type="text" placeholder="To date param" class="form-control paginationConditionalToDateParam">
								</div>
								<div class="col-sm-2">
									<input type="text" placeholder="{ EX: >, <, =, gt, lt }" class="form-control paginationConditionalToDateCondition">
								</div>
								<div class="col-sm-3">
									<input type="text" placeholder="To date(optional)" class="form-control paginationConditionalToDate">
								</div>
							</div>
						</div>

						<div class="row form-group">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.incrementalUpdate" /></label>
							<div class="col-sm-10">
								<form:checkbox path="webServiceApis[0].incrementalUpdate" cssClass="wsApiField incrementalUpdate" />
								<form:hidden path="webServiceApis[0].incrementalUpdateparamdata" cssClass="wsApiField incrementalUpdateparamdata"/>
							</div>
						</div>
						
						<div class="row form-group incrementalUpdateDetailsDiv" style="display: none;">
							<label class="control-label col-sm-12"><spring:message code="anvizent.package.label.incrementalUpdate" /><spring:message code="anvizent.package.message.Details" /> :</label>
							<div>
								<input type="hidden" class="incrementalUpdateParamColumnName">
								<div class=" col-sm-4"><input type="text" class="incrementalUpdateParamName form-control" placeholder="Paramter Name"></div>
								<div class=" col-sm-4"><input type="text" class="incrementalUpdateParamvalue col-sm-4 form-control" placeholder="Ex: /* {date} */"></div>
								<div class=" col-sm-4">
									<select class="incrementalUpdateParamType col-sm-4 form-control">
										<option><spring:message code="anvizent.package.message.RequestParameter" /></option>
										<option class="incrementalUpdateBodyParamType"><spring:message code="anvizent.package.message.BodyParameter" /></option>
									</select>
								</div>	
							</div>
						</div>
			
						 <div class="row-form-group">
							<div class="row form-group soapBodyElement">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.soapBodyElement"/> :</label>
								<div class="col-sm-8">
									<form:textarea path="webServiceApis[0].soapBodyElement" cssClass="form-control wsApiField soapBodyElement"/>
								</div>
							</div>
						  </div>
				
						<div class="row form-group">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.responseobjectname"/></label>
							<div class="col-sm-10">
								<form:input path="webServiceApis[0].responseObjectName" cssClass="form-control wsApiField responseObjectName"/>
							</div>
						</div>
						
			
						<div class="row form-group">
											<label class="control-label col-sm-2">Default Mapping :</label>
											<div class="col-sm-10">
											
										<div id="accordion-first" class="clearfix"><!-- standard start accordion -->
	                          <div class="accordionWs" id="accordion2Ws">
		                          <div class="accordion-group">
					                    <div class="accordion-heading" style="border: 1px solid #dce1e4;background:#1096eb;">
					                  <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion2WS" href="#collapseOneWS">
					                  <span class="glyphicon glyphicon-plus-sign"></span>Default Mapping
					                  </a>
					               </div>
						           <div style="height: 0px;" id="collapseOneWS" class="accordion-body collapse">	
							           <div class="panel panel-info">
							                 <div class="panel-body">
												
												 <div id="defaultMappingDiv" class="panel-collapse collapse1 in collapseDiv"  style="margin-top: 5px;"></div>
						        <div class="row-form-group columnsKeyValue" id="columnsParameters" style="display: none">
										 
										<div class="row form-group">
											<div class="col-sm-4">
												<spring:message code = "anvizent.package.label.enterKey" var="enterKey"/>
												<input class="form-control columnKey" placeholder="key">
											</div>
											<div class="col-sm-4">
												<spring:message code = "anvizent.package.label.enterValue" var ="enterValue"/>
												<input class="form-control columnValue" placeholder="value">
											</div>
											<div class="col-sm-2">
												<button type="button" class="btn btn-primary btn-sm addMappingField">
													<span class="glyphicon glyphicon-plus"></span>
												</button>
												<button type="button" class="btn btn-primary btn-sm deleteMappingField">
													<span class="glyphicon glyphicon-trash"></span>
												</button>
											</div>
										</div>
						          </div> 
										    </div>
										</div>
									</div>
								  </div>
							</div>
						</div>
					     </div>
						</div>
					</div>			
					</div>
				</div>	
			</div>
			
		<div id="pathParamDetailsSampleBlock" style="display:none;">
			<div class="panel panel-default pathParamDetailsBlock">
				<div class="panel-heading"><spring:message code="anvizent.package.label.pathParamsDetails"/></div>
				<div class="panel-body">
					<div class="row form-group">
						<label class="control-label col-sm-2 pathParamName"></label>
						<div class="col-sm-9">
							<div class="methodTypeValidation">
								<label class="radio-inline">
									<input type="radio" name="pathParamValueType" value="M" class="pathParamValueType">
									<spring:message code="anvizent.package.label.manual"/>
								</label>
								<label class="radio-inline">
									<input type="radio" name="pathParamValueType" value="S" class="pathParamValueType">
									<spring:message code="anvizent.package.label.subUrl"/>
								</label>
							</div>
						</div>
					</div>
					
					<div class="subUrlBlock" style="display: none;">
						<div class="row form-group">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.subUrl"/></label>
							<div class="col-sm-9">
								<input type="text" class="form-control subUrl" placeholder="Sub URL"> <br>
								<input type="checkbox" class="baseUrlRequired"/>
								Base URL Required <span class="baseUrlForSubUrl"></span>
							</div>
						</div>
						
						<div class="row form-group">
							<label class="control-label col-sm-2">
								<spring:message code="anvizent.package.label.subUrl"/> <spring:message code="anvizent.package.label.methodType"/>
							</label>
							<div class="col-sm-9">
								<div class="subUrlMethodTypeValidation">
									<label class="radio-inline">
										<input type="radio" name="subUrlMethodType" value="GET" class="subUrlMethodType"><spring:message code="anvizent.package.label.get"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name="subUrlMethodType" value="POST" class="subUrlMethodType"><spring:message code="anvizent.package.label.post"/>
									</label>
								</div>
							</div>
						</div>
						<div class="row form-group">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.ispaginationrequired" />:</label>
							<div class="col-sm-9">
							    <div class="subUrlPaginationValidation">
								   <label class="radio-inline">
										<input type="radio" name="subUrlPaginationRequired" class="subUrlPaginationRequired"  value="yes"/>
										<spring:message code="anvizent.package.button.yes"/> 
									</label>
									<label class="radio-inline">
										<input type="radio" name="subUrlPaginationRequired" class="subUrlPaginationRequired"  value="no"/>
										<spring:message code="anvizent.package.button.no"/>
									</label>
								</div>	
							</div>
						</div>
						 <div class="row form-group subUrlPaginationType" style="display:none;">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.paginationType" />:</label>
							<div class="col-sm-8">
								   <label class="radio-inline">
										<input type="radio"  name = "subUrlPaginationOffsetDateType" class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationOffset" value="offset"/>
										<spring:message code="anvizent.package.label.paginationoffset"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name ="subUrlPaginationOffsetDateType"  class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationDate" value="date"/>
										<spring:message code="anvizent.package.label.paginationdate"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name ="subUrlPaginationOffsetDateType"  class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationIncrementalDate" value="incrementaldate"/>
										<spring:message code="anvizent.package.label.incrementalpaginationdate"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name ="subUrlPaginationOffsetDateType"  class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationOther" value="hypermedia"/>
										<spring:message code="anvizent.package.label.pagination.nextpage"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name ="subUrlPaginationOffsetDateType" class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationConditionalDate" value="conditionaldate"/>
										<spring:message code="anvizent.package.label.pagination.conditionaldate"/>
									</label>
									<label class="radio-inline">
										<input type="radio" name ="subUrlPaginationOffsetDateType" class="wsApiField subUrlPaginationOffsetDateType subUrlPaginationPageNumberSize" value="page"/>
										<spring:message code="anvizent.package.label.pagination.page"/>
									</label>
							</div>
							<div class="col-sm-2 subUrlPaginationParamTypeDiv">
									<select class="subUrlPaginationParamType form-control">
										<option value="Request Parameter"><spring:message code="anvizent.package.message.RequestParameter" /></option>
										<option value="Body Parameter"><spring:message code="anvizent.package.message.BodyParameter" /></option>
								    </select>
							</div>
	                        </div>
							<div class="row form-group subUrlPaginationDateType" id="subUrlPaginationDateType" style="display:none">
							     <div class="row form-group">
							    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.datepagination"/> :</label>
								 <div class="col-sm-2">
									 <input type="text" placeholder="From Date Param Name" title ="From Date Param Name"  class="form-control subUrlPaginationStartDateParam"/>
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Starts From" title ="start date" class="form-control subUrlPaginationStartDate" >
							     </div>
							     <div class="col-sm-2">
									 <input type="text" placeholder="To Date Param Name" title ="To Date Param Name"  class="form-control subUrlPaginationEndDateParam"/>
								 </div>
							      <div class="col-sm-2">
							     <select class="subUrlPaginationDateRange col-sm-4 form-control" title="day range">
										<c:forEach var="dr" begin="0" end="364">
					                        <option value="${dr+1}">${dr+1}</option>
					                    </c:forEach>
								 </select>
								</div>
								</div>
							 <div class="row form-group">
								    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
									 <div class="col-sm-3">
										 <input type="text" placeholder="Param Name"  class="form-control subUrlDatePaginationPageNumberRequestParamName"/>
									 </div>
									 <div class="col-sm-1">
									 :
									 </div>
								     <div class="col-sm-3">
								     	<input type="text" placeholder="Starts From" class="form-control subUrlDatePaginationPageNumberRequestParamValue" >
								     </div>
						      </div>
							  <div class="row form-group">
							    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
								 <div class="col-sm-3">
									 <input type="text" placeholder="Param Name"  class="form-control subUrlDatePaginationPageSizeRequestParamName"/>
								 </div>
								 <div class="col-sm-1">
								 :
								 </div>
							     <div class="col-sm-3">
							     	<input type="text" placeholder="Param Value" class="form-control subUrlDatePaginationPageSizeRequestParamValue" >
							     </div>
							  </div>
							  </div>
							  <div class="row form-group subUrlPaginationIncrementalDateType" id="subUrlPaginationIncrementalDateType" style="display:none">
							    <div class="row form-group">
							    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.incrementalpaginationdatepagination"/> :</label>
								 <div class="col-sm-2">
									 <input type="text" placeholder="From Date Param Name" title ="From Date Param Name"  class="form-control subUrlPaginationIncrementalStartDateParam"/>
								 </div>
								 <div class="col-sm-2">
							     	<input type="text" placeholder="Starts From" title ="start date" class="form-control subUrlPaginationIncrementalStartDate" >
							     </div>
								  <div class="col-sm-2">
									 <input type="text" placeholder="To Date Param Name" title ="To Date Param Name"  class="form-control subUrlPaginationIncrementalEndDateParam"/>
								 </div>
								  <div class="col-sm-2">
							     	<input type="text" placeholder="End Date(optional)" title ="End date" title ="End Date should be grater than Start date(Date range must be more than 7 days)." class="form-control subUrlPaginationIncrementalEndDate" >
							     </div>
							     <div class="col-sm-2">
							     <select class="subUrlPaginationIncrementalDateRange col-sm-4 form-control" title="day range">
										<c:forEach var="dr" begin="0" end="364">
					                       <option value="${dr+1}">${dr+1}</option>
					                     </c:forEach>
								 </select>
								</div>
								</div>
							  <div class="row form-group">
								     <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
									 <div class="col-sm-3">
										 <input type="text" placeholder="Param Name(optional)" title ="Param Name(optional)" class="form-control subUrlPaginationIncrementalDatePageNumberRequestParamName"/>
									 </div>
									 <div class="col-sm-1">
									 :
									 </div>
								     <div class="col-sm-3">
								     	<input type="text" placeholder="Starts From(optional)" title ="Starts From(optional)" class="form-control subUrlPaginationIncrementalDatePageNumberRequestParamValue" >
								</div>
				        </div>
						 <div class="row form-group">
						  <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
							 <div class="col-sm-3">
								 <input type="text" placeholder="Param Name(optional)"  title ="Param Name(optional)" class="form-control subUrlPaginationIncrementalDatePageSizeRequestParamName"/>
							 </div>
							 <div class="col-sm-1">
							 :
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Param Value(optional)" title ="Param Value(optional)" class="form-control subUrlPaginationIncrementalDatePageSizeRequestParamValue" >
						     </div>
						  </div>
					  </div>
						<div class="row form-group subUrlPaginationOffSetType" id="subUrlPaginationOffSetType" style="display:none">
						  <div class="row form-group">
						    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.offsetparamnameandstartsfrom"/> :</label>
							 <div class="col-sm-3">
								 <input type="text" placeholder="Param Name"  class="form-control subUrlPaginationOffSetRequestParamName"/>
							 </div>
							 <div class="col-sm-1">
							 :
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Starts From" class="form-control subUrlPaginationOffSetRequestParamValue" >
						     </div>
						  </div>
						  <div class="row form-group">
						    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.limitparamnameandvalue"/> :</label>
							 <div class="col-sm-3">
								 <input type="text" placeholder="Param Name"  class="form-control subUrlPaginationLimitRequestParamName"/>
							 </div>
							 <div class="col-sm-1">
							 :
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Param Value" class="form-control subUrlPaginationLimitRequestParamValue" >
						     </div>
						  </div>
						</div>
						<div class="row form-group subUrlPaginationPageNumberSizeType" id="subUrlPaginationPageNumberSizeType" style="display:none">
						  <div class="row form-group">
						    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagenumberparamnameandstartsfrom"/> :</label>
							 <div class="col-sm-3">
								 <input type="text" placeholder="Param Name"  class="form-control subUrlPaginationPageNumberRequestParamName"/>
							 </div>
							 <div class="col-sm-1">
							 :
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Starts From" class="form-control subUrlPaginationPageNumberRequestParamValue" >
						     </div>
						  </div>
						  <div class="row form-group">
						    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagesizeparamnameandvalue"/> :</label>
							 <div class="col-sm-3">
								 <input type="text" placeholder="Param Name"  class="form-control subUrlPaginationPageSizeRequestParamName"/>
							 </div>
							 <div class="col-sm-1">
							 :
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Param Value" class="form-control subUrlPaginationPageSizeRequestParamValue" >
						     </div>
						  </div>
						</div>
						<div class="row form-group subUrlPaginationOtherType" id="subUrlPaginationOther" style="display:none">
						  <div class="row form-group">
						    <label class="control-label col-sm-2"><spring:message code = "anvizent.package.label.pagination.nextpage.pattern.and.Limit"/> :</label>
							 <div class="col-sm-3">
								 <input type="text" placeholder="<spring:message code = "anvizent.package.label.placeholder.hyperlink.next.record.pattern"/>" title="<spring:message code = "anvizent.package.label.placeholder.hyperlink.next.record.pattern"/>"  class="form-control subUrlPaginationOtherRequestParamkey"/>
							 </div>
						     <div class="col-sm-3">
						     	<input type="text" placeholder="Limit" class="form-control subUrlPaginationOtherRequestLimit" >
						     </div>
						  </div>
						</div>
						
						<div class="row form-group subUrlPaginationConditionalType" id="subUrlPaginationConditionalDate" style="display: none">
							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.pagination.filtercondition"/>:</label>
								<div class="col-sm-3">
									<input type="text" placeholder="{ $filter }" class="form-control subUrlPaginationFilterName">
								</div>
								<div class="col-sm-3">
									<select class="subUrlPaginationConditionalDayRange col-sm-4 form-control"  title="date range">
										 <option value="0"><spring:message code="anvizent.package.label.pagination.selectDayRange"/></option>
									  <c:forEach var="dr" begin="1" end="365">
			                             <option value="${dr}">${dr}</option>
			                           </c:forEach>
							  		</select>
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-2"></label>
								<div class="col-sm-3">
									<input type="text" placeholder="From date param" class="form-control subUrlPaginationConditionalFromDateParam">
								</div>
								<div class="col-sm-2">
									<input type="text" placeholder="{EX: >, <, =, ge, le }" class="form-control subUrlPaginationConditionalFromDateCondition">
								</div>
								<div class="col-sm-3">
									<input type="text" placeholder="From date" class="form-control subUrlPaginationConditionalFromDate">
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-2"></label>
								<div class="col-sm-3">
									<input type="text" placeholder="{ $condition Ex: { and, or } }" class="form-control subUrlPaginationConditionalParam">
								</div>
							</div>
							<div class="row form-group">
								<label class="control-label col-sm-2"></label>
								<div class="col-sm-3">
									<input type="text" placeholder="To date param" class="form-control subUrlPaginationConditionalToDateParam">
								</div>
								<div class="col-sm-2">
									<input type="text" placeholder="{ EX: >, <, =, ge, le }" class="form-control subUrlPaginationConditionalToDateCondition">
								</div>
								<div class="col-sm-3">
									<input type="text" placeholder="To date(optional)" class="form-control subUrlPaginationConditionalToDate">
								</div>
							</div>
						</div>
						
						<div class="row form-group">
							<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.incrementalUpdate" /></label>
							<div class="col-sm-10">
							<input type="checkbox" name="subUrlIncrementalUpdate" placeholder="Param Name" class="subUrlIncrementalUpdate">
							</div>
						</div>
						
						<div class="row form-group subUrlIncrementalUpdateDetailsDiv" style="display: none;">
							<label class="control-label col-sm-12"><spring:message code="anvizent.package.label.incrementalUpdate" /> <spring:message code="anvizent.package.message.Details" />:</label>
							<div>
								<input type="hidden" class="subUrlIncrementalUpdateParamColumnName">
								<div class=" col-sm-4"><input type="text" class="subUrlIncrementalUpdateParamName form-control" placeholder="Paramter Name"></div>
								<div class=" col-sm-4"><input type="text" class="subUrlIncrementalUpdateParamvalue col-sm-4 form-control" placeholder="Ex: /* {date} */"></div>
								<div class=" col-sm-4">
									<select class="subUrlIncrementalUpdateParamType col-sm-4 form-control">
										<option><spring:message code="anvizent.package.message.RequestParameter" /></option>
										<option class="subUrlIncrementalUpdateBodyParamType"><spring:message code="anvizent.package.message.BodyParameter" /></option>
									</select>
								</div>	
							</div>
						</div>
						 
						<div class="row form-group">
							<label class="control-label col-sm-2">
								<spring:message code="anvizent.package.label.responseobjectname"/>
							</label>
							<div class="col-sm-9">
								<input type="text" class="form-control subUrlResObj" placeholder="Sub URL Response Object Name"> 
							</div>
						</div>
					</div>
				</div>
			</div>	
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="deleteWSILMappingAlert" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog">
				    <div class="modal-content">
					      <div class="modal-header">
						        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.deleteWebServiceILMapping"/></h4>
					      </div>
					      <div class="modal-body">
						        <p>
						        	<spring:message code="anvizent.package.label.areYouSure,YouWantToDeleteThisWebServiceILMapping"/>
					        	</p>
					      </div>
					      <div class="modal-footer">
						        <button type="button" class="btn btn-primary startLoader" id="confirmDeletedeleteWSILMapping"><spring:message code="anvizent.package.button.yes"/></button>
						        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
					      </div>
				    </div> 
			  </div> 
		</div>
	</form:form>
</div>	