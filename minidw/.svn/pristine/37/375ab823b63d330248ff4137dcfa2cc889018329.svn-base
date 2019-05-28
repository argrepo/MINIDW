<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="clearfix"></div>
<div id ="pageErrors">
	
	<c:if test="${not empty errors}">
		<c:choose>
			<c:when test="${not empty messagecode}">
				<c:if test="${messagecode == 'SUCCESS'}">
					<div class="alert alert-success alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
			        <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		   	         </button>
	    	        <div>${errors}</div>
					</div>
				</c:if>
				<c:if test="${messagecode == 'FAILED' || messagecode == 'ERROR'}">
					<div class="alert alert-danger alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
			        <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		   	         </button>
	    	        <div>${errors}</div>
					</div>
				</c:if>
			</c:when>
			<c:otherwise>
				
			</c:otherwise>
		</c:choose>
			
	    	</c:if>
		</div>
		
		<div class="modal fade" tabindex="-1" role="dialog" id="releaseNotesModel" data-backdrop="static" data-keyboard="false">
		  <div class="modal-dialog" style="width:65%;"> 
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <img src="<c:url value="/resources/images/anvizent_icon.png" />" class="anvizentLogo"/> 
		        <h4 class="modal-title custom-modal-title">Change log</h4>
		      </div>
		      <div class="modal-body" style="max-height: 400px; overflow-y: auto; overflow-x: hidden;">
		        	<div>
		        		<pre id="releaseNotesDetails" style="overflow: auto; max-height: 300px;">
		        				    
		        		</pre>
					</div>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="anvizent.package.button.close"/></button>
		      </div>
		    </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div>
 