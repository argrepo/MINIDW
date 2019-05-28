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
				$("#previousExecutedScripts").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
			}
			
            var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		},
		tableScriptsCreationValidation : function(){
			  var client_id=$("#clientId option:selected").val();
	          var scriptName=$("#scriptName").val();   
	      	  var sqlScript=$("#sqlScript").val();
	      	  var version=$("#version").val();
	          var client_id=$("#clientId option:selected").val();
	      	  var schemaList = $("#schemaName option:selected").val();
	      	  var scriptType = $("#scriptTypeName option:selected").val();
	      	  var isActive = $("input[name='is_Active']").is(":checked") ? true : false;
	      	  var scriptDescription =  $("#scriptDescription").val();
	      	  
	      	  var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/
	      	    
	      	  common.clearValidations(["#clientId","#scriptTypeName", "#sqlScript","#version","#clientId","#schemaName","#scriptName", ".is_Active","#scriptDescription"]);
	      	  
	      	  var validStatus=true;
	      	  
	      	  if(scriptName == '' ){
	      		  common.showcustommsg("#scriptName",globalMessage['anvizent.package.label.pleasechoosescriptname'],"#scriptName");
	      		  validStatus=false;
	      	  } else if(!regex.test(scriptName)){
	      		  common.showcustommsg("#scriptName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#scriptName");
	      	      validStatus=false;
	      	  }
	      	  if(sqlScript == ''){
	      		  common.showcustommsg("#sqlScript", globalMessage['anvizent.package.label.pleasechoosesqlscript'],"#sqlScript");
	      	      validStatus=false;
	      	  }
	      	  if(scriptType == '' || scriptType == 'select'){
	      		  common.showcustommsg("#scriptTypeName",globalMessage['anvizent.package.label.pleasechoosescripttype'],"#scriptTypeName");
	      		  validStatus=false;
	      	  }
	      	  if(version == ''){
	      		common.showcustommsg("#version",globalMessage['anvizent.package.label.pleaseEnterVersion'],"#version");
	      		  validStatus=false; 
	      	  }else if(!regex.test(version)){
	      		  common.showcustommsg("#version", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#version");
	      	      validStatus=false;
	      	  }
	      	    
	      	  if(scriptType === "Client Specific" || scriptType == 'select' ){
	      		  if(client_id == '' || client_id == 0 ){
	      	    	 common.showcustommsg("#clientId", globalMessage['anvizent.package.label.pleasechooseclientid'],"#clientId");
	      	    	 validStatus=false;
	      		  }
	      	  } 
	      	  if(schemaList == '' || schemaList == 'select' ){
	      		  common.showcustommsg("#schemaName", globalMessage['anvizent.package.label.pleasechoosetargetschema'],"#schemaName");
	      		  validStatus=false;
	      	  }
	      	  if(!isActive){
	      		  common.showcustommsg(".is_Active", globalMessage['anvizent.package.label.PleaseChooseActiveStatus'],".is_Active");
	      		  validStatus=false;
	      	  }
	      	  if(scriptDescription == ''){
	      		  common.showcustommsg("#scriptDescription", globalMessage['anvizent.package.label.PleaseChoosedescription'],"#scriptDescription");
	      		  validStatus=false;
	      	  }  else if(!regex.test(scriptDescription)){
	      		  common.showcustommsg("#scriptDescription", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#scriptDescription");
	      		  validStatus=false;
	      	  }
	      	  return validStatus;	
	      	   
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

if($('.clientTableScripts-page').length || $('.defaultMappingInfo-page').length || $('.clientTableScriptInstantExecution-page').length){
 
	
	clientTableScripts.initialPage();
	
	$("#tableScriptsMappingTable").DataTable({
		"order": [[ 0, "desc" ]],"language": {
            "url": selectedLocalePath
        }
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
   
	

	 $("#addTableScriptsMapping").on('click', function() {
	 
			$("#tableScripts").prop("action",$(this).data("addurl"));
			this.form.submit();
		    showAjaxLoader(true);
		 
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

	 $("#addTableScript").on('click', function() {
		 
		    var selectors = [];
			
	        selectors.push('#scriptName');
	        selectors.push('#scriptDescription');
	        
			var status= clientTableScripts.tableScriptsCreationValidation();
			
			if(!status){ return false;}
			
			var valid = common.validate(selectors);
			
			if(!valid){ return false;}
			
			$("#tableScripts").prop("action",$(this).val()); 
			this.form.submit();
		    showAjaxLoader(true);
			
	 });
	 $("#updateTableScript").on('click', function() {
		 
		   var selectors = [];
			
	        selectors.push('#scriptName');
	        selectors.push('#scriptDescription');
	        
			var status= clientTableScripts.tableScriptsCreationValidation();
			
			if(!status){ return false;}
			
			var valid = common.validate(selectors);
			
			if(!valid){ return false;}
			
			$("#tableScripts").prop("action",$("#updateUrl").val()); 
			this.form.submit();
		    showAjaxLoader(true);
	 });
	
		  
	 $("#scriptTypeName").on('change', function() { 
		 
		 var scriptType = $(this).val();
		 if(scriptType == "select"){
			 common.showErrorAlert(globalMessage['anvizent.package.label.pleasechoosescripttype']);
			 return false;
		 }
		 if(scriptType == 'Default'){
			  $("#clientListId").hide();
		 }else{
			 $("#clientListId").show();
		 }
		 
	 });
	 
	 $(document).on('click', '#resetTableScript', function(){
		 common.clearValidations(["#scriptTypeName, #sqlScript,#scriptDescription,#version,#clientId,#schemaName,#scriptName,.is_Active"]);
		   $("#scriptTypeName").val("select");
     	   $("#sqlScript").val("");
     	   $("#version").val("");
           $("#clientId").val("0");
     	   $("#schemaName").val("select");
     	   $("#scriptName").val("");
     	   $("#is_Active2").attr("checked", false);
     	   $('#is_Active1').attr("checked", false);
     	   $("#scriptDescription").val("");
		   
	 });
	 
	 if ( $("select#clientId").length && $("#scriptTypeName").val() != 'select') {
		 $("#scriptTypeName").change();
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
	 
	 if ( $("#clientId").length > 0 ) {
			if ( $("#clientId").children().length > 1 &&  $("#clientId").val() == '0') {
				$("#clientId").val($("#clientId option").eq(1).val())
				$("#clientId").change();
			}
	}
	 
}
