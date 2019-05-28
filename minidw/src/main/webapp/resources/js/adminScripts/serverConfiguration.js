var serverConfiguration = {
		initialPage : function(){
			$("#existingServerConfigurations").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
			
		},
		validateForm : function(){
			var status = true;
			var selectors = [];
			selectors.push({ "selector":"#serverName", "message":"Please enter server name.", "regex":"", "regexMsg":"" });
			selectors.push({ "selector":"#description", "message":"Please enter description.", "regex":"", "regexMsg":"" });
			selectors.push({ "selector":"#ipAddress", "message":"Please enter Ip address.", "regex":"", "regexMsg":"" });
			selectors.push({ "selector":"#portNumber", "message":"Please enter port number.", "regex":"", "regexMsg":"" });
			selectors.push({ "selector":"#minidwSchemaName", "message":"Please enter minidw schema name.", "regex":"", "regexMsg":"" });
			selectors.push({ "selector":"#anvizentSchemaName", "message":"Please enter anvizent schema name.", "regex":"", "regexMsg":"" });
			selectors.push({ "selector":"#userName", "message":"Please enter user name.", "regex":"", "regexMsg":"" });
			selectors.push({ "selector":"#serverPassword", "message":"Please enter password.", "regex":"", "regexMsg":"" });
			selectors.push({ "selector":"#clientDbDetailsEndPoint", "message":"Please enter end point.", "regex":"", "regexMsg":"" });
			
			status = common.validateWithCustomMessages(selectors);
			
			return status;
		},
		
}

if($(".serverConfiguration-page").length){
	//events
	serverConfiguration.initialPage();
	$(".editServerConfig").on("click", function(){ 
		$("#id").val($(this).val());
		$("#serverConfigurationsForm").prop("action", $("#edit").val());
		this.form.submit();
		showAjaxLoader(true);
	});
	
	
	$(".saveServerConfigDetails").on("click", function(){
		var status = true;
		status = serverConfiguration.validateForm();
		
		if(!status){
			return false;
		}
		
		$("#serverConfigurationsForm").prop("action", $("#save").val());
		this.form.submit();
		showAjaxLoader(true);
	});
	
	$(".testConnection").on("click", function(){
		var status = true;
		status = serverConfiguration.validateForm();
		
		if(!status){
			return false;
		}
		
		$("#serverConfigurationsForm").prop("action", $("#testConnection").val());
		this.form.submit();
		showAjaxLoader(true);
	})
	
}

