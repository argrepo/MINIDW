var headers = {};
var isConnectionWithPlaceHolder = false;
var databaseConnection = {
        initialPage : function() {
        	$("#existingConnectionsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }, "searching": false,"order": [[ 1, "desc" ]]});
             setTimeout(function(){
				$("#database_username").val("").attr("disabled",false);
				$("#database_password").val("").attr("disabled",false);
             }, 25)
            var token = $("meta[name='_csrf']").attr("content");
 			var header = $("meta[name='_csrf_header']").attr("content");
 			headers[header] = token;
        },
        showSuccessMessage:function(text, hidetick, time) {
            $(".messageText,.successMessageText").empty();
            $(".successMessageText").html(text +(hidetick ? '' : '<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'));
            $(".successMessage").show();
            setTimeout(function() { $(".successMessage").hide(); }, time && time>0 ? time : 10000);
        },
        resetNewConnectionForm : function(){
        	$(".connectionTitle").text(globalMessage['anvizent.package.button.createNewConnection']);
        	common.clearValidations(['#database_connectionName', '#database_serverName', '#database_username', '#database_password',"#dateFormat","#timesZone","dataSourceName","#dataSourceOtherName"]);
        	$("#editConnection,#updateConnection, .msAccessDBFile").hide();
        	$("#testConnection,#saveNewConnection_dataBaseType").show();
        	$("#database_connectionName,#database_serverName,#database_username,#database_password").val("").removeAttr("disabled");
    	    var urlformat = $("#database_databaseType option:selected").data("urlformat");
    	    var protocal = $("#database_databaseType option:selected").data("protocal");
    	    if(protocal.indexOf("mysql")!=-1){
    	    	$("#sslEnable").attr("checked",false);
    	    	$("#mysqlSslCertificateFileNamesDiv").hide();
    	    	$("#mysqlSslCertificateFilesDiv").hide();
    	    	$("#sslEnableDiv").show();
    	    }
    	    var connectionStringParams = $("#database_databaseType option:selected").data("connection_string_params");
    	    if (connectionStringParams) {
    	    	isConnectionWithPlaceHolder = true;
    	    }
	 	    $('.serverIpWithPort').empty().text("Format : "+urlformat); 	
        	$("#database_databaseType").val($("#database_databaseType option:first").val()).removeAttr("disabled");
    		$("#database_connectionType").val($("#database_connectionType option:first").val()).removeAttr("disabled");
    		//$("#database_databaseType").val(1);
    		$("#dateFormat").val('').removeAttr("disabled");;
    		$("#timesZone").val(common.getTimezoneName()).removeAttr("disabled");
    		$("#dataSourceName").val(0).removeAttr("disabled");
    		$("#dataSourceOtherName").val("").removeAttr("disabled");
    		$("#database_queryParam").val("").removeAttr("disabled");
    		$("#requestParamsTable").hide();
    		$('#dbVariablesTbl tbody').empty();
    		
        },
        updateConnectionPanel : function(result){
		       var  connectionname = result["connectionName"],
	       		databaseId = result["database"].id,
	       		connectionType = result["connectionType"], 
	       		server = result["server"],
	       		username = result["username"],
	       		password = result["password"],
	            dateFormat = result["dateFormat"],
	            timesZone = result["timeZone"],
	            dataSource = result["dataSourceName"],
	            sslEnable = result["sslEnable"],
	            sslFileNames = result["sslTrustKeyStoreFilePaths"],
	            protocal = result["database"].protocal,
	            dbVariables = JSON.parse(result["dbVariables"])
	        	 
	       $("#database_connectionName").val(connectionname).attr("disabled","disabled");
	       $("#database_databaseType").val(databaseId).attr("disabled","disabled");
	       $("#database_databaseType").change();
	       $("#database_serverName").val(server).attr("disabled","disabled");
	       this.parseConnectionParameters(server);
	       $("#database_connectionType").val(connectionType).attr("disabled","disabled");
		   $("#database_username").val(username).attr("disabled","disabled");
		   $("#database_password").val(password).attr("disabled","disabled");
		   $("#dateFormat").val(dateFormat).attr("disabled","disabled");
		   $("#timesZone").val(timesZone).attr("disabled","disabled");
		   
		   if(sslEnable){
			   $("#sslEnableDiv").show();
			   $("#sslEnable").prop('checked', true);
			   $("#mysqlSslCertificateFilesDiv").show();
			   $("#mysqlSslCertificateFileNamesDiv").show();
			   
			   var sslFileNamesArray = sslFileNames.split(',');
			   var truststore = sslFileNamesArray[1].split('/').pop();
			   var keystore = sslFileNamesArray[2].split('/').pop();
			   var ca = sslFileNamesArray[3].split('/').pop();
			   $("#mysqlSslCertificateFileNames").text(truststore+","+keystore+","+ca);
			   
			   }else{
				   $("#sslEnableDiv").hide();
				   $("#sslEnable").prop('checked', false);
				   $("#mysqlSslCertificateFilesDiv").hide();
				   $("#mysqlSslCertificateFileNamesDiv").hide();
				   $("#mysqlSslCertificateFileNames").text("");
			   }
		   console.log("protocal-->",protocal)
		   if(protocal.indexOf("mysql") != -1){
   	    	$("#sslEnableDiv").show();
	   	    }else{
	   	    	$("#sslEnableDiv").hide();
	   	    }
		   if (dataSource){
			   $("#dataSourceName").val(dataSource).attr("disabled","disabled");
		   }
		   var dbVarTbl = $('#dbVariablesTbl').find("tbody");
		   dbVarTbl.empty();
		   $("#dataSourceName").trigger("change");
		   if(dbVariables != null && dbVariables.length > 0){
				 for(var i=0;i<dbVariables.length;i++){
					 var trDiv = $("#dbVariablesTbl tfoot tr.clonedDbVariable").clone().removeClass('hidden');
					 trDiv.find(".dbVarKey").val(dbVariables[i].key).attr("disabled","disabled");
					 trDiv.find(".dbVarValue").val(dbVariables[i].value).attr("disabled","disabled");;
						dbVarTbl.append($(trDiv)) 
				 }
		   
		   }
		   $("#createNewConnectionPopUp").modal("show");
   },
        parseConnectionParameters: function (server) {
        	$(".servername-div").hide();
        	$(".placeholders-div, .query-parameters-div").hide();
        	if (!server.match("^{")) {
        		isConnectionWithPlaceHolder = false;
        		$(".servername-div").show();
        		$("#database_serverName").val(server);
        		return;
    		}
        	
        	$(".placeholders-div, .query-parameters-div").show();
        	var connectionStringParams = JSON.parse(server);
        	var connectionParamArray = connectionStringParams["connectionParams"];
			$.each(connectionParamArray,function(index, obj){
				if ($("[class='placeHolderKey'][value='"+obj['placeholderKey']+"']").length) {
					$("[class='placeHolderKey'][value='"+obj['placeholderKey']+"']").parent().find(".placeHolderValue").val(obj['placeholderValue']).attr("disabled","disabled");
				}
			});
			 $("#database_queryParam").val(connectionStringParams["queryParams"]).attr("disabled","disabled");
        },
    	getILDatabaseConnections: function(from) {
     		var userID = $("#userID").val();
     		var url_getConnections = "/app/user/"+userID+"/package/getUserILConnections";
     		var myAjax = common.loadAjaxCall(url_getConnections,'GET','',headers);
     	    myAjax.done(function(result) {
     	    		databaseConnection.updateExistingDatabaseConnections([],from);
     		    	if(result != null && result.hasMessages ){
     		    		if(result.messages[0].code == "SUCCESS"){
     		    			databaseConnection.updateExistingDatabaseConnections(result.object,from) ; 
     			    	} else {
     				    	common.displayMessages(result.messages);
     			    	}
     		    	} else {
     		    		var messages = [ {
     		    			code : globalMessage['anvizent.message.error.code'],
 							text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
     					} ];
     		    		common.displayMessages(messages);
     		    	}
     		    	
     	    });
     	},
     	
    	editConnection : function() {
    			$(".connectionTitle").text(globalMessage['anvizent.package.button.editConnection']);
				$("#saveNewConnection_dataBaseType,#editConnection, .msAccessDBFile").hide();
				$("#updateConnection,#testConnection").show();
				$("#database_connectionName,#database_serverName,#database_username,#database_password,#database_connectionType,#dateFormat,#timesZone,#dataSourceName,#dataSourceOtherName").removeAttr("disabled");
				var connectorId=$("#database_databaseType option:selected").attr("data-connectorId");
			    var urlformat = $("#database_databaseType option:selected").data("urlformat");
		        $('.serverIpWithPort').empty().text("Format : "+urlformat);
    	},
    	validateDateForamtAndTimeZone : function(){
 	        var timeZone = $("#timesZone").val();
    		      var validate = true;
    		   	 if(timesZone == ''){
    		   		 common.showcustommsg("#timesZone", "please select time zone","#timesZone");
    		   		 validate = false;
    		   	 }
    		   	return validate;
    	},
	showCustomMessage: function(selector, msg) {
		$(selector).empty();
		var message = '<div class="alert '+(msg.code === 'SUCCESS' ? 'alert-success' : 'alert-danger')+'">' +msg.text+ '</div>';
		$(selector).append(message).show();
		setTimeout(function() { $(selector).hide().empty(); }, 5000);
	},
    testAndSaveDbConnection : function(elementId,headers,selectData,dataSourceOther,sslEnable,database_databaseTypeId,protocal){
    	   var userID = $("#userID").val();
          if(sslEnable){
        	   if(protocal.indexOf("mysql") != -1){ 
        		   
        	    var sslClientKeyFile  =   $("#sslClientKeyFile")[0].files[0];
       			var sslClientCertFile =   $("#sslClientCertFile")[0].files[0];
       			var sslServerCaFile   =   $("#sslServerCaFile")[0].files[0];
       			
       			console.log("sslClientKeyFile-->",sslClientKeyFile)
       			
       			var validSSLStatus = true;
       			
       		   if(sslClientKeyFile != undefined){
       			var sslClientKeyFileSize =  $("#sslClientKeyFile")[0].files[0].size;
       			  if ( sslClientKeyFileSize > 0) {
       	   			  var fileExtension = $("#sslClientKeyFile").val().replace(/^.*\./, '');
       			      if(!(fileExtension == 'pem')) {
       			    	common.showcustommsg("#sslClientKeyFile", globalMessage['anvizent.package.label.pleasechoosepemfile'],"#sslClientKeyFile");
       			    	validSSLStatus = false;
       			      } 
       	   		     }
       		   }
       		  if(sslClientCertFile != undefined){
       		var sslClientCertFileSize =  $("#sslClientCertFile")[0].files[0].size;
   		   if ( sslClientCertFileSize > 0) {
    			  var fileExtension = $("#sslClientCertFile").val().replace(/^.*\./, '');
    		      if(!(fileExtension == 'pem')) {
    		    	common.showcustommsg("#sslClientCertFile", globalMessage['anvizent.package.label.pleasechoosepemfile'],"#sslClientCertFile");
    		    	validSSLStatus = false;
    		      } 
       		     }
       		  }
       		  if(sslServerCaFile != undefined){
       		 var sslServerCaFileSize =  $("#sslServerCaFile")[0].files[0].size;
   		   if ( sslServerCaFileSize > 0) {
    			  var fileExtension = $("#sslServerCaFile").val().replace(/^.*\./, '');
    		      if(!(fileExtension == 'pem')) {
    		    	common.showcustommsg("#sslServerCaFile", globalMessage['anvizent.package.label.pleasechoosepemfile'],"#sslServerCaFile");
    		    	validSSLStatus = false;
    		      } 
       		     }
       		  }
       		if(!validSSLStatus){
			 	return validSSLStatus;
		     }
        	    var formData = new FormData();
      			formData.append('sslClientKeyFile', sslClientKeyFile);
      			formData.append('sslClientCertFile', sslClientCertFile);
      			formData.append('sslServerCaFile', sslServerCaFile);
      			formData.append('sslEnable', sslEnable);
      			formData.append('databaseId', database_databaseTypeId);
      			formData.append('selectData', JSON.stringify(selectData));

               showAjaxLoader(true);
       		   var url_connectionTest = "/app/user/"+userID+"/package/sslConnectionTest";
       		   var myAjax = common.postAjaxCallForFileUpload(url_connectionTest,'POST', formData,headers);
       		    myAjax.done(function(result) {
       		    	showAjaxLoader(false);
       		    	  if(result != null && result.hasMessages){
       		    		  if(result.object.code == "SUCCESS"){
       		    				  if(elementId == "testConnection"){
       		    					  $('#targetTableDirectMappingPopUp').css('overflow-y','scroll');
       		    					  common.showSuccessAlert(result.object.text);
       		    					  return false; 
       		    				  }
       		    				  if(elementId == "saveNewConnection_dataBaseType"){
       		    					  var url_createILConnection = "/app/user/"+userID+"/package/createsSSLILConnection";
       		    					   var myAjax1 = common.postAjaxCallForFileUpload(url_createILConnection,'POST', formData,headers);
       		    					    myAjax1.done(function(result) {
       		    					    		  if(result != null && result.hasMessages){
       		    					    			  if(result.messages[0].code == "SUCCESS"){
       		    					    			 	if (selectData.dataSourceName == "-1"){
       	                                            	  common.addToSelectBox(dataSourceOther,"option.otherOption")
       		    					    				} 
       		    					    				  if(result.messages[0].code == 'DUPL_CONN_NAME'){
       			    					    				  common.showcustommsg("#IL_database_connectionName", result.messages[0].text);
       			    					    				  return false;
       			    					    			  } 
       		    					    				  databaseConnection.resetConnection();
       			    					    			  $("#databaseConnectionPanel").hide();
       			    					    			  var message = result.messages[0].text;
       			    					    		      databaseConnection.showSuccessMessage(message+" "+globalMessage['anvizent.package.label.pleaseSelectTheConnectionToCompleteTheProcess'], true, 5000);
       			    					    		      databaseConnection.getILDatabaseConnections('fromSpOrCp');
       		    					    			  }else {
       		    								    		common.displayMessages(result.messages);
       		    								    	}
       		    					    		  }else {
       	    								    		var messages = [ {
       	    								    			code : globalMessage['anvizent.message.error.code'],
       	    												text : globalMessage['anvizent.package.label.unableToProcessYourRequest'] 
       	    											} ];
       	    								    		common.displayMessages(messages);
       	    								    	}
       		    					    	  
       		    					    });
       		    				  }
       		    				 
       		    			 
       		    		  }else{
       		    			databaseConnection.showCustomMessage("#databasemessage", {code: result.object.code, text: result.object.text });
       		    		  }
       		    	  }else {
       			    		var messages = [ {
       			    			code : globalMessage['anvizent.message.error.code'],
       							text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
       						} ];
       			    		common.displayMessages(messages);
       			    	}
       		    });
                
            }
           }else{
           showAjaxLoader(true);
  		   var url_connectionTest = "/app/user/"+userID+"/package/connectionTest";
  		   var myAjax = common.postAjaxCall(url_connectionTest,'POST', selectData,headers);
  		    myAjax.done(function(result) {
  		    	showAjaxLoader(false);
  		    	  if(result != null && result.hasMessages){
  		    		  if(result.object.code == "SUCCESS"){
  		    				  if(elementId == "testConnection"){
  		    					  $('#targetTableDirectMappingPopUp').css('overflow-y','scroll');
  		    					  common.showSuccessAlert(result.object.text);
  		    					  return false; 
  		    				  }
  		    				  if(elementId == "saveNewConnection_dataBaseType"){
  		    					  var url_createILConnection = "/app/user/"+userID+"/package/createsILConnection";
  		    					   var myAjax1 = common.postAjaxCall(url_createILConnection,'POST', selectData,headers);
  		    					    myAjax1.done(function(result) {
  		    					    		  if(result != null && result.hasMessages){
  		    					    			  if(result.messages[0].code == "SUCCESS"){
  		    					    			 	if (selectData.dataSourceName == "-1"){
  	                                            	  common.addToSelectBox(dataSourceOther,"option.otherOption")
  		    					    				} 
  		    					    				  if(result.messages[0].code == 'DUPL_CONN_NAME'){
  			    					    				  common.showcustommsg("#IL_database_connectionName", result.messages[0].text);
  			    					    				  return false;
  			    					    			  } 
  		    					    				  databaseConnection.resetConnection();
  			    					    			  $("#databaseConnectionPanel").hide();
  			    					    			  var message = result.messages[0].text;
  			    					    		      databaseConnection.showSuccessMessage(message+" "+globalMessage['anvizent.package.label.pleaseSelectTheConnectionToCompleteTheProcess'], true, 5000);
  			    					    		      databaseConnection.getILDatabaseConnections('fromSpOrCp');
  		    					    			  }else {
  		    								    		common.displayMessages(result.messages);
  		    								    	}
  		    					    		  }else {
  	    								    		var messages = [ {
  	    								    			code : globalMessage['anvizent.message.error.code'],
  	    												text : globalMessage['anvizent.package.label.unableToProcessYourRequest'] 
  	    											} ];
  	    								    		common.displayMessages(messages);
  	    								    	}
  		    					    	  
  		    					    });
  		    				  }
  		    				 
  		    			 
  		    		  }else{
  		    			databaseConnection.showCustomMessage("#databasemessage", {code: result.object.code, text: result.object.text });
  		    		  }
  		    	  }else {
  			    		var messages = [ {
  			    			code : globalMessage['anvizent.message.error.code'],
  							text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
  						} ];
  			    		common.displayMessages(messages);
  			    	}
  		    });
           }
    	},
    	updateExistingDatabaseConnections : function(result,from){
    		if(from === 'fromSpOrCp'){ 
    		$("#existingConnections").empty();
    		$("#existingConnections").append("<option value=''>"+globalMessage['anvizent.package.label.selectConnection']+"</option>");
    		if(result.length > 0){
    			if(result.length == 1){ 
    			var existingConnectins='';
    			$.each(result, function(key, obj) { 
    				var disabled = obj["webApp"] && !obj["availableInCloud"] ? 'disabled':'';
    				 existingConnectins+= "<option value='" + obj["connectionId"] + "'" +disabled+" selected>" + obj["connectionName"]+(obj["webApp"] && !obj["availableInCloud"] ? ' ( Local DB)':'') + "</option>";
    			 });
    			$("#existingConnections").append(existingConnectins);
    			$("#existingConnections").trigger("change");
    		} else {
    			var existingConnectins='';
    			$.each(result, function(key, obj) {
    				var disabled = obj["webApp"] && !obj["availableInCloud"] ? 'disabled':'';
     				 existingConnectins+= "<option value='" + obj["connectionId"] + "'" + disabled+">" + obj["connectionName"]+(obj["webApp"] && !obj["availableInCloud"] ? ' ( Local DB)':'') + "</option>";
    			 
    			 });
    			$("#existingConnections").append(existingConnectins);
    		}
    	  }
    		}else{

        		var existingConnectionsTable = $("#existingConnectionsTable").DataTable();
        		existingConnectionsTable.clear();
        			$.each(result, function(key, obj) {
        				var disabled = obj["webApp"] && !obj["availableInCloud"] ? 'disabled':'';
        				var editButton = "<button class='btn btn-primary btn-sm tablebuttons editConnection'  data-connectionId='"+obj["connectionId"]+"' "+disabled+" >"+
        								 "<i class='fa fa-pencil'aria-hidden='true' title='"+globalMessage['anvizent.package.label.edit']+"'></i></button>";
        				
        				var deleteButton = "<button class='btn btn-primary btn-sm tablebuttons deleteConnection' data-connectionId='"+obj["connectionId"]+"'>"+
    					 				   "<span class='glyphicon glyphicon-trash' title='"+globalMessage['anvizent.package.label.delete']+"' aria-hidden='true'></span></button>";
        				
        				var row = [];
        				row.push(key+1);
        				row.push(obj["connectionId"]);
        				row.push(obj["connectionName"].encodeHtml()+(obj["webApp"] && !obj["availableInCloud"] ? '( Local DB)':''));
        				row.push(editButton);
        				row.push(deleteButton);
        				existingConnectionsTable.row.add(row);    				
        			});
        			$("#createNewConnectionPopUp").modal("hide");
        			existingConnectionsTable.draw();
        	
    		}
    	},
    	resetConnection : function() {
    		$("#IL_database_connectionName").val("").removeAttr("disabled");
    		$("#IL_database_serverName").val("").removeAttr("disabled");
    		$("#IL_database_username").val("").removeAttr("disabled");
    		$("#IL_database_password").val("").removeAttr("disabled");
    		$("#IL_database_databaseType").val($("#IL_database_databaseType option:first").val()).removeAttr("disabled");
    	    $("#IL_database_connectionType").val($("#IL_database_connectionType option:first").val()).removeAttr("disabled");
    	    $("#existingConnections").val("");
    	    $(".duplicateConnectionNameLabel").empty();
    	    $("#dateFormat").val("").removeAttr("disabled");
    	    $("#timesZone").val(common.getTimezoneName()).removeAttr("disabled");
    	    $("#dataSourceName").val("0").removeAttr("disabled");
    	    $("#dataSourceOtherName").val("");
    	},
    	  addDBVariableDiv: function(_this) {
  			
  			var thisDiv = $("#dbVariablesTbl tbody");
  			var trDiv =  $("#dbVariablesTbl tfoot tr.clonedDbVariable").clone().removeClass('hidden');
  			thisDiv.append($(trDiv))
  		},
  		
  		addDbVariablePair: function(_this) {
  			var _currTr = $(_this).closest("tr");
  			var trDiv = $("#dbVariablesTbl tfoot tr.clonedDbVariable").clone().removeClass('hidden');
  			trDiv.find(".dbVarKey,.dbVarValue").val('');
  			trDiv.insertAfter($(_currTr))
  			
  		},
  		
  		deleteDbVariablePair : function(_this) {
  			var deleteDiv = $(_this).parents(".clonedDbVariable");
  			$(deleteDiv).remove();
  		},
};

if($(".databaseConnection-page").length || $(".databaseNewConnection-page").length){
    databaseConnection.initialPage();
    $('.collapse2').on('shown.bs.collapse hidden.bs.collapse', function(){
    	isConnectionWithPlaceHolder = true;
	});
    $("#timeZone").val(common.getTimezoneName()).trigger("change");
    //edit and update existing connections
    $(document).on('click','.editConnection', function(){
		var userID = $("#userID").val();
    	var connectionId = $(this).attr("data-connectionId");
    	$(".connectionTitle").text(globalMessage['anvizent.package.button.editConnection']);
    	$("#updateConnection").attr("data-connectionId",connectionId);
    	$("#testConnection").attr("data-connectionId",connectionId);
		$("#editConnection").show();
		$(".dataSourceOther").show();
		$("#saveNewConnection_dataBaseType,#testConnection,#updateConnection").hide();
		$("#database_databaseType").val($("#database_databaseType option:first").val());
		common.clearValidations(['#database_connectionName', '#database_serverName', '#database_username', '#database_password',"#dateFormat","timesZone","#dataSourceName","#dataSourceOtherName"]);
		$(".serverIpWithPort").empty();
		$("#database_queryParam").val('');
		
		var url_getConnectionById = "/app/user/"+userID+"/package/getILsConnectionById/"+connectionId;
		if(connectionId != null){
			showAjaxLoader(true);
		    var myAjax = common.loadAjaxCall(url_getConnectionById,'GET','',headers);
		    myAjax.done(function(result) {
			   showAjaxLoader(false);
			   if(result != null && result.hasMessages) {
				   if(result.messages[0].code == "SUCCESS"){
	    		  	databaseConnection.updateConnectionPanel(result.object);
	    		  	console.log(result);
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
		}
	});
    
    $(document).on("click","#editConnection",function(){
    	databaseConnection.editConnection();
    });
    
  //create new connection
    $(document).on("click","#createNewConnection_dataBaseType",function(){
    	$(".dataSourceOther").hide();
    	databaseConnection.resetNewConnectionForm();
    	$("#createNewConnectionPopUp").modal("show");
    });
    
    $(document).on('click', '#saveNewConnection_dataBaseType,#testConnection,#updateConnection', function(){
	       var elementId = this.id;
	       var database_connectionName = $("#database_connectionName").val().trim();
	       var database_databaseTypeId = $("#database_databaseType").val();
	       var database_databaseType = $("#database_databaseType option:selected").text();
	       var database_connectionType = $("#database_connectionType").val();
	       var database_serverName = $("#database_serverName").val();
	       var database_username = $("#database_username").val();
	       var database_password = $("#database_password").val();
	       var userID = $("#userID").val();
	       var connector_Id = $("#database_databaseType option:selected").attr("data-connectorId");
	       var protocal = $("#database_databaseType option:selected").attr("data-protocal");
	       var connectionId = $(this).attr("data-connectionId");
	       var dateFormat = $("#dateFormat").val();
	       var timeZone = $("#timesZone").val();
	       var dataSourceName=  $("#dataSourceName").val();
	       var queryParams=$("#database_queryParam").val();
	       var dataSourceOther = $("#dataSourceOtherName").val();
	      
	       common.clearValidations(["#dateFormat","#timesZone","#dataSourceName","#dataSourceOtherName"]);
	       var from ="fromMenu";
	       
	       var selectors = [];
	       selectors.push('#database_connectionName');
	       selectors.push('#dataSourceName');
	       
	       if(dataSourceName == "-1"){
	    	   if(dataSourceOther  == ''){
	    		   common.showcustommsg("#dataSourceOtherName", globalMessage['anvizent.package.label.enterDataSource'],"#dataSourceOtherName");
	    		   return false;
	    	   }
	       }
	       
	       var valid = common.validate(selectors);
	
	       if (!valid) {
	           return false;
	       }
	       if(elementId == "saveNewConnection_dataBaseType" || elementId == "updateConnection"){
           	var validate =  databaseConnection.validateDateForamtAndTimeZone();
           	  if (!validate) {
      	           return false;
      	        }
	       }
	        var paramObject={};
		    var connectionStringParam = {};
		   	var _tblObj = $("#requestParamsTable tbody tr");
			connectionStringParam["queryParams"] = queryParams;
			if (_tblObj.length ) {
				connectionStringParam["connectionParams"] = [];
				_tblObj.each(function(index, tr){
					var _tr = $(tr);
					 paramObject = {
							"placeholderKey":_tr.find(".placeHolderKey").val(),
							"placeholderValue":_tr.find(".placeHolderValue").val(),
							"isMandatory": _tr.find(".mandatorySpan").hasClass("hidden") ? false: true,
					};
					
					connectionStringParam["connectionParams"].push(paramObject);	
				});	
			}
			var validStatus = true;
			var dbVarKayValuePair = [];
			var dbVariableDiv = $("#dbVariablesTbl tbody tr");
			$(dbVariableDiv).each(function(){
				var dbVarKey = $(this).find(".dbVarKey").val();
				var dbVarValue = $(this).find(".dbVarValue").val();
				if(dbVarKey != '' && dbVarValue != '') {
					var startsWith = dbVarKey.startsWith("{")
			   	    var endsWith = dbVarKey.endsWith("}") 
			   	    common.clearValidations([$(this).find(".dbVarKey")]);
			   		if(!startsWith && !endsWith){
						common.showcustommsg($(this).find(".dbVarKey"),globalMessage['anvizent.package.label.dbvariablekeystartwithandendswith']);
						validStatus = false;
			   			}
					dbVarKayValuePair.push({"key":dbVarKey,"value":dbVarValue})	
					console.log(dbVarKayValuePair);
					}
			});
			 if (!validStatus) {
    	           return false;
    	        }
			var serverInfo = "";
			if (isConnectionWithPlaceHolder) {
				serverInfo = JSON.stringify(connectionStringParam);
			} else {
				serverInfo = database_serverName;
			}
			var sslEnable;
			 if(protocal.indexOf("mysql") != -1){ 
				 sslEnable = $("#sslEnable").is(':checked');
			 }else{
				 sslEnable = false; 
			 }
	       var selectData={
	               clientId : userID,
	               database :{
	                   id: database_databaseTypeId,
	                   name : database_databaseType
	               },
	               connectionType : database_connectionType,
	               server : serverInfo,
	               username : database_username,
	               password : database_password,
	               connectionName : database_connectionName,
	               connectionId : connectionId,
	               dateFormat:dateFormat,
	           	   timeZone:timeZone,
	           	   dataSourceName:dataSourceName,
	           	   dataSourceNameOther:dataSourceOther,
	           	   dbVariables : JSON.stringify(dbVarKayValuePair),
	               sslEnable:sslEnable
	               
	       };
	        console.log("selectData-->",selectData)
	        var token = $("meta[name='_csrf']").attr("content");
   		    var header = $("meta[name='_csrf_header']").attr("content");
   			headers[header] = token;
   		 
           if(sslEnable){
        	   
        	   if(protocal.indexOf("mysql") != -1){ 
        		   
        	    var sslClientKeyFile  =   $("#sslClientKeyFile")[0].files[0];
       			var sslClientCertFile =   $("#sslClientCertFile")[0].files[0];
       			var sslServerCaFile   =   $("#sslServerCaFile")[0].files[0];
       			
       			console.log("sslClientKeyFile-->",sslClientKeyFile)
       			
       			var validSSLStatus = true;
       			
       		   if(sslClientKeyFile != undefined){
       			var sslClientKeyFileSize =  $("#sslClientKeyFile")[0].files[0].size;
       			  if ( sslClientKeyFileSize > 0) {
       	   			  var fileExtension = $("#sslClientKeyFile").val().replace(/^.*\./, '');
       			      if(!(fileExtension == 'pem')) {
       			    	common.showcustommsg("#sslClientKeyFile", globalMessage['anvizent.package.label.pleasechoosepemfile'],"#sslClientKeyFile");
       			    	validSSLStatus = false;
       			      } 
       	   		     }
       		   }
       		  if(sslClientCertFile != undefined){
       		var sslClientCertFileSize =  $("#sslClientCertFile")[0].files[0].size;
   		   if ( sslClientCertFileSize > 0) {
    			  var fileExtension = $("#sslClientCertFile").val().replace(/^.*\./, '');
    		      if(!(fileExtension == 'pem')) {
    		    	common.showcustommsg("#sslClientCertFile", globalMessage['anvizent.package.label.pleasechoosepemfile'],"#sslClientCertFile");
    		    	validSSLStatus = false;
    		      } 
       		     }
       		  }
       		  if(sslServerCaFile != undefined){
       		 var sslServerCaFileSize =  $("#sslServerCaFile")[0].files[0].size;
   		   if ( sslServerCaFileSize > 0) {
    			  var fileExtension = $("#sslServerCaFile").val().replace(/^.*\./, '');
    		      if(!(fileExtension == 'pem')) {
    		    	common.showcustommsg("#sslServerCaFile", globalMessage['anvizent.package.label.pleasechoosepemfile'],"#sslServerCaFile");
    		    	validSSLStatus = false;
    		      } 
       		     }
       		  }
       		if(!validSSLStatus){
			 	return validSSLStatus;
		     }
        	    var formData = new FormData();
      			formData.append('sslClientKeyFile', sslClientKeyFile);
      			formData.append('sslClientCertFile', sslClientCertFile);
      			formData.append('sslServerCaFile', sslServerCaFile);
      			formData.append('sslEnable', sslEnable);
      			formData.append('databaseId', database_databaseTypeId);
      			formData.append('selectData', JSON.stringify(selectData));
      		showAjaxLoader(true);
      	    var url_connectionTest = "/app/user/"+userID+"/package/sslConnectionTest";
  	        var myAjax = common.postAjaxCallForFileUpload(url_connectionTest,'POST', formData,headers);
  	        myAjax.done(function(result) {
  	            showAjaxLoader(false);
  	              if(result != null && result.hasMessages){
  	                  if(result.object.code == "SUCCESS"){
  	                      if(elementId == "testConnection"){
  	                    	  $('#createNewConnectionPopUp').css('overflow-y','scroll');
  	                    	  common.showSuccessAlert(result.object.text);
  	                          return false;
  	                      }
  	                      if(elementId == "saveNewConnection_dataBaseType"){
  	                    	showAjaxLoader(true);
  	                          var url_createILConnection = "/app/user/"+userID+"/package/createsSSLILConnection";
  	                          var myAjax1 = common.postAjaxCallForFileUpload(url_createILConnection,'POST',formData,headers);
  	                          myAjax1.done(function(result) {
  	                        	   showAjaxLoader(false);
  	                                      if(result != null && result.hasMessages){
                                      	  	if(result.messages[0].code == "SUCCESS"){
  	                                        	  databaseConnection.getILDatabaseConnections(from);
  	                                              var message = result.messages[0].text;
  	                                              if ( dataSourceName == "-1"){
  	                                            	  common.addToSelectBox(dataSourceOther,"option.otherOption")
  	                                              }
  	                                              databaseConnection.showSuccessMessage(message, true, 15000);
                                      	  	} else if(result.messages[0].code == 'DUPL_CONN_NAME'){
  	                                    		  common.showcustommsg("#database_connectionName", result.messages[0].text);
  	                                              return false;
  	                                        } else {
      								    		common.showErrorAlert(result.messages[0].text);
      								    	}
  	                                      }else {
    								    		var messages = [ {
    								    			code : globalMessage['anvizent.message.error.code'],
    												text : globalMessage['anvizent.package.label.unableToProcessYourRequest'] 
  											} ];
  								    		common.displayMessages(messages);
  								    		$("#createNewConnectionPopUp").modal("hide");
  								    	}
  	                                  
  	                            });
  	                      }
  	                      if(elementId == "updateConnection"){
  	               	           	var url_updateConnection = "/app/user/"+userID+"/package/updateSSLDatabaseConnection";
  		                        var myAjax1 = common.postAjaxCallForFileUpload(url_updateConnection,'POST', formData,headers);
  		                        myAjax1.done(function(result) {
  		                                  if(result != null && result.hasMessages){
  		                                    	  if(result.messages[0].code == 'DUPL_CONN_NAME'){
  		                                    		  common.showcustommsg("#database_connectionName", result.messages[0].text);
  		                                              return false;
  		                                          }else if(result.messages[0].code == "SUCCESS"){
  		                                        	  databaseConnection.getILDatabaseConnections(from);
  		                                              var message = result.messages[0].text;
  		                                              if ( dataSourceName == "-1")
  		                                            	  common.addToSelectBox(dataSourceOther,"option.otherOption")
  		                                            databaseConnection.showSuccessMessage(message, true, 15000);
  		                                          }else{
  		                                        	  common.showErrorAlert(result.messages[0].text); 
  		                                          }
  		                                  }else{
  		          							var messages = [ {
  		          								code : globalMessage['anvizent.message.error.code'],
  		        								text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
  		        							} ];
  		        				    		common.displayMessages(messages);
  		        				    		$("#createNewConnectionPopUp").modal("hide");
  		        						}
  		                        });
  	                      }
  	                  }else{
  	                      $('#createNewConnectionPopUp').css('overflow-y','scroll');
  	                      common.showErrorAlert(result.object.text);
  	                  }
  	           }else {
  		    		var messages = [ {
  		    			code : globalMessage['anvizent.message.error.code'],
  						text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
  					} ];
  		    		common.displayMessages(messages);
  		    	}
  	         });
            }
           }else{
	        var url_connectionTest = "/app/user/"+userID+"/package/connectionTest";
	        var myAjax = common.postAjaxCall(url_connectionTest,'POST', selectData,headers);
	        myAjax.done(function(result) {
	            showAjaxLoader(false);
	              if(result != null && result.hasMessages){
	                  if(result.object.code == "SUCCESS"){
	                      if(elementId == "testConnection"){
	                    	  $('#createNewConnectionPopUp').css('overflow-y','scroll');
	                    	  common.showSuccessAlert(result.object.text);
	                          return false;
	                      }
	                      if(elementId == "saveNewConnection_dataBaseType"){
	                          var url_createILConnection = "/app/user/"+userID+"/package/createsILConnection";
	                          var myAjax1 = common.postAjaxCall(url_createILConnection,'POST', selectData,headers);
	                          myAjax1.done(function(result) {
	                                  
	                                      if(result != null && result.hasMessages){
                                    	  	if(result.messages[0].code == "SUCCESS"){
	                                        	  databaseConnection.getILDatabaseConnections(from);
	                                              var message = result.messages[0].text;
	                                              if ( dataSourceName == "-1")
	                                            	  common.addToSelectBox(dataSourceOther,"option.otherOption")
	                                              databaseConnection.showSuccessMessage(message, true, 15000);
                                    	  	} else if(result.messages[0].code == 'DUPL_CONN_NAME'){
	                                    		  common.showcustommsg("#database_connectionName", result.messages[0].text);
	                                              return false;
	                                        } else {
    								    		common.showErrorAlert(result.messages[0].text);
    								    	}
	                                      }else {
  								    		var messages = [ {
  								    			code : globalMessage['anvizent.message.error.code'],
  												text : globalMessage['anvizent.package.label.unableToProcessYourRequest'] 
											} ];
								    		common.displayMessages(messages);
								    		$("#createNewConnectionPopUp").modal("hide");
								    	}
	                                  
	                            });
	                      }
	                      if(elementId == "updateConnection"){
	               	           	var url_updateConnection = "/app/user/"+userID+"/package/updateDatabaseConnection";
		                        var myAjax1 = common.postAjaxCall(url_updateConnection,'POST', selectData,headers);
		                        myAjax1.done(function(result) {
		                                  if(result != null && result.hasMessages){
		                                    	  if(result.messages[0].code == 'DUPL_CONN_NAME'){
		                                    		  common.showcustommsg("#database_connectionName", result.messages[0].text);
		                                              return false;
		                                          }else if(result.messages[0].code == "SUCCESS"){
		                                        	  databaseConnection.getILDatabaseConnections(from);
		                                              var message = result.messages[0].text;
		                                              if ( dataSourceName == "-1")
		                                            	  common.addToSelectBox(dataSourceOther,"option.otherOption")
		                                              databaseConnection.showSuccessMessage(message, true, 15000);
		                                          }else{
		                                        	  common.showErrorAlert(result.messages[0].text); 
		                                          }
		                                  }else{
		          							var messages = [ {
		          								code : globalMessage['anvizent.message.error.code'],
		        								text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
		        							} ];
		        				    		common.displayMessages(messages);
		        				    		$("#createNewConnectionPopUp").modal("hide");
		        						}
		                        });
	                      }
	                  }else{
	                	  $('#createNewConnectionPopUp').css('overflow-y','scroll');
	                      common.showErrorAlert(result.object.text);
	                  }
	           }else {
		    		var messages = [ {
		    			code : globalMessage['anvizent.message.error.code'],
						text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
					} ];
		    		common.displayMessages(messages);
		    	}
	      });
	   	}
    });
    
    //delete existing connections
    $(document).on("click",".deleteConnection",function(){
    	$("#deleteConnectionAlert").modal("show");
    	var connectionId = $(this).attr("data-connectionId");
    	$("#confirmDeleteConnection").attr("data-connectionId",connectionId);
    });
    
    $(document).on("click","#confirmDeleteConnection",function(){
    	var userID = $("#userID").val();
		var connectionId = $(this).attr("data-connectionId");
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url_deleteConnection = "/app/user/"+userID+"/package/deleteILConnection/"+connectionId;
		showAjaxLoader(true);
		var myAjax = common.postAjaxCall(url_deleteConnection,'POST','',headers);
 	    myAjax.done(function(result) {
 	    	showAjaxLoader(false);
 	    	  if(result != null && result.hasMessages){ 
	    			  $("#deleteConnectionAlert").modal("hide");
	    			  if(result.messages[0].code == "ERROR") {	    				 
	    				  $('.messageText').empty().append(result.messages[0].text).show();
		    			  setTimeout(function() { $(".message").hide().empty(); }, 10000);
	    			  }
	    			  else if(result.messages[0].code == "SUCCESS") {
	    				  databaseConnection.showSuccessMessage(result.messages[0].text, true, 5000);
	    				  databaseConnection.getILDatabaseConnections('fromMenu');
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

    $(document).on('change', '#database_databaseType', function(){
    	var connectionId=$(this).find('option:selected').attr("data-connectorId");
	    var urlformat = $(this).find('option:selected').data("urlformat");
        $('.serverIpWithPort').empty().text("Format : "+urlformat);
        var connectionStringParams = $(this).find('option:selected').data("connection_string_params");
        console.log(connectionStringParams);
        isConnectionWithPlaceHolder =true;
        if (connectionStringParams) {
        	$(".servername-div").hide();
        	$(".placeholders-div, .query-parameters-div").show();
			var connectionParamArray = connectionStringParams["connectionParams"];
			$("#requestParamsTable").show();
			var _tblObj = $("#requestParamsTable tbody");
			var _footerRow = $("#requestParamsTable tfoot tr");
			_tblObj.empty();
			$.each(connectionParamArray,function(index, obj){
				var _newRow = _footerRow.clone();
				
				_newRow.find(".placeHolderLabelName").text(obj['lableName']);
				_newRow.find(".placeHolderKey").val(obj['placeholderName']);
				
				obj['isMandatory'] ? _newRow.find(".mandatorySpan").removeClass('hidden') : _newRow.find(".mandatorySpan").addClass('hidden');
				_tblObj.append(_newRow);
			});
		} else {
			isConnectionWithPlaceHolder = false;
			$("#requestParamsTable").hide();
			$(".servername-div").show();
			$(".placeholders-div, .query-parameters-div").hide();
		}
        
        var protocal=$(this).find('option:selected').attr("data-protocal");
        
        if(protocal.indexOf("mysql")!=-1){
	    	$("#sslEnableDiv").show();
	    	var sslEnable = $("#sslEnable").is(':checked');
	    	if(sslEnable){
	    	$("#mysqlSslCertificateFilesDiv").show();
	    	}
	    }else{
	    	$("#sslEnableDiv").hide();
	    	$("#mysqlSslCertificateFilesDiv").hide();
	    }
        
    });
    $(document).on("change","#dataSourceName",function(){
    	var dataSourceName=  $("#dataSourceName option:selected").val();
    	if(dataSourceName == "-1"){
    		$("#dataSourceOtherName").val("");
	    	   $(".dataSourceOther").show();
	       }else{
	    	   $(".dataSourceOther").hide();
	       }
    });
    
    $("#addDBVarDiv").on("click",function(){
    	databaseConnection.addDBVariableDiv(this);
	});
    
    $(document).on("click",".addDbVariablePairDiv",function(){
		databaseConnection.addDbVariablePair(this);
	});
		
	$(document).on("change","#sslEnable",function(){
		var sslEnable = $(this).is(':checked');
		if(sslEnable){
			$("#mysqlSslCertificateFilesDiv").show();
			$("#sslClientCertFile").val("");
			$("#sslClientKeyFile").val("");
			$("#sslServerCaFile").val("");
		}else{
			$("#mysqlSslCertificateFilesDiv").hide();	
		}
	});
    
} 