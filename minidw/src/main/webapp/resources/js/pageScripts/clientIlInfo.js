var clientIlinfo = {
		initialPage : function() {
		//$("#clientILInfo").dataTable();	
		//$("#addDiv").attr('disabled','disabled');
		$("#addDiv").prop('disabled',true);
		},
		getClientIlInfo : function(result){
			 var table =$("#clientILInfo").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
			 table.clear();
			 
			 if(result != null){				
				 for (var i = 0; i < result.object.length; i++) {
					 
					 var ilId = result.object[i].iL_id;
					 var ilName = result.object[i].iL_name;
					 var version = result.object[i].version;
					 var iLType = result.object[i].iLType;
					 var ilTableName = result.object[i].iL_table_name;
					 
					 var edit = "<button class='btn btn-primary btn-sm tablebuttons editdefaultQuery' data-ilId='"+ilId+"' title='"+globalMessage['anvizent.package.label.edit']+"'>"+
								"<i class='fa fa-pencil' aria-hidden='true'></i></button>";
					 
					 var row = [];
					 
					 row.push(i+1);
					 row.push(ilId);
					 row.push(ilName);
					 row.push(version);
					 row.push(iLType);
					 row.push(ilTableName);
					 row.push(edit);
					 table.row.add(row);
				}
				table.draw(true);
			}
		}
}
if($('.clientILInfo-page').length){
	clientIlinfo.initialPage();
	
	var userID = $("#userID").val();
	var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true);
	   var url_getClientILInfo = "/app/user/"+userID+"/package/clientILInfo";
	   var myAjax = common.loadAjaxCall(url_getClientILInfo,'GET','',headers);
		myAjax.done(function(result) {
			showAjaxLoader(false);
			if(result != null){
	    		  if(result.hasMessages){
    				  if(result.messages[0].code=="ERROR"){
    					  common.showErrorAlert(result.messages[0].text);
    					  var message = result.messages[0].text;		    					  
	    			  }
    				  else{			    				
    					  clientIlinfo.getClientIlInfo(result);
	    			  }
	    		  }
			}
	});
}

$('#addID').on('click', function(){
   $('#createIlinfoPopUp').modal("show");
    
 });
$('#saveid').on('click', function(){
	
	 var ilID= $("#ilID").val();
	var ilName= $("#ilNamee").val();
	var version= $("#versionn").val();
	var iLType= $("#ilTypee").val();
	var ilTableName= $("#ilTableNamee").val();
	
	var selectData = {
			ilName:ilName,
			version:version,
			iLType:iLType,
			ilTableName:ilTableName
			
	};
	
	   var url_getClientILInfoAfterSave = "/app/user/"+userID+"/package/clientILInfoAfterSave";
	   var myAjaxAfterSave = common.postAjaxCall(url_getClientILInfoAfterSave,'POST',selectData,headers);
	   myAjaxAfterSave.done(function(result) {
			showAjaxLoader(false);
			if(result != null){
	    		  if(result.hasMessages){
    				  if(result.messages[0].code=="ERROR"){
    					  common.showErrorAlert(result.messages[0].text);
    					  var message = result.messages[0].text;		    					  
	    			  }
    				  else{	
    					  
    					  var message = result.messages[0].code;
	    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>").show();
	    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);
	    				  $('#createIlinfoPopUp').modal("hide");
	    				 // clientIlinfo.getClientIlInfo(result);
	    				  
	    				  var url_getClientILInfo2 = "/app/user/"+userID+"/package/clientILInfo";
	    				   var myAjax = common.loadAjaxCall(url_getClientILInfo2,'GET','',headers);
	    					myAjax.done(function(result2) {
	    						showAjaxLoader(false);
	    						if(result2 != null){
	    				    		  if(result2.hasMessages){
	    			    				  if(result2.messages[0].code=="ERROR"){
	    			    					  common.showErrorAlert(result2.messages[0].text);
	    			    					  var message = result2.messages[0].text;		    					  
	    				    			  }
	    			    				  else{			    				
	    			    					  clientIlinfo.getClientIlInfo(result2);
	    				    			  }
	    				    		  }
	    						}
	    				});
	    				  
	    			  }
	    		  }
			}
	});
	   
	 });

