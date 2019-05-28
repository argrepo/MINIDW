var validateBusinessCases = {
		initialPage : function(){
			
			$(".dlList").multipleSelect({
				filter : true,
				placeholder : 'Select Module / DL',
			    enableCaseInsensitiveFiltering: true
			});
			
			$("#validationTypeId").select2({               
                allowClear: true,
                theme: "classic"
			});
			
			$(".datavalidationContextParams").multipleSelect({
				filter : true,
				placeholder : 'Select Context Parameters',
			    enableCaseInsensitiveFiltering: true
			});
			
			$(".existedJarFile").multipleSelect({
				filter : true,
				placeholder : 'Select jar files',
			    enableCaseInsensitiveFiltering: true
			});
			
		},
		
		postLoadCreationFormValidation : function(){
			debugger;
			 var validStatus=true;
			 var validationName = $("#validationName").val();
			 var validationScripts = $("#validationScripts").val();
			 var dlInfo =$("#dlList").multipleSelect("getSelects","text");
			 validationType = $("#validationTypeId").val();
			  //jobName = $(".jobName").val(),
			  //uploadedJobFileNames = $(".uploadedJobFileNames").val(); , .jobName, .jobFile, .uploadedJobFileNames, .datavalidationContextParams
			 common.clearValidations(['#validationName, #validationScripts, #isActive, #dlList']);
			 var validStatus=true;
			 
			 /*if(jobName == ''){
		   	    	common.showcustommsg(".jobName", globalMessage['anvizent.package.label.pleaseEnterJobName'],".jobName");
		   	        validStatus=false;
		   	    }else if(!/^[a-zA-Z0-9_.]*$/.test(jobName.trim())){
		   	    	 common.showcustommsg(".jobName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],".jobName");
		   	    	 validStatus=false;
		   	    	
		   	    }
		   	    if($("select.datavalidationContextParams option:selected").length == 0){
					common.showcustommsg("div.datavalidationContextParams", globalMessage['anvizent.package.label.pleasechooseContextParameters']);
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
				$(".jobFilesDiv").find(".jobFile").each(function(){	   	
					var filePath = $(this).val(); 
					if(filePath != "")
	    		    jobFiles.push(filePath.substring(filePath.lastIndexOf('\\')+1 , filePath.length));
				});
				$(".jobFilesDiv .fileContainer").find(".jobFileNames").each(function(){
					if($(this).parents(".fileContainer").find(".useOldJobFile").is(":checked")){
						var fileName = $(this).val();
						if(fileName != ''){
							jobFiles.push(fileName);
						}
					}
				});
				$(".existedJarFile option:selected").each(function(){
					var fileName = $(this).val();
					if(fileName != ''){
						jobFiles.push(fileName);
					}
				}); 
				
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
		   	    }*/
			 
			 if(validationName == ''){
				 common.showcustommsg("#validationName", globalMessage['anvizent.package.label.pleaseEnterValidationName'], "#validationName");
				 validStatus = false;
			 }
			 if(validationScripts == ''){
				 common.showcustommsg("#validationScripts", globalMessage['anvizent.package.label.pleaseEnterValidationScripts'], "#validationScripts");
				 validStatus = false;
			 }
			 if(dlInfo.length == 0){
				 common.showcustommsg("#dlList", globalMessage['anvizent.package.label.pleaseChoosedls'], "#dlList");
				 validStatus = false;
			 }
			 if(validationType == "null" || validationType == ''){
				 common.showcustommsg("#validationTypeId", globalMessage['anvizent.package.label.pleaseChooseValidationtype'], "#validationTypeId");
				 validStatus = false;
			 }
			 return validStatus;
		},
		
		 viewExecutionStatusComments :  function(result){
			  if(result.messages[0].code == "SUCCESS") {
					 var  messages=[{
						  code : result.messages[0].code,
						  text : result.messages[0].text
					  }];
					 var executionComments = result.object;
						
					 
					 if(executionComments === "" || executionComments === null){
						 executionComments = "Execution comments not found.";
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
				          popup.document.title = "Upload Or Execution Status Comments";
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
	
				          
				          
				  }else {
			    		common.displayMessages(result.messages);
			    	}
		  
		},
		viewValidationScript : function(result){
			var scriptData = result;
			if(scriptData == '' || scriptData == null){
				scriptData = 'No Script Found.';
			}
			var params ="";
			var ua = window.navigator.userAgent;
		    var msie = ua.indexOf("MSIE ");
		    
		    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))  // If Internet Explorer, return version number
		    {
		    	params = [
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
	          popup.document.title = "Validation Script";
	          popup.document.body.innerHTML = "<pre>"+scriptData+"</pre>";
	          if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
	        	  popup.addEventListener (
		        	        "load",
		        	        function () {
		        	            var destDoc = popup.document;
		        	            destDoc.open ();
		        	            destDoc.title = "Script Layout";
		        	            destDoc.write ('<html><head></head><body><pre>'+scriptData+'</pre></body></html>');
		        	            destDoc.close ();
		        	        },
		        	        false
		        	    );
	          }

		},
		showMessage:function(text){
			$(".messageText").empty();
			$(".successMessageText").empty();
			$(".messageText").html(text);
		    $(".message").show();
		   setTimeout(function() { $(".message").hide(); }, 10000);
	 },
	 
	 viewScriptExecutionResultsPopup : function(result){
		 
	 },
	 
	 updateUploadedFileNames : function(){
			var fileNames = "";
			
			$(".jobFilesDiv .fileContainer").find(".jobFileNames").each(function(){
				var fileName = "";
				if($(this).parents(".fileContainer").find(".useOldJobFile").is(":checked")){
					fileName = $(this).val();
					if(fileNames != '' && fileName != ''){
						fileNames+=",";
					}
					fileNames+=fileName;
				}
			});
			
			$(".jobFilesDiv .fileContainer").find(".jobFile").each(function(){
				var filePath = $(this).val();
				var fileName = filePath.substring(filePath.lastIndexOf('\\')+1 , filePath.length);
				if(fileNames != '' && fileName != ''){
					fileNames+=",";
				}
				fileNames+=fileName;
			});
			
			$(".existedJarFile option:selected").each(function(){
				var fileName = $(this).val();
				if(fileNames != '' && fileName != ''){
					fileNames+=",";
				}
				fileNames+=fileName;
			});
			$("#uploadedJobFileNames, .uploadedJobFileNames").val(fileNames);
		},
		
}

if($('.validateBusinessCases-page').length){
	validateBusinessCases.initialPage();
	var table = $(".validationTbl").DataTable( {  "order": [[1, "desc" ]],"language": {
        "url": selectedLocalePath
    } } );
	
	if ($("#dlList").length) {
		$("#dlList").multipleSelect({ filter : true, placeholder : "Select Module / DL", });
	}
	

	$(document).on("click",".selectAll",function(){
		var isChecked = this.checked;
		$(this).parents('.table').find("input[type='checkbox']").each(function(){
			if (this.checked != isChecked ) {
				this.checked = !isChecked;
				$(this).trigger("click");
			}
		});
	});
	
	$(document).on('click', '#viewValidationscript', function(){
		var validationscript = $(this).attr('data-validationscript');
		
		 if(validationscript !=null || validationscript==''){
			 validateBusinessCases.viewValidationScript(validationscript);
		 }else{
			 validateBusinessCases.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		 }
	});
	
	$(document).on('click', '#viewjobResults', function(){
		var validationscriptId = $(this).attr('data-validationscriptId');
		var userId = $('#userID').val();
		var selectData = {
				scriptId : validationscriptId
		  };
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
		
		showAjaxLoader(true);
		var url = "/app/user/"+userId+"/package/viewJobResults/"+validationscriptId;
		var myAjax = common.loadAjaxCall(url,'GET', '', headers);
		 myAjax.done(function(result) {
			 showAjaxLoader(false);
			 if(result != null && result.hasMessages){ 
				validateBusinessCases.viewExecutionStatusComments(result); 
   		    }else{
 			   validateBusinessCases.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
 		    }
		 });
	});
	
	$(document).on('click', '#runScript', function(){
		var validationscriptId = $(this).attr('data-validationscriptId');
		var validationTypeId = $("#datavalidationTypeid").val();
		var userId = $('#userID').val();
		var selectData = {
				scriptId : validationscriptId,
				validationTypeId : validationTypeId
		};
		
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
		
		showAjaxLoader(true);
		var url_runScript = "/app_Admin/user/"+userId+"/etlAdmin/runValidationScriptById";
		var myAjax = common.loadAjaxCall(url_runScript,'POST', selectData, headers);
		myAjax.done(function(result){
			showAjaxLoader(false);
			if(result != null && result.hasMessages){
					if(result.messages[0].code == "SUCCESS"){
				    	$("#messagePopUp").modal('show');
				    		$("#popUpMessage").text(globalMessage['anvizent.package.label.successfullyValidated']).addClass('alert-success').removeClass('alert-danger');
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
	
	$("button#validatebutton").on('click', function() {
		var userID = $("#userID").val();
		var selScripts = $("#tvalidateBusinesscasesTable").find("input.jcolumn-check:checked");
		var selScriptIds = [];
		if(selScripts.length == 0){
			$("#messagePopUp").modal('show');
			$("#popUpMessage").text(globalMessage['anvizent.package.label.selectAtleastonescript']).addClass("alert-danger").removeClass('alert-success');
			return false;
		}
		var validationTypeId = $("#datavalidationTypeid").val();
		table.$(".jcolumn-check:checked").each(function(){
			selScriptIds.push({"scriptId":$(this).data("scriptid"),"validationTypeId":validationTypeId});
		});
		
		var selectedData = {
				targetTables : selScriptIds
		}
		
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
		
		showAjaxLoader(true);
		var url = "/app_Admin/user/"+userID+"/etlAdmin/executeSeletedScripts";
		var myAjax = common.loadAjaxCall(url,'POST', selScriptIds, headers);
		
		myAjax.done(function(result){
			showAjaxLoader(false);
			if(result != null && result.hasMessages){
	    		if(result.messages[0].code == "SUCCESS"){
			    	$("#messagePopUp").modal('show');
			    		$("#popUpMessage").text(globalMessage['anvizent.package.label.successfullyValidated']).addClass('alert-success').removeClass('alert-danger');
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
	
	$(document).on('click', '#resetBusinessCasesValidation', function(){
		common.clearValidations(['#validationName', '#validationScripts', 'isActive', '#dlList']);
		$('#dlList').multipleSelect("uncheckAll");
		$("#validationName").val("");
		$('#validationScripts').val("");
		$("#active1").attr("checked",true);
	});
	
	$("#addBusinessCasesValidation").on('click', function() {
		 
	       var status= validateBusinessCases.postLoadCreationFormValidation();
		   if(!status){ return false;}
		    
	       var selectors = [];
		
	       selectors.push('#validationName');
	       selectors.push('#validationScripts');
		    
	        var valid = common.validate(selectors);
		
		    if(!valid){ return false;}
		
			var userID=$('#userID').val();
			$("#dataValidationForm").prop("action",$("#addUrl").val()); 
			this.form.submit();
		    showAjaxLoader(true);
			 
	 });
	
	$("#updateBusinessCasesValidation").on('click', function() {
		 
		   var status= validateBusinessCases.postLoadCreationFormValidation();
		   if(!status){ return false;}
		   var selectors = [];
		
		   selectors.push('#validationName');
	       selectors.push('#validationScripts');
		    
	        var valid = common.validate(selectors);
		
		    if(!valid){ return false;}
			var userID=$('#userID').val();
			$("#dataValidationForm").prop("action",$("#updateUrl").val()); 
			this.form.submit();
			showAjaxLoader(true);
      });
	
	$(document).on("change",".jobFile, .existedJarFile",function(){
		validateBusinessCases.updateUploadedFileNames();
	});
    
	$(document).on("click"," .addJobFile, .deleteJobFile",function(e){
		 e.preventDefault();
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
		validateBusinessCases.updateUploadedFileNames();
	});
	
	$(document).on("click",".prepareStatement",function(){
		if($(this).is(":checked")){
			$(this).val("true");
		}
		else{
			$(this).val("false");
		}
	});	
}