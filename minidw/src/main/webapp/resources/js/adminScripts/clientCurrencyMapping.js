var  clientCurrencyMapping = {
		initialPage : function() {
			$("#clientCurrencyMappingTable").dataTable();
			
				$("#currencyName,#basecurrencyCode,#accountingCurrencyCode").select2(); 
			
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 20000);	
			if($("#pageMode").val() == "add"){
				$("#clientId").select2({               
		            allowClear: true,
		            theme: "classic"
				});
			}
		},
		clientCurrencyMappingFormValidation : function(){
	  	       	var clientId = $("#clientId option:selected").val();
	  	       	var currencyType = $("input[name='currencyType']").is(":checked");
	  	       	var isActive = $("input[name='isActive']").is(":checked");
	  	       	var currencyName = $("#currencyName").val();
	  	    	var baseCurrency = $("#basecurrencyCode").val();
	  	    	var accountCurrency = $("#accountingCurrencyCode").val();
	  	        common.clearValidations(["#clientId, .currencyType,#currencyName,#basecurrencyCode,#accountingCurrencyCode,.activeStatus,.messageForCurrency"]);
	  	        var validStatus=true;
	      	    if(clientId == '0'){
		  	    	common.showcustommsg("#clientId", "Please choose client id","#clientId");
		  	    	validStatus=false;
	      	    }
	      	    if(!currencyType){
		  	    	common.showcustommsg(".currencyType", "Please choose Currency type",".currencyType");
		  	    	validStatus=false;
	      	    }
	      	    if(!isActive){
	      	    	common.showcustommsg(".activeStatus", globalMessage['anvizent.package.label.PleaseChooseActiveStatus'],".activeStatus");
		  	    	validStatus=false;
	      	    }
		  	    	
	      	    if(currencyName == '' && baseCurrency == '' && accountCurrency == ''){
	  	    		common.showcustommsg(".messageForCurrency","Please choose atleast one currency",".messageForCurrency");
		  	    	validStatus=false;
	  	    	}
		  	    
	      	    return validStatus;
		 }
}
if($('.clientCurrencyMapping-page').length){
	clientCurrencyMapping.initialPage();
	
	 $("#addclientCurrency,#updateClientCurrency").on('click',function() {
		 
			var status= clientCurrencyMapping.clientCurrencyMappingFormValidation();
			if(!status){
				return false;
			}
			else{
				$("#clientCurrencyMapping").prop("action",$("#addUrl").val()); 
				this.form.submit();
			    showAjaxLoader(true);
			}
	 });
	
}