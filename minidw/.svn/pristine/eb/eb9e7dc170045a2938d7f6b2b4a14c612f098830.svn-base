var currencyIntegration = {
		initialPage : function(){
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
			$("input[type='checkbox']").hide();
		},
		validation : function(){
			var apiUrl = $("#apiUrl").val();
			var accesKey= $("#accessKey").val();
			var source = $("#source").val();
			var jobName = $("#jobName").val();
			var clientSpecificJobName = $("#clientSpecificJobName").val();
			var UploadFileNames = $(".uploadedJobFileNames").val();
			var clientSpecificUploadFileNames = $(".ClientSpecificUploadedJobFileNames").val();
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
			
			
			var clientSpecificJobFiles = [];
			$(".ClientSpecificJobFilesDiv .clientSpecificFileContainer").find(".clientSpecificJobFile").each(function(){	   	
				var filePath = $(this).val();
				if(filePath != "")
				clientSpecificJobFiles.push(filePath.substring(filePath.lastIndexOf('\\')+1 , filePath.length));
			});
			
			
			
			$(".ClientSpecificJobFilesDiv .clientSpecificFileContainer").find(".clientSpecificJobFileNames").each(function(){
				if($(this).parents(".clientSpecificFileContainer").find(".useOldCLJobFile").is(":checked")){
					var fileName = $(this).val();
					if(fileName != ''){
						clientSpecificJobFiles.push(fileName);
					}
				}
			});
			
			
			
			var validStatus = true;
			if(apiUrl == ""){
				common.showcustommsg("#apiUrl","Enter api url", "#apiUrl");
				validStatus = false;
			}
			if(accesKey == ""){
				common.showcustommsg("#accessKey","Enter access key", "#accessKey");
				validStatus = false;
			}
			
			if(source == ""){
				common.showcustommsg("#source","Enter source", "#source");
				validStatus = false;
			}else if(!/^[a-zA-Z0-9_]*$/.test(source.trim())){
	   	    	 common.showcustommsg("#source", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters'],"#source");
	   	    	 validStatus=false;
	   	    }
			if(jobName == ""){
				common.showcustommsg("#jobName","Enter job name.", "#jobName");
				validStatus = false;
			} else if(!/^[a-zA-Z0-9_.]*$/.test(jobName.trim())){
	   	    	 common.showcustommsg("#jobName", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters'],"#jobName");
	   	    	 validStatus=false;
	   	    }
			if(clientSpecificJobName == ""){
				common.showcustommsg("#clientSpecificJobName","Enter client specific job name.", "#clientSpecificJobName");
				validStatus = false;
			} else if(!/^[a-zA-Z0-9_.]*$/.test(clientSpecificJobName.trim())){
	   	    	 common.showcustommsg("#clientSpecificJobName", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters'],"#clientSpecificJobName");
	   	    	 validStatus=false;
	   	    }
			if(UploadFileNames == ''){
	   	    	common.showcustommsg(".uploadedJobFileNames", globalMessage['anvizent.package.label.pleaseAddAtLeastOneJobFile'],".uploadedJobFileNames");
	   	    	validStatus = false;
	   	    }
			
			if(clientSpecificUploadFileNames == ''){
	   	    	common.showcustommsg(".ClientSpecificUploadedJobFileNames", globalMessage['anvizent.package.label.pleaseAddAtLeastOneJobFile'],".ClientSpecificUploadedJobFileNames");
	   	    	validStatus = false;
	   	    }
			$(".jobFilesDiv").find(".jobFile").each(function(){	   	       
	   	    		var fileupload = $(this).val();
	     		      if(fileupload == '') {
     		    	  common.showcustommsg( $(this), globalMessage['anvizent.package.label.pleaseChooseaFile']);
     		    	  validStatus = false;
	     		     } 
			});	
			
			$(".jobFilesDiv").find(".jobFile").each(function(){	   	       
		   	    	if($(this).val() != ''){
		   				  var fileExtension = $(this).val().replace(/^.*\./, '');
		     		      if(!(fileExtension == 'jar')) {
		     		    	common.showcustommsg($(this), globalMessage['anvizent.package.label.pleaseChooseJarFile'],$(this));
		     		    	validStatus = false;
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
			
			$(".ClientSpecificJobFilesDiv").find(".clientSpecificJobFile").each(function(){	   	       
   	    		var fileupload = $(this).val();
     		      if(fileupload == '') {
 		    	  common.showcustommsg( $(this), globalMessage['anvizent.package.label.pleaseChooseaFile']);
 		    	  validStatus = false;
     		     } 
			});	
		
			$(".ClientSpecificJobFilesDiv").find(".clientSpecificJobFile").each(function(){	   	       
		   	    	if($(this).val() != ''){
		   				  var fileExtension = $(this).val().replace(/^.*\./, '');
		     		      if(!(fileExtension == 'jar')) {
		     		    	common.showcustommsg($(this), globalMessage['anvizent.package.label.pleaseChooseJarFile'],$(this));
		     		    	validStatus = false;
		     		     } 
		   			}
	   		});	
			
			
			if(clientSpecificJobFiles != ""){
	   	    	for(var i = 0; i <= clientSpecificJobFiles.length; i++) {
		   	         for(var j = i; j <= clientSpecificJobFiles.length; j++) {
		   	             if(i != j && clientSpecificJobFiles[i] == clientSpecificJobFiles[j]) {
		   	            	common.showcustommsg(".ClientSpecificUploadedJobFileNames",globalMessage['anvizent.package.label.duplicateJobFiles'],".ClientSpecificUploadedJobFileNames");
		   	            	validStatus = false;
		   	                 break;
		   	             }
		   	         }
		   	     }
	   	    }
				
			return validStatus;
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
			
			$("#uploadedJobFileNames, .uploadedJobFileNames").val(fileNames);
		},
		
		clientspecificUploadFileNames : function(){
			var fileNames = "";
			
			$(".ClientSpecificJobFilesDiv .clientSpecificFileContainer").find(".clientSpecificJobFileNames").each(function(){
				var fileName = "";
				if($(this).parents(".clientSpecificFileContainer").find(".useOldCLJobFile").is(":checked")){
					fileName = $(this).val();
					if(fileNames != '' && fileName != ''){
						fileNames+=",";
					}
					fileNames+=fileName;
				}
			});
			console.log(fileNames);
			$(".ClientSpecificJobFilesDiv .clientSpecificFileContainer").find(".clientSpecificJobFile").each(function(){
				var filePath = $(this).val();
				var fileName = filePath.substring(filePath.lastIndexOf('\\')+1 , filePath.length);
				if(fileNames != '' && fileName != ''){
					fileNames+=",";
				}
				fileNames+=fileName;
			});
			
			console.log(fileNames);
			$("#clientSpecificJobfile_names").val(fileNames);
		},

		
}
if($('.currencyIntegration-page').length){
	currencyIntegration.initialPage();
	$(".edit,#immediateRun").show();
	
	$("#timeZone").val() && $("#timeZone").val() == 0 ? $("#timeZone").val(common.getTimezoneName()) :"";
	
	$("#saveCurrencyIntDetails").hide();
	var id = $("#id").val();
	if(id == 0){
		$("#saveCurrencyIntDetails").show();
		$(".edit").hide();
		$("#apiUrl,#accessKey,#source,#jobName,#clientSpecificJobName").removeAttr("readonly");
		$("#time_hours,#time_minutes,#timeZone").removeAttr("disabled");
	}
	
	$("#saveCurrencyIntDetails").on("click",function(){
		common.clearValidations(["#apiUrl,#accessKey,#source,#startPeriod,#endPeriod,.jobFile,.clientSpecificJobFile,.ClientSpecificUploadedJobFileNames"])
		var status = currencyIntegration.validation();
		if(!status){
			return false;
		}else{
			$("#currencyIntegration").prop("action",$("#save").val());
			this.form.submit();
			showAjaxLoader(true);	
		}
		
	})
	$(".edit").on("click",function(){
		$("#saveCurrencyIntDetails,.add,.fileNames,#cancel").show();
		$(".edit,#immediateRun").hide();
		$("#apiUrl,#accessKey,#source,#jobName,#clientSpecificJobName").removeAttr("readonly");
		$("#time_hours,#time_minutes,#timeZone").removeAttr("disabled");
		$("input[type='checkbox']").show();
	});
	
	$(document).on("click",".addJobFile",function(){
		var container = $(".jobFilesDiv");
		var jobFileContainer = $(".job-files-div .fileContainer").clone();
		$(jobFileContainer).find(".jobfile-label").text("");
		$(jobFileContainer).find(".deleteJobFile").css("display","inline-block");
		container.append($(jobFileContainer));
	});
	
	$(document).on("click",".clientSpecificAddJobFile",function(){
		var container = $(".ClientSpecificJobFilesDiv");
		var jobFileContainer = $(".job-files-div-CL .clientSpecificFileContainer").clone();
		$(jobFileContainer).find(".jobfile-label-CL").text("");
		$(jobFileContainer).find(".clientSpecificDeleteJobFile").css("display","inline-block");
		container.append($(jobFileContainer));
	});
	
	$(document).on("click",".deleteJobFile",function(){
		$(this).parents(".fileContainer").remove();
		currencyIntegration.updateUploadedFileNames();
	});
	$(document).on("click",".clientSpecificDeleteJobFile",function(){
		$(this).parents(".clientSpecificFileContainer").remove();
		currencyIntegration.clientspecificUploadFileNames();
	});
	
	$(document).on("change",".jobFile",function(){
		currencyIntegration.updateUploadedFileNames();
	});
	
	$(document).on("change",".clientSpecificJobFile",function(){
		currencyIntegration.clientspecificUploadFileNames();
	});
	
	$("#immediateRun").on("click",function(){
		$("#startDate,#endDate").val("");
		$(".runDetails").show();
		$(".currencyApiDetails").hide();
	})
	
	$(document).on("click",".useOldJobFile",function(){
		currencyIntegration.updateUploadedFileNames();
	});
	$(document).on("click",".useOldCLJobFile",function(){
		currencyIntegration.clientspecificUploadFileNames();
	});
	
	$(document).on("click","#cancel",function(){
		window.location.reload();
	});
	$('.datepicker').datepicker({
		onSelect : function(date) {
			var toDate =  $("#endDate"),
				minDate = $("#startDate").val();
			var minDateVar = new Date((minDate));
			minDateVar.setDate(minDateVar.getDate() + 1);
			toDate.datepicker('option','minDate',minDateVar);
		},
		dateFormat : 'yy-mm-dd',
		changeMonth : true,
		changeYear : true,
		yearRange : "0:+20",
		numberOfMonths : 1
	});
	
	$("#run").on("click",function(){
		common.clearValidations(["#startDate,#endDate"])
		var starDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var validStatus = true;
		if(starDate == ''){
			common.showcustommsg("#startDate",globalMessage['anvizent.package.label.pleaseChooseStartDate'],"#startDate");
    		  validStatus=false;
		}
		if(endDate == ''){
			common.showcustommsg("#endDate",globalMessage['anvizent.package.label.pleaseChooseEndDate'],"#endDate");
    		  validStatus=false;
		}
		if(!validStatus){
			return false;
		}else{
			$("#currencyIntegration").prop("action",$("#urlRun").val());
			this.form.submit();
			showAjaxLoader(true);
		}
	})
	
}