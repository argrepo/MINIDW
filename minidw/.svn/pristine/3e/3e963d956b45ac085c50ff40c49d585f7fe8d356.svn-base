var headers = {};
var iLInfo = {
		initialPage : function(){
			$("#existingILsTable,#commonJobsInfoTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
			
			$(".contextParamsDiv,.jobNameDiv,.jobFileNameDiv,.jobFilesDiv,.existedJarFileDiv").removeClass('hidden').show();
			$(".iLContextParams").multipleSelect({
				filter : true,
				placeholder : 'Select Context Parameters',
			    enableCaseInsensitiveFiltering: true
			});
			
			$("#connectors").multipleSelect({
				filter : true,
				placeholder : globalMessage['anvizent.package.label.selectConnectors'],
			    enableCaseInsensitiveFiltering: true
			});
			
			$("#clientId").select2({               
                allowClear: true,
                theme: "classic"
			});
			$(".existedJarFile").multipleSelect({
				filter : true,
				placeholder : 'Select jar files',
			    enableCaseInsensitiveFiltering: true
			});
			
			setTimeout(function() { $("#pageErrors").hide(); }, 5000);
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		},
		ViewILDetailsPanel : function(iLDetails){
			debugger
			var iLInfo = iLDetails.ilInfo;
			$(".iLName").val(iLInfo.iL_name).attr("disabled",true);			
			$(".iLDescription").val(iLInfo.description).attr("disabled",true);
			$(".iLTableName").val(iLInfo.iL_table_name).attr("disabled",true);
			$(".xrefILTableName").val(iLInfo.xref_il_table_name).attr("disabled",true);
			$("input[name='iLType'], input[name='isActive'], .iLPurgeScript").attr("disabled",true);
			var purgeScripts = iLInfo.purgeScripts;
			$(".iLPurgeScript").val(purgeScripts);
			var jobTagId = iLInfo.jobTagId;
			var loadParameterId = iLInfo.loadParameterId;
			
			if(iLInfo.iLType == "D")
				$("input[name='iLType'][value='D']").prop("checked",true);
			else if(iLInfo.iLType == "T")
				$("input[name='iLType'][value='T']").prop("checked",true);	
			if(iLInfo.isActive)
				$("input[name='isActive'][value='true']").prop("checked",true);
			else
				$("input[name='isActive'][value='false']").prop("checked",true);
		
			
			if(iLInfo.jobExecutionType == "E"){
				$("input[name='jobExecutionType'][value='E']").prop("checked",true);
				$(".jobTags_Div,.loadParamters_Div").removeClass('hidden').show();
				$(".jobTagIdVal").val(jobTagId);
				$(".loadParamtersVal").val(loadParameterId);
				$(".iLContextParams_div").hide();	
			}
			else {
				$("input[name='jobExecutionType'][value='T']").prop("checked",true);
				$(".jobTags_Div,.loadParamters_Div").hide();
				$(".iLContextParams_div").removeClass('hidden').show();
				
				var contextParams = iLDetails.eTLJobContextParamList;
				var options = "";
				$.each(contextParams, function(i,val){
					options += "<option selected value='"+val.paramId+"'>"+val.paramValue+"</option>";
				});

				$(".iLContextParams").empty().append(options).multipleSelect({
					filter : true,
					placeholder : 'Select Context Parameters',
				    enableCaseInsensitiveFiltering: true,
				    selectAll: true,
				    allSelected: 'All selected'
				});
				$('.iLContextParams').find('input').each(function(){
				    $(this).prop('disabled', true);
				});
				
				$(".jobName").val(iLInfo.jobName).attr("disabled",true);
				
				$(".ms-choice").prop("disabled",true);
			}
				
			
		},
		validateILUpdateForm : function(){
			common.clearValidations([".version, .iLName,.iLDescription, .iLTableName, .xrefILTableName, .jobName, .jobFile, .iLContextParams, .uploadedJobFileNames"]);
			var validStatus = true;
			var regex = /^[0-9a-zA-Z\_ ]+$/;
			var version_regex = /^[0-9a-zA-Z/ /_/./-]+$/;
			var	iLName = $(".iLName").val(),
				iLTableName = $(".iLTableName").val(),
				jobName = $(".jobName").val(),
				xrefILTableName = $(".xrefILTableName").val(),
				iLType = $("input[name='iLType']:checked").val() == 'D' ? 'D' : 'T',
				uploadedJobFileNames = $(".uploadedJobFileNames").val(),
				version = $(".version").val();
			
				jobExecutionType = $("input[name='jobExecutionType']:checked").val(),
				jobTagId = $(".jobTags").val(),
				loadParamterId = $(".loadParameters").val();
			
				
				if(jobExecutionType == 'E'){
					
					if(jobTagId == 0){
						common.showcustommsg("#jobTagId","Please Choose Job tags");
						validStatus=false; 
					}	
			   	    
			   	    if(loadParamterId == 0){
						common.showcustommsg("#loadParameterId","Please Choose Load Paramter");
						validStatus=false; 
					}	
				}else{
					var jobFiles = [];
					$(".jobFilesDiv .fileContainer").find(".jobFile").each(function(){	   	
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
					
					if($("select.iLContextParams option:selected").length == 0){
						common.showcustommsg("div.iLContextParams", globalMessage['anvizent.package.label.pleasechooseContextParameters']);
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
				   	    
				   	 if(uploadedJobFileNames == ''){
				   	    	common.showcustommsg(".uploadedJobFileNames", globalMessage['anvizent.package.label.pleaseAddAtLeastOneJobFile'],".uploadedJobFileNames");
			   				validStatus=false;
			   	    }
				   	 
				   	if(jobName == ''){
			   	    	common.showcustommsg(".jobName", globalMessage['anvizent.package.label.pleaseEnterJobName'],".jobName");
			   	        validStatus=false;
			   	    }else if(!/^[a-zA-Z0-9_.]*$/.test(jobName.trim())){
			   	    	 common.showcustommsg(".jobName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],".jobName");
			   	    	 validStatus=false;
			   	    	
			   	    }
				   	   
				}
			
			
			
			if(iLName == '' ){
	   	    	 common.showcustommsg(".iLName", globalMessage['anvizent.package.label.pleaseEnterILName'],"iLName");
	   	    	 validStatus=false;
	   	    }
			else if(!regex.test(iLName)){
				common.showcustommsg(".iLName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],".iLName");
      	      	validStatus=false;
			}
			if(version == ''){
	      		 common.showcustommsg(".version", globalMessage['anvizent.package.label.pleaseEnterVersion'], ".version");
	      		 validStatus=false;
      	  	}
      	  	else if(!version_regex.test(version)){
      	  		common.showcustommsg(".version", globalMessage['anvizent.package.message.specialCharacters_and-and.AreOnlyAllowed'], ".version");
      	  		validStatus = false;
      	  	} 
			
			if(iLTableName == '' ){
		    	 common.showcustommsg(".iLTableName", globalMessage['anvizent.package.label.pleaseEnterILTableName'],".iLTableName");
		    	 validStatus=false;
		    }else if(!/^[a-zA-Z0-9_]*$/.test(iLTableName.trim())){
	   	    	 common.showcustommsg(".iLTableName", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters']+" "+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInTableName'],".iLTableName");
	   	    	 validStatus=false;
	   	    }
			if(!/^[a-zA-Z0-9_]*$/.test(xrefILTableName.trim()) && iLType == 'D'){
	   	    	common.showcustommsg(".xrefILTableName", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters']+" "+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInTableName'],".xrefILTableName");
	   	        validStatus=false;
	   	    }
			
	   	   
	   	    
	   	    if(validStatus){
	   	    	var selectors = [];
		   	    selectors.push(".iLName");
		   	    selectors.push(".iLTableName");
		   	    selectors.push(".iLDescription");
		   	    if(iLType == 'D')
		   	    	selectors.push(".xrefILTableName");
		   	    selectors.push(".jobName");
		   	    validStatus = iLInfo.validateMinMaxFieldLengths(selectors);
	   	    }
	   	    
	   	   return validStatus; 
		},
		validateILCreationForm : function(){
			common.clearValidations([".version, .iLName,.iLDescription, .iLTableName, .xrefILTableName, .jobName, .jobFile, .iLContextParams, " +
					".iLPurgeScript, .uploadedJobFileNames"]);
			var validStatus = true;
			var regex = /^[0-9a-zA-Z\_ ]+$/;
			var version_regex = /^[0-9a-zA-Z/ /_/./-]+$/;
			var	iLName = $(".iLName").val(),
				iLDescription = $(".iLDescription").val(),
				iLTableName = $(".iLTableName").val(),
				xrefILTableName = $(".xrefILTableName").val(),
				jobName = $(".jobName").val(),
				iLType = $("input[name='iLType']:checked").val() == 'D' ? 'D' : 'T',
				version = $(".version").val()
				uploadedJobFileNames = $(".uploadedJobFileNames").val();
				jobExecutionType = $("input[name='jobExecutionType']:checked").val(),
				jobTagId = $(".jobTags").val(),
				loadParamterId = $(".loadParameters").val();
			
				if(jobExecutionType == 'E'){
					
					if(jobTagId == 0){
						common.showcustommsg("div.jobTags","Please Choose Job tags");
						validStatus=false; 
					}	
			   	    
			   	    
			   	    if(loadParamterId == 0){
						common.showcustommsg("div.loadParamterId","Please Choose Load Paramter");
						validStatus=false; 
					}	
					
				}else{
					
					 if(jobName == ''){
				   	    	common.showcustommsg(".jobName", globalMessage['anvizent.package.label.pleaseEnterJobName'],".jobName");
				   	        validStatus=false;
				   	    }else if(!/^[a-zA-Z0-9_.]*$/.test(jobName.trim())){
				   	    	 common.showcustommsg(".jobName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],".jobName");
				   	    	 validStatus=false;
				   	    	
				   	    }
				   	    if($("select.iLContextParams option:selected").length == 0){
							common.showcustommsg("div.iLContextParams", globalMessage['anvizent.package.label.pleasechooseContextParameters']);
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
				   	    }
				   	    
					
				}
				
			
			$("#ilInfoForm .purgeScriptsDiv").find(".iLPurgeScript").each(function(){	   	       
		   			if($(this).val().trim() == ''){
		   				common.showcustommsg($(this), globalMessage['anvizent.package.label.pleaseEnterPurgeScript'],$(this));
		   				validStatus=false;
		   			}
	   		});
			 
			if(iLName == '' ){
	   	    	 common.showcustommsg(".iLName", globalMessage['anvizent.package.label.pleaseEnterILName'],".iLName");
	   	    	 validStatus=false;
	   	    } else if(!regex.test(iLName)){
	   	    	 common.showcustommsg(".iLName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],".iLName");
	   	    	 validStatus=false;
	   	    }
			if(version == ''){
	      		 common.showcustommsg(".version", globalMessage['anvizent.package.label.pleaseEnterVersion'], ".version");
	      		 validStatus=false;
     	  	}
     	  	else if(!version_regex.test(version)){
     	  		 common.showcustommsg(".version", globalMessage['anvizent.package.message.specialCharacters_and-and.AreOnlyAllowed'], ".version");
     	  		 validStatus = false;
     	  	}
		   	if(iLDescription == '' ){
		    	 common.showcustommsg(".iLDescription", globalMessage['anvizent.package.label.pleaseEnterILDescription'],".iLDescription");
		    	 validStatus=false;
		    }else if(!regex.test(iLDescription)){
	   	    	 common.showcustommsg(".iLDescription", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],".iLDescription");
	   	    	 validStatus=false;
	   	    }
	   	    if(iLTableName == '' ){
		    	 common.showcustommsg(".iLTableName", globalMessage['anvizent.package.label.pleaseEnterILTableName'],".iLTableName");
		    	 validStatus=false;
		    }else if(!/^[a-zA-Z0-9_]*$/.test(iLTableName.trim())){
	   	    	 common.showcustommsg(".iLTableName", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters']+" "+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInTableName'],".iLTableName");
	   	    	 validStatus=false;
	   	    }
	   	    if(!/^[a-zA-Z0-9_]*$/.test(xrefILTableName.trim()) && iLType == 'D'){
	   	    	common.showcustommsg(".xrefILTableName", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters']+" "+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInTableName'],".xrefILTableName");
	   	        validStatus=false;
	   	    }
	   	    
	   	    
	   	   
	   	    
	   	    if(validStatus){
	   	    	var selectors = [];
		   	    selectors.push(".iLName");
		   	    selectors.push(".iLTableName");
		   	    selectors.push(".iLDescription");
		   	    if(iLType == 'D')
		   	    	selectors.push(".xrefILTableName");
		   	    selectors.push(".jobName");
		   	    validStatus = iLInfo.validateMinMaxFieldLengths(selectors);
	   	    }
	   	    
	   	   return validStatus;
		},
		validateMinMaxFieldLengths : function(selectors){
			var sels = selectors;
			var valid = true;
			$.each(sels, function(i, o) {
				var sel = $(o),
				parent = sel.parent();				
				parent.removeClass('has-error');
				parent.find('span.help-block').remove();
				
				if ( sel.data("minlength") && sel.val().length < sel.data("minlength") ) {
					parent.addClass('has-error');
					$('<span class="help-block" style="font-size:12px;">'+"Minimum length of " + sel.data("minlength") + " is required"+'</span>').insertAfter(sel);
					if (valid)
						valid = false;
				} else if ( sel.data("maxlength") && sel.val().length > sel.data("maxlength") ) {
					parent.addClass('has-error');
					$('<span class="help-block" style="font-size:12px;">'+"Maximum length of " + sel.data("maxlength") + " is allowed"+'</span>').insertAfter(sel);
					if (valid)
						valid = false;
				}
			});
			return valid;
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
};

if($(".iLInfo-page").length || $(".clientConnectorMapping-page").length || $(".commonJob-page").length){
	iLInfo.initialPage();
	
	$(document).on("click", ".viewILDetails", function(){
		debugger
		var userId = $("#userId").val(),
			iLId = $(this).attr("data-ilid"),		
			selectedData = {
					iLId : iLId
			}
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true);
		var url_getILInfoById = "/app_Admin/user/"+userId+"/etlAdmin/getILInfoById";
		var myAjax = common.postAjaxCallObject(url_getILInfoById,'POST', selectedData,headers);
			myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    	  if(result != null){
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") {								 
				    			  common.showErrorAlert(result.messages[0].text)
			    				  return false;
		    			  }
		    			  if(result.messages[0].code == "SUCCESS") {
		    				  iLInfo.ViewILDetailsPanel(result.object);
		    				  $("#viewILDetailsPanel").modal("show");
		    			  }
		    		  }
		    	  }
			});
	});	
	
	$(document).on("click",".iLId", function(){
		debugger
		$("#iLId").val($(this).val());
		$("#ilInfoForm").prop("action", $("#editIL").val());
		this.form.submit();
	});
	
	$(document).on("click",".addPurgeScript, .deletePurgeScript, .addJobFile, .deleteJobFile",function(e){
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
		iLInfo.updateUploadedFileNames();
	});
	
	$(document).on("click","#updateILDetails",function(){
		var status = iLInfo.validateILUpdateForm();
		if(!status){
			return false;
		}
		
		$("#ilInfoForm").prop("action", $("#updateIL").val());
		this.form.submit();
		showAjaxLoader(true);
	});
	
	$(document).on("click","#addNewIL",function(){
		debugger
		var status = iLInfo.validateILCreationForm();
		if(!status){
			return false;
		}
		
		$("#ilInfoForm").prop("action", $("#createIL").val());
		this.form.submit();
		showAjaxLoader(true);
	});
	
	$(document).on("click","input[name='iLType']",function(){
		if($(this).val() == 'D'){
			$(".xrefILTableName").prop("disabled",false);
		}else{
			$(".xrefILTableName").val("").prop("disabled",true);
		}
	});
	
	$(document).on("change",".jobFile, .existedJarFile",function(){
		iLInfo.updateUploadedFileNames();
	});
	
/////////////////////////////////*********CLIENT CONNECTOR MAPPING*********///////////////////////////////////////
	$(document).on("change","#clientId",function(){
		showAjaxLoader(true);
		this.form.submit();
	});
	
	if ( $("#clientId").length > 0 ) {
		if ( $("#clientId").children().length > 1 &&  $("#clientId").val() == '0') {
			$("#clientId").val($("#clientId option").eq(1).val())
			$("#clientId").change();
		}
	}
	
	$(document).on("click","#save",function(){

		if($("#connectors option:selected").length == 0){
			common.showcustommsg("#connectors", globalMessage['anvizent.package.label.pleaseChooseAtLeastOneConnector'], "#connectors")
			return false;
		}
		
		$("#clientConnectorMappingForm").prop("action",$("#saveUrl").val()); 
		$("#clientConnectorMappingForm").submit();
	    showAjaxLoader(true);
	});
	
	$(document).on("click",".useOldJobFile",function(){
		iLInfo.updateUploadedFileNames();
	});
	
	
	
/////////////////////////////////*********COMMON JOBS*********///////////////////////////////////////
	$(document).on("click","#saveCommonJob",function(){
		var validate = true;
		common.clearValidations(["#jobType, input[name='jobFile'], .activeStatus"]);
		var jobType = $("#jobType option:selected").val(),
			jobFile = $("input[name='jobFile']").val(),
			activeStatus = $("input[name='isActive']").is(":checked") ? true : false;
		
		if(jobType == 0){
			common.showcustommsg("#jobType", "Please choose job type","#jobType");
			validate=false;
		}
		if(jobFile == ''){
			common.showcustommsg("input[name='jobFile']", globalMessage['anvizent.package.label.pleaseChooseFile'],"input[name='jobFile']");
			validate=false;
		}else{
			var fileExtension = $("input[name='jobFile']").val().replace(/^.*\./, '');
		    if(!(fileExtension == 'jar')) {
		    	common.showcustommsg("input[name='jobFile']", globalMessage['anvizent.package.label.pleaseChooseJarFile'],"input[name='jobFile']");
		    	validate=false;
		    }
		}
		if(!activeStatus){
			common.showcustommsg(".activeStatus", globalMessage['anvizent.package.label.PleaseChooseActiveStatus'],".activeStatus");
			validate=false;
		}
		
		if(!validate){
			return false;
		}
		$("#commonJobForm").prop("action",$("#saveUrl").val());
		showAjaxLoader(true);
		this.form.submit();
	});
	
	$(document).on("click",".edit",function(){
		$("#commonJobForm").prop("action",$("#editUrl").val());
		showAjaxLoader(true);
		this.form.submit();
		$("input[name='jobExecutionType']").change();
	});
	
	$(document).on("click","#updateCommonJob",function(){
		var validate = true;
		common.clearValidations([".activeStatus"]);
		var activeStatus = $("input[name='isActive']").is(":checked") ? true : false;

		if(!activeStatus){
			common.showcustommsg(".activeStatus", globalMessage['anvizent.package.label.PleaseChooseActiveStatus'],".activeStatus");
			validate=false;
		}
		
		if(!validate){
			return false;
		}
		$("#commonJobForm").prop("action",$("#updateUrl").val());
		showAjaxLoader(true);
		this.form.submit();
	});
	
	$("input[name='jobExecutionType']").on('change',function(){
		debugger
		var jobExecutionType = $("input[name='jobExecutionType']:checked").val();
		
		if(jobExecutionType == 'E'){
			$(".jobTagsDiv,.loadParametersDiv").removeClass('hidden').show();
			$(".contextParamsDiv,.jobNameDiv,.jobFileNameDiv,.jobFilesDiv,.existedJarFileDiv").hide();
		}else if(jobExecutionType == 'T'){
			$(".jobTagsDiv,.loadParametersDiv").hide();
			$(".contextParamsDiv,.jobNameDiv,.jobFileNameDiv,.jobFilesDiv,.existedJarFileDiv").removeClass('hidden').show();
		}
	});
	
	if($("input[name='jobExecutionType']").length){
		debugger
		var executionTypeEle = $("input[name='jobExecutionType']:checked").val();
		if(!executionTypeEle && executionTypeEle == ""){
			 $("input[name='jobExecutionType']").val('T');
		}
		console.log(executionTypeEle);
		$("input[name='jobExecutionType']").change();
	}
}
