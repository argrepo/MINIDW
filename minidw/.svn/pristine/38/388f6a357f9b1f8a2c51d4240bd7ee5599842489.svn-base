<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" /> 
 
<!--  <style>
 .modal-ku {
  width: 1200px;
  margin: auto;
  height: 1200px;
}
 </style> -->
 
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.navLeftTabLink.label.templateMigration"/></h4>
 	</div>
 	<div class="dummydiv"></div>
	<ol class="breadcrumb">
	</ol>
	<jsp:include page="admin_error.jsp"></jsp:include>
	<input type="hidden" id="userId" value="${principal.userId}">
	 
	    <div class="row form-group"> 
				<div class="col-sm-2 control-label"> 
					 <spring:message code="anvizent.package.label.Template" /> : 
				</div>	
				<div class="col-sm-3">
					<label>
						<input type="radio"  name="migrateTemplateOption" value="migrate" id="migrateTemplate" /><spring:message code="anvizent.package.label.migrate" /> 
					</label> 
					<label style="padding-left: 16px;">
						<input type="radio"  name="migrateTemplateOption" value="export" id="exportTemplate" /> <spring:message code="anvizent.package.label.export" />  
					</label>
					<label style="padding-left: 16px;">
						<input type="radio"  name="migrateTemplateOption" value="import" id="importTemplate"  /> <spring:message code="anvizent.package.label.import" />  
					</label>
				</div>
	    </div>
	   
	  
	    <div class="row form-group" id ="serverConfiguration" style="display:none;"> 
				  <div class="col-sm-2" >
					<spring:message code="anvizent.package.label.SourceServer"/> :
				  </div>
				  <div class="col-sm-3">
						 <select id="sourceServer" class="form-control">
							<option value="select"><spring:message code="anvizent.package.label.Select"/> </option>
                         </select>
				  </div>
					<div class="col-sm-2">
					<spring:message code="anvizent.package.label.DestinationServer"/> :
				  </div>
					<div class="col-sm-3">
						 <select id="destinationServer"  class="form-control">
							<option value="select"><spring:message code="anvizent.package.label.Select"/></option>
                         </select>
				   </div>
	    </div>
	   <div class="row form-group" id ="clientConfiguration" style="display:none;"> 
				  <div class="col-sm-2" id='sourceClientLable' style="display:none">
				 <spring:message code="anvizent.package.label.SourceClient"/>:
				  </div>
				  <div class="col-sm-3" id='sourceClientSelectBox' style="display:none">
						 <select id="sourceClient" class="form-control">
							<option value="select"><spring:message code="anvizent.package.label.Select"/></option>
                         </select>
				  </div>
					<div class="col-sm-2" id='destinationClientLable' style="display:none">
					<spring:message code="anvizent.package.label.DestinationClient"/> :
				  </div>
					<div class="col-sm-3" id='destinationClientSelectBox' style="display:none">
						 <select id="destinationClient"  class="form-control">
							<option value="select"><spring:message code="anvizent.package.label.Select"/></option>
                         </select>
				   </div>
				   <div class="col-sm-2">
						 <input type="button" class="btn btn-sm btn-success" name="viewMetaInfo" style="display:none" value="View Meta Info" id="viewDestinastionServerClientMetaInfo"  /> 
				   </div>
			
	    </div>
	    
	    <div class="col-sm-12" id="tabsDiv" style="display:none">
				<div id="sourceServerClientTabs" style="margin-bottom:20px;">
					<ul>  
					    <li><a href="#clientSpecificUsers" ><spring:message code="anvizent.package.label.Users"/></a></li>
						<li><a href="#clientSpecificPackages"><spring:message code="anvizent.package.label.Packages"/></a></li>
						<li><a href="#clientSpecificDbConnections"><spring:message code="anvizent.package.label.DatabaseConnections"/></a></li>
						<li><a href="#clientSpecificWsConnections"><spring:message code="anvizent.package.label.WebServiceConnections"/></a></li>
						<li><a href="#clientSpecificIlJars"><spring:message code="anvizent.package.label.ClientILJars"/></a></li>
						<li><a href="#clientSpecificDlJars"><spring:message code="anvizent.package.label.ClientDLJars"/></a></li>
						<li><a href="#clientSpecificTemplates"><spring:message code="anvizent.package.label.Verticals"/></a></li>
						<li><a href="#clientSpecificTableScripts"><spring:message code="anvizent.package.label.TableScripts"/></a></li>
					</ul>
					<div id="clientSpecificPackages">
					
				      	  <div class='row form-group' id="packageTableDiv">
				            <div class="table-responsive">
				 	             <div style = "overflow:auto;max-height:300px">
				               	<table class="table table-striped table-bordered tablebg" id="packageTable">
								<thead>
									<tr>
										<th class="col-xs-3"><input type="checkbox" value="selectAll" class="packageTableSelectAll"> <spring:message code="anvizent.package.label.SelectAll"/></th>
										<th class="col-xs-3"><spring:message code="anvizent.package.label.PackageId"/></th>
										<th class="col-xs-3"> <spring:message code="anvizent.package.label.PackageName"/></th>
										<th class="col-xs-3"> <spring:message code="anvizent.package.label.PackageType"/></th>
										<th class="col-xs-3"><spring:message code="anvizent.package.label.SourceUser"/></th>
										<!-- <th class="col-xs-3">Destination User</th> -->
									</tr>
								</thead>
								<tbody> </tbody>
					          </table>
					    </div>
					    
					     <div id="selectOnePackageAlert"></div>
					    </div>
					    <div class='row form-group'>
						  <div class='col-sm-4'>
							<label class="control-label ">  <spring:message code="anvizent.package.label.DestinationUsers"/>:</label>
						 </div>
						 <div class='col-sm-4'>
							<div id="destinationUsersDiv">
							<select id='destinationUserList'  multiple="multiple"  class='form-control'>
							</select>
							</div>
						 </div>
						</div>
					    <input type="button" class="btn btn-sm btn-success" value="Migrate Packages" id="migratePackages"  />
		               </div>	
		                <div class='row form-group'>
		               	<div id="packageTableMigrationAlert"  style="display:none">
							<!-- <button type="button" class="close" data-dismiss="alert">
					        <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				   	        </button> -->
		    	           <div class="table-responsive">
					         	<div style = "overflow:auto;max-height:300px">
						        <table class="table table-striped table-bordered tablebg" id="packageMigrationStatusTable">
									<thead>
										<tr>
											<th class="col-xs-3"> <spring:message code="anvizent.package.label.Migrationstatus"/></th>
											<th class="col-xs-3"> <spring:message code="anvizent.package.label.PackageId"/></th>
										</tr>
									</thead>
									<tbody> </tbody>
						       </table>
						    </div>
						  </div>
					   </div>
		                </div>
					</div>
					
					<div id="clientSpecificUsers">
						 
						  <div class='row form-group' id="userTableDiv">
				           <div class="table-responsive">
				         	<div style = "overflow:auto;max-height:300px">
					        <table class="table table-striped table-bordered tablebg" id="userTable">
								<thead>
									<tr>
										<th class="col-xs-3"><input type="checkbox" value="selectAll" class="userTableSelectAll"> <spring:message code="anvizent.package.label.SelectAll"/></th>
										<th class="col-xs-3"><spring:message code="anvizent.package.label.UserId"/></th>
										<th class="col-xs-3"><spring:message code="anvizent.package.label.UserName"/></th>
									</tr>
								</thead>
								<tbody> </tbody>
					       </table>
					    </div>
					    <div id="selectOneUserAlert"></div>
					  </div>
					  <input type="button" class="btn btn-sm btn-success" value="Migrate Users" id="migrateUsers"  />
		             </div>	
		              <div class='row form-group'>
		              <div class="alert alert-success" id="userTableMigrationAlert" style="display:none"></div>'
		             </div>
					</div>
					
					<div id="clientSpecificDbConnections">
						 
						     <div class='row form-group' id="dbConTableDiv" >
				             <div class="table-responsive">
						 	<div style = "overflow:auto;max-height:300px">
							<table class="table table-striped table-bordered tablebg" id="dbConTable">
										<thead>
											<tr>
												<th class="col-xs-3"><input type="checkbox" value="selectAll" class="dbConTableSelectAll"><spring:message code="anvizent.package.label.SelectAll"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.ConnectionId"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.DatabaseName"/></th>
											</tr>
										</thead>
										<tbody> </tbody>
							    </table>
							    </div>
					    <div id="selectOneDbConAlert"></div>
					</div>
					<div class='row form-group'>
						  <div class='col-sm-4'>
							<label class="control-label "><spring:message code="anvizent.package.label.DestinationUsers"/> :</label>
						 </div>
						 <div class='col-sm-4'>
							<div id="destinationUsersDiv">
							<select id='destinationDbConUserList'  multiple="multiple"  class='form-control'>
							</select>
							</div>
						 </div>
						</div>
					<input type="button" class="btn btn-sm btn-success" value="Migrate Db Connections" id="migrateDbConnections"  />
		            </div>	
		            <div class='row form-group'>
		              <div class="alert alert-success" id="dbConTableMigrationAlert" style="display:none"></div>'
		             </div>
					</div>
					
					<div id="clientSpecificWsConnections">
						  <div class='row form-group' id="wsConTableDiv" >
			            	 <div class="table-responsive">
						 	<div style = "overflow:auto;max-height:300px">
							<table class="table table-striped table-bordered tablebg" id="wsConTable">
										<thead>
											<tr>
												<th class="col-xs-3"><input type="checkbox" value="selectAll" class="wsConTableSelectAll"> <spring:message code="anvizent.package.label.SelectAll"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.ConnectionId"/></th>
												<th class="col-xs-3"><spring:message code="anvizent.package.label.WebServiceName"/></th>
											</tr>
										</thead>
										<tbody> </tbody>
							    </table>
							    </div>
					    <div id="selectOneWsConAlert"></div>
					   </div>
		              <div class='row form-group'>
						  <div class='col-sm-4'>
							<label class="control-label "> <spring:message code="anvizent.package.label.DestinationUsers"/> :</label>
						 </div>
						 <div class='col-sm-4'>
							<div id="destinationUsersDiv">
							<select id='destinationWsConUserList'  multiple="multiple"  class='form-control'>
							</select>
							</div>
						 </div>
						</div>
		              <input type="button" class="btn btn-sm btn-success" value="Migrate Ws Connections" id="migrateWsConnections"  />
		              </div>
		              <div class='row form-group'>
		              <div class="alert alert-success" id="wsConTableMigrationAlert" style="display:none"></div>'
		             </div>
					</div>
					<div id="clientSpecificDlJars">
				      	  <div class='row form-group' id="specificDlJarsDiv">
			            	 <div class="table-responsive">
						 	<div style = "overflow:auto;max-height:300px">
							<table class="table table-striped table-bordered tablebg" id="specificDlJarsTable">
										<thead>
											<tr>
												<th class="col-xs-3"><input type="checkbox" value="selectAll" class="specificDlJarsTableSelectAll"> <spring:message code="anvizent.package.label.SelectAll"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.DLId"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.JobName"/></th>
												<th class="col-xs-3"><spring:message code="anvizent.package.label.JobVersion"/></th>
											</tr>
										</thead>
										<tbody> </tbody>
							    </table>
							    </div>
					    <div id="selectOneDlJarsAlert"></div>
					   </div>
					    <input type="button" class="btn btn-sm btn-success" value="Migrate Dl Jars" id="migrateDlJars"  />
		              </div>
					</div>
					<div id="clientSpecificIlJars">
				      	  <div class='row form-group' id="specificIlJarsDiv">
			            	 <div class="table-responsive">
						 	<div style = "overflow:auto;max-height:300px">
							<table class="table table-striped table-bordered tablebg" id="specificIlJarsTable">
										<thead>
											<tr>
												<th class="col-xs-3"><input type="checkbox" value="selectAll" class="specificIlJarsTableSelectAll"> <spring:message code="anvizent.package.label.SelectAll"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.ILId"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.JobName"/></th>
												<th class="col-xs-3"><spring:message code="anvizent.package.label.JobVersion"/></th>
											</tr>
										</thead>
										<tbody> </tbody>
							    </table>
							    </div>
					    <div id="selectOneIlJarsAlert"></div>
					   </div>
					   <input type="button" class="btn btn-sm btn-success" value="Migrate Il Jars" id="migrateIlJars"  />
		              </div>
					</div>
					<div id="clientSpecificTemplates">
				      	  <div class='row form-group' id="specificTemplatesDiv">
			            	 <div class="table-responsive">
						 	<div style = "overflow:auto;max-height:300px">
							<table class="table table-striped table-bordered tablebg" id="specificVerticalTable">
										<thead>
											<tr>
												<th class="col-xs-3"><input type="checkbox" value="selectAll" class="specificVerticalTableSelectAll"><spring:message code="anvizent.package.label.SelectAll"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.TemplateId"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.TemplateName"/></th>
											</tr>
										</thead>
										<tbody> </tbody>
							    </table>
							    </div>
					    <div id="selectOneVerticalAlert"></div>
					   </div>
					<!--    <div class='row form-group'>
						  <div class='col-sm-4'>
							<label class="control-label ">Destination Users :</label>
						 </div>
						 <div class='col-sm-4'>
							<div id="destinationUsersDiv">
							<select id='destinationVerticalUserList'  multiple="multiple"  class='form-control'>
							</select>
							</div>
						 </div>
						</div> -->
					   <input type="button" class="btn btn-sm btn-success" value="Migrate Verticals" id="migrateVerticals"  />
		              </div>
					</div>
					 
					<div id="clientSpecificTableScripts">
				      	  <div class='row form-group' id="specificTableScriptsDiv">
			            	 <div class="table-responsive">
						 	<div style = "overflow:auto;max-height:300px">
							<table class="table table-striped table-bordered tablebg" id="specificTableScriptsTable">
										<thead>
											<tr>
												<th class="col-xs-3"><input type="checkbox" value="selectAll" class="specificTableScriptsTableSelectAll"><spring:message code="anvizent.package.label.SelectAll"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.ScriptId"/></th>
												<th class="col-xs-3"><spring:message code="anvizent.package.label.ScriptName"/></th>
												<th class="col-xs-3"><spring:message code="anvizent.package.label.ScriptType"/></th>
											</tr>
										</thead>
										<tbody> </tbody>
							    </table>
							    </div>
					    <div id="selectOneTableScriptsAlert"></div>
					   </div>
					  <!--  <div class='row form-group'>
						  <div class='col-sm-4'>
							<label class="control-label ">Destination Users :</label>
						 </div>
						 <div class='col-sm-4'>
							<div id="destinationUsersDiv">
							<select id='destinationUserList'  multiple="multiple"  class='form-control'>
							</select>
							</div>
						 </div>
						</div> -->
					    <input type="button" class="btn btn-sm btn-success" value="Migrate Table Scripts" id="migrateTableScripts"  />
		              </div>
					</div>
					
		 		</div>
 			</div>
	 <div class="modal fade" tabindex="-1" role="dialog" id="destinationServerClientMetaInfoPopUp" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog modal-lg" style="width:1080px">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<img src="<c:url value="/resources/images/anvizent_icon.png"/>" class="anvizentLogo" />
						<h4 class="modal-title custom-modal-title">
							<spring:message code="anvizent.package.label.DestinationServerClientMetaInfo"/>
						</h4>
					</div>
					<div class="modal-body">
						<div class="col-sm-12" id="destinationTabsDiv" style="display:none">
						 <div id="destinationServerClientTabs">
							<ul>  
								<li><a href="#destinationClientSpecificPackages"><spring:message code="anvizent.package.label.Packages"/></a></li>
								<li><a href="#destinationClientSpecificUsers" ><spring:message code="anvizent.package.label.Users"/></a></li>
								<li><a href="#destinationClientSpecificDbConnections"><spring:message code="anvizent.package.label.DatabaseConnections"/></a></li>
								<li><a href="#destinationClientSpecificWsConnections"><spring:message code="anvizent.package.label.WebServiceConnections"/></a></li>
								<li><a href="#destinationClientSpecificIlJars"><spring:message code="anvizent.package.label.ClientILJars"/></a></li>
								<li><a href="#destinationClientSpecificDlJars"><spring:message code="anvizent.package.label.ClientDLJars"/></a></li>
								<li><a href="#destinationClientSpecificTemplates"><spring:message code="anvizent.package.label.Verticals"/></a></li>
								<li><a href="#destinationClientSpecificTableScripts"><spring:message code="anvizent.package.label.TableScripts"/></a></li>
							</ul>
							<div id="destinationClientSpecificPackages">
						      	  <div class='row form-group' id="destinationPackageTableDiv">
						            <div class="table-responsive">
						 	             <div style = "overflow:auto;max-height:300px">
						               	<table class="table table-striped table-bordered tablebg" id="destinationPackageTable">
										<thead>
											<tr>
												<th class="col-xs-3"><spring:message code="anvizent.package.label.PackageId"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.PackageName"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.PackageType"/></th>
												<th class="col-xs-3"> <spring:message code="anvizent.package.label.CreatedBy"/></th>
											</tr>
										</thead>
										<tbody> </tbody>
							          </table>
							    </div>
				                </div>	
							</div>
							</div>
							<div id="destinationClientSpecificUsers">
								 
								  <div class='row form-group' id="destinationUserTableDiv">
						           <div class="table-responsive">
						         	<div style = "overflow:auto;max-height:300px">
							        <table class="table table-striped table-bordered tablebg" id="destinationUserTable">
										<thead>
											<tr>
												<th class="col-xs-3"><spring:message code="anvizent.package.label.UserId"/></th>
												<th class="col-xs-3"><spring:message code="anvizent.package.label.UserName"/></th>
											</tr>
										</thead>
										<tbody> </tbody>
							       </table>
							    </div>
							  </div>
				             </div>	
								 
							</div>
							
							<div id="destinationClientSpecificDbConnections">
								 
								     <div class='row form-group' id="destinationDbConTableDiv" >
						             <div class="table-responsive">
								 	<div style = "overflow:auto;max-height:300px">
									<table class="table table-striped table-bordered tablebg" id="destinationDbConTable">
												<thead>
													<tr>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.ConnectionId"/></th>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.DatabaseName"/></th>
													</tr>
												</thead>
												<tbody> </tbody>
									    </table>
									    </div>
							</div>
				            </div>	
								 
							</div>
							
							<div id="destinationClientSpecificWsConnections">
								  <div class='row form-group' id="destinationWsConTableDiv" >
					            	 <div class="table-responsive">
								 	<div style = "overflow:auto;max-height:300px">
									<table class="table table-striped table-bordered tablebg" id="destinationWsConTable">
												<thead>
													<tr>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.ConnectionId"/></th>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.WebServiceName"/></th>
													</tr>
												</thead>
												<tbody> </tbody>
									    </table>
									    </div>
							   </div>
				              </div>	
							</div>
							<div id="destinationClientSpecificDlJars">
						      	  <div class='row form-group' id="destinationSpecificDlJarsDiv">
					            	 <div class="table-responsive">
								 	<div style = "overflow:auto;max-height:300px">
									<table class="table table-striped table-bordered tablebg" id="destinationSpecificDlJarsTable">
												<thead>
													<tr>
														<th class="col-xs-3"><spring:message code="anvizent.package.label.DLId"/></th>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.JobName"/></th>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.JobVersion"/></th>
													</tr>
												</thead>
												<tbody> </tbody>
									    </table>
									    </div>
							   </div>
				              </div>
							</div>
							<div id="destinationClientSpecificIlJars">
						      	  <div class='row form-group' id="destinationSpecificIlJarsDiv">
					            	 <div class="table-responsive">
								 	<div style = "overflow:auto;max-height:300px">
									<table class="table table-striped table-bordered tablebg" id="destinationSpecificIlJarsTable">
												<thead>
													<tr>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.ILId"/></th>
														<th class="col-xs-3"><spring:message code="anvizent.package.label.JobName"/></th>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.JobVersion"/></th>
													</tr>
												</thead>
												<tbody> </tbody>
									    </table>
									    </div>
							   </div>
				              </div>
							</div>
							<div id="destinationClientSpecificTemplates">
						      	  <div class='row form-group' id="destinationSpecificTemplatesDiv">
					            	 <div class="table-responsive">
								 	<div style = "overflow:auto;max-height:300px">
									<table class="table table-striped table-bordered tablebg" id="destinationSpecificVertivcalTable">
												<thead>
													<tr>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.VerticalId"/></th>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.VerticalName"/></th>
													</tr>
												</thead>
												<tbody> </tbody>
									    </table>
									    </div>
							   </div>
				              </div>
							</div>
							 
							<div id="destinationClientSpecificTableScripts">
						      	  <div class='row form-group' id="destinationSpecificTableScriptsDiv">
					            	 <div class="table-responsive">
								 	<div style = "overflow:auto;max-height:300px">
									<table class="table table-striped table-bordered tablebg" id="destinationSpecificTableScriptsTable">
												<thead>
													<tr>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.ScriptId"/></th>
														<th class="col-xs-3"> <spring:message code="anvizent.package.label.ScriptName"/></th>
														<th class="col-xs-3"><spring:message code="anvizent.package.label.ScriptType"/></th>
													</tr>
												</thead>
												<tbody> </tbody>
									    </table>
									    </div>
							   </div>
				              </div>
							</div>
							
				 		</div>
 			</div>
					</div>
					<div class="modal-footer">
						 <button type="button" class="btn btn-default" data-dismiss="modal"> <spring:message code="anvizent.package.button.close"/> </button>
					</div>
				</div>
			</div>
		</div>
</div>	
 