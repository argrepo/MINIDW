var headers = {};
var resultOfTargetTable =[];
var dataTypeResult = "";
var dataTypeSelect = "";
var jsonObj = '';
var isConnectionWithPlaceHolder = false;
var customPackage = {
		initialPage : function() {
			$("input[type=radio][name='typeSelection']").prop('checked', false);
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
	},
	 
	showMessage:function(text){
		$(".messageText").empty();
		$(".successMessageText").empty();
		$(".messageText").html(text);
	    $(".message").show();
	    setTimeout(function() { $(".message").hide(); }, 10000);
	},
	
	showCustomMessage: function(selector, msg) {
		$(selector).empty();
		var message = '<div class="alert '+(msg.code === 'SUCCESS' ? 'alert-success' : 'alert-danger')+'">' +msg.text+ '</div>';
		$(selector).append(message).show();
		setTimeout(function() { $(selector).hide().empty(); }, 5000);
	},
	
	showSuccessMessage:function(text, hidetick, time) {
		$(".successMessage").show();
		$(".messageText").empty();
	    $(".successMessageText").empty();
	    $(".successMessageText").html(text +(hidetick ? '' : '<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'));
       setTimeout(function() { $(".successMessage").hide(); }, time && time>0 ? time : 2500);
},
updateConnectionPanel : function(result){
    var connectionname = result["connectionName"];
    var databaseId = result["database"].id; 
    var connectionType = result["connectionType"]; 
    var server = result["server"];
    var username = result["username"];
    var connectorId= result["database"].connector_id;
    var protocal= result["database"].protocal;
    var dateFormat = result["dateFormat"];
    var timesZone = result["timeZone"];
    var dataSource = result["dataSourceName"];
    var dbVariables = JSON.parse(result["dbVariables"]);
    var sslEnable = result["sslEnable"];
    var sslTrustKeyStoreFilePaths = result["sslTrustKeyStoreFilePaths"];
    
    $(".dbVariableCloneDiv").empty();
    $("#IL_database_connectionName").val(connectionname).attr("disabled","disabled");
    $("#IL_database_databaseType").val(databaseId).attr("disabled","disabled");
    $("#IL_database_databaseType").change();
    $("#IL_database_serverName").val(server).attr("disabled","disabled");
    standardPackage.parseConnectionParameters(server);
    $("#IL_database_connectionType").val(connectionType).attr("disabled","disabled");
    this.parseConnectionParameters(server);
	$("#IL_database_username").val(username).attr("disabled","disabled");
	$("#dateFormat").val(dateFormat).attr("disabled","disabled");
	$("#timesZone").val(timesZone).attr("disabled","disabled");
	console.log("dataSource->",dataSource)
   if (dataSource){
	   $("#dataSourceName").val(dataSource);
   } else {
	   $("#dataSourceName").val("-1");
   }
   $("#dataSourceName").trigger("change");
	
   if(protocal.indexOf('sforce') != -1){
   	$("#typeOfCommand").empty();
   	$("#typeOfCommand").append('<option value="Query">'+globalMessage['anvizent.package.label.query']+'</option>');
   }else{
   	$("#typeOfCommand").empty();
   	$("#typeOfCommand").append('<option value="Query">'+globalMessage['anvizent.package.label.query']+'</option> <option value="Stored Procedure">'+globalMessage['anvizent.package.label.storedProcedure']+'</option>');
   }
   
   $('#mysqlSslCertificateFilesDiv').hide();
   $('#mysqlSslCertificateFileNamesDiv').hide();
   if(protocal.indexOf('mysql') != -1){
    	 if(sslEnable) {
    		 $('#sslEnable').attr('checked', true);
    		 $('#sslEnable').attr('disabled', "disabled");
    		 $('#sslEnableDiv').show();
    		 $('#mysqlSslCertificateFileNamesDiv').show();
    		 var sslFileNamesArray = sslTrustKeyStoreFilePaths.split(',');
			 var truststore = sslFileNamesArray[1].split('/').pop();
			 var keystore = sslFileNamesArray[2].split('/').pop();
			 var ca = sslFileNamesArray[3].split('/').pop();
			 $("#mysqlSslCertificateFileNames").text(truststore+","+keystore+","+ca);
    		 }else{
    			 $('#sslEnableDiv').hide();
        		 $('#mysqlSslCertificateFileNamesDiv').hide();
    		 }
    } 
   $(".query-parameters-div").hide();
   $(".db-variables-add-div").removeClass("hidden");
   var _dbVariableTables = $("#dbVariablesTbl tbody");
   $("#addDBVarDiv").hide();
   _dbVariableTables.empty();
   if(dbVariables != null && dbVariables.length > 0){
	   $(".dbVariableCloneDiv").empty();
	   for(var i=0;i<dbVariables.length;i++){
			 var $this = $("#dbVariablesTbl tfoot tr.displayDbVariable").clone().addClass("cloneDBVar").removeClass('displayDbVariable hidden');
			 $this.find(".dbVarKey").text(dbVariables[i].key);
			 $this.find(".dbVarValue").text(dbVariables[i].value);
			 _dbVariableTables.append($($this));
		 }
   } else {
	   $(".db-variables-add-div").addClass("hidden");
	   $("#addDBVarDiv").show();
   }
   
},
parseConnectionParameters: function (server) {
	if (!server.match("^{")) {
		return;
	}
	var connectionStringParams = JSON.parse(server);
	var connectionParamArray = connectionStringParams["connectionParams"];
	$.each(connectionParamArray,function(index, obj){
		if ($("[class='placeHolderKey'][value='"+obj['placeholderKey']+"']").length) { 
			$("[class='placeHolderKey'][value='"+obj['placeholderKey']+"']").parent().find(".placeHolderValue").val(obj['placeholderValue']).attr("disabled","disabled");
		}
	});
	 $("#database_queryParam").val(connectionStringParams["queryParams"]).attr("disabled","disabled");
},
updateExistingSchemas : function(result){
	
	$("#existingSchema").empty();
	if(result.length>0){
		var existingSchema='';
		$.each(result, function(key, obj) {
			existingSchema+= "<option value='" + obj["tableId"] + "' " +
					"data-schemaName = '"+obj["schemaName"]+"'" +
					">" + obj["tableName"] + "</option>";
		 });
		$("#existingSchema").append(existingSchema);
		$("#existingSchema").prepend("<option value='' >"+globalMessage['anvizent.package.label.selectSchema']+"</option>");
	}else{
		$("#existingSchema").append("<option value=''>"+globalMessage['anvizent.package.label.selectSchema']+"</option>");
	}
	},
showCustomPackageSteps : function(stepid) {
	
	if (!stepid) stepid = 0;
	
	var imgsteps = $("img.img-steps", "#customPackageForm");
	
	imgsteps.filter(function() {
		return !$(this).hasClass('hide');
	}).addClass("hide");
	
	$(imgsteps.get(stepid)).removeClass('hide');
	
},
updateSelectFileHeadersTable : function(result) {
	var table = $("#selectFileHeadersTable tbody");
	table.empty();
	var tableRow='';
	if(result.object.length>0){
		for(var i=0;i<result.object.length;i++) {
			var file = result.object[i];
			 tableRow +=  "<tr>"+
				"<td>"+(i+1)+"</td>"+
				"<td class='file-name'>"+file.fileName+"</td>"+
				"<td class='smalltd'><input type='checkbox' value='"+file.fileId+"' class='chk-primary btn btn-primary btn-sm'></td>"+
				"</tr>";
			
		}
	}
	table.append(tableRow);
	$("#selectFileHeadersDiv").show();
},

buildTableColumnPopup : function(columns,columnDataTypes) {
	var tableRow = "";
	if (columns) {
		$.each(columns, function(i, col) {
			debugger
			var colSize = "";
			selectDatatype = '<select class="form-control input-sm dataType m-datatype" style="width:100%;">';
			for(var i=0;i<dataTypeResult.object.length;i++){
				 var dataTypeName = dataTypeResult.object[i].dataTypeName;
				 var range = dataTypeResult.object[i].range;
				 
				 if(dataTypeName == columnDataTypes[i]){
					 selectDatatype+="<option value='"+dataTypeName+"' selected>"+dataTypeName+"</option>"; 
					 if(columnDataTypes[i] == "DECIMAL" || columnDataTypes[i] == "FLOAT" || columnDataTypes[i] == "DOUBLE"){
							var columnSizeDecimal = range.split(",");
							colSize+= '<input type="text" class="input-sm m-colsize" value='+columnSizeDecimal[0]+' style="width:50%;"/>'+
						  		  	  '<input type="text" class="input-sm m-decimal" value='+columnSizeDecimal[1]+' style="width:50%;"/>';
					}else{
						   colSize = '<input type="text" class="input-sm m-colsize" value='+range+' style="width:50%;"/>'+
				          '<input type="text" class="input-sm m-decimal" value="2" style="width:50%;display:none;"/>';
					}
					 
				 }else{
					 var selected;
					 if(dataTypeName == 'VARCHAR'){
						 selected = "selected"
					 }else{
						 selected = "";
					 }
					 selectDatatype+="<option value='"+dataTypeName+"'"+selected+">"+dataTypeName+"</option>";
						if(columnDataTypes[i] == "" || columnDataTypes[i] == null){
							colSize = '<input type="text" class="input-sm m-colsize" value="45" style="width:50%;"/>'+
				  		  	  		  '<input type="text" class="input-sm m-decimal" value="2" style="width:50%;display:none;"/>';
						}
				 }
			 }
			
			selectDatatype += '</select>'
			
			tableRow +=  "<tr>"+
				'<td><input type="checkbox" class="m-check" ></td>'+
				'<input class="originalColumnName" type="hidden" value='+col+'>'+
				'<td><input class="form-control m-column" id="m-column'+i+'"  type="text" value='+col+'></td>'+
				'<td>'+selectDatatype+'</td>'+
				'<td nowrap>'+colSize+'</td>'+
				'<td><input type="checkbox" class="m-primary"></td>'+
				'<td><input type="checkbox" class="m-notnull"></td>'+
				'<td><input type="checkbox" class="m-unique"></td>'+
				'<td><input type="checkbox" class="m-auto"></td>'+
				'<td><input type="text" class="form-control input-sm m-default"/></td>'+
				"</tr>";
		});
	}

	return tableRow;
},
validateTableColumns : function (tableid) {
	
	var columns = [];
    var process = true;
    
    var tablerows = $("#"+tableid).find("tbody").find("tr").filter(function() {
    	return $(this).find('.m-check').is(':checked');
    });
    
    if (!tablerows || tablerows.length === 0) {
    	return false;
    }
    
  
    tablerows.each(function() {
    	var row = $(this);
    	var columnName = row.find('.m-column').val();
    	var dataType = row.find('.m-datatype').val();
    	var columnSize = row.find('.m-colsize').val();
    	var decimalPoints = row.find('.m-decimal').val();
    	var isPrimaryKey = row.find('.m-primary').is(":checked") ? true : false;
    	var isNotNull = row.find('.m-notnull').is(":checked") ? true : false;
    	var isUnique = row.find('.m-unique').is(":checked") ? true : false;
    	var isAutoIncrement = row.find('.m-auto').is(":checked") ? true : false;
    	var defaultValue = row.find('.m-default').val();
     
    	if(dataType == 'VARCHAR' || dataType == 'BIT') {
    		if(columnSize == '') {
    			alert(globalMessage['anvizent.package.label.lengthShouldNotBeEmptyforVARCHARBIT']);
    			process = false;
    			return false;
    		}
    	}
    	
    	if(dataType == 'DECIMAL' || dataType == 'FLOAT' || dataType == 'DOUBLE'){
    		columnSize  +=   "," + decimalPoints;
    	}
    	
    	var column = {
			 columnName : columnName,
	    	 dataType : dataType,
	    	 columnSize : columnSize,
	    	 decimalPoints : decimalPoints,
	    	 isPrimaryKey : isPrimaryKey,
	    	 isNotNull : isNotNull,
	    	 isUnique :isUnique,
	    	 isAutoIncrement : isAutoIncrement,
	    	 defaultValue : defaultValue
    	};
    	columns.push(column);
    });
    
	if (!process) {
		return process;
	}
	
	return columns;
},
validateTableOriginalColumns : function (tableid) {
	
	var columns = [];
    var process = true;
    
    var tablerows = $("#"+tableid).find("tbody").find("tr").filter(function() {
    	return $(this).find('.m-check').is(':checked');
    });
    
    if (!tablerows || tablerows.length === 0) {
    	alert(globalMessage['anvizent.package.label.selectColumnsToProcess']);
    	return false;
    }
    
    tablerows.each(function() {
    	var row = $(this);
    	var columnName = row.find('.originalColumnName').val();
    	var dataType = row.find('.m-datatype').val();
    	var columnSize = row.find('.m-colsize').val();
    	var decimalPoints = row.find('.m-decimal').val();
    	var isPrimaryKey = row.find('.m-primary').is(":checked") ? true : false;
    	var isNotNull = row.find('.m-notnull').is(":checked") ? true : false;
    	var isUnique = row.find('.m-unique').is(":checked") ? true : false;
    	var isAutoIncrement = row.find('.m-auto').is(":checked") ? true : false;
    	var defaultValue = row.find('.m-default').val();
    	    	
    	if(dataType == 'VARCHAR' || dataType == 'BIT') {
    		if(columnSize == '') {
    			alert(globalMessage['anvizent.package.label.lengthShouldNotBeEmptyforVARCHARBIT']);
    			process = false;
    			return false;
    		}
    	}
    	
    	columns.push(columnName);
    });
    
	if (!process) {
		return process;
	}
	
	return columns;
},

showTableCreationPopUP : function(object){
	$('.duplicateTargetTableName').empty();
	$("#targetTableCreationPopUp").find('.m-check-all').prop('checked', false);
	if(object != null){
		var coulmns = object.fileHeaders.split(",");
		var columnDataTypes=[];
		if(object.fileColumnDataTypes != null)
			columnDataTypes = object.fileColumnDataTypes.split(",");
		var table = $("#targetTable tbody");
		table.empty();
		var tableRow = customPackage.buildTableColumnPopup(coulmns,columnDataTypes);
		table.append(tableRow);
		table.find(".m-datatype").change();
		$("#targetTableNameDiv").show();
		$('#targetTableName_creation').val('');
		$("#targetTableCreationPopUp").modal('show');
	}
	
},
updateTargetTableStructureTable : function(result){
	var table = $("#targetTableStructure tbody");
	table.empty();
	var tableRow='';
	if(result.length>0){
		var i=0;
		$.each(result, function(key, obj) {
			var isPrimaryKey= obj["isPrimaryKey"];
			var isNotNull=obj["isNotNull"];
			var isAutoIncrement =obj["isAutoIncrement"];
			isPrimaryKey = isPrimaryKey == true ? '<span class="glyphicon glyphicon-ok"></span>' : '' ;
			isNotNull = isNotNull == false   ? '' : '<span class="glyphicon glyphicon-ok"></span>' ;
			isAutoIncrement = isAutoIncrement == true ? '<span class="glyphicon glyphicon-ok"></span>' : '';
			var columnName = obj["columnName"];
			var dataType = obj["dataType"];
			var columnSize =  "";
			for(var idx in dataTypeResult.object){
				if(dataTypeResult.object[idx].dataTypeName === dataType){
					columnSize = (dataTypeResult.object[idx].range == null|| dataTypeResult.object[idx].range == "") ? "" : obj["columnSize"];
			 }
			}
			tableRow +=  "<tr>"+
					"<td>"+(i+1)+"</td>"+
					"<td>"+columnName+"</td>"+
					"<td>"+dataType+"</td>"+
					"<td>"+columnSize+"</td>"+
					"<td>"+isPrimaryKey+"</td>"+
					"<td>"+isNotNull+"</td>"+
					"<td>"+isAutoIncrement+"</td>"+
					"<td>"+(obj["defaultValue"] || '')+"</td>"+
					"</tr>";
			i++;
		});
	}
	table.append(tableRow);
	$("#viewTargetTableStructurePopUp").modal('show');
},

getColumnDataType : function(dataTypeValue){
	var dataTypes = ["VARCHAR","INT","BIGINT","DECIMAL","BIT","DATETIME","DATE"];
	var colSize = "";
	var getColSize = {
			"VARCHAR" : "45",
			"INT" : "11",
			"BIGINT" : "20",
			"DECIMAL" :["24", "7"],
			"BIT" : "1",
			"DATETIME" : "19",	
			"DATE" : "0"
	};
	
	$.each(dataTypes, function(j,val){
		var selectDatatype = '<select class="form-control input-sm dataType m-datatype">';
		if(val == dataTypeValue){
			selectDatatype+="<option value='"+val+"' selected>"+val+"</option>";
			if(val == "DECIMAL"){
				colSize+= '<input type="text" class="input-sm m-colsize" value='+getColSize[val]+' style="width:50%;"/>'+
			  		  	  '<input type="text" class="input-sm m-decimal" value="7" style="width:50%;"/>';
			}
			else if(val == "BIT" || val == "DATETIME"){
				colSize+= '<input type="text" class="input-sm m-colsize" value='+getColSize[val]+' style="width:50%;" disabled="disabled"/>'+
	  		  	  '<input type="text" class="input-sm m-decimal" value="2" style="width:50%;"/>';
			}
			else{	colSize+= '<input type="text" class="input-sm m-colsize" value='+getColSize[val]+' style="width:50%;"/>'+
	  		  	  '<input type="text" class="input-sm m-decimal" value="2" style="width:50%;display:none;"/>';
			}
		}
		else{
			 var selected;
			 if(val == 'VARCHAR'){
				 selected = "selected"
			 }else{
				 selected = "";
			 }
			selectDatatype+="<option value='"+val+"'"+selected+">"+val+"</option>";
			if(dataTypeValue == "" || dataTypeValue == null){
				colSize = '<input type="text" class="input-sm m-colsize" value="45" style="width:50%;"/>'+
	  		  	  		  '<input type="text" class="input-sm m-decimal" value="2" style="width:50%;display:none;"/>';
			}
		}			
	});
	selectDatatype+='</select>';
},



getDataTypeList : function(){
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	headers[header] = token;
	var userID = $("#userID").val();
	
	var url_getDataTypesList = "/app/user/" + userID + "/getDataTypesList";
	showAjaxLoader(true); 				
	var myAjax = common.loadAjaxCall(url_getDataTypesList,'GET','',common.getcsrfHeader());
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
    				dataTypeResult = result;
    				customPackage.getDataTypes(result);
    			}		    			  		    			  	
	    	}
    	});
},

getDataTypes : function(result){
	debugger
	for(var i=0;i<result.object.length;i++){
		 var dataTypeName = result.object[i].dataTypeName;
		 var range = result.object[i].range;
		 var selected;
		 if(dataTypeName == 'VARCHAR'){
			 selected = "selected"
		 }else{
			 selected = "";
		 }
		 dataTypeSelect += "<option value='"+dataTypeName+"'"+selected+">"+dataTypeName+"</option>";
	 }
},

editTargetTableStructureTable : function(result){
	debugger
	var table = $("#targetTableStructure tbody");
	table.empty();
	var tableRow='';
	var colSize = "";
	var selectDatatype = "";
	if(result.length>0){
		var i=0,j=0;
		$.each(result, function(key, obj) {
			var isPrimaryKey= obj["isPrimaryKey"];
			var isNotNull=obj["isNotNull"];
			var isAutoIncrement =obj["isAutoIncrement"];
			isPrimaryKey = isPrimaryKey == true ? '<input type="checkbox" class="m-primary" checked="checked">' : '<input type="checkbox" class="m-primary">' ;
			isNotNull = isNotNull == false   ? '<input type="checkbox" class="m-notnull">' : '<input type="checkbox" class="m-notnull" checked="checked">' ;
			dataTypeValue = obj["dataType"];
			if(dataTypeValue == "BIGINT" || dataTypeValue == "INT" || dataTypeValue == "TINYINT" || dataTypeValue == "SMALLINT" || dataTypeValue == "MEDIUMINT"){
				isAutoIncrement = isAutoIncrement == true ? '<input type="checkbox" class="m-auto" checked="checked">' : '<input type="checkbox" class="m-auto">';
			}else{
				isAutoIncrement = isAutoIncrement == true ? '<input type="checkbox" class="m-auto" checked="checked">' : '<input type="checkbox" class="m-auto" disabled="disabled">';
			}
			
			var colSize = "";	
			var defaultVal = obj["defaultValue"];
			 
			selectDatatype = '<select class="form-control input-sm dataType m-datatype" style="width:100%;">';
			
			for(var i=0;i<dataTypeResult.object.length;i++){
			 
				 var dataTypeName = dataTypeResult.object[i].dataTypeName;
				 var range = dataTypeResult.object[i].range;
				 var columnSize = ( range == null || range == "" ) ? "" : obj["columnSize"];
				 if(dataTypeName == dataTypeValue){
					 selectDatatype+="<option value='"+dataTypeName+"' selected>"+dataTypeName+"</option>"; 
					 if(dataTypeValue == "DECIMAL" || dataTypeValue == "FLOAT" || dataTypeValue == "DOUBLE"){
							var columnSizeDecimal = obj["columnSize"].split(",");
							colSize+= '<input type="text" class="input-sm m-colsize" value='+columnSizeDecimal[0]+' style="width:50%;"/>'+
						  		  	  '<input type="text" class="input-sm m-decimal" value='+columnSizeDecimal[1]+' style="width:50%;"/>';
					}else{
						   colSize = '<input type="text" class="input-sm m-colsize" value="'+columnSize+'" style="width:50%;"/>'+
				          '<input type="text" class="input-sm m-decimal" value="2" style="width:50%;display:none;"/>';
					}
					 
				 }else{
					 var selected;
					 if(dataTypeName == 'VARCHAR'){
						 selected = "selected"
					 }else{
						 selected = "";
					 }
					 selectDatatype+="<option value='"+dataTypeName+"'"+selected+">"+dataTypeName+"</option>";
						if(dataTypeValue == "" || dataTypeValue == null){
							colSize = '<input type="text" class="input-sm m-colsize" value="45" style="width:50%;"/>'+
				  		  	  		  '<input type="text" class="input-sm m-decimal" value="2" style="width:50%;display:none;"/>';
						}
				 }
			 }
			
			selectDatatype += '</select>'
		
	     if(defaultVal != null){
	    	 if(obj["dataType"] == 'DATETIME'){
	 			defaultVal ="<input type='text' class='form-control m-default' placeholder='"+globalMessage['anvizent.package.label.yyyyMMdd']+"'>";
	 	     }else{
	 	    	defaultVal ="<input type='text' class='form-control m-default' value='"+defaultVal+"'>";
	 	     }
	     }else{
	    	 defaultVal ="<input type='text' class='form-control m-default'>";
	     }
			tableRow +=  "<tr>"+
					"<td>"+(j+1)+"</td>"+
					"<td class='m-column'>"+obj["columnName"]+"</td>"+
					"<td>"+selectDatatype+"</td>"+
					"<td>"+colSize+"</td>"+
					"<td class='primaryKey'>"+isPrimaryKey+"</td>"+
					"<td class='notNull'>"+isNotNull+"</td>"+
					"<td class='autoIncrement'>"+isAutoIncrement+"</td>"+
					"<td>"+defaultVal+"</td>"+
					"</tr>";
			j++;
		});
	}
	table.append(tableRow);
	$("#viewTargetTableStructurePopUp").modal('show');
},
getTargetTableDirectMappingInfo : function(flag){ 
	var packageId = $("#packageId").val();
	var userID = $("#userID").val();
	//get the status of files having same column names
	var url_getPackageById = "/app/user/"+userID+"/package/getPackagesById/"+packageId+"";
	var myAjax1 = common.loadAjaxCall(url_getPackageById,'GET','',headers);
	   myAjax1.done(function(result1) {
		   
    	  if(result1 != null) {
    		  var tableId = null;
    		  
    		  if(result1.table) {
    			  tableId = result1.table.tableId;
    		  }
    		  var url_getFileInfoByPackage = "/app/user/"+userID+"/package/getILsConnectionMappingInfoByPackage/"+packageId+"";
    			var myAjax = common.loadAjaxCall(url_getFileInfoByPackage,'GET','',headers);
    			
    			   myAjax.done(function(result) {
    				   console.log(":::: results::::: "+result);
    		    	  if(result != null && result.hasMessages) {
    		    		  if(result.messages[0].code == "SUCCESS"){
    		    			  var table = $("#targetTableDirectMappingInfoTable tbody");
    		    			  table.empty();
    		    			  if(result.object.length > 0){
    		    				 $("#targetTableDirectMappingInfoTable").show(); 
    		    			  }else{
    		    				  $("#targetTableDirectMappingInfoTable").hide();
    		    				  flag = true;
    		    			  }
    		    			  
    	    				  var tableRow='';
    	    				  for(var i=0; i<result.object.length; i++){
    	    					  debugger
					    			  tableRow +=  "<tr>"+
										"<td>"+(i+1)+"</td>";    							
					    			  if(result.object[i].ilConnectionMapping.isFlatFile){
					    				  tableRow += "<td>"+globalMessage['anvizent.package.label.flatFile']+"</td>";
					    				  tableRow += "<td>"+result.object[i].ilConnectionMapping.filePath+"</td>";
					    				  tableRow +=  "<td class='smalltd'><a class='btn btn-primary btn-sm startLoader' target='_blank' href='"+adt.appContextPath+"/adt/package/viewCustomPackageSource/"+packageId+"/"+result.object[i].ilConnectionMapping.connectionMappingId+"'>"+globalMessage['anvizent.package.label.viewDetails']+"</a></td>";
					    			  
					    			  }else{
					    				  var tableName = "";
					    				  tableRow += "<td>"+globalMessage['anvizent.package.label.database']+"</td>";    		    				  
					    				  if(result.object[i].ilConnectionMapping.parent_table_name == null){
					    					  tableRow += "<td>N/A</td>";
					    					  tableRow +=  "<td class='smalltd'><a class='btn btn-primary btn-sm startLoader' href='"+adt.appContextPath+"/adt/package/viewCustomPackageSource/"+packageId+"/"+result.object[i].ilConnectionMapping.connectionMappingId+"'>"+globalMessage['anvizent.package.label.viewDetails']+" </a></td>";  
					    				  }
					    				  else{
					    					  tableRow += "<td>"+result.object[i].ilConnectionMapping.parent_table_name+"</td>";
					    					  tableRow +=  '<td class="smalltd"><input type="button" class="btn btn-primary btn-sm viewDerivedTableStructure" value="'+globalMessage['anvizent.package.button.viewTableStructure']+'"/></td>';
					    				  }
					    				  }
											tableRow += '<td class="smalltd"><button type="button" class="btn btn-primary btn-sm delete-mapping" data-id="'+result.object[i].ilConnectionMapping.connectionMappingId+'"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button></td>';
										tableRow +=  "</tr>"
    	    					}
    	    				table.append(tableRow);
    		    			$("#targetTableSourceInfoDiv").show();
    		    			if(flag){
    		    				$("#proceedForMapping").hide();
    		    			}else{
    		    				$("#proceedForMapping").show();
    		    			}
    		    	  }else{
    		    		  common.displayMessages(result.messages);
    		    	  }
    		    	  }else {
    		    		  $("#targetTableSourceInfoDiv").hide();
    		    		  $("#proceedForMapping").hide();
    		    	  }
    			   });
    	  }
	   });
	
},
clearFlatFileDatabaseValues: function() {
	$("#file_direct").val("");
},

viewTagetTableQuery: function(object) {
	
	var targetTable = $("#targetTableName").val();
	$("#viewTargetTableQueryHeader").text(targetTable);
	$("#targetTableQuery").val(object);
	
	$("#viewTargetTableQueryPopUp").modal('show');
},

viewDerivedTableQuery: function(object,derived_table_name) {
	
	 
	$("#viewDerivedTableQueryHeader").text(derived_table_name);
	$("#derivedTableQuery").val(object);
	
	if(object === "" || object === null){
		$("#derivedTableQuery").val("No Query Found.");
	}
	
	$("#viewDerivedTableQueryPopUp").modal('show');
},

validateTargetTableFields : function(){
	var message = globalMessage['anvizent.package.message.invalidData'];
	var returnVal = true;
	common.clearcustomsg(".m-default");
	$("#targetTable tbody tr td").find(".m-check:checked").each(function(){		
			var this_m_colsize = $(this).parents("tr").find(".m-colsize");
		
			var thisColValLength = $(this_m_colsize).val();
			var thisColDefaultElement = $(this_m_colsize).parents("tr").find(".m-default");
			var thisColDefaultVal = $(thisColDefaultElement).val().trim();
			var thisColDefaultValLength = thisColDefaultVal.length;
			var thisColDataType = $(this_m_colsize).parents("tr").find(".m-datatype option:selected").val();
			var thisColDecimalValElement = $(this_m_colsize).parents("tr").find(".m-decimal");
			var thisColDecimalVal = $(this_m_colsize).parents("tr").find(".m-decimal").val().trim();
			
			if(thisColDefaultValLength > thisColValLength && thisColDataType != "DECIMAL"){				
				common.showcustommsg(thisColDefaultElement, globalMessage['anvizent.package.message.defaultValueLengthIsExceeded']);
		    	returnVal = false;								
			}
			else{
				if(thisColDefaultVal != ""){
					if(thisColDataType == "INT" || thisColDataType == "BIGINT"){
						if(!thisColDefaultVal.match("^[-]?[0-9]+$")){
							common.showcustommsg(thisColDefaultElement, message);
							returnVal = false;
						}
					}
					else if(thisColDataType == "DECIMAL"){												 
						if(!thisColDefaultVal.match("^([-]?\\d*\\.\\d*)$")){							
							common.showcustommsg(thisColDefaultElement, message);
							returnVal = false;
						}else{
							var floatVal = thisColDefaultVal.split(".");
							var fractionPartLength = floatVal[0].length;
							var decimalPartLength = floatVal[1].length;
							if(fractionPartLength > thisColValLength){
								common.showcustommsg(thisColDefaultElement, globalMessage['anvizent.package.message.defaultValueLengthIsExceeded']);
								returnVal = false;	
							}
							else if(decimalPartLength > thisColDecimalVal){
								common.showcustommsg(thisColDefaultElement, globalMessage['anvizent.package.message.defaultValueLengthIsExceeded']);
								returnVal = false;	
							}
						}
					}
					else if(thisColDataType == "BIT"){
						if(!thisColDefaultVal.match("^[0-1]{1}$")){
							common.showcustommsg(thisColDefaultElement, message);
							returnVal = false;
						}
					}
					else if(thisColDataType == "DATETIME"){
						var regEx = /^\d{4}\-(0?[1-9]|1[012])\-(0?[1-9]|[12][0-9]|3[01])$/;
						if(!thisColDefaultVal.match(regEx)){
							common.showcustommsg(thisColDefaultElement, message);
							returnVal = false;
						}
					}
					else if(thisColDataType == "DATE"){
						var regEx = /([0-9]{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$/;
						if(!thisColDefaultVal.match(regEx)){
							common.showcustommsg(thisColDefaultElement, message);
							returnVal = false;
						}
					}
				}
			}			
	});
	return returnVal;
},
	validateDateForamtAndTimeZone : function() {
		var timeZone = $("#timesZone").val();
		var validate = true;
		if (timesZone == '') {
			common.showcustommsg("#timesZone", "please select time zone", "#timesZone");
			validate = false;
		}
		return validate;
},

buildAlterQuery : function(tableName){
 
	var alterQuery = '';
	var allColumnsList = [];
	var changeQueryColumns = '';
	var quoteChar = '`';
	var primaryKey ='';
	var count = 0;	
	var primaryVal = [];
	var tableLength = $("#targetTableStructure tbody tr").length
	var primaryKeys = [];
	var isPrimaryExist = false;
	for (var i=0; i<resultOfTargetTable.length; i++) {
			var isPrimaryKey = resultOfTargetTable[i].isPrimaryKey;
			if(isPrimaryKey == true){
				isPrimaryExist = true;
				break;
			}
	}
	
	alterQuery = "ALTER TABLE "+quoteChar +tableName+ quoteChar;
	isPrimaryExist ? primaryKey = "DROP PRIMARY KEY, ADD PRIMARY KEY (" : primaryKey = "  ADD PRIMARY KEY (" ;
	var primaryKeyDropQueryStatus = true;
	$("#targetTableStructure tbody tr").each(function(){
		var row = $(this);
		var dataType = '';
		var columnSize = '';
		var decimalPoints = '';
		var columnName = row.find(".m-column").text();
		var defaultValue = '';
		var isPrimaryKey = '';
    	var isNotNull = '';
    	var isAutoIncrement = '';
    	var defaultValue = '';
		    dataType = row.find('.m-datatype').val();
			decimalPoints = row.find('.m-decimal').val();
		 	isPrimaryKey = row.find('.m-primary').is(":checked") ? true : false;
	    	isNotNull = row.find('.m-notnull').is(":checked") ? true : false;
	    	isAutoIncrement = row.find('.m-auto').is(":checked") ? true : false;
	    	defaultValue = row.find('.m-default').val();
	    	if(dataType == 'DECIMAL' || dataType == 'FLOAT' || dataType == 'DOUBLE'){
				columnSize = row.find('.m-colsize').val() + "," +decimalPoints;
			}else{
				columnSize = row.find('.m-colsize').val();
			}
	    	debugger
	    	var datatypeValue = '';
	    	if(columnSize != ""){
	    		datatypeValue = dataType + "(" +columnSize+ ")";
	    	}else{
	    		datatypeValue = dataType;
	    	}
			if(defaultValue != '' && defaultValue != null){
				var value = isNotNull ? " NOT NULL ":" NULL ";
				changeQueryColumns += " CHANGE COLUMN " + quoteChar +columnName+ quoteChar +" "+ quoteChar +columnName+ quoteChar + " " + datatypeValue+ " " + value + " DEFAULT "+ "'"+defaultValue +"'" ;
			}else{
				changeQueryColumns += " CHANGE COLUMN " + quoteChar +columnName+ quoteChar +" "+ quoteChar +columnName+ quoteChar+ " " + datatypeValue;
				isNotNull ? changeQueryColumns += " NOT NULL": changeQueryColumns += " NULL ";
				if(dataType == "INT" || dataType == "BIGINT" || dataType == "TINYINT" || dataType == "SMALLINT" || dataType == "MEDIUMINT"){
					isAutoIncrement ? changeQueryColumns += " AUTO_INCREMENT ":"";
				}
				
			}
			if(isPrimaryKey){
				if(isAutoIncrement){
					 primaryVal.splice(0, 0, quoteChar +columnName+ quoteChar);
				 }else{
					 primaryVal.push(quoteChar +columnName+ quoteChar);
				 }
			}
			if(count == tableLength-1){
				if(primaryVal.length != 0){
					primaryKey += primaryVal.join(',') + ")";
					changeQueryColumns += "," + primaryKey;
				}else if(primaryKeyDropQueryStatus && isPrimaryExist){
					changeQueryColumns += ", DROP PRIMARY KEY";
					primaryKeyDropQueryStatus = false;
				}
				changeQueryColumns += ";";
			}else{
				changeQueryColumns += ","
			}
		
		count++;
	});
	
	alterQuery += changeQueryColumns;
	if(changeQueryColumns.length != 0){
		return alterQuery;			
	}
},

checkNewColumnSize : function(){
	debugger
	var status = false;
	var targetTable = $("#targetTableStructure tbody tr");
	$("#targetTableStructure tbody tr").each(function(){
		var row = $(this);
		var columnSize = row.find('.m-colsize').val();
	});
	
	
	$("#targetTableStructure tbody tr").each(function(index, value ){
		var row = $(this);
		var newcolumnSize = row.find('.m-colsize').val();
		for(var j=index;j==index;j++){
			var existedColumnSize = resultOfTargetTable[j].columnSize;
			if(parseInt(newcolumnSize) < parseInt(existedColumnSize)){
				status = true;
				break;
			}
		}
	});
	return status;
},

saveEditedTargetTable : function(tableName){
	var userID = $("#userID").val();
	var mappedQuery = customPackage.buildAlterQuery(tableName);
	var selectData = {
			iLquery :mappedQuery
	} 
	showAjaxLoader(true);
    var url_editTargetMappedQuery = "/app/user/"+userID+"/package/editTargetMappedQuery";
	 var myAjax = common.postAjaxCall(url_editTargetMappedQuery,'POST', selectData,headers);
	    myAjax.done(function(result) {
	    	showAjaxLoader(false);
	    	  if(result != null && result.hasMessages){
	    			  if(result.messages[0].code == "ERROR") {
			    			  common.showErrorAlert(result.messages[0].text);
		    				  return false;
		    			  }
	    			  if(result.messages[0].code == "SUCCESS") {
	    				  common.showSuccessAlert(result.messages[0].text);
	    				  $("#viewTargetTableStructurePopUp").modal('hide');
	    				  resultOfTargetTable ='';
	    				  return true;
	    			  }
	    	  }else{
		    		var messages = [ {
		    			code : globalMessage['anvizent.message.error.code'],
		    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
		    		} ];
		    		common.displayMessages(messages);
		    	}
	    });
},

};

if($('.customPackage-page').length){
	customPackage.initialPage();
	
	var mappingId = "";
	$("#targetTableDirectMappingInfoTable").on('click', 'button.delete-mapping', function(){

		$("#deleSourceFileAlertMessage").empty();
		$("#deleSourceFileAlertMessage").hide();
		if ( $("#targetTableId").val() ) {
			if ( $('input[type=radio][name="isFileHavingSameColumnNames"]:checked').val() == "false" || $("#targetTableDirectMappingInfoTable tbody tr").length == 1) {
				$("#deleSourceFileAlertMessage").html("<b>Note:</b> <br />Target Table already created for this package. If you delete the source, all mappings will be destroyed.");
				$("#deleSourceFileAlertMessage").show();
			} 
		}
		
		mappingId = $(this).attr("data-id");
		$("#deleSourceFileAlert").modal('show');
	});
	
	$("#confirmDeleteSource").on('click', function() {
		$("#deleSourceFileAlert").modal('hide');
		var packageId = $("#packageId").val();
		var userID = $("#userID").val();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url_getPackageById = "/app/user/"+userID+"/package/deleteConnectionMapping/"+packageId+"/"+mappingId;
		showAjaxLoader(true);
		var myAjax = common.loadAjaxCall(url_getPackageById,'POST','',headers);
		myAjax.done(function(result) {
			showAjaxLoader(false);
			if(result != null && result.hasMessages){
				if(result.messages[0].code == "ERROR") {
					 var  messages=[{
						  code : result.messages[0].code,
						  text : result.messages[0].text
					  }];
					  common.showErrorAlert(result.messages[0].text);
					  return false;
  			  	}else{
	  			  	if (result.object) {
	  					if ($("#targetTableId").val()) {
	  						if ( $('input[type=radio][name="isFileHavingSameColumnNames"]:checked').val() == "false" || $("#targetTableDirectMappingInfoTable tbody tr").length == 1) {
	  							window.location = adt.appContextPath+"/adt/package/customPackage/edit/"+packageId;
	  						}
	  					}
	  					customPackage.getTargetTableDirectMappingInfo();
	  					common.showSuccessAlert(globalMessage['anvizent.package.label.sourceDeletedSuccessfully']);
	  				}
	  				else { 
	  					common.showErrorAlert(globalMessage['anvizent.package.label.sourceDeletionFailed']);
	  				}
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
	
	$("#createNewSchema").click(function(){
		
		$("input[type=radio][name='isFileHavingSameColumnNames']").prop('checked', false);
		$("#existingSchemaSelectDiv").hide();
		$("#mappingTableModule").hide();
		$("#YesOrNoOptionDiv").show();
		
		
	});
	
	$("#manualMapping").click(function(){
		var packageId = $("#packageId").val();
		var industryId = $("#industryId").val();
		window.location = adt.appContextPath+"/adt/package/queryBuilder/"+packageId + "/"+industryId;
	});
	
	$("#proceed").click(function(){
		
		
		var packageId = $("#packageId").val();
		var userID = $("#userID").val();
		var isEveryFileHavingSameColumnNames = "";
		
		if($('input[type=radio][name="isFileHavingSameColumnNames"]').is(':checked')) {
			isEveryFileHavingSameColumnNames =	$('input[type=radio][name="isFileHavingSameColumnNames"]:checked').val()
		}else{
			alert(globalMessage['anvizent.package.label.selectYesOrNo']);
			return false;
		}
		//save the status
		//updateFileHavingSameColumns field
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url_updateFilesHavingSameColumns = "/app/user/"+userID+"/package/updateFileHavingSameColumns/"+packageId+"/"+isEveryFileHavingSameColumnNames;
		var myAjax = common.loadAjaxCall(url_updateFilesHavingSameColumns,'POST','',headers);
		   myAjax.done(function(result) {
	    	  if(result != null && result.object == 1 && result.hasMessages) {
	    		  if (result.messages[0].code == "SUCCESS") {
		    		     $("#isFileHavingSameColumnNames").val(isEveryFileHavingSameColumnNames);
			    		 $("#primaryOptionsDiv").hide();
			    		 $("#proceed").hide();
		    		 if(isEveryFileHavingSameColumnNames== "true"){
		    			 $("#YesOrNoOption").text(globalMessage['anvizent.package.button.yes']);
		    		 }else{
		    			 $("#YesOrNoOption").text(globalMessage['anvizent.package.button.no']);
		    		 }
			    		 $("#YesOrNoOption").show();
			    		 $("#isFileHavingSameColumnNamesDiv").hide();
			    		$("#targetTable_source_options").hide();
			    		$("#YesOrNoOptionDiv .help-block").hide();
			    		$("#targetTableSourceDiv").show();
			    		$("#editAllSourcesHavingSameSetOfHeaders").show();
			    		$("#undoAllSourcesHavingSameSetOfHeaders").hide();
			    		$("#proceedForMapping").attr("disabled",false);
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
	})
	
	$("#proceedForMapping").click(function(){
		
		var packageId = $("#packageId").val();
		var userID = $("#userID").val();
		var isEveryFileHavingSameColumnNames = $('input[type=radio][name="isFileHavingSameColumnNames"]:checked').val(); 
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		if(isEveryFileHavingSameColumnNames === "true") {
			 showAjaxLoader(true);
			var url_getFileInfoByPackage = "/app/user/"+userID+"/package/getColumnHeaders/"+packageId+"";
			var myAjax = common.loadAjaxCall(url_getFileInfoByPackage,'GET','',headers);
			//check every file having same column names
			
			   myAjax.done(function(result) {
				   showAjaxLoader(false);
		    	  if(result != null && result.hasMessages) {
	    			  if(result.messages[0].code == 'ERROR'){
	    				  common.displayMessages(result.messages)
	    				  return false;
	    			  }
	    			  else{
			    		  customPackage.showTableCreationPopUP(result.object);
	    			  }
		    	  }
		    	  $("#proceed").hide();
		    	  $("#editAllSourcesHavingSameSetOfHeaders").show();
		    	  $("#undoAllSourcesHavingSameSetOfHeaders").hide();
			   });
		}else {
			var packageId = $("#packageId").val();
			var industryId = $("#industryId").val();
		    var userID = $("#userID").val();
		    showAjaxLoader(true);
			
		    var url_uploadingFilesFromClientDatabase = "/app/user/"+userID+"/package/uploadingFilesFromClientDatabaseForNoOption/"+packageId;
		    var myAjax1 = common.loadAjaxCall(url_uploadingFilesFromClientDatabase,'GET','',headers);
			myAjax1.done(function(result) {
				if(result.hasMessages){
					for(var i=0; i < result.messages.length; i++){
						if (result.messages[i].code === "ERROR") {
							common.showErrorAlert(result.messages[i].text);
							showAjaxLoader(false);
							return false;
						}
					}
					 					
				}else{
					var url_saveMappingTableInfo = "/app/user/"+userID+"/package/saveMultipleTablesMappingInfo/"+packageId+"/"+industryId;
				    var myAjax = common.postAjaxCall(url_saveMappingTableInfo,'POST','',headers);
				    
				    // to show loader.
					myAjax.done(function(result1) {
						showAjaxLoader(false);
						
					if(result1 != null && result1.hasMessages){
						if (result1.messages[0].code === "SUCCESS") {
							setTimeout(function() {
								window.location = adt.appContextPath+"/adt/package/queryBuilder/"+packageId+"/"+industryId;
							}, 50);
						}else{
							common.displayMessages(result1.messages);
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
		}
		
	});
	
	$("#selectFileHeadersTable").on('click', '.file-headers', function() {
		var row = $(this).closest('tr'), name = row.find('.file-name').text();
		var fileid = this.value;
		$("#mappingTableMultiple").removeData('fileid').find('tbody').find('tr').remove();
		$("#mappingTableMultiple").find('input[type=checkbox]').prop('checked', false);
		var packageId = $("#packageId").val();
		var userID = $("#userID").val();
		
		var url_getFileInfoByPackage = "/app/user/"+userID+"/package/getFilesInfoByPackage/"+packageId+"";
		var myAjax = common.loadAjaxCall(url_getFileInfoByPackage,'GET','',headers);
		
		myAjax.done(function(result) {
			var files = result.object;
			var length = files.length;
			var file;
			for (var i=0; i<length; i++) {
				file = files[i];
				if (name === file.fileName) {
					break;
				}
			}
			if (file) {
				var headers = file.fileHeaders;
				
				headers = headers.split(',');
				var rows = customPackage.buildTableColumnPopup(headers);
				table.find(".m-datatype").change();
				$("#mappingTableMultiple").data('fileid', fileid).find('tbody').append(rows);
				$("#mappingTableCreationPopUp").modal('show');
				
			}
			
		});
	});
	
	
	
	$("#mappingTableCreation").on('click' ,function() {
		
		var packageId = $("#packageId").val();
		var industryId = $("#industryId").val();
	    var userID = $("#userID").val();
		var columns = customPackage.validateTableColumns('mappingTableMultiple');
		
		var fileid = $("#mappingTableMultiple").data('fileid');
		
		 if (!columns) {
	    		return false;
	    	}
		    
		    var selectData = {
	    		userId : userID,
	    		userPackage : {
	    			packageId :packageId,
	    			industry : {
	    				id : industryId
	    			},
	    			table : {
	    				columns : columns
	    			}
	    		}
		    };
		    
		    var url_saveMappingTableInfo = "/app/user/"+userID+"/package/saveMultipleTableMappingInfo/"+fileid;
		    var myAjax = common.postAjaxCall(url_saveMappingTableInfo,'POST', selectData,headers);
		    
		    
		    myAjax.done(function(result) {
		    	console.log(result);
		    });
		    
		    
		    
	});
	
	$(document).on('click', '#useExistingSchema', function(){
		$("#TargetTableModule").hide();
		$("#existingSchemaSelectDiv").show();
		$("#targetTable_source_options").hide();
		$("#proceedForMappingDiv").hide();
		
		var packageId = $("#packageId").val();
	    var userID = $("#userID").val();
	    var url_getAllTargetTablesOfCustomPackage = "/app/user/"+userID+"/package/getAllTargetTableSOfCustomPackage/"+packageId+"";
		if(packageId != null){
		   var myAjax = common.loadAjaxCall(url_getAllTargetTablesOfCustomPackage,'GET','',headers);
		   myAjax.done(function(result) {
		    	  if(result != null && result.hasMessages){
		    		  if(result.messages[0].code == "SUCCESS"){
		    		  	customPackage.updateExistingSchemas(result.object);
		    	   }else {
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
		}
	    

	});
	
	$("#addSource").click(function(){
		debugger
			$("#targetTableDirectPopUpHeader").val($('.targetTableName').val());
			$("#file_direct").val("");
		    $("#saveILConnectionMapping").hide();
			$("#flatFilesLocationDetails").hide();
			$("#databaseConnectionDetails").hide();
			$("input[type=radio][name=typeSelection]").prop("checked",false);
			$("#targetTableDirectMappingPopUp").modal('show');
			$('#targetTableDirectMappingPopUp').addClass('dynamic').css('top',10);
	});
	
	$("#derivedTableAddSource").click(function(){
		if ( $('input[type=radio][name="isFileHavingSameColumnNames"]:checked').val() == "false" && $("#targetTableId").val() ) {
			$("#derivedTableAddSourceAlert").modal('show');
			showAjaxLoader(false);
			$("#derivedTableDirectPopUpHeader").val($('.targetTableName').val());
		} else {
			var packageId = $("#packageId").val();
			var industryId = $("#industryId").val();
			window.location = adt.appContextPath+"/adt/package/derivedTable/edit/"+packageId; 
		}		
	});
	
	$(document).on('click', '#flatFiles', function(){
		common.clearValidations(["#delimeter_direct", "input:radio[name='isFirstRowHasColumnNames']", "#firstrowcolsvalidation", "#file_direct"]);
		$("#file_direct").val("");
		 $("#databaseConnectionDetails").hide();
	    $("#databaseConnectionPanel").hide();
	    $(".IL_queryCommand").hide();
	    $("#flatFilesLocationDetails").show();
	    $("#flatFilesLocationPanel").show();
	    $("#saveAndUpload_direct").show();
	    $("#saveILConnectionMapping").hide();
	    
	});
	$(document).on('click', '#database', function(){
		$("#deleteDatabaseTypeConnection").hide();
		$("#flatFilesLocationDetails").hide();
		$("#saveButtons").hide();
		$("#databaseConnectionPanel").hide();
	    $("#flatFilesLocationPanel").hide();
	    $("#existingConnections").val("");
	    $("#databaseConnectionDetails").show();
	    $("#saveAndUpload_direct").hide();
	    databaseConnection.getILDatabaseConnections('fromSpOrCp');
	    
	    common.clearValidations(['#IL_database_connectionName', '#IL_database_serverName', '#IL_database_username', '#IL_database_password']);
	});
	
	$(document).on('click', '#createNewConnection_dataBaseType', function(){
		databaseConnection.resetConnection();
		 $("#addDBVarDiv").show();
		 $("#database_queryParam").removeAttr("disabled");
		 $(".db-variables-add-div").removeClass("hidden");
	     $("#dbVariablesTbl tbody").empty();
		 $("#deleteDatabaseTypeConnection").hide();
		 $(".buildQuery").hide();
		 $("#saveILConnectionMapping").hide();
		 $(".IL_queryCommand").hide();
		 $("#checkQuerySyntax").hide();
		 $(".queryValidatemessageDiv").empty();
		 $("#saveNewConnection_dataBaseType").show();
		 $("#testConnection").show();
		 $("#IL_database_password_div").show();
	     $("#databaseConnectionPanel").show();
	     $("#checkTablePreview").hide();
	 	 $("#IL_database_databaseType").change();
	 	 $(".servername-div").show();
	 	 var protocal = $("#IL_database_databaseType option:selected").data("protocal");
		 	if(protocal.indexOf("mysql")!=-1){
 	    	$("#sslEnable").attr("checked",false);
 	    	$("#mysqlSslCertificateFileNamesDiv").hide();
 	    	$("#mysqlSslCertificateFilesDiv").hide();
 	    	$("#sslEnableDiv").show();
 	    }
	}); 
	$("#addDBVarDiv").on("click",function(){
    	databaseConnection.addDBVariableDiv(this);
	});

    $(document).on("click",".addDbVariablePairDiv",function(){
		databaseConnection.addDbVariablePair(this);
	});

	$(document).on("click",".deleteDbVariablePairDiv",function(){
		databaseConnection.deleteDbVariablePair(this);
	});
		
	//delete Database Type Connection 
	$(document).on('click', '#deleteDatabaseTypeConnection', function(){
		$("#deleteIlConnection").modal('show');
	});
	$(document).on('click', '#confirmDeleteIlConnection', function(){
		var userID = $("#userID").val();
		var connectionId=$('#existingConnections').val();
		var connectionName=$("#existingConnections option:selected").text();
		common.clearValidations(['#existingConnections']);
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		
		var url_deleteILConnection = "/app/user/"+userID+"/package/deleteILConnection/"+connectionId;
		 var myAjax = common.postAjaxCall(url_deleteILConnection,'POST','',headers);
 	      myAjax.done(function(result) {
 	    	  if(result != null && result.hasMessages){ 
	    			  if(result.messages[0].code == "ERROR") {
	    				  $('.message').show();
	    				  $('.messageText').empty();
		    			  $(".messageText").append(result.messages[0].text);
		    			  setTimeout(function() { $(".message").hide().empty(); }, 10000);
		    			  return false; 
		    			  } else if(result.messages[0].code == "SUCCESS") {
		    				  customPackage.showSuccessMessage(connectionName+" "+result.messages[0].text, true, 5000);
		    				  databaseConnection.getILDatabaseConnections('fromSpOrCp');
			    			  $('#databaseConnectionPanel').hide();
			    			  $('#deleteDatabaseTypeConnection').hide();
			    			  $("#deleteIlConnection").modal('hide');
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
	$(document).on('click', '#closeDeleteConnectionMapping', function() {
		$("#deleteIlConnection").modal('hide');
		 $('#targetTableDirectMappingPopUp').css('overflow-y','scroll');
	    });
	$(document).on('click', '#closeDeleteConnection', function() {
		$("#deleteIlConnection").modal('hide');
		 $('#targetTableDirectMappingPopUp').css('overflow-y','scroll');
	    });
	
	$(document).on('change', '#IL_database_databaseType', function(){
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
	
	
	$(document).on('click', '#saveNewConnection_dataBaseType,#testConnection', function(){
		   var elementId = this.id;
		   var IL_database_connectionName = $("#IL_database_connectionName").val().trim();
		   var IL_database_databaseType = $("#IL_database_databaseType").val();
		   var IL_database_databaseName = $("#IL_database_databaseType option:selected").text();
		   var IL_database_connectionType = $("#IL_database_connectionType").val();
		   var IL_database_serverName = $("#IL_database_serverName").val();
		   var IL_database_username = $("#IL_database_username").val();
		   var IL_database_password = $("#IL_database_password").val();
		   var userID = $("#userID").val();
		   var connector_Id = $("#IL_database_databaseType option:selected").attr("data-connectorId");
		   var protocal = $("#IL_database_databaseType option:selected").data("protocal");
		   var dataSourceName=  $("#dataSourceName").val();
	       var dataSourceOther = $("#dataSourceOtherName").val();
		   common.clearValidations(['#IL_database_connectionName', '#IL_database_serverName', '#IL_database_username', '#IL_database_password',"#dataSourceOtherName","#dateFormat","#timesZone","#dataSourceName"]);
		   var dateFormat = $("#dateFormat").val();
	       var timeZone = $("#timesZone").val();
	       
		   var selectors = [];
		   selectors.push('#IL_database_connectionName');
           selectors.push('#dataSourceName');
		   
		   if ( $("#dataSourceName").val() == "-1") {
			   selectors.push('#dataSourceOtherName');
		   }
		   var valid = common.validate(selectors);
		   
		   if (!valid) {
			   return false;
		   }
		   if(elementId == "saveNewConnection_dataBaseType" || elementId == "updateConnection"){
	           	var validate =  customPackage.validateDateForamtAndTimeZone();
	           	  if (!validate) {
	      	           return false;
	      	        }
		       }
		   
		  	var paramObject={};
		    var connectionStringParam = {};
		   	var _tblObj = $("#requestParamsTable tbody tr");
		   	var queryParams=$("#database_queryParam").val();
		  
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
			var dbVariableDiv = $("#dbVariablesTbl tbody tr.clonedDbVariable");
			$(dbVariableDiv).each(function(){
				var dbVarKey = $(this).find(".dbVarKey").val().trim();
				var dbVarValue = $(this).find(".dbVarValue").val().trim();
				 	if(dbVarKey != '' && dbVarValue != '') {
				   		var startsWith = dbVarKey.startsWith("{")
				   	    var endsWith = dbVarKey.endsWith("}") 
				   	    common.clearValidations([$(this).find(".dbVarKey")]);
				   		if(!startsWith && !endsWith){
							common.showcustommsg($(this).find(".dbVarKey"),"db variable key start with '{' and ends with '}'.");
							validStatus = false;
				   			}
				   		dbVarKayValuePair.push({"key":dbVarKey,"value":dbVarValue})	
						console.log(dbVarKayValuePair);
				   		}
				
			});
			  if(!validStatus){
				 	return validStatus;
			   }
			var serverInfo = "";
			if (isConnectionWithPlaceHolder) {
				serverInfo = JSON.stringify(connectionStringParam);
			} else {
				serverInfo = IL_database_serverName;
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
					   id: IL_database_databaseType,
					   name : IL_database_databaseName
				   },
				   connectionType : IL_database_connectionType,
				   server : serverInfo,
				   username : IL_database_username,
				   password : IL_database_password,
				   connectionName : IL_database_connectionName,
				   dateFormat:dateFormat,
	           	   timeZone:timeZone,
	               dataSourceName:dataSourceName,
	           	   dataSourceNameOther:dataSourceOther,
	               dbVariables : JSON.stringify(dbVarKayValuePair),
	               sslEnable:sslEnable
		   };

		   var token = $("meta[name='_csrf']").attr("content");
		   var header = $("meta[name='_csrf_header']").attr("content");
		   headers[header] = token;
		   
		   databaseConnection.testAndSaveDbConnection(elementId,headers,selectData,dataSourceOther,sslEnable,IL_database_databaseType,protocal);
		   
		});
	  $(document).on("change","#dataSourceName,#flatDataSourceName,#webserviceDataSourceName,.dataSource_name",function(){
	    	var dataSourceName=  $("#dataSourceName option:selected").val();
	    	var dataSourceOtherName=$("#flatDataSourceName option:selected").val();
	    	var wsDataSourceName=$("#webserviceDataSourceName").val();
	    	var dataSorce_view=$(".dataSource_name option:selected").val();
	    	if(dataSourceName == "-1" || dataSourceOtherName == "-1" || wsDataSourceName == "-1" || dataSorce_view == "-1"){
		    	   $(".dataSourceOther,.flatDataSourceOther,.wsDataSourceOther").show();
		       }else{
		    	   $(".dataSourceOther,.flatDataSourceOther,.wsDataSourceOther").hide();
		       }
	    });
	$(document).on('change', '#existingConnections', function(){
		
		 $('#targetTableDirectMappingPopUp').css('overflow-y','scroll');
		if( $("#existingConnections").val() != '' ){
			$("#deleteDatabaseTypeConnection").show();
			}else{
				$("#deleteDatabaseTypeConnection").hide();
			}
        $("#procedureName").val('');
		$(".serverIpWithPort").empty();
		$('#targetTableDirectMappingPopUp').addClass('dynamic').css('top',0);
		$("#typeOfCommand").val('Query');
		$("#queryScript").empty();
		$("#queryScript").show();
		$("#checkTablePreview").hide();
		$("#procedureName").hide();
		$(".queryValidatemessageDiv").empty();
		common.clearValidations(['#IL_database_connectionName', '#IL_database_serverName', '#IL_database_username', '#IL_database_password','#procedureName',"#queryScript",'#dataSourceName','#dataSourceOtherName']);
		if($(this).val() != ''){
			var connectionId = $(this).val();
			var userID = $("#userID").val();
			var url_getILConnectionById = "/app/user/"+userID+"/package/getILsConnectionById/"+connectionId+"";
			if(connectionId != null){
			   showAjaxLoader(true);
			   var myAjax = common.loadAjaxCall(url_getILConnectionById,'GET','',headers);
			   myAjax.done(function(result) {
				   showAjaxLoader(false);
			    	  if(result != null && result.hasMessages){
			    		  if(result.messages[0].code == "SUCCESS"){
			    		  	customPackage.updateConnectionPanel(result.object);
			    		  	$("#queryScript").val("");
							$("#saveNewConnection_dataBaseType").hide();
							$("#testConnection").hide();
							$("#IL_database_password_div").hide();
							$("#databaseConnectionPanel").show();
							$("#checkQuerySyntax").show();
							$(".IL_queryCommand").show();
							$("#saveILConnectionMapping").hide();
							$(".buildQuery").show();
			    		  }else{
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
			var packageId = $("#packageId").val();
			$('.buildQuery').attr('href', adt.appContextPath+'/adt/package/easyQueryBuilderForCustomPackage/'+packageId+'/'+connectionId);
				
			
	}else{
		$("#databaseConnectionPanel").hide();
	}
	       
	});
	$(document).on('click', '#saveILConnectionMapping', function(){
		var isFlatFile =null;
		var flatFileType =null;
		var filePath =null;
		var delimeter =null;
		var isFirstRowHasColumnNames =null;
		var connectionId =null;
	    var typeOfCommand =null;
	    var queryScript =null;
	    var targetTableId = null;
	    var packageId =null;
	    var userID =null;	
	    var isWebservice = null;
	    var isHavingParentTable = null;
		if($('#flatFiles').is(':checked')) { 
					isFlatFile = true;
					isWebservice  = false;
					flatFileType = $("#flatFileType").val();
				    filePath = $("#filePath").val();
				    delimeter = $("#delimeter").val();
				    if(filePath =='' || delimeter == ''){
				    	customPackage.showMessage(globalMessage['anvizent.package.label.pleaseFillRequiredFields']);
						return false;
					}
				    if($("input:radio[name='isFirstRowHasColumnNames']").is(":checked")) {
				        if($(this).val() == 'Yes'){
				        	isFirstRowHasColumnNames = true;
				        } else {
				        	 isFirstRowHasColumnNames = false;
				        }       
				    } else{
					   customPackage.showMessage(globalMessage['anvizent.package.label.pleaseSelectFirstRowHasColumnNames']);
					   return false;
				    }
			
			}
		if($('#database').is(':checked')) {
			 isFlatFile = false;
			 isHavingParentTable = false;
			 isWebservice = false;
			 connectionId = $("#existingConnections").val();
			 typeOfCommand = $("#typeOfCommand").val();
			 var dataSourceName=  $("#dataSourceName").val();
			 var dataSourceOther = $("#dataSourceOtherName").val();
			 common.clearValidations(["#dataSourceName","#dataSourceOtherName"]);
			 queryScript = typeOfCommand== "Query" ? $("#queryScript").val() : $("#procedureName").val();
			 if(queryScript ==''){
				 customPackage.showMessage(globalMessage['anvizent.package.label.pleaseFillRequiredFields']);
				   return false;
			   }
			 if(dataSourceName == "0"){
				 common.showcustommsg("#dataSourceName",globalMessage['anvizent.package.label.enterDataSourceName']);
				 return false;
			 }
			 if(dataSourceName == null || dataSourceName == undefined){
				 common.showcustommsg("#dataSourceName",globalMessage['anvizent.package.label.enterDataSourceName']);
				 return false;
			 }
			 if(dataSourceName == "-1"){
		    	   if(dataSourceOther  == ''){
		    		   common.showcustommsg("#dataSourceOtherName", globalMessage['anvizent.package.label.enterDataSource'],"#dataSourceOtherName");
		    		   return false;
		    	   }
		       }
		}
		targetTableId = $("#targetTableId").val();
	    packageId = $("#packageId").val();
	    userID = $("#userID").val();
		  
		   var selectData={
				   isMapped : true,
				   isFlatFile : isFlatFile,
				   fileType :flatFileType,
				   filePath : filePath,
				   delimeter : delimeter,
				   isFirstRowHasColoumnNames : isFirstRowHasColumnNames,
				   iLConnection:{
					   connectionId : connectionId, 
				   },
				   typeOfCommand : typeOfCommand,
				   iLquery : queryScript,
				   packageId : packageId,
				   targetTableId : targetTableId,
				   isHavingParentTable :isHavingParentTable,
				   isWebservice : isWebservice,
				   ilSourceName:dataSourceName,
				   dataSourceNameOther:dataSourceOther
		   };
		   showAjaxLoader(true);
		   var url_saveILConnectionMapping = "/app/user/"+userID+"/package/saveILsConnectionMapping";
		   var myAjax = common.postAjaxCall(url_saveILConnectionMapping,'POST', selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    		  if(result != null && result.hasMessages){
		    			 if(result.messages[0].code == "SUCCESS"){
			    			  var messages = '';
			    			  $.each(result, function(key, value) {
			    				  if(key == 'messages'){
			    					   messages=[{
			    						  code : value[0].code,
			    						  text : value[0].text
			    					  }];
			    					  
			    				  }
			    				});
			    			    common.displayMessages(result.messages);
			    			    if ( $('input[type=radio][name="isFileHavingSameColumnNames"]:checked').val() == "false" && $("#targetTableId").val()) {
			    			    	setTimeout(function() {
				    					  window.location = adt.appContextPath+"/adt/package/customPackage/edit/"+packageId;
									}, 500);
				  				}
			    			    //$("#proceedForMapping").show();
			    			  customPackage.getTargetTableDirectMappingInfo();
			    			  $("#targetTableDirectMappingPopUp").modal('hide');
		    			 }else{
		    				 common.showErrorAlert(result.messages[0].text)
				    		//common.displayMessages(result.messages);
		    			 }
		    		  }else{
		    			  alert(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    	  
		    });
		   
		});
	
	$(document).on('change', '#existingSchema', function(){
		if($(this).val() != ''){
			$("#flatFilesLocationDetails").hide();
			$("#databaseConnectionDetails").hide();
			$("#targetTableMappingModule").show();
			$("#mappingTypeSelection").show();
			$("input[type=radio][name='typeSelection']").prop('checked', false);
			//check whether the target table is already mapped or not
		}
	       
	});
	
	$(document).on('click', '#saveAndUpload_direct', function() {
		var isFlatFile = true;
		var flatFileType = $("#flatFileType_direct").val();
	    var filePath = $("#file_direct").val();
	    var delimeter = $("#delimeter_direct").val();
		var isFirstRowHasColumnNames =$("input:radio[name='isFirstRowHasColumnNames']:checked", "#flatFilesLocationDetails").val();
	    var packageId = $("#packageId").val();
	    var userID = $("#userID").val(); 
	    var isSandBox = $("#isSandBox").val();
	    var targetTableId = $("#targetTableId").val();
	    var sourceFileInfoId='';
	    //validations
	    common.clearValidations(["#delimeter_direct", "input:radio[name='isFirstRowHasColumnNames']","#dataSourceName","#dataSourceOtherName", "#firstrowcolsvalidation", "#file_direct"]);
	    
	    if(flatFileType == 'csv'){
		    if(delimeter == '' || delimeter.match(/^\s+$/)) {
		    	common.showcustommsg("#delimeter_direct",globalMessage['anvizent.package.label.thisFieldRequired']);
				return false;
			}else if(delimeter.length > '1'){	
				console.log("delimeter.length - "+delimeter.length)
				common.showcustommsg("#delimeter_direct",globalMessage['anvizent.package.label.delimeterShouldContainOnlyOneCharacter']);
				return false;
			}	    
		}
	     var dataSourceName=  $("#flatDataSourceName").val();
		 var dataSourceOther = $("#flatDataSourceOther").val();
		 console.log("dataSourceName---",dataSourceName,dataSourceOther)
		 if(dataSourceName == "0"){
			 common.showcustommsg("#dataSourceName",globalMessage['anvizent.package.label.enterDataSourceName']);
			 return false;
		 }
		 if(dataSourceName == null || dataSourceName == undefined){
			 common.showcustommsg("#dataSourceName",globalMessage['anvizent.package.label.enterDataSourceName']);
			 return false;
		 }
		 if(dataSourceName == "-1"){
	    	   if(dataSourceOther == ''){
	    		   common.showcustommsg("#dataSourceOtherName", globalMessage['anvizent.package.label.enterDataSource'],"#dataSourceOtherName");
	    		   return false;
	    	   }
	       } 
	    if(filePath == '' ) {
	    	common.showcustommsg("#file_direct", globalMessage['anvizent.package.label.pleaseChooseaFile']);
			return false;
		}
	    var fileExtension = filePath.replace(/^.*\./, '');
	    if(fileExtension != flatFileType) {
	    	common.showcustommsg("#file_direct", globalMessage['anvizent.package.label.fileExtensionIsNotMatchingWithFileTypeSelected']);
	        return false;
	    }
	    if(  isSandBox == 'true'){ 
	      var fileSize =  $("#file_direct")[0].files[0].size;
	   	  if ( fileSize > 5242880)
	   	  {
	   	   common.showcustommsg("#file_direct", globalMessage['anvizent.package.label.allowedfilesizeexceededmax5mb']);
	   	   return false;
	   	  }
	      }
	    // to show loader.
	    showAjaxLoader(true);
	    
	    setTimeout(function() { 
	    	
			//submit the file upload form		    				
			$("#packageIdForFileUpload_direct").val(packageId);
			var formData = new FormData($("#fileUploadForm_direct")[0]);
	        var url_uploadFileIntoS3 = "/app/user/"+userID+"/package/uploadsFileIntoS3";
	        var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			var myAjax = common.postAjaxCallForFileUpload(url_uploadFileIntoS3,'POST', formData,headers);
			myAjax.done(function(result) {
				 if(result != null){
					 if(result.hasMessages){
						 if(result.messages[0].code == "ERROR") {
							 var  messages=[{
								  code : result.messages[0].code,
								  text : result.messages[0].text
							  }];
							 showAjaxLoader(false);
							 var msg = result.messages[0].text;
							 common.showcustommsg("#file_direct", "<h5>"+msg+"</h5>");
		    				  return false;
		    			  }
						 if(result.messages[0].code == "DUPL_FILE_NAME") {
							 var  messages=[{
								  code : result.messages[0].code,
								  text : result.messages[0].text
							  }];
							 showAjaxLoader(false);
							 common.showcustommsg("#file_direct", result.messages[0].text);
		    				  return false;
		    			  }
		    		  }
				 }
		    	  if(result != null){
	    			  if(result.object != null){
	    				 // var filePathLocationInServer = result.object;
	    				 sourceFileInfoId = result.object;
	    				//save the mapping
	    					var selectData={
	    							   isMapped : true,
	    							   isFlatFile : isFlatFile,
	    							   fileType :flatFileType,
	    							   sourceFileInfoId : sourceFileInfoId,
	    							   delimeter : delimeter,
	    							   isFirstRowHasColoumnNames : isFirstRowHasColumnNames,
	    							   packageId : packageId,
	    							   targetTableId : targetTableId,
	    							   isWebservice:false,
	    							   ilSourceName:dataSourceName,
	    							   dataSourceNameOther:dataSourceOther
	    					   };
	    				    var url_saveILConnectionMapping = "/app/user/"+userID+"/package/saveILsConnectionMapping";
	    					   var myAjax = common.postAjaxCall(url_saveILConnectionMapping,'POST', selectData,headers);
	    					    myAjax.done(function(result) {
	    					    	
	    					    	// to hider loader.
	    					    	showAjaxLoader(false);
	    					    		  if(result != null && result.hasMessages){
	    					    			  if(result.messages[0].code == "SUCCESS") {
		    					    			  var messages;
		    					    			  $.each(result, function(key, value) {
		    					    				  if(key == 'messages'){
		    					    					   messages=[{
		    					    						  code : value[0].code,
		    					    						  text : value[0].text
		    					    					  }];
		    					    					  
		    					    				  }
		    					    				});
		    					    			  customPackage.showCustomMessage("#flatfilemessage", messages[0]);
		    					    			  customPackage.clearFlatFileDatabaseValues();
		    					    			  if ( $('input[type=radio][name="isFileHavingSameColumnNames"]:checked').val() == "false" && $("#targetTableId").val()) {
		    					    				  setTimeout(function() {
		    					    					  window.location = adt.appContextPath+"/adt/package/customPackage/edit/"+packageId;
													}, 1000);
		    					  				  }
		    					    			  //$("#proceedForMapping").show();
		    					    			  customPackage.getTargetTableDirectMappingInfo();
	    					    			  
	    					    			  }else{
	    					    				  common.showErrorAlert(result.messages[0].text);
	    					    				  //common.displayMessages(result.messages);
	    					    			  }
	    					    		  }
	    					    		  else {
									    		var messages = [ {
									    			code : globalMessage['anvizent.message.error.code'],
													text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
												} ];
									    		common.displayMessages(messages);
	    					    		  }
	    					    	  
	    					    });
	    			  }
		    	  }
		    });
	    }, 150);	
		
	});
	$(document).on("click",".viewTargetTableDirectMappingInfo",function(){
		
		var _this = $(this);
		var isFlatFile = _this.attr("data-isFlatFile");
		if(isFlatFile == "true"){
			var delimeter = _this.attr("data-delimeter");
			var filePath = _this.attr("data-filePath");
			var fileType = _this.attr("data-fileType");
			var mappingId = _this.attr("data-mappingid");
			var isFirstRowHasColoumnNames = _this.attr("data-isFirstRowHasColoumnNames") == "true" ? "Yes" : "No" ;
			console.log("mappingId  -- > ", mappingId);
			$("#show_fileType").val(fileType)
			$("#show_delimiter").val(delimeter)
			$("#show_firstRowHasColumnNames").val(isFirstRowHasColoumnNames)
			$("#show_mappingId").val(mappingId)
			$("#show_filePath").val(filePath)
			$("#show_ILConnectionFlatFileInfo").modal('show');
			
		}else{
			
			var connectionName = _this.attr("data-connection-connectionName");
			var connectionId = _this.attr("data-connection-connectionId");
			var connectionType = _this.attr("data-connection-connectionType");
			var databseType = _this.attr("data-connection-database-type");
			var server = _this.attr("data-connection-server");
			var username = _this.attr("data-connection-username");
			var iLquery = _this.attr("data-iLquery");
			var typeOfCommand = _this.attr("data-typeOfCommand");
			
			$("#show_connectionName").val(connectionName);
			$("#show_connectionId").val(connectionId);
			$("#show_connectionType").val(connectionType)
			$("#show_databaseType").val(databseType)
			$("#show_serverName").val(server)
			$("#show_userName").val(username)
			$("#show_typeOfCommand").val(typeOfCommand)
			$("#show_queryScript").val(iLquery)
			
			$("#show_ILConnectionDataBaseInfo").modal('show');
		}
	});
	
	$('#createTargetTable').on('click',  function() {
		
		
		
		common.clearcustomsg("#targetTableName_creation");
		var packageId = $("#packageId").val();
		var industryId = $("#industryId").val();
	    var userID = $("#userID").val();
	    var targetTableName = $("#targetTableName_creation").val();
	    
	    if(targetTableName == ''){ 
	    	common.showcustommsg("#targetTableName_creation", globalMessage['anvizent.package.label.pleaseEnterTargetTableName']);
			return false;
	    }else if(targetTableName.match(/\s/g)) {
	    	
	    	common.showcustommsg("#targetTableName_creation",globalMessage['anvizent.package.label.tableNameShouldNotContainSpace']);
			return false;
	    }else if(/^[a-zA-Z0-9_]*$/.test(targetTableName) == false) {
	    	
	    	common.showcustommsg("#targetTableName_creation", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters']+ "<br>"+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInTableName']);
			return false;
	    }else if(targetTableName.match(/^(IL_|DL_|il_|dl_|iL_|Il_|dL_|Dl_)/)) {
		    	common.showcustommsg("#targetTableName_creation",globalMessage['anvizent.package.label.tableNameShouldNotContainILsandDLsNames']);
		    	return false;
	    }
	    
	    /*default values and column length validations*/
	    var status = customPackage.validateTargetTableFields();
	    if(!status){	    	
	    	return false;
	    }
	    

	    /*Min max length validation*/
	    var selectors = [];

		selectors.push('#targetTableName_creation');
	    var valid = common.validate(selectors);
	    if (!valid) {
			return false;
		}

	    var tablerows = $("#targetTable").find("tbody").find("tr").filter(function() {
	    	return $(this).find('.m-check').is(':checked');
	    });
	    
	    var valid=true;
	    
	 var columnValidation=tablerows.each(function() {
	    	var row = $(this);
	    	var columnName = row.find('.m-column').val();
	    	var inputTypeId=row.find('.m-column').attr('id');
	    	common.clearValidations(["#"+inputTypeId]);
	    	if(columnName === '') {
	    		common.showcustommsg("#"+inputTypeId, globalMessage['anvizent.package.label.columNameShouldntBeEmpty'], "#"+inputTypeId);
	    		valid=false;
			} else if(columnName.match(/\s/g)) {
		    	common.showcustommsg("#"+inputTypeId, globalMessage['anvizent.package.label.columnNameShouldNotContainSpace'],"#"+inputTypeId);
		    	valid=false;
		    }else if(/^[a-zA-Z0-9_]*$/.test(columnName) == false) {
		    	common.showcustommsg("#"+inputTypeId, globalMessage['anvizent.package.label.columNameContainsIllegalSpecialCharacters'] +"<br>"+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInColumnName'],"#"+inputTypeId);
		    	valid=false;
		    }
	    	
	    });
	 
	  if (!valid) return false;
	 
	    var columns = customPackage.validateTableColumns('targetTable');
	    var originalColumns = customPackage.validateTableOriginalColumns('targetTable');
	    
	    if (!columns && !originalColumns) {
    		// to stops processing ...
    		return false;
    	}
	    
	    var selectData = {
    		userId : userID,
    		userPackage : {
    			packageId :packageId,
    			industry : {
    				id : industryId
    			},
    			table : {
    				tableName : targetTableName,
    				isDirect : false,
    				columns :columns,
    				originalColumnNames : originalColumns
    			}
    		}
	    };
	    
	    showAjaxLoader(true);
	    var url_saveTargetTableInfo = "/app/user/"+userID+"/package/saveMappingTablesInfo";
	    var myAjax = common.postAjaxCall(url_saveTargetTableInfo,'POST', selectData,headers);
	    myAjax.done(function(result) {
	    	showAjaxLoader(false);
	    	  if(result != null) {
	    		  if(result.hasMessages){
	    			  if(result.messages[0].code == "DUPL_TARGET_TABLE") {
	    				  common.showcustommsg("#targetTableName_creation", result.messages[0].text);
	    				  return false;
	    			  }else if(result.messages[0].code == "ERROR") {
	    				  $('.duplicateTargetTableName').empty();
	    				  var errorMessage = '<label class="duplicateTargetTableName" for="targetTableName" style="color: #ff0000">'+result.messages[0].text+'</label>';
	    				  $("#targetTableName_creation").after(errorMessage);
	    				  return false;
	    			  }else {
	    				  $("#targetTableCreationPopUp").modal('hide');
	    				  console.log("result   -- > ", result);
	    				  if (result.messages[0].code == "SUCCESS") {
	    					  $("#popUpMessageForTableDelete").prop({"class":"alert alert-success", "data-isReloadRequired":"Y"}).text(result.messages[0].text);
	    					  $("#closeMessageWindow").attr("data-isReloadRequired", "Y");
	    					  $("#messagePopUpForTableDelete").modal('show');
	    				  }else{
	    					  common.displayMessages(result.messages);
	    				  }
	    				  
	    			  }
	    			 
	    			 
	    		  }else {
	    			  alert(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
	    		  }
	    	  }
	    });
	});
	
	$("#mappingTable").on('click', '.m-check-all', function() {
		$("#mappingTable").find('.m-check').prop('checked', this.checked);
	});
	$("#targetTableCreationPopUp").on('click', '.m-check-all', function() {
		$("#targetTableCreationPopUp").find('.m-check').prop('checked', this.checked);
	});
	
	$("#mappingTableMultiple").on('click', '.m-check-all', function() {
		$("#mappingTableMultiple").find('.m-check').prop('checked', this.checked);
	});
	
	$(".viewTargetTableStructure").click(function(){
		debugger
		var tableName = $(this).closest(".row").find("input.targetTableName").val();
		var industryId = $("#industryId").val();
		var userID = $("#userID").val();
		$("#viewTargetTableHeader").text(tableName);
		if(tableName != ''){
			showAjaxLoader(true);
			var url_getTableStructure = "/app/user/"+userID+"/package/getTablesStructure/"+industryId+"/"+tableName+"";
			   var myAjax = common.loadAjaxCall(url_getTableStructure,'GET','',headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	
			    	if(result != null && result.hasMessages ){
			    		
			    		if(result.messages[0].code == "SUCCESS"){
			    			customPackage.updateTargetTableStructureTable(result.object);
			    			resultOfTargetTable = result.object
			    			$("#saveTargetTableStructure").hide();
			    			$("#editTargetTableStructure").show();
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
		}
	});
	$("#queryScript").on('keyup',function(){
		$("#saveILConnectionMapping").hide();
		$("#checkTablePreview").hide();
		$(".queryValidatemessageDiv").empty();
	});
	$("#procedureName").on('keyup',function(){
		$("#saveILConnectionMapping").hide();
		$("#checkTablePreview").hide();
		$(".queryValidatemessageDiv").empty();
	});
	$("#checkQuerySyntax").click(function(){
		$(".queryValidatemessageDiv").empty();
		$("#saveILConnectionMapping").hide();
		 $("#checkTablePreview").hide();
		var userID = $("#userID").val(); 
		var connectionId = $("#existingConnections").val();
		var typeOfCommand = $("#typeOfCommand").val(); 
		var query = typeOfCommand === "Query" ? $("#queryScript").val() : $("#procedureName").val();
		
		common.clearValidations(["#queryScript", "#procedureName"]);
		
		if( query != '') {
			var selectData ={
					iLConnection : {
						connectionId : connectionId
					},
					iLquery : query,
					typeOfCommand : typeOfCommand
			}
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			 showAjaxLoader(true); 
			var url_checkQuerySyntax = "/app/user/"+userID+"/package/checksQuerySyntax";
			 var myAjax = common.postAjaxCall(url_checkQuerySyntax,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    	 showAjaxLoader(false);
			    	  if(result != null && result.hasMessages){
			    		  var message = '';
			    		  if(result.messages[0].code == "SUCCESS") {
			    			  message += '<div class="alert alert-success alert-dismissible" role="alert">'+
	    		  							''+result.messages[0].text+' <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+
	    		  							'</div>';
			    			  $(".queryValidatemessageDiv").append(message);
			    			  setTimeout(function() { $(".queryValidatemessageDiv > .alert-success").hide() .empty(); }, 10000);
			    			  $("#saveILConnectionMapping").show()
			    			  $("#checkTablePreview").show();
			    		  }else{
			    			  message += '<div class="alert alert-danger alert-dismissible" role="alert">'+
	  							''+result.messages[0].text+''+
	  							'</div>';
			    			  	$(".queryValidatemessageDiv").append(message);
			    			  	setTimeout(function() { $(".queryValidatemessageDiv > .alert-danger").hide() .empty(); }, 10000);
			    		  }
			    		  
			    	  }else{
							var messages = [ {
								code : globalMessage['anvizent.message.error.code'],
								text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
							} ];
				    		common.displayMessages(messages);
						}
			    });
			
		}else{
			common.showcustommsg((typeOfCommand === "Query" ? "#queryScript" : "#procedureName"), typeOfCommand+" "+globalMessage['anvizent.package.label.shouldNotBeEmpty']);
		}
	});
	
	$(document).on('click', '.closeTablePreview', function() {
		$("#tablePreviewPopUp").modal('hide');
		 $('#targetTableDirectMappingPopUp').css('overflow-y','scroll');
	    });
	
	
	$(document).on('click', '.closeTablePreview', function() {
		$("#tablePreviewPopUp").modal('hide');
		 $('#targetTableDirectMappingPopUp').css('overflow-y','scroll');
	});
	
	$(document).on('click', '#targetTablePopUpClose', function() {
		$("#targetTableDirectMappingPopUp").modal('hide');
	    });
	$(document).on('click', '#closePreview', function() {
		$("#tablePreviewPopUp").modal('hide'); 
	    });
	
	$("#targetTable").on('click', ".m-auto", function() {
			var obj = $(this);
			$("#targetTable .m-notnull, #targetTable .m-primary,.m-default").prop("disabled", ""); 
			if (this.checked) {
				this.checked = false;
				
				$("#targetTable .m-auto:checked").each(function(index,rowObject){
					if (rowObject.checked) {
						rowObject.checked = false;
						var currentRow = $(rowObject.closest("tr")); 
						var currentnotNulChkBox = currentRow.find(".m-notnull");
						if ( currentnotNulChkBox.prop("checked") && !currentnotNulChkBox.hasClass("alreadyChecked") ) {
							currentnotNulChkBox.prop("checked",false);
						} else {
							currentnotNulChkBox.removeClass("alreadyChecked");
						}
						var currentprimaryChkBox = currentRow.find(".m-primary");
						if ( currentprimaryChkBox.prop("checked") && !currentprimaryChkBox.hasClass("alreadyChecked") ) {
							currentprimaryChkBox.prop("checked",false);
						} else {
							currentprimaryChkBox.removeClass("alreadyChecked");
						}
					} 
					 
					
				});
				obj.prop("checked",true); 
				var selectedRow = $(obj.closest("tr"));
				var notNulChkBox = selectedRow.find(".m-notnull");
				var primaryChkBox = selectedRow.find(".m-primary");
				var defaultTextBox =  selectedRow.find(".m-default").prop("disabled","disabled");
				if (notNulChkBox.prop("checked")) {
					notNulChkBox.addClass("alreadyChecked");
				} else {
					notNulChkBox.prop("checked",true);
				}

				if (primaryChkBox.prop("checked")) {
					primaryChkBox.addClass("alreadyChecked");
				} else {
					primaryChkBox.prop("checked",true);
				}
				
			} else {
				var currentRow = $(obj.closest("tr")); 
				var currentnotNulChkBox = currentRow.find(".m-notnull");
				if ( currentnotNulChkBox.prop("checked") && !currentnotNulChkBox.hasClass("alreadyChecked") ) {
					currentnotNulChkBox.prop("checked",false);
				} else {
					currentnotNulChkBox.removeClass("alreadyChecked");
				}
				var currentprimaryChkBox = currentRow.find(".m-primary");
				if ( currentprimaryChkBox.prop("checked") && !currentprimaryChkBox.hasClass("alreadyChecked") ) {
					currentprimaryChkBox.prop("checked",false);
				} else {
					currentprimaryChkBox.removeClass("alreadyChecked");
				}
			}
	    });
	
	
	
	// validate query table Preview for custom package
	$(document).on('click', '#checkTablePreview', function() {
		$(".queryValidatemessageDiv").empty();
		$("#saveILConnectionMapping").show();
		var userID = $("#userID").val(); 
		var connectionId = $("#existingConnections").val();
		var typeOfCommand = $("#typeOfCommand").val(); 
		var packageId = $("#packageId").val();
		var previewSourceTitle = $("#existingConnections option:selected").text().trim();
		var query = typeOfCommand === "Query" ? $("#queryScript").val() : $("#procedureName").val();
		common.clearValidations(["#queryScript", "#procedureName"]);
		if( query != '') {
			var selectData ={
					packageId : packageId,
					iLConnection : {
						connectionId : connectionId
					},
					iLquery : query,
					typeOfCommand : typeOfCommand
			}
			if (typeOfCommand === "Stored Procedure") {
				var params = $("div.s-param-vals", "#databaseSettings");
				if (params && params.length) {
					var procedureParameters = [];
					params.each(function() {
						var param = $(this);
						var paramname = param.find('input.s-param-name').val(),
						paramvalue = param.find('input.s-param-value').val();
						if (paramname.replace(/\s+/g, '') && paramvalue.replace(/\s+/g, ''))
							procedureParameters.push(paramname+'='+paramvalue);
					});
					if (procedureParameters.length)
						selectData.procedureParameters = procedureParameters.join('^');
				}
			}
			showAjaxLoader(true);
			var url_checkQuerySyntax = "/app/user/"+userID+"/package/getTablePreview";
			 var myAjax = common.postAjaxCall(url_checkQuerySyntax,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null){
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") {
									
				    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+
		  							''+result.messages[0].text+''+
		  							'</div>';
				    			  	$(".queryValidatemessageDiv").append(message)
				    				  return false;
				    			  }  
			    		  }
			    		  
			    		  $("#tablePreviewPopUp").modal('show');
			    		  $("#tablePreviewPopUpHeader").text(previewSourceTitle);
			    	  var list = result.object;
			    	  if(list != null && list.length > 0){
			    		  var tablePreview='';
				    	  $.each(list, function (index, row) {
				    		  
				    		  tablePreview+='<tr>';
				    		  $.each(row, function (index1, column) {
				    			  tablePreview += (index == 0 ? '<th>'+column+'</th>' : '<td>'+column+'</td>');
				    		  });
				    		 tablePreview+='</tr>'; 
				    		});
				    	  $(".tablePreview").empty();
				    	  $(".tablePreview").append(tablePreview);
				    	  }
				    	  else{
				    		  $(".tablePreview").empty();
				    		  $(".tablePreview").append(globalMessage['anvizent.package.label.noRecordsAvailableInTable']);
				    	  } 
			    	  }
			    	  
			    });
			
		} else {
			common.showcustommsg((typeOfCommand === "Query" ? "#queryScript" : "#procedureName"), typeOfCommand+" "+globalMessage['anvizent.package.label.shouldNotBeEmpty']);
		}
	});
	
	$(document).on('click', '.viewDetailscloseTablePreview, .closetablePreviewPopUp, .closeViewDeatilsTablePreviewPopUp', function() {
		$("#viewDeatilsTablePreviewPopUp").modal('hide');
	 });
	
	$(document).on('click', '#checkTablePreviewViewDetails', function() {
		$(".queryValidatemessageDiv").empty();
		$("#saveILConnectionMapping").show();
		var userID = $("#userID").val(); 
		var connectionId = $("#show_connectionId").val();
		var typeOfCommand = $("#show_typeOfCommand").val(); 
		var query = typeOfCommand === "Query" ? $("#show_queryScript").val() : $("#show_procedureName").val();
		if( query != '') {
			var selectData = {
					iLConnection : {
						connectionId : connectionId
					},
					iLquery : query,
					typeOfCommand : typeOfCommand
			}
			console.log("selectData  -- > ", selectData);
			if (typeOfCommand === "Stored Procedure") {
				var params = $("div.s-param-vals", "#databaseSettings");
				if (params && params.length) {
					var procedureParameters = [];
					params.each(function() {
						var param = $(this);
						var paramname = param.find('input.s-param-name').val(),
						paramvalue = param.find('input.s-param-value').val();
						if (paramname.replace(/\s+/g, '') && paramvalue.replace(/\s+/g, ''))
							procedureParameters.push(paramname+'='+paramvalue);
					});
					if (procedureParameters.length)
						selectData.procedureParameters = procedureParameters.join('^');
				}
			}
			showAjaxLoader(true);
			var url_checkQuerySyntax = "/app/user/"+userID+"/package/getTablePreview";
			 var myAjax = common.postAjaxCall(url_checkQuerySyntax,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null){
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") {
									
				    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+
		  							''+result.messages[0].text+''+
		  							'</div>';
				    			  	$(".queryValidatemessageDiv").append(message)
				    				  return false;
				    			  }  
			    		  }
			    		  
			    		  $("#viewDeatilsTablePreviewPopUp").modal('show');
			    	  var list = result.object;
			    	  if(list != null && list.length > 0){
			    		  var tablePreview='';
				    	  $.each(list, function (index, row) {
				    		  
				    		  tablePreview+='<tr>';
				    		  $.each(row, function (index1, column) {
				    			  tablePreview += (index == 0 ? '<th>'+column+'</th>' : '<td>'+column+'</td>');
				    		  });
				    		 tablePreview+='</tr>'; 
				    		});
				    	  $(".viewDeatilsTablePreview").empty();
				    	  $(".viewDeatilsTablePreview").append(tablePreview);
				    	  }
				    	  else{
				    		  $(".viewDeatilsTablePreview").empty();
				    		  $(".viewDeatilsTablePreview").append(globalMessage['anvizent.package.label.noRecordsAvailableInTable']);
				    	  } 
			    	  }
			    	  
			    });
			
		} else {
			common.showcustommsg((typeOfCommand === "Query" ? "#show_queryScript" : "#show_procedureName"), typeOfCommand+" "+globalMessage['anvizent.package.label.shouldNotBeEmpty']);
		}
	});
	
	

	$(document).on('click', '#checkFlatPreviewViewDetails', function() {
		var userID = $("#userID").val();
		var packageId = $("#packageId").val();
		var mappedId = $("#show_mappingId").val();
		
		
			showAjaxLoader(true);
			var url_checkQuerySyntax = "/app/user/"+userID+"/package/getFlatFilePreview/"+packageId+"/"+mappedId;
			 var myAjax = common.loadAjaxCall(url_checkQuerySyntax,'GET','',headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null){
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") {
									
				    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+
		  							''+result.messages[0].text+''+
		  							'</div>';
				    			  	$(".queryValidatemessageDiv").append(message)
				    				  return false;
				    			  }  
			    		  }
			    		  
			    		  $("#viewDeatilsFlatPreviewPopUp").modal('show');
			    	  var list = result.object;
			    	  if(list != null && list.length > 0){
			    		  var tablePreview='';
				    	  $.each(list, function (index, row) {
				    		  
				    		  tablePreview+='<tr>';
				    		  $.each(row, function (index1, column) {
				    			  tablePreview += (index == 0 ? '<th>'+column+'</th>' : '<td>'+column+'</td>');
				    		  });
				    		 tablePreview+='</tr>'; 
				    		});
				    	  $(".viewDeatilsFlatPreview").empty();
				    	  $(".viewDeatilsFlatPreview").append(tablePreview);
				    	  }
				    	  else{
				    		  $(".viewDeatilsFlatPreview").empty();
				    		  $(".viewDeatilsFlatPreview").append(globalMessage['anvizent.package.label.noRecordsAvailable']);
				    	  } 
			    	  }
			    	  
			    });
			
		
	});
	
	
	
	
	$(document).on('change', '#typeOfCommand', function() {
		$(".queryValidatemessageDiv").empty();
		common.clearValidations(["#queryScript", "#procedureName"]);
		if (this.value === "Stored Procedure") {
			$("#procedureName").show();
			$("#queryScript").hide();
			$('#checkTablePreview').hide();
			$('#saveILConnectionMapping').hide();
		}
		else {
			$("#procedureName").hide();
			$("#queryScript").show();
			$('#checkTablePreview').hide();
			$('#saveILConnectionMapping').hide();
		}
	});
	
	$(document).on('change', '.m-datatype', function() {
		var _this = $(this), colsize = _this.closest('tr').find('.m-colsize');
		debugger
		var decimal = _this.closest('tr').find('.m-decimal');
		decimal.val("");
		decimal.hide();
		
		for(var i=0;i<dataTypeResult.object.length;i++){
			 var dataTypeName = dataTypeResult.object[i].dataTypeName;
			 var range = dataTypeResult.object[i].range;
			 if(_this.val() === dataTypeName){
				 if(_this.val() == "DECIMAL" || _this.val() == "FLOAT" || _this.val() == "DOUBLE"){
					 var size = range.split(",");
					 debugger
					 	colsize.val(size[0]).attr("disabled",false);
						decimal.val(size[1]);
						decimal.show(); 
				 }else{
					 colsize.val(range).attr("disabled",false);
				 }
				 
			 }
		 }
		
		/*if(_this.val() == 'INT' || 	_this.val() == 'BIGINT'){
			colsize.val("11").attr("disabled",false);
		}else if(_this.val() == 'DATETIME'){
			colsize.val("19").attr("disabled",false);
		}else if(_this.val() == 'DATE'){
			colsize.val("0").attr("disabled",false);
		}else if(_this.val() == 'VARCHAR' ){
			colsize.val("45").attr("disabled",false);
		}else if(_this.val() == 'BIT' ){
			colsize.val("1").attr("disabled",false);
		}else if(_this.val() == 'DECIMAL' ){
			colsize.val("24").attr("disabled",false);
			decimal.val("7");
			decimal.show();
		}*/
		
		var autoInc = _this.closest('tr').find('.m-auto');
		var primary = _this.closest('tr').find('.m-primary');
		var notnull = _this.closest('tr').find('.m-notnull');
		if(_this.val() == 'INT' || 	_this.val() == 'BIGINT' || _this.val() == "TINYINT" || _this.val() == "SMALLINT" || _this.val() == "MEDIUMINT"){
			if (autoInc.is(":disabled")) {
				autoInc.prop("disabled","");
			}
		} else {
			if ( autoInc.is(":checked") ) {
				primary.prop("checked",false);
				notnull.prop("checked",false);
				autoInc.prop("checked",false);
			}
			if (autoInc.is(":enabled")) {
				autoInc.prop("disabled","disabled");
			}
			
		}
	});
	
	$("#selectFileHeadersDiv").on('click', 'input.chk-primary', function() {
		
		var chks = $("#selectFileHeadersDiv").find('input.chk-primary').not(this).filter(function() {
			return this.checked;
		}).prop('checked', false);
		
	});

	$("#multipleMappingProceedButton").on('click', function() {
		console.log('in proceed .. ');
		
		var chks = $("#selectFileHeadersDiv").find('input.chk-primary').filter(function() {
			return this.checked;
		});
		
		console.log('length : ', chks.length);
		
		if (chks.length === 0) {
			alert(globalMessage['anvizent.package.label.pleaseSelectPrimaryFile']);
			return false;
		}
		else if (chks.length>1) {
			alert(globalMessage['anvizent.package.label.pleaseSelectOnePrimaryFile']);
			return false;
		}
		
		var fileid = chks.val();
		
		var packageId = $("#packageId").val();
		var industryId = $("#industryId").val();
	    var userID = $("#userID").val();
	   
	    var url_saveMappingTableInfo = "/app/user/"+userID+"/package/saveMultipleTableMappingInfo/"+packageId+"/"+industryId+"/"+fileid;
	    var myAjax = common.postAjaxCall(url_saveMappingTableInfo,'POST','',headers);
	    
	    // to show loader.
	    showAjaxLoader(true);
	    
		myAjax.done(function(result) {
			showAjaxLoader(false);
			console.log(result);
			
			console.log('result code : ', result.messages[0].code);
			
			if (result.messages[0].code === "SUCCESS") {
				setTimeout(function() {
					window.location = adt.appContextPath+"/adt/package/queryBuilder/"+packageId;
				}, 50);
			}
			
		});
		
	});
	
	
	$("#viewTargetTableDiv").on('click', '.delete-derived-table', function(){
		$("#confirmDeleteDerivedTable").attr("data-targetTableId",$(this).attr("data-targetTableId")).attr("data-tableId",$(this).attr("data-tableId"));
		$("#deleteDerivedTableAlert").modal('show');
	});

	$("#confirmDeleteDerivedTable").on("click",function(){
		var packageId = $("#packageId").val();
	    var userID = $("#userID").val();
	    var targetTableId = $(this).attr("data-targetTableId");
	    var tableId = $(this).attr("data-tableId");
	    var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
	    var url_saveMappingTableInfo = "/app/user/"+userID+"/package/deleteSameDatasetCustomTablesBYTableId/"+packageId+"/"+targetTableId+"/"+tableId;
	    var myAjax = common.postAjaxCall(url_saveMappingTableInfo,'POST','',headers);
	    $("#deleteDerivedTableAlert").modal('hide');
	    showAjaxLoader(true);
		myAjax.done(function(result) {
			showAjaxLoader(false);
			if(result != null && result.hasMessages){
			if (result.messages[0].code === "SUCCESS") {
				$("#popUpMessageForTableDelete").prop({"class":"alert alert-success", "data-isReloadRequired":"Y"}).text(result.messages[0].text);
				$("#closeMessageWindow").attr("data-isReloadRequired", "Y");
			}else {
				$("#popUpMessageForTableDelete").prop({"class":"alert alert-danger"}).text(result.messages[0].text);
				$("#closeMessageWindow").attr("data-isReloadRequired", "N");
			}
			$("#messagePopUpForTableDelete").modal('show');
		}else{
    		var messages = [ {
    			code : globalMessage['anvizent.message.error.code'],
    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
    		} ];
    		common.displayMessages(messages);
    	}
		
		});
	});
	
	$("#closeMessageWindow").click(function(){
		if ( $(this).attr("data-isReloadRequired") == "Y") {
			var packageId = $("#packageId").val();
			window.location = adt.appContextPath+"/adt/package/customPackage/edit/"+packageId;
		}
		$("#messagePopUpForTableDelete").modal('hide');
	});
	
	$("#viewTargetTableDiv").on("click",".delete-target-table",function(){
		$("#deleteTargetTableAlert").modal('show');
	});
	$("#confirmDeleteTargetTable").on("click",function(){
		var packageId = $("#packageId").val();
	    var userID = $("#userID").val();
	    var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
	    var url_saveMappingTableInfo = "/app/user/"+userID+"/package/deleteSameDatasetCustomTablesBYPackageId/"+packageId;
	    var myAjax = common.postAjaxCall(url_saveMappingTableInfo,'POST','',headers);
	    
	    $("#deleteTargetTableAlert").modal('hide');
	    showAjaxLoader(true);
	    
		myAjax.done(function(result) {
			showAjaxLoader(false);
			console.log(result);
			if(result != null && result.hasMessages){
			//console.log('result code : ', result.messages[0].code);
			
			if (result.messages[0].code === "SUCCESS") {
				$("#popUpMessageForTableDelete").prop({"class":"alert alert-success", "data-isReloadRequired":"Y"}).text(result.messages[0].text);
				$("#closeMessageWindow").attr("data-isReloadRequired", "Y");
			}else {
				$("#popUpMessageForTableDelete").prop({"class":"alert alert-success"}).text(result.messages[0].text);
				$("#closeMessageWindow").attr("data-isReloadRequired", "N");
			}
			$("#messagePopUpForTableDelete").modal('show');
		}else{
    		var messages = [ {
    			code : globalMessage['anvizent.message.error.code'],
    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
    		} ];
    		common.displayMessages(messages);
    	}
		});
	});
	

	$("#flatFileType_direct").on('change',function(){
		var fileType = $("#flatFileType_direct option:selected").val();
		if(fileType == "csv"){
			common.clearValidations(["#delimeter_direct"]);
			$("#delimeter_direct").val(",");
			$(".delimeter-block").show();
		}
		else{
			
			$("#delimeter_direct").val("");
			$(".delimeter-block").hide();
		}
	});
	

	$("input[name=isFromExistingPackages]").change(function(){
		if ( this.value === "true") {
			$(".existingPackageDiv").show();
		} else {
			$(".existingPackageDiv").hide();
		}
	});
  	 $(document).on("click",".viewDerivedTableStructure", function(e){ 
  		var table = $($(this).closest("tr")).find(".parentTable") .attr('data-table');
 		var industryId = $("#industryId").val();
 		var userID = $("#userID").val();
 		$("#viewTargetTableHeader").text(table.trim());
 		if(table != ''){
 			showAjaxLoader(true);
 			var url_getTableStructure = "/app/user/"+userID+"/package/getTablesStructure/"+industryId+"/"+table.trim()+"";
 			   var myAjax = common.loadAjaxCall(url_getTableStructure,'GET','',headers);
 			    myAjax.done(function(result) {
 			    	showAjaxLoader(false);
 			    	if(result != null && result.hasMessages ){
 			    		if(result.messages[0].code == "SUCCESS"){
 			    			derivedTable.updateTargetTableStructureTable(result.object);
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
 		}
 	});
  	$(document).on("change","#isDerivedTables", function(e){
  		showAjaxLoader(true);
  		var packageId = $("#packageId").val();
  		setTimeout(function() {
  		window.location = adt.appContextPath+"/adt/package/derivedTable/edit/"+packageId;
  		}, 100);
  	});

$(".viewTargetTableQuery").click(function(){
		 
	    var packageId = $("#packageId").val();
		var userID = $("#userID").val();
		 
		if(packageId != ''){
			showAjaxLoader(true);
			var url_getTargetTableQuery = "/app/user/"+userID+"/package/getTargetTableQuery/"+packageId+"";
			   var myAjax = common.loadAjaxCall(url_getTargetTableQuery,'GET','',headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null && result.hasMessages){
			    			  if(result.messages[0].code == "SUCCESS") {
			    				  customPackage.viewTagetTableQuery(result.object);
				    		  }	else{
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
		
$(".buildQuery").on('click',function(e){
	 $(this).attr("disabled",true).text(globalMessage['anvizent.package.label.loading']);
});

$("#editAllSourcesHavingSameSetOfHeaders").on('click',function(e){
	$(this).hide();
	$("#YesOrNoOption").hide();
	var isFileHavingSameColumnNames= $("#isFileHavingSameColumnNames").val();
	if(isFileHavingSameColumnNames == "true"){
		$("input[type=radio][name='isFileHavingSameColumnNames'][value=true]").prop('checked', true);
		
	}else{
		$("input[type=radio][name='isFileHavingSameColumnNames'][value=false]").prop('checked', true);
		
	}
	$("#isFileHavingSameColumnNamesDiv").show();
	$("#editAllSourcesHavingSameSetOfHeaders").hide();
	$("#undoAllSourcesHavingSameSetOfHeaders").show();
	$("#proceed").show();
	$("#proceedForMapping").attr("disabled",true);
	
	
});

$("#undoAllSourcesHavingSameSetOfHeaders").click(function(){
	
	var isFileHavingSameColumnNames= $("#isFileHavingSameColumnNames").val();
	if(isFileHavingSameColumnNames == "true"){
		$("input[type=radio][name='isFileHavingSameColumnNames'][value=true]").prop('checked', true);
		$("#YesOrNoOption").text(globalMessage['anvizent.package.button.yes']);
		
	}else{
		$("input[type=radio][name='isFileHavingSameColumnNames'][value=false]").prop('checked', true);
		$("#YesOrNoOption").text(globalMessage['anvizent.package.button.no']);
		
	}
	$("#YesOrNoOption").show();
	$("#editAllSourcesHavingSameSetOfHeaders").show();
	 $("#undoAllSourcesHavingSameSetOfHeaders").hide();
	 $("#proceed").hide();
	 $("#proceedForMapping").attr("disabled",false);
	 $("#isFileHavingSameColumnNamesDiv").hide();
	 
});

$(".viewDerivedTableQuery").click(function(){
	 
    var packageId = $("#packageId").val();
	var userID = $("#userID").val();
	var derived_table_name = $(this).attr("data-derivedTable");
	var targetTableId = $(this).attr("data-targetTableId");
	var	tableId = $(this).attr("data-tableId");
	
	  var selectData = {
			    packageId : packageId,
			    derived_table_name : derived_table_name,
			    targetTableId : targetTableId,
			    tableId : tableId,
		    }
		    
    var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	headers[header] = token;
	
	if(tableId != '' && packageId != ''){
		     showAjaxLoader(true);
		     var url_getDerivedTableQuery = "/app/user/"+userID+"/package/getDerivedTableQuery";
			 var myAjax = common.postAjaxCallObject(url_getDerivedTableQuery,'POST', selectData,headers);
		     myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    	  if(result != null && result.hasMessages){
		    			  if(result.messages[0].code == "SUCCESS") {
		    				  customPackage.viewDerivedTableQuery(result.object,derived_table_name);
			    		  }	else{
			    			  common.showErrorAlert(result.messages[0].text);
			    			  return false;
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
function getTimezoneName() {
    timezone = jstz.determine()
    return timezone.name();
} 

$(document).on("click",".updatePackageName",function(){
	debugger
	var userID = $("#userID").val();
	var packageName = $("#packageName").val();
	var packageID = $("#packageId").val();
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	headers[header] = token;
	showAjaxLoader(true);
	selectData = {
			packageName : packageName,
			packageID : packageID
	}
    var url_updatePackageName = "/app/user/"+userID+"/package/updatePackageName";
	var myAjax = common.postAjaxCallObject(url_updatePackageName,'POST',selectData,headers);
    myAjax.done(function(result) {
   	showAjaxLoader(false);
   	  if(result != null && result.hasMessages){
   		  if(result.messages[0].code=="ERROR"){
			  common.showErrorAlert(result.messages[0].text);
		  }
   		  if(result.messages[0].code=="DUPL_PACK_NAME"){
			  common.showErrorAlert(result.messages[0].text);
		  }
		  if(result.messages[0].code=="SUCCESS"){
			  common.showSuccessAlert(result.messages[0].text);
		  }
   	  }else{
	    		var messages = [ {
	    			code : globalMessage['anvizent.message.error.code'],
	    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
	    		} ];
	    		common.displayMessages(messages);
	    	}
   });
    
    
})
$("#targetTableStructure tbody").on("click",".primaryKey",function(){
	debugger
	if($(this).find(".m-primary").is(":checked")){
		$(this).parents('tr').find(".m-notnull").prop("checked",true);
	}else{
		$(this).parents('tr').find(".m-notnull").prop("checked",false);
	}
})

$("#targetTableStructure tbody").on("click",".autoIncrement",function(){
	debugger
	var status = false;
	var $this = $(this).parents('tr');
	if($(this).find(".m-auto").is(":checked")){
		status = true;
	}
	if(status){
		$("#targetTableStructure tbody").find('.m-auto').prop("disabled",true);
		$(this).find(".m-auto").prop("disabled",false);
		$this.find(".m-notnull,.m-primary").prop("checked",true);
		$("#targetTableStructure tbody").find('.m-auto').prop("checked",false);
		$(this).find(".m-auto").prop("checked",true);
		$(this).find(".m-default").prop("disabled",true);
	}else{
		$("#targetTableStructure tbody tr").each(function(index, value ){
			var row = $(this);
			var dataType = row.find('.m-datatype').val();
			if((dataType == "INT" || dataType == "BIGINT" || dataType == "TINYINT" || dataType == "SMALLINT" || dataType == "MEDIUMINT")){
				if(status)
				row.find(".m-notnull,.m-primary").prop("checked",false);
				row.find(".m-auto").prop("disabled",false);
			}
			
		});
	}
})
customPackage.getDataTypeList();
$(document).on("click","#editTargetTableStructure",function(){
	customPackage.editTargetTableStructureTable(resultOfTargetTable);
	$("#saveTargetTableStructure").show();
	$("#editTargetTableStructure").hide();
});
var tableName = '';
$(document).on("click","#saveTargetTableStructure",function(){
	debugger
	var userID = $("#userID").val();
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	headers[header] = token;
	tableName = $(this).parents("#viewTargetTableStructurePopUp").find("#viewTargetTableHeader").text();
	
	var dataColumnSizeStatus = customPackage.checkNewColumnSize();
	
	if(!dataColumnSizeStatus){
		customPackage.saveEditedTargetTable(tableName);
	}else{
		$("#columnSizeMisMatchAlert").modal('show');
	}
});

$(document).on("click","#proceedEditedTargetTbl",function(){
	customPackage.saveEditedTargetTable(tableName);
	$("#columnSizeMisMatchAlert").modal('hide');
})
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
