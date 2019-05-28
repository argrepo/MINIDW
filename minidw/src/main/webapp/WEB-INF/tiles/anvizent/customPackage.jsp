
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
        <div class="page-title-v1"><h4><spring:message code="anvizent.package.label.customPackage"/></h4></div>
        <div class="dummydiv"></div>
        <ol class="breadcrumb">
</ol>
	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.customPackage"/></h4>
	</div>
	<div class='col-sm-12'>
		<form:form modelAttribute="customPackageForm" method="POST" id="customPackageForm">
				<c:if test="${empty customPackageForm.targetTableId}">
					<div class='row form-group' style="display:none;">
							<div class='col-sm-12' >
								<img src="<c:url value="/resources/images/user-steps.png"/>" class="img-responsive img-steps" alt="Responsive image">		
								<img src="<c:url value="/resources/images/user-steps1.png"/>" class="img-responsive hide img-steps" alt="Responsive image">		
								<img src="<c:url value="/resources/images/user-steps2.png"/>" class="img-responsive hide img-steps" alt="Responsive image">		
								<img src="<c:url value="/resources/images/user-steps3.png"/>" class="img-responsive hide img-steps" alt="Responsive image">		
								<img src="<c:url value="/resources/images/user-steps4.png"/>" class="img-responsive hide img-steps" alt="Responsive image">		
							</div>
					</div>
				</c:if>
				<c:if test="${not empty customPackageForm.packageId && 
								not empty customPackageForm.targetTableId && customPackageForm.isProcessed == false 
								&& customPackageForm.isDirect == false}">
					<img src="<c:url value="/resources/images/user-steps3.png"/>" class="img-responsive img-steps" alt="Responsive image">			
				</c:if>
				<c:if test="${not empty customPackageForm.packageId && 
					not empty customPackageForm.targetTableId && customPackageForm.isProcessed == true 
					&& customPackageForm.isDirect == false}">
					<img src="<c:url value="/resources/images/user-steps4.png"/>" class="img-responsive img-steps" alt="Responsive image">	
				</c:if>
				
				<jsp:include page="_error.jsp"></jsp:include>
				<form:hidden path="industryId"/> 
				<form:hidden path="isStandard"/>
				<form:hidden path="isScheduled"/>
				<form:hidden path="tempTablesProcessed"/>
				<c:choose>
				<c:when test="${customPackageForm.isScheduled}">
					<spring:message code="anvizent.package.message.deleteSource.areYouSureYouWantToDeleteTargetTableForScheduled" var="deleteMessage"/>
				</c:when>
				<c:otherwise>
					<spring:message code="anvizent.package.message.deleteSource.areYouSureYouWantToDeleteTargetTable" var="deleteMessage"/>
				</c:otherwise>
				
				</c:choose>
				
				
				<form:hidden path="targetTableId"/>
				<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
				<input type="hidden" id="isTrialUser" value="<c:out value="${principal.isTrailUser}"/>">
				<input type="hidden" id="isSandBox" value="<c:out value="${principal.isSandBox}"/>">
				<c:if test="${not empty customPackageForm.packageId}">
					<div class="row form-group"  style="background-color:#fff;padding:5px;border-radius:4px;">
						
							    <div class='col-sm-6 col-md-4' >
								    <label class="control-label "><spring:message code="anvizent.package.label.packageId"/></label>
									<form:input path="packageId" class="form-control" disabled="true"/>
								</div>
								<div class='col-sm-6 col-md-4' >
								    <label class="control-label ">
										<spring:message code="anvizent.package.label.packageName"/></label>
									<form:input path="packageName" class="form-control"/>
								</div>
								<div class='col-sm-4 col-md-1' >
								<label class="control-label ">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
									<button type="button" class="btn btn-primary btn-sm btn-primary updatePackageName"><spring:message code="anvizent.package.button.update"/></button>
								</div>
						
					</div>
				</c:if>
				<c:if test="${empty customPackageForm.packageId}">
					  <div class="row form-group">
							    <div class='col-sm-9 radio' >
							    		<label> <form:radiobutton path="isFromExistingPackages" value="false" /><spring:message code="anvizent.package.label.createPackage"/></label> &nbsp; &nbsp; &nbsp;
										<label><form:radiobutton path="isFromExistingPackages" value="true" /><spring:message code="anvizent.package.label.createPackageAsToCompleteThisActionSelectAnExistingPackage"/></label>
								</div>
					</div>
					<div class="row form-group">
							<label class="col-sm-3 control-label ">
							<spring:message code="anvizent.package.label.packageName"/></label>
							    <div class='col-sm-4'>
										<form:input path="packageName" class="form-control" />
										<form:errors path="packageName" cssStyle="color: #ff0000"/>
								</div>
					</div>
					<div class="row form-group existingPackageDiv" style="${customPackageForm.isFromExistingPackages?'':'display:none'}">
							    <div class='col-sm-7' >
										<form:select path="existingPackageId" class="form-control">
										<form:option value="0"><spring:message code = "anvizent.package.label.SelectPackage"/></form:option>
											<c:if test="${not empty scheduledPackageList}">
												<c:forEach items="${scheduledPackageList}" var="userPackage"> 
													<form:option value="${userPackage.packageId }"><c:out value="${userPackage.packageName}" /></form:option>
												</c:forEach>
											</c:if>
										</form:select>
										
										<form:errors path="existingPackageId" cssStyle="color: #ff0000"/>
										<div class="alert alert-info" style="margin-top:10px;margin-bottom:0px;">
										<b><spring:message code = "anvizent.package.label.note"/></b> <spring:message code = "anvizent.package.label.noteThisOptionWillCopyOnlySourcesFromTheSelectedPackage"/>
										</div>
										
								</div>
					</div>
				</c:if>
		  		<div class="row form-group">
			  		 <div class="col-md-4">
					  			<c:choose>
								    <c:when test="${empty customPackageForm.packageId}">
								       <input type="submit" class="btn btn-primary btn-sm btn-primary startLoader" value="<spring:message code="anvizent.package.label.createPackage"/>" 
															id="createCustomPackage" name='createCustomPackage'/>
										<a href='<c:url value="/adt/package/custompackage"/>' class="btn btn-primary btn-sm back_btn startLoader"><spring:message code="anvizent.package.link.back"/></a>					
								    </c:when>
								</c:choose>
					  </div>
				 </div>
					
				<c:if test="${not empty customPackageForm.packageId 
							&& empty customPackageForm.isFileHavingSameColumnNames
							&& empty customPackageForm.targetTableId}">
					<div class='row form-group' id="primaryOptionsDiv">
								<div class='col-sm-4'>
									    <label class="radio-inline"><input type="radio" name="customPackage_schema" value="" class='' id='createNewSchema'>
							    <spring:message code = "anvizent.package.label.createTargetTable"/></label>
									</div>
 								<c:if test="${principal.isTrailUser == true}">
							      <div class='col-sm-4'>
									  <label class="radio-inline"><input type="radio" name="customPackage_schema" value="" class='' id='isDerivedTables' disabled="disabled">
							            <spring:message code = "anvizent.package.label.createNewDataSetTables"/> <img src="<c:url value="/resources/images/lock.png"/>" class="img-responsive lock-symbol" alt="Responsive image"></label>  
							   		</div> 
							   </c:if> 
					</div>
				</c:if>
				 <c:choose>
					    <c:when test="${not empty customPackageForm.packageId && not empty customPackageForm.isFileHavingSameColumnNames}">
							<div class='row form-group'>
							<div class='col-sm-4'>
								<label class="control-label ">
									<!-- Is every file having same column names?: -->
									<spring:message code = "anvizent.package.label.allSourcesHavingSameSetOfHeaders"/>
								</label>
							</div>
								<form:radiobutton path="isFileHavingSameColumnNames" value="${customPackageForm.isFileHavingSameColumnNames}" cssClass="hide"/>
								<input type="hidden" value="${customPackageForm.isFileHavingSameColumnNames}" id="isFileHavingSameColumnNames"/>
									<span id="YesOrNoOption">
										<c:if test="${customPackageForm.isFileHavingSameColumnNames == true}">
											<spring:message code = "anvizent.package.label.yes"/>
										</c:if>
										<c:if test="${customPackageForm.isFileHavingSameColumnNames == false}">
											<spring:message code = "anvizent.package.button.no"/>
										</c:if>
									 </span>
								<c:if test="${empty customPackageForm.targetTableId && !customPackageForm.isClientDbTables}">	
									<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.label.edit"/>" id='editAllSourcesHavingSameSetOfHeaders' name=''/>
								
								<div class='col-sm-4' id="isFileHavingSameColumnNamesDiv" style="display: none;">
								    <label class="radio-inline">
								    	<input type="radio" name="isFileHavingSameColumnNames" value="true" class=''>
								    	<spring:message code="anvizent.package.label.yes"/>
								    </label>
						    		<label class="radio-inline">
							    		<input type="radio" name="isFileHavingSameColumnNames" value="false" class=''>
						    			<spring:message code="anvizent.package.button.no"/>
						    		</label>
						    			<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.proceed"/>" id='proceed' name=''/>
						    			<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.undo"/>" id='undoAllSourcesHavingSameSetOfHeaders' name=''/>
								</div>
								</c:if>
							</div>
							
					    </c:when>
					    <c:otherwise>
					    	<div class='row form-group' id="YesOrNoOptionDiv" style="display: none;">
					    	<input type="hidden" value="" id="isFileHavingSameColumnNames"/>
					    	<span id="YesOrNoOption"></span>
					    	<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.label.edit"/>" id='editAllSourcesHavingSameSetOfHeaders' name='' style="display: none;"/>
								<div class='col-md-3 col-sm-3'>	
									<label class="control-label ">
									<spring:message code="anvizent.package.label.allSourcesHavingSameSetOfHeaders"/>
									</label>
									<p class="help-block"><em><spring:message code="anvizent.package.label.noteIfItIsASingleSourceChooseOptionYes"/></em></p>
								</div>
								<div id="isFileHavingSameColumnNamesDiv">
									<div class='col-md-3 col-sm-3'>
									    <label class="radio-inline">
									    	<input type="radio" name="isFileHavingSameColumnNames" value="true" class=''>
									    	<spring:message code="anvizent.package.label.yes"/>
									    </label>
							    		<label class="radio-inline">
								    		<input type="radio" name="isFileHavingSameColumnNames" value="false" class=''>
							    			<spring:message code="anvizent.package.button.no"/>
							    		</label>
							    		
									</div>
									<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.proceed"/>" id='proceed' name=''/>
									<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.undo"/>" id='undoAllSourcesHavingSameSetOfHeaders' name=''style="display: none;"/>
								</div>
							</div>
					    </c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${not empty customPackageForm.targetTableId}">
								<div id="viewTargetTableDiv">
				                   <div class="panel panel-info">
									<div class="panel-heading"><b><spring:message code="anvizent.package.label.targetTable"/></b></div>
										<div class="panel-body">
												<div class="row form-group">
													<label class="col-md-2 control-label" for="targetTableName">
																		<spring:message code="anvizent.package.label.targetTable"/>:</label>
														<div class='col-sm-4' >
															<form:input path="targetTableName" class="form-control targetTableName" disabled="true"/>
														</div>
														<div class="col-md-6">
															<input type="button" class="btn btn-primary btn-sm viewTargetTableStructure" value="<spring:message code="anvizent.package.button.viewTableStructure"/>" id="" name='viewTargetTableStructure' />
															<c:if test="${customPackageForm.isFileHavingSameColumnNames == false}">
															<input type="button" class="btn btn-primary btn-sm viewTargetTableQuery" value="<spring:message code="anvizent.package.button.viewQuery"/>" id="" name='viewQuery' />
														    </c:if>
														    <c:if test="${userPackage.isAdvanced == true && not empty targetTableDirectMappingInfo && customPackageForm.tempTablesProcessed}">
																<input type="button" class="btn btn-primary btn-sm" id="manualMapping" value="<spring:message code="anvizent.package.button.updateMapping"/>" >
														    </c:if>
														    
															<button type="button" class="btn btn-primary btn-sm delete-target-table" title="<spring:message code="anvizent.package.label.modalHeader.deleteTargetTable"/>"> <span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>
														</div>
												</div>
													<c:if test="${not empty derivedTables}">
														<c:forEach items="${derivedTables}" var="derivedTable" varStatus="index">
														<div class="row form-group">
															<label class="col-md-2 control-label" for="">
																			<spring:message code="anvizent.package.label.derivedTable"/>:</label>
															<div class='col-sm-4' >
																<input type="text" class="form-control targetTableName" disabled="disabled" value="<c:out value="${derivedTable.tableName}" />"/>
															</div>
															<div class="col-md-4">
																<input type="button" class="btn btn-primary btn-sm viewTargetTableStructure"   value="<spring:message code="anvizent.package.button.viewTableStructure"/>" id="" name='viewTargetTableStructure' />
																
															    <input type="button" class="btn btn-primary btn-sm viewDerivedTableQuery" id="executionStatus" data-derivedTable="<c:out value="${derivedTable.tableName}"/>" data-tableId="<c:out value="${derivedTable.tableId}"/>" data-targetTableId="<c:out value="${derivedTable.targetTableId}"/>"  value="<spring:message code="anvizent.package.button.viewQuery"/>" id="" name='viewQuery' />
																<button type="button" class="btn btn-primary btn-sm delete-derived-table"  title="<spring:message code="anvizent.package.label.modalHeader.deleteDerivedTable"/>" 
																data-tableId="<c:out value="${derivedTable.tableId}"/>" data-targetTableId="<c:out value="${derivedTable.targetTableId}"/>" > 
																<span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>
															</div>
															</div>
														</c:forEach>
													</c:if>
													<div class="row form-group">
														<%-- <c:if test="${customPackageForm.isScheduled == false}"> --%>
															<div class="col-md-2">
																<a href='<c:url value="/adt/package/schedule?packageId=${customPackageForm.packageId}"/>' class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.schedule"/></a>
															</div>
															<div class="col-md-2">
																<a href='<c:url value="/adt/package/tableBuilder/${customPackageForm.packageId}"/>' class="btn btn-primary btn-sm startLoader"><spring:message code="anvizent.package.button.createDerivedTable"/></a>
															</div>
														<%-- </c:if> --%>
														<%-- <c:if test="${customPackageForm.isScheduled}">
															<div class="col-md-3">
																<a href='<c:url value="/adt/package/tableBuilder/${customPackageForm.packageId}"/>' class="btn btn-primary btn-sm startLoader"><spring:message code="anvizent.package.button.createDerivedTable"/></a>
															</div>
														</c:if> --%>
														
													</div>
											</div>
										</div>
								</div>				
					</c:when>
					<c:otherwise>
						<div id="viewTargetTableDiv" style="display:none;">
				                   <div class="panel panel-info">
									<div class="panel-heading"><b><spring:message code="anvizent.package.label.targetTable"/></b></div>
										<div class="panel-body">
												<div class="row form-group">
													<label class="col-md-2 control-label" for="targetTableNameAfterMapping">
																		<spring:message code="anvizent.package.label.targetTable"/>:</label>
														<div class='col-sm-4' >
															<input type="text" id="targetTableName" class="form-control targetTableName" disabled="disabled"/>
														</div>
														<div class="col-md-4">
															<input type="button" class="btn btn-primary btn-sm viewTargetTableStructure" value="<spring:message code="anvizent.package.button.viewTableStructure"/>" id="" name='viewTargetTableStructure' /> &nbsp;
															<button type="button" class="btn btn-primary btn-sm delete-target-table"> <span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>
														</div>
												</div>
												<div class="row form-group">
													<div class="col-md-2">
														<a href='<c:url value="/adt/package/schedule?packageId=${customPackageForm.packageId}"/>' class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.schedule"/></a>
													</div>
													<div class="col-md-2">
													    <a href='<c:url value="/adt/package/tableBuilder/${customPackageForm.packageId}"/>' class="btn btn-primary btn-sm startLoader"><spring:message code="anvizent.package.button.createDerivedTable"/></a>
													</div>
												</div>
											</div>
										</div>
							</div>
					</c:otherwise>
				</c:choose>
				 
				
				<c:choose>
					<c:when test="${not empty customPackageForm.isFileHavingSameColumnNames}">
						<div id="targetTableSourceDiv">
			                   <div class="panel panel-info">
								<div class="panel-heading"><b><spring:message code = "anvizent.package.label.source"/></b></div>
									<div class="panel-body">
												<c:if test="${not empty targetTableDirectMappingInfo}">
													<div class='row form-group' id="targetTableSourceInfoDiv">
														<div class="col-sm-12">
															<div class="table-responsive" style="overflow-y:overlay;">
																<table class="table table-striped table-bordered tablebg" id="targetTableDirectMappingInfoTable">
																	<thead>
																		<tr>
																			<th><spring:message code = "anvizent.package.label.sNo"/></th>
																			<th><spring:message code = "anvizent.package.label.type"/></th>
																			<th><spring:message code = "anvizent.package.label.fileName"/></th>
																			<th><spring:message code = "anvizent.package.label.view"/></th>
																			<th><spring:message code = "anvizent.package.label.delete"/></th>
																		</tr>
																	</thead>
																	 <tbody>
																	 <c:choose>
																	 <c:when test="${!customPackageForm.isClientDbTables}">
																		<c:forEach items="${targetTableDirectMappingInfo}" var="clientData" varStatus="index">
																				<tr>
																					<td>${index.index+1}</td>
																					<c:if test="${clientData.ilConnectionMapping.isFlatFile == true}">
																						<td><spring:message code = "anvizent.package.label.flatFile"/></td>
																						<td><c:out value="${clientData.ilConnectionMapping.filePath}" /></td>
																						<td class="smalltd">
																								<a class="btn btn-primary btn-sm startLoader"  href="<c:url value="/adt/package/viewCustomPackageSource/${customPackageForm.packageId}/${clientData.ilConnectionMapping.connectionMappingId}"/>" ><spring:message code = "anvizent.package.label.viewDetails"/> </a>  
																						</td>
																					</c:if>
																					<c:if test="${clientData.ilConnectionMapping.isFlatFile == false}">
																						<td><spring:message code = "anvizent.package.label.database"/></td>
																						<td><spring:message code = "anvizent.package.label.nA"/></td>
																						<td  class="smalltd">
																							  <a class="btn btn-primary btn-sm startLoader"  href="<c:url value="/adt/package/viewCustomPackageSource/${customPackageForm.packageId}/${clientData.ilConnectionMapping.connectionMappingId}"/>" ><spring:message code = "anvizent.package.label.viewDetails"/> </a>  	
																								
																						</td>
																					</c:if>
																					 	<td class="smalltd">
																							<button type="button" class="btn btn-primary btn-sm delete-mapping" data-id="${clientData.ilConnectionMapping.connectionMappingId}">
																							<span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>
																						</td>
																				</tr>
																		</c:forEach>
																		</c:when>
																		<c:otherwise>
																		<c:forEach items="${targetTableDirectMappingInfo}" var="clientData" varStatus="index">
																				<tr class='sourceTable'>
																					<td><c:out value="${index.index+1}" /></td>
																					<c:if test="${clientData.ilConnectionMapping.isFlatFile == true}">
																						<td><spring:message code = "anvizent.package.label.flatFile"/></td>
																						<td><c:out value="${clientData.ilConnectionMapping.filePath}" /></td>
																						<td class="smalltd">
																								<a class="btn btn-primary btn-sm startLoader"  href="<c:url value="/adt/package/viewCustomPackageSource/${customPackageForm.packageId}/${clientData.ilConnectionMapping.connectionMappingId}"/>" ><spring:message code = "anvizent.package.label.viewDetails"/></a>  
																						</td>
																					</c:if>
																					<c:if test="${clientData.ilConnectionMapping.isFlatFile == false && clientData.ilConnectionMapping.isHavingParentTable == false}">
																						<td><spring:message code = "anvizent.package.label.database"/></td>
																						<td>N/A</td>
																						<td  class="smalltd">
																							  <a class="btn btn-primary btn-sm startLoader"  href="<c:url value="/adt/package/viewCustomPackageSource/${customPackageForm.packageId}/${clientData.ilConnectionMapping.connectionMappingId}"/>" ><spring:message code = "anvizent.package.label.viewDetails"/></a>  	
																								
																						</td>
																					</c:if>
																						<c:if test="${clientData.ilConnectionMapping.isFlatFile == false && clientData.ilConnectionMapping.isHavingParentTable == true}">
																						<td><spring:message code = "anvizent.package.label.derivedTable"/></td>
																						<td class='parentTable' data-table="<c:out value="${clientData.ilConnectionMapping.parent_table_name}"/>"><c:out value="${clientData.ilConnectionMapping.parent_table_name}" /></td>
																						<td  class="smalltd">
																							   <input type="button" class="btn btn-primary btn-sm viewDerivedTableStructure" value="<spring:message code="anvizent.package.button.viewTableStructure"/>"/>  
																								
																						</td>
																					</c:if>
																				               <td class="smalltd">
																							<button type="button" class="btn btn-primary btn-sm delete-mapping" data-id="<c:out value="${clientData.ilConnectionMapping.connectionMappingId}"/>">
																							<span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>
																						</td>
																				</tr>
																		</c:forEach>
																		</c:otherwise>
																		</c:choose>
																	</tbody> 
																</table>
											
															</div>
														</div>
													</div>
												</c:if>
												
												
												<c:if test="${empty targetTableDirectMappingInfo}">
													<div class='row form-group' id="targetTableSourceInfoDiv" style="display: none;">
														<div class="col-sm-12">
															<div class="table-responsive" style="overflow-y:overlay;">
																<table class="table table-striped table-bordered tablebg" id="targetTableDirectMappingInfoTable">
																	<thead>
																		<tr>
																			<th><spring:message code = "anvizent.package.label.sNo"/></th>
																			<th><spring:message code = "anvizent.package.label.type"/></th>
																			<th><spring:message code = "anvizent.package.label.fileName"/></th>
																			<th><spring:message code = "anvizent.package.label.view"/></th>
																			<th><spring:message code = "anvizent.package.label.delete"/></th>
																		</tr>
																	</thead>
																	 <tbody>
																		
																	</tbody> 
																</table>
											
															</div>
														</div>
													</div>
												</c:if>
													<div class="col-xs-12">
													<div class="row form-group">
														<c:choose>
															 <c:when test="${!customPackageForm.isClientDbTables}">
																<input type="button" class="btn btn-primary btn-sm" value="<spring:message code = "anvizent.package.label.addSource"/>" id="addSource" name='addSource' />
															 </c:when>
															 <c:otherwise>
															 <input type="button" class="btn btn-primary btn-sm startLoader" style="float:left"  id="derivedTableAddSource" value="<spring:message code = "anvizent.package.label.addSource"/>"/> 
															 </c:otherwise>
															 </c:choose>
														 <c:if test="${not empty targetTableDirectMappingInfo
																	&& empty customPackageForm.targetTableId}">
															<div class='col-sm-4'>
																<input type="button" class="btn btn-primary btn-sm" value="<spring:message code = "anvizent.package.button.proceedForMapping"/>" id='proceedForMapping' name=''/>
															</div>
														 </c:if> 
														 <c:if test ="${!customPackageForm.isFileHavingSameColumnNames && !customPackageForm.tempTablesProcessed && not empty customPackageForm.targetTableId}">
														   <div class='col-sm-4'>
																<input type="button" class="btn btn-primary btn-sm" value="<spring:message code = "anvizent.package.button.proceedForMapping"/>" id='proceedForMapping' name=''/>
															</div>
														 </c:if>
														  <c:if test="${empty targetTableDirectMappingInfo}"> 
															<div class='col-sm-4'>
																<input type="button" class="btn btn-primary btn-sm" value="<spring:message code = "anvizent.package.button.proceedForMapping"/>" id='proceedForMapping' name='' style="display: none;"/>
															</div>
														  </c:if> 
													</div>
													</div>
										</div>
									</div>
							</div>
					</c:when>
					<c:otherwise>
						<div id=targetTableSourceDiv style="display:none;">
				                   <div class="panel panel-info">
									<div class="panel-heading"><b><spring:message code = "anvizent.package.label.source"/></b></div>
										<div class="panel-body">
												<div class='row form-group' id="targetTableSourceInfoDiv" style="display: none;">
													<div class="col-sm-12">
														<div class="table-responsive" style="overflow-y:overlay;">
															<table class="table table-striped table-bordered tablebg" id="targetTableDirectMappingInfoTable">
																<thead>
																	<tr>
																		<th><spring:message code = "anvizent.package.label.sNo"/></th>
																			<th><spring:message code = "anvizent.package.label.type"/></th>
																			<th><spring:message code = "anvizent.package.label.fileName"/></th>
																			<th><spring:message code = "anvizent.package.label.view"/></th>
																			<th><spring:message code = "anvizent.package.label.delete"/></th>
																	</tr>
																</thead>
																 <tbody>
																	
																</tbody> 
															</table>
														</div>
													</div>
												</div>
												<div class="row form-group">
													<div class='col-sm-4'>
														<input type="button" class="btn btn-primary btn-sm" value="<spring:message code = "anvizent.package.label.addSource"/>" id="addSource" name='addSource' />
														<input type="button" class="btn btn-primary btn-sm" value="<spring:message code = "anvizent.package.button.proceedForMapping"/>" id='proceedForMapping' name='' style="display: none;"/>
													</div>
												</div>
											</div>
										</div>
							</div>
					</c:otherwise>
				</c:choose>
				 
				<c:if test="${not empty customPackageForm.packageId}">
					<div class="pull-left">		
						<c:choose>
							<c:when test="${customPackageForm.pageMode == 'schedule'}">
								<a type="reset" href="<c:url value="/adt/package/schedule"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back"/></a>
							</c:when>
							<c:otherwise>
								<a href="<c:url value="/adt/package/custompackage" />" class="btn btn-primary back_btn btn-sm"><spring:message code = "anvizent.package.link.back"/></a>
							</c:otherwise>
						</c:choose>
					</div>
				</c:if>
		</form:form>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="targetTableCreationPopUp" data-backdrop="static" data-keyboard="false"
			>
		  <div class="modal-dialog modal-lg" style="width:80%;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code = "anvizent.package.label.tableCreation"/></h4>
		      </div>
		      <div class="modal-body">
			      	<div class='row form-group' style="display: none;" id="targetTableNameDiv">
										<label class="col-md-3 control-label" for="targetTableName"> <spring:message code = "anvizent.package.label.targetTable"/> :</label>
										<div class='col-sm-6' >
											<input type="text" id="targetTableName_creation" class="form-control" data-minlength="1" data-maxlength="64" />
										</div>
						</div>
			        	<div class="table-responsive" style="max-height: 400px;">
							<table class="table table-striped table-bordered tablebg" id="targetTable">
								<thead>
									<tr>
										<th>
											<input type="checkbox" class="m-check-all">
										</th>
										<th style="width: 30%;"><spring:message code = "anvizent.package.label.columnName"/></th>
										<th style="width: 15%;"><spring:message code = "anvizent.package.label.dataType"/></th>
										<th style="width: 15%;"><spring:message code = "anvizent.package.label.length"/></th>
										<th class="smalltd"><spring:message code = "anvizent.package.label.pk"/></th>
										<th class="smalltd"><spring:message code = "anvizent.package.label.nn"/></th>
										<th class="smalltd"><spring:message code = "anvizent.package.label.uq"/></th>
										<th class="smalltd"><spring:message code = "anvizent.package.label.ai"/></th>
										<th style="width: 25%;" class="smalltd"><spring:message code = "anvizent.package.label.default"/></th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
							</table>
						</div>
						
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary btn-sm" id="createTargetTable"><spring:message code = "anvizent.package.button.createTable"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="mappingTableCreationPopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title"><spring:message code="anvizent.package.label.tableCreation"/></h4>
		      </div>
		      <div class="modal-body">
		        	<div class="table-responsive" >
						<table class="table table-striped table-bordered tablebg" id="mappingTableMultiple">
							<thead>
								<tr>
									<th>
										<input type="checkbox" class="m-check-all">
									</th>
									<th><spring:message code="anvizent.package.label.columnName"/></th>
									<th><spring:message code="anvizent.package.label.dataType"/></th>
									<th style="width: 9%;"><spring:message code="anvizent.package.label.length"/></th>
									<th class="smalltd"><spring:message code="anvizent.package.label.pk"/></th>
									<th class="smalltd"><spring:message code="anvizent.package.label.nn"/></th>
									<th class="smalltd"><spring:message code="anvizent.package.label.uq"/></th>
									<th class="smalltd"><spring:message code="anvizent.package.label.ai"/></th>
									<th class="smalltd"><spring:message code="anvizent.package.label.default"/></th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary btn-sm" id="mappingTableCreation"><spring:message code="anvizent.package.button.createTable"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div>
		  </div>
		</div>
			<div id="selectFileHeadersDiv" style="display:none;">
                   <div class="panel panel-info">
					<div class="panel-heading"><b><spring:message code="anvizent.package.label.mapping"/></b></div>
						<div class="panel-body fileList">
							<!--here files list will be added dynamically -->
							<div class='row form-group'>
								<div class="table-responsive" style="overflow-y:overlay;">
									<table class="table table-striped table-bordered tablebg" id="selectFileHeadersTable" style="width: 50%;">
										<thead>
											<tr>
												<th><spring:message code="anvizent.package.label.sNo"/></th>
												<th><spring:message code="anvizent.package.label.fileName"/></th>
												<th><spring:message code="anvizent.package.label.primary"/></th>
											</tr>
										</thead>
										 <tbody>
											
										</tbody>
									</table>
									<div class="col-sm-6">
										<button class="btn btn-md btn-primary pull-right" id="multipleMappingProceedButton"><spring:message code="anvizent.package.button.proceed"/></button>
									</div>
								</div>
							</div>
							
						</div>
				</div>
			</div>
				
				<div class="modal fade" tabindex="-1" role="dialog" id="viewTargetTableStructurePopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog" style="width:80%"> 
					    <div class="modal-content">
					      <div class="modal-header">
					      
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewTargetTableHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
					        	<div class="table-responsive" >
									<table class="table table-striped table-bordered tablebg" id="targetTableStructure">
										<thead>
											<tr>
												<th><spring:message code="anvizent.package.label.sNo"/></th>
												<th><spring:message code="anvizent.package.label.columnName"/></th>
												<th style="width:15%"><spring:message code="anvizent.package.label.dataType"/></th>
												<th><spring:message code="anvizent.package.label.columnSize"/></th>
												<th><spring:message code="anvizent.package.label.pk"/></th>
									            <th><spring:message code="anvizent.package.label.nn"/></th>
									            <th><spring:message code="anvizent.package.label.ai"/></th>
									            <th style="width:15%"><spring:message code="anvizent.package.label.default"/></th>
											</tr>
										</thead>
										<tbody>
											
										</tbody>
									</table>
								</div>
					      </div>
					      <div class="modal-footer">
					      	<button type="button" class="btn btn-primary" id="editTargetTableStructure"><spring:message code="anvizent.package.button.edit"/></button>
					      	 <button type="button" class="btn btn-primary" id="saveTargetTableStructure" style="display:none"><spring:message code="anvizent.package.button.save"/></button>
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div><!-- /.modal-content -->
					  </div><!-- /.modal-dialog -->
				</div>		
				
				
				<div class="modal fade" tabindex="-1" role="dialog" id="viewTargetTableQueryPopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog">
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewTargetTableQueryHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
										 <div class="form-group">
                                         <label for="comment"><spring:message code="anvizent.package.label.query"/>:</label>
                                         <textarea class="form-control" rows="5" id="targetTableQuery" disabled="disabled"></textarea>
                                         </div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div><!-- /.modal-content -->
					  </div><!-- /.modal-dialog -->
				</div>	
				<div class="modal fade" tabindex="-1" role="dialog" id="viewDerivedTableQueryPopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog">
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewDerivedTableQueryHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
										 <div class="form-group">
                                         <label for="comment"><spring:message code="anvizent.package.label.query"/>:</label>
                                         <textarea class="form-control" rows="5" id="derivedTableQuery" disabled="disabled"></textarea>
                                         </div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div><!-- /.modal-content -->
					  </div><!-- /.modal-dialog -->
				</div>	
				
				
				
				<div class="modal fade" tabindex="-1" role="dialog" id="targetTableDirectMappingPopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog modal-lg">
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close"  id='targetTablePopUpClose' ><span aria-hidden="true">&times;</span></button>
					       	<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="targetTableDirectPopUpHeader"><spring:message code="anvizent.package.label.addSource"/></h4>
					      </div>
					      <div class="modal-body">
									<div class="alert alert-danger message" style="display:none;">
												 <p class="messageText"></p>
									</div>
									<div class="alert alert-success successMessage" style="display:none;">
												 <p class="successMessageText"></p>
									</div>
									<div class='row form-group' id="mappingTypeSelection">
										<div class='col-md-4'>
													<label class="radio-inline"><input type="radio" name="typeSelection" id = 'flatFiles'><spring:message code="anvizent.package.label.flatFile"/></label>
										</div>	
										<div class='col-md-4'>
											<label class="radio-inline"><input type="radio" name="typeSelection" id='database' ${principal.isSandBox == false ? '' : 'disabled'}><spring:message code="anvizent.package.label.database"/></label>
										</div>	
									</div>
									<div id="flatFilesLocationDetails" style="display:none;">
						                   <div class="panel panel-info">
											<div class="panel-heading"><spring:message code="anvizent.package.label.flatFileDetails"/></div>
												<div class="panel-body">
												<form:form method="POST"  id="fileUploadForm_direct" enctype="multipart/form-data">
													<div class='row form-group '>
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.fileType"/>
														</div>
														<div class='col-sm-8'>
															<select class="form-control" id="flatFileType_direct" name = "flatFileType">
															<option value="csv" ${principal.isSandBox == false ? '' : 'disabled'}>csv</option>
															<option value="xls">xls</option>
															<option value="xlsx">xlsx</option>
															</select>
														</div>
													</div>
													<div class='row form-group delimeter-block'>
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.delimiter"/>:
														</div>
														<div class='col-sm-8' >
															<input type="text" class="form-control" id="delimeter_direct" name="delimeter" value="," readonly="readonly">
														</div>
													</div>
													<div class='row form-group'>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.dataSource"/>:
													</div>
													<div class='col-sm-8'>
														<select  class="form-control" id="flatDataSourceName">
														<option value='0'><spring:message code="anvizent.package.label.selectDataSource"/></option>
															<c:forEach items="${allDataSourceList}" var="dataSource">
																<option value="<c:out value="${dataSource.dataSourceName}"/>"><c:out value="${dataSource.dataSourceName}"/></option>
															</c:forEach>
															<option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></option>
														</select>
													</div>
												 </div>
												
												<div class='row form-group flatDataSourceOther' style="display:none">
														<div class='col-sm-4'>
														</div>
														<div class='col-sm-8'>
															<input type="text" id="flatDataSourceOtherName" class="form-control" data-minlength="1" data-maxlength="45">
														</div>
												</div>
													<div class='row form-group' style="display: none;">
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.firstRowHasColumnNames"/>:
														</div>
														<div class='col-sm-8'>
																<div class='col-sm-3'>
																	<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="true" checked="checked"><spring:message code="anvizent.package.label.yes"/></label>
																</div>
																<div class='col-sm-3' id="firstrowcolsvalidation">
																	<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="false"><spring:message code="anvizent.package.label.no"/></label>
																</div>
														</div>
													</div>
													<input type="hidden"  id="packageIdForFileUpload_direct" name="packageId"/>
													<input type="hidden"  id="userIdForFileUpload_direct" name="userId"/>
													<div class='row form-group '>
														
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.filePath"/>:
														</div>
														<div class='col-sm-8' >
															<input  type="file" name="file" id="file_direct"> 
														</div>
														<div class='col-sm-4'>
														</div>
														<div class='col-sm-8' >
														 <p class="help-block disclaimerNote"><em><spring:message code = "anvizent.package.label.notePleaseMakeSureFileIsHavingHeaders"/></em></p>
													     <p class="help-block"><em><spring:message code = "anvizent.package.label.noteDateTimeFormat"/> < yyyy-MM-dd HH:mm:ss ></em></p>
														</div>	
													</div>
													</form:form>
													<div class="row">
														<div class="col-sm-10 pull-right" id="flatfilemessage" style="display: none;"></div>
													</div>
												</div>
										</div>
									</div>
									<div id='databaseConnectionDetails' style="display:none;">
										<div class='row form-group '>
												<div class='col-sm-3'>
													<spring:message code="anvizent.package.label.existingConnections"/>
												</div>
												<div class='col-sm-3'>
													<select class="form-control" id="existingConnections">
													</select>
												</div>
												<div class="col-sm-6">
													    <button type="button" class="btn btn-primary btn-sm" id="createNewConnection_dataBaseType" name='createNewConnection_dataBaseType'><spring:message code="anvizent.package.button.createNewConnection"/></button>
										  		        <button type="button" class="btn btn-primary btn-sm" id="deleteDatabaseTypeConnection" style="display:none"   name='deleteDatabaseTypeConnection'><spring:message code="anvizent.package.button.deleteConnection"/></button>
										  		</div>
										  			 
											</div>
										<div class="panel panel-info" id="databaseConnectionPanel" style="display:none;">
											<div class="panel-heading"><spring:message code="anvizent.package.label.databaseConnectionDetails"/></div>
											<div class="panel-body">
												<div class='row form-group '>
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.connectionName"/>:
														</div>
														<div class='col-sm-8'>
															<input type="text" id="IL_database_connectionName" class="form-control" data-minlength="1" data-maxlength="64">
														</div>
													</div>
													<div class='row form-group'>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.dataSource"/>:
													</div>
													<div class='col-sm-8'>
														<select  class="form-control dataSourceName dataSource_name" id="dataSourceName">
														<option value='0'><spring:message code="anvizent.package.label.selectDataSource"/></option>
															<c:forEach items="${allDataSourceList}" var="dataSource">
																<option value="<c:out value="${dataSource.dataSourceName}"/>"><c:out value="${dataSource.dataSourceName}"/></option>
															</c:forEach>
															<option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></option>
														</select>
													</div>
											      </div>
											      <div class='row form-group dataSourceOther' style="display:none">
														<div class='col-sm-4'>
														</div>
														<div class='col-sm-8'>
															<input type="text" id="dataSourceOtherName" class="form-control" data-minlength="1" data-maxlength="45">
														</div>
												</div>
												<div class='row form-group '>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.connectorType"/>:
													</div>
													<div class='col-sm-8'>
														<select  class="form-control" id="IL_database_databaseType">
															<c:forEach items="${databseList}" var="database">
																<option value="<c:out value="${database.id}"/>" data-protocal="<c:out value="${database.protocal}"/>" data-connection_string_params="<c:out value="${database.connectionStringParams}"/>" data-urlformat="<c:out value="${database.urlFormat}"/>" data-connectorId = "<c:out value="${database.connector_id}"/>"><c:out value="${database.name}"/></option>
															</c:forEach>
														</select>
													</div>
												</div>
												<div class='row form-group '>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.connectionType"/>:
													</div>
													<div class='col-sm-8'>
														<select  class="form-control" id="IL_database_connectionType">
															<option value="Direct"><spring:message code = "anvizent.package.label.direct"/></option>
														</select>
													</div>
												</div>
												
												<div class='row form-group servername-div'>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.serverName"/>:
													</div>
													<div class='col-sm-8'>
														<input type="text"  class="form-control" id="IL_database_serverName"  data-minlength="1" data-maxlength="150">
														<p class="help-block"><span class='serverIpWithPort'></span></p>
													</div>
												</div>
												<div class='row form-group placeholders-div'>
										<div class='col-sm-12 database-placeholders'>
											<table class="table tablebg "
												id="requestParamsTable">
												<thead>
													<tr class="hidden">
														<th class="col-xs-4"><spring:message
																code="anvizent.package.label.paramName" /></th>
														<th><spring:message code="anvizent.package.label.values" /></th>
													</tr>
												</thead>
												<tbody>
													
												</tbody>
												<tfoot class="hidden">
													<tr>
														<td class="col-sm-4"><span class="placeHolderLabelName"></span>
															<span class="mandatorySpan hidden"
															style="color: red; font-size: 15;">*</span></td>
														<td class="col-sm-8"><input type="hidden"
															class="placeHolderKey"><input type="text"
															class="form-control placeHolderValue"></td>
													</tr>
												</tfoot>
											</table>
										</div>
									</div>

									<div class='row form-group query-parameters-div' style="display:none">
										<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.dbParams"/>:
										</div>
										<div class='col-sm-8'>
											<input type="text"  class="form-control" id="database_queryParam" value="" placeholder = "[< ?  or  ; >propertyName1][=propertyValue1][< & or ; >propertyName2][=propertyValue2]"  data-minlength="1" data-maxlength="1000">
										</div>
									</div>
									 <div class='row form-group' id="sslEnableDiv" style="display:none">
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.sslenable"/>:
											</div>
											<div class='col-sm-8'>
												<input type="checkbox" class="sslEnable" id="sslEnable">
											</div>
										</div>
											<div class="mysqlSslCertificateFileNamesDiv" id ="mysqlSslCertificateFileNamesDiv" style="display:none">
										<div class='row form-group'>
											<div class='col-sm-4'>
												 <spring:message code="anvizent.package.label.existingsslfiles"/>:
											</div>
											<div class='col-sm-8'>
												<span id="mysqlSslCertificateFileNames"> </span>
											</div>
										</div>
										</div>
										<div class="mysqlSslCertificateFilesDiv" id ="mysqlSslCertificateFilesDiv" style="display:none">
										<div class='row form-group'>
											<div class='col-sm-4'>
												 <spring:message code="anvizent.package.label.sslclientkeyfile"/>: 
											</div>
											<div class='col-sm-8'>
												<input type="file"  class="form-control sslClientKeyFile" id="sslClientKeyFile" >
											</div>
										</div>
											<div class='row form-group'>
											<div class='col-sm-4'>
											<spring:message code="anvizent.package.label.sslclientcerfile"/>:
											</div>
											<div class='col-sm-8'>
												<input type="file"  class="form-control sslClientCertFile"  id="sslClientCertFile">
											</div>
										</div>
											<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.sslservercafile"/>:
											</div>
											<div class='col-sm-8'>
												<input type="file"  class="form-control sslServerCaFile" id="sslServerCaFile">
											</div>
										</div>
										</div>  
												<div class='row form-group '>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.userName"/>:
													</div>
													<div class='col-sm-8'>
														<input type="text"  class="form-control" id="IL_database_username"  data-minlength="1" data-maxlength="45">
													</div>
												</div>
												<div class='row form-group ' id="IL_database_password_div">
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.password"/>:
													</div>
													<div class='col-sm-8' >
														<input type="password"  class="form-control" id="IL_database_password"  data-minlength="1" data-maxlength="100">
													</div>
												</div>
												<div class='row form-group'>
										 			<div class='col-sm-4'>
																<spring:message code="anvizent.package.label.dateFormat" />( <spring:message code="anvizent.package.label.optional"/>  ) :
													 </div>
													<div class="col-sm-8">
														<input type="text" class="form-control" id="dateFormat" placeholder="Ex: YYYY-MM-DD"  data-minlength="1" data-maxlength="45">
												 	</div>
											 	 </div>
											 	 
											 	 <div class='row form-group'>
															<div class='col-sm-4'>
																<spring:message code="anvizent.package.label.timeZone" /> :
															</div>
															<div class='col-sm-8'>
																<select  class="form-control timesZone" id="timesZone" >
																	<c:forEach items="${timesZoneList}" var="timesZoneList">
																		<option value="<c:out value="${timesZoneList.key}"/>"><c:out value="${timesZoneList.value}"/></option>
																	</c:forEach>
																</select>
															</div>
											     </div>
											     <div class='row form-group hidden db-variables-add-div'>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.dataBasevariables" /> : 
														<button class='btn btn-primary btn-sm' id='addDBVarDiv'> 
																					<span class='glyphicon glyphicon-plus'></span>
														</button>
													</div>
													
													<div class='col-sm-8'>
														<table class="table tablebg" id="dbVariablesTbl">
																<tbody>
																	
																</tbody>
																<tfoot>
																	<tr class="clonedDbVariable hidden">
																		<td class="dbVarPair">
																			<div class='col-sm-6'>
																				<input type='text' class='dbVarKey form-control' placeholder="{key}">
																			</div>
																			<div class='col-sm-6'>
																				<input type='text' class='dbVarValue form-control' placeholder="Value">
																			</div>
																		</td>
																		<td>
																			<button class='btn btn-primary btn-sm addDbVariablePairDiv'> 
																				<span class='glyphicon glyphicon-plus'></span>
																			</button>
																		</td>
																		<td>
																			<button class='btn btn-primary btn-sm deleteDbVariablePairDiv'>
																				<span class='glyphicon glyphicon-trash'></span>
																			</button>
																		</td>
																	</tr>
																	<tr class="displayDbVariable hidden">
																		<th>
																			<span class="dbVarKey txt-break"></span>
																		</th>
																		<td>
																			<span class="dbVarValue txt-break"></span>
																		</td>
																	</tr>
																</tfoot>
														</table>
													</div>
												</div>
												<div class='row form-group '>
													<div class='col-sm-3'>
														<input type="button" value='<spring:message code="anvizent.package.button.testConnection"/>' class="btn btn-primary btn-sm" id="testConnection"/>
													</div>
													<div class='col-sm-3'>
														<input type="button" id="saveNewConnection_dataBaseType" value='<spring:message code="anvizent.package.button.saveConnection"/>' class="btn btn-primary btn-sm"/>
													</div>
												</div>
												<div class='row form-group IL_queryCommand'>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.typeOfCommand"/>:
													</div>
													
													<div class='col-sm-8'>
														<select class="form-control" id="typeOfCommand">
															<option value="Query"><spring:message code="anvizent.package.label.query"/></option>
															<option value="Stored Procedure"><spring:message code="anvizent.package.label.storedProcedure"/></option>
														</select>
													</div> 
												</div>
												
												<div class='row form-group IL_queryCommand'>
													<div class='col-sm-12'>
														<textarea class="form-control" rows="6" id="queryScript" placeholder="<spring:message code="anvizent.package.label.query"/>"></textarea>
													</div>
												</div>
												<div class='row form-group IL_queryCommand' style="margin-top: -15px">
													<div class='col-sm-12'>
														<input class="form-control" id="procedureName" style="display:none;" placeholder="<spring:message code="anvizent.package.label.storedProcedureName"/>">
													</div>
												</div>
												<div class='col-sm-12 queryValidatemessageDiv'> </div>
												<div class="row">
														<div class="col-sm-10 pull-right" id="databasemessage" style="display: none;"></div>
											    </div>
												<div class='row form-group'>
													<div class='col-sm-12'>
														<input type="button" value='<spring:message code="anvizent.package.button.validateQuerySP"/>' id='checkQuerySyntax' class="btn btn-primary btn-sm"/>
														<input type="button" value='<spring:message code="anvizent.package.button.preview"/>' id='checkTablePreview' class="btn btn-primary btn-sm" data-target='#tablePreviewPopUp'/>														
														<c:if test="${principal.isTrailUser == false}">
													    	<a class="btn btn-primary btn-sm buildQuery"  href="" ><spring:message code="anvizent.package.button.buildQuery"/>
													    		<span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
													    	</a>
													    </c:if>
													    <c:if test="${principal.isTrailUser == true}">
													    	 <button type="button"  class="btn btn-primary btn-sm" id="" disabled="disabled">
													    	 	<spring:message code="anvizent.package.button.buildQuery"/> 
													    	 	<img src="<c:url value="/resources/images/lock.png"/>" class="img-responsive lock-symbol" alt="Responsive image">
													    	 </button>
													    </c:if>
													</div>
													
													
												</div>
											</div>
						                  </div>
									</div>
					      </div>
					      <div class="modal-footer">
					        <input type="button" value='<spring:message code="anvizent.package.button.save"/>' id='saveILConnectionMapping' class="btn btn-primary" style="display: none;"/>
					        <input type="button"  value='<spring:message code="anvizent.package.button.saveUpload"/>' id='saveAndUpload_direct' class="btn btn-primary btn-sm" style="display: none;"/>
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div><!-- /.modal-content -->
					  </div><!-- /.modal-dialog -->
				</div>
				
				<!-- Table Preview PopUp window for custom package-->
					<div class="modal fade" tabindex="-1" role="dialog" style='overflow-y:auto;max-height:90%; margin-top:50px;'id="tablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
				      <div class="modal-dialog" style="width: 90%;">
				     <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" id="closePreview" class="close"><span aria-hidden="true">&times;</span></button>
				        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
	 										<h4 class="modal-title custom-modal-title" id="tablePreviewPopUpHeader"></h4>
				      </div>
				      <div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto;   overflow-x: auto;">
				      <table class='tablePreview table table-striped table-bordered tablebg'></table>
				      </div>
				      <div class="modal-footer">
				        <button type="button" class="btn btn-default closeTablePreview" ><spring:message code="anvizent.package.button.close"/></button>
				      </div>
				    </div> 
				     </div> 
                     </div>
				
				<div class="modal fade" tabindex="-1" role="dialog" id="show_ILConnectionFlatFileInfo" data-backdrop="static" data-keyboard="false">
				  <div class="modal-dialog">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <h4 class="modal-title" id="viewSchemaHeader"></h4>
				      </div>
				      <div class="modal-body">
				        	<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.fileType"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_fileType" disabled="disabled">
													<input type="hidden" class="form-control" id="show_mappingId" disabled="disabled">
													
												</div>
							</div>
							<div class='row form-group'>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.delimiter"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_delimiter" disabled="disabled">
												</div>
							</div>
							<div class='row form-group' style="display: none;">
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.firstRowHasColumnNames"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_firstRowHasColumnNames" disabled="disabled">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.file"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_filePath" disabled="disabled">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													&nbsp;
												</div>
												<div class='col-sm-12' >
													<input type="button" value='Preview' id='checkFlatPreviewViewDetails' class="btn btn-primary btn-sm" data-target='#viewDeatilsFlatPreviewPopUp'/>
													
													
												</div>
							</div>
				      </div>
				      <div class="modal-footer" style="display:none">
				        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				      </div>
				    </div><!-- /.modal-content -->
				  </div><!-- /.modal-dialog -->
				</div>
				
				<div class="modal fade" tabindex="-1" role="dialog" style='overflow-y:auto;max-height:90%;' id="viewDeatilsFlatPreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
												      <div class="modal-dialog" style="width: 90%;">
												     <div class="modal-content">
												      <div class="modal-header">
												        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
												        <h4 class="modal-title" id="viewDeatilsFlatPreviewPopUpHeader"></h4>
												      </div>
												      <div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto;   overflow-x: auto;">
												      <table class='viewDeatilsFlatPreview table table-striped table-bordered tablebg'></table>
												      </div>
												      <div class="modal-footer">
												        <button type="button" data-dismiss="modal"  class="btn btn-default viewDetailscloseFlatPreview" ><spring:message code="anvizent.package.button.close"/></button>
												      </div>
												    </div> 
												     </div> 
						                           </div>
	
				<div class="modal fade" tabindex="-1" role="dialog" id="show_ILConnectionDataBaseInfo" data-backdrop="static" data-keyboard="false">
				  <div class="modal-dialog">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <h4 class="modal-title"></h4>
				      </div>
				      <div class="modal-body">
				        	<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.connectionName"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_connectionName" disabled="disabled">
													<input type="hidden" class="form-control" id="show_connectionId">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.connectorType"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_databaseType" disabled="disabled">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.connectionType"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_connectionType" disabled="disabled">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.serverName"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_serverName" disabled="disabled">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.userName"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_userName" disabled="disabled">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.typeOfCommand"/>:
												</div>
												<div class='col-sm-8' >
													<input type="text" class="form-control" id="show_typeOfCommand" disabled="disabled">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													
												</div>
												<div class='col-sm-12' >
													<textarea class="form-control" rows="6" id="show_queryScript" disabled="disabled"></textarea>
													<input type="text" class="form-control" id="show_procedureName" disabled="disabled" style="display:none;">
												</div>
							</div>
							<div class='row form-group '>
												<div class='col-sm-4'>
													&nbsp;
												</div>
												<div class='col-sm-12' >
													<input type="button" value='Preview' id='checkTablePreviewViewDetails' class="btn btn-primary btn-sm" data-target='#viewDeatilsTablePreviewPopUp'/>
													
												</div>
							</div>
							
							
						</div>
				        	
				      <div class="modal-footer">
				        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				      </div>
				    </div><!-- /.modal-content -->
				  </div><!-- /.modal-dialog -->
				  
				</div>	
		
	</div>
	<div class="modal fade" tabindex="-1" role="dialog" style='overflow-y:auto;max-height:90%; 'id="viewDeatilsTablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
			      <div class="modal-dialog" style="width: 90%;">
			     <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close closeViewDeatilsTablePreviewPopUp"  aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <h4 class="modal-title" id="viewDeatilsTablePreviewPopUpHeader"></h4>
			      </div>
			      <div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto;   overflow-x: auto;">
			      <table class='viewDeatilsTablePreview table table-striped table-bordered tablebg'></table>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default viewDetailscloseTablePreview" ><spring:message code="anvizent.package.button.close"/></button>
			      </div>
			    </div> 
			     </div> 
    </div>
	   <div class="modal fade" tabindex="-1" role="dialog" id="deleSourceFileAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.modalHeader.deleteSource"/></h4>
		      </div>
		      <div class="modal-body">
		        <p><spring:message code="anvizent.package.message.deleteSource.areYouSureYouWantToDeleteSource"/><!-- &hellip; --></p>
		        <div id="deleSourceFileAlertMessage" class="alert alert-danger" style="display:none;"></div>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-primary" id="confirmDeleteSource"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
		<!-- delete IlConnection and IlConnectionMapping -->
		  <div class="modal fade" tabindex="-1" role="dialog" id="deleteIlConnection" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" id="closeDeleteConnection" ><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.button.deleteConnection"/></h4>
		      </div>
		      <div class="modal-body">
		        <p><spring:message code="anvizent.package.message.deletePackage.alltheMappingsWithTheseConnectionWillBeDeleted"/><br>
		        <spring:message code="anvizent.package.message.deletePackage.areYouSureYouWantToDeleteIlConnection"/><!-- &hellip; -->
		        </p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary" id="confirmDeleteIlConnection"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" id="closeDeleteConnectionMapping" ><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="deleteTargetTableAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.modalHeader.deleteTargetTable"/></h4>
		      </div>
		      <div class="modal-body">
		        
		        <p>${deleteMessage } <!-- &hellip; --></p>
		        
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-primary" id="confirmDeleteTargetTable"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="deleteDerivedTableAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.modalHeader.deleteDerivedTable"/></h4>
		      </div>
		      <div class="modal-body">
		        <p><spring:message code="anvizent.package.message.deleteSource.areYouSureYouWantToDeleteDerivedTable"/><!-- &hellip; --></p>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-primary" id="confirmDeleteDerivedTable"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
	<div class="modal fade" tabindex="-1" role="dialog" id="messagePopUpForTableDelete" data-backdrop="static" data-keyboard="false">
	  <div class="modal-dialog" style="width: 500px;">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	         <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		     <h4 class="modal-title custom-modal-title"></h4>
	      </div>
	      <div class="modal-body">
	      	<div class="row form-group">
	      		<div class="col-sm-12">
		        	<div id="popUpMessageForTableDelete" class ="alert" style="text-align: center;"></div>
	      		</div>
	      	</div>
	      	<div class="row">
	      		<div class="col-sm-12" style="text-align: center;">
		        	<button type="button" class="btn btn-primary btn-sm" id="closeMessageWindow" ><spring:message code="anvizent.package.button.ok"/></button>
	      		</div>
	      	</div>
	      </div>
	    </div>
	  </div>
	</div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="addSourceAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title"><spring:message code="anvizent.package.label.addSource"/></h4>
		      </div>
		      <div class="modal-body">
		        <p class="alert alert-danger"><b><spring:message code="anvizent.package.label.note"/></b> <br /><spring:message code="anvizent.package.label.TargetTableIsAlreadyCreatedForThisPackageIfyouAddSourceAllMappingsWillBeDestroyed"/>
		        <br />
		        <spring:message code="anvizent.package.label.areYouSureYouWantToAddSource"/> <!-- &hellip; --></p>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-primary" id="showAddSourcePopup"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
	</div>
		
	<div class="modal fade" tabindex="-1" role="dialog" id="derivedTableAddSourceAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header" id="derivedTableDirectPopUpHeader">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		      </div>
		      <div class="modal-body">
		        <p class="alert alert-danger"><b><spring:message code="anvizent.package.label.note"/></b> <br /><spring:message code="anvizent.package.label.TargetTableIsAlreadyCreatedForThisPackageIfyouAddSourceAllMappingsWillBeDestroyed"/>
		        <br />
		        <spring:message code="anvizent.package.label.areYouSureYouWantToAddSource"/> <!-- &hellip; --></p>
		      </div>
		      <div class="modal-footer">
		      	<a  href="<c:url value="/adt/package/deleteCustomTargetTable/${customPackageForm.packageId}"/>" ><button type="button" class="btn btn-primary" ><spring:message code="anvizent.package.button.yes"/></button></a>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
	</div>
	
	
	<div class="modal fade" tabindex="-1" role="dialog" id="columnSizeMisMatchAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header" id="columnSizeMisMatchAlertPopUpHeader">Change Columns
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		      </div>
		      <div class="modal-body">
		       <spring:message code="anvizent.package.message.text.areYouSureYouWantToProceedToChange"/> <!-- &hellip; --><br/>
		        <p class="alert alert-danger"><b><spring:message code="anvizent.package.label.note"/></b> <br />Observed some columns changed, that will be effected on existing table data.
		        <br /></p>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-primary" id="proceedEditedTargetTbl"><spring:message code="anvizent.package.button.proceed"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
	</div>
		
</div>