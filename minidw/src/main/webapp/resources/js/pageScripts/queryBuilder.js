var headers = {};
var existedILColumns = '';
var dataTypeResult = "";
var dataTypeSelect = "";
if($('.queryBuilder-page').length) {

var queryBuilder = {
	initialPage : function() {
			var sessionInterval = setInterval(function(){
				common.refreshSessionURL()
			},15*60*1000);
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			common.refreshSessionURLLoop();
		},
	buildQuery : function() {
		$("#querycontainer").removeClass("alert-success alert-danger");
		$("#savebutton").hide();
		var printquery, addcomma,
		query = [], tables = [], tblaliases = {};
		query.push('SELECT ');
		
		var jointypes = {};
		
		$('select.j-type').each(function() {
			var jtype = $(this), 
			row = jtype.closest('.row');
			
			var jtable = row.find('.j-tablename').val();
			
			jointypes[jtable] = jtype.val();
		});
		var objColumnsList = {};
		$('select.temp-table').each(function(t) {
			var sel = $(this), table = sel.data('table'), tblalias = sel.data('tblalias');
			
			tblaliases[table] = tblalias;
			
			var opts = sel.find('option:selected');
		 
			if (opts.length) { 
				if (addcomma) query.push(', ');
				
				opts.each(function(i) {
					var columnName = this.value.toLowerCase(); 
					var originalColumnName = this.value; 
					
					var aliasName = "";
					var aliasText = ""; 
					
					if (objColumnsList[columnName] ) {
						aliasName = originalColumnName +"_"+ objColumnsList[columnName];
						while (objColumnsList[aliasName.toLowerCase()]){
							aliasName = originalColumnName +"_"+ (objColumnsList[aliasName.toLowerCase()]+1);
						}
						
						aliasText = " as " + aliasName;
						objColumnsList[aliasName.toLowerCase()] = 1;
						objColumnsList[columnName] += 1;
						
					} else {
						objColumnsList[columnName] = 1;
					}
					
					query.push('\n',tblalias, '.', "`"+this.value+"`",aliasText);
					if (i<opts.length-1) 
						query.push(', '); 
				});
				if (!printquery)
					printquery = true;
				
				addcomma = true;
			}
			else {
				addcomma = false;
			}
		});
		
		$("select.j-tablename").each(function() {
			tables.push(this.value);
		});
		 
		query.push('\n FROM ');
		

		var joins = [];
		
		$('div.j-row').each(function() {
			var row = $(this);
			var firsttbl = row.find('select.j-table.first').val(),
			secondtbl = row.find('select.j-table.second').val(),
			firstcol = row.find('select.j-column.first').val(),
			secondcol = row.find('select.j-column.second').val();
			
			if (firsttbl &&  firstcol && secondtbl && secondcol) {
				joins.push({'firsttbl': firsttbl, 'firstcol': firstcol, 'secondtbl': secondtbl, 'secondcol':secondcol, 'added': false});
			}
		});
		
		var tbllen = tables.length;
		var jtables = [];
		for (var i=0;i < tbllen; i++) {
			
			var addand = false;
			var secondtable;
			
			if (i>0 && i < tbllen) {
				query.push(' ', jointypes[secondtable], ' ');
			}
			secondtable = tables[i];
			query.push(secondtable, ' as ', tblaliases[secondtable]);
			
			if (i>0 && i < tbllen) {
				query.push(' ON');
				
				if (joins.length>0) {
					$.each(joins, function(jr, jobj) {
						if (!jobj.added) {
							var ft = jobj['firsttbl'], fc = jobj['firstcol'], st = jobj['secondtbl'], sc = jobj['secondcol'];
							
							if (ft === secondtable) {
								
								if (jtables.indexOf(st) !== -1) {
									if (addand)
										query.push(' AND');
									query.push(' ', tblaliases[st], ".", sc, " = ", tblaliases[ft], ".", fc);
									if (!addand) addand = true;
								}
								
							}
							else if (st === secondtable) {
								
								if (jtables.indexOf(ft) !== -1) {
									if (addand)
										query.push(' AND');
									query.push(' ', tblaliases[ft], ".", fc, " = ", tblaliases[st], ".", sc);
									if (!addand) addand = true;
								}
							}
						}
					});
				}
				else {
					if (printquery)
						printquery = false;
				}
				
			}
			jtables.push(secondtable);
			
		}
		
		if (printquery) {
			debugger
			console.log("query",query);
			$('#querycontainer').val(query.join(''));
			$('#querycontainer').trigger("keypress");
		}
	},
	
	
	getDataTypeList : function(){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var userID = $("#userID").val();
		
		var url_getDataTypesList = "/app/user/" + userID + "/package/getDataTypesList";
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
	    				 queryBuilder.getDataTypes(result);
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
		debugger
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
		row.push('<td>', '<input type="text" class="form-control input-sm m-default"/>', '</td>');
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
	saveILConnectionMapping :  function(selectData){
		var userID = $("#userID").val();
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
								 $("#popUpMessage").text(result.messages[0].text).addClass('alert alert-success').removeClass('alert-danger').show();
								 $("#fileMappingWithILPopUp").modal('hide');
								 $("#messagePopUp").modal('show');
				    			  
			    			  }else {
						    		common.displayMessages(result.messages);
						    	}
		    		  }else{
		    			  queryBuilder.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });
	},
	showMessage:function(text){
		$(".messageText").empty();
		$(".successMessageText").empty();
		$(".messageText").html(text);
	  $(".message").show();
	  setTimeout(function() { $(".message").hide(); }, 10000);
	},
	
	buildMappedQuery : function(){
		debugger
		var dropQuery = '';
		var addQuery = '';
		var existedColumnNames = [];
		var existedColumnList = {};
		var addedColumnsList = {};
		var allColumnsList = [];
		var dropColumns = [];
		var addQueryColumns = '';
    	var quoteChar = '`';
		var tableName = $("#fileMappingWithILTable").find('th.iLName').text();
		var queries = [];
		
		for(var i=0;i<existedILColumns.length;i++){
			existedColumnNames.push(existedILColumns[i].columnName);
			existedColumnList[existedILColumns[i].columnName] = existedColumnList[existedILColumns[i].columnName];
		}
		$("#fileMappingWithILTable tbody tr td.iLcolumnName").each(function(){
			addedColumnsList[$(this).find(".m-column").val()] = addedColumnsList[$(this).find(".m-column").val()];
			
		});
		dropQuery = "ALTER TABLE "+quoteChar +tableName+ quoteChar;
		for(var i=0; i < existedColumnNames.length;  i++){
			if(!(existedColumnNames[i] in addedColumnsList)){
				dropColumns.push(" drop column "+quoteChar +existedColumnNames[i]+ quoteChar ) ;
			}
		}
		
		dropQuery += dropColumns.join(',') + ";";
		if(dropColumns.length != 0){
			queries.push(dropQuery);
			queries.push(" & ");
		}
		
		addQuery = "ALTER TABLE "+quoteChar +tableName+ quoteChar;
		var primaryKey = ", ADD PRIMARY KEY (";
		var count = 0;	
		var primaryVal = [];
		var tableLength = $("#fileMappingWithILTable tbody tr").length;
		var dropPrimaryQueryStatus = false;
		$("#fileMappingWithILTable tbody tr").each(function(){
			var row = $(this);
			var dataType = '';
			var columnSize = '';
			var decimalPoints = '';
			var columnName = row.find(".m-column").val();
			var fileHeader = row.find('.fileHeader').val() != ''? row.find('.fileHeader option:selected').text() : " ";
			var defaultValue = '';
			var isPrimaryKey = '';
	    	var isNotNull = '';
	    	var isAutoIncrement = '';
	    	var defaultValue = '';
			if(!(columnName in existedColumnList)){
			    dataType = row.find('.m-datatype').val();
				//columnSize = row.find('.m-colsize').val();
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
				if(fileHeader.trim() == ''){
					defaultValue = row.find(".default").val() != ''? row.find('.default').val() : " ";
					var value = isNotNull ? " NOT NULL ":" NULL ";
					addQueryColumns += " ADD COLUMN " + quoteChar +columnName+ quoteChar +" " + dataType + "(" +columnSize+ ") " + value + " DEFAULT "+ "'"+defaultValue +"'," ;
				}else{
					var keyValue = isNotNull ? " NOT NULL ":" NULL ";
					if(dataType == "INT" || dataType == "BIGINT" || dataType == "TINYINT" || dataType == "SMALLINT" || dataType == "MEDIUMINT"){
						isAutoIncrement ? keyValue += " AUTO_INCREMENT ":"";
					}
					if(columnSize != "")
						addQueryColumns += " ADD COLUMN " + quoteChar +columnName+ quoteChar +" " + dataType + "(" +columnSize+ ")"+keyValue+ ",";
					else
						addQueryColumns += " ADD COLUMN " + quoteChar +columnName+ quoteChar +" " + dataType + " " +keyValue+ ",";	
					
				}
				if(isPrimaryKey){
					 if(isAutoIncrement){
						 primaryVal.splice(0, 0, quoteChar +columnName+ quoteChar);
					 }else{
						 primaryVal.push(quoteChar +columnName+ quoteChar);
					 }
				}
			}
			else{
			    dataType = row.find('.ilColumndatatype').text();
				//columnSize = row.find('.m-colsize').val();
				//decimalPoints = row.find('.m-decimal').text();
			 	isPrimaryKey = row.find('.primaryKey').data('primarykeyval');
		    	isNotNull = row.find('.notNull').data('notnullval');
		    	isAutoIncrement = row.find('.autoIncrement').data('autoincrementval');
		    	defaultValue = row.find('.default-dtype').val();
				columnSize = row.find('.ilColumnsize').text();
				if(fileHeader.trim() == ''){
					defaultValue = row.find(".default-dtype").val() != ''? row.find('.default-dtype').val() : " ";
					var value = isNotNull ? " NOT NULL ":" NULL ";
					if(defaultValue != "")
					  addQueryColumns += " CHANGE COLUMN " + quoteChar +columnName+ quoteChar +" " + quoteChar +columnName+ quoteChar +" " + dataType + "(" +columnSize+ ") " + value + " DEFAULT "+ "'"+defaultValue +"'," ;
				}
				if(isPrimaryKey){
					primaryVal.push(quoteChar +columnName+ quoteChar);
					dropPrimaryQueryStatus = true;
				}
				
			}
			
			if(count == tableLength-1){
				addQueryColumns = addQueryColumns.substring(0, addQueryColumns.length - 1);
				if(primaryVal.length != 0){
					if(dropPrimaryQueryStatus){
						addQueryColumns += ", DROP PRIMARY KEY ";
						dropPrimaryQueryStatus = false;
					}
					primaryKey += primaryVal.join(',') + ")";
					addQueryColumns +=  primaryKey;
				}else{
					addQueryColumns = addQueryColumns/*.substring(0, addQueryColumns.length - 1);*/;
				}
				addQueryColumns += ";";
			}
			count++;
		});
		debugger
		addQuery += addQueryColumns;
		if(addQueryColumns.length != 0){
			queries.push(addQuery);			
		}
		return queries;
	},
	
	getUpdatedTargetMappedQuery : function(){
		var mappedQuery = queryBuilder.buildMappedQuery();
		console.log(mappedQuery);
		var generatedSql = "";
		$.each(mappedQuery, function(i, query){
			generatedSql += mappedQuery[i]+ " ";
		});
		var selectData = {
    			iLquery :generatedSql.trim(),
    	} 
		showAjaxLoader(true);
	    var url_getUpdatedTargetMappedQuery = "/app/user/"+userID+"/package/getUpdatedTargetMappedQuery";
		 var myAjax = common.postAjaxCall(url_getUpdatedTargetMappedQuery,'POST', selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    	  if(result != null && result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") {
				    			  common.showErrorAlert(result.messages[0].text);
				    			  return false;
			    			  }
		    			  if(result.messages[0].code == "SUCCESS") {
		    				  $("#alertForTargetMappingPopUp").modal('show');
		    				  $("#targetPopUpMessage").text(result.messages[0].text).addClass('alert alert-success');
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
	
	validateTargetMappedHeaders : function(){
		debugger
		 var tablerows = $("#fileMappingWithILTable").find("tbody").find("tr");
		 var mappedList = [];
		 var matchedCount = 1;
		 var status = true;
		 tablerows.each(function() {
		    	var row = $(this);
		    	var fileHeader = row.find('.fileHeader').val() != null ? row.find('.fileHeader option:selected').val() : " ";
		    	if(fileHeader != ''){
		    		if($.inArray(fileHeader, mappedList) != -1){
		    			matchedCount++;
		    		}else{
		    			mappedList.push(fileHeader);
		    		}
		    	}
		    	if(matchedCount > 1){
		    		var id =  row.find('.fileHeader').attr('id');
	    			common.clearValidations(["#"+id]);
	    			common.showcustommsg("#"+id, globalMessage['anvizent.package.label.alreadyMapped'],"#"+id);
	    			status=false;
	    			matchedCount = 0;
	    			return false;
	    			
		    	}
		 });
		 return status;
	},
	
};
queryBuilder.initialPage();
var aliasColumnNames = [];
$(".checkbox").on('click',function(){
	var printquery, addcomma,
	query = [], tables = [], tblaliases = {};
	query.push('SELECT ');
	$(this).parent().find("label.checkbox-label").text(this.checked?globalMessage['anvizent.package.label.unselectAll']:globalMessage['anvizent.package.label.selectAll']);
	if( this.checked ){
		$(this).parent().next().find('option').prop("selected",true);
		queryBuilder.buildQuery();		
	}
	else{
		$(this).parent().next().find('option').prop("selected",false);
		$("#querycontainer").val("");
		queryBuilder.buildQuery();
	}	
	$('.temp-table').trigger('change');
	$("button#savebutton").hide();
	$("#querycontainer").removeClass('alert-success alert-danger');
});

$(document).on("select2:unselect",".temp-table",function(){	 
	$(this).parent().find("label.checkbox-label").text(this.checked?globalMessage['anvizent.package.label.unselectAll']:globalMessage['anvizent.package.label.selectAll']);
	$(this).parent().find("input[type='checkbox']").prop("checked",false);
});
	
$(document).on("select2:open","select.j-column",function(){	
	if($(this).find('option:eq(1)').val() == "Select All" || $(this).find('option:eq(1)').val() == "Unselect All"){
		$(this).find('option:eq(1)').remove();		
	}		
});

$(document).on('change', 'select.m-datatype', function() {
	var datatype = $(this);
	var colsize = datatype.closest('tr').find('.m-colsize');
	
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
	if(datatype.val() == 'INT' || 	datatype.val() == 'BIGINT' || datatype.val() == "TINYINT" || datatype.val() == "SMALLINT" || datatype.val() == "MEDIUMINT"){
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

$('select.temp-table').select2({
	 closeOnSelect: false
})
.each(function() {
	var obj = $(this),
	table = obj.data('table'),
	tblalias = obj.data('tblalias');
	obj.data('table', table);
	obj.data('tblalias', tblalias);
})
.on('select2:closing', function() {
	$("#querycontainer").val("");
	queryBuilder.buildQuery();
});

$('select.j-table').select2({ width: 'resolve' });

$('body').on('select2:select', 'select.j-table', function() {
	queryBuilder.buildQuery();
	var sel = $(this), val = this.value;
	var col;
	if (sel.hasClass('first')) {
		col = sel.closest('.j-row').find('.j-column').filter(function() {
			return $(this).hasClass('first');
		});
	}
	else {
		col = sel.closest('.j-row').find('.j-column').filter(function() {
			return $(this).hasClass('second');
		});
	}
	
	col.html('');
	col.append('<option value="">'+globalMessage['anvizent.package.label.selectColumn']+'</option>');
	$('select.temp-table').each(function() {
		var obj = $(this);
		if (obj.data('table') === val) {
			col.append(obj.html());
			return false;
		}
	});
	col.trigger('change');
});

$('select.j-column').select2({ width: 'resolve' });

$('body').on('select2:select', 'select.j-column', function() {
	queryBuilder.buildQuery();
});

$('body').on('click', 'button.add-j-row', function() {
	
	var row = $("<div />", {"class": "row j-row", "style": "margin-top:5px;"});
	var selcols = $("<div />", {"class": "col-xs-11", "style": "padding-left:0px;padding-right:0px;"});
	var addcol = $("<div />", {"class": "col-xs-1"});
	var jrow = $('div.j-row:first');
	
	row.append(selcols);
	row.append(addcol);
	
	jrow.find('select').each(function(i) {
		var sel = $(this), selcol = $("<div />", { "class" : "col-xs-3"});
		
		var newsel = $("<select />",{"style": "width:100%;"});
		
		if (sel.hasClass('j-table'))
			newsel.addClass('j-table');
		
		if (sel.hasClass('j-column'))
			newsel.addClass('j-column');
		
		if (sel.hasClass('first'))
			newsel.addClass('first');
		
		if (sel.hasClass('second'))
			newsel.addClass('second');
		
		
		newsel.html(sel.html());
		newsel.val(null);
		
		if (newsel.hasClass('j-column')) {
			newsel.find('option:not(:first)').remove();
		}
		
		selcol.append(newsel);
		selcols.append(selcol);
		
		newsel.select2({ width: 'resolve' });
	});
	
	
	addcol.append('<button type="button" class="btn btn-primary btn-xs add-j-row" style="margin-right: 2px;"><span class="glyphicon glyphicon-plus"></span></button>');
	addcol.append('<button type="button" class="btn btn-primary btn-xs delete-j-row"><span class="glyphicon glyphicon-trash"></span></button>');
	
	row.insertAfter($("div.j-row:last"));
	
	$("button#savebutton").hide();
	$("#querycontainer").removeClass('alert-success alert-danger');
});

$('body').on('click', 'button.delete-j-row', function() {
	$(this).closest('div.j-row').remove();
	queryBuilder.buildQuery();
});


$("table#customTargetTable").on('click', ".m-auto", function() {
		var obj = $(this);
		$("table#customTargetTable .m-notnull, table#customTargetTable .m-primary,.m-default").prop("disabled", ""); 
		if (this.checked) {
			this.checked = false;
			debugger
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


$("button#validatebutton").on('click', function() {
	var query = $("#querycontainer").val();
	debugger
	if (query.replace(/\s+/g, '') === '') {
		$("#querycontainer").addClass("border-red");
	}
	else {
		var userID = $("#userID").val();
		var industryId = $("#industryId").val();
		var packageId = $("#packageId").val();
		var isstaging = $("#isstaging").val();
		var tables = [];
		
		$("select.temp-table").each(function() {
			tables.push($(this).data('table'));
		});
		 
		showAjaxLoader(true);
		var queryDiv = $("#querycontainer").parent();
		
		if ( $(".alert-dismissible",queryDiv ).length ) {
			$(".alert-dismissible",queryDiv ).remove();
		}
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
			    		$("#querycontainer").removeClass("alert-danger").addClass('alert-success');
			    		var message = '';
			    		
			    			  message += '<div class="alert alert-success alert-dismissible" role="alert" style="margin-top:10px;">'+
			    			  globalMessage['anvizent.message.text.queryisOK']+ ' <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+
	    		  							'</div>';
			    			  queryDiv.append(message);
			    			  setTimeout(function() { $(".alert-dismissible",queryDiv).hide() .empty(); }, 10000);
			    			  $("#saveILConnectionMapping").show()
			    			  $("#checkTablePreview").show();
			    		
			    		var isFromWebServiceJoin = $("#isFromWebServiceJoin").val();
			    		var isAdvanced = $("#advanced").val();
			    		
			    		if(isFromWebServiceJoin || isAdvanced == "true" || isAdvanced == true){
			    			if(!isAdvanced && $("#targetTableNameHidden").val() == ""){
				    			$("#savebutton").show();
				    			$("#saveMapping").hide();
				    		}else{
				    			if(packageId > 0)
				    			    $("#advanced").val(true);
				    			$("#saveMapping").show();
				    			$("#savebutton").hide();
				    		}
			    		} else if ($("#advanced").val() == "true") {
			    			$("#saveCustomMapping").show();
			    		} else {
			    			$("#savebutton").show();
			    			$("#advanced").val(false);
			    		}
			    		
			    		if($("#targetTableNameHidden").val() != ""){
			    			$("#saveMapping").show();
			    			$("#savebutton").hide();
			    			if(packageId > 0)
			    				$("#advanced").val(true);
			    		}
			    		if(packageId > 0){
			    		  if($("#targetTableNameHidden").val() == "" && isAdvanced){
			    			$("#saveMapping").hide();
			    			$("#savebutton").show();
			    			$("#advanced").val(true);
			    		  }
			    		}
			    	}
			    	else {
			    		$("#savebutton, #saveMapping").hide();
			    		$("#querycontainer").addClass("alert-danger").removeClass('alert-success');
			    	}
	    		}else{
	    			$("#savebutton, #saveMapping").hide();
	    			common.showErrorAlert(result.messages[0].text);
	    		}
	    	}else{
	    		common.showErrorAlert(globalMessage['anvizent.package.label.unableToProcessYourRequest']);
	    	}
	    });
	}
});

$('select.j-type').select2({
	width: 'resolve'
})
.on("select2:select", function() {
	queryBuilder.buildQuery();
});

$("textarea#querycontainer").on('keyup' , function() {
	$(this).removeClass("alert-danger alert-success");
	$("textarea#querycontainer + .alert").text("").removeClass("alert-danger alert-success");
	$("button#savebutton, button#saveMapping").hide();
});
queryBuilder.getDataTypeList();
$("button#savebutton").on('click', function() {
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
			targetTable.append(queryBuilder.buildTargetTableRow(aliasText));
			targetTable.find(".m-datatype").change();
		});
	}
	$("#customTargetTablePopup").modal('show');
});

$("button#saveMapping").on('click', function() {
	var objColumnsList = {};
	var columnsList = [];
	var iLquery = $("#querycontainer").val().trim();
	onClickStatus = false;
	$("select.temp-table").each(function() {
		var sel = $(this),
		opts = sel.find('option:selected');
		table = sel.data('table');
		if (opts.length) {
			opts.each(function() {
				var columnName = this.value.toLowerCase();
				
				var originalColumnName = this.value; 
				
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
					aliasText = this.value;
				}
				columnsList.push(aliasText);
			});
		 }
	  });
	    var userID = $("#userID").val();
	    var ilId = $("#ilId").val();
	    var targetTableName = $("#targetTableNameHidden").val();
	    var webserviceconid = $("#webserviceconid").val();
	    
		var selectedData ={
				originalColumnNames : columnsList,
				ilId:ilId,
				tableStructure:iLquery,
				tableName: targetTableName,
				wsconnId : webserviceconid
				 
		}
	 
	   var url_getJoinWebServiceHeadersAndIlHeaders =  "/app/user/"+userID+"/package/getJoinWebServiceHeadersAndIlHeaders";
       showAjaxLoader(true);
	    var myAjax = common.postAjaxCall(url_getJoinWebServiceHeadersAndIlHeaders,'POST',selectedData,headers);
	    myAjax.done(function(result) {
	    	showAjaxLoader(false);
			if(result != null){
				   console.log("result:",result);
  		  if(result.hasMessages){ 
  			  if(result.messages[0].code == "ERROR") {
						 common.showErrorAlert(result.messages[0].text);
						 return false;
					 
  			  }else if(result.messages[0].code == "SUCCESS"){
  				$("#fileMappingWithILTable").find('th.iLName').text(result.object["originalFileName"]);
			    $("#fileMappingWithILTable").find('th.originalFileName').text("Mapped Headers");
			    console.log("result:---",result)
			    existedILColumns = result.object["iLcolumnNames"];
			    var pkgId = $("#packageId").val();
			    var pkgName = pkgId == "0" ? "standard":"queryBr";
			    fileMappingWithILTable.updateFileMappingWithILTable(result,pkgName);
  			  }
  			}	 
			} 
	    });
});

var packageId ='';
$("#saveMappingWithILForWebService").click(function(){
    var iLColumnNames = [];
    var selectedFileHeaders = [];
    var dafaultValues = [];
    packageId = $("#packageId").val();
	var industryId = $("#industryId").val();
	var advanced = $("#advanced").val();
    var userID = $("#userID").val();
    var fileType = '';
    var delimeter = '';
    var isFirstRowHasColumnNames = false; 
    var originalFileName = $("#fileMappingWithILTable").find("thead").find("th.originalFileName").text();
    var iLId = $("#ilId").val();
    var dLId = $("#dlId").val();
    var filePath = '';
    var tablerows = $("#fileMappingWithILTable").find("tbody").find("tr");
    var process = true;
    var mappingIds = [];
    $(".mappingid").each(function(){
    	mappingIds.push(this.value);
    });

    if(packageId == "0"){
        tablerows.each(function() {
        	var row = $(this);
        	var ilcolumn = row.find('.iLcolumnName').clone();
        	ilcolumn.children().remove("span");
        	
        	var iLColumn = ilcolumn.text();
        	
        	var fileHeader = row.find('.fileHeader'). val() != ''? row.find('.fileHeader option:selected').text() : " ";
        	var notNull = row.find('.notNull').attr('data-notNull');
        	var fileHeaderVal = row.find('.fileHeader'). val();
        	if(notNull == "YES"){
        		var defVal =  row.find(".default").val();
        		if(fileHeaderVal == '' && defVal == ''){
        			var id =  row.find('.fileHeader').attr('id');
        			common.clearValidations(["#"+id]);
        			common.showcustommsg("#"+id, globalMessage['anvizent.package.label.pleaseChooseMandatoryField'],"#"+id);
        			process=false;
        		}
        	}
        	var defaultValue = null;
        	if(fileHeader.trim() == ''){
        		defaultValue = row.find(".default").val() != ''? row.find('.default'). val() : " ";
        		
        		if (defaultValue !== " ") {
        			var s = standardPackage.validateDefaultValue(row.find('input.default-dtype').get(0));
        			if (process && !s) process = false;
        		}
        		
        	}
        	  iLColumnNames.push(iLColumn);
        	  selectedFileHeaders.push(fileHeader);
        	  dafaultValues.push(defaultValue);
        });
        
        if (!process) return false;
        
        	var selectData = {
        	    	packageId : packageId,
        	    	industryId : industryId,
        	    	iLId : iLId,
        	    	dLId : dLId,
        	    	fileType : fileType,
        	    	delimeter : delimeter,
        	    	isFirstRowHasColoumnNames : isFirstRowHasColumnNames,
        	    	originalFileName : originalFileName,
        	    	iLColumnNames : iLColumnNames.join(','),
        	    	selectedFileHeaders : selectedFileHeaders.join(','),
        	    	dafaultValues : dafaultValues.join(',')
        	    }
        	    showAjaxLoader(true);
        	    var url_processMappingFileWithIL = "/app/user/"+userID+"/package/saveMappingWithILForWebService";
        		 var myAjax = common.postAjaxCallObject(url_processMappingFileWithIL,'POST', selectData,headers);
        		    myAjax.done(function(result) {
        		    	showAjaxLoader(false);
        		    	  if(result != null && result.hasMessages){
        		    			  if(result.messages[0].code == "ERROR") {
        								 var  messages=[{
        									  code : result.messages[0].code,
        									  text : result.messages[0].text
        								  }];
        				    			 common.displayMessages(messages);
        			    				  return false;
        			    			  }
        		    			  if(result.messages[0].code == "SUCCESS") {
        		    				  if(result.object != null){
        			    				   var webserviceMappingHeaders = result.object;
        			    				   var webserviceconid =  $('#webserviceconid').val(); 
        			    				   var iLquery = $("#querycontainer").val().trim();
        			    				//save the mapping
        			    					var selectData={
        			    							   isMapped : true,
        			    							   isFlatFile : false,
        			    							   fileType :null,
        			    							   filePath : null,
        			    							   delimeter : null,
        			    							   isFirstRowHasColoumnNames : isFirstRowHasColumnNames,
        			    							   iLId : iLId,
        			    							   dLId : dLId,
        			    							   packageId : packageId,
        			    							   isWebservice:true,
        			    					           webserviceMappingHeaders:webserviceMappingHeaders,
        			    					           wsConId:webserviceconid,
        			    					           isHavingParentTable:false,
        			    					           joinWebService:true,
        			    					           iLquery:iLquery,
        			    					           procedureParameters:mappingIds.join(",")
        			    							   
        			    					   };
        			    					queryBuilder.saveILConnectionMapping(selectData);
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
       }
       else {
        	tablerows.each(function() {
            	var row = $(this);
            	var ilcolumn = row.find('.m-column').val();
            	//ilcolumn.children().remove("span");   	
            	var iLColumn = ilcolumn;
            	
            	var fileHeader = row.find('.fileHeader').val() != ''? row.find('.fileHeader option:selected').text() : " ";
            	var notNull = row.find('.notNull').attr('data-notNull');
            	var fileHeaderVal = row.find('.fileHeader'). val();
            		var defVal =  row.find(".default").val();
            		if(fileHeaderVal == '' && defVal == ''){
            			var id =  row.find('.fileHeader').attr('id');
            			common.clearValidations(["#"+id]);
            			common.showcustommsg("#"+id, globalMessage['anvizent.package.label.pleaseChooseMandatoryField'],"#"+id);
            			process=false;
            		}
            		if(fileHeaderVal != '' && defVal != ''){
            			var id =  row.find('.fileHeader').attr('id');
            			common.clearValidations(["#"+id]);
            			common.showcustommsg("#"+id, globalMessage['anvizent.package.message.text.pleasesettoselectfilecolumn'],"#"+id);
            			process=false;
            		}
            	var defaultValue = null;
            	if(fileHeader.trim() == ''){
            		defaultValue = row.find(".default").val() != ''? row.find('.default'). val() : " ";
            		
            		if (defaultValue !== " ") {
            			var id =  row.find('.fileHeader').attr('id');
            			common.clearValidations(["#"+id]);
            			var s = standardPackage.validateDefaultValue(row.find('input.default-dtype').get(0));
            			if (process && !s) process = false;
            		}
            		
            	}
            	  iLColumnNames.push(iLColumn);
            	  selectedFileHeaders.push(fileHeader);
            	  dafaultValues.push(defaultValue);
            });
            var mappedStatus = queryBuilder.validateTargetMappedHeaders();
            if (!process || !mappedStatus) return false;
            
            if (advanced == "true" || advanced == true) {
            	var qry = $("#querycontainer").val();
            	var generatedSql = "select ";
            	
            	var quoteChar = '`';
            	var columnLength = selectedFileHeaders.length;
            	for ( var i = 0;i < columnLength; i++ ) {
            		
            		
            		if (iLColumnNames[i] ==  selectedFileHeaders[i]) {
            			generatedSql += quoteChar + iLColumnNames[i] +quoteChar ;
            		} else {
            			if (selectedFileHeaders[i].trim() == '') {
            				var dafaultValue = dafaultValues[i].trim();
            				generatedSql += "'"+dafaultValue+"'" + " as " + quoteChar +iLColumnNames[i]+quoteChar ;
                		} else {
                			generatedSql += quoteChar + selectedFileHeaders[i] + quoteChar + " as " + quoteChar +iLColumnNames[i]+quoteChar ;
                		}
            		}
            		
            		if (i < (columnLength - 1)) {
            			generatedSql += ", ";
            		}
            		
            	}
            	generatedSql += " from ( /*index_started*/" + qry + "/*index_ended*/ ) d ";
            	var selectData = {
            			iLquery :generatedSql,
            			packageId : packageId
            	}

        	    showAjaxLoader(true);
        	    var url_processMappingFileWithIL = "/app/user/"+userID+"/package/updateTargetTableQuery";
        		 var myAjax = common.postAjaxCall(url_processMappingFileWithIL,'POST', selectData,headers);
        		 debugger
        		    myAjax.done(function(result) {
        		    	showAjaxLoader(false);
        		    	  if(result != null && result.hasMessages){
        		    			  if(result.messages[0].code == "ERROR") {
        								 var  messages=[{
        									  code : result.messages[0].code,
        									  text : result.messages[0].text
        								  }];
        				    			 common.displayMessages(messages);
        			    				  return false;
        			    			  }
        		    			  if(result.messages[0].code == "SUCCESS") {
        		    				  queryBuilder.getUpdatedTargetMappedQuery();
        		    				 
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
        }
    
});

$("#customCustomTable").on('click', function() {
	
	common.clearcustomsg("#targetTableName");
	var packageId = $("#packageId").val();
	var industryId = $("#industryId").val();
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
	
    var status = queryBuilder.validateTargetTableFields();
    if(!status){	    	
    	return false;
    }
    
    var columns = [];
    var process = true;
    var tablerows = $("#customTargetTable").find("tbody").find("tr");
    
    tablerows.each(function() {
    	debugger
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
    	
    	if(dataType == 'DECIMAL' || dataType == 'FLOAT' || dataType == 'DOUBLE') {
    		columnSize = columnSize+","+decimalPoints;
    	}
    	
    	var column = {
			 columnName : columnName,
	    	 dataType : dataType,
	    	 columnSize : columnSize,
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
				columns :columns
			}
		},
		ilConnectionMapping : {
			iLquery : $("#querycontainer").val()
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
    			window.location = adt.appContextPath+'/adt/package/customPackage/edit/'+packageId;
    			
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
(function() {
	$('.temp-table').select2({
		  tags: "true",
		  placeholder: globalMessage['anvizent.package.label.selectColumns'],
		  allowClear: true,
		});
	})();

$("#fileMappingWithILTable").on('change', 'select.fileHeader', function() {
	var obj = this;
	var ilColumnName = $($(obj).closest("tr")).find(".iLcolumnName").text().replace("*","").trim().toLowerCase();
	if ($(this).val() == ilColumnName) { 
		$(obj).removeClass("not-mapped").addClass("is-mapped");
	} else { 
		if ( $(obj).hasClass("is-mapped") ) { 
			$(obj).removeClass("is-mapped").addClass("not-mapped"); 
		} 
	}
	
	if($(this).val() != ''){
		$(this).closest('tr').find('input.default-dtype').val('');
	}
});

var onClickStatus = false;

$(document).on("change",".fileHeader",function(){
	if(packageId > 0){
		debugger
		//common.clearValidations([".fileHeader"]);
		var allColumns = $(".fileHeader").attr("data-column");
		var unMappedList = allColumns.split(",");
		var rowCount = $("#fileMappingWithILTable tbody tr").length
		selectedColumn = [];
		var dataTypeValue = $(".m-datatype").val();
		var mappedValue = $(this).val();
		var addedColumnsList = {}
		$("#fileMappingWithILTable tbody tr td.iLcolumnName").each(function(){
			addedColumnsList[$(this).find(".m-column").val()] = addedColumnsList[$(this).find(".m-column").val()];
		});
		
		//var status = validateTargetMappedHeaders($(this));
		
		$(".fileHeader").each(function(){
			if($(this).val() != ""){
				var id =  $(this).attr('id');
				common.clearValidations(["#"+id]);
				selectedColumn.push($(this).val());
			}else{
				common.clearValidations([$(this)]);
			}
		});
		var matched = 0;
		if(selectedColumn.length > 1){
			for(var i=0;i<selectedColumn.length;i++){
				if(mappedValue == selectedColumn[i]){
					matched++;
				}
			}
			if(matched > 1){
				common.showcustommsg($(this), "already mapped");
			}else{
				common.clearValidations([$(this)]);
			}
		}
		
		var addButton = "<button style='margin:5px;' class='btn btn-primary btn-sm' id='addUnmappedColumn'><span class='glyphicon glyphicon-plus'></span></button>";
		$("#addUnmappedColumn").remove();	
		$("#fileMappingWithILTable tbody tr:last").find(".delete").prepend(addButton);
	}
});

$(document).on("click","#addUnmappedColumn",function(){
	debugger
	map = [];
	onClickStatus = true;
	$("#fileMappingWithILTable tbody tr.addNewColumn").empty();
	var selectedColumn = [];
	var allColumns = $(".fileHeader").attr("data-column");
	var map = allColumns.split(",");
	var rowCount = $("#fileMappingWithILTable tbody tr").length
	var dataTypeValue = $(".m-datatype").val();
	var addedColumnsList = {}
	$(".fileHeader").each(function(){
		if($(this).val() != ""){
			selectedColumn.push($(this).val());
		}
		
	});
	
	$("#fileMappingWithILTable tbody tr td.iLcolumnName").each(function(){
		addedColumnsList[$(this).find(".m-column").val()] = addedColumnsList[$(this).find(".m-column").val()];
	});
	newDataTypes.getDataTypeList(dataTypeResult);
	for(var i=0;i<map.length;i++){
			if(!(map[i] in addedColumnsList)){
					if(!(selectedColumn[i] == map[i])){
						rowCount++;
						newColumnRow.getNewColumnRow(map[i],rowCount,dataTypeValue);
					}
			}
		}
	
	
});

$(document).on("click",".deleteUnmappedColumn",function(){
	var addButton = "<button style='margin:5px;' class='btn btn-primary btn-sm' id='addUnmappedColumn'><span class='glyphicon glyphicon-plus'></span></button>";
	var table = $(this).parents("#fileMappingWithILTable tfoot tr").remove();
	$(this).parents("#fileMappingWithILTable tbody tr").remove();
	$("#addUnmappedColumn").remove();	
	$("#fileMappingWithILTable tbody tr:last").find(".delete").prepend(addButton);
});

$("#fileMappingWithILTable tbody").on("click",".primaryKey",function(){
	debugger
	if($(this).find(".m-primary").is(":checked")){
		$(this).parents('tr').find(".m-notnull").prop("checked",true);
	}else{
		$(this).parents('tr').find(".m-notnull").prop("checked",false);
	}
})

$("#fileMappingWithILTable tbody").on("click",".autoIncrement",function(){
	debugger
	var status = false;
	var $this = $(this).parents('tr');
	if($(this).find(".m-auto").is(":checked")){
		status = true;
	}
	if(status){
		$("#fileMappingWithILTable tbody").find('.m-auto').prop("disabled",true);
		$(this).find(".m-auto").prop("disabled",false);
		$this.find(".m-notnull,.m-primary").prop("checked",true);
	}else{
		$("#fileMappingWithILTable tbody").find('.m-auto').prop("disabled",false);
		$this.find(".m-notnull,.m-primary").prop("checked",false);
	}
})
$(document).on("click","#updateSuccess",function(){
	$("#alertForTargetMappingPopUp").modal('hide');
	window.location = adt.appContextPath+'/adt/package/customPackage/edit/'+packageId;
})

}