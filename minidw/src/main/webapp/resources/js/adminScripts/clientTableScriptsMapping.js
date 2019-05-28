var tableScriptsMappingDataTable;
var headers = {};
var  clientTableScriptsMapping = {
		
		initialPage : function() {
			tableScriptsMappingDataTable = $("#tableScriptsMappingTable").DataTable({
				"order": [[ 1, "desc" ]],"language": {
	                "url": selectedLocalePath
	            }
			});
			$("#tableScriptsExecutedTable").DataTable({
				"order": [[ 1, "desc" ]],"language": {
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
		      	    	 common.showcustommsg("#clientId",globalMessage['anvizent.package.label.pleasechooseclientid'],"#clientId");
		      	    	 validStatus=false;
	      	    	}
	      	  
	      	    	var tablerows = $('#tableScriptsMappingTable').find("tbody").find("tr").filter(function() {
	      		    	return $(this).find('.tableScriptsInfoById').is(':checked');
	      		    });
	      		    
	      		    if (!tablerows || tablerows.length === 0) {
	      		        $('.tableScriptsValidation').empty();
	    				var  message = '<div class="alert alert-danger alert-dismissible" role="alert">'+'Please choose table scripts.'+' <span aria-hidden="true"></span></div>';
	    			    $('.tableScriptsValidation').append(message);
	    			    setTimeout(function() { $(".tableScriptsValidation").hide().empty(); }, 5000);
	      		    	 validStatus=false;
	      		    } 
	      		   $('#tableScriptsMappingTable').find("tbody").find("tr").filter(function() {
	      		    	 var checkedStatus =  $(this).find('.tableScriptsInfoById').is(':checked');
	      		    	 if(checkedStatus){
	      		    		var tableScriptPriority = $(this).find('.tableScriptPriority').val();
	      		    		if(tableScriptPriority == "" || tableScriptPriority == 0){
	      		    		var id=	$(this).find('.tableScriptPriority').attr("id");
	      		    			common.showcustommsg("#"+id, globalMessage['anvizent.package.label.pleasechoosepriority'], "#"+id);
	      		    			validStatus=false;
	      		    		}
	      		    	 }
	      		    });
	      		  
	      		 $('#tableScriptsMappingTable').find("tbody").find("tr").filter(function() {
   		    	    var tableScriptPriority =  $(this).find('.tableScriptPriority').val();
   		    		var checkedStatus =  $(this).find('.tableScriptsInfoById').is(':checked');
   		    		if( checkedStatus == false && tableScriptPriority != 0){
   		    	    	var id=	$(this).find('.tableScriptsInfoById').attr("id");
   		    			common.showcustommsg("#"+id, globalMessage['anvizent.package.label.pleasechoosetablescripts'], "#"+id);
   		    			validStatus=false;
   		    		   }
   		           });
	      	      
	      	    var values = [];
	      	    $('#tableScriptsMappingTable').find("tbody").find("tr").filter(function() {
  		    	    var tableScriptPriority =  $(this).find('.tableScriptPriority').val();
  		    		values.push(tableScriptPriority)
  		    	    
  		           });
	      	  var sorted_arr = values.sort();   
	          var results = []; 
	          for (var i = 0; i < values.length - 1; i++) {  
	              if (sorted_arr[i + 1] == sorted_arr[i]) {  
	                  results.push(sorted_arr[i]);  
	              }  
	          }  
	      	        return validStatus;	
	          },
	}

if($('.clientTableScriptsMapping-page').length){
 
	clientTableScriptsMapping.initialPage();
	
	// add multiple select / deselect functionality
	$(document).on('click', '#selectall', function(){
		var tableScriptsMappingTable = $('#tableScriptsMappingTable').DataTable();
		var allPages = tableScriptsMappingTable.cells().nodes();
		if($(this).is(":checked")){
		$('.tableScriptsInfoById').each(function(){
		if(!$(this).is(":disabled")){
		$(allPages).find('input[type="checkbox"]').prop('checked', true);
		$(this).parents("#tableScriptsRow").find(".tableScriptPriority").val(1);
		}
		});
		}
		else{
		$('.tableScriptsInfoById').each(function(){
		if(!$(this).is(":disabled")){
		$(allPages).find('input[type="checkbox"]').prop('checked', false);
		$(this).parents("#tableScriptsRow").find(".tableScriptPriority").val(0);
		}
		});
		}

		});
	
	$(document).on("click",".tableScriptsInfoById",function(){
		
		if($(this).is(":checked")){
			$(this).parents("#tableScriptsRow").find(".tableScriptPriority").val(1);
		}else{
			$(this).parents("#tableScriptsRow").find(".tableScriptPriority").val(0);
		}
	});
	
	// if all checkbox are selected, check the selectall checkbox and viceversa
	$(document).on('click', '.checkCase', function(){
		 
		if($(".tableScriptsInfoById").length == $(".tableScriptsInfoById:checked").length){
			$(".tableScriptsInfoById").parents("table").find("#selectall").prop("checked",true);
		}else{
			$(".tableScriptsInfoById").parents("table").find("#selectall").prop("checked",false);
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
	      	    	 common.showcustommsg("#clientId", globalMessage['anvizent.package.label.pleasechooseclientid'],"#clientId");
	      	    	return false;
     	    	}
     	  
				$("#tableScriptsForm").prop("action",$("#getUrl").val()); 
				this.form.submit();
			    showAjaxLoader(true);
    });
	 
	 if ( $("#clientId").length > 0 ) {
			if ( $("#clientId").children().length > 1 &&  $("#clientId").val() == '0') {
				$("#clientId").val($("#clientId option").eq(1).val())
				$("#clientId").change();
			}
	}
	 
	 $("#saveMapping").on('click', function() {
		 
		    var status= clientTableScriptsMapping.tableScriptsMappingValidation();
			if(!status){ return false;}
			else{
			$("#tableScriptsForm").prop("action",$("#saveMappingUrl").val()); 
			tableScriptsMappingDataTable.destroy();
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
