var headers = {};
var dataTypeResult = "";
var dataTypeSelect = "";
if($('.tableBuilder-page').length) {
var tableBuilder = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		},
	aggregates : ['SUM', 'MIN', 'MAX', 'AVG', 'COUNT'],
	lengths : {
		"VARCHAR" : "255",
		"BIT" : "1",
		"DATETIME" : "19",
		"DATE" : "0",
		"DECIMAL" : ["24", "7"],
		"INT" : "11",
		"BIGINT" : "11"
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
	    			}		    			  		    			  	
		    	}
	    	});
	},

	getDataTypes : function(result){
		debugger
		for(var i=0;i<result.object.length;i++){
			 var dataTypeName = result.object[i].dataTypeName;
			 var range = result.object[i].range;
			 dataTypeSelect += "<option value='"+dataTypeName+"'>"+dataTypeName+"</option>";
		 }
	},
	
	
	validateTableName : function(){
		var process = true;
		common.clearcustomsg("#targetTableName");
		
		var targetTableName = $("#targetTableName").val();
	    
	    if(targetTableName == '') { 
	    	common.showcustommsg("#targetTableName", globalMessage['anvizent.package.label.pleaseEnterTableName']);
	    	$("#targetTableName").focus();
	    	process = false;
	    }else if(targetTableName.match(/\s/g)) {
	    	common.showcustommsg("#targetTableName", globalMessage['anvizent.package.label.tableNameShouldNotContainSpace']);
	    	$("#targetTableName").focus(); 
	    	process = false;
	    }else if(/^[a-zA-Z0-9_]*$/.test(targetTableName) == false) {	    	
	    	common.showcustommsg("#targetTableName", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters']+ "<br>"+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInTableName']);
	    	$("#targetTableName").focus(); 
	    	process = false;
	    }else if(targetTableName.match(/^(IL_|DL_|il_|dl_|iL_|Il_|dL_|Dl_)/)) {
	    	common.showcustommsg("#targetTableName",globalMessage['anvizent.package.label.tableNameShouldNotContainILsandDLsNames']);
	    	$("#targetTableName").focus(); 
	    	process = false;
	    }
	    if(!process){
	    	return false;
	    }
	    
	    /*Min max length validation*/
	    var selectors = [];
		selectors.push('#targetTableName');
	    var valid = common.validate(selectors);
	    if (!valid) {
	    	process = false;
		}
	    
	    return process;
	}
};


Array.prototype.remove = function() {
    var what, a = arguments, L = a.length, ax;
    while (L && this.length) {
        what = a[--L];
        while ((ax = this.indexOf(what)) !== -1) {
            this.splice(ax, 1);
        }
    }
    return this;
};
var myColumns = [];

$('#columns').on('change', function(evt, params) {
	
	if (params) {
		
		if ( $('select#operations').val() !== '' ) {
			if (params.selected) {
				myColumns .push(params.selected)
			}
			if (params.deselected) {
				myColumns .remove(params.deselected)
			}
		} else {
			myColumns = [];
			myColumns .push(this.value)
		}
	}
	console.log(myColumns , params);
});
 

$("#customColumnDivPopup").on('shown.bs.modal', function () {
	$("select#columns",this).chosen('destroy').chosen();
	myColumns = [];	
});


$("button#customcolumn").on('click', function() {
	common.clearValidations(["#customColumnName", "input[name='valueType']", "#valueTypediv", "#customvalue", "#dataType", "#columns","#collength","#colscale"]);
	$(".note").hide();
	$("#customColumnDivPopup").modal('show');
	$("#customColumnName, #customvalue").val('');
	$("input[name='valueType']").prop('checked', false);
	$("#savebutton").hide();
	$("select#aggregates, select#operations, select#columns").val('').trigger('change');
	
	$("select#dataType").find('option:first').prop('selected', true);
	$("select#dataType").trigger('change');
	
	var measures = $("#targetTableColumns").find('select.jcolumn-type').filter(function() {
		return this.value === "Measure";
	});
	
	var columns = $("select#columns");
	columns.find('option:not(:first)').remove();
	
	measures.each(function() {
		var m = $(this);
		var colname = $.trim(m.closest('.j-row').find('.jcolumn-name').text());
		columns.append('<option value="'+colname+'">'+colname+'</option>');
	});
	
	columns.trigger('change');
	columns.chosen('destroy').chosen();
	$("div#customvaluerow, div#dereivedvaluerow").hide();
});

$("select#aggregates").on("change", function(){
	if ( this.value === "COUNT") {
		console.log("Hello");
		$('select#operations, select#columns').val("").change();
		$('select#operations, select#columns').prop("disabled",true);
	} else {
		$('select#operations, select#columns').prop("disabled",false);
	}
	$('select#operations').select2();
});

$("div#customColumnDivPopup").on('click', "input[name='valueType']", function() {
	$(".note").hide();
	var custom = $("div#customvaluerow"), 
	derived = $("div#dereivedvaluerow");
	common.clearValidations(["#customvalue", "#valueTypediv", "#columns","#collength","#colscale"]);
	if (this.value === "Derived") {
		custom.hide();
		derived.show();
		$(".note").show();
		$("select#columns").chosen('destroy').chosen();
	}
	else {
		$('.customDefaultInfo').empty();
		if (this.value === "Custom") {
			$('.customDefaultInfo').append('<p class="help-block"><em>'+globalMessage['anvizent.package.label.forCustomFormulaEgColumn']+'</em></p>');
		}else if (this.value === "Default"){
			$('.customDefaultInfo').append('<p class="help-block"><em>'+globalMessage['anvizent.package.label.setDefaultValueforaColumn']+'</em> </p>');
		}
		custom.show();
		derived.hide();
	}
});

$("select#aggregates, select#operations, select#dataType").select2({
	width: 'resolve'
});
$("select#columns").chosen();

$("#saveCustomColumn").on('click', function() {
	//selectUniqueColumnName
	common.clearValidations(["#customColumnName", "input[name='valueType']", "#valueTypediv", "#customvalue", "#dataType", "#columns","#collength","#colscale"]);
	var columnName = $("#customColumnName").val();	
	var valueType = $("input[name='valueType']:checked").val();
	var customval = $("#customvalue").val();
	var datatype = $("#dataType").val();
	var columnLength = $("#collength").val();
	var colscale = $("#colscale").val();
	var regxforcustomcolumnbuild = '/^[a-zA-Z0-9_\d\-+*/%]+$/';
	
	var tr = $("<tr />", {
		"class" : "custom-row"
	});
		
	var process = true;
	
	if (!$.trim(columnName)) {
		common.showcustommsg("#customColumnName", globalMessage['anvizent.package.label.pleaseEnterColumnName']);
		
		process = false;
	}else if(columnName.match(/\s/g)){
		common.showcustommsg("#customColumnName", globalMessage['anvizent.package.label.columnNameShouldNotContainSpace']);
		process = false;
	}else if(/^[a-zA-Z0-9_]*$/.test(columnName) == false){
		common.showcustommsg("#customColumnName", globalMessage['anvizent.package.label.columNameContainsIllegalSpecialCharacters']+"<br>"+globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInColumnName']);
		process = false;
	}
	
	if (!valueType) {
		common.showcustommsg("#valueTypediv", globalMessage['anvizent.package.label.pleaseSelectValueType']);
		if (process) process = false;
	}
	
	if (valueType !== "Derived" && !$.trim(customval)) {
		common.showcustommsg("#customvalue", globalMessage['anvizent.package.label.pleaseEnterValue']);
		if (process) process = false;
	}
	
	if($.trim(columnLength) == ""){
		common.showcustommsg("#collength", globalMessage['anvizent.package.label.pleaseEnterValue']);
		process = false;
	}
	else{
		var isInt = common.isInteger($.trim(columnLength));
		if (!isInt) {
			common.showcustommsg("#collength", globalMessage['anvizent.package.label.pleaseEnterValidColumnLength']);
			process = false;
		}
	}
	
	if($.trim(colscale) == "" && $("#colscale").is(":visible")){
		common.showcustommsg("#colscale", globalMessage['anvizent.package.label.pleaseEnterValue']);
		process = false;
	}else if($("#colscale").is(":visible")){
		var isInt = common.isInteger($.trim(colscale));
		if (!isInt) {
			common.showcustommsg("#colscale", globalMessage['anvizent.package.label.pleaseEnterValidColumnLength']);
			process = false;
		}
	}
	
	if (valueType === "Derived") {
		var columns = $("#columns").find('option:selected');
		var showerror = false;
		
		if (columns.length === 0) 
			showerror = true;
		else if (columns.length === 1 && !columns.val()) 
			showerror = true; 
		
		if (showerror) {
			if ($("#aggregates").val() !== "COUNT") {
				common.showcustommsg("#columns", globalMessage['anvizent.package.label.pleaseSelectValues'], true);
				if (process) process = false;
			}
		}
	}
	else if(valueType === "Custom" ){
		if(!$.trim(customval)){
			common.showcustommsg("#customvalue", globalMessage['anvizent.package.label.pleaseEnterValue']);
			process = false;
		}
		else if(/^[a-zA-Z0-9_ \d\-+*/%]+$/.test(customval) == false){
			common.showcustommsg("#customvalue", "allowed symbols : +, -, *, /, %");
			process = false;
		}
	}
	else if(valueType === "Default"){
		var msg = globalMessage['anvizent.package.message.invalidData'];
		var obj = $("#customvalue");
		common.clearcustomsg(obj);
		if(customval != ''){
			if($.trim(customval).length > columnLength && datatype !== 'DECIMAL'){
				common.showcustommsg(obj, globalMessage['anvizent.package.message.constantValueLengthIsExceeded']);
				process = false;
			}
			else{
				if (datatype === 'INT' || datatype === 'BIGINT') {
					var isInt = common.isInteger(customval);
					if (!isInt) {
						common.showcustommsg(obj, msg);
						process = false;
					}
				}
				else if (datatype === 'DECIMAL') {
					
					var floatVal = customval.split(".");
					var fractionPartLength = $.trim(floatVal[0]).length;
					var decimalPartLength = $.trim(floatVal[1]).length;
					
					if(!customval.match("^([-]?\\d*\\.\\d*)$") || decimalPartLength == 0){
						common.showcustommsg(obj, msg);
						process = false;
					}else{						
						if(fractionPartLength > columnLength){
							common.showcustommsg(obj, globalMessage['anvizent.package.message.constantValueLengthIsExceeded']);
							process = false;	
						}
						if(decimalPartLength > colscale){
							common.showcustommsg(obj, globalMessage['anvizent.package.message.constantValueLengthIsExceeded']);
							process = false;	
						}
					}
				}
				else if (datatype === 'BIT') {
					if (customval !== '0' && customval !== '1') {
						common.showcustommsg(obj, msg);
						process = false;
					}
				}
				else if (datatype === 'DATETIME') {
					var regEx = /^\d{4}\-(0?[1-9]|1[012])\-(0?[1-9]|[12][0-9]|3[01])$/;
					if(!customval.match(regEx)){
						common.showcustommsg(obj, msg);
						process = false;
					}
				}
			}
		}
		else{
			common.showcustommsg(obj, globalMessage['anvizent.package.label.pleaseEnterValue']);
			process = false;
		}
	}
	$("#customColumnsTable").find(".selected-row").each(function(){
		var column = $(this).find(".sel-columnname").text();		
		if(column === columnName){
			common.showcustommsg("#customColumnName", globalMessage['anvizent.package.label.duplicateColumnName']);
			process = false;
		} 
	});
	$("#customColumnsTable").find(".custom-row").each(function(){
		var column = $(this).find(".c-columnname").text();		
		if(column === columnName){
			common.showcustommsg("#customColumnName", globalMessage['anvizent.package.label.duplicateColumnName']);
			process = false;
		} 
	});
	
	if (!process) return false;
	
	tr.append("<td class='c-columnname'>"+columnName.encodeHtml()+"</td>");
	tr.append("<td class='c-valuetype'>"+valueType.encodeHtml()+"</td>");
	
	if (valueType === "Derived") {
		var aggtype = $("#aggregates").val();
		var operations = $("#operations").val();
		var columns = myColumns;

		var val = [];
		
		val.push(aggtype);
		val.push('(');
		
		var cols = [];
		
		$.each(columns,function(i,v) {
			cols.push(v);
		});
		
		val.push( aggtype === "COUNT"? "*": cols.join(' '+operations+' '));
		
		val.push(')');
		
		tr.append("<td class='c-columnvalue' "+(aggtype? "data-agg='"+aggtype.encodeHtml()+"'": "")+">"+val.join('').encodeHtml()+"</td>");
	}
	else {
		tr.append("<td class='c-columnvalue'>"+customval.encodeHtml()+"</td>");
	}
	
	var length = $("#collength").val(),
	scale = $("#colscale").val();
	
		tr.append("<td class='c-datatype' data-length='"+length+"' "+(datatype === "DECIMAL" ? "data-scale='"+scale+"'": "") +">"+datatype.encodeHtml()+"</td>");
	
	tr.append('<td class="col-sm-1">'+ 
			'<button type="button" style="margin-left: 3px;" class="btn btn-primary btn-xs delete-row"><span class="glyphicon glyphicon-trash"></span></button></td>');
	
	$("#customColumnsTable").find('tbody').append(tr);
	$("#customColumnsTable").show();
	$("#customColumnDivPopup").modal('hide');
});

$("select#operations").on('change', function() {
	var columns = $('select#columns');
	var placeHolderObj = {};
	myColumns = [];
	columns.find('option').prop('selected', false);
	if (this.value === '') {
		columns.prepend('<option value="">'+globalMessage['anvizent.package.label.selectColumn']+'</option>');
		columns.prop('multiple', false);
	}
	else {
		placeHolderObj = {placeholder_text_multiple:"  "}
		columns.prop('multiple', true);
		if (columns.find('option:first').val() === '') {
			columns.find('option:first').remove();
		};
		
	}
	
	columns.chosen('destroy').chosen(placeHolderObj);
});

$('#customColumnsTable').on('click', 'button.delete-row', function() {
	$(this).closest('tr.custom-row').remove();
	if ($("#customColumnsTable").find('tbody').find('tr').length === 0) {
		$("#customColumnsTable").hide();
	}
});

$('#customColumnsTable').on('click', 'button.sel-delete-row', function() {
	var tr = $(this).closest('tr.selected-row');
	var id = tr.prop('id');
	$("#savebutton").hide();
	$("#targetTableColumns").find('input.jcolumn-check:checked').each(function() {
			if (id === $.trim($(this).closest('.j-row').find('.jcolumn-name').text())) {
			this.checked = false;
			return false;
		}
	});
	
	tr.remove();
	
	if ($("#customColumnsTable").find('tbody').find('tr').length === 0) {
		$("#customColumnsTable").hide();
	}
});

$(document).on("click",".selectAll",function(){
	var isChecked = this.checked;
	$(this).parents('.table').find("input[type='checkbox']").each(function(){
		if (this.checked != isChecked ) {
			this.checked = !isChecked;
			$(this).trigger("click");
		}
	});
	
	
	
});

$("#targetTableColumns").on('click', 'input.jcolumn-check', function() {
	$("#savebutton").hide();
	common.clearcustomsg("#duplicateColValidation");	
	var selcol = $(this);
	var row = selcol.closest('.j-row');
	var colname = $.trim(row.find('.jcolumn-name').text());
	var process = true; 
	$("#duplicateColValidation").show();
	if($('.jcolumn-check').length == $('.jcolumn-check:checked').length){
		$(this).parents('.table').find(".selectAll").prop("checked",true);
	}else{
		$(this).parents('.table').find(".selectAll").prop("checked",false);
	}
	$("#customColumnsTable").find(".custom-row, .selected-row").each(function(){
		var column = $(this).find(".c-columnname").text().toLowerCase();		
		if(column === colname.toLowerCase()){
			selcol.prop("checked",false);
			common.showcustommsg("#duplicateColValidation", globalMessage['anvizent.package.label.duplicateColumnName']+" "+colname);
			$("#duplicateColValidation + span").stop().fadeIn(1).fadeOut(7000);
			process = false;
		} 
	});
	if(process){
		var table = $("#customColumnsTable").find('tbody');
		if (this.checked) {
			var datatype =  row.find('.jcolumn-dtype').text();
			var tr = $("<tr />", {
				"class" : "selected-row",
				"id" : colname
			});
			
			tr.append("<td class='sel-columnname'>"+colname+"</td>");
			tr.append("<td class='sel-valuetype'>"+globalMessage['anvizent.package.label.Selected']+"</td>");
			tr.append("<td class='sel-columnvalue'>"+colname+"</td>");
			tr.append("<td class='sel-datatype'>"+datatype+"</td>");
			tr.append('<td class="col-sm-1"><button type="button" style="margin-left: 3px;" class="btn btn-primary btn-xs sel-delete-row"><span class="glyphicon glyphicon-trash"></span></button></td>');
			
			if (table.find('tr.custom-row').length>0) {
				tr.insertBefore(table.find('tr.custom-row:first'));
			}
			else {
				table.append(tr);
			}
			
			table.parent().show();
		}
		else {
			$("#customColumnsTable").find("tr#"+colname).remove();
			if (!table.find('tr').length) {
				table.parent().hide();
			}
		
		}
	}
});

$("button#validatebutton").on('click', function() {
	
	var tablename = $("#tablename").val();
	tablename = "`"+tablename+"`";
	var selCols = $("#targetTableColumns").find("input.jcolumn-check:checked");
	var customrows = $("#targetTableColumns").find("tr.custom-row");
	
	common.clearcustomsg("#targetTableName");
	
	if (selCols.length === 0) {
		$("#messagePopUp").modal('show');
		$("#popUpMessage").text(globalMessage['anvizent.package.label.selectAtleastOneColumnFromTargetTable']).addClass("alert-danger").removeClass('alert-success');
		return false;
	}
	
	var process = tableBuilder.validateTableName();
	if(!process){
		return false;
	}
	
	var hasAgg = customrows.find('[data-agg]').length>0;
	var query = [];
	var colsadded = false;
	
	query.push('SELECT ');
	
	var len = selCols.length;
	
	var cols= [];
	
	selCols.each(function(index) {
		var selcol = $(this);
		var colname = $.trim(selcol.closest('.j-row').find('.jcolumn-name').text());
		colname = "`" + colname + "`";
		cols.push(colname);
		query.push(colname);
		
		if (!colsadded) colsadded = true;
		
		if (index < len-1)
			query.push(', ');
		
	});
	
	var clen = customrows.length;
	if (clen>0) {
		if (colsadded) query.push(', ');
		customrows.each(function(index) {
			var row = $(this);
			
			var colname = $.trim(row.find('td.c-columnname').text());
			var colval = $.trim(row.find('td.c-columnvalue').text());
			var valtype = $.trim(row.find('td.c-valuetype').text());
			var datatype = $.trim(row.find('td.c-datatype').text());
			
			if (valtype !== "Default") {
				if (valtype === "Custom") {
					var aggs = tableBuilder.aggregates;
					
					for (var i=0; i < aggs.length; i++ ) {
						if (colval.toUpperCase().indexOf(aggs[i]+'(') !== -1) {
							hasAgg = true;
							break;
						}
					}
				}
				
				query.push(colval);
			}
			else {
				if (datatype === 'VARCHAR' || datatype === 'DATETIME' || datatype === 'BIT') {
					query.push("'", colval, "'");
				}
				else {
					query.push(colval);
				}
			}
			
			query.push(' AS ', colname);
			
			if (index < clen-1) 
				query.push(', ');
			
		});
	}
	
	query.push(' FROM ', tablename);
	
	
	if (hasAgg) {
		query.push(' GROUP BY ');
		
		$.each(cols, function(index, col) {
			query.push(col);
			if (index < len-1)
				query.push(', ');
		});
	}
	
	$("#query").val(query.join(''));
	
	var userID = $("#userID").val();
	var industryId = $("#industryId").val();
	var packageId = $("#packageId").val();

	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	headers[header] = token;
	
	showAjaxLoader(true);
	
	var url = "/app/user/"+userID+"/package/validateCustomTempTablesQuery/"+industryId+"/"+packageId;
    var myAjax = common.postAjaxCallObject(url,'POST', {"queryvalue" : $("#query").val(), "tables" :  tablename, "isstaging": false },headers);
	
    myAjax.done(function(result) {
    	showAjaxLoader(false);
    	var status = result.object;
    	if(result != null && result.hasMessages){
    		if(result.messages[0].code == "SUCCESS"){
		    	$("#messagePopUp").modal('show');
		    	
		    	if (status) {
		    		$("#popUpMessage").text(globalMessage['anvizent.package.label.successfullyValidated']).addClass('alert-success').removeClass('alert-danger');
		    		$("#savebutton").show();
		    	}
		    	else {
		    		$("#popUpMessage").text(globalMessage['anvizent.package.label.errorWhileValidatingSelectedColumns']).addClass("alert-danger").removeClass('alert-success');
		    		$("#savebutton").hide();
		    	}
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

$("#savebutton").on('click', function(){
	
	var packageId = $("#packageId").val();
	var industryId = $("#industryId").val();
	var userID = $("#userID").val();
	var tablename =  $("#tablename").val();
	var tableid =  $("#tableid").val();
	$(".selectAll").prop("checked",false);
	common.clearcustomsg("#targetTableName");
	
	var process = tableBuilder.validateTableName();
	if(!process){
		return false;
	}
		
	var targetTableName = $("#targetTableName").val();
    	
    var customrows = $("#targetTableColumns").find("tr.custom-row");
    var query = $("#query").val();
    var clen = customrows.length;
    var ccols=[];
	if (clen>0) {
		customrows.each(function(index) {
			var row = $(this),
			colname = $.trim(row.find('td.c-columnname').text()),
			colval = $.trim(row.find('td.c-columnvalue').text()),
			valtype = $.trim(row.find('td.c-valuetype').text()),
			datatypetd = row.find('td.c-datatype'),
			datatype = $.trim(datatypetd.text()), 
			length = datatypetd.data('length') || "",
			scale = datatypetd.data('scale') || "0";
			
			var col = colname+"::"+datatype;
			
			if (datatype === "DECIMAL") {
				col += "^"+length+"~"+scale;
			}
			else {
				col += "^"+length;
			}
			
			ccols.push(col);
		});
	}
	
	showAjaxLoader(true);
	
	var url = "/app/user/"+userID+"/package/saveCustomTargetDerivativeTable/"+industryId+"/"+packageId;
    var myAjax = common.postAjaxCallObject(url,'POST', {"queryvalue" : query, "table" :  tablename, "targetTable": targetTableName, "tableid": tableid, "ccols" : ccols.join("##") },headers);
	
    myAjax.done(function(result) {
    	var status = result.object;
    	showAjaxLoader(false);
    	
    	$("#savebutton").hide();
    	
    	

        	if (result != null && result.hasMessages) {
        		var messages = result.messages;
        		var msg = messages[0];
        		if (msg.code === "SUCCESS") {
        	    		$("#targetTablePopup").modal('show');
        	    		$("#customColumnsTable").hide().find('tbody').find('tr').remove();
        	    		$("#targetTableName").val('');
        	    		$("#targetTableColumns").find("input.jcolumn-check:checked").prop('checked', false);
        	    		
        	    		$("#targetTableMessage").text(globalMessage['anvizent.package.label.tableCreated']).addClass('alert-success').removeClass('alert-danger');
        		} else {
        			common.showcustommsg("#targetTableName", msg.text);
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
});
tableBuilder.getDataTypeList();
$("select#dataType").on('change', function() {
	common.clearValidations(["#collength","#colscale"]);
	$("#collength").prop("disabled",false);
	
	for(var i=0;i<dataTypeResult.object.length;i++){
		 var dataTypeName = dataTypeResult.object[i].dataTypeName;
		 var range = dataTypeResult.object[i].range;
		 
		 if (this.value === "DECIMAL" || this.value === "FLOAT" || this.value === "DOUBLE") {
			 	var size = range.split(",");
				//var f = tableBuilder.lengths[this.value];
				$("#collength").val(size[0]);
				$("#colscale").val(size[1]).show();
			}
			else {
				$("#collength").val(range);
				$("#colscale").hide().val('');
				if(this.value === "BIT" || this.value === "DATETIME"){
					$("#collength").prop("disabled",true);
				}
			}
	 }
	
	
});
$("#targetTableName").on("keyup",function(){
	$("#savebutton").hide();
});
}