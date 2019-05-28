<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  <div class='row form-group'>
<h4 class="alignText"><spring:message code="anvizent.admin.button.viewILDL"/></h4>
</div>
	<div class="col-md-10">
			<div class="dummydiv"></div>
			<div>
			</div>
			<jsp:include page="_error.jsp"></jsp:include>
			<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	</div>
	
	<div class="col-md-12 " >
		<div class="row form-group" >			 
			<div class="col-sm-4">
				<label class="radio-inline control-label">
			    	<input type="radio" name="iLOrDl" value="IL" class=""> <spring:message code="anvizent.package.label.viewIL"/>		    	
			    </label>
			    <label class="radio-inline control-label">
			    	<input type="radio" name="iLOrDl" value="DL" class=""><spring:message code="anvizent.package.label.ViewDL"/>		    	
			    </label>			   
			</div>		
		</div>	
	</div>
	<form:form method="POST"  id="updateILDLForm" enctype="multipart/form-data" class="form-horizontal" role="form">
 	<div class="col-md-10 ilUpdationMsg"></div>
 	<div class="col-md-10 dlUpdationMsg"></div>
 	<div class="col-md-6"   id="iLdLList" style="display:none;">
		 
		<div class="form-group ilList">		 
			<label class="col-sm-3 control-label"><spring:message code="anvizent.package.label.ilName"/> :</label>
			<div class="col-sm-6">
				<select class="form-control" id="ilNameList">                    
				</select>
			</div>		
		</div>
		<div class="form-group dlList">		 
			<label class="col-sm-3 control-label"><spring:message code="anvizent.package.label.dlName"/> :</label>
			<div class="col-sm-6">
				<select class="form-control" id="dlNameList">                    
				</select>
			</div>		
		</div>		
	</div>
		
	<div class="iLAllDetails" style="display:none;">
		<div class="col-md-10 dummy"></div>
	
		<div class="col-md-6 ilDetails">
			
			<div class="form-group">
					<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.ilNameDisplayName"/></label> 
					<div class="col-sm-6">
						<input type="hidden" class="form-control" id="ilId"> 
						<input type="text" class="form-control" id="ilName">
					</div>
			</div>
			<div class="form-group">
					<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.tableName"/></label> 
					<div class="col-sm-6"> 
						<input type="text" class="form-control" id="tableName">
					</div>
			</div>
			<div class="form-group">
					<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.description"/></label> 
					<div class="col-sm-6">						
						<textarea rows="4" cols="50" class="form-control" id="description"></textarea>
					</div>
			</div>
			<div class="form-group">
					<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.jobName"/></label> 
					<div class="col-sm-6 " >
						<input type="text" class="form-control" id="JobName">
					</div>
			</div>			
			<div class="form-group" id="databasediv">
				<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.database"/>:</label>
				<div class="col-sm-6">
					<select class="form-control" id="iLDbList"></select>
					<select class="form-control" id="iLQueryList" style='display:none;'></select>
				</div>								
			</div>
			<div class="form-group" id="querydiv">
				<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.ilQuery"/></label>
				<div class="col-sm-6">
					<textarea rows="4" cols="50" id="iLQuery" class="form-control"></textarea>
				</div>				
			</div>
			<div class="form-group" id="savebutton">
				<label class="control-label col-sm-3" ></label>
				<div class="col-sm-3">
					<button id="saveQuery" type="button" class="btn btn-primary btn-sm" style="display:none;"><spring:message code="anvizent.package.label.saveQuery"/></button>
				</div>
				<div class="col-sm-4" id="savedQuery" role="alert"></div>	
			</div>
			<div class="form-group" id="il-fileContainer">						
			</div>
			<div class="form-group" id="il-fileContainer0">				 
					<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.file"/></label> 
					<div class="col-sm-6 il-input-fields">
						<input type="file"  class="filePath0 fileName" name="files" id="ilinputFile"  data-buttonText="Find file" disabled="disabled">
					</div>
					<button  class="btn btn-primary btn-xs il-addFile" disabled="disabled">
					<span class="glyphicon glyphicon-plus"></span></button>	
			</div>
			<div class="il-addFilePath" style='display:none'></div>			
		</div>
						   
     
     <div class="col-md-6 iLcontext-param-details">	 
        <div class="panel panel-info" id=''>		    
			<div class="panel-heading"><spring:message code="anvizent.admin.label.ExistingContextParameters"/></div>
			<div class="panel-body" style="overflow-y:scroll;max-height:425px">			
			<table class="table table-striped table-bordered tablebg table-hover" id="il-data-table">
                
            </table>
			</div>
       </div>
       <div id="il-context-params"></div>
	</div>
	
	</div>	
	
	<div class="dLAllDetails" style="display:none;">		
		<div class="col-md-10 dummy"></div>
		
			<div class="col-md-6 dlDetails">
				
				<div class="form-group">
						<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.dLNameDisplayName"/></label> 
						<div class="col-sm-6">
							<input type="hidden" class="form-control" id="dlId"> 
							<input type="text" class="form-control" id="dlName">
						</div>
				</div>
				<div class="form-group">
						<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.tableName"/></label> 
						<div class="col-sm-6"> 
							<input type="text" class="form-control" id="dltableName">
						</div>
				</div>
				<div class="form-group">
						<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.description"/></label> 
						<div class="col-sm-6">						
							<textarea rows="4" cols="50" class="form-control" id="dldescription"></textarea>
						</div>
				</div>
				<div class="form-group">
						<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.jobName"/></label> 
						<div class="col-sm-6 " >
							<input type="text" class="form-control" id="dlJobName">
						</div>
				</div>
				<div class="form-group" id="dl-fileContainer">	
										 
				</div>				
				<div class="form-group" id="dl-fileContainer0">				 
					<label class="control-label col-sm-3" ><spring:message code="anvizent.package.label.file"/></label> 
					<div class="col-sm-6 dl-input-fields">
						<input type="file"  class="filePath0 fileName" name="files" id="dlinputFile"  data-buttonText="Find file" disabled="disabled">
					</div>
					<button  class="btn btn-primary btn-xs dl-addFile" disabled="disabled">
					<span class="glyphicon glyphicon-plus"></span></button>	
				</div>
				
				<div class="dl-addFilePath" style='display:none'></div>
								
			 </div>
					 
		     <div class="col-md-6 dLcontext-param-details">	 
		        <div class="panel panel-info" id=''>		    
					<div class="panel-heading"><spring:message code="anvizent.admin.label.ExistingContextParameters"/></div>
					<div class="panel-body" style="overflow-y:scroll;max-height:425px">			
					<table class="table table-striped table-bordered tablebg table-hover" id="dl-data-table">
		                
		            </table>
					</div>
		       </div>
		       <div id="dl-context-params"></div>
			</div>		 
			
	</div>
	<div class="col-md-10 ">
		<div class="form-group"> 
		<div class="col-sm-6"> 
			<a href='<c:url value="/admin/ETLAdmin"/>' class="btn btn-primary btn-sm"><spring:message code="anvizent.admin.button.Back"/></a>
			<button  id='editIl' type="button" class="btn btn-primary btn-sm" style="display:none;"><spring:message code="anvizent.admin.button.Edit"/></button>
			<button  id='updateIl' type="button" class="btn btn-primary btn-sm" style="display:none;"><spring:message code="anvizent.admin.button.Update"/></button>
			<button  id='deleteIl' type="button" class="btn btn-primary btn-sm" style="display:none;"><spring:message code="anvizent.admin.button.Delete"/></button>
			<button  id='editDl' type="button" class="btn btn-primary btn-sm" style="display:none;"><spring:message code="anvizent.admin.button.Edit"/></button>
			<button  id='updateDl' type="button" class="btn btn-primary btn-sm" style="display:none;"><spring:message code="anvizent.admin.button.Update"/></button>
			<button  id='deleteDl' type="button" class="btn btn-primary btn-sm" style="display:none;"><spring:message code="anvizent.admin.button.Delete"/></button>			
		</div>
		</div>
	</div> 	
</form:form>
	
	
	<div class="modal fade" tabindex="-1" role="dialog" id="deleteIlFileAlert" data-backdrop="static" data-keyboard="false">
	  <div class="modal-dialog">
		    <div class="modal-content">
			      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>				        
				        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
				        <h4 class="modal-title custom-modal-title">
				       <spring:message code="anvizent.package.label.deleteFile"/>
				        </h4>
			      </div>
			      <div class="modal-body">
			        	<p>
			        	<spring:message code = "anvizent.package.message.areYouSureYouWantToDeleteThisIL"/></p>
			      </div>
			      <div class="modal-footer">				      	
				        <button type="button" class="btn btn-primary" id="confirmDeleteIL" data-dismiss="modal"><spring:message code="anvizent.package.button.yes"/></button>
				        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
			      </div>
		    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div>
	
	<div class="modal fade" tabindex="-1" role="dialog" id="deleteDlFileAlert" data-backdrop="static" data-keyboard="false">
	  <div class="modal-dialog">
		    <div class="modal-content">
			      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>				        
				        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
				        <h4 class="modal-title custom-modal-title">
				        <spring:message code="anvizent.package.label.deleteFile"/>
				        </h4>
			      </div>
			      <div class="modal-body">
			        	<p>
			        	<spring:message code = "anvizent.package.message.areYouSureYouWantToDeleteThisDL"/></p>
			      </div>
			      <div class="modal-footer">				      	
				        <button type="button" class="btn btn-primary" id="confirmDeleteDL" data-dismiss="modal"><spring:message code="anvizent.package.button.yes"/></button>
				        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
			      </div>
		    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div>	 
 </div>

 