<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<spring:htmlEscape defaultHtmlEscape="true" />
<spring:message code="anvizent.package.label.yes" var="yesVar"/>
<spring:message code="anvizent.package.label.no" var="noVar"/>
<div class="col-sm-12 rightdiv">
        
        <div class="dummydiv"></div>
		<ol class="breadcrumb">
		</ol>
		<div class="row form-group">
			<h4 class="alignText">
				<c:if test="${empty s3LogFor}"><spring:message code = "anvizent.package.label.s3AuditLogs"/></c:if>	
				<c:if test="${s3LogFor == 'client'}"><spring:message code = "anvizent.package.label.s3AuditLogsForClient"/></c:if>	
				<c:if test="${s3LogFor == 'user'}"><spring:message code = "anvizent.package.label.s3AuditLogsForUser"/></c:if>	
				<c:if test="${s3LogFor == 'packages'}"><spring:message code = "anvizent.package.label.s3AuditLogsForPackage"/></c:if>	
			</h4>
		</div>
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
		
		<form method="POST" action="<c:url value="/adt/package/s3AuditLogs/client"/>">
			<c:if test="${empty s3LogFor}">	
				<div class='col-sm-12'>
					<div class="table-responsive">
						<table class="table table-striped table-bordered tablebg s3AuditLogsTable">
							<thead>
								<tr>
									<th><spring:message code ="anvizent.package.label.clientId"/></th> 
									<th><spring:message code ="anvizent.package.label.noOfUsers"/></th>
									<th><spring:message code ="anvizent.package.label.noOfPackages"/></th>
									<th><spring:message code ="anvizent.package.label.uploadedFilesSize"/></th>
								</tr>
							</thead>
							<tbody>		
								<c:forEach var="logs"  items="${s3AuditLogs}">
									<tr class="<c:out value="${fn:containsIgnoreCase(logs.fileSize, 'MB') ? 'fileSizeMoreThan1Mb':''}"/>">
										<td><button type="submit" class="btn btn-primary btn-sm tablebuttons startLoader text-underline" value="<c:out value="${logs.clientId}"/>" name='client_Id'><c:out value="${logs.clientId}"/></button></td>
										<td><c:out value="${logs.usersCount}"/></td>
										<td><c:out value="${logs.packagesCount}"/></td>
										<td><c:out value="${logs.fileSize}"/></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>		
					</div>		
				</div>
			</c:if>	
			<input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
		</form>
		
		<form method="POST" action="<c:url value="/adt/package/s3AuditLogs/user"/>">
			<c:if test="${s3LogFor == 'client'}">
				<div class='col-sm-12'>
					<div class="table-responsive">
						
							<table class="table table-striped table-bordered tablebg s3AuditLogsTable">
								<thead>
									<tr>
										<th><spring:message code ="anvizent.package.label.userName"/></th>
										<th><spring:message code ="anvizent.package.label.noOfPackages"/></th>
										<th><spring:message code ="anvizent.package.label.uploadedFilesSize"/></th>
									</tr>
								</thead>
								<tbody>		
									<c:forEach var="logs"  items="${s3AuditLogs}">
										<tr class="<c:out value="${fn:containsIgnoreCase(logs.fileSize, 'MB') ? 'fileSizeMoreThan1Mb':''}"/>">
											<td><button type="submit" class="btn btn-primary btn-sm tablebuttons startLoader text-underline" value="<c:out value="${logs.userId}"/>" name='user_Id'><c:out value="${logs.createdBy}"/> </button></td>
											<td><c:out value="${logs.packagesCount}"/></td>
											<td><c:out value="${logs.fileSize}"/></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							
					</div>
				</div>
			</c:if>	
			<input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
		</form>
		
		<form method="POST" action="<c:url value="/adt/package/s3AuditLogs/package"/>">
			<c:if test="${s3LogFor == 'user'}">
				<div class='col-sm-12'>
					<div class="table-responsive">
							<input type="hidden" name="user_Id" value="<c:out value="${user_Id}"/>">							
							<table class="table table-striped table-bordered tablebg s3AuditLogsTable">
								<thead>
									<tr>
										<th><spring:message code ="anvizent.package.label.packageId"/></th>
										<th><spring:message code ="anvizent.package.label.packageName"/></th>
										<th><spring:message code ="anvizent.package.label.packageType"/></th>
										<th><spring:message code ="anvizent.package.label.uploadedFilesSize"/></th>
										<th><spring:message code ="anvizent.package.label.isActive"/></th>
									</tr>
								</thead>
								<tbody>		
									<c:forEach var="logs"  items="${s3AuditLogs}">										
										<tr class="<c:out value="${fn:containsIgnoreCase(logs.fileSize, 'MB') ? 'fileSizeMoreThan1Mb':''}"/>"> 
											<td><button type="submit" class="btn btn-primary btn-sm tablebuttons startLoader text-underline" value="<c:out value="${logs.packageId}"/>" name='package_Id'><c:out value="${logs.packageId}"/></button></td>
											<td><button type="submit" class="btn btn-primary btn-sm tablebuttons startLoader text-underline" style="word-break: break-all;white-space: normal;width:200px;cursor:auto;" value="<c:out value="${logs.packageId}"/>" name='package_Id'><c:out value="${logs.packageName}"/></button></td>
											<td>
												<c:if test="${logs.isStandard}">
													<spring:message code="anvizent.package.label.standard"/>									
												</c:if>
												<c:if test="${!logs.isStandard}">
													<spring:message code="anvizent.package.label.custom"/>								
												</c:if>
											</td>
											<td><c:out value="${logs.fileSize}"/></td>	
											<td><c:out value="${logs.isActive == true ? yesVar : noVar}"/></td>												
										</tr>
									</c:forEach>
								</tbody>
							</table>
							
					</div>
				</div>
			</c:if>	
			<input type="hidden" name="<c:out value="${_csrf.parameterName}"/>" value="<c:out value="${_csrf.token}"/>"/>
		</form>
		
		<c:if test="${s3LogFor == 'packages'}">
			<div class='col-sm-12'>
				<div class="row form-group" style="background-color:#fff;padding:5px;border-radius:4px;">						
					<div class="col-sm-4 col-md-4">
						<label class="control-label "><spring:message code ="anvizent.package.label.packageId"/></label>
						<input class="form-control" disabled="disabled" type="text" value="<c:out value="${userPackage.packageId}"/>">
					</div>
					<div class="col-sm-4 col-md-4">
						<label class="control-label "><spring:message code ="anvizent.package.label.packageName"/></label>
						<input class="form-control" disabled="disabled" type="text" value="<c:out value="${userPackage.packageName}"/>">
					</div>
					<div class="col-sm-4 col-md-4">
						<label class="control-label "><spring:message code ="anvizent.package.label.packageType"/></label>
						<c:if test="${userPackage.isStandard}">
							<input class="form-control" disabled="disabled" type="text" value="<spring:message code="anvizent.package.label.standard"/>">									
						</c:if>
						<c:if test="${!userPackage.isStandard}">
							<input class="form-control" disabled="disabled" type="text" value="<spring:message code="anvizent.package.label.custom"/>">						
						</c:if>						
					</div>	
				</div>
			
				<div class="table-responsive">
					<table class="table table-striped table-bordered tablebg s3SourcesAuditLogsTable">
						<thead>
							<tr>
								<th><spring:message code ="anvizent.package.label.fileName"/></th>
								<th><spring:message code ="anvizent.package.label.fileType"/></th>
								<th><spring:message code ="anvizent.package.label.uploadedFilesSize"/></th>
								<th><spring:message code ="anvizent.package.label.uploadedOn"/></th>
							</tr>
						</thead>
						<tbody>		
							<c:forEach var="logs"  items="${s3AuditLogs}">
									<tr class="<c:out value="${fn:containsIgnoreCase(logs.fileSize, 'MB') ? 'fileSizeMoreThan1Mb':''}"/>">
										<td><c:out value="${logs.fileName}"/></td>
										<td><c:out value="${logs.fileType}"/></td>
										<td><c:out value="${logs.fileSize}"/></td>
										<td><c:out value="${logs.uploadEndTime}"/></td>
									</tr>	
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</c:if>	
		<c:if test="${not empty s3LogFor && empty showBackBtn}">
			<div class="pull-left col-sm-12">		
				<a href="javascript:history.go(-1)" class="btn btn-primary back_btn btn-sm"><spring:message code="anvizent.package.link.back"/></a>		
			</div>
		</c:if>
</div>		