var userID = $("#userID").val();

var ilInfoMap = {};

var columnNames = [];
var columnSize = {};
var columnDataTypes = {};
var xrefAutoMergeParamList = [];

var xrefIlTableName;
var tableName;

var autoincrementColumnName;
var xRefKeyColumnName;


var ilHeaders = [];
var ilFirstRecordData = {};

var previewTable = null;

var crossRefSpitTable = null;
var crossRefMergeTable = null;

/*bulk upload variables */
var uploadedfilepath;
var isCreationMode = false;
var executionType = null;
var xrefObject = null;
var mergeType = null;

function xrefAutoMergeObj(conditionName, mergeParams){
	this.conditionName = conditionName;
	this.mergeParams = mergeParams;
}

var crossReference = {
	initialPage : function() {
		this.getIlsList();
		/*if ($("#manualMergerIlColumnValue").length) {
			$("#manualMergerIlColumnValue").multipleSelect({filter : true, single: true, placeholder : 'select Value'}); 
		}*/
		$('.filterByDiv').removeClass('hidden');
		if ($("#referenceField").length) {
			$("#referenceField").multipleSelect({filter : true, placeholder : globalMessage['anvizent.package.label.selectColumnNames']}); 
			$("#xReferenceField").multipleSelect({filter : true, placeholder : globalMessage['anvizent.package.label.selectColumnNames']}); 
			$("#autoMergeColumns").multipleSelect({filter : true, placeholder : globalMessage['anvizent.package.label.selectColumnNames']}); 
		}
		
		setTimeout(function() { $("#pageErrors").hide().empty(); }, 20000);
		previewTable = $(".tablePreview").DataTable({
	        "order": [],"language": {
                "url": selectedLocalePath
            }
	    });
		var crossReferenceTbl = $("#viewCrossReferenceTable").DataTable({"language": {
			"order": [[ 0, "desc" ]],
            "url": selectedLocalePath
        }});
		
		$("#crossrefTable").DataTable({
			"order": [[0, "desc" ]],"language": {
	            "url": selectedLocalePath
	        }
		});

		/*var crossRefSpitTable = $("#crossRefSpitTable").DataTable({"language": {
		                "url": selectedLocalePath
		            }});
		var crossRefMergeTable = $("#crossRefMergeTable").DataTable({"language": {
		                "url": selectedLocalePath
		            }});*/
		
		crossRefSpitTable = $("#crossRefSpitTable").DataTable();
		crossRefMergeTable = $("#crossRefMergeTable").DataTable();
		
		$("#crossRefEditTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
		$("#ilId").select2({               
            allowClear: true,
            theme: "classic"
		}); 
		$("#ilColumnName").select2({               
            allowClear: true,
            theme: "classic"
		});
		
		if ($("[name='crossReferenceOption']").length && $("[name='crossReferenceOption']:checked").val() == "automerge" ){
			$("#ilMergeColumns").multipleSelect({filter : true, single: false, placeholder : globalMessage['anvizent.package.label.selectColumn']});
		}
		
		$('.datepicker').datepicker({
			dateFormat : 'yy-mm-dd',
			defaultDate : new Date(),
			minDate : new Date(),
			setDate : new Date(),
			changeMonth : true,
			changeYear : true,
			yearRange : "0:+50",
			numberOfMonths : 1
		});
		$(".datepicker").datepicker( "setDate" , "2018-01-20" );
		$("#xReferenceOption3").addClass('hidden');

	},
	getIlsList : function() {
		var url = "/app/user/" + userID + "/crossreference/ilList";
		showAjaxLoader(true);
		var myAjax = common.loadAjaxCall(url, 'GET');
		myAjax.done(function(result) {
			showAjaxLoader(false);
			if (common.validateDataResponse(result)) {
				var ilIdSelectBox = $("#ilId");
				$.each(result.object, function(i,obj) {
					ilInfoMap[obj.iL_id] = obj;
					ilIdSelectBox.append($("<option>", {
						text : obj.iL_name,
						value : obj.iL_id
					}));
				});
			}
		});
	},
	
	getColumnsInfo : function(ilId, obj, isCreationMode) {
		var crossReferenceObj = this;
		if($.isEmptyObject(ilInfoMap)){
			var url = "/app/user/" + userID + "/crossreference/ilList";
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					var ilIdSelectBox = $("#ilId");
					$.each(result.object, function(i,obj) {
						ilInfoMap[obj.iL_id] = obj;
						ilIdSelectBox.append($("<option>", {
							text : obj.iL_name,
							value : obj.iL_id
						}));
					});
					$("#viewResultsBtn").prop('href', $("#viewResultsUrl").val() + ilId + "/" + ilInfoMap[ilId].iL_table_name);
					var url = "/app/user/" + userID + "/crossreference/columnInfo?tablename=" + ilInfoMap[ilId].iL_table_name;
					showAjaxLoader(true);
					var myAjax = common.loadAjaxCall(url, 'GET');
					myAjax.done(function(result) {
						showAjaxLoader(false);
						if (common.validateDataResponse(result)) {
							columnNames = result.object.columnsNames;
							columnSize = result.object.columnSize;
							columnDataTypes = result.object.columnDataTypes;
							if(!isCreationMode){
								crossReferenceObj.populateCrossReferenceInfo(obj);
							}
						}
					});
				}
			});
		}
		else{
			$("#viewResultsBtn").prop('href', $("#viewResultsUrl").val() + ilId + "/" + ilInfoMap[ilId].iL_table_name);
			var url = "/app/user/" + userID + "/crossreference/columnInfo?tablename=" + ilInfoMap[ilId].iL_table_name;
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					columnNames = result.object.columnsNames;
					columnSize = result.object.columnSize;
					columnDataTypes = result.object.columnDataTypes;
					if(!isCreationMode){
						crossReferenceObj.populateCrossReferenceInfo(obj);
					}
				}
			});
		}
	},
	   
	
	validateCommonMergerFields : function() {
		var conditionName = $("#conditionName").val();
	    var applicableDate = $("#applicableDate").val();
	    var regex = /^([a-zA-Z0-9_.+-])+$/;
	    common.clearValidations(["#conditionName","#applicableDate"]);
	    
	    if ( conditionName.trim().length == 0 ) {
			common.showcustommsg("#conditionName", globalMessage['anvizent.package.label.enterConditionName']);
			$("html, body").animate({ scrollTop: 0 }, "slow");
	        return false;
		}else if(! regex.test(conditionName) ){
			common.showcustommsg("#conditionName", globalMessage['anvizent.package.message.specialCharactersandOnlyAllowed']);
			$("html, body").animate({ scrollTop: 0 }, "slow");
	        return false;
		}
	    if ( applicableDate.trim().length == 0 ) {
			common.showcustommsg("#applicableDate", globalMessage['anvizent.package.label.selectApplicableFromDate']);
			$("html, body").animate({ scrollTop: 0 }, "slow");
	        return false;
		}
	    return true
	},
	getRecordsForManualMerge : function() {
		var ilId = $("#ilId").val();
		var $thisForm = this;
		var selectedObject = {
				ilId : $("#ilId").val(),
				crossReferenceOption : $("[name='crossReferenceOption']:checked").val(),
				typeOfMerge : $("[name='crossReferenceOption']:checked").val(),
				ilColumnName : $("[name='manualMergeIlColumnName']").val(),
				ilColumnValue : $("[name='manualMergerIlColumnValue']").val(),
		};
		var url = "/app/user/" + userID + "/crossreference/records";
		showAjaxLoader(true);
		
		var myAjax = common.postAjaxCall(url, 'POST',selectedObject);
		myAjax.done(function(result) {
			showAjaxLoader(false);
			if (common.validateDataResponse(result)) {
				autoincrementColumnName = result.object.autoincrement_column_name;
				xRefKeyColumnName = result.object.xRefKeyColumnName;
				$thisForm.showObjects(".manual-merge-input-screen-div");
				ilHeaders = result.object.headersList;
				ilFirstRecordData = result.object.valuesList[0];
				
				/*x-ref records */
				try {
					crossRefMergeTable.destroy();
				} catch (e) {
					console.log(e);
				}
				$(".newXref").click();
				var $xRefTHeader = $("#crossRefMergeTable thead");
				$xRefTHeader.empty();
				var $trHeader = $("<tr>");
				$trHeader.append($("<td>").text(""));
				$.each(result.object.headersList ,function(i, columnName){
					$trHeader.append($("<td>").text(columnName));
				});
				$xRefTHeader.append($trHeader);
				var $xRefTBody = $("#crossRefMergeTable tbody");
				$xRefTBody.empty();
				$.each(result.object.valuesList ,function(i, rowData){
					var $tr = $("<tr>");
					$.each(result.object.headersList ,function(j, columnName){
						var $td = $("<td>");
						if (j == 0) {
							if (rowData[xRefKeyColumnName]) {
								$td.addClass("remerge-row");
								var btn$ = $("<button>",{type:'button',class:'btn btn-primaary remerge',
									value:rowData[autoincrementColumnName],
										 'data-xrefid':rowData[xRefKeyColumnName]
									 });
								btn$.text(globalMessage['anvizent.package.label.unMerge']);
								$td.append(btn$);
								$td.append($("<input>",{type:'checkbox',id:'ilMergeColumns', class:'ilMergeColumns hidden', name:'ilMergeColumns',value:rowData[autoincrementColumnName] }));
							} else {
								$td.append($("<input>",{type:'checkbox',id:'ilMergeColumns', class:'ilMergeColumns', name:'ilMergeColumns',value:rowData[autoincrementColumnName] }));
							}
							$tr.append($td);
							$td = $("<td>");
							$td.text(rowData[columnName]);
						} else {
							$td.text(rowData[columnName]);
						}
						$tr.append($td);
					});
					$xRefTBody.append($tr);
				});
				
				crossRefMergeTable = $("#crossRefMergeTable").DataTable();
				
				/* end of x-ref records */
				
			}
		});
	},
	getRecordsForSplit : function() {
		var ilId = $("#ilId").val();
		var $thisForm = this;
		var selectedObject = {
				ilId : $("#ilId").val(),
				crossReferenceOption : $("[name='crossReferenceOption']:checked").val(),
				ilColumnName : $("[name='splitIlColumnName']").val(),
				ilColumnValue : $("[name='splitIlColumnValue']").val(),
		};
		var url = "/app/user/" + userID + "/crossreference/records";
		showAjaxLoader(true);
		
		var myAjax = common.postAjaxCall(url, 'POST',selectedObject);
		myAjax.done(function(result) {
			showAjaxLoader(false);
			if (common.validateDataResponse(result)) {
				autoincrementColumnName = result.object.autoincrement_column_name;
				xRefKeyColumnName = result.object.xRefKeyColumnName;
				$thisForm.showObjects(".split-input-screen-div");
				ilHeaders = result.object.headersList;
				ilFirstRecordData = result.object.valuesList[0];
				
				/*x-ref records */
				try {
					crossRefSpitTable.destroy();
				} catch (e) {
					console.log(e);
				}
				var $xRefTHeader = $("#crossRefSpitTable thead");
				$xRefTHeader.empty();
				var $trHeader = $("<tr>");
				// <input type="checkbox" id="checkAll">
				$trHeader.append($("<td>").text(""));
				$.each(result.object.headersList ,function(i, columnName){
					$trHeader.append($("<td>").text(columnName));
				});
				$xRefTHeader.append($trHeader);
				var $xRefTBody = $("#crossRefSpitTable tbody");
				$xRefTBody.empty();
				$.each(result.object.valuesList ,function(i, rowData){
					var $tr = $("<tr>");
					$.each(result.object.headersList ,function(j, columnName){
						var $td = $("<td>");
						if (j == 0) {
							$td.append($("<input>",{type:'checkbox',id:'ilMergeColumns', class:'ilMergeColumns', name:'ilMergeColumns',value:rowData[autoincrementColumnName] }));
							$tr.append($td);
							$td = $("<td>");
							$td.text(rowData[columnName]);
						} else {
							$td.text(rowData[columnName]);
						}
						$tr.append($td);
					});
					$xRefTBody.append($tr);
				});
				
				crossRefSpitTable = $("#crossRefSpitTable").DataTable();
				
				/* end of x-ref records */
				
			}
		});
	},
	getExistingRecordsForManualMerge : function() {
		var ilId = $("#ilId").val();
		var $thisForm = this;
		var selectedObject = {
				ilId : ilId,
				crossReferenceOption : $("[name='crossReferenceOption']:checked").val(),
				typeOfMerge : $("[name='crossReferenceOption']:checked").val(),
				ilColumnName : $("[name='manualMergeIlColumnName']").val(),
				existingXrefValue : $("#existingXrefValue").val(),
		};
		var url = "/app/user/" + userID + "/crossreference/existingXrefRecord?xrefIlTableName=" + ilInfoMap[ilId].xref_il_table_name;
		if($("#existingXrefValue").val() != undefined || $("#existingXrefValue").val() != null){
			showAjaxLoader(true);
			var myAjax = common.postAjaxCall(url, 'POST',selectedObject);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					$thisForm.showObjects(".existing-x-ref-values-div");
					/*existing x-ref records */
					var $xRefTHeader = $("#existingXrefTbl thead");
					$xRefTHeader.empty();
					var $trHeader = $("<tr>");
					$.each(result.object.headers ,function(i, columnName){
						$trHeader.append($("<td>").text(columnName));
					});
					$xRefTHeader.append($trHeader);
					var $xRefTBody = $("#existingXrefTbl tbody");
					$xRefTBody.empty();
					if(result.object.listOfXrefData.length == 0){
						var $trMsg = $("<tr>");
						$trMsg.append($("<td colspan = "+result.object.headers.length+" style='color: #ff0b00'>").text('Please select "Existing Cross Reference"'));
						$xRefTBody.append($trMsg);
					}
					$.each(result.object.listOfXrefData ,function(i, rowData){
						var $tr = $("<tr>");
						$.each(result.object.headers ,function(j, columnName){
							var $td = $("<td>");
							if (j == 0) {
									var radio$ = $("<input>",{type:'radio',
											name:'existingxrefValueKey',
											  value:rowData[xRefKeyColumnName]
										 });
									
									$td.append(radio$);
							} else {
								$td.text(rowData[columnName]);
							}
							$tr.append($td);
						});
						$xRefTBody.append($tr);
					});
					
					/* end of x-ref records */
					
				}
			});
		}
	},
	populateNewXrefHeadersandData: function() {
		var $thisForm = this;
		/*new x-ref record */
		$thisForm.showObjects(".new-x-ref-values-div");
		var $newXRefTHeader = $(".new-x-ref-values-table thead tr");
		$newXRefTHeader.empty();
		$.each(ilHeaders ,function(i, columnName){
			$newXRefTHeader.append($("<td>").text(columnName));
		});
		var $newXRefTBody = $(".new-x-ref-values-table tbody");
		$newXRefTBody.empty();
		var $tr = $("<tr>");
		$.each(ilHeaders ,function(j, columnName){
			var $td = $("<td>");
			if (columnName == "DataSource_Id") {
				$td.append($("<input>",{type:'hidden',class:'xrefcolumns_'+columnName,name:columnName,value:ilFirstRecordData[columnName] }));
				$td.append($("<span>").text(ilFirstRecordData[columnName]));
			} else {
				$td.append($("<input>",{type:'text',class:'xrefcolumns_'+columnName,name:columnName,value:ilFirstRecordData[columnName] }));
			}
			$tr.append($td);
		});
		$newXRefTBody.append($tr);
		
		/*end of new x-ref record */
		
	},
	populatereferencefieldsselectBoxes : function() {
		this.populateColumnNames("#referenceField,#xReferenceField",true);
	},
	populateManualMergeComboBox : function() {
		this.populateColumnNames("[name='manualMergeIlColumnName']",false,true);
	},
	populateSplitComboBox : function() {
		this.populateColumnNames("[name='splitIlColumnName']",false,true);
	},
	populateAutoMergeComboBox : function() {
		this.populateColumnNames("[name='autoMergeColumns']",true);
	},
	cleanUpBulkMerge : function(isConditionNameCleanupRequired) {
		if (isConditionNameCleanupRequired) {
			this.cleanUpCommonFields();
		}
		this.hideBulkMergeButtonsGroup();
		this.hideObjects(".file-headers-mapping-div");
		this.showObjects("#uploadBulkMergeFileBtn");
		$("#referenceField,#xReferenceField").val([]);
		$("#fileUpload").val("");
		$("#fileUpload").prop("disabled",false);
		$("#referenceField").multipleSelect("enable");
		$("#xReferenceField").multipleSelect("enable");
		$("#xrefId").val("");
         executionType = null;
		  
		this.populatereferencefieldsselectBoxes();
	},
	cleanUpCommonFields : function() {
		$("#conditionName").val("");
		$("#xrefId").val("");
		executionType = null;
	    //$("#applicableDate").val("");
	},
	cleanUpAutoMerge : function(isConditionNameCleanupRequired) {
		if (isConditionNameCleanupRequired) {
			this.cleanUpCommonFields();
		}
		$("[name='autoMergeColumns']").val([]);
		this.populateAutoMergeComboBox();
	},
	cleanUpManualMerge : function(isConditionNameCleanupRequired) {
		if (isConditionNameCleanupRequired) {
			this.cleanUpCommonFields();
		}
		try {
			$("#manualMergeIlColumnName").select2("val",0);
			$("#ilColumnValueAjax").select2("val",[]);
		} catch (e) {
			
		}
		$("[name='newOrExistingXref']").prop('checked', false);
		
		this.hideObjects(".new-x-ref-values-div");
		var $newXRefTHeader = $(".new-x-ref-values-table thead tr");
		$newXRefTHeader.empty();
		
		var $newXRefTBody = $(".new-x-ref-values-table tbody");
		$newXRefTBody.empty();
		
		/*try {
			crossRefMergeTable.destroy();
		} catch (e) {
			console.log(e);
		}
		var $xRefTHeader = $("#crossRefMergeTable thead");
		$xRefTHeader.empty();
		var $xRefTBody = $("#crossRefMergeTable tbody");
		$xRefTBody.empty();*/
		
		this.hideObjects(".manual-merge-input-screen-div");
		
	},
	cleanUpSplit : function() {
		try {
			$("#splitIlColumnName").select2("val",0);
			$("#splitIlColumnValue").select2("val",[]);
			$("#xrefId").val("");
			executionType = null;
		} catch (e) {
		}
		this.hideObjects(".split-input-screen-div");
		
	},
	populateColumnNames : function(selector,isMultiselect,isSelect2) {
		var _selector = $(selector);
		_selector.empty();
		$.each(columnNames, function(i, colName){
			_selector.append($("<option>", {
				text : colName,
				value : colName
			}));
		});
		if(isMultiselect) {
			$(selector).multipleSelect('refresh');
		}
		if(isSelect2) {
			_selector.prepend($("<option>", {
				text : globalMessage['anvizent.package.label.selectColumn'],
				value : "0"
			}));
			$(selector).select2();
		}
		
	},
	
	showObjects: function(selectors) {
		$(selectors).removeClass("hidden");
	},
	hideObjects: function(selectors) {
		$(selectors).addClass("hidden");
	},
	hideMergeAndSplitDivs: function() {
		this.hideObjects(".first-level-div")
	},
	hideMergeDivs: function() {
		this.hideObjects(".second-level-div")
	},
	hideBulkMergeButtonsGroup : function() {
		this.hideObjects(".bulk-merger-buttons-group")
	},
	resetXRefForm : function() {
		this.hideMergeAndSplitDivs();
	},
	resetRadioButtons : function(selector) {
		$(selector).prop('checked',false);
	},
	
	getDupliacteColumns : function(XrefColumnObject){
		var objectList = [];
		$.each(XrefColumnObject,function(i,obj){
			objectList.push(obj.columnName);
			
		});
		return objectList;
	},
	
	doFormSubmit: function(obj) {
		showAjaxLoader(true);  
		obj.form.submit(); 
	},
	validateDefaultValue : function(obj) {
		var msg = globalMessage['anvizent.package.message.invalidData'];
		var status = true;
		if (obj) {
			var val = $(obj).val();
			common.clearcustomsg(obj);
				var datatype = $(obj).attr("data-coldatatype");
				if (datatype === 'INT' || datatype === 'BIGINT' || datatype== 'INT UNSIGNED') {
					var isInt = common.isInteger(val);
					if (!isInt) {
						status = false;
					}
				}
				else if (datatype === 'FLOAT' || datatype === 'DECIMAL' || datatype === 'DECIMAL UNSIGNED') {
					if(!val.match("^([-]?\\d*\\.\\d*)$")){
						status = false;
					}
				}
				else if (datatype === 'BIT') {
					if (val !== '0' && val !== '1') {
						status = false;
					}
				}
				else if (datatype === 'DATETIME') {
					var regEx = /^\d{4}\-(0?[1-9]|1[012])\-(0?[1-9]|[12][0-9]|3[01])$/;
					if(!val.match(regEx)){
						status = false;
					}
				}
		}
		/*if(!status){
			common.showcustommsg("#ilXreferenceValue", msg, "#ilXreferenceValue");
		}*/
		return status;
	},
	showColumnValues : function(result){
		var viewColumnsTableObj = $("#viewColumnsTable").empty();
		var theadTr = $("<tr>");
		var tbodyTr = $("<tr>");
		viewColumnsTableObj.append(theadTr)
		viewColumnsTableObj.append(tbodyTr)
		if(result.length > 0){
			$.each($.parseJSON(result),function(key,value){
				theadTr.append($('<th>').append(key));
				tbodyTr.append($('<th>').append(value));
			});
		}
		$("#viewColumnValuesPopUp").modal('show');
	},showAutoMergeXrefQueries : function(result){
		var viewColumnsTableObj = $("#viewColumnsTable").empty();
		var theadTr = $("<tr>");
		var tbodyTr = $("<tr>");
		viewColumnsTableObj.append(theadTr)
		viewColumnsTableObj.append(tbodyTr);
		theadTr.append($('<th>').append("Queries"));
		tbodyTr.append($('<td>').append($("<pre>").css({'max-height': '300px', 'max-width': '950px','font-family': 'calibri'}).append(result)));
		$("#viewColumnValuesPopUp").modal('show');
	},
	showUnMergeRowWithColumns : function(result){
		console.log("result -- >",result);
		 var unMergeTableColumnsRowTable = $("#viewUnMergeTableColumnsRowTable");		    	
		 var headersList = result.headersList;
		 var valuesList = result.valuesList;
		 var unMergeTableColumnsRow  ='';
     	  if(headersList != null && headersList.length > 0){
     		 unMergeTableColumnsRow+='<thead><tr>';
	    	  $.each(headersList, function (key, value) {
	    		  unMergeTableColumnsRow += '<th>'+value+'</th>';
	    		  });
	    	  unMergeTableColumnsRow+='</tr></thead>'; 
	    	  unMergeTableColumnsRow+='<tbody><tr>';
	    	  $.each(valuesList, function (key, value) {
	    		  $.each(headersList, function (index,columnName) {
	    			  unMergeTableColumnsRow += '<td>'+value[columnName]+'</td>';
		    		  });
	    		  });
	    	  unMergeTableColumnsRow+='</tr></tbody>'; 
	    	  
	    	  unMergeTableColumnsRowTable.empty();
	    	  unMergeTableColumnsRowTable.append(unMergeTableColumnsRow);
	    	  }
	    	  else{
	    		  unMergeTableColumnsRowTable.empty();
	    		  unMergeTableColumnsRowTable.append(globalMessage['anvizent.package.label.noRecordsAvailableInTable']);
	    	  } 
    	$("#unMergePopUpAlert").modal('show');
	},
	validateManualMergeInputFields : function(){
		var status = false;
		common.clearValidations(["#manualMergeIlColumnName+span","#ilColumnValueAjax+span"]);
		var manualMergeIlColumnName = $("#manualMergeIlColumnName").val();
		var ilColumnValues = $("#ilColumnValueAjax").val();
		
		if ( !manualMergeIlColumnName || manualMergeIlColumnName == "0" ) {
			common.showcustommsg("#manualMergeIlColumnName+span", globalMessage['anvizent.package.label.pleaseSelectColumnName']);
		} else if ( !ilColumnValues || ilColumnValues.length == 0) {
			common.showcustommsg("#ilColumnValueAjax+span", globalMessage['anvizent.package.label.pleaseSelectValues']);
		} else {
			status = true;
		}
		return status;
	},
	validateSplitInputFields : function(){
		var status = false;
		common.clearValidations(["#splitIlColumnName+span","#splitIlColumnValue+span"]);
		var manualMergeIlColumnName = $("#splitIlColumnName").val();
		var ilColumnValues = $("#splitIlColumnValue").val();
		
		if ( !manualMergeIlColumnName || manualMergeIlColumnName == "0" ) {
			common.showcustommsg("#splitIlColumnName+span", globalMessage['anvizent.package.label.pleaseSelectColumnName']);
		} else if ( !ilColumnValues) {
			common.showcustommsg("#splitIlColumnValue+span", globalMessage['anvizent.package.label.pleaseSelectValues']);
		} else {
			status = true;
		}
		return status;
	},
	
	getCrossReferenceList : function() {
		var crossReferenceObj = this;
		var filterBy = $("#filterPackages").val();
		var url = "/app/user/" + userID + "/crossreference";
		showAjaxLoader(true);
		var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
			showAjaxLoader(false);
			if (common.validateDataResponse(result)) {
					crossReferenceObj.populateCrossReferenceList(result.object,filterBy);
				}
			});
	 },
	 
	 populateCrossReferenceList : function(data,filterBy){
		 var crossReferenceListTbl = $("#crossrefTable").DataTable();
		 	crossReferenceListTbl.clear();
	    	var l = data.length;
	    	for(var i = 0;i < l; i++){
    			var index = data[i];		    		
 	    		var row = [];
 	    		var isActive = index.active ? "active" : "deleted";
 	    		var id = index['id'];
 	    	  if(index.crossReferenceOption != 'split'){
 	    		if(filterBy == isActive || filterBy == 'all'){
 	    			row.push(i+1);
 		    		row.push(index.dimension || '-');
 		    		row.push(index.crossReferenceOption);
 		    		row.push(index.typeOfMerge || '-');
 		    		row.push(index.conditionName || '-');
 	    		    if(index.typeOfMerge  == 'automerge'){
 	    		    	if(index.active){
 	    		    		row.push('<button id="executeXrefCondition" data-conditionId = '+index.id+' class="btn btn-primary btn-sm" >'+globalMessage['anvizent.package.label.run']+'</button>');
 	    		    	}else{
 	    		    		row.push('<button id="executeXrefCondition" data-conditionId = '+index.id+' class="btn btn-primary btn-sm" disabled>'+globalMessage['anvizent.package.label.run']+'</button>');
 	    		    	}
 	    			}else{
 	    			 row.push('Done');
 	    			}
 	    		    if(index.typeOfMerge  == 'manualmerge'){
 	    		    	row.push('Success');
 	    			}else{
 	    				var path = $("#viewResultsByIdUrl").val() + index.id + "/" + index.conditionName + "/" + index.ilId;
 	    		    	row.push('<a id="viewResultsbyconditionId" data-conditionId = '+index.id+' href = "'+ path +'" >'+globalMessage['anvizent.package.link.viewResults']+'</a>')
 	    			}
 	    		    var disabledStatus = "";
 	    		    if(index.active){
 	    		    	disabledStatus = "";
 	    		    	row.push('<button class="btn btn-primary btn-sm tablebuttons deactivateCrossrefCondition" data-conditionId = '+index.id+' value="" title="Deactivate" >	<span class="glyphicon glyphicon-trash" aria-hidden="true"></span> </button>');
 	    		    }else{
 	    		    	disabledStatus = "disabled";
 	    		    	//row.push('Deleted');
 	    		    	row.push('<button class="btn btn-primary btn-sm tablebuttons activateCrossrefCondition" data-conditionId = '+index.id+' value="" title="Activate" > <span class="glyphicon glyphicon-folder-open" aria-hidden="true"></span> </button>');
 	    		    }
 	    		    row.push('<button type="button" class="btn btn-primary btn-sm  editXRefDetails" data-conditionId = '+index.id+' data-mergeType = '+index.typeOfMerge+'  value="" title="Edit" '+disabledStatus+' > <span class="fa fa-pencil" aria-hidden="true"></span> </button>');
 		    		crossReferenceListTbl.row.add(row);
 		    	}
 		    	crossReferenceListTbl.draw(true);
    		   }
    		 }
	    	 
	 },
	 
	 populateCrossReferenceAutoMergeTable : function(xrefObject) {
		 var columns = '';
		 var newColumns = [];
		 var crossReferenceObj = this;
		 var $xrefAutoMergeTbl = $('#crossRefAutoMergeTable tbody');
		 
		 	var columns = $("[name='autoMergeColumns']").val();
		     
		     if(isCreationMode){
		    	 $xrefAutoMergeTbl.empty();
		    	 xrefObject = null;
		     }else{
		    	 $xrefAutoMergeTbl.empty();
		    	 if(xrefObject != null && columns != null){
		    		 var xRefObjColumn = xrefObject;
		    		 var obj = {};
		    		 var temp = [];
		    		 for(var i = 0; i < xRefObjColumn.length; i++){
		    			 obj[xRefObjColumn[i].columnName] = (obj[xRefObjColumn[i].columnName] || 0) + 1;
		    		 }
		    		 for(j = 0 ; j < columns.length; j++){
		    			 var autoMergeCrossRefParams = {};
		    			 if(columns[j] in obj){
		    				 for(var k = 0 ; k < xRefObjColumn.length; k++){
		    				   	 if(columns[j] == xRefObjColumn[k].columnName){
		    				   		temp.push(xRefObjColumn[k]);
		    				   		break;
		    				   	 }
		    				 }
		    			 }else{
    					     autoMergeCrossRefParams['columnName'] = columns[j];
    					     autoMergeCrossRefParams['patternType'] = "1";
    					     autoMergeCrossRefParams['value'] = "";
    					     autoMergeCrossRefParams['pattern'] = "";
						    temp.push(autoMergeCrossRefParams);
	    				   } 
	    			 }
		    		 columns = temp;
		    	 }
		     }
			 $.each(columns, function(i,obj) {
				 var $tr = $("<tr>");
				 var $indexTd = $("<td>");
				   $indexTd.append($("<span>").text(i+1));
				   
				 var $columnTd = $("<td>");
				 if(isCreationMode)
				   $columnTd.append($("<span>", {class : 'columnName'}).text(obj));
				 else
					 $columnTd.append($("<span>", {class : 'columnName'}).text(obj.columnName));
				 var $patternTypeTd = $("<td>");
				 var select  = $('<select>',  { id : 'seletedPatternType', class : 'seletedPatternType form-control' });
		           select.append($('<option>', { value : '1', text : 'Starts with'}));
		           select.append($('<option>', { value : '2', text : 'Ends with'}));
		           select.append($('<option>', { value : '3', text : 'Contains'}));
		           select.append($('<option>', { value : '4', text : 'Match First N Chars'}));
		           select.append($('<option>', { value : '5', text : 'Exact Word Match (S)'}));
				   $patternTypeTd.append(select);
				   
				   if(!isCreationMode)
				       $patternTypeTd.find('#seletedPatternType').val(obj.patternType).attr('selected','selected');
				   
				   var $patternTd = $("<td>");
				   var $matchCharInput = $('<input>', {type : 'text', class : 'matchChars form-control hidden', maxlength : '1', placeholder: "No of Chars to match", onkeypress : 'return event.charCode >= 48 && event.charCode <= 57'}); 
				     $patternTd.append($matchCharInput);
				     if(isCreationMode){
				    	 $patternTd.append($("<input>", { type : 'text', class : 'selectPattern form-control', placeholder: 'Pattern'}));
				     }
				     else{
				    	 if(obj.patternType == 4)
				    	   $patternTd.append($("<input>", { type : 'text', class : 'selectPattern form-control', placeholder: 'Pattern',value : obj.value}));
				    	 else
				    	   $patternTd.append($("<input>", { type : 'text', class : 'selectPattern form-control', placeholder: 'Pattern',value : obj.pattern})); 
				     }
				  $tr.append($indexTd);
				  $tr.append($columnTd);
				  $tr.append($patternTypeTd);
				  $tr.append($patternTd);
				  
				   $xrefAutoMergeTbl.append($tr);
			 });
	    	     $('#autoMergeConditionTblDiv').removeClass('hidden');
	    	     $('.autoMergeBtnDiv').removeClass('hidden');
	},

	prepareXrefParamObject : function() {
		var conditionName = $("#conditionName").val();
		
		var $xrefDataTr = $('#crossRefAutoMergeTable tbody tr');
		var columns = $("[name='autoMergeColumns']").val();
		 xrefAutoMergeParamList = [];
		 var autoMergeObject = {};
	    $xrefDataTr.each(function(i, trData){
			
			var autoMergeCrossRefParams = {};
			
			var columnName = $(trData).find('.columnName').text();
			var patternType = $(trData).find('.seletedPatternType').val();
			var pattern = $(trData).find('.selectPattern').val();
			var matchFirstChar = $(trData).find('.matchChars').val();
			
			autoMergeCrossRefParams['columnName'] = columnName;
			autoMergeCrossRefParams['patternType'] = patternType;
			autoMergeCrossRefParams['value'] = matchFirstChar;
			autoMergeCrossRefParams['pattern'] = pattern;
			
			xrefAutoMergeParamList.push(autoMergeCrossRefParams);
		});
	       autoMergeObject['autoMergeColumns'] = xrefAutoMergeParamList;
		return autoMergeObject;
	},
	
	runCrossRefCondition : function(id) {
		showAjaxLoader(true);
		var url =  "/app/user/"+userID+"/crossreference/runCrossReferenceById/"+id;
		   var myAjax = common.loadAjaxCall(url,'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  common.showSuccessAlert(result.messages[0].text);
		    				  var message = result.messages[0].text;	
		    			  }
		    		  }
				}
		});
	},
	
	archiveCrossReference : function(id) {
		var crossReferenceObj = this;
		showAjaxLoader(true);
		var url =  "/app/user/"+userID+"/crossreference/archiveCrossReferenceById/"+id;
		var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  $("#archieveCrossReferencePopUpAlert").modal('hide');
		    				  crossReferenceObj.getCrossReferenceList();
		    				  common.showSuccessAlert(result.messages[0].text);
		    				  var message = result.messages[0].text;
		    			  }
		    		  }
				}
		});
	},
	
	unmergeExistingXRefandDelete : function(id) {
		var crossReferenceObj = this;
		showAjaxLoader(true);
		var url =  "/app/user/"+userID+"/crossreference/unmergeExistingXRefandDelete/"+ id + "/" + executionType;
		var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;
	    					  executionType = null;
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  $("#archieveCrossReferencePopUpAlert").modal('hide');
		    				  crossReferenceObj.getCrossReferenceList();
		    				  common.showSuccessAlert(result.messages[0].text);
		    				  var message = result.messages[0].text;
		    				  executionType = null;
		    			  }
		    		  }
				}
		});
	},
	
	
	
	activateCrossReference : function(id) {
		var crossReferenceObj = this;
		showAjaxLoader(true);
		var url =  "/app/user/"+userID+"/crossreference/activateCrossReferenceById/"+id;
		var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  $("#activeCrossReferencePopUpAlert").modal('hide');
		    				  crossReferenceObj.getCrossReferenceList();
		    				  common.showSuccessAlert(result.messages[0].text);
		    				  var message = result.messages[0].text;
		    			  }
		    		  }
				}
		});
	},
	
	getCrossReferenceLogsById : function(id){
		var crossReferenceObj = this;
		showAjaxLoader(true);
		var url =  "/app/user/"+userID+"/crossreference/getCrossReferenceLogsInfo/"+id;
		var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  var resultObj = result.object;
		    				  crossReferenceObj.getColumnsInfo(resultObj.ilId, resultObj, isCreationMode);
		    			  }
		    		  }
				}
		});
	},
	
	populateCrossReferenceInfo : function(resultObj){
		debugger;
		var crossReferenceObj = this;
		var ilid = resultObj.ilId;
		$("#ilId").val(ilid);
		$("#xrefId").val(resultObj.id);
		isCreationMode = false;
		var xRefConditionName = resultObj.conditionName;
		var xreference_type = resultObj.crossReferenceOption;
		var typeOfMerge = resultObj.typeOfMerge;
		xrefAutoMergeParamList = JSON.parse(resultObj.conditionObject);
		var manualMergeColumnName = resultObj.manualMergeColumnName;
		var manualMergeColumnValues = resultObj.manualMergeColumnValues == "" ? manualMergeColumnValues : JSON.parse(resultObj.manualMergeColumnValues);
		var typeOfXref = resultObj.typeOfXref;
		var existingSelectedXrefValue = resultObj.existingSelectedXrefValue;
		var selectedXrefKeyValue = resultObj.selectedXrefKeyValue;
		var bulkMergeReferenceFields = resultObj.bulkMergeReferenceFields;
		var bulkMergeXreferenceFields = resultObj.bulkMergeXreferenceFields != null ? resultObj.bulkMergeXreferenceFields.split(",") : "";
		var sourceFileInfoId = resultObj.sourceFileInfoId;
		var autoMergeColumns = resultObj.autoMergeColumns;
		var stats = resultObj.stats;
		
		var ilTableName= ilInfoMap[ilid].iL_table_name;
		var ilName= ilInfoMap[ilid].iL_name;
		crossReference.hideObjects(".filterByDiv,.crossrefInfoDiv, .crossRefTypeOptionDiv,#conditionName");
		crossReference.showObjects(".addCrossrefDiv,.crossReferenceTypeDiv,#xReferenceOption3");
		$(".selectSourceDetails").addClass('hidden');
		$(".selectSource").append($("<span>", {class : 'ilName'}).text(ilName));
		if(xreference_type == 'merge'){
			//$("#conditionName").prop("disabled",true);
			$("#crossReferenceOption1").prop("checked",true);
			  crossReference.showObjects(".mergerDiv");
			$('#conditionNameSpan').text(xRefConditionName);  
			$("#conditionName").val(xRefConditionName);
			  crossReference.hideObjects(".splitterDiv");
			if (typeOfMerge == "bulkmerge") {
				$(".mergeType").append($("<span>", {class : 'typeOfMerge'}).text(typeOfMerge));
				$(".bulkMergeDiv").removeClass("hidden");
				  crossReference.hideObjects(".typeOfMergeDiv,.bulkMergeBtnDiv");
				  crossReference.populatereferencefieldsselectBoxes();
				$("#referenceField").val(bulkMergeReferenceFields).multipleSelect('refresh');
				$("#xReferenceField").val(bulkMergeXreferenceFields).multipleSelect('refresh');
			} else if(typeOfMerge == "automerge"){
				$(".mergeType").append($("<span>", {class : 'typeOfMerge'}).text(typeOfMerge));
				  crossReference.showObjects(".autoMergeDiv");
				  crossReference.hideObjects(".typeOfMergeDiv");
				  crossReference.populateAutoMergeComboBox();
				var values = [];
				var tblPatternValues = [];
				var $xrefAutoMergeTbl = $('#crossRefAutoMergeTable tbody');
				xrefObject = xrefAutoMergeParamList.autoMergeColumns;
				$.each(xrefAutoMergeParamList.autoMergeColumns, function(i, paramValue){
					values.push(paramValue.columnName);
				});
				$("#autoMergeColumns").val(values).multipleSelect('refresh');
				$('#autoMergeConditionBtn').trigger('click');
				  //crossReference.populateCrossReferenceAutoMergeTable(xrefAutoMergeParamList.autoMergeColumns);
			} else if(typeOfMerge == "manualmerge"){
				$(".mergeType").append($("<span>", {class : 'typeOfMerge'}).text(typeOfMerge));
				  crossReference.showObjects(".manualMergeDiv");
				  crossReference.hideObjects(".typeOfMergeDiv");
				  crossReference.populateManualMergeComboBox();
				var comboBox = $("#ilColumnValueAjax");
				$("#manualMergeIlColumnName").val(manualMergeColumnName).trigger("change");
				$.each(manualMergeColumnValues, function(i, value){
				   common.populateComboBox(comboBox, value, value);
				});
				$("#ilColumnValueAjax").val(manualMergeColumnValues).trigger("change");
				$("#btnFindMatchedRecords").trigger('click');
			}
		}else if(xreference_type == 'split'){
			$("#crossReferenceOption2").prop("checked",true);	
			  crossReference.showObjects(".splitterDiv");
			  crossReference.hideObjects(".mergerDiv");
			  crossReference.populateSplitComboBox();
		}
	},
	
	autoSplitConditionById : function(id) {
		var crossReferenceObj = this;
		showAjaxLoader(true);
		var url =  "/app/user/"+userID+"/crossreference/autoSplitConditionById/"+id + "/" + executionType;
		var myAjax = common.loadAjaxCall(url, 'GET');
		 myAjax.done(function(result) {
			showAjaxLoader(false);
			if(result != null){
    		  if(result.hasMessages){
				  if(result.messages[0].code=="ERROR"){
					  common.showErrorAlert(result.messages[0].text);
					  var message = result.messages[0].text;		    				
    			  }
    			  if(result.messages[0].code=="SUCCESS"){
    				  common.showSuccessAlert(result.messages[0].text);
    				  var message = result.messages[0].text;
    				  executionType = null;
    			  }
    		  }
			}
		});
	},
	
	processManualMerge : function(){
		common.clearValidations(["#selectExistingXrefMsg, #selectColumnsToMergeMsg"]);
		var status = true;
		var xrefId = $("#xrefId").val();
		/*var dimension = $("#ilId option:selected").text();;
		var xreferenceType = $("#crossReferenceOption").is(":checked") ? merge : split;*/
		var conditionName = $("#conditionName").val();
	    var applicableDate = $("#applicableDate").val();
	    
	    var commonFieldsValidationStatus = crossReference.validateCommonMergerFields();
	    if (!commonFieldsValidationStatus) {
	    	return false;
	    }
		
	    var selectedMergeRecords = [];
	    crossRefMergeTable.$("#ilMergeColumns:checked").each(function(){
			selectedMergeRecords.push(this.value); 
		});
		
		if ($("input[name='newOrExistingXref']:checked").val() == "newXref" && selectedMergeRecords.length < 2) {
			common.showcustommsg("#selectColumnsToMergeMsg", globalMessage['anvizent.package.label.pleaseSelectMoreThanOneRecordForMerge'], "#selectColumnsToMergeMsg");
			status = false;
		}
		
		if ($("input[name='newOrExistingXref']:checked").val() == "existingXref" && selectedMergeRecords.length < 1) {
			common.showcustommsg("#selectColumnsToMergeMsg", globalMessage['anvizent.package.label.pleaseSelectAtLeastOneRecordForMerge'], "#selectColumnsToMergeMsg");
			status = false;
		} 
		
		if($("input[name='newOrExistingXref']:checked").val() == 'existingXref'){
			if($("#existingXrefValue").val()){
				if($(".xref-table").find("input[name='existingxrefValueKey']:checked").length == 0){
					common.showcustommsg("#selectExistingXrefMsg", globalMessage['anvizent.package.label.pleaseSelectAtLeastOneRecordForMerge'], "#selectExistingXrefMsg");
					status = false;
				}
			}
		}
		if(!status){
			return false;
		}
		$("#existingXrefKey").val($("input[name='existingxrefValueKey']:checked").val());
		
		var columns = {};
		$("[class^='xrefcolumns_']").each(function(inputObj){
			columns[$(this).prop("name")] = $(this).val();
		});
		$("#ilXreferenceColumn").val(JSON.stringify(columns));	
		
		var ilId = $("#ilId").val();
		var $thisForm = this;
		var selectedObject = [];
		var url = '';
		if ($("[name='newOrExistingXref']:checked").val() == 'newXref') {
			selectedObject = {
					id : xrefId == "" ? 0 : xrefId,
					ilId : ilId,
					ilColumnName : $("[name='manualMergeIlColumnName']").val(),
					crossReferenceOption : $("[name='crossReferenceOption']:checked").val(),
					typeOfMerge : $("[name='crossReferenceOption']:checked").val(),
					ilColumnValue: $("[name='manualMergerIlColumnValue']").val(),
					newOrExistingXref: $("[name='newOrExistingXref']:checked").val(),
					ilMergeColumns: selectedMergeRecords,
					ilXreferenceColumn: JSON.stringify(columns),
					autoincrementColumnName : autoincrementColumnName,
					xRefKeyColumnName : xRefKeyColumnName,
					conditionName : conditionName,
					applicableDate : applicableDate,
					xrefExecutionType : executionType == null ? "Regular" : executionType
			};
			url = "/app/user/" + userID + "/crossreference/mergeCrossReferenceRecords";
		} else if ($("[name='newOrExistingXref']:checked").val() == 'existingXref') {
			selectedObject = {
					id : xrefId == "" ? 0 : xrefId,
					ilId : ilId,
					ilColumnName : $("[name='manualMergeIlColumnName']").val(),
					existingXrefKey : $(".xref-table").find("input[name='existingxrefValueKey']:checked").val(),
					crossReferenceOption : $("[name='crossReferenceOption']:checked").val(),
					ilColumnValue: $("[name='manualMergerIlColumnValue']").val(),
					newOrExistingXref: $("[name='newOrExistingXref']:checked").val(),
					ilMergeColumns: selectedMergeRecords,
					ilXreferenceColumn: JSON.stringify(columns),
					autoincrementColumnName : autoincrementColumnName,
					xRefKeyColumnName : xRefKeyColumnName,
					conditionName : conditionName,
					applicableDate : applicableDate,
					xrefExecutionType : executionType == null ? "Regular" : executionType
			};
			url = "/app/user/" + userID + "/crossreference/mergeCrossReferenceRecordsWithExistingXref";
		}
		showAjaxLoader(true);
		var myAjax = common.postAjaxCall(url, 'POST',selectedObject);
		myAjax.done(function(result) {
			showAjaxLoader(false);
			if (common.validateDataResponse(result)) {
				crossReference.cleanUpManualMerge(true);
				common.showSuccessAlert(result.messages[0].text);
				setTimeout(function() {
					window.location = adt.appContextPath+'/adt/crossReference';
				},1000);
				
			}
		});
	},
	
	processAutoMerge : function(){
		var ilId = $("#ilId").val();
		var xrefId = $("#xrefId").val();
		var conditionName = $("#conditionName").val();
	    var applicableDate = $("#applicableDate").val();
	    
	    var commonFieldsValidationStatus = crossReference.validateCommonMergerFields();
	    if (!commonFieldsValidationStatus) {
	    	return false;
	    }
		common.clearcustomsg($("[name='autoMergeColumns']"));
		if ( !$("[name='autoMergeColumns']").val() ) {
			common.showcustommsg("#autoMergeColumns", globalMessage['anvizent.package.label.pleaseSelectAtLeastoneColumn'], "#ilMergeColumns");
			return false;
		}
		var conditionObject = crossReference.prepareXrefParamObject();
		var selectedObject = {
				id : xrefId == "" ? 0 : xrefId,
				ilId : ilId,
				crossReferenceOption : $("[name='crossReferenceOption']:checked").val(),
				typeOfMerge : $("[name='crossReferenceOption']:checked").val(),
				ilMergeColumns : $("[name='autoMergeColumns']").val(),
				conditionName : conditionName,
				applicableDate : applicableDate,
				conditionObject : JSON.stringify(conditionObject),
				xrefExecutionType : executionType == null ? "Regular" : executionType
		}
		showAjaxLoader(true);
		var url =  "/app/user/"+userID+"/crossreference/saveAutoMergeCrossReference";
		   var myAjax = common.postAjaxCall(url,'POST',selectedObject);
			myAjax.done(function(result) {
			showAjaxLoader(false);
				if(result != null){
	    		 if(result.hasMessages){
				   if(result.messages[0].code=="ERROR"){
					  common.showErrorAlert(result.messages[0].text);
					  var message = result.messages[0].text;
    			   }
    			   if(result.messages[0].code=="SUCCESS"){
    				  setTimeout(function(){ window.location.reload(); }, 1000);
    				  common.showSuccessAlert(result.messages[0].text);
    				  crossReference.cleanUpAutoMerge(true);
    			   }
	    		  }
			  }   
		});
	},
	
	processBulkMerge : function(){
		var ilId = $("#ilId").val();
		var xrefId = $("#xrefId").val();
		var flatFileType = $("#flatFileType").val();
	    var referenceFieldArray = $("#referenceField").val();
	    var xReferenceFieldArray = $("#xReferenceField").val();
	    var conditionName = $("#conditionName").val();
	    var applicableDate = $("#applicableDate").val();
	    var commonFieldsValidationStatus = crossReference.validateCommonMergerFields();
	    if (!commonFieldsValidationStatus) {
	    	return false;
	    }
	    var iLColumnNames = [];
	    var selectedFileHeaders = [];
	    var dafaultValues = [];
	    var proceed = true;
	    $(".mapped-header-combo-box").each(function(i, obj) {
			var iLColumn = obj.value;
			var fileHeader = $(obj).data("filecolumnname");
			if ( iLColumn != "0") {
				iLColumnNames.push(iLColumn);
				selectedFileHeaders.push(fileHeader);
				dafaultValues.push('');
			}
		});
	    $.each(referenceFieldArray.concat(xReferenceFieldArray),function(i,val){
	    	if (iLColumnNames.indexOf(val) == -1 ) {
	    		common.showErrorAlert(val + " not mapped to any of the file headers");
	    		proceed = false;
	    		return;
	    	}
		});
	    if (!proceed) {
	    	return false;
	    }
		var selectedObject = {
				id : xrefId == "" ? 0 : xrefId,
				filepath : uploadedfilepath,
				flatFileType : flatFileType,
				delimeter : ',',
				selectedIlId : ilId,
				referenceField : referenceFieldArray.join(',') ,
				xReferenceField : xReferenceFieldArray.join(',') ,
				iLColumnNames : iLColumnNames.join(','),
				selectedFileHeaders : selectedFileHeaders.join(','),
				dafaultValues : dafaultValues.join(','),
				conditionName : conditionName,
				applicableDate : applicableDate,
				xrefExecutionType : executionType == null ? "Regular" : executionType
		}
		showAjaxLoader(true);
		var url =  "/app/user/"+userID+"/crossreference/bulkCrossReference";
		   var myAjax = common.postAjaxCallObject(url,'POST',selectedObject);
			myAjax.done(function(result) {
			 showAjaxLoader(false);
			 if(result != null){
	    		  if(result.hasMessages){
    				  if(result.messages[0].code=="ERROR"){
    					  common.showErrorAlert(result.messages[0].text);
    					  var message = result.messages[0].text;		    				
	    			  }
	    			  if(result.messages[0].code=="SUCCESS"){
	    				  common.showSuccessAlert(result.messages[0].text);
	    				  setTimeout(function() {
	    						window.location = adt.appContextPath+'/adt/crossReference';
	    					},1000);
	    				  crossReference.cleanUpBulkMerge(true);
	    			  }
	    		  }
			  }
		});
	},
	
}

if ($('.crossReference-page').length) {
	crossReference.initialPage();
    crossReference.getCrossReferenceList();
	
    var xrefId = '';
	$("#ilId").change(function(){
		var ilId = $(this).val();
		console.log($(this).attr('data-tablename'));
		crossReference.hideObjects(".view-audit-logs-div");
		 if (this.value != "0") {
			 crossReference.hideObjects(".first-level-div")
			 crossReference.showObjects(".crossReferenceTypeDiv,.view-audit-logs-div")
			 $("[name='crossReferenceOption']").prop("checked",false);
			 crossReference.getColumnsInfo(ilId, null, isCreationMode);
		 } else {
			 crossReference.hideObjects(".crossReferenceTypeDiv")
		 }
	});
	
	$("#uploadBulkMergeFileBtn").click(function(){
		var flatFileType = $("#flatFileType").val();
	    var filePath = $("#fileUpload").val();
	    var referenceFieldArray = $("#referenceField").val();
	    var xReferenceFieldArray = $("#xReferenceField").val();
	    common.clearValidations(["#fileUpload","#xReferenceField+div","#referenceField+div"]);
		 var fileExtension = filePath.replace(/^.*\./, '');
		 if ( !referenceFieldArray || !referenceFieldArray.length ) {
			 common.showcustommsg("#referenceField+div", globalMessage['anvizent.package.label.pleaseSelectReferenceColumns']);
			 return false;
		 }
		 if ( !xReferenceFieldArray || !xReferenceFieldArray.length ) {
			 common.showcustommsg("#xReferenceField+div", globalMessage['anvizent.package.label.pleaseSelectXReferenceColumns']);
			 return false;
		 }

	    var commonColumns = $.grep(referenceFieldArray, function(element) {
	    	return $.inArray(element, xReferenceFieldArray ) !== -1;
	    });
	    
		if ( commonColumns.length ) {
			common.showcustommsg("#xReferenceField+div", ('<b>' + commonColumns.join(',') + '</b> '+ (commonColumns.length == 1 ?'is':'are') +' selected in both options'));
	        return false;
		}
		
		if(fileExtension != flatFileType) {
			common.showcustommsg("#fileUpload", globalMessage['anvizent.package.label.fileExtensionIsNotMatchingWithFileTypeSelected']);
			return false;
		}
	    showAjaxLoader(true);
	    var formData = new FormData();
	    formData.append('selectedIlId', $("#ilId").val());
	    formData.append('delimeter', ',');
	    formData.append('flatFileType', flatFileType);
	    //formData.append('referenceField', referenceFieldArray.join(',') ); 
	    //formData.append('xReferenceField', xReferenceFieldArray.join(',')); 
	    formData.append('file', $('#fileUpload')[0].files[0]); 
	    
	    var url_uploadFileIntoS3 = "/app/user/"+userID+"/crossreference/bulkCrossReferenceFileHeader";
		var myAjax = common.postAjaxCallForFileUpload(url_uploadFileIntoS3,'POST', formData,common.getcsrfHeader());
		myAjax.done(function(result) {
			showAjaxLoader(false);
			$("#referenceField").multipleSelect("disable");
			$("#xReferenceField").multipleSelect("disable");
			$("#fileUpload").prop("disabled","disabled");
			$(".bulkMergeBtnDiv").removeClass('hidden');
			if (common.validateDataResponse(result)) {
				var selectbox$ = $("<select>",{class:'form-control mapped-header-combo-box'});
				selectbox$.append($("<option>", {
					text : globalMessage['anvizent.package.label.select'],
					value : 0
				}));
				$.each(referenceFieldArray.concat(xReferenceFieldArray),function(i,val){
					selectbox$.append($("<option>", {
						text : val,
						value : val
					}));
				});
				uploadedfilepath = result.object.filePath;
				crossReference.hideBulkMergeButtonsGroup();
				crossReference.showObjects("#initiateBulkMergeBtn,#cancelBulkMergeInitiation,.file-headers-mapping-div");
				var mappingDiv = $(".file-headers-mapping-div div.file-headers-div");
				mappingDiv.empty();
				
				$.each(result.object.columnsList,function(i, colummnName){
					var select$ = selectbox$.clone().attr({"id":"mapped-header-combo-box"+i, "data-filecolumnname":colummnName});
					select$.children().each(function(){
						if (this.value.toLowerCase() == colummnName.toLowerCase()) {
							$(this).prop("selected","true");
				        }
					});
					
					mappingDiv.append($("<div>",{class:'row form-group'}).append($("<div>",{class:'col-sm-1'}).text(i+1)).append($("<div>",{class:'col-sm-5'}).text(colummnName))
							.append($("<div>",{class:'col-sm-6'}).append(select$)));
				});
				
				$("#mapped-header-combo-box0").children().each(function(){
					if (this.value.toLowerCase() == 'SUPPLIER_ID'.toLowerCase()) {
						$(this).prop("selected","true");
			        }
				});
				
				/*$("#fileUpload").val("");
			    $("#referenceField").multipleSelect("uncheckAll");
			    $("#xReferenceField").multipleSelect("uncheckAll");
				common.showSuccessAlert(result.messages[0].text);
				*/
				
			}
	    	  	
	    });		    
	});
	
	$("#btnFindMatchedXrefRecords").click(function(){
		var status = crossReference.validateSplitInputFields();
		if (!status) {
			return false;
		}
		crossReference.getRecordsForSplit();
	});
	
	
	$(".file-headers-mapping-div").on("change",".mapped-header-combo-box",function() {
		var currentValue = this.value;
		if (!currentValue) {
			return;
		}
		var isDuplicateFound = false;
		var _thisId = $(this).prop("id");
		common.clearValidations([$(this)]);
		$(".mapped-header-combo-box").each(function(i, obj){
			if (_thisId != $(obj).prop('id') && this.value == currentValue) {
				isDuplicateFound =true;
				return;
			}
		});
		
		if (isDuplicateFound && this.value != "0") {
			this.value = 0;
			common.showcustommsg($(this), currentValue + ' already mapped to other header');
		}
	});
	
	$("#refreshData").click(function(){
		common.clearValidations(["#xReferenceField+div","#referenceField+div"]);
		var referenceFieldArray = $("#referenceField").val();
	    var xReferenceFieldArray = $("#xReferenceField").val();
	    var commonColumns = $.grep(referenceFieldArray, function(element) {
	    	return $.inArray(element, xReferenceFieldArray ) !== -1;
	    });
		if ( commonColumns.length ) {
			common.showcustommsg("#xReferenceField+div", ('<b>' + commonColumns.join(',') + '</b> '+ (commonColumns.length == 1 ?'is':'are') +' selected in both options'));
	        return false;
		}
		var selectbox$ = $("<select>",{class:'form-control mapped-header-combo-box'});
		selectbox$.append($("<option>", {
			text : globalMessage['anvizent.package.label.select'],
			value : 0
		}));
		$.each(referenceFieldArray.concat(xReferenceFieldArray),function(i,val){
			selectbox$.append($("<option>", {
				text : val,
				value : val
			}));
		});
		$(".mapped-header-combo-box").each(function(i, obj){
			var objValue = obj.value;
			$(obj).empty();
			$(obj).append(selectbox$.html());
			$(obj).val(objValue);
		});
	});
	
	$("#initiateBulkMergeBtn").click(function(){
		mergeType = "bulk";
		if(isCreationMode){
			crossReference.processBulkMerge();
		}else{
			 $("#editCrossMergeCbx").prop( "checked", true );
			$("#editCrossReferencePopUpAlert").modal('show');
		}
	});
	
	$("#cancelBulkMergeInitiation").click(function(){
		crossReference.hideBulkMergeButtonsGroup();
		crossReference.hideObjects(".file-headers-mapping-div");
		crossReference.showObjects("#uploadBulkMergeFileBtn");
		$("#referenceField").multipleSelect("enable");
		$("#xReferenceField").multipleSelect("enable");
		$("#fileUpload").prop("disabled",false);
		
	});
	
	$("[name='crossReferenceOption']").click(function(){
		var selectedXrefType = $("[name='crossReferenceOption']:checked").val();
		if(isCreationMode){
			crossReference.populateSplitComboBox();
			crossReference.hideMergeAndSplitDivs();
			crossReference.cleanUpCommonFields();
			crossReference.cleanUpManualMerge();
			crossReference.cleanUpBulkMerge();
			crossReference.cleanUpAutoMerge();
			if (selectedXrefType == "merge") {
				crossReference.showObjects(".mergerDiv");
				$("[name='typeOfMerge']").prop("checked",false);
				crossReference.hideMergeDivs();
			} if(selectedXrefType == "split"){
				crossReference.showObjects(".splitterDiv");
			}
		}else{
			var typeOfMerge = $(".typeOfMerge").text();
			if(selectedXrefType == 'merge'){
				$("#manualMergeIlColumnName,#referenceField,#xReferenceField").prop("disabled",false);
				$("#crossReferenceOption1").prop("checked",true);
				crossReference.showObjects(".mergerDiv");
				crossReference.hideObjects(".splitterDiv");
				if (typeOfMerge == "bulkmerge") {
					crossReference.showObjects(".bulkMergeDiv,.autosplitBulkMergeDiv");
					crossReference.hideObjects(".typeOfMergeDiv,.bulkMergeBtnDiv,.autoSplitBtnDiv");
				} else if(typeOfMerge == "automerge"){
					crossReference.showObjects(".autoMergeDiv,#crossRefAutoMergeTable,.autoMergeBtnDiv,#autoMergeConditionBtn");
					crossReference.hideObjects(".typeOfMergeDiv,.autoSplitBtnDiv");
				} else if(typeOfMerge == "manualmerge"){
					crossReference.showObjects(".manualMergeDiv,.manualAutoSpltDiv");
					crossReference.hideObjects(".typeOfMergeDiv,.autoSplitBtnDiv");
				}
			}else if(selectedXrefType == 'split'){
				crossReference.showObjects(".splitterDiv");
				crossReference.hideObjects(".mergerDiv,.autoSplitBtnDiv");
				crossReference.populateSplitComboBox();
			}else if(selectedXrefType == 'autosplit'){
				crossReference.showObjects(".addCrossrefDiv,.crossReferenceTypeDiv,#xReferenceOption3");
				$(".selectSourceDetails").addClass('hidden');
				crossReference.hideObjects(".manualAutoSpltDiv,.crossRefTypeOptionDiv,s.splitterDiv,#crossRefAutoMergeTable,.autoMergeBtnDiv,.autosplitBulkMergeDiv,#autoMergeConditionBtn");
				crossReference.showObjects(".mergerDiv,.autoSplitBtnDiv");
				$("#conditionName,#manualMergeIlColumnName").prop("disabled",true);
				$("#referenceField,#xReferenceField").prop('disabled',true);
			}
		}
		
		/*if ($("#ilColumnValue").val()) {
			$("#ilColumnValue option:selected").prop("selected", false);
		}*/
	});
	
	$("[name='typeOfMerge']").click(function(){
		var selectedMergeType = $("[name='typeOfMerge']:checked").val();
		crossReference.hideMergeDivs();
		
		if (selectedMergeType == "bulkmerge") {
			$(".bulkMergeDiv").removeClass("hidden");
			crossReference.cleanUpBulkMerge();
		} else if(selectedMergeType == "automerge"){
			crossReference.showObjects(".autoMergeDiv");
			crossReference.populateAutoMergeComboBox();
			
		} else if(selectedMergeType == "manualmerge"){
			$(".manualMergeDiv").removeClass("hidden");
			crossReference.populateManualMergeComboBox();
			crossReference.cleanUpManualMerge();
		}
	});
	
	
	$("#btnFindMatchedRecords").click(function(){
		var status = crossReference.validateManualMergeInputFields();
		if (!status) {
			return false;
		}
		crossReference.getRecordsForManualMerge();
	});
	
	
	$(".newXref").click(function(){
		crossReference.hideObjects(".manual-merge-div");
		crossReference.populateNewXrefHeadersandData();
	});
		
	$(".existingXref").click(function(){
		$("#existingXrefValue").val([]);
		$("#existingXrefValue").val("0").trigger("change");
		crossReference.hideObjects(".manual-merge-div");
		crossReference.showObjects(".existing-x-ref-div");
	});
	
	$("#existingXrefValue").on("change",function(){
		crossReference.getExistingRecordsForManualMerge();
	});
	$("#autoMergeBtn").on("click",function(){
		mergeType = "auto";
		if(isCreationMode){
			crossReference.processAutoMerge();
		}else{
			 $("#editCrossMergeCbx").prop( "checked", true );
			$("#editCrossReferencePopUpAlert").modal('show');
		}
	});
	
/*	$("#checkAll").click(function(){
		$("[name='ilMergeColumns']").prop("checked",this.checked);
	});
	

	$("[name='ilMergeColumns']").click(function(){
		if ($("[name='crossReferenceOption']:checked").val() == "split" ){
			$("#checkAll").prop("checked",($("[name='ilMergeColumns']:checked").length == $("[name='ilMergeColumns']").length )); 
		}
	});*/	
	
	$("[name='ilXreferenceColumn']").click(function(){
		$("[name='ilMergeColumns']").prop("disabled" , false);
		$($(this).closest("tr")).find("[name='ilMergeColumns']").prop({"disabled":true,checked:false});
	});

	$("#mergeSelectedRecords").click(function(){
		//mergeType = "manual";
		/*if(isCreationMode){
			crossReference.processManualMerge();
		}else{
			$("#editCrossMergeCbx").prop( "checked", true );
			$("#editCrossReferencePopUpAlert").modal('show');
		}*/
		crossReference.processManualMerge();
	});
	
	$("#splitSelectedRecords").click(function(){
		
		var ilId = $("#ilId").val();
		var xrefId = $("#xrefId").val();
		
		var selectedSplitRecords = [];
		crossRefSpitTable.$("#ilMergeColumns:checked").each(function(){
			selectedSplitRecords.push(this.value); 
		});
		
		if (selectedSplitRecords.length == 0 /*|| diff == 1*/) {
			var  messages=[{
				  code : globalMessage['anvizent.message.error.code'],
				  text : globalMessage['anvizent.package.label.pleaseSelectAtLeastOneRecordForSplit']
			}];
			common.displayMessages(messages);
			return false;
		}   
	 
		var selectedObject = {
				ilId : ilId,
				ilColumnName : $("[name='splitIlColumnName']").val(),
				ilMergeColumns: selectedSplitRecords,
				autoincrementColumnName : autoincrementColumnName,
				xRefKeyColumnName : xRefKeyColumnName,
		};
		var url = "/app/user/" + userID + "/crossreference/splitRecords";
		showAjaxLoader(true);
		var myAjax = common.postAjaxCall(url, 'POST',selectedObject);
		myAjax.done(function(result) {
			showAjaxLoader(false);
			
			if (common.validateDataResponse(result)) {
				common.showSuccessAlert(result.messages[0].text);
				crossReference.cleanUpSplit();
			}
		});
		
	});
	
	
	
	var headers = {};
	$("#ilColumnValueAjax").select2({
		    minimumInputLength: 1,
		    ajax: {
		        url: "/minidw/app/user/"+$("#userID").val()+"/crossreference/getDistinctValues", 
		        dataType: 'json',
		        type: "POST",
		        beforeSend: function(request) {
		            request.setRequestHeader($("meta[name='_csrf_header']").attr("content"), $("meta[name='_csrf']").attr("content"));
		          },
		          delay: 250,
		        quietMillis: 50,
		        data: function (term) {
		        	var query = {
		        			ilId: $("#ilId").val(),
		        			tableName: ilInfoMap[$("#ilId").val()].iL_table_name,
			            	columnName: $("#manualMergeIlColumnName").val(),
			            	isXrefValueNull:$("[name='crossReferenceOption']:checked").val() == "merge", 
			            	columnValue: term.term 
		        	      }
		            return query;
		        },
		        processResults: function (data) { 
		            return {
		                results: $.map(data.object, function (item) {
		                    return {
		                        text: item,
		                        id: item.replace(new RegExp(",", 'g'),"#$#$#$")
		                    }
		                })
		            };
		        }
		    },cache: true
		});
	
	$("#splitIlColumnValue").select2({
	    minimumInputLength: 1,
	    ajax: {
	        url: "/minidw/app/user/"+$("#userID").val()+"/crossreference/getDistinctValues", 
	        dataType: 'json',
	        type: "POST",
	        beforeSend: function(request) {
	            request.setRequestHeader($("meta[name='_csrf_header']").attr("content"), $("meta[name='_csrf']").attr("content"));
	          },
	          delay: 250,
	        quietMillis: 50,
	        data: function (term) {
	        	var query = {
	        			ilId: $("#ilId").val(),
	        			tableName: ilInfoMap[$("#ilId").val()].iL_table_name,
		            	columnName: $("#splitIlColumnName").val(),
		            	isXrefValueNull:false, 
		            	columnValue: term.term 
	        	      }
	            return query;
	        },
	        processResults: function (data) { 
	            return {
	                results: $.map(data.object, function (item) {
	                    return {
	                        text: item,
	                        id: item.replace(new RegExp(",", 'g'),"#$#$#$")
	                    }
	                })
	            };
	        }
	    },cache: true
	});
	
	$("#existingXrefValue").select2({
	    minimumInputLength: 1,
	    ajax: {
	        url: "/minidw/app/user/"+$("#userID").val()+"/crossreference/getDistinctValues", 
	        dataType: 'json',
	        type: "POST",
	        beforeSend: function(request) {
	            request.setRequestHeader($("meta[name='_csrf_header']").attr("content"), $("meta[name='_csrf']").attr("content"));
	          },
	          delay: 250,
	        quietMillis: 50,
	        data: function (term) {
	        	var query = {
	        			ilId: $("#ilId").val() ,
		            	columnName: $("#manualMergeIlColumnName").val(),
		            	isXrefValueNull:false, 
		            	columnValue: term.term 
	        	      }
	            return query;
	        },
	        processResults: function (data) { 
	            return {
	                results: $.map(data.object, function (item) {
	                    return {
	                        text: item,
	                        id: item.replace(new RegExp(",", 'g'),"#$#$#$")
	                    }
	                })
	            };
	        }
	    },cache: true
	});
	
	
	
	$(document).on("keypress", ".select2-search__field", function(event){
		console.log("in ctrl + 1");
	    if (event.ctrlKey || event.metaKey) {
	        var id =$(this).parents("div[class*='select2-container']").attr("id").replace("s2id_","");
	        var element =$("#"+id);
	        if (event.which == 97){
	            var selected = [];
	            element.find("option").each(function(i,e){
	                selected[selected.length]=$(e).attr("value");
	            });
	            element.select2("val", selected);
	        } else if (event.which == 100){
	            element.select2("val", "");
	        }
	    }
	});
 
	/*$("#selectAll").click(function(){
			$("[name='ilMergeColumns']:not(:disabled)").prop("checked", this.checked);
	});
	*/
	$("#ViewInfo").on("click",function(){
		 $("#viewDetails").modal('show');
	});
	
	$(document).on('click','#ViewInfo',function(){		 
		var ilId = $("#ilId").val();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true); 
		var url =  "/app/user/"+userID+"/crossreference/getCrossInfo/"+ilId;
		var myAjax = common.loadAjaxCall(url,'GET','',headers);
	    	myAjax.done(function(result) {
			    showAjaxLoader(false);
		    if(result != null && result.hasMessages){
		    	if(result.messages[0].code == "SUCCESS") {
		    	var table = result["object"];
		    	var id = result.object["id"];
		    	
		    	var crossReferenceTbl = $("#viewCrossReferenceTable").DataTable();
		    	crossReferenceTbl.clear();
		    	var l = table.length;
		    	for(var i=0;i<l;i++){
		    		var t = table[i];		    		
		    		var row = [];
		    		var id = t['id'];
		    		row.push(i+1);
		    		row.push(t['crossReferenceOption']);
		    		row.push(t['typeOfMerge'] || '-');
		    		row.push(t['conditionName'] || '-');
		    		/*row.push(t['applicableDate'] || '-');*/
		    		row.push("-");
		    		//TODO row.push("<button class='btn btn-primary btn-sm tablebuttons viewColumnValues text-underline' data-id='"+id+"'>"+globalMessage['anvizent.package.label.ViewResults']+"</button>");
		    		row.push(t['startDate'] || '-');
		    		row.push(t['endDate'] || '-');
		    		crossReferenceTbl.row.add(row);
		    	}
		    	
		    	crossReferenceTbl.draw(true); 
    			var popup = $("#viewDetails");	  		  	
	  		  	popup.modal('show');
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
	
	$(document).on("click",".viewColumnValues",function(){
		var id = $(this).attr("data-id");
		var table = $(this);
		showAjaxLoader(true);
		var url =  "/app/user/"+userID+"/crossreference/getColumnValues/"+id+"";
		   var myAjax = common.postAjaxCallObject(url,'GET','',headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){		    				
		    				  crossReference.showColumnValues(result.object);
		    			  }
		    		  }
				}
		});
	});

	$(document).on("click",".viewAutoMergeQueries",function(){
		var id = $(this).attr("data-id");
		var table = $(this);
		showAjaxLoader(true);
		var url =  "/app/user/"+userID+"/crossreference/getAutoMergeQueriesById/"+id+"";
		   var myAjax = common.postAjaxCallObject(url,'GET','',headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){		    				
		    				  crossReference.showAutoMergeXrefQueries(result.object);
		    			  }
		    		  }
				}
		});
	});
	var unMergeBtn;
	$(document).on("click",".remerge",function(){
		var ilValue = this.value;
		unMergeBtn = $(this);
		$("#ilValue").val(ilValue);
		var ilId = $("#ilId").val();
		var xrefId = $(this).data("xrefid");
		var xrefautoIncColumn = $("#xRefKeyColumnName").val();
		
		var selectData = {
				id:xrefId,
				ilId:ilId,
				autoincrementColumnName : xRefKeyColumnName,
				xRefKeyColumnName : xRefKeyColumnName,
		};
		showAjaxLoader(true);
	 	var url =  "/app/user/"+userID+"/crossreference/getUnMergeRowWithColumns";
		var myAjax = common.postAjaxCall(url,'POST', selectData);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){		    				
		    				 crossReference.showUnMergeRowWithColumns(result.object);
		    			  }
		    		  }
				}
		}); 
	});
	
	$(document).on('click','#confirmUnMerge',function(){	
		
		var ilId = $("#ilId").val();
		var selectData = {
				id:$(unMergeBtn).data('xrefid'),
				ilId:ilId,
				ilValue:$(unMergeBtn).val(),
				autoincrementColumnName : autoincrementColumnName,
				xRefKeyColumnName : xRefKeyColumnName,
		};
		showAjaxLoader(true);
	 	var url =  "/app/user/"+userID+"/crossreference/unMergeSelectedCrossReferenceRecord";
		var myAjax = common.postAjaxCall(url,'POST', selectData);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					common.showSuccessAlert(result.messages[0].text);
					$($(unMergeBtn).closest("td")).find('.ilMergeColumns').removeClass("hidden");
					$(unMergeBtn).remove();
				}
		}); 
		
		$("#unMergePopUpAlert").modal('hide');
	});
	
	// Handle click on "Select all" control
	   $('#selectAll').on('click', function(){
	      // Get all rows with search applied
	      var rows = crossRefMergeTable.rows({ 'search': 'applied' }).nodes();
	      // Check/uncheck checkboxes for all rows in the table
	      $('input[type="checkbox"]', rows).prop('checked', this.checked);
	      
	   });

	   // Handle click on checkbox to set state of "Select all" control
	   $('.crossRefMergeTable tbody').on('change', 'input[type="checkbox"]', function(){
	      // If checkbox is not checked
	      if(!this.checked){
	         var el = $('#selectAll').get(0);
	         // If "Select all" control is checked and has 'indeterminate' property
	         if(el && el.checked && ('indeterminate' in el)){
	            // Set visual state of "Select all" control 
	            // as 'indeterminate'
	            el.indeterminate = true;
	         }
	      }
	      
	   });
	// Handle click on "Select all" control
	   $('#checkAll').on('click', function(){
	      // Get all rows with search applied
	      var rows = crossRefSpitTable.rows({ 'search': 'applied' }).nodes();
	      // Check/uncheck checkboxes for all rows in the table
	      $('input[type="checkbox"]', rows).prop('checked', this.checked);
	      
	   });

	   // Handle click on checkbox to set state of "Select all" control
	   $('#crossRefSpitTable tbody').on('change', 'input[type="checkbox"]', function(){
	      // If checkbox is not checked
	      if(!this.checked){
	         var el = $('#checkAll').get(0);
	         // If "Select all" control is checked and has 'indeterminate' property
	         if(el && el.checked && ('indeterminate' in el)){
	            // Set visual state of "Select all" control 
	            // as 'indeterminate'
	            el.indeterminate = true;
	         }
	      }
	      
	   });
	   
	   $(document).on('click', '.addCrossReference', function() {
		  $('.crossrefInfoDiv,.filterByDiv').addClass('hidden');
		  $('.addCrossrefDiv').removeClass('hidden');
		  isCreationMode = true;
		  executionType = "Regular";
		  $("#conditionNameSpan").text('');
	   });
	   
	   
	   $('#autoMergeConditionBtn').click(function() {
		  crossReference.populateCrossReferenceAutoMergeTable(xrefObject);
	   });
	   
	   $('#crossRefAutoMergeTable').on('click', '.seletedPatternType', function(){
			var patternType = $(this).val();
			var $tr = $(this).closest('tr');
			console.log($tr);
			if(patternType == "4"){
				$tr.find('.matchChars').removeClass('hidden');
				$tr.find('.selectPattern').addClass('hidden');
			}else {
				$tr.find('.matchChars').addClass('hidden');
				$tr.find('.selectPattern').removeClass('hidden');
			}
	   
	   });
	   
	   $('#crossrefTable').on('click', '#executeXrefCondition', function(){
		   var xrefConditionId = $(this).data('conditionid');
		   crossReference.runCrossRefCondition(xrefConditionId);
	   });
	   
	   $(document).on('click', '#confirmUnMergeCrossRef', function() {
		   crossReference.archiveCrossReference(xrefId);
	    });
	   
	   $(document).on('click', '#activateCrossRef', function() {
		   crossReference.activateCrossReference(xrefId);
	    });
	  
	   $(document).on('click', '#unMergeCrossRef', function(){
		   var unmergeStatus = $('#unmergeExistingXRef').is(':checked');
		   if(unmergeStatus){
			   executionType = "Delete";
		   }else{
			   executionType = "Regular";
		   }
		   crossReference.unmergeExistingXRefandDelete(xrefId);
	   });
	   
	   $('#crossrefTable').on('click', '.activateCrossrefCondition', function(){
		   $("#activeCrossReferencePopUpAlert").modal('show');
		   xrefId = $(this).attr('data-conditionId');
	   });
	   
	   $("#crossrefTable").on("click",'.deactivateCrossrefCondition',function(){
		   xrefId = $(this).attr('data-conditionId');
		   $("#unmergeExistingXRef").prop( "checked", true );
		   $("#archieveCrossReferencePopUpAlert").modal('show');
	   });
	   
	   $("#filterPackages").on("change",function(){
		   crossReference.getCrossReferenceList();
	   });
	  
	   $(document).on("click",".editXRefDetails",function(){
		 var xrefConditionId = $(this).attr('data-conditionId');
		 $('#xrefId').val(xrefConditionId);
		 crossReference.getCrossReferenceLogsById(xrefConditionId);
	   });
	   
	   $(document).on("click", "#editCrossMergeBtn", function(){
		   var dropConditionStatus = $("#editCrossMergeCbx").is(':checked');
		   var xrefId = $('#xrefId').val();
		   if(dropConditionStatus){
			   executionType = "Edit";
		   }else{
			   executionType = "Regular";
		   }
		   /*if(mergeType == "manual"){
			   crossReference.processManualMerge(); 
		   }else*/ 
		   if(mergeType == "auto"){
			   crossReference.processAutoMerge();
		   }else if(mergeType == "bulk"){
			   crossReference.processBulkMerge();
		   }
		   $("#editCrossReferencePopUpAlert").modal('hide');
	   });
	   
	   $(document).on("click", "#autoSplitBtn", function(){
		   var xrefId = $('#xrefId').val(); 
		   executionType = "Delete";
		   crossReference.autoSplitConditionById(xrefId);
	   });
} 