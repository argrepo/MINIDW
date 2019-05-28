<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">      
       <ol class="breadcrumb">
		
		</ol>
     <%-- UI Changes --%> 
      <jsp:include page="_error.jsp"></jsp:include>
      <input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
      <div class="row form-group">
      	<h4 class="alignText"><spring:message code="anvizent.package.label.purgeTables"/></h4>
      </div>
    <div class="col-sm-12">
		<div id="successOrErrorMessage"></div>
	</div>	
   	<div class="col-sm-12">
	  	<div class='row form-group'id="tableName">
			<div class="table-responsive">
				<div style = "overflow:auto;max-height:480px">
					<table class="table table-striped table-bordered tablebg clientILsAndDLs">
						<thead>
							<tr>
	                           <th><spring:message code = "anvizent.package.label.selectAll"/>  <input type="checkbox" value="selectAll" class="selectAll"></th>
	                           <th><spring:message code = "anvizent.package.label.tableName"/></th>
							</tr>
						</thead>
						<tbody>
	
							<c:forEach items="${clientStandardTables}" var="table" varStatus="increment">
								<tr>
	                            	<td><input type="checkbox" value="<c:out value="${table.tableStructure}"/>" class="check" data-tableid="<c:out value="${table.tableId}"/>"></td>
	                            	<td><c:out value="${table.tableName}"/></td>
								</tr>
	
							</c:forEach>
	
						</tbody>
					</table>
				</div>
			</div> 
		</div>
	</div>
	<div class="col-sm-12">
		<div class="row form-group">
	        <button class="btn btn-sm btn-primary purgeUserTables" id="validate"><spring:message code="anvizent.package.label.purgeTables"/></button>
	    </div>
	    <div id="selectOneTableAlert"></div>
	</div>
	<div class="modal fade" tabindex="-1" role="dialog" id="alertForPurgeUserTables" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 500px;">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        <h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.label.purgeTables"/></h4>
				      </div>
				      <div class="modal-body">
				        	<p><spring:message code="anvizent.package.message.deleteSource.areYouSureYouWantToTruncateeUserTables"/></p>
				      </div>
				      <div class="modal-footer" style="text-align: center;">					        	
							<button type="button" class="btn btn-primary btn-sm confirmPurgeUserTables" id = "checked"><spring:message code="anvizent.package.button.ok"/></button>
							<button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				      </div>
			    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
	 </div> 
</div>

 