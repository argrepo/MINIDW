var headers = [];
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

var multiClientScriptExecution = {
		initialPage : function() {
			 $("#clientId").multipleSelect({
					filter : true,
					placeholder : 'Select Client Ids',
					enableCaseInsensitiveFiltering : true
				});
		},
		
		getClientList:function(){
			var userID = $("#userID").val();
			headers[header] = token;
			var url_getClientList = "/app_Admin/user/"+userID+"/etlAdmin/getMultiClientList";
			   var myAjax = common.postAjaxCall(url_getClientList,'GET', "" ,headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;	
		    				  }
			    			  if(result.messages[0].code=="SUCCESS"){
			    				  var clientList = result.object;
			    				  var select$ = $("#clientId");
			    				  $.each(clientList,function(key,value){
			    					  $("<option/>",{value:key,text:value}).appendTo(select$);
			    				  });
			    				  $("#clientId").multipleSelect('refresh');
			    			  }
			    		  }
					}
				});
		},
		
		executeInsertScripts : function(){
			debugger
			var userID = $("#userID").val();
			var clientIdList = $("#clientId").val();
			var tableNamesList = [];
			
			$(".tableNameCheckbox:checked").each(function(){
				var tableNames = $(this).val();
				tableNamesList.push(tableNames);
			});
			var truncateTbl = $("input[name='truncateTbl']:checked").val();
				var selectedData = {
						clientIds : clientIdList,
						tableNames : tableNamesList,
						truncateTbl : truncateTbl
				}
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				var url_executeInsertScripts = "/app_Admin/user/"+userID+"/etlAdmin/executeInsertScripts";
				showAjaxLoader(true); 				
		 		var myAjax = common.postAjaxCall(url_executeInsertScripts,'POST',selectedData,headers);
		 	    myAjax.done(function(result) {
			 	    	showAjaxLoader(false); 
			 	    	if(result.hasMessages){		    			  
			    			if(result.messages[0].code == "ERROR") {
								var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
								$(".message-class").empty().append(message).show();
								setTimeout(function() { $(".message-class").hide(); }, 5000);
			    				return false;
			    			} 
			    			if(result.messages[0].code == "SUCCESS") {
			    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
								$(".message-class").empty().append(message).show();
								setTimeout(function() { $(".message-class").hide(); }, 5000);
								multiClientScriptExecution.clearData();
			    			}		    			  		    			  	
				    	}
		 	    	});
				
		},
		clearData : function(){
			debugger
			$("#clientId").val([]);
			$("#clientId").multipleSelect('refresh');
			$("input[name='truncateTbl']").prop("checked",false);
			$("input[type='checkbox']").prop("checked",false);
		}
		
}

if($('.multiClientInsertScriptsExecution-page').length){
	debugger
	multiClientScriptExecution.initialPage();
	multiClientScriptExecution.getClientList();
	
	 $("#executeScripts").on("click",function(){
			debugger
			multiClientScriptExecution.executeInsertScripts();
			
	});
	 $(document).on("click",".selectAll",function(){
			if($(this).is(":checked"))
				$(this).parents("table").find("input[type='checkbox']").prop("checked",true);
			else
				$(this).parents("table").find("input[type='checkbox']").prop("checked",false);
			
	});
	 
	 $(document).on("click",".tableNameCheckbox",function(){
			if($(".tableNameCheckbox").length == $(".tableNameCheckbox:checked").length){
				$(this).parents("table").find(".selectAll").prop("checked",true);
			}else{
				$(this).parents("table").find(".selectAll").prop("checked",false);
			}
	});
	 
}