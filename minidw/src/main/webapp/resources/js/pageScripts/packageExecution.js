var headers = {};
var viewUploaStatusInfoResultsTable = null;
var viewExecutionStatusInfoTable=null;
var packageExecution = {
		initialPage : function() { 
			viewUploaStatusInfoResultsTable  = $("#viewUploaStatusInfoResultsTable").DataTable({
		        "order": [[ 0, "desc" ]],"language": {
	                "url": selectedLocalePath
	            },
		    });
			viewExecutionStatusInfoTable  = $("#viewExecutionStatusInfoTable").DataTable({
		        "order": [[ 0, "desc" ]],"language": {
	                "url": selectedLocalePath
	            }
		    });
			
			$("#packageExecutionTable").DataTable({
		        "order": [[ 1, "desc" ]],"language": {
	                "url": selectedLocalePath
	            },
	            "bPaginate": false,
		    });
			
		},
		showMessage:function(text){
			$(".messageText").empty();
			$(".successMessageText").empty();
			$(".messageText").html(text);
		    $(".message").show();
		    setTimeout(function() { $(".message").hide(); }, 10000);
	  },
	  viewExecutionStatusComments :  function(result){
			  if(result.messages[0].code == "SUCCESS") {
					 var  messages=[{
						  code : result.messages[0].code,
						  text : result.messages[0].text
					  }];
					 var executionComments = result.object;
						
					 
					 if(executionComments === "" || executionComments === null){
						 executionComments = "Execution comments not found.";
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
				          popup.document.title = "Upload Or Execution Status Comments";
				          popup.document.body.innerHTML = "<pre>"+executionComments+"</pre>";
				          if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
				        	  popup.addEventListener (
					        	        "load",
					        	        function () {
					        	            var destDoc = popup.document;
					        	            destDoc.open ();
					        	            destDoc.title = "DD Layout";
					        	            destDoc.write ('<html><head></head><body><pre>'+executionComments+'</pre></body></html>');
					        	            destDoc.close ();
					        	        },
					        	        false
					        	    );
				          }
	
				          
				          
				  }else {
			    		common.displayMessages(result.messages);
			    	}
		  
		},
		viewUploaStatusInfoPopUp : function(result){
			 
			 viewUploaStatusInfoResultsTable.clear();
			 if(result != null){				
				 for (var i = 0; i < result.object.length; i++) {
					 var executionId = result.object[i].executionId;
					 var uploadStatus = result.object[i].uploadStatus;
					 var uploadComments =  result.object[i].uploadComments;
					 var executionStatus =  result.object[i].executionStatus;
					 var executionComments =  result.object[i].executionComments;
					 var row = [];
					 
					 row.push(i+1);
					 row.push(executionId);
					 row.push(uploadStatus);
					 row.push(uploadComments);
					 row.push(executionStatus);
					 row.push(executionComments);
					 
					 viewUploaStatusInfoResultsTable.row.add(row);
				}
				 viewUploaStatusInfoResultsTable.draw(true);
				 $("#viewUploaStatusInfoHeader").empty().text("Execution Mapping Info");
				 $("#viewUploaStatusInfoPopUp").modal('show');
			}
		},
		viewExecutionStatusInfoPopUp : function(result){
			viewExecutionStatusInfoTable.clear();
			 if(result != null){				
				 for (var i = 0; i < result.object.length; i++) {
					 var executionId = result.object[i].executionId;
					 var executionStatus =  result.object[i].executionStatus;
					 var tagetTableName =  result.object[i].tagetTableName;
					 var executionComments =  result.object[i].executionComments;
					 var executionStartDate = result.object[i].executionStartDate;
					 var lastExecutedDate =  result.object[i].lastExecutedDate;
					 
					 var row = [];
					 
					 row.push(i+1);
					 row.push(executionId);
					 row.push(tagetTableName);
					 row.push(executionStatus);
					 row.push("<pre style='border: 0;background-color: transparent;'>"+executionComments+"</pre>");
					 row.push(executionStartDate);
					 row.push(lastExecutedDate);
					 
					 viewExecutionStatusInfoTable.row.add(row);
				}
				 viewExecutionStatusInfoTable.draw(true);
				 $("#viewExecutionStatusInfoHeader").empty().text("Target Table Execution Info");
				 $("#viewExecutionStatusInfoPopUp").modal('show');
			}
		},
};
 
if($('.packageExecution-page').length ){ 
	
	packageExecution.initialPage();
	var packageId = $("#packageId").val();
	var popup = null;
	 
	$(document).on('click', '#uploadExecutionStatusInfo', function(){
		var executionId = $(this).attr("data-executionId");  
		var userID = $("#userID").val();
		var url_getScript = "/app/user/"+userID+"/package/getPackageExecutionSourceMappingInfo";
		var selectData = {
				executionId : executionId,
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
		    				  packageExecution.viewUploaStatusInfoPopUp(result);
		    			  }else {
						    		common.displayMessages(result.messages);
						    	}
		    		  }else{
		    			  packageExecution.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });
	});
	
	$(document).on('click', '#executionStatusInfo', function(){
		var executionId = $(this).attr("data-executionId");  
		var userID = $("#userID").val();
		var url_getScript = "/app/user/"+userID+"/package/getPackageExecutionTargetTableInfo";
		var selectData = {
				executionId : executionId,
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
		    				  packageExecution.viewExecutionStatusInfoPopUp(result);
		    			  }else {
						    		common.displayMessages(result.messages);
						    	}
		    		  }else{
		    			  packageExecution.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });
	});
	
	$(document).on('change', '#executionPaginationId', function(){
		var packageId = $("#packageId").val();
		var paginationId = $(this).val();  
		var dlId = $("#dlId").val();  
		var userID = $("#userID").val();
		var getPackageExecutionByPaginationId = '';
		var offset = paginationId - 1;
			if(packageId == 0){
				getPackageExecutionByPaginationId =	"/app/user/"+userID+"/getPackageExecutionByPaginationId?"+"dlId="+dlId+"&offset="+offset+"&limit=10";
			}else{
				getPackageExecutionByPaginationId =	"/app/user/"+userID+"/package/getCustomPackageExecutionByPaginationId?"+"packageId="+packageId+"&offset="+offset+"&limit=10";	
			}
		 
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
		 showAjaxLoader(true);
		   var myAjax = common.loadAjaxCall(getPackageExecutionByPaginationId,'GET', '',headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    		  if(result != null && result.hasMessages){
		    			  if(result.messages[0].code == "SUCCESS") { 
		    				  var $tbody = $("#packageExecutionTableTbody");
		    				  $tbody.empty();
		    				  var packageExecutionList = result.object;
		    				  if(packageExecutionList != null && packageExecutionList.length != 0)
		    					  {
		    					  $.each(packageExecutionList, function(index, packageExecution) {
		    						 var sno = index+1;
		    						var $tr = " <tr> "+
									    "<td>"+ sno  +"</td>"+
										"<td>"+packageExecution.executionId+"</td>"+
										"<td>"+ packageExecution.runType +"</td>";
										if(packageExecution.initiatedFrom == 'schedule'){
											$tr += 	"<td>Schedule</td>";
										}
										else if(packageExecution.initiatedFrom == 'runwithscheduler'){
											$tr += 	"<td>Schedule</td>";
										}else{
											$tr += 	"<td>Run Now</td>";
										}
										$tr += "<td>"+packageExecution.uploadStatus+"<br>"+
										     "<a href='#' data-executionId='"+packageExecution.executionId+"'  data-uploadOrExecution='upload' id='uploadExecutionStatusInfo' style='text-transform: capitalize;'>Info</a>"+" | "+
										     '<a href="#" onClick="window.open('+"'"+"/minidw/adt/package/executionInfo/"+packageExecution.executionId+"/upload"+"'"+','+"'"+"Comments"+"'"+','+"'"+"width=1700,height=700"+"'"+')" data-executionId="'+packageExecution.executionId+'" data-uploadOrExecution="upload" id="executionStatus" style="text-transform: capitalize;">Detailed Info</a>'+
										     "<br>"+
										     "Start Date :"; 
										     if(packageExecution.uploadStartDate == null ){
										     $tr += "-" ;
										     }else{
										     $tr += packageExecution.uploadStartDate ; 
										     }
										     $tr += "<br> End Date :";
									    	 if(packageExecution.lastUploadedDate == null ){
											     $tr += "-" ;
											     }else{
											     $tr += packageExecution.lastUploadedDate ; 
											     }
									    	 $tr +=  "</td>";
										if(packageExecution.executionStatus == null){
											$tr += "<td>-</td>";
										}else if(packageExecution.executionStatus != 'IGNORED') {
											$tr += "<td>"+packageExecution.executionStatus+"<br>"+
								    		"<a href='#' data-executionId='"+packageExecution.executionId+"' data-uploadOrExecution='execution' id='executionStatusInfo' style='text-transform: capitalize;'>Info</a>"+" | "+
								    		'<a href="#" onClick="window.open('+"'"+"/minidw/adt/package/executionInfo/"+packageExecution.executionId+"/execution"+"'"+', '+"'"+"newwindow"+"'"+','+"'"+"width=1700,height=700"+"'"+')" data-executionId="'+packageExecution.executionId+'" data-uploadOrExecution="execution" id="executionStatus" style="text-transform: capitalize;">Detailed Info</a>'+
								    	    "<br>"+
								    	    "Start Date :"
								    	    if(packageExecution.executionStartDate == null ){
											     $tr += "-" ;
											     }else{
											     $tr += packageExecution.executionStartDate ; 
											     }
								    	   
											$tr += "<br>End Date :";
											 if(packageExecution.lastExecutedDate == null ){
											     $tr += "-" ;
											     }else{
											     $tr += packageExecution.lastExecutedDate ; 
											     }
								    	    
											 $tr +=  "</td>";
										}else{
											$tr += "<td>"+packageExecution.executionStatus+"</td>";
										}	
										 
										if(packageExecution.druidStatus == null){
											$tr += "<td>-</td>";
										}else if(packageExecution.druidStatus != 'IGNORED') {
											$tr += "<td>"+packageExecution.druidStatus+"<br>"+
								    	//	"<a href='#' data-executionId='"+packageExecution.executionId+"' data-uploadOrExecution='execution' id='executionStatusInfo' style='text-transform: capitalize;'>Info</a>"+" | "+
											'<a href="#" onClick="window.open('+"'"+"/minidw/adt/package/executionInfo/"+packageExecution.executionId+"/druidStatus"+"'"+', '+"'"+"Comments"+"'"+','+"'"+"width=1700,height=700"+"'"+')" data-executionId="'+packageExecution.executionId+'" data-uploadOrExecution="druidStatus" id="executionStatus" style="text-transform: capitalize;">Detailed Info</a>'+
								    	    "<br>"+
								    	    "Start Date :";
					    	    		  if(packageExecution.druidStartDate == null ){
											     $tr += "-" ;
											     }else{
											     $tr += packageExecution.druidStartDate ; 
											     }
								    	   
											$tr += "<br>End Date :";
											 if(packageExecution.druidEndDate == null ){
											     $tr += "-" ;
											     }else{
											     $tr += packageExecution.druidEndDate ; 
											     }
					    	    		
					    	    		 $tr +=  "</td>";
										}else{
											$tr += "<td>"+packageExecution.druidStatus+"</td>";
										}	 
										$tr +="</tr>";
										$tbody.append($tr);
		    						});
		    					  }
		    				  else{
		    					  $tbody.append('<tr><td rowspan="8">No Data Found</td></tr>');
		    				  }
		    			  }
		    			  else 
		    			  {
						    common.displayMessages(result.messages);
						  }
		    		  }else{
		    			  packageExecution.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });
	});
	
	
	$(document).on('click','.refresh',function(){
		window.location.reload();
	});
}
