var headers = {};
var dlKpiMapping = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
	},
	ckeckMatchedDlKpis : function(tableRow){
	
	 $("#kpiInfo").find('input[type=checkbox]').each(function (){
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

if($('.dlKpiMapping-page').length){
	
	dlKpiMapping.initialPage();
	
	$(document).on('click', '#selectall', function(){
		 $('.kpiInfoByDlId').prop('checked',true);
		 $('.checkCase').attr('checked', this.checked);
		 
	});
	$(document).on('click', '.checkCase', function(){
		 
		if($(".checkCase").length == $(".checkCase:checked").length) {
			$("#selectall").attr("checked", "checked");
			
		} else {
			$("#selectall").removeAttr("checked");
		}

	});
	
	 
	 
	 $(document).on('change', '#dlName', function(){
		 
		    common.clearValidations(['#dlName']);
			var userID = $("#userID").val();
			var dlId = $("#dlName option:selected").val();
			$('.kpiInfoByDlId').prop('checked',false);
			$('#selectall').prop('checked',false);
			
			 var selectData = {
					 dL_id : dlId,
			    		 
			    }
			 console.log("selectData:",selectData);
			 if(dlId == 0){
				 common.showcustommsg("#dlName", globalMessage['anvizent.package.label.pleasechooseDl'],"#dlName");
		    	 return false;
			}
			var tableRow=[];
	         if(dlId != ''){
	        	 showAjaxLoader(true);
	        	 var url_getKpis = "/app_Admin/user/"+userID+"/etlAdmin/getKpiListByDlId";
	  		     var myAjax = common.postAjaxCall(url_getKpis,'POST',selectData,headers);
	  		     myAjax.done(function(result) {
	  		    	showAjaxLoader(false);
	  		    	  if(result != null){
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") { 
			    				  common.showErrorAlert(result.messages[0].text);
			    				  return false;
			    				  
			    			  } else if(result.messages[0].code == "SUCCESS"){
				    				  var list = result.object;
				    				  console.log("list:",list)
				  		  			  for(var i=0;i<list.length;i++){
				  	  		  			   tableRow.push(list[i]);
				  	  		  			  }
				  		  			dlKpiMapping.ckeckMatchedDlKpis(tableRow);
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
		
	 $(document).on('click', '#addKpiInfoByDlId', function(){
			 
		    common.clearValidations(['#dlName','.kpiValidation']);
			var userID = $("#userID").val();
			var checkedKpiNames="";
			var checkedKpiIds="";
			var dlId = $("#dlName option:selected").val();
			if(dlId == 0){
				 common.showcustommsg("#dlName", globalMessage['anvizent.package.label.pleasechooseDl'],"#dlName");
		    	 return false;
		       
			}
			var tablerows = $('#kpiInfo').find("tbody").find("tr").filter(function() {
		    	return $(this).find('.kpiInfoByDlId').is(':checked');
		    });
		    
		    if (!tablerows || tablerows.length === 0) {
		    	common.showcustommsg(".kpiValidation", globalMessage['anvizent.package.label.pleasechooseKpi'] ,".kpiValidation");
		    	return false;
		    }
		  
		    tablerows.each(function() {
		    	var row = $(this);
		    	var kpiName = row.find('.kpiName').text();
		    	var kpiId = row.find('.kpiId').text();
		    	 checkedKpiNames +=  kpiName + ",";
		    	 checkedKpiIds+= kpiId+",";
		    }); 
		    var	formattedCheckedKpiNames = checkedKpiNames.substring(0,checkedKpiNames.length - 1);
		    var	formattedCheckedKpiIds = checkedKpiIds.substring(0,checkedKpiIds.length - 1);
		    var selectData = {
		    		 dlId  : dlId,
		    		 checkedKpis : formattedCheckedKpiNames,
		    		 checkedIds : formattedCheckedKpiIds
		    }  
		    
		    showAjaxLoader(true);
		    var url_saveDlKpiMapping = "/app_Admin/user/"+userID+"/etlAdmin/saveDlKpiMapping";
			     var myAjax = common.postAjaxCallObject(url_saveDlKpiMapping,'POST',selectData,headers);
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
	
}