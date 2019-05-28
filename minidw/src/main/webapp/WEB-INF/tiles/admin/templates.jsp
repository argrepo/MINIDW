<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">      
      	<ol class="breadcrumb">	
		</ol>
    <%-- UI Changes --%> 
     <jsp:include page="admin_error.jsp"></jsp:include>
     <input type="hidden" id="userID" value="${principal.userId}">
	    <div class="row form-group">
	    	<h4 class="alignText"><spring:message code="anvizent.package.label.template"/></h4>
	    </div>
		<div class='col-sm-12'>
	    
		<div id="successOrErrorMessage"></div>
			<div class="row form-group">
				<table  class="table table-striped table-bordered tablebg" id="iLAndXrefTemplates">
					<thead>
						<tr>
						    <th><spring:message code="anvizent.package.label.ilID"/></th>  
							<th><spring:message code="anvizent.package.label.ilName"/></th>  
							<th><spring:message code="anvizent.package.label.fielName"/></th>
							<th><spring:message code="anvizent.package.label.xrefFileName"/></th>
							<th><spring:message code="anvizent.package.label.uploadFile"/></th>
							<th><spring:message code="anvizent.package.label.downloadILFile"/></th>
							<th><spring:message code="anvizent.package.label.downloadXrefFile"/></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${template}" var="template" varStatus="increment">
							<tr>
	               				<td>${template.il_id}</td>
	               				<td>${template.IL_name}</td>
	               				<c:choose>
	                       			<c:when test="${not empty template.filename}">	                            	
	                       					<td>${template.filename}</td>
	                       			</c:when>
	                       			<c:otherwise>
	                       					<td>-</td>
	                       			</c:otherwise>
		                       	</c:choose>
	              				                  			
		                       	<c:choose>
	                       			<c:when test="${not empty template.xref_filename}">	                            	
	                       					<td>${template.xref_filename}</td>
	                       			</c:when>
	                       			<c:otherwise>
	                       					<td>-</td>
	                       			</c:otherwise>
		                       	</c:choose>
		                       	<td><a href="#" class="upload" data-ILID="${template.il_id}" data-IlName="${template.IL_name}" data-IlFileName="${template.filename}" data-XrefFileName="${template.xref_filename}">Upload</a></td>
		                    	<td>
			                       	<c:if test="${not empty template.filename}">
										<button type="button" data-ILID="${template.il_id}" class="btn btn-primary btn-sm downloadILTemplate"><span title="Download IL Template" class="glyphicon glyphicon-download-alt" aria-hidden="true"></span></button>
									</c:if>
									<c:if test="${empty template.filename}">
										<button type="button" disabled="disabled" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span></button>
									</c:if>
								</td>
								<td>
									<c:if test="${not empty template.xref_filename}">
										<button type="button" data-ILID="${template.il_id}" data-XrefFileName="${template.xref_filename}" class="btn btn-primary btn-sm downloadXrefTemplate"><span title="Download Xref Template" class="glyphicon glyphicon-download-alt" aria-hidden="true"></span></button>	
									</c:if>
									
									<c:if test="${empty template.xref_filename}">
										<button type="button" disabled="disabled" class="btn btn-primary btn-sm" ><span title="Download Xref Template" class="glyphicon glyphicon-download-alt" aria-hidden="true"></span></button>
									</c:if>	
								</td>	
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>			
		</div>
<div class="modal fade" tabindex="-1" role="dialog" id="viewUploadFilePopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width:540px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title" id="ilNameHeaderText"></h4>
		      </div>
		      <div class="modal-body" style="max-height: 400px; overflow-y: auto;">
			       <form:form id="uploadIlAndXrefTemplate" enctype="multipart/form-data">		        	
			        	<div class='row form-group'>
			        	        <div class='col-sm-3'>
									<spring:message code="anvizent.package.label.ilTemplate"/>:
								</div>
								<div class='col-sm-4'>
									<input type="file"  data-buttonText="Find file" id="il_template" name="il_template">
									<div id="il_template_errorMessage" style="display: none;"></div>
								</div> 
						</div>
						<div class='row form-group'>
								<div class='col-sm-3'>
									<spring:message code="anvizent.package.label.xrefTemplate"/>:
								</div>
								<div class='col-sm-4'>
									<input type="file"  data-buttonText="Find file" id="xref_template" disabled="disabled" name="xref_template">
									<div id="xref_template_errorMessage" style="display: none;"></div>
								</div>
			        	</div>
			        	<div class='row form-group'>
								<div class='col-sm-3'>
									<spring:message code="anvizent.package.label.sameAsIl"/>:
								</div>
								<div class='col-sm-1'>
									<input type="checkbox" id="sameAsIl" checked="checked">
								</div>								
								<input type="hidden" name="iLName" id="iLName">
								<input type="hidden" name="iLId" id="iLId">
								<input type="hidden" name="isInsert" id="isInsert">		
								<input type="hidden" name="ilfileName" id="ilfileName">
								<input type="hidden" name="xrefFilename" id="xrefFilename">
			        			<input type="hidden" name="isSameAsIL" id="isSameAsIL">
			        			
			        	</div>
			        </form:form>	
		      </div>
		      
		      <div class="modal-footer">
		       		 <button type="button" class="btn btn-primary" id="uploadButton"><spring:message code="anvizent.package.button.upload"/></button>	
		       		 <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		      
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>	
		<div class="modal fade" tabindex="-1" role="dialog" id="downloadILTemplate" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"  ><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.template"/></h4>
		      </div>
		      <div class="modal-body" >
		         <div class="container">
				    <label class="radio-inline">
				      <input type="radio" name="ilTemplate" id="ilCsv" checked><spring:message code="anvizent.package.label.csv"/>
				    </label>
					</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary" id="confirmDownloadILTemplate"><spring:message code="anvizent.package.button.ok"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal" ><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div> 
		  </div> 
		</div> 		
		
		<div class="modal fade" tabindex="-1" role="dialog" id="downloadXrefILTemplate" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"  ><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.template"/></h4>
		      </div>
		      <div class="modal-body" >
		         <div class="container">
				    <label class="radio-inline">
				      <input type="radio" name="xrefilTemplate" id="xrefilCsv" checked><spring:message code="anvizent.package.label.csv"/>
				    </label>
					</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary" id="confirmDownloadXrefILTemplate"><spring:message code="anvizent.package.button.ok"/></button>
		        <button type="button" class="btn btn-default" data-dismiss="modal" ><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div> 
		  </div> 
		</div> 	
</div>
 