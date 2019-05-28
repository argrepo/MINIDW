var headers = {};
var addWebService = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		},
		updateWebServiceAuthPan : function(result){
			
				var webServiceId = result["web_service_id"];
				var webServiceName = result["webserviceName"];
				var authUrl = result["authenticationUrl"];
				var methodType = result["authentication_Method_Type"];                                  
				var authrequestParams = result["authentication_Request_Params"];
				$("#webServiceName").val(webServiceName);
				$("#authenticationUrl").val(authUrl);
				$("#methodType").val(methodType);
				$(".authRequestKey").val(authrequestParams);
				$(".authRequestValue").val(authrequestParams);
		
			
			$(".createWebServicePanel").show();
		}
}

if($('.addWebService-page').length){
 addWebService.initialPage();
 
 $(document).on("click",".addAuthRequestParam",function(){
	  $(this).find("#authRequestParams").clone();
 })
 
 $(document).on('click',"#addwebservice",function(){
	 $(".createWebServicePanel").show();		 
 });
 
 $(document).on('click',"#editwebservice",function(){
	 	common.clearValidations([".selectwebservice"]);	 	
	 	
	 	$("#webServiceName,#authenticationUrl, .authRequestKey, .authRequestValue").val("");
	 	$("input[name='methodTypeAuthSelection']").prop("checked",false);
	 	
	 	
	 	var userID = $("#userID").val();
	 	var webServiceId = $(".selectwebservice option:selected").val();
	 	
	 	if(webServiceId != 0){
	 		
	 		var selectData = {
					web_service_id : webServiceId,
			}
			
			showAjaxLoader(true);
			var url_getWebServiceAuthDetailsById = "/app_Admin/user/"+userID+"/etlAdmin/getWebServiceAuthDetailsById";
			   var myAjax = common.postAjaxCall(url_getWebServiceAuthDetailsById,'POST',selectData,headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					console.log(result);		
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;
			    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>");
			    			  }
			    			  if(result.messages[0].code=="SUCCESS"){
			    				// updateWebServiceAuthPan
			    				  addWebService.updateWebServiceAuthPan(result.object);
			    			  }
			    		  }
					}
			});
	 	}
	 	else{
	 		 $(".createWebServicePanel").hide();	
	 		 common.showcustommsg(".selectwebservice","Please select web service");
	 	}
		
	});
	 
	 
 
 
 $(document).on("click",".addAuthRequestParam",function(){
	  
     $("#authRequestParams").append(  
     '<div class="row form-group authRequestKeyValue" id="authRequestKeyValue" >' +
    '<label class="control-label col-sm-2"></label>' +
     '<div class="col-sm-4">' +
      '<input type="text" class="form-control authRequestKey" id="authRequestKey"   placeholder="'+globalMessage['anvizent.package.label.enterKey']+'">'+
     '</div>' +
     '<div class="col-sm-4">' +
       '<input type="text" class="form-control authRequestValue" id="authRequestValue" placeholder="'+globalMessage['anvizent.package.label.enterValue']+'">'+
     '</div>' +
     '<div class="col-sm-2" >' +
     '<button class="btn btn-primary btn-sm addAuthRequestParam"><span class="glyphicon glyphicon-plus"></span></button>' +
     '&nbsp;<button  class="btn btn-primary btn-sm deleteAuthRequestParam"><span class="glyphicon glyphicon-trash"></span></button>'+
     '<span><input type="checkbox" class = "viewOrShow">View/Show</span>' +
     '</div>' +
   '</div>');
      
 });
 $(document).on("click",".deleteAuthRequestParam",function(){ 		
		$(this).parents(".authRequestKeyValue").remove(); 		 			 
	 
	});

	  
	  $("#getAuthenticationObject").click(function(){
			
			common.clearValidations(["#authenticationUrl",".methodTypeForAuthenticationValidation","#authRequestKeyValue"]);
			var webServiceName = $("#webServiceName").val().trim();
			var authUrl = $("#authenticationUrl").val().trim();
			var userID = $("#userID").val();
			var requestMethod = $('input[name=methodTypeAuthSelection]:checked').val();
			$(".authorisationObjectParams").hide();
			var paramsListVar = $(".paramsList").hide();
			paramsListVar.children().not(":first").remove()
			 
	  	    var authRequsetParamsObject = {};   
	  	    var authRequsetParams = [];
	  	    
		    $('#authRequestParams').find('.authRequestKeyValue').each(function(i, obj) {
		    	
		    	var authRequestKey=$(this).find(".authRequestKey").val(); 
			    var authRequestValue=$(this).find(".authRequestValue").val();
			     
			    authRequsetParamsObject[authRequestKey] = authRequestValue;
			   
			    
		    });
		  authRequsetParams.push(authRequsetParamsObject);
		  
		var validStatus=true;
		var methodTypeAuthSelection = $('input[name=methodTypeAuthSelection]').is(':checked');
	   	if(!methodTypeAuthSelection){
	   	 common.showcustommsg(".methodTypeForAuthenticationValidation", globalMessage['anvizent.package.label.pleaseChoosemethodTypeAuthSelection'],".methodTypeForAuthenticationValidation");
	     validStatus =false;
	   	  
	   	} 
	   	
		  if(!validStatus){
			 	return validStatus;
		   }
		   
			if ( authUrl ) {
				var url_getAuthenticationObject = "/app/user/"+userID+"/package/getAuthenticationObjectForAllApi";
				
				var selectedData = {
	  					
  					  authenticationUrl:authUrl,
  					  authRequsetParams:authRequsetParams,
  					  requestMethod:requestMethod,
  			}
				
				showAjaxLoader(true);
	      		console.log("selectedData  - > ", selectedData );
				var myAjax = common.postAjaxCall(url_getAuthenticationObject,'POST',selectedData,headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	console.log("authenctication",result.object); 
			    	  if(result.object != null && result.hasMessages){
			    		    var messages = result.messages;
				      		var msg = messages[0];
				      		if (msg.code === "SUCCESS") {
				      			  $("#statuscode").text(msg.text);
				      			  $('#statuscode').css('color','blue');
				      		 
					    		  $(".authorisationObjectParams").show();
					    		  $(".save").show();
					    		  $(".save").removeAttr("disabled");
				      		}
				      		else if (msg.code === "ERROR") {
				      			common.showErrorAlert(msg.text);
				      			$("#statuscode").text(msg.text);
				      			$('#statuscode').css('color','red');
				      			$(".authorisationObjectParams").show();
		    				    return false;
				      		}
				      		else {
				      			common.showErrorAlert(msg.text);
				      			$("#statuscode").text(msg.text);
				      			$('#statuscode').css('color','red');
				      			$(".authorisationObjectParams").show();
		    				    return false;
				      		 }   
			      	  }else{
			      		var messages = result.messages;
			      		var msg = messages[0];
			      		if (msg.code === "ERROR") {
			      			common.showErrorAlert(msg.text);
			      			$("#statuscode").text(msg.text);
			      			$('#statuscode').css('color','red');
			      			$(".authorisationObjectParams").show();
	    				    return false;
			      		}
			      	  }
			    });
				
			} else {
				common.showErrorAlert("Invalid");
			}
			
		});
		
		$(document).on('click', '.save', function(){
			
			var userID = $("#userID").val();
			var webServiceName = $("#webServiceName").val().trim();
			var authUrl = $("#authenticationUrl").val().trim();
			var methodtype = $('input[name=methodTypeAuthSelection]:checked').val();
			var authRequestParams = $("#authRequestValue").val();
			
			  if(webServiceName == '' ){
		  	    	 common.showcustommsg("#webServiceName", globalMessage['anvizent.package.label.enterWebServiceName'],"#webServiceName");
		  	    	validStatus = false;
		  	     }
			
			var selectData = {
					webserviceName : webServiceName,
					authenticationUrl : authUrl,
					authentication_Method_Type : methodtype,
					authentication_Request_Params : authRequestParams
			}
			 showAjaxLoader(true);
			var url_createWebService = "/app_Admin/user/"+userID+"/etlAdmin/createWebService";
			   var myAjax = common.postAjaxCall(url_createWebService,'POST', selectData,headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
			    			  
			    				  if(result.messages[0].code=="SUCCESS"){
				    				var message = result.messages[0].text;
				    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>");
				    			  }
				    			  if(result.messages[0].code=="ERROR"){
				    				 var message = result.messages[0].text;
				    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>");
				    			  }
			    			 
			    			}
		}
		});
		});
		
		
$('.update').on('click', function(){
			
			var userID = $("#userID").val();
			var webServiceId = $(".selectwebservice option:selected").val();
			var webServiceName = $("#webServiceName").val().trim();
			var authUrl = $("#authenticationUrl").val().trim();
			var methodtype = $('input[name=methodTypeAuthSelection]:checked').val();
			var authRequestParams = $("#authRequestValue").val();
			
			var selectData = {
					webserviceName : webServiceName,
					authenticationUrl : authUrl,
					authentication_Method_Type : methodtype,
					authentication_Request_Params : authRequestParams,
					web_service_id : webServiceId
			}
			console.log(selectData)
			showAjaxLoader(true);
			var url_updateWebServicesById = "/app_Admin/user/"+userID+"/etlAdmin/updateWebServicesById";
			   var myAjax = common.postAjaxCall(url_updateWebServicesById,'POST', selectData,headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
			    			  
			    				  if(result.messages[0].code=="SUCCESS"){
				    				var message = result.messages[0].text;
				    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>");
				    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);
				    				  $(".createWebServicePanel").hide();
				    				  $(".selectwebservice option:selected").empty();
				    			  }
				    			  if(result.messages[0].code=="ERROR"){
				    				 var message = result.messages[0].text;
				    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>");
				    			  }
			    			 
			    			}
		}
		});
		});
		
		
		$("#authenticationUrl").on('keyup',function(){
			var authUrl = $("#authenticationUrl").val().length;
			if( authUrl > 0 ){
				$("#getAuthenticationObject").removeAttr("disabled");
				$(".save").attr("disabled",true);
	     	}else{
	     		$('#getAuthenticationObject').attr("disabled","disabled");

	     	}
			
		});
		$(document).on("click",".viewOrShow",function(){
			var viewOrShowStatus = $(".viewOrShow").is(":checked");
        	if(viewOrShowStatus){
        		$(this).parents(".authRequestKeyValue").find(".authRequestValue").prop('type','password');
        	}else{
        		$(this).parents(".authRequestKeyValue").find(".authRequestValue").prop('type','text');
        		
        	}
		});
		
}
