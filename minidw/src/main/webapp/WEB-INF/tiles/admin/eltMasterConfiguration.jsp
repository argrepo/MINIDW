<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.package.label.masterConfiguration"/></h4>
 	</div>

 	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	<div class="col-md-12 message-class"></div>
	<div class="col-sm-12">
				
	<div id="masterConfigTable">
		<div class='row form-group'>
			<a style="float:right;margin-right: 1.5em;" class="btn btn-sm btn-success addMasterInfoBtn"><spring:message code="anvizent.package.label.create"/></a>
		</div>
		<div class="row form-group tblConfig">
			<div class="table-responsive">
				<table class="table table-striped table-bordered tablebg"
					id="configTagsTbl">
					<thead>
						<tr>
							<th><spring:message code="anvizent.package.label.Id"/></th>
							<th> <spring:message code="anvizent.package.label.name"/></th>
							<th> <spring:message code="anvizent.package.label.sparkPath"/></th>
							<th><spring:message code="anvizent.package.label.eltClassName"/></th>
							<th><spring:message code="anvizent.package.label.eltJobPath"/></th>
							<th><spring:message code="anvizent.package.label.default"/></th>
							<th><spring:message code="anvizent.package.label.Edit"/></th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${eltMasterConfigurationList}" var="eltmaster">
						<tr class="${eltmaster.masterDefault?'active':'' }">
							<td>${eltmaster.id}</td>
							<td>${eltmaster.name}</td>
							<td>${eltmaster.sparkJobPath}</td>
							<td>${eltmaster.eltClassPath}</td>
							<td>${eltmaster.eltLibraryPath}</td>
							<td>${eltmaster.masterDefault ? 'Yes': 'No'}</td>
							<td>
								<button class="btn btn-primary btn-sm tablebuttons masterById" value="${eltmaster.id}" data-tagid="${eltmaster.id}" title="<spring:message code="anvizent.package.label.edit"/>" >
										<i class="fa fa-pencil" aria-hidden="true"></i>
								</button>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	
	<div class="row form-group createMasterDiv">
		<input type="hidden" id="selectedId">
			<div class="panel panel-default">
				<div class="panel-heading">  <spring:message code="anvizent.package.label.masterConfiguration"/> </div>
					<div class="panel-body">
							<div class='row form-group'>
									<div class='col-sm-2'>
										 <spring:message code="anvizent.package.label.name"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="name" name="name" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
							</div>
					
							<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.sparkPath"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="sparkJobPath" name="sparkJobPath" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
							</div>
							
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.eltClassName"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="eltClassPath" name="eltClassPath" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
							</div>
							
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.eltJobPath"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="eltLibraryPath" name="eltLibraryPath" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
							</div>
							
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.sparkSubmitMode"/>
									</div>
									<div class='col-sm-5'>
										<select class="form-control" name="sparkSubmitMode" id="sparkSubmitMode">
												<option value='0'><spring:message code="anvizent.package.label.select"/></option>
												<option value='local'><spring:message code="anvizent.package.label.local"/></option>
												<option value='yarn'><spring:message code="anvizent.package.label.yarn"/></option>
												<option value='standalone'><spring:message code="anvizent.package.label.standalone"/></option>
												<option value='mesos'><spring:message code="anvizent.package.label.mesos"/></option>
												<option value='kubernetes'><spring:message code="anvizent.package.label.kubernetes"/></option>
										</select> 
									</div>
							</div>
							
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.master"/>
									</div>
									<div class='col-sm-2'>
										<input type="text" id="master" name="master" readonly="readonly" class="form-control col-" data-minlength="1" data-maxlength="45">
									</div>
									<div class='col-sm-2 hostAndPortDivs'>
										<input type="text" id="host" name="host" placeholder="<spring:message code="anvizent.package.label.host"/>" class="form-control" data-minlength="1" data-maxlength="255">
									</div>
									<div class='col-sm-2 hostAndPortDivs'>
										 <input type="number" id="port" name="port" placeholder="<spring:message code="anvizent.package.label.port"/>" class="form-control">
									</div>
							</div>
							
							<div class='row form-group masterDeployMode hidden'>
									<div class='col-sm-2'>
									<spring:message code="anvizent.package.label.deployMode"/>
									</div>
									<div class='col-sm-5'> 
										<select class="form-control" name="deployMode" id="deployMode">
												<option value='0'><spring:message code="anvizent.package.label.select"/></option>
												<option id="deplymentModeClient" value='client'><spring:message code="anvizent.package.label.client"/></option>
												<option value='cluster'><spring:message code="anvizent.package.label.cluster"/></option>
										</select> 
									</div>
							</div>
							
							
							<div class='row form-group clusterMode'>
								<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.userName"/>
									</div>
									<div class='col-sm-5'>
										<input type="text" id="userName" name="userName" class="form-control">
									</div>
							</div>
							
							<div class='row form-group clusterMode'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.authenticationType"/>
									</div>
									<div class='col-sm-5'>
										<label class="radio-inline"><input type="radio" name="authenticationType" value="password"><spring:message code="anvizent.package.label.password"/></label>
										<label class="radio-inline"><input type="radio" name="authenticationType" value="ppkfile"><spring:message code="anvizent.package.label.ppkFile"/></label>
									</div>
									
							</div>
							
							
							
							<div class='row form-group authTypePassword clusterMode hidden'>
										<div class='col-sm-2'></div>
										<div class='col-sm-5'>
												<input type="password" id="password" name="password" class="form-control" data-minlength="1" data-maxlength="45">
										</div>
										
							</div>
							
							<div class='row form-group authTypePpkFile clusterMode hidden'> 
				   					 	<div class='col-sm-2 downloadPPk'>
						   					 	
										</div>
										<div class='col-sm-5'>
					 						<input type="text" id="ppkFile" name="ppkFile" class="form-control" data-minlength="1">
					 					</div>
			      			</div>
							
							
							<div class='row form-group hidden'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.sparkMaster"/>
									</div>
									<div class='col-sm-5'>
										<select class="form-control sparkMaster" name="sparkMaster">
												<option value='0'><spring:message code="anvizent.package.label.select"/></option>
												<option value='yarn'><spring:message code="anvizent.package.label.yarn"/></option>
												<option value='local[*]'><spring:message code="anvizent.package.label.local"/></option>
										</select> 
									</div>
							</div>
							
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.jobSubmitModeType"/>
									</div>
									<div class='col-sm-5'>
										<select class="form-control jobSubmitMode" name="jobSubmitMode">
												<option value='0'><spring:message code="anvizent.package.label.select"/></option>
												<option value='java'><spring:message code="anvizent.package.label.java"/></option>
												<option value='aurora'><spring:message code="anvizent.package.label.aurora"/></option>
										</select> 
									</div>
							</div>
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										<spring:message code="anvizent.package.label.sourceType"/>
									</div>
									<div class='col-sm-5'>
										<label class="radio-inline"><input type="radio" name="sourceType" value="local"><spring:message code="anvizent.package.label.local"/></label> 
										<label class="radio-inline"><input type="radio" name="sourceType" value="s3"><spring:message code="anvizent.package.label.s3"/></label>
									</div>
							</div>
							
							<div class="row form-group">
								<label class="control-label col-sm-2"><spring:message code="anvizent.package.label.makeDefault"/>:</label>
								<div class='col-sm-5'>
										<label class="radio-inline"><input type="radio" name="masterDefault" value="true"><spring:message code="anvizent.package.button.yes"/></label> 
										<label class="radio-inline"><input type="radio" name="masterDefault" value="false"><spring:message code="anvizent.package.button.no"/></label>
									</div>
							</div>
								
								
				<div class='row form-group'>
						<div class="table-responsive">
							<label><spring:message code="anvizent.package.label.environmentVariables"/></label>
							<table class="table table-striped table-bordered tablebg" id="eltStgConfigTable">
								<thead>
									<tr>
										<!-- <th>Id</th> -->
										<th><spring:message code="anvizent.package.label.key"/></th>
										<th><spring:message code="anvizent.package.label.value"/></th>
										<th><spring:message code="anvizent.package.label.addOrDelete"/>
										 	<a href="#" class="btn btn-primary btn-sm" id="addStgKeyDiv"> <span class="glyphicon glyphicon-plus"></span></a>
										</th>
									</tr>
								</thead>
								<tbody>
									<tr class='cloneCopystgRow hidden'>
										<td>
											<input type='text' class='stg_key form-control' placeholder="property key">
											<input type='hidden' class='keyId' name='id'>
										</td>
										<td>
											<input type='text' class='stg_value form-control' placeholder="property value">
										</td>
										<td>
											<button class='btn btn-primary btn-sm addKeyPair' id='addStgKey'> 
												<span class='glyphicon glyphicon-plus'></span>
											</button>
											<button class='btn btn-primary btn-sm deleteKeyPair'>
												<span class='glyphicon glyphicon-trash'></span>
											</button>
										</td>
									</tr>
		
		
								</tbody>
							</table>
						</div>
						
				</div>
							
							
								
							
							
							<div>
								<div class='col-sm-9'>
									<button id="masterBack" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Back"/></button>
									<button type="button" class="btn btn-primary btn-sm" id="saveConfigBtn">
											<spring:message code="anvizent.package.button.save"/>
									</button>
								</div>
							</div>
						</div>
						
					</div>
	</div>
	 		
</div>
</div>