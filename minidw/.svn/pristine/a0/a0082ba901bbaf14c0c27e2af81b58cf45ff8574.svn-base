var headers = {};
var clientError = { 
		initialPage : function(){
			$("#errorLogsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
			$(".s3AuditLogsTable").DataTable({
				"pageLength" : 100,"language": {
	                "url": selectedLocalePath
	            }
			});
			$(".s3SourcesAuditLogsTable").DataTable({
				"pageLength" : 100,
				"order": [[ 3, "desc" ]],
				"language": {
	                "url": selectedLocalePath
	            }
			});
			$("#from").datepicker();
			$("#to").datepicker();
		},
		
		showClientErrorBody : function(result){
			var error_body = result.object.errorBody;
			$("#viewErrorBodyPopUp").find(".error-body").empty().append($("<pre>").html(error_body));
			$("#viewErrorBodyPopUp").modal('show');
		},
		updateTableWithSearchedErrorLog : function(result){
			var table = $("#errorLogsTable").DataTable();			
			table.clear();
			 for (var i = 0; i < result.object.length; i++) {	
				 	var id = result.object[i].id;
				    var errorCode = result.object[i].errorCode;
				    var errorDatetime = result.object[i].errorDatetime;
				    var receivedParameters = result.object[i].receivedParameters;
				    var clientDetails = result.object[i].clientDetails;
				    var errorCodeHtml = "<button class='getErrorBody btn btn-primary btn-sm tablebuttons' data-errorId='"+id+"' style='white-space:normal;width:200px;cursor:auto;'>" +
				    		"<span style='cursor:pointer;'>"+errorCode+"</span></button>";
				    var row = [];
				    row.push(i+1);
				    row.push(errorCodeHtml);
				    row.push(errorDatetime);
				    row.push(receivedParameters);
				    row.push(clientDetails);
				    
				    table.row.add(row);
			 }	
			 table.draw(true);
		},
		validateSearchCriteria : function(){
			var status = true;
			common.clearcustomsg("#searchCriteriaValidation");
			var error_code = $("#error_code").val();
			var request_param =$("#request_param").val();
			var client_Details = $("#client_Details").val();
			if(error_code == "" && request_param == "" && client_Details == ""){
				common.showcustommsg("#searchCriteriaValidation", "Provide atleast one search criteria.");
				status = false;
			}			
			return status;
		},
}


if($('.clientErrorLogs-page').length || $('.s3AuditLogs-page').length){
	clientError.initialPage();
	
	 
	 
	 
	//Events
	 $(document).on("click","#searchErrorLog",function(){
		    var userId = $("#userID").val();
		    var errorCode ="%"+$("#error_code").val()+"%";
		    var receivedParameters = "%"+$("#request_param").val()+"%";
		    var clientDetails = "%"+$("#client_Details").val()+"%";
		    var errorlog = {    		
		    		errorCode : errorCode,
		    		receivedParameters : receivedParameters,
		    		clientDetails : clientDetails
		    }
		    var status = clientError.validateSearchCriteria();
		    if(!status){
		    	return false;
		    }
		    var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		    var url_getErrorLog = "/app_Admin/user/"+userId+"/etlAdmin/searchClientErrorLog";
		    var myAjax = common.postAjaxCall(url_getErrorLog,'POST', errorlog,headers,headers);
		    
		    showAjaxLoader(true);    
			myAjax.done(function(result) {
				showAjaxLoader(false);
				clientError.updateTableWithSearchedErrorLog(result);
			});
		   
		});
	 
	 $(document).on("click",".getErrorBody",function(){
		 var errorId = $(this).attr("data-errorId");		
		 var userId = $("#userID").val();
		 
		 var errorLog = {
				 id : errorId
		 }
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
	 	var url_getErrorLogById = "/app_Admin/user/"+userId+"/etlAdmin/getClientErrorLogById";
	 	showAjaxLoader(true); 
	    var myAjax = common.postAjaxCall(url_getErrorLogById,'POST', errorLog,headers);
		myAjax.done(function(result) {
			showAjaxLoader(false);
			clientError.showClientErrorBody(result);
		}); 
	 });

	 
}
			
