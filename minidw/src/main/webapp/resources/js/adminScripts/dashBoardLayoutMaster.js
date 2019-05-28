var  dashBoardLayoutMaster = {
		initialPage : function() {
			$("#verticalName").select2({               
                allowClear: true,
                theme: "classic"
			}); 
			$(".contextParamsDiv,.jobNameDiv,.jobFileNameDiv,.jobFilesDiv,.existedJarFileDiv").removeClass('hidden').show();
			$(".existedJarFile").multipleSelect({
				filter : true,
				placeholder : 'Select jar files',
			    enableCaseInsensitiveFiltering: true
			});
			
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
			debugger
			$(".jobExecution_type").click();
		},
		dlCreationFormValidation : function(){
			
	       	var dlName=$("#dlName").val();
	      	var dlJobName=$("#jobName").val();
	       	var dltableName = $("#dlTableName").val();
	       	var dldescription = $("#dlDescription").val();
	       	var allIlList = $("#ilList").multipleSelect("getSelects", "text");
	       	var allKpiList = $("#kpiList").multipleSelect("getSelects", "text");
	       	var contextParamList = $("#contextParamList").multipleSelect("getSelects", "text");
	       	var verticalName = $("#verticalName option:selected").val();
	       	var version = $("#version").val();
	       	var version_regex = /^[0-9a-zA-Z/ /_/./-]+$/,
	       		uploadedJobFileNames = $(".uploadedJobFileNames").val();
	      	common.clearValidations(["#version, #dlName", "#jobName","#inputFile","#dlTableName","#dlDescription","isActive","#ilList","#kpiList","#contextParamList","#verticalName, .uploadedJobFileNames"]);
	      	var validStatus=true;
	      	if(dlName == '' ){
	      	   	 common.showcustommsg("#dlName", globalMessage['anvizent.package.label.pleaseEnterDLName'],"#dlName");
	      	   	 validStatus=false;
	      	}
	      	if(verticalName == '' || verticalName == 0  ){
	      	   	 common.showcustommsg("#verticalName",globalMessage['anvizent.package.label.pleasechooseverticalname'],"#verticalName");
	      	   	 validStatus=false;
	      	}
      	    if(dltableName == '' ){
	 	    	 common.showcustommsg("#dlTableName", globalMessage['anvizent.package.label.pleaseEnterTableName'],"#dlTableName");
	 	    	 validStatus=false;
      	    }else if(!/^[a-zA-Z0-9_]*$/.test(dltableName.trim())){
	   	    	 common.showcustommsg("#dlTableName", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters']+" "+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInTableName'],"#dlTableName");
	   	    	 validStatus=false;
	   	    }
      	    if(dldescription == '' ){
	 	    	 common.showcustommsg("#dlDescription", globalMessage['anvizent.package.label.pleaseEnterDLDescription'],"#dlDescription");
	 	    	 validStatus=false;
      	    } 
      	    if(dlJobName == ''){
      	    	common.showcustommsg("#jobName", globalMessage['anvizent.package.label.pleaseEnterJobName'],"#jobName");
      	        validStatus=false;
      	    }else if(!/^[a-zA-Z0-9_.]*$/.test(dlJobName.trim())){
	   	    	 common.showcustommsg("#jobName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#jobName");
	   	    	 validStatus=false;
	   	    }
      	    if(allIlList.length == 0){ 
      	    	common.showcustommsg("#ilList",globalMessage['anvizent.package.label.pleasechooseils'],"#ilList");
      		  	validStatus=false;
      	    }
      	    if(allKpiList.length == 0){ 
      	    	common.showcustommsg("#kpiList",globalMessage['anvizent.package.label.pleasechoosekpis'],"#kpiList");
      		  	validStatus=false;
      	    }
      	    if(contextParamList.length == 0){ 
      	    	common.showcustommsg("#contextParamList",globalMessage['anvizent.package.label.pleasechooseparameters'],"#contextParamList");
      	    	validStatus=false;
      	    }
      	    if(version == ''){
	      		 common.showcustommsg("#version", globalMessage['anvizent.package.label.pleaseEnterVersion'], "#version");
	      		 validStatus=false;
      	    }
      	    else if(!version_regex.test(version)){
      	    	common.showcustommsg("#version", globalMessage['anvizent.package.message.specialCharacters_and-and.AreOnlyAllowed'], "#version");
      	    	validStatus = false;
      	    }
      	    if(uploadedJobFileNames == '' ){
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
	        return validStatus;	
		},
		dlUpdationFormValidation : function(){
		   	var dlName=$("#dlName").val();
		  	var dlJobName=$("#jobName").val();
		   	var dltableName = $("#dlTableName").val();
		   	var dldescription = $("#dlDescription").val();
		   	var allIlList = $("#ilList").multipleSelect("getSelects", "text");
		   	var allKpiList = $("#kpiList").multipleSelect("getSelects", "text");
		 	var contextParamList = $("#contextParamList").multipleSelect("getSelects", "text");
		  	var verticalName = $("#verticalName option:selected").val();
		    var uploadedJobFileNames = $(".uploadedJobFileNames").val();
			var version = $("#version").val();
		    var regex = /^[0-9a-zA-Z\_ ]+$/;
		    var version_regex = /^[0-9a-zA-Z/ /_/./-]+$/;
            common.clearValidations(["#version, #dlName", "#jobName","#inputFile","#dlTableName","#dlDescription","isActive","#ilList","#kpiList","#contextParamList","#verticalName",".uploadedJobFileNames","#jobTagId","#loadParameterId"]);
            var validStatus=true;
            
            
            var jobExecutionType = $("input[name='jobExecutionType']:checked").val();
			var jobTagId = $(".jobTags").val();
			var loadParamterId = $(".loadParameters").val();
			
			

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
				if(contextParamList.length == 0){ 
	          		common.showcustommsg("#contextParamList",globalMessage['anvizent.package.label.pleasechooseparameters'],"#contextParamList");
	          		validStatus=false;
	      	    }
				
				var jobFiles = [];
				$(".jobFilesDiv .fileContainer").find(".jobFile").each(function(){	   	
					var filePath = $(this).val();
					if(filePath != "")
				    jobFiles.push(filePath.substring(filePath.lastIndexOf('\\')+1 , filePath.length));
				});
			
				$(".jobFilesDiv .fileContainer").find("input[name='jobFileNames']").each(function(){
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
			    
			    if(dlJobName == ''){
	      	    	common.showcustommsg("#jobName", globalMessage['anvizent.package.label.pleaseEnterJobName'],"#jobName");
	      	        validStatus=false;
	      	    }else if(!/^[a-zA-Z0-9_.]*$/.test(dlJobName.trim())){
		   	    	 common.showcustommsg("#jobName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#jobName");
		   	    	 validStatus=false;
		   	    }
			    
			    if(uploadedJobFileNames == '' ){
	      	    	common.showcustommsg(".uploadedJobFileNames", globalMessage['anvizent.package.label.pleaseAddAtLeastOneJobFile'],".uploadedJobFileNames");
	 				validStatus=false;
	   	        }
			}
            
      	    if(dlName == ''){
      	    	 common.showcustommsg("#dlName", globalMessage['anvizent.package.label.pleaseEnterDLName'],"#dlName");
      	    	 validStatus=false;
      	    } else if(!regex.test(dlName)){
      	    	 common.showcustommsg("#dlName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#dlName");
      	    	 validStatus=false;
      	    }
      	    if(verticalName == '' || verticalName == 0 ){
      	    	 common.showcustommsg("#verticalName", globalMessage['anvizent.package.label.pleasechooseverticalname'],"#verticalName");
      	    	 validStatus=false;
      	    }
      	    if(dltableName == '' ){
	 	    	 common.showcustommsg("#dlTableName", globalMessage['anvizent.package.label.pleaseEnterTableName'],"#dlTableName");
	 	    	 validStatus=false;
      	    }else if(!/^[a-zA-Z0-9_]*$/.test(dltableName.trim())){
	   	    	 common.showcustommsg("#dlTableName", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters']+" "+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInTableName'],"#dlTableName");
	   	    	 validStatus=false;
	   	    }
      	    if(dldescription == ''){
	 	    	 common.showcustommsg("#dlDescription", globalMessage['anvizent.package.label.pleaseEnterDLDescription'],"#dlDescription");
	 	    	 validStatus=false;
      	    }  else if(!regex.test(dldescription)){
	 	    	 common.showcustommsg("#dlDescription", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#dlDescription");
	 	    	 validStatus=false;
      	    } 
      	   
      	    
      	    if(allIlList.length == 0){ 
      	    	common.showcustommsg("#ilList",globalMessage['anvizent.package.label.pleasechooseils'],"#ilList");
    	    	validStatus=false;
      	    }
      	    if(allKpiList.length == 0){ 
      	    	common.showcustommsg("#kpiList",globalMessage['anvizent.package.label.pleasechoosekpis'],"#kpiList");
    	    	validStatus=false;
      	    }
          	
          	if(version == ''){
	      		 common.showcustommsg("#version", globalMessage['anvizent.package.label.pleaseEnterVersion'], "#version");
	      		 validStatus=false;
    	  	}
    	  	else if(!version_regex.test(version)){
    	  		 common.showcustommsg("#version", globalMessage['anvizent.package.message.specialCharacters_and-and.AreOnlyAllowed'], "#version");
    	  		 validStatus = false;
    	  	}
      	    return validStatus;	
          },
          updateUploadedFileNames : function(){
				var fileNames = "";
				
				$(".jobFilesDiv .fileContainer").find("input[name='jobFileNames']").each(function(){
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

if($('.dashBoardLayoutMaster-page').length){
	debugger
	dashBoardLayoutMaster.initialPage();
	if ($("#ilList").length) {
		$("#ilList").multipleSelect({ filter : true, placeholder : "Select", });
	}
	if ($("#kpiList").length) {
		$("#kpiList").multipleSelect({ filter : true, placeholder : "Select", });
	}
	if ($("#contextParamList").length) {
		$("#contextParamList").multipleSelect( { filter : true, placeholder : "Select", });
	}
	
	$("#verticalList").removeAttr("multiple");
	var increment = 1;
	$("#tdlMasterTable").DataTable({
		"order": [[ 0, "asc" ]],"language": {
            "url": selectedLocalePath
        }
	});
	$(function() { $('#allIlList,#allKpiList').multipleSelect( { filter : true, placeholder : "Select", }); });
	 
	 
	$(document).on('click', '#resetDlMaster', function(){
		  
		  common.clearValidations(["#dlName","#version","#jobName","#inputFile","#dlTableName","#dlDescription","isActive","#ilList","#kpiList","#contextParamList","#verticalName",".uploadedJobFileNames"]);
		     $('#dl-fileContainer').empty();
		     $('.addFilePath').empty();
		     $('#ilList,#kpiList,#contextParamList').multipleSelect("uncheckAll");
		  
		     $("#verticalName").val("0");
		     $("#dlName").val("");
		     $("#version").val("");
	         $("#jobName").val("");
	         $(".filePath0").val("");
	       	 $("#dlTableName").val("");
	       	 $("#dlDescription").val("");
	       
		     $(".addFilePath").find("input[type='file']").each(function(){				 
   	         var id = $(this).attr("id");
   	         $("#"+id).val('');
   	         });
		     $("#active1").attr("checked",false);
		     $(".uploadedJobFileNames").val("");
		     $(".jobFilesDiv").empty();
		     $(".jobFilesDiv").append('<div class="row form-group fileContainer">'+
		    		 '<div class="row form-group fileContainer">'+
				     '<label class="control-label col-sm-3 jobfile-label">Job File :</label>'+
	    		'<div class="col-sm-8">'+			    			
			    	'<input type="file" class="jobFile" name="jobFile" data-buttonText="Find file">'+
			    '</div>'+
			    '<div class="col-sm-1">'+
			     '<a href="#" class="btn btn-primary btn-sm addJobFile">'+	
			     '<span class="glyphicon glyphicon-plus"></span>'+
			    	'</a>'+
			    '</div>'+
			  '</div>');
	});
	 $("#addDlMaster").on('click', function() {
		 var exectionType = $("input[name='jobExecutionType']:checked").val();
	       var status= dashBoardLayoutMaster.dlUpdationFormValidation();
		   if(!status){ return false;}
		    
	       var selectors = [];
		
	       selectors.push('#dlName');
	       selectors.push('#dlDescription');
	       selectors.push('#dlTableName');
	       if(exectionType == 'T')
	       selectors.push('#jobName');
		    
	        var valid = common.validate(selectors);
		
		    if(!valid){ return false;}
		
			var userID=$('#userID').val();
			$("#dashboardLayoutForm").prop("action",$("#addUrl").val()); 
			this.form.submit();
		    showAjaxLoader(true);
			 
	 });
	  
	 $("#updateDlMaster").on('click', function() {
		 var exectionType = $("input[name='jobExecutionType']:checked").val();
			   var status= dashBoardLayoutMaster.dlUpdationFormValidation();
			   if(!status){ return false;}
			   var selectors = [];
			
		       selectors.push('#dlName');
		       selectors.push('#dlDescription');
		       selectors.push('#dlTableName');
		       if(exectionType == 'T')
		       selectors.push('#jobName');
			    
		        var valid = common.validate(selectors);
			
			    if(!valid){ return false;}
				var userID=$('#userID').val();
				$("#dashboardLayoutForm").prop("action",$("#updateUrl").val()); 
				this.form.submit();
				showAjaxLoader(true);
	 });
	 $(document).on("click",".useOldJobFile",function(){
		 dashBoardLayoutMaster.updateUploadedFileNames();
		});
	
	 $(document).on("click",".addJobFile, .deleteJobFile",function(e){
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
				dashBoardLayoutMaster.updateUploadedFileNames();
		});
		
		$(document).on("change",".jobFile, .existedJarFile",function(){
			dashBoardLayoutMaster.updateUploadedFileNames();
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
