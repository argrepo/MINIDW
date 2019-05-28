<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" /> 

<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.navLeftTabLink.label.templateMigration"/></h4>
 	</div>
	<jsp:include page="admin_error.jsp"></jsp:include>
	<input type="hidden" id="userId" value="${principal.userId}">
	 <c:url value="/admin/templateMigration" var="url"/>
 	<input type="hidden" value="${url }" id="editUrl"/>
 	<input type="hidden" value="${url }/list" id="packageListUrl"/>
 	<input type="hidden" value="${url }/save" id="saveUrl"/>
	 
	<form:form modelAttribute="templateMigration" action="${url}">
	  <div class="row form-group"> 
				<div class="col-sm-1 control-label"> 
					<label ><spring:message code="anvizent.package.label.Template" /> :</label>
				</div>	
				<div class="col-sm-6">
					<label for="templateMigrationOption1">
						<form:radiobutton  path="templateMigrateExportOption" value="migrate" checked="checked"/><spring:message code="anvizent.package.label.migrate" /> 
					</label> 
					<label for="templateMigrationOption2"  style="padding-left: 16px;">
						<form:radiobutton  path="templateMigrateExportOption" value="export" /> <spring:message code="anvizent.package.label.export" />  
					</label>
				</div>
	   </div>
		<div class="row form-group" >
					 		<label class="col-sm-1 control-label"><spring:message code="anvizent.package.label.clientId"/> :</label>
							<div class="col-sm-3" id="clientIdDiv">
								<form:select path="clientId" cssClass="form-control">
									<spring:message code="anvizent.package.label.selectClient" var="selectClient" />
									<form:option value="0">${selectClient }</form:option>
									<form:options items="${allClients}" />
								</form:select>
							</div>
							<c:if test="${templateMigration.clientId != null && templateMigration.clientId != '0' }">
							<div class="col-sm-3" id="userIdDiv" >
								<form:select path="userId" cssClass="form-control">
									<spring:message code="anvizent.package.label.selectUser" var="selectUser" />
									<form:option value="0">${selectUser }</form:option>
									<form:options items="${getAllUsers}" />
								</form:select>
							</div>
							</c:if>
		</div>
							
		<c:if test="${templateMigration.pageMode == 'list' }">
			<div class='row form-group'>
				 <div class="table-responsive">
					<table class="table table-striped table-bordered tablebg" id="packageTable">
								<thead>
									<tr>
										<th class="col-xs-3"><input type="checkbox" id="checkedAll"> <spring:message code="anvizent.package.label.selectAll"/></th>
										<th class="col-xs-3"><spring:message code="anvizent.package.label.PackageId"/></th>
										<th class="col-xs-3"><spring:message code="anvizent.package.label.PackageName"/></th>
										<th class="col-xs-3"><spring:message code="anvizent.package.label.PackageType"/></th>
									</tr>
								</thead>
									<tbody>
									        <c:choose>
									        <c:when test="${not empty userPackageList}">
											<c:forEach items="${userPackageList}" var="packageList"  varStatus="loop"> 
												<tr>
													<td id="selectPackage"><input id="checked${loop.index}" type="checkbox" name="packageList[${loop.index}].isMapped" class='checkCase'></td>
													<td id="packageId"><c:out value="${packageList.packageId}" /><input type="hidden" name="packageList[${loop.index}].packageId" value="${packageList.packageId}"/></td>
													<td id="packageName"><c:out value="${packageList.packageName}" /><input type="hidden" name="packageList[${loop.index}].packageName" value="${packageList.packageName}"/></td>
													<td id="packageType">  <c:out value="${packageList.isStandard   ? 'Standard Package' : 'Custom Package'}" /><input type="hidden" name="packageList[${loop.index}].isStandard" value="${packageList.isStandard}"/></td>
												</tr>
											</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
												<td ><spring:message code="anvizent.package.label.NoPackagesFoundforuser"/></td>
												</tr>
											 </c:otherwise>
											</c:choose>
								</tbody>
					    </table>
					</div>
		</div>	
		<div class='row form-group'>
		<div class="col-sm-3">
		  <input  type="button" id="save" class="btn btn-primary btn-sm" value="Submit"> 
		  </div>
		</div>
   </c:if>
							
	 	
 	   
            
	</form:form>
</div>	