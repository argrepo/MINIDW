var headers = {};
var  viewClientScriptTableResultsTable  = null; 
var clientsInstantScriptExecution = {
	initialPage : function() {
		$("#clientInstanceScriptExecutionTable").dataTable({
	        "order": [[ 0, "desc" ]]
	    });
		$(".clientId").multipleSelect({
			filter : true,
			placeholder : 'Select Client Ids',
			enableCaseInsensitiveFiltering : true
		});
		viewClientScriptTableResultsTable  = $("#viewClientScriptTableResultsTable").DataTable({
	        "order": [[ 0, "desc" ]],"language": {
                "url": selectedLocalePath
            }
	    });
		setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
	},
	showMessage:function(text){
		$(".messageText").empty();
		$(".successMessageText").empty();
		$(".messageText").html(text);
	    $(".message").show();
	   setTimeout(function() { $(".message").hide(); }, 10000);
 },
	validateClientInstantScriptExecution : function(){
		
		common.clearValidations([".clientId","#sqlScript"]);
		var validStatus  = true;
		var sqlScript=$("#sqlScript").val();
		
		 if($("select.clientId option:selected").length == 0){
				common.showcustommsg("div.clientId", globalMessage['anvizent.package.label.selectClientIds']);
				validStatus=false; 
			}
		  if(sqlScript == ''){
      		  common.showcustommsg("#sqlScript", globalMessage['anvizent.package.label.pleasechoosesqlscript'],"#sqlScript");
      	      validStatus=false;
      	  }
		return validStatus;
	},
	viewCLientScriptExecutionPopUp : function(result){
		viewClientScriptTableResultsTable.clear();
		 if(result != null){				
			 for (var i = 0; i < result.object.length; i++) {
				 var clientInstantExecutionId = result.object[i].clientInstantExecutionId;
				 var clientId = result.object[i].clientId;
				 var executionMessage =  result.object[i].execution_status_msg;
				 var row = [];
				 
				 row.push(i+1);
				 row.push(clientInstantExecutionId);
				 row.push(clientId);
				 row.push("<pre>"+executionMessage+"</pre>");
				
				 viewClientScriptTableResultsTable.row.add(row);
			}
			 viewClientScriptTableResultsTable.draw(true);
			 $("#viewDDlTableResultsHeader").empty().text("Audit Log");
			 $("#viewCLientScriptExecutionPopUp").modal('show');
		}
	},
	
	viewSqlScript :  function(result){
		  if(result.messages[0].code == "SUCCESS") {
				 var  messages=[{
					  code : result.messages[0].code,
					  text : result.messages[0].text
				  }];
				 var sqlScript = result.object;
					
				 console.log("sqlScript:",sqlScript);
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
				        	            destDoc.title = "DD Layout";
				        	            destDoc.write ('<html><head></head><body><pre>'+sqlScript+'</pre></body></html>');
				        	            destDoc.close ();
				        	        },
				        	        false
				        	    );
			          }

			          
			          
			  }else {
		    		common.displayMessages(result.messages);
		    	}
	  
	}
	
};

if ($('.clientsInstantScriptExecution-page').length) {
	clientsInstantScriptExecution.initialPage();

$('#executeScripts').on('click', function() {
	var status= clientsInstantScriptExecution.validateClientInstantScriptExecution();
	
	if(!status){ return false;
	}else{
		$("#tableScriptsForm").prop("action", $("#executeScriptsUrl").val());
		this.form.submit();
		showAjaxLoader(true);
	}
});

	    $(document).on('click', '#viewClientInstantExecutionScript', function(){
			var clientsInstantScriptId = $(this).attr("data-clientsInstantScriptId"); 
			var userID = $("#userId").val();
			var url_getScript = "/app_Admin/user/"+userID+"/etlAdmin/instantScriptExcecution/viewSqlScriptByExecutionId";
			var selectData = {
					clientsInstantScriptId : clientsInstantScriptId
			};
			 
			var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
	 		 
			 showAjaxLoader(true);
			   var myAjax = common.postAjaxCall(url_getScript,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    		  if(result != null && result.hasMessages){
			    			  clientsInstantScriptExecution.viewSqlScript(result); 
			    		  }else{
			    			  clientsInstantScriptExecution.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
			    		  }
			    });
		});
	    
		$(document).on('click', '#viewClientInstantExecutionResults', function(){
			var clientsInstantScriptId = $(this).attr("data-clientsinstantscriptid"); 
			var userID = $("#userId").val();
			var url_getScript = "/app_Admin/user/"+userID+"/etlAdmin/instantScriptExcecution/viewClientInstantExecutionResults";
			var selectData = {
					clientsInstantScriptId : clientsInstantScriptId
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
			    				  clientsInstantScriptExecution.viewCLientScriptExecutionPopUp(result);
			    			  }else {
							    		common.displayMessages(result.messages);
							    	}
			    		  }else{
			    			  clientsInstantScriptExecution.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
			    		  }
			    });
		});
};
