migrateTemplate = {
		initialPage : function(){
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
			$("#sourceServer,#destinationServer,#sourceClient,#destinationClient").select2({               
                allowClear: true,
                theme: "classic"
			}); 

				
		},
		
}
if($('.migrateTemplate-page').length){
	migrateTemplate.initialPage();

	$(document).on('click', '#migrateTemplate', function(){
		
	        var userID = $("#userId").val();
		    var headers = {};
		    var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			showAjaxLoader(true);
		     var getAllServerConfigurations_url = "/app_Admin/user/"+userID+"/etlAdmin/getAllServerConfigurations";
		     var myAjax = common.loadAjaxCall(getAllServerConfigurations_url,'GET',headers);
		     myAjax.done(function(result) {	
		    	 showAjaxLoader(false);
		    	  if(result != null){ 
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") { 
		    				  common.showErrorAlert(result.messages[0].text)
			    			  return false;
		    			  } else if(result.messages[0].code == "SUCCESS") {		
		    				  var serverConfigurationList = result.object;
		    				  var sourceServer = $("#sourceServer");
		    				  var destinationServer = $("#destinationServer");
		    				  sourceServer.empty().append("<option value='select'>select</option>");
		    				  destinationServer.empty().append("<option value='select'>select</option>");
	    					  $.each(serverConfigurationList, function(key,val) {       
	    						  var options = "<option value='"+val.id+"'>"+val.shortName+"</option>";
	    						  sourceServer.append(options);
	    						  destinationServer.append(options);
	    				        })
		    				  $("#serverConfiguration").show();
		    			  }
		    			 
		    		  }
		    	  }		    	   
		    }); 
	});
	
	$(document).on('change', '#sourceServer,#destinationServer', function(){
		   common.clearValidations(['#sourceClient','#sourceServer','#destinationClient','#destinationServer']);
	        var userID = $("#userId").val();
	        var serverId = $(this).val();
	        
	        var $thisId = $(this).attr('id');
	        
	        common.clearcustomsg("#sourceServer");
	        if(serverId == "select"){
	        	common.showcustommsg("#sourceServer", "Please choose server");
	        	return false;
	        }
		    var headers = {};
		    var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			showAjaxLoader(true);
			
		     var getClientDetailsByServer_url = "/app_Admin/user/"+userID+"/etlAdmin/getAllClientsByServer/"+serverId;
		     
		     var myAjax = common.loadAjaxCall(getClientDetailsByServer_url,'GET',headers);
		     myAjax.done(function(result) {	
		    	 showAjaxLoader(false);
		    	  if(result != null){ 
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") { 
		    				  common.showErrorAlert(result.messages[0].text)
			    			  return false;
		    			  } else if(result.messages[0].code == "SUCCESS") {		
		    				  var clientList = result.object;
		    				  if($thisId == 'sourceServer'){
		    					  var sourceClient = $("#sourceClient");
		    					  sourceClient.empty().append("<option value='select'>select</option>");
		    					  $.each(clientList, function(key,val) {       
		    						  var options = "<option value='"+val+"'>"+val+"</option>";
		    						  sourceClient.append(options);
		    				        });
		    					  $('#sourceClientLable,#sourceClientSelectBox').show();
		    				  }else{
		    					  var destinationClient = $("#destinationClient");
			    				  destinationClient.empty().append("<option value='select'>select</option>");
		    					  $.each(clientList, function(key,val) {       
		    						  var options = "<option value='"+val+"'>"+val+"</option>";
		    						  destinationClient.append(options);
		    				        })
		    				      $('#destinationClientSelectBox,#destinationClientLable,#viewDestinastionServerClientMetaInfo').show();
		    				  }
		    				  $("#clientConfiguration").show();
		    			  }
		    			 
		    		  }
		    	  }		    	   
		    }); 
	});
	
	 
	$(document).on('change', '#sourceClient,#destinationClient', function(){
		   
        var userID = $("#userId").val();
        var sourceClientId =  $("#sourceClient option:selected").val();
        var sourceServerId = $("#sourceServer option:selected").val();
        var destinationServerId = $("#destinationServer option:selected").val();
        var destinationClientId = $("#destinationClient option:selected").val();
        
        common.clearValidations(['#sourceClient','#sourceServer','#destinationClient','#destinationServer']);
		
		 var validStatus = true;
		 
		  if(sourceClientId == "select"){
			   common.showcustommsg("#sourceClient","Please choose source client","#sourceClient");
			   validStatus =false;
			  }
		  
		  if(sourceServerId == "select" || sourceServerId == ""){
			   common.showcustommsg("#sourceServer","Please choose source server","#sourceServer");
			   validStatus =false;
			  }
		  if(destinationClientId == "select" || destinationClientId == '' ){
			   common.showcustommsg("#destinationClient","Please choose destination client","#destinationClient");
			   validStatus =false;
			  }
		  if(destinationServerId == "select" || destinationServerId == ""){
			   common.showcustommsg("#destinationServer","Please choose destination server","#destinationServer");
			   validStatus =false;
			  }
		   
		  if(!validStatus){
			 	return validStatus;
		   }
		
        
		  var selectData={
				  clientId : sourceClientId,
				  serverId : sourceServerId,
				  destinationServerId:destinationServerId,
				  destinationClientId :destinationClientId
				  
		   };
		  
	    var headers = {};
	    var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
	     showAjaxLoader(true);
	     var getDependencyDetails_url = "/app_Admin/user/"+userID+"/etlAdmin/getDependencyDetailsForSourceServerClient";
	     var myAjax = common.postAjaxCall(getDependencyDetails_url,'POST', selectData,headers);
	     myAjax.done(function(result) {		 
	     showAjaxLoader(false);
	    	  if(result != null){ 
	    		  if(result.hasMessages){
	    			  if(result.messages[0].code == "ERROR") { 
	    				  common.showErrorAlert(result.messages[0].text)
		    			  return false;
	    			  } else if(result.messages[0].code == "SUCCESS") { 
	    				  
	    				  var userDbWsConList = result.object;
	    				 
	    				  console.log("userDbWsConList -- >",userDbWsConList);
	    				  
	    				  $.each(userDbWsConList, function(key,val) { 
	    					  
							     if(key == 0){
							      var packageList = val;
							      var packageTableTBody = $("#packageTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
							      packageTableTBody.clear();
							      
							      var destinationUserDetais = "";
                                  $.each(packageList.destinationPackageList, function(destinationPackageKey,destinationPackageVal) {  
                                	   if(destinationPackageKey == 0){
                                		  $.each(destinationPackageVal.user, function(destinationUserKey,destinationUserVal) {  
                                			  destinationUserDetais += "<option value="+destinationUserVal.userId+">"+destinationUserVal.userName+"</option>";
                                	  });
                                	  }
		    					     });
                                  $("#destinationUserList").empty().append(destinationUserDetais).multipleSelect( {
              									filter : true,
              									placeholder : 'Select Destination Users',
              									position: 'top'
              								});
                                  
		                           $.each(packageList.sourcePackagesList, function(sourcePackageKey,sourcePackageVal) {  
		                        	   var packageType = sourcePackageVal.isStandard == true ? "Standard Package" : "Custom Package" ;
		                        	   var row = [];	
		                        	   row.push('<input type="checkbox" class="check" data-packageId='+sourcePackageVal.packageId+'>');
		                        	   row.push(sourcePackageVal.packageId);
		                        	   row.push(sourcePackageVal.packageName);
		                        	   row.push(packageType);
		                        	   row.push(sourcePackageVal.modification.createdBy);
		                        	 //  row.push(destinationUserDetais);
		                        	   
		                        	   packageTableTBody.row.add(row);
		    					     });
	    					     
		                           packageTableTBody.draw(true); 
							     } 	 
		    			     
	    					     if(key == 1){
	    					     var userTableTbody = $("#userTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
	    					     userTableTbody.clear();
	    					     var userList = val;
	    					     var CheckBox = '';
	    					     var bExists = false;
                                   $.each(userList.sourceUsersList, function(sourceUserKey,sourceUserVal) {  
                                	   $.each(userList.destinationUsersList, function(destinationUserKey,destinationUserVal) {  
                                    	   if(destinationUserVal.userId === sourceUserVal.userId){
                                    		   CheckBox = '<input type="text" disabled="disabled" value ="Already Exist"  data-sourceUserId='+sourceUserVal.userId+'>';    
                                    		   return false;
                                    	   }else{
                                    		   CheckBox =  '<input type="checkbox" class="check" data-sourceUserId='+sourceUserVal.userId+'>';
                                    	   }
  		    					      });
                                         var row = [];	
		                        	   row.push(CheckBox);
		                        	   row.push(sourceUserVal.userId);
		                        	   row.push(sourceUserVal.userName);
		                        	   userTableTbody.row.add(row);
		    					     });
                                   userTableTbody.draw(true); 
	    					     
	    					    } 	 
	    					     if(key == 2){
	    					         var  dbConTableTbody = $("#dbConTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
	    					         dbConTableTbody.clear();
	    					    	 var dbConList = val;
	    					    	 var destinationUserDetais = "";
	                                  $.each(dbConList.destinationDbConnectionList, function(destinationDbConKey,destinationDbConVal) {  
	                                	   if(destinationDbConKey == 0){
	                                		  $.each(destinationDbConVal.user, function(destinationUserKey,destinationUserVal) {  
	                                			  destinationUserDetais += "<option value="+destinationUserVal.userId+">"+destinationUserVal.userName+"</option>";
	                                	  });
	                                	  }
			    					     });
	                                  $("#destinationDbConUserList").empty().append(destinationUserDetais).multipleSelect( {
	              									filter : true,
	              									placeholder : 'Select Destination Users',
	              									position: 'top'
	              								});
		    					     $.each(dbConList.sourceDbConnectionList, function(sourceDbConKey,sourceDbConVal) {  
		    					    	  var row = [];	
			                        	   row.push('<input type="checkbox" class="check" data-sourceDbConId='+sourceDbConVal.connectionId+'>');
			                        	   row.push(sourceDbConVal.connectionId);
			                        	   row.push(sourceDbConVal.database.name);
			                        	   
			                        	   dbConTableTbody.row.add(row);
			    					     });
		    					     
		    					     dbConTableTbody.draw(true); 
	    					     }
	    					   
                                  if(key == 3){
                                	 var wsConTableTbody = $("#wsConTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
                                	 wsConTableTbody.clear();
                                	 var wsConList = val;
                            
                                	 var destinationUserDetais = "";
	                                  $.each(wsConList.destinationWsConnectionList, function(destinationWsConKey,destinationWsConVal) {  
	                                	   if(destinationWsConKey == 0){
	                                		  $.each(destinationWsConVal.user, function(destinationUserKey,destinationUserVal) {  
	                                			  destinationUserDetais += "<option value="+destinationUserVal.userId+">"+destinationUserVal.userName+"</option>";
	                                	  });
	                                	  }
			    					     });
	                                  $("#destinationWsConUserList").empty().append(destinationUserDetais).multipleSelect( {
	              									filter : true,
	              									placeholder : 'Select Destination Users',
	              									position: 'top'
	              								});
                                	 
 		    					     $.each(wsConList.sourceWsConnectionList, function(sourceWsConKey,sourceWsConVal) {  
 		    					    	 var row = [];	
			                        	   row.push('<input type="checkbox" class="check" data-sourceWsConId='+sourceWsConVal.id+'>');
			                        	   row.push(sourceWsConVal.id);
			                        	   row.push(sourceWsConVal.webServiceTemplateMaster.webServiceName);
			                        	   wsConTableTbody.row.add(row);
			    					     });
 		    					        wsConTableTbody.draw(true); 
	    					     }
                                  
                                  if(key == 4){
                                 	 var specificDlJarsTableTbody = $("#specificDlJarsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
                                 	specificDlJarsTableTbody.clear();
                                 	 var specificDlJarsList = val;
                                 
  		    					     $.each(specificDlJarsList.sourceSpecificDlJarsList, function(specificDlJarsKey,specificDlJarsVal) {  
  		    					    	 var row = [];	
			                        	   row.push('<input type="checkbox" class="check" data-sourceDlJarsId='+specificDlJarsVal.dLId+'>');
			                        	   row.push(specificDlJarsVal.dLId);
			                        	   row.push(specificDlJarsVal.clientSpecificJobName);
			                        	   row.push(specificDlJarsVal.clientSpecificJobVersion);
			                        	   specificDlJarsTableTbody.row.add(row);
			    					     });
  		    					         specificDlJarsTableTbody.draw(true); 
 	    					     }
                                  
                                  if(key == 5){
                                 	 var specificIlJarsTableTbody = $("#specificIlJarsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
                                 	specificIlJarsTableTbody.clear();
                                 	 var specificIlJarsList = val;
                                 	 
  		    					     $.each(specificIlJarsList.sourceSpecificIlJarsList, function(specificIlJarsKey,specificIlJarsVal) {  
  		    					    	 var row = [];	
			                        	   row.push('<input type="checkbox" class="check" data-sourceIlJarsId='+specificIlJarsVal.iLId+'>');
			                        	   row.push(specificIlJarsVal.iLId);
			                        	   row.push(specificIlJarsVal.clientSpecificJobName);
			                        	   row.push(specificIlJarsVal.clientSpecificJobVersion);
			                        	   specificIlJarsTableTbody.row.add(row);
			    					     });
  		    					        specificIlJarsTableTbody.draw(true);
 	    					     }
                                  
                                  if(key == 6){
                                  	 var specificVerticalsTableTbody = $("#specificVerticalTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
                                     specificVerticalsTableTbody.clear();
                                  	 var specificVerticalsList = val;
                                  	 
   		    					     $.each(specificVerticalsList.sourceSpecificTemplatesList, function(specificVerticalsKey,specificVerticalsVal) {   
   		    						   var row = [];	
		                        	   row.push('<input type="checkbox" class="check" data-sourceVerticalsId='+specificVerticalsVal.id+'>');
		                        	   row.push(specificVerticalsVal.id);
		                        	   row.push(specificVerticalsVal.name);
		                        	   specificVerticalsTableTbody.row.add(row);
		    					     });
   		    					     specificVerticalsTableTbody.draw(true);
  	    					     }
                                  
                                  
                                  if(key == 7){
                                   	 var specificTableScriptsTableTbody = $("#specificTableScriptsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
                                     specificTableScriptsTableTbody.clear();
                                   	 var specificTableScriptsList = val;
    		    					     $.each(specificTableScriptsList.sourceSpecificTableScriptsList, function(specificTableScriptsKey,specificTableScriptsVal) {   
    		    					    	  var row = [];	
    			                        	   row.push('<input type="checkbox" class="check" data-sourceTableScriptsId='+specificTableScriptsVal.id+'>');
    			                        	   row.push(specificTableScriptsVal.id);
    			                        	   row.push(specificTableScriptsVal.scriptName);
    			                        	   row.push(specificTableScriptsVal.scriptType);
    			                        	   specificTableScriptsTableTbody.row.add(row);
    			    					     });
    		    					     specificTableScriptsTableTbody.draw(true);
   	    					        }
                                  
	    					     });
	    				  $("#tabsDiv").show();  
	    			   }
	    			  
	    		  }
	    	  }		    	   
	    }); 
    });
	
	$(document).on('click', '#viewDestinastionServerClientMetaInfo', function(){

		 
        var userID = $("#userId").val();
        var sourceClientId =  $("#sourceClient option:selected").val();
        var sourceServerId = $("#sourceServer option:selected").val();
        var destinationServerId = $("#destinationServer option:selected").val();
        var destinationClientId = $("#destinationClient option:selected").val();
        
        common.clearValidations(['#sourceClient','#sourceServer','#destinationClient','#destinationServer']);
		
		 var validStatus = true;
		 
	/*	  if(sourceClientId == "select"){
			   common.showcustommsg("#sourceClient","Please choose source client","#sourceClient");
			   validStatus =false;
			  }
		  
		  if(sourceServerId == "select" || sourceServerId == ""){
			   common.showcustommsg("#sourceServer","Please choose source server","#sourceServer");
			   validStatus =false;
			  }*/
		  if(destinationClientId == "select" || destinationClientId == '' ){
			   common.showcustommsg("#destinationClient","Please choose destination client","#destinationClient");
			   validStatus =false;
			  }
		  if(destinationServerId == "select" || destinationServerId == ""){
			   common.showcustommsg("#destinationServer","Please choose destination server","#destinationServer");
			   validStatus =false;
			  }
		   
		  if(!validStatus){
			 	return validStatus;
		   }
		
        
		  var selectData={
				 // clientId : sourceClientId,
				//  serverId : sourceServerId,
				  destinationServerId:destinationServerId,
				  destinationClientId:destinationClientId,
				  
		   };
		  
	    var headers = {};
	    var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
	     showAjaxLoader(true);
	     var getDependencyDetails_url = "/app_Admin/user/"+userID+"/etlAdmin/getDependencyDetailsForDestinationServerClient";
	     var myAjax = common.postAjaxCall(getDependencyDetails_url,'POST', selectData,headers);
	     myAjax.done(function(result) {		 
	     showAjaxLoader(false);
	    	  if(result != null){ 
	    		  if(result.hasMessages){
	    			  if(result.messages[0].code == "ERROR") { 
	    				  common.showErrorAlert(result.messages[0].text)
		    			  return false;
	    			  } else if(result.messages[0].code == "SUCCESS") { 
	    				  
	    				  var destinationServerClientDetails = result.object;
	    				  
	    				  console.log("destinationServerClientDetails:---->",destinationServerClientDetails);
	    				  
	    				  $.each(destinationServerClientDetails, function(key,val) { 
	    					   
							     if(key == 0){
							      var packageTableTBody = $("#destinationPackageTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
							      packageTableTBody.clear();
			    				  var packageList = val;
		                           $.each(packageList.destinationPackagesList, function(sourcePackageKey,sourcePackageVal) {  
		                        	   var packageType = sourcePackageVal.isStandard == true ? "Standard Package" : "Custom Package" ;
		                        	   var row = [];	
		                        	   row.push(sourcePackageVal.packageId);
		                        	   row.push(sourcePackageVal.packageName);
		                        	   row.push(packageType);
		                        	   row.push(sourcePackageVal.modification.createdBy);
		                        	   packageTableTBody.row.add(row);
		                        	   
		                           });
		                           packageTableTBody.draw(true); 
							     } 	 
		    			     
	    					     if(key == 1){
	    					     var userTableTbody = $("#destinationUserTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
	    					     userTableTbody.clear();
	    					     var userList = val;
	    					   
                                   $.each(userList.destinationUsersList, function(sourceUserKey,sourceUserVal) {  
                                	   var row = [];	
		                        	   row.push(sourceUserVal.userId);
		                        	   row.push(sourceUserVal.userName);
		                        	   userTableTbody.row.add(row);
		    					     });
	    					     
                                   userTableTbody.draw(true); 
	    					     
	    					    } 	 
	    					     if(key == 2){
	    					         var  dbConTableTbody = $("#destinationDbConTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
	    					         dbConTableTbody.clear();
	    					    	 var dbConList = val;
	    					     
		    					     $.each(dbConList.destinationDbConnectionList, function(sourceDbConKey,sourceDbConVal) {  
		    					    	   var row = [];	
			                        	   row.push(sourceDbConVal.connectionId);
			                        	   row.push(sourceDbConVal.database.name);
			                        	   dbConTableTbody.row.add(row);
		    					     });
	    					    	
		    					     dbConTableTbody.draw(true); 
	    					     }
	    					     
                                  if(key == 3){
                                	 var wsConTableTbody = $("#destinationWsConTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
                                	 wsConTableTbody.clear();
                                	 var wsConList = val;
                                 
 		    					     $.each(wsConList.destinationWsConnectionList, function(sourceWsConKey,sourceWsConVal) {  
 		    					    	   var row = [];	
			                        	   row.push(sourceWsConVal.id);
			                        	   row.push(sourceWsConVal.webServiceTemplateMaster.webServiceName);
			                        	   wsConTableTbody.row.add(row);
 		    					     });
 		    					    wsConTableTbody.draw(true);
	    					     }
                                  
                                 if(key == 4){
                                 	 var specificDlJarsTableTbody = $("#destinationSpecificDlJarsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
                                 	 specificDlJarsTableTbody.clear();
                                 	 var specificDlJarsList = val;
                                 
  		    					     $.each(specificDlJarsList.destinationSpecificDlJarsList, function(specificDlJarsKey,specificDlJarsVal) {  
  		    					      var row = [];	
		                        	   row.push(specificDlJarsVal.dLId);
		                        	   row.push(specificDlJarsVal.clientSpecificJobName);
		                        	   row.push(specificDlJarsVal.clientSpecificJobVersion);
		                        	   specificDlJarsTableTbody.row.add(row); 
  		    					       });
  		    					   specificDlJarsTableTbody.draw(true);
                                 }
                                  
                                  if(key == 5){
                                 	 var specificIlJarsTableTbody = $("#destinationSpecificIlJarsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
                                   	specificIlJarsTableTbody.clear();
                                 	 var specificIlJarsList = val;
                               
  		    					     $.each(specificIlJarsList.destinationSpecificIlJarsList, function(specificIlJarsKey,specificIlJarsVal) {  
    		    					      var row = [];	
   		                        	      row.push(specificIlJarsVal.iLId);
   		                        	      row.push(specificIlJarsVal.clientSpecificJobName);
   		                        	      row.push(specificIlJarsVal.clientSpecificJobVersion);
   		                        	      specificIlJarsTableTbody.row.add(row); 
     		    					      });
  		    					      specificIlJarsTableTbody.draw(true);
                                     }
                                  
                                  if(key == 6){
                                  	 var specificTemplatesTableTbody = $("#destinationSpecificVertivcalTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
                                  	specificTemplatesTableTbody.clear();
                                  	 var specificTemplatesList = val;
                                   
   		    					     $.each(specificTemplatesList.destinationSpecificVerticalsList, function(specificVertivcalKey,specificVertivcalVal) {   
   		    					     var row = [];	
		                        	      row.push(specificVertivcalVal.id);
		                        	      row.push(specificVertivcalVal.name);
		                        	      specificTemplatesTableTbody.row.add(row); 
		    					      });
   		    					  specificTemplatesTableTbody.draw(true);
  	    					     }
                                  
                                  
                                  if(key == 7){
                                   	 var specificTableScriptsTableTbody = $("#destinationSpecificTableScriptsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
                                   	specificTableScriptsTableTbody.clear();
                                   	 var specificTableScriptsList = val;
                                   
    		    					     $.each(specificTableScriptsList.destinationSpecificTableScriptsList, function(specificTableScriptsKey,specificTableScriptsVal) {   
    		    					      var row = [];	
   		                        	      row.push(specificTableScriptsVal.id);
   		                        	      row.push(specificTableScriptsVal.scriptName);
   		                        	      row.push(specificTableScriptsVal.scriptType);
   		                        	     specificTableScriptsTableTbody.row.add(row); 
   		    					      });
    		    					     specificTableScriptsTableTbody.draw(true);
   	    					     }
                                  
	    					     });
	    				  $("#destinationTabsDiv").show();  
	    				  $("#destinationServerClientMetaInfoPopUp").modal('show');
	    			   }
	    			  
	    		  }
	    	  }		    	   
	    }); 
    });
	
 
   // Handle click on "Select all" control
   $('.packageTableSelectAll').on('click', function(){
      // Get all rows with search applied
      var rows = $("#packageTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }}).rows({ 'search': 'applied' }).nodes();
      // Check/uncheck checkboxes for all rows in the table
      $('input[type="checkbox"]', rows).prop('checked', this.checked);
      
   });

   // Handle click on checkbox to set state of "Select all" control
   $('#packageTable tbody').on('change', 'input[type="checkbox"]', function(){
      // If checkbox is not checked
      if(!this.checked){
         var el = $('.packageTableSelectAll').get(0);
         // If "Select all" control is checked and has 'indeterminate' property
         if(el && el.checked && ('indeterminate' in el)){
            // Set visual state of "Select all" control 
            // as 'indeterminate'
            el.indeterminate = true;
         }
      }
      
   });
   // Handle click on "Select all" control
   $('.userTableSelectAll').on('click', function(){
      // Get all rows with search applied
      var rows = $("#userTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }}).rows({ 'search': 'applied' }).nodes();
      // Check/uncheck checkboxes for all rows in the table
      $('input[type="checkbox"]', rows).prop('checked', this.checked);
      
   });

   // Handle click on checkbox to set state of "Select all" control
   $('#userTable tbody').on('change', 'input[type="checkbox"]', function(){
      // If checkbox is not checked
      if(!this.checked){
         var el = $('.userTableSelectAll').get(0);
         // If "Select all" control is checked and has 'indeterminate' property
         if(el && el.checked && ('indeterminate' in el)){
            // Set visual state of "Select all" control 
            // as 'indeterminate'
            el.indeterminate = true;
         }
      }
      
   });
   // Handle click on "Select all" control
   $('.dbConTableSelectAll').on('click', function(){
      // Get all rows with search applied
      var rows = $("#dbConTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }}).rows({ 'search': 'applied' }).nodes();
      // Check/uncheck checkboxes for all rows in the table
      $('input[type="checkbox"]', rows).prop('checked', this.checked);
      
   });

   // Handle click on checkbox to set state of "Select all" control
   $('#dbConTable tbody').on('change', 'input[type="checkbox"]', function(){
      // If checkbox is not checked
      if(!this.checked){
         var el = $('.dbConTableSelectAll').get(0);
         // If "Select all" control is checked and has 'indeterminate' property
         if(el && el.checked && ('indeterminate' in el)){
            // Set visual state of "Select all" control 
            // as 'indeterminate'
            el.indeterminate = true;
         }
      }
      
   });
   // Handle click on "Select all" control
   $('.wsConTableSelectAll').on('click', function(){
      // Get all rows with search applied
      var rows = $("#wsConTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }}).rows({ 'search': 'applied' }).nodes();
      // Check/uncheck checkboxes for all rows in the table
      $('input[type="checkbox"]', rows).prop('checked', this.checked);
      
   });

   // Handle click on checkbox to set state of "Select all" control
   $('#wsConTable tbody').on('change', 'input[type="checkbox"]', function(){
      // If checkbox is not checked
      if(!this.checked){
         var el = $('.wsConTableSelectAll').get(0);
         // If "Select all" control is checked and has 'indeterminate' property
         if(el && el.checked && ('indeterminate' in el)){
            // Set visual state of "Select all" control 
            // as 'indeterminate'
            el.indeterminate = true;
         }
      }
      
   });
   // Handle click on "Select all" control
   $('.specificDlJarsTableSelectAll').on('click', function(){
      // Get all rows with search applied
      var rows = $("#specificDlJarsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }}).rows({ 'search': 'applied' }).nodes();
      // Check/uncheck checkboxes for all rows in the table
      $('input[type="checkbox"]', rows).prop('checked', this.checked);
      
   });

   // Handle click on checkbox to set state of "Select all" control
   $('#specificDlJarsTable tbody').on('change', 'input[type="checkbox"]', function(){
      // If checkbox is not checked
      if(!this.checked){
         var el = $('.specificDlJarsTableSelectAll').get(0);
         // If "Select all" control is checked and has 'indeterminate' property
         if(el && el.checked && ('indeterminate' in el)){
            // Set visual state of "Select all" control 
            // as 'indeterminate'
            el.indeterminate = true;
         }
      }
      
   });
   
// Handle click on "Select all" control
   $('.specificIlJarsTableSelectAll').on('click', function(){
      // Get all rows with search applied
      var rows = $("#specificIlJarsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }}).rows({ 'search': 'applied' }).nodes();
      // Check/uncheck checkboxes for all rows in the table
      $('input[type="checkbox"]', rows).prop('checked', this.checked);
      
   });

   // Handle click on checkbox to set state of "Select all" control
   $('#specificIlJarsTable tbody').on('change', 'input[type="checkbox"]', function(){
      // If checkbox is not checked
      if(!this.checked){
         var el = $('.specificIlJarsTableSelectAll').get(0);
         // If "Select all" control is checked and has 'indeterminate' property
         if(el && el.checked && ('indeterminate' in el)){
            // Set visual state of "Select all" control 
            // as 'indeterminate'
            el.indeterminate = true;
         }
      }
      
   });
   
// Handle click on "Select all" control
   $('.specificVerticalTableSelectAll').on('click', function(){
      // Get all rows with search applied
      var rows = $("#specificVerticalTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }}).rows({ 'search': 'applied' }).nodes();
      // Check/uncheck checkboxes for all rows in the table
      $('input[type="checkbox"]', rows).prop('checked', this.checked);
      
   });

   // Handle click on checkbox to set state of "Select all" control
   $('#specificVerticalTable tbody').on('change', 'input[type="checkbox"]', function(){
      // If checkbox is not checked
      if(!this.checked){
         var el = $('.specificVerticalTableSelectAll').get(0);
         // If "Select all" control is checked and has 'indeterminate' property
         if(el && el.checked && ('indeterminate' in el)){
            // Set visual state of "Select all" control 
            // as 'indeterminate'
            el.indeterminate = true;
         }
      }
      
   });
   
// Handle click on "Select all" control
   $('.specificTableScriptsTableSelectAll').on('click', function(){
      // Get all rows with search applied
      var rows = $("#specificTableScriptsTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }}).rows({ 'search': 'applied' }).nodes();
      // Check/uncheck checkboxes for all rows in the table
      $('input[type="checkbox"]', rows).prop('checked', this.checked);
      
   });

   // Handle click on checkbox to set state of "Select all" control
   $('#specificTableScriptsTable tbody').on('change', 'input[type="checkbox"]', function(){
      // If checkbox is not checked
      if(!this.checked){
         var el = $('.specificTableScriptsTableSelectAll').get(0);
         // If "Select all" control is checked and has 'indeterminate' property
         if(el && el.checked && ('indeterminate' in el)){
            // Set visual state of "Select all" control 
            // as 'indeterminate'
            el.indeterminate = true;
         }
      }
      
   });
   
		$(document).on('click', '#migrateUsers', function(){
		 
	       var userID = $("#userId").val();
	       var destinationServerId = $("#destinationServer option:selected").val();
	       var destinationClientId = $("#destinationClient option:selected").val();
	       var sourceClientId =  $("#sourceClient option:selected").val();
	       var sourceServerId = $("#sourceServer option:selected").val();
	        
			var sourceUserIds = '';
			$("#userTable").find(".check:checked").each(function(){
				sourceUserIds += $(this).data("sourceuserid") + ",";
			});
	       
			 common.clearValidations(['#selectOneUserAlert','#destinationServer','#destinationClient','#sourceServer','#sourceClient']);
			
			   var validStatus = true;
			  if(sourceClientId == "select"){
			   common.showcustommsg("#sourceClient","Please choose source client","#sourceClient");
			   validStatus =false;
			  }
		  
		      if(sourceServerId == "select" || sourceServerId == ""){
			   common.showcustommsg("#sourceServer","Please choose source server","#sourceServer");
			   validStatus =false;
			  } 
			  if(destinationClientId == "select" || destinationClientId == '' ){
				   common.showcustommsg("#destinationClient","Please choose destination client","#destinationClient");
				   validStatus =false;
				  }
			  if(destinationServerId == "select" || destinationServerId == ""){
				   common.showcustommsg("#destinationServer","Please choose destination server","#destinationServer");
				   validStatus =false;
				  }
				var selCols = $("#userTable").find("input.check:checked");
				if (selCols.length === 0) {
					 common.showcustommsg("#selectOneUserAlert","Please choose atleast one package","#selectOneUserAlert");
					 validStatus =false;
				}
				 
			  if(!validStatus){
				 	return validStatus;
			   }
		 
		  var selectData={
				  clientId : sourceClientId,
				  serverId : sourceServerId,
				  destinationServerId:destinationServerId,
				  destinationClientId :destinationClientId,
				  sourceUserIds:sourceUserIds.substring(0, sourceUserIds.length-1)
				  
		   };
		 
	     var headers = {};
	     var token = $("meta[name='_csrf']").attr("content");
		 var header = $("meta[name='_csrf_header']").attr("content");
		 headers[header] = token;
	     showAjaxLoader(true);
	     var migrateUserFromSourceToDestination_url = "/app_Admin/user/"+userID+"/etlAdmin/migrateUserFromSourceToDestination";
	     var myAjax = common.postAjaxCall(migrateUserFromSourceToDestination_url,'POST', selectData,headers);
	     myAjax.done(function(result) {		 
	     showAjaxLoader(false);
	    	  if(result != null){ 
	    		  if(result.hasMessages){
	    			  if(result.messages[0].code == "ERROR") { 
	    				  common.showErrorAlert(result.messages[0].text)
		    			  return false;
	    			  } else if(result.messages[0].code == "SUCCESS") { 
	    				  $('#userTableMigrationAlert').empty().text(result.messages[0].text).show();
	    				  setTimeout(function() { $("#userTableMigrationAlert").hide().empty(); }, 10000);
	    				  $('#sourceClient').trigger('change');
	    			  }
	    			  
	    		  }
	    	  }		    	   
	    }); 
   });
		
		$(document).on('click', '#migratePackages', function(){
			 
		       var userID = $("#userId").val();
		       var destinationServerId = $("#destinationServer option:selected").val();
		       var destinationClientId = $("#destinationClient option:selected").val();
		       var sourceClientId =  $("#sourceClient option:selected").val();
		       var sourceServerId = $("#sourceServer option:selected").val();
		        
				var packageIds = '';
				$("#packageTable").find(".check:checked").each(function(){
					packageIds += $(this).data("packageid") + ",";
				});
				 
		       
				var destinationUserIds = '';
				 var userIds = $("#destinationUserList").multipleSelect("getSelects");
				jQuery.each(userIds, function(i, val) {
					destinationUserIds += val.trim() + ",";
				}); 
			 
	           common.clearValidations(['#selectOnePackageAlert','#destinationServer','#destinationClient','#sourceServer','#sourceClient','#destinationUserList']);
			
			  var validStatus = true;
			  if(sourceClientId == "select"){
			   common.showcustommsg("#sourceClient","Please choose source client","#sourceClient");
			   validStatus =false;
			  }
		  
		      if(sourceServerId == "select" || sourceServerId == ""){
			   common.showcustommsg("#sourceServer","Please choose source server","#sourceServer");
			   validStatus =false;
			  } 
			  if(destinationClientId == "select" || destinationClientId == '' ){
				   common.showcustommsg("#destinationClient","Please choose destination client","#destinationClient");
				   validStatus =false;
				  }
			  if(destinationServerId == "select" || destinationServerId == ""){
				   common.showcustommsg("#destinationServer","Please choose destination server","#destinationServer");
				   validStatus =false;
				  }
				var selCols = $("#packageTable").find("input.check:checked");
				if (selCols.length === 0) {
					 common.showcustommsg("#selectOnePackageAlert","Please choose atleast one package","#selectOnePackageAlert");
					 validStatus =false;
				}
				 if(userIds.length == 0){
					 common.showcustommsg("#destinationUserList","Please choose atleast one user.","#destinationUserList");
					 validStatus =false;
				} 
			  if(!validStatus){
				 	return validStatus;
			   }
			 
			  var selectData={
					  clientId : sourceClientId,
					  serverId : sourceServerId,
					  destinationServerId:destinationServerId,
					  destinationClientId :destinationClientId,
					  packageIds:packageIds.substring(0, packageIds.length-1),
					  destinationUserIds : destinationUserIds.substring(0, destinationUserIds.length-1),
					  
			   };
			  
			  
		     var headers = {};
		     var token = $("meta[name='_csrf']").attr("content");
			 var header = $("meta[name='_csrf_header']").attr("content");
			 headers[header] = token;
		     showAjaxLoader(true);
		     var migrateUserFromSourceToDestination_url = "/app_Admin/user/"+userID+"/etlAdmin/migratePackagesFromSourceToDestination";
		     var myAjax = common.postAjaxCall(migrateUserFromSourceToDestination_url,'POST', selectData,headers);
		     myAjax.done(function(result) {		 
		     showAjaxLoader(false);
		    	  if(result != null){ 
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") { 
		    				  common.showErrorAlert(result.messages[0].text)
			    			  return false;
		    			  } else if(result.messages[0].code == "SUCCESS") { 
		    				 // setTimeout(function() { $("#packageTableMigrationAlert").hide().empty(); }, 10000);
		    				  var  packageMigrationStatusTable = $("#packageMigrationStatusTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
		    				  packageMigrationStatusTable.clear();
	                           $.each(result.object, function(key,val) {  
	                        	   var row = [];	
	                        	   row.push(key);
	                        	   row.push(val);
	                        	   packageMigrationStatusTable.row.add(row);
	                           });
	                           packageMigrationStatusTable.draw(true); 
	                           $('#packageTableMigrationAlert').show();
		    				  $('#sourceClient').trigger('change');
		    			  }
		    			  
		    		  }
		    	  }		    	   
		    }); 
	   });
		
		$(document).on('click', '#migrateDbConnections', function(){
			 
		       var userID = $("#userId").val();
		       var destinationServerId = $("#destinationServer option:selected").val();
		       var destinationClientId = $("#destinationClient option:selected").val();
		       var sourceClientId =  $("#sourceClient option:selected").val();
		       var sourceServerId = $("#sourceServer option:selected").val();
		        
				var sourceDbConIds = '';
				$("#dbConTable").find(".check:checked").each(function(){
					sourceDbConIds += $(this).data("sourcedbconid") + ",";
				});
		       
				 common.clearValidations(['#selectOneDbConAlert','#destinationServer','#destinationClient','#sourceServer','#sourceClient']);
				
				   var validStatus = true;
				  if(sourceClientId == "select"){
				   common.showcustommsg("#sourceClient","Please choose source client","#sourceClient");
				   validStatus =false;
				  }
			  
			      if(sourceServerId == "select" || sourceServerId == ""){
				   common.showcustommsg("#sourceServer","Please choose source server","#sourceServer");
				   validStatus =false;
				  } 
				  if(destinationClientId == "select" || destinationClientId == '' ){
					   common.showcustommsg("#destinationClient","Please choose destination client","#destinationClient");
					   validStatus =false;
					  }
				  if(destinationServerId == "select" || destinationServerId == ""){
					   common.showcustommsg("#destinationServer","Please choose destination server","#destinationServer");
					   validStatus =false;
					  }
					var selCols = $("#dbConTable").find("input.check:checked");
					if (selCols.length === 0) {
						 common.showcustommsg("#selectOneDbConAlert","Please choose atleast one Db Connection.","#selectOneDbConAlert");
						 validStatus =false;
					}
					 
				  if(!validStatus){
					 	return validStatus;
				   }
			 
			  var selectData={
					  clientId : sourceClientId,
					  serverId : sourceServerId,
					  destinationServerId:destinationServerId,
					  destinationClientId :destinationClientId,
					  sourceDbConIds:sourceDbConIds.substring(0, sourceDbConIds.length-1)
					  
			   };
			 
		     var headers = {};
		     var token = $("meta[name='_csrf']").attr("content");
			 var header = $("meta[name='_csrf_header']").attr("content");
			 headers[header] = token;
		     showAjaxLoader(true);
		     var migrateDbConFromSourceToDestination_url = "/app_Admin/user/"+userID+"/etlAdmin/migrateDbConFromSourceToDestination";
		     var myAjax = common.postAjaxCall(migrateDbConFromSourceToDestination_url,'POST', selectData,headers);
		     myAjax.done(function(result) {		 
		     showAjaxLoader(false);
		    	  if(result != null){ 
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") { 
		    				  common.showErrorAlert(result.messages[0].text)
			    			  return false;
		    			  } else if(result.messages[0].code == "SUCCESS") { 
		    				  $('#dbConTableMigrationAlert').empty().text(result.messages[0].text).show();
		    				  setTimeout(function() { $("#dbConTableMigrationAlert").hide().empty(); }, 10000);
		    				  $('#sourceClient').trigger('change');
		    			  }
		    			  
		    		  }
		    	  }		    	   
		    }); 
	   });
		$(document).on('click', '#migrateWsConnections', function(){
			 
		       var userID = $("#userId").val();
		       var destinationServerId = $("#destinationServer option:selected").val();
		       var destinationClientId = $("#destinationClient option:selected").val();
		       var sourceClientId =  $("#sourceClient option:selected").val();
		       var sourceServerId = $("#sourceServer option:selected").val();
		        
				var sourceWsConIds = '';
				$("#wsConTable").find(".check:checked").each(function(){
					sourceWsConIds += $(this).data("sourcewsconid") + ",";
				});
		       
				 common.clearValidations(['#selectOneWsConAlert','#destinationServer','#destinationClient','#sourceServer','#sourceClient']);
				
				   var validStatus = true;
				  if(sourceClientId == "select"){
				   common.showcustommsg("#sourceClient","Please choose source client","#sourceClient");
				   validStatus =false;
				  }
			  
			      if(sourceServerId == "select" || sourceServerId == ""){
				   common.showcustommsg("#sourceServer","Please choose source server","#sourceServer");
				   validStatus =false;
				  } 
				  if(destinationClientId == "select" || destinationClientId == '' ){
					   common.showcustommsg("#destinationClient","Please choose destination client","#destinationClient");
					   validStatus =false;
					  }
				  if(destinationServerId == "select" || destinationServerId == ""){
					   common.showcustommsg("#destinationServer","Please choose destination server","#destinationServer");
					   validStatus =false;
					  }
					var selCols = $("#wsConTable").find("input.check:checked");
					if (selCols.length === 0) {
						 common.showcustommsg("#selectOneWsConAlert","Please choose atleast one Ws Connection.","#selectOneWsConAlert");
						 validStatus =false;
					}
					 
				  if(!validStatus){
					 	return validStatus;
				   }
			 
			  var selectData={
					  clientId : sourceClientId,
					  serverId : sourceServerId,
					  destinationServerId:destinationServerId,
					  destinationClientId :destinationClientId,
					  sourceWsConIds:sourceWsConIds.substring(0, sourceWsConIds.length-1)
					  
			   };
			 
		     var headers = {};
		     var token = $("meta[name='_csrf']").attr("content");
			 var header = $("meta[name='_csrf_header']").attr("content");
			 headers[header] = token;
		     showAjaxLoader(true);
		     var migrateDbConFromSourceToDestination_url = "/app_Admin/user/"+userID+"/etlAdmin/migrateWsConFromSourceToDestination";
		     var myAjax = common.postAjaxCall(migrateDbConFromSourceToDestination_url,'POST', selectData,headers);
		     myAjax.done(function(result) {		 
		     showAjaxLoader(false);
		    	  if(result != null){ 
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") { 
		    				  common.showErrorAlert(result.messages[0].text)
			    			  return false;
		    			  } else if(result.messages[0].code == "SUCCESS") { 
		    				  $('#wsConTableMigrationAlert').empty().text(result.messages[0].text).show();
		    				  setTimeout(function() { $("#wsConTableMigrationAlert").hide().empty(); }, 10000);
		    				  $('#sourceClient').trigger('change');
		    			  }
		    			  
		    		  }
		    	  }		    	   
		    }); 
	   });
}