<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="col-sm-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText">
			<spring:message code="anvizent.navLeftTabLink.label.hierarchical" />
		</h4>
	</div>
	<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	<jsp:include page="_error.jsp"></jsp:include>
	<input type="hidden" id="viewJobResultsUrl" value="<c:url value="/adt/hierarchical/hierarchicalJobResults" />">
	<div class="row">
		<div class="col-xs-12">
			
			<div id="hierarchicalListDiv">
				<div class='row form-group'>
					<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success" id="addHierarchicalBtn"><spring:message code="anvizent.package.label.create"/></a>
				</div>
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg"
						id="hierarchicalListTbl">
						<thead>
							<tr>
								<th><spring:message code="anvizent.package.label.Id" /></th>
								<th><spring:message code="anvizent.package.label.name" /></th>
								<th>Description</th>
								<th>Status</th>
								<th><spring:message code="anvizent.package.label.Edit" /></th>
								<th><spring:message code="anvizent.package.label.mapHierarchy" /></th>
								<%-- <th><spring:message code="anvizent.package.label.run" /></th> --%>
								<th><spring:message code="anvizent.package.label.ViewResults" /></th>
								<th><spring:message code="anvizent.package.label.delete" /></th>
							</tr>
						</thead>
						<tbody>
							
						</tbody>
						<tfoot>
							<tr class="hidden">
								<td class="id"></td>
								<td class="name"></td>
								<td class="description"></td>
								<td class="status"></td>
								<td>
								  <button class="btn btn-primary btn-sm tablebuttons hierarchicalEdit" value="" title="<spring:message code="anvizent.package.label.edit"/>" >
									<i class="fa fa-pencil" aria-hidden="true"></i>
								  </button>
								</td>
								<td>
                                  <button type="button" class="btn btn-primary mapHierarhy"><spring:message code="anvizent.package.label.mapHierarchy" /></button>
								</td>
								<%-- <td>
								  <button type="button" class="btn btn-primary runHierarchy"><spring:message code="anvizent.package.label.run" /></button>
								</td> --%>
								<td>
								<a target="hierarchicalresults" id="viewHierarchicalResults" href="" class="btn btn-primary btn-sm" ><spring:message code="anvizent.package.label.viewResults" /></a>
								</td>
								<td>
								  <button class="btn btn-primary btn-sm tablebuttons hierarchicalDelete" value="" title="<spring:message code="anvizent.package.label.delete" />" >
									<i class="fa fa-trash" aria-hidden="true"></i>
								  </button>
								</td>
							</tr>
						</tfoot>
					</table>
				</div>
				
			</div>
			<div id="addDiv"  class="hidden">
				<div class="row form-group">
					<div class="col-xs-3">
						<input type="hidden" id="id">
						<input type="text" id="name" class="form-control" placeholder="Name" title="Name">
					</div>
					<div class="col-xs-3">
						<input type="text" id="description" class="form-control" placeholder="Description" title="Description">
					</div>
					<div class="col-xs-3">
						<label class="radio-inline"> <input type="checkbox" 
							name="hierarchyType" id='hierarchyType' disabled ="disabled"
							class="hierarchyType">
							<spring:message code="anvizent.package.label.financialTemplate" />
						</label>
					</div>
					<div class="col-xs-1 nopad">
						<button type="button" class="btn btn-primary" id="saveHierarchical" style="margin:4px auto;">Save</button>
					</div>
					<div class="col-xs-1 nopad">
						<button type="button" class="btn btn-primary" id="backToHierarchicalList"  style="margin:4px auto;">Back</button>
					</div>
					
				</div>
				<div id="printMsg"></div>
				<div id="addHierarchy" class="context-menu-hierarchical-div">
				  
				</div>	

				<div class="row parent-element parentCloneDiv hidden">
					<div class="col-xs-12 parent-block">
							<i class="tab-alignment"></i>
							<i>&nbsp;&nbsp;&nbsp;&nbsp;</i> 
							<i class="symbol-span fa fa-folder-open-o"></i>
							<i class="space-span">&nbsp;&nbsp;&nbsp;&nbsp;</i> 
							<span class="element-name-span"></span>
					</div>
					<div class="col-xs-12 child-block hidden" style="cursor: pointer;">
						<a> </a>
					</div>
				</div>

				<div class="modal fade" role="dialog" id="createNewHierarchyPopUp"
					data-backdrop="static" data-keyboard="false" style="overflow-y: auto; max-height: 90%;">
					<div class="modal-dialog"  style="width:95%;">
						<div class="modal-content" >
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<img src="<c:url value="/resources/images/anvizent_icon.png" />"
									class="anvizentLogo" />
								<h4 class="modal-title custom-modal-title hierarchytitle">
									<spring:message
										code="anvizent.package.button.createNewHierarchy" />
								</h4>
							</div>
							<div class="modal-body">
								<div class="panel panel-info">
									<div class="panel-body">
										<div class="row form-group">
											<div class="col-sm-3">
											</div>
											<div class="col-sm-8">
												<input type="checkbox" id="autoDropDownCheckBox"> <label for="autoDropDownCheckBox">From Source</label>
											</div>
										</div>
										<div class="row form-group">
											<div class="col-sm-3">
												<input type="hidden" id="hierarchyId" />
												<spring:message code="anvizent.package.label.selectilordl"/>
											 </div>	
												<div id="selectSourceDetails" class="col-sm-5">
													<select id="ilId" class="form-control">
														<option value="0">Select IL</option>
													</select>
												</div>
										  </div>
										<div class="row form-group">
										   <div class="col-sm-3">
											 <spring:message code="anvizent.package.label.selectdimension"/>
										   </div>
										   <div class="col-sm-5">
											   <div>
											  	 <select id=ilColumnName name="ilColumnName"
														class="form-control">
														<option value="0"><spring:message
																code="anvizent.package.label.columnName" /></option>
												  </select>
											   </div>
										   </div>
										   	
										</div>
										 <div class="row form-group valueOptionsDiv" style="display: none;">
										 	<div class="col-sm-3">
											</div>
										  <div class='col-sm-2'>
										     <label class="radio-inline">
										       <input type="radio" name="valueOptionSelection" id='pattern'  class = "valueOptionSelection" value = "<spring:message code="anvizent.package.label.pattern" />">
										         <spring:message code="anvizent.package.label.pattern" />
										     </label>
										  </div>
										   <div class='col-sm-2'>
										     <label class="radio-inline"><input type="radio" name="valueOptionSelection" id='autoManual' class = "valueOptionSelection"
										            value = "<spring:message code="anvizent.package.label.autoManual" />">
										         <spring:message code="anvizent.package.label.autoManual" /></label>
										   </div>
										 </div>
										 
										<div class="row form-group columnValuesDiv">
											<div class="col-sm-3">
	 											<spring:message code="anvizent.package.label.dimensionvalues"/>
											</div>
											
											<div id="inputtextDiv" class="col-sm-5">
												<input type="text" id="ilColumnValue" class="form-control">
											</div>
											<div id="patternTextDiv" class="col-sm-7" style = "display:none">
											  <div class="col-sm-5">
											    <input type="text" id="ilPattrenValue" class="form-control">
											  </div> 
											  <div class="col-sm-4">
											     <select id="patternOptionsSelect" class="form-control">
												   <option value="starts_with" selected>Starts With</option>
												   <option value="contains" >Contains</option>
												   <option value="ends_with">Ends With</option>
												 </select>
											  </div>
											  <div class="col-sm-1">
											     <button class="btn btn-primary btn-sm fetchPatternData">
											 		<spring:message code="anvizent.package.label.fetch" />
												</button>
											  </div>	
											</div>
											<div id="ilTransactionTypeDiv" class="col-sm-2" style="display:none">
											 	<select id="ilTransactionTypeSelect" class="form-control">
												   <option value="+">+</option>
												   <option value="-">-</option>
												</select>
											</div>
											
											<div id="select2Div" style="display: none" class="col-sm-5">
												<select name="ilColumnValueSelect"
													id="ilColumnValueSelect" class="form-control"
													multiple="multiple">
												</select>
											</div>
											
											<div id="columnRangeDiv" style="display: none;" class="col-sm-6">
															<%-- <input id="fromRange" type="text" class="form-control rangeValues" placeholder="<spring:message code="anvizent.package.label.from"/>"> --%>
															<select id="fromRange" class="form-control rangeValues" >
																
															</select>
															<br />
															<%-- <input id="toRange" type="text" class="form-control rangeValues" placeholder="<spring:message code="anvizent.package.label.to"/>"> --%>
															<select id="toRange" class="form-control rangeValues" >
																
															</select>
														
											</div>
											
											<div class="col-sm-2 selectRangeDiv" style="display: none;">
											   <label><input type="checkbox" id="selectRange" > Range </label>
											   <br />
												<button class="btn btn-primary btn-sm fetchColumnData">
											 		<spring:message code="anvizent.package.label.fetch" />
												</button>
											</div>
										</div>
										
										<div class="row form-group exclusionDiv" style = "display:none">
										   <div class="col-sm-3">
											 <spring:message code="anvizent.package.label.selectExclusions"/>
										   </div>
										   <div class="col-sm-5">
											   <div>
											  	 <select name="ilColumnValueExclusionSelect"
													id="ilColumnValueExclusionSelect" class="form-control"
													multiple="multiple">
												</select>
											   </div>
										   </div>
										</div>
										
										<div class="row form-group customValueChkbxDiv" style = "display:none">
											<div class="col-sm-3">
											</div>
											<div class="col-sm-8">
												<input type="checkbox" id="customValueChkbx"> <label for="customValueChkbx">Custom Value</label>
											</div>
										</div>										
										
										
										<div class="row form-group customValueDiv" style = "display:none"> 
										   <div class="row form-group">
										    <div class="col-sm-3">
										      Name
										    </div>
										    <div class="col-sm-5">
										       <input type="text" id="customValue" class="form-control">
										    </div>
										   </div>  
										   <div class="row form-group"> 
										     <div class="col-sm-3">
										       Expression
										     </div>
										     <div class="col-sm-5">
										       <textarea rows="4" cols="50" id="customValueFormula" class="form-control"></textarea>
										     </div>
										    </div> 
										</div>										
										
										<div class="row form-group col-sm-2 addCloumnValueDiv" style = "float:right">
											<button class="btn btn-primary btn-sm addCloumnValue">
												 <spring:message code="anvizent.package.label.addtolevel" />
											</button>
										</div>
										 
										<div class='row form-group newElementsTableDiv'>
											<div class="col-sm-12">
												<div class="table-responsive" style="overflow-y: overlay;">
													<table id="newElementsTable"
														class="table table-striped table-bordered">
														<thead>
															<tr>
																<th>Table Name</th>
																<th>Column Name</th>
																<th>Value</th>
																<th class = "col-sm-2" style="display:none">TransactionType</th>
																<th>Delete</th>
															</tr>
														</thead>
														<tbody>

														</tbody>
													</table>
												</div>
											</div>
										</div>
										
										<div class = 'row form-group exclusionDataDiv' style="display:none">
										   <div class="col-sm-3">
											 Exclusions 
										   </div>
										   <div  class="col-sm-5">
												<span class = 'exclusionSpan'></span>
											</div>
										</div>
										
										<div class="modal-footer">
											<button type="button" class="btn btn-success btn-sm"
												id="createHierarchyBtn">
												<spring:message code="anvizent.package.label.create" />
											</button>
											<button type="button" class="btn btn-warning btn-sm"
												data-dismiss="modal">
												<spring:message code="anvizent.package.button.cancel" />
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
	
	<div class="modal fade" role="dialog" id="createHierarchyNamePopUp"
					data-backdrop="static" data-keyboard="false">
					<div class="modal-dialog">
						<div class="modal-content" style="width: 115%;">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<img src="<c:url value="/resources/images/anvizent_icon.png" />"
									class="anvizentLogo" />
								<h4 class="modal-title custom-modal-title hierarchy">
									<spring:message
										code="anvizent.package.button.addHierarchy" />
								</h4>
							</div>
							<div class="modal-body">
								<div class="panel panel-info">
									<div class="panel-body">
										<div class="row form-group">
											 <div class="col-sm-4">
											    <spring:message code="anvizent.package.label.name"/>
											 </div>
											 <div class="col-sm-8">
												<input type="text" id="hierarchyName" class="form-control" placeholder="HierarchyName">
											 </div>
										</div>
										<div  class="row form-group">
										 	 <div class="col-sm-4">
											    <spring:message code="anvizent.package.label.description"/>
											 </div>
											 <div class="col-sm-8" >
												<input type="text" id="hierarchyDesc" class="form-control" placeholder="Description">
											 </div>
										</div>
										<div  class="row form-group">
										 	 <div class="col-sm-4">
											    
											 </div>
											 <div class="col-sm-8" >
												<label class="radio-inline"><input type="checkbox"
													name="hierarchyTypeName" id='hierarchyTypeId'
													class="hierarchyTypeCls">
													<spring:message
														code="anvizent.package.label.financialTemplate" /></label>
											</div>
										</div>
										<div class="modal-footer">
											<button type="button" class="btn btn-success btn-sm"
												id="createHierarchyNameBtn">
												<spring:message code="anvizent.package.label.create" />
											</button>
											<button type="button" class="btn btn-warning btn-sm"
												data-dismiss="modal">
												<spring:message code="anvizent.package.button.cancel" />
											</button>
										</div>

									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="modal fade" role="dialog" id="mapHierarchyPopup"
					data-backdrop="static" data-keyboard="false">
					<div class="modal-dialog">
						<div class="modal-content" style="width: 115%;">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<img src="<c:url value="/resources/images/anvizent_icon.png" />"
									class="anvizentLogo" />
								<h4 class="modal-title custom-modal-title mapHierarchyHeader">
									
								</h4>
							</div>
							<div class="modal-body">
								<div class="panel panel-info">
									<div class="panel-body">
										<div class="row form-group">
											   <div class="col-sm-4">
													<input type="hidden" id="maphierarchyId" />
													<input type="hidden" id="maphierarchyName" />
													<input type="hidden" id="hierarchyMapId" />
													<spring:message code="anvizent.package.label.selectilordl"/>
												</div>
												<div id="selectSourceDetails" class="col-sm-8">
													<select id="mapIlId" class="form-control">
														<option value="0">Select IL</option>
													</select>
												</div>
										</div>
										<div class="row form-group">
										  <div class="col-sm-4">
										    <spring:message code="anvizent.package.label.columns"/>
										  </div>
											<div class="col-sm-8">
												<select id=ilMeasures name="ilMeasures"
													class="form-control" multiple="multiple">
												</select>
											</div>
										</div>
										<!-- <div class="row form-group">
											<div id="mapselect2Div" class="col-xs-8">
												<select name="ilDimensions"
													id="ilDimensions" class="form-control"
													multiple="multiple">

												</select>
											</div>
										</div> -->
										
										<div class="row form-group">
										  <div class="col-sm-4">
										    <spring:message code="anvizent.package.label.report.source.table"/>
										  </div>
										  <div id="associationDiv" class="col-sm-8">
										    <input type="text" id="associationName"  class="form-control">
										  </div>
										</div>
										<%-- <div id = "dataRangeDiv" class="row form-group">
										  <div class="col-sm-5">
										    <spring:message code="anvizent.package.label.associationname"/>
										  </div>
										  <div class='col-sm-2'>
										     <label class="radio-inline"><input type="radio" name="typeSelection" id='all'>
										         <spring:message code="anvizent.package.label.all" /></label>
										  </div>
										   <div class='col-sm-2'>
										     <label class="radio-inline"><input type="radio" name="typeSelection" id='range'>
										         <spring:message code="anvizent.package.label.columndaterange" /></label>
										   </div>
										</div> --%>
										<div class="modal-footer">
											<button type="button" class="btn btn-success btn-sm"
												id="mapHierarchyBtn">
												<spring:message code="anvizent.package.label.Map" />
											</button>
											<button type="button" class="btn btn-warning btn-sm"
												data-dismiss="modal">
												<spring:message code="anvizent.package.button.cancel" />
											</button>
										</div>

									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="modal fade" role="dialog" id="hierarchyLevelPopup"
					data-backdrop="static" data-keyboard="false">
					<div class="modal-dialog">
						<div class="modal-content" style="width: 115%;">
							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<img src="<c:url value="/resources/images/anvizent_icon.png" />"
									class="anvizentLogo" />
								<h4 class="modal-title custom-modal-title hierarchylevelheader">
									
								</h4>
							</div>
							<div class="modal-body">
								<div class="panel panel-info">
									<div class="panel-body">
									   <div id="hierarchylevelDiv">
									   
									   </div>
									   
									   <div class="modal-footer">
											<button type="button" class="btn btn-success btn-sm"
												id="hierarchyLevelBtn">
												<spring:message code="anvizent.package.button.save" />
											</button>
											<button type="button" class="btn btn-warning btn-sm"
												data-dismiss="modal">
												<spring:message code="anvizent.package.button.cancel" />
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="row form-group hierarchylevelcloneDiv hidden">
				  <div class="col-sm-4">
                     <span class = "levelNameSpan"></span>
				  </div>
				  <div class="col-sm-8">
				    <input type="text" id="levelaliasname"  class="form-control levelaliasname">
				  </div>
				</div>
				
</div>
