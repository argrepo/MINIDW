var generalSettings = {
		initialPage : function(){
			$("#existingJars").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
		setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
		$("#clientId").select2({               
            allowClear: true,
            theme: "classic"
		});
		},
		validation : function(){
			var flatFile = $("#flatFile option:selected").val();
			var database = $("#database option:selected").val();
			var webService = $("#webService option:selected").val();
			var fileSize = $("#fileSize").val();
			var validStatus=true;
			if(flatFile == 0){
				common.showcustommsg("#flatFile", globalMessage['anvizent.package.label.pleaseChooseOption'], "#flatFile");
				validStatus = false;
			}
			if(database == 0){
				common.showcustommsg("#database", globalMessage['anvizent.package.label.pleaseChooseOption'], "#database");
				validStatus = false;
			}
			if(webService == 0){
				common.showcustommsg("#webService", globalMessage['anvizent.package.label.pleaseChooseOption'], "#webService");
				validStatus = false;
			}
			if(fileSize == ""){
				common.showcustommsg("#fileSize", globalMessage['anvizent.package.label.pleaseChooseFileSize'], "#fileSize");
				validStatus = false;
			}	
			return validStatus;
		}
}
if($('.generalSettings-page').length || $('.existingJars-page').length){
	generalSettings.initialPage();
	
	$(document).on("change","#clientId",function(){
		common.clearValidations(["#flatFile,#database,#webService,#fileSize"])
		$("#flatFile,#database,#webService,#fileSize").val("");
		this.form.submit();
		showAjaxLoader(true);
	});
	
	$("#saveSettings").on("click",function(){
		common.clearValidations(["#flatFile,#database,#webService,#fileSize"])
		var status = generalSettings.validation();
		if(!status){
			return false;
		}else{
			$("#generalSettings").prop("action",$("#save").val());
			this.form.submit();
			showAjaxLoader(true);
		}
	});

}