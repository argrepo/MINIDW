<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">
       	<div class='row form-group'>
		<h4 class="alignText">
			<spring:message code = "anvizent.package.label.apisData"/>
		</h4>
	</div>
	<input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
	<jsp:include page="_error.jsp"></jsp:include>
	
	<div  style="width:100%;padding:0px 15px;">
		             <div class="row form-group" style="padding:5px;border-radius:4px;">
                        <a href="<c:url value="/adt/package/apisDataInfo/create"/>" class="btn btn-success btn-sm"  style="float:right;">
							<spring:message code="anvizent.package.label.create"/>
						</a>
		            </div> 
	 </div>
	 
	  <div class='row form-group' id="customDataDiv">
            <div class="col-xs-12 table-responsive">
 	             <div>
               	<table class="table table-striped table-bordered tablebg" id="ddLayoutTablesList">
				<thead>
					<tr>
					    <th><spring:message code = "anvizent.package.label.id"/></th>
						<th><spring:message code = "anvizent.package.label.apiName"/></th>
						<th><spring:message code = "anvizent.package.label.endPointUrl"/></th>
						<th><spring:message code = "anvizent.package.label.testLink"/></th>
						<th><spring:message code = "anvizent.package.label.methodType"/> </th>
						<th><spring:message code = "anvizent.package.label.query"/> </th>
						<th><spring:message code = "anvizent.package.label.active"/></th>
						<th><spring:message code="anvizent.package.label.edit"/> </th>
					</tr>
				</thead>
				<tbody> 
					<c:forEach  items="${apisDetailsList}" var="apisListInfo">
					  <tr>
					      <td><c:out value="${apisListInfo.id}"/></td>
					      <td> <c:out value="${apisListInfo.apiName}"/></td>
		                  <td> <c:out value="${apisListInfo.endPointUrl}"/></td>
		                  <td><a href="#" class="btn btn-primary btn-xs testLink" data-urlLink="${apisListInfo.testLink}" title="<spring:message code = "anvizent.package.label.testLink"/>"><spring:message code = "anvizent.package.label.testLink"/></a></td>
					      <td> <c:out value="${apisListInfo.methodType}"/></td>
					     <%--  <td> <c:out value="${apisListInfo.apiQuery}"/></td> --%>
					      <td><a href="#" class="btn btn-primary btn-xs viewQuery" data-apiId="${apisListInfo.id}" id="editApisForm" title="<spring:message code = "anvizent.package.button.viewQuery"/>"><spring:message code = "anvizent.package.button.viewQuery"/></a></td>
					      <td> <c:out value="${apisListInfo.active ? 'Yes':'No' }"/></td>
					      <td><a href="#" class="btn btn-primary btn-xs editApi" data-apiId="${apisListInfo.id}" id="editApisForm" title="<spring:message code="anvizent.package.label.edit"/>"><span class="glyphicon glyphicon-edit"></span></a></td>
					  </tr>	
	               </c:forEach>
				</tbody>
	          </table>
	    </div>
	    </div>
	 </div>	
	 	
				
     	<div class="modal fade" tabindex="-1" role="dialog" id="apisDataPopUp" data-backdrop="static" data-keyboard="false">
			  <div class="modal-dialog modal-lg">
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        	<h4 class="modal-title custom-modal-title"><spring:message code = "anvizent.package.label.apisData"/></h4>
			      </div>
			      <div class="modal-body"> 
			      	<div class='row form-group'>
							<label class="col-md-3 control-label" for="apiName"> <spring:message code = "anvizent.package.label.apiName"/></label>
							<div class='col-sm-6' >
								<input type="text" id="apiName" class="form-control" name="apiName" data-minlength="1" data-maxlength="64"/>
								<input type="hidden" name="id" value="id" id="apiId">
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
							<label class="col-md-3 control-label" for="methodType"> <spring:message code = "anvizent.package.label.methodType"/> </label>
							<div class='col-sm-6' >
								<label class="radio-inline"><input type="radio" name="methodType" value="GET"><spring:message code = "anvizent.package.label.get"/></label>  
								<label class="radio-inline"><input type="radio" name="methodType" value="POST"><spring:message code = "anvizent.package.label.post"/></label>
								<!-- <label class="radio-inline"><input type="radio" name="methodType" value="PUT" >PUT</label> -->
							</div>
					</div>
					
					<div class='row form-group'>
							<label class="col-md-3 control-label" for="active"><spring:message code = "anvizent.package.label.activeStatus"/></label>
							<div class='col-sm-6' >
								<label class="radio-inline"><input type="radio" name="active" value="true"><spring:message code = "anvizent.package.label.yes"/></label>  
								<label class="radio-inline"><input type="radio" name="active" value="false" ><spring:message code = "anvizent.package.label.no"/> </label>
							</div>
					</div>
					
					<div class='row form-group'>
							<label class="col-md-3 control-label" for="apiQuery"><spring:message code = "anvizent.package.label.query"/> </label>
							<div class='col-sm-6' >
								<textarea rows="4" id="apiQuery" name = "apiQuery" class="form-control"></textarea>
							</div>
					</div>
					
			      </div>
			      <div class="modal-footer">
			        <button type="button" class="btn btn-primary btn-sm" id="apisData"><spring:message code = "anvizent.package.button.save"/></button>
			        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "anvizent.package.button.close"/></button>
			      </div>
			    </div>
			  </div>
			</div>
			
</div>