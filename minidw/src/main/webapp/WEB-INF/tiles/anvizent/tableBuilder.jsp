<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<style type="text/css">
	.select2-results {
		font-size: 11px;
	}
	.border-red {
		border-color: red;
	}
	.border-green {
		border-color: green;
	}
</style>
<div class="col-sm-12 rightdiv">
	<div class="page-title-v1"><h4><spring:message code = "anvizent.package.label.derivedTableBuilder"/></h4></div>
    <div class="dummydiv"></div>
    
    <ol class="breadcrumb">
	</ol>
    <form:form modelAttribute="queryBuilderForm" method="POST" id="queryBuilderForm">
		<form:hidden path="packageId" value="${param['packageId']}"/>
		<form:hidden path="industryId"/>
	</form:form>

	<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
    <div class='row form-group'>
			<h4 class="alignText"><spring:message code = "anvizent.package.label.derivedTableBuilder"/></h4>
		</div>
    <div class='col-sm-12' id="targetTableColumns">
    	
		<div class="row form-group">
			<label class="col-md-2 control-label labelsgroup">
								<spring:message code = "anvizent.package.label.targetTable"/> :</label>
				<div class='col-sm-4' >
					<input type="text" class="form-control" value="<c:out value="${targetTable.tableName}"/>" disabled="disabled"/>
				</div>
		</div>
		<c:if test="${not empty targetTable}">
			<div class='row form-group' id="">
				<input type="hidden" value="<c:out value="${targetTable.tableName}"/>" id="tablename">
	    		<input type="hidden" value="<c:out value="${targetTable.tableId}"/>" id="tableid">
	    		<input type="hidden" id="query" value="">
				<div class="col-sm-12">
					<div class="table-responsive" style="overflow-y:overlay; max-height: 400px;">
						<table class="table table-striped table-bordered tablebg" id="">
							<thead>
								<tr>
									<th><input type="checkbox" class="selectAll"><spring:message code = "anvizent.package.label.select"/></th>
									<th><spring:message code = "anvizent.package.label.columnName"/></th>
									<th><spring:message code = "anvizent.package.label.dataType"/></th>
									<th><spring:message code = "anvizent.package.label.dimensionMeasure"/></th>
									
								</tr>
							</thead>
							 <tbody>
								<c:forEach var="column" items="${targetTable.columns}" varStatus="loopStatus">
										<tr class="j-row">
											<td><input type="checkbox" class="jcolumn-check"></td>
											<td class="jcolumn-name"><c:out value="${column.columnName}"/></td>
											<td class="jcolumn-dtype"><c:out value="${column.dataType}"/></td>
											<td>
												<select class="jcolumn-type">
									    			<option value="Dimension"><spring:message code = "anvizent.package.label.dimension"/></option>
									    			<c:if test="${column.dataType != 'VARCHAR' && column.dataType != 'DATETIME'}">
									    				<option value="Measure" selected="selected"><spring:message code = "anvizent.package.label.measure"/></option>
									    			</c:if>
								    			</select>
											</td>
										</tr>
								</c:forEach>
							</tbody> 
						</table>
					</div>
					<div id='duplicateColValidation'></div>
				</div>				
			</div>
		</c:if>
    	<div class='row form-group'>
			<label class="col-sm-3 control-label labelsgroup" for="targetTableName"> <spring:message code = "anvizent.package.label.derivedTableName"/>:</label>
			<div class='col-sm-4' >
				<input type="text" id="targetTableName" class="form-control" data-minlength='1' data-maxlength='64'/>
			</div>
			<div class="col-sm-3">
    			<button type="button" id="customcolumn" class="btn btn-sm btn-primary"><spring:message code = "anvizent.package.label.addCustomColumn"/></button>
    		</div>
		</div>
		<div class="row">
	    	<div class="table-responsive col-sm-9">
				<table class="table table-striped table-bordered tablebg" id="customColumnsTable" style="display:none;">
					<thead>
						<tr>
							<th><spring:message code = "anvizent.package.label.columnName"/></th>
							<th><spring:message code = "anvizent.package.label.valueType"/></th>
							<th><spring:message code = "anvizent.package.label.columnValues"/></th>
							<th><spring:message code = "anvizent.package.label.dataType"/></th>
							<th class='col-sm-1'><spring:message code = "anvizent.package.label.delete"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
		<jsp:include page="_error.jsp"></jsp:include>
		<div class="row form-group">
    		<div class="col-sm-12">
    			<button type="button" id="savebutton" class="btn btn-sm btn-primary" style="margin-left: 5px;display:none;"><spring:message code = "anvizent.package.button.saveTable"/></button>
    			<button type="button" id="validatebutton" class="btn btn-sm btn-primary"><spring:message code = "anvizent.package.button.validate"/></button>
    			<a class="btn btn-primary btn-sm tablebuttons"  href="<c:url value="/adt/package/customPackage/edit/${queryBuilderForm.packageId}"/>" ><input type="button" class="btn btn-primary btn-sm back_btn" value="<spring:message code = "anvizent.package.link.back"/>"/> </a>
    		</div>
    	</div>
    </div>
    
    <div class="modal fade" tabindex="-1" role="dialog" id="customColumnDivPopup" data-backdrop="static" data-keyboard="false">
	  <div class="modal-dialog modal-lg">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		    <h4 class="modal-title custom-modal-title"><spring:message code = "anvizent.package.label.addCustomColumn"/></h4>
	      </div>
	      <div class="modal-body">
	      	<div class='row form-group' id="customColumnDiv">
				<label class="col-sm-3 control-label" for="customColumnName"> <spring:message code = "anvizent.package.label.columnName"/> :</label>
				<div class='col-sm-6' >
					<input type="text" id="customColumnName" class="form-control" />
				</div>
			</div>
	        <div class="row form-group">
	        	<div class="col-md-3">
	        		<label><spring:message code = "anvizent.package.label.valueType"/></label>
	        	</div>
	        	<div class="col-md-9">
	        		<div id="valueTypediv">
		        		<label style="font-weight: normal;" class="radio-inline">
		        			<input type="radio" value="Derived" name="valueType">
		        				<spring:message code = "anvizent.package.label.derived"/>
		        		</label>
		        		<label style="font-weight: normal;" class="radio-inline">
		        			<input type="radio" value="Custom" name="valueType">
		        				<spring:message code = "anvizent.package.label.custom"/>
		        		</label>
		        		<label style="font-weight: normal;" class="radio-inline">
		        			<input type="radio" value="Default" name="valueType">
		        				<spring:message code = "anvizent.package.label.constant"/>
		        		</label>
	        		</div>
	        	</div>
	        </div>
	        <div id="customvaluerow" class="row form-group" style="display: none;">
	        	<div class="col-sm-3">
	        	</div>
	        	<div class="col-sm-8">
	        		 <div class='customDefaultInfo'></div>
	        	</div>
	        	<div class="col-sm-3">
	        		<label><spring:message code = "anvizent.package.label.value"/></label>
	        	</div>
	        	<div class="col-sm-6">
	        		<input type="text" class="form-control" id="customvalue">
	        	</div>
	        </div>
	        <div class="row form-group" style="display: none;" id="dereivedvaluerow">
		        	<div class="col-sm-3">
		        		<select id="aggregates" style="width:100%;">
		        			<option value=""><spring:message code = "anvizent.package.label.aggregates"/></option>
		        			<option value="SUM"><spring:message code = "anvizent.package.label.sum"/></option>
		        			<option value="MIN"><spring:message code = "anvizent.package.label.min"/></option>
		        			<option value="MAX"><spring:message code = "anvizent.package.label.max"/></option>
		        			<option value="AVG"><spring:message code = "anvizent.package.label.avg"/></option>
		        			<option value="COUNT"><spring:message code = "anvizent.package.label.count"/></option>
		        		</select>
		        	</div>
		        	<div class="col-sm-3">
		        		<select id="operations" style="width:100%;">
		        			<option value=""><spring:message code = "anvizent.package.label.operations"/></option>
		        			<option value="*"><spring:message code = "anvizent.package.label.multiplication"/></option>
		        			<option value="+"><spring:message code = "anvizent.package.label.addition"/></option>
		        			<option value="-"><spring:message code = "anvizent.package.label.subtraction"/></option>
		        			<option value="/"><spring:message code = "anvizent.package.label.division"/></option>
		        		</select>
		        	</div>
		        	<div class="col-sm-6">
		        		<select id="columns" style="width:100%;">
		        			<option value=""><spring:message code = "anvizent.package.label.selectColumn"/></option>
		        		</select>
		        	</div>
	        	</div>
	        <div class="row form-group">
	        	<div class="col-sm-3">
	        		<label><spring:message code = "anvizent.package.label.dataType"/></label>
	        	</div>
	        	<div class="col-sm-6">
	        		<select class="form-control input-sm" id="dataType">
	        		
						<c:forEach items="${dataTypesList}" var="dataTypes">
						  <option value="<c:out value="${dataTypes.dataTypeName}"/>"><c:out value="${dataTypes.dataTypeName}"/></option>
						</c:forEach>
	        		
						<%-- <option value="VARCHAR"><spring:message code = "anvizent.package.label.varchar"/></option>
						<option value="INT"><spring:message code = "anvizent.package.label.int"/></option>
						<option value="BIGINT"><spring:message code = "anvizent.package.label.bigInt"/></option>
						<option value="DECIMAL"><spring:message code = "anvizent.package.label.decimal"/></option>
						<option value="BIT"><spring:message code = "anvizent.package.label.bit"/></option>
						<option value="DATETIME"><spring:message code = "anvizent.package.label.dateTime"/></option>
						<option value="DATE"><spring:message code = "anvizent.package.label.date"/></option> --%>
					</select>
	        	</div>
	        </div>
	        <div class="row form-group">
	        	<div class="col-sm-3">
	        		<label><spring:message code = "anvizent.package.label.length"/></label>
	        	</div>
	        	<div class="col-sm-2">
	        		<input type="text" id="collength" class="form-control">
	        	</div>
	        	<div class="col-sm-2">
	        		<input type="text" id="colscale" style="display:none;" class="form-control">
	        	</div>
	        	
	        </div>
	         <div class="row form-group note" style='display:none;'>
	        	<p class="help-block">
	        		<em><spring:message code = "anvizent.package.label.noteColumnsonwhichaggregatesareapplicablewillappearinSelectColumnlist"/></em>  
	        	</p>	        	 
	        </div>
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-Please select value typeprimary btn-sm" id="saveCustomColumn"><spring:message code = "anvizent.package.button.add"/></button>
	        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
	      </div>
	    </div>
	  </div>
	</div>
	
	
	<div class="modal fade" tabindex="-1" role="dialog" id="targetTablePopup" data-backdrop="static" data-keyboard="false">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      	<div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		         <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
			     <h4 class="modal-title custom-modal-title"></h4>
		    </div>
	      	<div class="modal-body">
	      		<div class="row form-group">
		      		<div class="col-sm-12">
			        	<div id="targetTableMessage" class ="alert" style="text-align: center;"></div>
		      		</div>
		      	</div>
		      	<div class="row">
		      		<div class="col-sm-12">
		      			<a href="<c:url value="/adt/package/custompackage"/>" class="btn btn-primary btn-sm"><spring:message code = "anvizent.package.header"/></a>
			        	<a href='<c:url value="/adt/package/schedule?packageId=${queryBuilderForm.packageId}"/>' class="btn btn-primary btn-sm"><spring:message code = "anvizent.package.label.schedule"/></a>
			        	<button type="button" class="btn btn-primary btn-sm" data-dismiss="modal"><spring:message code = "anvizent.package.button.createDerivedTable"/></button>
			        	<button type="button" class="btn btn-primary btn-sm" data-dismiss="modal"><spring:message code = "anvizent.package.button.cancel"/></button>
		      		</div>
		      	</div>
	     	 </div>
	    </div>
	  </div>
	</div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="messagePopUp" data-backdrop="static" data-keyboard="false">
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
		        	<div id="popUpMessage" class ="alert" style="text-align: center;"></div>
	      		</div>
	      	</div>
	      	<div class="row">
	      		<div class="col-sm-12" style="text-align: center;">
		        	<button type="button" class="btn btn-primary btn-sm" data-dismiss="modal"><spring:message code="anvizent.package.button.ok"/></button>
	      		</div>
	      	</div>
	      </div>
	    </div>
	  </div>
	</div>
	
</div>   