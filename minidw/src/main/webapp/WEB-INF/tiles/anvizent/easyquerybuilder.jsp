<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv" >
  	<div class='row form-group'>
   			<h4 class="alignText"><spring:message code = "anvizent.package.label.queryBuilder"/></h4>
  	</div>
  
  	<jsp:include page="_error.jsp"></jsp:include>
  
	<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	<input type="hidden" id="connectionId" value="<c:out value="${iLConnection.connectionId}"/>">
	<input type="hidden" id="protocal" value="<c:out value="${iLConnection.database.protocal}"/>">
	<input type="hidden" id="dataSourceName" value="<c:out value="${iLConnection.dataSourceName}"/>">
	<input type="hidden" id="packageId" value="<c:out value="${packageId}"/>">
	<input type="hidden" id="dlId" value="<c:out value="${dLId}"/>">
	<input type="hidden" id="ilId" value="<c:out value="${iLId}"/>">
	<input type="hidden" id="isCustom" value="<c:out value="${isCustom}"/>">
	<input type="hidden" id="dbSourceName" value="<c:out value="${dbSourceName}"/>">
	<input type="hidden" id="isDDlayout" value="<c:out value="${isDDlayout}"/>">
	<input type="hidden" id="isApisData" value="<c:out value="${isApisData}"/>">
  	<div class='row form-group'>
  	<c:if test="${not isXref}">
		<div class='col-sm-6 col-lg-6'>
        <c:choose>
          <c:when test="${isStandard}">
	            <div id="accordion-first" class="clearfix"><!-- standard start accordion -->
	               <div class="accordion" id="accordion2">
	               	<div class="accordion-group">
		               <div class="accordion-heading" style="border: 1px solid #dce1e4;background:#1096eb;">
		                  <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">
		                  <span class="glyphicon glyphicon-plus-sign"></span> <spring:message code = "anvizent.package.link.packageDetails"/>
		                  </a>
		               </div>
			           <div style="height: 0px;" id="collapseOne" class="accordion-body collapse">		                        
			                <div class="panel panel-info">
			                 <div class="panel-body">
				                 <form:form modelAttribute="standardPackageForm" method="POST" id="standardPackageForm">
				                     <div class='row form-group'>
				                        <div class='col-sm-12 col-md-6 col-xs-12 col-lg-6' >
				                            <label class="control-label "><spring:message code = "anvizent.package.label.packageId"/></label>
				                            <form:input path="packageId" class="form-control" disabled="true"/>
				                        </div>
				                        <div class='col-sm-12 col-md-6 col-xs-12 col-lg-6' >
				                            <label class="control-label "><spring:message code = "anvizent.package.label.packageName"/></label>
				                            <form:input path="packageName"  class="form-control" disabled="true"/>
				                        </div>
				                        </div>
				                        <div class='row form-group'>
				                        <div class='col-sm-12 col-md-6 col-xs-12 col-lg-6' >
				                            <label class="control-label "><spring:message code = "anvizent.package.label.moduleName"/></label>
				                            <form:input path="dLName" class="form-control" disabled="true"/>
				                        </div>
				                        <div class='col-sm-12 col-md-6 col-xs-12 col-lg-6' >
				                            <label class="control-label "><spring:message code = "anvizent.package.label.inputLayout"/></label>
				                            <form:input path="iLName" class="form-control" disabled="true"/>
				                        </div>
				                     </div>
				                </form:form>
			                </div>
			               </div>
			         </div>
	                </div>
	             </div>
	        	</div><!-- end accordion -->
        </c:when>
        <c:otherwise>
        <c:choose>
        <c:when test="${isDDlayout == false && isApisData == false}">
            <div id="accordion-first" class="clearfix"> <!-- custom start accordion -->
                 <div class="accordion" id="accordion2">
	                 <div class="accordion-group">
		                 <div class="accordion-heading" style="border: 1px solid #dce1e4;background:#1096eb;">
		                    <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">
		                       <span class="glyphicon glyphicon-plus-sign"></span> <spring:message code = "anvizent.package.link.packageDetails"/>
		                    </a>
		                  </div>
		                  <div style="height: 0px;" id="collapseOne" class="accordion-body collapse">		                   
			                  <div class="panel panel-info" >
			                  <div class="panel-body">
				                  <form:form modelAttribute="customPackageForm" method="POST" id="customPackageForm">
				                    <div class="row form-group">
				                        <div class='col-sm-12 col-md-6 col-xs-12 col-lg-6' >
				                            <label class="control-label "><spring:message code = "anvizent.package.label.packageId"/></label>
				                            <form:input path="packageId" class="form-control" disabled="true"/>
				                        </div>
				                        <div class='col-sm-12 col-md-6 col-xs-12 col-lg-6' >
				                            <label class="control-label ">
				                                <spring:message code="anvizent.package.label.packageName"/></label>
				                            	<form:input path="packageName" class="form-control" disabled="true"/>
				                        </div>                        
				                    </div>
				                  </form:form>
			                  </div>
			                  </div>
		                  </div>
	                  </div>
                  </div>
             </div><!-- end accordion -->
             </c:when>
             </c:choose>
        </c:otherwise>
        </c:choose>
        </div>
         </c:if> 
       
         <c:choose>
         <c:when test="${isXref}">
         <div class='col-sm-12 col-lg-12'>
              <div id="accordion-first" class="clearfix"><!-- **** start accordion -->
                 <div class="accordion" id="accordion3">
                  <div class="accordion-group">
                  <div class="accordion-heading" style="border: 1px solid #dce1e4;background:#1096eb;">
                       <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion3" href="#collapseTwo" >
                       <span class="glyphicon glyphicon-plus-sign"></span> <spring:message code="anvizent.package.label.databaseConnectionDetails"/>
                       </a>
                   </div>
                   <div style="height: 0px;" id="collapseTwo" class="accordion-body collapse">
                  <div class="panel panel-info" id="databaseConnectionPanel">
                  <div class="panel-body">
                    <div class='row form-group '>
                        <div class='col-sm-6 col-md-6 col-lg-6 col-xs-12' >
				               <label class="control-label "><spring:message code="anvizent.package.label.connectionName"/>:</label>
				                  <input type="text" id="IL_database_connectionName" class="form-control" disabled="disabled" value="<c:out value="${iLConnection.connectionName}"/>"  data-minlength="1" data-maxlength="45">
				        </div>
                        <div class='col-sm-6 col-md-6 col-lg-6 col-xs-12'>
                            <label class="control-label "><spring:message code="anvizent.package.label.connectorType"/>:</label>
                             <c:forEach items="${databseList}" var="database">
                                 <c:if test="${database.name == iLConnection.database.name}">
                                     <input type="text" id="IL_database_databaseType" class="form-control" disabled="disabled" value="<c:out value="${database.name}"/>">
                                     <input type="hidden" id="IL_database_databaseTypeId" class="form-control" value="<c:out value="${database.id}"/>"> >
                                     <input type="hidden" id="connector_id" class="form-control"  value="<c:out value="${iLConnection.database.connector_id}"/>">
                                     <input type="hidden" id="dbProtocal" class="form-control"  value="<c:out value="${iLConnection.database.protocal}"/>">
                                 </c:if>
                             </c:forEach>
                         </div>
                    </div>
                    <div class='row form-group '>
                     <div class='col-sm-6 col-md-6 col-lg-6 col-xs-12' >
				               <label class="control-label "> <spring:message code="anvizent.package.label.connectionType"/>:</label>
				                <input type="text" id="IL_database_connectionType" class="form-control" disabled="disabled" value="<spring:message code = "anvizent.package.label.direct"/>">
				        </div>
                        <div class='col-sm-6 col-md-6 col-lg-6 col-xs-12' >
                        <label class="control-label "> <spring:message code="anvizent.package.label.serverName"/>:</label>
				              <input type="text" class="form-control" id="IL_database_serverName" disabled="disabled" value="<c:out value="${iLConnection.server}"/>"  data-minlength="1" data-maxlength="150">
                             <p class="help-block"><em class='serverIpWithPort'></em></p>
				        </div>
                       
                    </div>
                     <div class='row form-group '>
                      	<div class='col-sm-6 col-md-6 col-lg-6 col-xs-12' >
				               <label class="control-label "> <spring:message code="anvizent.package.label.userName"/>:</label>
				                <input type="text" class="form-control" id="IL_database_username" disabled="disabled" value="<c:out value="${iLConnection.username}"/>"  data-minlength="1" data-maxlength="45">
				        </div>
				        <div class='col-sm-6 col-md-6 col-lg-6 col-xs-12'>
								<label class='control-label"'>
									<spring:message code="anvizent.package.label.dataSource"/>:
								</label>
								<input type="text" class="form-control" id="dataSourceName" disabled="disabled" value="<c:out value="${iLConnection.dataSourceName}"/>"  data-minlength="1" data-maxlength="150">
						</div>
                    </div>
               </div>
               </div>
               </div>
               </div>
               </div>
               </div><!-- end accordion -->   
               </div> 
         
         </c:when>
         <c:otherwise>
         <c:choose>
         <c:when test="${isDDlayout == false && isApisData == false}">
         <div class='col-sm-6 col-lg-6'>
              <div id="accordion-first" class="clearfix"><!-- abc start accordion -->
                 <div class="accordion" id="accordion3">
                  <div class="accordion-group">
                  <div class="accordion-heading" style="border: 1px solid #dce1e4;background:#1096eb;">
                       <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion3" href="#collapseTwo">
                       <span class="glyphicon glyphicon-plus-sign"></span> <spring:message code="anvizent.package.label.databaseConnectionDetails"/>
                       </a>
                   </div>
                   <div style="height: 0px;" id="collapseTwo" class="accordion-body collapse">
                  <div class="panel panel-info" id="databaseConnectionPanel">
                  <div class="panel-body">
                    <div class='row form-group '>
                        <div class='col-sm-12 col-md-6 col-xs-12 col-lg-6' >
				               <label class="control-label "><spring:message code="anvizent.package.label.connectionName"/>:</label>
				                  <input type="text" id="IL_database_connectionName" class="form-control" disabled="disabled" value="<c:out value="${iLConnection.connectionName}"/>">
				        </div>
                        <div class='col-sm-12 col-md-6 col-xs-12 col-lg-6'>
                            <label class="control-label "><spring:message code="anvizent.package.label.connectorType"/>:</label>
                             <c:forEach items="${databseList}" var="database">
                                 <c:if test="${database.name == iLConnection.database.name}">
                                     <input type="text" id="IL_database_databaseType" class="form-control" disabled="disabled" value="<c:out value="${database.name}"/>">
                                     <input type="hidden" id="IL_database_databaseTypeId" class="form-control" value="<c:out value="${database.id}"/>">
                                     <input type="hidden" id="connector_id" class="form-control" value="<c:out value="${iLConnection.database.connector_id}"/>">
                                     <input type="hidden" id="dbProtocal" class="form-control" value="<c:out value="${iLConnection.database.protocal}"/>">
                                 </c:if>
                             </c:forEach>
                         </div>
                    </div>
                    <div class='row form-group '>
                     <div class='col-sm-12 col-md-6 col-xs-12 col-lg-6' >
				               <label class="control-label "> <spring:message code="anvizent.package.label.connectionType"/>:</label>
				                <input type="text" id="IL_database_connectionType" class="form-control" disabled="disabled" value="Direct">
				        </div>
                        <div class='col-sm-12 col-md-6 col-xs-12 col-lg-6' >
                        <label class="control-label "> <spring:message code="anvizent.package.label.serverName"/>:</label>
				              <input type="text" class="form-control" id="IL_database_serverName" disabled="disabled" value="<c:out value="${iLConnection.server}"/>"  >
                             <p class="help-block"><em class='serverIpWithPort'></em></p>
				        </div>
                       
                    </div>
                     <c:if test="${iLConnection.database.protocal != 'ucanaccess'}">
                    	<div class='row form-group '>
	                      	<div class='col-sm-12 col-md-6 col-xs-12 col-lg-6' >
					               <label class="control-label "> <spring:message code="anvizent.package.label.userName"/>:</label>
					               <input type="text" class="form-control" id="IL_database_username" disabled="disabled" value="<c:out value="${iLConnection.username}"/>"  data-minlength="1" data-maxlength="45">
					        </div>
					        <div class='col-sm-12 col-md-6 col-xs-12 col-lg-6'>
								<label class='control-label"'>
									<spring:message code="anvizent.package.label.dataSource"/>:
								</label>
								<input type="text" class="form-control" id="dataSourceName" disabled="disabled" value="<c:out value="${iLConnection.dataSourceName}"/>"  data-minlength="1" data-maxlength="150">
							</div>
	                    </div>
                    </c:if>
               </div>
               </div>
               </div>
               </div>
               </div>
               </div><!-- end accordion -->   
               </div> 
               </c:when>
               <c:when test="${isDDlayout == true || isApisData == true}">
               <div class='col-sm-12 col-lg-12'>
              <div id="accordion-first" class="clearfix"><!-- abc start accordion -->
                 <div class="accordion" id="accordion3">
                  <div class="accordion-group">
                  <div class="accordion-heading" style="border: 1px solid #dce1e4;background:#1096eb;">
                       <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion3" href="#collapseTwo">
                       <span class="glyphicon glyphicon-plus-sign"></span> <spring:message code="anvizent.package.label.databaseConnectionDetails"/>
                       </a>
                   </div>
                   <div style="height: 0px;" id="collapseTwo" class="accordion-body collapse">
                  <div class="panel panel-info" id="databaseConnectionPanel">
                  <div class="panel-body">
                    <div class='row form-group '>
                        <div class='col-sm-6 col-md-6 col-lg-6 col-xs-12' >
				               <label class="control-label "><spring:message code="anvizent.package.label.connectionName"/>:</label>
				               <input type="text" id="IL_database_connectionName" class="form-control" disabled="disabled" value="<c:out value="${iLConnection.connectionName}"/>">
				               <input type="hidden" id="connector_id" class="form-control"  value="<c:out value="${iLConnection.database.connector_id}"/>">
				               <input type="hidden" id="dbSchemaName" class="form-control"  value="<c:out value="${iLConnection.database.schema}"/>">
				               <input type="hidden" id="dbProtocal" class="form-control"  value="<c:out value="${iLConnection.database.protocal}"/>">
				        </div>
				         <div class='col-sm-6 col-md-6 col-lg-6 col-xs-12' >
				               <label class="control-label "> <spring:message code="anvizent.package.label.connectionType"/>:</label>
				                <input type="text" id="IL_database_connectionType" class="form-control" disabled="disabled" value="Direct">
				        </div>
                    </div>
                    <div class='row form-group '>
                        <div class='col-sm-6 col-md-6 col-lg-6 col-xs-12' >
                        <label class="control-label "> <spring:message code="anvizent.package.label.serverName"/>:</label>
				              <input type="text" class="form-control" id="IL_database_serverName" disabled="disabled" value="<c:out value="${iLConnection.server}"/>"  >
                             <p class="help-block"><em class='serverIpWithPort'></em></p>
				        </div>
                        <c:if test="${iLConnection.database.protocal != ''}">
                    	<div class='row form-group '>
	                      	<div class='col-sm-6 col-md-6 col-lg-6 col-xs-12' >
					               <label class="control-label "> <spring:message code="anvizent.package.label.userName"/>:</label>
					               <input type="text" class="form-control" id="IL_database_username" disabled="disabled" value="<c:out value="${iLConnection.username}"/>"  data-minlength="1" data-maxlength="45">
					        </div>
	                    </div>
                    </c:if>
                    </div>
                    
               </div>
               </div>
               </div>
               </div>
               </div>
               </div><!-- end accordion -->   
               </div> 
               
               </c:when>
               </c:choose>
         </c:otherwise>
         </c:choose>
              
        </div>
         
        <div class='row form-group existingSchemas'>
			 
			<div class='col-sm-2 col-md-1 col-xs-3 col-lg-1'>	
			 <label><spring:message code = "anvizent.package.label.schemas"/></label>	
			 </div>
			<div class='col-sm-4 col-md-4 col-xs-5 col-lg-4'>
				<select id="existingSchemaList" multiple="multiple" >
				
						<c:if test="${empty schemaName}">
						<c:forEach var="schema" items="${iLConnection.database.schemas}">
					  		<option   value="${schema}"  title="${schema}"><c:out value="${schema}"/></option>
					  	</c:forEach>
					</c:if> 
					<c:if test="${not empty schemaName}">
						<c:forEach var="schema" items="${iLConnection.database.schemas}">
							<c:if test="${schemaName == schema}">
								<option  value="<c:out value="${schema}"/>" title="<c:out value="${schema}"/> " selected><c:out value="${schema}"/></option>
							</c:if>
							<c:if test="${schemaName != schema}">
								<option  value="<c:out value="${schema}"/>" title="<c:out value="${schema}"/> " ><c:out value="${schema}"/></option>
							</c:if>					  		
					  	</c:forEach>
					</c:if>			
									 
				</select>
				</div>	
				<div class='col-sm-3 col-xs-4 col-md-2'>
				<input type="button" id="existingSchemaListGo" class="btn btn-primary btn-sm" style="font-weight:bold" value="<spring:message code = "anvizent.package.label.go"/>" title="<spring:message code = "anvizent.package.label.go"/>"">&nbsp;
				<button  class="btn btn-danger btn-sm" id="uncheckExistingSchemaLists" title="<spring:message code = "anvizent.package.button.reset"/>"><span class="glyphicon glyphicon-repeat"></span></button>
				</div>
				 							
											
		</div>
        
        <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4" style="">					          		 
			<div class="allTableListDiv" style='border:1px solid #ccc;display:none;height:604px;border-radius: 4px;' data-role="page">
			
			</div> 												
	    </div>
	    
	    
	     <div class="col-xs-12 col-sm-8 col-md-8 col-lg-8 panel panel-info tableWithColumns" style="display:none;padding:0px;height:604px;">
	    	<div class="panel-heading"><spring:message code = "anvizent.package.label.resultTableWithColumns"/></div>
			<div class="panel-body" style='max-height: 560px;overflow: auto;height:560px;'>
				<div class="addedColumnsDivText"><spring:message code = "anvizent.package.label.addTablesAndColumnsFromEntitiesAndAttributes"/></div>
		    	<div class="addedColumnsDiv table-responsive"></div>
		    </div>
	    </div>
	    
	    <div class='col-xs-12' style="padding:0px;">
	    	<div class="row">
	    	<div class="col-sm-5 col-md-5 col-xs-12" >
		    	<div class="panel panel-info joinsBlock" style="display:none;">
		    		<div class="panel-heading"><spring:message code = "anvizent.package.label.joinTables"/></div>		    		 
			    	<div class="joinDiv table-responsive" style="height: 100px;overflow-y: auto;"></div>			    	 
				</div>
			</div>
			<div class="col-sm-7 col-md-7 col-xs-12" >
				<div class="panel panel-info joinsBlock" style="display:none;">
		    		<div class="panel-heading"><spring:message code = "anvizent.package.label.joinConditions"/></div>		    							 			    	 				    	
					<div class="joinConditionsDiv table-responsive"></div>								    	 			     
				</div>	
			</div>
			</div>		    						  
      	</div>
      			
      	<div class="col-sm-7 col-md-7 col-xs-12 whereConditionsDiv"> 
	    	
	    </div>
	    <div class="col-sm-5 col-md-5 col-xs-12 orderByConditionsDiv"> 
	    	
	    </div>	
	    
        <div class="col-sm-7 col-md-7 col-xs-12 calculatedColumnDiv" > 
	    	
	    </div>	
      	<div class='row form-group queryholder' style='display:none;'>
	      	<div class='col-sm-6 hidden' id= "il_incremental_update_div"  style=''>
					<div class="checkbox">
					    <label>
					      <input type="checkbox" id='il_incremental_update'> <b><spring:message code = "anvizent.package.label.incrementalUpdate"/></b>
					    </label>
			 		</div>
			</div>
       		<div class="col-sm-12 queryScriptDiv" style="max-height: 430px;overflow-y: auto;"> 
		    	<label><spring:message code = "anvizent.package.label.query"/>:</label>
		    	<c:if test="${empty defaultQuery}">
		    		<textarea class="form-control" rows="6" id="queryScript" placeholder="<spring:message code = "anvizent.package.label.query"/>" readonly="readonly"></textarea>
		    	</c:if>
		    	<c:if test="${not empty defaultQuery}">
		    		<textarea class="form-control" rows="6" id="queryScript" placeholder="<spring:message code = "anvizent.package.label.query"/>" readonly="readonly"><c:out value="${defaultQuery}"/></textarea>
		    	</c:if>		    	
		    	<input type="hidden" id="validateQuery">
		    </div>
	    </div>
        
		<div class='col-sm-6'>
			<input type="button" value='<spring:message code = "anvizent.package.button.validateQuery"/>' id='checkQuerySyntax' class="btn btn-primary btn-sm" style='display:none;'/>			
		    <input type="button" value='<spring:message code = "anvizent.package.button.preview"/>' id='checkTablePreview' class="btn btn-primary btn-sm" data-target='#tablePreviewPopUp' style='display:none;'/>
		    <input type="button" value="<spring:message code = "anvizent.package.button.save"/>" id="saveILConnectionMapping" class="btn btn-primary btn-sm" style='display:none;'>
		    <input type="button" value='<spring:message code = "anvizent.package.button.enableQuery"/>' id='enableQuery' class="btn btn-primary btn-sm" style='display:none;'/>
		    <input type="button" value='<spring:message code = "anvizent.package.button.disableQuery"/>' id='disableQuery' class="btn btn-primary btn-sm" style='display:none;'/>
		   <c:choose>
	          <c:when test="${isStandard}">
			    	<a href='<c:url value="/adt/standardpackage/addILSource/${packageId}/${dLId}/${iLId}"/>' class="btn btn-primary btn-sm back back_btn"><spring:message code = "anvizent.package.link.back"/></a>
			  </c:when>
			  <c:otherwise>
			   		<c:choose>
		               <c:when test="${isCustom}">
		                   <a href='<c:url value="/adt/package/customPackage/edit/${packageId}"/>' class="btn btn-primary btn-sm back"><spring:message code = "anvizent.package.link.back"/></a>
		               </c:when>
		               <c:when test="${isDerived}">
		                   <a href='<c:url value="/adt/package/derivedTable/edit/${packageId}"/>' class="btn btn-primary btn-sm back"><spring:message code = "anvizent.package.link.back"/></a>
		               </c:when>
		                <c:when test="${isXref}">
		                    <a href='<c:url value="/adv/package/xReferenceIL/${iLId}"/>' class="btn btn-primary btn-sm back"><spring:message code="anvizent.package.link.back"/></a>
		                </c:when>
		                 <c:when test="${isDDlayout}">
		                    <a href='<c:url value="/adt/package/ddLayout"/>' class="btn btn-primary btn-sm back"><spring:message code="anvizent.package.link.back"/></a>
		                </c:when> 
		                <c:when test="${isApisData}">
		                    <a href='<c:url value="/adt/package/apisDataInfo"/>' class="btn btn-primary btn-sm back"><spring:message code="anvizent.package.link.back"/></a>
		                </c:when> 
		           </c:choose>
			  </c:otherwise>
		   </c:choose>
		</div>
		<div class='col-sm-offset-1 col-sm-8 queryValidatemessageDiv'></div>  
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
 
 		<div class="modal fade" tabindex="-1" role="dialog" id="existingSchemaErrorAlerts" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 65%;">
			    <div class="modal-content">
				      <div class="modal-header">
				        	<button type="button" class="close" data-dismiss="modal"  ><span aria-hidden="true">&times;</span></button>
				        	<img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
				        	<h4 class="modal-title custom-modal-title"></h4>
				      </div>
				      <div class="modal-body">
				           
				      </div>
				      <div class="modal-footer">
				        	<button type="button" class="btn btn-primary" data-dismiss="modal" ><spring:message code="anvizent.package.button.ok"/></button>
				        	<button type="button" class="btn btn-default" data-dismiss="modal" ><spring:message code="anvizent.package.button.close"/></button>
				      </div>
			    </div> 
		  </div> 
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="messagePopUp" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 500px;">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close close-popup" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        <h4 class="modal-title custom-modal-title"></h4>
				      </div>
				      <div class="modal-body">
				        	<div id="popUpMessage" style="text-align: center;"></div>
				      </div>
				      <div class="modal-footer" style="text-align: center;">	
				       <c:choose>
				        <c:when test="${isStandard}">				        	
					        <a href='<c:url value="/adt/standardpackage/addILSource/${packageId}/${dLId}/${iLId}"/>' class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.ok"/></a> 
				      	</c:when>
				      	 <c:when test="${isXref}">
		                         <a href='<c:url value="/adv/package/xReferenceIL/${iLId}"/>' class="btn btn-primary btn-sm back">OK</a>
		                 </c:when>
		                 </c:choose>
				      </div>
			    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
			
		<div class="modal fade" tabindex="-1" role="dialog" id="messagePopUpForCustomPackage" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 500px;">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close close-popup" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        <h4 class="modal-title custom-modal-title"></h4>
				      </div>
				      <div class="modal-body">
				        	<div id="popUpMessageForCustomPackage" style="text-align: center;"></div>
				      </div>
				      <div class="modal-footer" style="text-align: center;">					        	
					        <c:choose>					
								<c:when test="${isCustom}">
									<a href='<c:url value="/adt/package/customPackage/edit/${packageId}"/>' class="btn btn-primary btn-sm back"><spring:message code="anvizent.package.link.ok"/></a>
								</c:when>
								<c:otherwise>
									<a href='<c:url value="/adt/package/derivedTable/edit/${packageId}"/>' class="btn btn-primary btn-sm back"><spring:message code="anvizent.package.link.ok"/></a>
								</c:otherwise>	
							</c:choose>
							<button type="button" class="btn btn-default closeButton" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
				      </div>
			    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
		 <div class="modal fade" tabindex="-1" role="dialog" id="resetSchemas" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title">
							<spring:message code="anvizent.package.label.modalHeader.resetschemas" />
						</h4>
					</div>
					<div class="modal-body">
						<p>
							<spring:message code="anvizent.package.message.deleteSource.areYouSureYouWantToResetSchemas" />
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-primary" id="confirmResetSchemas">
							<spring:message code="anvizent.package.button.yes" />
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							<spring:message code="anvizent.package.button.no" />
						</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
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
								<input type="text" id="targetTableName" class="form-control" data-minlength="1" data-maxlength="64"/>
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
								<%-- 	<th class="smalltd"><spring:message code = "anvizent.package.label.default"/></th> --%>
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
					
		<div class="modal fade" tabindex="-1" role="dialog" id="messagePopUpDDlTable" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 500px;">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close close-popup" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        <h4 class="modal-title custom-modal-title"></h4>
				      </div>
				      <div class="modal-body">
				        	<div id="popUpMessageForDDlTable" style="text-align: center;"></div>
				      </div>
				      <div class="modal-footer" style="text-align: center;">					        	
					        <c:choose>					
								<c:when test="${isDDlayout}">
									<a href='<c:url value="/adt/package/ddLayout"/>' class="btn btn-primary btn-sm"><spring:message code="anvizent.package.link.ok"/></a>
								</c:when>
							</c:choose>
				      </div>
			    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="customApisDataPopUp" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog modal-lg">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title">Apis Data</h4>
			      </div>
			      <div class="modal-body"> 
			      	<div class='row form-group'>
							<label class="col-md-3 control-label" for="apiName"> <spring:message code = "anvizent.package.label.apiName"/></label>
							<div class='col-sm-6' >
								<input type="text" id="apiName" class="form-control" name="apiName" data-minlength="1" data-maxlength="64"/>
							</div>
					</div>
					
					<div class='row form-group'>
							<label class="col-md-3 control-label" for="endPointUrl"><spring:message code = "anvizent.package.label.endPointUrl"/></label>
							<div class='col-sm-6' >
								<input type="text" id="endPointUrl" name = "endPointUrl" class="form-control"/>
							</div>
					</div>
					
					<div class='row form-group'>
							<label class="col-md-3 control-label" for="apiDescription"><spring:message code = "anvizent.package.label.apiDescription"/></label>
							<div class='col-sm-6' >
								<textarea rows="4" id="apiDescription" name = "apiDescription" class="form-control"></textarea>
							</div>
					</div>
					
					<div class='row form-group'>
							<label class="col-md-3 control-label" for="methodType"> Method Type :</label>
							<div class='col-sm-6 method_type' >
								<label class="radio-inline"><input type="radio" name="methodType" value="GET">GET</label>  
								<label class="radio-inline"><input type="radio" name="methodType" value="POST" >POST </label>
								<!-- <label class="radio-inline"><input type="radio" name="methodType" value="PUT" >PUT</label> -->
							</div>
					</div>
					
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-primary btn-sm" id="customApisData"><spring:message code = "anvizent.package.button.save"/></button>
			        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "anvizent.package.button.close"/></button>
			      </div>
			    </div>
			  </div>
			</div>
			
						
		<div class="modal fade" tabindex="-1" role="dialog" id="messagePopUpApisData" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width: 500px;">
			    <div class="modal-content">
				      <div class="modal-header">
					        <button type="button" class="close close-popup" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        <h4 class="modal-title custom-modal-title"></h4>
				      </div>
				      <div class="modal-body">
				        	<div id="popUpMessageForApisData" style="text-align: center;"></div>
				      </div>
				      <div class="modal-footer" style="text-align: center;">					        	
					        <c:choose>					
								<c:when test="${isApisData}">
									<a href='<c:url value="/adt/package/apisDataInfo"/>' class="btn btn-primary btn-sm"><spring:message code="anvizent.package.link.ok"/></a>
								</c:when>
							</c:choose>
				      </div>
			    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
</div> 
