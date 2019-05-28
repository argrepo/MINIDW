<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<style>
.row{	
 /*  margin-bottom: 30px */
}
</style>

<div class="col-sm-12 rightdiv">
	<div class='row form-group'>
		<h4 class="alignText">
			<spring:message code="anvizent.package.label.xReference" />
		</h4>
	</div>
	
	<jsp:include page="_error.jsp"></jsp:include>
	
	<input type="hidden" id="viewResultsUrl" value="<c:url value="/adt/crossReference/jobResults/" />">
	<input type="hidden" id="viewResultsByIdUrl" value="<c:url value="/adt/crossReference/viewCrossRefExecutionResultsById/" />">
	<input type="hidden" id="xrefId" />
	
	<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	<div class='row form-group filterByDiv'>
	<label class="control-label col-sm-4 col-md-3 col-lg-2 labelsgroup"><spring:message
							code="anvizent.package.label.FilterBy" /></label>
					<div class="col-sm-4 col-md-3 col-lg-2">
						<select id="filterPackages" class="form-control">
							<option value="all"><spring:message
									code="anvizent.package.label.all" /></option>
							<option value="active" selected><spring:message
									code="anvizent.package.label.active" /></option>
							<option value="deleted"><spring:message
									code="anvizent.package.label.deleted" /></option>
						</select>
					</div>
	</div>	
	<div class="crossrefInfoDiv">
        <div  style="width:100%;padding:0px 15px;">
         <div class="row form-group" style="padding:10px;border-radius:4px;">
		  <a style="float:right;" class="btn btn-sm btn-success addCrossReference" href="#"> <spring:message code="anvizent.package.button.add"/> </a>
		 </div>
	    </div>
		<div class='row form-group'>
			 <div class="table-responsive">
				<table class="table table-striped table-bordered tablebg" id="crossrefTable" >
							<thead>
								<tr>
								    <th><spring:message code="anvizent.package.label.id"/></th>
								    <th><spring:message code="anvizent.package.label.dimension"/></th>
								    <th><spring:message code="anvizent.package.label.type"/></th>
								    <th><spring:message code="anvizent.package.label.type.of.merge"/></th>
									<th><spring:message code="anvizent.package.label.conditionName"/></th>
									<th><spring:message code="anvizent.package.label.run"/></th>
									<th><spring:message code="anvizent.package.link.viewResults"/></th>
									<th><spring:message code="anvizent.package.label.delete"/></th>
									<th><spring:message code="anvizent.package.label.edit"/></th>
								</tr>
							</thead>
							<tbody>
						    </tbody>
				</table>
			 </div>
		</div>
	</div>
	
	<div class="addCrossrefDiv hidden">
	   <div class="row form-group"  style="border-radius:4px;">
			<div class="col-xs-3">
				<label for="ilId" class="control-label"><spring:message code="anvizent.package.label.selectDimensiontable" /></label> 
			</div>	
			<div class="col-xs-3 selectSource">
				<input type="hidden" id="ilValue"/>
				<div id="selectSourceDetails" class="selectSourceDetails">
					<select id="ilId" class="form-control">
						<option value="0"><spring:message code="anvizent.package.label.select" /></option>
					</select>
				</div>
			</div>
				<div class="col-xs-1 view-audit-logs-div hidden">
				    <%-- <button class="btn btn-primary btn-sm" type="button" id="ViewInfo"><spring:message code="anvizent.package.label.viewDetails"/></button> --%>
				    <a target="crossreferenceresults" id="viewResultsBtn" href="" class="btn btn-primary btn-sm" ><spring:message code="anvizent.package.label.viewResults" /></a>
				</div>
				<div class="col-xs-1">
					<a type="reset" href="<c:url value="/adt/crossReference"/>" class="btn btn-primary btn-sm back_btn"><spring:message code="anvizent.package.link.back" /></a>
				</div>
				
		</div>
		
		<div class="crossReferenceTypeDiv hidden">
			<div class="row form-group crossRefTypeOptionDiv">
				<div class="col-xs-3"> 
					<label><spring:message code="anvizent.package.label.xReferenceType" /></label>
				</div>
				<div class="col-xs-5">
					<label for="crossReferenceOption1">
						<input type="radio" name="crossReferenceOption" id="crossReferenceOption1" value="merge"><spring:message code="anvizent.package.label.merge" /> 
					</label>
					<label for="crossReferenceOption2"  style="padding-left: 16px;">
						<input type="radio" name="crossReferenceOption" id="crossReferenceOption2" value="split"><spring:message code="anvizent.package.label.split" /> 
					</label>
					<label for="crossReferenceOption3"  style="padding-left: 16px;" id="xReferenceOption3">
						<input type="radio" name="crossReferenceOption" id="crossReferenceOption3" value="autosplit"><spring:message code="anvizent.package.label.autoSplit" /> 
					</label>
				</div>
			</div>
			
			<div class="first-level-div splitterDiv hidden">
			
					<div class="row form-group">
						<div class="col-xs-3" >
						<label for="splitIlColumnName"> <spring:message code="anvizent.package.label.columnName" /></label>
						</div>
						<div class="col-xs-5">
							<select id=splitIlColumnName name="splitIlColumnName"
								class="form-control">
								<option value="0"><spring:message code="anvizent.package.label.select" /></option>
							</select>

						</div>
					</div>
					
					<div class="row form-group"> 
						<div class="col-xs-3"><label><spring:message code="anvizent.package.label.values" /></label></div>
						<div class="col-xs-5">
						<select name="splitIlColumnValue" id="splitIlColumnValue" class="form-control" multiple="multiple">
							
						</select>
						</div> 
					</div>
					
						<div class="row form-group">
							<div class="col-xs-3"></div>
							<div class="col-xs-5">
								<button class="btn btn-primary btn-sm" type="button" id="btnFindMatchedXrefRecords"><spring:message code="anvizent.package.label.getRecords" /></button>
							</div>
						</div>
					
						
						<div class="split-input-screen-div hidden">
							<c:url value="/adt/crossReference/splitRecords" var="splitUrl" />
							<input type="hidden" id="splitUrl" value="${splitUrl}">
							<div class="row" style="padding: 10px;overflow-y: auto;">
								<table class='tablePreview table table-striped table-bordered tablebg' id="crossRefSpitTable">
									<thead>
									
									</thead>
									<tbody>
										
									</tbody>
								</table>
							</div>
							<div class="row" style="padding: 10px;overflow-y: auto;">
								<div>
									<button class="btn btn-primary btn-sm" type="button" id="splitSelectedRecords"><spring:message code="anvizent.package.label.splitSelectedRecords" /></button>
								</div>
							</div>
						</div>
				</div>
			
			<div class="first-level-div mergerDiv hidden">
				<div class="row form-group">
					<div class="col-xs-3">
						<label for="conditionName"><spring:message code="anvizent.package.label.crossRefName" /></label>
					</div>	
					<div class="col-xs-5">
					    <span id="conditionNameSpan"></span>
						<input type="text" name="conditionName" id="conditionName" class="form-control" value="" placeholder="Enter Condition Name for this merge"> 
					</div>
				</div>
				<div class='row form-group hidden'>
					<div class='col-sm-3'>
						<label for="applicableDate"><spring:message code="anvizent.package.label.applicableDate"/></label> 
					</div>
					<div class='col-sm-5'>
						<input type="text" class="form-control datepicker" id="applicableDate" placeholder="Ex: 2022-04-03"/>
					</div>
				</div>
				<div class="row form-group">
					<div class="col-xs-3">
						<label><spring:message code="anvizent.package.label.type.of.merge" /></label>
					</div>	
					<div class="col-xs-5 mergeType">
					<div class="typeOfMergeDiv">
						<label for="typeOfMerge2">
							<input type="radio" name="typeOfMerge" id="typeOfMerge2" value="manualmerge"><spring:message code="anvizent.package.label.manual"/> <spring:message code="anvizent.package.label.merge" />
						</label>
						<label for="typeOfMerge3" style="padding-left: 16px;">
							<input type="radio" name="typeOfMerge" id="typeOfMerge3" value="bulkmerge"><spring:message code="anvizent.package.label.bulkmerge"/>
						</label> 
						<label for="typeOfMerge1" style="padding-left: 16px;">
							<input type="radio" name="typeOfMerge" id="typeOfMerge1" value="automerge"><spring:message code="anvizent.package.label.automerge" />
						</label>
					</div>
					</div>
				</div>
				<div class="second-level-div bulkMergeDiv hidden">
					<form name="bulkMergeForm" id="bulkMergeForm" method="POST" enctype="multipart/form-data">
						<div class='row form-group hidden'>
							<div class='col-sm-3'>
								<label for="flatFileType"><spring:message code="anvizent.package.label.dataSource" /></label> 
							</div>
							<div class='col-sm-5'>
								<input type="text" class="form-control" >
							</div>
						</div>
						<div class='row form-group'>
							<div class='col-sm-3'>
								<label for="referenceField"><spring:message code="anvizent.package.label.referenceFields"/></label> 
							</div>
							<div class='col-sm-5'>
								<select class="form-control" id="referenceField" name="referenceField" multiple="multiple">
									
								</select>
							</div>
						</div>
						<div class='row form-group'>
							<div class='col-sm-3'>
								<label for="xReferenceField"><spring:message code="anvizent.package.label.crossReferenceFields"/></label> 
							</div>
							<div class='col-sm-5'>
								<select class="form-control" id="xReferenceField" name="xReferenceField" multiple="multiple">
									
								</select>
							</div>
						</div>
						
					<div class="autosplitBulkMergeDiv">
						
						<div class='row form-group'>
							<div class='col-sm-3'>
								<label for="flatFileType"><spring:message code="anvizent.package.label.fileType" /></label> 
							</div>
							<div class='col-sm-5'>
								<select class="form-control" id="flatFileType" name="flatFileType">
									<option value="csv">csv</option>
									<option value="xls">xls</option>
									<option value="xlsx">xlsx</option>
								</select>
							</div>
						</div>
						<div class='row form-group delimeter-block hidden'>
							<div class='col-sm-3'>
							<label for="delimeter"><spring:message code="anvizent.package.label.delimiter" />
								:</label> 
								
							</div>
							<div class='col-sm-5'>
								<input type="text" class="form-control" id="delimeter" value="," name="delimeter" readonly="readonly">
							</div>
						</div>
						<div class='row form-group hidden'>
							<div class='col-sm-3'>
							<label for="delimeter"><spring:message code="anvizent.package.label.firstRowHasColumnNames" /></label> 
								
							</div>
							<div class='col-sm-5'>
								<div class='col-sm-2'>
									<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="true" checked="checked"> <spring:message code="anvizent.package.label.yes" /></label>
								</div>
								<div class='col-sm-2' id="firstrowcolsvalidation">
									<label class="radio-inline"><input type="radio" name="isFirstRowHasColumnNames" value="false"> <spring:message code="anvizent.package.label.no" /></label>
								</div>
							</div>
						</div>
						<div class='row form-group '>
		
							<div class='col-sm-3'>
								<spring:message code="anvizent.package.label.file" />
		
							</div> 
							<div class='col-sm-5' >
								<input type="file" name="file" id="fileUpload" style="float:left">
								<button class="btn btn-primary btn-sm bulk-merger-buttons-group" type="button" id="uploadBulkMergeFileBtn"><spring:message code="anvizent.package.label.uploadFile" /></button>
							</div>
						</div>
						<div class='row form-group'>
						<div class='col-sm-3 '>&nbsp;</div>
							<div class='col-sm-5'>
								<p class="help-block disclaimerNote">
									<em><spring:message code="anvizent.package.label.notePleaseMakeSureFileIsHavingHeaders" /></em>
								</p>
							</div>
						</div>
						<div class='row form-group file-headers-mapping-div hidden'>
							<div class="col-sm-12">
								<div class="row hidden">
									<h3  class='col-sm-4'><spring:message code="anvizent.package.label.fileHeadersMapping" /></h3>
									<h3 class='col-sm-2'><button type="button" class="btn btn-primary btn-sm" id="refreshData"><span class="glyphicon glyphicon-repeat"></span></button></h3>
								</div>
								<div class="row">
									<div class='col-sm-8'>
										<div class="row">
											<div class='col-sm-1'>
											&nbsp;
											</div>
											<div class='col-sm-5'>
												<strong><spring:message code="anvizent.package.label.fileHeaders"/></strong>
											</div>
											<div class='col-sm-6'>
												<strong><spring:message code="anvizent.package.label.selectedILHeaders"/></strong>
											</div>
										</div>
									</div>
									<div class='col-sm-8 file-headers-div'>
										
									</div>
								</div>
							</div>
							
						</div>
						<div class='row form-group bulkMergeBtnDiv'>
							<div class='col-sm-6 text-right'>
									<button class="btn btn-primary btn-sm bulk-merger-buttons-group" type="button" id="initiateBulkMergeBtn"><spring:message code="anvizent.package.label.merge" /></button>
									<button class="btn btn-primary btn-sm bulk-merger-buttons-group" type="button" id="cancelBulkMergeInitiation"><spring:message code="anvizent.package.button.edit" /></button>
							</div>
							<%-- <div class='col-sm-6'>
									<a target="crossreferenceresults" id="viewResultsBtn" href="" class="btn btn-primary btn-sm" ><spring:message code="anvizent.package.label.viewResults" /></a>
							</div> --%>
						</div>
					  </div>
					</form>
				</div>
				<div class="second-level-div autoMergeDiv hidden">
					<div class="row form-group">
						<div class="col-xs-3" >
						<label for="ilColumnName"> <spring:message code="anvizent.package.label.columnName" />(s)</label>
						</div>
						<div class="col-xs-5">
							<select name="autoMergeColumns" id="autoMergeColumns" multiple="multiple" class="form-control">
							</select>
						</div>
					</div>
					<div class="row form-group">
						<div class="col-xs-3" >
						
						</div>
						<div class="col-xs-5">
							<button class="btn btn-primary btn-sm" type="button" id="autoMergeConditionBtn"><spring:message code="anvizent.package.label.addConditions" /></button>
						</div>
					</div>
					<div class="autoMergeConditionTblDiv hidden" id = "autoMergeConditionTblDiv">
				       <div class="row form-group">
				         <div class="row" style="padding: 10px;overflow-y: auto;">
							<table class='table table-striped table-bordered tablebg' id="crossRefAutoMergeTable">
								<thead>
								  <tr>
								  	<th class="col-xs-1"><spring:message code="anvizent.package.label.id"/></th>
								    <th class="col-xs-2"><spring:message code="anvizent.package.button.ColumnName"/></th>
								    <th class="col-xs-2">Pattern Type</th>
								    <th class="col-xs-3"><spring:message code="anvizent.package.label.patternormatchChars"/></th>
								  </tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						  </div> 
				       </div>
				    </div>
				    
				    <div class="col-xs-5 autoMergeBtnDiv hidden">
						<button class="btn btn-primary btn-sm" type="button" id="autoMergeBtn"><spring:message code="anvizent.package.label.Save" /></button>
					</div>
				</div>
				
				<div class="second-level-div manualMergeDiv hidden">
					<div class="row form-group">
						<div class="col-xs-3" >
						<label for="manualMergeIlColumnName"> <spring:message code="anvizent.package.label.columnName" /></label>
						</div>
						<div class="col-xs-5">
							<select id="manualMergeIlColumnName" name="manualMergeIlColumnName"
								class="form-control">
								<option value="0"><spring:message code="anvizent.package.label.select" /></option>
							</select>

						</div>
					</div>
					
					<div class="manualAutoSpltDiv">
					
					<div class="row form-group"> 
						<div class="col-xs-3"><label for="manualMergerIlColumnValue"><spring:message code="anvizent.package.label.values" /></label></div>
						<div class="col-xs-5">
						<select name="manualMergerIlColumnValue" id="ilColumnValueAjax" class="form-control .ilColumnName" multiple="multiple">
							
						</select>
						</div> 
					</div>
					
					<div class="row form-group">
						<div class="col-xs-3"></div>
						<div class="col-xs-1">
							<button class="btn btn-primary btn-sm" type="button" id="btnFindMatchedRecords"><spring:message code="anvizent.package.label.getRecords" /></button>
						</div>
					</div>
					
					<div class="hidden third-level-div hidden manual-merge-input-screen-div">
					
						<c:url value="/adt/crossReference/mergeRecords" var="mergeUrl" />
						<c:url value="/adt/crossReference/unMergeSelectedCrossReferenceRecord" var="unmergeUrl" />
						<input type="hidden" id="mergeUrl" value="<c:out value="${mergeUrl}"/>">
						<input type="hidden" id="unmergeUrl" value="<c:out value="${unmergeUrl}"/>">
					
						<div class="row form-group">
							<div class="col-xs-3"></div>
							<div class="col-xs-6">
								<label class="radio-inline control-label">
								<input type="radio" name="newOrExistingXref" id="newOrExistingXref" value="newXref" class="newXref">
							    	<spring:message code="anvizent.package.label.newXref" />
							    </label>
							    <label class="radio-inline control-label">
							    <input type="radio" name="newOrExistingXref" id="newOrExistingXref" value="existingXref" class="existingXref">
							    	<spring:message code="anvizent.package.label.existingXref" />
							    </label>
							</div>
						</div>
						
						<div class="row manual-merge-div existing-x-ref-div hidden form-group">
							<div class="col-xs-3"><label for="existingXrefValue"><spring:message code="anvizent.package.label.existingXref" /></label></div>
							<div class="col-xs-5">
							<select id="existingXrefValue" name="existingXrefValue"
								class="form-control">
								<option value="0"><spring:message code="anvizent.package.label.selectXref" /></option>
							</select>
							</div>
						</div>
						
							<div class="col-sm-12 row manual-merge-div new-x-ref-values-div hidden" style="overflow-y: auto;">
								<h3><spring:message code="anvizent.package.label.xRef"/></h3>
								
								<table class='new-x-ref-values-table table table-striped table-bordered tablebg '>
									<thead>
										<tr>
											
										</tr>
									</thead>
									<tbody>
										
									</tbody>
								</table>
							</div>
							
						
							<div class="col-sm-12 row manual-merge-div hidden existing-x-ref-values-div " style="overflow-y: auto;">
								<input type="hidden" name="existingXrefKey" id="existingXrefKey"/>
								<h3><spring:message code="anvizent.package.label.xRef"/></h3>
								<table class='xref-table table table-striped table-bordered tablebg' id="existingXrefTbl">
									<thead>
										
									</thead>
									<tbody>
										
									</tbody>
								</table>
								<div id="selectExistingXrefMsg"></div>
							</div>
						
						<div class="col-sm-12 row" style="overflow-y: auto;">
							<h3><spring:message code="anvizent.package.label.source"/></h3>
							<table class='tablePreview table table-striped table-bordered tablebg ' id="crossRefMergeTable">
								<thead>
									
								</thead>
								<tbody>
									
								</tbody>
							</table>
							<div id="selectColumnsToMergeMsg"></div>
						</div>
							
						<div class="row" style="padding: 10px;overflow-y: auto;">
							<div>
								<button class="btn btn-primary btn-sm" type="button" id="mergeSelectedRecords"><spring:message code="anvizent.package.label.mergeSelectedRecords"/></button>
							</div>
						</div>
					</div>
				  </div>
				</div>
				
			</div>
		</div>
	</div>
		
	<div class="modal fade" tabindex="-1" role="dialog" id="viewDetails" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog" style="width: 1250px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close close-popup" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title"> <spring:message code="anvizent.package.button.ViewDetails"/></h4>
					</div>
					<div class="modal-body">
						<div class="row form-group">
					         <div class="table-responsive" >
								<table class="table table-striped table-bordered tablebg " id="viewCrossReferenceTable">
									<thead>
										<tr>
											<th><spring:message code="anvizent.package.button.Id"/></th>
											<th><spring:message code="anvizent.package.label.type"/></th>
											<th><spring:message code="anvizent.package.label.type.of.merge" /></th>
											<th> <spring:message code="anvizent.package.label.conditionName" /></th>
											<!-- <th>Applicable Date</th> -->
											<th><spring:message code="anvizent.package.label.stats"/></th>
											<th> <spring:message code="anvizent.package.label.startDate"/></th>
											<th><spring:message code="anvizent.package.label.endDate"/></th>
										</tr>
									</thead>
									<tbody>							
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="modal-footer">
		        		<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      		</div>
				</div>
			</div>
		</div>
	
	
	<div class="modal fade" tabindex="-1" role="dialog" id="viewColumnValuesPopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width:75%;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title heading"><spring:message code="anvizent.package.button.XrefColumnValues"/></h4>
		      </div>
		      <div class="modal-body">
					<div class="row form-group">
					         <div class="table-responsive" >
								<table class="table table-striped table-bordered tablebg " id="viewColumnsTable">
								</table>
							</div>
						</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="unMergePopUpAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 1250px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title"><spring:message code="anvizent.package.label.unMerge"/></h4>
		      </div>
		      <div class="modal-body">
		       <div class="row form-group">
					         <div class="table-responsive" >
								<table class="table table-striped table-bordered tablebg " id="viewUnMergeTableColumnsRowTable">
								</table>
							</div>
						</div>
		      </div>
		      <div class="modal-footer">
		        <span style="color:red;margin-right:100px"><spring:message code="anvizent.package.label.areYouSure"/></span>
		      	<button type="button" class="btn btn-primary" id="confirmUnMerge"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="viewXrefColumnsRowPopup" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog" style="width: 1250px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close close-popup" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title"> <spring:message code="  anvizent.package.button.UpdateRow"/></h4>
					</div>
					<div class="modal-body">
						<div class="row form-group">
					         <div class="table-responsive" >
								<table class="table table-striped table-bordered tablebg " id="viewCrossReferenceTableColumnsRow">
									<thead>
										<tr>
									    	<th> <spring:message code="anvizent.package.button.SNo"/></th>
											<th> <spring:message code=" anvizent.package.button.ColumnName"/></th>
											<th><spring:message code=" anvizent.package.button.ColumnValue"/></th>
										</tr>
									</thead>
									<tbody>							
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="modal-footer">
					   <button type="button" class="btn btn-default"><spring:message code="anvizent.package.button.update"/></button>
		        		<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      		</div>
				</div>
			</div>
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="archieveCrossReferencePopUpAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 600px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title"><spring:message code="anvizent.package.label.delete"/></h4>
		      </div>
		      <div class="modal-body">
		        <div class="row form-group">
		          <input type="hidden" id = "deleteConditionId" >
		          <spring:message code="anvizent.package.label.doUWantUnMergetheExistingCross-ref"/>
		          <label class="radio-inline"> <input type="checkbox" id = "unmergeExistingXRef"  name = "unmergeExistingXRef"> </label>
			    </div>
		      </div>
		      <div class="modal-footer">
		      	<%-- <button type="button" class="btn btn-primary" id="confirmUnMergeCrossRef"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button> --%>
		        
		         <button type="button" class="btn btn-primary" id="unMergeCrossRef"><spring:message code="anvizent.package.button.ok"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="activeCrossReferencePopUpAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 600px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title"><spring:message code="anvizent.package.label.activate"/></h4>
		      </div>
		      <div class="modal-body">
		       <div class="row form-group">
					         Are you sure want to activate ?
						</div>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-primary" id="activateCrossRef"><spring:message code="anvizent.package.button.yes"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		
		
		<div class="modal fade" tabindex="-1" role="dialog" id="editCrossReferencePopUpAlert" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 600px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title"><spring:message code="anvizent.admin.button.Edit"/></h4>
		      </div>
		      <div class="modal-body">
		        <div class="row form-group">
			         <spring:message code = "anvizent.package.label.doUWantdroptheexistingmergeCondition" />
			         <label class="radio-inline"> <input type="checkbox" id = "editCrossMergeCbx"  name = "editCrossMergeCbx"> </label>
				</div>
		      </div>
		      <div class="modal-footer">
		       <button type="button" class="btn btn-primary" id="editCrossMergeBtn"><spring:message code="anvizent.package.button.ok"/></button>
		      </div>
		    </div> 
		  </div> 
		</div>
		<div class="col-xs-5 autoSplitBtnDiv hidden" style="float:center;margin-right: 1.5em;">
						<button class="btn btn-primary btn-sm" type="button" id="autoSplitBtn"><spring:message code="anvizent.package.button.proceed" /></button>
		</div>
</div>
