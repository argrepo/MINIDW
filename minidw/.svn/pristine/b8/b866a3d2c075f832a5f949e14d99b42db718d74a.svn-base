var  viewDDlTableResultsTable  = null; 
var ddLayout = {
		initialPage : function() { 
			$("#ddLayoutTablesList").DataTable(
					{
				        "order": [[ 0, "desc" ]],"language": {
			                "url": selectedLocalePath
			            }
				    }
					);
			viewDDlTableResultsTable  = $("#viewDDlTableResultsTable").DataTable({
		        "order": [[ 0, "desc" ]],"language": {
	                "url": selectedLocalePath
	            }
		    });
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
		},
		showMessage:function(text){
			$(".messageText").empty();
			$(".successMessageText").empty();
			$(".messageText").html(text);
		  $(".message").show();
		  setTimeout(function() { $(".message").hide(); }, 10000);
	},
	viewDDlTableQuery: function(object) {
		$("#viewDDlTableQueryHeader").empty().text(object.tableName);
		$("#viewDDlTableQuery").empty().val(object.selectQry);
		$("#viewDDlTableQueryPopUp").modal('show');
	},
	updateTargetTableStructureTable : function(result) {
		var table = $("#targetTableStructure tbody");
		table.empty();
		var tableRow = '';
		if (result.length > 0) {
			var i = 0;
			$.each(result,function(key, obj) {
								var isPrimaryKey = obj["isPrimaryKey"];
								var isNotNull = obj["isNotNull"];
								var isAutoIncrement = obj["isAutoIncrement"];
								var defaultValue = obj["defaultValue"] || '';
								isPrimaryKey == true ? isPrimaryKey = '<span class="glyphicon glyphicon-ok"></span>'
										: isPrimaryKey = '';
								isNotNull == false ? isNotNull = ''
										: isNotNull = '<span class="glyphicon glyphicon-ok"></span>';
								isAutoIncrement == true ? isAutoIncrement = '<span class="glyphicon glyphicon-ok"></span>'
										: isAutoIncrement = '';
								tableRow += "<tr style=' overflow: hidden;'>" + "<td class='col-xs-1'>" + (i + 1) + "</td>"
										+ "<td class='col-xs-2'>" + obj["columnName"] + "</td>"
										+ "<td class='col-xs-1'>" + obj["dataType"] + "</td>"
										+ "<td class='col-xs-2'>" + obj["columnSize"] + "</td>"
										+ "<td class='col-xs-2'>" + isPrimaryKey + "</td>"
										+ "<td class='col-xs-2'>" + isNotNull + "</td>" + "<td class='col-xs-2'>"
										+ isAutoIncrement + "</td></tr>" ;
										/*+ "<td>"+defaultValue+"</td></tr>";*/								
								i++;
							});
		}
		table.append(tableRow);
		$("#viewTargetTableStructurePopUp").modal('show');

	},
	updateDDlTable : function(result) { 
        var  ddLayoutTable  = $("#ddLayoutTablesList").DataTable();
        ddLayoutTable.clear();
	     $.each(result.object, function(key,val) {  
	       var row = [];	
       	   row.push(val.id);
       	   row.push(val.tableName);
       	   row.push('<input id="viewTableStructure" type="button" value="'+globalMessage['anvizent.package.button.viewTableStructure']+'" data-tablename='+val.tableName+' data-ddlayoutid='+val.id+' class="btn btn-primary btn-sm">');
       	   row.push('<input id="viewSelectQry" type="button" value="'+globalMessage['anvizent.package.button.viewQuery']+'" data-ddlayoutid='+val.id+' class="btn btn-primary btn-sm">');
           row.push('<a href="#" class="btn btn-primary btn-xs" data-ddlayoutid='+val.id+' id="editDdlQry" title="edit"><span class="glyphicon glyphicon-edit"></span>');
           row.push('<input id="runQry" type="button" value="'+globalMessage['anvizent.package.label.run']+'" data-ddlayoutid='+val.id+' class="btn btn-primary btn-sm"> ');
           row.push('<button class="btn btn-primary btn-sm" id="viewDDlTableResults" data-tablename='+val.tableName+' data-ddlayoutid='+val.id+' title="'+globalMessage['anvizent.package.link.viewResults']+'">'+globalMessage['anvizent.package.label.viewResults']+'</button>');
           row.push('<button class="btn btn-primary btn-sm" id="deleteDDlTable" data-tablename='+val.tableName+' data-ddlayoutid='+val.id+' title="'+globalMessage['anvizent.package.label.DELETE']+'"> <span class="glyphicon glyphicon-trash" aria-hidden="true" style="color:#fff;"></span></button>');
           ddLayoutTable.row.add(row);
		     });
	      ddLayoutTable.draw(true); 
	     if(result.hasMessages){
			   var messages=[{
					  code : result.messages[0].code,
					  text : result.messages[0].text
				  }];
			   common.displayMessages(messages);
		   }
	},
	viewDDlTableResultsPopUp : function(result,targetTblName) { 
        
        viewDDlTableResultsTable.clear();
	     $.each(result.object, function(key,val) {  
	       var row = [];	
	       row.push((key+1));
       	   row.push(val.insertedCount);
       	   row.push(val.ddlTables); 
           row.push('<button class="btn btn-primary btn-sm" id="viewDDlTableSelectQry"  data-ddlayoutaudittblid='+val.ddlAuditTblId+' title="'+globalMessage['anvizent.package.button.viewQuery']+'">'+globalMessage['anvizent.package.button.viewQuery']+'</button>');
       	   row.push(val.runType);
       	   var errorLog = val.errorMessage;
       	   if(errorLog != null && errorLog != ''){
       		 row.push('<button class="btn btn-primary btn-sm" id="viewDDlErrorLog"  data-errorlog="'+errorLog+'" title="'+globalMessage['anvizent.package.label.ViewErrorLog']+'">'+globalMessage['anvizent.package.label.ViewErrorLog']+'</button>');  
       	   }else{
       		 row.push('-');  
       	   }
       	  
       	   row.push(val.modification.createdTime);
           viewDDlTableResultsTable.row.add(row); 
		     });
	     viewDDlTableResultsTable.draw(true); 
	     $("#viewDDlTableResultsHeader").empty().text(targetTblName);
	    $("#viewDDlTableResultsPopUp").modal('show');
	    	 
	},
	viewSelectQuery :  function(result){
		  if(result.messages[0].code == "SUCCESS") {
				 var  messages=[{
					  code : result.messages[0].code,
					  text : result.messages[0].text
				  }];
				 var sqlScript = result.object.selectQry;
					
				 
				 if(sqlScript === "" || sqlScript === null){
					 sqlScript = "No Query Found.";
				} 
					
				 var params = "";
				 	var ua = window.navigator.userAgent;
				    var msie = ua.indexOf("MSIE ");
				    
				    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))  // If Internet Explorer, return version number
				    {
				    	params = [
					              //'height='+screen.height,
					              'width='+screen.width,
					              'fullscreen=no' // only works in IE, but here for completeness
					          ].join(',');
				    } else {
				    	params = [
					              'height='+screen.height,
					              'width='+screen.width,
					          ].join(',');
				    }
			          popup = window.open('about:blank', '_blank', params); 
			          popup.moveTo(0,0);
			          popup.document.title = "Table script";
			          popup.document.body.innerHTML = "<pre>"+sqlScript+"</pre>";
			          if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
			        	  popup.addEventListener (
				        	        "load",
				        	        function () {
				        	            var destDoc = popup.document;
				        	            destDoc.open ();
				        	            destDoc.title = "DD Layout";
				        	            destDoc.write ('<html><head></head><body><pre>'+sqlScript+'</pre></body></html>');
				        	            destDoc.close ();
				        	        },
				        	        false
				        	    );
			          }

			          
			          
			  }else {
		    		common.displayMessages(result.messages);
		    	}
	  
	}
};
		
if($('.ddLayout-page').length){ 
	ddLayout.initialPage();
	
	var popup = null;
	$(document).on('click', '#viewSelectQry', function(){
		var ddlayoutid = $(this).data("ddlayoutid"); 
		var userID = $("#userID").val();
		var url_getScript = "/app/user/"+userID+"/package/getDDlTableView";
		var selectData = {
				id : ddlayoutid
		};
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
		 showAjaxLoader(true);
		   var myAjax = common.postAjaxCall(url_getScript,'POST', selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    		  if(result != null && result.hasMessages){ 
		    			  ddLayout.viewSelectQuery(result); 
		    		  }else{
		    			  ddLayout.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });
	});
	var ddlayoutid = '';
	$(document).on('click', '#editDdlQry', function(){
		$("#queryPreview,#updateDDlQuery").hide();
		ddlayoutid = $(this).data("ddlayoutid"); 
		var userID = $("#userID").val();
		var url_getScript = "/app/user/"+userID+"/package/getDDlTableView";
		var selectData = {
				id : ddlayoutid
		};
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
		 showAjaxLoader(true);
		   var myAjax = common.postAjaxCall(url_getScript,'POST', selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    		  if(result != null && result.hasMessages){
		    			  if(result.messages[0].code == "SUCCESS") { 
		    				  ddLayout.viewDDlTableQuery(result.object);
		    			  }else {
						    		common.displayMessages(result.messages);
						    	}
		    		  }else{
		    			  ddLayout.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });
	});
	
	$(document).on('click', '#updateDDlQuery', function(){
		var userID = $("#userID").val();
		var selectQry = $("#viewDDlTableQuery").val();
		var selectData = {
				id : ddlayoutid,
				selectQry: selectQry
		};
		console.log("selectData:",selectData);
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
		 showAjaxLoader(true);
		   var url_getScript = "/app/user/"+userID+"/package/updateDDlayoutTable";
		   var myAjax = common.postAjaxCall(url_getScript,'POST', selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    		  if(result != null && result.hasMessages){
		    			  if(result.messages[0].code == "SUCCESS") { 
		    				  common.showSuccessAlert(result.messages[0].text);
		    				  $("#viewDDlTableQueryPopUp").modal('hide');
		    			  }else {
		    				  common.showErrorAlert(result.messages[0].text);
						  }
		    		  }else{
		    			  ddLayout.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });
	});
	 $(document).on("click","#viewTableStructure", function(e){ 
	  		var table = $(this).data("tablename");
	 		var industryId = "0";
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
	 			    			ddLayout.updateTargetTableStructureTable(result.object);
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
	 $(document).on("click","#validateQuery", function(e){ 	  
			$(".queryValidatemessageDiv").empty();
			var userID = $("#userID").val(); 
			var connectionId = 1;
			var isDDlayout = true;
			var typeOfCommand = "Query";		
			var query = $("textarea#viewDDlTableQuery").val();
			if(query.indexOf('Order by') != -1)
				query = query.substring(0, query.indexOf('Order by'));
			 
			common.clearValidations(["#queryScript"]);
			
			if( query != '') {			
				var selectData ={
						iLConnection : {
							connectionId : connectionId,
							ddLayout : isDDlayout
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
				    		  if(result.messages[0].code == "SUCCESS") {
				    			  var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+' <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+'</div>';
				    			  $(".queryValidatemessageDiv").append(message);
				    			  $("#queryPreview,#updateDDlQuery").show();
				    			  $(".queryValidatemessageDiv").show();
				    			  setTimeout(function() { $(".queryValidatemessageDiv").hide(); }, 10000);
				    		  }else{
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
				
			} else {			 
				common.showcustommsg("#queryScript", globalMessage['anvizent.package.label.queryShouldNotBeEmpty']);
			}
		});

		// validate query table Preview
		$(document).on('click', '#queryPreview', function() {
			var userID = $("#userID").val(); 
			var connectionId = $("#connectionId").val();
			var typeOfCommand = "Query"; 
			var query = $("textarea#viewDDlTableQuery").val();
			var packageId = $("#packageId").val();
			var il_incremental_update = false;
			var ilId = $("#ilId").val();
			var databaseTypeId = 1;
			var protocal = "jdbc:mysql://";
			var isDDlayout = true;
			if(query.indexOf('Order by') != -1 && protocal.indexOf('sqlserver') != -1)
				query = query.substring(0, query.indexOf('Order by'));
			
			common.clearValidations(["#viewDDlTableQuery"]);
			if( query != '') {
				var selectData ={
						packageId : packageId,
						iLId : ilId,
						iLConnection : {
							connectionId : connectionId,
							ddLayout : isDDlayout
						},
						iLquery : query,
						typeOfCommand : typeOfCommand,
						il_incremental_update : il_incremental_update
				}			
				showAjaxLoader(true);
				var url_checkQuerySyntax = "/app/user/"+userID+"/package/getTablePreview";
				 var myAjax = common.postAjaxCall(url_checkQuerySyntax,'POST', selectData,headers);
				    myAjax.done(function(result) {
				    	showAjaxLoader(false);
				    	  if(result != null){
				    		  if(result.hasMessages){
				    			  if(result.messages[0].code == "ERROR") { 
				    				  common.showErrorAlert(result.messages[0].text);
				    				  return false;
				    			  }  
				    		  }			    		  
				    		  $("#tablePreviewPopUp").modal('show');
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
				common.showcustommsg("#viewDDlTableQuery",globalMessage['anvizent.package.label.queryShouldNotBeEmpty']);
			}
		});
		
		var ddlayoutid = '';
		$(document).on('click', '#deleteDDlTable', function(){
			 ddlayoutid = $(this).data("ddlayoutid");
			$("#deleteDDlTableAlert").modal('show');
		});
		
		$(document).on('click', '#confirmDeleteDDlTable', function(){
			$("#deleteDDlTableAlert").modal('hide');
			var userID = $("#userID").val();
			var url_getScript = "/app/user/"+userID+"/package/deleteDDlayoutTable";
			var selectData = {
					id : ddlayoutid,
			};
			var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
			 showAjaxLoader(true);
			   var myAjax = common.postAjaxCall(url_getScript,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    		  if(result != null && result.hasMessages){
			    			  if(result.messages[0].code == "SUCCESS") { 
			    				  ddLayout.updateDDlTable(result);
			    			  }else {
			    				  common.showErrorAlert(result.messages[0].text);
			    				  return false;
							    	}
			    		  }else{
			    			  ddLayout.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
			    		  }
			    });
		});
		
		$(document).on('click', '#runQry', function(){
			var userID = $("#userID").val();
			var ddlayoutid = $(this).data("ddlayoutid");
			var url_getScript = "/app/user/"+userID+"/package/runDDlayoutTable";
			var selectData = {
					id : ddlayoutid,
			};
			var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
			 showAjaxLoader(true);
			   var myAjax = common.postAjaxCall(url_getScript,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    		  if(result != null && result.hasMessages){
			    			  if(result.messages[0].code == "SUCCESS") { 
			    				  var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+' <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+'</div>';
				    			  $(".ddlRunStatus").empty().append(message);
				    			  $(".ddlRunStatus").show();
				    			  setTimeout(function() { $(".ddlRunStatus").hide(); }, 10000);
			    			  }else {
							    		common.displayMessages(result.messages);
							    	}
			    		  }else{
			    			  ddLayout.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
			    		  }
			    });
		});
		
	 
		
		$(document).on('click', '#viewDDlTableResults', function(){
			var userID = $("#userID").val();
			var ddlayoutId = $(this).data("ddlayoutid");
			var targetTblName = $(this).data("tablename");
			var url_getScript = "/app/user/"+userID+"/package/viewDDlTableResults";
			var selectData = {
					id : ddlayoutId,
			};
			var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
			 showAjaxLoader(true);
			   var myAjax = common.postAjaxCall(url_getScript,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    		  if(result != null && result.hasMessages){
			    			  if(result.messages[0].code == "SUCCESS") { 
			    				  ddLayout.viewDDlTableResultsPopUp(result,targetTblName);
			    			  }else {
							    		common.displayMessages(result.messages);
							    	}
			    		  }else{
			    			  ddLayout.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
			    		  }
			    });
		});
		$(document).on('click', '#viewDDlTableSelectQry', function(){
			var ddlayoutaudittblid = $(this).data("ddlayoutaudittblid"); 
			var userID = $("#userID").val();
			var url_getScript = "/app/user/"+userID+"/package/viewDDlTableSelectQry";
			var selectData = {
					ddlAuditTblId : ddlayoutaudittblid
			};
			var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
			 showAjaxLoader(true);
			   var myAjax = common.postAjaxCall(url_getScript,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    		  if(result != null && result.hasMessages){
			    			  ddLayout.viewSelectQuery(result); 
			    		  }else{
			    			  ddLayout.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
			    		  }
			    });
		});
		
		$(document).on('click', '#viewDDlErrorLog', function(){
			var errorlog = $(this).data("errorlog");
			$("#errorLog").empty().text(errorlog);
			$("#viewDDlTableErrorLoHeader").empty().text(globalMessage['anvizent.package.label.errorLog']);
			$("#viewDDlTableErrorLogPopup").modal('show');
		});
		 $(document).on('keyup',"#viewDDlTableQuery", function(){
			 $("#updateDDlQuery,#queryPreview").hide();
			  
	    }); 	
		
}
