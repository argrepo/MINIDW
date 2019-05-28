var headers = [];
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

var  s3clientInfo = {
		initialPage : function() {
			
			
		},
		
		getClientList:function(){
			var userID = $("#userID").val();
			headers[header] = token;
			var url_getClientList = "/app_Admin/user/"+userID+"/etlAdmin/getClientList";
			   var myAjax = common.postAjaxCallObject(url_getClientList,'GET', "" ,headers);
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
			    				  var select$ = $("<select/>",{class:"clientId"});//.append($('<option>').val('0').text('Select Client'));
			    				  $.each(clientList,function(key,value){
			    					  $("<option/>",{value:key,text:value}).appendTo(select$);
			    				  });
			    				  select$.appendTo(".clientList");
			    				  $(".clientId").select2({               
			    		                allowClear: true,
			    		                theme: "classic"
			    					});
			    			  }
			    		  }
					}
				});
		},	
		
		getBucketList:function(){
			var userID = $("#userID").val();
			headers[header] = token;
			var url_getClientList = "/app_Admin/user/"+userID+"/etlAdmin/getBucketList";
			   var myAjax = common.postAjaxCallObject(url_getClientList,'GET', "" ,headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;	
		    				  }
			    			  if(result.messages[0].code=="SUCCESS"){
			    				  var bucketList = result.object;
			    				  var select$ = $("<select/>",{class:"bucket"});//.append($('<option>').val('0').text('Select Bucket Name'));
			    				  $.each(bucketList,function(key,value){
			    					  $("<option/>",{value:key,text:value}).appendTo(select$);
			    				  });
			    				  select$.appendTo(".bucketList");
			    				  $(".bucket").select2({               
			    		                allowClear: true,
			    		                theme: "classic"
			    					});
			    			  }
			    		  }
					}
				});
		},	
}

if($('.s3ClientMapping-page').length){
	s3clientInfo.initialPage();
	s3clientInfo.getClientList();
	s3clientInfo.getBucketList();
	$("#clientInfo").show();
	$(".saveBtn").hide();
	$("#bucketInfo").show();
	$(".saveBtn").show()
	
	$(document).on("click","#save",function(){
		common.clearValidations([".clientId,.bucket"])
		var clientId = $(".clientId option:selected").val();
		var id = $(".bucket option:selected").val();
		var userID = $("#userID").val();
		
		if(clientId == '0'){
			common.showcustommsg(".clientId", "Please choose Client", ".clientId");
			return false;
		}
		if(id == '0'){
			common.showcustommsg(".bucket", "Please choose bucket", ".bucket");
			return false;
		}
		var selectData = {
				clientId: clientId,
				id : id
		}
		
		showAjaxLoader(true);
		headers[header] = token;
		var url_saveClientMapping = "/app_Admin/user/"+userID+"/etlAdmin/saveClientMapping";
		   var myAjax = common.postAjaxCallObject(url_saveClientMapping,'POST',selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){						
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  var message = result.messages[0].text;
	    					  $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>").show();
		    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  var message = result.messages[0].text;
		    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>").show();
		    				  setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);
		    				
		    				  id		    			  }
		    		  }
				}
			});
	});
	
	
}