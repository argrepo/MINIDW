var clientConfigurationSettings = {
		initialPage : function(){
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
		},
		
}
if($('.auth-clientConfigurationSettings-page').length){
	clientConfigurationSettings.initialPage();
	$("#saveClientConfigSettings").on("click",function(){
		var selectors = [];			
		selectors.push({"selector":".from","message":"Please enter from","regex":"","regexMsg":""});
		selectors.push({"selector":".password","message":"Please enter password","regex":"","regexMsg":""});
		selectors.push({"selector":".to","message":"Please enter to","regex":"","regexMsg":""});
		selectors.push({"selector":".smtpHost","message":"Please enter smtpHost","regex":"","regexMsg":""});
		selectors.push({"selector":".socketFactPort","message":"Please enter socketFactPort","regex":"","regexMsg":""});
		selectors.push({"selector":".smtpPort","message":"Please enter smtpPort","regex":"","regexMsg":""});
		var status = common.validateWithCustomMessages(selectors);	
		if(!status){
			return false;
		}else{
			showAjaxLoader(true);
			return true;
		}
	});
	
	$("#skipClientConfigSettings").on("click",function(){
		$(".from, .password, .to, .smtpHost, .ccInfo, .bccInfo, .replyTo").val("");
		$(" .socketFactPort, .smtpPort").val("0");
		showAjaxLoader(true);
		return true;
	});
	
	
	
	/*$(document).on("change","#clientId",function(){
		common.clearValidations(["#from,#password,#to,#cc,#bcc,#replyTo,#smtpHost,#smtpFactoryPort,#smtpPort"])
		$("#from,#password,#to,#cc,#bcc,#replyTo,#smtpHost,#smtpFactoryPort,#smtpPort").val("");
		this.form.submit();
		showAjaxLoader(true);
	});*/
}