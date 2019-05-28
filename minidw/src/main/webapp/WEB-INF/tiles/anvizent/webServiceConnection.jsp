<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
    	
   	<div class="row form-group">
		<h4 class="alignText"><spring:message code = "anvizent.package.label.webServiceConnectionDetails"/></h4>
	</div>
	
	<jsp:include page="_error.jsp"/>
    
   	<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	
	<c:url value="/adt/package/webServiceConnection" var="url"></c:url>
	<input type="hidden" value="${url}/add" id="addUrl">
	<input type="hidden" value="${url}/save" id="saveUrl">
	<input type="hidden" value="${url}/validate" id="validateUrl">
	
	<c:url value="/adt/package/webServiceConnection/edit" var="editUrl"/>
	
	<div class="col-sm-12">
	<form:form modelAttribute="webServiceConnectionMaster" action="${editUrl}">
		<form:hidden path="pageMode"/>
			<c:choose>
				<c:when test="${webServiceConnectionMaster.pageMode == 'list'}">
					<div  style="width:100%;padding:0px 15px;">
		             <div class="row form-group" style="padding:5px;border-radius:4px;">
                        <a href="<c:url value="/adt/package/webServiceConnection/add"/>" class="btn btn-success btn-sm"  style="float:right;">
							<spring:message code="anvizent.package.label.add"/>
						</a>
		            </div>
	                </div>
					
					<div class='row form-group'>
						<div class="table-responsive">
							<table class="table table-striped table-bordered tablebg " id="existingWsConnectionsTable">
								<thead>
									<tr>
										<%-- <th><spring:message code="anvizent.package.label.sNo"/></th> --%>
										<th><spring:message code="anvizent.package.label.id"/></th>
										<th><spring:message code="anvizent.package.label.webServiceConnectionName"/></th>
										<%-- <th><spring:message code="anvizent.package.label.isActive"/></th> --%>
										<th><spring:message code="anvizent.package.label.edit"/></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${webServiceConnectionMasterList}" var="webServiceConMaster"  varStatus="index" >
										<tr>
									      	<%-- <td><c:out value="${index.index+1}"/></td> --%>
											<td><c:out value="${webServiceConMaster.id}"/></td>
											<td><c:out value="${webServiceConMaster.webServiceConName}"/></td>
											<td>
												<button class="btn btn-primary btn-sm tablebuttons editWsConnection" name="id" value="<c:out value="${webServiceConMaster.id}"/>" title="<spring:message code="anvizent.package.label.edit"/>" >
													<i class="fa fa-pencil" aria-hidden="true"></i>
												</button>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
			     	</div>
			   	</c:when>
			   	<c:when test="${webServiceConnectionMaster.pageMode == 'add' || webServiceConnectionMaster.pageMode == 'edit'}">
			     	 <div class="panel panel-info" id="createNewWebservicePanel">
						<div class="panel-heading">
						<spring:message code="anvizent.package.label.webserviceconnectiondetails"/> 
						</div>
						<div class="panel-body">
						<div class='row form-group '>
								<label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.webServiceTemplate"/> :</label>
								<div class='col-sm-9'>
									<form:select cssClass="form-control" path="webServiceTemplateMaster.id">
									     <form:option value="0"><spring:message code="anvizent.package.label.selectWebServiceTemplate"/></form:option>
										  <form:options items="${webServiceList}"/>
									</form:select>   
								</div>
							</div>
						
							<div class='row form-group '>
								<label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.webServiceConnectionName"/> :</label>
								<div class='col-sm-9'>
									<form:hidden path="id"/>
									<spring:message code="anvizent.package.label.webServiceConnectionName" var="webServiceConnectionName" />
									<form:input cssClass="form-control" path="webServiceConName" data-minlength="1" data-maxlength="255" placeholder="${webServiceConnectionName}"></form:input>
									<form:hidden path="webServiceTemplateMaster.webServiceAuthenticationTypes.id"/>
									<form:hidden path="webServiceTemplateMaster.webserviceType"/>
									<form:hidden path="webServiceTemplateMaster.soapBodyElement"/>
									<form:hidden path="webServiceTemplateMaster.apiAuthBodyParams"/>
									<%-- <form:hidden path="webServiceTemplateMaster.authenticationBodyParams"/> --%>
									<form:hidden path="requestParams"/>
									<form:hidden path="bodyParams"/>
									<form:hidden path="headerKeyvalues"/>
									<form:hidden path="authPathParams"/>
									<form:hidden path="responseStatusCode"/>
									<form:hidden path="responseStatusText"/>
									<form:hidden path="authenticationToken"/>
									<form:hidden path="authenticationRefreshToken"/>
									<form:hidden path="webServiceTemplateMaster.apiSessionAuthURL"/>
								</div>
							</div>
							
							<div class='row form-group'>
									<label class='col-sm-3 control-label'>
										<spring:message code="anvizent.package.label.dataSource"/>:
									</label>
									<div class='col-sm-9'>
										<form:select cssClass="form-control" path="dataSourceName">
										     <form:option value="0"><spring:message code="anvizent.package.label.selectDataSource"/></form:option>
											  <form:options items="${allDataSourceList}"/>
											  <form:option value="-1" class="otherOption"><spring:message code="anvizent.package.label.other"/></form:option>
										</form:select>   
									</div>
							</div>
							
							<div class='row form-group dataSourceOther' style="${webServiceConnectionMaster != '-1'?'display:none;':''}">
									<label class='col-sm-3 control-label'>
									</label>
									<div class='col-sm-9'>
										<form:input path="dataSourceNameOther" cssClass="form-control" data-minlength="1" data-maxlength="45"/>
									</div>
							</div>
							
							<c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.id != null && webServiceConnectionMaster.webServiceTemplateMaster.id != '0'}">
								<div class='row form-group authenticationType'> 
									<label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.authenticationType"/> :</label> 
									<div class='col-sm-9 ' >
										<form:hidden path="webServiceTemplateMaster.webServiceAuthenticationTypes.authenticationType"/>
										<span><c:out value="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.authenticationType}"/></span>
                                       <%-- <c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id == 4}">
														 <input type="button" value='Get OAuth Verifier' data-templateid="${webServiceConnectionMaster.webServiceTemplateMaster.id}"  class="btn btn-primary btn-sm" id="oauthVerifier" />
										</c:if> --%>
                                       </div>
							    </div>
							    <div class='row form-group sslDisable'> 
									<label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.sslDisable"/> :</label> 
									<div class='col-sm-9 ' >
										<form:checkbox path="webServiceTemplateMaster.sslDisable"/> 
										<c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.sslDisable == true}">
										 <span id ="sslauthenticationdisable" style="color:red"><spring:message code="anvizent.package.message.sslauthenticationdisable"/>
										</span>
										</c:if>
										<span id ="sslauthenticationdisable" style="color:red"></span>
										 <br>
                                       </div>
							    </div>
						 <c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id == 1}">
						  <div class='row form-group baseUrl'>
										   <spring:message code="anvizent.package.label.enterUrl" var="enterUrl" />
											<label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.baseUrl" /> :</label>
											<div class='col-sm-9'>
												<form:input cssClass="form-control" placeholder="${enterUrl}"  path="webServiceTemplateMaster.baseUrl"/>
											</div>
						  </div>
						  <div class='row form-group selectTimeZone'>
						 					<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.timeZone" /> :</label>
											<div class="col-sm-9">
												<form:select path="webServiceTemplateMaster.timeZone" cssClass="form-control timeZone">
													<spring:message code="anvizent.package.label.selectTimeZone" var="selectOption" />
														<form:option value="0"><c:out value="${selectOption}"/></form:option>
														<form:options items="${timesZoneList}" />
												</form:select>
								 			</div>
							</div>
						 </c:if>
					<c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id != 1}">
					  <div class="row form-group">
					  <label class='col-sm-3 control-label'></label>
					  <div class='col-sm-9'>
					  <div id="accordion-first" class="clearfix"><!-- standard start accordion -->
                              <div class="accordionWs" id="accordion2Ws">
                                	<div class="accordion-group">
			                    <div class="accordion-heading" style="border: 1px solid #dce1e4;background:#1096eb;">
			                  <a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordion2WS" href="#collapseOneWS">
			                  <span class="glyphicon glyphicon-plus-sign"></span> Detailed Info
			                  </a>
			               </div>
			           <div style="height: 0px;" id="collapseOneWS" class="accordion-body collapse">	
			           <div class="panel panel-info">
			                 <div class="panel-body">
								 <div class='row form-group baseUrl'>
										   <spring:message code="anvizent.package.label.enterUrl" var="enterUrl" />
											<label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.baseUrl" /> :</label>
											<div class='col-sm-9'>
												<form:input cssClass="form-control" placeholder="${enterUrl}"  path="webServiceTemplateMaster.baseUrl"/>
											</div>
										</div>
									<c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id != 3}">
									   <c:choose>
										 <c:when test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id != 6}">
										 	<c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id != 4 }">
										  <div class='row form-group authenticationUrl '>
											<label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.authenticationUrl" /> :</label>
										    <div class='col-sm-9'>
											  <form:input cssClass="form-control" path="webServiceTemplateMaster.authenticationUrl"/><br>
										    </div>
										  </div>
										  </c:if>
										 </c:when>
										 <c:otherwise>
										    <div class="row form-group loginurl">
												<label class="control-label col-sm-3"> Login URL :</label>
												<div class="col-sm-9">
												    <spring:message code="anvizent.package.label.enterUrl" var="enterUrl" />
													<form:input path="webServiceTemplateMaster.loginUrl" placeholder="${enterUrl}"  data-minlength="1" data-maxlength="255" cssClass="form-control"/>
												</div>
											</div>
											<div class="row form-group logouturl">
												<label class="control-label col-sm-3"> Logout URL :</label>
												<div class="col-sm-9">
												    <spring:message code="anvizent.package.label.enterUrl" var="enterUrl" />
													<form:input path="webServiceTemplateMaster.logoutUrl" placeholder="${enterUrl}"  data-minlength="1" data-maxlength="255" cssClass="form-control"/>
												</div>
											</div>
										 </c:otherwise>
										</c:choose>
									
								        <div class='row form-group'>
								         <label class='col-sm-3 control-label'></label>
								          <div class='col-sm-9'>
								           <form:checkbox path="webServiceTemplateMaster.baseUrlRequired"/> <spring:message code="anvizent.package.label.baseUrlRequired"	/>
								          </div>
									    </div>
										
									<div class='row form-group selectTimeZone'>
						 					<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.timeZone" /> :</label>
											<div class="col-sm-9">
												<form:select path="webServiceTemplateMaster.timeZone" cssClass="form-control timeZone">
													<spring:message code="anvizent.package.label.selectTimeZone" var="selectOption" />
														<form:option value="0"><c:out value="${selectOption}"/></form:option>
														<form:options items="${timesZoneList}" />
												</form:select>
								 			</div>
							 	 		</div>
										
										<div class='row form-group authenticationMethodType '>
											<label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.methodType" /> :</label>
											<div class='col-sm-9'>
											   <div class="methodType hidden">
													<%-- <span>${webServiceConnectionMaster.webServiceTemplateMaster.authenticationMethodType}</span> --%>
														<label class="radio-inline">
															<form:radiobutton path="webServiceTemplateMaster.authenticationMethodType" value="GET"/>
															<spring:message code="anvizent.package.label.get" />
											 			</label>	
														<label class="radio-inline">
															<form:radiobutton path="webServiceTemplateMaster.authenticationMethodType" value="POST"/>
															<spring:message code="anvizent.package.label.post" />
														</label>
												</div>
												<div class="methodTypeText">
														<c:out value="${webServiceConnectionMaster.webServiceTemplateMaster.authenticationMethodType }"/>
												</div>
											</div>
										</div>
									</c:if>
									<c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id == 3}">
										<form:hidden path="webServiceTemplateMaster.authenticationUrl"/>
									</c:if>
									<!--  OAuth2 start  -->
							  		<c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id == 5 }">
								        <input type="hidden" class="form-control" id="authCodeValue" name="authCodeValue">
								        <form:hidden path="webServiceTemplateMaster.oAuth2.accessTokenValue"/>
								        <form:hidden path="webServiceTemplateMaster.oAuth2.refreshTokenValue"/>
										<form:hidden path="webServiceTemplateMaster.oAuth2.authCodeValue"/>
										
								        <div class='row form-group calBackUrl'>
											  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.calBackUrl" /> :</label>                         
											   <div class='col-sm-9'>
													<form:input path="webServiceTemplateMaster.oAuth2.redirectUrl" cssClass="form-control"/>
											   </div>
										 </div>
									 
										 <div class='row form-group accessTokenUrl'>
											   <label class='col-sm-3 control-label'> <spring:message code="anvizent.package.label.accessTokenUrl" /> :</label>
											   <div class='col-sm-9' >
													<form:input cssClass="form-control" path="webServiceTemplateMaster.oAuth2.accessTokenUrl"/>
											   </div>
										 </div>
										 <div class='row form-group clientIdentifier'>
											  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.clientIdentifier" /> :</label>
											   <div class='col-sm-9' >
													<form:input path="webServiceTemplateMaster.oAuth2.clientIdentifier" cssClass="form-control" />
											   </div>
										 </div>
										 <div class='row form-group clientSecret'>
											  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.clientSecret" /> :</label>
											   <div class='col-sm-9' >
													<form:input cssClass="form-control" path="webServiceTemplateMaster.oAuth2.clientSecret" type="password"/>
											   </div>
										 </div>
										 
										<div class='row form-group scope'>
											  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.scope" /> :</label>
											   <div class='col-sm-9' >
													<form:input cssClass="form-control" path="webServiceTemplateMaster.oAuth2.scope"/>
											   </div>
										 </div>	
										 
										 	<div class='row form-group scope'>
											  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.state" /> :</label>
											   <div class='col-sm-9' >
													<form:input cssClass="form-control" path="webServiceTemplateMaster.oAuth2.state"/>
											   </div>
										 </div>	
										 									  
										 <div class='row form-group grantType'>
											  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.grantType" /> :</label>
											   <div class='col-sm-9' >
													<form:hidden path="webServiceTemplateMaster.oAuth2.grantType"/>
										          <span><c:out value="${webServiceConnectionMaster.webServiceTemplateMaster.oAuth2.grantType}"/></span>
											   </div>
										 </div>
									 </c:if>
									<!--  OAuth2 end  -->
									<!--  OAuth1 start  -->
									<c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id == 4 }">
								        <input type="hidden" class="form-control" id="oauth1CodeValue" name="oauth1CodeValue">
								        <form:hidden path="webServiceTemplateMaster.oAuth1.requestToken"/>
								        <form:hidden path="webServiceTemplateMaster.oAuth1.requestTokenSecret"/>
								        <form:hidden path="webServiceTemplateMaster.oAuth1.token"/>
								        <form:hidden path="webServiceTemplateMaster.oAuth1.tokenSecret"/>
								        <form:hidden path="webServiceTemplateMaster.oAuth1.verifier" />
										 <div class='row form-group oAuth1RequestURL'>
											  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.requestURL" /> :</label>
											    <spring:message code="anvizent.package.label.requestURL" var="requestURL" /> 
											   <div class='col-sm-9' >
													<form:input path="webServiceTemplateMaster.oAuth1.requestURL"  placeholder="${requestURL}" cssClass="form-control"/>
											   </div>
										 </div>
										 <div class='row form-group oAuth1TokenURL' >
													  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.tokenURL" /> :</label>
													    <spring:message code="anvizent.package.label.tokenURL" var="tokenURL" /> 
													   <div class='col-sm-9' >
															<form:input cssClass="form-control"  placeholder="${tokenURL}" path="webServiceTemplateMaster.oAuth1.tokenURL" />
													   </div>
										 </div>
		                                  <div class='row form-group oAuth1AuthURL'>
													  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.authenticationUrl" /> :</label>
													     <spring:message code="anvizent.package.label.authenticationUrl" var="authenticationUrl" /> 
													   <div class='col-sm-9' >
															<form:input cssClass="form-control" placeholder="${authenticationUrl}" path="webServiceTemplateMaster.oAuth1.authURL" />
													   </div>
										  </div>	
										   <div class='row form-group oAuth1CallbackURL'>
											  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.callbackUrl" /> :</label>
											     <spring:message code="anvizent.package.label.callbackUrl" var="callbackUrl" /> 
											   <div class='col-sm-9' >
													<form:input cssClass="form-control" placeholder="${callbackUrl}" path="webServiceTemplateMaster.oAuth1.callbackURL" />
											   </div>
								          </div>
										   <div class='row form-group oAuth1ConsumerKey'>
													  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.consumerKey" /> :</label>
													  <spring:message code="anvizent.package.label.consumerKey" var="consumerKey" />  
													   <div class='col-sm-9' >
															<form:input cssClass="form-control" placeholder="${consumerKey}" path="webServiceTemplateMaster.oAuth1.consumerKey" />
													   </div>
										  </div>
										   <div class='row form-group oAuth1ConsumerSecret'>
													  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.consumerSecret" /> :</label>
													  <spring:message code="anvizent.package.label.consumerSecret" var="consumerSecret" /> 
													   <div class='col-sm-9' >
															<form:input cssClass="form-control" placeholder="${consumerSecret}" path="webServiceTemplateMaster.oAuth1.consumerSecret" />
													   </div>
										  </div>
										   <div class='row form-group oAuth1SignatureMethod'>
													  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.signatureMethod" /> :</label>
													   <div class='col-sm-9' >
															<form:select path="webServiceTemplateMaster.oAuth1.signatureMethod" cssClass="form-control signatureMethod" >
																<form:option value="0">Select Signature Method</form:option>
																<form:option value="HMAC-SHA1">HMAC-SHA1</form:option>
															</form:select>
													   </div>
										   </div>
										     <div class='row form-group oAuth1Version'>
											  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.version" /> :</label>
											 <spring:message code="anvizent.package.label.version" var="oatuh1version" />  
											   <div class='col-sm-9' >
													<form:input cssClass="form-control" placeholder="${oatuh1version}" path="webServiceTemplateMaster.oAuth1.version" />
											   </div>
							   	           </div>
										    <div class='row form-group oAuth1Scope'>
													  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.scope" /> :</label>
													 <spring:message code="anvizent.package.label.scope" var="oatuh1scope" />  
													   <div class='col-sm-9' >
															<form:input cssClass="form-control" placeholder="${oatuh1scope}(Optional)" path="webServiceTemplateMaster.oAuth1.scope" />
													   </div>
									   	 </div>
										    <div class='row form-group oAuth1Realm'>
													  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.realm" /> :</label>
													  <spring:message code="anvizent.package.label.realm" var="realm" /> 
													   <div class='col-sm-9' >
															<form:input cssClass="form-control"  placeholder="${realm}(Optional)"  path="webServiceTemplateMaster.oAuth1.realm" />
													   </div>
										    </div>
									 </c:if>
									 
									 <!-- OAuth1 End -->
									 
									 <div class="row form-group hidden">
										<label class="control-label col-sm-3"><spring:message code="anvizent.package.label.activeStatus"/> :</label>
										<div class='col-sm-6'>
										 	<div class="activeStatus">
										 		 <label class="radio-inline">
													<form:radiobutton path="active" value="1"/><spring:message code="anvizent.package.button.yes"/>
												 </label>	
												 <label class="radio-inline">
											    	<form:radiobutton path="active" value="0"/><spring:message code="anvizent.package.button.no"/> 
											    </label>
										 	</div>
										</div>
									</div>
									</div>
									</div>
									</div>
									</div>
									</div>
									</div>
									</div>
									</div>
									<%-- <c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id == 4}">
									  <div class='row form-group oAuth1Verifier' style="display:none">
												  <label class='col-sm-3 control-label'><spring:message code="anvizent.package.label.verifier" /><span class="paramNameMandatory" style="position: inherit;">*</span> :</label>
												  <spring:message code="anvizent.package.label.verifier" var="verifier" /> 
												   <div class='col-sm-9' >
														<form:input cssClass="form-control"  placeholder="${verifier}"  path="webServiceTemplateMaster.oAuth1.verifier" />
												   </div>
									    </div>
									</c:if> --%>
									<c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id == 2 || 
										webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id == 3 ||
										webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id == 6 || webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id == 4}">
										
										<c:forEach var="authReqParams" items="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceTemplateAuthRequestparams}" varStatus="loop">
											<div class='row form-group' id="requestParams">
												<c:if test="${loop.index == 0}">
													<label class='col-sm-3 control-label reqParamHeader'><spring:message code="anvizent.package.label.requestParamHeader" /> :</label>
												</c:if>
												<c:if test="${loop.index > 0}"><label class='col-sm-3 control-label reqParamHeader'></label></c:if>
												<div class="col-sm-3">
													<span class="paramName"><c:out value="${authReqParams.paramName}"/></span>
													<form:hidden path="webServiceTemplateMaster.webServiceTemplateAuthRequestparams[${loop.index}].paramName"/>
													<c:if test="${authReqParams.mandatory}">
														<span class="paramNameMandatory" style="position: inherit;">*</span>
														<form:hidden path="webServiceTemplateMaster.webServiceTemplateAuthRequestparams[${loop.index}].mandatory"/>
													</c:if>
												</div>
												<div class="col-sm-6">
													<c:if test="${authReqParams.passwordType}">
														<form:hidden path="webServiceTemplateMaster.webServiceTemplateAuthRequestparams[${loop.index}].passwordType"/>
														<input type="password" class="form-control paramValue ${authReqParams.mandatory ? 'fieldIsMadatory' : ''}" placeholder="<spring:message code="anvizent.package.label.value" />">
													</c:if>
													<c:if test="${!authReqParams.passwordType}">
														<form:hidden path="webServiceTemplateMaster.webServiceTemplateAuthRequestparams[${loop.index}].passwordType"/>
														<input type="text" class="form-control paramValue ${authReqParams.mandatory ? 'fieldIsMadatory' : ''}" placeholder="<spring:message code="anvizent.package.label.value" />">
													</c:if>
												</div>
											</div>
										</c:forEach>	
										
										<c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.webServiceAuthenticationTypes.id != 3}">
											<div id="pathParamsBlocks">
										
											</div>
											
											<div  id="samplePathParamsBlock"  style="display:none;">
												<div class="row form-group pathParamsBlock">
													<label class="col-sm-3 control-label labelText"> <spring:message code="anvizent.package.label.pathParameters" /></label>
													<div class="col-sm-3">
														<span class="pathParamName"></span>
													</div>
													<div class="col-sm-6">
														<input type="text" class="form-control pathParamValue" placeholder="<spring:message code="anvizent.package.label.value" />">
													</div>
												</div>
											</div>
											
											<form:hidden path="webServiceTemplateMaster.apiAuthRequestHeaders"/>
											<div id="headerKeyValueBlocks">
											
											</div>
											<form:hidden path="webServiceTemplateMaster.authenticationBodyParams" class="authenticationBodyParams"/>
											<div id="authenticationBodyParamsDiv" style="display:none;">
											
											</div>
											<div id="sampleHeaderKeyValueBlock" style="display:none;">
												<div class="row form-group headerKeyValue">
													<label class='col-sm-3 control-label labelText'><spring:message code="anvizent.package.label.headerKeyValues" /> :</label>
													<div class="col-sm-3">
														<span class="headerKey"></span>
													</div>
													<div class="col-sm-6">
														<input type="text" class="form-control headerValue" placeholder="<spring:message code="anvizent.package.label.value" />">
													</div>
												</div>
											</div>
											
											<div class="row form-group paramNameValue" style="display:none;" id="authenticationBodyParamsBlock">
												<label class="col-sm-3 control-label requestBodyParams"></label>
												<div class="col-sm-3">
													<span class="paramName"></span>
													<span class="paramNameMandatory" style="position: inherit;">*</span>
												</div>
												<div class="col-sm-6">
													<input type="text" class="form-control paramValue" placeholder="<spring:message code="anvizent.package.label.value" />"
													data-minlength="1" data-maxlength="45">
												</div>
											</div>

										</c:if>
									</c:if>
								</c:if>
								<div class='row form-group col-sm-12'>
									<input type="button" value='<spring:message code="anvizent.package.button.validate"/>' class="btn btn-primary btn-sm" id="testWebServiceAuthenticate" />
									<input type="button" id="saveNewWebserviceConnection" value='<spring:message code="anvizent.package.label.saveWebServiceConnection"/>' class="btn btn-primary btn-sm" style="display:none;"/>
									<a href="<c:url value="/adt/package/webServiceConnection"/>" class="btn btn-primary btn-sm back back_btn"><spring:message code="anvizent.package.label.Back"/></a>
								</div>
								<div class='row form-group col-sm-12'>
									<div id="statusCode"></div>								
								</div>
							</c:if>
							<c:if test="${webServiceConnectionMaster.webServiceTemplateMaster.id == null || webServiceConnectionMaster.webServiceTemplateMaster.id == '0'}">
								<div class='row form-group col-sm-12'>
									<a href="<c:url value="/adt/package/webServiceConnection"/>" class="btn btn-primary btn-sm back back_btn"><spring:message code="anvizent.package.label.Back"/></a>
								</div>	
							</c:if>
						</div>
					</div>
				</c:when>
			</c:choose>
		</form:form>
	</div>
</div>