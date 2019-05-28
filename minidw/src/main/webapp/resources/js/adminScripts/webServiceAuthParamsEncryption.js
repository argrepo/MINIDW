var headers = [];
var  webServiceAuthParamsEncryption = {
		initialPage : function() {
		},
};

if($('.webServiceAuthParamsEncryption-page').length){
	webServiceAuthParamsEncryption.initialPage();
	$("#columnInfoDiv .columnInfo").slice(0).remove(); 
	if ( $("#columnInfoDiv > div").length == 0) {
		var addColumnInfo = $("#columnInfo").clone().removeAttr("id").show();
		addColumnInfo.find(".deleteColumnInfo").remove();
		addColumnInfo.find(".columnName").val('');
		addColumnInfo.find(".columnText").val('');
		$("#columnInfoDiv").append(addColumnInfo);
	} 
	
	$("#columnInfoDiv").on("click",".addColumnInfo",function(){
		var addColumnInfo = $("#columnInfo").clone().removeAttr("id style");
		$("#columnInfoDiv").append(addColumnInfo);
	});
	
	$("#columnInfoDiv").on("click",".deleteColumnInfo",function(){
		$(this).parents(".columnInfo").remove();
	});
	
	
	$(document).on("click","#encryptColumnInfo",function(){
		
		 var tableName =  $("#tableName").val();
		
		 var whereConditionColumnInfo = {};
		 
		 var whereConditioncolumnName =  $("#whereConditioncolumnName").val();
		 var whereConditionColumnText = $("#whereConditionColumnText").val();
		 whereConditionColumnInfo[whereConditioncolumnName]=whereConditionColumnText;
		 
		 var columnInfo = {}
		 $("#columnInfoDiv .columnInfo").each(function(){
			 var columnName = $(this).find(".columnName").val();
			 var columnValue =$(this).find(".columnText").val();
			 columnInfo[columnName]=columnValue;
		 });
		 
		 var userID = $("#userID").val();
			 var selectedData = {
					 tableName:tableName,
					 whereConditionColumnInfo :JSON.stringify(whereConditionColumnInfo),
					 columnInfo :JSON.stringify(columnInfo)
					
				};
			    console.log("selectedData",selectedData)
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				 
			   showAjaxLoader(true);
			   var encryptWebServiceAuthParams = "/app_Admin/user/"+userID+"/etlAdmin/encryptWebServiceAuthParams";
			   var myAjax = common.postAjaxCallObject(encryptWebServiceAuthParams,'POST',selectedData,headers);
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
	
		})
	
	
}