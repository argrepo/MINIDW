<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-md-12 rightdiv">
  	
  	<div class='row form-group'>
		<h4 class="alignText">ELT Load Parameters</h4>
 	</div>

 	<input type="hidden" id="userID" value="${principal.userId}">
	<jsp:include page="admin_error.jsp"></jsp:include>
	<div class="col-md-12 message-class"></div>
	<div class="col-sm-12">
				
	<div id="loadParametersTable">
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
							<th> Name</th>
							<th>No of Executers</th>
							<th>Executer Memory</th>
							<th>Executor Cores</th>
							<th>Edit</th>
							<th>Delete</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${eltLoadParamterList}" var="eltloadParameter">
						<tr>
							<td>${eltloadParameter.id}</td>
							<td>${eltloadParameter.name}</td>
							<td>${eltloadParameter.noOfExecutors}</td>
							<td>${eltloadParameter.executorMemory}${eltloadParameter.executorMemoryType }</td>
							<td>${eltloadParameter.executorCores}</td>
							<td>
								<button class="btn btn-primary btn-sm tablebuttons loadParametersId" value="${eltloadParameter.id}" data-tagid="${eltloadParameter.id}" title="<spring:message code="anvizent.package.label.edit"/>" >
										<i class="fa fa-pencil" aria-hidden="true"></i>
								</button>
							</td>
							<td>
								<button class="btn btn-primary btn-sm tablebuttons deleteById" value="${eltloadParameter.id}" data-tagid="${eltloadParameter.id}" title="<spring:message code="anvizent.package.label.delete"/>" >
										<span class='glyphicon glyphicon-trash'></span>
								</button>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	
		<div class="row form-group createLoadParamtersDiv">
		<input type="hidden" id="selectedId">
			<div class="panel panel-default">
				<div class="panel-heading"> ELT Load Parameters</div>
					<div class="panel-body">
							<div class='row form-group'>
									<div class='col-sm-2'>
										Name
									</div>
									<div class='col-sm-5'>
										<input type="text" id="name" name="name" class="form-control" data-minlength="1" data-maxlength="45"> 
									</div>
							</div>
					
							<div class='row form-group'>
									<div class='col-sm-2'>
										No Of Executers
									</div>
									<div class='col-sm-5'>
										<select class="form-control noOfExecutors" name="noOfExecutors">
												<option value='0'>Select</option>
												 <c:forEach var = "i" begin = "1" end = "100">
        											 <option value='${i}'><c:out value = "${i}"/></option>
   												 </c:forEach>
										</select>   
									</div>
							</div>
							
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										Memory  (in GB's)
									</div>
									<div class='col-sm-5'>
										<select class="form-control executorMemory" name="executorMemory">
												<option value='0'>Select</option>
												<c:forEach var = "i" begin = "1" end = "100">
        											 <option value='${i}'><c:out value = "${i}"/></option>
   												 </c:forEach>
										</select>   
									</div>
							</div>
							
							
							<div class='row form-group'>
									<div class='col-sm-2'>
										Execution cores
									</div>
									<div class='col-sm-5'>
										<select class="form-control executorCores" name="executorCores">
												<option value='0'>Select</option>
												<c:forEach var = "i" begin = "1" end = "100">
        											 <option value='${i}'><c:out value = "${i}"/></option>
   												 </c:forEach>
										</select>  
									</div>
							</div>
							
					<div class='col-sm-9'>
						<button type="button" class="btn btn-primary btn-sm saveLoadParametersBtn">
								<spring:message code="anvizent.package.button.save"/>
						</button>
						<button id="loadParametersBack" class="btn btn-primary btn-sm"><spring:message code="anvizent.package.label.Back"/></button>
					</div>
				</div>
			</div>
						
					</div>
	</div>
	
</div>
