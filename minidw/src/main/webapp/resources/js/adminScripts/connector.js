var  connector = {
		initialPage : function() {
			$("#ConnectorTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
			console.log("in connector page");
			$("#connectorId").select2({               
                allowClear: true,
                theme: "classic"
			}); 
			
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);	 
		},
		connectorFormValidation : function(){
			    var connectorId =$("#connectorId").val();
	  	       	var connectorName=$("#connectorName").val();
	  	       	var selectors = [];
	  	       	var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/;
	  	      
		   	    selectors.push("#connectorName");
	  	        common.clearValidations(["#connectorId,#connectorName,.activeStatus"]);
	  	        var validStatus=true;
	  	        if(connectorId == 0){
	  	        	common.showcustommsg("#connectorId", globalMessage['anvizent.package.label.pleaseChooseDatabase'],"#connectorId");
		  	    	validStatus=false;
	  	        }
	      	    if(connectorName == '' ){
		  	    	common.showcustommsg("#connectorName", globalMessage['anvizent.package.label.pleaseEnterConnectorName'],"#connectorName");
		  	    	validStatus=false;
	      	    }else if(!regex.test(connectorName)){
	      	    	common.showcustommsg("#connectorName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#connectorName");
		      	      validStatus=false;
	      	    }
	      	    if(!$("input[name='isActive']").is(":checked")){
	      	    	common.showcustommsg(".activeStatus", globalMessage['anvizent.package.label.PleaseChooseActiveStatus'],".activeStatus");
		  	    	validStatus=false;
	      	    }
	      	    if ( validStatus ) {
	      	    	validStatus = common.validate(selectors);
	      	    }
	      	    return validStatus;
		 }
}
if($('.connector-page').length){
	connector.initialPage();
	
	 $(document).on('click',"#updateConnectorMaster", function() {
		 
			var status= connector.connectorFormValidation();
			console.log("status",status)
			if(!status){ 
			return false;
			}
			else{ 
				$("#connectorForm").prop("action",$("#updateUrl").val()); 
				this.form.submit();
				showAjaxLoader(true);
			}
	 });
	 $("#addConnector").on('click', function() {
		 
			var status= connector.connectorFormValidation();
			if(!status){
			return false;
			}
			else{
				
				$("#connectorForm").prop("action",$("#addUrl").val()); 
				this.form.submit();
			    showAjaxLoader(true);
			}
	 });
	  
	
}