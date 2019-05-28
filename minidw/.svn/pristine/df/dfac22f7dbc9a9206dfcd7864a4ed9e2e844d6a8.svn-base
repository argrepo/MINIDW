<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
      <div class="page-title-v1"><h4><spring:message code="anvizent.package.header"/></h4></div>
      <div class="dummydiv"></div>
       <ol class="breadcrumb">
		</ol>
     <%-- UI Changes --%> 
      <jsp:include page="_error.jsp"></jsp:include>
      <c:if test="${isStandard == true}">
      <div class="row form-group">
      <h4 class="alignText"><spring:message code = "anvizent.package.label.xrefPackage"/></h4>
      </div>
      </c:if>
      <c:if test="${isStandard == false}">
      <div class="row form-group">
      <h4 class="alignText"><spring:message code = "anvizent.package.label.customPackage"/></h4>
      </div>
      </c:if>
  <form:form method="POST">
   <input type="hidden" id="userID" value="${principal.userId}">
   <div class="tab-content">
   <c:if test="${isStandard == true}">
         <div role="tabpanel" class="tab-pane fade active in tabpad" id="standardPackages" aria-labelledby="standardPackages"> 		
		      <div class="row form-group">
		         <div class="table-responsive" >
							<table class="table table-striped table-bordered tablebg " id="standardPackageTable">
											<thead>
												<tr>
													<th><spring:message code="anvizent.package.label.packageId"/></th>
													<th><spring:message code="anvizent.package.label.packageName"/></th>
													<th><spring:message code="anvizent.package.label.verticalDetails"/></th>
													<th><spring:message code="anvizent.package.label.isActive"/></th>
													<th><spring:message code="anvizent.package.label.isMapped"/></th>
													<th><spring:message code="anvizent.package.label.scheduled"/></th>
								                    <th class="smalltd"><spring:message code="anvizent.package.label.edit"/></th>
								                    <th class="smalltd"><spring:message code="anvizent.package.label.delete"/></th>	
								                    <th><spring:message code="anvizent.package.label.viewResults"/></th> 					                    
												</tr>
											</thead>
											<tbody>
											
												<c:forEach items="${userPackageList}" var="userPackage" varStatus="index">
													<c:if test="${userPackage.isStandard == true && userPackage.isAdvanced == true}">
														<tr>
															<td>${userPackage.packageId}</td>
															<td>${userPackage.packageName}</td>
															<td>${userPackage.industry.name}</td>
															<td>${userPackage.isActive == true ? 'Yes' : 'No'}</td>
															<td>${userPackage.isMapped == true ? 'Yes' : 'No'}</td>
															<td>
																${userPackage.isScheduled == true ? 'Yes' : 'No'}
															</td>
											                <td  class="smalltd"><a class="btn btn-primary btn-sm tablebuttons" href="<c:url value="/adv/package/standardPackage/edit/${userPackage.packageId}"/>" ><span class="glyphicon glyphicon-edit" title="<spring:message code="anvizent.package.label.edit"/>" aria-hidden="true"></span></a></td>
											                <td  class="smalltd"><button type ="button" class="btn btn-primary btn-sm deletePackage tablebuttons" data-id = "${userPackage.packageId}"><span class="glyphicon glyphicon-trash"  title="<spring:message code="anvizent.package.label.delete"/>" aria-hidden="true"></span></button></td>
											                <td  class="smalltd">
											                	<c:choose>
																    <c:when test="${userPackage.isScheduled == true}">
																     	<a class="btn btn-primary btn-sm tablebuttons" href="<c:url value="/adv/package/jobResults/${userPackage.packageId}"/>" ><spring:message code = "anvizent.package.label.viewResults"/></a>   
																    </c:when>    
																    <c:otherwise>
																        N/A
																    </c:otherwise>
																</c:choose>
											                </td>
														</tr>
													</c:if>		
												</c:forEach>
											</tbody>
								</table>
						</div>
						</div> 
					<div class="row form-group">
			          	<div class="pull-left">
								<a href='<c:url value="/adv/package/standardPackage/new"/>' class="btn btn-primary btn-sm createpackage"><spring:message code="anvizent.package.link.createxrefPackag"/></a>
						</div>
					</div>

    </div>
    </c:if>
     <c:if test="${isStandard == false}">
     <div role="tabpanel" class="tab-pane fade active in tabpad" id="customPackages" aria-labelledby="customPackages">
			  <div class="row form-group">
			       <div class="table-responsive">
					
								<table class="table table-striped table-bordered tablebg" id="customPackageTable">
									<thead>
										<tr>
											<th><spring:message code="anvizent.package.label.packageId"/></th>
											<th><spring:message code="anvizent.package.label.packageName"/></th>
											<th><spring:message code="anvizent.package.label.verticalDetails"/></th>
											<th><spring:message code="anvizent.package.label.isActive"/></th>
											<th><spring:message code="anvizent.package.label.isMapped"/></th>
											<th><spring:message code="anvizent.package.label.isProcessed"/></th>
											<th><spring:message code="anvizent.package.label.scheduled"/></th>
						                    <th class="smalltd"><spring:message code="anvizent.package.label.editView"/></th>
						                    <th class="smalltd"><spring:message code="anvizent.package.label.delete"/></th>
										</tr>
									</thead>
									<tbody>
											<c:forEach items="${userPackageList}" var="userPackage" varStatus="index">
													<c:if test="${userPackage.isStandard == false}">
														<tr>
															<td>${userPackage.packageId}</td>
															<td>${userPackage.packageName}</td>
															<td>${userPackage.industry.name}</td>
															<td>${userPackage.isActive == true ? 'Yes' : 'No'}</td>
															<td>${userPackage.isMapped == true ? 'Yes' : 'No'}</td>
															<td>
																<c:if test="${not empty userPackage.table.tableName}">
																	<b>Target Table</b> :${userPackage.table.tableName}<br>
																	<b>Status</b> :
																	<c:choose>
																		<c:when test="${userPackage.table.isProcessed}">
																			<a href="#" class="show-staus" data-packageid="${userPackage.packageId}" 
																			data-industryid="${userPackage.industry.id}" data-pname="${userPackage.packageName}">
																				<spring:message code="anvizent.package.link.done"/>
																			</a>
																		</c:when>
																		<c:otherwise>
																			<spring:message code="anvizent.package.label.pending"/>
																		</c:otherwise>
																	</c:choose>
																</c:if>
																<c:if test="${empty userPackage.table.tableName}">
																	No
																</c:if>
															</td>
															<td>
																${userPackage.isScheduled == true ? 'Yes' : 'No'}
																
															</td>
											                <td  class="smalltd">
											                	<a class="btn btn-primary btn-sm tablebuttons" href="<c:url value="/adv/package/customPackage/edit/${userPackage.packageId}"/>" >
											                		<c:choose>
												                		<c:when test="${userPackage.table.isProcessed}">
												                			<!-- View -->
												                			<span class="glyphicon glyphicon-eye-open" title="<spring:message code="anvizent.package.label.view"/>"></span>
												                		</c:when>
												                		<c:otherwise>
												                			<!--Edit  -->
												                			<span class="glyphicon glyphicon-edit" title="<spring:message code="anvizent.package.label.edit"/>" aria-hidden="true"></span>
												                		</c:otherwise>
											                		</c:choose>
											                	</a>
											                </td>
											                <td  class="smalltd"><button type ="button" class="btn btn-primary btn-sm deletePackage tablebuttons" data-id = "${userPackage.packageId}"><span class="glyphicon glyphicon-trash" Title="Delete" aria-hidden="true"></span></button></td>
														</tr>
													</c:if>		
												</c:forEach>
									</tbody>
								</table>
					</div>
					</div>
				<div class="row form-group">
					<a href='<c:url value="/adv/package/customPackage/new"/>' class="btn btn-primary btn-sm createpackage"><spring:message code="anvizent.package.button.createCustomPackage"/></a>
				</div>
		</div>
		</c:if>
     </div> 
     </form:form>
     <div class="modal fade" tabindex="-1" role="dialog" id="showCustomPackageStaus" data-backdrop="static" data-keyboard="false">
     		<div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"></h4>
		      </div>
		      <div class="modal-body">
		      		<h5><spring:message code="anvizent.package.label.targetTable"/></h5>
		        	<div class="table-responsive">
						<table class="table table-striped tablebg " id="customTargetTableStaus">
							<thead>
								<tr>
									<th><spring:message code="anvizent.package.label.tableName"/></th>
									<th><spring:message code="anvizent.package.label.totalRecords"/></th>
									<th><spring:message code="anvizent.package.label.processedRecords"/></th>
									<th><spring:message code="anvizent.package.label.failedRecords"/></th>
								</tr>
							</thead>
							<tbody>
								
							</tbody>
		        		</table>
		        	</div>
		        	<div class="table-responsive">
		        		<h5><spring:message code="anvizent.package.label.derivedTables"/></h5>
						<table class="table table-striped tablebg " id="customDerivativeTableStaus">
							<thead>
								<tr>
									<th><spring:message code="anvizent.package.label.tableName"/></th>
									<th><spring:message code="anvizent.package.label.totalRecords"/></th>
									<th><spring:message code="anvizent.package.label.processedRecords"/></th>
									<th><spring:message code="anvizent.package.label.failedRecords"/></th>
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
		    </div>
		  </div>
     </div>
     <div class="modal fade" tabindex="-1" role="dialog" id="delePackageAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.modalHeader.archivePackage"/></h4>
		      </div>
		      <div class="modal-body">
		        <p><spring:message code="anvizent.package.message.deletePackage.areYouSureYouWantToDeletePackage"/><!-- &hellip; --></p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary" id="confirmDeletePackage"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
</div>