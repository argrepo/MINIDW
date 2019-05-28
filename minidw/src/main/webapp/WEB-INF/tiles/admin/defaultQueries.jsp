<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">	
	<ol class="breadcrumb">
	</ol>
	
	 
	<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	
	<div class="row form-group">
      	 <h4 class="alignText"><spring:message code="anvizent.package.label.ilPreBuildQueries"/></h4>
    </div>
	
	<div class="col-sm-10">
		<div id="successOrErrorMessage"></div>
	</div>	
	<jsp:include page="admin_error.jsp"></jsp:include>
	
	<div class='row form-group'> 
		<label class="control-label labelsgroup col-sm-2"> <spring:message code="anvizent.package.label.connector" /> :</label>
	    <div class="col-sm-6">
		   <select class="selectConnector form-control" name="selectConnector"> 
		   		<option value="0" selected>Select Connector</option>
			    <c:forEach items="${databseList}" var="database">
					<option value="${database.id}"><c:out value="${database.name}"/></option>
				</c:forEach>
		    </select>
		</div>
		<div class="col-sm-2" >	
		 	<button type="button" id="add" class="btn btn-sm btn-success" style="margin:5px 0px" ><spring:message code = "anvizent.package.button.add"/></button>
		</div>
	</div>
	<div class="row form-group defaultQueriesDiv" style="display: none;">
		<table  class="table table-striped table-bordered tablebg" id = "defaultQueryTable">
			<thead>
				<tr>
				    <th class="col-md-1"><spring:message code="anvizent.package.label.sNo"/></th>  
					<th class="col-md-1"><spring:message code="anvizent.package.label.ilName"/></th>
					<th class="col-md-1"><spring:message code="anvizent.package.label.ilQuery"/></th>
					<th class="col-md-2"><spring:message code="anvizent.package.label.ilIncrementalupdate"/></th>
					<th class="col-md-2"><spring:message code="anvizent.package.label.maxDateQuery"/></th>
					<th class="col-md-2"><spring:message code="anvizent.package.label.historicalLoad"/></th>
					<th class="col-md-1"><spring:message code="anvizent.package.label.isActive"/></th>
					<th class="col-md-1"><spring:message code="anvizent.package.label.edit"/></th>
				</tr>
			</thead>
			<tbody>	
			</tbody>
		</table>
	</div>		
		
	    	
	<div class="modal fade" role="dialog" id="addDefaultQueryPopUp" data-backdrop="static" data-keyboard="false" style="display: none;">
		  <div class="modal-dialog" style="width:50%;">
			    <div class="modal-content">
			      	<form method="POST" action='<c:url value="/admin/saveILDefaultQuery"/>' id="saveILDefaultQueryForm">
				      	<div class="modal-header">
						        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
						        <h4 class="modal-title custom-modal-title heading"></h4>
					    </div>
					    <div class="modal-body">
							<div class="row form-group">
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.connectorName" /></label>
								<div class="col-sm-8">
									<input type="text" class="form-control" id="connectorName" disabled="disabled">
									<input type="hidden" id="databaseTypeId" name="id">
								</div>
							</div> 
							
							<div class="row form-group" id="ilQuerySelect" style="display: none;">
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.ilName"/></label>
								<div class="col-sm-8">
					       			<select class="selectIlname form-control col-sm-12" name="ilName">
								   		<option value="0" selected><spring:message code="anvizent.package.label.selectIL"/></option>
								    </select>
								</div>
							</div>
							
							<div class="row form-group" id="ilQueryText" style="display: none;">
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.ilName"/></label>
								<input type="hidden" id="iLId" name="iLId">
								<div class="col-sm-8">
								 		<input type = "text" class="form-control ilName" id="ilQueryName" disabled="disabled">
								</div>
							</div>
							
							<div class="row form-group" id="query">
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.query" /></label>
								<div class="col-sm-8">
									<textarea class="form-control" rows="3" id="queryScript" name="iLquery" placeholder="<spring:message code="anvizent.package.label.ilQuery"/>"></textarea> <br /> 
								</div>
							</div>
							<div class="row form-group" id="incremantalQuery">
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.incrementalUpdateQuery" /></label>
								<div class="col-sm-8">
									<textarea class="form-control" rows="3" id="incrementalQuery" name="iLIncrementalUpdateQuery" placeholder="<spring:message code="anvizent.package.label.incrementalUpdateQuery"/>"></textarea> <br /> 
								</div>
							</div>
							<div class="row form-group" id="maxDateQuery">
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.maxDateQuery" /></label>
								<div class="col-sm-8">
									<textarea class="form-control" rows="3" id="maxDate" name="maxDateQuery" placeholder="<spring:message code="anvizent.package.label.maxDateQuery"/>"></textarea> <br /> 
								</div>
							</div>
							<div class="row form-group" id="historicalLoad">
								<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.historicalLoad" /></label>
								<div class="col-sm-8">
									<textarea class="form-control" rows="3" id="historyLoad" name="historicalLoad" placeholder="<spring:message code="anvizent.package.label.historicalLoad"/>"></textarea> <br /> 
								</div>
							</div>
			     	</div>
				    <div class="modal-footer">
				        <input type="button" id="update" class="btn btn-primary btn-sm" style="display: none;" value="<spring:message code = "anvizent.package.button.update"/>">
				    	<button type="submit" id="save" class="btn btn-primary btn-sm" style="display: none;"><spring:message code = "anvizent.package.button.save"/></button>
				        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				    </div>
				    <input type="hidden" name="<c:out value="${_csrf.parameterName}" />" value="<c:out value="${_csrf.token}" />"/>
			    </form>
			    
		    </div><!-- /.modal-content -->
	  	</div><!-- /.modal-dialog -->
	</div>	
		<div class="modal fade" tabindex="-1" role="dialog" id="viewQueryPopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 60%;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title heading"><spring:message code="anvizent.package.label.ilQuery"/></h4>
		      </div>
		      <div class="modal-body" style="max-height: 400px; overflow-y: auto;">
					<div style='overflow-y: auto;max-height: 300px;'>
						<textarea class='view-Query' readonly="readonly" rows="10" cols="10" style="width:100%">
								
						</textarea>
					</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>			
	
</div>
