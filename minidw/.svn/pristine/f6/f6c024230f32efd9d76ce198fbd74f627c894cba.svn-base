<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
       	<div class='row form-group'>
		<h4 class="alignText">
			<spring:message code="anvizent.navLeftTabLink.label.customDataSets"/>
		</h4>
	</div>
	<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	<jsp:include page="_error.jsp"></jsp:include>
	
	<div  style="width:100%;padding:0px 15px;">
	<div class='ddlRunStatus'></div>
		             <div class="row form-group" style="padding:5px;border-radius:4px;">
                        <a href="<c:url value="/adt/package/easyQueryBuilderForDDLayout"/>" class="btn btn-success btn-sm"  style="float:right;">
							<spring:message code="anvizent.package.label.create"/>
						</a>
		            </div> 
	 </div>
	 
	  <div class='row form-group' id="ddLayoutTablesDiv">
            <div class="table-responsive">
 	             <div>
               	<table class="table table-striped table-bordered tablebg" id="ddLayoutTablesList">
				<thead>
					<tr>
					    <th class="col-xs-3"><spring:message code="anvizent.package.label.sNo"/></th>
						<th class="col-xs-3"><spring:message code="anvizent.package.label.tableName"/></th>
						<th class="col-xs-3"><spring:message code="anvizent.package.label.xref.viewtablestructure"/> </th>
						<th class="col-xs-3"><spring:message code="anvizent.package.button.viewQuery"/></th>
						<th class="col-xs-3"><spring:message code="anvizent.package.label.edit"/></th>
						<th class="col-xs-3"><spring:message code="anvizent.package.label.run"/></th>
						<th class="col-xs-3"><spring:message code="anvizent.package.label.viewResults"/> </th>
						<th class="col-xs-3"><spring:message code="anvizent.package.label.delete"/></th>
					</tr>
				</thead>
				<tbody> 
					<c:forEach  items="${ddLayoutTablesList}" var="ddLayout">
					  <tr>
					      <td><c:out value="${ddLayout.id}" /></td>
					      <td> <c:out value="${ddLayout.tableName}" /></td>
		                  <td><input type="button" id="viewTableStructure" value="<spring:message code="anvizent.package.button.viewTableStructure"/>" data-tablename="${ddLayout.tableName}" data-ddlayoutid="${ddLayout.id}" class="btn btn-primary btn-sm"></td>
		                  <td><input type="button" id="viewSelectQry" value="<spring:message code="anvizent.package.button.viewQuery"/>" data-ddlayoutid="${ddLayout.id}" class="btn btn-primary btn-sm"></td>
					      <td>
					      <c:choose>
					      	<c:when test="${ddLayout.ddlEditable}">
					      		<a href="#" class="btn btn-primary btn-xs" data-ddlayoutid="${ddLayout.id}" id="editDdlQry" title="<spring:message code="anvizent.package.label.edit"/>"><span class="glyphicon glyphicon-edit"></span></a>  
					      	</c:when>
					      	<c:otherwise>
					      		<button disabled="disabled" class="btn btn-primary btn-xs" title="This DDL created by another user. You can not edit this DDL" ><span class="glyphicon glyphicon-edit"></span></button>
					      	</c:otherwise>
					      </c:choose>
					      
					      </td>
					      <td><input type="button" id="runQry" value="<spring:message code="anvizent.package.label.run"/>" data-ddlayoutid="${ddLayout.id}" class="btn btn-primary btn-sm"> </td>
					       <td> 
					      <button class="btn btn-primary btn-sm" id="viewDDlTableResults" data-tablename="${ddLayout.tableName}" data-ddlayoutid="${ddLayout.id}" title="<spring:message code="anvizent.package.label.ViewResults"/>">
					       <spring:message code="anvizent.package.label.viewResults"/>
					      </button>
					      </td>
					      <td> 
					      	<c:choose>
					      	<c:when test="${ddLayout.ddlEditable}">
					      		<button class="btn btn-primary btn-sm" id="deleteDDlTable" data-tablename="${ddLayout.tableName}" data-ddlayoutid="${ddLayout.id}" title="<spring:message code="anvizent.package.label.delete"/>">
					      			<span class="glyphicon glyphicon-trash" aria-hidden="true" style="color:#fff;"></span>
					      		</button>  
					      	</c:when>
					      	<c:otherwise>
					      		<button disabled="disabled" class="btn btn-primary btn-sm" title="This DDL created by another user. You can not delete this DDL">
					      			<span class="glyphicon glyphicon-trash" aria-hidden="true" style="color:#fff;"></span>
					      		</button>
					      	</c:otherwise>
					      </c:choose>
					      
					      	
					      </td>
					  </tr>	
	               </c:forEach>
				</tbody>
	          </table>
	    </div>
	    </div>
	 </div>	
	 	<div class="modal fade" tabindex="-1" role="dialog" id="viewDDlTableQueryPopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog">
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewDDlTableQueryHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 800px; overflow-y: auto; overflow-x: hidden;">
										 <div class="form-group">
                                         <label for="comment"><spring:message code="anvizent.package.label.query"/>:</label>
                                         <textarea class="form-control" rows="20" id="viewDDlTableQuery"></textarea>
                                         </div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-primary btn-sm" id="validateQuery">
					        <spring:message code="anvizent.package.button.validateQuery"/> 
					        </button>
					        <button type="button" style="display:none" class="btn btn-primary btn-sm" id="queryPreview"> 
					        <spring:message code="anvizent.package.button.preview"/>
					        </button>
					         <button type="button" style="display:none" class="btn btn-primary btn-sm" id="updateDDlQuery">
					        <spring:message code="anvizent.package.button.update"/>
					        </button>
					        <button type="button" class="btn btn-default" data-dismiss="modal">
					        <spring:message code="anvizent.package.button.close"/>
					        </button>
					        <div class='col-sm-offset-1 col-sm-8 queryValidatemessageDiv'></div>
					      </div>
					    </div> 
					  </div> 
					  
				</div>	
				
      <div class="modal fade" tabindex="-1" role="dialog" id="viewTargetTableStructurePopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog" style="width:65%"> 
					    <div class="modal-content">
					      <div class="modal-header">
					      
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewTargetTableHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
					    
					      
					      
					        	<div class="table-responsive" >
									<table class="table table-striped table-bordered tablebg table-fixed" id="targetTableStructure">
										<thead>
											<tr>
												<th class="col-xs-1"><spring:message code="anvizent.package.label.sNo"/></th>
												<th class="col-xs-2"><spring:message code="anvizent.package.label.columnName"/></th>
												<th class="col-xs-1"><spring:message code="anvizent.package.label.dataType"/></th>
												<th class="col-xs-2"><spring:message code="anvizent.package.label.columnSize"/></th>
												<th class="col-xs-2"><spring:message code="anvizent.package.label.pk"/></th>
									            <th class="col-xs-2"><spring:message code="anvizent.package.label.nn"/></th>
									            <th class="col-xs-2"><spring:message code="anvizent.package.label.ai"/></th>
									           <%--  <th><spring:message code="anvizent.package.label.default"/></th> --%>
											</tr>
										</thead>
										<tbody>
											
										</tbody>
									</table>
								</div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div><!-- /.modal-content -->
					  </div><!-- /.modal-dialog -->
				</div>	
					<!-- Table Preview PopUp window -->
		<div class="modal fade" tabindex="-1" role="dialog" id="tablePreviewPopUp" data-backdrop="static" data-keyboard="false" aria-hidden='true'>
			   <div class="modal-dialog" style="width: 90%;">
				     <div class="modal-content">
					     <div class="modal-header">
					        	<button type="button" class="close"   data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
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
        <div class="modal fade" tabindex="-1" role="dialog" id="deleteDDlTableAlert" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-label="Close"> <span aria-hidden="true">&times;</span> </button>
					<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
					<h4 class="modal-title custom-modal-title"> <spring:message code="anvizent.package.label.modalHeader.deleteddltable" /> </h4>
				</div>
				<div class="modal-body">
					<p> <spring:message code="anvizent.package.message.deletePackage.areYouSureYouWantToDeleteDDLTable" /> </p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" id="confirmDeleteDDlTable"> <spring:message code="anvizent.package.button.yes" /> </button>
					<button type="button" class="btn btn-default" data-dismiss="modal"> <spring:message code="anvizent.package.button.no" /> </button>
				</div>
			</div>
		</div>
	</div>	
	
	<div class="modal fade" tabindex="-1" role="dialog" id="viewDDlTableResultsPopUp" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog" style="width:65%"> 
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewDDlTableResultsHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
					        	<div class="table-responsive" >
									<table class="table table-striped table-bordered tablebg" id="viewDDlTableResultsTable">
										<thead>
											<tr>
												<th><spring:message code="anvizent.package.label.sNo"/></th>
												<th><spring:message code="anvizent.package.label.insertedRecords"/></th>
												<th><spring:message code="anvizent.package.label.ddlsources"/></th>
												<th><spring:message code="anvizent.package.button.viewQuery"/></th>
												<th><spring:message code="anvizent.package.label.runtype"/></th>
												<th><spring:message code="anvizent.package.label.errorLog"/></th>
												<th><spring:message code="anvizent.package.label.startDate"/></th>
											</tr>
										</thead>
										<tbody>
										</tbody>
									</table>
								</div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div> 
					  </div> 
				</div>	
				
				<div class="modal fade" tabindex="-1" role="dialog" id="viewDDlTableErrorLogPopup" data-backdrop="static" data-keyboard="false">
					  <div class="modal-dialog" style="width:45%"> 
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        			<h4 class="modal-title custom-modal-title" id="viewDDlTableErrorLoHeader"></h4>
					      </div>
					      <div class="modal-body" style="max-height: 450px; overflow-y: auto; overflow-x: hidden;">
					        	<div class="table-responsive" >
					        	<div  class="errorLog"  id="errorLog"> </div>
								</div>
					      </div>
					      <div class="modal-footer">
					        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
					      </div>
					    </div> 
					  </div> 
				</div>	
</div>