var headers = {};
var fileSettings = {
		initialPage : function() {
			if ( $('#multiPartEnabled').length > 0 ) {
				var multiPartEnabled=$('#multiPartEnabled').is(":checked");
				var noOfRecordsPerFile=$('#noOfRecordsPerFile').val().trim();
				if ( multiPartEnabled ) {
					$('.noOfRecordsPerFilesDiv').show();
					
				} else {
					$('#noOfRecordsPerFile').val("");
					$('.noOfRecordsPerFilesDiv').hide();
				}
			}
			
			debugger
			if($("select#clientId").length){
				$("#clientId").select2({               
	                allowClear: true,
	                theme: "classic"
				});
			}
			
	}
}

if($('.fileSettings-page').length || $('.clientchange-page').length ){
	
	fileSettings.initialPage();
	$(document).on('click' ,'#updateMaxFileSize', function() {
		 var userID = $("#userID").val();
		 var maxFileSizeInMB=$('#maxFileSize').val();
		 var multiPartEnabled=$('#multiPartEnabled').is(":checked");
		 var fileEncryption=$('#fileEncryption').is(":checked");
		 var noOfRecordsPerFile=$('#noOfRecordsPerFile').val().trim();
		 var regex = new RegExp(/^\+?[0-9()]+$/); 
		  common.clearValidations(["#maxFileSize","#noOfRecordsPerFile"]);
		  
		  if ( multiPartEnabled ) {
			  if ( noOfRecordsPerFile == '') {
				    common.showcustommsg("#noOfRecordsPerFile", 'Please enter no. of allowable records per file',"#noOfRecordsPerFile");
					return false;
			   }
			  if ( !noOfRecordsPerFile.match(regex) ) {
				  common.showcustommsg("#noOfRecordsPerFile",globalMessage['anvizent.package.label.allowsOnlyNumeric'],"#noOfRecordsPerFile");
				  return false;
			  }
			  
			  if ( parseInt(noOfRecordsPerFile,10) <= 1000 ) {
				  common.showcustommsg("#noOfRecordsPerFile",'Record limit should be greater than 1000',"#noOfRecordsPerFile");
				  return false;
			  }
		  }
		  
		  if ( !maxFileSizeInMB ) {
			  maxFileSizeInMB = "2024";
		  }
		 if ( maxFileSizeInMB == '') {
			 common.showcustommsg("#maxFileSize", globalMessage['anvizent.package.label.maxFIleSizeEmpty'],"#maxFileSize");
			 return false;
		 }else if(maxFileSizeInMB == 0){
			 common.showcustommsg("#maxFileSize", globalMessage['anvizent.package.label.maxFIleSizeshouldnot'],"#maxFileSize");
			 return false;
		 }
		  var regex = new RegExp(/^\+?[0-9()]+$/);
		    if(!maxFileSizeInMB.match(regex)) {
		    		  common.showcustommsg("#maxFileSize",globalMessage['anvizent.package.label.allowsOnlyNumeric'],"#maxFileSize");
		    		  return false;
		    }
		    
		    
		  
		    var selectData = {
		    		maxFileSizeInMb : maxFileSizeInMB,      
		    	    multiPartEnabled:multiPartEnabled,
		            noOfRecordsPerFile:noOfRecordsPerFile,
		            fileEncryption : fileEncryption
		    }
		    console.log("selectData:",selectData);
		     
		    var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		     var updateMaxFileSize_url = "/app_Admin/user/"+userID+"/etlAdmin/updateFileSettings";
		     var myAjax = common.postAjaxCall(updateMaxFileSize_url,'POST',selectData,headers);
		     myAjax.done(function(result) {		    		
		    	  if(result != null){ 
		    		  if(result.hasMessages){
		    			 
		    			  if(result.messages[0].code == "ERROR") { 
		    				  var  message = '<div class="alert alert-danger alert-dismissible" role="alert">'+
								''+''+result.messages[0].text+''+'</div>';
		    				  $('.createFileSizeSucessFailure').show().empty().append(message);
				    		  setTimeout(function() { $(".createFileSizeSucessFailure").hide().empty(); }, 10000);
			    			  return false;
			    			  
		    			  } else if(result.messages[0].code == "SUCCESS") {		    				 
		    				  var  message = '<div class="alert alert-success alert-dismissible" role="alert">'+
								''+''+result.messages[0].text+''+ '</div>';
		    				  $('.createFileSizeSucessFailure').show().empty().append(message);
				    		  setTimeout(function() { $(".createFileSizeSucessFailure").hide().empty(); }, 10000);
		    			  }
		    			 
		    		  }
		    	  }		    	   
		    }); 
	});
	
	
	$("#multiPartEnabled").on('click',function(){
		$('#noOfRecordsPerFile').val("100000");
		if ( this.checked ) {
			$('.noOfRecordsPerFilesDiv').show();
		} else {
			$('.noOfRecordsPerFilesDiv').hide();
		}
		
	});
}