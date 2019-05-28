var headers = {};
var tableScripts = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
	},
	getTableScripts : function(){
		    var userID = $("#userID").val();
		    var url_getTableScripts = "/app_Admin/user/"+userID+"/etlAdmin/getTableScripts";
		    var myAjax = common.postAjaxCall(url_getTableScripts,'GET','',headers);
		    myAjax.done(function(result) {
		    	  if(result != null){
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") { 
		    				  common.showErrorAlert(result.messages[0].text);
		    				  return false;
		    			  } else if(result.messages[0].code == "SUCCESS") { 
		    				  $("#tableScriptsTableBody").empty();
		    				  var tableScriptsTableBody = "";
		    				  var list=result.object;
		    				  if(list  == '' || list == null){
		    					  $("#tableScriptsTable").empty();
		    					  $("#tableScriptsTable").append("No data Available.");
		    				  }else{
		    					  $.each( list, function( key, value ) {
				    				     tableScriptsTableBody += '<tr id="tableScriptsRow" > <td id="tableScriptsId">'+value.id+'</td>'+
				    					                            '<td id="tableScriptsPriority">'+value.priority+'</td> '+
				    					                            '<td id="tableScriptsName">'+value.scriptName+'</td>'+ 
				    					                            '<td style="display:none"><input type="hidden" id="viewSqlTableScripts"  data-sqlScriptName="'+value.scriptName+'"  data-sqlScript="'+value.sqlScript+'"></td>'+ 
					   		                                        '<td><input id="viewSqlScript" value="View Sql Script" class="btn btn-primary btn-sm"></td>' +
					   		                                        '<td><a href="#" class="btn btn-primary btn-xs editTableScript" ><span class="glyphicon glyphicon-edit"></span></a> </td><td>'; 
					   		                                   if(value.is_Active == true){
					   		               tableScriptsTableBody += '<label> <input type="radio" class="param-radio"   value="Yes" name="isActive'+value.id+'"  checked="checked">'+
					   										        '<span>Yes</span>  </label>   <label> <input type="radio" class="param-radio" value="No" name="isActive'+value.id+'">'+
					   										        '<span>No</span> </label>';
					   		                                   }
					   		                                if(value.is_Active == false){
					   		               tableScriptsTableBody += ' <label> <input type="radio" class="param-radio" value="Yes" name="isActive'+value.id+'"> <span>Yes</span>'+
					   								                  '</label>  <label> <input type="radio" class="param-radio" value="No"name="isActive'+value.id+'" checked="checked"><span>No</span> </label>';
					   		                                }
					   								        
					   		               tableScriptsTableBody +=	 '</td></tr>' ;
				    				     });
				    				  $("#tableScriptsTableBody").append(tableScriptsTableBody);
		    				  }
		    				
			    			 }
		    		    }
		    		 }
		    });
		
	},
}
if($('.tableScripts-page').length){
	
	tableScripts.initialPage();
	$("#tableScriptsTable").DataTable({
		"order": [[ 0, "desc" ]],"language": {
            "url": selectedLocalePath
        }
	});
 
	$(document).on('click', '#addTableScript', function(){
		
		common.clearValidations(['input#scriptNameFromForm','#sqlScriptFromForm','select#priorityFromForm','.isActiveYesNo']);
		var userID = $("#userID").val();
		var scriptName =$("input#scriptNameFromForm").val().trim();
		var sqlScript =  $("textarea#sqlScriptFromForm").val().trim();
		var tableScriptsId =  $("input#tableScriptId").val().trim();
		var tableScriptsPriority =   $("select#priorityFromForm option:selected").val();
		var isActive =  $("input[name='isActiveYesNo']:checked").val();
		var isActiveChecked = false;
		
		if(isActive == 'Yes'){
			isActiveChecked = true;
		}else{
			isActiveChecked = false;
		}
		
		var is_Update = true;
		
		  var validStatus = true;
		
		  if(scriptName == ""){
			   common.showcustommsg("input#scriptNameFromForm","Please choose Script Name","input#scriptNameFromForm");
			   validStatus =false;
			  }
		  if(sqlScript == ""){
			   common.showcustommsg("textarea#sqlScriptFromForm","Please choose Sql Script","textarea#sqlScriptFromForm");
			   validStatus =false;
			  }
		  if(tableScriptsPriority == "" || isNaN(tableScriptsPriority) || tableScriptsPriority == 0){
			   common.showcustommsg("select#priorityFromForm","Please choose priority","select#priorityFromForm");
			   validStatus =false;
			  }
		  
		  var isActive =  $("input[name='isActiveYesNo']:checked").is(":checked");
		  
		  if(!isActive){
			  common.showcustommsg(".isActiveYesNo","Please choose is active",".isActiveYesNo");
			   validStatus =false;
		  }
		  if(!validStatus){
			 	return validStatus;
		   }
		  
		  var addTableScript = $(this).text().trim();
		  
		  if(addTableScript == 'Add'){
			  is_Update = false;
			  
		  }
		  var selectData = {
				  scriptName:scriptName,
				  sqlScript:sqlScript,
				  is_Update : is_Update,
				  id : tableScriptsId,
				  priority : tableScriptsPriority,
				  is_Active : isActiveChecked
		  }
		  
		 showAjaxLoader(true);
		 var url_tableScripts = "/app_Admin/user/"+userID+"/etlAdmin/addOrUpdateTableScripts";
		   var myAjax = common.postAjaxCall(url_tableScripts,'POST',selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    	  if(result != null){
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") { 
		    				  common.showErrorAlert(result.messages[0].text);
		    				  return false;
		    			  } else if(result.messages[0].code == "SUCCESS") { 
		    				  common.showSuccessAlert(result.messages[0].text);
		    				 $("input#scriptNameFromForm").val("");
		    				 $("textarea#sqlScriptFromForm").val("");
		    				 $("select#priorityFromForm").val("0");
		    				 $("input#tableScriptId").val("");
		    				 $('#addTableScript').attr("disabled", false);
		    				 $('.updateTableScript').attr("disabled", true);
		    				 $('#isActiveYes').attr("checked", false);
		    				 $('#isActiveNo').attr("checked", false);
		    				  tableScripts.getTableScripts();
		    				 
			    			 }
		    		    }
		    		 }
		    });
	
	});
	
	$(document).on('click', '.editTableScript', function(){
		    
		     $('#addTableScript').attr("disabled", true);
		     $('.updateTableScript').attr("disabled", false);
        	 var tableScriptsId = $(this).parents("#tableScriptsRow").find("#tableScriptsId").text().trim();
             var tableScriptsName =  $(this).parents("#tableScriptsRow").find("#tableScriptsName").text().trim();
		     var sqlScript = $(this).parents("#tableScriptsRow").find("#viewSqlTableScripts").attr("data-sqlScript").trim();
		     var tableScriptsId = $(this).parents("#tableScriptsRow").find("#tableScriptsId").text().trim();
		     var tableScriptsPriority =  $(this).parents("#tableScriptsRow").find("#tableScriptsPriority").text().trim();
		     var isActive =  $(this).parents("#tableScriptsRow").find("input[type='radio']:checked").val();
		     
		     $("input#scriptNameFromForm").val(tableScriptsName);
			 $("textarea#sqlScriptFromForm").val(sqlScript);
			 $("input#tableScriptId").val(tableScriptsId) ;
			 $("select#priorityFromForm").val(tableScriptsPriority) ;
			 
			 var isActiveYes =   $("input#isActiveYes").val();
			 var isActiveNo =  $("input#isActiveNo").val();
			 
			  if(isActive == isActiveYes ){
				  $("input#isActiveYes").prop("checked", true);
				  $("input#isActiveNo").prop("checked", false);
			  } 
			  if(isActive == isActiveNo){
				  $("input#isActiveYes").prop("checked", false);
				  $("input#isActiveNo").prop("checked", true);
			  }
			 
	});
	$(document).on('click', '#resetTableScript', function(){
		common.clearValidations(['input#scriptNameFromForm','#sqlScriptFromForm','select#priorityFromForm','.isActiveYesNo']);
		  $('#addTableScript').attr("disabled", false);
		  $('.updateTableScript').attr("disabled", true);
	      $("input#scriptNameFromForm").val("");
		  $("textarea#sqlScriptFromForm").val("");
		  $("input#tableScriptId").val("") ;
		  $("select#priorityFromForm").val("0") ;
		  $('#isActiveYes').attr("checked", false);
		  $('#isActiveNo').attr("checked", false);
		  
	});
	
	$(document).on('click', '#viewSqlScript', function(){
		
		 $("#viewsqlScriptHeader").text("");
		 $("#viewsqlScripts").val("");
		 var tableScriptsName = $(this).parents("#tableScriptsRow").find("#viewSqlTableScripts").attr("data-sqlScriptName").trim();
		 $("#viewsqlScriptHeader").text(tableScriptsName);
		 var sqlScript = $(this).parents("#tableScriptsRow").find("#viewSqlTableScripts").attr("data-sqlScript").trim();
		 $("#viewsqlScripts").val(sqlScript);
		 
		if(sqlScript === "" || sqlScript === null){
			$("#viewsqlScripts").val("No Query Found.");
		}
		$("#viewsqlScriptPopUp").modal('show');
	});
	
}