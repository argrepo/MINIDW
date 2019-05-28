<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
        <div class="page-title-v1"><h4><spring:message code="anvizent.package.label.standardPackage"/></h4></div>
        <div class="dummydiv"></div>
		<ol class="breadcrumb">
		</ol>
<div class=''>
<form:form modelAttribute="standardPackageForm" method="POST" id="standardPackageForm">
		
		 <div class="row form-group">
      <h4 class="alignText"><spring:message code="anvizent.package.label.standardPackage"/></h4>
      </div>
      <jsp:include page="_error.jsp"></jsp:include>
		<form:hidden path="packageId"/>  
		<form:hidden path="isStandard"/>
		<form:hidden path="industryId"/>
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
		<input type="hidden" id="iLId" value="">
		<input type="hidden" id="dLId" value="">
		 <div class='col-sm-12'>
			    <div class='row form-group packgaelist_standard'>
						<label class="col-sm-3 col-md-3 col-lg-2 control-label labelsgroup">
						<spring:message code="anvizent.package.label.packageName"/></label>
						<div class='col-sm-4' >
							<c:choose>
							    <c:when test="${not empty standardPackageForm.packageId}">
									<form:input path="packageName" class="form-control" disabled="true"/>
									<form:errors path="packageName" cssStyle="color: #ff0000"/>
							    </c:when>
							    <c:otherwise>
									<form:input path="packageName" class="form-control" />
									<form:errors path="packageName" cssStyle="color: #ff0000"/>
							    </c:otherwise>
							</c:choose>
						</div>
						 <div class="col-md-4">
						  			<c:choose>
									    <c:when test="${empty standardPackageForm.packageId}">
									       <input type="submit" class="btn btn-primary btn-sm btn-primary startLoader" value="<spring:message code="anvizent.package.label.createPackage"/>" 
																id="createStandardPackage" name='createStandardPackage'/>
																
<a href='<c:url value="/adt/standardpackage"/>' class="btn btn-primary btn-sm back_btn startLoader"><spring:message code="anvizent.package.link.back"/></a>
									    </c:when>
									</c:choose>
						  </div>
					 
			  </div>
			  		 
		
		</div>
		
		<c:if test="${not empty standardPackageForm.packageId}">
			<div class='row form-group'>
				<c:if test="${empty userDlList}">
					<div class="alert alert-warning" role="alert"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>  <spring:message code="anvizent.package.message.modulesAreNotAssignedPleaseContactSuperAdmin"/></div>
				</c:if>
				<c:if test="${not empty userDlList}">
					<c:choose>
				 		<c:when test="${empty mappedModuleSatandardPackage && packageCreation == false}">
					  	  	<div class="alert alert-warning" role="alert"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span> 
					     		<spring:message code="anvizent.package.standardPackage.NoModuleMapped"/></div>
						</c:when>
						<c:when test="${not empty mappedModuleSatandardPackage && packageCreation == false}">
							<div class="row form-group">
							<div class='col-sm-12'>
						    <div id="accordion-first" class="clearfix">
				                        <div class="accordion" id="accordion2">
				                          <div class="accordion-group">
				                            <div class="accordion-heading" style="border: 1px solid #dce1e4;">
				                              <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne" >
				                              <span class="glyphicon glyphicon-plus-sign"></span> <spring:message code="anvizent.package.label.mappedModulesDataSets"/> 
				                              </a>
				                            </div>
				                            <div style="height: 0px;" id="collapseOne" class="accordion-body collapse">
				                              <div class="accordion-inner">
				                              </div>
				                    <div class='row form-group'>
								
										<div class="table-responsive">
											<table class="table table-striped table-bordered tablebg">
												<thead>
													<tr>
														<th><spring:message code="anvizent.package.label.sNo"/></th>  
														<th><spring:message code="anvizent.package.label.moduleName"/></th>
														<th><spring:message code="anvizent.package.label.inputLayout"/></th>
														<th></th>
													</tr>
												</thead>
												<tbody>
												
													<c:forEach items="${mappedModuleSatandardPackage}" var="mappedModuleSatandardPackage"  varStatus="increment">
																<tr>
																	<td><c:out value="${increment.index+1}"/></td>	
																	<td><c:out value="${mappedModuleSatandardPackage.dlInfo.dL_name}"/></td>												
																	<td><c:out value="${mappedModuleSatandardPackage.ilInfo.iL_name}"/></td>
																	<td><a class="btn btn-primary btn-sm" href='<c:url value="/adt/package/viewIlSource/${standardPackageForm.packageId}/${mappedModuleSatandardPackage.ilConnectionMapping.dLId}/${mappedModuleSatandardPackage.ilInfo.iL_id}?from=standard" />'><spring:message code="anvizent.package.link.viewSourceDetails"/> </a></td>
																</tr>
														 
													</c:forEach>
												</tbody>
											</table>
										</div>
									
							 
							</div>
				                              </div>
				                            </div>
				                          </div>
				                          
				                        </div><!-- end accordion -->
				                    </div> 
				                    
							</div>
						 
						</c:when>
					</c:choose>
					<div class='col-sm-12'>
						<h3><spring:message code ="anvizent.package.label.modules"/></h3>
						<div class="table-responsive multi-tbl">
							<table class="table table-striped table-bordered tablebg table main-tbl" id="DLSectionTable">
								<thead class="multi-tbl-header">
									<tr>
										<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.sNo"/></th>  
										<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.moduleName"/></th>
										<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.kPI"/></th>
										<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.tableName"/></th>
										<th class="multi-tbl-cnts"></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${userDlList}" var="dLInfo" varStatus="loop">
										<c:choose>
											<c:when test="${dLInfo.isLocked == false}">
												<tr class="accordion-toggle dl-block">
													<td class="main-tbl-row"><c:out value="${dLInfo.dL_id}"/></td>													
													<td class="main-tbl-row">
										    			<label class="radio-inline toolTip" data-toggle="tooltip" data-placement="bottom" title="<c:out value="${dLInfo.description}"/>" style="padding-left: 0px;">
															<button type="button" data-toggle="collapse" data-target=".il-inner-block${loop.index}" class="inner-tbl dlSelection module_expand" data-dLId='<c:out value="${dLInfo.dL_id}"/>' data-dLName='<c:out value="${dLInfo.dL_name}"/>'>
																<i class="inner-tbls fa fa-caret-down" aria-hidden="true"></i>
															</button>	
															<c:out value="${dLInfo.dL_name}"/>
										    			</label>
										    		</td>
										    		<td class="main-tbl-row">
										    			<c:choose>
										    				<c:when test="${not empty dLInfo.kpi}">	
											    				<c:forEach var="kpi" items="${dLInfo.kpi}">	
											    					<label class="" for="${kpi}">
											    					<c:if test="${principal.isTrailUser == true}">
											    						<c:if test="${kpi == 'Returns'}">
											    							<img src="<c:url value="/resources/images/lock.png"/>" class="" alt="Responsive image" style="opacity: 0.3;">
											    						</c:if>
											    					</c:if>
											    					<c:if test="${principal.isTrailUser == false}">
											    						<c:if test="${kpi == 'Returns'}">
											    							<input type="checkbox" checked="checked" disabled="disabled">
											    						</c:if>
											    					</c:if>
											    						<c:if test="${kpi != 'Returns'}">
											    							<input type="checkbox" checked="checked" disabled="disabled">
											    						</c:if>
											    						<span style="font-weight:normal;"><c:out value="${kpi}"/></span>
											    					</label><br>
											    				</c:forEach>
										    				</c:when>
										    			</c:choose>
										    		</td>
													<td class="main-tbl-row"><c:out value="${dLInfo.dL_table_name}"/></td>
													<td class="main-tbl-row">
														 <button type='button' class='viewDLSchema btn btn-primary btn-xs' data-dLId='<c:out value="${dLInfo.dL_id}"/>' data-dL_name='<c:out value="${dLInfo.dL_table_name}"/>'><spring:message code ="anvizent.package.button.viewTableStructure"/>  </button>
													</td>
												</tr>
												<tr class="il-block">
													<td colspan="5" class="hiddentablerow">
														<div class="accordian-body collapse il-inner-block il-inner-block<c:out value="${loop.index}"/>">
															<button type="button" class="close close-accordian" aria-label="Close"><span aria-hidden="true">&times;</span></button>
															<div class="table-responsive table-inner-1 table-inner-arrow">
																<table class="table table-inner-1">
																	<thead class="multi-tbl-inner-header">
																		<tr>
																			<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.sNo"/></th>
																			<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.inputLayout"/></th>
																			<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.tableName"/></th>
																			<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.status"/></th>
																			<th class="multi-tbl-cnts"></th>
																			<th class="multi-tbl-cnts"></th>
																			<th class="multi-tbl-cnts"></th>
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
													<td><c:out value="${dLInfo.dL_id}"/></td>													 
													<td colspan="4"> 
														 <label class="radio-inline toolTip disable-text" class='btn btn-primary btn-sm' data-toggle="tooltip" data-placement="bottom" title="<c:out value="${dLInfo.description}"/>">
															 <input type="radio" name="DLSelection" value="" class='dlSelection' disabled="disabled"><c:out value="${dLInfo.dL_name}"/> 
														</label>
														<img src="<c:url value="/resources/images/lock.png"/>" class="img-responsive lock-symbol" alt="Responsive image">
										    		</td>										    		
												</tr>												
												 
											</c:otherwise>
										</c:choose>										
									</c:forEach>
									</tbody></table></div>
					</div>
					
					<div class='col-sm-12'>
						<h3>IL Info</h3>
						<div class="table-responsive multi-tbl">
							<table class="table table-striped table-bordered tablebg table main-tbl" id="ILSectionTable">
								<thead class="multi-tbl-header">
									<tr>
										<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.sNo"/></th>  
										<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.inputLayout"/></th>
										<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.tableName"/></th>
										<th class="multi-tbl-cnts"></th>
										<th class="multi-tbl-cnts"></th>
										<th class="multi-tbl-cnts"></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${userIlList}" var="iLInfo" varStatus="loop">
												<tr class="accordion-toggle dl-block">
													<td class="main-tbl-row"><c:out value="${iLInfo.iL_id}"/></td>													
													<td class="main-tbl-row">
															<c:out value="${iLInfo.iL_name}"/>
										    		</td>
													<td class="main-tbl-row"><c:out value="${iLInfo.iL_table_name}"/></td>
													<td class='smalltd'> 
														<a class='btn btn-primary btn-sm table_btn startLoader' href='/adt/standardpackage/addILSource/${standardPackageForm.packageId }/${iLInfo.iL_id}'>
															<spring:message code ="anvizent.package.label.addSource"/> 
														</a> 
													</td>
													<td class='smalltd'>
													<a class='btn btn-primary table_btn btn-sm' href='/adt/package/viewIlSource/${standardPackageForm.packageId }/${iLInfo.iL_id}/?from=standard'>
				 										<spring:message code ="anvizent.package.link.viewSourceDetails"/> 
				 									</a></td>
				 									<td class='smalltd'><button type='button' class='viewILSchema btn btn-primary table_btn btn-sm' data-iLId='${iLInfo.iL_id}' data-iLName='${iLInfo.iL_table_name}'>
				 										<spring:message code ="anvizent.package.button.viewTableStructure"/> 
				 										</button>
				 									</td>
												</tr>
												
												<tr class="il-block">
													<td colspan="6" class="hiddentablerow">
														<div class="accordian-body collapse il-inner-block il-inner-block<c:out value="${loop.index}"/>">
															<button type="button" class="close close-accordian" aria-label="Close"><span aria-hidden="true">&times;</span></button>
															<div class="table-responsive table-inner-1 table-inner-arrow">
																<table class="table table-inner-1">
																	<thead class="multi-tbl-inner-header">
																		<tr>
																			<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.sNo"/></th>
																			<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.inputLayout"/></th>
																			<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.tableName"/></th>
																			<th class="multi-tbl-cnts"><spring:message code ="anvizent.package.label.status"/></th>
																			<th class="multi-tbl-cnts"></th>
																			<th class="multi-tbl-cnts"></th>
																			<th class="multi-tbl-cnts"></th>
																		</tr>
																	</thead>
																	<tbody class="multi-tbl-inner-body">
																		
																	</tbody>
																</table>	
															</div>
														</div>
													</td>
												</tr>
												 
									</c:forEach>
									</tbody></table></div>
					</div>
					
				</c:if>
				<div class="pull-left col-sm-12">
					<c:choose>
						<c:when test="${standardPackageForm.pageMode == 'schedule'}">
							<a type="reset" href="<c:url value="/adt/package/schedule"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
						</c:when>
						<c:otherwise>
							<a href="<c:url value="/adt/standardpackage" />" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</c:if>
		
		<div class="row form-group">
			
		</div>
		
</form:form>   
		<div class="modal fade" tabindex="-1" role="dialog" id="viewSchema" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width:65%;"> 
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title" id="viewSchemaHeader"></h4>
		      </div>
		      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
		        	<div class="table-responsive" >
						<table class="table table-striped table-bordered tablebg" id="viewSchemaTable">
							<thead>
								<tr>
									<th><spring:message code="anvizent.package.label.sNo"/></th> 
									<th><spring:message code="anvizent.package.label.columnName"/></th>
									<th><spring:message code="anvizent.package.label.dataType"/></th>
									<th><spring:message code="anvizent.package.label.columnSize"/></th>
									<th><spring:message code="anvizent.package.label.pk"/></th>
									<th><spring:message code="anvizent.package.label.nn"/></th>
									<th><spring:message code="anvizent.package.label.ai"/></th>
									<th><spring:message code="anvizent.package.label.default"/></th>
								</tr>
							</thead>
							<tbody>
								
							</tbody>
						</table>
					</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="viewSourceDetailsPoUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="viewSourceDetailsPoUpHeader"></h4>
		      </div>
		      <div class="modal-body">
		      	
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
		
</div>
</div>