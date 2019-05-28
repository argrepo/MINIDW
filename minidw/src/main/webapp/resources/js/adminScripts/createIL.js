var headers = {};
var createIL = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		},
  updateSlno : function(tbl) {
	if (tbl) {
		tbl.find('tr.data-row').each(function(index) {
			$(this).find("td:first").text(index+1);
		});
	}
       },
       uploadIlFiles:function(formData,userID) { 
    	   $(".ilCreationMsg").show();
    	   var url_createIl = "/app_Admin/user/"+userID+"/etlAdmin/uploadIlOrDlFiles";
 		   var myAjax = common.postAjaxCallForFileUpload(url_createIl,'POST', formData,headers);
 	      myAjax.done(function(result) {
 	    	  if(result != null){ 
	    		  if(result.hasMessages){
	    			  if(result.messages[0].code == "ERROR") {
	    				  $('.ilCreationMsg').empty();
		    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
		    			  $('.ilCreationMsg').append(message).fadeOut(5000);
		    			  return false;
		    			  } else if(result.messages[0].code == "SUCCESS") {
		    				  $('.ilCreationMsg').empty();
		    				  	
		    				  	var  message = '<div class="alert alert-success alert-dismissible" role="alert">'+
									''+''+result.messages[0].text+''+ '<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+
									'</div>';
				    			$('.ilCreationMsg').append(message).fadeOut(5000);
				    			$("#ilName, #JobName, .filePath0, #description, #tableName").val('');				    	   	     
				    	    	$(".addFilePath").find("input[type='file']").each(function(){				 
			    	      	         var id = $(this).attr("id");
			    	      	         $("#"+id).val('');
				    	      	});
				    	    	$("#data-table").find("input[type=checkbox]:checked").each(function(){
				    	    		$(this).prop("checked",false);	
				    	    	}); 
		    			  }
	    		  }
	    	  }
 	    	   
 	    });
       },
       ilCreationFormValidation : function(){
    	var ilName=$("#ilName").val();
   	    var jobName=$("#JobName").val();
    	var file=$(".filePath0").val();
    	var contextParameter= $("#contextParameters").val();
    	var tableName = $("#tableName").val();
    	var description = $("#description").val();
   	    common.clearValidations(["#ilName", "#JobName","#inputFile","#contextParameters","#context-params","#description","#tableName"]);
   	    var validStatus=true;
   	    if(ilName == '' ){
   	    	 common.showcustommsg("#ilName", globalMessage['anvizent.package.label.pleaseEnterILName'],"#ilName");
   	    	 validStatus=false;
   	     }
	   	if(tableName == '' ){
	    	 common.showcustommsg("#tableName", globalMessage['anvizent.package.label.pleaseEnterTableName'],"#tableName");
	    	 validStatus=false;
	    }
   	    if(description == '' ){
	    	 common.showcustommsg("#description", globalMessage['anvizent.package.label.pleaseEnterILDescription'],"#description");
	    	 validStatus=false;
	     }   	    
   	    if(jobName == ''){
   	    	common.showcustommsg("#JobName", globalMessage['anvizent.package.label.pleaseEnterJobName'],"#JobName");
   	        validStatus=false;
   	       }
   	     if(file == ''){	 
   	    	    common.showcustommsg("#inputFile", globalMessage['anvizent.package.label.pleaseChooseFile'],"#inputFile");
   	    	    validStatus=false;
   	    	} else{
   	    	 var fileExtension = file.replace(/^.*\./, '');
   	    	 console.log(fileExtension+":fileExtension");
   		      if(!(fileExtension == 'jar' || fileExtension == 'txt' )) {
   		    	common.clearValidations(['#inputFile']);
   		    	common.showcustommsg("#inputFile", globalMessage['anvizent.package.label.pleaseChooseEitherTxtorJarFile'],"#inputFile");
   		    	validStatus=false;
   		     }
   		      
   	    	}
   	  					
		if($("#data-table input:checkbox:checked").length == 0){
			common.showcustommsg("#context-params", globalMessage['anvizent.package.label.pleasechooseContextParameters']);
			validStatus=false; 
		}
			
   	   $(".addFilePath").find("input[type='file']").each(function(){				 
   	        var id = $(this).attr("id");
   	        common.clearValidations(["#"+id]);
   			if($(this).val() == ''){
   				common.showcustommsg("#"+id, globalMessage['anvizent.package.label.pleaseChooseFile'],"#"+id);
   				validStatus=false;
   			}else{
   				  var fileExtension = $(this).val().replace(/^.*\./, '');
     		      if(!(fileExtension == 'jar' || fileExtension == 'txt' )) {
     		    	common.showcustommsg("#"+id, globalMessage['anvizent.package.label.pleaseChooseEitherTxtorJarFile'],"#"+id);
     		    	validStatus=false;
     		     } 
   			}
   		});
   	   
   	    return validStatus;	
   	   
       }
	}
if($('.createIL-page').length){
	createIL.initialPage();
	 var fileIncrement=1;
	var dynamicFile=''; 
	var increment = 1;
	$('.addFile').click(function(e){
		e.preventDefault();		
		$('.addFilePath').css('display','block');		
			dynamicFile='<div class="form-group" id="fileContainer'+increment+'" ><label class="control-label col-sm-3" >File:</label>'+'<div class="col-sm-6"><input type="file" class="filePath'+increment+'" name="files" id="inputFile'+increment+'" data-buttonText="Find file">'+'</div> <a href="#" class="btn btn-info btn-xs remove_field">'+'<span class="glyphicon glyphicon-trash"></span></a></div>';					
			$('.addFilePath').append(dynamicFile);
			increment++;
	});
	$(document).on("click",".remove_field", function(e){ 
		   e.preventDefault();
		   $(this).parent('.form-group').remove();
		   increment++;
		});
	$('#contextParameters').multiselect({				
		onChange : function(option, checked, select){
			common.clearValidations(["#contextParameters"]);
			if(checked){
				var tbl = $('#tables').find("tbody");
				var row = tbl.find('tr.hiddenrow').clone().removeClass('hiddenrow hidden').addClass('data-row delete' +option.val());							
				row.find('td:eq(1)').text(option.text());							
				row.find('input[type=radio]').prop('name', 'radio_'+option.val());	
				row.find('input[type=text]').prop('id', 'parameterValue'+option.val());	
				tbl.append(row);
				createIL.updateSlno(tbl);
			}
			else{							
				$('.delete'+option.val()).remove();
				var row = $('.delete-row').closest("tr.data-row");							
				tbl = row.closest('tbody');
				createIL.updateSlno(tbl);	
			}								 
		 }					 
	});
	$("#tables").on('click', ".delete-row", function() {
		var row = $(this).closest("tr.data-row");
		tbl = row.closest('tbody');
		var sel = $("#contextParameters");
		var id = row.find('input[type=radio]').prop('name').replace('radio_', '');
		sel.find('option[value="'+id+'"]').prop('selected', false);
		sel.multiselect('refresh');
		row.remove();
		createIL.updateSlno(tbl);
	});
	
	$("#tables").on('click',  "input.param-radio", function() {
		var row = $(this).closest('tr.data-row'),
		select = row.find('select.param-vals-select'),
		input = row.find('input.param-vals-input');
		row.find('input.param-vals-input') ;
		if (this.value === "Existing") {   
			select.show();
		    input.hide();
		    var id=input.attr('id');
    	    common.clearValidations(["#"+id]);
		}
		else {
			select.hide();
			input.show();	
		}
	});
		
	 $("#ilCreation").on('click', function() {
		var status= createIL.ilCreationFormValidation();
		if(!status){ return false;}
		var paramValues='';
		var pramId=[];
		var paramName=[];
		var ilContextParams=[];
		var ilList=[];
		var	seletedParams ='';
		var userID = $("#userID").val();
		var ilName=$("#ilName").val();
		var jobName=$("#JobName").val();
		var fileUploadPath=[];
		var tableName = $("#tableName").val();
    	var description = $("#description").val();
		 
		for(var i=0;i<increment ;i++){
			var filePath=($(".filePath"+i).val());
			if(filePath != null){
				fileUploadPath.push(filePath);
			}
			
		} 
			
		$("#data-table").find('.data-row').each(function() {
			var paramId =null;
			var paramValue = null;		
			var row = $(this);			
			var chekbox = row.find("input[type='checkbox']");
			
			if(row.find("input[type='checkbox']").is(":checked")){
				paramId = chekbox.parents('.data-row').find('.paramId').text();
				paramValue = chekbox.parents('.data-row').find('.paramVal').val();
			
				var ilContextParam= {
		         	 paramValue : paramValue,
		             paramId : paramId
		        }
				ilContextParams.push(ilContextParam);
			}
		});
		
	    var selectData = {
	    		ilInfo : {
	    			iL_name : ilName,
		    		jobName : jobName,
		    		iL_table_name : tableName,
		    		description : description
	    		},
	           fileNames: fileUploadPath,
	           eTLJobContextParamList:ilContextParams	          
	    }
	    
	    
	     var formData = new FormData($("#createIlForm")[0]);	
	     showAjaxLoader(true);
	     var url_createIl = "/app_Admin/user/"+userID+"/etlAdmin/createIl";
	     var myAjax = common.postAjaxCall(url_createIl,'POST',selectData,headers);
	     myAjax.done(function(result) {
	    	 showAjaxLoader(false);
	    	  if(result != null){ 
	    		  if(result.hasMessages){
	    			  if(result.messages[0].code == "ERROR") {
	    				  $('.ilCreationMsg').empty();
		    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
		    			  $(".ilCreationMsg").append(message);
		    			  return false;
		    			  } else if(result.messages[0].code == "SUCCESS") {
		    				   if(result.object > 0){
		    					   createIL.uploadIlFiles(formData,userID);
		    				   }
		    			  }
	    		  }
	    	  }
	    	   
	    });
	      
	});
	 
	 $("input[name='selectAll']").on('change',function(){
			var isChecked = $(this).is(":checked");
			if(isChecked){
				$("#data-table tbody tr").each(function(){					
					$(this).find("input[type='checkbox']").prop("checked",true);					
				});
			}
			else{
				$("#data-table tbody tr").each(function(){					
					$(this).find("input[type='checkbox']").prop("checked",false);
				});
			}
	});
		
} 