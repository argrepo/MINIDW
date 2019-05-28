<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<spring:message code="anvizent.package.label.yes" var="yesVar"/>
<spring:message code="anvizent.package.label.no" var="noVar"/>
<div class="col-sm-12 rightdiv">
      <div class="page-title-v1"><h4><spring:message code="anvizent.package.header"/></h4></div>
      <div class="dummydiv"></div>
       <ol class="breadcrumb"> </ol>
      <div class="row form-group">
      	<h4 class="alignText"><spring:message code="anvizent.package.label.archivedPackages"/></h4>
      </div>
      <jsp:include page="_error.jsp"></jsp:include>
      <div class="row form-group hidden" style="padding: 0px 10px;">	   		 
	 		<div class="col-sm-12 packgaelist_standard">
	  			<label class="col-sm-3 col-md-3 col-lg-2 control-label labelsgroup"><spring:message code = "anvizent.package.label.filterPackagesBy"/></label>
	  			<div class="col-sm-4 col-md-3 col-lg-2">
		   			<select id="filterPackages" class="form-control">
		   				<option value="all" selected><spring:message code = "anvizent.package.label.all"/></option>
		   				<option value="standard"><spring:message code = "anvizent.package.label.standard"/></option>
		   				<option value="custom"><spring:message code = "anvizent.package.label.custom"/></option>	
		   			</select>
	   			</div>
	  		</div>		   		 
		</div>
      <input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
         <div role="tabpanel" class="tab-pane fade active in tabpad" id="" aria-labelledby="disabledPackages"> 		
		      <div class="row form-group">
		         <div class="table-responsive" >
						<table class="table table-striped table-bordered tablebg " id="disabledPackages">
								<thead>
									<tr>
										<th><spring:message code="anvizent.package.label.packageId"/></th>
										<th><spring:message code="anvizent.package.label.packageType" /></th>		
										<th><spring:message code="anvizent.package.label.packageName"/></th>
										<th><spring:message code="anvizent.package.label.verticalDetails"/></th>
										<th><spring:message code="anvizent.package.label.isActive"/></th>
										<th><spring:message code="anvizent.package.label.isMapped"/></th>
										<th><spring:message code="anvizent.package.label.scheduled"/></th>
										<th><spring:message code="anvizent.package.label.activate"/></th>																		
										<%-- <th><spring:message code="anvizent.package.label.delete"/></th>	 --%>
					                    					                    
									</tr>
								</thead>
								<tbody>								
									<c:forEach items="${userPackageList}" var="userPackage" varStatus="index">
										<c:if test="${!userPackage.isActive}">
											<tr>
												<td><c:out value="${userPackage.packageId}"/></td>
												<td><c:if test="${userPackage.isStandard}">
													<spring:message code = "anvizent.package.label.standard"/>
													</c:if> 
													<c:if test="${!userPackage.isStandard}">
													<spring:message code = "anvizent.package.label.custom"/>
													</c:if>
												</td>
												<td>
													<div style="word-break: break-all;white-space: normal;"><span class='packageName'><c:out value="${userPackage.packageName}"/></span>
													</div>
												</td>
												<td><c:out value="${userPackage.industry.name}"/></td>
												<td><c:out value="${userPackage.isActive == true ? yesVar : noVar}"/></td>
												<td><c:out value="${userPackage.isMapped == true ? yesVar : noVar}"/></td>
												<td>
												 <c:if test="${userPackage.isActive == true}">
												 <c:out value=" ${userPackage.isScheduled == true ? yesVar : noVar}"/>
												 </c:if> 
												 <c:if test="${userPackage.isActive == false}">
														 <spring:message code="anvizent.package.label.no"/>			
												 </c:if></td>
												<td class="smalltd"><button
														class="btn btn-primary btn-sm activatePackage"
														data-packageId="<c:out value="${userPackage.packageId}"/>"><spring:message code = "anvizent.package.label.activate"/></button></td>
												<%-- <td class="smalltd"><button
														class="btn btn-primary btn-sm deletePackage"
														data-packageId="<c:out value="${userPackage.packageId}"/>"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button></td> --%>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
						</table>
					</div>
			</div> 
    </div>
   <!--  ActivatePackageAlert model start -->
      <div class="modal fade" tabindex="-1" role="dialog" id="activatePackageAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code = "anvizent.package.label.activatePackage"/></h4>
		      </div>
		      <div class="modal-body">
                <p> <spring:message code="anvizent.package.label.areyousure,youwanttoactivatethispackage"/></p>
		      </div>
		      <div class="modal-footer">
		       <button type="button" class="btn btn-primary" id="confirmActivatePackage"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		<!--  ActivatePackageAlert model end -->
		
		 <!--  DeletePackageAlert model start -->
      <div class="modal fade" tabindex="-1" role="dialog" id="deletePackageAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code = "anvizent.package.label.deletePackage"/></h4>
		      </div>
		      <div class="modal-body">
                <p> <spring:message code="anvizent.package.label.areyousure,youwanttodeletethispackage"/></p>
		      </div>
		      <div class="modal-footer">
		       <button type="button" class="btn btn-primary" id="confirmDeletePackage"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		<!--  DeletePackageAlert model end -->
		
</div>