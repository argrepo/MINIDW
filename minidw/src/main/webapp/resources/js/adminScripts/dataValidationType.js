var userId = $("#userID").val();

var dataValidationType = {
	initialPage : function() {

		
		$("#dataValidationTypeTable").DataTable({
			"order": [[0, "desc" ]],"language": {
	            "url": selectedLocalePath
	        }
		});
		$(".existedJarFile").multipleSelect({
			filter : true,
			placeholder : 'Select jar files',
		    enableCaseInsensitiveFiltering: true
		});
		$(".dataValidationTypeContextParams").multipleSelect({
			filter : true,
			placeholder : 'Select Context Parameters',
		    enableCaseInsensitiveFiltering: true
		});
	},

	updateUploadedFileNames : function(){
		var fileNames = "";
		var i = 0;
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
	
	
	contextParams : function(contextParams) {

		var _selector = $("[name='contextParameters']");
		_selector.empty();
		common.populateComboBox(_selector, "0", "Select ContextParams");
		$.each(contextParams, function(key, value) {
			_selector.append($("<option>", {
				text : value,
				value : index
				
			}));
		});
		_selector.select2();
	},
	
	
	dataValidation : function(dataValidation) {
		
		$(".dataValidationSelectDiv").empty();
		  var select$ = $("<select/>",{class:"dataValidation_Select",name:"dataValidation_Select",id:"dataValidation_Select"}).append($("<option/>",{value:'0',text:"Select DataValidation"}));
		  $.each(dataValidation,function(key,value){
			  $("<option/>",{value:key,text:value}).appendTo(select$);
		  });
		  select$.appendTo(".dataValidationSelectDiv");
		  $(".dataValidation_Select").select2({               
                allowClear: true,
                theme: "classic"
			});
	},
	  cleanupdataValidationType : function() {
    	  common.clearValidations(["#validationTypeDesc","#validationTypeName",".dataValidation_Select","#active","#existedJarjobFileNames","#jobName","#uploadedDJobFileNames",".jobFile"]);
		  $("#validationTypeDesc").val("");
		  $("#validationTypeName").val("");
		  $(".dataValidation_Select").val("");
		  $("#active").prop("checked",false);
		  $("#jobName").val("");
		  $("#uploadedDJobFileNames").val("");
		  $("#jobFileNames").val("");
		  $(".jobFileName").val("");
		  $('input[name=useOldJobFile]').attr("checked", false);
		  $(".jobFile").val("");
		  $("#existedJarjobFileNames").val([]);
		  $("#existedJarjobFileNames").multipleSelect('refresh');
		  $("#contextParameters").val([]);
		  $("#contextParameters").multipleSelect('refresh');
		  $("#validationTypeID").val("");
		  $(".connectionTitle").text(globalMessage['anvizent.package.button.createNewConnection']);
	},
	
		selectFieldsData : function(validationTypeId) {
			
			var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
	 		showAjaxLoader(true);
	 		var url = "/app_Admin/user/" + userId
			+ "+/etlAdmin/getDatavalidation";
			var myAjax = common.loadAjaxCall(url,'GET','',headers);
			  myAjax.done(function(result){
				  showAjaxLoader(false);
					if(result != null && result.hasMessages){
						if(result.messages[0].code == 'SUCCESS'){
							var dataValidation =result["object"];
							dataValidationType.cleanupdataValidationType();
							 dataValidationType.dataValidation(dataValidation);
							showAjaxLoader(true);
							var url = "/app_Admin/user/" + userId
									+ "+/etlAdmin/getAvailableJarsList";

							var myAjax = common.loadAjaxCall(url, 'GET', '', headers);

							myAjax.done(function(result) {
								showAjaxLoader(false);

								if (result != null && result.hasMessages) {
									if (result.messages[0].code == 'SUCCESS') {
										success = true;
										var jarFilesList = result["object"]["jarsList"];
										var select$ = $("#existedJarjobFileNames").empty();

										$.each(jarFilesList, function(key, value) {
											$("<option/>", {
												value : value.fileName,
												text : value.fileName
											}).appendTo(select$);
										});

										var contextParamMap = result["object"]["contextParamMap"];
										var contextParam$ = $("#contextParameters").empty();
										$.each(contextParamMap, function(key, value) {
											$("<option/>", {
												value : value.paramId,
												text : value.paramName
											}).appendTo(contextParam$);
										});

										$("#existedJarjobFileNames").multipleSelect('refresh');
										$("#contextParameters").multipleSelect('refresh');
										 /*$("#uploadedJobFileNames").attr("disabled", "disabled");*/
										if (validationTypeId > 0) {
											$("#updateDataTypeValidation").show();
											$("#saveDataTypeValidation").hide();
											dataValidationType
													.getDataValidationById(validationTypeId);
										} else {
											$("#updateDataTypeValidation").hide();
											$("#saveDataTypeValidation").show();
											$(".jobFilesDiv").show();
											$("#dataValidationTypeDiv").modal("show");
										}
									}
								}
							});
							
						}
					}
			  });
	},
	
	validateFormTypeUpload : function(){
		
		common.clearValidations([ "#validationTypeName",
		  						"#validationTypeDesc", ".dataValidation_Select",".dataValidationTypeContextParams",".jobName",".uploadedJobFileNames",]);
		
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
			
			var validationTypeName = $("#validationTypeName").val();
			
			if(validationTypeName == ''){
	   	    	common.showcustommsg("#validationTypeName", globalMessage['anvizent.package.label.pleaseEnterValidationName'],"#validationTypeName");
	   	        validStatus=false;
	   	    }/*else if(!/^[a-zA-Z0-9_.]*$/.test(validationTypeName.trim())){
	   	    	 common.showcustommsg("#validationTypeName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#validationTypeName");
	   	    	 validStatus=false;
	   	    }*/
			
			var validationTypeDesc = $("#validationTypeDesc").val();
			if(validationTypeDesc == ''){
				 common.showcustommsg("#validationTypeDesc", globalMessage['anvizent.package.label.pleaseEnterValidationDesc'], "#validationTypeDesc");
				 validStatus = false;
			  }/*else if(!/^[a-zA-Z0-9_.]*$/.test(validationTypeDesc.trim())){
	   	    	 common.showcustommsg("#validationTypeDesc", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#validationTypeDesc");
	   	    	 validStatus=false;
	   	    }*/
			
			var jobName = $("#jobName").val();
			if(jobName == ''){
				common.showcustommsg("#jobName", globalMessage['anvizent.package.label.pleaseEnterJobName'], "#jobName");
				validStatus = false;
			}else if(!/^[a-zA-Z0-9_.]*$/.test(jobName.trim())){
				common.showcustommsg("#jobName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#jobName");
				validStatus=false;
			}
			
			var dataValidation_Select = $(".dataValidation_Select option:selected").val();
			
			if(dataValidation_Select == '0'){
				common.showcustommsg(".dataValidation_Select", "Please select Validation", ".");
				validStatus= false;
			}
			return validStatus;
	},
	getDataValidationTypeList : function() {

		var dataValidationTypeObj = this;
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true);
		var url = "/app_Admin/user/" + userId
				+ "+/etlAdmin/getAllDataValidationTypes";
		var myAjax = common.loadAjaxCall(url, 'GET', '', headers);
		myAjax.done(function(result) {
			showAjaxLoader(false);
			if (result != null) {
				dataValidationTypeObj.populateDataValidationTypeList(result.object);
			}
			});
	},
	populateDataValidationTypeList : function(data) {
		var dataValidationListTbl = $("#dataValidationTypeTable tbody");
		var connectionClone = $("#dataValidationTypeTable tfoot tr");
		dataValidationListTbl.empty();
		$.each(data,
				function(index, obj) {
					var newTr$ = connectionClone.clone().removeClass("hidden")
					newTr$.find(".dataValidationTypeId").text(
							obj.validationTypeId);
					newTr$.find(".dataValidationTypeName").text(
							obj.validationTypeName);
					newTr$.find(".dataValidationTypeDesc").text(
							obj.validationTypeDesc);
					 newTr$.find(".dataValidation").text(obj.validationName);
					newTr$.find(".active").text(obj.active);
					newTr$.find("#validationTypeEdit").attr("data-dataValidationTypeId", obj.validationTypeId);
					//newTr$.find(".deleteConnection").attr("data-dataValidationTypeId", obj.validationTypeId);
					dataValidationListTbl.append(newTr$);
				});
	},
	
	getDataValidationById : function(dataValidationTypeId) {
			var dataValidationTypeObj = this;
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			showAjaxLoader(true);
			var url = "/app_Admin/user/" + userId
			+ "+/etlAdmin/getDataValidationTypeById/" + dataValidationTypeId;
			var myAjax = common.loadAjaxCall(url, 'GET', '', headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					dataValidationTypeObj.populateDataValidationTypeById(result.object);
				}
			});
		},
		
		populateDataValidationTypeById : function (data) {
			$("#dataValidationTypeDiv").modal("show");
		    $(".editJobFileTypeDiv").removeClass('hidden').show();
		    $(".jobFileData").removeClass('hidden').show();
		    $(".fileTypeDiv").hide();
			$("#validationTypeID").val(data.validationTypeId);
			$(".connectionTitle").text(globalMessage['anvizent.package.button.editConnection']);
			$("#saveDataTypeValidation,#editDataTypeValidation").hide();
			$("#updateDataTypeValidation").show();
	        $("#validationTypeName").val(data.validationTypeName);
	        $("#validationTypeDesc").val(data.validationTypeDesc);
	        $("#jobName").val(data.jobName);
	        $("#dataValidation_Select").val(data.validationId).select2().trigger('change');
	        $("#contextParameters").val(data.contextParamList);
	        $("#contextParameters").multipleSelect('refresh');
	        $("#dataValidation_Select").val(data.validationId);
	        var optionValues = [];
	        $('#existedJarjobFileNames option').each(function() {
	            optionValues.push($(this).val());
	        });
	        var selectedJobFileNames = [];
	        	var dependencyJarsList = data.dependencyJars.split(",");
	        	for(var j=0;j< dependencyJarsList.length;j++){
	        		for(var k=0;k<optionValues.length;k++){
	        		if(optionValues[k]==dependencyJarsList[j]){
	        			selectedJobFileNames.push(dependencyJarsList[j]);
	        		}
	        	}
	        }
	        
	        	var a = [];
	        	$.each(dependencyJarsList,function(i,val){
	        	var result=$.inArray(val,selectedJobFileNames);
	        	if(result==-1)
	        	a.push(dependencyJarsList[i])
	        	})
	        	
	        	var container = $('.fileContainer');
	        	var textfile = container.find('.jobFileData');
	        	 $('.fileContainer').find('.editJobFileTypeDiv').empty();
	        	 $('.fileContainer').find('.addjobFile').empty();
	        	if(a.length >= 1){
	        		for(var i=0;i<a.length;i++){
	        			var inputDiv = "<div class='col-sm-4'></div><div class='col-sm-6 editJobFileTypeDiv' >"+
						"<label class='checkbox' style='margin-left: 20px;'> <input type='checkbox' name='useOldJobFile' class='useOldJobFile' id='useOldJobFile"+[i]+"'></label>"+
						"<h5 class='jobFileName' style='margin-left: 20px;'>"+
						"<input type='hidden' id ='jobFileNames' class='jobFileNames' value ='"+a[i]+"'></input> "+a[i]+" "+
						"</h5> </div>"+
						"<div class='col-sm-2 addjobFile'>" +
						"<a href='#' class='btn btn-primary btn-sm addJobFile'><span class='glyphicon glyphicon-plus'></span></a>" +
						"</div>";
	        		   $(textfile).append(inputDiv);
	        		}
	        	}
	        	$('input[name=useOldJobFile]').attr("checked", true);
	        	$('.jobFileData').refresh
	        $("#existedJarjobFileNames").val(selectedJobFileNames);
	        $("#existedJarjobFileNames").multipleSelect('refresh');
	        $("#uploadedJobFileNames").val(data.dependencyJars);
	        $(".jobFileName").val(a);
	       if(data.active == true){
	    	   $('#active').attr("checked",true);
	       }
		},
}

if ($('.dataValidationType-page').length) {
	dataValidationType.initialPage();
	dataValidationType.getDataValidationTypeList();

	$(document).on("click", "#addDataValidationType", function() {
		var container = $('.fileContainer');
		var jobFileDiv = container.find('.fileTypeDiv');
		jobFileDiv.empty();
		var jobFileInput = $('<input />', { type: 'file', class: 'col-sm-6 jobFile', name:'jobFile' }).appendTo(jobFileDiv);
		
		var jobFilehref = "<div class='col-sm-2 addjobFile'>" +
		"<a href='#' class='btn btn-primary btn-sm addJobFile'><span class='glyphicon glyphicon-plus'></span></a>" +
		"</div>";
		jobFileDiv.append(jobFilehref);
		
		$('.jobFileData').hide();
		$('.fileTypeDiv').css('display','block');
		/*<input type="file" class="jobFile" name="jobFile" data-buttonText="Find file">*/
		dataValidationType.selectFieldsData(0);
	});

	 $("#dataValidationTypeTable").on("click","#validationTypeEdit",function(){
		 dataValidationType.cleanupdataValidationType();
		  var dataValidationTypeId =$(this).attr("data-dataValidationTypeId");
		 dataValidationType.selectFieldsData(dataValidationTypeId);
		 $("#validationTypeID").val(dataValidationTypeId);
	  });
	
	 $(document).on('click', '#saveDataTypeValidation,#updateDataTypeValidation', function(){
		 
		 		var validationTypeId = $("#validationTypeID").val()
				var validationTypeName = $("#validationTypeName").val();
				var validationTypeDesc = $("#validationTypeDesc").val();
				var dataValidation_Select = $("#dataValidation_Select option:selected").val();
				var jobName = $("#jobName").val();
				var jobFile =  $(".jobFile").val();
				var uploadedJobFileNames = $("#uploadedJobFileNames").val();
				var contextParameters = $("#contextParameters").val();
				
				var active =$("input:radio[name='active']:checked").val();
				
				var userID = $("#userID").val();
				var status = dataValidationType.validateFormTypeUpload();
				if(!status){
					return false;
				}
				var formData = new FormData($("#jobFileForm_direct")[0]);
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				showAjaxLoader(true);
				var url = "/app_Admin/user/" + userId
				+ "+/etlAdmin/saveDataValidationTypes";
				var myAjax = common.postAjaxCallForFileUpload(url, 'POST',
						formData, headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if (result != null && result.hasMessages) {
						if (result.messages[0].code == "SUCCESS") {
							$('#dataValidationTypeDiv').modal('hide');
							dataValidationType.getDataValidationTypeList();
						}
					}

				});
			});
	 
	 $(document).on("change",".jobFile, .existedJarFile",function(){
		 dataValidationType.updateUploadedFileNames();
		});
	    
		$(document).on("click"," .addJobFile, .deleteJobFile",function(e){
			 e.preventDefault();
		});
		
		$(document).on("click",".useOldJobFile",function(){
			dataValidationType.updateUploadedFileNames();
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
			dataValidationType.updateUploadedFileNames();
		});
}