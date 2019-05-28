var userID = $("#userID").val();

var ilInfoMap = {};
var columnNames = [];
var dimensions = [];
var measures = [];

var hierarchicalList = [];
var hierarchicalLevelObject = [];

var listOfNewElements = [];
var maxLevels;
var maxHierarchicalDepth = 0;

var columnMap = {};
var numberColumnTypes = ["BIGINT", "INT", "SMALLINT", "TINYINT", "MEDIUMINT", "INTEGER","DECIMAL"];

var isCreationMode = false;
var editElement = "";
var editElementdepth = "";
var isFinancialTemplate = false;


function HierarchicalObj(ilId, tableName, columnName, valueType, value, autoValueType, expression, expressionPattern, exclusions, 
		transactionType, isCustomColumn, customValueFormula, children) {
	this.ilId = ilId;
    this.tableName = tableName;
    this.columnName = columnName;
    this.valueType = valueType;
    this.value = value;
    this.autoValueType = autoValueType;
    this.expression = expression;
    this.expressionPattern = expressionPattern;
    this.exclusions = exclusions;
    this.transactionType = transactionType;
    this.isCustomColumn = isCustomColumn;
    this.customValueFormula = customValueFormula;
    if (exclusions) {
    	this.exclusions = exclusions;
    }else{
    	this.exclusions = [];
    }
    if (children) {
    	this.children = children;
    } else {
    	this.children = [];
    }
}

function HierarchicalObjwithName(name, financialTemplateStatus,  hierarchicalObjList){
	this.name = name;
	this.financialTemplateStatus = financialTemplateStatus;
	this.config = hierarchicalObjList;
}

function HierarchicalLevels(level, aliasName){
	this.level = level;
	this.aliasName = aliasName;
}


var headers = {};
var hierarchical = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			$('#newElementsTable tbody').sortable();
			$("#ilId,#ilColumnName").select2(); 
			
			/*$("#ilDimensions").multipleSelect({
				filter : true,
				placeholder : 'Select Dimensions',
			    enableCaseInsensitiveFiltering: true
			});*/
			
			$("#ilMeasures").multipleSelect({
				filter : true,
				placeholder : 'Select Columns',
			    enableCaseInsensitiveFiltering: true
			});
			
			$("#ilColumnValueSelect").multipleSelect({
				filter : true,
				placeholder : 'Select Dimensions',
			    enableCaseInsensitiveFiltering: true
			});
			
			$("#ilColumnValueExclusionSelect").multipleSelect({
				filter : true,
				placeholder : 'Select Exclusions',
			    enableCaseInsensitiveFiltering: true,
			  	  afterSelect: function(values){
			  		var exclusions = $(this).val();
			    	$('.exclusionSpan').text(exclusions.join(','));
			  	  },
			  	  afterDeselect: function(values){
			  		var exclusions = $(this).val();
			    	$('.exclusionSpan').text(exclusions.join(','));
			  	  }
			  	
			});
			
			$(".tool").tooltip({
		          content: function () {
		              return $($(this)).prop('title');
		          }
		    });
			
		},
		getHierarchicalsList : function() {
			var hierarchiclaObj = this;
			var url = "/app/user/" + userID + "/hierarchical";
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
			if (common.validateDataResponse(result)) {
					hierarchiclaObj.populateHierarchicalList(result.object);
				}
			});
		},
		getHierarchicalById : function(id) {
			var hierarchiclaObj = this;
			var url = "/app/user/" + userID + "/hierarchical/" + id;
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					hierarchiclaObj.populateHierarchicalInfo(result.object);
					$("#hierarchicalListDiv").addClass("hidden");
					$("#addDiv").removeClass("hidden");
				}
			});
		},
		
		deleteHierarchicalById : function(id){
			var hierarchiclaObj = this;
			var url = "/app/user/" + userID + "/hierarchical/" + id;
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'DELETE');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					hierarchiclaObj.getHierarchicalsList();
				}
			});
		},
		
		populateHierarchicalList : function (data) {
			var hierarchicalListTbl = $("#hierarchicalListTbl tbody");
			var hierarchicalClone = $("#hierarchicalListTbl tfoot tr");
			hierarchicalListTbl.empty();
			$.each(data, function(index, obj){
				var newTr$ = hierarchicalClone.clone().removeClass("hidden")
				newTr$.find(".id").text(obj.id);
				newTr$.find(".name").text(obj.name);
				newTr$.find(".description").text(obj.description);
				newTr$.find(".status").text(obj.active ? "Active" : "InActive");
				newTr$.find(".hierarchicalEdit").val(obj.id);
				newTr$.find(".mapHierarhy").val(obj.id);
				newTr$.find("#viewHierarchicalResults").attr('href', $("#viewJobResultsUrl").val()+ "/" + obj.id + "/" + obj.name);
				newTr$.find(".hierarchicalDelete").val(obj.id);
				hierarchicalListTbl.append(newTr$);
			});
		},
		
		populateHierarchicalInfo : function (data) {
			$("#id").val(data.id);
			$("#name").val(data.name);
			$("#description").val(data.description);
			 if(data.financialTemplate){
				  $("#hierarchyType").prop("checked",true);
			  }	else{
				  $("#hierarchyType").prop("checked",false);
			  }
			 isFinancialTemplate = data.financialTemplate;
			common.clearValidations(["#name", "#description","#addHierarchy", "#printMsg" ]);
			hierarchicalList = JSON.parse(data.hierarchicalFormData); // re populate from db
			hierarchicalLevelObject = JSON.parse(data.hierarchicalLevelData);
			hierarchical.populateHierarchyStructure($("#addHierarchy"),"",0,hierarchicalList);
			hierarchical.evenOddBgSetup();
		},
		
		saveHierarchical : function() {
			var hierarchicalObj = this;
			var hierarchicalId = $("#id").val();
			var hierarchicalName = $("#name").val().trim();
			var hierarchicalDescription = $("#description").val();
			common.clearValidations(["#name", "#description","#addHierarchy", "#printMsg" ]);
			
			if ( !hierarchicalId || hierarchicalId.length == 0) {
				hierarchicalId = "0";
			}
			if (hierarchicalName.length == 0) {
				common.showcustommsg("#name", "Please enter hierarchy name");
				return false;
			}
			if (hierarchicalList.length == 0) {
				common.showcustommsg("#addHierarchy", "Please create hierarchy");
				return false;
			}
			
			/*var checkDBStatus = true;
			for(var i = 0; i< hierarchicalList.length ; i++){
				if(hierarchicalList[i].children.length != 0){
						checkDBStatus = hierarchicalObj.checkAutopopulateFromDB(hierarchicalList[i].children);
				} else if( !hierarchicalList[i].tableName ){
					common.showcustommsg("#printMsg", hierarchicalList[i].value + " is not populated from database");
					checkDBStatus =  false;
					return false;
				} 
			}
			
			if (checkDBStatus == "false") {
				return false;
			}*/
			hierarchicalObj.populateHierarchylevelPopup(hierarchicalId);
		},
		
		populateHierarchylevelPopup : function(hierarchicalId){
			var hierarchicalObj = this;
			var levelDivElement = $("#hierarchylevelDiv");
			maxHierarchicalDepth = 0;
			$.each(hierarchicalList, function(i, obj){
				var tempdepthOfObj = hierarchicalObj.getDepth(obj);
				if(tempdepthOfObj > maxHierarchicalDepth){
					maxHierarchicalDepth = tempdepthOfObj;
				}
			});
			
			levelDivElement.empty();
			$(".hierarchylevelheader").text("Please Enter the LevelNames");
			
				for(var i = 0 ; i < maxHierarchicalDepth; i++){
					
					var newLevelDiv = $(".hierarchylevelcloneDiv").clone();
						newLevelDiv.removeClass("hierarchylevelcloneDiv hidden");
						newLevelDiv.find(".levelNameSpan").text("Group / Level "+i);
						newLevelDiv.find(".levelaliasname").removeClass("levelaliasname").addClass("levelaliasname"+i);
						if(hierarchicalLevelObject != null && i < hierarchicalLevelObject.length ){
							var obj = hierarchicalLevelObject[i];
							newLevelDiv.find(".levelaliasname"+i).val(obj.aliasName);
						}
						$("#hierarchylevelDiv").append(newLevelDiv);
				}
			$("#hierarchyLevelPopup").modal("show");
		},
		
		getDepth :  function(obj){
			var hierarchicalObj = this;
			    var depth = 0;
			    if (obj.children) {
			        obj.children.forEach(function (d) {
			            var tmpDepth = hierarchicalObj.getDepth(d);
			            if (tmpDepth > depth) {
			                depth = tmpDepth;
			            }
			        })
			    }
			    return 1 + depth
		},
		
		checkAutopopulateFromDB : function(listObject) {
			var hierarchicalObj = this;
		 	    for (var property in listObject) {
			        if (listObject.hasOwnProperty(property)) {
			            if (listObject[property].children.length == 0){
			            	if( !listObject[property].tableName ){
			            		common.showcustommsg("#printMsg", listObject[property].value + " is not populated from database");
			            		status = false;
			            		break;
			            	}
			            }else{
			            	hierarchicalObj.checkAutopopulateFromDB(listObject[property].children);
			            }
			        }
			    }
		 	    return status;
		},
		
		resetHierarchyForm :function(){
			
		},
		
		getIlsList : function() {
			var hierarchiclaObj = this;
			var url = "/app/user/" + userID + "/hierarchical/ilList";
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					$.each(result.object, function(i,obj) {
						ilInfoMap[obj.iL_id] = obj;
					});
					hierarchiclaObj.populateIlInfoWithList();
				}
			});
		},
		
		populateIlInfo: function(){
			if ( !$.isEmptyObject(ilInfoMap) ) {
				this.populateIlInfoWithList();
			} else {
				this.getIlsList();
			}
		},
		
		populateIlInfoWithList: function(){
			var ilIdSelectBox = $("#ilId");
			var mapIlIdSelectBox = $("#mapIlId");
			
			ilIdSelectBox.empty();
			mapIlIdSelectBox.empty();
			
			common.populateComboBox(ilIdSelectBox,"0","Select IL/DL");
			common.populateComboBox(mapIlIdSelectBox,"0","Select IL/DL");
			
			ilIdSelectBox.select2();
			mapIlIdSelectBox.select2();
			
			ilIdSelectBox.select2("val","0");
			mapIlIdSelectBox.select2("val", "0");
			
			$.each(ilInfoMap,function(key,obj){
				common.populateComboBox(ilIdSelectBox,obj.iL_id,obj.iL_name);
			});
			
			$.each(ilInfoMap,function(key,obj){
				common.populateComboBox(mapIlIdSelectBox,obj.iL_id,obj.iL_name);
			});
			
			ilIdSelectBox.select2();
			mapIlIdSelectBox.select2();
		},
		
		getColumnsInfo : function() {
			var ilId = $("#ilId").val();
			var hierarchiclaObj = this;
			var url = "/app/user/" + userID + "/hierarchical/columnInfo?tablename=" + ilInfoMap[ilId].iL_table_name;
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					columnNames = result.object.columnsNames;
					columnMap = result.object.columnDataTypes;
					hierarchiclaObj.populateSplitComboBox();
				}
			});
		},
		
		populateSplitComboBox : function() {
			var _selector = $("[name='ilColumnName']");
			_selector.empty();
			common.populateComboBox(_selector,"0","Select Dimension");
			$.each(columnNames, function(i, colName){
				_selector.append($("<option>", {
					text : colName,
					value : colName
				}));
			});
			_selector.select2();
		},

		populateHierarchyStructure : function(parentElement,parentId,depthLength,dataArray){
			parentElement.empty();
			
			var indexSpaces = "";
			for (var i = 0; i < depthLength; i++) {
				indexSpaces += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
			}
			for (var a=0; a < dataArray.length; a++) {
				
				var newParentId = ""; 
				if (parentId == "") {
					newParentId = a + "";
				} else {
					newParentId = parentId + "_" + a ;
				}
				var newParentDiv = $(".parentCloneDiv").clone();
				newParentDiv.removeClass("parentCloneDiv hidden");
				newParentDiv.find(".child-block").removeClass("hidden");
				parentElement.append(newParentDiv);
				newParentDiv.prop("id", newParentId);
				newParentDiv.attr("data-depth",newParentId);
				newParentDiv.find(".tab-alignment").html(indexSpaces);
				var appendPattern  = "";
				if(dataArray[a].expressionPattern.length != 0){
					appendPattern = " ( "+ dataArray[a].expressionPattern + " )";
				}
				newParentDiv.find(".element-name-span").text(dataArray[a].value + appendPattern);
				if (parentId != "") {
					parentElement.parent().find(".parent-block > .symbol-span:first").show();
					parentElement.parent().find(".parent-block > .space-span:first").hide();
				}
				if (dataArray[a].children.length > 0) {
					newParentDiv.find(".symbol-span").show();
					this.populateHierarchyStructure(newParentDiv.find(".child-block"), newParentId, depthLength+1, dataArray[a].children);
				} else {
					newParentDiv.find(".symbol-span").hide();
				}
			}
		},
		
		evenOddBgSetup : function() {
			$('#addHierarchy .parent-block:not(.hidden)').each(function(index,ele){
				$(ele).removeClass("even odd");
				if (index % 2 == 0) {
					$(ele).addClass('odd');
				} else {
					$(ele).addClass('even');
				}
			});
		},
		
		cleanupModelWindow : function() {
			
			var hierarchicalObj = this;
			hierarchical.populateIlInfo();
            listOfNewElements = [];
            common.clearValidations(["#ilId", "#ilColumnName","#ilColumnValue" ]);
            $("#newElementsTable tbody").empty();
            $(".newElementsTableDiv").show();
            if(isCreationMode) {
                $("#hierarchyId").val("");
                $("#autoDropDownCheckBox").prop("checked",true);
                $("#autoDropDownCheckBox").click();
                $('#inputtextDiv').show();
                $('#ilColumnValue').val("");
        		$('#select2Div').hide();
        		$("#ilId").prop('disabled', true);
        		$("#ilColumnName").prop('disabled', true);
                $("#ilColumnName").empty();
                $("#ilColumnName").select2();
                $("input[name='valueOptionSelection'][value='Manual']").attr("checked","checked");
                $("#createHierarchyBtn").prop("disabled","disabled")
                $(".addCloumnValueDiv").show();
                $("#ilTransactionTypeDiv").hide();
                $('#ilPattrenValue').val("");
                $('.exclusionSpan').text("");
            	$('.exclusionDataDiv').hide();
            	$("#customValueChkbx").prop("checked",true);
            	$("#customValueChkbx").click();
                $("#createNewHierarchyPopUp").modal("show");
            }
            else
              {
            	$("#createNewHierarchyPopUp").modal("show");
        		var valueType = editElement.valueType;	
        		var value = editElement.value;
        		var tableName = editElement.tableName;
        		var ilId = editElement.ilId;
        		var columnName = editElement.columnName;
        		var autoValueType = editElement.autoValueType;
        		var expression = editElement.expression;
        		var expressionPattern = editElement.expressionPattern;
        		var exclusions = editElement.exclusions;
        		var transactionType = editElement.transactionType;
        		var isCustomColumn = editElement.isCustomColumn;
        		var customValueFormula = editElement.customValueFormula;
        		
	                if(valueType == "auto"){
	                	$('#ilColumnValue').val("");
	                	$('#inputtextDiv').hide();
	                	$("#autoDropDownCheckBox").prop("checked",false);
	    	            $("#autoDropDownCheckBox").click();
		    	    		setTimeout(function(){
		    	    			$("#ilId").val(ilId).select2().trigger('change');
		    	            }, 500);
	    	            
		    	            setTimeout(function(){
		    	            	 $("[name='ilColumnName']").val(columnName).select2().trigger('change');
		    	            }, 1000);
	    		            if(autoValueType == "pattern"){
	    		            	$("input[name='valueOptionSelection'][value='Pattern']").attr("checked",true).trigger('click');
	    		            	$("#patternTextDiv").show();
	    		            	$('#ilPattrenValue').val(expression);
	    		            	$('#ilColumnValue').val("");
	    	    		        $("#patternOptionsSelect").val(expressionPattern);
	    	    		        if(isFinancialTemplate){
	    		            		$('#ilTransactionTypeDiv').show();
	    		            		$('#ilTransactionTypeSelect').val(transactionType);
	    		            	}
	    	    		        $('#inputtextDiv').hide();
	    	    		        $('#select2Div').hide();
	    	    		        setTimeout(function(){
			    	            	 $(".fetchPatternData").click();
			    	            }, 1500);
	    	    		        
	    	    		        setTimeout(function() {
			    	    		     $("#ilColumnValueExclusionSelect").val(editElement.exclusions).multipleSelect('refresh');
								}, 2000);
	    	    		        
	    	    		        if(isCustomColumn == "true"){
    	    		        	    $('#customValueFormula').val(customValueFormula);
    		            		    $('#customValue').val(value);
    		            		    $('#customValueChkbx').prop('checked', false);
    		            		    $('#customValueChkbx').click();
	    	    		        }else{
	    		            		 $('#customValueChkbx').prop('checked', true);
	    		            		 $('#customValueChkbx').click();
	    		            		 $('.customValueDiv').hide();
	    		            	 }
	    	    		        $('.exclusionSpan').text(exclusions.join(','));
	    	    		        $('.exclusionDataDiv').show();

	    		            }else if(autoValueType == "sourceManual"){	
	    		            	$("input[name='valueOptionSelection'][value='Manual']").attr("checked",true).trigger('click');
	    		            	$('#select2Div').show();
	    		            	$('#ilPattrenValue').val("");
	    		            	var setValue = "";
	    		            	 if(isFinancialTemplate){
	    		            		$('#ilTransactionTypeDiv').show();
	    		            		$('#ilTransactionTypeSelect option:selected').val(transactionType);
	    		            	 }
	    		            	 if(isCustomColumn == "true"){
	    		            		 setValue = customValueFormula.split("/[^\s()*/%+-]+/");
	    		            		 $('#customValueFormula').val(customValueFormula);
	    		            		 $('#customValue').val(value);
	    		            		 $('#customValueChkbx').prop('checked', false);
	    		            		 $('#customValueChkbx').click();
	    		            	 }else{
	    		            		 setValue = value;
	    		            		 $('#customValueChkbx').prop('checked', true);
	    		            		 $('#customValueChkbx').click();
	    		            		 $('.customValueDiv').hide();
	    		            	 }
	    		            	 setTimeout(function() {
    		            			 $("#ilColumnValueSelect").val(setValue);
	    		         	    	 $("#ilColumnValueSelect").multipleSelect('refresh');
								}, 2200);
	    		            	$('.exclusionSpan').text("");
	    		            	$('.exclusionDataDiv').hide();
	    		            }
	    		            	hierarchical.addRowtoTempTable();
	                } 
                else if(valueType == "manual")
                  {
                	$("#autoDropDownCheckBox").prop("checked",true);
                	$("#autoDropDownCheckBox").click();
    	            $("#ilId, #ilColumnName").prop('disabled', true);
    	    		$('#inputtextDiv').show();
    	    		$('#ilColumnValue').val(value);
    	    		$("#patternTextDiv").hide();
    	    		$('.customValueDiv').hide();
    	    		$('#select2Div').hide();
                  }
                $("#createHierarchyBtn").prop("disabled",false);
            }
		},
		
		addRowtoTempTable : function(){
			
			var autoPopulateStatus = $('#autoDropDownCheckBox').is(':checked');	
			var valueType = editElement.valueType;	
    		var value = editElement.value;
    		var tableName = editElement.tableName;
    		var ilId = editElement.ilId;
    		var ilColumnName = editElement.columnName;
    		var autoValueType = editElement.autoValueType;
    		var patternValue = editElement.expression;
    		var expressionValue = editElement.expressionPattern;
    		var exclusions = editElement.exclusions;
    		var transactionType = editElement.transactionType;
    		var isCustomColumn = editElement.isCustomColumn;
    		var customValueFormula = editElement.customValueFormula;
    		
    		var toolTitle = exclusions.join(',');
    		
    		listOfNewElements.push(value);
    		var tr$ = $("<tr>",{class:'cross-mouse'});
    		var ilNameTd$ = $("<td>",{class:'ilName'}).text(tableName);
     		ilNameTd$.append($("<input>", {type:"hidden", class:"ilId", value:ilId}));
     		ilNameTd$.attr('title', toolTitle);
     		tr$.append(ilNameTd$);
    		var td$ = $("<td>",{class:'ilColumnName'}).text(ilColumnName);
    		td$.append($("<input>",{ type:"hidden", class:"ilValueType",value:valueType}));
    		td$.append($("<input>",{ type:"hidden", class:"ilAutoValueType", value: autoValueType}));
    		td$.append($("<input>",{ type:"hidden", class:"ilExclusions", value: exclusions}));
    		td$.append($("<input>",{ type:"hidden", class:"ilPatternValue", value:patternValue}));
    		td$.append($("<input>",{ type:"hidden", class:"ilPattrenExpression", value:expressionValue}));
    		td$.append($("<input>",{ type:"hidden", class:"isCustomColumn", value:isCustomColumn}));
    		td$.append($("<input>",{ type:"hidden", class:"customValueFormula", value:customValueFormula}));
    		tr$.append(td$);
    		tr$.append($("<td>",{class:'columnValue tool', title:toolTitle}).text(value));
    		var selectOptionTd$ = $("<td>");
    		if(autoPopulateStatus && isFinancialTemplate){
    			$("#newElementsTable th:nth-child(4)").show();
        		var select$ = $("<select/>",{class:"form-control transactionTypeSelect"});
        		$("<option/>",{value:"+",text:"+"}).appendTo(select$);
        		$("<option/>",{value:"-",text:"-"}).appendTo(select$);
        		selectOptionTd$.append(select$);
        		selectOptionTd$.append($("<input>", { type:"hidden", class:"ilFinancialStatus", value: isFinancialTemplate}));
        		tr$.append(selectOptionTd$);
    		}
    		var deleteTd$ = $("<td>");
    		deleteTd$.append($("<i>",{class:"fa fa-trash deleteAddedElement", style:"cursor: pointer;"}))
    		tr$.append(deleteTd$);
    		$("#newElementsTable tbody").append(tr$);
		},
		
		cleanupPopup : function() {
			  $("#hierarchyName").val("");
			  $("#hierarchyDesc").val("");
			  $("#hierarchyTypeId").prop('checked', false);
			  $("#createHierarchyNamePopUp").modal("show");
		},
		
		getRequiredArray : function(hierarchyId) {
			hierarchyId += "";
			var selectedArray = hierarchicalList;
			if (hierarchyId != "") {
	    		var hierarchyIdArr = hierarchyId.split("_");
	    		for (var i = 0; i < hierarchyIdArr.length; i++) {
					selectedArray = selectedArray[hierarchyIdArr[i]].children;
				} 
			}
			return selectedArray;
		},
		
		getRequiredObj : function(hierarchyId) {
			hierarchyId += "";
			if (hierarchyId != "") {
	    		var hierarchyIdArr = hierarchyId.split("_");
	    		var selectedObject = hierarchicalList[hierarchyIdArr[0]];
	    		
	    		for (var i = 1; i < hierarchyIdArr.length; i++) {
	    			selectedObject = selectedObject.children[hierarchyIdArr[i]];
				} 
			}
			return selectedObject;
		},
		
		getParentObject : function(hierarchyId) {
			hierarchyId += "";
			if (hierarchyId != "") {
	    		var hierarchyIdArr = hierarchyId.split("_");
	    		var selectedObject = hierarchicalList[hierarchyIdArr[0]];
	    		for (var i = 1; i < hierarchyIdArr.length-1; i++) {
	    			selectedObject = selectedObject.children[hierarchyIdArr[i]];
				} 
			}
			return selectedObject;
		},
		
		getSelectedArrayDepth : function(hierarchyId) {
			hierarchyId += "";
			var selectedArray = hierarchicalList;
			var arrDepth = 0;
			if (hierarchyId != "") {
	    		var hierarchyIdArr = hierarchyId.split("_");
	    		for (var i = 0; i < hierarchyIdArr.length; i++) {
					selectedArray = selectedArray[hierarchyIdArr[i]].children;
					arrDepth++;
				} 
			}
			return arrDepth;
		},
		
		cleanupMapHierarchy : function(hierarchicalId, hierarchicalName){
			hierarchical.populateIlInfo();
			hierarchical.getAssociationData(hierarchicalId);
			$("#maphierarchyId").val(hierarchicalId);
			$("#maphierarchyName").val(hierarchicalName);
			$(".mapHierarchyHeader").text(hierarchicalName);
			$("#ilMeasures").val([]);
			$("#ilMeasures").multipleSelect('refresh');

    		$("#associationName").val("");
			$("#mapHierarchyPopup").modal("show");
		},
		
		getMeasures :function(){
			var ilId = $("#mapIlId").val();
			var hierarchicalObj = this;
			var url = "/app/user/" + userID + "/hierarchical/getMeasures?tablename=" + ilInfoMap[ilId].iL_table_name;
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					measures = result.object.columnsNames;
					columnMap = result.object.columnDataTypes;
					hierarchicalObj.populateMeasures();
				}
			});
		},
		
		populateMeasures :function(){
			var _selector = $("#ilMeasures");
			_selector.empty();
			$.each(measures, function(i, colName){
				_selector.append($("<option>", {
					text : colName,
					value : colName
				}));
			});
			$("#ilMeasures").multipleSelect();
		},
		
		/*getDimensions :function(){
			var hierarchiclaObj = this;
			var ilId = $("#mapIlId").val();
			var hierarchiclaObj = this;
			var url = "/app/user/" + userID + "/hierarchical/getDimensions?tablename=" + ilInfoMap[ilId].iL_table_name;
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					dimensions = result.object.columnsNames;
					hierarchiclaObj.populateDimensions();
				}
			});
		},
		
		populateDimensions :function(){
			var _selector = $("#ilDimensions");
			_selector.empty();
			$.each(dimensions, function(i, colName){
				_selector.append($("<option>", {
					text : colName,
					value : colName
				}));
			});
			$("#ilDimensions").multipleSelect();
		},*/
		
		saveHierarchicalAssosiation: function() {

			var hierarchiclaObj = this
			var ilId = $("#mapIlId").val();
			
			if (ilId == 0) {
		    	common.showcustommsg("#mapIlId+span", "Please select IL");
				return false;
			}
			
			var hierarchyId = $("#maphierarchyId").val();
		    var ilMeasuresArray = $("#ilMeasures").val();
		    //var ilDimensionsArray = $("#ilDimensions").val();
		    var associationName = $("#associationName").val();
		    var associationId = $("#hierarchyMapId").val();
		    var hierarchyName = $("#maphierarchyName").val();
		    var tableName = ilInfoMap[ilId].iL_table_name;
		    
		    //common.clearValidations(["#mapIlId", "#ilMeasures","#ilDimensions","#associationName" ]);
		    common.clearValidations(["#mapIlId+span", "#ilMeasures+div","#associationName" ]);
		    
		    if ( !hierarchyId || hierarchyId.length == 0) {
				hierarchicalId = "0";
			}
			if (!ilMeasuresArray || ilMeasuresArray.length == 0) {
				common.showcustommsg("#ilMeasures+div", "Please select columns");
				return false;
			}
			/*if (!ilDimensionsArray || ilDimensionsArray.length == 0) {
				common.showcustommsg("#ilDimensions", "Please select dimension");
				return false;
			}*/
			if (associationName.length == 0) {
				common.showcustommsg("#associationName", "Please enter source table name");
				return false;
			}
		    
		    var selectedData = {
		    		id : hierarchyId,
		    		name : hierarchyName,
		    		measures : ilMeasuresArray.join(','),
		            //dimensions : ilDimensionsArray.join(','),
		            associationName: associationName,
		            tableName : tableName,
		            associationId : associationId
		    }
		    
		    var url = "/app/user/" + userID + "/hierarchical/saveAssociation";
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'POST',selectedData);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					$("#mapHierarchyPopup").modal("hide");
					common.displayMessages(result.messages);
					hierarchiclaObj.getHierarchicalsList();
					$("#backToHierarchicalList").click();
				}else{
					$("#mapHierarchyPopup").modal("hide");
				}
			});
		},
		
		getAssociationData : function(configId){
			var hierarchiclaObj = this;
			var url = "/app/user/" + userID + "/hierarchical/getHierarchicalAssociationByHierarchyId/"+configId;
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					var data = result.object;
					hierarchiclaObj.populateHierarchicalAssociationInfo(data)
				}
			});
		},
		
		populateHierarchicalAssociationInfo : function (data) {
			if(data != null){
				$("#hierarchyMapId").val(data.associationId);	
			}
			/*$("#mapIlId").val(data.ilId);
			var seletedMeasuresValues = data.measures;
			measures = seletedMeasuresValues.split(',');
			hierarchiclaObj.populateMeasures();*/
		},
		
		runHierarchicalStructure : function (hierarchicalId){
			var hierarchiclaObj = this;
			var url = "/app/user/" + userID + "/hierarchical/run/"+hierarchicalId;
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					common.displayMessages(result.messages);
					hierarchiclaObj.getHierarchicalsList();
					$("#backToHierarchicalList").click();
				}
			});	
		},
		
		getColumnValues : function(){
			var hierarchicalObj = this;
			var ilId = $("#ilId").val();
			var tableName = ilInfoMap[ilId].iL_table_name;
			var columnName = $("#ilColumnName").val();
			var url = "/app/user/" + userID + "/hierarchical/getDistinctValuesByColumnName?ilId=" + ilId + "&tableName=" + tableName + "&columnName=" + columnName;
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'POST');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					hierarchicalObj.populateColumnValues(result.object);
				}
			});
			
		},
		
		getColumnValuesInRange : function(fromrange, torange){
			var hierarchicalObj = this;
			var ilId = $("#ilId").val();
			var tableName = ilInfoMap[ilId].iL_table_name;
			var columnName = $("#ilColumnName").val();
			var url = "/app/user/" + userID + "/hierarchical/getDistinctValuesinRange?ilId=" + ilId + "&tableName=" + tableName + "&columnName=" + columnName +"&fromRange=" + fromrange +
			   "&toRange="+torange;
			showAjaxLoader(true);
			var myAjax = common.loadAjaxCall(url, 'POST');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if (common.validateDataResponse(result)) {
					hierarchicalObj.populateColumnValuestoCreateNodes(result.object);
				}
			});
		},
		
		populateColumnValues :function(obj){
			var _selector = $("#ilColumnValueSelect");
			_selector.empty();
			$.each(obj, function(i, colValue){
				_selector.append($("<option>", {
					text : colValue,
					value : colValue
				}));
			});
			$("#ilColumnValueSelect").multipleSelect();
		},
		
		populateColumnValuestoCreateNodes : function(objValues) {
			var ilId = $("#ilId").val();
			var tableName = ilInfoMap[ilId].iL_table_name;
			var ilName = ilInfoMap[ilId].iL_name;
			var ilColumnName = $("#ilColumnName").val();
    		var valueType = "auto";
    		var autoValueType = $(".valueOptionSelection:checked").val();
    		var ilExclusions = $('#ilColumnValueExclusionSelect').val();
    		var financialStatus = $("#hierarchyType").is(':checked');
    		var patternValue = $("#ilPattrenValue").val();
    		var expressionValue = $("#patternOptionsSelect").val();
    	
			var customValueStatus = $("#customValueChkbx").is(':checked');
		
			var columnValueList = "";
			if(customValueStatus){
				hierarchical.populateCustomSeletedValues(objValues);
				$(".addCloumnValueDiv").show();
			}else{
				columnValueList = objValues;
				$("#customValue").val("");
				$("#customValueFormula").val("");
				$(".customValueDiv").hide();
			}
    		var toolTitle = ilExclusions == (undefined || null) ? null : ilExclusions.join(',');

        	$.each(columnValueList,function(index,obj){
        		
        		listOfNewElements.push(obj);
        		var tr$ = $("<tr>",{class:'cross-mouse'});
        		var ilNameTd$ = $("<td>",{class:'ilName tool'}).text(tableName);
        		ilNameTd$.append($("<td>", {type:"hidden", class:"ilId", value:ilId}));
        		ilNameTd$.attr('title', toolTitle);
        		tr$.append(ilNameTd$);
        		var td$ = $("<td>",{class:'ilColumnName'}).text(ilColumnName);
        		td$.append($("<input>",{ type:"hidden",	class:"ilValueType",value:valueType}));
        		td$.append($("<input>",{ type:"hidden", class:"ilAutoValueType", value: autoValueType}));
        		td$.append($("<input>",{ type:"hidden", class:"ilExclusions", value: ilExclusions}));
        		td$.append($("<input>",{ type:"hidden", class:"ilPatternValue", value:patternValue}));
        		td$.append($("<input>",{ type:"hidden", class:"ilPattrenExpression", value:expressionValue}));
        		tr$.append(td$);
        		tr$.append($("<td>",{class:'columnValue tool', title:toolTitle}).text(obj));
        		var selectOptionTd$ = $("<td>");
        		if(financialStatus){
        			$("#newElementsTable th:nth-child(4)").show();
            		var select$ = $("<select/>",{class:"form-control transactionTypeSelect"});
            		$("<option/>",{value:"+",text:"+"}).appendTo(select$);
            		$("<option/>",{value:"-",text:"-"}).appendTo(select$);
            		selectOptionTd$.append(select$);
            		selectOptionTd$.append($("<input>", { type:"hidden", class:"ilFinancialStatus", value: financialStatus}));
            		tr$.append(selectOptionTd$);
        		}
        		var deleteTd$ = $("<td>");
        		deleteTd$.append($("<i>",{class:"fa fa-trash deleteAddedElement", style:"cursor: pointer;"}))
        		tr$.append(deleteTd$);
        		
        		$("#newElementsTable tbody").append(tr$);
        		
        	});
        	$("#createHierarchyBtn").removeAttr("disabled")
        	$("#ilColumnValueSelect").val([]);
        	$("#ilColumnValueSelect").multipleSelect('refresh');
        	$('#ilColumnValue').val("");
		},
		
		populateColumnValuestoCreateExclusions : function(objValues){
			
			var _selector = $("#ilColumnValueExclusionSelect");
			_selector.empty();
			$.each(objValues, function(i, colValue){
				_selector.append($("<option>", {
					text : colValue,
					value : colValue
				}));
			});
			$("#ilColumnValueExclusionSelect").multipleSelect();
			$('.exclusionDiv').show();
		},
		
		createHierarchicalLevelObj : function() {
			var hierarchiclaObj = this;
			var hierarchicalId = $("#id").val();
			var hierarchicalName = $("#name").val().trim();
			var hierarchicalDescription = $("#description").val();
			var financialStatus = $("#hierarchyType").is(':checked');
			var status = true;
			 common.clearValidations(["#hierarchylevelDiv"]);
             hierarchicalLevelObject = [];
            var regex = /^([a-zA-Z0-9_])+$/;
            
			for(var i = 0; i < maxHierarchicalDepth; i++){
				common.clearValidations([".levelaliasname"+i]);
				var level = i;
				var levelName = $(".levelaliasname"+i).val();
				if(levelName.length == 0){
					common.showcustommsg(".levelaliasname"+i, "Please enter Level "+i);
					status = false;
					break;
				}else if(! regex.test(levelName) ){
	         		common.showcustommsg(".levelaliasname"+i, globalMessage['anvizent.package.message.specialCharacterunderscoreonlyAllowed']);
	         		status = false;
	                 break;
	         	}
				else{
					 var hierarchicallevelObj = new HierarchicalLevels(level, levelName);
					 hierarchicalLevelObject.push(hierarchicallevelObj);
				}
			}
			if(!status){
			  return false;
			}	
			var hierarchicalObjWithName = new HierarchicalObjwithName(hierarchicalName, financialStatus, hierarchicalList);
				var selectObj = {
						id : hierarchicalId,
						name : hierarchicalName,
						description : hierarchicalDescription,
						active: true,
						hierarchicalFormData : JSON.stringify(hierarchicalObjWithName),
						hierarchicalLevelData : JSON.stringify(hierarchicalLevelObject)
				}
				var url = "/app/user/" + userID + "/hierarchical";
				showAjaxLoader(true);
				var myAjax = common.loadAjaxCall(url, 'POST',selectObj);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if (common.validateDataResponse(result)) {
						$("#hierarchyLevelPopup").modal("hide");
						$("#backToHierarchicalList").click();
						common.displayMessages(result.messages);
						hierarchiclaObj.getHierarchicalsList();
					}
				});	    	
		 },
	    	
	    	getIlColumnPatternValues : function(){
	    		var hierarchicalObj = this;
	    		 common.clearValidations(["#ilId", "#ilColumnName","#ilPattrenValue" ]);
	    		var ilId = $("#ilId").val();
	    		
	    		if ( !ilId || ilId == 0) {
    				common.showcustommsg("#ilId", "Please select IL/DL");
    				return false;
    			}
				var tableName = ilInfoMap[ilId].iL_table_name;
				var columnName = $("#ilColumnName").val();
				var patternValue = $('#ilPattrenValue').val();
    			var patternRangeValue = $('#patternOptionsSelect').val();
    			var customValueStatus = $("#customValueChkbx").is(':checked');
    			
    			if (!columnName || columnName == 0) {
    				common.showcustommsg("#ilColumnName", "Please select Dimension");
    				return false;
    			}
    			if (!patternValue || patternValue == 0) {
    				common.showcustommsg("#ilPattrenValue", "Please enter pattern");
    				return false;
    			}
    			var url = "/app/user/" + userID + "/hierarchical/getIlColumnPatternValues?ilId=" + ilId + "&tableName=" + tableName + "&columnName=" + columnName + "&patternValue=" + patternValue + "&patternRangeValue=" +patternRangeValue;
    			showAjaxLoader(true);
    			var myAjax = common.loadAjaxCall(url, 'POST');
    			myAjax.done(function(result) {
    				showAjaxLoader(false);
    				if (common.validateDataResponse(result)) {
    					hierarchicalObj.populateColumnValuestoCreateExclusions(result.object);
    					if(customValueStatus)
    					    hierarchical.populateCustomSeletedValues(result.object);
    				}
    			});
	    	},
	    	
	    	prepareEditHierarchyObject : function(){
	    		
	    		var hierarchicalObj = this;
	    		
	    		var selectedObj = hierarchical.getRequiredObj($("#hierarchyId").val());
	    		var ilId = $("#ilId").val();
	        	var ilColumnName = $("#ilColumnName").val();
	        	var columnValue = "";
	        	var columnValueElement = null;
	        	var tableName = "";
	        	var ilName = "";
	        	var isValid = false;
	        	var valueType = "manual";
	        	var autoValueType = ""; 
	        	var exclusions = [];
	        	var financialStatus = $("#hierarchyType").is(':checked');
	        	var expressionValue = "";
	        	var transactionType = $("#ilTransactionTypeSelect").val();
	        	if (!ilColumnName || ilColumnName == "0") {
	        		ilColumnName = "";
	        	}
	        	common.clearValidations(["#ilColumnValueSelect + span", "#ilColumnValue" ]);
	        	
	        	var autoPopulateStatus = $('#autoDropDownCheckBox').is(':checked');
	        	
	        	if (autoPopulateStatus) {
	        		var columnValueOption = $(".valueOptionSelection:checked").val();
	        		tableName = ilInfoMap[ilId].iL_table_name;
	        		ilName = ilInfoMap[ilId].iL_name;
	        		valueType = "auto"; 
	        		
	        		if(columnValueOption.toLowerCase() == "pattern"){
	        			autoValueType = "pattern";
	        		    columnValueElement = $('#ilPattrenValue');
	        		    columnValue = $('#ilPattrenValue').val();
	        		    expressionValue = $('#patternOptionsSelect').val();
	        			exclusions = $('#ilColumnValueExclusionSelect').val();
	        			if(exclusions == null){
	        				exclusions = [];
	        			}
	        			if ( !columnValue ) {
	               		 	common.showcustommsg(columnValueElement, "value should not be null");
	               		 	return false;
	            		}
	        			
	        		}else if(columnValueOption.toLowerCase() == "manual"){
	        			autoValueType = "sourceManual"; 
	        			columnValueElement = $("#ilColumnValue");
	        			columnValue = $('#ilColumnValue').val();
	        			if ( !columnValue ) {
	               		 	common.showcustommsg(columnValueElement, "value should not be null");
	               		 	return false;
	            		}
	        		}
	        	} else {
	        		tableName = "";
	        		ilColumnName = "";
	        		columnValueElement = $('#ilColumnValue');
	        		columnValue = $('#ilColumnValue').val();
	        		if ( !columnValue || columnValue.trim().length == 0 ) {
	           		 	common.showcustommsg(columnValueElement, "value should not be null");
	           		 	return false;
	        		}
	        	}
	        	selectedObj.valueType = valueType;
	        	selectedObj.value = columnValue.trim();
	        	selectedObj.tableName = tableName;
	        	selectedObj.ilId = ilId;
	        	selectedObj.columnName = ilColumnName;
	        	selectedObj.autoValueType = autoValueType;
	        	selectedObj.expression = columnValue;
	        	selectedObj.expressionPattern = expressionValue;
	        	selectedObj.exclusions = exclusions;
	        	selectedObj.transactionType = transactionType;
	        	
	        	if (isValid) {
	        		return false;
	        	}
	        	hierarchical.populateHierarchyStructure($("#addHierarchy"),"",0,hierarchicalList);
				hierarchical.evenOddBgSetup();
	        	
	        	$("#createHierarchyBtn").removeAttr("disabled")
	        	$("#ilColumnValueSelect").val([]);
	        	$("#ilColumnValueSelect").multipleSelect('refresh');
	        	$('#ilColumnValue').val("");
	        	$('#ilPattrenValue').val("");
	        	$("#patternOptionsSelect").val($("#patternOptionsSelect option:first").val());
	        	$("#ilColumnValueExclusionSelect").val([]);
	        	$("#ilColumnValueExclusionSelect").multipleSelect('refresh');
	    		$('.exclusionDiv').hide();
	    		editElementdepth = "";
	    		$("#hierarchyId").val("");
	    	},
	    	
	    	populateCustomSeletedValues : function(selectedValues){
	    		var buildRegexBySelectedValues = "";
	    		$.each(selectedValues, function(idx, val){
	    			 if(idx < selectedValues.length-1)
	    				 buildRegexBySelectedValues += val +" + ";
	    			 else
	    				 buildRegexBySelectedValues += val;
	    		});
	    		  var dataTr$ = $("#newElementsTable tbody tr");
		    		dataTr$.each(function(index,toBeDeletedRow){
		    		  var value = $(toBeDeletedRow).find(".columnValue").text();
		    		  if(selectedValues != null && selectedValues.indexOf(parseInt(value, 10)) > -1){
		    			  toBeDeletedRow.remove();
		    		  }
	    	    	  if (listOfNewElements.indexOf(value) > -1) {
	    	    		listOfNewElements.splice(index, 1);
	    			  }
		    		});
	    		$("#customValueFormula").val(buildRegexBySelectedValues);
	    		$(".customValueDiv").show();
	    	}
};

if($('.hierarchicalData-page').length){
	  hierarchical.initialPage();
	  hierarchical.getHierarchicalsList();
	  
	  $("#addHierarchicalBtn").click(function(){
		  hierarchical.cleanupPopup();
	  });
	  $("#createHierarchyNameBtn").click(function(){
		  hierarchicalList = [];
		  	var hierarchyName = $("#hierarchyName").val().trim();
			var hierarchyDesc = $("#hierarchyDesc").val();
			var hierarchyTemplateType = $('#hierarchyTypeId').is(':checked');
			common.clearValidations(["#hierarchyName", "#hierarchyDesc"]);
			
			if (hierarchyName.length == 0) {
				common.showcustommsg("#hierarchyName", "Please enter hierarchy name");
				return false;
			}	
		  $("#name").val(hierarchyName);
		  $("#description").val(hierarchyDesc); 
		  
		  if(hierarchyTemplateType){
			  $("#hierarchyType").prop("checked","checked");
		  }	else{
			  $("#hierarchyType").prop("checked",false);
		  }
		  
		  $("#createHierarchyNamePopUp").modal("hide");
		  
		  $("#id").val("");
		  $("#addHierarchy").empty();
		  $("#hierarchicalListDiv").addClass("hidden");
		  $("#addDiv").removeClass("hidden");
		  $( "#addHierarchy" ).contextmenu();
		  hierarchicalLevelObject = [];
	  });
	  
	  $("#backToHierarchicalList").click(function(){
		  $("#addDiv").addClass("hidden");
		  $("#hierarchicalListDiv").removeClass("hidden");
	  });
	  
	  $("#saveHierarchical").click(function(){
		  hierarchical.saveHierarchical();
	  });
	  
	  $("#hierarchicalListTbl").on("click",".hierarchicalEdit",function(){
		  var hierarchicalId = this.value;
		  hierarchical.getHierarchicalById(hierarchicalId);
	  });
	  
	  $("#hierarchicalListTbl").on("click",".hierarchicalDelete",function(){
		  var hierarchicalId = this.value;
		  hierarchical.deleteHierarchicalById(hierarchicalId);
	  });
	  
    $.contextMenu({
        selector: '#addHierarchy', 
        callback: function(key, options) {
        	isCreationMode = true;
        	hierarchical.cleanupModelWindow();
        },
        items: {
            "create": {name: "Create", icon: "add"},
        }
    });
    
    $('#addHierarchy').contextMenu({
        selector: '.parent-element .parent-block', 
        callback: function(key, options) {
        	if (key == "new") {
        		isCreationMode = true;
        		hierarchical.cleanupModelWindow();
        		var depth = $($(this).closest(".parent-element")).data("depth");
        		$("#hierarchyId").val(depth);
        		var selectedArray = hierarchical.getRequiredArray(depth);
	        		for (var i = 0; i< selectedArray.length; i++) {
	        			listOfNewElements.push(selectedArray[i].value.trim().toLowerCase()); 
	        		}
	        	}
        		else if (key == "rename"){
        			var depth = $($(this).closest(".parent-element")).data("depth");
        			var divElementId = $("#"+depth);
        			var selectedNode = hierarchical.getRequiredObj(depth);
        			var div$ = $(this).closest(".parent-block");
        			div$.find('.element-name-span').remove();
        			div$.append($("<input>",{ type:"text",class:"inlineNodeEditInput",value:selectedNode.value}));
	        	}
        		else if (key == "edit/view") {
        			isCreationMode = false;
        			depth = $($(this).closest(".parent-element")).data("depth");
        			var divElementId = $("#"+depth);
        			$("#hierarchyId").val(depth);
        			editElement = hierarchical.getRequiredObj(depth);
        			hierarchical.cleanupModelWindow();
        		}
        		else if (key == "delete"){
        			var depth = $($(this).closest(".parent-element")).data("depth");
            		$("#hierarchyId").val(depth);
            		var parentArray = hierarchical.getParentObject(depth);
            		var selectedElementHierarchyArr = depth.split("_");
            		var index = selectedElementHierarchyArr[(selectedElementHierarchyArr.length)-1];
            		parentArray.children.splice(index, 1);
            		hierarchical.populateHierarchyStructure($("#addHierarchy"),"",0,hierarchicalList);
            		hierarchical.evenOddBgSetup();
        		}
        },
        items: {
            "new": 	{name: "Add Level", icon: "add"},
            "rename": {name: "Rename", icon: "edit"},
            "edit/view": {name: "Edit/View", icon: "edit"},
            "delete": {name: "Delete", icon:"delete"},
        }
    });
    
    $("#addHierarchy").on("click",".symbol-span",function(){
    	$(this).toggleClass('fa fa-folder-o').toggleClass('fa fa-folder-open-o');
    	
    	if ($(this).hasClass("fa-folder-o")) {
    		$($(this).closest(".parent-element")).find(".child-block:first").addClass("hidden");
    		$($(this).closest(".parent-element")).find(".child-block:first").find(".parent-block").addClass("hidden");
    	} else {
    		$($(this).closest(".parent-element")).find(".child-block:first").removeClass("hidden");
    		$($(this).closest(".parent-element")).find(".child-block:first").find(".parent-block").removeClass("hidden");
    	}
    	hierarchical.evenOddBgSetup();
    });
    
    $("#ilId").change(function(){
    	if (this.value && this.value != "0" ) {
    		hierarchical.getColumnsInfo();
    	}
	});

    $(".rangeValues").select2({
	    minimumInputLength: 1,
	    ajax: {
	        url: "/minidw/app/user/"+$("#userID").val()+"/hierarchical/getDistinctValues", 
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
		            	columnName: $("#ilColumnName").val(),
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
    
    $('#autoDropDownCheckBox').click(function(){
    	common.clearValidations(["#ilId", "#ilColumnName","#ilPattrenValue" ]);
    	var fromSourceStatus = $("#autoDropDownCheckBox").is(":checked");
    	$(".selectRangeDiv").hide();
		$('.exclusionDiv').hide();
    	if(isCreationMode)
	      {
    		$("#selectRange").prop("checked",true);
    		$("#selectRange").click();    		
    		$('.addCloumnValueDiv').show();
    		$('.newElementsTableDiv').show();
    		if(fromSourceStatus){
	    		$(".valueOptionsDiv").show();
	    		$('#inputtextDiv').hide();
	    		$("#ilColumnName,#ilId").prop('disabled', false);
	    		$("input[name='valueOptionSelection'][value='Manual']").prop("checked","checked");
	    		$("#patternTextDiv").hide();
	     		$('#select2Div').show();
	     		$(".customValueChkbxDiv").show();
	    	}else{
	    		$('#inputtextDiv').show();
	    		$(".valueOptionsDiv").hide();
	    		$('#select2Div').hide();
	    		$("#ilColumnName,#ilId").prop('disabled', true);
	    		$("#patternTextDiv").hide();
	    		$(".customValueChkbxDiv").hide();
	    		$("#customValueChkbx").prop("checked",true);
            	$("#customValueChkbx").click();
	    	}
    	  }
    	 else
    	  {
    		 if(fromSourceStatus){
    			 var selectedOption = $("input[name='valueOptionSelection']:checked").val();
    			 $("#ilColumnName,#ilId").prop('disabled', false );
    			 $(".valueOptionsDiv").show();
    			 if(selectedOption != undefined && selectedOption != null && selectedOption.toLowerCase() == "pattern"){
    				 $("#patternTextDiv").show();
    				 $('#inputtextDiv').hide();
    				 $('#select2Div').hide();
    				 $(".exclusionDataDiv").show();
    			 }else{
    	 	    	 $('#inputtextDiv').hide();
    	 	    	 $('#select2Div').show();
    	 	    	 $("#patternTextDiv").hide();
    	 	    	 $(".exclusionDataDiv").hide();
    			 }
 	    		if(isFinancialTemplate){
            		$('#ilTransactionTypeDiv').show();
            		$('#ilTransactionTypeSelect option:first').val();
            	}
 	    	}else{
 	    		$(".valueOptionsDiv").hide();
 	    		$('#inputtextDiv').show();
 	    		$("#patternTextDiv").hide();
 	    		$("#ilColumnName,#ilId").prop('disabled', true);
 	    		$(".exclusionDataDiv").hide();
 	    		$('#select2Div').hide();
 	    		if(isFinancialTemplate){
            		$('#ilTransactionTypeDiv').show();
            		$('#ilTransactionTypeSelect option:first').val();
            	}
 	    	}
    	  }
    });
    
    $('.addCloumnValue').click(function(){
    	
    	var autoPopulateStatus = $('#autoDropDownCheckBox').is(':checked');
    	var ilId = $("#ilId").val();
    	var ilColumnName = $("#ilColumnName").val();
    	var columnValueList = [];
    	var columnValueElement = null;
    	var tableName = "";
    	var ilName = "";
    	var isValid = false;
    	var valueType = "manual";
    	var autoValueType = ""; 
    	var exclusions = [];
    	var financialStatus = $("#hierarchyType").is(':checked');
    	var expressionValue = "";
    	var customValueName = "";
		var customValueFormula = "";
    	
    	common.clearValidations(["#ilColumnValueSelect + span", "#ilColumnValue", "#ilId", "#ilColumnName", "#customValue", "#customValueFormula"]);
    	
        if(autoPopulateStatus){
        	if ( ilId == 0) {
       		 	common.showcustommsg("#ilId", "Please Select IL / DL");
       		 	return false;
    		}
        	if (!ilColumnName || ilColumnName == "0") {
       		 	common.showcustommsg("#ilColumnName", "Please Select dimension");
       		 	return false;
    		}
        }
    	if (!ilColumnName || ilColumnName == "0") {
    		ilColumnName = "";
    	}
    	if (autoPopulateStatus) {
    		var columnValueOption = $(".valueOptionSelection:checked").val();
    		var customColumnStatus = $("#customValueChkbx").is(":checked");
    		
    		tableName = ilInfoMap[ilId].iL_table_name;
    		ilName = ilInfoMap[ilId].iL_name;
    		valueType = "auto"; 
    		
    		if(columnValueOption.toLowerCase() == "pattern"){
    			autoValueType = "pattern";
		    	 if(customColumnStatus){
		    		 	 columnValueElement = $("#customValue");
		   				 customValueName = $("#customValue").val();
		   				 customValueFormula = $("#customValueFormula").val();
		   		    	 var regex = /^([a-zA-Z0-9\+\-\*\/\s])+$/;
		   		    	if(customValueName.trim().length == 0){
		   		    		common.showcustommsg("#customValue", "Please enter custom value name");
		   			        return false;
		   		    	}	
		   		    	if ( customValueFormula.trim().length == 0 ) {
		   					common.showcustommsg("#customValueRegxId", "Please enter custom value expression");
		   			        return false;
		   				}else if(! regex.test(customValueFormula) ){
		   					common.showcustommsg("#customValueFormula", "Only allowed arithmetic operators");
		   			        return false;
		   				}
		   		    	columnValueList.push(customValueName);
		   				if ( !columnValueList ) {
		              		 	common.showcustommsg("#customValueRegxId", "value should not be null");
		              		 	return false;
		           		}
		   			}else{
		   				columnValueElement = $('#ilPattrenValue');
		    			var patternValue = $('#ilPattrenValue').val();
		    			expressionValue = $('#patternOptionsSelect').val();	
		    			exclusions = $('#ilColumnValueExclusionSelect').val();
		    			if(exclusions == null){
		    				exclusions = [];
		    			}
		   				columnValueList.push(patternValue);
		   				if ( !columnValueList ) {
		              		 	common.showcustommsg(columnValueElement, "value should not be null");
		              		 	return false;
		           		}
		   			}
    			if ( !columnValueList ) {
           		 	common.showcustommsg(columnValueElement, "value should not be null");
           		 	return false;
        		}
    			
    		}else if(columnValueOption.toLowerCase() == "manual"){
    			autoValueType = "sourceManual"; 
    			
    			if(customColumnStatus){
    				columnValueElement = $("#customValue");
    				 customValueName = $("#customValue").val();
    				 customValueFormula = $("#customValueFormula").val();
    		    	 var regex = /^([a-zA-Z0-9\+\-\*\/\s])+$/;
    		    	if(customValueName.trim().length == 0){
    		    		common.showcustommsg("#customValue", "Please enter custom value name");
    			        return false;
    		    	}	
    		    	if ( customValueFormula.trim().length == 0 ) {
    					common.showcustommsg("#customValueRegxId", "Please enter custom value expression");
    			        return false;
    				}else if(! regex.test(customValueFormula) ){
    					common.showcustommsg("#customValueFormula", "Only allowed arithmetic operators");
    			        return false;
    				}
    		    		columnValueList.push(customValueName);
    				if ( !columnValueList ) {
               		 	common.showcustommsg("#customValueRegxId", "value should not be null");
               		 	return false;
            		}
    			}else{
    				columnValueElement = $("#ilColumnValueSelect");
    				columnValueList = $('#ilColumnValueSelect').val();
    				if ( !columnValueList ) {
               		 	common.showcustommsg(columnValueElement, "value should not be null");
               		 	return false;
            		}
    			}
    		}
    	} else {
    		tableName = "";
    		ilColumnName = "";
    		columnValueElement = $('#ilColumnValue');
    		var selectedValue = $('#ilColumnValue').val();
    		if ( !selectedValue || selectedValue.trim().length == 0 ) {
       		 	common.showcustommsg(columnValueElement, "value should not be null");
       		 	return false;
    		}
    		columnValueList.push(selectedValue);
    	}
    	
    	$.each(columnValueList,function(index,obj){
    		var existIndex = listOfNewElements.indexOf(obj.toLowerCase());
        	if (existIndex != -1) {
        		common.showcustommsg(columnValueElement, obj + " value already exist in the below list / Hierarchy");
        		isValid = true;
        		return;
        	}
    	});
    	
    	if (isValid) {
    		return false;
    	}
    	
    	var toolTitle = exclusions.join(',');
    	$.each(columnValueList,function(index,obj){
    		
    		listOfNewElements.push(obj.toLowerCase());
    		var tr$ = $("<tr>",{class:'cross-mouse'});
    		var ilNameTd$ = $("<td>",{class:'ilName'}).text(tableName);
     		ilNameTd$.append($("<input>", {type:"hidden", class:"ilId", value:ilId}));
     		ilNameTd$.attr('title', toolTitle);
     		tr$.append(ilNameTd$);
    		var td$ = $("<td>",{class:'ilColumnName'}).text(ilColumnName);
    		td$.append($("<input>",{ type:"hidden", class:"ilValueType",value:valueType}));
    		td$.append($("<input>",{ type:"hidden", class:"ilAutoValueType", value: autoValueType}));
    		td$.append($("<input>",{ type:"hidden", class:"ilExclusions", value: exclusions}));
    		td$.append($("<input>",{ type:"hidden", class:"ilPatternValue", value:patternValue}));
    		td$.append($("<input>",{ type:"hidden", class:"ilPattrenExpression", value:expressionValue}));
    		td$.append($("<input>",{ type:"hidden", class:"isCustomColumn", value:customColumnStatus}));
    		td$.append($("<input>",{ type:"hidden", class:"customValueFormula", value:customValueFormula}));
    		tr$.append(td$);
    		tr$.append($("<td>",{class:'columnValue tool', title:toolTitle}).text(obj.trim()));
    		var selectOptionTd$ = $("<td>");
    		if(autoPopulateStatus && financialStatus){
    			$("#newElementsTable th:nth-child(4)").show();
        		var select$ = $("<select/>",{class:"form-control transactionTypeSelect"});
        		$("<option/>",{value:"+",text:"+"}).appendTo(select$);
        		$("<option/>",{value:"-",text:"-"}).appendTo(select$);
        		selectOptionTd$.append(select$);
        		selectOptionTd$.append($("<input>", { type:"hidden", class:"ilFinancialStatus", value: financialStatus}));
        		tr$.append(selectOptionTd$);
    		}
    		var deleteTd$ = $("<td>");
    		deleteTd$.append($("<i>",{class:"fa fa-trash deleteAddedElement", style:"cursor: pointer;"}))
    		tr$.append(deleteTd$);
    		$("#newElementsTable tbody").append(tr$);
    	});
    	$("#fromRange").val("");
    	$("#toRange").val("");
    	$("#customValueChkbx").prop("checked",true);
    	$("#customValueChkbx").trigger('click');
    	$("#selectRange").prop("checked",true);
		$("#selectRange").trigger("click");
    	$("#createHierarchyBtn").removeAttr("disabled")
    	$("#ilColumnValueSelect").val([]);
    	$("#ilColumnValueSelect").multipleSelect('refresh');
    	$('#ilColumnValue').val("");
    	$('#ilPattrenValue').val("");
    	$("#patternOptionsSelect").val($("#patternOptionsSelect option:first").val());
    	$("#ilColumnValueExclusionSelect").val([]);
    	$("#ilColumnValueExclusionSelect").multipleSelect('refresh');
		$('.exclusionDiv').hide();
    });
    
    $("#newElementsTable").on("click",".exclusionValView",function(){
    	var checkedStatus  = $(this).is(':checked');
    	if(checkedStatus){
    		var selectedRow = $($(this).closest("tr"));
        	var exclusions = selectedRow.find(".ilExclusions").val();
        		exclusions.split(',');
    	}
    });
    
    $("#newElementsTable").on("click",".deleteAddedElement",function(){
    	var toBeDeletedRow = $($(this).closest("tr"));
    	var selectedValue = toBeDeletedRow.find(".columnValue").text().toLowerCase().trim();
    	toBeDeletedRow.remove()
    	var index = listOfNewElements.indexOf(selectedValue);
    	if (index > -1) {
    		listOfNewElements.splice(index, 1);
		}
    	if ($("#newElementsTable tbody tr").length > 0) {
    		$("#createHierarchyBtn").removeAttr("disabled");
    	} else {
    		$("#createHierarchyBtn").prop("disabled","disabled");
    	}
    });
    
    $(document).on("click","#createHierarchyBtn",function(){
    	if(isCreationMode){
        	var hierarchyId = $("#hierarchyId").val();
        	var dataTr$ = $("#newElementsTable tbody tr");
        	var selectedArray = hierarchical.getRequiredArray(hierarchyId);
        	var depthLength = hierarchical.getSelectedArrayDepth(hierarchyId);
        	var selectedParent = $("#addHierarchy");
        	if (hierarchyId != "") {
        		selectedParent = $("#"+hierarchyId).find(".child-block:first");
        	}
        	dataTr$.each(function(index,obj){
        		var tableName = $(obj).find(".ilName").text();
        		var ilId = $(obj).find(".ilId").val();
        		var columnName = $(obj).find(".ilColumnName").text();
        		var valueType = $(obj).find(".ilValueType").val();
        		var value = $(obj).find(".columnValue").text();
        		var autoValueType = $(obj).find(".ilAutoValueType").val();
        		var exclusionData = $(obj).find(".ilExclusions").val();
        		var financialStatus = $(obj).find(".ilFinancialStatus").val();
        		var expressionValue = $(obj).find(".ilPatternValue").val();
        		var expressionPattern = $(obj).find(".ilPattrenExpression").val();
        		var customValueFormula = $(obj).find(".customValueFormula").val();
        		var isCustomColumn = $(obj).find(".isCustomColumn").val();
        		var transactionType = "";
        		if(financialStatus){
        			 transactionType = $(obj).find(".transactionTypeSelect").val();
        		}
        		exclusionData = exclusionData.split(',');
        		var hierarchicalObj = new HierarchicalObj(ilId, tableName, columnName, valueType, value, autoValueType, expressionValue,
        				           expressionPattern, exclusionData, transactionType, isCustomColumn, customValueFormula);
        		selectedArray.push(hierarchicalObj); 
        	});
        	hierarchical.populateHierarchyStructure(selectedParent,hierarchyId,depthLength,selectedArray);
        	console.log("hierarchicalObj -> ", selectedArray);
    	}else{
    		var hierarchyId = $("#hierarchyId").val();
        	var dataTr$ = $("#newElementsTable tbody tr");
        	var selectedArray = hierarchical.getRequiredArray(hierarchyId);
        	var depthLength = hierarchical.getSelectedArrayDepth(hierarchyId);
        	var selectedParent = $("#addHierarchy");
        	if (hierarchyId != "") {
        		selectedParent = $("#"+hierarchyId).closest(".parent-block");
        	}
        	dataTr$.each(function(index,obj){
        		var tableName = $(obj).find(".ilName").text();
        		var ilId = $(obj).find(".ilId").val();
        		var columnName = $(obj).find(".ilColumnName").text();
        		var valueType = $(obj).find(".ilValueType").val();
        		var value = $(obj).find(".columnValue").text();
        		var autoValueType = $(obj).find(".ilAutoValueType").val();
        		var exclusionData = $(obj).find(".ilExclusions").val();
        		var financialStatus = $(obj).find(".ilFinancialStatus").val();
        		var expressionValue = $(obj).find(".ilPatternValue").val();
        		var expressionPattern = $(obj).find(".ilPattrenExpression").val();
        		var customValueFormula = $(obj).find(".customValueFormula").val();
        		var isCustomColumn = $(obj).find(".isCustomColumn").val();
        		var transactionType = "";
        		if(financialStatus){
        			 transactionType = $(obj).find(".transactionTypeSelect").val();
        		}
        		exclusionData = exclusionData.split(',');
        		var hierarchicalObj = new HierarchicalObj(ilId, tableName, columnName, valueType, value, autoValueType, expressionValue,
        				           expressionPattern, exclusionData, transactionType, isCustomColumn, customValueFormula);
        		selectedArray.push(hierarchicalObj); 
        	});
        	hierarchical.populateHierarchyStructure(selectedParent,hierarchyId,depthLength,selectedArray);
    		//hierarchical.prepareEditHierarchyObject();
    	}	
    	$("#createNewHierarchyPopUp").modal('hide');
    	hierarchical.evenOddBgSetup();
    });
    
    $("#hierarchicalListTbl").on("click",".mapHierarhy",function(){
		  var hierarchicalId = this.value;
		  var currentRow=$(this).closest("tr");
		  var hierarchicalName = currentRow.find(".name").html();
		  hierarchical.cleanupMapHierarchy(hierarchicalId, hierarchicalName);
	  });
    
    $("#mapIlId").change(function(){
    	if (this.value && this.value != "0" ) {
    		var tableName = ilInfoMap[this.value].iL_table_name;
    		var hierarchyName = $("#maphierarchyName").val();
    		$("#ilMeasures").val([]);
    		$("#ilMeasures").multipleSelect('refresh');
    		$("#associationName").val(tableName+"_"+hierarchyName);
    		hierarchical.getMeasures();
    		//hierarchical.getDimensions();
    	}
    	else{
    		$("#ilMeasures").val([]);
    		$("#ilMeasures").multipleSelect('refresh');
    		$("#associationName").val("");
    	}
    });
    
    $("#ilColumnName").change(function(){
    	$(".selectRangeDiv").hide();
    	var isAutoDropDown = $("#autoDropDownCheckBox").prop("checked");
    	var columnName = $("#ilColumnName").val();
		var columnType = columnMap[columnName];
		var selectedOption = $("input[name='valueOptionSelection']:checked").val();
		if (isCreationMode) {
			$("#fromRange,#toRange").select2("val","");
			$(".selectRangeDiv").show();
			if($.inArray(columnType, numberColumnTypes) == -1){
				 $(".selectRangeDiv").hide();
			} else {
		    	 if(selectedOption.toLowerCase() == "pattern"){
		    		 $(".selectRangeDiv").hide(); 
		    		 $("#columnRangeDiv").hide();
		    	 }else{
		    		$("#selectRange").prop("checked",true);
		    		$("#selectRange").trigger("click");
		    	 }
			}
			var isRangeSelected = $("#selectRange").prop("checked");
	    	if (isAutoDropDown && !isRangeSelected) {
	    		
	    		if(this.value && this.value != "0" ){
	    			$("#ilColumnValueSelect").val([]);
	    	    	$("#ilColumnValueSelect").multipleSelect('refresh');
	    			hierarchical.getColumnValues();
	    		}
	    	}
		} else {
			if(selectedOption.toLowerCase() == "pattern"){
				$("#patternTextDiv").show();
				$("#select2Div").hide();
	    	 }else{
	    		 $("#patternTextDiv").hide();
	    		 $("#select2Div").show();
	    	 }
    		$("#ilColumnValueSelect").val([]);
	    	$("#ilColumnValueSelect").multipleSelect('refresh');
			hierarchical.getColumnValues();
		}
    });
    
    $("#selectRange").click(function(){
    	debugger;
    	var isAutoDropDown = $("#autoDropDownCheckBox").prop("checked");
    	var isRangeSelected = this.checked;
    	
    	$(".addCloumnValueDiv").show();
    	$(".fetchColumnData").hide();
    	if (isAutoDropDown) {
    		$("#inputtextDiv").hide();
    		if (isRangeSelected) {
    			$("#select2Div").hide();
    			$("#columnRangeDiv").show();
    			$(".fetchColumnData").show();
    			$(".addCloumnValueDiv").hide();
    		} else {
    			$("#columnRangeDiv").hide();
    			$("#select2Div").show();
    			
    			$("#ilColumnValueSelect").val([]);
    	    	$("#ilColumnValueSelect").multipleSelect('refresh');
    			hierarchical.getColumnValues();
    		}
    	} else {
    		$("#inputtextDiv").show();
    		$("#select2Div,#columnRangeDiv").hide();
    	}
    });
    
    $(".fetchColumnData").on("click", function(){
    	var fromrange = $("#fromRange").val();
    	var torange  = $("#toRange").val();
    	 common.clearValidations(["#fromRange", "#toRange"]);
    	 if(fromrange == null || fromrange.length == "0"){
    		 common.showcustommsg("#fromRange", "Please enter from range");
    		 return false;
    	 }
    	 if(torange == null || torange.length == "0"){
    		 common.showcustommsg("#toRange", "Please enter to range");
    		 return false;
    	 }
    	 hierarchical.getColumnValuesInRange(fromrange, torange);
    });
    
    $(document).on("click", "#mapHierarchyBtn", function() {
	    hierarchical.saveHierarchicalAssosiation();
	});
    
    $("#hierarchicalListTbl").on("click", ".runHierarchy", function(){
    	  var currentRow=$(this).closest("tr");
		  var hierarchicalId = currentRow.find(".id").html();
    	hierarchical.runHierarchicalStructure(hierarchicalId);
    });
    
    /*$(document).on("select", "#ilMeasures", function(){
    	var selectedvalue =  $("#ilMeasures").val();
    	var enableDateRangeDiv = false;
    	$.each(selectedvalue, function(i, obj){
    		if(columnMap(obj) == "DATETIME" || columnMap[obj] == "DATE"){
    			enableDateRangeDiv = true;
    		}
    	});
    	
    	if(enableDateRangeDiv){
    		$("#dataRangeDiv").show();  	
    	}
    	
    });*/
    
    $(document).on("click", "#hierarchyLevelBtn", function() {
		hierarchical.createHierarchicalLevelObj();
	});
    
    $("#addHierarchy").on('keypress', '.inlineNodeEditInput' ,function (e) {
        if (e.keyCode == 13) {
        	var depth = $($(this).closest(".parent-element")).data("depth");
			var divElementId = $("#"+depth);
			var selectedArray = hierarchical.getRequiredObj(depth);
			var div$ = $(this).closest(".parent-block");
			var editNodeValue = $(".inlineNodeEditInput").val();
			selectedArray.value = editNodeValue;
			div$.append($("<span>",{ type:"text",class:"element-name-span",text:editNodeValue}));
			$(this).remove();
        }
    });
    
    $(document).on('click', 'input[name="valueOptionSelection"]', function(e){
    	var selectedOption = $("input[name='valueOptionSelection']:checked").val();
    	var columnName = $("#ilColumnName").val();
		var columnType = columnMap[columnName];
    	if(isCreationMode){
    		if(selectedOption.toLowerCase() == "pattern"){
         		$("#patternTextDiv").show();
         		$('#select2Div').hide();
         		$(".selectRangeDiv").hide();
         		$("#columnRangeDiv").hide();
         		$("#ilColumnValueExclusionSelect").val([]);
            	$("#ilColumnValueExclusionSelect").multipleSelect('refresh');
            	$(".addCloumnValueDiv").show();	
            	$('#inputtextDiv, #ilTransactionTypeDiv').hide();
        	 }else{
        		$("#patternTextDiv").hide();
          		$('#select2Div').show(); 
        		$(".selectRangeDiv").show();
        		$('.exclusionDiv').hide();
        		if($.inArray(columnType, numberColumnTypes) == -1){
       			 	$(".selectRangeDiv").hide();
       			 	$("#columnRangeDiv").hide();
       			 	$(".addCloumnValueDiv").show();
    	   		} else {
    	   	    	$("#selectRange").prop("checked",true);
    	   	    	$("#selectRange").trigger("click");
    	   		}
        	 }
        
    	 }else{
    		 $(".customValueChkbxDiv").show();
    		 if(selectedOption.toLowerCase() == "pattern"){
          		$("#patternTextDiv").show();
          		$("#ilColumnValueExclusionSelect").val([]);
             	$("#ilColumnValueExclusionSelect").multipleSelect('refresh');
             	$('#inputtextDiv, #ilTransactionTypeDiv').hide();
             	$('#select2Div').hide();
             	$('.exclusionDiv').show();
             	$('.exclusionDataDiv').show();
             	if(isFinancialTemplate){
            		$('#ilTransactionTypeDiv').show();
            		$('#ilTransactionTypeSelect option:first').val();
            	 }
         	 }else{
         		 $("#patternTextDiv").hide();
         		 $('.exclusionDiv').hide();
         		 $('#inputtextDiv').hide();
         		 $('#ilTransactionTypeDiv').hide();
         		 $('.exclusionDataDiv').hide();
         		 $('#select2Div').show();
         		 if(isFinancialTemplate){
            		$('#ilTransactionTypeDiv').show();
            		$('#ilTransactionTypeSelect option:first').val();
            	  }
         	 }
    	}
    });
    
    $(document).on('click', '.fetchPatternData', function(){
         hierarchical.getIlColumnPatternValues();	
    });
    
    $(document).on('click', '#ilColumnValueExclusionSelect', function(){
    	var exclusions = $(this).val();
    	$('.exclusionSpan').text(exclusions.join(','));
    });
    
    $("#customValueChkbx").click(function(){
      var customColumnStatus = $(this).is(':checked');	
      var isRangeSelected = $("#selectRange").is(":checked");
      var selectedOption = $("input[name='valueOptionSelection']:checked").val();
      if(customColumnStatus){
    	   var selectedValues = $('#ilColumnValueSelect').val();
    	   if(selectedOption != undefined && selectedOption != null && selectedOption.toLowerCase() == "pattern"){
    		   hierarchical.getIlColumnPatternValues();
    	   }else if(selectedOption != undefined && selectedOption != null && selectedOption.toLowerCase() == "manual"){
    		   if(isRangeSelected){
       		   	var fromrange = $("#fromRange").val();
      	    		var torange  = $("#toRange").val();
       	    	var result = hierarchical.getColumnValuesInRange(fromrange, torange);
	       	   }else{
	     		   	 hierarchical.populateCustomSeletedValues(selectedValues);
	       	   }
    	   }
        }else{
        	if(selectedOption != undefined && selectedOption != null && selectedOption.toLowerCase() == "pattern"){
        		hierarchical.getIlColumnPatternValues();
        	}else if(selectedOption != undefined && selectedOption != null && selectedOption.toLowerCase() == "manual"){
        		if(isRangeSelected){
        		   	var fromrange = $("#fromRange").val();
       	    		var torange  = $("#toRange").val();
        	    	var result = hierarchical.getColumnValuesInRange(fromrange, torange);
        	   }else{
        		   $(".customValueDiv").hide();
        	   }
        	}
        	$(".customValueDiv").hide();
        }
    });
    
    $(document).on('change', '#ilColumnValueSelect', function(){
    	var values = $(this).val();
    	var selectedOption = $("input[name='valueOptionSelection']:checked").val();
    	var customColumnStatus = $("#customValueChkbx").is(':checked');	
    	if(customColumnStatus){
    		var selectedValues = $('#ilColumnValueSelect').val();
    		hierarchical.populateCustomSeletedValues(selectedValues);
    	}
    });
    
}

