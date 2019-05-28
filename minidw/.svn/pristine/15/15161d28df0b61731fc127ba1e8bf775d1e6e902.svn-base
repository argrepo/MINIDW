var headers = {};
var dataTypeResult = "";
var dataTypeSelect = "";
var easyquerybuilder = {
		initialPage : function() {			
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
	},
	
	
	addColumnsToSeletedTable:function(result ,container,checked){
		 if(checked === true){
			 	for(var j=0;j<result.length;j++){						 
			 		container.append("<div class='column-attr' title='"+result[j].columnName+"'>"+					  	
		  		     "<label class='column-attr-label'>"+
		  		   "<input type='checkbox' name='columnName' value='"+result[j].columnName+"' checked class='checkbox-cursor'>"+
		  		   "<input type='hidden' name='columnDataType' value='"+result[j].dataType+"'>"+
		  		   "<input type='hidden' name='columnDataTypeSize' value='"+result[j].columnSize+"'>"+
		  		 "</label>"+result[j].columnName+"</div>"); 
		   }
		 }else{
				for(var j=0;j<result.length;j++){						 
			 		container.append("<div class='column-attr' title='"+result[j].columnName+"' >"+					  	
		  		 "<label class='column-attr-label'>"+
		  		 "<input type='checkbox' name='columnName' value='"+result[j].columnName+"' class='checkbox-cursor'>"+
		  		 "<input type='hidden' name='columnDataType' value='"+result[j].dataType+"'>"+
		  		 "<input type='hidden' name='columnDataTypeSize' value='"+result[j].columnSize+"'>"+
		  		 "</label>"+result[j].columnName+"</div>"); 
		   }
		 }
		
	},
	getSchemaRelatedTablesAndColumns:function(requestingFunction,schemaList){
		$(".addedColumnsDiv,.joinConditionsDiv,.whereConditionsDiv, .joinDiv,.orderByConditionsDiv,.queryValidatemessageDiv,.calculatedColumnDiv").empty();
		$(".allTableListDiv,.addColumns,.clearSelections,.joinsBlock,.tableWithColumns").hide();
		$("#checkTablePreview,#saveILConnectionMapping,#disableQuery").hide();		
		if(requestingFunction == "load"){
			$(".queryholder,#checkQuerySyntax,#enableQuery").show();			
		}
		else{
			$(".queryholder,#checkQuerySyntax,#enableQuery").hide();
			$("#queryScript").val("");
		}		
		$(".addedColumnsDivText").show();
		 
		var schema =  schemaList ;
		 
		var userID = $("#userID").val();
		var connectionId = $('#connectionId').val();
		var isDDlayout = $('#isDDlayout').val();
		var isApisData = $("#isApisData").val();
		var selectedData = {
				connectionId : 	connectionId,
				database : {
					schema : schema 
				},
				ddLayout : isDDlayout,
				apisData : isApisData
		}
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true);
		console.log(selectedData);
		var url_getSchemaRelatedTables = "/app/user/"+userID+"/package/getSchemaRelatedTablesAndColumns";
		if(connectionId != null && connectionId != '' && schema != "-------- Select Schema --------"){
		   var myAjax = common.loadAjaxCall(url_getSchemaRelatedTables,'POST',selectedData,headers);
		   myAjax.done(function(result) {	
			   showAjaxLoader(false);
		    	  if(result != null && result.hasMessages) {
		    		  if(result.messages[0].code == "ERROR"){	    			  
		    			  $("#existingSchemaErrorAlerts .modal-body").empty();
		    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
		    			  $("#existingSchemaErrorAlerts .modal-body").append(message);
		    			  $("#existingSchemaErrorAlerts").modal('show');
		    			  $(".allTableListDiv,.addColumns,.clearSelections,.tableWithColumns").hide();		    			  
		    			  $(".queryValidatemessageDiv").empty();
		    		  }
		    		  else if(result.messages[0].code == "SUCCESS"){		    			  
		    			  easyquerybuilder.buildSchemaRelatedTablesAndColumnsList(result);		    			  
		    			  $('html, body').animate({scrollTop: $('.allTableListDiv').offset().top}, 400);
		    		  } 
		    	  }	else{
			    		var messages = [ {
			    			code : globalMessage['anvizent.message.error.code'],
			    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
			    		} ];
			    		common.displayMessages(messages);
			    	}    	  	
		    });
		} else{
			 showAjaxLoader(false);
		}
	},	
	showMessage:function(text){
		$(".messageText").empty();
		$(".successMessageText").empty();
		$(".messageText").html(text);
	  $(".message").show();
	  setTimeout(function() { $(".message").hide(); }, 10000);
	},
	
	buildSchemaRelatedTablesAndColumnsList : function(result){		
		var container = $(".allTableListDiv");
		container.empty();
		$(".tableWithColumns,.allTableListDiv,.addColumns,.clearSelections").show();
		var object = result.object;		
		
		var title = "<div data-role='header' data-position='fixed' style='font-size: 14px;font-weight: normal;border-bottom: 1px solid #ccc;padding:11px 5px;background-color: #97B2C3;color: #333;'>"+globalMessage['anvizent.package.label.entitiesAndAtrributes']+"</div>";
		var allTableListDiv = title;		 
		var searchBox = "<div class='input-group stylish-input-group'>"+
							"<input type='text' class='form-control' placeholder='"+globalMessage['anvizent.package.label.search']+"' id='searchTable'>"+
							"<span class='input-group-addon'>"+		 
								"<span class='glyphicon glyphicon-search'></span>"+	 
							"</span>"+
						"</div>";
		allTableListDiv+=searchBox;
	 
		allTableListDiv+="<div class='coveringDiv' style='overflow:auto;max-height:490px;height:490px;background-color:#fff;'>";
			for(var i=0; i<object.length; i++){			
				
				var row = "<div class='tableList-child' style='cursor:pointer;'>" +
						  "<div class='tableList-child-node tableCollapse' title='"+object[i].tableName+"'>"+
						  "<a href='javascript:void(0)' class='tableList-child-node-button'><i class='fa fa-plus' aria-hidden='true'></i></a>"+
						  "<label class='table-attr-label'>"+
						  "<input type='checkbox' name='checkAllColumns' class='tableCollapseChecked' value='"+object[i].tableName+"' class='checkbox-cursor'>"+
						  "<input type='hidden' name='tableAliasName' value='t"+i+"'>"+
						  "</label><span class='text-elipse'>"+object[i].tableName+"</span></div>";
						   
				var columns = object[i].columns;
				row+= "<div class='columnList-child-node' style='display:none;padding-left: 30px;cursor:auto;'>";
			  	  	
					/*	for(var j=0;j<columns.length;j++){						 
							row+="<div class='column-attr' title='"+columns[j].columnName+"' >"+					  	
						  		 "<label class='column-attr-label'>"+
						  		 "<input type='checkbox' name='columnName' value='"+columns[j].columnName+"'>"+
						  		 "<input type='hidden' name='columnDataType' value='"+columns[j].dataType+"'>"+
						  		 "<input type='hidden' name='columnDataTypeSize' value='"+columns[j].columnSize+"'>"+
						  		 "</label>"+columns[j].columnName+"</div>"; 
						}*/
				
				row+="</div></div>";					  
				allTableListDiv+=row;				 
			}
			allTableListDiv+="</div>";
			allTableListDiv+="<div><button title='"+globalMessage['anvizent.package.label.addColumns']+"' type='button' class='btn btn-primary addColumns' >"+globalMessage['anvizent.package.label.add']+"</button>"+	
									"<button  title='"+globalMessage['anvizent.package.label.clearSelection']+"' type='button' class='btn btn-danger clearSelections' >"+globalMessage['anvizent.package.label.remove']+"</button>" +
							  "</div>"
			container.append(allTableListDiv);		 
	},
	
	addColumns : function(object){		 
		var container = $(".addedColumnsDiv");
	    container.empty();		
	    var isDDlayout = $("#isDDlayout").val();
	    var isApisData = $("#isApisData").val();
		var aggForStringsOrBits = ["Count"];
		var aggForIntegers = ["Sum","Count","Min","Max","Avg"];
		var aggForDates = ["Count","Min","Max"];		
		var databaseTypeId = $("#connector_id").val();
		var protocal = $("#dbProtocal").val();
		for(var i=0;i<object.length;i++){
	 
			var row = 	"<div id='accordion-first' class='clearfix'><div class='accordion' id='accordion"+i+"'>" +
							"<div class='accordion-group columns-block panel panel-default'>" ;
			  
				row +=	"<div class='accordion-heading table-title' title='"+object[i].tableName+'('+object[i].tableAliasName+')'+"' style='border: 1px solid #dce1e4;'>"+
				"<input type='hidden' name='table' class='tableName' value='"+object[i].tableName+"'>"+
				"<input type='hidden' name='alias' value='"+object[i].tableAliasName+"'>" +
				"<a class='accordion-toggle collapsed' data-toggle='collapse' data-parent='#accordion"+i+"' href='#collapse"+i+"' aria-expanded='false'>"+
				"<span class='glyphicon glyphicon-minus-sign'></span>"+object[i].tableName+" ("+object[i].tableAliasName+") </a></div>";
			 	 				
				row+="<div class='collapse in accordion-body' id='collapse"+i+"'><table class='table table-striped table-bordered tablebg addedColumns'><thead class='columnHeader'>" +
				 	"<tr><th>"+globalMessage['anvizent.package.label.columnName']+"</th><th>"+globalMessage['anvizent.package.label.aliasName']+"</th><th>"+globalMessage['anvizent.package.label.dataType']+"</th><th></th><th></th><th></th></tr></thead><tbody>";
				
				for(var j=0;j<object[i].columnNames.length;j++){
					var columnNames = object[i].columnNames;
					var columnDataType = object[i].columnDataType;
					var dataType = columnDataType[j].toUpperCase();
					var columnsDataTypeSize =object[i].columnsDataTypeSize;
					 
					var applicableAggregation = [];
					var aggregations="";
					if(     dataType == "VARCHAR" || dataType == "BIT" || dataType == "CHAR" || dataType == "VARCHAR2" || dataType == "NVARCHAR" || 
							dataType == "TEXT" || dataType == "PICKLIST" || dataType == "REFERENCE" || dataType == "TEXTAREA" || dataType == "PHONE" || 
							dataType == "URL" || dataType == "LONGTEXTAREA" || dataType == "ID" || dataType == "NCHAR" || dataType == "NTEXT"  ||
						    dataType == "CHARACTER" ||   dataType == "VARBINARY" || dataType == "UNIQUEIDENTIFIER" || dataType == "BOOLEAN" || dataType == "CHARACTER VARYING" || dataType =="BOOL" ||
						    dataType == "CHARACTER VARYING" || dataType =="BOOL" || dataType == "LONGVARBINARY" ||  dataType == "LONGVARCHAR" ){
						applicableAggregation = aggForStringsOrBits;
					}					
					else if(dataType == "BIGINT" || dataType == "DECIMAL" || dataType == "INT" || dataType == "FLOAT" || dataType == "NUMBER" ||
							dataType == "DOUBLE" || dataType == "NUMERIC" || dataType == "INTEGER" || dataType == "LONG" || dataType == "MEDIUMINT" || 
							dataType == "REAL" || dataType == "SMALLINT" || dataType == "TINYINT" || dataType =="MONEY" || dataType =="INT UNSIGNED" ||
							dataType == "SMALLSERIAL" || dataType =="SERIAL" || dataType == "BIGSERIAL" ||   dataType == "VARBIT"  || dataType == "BIT VARYING"){
						applicableAggregation = aggForIntegers;
					}
					
					else if(dataType == "DATETIME" || dataType == "DATE" || dataType == "TIMESTAMP" || dataType == "TIME" || dataType == "YEAR" || 
							dataType == "DATETIME2" || dataType == "DATETIMEOFFSET" || dataType == "SMALLDATETIME" ||
							dataType == "TIMESTAMP WITHOUT TIME ZONE" || dataType == "TIMESTAMP WITH TIME ZONE" || dataType == "TIME WITHOUT TIME ZONE" ||  dataType =="TIME WITH TIME ZONE"){
						applicableAggregation = aggForDates;
					}
					 
					aggregations  = "<ul class='dropdown-menu'><li class='aggregation'><a tabindex='-1'>Default</a></li>";
					for(var k =0;k<applicableAggregation.length;k++){						 
						aggregations+="<li class='aggregation'><a tabindex='-1'>"+applicableAggregation[k]+"</a></li>";
					}
					aggregations+=  "</ul>"; 
					var aliasColumnName = '';
					//mysql-1, oracle-4 , db2-5 ,Salesforce-6 , DB2AS400-7  ,postgresql-8
					if(protocal.indexOf('mysql') != -1  || protocal.indexOf('oracle') != -1  || protocal.indexOf('db2') != -1 || protocal.indexOf('sforce') != -1 ||  protocal.indexOf('as400') != -1   || protocal.indexOf('ucanaccess') != -1   || protocal.indexOf('postgresql') != -1  ||  protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1){
						if(isDDlayout == 'true' || isApisData == 'true' ){
							aliasColumnName = columnNames[j];
						}else{
							aliasColumnName = columnNames[j]+'_'+object[i].tableAliasName;
						}
					}
					//sqlserver-2 ||  odbc - 10
					else if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
						var lastChar = columnNames[j] ;
						if(lastChar[lastChar.length-1] === ']')
						aliasColumnName =  columnNames[j].replace(']', '_'+object[i].tableAliasName+']');
						else
						aliasColumnName	=  columnNames[j]+'_'+object[i].tableAliasName;
					}
					row+= "<tr class='duplicateColumn'><td><input type='text' name='aggregatedColumnName' value='"+columnNames[j]+"' title='"+columnNames[j]+"'  disabled='disabled' class='table-input-box form-control input-sm'>" +
							       "<input type='hidden' name='originalColumnName' value='"+columnNames[j]+"'>" +
							       "<input type='hidden' name='aggregation'></td>" +
							   "<td><input type='text' name='columnAliasName' value='"+aliasColumnName+"' title='"+aliasColumnName+"' class='table-input-box form-control input-sm'></td>"+
							   "<td><input type='text' value='"+columnsDataTypeSize[j]+"' title='"+columnsDataTypeSize[j]+"' class='table-input-box form-control input-sm' disabled='disabled'></td>"+							 
							   "<td><div class='dropdown'><button class='btn btn-primary dropdown-toggle' type='button' data-toggle='dropdown' title='"+globalMessage['anvizent.package.label.aggregation']+"'><img src='"+adt.appContextPath+"/resources/images/aggrigateRow.svg'/ style='filter:invert(80%); -webkit-filter: invert(80%);'></span></button>"+aggregations+"</div></td>" +
							   "<td><button class='btn btn-primary btn-sm addDuplicateRow' title='"+globalMessage['anvizent.package.label.createsDuplicateColumn']+"'><img src='"+adt.appContextPath+"/resources/images/copyRow.svg'/ style='filter:invert(80%); -webkit-filter: invert(80%);'></button></td>" +
							   "<td><button class='btn btn-primary btn-sm deleteColumn' title='"+globalMessage['anvizent.package.label.delete']+"' table-name='"+object[i].tableName+"'><span class='glyphicon glyphicon-trash' aria-hidden='true'  style='color:#fff;'></span></button>" +							   
							   "</td>" +
						  "</tr>";					
				}
				row+="</tbody></table></div></div></div></div>" 			
				container.append(row); 
		}			
	},
	whereCondition : function(object){
		var container = $(".whereConditionsDiv");
		container.empty();		
		var row = "<div class='columns-block panel panel-default'><div class='table-title'><h4>"+globalMessage['anvizent.package.label.whereConditions']+"</h4></div>";
		
		row+="<div class='table-responsive'><table class='table table-striped table-bordered table-fixed-new tablebg addedWhereConditions'><thead class='columnHeader'><tr><th class='col-md-2 col-lg-2'>"+globalMessage['anvizent.package.label.tableName']+"</th><th class='col-md-2 col-lg-2'>"+globalMessage['anvizent.package.label.columnName']+"</th><th class='col-md-2 col-lg-2'>"+globalMessage['anvizent.package.label.clauses']+"</th><th class='col-md-4 col-lg-4'>"+globalMessage['anvizent.package.label.inputValue']+"</th class='col-md-2 col-lg-2'><th>&nbsp;</th></tr></thead><tbody>";
				
		var tableSelect = "<select class='selectTable firstSelectTable j-tablename table-responsive table-select form-control'>";
			tableSelect+="<option value='Select Table' data-tablename='Select Table'>"+globalMessage['anvizent.package.label.selectTable']+"</option>"
		for(var i=0;i<object.length;i++){
			tableSelect+="<option value='"+object[i].tableAliasName+"' data-tableName='"+object[i].tableName+"'>"+object[i].tableName+"</option>";
		}
		tableSelect+="</select>";
		
		var columnSelect = "<select class='selectColumn j-tablename table-select-input-box form-control'><option value='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option></select>";
		var clauses =   "<select class='selectClauses j-tablename form-control'><option value='Select Clause'>"+globalMessage['anvizent.package.label.clause']+"</option></select>";
		var inputText = "<input type='text' disabled='disabled' placeholder='"+globalMessage['anvizent.package.label.inputValue']+"' name='inputValue' class='j-tablename table-select-input-box form-control'>";
		row+="<tr>" + 
				"<td class='col-md-2 col-lg-2'>"+tableSelect+"</td>" +
				"<td class='col-md-2 col-lg-2'>"+columnSelect+"</td>" +
				"<td class='col-md-2 col-lg-2'>"+clauses+"</td>" +
				"<td class='col-md-2 col-lg-3'>"+inputText+"</td>" +				
				"<td class='addOneMoreColumntd col-md-3 col-lg-3'><button  class='btn btn-primary btn-sm addOneMoreColumn'><i class='fa fa-plus' aria-hidden='true'></i></button></td>" +
			"</tr>"; 
		row+="</tbody></table></div></div>";
	    container.append($(row));
	},
	customColumn : function(object){
		var container = $(".calculatedColumnDiv");
		container.empty();		
		var columnsForCreateCustomColumn = "<div style='overflow: auto; max-height:138px;border:1px;'><fieldset class='columnsForCreateCustomColumn' id='columnsForCreateCustomColumn'>";
		for(var i=0;i<object.length;i++){
			for(var j=0;j<object[i].columnNames.length;j++){
				var columnNames = object[i].columnNames;
				var columnDataType = object[i].columnDataType;
				var dataType = columnDataType[j].toUpperCase();
				  if(dataType == "BIGINT" || dataType == "DECIMAL" || dataType == "INT" || dataType == "FLOAT" || dataType == "NUMBER" ||
						dataType == "DOUBLE" || dataType == "NUMERIC" || dataType == "INTEGER" || dataType == "LONG" || dataType == "MEDIUMINT" || 
						dataType == "REAL" || dataType == "SMALLINT" || dataType == "TINYINT" || dataType =="MONEY" || dataType =="INT UNSIGNED"){
					    columnsForCreateCustomColumn +=  "<label class='availableColumnsForCustomColumn' title="+object[i].tableAliasName+"."+columnNames[j]+">"+object[i].tableAliasName+"."+columnNames[j]+"</label>" ;
				  }
				} 
			}
		columnsForCreateCustomColumn += "</fieldset></div>";
		var row = "<div class='columns-block panel panel-default'><div class='table-title'><h4>"+globalMessage['anvizent.package.label.addcustomColumn']+"</h4></div>";
		row+="<table class='table table-striped table-bordered tablebg table-responsive table-fixed-three addedCustomColumn'><thead class='columnHeader'><tr><th class='col-xs-6' style='padding-right:0'>"+globalMessage['anvizent.package.label.newColumnName']+"</th><th class='col-xs-4' style='padding-left:0'>"+globalMessage['anvizent.package.label.availableColumns']+" </th><th class='col-xs-2'>&nbsp;</th></tr></thead><tbody>";
		var customColumnNameAndFormula = "<input type='text' placeholder='"+globalMessage['anvizent.package.label.columnName']+"' name='customColumnName' class='j-customColumnName table-input-box form-control input-sm'>"+
		                        "<label>"+globalMessage['anvizent.package.label.customColumnFormula']+" :</label><textarea rows='4' disabled='disabled' cols='100' placeholder='"+globalMessage['anvizent.package.label.customColumnFormula']+"' name='customColumnFormulaInputValue' class='j-customColumnFormula form-control input-sm'></textarea>";
		row+="<tr>" + 
				"<td class='col-md-6'>"+customColumnNameAndFormula+"</td>" +
				"<td class='col-md-4'>"+columnsForCreateCustomColumn+"</td>" +
				"<td class='addOneMoreCustomColumntd col-md-2'><button  class='btn btn-primary btn-sm addOneMoreCustomColumn'><i class='fa fa-plus' aria-hidden='true'></i></button>" +
				"&nbsp;<button  class='btn btn-primary btn-sm deleteRow' style='display:none' title='"+globalMessage['anvizent.package.label.delete']+"'><span class='glyphicon glyphicon-trash'  style='color:#fff;'></span></button></td>" +
			"</tr>"; 
		row+="</tbody></table></div>";
	    container.append($(row));
	},
	addSelectedTableColumns : function(object,container){					
		var options="<option value='Select Column' data-columnName='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option>";
		for(var i=0;i<object.length;i++){
			options+="<option value='"+object[i].dataType.encodeHtml()+"' data-columnName='"+object[i].columnName.encodeHtml()+"'>"+object[i].columnName.encodeHtml()+"</option>";
		}
		container.append(options)
	},
	
	joinCondition : function(object){
		var jtablename="<option value='Select Table' selected data-tablename='Select Table'>"+globalMessage['anvizent.package.label.selectTable']+"</option>";
		var container = $(".joinConditionsDiv");
		container.empty();
		$(".joinDiv").empty();	 
		
		var noOfTables = object.length,count=object.length;
		var joinTables="";
		for(var i=0;i<noOfTables;i++){			
			joinTables+="<div class='row'>" +
						"<div class='col-sm-12 col-md-6 col-xs-12 col-lg-6'><select class='j-tablename joinTable col-md-12 col-xs-12 col-sm-12 form-control' id='joinTable"+i+"'></select></div>";
			if(count != 1)
				joinTables+="<div class='col-sm-12 col-md-6 col-xs-12 col-lg-6'>" +
						"<select class='selectJoins j-tablename col-md-12 col-xs-12 col-sm-12 form-control' id='selectJoins"+i+"'>" +
								/*"<option value='Select Join'>Select Join</option>" +*/
								"<option value='INNER JOIN'>"+globalMessage['anvizent.package.label.innerJoin']+"</option>" +
								"<option value='LEFT OUTER JOIN'>"+globalMessage['anvizent.package.label.leftOuterJoin']+"</option>" +
								"<option value='RIGHT OUTER JOIN'>"+globalMessage['anvizent.package.label.rightOuterJoin']+"</option>" +
						"</select><span>"+globalMessage['anvizent.package.label.with']+"</span></div>";
			
			joinTables+="</div>";
			count--;
		}
		$(".joinDiv").append(joinTables); 
		 
		var row="<table class='table table-striped table-bordered tablebg table-fixed-new table-responsive addedJoins'><thead class='columnHeader'><tr><th>"+globalMessage['anvizent.package.label.tableName']+"</th><th>"+globalMessage['anvizent.package.label.columnName']+"</th><th>"+globalMessage['anvizent.package.label.tableName']+"</th><th>"+globalMessage['anvizent.package.label.columnName']+"</th><th>&nbsp;</th></tr></thead><tbody>";
				
		var tableSelectFirst = "<select class='selectJoinFirstTable firstJoinSelectTable j-tablename table-select form-control'>";
			tableSelectFirst+="<option value='Select Table' data-tablename='Select Table'>"+globalMessage['anvizent.package.label.selectTable']+"</option>";
		var tableSelectSecond = "<select class='selectJoinSecondTable secondJoinSelectTable j-tablename table-select form-control'>";
			tableSelectSecond+="<option value='Select Table' data-tablename='Select Table'>"+globalMessage['anvizent.package.label.selectTable']+"</option>";
		
		for(var i=0;i<object.length;i++){
			jtablename+="<option value='"+object[i].tableAliasName+"' data-tableName='"+object[i].tableName+"'>"+object[i].tableName+"</option>";
			tableSelectFirst+="<option value='"+object[i].tableAliasName+"' data-tableName='"+object[i].tableName+"'>"+object[i].tableName+"</option>";
			tableSelectSecond+="<option value='"+object[i].tableAliasName+"' data-tableName='"+object[i].tableName+"'>"+object[i].tableName+"</option>";
		}
		
		tableSelectFirst+="</select>";
		tableSelectSecond+="</select>";
		var columnSelectFirst = "<select class='selectJoinColumnFirst j-tablename table-select-input-box form-control'><option value='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option></select>";
		var columnSelectSecond = "<select class='selectJoinColumnSecond j-tablename table-select-input-box form-control'><option value='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option></select>";
				
		row+="<tr>" +
				"<td>"+tableSelectFirst+"</td>" +
				"<td>"+columnSelectFirst+"</td>" +				
				"<td>"+tableSelectSecond+"</td>" +
				"<td>"+columnSelectSecond+"</td>" +
				"<td class='addOneMoreColumnJointd'><button  class='btn btn-primary btn-sm addOneMoreJoinColumn'><i class='fa fa-plus' aria-hidden='true'></i></button></td>" +
			"</tr>"; 
	   
	   container.append(row);
	   
	   var index = 1;
	   $(".joinTable").each(function(){
		  $(this).append(jtablename);
		  $(this).find("option")[index].selected = true;
		  index++;
	   });
	   
	},
	addSelectedTableJoinColumnsFirst : function(object,container){		
		var options="<option value='Select Column'>Select Column</option>";
		for(var i=0;i<object.length;i++){
			options+="<option value='"+object[i].dataType+"'>"+object[i].columnName+"</option>";
		}
		container.append(options)
	},
	  
	orderByCondition : function(object){
		var container = $(".orderByConditionsDiv");
		container.empty();		
		var row = "<div class='columns-block panel panel-default'><div class='table-title'><h4>"+globalMessage['anvizent.package.label.orderByColumnSorting']+"</h4></div>";
		
		row+="<div class='table-responsive'><table class='table table-striped table-bordered table-fixed-two tablebg addedOrderByConditions'><thead class='columnHeader'><tr><th class='col-md-3 col-lg-3 col-sm-3'>"+globalMessage['anvizent.package.label.tableName']+"</th><th class='col-md-3 col-lg-3 col-sm-3'>"+globalMessage['anvizent.package.label.columnName']+"</th><th class='col-md-3 col-lg-3 col-sm-3'>"+globalMessage['anvizent.package.label.orderBy']+"</th><th class='col-md-3 col-lg-3 col-sm-3'>&nbsp;</th></tr></thead><tbody>";
				
		var tableSelect = "<select class='oderBySelectTable firstOderBySelectTable j-tablename table-responsive table-select form-control'>";
			tableSelect+="<option value='Select Table' data-tablename='Select Table'>"+globalMessage['anvizent.package.label.selectTable']+"</option>"
		for(var i=0;i<object.length;i++){
			tableSelect+="<option value='"+object[i].tableAliasName+"' data-tableName='"+object[i].tableName+"'>"+object[i].tableName+"</option>";
		}
		tableSelect+="</select>";
		
		var columnSelect = "<select class='selectOrderByColumn j-tablename table-select-input-box form-control'><option value='Select Column' data-columnName='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option></select>";
		var orderBy =   "<select class='orderBy j-tablename form-control'><option value='Desc'>Desc</option><option value='Asc'>Asc</option></select>";
		
		row+="<tr>" +
				"<td class='col-md-3 col-lg-3 col-sm-3'>"+tableSelect+"</td>" +
				"<td class='col-md-3 col-lg-3 col-sm-3'>"+columnSelect+"</td>" +
				"<td class='col-md-3 col-lg-3 col-sm-3'>"+orderBy+"</td>" +					
				"<td class='addOneMoreOrderByColumntd col-md-3 col-lg-3 col-sm-3'><button  class='btn btn-primary btn-sm addOneMoreOrderByColumn'><i class='fa fa-plus' aria-hidden='true'></i></button></td>" +
			"</tr>"; 
		row+="</tbody></table></div></div>";
	   container.append(row);		  
	},
	collectAllSelections : function(){
		
		var object = [], tablesAndColumns = [], joinTableAndColumns = [], whereTableAndColumns = [],orderByColumns=[],groupByColumns=[],nonGroupByColumns = [] , customColumns = [];
		var joinTables=[],jointypes = {};  
		var i=0; 
		//find all selected tables and respective columns && group by columns
		$(".addedColumns").each(function(){
			var columnNames = [];
			var groupByColumnNames = [];
			var tableName = $(this).parents(".columns-block").find("input[name='table']").val();
			var tableAliasName = $(this).parents(".columns-block").find("input[name='alias']").val();		
			$(this).find("tbody tr").each(function(){				 
					var aggregatedColumnName = $(this).find("input[name='aggregatedColumnName']").val();
					var originalColumnName = $(this).find("input[name='originalColumnName']").val();
					var columnAliasName = $(this).find("input[name='columnAliasName']").val();
					var aggregation = $(this).find("input[name='aggregation']").val();
					columnNames.push({"originalColumnName":originalColumnName,"columnAliasName":columnAliasName,"aggregatedColumnName":aggregatedColumnName,"aggregation":aggregation});
					
					if(aggregatedColumnName == originalColumnName){
						groupByColumnNames.push({"originalColumnName":originalColumnName});
					} 
					else{
						nonGroupByColumns.push({"originalColumnName":originalColumnName});
					}
						
			});
			i++;
			tablesAndColumns.push({"tableName":tableName,"tableAliasName":tableAliasName,"columnNames":columnNames});
			groupByColumns.push({"tableName":tableName,"tableAliasName":tableAliasName,"columnNames":groupByColumnNames});
		});		 
				
		//find all selected join tables		
		$("select.joinTable").each(function(){			
			var table = $(this).find("option:selected").attr("data-tablename");
			var tableAlias = $(this).val();			
			if(table != "Select Table"){			 
				joinTables.push({"table":table,"alias":tableAlias});
			}			 
		});
		
		//find all selected joins & respective tables	
		$('select.selectJoins').each(function() {
			var jointype = $(this), 
			row = jointype.closest('.row');			
			var jointable = row.find('.joinTable option:selected').attr("data-tablename");
			var join = jointype.val();
			if(jointable != "Select Table" && join != "Select Join")
				jointypes[jointable] = join;
		});
		
		//find all selected join columns
		$(".addedJoins").each(function(){
			$(this).find("tbody tr").each(function(){				
				
				var firstJoinTable = $(this).find(".selectJoinFirstTable option:selected").text();
				var firstJoinTableColumn = $(this).find(".selectJoinColumnFirst option:selected").text();
				 
				var secondJoinTable = $(this).find(".selectJoinSecondTable option:selected").text();
				var secondJoinTableColumn = $(this).find(".selectJoinColumnSecond option:selected").text();
				
				var tableAliasNameFirst = $(this).find(".selectJoinFirstTable").val();
				var tableAliasNameSecond = $(this).find(".selectJoinSecondTable").val();
				
				if(firstJoinTable != "Select Table" && secondJoinTable != "Select Table" && firstJoinTableColumn != "Select Column" && secondJoinTableColumn != "Select Column"){
					joinTableAndColumns.push({"firstJoinTable":firstJoinTable,"firstJoinTableColumn":firstJoinTableColumn,"tableAliasNameFirst":tableAliasNameFirst,"secondJoinTable":secondJoinTable,"secondJoinTableColumn":secondJoinTableColumn,"tableAliasNameSecond":tableAliasNameSecond});					
				}
			});
		});
		
		
		//find all selected where tables and respective columns
		$(".addedWhereConditions").each(function(){			
			$(this).find("tbody tr").each(function(){
				
				var selectTable = $(this).find(".selectTable option:selected").text();
				var selectColumn = $(this).find(".selectColumn option:selected").text();
				var selectClauses = $(this).find(".selectClauses").val();
				var inputValue = $(this).find("input[name='inputValue']").val();
				var tableAliasName = $(this).find(".selectTable").val();
				var columnDataType = $(this).find(".selectColumn option:selected").val();
				
				if(selectTable != "Select Table" && selectColumn != "Select Column" && selectClauses != "Select Clause" && (inputValue != "" || selectClauses == "IS NOT NULL" || selectClauses == "IS NULL")){
					whereTableAndColumns.push({"selectTable":selectTable,"selectColumn":selectColumn,"selectClauses":selectClauses,"inputValue":inputValue,"tableAliasName":tableAliasName,"columnDataType":columnDataType});
				}				 
			});			
		});
		
		//find all selected where tables and respective columns
		$(".addedCustomColumn").each(function(){			
			$(this).find("tbody tr").each(function(){
				var customColumnName = $(this).find(".j-customColumnName").val().trim();
				var customColumnFormula = $(this).find(".j-customColumnFormula").val().trim();
				 var regex = /^[0-9a-zA-Z\_]+$/;
				if(customColumnName.length != 0 && customColumnFormula.length != 0){
					common.clearcustomsg($(this).find(".j-customColumnName"));
					if ( regex.test(customColumnName) ) {
						
						if(customColumnFormula.toLowerCase().indexOf("sum") >= 0 || customColumnFormula.toLowerCase().indexOf("count") >= 0 ||  customColumnFormula.toLowerCase().indexOf("min") >= 0 
								|| customColumnFormula.toLowerCase().indexOf("max") >= 0  ||  customColumnFormula.toLowerCase().indexOf("avg") >= 0
						){
							nonGroupByColumns.push({"originalColumnName":customColumnName});
						}
						customColumns.push({"customColumnName":customColumnName,"customColumnFormula":customColumnFormula});
						
					} else {
						common.showcustommsg($(this).find(".j-customColumnName"),'Invalid column name');
					}
				} 		 
			});			
		});
		//find all selected order By conditions
		$(".orderByConditionsDiv").each(function(){			
			$(this).find("tbody tr").each(function(){
				var selectTable = $(this).find(".oderBySelectTable option:selected").attr("data-tablename");
				var tableAliasName = $(this).find(".oderBySelectTable").val();
				var selectColumn = $(this).find(".selectOrderByColumn option:selected").attr("data-columnname");
				var selectOrderBy = $(this).find(".orderBy").val();
				
				if(selectTable != "Select Table" && selectColumn != "Select Column"){
					orderByColumns.push({"selectTable":selectTable,"selectColumn":selectColumn,"selectOrderBy":selectOrderBy,"tableAliasName":tableAliasName});
				}				 
			});
		});	 
		object.push(tablesAndColumns,joinTableAndColumns,whereTableAndColumns,jointypes,joinTables,orderByColumns,groupByColumns,nonGroupByColumns,customColumns);
		return object;
	},
	 
	
	buildQuery : function(object){		 
		$("#saveILConnectionMapping,#checkTablePreview").hide();
		var databaseTypeId = $("#connector_id").val();
		var protocal = $("#dbProtocal").val();
 		var schema = $("#existingSchemaList option:selected").val();
		var tablesAndColumns = object[0];
		var joinTableAndColumns = object[1];
		var whereTableAndColumns = object[2];
		var jointypes = object[3];
		var joinTables = object[4];
		var orderByColumns = object[5];
		var groupByColumns = object[6];
		var nonGroupByColumns = object[7];		
		var customColumns = object[8];
		var query = []; 
		query.push("select \n");
			if(tablesAndColumns != ""){				
				var tablesNamesAndAliasTableNames = [];		 
				for(var i=0;i<tablesAndColumns.length;i++){
					tablesNamesAndAliasTableNames.push({"originalTableName":tablesAndColumns[i].tableName,"aliasTableName":tablesAndColumns[i].tableAliasName});
					
					var tableAliasName = tablesAndColumns[i].tableAliasName;
					var columnNames = tablesAndColumns[i].columnNames;
					for(var j=0;j<columnNames.length;j++){
						var column = "";
						if(columnNames[j].aggregatedColumnName.indexOf("(") > -1){
							column = columnNames[j].aggregation+"("+tableAliasName+"."+columnNames[j].originalColumnName+")"+" "+columnNames[j].columnAliasName+",";
						}
						else if(columnNames[j].aggregatedColumnName.indexOf("(") == -1){
							column = tableAliasName+"."+columnNames[j].aggregatedColumnName+" "+columnNames[j].columnAliasName+",";
						}					 
						query.push(column,"\n");				
					}
				}
				if(customColumns != ""){
					for(var k=0;k<customColumns.length;k++){
						query.push(customColumns[k].customColumnFormula+" "+customColumns[k].customColumnName+"," ,"\n" );
					} 
				}
				var lastCol = query[query.length-2].slice(0,-1);			 
				query.splice(-1,1);
				query.splice(-1,1);
				query.push(lastCol);
			}
			
			
			
			query.push("\nfrom \n");			
			// add join tables and conditions			 
			if(joinTableAndColumns != "" && jointypes != "" ){
				var tablelen = joinTables.length;
				var jtables = [];			
				
				for (var i=0; i<tablelen; i++) {					
					var addand = false;
					var secondtable;
					
					if (i>0 && i < tablelen) {						
						query.push(jointypes[secondtable], "\n");
					} 
					 
					secondtable = joinTables[i].table;	
					
					//mysql-1, oracle-4 , db2-5 ,DB2AS400-7 ,postgresql-8,
					if(protocal.indexOf('mysql') != -1  || protocal.indexOf('mysql') != -1  || protocal.indexOf('db2') != -1 ||  protocal.indexOf('as400') != -1   || protocal.indexOf('ucanaccess') != -1  || protocal.indexOf('postgresql') != -1  ||  protocal.indexOf('vortex') != -1 ){
						query.push("","",secondtable, ' ', joinTables[i].alias,"\n");
					}
					//sqlserver-2
					else if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
						query.push("","",secondtable, ' ', joinTables[i].alias,"\n");
					}
					//salesforce-6
					else if(protocal.indexOf('sforce') != -1 ){
						query.push(secondtable, ' ', joinTables[i].alias,"\n");
					}
					
					if (i>0 && i < tablelen){
						query.push('ON',"\n");
						if (joinTableAndColumns.length>0){							 
							
							for(var j=0;j<joinTableAndColumns.length;j++){
								var obj = joinTableAndColumns[j];
								var firstJoinTable = obj['firstJoinTable'], firstTableColumn = obj['firstJoinTableColumn'], ftAlias= obj["tableAliasNameFirst"];
								var secondJoinTable = obj['secondJoinTable'], secondTableColumn = obj['secondJoinTableColumn'], scAlias = obj['tableAliasNameSecond'];
								
								if (firstJoinTable === secondtable) {
									if (jtables.indexOf(secondJoinTable) !== -1) {
										if (addand)
											query.push('AND\n');
										query.push(scAlias, ".", secondTableColumn, " = ", ftAlias, ".", firstTableColumn,"\n");
										if (!addand) addand = true;
									}									
								}
								else if (secondJoinTable === secondtable) {
									if (jtables.indexOf(firstJoinTable) !== -1) {
										if (addand)
											query.push('AND\n');
										query.push(ftAlias, ".", firstTableColumn, " = ", scAlias, ".", secondTableColumn,"\n");
										if (!addand) addand = true;
									}
								}								 
							}
						} 						
					}
					jtables.push(secondtable); 
				}				 
				query.splice(-1,1);				 
			}
			
			//if join conditions are not present			 
			if(joinTableAndColumns == ""){
				for(var i=0;i<tablesNamesAndAliasTableNames.length;i++){
					var from = "";
					//mysql-1, oracle-4, db2-5 ,DB2AS400-7,postgresql-8
					if(protocal.indexOf('mysql') != -1  || protocal.indexOf('mysql') != -1  || protocal.indexOf('db2') != -1 ||  protocal.indexOf('as400') != -1   || protocal.indexOf('ucanaccess') != -1  || protocal.indexOf('postgresql') != -1  ||  protocal.indexOf('vortex') != -1 ){
						from =  tablesNamesAndAliasTableNames[i].originalTableName+" "+tablesNamesAndAliasTableNames[i].aliasTableName+",";
					}
					//sqlserver-2 || odbc - 10
					else if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
						from =  tablesNamesAndAliasTableNames[i].originalTableName+" "+tablesNamesAndAliasTableNames[i].aliasTableName+",";
					}
					// salesforce-6
					else if(protocal.indexOf('sforce') != -1 ){
						from = tablesNamesAndAliasTableNames[i].originalTableName+" "+tablesNamesAndAliasTableNames[i].aliasTableName+",";
					}
					query.push(from,"\n");
				}
				 
				var lastColFrom = query[query.length-2].slice(0,-1);				 
				query.splice(-1,1);
				query.splice(-1,1);
				query.push(lastColFrom);					 
			}
			
			//where conditions are present
			if(whereTableAndColumns != ""){				 
				query.push("\nwhere","\n");
				
				for(var i=0;i<whereTableAndColumns.length;i++){
					var where = whereTableAndColumns[i].tableAliasName+"."+whereTableAndColumns[i].selectColumn;
					
					if(whereTableAndColumns[i].selectClauses == "IS NULL" || whereTableAndColumns[i].selectClauses == "IS NOT NULL")
						query.push(where," ",whereTableAndColumns[i].selectClauses,"\n");
					else
						query.push(where," ",whereTableAndColumns[i].selectClauses);
					
					var dataType = whereTableAndColumns[i].columnDataType.toUpperCase();
					if(dataType == "VARCHAR" || dataType == "BIT" || dataType == "CHAR" || dataType == "VARCHAR2" || dataType == "NVARCHAR" || 
							dataType == "TEXT" || dataType == "PICKLIST" || dataType == "REFERENCE" || dataType == "TEXTAREA" || dataType == "PHONE" || 
							dataType == "URL" || dataType == "LONGTEXTAREA" || dataType == "ID" || dataType == "NCHAR" || dataType == "NTEXT"  ||
						    dataType == "CHARACTER" ||   dataType == "VARBINARY" || dataType == "UNIQUEIDENTIFIER" || dataType == "BOOLEAN" || dataType == "CHARACTER VARYING" || dataType =="BOOL" ||
						    dataType == "CHARACTER VARYING" || dataType =="BOOL" || dataType == "LONGVARBINARY" ||  dataType == "LONGVARCHAR" ){	
						
					 	if(whereTableAndColumns[i].selectClauses != "IS NULL" && whereTableAndColumns[i].selectClauses != "IS NOT NULL"){					 		 
							query.push(" '"+whereTableAndColumns[i].inputValue+"'","\n");
						}					 	
					}
					else{						 
						query.push(" "+whereTableAndColumns[i].inputValue,"\n");
					}
					query.push("AND","\n");
					
				}
				query.splice(-1,1);
				query.splice(-1,1);				 
				query.splice(-1,1);
			}
			//var aggForIntegers = ["Sum","Count","Min","Max","Avg"];
			// add group by conditions on selection of aggregation
			if(nonGroupByColumns != ""){
				query.push("\ngroup by","\n");
				for(var i=0;i<groupByColumns.length;i++){
					var tableAliasName = groupByColumns[i].tableAliasName;
					var columnNames = groupByColumns[i].columnNames;
					for(var j=0;j<columnNames.length;j++){
						var column = tableAliasName+"."+columnNames[j].originalColumnName;
						query.push(column);				
						query.push(",","\n");
					}
				}
				if(customColumns != ""){
					for(var k=0;k<customColumns.length;k++){
						var customColumnFormulaVal = customColumns[k].customColumnFormula;
						if(customColumnFormulaVal.toLowerCase().indexOf("sum") >= 0 || customColumnFormulaVal.toLowerCase().indexOf("count") >= 0 ||  customColumnFormulaVal.toLowerCase().indexOf("min") >= 0 
								|| customColumnFormulaVal.toLowerCase().indexOf("max") >= 0  ||  customColumnFormulaVal.toLowerCase().indexOf("avg") >= 0
						){
						}else{
						query.push(customColumnFormulaVal);
						query.push(",","\n");
						}
					} 
				}
				query.splice(-1,1);
				query.splice(-1,1);				 			
			}
			//order by conditions are present
			if(orderByColumns != ""){
				query.push("\nOrder by\n");
				for(var i=0;i<orderByColumns.length;i++){
					var orderBy = orderByColumns[i].tableAliasName+"."+orderByColumns[i].selectColumn+" "+orderByColumns[i].selectOrderBy;
					query.push(orderBy);
					query.push(",","\n");					
				} 
				query.splice(-1,1);
				query.splice(-1,1);
			} 
		
		return query;
	},
	validateJoinSelection : function(){
		var returnVal = true;
		$(".joinTable").each(function(){
			var id = $(this).attr("id");
			common.clearValidations(["#"+id]);
			if($(this).find("option:selected").attr("data-tablename") == "Select Table"){
				var id = $(this).attr("id");				 
				common.showcustommsg("#"+id, globalMessage['anvizent.package.label.pleaseSelectTableToJoin']);
				returnVal = false;
			}			 
		});
		$(".selectJoins").each(function(){
			var id = $(this).attr("id");
			common.clearValidations(["#"+id]);
			if($(this).find("option:selected").val() == "Select Join"){
				var id = $(this).attr("id");				 
				common.showcustommsg("#"+id, globalMessage['anvizent.package.label.pleaseSelectJoinType']);
				returnVal = false;
			}
		});
		return returnVal;
	},
	updateFilesHavingSameColumns : function(userID,packageId,isEveryFileHavingSameColumnNames){
		var url_updateFilesHavingSameColumns = "/app/user/"+userID+"/package/updateFileHavingSameColumns/"+packageId+"/"+isEveryFileHavingSameColumnNames;
		var myAjax = common.postAjaxCall(url_updateFilesHavingSameColumns,'POST','',headers);
		   myAjax.done(function(result) {
	    	  if(result != null &&  result.object == 1) {
	    		  if(result.hasMessages){
	    			  if(result.messages[0].code == 'ERROR'){
	    				  
	    				  common.displayMessages(result.messages[0].text)
	    				  return false;
	    			  }
	    		  }
	    	  }
		   });
		},
		updateIsFromDerivedTables : function(userID,packageId,isFromDerivedTables){

			var url_updateIsFromDerivedTables = "/app/user/"+userID+"/package/updateIsFromDerivedTables/"+packageId+"/"+isFromDerivedTables;
			var myAjax = common.postAjaxCall(url_updateIsFromDerivedTables,'POST','',headers);
			   myAjax.done(function(result) {
		    	  if(result != null &&  result.object == 1) {
		    		      
		    			  if(result.hasMessages){
			    			  if(result.messages[0].code == 'ERROR'){
			    				  common.displayMessages(result.messages[0].text)
			    				  return false;
			    			  }
			    		  }
		    	  }
			   });
			
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
		    				easyquerybuilder.getDataTypes(result);
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
		
		
		
		buildTargetTableRow : function(column) {
			var row = [];
			
			row.push('<tr class="m-row">', '<td><span class="m-column">', column, '</span></td>');
			row.push('<td>', '<select class="form-control input-sm m-datatype" >' + dataTypeSelect + '</select>', '</td>'); 
			//row.push()
			/*row.push('<option value="VARCHAR">'+globalMessage['anvizent.package.label.varchar']+'</option>');
			row.push('<option value="INT">'+globalMessage['anvizent.package.label.int']+'</option>');
			row.push('<option value="BIGINT">'+globalMessage['anvizent.package.label.bigInt']+'</option>');
			row.push('<option value="DECIMAL">'+globalMessage['anvizent.package.label.decimal']+'</option>');
			row.push('<option value="BIT">'+globalMessage['anvizent.package.label.bit']+'</option>');
			row.push('<option value="DATETIME">'+globalMessage['anvizent.package.label.dateTime']+'</option>');
			row.push('<option value="DATE">'+globalMessage['anvizent.package.label.date']+'</option>');*/
			//row.push('</select>');
			row.push('<td  nowrap>', '<input type="text" class="input-sm m-colsize" value="100"  style="width:50%;"/>', 
					'<input type="text" class="input-sm m-decimal" value="2" style="width:50%;display:none;"/>', '</td>');
			row.push('<td>', '<input type="checkbox" class="m-primary">', '</td>');
			row.push('<td>', '<input type="checkbox" class="m-notnull">', '</td>');
			row.push('<td>', '<input type="checkbox" class="m-unique">', '</td>');
			row.push('<td>', '<input type="checkbox" class="m-auto">', '</td>');
			row.push('<td>', '<input type="text" style="display:none" class="form-control input-sm m-default"/>', '</td>'); 
			row.push('</tr>');
			
			return row.join('');
		},
		validateTargetTableFields : function(){		
			var returnVal = true;
			var message = globalMessage['anvizent.package.message.invalidData'];
			common.clearcustomsg(".m-default");
			$("#customTargetTable tbody tr td").find(".m-colsize").each(function(){		
				if($(this).is(":visible")){
					var thisColValLength = $(this).val();
					var thisColDefaultElement = $(this).parents("tr").find(".m-default");
				    var thisColDefaultVal = $(thisColDefaultElement).val().trim();
					var thisColDefaultValLength = thisColDefaultVal.length;
					var thisColDataType = $(this).parents("tr").find(".m-datatype option:selected").val();
					var thisColDecimalValElement = $(this).parents("tr").find(".m-decimal");
					var thisColDecimalVal = $(this).parents("tr").find(".m-decimal").val().trim();
					
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
				}
			});
			return returnVal;
		},
}

if($('.easyquerybuilder-page').length){
	easyquerybuilder.initialPage();
	 
	$(function() {
	var schemaName = $("#existingSchemaList").val();
		var schemas = '';
		$.each(schemaName, function(i, val) {
			if(schemaName.length == 1){
				schemas += val.trim() ;
			} 
		});
		if(schemaName != null && schemas != ''){
			 easyquerybuilder.getSchemaRelatedTablesAndColumns("load",schemas);
		}
		 
	});
	$(function() {	

		$('select#existingSchemaList').multipleSelect({
							filter : true,
							placeholder : globalMessage['anvizent.package.label.selectSchemas'],
						    enableCaseInsensitiveFiltering: true,
						    allSelected : globalMessage['anvizent.package.label.selectedAll'],
						    selectAllText : globalMessage['anvizent.package.label.selectALLOpt'],
						    countSelected: '# of % selected',
						});
		 
		if($("#isDDlayout").val() == 'true' || $("isApisData").val() == 'true'){
			var schemaName = $("#dbSchemaName").val().trim();
			$("#existingSchemaList").multipleSelect("setSelects", [schemaName]);
			$('#existingSchemaListGo').trigger('click');
		 }
	    });
	 
	// on change of schema
	$(document).on( 'click', '#existingSchemaListGo', function() {
				var  existingSchemaList = $("#existingSchemaList").multipleSelect("getSelects", "text");
				var schemas = '';
				$.each(existingSchemaList, function(i, val) {
					if(existingSchemaList.length == 1){
						schemas +=   val.trim() ;
					}else{
						schemas +=   val.trim() + ",";
					}
					
				});
				var pos = '';
				var schemaList = '';
				 if(existingSchemaList.length == 1){
					  schemaList = schemas;
				 }else{
					  pos = schemas.lastIndexOf(",");
					  schemaList = schemas.substring(0, pos); 
				 }
				  if(schemaList == '' || schemaList == null ){
					  common.showErrorAlert(globalMessage['anvizent.package.message.pleaseSelectAtleastOneSchema']);
					  return false;
				  }
				easyquerybuilder.getSchemaRelatedTablesAndColumns("call",schemaList);	
	});
	$(document).on("click", "#uncheckExistingSchemaLists", function(e) {
		var  existingSchemaList = $("#existingSchemaList").multipleSelect("getSelects", "text");
		  if(existingSchemaList == '' || existingSchemaList == null ){
			  common.showErrorAlert(globalMessage['anvizent.package.label.noSchemaSelected']);
			  return false;
		  }else{
		      $("#resetSchemas").modal('show');
		  }
     });

// resetSchemas
$("#confirmResetSchemas").on('click', function(){
		    $("#existingSchemaList").multipleSelect("uncheckAll");
			$(".addedColumnsDiv,.joinConditionsDiv,.whereConditionsDiv, .joinDiv,.orderByConditionsDiv,.queryValidatemessageDiv,.calculatedColumnDiv").empty();
			$(".allTableListDiv,.addColumns,.clearSelections,.joinsBlock,.tableWithColumns").hide();
			$("#checkTablePreview,#saveILConnectionMapping,#disableQuery").hide();	
			$(".queryholder,#checkQuerySyntax,#enableQuery").hide();
			$("#queryScript").val("");
			$("#resetSchemas").modal('hide');
});
	//hide/show the columns under table   
	$(document).on("click",".tableList-child-node",function(){
		var schema = $("#existingSchemaList option:selected").val();		
		var connectionId = $('#existingConnections').val();		
		var container = $(this);
		container.find("i").toggleClass('fa fa-plus').toggleClass('fa fa-minus');		 
		var columnListChildNode = container.parents(".tableList-child").find(".columnList-child-node");		 
		columnListChildNode.toggle();		 		 
	 }).on("click","input[name='checkAllColumns']",function(e){
		 e.stopPropagation();
	 }); 	
	
	//select/unselect all columns, on checking the table checkbox
 	$(document).on("click","input[name='checkAllColumns']",function(){
 		var isChecked = $(this).is(":checked"); 		 
 		if(isChecked){
 			$(this).parents(".tableList-child").find(".columnList-child-node").children(".column-attr").each(function(){
 				$(this).find("input[name='columnName']").prop("checked",true);
 			});
 		} 
 		else{
 			$(this).parents(".tableList-child").find(".columnList-child-node").children(".column-attr").each(function(){
 				$(this).find("input[name='columnName']").prop("checked",false);
 			});
 		}
 	});
 	
 	// select/unselect table, on selection of its columns(validation)
 	$(document).on("click","input[name='columnName']",function(){
 		var columnName = $(this).val();
 		var label = $(this).parents(".tableList-child").find(".tableList-child-node").children("label.table-attr-label");	 		
 		label.find("input[name='checkAllColumns']").prop("checked",true);
 		var columnAttr=$(this).parents(".columnList-child-node").find(".column-attr");
 	    var checkLength =  columnAttr.find("input[name='columnName']:checked").length;
 	    if(checkLength == 0){
 	    	label.find("input[name='checkAllColumns']").prop("checked",false);
 	    }		
 	});
 	
 	// Add Columns click below table and column selection
 	$(document).on("click",".addColumns",function(){
 		$(".joinConditionsDiv,.whereConditionsDiv, .joinDiv,.orderByConditionsDiv,.calculatedColumnDiv").empty();
 		$('#queryScript').attr("readonly","readonly");
		$("#enableQuery").show();
		$("#disableQuery").hide();
 		var selectedObject = [], nonSelectedObject = [];
 		var selectedTable = null; 		
 		
 		$(".tableList-child").each(function(){
 			var selectedColumns = [], columnsDataType=[], allColumns=[], allColumnsDataTypes=[],columnsDataTypeSize=[];
 			var tableLabel = $(this).children(".tableList-child-node").find(".table-attr-label"); 
 			var isTableChecked = tableLabel.find("input[name='checkAllColumns']").is(":checked");
 			if(isTableChecked){
 				selectedTable = tableLabel.find("input[name='checkAllColumns']").val();
 				tableAliasName = tableLabel.find("input[name='tableAliasName']").val();
 				var columnAttr = $(this).children(".columnList-child-node").find(".column-attr");
 				columnAttr.each(function(){
 					var isColumnChecked = $(this).find("input[name='columnName']").is(":checked");
 					allColumns.push($(this).find("input[name='columnName']").val());
 					allColumnsDataTypes.push($(this).find("input[name='columnDataType']").val());
 					if(isColumnChecked){
 						selectedColumns.push($(this).find("input[name='columnName']").val());
 						columnsDataType.push($(this).find("input[name='columnDataType']").val())
 						columnsDataTypeSize.push($(this).find("input[name='columnDataTypeSize']").val())
 					} 					
 				});
 				 
 				selectedObject.push({"tableName":selectedTable,"columnNames":selectedColumns,"columnDataType":columnsDataType,"columnsDataTypeSize":columnsDataTypeSize,"allColumns":allColumns,"tableAliasName":tableAliasName});
 				nonSelectedObject.push({"tableName":selectedTable,"columnNames":allColumns,"columnDataType":allColumnsDataTypes,"tableAliasName":tableAliasName});
 			} 			
 		});
 		if(selectedTable === null || selectedTable === ''){
 			common.showErrorAlert(globalMessage['anvizent.package.label.pleaseChooseTables']);
 		}
 		easyquerybuilder.addColumns(selectedObject);
 		if(nonSelectedObject.length >= 1){
 			easyquerybuilder.whereCondition(nonSelectedObject);
 			easyquerybuilder.customColumn(selectedObject);
 			easyquerybuilder.orderByCondition(nonSelectedObject);
 		} 		
 		if(selectedObject.length > 1){ 			
 			easyquerybuilder.joinCondition(selectedObject);
 			$(".joinsBlock").show();
 		}
 		else{ 			
 			$(".joinsBlock").hide();
 		}
 		var object = easyquerybuilder.collectAllSelections();
 		if(!$.isEmptyObject(object[0])){ 			 
 			$(".queryholder,#checkQuerySyntax,#enableQuery").show();
 			$(".addedColumnsDivText").hide();
 			$("#saveILConnectionMapping,#checkTablePreview").hide();
 			$(".queryValidatemessageDiv").empty();
 			var query = easyquerybuilder.buildQuery(object);
 	 		$("#queryScript").val(query.join(''));
 		}
 		else{
 			$(".queryholder,#checkQuerySyntax,#saveILConnectionMapping,#checkTablePreview,#enableQuery,#disableQuery").hide();
 			$(".queryValidatemessageDiv").empty();
 			$(".addedColumnsDivText").show();
 		}
 		
 	 	 $(".addedColumns tbody").sortable({
 	 		 
 	 	    update: function(event, ui) {
 	 	    	var object = easyquerybuilder.collectAllSelections(); 				
 		 		var query = easyquerybuilder.buildQuery(object);
 	 	 		$("#queryScript").val(query.join(''));
 	 	    }
 	 	    
 	 	});
 		 
		$(".addedColumns tbody").disableSelection();
		$('.addedColumns tbody tr').css( 'cursor', 'pointer' ); 
 		
 		
 	}); 

 	//delete the selected column //
 	$(document).on("click",".deleteColumn",function(){ 		
 		var deleteColumnName = $(this).parents("tr").find("input[name='originalColumnName']").val(); 		 
 		var allOtherTrs = $(this).parents("tr").siblings();
 		 
 		$(this).parents("tr").remove();
 		
 		if($(".deleteColumn").length>0){ 			 
 			var object = easyquerybuilder.collectAllSelections(); 				
	 		var query = easyquerybuilder.buildQuery(object);
 	 		$("#queryScript").val(query.join(''));
 		}
 		else if($(".deleteColumn").length == 0){ 			
 			$(".addedColumnsDiv,.joinConditionsDiv,.whereConditionsDiv, .joinDiv,.orderByConditionsDiv,.calculatedColumnDiv").empty();
 			$(".addedColumnsDivText").show();
 			$("#queryScript").val("");
 			$(".joinsBlock").hide();
 		} 		
 		
 		$(".addedColumns").each(function(){
 			if($(this).find(".deleteColumn").length == 0){
 				$(this).parents(".columns-block").remove(); 				
 	 			var object = easyquerybuilder.collectAllSelections(); 				
 		 		var query = easyquerybuilder.buildQuery(object);
 	 	 		$("#queryScript").val(query.join(''));
 			}
 		});  
 		 
 		if($(".addedColumns").length == 1){
 			$(".joinDiv,.joinConditionsDiv").empty(); 			 
 			$(".joinsBlock").hide();
 			var object = easyquerybuilder.collectAllSelections(); 				
	 		var query = easyquerybuilder.buildQuery(object);
 	 		$("#queryScript").val(query.join(''));
 		}
 	  		 
 		var deleteColumnTableName = $(this).attr("table-name"); 
 		
 		$(".allTableListDiv").find("input[name='checkAllColumns']:checked").each(function(){ 
 			if(deleteColumnTableName === $(this).val()){
 				var totalColumns = $(this).parents(".tableList-child").find("input[name='columnName']").length;
				$(this).parents(".tableList-child").find("input[name='columnName']:checked").each(function(){
					var duplicateCols = allOtherTrs.find("input[name='originalColumnName'][value='"+deleteColumnName+"']").length;
					if(deleteColumnName == $(this).val() && duplicateCols == 0){
						$(this).attr("checked",false);						 			
					}						
				}); 				
				var uncheckedColumn = $(this).parents(".tableList-child").find("input[name='columnName']:not(:checked)").length; 				
 				if(totalColumns == uncheckedColumn){ 					 
 					$(this).attr("checked",false);
 				}	
 			} 				 
 		});
 		
 	});
 	
 	$(document).on("click",".deleteRow",function(){ 		
 		$(this).parents("tr").remove(); 		 			 
		var object = easyquerybuilder.collectAllSelections(); 				
 		var query = easyquerybuilder.buildQuery(object);
 		$("#queryScript").val(query.join(''));
 	});
 	 	
 	//on change of table names in where conditions
 	$(document).on("change",".selectTable",function(){ 		 
 		var selectedTable = $(this).find("option:selected").attr("data-tableName");
 		var schema = $("#existingSchemaList option:selected").val();
		var userID = $("#userID").val();
		var connectionId = $('#connectionId').val();
		var container = $(this).parents("tr").find("select.selectColumn");
		var isDDlayout = $("#isDDlayout").val();
		var isApisData = $("#isApisData").val();
		container.empty();
		var selectedData = {
				connectionId : 	connectionId,
				database : {
					schema : schema,
					table : {
						tableName : selectedTable
					}
				},
				ddLayout:isDDlayout,
				apisData : isApisData
		}
		showAjaxLoader(true);
		var url_getTableRelatedColumns = "/app/user/"+userID+"/package/getTableRelatedColumns";
		if(connectionId != null && connectionId != '' && selectedTable != 'Select Table'){
		   var myAjax = common.loadAjaxCall(url_getTableRelatedColumns,'POST',selectedData,headers);
		   myAjax.done(function(result) {	
				showAjaxLoader(false);
		    	  if(result != null && result.hasMessages) {
		    		  if(result.messages[0].code == "ERROR"){		    			  
		    			   
		    		  }
		    		  else if(result.messages[0].code == "SUCCESS"){
		    			  easyquerybuilder.addSelectedTableColumns(result.object,container);		    			    			  
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
		else{
			showAjaxLoader(false);
			container.append("<option value='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option>");
			$(this).parents("tr").find("select.selectClauses").empty().append("<option value='Select Clause'>"+globalMessage['anvizent.package.label.selectClause']+"</option>");
			$(this).parents("tr").find("input[name='inputValue']").val("").attr("disabled",true);
		}
 	});
 	
 	// add few more where conditions
 	$(document).on("click",".addOneMoreColumn",function(){ 		
 		var tableSelect = $(this).parents("tbody").find(".firstSelectTable").clone();
 		tableSelect.removeClass("firstSelectTable"); 		
 	 
 		var columnSelect = "<select class='selectColumn j-tablename table-select-input-box form-control'><option value='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option></select>";
 		var clauses =   "<select class='selectClauses j-tablename form-control'><option value='Select Clause'>"+globalMessage['anvizent.package.label.clause']+"</option></select>";
		var inputText = "<input type='text' disabled='disabled' placeholder='"+globalMessage['anvizent.package.label.inputValue']+"' name='inputValue' class='j-tablename table-select-input-box form-control'>";
		var row = "<tr>" +
					"<td class='selectTabletd col-md-2 col-lg-2'></td>" +
					"<td class='col-md-2 col-lg-2'>"+columnSelect+"</td>" +
					"<td class='col-md-2 col-lg-2'>"+clauses+"</td>" +
					"<td class='col-md-3 col-lg-3'>"+inputText+"</td>" +				
					"<td class='addOneMoreColumntd col-md-3 col-lg-3'><button  class='btn btn-primary btn-sm addOneMoreColumn'><i class='fa fa-plus' aria-hidden='true'></i></button>" +
					"&nbsp;<button  class='btn btn-primary btn-sm deleteRow' title='"+globalMessage['anvizent.package.label.delete']+"'><span class='glyphicon glyphicon-trash'  style='color:#fff;'></span></button></td>" +
				 "</tr>"; 
 		 
 		var tbody = $(this).parents("tbody"); 		
 		var newRow = row; 		
 		tbody.append(newRow);
 		tbody.find(".selectTabletd").append(tableSelect);
 		tbody.find("td.selectTabletd").removeClass("selectTabletd");
 	}); 
 	
 	// add few more custom columns 
 	$(document).on("click",".addOneMoreCustomColumn",function(){ 	
 		var tbody = $(this).parents("tbody");
 		var tbodyFirst = $(this).parents("tbody tr:first").clone();
 		tbodyFirst.find(".j-customColumnFormula").prop('disabled',true);
 		tbodyFirst.find(".j-customColumnName,.j-customColumnFormula").val("");
 		tbodyFirst.find(".deleteRow").show();
 		tbody.append(tbodyFirst);
 		customColumnFormula = ''; 
 	}); 
 	
 	//on change of Column names in where conditions
 	$(document).on("change",".selectColumn",function(){ 		 
 		var dataType = $(this).val().toUpperCase();
 		var selectedColumnName = $(this).text();
 		container = $(this).parents("tr").find(".selectClauses");
 		container.empty(); 
		var clausesForIntegers = ["<","<=",">",">=","=","<>"];
		var clausesForStringsOrBits = ["=","<>","LIKE","in","IS NULL","IS NOT NULL"];
		var clausesForDates = ["<","<=",">",">=","=","<>","IS NULL","IS NOT NULL"];
		var applicableClauses = [];
		if(dataType == "VARCHAR" || dataType == "BIT" || dataType == "CHAR" || dataType == "VARCHAR2" || dataType == "NVARCHAR" || 
				dataType == "TEXT" || dataType == "PICKLIST" || dataType == "REFERENCE" || dataType == "TEXTAREA" || dataType == "PHONE" || 
				dataType == "URL" || dataType == "LONGTEXTAREA" || dataType == "ID" || dataType == "NCHAR" || dataType == "NTEXT"  ||
			    dataType == "CHARACTER" ||   dataType == "VARBINARY" || dataType == "UNIQUEIDENTIFIER" || dataType == "BOOLEAN" || dataType == "CHARACTER VARYING" || dataType =="BOOL" ||
			    dataType == "CHARACTER VARYING" || dataType =="BOOL" || dataType == "LONGVARBINARY" ||  dataType == "LONGVARCHAR" ){
			applicableClauses = clausesForStringsOrBits;
		}
		else if(dataType == "BIGINT" || dataType == "DECIMAL" || dataType == "INT" || dataType == "FLOAT" || dataType == "NUMBER" ||
				dataType == "DOUBLE" || dataType == "NUMERIC" || dataType == "INTEGER" || dataType == "LONG" || dataType == "MEDIUMINT" || 
				dataType == "REAL" || dataType == "SMALLINT" || dataType == "TINYINT" || dataType =="MONEY" || dataType =="INT UNSIGNED"||
				dataType == "SMALLSERIAL" || dataType =="SERIAL" || dataType == "BIGSERIAL" ||   dataType == "VARBIT"  || dataType == "BIT VARYING"){
			applicableClauses = clausesForIntegers;
		}
		else if(dataType == "DATETIME" || dataType == "DATE" || dataType == "TIMESTAMP" || dataType == "TIME" || dataType == "YEAR" || 
				dataType == "DATETIME2" || dataType == "DATETIMEOFFSET" || dataType == "SMALLDATETIME" || 
				dataType == "TIMESTAMP WITHOUT TIME ZONE" || dataType == "TIMESTAMP WITH TIME ZONE" || dataType == "TIME WITHOUT TIME ZONE" ||  dataType =="TIME WITH TIME ZONE" ){
			applicableClauses = clausesForDates;
		}
		
 		var whereClauses = "<option value='Select Clause'>"+globalMessage['anvizent.package.label.selectClause']+"</option>";
		for(var k =0;k<applicableClauses.length;k++){						 
			whereClauses+="<option value='"+applicableClauses[k]+"'>"+applicableClauses[k]+"</option>";
		}		 
		container.append(whereClauses);
		$(this).parents("tr").find("input[name='inputValue']").val("").attr("disabled",true);
		
 	});
 	
 	$(document).on("change",".selectClauses",function(){
 		var selectedClause = $(this).val();
 		if(selectedClause != "Select Clause")
 			$(this).parents("tr").find("input[name='inputValue']").removeAttr("disabled"); 	
 		else
 			$(this).parents("tr").find("input[name='inputValue']").attr("disabled",true); 
 	}); 
 	
 	//on change of First td table names in join conditions
 	$(document).on("change",".selectJoinFirstTable",function(){ 		 
 		var selectedTable = $(this).find("option:selected").attr("data-tableName");
 	 
 		var schema = $("#existingSchemaList option:selected").val();
		var userID = $("#userID").val();
		var connectionId = $('#connectionId').val();
		var container = $(this).parents("tr").find("select.selectJoinColumnFirst");
		var isDDlayout = $("#isDDlayout").val();
		var isApisData = $("#isApisData").val();
		container.empty();
		var selectedData = {
				connectionId : 	connectionId,
				database : {
					schema : schema,
					table : {
						tableName : selectedTable
					}
				},
				ddLayout : isDDlayout,
				apisData : isApisData
		}
		showAjaxLoader(true);
		var url_getTableRelatedColumns = "/app/user/"+userID+"/package/getTableRelatedColumns";
		if(connectionId != null && connectionId != '' && selectedTable != 'Select Table'){
		   var myAjax = common.loadAjaxCall(url_getTableRelatedColumns,'POST',selectedData,headers);
		   myAjax.done(function(result) {
			   showAjaxLoader(false);
		    	  if(result !=null && result.hasMessages) {
		    		  if(result.messages[0].code == "ERROR"){		    			  
		    			   
		    		  }
		    		  else if(result.messages[0].code == "SUCCESS"){
		    			  easyquerybuilder.addSelectedTableJoinColumnsFirst(result.object,container);		    			    			  
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
		else{
			showAjaxLoader(false);
			container.append("<option value='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option>");
		}
 	});
 	 
 	//on change of Second td table names in join conditions
 	$(document).on("change",".selectJoinSecondTable",function(){ 		 
 		var selectedTable = $(this).find("option:selected").attr("data-tableName");
 		var schema = $("#existingSchemaList option:selected").val();
		var userID = $("#userID").val();
		var connectionId = $('#connectionId').val();
		var container = $(this).parents("tr").find("select.selectJoinColumnSecond");
		var isDDlayout = $("#isDDlayout").val();
		var isApisData = $("#isApisData").val();
		container.empty();
		var selectedData = {
				connectionId : 	connectionId,
				database : {
					schema : schema,
					table : {
						tableName : selectedTable
					}
				},
				ddLayout :isDDlayout,
				apisData : isApisData
		}
		showAjaxLoader(true);
		var url_getTableRelatedColumns = "/app/user/"+userID+"/package/getTableRelatedColumns";
		if(connectionId != null && connectionId != '' && selectedTable != 'Select Table'){
		   var myAjax = common.loadAjaxCall(url_getTableRelatedColumns,'POST',selectedData,headers);
		   myAjax.done(function(result) {	
			   showAjaxLoader(false);
		    	  if(result != null && result.hasMessages) {
		    		  if(result.messages[0].code == "ERROR"){		    			  
		    			   
		    		  }
		    		  else if(result.messages[0].code == "SUCCESS"){
		    			  easyquerybuilder.addSelectedTableJoinColumnsFirst(result.object,container);		    			    			  
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
		else{
			showAjaxLoader(false);
			container.append("<option value='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option>");
		}
 	}); 	 
 	
 // add few more join conditions
 	$(document).on("click",".addOneMoreJoinColumn",function(){ 		
 		var tableSelectFirst = $(this).parents("tbody").find(".firstJoinSelectTable").clone();
 		tableSelectFirst.removeClass("firstJoinSelectTable"); 		
 		var tableSelectSecond = $(this).parents("tbody").find(".secondJoinSelectTable").clone();
 		tableSelectSecond.removeClass("secondJoinSelectTable"); 		
 	 		 
		var columnSelectFirst = "<select class='selectJoinColumnFirst j-tablename table-select-input-box form-control'><option value='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option></select>";
		var columnSelectSecond = "<select class='selectJoinColumnSecond j-tablename table-select-input-box form-control'><option value='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option></select>";
				
		var row ="<tr>" +
				"<td class='selectTabletdFirst'></td>" +
				"<td>"+columnSelectFirst+"</td>" +				
				"<td class='selectTabletdSecond'></td>" +
				"<td>"+columnSelectSecond+"</td>" +
				"<td class='addOneMoreColumnJointd'><button  class='btn btn-primary btn-sm addOneMoreJoinColumn'><i class='fa fa-plus' aria-hidden='true'></i></button>" +
				"&nbsp;<button  class='btn btn-primary btn-sm deleteRow' title='"+globalMessage['anvizent.package.label.delete']+"'><span class='glyphicon glyphicon-trash'  style='color:#fff;'></span></button></td>"+
			"</tr>"; 
			
 		var tbody = $(this).parents("tbody"); 		
 		var newRow = row; 		
 		tbody.append(newRow);
 		tbody.find(".selectTabletdFirst").append(tableSelectFirst);
 		tbody.find("td.selectTabletdFirst").removeClass("selectTabletdFirst");
 		tbody.find(".selectTabletdSecond").append(tableSelectSecond);
 		tbody.find("td.selectTabletdSecond").removeClass("selectTabletdSecond");
 	});
 	
 	//on change of table names in Order By conditions
 	$(document).on("change",".oderBySelectTable",function(){ 		 
 		var selectedTable = $(this).find("option:selected").attr("data-tableName");
 		var schema = $("#existingSchemaList option:selected").val();
		var userID = $("#userID").val();
		var connectionId = $('#connectionId').val();
		var container = $(this).parents("tr").find("select.selectOrderByColumn");
		var isDDlayout = $("#isDDlayout").val();
		var isApisData = $("#isApisData").val();
		container.empty();
		var selectedData = {
				connectionId : 	connectionId,
				database : {
					schema : schema,
					table : {
						tableName : selectedTable
					}
				},
				ddLayout : isDDlayout,
				apisData : isApisData
		}
		showAjaxLoader(true);		 
			var url_getTableRelatedColumns = "/app/user/"+userID+"/package/getTableRelatedColumns";			 
			if(connectionId != null && connectionId != '' && selectedTable != 'Select Table'){
			   var myAjax = common.loadAjaxCall(url_getTableRelatedColumns,'POST',selectedData,headers);
			   myAjax.done(function(result) {		
				   showAjaxLoader(false);
			    	  if(result != null && result.hasMessages) {
			    		  if(result.messages[0].code == "ERROR"){		    			  
			    			   
			    		  }
			    		  else if(result.messages[0].code == "SUCCESS"){
			    			  easyquerybuilder.addSelectedTableColumns(result.object,container);		    			  	
			    			  	var object = easyquerybuilder.collectAllSelections(); 				
			    		 		var query = easyquerybuilder.buildQuery(object);
			    		 		$("#queryScript").val(query.join(''));
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
			else{
				showAjaxLoader(false);
				container.append("<option value='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option>");
				$(this).parents("tr").find("select.orderBy").val("Desc");
				var object = easyquerybuilder.collectAllSelections(); 				
		 		var query = easyquerybuilder.buildQuery(object);
		 		$("#queryScript").val(query.join(''));
			}
		 
 	});
 	
 	// add few more order by conditions
 	$(document).on("click",".addOneMoreOrderByColumn",function(){ 		
 		var tableSelect = $(this).parents("tbody").find(".firstOderBySelectTable").clone();
 		tableSelect.removeClass("firstOderBySelectTable"); 		
 	 
 		var columnSelect = "<select class='selectOrderByColumn j-tablename table-select-input-box form-control'><option value='Select Column' data-columnName='Select Column'>"+globalMessage['anvizent.package.label.selectColumn']+"</option></select>";
		var orderBy =   "<select class='orderBy j-tablename form-control'><option value='Desc'>Desc</option><option value='Asc'>Asc</option></select>";
		
		var row="<tr>" +
				"<td class='selectTabletd col-md-3 col-lg-3 col-sm-3'></td>" +
				"<td class='col-md-3 col-lg-3 col-sm-3'>"+columnSelect+"</td>" +
				"<td class='col-md-3 col-lg-3 col-sm-3'>"+orderBy+"</td>" +					
				"<td class='addOneMoreOrderByColumntd col-md-3 col-lg-3 col-sm-3'><button  class='btn btn-primary btn-sm addOneMoreOrderByColumn'><i class='fa fa-plus' aria-hidden='true'></i></button>" +
				"&nbsp;<button  class='btn btn-primary btn-sm deleteRow' title='"+globalMessage['anvizent.package.label.delete']+"'><span class='glyphicon glyphicon-trash'  style='color:#fff;'></span></button></td>" +
			"</tr>";
 		 
 		var tbody = $(this).parents("tbody"); 		
 		var newRow = row; 		
 		tbody.append(newRow);
 		tbody.find(".selectTabletd").append(tableSelect);
 		tbody.find("td.selectTabletd").removeClass("selectTabletd");
 	});
 	
 // Remove table selections 
 	$(document).on("click",".clearSelections,.close-popup",function(){ 		
 		$("input[name='checkAllColumns']").each(function(){
 			$(this).prop("checked",false);
 		});
 		$("input[name='columnName']").each(function(){
 			$(this).prop("checked",false);
 		});
 		$(".addedColumnsDivText").show();
 		$("#il_incremental_update").attr('checked',false);	
		$(".joinsBlock,.queryholder,#checkQuerySyntax,#checkTablePreview,#saveILConnectionMapping,#enableQuery,#disableQuery").hide();
		$(".addedColumnsDiv,.joinConditionsDiv,.whereConditionsDiv,.joinDiv,.orderByConditionsDiv,.queryValidatemessageDiv,.calculatedColumnDiv").empty();
		$("#queryScript").val("");
 	});
 	
 // change column name based on aggregation
 	$(document).on("click",".aggregation",function(){
 		var selectedAggregation = $(this).text(); 		
 		var columnNameWithoutAggregation = $(this).parents("tr").find("input[name='originalColumnName']").val();
 		var columnNameWithAggregation = "";
 		if(selectedAggregation != "Default"){
 			columnNameWithAggregation = selectedAggregation+"("+columnNameWithoutAggregation+")";
 		}
 		else{
 			columnNameWithAggregation = columnNameWithoutAggregation;
 		}
 		
 		$(this).parents("tr").find("input[name='aggregatedColumnName']").val(columnNameWithAggregation); 
 		$(this).parents("tr").find("input[name='aggregation']").val(selectedAggregation); 
 		
 		var object = easyquerybuilder.collectAllSelections(); 				
 		var query = easyquerybuilder.buildQuery(object);
 		$("#queryScript").val(query.join(''));
 	});
 	
 	$(document).on("change",".selectOrderByColumn,.orderBy,.selectTable,.selectColumn",function(){
 		var object = easyquerybuilder.collectAllSelections(); 				
 		var query = easyquerybuilder.buildQuery(object);
 		$("#queryScript").val(query.join(''));
 	});
 	
 	$(document).on("change",".selectJoinFirstTable,.selectJoinSecondTable,.selectJoinColumnFirst,.selectJoinColumnSecond,.selectJoins",function(){
 		var status = easyquerybuilder.validateJoinSelection();
 		if(status){
 			var object = easyquerybuilder.collectAllSelections(); 				
 			var query = easyquerybuilder.buildQuery(object);
 			$("#queryScript").val(query.join(''));
 		}
 	});
 	
 	$(document).on('keyup',"input[name='inputValue'], input[name='columnAliasName'],input[name='customColumnName'],textarea[name='customColumnFormulaInputValue']", function(){
 		var object = easyquerybuilder.collectAllSelections(); 				
 		var query = easyquerybuilder.buildQuery(object);
 		$("#queryScript").val(query.join(''));
    });
 	 
 	$(document).on('click',".availableColumnsForCustomColumn", function(){
 		 var  customColumnName = $(this).parents("tr").find(".j-customColumnName");
 		 var  customColumnFormula = $(this).parents("tr").find(".j-customColumnFormula").val();
 		 var  customColumnNameVal = customColumnName.val().trim();
 		 common.clearValidations([customColumnName]);
 		 var regex = /^[0-9a-zA-Z\_]+$/;
 		 if(customColumnNameVal.length != 0 && regex.test(customColumnNameVal)){
 			$(this).parents("tr").find(".j-customColumnFormula").val(customColumnFormula + $(this).text());
 	 		$(this).parents("tr").find('textarea[name="customColumnFormulaInputValue"]').trigger('keyup');
 	 		$(this).parents("tr").find('input[name="customColumnName"]').trigger('keyup');
 		 }else{
 			common.showcustommsg(customColumnName,"custom column name required and spaces are not allowed.",customColumnName); 			 
 		 }
    });
 	
 	$(document).on('click',"input[name='customColumnName']", function(){
 		$(this).parents("tr").find(".j-customColumnFormula").prop('disabled',false);
 		 common.clearValidations([$(this)]);
    });
 	$(document).on("change",".selectClauses",function(){
 		var clause = $(this).val(); 		 
 		if(clause == "Select Clause" || clause == "IS NULL" || clause == "IS NOT NULL") 			
 			$(this).parents("tr").find("input[name='inputValue']").val("").attr("disabled",true);
 		
 		var object = easyquerybuilder.collectAllSelections(); 				
 		var query = easyquerybuilder.buildQuery(object);
 		$("#queryScript").val(query.join(''));
 	}); 
 	var id=0;
	$(document).on("click",".addDuplicateRow",function(index){
		 var databaseTypeId = $("#connector_id").val();
		 var protocal = $("#dbProtocal").val();
		 var thisRow = $( this ).closest('tr')[0];
		 var aliasName = $( this ).closest('tr').find('input[name=columnAliasName]').val();
		 
		 var index = '';
			//mysql-1, oracle-4 , db2-5 ,Salesforce-6 , DB2AS400-7, MS Access - 3 ,postgresql-8
			if(protocal.indexOf('mysql') != -1  || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1 || protocal.indexOf('sforce') != -1  || protocal.indexOf('as400') != -1  || protocal.indexOf('ucanaccess') != -1  ||  protocal.indexOf('postgresql') != -1 || protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1){
				index = aliasName+"_"+ id++ ;
			}
			//sqlserver-2 ||  odbc - 10
			else if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
				if(aliasName[aliasName.length-1] === ']')
					index =  aliasName.replace(']', '_'+ id++ +']');
					else
				    index = aliasName+"_"+ id++ ;
			}
			
		  
		 $(thisRow).clone().insertAfter(thisRow).find('input[name=columnAliasName]').val(index);
		 var object = easyquerybuilder.collectAllSelections(); 				
 		 var query = easyquerybuilder.buildQuery(object);
 		 $("#queryScript").val(query.join(''));
 		 
	});
  
 	
 	$(document).on("click",".move-up,.move-down,.move-top,.move-bottom",function(){
    	var table = $(this).parents('table');	
        var thisRow = $(this).parents("tr");
        if ($(this).is(".move-up")) {						 
            thisRow.insertBefore(thisRow.prev());
        } 
		else if ($(this).is(".move-down")) {			
            thisRow.insertAfter(thisRow.next());
        }
		else if ($(this).is(".move-top")) {				
            thisRow.insertBefore(table.find("tbody tr:first"));
        }
		else {           		
           thisRow.insertAfter(table.find("tbody tr:last"));		  
        }
		var object = easyquerybuilder.collectAllSelections(); 				
 		var query = easyquerybuilder.buildQuery(object);
 		$("#queryScript").val(query.join(''));
    });
 	
 	
 	
 	$("#checkQuerySyntax").click(function(){
		$(".queryValidatemessageDiv").empty();
		$("#saveILConnectionMapping").hide();
		$("#checkTablePreview").hide();
		var userID = $("#userID").val(); 
		var connectionId = $("#connectionId").val();
		var isDDlayout = $("#isDDlayout").val();
		var typeOfCommand = "Query";		
		var query = $("#queryScript").val();
		var isApisData = $("#isApisData").val(); 
		if(query.indexOf('Order by') != -1)
			query = query.substring(0, query.indexOf('Order by'));
		 
		common.clearValidations(["#queryScript"]);
		
		if( query != '') {			
			var selectData ={
					iLConnection : {
						connectionId : connectionId,
						ddLayout : isDDlayout,
						apisData : isApisData
					},
					iLquery : query,
					typeOfCommand : typeOfCommand
			} 
			
			showAjaxLoader(true);
			var url_checkQuerySyntax = "/app/user/"+userID+"/package/checksQuerySyntax";
			 var myAjax = common.postAjaxCall(url_checkQuerySyntax,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);	
			    	  if(result != null && result.hasMessages){
			    		  var message = '';	
			    		  if(result.messages[0].code == "SUCCESS") {
			    		  $(".queryValidatemessageDiv").show();
			    			  message += '<div class="alert alert-success alert-dismissible" role="alert">'+
	    		  							''+result.messages[0].text+' <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+
	    		  							'</div>';
			    			  
			    			  $(".queryValidatemessageDiv").append(message);
			    			  $("#saveILConnectionMapping").show();
			    			  $("#checkTablePreview").show();			    			 
			    			  
			    		  }else{
			    			  message += '<div class="alert alert-danger alert-dismissible" role="alert">'+
	  							''+result.messages[0].text+''+
	  							'</div>';
			    			  	$(".queryValidatemessageDiv").append(message);
			    			  	setTimeout(function() { $(".alert-danger").hide() .empty(); }, 10000);
			    			  	//setTimeout(function() { $(".queryValidatemessageDiv").hide().empty(); }, 10000);
			    		  }
			    		  
			    	  }else{
							var messages = [ {
								code : globalMessage['anvizent.message.error.code'],
								text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
							} ];
				    		common.displayMessages(messages);
						}
			    });
			
		} else {			 
			common.showcustommsg("#queryScript", globalMessage['anvizent.package.label.queryShouldNotBeEmpty']);
		}
	});

	// validate query table Preview
	$(document).on('click', '#checkTablePreview', function() {
		$(".queryValidatemessageDiv").empty();
		$("#saveILConnectionMapping").show();
		var userID = $("#userID").val(); 
		var connectionId = $("#connectionId").val();
		var typeOfCommand = "Query"; 
		var query = $("#queryScript").val();
		var packageId = $("#packageId").val();
		var il_incremental_update = $("#il_incremental_update").is(":checked");
		var ilId = $("#ilId").val();
		var databaseTypeId = $("#connector_id").val();
		var protocal = $("#dbProtocal").val();
		var isDDlayout = $("#isDDlayout").val();
		var isApisData = $("#isApisData").val();
		if(query.indexOf('Order by') != -1 && (protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1))
			query = query.substring(0, query.indexOf('Order by'));
		
		common.clearValidations(["#queryScript"]);
		if( query != '') {
			var selectData ={
					packageId : packageId,
					iLId : ilId,
					iLConnection : {
						connectionId : connectionId,
						ddLayout : isDDlayout,
						apisData : isApisData
					},
					iLquery : query,
					typeOfCommand : typeOfCommand,
					il_incremental_update : il_incremental_update
			}			
			showAjaxLoader(true);
			var url_checkQuerySyntax = "/app/user/"+userID+"/package/getTablePreview";
          /* if(packageId == 0){
        	   url_checkQuerySyntax = "/app/user/"+userID+"/package/getXrefTablePreview";
			}*/
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
			    		  var list = result.object;
			    		  if(list != null && list.length > 0){
			    			  var tablePreview='';
			    			  var colCount = 0;
			    			  $.each(list, function (index, row) {				    		  
			    				  tablePreview+='<tr>';
			    				  $.each(row, function (index1, column) {			    					   
			    					  tablePreview += (index == 0 ? '<th>'+column+'</th>' : '<td>'+column+'</td>');
			    					  colCount = colCount + 1;
			    				  });
			    				  tablePreview+='</tr>'; 
			    			  });
			    			  $(".tablePreview").empty();
			    			  if(list.length<2){
					    		  tablePreview+='<tr>';
					    		  tablePreview+='<td colspan = "'+colCount+'" >';
					    		  tablePreview+=globalMessage['anvizent.package.label.noRecordsAvailable'];
					    		  tablePreview+='</td>'; 
					    		  tablePreview+='</tr>'; 
					    	  }
			    			  $(".tablePreview").append(tablePreview);
				    	  }
				    	  else{
				    		  $(".tablePreview").empty();
				    		  $(".tablePreview").append(globalMessage['anvizent.package.label.noRecordsAvailableInTable']);
				    	  } 
			    	  }			    	  
			    });
			
		} else {
			common.showcustommsg("#queryScript",globalMessage['anvizent.package.label.queryShouldNotBeEmpty']);
		}
	});
	var tables = [];
	var ddlTables ="";
	easyquerybuilder.getDataTypeList();
	$(document).on('click', '#saveILConnectionMapping', function(){
		$('.alert-success').empty();
		$('.alert-success').css('display','none'); 
		var iLId = $("#ilId").val();
	    var dLId = $("#dlId").val();
	    $('#messagePopUpForCustomPackage .back').show();
	    $("#messagePopUpForCustomPackage .closeButton").show();
	    var isDDlayout = $("#isDDlayout").val();
	    var isApisData = $("#isApisData").val();
	  //custom and derived package
	    var isWebservice = false;
		if((iLId == "" && dLId == "")){
			
			  if(isDDlayout != 'true'){
			   var isFlatFile = false;
			   var flatFileType = null;
			   var filePath = null;
			   var delimeter = null;
			   var isFirstRowHasColumnNames =null;
			   var connectionId = $("#connectionId").val();
			   var typeOfCommand = "Query";
			   var queryScript = $("#queryScript").val();
			   var targetTableId = null;		 
			   var packageId = $("#packageId").val();
			   var userID = $("#userID").val();	
			   var isHavingParentTable = false;	
			   var isEveryFileHavingSameColumnNames=false;
    		   var isFromDerivedTables= $("#isCustom").val() == "false" ? true : false;
    		   
			   if(queryScript ==''){
				   easyquerybuilder.showMessage(globalMessage['anvizent.package.label.pleaseFillRequiredFields']);
				   return false;
			   } 
				  
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
					   isHavingParentTable :isHavingParentTable ,
					   isWebservice : isWebservice
			   };
				   
			   showAjaxLoader(true);
			   var url_saveILConnectionMapping = "/app/user/"+userID+"/package/saveILsConnectionMapping";
			   var myAjax = common.postAjaxCall(url_saveILConnectionMapping,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    		  showAjaxLoader(false);
			    		  if(result != null && result.hasMessages){
			    			  if(result.messages[0].code == "SUCCESS") {
									 var  messages=[{
										  code : result.messages[0].code,
										  text : result.messages[0].text
									  }];		
									 if($("#isCustom").val() == "false"){
										 easyquerybuilder.updateFilesHavingSameColumns(userID,packageId,isEveryFileHavingSameColumnNames);
									 }
					    			 easyquerybuilder.updateIsFromDerivedTables(userID,packageId,isFromDerivedTables);
									 $("#popUpMessageForCustomPackage").empty();
									 $("#popUpMessageForCustomPackage").append("<div class='alert alert-success'>"+result.messages[0].text+"</div>");
									 $("#messagePopUpForCustomPackage .closeButton").hide();
									 $("#messagePopUpForCustomPackage").modal('show');    			  
				    			  }
			    			  else if(result.messages[0].code == "ERROR"){
			    				     $("#popUpMessageForCustomPackage").empty();
									 $("#popUpMessageForCustomPackage").append("<div class='alert alert-danger'>"+result.messages[0].text+"</div>");
									 $('#messagePopUpForCustomPackage .back').hide();
									 $("#messagePopUpForCustomPackage").modal('show');
			    			  }
			    		  }else{
			    			  easyquerybuilder.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
			    		  }
			    	  
			    });		
			   }else{
				   // ddl layout start here
				    var query = $("#queryScript").val();
					
					if (query.replace(/\s+/g, '') === '') {
						$("#queryScript").addClass("border-red");
					}

					var userID = $("#userID").val();
					var industryId = "0";
					var packageId = "0";
					var isstaging = false;
					$("input.tableName").each(function() {
						tables.push($(this).val());
						ddlTables += $(this).val().split(".").pop()+","
					});
					showAjaxLoader(true);
					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					headers[header] = token;
					var url = "/app/user/"+userID+"/package/validateCustomTempTablesQuery/"+industryId+"/"+packageId;
				    var myAjax = common.postAjaxCallObject(url,'POST', {"queryvalue" : query, "tables" : tables.join('::'), "isstaging": isstaging},headers);
				    
				    myAjax.done(function(result) {
				    	showAjaxLoader(false);
				    	if(result != null && result.hasMessages){
				    		if(result.messages[0].code == "SUCCESS"){
				    			var obj = result.object;
				    			var status = obj.isValid;
				    			aliasColumnNames = obj.columnNames;
						    	if (status) {
									var targetTable = $("table#customTargetTable").find('tbody');
									common.clearcustomsg("#targetTableName");
									targetTable.find('tr').remove();
									var objColumnsList = {};
									var sel = $(this);
									if (aliasColumnNames.length) {
										$.each(aliasColumnNames, function(i, val){
											var columnName = val.toLowerCase();
											var originalColumnName = val; 
											
											var aliasName = "";
											var aliasText = ""; 
											
											if (objColumnsList[columnName] ) {
												aliasName = originalColumnName +"_"+ objColumnsList[columnName];
												while (objColumnsList[aliasName.toLowerCase()]){
													aliasName = originalColumnName +"_"+ (objColumnsList[aliasName.toLowerCase()]+1);
												}
												aliasText = aliasName;
												objColumnsList[aliasName.toLowerCase()] = 1;
												objColumnsList[columnName] += 1;
												
											} else {
												objColumnsList[columnName] = 1;
												aliasText = val;
											}
										 
											targetTable.append(easyquerybuilder.buildTargetTableRow(aliasText));
											targetTable.find(".m-datatype").change();
										});
									}
									$("#customTargetTablePopup").modal('show');
						    	}
						    	else {
						    		$("#queryScript").addClass("alert-danger").removeClass('alert-success');
						    	}
				    		}else{
				    			common.showErrorAlert(result.messages[0].text);
				    		}
				    	}else{
				    		common.showErrorAlert(globalMessage['anvizent.package.label.unableToProcessYourRequest']);
				    	}
				    });
				
			   }
		}		
		
		 //standard package
		else{
			var isFlatFile = false;
			var connectionId = $("#connectionId").val();
		    var typeOfCommand = "Query";
		    var queryScript = $("#queryScript").val();		    
		    var packageId = $("#packageId").val();
		    var userID = $("#userID").val();	
		    var dbSourceName = $("#dataSourceName").val();
		    
		    var isHavingParentTable = false;
		    var isIncrementalUpdate = false;
		    if($("#il_incremental_update").is(':checked')){
		    	isIncrementalUpdate = true;
		    }else{
		    	isIncrementalUpdate = false;
		    }
			if(queryScript == '') {
				common.showcustommsg("#queryScript",globalMessage['anvizent.package.label.queryShouldNotBeEmpty']);
				return false;
			} 
			 
		   var selectData={
				   isMapped : true,
				   isFlatFile : isFlatFile,
				   iLConnection:{
					   connectionId : connectionId, 
				   },
				   typeOfCommand : typeOfCommand,
				   iLquery : queryScript,
				   iLId : iLId,
				   dLId : dLId,
				   packageId : packageId, 
				   isHavingParentTable :isHavingParentTable,
				   isIncrementalUpdate : isIncrementalUpdate,
				   ilSourceName: dbSourceName,
				   isWebservice : isWebservice
		   };   
		   var url_saveILConnectionMapping = "/app/user/"+userID+"/package/saveILsConnectionMapping";
		   showAjaxLoader(true);
		   var myAjax = common.postAjaxCall(url_saveILConnectionMapping,'POST', selectData,headers);
		    myAjax.done(function(result) {	    	
		    	showAjaxLoader(false);
		    		  if(result != null && result.hasMessages){
		    			  if(result.messages[0].code == "SUCCESS") {
								 var  messages=[{
									  code : result.messages[0].code,
									  text : result.messages[0].text
								  }];
								 $("#popUpMessage").empty();
								 $("#popUpMessage").text(result.messages[0].text).addClass('alert alert-success').removeClass('alert-danger');
								 $("#messagePopUp").modal('show');			    			  
			    			  }else{
			    				  common.displayMessages(result.messages);
			    			  }
		    		  }else{
		    			  easyquerybuilder.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });			   
		}
		
		if(isApisData == 'true'){
			$("#customApisDataPopUp").modal('show');
		}
		 
	});	
		
	//search box for table	
	$(document).on('keyup','#searchTable',function(e){
	    var tableListChildNode = $('.coveringDiv').find(".tableList-child-node");
	    var tableList = $('.coveringDiv').find(".tableList-child");
	    tableList.hide();
	    
	    for(var i = 0; i < tableListChildNode.length; i++){
	        var child = $(tableListChildNode).eq(i);
	        
	        if(($(child).text().toLowerCase()).indexOf($(this).val().toLowerCase()) != -1){
	            $(child).show();
	            child.parent().show();
	        }
	        
	    }
	});
 
	// Accordion Toggle Items
    var iconOpen = 'glyphicon glyphicon-minus-sign', iconClose = 'glyphicon glyphicon-plus-sign';

	$(document).on('show.bs.collapse hide.bs.collapse', '.accordion', function (e) {
	    var $target = $(e.target)
	      $target.siblings('.accordion-heading')
	      .find('span').toggleClass(iconOpen + ' ' + iconClose);
	      if(e.type == 'show')
	          $target.prev('.accordion-heading').find('.accordion-toggle').addClass('active');
	      if(e.type == 'hide')
	          $(this).find('.accordion-toggle').not($target).removeClass('active');
	});
	
	$(document).on('click', '#enableQuery', function () {		
		$('#queryScript').removeAttr("readonly");
		$("#disableQuery").show();
		$(this).hide();
	});
	
	$(document).on('click', '#disableQuery', function () {		
		$('#queryScript').attr("readonly","readonly");
		$("#enableQuery").show();
		$(this).hide();
	});
	
	$(document).on('click', '.tableList-child-node', function () {
		
 		var selectedTable = $(this).attr("title");
 		var schema = $("#existingSchemaList option:selected").val();
		var userID = $("#userID").val();
		var connectionId = $('#connectionId').val();
		var container = $(this).parents('.tableList-child').find("div.columnList-child-node");
		var isDDlayout = $('#isDDlayout').val();
		var isApisData = $("#isApisData").val();
		var selectedData = {
				connectionId : 	connectionId,
				database : {
					schema : schema,
					table : {
						tableName : selectedTable
					}
				},
				ddLayout:isDDlayout,
				apisData : isApisData
		}
		if($(this).hasClass('tableCollapse')){
			container.empty();
			$(this).removeClass("tableCollapse");
			$(this).addClass("tableExpand");
			$(this).find('input[name=checkAllColumns]').removeClass('tableCollapseChecked');
			
			var tableChecked=$(this).find('input[name=checkAllColumns]').is(":checked");
			showAjaxLoader(true);
			var url_getTableRelatedColumns = "/app/user/"+userID+"/package/getTableRelatedColumns";
			if(connectionId != null && connectionId != ''){
			   var myAjax = common.loadAjaxCall(url_getTableRelatedColumns,'POST',selectedData,headers);
			   myAjax.done(function(result) {
				   showAjaxLoader(false);
			    	  if(result != null && result.hasMessages) {
			    		  if(result.messages[0].code == "ERROR"){		    			  
			    			   alert(result.messages[0].text);
			    		  }
			    		  else if(result.messages[0].code == "SUCCESS"){
			    			  
			    			   easyquerybuilder.addColumnsToSeletedTable(result.object,container,tableChecked);		    			    			  
			    		  } 
			    	  }	else{
				    		var messages = [ {
				    			code : globalMessage['anvizent.message.error.code'],
				    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
				    		} ];
				    		common.displayMessages(messages);
				    	}	    	  	
			    });
			}
			else{
				 alert(globalMessage['anvizent.package.label.connectionIsisEmpty']); 
			}
		}else{
			$(this).removeClass("tableExpand");
		}
		
 	
	});
	
$(document).on('change', 'input[name=checkAllColumns]', function () {
		
 		var selectedTable = $(this).parents('.tableList-child').find('.tableList-child-node').attr("title");
 		var schema = $("#existingSchemaList option:selected").val();
		var userID = $("#userID").val();
		var connectionId = $('#connectionId').val();
		var container = $(this).parents('.tableList-child').find("div.columnList-child-node");
		var isDDlayout = $('#isDDlayout').val();
		var isApisData = $("#isApisData").val();
		var selectedData = {
				connectionId : 	connectionId,
				database : {
					schema : schema,
					table : {
						tableName : selectedTable
					}
				},
				ddLayout:isDDlayout,
				apisData : isApisData
		}
		 
		if($(this).hasClass('tableCollapseChecked')){
			container.empty();
			$(this).removeClass("tableCollapseChecked");
			$(this).addClass("tableExpandChecked");
			$(this).parents('.tableList-child').find('.tableList-child-node').removeClass('tableCollapse');
			var tableChecked=$(this).is(":checked");
			showAjaxLoader(true);
			var url_getTableRelatedColumns = "/app/user/"+userID+"/package/getTableRelatedColumns";
			if(connectionId != null && connectionId != ''){
			   var myAjax = common.loadAjaxCall(url_getTableRelatedColumns,'POST',selectedData,headers);
			   myAjax.done(function(result) {
				   showAjaxLoader(false);
			    	  if(result != null && result.hasMessages) {
			    		  if(result.messages[0].code == "ERROR"){		    			  
			    			   alert(result.messages[0].text);
			    		  }
			    		  else if(result.messages[0].code == "SUCCESS"){
			    			  
			    			   easyquerybuilder.addColumnsToSeletedTable(result.object,container,tableChecked);		    			    			  
			    		  } 
			    	  }	else{
				    		var messages = [ {
				    			code : globalMessage['anvizent.message.error.code'],
				    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
				    		} ];
				    		common.displayMessages(messages);
				    	}	    	  	
			    });
			}
			else{
				 alert(globalMessage['anvizent.package.label.connectionIsisEmpty']); 
			}
		}else{
			$(this).removeClass("tableExpandChecked");
			
		}
	});
	
	$("#queryScript").on("keyup", function(){
		$("#checkTablePreview, #saveILConnectionMapping").hide();
	});
	
	$("#customCustomTable").on('click', function() {
		
		common.clearcustomsg("#targetTableName");
		var packageId =  0;
		var industryId = "0";
	    var userID = $("#userID").val();
	    
	    var targetTableName = $("#targetTableName").val();
	    
	    if(targetTableName == ''){ 
	    	common.showcustommsg("#targetTableName", globalMessage['anvizent.package.label.pleaseEnterTargetTableName']);
			return false;
	    }else if(targetTableName.match(/\s/g)) {
	    	
	    	common.showcustommsg("#targetTableName", globalMessage['anvizent.package.label.tableNameShouldNotContainSpace']);
			return false;
	    }else if(/^[a-zA-Z0-9_]*$/.test(targetTableName) == false) {
	    	
	    	common.showcustommsg("#targetTableName", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters']+ "<br>"+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInTableName']);
			return false;
	    }else if(targetTableName.match(/^(IL_|DL_|il_|dl_|iL_|Il_|dL_|Dl_)/)) {
	    	common.showcustommsg("#targetTableName",globalMessage['anvizent.package.label.tableNameShouldNotContainILsandDLsNames']);
	    	return false;
	   }
	    
	    var selectors = [];

		selectors.push('#targetTableName');
	    var valid = common.validate(selectors);
	    if (!valid) {
			return false;
		}
		
	    var status = easyquerybuilder.validateTargetTableFields();
	    if(!status){	    	
	    	return false;
	    }
	    
	    var columns = [];
	    var process = true;
	    var tablerows = $("#customTargetTable").find("tbody").find("tr");
	    
	    tablerows.each(function() {
	    	var row = $(this);
	    	var columnName = row.find('.m-column').text();
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
	    			alert(globalMessage['anvizent.package.label.lengthShouldNotBeEmptyFor']+' '+dataType);
	    			process = false;
	    			return false;
	    		}
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
	    
	    var selectData = {
			userId : userID,
			userPackage : {
				packageId : packageId,
				industry : {
					id : industryId
				},
				table : {
					tableName : targetTableName,
					isDirect : false,
					columns :columns,
					ddlTables : ddlTables.substring(0,ddlTables.length - 1)
				}
			},
			ilConnectionMapping : {
				iLquery :  $("#queryScript").val(),
			}
	    };
	    
	    showAjaxLoader(true); 
	    var url = "/app/user/"+userID+"/package/createsCustomTargetTable";
	    var myAjax = common.postAjaxCall(url,'POST', selectData,headers);
	    
	    myAjax.done(function(result) {
	    	
	    	if (result != null && result.hasMessages) {
	    		showAjaxLoader(false);
	    		var messages = result.messages;
	    		var msg = messages[0];
	    		if (msg.code === "SUCCESS") {
	    			//window.location = adt.appContextPath+'/adt/package/ddLayout';
	    			 $("#popUpMessageForDDlTable").empty();
					 $("#popUpMessageForDDlTable").append("<div class='alert alert-success'>"+result.messages[0].text+"</div>");
					 $("#messagePopUpDDlTable").modal('show');    	
	    		}
	    		else if (msg.code === "DUPL_TARGET_TABLE") {
	    			common.showcustommsg("#targetTableName", msg.text);
	    		}
	    		else {
	    			common.showcustommsg("#targetTableName", msg.text);
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
	$(document).on('change', 'select.m-datatype', function() {
		var datatype = $(this), colsize = datatype.closest('tr').find('.m-colsize');
		
		var decimal = datatype.closest('tr').find('.m-decimal');
		decimal.val("");
		decimal.hide();
		
		for(var i=0;i<dataTypeResult.object.length;i++){
			 var dataTypeName = dataTypeResult.object[i].dataTypeName;
			 var range = dataTypeResult.object[i].range;
			 if(datatype.val() === dataTypeName){
				 if(datatype.val() == "DECIMAL" || datatype.val() == "FLOAT" || datatype.val() == "DOUBLE"){
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
		
		/*if(datatype.val() === 'INT' || datatype.val() === 'BIGINT')
			colsize.val("10").attr("disabled",false);
		else if(datatype.val() === 'DATETIME')
			colsize.val("19").attr("disabled",true);
		else if(datatype.val() === 'DATE')
			colsize.val("0").attr("disabled",true);
		else if(datatype.val() === 'VARCHAR')
			colsize.val("100").attr("disabled",false);
		else if(datatype.val() === 'BIT')
			colsize.val("1").attr("disabled",true);
		else if(datatype.val() === 'DECIMAL') {
			colsize.val("24").attr("disabled",false);
			decimal.val("7")
			decimal.show();
		}*/
		

		var autoInc = datatype.closest('tr').find('.m-auto');
		if(datatype.val() == 'INT' || 	datatype.val() == 'BIGINT' || datatype.val() == 'TINYINT' || datatype.val() == 'SMALLINT' || datatype.val() == 'MEDIUMINT'){
			if (autoInc.is(":disabled")) {
				autoInc.prop("disabled","");
			}
		} else {
			if ( autoInc.is(":checked") ) {
				autoInc.prop("checked",true).click();
			}
			if (autoInc.is(":enabled")) {
				autoInc.prop("disabled","disabled");
			}
			
		}
		
		
	});
	$("table#customTargetTable").on('click', ".m-auto", function() {
		var obj = $(this);
		$("table#customTargetTable .m-notnull, table#customTargetTable .m-primary,.m-default").prop("disabled", ""); 
		if (this.checked) {
			this.checked = false;
			
			$("table#customTargetTable .m-auto:checked").each(function(index,rowObject){
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
	
$("#customApisData").on('click', function() {
	common.clearValidations(["#apiName,#endPointUrl,#apiDescription,#methodType"])
		
	    var userID = $("#userID").val();
	    
	    var apiName = $("#apiName").val();
	    var endPointUrl = $("#endPointUrl").val();
	    var methodType =  $("[name='methodType']:checked").val();
	    var apiDescription = $("#apiDescription").val();
	    
	    if(apiName.trim().length == 0){ 
	    	common.showcustommsg("#apiName", globalMessage['anvizent.package.label.pleaseEnterAPIName']);
			return false;
	    }
	    if(endPointUrl == ''){ 
	    	common.showcustommsg("#endPointUrl", globalMessage['anvizent.package.label.pleaseEnterEndPointUrl']);
			return false;
	    }
	    
	    if(apiDescription == ''){ 
	    	common.showcustommsg("#apiDescription", globalMessage['anvizent.package.label.pleaseEnterApiDescription']);
			return false;
	    }
	    if(!methodType){ 
	    	common.showcustommsg(".method_type",  globalMessage['anvizent.package.label.pleaseChooseMethodType']);
			return false;
	    }
	    
	    var selectors = [];

		selectors.push('#apiName');
	    var valid = common.validate(selectors);
	    if (!valid) {
			return false;
		}
		
	   var selectData = {
			   apiName : apiName,
			   endPointUrl : endPointUrl,
			   apiDescription : apiDescription,
			   methodType : methodType,
			   apiQuery : $("#queryScript").val()
			  
	   }
	    
	    showAjaxLoader(true); 
	    var url = "/app/user/"+userID+"/package/saveApisDataInfo";
	    var myAjax = common.postAjaxCall(url,'POST', selectData,headers);
	    
	    myAjax.done(function(result) {
	    	
	    	if (result != null && result.hasMessages) {
	    		showAjaxLoader(false);
	    		var messages = result.messages;
	    		var msg = messages[0];
	    		if (msg.code === "SUCCESS") {
	    			 $("#popUpMessageForApisData").empty();
					 $("#popUpMessageForApisData").append("<div class='alert alert-success'>"+result.messages[0].text+"</div>");
					 $("#messagePopUpApisData").modal('show');    	
	    		}else{
	    			common.showErrorAlert(result.messages[0].text);
		    		common.displayMessages(messages);
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


} 
