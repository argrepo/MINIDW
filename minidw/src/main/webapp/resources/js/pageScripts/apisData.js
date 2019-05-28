var headers = {};
var apisData = {
		initialPage : function() {			
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
	},
	
	viewSelectQuery :  function(result){
		  if(result.messages[0].code == "SUCCESS") {
				 var  messages=[{
					  code : result.messages[0].code,
					  text : result.messages[0].text
				  }];
				 var sqlScript = result.object.apiQuery;
					
				 
				 if(sqlScript === "" || sqlScript === null){
					 sqlScript = "No Query Found.";
				} 
				 openPopUp(sqlScript, "Api Query");			          
			          
			  }else {
		    		common.displayMessages(result.messages);
		    	}
	  
	},
	
	viewLink :  function(urlLink){
				 
				 if(urlLink === "" || urlLink === null){
					 urlLink = "No Query Found.";
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
			          popup.document.title = "Test Link";
			          popup.document.body.innerHTML = "<pre>"+urlLink+"</pre>";
			          if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
			        	  popup.addEventListener (
				        	        "load",
				        	        function () {
				        	            var destDoc = popup.document;
				        	            destDoc.open ();
				        	            destDoc.title = "Api Query";
				        	            destDoc.write ('<html><head></head><body><pre>'+urlLink+'</pre></body></html>');
				        	            destDoc.close ();
				        	        },
				        	        false
				        	    );
			          }

			          
			          
			  
	  
	},

}

if($('.apisData-page').length){
apisData.initialPage();
	
$(document).on("click",".editApi",function(){
	var id =  $(this).attr("data-apiId");
	 var userID = $("#userID").val();
	
	showAjaxLoader(true);
	var url_getApistDetailsById = "/app/user/"+userID+"/package/getApistDetailsById/"+id;
	
	   var myAjax = common.loadAjaxCall(url_getApistDetailsById,'GET',id,headers);
	   myAjax.done(function(result) {	
		   showAjaxLoader(false);
	    	  if(result != null && result.hasMessages) {
	    		  if(result.messages[0].code == "ERROR"){	    			  
	    			 var message = result.messages[0].text;
	    			 $("#popUpApisDataMsg").append("<div class='alert alert-danger'>"+result.messages[0].text+"</div>");
	    		  }
	    		  else if(result.messages[0].code == "SUCCESS"){
	    			  $("#apisDataPopUp").modal("show");
	    			 var resultVal = result.object;
	    			 var methodType = result.object.methodType;
	    			 var activeStatus = result.object.active;
	    			 $("#apiId").val(resultVal.id);
	    			 $("#apiName").val(resultVal.apiName);
	    			 $("#endPointUrl").val(resultVal.endPointUrl); 
	    			 if(methodType == 'GET')
	    					$("input[name='methodType'][value='GET']").prop("checked",true);
	    				else
	    					$("input[name='methodType'][value='POST']").prop("checked",true);
	    			 $("#apiQuery").val(resultVal.apiQuery); 
	    			 $("#apiDescription").val(resultVal.apiDescription);
	    			 activeStatus ? $("input[name='active'][value='true']").prop("checked",true) : $("input[name='active'][value='false']").prop("checked",true);
	    		  } 
	    	  }	else{
		    		var messages = [ {
		    			code : globalMessage['anvizent.message.error.code'],
		    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
		    		} ];
		    		common.displayMessages(messages);
		    	}    	  	
	    });
	
	
})

$(document).on("click","#apisData",function(){
	 var userID = $("#userID").val();
	 common.clearValidations(["#apiName,#endPointUrl,#apiDescription,#methodType"])
	    
	    var apiName = $("#apiName").val();
	    var endPointUrl = $("#endPointUrl").val();
	    var methodType =  $("[name='methodType']:checked").val();
	    var query = $("#apiQuery").val();
	    var apiDescription = $("#apiDescription").val();
	    var id = $("#apiId").val();
	    var activeStatus =  $("[name='active']:checked").val();
	    
	    if(apiName.trim().length == 0){ 
	    	common.showcustommsg("#apiName", globalMessage['anvizent.package.label.pleaseEnterAPIName']);
			return false;
	    }
	    if(endPointUrl == ''){ 
	    	common.showcustommsg("#endPointUrl", globalMessage['anvizent.package.label.pleaseEnterEndPointUrl']);
			return false;
	    }
	    if(!methodType){ 
	    	common.showcustommsg(".method_type", globalMessage['anvizent.package.label.pleaseChooseMethodType']);
			return false;
	    }
	    
	    if(apiDescription == ''){ 
	    	common.showcustommsg("#apiDescription",globalMessage['anvizent.package.label.pleaseEnterApiDescription']);
			return false;
	    }
	    
	    if(query == ''){ 
	    	common.showcustommsg("#apiQuery", globalMessage['anvizent.package.label.pleaseEnterQuery']);
			return false;
	    }
	    
	    
	    var selectors = [];

		selectors.push('#apiName');
	    var valid = common.validate(selectors);
	    if (!valid) {
			return false;
		}
		
	   var selectData = {
			   id : id,
			   apiName : apiName,
			   endPointUrl : endPointUrl,
			   apiDescription : apiDescription,
			   methodType : methodType,
			   apiQuery : query,
			   active : activeStatus
			  
	   }
	   var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
	    showAjaxLoader(true); 
	    var url = "/app/user/"+userID+"/package/saveApisDataInfo";
	    var myAjax = common.postAjaxCall(url,'POST', selectData,headers);
	    
	    myAjax.done(function(result) {
	    	
	    	if (result != null && result.hasMessages) {
	    		showAjaxLoader(false);
	    		var messages = result.messages;
	    		var msg = messages[0];
	    		if (msg.code === "SUCCESS") {
	    			 common.showSuccessAlert(result.messages[0].text);
					 $("#apisDataPopUp").modal('hide');    	
	    		}else{
	    			 $("#apisDataPopUp").modal('show');
	    			 common.showErrorAlert(result.messages[0].text);
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
	
$(document).on("click",".viewQuery",function(){
	var id =  $(this).attr("data-apiId");
	 var userID = $("#userID").val();
	showAjaxLoader(true);
	var url_getApistDetailsById = "/app/user/"+userID+"/package/getApistDetailsById/"+id;
	
	   var myAjax = common.loadAjaxCall(url_getApistDetailsById,'GET',id,headers);

		myAjax.done(function(result) {
			showAjaxLoader(false);
			if(result != null){
	    		  if(result.hasMessages){
    				  if(result.messages[0].code=="ERROR"){
    					  common.showErrorAlert(result.messages[0].text);
	    			  }
	    			  if(result.messages[0].code=="SUCCESS"){
	    				  apisData.viewSelectQuery(result); 
	    			  }
	    		  }
			}
	});
});

$(document).on("click",".testLink",function(){
	var urlLink =  $(this).attr("data-urlLink");
	 apisData.viewLink(urlLink); 
		
});


$(document).on("click",".testBtn",function(){
	var url =  $(this).attr("data-url");
	var type =  $(this).attr("data-type");
	var clientId =  $(this).attr("data-clientid");
	var data = '';
	url += "?X-Authentication-code="+clientId; 
	showAjaxLoader(true);
	var custHeaders = {};
	custHeaders['X-Authentication-code'] = clientId;
	var myAjax =$.ajax({
        url: url,
        headers:custHeaders,
        type: type,
        data: (type.toLowerCase()=="get" ? "":JSON.stringify(data)),
        cache: false,
        contentType: "application/json; charset=utf-8",
	    error: function (jqXHR, exception) {
	    	common.errorMessages(jqXHR, exception);
	        }
    });

		myAjax.done(function(result) {
			showAjaxLoader(false);
			openPopUp(JSON.stringify(result), "Sample Data");
	});
});

function openPopUp(sqlScript,title) {
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
       popup.document.title = title;
       popup.document.body.innerHTML = "<pre>"+sqlScript+"</pre>";
       if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
     	  popup.addEventListener (
	        	        "load",
	        	        function () {
	        	            var destDoc = popup.document;
	        	            destDoc.open ();
	        	            destDoc.title = "Api Query";
	        	            destDoc.write ('<html><head></head><body><pre>'+sqlScript+'</pre></body></html>');
	        	            destDoc.close ();
	        	        },
	        	        false
	        	    );
       }
}

} 
