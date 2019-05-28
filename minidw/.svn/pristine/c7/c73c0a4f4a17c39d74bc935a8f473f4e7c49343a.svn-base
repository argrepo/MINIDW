var  middleLevelManager = {
		initialPage : function() {
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);	 
		},
		

	validatemiddleLevelManagerCreationForm : function(){
    var contextPath =$("#contextPath").val();
     	var uploadListEndPoint=$("#uploadListEndPoint").val();
     	var writeEndPoint=$("#writeEndPoint").val();
     	var deleteEndPoint=$("#deleteEndPoint").val();
     	var upgradeEndPoint=$("#upgradeEndPoint").val();
     	var userAuthToken=$("#userAuthToken").val();
     	var clientAuthToken=$("#clientAuthToken").val();
     	var encryptionPrivateKey=$("#encryptionPrivateKey").val();
     	var encryptionIV=$("#encryptionIV").val();
     	
        common.clearValidations(["#contextPath","#uploadListEndPoint",".writeEndPoint","#deleteEndPoint","#upgradeEndPoint","#userAuthToken","#clientAuthToken","#encryptionPrivateKey","#encryptionIV"]);
        var validStatus=true;
      
	    if(contextPath == '' ){
	    	common.showcustommsg("#contextPath", globalMessage['anvizent.package.label.contextPath'],"#contextPath");
	    	validStatus=false;
	    }else if(!common.validURL(contextPath)){
	    	 common.showcustommsg("#contextPath", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#contextPath");
  	         validStatus=false;
	    }
	    
	    if(uploadListEndPoint == '' ){
	    	common.showcustommsg("#uploadListEndPoint", globalMessage['anvizent.package.label.uploadListEndPoint'],"#uploadListEndPoint");
	    	validStatus=false;
	    }
	    if(writeEndPoint == '' ){
	    	common.showcustommsg("#writeEndPoint", globalMessage['anvizent.package.label.writeEndPoint'],"#writeEndPoint");
	    	validStatus=false;
	    }
	    /*if(deleteEndPoint == '' ){
	    	common.showcustommsg("#deleteEndPoint", globalMessage['anvizent.package.label.deleteEndPoint'],"#deleteEndPoint");
	    	validStatus=false;
	    }
	    if(upgradeEndPoint == '' ){
	    	common.showcustommsg("#upgradeEndPoint", globalMessage['anvizent.package.label.upgradeEndPoint'],"#upgradeEndPoint");
	    	validStatus=false;
	    }*/
	    
	    if(userAuthToken == '' ){
	    	common.showcustommsg("#userAuthToken", globalMessage['anvizent.package.label.userAuthToken'],"#userAuthToken");
	    	validStatus=false;
	    } else if(!/^[a-zA-Z0-9_.-]*$/.test(userAuthToken)){
	    	common.showcustommsg("#userAuthToken", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#userAuthToken");
	    	validStatus=false;
	    }
	    
	    if(clientAuthToken == '' ){
	    	common.showcustommsg("#clientAuthToken", globalMessage['anvizent.package.label.clientAuthToken'],"#clientAuthToken");
	    	validStatus=false;
	    }else if(!/^[a-zA-Z0-9_.-]*$/.test(clientAuthToken)){
	    	common.showcustommsg("#clientAuthToken", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#clientAuthToken");
	    	validStatus=false;
	    }
	    
	    if(encryptionPrivateKey == '' ){
	    	common.showcustommsg("#encryptionPrivateKey", globalMessage['anvizent.package.label.encryptionPrivateKey'],"#encryptionPrivateKey");
	    	validStatus=false;
	    }
	    
	    if(encryptionIV == '' ){
	    	common.showcustommsg("#encryptionIV", globalMessage['anvizent.package.label.encryptionIV'],"#encryptionIV");
	    	validStatus=false;
	    }else if(!/^[0-9a-zA-Z/ -/_/,/./]+$/.test(encryptionIV)){
	    	common.showcustommsg("#encryptionIV", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],"#encryptionIV");
	    	validStatus=false;
	    }
	    return validStatus;
}};

if($('.middleLevelManager-page').length){
	middleLevelManager.initialPage();
	 
}

$(document).on("click","#updateMiddleLevelManager",function(){
	var status = middleLevelManager.validatemiddleLevelManagerCreationForm();
	if(!status){
		return false;
	}
	
	$("#middleLevelManager").prop("action", $("#submit").val());
	this.form.submit();
	showAjaxLoader(true);
});