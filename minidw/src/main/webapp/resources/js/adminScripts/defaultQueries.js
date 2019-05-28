var headers = {};
var defaultQueries = {
		initialPage : function() {
			$("#defaultQueryTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});	
			$(".selectConnector, .selectIlname").select2({               
                allowClear: true,
                theme: "classic"
			}); 
			
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
		},
		selectIlQuery : function(result){
			var options = "<option value='0' selected>Select IL</option>";
  		    $.each(result, function(key,value){
  			   options+= "<option value ='"+key+"'>"+value+"</option>";
  		    })
  		 $(".selectIlname").empty().append(options);
		},		
		getdefaultQueriesById : function(result){
			 var table =$("#defaultQueryTable").DataTable();
			 table.clear();
			 if(result != null){				
				 for (var i = 0; i < result.object.length; i++) {
					 var object = result.object[i];
					 var mappingid = object.connectionMappingId;
					 var ilId = result.object[i].iLId;
					 var databaseTypeId = result.object[i].iLConnection.database.id;
					 var ilName = result.object[i].ilSourceName;
					 var iLQuery = result.object[i].iLquery;
					 var iLIncrLoadQuery = result.object[i].iLIncrementalUpdateQuery;
					 var historicalLoad = result.object[i].historicalLoad;
					 var maxDateQuery = result.object[i].maxDateQuery;
					 
					 var isactive = result.object[i].iLConnection.database.isActive ? "Yes":"No";
					 var edit = "<button class='btn btn-primary btn-sm tablebuttons editdefaultQuery' data-ilId='"+ilId+"' data-databaseTypeId='"+databaseTypeId+"' title='"+globalMessage['anvizent.package.label.edit']+"'>"+
								"<i class='fa fa-pencil' aria-hidden='true'></i></button>";
					 
					 var viewIlQuery = '';
					 if(!iLQuery)
						 viewIlQuery+="-";
					 else
						 viewIlQuery+="<button class='btn btn-primary btn-sm tablebuttons viewIlQuery text-underline' data-ilId='"+ilId+"' data-databaseTypeId='"+databaseTypeId+"' >View IL Query</button>";
					 
					 var viewIncrQuery = '';
					 if(!iLIncrLoadQuery)
						 viewIncrQuery+="-";
					 else
						 viewIncrQuery+="<button class='btn btn-primary btn-sm tablebuttons viewIncrQuery text-underline' data-ilId='"+ilId+"' data-databaseTypeId='"+databaseTypeId+"'>View Incremental IL Query</button>";
					 
					 var viewhistoricalLoad = '';
					 if(!historicalLoad)
						 viewhistoricalLoad+="-"
					 else
						 viewhistoricalLoad+="<button class='btn btn-primary btn-sm tablebuttons viewhistoricalLoad text-underline' data-ilId='"+ilId+"' data-databaseTypeId='"+databaseTypeId+"'>View History Load</button>";
					 var viewMaxDateQuery = '';
					 if(!maxDateQuery)
						 viewMaxDateQuery+="-"
					 else
						 viewMaxDateQuery+="<button class='btn btn-primary btn-sm tablebuttons viewMaxDateQuery text-underline' data-ilId='"+ilId+"' data-databaseTypeId='"+databaseTypeId+"'>View Max date Query</button>";
					 
					 var row = [];
					 row.push(i+1);
					 row.push(ilName);
					 row.push(viewIlQuery);
					 row.push(viewIncrQuery);
					 row.push(viewMaxDateQuery);
					 row.push(viewhistoricalLoad);
					 row.push(isactive);
					 row.push(edit);
					 table.row.add(row);
				}
				table.draw(true);
			}
		},		
		showIlQuery : function(result){	
			$(".heading").text("IL Query");
			var ilQuery = result.object;			
			$("#viewQueryPopUp").find(".view-Query").text(ilQuery);
			$("#viewQueryPopUp").modal('show');
		},
		showILincrQuery : function(result){		
			$(".heading").text("IL Incremental Update Query");
			var ilIncrQuery = result.object;			
			$("#viewQueryPopUp").find(".view-Query").text(ilIncrQuery);
			$("#viewQueryPopUp").modal('show');
		},
		showHistoricalLoad : function(result){
			$(".heading").text("Historical Load");
			var historyLoad = result.object;			
			$("#viewQueryPopUp").find(".view-Query").text(historyLoad);
			$("#viewQueryPopUp").modal('show');
		},
		showMaxDateQuery : function(result){
			$(".heading").text("Max Date Query");
			var maxDate = result.object;			
			$("#viewQueryPopUp").find(".view-Query").text(maxDate);
			$("#viewQueryPopUp").modal('show');
		},
		
		updateDefaultQueryAuthPan:function(result){
			var ilname = result["ilSourceName"];
			var ilQuery = result["iLquery"];
			var ilIncrQuery = result["iLIncrementalUpdateQuery"];
			var historyLoad = result["historicalLoad"];
			var maxDateQuery = result["maxDateQuery"];
			$("#ilQueryName").val(ilname);
			$("#queryScript").val(ilQuery);
			$("#incrementalQuery").val(ilIncrQuery);
			$("#historyLoad").val(historyLoad);
			$("#maxDate").val(maxDateQuery);
		}
		
}
if($('.defaultQueries-page').length){
	defaultQueries.initialPage();
	//events
	$(document).on("change",".selectConnector",function(){
		common.clearValidations([".selectConnector"])
		var userID = $("#userID").val();
		var databaseTypeId = $(this).val();
		
        if(databaseTypeId != 0){
	 		
	 		var selectData = {
	 				databaseTypeId : databaseTypeId,
			}
	 		var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
	 		showAjaxLoader(true);
			var url_getDefaultQuery = "/app_Admin/user/"+userID+"/etlAdmin/getDefaultQuery";
			   var myAjax = common.postAjaxCallObject(url_getDefaultQuery,'POST',selectData,headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;		    					  
			    			  }
		    				  else{			    				
			    				  defaultQueries.getdefaultQueriesById(result);
			    				  $(".defaultQueriesDiv").show();
			    			  }
			    		  }
					}
			});
	 	}
	 	else{
	 		 $(".defaultQueriesDiv").hide();	
	 	}
		
	});
	
	$(document).on("click","#add",function(){
		common.clearValidations([".selectConnector","#queryScript","#incrementalQuery","#ilQueryName","#historyLoad","#maxDate"]);		
		$("#queryScript, #incrementalQuery,#historyLoad").text("").val("");
		
		var userID = $("#userID").val();		
		var databaseTypeId = $(".selectConnector option:selected").val();
		var connectorName= $(".selectConnector option:selected").text().trim();
        if(databaseTypeId != 0){	 		
	 		var selectData = {
	 				databaseTypeId : databaseTypeId,
			}
	 		var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
	 		showAjaxLoader(true);
			var url_getNotMappedILs = "/app_Admin/user/"+userID+"/etlAdmin/getNotMappedILsByDBTypeId";
			   var myAjax = common.postAjaxCallObject(url_getNotMappedILs,'POST',selectData,headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){						
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;		    					  
			    			  }
			    			  if(result.messages[0].code=="SUCCESS"){	
			    				  defaultQueries.selectIlQuery(result.object);
					    		 
					    		  $("#connectorName").val(connectorName);
					    		  $("#databaseTypeId").val(databaseTypeId);
					    		  $("#ilQuerySelect").show();
                                  $("#ilQueryText").hide();
					    		  $("#save").show();
					    		  $("#update").hide();
					    		  $("#addDefaultQueryPopUp").modal("show");
					    		  
			    			  }
			    		  }
					}
				});
		}else{
			common.showcustommsg(".selectConnector", globalMessage['anvizent.package.label.pleaseSelectConnectorType'], ".selectConnector");
		}
	});
	
	$('#addDefaultQueryPopUp').on('shown.bs.modal', function () {
		$(".selectIlname").select2({               
            allowClear: true,
            theme: "classic"
		 });
    })
	
	$(document).on("click",".viewIlQuery",function(){
		var userID = $("#userID").val();
		var ilid = $(this).attr("data-ilId");
		var databaseTypeId =  $(this).attr("data-databaseTypeId");
		showAjaxLoader(true);
		var url_getIlQueryById = "/app_Admin/user/"+userID+"/etlAdmin/getIlQueryById/"+ilid+"/"+databaseTypeId;
		   var myAjax = common.postAjaxCallObject(url_getIlQueryById,'GET','',headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){		    				
		    				  defaultQueries.showIlQuery(result);
		    				  $(".defaultQueriesDiv").show();
		    			  }
		    		  }
				}
		});
	});
	$(document).on("click",".viewIncrQuery",function(){
		var userID = $("#userID").val();
		var ilid = $(this).attr("data-ilId");
		var databaseTypeId =  $(this).attr("data-databaseTypeId");
		showAjaxLoader(true);
		var url_getIlincrementalQueryById = "/app_Admin/user/"+userID+"/etlAdmin/getIlincrementalQueryById/"+ilid+"/"+databaseTypeId;
		   var myAjax = common.postAjaxCallObject(url_getIlincrementalQueryById,'GET','',headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				  
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){		    				
		    				  defaultQueries.showILincrQuery(result);
		    				  $(".defaultQueriesDiv").show();
		    			  }
		    		  }
				}
		});
	});
	
	$(document).on("click",".viewhistoricalLoad",function(){
		var userID = $("#userID").val();
		var ilid = $(this).attr("data-ilId");
		var databaseTypeId =  $(this).attr("data-databaseTypeId");
		showAjaxLoader(true);
		var url_getIlincrementalQueryById = "/app_Admin/user/"+userID+"/etlAdmin/gethistoryLoadById/"+ilid+"/"+databaseTypeId;
		   var myAjax = common.postAjaxCallObject(url_getIlincrementalQueryById,'GET','',headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				  
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){		    				
		    				  defaultQueries.showHistoricalLoad(result);
		    				  $(".defaultQueriesDiv").show();
		    			  }
		    		  }
				}
		});
	});
	
	$(document).on("click",".viewMaxDateQuery",function(){
		var userID = $("#userID").val();
		var ilid = $(this).attr("data-ilId");
		var databaseTypeId =  $(this).attr("data-databaseTypeId");
		showAjaxLoader(true);
		var url_geMaxDateQueryById = "/app_Admin/user/"+userID+"/etlAdmin/geMaxDateQueryById/"+ilid+"/"+databaseTypeId;
		   var myAjax = common.postAjaxCallObject(url_geMaxDateQueryById,'GET');
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;		    				  
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){		    				
		    				  defaultQueries.showMaxDateQuery(result);
		    				  $(".defaultQueriesDiv").show();
		    			  }
		    		  }
				}
		});
	});
		
	$(document).on("click","#save",function(){
		common.clearValidations([".selectConnector,#queryScript,#incrementalQuery,#ilQueryName,#historyLoad"]);
		var databaseTypeId = $(".selectConnector option:selected").val();
		var iLId = $(".selectIlname option:selected").val();
		$("#iLId").val(iLId);
		var iLQuery = $("#queryScript").val();
		var iLIncrUpdateQuery = $("#incrementalQuery").val();
		var historicalLoad = $("#historyLoad").val();
		var maxDateQuery = $("#maxDate").val();
		var status = true;		
		if(iLId == 0){
			common.showcustommsg(".selectIlname", globalMessage['anvizent.package.label.pleaseSelectIl'], ".selectIlname");
			return false;
		}
		if(iLQuery.trim().length == 0){
			common.showcustommsg("#queryScript", globalMessage['anvizent.package.label.pleaseEnterILQuery'], "#queryScript");
			return false;
		}
		
		var dateIndex = iLIncrUpdateQuery.indexOf("{date}");
		if (iLIncrUpdateQuery.trim().length != 0 && dateIndex == -1) {
			common.showcustommsg("#incrementalQuery", globalMessage['anvizent.package.msg.datePlaceholderNotFoundInQuery'], "#incrementalQuery");
			return false;
		}
		
		var date = maxDateQuery.indexOf("{date}");
		if (maxDateQuery.trim().length != 0 && date == -1) {
			common.showcustommsg("#maxDate", globalMessage['anvizent.package.msg.datePlaceholderNotFoundInQuery'], "#maxDate");
			return false;
		}
		
		  var fromDate ="{fromDate}";
		  var toDate="{toDate}";
	   if (historicalLoad.trim().length != 0 && (historicalLoad.indexOf(fromDate) == -1 || historicalLoad.indexOf(toDate) == -1)) {
		  common.showcustommsg("#historyLoad",   globalMessage['anvizent.package.message.variblesnotfound'],"#historyLoad");
		  return false;
    	}
	   if(!status){
		 return false;
		}
		
		showAjaxLoader(true);
		$("#saveILDefaultQueryForm").submit();
		
	  
	});
	
	$(document).on("click",".editdefaultQuery",function(){
		common.clearValidations([".selectConnector","#queryScript","#incrementalQuery","#ilQueryName","#historyLoad"]);
	 	$("#ilQueryName,#queryScript, #incrementalQuery,#historyLoad").val("");
	 	var userID = $("#userID").val();
	 	var ilid = $(this).attr("data-ilId");
	 	var databaseTypeId = $(".selectConnector option:selected").val();
	 	var connectorName= $(".selectConnector option:selected").text().trim();
	 	$("#iLId").val(ilid);
	 		
	 		var selectData = {
	 				ilid : ilid,
	 				databaseTypeId : databaseTypeId,
			}
			
			showAjaxLoader(true);
	 		var url_editILDefaultQuery = "/app_Admin/user/"+userID+"/etlAdmin/editILDefaultQuery";
			 var myAjax = common.postAjaxCallObject(url_editILDefaultQuery,'POST',selectData,headers);
			 myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;
			    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>");
			    			  }
			    			  if(result.messages[0].code=="SUCCESS"){
			    				// updateDefaultQueryAuthPan
			    				  $("#connectorName").val(connectorName);
			    				  $("#databaseTypeId").val(databaseTypeId);
			    				  $("#ilQuerySelect").hide();
			    				  $("#ilQueryText").show();
			    				  $("#update").show();
			    				  $("#save").hide();
			    				  
			    				  defaultQueries.updateDefaultQueryAuthPan(result.object);
			    				  $("#addDefaultQueryPopUp").modal("show");
			    			  }
			    		  }
					}
			});
	 	
			
	});
	
	$("#update").on('click', function(){
		
		var userID = $("#userID").val();
		var iLId = $("#iLId").val();
		var databaseTypeId = $(".selectConnector option:selected").val();
		var iLQuery = $("#queryScript").val();
		var iLIncrUpdateQuery = $("#incrementalQuery").val();
		var historicalLoad = $("#historyLoad").val();
		var maxDateQuery = $("#maxDate").val();
		
		var selectData = {
				iLquery : iLQuery,
				iLIncrementalUpdateQuery : iLIncrUpdateQuery,
				historicalLoad : historicalLoad,
				maxDateQuery : maxDateQuery,
				iLId : iLId,
				iLConnection : {
					database : {
						id : databaseTypeId
					}
				}
		}
		if(iLQuery.trim().length == 0){
			common.showcustommsg("#queryScript", globalMessage['anvizent.package.label.pleaseEnterILQuery'], "#queryScript");
			return false;
		}
		var dateIndex = iLIncrUpdateQuery.indexOf("{date}");
		if (iLIncrUpdateQuery.trim().length != 0 && dateIndex == -1) {
			common.showcustommsg("#incrementalQuery", globalMessage['anvizent.package.msg.datePlaceholderNotFoundInQuery'], "#incrementalQuery");
				return false;
		}
		
		var fromDate ="{fromDate}";
		var toDate="{toDate}";
		  if (historicalLoad.trim().length != 0 && (historicalLoad.indexOf(fromDate) == -1 || historicalLoad.indexOf(toDate) == -1)) {
			  common.showcustommsg("#historyLoad",   globalMessage['anvizent.package.message.variblesnotfound'],"#historyLoad");
			  return false;
	    }
		var date = maxDateQuery.indexOf("{date}");
		  if (maxDateQuery.trim().length != 0 && date == -1) {
			common.showcustommsg("#maxDate", globalMessage['anvizent.package.msg.datePlaceholderNotFoundInQuery'], "#maxDate");
			return false;
		  }
		showAjaxLoader(true);
		var url_updateILDefaultQuery = "/app_Admin/user/"+userID+"/etlAdmin/updateILDefaultQuery";
		   var myAjax = common.postAjaxCall(url_updateILDefaultQuery,'POST', selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				$("#addDefaultQueryPopUp").modal("hide");
				if(result != null && result.hasMessages){
    				  if(result.messages[0].code=="SUCCESS"){
	    				var message = result.messages[0].text;
	    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>").show();
	    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);
	    				  defaultQueries.getdefaultQueriesById(result);
	    			  }
	    			  if(result.messages[0].code=="ERROR"){
	    				  var message = result.messages[0].text;
	    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
	    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);
	    			  }
				}
				else{
					var message = globalMessage['anvizent.package.label.unableToProcessYourRequest'];
  				  	$("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
  				  	setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);
				}
			});
	});
	
}


        