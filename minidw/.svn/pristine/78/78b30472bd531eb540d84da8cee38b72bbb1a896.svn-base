
var  aiSourceDefinition = {
		initialPage : function() {
			$("#aiSourceDefTbl").dataTable();
			$("#selectedSourceId").val('');
			/*$("#aiModalId").multipleSelect({
				filter : true,
				placeholder : 'Select AI Modal',
			    enableCaseInsensitiveFiltering: true
			});*/
		},
		
		validateAiSourceDefFields : function(){
			common.clearValidations(["#businessModalId","#aiModalId","#stagingTable","#sourceQuery"]);
			var userId = $("#userID").val();
			var sourceDefId = $("#selectedSourceId").val();
			var bussinessId = $("#businessModalId").val();
			var stgTable = $("#stagingTable").val();
			var sourceQuery = $("#sourceQuery").val();
			var isActive = $("input[name='isActive']").val();
			var aiModalId = $("#aiModalId").val();
			
			 
		   	 var validStatus=true;
	      	    if(bussinessId == '0' ){
		  	    	common.showcustommsg("#businessModalId", globalMessage['anvizent.package.label.pleaseSelectBusinessModal'],"#businessModalId");
		  	    	validStatus=false;
	      	    }
	      	    
	      	  if(sourceQuery == '' ){
		  	    	common.showcustommsg("#sourceQuery", globalMessage['anvizent.package.label.pleaseEnterSourceQuery'],"#sourceQuery");
		  	    	validStatus=false;
	      	    }
	      	  
		      	if(stgTable == '0' ){
		  	    	common.showcustommsg("#stagingTable", globalMessage['anvizent.package.label.pleaseSelectStagingTable'],"#stagingTable");
		  	    	validStatus=false;
	      	    }
	      	  
	      	  return validStatus;
		},
		
		saveAiSourceDefinition : function(){
			var userId = $("#userID").val();
			var sourceDefId = $("#selectedSourceId").val();
			var bussinessId = $("#businessModalId").val();
			var stgTable = $("#stagingTable").val();
			var sourceQuery = $("#sourceQuery").val();
			var isActive = $("input[name='isActive']").val();
			var aiModalId = $("#aiModalId").val();
			
			debugger
			var selectedData = {
					sourceDefId : sourceDefId,
				//	businessId : bussinessId,
					bussinessName:bussinessId,
					//aiModalId : aiModalId.toString(),
					stagingTbl : stgTable,
					sourceQuery : sourceQuery,
					isActive : isActive
			}
			var url_saveAISourceDefinition = "/app_Admin/user/"+userId+"/aiJobs/saveAISourceDefinition";
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCall(url_saveAISourceDefinition,'POST',selectedData,common.getcsrfHeader());
	 	    myAjax.done(function(result) {
		 	    	showAjaxLoader(false); 
		 	    	if(result.hasMessages){		    			  
		    			if(result.messages[0].code == "ERROR") {		    				  
							var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
		    				return false;
		    			} 
		    			if(result.messages[0].code == "SUCCESS") {
		    				var result = result.object;
		    				$("#selectedSourceId").val(result);
		    				window.location.reload();
		    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		
		getAISourceDefinitionInfo : function(result){
			debugger
			if(result != null){
				var resultInfo = result.object;
				var sourceId = resultInfo.sourceDefId;
				var businessId =  resultInfo.bussinessName;
				var aid = resultInfo.aiModalId;
				var stagingTbl = resultInfo.stagingTbl;
				var sourceQuery = resultInfo.sourceQuery;
				var isActive = resultInfo.isActive;
				$("#selectedSourceId").val(sourceId);
				$("#businessModalId").val(businessId);
				
				$("#stagingTable").val(stagingTbl);
				$("#sourceQuery").val(sourceQuery);
				isActive ?  $('input[name="isActive"][value="true"]').prop('checked',true) : $('input[name="isActive"][value="false"]').prop('checked',true);
				/*var modelIdList= [];
				var aiModalList = aid.split(',');
				for(var i = 0; i < aiModalList.length; i++){
					var aid = parseInt(aiModalList[i]);
					modelIdList.push(aid);
				}
				$("#aiModalId").val(modelIdList);*/
				// $("#aiModalId").multipleSelect('refresh');
			}
		},
		
		getAISourceInfo : function(sourceDefId){
			var userId = $("#userID").val();
			var url_aiSourceDefinition = "/app_Admin/user/"+userId+"/aiJobs/aiSourceDefinition/"+sourceDefId;
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCallObject(url_aiSourceDefinition,'GET','',common.getcsrfHeader());
	 	    myAjax.done(function(result) {
		 	    	showAjaxLoader(false); 
		 	    	if(result.hasMessages){		    			  
		    			if(result.messages[0].code == "ERROR") {		    				  
							var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
		    				return false;
		    			} 
		    			if(result.messages[0].code == "SUCCESS") {
		    				aiSourceDefinition.getAISourceDefinitionInfo(result);
		    				$(".createAISourceDiv").removeClass('hidden');
		    				$('.aimodelDiv').removeClass('hidden');
		    				$("#aiSourceDefinitionTable").addClass('hidden');
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		
		deleteSourceById: function(sourceDefId) {
			//debugger
			var userId = $("#userID").val();
			var url_deleteParamtersById= "/app_Admin/user/"+userId+"/aiJobs/deleteAISourceDefinition/"+sourceDefId;
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCallObject(url_deleteParamtersById,'DELETE','',common.getcsrfHeader());
	 	    myAjax.done(function(result) {
		 	    	showAjaxLoader(false); 
		 	    	if(result.hasMessages){		    			  
		    			if(result.messages[0].code == "ERROR") {		    				  
							var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
		    				return false;
		    			} 
		    			if(result.messages[0].code == "SUCCESS") {
		    				$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
		    				window.location.reload();
		    			}		    			  		    			  	
			    	}
	 	    	});
			
			
		},
		getAIStagingTableInfoByBMID : function(businessCaseName){
			var userId = $("#userID").val();
			var selectData = {
					businessCaseName : businessCaseName,
			}
			 var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
	 		showAjaxLoader(true);
			var url_getS3BucketInfoById = "/app_Admin/user/"+userId+"/aiJobs/getAIStagingTableInfoByBMID";
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
			    				  var stagingTable= [];
			    				  $.each(result.object.aIModel,function(idx,val){
			    					  debugger;
			    					  stagingTable.push(val.id);
			    				  });
			    				  $("#stagingTable").val(modelId);
			    				//  $("#aiModalId").multipleSelect('refresh');
			    				  
			    			  }
			    			  
			    		  }
					}
				});
		},
		getAIModalInfoByBMID : function(id){
			var userId = $("#userID").val();
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
			    				  var modelId= [];
			    				  $.each(result.object.aIModel,function(idx,val){
			    					  debugger;
			    					  modelId.push((val.id));
			    				  });
			    				  $("#aiModalId").val(modelId);
			    				//  $("#aiModalId").multipleSelect('refresh');
			    				  
			    			  }
			    			  
			    		  }
					}
				});
		},
		showQuery : function(result){	
			$(".heading").text("Source Query");
			var query = result.object;			
			$("#viewQueryPopUp").find(".view-Query").text(query);
			$("#viewQueryPopUp").modal('show');
		},
		
}
if($('.aiSourceDefinition-page').length){
	aiSourceDefinition.initialPage();
	
	$('.addAISourceDefBtn').on("click",function(){
		$(".createAISourceDiv").removeClass('hidden');
		$("#aiSourceDefinitionTable").addClass('hidden');
		$('input[name="isActive"][value="true"]').prop('checked',true)
		common.clearValidations(["#businessModalId","#aiModalId","#stagingTable","#sourceQuery"]);
		$("#businessModalId,#stagingTable").val('0');
		$("#sourceQuery").val('');
		/*$("#aiModalId").val([]);
		$("#aiModalId").multipleSelect('refresh');*/
	});
	
	$('#saveAiSourceBtn').on("click",function(){
		var status = aiSourceDefinition.validateAiSourceDefFields();
		 
		 if(!status){
			 return false
		 }else{
			 aiSourceDefinition.saveAiSourceDefinition();
		 }
	});
	
	$('.editSourceQuery').on("click",function(){
		common.clearValidations(["#businessModalId","#aiModalId","#stagingTable","#sourceQuery"]);
		var aiSourceId = $(this).attr('data-sourceid');
		aiSourceDefinition.getAISourceInfo(aiSourceId);
	});
	var aiSourceId;
	$('.deleteSourceQuery').on("click",function(){
		 aiSourceId = $(this).attr('data-sourceid');
		 $("#deleteAISourceDefinition").modal('show');
	});
	
	$(document).on('click', '#confirmDeleteAISourceDefinition', function(){
		 aiSourceDefinition.deleteSourceById(aiSourceId);
		 $("#deleteAISourceDefinition").modal('hide');
	});
	
	$(document).on("click","#aiSourceBack",function(){
		$("#aiSourceDefinitionTable").removeClass('hidden').show();
		$(".createAISourceDiv").addClass('hidden');
	})
	$(document).on('click', '#checkTablePreview', function() {
		var userId = $("#userID").val();
		var query =  $("#sourceQuery").val();
		if( query != '') {
			var selectData ={
					query : query
			}
		
			showAjaxLoader(true);
			var url_checkQuerySyntax = "/app_Admin/user/"+userId+"/aiJobs/getTablePreview";
			 var myAjax =  common.postAjaxCallObject(url_checkQuerySyntax,'POST', selectData);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null){
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") {
									
				    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+
		  							''+result.messages[0].text+''+
		  							'</div>';
				    			  	$(".queryValidatemessageDiv").append(message)
				    				  return false;
				    			  }  
			    		  }
			    		  
			    		  $("#tablePreviewPopUp").modal('show');
			    		//  $("#tablePreviewPopUpHeader").text(previewSourceTitle);
			    	  var list = result.object;
			    	  if(list != null && list.length > 0){
			    		  var tablePreview='';
				    	  $.each(list, function (index, row) {
				    		  
				    		  tablePreview+='<tr>';
				    		  $.each(row, function (index1, column) {
				    			  tablePreview += (index == 0 ? '<th>'+column+'</th>' : '<td>'+column+'</td>');
				    		  });
				    		 tablePreview+='</tr>'; 
				    		});
				    	  $(".tablePreview").empty();
				    	  $(".tablePreview").append(tablePreview);
				    	  }
				    	  else{
				    		  $(".tablePreview").empty();
				    		  $(".tablePreview").append(globalMessage['anvizent.package.label.noRecordsAvailableInTable']);
				    	  } 
			    	  }
			    	  
			    });
			
		} else {
			common.showcustommsg((typeOfCommand === "Query" ? "#queryScript" : "#procedureName"), typeOfCommand+" "+globalMessage['anvizent.package.label.shouldNotBeEmpty']);
		}
	});
/*	$(document).on("change","#businessModalId",function(){
		var bmId = $(this).val();
		if(bmId != '0')
			$('.aimodelDiv').removeClass('hidden');
		aiSourceDefinition.getAIModalInfoByBMID(bmId);
	});*/
	
$(document).on("change","#businessModalId",function(){
	
	var bid = $(this).val();
	if(bid == 0)
		{
		 common.showcustommsg("#businessModalId","Please choose business model.");
		 return;
		}
	
	
	var bName = $(this).find("option:selected").text();
	if(bName != '')
		$('.bussinessUsecaseStagingTableDiv').removeClass('hidden');
	aiSourceDefinition.getAIStagingTableInfoByBMID(bName);
}); 
	
	$(document).on("click",".sourceQueryById",function(){
		var userId = $("#userID").val();
		var sourceId =  $(this).attr("data-sourceid");
		var selectData = {
				id : sourceId,
		}
		 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true);
		var url_getSourceQueryById = "/app_Admin/user/"+userId+"/aiJobs/getSourceQueryById";
		   var myAjax = common.postAjaxCallObject(url_getSourceQueryById,'POST',selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				  
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){		    				
		    				  aiSourceDefinition.showQuery(result);
		    			  }
		    		  }
				}
		});
	});
	
	

}