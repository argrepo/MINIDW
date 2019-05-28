<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="<c:url value="/resources/js/jquery-1.12.3.min.js"/>"></script>
<script src="<c:url value="/resources/js/moment-with-locales.js"/>" ></script>
<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
<script src="<c:url value="/resources/js/jquery-ui-1.10.3.custom.min.js"/>"></script>

<script src="<c:url value="/resources/js/js.cookie-1.5.1.js"/>"></script>
<script src="<c:url value="/resources/js/jquery.dataTables.min.js"/>"></script>
<script src="<c:url value="/resources/js/select2.min.js"/>"></script>
<script src="<c:url value="/resources/js/jstz.min.js"/>"></script>

<script src="<c:url value="/resources/js/ie10-viewport-bug-workaround.js"/>"></script>
<script src="<c:url value="/resources/js/responsive-tabs.js"/>"></script>
<script src="<c:url value="/resources/js/bootstrap-multiselect.js"/>"></script>
<script src="<c:url value="/resources/js/multiple-select.js"/>"></script>
<link href="<c:url value="/resources/css/chosen.min.css"/>" rel="stylesheet" />
<script src="<c:url value="/resources/js/chosen.jquery.min.js"/>"></script>
<script src="<c:url value="/resources/js/prettify.js"/>"></script>
<script src="<c:url value="/resources/js/jquery.slimscroll.min.js"/>"></script>
<link href="<c:url value="/resources/css/font-awesome.min.css"/>" rel="stylesheet" />
<script src='<c:url value="/resources/js/dataTables.bootstrap.js"/>'></script>
<link href="<c:url value="/resources/css/bootstrap-datetimepicker.css"/>" rel="stylesheet" />

<script src="<c:url value="/resources/js/bootstrap-datetimepicker.js"/>"></script>

<link href="<c:url value="/resources/css/bootstrap-toggle.min.css"/>" rel="stylesheet" />
<script src="<c:url value="/resources/js/bootstrap-toggle.min.js"/>"></script>

<link href="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.6.4/jquery.contextMenu.min.css"/>" rel="stylesheet" />
<script src="<c:url value="https://cdnjs.cloudflare.com/ajax/libs/jquery-contextmenu/2.6.4/jquery.contextMenu.min.js"/>"></script>


<!-- include messages defined in .js files -->

<c:if test="${empty loginError}">
<%@ include file="scripts_messages.jsp" %>
</c:if>
<% 
		String url = "";
		if (request.getContextPath() != null && request.getContextPath().toString().equals("") ) {
			url = "/";
		} else {
			url = request.getContextPath();
		}
	
	%>
<script>
var adt = {
		
	appContextPath: "<%=url%>", 
	contextPath: "<%=url%>", 
};
var selectedLocalePath = '<c:url value="/resources/js/data-table-i18n/dataTables.${selectedLocale}.lang"/>';
	function showAjaxLoader(val) {
		if (val) {
			$('.loader').fadeIn('fast');
			$('body').addClass('cursor-wait');
		}
		else {
			$('.loader').fadeOut('fast');
			$('body').removeClass('cursor-wait');
		}
	} 
	<%-- to display alerts --%>
	function showAlert(content) {
		$("#messagecontanier").modal('show').find("#msgcontent").append(content);
	}
	<%-- Attaching hidden event to clear message content, when we click on Ok button. --%>
	(function() {
		$("#messagecontanier").on('hidden.bs.modal', function() {
			$(this).find("#msgcontent").html('');
		});
	})();
	 
	(function() {
		
		$(".changelogBtns").click(function(e){
			e.preventDefault();
			
			var userID = $("#userID").val();
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			
			showAjaxLoader(true);
			var url_getTableStructure = "/app/user/"+userID+"/package/getChangeLogDetails";
			   var myAjax = common.loadAjaxCall(url_getTableStructure,'GET','',headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	
			    	if(result != null && result.hasMessages ){
			    		
			    		if(result.messages[0].code == "SUCCESS"){
			    			$("#releaseNotesDetails").html(result.object);
			    			$("#releaseNotesModel").modal('show');
				    	} else {
					    		common.displayMessages(result.messages);
				    	}
			    	} else {
			    		var messages = [ {
			    			code : globalMessage['anvizent.message.error.code'],
							text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
						} ];
			    		common.displayMessages(messages);
			    	}
			    });
		
			
		});
		
	    $(document).on("keypress" , ".m-colsize, .m-decimal",function(e){
	    	if ( (e.keyCode > 47 && e.keyCode < 58) || e.keyCode ==8 ) 
	    		return true;
	    	
	    	return false;
	    });
	    
	    $("#loginForm").submit(function( event ) {
	    	$("button","#loginForm").prop("disabled","disabled"); 
			showAjaxLoader(true);
		});
	    
	    $(".startLoader").click(function( event ) {
			showAjaxLoader(true);
		});


	    $(document).on("click","textarea[readonly='readonly']", function(){
	    	if($(this).val() != ""){
	    		$(this).select();
		    	var status = document.execCommand('copy');
		    	$(this).parent().find("span").remove();
		    	if(status){
		    		var elm = $('<span class="help-block text-right" style="font-size:12px;color:#22791d;">Copied to clipboard!</span>');
		    		elm.insertAfter($(this));
		    		elm.fadeOut(5000)
		    	}
	    	}
	    });
	})();
	
	var popOverSettings = {
		    placement: 'right',
		    container: 'body',
		    html: true,
		    selector: '[data-toggle="popover"]', //Sepcify the selector here
		    trigger : 'focus',
		}
	
		$(function() {
	 	    $( "#defaultTabs" ).tabs();
	 	    $("#sourceServerClientTabs" ).tabs();
	 	    $("#propertiesTabs").tabs();
	 		$("#destinationServerClientTabs" ).tabs();
 	  	});

		$('body').popover(popOverSettings);
		
		String.prototype.encodeHtml = function() {
		var tagsToReplace = {
		'&': '&amp;',
		'<': '&lt;',
		'>': '&gt;'
		};
		return this.replace(/[&<>]/g, function(tag) {
		return tagsToReplace[tag] || tag;
		});
		};

		String.prototype.decodeHtml = function() {
		var tagsToReplace = {
		'&amp;': '&',
		'&lt;': '<',
		'&gt;': '>'};
		return this.replace(/[&<>]/g, function(tag) {
		return tagsToReplace[tag] || tag;
		});
		};
		
		/* try {
			var $container = $("#navbar");
			var $scrollTo = $('.activemenu_link');
			$container.animate({scrollTop: $scrollTo.offset().top - $container.offset().top + $container.scrollTop(), scrollLeft: 0},300);
		} catch (e) {
			// TODO: handle exception
		} */
		
</script>