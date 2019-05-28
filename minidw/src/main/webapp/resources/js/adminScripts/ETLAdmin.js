var ETLAdmin = {
		initialPage : function() {
		
		$("#clientId").select2({               
            allowClear: true,
            theme: "classic"
		});	
	}
}

if($('.ETLAdmin-page').length || $(".allMappingInfo-page").length){
	ETLAdmin.initialPage();
}