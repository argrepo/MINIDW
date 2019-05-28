var webServiceAuthentication = {
		 initialPage : function(){ 
			 setTimeout(function() { $("#pageErrors").hide(); }, 5000); 
		 },
		 validateWebServiceTemplate : function(){
			 var validStatus = true;
			 var calBackURL = $('#oAuth2\\.redirectUrl').val();
			 var accessUrl = $('#oAuth2\\.accessTokenUrl').val();
			 var grantType = $('#OAuth2\\.grantType option:selected').text();
				 if(calBackURL == ''){
					 common.showcustommsg("#oAuth2\\.redirectUrl", globalMessage['anvizent.package.label.enterCallBackUrl'], "#oAuth2\\.redirectUrl");
					 validStatus = false;
				 }
				 if(accessUrl == ''){
					 common.showcustommsg("#oAuth2\\.accessTokenUrl", globalMessage['anvizent.package.label.enterAccessTokenUrl'], "#oAuth2\\.accessTokenUrl");
					 validStatus = false;
				 }
				 if(grantType == ''){
					 common.showcustommsg("#OAuth2\\.grantType", globalMessage['anvizent.package.label.enterGrantType'], "#OAuth2\\.grantType");
					 validStatus = false;
				 }
			 return validStatus;
		 },
		 
		 validateWebServiceBasicTemplate : function(){
			 var validStatus =true;
			 var webserviceName = $('#webServiceName').val();
			 var authUrl = $("#authenticationUrl").val();
			 var methodType = $('input[type=radio][name="authenticationMethodType"]:checked').val();
			 var authReqHeaders = $("#authrequestheaders").val();
			 var reqParamsArray = [];
			 var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/
				 if(webserviceName == ''){
					 common.showcustommsg("#webServiceName", globalMessage['anvizent.package.label.pleaseEnterWebServiceName'], "#webServiceName");
					  validStatus = false;
				 } else if(!regex.test(webserviceName)){
		      		  common.showcustommsg("#webServiceName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#webServiceName");
		      	      validStatus=false;
		      	  }
				 if(authUrl == ''){
					 common.showcustommsg("#authenticationUrl", globalMessage['anvizent.package.label.pleaseEnterAuthenticationURL'], "#authenticationUrl");
					 validStatus=false;
				 }
				 if(!methodType){
					 common.showcustommsg(".methodTypeSelectionValidation", globalMessage['anvizent.package.label.pleaseChooseMethodType'], ".methodTypeSelectionValidation");
					 validStatus=false;
				 }
				
				 $("#requestParamsTable tbody tr").each(function(){
					 var requestParam = $(this).find(".requestParam").val();
					 if(requestParam == '' ){
			   				common.showcustommsg($(this).find(".requestParam"),globalMessage['anvizent.package.label.enterReqparams'],$(this).find(".requestParam"));
			   				validStatus=false;
			   			}
			     });
				 var reqParamObject = {}
				 $("#authRequestParamsToApi .authRequestKeyValue").each(function(){
					 var reqKey = $(this).find(".authRequestKey").val();
					 var reqVal =$(this).find(".authRequestValue").val();
					 
					 if ( !reqKey && !reqVal ) {
						 
					 } else {
						 if(reqKey == '' ){
							 common.showcustommsg($(this).find(".authRequestKey"),globalMessage['anvizent.package.label.enterKey'],$(this).find(".authRequestKey"));
				   				validStatus=false;
						 }
						 if(reqVal ==''){
							 common.showcustommsg($(this).find(".authRequestValue"),globalMessage['anvizent.package.label.enterValue'],$(this).find(".authRequestValue"));
				   				validStatus=false;
						 }else{
							 reqParamObject[reqKey]=reqVal;
						 }
					 }
					 
					 
				 });
				 $("#apiAuthRequestParams").val(JSON.stringify(reqParamObject));
				 
			 return validStatus;
		 },
		 validateWebServiceNoAuthTemplate : function(){
			 var validStatus =true;
			 var webserviceName = $('#webServiceName').val();
				if(webserviceName == ''){
					 common.showcustommsg("#webServiceName", globalMessage['anvizent.package.label.pleaseEnterWebServiceName'], "#webServiceName");
					 validStatus= false;
				 }
				return validStatus;
		 }
}
if($(".webServiceAuthentication-page").length){
	webServiceAuthentication.initialPage();
	
	var addReq = $("#webserviceParamSample");
	addReq.find("[name^='webServiceTemplateAuthRequestparams[0]']").prop("checked",false)
	addReq.find("[name^='webServiceTemplateAuthRequestparams[0]']").val("");

	
	$("#webServiceAuthenticationTypes\\.id").on("change",function(){
		common.clearValidations(["#webServiceName,#authenticationUrl,#authenticationMethodType,.methodTypeSelectionValidation,#oAuth2\\.redirectUrl,#oAuth2\\.accessTokenUrl,#OAuth2\\.grantType,#oAuth2\\.authUrl,.requestParam,#authRequestKey,#authRequestValue,#authrequestheaders"]);
		$("#authenticationUrl,#oAuth2\\.redirectUrl,#oAuth2\\.accessTokenUrl,#oAuth2\\.authUrl,.requestParam,.oAuthRequestParam,.authRequestKey,.authRequestValue,.authreqheaders,#OAuth2\\.grantType").val("");
		$("[name='authenticationMethodType']").prop("checked",false);
		$(".isMandatory,.isPassword,.oAuthIsMandatory,.oAuthIsPassword").prop("checked",false);
		
		var authTypeid = $('#webServiceAuthenticationTypes\\.id option:selected').val();
		var authvalue = $('#webServiceAuthenticationTypes\\.id option:selected').val();
		var authName = $('#webServiceAuthenticationTypes\\.id option:selected').text();
			if(authvalue!=0){
			 $(".authName").text(authName);
			}
			if(authTypeid == 2){
				$(".calBackUrl,.oAuthAuthenticationUrl,.accessTokenUrl,.webServicesCoveringDiv").hide();
				$(".authView,.ViewSave").show();
			}
			if(authTypeid == 5){
				$(".calBackUrl,.oAuthAuthenticationUrl,.accessTokenUrl,.webServicesCoveringDiv,.authView,.ViewSave").show();
			}
			if(authTypeid == 1){
				$(".authView").hide();
				$(".ViewSave").show();
			}
	 });
		
		$(".addAuthRequestParam").on("click",function(){
			var rowCount = $("#requestParamsTable tbody tr").length;
			var addReq = $("#webserviceParamSample").clone().removeAttr("id");
			addReq.find("inupt[type='checkbox']:checked").prop("checked",false);
			addReq.find("inupt[type='text']").val(""); 
			
				addReq.find("[name^='webServiceTemplateAuthRequestparams[0]']").each(function(i,v){
					var vObj = $(v);
					vObj.prop("name",vObj.prop("name").split("[0]").join("["+rowCount+"]"));
				});
			$(addReq).removeAttr("id");
			$(addReq).css("display","");
			$("#requestParamsTable").append(addReq);
		});
	
		$(document).on("click","#deleteAuthRequestParam",function(){
			if ($("#requestParamsTable tbody tr").length) {
				$("#requestParamsTable tbody tr:last").remove();
			}
		});
		
$(document).on("click","#saveWebserviceTemp",function(){
	common.clearValidations(["#webServiceName,#authenticationUrl,#authenticationMethodType,.methodTypeSelectionValidation,#oAuth2\\.redirectUrl,#oAuth2\\.accessTokenUrl,#OAuth2\\.grantType,.requestParam,#oAuth2\\.authUrl,#authRequestKey,#authRequestValue,#authrequestheaders"]);
	var status=true;
	var statusTemp=true;
	var authTypeid = $('#webServiceAuthenticationTypes\\.id option:selected').val();
		if(authTypeid==1){
			 status = webServiceAuthentication.validateWebServiceNoAuthTemplate();
		}
		else if(authTypeid==2){
			 status = webServiceAuthentication.validateWebServiceBasicTemplate();
		}
		else if(authTypeid==5){
			  status = webServiceAuthentication.validateWebServiceBasicTemplate();
			  statusTemp = webServiceAuthentication.validateWebServiceTemplate();
		}
		if(!(status && statusTemp)){
			return status;
		}
		 else{
			$("#webServiceTemplateMaster").prop("action",$("#saveUrl").val());
			$("#webserviceParamSample").remove();
			$("#oAuthWebserviceParamSample").remove()
			showAjaxLoader(true);
       }
	});
	$("#authRequestParamsToApi").on("click",".addRequestParam",function(){
		var addReqParam = $("#addRequestParamsApi").clone().removeAttr("id").removeAttr("style");
		$("#authRequestParamsToApi").append(addReqParam);
	});
	$("#authRequestParamsToApi").on("click",".deleteRequestParam",function(){
		$(this).parents(".authRequestKeyValue").remove();
	});
	
}