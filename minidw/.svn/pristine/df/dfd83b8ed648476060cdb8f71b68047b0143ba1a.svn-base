var  database = {
		initialPage : function() {
			$("#databaseConnectorTable").dataTable();
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);	 
		},
		populateConnectionParameters : function(){
			var connectionStringParameters = $("#connectionStringParams").val();
			if (connectionStringParameters) {
				var connectionStringParametersObj = JSON.parse(connectionStringParameters);
				var connectionParamArray = connectionStringParametersObj["connectionParams"];
				
				if(connectionParamArray != undefined)
				$("#requestParamsTable").show();
					
				var _tblObj = $("#requestParamsTable tbody");
				var _footerRow = $("#requestParamsTable tfoot tr");
				_tblObj.empty();
				
				$.each(connectionParamArray,function(index, obj){
					var _newRow = _footerRow.clone();
					_newRow.find(".placeHolderLableName").val(obj['lableName']);
					_newRow.find(".placeHolderValue").val(obj['placeholderName']);
					_newRow.find(".placeHolderValueSpan").text(obj['placeholderName']);
					_newRow.find(".isMandatory").prop("checked",obj['isMandatory']);
					
					_tblObj.append(_newRow);
				});
			} else {
				$("#requestParamsTable").hide();
			}
		},
		dbUpdationFormValidation : function(){
			  common.clearValidations(["#databaseName, .activeStatus,#databaseName,#driverName,#protocal,#urlFormat,.connectorJars,.conJars,.uploadedJobFileNames"]);
			
	  	       	var databaseName=$("#databaseName").val();
	  	       	var driverName=$("#driverName").val();
	  	        var protocal=$("#protocal").val();
	  	        var urlFormat=$("#urlFormat").val();
	  	        
	  	      var jobFiles = [];
				$(".jobFilesDiv .fileContainer").find(".connectorJars").each(function(){	   	
					var filePath = $(this).val();
					if(filePath != "")
	    		    jobFiles.push(filePath.substring(filePath.lastIndexOf('\\')+1 , filePath.length));
				});
				
				$(".jobFilesDiv .fileContainer").find(".conJars").each(function(){
					if($(this).parents(".fileContainer").find(".useOldConnectorJarFile").is(":checked")){
						var fileName = $(this).val();
						if(fileName != ''){
							jobFiles.push(fileName);
						}
					}
				});
	  	        
	  	       	var selectors = [];
		   	    selectors.push("#databaseName");
		   	    var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/;
		   	 
	  	        var validStatus=true;
	      	    if(databaseName == '' ){
		  	    	common.showcustommsg("#databaseName", globalMessage['anvizent.package.label.pleaseEnterDatabaseName'],"#databaseName");
		  	    	validStatus=false;
	      	    }else if(!regex.test(databaseName)){
	      	    	common.showcustommsg("#databaseName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#databaseName");
		      	      validStatus=false;
	      	    }else{
	      	    	validStatus = common.validate(selectors);
	      	    	
	      	    }
	      	    if(!$("input[name='isActive']").is(":checked")){
	      	    	common.showcustommsg(".activeStatus", globalMessage['anvizent.package.label.PleaseChooseActiveStatus'],".activeStatus");
		  	    	validStatus=false;
	      	    }
	      	  if(driverName == '' ){
		  	    	common.showcustommsg("#driverName", globalMessage['anvizent.package.label.pleaseEnterDatabaseDriverName'],"#driverName");
		  	    	validStatus=false;
	      	    } 
	      	 if(protocal == '' ){
		  	    	common.showcustommsg("#protocal", globalMessage['anvizent.package.label.pleaseEnterDatabaseProtocal'],"#protocal");
		  	    	validStatus=false;
	      	    }
	      	 if(urlFormat == '' ){
		  	    	common.showcustommsg("#urlFormat", globalMessage['anvizent.package.label.pleaseEnterDatabaseUrlFormat'],"#urlFormat");
		  	    	validStatus=false;
	      	    }
	      	 
	      	 
	      	 $(".jobFilesDiv").find(".connectorJars").each(function(){	   	       
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
		   	    
	      	    return validStatus;
		 },
		 getPathParams : function(source,pattern){
			  var pathParams = [];   var lastIndex = 0;
			  if (!source.length || !pattern || !pattern.length) {
				  return pathParams;
			  }
			  while(source.indexOf(pattern,lastIndex) != -1) {
				  var param = "";
				  var startIndex = source.indexOf(pattern, lastIndex);
				  var endIndex = source.indexOf("}",startIndex);
				  param = source.substring(startIndex+pattern.length,endIndex);
				  
				  if ( pathParams.indexOf(param) == -1 ) {
					  pathParams.push(param);
				  }
				  
				  lastIndex = endIndex;
			  }
			  return pathParams;
		  }
}
if($('.database-page').length){
	database.initialPage();
	database.populateConnectionParameters();
	 $(document).on('click',"#updateDBMaster", function() {
		 
			var status= database.dbUpdationFormValidation();
			 var connectionStringParam = {};
				var _tblObj = $("#requestParamsTable tbody tr");
				if (_tblObj.length ) {
					connectionStringParam["connectionParams"] = [];
					_tblObj.each(function(index, tr){
						var _tr = $(tr);
						var paramObject = {
								"lableName":_tr.find(".placeHolderLableName").val(),
								"placeholderName":_tr.find(".placeHolderValue").val(),
								"isMandatory":_tr.find(".isMandatory").prop("checked"),
						};
						connectionStringParam["connectionParams"].push(paramObject);
					});
				}
				$("#connectionStringParams").val(JSON.stringify(connectionStringParam));
			console.log("status",status)
			if(!status){ 
			return false;
			}
			else{ 
				$("#databaseForm").prop("action",$("#updateUrl").val()); 
				this.form.submit();
				showAjaxLoader(true);
			}
	 });
	 $("#addDB").on('click', function() {
		 
			var status= database.dbUpdationFormValidation();
			if(!status){
				return false;
			}
			else{
				$("#databaseForm").prop("action",$("#addUrl").val()); 
				this.form.submit();
			    showAjaxLoader(true);
			}
	 });
	 
		$(document).on("click",".addConnectorJar",function(){
			var container = $(".jobFilesDiv");
			var jobFileContainer = $("#fileContainer").clone().removeAttr("id").addClass("fileContainer").show();
			container.append($(jobFileContainer));
		});
		
		$(document).on("click",".deleteConnectorJar",function(){
			$(this).parents(".fileContainer").remove();
		});
		
		$("#protocal").blur(function() {
			var protocol = $("#protocal").val();
			var protocolArr = database.getPathParams(protocol, "{");
			console.log(protocolArr);
			if (protocolArr.length) {
				$("#requestParamsTable").show();
			} else {
				$("#requestParamsTable").hide();
			}
			var _tblObj = $("#requestParamsTable tbody");
			var _footerRow = $("#requestParamsTable tfoot tr");
			_tblObj.empty();
			$.each(protocolArr,function(index, obj){
				var _newRow = _footerRow.clone();
				_newRow.find(".placeHolderLableName").val(obj);
				_newRow.find(".placeHolderValue").val(obj);
				_newRow.find(".placeHolderValueSpan").text(obj);
				
				_tblObj.append(_newRow);
			});
		});
	
}