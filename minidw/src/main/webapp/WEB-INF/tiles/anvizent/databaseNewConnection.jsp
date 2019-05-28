<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
      <div class="page-title-v1"><h4><spring:message code="anvizent.package.header"/></h4></div>
      <div class="dummydiv"></div>
       <ol class="breadcrumb"> </ol>
      <jsp:include page="_error.jsp"></jsp:include>
     	<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
     	
     	<div class="row form-group">
			<h4 class="alignText"><spring:message code = "anvizent.package.label.databaseConnectionDetails"/></h4>
		</div>
     	
     
     	<div class='row form-group '>
			<div class='col-sm-6'>
				<div class="alert alert-success successMessage" style="display:none;">
					 <p class="successMessageText"></p>
				</div>
				<div class="alert alert-danger message" style="display:none;">				
					 <p class="messageText"></p>
				</div>
			</div>
		</div>
     	<div tabindex="-1" role="dialog" id="createNewConnectionPopUp" data-backdrop="static" data-keyboard="false">
			  
					    	  <div class="panel panel-info">
					    	  		<div class="panel-body">
							      		<div class='row form-group'>
												<div class='col-sm-4'>
													<spring:message code="anvizent.package.label.connectionName"/>:
												</div>
												<div class='col-sm-8'>
													<input type="text" id="database_connectionName" class="form-control" data-minlength="1" data-maxlength="45">
												</div>
										</div>
										
										<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.dataSource"/>:
											</div>
											<div class='col-sm-8'>
												<select  class="form-control" id="dataSourceName">
												<option value='0'><spring:message code="anvizent.package.label.selectDataSource"/></option>
													<c:forEach items="${allDataSourceList}" var="dataSource">
														<option value="<c:out value="${dataSource.dataSourceName}"/>"><c:out value="${dataSource.dataSourceName}"/></option>
													</c:forEach>
													<option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></option>
												</select>
											</div>
										</div>
										
										<div class='row form-group dataSourceOther' style="display:none">
											<div class='col-sm-4'>
											</div>
											<div class='col-sm-8'>
												<input type="text" id="dataSourceOtherName" class="form-control" data-minlength="1" data-maxlength="45">
											</div>
										</div>
										
										<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.connectorType"/>:
											</div>
											<div class='col-sm-8'>
												<select  class="form-control" id="database_databaseType">
													<c:forEach items="${databseList}" var="database">
														<option value="<c:out value="${database.id}"/>" data-urlformat="<c:out value="${database.urlFormat}"/>" data-connectorId = "<c:out value="${database.connector_id}"/>"><c:out value="${database.name}"/></option>
													</c:forEach>
												</select>
											</div>
										</div>
										
										<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.connectionType"/>:
											</div>
											<div class='col-sm-8'>
												<select  class="form-control" id="database_connectionType">
													<option value="Direct"><spring:message code ="anvizent.package.label.direct"/></option>
												</select>
											</div>
										</div>
										
										<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.serverName"/>:
											</div>
											<div class='col-sm-8'>
												<input type="text"  class="form-control" id="database_serverName" data-minlength="1" data-maxlength="150">
												<p class="help-block"><span class='serverIpWithPort'></span></p>
											</div>
										</div>
										<div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.userName"/>:
											</div>
											<div class='col-sm-8'>
												<input type="text"  class="form-control" id="database_username" value="" data-minlength="1" data-maxlength="45">
											</div>
										</div>
										<div class='row form-group' id="IL_database_password_div">
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.password"/>:
											</div>
											<div class='col-sm-8' >
												<input type="password"  class="form-control" id="database_password" value=""  data-minlength="1" data-maxlength="100">
											</div>
										</div>
								 <div class='row form-group'>
						 			<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.dateFormat" />( <spring:message code="anvizent.package.label.optional"/>)  ) :
									 </div>
									<div class="col-sm-8">
										<input type="text" class="form-control" id="dateFormat" placeholder="Ex: YYYY-MM-DD" data-minlength="1" data-maxlength="45">
								 	</div>
							 	 </div>
							 	 
							 	 <div class='row form-group'>
											<div class='col-sm-4'>
												<spring:message code="anvizent.package.label.timeZone" /> :
											</div>
											<div class='col-sm-8'>
												<select  class="form-control timesZone" id="timesZone" >
													<c:forEach items="${timesZoneList}" var="timesZoneList">
														<option value="<c:out value="${timesZoneList.key}"/>"><c:out value="${timesZoneList.value}"/></option>
													</c:forEach>
												</select>
											
								</div>											      
					      </div>
					      <div class="">
<%-- 						        <button type="button"  id="testConnection" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.testConnection"/></button>
 --%>								<button type="button" id="saveNewConnection" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.button.saveConnection"/></button>
						        <button type="button" class="btn btn-primary btn-sm" id="editConnection" style="display:none" name='editConnection'><spring:message code="anvizent.package.button.editConnection"/></button>
								<button type="button" class="btn btn-primary btn-sm" id="updateConnection" style="display:none" name='updateConnection'><spring:message code="anvizent.package.button.updateConnection"/></button>
								<a href="/minidw/logout"> <span><button type="button" class="btn btn-default"><spring:message code="anvizent.package.button.cancel"/></button></span></a>
					      </div>
				    </div> 
			  </div> 
		</div>
     	
     	<div class="modal fade" tabindex="-1" role="dialog" id="deleteConnectionAlert" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog">
				    <div class="modal-content">
					      <div class="modal-header">
						        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
					        	<h4 class="modal-title custom-modal-title"><spring:message code="anvizent.package.button.deleteConnection"/></h4>
					      </div>
					      <div class="modal-body">
						        <p><spring:message code="anvizent.package.message.deletePackage.alltheMappingsWithTheseConnectionWillBeDeleted"/><br>
						        <spring:message code="anvizent.package.message.deletePackage.areYouSureYouWantToDeleteIlConnection"/> 
					        </p>
					      </div>
					      <div class="modal-footer">
						        <button type="button" class="btn btn-primary" id="confirmDeleteConnection"><spring:message code="anvizent.package.button.yes"/></button>
						        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.no"/></button>
					      </div>
				    </div> 
			  </div> 
		</div>
</div>      