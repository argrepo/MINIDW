<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
 
<div class="col-md-12 rightdiv">
		<div class='row form-group'>
		<h4 class="alignText">Encrypt Authentication Parameters</h4>
 	</div>
	<div class="col-md-10">
		<div class="dummydiv"></div>
		<div class="col-sm-10"></div>
		<jsp:include page="admin_error.jsp"></jsp:include>
		<input type="hidden" id="userID"
			value="<c:out value="${principal.userId}"/>">
	</div>

	<div class="col-sm-10">
		<div id="successOrErrorMessage"></div>
	</div>
	<div class="col-sm-10">
	
	 <div class="row-form-group tableInfo" id="tableInfo">
			<div class='control-label col-sm-2'>
			Table Name
		   </div>
			<div class="row form-group">
				<div class="col-sm-4">
					<input class="form-control tableName" id ="tableName" placeholder="Table Name">
				</div>
			</div>
			</div>
			<div class='row form-group'>
    <div id="columnInfoDiv" style="margin-top: 5px;"></div>
    </div>
    <div class='row form-group'>
    <div class='control-label col-sm-2'>
			Where Condition Column Info
		   </div>
			<div class="row form-group">
				<div class="col-sm-4">
					<input class="form-control whereConditioncolumnName" id="whereConditioncolumnName" placeholder="Column Name">
				</div>
				<div class="col-sm-4">
					<input class="form-control whereConditionColumnText" id="whereConditionColumnText" placeholder="Column Value">
				</div>
			</div>
     <div class='control-label col-sm-2'>
		   </div>
			<div class="row form-group">
				<div class="col-sm-4">
					<input type="button" id="encryptColumnInfo" value="Encrypt"  class="btn btn-primary btn-sm">
				</div>
			</div>
    </div>
       <div class="row-form-group columnInfo" id="columnInfo" style="display: none">
			<div class='control-label col-sm-2'>
			Column Info
		   </div>
			<div class="row form-group">
				<div class="col-sm-4">
					<input class="form-control columnName" placeholder="Column Name">
				</div>
				<div class="col-sm-4">
					<input class="form-control columnText" placeholder="Column Value">
				</div>
				<div class="col-sm-2">
					<button type="button" class="btn btn-primary btn-sm addColumnInfo">
						<span class="glyphicon glyphicon-plus"></span>
					</button>
					<button type="button" class="btn btn-primary btn-sm deleteColumnInfo">
						<span class="glyphicon glyphicon-trash"></span>
					</button>
				</div>
			</div>
       </div>
	 </div>
	 </div>


