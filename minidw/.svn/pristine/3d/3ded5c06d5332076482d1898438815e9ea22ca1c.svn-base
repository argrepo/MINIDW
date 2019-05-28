var headers = {}; 
var viewILDL = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		},
		viewILList : function(result){			
			$("#ilNameList").empty();
			 
			var list='';			
			list+="<option value='Select IL'>--------------"+globalMessage['anvizent.package.label.selectIL']+"--------------</option>";
			for(var i=0;i<result.object.length;i++){
				var object = result.object[i];
				list+="<option value="+object['iL_id']+">"+object['iL_name']+"</option>";				
			}			 
			$("#ilNameList").append(list);
			 
		},
		viewDLList : function(result){			
			$("#dlNameList").empty();
			$(".iLDl-label").text("DL Name");
			var list='';			
			list+="<option value='Select DL'>--------------"+globalMessage['anvizent.package.label.selectDL']+"--------------</option>";	
			for(var i=0;i<result.object.length;i++){
				var object = result.object[i];
				list+="<option value="+object['dL_id']+">"+object['dL_name']+"</option>";				
			}			 
			$("#dlNameList").append(list);
			
		},
		validateEditIL : function(){
			var validStatus = true;
			common.clearValidations(["#ilNameList"]);
			if($("#ildlNameList option:selected").val() == 'Select IL'){
				common.showcustommsg("#ilNameList", globalMessage['anvizent.package.label.pleaseChooseIL']);
				validStatus=false;
			}
			return validStatus;
		},
		validateEditDL : function(){
			var validStatus = true;
			common.clearValidations(["#dlNameList"]);
			if($("#ildlNameList option:selected").val() == 'Select DL'){
				common.showcustommsg("#dlNameList", globalMessage['anvizent.package.label.pleaseChooseDL']);
				validStatus=false;
			}
			return validStatus;
		},
		
		viewILData : function(result){
			$("#il-data-table").empty();
			$("#il-fileContainer").empty();			
			$("#editIl,#deleteIl").show();
			$("#updateIl,#saveQuery").hide();
			$("#iLDbList,#iLQueryList").empty();
			$("#iLQuery").empty();
			var iLInfo = result.object.iLInfo[0];
			var filesInfo = result.object.fileNames;
			var paramInfo = result.object.eTLJobContextParamList;
			var iLConnectionMappingInfo = result.object.iLConnectionMapping;
			var table = null;						 
			var list=''; 
			iLQuery=[],connectionMappingIds = [];
			$("#ilName").val(iLInfo.iL_name).attr("disabled","disabled");
			$("#tableName").val(iLInfo.iL_table_name).attr("disabled","disabled");
			$("#description").val(iLInfo.description).attr("disabled","disabled");
			$("#JobName").val(iLInfo.jobName).attr("disabled","disabled");
			$("#iLDbList,#iLQuery").attr("disabled","disabled");			
			  
			if(filesInfo.length>0){				 
				$("#il-fileContainer0, .il-addFilePath").hide();
				$("#il-fileContainer").show();
				for(var i=0;i<filesInfo.length;i++){					
					$("#il-fileContainer").append("<div class='form-group' id='fileContainer"+i+"'><label class='control-label col-sm-3'>"+globalMessage['anvizent.package.label.file']+"</label>" +
												"<div class='col-sm-6 il-input-fields'>"+ 
												"<input type='file' name='files' class='filePath"+i+"' id='ilinputFile"+i+"' data-buttonText='Find file' style='display:none;'><span class='ilfiletext'>"+
												filesInfo[i]+"</span></div>" +
												"<a href='#' style='display:none;' class='btn btn-info btn-xs il-remove_field'><span class='glyphicon glyphicon-trash'></span></a>" +
												"</div>");
			  		increment++;
				}				
			}
			else if(filesInfo.length == 0){
				 
				$("#il-fileContainer0, .il-addFilePath").show();
				$("#il-fileContainer").hide();
			}
					
			for(var i=0;i<iLConnectionMappingInfo.length;i++){				
				var iLConnection = iLConnectionMappingInfo[i].iLConnection
				var dbId = iLConnection.database.id;
				var dbName = iLConnection.database.name;				
				$("#iLDbList").append("<option value='"+dbId+"'>"+dbName+"</option>");
				$("#iLQueryList").append("<option value='"+dbId+"'>"+iLConnectionMappingInfo[i].iLquery+"</option>");
				connectionMappingIds.push(iLConnectionMappingInfo[i].connectionMappingId);
				iLQuery.push({"dbId":dbId,"query":iLConnectionMappingInfo[i].iLquery});				
			}
			
			if(iLQuery.length == 0){
				$("#databasediv ,#querydiv,#savebutton").hide();
			}
			else if(iLQuery.length > 0){
				$("#databasediv ,#querydiv,#savebutton").show();
				$("#iLQuery").val(iLQuery[0].query);
				dbid = iLQuery[0].dbId;
			}
			
			
			
			table+="<thead><tr>"+					
					"<th>"+globalMessage['anvizent.package.label.paramName']+"</th>"+
					"<th>"+globalMessage['anvizent.package.label.paramValue']+"</th>"+
					"</tr></thead><tbody>"; 
			
			for(var i=0;i<paramInfo.length;i++){
				table+="<tr class='data-row'><td>"+paramInfo[i].paramId+"</td>" +
						"<td>"+paramInfo[i].paramValue+"</td></tr>"
			}		 
			
			$("#il-data-table").append(table);
			$(".iLAllDetails").show();	
			 
		},
		ilUpdationFormValidation : function(){			 
	   	    var iLName = $("#ilName").val();
			var jobName = $("#JobName").val();
	    	var tableName = $("#tableName").val();
	    	var description = $("#description").val();
	    	var iLquery =  $("#iLQuery").val();
	    	var visibllity = $("#iLQuery").is(":visible");
	   	    common.clearValidations(["#ilName", "#JobName","#ilinputFile","#description","#tableName","#iLQuery"]);
	   	    var validStatus=true;	   	     
		   	if(iLName == '' ){
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
	   	    if(iLquery == '' && visibllity){
	   	    	common.showcustommsg("#iLQuery", globalMessage['anvizent.package.label.pleaseEnterILQuery'],"iLQuery");
	   	        validStatus=false;
	   	    }
	   	 
	   	  	   		   
	   		$("#il-fileContainer0, .il-addFilePath").find("input[type='file']").each(function(){
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
		},
		uploadIlFiles:function(formData,userID,ilOrDl) { 
			var messageClass = "",messageText=null;
			if(ilOrDl == "IL"){
				messageClass = ".ilUpdationMsg";				
				$("#updateIl,#deleteIl").hide();
				$("#ilNameList").val("Select IL");
				messageText = 'IL is updated';
			}
			else{
				messageClass = ".dlUpdationMsg";				
				$("#updateDl,#deleteDl").hide();
				$("#dlNameList").val("Select DL");
				messageText = 'DL is updated';
			}
			$(messageClass).empty();
			$(messageClass).show();
			showAjaxLoader(true); 
	    	   var url_createIl = "/app_Admin/user/"+userID+"/etlAdmin/uploadIlOrDlFiles";
	 		   var myAjax = common.postAjaxCallForFileUpload(url_createIl,'POST', formData,headers);
	 	       myAjax.done(function(result) {
	 	    	  showAjaxLoader(false); 
	 	    	  if(result != null){		    		 
	 	    		  if(result.hasMessages){		    			  
		    			  if(result.messages[0].code == "ERROR") {		    				  
		    				  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
		    				  $(messageClass).append(message).fadeOut(5000);
			    			  	return false;
			    			  } 
		    			  else if(result.messages[0].code == "SUCCESS") {
		    				  var  message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+messageText+''+'<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+
									'</div>';
		    				  $(messageClass).append(message).fadeOut(5000);
			    		  }		    			  		    			  	
		    		  }
	 	    		 $("#iLdLList,.iLAllDetails, .dLAllDetails").hide();
	 	    		 $("input[name='iLOrDl']").attr("checked",false);
		    	  }	 	    	   
	 	       });
	       },
	       
	       viewDLData : function(result){
				$("#dl-data-table").empty();
				$("#dl-fileContainer").empty();				
				$("#editDl,#deleteDl").show();
				$("#updateDl").hide();
				var dLInfo = result.object.dLInfo[0];				 
				
				$("#dlName").val(dLInfo.dL_name).attr("disabled","disabled");
				$("#dltableName").val(dLInfo.dL_table_name).attr("disabled","disabled");
				$("#dldescription").val(dLInfo.description).attr("disabled","disabled");
				$("#dlJobName").val(dLInfo.jobName).attr("disabled","disabled");
							
				var filesInfo = result.object.fileNames;			
				 
				if(filesInfo.length>0){					 
					$("#dl-fileContainer0, .dl-addFilePath").hide();
					$("#dl-fileContainer").show();
					for(var i=0;i<filesInfo.length;i++){					
						$("#dl-fileContainer").append("<div class='form-group'><label class='control-label col-sm-3'>"+globalMessage['anvizent.package.label.file']+"</label>" +
													"<div class='col-sm-6 dl-input-fields'>"+ 
													"<input type='file' name='files' id='dlinputFile"+i+"' data-buttonText='Find file' style='display:none;'><span class='dlfiletext'>"+
													filesInfo[i]+"</span></div>" +
													"<a href='#' style='display:none;' class='btn btn-info btn-xs dl-remove_field'><span class='glyphicon glyphicon-trash'></span></a>" +
													"</div>");
						increment++;
					}				
				}
							
				else if(filesInfo.length == 0){
				 	$("#dl-fileContainer0, .dl-addFilePath").show();
					$("#dl-fileContainer").hide();
				}
				
				var paramInfo = result.object.eTLJobContextParamList;
				var table = null;
				
				table+="<thead><tr>"+					
						"<th>"+globalMessage['anvizent.package.label.paramName']+"</th>"+
						"<th>"+globalMessage['anvizent.package.label.paramValue']+"</th>"+
						"</tr></thead><tbody>"; 
				
				for(var i=0;i<paramInfo.length;i++){
					table+="<tr class='data-row'><td>"+paramInfo[i].paramId+"</td>" +
							"<td>"+paramInfo[i].paramValue+"</td></tr>"
				}		 
				
				$("#dl-data-table").append(table);
				$(".dLAllDetails").show();				 
			},
			
		dlUpdationFormValidation : function(){
				var dlName=$("#dlName").val();
		   	    var jobName=$("#dlJobName").val();		    	 
		    	var tableName = $("#dltableName").val();
		    	var description = $("#dldescription").val();
		   	    common.clearValidations(["#dlName", "#dlJobName","#dlinputFile","#dldescription","#dltableName"]);
		   	    var validStatus=true;	   	     
			   	if(dlName == '' ){
			    	 common.showcustommsg("#dlName", globalMessage['anvizent.package.label.pleaseEnterDLName'],"#dlName");
			    	 validStatus=false;
			    }
		   	    if(tableName == '' ){
			    	 common.showcustommsg("#dltableName", globalMessage['anvizent.package.label.pleaseEnterTableName'],"#dltableName");
			    	 validStatus=false;
			    }
		   	    if(description == '' ){
			    	 common.showcustommsg("#dldescription", globalMessage['anvizent.package.label.pleaseEnterDLDescription'],"#dldescription");
			    	 validStatus=false;
			    }   	    
		   	    if(jobName == ''){
		   	    	common.showcustommsg("#dlJobName", globalMessage['anvizent.package.label.pleaseEnterJobName'],"#dlJobName");
		   	        validStatus=false;
		   	   }		   	   
		   	   
		   		$("#dl-fileContainer0, .dl-addFilePath").find("input[type='file']").each(function(){				 
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
			},
			saveModifiedQuery : function(){
				iLQuery = [];
				$("#iLQueryList option").each(function(){
					iLQuery.push({"dbId":$(this).val(),"query":$(this).text()});								
				});
			}
		
}

if($('.viewILDL-page').length){
	viewILDL.initialPage();
	var userID=$('#userID').val();
	var increment = 0;
	var dynamicFile=''; 	 
	var iLQuery = [], connectionMappingIds=[];
	var dbid=0;
		$("input[name='iLOrDl']").on('change',function(){
			common.clearValidations(["#ilNameList","#dlNameList"]);
			$(".il-input-fields").find("input[type='file']").each(function(){				 
	   	        var id = $(this).attr("id");   	        
	   	        common.clearValidations(["#"+id]);
			});
			
		$("#iLdLList").show();
		
		var iLOrDL = $("input[name='iLOrDl']:checked").val();
		$(".iLAllDetails,.dLAllDetails").hide();
		$("#editIl,#editDl,#updateIl,#updateDl,#deleteIl,#deleteDl").hide();
		$("#il-fileContainer, .il-addFilePath").empty();
		
		if(iLOrDL == 'IL'){				
			showAjaxLoader(true); 
			var url_getIl = "/app_Admin/user/"+userID+"/etlAdmin/getAllIlInfo";
		    var myAjax = common.postAjaxCall(url_getIl,'GET','',headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false); 
		    	$(".dlList").hide();
		    	$(".ilList").show();
		    		
		    	if(result != null){		    		  
		    		  viewILDL.viewILList(result); 
		    	}		    	   
		    }); 
		}
		
		else{			
			showAjaxLoader(true); 
		    var url_getIl = "/app_Admin/user/"+userID+"/etlAdmin/getDlInfo";
		    var myAjax = common.postAjaxCall(url_getIl,'GET');
		    myAjax.done(function(result) {	    	
		    	showAjaxLoader(false); 
		    	$(".dlList").show();
		    	$(".ilList").hide();  
		    	
		    	if(result != null){ 
		    		  viewILDL.viewDLList(result); 
		    	}		    	   
		    });
		}		 
	});		
	
	
	// IL events //	 
	$(document).on('change','#ilNameList',function(){
		$("#il-fileContainer, .il-addFilePath").empty();
		$("#ilinputFile, #iLQuery").val("");
		$(".filePath0,.il-addFile").attr("disabled",true);
		
		common.clearValidations(["#ilName", "#JobName","#ilinputFile","#description","#tableName","#iLQuery"]);
		common.clearValidations(["#dlName", "#dlJobName","#dlinputFile","#dldescription","#dltableName"]);
		
		$(".il-input-fields").find("input[type='file']").each(function(){				 
   	        var id = $(this).attr("id");   	        
   	        common.clearValidations(["#"+id]);
		});	
		
		var il_id = $("#ilNameList option:selected").val();
		$("#ilId").val(il_id);
		var userID=$('#userID').val();
		
		if(il_id !== 'Select IL'){
			var url_getILData = "/app_Admin/user/"+userID+"/etlAdmin/getILData/"+il_id;
			showAjaxLoader(true); 
		    var myAjax = common.postAjaxCall(url_getILData,'GET','',headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    	if(result != null){			    		 
		    		if (!result.hasMessages) {
		    			viewILDL.viewILData(result);
		    		}			    		 
		    	}		    	   
		    });
		}
		else{
			$(".iLAllDetails,#editIl,#updateIl,#deleteIl").hide();
		}		 
	});	 
	
	$('.il-addFile').click(function(e){
		e.preventDefault();		
		$('.il-addFilePath').css('display','block');		
			dynamicFile =	"<div class='form-group' id='fileContainer"+increment+"' >"+
							"<label class='control-label col-sm-3' >"+globalMessage['anvizent.package.label.file']+"</label>"+
							"<div class='col-sm-6 il-input-fields'>" +
							"<input type='file' class='filePath"+increment+"' name='files' id='ilinputFile"+increment+"' data-buttonText='Find file'>"+
							"</div> " +
							"<a href='#' class='btn btn-info btn-xs remove_field'><span class='glyphicon glyphicon-trash'></span></a>" +
							"</div>";
			
			$('.il-addFilePath').append(dynamicFile);
			increment++;
	});
	
	$(document).on("click",".remove_field", function(e){ 
	   e.preventDefault();
	   $(this).parent('.form-group').remove();
	   increment++;
	});
	
	$("#editIl").on('click',function(){				 
		$("#ilName,#description,#JobName,#iLDbList,#iLQuery,.filePath0,.il-addFile").removeAttr("disabled");	
		$("#saveQuery,#updateIl,#il-fileContainer0").show();
		$(this).hide();
		$("#il-fileContainer").empty().hide(); 
	});	
	
	$("#iLDbList").on("change",function(){
		$("#iLQuery").val("");
		var dbId = $("#iLDbList option:selected").val();
		dbid = dbId;
		for(var i=0;i<iLQuery.length;i++){			
			if(dbId == iLQuery[i].dbId){				 
				$("#iLQuery").val(iLQuery[i].query);				
			}				
		}		
	});	
	$("#saveQuery").on("click",function(e){
		e.preventDefault();				
		$("#iLQueryList option").each(function(){			
			if($(this).val() == dbid){
				$(this).text($("#iLQuery").val());
			}			
		});
		viewILDL.saveModifiedQuery();
		$("#savedQuery").show();
		$("#savedQuery").addClass("alert alert-success alert-dismissible").text(globalMessage['anvizent.package.label.querySaved']).fadeOut(3500);
	});
	
	$("#updateIl").on('click',function(){
		var status= viewILDL.ilUpdationFormValidation();
		if(!status){ return false;}
		else{			 
			var userID = $("#userID").val();
			var iLId = $("#ilId").val();
			var ilName=$("#ilName").val();
			var jobName=$("#JobName").val();
			var fileUploadPath=[];
			var tableName = $("#tableName").val();
	    	var description = $("#description").val();	    	
	    	var iLQueryList = [], dBIds = [];   	 
	    	 
    		 $("#iLDbList option").each(function(){    			 
    			 dBIds.push($(this).val());
    		 });
    		 $("#iLQueryList option").each(function(){    			 
    			 iLQueryList.push($(this).text());
    		 }); 
    		 
    		fileUploadPath.push($("#ilinputFile").val());	    		
	    	    	 
	    	for(var i=0;i<increment ;i++){
				var filePath = ($("#ilinputFile"+i).val());
				 
				if(filePath != null){
					fileUploadPath.push(filePath);
				}				 
			}
	    	
		    var selectData = {
		    		ilInfo : {
		    			iL_id : iLId,
		    			iL_name : ilName,
			    		jobName : jobName,
			    		iL_table_name : tableName,
			    		description : description
		    		},
		           fileNames: fileUploadPath,
		           ilConnectionMapping : {
		        	   connectionMappingIds : connectionMappingIds,
		        	   ilquery : iLQueryList,
		        	   iLConnection : { 
		        		   database : {
		        			   ids : dBIds
		        		   }
		        	   }
		           }
		    }		    
		   var formData = new FormData($("#updateILDLForm")[0]);	     
		    showAjaxLoader(true); 
		    var url_updateIL = "/app_Admin/user/"+userID+"/etlAdmin/updateIL";
		   var myAjax = common.postAjaxCall(url_updateIL,'POST',selectData,headers);
		   myAjax.done(function(result) {
			   showAjaxLoader(false); 
		    	  if(result != null){ 
		    		  if(result.hasMessages){
		    			  $('.ilUpdationMsg').empty();
		    			  $('.ilUpdationMsg').show();
		    			  if(result.messages[0].code == "ERROR") {
		    				  $('.ilUpdationMsg').empty();
			    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
			    			  $(".ilUpdationMsg").append(message);
			    			  return false;
		    			  } else if(result.messages[0].code == "SUCCESS" && selectData.fileNames != "") {
		    				  viewILDL.uploadIlFiles(formData,userID,"IL");			    				   
		    			  }
		    			  else if(result.messages[0].code == "SUCCESS" && selectData.fileNames == ""){
		    				  var  message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+globalMessage['anvizent.package.label.iLIsupdated']+''+'<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+
								'</div>';		    				  		    				  
		    				  $(".ilUpdationMsg").append(message).fadeOut(5000);
		    				  $("#updateIl,.iLAllDetails,#iLdLList").hide();			    				  
		    			  }
		    			  $("input[name='iLOrDl']").attr("checked",false);
		    		  }
		    	  }		    	   
		    }); 
		}		
	});
	
	$("#deleteIl").on('click',function(){
		$("#deleteIlFileAlert").modal('show');
	});
	
	$("#confirmDeleteIL").on('click',function(){
		var iLId = $("#ilId").val();
		var userID = $("#userID").val();
		if(iLId !== ""){
			var url_deleteILData = "/app_Admin/user/"+userID+"/etlAdmin/deleteIL/"+iLId;
		    var myAjax = common.postAjaxCall(url_deleteILData,'GET','',headers);
		    myAjax.done(function(result) {			    	
		    	if(result != null){			    		 
		    		if (result.hasMessages) {
		    			$('.ilUpdationMsg').empty().show();
		    			if(result.messages[0].code == "ERROR") {		    				  
			    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
			    			  $(".ilUpdationMsg").append(message).fadeOut(5000);
			    			  return false;
		    			}
		    			else if(result.messages[0].code == "SUCCESS") {
		    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
			    			  $(".ilUpdationMsg").append(message).fadeOut(5000);			    			  
		    			}
		    			$("#editIl,#updateIl,#deleteIl,.iLAllDetails,#iLdLList").hide();
		    			$("input[name='iLOrDl']").attr("checked",false);	
		    		}			    		 
		    	}		    	   
		    });
		}
		else{
			$("#editIl,#updateIl,#deleteIl,.iLAllDetails,#iLdLList").hide();
			$("input[name='iLOrDl']").attr("checked",false);
		}	
	});
	
	
	// DL events //
		
	$(document).on('change','#dlNameList',function(){		
		$("#dl-fileContainer, .dl-addFilePath").empty();
		$("#dlinputFile").val("");
		$(".filePath0,.dl-addFile").attr("disabled",true);
		$(".dl-input-fields").find("input[type='file']").each(function(){				 
   	        var id = $(this).attr("id");   	        
   	        common.clearValidations(["#"+id]);
		});
				
		var dl_id = $("#dlNameList option:selected").val();
		$("#dlId").val(dl_id);
		var userID=$('#userID').val();
		
		if(dl_id !== 'Select DL'){
			var url_getILData = "/app_Admin/user/"+userID+"/etlAdmin/getDLData/"+dl_id;
			showAjaxLoader(true); 
		    var myAjax = common.postAjaxCall(url_getILData,'GET','',headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false); 
		    	if(result != null){			    		 
		    		if (!result.hasMessages) {			    			
		    			viewILDL.viewDLData(result);
		    		}			    		 
		    	}		    	   
		    });
			}
			else{
				$(".dLAllDetails,#editDl,#updateDl,#deleteDl").hide();
			}		 
	});
	
	$('.dl-addFile').click(function(e){
		e.preventDefault();		
		$('.dl-addFilePath').css('display','block');		
			dynamicFile =	"<div class='form-group' id='fileContainer"+increment+"' >"+
							"<label class='control-label col-sm-3' >"+globalMessage['anvizent.package.label.file']+"</label>"+
							"<div class='col-sm-6 dl-input-fields'>" +
							"<input type='file' class='filePath"+increment+"' name='files' id='dlinputFile"+increment+"' data-buttonText='Find file'>"+
							"</div> " +
							"<a href='#' class='btn btn-info btn-xs remove_field'><span class='glyphicon glyphicon-trash'></span></a>" +
							"</div>";
			
			$('.dl-addFilePath').append(dynamicFile);
			increment++;
	});
	
	$("#editDl").on('click',function(){		
		$("#dlName,#dldescription,#dlJobName,.fileName,.dl-addFile").removeAttr("disabled");
		$("#updateDl,#dl-fileContainer0").show();
		$(this).hide();
		$("#dl-fileContainer").empty().hide();
	});
	
	
	$("#updateDl").on('click',function(){
		var status= viewILDL.dlUpdationFormValidation();
		if(!status){ return false;}
		else{			 
			var userID = $("#userID").val();
			var dLId = $("#dlId").val();
			var dlName=$("#dlName").val();
			var jobName=$("#dlJobName").val();
			var fileUploadPath=[];
			var tableName = $("#dltableName").val();
	    	var description = $("#dldescription").val();
	    		    	 
	    	fileUploadPath.push($("#dlinputFile").val());	    		 
	    	   	 
    		for(var i=0;i<increment;i++){
				var filePath = ($("#dlinputFile"+i).val());
				 
				if(filePath != null){
					fileUploadPath.push(filePath);
				}				
			}	    	     	
			 				
		    var selectData = {
		    		dlInfo : {
		    			dL_id : dLId,
		    			dL_name : dlName,
			    		jobName : jobName,
			    		dL_table_name : tableName,
			    		description : description,
			    		is_Active : true
		    		},
		           fileNames: fileUploadPath	          
		    }
		     var formData = new FormData($("#updateILDLForm")[0]);	     
		     var url_updateDL = "/app_Admin/user/"+userID+"/etlAdmin/updateDL";
		     showAjaxLoader(true); 
		     var myAjax = common.postAjaxCall(url_updateDL,'POST',selectData,headers);
		     myAjax.done(function(result) {
		    	 showAjaxLoader(false); 
		    	  if(result != null){ 
		    		  if(result.hasMessages){
		    			  $('.dlUpdationMsg').empty();
		    			  $('.dlUpdationMsg').show();
		    			  if(result.messages[0].code == "ERROR") {
		    				  $('.dlUpdationMsg').empty();
			    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
			    			  $(".dlUpdationMsg").append(message);
			    			  return false;
		    			  } else if(result.messages[0].code == "SUCCESS" && selectData.fileNames != "") {		    				 
		    				  viewILDL.uploadIlFiles(formData,userID,"DL");			    				   
		    			  }
		    			  else if(result.messages[0].code == "SUCCESS" && selectData.fileNames == ""){
		    				  var  message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+globalMessage['anvizent.package.label.dLIsUpdated']+''+'<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+
								'</div>';
		    				  $("#updateDl").hide();		    				  
		    				  $(".dlUpdationMsg").append(message).fadeOut(5000);
		    				  $(".dLAllDetails, #iLdLList").hide();			    				  
		    			  }
		    			  $("input[name='iLOrDl']").attr("checked",false);
		    		  }
		    	  }		    	   
		    }); 
		}		
	});
	
	$("#deleteDl").on('click',function(){
		$("#deleteDlFileAlert").modal('show');
	});
	
	$("#confirmDeleteDL").on('click',function(){		 
		var dLId = $("#dlId").val();
		var userID = $("#userID").val();
		if(dLId !== ""){
			var url_deleteDLData = "/app_Admin/user/"+userID+"/etlAdmin/deleteDL/"+dLId;
			showAjaxLoader(true); 
		    var myAjax = common.postAjaxCall(url_deleteDLData,'GET','',headers);
		    myAjax.done(function(result) {
		    	
		    	showAjaxLoader(false); 
		    	if(result != null){			    		 
		    		if (result.hasMessages) {
		    			$('.dlUpdationMsg').empty().show();
		    			if(result.messages[0].code == "ERROR") {		    				  
			    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
			    			  $(".dlUpdationMsg").append(message).fadeOut(5000);
			    			  return false;
		    			}
		    			else if(result.messages[0].code == "SUCCESS") {
		    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
			    			  $(".dlUpdationMsg").append(message).fadeOut(5000);			    			  
		    			}
		    			$("#editDl,#updateDl,#deleteDl,.dLAllDetails,#iLdLList").hide();
		    			$("input[name='iLOrDl']").attr("checked",false);	
		    		}			    		 
		    	}		    	   
		    });
		}
		else{
			$("#editDl,#updateDl,#deleteDl,.dLAllDetails,#iLdLList").hide();
			$("input[name='iLOrDl']").attr("checked",false);
		}	
	});
	
}