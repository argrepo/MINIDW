<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">

	<div class="dummydiv"></div>
	<ol class="breadcrumb">
	</ol>

	<input type="hidden" id="userID"
		value="<c:out value="${principal.userId}"/>">
	<div class="row form-group">
		<h4 class="alignText">Client ILInfo Results</h4>
	</div>
	<div id="successOrErrorMessage"></div>
	<div class='col-sm-12' style="padding: 0px;">

		<%-- <div class="row form-group" style="padding:5px;border-radius:4px;">
	                      <a href="<c:url value="/adt/package/webServiceConnection/add"/>" class="btn btn-success btn-sm"  style="float:right;">
						<spring:message code="anvizent.package.label.add"/>
					</a>
         </div> --%>
		<div class="row form-group" style="padding: 5px; border-radius: 4px;">
			<input type="button" id="addID" class="btn btn-success btn-sm"
				style="float: right;"
				value=<spring:message code="anvizent.package.label.add"/>>

		</div>

		<div role="tabpanel" class="tab-pane fade active in tabpad">
			<div class="row form-group">
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg "
						id="clientILInfo">
						<thead>
							<tr>
								<th>S.No</th>
								<th>ilID</th>
								<th>ilName</th>
								<th>Version</th>
								<th>IL Type</th>
								<th>Il Table Name</th>
								<th>Edit</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>
		</div>
		
		<!-- <div class='row form-group '  id="addDiv"  style="display:none">
		
			<div class='col-sm-9'>
					<div class="row form-group" id="il_id">
								<label class="control-label col-sm-3">IL Id</label>
								<div class="col-sm-8">
								 		<input type = "text" class="form-control ilName" id="ilID">
								</div>
					</div>
					<div class="row form-group" id="ilName">
								<label class="control-label col-sm-3">ilName</label>
								<div class="col-sm-8">
								 		<input type = "text" class="form-control ilName" id="ilNamee">
								</div>
					</div>
					<div class="row form-group" id="version">
								<label class="control-label col-sm-3">version</label>
								<div class="col-sm-8">
								 		<input type = "text" class="form-control ilName" id="versionn">
								</div>
					</div>
					
					<div class="row form-group" id="ilType">
								<label class="control-label col-sm-3">ilType</label>
								<div class="col-sm-8">
								 		<input type = "text" class="form-control ilName" id="ilTypee">
								</div>
					</div>
					<div class="row form-group" id="ilTableName">
								<label class="control-label col-sm-3">ilTableName</label>
								<div class="col-sm-8">
								 		<input type = "text" class="form-control ilName" id="ilTableNamee">
								</div>
					</div>
					<div class="row form-group" style="padding: 5px; border-radius: 4px;">
						<input type="button" id="saveid" class="btn btn-success btn-sm"
							style="float: right;"
							value="save"> 

					</div>
				
			</div>

		</div>
		
	</div>
	 -->
	
	
	
	
	<div class="modal fade" tabindex="-1" role="dialog" id="createIlinfoPopUp" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog">
				    <div class="modal-content" style="width:115%;">
					      <div class="modal-header">
						        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        	<h4 class="modal-title custom-modal-title connectionTitle">create IL info</h4>
					      </div>
					      <div class="modal-body">
					    	  <div class="panel panel-info">
					    	  		<div class="panel-body">
								<div class="row form-group" id="ilName">
											<label class="control-label col-sm-3">ilName</label>
											<div class="col-sm-8">
											 		<input type = "text" class="form-control ilName" id="ilNamee">
											</div>
								</div>
								<div class="row form-group" id="version">
											<label class="control-label col-sm-3">version</label>
											<div class="col-sm-8">
											 		<input type = "text" class="form-control ilName" id="versionn">
											</div>
								</div>
								
								<div class="row form-group" id="ilType">
											<label class="control-label col-sm-3">ilType</label>
											<div class="col-sm-8">
											 		<input type = "text" class="form-control ilName" id="ilTypee">
											</div>
								</div>
								<div class="row form-group" id="ilTableName">
											<label class="control-label col-sm-3">ilTableName</label>
											<div class="col-sm-8">
											 		<input type = "text" class="form-control ilName" id="ilTableNamee">
											</div>
								</div>
				
							</div>
						</div>											      
				      </div>
				      <div class="modal-footer">
							<input type="button" id="saveid" class="btn btn-success btn-sm"
								style="float: right;"
								value="save"> 
				      </div>
				    </div> 
			  </div> 
		</div>
</div>
</div>