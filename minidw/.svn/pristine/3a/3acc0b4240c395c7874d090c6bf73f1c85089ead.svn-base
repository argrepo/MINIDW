var templateMigration = {
		initialPage : function(){
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
			/*$("#clientId,#userId").select2({               
                allowClear: true,
                theme: "classic"
			}); */
		},
		
}
if($('.templateMigration-page').length){
	templateMigration.initialPage();
	
	 $(document).on("click","input:radio[name=templateMigrateExportOption]",function(){
		 var templateMigrateExportOption = $(this).val();
			if(templateMigrateExportOption == 'migrate'){
				 $("#clientIdDiv,#userIdDiv").show();
			}else{
				$("#userIdDiv").hide();
			} 
		});
	
		$(document).on("change","#clientId",function(){
			$("#templateMigration").prop("action",$("#editUrl").val());
			showAjaxLoader(true);
			this.form.submit();
		});
		
		$(document).on("change","#userId",function(){
			$("#templateMigration").prop("action",$("#packageListUrl").val());
			showAjaxLoader(true);
			this.form.submit();
		});
		
		$(document).on("click","#save",function(){
			$("#templateMigration").prop("action",$("#saveUrl").val());
			showAjaxLoader(true);
			this.form.submit();
		});
}