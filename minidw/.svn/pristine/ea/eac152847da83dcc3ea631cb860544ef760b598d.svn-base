var headers = {};
var errorLogsTable = null;
var viewResults = {
		initialPage : function() {
			$("#viewResultsTable").DataTable( {
				"order": [[ 2, "desc" ]],
		        	"language": {
		        		"url": selectedLocalePath
		        	}
		    });
			errorLogsTable = $("#errorLogsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
	}
}

if($('.viewResults-page').length || $('.viewResultsForCustomPackage-page').length){
	viewResults.initialPage();
	//add all events here
	
	$('.fromdatepicker').datepicker({
		onSelect : function(date) {	
					var toDate = $('.todatepicker');				 
		            var minDate = $(this).datepicker('getDate');		           
		            toDate.datepicker('option', 'minDate', minDate); 
		     },
		dateFormat : 'yy-mm-dd',
		defaultDate : new Date(),
		changeMonth : true,
		changeYear : true, 
		yearRange : "0:+20",
		numberOfMonths : 1
	});
	
	$('.todatepicker').datepicker({
		onSelect : function(date) {
			var toDate = $(this);			
	        var minDate = $('.fromdatepicker').datepicker('getDate');	       
	        toDate.datepicker('option', 'minDate', minDate); 
		},
		dateFormat : 'yy-mm-dd',
		changeMonth : true,
		changeYear : true, 
		yearRange : "0:+20",
		numberOfMonths : 1
	});
	 		
    var minDate = $('.fromdatepicker').datepicker('getDate');	       
    $('.todatepicker').datepicker('option', 'minDate', minDate);
	
	$("#searchJobResult").on('click',function(e){
		e.preventDefault();
		//any validations here
		var searchStartDate = $("#fromDate").val(),
			searchEndDate = $("#toDate").val();
		common.clearValidations(["#fromDate","#toDate"]);
		if(searchStartDate == ""){			
			common.showcustommsg("#fromDate",globalMessage['anvizent.package.label.thisFieldIsRequired']);
			return false;
		}
		if(searchEndDate == ""){			
			common.showcustommsg("#toDate",globalMessage['anvizent.package.label.thisFieldIsRequired']);
			return false;
		}	
		showAjaxLoader(true);
		$("#jobResultForm").submit();
		
	});
	
	$("#searchJobResultForHistoricalLoad").on('click',function(e){
		e.preventDefault();
	 
		var searchStartDate = $("#fromDate").val(),
			searchEndDate = $("#toDate").val();
		common.clearValidations(["#fromDate","#toDate"]);
		if(searchStartDate == ""){			
			common.showcustommsg("#fromDate",globalMessage['anvizent.package.label.thisFieldIsRequired']);
			return false;
		}
		if(searchEndDate == ""){			
			common.showcustommsg("#toDate",globalMessage['anvizent.package.label.thisFieldIsRequired']);
			return false;
		}	
		
		$("#jobResultForm").prop("action",$(this).data("getUrl"));
    	this.form.submit();
    	showAjaxLoader(true);
		
	});
	$("#searchCurrencyLodJobResults").on('click',function(e){
		e.preventDefault();
	 
		var searchStartDate = $("#fromDate").val(),
			searchEndDate = $("#toDate").val();
		common.clearValidations(["#fromDate","#toDate"]);
		if(searchStartDate == ""){			
			common.showcustommsg("#fromDate",globalMessage['anvizent.package.label.thisFieldIsRequired']);
			return false;
		}
		if(searchEndDate == ""){			
			common.showcustommsg("#toDate",globalMessage['anvizent.package.label.thisFieldIsRequired']);
			return false;
		}	
		console.log("--",$(this).data("currencyloadurl"));	
		$("#jobResultForm").prop("action",$(this).data("currencyloadurl"));
    	this.form.submit();
    	showAjaxLoader(true);
		
	});
/*	$(document).on('click','.view-error-log',function(){		 
		var batchId = $(this).attr('data-batchid');
		var userID = $("#userID").val(); 
		showAjaxLoader(true); 
		var url =  "/app/user/"+userID+"/package/viewErrorLogs/"+batchId+"";
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
	});*/
	
	$(document).on('click','.viewCustomErrors',function(){		 
		var batchId = $(this).attr('data-batchid');
		var userID = $("#userID").val(); 
		showAjaxLoader(true); 
		var url =  "/app/user/"+userID+"/package/viewErrorLogs/"+batchId+"";
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
				    		row.push(t['batchId']);
				    		row.push(t['errorMessage']);
				    		console.log(row);
				    		
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
	
	$(document).on('click','.view-error-log',function(){		 
		var batchId = $(this).attr('data-batchid');
		var userID = $("#userID").val(); 
		showAjaxLoader(true); 
		var url =  "/app/user/"+userID+"/package/viewFileErrorLogs/"+batchId+"";
		var myAjax = common.loadAjaxCall(url,'GET','',headers);
	    	myAjax.done(function(result) {
			    showAjaxLoader(false);
			    debugger
		    if(result != null && result.hasMessages){
		    	if(result.messages[0].code == "SUCCESS") {
		    		
		    		var popup = $("#viewErrorLogs").modal('show');	  		  	
		    		 var list = result.object;
			    	  if(list != null && list.length > 0){
			    		  
			    		  
			    		  try {
			    			  errorLogsTable.destroy();
							} catch (e) {
								console.log(e);
							}
							var $xRefTHeader = $("#errorLogsTable thead");
							$xRefTHeader.empty();
							 var $trHeader = $("<tr>");							
							 $.each(list[0], function (index, columnName) {
								 $trHeader.append($("<th>").text(columnName));
							 });
							 $xRefTHeader.append($trHeader);
							 var $xRefTBody = $("#errorLogsTable tbody");
								$xRefTBody.empty();
								
				    	   $.each(list, function (index, row) {
				    		  if (index > 0) {
				    			  var $tr = $("<tr>");
				    			  $.each(row, function (index1, column) {
				    				  var $td = $("<td>");
				    				  $td.text(column);
				    				  $tr.append($td);
					    		  });
				    			  $xRefTBody.append($tr);
				    		  }
				    		});
				    	   errorLogsTable = $("#errorLogsTable").DataTable();
				    	  }
			    	  else{
					    	common.showErrorAlert(result.messages[0].text);
					    }
		    	}
		    	
		    	else{
		    		common.showErrorAlert(result.messages[0].text);
		    	}
		    }else{
	    		var messages = [ {
	    			code : globalMessage['anvizent.message.error.code'],
	    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
	    		} ];
	    		common.showErrorAlert(globalMessage['anvizent.package.label.unableToProcessYourRequest']);
	    	}
	    	});
	});
	
$(document).on("click","#resetResults",function(){
	debugger
	$("#fromDate,#toDate").val('');
	var dLId = $("#dlId").val();
	var dlName = $("#dlName").val();
	window.location =  adt.appContextPath+"/adt/standardpackage/viewjobResults/"+dLId+"?source=standardPacakge&dl_Name="+dlName+""
});

$(document).on("click","#resetCustomResults",function(){
	debugger
	$("#fromDate,#toDate").val('');
	var packageId = $("#packageId").val();
	window.location =  adt.appContextPath+"/adt/package/viewResultsForCustomPackage/"+packageId+"?source=schedule"
});
	
}

