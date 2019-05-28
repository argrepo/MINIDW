var fileHeaders = '';
var dataTypeResult = '';
var  fileMappingWithILTable = {
			updateFileMappingWithILTable  : function(result,param){
				var table = $("#fileMappingWithILTable tbody");
				table.empty();
				 
				if(result.object["originalFileheaders"]) {
					fileHeaders = result.object["originalFileheaders"];
					var iLColumns = result.object["iLcolumnNames"];
					var fileHeadersSelect = '';
					for(var i = 0; i < fileHeaders.length; i++) {
						fileHeadersSelect += "<option value='"+fileHeaders[i].toLowerCase()+"'>"+fileHeaders[i]+"</option>";
					}
					var sno = 0;

					for(var i = 0; i < iLColumns.length ; i++) {
						var columnname='';
						var isPrimaryKey=iLColumns[i].isPrimaryKey;
						var isPrimaryKeyVal=iLColumns[i].isPrimaryKey;
						var isNotNull=iLColumns[i].isNotNull;
						var isNotNullVal=iLColumns[i].isNotNull;
						var notNull='';
						var isAutoIncrement =iLColumns[i].isAutoIncrement;
						var isAutoIncrementVal =iLColumns[i].isAutoIncrement;
						if(!result.object["isdefaultMapping"]) {
						if (isAutoIncrement || iLColumns[i].columnName.match("^XRef_")) {
							continue;
						}
						}
						if(isNotNull == true && isAutoIncrement == true){
							notNull = 'NO';
						}else if(isNotNull == true && isAutoIncrement == false){
							notNull = 'YES';
						}
						isPrimaryKey == true ? isPrimaryKey='<span class="glyphicon glyphicon-ok"></span>' : isPrimaryKey='' ;
					    isNotNull == false  ? isNotNull='' : isNotNull='<span class="glyphicon glyphicon-ok"></span>' ;
					    isAutoIncrement == true ? isAutoIncrement='<span class="glyphicon glyphicon-ok"></span>' : isAutoIncrement='';
					    
					    //Standard Package Il Mapping Table
					    if(param == 'standard' || param == "viewIl"){
					    	  if(notNull == 'YES'){
							    	columnname	= iLColumns[i].columnName+' <span class="required">*</span>';
							    }
							    else{
							    	columnname	= iLColumns[i].columnName;
							    }
					    	$('#fileMappingWithILTable').find('.addordelete').hide();
							var tableRow =  "<tr>"+
								"<td>"+(sno+1)+"</td>"+
								"<td class='iLcolumnName'>"+columnname+"</td>"+
								"<td class='coldatatype' >"+iLColumns[i].dataType+"</td>"+
								"<td class='length' >"+iLColumns[i].columnSize+"</td>"+
								"<td class='primaryKey'>"+isPrimaryKey+"</td>"+
								"<td class='notNull' id='notNull"+i+"' data-notNull='"+notNull+"' >"+isNotNull+"</td>"+
								"<td class='autoIncrement'>"+isAutoIncrement+"</td>"+
								"<td><select class='form-control fileHeader' id='fileHeader"+i+"'>" +
									"<option value=''>"+globalMessage['anvizent.package.label.selectFileColumn']+"</option>"+
									""+fileHeadersSelect+"" +
								"</select></td>";
							     if(iLColumns[i].dataType == 'DATETIME'){
							    	 tableRow+="<td><input type='text' class='form-control default default-dtype' placeholder='"+globalMessage['anvizent.package.label.yyyyMMdd']+"'></td>";
							     }
							     else{
							    	 tableRow+="<td><input type='text' class='form-control default default-dtype'></td>";
							     }
							     tableRow+= "</tr>";
							tableRow = $(tableRow);
							table.append(tableRow);
					      }
					    
					    //Custom Package Il Mapping Table
					    else{
					    	if(notNull == 'YES'){
						    	columnname	= iLColumns[i].columnName;
						    }
						    else{
						    	columnname	= iLColumns[i].columnName;
						    }
					    	var addButton = "<button style='margin:5px;' class='btn btn-primary btn-sm' id='addUnmappedColumn'><span class='glyphicon glyphicon-plus'></span></button>";
						    var addOrDelete = "<td class='delete'>" +
												"<button style='margin:5px;' class='btn btn-primary btn-sm deleteUnmappedColumn'>"+
													"<span class='glyphicon glyphicon-trash'></span>"+
												"</button>"+
											  "</td>"
						var columnSize = "";
						for(var idx in dataTypeResult.object){
							if(dataTypeResult.object[idx].dataTypeName === iLColumns[i].dataType){
								columnSize = (dataTypeResult.object[idx].range == null|| dataTypeResult.object[idx].range == "") ? "" : iLColumns[i].columnSize;
						 }
						}
						var tableRow =  "<tr>"+
							"<td>"+(sno+1)+"</td>"+
							"<td class='iLcolumnName'><input type='text' class='form-control m-column' value="+columnname+" readonly='readonly'></td>"+
							"<td class='ilColumndatatype' data-dtype='"+iLColumns[i].dataType+"' >"+iLColumns[i].dataType+"</td>"+
							"<td class = 'ilColumnsize' nowrap data-colsize='"+columnSize+"'>"+columnSize+"</td>"+
							"<td class='primaryKey' data-primarykeyval='"+isPrimaryKeyVal+"'>"+isPrimaryKey+"</td>"+
							"<td class='notNull' data-notnullval='"+isNotNullVal+"' id='notNull"+i+"' data-notNull='"+notNull+"'>"+isNotNull+"</td>"+
							"<td class='autoIncrement' data-autoincrementval='"+isAutoIncrementVal+"'>"+isAutoIncrement+"</td>"+
							"<td><select class='form-control fileHeader' data-column='"+fileHeaders+"' id='fileHeader"+i+"'>" +
								"<option value=''>"+globalMessage['anvizent.package.label.selectFileColumn']+"</option>"+
								""+fileHeadersSelect+"" +
							"</select></td>";
						     if(iLColumns[i].dataType == 'DATETIME'){
						    	 tableRow+="<td><input type='text' class='form-control default default-dtype' placeholder='"+globalMessage['anvizent.package.label.yyyyMMdd']+"'></td>";
						     }
						     else{
						    	 tableRow+="<td><input type='text' class='form-control default default-dtype'></td>";
						     }
						     
						     if(param != 'standard')
						     tableRow+= addOrDelete;
						     tableRow+= "</tr>";
						tableRow = $(tableRow);
						table.append(tableRow);
					    }
					    
					sno++;
					 
					var sel = tableRow.find('select.fileHeader');
					
					if(param == 'viewIl'){
						var defualtValue = tableRow.find('input.default-dtype');
						var mappedWebserviceHeaders = result.object["mappedWebserviceHeaders"];
						$.each( mappedWebserviceHeaders, function(key, value ) {
							if(iLColumns[i].columnName == key.trim()) 
							{
								if(value.indexOf("{") >= 0 ){
									var replacedVlaue = value.replace("{","").replace("}","");
									defualtValue.val(replacedVlaue.trim());
								  }else if(value.indexOf("{") == -1 ){
									  sel.val(value.trim().toLowerCase());  
								 }
							}
							if (!sel.val()) {
								sel.find('option:first').prop('selected', true);
								sel.removeClass("is-mapped").addClass("not-mapped");
							}
							});
						$("#fileMappingWithILTable .fileHeader").change();
					} else if(param == 'queryBr'){
						sel.val(iLColumns[i].columnName.toLowerCase()).addClass("is-mapped");
						if (!sel.val()) {
							sel.find('option:first').prop('selected', true);
							sel.removeClass("is-mapped").addClass("not-mapped");
						}
					}
					else{
						sel.val(iLColumns[i].columnName.toLowerCase()).addClass("is-mapped");
						if (!sel.val()) {
							sel.find('option:first').prop('selected', true);
							sel.removeClass("is-mapped").addClass("not-mapped");
						}
						
						if($('#webService').is(':checked') || $("#isFromWebServiceJoin").val()) { 
							$("#saveMappingWithIL").hide();
							$("#saveMappingWithILForWebService").show();
							
							}else{
								$("#saveMappingWithIL").show();
								$("#saveMappingWithILForWebService").hide();	
							}
					}
				}
			}		
				if(param == 'standard'){
					$('#deleteColumnMessageAlertMessage').hide();
				}else{
					$("#fileMappingWithILTable tbody tr:last").find(".delete").prepend(addButton);
				}
				$("#fileMappingWithILPopUp").modal('show');
		}
   };

var newDataTypes = {
		getDataTypeList : function(result){
			dataTypeResult = result;
		}
}

var newColumnRow = {
		getNewColumnRow : function(unMappedList,rowCount,dataTypeValue){
			debugger
			var dataTypes = '';
			var table = $("#fileMappingWithILTable tbody");
			//table.empty();
			var fileHeaders_select = '';
			/*var dataTypes = ["VARCHAR","INT","BIGINT","DECIMAL","BIT","DATETIME","DATE"];
			var colSize = "";
			var getColSize = {
					"VARCHAR" : "45",
					"INT" : "11",
					"BIGINT" : "20",
					"DECIMAL" :["24", "7"],
					"BIT" : "1",
					"DATETIME" : "19",	
					"DATE" : "0"
			};*/
			
			for(var i = 0; i < fileHeaders.length; i++) {
				if(unMappedList == fileHeaders[i]){
					fileHeaders_select += "<option value='"+fileHeaders[i].toLowerCase()+"' selected='selected'>"+fileHeaders[i]+"</option>";
				}else{
					fileHeaders_select += "<option value='"+fileHeaders[i].toLowerCase()+"'>"+fileHeaders[i]+"</option>";
				}
				
			}
			
			
			selectDatatype = '<select class="form-control input-sm dataType m-datatype" style="width:100%;">';
			
			
			for(var i=0;i<dataTypeResult.object.length;i++){
				 var dataTypeName = dataTypeResult.object[i].dataTypeName;
				 var range = dataTypeResult.object[i].range;
				 
				 if(dataTypeName == dataTypeValue){
					 selectDatatype+="<option value='"+dataTypeName+"' selected>"+dataTypeName+"</option>"; 
					 if(dataTypeValue == "DECIMAL" || dataTypeValue == "FLOAT" || dataTypeValue == "DOUBLE"){
							var columnSizeDecimal = obj["columnSize"].split(",");
							colSize+= '<input type="text" class="input-sm m-colsize" value='+columnSizeDecimal[0]+' style="width:50%;"/>'+
						  		  	  '<input type="text" class="input-sm m-decimal" value='+columnSizeDecimal[1]+' style="width:50%;"/>';
					}else{
						   colSize = '<input type="text" class="input-sm m-colsize" value='+obj["columnSize"]+' style="width:50%;"/>'+
				          '<input type="text" class="input-sm m-decimal" value="2" style="width:50%;display:none;"/>';
					}
					 
				 }else{
					 selectDatatype+="<option value='"+dataTypeName+"'>"+dataTypeName+"</option>";
						if(dataTypeValue == "" || dataTypeValue == null){
							colSize = '<input type="text" class="input-sm m-colsize" value="45" style="width:50%;"/>'+
				  		  	  		  '<input type="text" class="input-sm m-decimal" value="2" style="width:50%;display:none;"/>';
						}
				 }
			 }
			
			selectDatatype += '</select>'
			
			/*var selectDatatype = '<select class="form-control input-sm dataType m-datatype">';
			$.each(dataTypes, function(j,val){
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
					selectDatatype+="<option value='"+val+"'>"+val+"</option>";
					if(dataTypeValue == "" || dataTypeValue == null){
						colSize = '<input type="text" class="input-sm m-colsize" value="45" style="width:50%;"/>'+
			  		  	  		  '<input type="text" class="input-sm m-decimal" value="2" style="width:50%;display:none;"/>';
					}
				}			
			});
		selectDatatype+='</select>';*/
		
		
			var addButton = "<button style='margin:5px;' class='btn btn-primary btn-sm' id='addUnmappedColumn'><span class='glyphicon glyphicon-plus'></span></button>";
			var newTableRow =  "<tr>"+
			"<td>"+rowCount+"</td>"+
			"<td class='iLcolumnName'><input  type='text' class='form-control m-column' value='"+unMappedList+"'></td>"+
			"<td>"+selectDatatype+"</td>"+
			"<td nowrap>"+colSize+"</td>"+
			"<td class='primaryKey'><input type='checkbox' class='m-primary'></td>"+
			"<td class='notNull'><input type='checkbox' class='m-notnull'></td>"+
			"<td class='autoIncrement'><input type='checkbox' class='m-auto' disabled = 'disabled'></td>"+
			"<td><select class='form-control fileHeader' data-column='"+fileHeaders+"' id='fileHeader"+rowCount+"'>" +
				"<option value=''>"+globalMessage['anvizent.package.label.selectFileColumn']+"</option>"+
				""+fileHeaders_select+"" +
			"</select></td>";
		     newTableRow+="<td><input type='text' class='form-control default default-dtype'></td>";
		     var addOrDelete = "<td style='margin:5px;' class='delete'>" +
							"<button class='btn btn-primary btn-sm deleteUnmappedColumn'>"+
								"<span class='glyphicon glyphicon-trash'></span>"+
							"</button>"+
						  "</td>"
			 $("#addUnmappedColumn").remove();			
							
		     newTableRow+= addOrDelete;
		     newTableRow+= "</tr>";
		     newTableRow = $(newTableRow);
		     table.append(newTableRow);
		     $("#fileMappingWithILTable tbody tr:last").find(".delete").prepend(addButton);
		}
}