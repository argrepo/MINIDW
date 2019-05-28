var clientSpecificILDL = {
		initialPage : function(){
			if($("input[name='useDefault']").is(":checked")){
				$("#clientSpecificJobName, .jobFile, .addJobFile, .deleteJobFile, #save, .useCurrentJobFile, #clientSpecificJobVersion").prop("disabled",true);
			}
			
			if($("input[name='useDefault']").is(":checked") && $("#currentClientJobName").val() != '' && $("#currentClientJarFileNames").val() != ''){
				$("#save").prop("disabled",false);
			}
			
			$("#clientId, #iLId, #dLId").select2({               
                allowClear: true,
                theme: "classic"
			});
			$(".existedJarFile").multipleSelect({
				filter : true,
				placeholder : 'Select jar files',
			    enableCaseInsensitiveFiltering: true
			});
			setTimeout(function() { $("#pageErrors").hide(); }, 5000);
		},
		updateUploadedFileNames : function(){
			var fileNames = "";
			
			$(".currentJobFilesDiv").find(".currentJobFileName").each(function(){
				var fileName = "";
				if($(this).parents(".currentJobFilesDiv").find(".useCurrentJobFile").is(":checked")){
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
			$("#jobFileNames").val(fileNames);
		},
		validateForm : function(){
			common.clearValidations(["#clientSpecificJobName, .jobFile, #jobFileNames, #clientSpecificJobVersion"]);
			var validStatus = true;
			var	clientSpecificJobName = $("#clientSpecificJobName").val();
			var defaultJobName = $("#defaultJobName").val();
			var defaultJarFileNames = $("#defaultJobJarFileNames").val().split(","),
				clientSpecificJarFileNames = $("#jobFileNames").val().split(",");
			var version = $("#clientSpecificJobVersion").val();
			var jobFiles = [];
			var jobFileName = $("#jobFileNames").val();
			
			$(".jobFilesDiv").find(".jobFile").each(function(){	   	
				var filePath = $(this).val(); 
				if(filePath != "")
			    jobFiles.push(filePath.substring(filePath.lastIndexOf('\\')+1 , filePath.length));
			});
			
			$(".currentJobFilesDiv").find(".currentJobFileName").each(function(){
				if($(this).parents(".currentJobFilesDiv").find(".useCurrentJobFile").is(":checked")){
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
			
			if(!$("input[name='useDefault']").is(":checked")){
				var regexMsg = globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsdotsandalphabets'];
			    var selectors = [];
			    selectors.push({"selector":"#clientSpecificJobName","message":globalMessage['anvizent.package.label.pleaseEnterJobName'], 
			    				"regex":/^[0-9a-zA-Z/ /_/./-]+$/, "regexMsg":regexMsg});
			    selectors.push({"selector":"#clientSpecificJobVersion","message":globalMessage['anvizent.package.label.pleaseEnterJobVersion'],
			    				"regex":/^[a-zA-Z0-9/_/-/./(/)]*$/,"regexMsg":regexMsg});
			    selectors.push({"selector":"#jobFileNames","message":globalMessage['anvizent.package.label.pleaseAddAtLeastOneJobFile'],"regex":"","regexMsg":""});
				var valid = common.validateWithCustomMessages(selectors);
				if(!valid){
						validStatus = false;
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
				
				if(defaultJobName.toLowerCase() == clientSpecificJobName.toLowerCase()){
					 common.showcustommsg("#clientSpecificJobName",globalMessage['anvizent.package.label.sameAsDefaultJobPleaseChangeTheJob'],"#clientSpecificJobName");
			    	 validStatus=false;
				}
				
			  	if(defaultJarFileNames != ''){
			  		for(var i=0;i<defaultJarFileNames.length;i++)
				     {
				        if($.inArray(defaultJarFileNames[i].trim(),clientSpecificJarFileNames) != -1){
				        	common.showcustommsg("#jobFileNames",globalMessage['anvizent.package.label.sameAsDefaultJobJarFilesChangeTheJarFiles'],"#jobFileNames");
					    	validStatus=false;
					    	break;
				        }
				     }
			  	}
			  	
			  	if(jobFiles != ""){
		   	    	for(var i = 0; i <= jobFiles.length; i++) {
			   	         for(var j = i; j <= jobFiles.length; j++) {
			   	             if(i != j && jobFiles[i] == jobFiles[j]) {
			   	            	common.showcustommsg("#jobFileNames",globalMessage['anvizent.package.label.duplicateJobFiles'],"#jobFileNames");
			   	            	validStatus = false;
			   	                 break;
			   	             }
			   	         }
			   	     }
		   	    }
			}
			return validStatus;
		}
		
};

if($(".clientSpecificIL-page").length || $(".clientSpecificDL-page").length){
	clientSpecificILDL.initialPage();
	
	$("#clientId").on("change",function(){
		$("#iLId").val(0);
		$("#dLId").val(0);
		showAjaxLoader(true);
		this.form.submit();
	});
	
	if ( $("#clientId").length > 0 ) {
		if ( $("#clientId").children().length > 1 &&  $("#clientId").val() == '0') {
			$("#clientId").val($("#clientId option").eq(1).val())
			$("#clientId").change();
		}
	}
	
	$("#iLId").on("change",function(){
		showAjaxLoader(true);
		this.form.submit();
	});	
	
	$("#dLId").on("change",function(){
		showAjaxLoader(true);
		this.form.submit();
	});
	
	$(document).on("click",".addJobFile",function(e){
		e.preventDefault();
		var container = $(".jobFilesDiv");
		var jobFileContainer = $(".job-files-div .fileContainer").clone();
		$(jobFileContainer).find(".jobfile-label").text("");
		$(jobFileContainer).find(".deleteJobFile").css("display","inline-block");
		container.append($(jobFileContainer));
	});
	
	$(document).on("click",".deleteJobFile",function(e){
		e.preventDefault();
		$(this).parents(".fileContainer").remove();
		clientSpecificILDL.updateUploadedFileNames();
	});
	
	$(document).on("click","input[name='useDefault']",function(){
		common.clearValidations(["#clientSpecificJobName,.jobFile, #jobFileNames, #clientSpecificJobVersion"]);
		if($(this).is(":checked")){
			var container = $(".jobFilesDiv");
			var jobFileContainer = $(".job-files-div .fileContainer").clone();
			container.empty().append($(jobFileContainer));
			$("#clientSpecificJobName, #jobFileNames, #clientSpecificJobVersion").val("");
			$(".useCurrentJobFile").prop("checked",false);
			$("#clientSpecificJobName, .jobFile, .addJobFile, .deleteJobFile, .useCurrentJobFile, #clientSpecificJobVersion").prop("disabled",true);
		}
		else{
			$("#clientSpecificJobName, .jobFile, .addJobFile, .deleteJobFile, #save, .useCurrentJobFile, #clientSpecificJobVersion").prop("disabled",false);
		}
		
		if($(this).is(":checked") && $("#currentClientJobName").val() == '' && $("#currentClientJarFileNames").val() == ''){
			$("#save").prop("disabled",true);
		}else if($(this).is(":checked") && typeof $("#currentClientJobName").val() == "undefined" && typeof $("#currentClientJarFileNames").val() == "undefined"){
			$("#save").prop("disabled",true);
		}
	});
	
	$(document).on("change",".jobFile,.existedJarFile",function(){
		clientSpecificILDL.updateUploadedFileNames();
	});	
	
	$(document).on("click","#save",function(){
		var validStatus = clientSpecificILDL.validateForm();
	    if(!validStatus){
	    	return false;
	    }
	    $("#clientSpecificJobJarFileNames").val($("#jobFileNames").val());
		$("#clientSpecificILDLForm").prop("action",$("#saveUrl").val()); 
		$("#clientSpecificILDLForm").submit();
	    showAjaxLoader(true);
	});
	
	$(document).on("click",".useCurrentJobFile",function(){
		clientSpecificILDL.updateUploadedFileNames();
	});
	
}