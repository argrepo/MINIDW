var addBusinessCaseValidation = {
		initialPage : function(){
			/*$("#dlList").select2({               
                allowClear: true,
                theme: "classic"
			});*/ 
			$(".dlList").multipleSelect({
				filter : true,
				placeholder : 'Select Module / DL',
			    enableCaseInsensitiveFiltering: true
			});
			
			$("#validationTypeId").select2({               
                allowClear: true,
                theme: "classic"
			});
			
		},
		dataValidationType : function(validationType) {

			var _selector = $("[name='validationTypeId']");
			_selector.empty();
			common.populateComboBox(_selector, "0", "Select Data Validation Type");
			$.each(validationType, function(key, value) {
				_selector.append($("<option>", {
					text : value,
					value : key
					
				}));
			});
			_selector.select2();
		},
		clearvalidationTypedata:function(){
			$("#validationTypeid").val("");
		},
		validateFormTypeUpload : function(){
			
			var validStatus = true;
			var jobName = $(".jobName").val();
			var uploadedJobFileNames = $(".uploadedJobFileNames").val();
			
			var validationTypeid = $("#validationTypeid").val();
			if(validationTypeid == 0){
				common.showcustommsg("#validationTypeid","Please Choose Validation Type Id");
				validStatus=false; 
			}
			
			var activeStatus = $("input[name='active']").is(":checked") ? true : false;

			if(!activeStatus){
				common.showcustommsg("#activestatusJarFile", globalMessage['anvizent.package.label.PleaseChooseActiveStatus'],".#activestatusJarFile");
				validStatus=false;
			}
				return validStatus;
		},
		
		postLoadCreationFormValidation : function(){
			 var validStatus=true;
			 var validationName = $("#validationName").val();
			 var validationScripts = $("#validationScripts").val();
			 var dlInfo =$("#dlList").multipleSelect("getSelects","text");
			 validationType = $("#validationTypeId").val();
			 common.clearValidations(['#validationName, #validationScripts, #isActive, #dlList']);
			 var validStatus=true;
			 
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
}

if($('.addBusinessCaseValidation-page').length){
	addBusinessCaseValidation.initialPage();
	if ($("#dlList").length) {
		$("#dlList").multipleSelect({ filter : true, placeholder : "Select Module / DL", });
	}
	$("#tbusinessCasesValidationTable").DataTable({
		"order": [[0, "desc" ]],"language": {
            "url": selectedLocalePath
        }
	});
	$(document).on('click', '#resetBusinessCasesValidation', function(){
		common.clearValidations(['#validationName', '#validationScripts', 'isActive', '#dlList']);
		$('#dlList').multipleSelect("uncheckAll");
		$("#validationName").val("");
		$('#validationScripts').val("");
		$("#active1").attr("checked",true);
	});
	
	$("#addBusinessCasesValidation").on('click', function() {
		 
	       var status= addBusinessCaseValidation.postLoadCreationFormValidation();
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
		 
		   var status= addBusinessCaseValidation.postLoadCreationFormValidation();
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
	
	$(document).on('click', '#viewValidationscript', function(){
		var validationscriptId = $(this).attr('data-scriptId');
		var userId = $('#userID').val();
		
		var selectData = {
				scriptId : validationscriptId
		};
		
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
		
		showAjaxLoader(true);
		var url = "/app_Admin/user/"+userId+"+/etlAdmin/viewValidationScriptByScriptId";
		
		var myAjax = common.postAjaxCallObject(url, 'POST', selectData, headers);
		  myAjax.done(function(result){
				showAjaxLoader(false);
				if(result != null && result.hasMessages){
					if(result.messages[0].code == 'SUCCESS'){
						if(result.object.scriptId !=null || result.object.scriptId != 0){
							 addBusinessCaseValidation.viewValidationScript(result.object.validationScripts);
						 }else{
							 addBusinessCaseValidation.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
						 }
					}
					else{
						common.displayMessages(result.messages);
					}
				}
			});
		 
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