var currencyLoad = {
		initialPage : function() {

		},

		flatFileTypeCheck : function() {
			var flatFile = $("#flatFileType option:selected").val();
			if (flatFile == 'csv') {
				$(".delimeter-block").show();
			} else {
				$(".delimeter-block").hide();
			}
		}

}

if ($('.currencyLoad-page').length) {

	$("#flatFileType").on('change', function() {
		currencyLoad.flatFileTypeCheck();
		$("currencyLoadForm").next('.delimeter-block').css("background-color","green");
	});



	$("#saveAndUpload").on('click', function(){
		$("#currencyLoadForm").next().css("background-color","green");

		var fileupload =  $("#fileUpload").val();
		var flatFile = $("#flatFileType option:selected").val();
		var delimiter = $("#delimeter").val();

		if(fileupload == ''){
			common.showcustommsg("#fileUpload", globalMessage['anvizent.package.label.pleaseChooseaFile']);
			return false;
		}
		
		var fileExtension = fileupload.replace(/^.*\./, '');
	    if(fileExtension != flatFile) {
	    	common.showcustommsg("#fileUpload", globalMessage['anvizent.package.label.fileExtensionIsNotMatchingWithFileTypeSelected']);
	        return false;
	    }
	    
	    showAjaxLoader(true);
		
		//submit the file upload form	
		$("#userIdForFileUpload").val(userID);

		//to avoid FormData object issue
		setTimeout(function(){
			var formData = new FormData($("#currencyLoadForm")[0]);
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			var userID = $("#userID").val();
			headers[header] = token;				 
			var url_uploadFileIntoCurrencyLoad = "/app/user/"+userID+"/package/schedule/uploadFileIntoCurrencyLoad";
			var myAjax = common.postAjaxCallForFileUpload(url_uploadFileIntoCurrencyLoad,'POST', formData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
		    	  if(result != null){
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") {
		    				  $("#fileUpload").val('');
		    				  showAjaxLoader(false);
								 var  messages=[{
									  code : result.messages[0].code,
									  text : result.messages[0].text
								  }];
								 common.displayMessages(messages);
			    				 return false;
		    			  }else if(result.messages[0].code == "SUCCESS"){
		    				  $("#fileUpload").val('');
		    				  var  messages=[{
								  code : result.messages[0].code,
								  text : result.messages[0].text
							  }];
							 common.displayMessages(messages);
		    			  }
		    		  }
		    	  }
		    });

		});

	});
	
	
	$(document).on('click', '#downloadTemplate', function() {
		$("#downloadILTemplate").modal("show");
	});
	$(document).on('click', '#confirmDownloadILTemplate', function() {
		var il_id=$("#iLName" ).val();
		var userID = $("#userID").val();
		var packageId = $("#packageId").val();
		var templateType='';
		$("#downloadILTemplate").modal("hide");
		if($('#ilCsv').is(':checked')) {
			templateType="csv";	
		}
		if($('#ilXls').is(':checked')) {
			templateType="xls";	
		}
		if($('#ilXlsx').is(':checked')) {
			templateType="xlsx";	
		}
		
		var url_defaultILincrementalquery = "/app/user/"+userID+"/package/checkFordownloadIlCurrencyRateTemplate/"+templateType;
		 var myAjax = common.loadAjaxCall(url_defaultILincrementalquery,'GET','',headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    	if(result != null && result.hasMessages){
		    		if(result.messages[0].code == "SUCCESS") {
		    			window.open(adt.contextPath+"/app/user/"+userID+"/package/downloadIlCurrencyRateTemplate/"+templateType,"_blank","");
		    		} else {
		    			common.displayMessages(result.messages);
		    		}
		    		
		    	}else{
		    		standardPackage.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    	}
		    });
		    
	});
}
