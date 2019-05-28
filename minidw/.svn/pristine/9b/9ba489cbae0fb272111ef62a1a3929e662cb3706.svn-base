var headers = {};
var  clientTableScripts = {
		initialPage : function() {
			console.log("in table scrits")
			if ( $("select#clientId").length != 0 ) {				
				$("#clientId").select2({               
					allowClear: true,
					theme: "classic"
				});
			}
			
			if ( $("#previousExecutedScripts").length ) {
				$("#previousExecutedScripts").DataTable();
			}
			
            var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		},
		
	   
	   tableScriptsMappingValidation : function(){
		   	  common.clearValidations(["#clientId, #schemaName, #sqlScript, #scriptDescription"]);
	          var client_id = $("#clientId option:selected").val();
	          var scriptDescription =  $("#scriptDescription").val();
	      	  var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/
	      	  var schemaList = $("#schemaName option:selected").val();
	      	  var sqlScript = $("#sqlScript").val();
	      	  var validStatus=true;
	      	   
      	    	if(client_id == '' || client_id == 0 ){
	      	    	common.showcustommsg("#clientId", globalMessage['anvizent.package.label.pleasechooseclientid'],"#clientId");
	      	    	validStatus=false;
      	    	}
      	    	if(schemaList == '' || schemaList == 'select' ){
	  	      		common.showcustommsg("#schemaName", globalMessage['anvizent.package.label.pleasechoosetargetschema'],"#schemaName");
	  	      		validStatus=false;
  	      	  	}
      	    	if(sqlScript == ''){
  	      		  	common.showcustommsg("#sqlScript", globalMessage['anvizent.package.label.pleasechoosesqlscript'],"#sqlScript");
  	      		  	validStatus=false;
  	      	  	}
      	    	if(scriptDescription == ''){
   	      		  	common.showcustommsg("#scriptDescription", globalMessage['anvizent.package.label.PleaseChoosedescription'],"#scriptDescription");
   	      		  	validStatus=false;
   	      	  	}
      	    	else if(!regex.test(scriptDescription)){
	   	      		common.showcustommsg("#scriptDescription", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#scriptDescription");
	   	      		validStatus=false;
   	      	  	}
      	    	
	      	    return validStatus;	
	          },
	          
	          viewTableScriptExecutionError: function(object) {
	        	  
	        		$("#tableScriptExecutionError").empty().val(object);
	        		
	        		if(object === "" || object === null){
	        			$("#tableScriptExecutionError").val("No Error Found.");
	        		}
	        		
	        		$("#viewTableScriptExecutionErrorPopUp").modal('show');
	        	},
	           
		 
	}

if($('.clientTableScriptExecution-page').length){
 
	
	clientTableScripts.initialPage();
	
	$("#tableScriptsMappingTable").DataTable({
		"order": [[ 0, "desc" ]],
	});
	
	$("#client_id").removeAttr("multiple");
	$("#schemaList").removeAttr("multiple");
	$("#scriptType").removeAttr("multiple");
	
	var popup = null;
	$(document).on('click', '#viewSqlScript', function(){
		
		var scriptId = $(this).attr("data-sid"); 
		
		var userID = $("#userID").val();
		
		var url_getScript = "/app_Admin/user/"+userID+"/etlAdmin/getTableScriptView";
		var selectData = {
				id : scriptId
		};
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
		console.log("selectData  -- > ", selectData)
		 showAjaxLoader(true);
		   var myAjax = common.postAjaxCall(url_getScript,'POST', selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    		  if(result != null && result.hasMessages){
		    			  if(result.messages[0].code == "SUCCESS") {
								 var  messages=[{
									  code : result.messages[0].code,
									  text : result.messages[0].text
								  }];
								 var sqlScript = result.object.sqlScript;
									
								 
								 if(sqlScript === "" || sqlScript === null){
									 sqlScript = "No Query Found.";
								} 
									
								 var params = "";
								 	var ua = window.navigator.userAgent;
								    var msie = ua.indexOf("MSIE ");
								    
								    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))  // If Internet Explorer, return version number
								    {
								    	params = [
									              //'height='+screen.height,
									              'width='+screen.width,
									              'fullscreen=no' // only works in IE, but here for completeness
									          ].join(',');
								    } else {
								    	params = [
									              'height='+screen.height,
									              'width='+screen.width,
									          ].join(',');
								    }
							          popup = window.open('about:blank', '_blank', params); 
							          popup.moveTo(0,0);
							          popup.document.title = "Table script";
							          popup.document.body.innerHTML = "<pre>"+sqlScript+"</pre>";
							          if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
							        	  popup.addEventListener (
								        	        "load",
								        	        function () {
								        	            var destDoc = popup.document;
								        	            destDoc.open ();
								        	            destDoc.title = "Table script";
								        	            destDoc.write ('<html><head></head><body><pre>'+sqlScript+'</pre></body></html>');
								        	            destDoc.close ();
								        	        },
								        	        false
								        	    );
							          }

							          
							          
			    			  }else {
						    		common.displayMessages(result.messages);
						    	}
		    		  }else{
		    			  standardPackage.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });
	});
	
	
	var popup = null;
	$(document).on('click', '#viewScript', function(){
		
		var scriptId = $(this).attr("data-sid"); 
		var scriptClientID= $("#clientId option:selected").val();
		var userID = $("#userID").val();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url_getScript = "/app_Admin/user/"+userID+"/etlAdmin/getPreviousTableScriptView";
		var selectData = {
				id : scriptId,
				clientId:scriptClientID
		};
		 showAjaxLoader(true);
		   var myAjax = common.postAjaxCall(url_getScript,'POST', selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    		  if(result != null && result.hasMessages){
		    			  if(result.messages[0].code == "SUCCESS") {
								 var  messages=[{
									  code : result.messages[0].code,
									  text : result.messages[0].text
								  }];
								 var sqlScript = result.object;
									
								 
								 if(sqlScript === "" || sqlScript === null){
									 sqlScript = "No Query Found.";
								} 
									
								 var params = "";
								 	var ua = window.navigator.userAgent;
								    var msie = ua.indexOf("MSIE ");
								    
								    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))  // If Internet Explorer, return version number
								    {
								    	params = [
									              'width='+screen.width,
									              'fullscreen=no' // only works in IE, but here for completeness
									          ].join(',');
								    } else {
								    	params = [
									              'height='+screen.height,
									              'width='+screen.width,
									          ].join(',');
								    }
							          popup = window.open('about:blank', '_blank', params); 
							          popup.moveTo(0,0);
							          popup.document.title = "Table script";
							          popup.document.body.innerHTML = "<pre>"+sqlScript+"</pre>";
							          if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
							        	  popup.addEventListener (
								        	        "load",
								        	        function () {
								        	            var destDoc = popup.document;
								        	            destDoc.open ();
								        	            destDoc.title = "Table script";
								        	            destDoc.write ('<html><head></head><body><pre>'+sqlScript+'</pre></body></html>');
								        	            destDoc.close ();
								        	        },
								        	        false
								        	    );
							          }

							          
							          
			    			  }else {
						    		common.displayMessages(result.messages);
						    	}
		    		  }else{
		    			  standardPackage.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });
	});
	 
	 
	
	
	
	$(document).on('click', '.viewErrorScripts', function(){
		 
	     var scriptId = $(this).attr("data-sid"); 
	     var scriptClientID= $("#clientId option:selected").val();
	     var userID = $("#userID").val();

		var selectData = {
				id : scriptId,
				clientId:scriptClientID
		};
	     var url_errorMsg = "/app_Admin/user/"+userID+"/etlAdmin/getInstantTableScriptsIsNotExecutedErrorMsg";
	 	var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
     		showAjaxLoader(true);
		    var myAjax = common.postAjaxCall(url_errorMsg,'POST', selectData,headers);
		     myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    	  if(result != null){
	    		  if(result.hasMessages){
	    			  if(result.messages[0].code == "ERROR") { 
	    				  common.showErrorAlert(result.messages[0].text);
	    				  return false;
	    			  } else if(result.messages[0].code == "SUCCESS"){ 
	    				  clientTableScriptExecution.viewTableScriptExecutionError(result.object); 
	    			     }
	    		       }
		    	       }
		    	  else{ 
		    		  common.showErrorAlert(result.messages[0].text)
		    		  return false;
		    	  }
		     });
		    });
   
	

	
	 function someFunctionToCallWhenPopUpCloses() {
		    window.setTimeout(function() {
		    	window.setTimeout(function() {
		        	  if (popup.closed) {
			        	  showAjaxLoader(false);
			          }
		          }, 1);
		    }, 1);
		}

	 
	 $("#executeScripts").on('click', function() {
		 
		    var status= clientTableScripts.tableScriptsMappingValidation();
			if(!status){ return false;}
			else{
			$("#tableScriptsForm").prop("action",$("#executeScriptsUrl").val()); 
			this.form.submit();
		    showAjaxLoader(true);
			}
	 });
	 
	 $("#showPreviousExecutedScripts").on('click', function() {
		 
			$("#tableScriptsForm").prop("action",$("#previousExecuteScriptsUrl").val()); 
			this.form.submit();
		    showAjaxLoader(true);
		    
});
	 
	 if ( $('.clientTableScriptInstantExecution-page').length ) {
		 $("#clientId").change(function(){
			 var currValue = this.value;
			 if (currValue != '0') {
				 $("#showPreviousExecutedScripts").hasClass("hidden") ? $("#showPreviousExecutedScripts").removeClass("hidden"): "";
			 } else {
				 $("#showPreviousExecutedScripts").hasClass("hidden") ? "": $("#showPreviousExecutedScripts").addClass("hidden");
				 
			 }
		 });
		 $("#clientId").trigger("change");
	 }
	 
}
