var  clientJobExecutionParameters = {
		initialPage : function() {
			$("#sourceTimeZone,#destTimeZone").select2({               
                allowClear: true,
                theme: "classic"
			});
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);	 
		},
		

	validateClientJobExecParamsCreationForm : function(){
		var sourceTimeZone =$("#sourceTimeZone").val();
     	var destTimeZone=$("#destTimeZone").val();
     	var nullReplaceValues=$("#nullReplaceValues").val();
     	var caseSensitive = $("input[name='caseSensitive']").is(":checked");
     	var interval=$("#interval").val();
     	
     	
        common.clearValidations(["#sourceTimeZone","#destTimeZone",".nullReplaceValues",".activeStatus","#interval"]);
        var validStatus=true;
      
	    if(sourceTimeZone == '0' ){
	    	common.showcustommsg("#sourceTimeZone", "Please select source time zone","#sourceTimeZone");
	    	validStatus=false;
	    }
	    
	    if(destTimeZone == '0' ){
	    	common.showcustommsg("#destTimeZone", "Please select source time zone","#destTimeZone");
	    	validStatus=false;
	    }
	    if(nullReplaceValues == '' ){
	    	common.showcustommsg("#nullReplaceValues", "Please enter null replace values","#nullReplaceValues");
	    	validStatus=false;
	    }
	    if(!caseSensitive){
	    	common.showcustommsg(".activeStatus", "Please choose case sensitive",".activeStatus");
	    	validStatus=false;
	    }
	    
	    if(interval == '0' ){
	    	common.showcustommsg("#interval", "Please select interval time","#interval");
	    	validStatus=false;
	    }
	    return validStatus;
}};

if($('.clientJobExecutionParameters-page').length){
	clientJobExecutionParameters.initialPage();
	 
}

$(document).on("click","#updateClientJobExecutionParams",function(){
	var status = clientJobExecutionParameters.validateClientJobExecParamsCreationForm();
	if(!status){
		return false;
	}
	
	$("#clientJobExecutionParameters").prop("action", $("#submit").val());
	this.form.submit();
	showAjaxLoader(true);
});