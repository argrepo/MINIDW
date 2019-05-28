<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<spring:message code="anvizent.package.label.yes" var="yesVar"/>
<spring:message code="anvizent.package.label.no" var="noVar"/>

<div class="col-sm-12 rightdiv">
	<div class="page-title-v1">
		<h4>
			<spring:message code="anvizent.package.header" />
		</h4>
	</div>
	<div class="dummydiv"></div>
	<ol class="breadcrumb">
	</ol>
	<c:if test="${isStandard == true}">
		<div class="row form-group">
			<h4 class="alignText">
				<spring:message code="anvizent.package.label.standardPackage" />
			</h4>
		</div>
	</c:if>
	<c:if test="${isStandard == false}">
		<div class="row form-group">
			<h4 class="alignText">
				<spring:message code="anvizent.package.label.customPackage" />
			</h4>
		</div>
	</c:if>
	<jsp:include page="_error.jsp"></jsp:include>
	
	<form:form method="POST">
		<input type="hidden" id="userID"
			value="<c:out value="${principal.userId}"/>">
		<div class="tab-content">
			<div class="row" style="padding: 0px 10px;">
				<div class="col-sm-12" style="padding: 5px; border-radius: 4px;">
					<label class="control-label col-sm-4 col-md-3 col-lg-2 labelsgroup"><spring:message
							code="anvizent.package.label.filterPackagesBy" /></label>
					<div class="col-sm-4 col-md-3 col-lg-2">
						<select id="filterPackages" class="form-control">
							<option value="all"><spring:message
									code="anvizent.package.label.all" /></option>
							<option value="active" selected><spring:message
									code="anvizent.package.label.active" /></option>
							<option value="deleted"><spring:message
									code="anvizent.package.label.Archived" /></option>
						</select>
					</div>
					<c:if test="${isStandard == true}">
						<div class="row">
							<div class="pull-right">
								<a href='<c:url value="/adt/standardpackage/new"/>'
									class="btn btn-primary btn-sm createpackage startLoader"><spring:message
										code="anvizent.package.button.createStandardPackage" /></a>
							</div>
						</div>
					</c:if>
					<c:if test="${isStandard == false}">
						<div class="row">
							<div class="pull-right">
								<button type="button" class="btn btn-primary"
									id="archievePackage" style="display:none">
									<spring:message code="anvizent.package.label.archive"/>
								</button>
							
								<a href='<c:url value="/adt/package/customPackage/new"/>'
									class="btn btn-primary btn-sm createpackage startLoader"><spring:message
										code="anvizent.package.button.createCustomPackage" /></a>
							</div>
						</div>
					</c:if>
				</div>
			</div>
			<c:if test="${isStandard == true}">
				<div role="tabpanel" class="tab-pane fade active in tabpad"
					id="standardPackages" aria-labelledby="standardPackages">
					<div class="row form-group">
						<div class="table-responsive">
							<table class="table table-striped table-bordered tablebg "
								id="standardPackageTable">
								<thead>
									<tr>
										<th><spring:message
												code="anvizent.package.label.packageId" /></th>
										<th><spring:message
												code="anvizent.package.label.packageName" /></th>
										<th><spring:message
												code="anvizent.package.label.isActive" /></th>
										<th><spring:message
												code="anvizent.package.label.scheduled" /></th>
										<th class="smalltd"><spring:message
												code="anvizent.package.label.edit" /></th>
										<th class="smalltd"><spring:message
												code="anvizent.package.label.archive" /></th>
										<th><spring:message
												code="anvizent.package.label.viewResults" /></th>
									</tr>
								</thead>
								<tbody>

									<c:forEach items="${userPackageList}" var="userPackage"
										varStatus="index">
										<c:if
											test="${userPackage.isStandard == true && userPackage.isActive == true}">
											<tr>
												<td><c:out value="${userPackage.packageId}" /></td>
												<td style="word-break: break-all; white-space: normal;"><c:out
														value="${userPackage.packageName}" /></td>
												<td><c:out
														value="${userPackage.isActive == true ? yesVar : noVar}" />
												</td>
												<td><c:if test="${userPackage.isActive == true}">
														<c:out
															value="${userPackage.isScheduled == true ? yesVar : noVar}" />
													</c:if> <c:if test="${userPackage.isActive == false}">
														<spring:message code="anvizent.package.button.no" />
													</c:if></td>
												<c:choose>
													<c:when test="${userPackage.isActive == true}">
														<td class="smalltd"><a
															class="btn btn-primary btn-sm tablebuttons startLoader"
															href="<c:url value="/adt/standardpackage/edit/${userPackage.packageId}"/>"><i
																class="fa fa-pencil"
																title="<spring:message code="anvizent.package.label.edit"/>"
																aria-hidden="true"></i></a></td>
														<td class="smalltd"><button type="button"
																class="btn btn-primary btn-sm deletePackage tablebuttons"
																data-id="<c:out value="${userPackage.packageId}"/>"
																data-pkgName="<c:out value="${userPackage.packageName}"/>"
																title="<spring:message code="anvizent.package.label.archive"/>">
																<span class="glyphicon glyphicon-folder-close"
																	aria-hidden="true"></span>
															</button></td>
													</c:when>
													<c:otherwise>
														<td class="smalltd"><a
															class="btn btn-primary btn-sm tablebuttons disabled"><i
																class="fa fa-pencil"
																title="<spring:message code="anvizent.package.label.edit"/>"
																aria-hidden="true"></i></a></td>
														<td class="smalltd"><button type="button"
																class="btn btn-primary btn-sm  disabled tablebuttons"
																title="<spring:message code="anvizent.package.label.archive"/>">
																<span class="glyphicon glyphicon-folder-close"
																	aria-hidden="true"></span>
															</button></td>
													</c:otherwise>
												</c:choose>
												<c:if test="${userPackage.isActive == true}">
													<c:choose>
														<c:when test="${userPackage.isScheduled == true}">
															<td><a
																class="btn btn-primary btn-sm tablebuttons startLoader"
																href="<c:url value="/adt/package/jobResults/${userPackage.packageId}"/>"><spring:message
																		code="anvizent.package.label.viewResults" /></a></td>
														</c:when>
														<c:otherwise>
															<td><spring:message code="anvizent.package.label.nA" />
															</td>
														</c:otherwise>
													</c:choose>
												</c:if>
												<c:if test="${userPackage.isActive == false}">
													<c:choose>
														<c:when test="${userPackage.isScheduled == true}">
															<td><a
																class="btn btn-primary btn-sm tablebuttons disabled startLoader"
																href="<c:url value="/adt/package/jobResults/${userPackage.packageId}"/>"><spring:message
																		code="anvizent.package.label.viewResults" /></a></td>
														</c:when>
														<c:otherwise>
															<td><spring:message code="anvizent.package.label.nA" />
															</td>
														</c:otherwise>
													</c:choose>
												</c:if>

											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>

				</div>
			</c:if>
			<c:if test="${isStandard == false}">
				<div role="tabpanel" class="tab-pane fade active in tabpad"
					id="customPackages" aria-labelledby="customPackages">
					<div class="row form-group">
						<div class="table-responsive">

							<table class="table table-striped table-bordered tablebg"
								id="customPackageTable">
								<thead>
									<tr>
										<th><spring:message code = "anvizent.package.label.selectAll"/></th>
										<th><spring:message
												code="anvizent.package.label.packageId" /></th>
										<th><spring:message
												code="anvizent.package.label.packageName" /></th>
										<th><spring:message
												code="anvizent.package.label.isActive" /></th>
										<th><spring:message
												code="anvizent.package.label.isProcessed" /></th>
										<th><spring:message
												code="anvizent.package.label.scheduled" /></th>
										<th class="smalltd"><spring:message
												code="anvizent.package.label.edit" /></th>
										<th class="smalltd"><spring:message
												code="anvizent.package.label.archive" /></th>
										 <th class="smalltd"><spring:message
												code="anvizent.package.label.clone" /></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${userPackageList}" var="userPackage"
										varStatus="index">
										<c:if
											test="${userPackage.isStandard == false && userPackage.isActive == true}">
											<tr>
												<td><input type="checkbox" class="check" data-pkgName = "${userPackage.packageName}" data-tableid="${userPackage.packageId}"></td>
												<td><c:out value="${userPackage.packageId}" /></td>
												<td style="word-break: break-all; white-space: normal;"><c:out
														value="${userPackage.packageName}" /></td>
												<td><c:out
														value="${userPackage.isActive == true ? yesVar : noVar}" /></td>
												<td><c:if
														test="${not empty userPackage.table.tableName}">
														<b><spring:message
																code="anvizent.package.label.targetTable" /></b> :<c:out
															value="${userPackage.table.tableName}" />
														<br>
														<b><spring:message
																code="anvizent.package.label.status" /></b> :
																	<c:choose>

															<c:when test="${userPackage.table.isProcessed}">
																<a class="startLoader"
																	href="<c:url value="/adt/package/viewResultsForCustomPackage/${userPackage.packageId}"/>"
																	data-packageid="<c:out value="${userPackage.packageId}"/>"
																	data-industryid="<c:out value="${userPackage.industry.id}"/>"
																	data-pname="<c:out value="${userPackage.packageName}"/>">
																	<spring:message code="anvizent.package.link.completed" />
																</a>
															</c:when>
															<c:otherwise>
																<spring:message code="anvizent.package.label.pending" />
															</c:otherwise>
														</c:choose>
													</c:if> <c:if test="${empty userPackage.table.tableName}">
														<spring:message code="anvizent.package.button.no" />
													</c:if></td>
												<td><c:if test="${userPackage.isActive == true }">
														<c:out
															value="${userPackage.isScheduled == true ? yesVar : noVar}" />
													</c:if> <c:if test="${userPackage.isActive == false }">
														<spring:message code="anvizent.package.button.no" />
													</c:if></td>
												<td class="smalltd">
												<c:if test="${userPackage.isActive == true }">
																<%-- <!-- View -->
																<a
																	class="btn btn-primary btn-sm tablebuttons startLoader"
																	href="<c:url value="/adt/package/customPackage/edit/${userPackage.packageId}"/>">
																	<span class="glyphicon glyphicon-eye-open"
																	title="<spring:message code="anvizent.package.label.view"/>"></span>
																</a> --%>
																<!--Edit  -->
																<a
																	class="btn btn-primary btn-sm tablebuttons startLoader"
																	href="<c:url value="/adt/package/customPackage/edit/${userPackage.packageId}"/>">
																	<i class="fa fa-pencil"
																	title="<spring:message code="anvizent.package.label.edit"/>"
																	aria-hidden="true"></i>
																</a>
													</c:if> 
													<c:if test="${userPackage.isActive == false }">
																<%-- <!-- View -->
																<a class="btn btn-primary btn-sm tablebuttons disabled"
																	href="#">
																	<button type="button"
																		class="btn btn-primary btn-sm   tablebuttons ">
																		<span class="glyphicon glyphicon-eye-open"
																			title="<spring:message code="anvizent.package.title.view"/>"></span>
																	</button>
																</a> --%>
															
																<!--Edit  -->
																<a class="btn btn-primary btn-sm tablebuttons disabled"
																	href="#">
																	<button type="button"
																		class="btn btn-primary btn-sm   tablebuttons ">
																		<i class="fa fa-pencil "
																			title="<spring:message code="anvizent.package.label.edit"/>"
																			aria-hidden="true"></i>
																	</button>
																</a>
													</c:if></td>
												<c:choose>
													<c:when test="${userPackage.isActive == true }">
														<td class="smalltd"><button type="button"
																class="btn btn-primary btn-sm deletePackage tablebuttons"
																data-id="${userPackage.packageId}"
																data-pkgName="${userPackage.packageName}">
																<span class="glyphicon glyphicon-folder-close"
																	Title="<spring:message code="anvizent.package.label.archive"/>"
																	aria-hidden="true"></span>
															</button></td>
													</c:when>
													<c:otherwise>
														<td class="smalltd"><button type="button"
																class="btn btn-primary btn-sm   tablebuttons disabled">
																<span class="glyphicon glyphicon-folder-close"
																	Title="<spring:message code="anvizent.package.label.archive"/>"
																	aria-hidden="true"></span>
															</button></td>
													</c:otherwise>
												</c:choose>
												<td class="smalltd"><button type="button"
													class="btn btn-primary btn-sm cloneCustomPackage"
													data-id="${userPackage.packageId}"
													data-pkgName="${userPackage.packageName}">
													<span Title="<spring:message code="anvizent.package.label.clone"/>"
														aria-hidden="true"></span><spring:message code="anvizent.package.label.clone"/>
													</button></td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>

				</div>
			</c:if>
		</div>
	</form:form>
	<div class="modal fade" tabindex="-1" role="dialog"
		id="showCustomPackageStaus" data-backdrop="static"
		data-keyboard="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<img src="<c:url value="/resources/images/anvizent_icon.png" />"
						class="anvizentLogo" />
					<h4 class="modal-title custom-modal-title"></h4>
				</div>
				<div class="modal-body">
					<h5>
						<spring:message code="anvizent.package.label.targetTable" />
					</h5>
					<div class="table-responsive">
						<table class="table table-striped tablebg "
							id="customTargetTableStaus">
							<thead>
								<tr>
									<th><spring:message
											code="anvizent.package.label.tableName" /></th>
									<th><spring:message
											code="anvizent.package.label.totalRecords" /></th>
									<th><spring:message
											code="anvizent.package.label.processedRecords" /></th>
									<th><spring:message
											code="anvizent.package.label.failedRecords" /></th>
								</tr>
							</thead>
							<tbody>

							</tbody>
						</table>
					</div>
					<div class="table-responsive">
						<h5>
							<spring:message code="anvizent.package.label.derivedTables" />
						</h5>
						<table class="table table-striped tablebg "
							id="customDerivativeTableStaus">
							<thead>
								<tr>
									<th><spring:message
											code="anvizent.package.label.tableName" /></th>
									<th><spring:message
											code="anvizent.package.label.totalRecords" /></th>
									<th><spring:message
											code="anvizent.package.label.processedRecords" /></th>
									<th><spring:message
											code="anvizent.package.label.failedRecords" /></th>
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
		</div>
	</div>
	<div class="modal fade" tabindex="-1" role="dialog"
		id="delePackageAlert" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<img src="<c:url value="/resources/images/anvizent_icon.png" />"
						class="anvizentLogo" />
					<h4 class="modal-title custom-modal-title">
						<spring:message
							code="anvizent.package.label.modalHeader.archivePackage" />
					</h4>
				</div>
				<div class="modal-body">
					<p>
						<spring:message
							code="anvizent.package.message.deletePackage.areYouSureYouWantToDeletePackage" />
					</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary"
						id="confirmDeletePackage">
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
	
	<div class="modal fade" tabindex="-1" role="dialog" id="cloneCustomPackageModal" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content" style="width:150%;">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title" id="packageTitle"></h4>
		      </div>
		      <div class="modal-body">
		      	<input type="hidden" id="packageId">
		      	<div class="row form-group">
		      		<div class="col-sm-3">
		      		  <spring:message code="anvizent.package.label.packageName" />
		      		</div>
		      		<div class="col-sm-6">
		      		  <input type="text" class="form-control" id="packageName" name="packageName" data-minlength="3" data-maxlength="55">
		      		</div>
		      	</div>
		      	
		      	<div>
		      	
		      		<div class='row form-group' id="sourceInfoDiv">
						<div class="col-sm-12">
							<div class="table-responsive" style="overflow-y:overlay;">
								<table class="table table-striped table-bordered tablebg" id="sourceMappingInfoTable">
									<thead>
										<tr>
											<th><spring:message code = "anvizent.package.label.sNo"/></th>
											<th><spring:message code = "anvizent.package.label.type"/></th>
											<th><spring:message code = "anvizent.package.label.fileName"/></th>
											<th><spring:message code = "anvizent.package.label.viewDetails"/></th>
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
		      <div class="modal-footer">
		       <input type="button" class="btn btn-primary btn-sm btn-primary" value="<spring:message code="anvizent.package.label.createPackage"/>" 
															id="createCloneCustomPackage"/>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.cancel"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		
</div>