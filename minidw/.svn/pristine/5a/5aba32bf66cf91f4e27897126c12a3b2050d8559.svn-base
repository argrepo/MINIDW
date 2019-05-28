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
	<div class="page-title-v1"><h4><spring:message code = "anvizent.package.label.queryBuilder"/></h4></div>
    <div class="dummydiv"></div>
    <ol class="breadcrumb">
	</ol>
	<div class='row form-group'>
			<h4 class="alignText"><spring:message code = "anvizent.package.label.queryBuilder"/></h4>
		</div>
	<div class='col-sm-12'>
	 
		<c:if test="${not empty customTempTables}">
			
			<form:form modelAttribute="queryBuilderForm" method="POST" id="queryBuilderForm">
				<form:hidden path="packageId"/>
				<form:hidden path="industryId"/>
				<form:hidden path="isstaging"/>
				<form:hidden path="advanced"/>
			</form:form>
		
			<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
			<input type="hidden" id="isFromWebServiceJoin" value="<c:out value="${isFromWebServiceJoin}"/>">
			<input type="hidden" id="ilId" value="<c:out value="${ilId}"/>">  
			<input type="hidden" id="webserviceconid" value="<c:out value="${webserviceconid}"/>">
			<input type="hidden" id="dlId" value="<c:out value="${dlId}"/>">
			<input type="hidden" id="targetTableNameHidden" value="<c:out value="${targetTableName}"/>">
			
			<div class="row form-group">
				<c:forEach var="tempTable" items="${customTempTables}" varStatus="loopStatus">
				<input type="hidden" class="mappingid" value="<c:out value="${tempTable.tableId}"/>">
					<div class="col-sm-12">						
							<label class="querylabel"><c:out value="${tempTable.fileName}"/> <small style="font-weight: normal;">(<c:out value="${tempTable.tableName}"/>)</small></label>
						<div class="col-sm-6" style="float:right;">							
							<input type="checkbox" name="selectAll" class="checkbox col-sm-1" style="margin:12px 0px;float: right;"/>
							<label class="querylabel checkbox-label col-sm-3" style="text-align: right;float: right;"><spring:message code = "anvizent.package.label.selectAll"/></label>
						</div>
						<select class="temp-table <c:out value="${tempTable.isPrimaryTable?'primary-table':''}"/>" multiple="multiple" style="width: 100%;" 
							data-table="<c:out value="${tempTable.tableName}"/>" data-tblalias="t<c:out value="${loopStatus.index+1}"/>">							
							<c:forEach var="column" items="${tempTable.columns}">
								<option value="<c:out value="${column.columnName}"/>"><c:out value="${column.columnName}"/></option>
							</c:forEach>
						</select>
					</div>
				</c:forEach>
			</div>
			
			
			<c:set var="tableslength" value="${fn:length(customTempTables)}"/>
			<c:if test="${tableslength >= 1}">
				<div class="row form-group" style="<c:out value="${tableslength == 1 ? 'display:none;':''}"/>">
					<div class="col-sm-12">
						<label><spring:message code = "anvizent.package.label.joins"/></label>
					</div>
					
					<div class="col-sm-12" style="padding-left:0px;padding-right:0;">
						<c:forEach begin="1" end="${tableslength}" varStatus="loop">
							<div class="row" ${loop.index == 1 ? '':'style="margin-top:5px;"'}>
								<div class="${tableslength == loop.index ? 'col-sm-12' : 'col-sm-8 col-md-7'}">
									<select class="j-tablename">
										<c:forEach var="tempTable" items="${customTempTables}" varStatus="optloop">
										<c:choose>
										<c:when test="${empty tempTable.fileName}"> 
										<option value="<c:out value="${tempTable.tableName}"/>" ${optloop.index == loop.index-1 ? "selected='selected'" : "" }><c:out value="${tempTable.tableName}"/></option>
										</c:when>
										<c:otherwise>
										<option value="${tempTable.tableName}" ${optloop.index == loop.index-1 ? "selected='selected'" : "" }><c:out value="${tempTable.fileName}"/> (<c:out value="${tempTable.tableName}"/>)</option>
										</c:otherwise>
										</c:choose>
											
										</c:forEach>
									</select>
								</div>
								<c:if test="${tableslength != loop.index}">
									<div class="col-sm-4 col-md-5">
										<select class="j-type col-sm-3"> 
											<option value="INNER JOIN"><spring:message code = "anvizent.package.label.innerJoin"/></option>
											<option value="LEFT OUTER JOIN"><spring:message code = "anvizent.package.label.leftOuterJoin"/></option>
											<option value="RIGHT OUTER JOIN"><spring:message code = "anvizent.package.label.rightOuterJoin"/></option>
										</select>
										<label style="font-weight: normal;margin-left: 15px;"><spring:message code = "anvizent.package.label.with"/></label>
									</div>
								</c:if>
							</div>
						</c:forEach>
					</div>
				</div>
			</c:if>
			<c:if test="${tableslength > 1}">
				<div class="row form-group">
					<div class="col-sm-12">
					<label><spring:message code = "anvizent.package.label.joinConditions"/></label>
					</div>
						<div class="row">
							<div class="col-sm-12" style="padding-left: 0px; padding-right: 0px;"> 
								<div class="row j-row">
									<div class="col-xs-11" style="padding-left: 0px; padding-right: 0px;">
										<div class="col-xs-3">
											<select class="j-table first" style="width: 100%;">
												<option value=''><spring:message code = "anvizent.package.label.selectTable"/></option>
												<c:forEach var="tempTable" items="${customTempTables}">
												<c:choose>
												<c:when test="${empty tempTable.fileName}">
												<option value="<c:out value="${tempTable.tableName}"/>"><c:out value="${tempTable.tableName}"/><c:out value="${tempTable.tableName}"/></option>
												</c:when>
												<c:otherwise>
												<option value="<c:out value="${tempTable.tableName}"/>"><c:out value="${tempTable.fileName}"/></option>
												</c:otherwise>
												</c:choose>
												</c:forEach>
											</select>
										</div>
										<div class="col-xs-3">
											<select class="j-column first" style="width: 100%;">
												<option value=''><spring:message code = "anvizent.package.label.selectColumn"/></option>
											</select>
										</div>
										<div class="col-xs-3">
											<select class="j-table second" style="width: 100%;">
												<option value=''><spring:message code = "anvizent.package.label.selectTable"/></option>
												<c:forEach var="tempTable" items="${customTempTables}">
												<c:choose>
												<c:when test="${empty tempTable.fileName}">
												<option value="<c:out value="${tempTable.tableName}"/>"><c:out value="${tempTable.tableName}"/></option>
												</c:when>
												<c:otherwise>
												<option value="<c:out value="${tempTable.tableName}"/>"><c:out value="${tempTable.fileName}"/></option>
												</c:otherwise>
												</c:choose>
												</c:forEach>
											</select>
										</div>
										<div class="col-xs-3">
											<select class="j-column second" style="width: 100%;">
											<option value=''><spring:message code = "anvizent.package.label.selectColumn"/></option>
											</select>
										</div>
									</div>
									<div class="col-xs-1">
									 	<button type="button" class="btn btn-primary btn-xs add-j-row">
								          <span class="glyphicon glyphicon-plus"></span>
								        </button>
									</div>
								</div>
							</div>
						</div>
					
				</div>
			</c:if>
			<div class="row form-group">
				<div class="col-sm-12">
					<label>
						<spring:message code = "anvizent.package.label.query"/>
					</label>
					<textarea rows="5" style="width:100%; resize: none;" class="form-control" id="querycontainer" spellcheck="false"><c:if test="${not empty targetTableQuery}">${targetTableQuery}</c:if></textarea>
					
				</div>
			</div>
			<div class="row form-group">
				<div class="col-sm-12">
				   <c:choose>
				   <c:when test="${isFromWebServiceJoin == true || (queryBuilderForm.advanced == true && not empty targetTableName)}">
				   		<button type="button" class="btn btn-primary btn-sm" id="saveMapping" style="display:none;margin-left: 5px;"><spring:message code = "anvizent.package.label.mapHeaders"/></button>
				   </c:when>
				   <%-- <c:when test="${queryBuilderForm.advanced == true && not empty targetTableName}">
				   		<button type="button" class="btn btn-primary btn-sm" id="saveCustomMapping" style="display:none;margin-left: 5px;"><spring:message code = "anvizent.package.label.mapHeaders"/></button>
				   </c:when> --%>
					<c:otherwise>
					<button type="button" class="btn btn-primary btn-sm" id="savebutton" style="display:none;margin-left: 5px;"><spring:message code = "anvizent.package.button.save"/></button>
					</c:otherwise>
					</c:choose>
					
					<button type="button" class="btn btn-primary btn-sm" id="validatebutton"><spring:message code = "anvizent.package.button.validate"/></button>
					<c:choose>
					<c:when test="${isFromDerivedTables == true}">
					<a class="btn btn-primary btn-sm tablebuttons"  href="<c:url value="/adt/package/derivedTable/edit/${queryBuilderForm.packageId}"/>" ><input type="button" class="btn btn-primary btn-sm" value="<spring:message code = "anvizent.package.link.back"/>"/> </a>
					</c:when>
					<c:when test="${isFromWebServiceJoin == true}">
				   <a class="btn btn-primary btn-sm tablebuttons" style="display:none"  href='<c:url value="/adt/standardpackage/addILSource/${packageId}/${dlId}/${ilId}"/>' ><input type="button" class="btn btn-primary btn-sm" value="<spring:message code = "anvizent.package.link.back"/>"/> </a>
				   </c:when>
					<c:otherwise>
					<a class="btn btn-primary btn-sm tablebuttons"  href="<c:url value="/adt/package/customPackage/edit/${queryBuilderForm.packageId}"/>" ><input type="button" class="btn btn-primary btn-sm" value="<spring:message code = "anvizent.package.link.back"/>"/> </a>
					</c:otherwise>
					</c:choose>
					
				</div>
			</div>
			
			<div class="modal fade" tabindex="-1" role="dialog" id="customTargetTablePopup" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog modal-lg">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title"><spring:message code = "anvizent.package.label.tableCreation"/></h4>
			      </div>
			      <div class="modal-body"> 
			      	<div class='row form-group' id="customTargetTableDiv">
							<label class="col-md-3 control-label" for="targetTableName"> <spring:message code = "anvizent.package.label.targetTable"/> :</label>
							<div class='col-sm-6' >
								<input type="text" id="targetTableName" class="form-control" data-minlength="1" data-maxlength="100"/>
							</div>
					</div>
			        <div class="table-responsive" style="max-height: 400px;">
						<table class="table table-striped table-bordered tablebg" id="customTargetTable">
							<thead>
								<tr>
									<th><spring:message code = "anvizent.package.label.columnName"/></th>
									<th style="width: 15%;"><spring:message code = "anvizent.package.label.dataType"/></th>
									<th style="width: 15%;"><spring:message code = "anvizent.package.label.length"/></th>
									<th class="smalltd"><spring:message code = "anvizent.package.label.pk"/></th>
									<th class="smalltd"><spring:message code = "anvizent.package.label.nn"/></th>
									<th class="smalltd"><spring:message code = "anvizent.package.label.uq"/></th>
									<th class="smalltd"><spring:message code = "anvizent.package.label.ai"/></th>
									<th class="smalltd"><spring:message code = "anvizent.package.label.default"/></th>
									
								</tr>
							</thead>
							<tbody>
								
							</tbody>
						</table>
					</div>
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-primary btn-sm" id="customCustomTable"><spring:message code = "anvizent.package.button.save"/></button>
			        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "anvizent.package.button.close"/></button>
			      </div>
			    </div>
			  </div>
			</div>
			<div class="modal fade" tabindex="-1" role="dialog" id="fileMappingWithILPopUp" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<p>
							<b><spring:message code="anvizent.package.button.mapFileHeadersAndSave" /></b>
						</p>
						<div id="deleteColumnMessageAlertMessage" class="alert alert-danger">
						   <spring:message code="anvizent.package.label.note" />
						   <spring:message code="anvizent.package.label.alertonDropColumnwhileheadersmapping"/>
						</div>
					</div>
					<div class="modal-body">
						<div class="table-responsive" style="max-height: 400px;">
							<table class="table table-striped table-bordered tablebg" id="fileMappingWithILTable">
								<thead>
									<tr>
										<th><spring:message code="anvizent.package.label.sNo" /></th>
										<th style="width: 20%;" class="iLName"></th>
										<th style="width: 15%;"><spring:message code="anvizent.package.label.dataType" /></th>
										<th style="width: 15%;"><spring:message code = "anvizent.package.label.length"/></th>
										<th class="smalltd"><spring:message code="anvizent.package.label.pk" /></th>
										<th class="smalltd"><spring:message code="anvizent.package.label.nn" /></th>
										<th class="smalltd"><spring:message code="anvizent.package.label.ai" /></th>
										<th class="originalFileName" style="width:30%;"></th>
										<th style="width: 30%;" class="smalltd"><spring:message code="anvizent.package.label.default" /></th>
										<th class="addordelete"><spring:message code = "anvizent.package.label.addOrDelete"/></th>
									</tr>
								</thead>
								<tbody>

								</tbody>
								<tfoot></tfoot>
							</table>

						</div>
					</div>
					<div class="modal-footer">
						<input type="button" class="btn btn-primary btn-sm" value="<spring:message code="anvizent.package.button.saveMapping"/>" id="saveMappingWithILForWebService" name='saveMappingWithILForWebService'/>
						<button type="button" class="btn btn-default" data-dismiss="modal"> <spring:message code="anvizent.package.button.close" /> </button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" id="messagePopUp" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog" style="width: 500px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close close-popup" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					 
						<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title"></h4>
					</div>
					<div class="modal-body">
						<div id="popUpMessage" style="text-align: center;"></div>
					</div>
					<div class="modal-footer" style="text-align: center;">
						 
						<%-- <a href='<c:url value="/adt/standardpackage/addILSource/${packageId}/${dlId}/${ilId}"/>' class="btn btn-primary btn-sm"><span class='glyphicon glyphicon-chevron-left' aria-hidden='true'></span> 	<spring:message code="anvizent.package.label.addSource" /></a> --%>
						<a href='<c:url value="/adt/standardpackage"/>' class="btn btn-primary btn-sm">
						<span class='glyphicon glyphicon-chevron-left' aria-hidden='true'></span>
						<spring:message code="anvizent.package.label.standardPackage" /></a> 
						<%-- <a href='<c:url value="/adt/package/schedule"/>' class="btn btn-primary btn-sm" ><spring:message code="anvizent.package.label.schedule" /><span class='glyphicon glyphicon-chevron-right' aria-hidden='true'></span></a> --%>
					    <a href='<c:url value="/adt/standardpackage/addILSource/${packageId}/${dlId}/${ilId}"/>' class="btn btn-primary btn-sm"> <spring:message code = "anvizent.package.button.close"/></a>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="alertForTargetMappingPopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 500px;">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        <h4 class="modal-title custom-modal-title"></h4>
				      </div>
				      <div class="modal-body">
				        	<div id="targetPopUpMessage" style="text-align: center;"></div>
				      </div>
				      <div class="modal-footer" style="text-align: center;">					        	
							<button type="button" class="btn btn-primary btn-sm" id="updateSuccess"><spring:message code="anvizent.package.button.ok"/></button>
				      </div>
			    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
	 </div>
		
		</c:if>	
	</div>
</div>