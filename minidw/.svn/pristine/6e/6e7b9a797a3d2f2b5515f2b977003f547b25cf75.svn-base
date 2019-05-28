var headers = {};
var  iLDLMapping = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		},
		ckeckMatchedIls : function(tableRow){
			
			 $("#ilInfo").find('input[type=checkbox]').each(function (){
		  			 var ilId = $(this).val();
					for(var j=0; j < tableRow.length;j++){
						var ID = tableRow[j];				
						if(ID == ilId){
							$(this).prop('checked',true);
						}
					}
				});
	     }
	}

if($('.iLDLMapping-page').length){
	iLDLMapping.initialPage();
	// add multiple select / deselect functionality
	$(document).on('click', '#selectall', function(){
		 $('.ilInfoByDlId').prop('checked',true);
		 $('.checkCase').attr('checked', this.checked);
		 
	});
	// if all checkbox are selected, check the selectall checkbox and viceversa
	$(document).on('click', '.checkCase', function(){
		 
		if($(".checkCase").length == $(".checkCase:checked").length) {
			$("#selectall").attr("checked", "checked");
			
		} else {
			$("#selectall").removeAttr("checked");
		}

	});
	$(document).on('change', '#dlName', function(){
		
		common.clearValidations(['.ilValidation']);
		var userID = $("#userID").val();
		var dlId = $("#dlName option:selected").val();
		$('.ilInfoByDlId').prop('checked',false);
		$('#selectall').prop('checked',false);
		var tableRow=[];
         if(dlId != ''){
        	 showAjaxLoader(true);
        	 var url_getIlInfo = "/app_Admin/user/"+userID+"/etlAdmin/getIlInfo/"+dlId;
  		     var myAjax = common.postAjaxCall(url_getIlInfo,'GET','',headers);
  		     myAjax.done(function(result) {
  		    	showAjaxLoader(false);
  		    	  if(result != null){
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") {
		    				  $('.saveEtlDlIlMapping').empty();
			    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
			    			  $(".saveEtlDlIlMapping").append(message)
			    			  return false;
			    			  } else if(result.messages[0].code == "SUCCESS"){
			    				  var list = result.object;
			  		  			  for(var i=0;i<list.length;i++){
			  	  		  			tableRow.push(list[i].iL_id);
			  	  		  			   }
			  	  		  		    iLDLMapping.ckeckMatchedIls(tableRow);
			  	  		  		    }
		    		       }
  		    	       }
  		    	  else{
  		    		  $('.saveEtlDlIlMapping').empty();
		    			var  message = '<div class="alert alert-danger alert-dismissible" role="alert">'+
							''+globalMessage['anvizent.package.label.thereisNoIlFor']+''+dlId+' <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+
							'</div>';
		    			$('.saveEtlDlIlMapping').append(message);
		    			setTimeout(function() { $(".saveEtlDlIlMapping").hide().empty(); }, 10000);
  		    	  }
  		    });
  	 
         }
        
        
	
	   });
	
	$(document).on('click', '#addIls', function(){
	
		$(".saveEtlDlIlMapping").show();
		var userID = $("#userID").val();
		var  checkedIls=[];
		var dlId = $("#dlName option:selected").val();
		if(dlId == 0){
			 common.showcustommsg("#dlName", globalMessage['anvizent.package.label.pleaseChooseDlName'],"#dlName");
	    	 return false;
	       
		}
		common.clearValidations(['#dlName']);
		common.clearValidations(['.ilValidation']);
		var tablerows = $('#ilInfo').find("tbody").find("tr").filter(function() {
	    	return $(this).find('.ilInfoByDlId').is(':checked');
	    });
	    
	    if (!tablerows || tablerows.length === 0) {
	    	common.showcustommsg(".ilValidation", globalMessage['anvizent.package.label.pleaseChooseilNames']);
	    	return false;
	    }
	  
	    tablerows.each(function() {
	    	var row = $(this);
	    	var iL_id = row.find('.iLId').text();
	    	var iL_name = row.find('.ilname').text();
	    	var ilInfo = {
	    			iL_id : iL_id,
	    			iL_name : iL_name
		    	};
	    	  checkedIls.push(ilInfo);
	    }); 
	    var selectData = {
	    		dlId : dlId,
	    		iLInfo : checkedIls
	    }  
	    if(dlId != ''){
	    showAjaxLoader(true);
	    var url_saveEtlDlIlMapping = "/app_Admin/user/"+userID+"/etlAdmin/saveEtlDlIlMapping";
		     var myAjax = common.postAjaxCall(url_saveEtlDlIlMapping,'POST',selectData,headers);
		     myAjax.done(function(result) {
		    	 showAjaxLoader(false);
		    	  if(result != null){ 
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") {
		    				  $('.saveEtlDlIlMapping').empty();
			    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
			    			  $(".saveEtlDlIlMapping").append(message)
			    			  return false;
			    			  } else if(result.messages[0].code == "SUCCESS"){
			    		        if(result.object > 0){
			    			    $('.saveEtlDlIlMapping').empty();
			    				var  message = '<div class="alert alert-success alert-dismissible" role="alert">'+
	  							''+''+result.messages[0].text+''+' <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+
	  							'</div>';
			    			    $('.saveEtlDlIlMapping').append(message);
			    			    setTimeout(function() { $(".saveEtlDlIlMapping").hide().empty(); }, 10000);
			    			     $("#dlName").val('0');
			    			     $('.ilInfoByDlId').prop('checked',false);
			    		     }
			    		        location.reload();
			    		  }
		    		  }
		    	  }
		    	   
		    });
	    
	    }
	});

	
}
