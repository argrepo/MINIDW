<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<style>
.headerH1 {
	font-weight: bold;
}
</style>

<div class="col-sm-12 rightdiv">
        <div class="page-title-v1"><h4><spring:message code="anvizent.package.label.standardPackage"/></h4></div>
        <div class="dummydiv"></div>
		<ol class="breadcrumb">
		</ol>
<div class='col-sm-12'>
<form:form modelAttribute="standardPackageForm" method="POST" id="standardPackageForm">
		<jsp:include page="_error.jsp"></jsp:include>
		<form:hidden path="packageId"/>  
		<form:hidden path="isStandard"/>
		<form:hidden path="industryId"/>
		<input type="hidden" id="userID" value="${principal.userId}">
		<input type="hidden" id="iLId" value="">
		<form:hidden path="dLId"/> 
        <div class="panel panel-default">
			  <div class="panel-heading">
			    <h3 class="panel-title"> <spring:message code = "anvizent.package.label.xrefPackage"/></h3>
			  </div>
			  <div class="panel-body">
			  	<div class='row form-group'>
						<label class="col-sm-3 control-label labelsgroup">
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
									       <input type="submit" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.label.createPackage"/>" 
																id="createStandardPackage" name='createStandardPackage'/>
									    </c:when>
									</c:choose>
						  </div>
					 
			  </div>
		  		<div class="row form-group">
			  		
				  </div>
			  </div>
		</div>
		</form:form>
		<c:if test="${not empty standardPackageForm.packageId}">
			<div class='row form-group'>
				<c:if test="${empty ilsList}">
					<div class="alert alert-warning" role="alert"><span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>  <spring:message code="anvizent.package.message.dLsAreNotAssignedPleaseContactSuperAdmin"/></div>
				</c:if>
				<c:if test="${not empty ilsList}">
					<div id="viewIlSourceDetails">
					
					  <div class="panel-group" id="accordionMain" role="tablist" aria-multiselectable="true">
					  
					  	<table class="table table-striped table-bordered tablebg" id="tblIlsList">
							<thead>
								<tr>
									<th><spring:message code = "anvizent.package.label.sNo"/></th>
									<th><spring:message code = "anvizent.package.label.ilName"/></th>
									<th><spring:message code = "anvizent.package.label.sourceampMapping"/></th>
									<th><spring:message code = "anvizent.package.label.xReference"/></th>
									<th><spring:message code = "anvizent.package.label.splitting"/> </th>
									<c:if test="${not empty param.show }">
										<th><spring:message code = "anvizent.package.label.xRefData"/></th>
										<th><spring:message code = "anvizent.package.label.ilData"/></th>
									</c:if>
								</tr>
							</thead> 
						 <tbody>
						  	<c:forEach items="${ilsList}" var="ilsList" varStatus="increment">
						  		<tr>
						  			<td>${increment.index+1}</td>
						  			<td>${ilsList.iL_name }</td>
						  			
						  			<td><button type="button" style="margin-top:-6px;" data-ilid="${ilsList.iL_id}" data-iLName="${ilsList.iL_name }" data-iLTableName="${ilsList.iL_table_name }" class="btn btn-primary btn-sm  ilMappingingButton"><spring:message code = "anvizent.package.label.sourceampMapping"/></button></td>
						  			<td><button type="button" style="margin-top:-6px;" data-ilid="${ilsList.iL_id}" data-iLName="${ilsList.iL_name }" data-iLTableName="${ilsList.iL_table_name }" class="btn btn-primary btn-sm  ilXRefgButton"><spring:message code = "anvizent.package.label.xReference"/></button></td>
							      	<td><button type="button" style="margin-top:-6px;" data-ilid="${ilsList.iL_id}" data-iLName="${ilsList.iL_name }" data-iLTableName="${ilsList.iL_table_name }" class="btn btn-primary btn-sm  ilSplittingButton"}><spring:message code = "anvizent.package.label.splitting"/></button></td>
							      	<c:if test="${not empty param.show }">
								      	<td><button type="button" style="margin-top:-6px;" data-tableType="xref" data-ilid="${ilsList.iL_id}" class="btn btn-primary btn-sm  displayTabledata"><spring:message code = "anvizent.package.button.xrefTable"/></button></td>
								      	<td><button type="button" style="margin-top:-6px;" data-tableType="il" data-ilid="${ilsList.iL_id}" class="btn btn-primary btn-sm displayTabledata"}><spring:message code = "anvizent.package.button.ilTable"/></button></td>
							      	</c:if>		      
						  		</tr>	
						  	</c:forEach>
						  </tbody>
					  	
					  	</table>
						
				      </div>
				      
				      
				      
				      
				      
				      
				      				<div class="xReferenceSourceMappingDiv" id="accordion"  style="border: 1px solid #000;padding: 10px;">
										<div class="pull-right">
											<button type="button" class="btn btn-sm btn-danger" id="closexReferenceSourceMappingBtn"><spring:message code = "anvizent.package.button.x"/></button>
										</div>
										<div class='row form-group'>
											<div class='col-sm-4 col-lg-3'>
												<label class="control-label "><spring:message code = "anvizent.package.label.ilName"/></label>
												<input type="text" id="iLName" class="form-control" disabled="true">
											</div>
											<div class='col-sm-4 col-lg-3'>
												<label class="control-label">&nbsp;</label>
												<div>
													<input type="button" class="btn btn-primary btn-sm" value="<spring:message code = "anvizent.package.label.addSource"/>" id="addSource" name='addSource' />
												</div>
											</div>
				
										</div>
										<div>
											<table class="table table-striped table-bordered tablebg" id="customPackageTable">
												<thead>
													<tr>
														<th><spring:message code = "anvizent.package.label.sourceName"/></th>
														<th><spring:message code = "anvizent.package.label.fileName"/></th>
														<th><spring:message code = "anvizent.package.label.mapping"/></th>
														<th class="mappingColumn"><spring:message code = "anvizent.package.label.merging"/></th>
													</tr>
												</thead> 
											 	<tbody>
											 	</tbody>
											</table>
										</div>
										
									</div>
				      
				      
				      
				      <div class="xReferenceSplittingDiv" style="border: 1px solid #000;padding: 10px;">
				      <div class="pull-right"> <button type="button" class="btn btn-sm btn-danger" id="closexReferenceSplittingBtn" ><spring:message code = "anvizent.package.button.x"/></button></div>
				      
				      <div class="clearfix" style="height:20px;"></div>
				      
					      <div id="split-XrefDiv" class="row">
					      <div class="col-xs-12" >
					     <table class="table table-striped table-bordered tablebg" id="tblIlsXrefIds">
							 <tbody>
							  	<tr>
							  		<th class="col-xs-6">
							  			<select class="form-control xrefIdColumn selectIlColumnNameforMerging" data-targetselectbox=".xrefIdColumnValue" data-coltype="il">
							  			</select>
							  		</th>
							  		<th class="col-xs-6">
							  			<select class="form-control xrefIdColumnValue">
							  			</select>
							  		</th>
							  	</tr>
							  </tbody>
					  	</table>
					  	</div>
					  	<div class="clearfix" style="height:20px;"></div>
					  	<div class="col-xs-12" style="overflow: auto;">
					  	 	<table class="table table-striped table-bordered tablebg" id="tblIlsXrefData">
					  	 	<thead>
					  	 	</thead>
							 <tbody>
							  </tbody>
					  		</table>
					  	</div>
					  	<div class="clearfix" style="height:20px;"></div>
					  	<div class="text-center">
					  	<button class="btn btn-primary splitBtn"><spring:message code = "anvizent.package.button.splitSelectedRows"/>
					  	</button>
					  	</div>
					  	
					      </div>
					      
				      </div>
				      
				      
				      
				      <div class="xReferenceDetailsDiv" style="border: 1px solid #000;padding: 10px;" >
				      <div class="pull-right"> <button type="button" class="btn btn-sm btn-danger" id="closexReferenceDetailsBtn" ><spring:message code = "anvizent.package.button.x"/></button></div>
				      	<div>
					      	<button type="button" class="btn btn-primary btn-sm xrefButton mappingBtn hidden" data-divId="mappingDiv-Xref"><spring:message code = "anvizent.package.label.mapping"/></button>
					      	<button type="button" class="btn btn-primary btn-sm xrefButton mergingBtn hidden" data-divId="mergingDiv-Xref"><spring:message code = "anvizent.package.label.xReference"/></button>
				      	</div>
				      
				      <div class="clearfix" style="height:20px;"></div>
				      
					      <div id="mappingDiv-Xref">
					      	<div class="table-responsive" style="max-height: 400px;">
					      			<input type="hidden" id="ilMappingId">
					      			<input type="hidden" id="xRefId">
					      			<input type="hidden" id="selectedXrefOption">
									<table class="table table-striped table-bordered tablebg" id="fileMappingWithILTable">
										<thead>
											<tr>
												<th><spring:message code="anvizent.package.label.sNo"/></th>
												<th class="iLName"></th>
												<th class=""><spring:message code="anvizent.package.label.dataType"/></th>
												<th><spring:message code="anvizent.package.label.pk"/></th>
											    <th><spring:message code="anvizent.package.label.nn"/></th>
											    <th><spring:message code="anvizent.package.label.ai"/></th>
												<th class="originalFileName"></th>
												<th><spring:message code="anvizent.package.label.default"/></th>
											</tr>
										</thead>
										 <tbody>
											
										</tbody> 
									</table>
				
								</div> 
								<div>
						         	<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.saveMapping"/>" id="saveMappingWithIL" name='saveMappingWithIL'/>
						      	</div>
					      </div>
					      <div id="mergingDiv-Xref">
					      
					      	<div>
						      	<button type="button" class="btn btn-primary xRefMergeButtons btn-sm" data-divId="columnsDiv-Xref"><spring:message code="anvizent.package.label.xref.xrefbycondition"/></button>
						      	<button type="button" class="btn btn-primary xRefMergeButtons btn-sm" data-divId="valuesDiv-Xref"><spring:message code="anvizent.package.label.xref.xrefbyvalues"/></button>
				      		</div>
					      <div id="columnsDiv-Xref">
					      <div class="row hidden">
							<div class="col-sm-12">
								<div class="pull-right">
									<button class="btn btn-success addConditionGroupBtn" data-divId="columnsDiv-Xref"><spring:message code="anvizent.package.button.addGroup"/></button>
								</div>
							</div>
						  </div>
						  <div style="height:10px;"></div>
					      <div class="row groupBoxTemplate hidden">
						    <div class="col-xs-12">
							<div class="pull-right addColumnBox">
								<button class="btn btn-success addConditionBtn" ><spring:message code="anvizent.package.button.add"/></button>
								<button class="btn btn-danger removeGroupBtn hidden"><spring:message code="anvizent.package.button.removeGroup"/></button>
							</div>
								<table class="table table-striped table-bordered tablebg tblForMergingConditions">
									<thead>
										<tr>
											<th class="iLName"></th>
											<th class="originalFileName"></th>
											<th>
												<spring:message code="anvizent.package.label.delete"/>
											</th>
										</tr>
									</thead>
									<tbody>
										<tr class="hidden">
											<td>
												<select class="form-control selectIlColumnNameforMerging">
													
												</select>
											</td>
											<td>
												<select class="form-control selectSourceColumnNameforMerging">
													
												</select>
											</td>
											<td>
												<button type="button" class="btn btn-sm btn-danger removeConditionBtn hidden">
												<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
												</button>
											</td>
										</tr>
									</tbody>
									<tfoot style="background-color:#fff;">
										<tr>
											<td>
												<spring:message code="anvizent.package.label.conditionName"/>
											</td>
											<td>
												<input type="text" class="mergingConditionName form-control">
											</td>
											<td>
											</td>
										</tr>
									</tfoot>
								</table>
							</div>
						  </div>
						  </div>
					      
					      <div id="valuesDiv-Xref">
					      <div class="row hidden">
							<div class="col-sm-12">
								<div class="pull-right">
									<button class="btn btn-success addConditionGroupBtn" data-divId="valuesDiv-Xref"><spring:message code="anvizent.package.button.addGroup"/></button>
								</div>
							</div>
						  </div>
						  <div style="height:10px;"></div>
					      <div class="row groupBoxTemplate hidden">
						    <div class="col-xs-12">
							<div class="pull-right addColumnBox">
								<button class="btn btn-success addConditionBtn" ><spring:message code="anvizent.package.button.add"/></button>
								<button class="btn btn-danger removeGroupBtn hidden" data-divId="valuesDiv-Xref"><spring:message code="anvizent.package.button.removeGroup"/></button>
							</div>
								<table class="table table-striped table-bordered tablebg tblForMergingConditions">
									<thead>
										<tr>
											<th class="iLName"></th>
											<th><spring:message code="anvizent.package.label.columnValues"/></th>
											<th class="originalFileName"></th>
											<th><spring:message code="anvizent.package.label.columnValues"/></th>
											<th>
												<spring:message code="anvizent.package.label.delete"/>
											</th>
										</tr>
									</thead>
									<tbody>
										<tr class="hidden">
											<td>
												<select class="form-control selectIlColumnNameforMerging" data-colType="il" data-targetSelectBox=".selectedIlColumnValue">
													
												</select>
											</td>
											<td>
												<select class="form-control selectedIlColumnValue">
													
												</select>
											</td>
											<td>
												<select class="form-control selectSourceColumnNameforMerging" data-colType="source"  data-targetSelectBox=".selectedSourceColumnValue">
													
												</select>
											</td>
											<td>
												<select class="form-control selectedSourceColumnValue">
													
												</select>
											</td>
											<td>
												<button type="button" class="btn btn-sm btn-danger removeConditionBtn hidden">
												<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
												</button>
											</td>
										</tr>
									</tbody>
									<tfoot style="background-color:#fff;">
										<tr>
											<td>
												<spring:message code="anvizent.package.label.conditionName"/>
											</td>
											<td>
												<input type="text" class="mergingConditionName form-control">
											</td>
											<td colspan="3">
											</td>
										</tr>
									</tfoot>
								</table>
							</div>
						  </div>
						  </div>
					      
					      <div class="processMergingBlock">
					      	 <div>
						      	<input type="button" class="btn btn-primary btn-sm" value="Proceed" id="addMergingConstraint" name='addMergingConstraint'/>
						     </div>
					      </div>
					      	
					      </div>
				      </div>
      
      </div>
      
       <div class="clearfix" style="height:40px;"></div> 
      <div class="xReferenceUpdatesStatusDiv" style="border: 1px solid #000;padding: 10px;">
				      <div class="pull-right"> <button type="button" class="btn btn-sm btn-danger" id="closexReferenceUpdatesStatusBtn" ><spring:message code="anvizent.package.button.x"/></button></div>
				      
				      <div class="clearfix" style="height:40px;"></div>
				          
				          <div id="xrefStatusReport" class="boxContainer">
					          <h3 class="text-center" style="margin-top: 0px;margin-bottom: 0px;"><spring:message code="anvizent.package.label.mergingStatusof"/> <span class="mergingStatusSourceName" style="font-weight: bold;">-</span></h3>
					          <hr style="border:1px solid gray !important;" />
					      		<div class="row">
									<div class="col-lg-6  borderRow">
										<div class="headerH1 pull-left"><spring:message code="anvizent.package.label.conditionName"/></div>&nbsp;&nbsp;&nbsp;<span class="xrefConditionName">-</span>
									</div>
									<div class="col-lg-6 borderRow">
										<div class="headerH1 pull-left"><spring:message code="anvizent.package.label.conditions"/> </div>&nbsp;&nbsp;&nbsp;<span class="xrefConditionColumns"><spring:message code="anvizent.package.label.addressCompanyName"/></span>
									</div>
								</div>
								<div class="row">
									<div class="col-lg-3 borderRow">
										<div class="headerH1 pull-left"><spring:message code="anvizent.package.label.noOfXRefInsertedRecords"/> </div>&nbsp;&nbsp;&nbsp;<span class="countOfXrefInsertedRecords">75</span>
									</div>
									<div class="col-lg-3 borderRow">
										<div class="headerH1 pull-left"><spring:message code="anvizent.package.label.noOfXRefUpdatedRecords"/></div> &nbsp;&nbsp;&nbsp;<span class="countOfXrefUpdatedRecords">45</span>
									</div>
									<div class="col-lg-3 borderRow">
										<div class="headerH1 pull-left"><spring:message code="anvizent.package.label.noOfIlInsertedRecords"/> </div> &nbsp;&nbsp;&nbsp;<span class="countOfIlInsertedRecords">96</span> 
									</div>
									<div class="col-lg-3 borderRow">
										<div class="headerH1 pull-left"><spring:message code="anvizent.package.label.noOfIlFailedRecords"/> </div>&nbsp;&nbsp;&nbsp;<span class="countOfIlfailedRecords">77</span>
									</div>
								</div>  
				     	  </div>
				     	  <hr style="border:1px solid gray !important;" />
				           <div class="modal-body table-responsive" id="displayData">
					      		<table class='table table-striped table-bordered tablebg' id="tablePreviewForRecords"></table> 
					      </div>
					      
				      </div>
				      
				     
				</c:if>
				
				<div class="pull-left col-sm-12">		
					<a href="/adv/package/standardpackage" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.link.back"/></a>		
				</div>
				
			</div>
		</c:if>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="addSourcePopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog">
					    <div class="modal-content" style="width:700px">
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
										<div class='col-sm-2'>
											<label class="radio-inline"><input type="radio" name="typeSelection" id = 'flatFiles'><spring:message code="anvizent.package.label.flatFile"/></label>
										</div>
									 	<c:choose>
									 		<c:when test="${principal.isTrailUser == true}">
									 			<div class='col-sm-2'>
													<label class="radio-inline disable-text"><input type="radio" name="typeSelection" id='database' disabled="disabled"><spring:message code="anvizent.package.label.database"/>
													<img src="<c:url value="/resources/images/lock.png"/>" class="img-responsive lock-symbol" alt="Responsive image"></label>
												</div>
									 		</c:when>
									 		<c:otherwise>
									 			<div class='col-sm-2'>
													<label class="radio-inline"><input type="radio" name="typeSelection" id='database'><spring:message code="anvizent.package.label.database"/></label>
												</div>
									 		</c:otherwise>
									 	</c:choose>
									</div>
									<div id="flatFilesLocationDetails" style="display:none;">
						                   <div class="panel panel-info">
											<div class="panel-heading"><spring:message code="anvizent.package.label.flatFileDetails"/></div>
												<div class="panel-body">
												<form:form method="POST"  id="fileUploadForm_direct" enctype="multipart/form-data">
													<div class='row form-group '>
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.sourceName"/>:
														</div>
														<div class='col-sm-8'>
															<input type="text" class="form-control" id="flatFileSourceName" name="flatFileSourceName" value="">
														</div>
													</div>
													<div class='row form-group '>
														<div class='col-sm-4'>
															<spring:message code="anvizent.package.label.fileType"/>:
														</div>
														<div class='col-sm-8'>
															<select class="form-control" id="flatFileType_direct" name = "flatFileType">
																<option value="csv"><spring:message code="anvizent.package.label.csv"/></option>
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
															<input type="text" id="IL_database_connectionName" class="form-control"  data-minlength="1" data-maxlength="45">
														</div>
													</div>
												<div class='row form-group '>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.connectorType"/>:
													</div>
													<div class='col-sm-8'>
														<select  class="form-control" id="IL_database_databaseType">
															<c:forEach items="${databseList}" var="database">
																<option value="${database.connector_id}">${database.name}</option>
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
															<option value="Tunnel"><spring:message code = "anvizent.package.label.tunnel"/></option>
														</select>
													</div>
												</div>
												
												<div class='row form-group '>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.serverName"/>:
													</div>
													<div class='col-sm-8'>
														<input type="text"  class="form-control" id="IL_database_serverName"  data-minlength="1" data-maxlength="150">
														<p class="help-block"><em class='serverIpWithPort'></em></p>
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
												<div class='row form-group '>
													<div class='col-sm-3'>
														<input type="button" value='Test Connection' class="btn btn-primary btn-sm" id="testConnection"/>
													</div>
													<div class='col-sm-3'>
														<input type="button" id="saveNewConnection_dataBaseType" value='<spring:message code="anvizent.package.button.saveConnection"/>' class="btn btn-primary btn-sm"/>
													</div>
												</div>
												<div class='row form-group '>
														<div class='col-sm-4'>
															<spring:message code = "anvizent.package.label.sourceName"/>:
														</div>
														<div class='col-sm-8'>
															<input type="text" class="form-control" id="databaseSourceName" name="databaseSourceName" value="">
														</div>
													</div>
												<div class='row form-group IL_queryCommand'>
													<div class='col-sm-4'>
														<spring:message code="anvizent.package.label.typeOfCommand"/>:
													</div>
													
													<div class='col-sm-8'>
														<select class="form-control" id="typeOfCommand">
															<option value="Query"><spring:message code = "anvizent.package.label.query"/></option>
															<option value="Stored Procedure"><spring:message code = "anvizent.package.label.storedProcedure"/></option>
														</select>
													</div>
												</div>
												
												<div class='row form-group IL_queryCommand'>
													<div class='col-sm-12'>
														<textarea class="form-control" rows="6" id="queryScript" placeholder="Query"></textarea>
													</div>
												</div>
												<div class='row form-group IL_queryCommand' style="margin-top: -15px">
													<div class='col-sm-12'>
														<input class="form-control" id="procedureName" style="display:none;" placeholder="Stored Procedure Name">
													</div>
												</div>
												<div class='col-sm-12 queryValidatemessageDiv'>
														
													</div>
												<div class="row">
														<div class="col-sm-10 pull-right" id="databasemessage" style="display: none;"></div>
													</div>
												<div class='row form-group'>
													<div class='col-sm-6'>
														<input type="button" value='Validate Query/SP' id='checkQuerySyntax' class="btn btn-primary btn-sm"/>
														<input type="button" value='Preview' id='checkTablePreview' class="btn btn-primary btn-sm" data-target='#tablePreviewPopUp'/>
													</div>
													<!-- Table Preview PopUp window for custom package-->
													
												</div>
											</div>
						                  </div>
									</div>
					      </div>
					      <div class="modal-footer">
					        <input type="button" value='<spring:message code="anvizent.package.button.save"/>' id='saveILConnectionMapping' class="btn btn-primary" style="display: none;"/>
					        <input type="button"  value='Save & Upload' id='saveAndUpload_direct' class="btn btn-primary btn-sm" style="display: none;"/>
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div><!-- /.modal-content -->
					  </div><!-- /.modal-dialog -->
				</div>
				
                           
	<div class="modal fade" tabindex="-1" role="dialog" id="deleStandardSourceFileAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.modalHeader.deleteSource"/></h4>
		      </div>
		      <div class="modal-body">
		        <p><spring:message code="anvizent.package.message.deleteSource.areYouSureYouWantToDeleteSource"/><!-- &hellip; --></p>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-primary" id="confirmDeleteStandardSource"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" style='overflow-y:auto;max-height:90%; 'id="viewDeatilsPreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
			      <div class="modal-dialog" style="width: 90%;">
			     <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title" id="viewDeatilsPreviewPopUpHeader"></h4>
			      </div>
			      <div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto;   overflow-x: auto;">
			      <table class='viewDeatilsPreview table table-striped table-bordered tablebg'></table>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-default viewDetailsclosePreview" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
			      </div>
			    </div> 
			     </div> 
    </div>	 
    <!-- Table Preview PopUp window -->
	<div class="modal fade" tabindex="-1" role="dialog" id="tablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
      <div class="modal-dialog" style="width: 90%;">
     <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		<h4 class="modal-title custom-modal-title" id="tablePreviewPopUpHeader"></h4>
      </div>
      <div class="modal-body table-responsive" style="max-height: 400px; overflow-y: auto;   overflow-x: auto;">
      <table class='table table-striped table-bordered tablebg tablePreview'></table>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
      </div>
    </div> 
  </div> 
   </div>

		
   

</div>
</div>