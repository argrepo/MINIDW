var  contextParameter = {
		initialPage : function() {
			$("#ConnectorTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);	 
		},
		contextParameterFormValidation : function(){
			    var paramName =$("#paramName").val();
	  	       	var paramval=$("#paramval").val();
	  	       	var description=$("#description").val();
	  	        common.clearValidations(["#paramName,#paramval,#description,.activeStatus"]);
	  	        
	  	        var validStatus=true;
	  	        
	  	        var selectors = [];
				
		        selectors.push('#paramName');
		        selectors.push('#paramval');
		        selectors.push('#description');
		        
				var valid = common.validate(selectors);
				
				if(!valid){
					validStatus=false;
					}
				
	  	       /*if(paramName == ''){
	  	        	common.showcustommsg("#paramName", globalMessage['anvizent.package.label.pleaseEnterParameterName'],"#paramName");
		  	    	validStatus=false;
	  	        }
	      	    if(paramval == '' ){
		  	    	common.showcustommsg("#paramval", globalMessage['anvizent.package.label.pleaseEnterParameterValue'],"#paramval");
		  	    	validStatus=false;
	      	    }
	      	    if(description == '' ){
		  	    	common.showcustommsg("#description", globalMessage['anvizent.package.label.pleaseEnterParameterDescription'],"#description");
		  	    	validStatus=false;
	      	    }*/
				
	      	    if(!$("input[name='isActive']").is(":checked")){
	      	    	common.showcustommsg(".activeStatus", globalMessage['anvizent.package.label.PleaseChooseActiveStatus'],".activeStatus");
		  	    	validStatus=false;
	      	    }
	      	    return validStatus;
		 }
}
if($('.contextParameters-page').length){
	contextParameter.initialPage();
	
	 $("#updateContextParameter").on('click', function() {
		 
			var status= contextParameter.contextParameterFormValidation();
			console.log("status",status)
			if(!status){ 
			return false;
			}
			else{ 
				$("#contextParameterForm").prop("action",$("#updateUrl").val()); 
				this.form.submit();
				showAjaxLoader(true);
			}
	 });
	 $("#addContextParameter").on('click', function() {
		 
			var status= contextParameter.contextParameterFormValidation();
			if(!status){
			return false;
			}
			else{
				
				$("#contextParameterForm").prop("action",$("#addUrl").val()); 
				this.form.submit();
			    showAjaxLoader(true);
			}
	 });
	  
	
}