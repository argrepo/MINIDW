var headers = {};
var viewValidationScriptResults = {
		initialPage : function() {
			$("#viewScriptResultsTable").DataTable( {
		        "order": [[ 2, "desc" ]],"language": {
	                "url": selectedLocalePath
	            }
		    });
			$("#errorLogsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
			
	}
		
}

if($('.viewValidationScriptResults-page').length){
	viewValidationScriptResults.initialPage();
	
	$(document).on('click','.view-error-log',function(){	
		var batchId = $(this).attr('data-batchid');
		var userID = $("#userID").val(); 
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true); 
		var url =  "/app_Admin/user/"+userID+"/etlAdmin/viewScriptValidationErrorLogs/"+batchId+"";
		var myAjax = common.loadAjaxCall(url,'GET','',headers);
	    	myAjax.done(function(result) {
			    showAjaxLoader(false);
		    if(result != null && result.hasMessages){
		    	if(result.messages[0].code == "SUCCESS") {
		    	var table = result["object"];
		    	
		    	var errorLogTable = $("#errorLogsTable").DataTable();		    	
		    	errorLogTable.clear();
	    		
		    	var l = table.length;
		    	
		    	for(var i=0;i<l;i++){
		    		var t = table[i];		    		
		    		var row = [];					  
		    		 
		    		row.push(t['errorId']);
		    		row.push(t['batchId'] ? t['batchId'].encodeHtml() :'');
		    		row.push(t['errorCode']);
		    		row.push(t['errorType'] ? t['errorType'].encodeHtml() :'');
		    		row.push(t['errorMessage'] ? t['errorMessage'].encodeHtml() :'');
		    		row.push(t['errorSyntax']);
		    		
		    		errorLogTable.row.add(row);
		    	}
		    	
		    	errorLogTable.draw(true); 
    			var popup = $("#viewErrorLogs");	  		  	
	  		  	popup.modal('show');
		    	}else{
		    		common.displayMessages(result.messages);
		    	}
		    }else{
	    		var messages = [ {
	    			code : globalMessage['anvizent.message.error.code'],
	    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
	    		} ];
	    		common.displayMessages(messages);
	    	}
	    	});
	});
	
	$(document).on('click','#downloadBatchFiles',function(){		 
		var batchId = $(this).attr('data-batchid');
		var userID = $("#userID").val(); 
		var isFileDownload = false;
		showAjaxLoader(true); 
		var url =  "/app_Admin/user/"+userID+"/etlAdmin/downloadBatchResults/"+batchId+"/"+isFileDownload;
		var myAjax = common.loadAjaxCall(url,'GET','',headers);
	    	myAjax.done(function(result) {
			    showAjaxLoader(false);
		     if(result != null && result.hasMessages){
		    	if(result.messages[0].code == "SUCCESS") {
		    		isFileDownload = true;
		    		window.open(adt.contextPath+"/app_Admin/user/"+userID+"/etlAdmin/downloadBatchResults/"+batchId+"/"+isFileDownload);
		    	}else{
		    		common.displayMessages(result.messages);
		    	}
		    }else{
	    		var messages = [ {
	    			code : globalMessage['anvizent.message.error.code'],
	    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
	    		} ];
	    		common.displayMessages(messages);
	    	}
	    	});
	});
}