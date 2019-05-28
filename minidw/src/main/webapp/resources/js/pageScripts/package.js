var headers = {};
var packages = {
		initialPage : function() {
			$("#standardPackageTable").DataTable( {
		        "order": [[ 0, "desc" ]],
		        "language": {
	                "url": selectedLocalePath
	            }
		    } );
			$("#customPackageTable").DataTable( {
		        "order": [[ 1, "desc" ]],
		        "language": {
	                "url": selectedLocalePath
	            }
		    } );
			var allPages = $(".clientILsAndDLs")
			$(allPages).find("input:checkbox").prop('checked',true);
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
	},
	updatePackageTable: function(result,pkgName) {
		var filterBy = $("#filterPackages").val();
		var standardpackageTable = $("#standardPackageTable").DataTable();
		var customPackageTable = $("#customPackageTable").DataTable();
		var isScheduled='';
			  if (result != null) {
				  	standardpackageTable.clear();
					customPackageTable.clear();
			   for (var i = 0; i < result.object.length; i++) {				   
				   var isActivePackage = result.object[i].isActive ? "active" : "deleted";
				   //standard packages
				   if((result.object[i].isStandard && isActivePackage == filterBy) || (result.object[i].isStandard && filterBy == "all")){
					    var packageId = result.object[i].packageId !=null ? result.object[i].packageId : "";
					    var packageName = result.object[i].packageName != null ? result.object[i].packageName.encodeHtml() : "";
					    var industry = result.object[i].industry != null ? result.object[i].industry.name.encodeHtml() : "";
					    var description = result.object[i].description !=null ? result.object[i].description.encodeHtml() : "";
					    var isActive = result.object[i].isActive == true ? globalMessage['anvizent.package.button.yes'] : globalMessage['anvizent.package.button.no'];
					    var isStandard = result.object[i].isStandard ? result.object[i].isStandard : "";
					    var isMapped = result.object[i].isMapped == true ? globalMessage['anvizent.package.button.yes'] : globalMessage['anvizent.package.button.no'];
					    var viewJobResult = null;
					    if(result.object[i].isActive == true){
					    		isScheduled = result.object[i].isScheduled == true ? globalMessage['anvizent.package.button.yes'] : globalMessage['anvizent.package.button.no'];
					    	  	if(result.object[i].isScheduled){
							    	viewJobResult ="<td><a class='btn btn-primary btn-sm tablebuttons' href='"+adt.appContextPath+"/adt/package/jobResults/"+packageId+"'>"+globalMessage['anvizent.package.label.viewResults']+"</a></td>";
							    }else{
							    	viewJobResult = "<td>N/A</td>"
							    }
					    }else{
					    	 	isScheduled='No';
					    		if(result.object[i].isScheduled){
							    	viewJobResult ="<td><a class='btn btn-primary btn-sm tablebuttons disabled' href='#'>"+globalMessage['anvizent.package.label.viewResults']+"</a></td>";
							    }else{
							    	viewJobResult = "<td>N/A</td>"
							    }
					    }					  
					    
					    var row = [];
					    row.push(packageId);
					    row.push(packageName);
					    row.push(isActive);
					    row.push(isScheduled);
					    if(result.object[i].isActive == true){
					    	row.push("<a class='btn btn-primary btn-sm tablebuttons' href='"+adt.appContextPath+"/adt/package/standardPackage/edit/"+packageId+"' title='Edit'> <span class='fa fa-pencil' aria-hidden='true'></span></a>");
						    row.push("<td  class='smalltd'><button type ='button' class='btn btn-primary btn-sm deletePackage tablebuttons' title='Archive' data-id = "+packageId+" data-pkgName="+packageName+" title='Archive'><span class='glyphicon glyphicon-folder-close' aria-hidden='true'></span></button></td>");
						   
					    }else{
					    	row.push("<a class='btn btn-primary btn-sm tablebuttons disabled' href='#'> <span class='fa fa-pencil' aria-hidden='true'></span></a>");
						    row.push("<td  class='smalltd'><button type ='button' class='btn btn-primary btn-sm   tablebuttons disabled'><span class='glyphicon glyphicon-folder-close' aria-hidden='true'></span></button></td>");
						   
					    }
					     row.push(viewJobResult);
					    standardpackageTable.row.add(row);
				   }
				   //custom packages
				   else if((!result.object[i].isStandard && isActivePackage == filterBy) || (!result.object[i].isStandard && filterBy == "all")){
						   	var packageId = result.object[i].packageId !=null ? result.object[i].packageId : "";
						    var packageName = result.object[i].packageName != null ? result.object[i].packageName.encodeHtml() : "";
						    var industry = result.object[i].industry != null ? result.object[i].industry.name.encodeHtml() : "";
						    var description = result.object[i].description !=null ? result.object[i].description.encodeHtml() : "";
						    var isActive = result.object[i].isActive == true ? globalMessage['anvizent.package.button.yes'] : globalMessage['anvizent.package.button.no'];
						    var isStandard = result.object[i].isStandard ? result.object[i].isStandard : "";							      
							var isProcessed = "";
						    if(result.object[i].table != null){
						    	
						    	isProcessed += "<b>"+globalMessage['anvizent.package.label.targetTable']+"</b> :"+result.object[i].table.tableName.encodeHtml();
						    	isProcessed += "<br>";
						    	isProcessed += "<b>"+globalMessage['anvizent.package.label.status']+"</b> :";
						    	
						    	if(result.object[i].table.isProcessed){
						    		if(result.object[i].isActive == true){
						    				isProcessed +=  "<a href='"+adt.appContextPath+"/adt/package/viewResultsForCustomPackage/"+packageId+"' data-packageid='"+packageId+"' data-industryid='"+result.object[i].industry.id+"' data-pname='"+packageName+"'>"+globalMessage['anvizent.package.link.completed']+"</a>";
						    		}else{
						    			isProcessed +=globalMessage['anvizent.package.link.completed'];
						    		}
						    	}else{
						    		isProcessed += globalMessage['anvizent.package.label.pending'];
						    	}
						    }else{
						    	isProcessed = "No";
						    }
						    var isScheduled = '';
						    
						       if(result.object[i].isActive == true)
						    	isScheduled = result.object[i].isScheduled == true ? "Yes" : "No";
						       if(result.object[i].isActive == false)
						    	isScheduled='No';
						       
						    var isMapped = result.object[i].isMapped == true ? "Yes" : "No";
						    var editOrView='';
						    
						     if(result.object[i].table != null){
						    	if(result.object[i].isActive == true){
						    		 editOrView +=  "<a class='btn btn-primary btn-sm tablebuttons' href='"+adt.appContextPath+"/adt/package/customPackage/edit/"+packageId+"' title='"+globalMessage['anvizent.package.label.edit']+"'>";
									    	
									    	/*editOrView += '<span class="glyphicon glyphicon-eye-open" title='+globalMessage['anvizent.package.label.view']+'></span>';*/
									   
									    	editOrView += '<span class="fa fa-pencil" title='+globalMessage['anvizent.package.label.edit']+' aria-hidden="true">';
						    	}
						    	if(result.object[i].isActive == false){
						    		  editOrView +=  "<a class='btn btn-primary btn-sm tablebuttons disabled' href='#'>";
									    	/*editOrView += '<span class="glyphicon glyphicon-eye-open" title='+globalMessage['anvizent.package.label.view']+'></span>';*/
									    
									    	editOrView += '<span class="fa fa-pencil" title='+globalMessage['anvizent.package.label.edit']+' aria-hidden="true">';
						    	}
						      }else{
						    	  	if(result.object[i].isActive == true){
						    	  		  editOrView +=  "<a class='btn btn-primary btn-sm tablebuttons' href='"+adt.appContextPath+"/adt/package/customPackage/edit/"+packageId+"' title='"+globalMessage['anvizent.package.label.edit']+"'>";
									      editOrView += '<span class="fa fa-pencil" title='+globalMessage['anvizent.package.label.edit']+' aria-hidden="true">';
						    	  	}else{
						    	  		editOrView +=  "<a class='btn btn-primary btn-sm tablebuttons disabled' href='#'>";
									     editOrView += '<span class="fa fa-pencil" title='+globalMessage['anvizent.package.label.edit']+' aria-hidden="true">';
						    	  	}
						    	  
						    } 
						    
						    editOrView += "</a>";
						    var row = [];
						    row.push("<td><input type='checkbox' class='check' data-pkgName = "+packageName+" data-tableid="+packageId+"></td>");
						    row.push(packageId);
						    row.push(packageName);
						    row.push(isActive);
						    row.push(isProcessed);
						    row.push(isScheduled);
						    row.push(editOrView);
						    if(result.object[i].isActive == true){
						    row.push("<td  class='smalltd'><button type ='button' class='btn btn-primary btn-sm deletePackage tablebuttons' data-id = "+packageId+" data-pkgName="+packageName+" title='"+globalMessage['anvizent.package.label.archive']+"'><span class='glyphicon glyphicon-folder-close' aria-hidden='true'></span></button></td>");
						    }
						    if(result.object[i].isActive == false){ 
						    	 row.push("<td  class='smalltd'><button type ='button' class='btn btn-primary btn-sm   tablebuttons disabled'><span class='glyphicon glyphicon-folder-close' aria-hidden='true'></span></button></td>");
						    }
						    
						    row.push("<td  class='smalltd'><button type ='button' class='btn btn-primary btn-sm cloneCustomPackage' data-id = "+packageId+" title='"+globalMessage['anvizent.package.label.clone']+"'>"+globalMessage['anvizent.package.label.clone']+"</button></td>");
						    customPackageTable.row.add(row);
				   }
			   }
			   standardpackageTable.draw(true);
			   customPackageTable.draw(true);
			   if(result.hasMessages){
				   var messages=[{
						  code : result.messages[0].code,
						  text : result.messages[0].text
					  }];
				   common.displayMessages(messages);
			   }
			  }
	},
	
	filterPackageTable: function(result,filterBy){ 
		debugger
		var standardpackageTable = $("#standardPackageTable").DataTable();
		var customPackageTable = $("#customPackageTable").DataTable();
		standardpackageTable.clear();
		customPackageTable.clear();
		standardpackageTable.draw(true);
		   customPackageTable.draw(true);
		var isScheduled='';
			  if (result && result.object ) {
				  	
			   for (var i = 0; i < result.object.length; i++) {
				   //standard packages
				   var isActivePackage = result.object[i].isActive ? "active" : "deleted"; 
				   
				   if((result.object[i].isStandard && isActivePackage == filterBy) || (result.object[i].isStandard && filterBy == "all")){
					   
					    var packageId = result.object[i].packageId != null ? result.object[i].packageId : "";
					    var packageName = result.object[i].packageName != null ? result.object[i].packageName.encodeHtml() : "";
					    var industry = result.object[i].industry != null ? result.object[i].industry.name.encodeHtml() : "";
					    var description = result.object[i].description !=null ? result.object[i].description.encodeHtml() : "";
					    var isActive = result.object[i].isActive == true ? globalMessage['anvizent.package.button.yes'] : globalMessage['anvizent.package.button.no'];
					    var isStandard = result.object[i].isStandard ? result.object[i].isStandard : "";					      
					    var isMapped = result.object[i].isMapped == true ? globalMessage['anvizent.package.button.yes'] : globalMessage['anvizent.package.button.no'];
					    var viewJobResult = null;
					    if(result.object[i].isActive == true){
					    	  isScheduled = result.object[i].isScheduled == true ? globalMessage['anvizent.package.button.yes'] : globalMessage['anvizent.package.button.no'];
					    	  if(result.object[i].isScheduled){
							    	viewJobResult ="<td><a class='btn btn-primary btn-sm tablebuttons' href='"+adt.appContextPath+"/adt/package/jobResults/"+packageId+"'>"+globalMessage['anvizent.package.label.viewResults']+"</a></td>";
							  }else{
							    	viewJobResult = "<td>N/A</td>"
							  }
					    }else{
					    	  isScheduled='No';
					    	  if(result.object[i].isScheduled){
							    	viewJobResult ="<td><a class='btn btn-primary btn-sm tablebuttons disabled' href='#'>"+globalMessage['anvizent.package.label.viewResults']+"</a></td>";
							  }else{
							    	viewJobResult = "<td>N/A</td>"
							  }
					    }					  
					    debugger
					    var row = [];
					    
					    row.push("<td><input type='checkbox' class='check' data-pkgName = "+packageName+" data-tableid="+packageId+"></td>");
					    row.push(packageId);
					    row.push(packageName);
					    row.push(isActive);
					    row.push(isScheduled);
					    if(result.object[i].isActive == true){
					    	row.push("<a class='btn btn-primary btn-sm tablebuttons' href='"+adt.appContextPath+"/adt/package/standardPackage/edit/"+packageId+"' title='"+globalMessage['anvizent.package.label.edit']+"'> <span class='fa fa-pencil' aria-hidden='true'></span></a>");
						    row.push("<td  class='smalltd'><button type ='button' class='btn btn-primary btn-sm deletePackage tablebuttons' data-id = "+packageId+" data-pkgName="+packageName+" title='"+globalMessage['anvizent.package.label.archive']+"'><span class='glyphicon glyphicon-folder-close' aria-hidden='true'></span></button></td>");
						   
					    }else{
					    	row.push("<a class='btn btn-primary btn-sm tablebuttons disabled' href='#'> <span class='fa fa-pencil' aria-hidden='true'></span></a>");
						    row.push("<td  class='smalltd'><button type ='button' class='btn btn-primary btn-sm   tablebuttons disabled'><span class='glyphicon glyphicon-folder-close' aria-hidden='true'></span></button></td>");
						   
					    }
					     row.push(viewJobResult);
					    standardpackageTable.row.add(row);
				   }
				   //custom packages
				   else if((!result.object[i].isStandard && isActivePackage == filterBy) || (!result.object[i].isStandard && filterBy == "all")){
			   			var packageId = result.object[i].packageId !=null ? result.object[i].packageId : "";
					    var packageName = result.object[i].packageName != null ? result.object[i].packageName.encodeHtml() : "";
					    var industry = result.object[i].industry != null ? result.object[i].industry.name.encodeHtml() : "";
					    var description = result.object[i].description !=null ? result.object[i].description.encodeHtml() : "";
					    var isActive = result.object[i].isActive == true ? globalMessage['anvizent.package.button.yes'] : globalMessage['anvizent.package.button.no'];
					    var isStandard = result.object[i].isStandard ? result.object[i].isStandard : "";
					    var isProcessed = "";
					    if(result.object[i].table != null){
					    	
					    	isProcessed += "<b>Target Table</b> :"+result.object[i].table.tableName.encodeHtml();
					    	isProcessed += "<br>";
					    	isProcessed += "<b>Status</b> :";
					    	
					    	if(result.object[i].table.isProcessed){
					    		if(result.object[i].isActive == true){
					    				isProcessed +=  "<a href='"+adt.appContextPath+"/adt/package/viewResultsForCustomPackage/"+packageId+"' data-packageid='"+packageId+"' data-industryid='"+result.object[i].industry.id+"' data-pname='"+packageName+"'>"+globalMessage['anvizent.package.link.completed']+"</a>";
					    		}else{
					    			isProcessed +=globalMessage['anvizent.package.link.completed'];
					    		}	
					    	}else{
					    		isProcessed += globalMessage['anvizent.package.label.pending'];
					    	}
					    }else{
					    	isProcessed = globalMessage['anvizent.package.button.no'];
					    }
					    var isScheduled = '';					    
				        if(result.object[i].isActive == true)
				        	isScheduled = result.object[i].isScheduled == true ? globalMessage['anvizent.package.button.yes'] : globalMessage['anvizent.package.button.no'];
				        if(result.object[i].isActive == false)
				        	isScheduled=globalMessage['anvizent.package.button.no'];
					       
					    var isMapped = result.object[i].isMapped == true ? globalMessage['anvizent.package.button.yes'] : globalMessage['anvizent.package.button.no'];
					    var editOrView='';
					    
					     if(result.object[i].table != null){
					    	if(result.object[i].isActive == true){
					    		 editOrView +=  "<a class='btn btn-primary btn-sm tablebuttons' href='"+adt.appContextPath+"/adt/package/customPackage/edit/"+packageId+"' title='"+globalMessage['anvizent.package.label.edit']+"'>";
								    	
								    	/*editOrView += '<span class="glyphicon glyphicon-eye-open" title='+globalMessage['anvizent.package.label.view']+'></span>';*/
								    
								    	editOrView += '<span class="fa fa-pencil" title='+globalMessage['anvizent.package.label.edit']+' aria-hidden="true">';
					    	}
					    	if(result.object[i].isActive == false){
					    		  editOrView +=  "<a class='btn btn-primary btn-sm tablebuttons disabled' href='#'>";
								    	/*editOrView += '<span class="glyphicon glyphicon-eye-open" title='+globalMessage['anvizent.package.label.view']+'></span>';*/
								    
								    	editOrView += '<span class="fa fa-pencil" title='+globalMessage['anvizent.package.label.edit']+' aria-hidden="true">';
					    	}
					       }
					       else{
					    	  	if(result.object[i].isActive == true){
					    	  		  editOrView +=  "<a class='btn btn-primary btn-sm tablebuttons' href='"+adt.appContextPath+"/adt/package/customPackage/edit/"+packageId+"' title='"+globalMessage['anvizent.package.label.edit']+"'>";
								      editOrView += '<span class="fa fa-pencil" title='+globalMessage['anvizent.package.label.edit']+' aria-hidden="true">';
					    	  	}else{
					    	  		editOrView +=  "<a class='btn btn-primary btn-sm tablebuttons disabled' href='#'>";
								     editOrView += '<span class="fa fa-pencil" title='+globalMessage['anvizent.package.label.edit']+' aria-hidden="true">';
					    	  	}
					      } 
					    
					    editOrView += "</a>";
					    var row = [];
					    if(result.object[i].isActive == false){
					    	row.push("<td><input type='checkbox' disabled='disabled'  class='check' data-pkgName = "+packageName+" data-tableid="+packageId+"></td>");	
					    }else{
					    	row.push("<td><input type='checkbox' class='check' data-pkgName = "+packageName+" data-tableid="+packageId+"></td>");
					    }
					    
					    row.push(packageId);
					    row.push(packageName);
					    row.push(isActive);
					    row.push(isProcessed);
					    row.push(isScheduled);
					    row.push(editOrView);
					    if(result.object[i].isActive == true){
					    row.push("<td  class='smalltd'><button type ='button' class='btn btn-primary btn-sm deletePackage tablebuttons' title='"+globalMessage['anvizent.package.label.archive']+"' data-id = "+packageId+" data-pkgName="+packageName+" title='"+globalMessage['anvizent.package.label.archive']+"'><span class='glyphicon glyphicon-folder-close' aria-hidden='true'></span></button></td>");
					    row.push("<td  class='smalltd'><button type ='button' class='btn btn-primary btn-sm cloneCustomPackage' data-id = "+packageId+" title='"+globalMessage['anvizent.package.label.clone']+"'>"+globalMessage['anvizent.package.label.clone']+"</button></td>");
					    }
					    if(result.object[i].isActive == false){
					    	 row.push("<td  class='smalltd'><button type ='button' class='btn btn-primary btn-sm   tablebuttons disabled'><span class='glyphicon glyphicon-folder-close' aria-hidden='true'></span></button></td>");
					    	 row.push("<td  class='smalltd'><button type ='button' class='btn btn-primary btn-sm cloneCustomPackage' disabled='disabled' data-id = "+packageId+" title='"+globalMessage['anvizent.package.label.clone']+"'>"+globalMessage['anvizent.package.label.clone']+"</button></td>");
					    }
					    customPackageTable.row.add(row);
				   }
				    
			   }
			   standardpackageTable.draw(true);
			   customPackageTable.draw(true);			  
			 }
	},	
	
	
	getPackageSourcesInfo : function($button,packageId){
		debugger
		var userID = $("#userID").val();
		var packageId = $("#packageId").val();
		var packageName = $button.attr("data-pkgName");
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true);
	    var url_getPackageSourcesinfo = "/app/user/"+userID+"/package/getILsConnectionMappingInfoByPackage/"+packageId+"";
		var myAjax = common.loadAjaxCall(url_getPackageSourcesinfo,'GET','',headers);
	    myAjax.done(function(result) {
	   	showAjaxLoader(false);
	   	  if(result != null && result.hasMessages){
	   		  if(result.messages[0].code=="FAILED"){
				  common.showErrorAlert(result.messages[0].text);
			  }
			  if(result.messages[0].code=="SUCCESS"){
				  
				  if(result.object.length == 0){
				   		common.showErrorAlert(globalMessage['anvizent.package.label.noSourceIsAdded']);
				   		$button.prop("disabled",true);
				   	  }
				  else{
					  $("#cloneCustomPackageModal").modal('show');
					  $("#packageName").val(packageName+"_copy");
					  $("#packageTitle").text("Clone "+packageName);
		    			  var table = $("#sourceMappingInfoTable tbody");
		    			  table.empty();
	    				  var tableRow='';
	    				  for(var i=0; i<result.object.length; i++){
				    			  tableRow +=  "<tr>"+
									"<td>"+(i+1)+"</td>";    							
				    			  if(result.object[i].ilConnectionMapping.isFlatFile){
				    				  tableRow += "<td>"+globalMessage['anvizent.package.label.flatFile']+"</td>";
				    				  tableRow += "<td>"+result.object[i].ilConnectionMapping.filePath+"</td>";
				    				  tableRow +=  "<td class='smalltd'><a class='btn btn-primary btn-sm startLoader' target='_blank' href='"+adt.appContextPath+"/adt/package/viewCustomPackageSource/"+packageId+"/"+result.object[i].ilConnectionMapping.connectionMappingId+"'>"+globalMessage['anvizent.package.label.viewDetails']+"</a></td>";
				    			  
				    			  }else{
				    				  var tableName = "";
				    				  tableRow += "<td>"+globalMessage['anvizent.package.label.database']+"</td>";    		    				  
				    				  if(result.object[i].ilConnectionMapping.parent_table_name == null){
				    					  tableRow += "<td>N/A</td>";
				    				  }
				    				  else{
				    					  tableRow += "<td>"+result.object[i].ilConnectionMapping.parent_table_name+"</td>";
				    				  }
				    				   tableRow +=  "<td class='smalltd'><a class='btn btn-primary btn-sm startLoader' target='_blank' href='"+adt.appContextPath+"/adt/package/viewCustomPackageSource/"+packageId+"/"+result.object[i].ilConnectionMapping.connectionMappingId+"'>"+globalMessage['anvizent.package.label.viewDetails']+"</a></td>";
				    				  }
									tableRow +=  "</tr>"
	    					}
	    				table.append(tableRow);
					  } 
				  }
				  
	   	  }else if(result.object.length == 0){
	   		common.showErrorAlert(globalMessage['anvizent.package.label.noSourceIsAdded']);
	   		$button.prop("disabled",true);
	   	  }else{
		    		var messages = [ {
		    			code : globalMessage['anvizent.message.error.code'],
		    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
		    		} ];
		    		common.displayMessages(messages);
		    	}
	   });
		
	}
	
};


if($('.package-page').length || $('.purge-page').length){
	packages.initialPage();
	debugger
	var table = $(".clientILsAndDLs").DataTable( {  "order": [[ 1, "asc" ]],"language": {
        "url": selectedLocalePath
    } } );
	var packageId ='';
	debugger
	$(document).on("click",".check",function(){
		if($(".check:checked").length > 0)
			$("#archievePackage").show();
		else
			$("#archievePackage").hide();
	})
	
	$(document).on('click', '.deletePackage,#archievePackage', function(){
		debugger
		packageId = $(this).attr("data-id");
		var packageName = $(this).attr("data-pkgName");
		$("#confirmDeletePackage").attr("href","");
		var deletePackageUrl = adt.appContextPath +"/adt/package/deletePackages/"+packageId;
		$("#confirmDeletePackage").attr("href",deletePackageUrl);
		//$("#confirmDeletePackage").attr("data-packagename",packageName.encodeHtml());
		$("#delePackageAlert").modal('show');
	});
	
	$("#confirmDeletePackage").click(function() {
		debugger
		var packageName = $(this).attr("data-packagename");
		var userID = $("#userID").val();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true);
		
		var packageIdsList = [];
		if($(".check:checked").length != 0){
			$(".check:checked").each(function(){
				packageIdsList.push($(this).attr("data-tableid"));
			});
		}else{
			packageIdsList.push(packageId);
		}
		
		var url =  "/app/user/"+userID+"/package/disablePackage?packageIds="+packageIdsList.join(",");
	    var myAjax = common.loadAjaxCall(url,'POST','',headers);
	    myAjax.done(function(result) {
	    	showAjaxLoader(false);
	    	  if(result != null && result.hasMessages){
	    		  if(result.messages[0].text != null){
	    			  $("#archievePackage").hide();
	    			  packages.updatePackageTable(result,packageName);
	    		  }
	    	  }
	    	  packageId='';
	    	  $("#delePackageAlert").modal('hide');
	    });
	});
	
	$("#customPackages").on('click', '.show-staus', function() {
		var obj = $(this),
		packageid = obj.data('packageid'),
		industryid = obj.data('industryid'),
		packageName = obj.data('pname');
		
		var userID = $("#userID").val();
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url = "/app/user/"+userID+"/package/showCustomPackageTablesStatus/"+packageid+"/"+industryid;
		var myAjax = common.loadAjaxCall(url,'GET','',headers);
	    myAjax.done(function(result) {
    	  if(result != null && result.hasMessages) {
    		  if(result.messages[0].code == "SUCCESS"){
    		  console.log('result ', result);
    		  var obj = result["object"];
    		  
    		  var table = obj["table"];
    		  var derivatives = obj["derivatives"];
    		  
    		  var cTable = $("table#customTargetTableStaus").find('tbody');
    		  cTable.empty();
    		  
    		  var tr = [];
    		  
    		  tr.push('<tr>');
    		  tr.push('<td>', table['tableName'] , '</td>');
    		  tr.push('<td>', table['totalRecords'] , '</td>');
    		  tr.push('<td>', table['noOfRecordsProcessed'] , '</td>');
    		  tr.push('<td>', table['noOfRecordsFailed'] , '</td>');
    		  cTable.append(tr.join(''));
    		  
    		  var dTable = $("table#customDerivativeTableStaus").find('tbody');
    		  dTable.empty();
    		  
    		  var len= derivatives.length;
    		  
    		  if (len>0) {
    		  
	    		  for (var i=0; i<len; i++) {
	    			  var t = derivatives[i];
	    			  tr = [];
	        		  
	        		  tr.push('<tr>');
	        		  tr.push('<td>', t['tableName'] , '</td>');
	        		  tr.push('<td>', t['totalRecords'] , '</td>');
	        		  tr.push('<td>', t['noOfRecordsProcessed'] , '</td>');
	        		  tr.push('<td>', t['noOfRecordsFailed'] , '</td>');
	        		  dTable.append(tr.join(''));
	    		  }
	    		  dTable.closest('div.table-responsive').show();
    		  }
    		  else {
    			  dTable.closest('div.table-responsive').hide();
    		  }
    		  
    		  var popup = $("#showCustomPackageStaus");
    		  popup.find('h4.modal-title').text(packageName);
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
	
	$(document).on("change","#filterPackages",function(){
		var filterBy = $(this).val();
		var userID = $("#userID").val();	
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true);		
		var url =  "/app/user/"+userID+"/package/filterPackagesOnIsActive";
	    var myAjax = common.loadAjaxCall(url,'GET','',headers);
	    myAjax.done(function(result) {
	    	showAjaxLoader(false);
	    	  if(result != null && result.hasMessages){
	    		  console.log(result)
	    		  if(result.messages[0].code == "SUCCESS"){
	    			  packages.filterPackageTable(result,filterBy);	    			
	    		  }
	    		  else{ 
	    			  packages.filterPackageTable(result,filterBy);	 
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
	
$(document).on("click",".purgeUserTables",function(){
		
		common.clearcustomsg("#selectOneTableAlert");
		var selCols = $("#tableName").find("input.check:checked");
		if (selCols.length === 0) {
			var message = globalMessage['anvizent.package.label.pleaseSelectAtleastOneTable']
			common.showcustommsg("#selectOneTableAlert", message);
			
			return false;
		}
		$("#alertForPurgeUserTables").modal("show");
	});



	
	$(document).on("click",".confirmPurgeUserTables",function(){
		
		var allPages = $(".clientILsAndDLs")	
		var userID = $("#userID").val();
		var tableNames = [];
		table.$(".check:checked").each(function(){
			tableNames.push({"tableName":$(this).val(), "tableId":$(this).data("tableid") });
		});
		
		var selectedData = {
				targetTables : tableNames
		}
		
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true);		
		var url =  "/app/user/"+userID+"/package/purgeUserTables";
	    var myAjax = common.loadAjaxCall(url,'POST',tableNames,headers);
	    myAjax.done(function(result) {
	    	showAjaxLoader(false);
	    	  if(result != null && result.hasMessages){
	    			  $("#successOrErrorMessage").show();
	    			  $("#alertForPurgeUserTables").modal("hide");
	    			  var message = "";
	    			  if(result.messages[0].code=="SUCCESS"){
	    				  message = result.messages[0].text;
	    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>");
	    			  }
	    			  if(result.messages[0].code=="ERROR" || result.messages[0].code=="FAILED"){
	    				  message = result.messages[0].text;
	    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>");
	    			  }
	    			  setTimeout(function() { $("#successOrErrorMessage").hide(); }, 10000);
	    		  
	    		  $(allPages).find(".check").prop('checked',false);
    			  $(allPages).find(".selectAll").prop('checked',false);
	    		   
	    	  }	else{
		    		var messages = [ {
			    			code : globalMessage['anvizent.message.error.code'],
			    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
			    		} ];
			    		common.displayMessages(messages);
			    	}    	 
	    });
	});
	
  // Handle click on "Select all" control
   $('.selectAll').on('click', function(){
      // Get all rows with search applied
      var rows = table.rows({ 'search': 'applied' }).nodes();
      // Check/uncheck checkboxes for all rows in the table
      $('input[type="checkbox"]', rows).prop('checked', this.checked);
      
   });

   // Handle click on checkbox to set state of "Select all" control
   $('.clientILsAndDLs tbody').on('change', 'input[type="checkbox"]', function(){
      // If checkbox is not checked
	  if(!this.checked){
         var el = $('.selectAll').get(0);
         // If "Select all" control is checked and has 'indeterminate' property
         if(el && el.checked && ('indeterminate' in el)){
            // Set visual state of "Select all" control 
            // as 'indeterminate'
            el.indeterminate = true;
         }
      }
      
   });


	$(document).on("keyup",function (e) {
		if (e.keyCode == 119) {
			$("a.createpackage").click();
			window.location.href = $("a.createpackage").prop("href");
		}
		return true;
	});
	
	$(document).on("click",".cloneCustomPackage",function(){
		$("#packageName").val("");
		common.clearValidations(["#packageName"]);
		$("#packageId").val($(this).attr("data-id"));
		var packageName = $(this).attr("data-pkgName");
		var packageId = $("#packageId").val();
		var $button = $(this)
		packages.getPackageSourcesInfo($button,packageId);
		
	});
	
	$(document).on("click","#createCloneCustomPackage",function(){
		debugger
		var userID = $("#userID").val();
		var packageId = $("#packageId").val();
		var packageName = $("#packageName").val();
		var validStatus = true;
		var industryId = 0;
		var selectors = [];
		selectors.push({"selector":"#packageName","message":"Please enter Name","regex":"","regexMsg":""});
		var regex = /^[a-zA-Z0-9_-]+$/;
   	    validStatus = common.validateWithCustomMessages(selectors);
   	    if(!regex.test(packageName)){
   	    	common.showcustommsg("#packageName", globalMessage['anvizent.package.message.specialCharactersandOnlyAllowed']);
   	    	validStatus = false;
   	    }
   	 
		if(!validStatus){
			return false;
		}else{
			var selectData = {
					existingPackageId : packageId,
					packageName : packageName,
					isFromExistingPackages : true,
					industryId : industryId
			}
			showAjaxLoader(true);

			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			var url =  "/app/user/"+userID+"/package/createCustomPackage";
		    var myAjax = common.loadAjaxCall(url,'POST',selectData,headers);
		    myAjax.done(function(result) {
		    	
		    	showAjaxLoader(false);
		    	  if(result != null && result.hasMessages){
		    			  var message = "";
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  $("#cloneCustomPackageModal").modal('hide');
		    				  setTimeout(function(){ window.location.reload(); }, 1000);
		    				  var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							  $(".message-class").empty().append(message).show();
							  setTimeout(function() { $(".message-class").hide(); }, 5000);
		    			  }
		    			  if(result.messages[0].code=="ERROR" || result.messages[0].code=="FAILED"){
		    				  common.showErrorAlert(result.messages[0].text);
							  return false;
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
					
	})
}

