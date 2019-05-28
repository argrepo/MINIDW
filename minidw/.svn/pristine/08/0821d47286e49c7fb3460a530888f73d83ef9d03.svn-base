var allMappingInfo = {
		initialPage : function(){
			if ( $("input[name='verticals']").length ) {
				$($("input[name='verticals']").get(0)).prop("checked",false).trigger("click");
			}
			if($(".skipMapping").length + $(".skip").length == 6 ){
				$(".saveMapping").prop("disabled",true);
			}
		},
		
};

if($(".allMappingInfo-page").length || $(".defaultMappingInfo-page").length){
	allMappingInfo.initialPage();
    
	$(".defaultMappings").on("click",function(e){
		e.preventDefault();
		
		var params = "";
	 	var ua = window.navigator.userAgent;
	    var msie = ua.indexOf("MSIE ");
	    
	    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))  // If Internet Explorer, return version number
	    {
	    	params = [
		              //'height='+screen.height,
		              'width='+screen.width,
		              'fullscreen=no' // only works in IE, but here for completeness
		          ].join(',');
	    } else {
	    	params = [
		              'height='+screen.height,
		              'width='+screen.width,
		          ].join(',');
	    }
		
	    var popup = window.open($(this).prop("href"), 'defaultMappingScreen', params); 
	    popup.moveTo(0,0);
	    popup.document.title = "Default Mapping Details";
        if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
      	  	popup.addEventListener (
        	        "load",
        	        function () {
        	            var destDoc = popup.document;
        	            destDoc.title = "Default Mapping Details";
        	        },
        	        false
	       );
        }
        
        var timer = setInterval(checkChild, 500);
        
        function checkChild() {
        	if (popup.closed) {
        		clearInterval(timer);
        		$("#clientId").trigger("change"); 
        	}
        }
	});
	
	$(".saveMapping").on("click",function(e){
		 
		common.clearValidations(["#verticalValidation, #connectorValidation, #dLValidation, #tableScriptValidation, #webServicesValidation"]);
		var validate = true;
		if($("input[name='verticals']").length && $("input[name='verticals']:checked").length == 0 && !$("input[name='skipVerticals']").is(":checked")){
			common.showcustommsg("#verticalValidation", "Please choose atleast one vertical.", "#verticalValidation");
			validate = false;
		}
		
		if($("input[name='connectors']").length && $("input[name='connectors']:checked").length == 0 && !$("input[name='skipConnectors']").is(":checked")){
			common.showcustommsg("#connectorValidation", "Please choose atleast one connector.", "#connectorValidation");
			validate = false;
		}
		
		if($(".dLs").length && $(".dLs:checked").length == 0 && !$("input[name='skipDLs']").is(":checked")){
			common.showcustommsg("#dLValidation", "Please choose atleast one DL.", "#dLValidation");
			validate = false;
		}
		
		if($(".tablescript").length && $(".tablescript:checked").length == 0 && !$("input[name='skipTableScripts']").is(":checked")){
			common.showcustommsg("#tableScriptValidation", "Please choose atleast one table script.", "#tableScriptValidation");
			validate = false;
		}
		
		if($("input[name='webServices']").length && $("input[name='webServices']:checked").length == 0 && !($("input[name='skipWebServices']").is(":checked"))){
			common.showcustommsg("#webServicesValidation", "Please choose atleast one web servce.", "#webServicesValidation");
			validate = false;
		}
		
		if(!validate){
			return false;
		}
		$("#defaultMappingInfo").prop("action", $("#save").val());
		showAjaxLoader(true);
		this.form.submit();
	});
	
	$("#clientId").on("change",function(){
		this.form.submit();
		showAjaxLoader(true);
	}); 
	
	if ( $("#clientId").length > 0 ) {
		if ( $("#clientId").children().length > 1 &&  $("#clientId").val() == '0') {
			$("#clientId").val($("#clientId option").eq(1).val())
			$("#clientId").change();
		}
	}
	
	$("#reload").on("click",function(){
		$("#clientId").trigger("change"); 
	});
	
	$(document).on("click",".clientMapping",function(){
		$("#allMappingInfoForm").prop("action", $(this).attr("data-formaction"));
		$("#allMappingInfoForm").prop("id", $(this).attr("data-formid"));
		this.form.submit();
	    showAjaxLoader(true);
	});
	
	$(document).on("click",".check",function(){
		if($(".check").length == $(".check:checked").length){
			$(".saveMapping").prop("disabled",true);
		}else{
			$(".saveMapping").prop("disabled",false);
		}
	});
	
	$(document).on("click","input[name='verticals']",function(){
		$(".dLs").prop({"disabled": true, "checked":false});
		$("input[name='verticals']").each(function(){
			var verticalId = $(this).val();
			if($(this).is(":checked")){
				$(".dLs").each(function(){
						if($(this).attr("data-industryid") == verticalId){
							$(this).prop("disabled",false);
							$(this).prop("checked",true);
						}
				});
			}
			else{
				$(".dLs").each(function(){
						if($(this).attr("data-industryid") == verticalId){
							$(this).prop("disabled",true);
							$(this).prop("checked",false);
						}
				});
			}
		});
		
		if($("input[name='verticals']").length == $("input[name='verticals']:checked").length){
			$(".selectAllVerticals").prop("checked",true);
		}
		else{
			$(".selectAllVerticals").prop("checked",false);
		}
	});
	
	if ( $("input[name='verticals']").length ) {
		$($("input[name='verticals']").get(0)).prop("checked",false).trigger("click");
	}
	
	$(document).on("click","input[name='selectAll']",function(){
		if($(this).is(":checked")){
			$(this).parents("table").find("input[type='checkbox']").prop("checked",true);
			if($(this).parents("table").attr("id") == "existingVerticalsTable")
				$($("input[name='verticals']").get(0)).prop("checked",false).trigger("click");
		}
		else{
			$(this).parents("table").find("input[type='checkbox']").prop("checked",false);
			if($(this).parents("table").attr("id") == "existingVerticalsTable")
				$($("input[name='verticals']").get(0)).prop("checked",true).trigger("click");
		}
	});
	
	$(document).on("click","input[name='connectors']",function(){
		if($("input[name='connectors']").length == $("input[name='connectors']:checked").length){
			$(".selectAllConnector").prop("checked",true);
		}
		else{
			$(".selectAllConnector").prop("checked",false);
		}
	});
	
	$(document).on("click",".dLs",function(){
		if($(".dLs").length == $(".dLs:checked").length){
			$(".selectAllDLs").prop("checked",true);
		}
		else{
			$(".selectAllDLs").prop("checked",false);
		}
	});
	
	$(document).on("click",".tablescript",function(){
		if($(".tablescript").length == $(".tablescript:checked").length){
			$(".selectAllScripts").prop("checked",true);
		}
		else{
			$(".selectAllScripts").prop("checked",false);
		}
	});
	
	$(document).on("click","input[name='webServices']",function(){
		if($("input[name='webServices']").length == $("input[name='webServices']:checked").length){
			$(".selectAllWs").prop("checked",true);
		}
		else{
			$(".selectAllWs").prop("checked",false);
		}
	});
	
	$(".back").on("click",function(){
		$("#defaultMappingInfo").prop("action",$(this).attr("data-formaction"));
		$("#defaultMappingInfo").prop("id",$(this).attr("data-formid"));
		this.form.submit();
		window.close();
	});
	
	$(document).on("change","#templateId",function(){
		$("#defaultMappingInfo").prop("action",$("#geturl").val());
		this.form.submit();
		showAjaxLoader(true);
	});
	
	$(document).on("click",".saveDefaultMapping",function(){
		debugger
		var userID = $("#userId").val();
		var clientID = $("#clientId").val();
		var tempId = $("#templateId").val();
		headers[header] = token;
		var url_getClientList = "/commonMappings2/saveDefault/"+tempId+"/"+clientID;
		   var myAjax = common.loadAjaxCall(url_getClientList,'GET', "" ,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;	
	    				  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  var bucketList = result.object;
		    				  var select$ = $("<select/>",{class:"bucket form-control"});
		    				  $.each(bucketList,function(key,value){
		    					  $("<option/>",{value:key,text:value}).appendTo(select$);
		    				  });
		    				  select$.appendTo(".bucketList");
		    				  select$.val($("#bucketId").val());
		    				  
		    			  }
		    		  }
				}
			});
	});
}