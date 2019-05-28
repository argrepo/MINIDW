var headers = {};
var isManualTrigger = false;
var historicalLoad = {
		initialPage : function() {
			setTimeout(function() { $("#pageErrors").hide(); }, 5000);
			$(".historicalUploadStatus").DataTable({
				"order": [[ 0, "desc" ]],"language": {
	                "url": selectedLocalePath
	            }
		    } );
			
			if ( $(".terminateJob").length ) {
				setTimeout(function() { window.location.reload() }, 5*60*1000);
			}
			
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			
	},
	validateHistoricalLoad : function(){
		
		  common.clearValidations(['#connectorId','#ilId','#historicalFromDate','#historicalToDate','#loadInterval','#histiricalQueryScript','#noOfRecordsPerFile','#multipartEnabled1']);
		
		   var ilid = $("#ilId option:selected").val();
	       var connectionId = $("#connectorId option:selected").val();
		   var historicalFromDate = $("#historicalFromDate").val();
	       var historicalToDate=$("#historicalToDate").val();
	       var loadInterval = $("#loadInterval option:selected").val();
	       var histiricalQueryScript = $("#histiricalQueryScript").val();
	       var multipartEnabled = $("input[name='multipartEnabled']").is(":checked");
	       var noOfRecordsPerFile = $("#noOfRecordsPerFile").val();
	        
		   var validStatus=true;
			
		    if(ilid == '' ||  ilid == 0){
	  	    	 common.showcustommsg("#ilId",  globalMessage['anvizent.package.label.pleasechooseilid'],"#ilId");
	  	    	 validStatus = false;
	  	     }
			
		   	if(connectionId == '' || connectionId == 0){
		   	 common.showcustommsg("#connectorId",  globalMessage['anvizent.package.label.pleasechooseconnectorid'],"#connectorId");
	    	 validStatus = false;
		   	}    
		    if(historicalFromDate == '' ){
	  	    	 common.showcustommsg("#historicalFromDate",   globalMessage['anvizent.package.label.pleasechoosehistoricalfromdate'],"#historicalFromDate");
	  	    	 validStatus = false;
	  	     }
			
		   	if(historicalToDate == ''){
		   	 common.showcustommsg("#historicalToDate", globalMessage['anvizent.package.label.pleasechoosehistoricaltodate'],"#historicalToDate");
	    	 validStatus = false;
		   	} 
		    if(loadInterval == '' || loadInterval == 0 ){
	  	    	 common.showcustommsg("#loadInterval",  globalMessage['anvizent.package.label.pleasechooseloadinterval'] ,"#loadInterval");
	  	    	 validStatus = false;
	  	     }
		   /* if(!multipartEnabled){
	  	    	 common.showcustommsg($("input[name='multipartEnabled']"), 'Please choose multi part file enabled.' ,$("input[name='multipartEnabled']"));
	  	    	 validStatus = false;
	  	     }*/
		    if(multipartEnabled){
		    if(noOfRecordsPerFile == '' || noOfRecordsPerFile == 0 ){
	  	    	 common.showcustommsg("#noOfRecordsPerFile",'Please choose no of records per file.',"#noOfRecordsPerFile");
	  	    	 validStatus = false;
	  	     }
		    }
		   	if(histiricalQueryScript == '' ){
		   	 common.showcustommsg("#histiricalQueryScript",   globalMessage['anvizent.package.label.pleasechoosehistoricalqueryscript'],"#histiricalQueryScript");
	    	 validStatus = false;
		   	} 
		   	  
		   	if (validStatus) {
		   	 var fromDate ="{fromDate}";
			  var toDate="{toDate}";
			  if (histiricalQueryScript.indexOf(fromDate) == -1 || histiricalQueryScript.indexOf(toDate) == -1) {
				  common.showcustommsg("#histiricalQueryScript",   globalMessage['anvizent.package.message.variblesnotfound'],"#histiricalQueryScript");
		    		validStatus = false;
		    	}
		   	}
		   	 
		    return validStatus;		
	 
	},
	showHistoryLoadQuery : function(result){	
		$(".heading").text("History Load Query");
		var historyLoadQuery = result.object.histiricalQueryScript;
		$("#viewQueryPopUp").find(".view-Query").text(historyLoadQuery);
		$("#viewQueryPopUp").modal('show');
	},
	displayDatabaseSchemaBasedOnConnectorId : function(connector_id,connectionId,mainDiv,protocal){
		$('#defualtVariableDbSchema').empty();
		var userID = $("#userID").val();  
		if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
			$('#defualtVariableDbSchema,mainDiv').append('<div class="row form-group dbSchemaSelection" id="dbSchemaSelection" >' +
				     '<div class="col-sm-2">'+globalMessage['anvizent.package.label.databaseandschema']+':</div>'+
					'<div class="col-sm-2">' +
						'<select class="form-control dbVariable"  id="dbVariable">'+
						'<option value="{db0}">'+globalMessage['anvizent.package.label.selectdbvariable']+'</option>'+
					'</select>'+
					'</div>'+
					'<div class="col-sm-2">'+
						'<select class="form-control dbName" id="dbName">'+
						'<option value="{dbName}">'+globalMessage['anvizent.package.label.selectdbname']+'</option>'+
					'</select>'+
					'</div>'+
				 
					'<div class="col-sm-2">'+
						'<select class="form-control schemaVariable" id="schemaVariable">'+
						'<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>'+
					   '</select>'+
					'</div>'+
					'<div class="col-sm-2">'+
						'<select  class="form-control schemaName" id="schemaName">'+
						'<option value="{schemaName}">'+globalMessage['anvizent.package.label.selectschemaname']+'</option>'+
					    '</select>'+
					'</div>'+
					
					'<div class="col-sm-2">'+
						'<button type="button" class="btn btn-primary btn-sm addDbSchema"> <i class="fa fa-plus" aria-hidden="true"></i> </button>'+
					'</div>'+
				'</div>'); 
			
			showAjaxLoader(true);
			var url_getDbSchemaVaribles ="/app/user/"+userID+"/package/getDbSchemaVaribles/"+connector_id ; 
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			 var myAjax = common.loadAjaxCall(url_getDbSchemaVaribles,'GET','',headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null && result.hasMessages){ 
			    			  if(result.messages[0].code == "ERROR") { 
			    				  $("#defualtVariableDbSchema,mainDiv").hide();
			    				  $("#replaceShemas,mainDiv").hide();
			    				  common.showErrorAlert(result.messages[0].text);
			    				  return false;
			    			  }  
			    		   else if(result.messages[0].code == "SUCCESS"){
			    			   
			    			   var dbVariable='';
			    			   var schemaVariable = '';
			    			   if(result.object != null){
			    				   $('#dbVariable,#schemaVariable,mainDiv').empty();
			    				   dbVariable ='<option value="{db0}">'+globalMessage['anvizent.package.label.selectdbvariable']+'</option>'; 
			    				   schemaVariable ='<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
			    				   $.each( result.object, function( key, value ) {
			    					   dbVariable += '<option value='+key+'>'+key+'</option>';
			    					   schemaVariable += '<option value='+value+'>'+value+'</option>';
				    				 });
			    				   $('#dbVariable,mainDiv').append(dbVariable);
			    				   $('#schemaVariable,mainDiv').append(schemaVariable);
			    				   historicalLoad.getAllSchemasFromDatabase(connector_id,connectionId,mainDiv,protocal) ;
			    			   }else{
			    				   common.showErrorAlert(result.messages[0].text);
			    				   return false;
			    			   }
			    			 
			    			  } 
			    		    
			    	  } else{
				    		var messages = [ {
				    			code : globalMessage['anvizent.message.error.code'],
				    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
				    		} ];
				    		common.displayMessages(messages);
				    	}   	  
			    }); 
			   
			}
		 
		 else if(protocal.indexOf('mysql') != -1 || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1   || protocal.indexOf('sforce') != -1 || protocal.indexOf('as400') != -1  ||  protocal.indexOf('postgresql') != -1    || protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1){

				$('#defualtVariableDbSchema,mainDiv').append('<div class="row form-group dbSchemaSelection" id="dbSchemaSelection" >' +
					    
						'<div class="col-sm-2">'+globalMessage['anvizent.package.label.databaseandschema']+':</div>'+
						'<div class="col-sm-2">'+
							'<select class="form-control schemaVariable" id="schemaVariable">'+
							'<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>'+
						   '</select>'+
						'</div>'+
						'<div class="col-sm-2">'+
						'<select class="form-control dbName" id="dbName">'+
						'<option value="{dbName}">'+globalMessage['anvizent.package.label.selectschemaname']+'</option>'+
					   '</select>'+
					   '</div>'+
						'<div class="col-sm-2">'+
							'<button type="button" class="btn btn-primary btn-sm addDbSchema"> <i class="fa fa-plus" aria-hidden="true"></i> </button>'+
						'</div>'+
					'</div>'); 
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				showAjaxLoader(true);
				var url_getDbSchemaVaribles ="/app/user/"+userID+"/package/getDbSchemaVaribles/"+connector_id ; 
				 var myAjax = common.loadAjaxCall(url_getDbSchemaVaribles,'GET','',headers);
				    myAjax.done(function(result) {
				    	showAjaxLoader(false);
				    	  if(result != null && result.hasMessages){ 
				    			  if(result.messages[0].code == "ERROR") { 
				    				  $("#defualtVariableDbSchema,mainDiv").hide();
				    				  $("#replaceShemas,mainDiv").hide();
				    				  common.showErrorAlert(result.messages[0].text);
				    				  return false;
				    			  }  
				    		   else if(result.messages[0].code == "SUCCESS"){
				    			   var schemaVariable = '';
				    			   if(result.object != null){
				    				   $('#schemaVariable').empty();
				    				   schemaVariable ='<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
				    				   $.each( result.object, function( key, value ) {
				    					   schemaVariable += '<option value='+value+'>'+value+'</option>';
					    				 });
				    				   $('#schemaVariable').append(schemaVariable);
				    				   
				    				   historicalLoad.getAllSchemasFromDatabase(connector_id,connectionId,mainDiv) ;
				    				   
				    			   }else{
				    				   common.showErrorAlert(result.messages[0].text);
				    				   return false;
				    			   }
				    			 
				    			  } 
				    	  } else{
					    		var messages = [ {
					    			code : globalMessage['anvizent.message.error.code'],
					    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
					    		} ];
					    		common.displayMessages(messages);
					    	}   	  
				    }); 
		       }
		 $("#defualtVariableDbSchema,#replaceShemas,#replace,#replaceAll,mainDiv").show();
	},
	
	getAllSchemasFromDatabase: function(connector_id,connectionId,mainDiv,protocal){
		var userID = $("#userID").val();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true);
		var url_getDbSchemaVaribles1 ="/app/user/"+userID+"/package/getAllSchemasFromDatabase/"+connectionId+""; 
		 var myAjax = common.loadAjaxCall(url_getDbSchemaVaribles1,'GET','',headers);
		    myAjax.done(function(dbResult) {
		    	showAjaxLoader(false);
		    	  if(dbResult != null){ 
		    		 
		    		  if(dbResult.hasMessages){
		    			  if(dbResult.messages[0].code == "ERROR") { 
		    				 
		    				  common.showErrorAlert(dbResult.messages[0].text);
		    				  return false;
		    			  }  
		    		   else if(dbResult.messages[0].code == "SUCCESS"){
		    			   
		    			   var dbName='';
		    			   if(dbResult.object != null){
		    				   $('#dbName',mainDiv).empty();
		    				   if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
		    					   dbName ='<option value="{dbName}">'+globalMessage['anvizent.package.label.selectdbname']+'</option>';
		    				   }else if(protocal.indexOf('mysql') != -1 || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1   || protocal.indexOf('sforce') != -1 || protocal.indexOf('as400') != -1  ||  protocal.indexOf('postgresql') != -1    || protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1){
		    					   dbName ='<option value="{dbName}">'+globalMessage['anvizent.package.label.selectschemaname']+'</option>';
		    				   }
		    				  
		    				   $.each( dbResult.object, function( key, value ) {
		    					   dbName += '<option value='+value+'>'+value+'</option>';
			    				 });
		    				  
		    				   $('#dbName',mainDiv).append(dbName);
		    			  
		    			   }else{
		    				   common.showErrorAlert(result.messages[0].text);
		    				   return false;
		    			   }
		    			 
		    			  } 
		    		  }	  
		    	  }    	  
		    }); 
	   }
}

if($('.historicalLoad-page').length){
	
	historicalLoad.initialPage();
	
	$("#historicalLoadTable").DataTable({
		"order": [[ 1, "dasc" ]],"language": {
            "url": selectedLocalePath
        }
	});
	 
         
	$("#ilId,#connectorId,#loadInterval").select2({               
        allowClear: true,
        theme: "classic"
	}); 
	
	$('#historicalFromDate').datepicker({
		onSelect : function(date) {	
					var toDate = $('#historicalToDate');				 
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
	
	$('#historicalToDate').datepicker({
		onSelect : function(date) {
			var toDate = $(this);			
	        var minDate = $('#historicalFromDate').datepicker('getDate');	       
	        toDate.datepicker('option', 'minDate', minDate); 
		},
		dateFormat : 'yy-mm-dd',
		changeMonth : true,
		changeYear : true, 
		yearRange : "0:+20",
		numberOfMonths : 1
	});
	
	
    $(document).on("change","#connectorId, #ilId",function(){
    	$("#defualtVariableDbSchema,#replaceShemas,#replace,#replaceAll").hide();
    	   common.clearValidations(['#connectorId','#ilId','#historicalFromDate','#historicalToDate','#loadInterval','#histiricalQueryScript']);
    	   var mainDiv = $(this).closest("#historicalLoadDiv");
	       var ilid = mainDiv.find("#ilId option:selected").val();
	       var connectionId = mainDiv.find("#connectorId option:selected").val();
	       var connector_id;
	       var protocal = '';
		   var validStatus = true;
	 
		    if(!validStatus){
		    	return validStatus;
	        }
		
		   var userID = $("#userID").val();
		   if (!isManualTrigger) {
			   $("#histiricalQueryScript").val("");
		   }
		   if(ilid != 0 && connectionId != 0){
			showAjaxLoader(true);
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			var url_defaultILincrementalquery = "/app/user/"+userID+"/package/defaultILHistoricalLoadQuery/"+ilid+"/"+connectionId;
			var myAjax = common.loadAjaxCall(url_defaultILincrementalquery,'GET','',headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	if(result != null && result.hasMessages && result.messages[0].code == "SUCCESS"){
			    		var obj = result.object;
			    		var query = result.object.query;
			    		if (isManualTrigger) {
			    			isManualTrigger = false;
			    		} else {
			    			mainDiv.find("#histiricalQueryScript").val(query);
			    		}
			    		console.log(obj);
			    		connector_id = obj.connectorId;
			    		protocal = obj.protocal;
			    		$("#oldQueryScript").val(query);
			    		$("#connector_Id").val(connector_id);
			    		$("#protocal").val(protocal);
			    		if(query != null && query != ""){
			    			$("#replaceShemas,#defualtVariableDbSchema,#replace,#replaceAll").show(); 
			    			
			    			if(connectionId != null ){
			    				historicalLoad.displayDatabaseSchemaBasedOnConnectorId(connector_id,connectionId,mainDiv,protocal);
	    					} 

			    			  var n = query.split(/from|FROM/).pop().split('.').shift().trim().split(" ");
				    		  var schemaName = n[n.length - 1]; 
				    		  if(protocal.indexOf('sqlserver') == -1 || protocal.indexOf('odbc') == -1){
			            	   schemaName = schemaName.replace(/[[\]]/g,''); 
			    			  }
				    		  else{
				    			  if(schemaName.indexOf("[") < 0)
				    				  schemaName = '['+schemaName+']'; 
				    		  }	
			    		}
			    	}
			    });
			    
		     }
		   
   	       }); 
    
    if($("#pageMode").val() == "edit"){
    	isManualTrigger = true;
    	$("#connectorId").trigger("change");
    }
    
    $("#saveHistoricalLoad").on('click', function() {
   	  
    	var status = historicalLoad.validateHistoricalLoad();
    	if(!status){ return false;}
    	 
    	$("#historicalLoadForm").prop("action",$(this).data("saveurl"));
    	showAjaxLoader(true);
     });
    
    $("textarea#histiricalQueryScript").on('keyup' , function() {
    	$(".queryValidatemessageDiv").empty();
    	$(this).removeClass("alert-danger alert-success");
    	$("#saveHistoricalLoad,#updateHistoricalLoad,#checkTablePreview").hide();
    });
    
    $("#updateHistoricalLoad").on('click', function() {
   	  
    	var status = historicalLoad.validateHistoricalLoad();
    	if(!status){ return false;}
    	 
    	$("#historicalLoadForm").prop("action",$(this).data("updateurl"));
    	showAjaxLoader(true);
     });

	 $("#addHistoricalLoad").on('click', function() {
			$("#historicalLoadForm").prop("action",$(this).data("addurl"));
		    showAjaxLoader(true);
		 
      });
	 
	 $("#runMultipleLoads").click(function(){	 
			 var idsVar = [];
			 $(".historyChkBox:checked").each(function(index, obj){
				 idsVar.push($(obj).data("historicalloadid"));
			 });
		 
		 	$("#ids").val(idsVar.join(","));
		 	$("#historicalLoadForm").prop("action",$("#runMultipleUrl").val());
		    showAjaxLoader(true);
	 });
	 
	 
	 $("#historicalLoadTable").on("click",".runHistoricallLoad",function(){
		 $("#historicalLoadForm").prop("action",$("#runUrl").val());
		 showAjaxLoader(true);
	 });
	 $("#historicalLoadTable").on("click",".terminateJob",function(){
		 $("#historicalLoadForm").prop("action",$("#stopUrl").val());
		 showAjaxLoader(true);
	 });

	 $("#historicalLoadTable").on("click",".cloneHistoricalLoad",function(){
		 $("#historicalLoadForm").prop("action",$("#cloneUrl").val());
		 showAjaxLoader(true);
	 });
	
	 $(".runHistoricallLoad1").click(function(){
		 var userID = $("#userID").val();
		 var historicalLoadId = $(this).data("historicalloadid");
		 var ilname = $(this).data("ilname");
		 var historicalfromdate = $(this).data("historicalfromdate");
		 var historicaltodate = $(this).data("historicaltodate");
		 var historicallastupdatedtime = $(this).data("historicallastupdatedtime");
		 var loadinterval = $(this).data("loadinterval");
		 	$("#hid").val(historicalLoadId);
			$(".historicalLoadInfo .runIlName").text(ilname);
			$(".historicalLoadInfo .runFromDate").text(historicalfromdate);
			$(".historicalLoadInfo .runToDate").text(historicaltodate);
			$(".historicalLoadInfo .runInterval").text(loadinterval);
			$(".historicalLoadInfo .runLastUpdatedTime").text(historicallastupdatedtime);
		 
			
			$(".viewDeatilsPreview tbody").empty();
			   if(historicalLoadId != ''){
				   
				showAjaxLoader(true);
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				var url_defaultILincrementalquery = "/app/user/"+userID+"/package/viewHistoricalLoadUploadStatus/"+historicalLoadId;
				var myAjax = common.loadAjaxCall(url_defaultILincrementalquery,'GET','',headers);
				    myAjax.done(function(result) {
				    	showAjaxLoader(false);
				    	if(result != null){
			    			  if(result.messages[0].code == "FAILED") {
			    				     common.showErrorAlert(result.messages[0].text);
				    				 return false;
				    			  } 
			    			  else if(result.messages[0].code == "SUCCESS"){
		                           var tbody = '';
		                           var historicalLoadStatus = result.object;
		                           if(historicalLoadStatus.length > 1){
		                        	   for(var i = 0;i<historicalLoadStatus.length ; i++ ){
		                        		   var $tr = $("<tr>");
		           							var $td = $("<td>");
		                        		   var trCount = $(".viewDeatilsPreview tbody tr").length + 1;
			           						$tr.append($td.clone().text( trCount ));
			           						$tr.append($td.clone().text( historicalLoadStatus[i].startDate ));
			           						$tr.append($td.clone().text( historicalLoadStatus[i].endDate ));
			           						$tr.append($td.clone().text( historicalLoadStatus[i].sourceRecordsCount ));
			           						$tr.append($td.clone().text( historicalLoadStatus[i].fileSizeToDisplay ));
			           						$(".viewDeatilsPreview tbody").append($tr);
		                               }
		                           }else if(historicalLoadStatus.length == 0){
		                        	   common.showErrorAlert(globalMessage['anvizent.package.label.noDataFound']);
		  		    				   return false;
	                               }
				    	     }
				          } 
				      });
				    
			       }
			
		 $("#viewDeatilsPreviewPopUp").modal('show');
		 
	 });
	 
	  
   $(document).on("click",".viewUploadStatus",function(){
	  
	   var historicalLoadId = $(this).data("historicalloadid");
	   var userID = $("#userID").val();
	   console.log(historicalLoadId ,userID );
	   
	   if(historicalLoadId != ''){
		   
		showAjaxLoader(true);
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url_defaultILincrementalquery = "/app/user/"+userID+"/package/viewHistoricalLoadUploadStatus/"+historicalLoadId;
		var myAjax = common.loadAjaxCall(url_defaultILincrementalquery,'GET','',headers);
		    myAjax.done(function(result) {
		    	console.log("resulkt",result);
		    	showAjaxLoader(false);
		    	if(result != null){
	    			  if(result.messages[0].code == "FAILED") {
	    				     common.showErrorAlert(result.messages[0].text);
		    				 return false;
		    			  } 
	    			  else if(result.messages[0].code == "SUCCESS"){
                           var tbody = '';
                           var historicalLoadStatus = result.object;
                           if(historicalLoadStatus.length > 0){
                        	   var  historicalUploadStatusTable = $(".historicalUploadStatus").DataTable();
                              historicalUploadStatusTable.clear();
                              	
                              	for(var i = 0;i<historicalLoadStatus.length ; i++ ){
                              		   		
                     		    		var row = [];			
                     		    		
                     		    		row.push((i+1));
                     		    		row.push(historicalLoadStatus[i].startDate);
                     		    		row.push(historicalLoadStatus[i].endDate); 
                     		    	 
                     		    		row.push(historicalLoadStatus[i].loadInterval);
                     		    		row.push(historicalLoadStatus[i].fileSizeToDisplay);
                     		    		row.push(historicalLoadStatus[i].sourceRecordsCount);
                     		    		row.push(historicalLoadStatus[i].ilExecutionStatus ? 'Done':'Pending');
                     		    		row.push("<a href='#' onclick=\"document.getElementById('historyLoadViiewId"+(i+1)+"').style.display='';this.style.display='none';\">Display Comment</a> <pre  id='historyLoadViiewId"+(i+1)+"' style='width: 350px;height: 200px;background-color: transparent;border: 0px;display:none;'>"+historicalLoadStatus[i].comment+"</pre>");
                     		    		row.push(historicalLoadStatus[i].modification.createdTime); 
                     		    		row.push(historicalLoadStatus[i].modification.modifiedTime ? historicalLoadStatus[i].modification.modifiedTime : "-"); 
                     		    		
                     		    		historicalUploadStatusTable.row.add(row);
                                  }
                              	
                              	historicalUploadStatusTable.draw(true); 
                        	   
                           }else if(historicalLoadStatus.length == 0){
                        	   common.showErrorAlert(globalMessage['anvizent.package.label.noDataFound']);
  		    				   return false;
                        	     
                              }
		    		       $("#viewHistoricalUploadStatusPopUp").modal('show');
		    	     }
		          } 
		      });
		    
	       }
	  });
 
	 
	 
	 $("#selectAll").click(function(){
			$(".historyChkBox:not(:disabled)").prop("checked", this.checked);
			if ( $(".historyChkBox:checked").length ) {
				 $("#runMultipleLoads").show();
			 } else {
				 $("#runMultipleLoads").hide();
				
			 }
	});
	 
	 $(".historyChkBox").click(function(){
		 if ( $(".historyChkBox:checked").length ) {
			 $("#runMultipleLoads").show();
		 } else {
			 $("#runMultipleLoads").hide();
			
		 }
	 });
	 
	 
	 
	  $(document).on("click","#validateHistoricalQuery",function(){
				
		    var userID = $("#userID").val(); 
			var connectionId = $("#connectorId option:selected").val();
			
			var typeOfCommand = "Query"; 
			var query = $("#histiricalQueryScript").val();;
			var isIncrementalUpdate = false; 
			var iL_id = $("#ilId option:selected").val();
			var historicalFromDate = $("#historicalFromDate").val().trim();
		    var historicalToDate=$("#historicalToDate").val();
		     
			var status = historicalLoad.validateHistoricalLoad();
			
	    	if(!status){ 
	    		$("#saveHistoricalLoad,#updateHistoricalLoad,#checkTablePreview").hide();
	    		$("textarea#histiricalQueryScript").trigger('keypress'); 
	    		return false;}
	    	
	    	var modifiedQuery = '';
	    	
	    	if(query.indexOf('/*') > -1 && query.indexOf('*/') > -1){
	    		modifiedQuery = query.replace("/*", " ").replace("*/", " ").replace("{fromDate}","'"+historicalFromDate+"'").replace("{toDate}","'"+historicalToDate+"'");
	    	}else{
	    		modifiedQuery = query;
	    	}
	    	var connector_id = $("#connector_Id").val();
			var url_getDbSchemaVaribles ="/app/user/"+userID+"/package/getDbSchemaVaribles/"+connector_id+"";
			showAjaxLoader(true);
			 var myAjax = common.loadAjaxCall(url_getDbSchemaVaribles,'GET','',headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
		    	  if(result != null && result.hasMessages){ 
		    			  if(result.messages[0].code == "ERROR") { 
		    				  common.showErrorAlert(result.messages[0].text);
		    				  return false;
		    			  }  
		    		   else if(result.messages[0].code == "SUCCESS"){
		    			   var unReplacedschemaAndDatabses = '';
		    			   if(result.object != null){ 
		    				   var schemaAndDatabses = [];
		    				   $.each( result.object, function( key, value ) {
		    					   schemaAndDatabses.push(value);
		    					   schemaAndDatabses.push(key);
			    				 });
		    				   for (i = 0; i < schemaAndDatabses.length; i++) {
		    						if (query.indexOf(schemaAndDatabses[i]) >= 0){
		    							unReplacedschemaAndDatabses += 	schemaAndDatabses[i] +',';
		    					    }  
		    					}
		    			   }
		   				    if(unReplacedschemaAndDatabses != ''){
		   						 var replacedWord = unReplacedschemaAndDatabses;
		   						 replacedWord = replacedWord.substring(0, replacedWord.length - 1);
		   						 common.showErrorAlert(globalMessage['anvizent.package.label.pleaseReplace']+" [ "+replacedWord+" ] "+globalMessage['anvizent.package.label.DatabasesOrSchemasInDefaultQuery']);
		   						 return false;
		   					}else{
		   						var selectData ={
		   								iLId : iL_id,
		   								iLConnection : {
		   									connectionId : connectionId
		   								},
		   								iLquery : modifiedQuery,
		   								typeOfCommand : typeOfCommand,
		   								isIncrementalUpdate : isIncrementalUpdate
		   					 	}
		   						var token = $("meta[name='_csrf']").attr("content");
		   						var header = $("meta[name='_csrf_header']").attr("content");
		   						headers[header] = token;
		   							showAjaxLoader(true);
		   							var url_checkQuerySyntax = "/app/user/"+userID+"/package/checksQuerySyntax";
		   							 var myAjax = common.postAjaxCall(url_checkQuerySyntax,'POST', selectData,headers);
		   							    myAjax.done(function(result) {
		   							     showAjaxLoader(false);
		   							    	  if(result != null && result.hasMessages){
		   							    		var message = '';
		   							    		 if(result.messages[0].code == "SUCCESS") {
		   							    			  message += '<div class="alert alert-success alert-dismissible" role="alert">'+
		   					    		  							''+result.messages[0].text+' <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+
		   					    		  							'</div>';
		   							    			  $(".queryValidatemessageDiv").append(message);
		   							    			  setTimeout(function() { $(".alert-success").hide().empty(); }, 10000);
		   							    			  $("#saveHistoricalLoad,#updateHistoricalLoad,#checkTablePreview").show();
		   							    		  }else{
		   							    			  message += '<div class="alert alert-danger alert-dismissible" role="alert">'+
		   					  							''+result.messages[0].text+''+
		   					  							'</div>';
		   							    			  	$(".queryValidatemessageDiv").append(message);
		   							    			  	setTimeout(function() { $(".alert-danger").hide().empty(); }, 10000);
		   							    		  }
		   							    	  }else{
		   										var messages = [ {
		   											code : globalMessage['anvizent.message.error.code'],
		   											text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
		   										} ];
		   							    		common.displayMessages(messages);
		   									}
		   							    });
		   							}
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
	    	 
	  $(document).on("click",".viewHistoryLoadQuery",function(){
			var userID = $("#userID").val();
			var id = $(this).attr("data-historicalloadid");
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			showAjaxLoader(true);
			var url_getHistoryLoadById = "/app/user/"+userID+"/package/getHistoricalLoadDetailsById/"+id;
			   var myAjax = common.postAjaxCall(url_getHistoryLoadById,'POST',{},headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;		    				
			    			  }
			    			  if(result.messages[0].code=="SUCCESS"){		    				
			    				  historicalLoad.showHistoryLoadQuery(result);
			    			  }
			    		  }
					}
			});
		});
	  $(document).on("click",".addDbSchema",function(){
		var mainDiv = $(this).closest("#historicalLoadDiv"); 
  		var defualtVariableDbSchema = '';
  		var connector_id = $("#connector_Id").val();
  		var protocal = $("#protocal").val(protocal);
    		var dbVariable = $(this).parents("#dbSchemaSelection").find("#dbVariable > option").clone();
    		var dbname = $(this).parents("#dbSchemaSelection").find('#dbName option').clone();
    		var schemaVariable = $(this).parents("#dbSchemaSelection").find('#schemaVariable option').clone();
    		var schemaName = $(this).parents("#dbSchemaSelection").find('#schemaName option').clone();
         
    		if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
    			defualtVariableDbSchema += '<div class="row form-group dbSchemaSelection" id="dbSchemaSelection">'+'<div class="col-sm-2">'+'</div>'+
      		'<div class="col-sm-2">'+
      			'<select class="form-control dbVariable" id="dbVariable">'+
      			'<option value="{db0}">select Db Variable</option>';
        		  $.each( dbVariable, function( key, value ) {
        			  console.log(value.innerHTML)
        			 if(key != 0)
        			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
        		 });
        		
      		 
        		defualtVariableDbSchema += '</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      		'<select class="form-control dbName" id="dbName">'+
        		'<option value="{dbName}">'+globalMessage['anvizent.package.label.selectdbname']+'</option>';
      		  $.each( dbname, function( key, value ) {
      			  if(key != 0)
      			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
      		 });
      		 
        		defualtVariableDbSchema +='</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      		'<select class="form-control schemaVariable" id="schemaVariable">'+
        		'<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
        		 $.each( schemaVariable, function( key, value ) {
        			if(key != 0)
       			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
       		 });
       		 
        		defualtVariableDbSchema += '</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      		'<select class="form-control schemaName" id="schemaName">'+
        		'<option value="{dbName}">'+globalMessage['anvizent.package.label.selectschemaname']+'</option>';
      		  $.each( schemaName, function( key, value ) {
      			  if(key != 0)
      			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
      		 });
      		
      		 
      		defualtVariableDbSchema += '</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      			'<button type="button" class="btn btn-primary btn-sm addDbSchema"><i class="fa fa-plus" aria-hidden="true"></i> </button>'+
      			'&nbsp;<button type="button" class="btn btn-primary btn-sm deleteDbSchema"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>'+
      		'</div>'+
      	  '</div>' ;
      		
    		}else if(protocal.indexOf('mysql') != -1 || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1   || protocal.indexOf('sforce') != -1 || protocal.indexOf('as400') != -1  ||  protocal.indexOf('postgresql') != -1    || protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1){
    			
    			defualtVariableDbSchema += '<div class="row form-group dbSchemaSelection" id="dbSchemaSelection">'+
        	    '<div class="col-sm-2"> </div>'+
      		'<div class="col-sm-2">'+
      		'<select class="form-control schemaVariable" id="schemaVariable">'+
        		'<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
        		 $.each( schemaVariable, function( key, value ) {
        			if(key != 0)
       			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
       		 });
       		 
        		defualtVariableDbSchema += '</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      		'<select class="form-control dbName" id="dbName">'+
        		'<option value="{dbName}">'+globalMessage['anvizent.package.label.selectschemaname']+'</option>';
      		  $.each( dbname, function( key, value ) {
      			  if(key != 0)
      			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
      		 });
      		 
      		defualtVariableDbSchema += '</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      			'<button type="button" class="btn btn-primary btn-sm addDbSchema"><i class="fa fa-plus" aria-hidden="true"></i> </button>'+
      			'&nbsp;<button type="button" class="btn btn-primary btn-sm deleteDbSchema"><span class="glyphicon glyphicon-trash"></span></button>'+
      		'</div>'+
      	  '</div>' ;
    		}
    		
  		  $("#defualtVariableDbSchema,mainDiv").append(defualtVariableDbSchema);
		});
	  
	  	$(document).on("change","#dbName",function(){ 		
	  		var mainDiv = $(this).closest("#historicalLoadDiv");
	  		var ilid = $("#ilId option:selected").val();
			var userID = $("#userID").val();
			var $schemaName = $(this).parents('#dbSchemaSelection').find('#schemaName');
			var databaseName = $(this).val();
			if(databaseName === '{dbName}' ){
				common.showErrorAlert(globalMessage['anvizent.package.message.pleaseselectdatabase']);
				return false;
			}
			var connector_id = $("#connector_Id").val();
			var protocal = $("#protocal").val();  
			var connectionId = mainDiv.find("#connectorId option:selected").val();
		if(connectionId !=null ){
			if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				showAjaxLoader(true);
				var url_getSchemaByDatabse ="/app/user/"+userID+"/package/getSchemaByDatabse/"+connectionId+"/"+databaseName+""; 
				var myAjax = common.loadAjaxCall(url_getSchemaByDatabse,'GET','',headers);
				    myAjax.done(function(result) {
				    	showAjaxLoader(false);
				    	  if(result != null){ 
				    		  
				    		  if(result.hasMessages){
				    			  if(result.messages[0].code == "ERROR") { 
				    				 
				    				  common.showErrorAlert(result.messages[0].text);
				    				  return false;
				    			  }  
				    		   else if(result.messages[0].code == "SUCCESS"){
				    			   
				    			   var schemaName='';
				    			  
				    			   if(result.object != null){
				    				   $schemaName.empty();
				    				      schemaName  = '<option value="{schemaName}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
				    				   $.each( result.object, function( key, value ) {
				    					   schemaName += '<option value='+value+'>'+value+'</option>';
					    				 });
				    				  
				    				   $schemaName.append(schemaName);
				    				  
				    			   }else{
				    				   common.showErrorAlert(result.messages[0].text);
				    				   return false;
				    			   }
				    			 
				    			  } 
				    		  }	  
				    	  }    	  
				    }); 
			}
			
			}
		 	 
	 	});
	  	
	  	$(document).on("click","#replaceShemas",function(){ 
	  		var mainDiv = $(this).closest("#historicalLoadDiv");
	  		var script = mainDiv.find("#histiricalQueryScript").val();
	  		var query='';
	  		var connector_id = $("#connector_Id").val();
	  		var protocal = $("#protocal").val();
	  		showAjaxLoader(true);
		    $('#defualtVariableDbSchema,minDiv').find('.dbSchemaSelection').each(function(i, obj) {
		    	
		    	var dbVariable=$(this).find(".dbVariable").val(); 
			    var dbName=$(this).find(".dbName").val();
			    var schemaVariable=$(this).find(".schemaVariable").val();
			    var schemaName=$(this).find(".schemaName").val();
			    common.clearValidations([$(obj).find('.dbVariable'), $(obj).find('.dbName'), $(obj).find('.schemaVariable'), $(obj).find('.schemaName')]);
			    
			  var validStatus = true;
			  if(dbVariable == '' || dbVariable == '{db0}'){
				    common.showcustommsg($(obj).find('.dbVariable'),globalMessage['anvizent.package.message.pleasechoosedatabasevariable'],$(obj).find('.dbVariable'));
			    	validStatus =false;
			  }
			  if(dbName == '' || dbName == '{dbName}'){
				    common.showcustommsg($(obj).find('.dbName'),globalMessage['anvizent.package.message.pleasechoosedatabasename'],$(obj).find('.dbName'));
			    	validStatus =false;
			  }
			  if(schemaVariable == '' || schemaVariable == '{schema0}'){
				    common.showcustommsg($(obj).find('.schemaVariable'),globalMessage['anvizent.package.message.pleasechooseschemavariable'],$(obj).find('.schemaVariable'));
			    	validStatus =false;
			  }
			  if(schemaName == '' || schemaName == '{schemaName}'){
				    common.showcustommsg($(obj).find('.schemaName'),globalMessage['anvizent.package.message.pleasechooseschemaname'],$(obj).find('.schemaName'));
			    	validStatus =false;
			  }
			 
			  if(!validStatus){
				 	return validStatus;
			   }
			  if( protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
			    if (script.indexOf(dbVariable) > -1 || script.indexOf(schemaVariable) > -1) {
				    	query = script.split(dbVariable).join(dbName);
				    	query = query.split(schemaVariable).join(schemaName);
			    	}
			    	
			    else{
			    	query = script;
			    }
			  }
		     else if(protocal.indexOf('mysql') != -1 || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1   || protocal.indexOf('sforce') != -1 || protocal.indexOf('as400') != -1  ||  protocal.indexOf('postgresql') != -1    || protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1){
		    	 console.log(schemaVariable ,dbName);
		    	 if (script.indexOf(schemaVariable) > -1) {
		    		 	
		    		    query = script.split(schemaVariable).join(dbName);
			    	}
			    	
			    else{
			    	query = script;
			    }
	    	}
			    script = query;
		    });
		    showAjaxLoader(false);
		    mainDiv.find("#histiricalQueryScript").val(script);
	  	});
	  	
	  	$(document).on("click",".deleteDbSchema",function(){ 		
	 		$(this).parents("#dbSchemaSelection").remove(); 		 			 
	 	});
	  	
	  	 $("#replace_all").click(function(){
	   		common.clearValidations(["#replace_variable"]);
	   		var mainDiv = $(this).closest("#historicalLoadDiv");
	   		var replaceVar = mainDiv.find('#replace_variable').val().toLowerCase();
	   		if(replaceVar == ''){ 
	 	    	common.showcustommsg("#replace_variable", globalMessage['anvizent.package.label.pleaseEnterVariable'] ,"#replace_variable");
	 			status = false;
	   		}else{
	   		    $("#replaceAllAlert,mainDiv").modal("show");
	   		}
	   	 });
	  	$("#confirmReplaceAll").click(function(){
	  		$("#undo").show();
	  		var queryScript = $('#histiricalQueryScript').val().toLowerCase();
	  		var replaceVar = $('#replace_variable').val().toLowerCase();
	  		var replaceWith = $('#replace_with').val();
	  		var status = true;
	  		if(replaceVar == ''){ 
		    	common.showcustommsg("#replace_variable", globalMessage['anvizent.package.label.pleaseEnterVariable'] ,"#replace_variable");
				status = false;
	  		}
	  		if(!status){
	  			return false;
	  		}
	  		queryScript=queryScript.replace(new RegExp(replaceVar, 'g'), replaceWith);
	  		$('#histiricalQueryScript').val(queryScript);
	  		$("#replaceAllAlert").modal("hide");
	  	 });
	 	 
		 $("#undo").click(function(){
			 	var mainDiv = $(this).closest("#historicalLoadDiv");
		  		var queryScript = mainDiv.find('#oldQueryScript').val(); 
		  		mainDiv.find('#histiricalQueryScript').val(queryScript);
		  		$("#undo,mainDiv").hide();
		  		$('#replace_variable,mainDiv').val("");
		  	    $('#replace_with,mainDiv').val("");
		  	 });
	 
		 $(document).on("change","#dataSourceName",function(){
		    	var dataSourceName=  $("#dataSourceName option:selected").val().trim();
		    	if(dataSourceName == "-1"){
			    	   $(".dataSourceOther").show();
			       }else{
			    	   $(".dataSourceOther").hide();
			       }
		    });

		 $(document).on("click","input[name='multipartEnabled']",function(){
			   var checked = $(this).is(":checked");
			   if(checked){
				   $("#noOfRecordsPerFileDiv").show();
			   }else{
				   $("#noOfRecordsPerFileDiv").hide();
				   $("#noOfRecordsPerFile").val(0);
			   }
			  
		    });
		 	 
 }