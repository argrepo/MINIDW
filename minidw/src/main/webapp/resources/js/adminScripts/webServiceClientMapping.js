var headers = {};
var webServiceClientMapping = {
		initialPage : function() {
			$("#webservices").multipleSelect({
				filter : true,
				placeholder : "Select Web Service",
			    enableCaseInsensitiveFiltering: true
			});
			
			$("#clientId, #webserviceName").select2({               
                allowClear: true,
                theme: "classic"
			});
			
			setTimeout(function() { $("#pageErrors").hide(); }, 5000);
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		},
 ckeckMatchedClients : function(tableRow){
	
	 $("#webserviceInfo").find('input[type=checkbox]').each(function (){
 			 var clientId = $(this).val();
			for(var j=0; j < tableRow.length;j++){
				var ID = tableRow[j];				
				if(ID == clientId){
					$(this).prop('checked',true);
				}
			}
		});
    },
}

if($('.webServiceClientMapping-page').length || $(".clientWebserviceMapping-page").length){
	
	webServiceClientMapping.initialPage();
	
	$(document).on('click', '#selectall', function(){
		 $('.clientInfoByWebserviceId').prop('checked',true);
		 $('.checkCase').attr('checked', this.checked);
		 
	});
	$(document).on('click', '.checkCase', function(){
		 
		if($(".checkCase").length == $(".checkCase:checked").length) {
			$("#selectall").attr("checked", "checked");
			
		} else {
			$("#selectall").removeAttr("checked");
		}

	});
	
	 
	 
	 $(document).on('change', '#webserviceName', function(){
		 
		    common.clearValidations(['#webserviceName']);
			var userID = $("#userID").val();
			var webserviceId = $("#webserviceName option:selected").val();
			$('.clientInfoByWebserviceId').prop('checked',false);
			$('#selectall').prop('checked',false);
			
			 var selectData = {
					    web_service_id : webserviceId,
			    		 
			    }
			 if(webserviceId == 0){
				 common.showcustommsg("#webserviceName", globalMessage['anvizent.package.label.pleasechoosewebservice'],"#webserviceName");
		    	 return false;
			}
			var tableRow=[];
	         if(webserviceId != ''){
	        	 showAjaxLoader(true);
	        	 var url_getWebserviceClients = "/app_Admin/user/"+userID+"/etlAdmin/getWebserviceClients";
	  		     var myAjax = common.postAjaxCall(url_getWebserviceClients,'POST',selectData,headers);
	  		     myAjax.done(function(result) {
	  		    	showAjaxLoader(false);
	  		    	  if(result != null){
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") { 
			    				  common.showErrorAlert(result.messages[0].text);
			    				  return false;
			    				  
			    			  } else if(result.messages[0].code == "SUCCESS"){
				    				  var list = result.object;
				  		  			  for(var i=0;i<list.length;i++){
				  	  		  			   tableRow.push(list[i]);
				  	  		  			  }
				  		  			   webServiceClientMapping.ckeckMatchedClients(tableRow);
				  	  		  		    }
			    		       }
	  		    	       }
	  		    	  else{ 
	  		    		  common.showErrorAlert(result.messages[0].text)
	  		    		  return false;
	  		    	  }
	  		    });
	  	 
	         }
	        
		   });
		
	 $(document).on('click', '#addClientsAndWebserviceId', function(){
			 
		    common.clearValidations(['#webserviceName','.webserviceValidation']);
			var userID = $("#userID").val();
			var  checkedClients="";
			var webserviceId = $("#webserviceName option:selected").val();
			if(webserviceId == 0){
				 common.showcustommsg("#webserviceName", globalMessage['anvizent.package.label.pleasechoosewebservice'],"#webserviceName");
		    	 return false;
		       
			}
			var tablerows = $('#webserviceInfo').find("tbody").find("tr").filter(function() {
		    	return $(this).find('.clientInfoByWebserviceId').is(':checked');
		    });
		    
		    if (!tablerows || tablerows.length === 0) {
		    	common.showcustommsg(".webserviceValidation", globalMessage['anvizent.package.label.pleasechooseclients'] ,".webserviceValidation");
		    	return false;
		    }
		  
		    tablerows.each(function() {
		    	var row = $(this);
		    	var clientId = row.find('.clientId').text();
		    	 checkedClients +=  clientId + ",";
		    }); 
		    var	formattedCheckedClients = checkedClients.substring(0,checkedClients.length - 1);
		    var selectData = {
		    		webserviceId : webserviceId,
		    		checkedClients : formattedCheckedClients
		    }  
		    
		    showAjaxLoader(true);
		    var url_saveEtlDlIlMapping = "/app_Admin/user/"+userID+"/etlAdmin/saveWebserviceClientMapping";
			     var myAjax = common.postAjaxCallObject(url_saveEtlDlIlMapping,'POST',selectData,headers);
			     myAjax.done(function(result) {
			    	 showAjaxLoader(false);
			    	  if(result != null){ 
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") { 
			    				  common.showErrorAlert(result.messages[0].text );
			    				  return false;
			    			  } else if(result.messages[0].code == "SUCCESS"){
				    		        if(result.object > 0){ 
				    		        	common.showSuccessAlert(result.messages[0].text );
				    		        }
				    		  }
			    		  }
			    	  }
			    	   
			    });
		   });
/////////////////////////////////*********CLIENT WEBSERVICE MAPPING*********///////////////////////////////////////
	 
	 $(document).on("change","#clientId",function(){
			this.form.submit();
			showAjaxLoader(true);
	 });
	 
	 if ( $("#clientId").length > 0 ) {
			if ( $("#clientId").children().length > 1 &&  $("#clientId").val() == '0') {
				$("#clientId").val($("#clientId option").eq(1).val())
				$("#clientId").change();
			}
	 }
	 
	 $(document).on("click","#saveWebserviceMapping",function(){ 

			if($("#webservices option:selected").length == 0){
				common.showcustommsg("#webservices", globalMessage['anvizent.package.label.pleaseChooseAtLeastOneWebservice'], "#webservices")
				return false;
			}
			
			$("#clientWebserviceMappingForm").prop("action",$("#saveUrl").val()); 
			$("#clientWebserviceMappingForm").submit();
		    showAjaxLoader(true);
		});
}