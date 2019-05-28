var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var headers = {};
var scheduleJobInfo = {
		initialPage : function() {
			
		},
		populateJobDetails : function(status){
			var userID = $("#userID").val();
			var $table = $("#scheduleTbl tbody").empty();
				showAjaxLoader(true);
				headers[header] = token;
				var url_getScheduledJobDetails = "/app/user/" + userID + "/package/scheduler/getScheduledInfoJob";
				   var myAjax = common.postAjaxCall(url_getScheduledJobDetails,'POST',null,headers);
					myAjax.done(function(result) {
						showAjaxLoader(false);
						if(result != null){		
				    		  if(result.hasMessages){
			  				  if(result.messages[0].code=="ERROR"){
			  					  var message = result.messages[0].text;
			  					  if (message) {
			  						$("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
				    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
				    			  }  
			  					  }
			  					  
				    			  if(result.messages[0].code=="SUCCESS"){
				    				  console.log(result.object);
				    				  var jobDetails = result.object;
				    				  if(jobDetails != null){
				    					  $.each(jobDetails,function(i,data){
				    						  var tr$ = $("<tr>");
				    						  var pauseStatus = data.status;
				    						  var endTime = data.jobEndTime;
				    						  tr$.append($("<td class:'jobId'>").text(data.jobId));
				    						  tr$.append($("<td class:'jobName' title='"+data.jobKeyName+"'>").text(common.transform(data.jobKeyName)));
				    						  tr$.append($("<td class:'groupName'>").text(data.groupName));
				    						  tr$.append($("<td class:'startTime'>").text(data.jobStartTime));
				    						  if(endTime == null){
				    							  tr$.append($("<td>").text('-')); 
				    						  }else{
				    							  tr$.append($("<td>").text(data.jobEndTime));
				    						  }
				    						  /*tr$.append($("<td class:'nextFireTime'>").text(data.nextFireTime));*/
				    						  tr$.append($("<td class:'status'>").text(data.status));
				    						  if(pauseStatus == "READY"){
				    							  tr$.append($("<td>").text(data.nextFireTime));
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
				    				  }
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
	    				  scheduleJobInfo.populateJobDetails(result.object);
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

if($('.scheduleJobInfo-page').length){
	
	scheduleJobInfo.initialPage();
	scheduleJobInfo.populateJobDetails();
	$("#scheduleTable,.backSchedulers").show();
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
				scheduleJobInfo.processResponse(result);
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
				scheduleJobInfo.processResponse(result);
			});
	});
	
	$(document).on("click",".viewResults",function(){
		   $("#scheduleTable,.backSchedulers").hide();
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
					scheduleJobInfo.processTriggerJobInfo(result);
				});
	   
	   });

	$(document).on("click","#back",function(){
		   $("#scheduleTable,.backSchedulers").show();
		   $("#scheduleTriggerTable").hide();
		   $(".backBtn").hide();
		});	  
	
}