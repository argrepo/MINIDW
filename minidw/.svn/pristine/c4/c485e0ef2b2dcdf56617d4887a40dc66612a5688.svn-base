var validatePreload = {
		initialPage : function(){
			
			$("#databaseConnectorId, #ilId, #validationTypeId").select2({               
                allowClear: true,
                theme: "classic"
			});
			
			$(".datavalidationContextParams").multipleSelect({
				filter : true,
				placeholder : 'Select Context Parameters',
			    enableCaseInsensitiveFiltering: true
			});
			
		},
		viewValidationScript : function(result){
			var scriptData = result;
			if(scriptData == '' || scriptData == null){
				scriptData = 'No Script Found.';
			}
			var params ="";
			var ua = window.navigator.userAgent;
		    var msie = ua.indexOf("MSIE ");
		    
		    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))  // If Internet Explorer, return version number
		    {
		    	params = [
			              'width='+screen.width,
			              'fullscreen=no' // only works in IE, but here for completeness
			          ].join(',');
		    } else {
		    	params = [
			              'height='+screen.height,
			              'width='+screen.width,
			          ].join(',');
		    }
	          popup = window.open('about:blank', '_blank', params); 
	          popup.moveTo(0,0);
	          popup.document.title = "Validation Script";
	          popup.document.body.innerHTML = "<pre>"+scriptData+"</pre>";
	          if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
	        	  popup.addEventListener (
		        	        "load",
		        	        function () {
		        	            var destDoc = popup.document;
		        	            destDoc.open ();
		        	            destDoc.title = "Script Layout";
		        	            destDoc.write ('<html><head></head><body><pre>'+scriptData+'</pre></body></html>');
		        	            destDoc.close ();
		        	        },
		        	        false
		        	    );
	          }

		},
		showMessage:function(text){
			$(".messageText").empty();
			$(".successMessageText").empty();
			$(".messageText").html(text);
		    $(".message").show();
		   setTimeout(function() { $(".message").hide(); }, 10000);
	 },
	 
	// Start DBvariables Div
 	
 	displayDatabaseSchemaBasedOnConnectorId : function(connector_id,connectionId,protocal){
 		$('#defualtVariableDbSchema').empty();
 		var userID = $("#userID").val();  
 		if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
 			$('#defualtVariableDbSchema').append('<div class="row form-group dbSchemaSelection" id="dbSchemaSelection" >' +
 				     '<div class="col-sm-2">'+globalMessage['anvizent.package.label.databaseandschema']+':</div>'+
 					'<div class="col-sm-2">' +
 						'<select class="form-control dbVariable"  id="dbVariable">'+
 						'<option value="{db0}">'+globalMessage['anvizent.package.label.selectdbvariable']+'</option>'+
 					'</select>'+
 					'</div>'+
 					'<div class="col-sm-2">'+
 						'<select class="form-control dbName" id="dbName">'+
 						'<option value="{dbName}">'+globalMessage['anvizent.package.label.selectdbname']+'</option>'+
 					'</select>'+
 					'</div>'+
 				 
 					'<div class="col-sm-2">'+
 						'<select class="form-control schemaVariable" id="schemaVariable">'+
 						'<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>'+
 					   '</select>'+
 					'</div>'+
 					'<div class="col-sm-2">'+
 						'<select  class="form-control schemaName" id="schemaName">'+
 						'<option value="{schemaName}">'+globalMessage['anvizent.package.label.selectschemaname']+'</option>'+
 					    '</select>'+
 					'</div>'+
 					
 					'<div class="col-sm-2">'+
 						'<button type="button" class="btn btn-primary btn-sm addDbSchema"> <i class="fa fa-plus" aria-hidden="true"></i> </button>'+
 					'</div>'+
 				'</div>'); 
 			
 			showAjaxLoader(true);
 			var url_getDbSchemaVaribles ="/app/user/"+userID+"/package/getDbSchemaVaribles/"+connector_id ; 
 			var token = $("meta[name='_csrf']").attr("content");
 			var header = $("meta[name='_csrf_header']").attr("content");
 			headers[header] = token;
 			 var myAjax = common.loadAjaxCall(url_getDbSchemaVaribles,'GET','',headers);
 			    myAjax.done(function(result) {
 			    	showAjaxLoader(false);
 			    	  if(result != null && result.hasMessages){ 
 			    			  if(result.messages[0].code == "ERROR") { 
 			    				  $("#defualtVariableDbSchema").hide();
 			    				  common.showErrorAlert(result.messages[0].text);
 			    				  return false;
 			    			  }  
 			    		   else if(result.messages[0].code == "SUCCESS"){
 			    			   var dbVariable='';
 			    			   var schemaVariable = '';
 			    			   if(result.object != null){
 			    				   $('#dbVariable,#schemaVariable').empty();
 			    				   dbVariable ='<option value="{db0}">'+globalMessage['anvizent.package.label.selectdbvariable']+'</option>'; 
 			    				   schemaVariable ='<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
 			    				   $.each( result.object, function( key, value ) {
 			    					   dbVariable += '<option value='+key+'>'+key+'</option>';
 			    					   schemaVariable += '<option value='+value+'>'+value+'</option>';
 				    				 });
 			    				   $('#dbVariable').append(dbVariable);
 			    				   $('#schemaVariable').append(schemaVariable);
 			    				  validatePreload.getAllSchemasFromDatabase(connector_id,connectionId,protocal) ;
 			    			   }else{
 			    				   common.showErrorAlert(result.messages[0].text);
 			    				   return false;
 			    			   }
 			    			 
 			    			  } 
 			    		    
 			    	  } else{
 				    		var messages = [ {
 				    			code : globalMessage['anvizent.message.error.code'],
 				    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
 				    		} ];
 				    		common.displayMessages(messages);
 				    	}   	  
 			    }); 
 			   
 			}
 		 
 		 else if(protocal.indexOf('mysql') != -1 || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1   || protocal.indexOf('sforce') != -1 || protocal.indexOf('as400') != -1  ||  protocal.indexOf('postgresql') != -1    || protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1){

 				$('#defualtVariableDbSchema').append('<div class="row form-group dbSchemaSelection" id="dbSchemaSelection" >' +
 					    
 						'<div class="col-sm-2">'+globalMessage['anvizent.package.label.databaseandschema']+':</div>'+
 						'<div class="col-sm-2">'+
 							'<select class="form-control schemaVariable" id="schemaVariable">'+
 							'<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>'+
 						   '</select>'+
 						'</div>'+
 						'<div class="col-sm-2">'+
 						'<select class="form-control dbName" id="dbName">'+
 						'<option value="{dbName}">'+globalMessage['anvizent.package.label.selectschemaname']+'</option>'+
 					   '</select>'+
 					   '</div>'+
 						'<div class="col-sm-2">'+
 							'<button type="button" class="btn btn-primary btn-sm addDbSchema"> <i class="fa fa-plus" aria-hidden="true"></i> </button>'+
 						'</div>'+
 					'</div>'); 
 				var token = $("meta[name='_csrf']").attr("content");
 				var header = $("meta[name='_csrf_header']").attr("content");
 				headers[header] = token;
 				showAjaxLoader(true);
 				var url_getDbSchemaVaribles ="/app/user/"+userID+"/package/getDbSchemaVaribles/"+connector_id ; 
 				 var myAjax = common.loadAjaxCall(url_getDbSchemaVaribles,'GET','',headers);
 				    myAjax.done(function(result) {
 				    	showAjaxLoader(false);
 				    	  if(result != null && result.hasMessages){ 
 				    			  if(result.messages[0].code == "ERROR") { 
 				    				  $("#defualtVariableDbSchema").hide();
 				    				  common.showErrorAlert(result.messages[0].text);
 				    				  return false;
 				    			  }  
 				    		   else if(result.messages[0].code == "SUCCESS"){
 				    			   var schemaVariable = '';
 				    			   if(result.object != null){
 				    				   $('#schemaVariable').empty();
 				    				   schemaVariable ='<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
 				    				   $.each( result.object, function( key, value ) {
 				    					   schemaVariable += '<option value='+value+'>'+value+'</option>';
 					    				 });
 				    				   $('#schemaVariable').append(schemaVariable);
 				    				   
 				    				  validatePreload.getAllSchemasFromDatabase(connector_id,connectionId,protocal) ;
 				    				   
 				    			   }else{
 				    				   common.showErrorAlert(result.messages[0].text);
 				    				   return false;
 				    			   }
 				    			 
 				    			  } 
 				    	  } else{
 					    		var messages = [ {
 					    			code : globalMessage['anvizent.message.error.code'],
 					    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
 					    		} ];
 					    		common.displayMessages(messages);
 					    	}   	  
 				    }); 
 		       }
 		 $("#defualtVariableDbSchema").show();
 	},
 	
 	getAllSchemasFromDatabase: function(connector_id,connectionId,protocal){
 		var userID = $("#userID").val();
 		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
 		showAjaxLoader(true);
 		var url_getDbSchemaVaribles1 ="/app/user/"+userID+"/package/getAllSchemasFromDatabase/"+connectionId+""; 
 		 var myAjax = common.loadAjaxCall(url_getDbSchemaVaribles1,'GET','',headers);
 		    myAjax.done(function(dbResult) {
 		    	showAjaxLoader(false);
 		    	  if(dbResult != null){ 
 		    		 
 		    		  if(dbResult.hasMessages){
 		    			  if(dbResult.messages[0].code == "ERROR") { 
 		    				  common.showErrorAlert(dbResult.messages[0].text);
 		    				  return false;
 		    			  }  
 		    		   else if(dbResult.messages[0].code == "SUCCESS"){
 		    			   
 		    			   var dbName='';
 		    			   if(dbResult.object != null){
 		    				   $('#dbName').empty();
 		    				   if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
 		    					   dbName ='<option value="{dbName}">'+globalMessage['anvizent.package.label.selectdbname']+'</option>';
 		    				   }else if(protocal.indexOf('mysql') != -1 || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1   || protocal.indexOf('sforce') != -1 || protocal.indexOf('as400') != -1  ||  protocal.indexOf('postgresql') != -1    || protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1){
 		    					   dbName ='<option value="{dbName}">'+globalMessage['anvizent.package.label.selectschemaname']+'</option>';
 		    				   }
 		    				  
 		    				   $.each( dbResult.object, function( key, value ) {
 		    					   dbName += '<option value='+value+'>'+value+'</option>';
 			    				 });
 		    				  
 		    				   $('#dbName').append(dbName);
 		    			  
 		    			   }else{
 		    				   common.showErrorAlert(result.messages[0].text);
 		    				   return false;
 		    			   }
 		    			 
 		    			  } 
 		    		  }	  
 		    	  }    	  
 		    }); 
 	   },
 	   
 		 getData : function(){
	  		var dataset=[];
	  		$('#defualtVariableDbSchema').find('.dbSchemaSelection').each(function(i, obj) {
		    	var dbVariable=$(this).find(".dbVariable").val(); 
			    var dbName=$(this).find(".dbName").val();
			    var schemaVariable=$(this).find(".schemaVariable").val();
			    var schemaName=$(this).find(".schemaName").val();
			    common.clearValidations([$(obj).find('.dbVariable'), $(obj).find('.dbName'), $(obj).find('.schemaVariable'), $(obj).find('.schemaName')]);
			  var validStatus = true;
			  if(dbVariable == '' || dbVariable == '{db0}'){
				    common.showcustommsg($(obj).find('.dbVariable'),globalMessage['anvizent.package.message.pleasechoosedatabasevariable'],$(obj).find('.dbVariable'));
			    	validStatus =false;
			  }
			  if(dbName == '' || dbName == '{dbName}'){
				    common.showcustommsg($(obj).find('.dbName'),globalMessage['anvizent.package.message.pleasechoosedatabasename'],$(obj).find('.dbName'));
			    	validStatus =false;
			  }
			  if(schemaVariable == '' || schemaVariable == '{schema0}'){
				    common.showcustommsg($(obj).find('.schemaVariable'),globalMessage['anvizent.package.message.pleasechooseschemavariable'],$(obj).find('.schemaVariable'));
			    	validStatus =false;
			  }
			  if(schemaName == '' || schemaName == '{schemaName}'){
				    common.showcustommsg($(obj).find('.schemaName'),globalMessage['anvizent.package.message.pleasechooseschemaname'],$(obj).find('.schemaName'));
			    	validStatus =false;
			  }
			  
			  if(!validStatus){
				  return false;
			  }
			 
			  if(validStatus){
				  var obj = {
						  dbVariable: dbVariable,
						  dbValue: dbName,
						  schemaVariable: schemaVariable,
						  schemaValue : schemaName
						};
				  dataset.push(obj);
			   }
		    });
	  		return dataset;
	  	},
	  	
	  	validationCreationFormValidation : function(){
			 var validStatus=true;
			 var validationName = $("#validationName").val(),
			  validationScripts = $("#validationScripts").val(),
			  ilInfo =$("#ilId").val(),
			  validationType = $("#validationTypeId").val(),
			  connector =$("#databaseConnectorId").val();
			 common.clearValidations(['#validationName, #validationScripts, #isActive, #ilId, #databaseConnectorId, #preLoadValidationType']);
			 var validStatus=true;
			 
			 if(validationName == ''){
				 common.showcustommsg("#validationName", globalMessage['anvizent.package.label.pleaseEnterValidationName'], "#validationName");
				 validStatus = false;
			 }
			 if(validationScripts == ''){
				 common.showcustommsg("#validationScripts", globalMessage['anvizent.package.label.pleaseEnterValidationScripts'], "#validationScripts");
				 validStatus = false;
			 }
			 if(ilInfo == '0'){
				 common.showcustommsg("#ilId", globalMessage['anvizent.package.label.pleaseChooseil'], "#ilId");
				 validStatus = false;
			 }
			 if(connector == '0'){
				 common.showcustommsg("#databaseConnectorId", globalMessage['anvizent.package.label.pleaseChooseconnector'], "#databaseConnectorId");
				 validStatus = false;
			 }
			 if(validationType == "null" || validationType == ''){
				 common.showcustommsg("#validationTypeId", globalMessage['anvizent.package.label.pleaseChooseValidationtype'], "#validationTypeId");
				 validStatus = false;
			 }
			 return validStatus;
		},
}

if($('.validatePreload-page').length){
	validatePreload.initialPage();
	var connectionId=$('#connectionId').val();
	if(connectionId > 0) {
		var connectorId = $('#connectionId').find('option:selected').attr("data-connectorId");
	    var protocal = $('#connectionId').find('option:selected').data("protocal");
		validatePreload.displayDatabaseSchemaBasedOnConnectorId(connectorId,connectionId,protocal);
	}
	
	$("#connectionId").on("change",function(){
		showAjaxLoader(true);
		this.form.submit();
	});
	
    var table = $(".validationTbl").DataTable( { "order": [[1, "desc" ]],"language": {
        "url": selectedLocalePath
    }});
	
	$(document).on("click",".selectAll",function(){
		var isChecked = this.checked;
		$(this).parents('.table').find("input[type='checkbox']").each(function(){
			if (this.checked != isChecked ) {
				this.checked = !isChecked;
				$(this).trigger("click");
			}
		});
	});
	
	$(document).on('click', '#viewValidationscript', function(){
		var validationscript = $(this).attr('data-validationscript');
		
		 if(validationscript !=null || validationscript==''){
			 validatePreload.viewValidationScript(validationscript);
		 }else{
			 validatePreload.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		 }
	});
	
//db schmema
    
    $(document).on("click",".addDbSchema",function(){
  		var defualtVariableDbSchema = '';
  		
  		var connector_id = $('#connectionId').find('option:selected').attr("data-connectorId");
	    var protocal = $('#connectionId').find('option:selected').data("protocal");
	    
    		var dbVariable = $(this).parents("#dbSchemaSelection").find("#dbVariable > option").clone();
    		var dbname = $(this).parents("#dbSchemaSelection").find('#dbName option').clone();
    		var schemaVariable = $(this).parents("#dbSchemaSelection").find('#schemaVariable option').clone();
    		var schemaName = $(this).parents("#dbSchemaSelection").find('#schemaName option').clone();
         
    		if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
    			defualtVariableDbSchema += '<div class="row form-group dbSchemaSelection" id="dbSchemaSelection">'+'<div class="col-sm-2">'+'</div>'+
      		'<div class="col-sm-2">'+
      			'<select class="form-control dbVariable" id="dbVariable">'+
      			'<option value="{db0}">select Db Variable</option>';
        		  $.each( dbVariable, function( key, value ) {
        			  console.log(value.innerHTML)
        			 if(key != 0)
        			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
        		 });
        		
      		 
        		defualtVariableDbSchema += '</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      		'<select class="form-control dbName" id="dbName">'+
        		'<option value="{dbName}">'+globalMessage['anvizent.package.label.selectdbname']+'</option>';
      		  $.each( dbname, function( key, value ) {
      			  if(key != 0)
      			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
      		 });
      		 
        		defualtVariableDbSchema +='</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      		'<select class="form-control schemaVariable" id="schemaVariable">'+
        		'<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
        		 $.each( schemaVariable, function( key, value ) {
        			if(key != 0)
       			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
       		 });
       		 
        		defualtVariableDbSchema += '</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      		'<select class="form-control schemaName" id="schemaName">'+
        		'<option value="{dbName}">'+globalMessage['anvizent.package.label.selectschemaname']+'</option>';
      		  $.each( schemaName, function( key, value ) {
      			  if(key != 0)
      			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
      		 });
      		
      		 
      		defualtVariableDbSchema += '</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      			'<button type="button" class="btn btn-primary btn-sm addDbSchema"><i class="fa fa-plus" aria-hidden="true"></i> </button>'+
      			'&nbsp;<button type="button" class="btn btn-primary btn-sm deleteDbSchema"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>'+
      		'</div>'+
      	  '</div>' ;
      		
    		}else if(protocal.indexOf('mysql') != -1 || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1   || protocal.indexOf('sforce') != -1 || protocal.indexOf('as400') != -1  ||  protocal.indexOf('postgresql') != -1    || protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1){
    			
    			defualtVariableDbSchema += '<div class="row form-group dbSchemaSelection" id="dbSchemaSelection">'+
        	    '<div class="col-sm-2"> </div>'+
      		'<div class="col-sm-2">'+
      		'<select class="form-control schemaVariable" id="schemaVariable">'+
        		'<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
        		 $.each( schemaVariable, function( key, value ) {
        			if(key != 0)
       			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
       		 });
       		 
        		defualtVariableDbSchema += '</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      		'<select class="form-control dbName" id="dbName">'+
        		'<option value="{dbName}">'+globalMessage['anvizent.package.label.selectschemaname']+'</option>';
      		  $.each( dbname, function( key, value ) {
      			  if(key != 0)
      			defualtVariableDbSchema += '<option value='+value.innerHTML+'>'+value.innerHTML+'</option>';
      		 });
      		 
      		defualtVariableDbSchema += '</select>'+
      		'</div>'+
      		'<div class="col-sm-2">'+
      			'<button type="button" class="btn btn-primary btn-sm addDbSchema"><i class="fa fa-plus" aria-hidden="true"></i> </button>'+
      			'&nbsp;<button type="button" class="btn btn-primary btn-sm deleteDbSchema"><span class="glyphicon glyphicon-trash"></span></button>'+
      		'</div>'+
      	  '</div>' ;
    		}
    		
  		  $("#defualtVariableDbSchema").append(defualtVariableDbSchema);
		});
    
	    $(document).on("click",".deleteDbSchema",function(){ 		
	 		$(this).parents("#dbSchemaSelection").remove(); 		 			 
	 	});
	  
	  	$(document).on("change","#dbName",function(){ 	
			var userID = $("#userID").val();
			var $schemaName = $(this).parents('#dbSchemaSelection').find('#schemaName');
			var databaseName = $(this).val();
			if(databaseName === '{dbName}' ){
				common.showErrorAlert(globalMessage['anvizent.package.message.pleaseselectdatabase']);
				return false;
			}
			var connectionId = $("#connectionId").val();
			var connector_id = $('#connectionId').find('option:selected').attr("data-connectorId");
		    var protocal = $('#connectionId').find('option:selected').data("protocal");
		if(connectionId !=null ){
			if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				showAjaxLoader(true);
				var url_getSchemaByDatabse ="/app/user/"+userID+"/package/getSchemaByDatabse/"+connectionId+"/"+databaseName+""; 
				var myAjax = common.loadAjaxCall(url_getSchemaByDatabse,'GET','',headers);
				    myAjax.done(function(result) {
				    	showAjaxLoader(false);
				    	  if(result != null){ 
				    		  
				    		  if(result.hasMessages){
				    			  if(result.messages[0].code == "ERROR") { 
				    				 
				    				  common.showErrorAlert(result.messages[0].text);
				    				  return false;
				    			  }  
				    		   else if(result.messages[0].code == "SUCCESS"){
				    			   
				    			   var schemaName='';
				    			  
				    			   if(result.object != null){
				    				   $schemaName.empty();
				    				      schemaName  = '<option value="{schemaName}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
				    				   $.each( result.object, function( key, value ) {
				    					   schemaName += '<option value='+value+'>'+value+'</option>';
					    				 });
				    				  
				    				   $schemaName.append(schemaName);
				    				  
				    			   }else{
				    				   common.showErrorAlert(result.messages[0].text);
				    				   return false;
				    			   }
				    			 
				    			  } 
				    		  }	  
				    	  }    	  
				    }); 
			}
			
			}
		 	 
	 	});
	  	
	  	$(document).on('click', '#viewValidationscript', function(){
			var validationscriptId = $(this).attr('data-scriptId');
			var userId = $('#userID').val();
			
			var selectData = {
					scriptId : validationscriptId
			};
			
			var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
			
			showAjaxLoader(true);
			var url = "/app_Admin/user/"+userId+"+/etlAdmin/viewValidationScriptByScriptId";
			
			var myAjax = common.postAjaxCallObject(url, 'POST', selectData, headers);
			  myAjax.done(function(result){
					showAjaxLoader(false);
					if(result != null && result.hasMessages){
						if(result.messages[0].code == 'SUCCESS'){
							if(result.object.scriptId !=null || result.object.scriptId != 0){
								validatePreload.viewValidationScript(result.object.validationScripts);
							 }else{
								 validatePreload.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
							 }
						}
						else{
							common.displayMessages(result.messages);
						}
					}
				});
			 
		});
	  	
	  	$("button#validatebutton").on('click', function() {
			var userID = $("#userID").val();
			//$('#validateDbVaribleshiddenbtn').click();
			var dataObject = validatePreload.getData();
			var selScripts = $("#tvalidatePreloadTable").find("input.jcolumn-check:checked");
			var connectionId = $("#connectionId").val();
			var selScriptIds = [];
			
			if(dataObject == null || dataObject == ""){
				return false;s
			}
			if(selScripts.length == 0){
				$("#messagePopUp").modal('show');
				$("#popUpMessage").text(globalMessage['anvizent.package.label.selectAtleastonescript']).addClass("alert-danger").removeClass('alert-success');
				return false;
			}
			table.$(".jcolumn-check:checked").each(function(){
				selScriptIds.push($(this).data("scriptid"));
			});
			console.log(selScriptIds);
			var selectdata = {
					scriptIds : selScriptIds,
					schemaDbVariables : dataObject,
					ilConnection:{
						connectionId : connectionId
					}
			}
			console.log(dataObject);
			console.log(selectdata);
			var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
			
			showAjaxLoader(true);
			var url = "/app_Admin/user/"+userID+"/etlAdmin/executePreloadScripts";
			var myAjax = common.loadAjaxCall(url,'POST', selectdata, headers);
			
			myAjax.done(function(result){
				showAjaxLoader(false);
				if(result != null && result.hasMessages){
		    		if(result.messages[0].code == "SUCCESS"){
				    	$("#messagePopUp").modal('show');
				    		$("#popUpMessage").text(globalMessage['anvizent.package.label.successfullyValidated']).addClass('alert-success').removeClass('alert-danger');
					}else{
						common.displayMessages(result.messages);
					}
		    }else{
		    	var messages = [ {
					code : globalMessage['anvizent.message.error.code'],
					text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
				} ];
				common.displayMessages(messages);
		    }
			});
			
		});
	  	
	  	$(document).on("click","#validateDbVaribleshiddenbtn",function(){ 	
	  		showAjaxLoader(true);
	  		var dataObject=validatePreload.getData();
	  		console.log('db vars'+JSON.stringify(dataObject));
		    showAjaxLoader(false);
	  	});
	  	
	  	$(document).on('click', '#resetPreloadValidation', function(){
			common.clearValidations(['#validationName', '#validationScripts', 'isActive', '#dlList']);
			$('#dlList').multipleSelect("uncheckAll");
			$("#validationName").val("");
			$('#validationScripts').val("");
			$("#active1").attr("checked",true);
		});
		
		$("#addPreloadValidation").on('click', function() {
			 
		       var status= validatePreload.validationCreationFormValidation();
			   if(!status){ return false;}
			    
		       var selectors = [];
			
		       selectors.push('#validationName');
		       selectors.push('#validationScripts');
			    
		        var valid = common.validate(selectors);
			
			    if(!valid){ return false;}
			
				var userID=$('#userID').val();
				$("#dataValidationForm").prop("action",$("#addUrl").val()); 
				this.form.submit();
			    showAjaxLoader(true);
				 
		 });
		
		$("#updatePreloadValidation").on('click', function() {
			 
		       var status= validatePreload.validationCreationFormValidation();
			   if(!status){ return false;}
			    
		       var selectors = [];
			
		       selectors.push('#validationName');
		       selectors.push('#validationScripts');
			    
		        var valid = common.validate(selectors);
			
			    if(!valid){ return false;}
			
				var userID=$('#userID').val();
				$("#dataValidationForm").prop("action",$("#updateUrl").val()); 
				this.form.submit();
			    showAjaxLoader(true);
				 
		 });
		
		$(".editPreloadValidation").on('click', function() {
			var userID=$('#userID').val();
			$("#dataValidationForm").prop("action",$("#editUrl").val()); 
			this.form.submit();
		    showAjaxLoader(true);
		 });
}