var headers = {};
var  internalization = {
		initialPage : function() {
			$("#internalizationTbl").dataTable();
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
		},
		loadLocaleMappingList: function () {
			var userID = $("#userID").val();
			var internalizationTbl$ = $("#internalizationTbl tbody").empty();
			showAjaxLoader(true);
			var url_loadLocaleMappingList = "/app_Admin/user/"+userID+"/etlAdmin/loadLocaleMappingList";
			   var myAjax = common.postAjaxCallObject(url_loadLocaleMappingList,'GET', null ,headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;	
		    				  }
			    			  if(result.messages[0].code=="SUCCESS"){
			    				  var list = result.object;
			    				  $.each(list,function(i,data){
			    					  var tr$ = $("<tr>");
			    					  tr$.append($("<td>").text(data.id));
			    					  tr$.append($("<td>").text(data.locale));
			    					  tr$.append($("<td>").text(data.localeName));
			    					  tr$.append($("<td>").append($("<button>",{type:'button',class:'btn btn-primary btn-sm editDetails',value:data.id}).append($("<span>",{class:'glyphicon glyphicon-edit',title:'Edit'}))));
			    					  internalizationTbl$.append(tr$);
			    				  })
			    			  }
			    		  }
					}
				});
		},
		
		getLocaleList:function(){
			var userID = $("#userID").val();
			var url_getLocaleNamesList = "/app_Admin/user/"+userID+"/etlAdmin/getLocaleNamesList";
			   var myAjax = common.postAjaxCallObject(url_getLocaleNamesList,'GET', "" ,headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;	
		    				  }
			    			  if(result.messages[0].code=="SUCCESS"){
			    				  var localeList = result.object;
			    				  var select$ = $("<select/>",{class:"languageName"}).append($('<option>').val('0').text('Select Language'));
			    				  $.each(localeList,function(key,value){
			    					  $("<option/>",{value:key,text:value}).appendTo(select$);
			    				  });
			    				  select$.appendTo(".localeList");
			    			  }
			    		  }
					}
				});
		},
		populateSavedProperties: function (properties, tableObj, savedProperties ) {
			$.each(properties,function(key,value){
				  var tr$ = $("<tr>");
				  tr$.append($("<td>").text(key).css('width','10px'));
				  tr$.append($("<td>").text(value));
				  tr$.append($("<td>").append($("<input>",{type:'text',class:"propValue",value:savedProperties[key],'data-propkey':key})));
				  tableObj.append(tr$);
			  })
		},
		
		populateProperties : function(properties,tableObj){
			$.each(properties,function(key,value){
				  var tr$ = $("<tr>");
				  tr$.append($("<td>").text(key).css('width','10px'));
				  tr$.append($("<td>").text(value));
				  tr$.append($("<td>").append($("<input>",{type:'text',class:"propValue",value:value,'data-propkey':key})));
				  tableObj.append(tr$);
			  })  
		}
		
}

var applicationProperties = {};
var serviceProperties = {};
if($('.internalization-page').length){
	internalization.initialPage();
	internalization.loadLocaleMappingList();
	internalization.getLocaleList();
	$("#pageInfo").show();
	$(document).on("click",".addInternalization",function(){
		$("#addInfo").show();
		$("#pageInfo").hide();
		$("#propertiesInfo").hide();
		$(".languageName").val("0");
	});
	
	 $("#proceedForMapping").on("click",function(){
		common.clearValidations([".languageName"])
		$("#addInfo").show();
		$("#pageInfo").hide();
		var userID = $("#userID").val();
		var languageName = $(".languageName option:selected").val(); 
		var languageDisplayName = $(".languageName option:selected").text(); 
		if(languageName != '0'){
			$("#serviceLanguage").text(languageDisplayName);
			$("#proLanguage").text(languageDisplayName);
			var packageProTable$ = $("#packageProTable tbody").empty();
			var serviceProTable$ = $("#serviceProTable tbody").empty();
			if($.isEmptyObject(applicationProperties)){

				var url_getDefaultQuery = "/app_Admin/user/"+userID+"/etlAdmin/getPropertyKeyValueFromminidw";
				   var myAjax = common.postAjaxCallObject(url_getDefaultQuery,'GET',"",headers);
					myAjax.done(function(result) {
						showAjaxLoader(false);
						if(result.object != null){
							console.log("result -- > " , result);
			    				  if(result.messages[0].code=="ERROR"){
			    					  common.showErrorAlert(result.messages[0].text);
			    					  var message = result.messages[0].text;		    					  
				    			  }
			    				  if(result.messages[0].code=="SUCCESS"){
			    					  $("#propertiesInfo").show();
			    					  applicationProperties = result.object;
			    					  internalization.populateProperties(applicationProperties,packageProTable$);
		    					  }
						}
				});
			} else {
				// call created function with packageProperties and packageProTable$
				internalization.populateProperties(applicationProperties,packageProTable$);
			}
			
			if($.isEmptyObject(serviceProperties)){
				var url_getDefaultQuery = "/app_Admin/user/"+userID+"/etlAdmin/getPropertyKeyValueFromminidwWebService";
				   var myAjax = common.postAjaxCallObject(url_getDefaultQuery,'GET',"",headers);
					myAjax.done(function(result) {
						showAjaxLoader(false);
						if(result != null){
				    		  if(result.hasMessages){
			    				  if(result.messages[0].code=="ERROR"){
			    					  common.showErrorAlert(result.messages[0].text);
			    					  var message = result.messages[0].text;		    					  
				    			  }
			    				  if(result.messages[0].code=="SUCCESS"){		    				
			    					  serviceProperties = result.object;
			    					  internalization.populateProperties(serviceProperties,serviceProTable$);	  
				    			  }
				    		  }
						}
				});
			}else{
				internalization.populateProperties(serviceProperties,serviceProTable$);
			}
			
		}else{
			common.showcustommsg(".languageName", "Please choose Language",".languageName");
  	    	return false;
		}
		
	});
	$("#savePropreties").on("click",function(){
		common.clearValidations([".propValue,#errorMessage"])
		var localeName = $(".languageName option:selected").val(); 
		var localeDisplayName = $(".languageName option:selected").text();
		var validStatus = true;
		var userID = $("#userID").val();
		var id = $("#idValue").val();
		if (!id) {
			id = 0;
		}
		var properties = {}﻿;
		﻿
		var appProperties = {};
		$("#packageProTable tbody tr").each(function(){
			appProperties[$(this).find("input.propValue").data("propkey")] = $(this).find("input.propValue").val();
			 var propValue = $(this).find("input.propValue").val();
			 if(propValue == ""){
				$(this).find("input.propValue").focus();
				common.showcustommsg($(this).find("input.propValue"), "Please enter Value",$(this).find("input.propValue"));
				common.showcustommsg("#errorMessage", "Some fields are empty in application properties","#errorMessage");
				validStatus = false; 
			 }
        });
		
		 var serviceProper = {};
			$("#serviceProTable tbody tr").each(function(){
				serviceProper[$(this).find("input.propValue").data("propkey")] = $(this).find("input.propValue").val();
				var propValue = $(this).find("input.propValue").val();
				 if(propValue == ""){
					 $(this).find("input.propValue").focus();
					common.showcustommsg($(this).find("input.propValue"), "Please enter Value",$(this).find("input.propValue"));
					common.showcustommsg("#errorMessage", "Some fields are empty in service properties","#errorMessage");
					validStatus = false; 
				 }
	        });
			 
			 properties['appProperties']=appProperties;
			 properties['serviceProper']=serviceProper;
			 
		if(!validStatus){
			return 	validStatus;
		}else{
			var selectData = {
					id: id,
					locale : localeName,
					localeName : localeDisplayName,
					properties : JSON.stringify(properties)
			}
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
	 		showAjaxLoader(true);
			var url_savePropertiesKeyValuePairs = "/app_Admin/user/"+userID+"/etlAdmin/savePropertiesKeyValuePairs";
			   var myAjax = common.postAjaxCallObject(url_savePropertiesKeyValuePairs,'POST',selectData,headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){						
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  var message = result.messages[0].text;
		    					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
			    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
			    			  }
			    			  if(result.messages[0].code=="SUCCESS"){
			    				  var message = result.messages[0].text;
			    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>").show();
			    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);
			    				  $("#pageInfo").show();
			    				  $("#addInfo").hide();
			    				  $("#propertiesInfo").hide();
			    				  internalization.loadLocaleMappingList();
			    			  }
			    		  }
					}
				});
		}	 
	})
	
	$(document).on("click",".editDetails",function(){
		$("#addInfo").show();
		$("#pageInfo").hide();
		$("#propertiesInfo").hide();
		var userID = $("#userID").val();
		var id = $(this).val();
		$("#idValue").val(id);
		var selectData = {
				id : id,
		}
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
 		showAjaxLoader(true);
		var url_getKeyValuePairsById = "/app_Admin/user/"+userID+"/etlAdmin/getpropertiesKeyValuePairsById";
		   var myAjax = common.postAjaxCallObject(url_getKeyValuePairsById,'POST',selectData,headers);
			myAjax.done(function(result) {
				var packageProTable$ = $("#packageProTable tbody").empty();
				var serviceProTable$ = $("#serviceProTable tbody").empty();
				showAjaxLoader(false);
				if(result != null){		
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  var message = result.messages[0].text;
	    					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
		    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  console.log(result.object);
		    				  $("#addInfo").show();
		    				  $("#propertiesInfo").show();
		    				  var message = result.messages[0].text;
		    				  var locale = result.object.savedproperties.locale;
		    				  var localname = result.object.savedproperties.localeName;
		    				  var properties = result.object.savedproperties.properties;
		    				  $(".languageName").val(locale);
		    				  $("#serviceLanguage").text(localname);
		    				  $("#proLanguage").text(localname);
		    				  var applicationProperties = result.object.applicationProperties;
		    				  var servicePropertiesData = result.object.servicePropertiesData;
		    				  properties = $.parseJSON(properties);
		    				  
		    				  internalization.populateSavedProperties(applicationProperties,packageProTable$,properties.appProperties);
		    				  internalization.populateSavedProperties(servicePropertiesData,serviceProTable$,properties.serviceProper);
		    			  }
		    			  
		    		  }
				}
			});
	});
	$(document).on("click","#back",function(){
		$("#addInfo").hide();
		$("#pageInfo").show();
		$("#propertiesInfo").hide();	
	});
	//$('#fixedHeader').freezeHeader({ 'height': '300px' });
	$('#packageProTable').fixedHeaderTable({ footer: true, cloneHeadToFoot: true, fixedColumn: false });
	
}