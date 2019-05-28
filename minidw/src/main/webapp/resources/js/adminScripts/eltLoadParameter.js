var  eltLoadParamters = {
		initialPage : function() {
			$(".createLoadParamtersDiv").hide();
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);	
		},
		
		saveLoadParameters: function(){
			debugger
			var userId = $("#userID").val();
			var name = $("#name").val();
			var noOfExecutors = $(".noOfExecutors").val();
			var executorMemory = $(".executorMemory").val();
			var executorCores = $(".executorCores").val();
			var id = $("#selectedId").val();
			var executorMemoryType = 'G';
			
			var selectedData = {
					id : id,
					name : name,
					executorMemoryType : executorMemoryType,
					noOfExecutors : noOfExecutors,
					executorMemory : executorMemory,
					executorCores : executorCores
					
			}
			
			var url_saveEltMasterConfig = "/app_Admin/user/"+userId+"/eltconfig/saveEltLoadParameters";
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCall(url_saveEltMasterConfig,'POST',selectedData,common.getcsrfHeader());
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
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		
		
	
		
		displayLoadParamtersInfo: function(id) {
			debugger
			var userId = $("#userID").val();
			$("#selectedId").val(id);
			var url_getLoadParamtersInfo = "/app_Admin/user/"+userId+"/eltconfig/getLoadParamtersInfo/"+id;
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCallObject(url_getLoadParamtersInfo,'GET','',common.getcsrfHeader());
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
		    				eltLoadParamters.getLoadParamtersInfo(result);
		    			}		    			  		    			  	
			    	}
	 	    	});
			
			
		},
		
		deleteParamtersById: function(id) {
			debugger
			var userId = $("#userID").val();
			
			var url_deleteParamtersById= "/app_Admin/user/"+userId+"/eltconfig/deleteParamtersById/"+id;
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCallObject(url_deleteParamtersById,'DELETE','',common.getcsrfHeader());
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
		    				$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
		    				window.location.reload();
		    			}		    			  		    			  	
			    	}
	 	    	});
			
			
		},
		
		getLoadParamtersInfo : function(result){
			if(result != null){
				var resultInfo = result.object;
				var id =  resultInfo.id;
				var name = resultInfo.name;
				var noOfExecutors = resultInfo.noOfExecutors;
				var executorMemory = resultInfo.executorMemory;
				var executorCores = resultInfo.executorCores;
				
				
				$("#name").val(name);
				$(".noOfExecutors").val(noOfExecutors);
				$(".executorMemory").val(executorMemory)
				$(".executorCores").val(executorCores)
			}
		},
		
}
if($('.eltLoadParamters-page').length){
	debugger
	eltLoadParamters.initialPage();
	
	$('.addMasterInfoBtn').on("click",function(){
		$("#loadParametersTable").hide();
		$(".createLoadParamtersDiv").show();
	});
	
	$(document).on("click",".saveLoadParametersBtn",function(){
		debugger
		eltLoadParamters.saveLoadParameters();
	});
	$(document).on("click",".loadParametersId",function(){
		id = this.value;
		$('#loadParametersTable').hide();
		$('.createLoadParamtersDiv').show();
		eltLoadParamters.displayLoadParamtersInfo(id);
	})
	$("#loadParametersBack").on("click",function(){
		window.location.reload();
		$('#loadParametersTable').show();
		$('.createLoadParamtersDiv').hide();
	})
	
	$(document).on("click",".deleteById",function(){
		id = this.value;
		eltLoadParamters.deleteParamtersById(id);
	})
		
		
}