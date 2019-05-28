var  versionUpgrade = {
		initialPage : function() {
			$("#versionUpgradeTable").dataTable();
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);	 
		},
		  validURL: function(url) { 
			  var r = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/;
			  return r.test(url);
		  },
		  
		versionUpgradeFormValidation : function(){
	  	       	var versionNumber = $("#versionNumber").val();
	  	        var description = $("#description").val();
	  	        var filePath = $("#filePath").val();
	  	        common.clearValidations(["#versionNumber, #description,#filePath"]);
	  	        var validStatus=true;
	      	    if(versionNumber == 0 ){
		  	    	common.showcustommsg("#versionNumber", globalMessage['anvizent.package.label.pleaseEnterVersionNumber'],"#versionNumber");
		  	    	validStatus=false;
	      	    }else if(!versionNumber.match(/^[0-9.]+$/)){
	      	    	common.showcustommsg("#versionNumber", "Version number should allow numbers and dot","#versionNumber");
	      	    	validStatus=false;
	      	    }else if(versionNumber.substring(versionNumber.length-1) == "."){
	      	    	common.showcustommsg("#versionNumber", "Version number should should not ends with dot","#versionNumber");
	      	    	validStatus=false;
	      	    }
	      	    if(description == ''){
		  	    	common.showcustommsg("#description", globalMessage['anvizent.package.label.PleaseChoosedescription'],"#description");
		  	    	validStatus=false;
	      	    }
		      	if(filePath == ''){
		  	    	common.showcustommsg("#filePath", globalMessage['anvizent.package.label.PleaseEnterFilePath'],"#filePath");
		  	    	validStatus=false;
	      	    }
		      	if(!versionUpgrade.validURL(filePath) ){
		      		common.showcustommsg("#filePath", globalMessage['anvizent.package.message.invalidurl'],"#filePath");
		  	    	validStatus=false;
		      	}else if(!filePath.endsWith(".war")){
		      		common.showcustommsg("#filePath", "url should ends with .war","#filePath");
		  	    	validStatus=false;
		      	}
	      	    return validStatus;
		 }
}
if($('.versionUpgrade-page').length){
	versionUpgrade.initialPage();
	 $("#addVersionUpgrade,#updateVersionUpgrade").on('click',function() {
			var status= versionUpgrade.versionUpgradeFormValidation();
			if(!status){
				return false;
			}
			else{
				$("#versionUpgrade").prop("action",$("#addUrl").val()); 
				this.form.submit();
			    showAjaxLoader(true);
			}
	 });
	  
	
}