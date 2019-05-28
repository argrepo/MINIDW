var headers = {};
var viewCustomPackageSource = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
	},
}
if($('.viewCustomPackageSource-page').length){
	viewCustomPackageSource.initialPage();
	
	$(document).on('click', '#checkTablePreviewViewDetails', function() {
		 
		var userID = $("#userID").val(); 
		var connectionId = $("#show_connectionId").val();
		var typeOfCommand = $("#show_typeOfCommand").val(); 
		var packageId = $("#packageId").val();
		var query = typeOfCommand === "Query" ? $("#show_queryScript").val() : $("#show_procedureName").val();
		var previewSourceTitle = $(this).attr("data-previewSourcetitle"); 
		console.log(userID , connectionId,typeOfCommand);
		if( query != '') {
			var selectData = {
					packageId : packageId,
					iLConnection : {
						connectionId : connectionId
					},
					iLquery : query,
					typeOfCommand : typeOfCommand
			}

			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			showAjaxLoader(true);
			var url_checkQuerySyntax = "/app/user/"+userID+"/package/getTablePreview";
			 var myAjax = common.postAjaxCall(url_checkQuerySyntax,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null){
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") { 
			    				  common.showErrorAlert(result.messages[0].text)
					    		  return false;
			    			  }  
			    		  }
			    		  
			    		  $("#viewDeatilsTablePreviewPopUp").modal('show');
			    		  $("#viewDeatilsTablePreviewPopUpHeader").text(previewSourceTitle);
			    	  
			    	  var list = result.object;
			    	  console.log("column:"+list);
			    	  if(list != null && list.length > 0){
			    		  var tablePreview='';
				    	  $.each(list, function (index, row) {
				    		  
				    		  tablePreview+='<tr>';
				    		  $.each(row, function (index1, column) {
				    		      console.log("column:"+column);
				    			  tablePreview += (index == 0 ? '<th>'+column+'</th>' : '<td>'+column+'</td>');
				    		  });
				    		 tablePreview+='</tr>'; 
				    		});
				    	  $(".viewDeatilsTablePreview").empty();
				    	  $(".viewDeatilsTablePreview").append(tablePreview);
				    	  }
				    	  else{
				    		  $(".viewDeatilsTablePreview").empty();
				    		  $(".viewDeatilsTablePreview").append(globalMessage['anvizent.package.label.noRecordsAvailableInTable']);
				    	  } 
			    	  }
			    	  
			    });
			
		}  
	});
	
	$(document).on('click', '#checkFlatPreviewViewDetails', function() {
		var userID = $("#userID").val();
		var packageId = $("#packageId").val();
		var mappedId = $("#show_mappingId").val();
		var previewSourceTitle = $(this).attr("data-previewSourcetitle"); 
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
			showAjaxLoader(true);
			var url_checkQuerySyntax = "/app/user/"+userID+"/package/getFlatFilePreview/"+packageId+"/"+mappedId;
			 var myAjax = common.loadAjaxCall(url_checkQuerySyntax,'GET','',headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null){
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") {
									
			    				  $('.iLInputMessage').show().empty().append(result.messages[0].text);
					    		  setTimeout(function() { $(".iLInputMessage").hide().empty(); }, 10000);
				    				  return false;
				    			  }  
			    		  }
			    		  
			    		  $("#viewDeatilsFlatPreviewPopUp").modal('show');
			    		  $("#viewDeatilsFlatPreviewPopUpHeader").text(previewSourceTitle);
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
				    	  if(list != null && list.length == 1){
				    		  tablePreview +='<tr><td colspan="'+list[0].length+'">'+globalMessage['anvizent.package.label.noRecordsAvailable']+'</td></tr>';
				    	  }
				    	  $(".viewDeatilsFlatPreview").empty();
				    	  $(".viewDeatilsFlatPreview").append(tablePreview);
				    	 
				    	  }
				    	  else{
				    		  $(".viewDeatilsFlatPreview").empty();
				    		  $(".viewDeatilsFlatPreview").append(globalMessage['anvizent.package.label.noRecordsAvailable']);
				    	  } 
			    	  }
			    	  
			    });
	});
$(document).on('click', '.editIlSourceDetails', function() {
		/*if ($("#isMapped").val() == "true") {
			$("#deleteTargetTableAlert").modal('show');
		} else {
			enableEditFunctionality();
		}*/
		enableEditFunctionality();
	    
		 
});
$(document).on('click', '#confirmRedirectToQueryBuilder', function() {
	var packageId = $("#packageId").val();
	window.location = adt.appContextPath+"/adt/package/queryBuilder/"+packageId+"/0";
});

function enableEditFunctionality() {
	$("#checkQuerySyntax").show();
	$(".editIlSourceDetails").hide();
	$("#show_typeOfCommand").prop("disabled",false);
	var typeOfCommand = $(".typeOfCommand").val(); 
	if( typeOfCommand == 'Query'){
		$(".typeOfCommand").empty();
		//$("#show_queryScript").prop("disabled",false);
		$("#show_queryScript").removeAttr("readonly");
		$(".typeOfCommand").append('<option value="Query">'+globalMessage['anvizent.package.label.query']+'</option>');
		$(".typeOfCommand").append('<option value="Stored Procedure">'+globalMessage['anvizent.package.label.storedProcedure']+'</option>');
	}
	 if(typeOfCommand == 'Stored Procedure'){
		 //$("#show_procedureName").prop("disabled",false);
		 $("#show_procedureName").removeAttr("readonly");
		 $(".typeOfCommand").empty();
		 $(".typeOfCommand").append('<option value="Stored Procedure">'+globalMessage['anvizent.package.label.storedProcedure']+'</option>');
		 $(".typeOfCommand").append('<option value="Query">'+globalMessage['anvizent.package.label.query']+'</option>');
	}
}


$(document).on('change', '.typeOfCommand', function() {
	 var typeOfCommand = $(".typeOfCommand").val(); 
	 
	 if(typeOfCommand  == "Query"){
		 $("#show_queryScript").val("");
		// $("#show_queryScript").prop("disabled",false);
		 $("#show_queryScript").removeAttr("readonly");
		 $('#show_procedureName').hide();
		 $('#show_queryScript').show();
		 common.clearValidations(["#show_procedureName"]);
	 }else{
		 common.clearValidations(["#show_procedureName"]);
		 $("#show_procedureName").val("");
		 $("#show_procedureName").prop("disabled",false);
		 $("#show_procedureName").removeAttr("readonly");
		 $('#show_queryScript').hide();
		 $('#show_procedureName').show();
	 }
 
	 
}); 
$(document).on('click', '#checkQuerySyntax', function() {
	 
	 
	var userID = $("#userID").val(); 
	var connectionId =  $("#show_connectionId").val();
	 
	 var typeOfCommand = $(".typeOfCommand").val(); 
	var queryOrSp = '';
	 
	if(typeOfCommand  === "Query"){
		  common.clearValidations(["#show_queryScript"]);
		queryOrSp=$("#show_queryScript").val();
	}
	  if(typeOfCommand  === "Stored Procedure"){
		  common.clearValidations(["#show_procedureName"]);
		  queryOrSp=$("#show_procedureName").val();
	}
	  if( queryOrSp != '') {
		var selectData ={
				iLConnection : {
					connectionId : connectionId
				},
				iLquery : queryOrSp,
				typeOfCommand : typeOfCommand
		}
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		showAjaxLoader(true);
		var url_checkQuerySyntax = "/app/user/"+userID+"/package/checksQuerySyntax";
		 var myAjax = common.postAjaxCall(url_checkQuerySyntax,'POST', selectData,headers);
		    myAjax.done(function(result) {
		    	 showAjaxLoader(false);
		    	  if(result != null && result.hasMessages){
		    		  if(result.messages[0].code == "SUCCESS") {
		    			  $('.successIlMessage').show().empty().append(result.messages[0].text);
		    			  setTimeout(function() { $(".successIlMessage").hide().empty(); }, 10000);
			    		  $(".checkTablePreview").show();
			    		  $("#updateILConnectionMapping").show();
		    		  }else{
		    			  if(result.messages[0].text === null){
		    				  $('.iLInputMessage').show().empty().append("Invalid Query/Sp");
		    				  setTimeout(function() {  $(".iLInputMessage").hide().empty(); }, 10000);
				    		  $(".checkTablePreview").hide();
				    		  $("#updateILConnectionMapping").hide();
		    				  return false; 
		    			  }else{
						  $('.iLInputMessage').show().empty().append(result.messages[0].text);
			    		  setTimeout(function() {  $(".iLInputMessage").hide().empty(); }, 10000);
			    		  $(".checkTablePreview").hide();
			    		  $("#updateILConnectionMapping").hide();
			    		  return false;
		    			  }
		    		  }
		    		  
		    	  }else {
			    		var messages = [ {
			    			code : globalMessage['anvizent.message.error.code'],
							text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
						} ];
			    		common.displayMessages(messages);
			    	}
		    });
		
	} else   {
		
		if(typeOfCommand  === "Query"){
			 queryOrSpId=$(this).closest("div.panel").find(".iLquery").attr('id');
			 common.clearValidations(["#show_queryScript"]);
			 common.showcustommsg("#show_queryScript", globalMessage['anvizent.package.label.shouldNotBeEmpty'],"#show_queryScript");
		}
		  if(typeOfCommand  === "Stored Procedure"){
			  queryOrSpId=$(this).closest("div.panel").find(".iLSp").attr('id');
			  common.clearValidations(["#show_procedureName"]);
			  common.showcustommsg("#show_procedureName", globalMessage['anvizent.package.label.shouldNotBeEmpty'],"#show_procedureName");
		}
	 
		 
	}
});
$(document).on('keyup', '#show_queryScript', function(){
	$('#updateILConnectionMapping').hide();
	$('.checkTablePreview').hide();
});
$(document).on('keyup', '#show_procedureName', function(){
	$('#updateILConnectionMapping').hide();
	$('.checkTablePreview').hide();
});

$(document).on('click', '#updateILConnectionMapping', function(){
	 
	var userID = $("#userID").val(); 
	var connectionId =  $("#show_connectionId").val();
	 var typeOfCommand = $(".typeOfCommand").val(); 
	 var packageId = $("#packageId").val();
	 var connectionMappingId =  $(this).attr('data-mappingId');
	 var queryOrSp = '';
	 
		if(typeOfCommand  === "Query"){
			queryOrSp=$("#show_queryScript").val();;
		}
		  if(typeOfCommand  === "Stored Procedure"){
			  queryOrSp=$("#show_procedureName").val();
		}
	  var selectData={
			   iLConnection:{
				   connectionId : connectionId, 
			   },
			   connectionMappingId:connectionMappingId,
			   typeOfCommand : typeOfCommand,
			   iLquery : queryOrSp,
			   packageId : packageId
	   };
	  	showAjaxLoader(true);
		 var url_updateILConnectionMapping = "/app/user/"+userID+"/package/updateIlSource";
		   var myAjax = common.postAjaxCall(url_updateILConnectionMapping,'POST', selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    	 showAjaxLoader(false);
		    	  if(result != null){
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") {
				    		  $('.iLInputMessage').show().empty().append(result.messages[0].text);
				    		  setTimeout(function() {$(".iLInputMessage").hide().empty(); }, 10000);
			    			  return false;
			    			  } else if(result.messages[0].code == "SUCCESS") {
			    				  $(".editIlSourceDetails").show();
					    		  $('.successIlMessage').show().empty().append(result.messages[0].text);
					    		  setTimeout(function() {$(".successIlMessage").hide().empty(); }, 10000);
					    			if(typeOfCommand  === "Query"){
										 $(".typeOfCommand").prop("disabled",true);
										 $("#show_queryScript").attr("readonly","readonly");
										 $(".checkTablePreview").hide(); 
										 $("#checkQuerySyntax").hide(); 
										 $(".updateILConnectionMapping").hide();
									}
									  if(typeOfCommand  === "Stored Procedure"){
									      $("#show_procedureName").attr("readonly","readonly");
									      $(".typeOfCommand").prop("disabled",true);
									      $(".checkTablePreview").hide(); 
										  $("#checkQuerySyntax").hide(); 
										  $(".updateILConnectionMapping").hide();
									}
			    			  }  else if(result.messages[0].code == "PROCEED_FOR_MAPPING")  {
			    				  $("#changeColumnMappings").modal('show');
			    			  }
		    		  } 
		    	  }
		    });
	   
	   
	});

$("#confirmDeleteTargetTable").on("click",function(){
	var packageId = $("#packageId").val();
    var userID = $("#userID").val();
    var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	headers[header] = token;
    var url_saveMappingTableInfo = "/app/user/"+userID+"/package/deleteSameDatasetCustomTablesBYPackageId/"+packageId;
    var myAjax = common.postAjaxCall(url_saveMappingTableInfo,'POST','',headers);
    
    $("#deleteTargetTableAlert").modal('hide');
    showAjaxLoader(true);
    
	myAjax.done(function(result) {
		showAjaxLoader(false);
		if(result != null && result.hasMessages){
		if (result.messages[0].code === "SUCCESS") {
			enableEditFunctionality();
			$("#isMapped").val("false");
		}else {
			common.displayMessages(result.messages);
			//$("#popUpMessageForTableDelete").prop({"class":"alert alert-success"}).text(result.messages[0].text);
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


}