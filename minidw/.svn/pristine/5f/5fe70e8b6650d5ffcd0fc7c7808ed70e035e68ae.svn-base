var headers = {};
var  s3BucketInfo = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			$("#s3InfoTbl").dataTable ({  "order": [[ 0, "desc" ]]  });
		},
		s3Validation : function(){
			var bucketName = $("#bucketName").val();
			 var secretKey = $("#secretKey").val();
			 var accessKey = $("#accessKey").val();
			 var isActive = $("input[name='isActive']").is(":checked");
			 common.clearValidations(["#bucketName","#secretKey","#accessKey","#active"]);
			 var validStatus = true;
			if(bucketName == ""){
				 common.showcustommsg("#bucketName", "Please bucket name","#bucketName");
					validStatus = false; 
			 }else if(!/^[a-zA-Z0-9_-]*$/.test(bucketName.trim())){
	   	    	 common.showcustommsg("#bucketName", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters'],"#bucketName");
	   	    	 validStatus=false;
	   	    }
			 if(secretKey == ""){
					common.showcustommsg("#secretKey","Please enter secret key","#secretKey");
					validStatus = false; 
			 }else if(!/^[a-zA-Z0-9+/ /_/-/(/)]*$/.test(secretKey.trim())){
	   	    	 common.showcustommsg("#secretKey", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters'],"#secretKey");
	   	    	 validStatus=false;
	   	    }
			 if(accessKey == ""){
					common.showcustommsg("#accessKey", "Please enter access key","#accessKey");
					validStatus = false; 
			 }else if(!/^[a-zA-Z0-9_]*$/.test(accessKey.trim())){
	   	    	 common.showcustommsg("#accessKey", globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters'],"#accessKey");
	   	    	 validStatus=false;
	   	    }
			 if(!isActive){
					common.showcustommsg("#active", "Please choose status","#active");
					validStatus = false; 
			 }
			 return validStatus;
		},
		
		loads3InfoList: function () {
			var userID = $("#userID").val();
			var s3InfoTbl$ = $("#s3InfoTbl tbody").empty();
			showAjaxLoader(true);
			var url_getS3InfoList = "/app_Admin/user/"+userID+"/etlAdmin/getS3InfoList";
			   var myAjax = common.postAjaxCallObject(url_getS3InfoList,'GET', null ,headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;	
		    				  }
			    			  if(result.messages[0].code=="SUCCESS"){
			    				  var list = result.object;
			    				  $.each(list,function(i,data){
			    					  var tr$ = $("<tr>");
			    					  tr$.append($("<td>").text(data.id));
			    					  tr$.append($("<td>").text(data.bucketName));
			    					  tr$.append($("<td>").text(data.accessKey));
			    					  tr$.append($("<td>").text(data.secretKey));
			    					  tr$.append($("<td>").text(data.isActive == true ? globalMessage['anvizent.package.button.yes'] : globalMessage['anvizent.package.button.no']));
			    					  tr$.append($("<td>").append($("<button>",{type:'button',class:'btn btn-primary btn-sm editDetails',value:data.id}).append($("<span>",{class:'glyphicon glyphicon-edit',title:'Edit'}))));
			    					  s3InfoTbl$.append(tr$);
			    				  })
			    					
			    			  }
			    		  }
					}
				});
		},
		
}

if($('.s3BucketInfo-page').length){
	s3BucketInfo.initialPage();
	common.clearValidations(["#bucketName","#secretKey","#accessKey","#active"]);
	$("#s3InfoTable").show();
	s3BucketInfo.loads3InfoList();
	
	$(document).on("click",".addS3Info",function(){
		$("#bucketName,#accessKey,#secretKey").val('');
		$('input[name="isActive"]').prop("checked",false);
		$(".s3Info").show();
		$("#s3InfoTable").hide();
	});
	$(document).on("click",".editDetails",function(){
		$(".s3Info").show();
		$("#s3InfoTable").hide();
		var userID = $("#userID").val();
		var id = $(this).val();
		$("#idValue").val(id);
		var selectData = {
				id : id,
		}
		 var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
 		showAjaxLoader(true);
		var url_getS3BucketInfoById = "/app_Admin/user/"+userID+"/etlAdmin/getS3BucketInfoById";
		   var myAjax = common.postAjaxCallObject(url_getS3BucketInfoById,'POST',selectData,headers);
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
		    				  console.log(result.object);
		    				  $("#propertiesInfo").show();
		    				  var bucketName = result.object.bucketName;
		    				  var secretKey = result.object.secretKey;
		    				  var AccessKey = result.object.accessKey;
		    				  var isActive = result.object.isActive
		    				  var message = result.messages[0].text;
		    				  $("#bucketName").val(bucketName);
		    				  $("#secretKey").val(secretKey);
		    				  $("#accessKey").val(AccessKey);
		    				  if(isActive == true){
		    					  $("#isActiveYes").prop("checked",true);  
		    				  }else{
		    					  $("#isActiveNo").prop("checked",true);
		    				  }
		    				  
		    			  }
		    			  
		    		  }
				}
			});
		
	})
	
	$(document).on("click","#back",function(){
		$(".s3Info").hide();
		$("#s3InfoTable").show();
	})
	
	$(document).on("click","#saveInfo",function(){
	 common.clearValidations(["#bucketName,#secretKey,#accessKey"])
	 var id = $("#id").val();
	 var bucketName = $("#bucketName").val();
	 var secretKey = $("#secretKey").val();
	 var accessKey = $("#accessKey").val();
	 var isActive= $("input[name='isActive']:checked").val();
	
	 var id = $("#idValue").val();	
	 var userID = $("#userID").val();
	 if (!id) {
		id = 0;
	 }
	 var status = s3BucketInfo.s3Validation();
	 if(!status){
		 return false;
	 }else{
		 var selectData = {
					id: id,
					bucketName : bucketName,
					secretKey : secretKey,
					accessKey : accessKey,
					isActive : isActive
			}
		 var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		 showAjaxLoader(true);
			var url_saveS3BucketInfo = "/app_Admin/user/"+userID+"/etlAdmin/saveS3BucketInfo";
			   var myAjax = common.postAjaxCallObject(url_saveS3BucketInfo,'POST',selectData,headers);
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
			    				  $(".s3Info").hide();
			    				  $("#s3InfoTable").show();
			    				  s3BucketInfo.loads3InfoList();
			    			  }
			    		  }
					}
				});
	 }
		
		
	})
	
}