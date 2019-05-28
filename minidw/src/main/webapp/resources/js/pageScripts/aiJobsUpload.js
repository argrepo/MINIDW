var userId = $("#userID").val();

var aiJobsUpload = {
	initialPage : function() {

		
		$("#aiJobsUploadTable").DataTable({
			"order": [[0, "asc" ]],"language": {
	            "url": selectedLocalePath
	        }
		});
		
		/*$("#aiModalId").multipleSelect({
			filter : true,
			placeholder : 'Select AI Modal',
		    enableCaseInsensitiveFiltering: true
		});*/
		$(".dataValidationTypeContextParams").multipleSelect({
			filter : true,
			placeholder : 'Select Context Parameters',
		    enableCaseInsensitiveFiltering: true
		});
	},

	updateUploadedFileNames : function(){
		var fileNames = "";
		var i = 0;
		var uploadedFilenames = $("#uploadedJobFileNames").val();
		/*if(uploadedFilenames != ''){
			fileNames += uploadedFilenames
		}*/
		if(uploadedFilenames == ""){
			//var jobFileDiv = $(".jobFilesDiv .fileContainer").find(".jobFileNames");
			//jobFileDiv.parents(".fileContainer").find(".useOldJobFile");
			$('input[id=useOldJobFile0]:checked').prop("checked",false);
		
		}
			
		debugger
		$(".jobFilesDiv .fileContainer").find(".jobFileNames").each(function(){
			var fileName = "";
			var oldJobFile = $(this).parents(".fileContainer").find(".useOldJobFile");
			var jobExecutionType = $('input[id=useOldJobFile'+i+']:checked').val();
			if(oldJobFile.find(jobExecutionType)){
				if(jobExecutionType == 'on'){
				fileName = $(this).val();
				if(fileNames != '' && fileName != ''){
					fileNames+=",";
				}
				fileNames+=fileName;
			}
			}
			i++;
		});
		
		
		$(".jobFilesDiv .fileContainer").find(".jobFile").each(function(){
			debugger
			var filePath = $(this).val();
			var fileName = filePath.substring(filePath.lastIndexOf('\\')+1 , filePath.length);
		    	if((fileNames != '' && fileName != '')){
					fileNames+=",";
				}
				fileNames+=fileName;
		});
		
			$("#uploadedJobFileNames, .uploadedJobFileNames").val(fileNames);
			$("#jobName, .jobName").val(fileNames);
	},
	

	
	  cleanupAIJobUpload : function() {
    	  common.clearValidations(["#businessModalId","#active","#jobName","#uploadedDJobFileNames",".jobFile"]);
    	  
    	  $("#businessModalId").val('0');
    	  $("#genericContextParam").val('0');
    	  $("#jobName").val("");
    	  $("#uploadedJobFileNames").val('');
    	  $('input[name=active]').attr("checked", false);
		 
	},
	
		selectFieldsData : function(rJobId) {
			debugger
			if (rJobId > 0) {
				$("#updateAIJobUpload").show();
				$("#saveAIJobUpload").hide();
			} else {
				$("#updateAIJobUpload").hide();
				$("#saveAIJobUpload").show();
				$(".jobFilesDiv").show();
				$("#aiJobsUploadDiv").modal("show");
				$('input[name="active"][value="true"]').prop('checked',true)
			}
			 
	},
	
	validateFormTypeUpload : function(){
		
		common.clearValidations(["#aiJobType","#active","#existedJarjobFileNames","#jobName","#uploadedDJobFileNames",".jobFile"]);
		var validStatus = true;
		var jobName = $(".jobName").val();
		var uploadedJobFileNames = $("#uploadedJobFileNames").val();
		var contextparams = $("#contextParameters").val();
		
		if(jobName == ''){
   	    	common.showcustommsg(".jobName", globalMessage['anvizent.package.label.pleaseEnterJobName'],".jobName");
   	        validStatus=false;
   	    }else if(!/^[a-zA-Z0-9_.]*$/.test(jobName.trim())){
   	    	 common.showcustommsg(".jobName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],".jobName");
   	    	 validStatus=false;
   	    	
   	    }
		  if(uploadedJobFileNames == ''){
	   	    	common.showcustommsg(".uploadedJobFileNames", globalMessage['anvizent.package.label.pleaseAddAtLeastOneJobFile'],".uploadedJobFileNames");
				validStatus=false;
	   	    }
	   	    $(".jobFilesDiv").find(".jobFile").each(function(){	   	       
	   	    	if($(this).val() != ''){
	   				  var fileExtension = $(this).val().replace(/^.*\./, '');
	     		      if(!(fileExtension == 'jar')) {
	     		    	common.showcustommsg($(this), globalMessage['anvizent.package.label.pleaseChooseJarFile'],$(this));
	     		    	validStatus=false;
	     		     }
	   			}
	   		});
	   	 var jobFiles = [];
			/*$(".jobFilesDiv").find(".jobFile").each(function(){	   	
				var filePath = $(this).val(); 
				if(filePath != "")
    		    jobFiles.push(filePath.substring(filePath.lastIndexOf('\\')+1 , filePath.length));
			});*/
			$(".jobFilesDiv .fileContainer").find(".jobFileNames").each(
				function() {
					var i = 0;
					var oldJobFile = $(this).parents(".fileContainer").find(".useOldJobFile");
					var jobExecutionType = $('input[id=useOldJobFile' + i + ']:checked').val();
					if (oldJobFile.find(jobExecutionType)) {
						if (jobExecutionType == 'on') {
							var fileName = $(this).val();
							if (fileName != '') {
								jobFiles.push(fileName);
							}
						}
					}
					i++
				});
			$(".existedJarFile option:selected").each(function(){
				var fileName = $(this).val();
				if(fileName != ''){
					jobFiles.push(fileName);
				}
			}); 
			
			
			if($("select.dataValidationTypeContextParams option:selected").length == 0){
				common.showcustommsg("#contextParameters", globalMessage['anvizent.package.label.pleasechooseContextParameters']);
				validStatus=false; 
			}
			
			var jobFile_data = $(".jobFile").val();
			var existedJarFile_Data = $(".existedJarFile option:selected");
			
			
			/*if(jobFile_data != '' && existedJarFile_Data.val() != null){
				common.showcustommsg("#uploadedJobFileNames","Either Job File or Existed Jar Files need to be Uploaded","#uploadedJobFileNames");
				validStatus = false;
			}*/
			
			if(jobFiles != ""){
	   	    	for(var i = 0; i <= jobFiles.length; i++) {
		   	         for(var j = i; j <= jobFiles.length; j++) {
		   	             if(i != j && jobFiles[i] == jobFiles[j]) {
		   	            	common.showcustommsg(".uploadedJobFileNames",globalMessage['anvizent.package.label.duplicateJobFiles'],".uploadedJobFileNames");
		   	            	validStatus = false;
		   	                 break;
		   	             }
		   	         }
		   	     }
	   	    }
			var jobName = $("#jobName").val();
			if(jobName == ''){
				common.showcustommsg("#jobName", globalMessage['anvizent.package.label.pleaseEnterJobName'], "#jobName");
				validStatus = false;
			}else if(!/^[a-zA-Z0-9_.]*$/.test(jobName.trim())){
				common.showcustommsg("#jobName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#jobName");
				validStatus=false;
			}
			return validStatus;
	},
		saveAIUploadInfo : function(){
			debugger
			var businessModalId = $("#businessModalId").val();
			var aiModalId = $("#aiModalId");
			var aiModalIdVal = $("#aiModalId option:selected").text();
			var genericContextParamId = 0;//$("#genericContextParam").val();
			//var genericContextParamValue = $("#genericContextParam").text();
			var jobName = $("#jobName").val();
			var uploadedJobFileNames = $("#uploadedJobFileNames").val();
			var active =$("input:radio[name='active']:checked").val();
			var userID = $("#userID").val();
			var rid = $("#rJobId").val();
			var aiModelContextParameters = {}
			var count = 1;
			
			var aiModel = [];
			if(aiModalId != null){
				 for(var i=0;i<aiModalId.length;i++){
						var _accordianDiv = $('#aINewContextParametersDiv').find("#aicontextParameters"+$("#aiModalId").val());
						var keyValuePairDivLength = $(_accordianDiv).children('div');
						$.each(keyValuePairDivLength, function(idx){
							idx++;
							var keyValuepairDiv = $('.keyValuPair'+idx);
							var bodyKey = $(keyValuepairDiv).find(".authBodyKey").val();
							var bodyVal =$(keyValuepairDiv).find(".authBodyValue").val();
							aiModelContextParameters[bodyKey]=bodyVal;
						});
						 var aiModelObj = {
								 id : $("#aiModalId").val(),
								 aiContextParameters : JSON.stringify(aiModelContextParameters)
						 }
						 aiModel.push(aiModelObj);
					 }
			}
			
			var aiSpecificContextParams = aiModelContextParameters; 
			 var selectedData = {
					 rid : rid,
					 businessId:businessModalId,
					 businessName:aiModalIdVal,
					 aiModal : aiModel,
					 aiGenericContextParam : {
						pid : genericContextParamId,
					 },
					 jobName: jobName,
					 jobFileName : uploadedJobFileNames,
					 isActive :active
					
				};
			 
			 	console.log("selectedData",selectedData)
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				 
			   showAjaxLoader(true);
				var url_saveAIJobUploadInfo= "/app_Admin/user/"+userID+"/aiJobs/saveAIJobUploadInfo";
				   var myAjax =  common.postAjaxCall(url_saveAIJobUploadInfo,'POST',selectedData,headers);
					myAjax.done(function(result) {
						showAjaxLoader(false);
						if(result != null){						
				    		  if(result.hasMessages){
				    			  debugger
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
				    				  $(".addAImodelDiv").hide();
				    				  $("#aiModelInfoTable").show();
				    			  }
				    		  }
						}
					});
		},
		
		
		getJobSpecificContextParams : function(id){
			var selectData = {
					id : id
			}
			$("#aiModalSelectedId").val('');
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
			    				  var modalname = result.object.aIModelName;
			    				  var aiModelContextParameters =JSON.parse(result.object.aiContextParameters);
			    				  aiJobsUpload.buildJobSpecificParamsAccordian(id,aiModelContextParameters,modalname);
			    				  
			    			  }
			    			  
			    		  }
					}
				});
		},
		
		
		buildJobSpecificParamsAccordian : function(id,aiModelContextParameters,modalname){
			debugger
			  var addNewAccordian = $("#ai_contextParameters").clone().removeClass('hidden').removeAttr('id');
			  addNewAccordian.attr('id', 'ai_contextParameters'+id);
			  debugger
			  $("#aiModalSelectedId").val(id);
			  var a_href = $(addNewAccordian).find('a');
			  $(addNewAccordian).find('.accordion').attr('id','accordion'+id);
   		   	  $(a_href).attr('data-parent','#accordion'+id);
   		   	  $(a_href).attr('href','#collapse'+id);
   		   	  var sel = $(a_href).find('.glyphicon-plus-sign');
   		   	  $('<p>'+modalname+''+'  context parameters'+'</p>').insertAfter(sel)
   		      $(addNewAccordian).find('.accordion-body').attr('id','collapse'+id);
				if ( aiModelContextParameters && aiModelContextParameters != null) {
					var addContextParam = $(addNewAccordian).find("#aicontextParameters").clone().removeClass('hidden').removeAttr("id");
					addContextParam.attr('id', 'aicontextParameters'+id)
					addNewAccordian.find("#aicontextParameters").remove();
					var count = 0;
					$.each(aiModelContextParameters,function(key,val){
						debugger;
						count++;
						var keyValuPairDiv = addContextParam.find('.keyValuPair').clone().removeClass('keyValuPair');
						keyValuPairDiv.attr('class', 'row form-group keyValuPair'+count);
						
						keyValuPairDiv.find(".authBodyKey").val(key);
						keyValuPairDiv.find(".authBodyValue").val(val);
						addContextParam.append(keyValuPairDiv);
						
        	    	   $(addNewAccordian).find("#aIContextParametersDiv").append(addContextParam);
        	    		   
					});
					addNewAccordian.find('.keyValuPair').remove();
					$("#aINewContextParametersDiv").append(addNewAccordian);
				 }
				
		},
		
		getAIUploadedJobInfo : function(id){
			var userID = $("#userID").val();
			var selectData = {
					id : id
			}
			 var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
	 		showAjaxLoader(true);
			var url_getAIUploadedJobInfo= "/app_Admin/user/"+userId+"/aiJobs/getAIUploadedJobById";
			   var myAjax = common.postAjaxCallObject(url_getAIUploadedJobInfo,'POST',selectData,headers);
				myAjax.done(function(result) {
					console.log("result---->",result);
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
			    				  aiJobsUpload.getAIUploadedJobResult(result.object)
			    			  }
			    			  
			    		  }
					}
				});
			
		},
		
		getAIUploadedJobResult : function(result){
			  debugger
			  console.log("result------->",result);
			  $(".editJobFileTypeDiv").removeClass('hidden').show();
			  $(".jobFileData").removeClass('hidden').show();
			  $(".fileTypeDiv").hide();
			  $("#aiModalName").append(result.aIModelName);
			  
			  $("#businessModalId").val(result.businessId);
			  $("#genericContextParam").val(result.aiGenericContextParam.pid);
			  $("#jobName").val(result.jobName);
			  $("#uploadedJobFileNames").val(result.jobFileName);
			  var modelId= [];
			  var contextParams = [];
			  $("#aiModalId").val(result.aiModal.id);
			  $.each(result.aiModal,function(idx,val){
				  debugger;
				  $("#aiModalId").val(val.id);
				 // modelId.push((val.id));
				  contextParams = val.aiContextParameters;
			  });
			 
	        	result.isActive ?  $('input[name="active"][value="true"]').prop('checked',true) : $('input[name="active"][value="false"]').prop('checked',true);
	        	//$("#aiModalId").val(modelId);
	        	
	        	//aiJobsUpload.buildJobSpecificParamsAccordian(modelId,contextParams);
	        	debugger
	        	
	        		var selectedJobFileNames = [];
		        	var dependencyJarsList = result.jobFileName.split(",");
		        	for(var j=0;j< dependencyJarsList.length;j++){
		        		selectedJobFileNames.push(dependencyJarsList[j]);
		        	}
		        	var container = $('.fileContainer');
		        	var textfile = container.find('.jobFileData');
		        	 $('.fileContainer').find('.editJobFileTypeDiv').empty();
		        	 $('.fileContainer').find('.addjobFile').empty();
		        	if(selectedJobFileNames.length >= 1){
		        		for(var i=0;i<selectedJobFileNames.length;i++){
		        			var inputDiv = "<div class='col-sm-4'></div><div class='col-sm-6 editJobFileTypeDiv' >"+
							"<label class='checkbox' style='margin-left: 20px;'> <input type='checkbox' name='useOldJobFile' class='useOldJobFile' id='useOldJobFile"+[i]+"'></label>"+
							"<h5 class='jobFileName' style='margin-left: 20px;'>"+
							"<input type='hidden' id ='jobFileNames' class='jobFileNames' value ='"+selectedJobFileNames[i]+"'></input> "+selectedJobFileNames[i]+" "+
							"</h5> </div>";
		        		   $(textfile).append(inputDiv);
		        		}
		        	}
		        	var container = $('.fileContainer');
		    		var jobFileDiv = container.find('.fileTypeDiv');
		    		jobFileDiv.empty();
		    		var jobFileInput = $('<input />', { type: 'file', class: 'col-sm-6 jobFile', name:'jobFile' }).appendTo(jobFileDiv);
		    		$('.jobFileData').hide();
		    		$('.fileTypeDiv').css('display','block');
	        	
			//	$("#aiModalId").multipleSelect('refresh');
		    		 $("#aiModalId").trigger("change");
		},
		
		deleteAIUploadedJobInfo : function(id){
			var userId = $("#userID").val();
			var selectData = {
					id : id
			}
			 var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
	 		showAjaxLoader(true);
			var url_deleteAIUploadedJobInfo = "/app_Admin/user/"+userId+"/aiJobs/deleteAIUploadedJobById";
			   var myAjax = common.postAjaxCallObject(url_deleteAIUploadedJobInfo,'POST',selectData,headers);
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
			    				  window.location.reload();
			    				  
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
			    					  $("#aiModalId").val(val.id);
			    				  });
			    				  $("#aiModalId").trigger("change");
			    				  //$("#aiModalId").val(result.object.aIModel.id);
			    				//  $("#aiModalId").multipleSelect('refresh');
			    				  
			    			  }
			    			  
			    		  }
					}
				});
		},
		
		validateAIJobUploadInfo : function(){
			common.clearValidations(["#businessModalId","#genericContextParam","#jobName","#aiModalId","#uploadedJobFileNames"]);
			var businessModalId = $("#businessModalId").val();
			var aiModalId = $("#aiModalId").val();
			var genericContextParamId = $("#genericContextParam").val();
			//var genericContextParamValue = $("#genericContextParam").text();
			var jobName = $("#jobName").val();
			var uploadedJobFileNames = $("#uploadedJobFileNames").val();
			var active =$("input:radio[name='active']:checked").val();
			var userID = $("#userID").val();
			var fileupload = $(".jobFile").val()
			var rid = $("#rJobId").val();
		   	var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/;
		   	var validStatus=true;
		   	/*if(businessModalId == '0' ){
	  	    	common.showcustommsg("#businessModalId", globalMessage['anvizent.package.label.pleaseSelectBusinessModal'],"#businessModalId");
	  	    	validStatus=false;
      	    }*/
		   	
		   	if(aiModalId == null ){
	  	    	common.showcustommsg("#aiModalId", globalMessage['anvizent.package.label.pleaseSelectAiModel'],"#aiModalId");
	  	    	validStatus=false;
      	    }
		   	
		  /* 	if(genericContextParamId == '0' ){
	  	    	common.showcustommsg("#genericContextParam", globalMessage['anvizent.package.label.pleaseSelectGenericContextParams'],"#genericContextParam");
	  	    	validStatus=false;
      	    }*/
		   	
		   	if(jobName == '' ){
	  	    	common.showcustommsg("#jobName", globalMessage['anvizent.package.label.pleaseEnterJobName'],"#jobName");
	  	    	validStatus=false;
      	    }
		   	
			if(uploadedJobFileNames == '' ){
	  	    	common.showcustommsg("#uploadedJobFileNames", globalMessage['anvizent.package.label.pleaseChooseFile'],"#uploadedJobFileNames");
	  	    	validStatus=false;
      	    }

			var fileExtension = fileupload.replace(/^.*\./, '');
		    if(fileExtension != 'R' && fileExtension != 'r') {
		    	common.showcustommsg(".jobFile",globalMessage['anvizent.package.label.pleaseChooseRFile']);
		    	validStatus=false;
		    }
			
	      	  return validStatus;
			
		}
}

if ($('.aiJobsUpload-page').length) {
	debugger
	aiJobsUpload.initialPage();
	//aiJobsUpload.getAIJobUploadList();

	$(document).on("click", "#addAIJobsUpload", function() {
		common.clearValidations(["#businessModalId","#genericContextParam","#jobName","#aiModalId","#uploadedJobFileNames",".jobFile"]);
		aiJobsUpload.cleanupAIJobUpload();
		var container = $('.fileContainer');
		var jobFileDiv = container.find('.fileTypeDiv');
		jobFileDiv.empty();
		var jobFileInput = $('<input />', { type: 'file', class: 'col-sm-6 jobFile', name:'jobFile' }).appendTo(jobFileDiv);
		$('.jobFileData').hide();
		$('.fileTypeDiv').css('display','block');
		$("#rJobId").val('');
		$(".aiSpecificContextParamsDiv").hide();
		aiJobsUpload.selectFieldsData(0);
		$("#uploadedJobFileNames").val('');
		$("#aiModalId").val([]);
	  // 	$("#aiModalId").multipleSelect('refresh');
	   
	});

	 $("#aiJobsUploadTable").on("click","#aiJobsUploadEdit",function(){
		 aiJobsUpload.cleanupAIJobUpload();
		  var aiJobUploadId =$(this).attr("data-sourceid");
		  aiJobsUpload.selectFieldsData(aiJobUploadId);
		 $("#aiJobsUploadId").val(aiJobUploadId);
	  });
	
	 $(document).on('click', '#saveAIJobUpload,#updateAIJobUpload', function(){
		 debugger
				var userID = $("#userID").val();
				var status = aiJobsUpload.validateAIJobUploadInfo();
				var modelName = $("#aiModalId option:selected").text();
				if(!status){
					return false;
				}
		 		var rid = $("#rJobId").val();
		 		
		 			var formData = new FormData($("#jobFileForm_direct")[0]);
					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					headers[header] = token;
					showAjaxLoader(true);
					var url = "/app_Admin/user/"+userID+"/aiJobs/saveAIFileUpload/"+modelName ;
					var myAjax = common.postAjaxCallForFileUpload(url, 'POST',formData, headers);
					myAjax.done(function(result) {
						showAjaxLoader(false);
						if (result != null && result.hasMessages) {
							if (result.messages[0].code == "SUCCESS") {
								var result = result.object;
								if(result){
									debugger
									aiJobsUpload.saveAIUploadInfo();
								}
								$('#aiJobsUploadDiv').modal('hide');
							}
						}

					});
		 			
				
			});
	 
	 $(document).on("change",".jobFile",function(){
		 debugger
		 aiJobsUpload.updateUploadedFileNames();
		});
	    
		$(document).on("click"," .addJobFile, .deleteJobFile",function(e){
			 e.preventDefault();
		});
		
		$(document).on("click",".useOldJobFile",function(){
			aiJobsUpload.updateUploadedFileNames();
		});
		
		$(document).on("click",".addJobFile",function(){
			var container = $(".jobFilesDiv");
			var jobFileContainer = $(".job-files-div .fileContainer").clone();
			$(jobFileContainer).find(".jobfile-label").text("");
			$(jobFileContainer).find(".deleteJobFile").css("display","inline-block");
			container.append($(jobFileContainer));
		});
		
		$(document).on("click",".deleteJobFile",function(){
			$(this).parents(".fileContainer").remove();
			aiJobsUpload.updateUploadedFileNames();
		});
		
		$("#aINewContextParametersDiv").on("click",".addContextParams",function(){
			debugger
			var addBodyParam = $(".keyValuPair").clone().removeClass('.keyValuPair');
			var _thisDiv = $(this).parents('.contextKeyValue').find('.row');
			var _thisDivLength =  _thisDiv.length+1;
			 $(addBodyParam).attr('class', 'row form-group keyValuPair'+_thisDivLength);
			$($(this).parents('.contextKeyValue')).append(addBodyParam);
		});
		
		$("#aINewContextParametersDiv").on("click",".deleteContextParams",function(){
			var _deleteDiv = $(this).parents('.row').attr('class').split(' ')[2];
			var _aiDiv = $(this).parents('.contextKeyValue');
			_aiDiv.find('.'+_deleteDiv).remove();
		});
		
		$(document).on("change","#aiModalId",function(){
			debugger
			console.log("checking")
			$("#aIContextParametersDiv .contextKeyValue").slice(0).remove(); 
				var aiModalLength = $("#aiModalId option:selected").length
				$("#aINewContextParametersDiv").empty();
				if(aiModalLength != 0){
					$(".aiSpecificContextParamsDiv").show();
					for(var i=0;i<=aiModalLength-1;i++){
						var aiModalId = $(this).val();
						if(aiModalId != ''){
							aiJobsUpload.getJobSpecificContextParams(aiModalId);
						}	
					}
				}else{
					$(".aiSpecificContextParamsDiv").hide();
				}
				
				// e.stopImmediatePropagation();
		});
		
		$(document).on("click",".editAIUplodedJob",function(){
			
			// aiJobsUpload.cleanupAIJobUpload();
			common.clearValidations(["#businessModalId","#genericContextParam","#jobName","#aiModalId","#uploadedJobFileNames",".jobFile"]);
			 var id = $(this).attr("data-sourceid");
			 aiJobsUpload.selectFieldsData(id);
			 $("#aiJobsUploadDiv").modal("show");
			
			$("#aIContextParametersDiv .contextKeyValue").slice(0).remove(); 
			
			$("#rJobId").val(id);
			aiJobsUpload.getAIUploadedJobInfo(id);
		});
		var id;
		$(document).on("click",".deleteAIUplodedJob",function(){
			id = $(this).val();
			$("#deleteAIJobsUpload").modal('show');
		});
		
		$(document).on("click","#confirmDeleteAIJobsUpload",function(){
			aiJobsUpload.deleteAIUploadedJobInfo(id);
			$("#deleteAIJobsUpload").modal('hide');
		});
		
		
		$(document).on("change","#businessModalId",function(){
			var bmId = $(this).val();
			aiJobsUpload.getAIModalInfoByBMID(bmId);
		})

}