var headers = {};
var template = {
		initialPage : function(){
			$("#iLAndXrefTemplates").DataTable({"language": {
	                "url": selectedLocalePath
	            }});	
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
		},
		validateFileUpload : function(){			
			common.clearValidations(['#xref_template,#il_template']);
			var isSameAsIL = $("#sameAsIl").is(":checked");
			var il_template = "",
				xref_template = "";
			var returnVal = true;
			
			if(isSameAsIL){
				il_template = $("#il_template").val();	
				if(il_template != ""){
					var fileExtension = il_template.replace(/^.*\./, '');
		   		    if(fileExtension != 'csv') {
		   		    	common.showcustommsg("#il_template", globalMessage['anvizent.package.label.pleaseChooseCSVFile'],"#il_template");
		   		    	returnVal=false;
		   		    }
				}else{
	   		    	common.showcustommsg("#il_template", globalMessage['anvizent.package.label.pleaseChooseFile'],"#il_template");
	   		    	returnVal=false;
				}
				
			}
			else{
				il_template = $("#il_template").val();
				xref_template = $("#xref_template").val();
				
				var il_template_Extension = il_template.replace(/^.*\./, '');
				var xref_template_Extension = xref_template.replace(/^.*\./, '');
				
				if(il_template != ""){
					if(il_template_Extension != 'csv') {
		   		    	common.showcustommsg("#il_template", globalMessage['anvizent.package.label.pleaseChooseCSVFile'],"#il_template");
		   		    	returnVal=false;
		   		    }
				}
				else{
	   		    	common.showcustommsg("#il_template", globalMessage['anvizent.package.label.pleaseChooseFile'],"#il_template");
	   		    	returnVal=false;
				}
		   		    
	   		    if(xref_template != ""){
	   		    	if(xref_template_Extension != 'csv') {
		   		    	common.showcustommsg("#xref_template", globalMessage['anvizent.package.label.pleaseChooseCSVFile'],"#xref_template");
		   		    	returnVal=false;
		   		    }
	   		    }
	   		    else{
	   		    	common.showcustommsg("#xref_template", globalMessage['anvizent.package.label.pleaseChooseFile'],"#xref_template");
	   		    	returnVal=false;
	   		    }
		   		    
			}
			 			
			return returnVal;
		},
		uploadILAndXrefTemplate : function(formData,userID) { 
	    	  var url_uploadILAndXrefTemplate = "/app_Admin/user/"+userID+"/etlAdmin/uploadILAndXrefTemplate";
	 		  var myAjax = common.postAjaxCallForFileUpload(url_uploadILAndXrefTemplate,'POST', formData,headers);
	 	      myAjax.done(function(result) {
	 	    	 showAjaxLoader(false);
	 	    	  if(result != null){ 
		    		  if(result.hasMessages){
		    			  $("#successOrErrorMessage").show();
		    			  var message = "";
		    			  if(result.messages[0].code=="ERROR_IL"){
		    				  console.log(result.messages[0].text)
		    				  message = result.messages[0].text;
		    				  common.showcustommsg("#il_template_errorMessage", message,"#il_template_errorMessage");
		    				 $("#il_template_errorMessage").show();
		    				  return false;
		    			  }
		    			  if(result.messages[0].code=="ERROR_XREF"){
		    				  message = result.messages[0].text;
		    				  common.showcustommsg("#xref_template_errorMessage", message,"#xref_template_errorMessage");
		    				  $("#xref_template_errorMessage").show();
		    				  return false;
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  message = result.messages[0].text;
		    				  $("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>");
		    				  $("#viewUploadFilePopUp").modal('hide');
		    				  template.showILAndXrefTemplate(result);
		    			  }
		    			  setTimeout(function() { $("#successOrErrorMessage").hide(); }, 10000);
		    			  
		    		  }
		    	  }
	 	    });
	       },
	       showILAndXrefTemplate : function(result){
				var table = $("#iLAndXrefTemplates").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
					table.clear();
					for (var i = 0; i < result.object.length; i++) {
					    var iLId = result.object[i].il_id;
					    var ilName = result.object[i].IL_name;
					    var fielName = result.object[i].filename;
					    var xrefFileName = result.object[i].xref_filename;
					    console.log(result.object)
					    var downloadILTemplateBtn = "",downloadXrefTemplateBtn = ""; 
					    var uploadFile = "<a href='#' class='upload' data-ILID='"+iLId+"' data-IlName='"+ilName+"' data-IlFileName='"+fielName+"' data-XrefFileName='"+xrefFileName+"'>Upload</a>";
					    var row = [];
					    row.push(iLId);
					    row.push(ilName);
					    if(fielName != null && fielName != ""){
					    	row.push("<td>"+fielName+"</td>");
					    	downloadILTemplateBtn = "<td><button type='button' data-ILID='"+iLId+"' class='btn btn-primary btn-sm downloadILTemplate'><span title='Download IL Template' class='glyphicon glyphicon-download-alt' aria-hidden='true'></span></button></td>";
					    }else{
					    	row.push("<td>-</td>");
					    	downloadILTemplateBtn = "<td><button type='button' disabled='disabled' class='btn btn-primary btn-sm'><span title='Download IL Template' class='glyphicon glyphicon-download-alt' aria-hidden='true'></span></button></td>";
					    }
					    if(xrefFileName != null && xrefFileName != ""){
					    	row.push("<td>"+xrefFileName+"</td>");
					    	downloadXrefTemplateBtn = "<td><button type='button' data-ILID='"+iLId+"' data-XrefFileName='"+fielName+"' class='btn btn-primary btn-sm downloadXrefTemplate'><span title='Download Xref Template' class='glyphicon glyphicon-download-alt' aria-hidden='true'></span></button></td>";
					    }else{
					    	row.push("<td>-</td>");
					    	downloadXrefTemplateBtn = "<td><button type='button' disabled='disabled' class='btn btn-primary btn-sm'><span title='Download Xref Template' class='glyphicon glyphicon-download-alt' aria-hidden='true'></span></button></td>";
					    }
					    row.push(uploadFile);
					    row.push(downloadILTemplateBtn);
					    row.push(downloadXrefTemplateBtn);
					    table.row.add(row);
				}
				table.draw(true);
			
	       },   
}


if($('.templates-page').length){
	template.initialPage();
	
	//events	
	$(document).on("click",".upload",function(){
			common.clearValidations(['#xref_template,#il_template']);
			$("#il_template,#xref_template").val("");	
			var ilName = $(this).attr("data-IlName");
			var iLId = $(this).attr("data-ILID");
			var iLFileName = $(this).attr("data-IlFileName");
			var xreFileName = $(this).attr("data-XrefFileName");
			$("#il_template_errorMessage").empty();
			$("#xref_template_errorMessage").empty();
			$("#ilNameHeaderText").text(ilName);
			$("#iLName").val(ilName);
			$("#iLId").val(iLId);
			$("#ilfileName").val(iLFileName);
			$("#xrefFilename").val(xreFileName);			
			console.log(iLFileName+" --"+xreFileName)
			 if(iLFileName == "" && xreFileName == ""){	
				 $("#isInsert").val(true);
			 }else{
				 $("#isInsert").val(false);
			 }
			
			$("#viewUploadFilePopUp").modal('show');
			$("#viewUploadFilePopUp").find("input:checkbox").prop('checked',true); 
	});
	
	$(document).on("click","#sameAsIl",function(){
		if($(this).is(":checked")){
			$("#xref_template").prop("disabled",true);	
		}
		else{
			$("#xref_template").prop("disabled",false);
		}
	});
	
	$(document).on("click","#uploadButton",function(){
		var process = template.validateFileUpload();		
		if(!process){
			return false;
		}
		var userId = $("#userID").val();
		if($("#sameAsIl").is(":checked"))
			$("#isSameAsIL").val("true");
		else
			$("#isSameAsIL").val("false");
				
		var formData = new FormData($("#uploadIlAndXrefTemplate")[0]);
		showAjaxLoader(true);
		template.uploadILAndXrefTemplate(formData,userId);
	});
	// download Il template
	var il_id='';
	$(document).on('click', '.downloadILTemplate', function() {
		$("#downloadILTemplate").modal("show");
		il_id=$(this).attr("data-ILID");
		var iLFileName = $(this).attr("data-IlFileName");
	})
	$(document).on('click', '#confirmDownloadILTemplate', function() {
		var userID = $("#userID").val();
		var templateType='';
		$("#downloadILTemplate").modal("hide");
		if($('#ilCsv').is(':checked')) {
			templateType="csv";	
		}
		window.location.href = adt.serviceContextPath+"/anvizentWServices/user/"+userID+"/package/downloadIlTemplate/"+il_id+"/"+templateType;
	});
	
	$(document).on('click', '.downloadXrefTemplate', function() {
		$("#downloadXrefILTemplate").modal("show");
		il_id=$(this).attr("data-ILID");
	})
	$(document).on('click', '#confirmDownloadXrefILTemplate', function() {
		var userID = $("#userID").val();
		var templateType='';
		$("#downloadXrefILTemplate").modal("hide");
		if($('#xrefilCsv').is(':checked')) {
			templateType="csv";	
		}
		window.location.href = adt.serviceContextPath+"/anvizentWServices/user/"+userID+"/package/downloadXRefIlTemplate/"+il_id+"/"+templateType;
	});
}

