var headers = {
		
};
var localTimeZone = common.getTimezoneName();
var  aiModel = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			
			$("#aiModalId").multipleSelect({
				filter : true,
				placeholder : 'Select AI Modal',
			    enableCaseInsensitiveFiltering: true
			});
			
			$("#aiModelInfoTbl").DataTable({
				"order": [[0, "asc" ]],"language": {
		            "url": selectedLocalePath
		        }
			});
		},
		
		
		getAIModalInfoById : function(id,selectedType){
			debugger
			if(id != null && id != ''){
				var selectData = {
						id : id
				}
				 var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
		 		showAjaxLoader(true);
				var url_getS3BucketInfoById = "/app_Admin/user/"+userId+"/aiJobs/geAiModelInfoById";
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
				    				  
				    				  if(selectedType == 'editAi'){
				    					  $("#aiModelName").val(result.object.aIModelName);
					    				  $("#aiModelType").val(result.object.aiModelType); 
				    				  }
				    				  var aiModelContextParameters =JSON.parse(result.object.aiContextParameters);
				    			
				    					if ( aiModelContextParameters && aiModelContextParameters != null) {
				    						$.each(aiModelContextParameters,function(key,val){
				    							var addContextParam = $("#aicontextParameters").clone().removeAttr("id").show();
				    							var divCount = $("#aIContextParametersDiv > div").length;
				    							if ( divCount > 0 ) {
				    								addContextParam.find(".control-label").text("")
				    							}else if ( divCount == 0) {
				    								addContextParam.find(".deleteContextParams").remove();
				    							}
				    							addContextParam.find(".authBodyKey").val(key);
				    							addContextParam.find(".authBodyValue").val(val);
				    							$("#aIContextParametersDiv").append(addContextParam);
				    						});
				    					 }
				    					
				    			  }
				    			  
				    		  }
						}
					});
			}
			
		},
		
		validateAiModelInfo : function(){
		common.clearValidations(["#aiModelName"]);	
		var aIModelName = $("#aiModelName").val();
		 var aiModelType = $("#aiModelType").val();
		 var regEx = /^[A-Za-z_-][A-Za-z0-9_-]*$/;
		var validStatus=true;
	   	   if(aIModelName == '' ){
		   	common.showcustommsg("#aiModelName",globalMessage['anvizent.package.label.pleaseEnterModelName'],"#aiModelName");
		   	validStatus=false;
	   	   }
	     	if(!aIModelName.match(regEx)){
		   		common.showcustommsg("#aiModelName","Accept only alphanumeric and _ , - special characters allowed.","#aiModelName");
			   	validStatus=false;
			}
	   	 
	   	 return validStatus;
		},
     
		
}

if($('.aiModel-page').length){
	aiModel.initialPage();
	
	$("#aiModelInfoTable").show();
	
	
	$(document).on("click",".addAiModel",function(){
		$("#aid").val('');
		$("#aIContextParametersDiv .contextKeyValue").slice(0).remove(); 
		$(".addAImodelDiv").show();
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

	
	$(document).on("click","#back",function(){
		$(".addAImodelDiv").hide();
		$("#aiModelInfoTable").show();
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
				    				  var message = result.messages[0].text;
				    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>").show();
				    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);
				    				  $(".addAImodelDiv").hide();
				    				  window.location.reload();
				    				  $("#aiModelInfoTable").show();
				    			  }
				    		  }
						}
					});
	
		})
		
		
	$(document).on("click",".editDetails",function(){
		$("#aIContextParametersDiv .contextKeyValue").slice(0).remove(); 
		$(".addAImodelDiv").show();
		common.clearValidations(["#aiModelName"]);	
		$("#aiModelInfoTable").hide();
		var userId = $("#userID").val();
		var id = $(this).val();
		$("#aid").val(id);
		var selectedType = "editAi";
		aiModel.getAIModalInfoById(id,selectedType);
		
	})
	
	var id ;
	$(document).on("click",".deleteAIModel",function(){
		 id = $(this).val();
		 $("#deleteAIModel").modal('show');
	});
	
	$(document).on("click","#confirmDeleteAIModel",function(){
		$("#deleteAIModel").modal('hide');
		$("#aiModelInfoTable").show();
		var userId = $("#userID").val();
		$("#aid").val(id);
		var selectData = {
				id : id
		}
		 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
 		showAjaxLoader(true);
		var url_getS3BucketInfoById = "/app_Admin/user/"+userId+"/aiJobs/deleteAiModelInfoById";
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
		    				  $(".addAImodelDiv").hide();
		    				  window.location.reload();
		    				  
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
	

}