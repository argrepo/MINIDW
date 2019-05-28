var clientConfigurationSettings = {
		initialPage : function(){
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
		},
		
}
if($('.clientConfigurationSettings-page').length){
	clientConfigurationSettings.initialPage();
	$("#saveClientConfigSettings").on("click",function(){
		common.clearValidations(["#from,#password,#to,#cc,#bcc,#replyTo,#smtpHost,#smtpFactoryPort,#smtpPort"])
		var selectors = [];			
		selectors.push({"selector":".from","message":"Please enter from","regex":"","regexMsg":""});
		selectors.push({"selector":".password","message":"Please enter password","regex":"","regexMsg":""});
		selectors.push({"selector":".to","message":"Please enter to","regex":"","regexMsg":""});
		selectors.push({"selector":".smtpHost","message":"Please enter SMTP Host","regex":"","regexMsg":""});
		selectors.push({"selector":".socketFactPort","message":"Please enter Socket Factory Port","regex":"","regexMsg":""});
		selectors.push({"selector":".smtpPort","message":"Please enter SMTP Port","regex":"","regexMsg":""});
		var status = common.validateWithCustomMessages(selectors);	
		if(!status){
			return false;
		}else{
			$("#clientConfigurationSettings").prop("action",$("#save").val());
			this.form.submit();
			showAjaxLoader(true);
		}
	});
	$(document).on("change","#clientId",function(){
		common.clearValidations(["#from,#password,#to,#cc,#bcc,#replyTo,#smtpHost,#smtpFactoryPort,#smtpPort"])
		$("#from,#password,#to,#cc,#bcc,#replyTo,#smtpHost,#smtpFactoryPort,#smtpPort").val("");
		this.form.submit();
		showAjaxLoader(true);
	});
}