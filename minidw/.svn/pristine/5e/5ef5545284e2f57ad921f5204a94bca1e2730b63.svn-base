<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv" >
    <div class="col-md-10">
    <div class="dummydiv"></div>
		<div class="col-sm-10">
		    <h4><spring:message code = "anvizent.package.label.ilSourceDetails"/></h4>
		</div>
		<jsp:include page="_error.jsp"></jsp:include>
		 
   </div>
   
  <div class="col-xs-12">

	<form:form modelAttribute="standardPackageForm" method="POST" id="standardPackageForm">
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
		<form:hidden path="industryId"/>
		<form:hidden path="packageId"/>
		<form:hidden path="dLId"/>
		<form:hidden path="iLId"/>
		<input type="hidden" id="mappingId">
		<c:set var="packageId_var" value="<c:out value="${standardPackageForm.packageId}"/>"/>
		<jsp:include page="_error.jsp"></jsp:include>
		 <div class='row form-group'>
			<div class='col-sm-4 col-lg-2' >
			    <label class="control-label "><spring:message code = "anvizent.package.label.packageName"/></label>
				<form:input path="packageName" class="form-control" disabled="true"/>
			</div>
			<div class='col-sm-4 col-lg-3' >
			    <label class="control-label "><spring:message code = "anvizent.package.label.dlName"/></label>
				<form:input path="dLName" class="form-control" disabled="true"/>
			</div>
			<div class='col-sm-4 col-lg-3' >
			    <label class="control-label "><spring:message code = "anvizent.package.label.ilName"/></label>
				<form:input path="iLName" class="form-control" disabled="true"/>
			</div>
			<div class='col-sm-4 col-lg-3' >
			<label class="control-label">&nbsp;</label>
			 <div>
			<a href="<c:url value="${referer}"/>" class="btn btn-primary btn-sm"><spring:message code = "anvizent.package.link.back"/></a>
			 </div>
			</div>
			
		</div>
     <div class="alert alert-danger Ilmessage" style="display:none;">
	  </div>
	<div class="alert alert-success successIlMessage" style="display:none;">
	</div>
	</form:form>  
	
	  <div id="viewIlSourceDetails">
	  <c:choose>
	  <c:when test="${not empty iLConnectionMapping}">
	  <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
	  
	  	<table class="table table-striped table-bordered tablebg" id="customPackageTable">
			<thead>
				<tr>
					<th><spring:message code = "anvizent.package.label.name"/></th>
					<th><spring:message code = "anvizent.package.label.mapping"/></th>
					<th><spring:message code = "anvizent.package.label.xReference"/></th>
					<th><spring:message code = "anvizent.package.label.mapping"/></th>
					<th><spring:message code = "anvizent.package.label.xReference"/></th>
					<th><spring:message code = "anvizent.package.label.preview"/></th>
				</tr>
			</thead> 
		 <tbody>
		  	<c:forEach items="${iLConnectionMapping}" var="iLConnectionMapping" varStatus="increment">
		  		<tr>
		  			<td><c:out value="${iLConnectionMapping.isFlatFile ? iLConnectionMapping.filePath : iLConnectionMapping.iLConnection.connectionName}"/></td>
		  			<td><c:out value="${iLConnectionMapping.xReferenceDetails.mappingCompleted ? 'Completed':'Pending'}"/></td>
		  			<td><c:out value="${iLConnectionMapping.xReferenceDetails.mergingCompleted ? 'Completed':'Pending'}"/></td>
		  			<td><button type="button" style="float:right;margin-top:-6px;" id="click" data-dlid="<c:out value="${iLConnectionMapping.dLId}"/>" 
			      data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" 
			      data-mappingCompleted="<c:out value="${iLConnectionMapping.xReferenceDetails.mappingCompleted}"/>"
			      data-mergingCompleted="<c:out value="${iLConnectionMapping.xReferenceDetails.mergingCompleted}"/>"
			      data-constraintsCompleted="<c:out value="${iLConnectionMapping.xReferenceDetails.constraintsCompleted}"/>"
			      data-sequenceCompleted="<c:out value="${iLConnectionMapping.xReferenceDetails.sequenceCompleted}"/>"
			      data-xRefId="<c:out value="${iLConnectionMapping.xReferenceDetails.xRefId}"/>"
			      data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" class="btn btn-primary btn-sm  xRefMappingButton"><spring:message code = "anvizent.package.label.mapping"/></button></td>
			      
			      <td><button type="button" style="float:right;margin-top:-6px;" id="click" data-dlid="<c:out value="${iLConnectionMapping.dLId}"/>" 
			      data-ilid="<c:out value="${iLConnectionMapping.iLId}"/>" 
			      data-mappingCompleted="<c:out value="${iLConnectionMapping.xReferenceDetails.mappingCompleted}"/>"
			      data-mergingCompleted="<c:out value="${iLConnectionMapping.xReferenceDetails.mergingCompleted}"/>"
			      data-constraintsCompleted="<c:out value="${iLConnectionMapping.xReferenceDetails.constraintsCompleted}"/>"
			      data-sequenceCompleted="<c:out value="${iLConnectionMapping.xReferenceDetails.sequenceCompleted}"/>"
			      data-xRefId="<c:out value="${iLConnectionMapping.xReferenceDetails.xRefId}"/>"
			      data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" class="btn btn-primary btn-sm  xRefMergingButton " <c:out value="${iLConnectionMapping.xReferenceDetails.mappingCompleted?"":"disabled"}"/> ><spring:message code = "anvizent.package.label.xReference"/></button></td>
			      
		  			<td><button type="button" style="float:right;margin-top:-6px;" data-connectionid="<c:out value="${iLConnectionMapping.iLConnection.connectionId}"/>" data-typeofcommand="<c:out value="${iLConnectionMapping.typeOfCommand}"/>" data-query="<c:out value="${iLConnectionMapping.iLquery}"/>" data-mappingid="<c:out value="${iLConnectionMapping.connectionMappingId}"/>" class="btn btn-primary btn-sm  <c:out value="${iLConnectionMapping.isFlatFile ? 'flatFilePreviewStandard':'databasePreviewStandard'}"/> "><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></button></td>
		  		</tr>	
		  	</c:forEach>
		  </tbody>
	  	
	  	</table>
		
      </div>
      
      <div class="xReferenceDetailsDiv" style="border: 1px solid #000;padding: 10px;">
      <div class="pull-right"> <button type="button" class="btn btn-sm btn-danger" id="closexReferenceDetailsBtn" ><spring:message code = "anvizent.package.button.x"/></button></div>
      <!-- <div class="clearfix"></div> -->
      	<div>
	      	<button type="button" class="btn btn-primary btn-sm xrefButton mappingBtn hidden" data-divId="mappingDiv-Xref"><spring:message code = "anvizent.package.label.mapping"/></button>
	      	<button type="button" class="btn btn-primary btn-sm xrefButton mergingBtn hidden" data-divId="mergingDiv-Xref"><spring:message code = "anvizent.package.label.merging"/></button>
	      	<button type="button" class="btn btn-primary btn-sm xrefButton constraintBtn hidden" data-divId="constraintDiv-Xref"><spring:message code = "anvizent.package.label.constraints"/></button>
	      	<button type="button" class="btn btn-primary btn-sm xrefButton sequenceBtn hidden" data-divId="sequenceDiv-Xref"><spring:message code = "anvizent.package.button.sequence"/></button>
      	</div>
      
      <div class="clearfix" style="height:20px;"></div>
      
	      <div id="mappingDiv-Xref">
	      	<div class="table-responsive" style="max-height: 400px;">
	      			<input type="hidden" id="ilMappingId">
	      			<input type="hidden" id="xRefId">
					<table class="table table-striped table-bordered tablebg" id="fileMappingWithILTable">
						<thead>
							<tr>
								<th><spring:message code = "anvizent.package.label.sNo"/></th>
								<th class="iLName"></th>
								<th class=""><spring:message code = "anvizent.package.label.dataType"/></th>
								<th><spring:message code="anvizent.package.label.pk"/></th>
							    <th><spring:message code="anvizent.package.label.nn"/></th>
							    <th><spring:message code="anvizent.package.label.ai"/></th>
								<th class="originalFileName"></th>
								<th><spring:message code = "anvizent.package.label.default"/></th>
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
		      	<button type="button" class="btn btn-primary xRefMergeButtons btn-sm" data-divId="columnsDiv-Xref"><spring:message code = "anvizent.package.label.xref.xrefbycondition"/></button>
		      	<button type="button" class="btn btn-primary xRefMergeButtons btn-sm" data-divId="valuesDiv-Xref"><spring:message code = "anvizent.package.label.xref.xrefbyvalues"/></button>
      		</div>
	      <div id="columnsDiv-Xref">
	      <div class="row hidden">
			<div class="col-sm-12">
				<div class="pull-right">
					<button class="btn btn-success addConditionGroupBtn" data-divId="columnsDiv-Xref"><spring:message code = "anvizent.package.button.addGroup"/></button>
				</div>
			</div>
		  </div>
		  <div style="height:10px;"></div>
	      <div class="row groupBoxTemplate hidden">
		    <div class="col-xs-12">
			<div class="pull-right addColumnBox">
				<button class="btn btn-success addConditionBtn" ><spring:message code = "anvizent.package.button.add"/></button>
				<button class="btn btn-danger removeGroupBtn hidden"><spring:message code = "anvizent.package.button.removeGroup"/></button>
			</div>
				<table class="table table-striped table-bordered tablebg tblForMergingConditions">
					<thead>
						<tr>
							<th class="iLName"></th>
							<th class="originalFileName"></th>
							<th>
								<spring:message code = "anvizent.package.label.delete"/>
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
								<spring:message code = "anvizent.package.label.conditionName"/>
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
					<button class="btn btn-success addConditionGroupBtn" data-divId="valuesDiv-Xref"><spring:message code = "anvizent.package.button.addGroup"/></button>
				</div>
			</div>
		  </div>
		  <div style="height:10px;"></div>
	      <div class="row groupBoxTemplate hidden">
		    <div class="col-xs-12">
			<div class="pull-right addColumnBox">
				<button class="btn btn-success addConditionBtn" ><spring:message code = "anvizent.package.button.add"/></button>
				<button class="btn btn-danger removeGroupBtn hidden" data-divId="valuesDiv-Xref"><spring:message code = "anvizent.package.button.removeGroup"/></button>
			</div>
				<table class="table table-striped table-bordered tablebg tblForMergingConditions">
					<thead>
						<tr>
							<th class="iLName"></th>
							<th><spring:message code = "anvizent.package.label.columnValues"/></th>
							<th class="originalFileName"></th>
							<th><spring:message code = "anvizent.package.label.columnValues"/></th>
							<th>
								<spring:message code = "anvizent.package.label.delete"/>
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
								<spring:message code = "anvizent.package.label.conditionName"/>
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
	      <div id="constraintDiv-Xref">
	      <spring:message code = "anvizent.package.label.constraint"/>
	      </div>
	      <div id="sequenceDiv-Xref">
	      <spring:message code = "anvizent.package.label.sequence"/>
	      </div>
      </div>
      
	  </c:when>
	  <c:otherwise>
	   <div class="alert alert-danger Ilmessage"  ><spring:message code = "anvizent.package.label.noSourceFileIsAdded"/>
	  </div>
	  </c:otherwise>
	  </c:choose>
       
 
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
      <table class='tablePreview table table-striped table-bordered tablebg'></table>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
      </div>
    </div> 
  </div> 
   </div>
   </div>
   		
   </div>
 