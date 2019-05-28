var headers = {};
var vertical = {
		initialPage : function(){
			$("#existingVerticalsTable").DataTable({
				"order": [[ 0, "desc" ]],"language": {
	                "url": selectedLocalePath
	            }
			});
			$("#existingKpisTable").DataTable({
				"order": [[ 0, "asc" ]],"language": {
	                "url": selectedLocalePath
	            }
			});
			
			$("#verticals").multipleSelect({
				filter : true,
				placeholder : globalMessage['anvizent.package.label.selectVerticals'],
			    enableCaseInsensitiveFiltering: true
			});
			
			$(".databases").multipleSelect({
				filter : true,
				placeholder : globalMessage['anvizent.package.label.selectDatabases'],
			    enableCaseInsensitiveFiltering: true
			});
			
			$(".clients").multipleSelect({
				filter : true,
				placeholder : globalMessage['anvizent.package.label.selectClients'],
			    enableCaseInsensitiveFiltering: true
			});

			$("#clientId").select2({               
                allowClear: true,
                theme: "classic"
			}); 
			
			setTimeout(function() { $("#pageErrors").hide(); }, 5000);
		},			
		updateVerticalDetailsPanel : function(vertical){
			$(".verticalId").val(vertical.id);
			$(".editVerticalName").val(vertical.name);
			$(".editVerticalDescription").val(vertical.description);			
			if(vertical.isActive){
				$("input[name='isActive'][value='true']").prop("checked",true);				
			}else{
				$("input[name='isActive'][value='false']").prop("checked",true);				
			}			
			$("#editVerticalPanel").modal("show");
		},
		updateKpiDetailsPanel : function(kpi){					
			$(".kpiId").val(kpi.kpiId);
			$(".editKpiName").val(kpi.kpiName);
			$(".editKpiDescription").val(kpi.kpiDescription);
			if(kpi.isActive){
				$("input[name='isActive'][value='true']").prop("checked",true);				
			}else{
				$("input[name='isActive'][value='false']").prop("checked",true);				
			}			
			$("#editKpiPanel").modal("show");
		},
		 
};

if($(".vertical-page").length || $(".kpi-page").length || $(".clientVerticalMapping-page").length || $(".clientDatabaseMapping-page").length){
	vertical.initialPage();
	
	
	
	$("#createVertical").on("click",function(){
		common.clearValidations([".verticalName, .verticalDescription"]);
		$(".verticalName, .verticalDescription").val("");
		$("#createNewVerticalPanel").modal("show");
	});
	
	$("#createNewVertical").on("click",function(){
		var selectors = [];	
		var regexMsg = globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsdotsandalphabets'];
		var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/;
		
		selectors.push({"selector":".verticalName","message":globalMessage['anvizent.package.label.pleaseEnterVerticalName'], 
				"regex":regex, "regexMsg":regexMsg});
		selectors.push({"selector":".verticalDescription","message":globalMessage['anvizent.package.label.pleaseEnterVerticalDescription'], 
			"regex":"", "regexMsg":""});

		var status = common.validateWithCustomMessages(selectors);	
		if(!status){
			return false;
		}else{
			showAjaxLoader(true);
			$("#createNewVerticalForm").submit();
		}
				
	});
		
	$(document).on("click",".editVertical",function(){
		common.clearValidations([".editVerticalName, .editVerticalDescription"]);
		var verticalId = $(this).attr("data-verticalId");		
		var userId = $("#userId").val();
		var selectedData = {
			id : verticalId
		}
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url_getVerticalDetailsById = "/app_Admin/user/"+userId+"/etlAdmin/getVerticalDetailsById";
		showAjaxLoader(true); 				
 		var myAjax = common.postAjaxCall(url_getVerticalDetailsById,'POST',selectedData,headers);
 	    myAjax.done(function(result) {
	 	    	showAjaxLoader(false); 
	 	    	if(result.hasMessages){		    			  
	    			if(result.messages[0].code == "ERROR") {		    				  
						var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
						$(".message-class").empty().append(message).show();
						setTimeout(function() { $(".message-class").hide(); }, 5000);
	    				return false;
	    			} 
	    			if(result.messages[0].code == "SUCCESS") {		    				
	    				vertical.updateVerticalDetailsPanel(result.object);		    				
	    			}		    			  		    			  	
		    	}
 	    });
	});
	
	$("#updateVertical").on("click",function(){
		 
		var selectors = [];	
		var regexMsg = globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsdotsandalphabets'];
		var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/;
		
		selectors.push({"selector":".editVerticalName","message":globalMessage['anvizent.package.label.pleaseEnterVerticalName'], 
				"regex":regex, "regexMsg":regexMsg});
		selectors.push({"selector":".editVerticalDescription","message":globalMessage['anvizent.package.label.pleaseEnterVerticalDescription'], 
			"regex":regex, "regexMsg":regexMsg});
		
		var status = common.validateWithCustomMessages(selectors); 		
		if(!status){
			return false;
		}
		showAjaxLoader(true);
		$("#editVerticalForm").submit();
	});
	
	$(document).on("click",".deleteVertical",function(){
		var verticalId = $(this).attr("data-verticalId");
		$("#confirmDeleteVertical").val(verticalId);
		$("#deleteVerticalAlert").modal("show")
	});
	
	
	/////////////////////////////////*********KPI*********///////////////////////////////////////
	
	$("#createKpi").on("click", function(){
		common.clearValidations([".kpiName, .kpiDescription"]);
		$(".kpiName, .kpiDescription").val("");
		$("#createNewKpiPanel").modal("show");
	});
	
	$("#createNewKpi").on("click",function(){
		var selectors = [];
		var regexMsg = globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsdotsandalphabets'];
		var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/;
		selectors.push({"selector":".kpiName","message":globalMessage['anvizent.package.label.pleaseEnterKpiName'],"regex":regex,"regexMsg":regexMsg});
		selectors.push({"selector":".kpiDescription","message":globalMessage['anvizent.package.label.pleaseEnterKpiDescription'],"regex":"","regexMsg":""});
		var status = common.validateWithCustomMessages(selectors);	
		if(!status){
			return false;
		}
		showAjaxLoader(true);
		$("#createNewKpiForm").submit();		
	});
	
	$(document).on("click", ".editKpi", function(){
		common.clearValidations([".editKpiName, .editKpiDescription"]);
		var kpiId = $(this).attr("data-kpiid");		
		var userId = $("#userId").val();
		var selectedData = {
			kpiId : kpiId
		}
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url_getKpiDetailsById = "/app_Admin/user/"+userId+"/etlAdmin/getKpiDetailsById";
		showAjaxLoader(true); 				
 		var myAjax = common.postAjaxCall(url_getKpiDetailsById,'POST',selectedData,headers);
 	    myAjax.done(function(result) {
	 	    	showAjaxLoader(false); 
	 	    	if(result.hasMessages){		    			  
	    			if(result.messages[0].code == "ERROR") {		    				  
						var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
						$(".message-class").empty().append(message).show();
						setTimeout(function() { $(".message-class").hide(); }, 5000);
	    				return false;
	    			} 
	    			if(result.messages[0].code == "SUCCESS") {		    				
	    				vertical.updateKpiDetailsPanel(result.object);		    				
	    			}		    			  		    			  	
		    	}
 	    });
	});
	
	$("#updateKpi").on("click",function(){
		var selectors = [];
		var regexMsg = globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsdotsandalphabets'];
		var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/;
		selectors.push({"selector":".editKpiName","message":globalMessage['anvizent.package.label.pleaseEnterKpiName'],"regex":regex,"regexMsg":regexMsg});
		selectors.push({"selector":".editKpiDescription","message":globalMessage['anvizent.package.label.pleaseEnterKpiDescription'],"regex":regex,"regexMsg":regexMsg});
		var status = common.validateWithCustomMessages(selectors);	
		
		if(!status){
			return false;
		}
		showAjaxLoader(true);
		$("#editKpiForm").submit();
	});
	
	
	/////////////////////////////////*********CLIENT VERTICAL MAPPING*********///////////////////////////////////////
	
	$(document).on("change","#clientId",function(){
		showAjaxLoader(true);
		this.form.submit();
	});
	
	if ( $("#clientId").length > 0 ) {
		if ( $("#clientId").children().length > 1 &&  $("#clientId").val() == '0') {
			$("#clientId").val($("#clientId option").eq(1).val())
			$("#clientId").change();
		}
	}
	
	$(document).on("click","#saveVertcalMapping",function(){ 

		if($("#verticals option:selected").length == 0){
			common.showcustommsg("#verticals", globalMessage['anvizent.package.label.pleaseChooseAtLeastOneVertical'], "#verticals")
			return false;
		}
		
		$("#clientVerticalMappingForm").prop("action",$("#saveUrl").val()); 
		$("#clientVerticalMappingForm").submit();
	    showAjaxLoader(true);
	});
	
/////////////////////////////////*********CLIENT DATABASE MAPPING*********///////////////////////////////////////
	
	$(document).on("change","#clientId",function(){
		$("#databaseId").val('0');
		$("#pageMode").val("databases");
		showAjaxLoader(true);
		this.form.submit();
	});
	
	$(document).on("change","#databaseId",function(){
		$("#clientId").val('0');
		$("#pageMode").val("clients");
		showAjaxLoader(true);
		this.form.submit();
	});
	
	$(document).on("click","#save",function(){
		
		var ref_this = $("ul.nav-tabs li.active a").attr("href");
		var regex = /^[0-9a-zA-Z/ /_/,/./-]+$/;
		
		
		if($(".databases option:selected").length == 0 && ref_this == "#clientDatabaseMapping"){
			common.showcustommsg("#databases", globalMessage['anvizent.package.label.pleaseChooseAtLeastOneDatabse'], "#databases")
			return false;
		}
		else if($(".clients option:selected").length == 0 && ref_this == "#databaseClientMapping"){
			common.showcustommsg("#clients", globalMessage['anvizent.package.label.pleaseChooseAtLeastOneClient'], "#clients")
			return false;
		}
		if(ref_this == "#clientDatabaseMapping"){
			$("#pageMode").val("databases");
		}else{
			$("#pageMode").val("clients");
		}
		
		if(!regex.test(".editVerticalName")){
   		  common.showcustommsg(".editVerticalName", globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'],".editVerticalName");
    	}
		
		$("#clientDatabaseMappingForm").prop("action",$("#saveUrl").val()); 
		$("#clientDatabaseMappingForm").submit();
	    showAjaxLoader(true);
	});
}