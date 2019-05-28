var headers = {
		
};
var  aiContextParams = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;

			$("#rJobExecutionTable").DataTable({
				"order": [[0, "desc" ]],"language": {
		            "url": selectedLocalePath
		        }
			});

			$("#aiGlobalContextParamTbl").DataTable({
				"order": [[0, "asc" ]],"language": {
		            "url": selectedLocalePath
		        }
			
			});
			$("#CommmonJobTbl").DataTable({
				"order": [[0, "asc" ]],"language": {
		            "url": selectedLocalePath
		        }
			
			});
		},
		validateAiContextParamFields : function(){
			common.clearValidations(["#paramName"]);
			 var paramName = $("#paramName").val().trim();
			 var paramValue = $("#paramValue").val();
			 var isActive= $("input[name='isActive']:checked").val();
			 var pid =  $("#pid").val();
			
		   	    var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/;
			 
		   	 var validStatus=true;
	      	    if(paramName == '' ){
		  	    	common.showcustommsg("#paramName", globalMessage['anvizent.package.label.pleaseEnterParamName'],"#paramName");
		  	    	validStatus=false;
	      	    } else if(!regex.test(paramName)){
	      	    	common.showcustommsg("#paramName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#paramName");
		      	      validStatus=false;
	      	    }
	      	  
	      	  return validStatus;
		},
		
		updateUploadedFileNames : function(){
			var fileNames = "";
			var i = 0;
			var jobfile = $(".job-files-div .fileContainer").find(".jobFile").val();
			var firstFile = jobfile.substring(jobfile.lastIndexOf('\\')+1 , jobfile.length);
			fileNames+=firstFile;
			$(".jobFilesDiv .fileContainer").find(".jobFile").each(function(){
				var filePath = $(this).val();
				var fileName = filePath.substring(filePath.lastIndexOf('\\')+1 , filePath.length);
				if((fileNames != '' && fileName != '')){
					fileNames+=",";
				}
				fileNames+=fileName;
			});
			$("#uploadedJobFileNames, .uploadedJobFileNames").val(fileNames);
		},
		
		validataAiCommonJobs : function(){
			common.clearValidations($(".job-files-div .fileContainer").find(".jobFile"));
			var validStatus = true;
			 var jobfile = $(".job-files-div .fileContainer").find(".jobFile").val();
			 var firstFile = jobfile.substring(jobfile.lastIndexOf('\\')+1 , jobfile.length);
			 
			 if(firstFile != ""){
				 var path = $(".job-files-div .fileContainer").find(".jobFile");
				 var filePath = $(".job-files-div .fileContainer").find(".jobFile").val();
				 var fileExtension = filePath.replace(/^.*\./, '');
				    if(fileExtension != 'r' && fileExtension != 'R') {
				    	common.showcustommsg(path,globalMessage['anvizent.package.label.pleaseChooseRFile']);
				    	validStatus=false;
				    }
			 }else{
				 var path = $(".job-files-div .fileContainer").find(".jobFile");
				 common.showcustommsg(path,globalMessage['anvizent.package.label.pleaseChooseRFile']);
			    	validStatus=false;
			 }
			 
			 $(".jobFilesDiv .fileContainer").find(".jobFile").each(function(){
					var filePath = $(this).val();
					var fileName = filePath.substring(filePath.lastIndexOf('\\')+1 , filePath.length);
					var fileExtension = filePath.replace(/^.*\./, '');
				    if(fileExtension != 'r' && fileExtension != 'R') {
				    	common.showcustommsg($(this),globalMessage['anvizent.package.label.pleaseChooseRFile']);
				    	validStatus = false;
				    }
				});
			 
			 return validStatus;
		},
		
		viewErrorLog :  function(result){
			  if(result.messages[0].code == "SUCCESS") {
					 var  messages=[{
						  code : result.messages[0].code,
						  text : result.messages[0].text
					  }];
					 var errorLogInfo = result.object;
						
					 
					 if(errorLogInfo === "" || errorLogInfo === null){
						 errorLogInfo = "File Not Data Found.";
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
				          popup.document.title = "Error Log";
				          popup.document.body.innerHTML = "<pre>"+errorLogInfo+"</pre>";
				          if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
				        	  popup.addEventListener (
					        	        "load",
					        	        function () {
					        	            var destDoc = popup.document;
					        	            destDoc.open ();
					        	            destDoc.title = "AI Job Execution";
					        	            destDoc.write ('<html><head></head><body><pre>'+errorLogInfo+'</pre></body></html>');
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

if($('.aiContextParameters-page').length || $('.aiCommonJob-page').length || $('.rJobExecution-page').length){
	aiContextParams.initialPage();
	
	$("#AIContextParametersInfoTable").show();
	
	
	$(document).on("click",".addAIContextParameters",function(){
		$("#pid").val('');
		$(".addAIContextParametersDiv").show();
		$("#AIContextParametersInfoTable").hide();
		common.clearValidations(["#paramName"]);
		$('input[name="isActive"][value="true"]').prop('checked',true)
		$("#paramName").val('');
		$("#paramValue").val('');
	});

	
	$(document).on("click","#back",function(){
		$(".addAIContextParametersDiv").hide();
		$("#AIContextParametersInfoTable").show();
	})
	
	$(document).on("click","#SaveAiContextParameters",function(){
		
		 var paramName = $("#paramName").val();
		 var paramValue = $("#paramValue").val();
		 var isActive= $("input[name='isActive']:checked").val();
		 var pid =  $("#pid").val();
		 
		 var userId = $("#userID").val();
		
		  var status = aiContextParams.validateAiContextParamFields()
		 
		 if(!status){
			 return false
		 }else{
			 var selectedData = {
					 "pid":pid,
					 "paramName": paramName,
					 "paramValue" : paramValue,
					 "active" : isActive
				};
			 
			 console.log("selectedData",selectedData)
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				 
			   showAjaxLoader(true);
				var url_saveBusinessModalInfo = "/app_Admin/user/"+userId+"/aiJobs/saveAiContextParameters";
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
				    				  $(".addAIContextParametersDiv").hide();
				    				  $("#AIContextParametersInfoTable").show();
				    			  }
				    		  }
						}
					});
		 }
			 
	
		})
		
		
	$(document).on("click",".editDetails",function(){
		$(".addAIContextParametersDiv").show();
		common.clearValidations(["#paramName"]);
		$("#AIContextParametersInfoTable").hide();
		var userId = $("#userID").val();
		var id = $(this).val();
		$("#pid").val(id);
		var selectData = {
				id : id,
		}
		 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
 		showAjaxLoader(true);
		var url_getS3BucketInfoById = "/app_Admin/user/"+userId+"/aiJobs/getAiContextParametersById";
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
		    				  $("#paramName").val(result.object.paramName);
		    				  $("#paramValue").val(result.object.paramValue);
		    				  debugger
		    				  if(result.object.active){
		    					  $("#isActiveYes").prop("checked",true);  
		    				  }else{
		    					  $("#isActiveNo").prop("checked",true);
		    				  }
		    				  
		    			  }
		    			  
		    		  }
				}
			});
		
	})
	
	var id ;
	$(document).on("click",".deleteAiContextParameters",function(){
		 id = $(this).val();
		 $("#deleteAIContextParam").modal('show');
	});
	
	$(document).on("click","#confirmDeleteAIContextParam",function(){
		$("#AIContextParametersInfoTable").show();
		$("#deleteAIContextParam").modal('hide');
		var userId = $("#userID").val();
		$("#pid").val(id);
		var selectedData = {
				id : id,
		}
		 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
 		showAjaxLoader(true);
		var url_getS3BucketInfoById = "/app_Admin/user/"+userId+"/aiJobs/deleteAiContextParametersById";
		   var myAjax = common.postAjaxCallObject(url_getS3BucketInfoById,'POST',selectedData,headers);
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
		    				  $(".addAIContextParametersDiv").hide();
		    				  
		    			  }
		    			  
		    		  }
				}
			});
		
	})
	
	/*$(document).on("click",".addCommmonJob",function(){
		$(".addCommonJobDiv").show();
		$("#commonJobInfoTable").hide();
		
	});*/

	 $(document).on("change",".jobFile",function(){
		 aiContextParams.updateUploadedFileNames();
	});
	
	$(document).on("click"," .addJobFile, .deleteJobFile",function(e){
		 e.preventDefault();
	});
	
	$(document).on("click",".addJobFile",function(){
		debugger
		var container = $(".jobFilesDiv");
		var jobFileContainer = $(".job-files-div .fileContainer").clone();
		$(jobFileContainer).find(".jobfile-label").text("");
		$(jobFileContainer).find(".jobFile").val("");
		$(jobFileContainer).find(".deleteJobFile").css("display","inline-block");
		common.clearValidations($(jobFileContainer).find(".jobFile"));
		container.append($(jobFileContainer));
	});
	
	$(document).on("click",".deleteJobFile",function(){
		$(this).parents(".fileContainer").remove();
		aiContextParams.updateUploadedFileNames();
	});
	
	$(document).on('click', '#saveAICommonJob', function(){
		 debugger
		 var status = aiContextParams.validataAiCommonJobs();
		 if(!status){
			 return false;
		 }
					var userID = $("#userID").val();
		 			var formData = new FormData($("#jobFileForm_direct")[0]);
					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					headers[header] = token;
					showAjaxLoader(true);
					var url = "/app_Admin/user/"+userID+"/aiJobs/saveCommonAIFileUpload" ;
					var myAjax = common.postAjaxCallForFileUpload(url, 'POST',formData, headers);
					myAjax.done(function(result) {
						showAjaxLoader(false);
						if (result != null && result.hasMessages) {
							if(result.messages[0].code=="ERROR"){
		    					  var message = result.messages[0].text;
		    					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
			    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
			    			  }
			    			  if(result.messages[0].code=="SUCCESS"){
			    				  var message = result.messages[0].text;
			    				  window.location.reload();
		    					  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>").show();
			    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 5000);
			    			  }
						}

					});
		 			
				
			});
	var fileName;
	$(document).on("click",".deleteCommonJobFile",function(){
		fileName = $(this).val();
		$("#deleteAICommonJob").modal('show');
	});
	
	
	$(document).on("click","#confirmDeleteAICommonJob",function(){
		var userID = $("#userId").val();
		$("#deleteAICommonJob").modal('hide');
		var selectedData = {
				fileName : fileName,
		}
		 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
 		showAjaxLoader(true);
		var url_getS3BucketInfoById = "/app_Admin/user/"+userID+"/aiJobs/deleteCommonJob";
		   var myAjax = common.postAjaxCallObject(url_getS3BucketInfoById,'POST',selectedData,headers);
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
		    				  
		    			  }
		    			  
		    		  }
				}
			});
		
	})
	
	$(document).on("click",".aiJobRefresh",function(){
		window.location.reload();
	})
	
	$(document).on("click","#viewErrorLog",function(){
		debugger
		var errorLog = $(this).attr("data-errorLog"); 
		var userID = $("#userID").val();
		var selectData = {
				errorLogName : errorLog
		};
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
		 showAjaxLoader(true);
		 var url_getErrorLogInfo = "/app_Admin/user/"+userID+"/aiJobs/getAIErrorLogInfo";
		   var myAjax = common.postAjaxCallObject(url_getErrorLogInfo,'POST',selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    		  if(result != null && result.hasMessages){ 
		    			  aiContextParams.viewErrorLog(result); 
		    		  }else{
		    			  aiContextParams.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });
	})

}