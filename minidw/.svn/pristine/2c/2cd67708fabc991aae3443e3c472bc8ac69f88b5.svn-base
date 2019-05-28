var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

var headers = {};
var scheduleStatus = {
		initialPage : function() {
			
			$("#stoppedDiv, #runningDiv").hide();
			$("#groupName").multipleSelect({
				filter : true,
				placeholder : 'select groups',
			    enableCaseInsensitiveFiltering: true
			});
			$("#scheduleStatus").multipleSelect({
				filter : true,
				placeholder : 'select groups',
			    enableCaseInsensitiveFiltering: true
			});
			$("#clientId").multipleSelect({
				filter : true,
				single:true,
			    enableCaseInsensitiveFiltering: true
			});
		},
		loadStatus : function(){
			var userID = $("#userID").val();
			headers[header] = token;
			showAjaxLoader(true);
			var url_getSchedulerStatus = "/app/user/" + userID + "/package/scheduler/schedulerStatus";
			   var myAjax = common.loadAjaxCall(url_getSchedulerStatus,'POST','',headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					scheduleStatus.processResponse(result);
				});
		},
		
		loadFilterGroupNames : function(){
					var userID = $("#userID").val();
					headers[header] = token;
					showAjaxLoader(true);
					var url_getSchedulerStatus = "/app/user/" + userID + "/package/scheduler/getFilterGroupNames";
					   var myAjax = common.loadAjaxCall(url_getSchedulerStatus,'POST','',headers);
						myAjax.done(function(result) {
							showAjaxLoader(false);
							if(result != null){
					    		  if(result.hasMessages){
				    				  if(result.messages[0].code=="ERROR"){
				    					  common.showErrorAlert(result.messages[0].text);
				    					  var message = result.messages[0].text;	
				    				  } 
					    			  if(result.messages[0].code=="SUCCESS"){
					    				  var groupNamesList = result.object;
					    				  var select$ = $("#groupName").empty();
					    				  $.each(groupNamesList,function(key,value){
					    					  $("<option/>",{value:value,text:value}).appendTo(select$);
					    				  });
					    				  $("#groupName").multipleSelect('refresh');
					    			  }
					    		  }
							}
						});
		},
		
		populateJobDetails : function(status){
			var userID = $("#userID").val();
			$("#scheduleTable").hide();
			var $table = $("#scheduleTbl tbody").empty();
			$("#stoppedDiv, #runningDiv").hide();
			status ? $("#runningDiv").show():$("#stoppedDiv").show();
			status ? $("#schedulerRunningInfo").show():$("#schedulerRunningInfo").hide()
			if (status) {
				scheduleStatus.getClientIds();
				$("#scheduleTable").show();
				
				showAjaxLoader(true);
				scheduleStatus.loadFilterGroupNames();
				headers[header] = token;
				var url_getScheduledJobDetails = "/app/user/" + userID + "/package/scheduler/getScheduledInfoJob";
				   var myAjax = common.postAjaxCall(url_getScheduledJobDetails,'POST',null,headers);
					myAjax.done(function(result) {
						showAjaxLoader(false);
						scheduleStatus.processScheduledInfo(result);
						
					});
			}
			
		},
		
		processSchedulerResponse : function(result){
			var $table = $("#scheduleTbl tbody").empty();
			if(result != null){		
	    		  if(result.hasMessages){
  				  if(result.messages[0].code=="ERROR"){
  					  var message = result.messages[0].text;
  					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
	    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
	    			  }
	    			  if(result.messages[0].code=="SUCCESS"){
	    				  console.log(result.object);
	    				  var jobDetails = result.object;
	    				  $.each(jobDetails,function(i,data){
	    					  var tr$ = $("<tr>");
	    					  var pauseStatus = data.paused;
	    					  var endTime = data.endTime;
	    					  tr$.append($("<td class:'jobName'>").text(data.jobName));
	    					  tr$.append($("<td class:'groupName'>").text(data.groupName));
	    					  if(pauseStatus){
	    						  tr$.append($("<td>").text('-')); 
	    						  tr$.append($("<td>").append($("<button>",{type:'button',class:'btn btn-primary btn-sm pauseResume','data-jobName':data.jobName,'data-groupName':data.groupName,value:data.paused}).append($("<span>",{class:'glyphicon glyphicon-play',title:'resume'}))));
	    					  }else{
	    						  tr$.append($("<td>").text(data.nextFireTime));
	    						  tr$.append($("<td>").append($("<button>",{type:'button',class:'btn btn-primary btn-sm pauseStatus','data-jobName':data.jobName,'data-groupName':data.groupName,value:data.paused}).append($("<span>",{class:'glyphicon glyphicon-pause',title:'pause'}))));
	    					  }
	    					  tr$.append($("<td class:'startTime'>").text(data.startTime));
	    					  tr$.append($("<td class:'endTime'>").text(data.endTime));
	    					  $table.append(tr$);
	    				  })
	    			  }
	    		  }
			}	
		},
		
		
		processScheduledInfo : function(result){
			var clientId = $("#client_id").val();
			var $table = $("#scheduleTbl tbody").empty();
			$("#scheduleTable").show();
			if(result != null){		
	    		  if(result.hasMessages){
  				  if(result.messages[0].code=="ERROR"){
  					  var message = result.messages[0].text;
  					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
	    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
	    			  }
	    			  if(result.messages[0].code=="SUCCESS"){
	    				  console.log(result.object);
	    				  var jobDetails = result.object;
	    				  if(jobDetails != null){
	    					  if(jobDetails.length > 0){
	    						  $.each(jobDetails,function(i,data){
		    						  var tr$ = $("<tr>");
		    						  var pauseStatus = data.status;
		    						  var endTime = data.jobEndTime;
		    						  tr$.append($("<td class='jobId'>").text(data.jobId));
		    						  tr$.append($("<td class='jobName' title='"+data.jobKeyName+"'>").text(common.transform(data.jobKeyName,15)));
		    						  tr$.append($("<td class='groupName'>").text(data.groupName));
		    						  tr$.append($("<td class='startTime'>").text(data.jobStartTime));
		    						  if(endTime == null){
		    							  tr$.append($("<td>").text('-')); 
		    						  }else{
		    							  tr$.append($("<td>").text(data.jobEndTime));
		    						  }
		    						  /*tr$.append($("<td class:'nextFireTime'>").text(data.nextFireTime));*/
		    						  tr$.append($("<td class='status'>").text(data.status));
		    						  if(pauseStatus == "READY"){
		    							  tr$.append($("<td>").text(data.nextFireTime || '-' ));
		    							  tr$.append($("<td>").append($("<button>",{type:'button',class:'btn btn-primary btn-sm pauseStatus','data-jobName':data.jobKeyName,'data-groupName':data.groupName,value:data.paused}).append($("<span>",{class:'glyphicon glyphicon-pause',title:'pause'}))));
		    							  
		    						  }else if(pauseStatus == "PAUSE"){
		    							  tr$.append($("<td>").text('-')); 
		    							  tr$.append($("<td>").append($("<button>",{type:'button',class:'btn btn-primary btn-sm pauseResume','data-jobName':data.jobKeyName,'data-groupName':data.groupName,value:data.paused}).append($("<span>",{class:'glyphicon glyphicon-play',title:'resume'}))));
		    						  }else if(pauseStatus == "RUNNING"){
		    							  tr$.append($("<td>").text('-'));
		    							  tr$.append($("<td>").text('-'));
		    						  } else {
		    							  tr$.append($("<td>").text('-')); 
		    							  tr$.append($("<td>").text('-'));
		    						  }
		    						  if (data.groupName.startsWith('SCHEDULED_PACKAGES')) {
		    							  tr$.append($("<td>").append($("<button>",{type:'button',class:'btn btn-primary btn-sm viewResults','data-jobId':data.jobId,value:data.jobId}).append("View Job Results")));
		    						  } else {
		    							  tr$.append($("<td>").text('-'));
		    						  }
		    						  $table.append(tr$);
		    					  })  
	    					  }else{
	    						  var message = "No Scheduled Jobs"
		    						  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
				    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000); 
				    				  $("#scheduleTable").hide();
			    			  }
	    				  }else{
	    					  $("#scheduleTable").hide();
	    				  }
	    			  }
	    		  }
			}	
		},
		
		getClientIds : function(){
		 $("#clientInfo").show();
		 headers[header] = token;
		 var userID = $("#userID").val();
			var url_getClientList = "/app/user/" + userID + "/package/scheduler/getClientIds";
			   var myAjax = common.postAjaxCall(url_getClientList,'POST','',headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;	
		    				  }
			    			  if(result.messages[0].code=="SUCCESS"){
			    				  var clientList = result.object;
			    				  var select$ = $("#clientId").empty();
			    				  select$.append($('<option>',{value:"0",text:"Select Client"}))
			    				  $.each(clientList,function(key,value){
			    					  $("<option/>",{value:value,text:value}).appendTo(select$);
			    				  });
			    				  
			    				  $("#clientId").multipleSelect('refresh');
			    			  }
			    			  
			    		  }
					}
				});             
		},
		
		processResponse : function(result){
			if(result != null){
	    		  if(result.hasMessages){
  				  if(result.messages[0].code=="ERROR"){
  					  var message = result.messages[0].text;
  					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
	    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
	    			  } else if(result.messages[0].code=="SUCCESS"){
	    				  scheduleStatus.populateJobDetails(result.object);
	    			  }
	    		  }
			} else {
				var messages = [ {
	    			code : globalMessage['anvizent.message.error.code'],
	    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
	    		} ];
	    		common.displayMessages(messages);
			}
		},
		
		processTriggerJobInfo : function(result){
			var $table = $("#triggerTbl tbody").empty();
			if(result != null){		
	    		  if(result.hasMessages){
  				  if(result.messages[0].code=="ERROR"){
  					  var message = result.messages[0].text;
  					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
	    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
	    			  }
	    			  if(result.messages[0].code=="SUCCESS"){
	    				  console.log(result.object);
	    				  var jobDetails = result.object;
	    				  if(jobDetails != null){
	    					  $.each(jobDetails,function(i,data){
	    						  var tr$ = $("<tr>");
	    						  var pauseStatus = data.status;
	    						  var endTime = data.jobEndTime;
	    						  tr$.append($("<td class:'triggerID'>").text(data.masterId));
	    						  tr$.append($("<td class:'jobDescription'>").text(data.jobDescription));
	    						  tr$.append($("<td class:'jobStartTime'>").text(data.jobStartTime));
	    						  if(endTime == null){
	    							  tr$.append($("<td>").text('-')); 
	    						  }else{
	    							  tr$.append($("<td>").text(data.jobEndTime));
	    						  }
	    						  tr$.append($("<td class:'nextTriggerTime'>").text(data.nextFireTime || '-'));
	    						  tr$.append($("<td class:'statusInfo'>").text(data.status));
	    						  
	    						  $table.append(tr$);
	    					  })
	    				  }
	    			  }
	    		  }
			}	
		},
		
}

if($('.scheduleStatus-page').length){
	
	scheduleStatus.initialPage();
	headers[header] = token;
	scheduleStatus.loadStatus();
	
	var clientId = $("#client_id").val();
	  if(clientId != ""){
		  $(".clientSelectedVal").show();
		  $("#clientIdValue").text(clientId);
		  $("#editClientId").show();
		  $("#saveClientId,#cancelBtn,#refresh").hide();
		  //select$.val(clientId);
	  }
	
	$("#scheduleTable").show();
	$(".backBtn").hide();
	var client_id = $("#client_id").val();
	$(".clientList").hide();
	if(client_id != ""){
		$("#clientId").val(client_id);
	}else{
		 $(".clientList,#saveClientId,#refresh").show();
		 $("#editClientId").hide();
	}
	$(document).on("click","#start",function(){
		var userID = $("#userID").val();
		showAjaxLoader(true);
		headers[header] = token;

		var url_startScheduler = "/app/user/" + userID + "/package/scheduler/startscheduler";
		   var myAjax = common.loadAjaxCall(url_startScheduler,'POST','',headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				window.location.reload();
				//scheduleStatus.processResponse(result);
			});
	});
	
	$(document).on("click","#refreshJobData",function(){
		scheduleStatus.loadStatus();
	});
	
	$(document).on("click","#stop",function(){
		var userID = $("#userID").val();
		headers[header] = token;
		showAjaxLoader(true);
		var url_stopScheduler = "/app/user/" + userID + "/package/scheduler/stopscheduler";
		   var myAjax = common.loadAjaxCall(url_stopScheduler,'POST','',headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				window.location.reload();
				//scheduleStatus.processResponse(result);
			});
	})
	
	$(document).on("click",".pauseStatus",function(){
		var userID = $("#userID").val();
		var jobName = $(this).attr("data-jobName");
		var groupName = $(this).attr("data-groupName");
		var selectData = {
				jobName : jobName,
				groupName : groupName
		}
	 showAjaxLoader(true);
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url_getSchedulerpauseStatus = "/app/user/" + userID + "/package/scheduler/getPauseJobDetails";
		   var myAjax = common.postAjaxCallObject(url_getSchedulerpauseStatus,'POST',selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				scheduleStatus.processResponse(result);
			});
	});
	
	$(document).on("click",".pauseResume",function(){
		var userID = $("#userID").val();
		var jobName = $(this).attr("data-jobName");
		var groupName = $(this).attr("data-groupName");
		var selectData = {
				jobName : jobName,
				groupName : groupName
		}
	 showAjaxLoader(true);
		headers[header] = token;
		var url_getResumeJobDetails = "/app/user/" + userID + "/package/scheduler/getResumeJobDetails";
		   var myAjax = common.postAjaxCallObject(url_getResumeJobDetails,'POST',selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				scheduleStatus.processResponse(result);
			});
	});
	
	$(document).on("click","#search",function(){
		var userID = $("#userID").val();
		var $table = $("#scheduleTbl tbody").empty();
		var groupNames = $("#groupName").val();
		var fromDate = $("#fromDate").val();
		var toDate = $("#toDate").val();
		var status = $("#scheduleStatus").val();
		
		$("#scheduleTable").show();
		var selectData = {
				groupNames : groupNames,
				fromDate : fromDate,
				toDate : toDate,
				status : status
		}
		
		showAjaxLoader(true);
		headers[header] = token;
		var url_getGroupJobDetails = "/app/user/" + userID + "/package/scheduler/getScheduledJobDetails";
		   var myAjax = common.postAjaxCall(url_getGroupJobDetails,'POST',selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				scheduleStatus.processScheduledInfo(result);
			});
	}); 

		    $('.datepicker').datepicker({
				onSelect : function(date) {
					var toDate =  $("#toDate"),
						minDate = $("#fromDate").val();
					var minDateVar = new Date((minDate));
					minDateVar.setDate(minDateVar.getDate() + 1);
					toDate.datepicker('option','minDate',minDateVar);
				},
				dateFormat : 'yy-mm-dd',
				defaultDate : new Date(),
				minDate : 0,
				changeMonth : true,
				changeYear : true,
				yearRange : "0:+20",
				numberOfMonths : 1
			});
		    
   $("#saveClientId").on("click",function(){
	   var userID = $("#userID").val();
	   var clientId  =$("#clientId").val();
	   if(clientId == '0'){
		   common.showcustommsg("#clientId", "Please choose client", "#clientId");
			return false;
		}
   
		var $table = $("#scheduleTbl tbody").empty();
		$("#scheduleTable").show();
		
		showAjaxLoader(true);
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url_getGroupJobDetails = "/app/user/" + userID + "/package/scheduler/saveClientId/"+clientId;
		   var myAjax = common.postAjaxCall(url_getGroupJobDetails,'POST',null,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				window.location.reload();
				//scheduleStatus.processScheduledInfo(result);
			});
   
   })
   $("#editClientId").on("click",function(){
	   $(".clientSelectedVal,#editClientId").hide();
	   $(".clientList,#saveClientId,#cancelBtn,#refresh").show();
	   if ( $("#client_id").val() ) {
		   $("#clientId").val($("#client_id").val());
		   $("#clientId").multipleSelect('refresh');
	   }
   })
   $("#refresh").on("click",function(){
	   scheduleStatus.getClientIds();
	   $(".clientSelectedVal").hide();
   });
   
   $(document).on("click","#cancelBtn",function(){
		window.location.reload();
	});
   
   $(document).on("click",".viewResults",function(){
	   $("#scheduleTable").hide();
	   $(".backBtn").show();
	   var userID = $("#userID").val();
	   var jobId  = $(this).attr("data-jobId");
	   var $table = $("#triggerTbl tbody").empty();
		$("#scheduleTriggerTable").show();
		
		showAjaxLoader(true);
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url_getTriggerJobDetails = "/app/user/" + userID + "/package/scheduler/getTriggeredInfoByID/"+jobId;
		   var myAjax = common.postAjaxCall(url_getTriggerJobDetails,'POST',null,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				//window.location.reload();
				scheduleStatus.processTriggerJobInfo(result);
			});
   
   });
   
   $(document).on("click","#back",function(){
	   $("#scheduleTable").show();
	   $("#scheduleTriggerTable").hide();
	   $(".backBtn").hide();
	});
  
	
}