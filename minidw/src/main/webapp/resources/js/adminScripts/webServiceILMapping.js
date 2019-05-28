var webService = {
		initialPage : function(){
			if($("select#wsTemplateId").length){
				$("#wsTemplateId").select2({               
	                allowClear: true,
	                theme: "classic"
				});
			}
			
			$("#iLsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
			if($("#wsApiMappingBlocks .wsApiMappingBlock").length == "0"){
				$(".saveWebService").prop("disabled",true);
			}
		},
		changeIndices : function(){
			var count = $("#wsApiMappingBlocks .wsApiMappingBlock").length;
			for(var i= 0; i<count; i++){
				$("#wsApiMappingBlocks .wsApiMappingBlock").each(function(j,row){
					if(i == j){
						$(row).find(".wsApiField").each(function(){
							var str = $(this).attr("name");
							var index = str.substring(str.indexOf("["),str.indexOf("]")+1);
							$(this).prop("name",$(this).attr("name").split(index).join("["+i+"]"));
						});
						$(row).find(".getPathParams").val(i);
						$(row).find(".wsApiMappingBlockAccordian a").prop("href","#collapse"+i);
						$(row).find(".accordionWs a").prop("href","#collapseOneWS"+i);
						$(row).find("#collapseOneWS").attr("id","collapseOneWS"+i);
						$(row).find("#collapse0").attr("id","collapse"+i);
					}
				});
			}
		},
		addRequestParameters : function(){
			$("#wsApiMappingBlocks .wsApiMappingBlock").each(function(i,v){
				var mainDiv = $(v);
				var paramArray = mainDiv.find(".reqParams").val();
				if (paramArray) {
					var paramArrayObj = JSON.parse(paramArray);
					$.each(paramArrayObj,function(ind,param){
						var newParamTr = mainDiv.find("#requestParamsTable tfoot tr:first").clone();
						$(newParamTr).removeAttr("style class");
						newParamTr.find(".paramValue").val(param['paramName']);
						newParamTr.find(".isMandatory").prop({"checked":param['isMandatory'], "disabled":false});
						newParamTr.find(".isPassword").prop({"checked":param['ispasswordField'], "disabled":false});
						mainDiv.find("#requestParamsTable tbody").append(newParamTr);
					});
				}
				
				var bodyArray = mainDiv.find(".bodyParams").val();
				if (bodyArray) {
					var paramArrayObj = JSON.parse(bodyArray);
					$.each(paramArrayObj,function(ind,param){
						var newParamTr = mainDiv.find("#bodyParamsTable tfoot tr:first").clone();
						$(newParamTr).removeAttr("style class");
						newParamTr.find(".paramValue").val(param['paramName']);
						newParamTr.find(".isMandatory").prop({"checked":param['isMandatory'], "disabled":false});
						newParamTr.find(".isPassword").prop({"checked":param['ispasswordField'], "disabled":false});
						mainDiv.find("#bodyParamsTable tbody").append(newParamTr);
					});
				}
				
				var defaultMapping = mainDiv.find(".defaultMapping").val();
				var paramArrayObj = JSON.parse(defaultMapping);
				if (!$.isEmptyObject(paramArrayObj)) {
					$.each(paramArrayObj,function(key,val,i){
						var newParamTr = mainDiv.find("#columnsParameters").clone().removeAttr("id style");
						newParamTr.find(".columnKey").val(key);
						newParamTr.find(".columnValue").val(val);
						if ( mainDiv.find("#defaultMappingDiv > div").length < 1 ) {
							newParamTr.find(".deleteMappingField").remove();	
						}
						mainDiv.find("#defaultMappingDiv").append(newParamTr);
					});
				}else{
					var addBodyParam = mainDiv.find("#columnsParameters").clone().removeAttr("id").show();
					addBodyParam.find(".deleteMappingField").remove();
					addBodyParam.find(".columnKey").val('');
					addBodyParam.find(".columnValue").val('');
					mainDiv.find("#defaultMappingDiv").append(addBodyParam)
				}
				
				var paginationRequestParamsArray = mainDiv.find(".paginationRequestParamsData").val();
				var paginationRequired = mainDiv.find(".paginationRequired:checked").val() == 'yes' ? true : false;
				var paginationType = mainDiv.find(".paginationOffsetDateType:checked").val();
				if (paginationRequired && paginationRequestParamsArray) {
					var paramArrayObj = JSON.parse(paginationRequestParamsArray);
					if(paginationType == 'offset'){
						$.each(paramArrayObj,function(ind,param){
							mainDiv.find(".paginationParamType").val(param['paginationParamType']);
							mainDiv.find(".paginationOffSetRequestParamName").val(param['paginationOffSetRequestParamName']);
							mainDiv.find(".paginationOffSetRequestParamValue").val(param['paginationOffSetRequestParamValue']);
							mainDiv.find(".paginationLimitRequestParamName").val(param['paginationLimitRequestParamName']);
							mainDiv.find(".paginationLimitRequestParamValue").val(param['paginationLimitRequestParamValue']);
							mainDiv.find(".paginationObjectName").val(param['paginationObjectName']);
							mainDiv.find(".paginationSearchId").val(param['paginationSearchId']);
							mainDiv.find(".PaginationSoapBody").val(param['PaginationSoapBody']);
							mainDiv.find(".paginationOffSetType").show();
							mainDiv.find(".paginationPageNumberSizeType").hide();
							mainDiv.find(".paginationDateType").hide();
							mainDiv.find(".paginationOtherType").hide();
							mainDiv.find(".paginationIncrementalDateType").hide();
							mainDiv.find(".paginationConditionalType").hide();
						});
					}else if(paginationType == 'page'){
						$.each(paramArrayObj,function(ind,param){
							mainDiv.find(".paginationParamType").val(param['paginationParamType']);
							mainDiv.find(".paginationPageNumberRequestParamName").val(param['paginationPageNumberRequestParamName']);
							mainDiv.find(".paginationPageNumberRequestParamValue").val(param['paginationPageNumberRequestParamValue']);
							mainDiv.find(".paginationPageSizeRequestParamName").val(param['paginationPageSizeRequestParamName']);
							mainDiv.find(".paginationPageSizeRequestParamValue").val(param['paginationPageSizeRequestParamValue']);
							mainDiv.find(".paginationPageNumberSizeType").show();
							mainDiv.find(".paginationOffSetType").hide();
							mainDiv.find(".paginationDateType").hide();
							mainDiv.find(".paginationOtherType").hide();
							mainDiv.find(".paginationIncrementalDateType").hide();
							mainDiv.find(".paginationConditionalType").hide();
						});
					}else if(paginationType == 'date'){
						$.each(paramArrayObj,function(ind,param){
							mainDiv.find(".paginationParamType").val(param['paginationParamType']);
							mainDiv.find(".paginationStartDateParam").val(param['paginationStartDateParam']);
							mainDiv.find(".paginationEndDateParam").val(param['paginationEndDateParam']);
							mainDiv.find(".paginationStartDate").val(param['paginationStartDate']);
							mainDiv.find(".paginationDateRange").val(param['paginationDateRange']);
							mainDiv.find(".paginationDatePageNumberRequestParamName").val(param['paginationDatePageNumberRequestParamName']);
							mainDiv.find(".paginationDatePageNumberRequestParamValue").val(param['paginationDatePageNumberRequestParamValue']);
							mainDiv.find(".paginationDatePageSizeRequestParamName").val(param['paginationDatePageSizeRequestParamName']);
							mainDiv.find(".paginationDatePageSizeRequestParamValue").val(param['paginationDatePageSizeRequestParamValue']);
							mainDiv.find(".paginationDateType").show();
							mainDiv.find(".paginationOffSetType").hide();
							mainDiv.find(".paginationOtherType").hide();
							mainDiv.find(".paginationIncrementalDateType").hide();
							mainDiv.find(".paginationConditionalType").hide();
							mainDiv.find(".paginationPageNumberSizeType").hide();
							mainDiv.find(".paginationStartDate").datepicker({
									dateFormat : 'yy-mm-dd',
									defaultDate : new Date(),
									changeMonth : true,
									changeYear : true, 
									yearRange : "0:+20",
									numberOfMonths : 1
								});
						});
					}else if(paginationType == 'incrementaldate'){
						$.each(paramArrayObj,function(ind,param){
							mainDiv.find(".paginationParamType").val(param['paginationParamType']);
							mainDiv.find(".paginationIncrementalStartDateParam").val(param['paginationIncrementalStartDateParam']);
							mainDiv.find(".paginationIncrementalStartDate").val(param['paginationIncrementalStartDate']);
							mainDiv.find(".paginationIncrementalEndDateParam").val(param['paginationIncrementalEndDateParam']);
							mainDiv.find(".paginationIncrementalEndDate").val(param['paginationIncrementalEndDate']);
							mainDiv.find(".paginationIncrementalDateRange").val(param['paginationIncrementalDateRange']);
							mainDiv.find(".paginationIncrementalDatePageNumberRequestParamName").val(param['paginationIncrementalDatePageNumberRequestParamName']);
							mainDiv.find(".paginationIncrementalDatePageNumberRequestParamValue").val(param['paginationIncrementalDatePageNumberRequestParamValue']);
							mainDiv.find(".paginationIncrementalDatePageSizeRequestParamName").val(param['paginationIncrementalDatePageSizeRequestParamName']);
							mainDiv.find(".paginationIncrementalDatePageSizeRequestParamValue").val(param['paginationIncrementalDatePageSizeRequestParamValue']);
							mainDiv.find(".paginationIncrementalDateType").show();
							mainDiv.find(".paginationConditionalType").hide();
							mainDiv.find(".paginationDateType").hide();
							mainDiv.find(".paginationOffSetType").hide();
							mainDiv.find(".paginationOtherType").hide();
							mainDiv.find(".paginationPageNumberSizeType").hide();
							mainDiv.find(".paginationIncrementalStartDate").datepicker({
									dateFormat : 'yy-mm-dd',
									defaultDate : new Date(),
									changeMonth : true,
									changeYear : true, 
									yearRange : "0:+20",
									numberOfMonths : 1
								});
							mainDiv.find(".paginationIncrementalEndDate").datepicker({
								dateFormat : 'yy-mm-dd',
								defaultDate : new Date(),
								changeMonth : true,
								changeYear : true, 
								yearRange : "0:+20",
								numberOfMonths : 1
							});
						});
					}else if(paginationType == 'conditionaldate'){
						$.each(paramArrayObj,function(ind,param){
							mainDiv.find(".paginationParamType option:first").prop("selected", "selected");
							mainDiv.find(".paginationFilterName").val(param['paginationFilterName']);
							mainDiv.find(".paginationConditionalFromDateParam").val(param['paginationConditionalFromDateParam']);
							mainDiv.find(".paginationConditionalFromDateCondition").val(param['paginationConditionalFromDateCondition']);
							mainDiv.find(".paginationConditionalFromDate").val(param['paginationConditionalFromDate']);
							mainDiv.find(".paginationConditionalParam").val(param['paginationConditionalParam']);
							mainDiv.find(".paginationConditionalToDateParam").val(param['paginationConditionalToDateParam']);
							mainDiv.find(".paginationConditionalToDateCondition").val(param['paginationConditionalToDateCondition']);
							mainDiv.find(".paginationConditionalToDate").val(param['paginationConditionalToDate']);
							mainDiv.find(".paginationConditionalDayRange").val(param['paginationConditionalDayRange']);
							mainDiv.find(".paginationOtherType").hide();
							mainDiv.find(".paginationParamTypeDiv").hide();
							mainDiv.find(".paginationOffSetType").hide();
							mainDiv.find(".paginationDateType").hide();
							mainDiv.find(".paginationIncrementalDateType").hide();
							mainDiv.find(".paginationPageNumberSizeType").hide();
							mainDiv.find(".paginationConditionalType").show();
							
							mainDiv.find(".paginationConditionalFromDate").datepicker({
								dateFormat : 'yy-mm-dd',
								defaultDate : new Date(),
								changeMonth : true,
								changeYear : true, 
								yearRange : "0:+20",
								numberOfMonths : 1
							});
							mainDiv.find(".paginationConditionalToDate").datepicker({
								dateFormat : 'yy-mm-dd',
								defaultDate : new Date(),
								changeMonth : true,
								changeYear : true, 
								yearRange : "0:+20",
								numberOfMonths : 1
							});
					  });
					}else{
						$.each(paramArrayObj,function(ind,param){
							mainDiv.find(".paginationParamType option:first").prop("selected", "selected");
							mainDiv.find(".paginationOtherRequestKeyParam").val(param['paginationHyperLinkPattern']);
							mainDiv.find(".paginationOtherRequestLimit").val(param['paginationHypermediaPageLimit']);
							mainDiv.find(".paginationOtherType").show();
							mainDiv.find(".paginationPageNumberSizeType").hide();
							mainDiv.find(".paginationParamTypeDiv").hide();
							mainDiv.find(".paginationOffSetType").hide();
							mainDiv.find(".paginationDateType").hide();
							mainDiv.find(".paginationIncrementalDateType").hide();
							mainDiv.find(".paginationConditionalType").hide();
						});
					}
				}else{
					mainDiv.find("input[value='no']").prop("checked",true);
					mainDiv.find("input[value='offset']").prop("checked",false);
					mainDiv.find("input[value='date']").prop("checked",false);
					mainDiv.find("input[value='hypermedia']").prop("checked",false);
					mainDiv.find("input[value='incrementaldate']").prop("checked",false);
					mainDiv.find("input[value='conditionaldate']").prop("checked",false);
					mainDiv.find("input[value='page']").prop("checked",false);
					mainDiv.find(".paginationPageNumberSizeType").hide();
					mainDiv.find(".paginationOffSetType").hide();
					mainDiv.find(".paginationDateType").hide();
					mainDiv.find(".paginationOtherType").hide();
					mainDiv.find(".paginationIncrementalDateType").hide();
					mainDiv.find(".paginationConditionalType").hide();
				}
				
				var incrementalUpdateparamdArray = mainDiv.find(".incrementalUpdateparamdata").val();
				var incrementalUpdate = mainDiv.find(".incrementalUpdate").prop("checked");
				
				if (incrementalUpdate && incrementalUpdateparamdArray) {
					var paramArrayObj = JSON.parse(incrementalUpdateparamdArray);
					$.each(paramArrayObj,function(ind,param){
						mainDiv.find(".incrementalUpdateParamName").val(param['incrementalUpdateParamName']);
						mainDiv.find(".incrementalUpdateParamvalue").val(param['incrementalUpdateParamvalue']);
						mainDiv.find(".incrementalUpdateParamColumnName").val(param['incrementalUpdateParamColumnName']);
						mainDiv.find(".incrementalUpdateParamType").val(param['incrementalUpdateParamType']);
						mainDiv.find(".incrementalUpdateDetailsDiv").show();
					});
				}
			});
		},
		addPathParameters : function(){
			$("#wsApiMappingBlocks .wsApiMappingBlock").each(function(i,v){
				
				var mainDiv = $(v);
				var paramArray = mainDiv.find(".apiPathParams").val();
				var baseUrl = mainDiv.find(".baseUrl").val();
				
				if (paramArray) {
					var paramArrayObj = JSON.parse(paramArray);
					
					$.each(paramArrayObj,function(j, param){
						var pathParamDetailsBlock = $("#pathParamDetailsSampleBlock").clone();
						$(pathParamDetailsBlock).removeAttr("style id");
						pathParamDetailsBlock.find(".pathParamName").text(param['paramName']);
						
						var valueType = param['valueType'];
						pathParamDetailsBlock.find(".pathParamValueType").prop("name","pathParamValueType"+i+j)
						if(valueType == "M"){
							pathParamDetailsBlock.find("input[value='M']").prop("checked",true);
						}
						else{
							pathParamDetailsBlock.find("input[value='S']").prop("checked",true);
							
							var subUrlObj = param['subUrldetails'];
							var subUrlBlock = pathParamDetailsBlock.find(".subUrlBlock");
							$(subUrlBlock).css("display","");
							var methodType = subUrlObj["methodType"];
							var subUrlPaginationRequired = subUrlObj["subUrlPaginationRequired"];
							var subUrlPaginationType = subUrlObj["subUrlPaginationType"];
							var subUrlPaginationStartDateParam = subUrlObj["subUrlPaginationStartDateParam"];
							var subUrlPaginationEndDateParam = subUrlObj["subUrlPaginationEndDateParam"];
							var subUrlPaginationStartDate = subUrlObj["subUrlPaginationStartDate"];
							var subUrlPaginationDateRange = subUrlObj["subUrlPaginationDateRange"];
							var subUrlPaginationParamType = subUrlObj["subUrlPaginationParamType"];
							
							
							var subUrlPaginationIncrementalStartDateParam = subUrlObj["subUrlPaginationIncrementalStartDateParam"];
							var subUrlPaginationIncrementalStartDate = subUrlObj["subUrlPaginationIncrementalStartDate"];
							var subUrlPaginationIncrementalEndDateParam = subUrlObj["subUrlPaginationIncrementalEndDateParam"];
							var subUrlPaginationIncrementalEndDate = subUrlObj["subUrlPaginationIncrementalEndDate"];
							var subUrlPaginationIncrementalDateRange = subUrlObj["subUrlPaginationIncrementalDateRange"];
							
							var subUrlPaginationOffSetRequestParamName = subUrlObj["subUrlPaginationOffSetRequestParamName"];
							var subUrlPaginationOffSetRequestParamValue = subUrlObj["subUrlPaginationOffSetRequestParamValue"];
							var subUrlPaginationLimitRequestParamName = subUrlObj["subUrlPaginationLimitRequestParamName"];
							var subUrlPaginationLimitRequestParamValue = subUrlObj["subUrlPaginationLimitRequestParamValue"];
							
							var subUrlPaginationPageNumberRequestParamName = subUrlObj["subUrlPaginationPageNumberRequestParamName"];
							var subUrlPaginationPageNumberRequestParamValue = subUrlObj["subUrlPaginationPageNumberRequestParamValue"];
							var subUrlPaginationPageSizeRequestParamName = subUrlObj["subUrlPaginationPageSizeRequestParamName"];
							var subUrlPaginationPageSizeRequestParamValue = subUrlObj["subUrlPaginationPageSizeRequestParamValue"];
							
							var subUrlPaginationOtherRequestParamkey = subUrlObj["paginationHyperLinkPattern"];
							var subUrlPaginationOtherRequestLimit = subUrlObj["paginationHypermediaPageLimit"];
							
							var subUrlPaginationFilterName = subUrlObj['subUrlPaginationFilterName'];
							var subUrlPaginationConditionalDayRange = subUrlObj['subUrlPaginationConditionalDayRange'];
							var subUrlPaginationConditionalFromDateParam = subUrlObj['subUrlPaginationConditionalFromDateParam'];
							var subUrlPaginationConditionalFromDateCondition = subUrlObj['subUrlPaginationConditionalFromDateCondition'];
							var subUrlPaginationConditionalFromDate = subUrlObj['subUrlPaginationConditionalFromDate'];
							var subUrlPaginationConditionalParam = subUrlObj['subUrlPaginationConditionalParam'];
							var subUrlPaginationConditionalToDateParam = subUrlObj['subUrlPaginationConditionalToDateParam'];
							var subUrlPaginationConditionalToDateCondition = subUrlObj['subUrlPaginationConditionalToDateCondition'];
							var subUrlPaginationConditionalToDate = subUrlObj['subUrlPaginationConditionalToDate'];
							
							var subUrlIncrementalUpdate = subUrlObj['subUrlIncrementalUpdate'];
							
							var subUrlIncrementalUpdateParamColumnName = subUrlObj['subUrlIncrementalUpdateParamColumnName'];
							var subUrlIncrementalUpdateParamName = subUrlObj['subUrlIncrementalUpdateParamName'];
							var subUrlIncrementalUpdateParamvalue = subUrlObj['subUrlIncrementalUpdateParamvalue'];
							var subUrlIncrementalUpdateParamType = subUrlObj['subUrlIncrementalUpdateParamType'];
							
							 						
							subUrlBlock.find(".subUrl").val(subUrlObj["url"]);
							if(baseUrl != null && baseUrl != ''){
								subUrlBlock.find(".baseUrlForSubUrl").text("["+baseUrl+"]");
							}
							if(subUrlObj["baseUrlRequired"]){
								subUrlBlock.find(".baseUrlRequired").prop("checked",true);
							}else{
								subUrlBlock.find(".baseUrlRequired").prop("checked",false);
							}
							subUrlBlock.find(".subUrlResObj").val(subUrlObj["responseObjName"]);
							subUrlBlock.find(".subUrlMethodType").prop("name","subUrlMethodType"+i+j);
							
							if(methodType == "GET"){
								subUrlBlock.find("input[value='GET']").prop("checked",true);
								subUrlBlock.find(".subUrlIncrementalUpdateParamType .subUrlIncrementalUpdateBodyParamType").hide()
							}else{
								subUrlBlock.find("input[value='POST']").prop("checked",true);
							}
							subUrlBlock.find(".subUrlPaginationRequired").prop("name","subUrlPaginationRequired"+i+j);
							subUrlBlock.find(".subUrlPaginationOffsetDateType").prop("name","subUrlPaginationOffsetDateType"+i+j);
							if(subUrlPaginationRequired){
								subUrlBlock.find(".subUrlPaginationType").show();
								subUrlBlock.find("input[value='yes']").prop("checked",true);
								subUrlBlock.find(".subUrlPaginationParamType").val(subUrlPaginationParamType);
								if(subUrlPaginationType == 'offset'){
									subUrlBlock.find("input[value='offset']").prop("checked",true);
									subUrlBlock.find(".subUrlPaginationOffSetRequestParamName").val(subUrlPaginationOffSetRequestParamName);
									subUrlBlock.find(".subUrlPaginationOffSetRequestParamValue").val(subUrlPaginationOffSetRequestParamValue);
									subUrlBlock.find(".subUrlPaginationLimitRequestParamName").val(subUrlPaginationLimitRequestParamName);
									subUrlBlock.find(".subUrlPaginationLimitRequestParamValue").val(subUrlPaginationLimitRequestParamValue);
									subUrlBlock.find(".subUrlPaginationOffSetType").show();
									subUrlBlock.find(".subUrlPaginationDateType").hide();
									subUrlBlock.find(".subUrlPaginationIncrementalDateType").hide();
									subUrlBlock.find(".subUrlPaginationOtherType").hide();
									subUrlBlock.find(".subUrlPaginationConditionalType").hide();
									subUrlBlock.find(".subUrlPaginationPageNumberSizeType").hide();
								}else if(subUrlPaginationType == 'page'){
									subUrlBlock.find("input[value='page']").prop("checked",true);
									subUrlBlock.find(".subUrlPaginationPageNumberRequestParamName").val(subUrlPaginationPageNumberRequestParamName);
									subUrlBlock.find(".subUrlPaginationPageNumberRequestParamValue").val(subUrlPaginationPageNumberRequestParamValue);
									subUrlBlock.find(".subUrlPaginationPageSizeRequestParamName").val(subUrlPaginationPageSizeRequestParamName);
									subUrlBlock.find(".subUrlPaginationPageSizeRequestParamValue").val(subUrlPaginationPageSizeRequestParamValue);
									subUrlBlock.find(".subUrlPaginationOffSetType").hide();
									subUrlBlock.find(".subUrlPaginationPageNumberSizeType").show();
									subUrlBlock.find(".subUrlPaginationDateType").hide();
									subUrlBlock.find(".subUrlPaginationIncrementalDateType").hide();
									subUrlBlock.find(".subUrlPaginationOtherType").hide();
									subUrlBlock.find(".subUrlPaginationConditionalType").hide();
								}else if(subUrlPaginationType == 'date'){
									subUrlBlock.find(".subUrlPaginationStartDate").datepicker({
											dateFormat : 'yy-mm-dd',
											defaultDate : new Date(),
											changeMonth : true,
											changeYear : true, 
											yearRange : "0:+20",
											numberOfMonths : 1
										});
									subUrlBlock.find("input[value='date']").prop("checked",true);
									subUrlBlock.find(".subUrlPaginationStartDate").val(subUrlPaginationStartDate);
									subUrlBlock.find(".subUrlPaginationDateRange").val(subUrlPaginationDateRange);
									subUrlBlock.find(".subUrlPaginationStartDateParam").val(subUrlPaginationStartDateParam);
									subUrlBlock.find(".subUrlPaginationEndDateParam").val(subUrlPaginationEndDateParam);
									subUrlBlock.find(".subUrlPaginationDatePageNumberRequestParamName").val(param['subUrlPaginationDatePageNumberRequestParamName']);
									subUrlBlock.find(".subUrlPaginationDatePageNumberRequestParamValue").val(param['subUrlPaginationDatePageNumberRequestParamValue']);
									subUrlBlock.find(".subUrlPaginationDatePageSizeRequestParamName").val(param['subUrlPaginationDatePageSizeRequestParamName']);
									subUrlBlock.find(".subUrlPaginationDatePageSizeRequestParamValue").val(param['subUrlPaginationDatePageSizeRequestParamValue']);
									subUrlBlock.find(".subUrlPaginationDateType").show();
									subUrlBlock.find(".subUrlPaginationIncrementalDateType").hide();
									subUrlBlock.find(".subUrlPaginationOffSetType").hide();
									subUrlBlock.find(".subUrlPaginationOtherType").hide();
									subUrlBlock.find(".subUrlPaginationConditionalType").hide()
									subUrlBlock.find(".subUrlPaginationPageNumberSizeType").hide();
								}else if(subUrlPaginationType == 'conditionaldate'){
										
									subUrlBlock.find("input[value='conditionaldate']").prop("checked",true);
									subUrlBlock.find(".subUrlPaginationFilterName").val(subUrlPaginationFilterName);
									subUrlBlock.find(".subUrlPaginationConditionalFromDateParam").val(subUrlPaginationConditionalFromDateParam);
									subUrlBlock.find(".subUrlPaginationConditionalFromDateCondition").val(subUrlPaginationConditionalFromDateCondition);
									subUrlBlock.find(".subUrlPaginationConditionalFromDate").val(subUrlPaginationConditionalFromDate);
									subUrlBlock.find(".subUrlPaginationConditionalParam").val(subUrlPaginationConditionalParam);
									subUrlBlock.find(".subUrlPaginationConditionalToDateParam").val(subUrlPaginationConditionalToDateParam);
									subUrlBlock.find(".subUrlPaginationConditionalToDateCondition").val(subUrlPaginationConditionalToDateCondition);
									subUrlBlock.find(".subUrlPaginationConditionalToDate").val(subUrlPaginationConditionalToDate);
									subUrlBlock.find(".subUrlPaginationConditionalDayRange").val(subUrlPaginationConditionalDayRange);
									subUrlBlock.find(".subUrlPaginationDateType").hide();
									subUrlBlock.find(".subUrlPaginationOtherType").hide();
									subUrlBlock.find(".subUrlPaginationDateType").hide();
									subUrlBlock.find(".subUrlPaginationIncrementalDateType").hide();
									subUrlBlock.find(".subUrlPaginationConditionalType").show();
									subUrlBlock.find(".subUrlPaginationPageNumberSizeType").hide();
										
									subUrlBlock.find(".subUrlPaginationConditionalFromDate").datepicker({
											dateFormat : 'yy-mm-dd',
											defaultDate : new Date(),
											changeMonth : true,
											changeYear : true, 
											yearRange : "0:+20",
											numberOfMonths : 1
										});
									subUrlBlock.find(".subUrlPaginationConditionalToDate").datepicker({
											dateFormat : 'yy-mm-dd',
											defaultDate : new Date(),
											changeMonth : true,
											changeYear : true, 
											yearRange : "0:+20",
											numberOfMonths : 1
										});
								}else if(subUrlPaginationType == 'incrementaldate'){
									subUrlBlock.find(".subUrlPaginationIncrementalStartDate").datepicker({
										dateFormat : 'yy-mm-dd',
										defaultDate : new Date(),
										changeMonth : true,
										changeYear : true, 
										yearRange : "0:+20",
										numberOfMonths : 1
									});
									subUrlBlock.find(".subUrlPaginationIncrementalEndDate").datepicker({
										dateFormat : 'yy-mm-dd',
										defaultDate : new Date(),
										changeMonth : true,
										changeYear : true, 
										yearRange : "0:+20",
										numberOfMonths : 1
									});
								subUrlBlock.find("input[value='incrementaldate']").prop("checked",true);
								subUrlBlock.find(".subUrlPaginationIncrementalStartDate").val(subUrlPaginationIncrementalStartDate);
								subUrlBlock.find(".subUrlPaginationIncrementalDateRange").val(subUrlPaginationIncrementalDateRange);
								subUrlBlock.find(".subUrlPaginationIncrementalStartDateParam").val(subUrlPaginationIncrementalStartDateParam);
								subUrlBlock.find(".subUrlPaginationIncrementalEndDateParam").val(subUrlPaginationIncrementalEndDateParam);
								subUrlBlock.find(".subUrlPaginationIncrementalEndDate").val(subUrlPaginationIncrementalEndDate);
								subUrlBlock.find(".subUrlPaginationIncrementalDatePageNumberRequestParamName").val(param['subUrlPaginationIncrementalDatePageNumberRequestParamName']);
								subUrlBlock.find(".subUrlPaginationIncrementalDatePageNumberRequestParamValue").val(param['subUrlPaginationIncrementalDatePageNumberRequestParamValue']);
								subUrlBlock.find(".subUrlPaginationIncrementalDatePageSizeRequestParamName").val(param['subUrlPaginationIncrementalDatePageSizeRequestParamName']);
								subUrlBlock.find(".subUrlPaginationIncrementalDatePageSizeRequestParamValue").val(param['subUrlPaginationIncrementalDatePageSizeRequestParamValue']);
								subUrlBlock.find(".subUrlPaginationIncrementalDateType").show();
								subUrlBlock.find(".subUrlPaginationDateType").hide();
								subUrlBlock.find(".subUrlPaginationOffSetType").hide();
								subUrlBlock.find(".subUrlPaginationOtherType").hide();
								subUrlBlock.find(".subUrlPaginationConditionalType").hide();
								subUrlBlock.find(".subUrlPaginationPageNumberSizeType").hide();
							}else{
									subUrlBlock.find("input[value='hypermedia']").prop("checked",true);
									subUrlBlock.find(".subUrlPaginationOtherRequestParamkey").val(subUrlPaginationOtherRequestParamkey);
									subUrlBlock.find(".subUrlPaginationOtherRequestLimit").val(subUrlPaginationOtherRequestLimit);
									subUrlBlock.find(".subUrlPaginationParamType option:first").prop("selected", "selected");
									subUrlBlock.find(".subUrlPaginationOtherType").show();
									subUrlBlock.find(".subUrlPaginationParamTypeDiv").hide();
									subUrlBlock.find(".subUrlPaginationOffSetType").hide();
									subUrlBlock.find(".subUrlPaginationDateType").hide();
									subUrlBlock.find(".subUrlPaginationConditionalType").hide();
									subUrlBlock.find(".subUrlPaginationIncrementalDateType").hide();
									subUrlBlock.find(".subUrlPaginationPageNumberSizeType").hide();
								}
							}else{
								subUrlBlock.find("input[value='no']").prop("checked",true);
								subUrlBlock.find("input[value='offset']").prop("checked",false);
								subUrlBlock.find("input[value='date']").prop("checked",false);
								subUrlBlock.find("input[value='incrementaldate']").prop("checked",false);
								subUrlBlock.find("input[value='hypermedia']").prop("checked",false);
								subUrlBlock.find("input[value='conditionaldate']").prop("checked",false);
								subUrlBlock.find("input[value='page']").prop("checked",false);
								subUrlBlock.find(".subUrlPaginationDateType").hide();
								subUrlBlock.find(".subUrlPaginationOffSetType").hide();
								subUrlBlock.find(".subUrlPaginationDateType").hide();
								subUrlBlock.find(".subUrlPaginationConditionalType").hide();
								subUrlBlock.find(".subUrlPaginationPageNumberSizeType").hide();
							}
							 
							subUrlBlock.find(".subUrlIncrementalUpdate").prop("name","subUrlIncrementalUpdate"+i+j);
							if(subUrlIncrementalUpdate){
								subUrlBlock.find(".subUrlIncrementalUpdateDetailsDiv").show();
								subUrlBlock.find(".subUrlIncrementalUpdate").prop("checked",true);
								subUrlBlock.find(".subUrlIncrementalUpdateParamColumnName").val(subUrlIncrementalUpdateParamColumnName);
								subUrlBlock.find(".subUrlIncrementalUpdateParamName").val(subUrlIncrementalUpdateParamName);
								subUrlBlock.find(".subUrlIncrementalUpdateParamvalue").val(subUrlIncrementalUpdateParamvalue);
								subUrlBlock.find(".subUrlIncrementalUpdateParamType").val(subUrlIncrementalUpdateParamType);
							}
							
						}
						mainDiv.find(".pathParamsDetailsBlocks").append(pathParamDetailsBlock);
					});
				}
			});
		},
		getPathParams : function(source,pattern){
		  var pathParams = [];   var lastIndex = 0;
		  while(source.indexOf(pattern,lastIndex) != -1) {
			  var param = "";
			  var startIndex = source.indexOf(pattern, lastIndex);
			  var endIndex = source.indexOf("}",startIndex);
			  if(endIndex != -1){
				  param = source.substring(startIndex+2,endIndex);
			  
				  if ( pathParams.indexOf(param) == -1 ) {
					  pathParams.push(param);
				  }
				  
				  lastIndex = endIndex;
			  }
			  else{
				  break;
			  }
		  }
		  return pathParams;
	  },	
	
};

if($(".webServiceILMapping-page").length){
	 
	webService.initialPage();
	webService.addRequestParameters();
	webService.addPathParameters();
	
	$(document).on("click", ".addOrEdit", function(){
		var iLId = $(this).val();
		$("#iLId").val(iLId);
		$("#webServiceILMapping").prop("action",$("#edit").val());
		this.form.submit();
		showAjaxLoader(true);
	});
	
	$(document).on("change","#wsTemplateId",function(){
		this.form.submit();
		showAjaxLoader(true);
	});
	
	$(document).on("click",".addRequestParam",function(){
		var requestKeyValue = $(this).parents("#requestParamsTable").find("tfoot tr.data-row").clone();
		$(requestKeyValue).removeAttr("style class");
		$(this).parents("#requestParamsTable").append(requestKeyValue);
	});
	
	$(document).on("click",".deleteRequestParam",function(){ 		
 		$(this).parents("tr").remove(); 		 			 
 	});
	

	$(document).on("click",".addBodyParam",function(){
		var requestKeyValue = $(this).parents("#bodyParamsTable").find("tfoot tr.data-row").clone();
		$(requestKeyValue).removeAttr("style class");
		$(this).parents("#bodyParamsTable").append(requestKeyValue);
	});
	
	$(document).on("click",".addMappingField",function(){
		var mappingColumns = $("#columnsParameters").clone().removeAttr("id style");
		if ( $("#defaultMappingDiv > div").length > 0 ) {
			mappingColumns.find(".control-label").text("")
		}
		
		$(this).parents("#defaultMappingDiv").append(mappingColumns);
	});
	
	$(document).on("click",".deleteMappingField",function(){
		$(this).parents(".columnsKeyValue").remove();
	});
	
	$(document).on("click",".deleteBodyParam",function(){ 		
 		$(this).parents("tr").remove(); 		 			 
 	});
	
	
	$(document).on("click", ".deleteWsApiBlock", function(){
		$(this).parents(".wsApiMappingBlock").remove();
		webService.changeIndices();
		if($("#wsApiMappingBlocks .wsApiMappingBlock").length == "0"){
			$(".saveWebService").prop("disabled",true);
		}
	});
	
	$(document).on("click", ".addNewWsApiBlock", function(){
		var wsApiMappingBlock = $("#wsApiBlock .wsApiMappingBlock").clone();
		
		
			var addBodyParam = wsApiMappingBlock.find("#columnsParameters").clone().removeAttr("id").show();
			addBodyParam.find(".deleteMappingField").remove();
			addBodyParam.find(".columnKey").val('');
			addBodyParam.find(".columnValue").val('');
			wsApiMappingBlock.find("#defaultMappingDiv").append(addBodyParam);
	 
		$("#wsApiMappingBlocks").append(wsApiMappingBlock);
		webService.changeIndices();
		$(".saveWebService").prop("disabled",false);
	});
	

	var mappingDetails = $("#mappingDetails").val();
	if(mappingDetails == ''){
		$(".addNewWsApiBlock").trigger("click");
	}
	
	$(document).on("click",".saveWebService", function(){
		common.clearValidations([".apiName, .apiUrl, .methodTypeValidation, .paginationValidation, .paramValue, .pathParamValue, .subUrl, " +
				".subUrlResObj, .subUrlMethodTypeValidation,.subUrlPaginationValidation, .pathParamValueType, .responseObjectName, " +
				".paginationOffSetRequestParamName,.paginationOffSetRequestParamValue,.incrementalUpdateParamName," +
				".paginationLimitRequestParamName,.paginationLimitRequestParamValue," +
				".paginationPageNumberRequestParamName,.paginationPageNumberRequestParamValue," +
				".paginationPageSizeRequestParamName,.paginationPageSizeRequestParamValue," +
				".paginationIncrementalDatePageNumberRequestParamName,.paginationIncrementalDatePageNumberRequestParamValue," +
				".paginationIncrementalDatePageSizeRequestParamName,.paginationIncrementalDatePageSizeRequestParamValue," +
				".paginationDatePageNumberRequestParamName,.paginationDatePageNumberRequestParamValue," +
				".paginationDatePageSizeRequestParamName,.paginationDatePageSizeRequestParamValue," +
				".incrementalUpdateParamvalue, .responseColumnObjectName,.subUrlPaginationRequired,.paginationIncrementalStartDate,.paginationIncrementalEndDate,.paginationIncrementalDateRange," +
				".paginationIncrementalEndDate,.paginationIncrementalStartDateParam,.paginationIncrementalEndDateParam"+
				".subUrlPaginationOffSetRequestParamName," +
				".subUrlPaginationLimitRequestParamName," +
				".subUrlPaginationOffSetRequestParamValue,.subUrlPaginationLimitRequestParamValue," +
				".subUrlPaginationPageNumberRequestParamValue,.subUrlPaginationPageNumberRequestParamValue," +
				".subUrlPaginationPageSizeRequestParamValue,.subUrlPaginationPageSizeRequestParamValue," +
				".subUrlPaginationStartDateParam,.subUrlPaginationStartDate,.subUrlPaginationDateRange," +
				".subUrlPaginationEndDateParam,.paginationEndDateParam,.paginationStartDateParam,.subUrlPaginationOffsetDateType,.paginationOffsetDateType, .paginationOtherRequestKeyParam, .subUrlPaginationOtherRequestParamkey," +
				".subUrlPaginationPageNumberRequestParamName,.subUrlPaginationPageNumberRequestParamValue," +
				".subUrlPaginationPageSizeRequestParamName,.subUrlPaginationPageSizeRequestParamValue," +
				".subUrlPaginationIncrementalDatePageNumberRequestParamName,.subUrlPaginationIncrementalDatePageNumberRequestParamValue," +
				".subUrlPaginationIncrementalDatePageSizeRequestParamName,.subUrlPaginationIncrementalDatePageSizeRequestParamValue," +
				".subUrlPaginationDatePageNumberRequestParamName,.subUrlPaginationDatePageNumberRequestParamValue," +
				".subUrlPaginationDatePageSizeRequestParamName,.subUrlPaginationDatePageSizeRequestParamValue," +
				".subUrlPaginationIncrementalStartDateParam,.subUrlPaginationIncrementalStartDate,.subUrlPaginationIncrementalDateRange," +
				".subUrlPaginationIncrementalEndDateParam,.subUrlPaginationIncrementalEndDate," +
				".paginationFilterName, .paginationConditionalFromDateParam, .paginationConditionalFromDateCondition, .paginationConditionalFromDate, .paginationConditionalParam," +
				".paginationConditionalToDateParam, .paginationConditionalToDateCondition, .paginationConditionalToDate, " +
				".subUrlPaginationFilterName,.subUrlPaginationConditionalFromDateParam, .subUrlPaginationConditionalFromDateCondition, " +
				".subUrlPaginationConditionalFromDate, .subUrlPaginationConditionalParam, .subUrlPaginationConditionalToDateParam, " +
				".subUrlPaginationConditionalToDateCondition, .subUrlPaginationConditionalToDate,.subUrlIncrementalUpdate,.subUrlIncrementalUpdateParamName,.subUrlIncrementalUpdateParamvalue,.subUrlIncrementalUpdateParamType"]);
		var validStatus = true,
			arrayApiName = [];
		
		$("#wsApiMappingBlocks .wsApiMappingBlock").each(function(i,val){
			var apiName = $(this).find(".apiName").val(),
				apiUrl = $(this).find(".apiUrl").val(),
				baseUrlRequired = $(this).find(".baseUrlRequired").val(),
				apiMethodType = $(this).find('.apiMethodType').is(':checked'),
				apiMethodTypeVal = $(this).find("input[name='webServiceApis[0].apiMethodType']:checked").val(),
				apiResObj = $(this).find(".responseObjectName").val(),
				soapBodyElement = $(this).find(".soapBodyElement").val(),
				requestParamsArray = [],
				bodyParamsArray = [],
				inclUpdateParamsArray = [],
				paginationRequestParamsArray = [],
				pathParamsArray = [],
				collapseDiv = $(this).find(".collapseDiv"),
				status = true;
			
			if(apiName == ''){
				common.showcustommsg($(this).find(".apiName"), globalMessage['anvizent.package.label.pleaseEnterAPIName'], $(this).find(".apiName"));
				status = false;
			}else{
				arrayApiName.push(apiName);
			} 
			
			if(apiUrl == ''){
				common.showcustommsg($(this).find(".apiUrl"), globalMessage['anvizent.package.label.pleaseEnterAPIURL'], $(this).find(".apiUrl"));
				status = false;
			}
			if(!apiMethodType){
				common.showcustommsg($(this).find(".methodTypeValidation"), globalMessage['anvizent.package.label.pleaseChooseMethodType'], 
						$(this).find(".methodTypeValidation"));
				status = false;
			}
			
			// Request Params
			$(this).find("#requestParamsTable tbody tr").each(function(){
				var isMandatory = $(this).find(".isMandatory").is(":checked"),
					isPassword = $(this).find(".isPassword").is(":checked"),
					paramValue = $(this).find(".paramValue").val()
				if(((isMandatory || isPassword) ) && paramValue == ''){
					common.showcustommsg($(this).find(".paramValue"), globalMessage['anvizent.package.label.pleaseEnterValue'], $(this).find(".paramValue"));
					status = false;
				}
				else if(paramValue != ''){
					var paramsObj = {};
					paramsObj['paramName'] = paramValue;
					paramsObj['isMandatory'] = isMandatory;
					paramsObj['ispasswordField'] = isPassword;
						
					if(!$.isEmptyObject(paramsObj))
						requestParamsArray.push(paramsObj);
				}
			});
			var defaultMappingColumns = {};
			$(this).find("#defaultMappingDiv .columnsKeyValue").each(function(){
					columnKey = $(this).find(".columnKey").val();
					columnValue = $(this).find(".columnValue").val();
					if(columnKey != '' && columnValue!= ''){
						defaultMappingColumns[columnKey]=columnValue;
					}
			});
		    $(this).find(".defaultMapping").val(JSON.stringify(defaultMappingColumns));
			
			if(requestParamsArray.length > 0)
				$(this).find(".reqParams").val(JSON.stringify(requestParamsArray));
			else
				$(this).find(".reqParams").val("");
			

			// Body Params
			$(this).find(".bodyParams").val("");
			if ( apiMethodTypeVal == "POST" ) {
				$(this).find("#bodyParamsTable tbody tr").each(function(){
					var isMandatory = $(this).find(".isMandatory").is(":checked"),
						isPassword = $(this).find(".isPassword").is(":checked"),
						paramValue = $(this).find(".paramValue").val()
					if(((isMandatory || isPassword) ) && paramValue == ''){
						common.showcustommsg($(this).find(".paramValue"), globalMessage['anvizent.package.label.pleaseEnterValue'], $(this).find(".paramValue"));
						status = false;
					}
					else if(paramValue != ''){
						var paramsObj = {};
						paramsObj['paramName'] = paramValue;
						paramsObj['isMandatory'] = isMandatory;
						paramsObj['ispasswordField'] = isPassword;
							
						if(!$.isEmptyObject(paramsObj))
							bodyParamsArray.push(paramsObj);
					}
				});
				
				if(bodyParamsArray.length > 0)
					$(this).	find(".bodyParams").val(JSON.stringify(bodyParamsArray));
			}
			 
			var paginationRequestParamsObject = {};
			var paginationOption = $($(this).closest("div.wsApiMappingBlock")).find(".paginationRequired:checked").val();
			if( paginationOption == undefined || paginationOption == null || paginationOption == '' ){
				common.showcustommsg($(this).find(".paginationValidation"), globalMessage['anvizent.package.label.pleasechoosepagination'], $(this).find(".paginationValidation"));
				status = false;
			}
			var paginationRequired = $($(this).closest("div.wsApiMappingBlock")).find(".paginationRequired:checked").val() == 'yes' ? true : false ;
			var paginationType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOffsetDateType:checked").val();
			console.log("paginationType",paginationType);
			var paginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOffSetType");
			var paginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationPageNumberSizeType");
			var paginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationDateType");
			var paginationIncrementalDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationIncrementalDateType");
			var paginationOtherType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOtherType");
			var paginationParamType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationParamType");
			var paginationConditionalType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationConditionalType");
			if ( paginationRequired ) {
				if(paginationType != 'hypermedia'){
					paginationRequestParamsObject['paginationParamType']  =  paginationParamType.val();
				}
				 
			 if(paginationType != 'undefined' && paginationType != null){ 
				if(paginationType == 'offset'){
					var paginationOffSetRequestParamName =  paginationOffSetTypeDiv.find(".paginationOffSetRequestParamName").val();
					var paginationOffSetRequestParamValue = paginationOffSetTypeDiv.find(".paginationOffSetRequestParamValue").val();
					var paginationLimitRequestParamName =   paginationOffSetTypeDiv.find(".paginationLimitRequestParamName").val();
					var paginationLimitRequestParamValue =  paginationOffSetTypeDiv.find(".paginationLimitRequestParamValue").val();
					var paginationObjectName =  paginationOffSetTypeDiv.find(".paginationObjectName").val();
					var paginationSearchId =  paginationOffSetTypeDiv.find(".paginationSearchId").val();
					var PaginationSoapBody =  paginationOffSetTypeDiv.find(".PaginationSoapBody").val();
					
					if(paginationOffSetRequestParamName == ''){
						common.showcustommsg(paginationOffSetTypeDiv.find(".paginationOffSetRequestParamName"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationOffSetTypeDiv.find(".paginationOffSetRequestParamName"));
						status = false;
					}
					if(paginationOffSetRequestParamValue == '' || paginationOffSetRequestParamValue == 0 || !$.isNumeric(paginationOffSetRequestParamValue)){
						common.showcustommsg(paginationOffSetTypeDiv.find(".paginationOffSetRequestParamValue"), globalMessage['anvizent.package.label.pleaseenternumericvalueexcept0'], paginationOffSetTypeDiv.find(".paginationOffSetRequestParamValue"));
						status = false;
					}
					if(paginationLimitRequestParamName == ''){
						common.showcustommsg(paginationOffSetTypeDiv.find(".paginationLimitRequestParamName"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationOffSetTypeDiv.find(".paginationLimitRequestParamName"));
						status = false;
					}
					if(paginationLimitRequestParamValue == '' || paginationLimitRequestParamValue == 0 || !$.isNumeric(paginationLimitRequestParamValue)){
						common.showcustommsg(paginationOffSetTypeDiv.find(".paginationLimitRequestParamValue"),globalMessage['anvizent.package.label.pleaseenternumericvalueexcept0'], paginationOffSetTypeDiv.find(".paginationLimitRequestParamValue"));
						status = false;
					}
					
					paginationRequestParamsObject['paginationOffSetRequestParamName']  =  paginationOffSetRequestParamName;
					paginationRequestParamsObject['paginationOffSetRequestParamValue'] = paginationOffSetRequestParamValue;
					paginationRequestParamsObject['paginationLimitRequestParamName'] = paginationLimitRequestParamName;
					paginationRequestParamsObject['paginationLimitRequestParamValue']  =  paginationLimitRequestParamValue;
					paginationRequestParamsObject['paginationObjectName']  =  paginationObjectName;
					paginationRequestParamsObject['paginationSearchId']  =  paginationSearchId;
					paginationRequestParamsObject['PaginationSoapBody']  =  PaginationSoapBody;
					
				}else if(paginationType == 'page'){
					var paginationPageNumberRequestParamName =  paginationPageNumberSizeTypeDiv.find(".paginationPageNumberRequestParamName").val();
					var paginationPageNumberRequestParamValue = paginationPageNumberSizeTypeDiv.find(".paginationPageNumberRequestParamValue").val();
					var paginationPageSizeRequestParamName =   paginationPageNumberSizeTypeDiv.find(".paginationPageSizeRequestParamName").val();
					var paginationPageSizeRequestParamValue =  paginationPageNumberSizeTypeDiv.find(".paginationPageSizeRequestParamValue").val();
					
					if(paginationPageNumberRequestParamName == ''){
						common.showcustommsg(paginationPageNumberSizeTypeDiv.find(".paginationPageNumberRequestParamName"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationPageNumberSizeTypeDiv.find(".paginationPageNumberRequestParamName"));
						status = false;
					}
					if(paginationPageNumberRequestParamValue == '' ||  !$.isNumeric(paginationPageNumberRequestParamValue)){
						common.showcustommsg(paginationPageNumberSizeTypeDiv.find(".paginationPageNumberRequestParamValue"), globalMessage['anvizent.package.label.pleaseenternumericvalue'], paginationPageNumberSizeTypeDiv.find(".paginationPageNumberRequestParamValue"));
						status = false;
					}
					if(paginationPageSizeRequestParamName == ''){
						common.showcustommsg(paginationPageNumberSizeTypeDiv.find(".paginationPageSizeRequestParamName"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationPageNumberSizeTypeDiv.find(".paginationPageSizeRequestParamName"));
						status = false;
					}
					if(paginationPageSizeRequestParamValue == '' || paginationPageSizeRequestParamValue == 0 || !$.isNumeric(paginationPageSizeRequestParamValue)){
						common.showcustommsg(paginationPageNumberSizeTypeDiv.find(".paginationPageSizeRequestParamValue"),globalMessage['anvizent.package.label.pleaseenternumericvalueexcept0'], paginationPageNumberSizeTypeDiv.find(".paginationPageSizeRequestParamValue"));
						status = false;
					}
					
					paginationRequestParamsObject['paginationPageNumberRequestParamName']  =  paginationPageNumberRequestParamName;
					paginationRequestParamsObject['paginationPageNumberRequestParamValue'] = paginationPageNumberRequestParamValue;
					paginationRequestParamsObject['paginationPageSizeRequestParamName'] = paginationPageSizeRequestParamName;
					paginationRequestParamsObject['paginationPageSizeRequestParamValue']  =  paginationPageSizeRequestParamValue;
					
				}else if(paginationType == 'date'){
					var paginationStartDateParam =  paginationDateType.find(".paginationStartDateParam").val();
					var paginationEndDateParam =   paginationDateType.find(".paginationEndDateParam").val();
					var paginationStartDate = paginationDateType.find(".paginationStartDate").val();
					var paginationDateRange =   paginationDateType.find(".paginationDateRange").val();
					var paginationDatePageNumberRequestParamName =  paginationDateType.find(".paginationDatePageNumberRequestParamName").val();
					var paginationDatePageNumberRequestParamValue = paginationDateType.find(".paginationDatePageNumberRequestParamValue").val();
					var paginationDatePageSizeRequestParamName =   paginationDateType.find(".paginationDatePageSizeRequestParamName").val();
					var paginationDatePageSizeRequestParamValue =  paginationDateType.find(".paginationDatePageSizeRequestParamValue").val();
					
					if(paginationStartDateParam == ''){ 
						common.showcustommsg(paginationDateType.find(".paginationStartDateParam"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationDateType.find(".paginationStartDateParam"));
						status = false;
					}
					if(paginationEndDateParam == ''){ 
						common.showcustommsg(paginationDateType.find(".paginationEndDateParam"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationDateType.find(".paginationEndDateParam"));
						status = false;
					}
					if(paginationStartDate == ''){
						common.showcustommsg(paginationDateType.find(".paginationStartDate"), globalMessage['anvizent.package.label.pleasechoosestartdate'],paginationDateType.find(".paginationStartDate"));
						status = false;
					}
					if(paginationDateRange == ''){
						common.showcustommsg(paginationDateType.find(".paginationDateRange"),globalMessage['anvizent.package.label.pleaseSelectdaterange'],paginationDateType.find(".paginationDateRange"));
						status = false;
					}
					if(paginationDatePageNumberRequestParamName != '' ){ 
						if(!$.isNumeric(paginationDatePageNumberRequestParamValue)){
						common.showcustommsg(paginationDateType.find(".paginationDatePageNumberRequestParamValue"), globalMessage['anvizent.package.label.pleaseenternumericvalue'], paginationDateType.find(".paginationDatePageNumberRequestParamValue"));
						status = false;
						}
					}
					if(paginationDatePageSizeRequestParamName != '' ){ 
						if( paginationDatePageSizeRequestParamValue == 0 || !$.isNumeric(paginationDatePageSizeRequestParamValue)){
						common.showcustommsg(paginationDateType.find(".paginationDatePageSizeRequestParamValue"), globalMessage['anvizent.package.label.pleaseenternumericvalueexcept0'], paginationDateType.find(".paginationDatePageSizeRequestParamValue"));
						status = false;
						}
					}
					paginationRequestParamsObject['paginationStartDateParam']  =  paginationStartDateParam;
					paginationRequestParamsObject['paginationEndDateParam']  =  paginationEndDateParam;
					paginationRequestParamsObject['paginationStartDate'] = paginationStartDate;
					paginationRequestParamsObject['paginationDateRange'] = paginationDateRange;
					paginationRequestParamsObject['paginationDatePageNumberRequestParamName']  =  paginationDatePageNumberRequestParamName;
					paginationRequestParamsObject['paginationDatePageNumberRequestParamValue'] = paginationDatePageNumberRequestParamValue;
					paginationRequestParamsObject['paginationDatePageSizeRequestParamName'] = paginationDatePageSizeRequestParamName;
					paginationRequestParamsObject['paginationDatePageSizeRequestParamValue']  =  paginationDatePageSizeRequestParamValue;
					
				}else if(paginationType == 'incrementaldate'){
					var paginationIncrementalStartDateParam =  paginationIncrementalDateType.find(".paginationIncrementalStartDateParam").val();
					var paginationIncrementalEndDateParam =   paginationIncrementalDateType.find(".paginationIncrementalEndDateParam").val();
					var paginationIncrementalStartDate = paginationIncrementalDateType.find(".paginationIncrementalStartDate").val();
					var paginationIncrementalEndDate =   paginationIncrementalDateType.find(".paginationIncrementalEndDate").val();
					var paginationIncrementalDateRange =   paginationIncrementalDateType.find(".paginationIncrementalDateRange").val();
					var paginationIncrementalDatePageNumberRequestParamName =  paginationIncrementalDateType.find(".paginationIncrementalDatePageNumberRequestParamName").val();
					var paginationIncrementalDatePageNumberRequestParamValue = paginationIncrementalDateType.find(".paginationIncrementalDatePageNumberRequestParamValue").val();
					var paginationIncrementalDatePageSizeRequestParamName =   paginationIncrementalDateType.find(".paginationIncrementalDatePageSizeRequestParamName").val();
					var paginationIncrementalDatePageSizeRequestParamValue =  paginationIncrementalDateType.find(".paginationIncrementalDatePageSizeRequestParamValue").val();
					
					if(paginationIncrementalStartDateParam == ''){ 
						common.showcustommsg(paginationIncrementalDateType.find(".paginationIncrementalStartDateParam"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationIncrementalDateType.find(".paginationIncrementalStartDateParam"));
						status = false;
					}
					if(paginationIncrementalEndDateParam == ''){ 
						common.showcustommsg(paginationIncrementalDateType.find(".paginationIncrementalEndDateParam"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationIncrementalDateType.find(".paginationIncrementalEndDateParam"));
						status = false;
					}
					if(paginationIncrementalStartDate == ''){
						common.showcustommsg(paginationIncrementalDateType.find(".paginationIncrementalStartDate"), globalMessage['anvizent.package.label.pleasechoosestartdate'],paginationIncrementalDateType.find(".paginationIncrementalStartDate"));
						status = false;
					}
					/*if(paginationIncrementalEndDate == ''){
						common.showcustommsg(paginationIncrementalDateType.find(".paginationIncrementalEndDate"),globalMessage['anvizent.package.label.pleasechooseenddate'],paginationIncrementalDateType.find(".paginationIncrementalEndDate"));
						status = false;
					}*/
					if(paginationIncrementalDateRange == ''){
						common.showcustommsg(paginationIncrementalDateType.find(".paginationIncrementalDateRange"),globalMessage['anvizent.package.label.pleaseSelectdaterange'],paginationIncrementalDateType.find(".paginationIncrementalDateRange"));
						status = false;
					}
					if(new Date(paginationIncrementalStartDate) >= new Date(paginationIncrementalEndDate))
					{
						common.showcustommsg(paginationIncrementalDateType.find(".paginationIncrementalEndDate"),globalMessage['anvizent.package.message.EnddateshouldbegreaterthanStartdate'],paginationIncrementalDateType.find(".paginationIncrementalEndDate"));
						status = false;
					}
					
					if(paginationIncrementalDatePageNumberRequestParamName != '' ){ 
						if(!$.isNumeric(paginationIncrementalDatePageNumberRequestParamValue)){
						common.showcustommsg(paginationIncrementalDateType.find(".paginationIncrementalDatePageNumberRequestParamValue"), globalMessage['anvizent.package.label.pleaseenternumericvalue'], paginationIncrementalDateType.find(".paginationIncrementalDatePageNumberRequestParamValue"));
						status = false;
						}
					}
					if(paginationIncrementalDatePageSizeRequestParamName != '' ){ 
						if( paginationIncrementalDatePageSizeRequestParamValue == 0 || !$.isNumeric(paginationIncrementalDatePageSizeRequestParamValue)){
						common.showcustommsg(paginationIncrementalDateType.find(".paginationIncrementalDatePageSizeRequestParamValue"), globalMessage['anvizent.package.label.pleaseenternumericvalueexcept0'], paginationIncrementalDateType.find(".paginationIncrementalDatePageSizeRequestParamValue"));
						status = false;
						}
					}
					
					paginationRequestParamsObject['paginationIncrementalStartDateParam']  =  paginationIncrementalStartDateParam;
					paginationRequestParamsObject['paginationIncrementalEndDateParam']  =  paginationIncrementalEndDateParam;
					paginationRequestParamsObject['paginationIncrementalStartDate'] = paginationIncrementalStartDate;
					paginationRequestParamsObject['paginationIncrementalEndDate'] = paginationIncrementalEndDate;
					paginationRequestParamsObject['paginationIncrementalDateRange'] = paginationIncrementalDateRange;
					paginationRequestParamsObject['paginationIncrementalStartDateParam']  =  paginationIncrementalStartDateParam;
					paginationRequestParamsObject['paginationIncrementalDatePageNumberRequestParamName']  =  paginationIncrementalDatePageNumberRequestParamName;
					paginationRequestParamsObject['paginationIncrementalDatePageNumberRequestParamValue'] = paginationIncrementalDatePageNumberRequestParamValue;
					paginationRequestParamsObject['paginationIncrementalDatePageSizeRequestParamName'] = paginationIncrementalDatePageSizeRequestParamName;
					paginationRequestParamsObject['paginationIncrementalDatePageSizeRequestParamValue'] = paginationIncrementalDatePageSizeRequestParamValue;
					
					
				}else if(paginationType == 'conditionaldate'){
					var paginationConditionalFilter  = paginationConditionalType.find(".paginationFilterName").val();
					var paginationConditionalFromDateParam  = paginationConditionalType.find(".paginationConditionalFromDateParam").val();
					var paginationConditionalFromDate  = paginationConditionalType.find(".paginationConditionalFromDate").val();
					var paginationConditionalToDateParam  = paginationConditionalType.find(".paginationConditionalToDateParam").val();
					var paginationConditionalToDate  = paginationConditionalType.find(".paginationConditionalToDate").val();
					var paginationConditionalFromDateCondition  = paginationConditionalType.find(".paginationConditionalFromDateCondition").val();
					var paginationConditionalToDateCondition  = paginationConditionalType.find(".paginationConditionalToDateCondition").val();
					var paginationConditionalParam  = paginationConditionalType.find(".paginationConditionalParam").val();
					var paginationConditionalDayRange = paginationConditionalType.find(".paginationConditionalDayRange").val();
					
					if(paginationConditionalFilter == ''){ 
						common.showcustommsg(paginationConditionalType.find(".paginationFilterName"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationConditionalType.find(".paginationFilterName"));
						status = false;
					}
					if(paginationConditionalFromDateParam == ''){ 
						common.showcustommsg(paginationConditionalType.find(".paginationConditionalFromDateParam"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationConditionalType.find(".paginationConditionalFromDateParam"));
						status = false;
					}
					if(paginationConditionalFromDate == ''){ 
						common.showcustommsg(paginationConditionalType.find(".paginationConditionalFromDate"), globalMessage['anvizent.package.message.pleasechoosefromdate'], paginationConditionalType.find(".paginationConditionalFromDate"));
						status = false;
					}
					if(paginationConditionalToDateParam == ''){ 
						common.showcustommsg(paginationConditionalType.find(".paginationConditionalToDateParam"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationConditionalType.find(".paginationConditionalToDateParam"));
						status = false;
					}
					/*if(paginationConditionalToDate == ''){ 
						common.showcustommsg(paginationConditionalType.find(".paginationConditionalToDate"), globalMessage['anvizent.package.message.pleasechoosetodate'], paginationConditionalType.find(".paginationConditionalToDate"));
						status = false;
					}*/
					if(paginationConditionalFromDateCondition == ''){ 
						common.showcustommsg(paginationConditionalType.find(".paginationConditionalFromDateCondition"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationConditionalType.find(".paginationConditionalFromDateCondition"));
						status = false;
					}
					if(paginationConditionalToDateCondition == ''){ 
						common.showcustommsg(paginationConditionalType.find(".paginationConditionalToDateCondition"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationConditionalType.find(".paginationConditionalToDateCondition"));
						status = false;
					}
					if(paginationConditionalParam == ''){ 
						common.showcustommsg(paginationConditionalType.find(".paginationConditionalParam"), globalMessage['anvizent.package.label.pleaseEnterValue'], paginationConditionalType.find(".paginationConditionalParam"));
						status = false;
					}
					if(new Date(paginationConditionalFromDate) >= new Date(paginationConditionalToDate))
					{
						common.showcustommsg(paginationConditionalType.find(".paginationConditionalToDate"), globalMessage['anvizent.package.message.EnddateshouldbegreaterthanStartdate'], paginationConditionalType.find(".paginationConditionalToDate"));
						status = false;
					}
					paginationRequestParamsObject['paginationFilterName'] = paginationConditionalFilter;
					paginationRequestParamsObject['paginationConditionalFromDateParam'] = paginationConditionalFromDateParam;
					paginationRequestParamsObject['paginationConditionalFromDateCondition'] = paginationConditionalFromDateCondition;
					paginationRequestParamsObject['paginationConditionalFromDate'] = paginationConditionalFromDate;
					paginationRequestParamsObject['paginationConditionalParam'] = paginationConditionalParam;
					paginationRequestParamsObject['paginationConditionalToDateParam'] = paginationConditionalToDateParam;
					paginationRequestParamsObject['paginationConditionalToDateCondition'] = paginationConditionalToDateCondition;
					paginationRequestParamsObject['paginationConditionalToDate'] = paginationConditionalToDate;
					paginationRequestParamsObject['paginationConditionalDayRange'] = paginationConditionalDayRange;
				}else {
					var paginationOtherRequestKeyParam = paginationOtherType.find(".paginationOtherRequestKeyParam").val();
					var paginationOtherRequestLimit = paginationOtherType.find(".paginationOtherRequestLimit").val();
					if(paginationOtherRequestKeyParam == ''){ 
						common.showcustommsg(paginationOtherType.find(".paginationOtherRequestKeyParam"), globalMessage['anvizent.package.label.pleaseEnterKeyParam'], paginationOtherType.find(".paginationOtherRequestKeyParam"));
						status = false;
					}
					
					paginationRequestParamsObject['paginationHyperLinkPattern'] = paginationOtherRequestKeyParam;
					paginationRequestParamsObject['paginationHypermediaPageLimit'] = paginationOtherRequestLimit;
				}
				
				if(!$.isEmptyObject(paginationRequestParamsObject))
					paginationRequestParamsArray.push(paginationRequestParamsObject);
			 }else{
				 common.showcustommsg($($(this).closest("div.wsApiMappingBlock")).find(".paginationOffsetDateType"),'please choose pagination type.',$($(this).closest("div.wsApiMappingBlock")).find(".paginationOffsetDateType"));
				 status = false;
			 }
		   }
			if(paginationRequestParamsArray.length > 0)
				$($(this).closest("div.wsApiMappingBlock")).find(".paginationRequestParamsData").val(JSON.stringify(paginationRequestParamsArray));
			else
				$($(this).closest("div.wsApiMappingBlock")).find(".paginationRequestParamsData").val("");
		 
			var incrementalUpdate = $(this).find(".incrementalUpdate").prop("checked");
			var incrementalUpdateDetailsDiv = $($(this).closest("div.wsApiMappingBlock")).find(".incrementalUpdateDetailsDiv");
			if ( incrementalUpdate ) {
				var incrementalUpdateParamName = incrementalUpdateDetailsDiv.find(".incrementalUpdateParamName").val();
				var incrementalUpdateParamvalue = incrementalUpdateDetailsDiv.find(".incrementalUpdateParamvalue").val();
				var incrementalUpdateParamColumnName = incrementalUpdateDetailsDiv.find(".incrementalUpdateParamColumnName").val();
				var incrementalUpdateParamType = incrementalUpdateDetailsDiv.find(".incrementalUpdateParamType").val();
				
				if ( incrementalUpdateParamName.trim().length == 0 ) {
					common.showcustommsg(incrementalUpdateDetailsDiv.find(".incrementalUpdateParamName"), globalMessage['anvizent.package.label.pleaseEnterValue'], incrementalUpdateDetailsDiv.find(".incrementalUpdateParamName") );
					status = false;
				}
				if ( incrementalUpdateParamvalue.trim().length == 0 ) {
					common.showcustommsg(incrementalUpdateDetailsDiv.find(".incrementalUpdateParamvalue"), globalMessage['anvizent.package.label.pleaseEnterValue'], incrementalUpdateDetailsDiv.find(".incrementalUpdateParamvalue") );
					status = false;
				}
				
				var inclUpdateParamsObject = {};
				inclUpdateParamsObject['incrementalUpdateParamName'] = incrementalUpdateParamName;
				inclUpdateParamsObject['incrementalUpdateParamvalue'] = incrementalUpdateParamvalue;
				inclUpdateParamsObject['incrementalUpdateParamColumnName'] = incrementalUpdateParamName;
				inclUpdateParamsObject['incrementalUpdateParamType'] = incrementalUpdateParamType;
				
				if(!$.isEmptyObject(inclUpdateParamsObject))
					inclUpdateParamsArray.push(inclUpdateParamsObject);
			} 
			if(inclUpdateParamsArray.length > 0)
				$(this).find(".incrementalUpdateparamdata").val(JSON.stringify(inclUpdateParamsArray));
			else
				$(this).find(".incrementalUpdateparamdata").val("");
		
			
			//Path Params
			$(this).find(".pathParamDetailsBlock").each(function(){
				var pathParamDetailsBlock = $(this),
					paramValueTypeEle = $(pathParamDetailsBlock).find(".pathParamValueType"),
					pathParamsObj = {};
				
				if(!$(paramValueTypeEle).is(":checked")){
					common.showcustommsg($(paramValueTypeEle).parents(".methodTypeValidation") ,globalMessage['anvizent.package.label.pleasechoosepathparamvaluetype'], 
							$(paramValueTypeEle).parents(".methodTypeValidation"));
					status = false;
				}
				else{
					var paramValueType = $(pathParamDetailsBlock).find(".pathParamValueType:checked").val();
					pathParamsObj["paramName"] = $(pathParamDetailsBlock).find(".pathParamName").text();

					if(paramValueType == "M"){
						pathParamsObj["valueType"] = "M";
						pathParamsObj["subUrldetails"] = {};
					}
					else{
						pathParamsObj["valueType"] = "S";
						var subUrlEle = $(pathParamDetailsBlock).find(".subUrl"),
						subUrlMethodTypeEle = $(pathParamDetailsBlock).find(".subUrlMethodType"),
						subUrlResObj = $(pathParamDetailsBlock).find(".subUrlResObj"),
						subUrlPaginationOffSetType = $(pathParamDetailsBlock).find(".subUrlPaginationOffSetType"),
						subUrlPaginationPageNumberSizeType = $(pathParamDetailsBlock).find(".subUrlPaginationPageNumberSizeType"),
						subUrlPaginationDateTypeEle = $(pathParamDetailsBlock).find(".subUrlPaginationDateType"),
						subUrlPaginationIncrementalDateTypeEle = $(pathParamDetailsBlock).find(".subUrlPaginationIncrementalDateType"),
						subUrlPaginationOtherType = $(pathParamDetailsBlock).find(".subUrlPaginationOtherType"),
						subUrlPaginationConditionalType = $(pathParamDetailsBlock).find(".subUrlPaginationConditionalType");
						subUrldetailsObj = {};
						
						if($(subUrlEle).val() == ''){
							common.showcustommsg($(subUrlEle) ,globalMessage['anvizent.package.label.pleaseentersuburl'] , $(subUrlEle));
							status = false;
							subUrldetailsObj["url"] = "";
						}
						else{
							subUrldetailsObj["url"] = $(subUrlEle).val();
						}
						
						if(!$(subUrlMethodTypeEle).is(":checked")){
							common.showcustommsg($(subUrlMethodTypeEle).parents(".subUrlMethodTypeValidation") ,globalMessage['anvizent.package.label.pleaseChooseMethodType']
							, $(subUrlMethodTypeEle).parents(".subUrlMethodTypeValidation"));
							status = false;
							subUrldetailsObj["methodType"] = "";
						}
						else{
							subUrldetailsObj["methodType"] = $(pathParamDetailsBlock).find(".subUrlMethodType:checked").val();
						}
					 
						subUrldetailsObj["responseObjName"] = $(subUrlResObj).val();
						var subUrlPaginationOption = $(pathParamDetailsBlock).find(".subUrlPaginationRequired:checked").val();
						if( subUrlPaginationOption == undefined || subUrlPaginationOption == null || subUrlPaginationOption == '' ){
						   var $subUrlPaginationOption = $(pathParamDetailsBlock).find(".subUrlPaginationValidation");
						   common.showcustommsg($subUrlPaginationOption, globalMessage['anvizent.package.label.pleasechoosepagination'] , $subUrlPaginationOption);
						   status = false;
						}
						var subUrlPaginationRequired = $(pathParamDetailsBlock).find(".subUrlPaginationRequired:checked").val() == 'yes' ? true : false;
						var subUrlPaginationType = $(pathParamDetailsBlock).find(".subUrlPaginationOffsetDateType:checked").val();
						if(subUrlPaginationRequired){
							var subUrlPaginationParamType = $(pathParamDetailsBlock).find(".subUrlPaginationParamType").val();
							if(subUrlPaginationType != 'hypermedia'){
								subUrldetailsObj["subUrlPaginationParamType"] = subUrlPaginationParamType;
							}
							subUrldetailsObj["subUrlPaginationRequired"] = subUrlPaginationRequired;
						 if(subUrlPaginationType != 'undefined' && subUrlPaginationType != null){
						  if(subUrlPaginationType == 'offset'){
							var subUrlPaginationOffSetRequestParamName = subUrlPaginationOffSetType.find('.subUrlPaginationOffSetRequestParamName').val();
							var subUrlPaginationOffSetRequestParamValue = subUrlPaginationOffSetType.find('.subUrlPaginationOffSetRequestParamValue').val();
							var subUrlPaginationLimitRequestParamName = subUrlPaginationOffSetType.find('.subUrlPaginationLimitRequestParamName').val();
							var subUrlPaginationLimitRequestParamValue = subUrlPaginationOffSetType.find('.subUrlPaginationLimitRequestParamValue').val();
							
							if(subUrlPaginationOffSetRequestParamName == ''){
								common.showcustommsg(subUrlPaginationOffSetType.find('.subUrlPaginationOffSetRequestParamName') ,globalMessage['anvizent.package.label.pleaseentersuburloffsetparamname'] , subUrlPaginationOffSetType.find('.subUrlPaginationOffSetRequestParamName'));
								status = false;
								subUrldetailsObj["subUrlPaginationOffSetRequestParamName"] = "";
							}else{
								subUrldetailsObj["subUrlPaginationOffSetRequestParamName"] = subUrlPaginationOffSetRequestParamName;
							}
							if(subUrlPaginationOffSetRequestParamValue == ''){
								common.showcustommsg(subUrlPaginationOffSetType.find('.subUrlPaginationOffSetRequestParamValue') ,globalMessage['anvizent.package.label.pleaseentersuburloffsetparamvalue'] , subUrlPaginationOffSetType.find('.subUrlPaginationOffSetRequestParamValue'));
								status = false;
								subUrldetailsObj["subUrlPaginationOffSetRequestParamValue"] = "";				
								}else{
									subUrldetailsObj["subUrlPaginationOffSetRequestParamValue"] = subUrlPaginationOffSetRequestParamValue;	
								}
							if(subUrlPaginationLimitRequestParamName == ''){
								common.showcustommsg(subUrlPaginationOffSetType.find('.subUrlPaginationLimitRequestParamName') ,globalMessage['anvizent.package.label.pleaseentersuburllimitparamname'], subUrlPaginationOffSetType.find('.subUrlPaginationLimitRequestParamName'));
								status = false;
								subUrldetailsObj["subUrlPaginationLimitRequestParamName"] = "";
							}else{
								subUrldetailsObj["subUrlPaginationLimitRequestParamName"] = subUrlPaginationLimitRequestParamName;
							}
							if(subUrlPaginationLimitRequestParamValue == ''){
								common.showcustommsg(subUrlPaginationOffSetType.find('.subUrlPaginationLimitRequestParamValue') ,globalMessage['anvizent.package.label.pleaseentersuburllimitparamvalue'], subUrlPaginationOffSetType.find('.subUrlPaginationLimitRequestParamValue'));
								status = false;
								subUrldetailsObj["subUrlPaginationLimitRequestParamValue"] = "";
							}else{
								subUrldetailsObj["subUrlPaginationLimitRequestParamValue"] = subUrlPaginationLimitRequestParamValue;
							}
							 subUrldetailsObj['subUrlPaginationType']  =  subUrlPaginationType;
							} else if(subUrlPaginationType == 'page'){
								var subUrlPaginationPageNumberRequestParamName = subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageNumberRequestParamName').val();
								var subUrlPaginationPageNumberRequestParamValue = subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageNumberRequestParamValue').val();
								var subUrlPaginationPageSizeRequestParamName = subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageSizeRequestParamName').val();
								var subUrlPaginationPageSizeRequestParamValue = subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageSizeRequestParamValue').val();
								
								if(subUrlPaginationPageNumberRequestParamName == ''){
									common.showcustommsg(subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageNumberRequestParamName') ,globalMessage['anvizent.package.label.pleaseentersuburloffsetparamname'] , subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageNumberRequestParamName'));
									status = false;
									subUrldetailsObj["subUrlPaginationPageNumberRequestParamName"] = "";
								}else{
									subUrldetailsObj["subUrlPaginationPageNumberRequestParamName"] = subUrlPaginationPageNumberRequestParamName;
								}
								if(subUrlPaginationPageNumberRequestParamValue == ''){
									common.showcustommsg(subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageNumberRequestParamValue') ,globalMessage['anvizent.package.label.pleaseentersuburloffsetparamvalue'] , subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageNumberRequestParamValue'));
									status = false;
									subUrldetailsObj["subUrlPaginationPageNumberRequestParamValue"] = "";				
									}else{
										subUrldetailsObj["subUrlPaginationPageNumberRequestParamValue"] = subUrlPaginationPageNumberRequestParamValue;	
									}
								if(subUrlPaginationPageSizeRequestParamName == ''){
									common.showcustommsg(subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageSizeRequestParamName') ,globalMessage['anvizent.package.label.pleaseentersuburllimitparamname'], subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageSizeRequestParamName'));
									status = false;
									subUrldetailsObj["subUrlPaginationPageSizeRequestParamName"] = "";
								}else{
									subUrldetailsObj["subUrlPaginationPageSizeRequestParamName"] = subUrlPaginationPageSizeRequestParamName;
								}
								if(subUrlPaginationPageSizeRequestParamValue == ''){
									common.showcustommsg(subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageSizeRequestParamValue') ,globalMessage['anvizent.package.label.pleaseentersuburllimitparamvalue'], subUrlPaginationPageNumberSizeType.find('.subUrlPaginationPageSizeRequestParamValue'));
									status = false;
									subUrldetailsObj["subUrlPaginationPageSizeRequestParamValue"] = "";
								}else{
									subUrldetailsObj["subUrlPaginationPageSizeRequestParamValue"] = subUrlPaginationPageSizeRequestParamValue;
								}
								 subUrldetailsObj['subUrlPaginationType']  =  subUrlPaginationType;
								}else if(subUrlPaginationType == 'date'){
								var subUrlPaginationStartDateParam = subUrlPaginationDateTypeEle.find('.subUrlPaginationStartDateParam').val();
								var subUrlPaginationStartDate = subUrlPaginationDateTypeEle.find('.subUrlPaginationStartDate').val();
								var subUrlPaginationDateRange = subUrlPaginationDateTypeEle.find('.subUrlPaginationDateRange').val();
								var subUrlPaginationEndDateParam = subUrlPaginationDateTypeEle.find('.subUrlPaginationEndDateParam').val();
								var subUrlPaginationDatePageNumberRequestParamName = subUrlPaginationDateTypeEle.find('.subUrlPaginationDatePageNumberRequestParamName').val();
								var subUrlPaginationDatePageNumberRequestParamValue = subUrlPaginationDateTypeEle.find('.subUrlPaginationDatePageNumberRequestParamValue').val();
								var subUrlPaginationDatePageSizeRequestParamName = subUrlPaginationDateTypeEle.find('.subUrlPaginationDatePageSizeRequestParamName').val();
								var subUrlPaginationDatePageSizeRequestParamValue = subUrlPaginationDateTypeEle.find('.subUrlPaginationDatePageSizeRequestParamValue').val();
								
								if(subUrlPaginationStartDateParam == ''){
									common.showcustommsg(subUrlPaginationDateTypeEle.find('.subUrlPaginationStartDateParam') ,globalMessage['anvizent.package.label.pleaseentersuburlfromdateparamname'] , subUrlPaginationDateTypeEle.find('.subUrlPaginationStartDateParam'));
									status = false;
									subUrldetailsObj["subUrlPaginationStartDateParam"] = "";
								}else{
									subUrldetailsObj["subUrlPaginationStartDateParam"] = subUrlPaginationStartDateParam;
								}
								if(subUrlPaginationStartDate == ''){
									common.showcustommsg(subUrlPaginationDateTypeEle.find('.subUrlPaginationStartDate') ,globalMessage['anvizent.package.label.pleaseentersuburlstartdate'] , subUrlPaginationDateTypeEle.find('.subUrlPaginationStartDate'));
									status = false;
									subUrldetailsObj["subUrlPaginationStartDate"] = "";				
									}else{
										subUrldetailsObj["subUrlPaginationStartDate"] = subUrlPaginationStartDate;	
									}
								if(subUrlPaginationDateRange == ''){
									common.showcustommsg(subUrlPaginationDateTypeEle.find('.subUrlPaginationDateRange') ,globalMessage['anvizent.package.label.pleasechoosedaterange'], subUrlPaginationDateTypeEle.find('.subUrlPaginationDateRange'));
									status = false;
									subUrldetailsObj["subUrlPaginationDateRange"] = "";
								}else{
									subUrldetailsObj["subUrlPaginationDateRange"] = subUrlPaginationDateRange;
								}
								if(subUrlPaginationEndDateParam == ''){
									common.showcustommsg(subUrlPaginationDateTypeEle.find('.subUrlPaginationEndDateParam') ,globalMessage['anvizent.package.label.pleaseentersuburltodateparamname'] , subUrlPaginationDateTypeEle.find('.subUrlPaginationEndDateParam'));
									status = false;
									subUrldetailsObj["subUrlPaginationEndDateParam"] = "";
								}else{
									subUrldetailsObj["subUrlPaginationEndDateParam"] = subUrlPaginationEndDateParam;
								}
								
								if(subUrlPaginationDatePageNumberRequestParamName != '' ){ 
									if(!$.isNumeric(subUrlPaginationDatePageNumberRequestParamValue)){
									common.showcustommsg(subUrlPaginationDateTypeEle.find(".subUrlPaginationDatePageNumberRequestParamValue"), globalMessage['anvizent.package.label.pleaseenternumericvalue'], subUrlPaginationDateTypeEle.find(".subUrlPaginationDatePageNumberRequestParamValue"));
									status = false;
									}
								}
								if(subUrlPaginationDatePageSizeRequestParamName != '' ){ 
									if( subUrlPaginationDatePageSizeRequestParamValue == 0 || !$.isNumeric(subUrlPaginationDatePageSizeRequestParamValue)){
									common.showcustommsg(subUrlPaginationDateTypeEle.find(".subUrlPaginationDatePageSizeRequestParamValue"), globalMessage['anvizent.package.label.pleaseenternumericvalueexcept0'], subUrlPaginationDateTypeEle.find(".subUrlPaginationDatePageSizeRequestParamValue"));
									status = false;
									}
								}
								
								subUrldetailsObj['subUrlPaginationType']  =  subUrlPaginationType;
								subUrldetailsObj['subUrlPaginationDatePageNumberRequestParamName']  =  subUrlPaginationDatePageNumberRequestParamName;
								subUrldetailsObj['subUrlPaginationDatePageNumberRequestParamValue'] = subUrlPaginationDatePageNumberRequestParamValue;
								subUrldetailsObj['subUrlPaginationDatePageSizeRequestParamName'] = subUrlPaginationDatePageSizeRequestParamName;
								subUrldetailsObj['subUrlPaginationDatePageSizeRequestParamValue']  =  subUrlPaginationDatePageSizeRequestParamValue;
							}else if(subUrlPaginationType == 'incrementaldate'){
								var subUrlPaginationIncrementalStartDateParam = subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalStartDateParam').val();
								var subUrlPaginationIncrementalStartDate = subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalStartDate').val();
								var subUrlPaginationIncrementalDateRange = subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalDateRange').val();
								var subUrlPaginationIncrementalEndDateParam = subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalEndDateParam').val();
								var subUrlPaginationIncrementalEndDate = subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalEndDate').val();
								
								var subUrlPaginationIncrementalDatePageNumberRequestParamName = subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalDatePageNumberRequestParamName').val();
								var subUrlPaginationIncrementalDatePageNumberRequestParamValue = subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalDatePageNumberRequestParamValue').val();
								var subUrlPaginationIncrementalDatePageSizeRequestParamName = subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalDatePageSizeRequestParamName').val();
								var subUrlPaginationIncrementalDatePageSizeRequestParamValue = subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalDatePageSizeRequestParamValue').val();
								
								if(subUrlPaginationIncrementalStartDateParam == ''){
									common.showcustommsg(subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalStartDateParam') ,globalMessage['anvizent.package.label.pleaseentersuburlfromdateparamname'] , subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalStartDateParam'));
									status = false;
									subUrldetailsObj["subUrlPaginationIncrementalStartDateParam"] = "";
								}else{
									subUrldetailsObj["subUrlPaginationIncrementalStartDateParam"] = subUrlPaginationIncrementalStartDateParam;
								}
								if(subUrlPaginationIncrementalStartDate == ''){
									common.showcustommsg(subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalStartDate') ,globalMessage['anvizent.package.label.pleaseentersuburlstartdate'] , subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalStartDate'));
									status = false;
									subUrldetailsObj["subUrlPaginationIncrementalStartDate"] = "";				
									}else{
										subUrldetailsObj["subUrlPaginationIncrementalStartDate"] = subUrlPaginationIncrementalStartDate;	
									}
								if(subUrlPaginationIncrementalDateRange == ''){
									common.showcustommsg(subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalDateRange') ,globalMessage['anvizent.package.label.pleasechoosedaterange'], subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalDateRange'));
									status = false;
									subUrldetailsObj["subUrlPaginationIncrementalDateRange"] = "";
								}else{
									subUrldetailsObj["subUrlPaginationIncrementalDateRange"] = subUrlPaginationIncrementalDateRange;
								}
								if(subUrlPaginationIncrementalEndDateParam == ''){
									common.showcustommsg(subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalEndDateParam') ,globalMessage['anvizent.package.label.pleaseentersuburltodateparamname'] , subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalEndDateParam'));
									status = false;
									subUrldetailsObj["subUrlPaginationIncrementalEndDateParam"] = "";
								}else{
									subUrldetailsObj["subUrlPaginationIncrementalEndDateParam"] = subUrlPaginationIncrementalEndDateParam;
								}
								/*if(subUrlPaginationIncrementalEndDate == ''){
									common.showcustommsg(subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalEndDate') ,globalMessage['anvizent.package.label.pleaseentersuburlenddate'] , subUrlPaginationIncrementalDateTypeEle.find('.subUrlPaginationIncrementalEndDate'));
									status = false;
									subUrldetailsObj["subUrlPaginationIncrementalEndDate"] = "";
								}else{
									subUrldetailsObj["subUrlPaginationIncrementalEndDate"] = subUrlPaginationIncrementalEndDate;
								}*/
								subUrldetailsObj["subUrlPaginationIncrementalEndDate"] = subUrlPaginationIncrementalEndDate;
								if(new Date(subUrlPaginationIncrementalStartDate) >= new Date(subUrlPaginationIncrementalEndDate))
								{
									common.showcustommsg(subUrlPaginationIncrementalDateTypeEle.find(".subUrlPaginationIncrementalEndDate"),globalMessage['anvizent.package.message.EnddateshouldbegreaterthanStartdate'],subUrlPaginationIncrementalDateTypeEle.find(".subUrlPaginationIncrementalEndDate"));
									status = false;
								}
								if(subUrlPaginationIncrementalDatePageNumberRequestParamName != '' ){ 
									if(!$.isNumeric(subUrlPaginationIncrementalDatePageNumberRequestParamValue)){
									common.showcustommsg(subUrlPaginationIncrementalDateTypeEle.find(".subUrlPaginationIncrementalDatePageNumberRequestParamValue"), globalMessage['anvizent.package.label.pleaseenternumericvalue'], subUrlPaginationIncrementalDateTypeEle.find(".subUrlPaginationIncrementalDatePageNumberRequestParamValue"));
									status = false;
									}
								}
								if(subUrlPaginationIncrementalDatePageSizeRequestParamName != '' ){ 
									if( subUrlPaginationIncrementalDatePageSizeRequestParamValue == 0 || !$.isNumeric(subUrlPaginationIncrementalDatePageSizeRequestParamValue)){
									common.showcustommsg(subUrlPaginationIncrementalDateTypeEle.find(".subUrlPaginationIncrementalDatePageSizeRequestParamValue"), globalMessage['anvizent.package.label.pleaseenternumericvalueexcept0'], subUrlPaginationIncrementalDateTypeEle.find(".subUrlPaginationIncrementalDatePageSizeRequestParamValue"));
									status = false;
									}
								}
								subUrldetailsObj['subUrlPaginationType']  =  subUrlPaginationType;
								subUrldetailsObj['subUrlPaginationIncrementalDatePageNumberRequestParamName']  =  subUrlPaginationIncrementalDatePageNumberRequestParamName;
								subUrldetailsObj['subUrlPaginationIncrementalDatePageNumberRequestParamValue'] = subUrlPaginationIncrementalDatePageNumberRequestParamValue;
								subUrldetailsObj['subUrlPaginationIncrementalDatePageSizeRequestParamName'] = subUrlPaginationIncrementalDatePageSizeRequestParamName;
								subUrldetailsObj['subUrlPaginationIncrementalDatePageSizeRequestParamValue']  =  subUrlPaginationIncrementalDatePageSizeRequestParamValue;
							}
							else if( subUrlPaginationType == 'conditionaldate' )
							{
								var subUrlPaginationFilterName  = subUrlPaginationConditionalType.find(".subUrlPaginationFilterName").val();
								var subUrlPaginationConditionalFromDateParam  = subUrlPaginationConditionalType.find(".subUrlPaginationConditionalFromDateParam").val();
								var subUrlPaginationConditionalFromDate  = subUrlPaginationConditionalType.find(".subUrlPaginationConditionalFromDate").val();
								var subUrlPaginationConditionalToDateParam  = subUrlPaginationConditionalType.find(".subUrlPaginationConditionalToDateParam").val();
								var subUrlPaginationConditionalToDate  = subUrlPaginationConditionalType.find(".subUrlPaginationConditionalToDate").val();
								var subUrlPaginationConditionalFromDateCondition  = subUrlPaginationConditionalType.find(".subUrlPaginationConditionalFromDateCondition").val();
								var subUrlPaginationConditionalToDateCondition  = subUrlPaginationConditionalType.find(".subUrlPaginationConditionalToDateCondition").val();
								var subUrlPaginationConditionalParam  = subUrlPaginationConditionalType.find(".subUrlPaginationConditionalParam").val();
								var subUrlPaginationConditionalDayRange = subUrlPaginationConditionalType.find(".subUrlPaginationConditionalDayRange").val();
								
								if(subUrlPaginationFilterName == ''){ 
									common.showcustommsg(subUrlPaginationConditionalType.find(".subUrlPaginationFilterName"), globalMessage['anvizent.package.label.pleaseEnterValue'], subUrlPaginationConditionalType.find(".subUrlPaginationFilterName"));
									status = false;
								}
								if(subUrlPaginationConditionalFromDateParam == ''){ 
									common.showcustommsg(subUrlPaginationConditionalType.find(".subUrlPaginationConditionalFromDateParam"), globalMessage['anvizent.package.label.pleaseEnterValue'], subUrlPaginationConditionalType.find(".subUrlPaginationConditionalFromDateParam"));
									status = false;
								}
								if(subUrlPaginationConditionalFromDate == ''){ 
									common.showcustommsg(subUrlPaginationConditionalType.find(".subUrlPaginationConditionalFromDate"), globalMessage['anvizent.package.message.pleasechoosefromdate'], subUrlPaginationConditionalType.find(".subUrlPaginationConditionalFromDate"));
									status = false;
								}
								if(subUrlPaginationConditionalToDateParam == ''){ 
									common.showcustommsg(subUrlPaginationConditionalType.find(".subUrlPaginationConditionalToDateParam"), globalMessage['anvizent.package.label.pleaseEnterValue'], subUrlPaginationConditionalType.find(".subUrlPaginationConditionalToDateParam"));
									status = false;
								}
								/*if(subUrlPaginationConditionalToDate == ''){ 
									common.showcustommsg(subUrlPaginationConditionalType.find(".subUrlPaginationConditionalToDate"), globalMessage['anvizent.package.message.pleasechoosetodate'], subUrlPaginationConditionalType.find(".subUrlPaginationConditionalToDate"));
									status = false;
								}*/
								if(subUrlPaginationConditionalFromDateCondition == ''){ 
									common.showcustommsg(subUrlPaginationConditionalType.find(".subUrlPaginationConditionalFromDateCondition"), globalMessage['anvizent.package.label.pleaseEnterValue'], subUrlPaginationConditionalType.find(".subUrlPaginationConditionalFromDateCondition"));
									status = false;
								}
								if(subUrlPaginationConditionalToDateCondition == ''){ 
									common.showcustommsg(subUrlPaginationConditionalType.find(".subUrlPaginationConditionalToDateCondition"), globalMessage['anvizent.package.label.pleaseEnterValue'], subUrlPaginationConditionalType.find(".subUrlPaginationConditionalToDateCondition"));
									status = false;
								}
								if(subUrlPaginationConditionalParam == ''){ 
									common.showcustommsg(subUrlPaginationConditionalType.find(".subUrlPaginationConditionalParam"), globalMessage['anvizent.package.label.pleaseEnterValue'], subUrlPaginationConditionalType.find(".subUrlPaginationConditionalParam"));
									status = false;
								}
								if(new Date(subUrlPaginationConditionalFromDate) >= new Date(subUrlPaginationConditionalToDate))
								{
									common.showcustommsg(subUrlPaginationConditionalType.find(".subUrlPaginationConditionalToDate"), globalMessage['anvizent.package.message.EnddateshouldbegreaterthanStartdate'], subUrlPaginationConditionalType.find(".subUrlPaginationConditionalToDate"));
									status = false;
								}
								subUrldetailsObj['subUrlPaginationFilterName'] = subUrlPaginationFilterName;
								subUrldetailsObj['subUrlPaginationConditionalFromDateParam'] = subUrlPaginationConditionalFromDateParam;
								subUrldetailsObj['subUrlPaginationConditionalFromDateCondition'] = subUrlPaginationConditionalFromDateCondition;
								subUrldetailsObj['subUrlPaginationConditionalFromDate'] = subUrlPaginationConditionalFromDate;
								subUrldetailsObj['subUrlPaginationConditionalParam'] = subUrlPaginationConditionalParam;
								subUrldetailsObj['subUrlPaginationConditionalToDateParam'] = subUrlPaginationConditionalToDateParam;
								subUrldetailsObj['subUrlPaginationConditionalToDateCondition'] = subUrlPaginationConditionalToDateCondition;
								subUrldetailsObj['subUrlPaginationConditionalToDate'] = subUrlPaginationConditionalToDate;
								subUrldetailsObj['subUrlPaginationConditionalDayRange'] = subUrlPaginationConditionalDayRange;
								subUrldetailsObj['subUrlPaginationType']  =  subUrlPaginationType;
							}else{
								var subUrlPaginationOtherRequestParamkey = subUrlPaginationOtherType.find('.subUrlPaginationOtherRequestParamkey').val();
								var subUrlPaginationOtherRequestLimit = subUrlPaginationOtherType.find('.subUrlPaginationOtherRequestLimit').val();
								
								if(subUrlPaginationOtherRequestParamkey == ''){ 
									common.showcustommsg(subUrlPaginationOtherType.find(".subUrlPaginationOtherRequestParamkey"), globalMessage['anvizent.package.label.pleaseEnterKeyParam'], subUrlPaginationOtherType.find(".subUrlPaginationOtherRequestParamkey"));
									status = false;
								}
								  subUrldetailsObj['subUrlPaginationType']  =  subUrlPaginationType;
								  subUrldetailsObj["paginationHyperLinkPattern"] = subUrlPaginationOtherRequestParamkey;
								  subUrldetailsObj["paginationHypermediaPageLimit"] = subUrlPaginationOtherRequestLimit;
							}
						 }else{
								common.showcustommsg($(pathParamDetailsBlock).find(".subUrlPaginationOffsetDateType") ,globalMessage['anvizent.package.label.pleasechoosesuburlpaginationtype'], $(pathParamDetailsBlock).find(".subUrlPaginationOffsetDateType"));
								status = false;
						 }
						}else{
							subUrldetailsObj["subUrlPaginationRequired"] = subUrlPaginationRequired;
						}
						
						var subUrlIncrementalUpdate = $(pathParamDetailsBlock).find(".subUrlIncrementalUpdate").prop("checked");
						var subUrlIncrementalUpdateDetailsDiv = $(pathParamDetailsBlock).find(".subUrlIncrementalUpdateDetailsDiv");
						subUrldetailsObj['subUrlIncrementalUpdate'] = subUrlIncrementalUpdate;
						if ( subUrlIncrementalUpdate ) {
							var subUrlIncrementalUpdateParamName = subUrlIncrementalUpdateDetailsDiv.find(".subUrlIncrementalUpdateParamName").val();
							var subUrlIncrementalUpdateParamvalue = subUrlIncrementalUpdateDetailsDiv.find(".subUrlIncrementalUpdateParamvalue").val();
							var subUrlIncrementalUpdateParamColumnName = subUrlIncrementalUpdateDetailsDiv.find(".subUrlIncrementalUpdateParamColumnName").val();
							var subUrlIncrementalUpdateParamType = subUrlIncrementalUpdateDetailsDiv.find(".subUrlIncrementalUpdateParamType").val();
							
							if ( subUrlIncrementalUpdateParamName.trim().length == 0 ) {
								common.showcustommsg(subUrlIncrementalUpdateDetailsDiv.find(".subUrlIncrementalUpdateParamName"), globalMessage['anvizent.package.label.pleaseEnterValue'], subUrlIncrementalUpdateDetailsDiv.find(".subUrlIncrementalUpdateParamName") );
								status = false;
							}
							if (  subUrlIncrementalUpdateParamvalue.trim().length == 0 ) {
								common.showcustommsg(subUrlIncrementalUpdateDetailsDiv.find(".subUrlIncrementalUpdateParamvalue"), globalMessage['anvizent.package.label.pleaseEnterValue'], incrementalUpdateDetailsDiv.find(".subUrlIncrementalUpdateParamvalue") );
								status = false;
							}
							subUrldetailsObj['subUrlIncrementalUpdateParamName'] = subUrlIncrementalUpdateParamName;
							subUrldetailsObj['subUrlIncrementalUpdateParamvalue'] = subUrlIncrementalUpdateParamvalue;
							subUrldetailsObj['subUrlIncrementalUpdateParamColumnName'] = subUrlIncrementalUpdateParamName;
							subUrldetailsObj['subUrlIncrementalUpdateParamType'] = subUrlIncrementalUpdateParamType;
							 
						}  
						
						subUrldetailsObj["baseUrlRequired"] = $(pathParamDetailsBlock).find('.baseUrlRequired').is(":checked") ? true : false;

						pathParamsObj["subUrldetails"] = subUrldetailsObj;
					}
				}
				if(!$.isEmptyObject(pathParamsObj))
					pathParamsArray.push(pathParamsObj);
				
			});
			if(pathParamsArray != '')
				$(this).find(".apiPathParams").val(JSON.stringify(pathParamsArray));
			else
				$(this).find(".apiPathParams").val("");
			
			if(arrayApiName != ""){
				for(var i=0; i< arrayApiName.length; i++){
					for(var j=0; j <arrayApiName.length; j++){
						if(i!=j && arrayApiName[i] == arrayApiName[j]){
							common.showcustommsg($(this).find(".apiName"), globalMessage['anvizent.package.label.duplicateName'], $(this).find(".apiName"));
							status = false;
							break;
						}
					}
				}
			}
			
			if(!status){
				validStatus = status;
				collapseDiv.removeClass("collapse").addClass("collapse in").removeAttr("style");
				var glyphicon = collapseDiv.siblings(".accordion-heading").find("a span").attr("class");
				if(glyphicon == "glyphicon glyphicon-plus-sign"){
					collapseDiv.siblings(".accordion-heading").find("a span").removeClass("glyphicon-plus-sign").addClass("glyphicon-minus-sign");
				}
			}
		});
		
		if(!validStatus){
			return false;
		}
		
		$("#wsApiBlock").remove();
		$("#pageMode").val("");
		$("#webServiceILMapping").prop("action",$("#save").val());
		this.form.submit();
		showAjaxLoader(true);
	});
	
	$(document).on("click", ".inactivateWsILMapping", function(){
		$("#deleteWSILMappingAlert #confirmDeletedeleteWSILMapping").val($(this).parents(".wsApiMappingBlock").find(".id").val());
		$("#deleteWSILMappingAlert").modal("show");
	});
	
	$(document).on("click", ".paginationRequired", function(){
		var isPaginationRequired = $(this).val() == 'yes' ? true : false ;
		var paginationTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationType");
		var paginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOffSetType");
		var paginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationPageNumberSizeType");
		var paginationIncrementalDateTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationIncrementalDateType");
		var paginationDateTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationDateType");
		var paginationOtherTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOtherType");
		var paginationOffsetDateType =  $($(this).closest("div.wsApiMappingBlock")).find(".paginationOffsetDateType:checked").val();
		var paginationParamTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationParamTypeDiv");
		var paginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationConditionalType");
		if(isPaginationRequired){
			paginationTypeDiv.show();
			paginationParamTypeDiv.show();
			if(paginationOffsetDateType  == 'offset'){
				paginationOffSetTypeDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".paginationOffset").trigger("click");
				paginationDateTypeDiv.hide();
				paginationOtherTypeDiv.hide();
				paginationIncrementalDateTypeDiv.hide();
				paginationConditionalTypeDiv.hide();
				paginationPageNumberSizeTypeDiv.hide();
			}else if(paginationOffsetDateType  == 'page'){
				paginationPageNumberSizeTypeDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".paginationPageNumberSize").trigger("click");
				paginationOffSetTypeDiv.hide();
				paginationDateTypeDiv.hide();
				paginationOtherTypeDiv.hide();
				paginationIncrementalDateTypeDiv.hide();
				paginationConditionalTypeDiv.hide();
			}else if(paginationOffsetDateType  == 'date'){
				paginationDateTypeDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".paginationDate").trigger("click");
				paginationOffSetTypeDiv.hide();
				paginationOtherTypeDiv.hide();
				paginationIncrementalDateTypeDiv.hide();
				paginationConditionalTypeDiv.hide();
				paginationPageNumberSizeTypeDiv.hide();
			} else if(paginationOffsetDateType  == 'incrementaldate'){
				paginationIncrementalDateTypeDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".paginationIncrementalDate").trigger("click");
				paginationOffSetTypeDiv.hide();
				paginationOtherTypeDiv.hide();
				paginationDateTypeDiv.hide();
				paginationConditionalTypeDiv.hide();
				paginationPageNumberSizeTypeDiv.hide();
			}
			else if(paginationOffsetDateType  == 'hypermedia'){ 
				paginationOtherTypeDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".paginationOther").trigger("click");
				paginationOffSetTypeDiv.hide();
				paginationDateTypeDiv.hide();
				paginationIncrementalDateTypeDiv.hide();
				paginationConditionalTypeDiv.hide();
				paginationPageNumberSizeTypeDiv.hide();
			}else if(paginationOffsetDateType  == 'conditionaldate'){
				paginationOtherTypeDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".paginationConditionalDate").trigger("click");
				paginationOffSetTypeDiv.hide();
				paginationDateTypeDiv.hide();
				paginationIncrementalDateTypeDiv.hide();
				paginationConditionalTypeDiv.show();
				paginationPageNumberSizeTypeDiv.hide();
			}else{
				 $($(this).closest("div.wsApiMappingBlock")).find(".paginationOffset").prop("checked",false);
				 $($(this).closest("div.wsApiMappingBlock")).find(".paginationPageNumberSize").prop("checked",false);
				 $($(this).closest("div.wsApiMappingBlock")).find(".paginationDate").prop("checked",false);
				 $($(this).closest("div.wsApiMappingBlock")).find(".paginationOther").prop("checked",false);
				 $($(this).closest("div.wsApiMappingBlock")).find(".paginationIncrementalDate").prop("checked",false);
				 $($(this).closest("div.wsApiMappingBlock")).find(".paginationConditionalDate").prop("checked",false);
			}
		}else{
			paginationTypeDiv.hide();
			paginationDateTypeDiv.hide();
			paginationOffSetTypeDiv.hide();
			paginationOtherTypeDiv.hide();
			paginationIncrementalDateTypeDiv.hide();
			paginationConditionalTypeDiv.hide();
			paginationPageNumberSizeTypeDiv.hide();
			paginationTypeDiv.find(".paginationOffset").prop("checked",false);
			paginationTypeDiv.find(".paginationPageNumberSize").prop("checked",false);
			paginationTypeDiv.find(".paginationDate").prop("checked",false);
			paginationTypeDiv.find(".paginationOther").prop("checked",false);
			paginationTypeDiv.find(".paginationIncrementalDate").prop("checked",false);
			paginationTypeDiv.find(".paginationConditionalDate").prop("checked",false);
			paginationOffSetTypeDiv.find(".paginationOffSetRequestParamName").val("");
			paginationOffSetTypeDiv.find(".paginationOffSetRequestParamValue").val("");
			paginationOffSetTypeDiv.find(".paginationLimitRequestParamName").val("");
			paginationOffSetTypeDiv.find(".paginationLimitRequestParamValue").val("");
			paginationPageNumberSizeTypeDiv.find(".paginationPageNumberRequestParamName").val("");
			paginationPageNumberSizeTypeDiv.find(".paginationPageNumberRequestParamValue").val("");
			paginationPageNumberSizeTypeDiv.find(".paginationPageSizeRequestParamName").val("");
			paginationPageNumberSizeTypeDiv.find(".paginationPageSizeRequestParamValue").val("");
			
			paginationPageNumberSizeTypeDiv.find(".paginationDatePageNumberRequestParamName").val("");
			paginationPageNumberSizeTypeDiv.find(".paginationDatePageNumberRequestParamValue").val("");
			paginationPageNumberSizeTypeDiv.find(".paginationDatePageSizeRequestParamName").val("");
			paginationPageNumberSizeTypeDiv.find(".paginationDatePageSizeRequestParamValue").val("");
			
			paginationIncrementalDateTypeDiv.find(".paginationIncrementalDatePageNumberRequestParamName").val("");
			paginationIncrementalDateTypeDiv.find(".paginationIncrementalDatePageNumberRequestParamValue").val("");
			paginationIncrementalDateTypeDiv.find(".paginationIncrementalDatePageSizeRequestParamName").val("");
			paginationIncrementalDateTypeDiv.find(".paginationIncrementalDatePageSizeRequestParamValue").val("");
			
			paginationDateTypeDiv.find(".paginationStartDateParam").val("");
			paginationDateTypeDiv.find(".paginationEndDateParam").val("");
			paginationDateTypeDiv.find(".paginationStartDate").val("");
			paginationIncrementalDateTypeDiv.find(".paginationIncrementalStartDateParam").val("");
			paginationIncrementalDateTypeDiv.find(".paginationIncrementalStartDate").val("");
			paginationIncrementalDateTypeDiv.find(".paginationIncrementalEndDateParam").val("");
			paginationIncrementalDateTypeDiv.find(".paginationIncrementalEndDate").val("");
			paginationOtherTypeDiv.find(".paginationOtherRequestKeyParam").val("");
			paginationOtherTypeDiv.find(".paginationOtherRequestLimit").val("");
			paginationOtherTypeDiv.find(".paginationObjectName").val("");
			paginationOtherTypeDiv.find(".paginationSearchId").val("");
			paginationOtherTypeDiv.find(".PaginationSoapBody").val("");
			paginationConditionalTypeDiv.find(".paginationFilterName").val("");
			paginationConditionalTypeDiv.find(".paginationConditionalFromDateParam").val("");
			paginationConditionalTypeDiv.find(".paginationConditionalFromDate").val("");
			paginationConditionalTypeDiv.find(".paginationConditionalToDateParam").val("");
			paginationConditionalTypeDiv.find(".paginationConditionalToDate").val("");
			paginationConditionalTypeDiv.find(".paginationConditionalFromDateCondition").val("");
			paginationConditionalTypeDiv.find(".paginationConditionalToDateCondition").val("");
			paginationConditionalTypeDiv.find(".paginationConditionalParam").val("");
			paginationConditionalTypeDiv.find(".paginationConditionalDayRange").val("");
		}
	});
	$(document).on("click", ".paginationOffset", function(){
		var paginationType = $(this).val();
		var paginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOffSetType");
		var paginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationPageNumberSizeType");
		var paginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationDateType");
		var paginationIncrementalDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationIncrementalDateType");
		var paginationOtherType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOtherType");
		var paginationParamTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationParamTypeDiv");
		var paginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationConditionalType");
		if(paginationType == 'offset') 
		paginationOffSetTypeDiv.show();
		paginationParamTypeDiv.show();
		paginationDateType.hide();
		paginationOtherType.hide();
		paginationIncrementalDateType.hide();
		paginationConditionalTypeDiv.hide();
		paginationPageNumberSizeTypeDiv.hide();
	});
	$(document).on("click", ".paginationPageNumberSize", function(){
		var paginationType = $(this).val();
		var paginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOffSetType");
		var paginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationPageNumberSizeType");
		var paginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationDateType");
		var paginationIncrementalDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationIncrementalDateType");
		var paginationOtherType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOtherType");
		var paginationParamTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationParamTypeDiv");
		var paginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationConditionalType");
		if(paginationType == 'page') 
			paginationPageNumberSizeTypeDiv.show();
		paginationOffSetTypeDiv.hide();
		paginationParamTypeDiv.show();
		paginationDateType.hide();
		paginationOtherType.hide();
		paginationIncrementalDateType.hide();
		paginationConditionalTypeDiv.hide();
		
	});
	$(document).on("click", ".paginationDate", function(){
		var paginationType = $(this).val();
		var paginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOffSetType");
		var paginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationPageNumberSizeType");
		var paginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationDateType");
		var paginationIncrementalDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationIncrementalDateType");
		var paginationOtherType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOtherType");
		var paginationParamTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationParamTypeDiv");
		var paginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationConditionalType");
		if(paginationType == 'date') {
			paginationDateType.find('.paginationStartDate').datepicker({
					dateFormat : 'yy-mm-dd',
					defaultDate : new Date(),
					changeMonth : true,
					changeYear : true, 
					yearRange : "0:+20",
					numberOfMonths : 1
				});
			 paginationDateType.show();
		}
		paginationParamTypeDiv.show();
		paginationOtherType.hide();
		paginationOffSetTypeDiv.hide();
		paginationIncrementalDateType.hide();
		paginationConditionalTypeDiv.hide();
		paginationPageNumberSizeTypeDiv.hide();
	});
	
	$(document).on("click", ".paginationIncrementalDate", function(){
		var paginationType = $(this).val();
		var paginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOffSetType");
		var paginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationPageNumberSizeType");
		var paginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationDateType");
		var paginationIncrementalDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationIncrementalDateType");
		var paginationOtherType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOtherType");
		var paginationParamTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationParamTypeDiv");
		var paginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationConditionalType");
		if(paginationType == 'incrementaldate') {
			paginationIncrementalDateType.find('.paginationIncrementalStartDate').datepicker({
					dateFormat : 'yy-mm-dd',
					defaultDate : new Date(),
					changeMonth : true,
					changeYear : true, 
					yearRange : "0:+20",
					numberOfMonths : 1
				});
			paginationIncrementalDateType.find('.paginationIncrementalEndDate').datepicker({
				dateFormat : 'yy-mm-dd',
				defaultDate : new Date(),
				changeMonth : true,
				changeYear : true, 
				yearRange : "0:+20",
				numberOfMonths : 1
			}); 
			
			paginationIncrementalDateType.show();
		}
		paginationParamTypeDiv.show();
		paginationOtherType.hide();
		paginationOffSetTypeDiv.hide();
		paginationDateType.hide();
		paginationConditionalTypeDiv.hide();
		paginationPageNumberSizeTypeDiv.hide();
	});
	
	$(document).on("click", ".paginationOther", function(){
		var paginationType = $(this).val();
		var paginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOffSetType");
		var paginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationPageNumberSizeType");
		var paginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationDateType");
		var paginationIncrementalDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationIncrementalDateType");
		var paginationOtherType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOtherType");
		var paginationParamTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationParamTypeDiv");
		var paginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationConditionalType");
        var subUrlPaginationParamTypeDiv = $($(this).closest("")) 
		if(paginationType == 'hypermedia') 
			paginationOtherType.show();
        paginationParamTypeDiv.hide();
		paginationDateType.hide();
		paginationOffSetTypeDiv.hide();
		paginationIncrementalDateType.hide();
		paginationConditionalTypeDiv.hide();
		paginationPageNumberSizeTypeDiv.hide();
	});
	
	$(document).on("click", ".paginationConditionalDate", function(){
		var paginationType = $(this).val();
		var paginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOffSetType");
		var paginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationPageNumberSizeType");
		var paginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationDateType");
		var paginationIncrementalDateType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationIncrementalDateType");
		var paginationOtherType = $($(this).closest("div.wsApiMappingBlock")).find(".paginationOtherType");
		var paginationParamTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationParamTypeDiv");
		var paginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".paginationConditionalType");
        var subUrlPaginationParamTypeDiv = $($(this).closest("")) 
		if(paginationType == 'conditionaldate'){
			paginationConditionalTypeDiv.find(".paginationConditionalFromDate").datepicker({
				dateFormat : 'yy-mm-dd',
				defaultDate : new Date(),
				changeMonth : true,
				changeYear : true, 
				yearRange : "0:+20",
				numberOfMonths : 1
			});
			paginationConditionalTypeDiv.find(".paginationConditionalToDate").datepicker({
				dateFormat : 'yy-mm-dd',
				defaultDate : new Date(),
				changeMonth : true,
				changeYear : true, 
				yearRange : "0:+20",
				numberOfMonths : 1
			});
			paginationConditionalTypeDiv.show();
		}
		paginationOtherType.hide();
        paginationParamTypeDiv.hide();
		paginationDateType.hide();
		paginationOffSetTypeDiv.hide();
		paginationIncrementalDateType.hide();
		paginationPageNumberSizeTypeDiv.hide();
	});
	
	$(document).on("click", ".subUrlPaginationRequired", function(){
		var subUrlPaginationRequired = $(this).val() == 'yes' ? true : false ;
		var subUrlPaginationTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationType");
		var subUrlPaginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOffSetType");
		var subUrlPaginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationPageNumberSizeType");
		var subUrlPaginationDateTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationDateType");
		var subUrlPaginationIncrementalDateTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationIncrementalDateType");
		var subUrlPaginationOtherDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOtherType");
		var subUrlPaginationParamTypeDiv= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationParamTypeDiv");
		var subUrlPaginationOffsetDateType =  $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOffsetDateType:checked").val();
		var subUrlPaginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationConditionalType");
		if(subUrlPaginationRequired){
			subUrlPaginationTypeDiv.show();
			subUrlPaginationParamTypeDiv.show();
			if(subUrlPaginationOffsetDateType  == 'offset'){
				subUrlPaginationOffSetTypeDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOffset").trigger("click");
				subUrlPaginationDateTypeDiv.hide();
				subUrlPaginationOtherDiv.hide();
				subUrlPaginationConditionalTypeDiv.hide();
				subUrlPaginationIncrementalDateTypeDiv.hide();
				subUrlPaginationPageNumberSizeTypeDiv.hide();
			}else if(subUrlPaginationOffsetDateType  == 'page'){
				subUrlPaginationPageNumberSizeTypeDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationPageNumberSize").trigger("click");
				subUrlPaginationOffSetTypeDiv.hide();
				subUrlPaginationDateTypeDiv.hide();
				subUrlPaginationOtherDiv.hide();
				subUrlPaginationConditionalTypeDiv.hide();
				subUrlPaginationIncrementalDateTypeDiv.hide();
			}else if(subUrlPaginationOffsetDateType  == 'date'){
				subUrlPaginationDateTypeDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationDate").trigger("click");
				subUrlPaginationOffSetTypeDiv.hide();
				subUrlPaginationOtherDiv.hide();
				subUrlPaginationConditionalTypeDiv.hide();
				subUrlPaginationIncrementalDateTypeDiv.hide();
				subUrlPaginationPageNumberSizeTypeDiv.hide();
			}else if(subUrlPaginationOffsetDateType  == 'incrementaldate'){
				subUrlPaginationIncrementalDateTypeDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationIncrementalDate").trigger("click");
				subUrlPaginationOffSetTypeDiv.hide();
				subUrlPaginationOtherDiv.hide();
				subUrlPaginationDateTypeDiv.hide();
				subUrlPaginationConditionalTypeDiv.hide();
				subUrlPaginationPageNumberSizeTypeDiv.hide();
			}else if(subUrlPaginationOffsetDateType  == 'hypermedia'){
				subUrlPaginationOtherDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOther").trigger("click");
				subUrlPaginationDateTypeDiv.hide();
				subUrlPaginationOffSetTypeDiv.hide();
				subUrlPaginationIncrementalDateTypeDiv.hide();
				subUrlPaginationConditionalTypeDiv.hide();
				subUrlPaginationPageNumberSizeTypeDiv.hide();
			}else if(subUrlPaginationOffsetDateType  == 'conditionaldate'){
				subUrlPaginationConditionalTypeDiv.show();
				$($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationConditionalDate").trigger("click");
				subUrlPaginationDateTypeDiv.hide();
				subUrlPaginationOffSetTypeDiv.hide();
				subUrlPaginationOtherDiv.hide();
				subUrlPaginationIncrementalDateTypeDiv.hide();
				subUrlPaginationPageNumberSizeTypeDiv.hide();
			}else{
				 $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOffSetType").prop("checked",false);
				 $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationPageNumberSizeType").prop("checked",false);
				 $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationDateType").prop("checked",false);
				 $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationIncrementalDateType").prop("checked",false);
				 $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOther").prop("checked",false);
				 $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationConditionalDate").prop("checked",false);
			}
		}else{
			subUrlPaginationTypeDiv.hide();
			subUrlPaginationDateTypeDiv.hide();
			subUrlPaginationOffSetTypeDiv.hide();
			subUrlPaginationOtherDiv.hide();
			subUrlPaginationConditionalTypeDiv.hide();
			subUrlPaginationIncrementalDateTypeDiv.hide();
			subUrlPaginationPageNumberSizeTypeDiv.hide();
			subUrlPaginationTypeDiv.find(".subUrlPaginationOffset").prop("checked",false);
			subUrlPaginationTypeDiv.find(".subUrlPaginationPageNumberSize").prop("checked",false);
			subUrlPaginationTypeDiv.find(".subUrlPaginationDate").prop("checked",false);
			subUrlPaginationTypeDiv.find(".subUrlPaginationOther").prop("checked",false);
			subUrlPaginationTypeDiv.find(".subUrlPaginationConditionalDate").prop("checked",false);
			subUrlPaginationOffSetTypeDiv.find(".subUrlPaginationOffSetRequestParamName").val("");
			subUrlPaginationOffSetTypeDiv.find(".subUrlPaginationOffSetRequestParamValue").val("");
			subUrlPaginationOffSetTypeDiv.find(".subUrlPaginationLimitRequestParamName").val("");
			subUrlPaginationOffSetTypeDiv.find(".subUrlPaginationLimitRequestParamValue").val("");
			subUrlPaginationPageNumberSizeTypeDiv.find(".subUrlPaginationPageNumberRequestParamName").val("");
			subUrlPaginationPageNumberSizeTypeDiv.find(".subUrlPaginationPageNumberRequestParamValue").val("");
			subUrlPaginationPageNumberSizeTypeDiv.find(".subUrlPaginationPageSizeRequestParamName").val("");
			subUrlPaginationPageNumberSizeTypeDiv.find(".subUrlPaginationPageSizeRequestParamValue").val("");
			
			subUrlPaginationDateTypeDiv.find(".subUrlPaginationDatePageNumberRequestParamName").val("");
			subUrlPaginationDateTypeDiv.find(".subUrlPaginationDatePageNumberRequestParamValue").val("");
			subUrlPaginationDateTypeDiv.find(".subUrlPaginationDatePageSizeRequestParamName").val("");
			subUrlPaginationDateTypeDiv.find(".subUrlPaginationDatePageSizeRequestParamValue").val("");
			
			subUrlPaginationIncrementalDateTypeDiv.find(".subUrlPaginationIncrementalDatePageNumberRequestParamName").val("");
			subUrlPaginationIncrementalDateTypeDiv.find(".subUrlPaginationIncrementalDatePageNumberRequestParamValue").val("");
			subUrlPaginationIncrementalDateTypeDiv.find(".subUrlPaginationIncrementalDatePageSizeRequestParamName").val("");
			subUrlPaginationIncrementalDateTypeDiv.find(".subUrlPaginationIncrementalDatePageSizeRequestParamValue").val("");
			
			subUrlPaginationOtherDiv.find(".subUrlPaginationOtherRequestParamkey").val("");
			subUrlPaginationOtherDiv.find(".subUrlPaginationOtherRequestLimit").val("");
			subUrlPaginationDateTypeDiv.find(".subUrlPaginationStartDateParam").val("");
			subUrlPaginationDateTypeDiv.find(".subUrlPaginationEndDateParam").val("");
			subUrlPaginationDateTypeDiv.find(".subUrlPaginationStartDate").val("");
			subUrlPaginationIncrementalDateTypeDiv.find(".subUrlPaginationIncrementalStartDateParam").val("");
			subUrlPaginationIncrementalDateTypeDiv.find(".subUrlPaginationIncrementalStartDate").val("");
			subUrlPaginationIncrementalDateTypeDiv.find(".subUrlPaginationIncrementalEndDateParam").val("");
			subUrlPaginationIncrementalDateTypeDiv.find(".subUrlPaginationIncrementalEndDate").val("");
			subUrlPaginationConditionalTypeDiv.find(".subUrlPaginationFilterName").val("");
			subUrlPaginationConditionalTypeDiv.find(".subUrlPaginationConditionalFromDateParam").val("");
			subUrlPaginationConditionalTypeDiv.find(".subUrlPaginationConditionalFromDate").val("");
			subUrlPaginationConditionalTypeDiv.find(".subUrlPaginationConditionalToDateParam").val("");
			subUrlPaginationConditionalTypeDiv.find(".subUrlPaginationConditionalToDate").val("");
			subUrlPaginationConditionalTypeDiv.find(".subUrlPaginationConditionalFromDateCondition").val("");
			subUrlPaginationConditionalTypeDiv.find(".subUrlPaginationConditionalToDateCondition").val("");
			subUrlPaginationConditionalTypeDiv.find(".subUrlPaginationConditionalParam").val("");
			subUrlPaginationConditionalTypeDiv.find(".subUrlPaginationConditionalDayRange").val("");
		}
	});
	$(document).on("click", ".subUrlPaginationOffset", function(){
		var subUrlPaginationType = $(this).val();
		var subUrlPaginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOffSetType");
		var subUrlPaginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationPageNumberSizeType");
		var subUrlPaginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationDateType");
		var subUrlPaginationIncrementalDateTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationIncrementalDateType");
		var subUrlPaginationOtherType= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOtherType");
		var subUrlPaginationParamTypeDiv= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationParamTypeDiv");
		var subUrlPaginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationConditionalType");
		if(subUrlPaginationType == 'offset') 
			subUrlPaginationOffSetTypeDiv.show();
		
		subUrlPaginationParamTypeDiv.show();
		subUrlPaginationDateType.hide();
		subUrlPaginationOtherType.hide();
		subUrlPaginationConditionalTypeDiv.hide();
		subUrlPaginationIncrementalDateTypeDiv.hide();
		subUrlPaginationPageNumberSizeTypeDiv.hide();
	});
	$(document).on("click", ".subUrlPaginationPageNumberSize", function(){
		var subUrlPaginationType = $(this).val();
		var subUrlPaginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOffSetType");
		var subUrlPaginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationPageNumberSizeType");
		var subUrlPaginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationDateType");
		var subUrlPaginationIncrementalDateTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationIncrementalDateType");
		var subUrlPaginationOtherType= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOtherType");
		var subUrlPaginationParamTypeDiv= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationParamTypeDiv");
		var subUrlPaginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationConditionalType");
		if(subUrlPaginationType == 'page') 
			subUrlPaginationPageNumberSizeTypeDiv.show();
		
		subUrlPaginationParamTypeDiv.show();
		subUrlPaginationDateType.hide();
		subUrlPaginationOtherType.hide();
		subUrlPaginationConditionalTypeDiv.hide();
		subUrlPaginationIncrementalDateTypeDiv.hide();
		subUrlPaginationOffSetTypeDiv.hide();
	});
	$(document).on("click", ".subUrlPaginationDate", function(){
		var subUrlPaginationType = $(this).val();
		var subUrlPaginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOffSetType");
		var subUrlPaginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationPageNumberSizeType");
		var subUrlPaginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationDateType");
		var subUrlPaginationIncrementalDateTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationIncrementalDateType");
		var subUrlPaginationOtherType= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOtherType");
		var subUrlPaginationParamTypeDiv= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationParamTypeDiv");
		var subUrlPaginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationConditionalType");
		if(subUrlPaginationType == 'date') {
			subUrlPaginationDateType.find('.subUrlPaginationStartDate').datepicker({
					dateFormat : 'yy-mm-dd',
					defaultDate : new Date(),
					changeMonth : true,
					changeYear : true, 
					yearRange : "0:+20",
					numberOfMonths : 1
				});
			subUrlPaginationDateType.show();
	   }
		subUrlPaginationParamTypeDiv.show();
		subUrlPaginationOffSetTypeDiv.hide();
		subUrlPaginationOtherType.hide();
		subUrlPaginationConditionalTypeDiv.hide();
		subUrlPaginationIncrementalDateTypeDiv.hide();
		subUrlPaginationPageNumberSizeTypeDiv.hide();
	});
	$(document).on("click", ".subUrlPaginationIncrementalDate", function(){
		var subUrlPaginationType = $(this).val();
		var subUrlPaginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOffSetType");
		var subUrlPaginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationPageNumberSizeType");
		var subUrlPaginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationDateType");
		var subUrlPaginationIncrementalDateTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationIncrementalDateType");
		var subUrlPaginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationConditionalType");
		var subUrlPaginationOtherType= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOtherType");
		var subUrlPaginationParamTypeDiv= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationParamTypeDiv");
		if(subUrlPaginationType == 'incrementaldate') {
			subUrlPaginationIncrementalDateTypeDiv.find('.subUrlPaginationIncrementalStartDate').datepicker({
					dateFormat : 'yy-mm-dd',
					defaultDate : new Date(),
					changeMonth : true,
					changeYear : true, 
					yearRange : "0:+20",
					numberOfMonths : 1
				});
			subUrlPaginationIncrementalDateTypeDiv.find('.subUrlPaginationIncrementalEndDate').datepicker({
				dateFormat : 'yy-mm-dd',
				defaultDate : new Date(),
				changeMonth : true,
				changeYear : true, 
				yearRange : "0:+20",
				numberOfMonths : 1
			});//.datepicker("setDate", "0");
			subUrlPaginationIncrementalDateTypeDiv.show();
	   }
		subUrlPaginationParamTypeDiv.show();
		subUrlPaginationOffSetTypeDiv.hide();
		subUrlPaginationOtherType.hide();
		subUrlPaginationDateType.hide();
		subUrlPaginationConditionalTypeDiv.hide();
		subUrlPaginationPageNumberSizeTypeDiv.hide();
	});
	$(document).on("click", ".subUrlPaginationOther", function(){
		var subUrlPaginationType = $(this).val();
		var subUrlPaginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOffSetType");
		var subUrlPaginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationPageNumberSizeType");
		var subUrlPaginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationDateType");
		var subUrlPaginationIncrementalDateTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationIncrementalDateType");
		var subUrlPaginationOtherType= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOtherType");
		var subUrlPaginationParamTypeDiv= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationParamTypeDiv");
		var subUrlPaginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationConditionalType");
		if(subUrlPaginationType == 'hypermedia')
			subUrlPaginationOtherType.show();
		
		subUrlPaginationParamTypeDiv.hide();
		subUrlPaginationDateType.hide();
		subUrlPaginationOffSetTypeDiv.hide();
		subUrlPaginationConditionalTypeDiv.hide();
		subUrlPaginationIncrementalDateTypeDiv.hide();
		subUrlPaginationPageNumberSizeTypeDiv.hide();
	});
	
	$(document).on("click", ".subUrlPaginationConditionalDate", function(){
		var subUrlPaginationType = $(this).val();
		var subUrlPaginationOffSetTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOffSetType");
		var subUrlPaginationPageNumberSizeTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationPageNumberSizeType");
		var subUrlPaginationDateType = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationDateType");
		var subUrlPaginationOtherType= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationOtherType");
		var subUrlPaginationParamTypeDiv= $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationParamTypeDiv");
		var subUrlPaginationConditionalTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationConditionalType");
		var subUrlPaginationIncrementalDateTypeDiv = $($(this).closest("div.wsApiMappingBlock")).find(".subUrlPaginationIncrementalDateType");
		if(subUrlPaginationType == 'conditionaldate'){
			subUrlPaginationConditionalTypeDiv.find(".subUrlPaginationConditionalFromDate").datepicker({
				dateFormat : 'yy-mm-dd',
				defaultDate : new Date(),
				changeMonth : true,
				changeYear : true, 
				yearRange : "0:+20",
				numberOfMonths : 1
			});
			subUrlPaginationConditionalTypeDiv.find(".subUrlPaginationConditionalToDate").datepicker({
				dateFormat : 'yy-mm-dd',
				defaultDate : new Date(),
				changeMonth : true,
				changeYear : true, 
				yearRange : "0:+20",
				numberOfMonths : 1
			});
			subUrlPaginationConditionalTypeDiv.show();
		}
		subUrlPaginationParamTypeDiv.hide();
		subUrlPaginationDateType.hide();
		subUrlPaginationOffSetTypeDiv.hide();
		subUrlPaginationOtherType.hide();
		subUrlPaginationIncrementalDateTypeDiv.hide();
		subUrlPaginationPageNumberSizeTypeDiv.hide();
	});
	
	$(document).on("click", ".incrementalUpdate", function(){
		var incrementalUpdate = this.checked;
		var incrementalUpdateDetailsDiv = $($(this).closest("div.wsApiMappingBlock")).find(".incrementalUpdateDetailsDiv");
		if ( incrementalUpdate ) {
			incrementalUpdateDetailsDiv.show();
		} else {
			incrementalUpdateDetailsDiv.hide();
			incrementalUpdateDetailsDiv.find(".incrementalUpdateParamName").val("");
			incrementalUpdateDetailsDiv.find(".incrementalUpdateParamvalue").val("");
			incrementalUpdateDetailsDiv.find(".incrementalUpdateParamColumnName").val("");
		}
	});
	
	$(document).on("click", ".incrementalUpdate", function(){
		var incrementalUpdate = this.checked;
		var incrementalUpdateDetailsDiv = $($(this).closest("div.wsApiMappingBlock")).find(".incrementalUpdateDetailsDiv");
		if ( incrementalUpdate ) {
			incrementalUpdateDetailsDiv.show();
		} else {
			incrementalUpdateDetailsDiv.hide();
			incrementalUpdateDetailsDiv.find(".incrementalUpdateParamName").val("");
			incrementalUpdateDetailsDiv.find(".incrementalUpdateParamvalue").val("");
			incrementalUpdateDetailsDiv.find(".incrementalUpdateParamColumnName").val("");
		}
	});
	
	$(document).on("click", ".subUrlIncrementalUpdate", function(){
		var subUrlIncrementalUpdate = this.checked;
		var subUrlIncrementalUpdateDetailsDiv = $($(this).parents(".subUrlBlock")).find(".subUrlIncrementalUpdateDetailsDiv");
		if ( subUrlIncrementalUpdate ) {
			subUrlIncrementalUpdateDetailsDiv.show();
		} else {
			subUrlIncrementalUpdateDetailsDiv.hide();
			subUrlIncrementalUpdateDetailsDiv.find(".subUrlIncrementalUpdateParamName").val("");
			subUrlIncrementalUpdateDetailsDiv.find(".subUrlIncrementalUpdateParamvalue").val("");
			subUrlIncrementalUpdateDetailsDiv.find(".subUrlIncrementalUpdateParamColumnName").val("");
		}
	});
	
	
	$("#confirmDeletedeleteWSILMapping").on("click", function(){
		$("#wsILMappingId").val($(this).val());
		$("#pageMode").val("delete");
		$("#webServiceILMapping").prop("action",$("#edit").val());
		this.form.submit();
		showAjaxLoader(true);
	});
	
	$(document).on("click", ".wsApiMappingBlock .getPathParams", function(){
		var j = $(this).val();
		var ele = $(this).parents(".wsApiMappingBlock").find(".apiUrl");
		common.clearValidations([$(ele)])
		var apiUrl = $(ele).val();
		
		if(apiUrl == ''){
			common.showcustommsg($(ele), globalMessage['anvizent.package.label.pleaseEnterAPIURL'], $(ele));
  	    	$(this).parents(".wsApiMappingBlock").find(".pathParamsDetailsBlocks").empty();
		}
		else{
			var getPathParams = webService.getPathParams(apiUrl,"{#");
			
			if(getPathParams != '' && getPathParams.length > 0){
				var pathParamDetailsBlocks = "";
				
				$.each(getPathParams , function(i, val){
					var pathParamDetailsBlock = $("#pathParamDetailsSampleBlock").clone();
					$(pathParamDetailsBlock).removeAttr("style id");
					$(pathParamDetailsBlock).find("label.pathParamName").text(val);
					$(pathParamDetailsBlock).find("input[name='pathParamValueType']").prop("name","pathParamValueType"+j+i);
					$(pathParamDetailsBlock).find("input[name='subUrlMethodType']").prop("name","subUrlMethodType"+j+i);
					pathParamDetailsBlocks += pathParamDetailsBlock.html(); 
				});
				
				$(ele).parents(".wsApiMappingBlock").find(".pathParamsDetailsBlocks").empty().append(pathParamDetailsBlocks);
			}
			else{
				common.showcustommsg($(ele), "No path params found", $(ele));
	  	    	$(this).parents(".wsApiMappingBlock").find(".pathParamsDetailsBlocks").empty();
			}
		}
	});
	
	$(document).on("click", ".pathParamValueType", function(){
		if($(this).val() == 'M'){
			$(this).parents(".pathParamDetailsBlock").find(".subUrlBlock").hide();
		}
		else{
			$(this).parents(".pathParamDetailsBlock").find(".subUrlBlock").show();
		}
	});

	// Accordion Toggle Items
    var iconOpen = 'glyphicon glyphicon-minus-sign', iconClose = 'glyphicon glyphicon-plus-sign';

	$(document).on('show.bs.collapse hide.bs.collapse', '#accordion' ,'#accordion2Ws', function (e) {
	    var $target = $(e.target);
	    $target.siblings('.accordion-heading').find('a span').toggleClass(iconOpen + ' ' + iconClose);
	    if(e.type == 'show')
	    	$target.prev('.accordion-heading').find('.accordion-toggle').addClass('active');
	    if(e.type == 'hide')
	    	$(this).find('.accordion-toggle').not($target).removeClass('active');
	});
	
	$(document).on("click",".apiMethodType",function(){
		if($(this).is(":checked") && $(this).val() == 'POST'){
			$(this).parents(".wsApiMappingBlock").find("#apiBodyParams").show();
			$(this).parents(".wsApiMappingBlock").find(".incrementalUpdateParamType").val("Body Parameter");
			$(this).parents(".wsApiMappingBlock").find(".paginationParamType").val("Body Parameter");
			$(this).parents(".wsApiMappingBlock").find(".incrementalUpdateParamType .incrementalUpdateBodyParamType").show();
			
		}
		else{
			$(this).parents(".wsApiMappingBlock").find("#apiBodyParams").hide();
			$(this).parents(".wsApiMappingBlock").find(".incrementalUpdateParamType").val("Request Parameter");
			$(this).parents(".wsApiMappingBlock").find(".paginationParamType").val("Request Parameter");
			$(this).parents(".wsApiMappingBlock").find(".incrementalUpdateParamType .incrementalUpdateBodyParamType").hide();
		}
	});
	$(document).on("click",".subUrlMethodType",function(){
		if($(this).is(":checked") && $(this).val() == 'POST'){
			$(this).parents(".subUrlBlock").find(".subUrlPaginationParamType").val("Body Parameter");
			$(this).parents(".subUrlBlock").find(".subUrlIncrementalUpdateParamType .subUrlIncrementalUpdateBodyParamType").show();
		}
		else{
			$(this).parents(".subUrlBlock").find(".subUrlPaginationParamType").val("Request Parameter");
			$(this).parents(".subUrlBlock").find(".subUrlIncrementalUpdateParamType .subUrlIncrementalUpdateBodyParamType").hide();
		}
	});
	 
}
