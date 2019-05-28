<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:htmlEscape defaultHtmlEscape="true" />
<div class="col-sm-12 rightdiv">      
       <ol class="breadcrumb">
		
		</ol>
<jsp:include page="_error.jsp"></jsp:include>
<c:url value="/adt/package/saveSample/save" var="url"/>
      <input type="hidden" id="userID" value="<c:out value="${principal.userId}"/>">
      <div class="row form-group">
      
      	
      </div>
      <div>
      <h1>hiii</h1>
      <form:form modelAttribute="sampleForm" method="GET" action="${url}" >
      <table>
      <tr>
      <td>
      userName:<input type="text" name="name">
      age: <input type="text" name="age">
      </td>
      </tr>
      <tr><td>
      <input type="submit" value="click here">
      </td></tr>
      </table>
      </form:form>
      
      </div>
      </div>