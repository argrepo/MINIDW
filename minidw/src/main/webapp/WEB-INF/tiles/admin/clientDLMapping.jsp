<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	<div class='row form-group'>
		<h4 class="alignText"><spring:message code="anvizent.admin.button.clientDLMapping"/></h4>
	</div>
	
<div class="dummydiv"></div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">


	<c:url value="/admin/dashBoardLayoutMaster/clientMapping" var="url"/>
 	<input type="hidden" value="${url }/save" id="saveUrl"/>

	<form:form modelAttribute="clientDLMappingForm" action="${url}">
		 		<div class='row form-group'>
		 			<label class="col-sm-2 labelsgroup control-label"><spring:message code="anvizent.package.label.clientId"/> :</label>
					<div class="col-sm-6">
						<form:select path="clientId" cssClass="form-control" >
							<spring:message code="anvizent.package.label.selectClient" var="selectOption" />
							<form:option value="0">${selectOption }</form:option>
							<form:options items="${allClients}"/>
						</form:select>
				 	</div>
		 	   	</div>
		 	   <div id="selectOneTableAlert"></div>
				<c:if test="${clientDLMappingForm.clientId != '0' && clientDLMappingForm.clientId != null}">
				  <c:if test="${not empty dLList}">
				  <div class="col-xs-12">
					<div class="table-responsive" id="dlsAlert">
						<table class="table table-striped table-bordered tablebg dlsInfo">
							<thead>
								<tr>
								    <th><spring:message code = "anvizent.package.label.selectAll"/> 
								     	<input type="checkbox" value="selectAll" class="selectAll">
								    </th>
									<th><spring:message code="anvizent.package.label.tableName"/></th>
									<th colspan=2><spring:message code="anvizent.package.label.lockUnLock"/></th>
								</tr>
							</thead>
							
							<tbody>
								<c:forEach items="${dLList}" var="dLInfo" varStatus="loop">
									<tr class="data-row">
						                <td> 
						                	<input type="checkbox" name="dLInfo[${loop.index}].checkedDl"  data-dLid="<c:out value="${dLInfo.dL_id}"/>" class='dlInfoByDlId'
						                		${dLInfo.checkedDl ? "checked='checked'" : ""}/> 
						               	 	<input type="hidden" name="dLInfo[${loop.index}].dL_id"  value="<c:out value="${dLInfo.dL_id}"/>" >
						               	 </td>
										<td>
											<c:out value="${dLInfo.dL_name}"/>
											<input type="hidden" name="dLInfo[${loop.index}].dL_name" value="<c:out value="${dLInfo.dL_name}"/>"/>
										</td>
										<td>
											<spring:message code="anvizent.package.message.Lock"/>
											<input type="radio" name="dLInfo[${loop.index}].isLocked" id="isLock" value="true" ${dLInfo.isLocked ? "checked='checked'" : ""}>
										</td>
										<td>
											<spring:message code="anvizent.package.message.UnLock"/>
											<input type="radio" name="dLInfo[${loop.index}].isLocked" id="isUnLock" value="false" ${!dLInfo.isLocked ? "checked='checked'" : ""}>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					</div>
					<div class="row form-group">
						<div class='col-sm-6'>
							<button type="button" class="btn btn-sm btn-primary" id="save"><spring:message code="anvizent.package.label.submit"/></button>
						</div>
					</div>
				</c:if>	
			</c:if>
	</form:form>
</div>
 
