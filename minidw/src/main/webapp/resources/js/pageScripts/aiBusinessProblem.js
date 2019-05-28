var headers = {
		
};
var viewAIStatusTableResultsTable = null;
var  businessModel = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			
			$("#selectedhours").multipleSelect({
				filter : true,
				placeholder : 'Select Hours',
				enableCaseInsensitiveFiltering : true
			});
			
			$("#timeZone").select2({               
	             allowClear: false,
	             theme: "classic"
				});
			$("#businessModelTbl").DataTable({
				"order": [[0, "asc" ]],"language": {
		            "url": selectedLocalePath
		        }
			});
		/*	$("#aiModel").multipleSelect({
				filter : true,
				placeholder : 'Select AI Modal',
			    enableCaseInsensitiveFiltering: true
			});*/
			$("#aiModalId").multipleSelect({
				filter : true,
				placeholder : 'Select AI Modal',
			    enableCaseInsensitiveFiltering: true
			});
			
			viewAIStatusTableResultsTable  = $("#viewAIStatusTableResultsTable").DataTable({
		        "order": [[ 0, "desc" ]],"language": {
	                "url": selectedLocalePath
	            }
		    });

		},
     
    validateBusinessModel : function (tableId,TableName){
    	//common.clearValidations([tableId]);
    	var validateStatus = true;
	 if(TableName == ''){ 
	    	common.showcustommsg(tableId, globalMessage['anvizent.package.label.pleaseEnterTargetTableName']);
	    	validateStatus = false;
	    }else if(TableName.match(/\s/g)) {
	    	
	    	common.showcustommsg(tableId,globalMessage['anvizent.package.label.tableNameShouldNotContainSpace']);
	    	validateStatus = false;
	    }else if(/^[a-zA-Z0-9_]*$/.test(TableName) == false) {
	    	
	    	common.showcustommsg(tableId, globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters']+ "<br>"+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInTableName']);
	    	validateStatus = false;
	    }
	 	return validateStatus;
	},
	
	validateBusinessModelInfo : function(){
		common.clearValidations(["#businessName","#aiModel","#aiStagingTableScript","#aiIlTableScript","#aiOlTableScript","#aiStagingTable","#aiIlTable","#aiOlTable"]);
		var businessProblem = $("#businessName").val();
		 var aIModel = $("#aiModel").val();
		 var aIStagingTable = $("#aiStagingTable").val(); 
		 var aIStagingTableScript = $("#aiStagingTableScript").val();
		 var aIILTable = $("#aiIlTable").val();
		 var aIILTableScript = $("#aiIlTableScript").val();
		 var aIOLTable = $("#aiOlTable").val();
		 var aIOLTableScript = $("#aiOlTableScript").val();
		 
		 var regEx = /^[A-Za-z_-][A-Za-z0-9_-]*$/;
		 var validStatus=true;
   	   if(businessProblem == ''  ){
	   	common.showcustommsg("#businessName",globalMessage['anvizent.package.label.pleaseEnterBusinessProblem'],"#businessName");
	   	validStatus=false;
   	   }
	   	if(!businessProblem.match(regEx)){
	   		common.showcustommsg("#businessName","Accept only alphanumeric and _ , - special characters allowed.","#businessName");
		   	validStatus=false;
		}
   	 if(aIModel == 0 ){
	   	common.showcustommsg("#aiModel", globalMessage['anvizent.package.label.pleaseSelectModelName'],"#aiModel");
	   	validStatus=false;
   	   }
   	 if(aIStagingTableScript == '' ){
 	   	common.showcustommsg("#aiStagingTableScript", globalMessage['anvizent.package.label.pleaseEnterStagingTableScript'],"#aiStagingTableScript");
 	   	validStatus=false;
    	   }
   	 if(aIStagingTableScript.indexOf(aIStagingTable) == -1 && aIStagingTableScript !=''){
    		   common.showcustommsg("#aiStagingTableScript",globalMessage['anvizent.package.label.tableScriptTablesAreDifferent'],"#aiStagingTableScript");
    	 	   	validStatus=false;
    	 
   	 }
   	 if(aIILTableScript == '' ){
 	   	common.showcustommsg("#aiIlTableScript", globalMessage['anvizent.package.label.pleaseEnterAlTableScript'],"#aiIlTableScript");
 	   	validStatus=false;
    	   }
   	if(aIILTableScript.indexOf(aIILTable) == -1 && aIILTableScript !=''){
		   common.showcustommsg("#aiIlTableScript",globalMessage['anvizent.package.label.tableScriptTablesAreDifferent'],"#aiIlTableScript");
	 	   	validStatus=false;
	 
   }
   	 if(aIOLTableScript == '' ){
 	   	common.showcustommsg("#aiOlTableScript", globalMessage['anvizent.package.label.pleaseEnterOlTableScript'],"#aiOlTableScript");
 	   	validStatus=false;
    	   }
   	if(aIOLTableScript.indexOf(aIOLTable) == -1 && aIOLTableScript !=''){
		   common.showcustommsg("#aiOlTableScript",globalMessage['anvizent.package.label.tableScriptTablesAreDifferent'],"#aiOlTableScript");
	 	   	validStatus=false;
	  }
   	
		   	var status = businessModel.validateBusinessModel("#aiStagingTable",aIStagingTable);
			 if (status == false){
				 validStatus=false;
			 }
			 var status = businessModel.validateBusinessModel($("#aiIlTable"), aIILTable);
			 if (status == false){
				 validStatus=false;
			 }
			 var status = businessModel.validateBusinessModel($("#aiOlTable"), aIOLTable);
			 if (status == false){
				 validStatus=false;
		
			 }
    
   	 
   	 return validStatus;
	},
	getAlModelInfoList : function(){

		
		var userId = $("#userID").val();
		$('#aiModel').empty();
		 var select = $("#aiModel");
		 select.append($("<option>").val(0).text('Select Model'));
		 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
 		showAjaxLoader(true);
		var url_getAlModelInfo = "/app_Admin/user/"+userId+"/aiJobs/aiModelInfoList";
		   var myAjax = common.postAjaxCallObject(url_getAlModelInfo,'GET','',headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){		
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  var message = result.messages[0].text;
	    					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
		    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  console.log(result.object);
		    				   if (result.object) {
		                           $(result.object).each(function(index, item) {
		                               select.append($("<option>").val(item.aIModelName).text(item.aIModelName));
		                           });
		                       }
		    				  // $("#aiModel").multipleSelect('refresh');
		    			  }
		    			  
		    		  }
				}
			});
			
	},
	
	
	viewAIExecutionTableResultsPopUp : function(result) { 
        
        viewAIStatusTableResultsTable.clear();
	     $.each(result, function(key,val) {  
	       var row = [];	
	       row.push((key+1));
	       row.push(val.executionId);
       	   row.push(val.dlId); 
       	   row.push(val.runType);
       	   row.push(val.executionStatus);
       	   row.push('<input id="viewExecutionComments" type="button" value="'+globalMessage['anvizent.package.button.viewExecutionComments']+'" data-executionid='+val.executionId+' class="btn btn-primary btn-sm">')
       	   //row.push(val.executionComments);
       	   row.push(val.executionStartDate);
       	   row.push(val.lastExecutedDate);
       	  
       	viewAIStatusTableResultsTable.row.add(row); 
		     });
	     viewAIStatusTableResultsTable.draw(true); 
	     $("#viewAITableResultsHeader").empty().text("View Execution Results");
	    $("#aiStausViewPopUp").modal('show');
	    	 
	},
	
	getExecutionInfoByBusinessId : function(){
		var id = $("#statusById").val()
		var selectData = {
				id : id,
		}
		 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
 		showAjaxLoader(true);
		var url_getExecutionInfoByBusinessId = "/app_Admin/user/"+userId+"/aiJobs/getExecutionInfoByBusinessId";
		   var myAjax = common.postAjaxCallObject(url_getExecutionInfoByBusinessId,'POST',selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){		
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  var message = result.messages[0].text;
	    					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
		    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  console.log(result.object);
		    				  var result = result.object;
		    				  businessModel.viewAIExecutionTableResultsPopUp(result)
		    			  }
		    			  
		    		  }
				}
			});
	},
	
	viewExecutionCommentsInfo :  function(result){
		  if(result.messages[0].code == "SUCCESS") {
				 var  messages=[{
					  code : result.messages[0].code,
					  text : result.messages[0].text
				  }];
				 var executionComments = result.object;
					
				 
				 if(executionComments === "" || executionComments === null){
					 executionComments = "No Query Found.";
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
			          popup.document.body.innerHTML = "<pre>"+executionComments+"</pre>";
			          if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
			        	  popup.addEventListener (
				        	        "load",
				        	        function () {
				        	            var destDoc = popup.document;
				        	            destDoc.open ();
				        	            destDoc.title = "DD Layout";
				        	            destDoc.write ('<html><head></head><body><pre>'+executionComments+'</pre></body></html>');
				        	            destDoc.close ();
				        	        },
				        	        false
				        	    );
			          }

			          
			          
			  }else if(result.messages[0].code == "ERROR"){
				  var message = result.messages[0].text;
				  common.showErrorAlert(message);
		     }
	  
	}
}

if($('.aiBusinessProblem-page').length){
	businessModel.initialPage();
	
	$("#businessModelInfoTable").show();
	
	
	$(document).on("click",".addBussinessproblem",function(){
		$(".addBusinessDiv").show();
		common.clearValidations(["#businessName","#aiModel","#aiStagingTableScript","#aiIlTableScript","#aiOlTableScript","#aiStagingTable","#aiIlTable","#aiOlTable"]);
		 $("#isActiveYes").prop("checked",true);
		  $("#businessName").val('');
		  $("#bmid").val(' ');
		  $("#aiStagingTable").val(''); 
		  $("#aiStagingTableScript").val('');
		  $("#aiIlTable").val('');
		  $("#aiIlTableScript").val('');
		  $("#aiOlTable").val('');
		  $("#aiOlTableScript").val('');
		  $("#businessModelInfoTable").hide();
	   	//$("#aiModel").val([]);
		//$("#aiModel").multipleSelect('refresh');
		businessModel.getAlModelInfoList();
	});

	
	$(document).on("click","#back",function(){
		$(".addBusinessDiv").hide();
		$("#businessModelInfoTable").show();
	})
	
	$(document).on("click","#saveBusinessProblem",function(){
		
		 var businessProblem = $("#businessName").val();
		 var aIModel = $("#aiModel").val();
		// var aIModelName = $("#aiModel :selected").text().trim();
		 var aIStagingTable = $("#aiStagingTable").val(); 
		 var aIStagingTableScript = $("#aiStagingTableScript").val();
		 var aIILTable = $("#aiIlTable").val();
		 var aIILTableScript = $("#aiIlTableScript").val();
		 var aIOLTable = $("#aiOlTable").val();
		 var aIOLTableScript = $("#aiOlTableScript").val();
		 var isActive= $("input[name='isActive']:checked").val();
		 var bmid =  $("#bmid").val();
		 
		var status = businessModel.validateBusinessModelInfo();
		 if (status == false){
			 return false;
		 }
		/* var status = businessModel.validateBusinessModel("#aiStagingTable",aIStagingTable);
		 if (status == false){
			 return false;
		 }
		 var status = businessModel.validateBusinessModel($("#aiIlTable"), aIILTable);
		 if (status == false){
			 return false;
		 }
		 var status = businessModel.validateBusinessModel($("#aiOlTable"), aIOLTable);
		 if (status == false){
			 return false;

		 }*/
		 
		 var aiModelList = [];
		 /*for(var i=0;i<=aIModel.length-1;i++){
			 var aIModelName = $("#aiModel").attr('data-modalName');
			 
			 var aiModelObj = {
					 id : aIModel[i],
			 }
			 aiModelList.push(aiModelObj);
		 }*/
		 var aiModelObj = {
				// id : aIModel,
				 aIModelName:aIModel
		 }
		 aiModelList.push(aiModelObj);

		 var userId = $("#userID").val();
		
			 var selectedData = {
					 "bmid":bmid,
					 "businessProblem": businessProblem,
					 "aIModel" : aiModelList,
					 "aIStagingTable" : aIStagingTable,
					 "aIStagingTableScript" : aIStagingTableScript,
					 "aIILTable":aIILTable,
					 "aIILTableScript":aIILTableScript,
					 "aIOLTable":aIOLTable,
					 "aIOLTableScript":aIOLTableScript,
					 "isActive":isActive
					
				};
			 
			 console.log("selectedData",selectedData)
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				 
			   showAjaxLoader(true);
				var url_saveBusinessModalInfo = "/app_Admin/user/"+userId+"/aiJobs/saveBusinessModalInfo";
				   var myAjax =  common.postAjaxCall(url_saveBusinessModalInfo,'POST',selectedData,headers);
					myAjax.done(function(result) {
						showAjaxLoader(false);
						if(result != null){						
				    		  if(result.hasMessages){
			    				  if(result.messages[0].code=="ERROR"){
			    					  var message = result.messages[0].text;
			    					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
				    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
				    			  }
				    			  if(result.messages[0].code=="SUCCESS"){
				    				  var message = result.messages[0].text;
				    				  window.location.reload();
				    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>").show();
				    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);
				    				  $(".addBusinessDiv").hide();
				    				  $("#businessModelInfoTable").show();
				    			  }
				    		  }
						}
					});
	
		})
		
		
	$(document).on("click",".editDetails",function(){
		$(".addBusinessDiv").show();
		businessModel.getAlModelInfoList();
		common.clearValidations(["#businessName","#aiModel","#aiStagingTableScript","#aiIlTableScript","#aiOlTableScript","#aiStagingTable","#aiIlTable","#aiOlTable"]);
		$("#businessModelInfoTable").hide();
		var userId = $("#userID").val();
		var id = $(this).val();
		$("#bmid").val(id);
		var selectData = {
				id : id,
		}
		 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
 		showAjaxLoader(true);
		var url_getS3BucketInfoById = "/app_Admin/user/"+userId+"/aiJobs/getBusinessInfoById";
		   var myAjax = common.postAjaxCallObject(url_getS3BucketInfoById,'POST',selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){		
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  var message = result.messages[0].text;
	    					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
		    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  console.log(result.object);
		    				  
		    				  var message = result.messages[0].text;
		    				  $("#businessName").val(result.object.businessProblem);
		    				 // $("#aiModel").val(result.object.aIModel);
		    				  $("#aiStagingTable").val(result.object.aIStagingTable);
		    				  $("#aiStagingTableScript").val(result.object.aIStagingTableScript);
		    				  $("#aiIlTable").val(result.object.aIILTable);
		    				  $("#aiIlTableScript").val(result.object.aIILTableScript);
		    				  $("#aiOlTable").val(result.object.aIOLTable);
		    				  $("#aiOlTableScript").val(result.object.aIOLTableScript);
		    				  console.log("result.object.isActive--",result.object.isActive)
		    				  if(result.object.isActive == 'Yes'){
		    					  $("#isActiveYes").prop("checked",true);  
		    				  }else{
		    					  $("#isActiveNo").prop("checked",true);
		    				  }
		    				  $("#aiModel").val(result.object.modelName);
		    				 // var modelId= [];
		    				/*  $.each(result.object.aIModel,function(idx,val){
		    					  debugger;
		    					 // modelId.push((val.id));
		    					  $("#aiModel").val(val.id);
		    				  });*/
		    				//  $("#aiModel").val(modelId);
		    				 // $("#aiModel").multipleSelect('refresh');
		    				  
		    			  }
		    			  
		    		  }
				}
			});
		
	})
	
	var id ;
	$(document).on("click",".deleteBusinessModel",function(){
		 id = $(this).val();
		 $("#deleteAIBP").modal('show');
	});
	
	$(document).on("click","#confirmDeleteAIBP",function(){
		$("#businessModelInfoTable").show();
		$("#deleteAIBP").modal('hide');
		var userId = $("#userID").val();
		$("#bmid").val(id);
		var selectData = {
				id : id,
		}
		 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
 		showAjaxLoader(true);
		var url_getS3BucketInfoById = "/app_Admin/user/"+userId+"/aiJobs/deleteBusinessInfoById";
		   var myAjax = common.postAjaxCallObject(url_getS3BucketInfoById,'POST',selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){		
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  var message = result.messages[0].text;
	    					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
		    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  var message = result.messages[0].text;
		    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>").show();
		    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);
		    				  $(".addBusinessDiv").hide();
		    				  window.location.reload();
		    				  
		    			  }
		    			  
		    		  }
				}
			});
		
	})
	var bmid = '';
	$(document).on('click', '.scheduleAi', function() {
		$("#schedulePackagePopUp").modal('show');
		var _this = $(this).val();
		$("#bpid").val(_this);
		//$("input[name='runNowOrSchedule'][value='schedule']").click()
		bmid = $(this).val();
		$("#once").prop("checked",true);
		$("#dailyRecurrencePattern").hide();
		$("#weeklyRecurrencePattern").hide();
		$("#monthlyRecurrencePattern").hide();
		$("#yearlyRecurrencePattern").hide();
		$("#hourlyRecurrencePattern").hide();
		$("#scheduleTime,#timeZoneDivPanel,#RangeofRecurrence").hide();
		
	});
	$(' input[name="recurrencePattern"]').click(function() {
		
		 common.clearcustomsg("#recurrencePatternValidation");
		 $("#scheduleTime,#timeZoneDivPanel,#RangeofRecurrence").show();
		if ($(this).val() == "runnow" || $(this).val() == "hourly" || $(this).val() == "daily") {
			// empty other fields
			$("#dailyRecurrencePattern").show();
			$("#weeklyRecurrencePattern").hide();
			$("#monthlyRecurrencePattern").hide();
			$("#yearlyRecurrencePattern").hide();
			$("#hourlyRecurrencePattern").hide();
		}
		if ($(this).val() == "once") {
			// empty other fields
			$("#dailyRecurrencePattern").hide();
			$("#weeklyRecurrencePattern").hide();
			$("#monthlyRecurrencePattern").hide();
			$("#yearlyRecurrencePattern").hide();
			$("#hourlyRecurrencePattern").hide();
			$("#scheduleTime,#timeZoneDivPanel,#RangeofRecurrence").hide();
		}
		if ($(this).val() == "hourly") {
			// empty other fields
			$("#hourlyRecurrencePattern").show();
			$("#minutesPatternValidation").hide();
			$("#dailyRecurrencePattern").hide();
			$("#weeklyRecurrencePattern").hide();
			$("#monthlyRecurrencePattern").hide();
			$("#yearlyRecurrencePattern").hide();
		}
		if ($(this).val() == "weekly") {
			// empty other fields
			$("#weeksToRun").val("1");
			$('input[name="weekDayCheckBox"]').each(function() {
				if ($(this).is(":checked")) {
					$(this).attr('checked', false);
				}
			});

			$("#dailyRecurrencePattern").hide();
			$("#weeklyRecurrencePattern").show();
			$("#monthlyRecurrencePattern").hide();
			$("#yearlyRecurrencePattern").hide();
			$("#hourlyRecurrencePattern").hide();
		}
		if ($(this).val() == "monthly") {
			// empty other fields

			$("#dayOfMonth").val($("#dayOfMonth option:last").val());
			$("#monthsToRun").val($("#monthsToRun option:first").val());

			$("#dailyRecurrencePattern").hide();
			$("#weeklyRecurrencePattern").hide();
			$("#monthlyRecurrencePattern").show();
			$("#yearlyRecurrencePattern").hide();
			$("#hourlyRecurrencePattern").hide();
		}
		if ($(this).val() == "yearly") {
			// empty other fields

			$("#monthOfYear").val("1");
			$("#dayOfYear").val("1");
			$("#dailyRecurrencePattern").hide();
			$("#weeklyRecurrencePattern").hide();
			$("#monthlyRecurrencePattern").hide();
			$("#yearlyRecurrencePattern").show();
			$("#hourlyRecurrencePattern").hide();
		}
		
		if ($(this).val() == "once") {
			recurrencePattern = $(this).val();
			$("#hourlyRecurrencePattern").hide();
			$("#minutesPatternValidation").hide();
			$("#dailyRecurrencePattern").hide();
			$("#weeklyRecurrencePattern").hide();
			$("#monthlyRecurrencePattern").hide();
			$("#yearlyRecurrencePattern").hide();
		}
	});
	
	$("#monthOfYear").on( "change", function() {
		var _this = $(this);
		if (_this.val() == 2) {
			$("#dayOfYear option[value=30]").attr('disabled', true);
			$("#dayOfYear option[value=31]").attr('disabled', true);
		} else if (_this.val() == 4 || _this.val() == 6 || _this.val() == 9 || _this.val() == 11) {
			$("#dayOfYear option[value=30]").attr('disabled', false);
			$("#dayOfYear option[value=31]").attr('disabled', true);
		} else {
			$("#dayOfYear option[value=30]").attr('disabled', false);
			$("#dayOfYear option[value=31]").attr('disabled', false);

		}
	});
			$('.datepicker').datepicker({
			onSelect : function(date) {
				var toDate =  $("#scheduleEndDate"),
					minDate = $("#scheduleStartDate").val();
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
			
			$(document).on("click",".aiViewstatus",function(){
				var id = $(this).attr("data-status");
				$("#statusById").val(id);
				$("#aiStausViewPopUp").modal('show');
				businessModel.getExecutionInfoByBusinessId()
			})
	
			
			$(document).on("click",".confirmSchedule", function(){
		           var userId = $("#userID").val();
				   var timeZone = common.getTimezoneName();
				   var dldata = {
						     dL_id: bmid,
						     rJob:true
						};
				 
				    var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					headers[header] = token;
					
					var scheduleType = "runnow";
					
					var scheduleId = 0;
					
				   var url_runR =  "/app/user/"+userId+"/runSources?timeZone="+timeZone+"&schedulerType="+scheduleType+"&schedulerId="+scheduleId;
				   showAjaxLoader(true);
				   var myAjax = common.postAjaxCall(url_runR, 'POST', dldata, headers);
				    myAjax.done(function(result){
				    	showAjaxLoader(false);
				    	if(result != null && result.hasMessages){
				    		if(result.messages[0].code == "FAILED" || result.messages[0].code == "ERROR") {
						         common.showErrorAlert(result.messages[0].text);
						         if (scheduleId == 0) {
						        	setTimeout(function(){location.reload()}, 3000);
						         }
			    				 return false;
			    			  } 
							  else if(result.messages[0].code == "SUCCESS"){
								  $("#schedulePackagePopUp").modal('hide');
								  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+result.messages[0].text+"</div>").show();
				  				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);
				  			 if (scheduleId == 0) {
						        	setTimeout(function(){location.reload()}, 3000)
						         }
				  				$("html, body").animate({ scrollTop: 0 }, "slow"); 
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
			
		/*	
		$(document).on('click', '.confirmSchedule', function(){
				
				var scheduleStartDateVal = $("#scheduleStartDate").val();
				var reloadUrl = $("#reloadUrl").val();
				var rangeOfRecurrenceValidation = $('input:radio[name=rangeOfRecurrence]:checked').val();
				var scheduleOnce = $('input:radio[name=recurrencePattern]:checked').val();
				
				if(scheduleOnce != 'once'){
					
				if (scheduleStartDateVal.length == 0) {
					common.showcustommsg("#scheduleStartDateValidation", globalMessage['anvizent.package.label.pleaseFillScheduleStartDate']);
				}
				
				if ($('input:radio[name=rangeOfRecurrence]').is(":checked")) {
					if (rangeOfRecurrenceValidation == 'ScheduleEndDate') {
						var scheduleEndDateVal = $("#scheduleEndDate").val();
						if (scheduleEndDateVal.length == 0) {
							common.showcustommsg("#scheduleEndDateVaLidation", globalMessage['anvizent.package.label.pleaseFillScheduleEndDate']);
						}
					}
				} else {
					common.showcustommsg("#rangeofRecurrenceValidation", globalMessage['anvizent.package.label.pleaseChooseAnyOneOfRangeOfRecurrence']);
				}
				}
				var scheduleStartDate = null;
				var scheduleStartTime = null;
				var recurrencePattern = null;
				var daysToRun = null;
				var weeksToRun = null;
				var dayOfMonth = null;
				var monthsToRun = null;
				var dayOfYear = null;
				var monthOfYear = null;
				var yearsToRun = null;
				var isNoEndDate = false;
				var scheduleEndDate = null;
				var noOfMaxOccurences = null;
				var timeZone=null;
				var typeOfHours=null;
				var hoursToRun=null;
				var runNowOrSchedule = $("input[type=radio][name='runNowOrSchedule']:checked").val();
				var bpid = $("#bpid").val();
				// TODO validation for all fields
				
					if(scheduleOnce != 'once'){
						var validation = checkValidation();
						if (!validation)
							return false;
					}
					scheduleStartDate = $("#scheduleStartDate").val();
					var scheduleStartHours = $("#scheduleStartHours").val();
					var scheduleStartMinutes = $("#scheduleStartMinutes").val();
					scheduleStartTime = scheduleStartHours + ":"+ scheduleStartMinutes;
					
					var recurrencePatternSelector = 'input[name="recurrencePattern"]';
					$(recurrencePatternSelector).each(function() {
						if ($(this).is(":checked")) {
							
							if ($(this).val() == "once") {
								recurrencePattern = $(this).val();
							}
							
							if ($(this).val() == "runnow") {
								// no options for run
								// now
								recurrencePattern = $(this).val();
							}
							if ($(this).val() == "hourly") {
								// no options for hourly
								// recurrence
								recurrencePattern = $(this).val();
								if ($("#everyhourlyRadios").is(":checked")) { 
									typeOfHours=$("#everyhourlyRadios").val();
									hoursToRun = $("#everyhour").val();	
								}
							
								if ($("#selectedhourlyRadios").is(":checked")) {
									typeOfHours= $("#selectedhourlyRadios").val();
									hoursToRun = $("#selectedhours").val().join(",");
								}
								
							}
							if ($(this).val() == "daily") {
								// no options for daily
								// recurrence
								recurrencePattern = $(this).val();
							}
							if ($(this).val() == "weekly") {
								recurrencePattern = $(this).val();
								daysToRun = $('input[name=weekDayCheckBox]:checked') .map(function() {
													return $(this).val();
												}).get().join(",");
								weeksToRun = $("#weeksToRun").val();
								console.log("daysToRun>>>" + daysToRun)
							}
							if ($(this).val() == "monthly") {
								recurrencePattern = $(this).val();
								$('input[name=monthlyRadios]').each(function() {
													if ($(this).is(":checked")) { 
														if ($(this).val() == 'monthlyOption_first') {
															dayOfMonth = $("#dayOfMonth").val();
															monthsToRun = $("#monthsToRun").val();
														}
													}
												});
							}
							if ($(this).val() == "yearly") {
								recurrencePattern = $(this).val();
								$('input[name=yearlyRadios]').each(function() {
													if ($(this).is(":checked")) {
														if ($(this).val() == 'yearlyOptions_first') {
															dayOfYear = $("#dayOfYear").val();
															monthOfYear = $("#monthOfYear").val();
															yearsToRun = $("#yearsToRun").val();
														}
													}
												});
							}
							
						}
					});

					var rangeOfRecurrence = 'input[name="rangeOfRecurrence"]';
					$(rangeOfRecurrence).each(function() {
						if ($(this).is(":checked")) {
							if ($(this).val() == 'NoEndDate') {
								isNoEndDate = true;
							}
							if ($(this).val() == 'MaxOccurences') {
								noOfMaxOccurences = $( "#noOfMaxOccurences").val();
							}
							if ($(this).val() == 'ScheduleEndDate') {
								scheduleEndDate = $("#scheduleEndDate").val();

							}
						}
					});
					timeZone = $("#timeZone").val();
					if(timeZone == "select"){
						common.showcustommsg("#timeZone", globalMessage['anvizent.package.msg.pleaseChoosetimezone'],"#timeZone");
						return false;
					}
				
				showAjaxLoader(true);
			
					//schedule
					var selectData = {
							userPackage : {
								
								industry : {
									
								}
							},
							
							dlInfo :{
								
								dL_id : bpid
							},
							schedule : {
							
								scheduleType : runNowOrSchedule,
								scheduleStartDate : scheduleStartDate,
								scheduleStartTime : scheduleStartTime,
								recurrencePattern : recurrencePattern,
								daysToRun : daysToRun,
								weeksToRun : weeksToRun,
								dayOfMonth : dayOfMonth,
								monthsToRun : monthsToRun,
								dayOfYear : dayOfYear,
								monthOfYear : monthOfYear,
								yearsToRun : yearsToRun,
								isNoEndDate : isNoEndDate,
								scheduleEndDate : scheduleEndDate,
								noOfMaxOccurences : noOfMaxOccurences,
								timeZone: timeZone,
								typeOfHours:typeOfHours,
								hoursToRun:hoursToRun,
							}

						};
		     			$('.loader').fadeIn('fast');
						$('body').addClass('cursor-wait');
						 var userId = $("#userID").val();
						var token = $("meta[name='_csrf']").attr("content");
						var header = $("meta[name='_csrf_header']").attr("content");
						headers[header] = token;
						var url_saveSchedule = "/app_Admin/user/"+userId+"/aiJobs/savePackageSchedule"
						var myAjax = common.postAjaxCall(url_saveSchedule, 'POST', selectData,headers);
						myAjax.done(function(result1) {
							$('.loader').fadeOut('fast');
							$('body').removeClass('cursor-wait');

							if (result1.hasMessages) {
								var messages = "";
								$.each(result1, function(key, value) {
									if (key == 'messages') {
										messages = [ {
											code : value[0].code,
											text : value[0].text
										} ];

									}
								});
								$("#schedulePackagePopUp").modal('hide');
								common.displayMessages(messages);

								//schedulePage.getPackageListForSchedule();
								setTimeout(function(){ window.location.reload(); }, 1500);
							} else {
								standardPackage.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
								setTimeout(function(){ window.location.reload(); }, 1000);
							}
						});
				
			});
			
			*/

			$(document).on('click', '.addAiModel', function() {

				$("#aid").val('');
				$("#aIContextParametersDiv .contextKeyValue").slice(0).remove(); 
				$("#addAiModelPopUp").modal('show');
				common.clearValidations(["#aiModelName"]);	
				$("#aiModelName").val('');
				  $("#aiModelType").val('');
				$("#aiModelInfoTable").hide();
				if ( $("#aIContextParametersDiv > div").length == 0) {
					var addBodyParam = $("#aicontextParameters").clone().removeAttr("id").show();
					addBodyParam.find(".deleteContextParams").remove();
					addBodyParam.find(".authBodyKey").val('');
					addBodyParam.find(".authBodyValue").val('');
					$("#aIContextParametersDiv").append(addBodyParam);
				} 
				
				$("#aiModalId").val([]);
			   	$("#aiModalId").multipleSelect('refresh');
			
			});
			
			$(document).on("change","#aiModalId",function(){
				debugger
				//$("#aIContextParametersDiv .contextKeyValue").slice(0).remove(); 
				var selectedType = "aiModalType";
				var aiModalLength = $("#aiModalId option:selected").length
				if(aiModalLength > 0)
					$("#aIContextParametersDiv .contextKeyValue").slice(0).remove();
				var aiModalId = $("#aiModalId").val();
				if(aiModalId == null){
					$("#aIContextParametersDiv .contextKeyValue").empty();
					var addBodyParam = $("#aicontextParameters").clone().removeAttr("id").show();
					addBodyParam.find(".deleteContextParams").remove();
					addBodyParam.find(".authBodyKey").val('');
					addBodyParam.find(".authBodyValue").val('');
					$("#aIContextParametersDiv").append(addBodyParam);
				}
					
				for(var i=0;i<=aiModalLength;i++){
					var aiModalId = $("#aiModalId").val();
					if(aiModalId != ''){
						aiModel.getAIModalInfoById(aiModalId[i],selectedType);
					}
				}
				
				 e.stopImmediatePropagation();
			})
			
			
			$(document).on("click","#saveAIModel",function(){
				
				 var aIModelName = $("#aiModelName").val();
				 var aiModelType = $("#aiModelType").val();
				 var id =  $("#aid").val();
				 
				 var status = aiModel.validateAiModelInfo();
				 if (status == false){
					 return false;
				 }
				 
				 var aiModelContextParameters = {}
				 $("#aIContextParametersDiv .contextKeyValue").each(function(){
					 var bodyKey = $(this).find(".authBodyKey").val();
					 var bodyVal =$(this).find(".authBodyValue").val();
					 
					 aiModelContextParameters[bodyKey]=bodyVal;
					
				 });
				 var aiContextParameters = aiModelContextParameters; 
				 var userId = $("#userID").val();
				
					 var selectedData = {
							 "id":id,
							 "aIModelName": aIModelName,
							 "aiModelType" : aiModelType,
							 "aiContextParameters" :JSON.stringify(aiContextParameters)
							
						};
					 
					 console.log("selectedData",selectedData)
						var token = $("meta[name='_csrf']").attr("content");
						var header = $("meta[name='_csrf_header']").attr("content");
						headers[header] = token;
						 
					   showAjaxLoader(true);
						var url_saveBusinessModalInfo = "/app_Admin/user/"+userId+"/aiJobs/saveAiModelInfo";
						   var myAjax =  common.postAjaxCall(url_saveBusinessModalInfo,'POST',selectedData,headers);
							myAjax.done(function(result) {
								showAjaxLoader(false);
								if(result != null){						
						    		  if(result.hasMessages){
					    				  if(result.messages[0].code=="ERROR"){
					    					  var message = result.messages[0].text;
					    					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
						    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
						    			  }
						    			  if(result.messages[0].code=="SUCCESS"){
						    				  $("#addAiModelPopUp").modal('hide');
						    				  businessModel.getAlModelInfoList();
						    				 
						    			  }
						    		  }
								}
							});
			
				})
				

				$("#aIContextParametersDiv").on("click",".addContextParams",function(){
					var addBodyParam = $("#aicontextParameters").clone().removeAttr("id style");
					if ( $("#aIContextParametersDiv > div").length > 0 ) {
						addBodyParam.find(".control-label").text("")
					}
					
					$("#aIContextParametersDiv").append(addBodyParam);
				});
				
				$("#aIContextParametersDiv").on("click",".deleteContextParams",function(){
					$(this).parents(".contextKeyValue").remove();
				});
				
				$(document).on('click', '#viewExecutionComments', function(){
					debugger
					var executionId = $(this).attr("data-executionid"); 
					var userId = $("#userID").val();
					var selectData = {
							id : executionId
					};
					var token = $("meta[name='_csrf']").attr("content");
			 		var header = $("meta[name='_csrf_header']").attr("content");
			 		headers[header] = token;
					 showAjaxLoader(true);
					 var url_getExecutionCommentsById= "/app_Admin/user/"+userId+"/aiJobs/getExecutionCommentsById";
					   var myAjax = common.postAjaxCallObject(url_getExecutionCommentsById,'POST', selectData,headers);
					    myAjax.done(function(result) {
					    	showAjaxLoader(false);
					    		  if(result != null && result.hasMessages){ 
					    			  businessModel.viewExecutionCommentsInfo(result); 
					    		  }else{
					    			  businessModel.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
					    		  }
					    });
				});
		
}