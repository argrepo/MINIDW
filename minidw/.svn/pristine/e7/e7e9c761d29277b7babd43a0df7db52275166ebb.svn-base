var headers = {};
var  clientTableScriptExecution = {
		initialPage : function() {
			$("#tableScriptsMappingTable").DataTable({
				"order": [[ 0, "desc" ]],
				"pageLength" : 100,"language": {
	                "url": selectedLocalePath
	            }
			});
			
			$("#clientId").select2({               
                allowClear: true,
                theme: "classic"
			}); 
			setTimeout(function() { $("#pageErrors").hide(); }, 5000);
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		},
		tableScriptsMappingValidation : function(){
	           
	          var client_id=$("#clientId option:selected").val();
	      	  common.clearValidations(["#clientId"]);
	      	 
	      	  var validStatus=true;
	      	   
	      	    	if(client_id == '' || client_id == 0 ){
		      	    	 common.showcustommsg("#clientId", globalMessage['anvizent.package.label.pleasechooseclientid'],"#clientId");
		      	    	 validStatus=false;
	      	    	}
	      	  
	      	    	var tablerows = $('#tableScriptsMappingTable').find("tbody").find("tr").filter(function() {
	      		    	return $(this).find('.tableScriptsInfoById').is(':checked');
	      		    });
	      		    
	      		    if (!tablerows || tablerows.length === 0) {
	      		        $('.tableScriptsValidation').empty();
	    				var  message = '<div class="alert alert-danger alert-dismissible" role="alert">'+globalMessage['anvizent.package.label.pleasechoosetablescripts']+' <span aria-hidden="true"></span></div>';
	    			    $('.tableScriptsValidation').append(message);
	    			    setTimeout(function() { $(".tableScriptsValidation").hide().empty(); }, 5000);
	      		    	 validStatus=false;
	      		    } 
	      		  var notExecuted = $('#tableScriptsMappingTable').find("tbody").find("tr").filter(function() {
	      			   if($(this).find('.tableScriptsExecutedOrNot').val() === 'false'){
	      				 if( $(this).find('.tableScriptsInfoById').is(':checked') === true){
	      					return $(this).find('.tableScriptsInfoById').is(':checked');
	      				 }
	      			   }
	      		    });
	      		   
	      		    if (!notExecuted || notExecuted.length === 0) {
	      		        $('.tableScriptsValidation').empty();
	    				var  message = '<div class="alert alert-danger alert-dismissible" role="alert">'+globalMessage['anvizent.package.label.pleasechooseatleastonependingscript']+' <span aria-hidden="true"></span></div>';
	    			    $('.tableScriptsValidation').append(message).show();
	    			    setTimeout(function() { $(".tableScriptsValidation").hide().empty(); }, 5000);
	      		    	validStatus=false;
	      		    } 
	      		    
	      	        return validStatus;	
	          },
	           
	          viewTableScriptExecutionError: function(object,scriptName) {
	        		
	        	  
	        		$("#viewTableScriptExecutionErrorHeader").text(scriptName.encodeHtml());
	        		$("#tableScriptExecutionError").empty().val(object.encodeHtml());
	        		
	        		if(object === "" || object === null){
	        			$("#tableScriptExecutionError").val("No Error Found.");
	        		}
	        		
	        		$("#viewTableScriptExecutionErrorPopUp").modal('show');
	        	},
	}

if($('.clientTableScriptExecution-page').length){
 
	clientTableScriptExecution.initialPage();
	
	// add multiple select / deselect functionality
	$(document).on('click', '#selectall', function(){
	 
		if($(this).is(":checked")){
			$('.tableScriptsInfoById').each(function(){
				if(!$(this).is(":disabled")){
					$(this).prop('checked',true);
				}
			});
		}
		else{
			$('.tableScriptsInfoById').each(function(){
				if(!$(this).is(":disabled")){
					$(this).prop('checked',false);
				}
			});
		}
		 
	});
	// if all checkbox are selected, check the selectall checkbox and viceversa
	$(document).on('click', '.checkCase', function(){
		 
		if($(".checkCase").length == $(".checkCase:checked").length) {
			$("#selectall").attr("checked", "checked");
			
		} else {
			$("#selectall").removeAttr("checked");
		}

	});
	
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
 
	 $("#clientId").on('change', function() {
		       var client_id=$(this).val();
		       console.log("client_id:",client_id)
     	       common.clearValidations(["#clientId"]);
     	    	if(client_id == '' || client_id == 0 ){
	      	    	 common.showcustommsg("#clientId", "Please choose client Id","#clientId");
	      	    	return false;
     	    	}
     	  
				$("#tableScriptsForm").prop("action",$("#getUrl").val()); 
				this.form.submit();
			    showAjaxLoader(true);
		 
		
		 
    });
	 
	 $("#executeScripts").on('click', function() {
		 
		    var status= clientTableScriptExecution.tableScriptsMappingValidation();
			if(!status){ return false;}
			else{
			$("#tableScriptsForm").prop("action",$("#executeScriptsUrl").val()); 
			this.form.submit();
		    showAjaxLoader(true);
			}
     });
	 
		 $(document).on('click', '.viewErrorScripts', function(){
			 
			     var scriptId = $(this).attr("data-sid"); 
			     var scriptName = $(this).attr("data-sname"); 
			     var scriptClientID= $("#clientId option:selected").val();
			     var userID = $("#userID").val();
			     
	    		var url_getScript = "/app_Admin/user/"+userID+"/etlAdmin/getTableScriptView";
	    		var selectData = {
	    				id : scriptId,
	    				clientId:scriptClientID
	    		};
	    		var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
			     var url_errorMsg = "/app_Admin/user/"+userID+"/etlAdmin/getTableScriptsMappingIsNotExecutedErrorMsg";
	        	 showAjaxLoader(true);
	  		     var myAjax = common.postAjaxCall(url_errorMsg,'POST', selectData,headers);
	  		     myAjax.done(function(result) {
	  		    	showAjaxLoader(false);
	  		    	console.log("result:",result);
	  		    	  if(result != null){
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") { 
			    				  common.showErrorAlert(result.messages[0].text);
			    				  return false;
			    			  } else if(result.messages[0].code == "SUCCESS"){ 
			    				  clientTableScriptExecution.viewTableScriptExecutionError(result.object,scriptName); 
			    			     }
			    		       }
	  		    	       }
	  		    	  else{ 
	  		    		  common.showErrorAlert(result.messages[0].text)
	  		    		  return false;
	  		    	  }
	  		     });
	  		    });
	        
         }
