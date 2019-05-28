var headers = {};
var viewIlSource = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			
	},
	updateViewSourceDetailsPoUp : function(result){
		$("#viewIlSourceDetails").empty();
		var mappingInfo = '<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">';
		
		for(var i=0;i<result.length;i++){
			var clientId=null;
			var iLId=null;
			var dLId=null;
			var packageId=null;
			var connectionMappingId=null;
			var delimeter = null;
			var filePath = null;
			var fileType= null;
			var isFirstRowHasColoumnNames= null;
			var typeOfCommand= null;
			var iLquery= null;
			var connectionName = null;
			var connectionId = null;
			var connectionType= null;
			var databaseName= null;
			var server= null;
			var username= null;
			var procedureParameters= null;
			var webserviceName = null;
			var previewClass = null;
			var disabled = '';
			var editSavedMappingButton='';
			 clientId=result[i].clientId != null ? result[i].clientId : "";
			 iLId=result[i].iLId != null ? result[i].iLId : "";
			 dLId=result[i].dLId != null ? result[i].dLId : "";
			 packageId=result[i].packageId != null ? result[i].packageId : "";
			 connectionMappingId=result[i].connectionMappingId != null ? result[i].connectionMappingId : "";
			if(result[i].isFlatFile && !result[i].isWebservice){
				
				 delimeter = result[i].delimeter != null ? result[i].delimeter.encodeHtml() : "";
				 filePath = result[i].filePath != null ? result[i].filePath.encodeHtml() : "";
				 fileType= result[i].fileType != null ? result[i].fileType.encodeHtml() : "";
				 isFirstRowHasColoumnNames= result[i].isFirstRowHasColoumnNames == true ? "Yes" : "No"
			     previewClass = 'flatFilePreviewStandard';
			}else if(!result[i].isFlatFile && !result[i].isWebservice){
				
				if(result[i].iLConnection != null){
					 connectionName = result[i].iLConnection.connectionName != null ? result[i].iLConnection.connectionName.encodeHtml() : "";
					 connectionId = result[i].iLConnection.connectionId != null ? result[i].iLConnection.connectionId : "";
					 connectionType= result[i].iLConnection.connectionType != null ? result[i].iLConnection.connectionType.encodeHtml() : "";
					 databaseName= result[i].iLConnection.database.name != null ? result[i].iLConnection.database.name.encodeHtml() : "";
					 server= result[i].iLConnection.server != null ? result[i].iLConnection.server.encodeHtml() : "";
					 username= result[i].iLConnection.username != null ? result[i].iLConnection.username.encodeHtml() : "";
					 disabled = result[i].iLConnection.webApp && !result[i].iLConnection.availableInCloud ? "disabled" : "";
					 
				}
				 typeOfCommand= result[i].typeOfCommand != null ? result[i].typeOfCommand.encodeHtml() : "";
				 iLquery= result[i].iLquery != null ? result[i].iLquery.encodeHtml() : "";
				 previewClass =  'databasePreviewStandard';
				 
			}  else if(!result[i].isFlatFile && result[i].isWebservice){
						webserviceName = result[i].webService.webserviceName.encodeHtml();
						previewClass = 'webservicePreviewStandard';
						iLquery= result[i].iLquery != null ? result[i].iLquery.encodeHtml() : "";
						fileType= result[i].fileType != null ? result[i].fileType.encodeHtml() : null;
					}
			 
				
			mappingInfo += '<div class="panel panel-default">'
								+'<div class="panel-heading" role="tab" id="heading'+i+'">'
									+'<h4 class="panel-title">'
									+'<a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse'+i+'" aria-expanded="true" aria-controls="collapse'+i+'">';
										if(result[i].isFlatFile && !result[i].isWebservice){								
											mappingInfo += filePath;
											
										}else if(!result[i].isFlatFile && !result[i].isWebservice){
											mappingInfo += connectionName;
											
										}else if(!result[i].isFlatFile && result[i].isWebservice){
											mappingInfo += webserviceName;
											if(fileType == null || fileType == 'csv'){
												editSavedMappingButton += '<button type="button" style="float:right;margin-top:-6px;margin-right:3px;" data-isWebservice="'+result[i].isWebservice+'" data-isJoinWebservice="'+result[i].joinWebService+'"  data-webserviceId="'+result[i].webService.web_service_id+'" data-mappingId="'+connectionMappingId+'" data-webserviceconId="'+result[i].webService.wsConId+'" class="btn btn-primary btn-sm editMappedHeadersForWebService" title="'+globalMessage['anvizent.message.text.editMappedHeaders']+'"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span></button>';
												}
										}
				//to make first one open and others close when opening the pop up 	 				
                   var collapseIn = i==0 ? "in": "";							
                   mappingInfo +='</a>';
                   mappingInfo += editSavedMappingButton ;
                     if(!result[i].isFlatFile && result[i].isWebservice){
                    	 if(result[i].joinWebService){
                    		 mappingInfo += '&nbsp;<button type="button" style="float:right;margin-top:-6px;margin-right:3px;" data-webserviceconId="'+result[i].webService.wsConId+'" data-dlId="'+dLId+'" data-iLId="'+iLId+'"  data-mappingId="'+connectionMappingId+'" class="btn btn-primary btn-sm  deleteIlSource" title="'+globalMessage['anvizent.package.label.delete']+'" ><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>'
                                            +'</h4></div>' 
      						                +'<div id="collapse'+i+'" class="panel-collapse collapse '+collapseIn+'" role="tabpanel" aria-labelledby="heading'+i+'">'
      						               +'<div class="panel-body">';
                    	   }else{
                    		   mappingInfo += '&nbsp;<button type="button" style="float:right;margin-top:-6px;margin-right:3px;" data-webserviceconId="'+result[i].webService.wsConId+'" data-dlId="'+dLId+'" data-iLId="'+iLId+'"  data-mappingId="'+connectionMappingId+'"  data-webserviceconId="'+result[i].webService.wsConId+'" class="btn btn-primary btn-sm  deleteIlSource" title="'+globalMessage['anvizent.package.label.delete']+'" ><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>'
		                                       +'&nbsp;<button type="button" style="float:right;margin-top:-6px;margin-right:3px;" data-webserviceconId="'+result[i].webService.wsConId+'" data-webserviceId="'+result[i].webService.web_service_id+'" data-mappingId="'+connectionMappingId+'" class="btn btn-primary btn-sm  '+previewClass+'" title="'+globalMessage['anvizent.package.button.preview']+'"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></button>' 
		                                     +'</h4></div>' 
								             +'<div id="collapse'+i+'" class="panel-collapse collapse '+collapseIn+'" role="tabpanel" aria-labelledby="heading'+i+'">'
								             +'<div class="panel-body">';
						     }
						 }else{
							 mappingInfo += '&nbsp;<button type="button" style="float:right;margin-top:-6px;margin-right:3px;" data-dlId="'+dLId+'" data-iLId="'+iLId+'"  data-mappingId="'+connectionMappingId+'" class="btn btn-primary btn-sm  deleteIlSource" title="'+globalMessage['anvizent.package.label.delete']+'" ><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button>'
		                       +'&nbsp;<button  type="button" style="float:right;margin-top:-6px;margin-right:3px;" data-webserviceId="'+result[i].webService.web_service_id+'" data-mappingId="'+connectionMappingId+'" class="btn btn-primary btn-sm  '+previewClass+'"  title="'+globalMessage['anvizent.package.button.preview']+'" '+disabled+' ><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></button>' 
		                       +'</h4></div>' 
							   +'<div id="collapse'+i+'" class="panel-collapse collapse '+collapseIn+'" role="tabpanel" aria-labelledby="heading'+i+'">'
							   +'<div class="panel-body">';
						 }
                   
                   
			if(result[i].isFlatFile && !result[i].isWebservice){
				mappingInfo += "<div class='row form-group '>"
									+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.fileType']+' :</label>'
									+"<div class='col-sm-8' >"
										+'<input type="text" class="form-control" disabled="disabled" value="'+fileType+'">'
									+'</div>'
								+'</div>'
								+"<div class='row form-group '>"
									+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.delimeter']+' :</label>'
									+"<div class='col-sm-8' >"
										+'<input type="text" class="form-control" disabled="disabled" value="'+delimeter+'">'
									+'</div>'
								+'</div>'
								+"<div class='row form-group ' style='display: none;'>"
									+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.firstRowHasColumnNames']+' :</label>'
									+"<div class='col-sm-8' >"
										+'<input type="text" class="form-control" disabled="disabled" value="'+isFirstRowHasColoumnNames+'">'
									+'</div>'
								+'</div>'
								+"<div class='row form-group '>"
									+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.file']+' :</label>'
									+"<div class='col-sm-8' >"
										+'<input type="text" class="form-control" disabled="disabled" value="'+filePath+'">'
									+'</div>'
								+'</div>';
				
			}else if(!result[i].isFlatFile && !result[i].isWebservice){
				mappingInfo += "<div class='row form-group '>"
									+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.connectionName']+' :</label>'
									+"<div class='col-sm-8' >"
										+'<input type="text" class="form-control" id="existingConnections" disabled="disabled" data-connectionId="'+connectionId+'" value="'+connectionName+'">'
									+'</div>'
								+'</div>'
								+"<div class='row form-group '>"
									+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.connectorType']+' :</label>'
									+"<div class='col-sm-8' >"
										+'<input type="text" class="form-control" disabled="disabled" value="'+databaseName+'">'
									+'</div>'
								+'</div>'
								+"<div class='row form-group '>"
									+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.connectionType']+' :</label>'
									+"<div class='col-sm-8' >"
										+'<input type="text" class="form-control" disabled="disabled" value="'+connectionType+'">'
									+'</div>'
								+'</div>'
								+"<div class='row form-group '>"
									+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.serverName']+' :</label>'
									+"<div class='col-sm-8' >"
										+'<input type="text" class="form-control" disabled="disabled" value="'+server+'">'
									+'</div>'
								+'</div>'
								+"<div class='row form-group '>"
									+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.userName']+' :</label>'
									+"<div class='col-sm-8' >"
										+'<input type="text" class="form-control" disabled="disabled" value="'+username+'">'
									+'</div>'
								+'</div>'
								+"<div class='row form-group '>"
									+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.typeOfCommand']+' :</label>'
										+"<div class='col-sm-6' >"
											+'<select class="form-control typeOfCommand"   disabled="disabled">'
											+'<option value="Query">'+typeOfCommand+'</option>'
										    +'</select>'
											+'</div>'
											+"<div class='col-sm-2'>"
											+'<button type="button" class="btn btn-primary btn-sm  editIlSourceDetails" '+disabled+'><span class="glyphicon glyphicon-edit" title="'+globalMessage['anvizent.package.label.edit']+'" aria-hidden="true"></span></button>'
											+'</div>'
								 +'</div>';
				if(typeOfCommand == "Query"){
					mappingInfo += "<div class='row form-group '>"
										+"<div class='col-sm-4'>"
										+"</div>"
										+"<div class='col-sm-8' >"
											+'<textarea class="form-control iLquery" id="iLqueryTextArea'+i+'"  rows="6" readonly="readonly" value="">'+iLquery+'</textarea>'
											+'<input class="form-control iLSp" id="iLqueryInput'+i+'" style="display:none">'
										+'</div>'
									+'</div>'
									+"<div class='row form-group '>"
									 +"<div class='col-sm-4'>" 
								    +'</div>' 
									 +"<div class='col-sm-8' >"
										 +'<input type="button" value="'+globalMessage['anvizent.package.button.validateQuerySP']+'"  id="checkQuerySyntax" style="display:none"  class="btn btn-primary btn-sm checkQuerySyntax">'
										 +' &nbsp;<input type="button" value="'+globalMessage['anvizent.package.button.preview']+'" id="checkTablePreview" style="display:none" class="btn btn-primary btn-sm checkTablePreview">'
									    +' &nbsp;<input type="button" value="'+globalMessage['anvizent.package.button.update']+'" id="updateILConnectionMapping" data-mappingId="'+connectionMappingId+'" style="display:none" class="btn btn-primary btn-sm updateILConnectionMapping">' 
									 +'</div>'
								+'</div>';
									
				}else{
					mappingInfo += "<div class='row form-group '>"
										+"<div class='col-sm-4'>"
										+"</div>"
										+"<div class='col-sm-8' >"
											+'<input class="form-control iLSp" id="iLqueryInput'+i+'"  readonly="readonly" value="'+iLquery+'">'
											+'<textarea class="form-control iLquery" id="iLqueryTextArea'+i+'"  style="display:none" rows="6"></textarea>' 
										+'</div>'
									+'</div>'
									+"<div class='row form-group '>"
									 +"<div class='col-sm-4'>" 
									    +'</div>' 
										 +"<div class='col-sm-8' >"
											 +'<input type="button" value="'+globalMessage['anvizent.package.button.validateQuerySP']+'"  id="checkQuerySyntax" style="display:none"  class="btn btn-primary btn-sm checkQuerySyntax">'
											 +' &nbsp;<input type="button" value="'+globalMessage['anvizent.package.button.preview']+'" id="checkTablePreview" style="display:none" class="btn btn-primary btn-sm checkTablePreview">'
										    +' &nbsp;<input type="button" value="'+globalMessage['anvizent.package.button.update']+'" id="updateILConnectionMapping" data-mappingId="'+connectionMappingId+'" style="display:none" class="btn btn-primary btn-sm updateILConnectionMapping">' 
										 +'</div>'
									+'</div>';
				}
			}else if(!result[i].isFlatFile && result[i].isWebservice){

				if(!result[i].joinWebService){
					mappingInfo += "<div class='row form-group '>"
						+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.webserviceName']+' :</label>'
						+"<div class='col-sm-8' >"
							+'<input type="text" class="form-control" disabled="disabled" title="'+webserviceName.encodeHtml()+'" value="'+webserviceName.encodeHtml()+'">'
						+'</div>'
					+'</div>'
					+"<div class='row form-group'>"
						+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.apiname']+' :</label>'
						+"<div class='col-sm-8' >"
							+'<input type="text" class="form-control" disabled="disabled" title="'+result[i].webService.apiName.encodeHtml()+'" value="'+result[i].webService.apiName.encodeHtml()+'">'
						+'</div>'
					+'</div>'
					+"<div class='row form-group '>"
						+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.anvizent.package.label.url']+'</label>'
						+"<div class='col-sm-8' >"
							+'<input type="text" class="form-control" disabled="disabled" title="'+result[i].webService.url.encodeHtml()+'" value="'+result[i].webService.url.encodeHtml()+'">'
						+'</div>'
					+'</div>'
					 +'<div class="row form-group">'
						+'<label class="col-sm-4 control-label">File Type:</label>'
					    +'<div class="col-sm-8">'
		                +'<input type="text" class="form-control" value="'+fileType == null ? 'csv' : fileType +'">'
		                +'</div>'
		            +'</div>'
					+"<div class='row form-group' style='display:none'>"
						+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.webserviceName']+':</label>'
						+"<div class='col-sm-8' >"
							+'<input type="text" class="form-control" disabled="disabled" title="'+result[i].webService.web_service_id+'" value="'+result[i].webService.web_service_id+'">'
						+'</div>'
					+'</div>';
				}else{
					mappingInfo += "<div class='joinWsApiInfoDiv'><div class='row form-group '>"
						+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.webserviceName']+' :</label>'
						+"<div class='col-sm-8' >"
							+'<input type="text" class="form-control" disabled="disabled" title="'+webserviceName+'" value="'+webserviceName+'">'
						+'</div>'
					+'</div>'
					+"<div class='row form-group'>"
						+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.apiname']+' :</label>'
						+"<div class='col-sm-8' >"
							+'<input type="text" class="form-control" disabled="disabled" title="'+result[i].webService.apiName.encodeHtml()+'" value="'+result[i].webService.apiName.encodeHtml()+'">'
						+'</div>'
					+'</div>'
					+"<div class='row form-group '>"
						+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.joinUrls']+':</label>'
						+"<div class='col-sm-8' >"
							+'<textarea class="form-control iLquery" id="iLqueryTextArea'+i+'"  rows="6" title="'+result[i].webserviceJoinUrls.encodeHtml()+'"  disabled="disabled" value="">'+result[i].webserviceJoinUrls.encodeHtml()+'</textarea>'
							+'</div>'
					+'</div>';

					if(fileType == 'json' || fileType == 'xml')
					{
						mappingInfo += '<div class="row form-group">'
							+'<label class="col-sm-4 control-label">File Type:</label>'
						    +'<div class="col-sm-8">'
			                +'<input type="text" class="form-control" value="'+fileType+'">'
			                +'</div>'
			                +'</div>';
					}else if(fileType == null || fileType == 'csv'){
						mappingInfo += 
							'<div class="row form-group">'
							+'<label class="col-sm-4 control-label">File Type:</label>'
						    +'<div class="col-sm-8">'
			                +'<input type="text" class="form-control" value="'+fileType == null ? 'csv' : fileType +'">'
			                +'</div>'
			                +'</div>'
							+'<div class="row form-group">'
							+'<label class="col-sm-4 control-label"> </label>'
						    +'<div class="col-sm-8">'
			                +'<button type="button" class="btn btn-primary btn-sm  editIlWsApiQuery" title="'+globalMessage['anvizent.package.label.editQuery']+'" style="float: right;"><span class="glyphicon glyphicon-edit"  aria-hidden="true"></span></button>'
			                +'</div>'
			                +'</div>'
							+"<div class='row form-group '>"
							+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.joinQuery']+'</label>'
							+"<div class='col-sm-8' >"
							+'<textarea class="form-control wsApiILQuery" id="iLqueryTextArea'+i+'"  title="'+iLquery+'" rows="6" readonly="readonly" value="">'+iLquery+'</textarea>'
							+'</div></div>';
					}
					mappingInfo += '<div class="row form-group">'
					+"<div class='col-sm-4'> </div>" 
					+"<div class='col-sm-8' >"
						 +'<input type="button"  value="'+globalMessage['anvizent.package.button.validate']+'"   style="display:none"  class="btn btn-primary btn-sm checkWsQuerySyntax">'  
					     +' &nbsp; <input type="button" value="'+globalMessage['anvizent.package.label.mapHeaders']+'" style="display:none" data-isWebservice="'+result[i].isWebservice.encodeHtml()+'"  data-webserviceconId="'+result[i].webService.wsConId+'" data-isJoinWebservice="'+result[i].joinWebService+'" data-webserviceId="'+result[i].webService.web_service_id+'" data-ilid="'+iLId+'"  data-mappingid="'+connectionMappingId+'" class="btn btn-primary btn-sm  editMappedQueryHeadersForWebService" title="'+globalMessage['anvizent.message.text.editMappedHeaders']+'"/>' 
					 +'</div>'
				   +'</div>'
					+"<div class='row form-group' style='display:none'>"
						+'<label class="col-sm-4 control-label">'+globalMessage['anvizent.package.label.webserviceName']+':</label>'
						+"<div class='col-sm-8' >"
							+'<input type="text" class="form-control" disabled="disabled" title="'+result[i].webService.web_service_id+'" value="'+result[i].webService.web_service_id+'">'
						+'</div>'
					+'</div></div>';
				}
			}
			mappingInfo += "<div class='row form-group '>"
			+ " <div class='col-sm-4'> </div><div class='col-sm-8'>" 
			 +'<div class="alert alert-danger iLInputMessage" style="display:none;"></div>'
			 +'<div class="alert alert-success successIlMessage" style="display:none;"></div>' 
		    +'</div> '
		     +'</div>'
			
			mappingInfo += '</div>'
						   +'</div>'
						   +'</div>';
				
				
		}
		 
		$("#viewIlSourceDetails").append(mappingInfo);
	},
	updateMappedHeadersForWebservice :  function(selectData){
		var userID = $("#userID").val();
		 var updateMappedHeadersForWebservice = "/app/user/"+userID+"/package/updateMappedHeadersForWebservice";
		 showAjaxLoader(true);
		   var myAjax = common.postAjaxCall(updateMappedHeadersForWebservice,'POST', selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    	  if(result != null){
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "SUCCESS") {
								 var  messages=[{
									  code : result.messages[0].code,
									  text : result.messages[0].text
								  }];
								 $("#fileMappingWithILPopUp").modal('hide');
								 common.showSuccessAlert(result.messages[0].text);
								 editMappedQueryHeadersForWebServiceDiv.hide();
								 checkWsQuerySyntaxDiv.hide();
								 $(".wsApiILQuery").attr("readonly","readonly");
			    			  }else{
			    				  common.showErrorAlert(result.messages[0].text);
			    			  }
		    		  }else{
		    			  common.showSuccessAlert(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    	  }
		    });
	},
	showMessage:function(text){
		$(".messageText").empty();
		$(".successMessageText").empty();
		$(".messageText").html(text);
	  $(".message").show();
	  setTimeout(function() { $(".message").hide(); }, 10000);
},
validateDefaultValue : function(obj) {
	var msg = globalMessage['anvizent.package.message.invalidData'];
	var status = true;
	
	if (obj) {
	 
		var val = $.trim(obj.value);
		common.clearcustomsg(obj);
		if (val !== '') {
			var input = $(obj), 
			row =  input.closest('tr'),
			datatype = row.find('td[data-dtype]').data('dtype'),
			dataColSize = row.find('td[data-colsize]').data('colsize');
			if(val.length > dataColSize && (datatype !== 'FLOAT' || datatype !== 'DECIMAL' || datatype !== 'DECIMAL UNSIGNED')){				
				common.showcustommsg(obj, globalMessage['anvizent.package.message.defaultValueLengthIsExceeded']);
				status = false;								
			}
			else{
				if (datatype === 'INT' || datatype === 'BIGINT' || datatype== 'INT UNSIGNED') {
					var isInt = common.isInteger(val);
					if (!isInt) {
						common.showcustommsg(obj, msg);
						status = false;
					}
				}
				else if (datatype === 'FLOAT' || datatype === 'DECIMAL' || datatype === 'DECIMAL UNSIGNED') {
					if(!val.match("^([-]?\\d*\\.\\d*)$")){
						common.showcustommsg(obj, msg);
						status = false;
					}
				}
				else if (datatype === 'BIT') {
					if (val !== '0' && val !== '1') {
						common.showcustommsg(obj, msg);
						status = false;
					}
				}
				else if (datatype === 'DATETIME') {
					var regEx = /^\d{4}\-(0?[1-9]|1[012])\-(0?[1-9]|[12][0-9]|3[01])$/;
					if(!val.match(regEx)){
						common.showcustommsg(obj, msg);
						status = false;
					}
				}
			}	
		}
		else {
			this.value = val;
		}
	}
	return status;
},

editMappedHeadersForWebService : function(userID,selectedData) {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	headers[header] = token;
showAjaxLoader(true);
	var editMappedHeadersForWebService_url = "/app/user/"+userID+"/package/editMappedHeadersForWsApi";
	var myAjax = common.postAjaxCall(editMappedHeadersForWebService_url,'POST',selectedData,headers);
	    myAjax.done(function(result) {
	    	showAjaxLoader(false);
	    	$("#mapFileWithIL").val('Map File Headers & Update').prop('disabled',false);
			if(result != null){
			 
    		  if(result.hasMessages){ 
    			  if(result.messages[0].code == "ERROR") {
						 var  messages=[{
							  code : result.messages[0].code,
							  text : result.messages[0].text
						  }];
						 common.showErrorAlert(result.messages[0].text);
						 return false;
    			  }
    			}	 
				$("#fileMappingWithILTable").find('th.iLName').text($("#iLName").val());
				$("#fileMappingWithILTable").find('th.originalFileName').text(result.object["originalFileName"]);
				fileMappingWithILTable.updateFileMappingWithILTable(result,"viewIl");
			}else{
				 if(result.messages[0].code == "ERROR") {
						 var  messages=[{
							  code : result.messages[0].code,
							  text : result.messages[0].text
						  }];
					     common.showErrorAlert(result.messages[0].text);
						 return false;
    			  }
    			}	
				 
			 
	    });
},

displayDatabaseSchemaBasedOnConnectorId : function(connector_id,connectionId, mainDiv,protocal){
	var userID = $("#userID").val();  
	if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
		var dbSchemaSelection = $("#dbSchemaSelectionDivForSqlServer").clone().removeAttr('id').prop("id","dbSchemaSelection").show();
		$('#defualtVariableDbSchema',mainDiv).append(dbSchemaSelection); 
		showAjaxLoader(true);
		var url_getDbSchemaVaribles ="/app/user/"+userID+"/package/getDbSchemaVaribles/"+connector_id+""; 
		 var myAjax = common.loadAjaxCall(url_getDbSchemaVaribles,'GET','',headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    	  if(result != null && result.hasMessages){ 
		    			  if(result.messages[0].code == "ERROR") { 
		    				  $("#defualtVariableDbSchema").hide();
		    				  $("#replaceShemas").hide();
		    				  common.showErrorAlert(result.messages[0].text);
		    				  return false;
		    			  }  
		    		   else if(result.messages[0].code == "SUCCESS"){
		    			   var dbVariable='';
		    			   var schemaVariable = '';
		    			   if(result.object != null){
		    				   $('#dbVariable,#schemaVariable').empty();
		    				   dbVariable ='<option value="{db0}">'+globalMessage['anvizent.package.label.selectdbvariable']+'</option>'; 
		    				   schemaVariable ='<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
		    				   $.each( result.object, function( key, value ) {
		    					   dbVariable += '<option value='+key+'>'+key+'</option>';
		    					   schemaVariable += '<option value='+value+'>'+value+'</option>';
			    				 });
		    				   $('#dbVariable').append(dbVariable);
		    				   $('#schemaVariable').append(schemaVariable);
		    				   viewIlSource.getAllSchemasFromDatabase(connectionId,connector_id,mainDiv,protocal) ;
		    			   }else{
		    				   common.showErrorAlert(result.messages[0].text);
		    				   return false;
		    			   }
		    			 
		    			  } 
		    		    
		    	  } else{
			    		var messages = [ {
			    			code : globalMessage['anvizent.message.error.code'],
			    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
			    		} ];
			    		common.displayMessages(messages);
			    	}   	  
		    }); 
		   
		}
	 
	 else if(protocal.indexOf('mysql') != -1 || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1 || protocal.indexOf('sforce') != -1   || protocal.indexOf('as400') != -1   || protocal.indexOf('postgresql') != -1  ||  protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1){
		 
         var dbSchemaSelectionDiv = $("#dbSchemaSelectionDivForNotSqlServer").clone().removeAttr('id').prop("id","dbSchemaSelection").show();
			$('#defualtVariableDbSchema',mainDiv).append(dbSchemaSelectionDiv); 
		 
			showAjaxLoader(true);
			var url_getDbSchemaVaribles ="/app/user/"+userID+"/package/getDbSchemaVaribles/"+connector_id+""; 
			 var myAjax = common.loadAjaxCall(url_getDbSchemaVaribles,'GET','',headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null && result.hasMessages){ 
			    			  if(result.messages[0].code == "ERROR") { 
			    				  $("#defualtVariableDbSchema").hide();
			    				  $("#replaceShemas").hide();
			    				  common.showErrorAlert(result.messages[0].text);
			    				  return false;
			    			  }  
			    		   else if(result.messages[0].code == "SUCCESS"){
			    			   var schemaVariable = '';
			    			   if(result.object != null){
			    				   $('#schemaVariable').empty();
			    				   schemaVariable ='<option value="{schema0}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
			    				   $.each( result.object, function( key, value ) {
			    					   schemaVariable += '<option value='+value+'>'+value+'</option>';
				    				 });
			    				   $('#schemaVariable').append(schemaVariable);
			    				   
			    				   viewIlSource.getAllSchemasFromDatabase(connectionId,connector_id,mainDiv,protocal) ;
			    				   
			    			   }else{
			    				   common.showErrorAlert(result.messages[0].text);
			    				   return false;
			    			   }
			    			 
			    			  } 
			    	  } else{
				    		var messages = [ {
				    			code : globalMessage['anvizent.message.error.code'],
				    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
				    		} ];
				    		common.displayMessages(messages);
				    	}   	  
			    }); 
	       }
	 $("#defualtVariableDbSchema,#replaceShemas,#replace,#replaceAll",mainDiv).show();
},
getAllSchemasFromDatabase: function(connectionId,connector_id,mainDiv,protocal){
	var userID = $("#userID").val();
	showAjaxLoader(true);
	var url_getDbSchemaVaribles1 ="/app/user/"+userID+"/package/getAllSchemasFromDatabase/"+connectionId+""; 
	 var myAjax = common.loadAjaxCall(url_getDbSchemaVaribles1,'GET','',headers);
	    myAjax.done(function(dbResult) {
	    	showAjaxLoader(false);
	    	  if(dbResult != null){ 
	    		 
	    		  if(dbResult.hasMessages){
	    			  if(dbResult.messages[0].code == "ERROR") { 
	    				 
	    				  common.showErrorAlert(dbResult.messages[0].text);
	    				  return false;
	    			  }  
	    		   else if(dbResult.messages[0].code == "SUCCESS"){
	    			   
	    			   var dbName='';
	    			   if(dbResult.object != null){
	    				   $('#dbName',mainDiv).empty();
	    				   if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
	    					   dbName ='<option value="{dbName}">'+globalMessage['anvizent.package.label.selectdbname']+'</option>';
	    				   }else if(protocal.indexOf('mysql') != -1 || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1 || protocal.indexOf('sforce') != -1   || protocal.indexOf('as400') != -1   || protocal.indexOf('postgresql') != -1  ||  protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1 ){
	    					   dbName ='<option value="{dbName}">'+globalMessage['anvizent.package.label.selectschemaname']+'</option>';
	    				   }
	    				  
	    				   $.each( dbResult.object, function( key, value ) {
	    					   dbName += '<option value='+value+'>'+value+'</option>';
		    				 });
	    				  
	    				   $('#dbName',mainDiv).append(dbName);
	    			  
	    			   }else{
	    				   common.showErrorAlert(result.messages[0].text);
	    				   return false;
	    			   }
	    			 
	    			  } 
	    		  }	  
	    	  }    	  
	    }); 
   },
   
   getToggleStatus :function(){
	   debugger
	   $(".toggle-demo").each(function(){
			var isActiveRequired = $(this).attr("data-activeStatus");
			if (isActiveRequired == "true") {
				 $(this).bootstrapToggle('on');
				 $(this).parents(".toggle-col").find(".databasePreviewStandard").prop("disabled",false);
				 $(this).parents(".toggle-col").find(".flatFilePreviewStandard").prop("disabled",false);
				 $(this).parents(".toggle-col").find(".deleteIlSource").prop("disabled",false);
			} else {
				$(this).bootstrapToggle('off');
				$(this).parents(".toggle-col").find(".databasePreviewStandard").prop("disabled",true);
				$(this).parents(".toggle-col").find(".flatFilePreviewStandard").prop("disabled",true);
				$(this).parents(".toggle-col").find(".deleteIlSource").prop("disabled",true);
			}
		});
   },
}

if($('.viewIlSource-page').length || $('.xRefDetails-page').length){
	viewIlSource.initialPage();
	var mainDiv = $($(this).closest("div.panel"));

	viewIlSource.getToggleStatus();
	
	var activeStatus = $("#activeStatus").val();
    if(activeStatus == "true"){
    	 $('#toggle-demo').bootstrapToggle('off');
    }else{
    	$('#toggle-demo').bootstrapToggle('on');
    }
   
	
	$("input[name='isIncrementalUpdate']").each(function(){
		if ( this.checked ) {
			var mainDiv = $($(this).closest("div.panel"));
			$(".max_date_query", mainDiv).show();
		}
	});
	 
	var iLId = "";
    var dLId =  "";
    var connectionMappingId= "";
	$(document).on('click', 'button.deleteIlSource', function(){
		iLId = $(this).attr("data-iLId");
	    dLId =  $(this).attr("data-dlId");
	    connectionMappingId= $(this).attr("data-mappingId");
		$("#deleStandardSourceFileAlert").modal('show');
	});
	
	
	$("#confirmDeleteStandardSource").on('click', function(){
	    var userID = $("#userID").val();
	    var packageId = $("#packageId").val();
	    $("#deleStandardSourceFileAlert").modal('hide');
	    showAjaxLoader(true);
	    var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
	    var url_deleteIlSource = "/app/user/"+userID+"/package/deleteIlSource/"+connectionMappingId+"/"+packageId+"/"+iLId+"/"+dLId+"";
	    var myAjax = common.loadAjaxCall(url_deleteIlSource,'POST','',headers);
	    myAjax.done(function(result) {
	    	
	    	  if(result != null){
	    		  if(result.hasMessages){
	    			  if(result.messages[0].code == "ERROR") {
	    				  showAjaxLoader(false); 
							 var  messages=[{
								  code : result.messages[0].code,
								  text : result.messages[0].text
							  }];
							 
							 $("#popUpMessageForDeleteILSource").empty();
							 $("#popUpMessageForDeleteILSource").append("<div class='alert alert-danger'>"+result.messages[0].text+"</div>");
							 $('#messagePopUpForDeleteIlSource .back').hide();
							 $("#messagePopUpForDeleteIlSource").modal('show');
		    				  return false;
		    			  }
	    		  }
	    		  
	    		     $("#popUpMessageForDeleteILSource").empty();
					 $("#popUpMessageForDeleteILSource").append("<div class='alert alert-success'>"+result.messages[0].text+"</div>");
					 $("#messagePopUpForDeleteIlSource .closeButton").hide();
					 $("#messagePopUpForDeleteIlSource").modal('show');    
					 showAjaxLoader(false);
	    		  /*showAjaxLoader(false);
	    		  var url_getILConnectionMappingInfo = "/app/user/"+userID+"/package/getILsConnectionMappingInfo/"+iLId+"/"+dLId+"/"+packageId+"";
	  		      var myAjax = common.loadAjaxCall(url_getILConnectionMappingInfo,'GET','',headers);
	  		      myAjax.done(function(result) {
	  		    	  if(result != '' && result.hasMessages){
	  		    		if(result.messages[0].code == "SUCCESS"){
	  		    		viewIlSource.updateViewSourceDetailsPoUp(result.object);
	  		    		}else{
	  		    			common.displayMessages(result.messages);
	  		    		}
	  		    	  } 
	  		    		else{
	  		    		$("#viewIlSourceDetails").empty();
	  		    	  }
	  		    }); */ 
	    	  }
	    }); 
	 	}); 
	$(document).on('click', '.databasePreviewStandard', function() {
	 debugger
		var userID = $("#userID").val(); 
		var packageId = $("#packageId").val();
		var iLName = $("#iLName").val();
		var mappingId = $(this).attr("data-mappingId");
		var query = $(this).attr("data-query");
			var selectData ={
					packageId : packageId,
					connectionMappingId : mappingId
			}

			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			showAjaxLoader(true);
			var url_checkQuerySyntax = "/app/user/"+userID+"/package/getTablePreview";
			 var myAjax = common.postAjaxCall(url_checkQuerySyntax,'POST', selectData,headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null){
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") {
			    				  $('.Ilmessage').show();
								  $('.Ilmessage').empty();
					    		  $('.Ilmessage').append(result.messages[0].text);
					    		  setTimeout(function() { $(".Ilmessage").hide().empty(); }, 10000);
				    			  }  
			    		  }
			    		  $("#viewDeatilsPreviewPopUp").modal('show');
			    		  $("#viewDeatilsPreviewPopUpHeader").text(iLName);
			    	  var list = result.object;
			    	  if(list != null && list.length > 0){
			    		  var tablePreview='';
			    		  var colCount = 0;
				    	  $.each(list, function (index, row) {
				    		  
				    		  tablePreview+='<tr>';
				    		  $.each(row, function (index1, column) {
				    			  tablePreview += (index == 0 ? '<th>'+column+'</th>' : '<td>'+column+'</td>');
				    			  colCount = colCount + 1 ;
				    		  });
				    		 tablePreview+='</tr>'; 
				    		});
				    	  $(".viewDeatilsPreview").empty();
				    	  if(list.length<2){
				    		  tablePreview+='<tr>';
				    		  tablePreview+='<td colspan = "'+colCount+'" >';
				    		  tablePreview+=globalMessage['anvizent.package.label.noRecordsAvailable'];
				    		  tablePreview+='</td>'; 
				    		  tablePreview+='</tr>'; 
				    	  } 
				    	  $(".viewDeatilsPreview").append(tablePreview);
				    	  }
			    	  	else{
			    		  $(".viewDeatilsPreview").empty();
			    		  $(".viewDeatilsPreview").append(globalMessage['anvizent.package.label.noRecordsAvailableInTable']);
			    	  	} 
				    	 
			    	  }
			    	  
			    });
			
		
	});
	
	$(document).on('click', '.flatFilePreviewStandard', function() {
		var userID = $("#userID").val();
		var packageId = $("#packageId").val();
		var mappedId = $(this).attr("data-mappingId");
		var iLName = $("#iLName").val();
			showAjaxLoader(true);
			var url_checkQuerySyntax = "/app/user/"+userID+"/package/getFlatFilePreview/"+packageId+"/"+mappedId;
			console.log("url_checkQuerySyntax",url_checkQuerySyntax);
			 var myAjax = common.loadAjaxCall(url_checkQuerySyntax,'GET','',headers);
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null){
			    		  if(result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") {
			    				  $('.iLInputMessage').show();
								  $('.iLInputMessage').empty();
					    		  $('.iLInputMessage').append(result.messages[0].text);
					    		  setTimeout(function() { $(".iLInputMessage").hide().empty(); }, 10000);
				    			  }else if(result.messages[0].code == "SUCCESS"){
					    		  $("#viewDeatilsPreviewPopUp").modal('show');
					    		  $("#viewDeatilsPreviewPopUpHeader").text(iLName);
						    	  var list = result.object;
						    	  if(list != null && list.length > 0){
						    		  var tablePreview='';
						    		  var colCount = 0;
							    	  $.each(list, function (index, row) {
							    		  tablePreview+='<tr>';
							    		  $.each(row, function (index1, column) {
							    			  tablePreview += (index == 0 ? '<th>'+column+'</th>' : '<td>'+column+'</td>');
							    			  colCount = colCount + 1 ;
							    		  });
							    		 tablePreview+='</tr>'; 
							    		});
							    	  $(".viewDeatilsPreview").empty();
							    	  if(list.length<2){
							    		  tablePreview+='<tr>';
							    		  tablePreview+='<td colspan = "'+colCount+'" >';
							    		  tablePreview+=globalMessage['anvizent.package.label.noRecordsAvailable'];
							    		  tablePreview+='</td>'; 
							    		  tablePreview+='</tr>'; 
							    	  }
							    	  $(".viewDeatilsPreview").append(tablePreview);
							    	  }
							    	  else{
							    		  $(".viewDeatilsPreview").empty();
							    		  $(".viewDeatilsPreview").append(globalMessage['anvizent.package.label.noRecordsAvailable']);
							    	  } 
				    			  }  
			    		  }
			    	
			    	  }
			    	  
			    });
	});
	
	 $("#viewIlSourceDetails").find("panel").each(function(){				 
	        var id = $(this).attr("id");
	        common.clearValidations(["#"+id]);
			if($(this).val() == ''){
				common.showcustommsg("#"+id, "Please Choose file","#"+id);
				validStatus=false;
			}else{
				  var fileExtension = $(this).val().replace(/^.*\./, '');
  		      if(!(fileExtension == 'jar' || fileExtension == 'txt' )) {
  		    	common.showcustommsg("#"+id, globalMessage['anvizent.package.label.pleaseChooseEitherTxtorJarFile'],"#"+id);
  		    	validStatus=false;
  		     } 
			}
		});
	 
	 $(document).on("click","#il_incremental_update",function(){
		 console.log("Hello")
		 var mainDiv = $($(this).closest("div.panel"));
		 if ( !this.checked ) {
			 $(".max_date_query",mainDiv).hide();
			 $("#maxDatequery",mainDiv).val("");
		 } else {
			 $(".max_date_query",mainDiv).show();
		 }
	 });
	$(document).on('click', '.editIlSourceDetails', function() {
		var mainDiv = $($(this).closest("div.panel"));
		$(".preBuildQuery",mainDiv).prop("disabled",false);
		$("#maxDatequery",mainDiv).prop("readonly","");;
		$("#il_incremental_update",mainDiv).prop("disabled",false);
		$(".typeOfCommand",mainDiv).prop("disabled",true);
		$(".iLSp",mainDiv).prop("readonly","readonly");;
		$(".iLquery",mainDiv).prop("readonly","readonly");;
		 $(".checkTablePreview",mainDiv).hide(); 
		 $(".checkQuerySyntax",mainDiv).hide(); 
		 $(".updateILConnectionMapping",mainDiv).hide(); 
		
		//mainDiv.find(".typeOfCommand").prop("disabled",false);
		var typeOfCommand= mainDiv.find(".typeOfCommand").val();
		mainDiv.find("#checkQuerySyntax").show();
		var typeOfCommandOption= mainDiv.find(".typeOfCommand");
		
		if( typeOfCommand == 'Query'){
			mainDiv.find(".iLquery").prop("readonly","");;
			mainDiv.find(".typeOfCommand").empty();
			typeOfCommandOption.append('<option value="Query">'+globalMessage['anvizent.package.label.query']+'</option>');
			typeOfCommandOption.append('<option value="Stored Procedure">'+globalMessage['anvizent.package.label.storedProcedure']+'</option>');
		}
		 if(typeOfCommand == 'Stored Procedure'){
			mainDiv.find(".iLSp").prop("readonly","");;
			mainDiv.find(".typeOfCommand").empty();
			typeOfCommandOption.append('<option value="Stored Procedure">'+globalMessage['anvizent.package.label.storedProcedure']+'</option>');
			typeOfCommandOption.append('<option value="Query">'+globalMessage['anvizent.package.label.query']+'</option>');
		}
		 
	});
	 $(document).on('change', '.typeOfCommand', function() {
		 $(".max_date_query").hide();
		 $("#updateILConnectionMapping").hide();
		 $("#checkTablePreview").hide();
		 var typeOfCommand = $(this).closest("div.panel").find(".typeOfCommand").val(); 
		   $(".iLSp").prop("readonly","");;
		   $(".iLquery").prop("readonly","");;
		 if(typeOfCommand  === "Query"){
		     var queryOrSp=$(this).closest("div.panel").find(".iLquery").attr('id');
			 common.clearValidations(["#"+queryOrSp]);
			$(this).closest("div.panel").find(".iLSp").hide();
			$(this).closest("div.panel").find(".iLquery").show();
			 $(".iLquery").prop("readonly","readonly");;
			 $(".iLSp").prop("readonly","readonly");;
			 $(this).closest("div.panel").find(".iLquery").prop("readonly","");;
		}
		if(typeOfCommand  === "Stored Procedure"){
		     var queryOrSp=$(this).closest("div.panel").find(".iLquery").attr('id');
			 common.clearValidations(["#"+queryOrSp]);
			$(this).closest("div.panel").find(".iLSp").show();
			$(this).closest("div.panel").find(".iLquery").hide();
			$(".iLquery").prop("readonly","readonly");;
			$(".iLSp").prop("readonly","readonly");;
			 $(this).closest("div.panel").find(".iLSp").prop("readonly","");;
			 
		} 
		 
	}); 
	
	// validate query table Preview
		$(document).on('click', '#checkTablePreview', function() {
			var userID = $("#userID").val(); 
			var connectionId =  $(this).closest("div.panel").find("#existingConnections").attr('data-connectionId');
			 var typeOfCommand = $(this).closest("div.panel").find(".typeOfCommand").val(); 
			 var queryOrSp = '';
			 var packageId = $("#packageId").val();
			if(typeOfCommand  === "Query"){
				queryOrSp=$(this).closest("div.panel").find(".iLquery").val();
			}
			  if(typeOfCommand  === "Stored Procedure"){
				  queryOrSp=$(this).closest("div.panel").find(".iLSp").val();
			}
		 
			if( queryOrSp != '') {
				
				var selectData ={
						packageId : packageId,
						iLConnection : {
							connectionId : connectionId
						},
						iLquery : queryOrSp,
						typeOfCommand : typeOfCommand
				}
			 
				showAjaxLoader(true);
				var url_checkQuerySyntax = "/app/user/"+userID+"/package/getTablePreview";
				 var myAjax = common.postAjaxCall(url_checkQuerySyntax,'POST', selectData,headers);
				    myAjax.done(function(result) {
				    	showAjaxLoader(false);
				    	  if(result != null){
				    		  if(result.hasMessages){
				    			  if(result.messages[0].code == "ERROR") {
				    				  $('.iLInputMessage').show();
					    			  $('.iLInputMessage').empty();
									  $('.iLInputMessage').append(result.object);
						    		  setTimeout(function() { $(".iLInputMessage").hide().empty(); }, 10000);
					    			  return false;
					    			  }  
				    		  }
				    		  $("#tablePreviewPopUp").modal('show');
				    	 
				    	  var list = result.object;
				    	  if(list != null && list.length > 0){
				    		  var tablePreview='';
				    		  var colCount = 0;
					    	  $.each(list, function (index, row) {
					    		  
					    		  tablePreview+='<tr>';
					    		  $.each(row, function (index1, column) {
					    			  tablePreview += (index == 0 ? '<th>'+column+'</th>' : '<td>'+column+'</td>');
					    			  colCount = colCount + 1 ;
					    		  });
					    		 tablePreview+='</tr>'; 
					    		});
					    	  $(".tablePreview").empty();
					    	  if(list.length<2){
					    		  tablePreview+='<tr>';
					    		  tablePreview+='<td colspan = "'+colCount+'" >';
					    		  tablePreview+=globalMessage['anvizent.package.label.noRecordsAvailable'];
					    		  tablePreview+='</td>'; 
					    		  tablePreview+='</tr>'; 
					    	  }
					    	  $(".tablePreview").append(tablePreview);
					    	  }
					    	  else{
					    		  $(".tablePreview").empty();
					    		  $(".tablePreview").append(globalMessage['anvizent.package.label.noRecordsAvailableInTable']);
					    	  } 
				    	  }
				    	  
				    });
				
			} else {

				if(typeOfCommand  === "Query"){
					
					 queryOrSp=$(this).closest("div.panel").find(".iLquery").attr('id');
					 common.clearValidations(["#"+queryOrSp]);
					 common.showcustommsg("#"+queryOrSp, globalMessage['anvizent.package.label.shouldNotBeEmpty'],"#"+queryOrSp);
				}
				  if(typeOfCommand  === "Stored Procedure"){
					  queryOrSp=$(this).closest("div.panel").find(".iLSp").attr('id');
					  common.clearValidations(["#"+queryOrSp]);
					  common.showcustommsg("#"+queryOrSp, globalMessage['anvizent.package.label.shouldNotBeEmpty'],"#"+queryOrSp);
				}
			 
				 
			
			}
		});
		$(document).on('click', '#updateILConnectionMapping', function(){
			var validateBtn = $(this);
			var mainDiv = $($(this).closest("div.panel"));
			var packageId = $("#packageId").val();
			var userID = $("#userID").val(); 
			var connectionId =  mainDiv.find("#existingConnections").attr('data-connectionId');
			var typeOfCommand = mainDiv.find(".typeOfCommand").val(); 
			var connectionMappingId =  $(this).attr('data-mappingid');
			var isIncrementalUpdate = mainDiv.find("input[name='isIncrementalUpdate']").is(":checked") ? true : false;
			var maxDateQuery = mainDiv.find('#maxDatequery').val();
			var queryOrSp = '';
			 
				if(typeOfCommand  === "Query"){
					queryOrSp=$(this).closest("div.panel").find(".iLquery").val();
				}
				  if(typeOfCommand  === "Stored Procedure"){
					  queryOrSp=$(this).closest("div.panel").find(".iLSp").val();
				}
			  var selectData={
					   iLConnection:{
						   connectionId : connectionId, 
					   },
					   connectionMappingId:connectionMappingId,
					   typeOfCommand : typeOfCommand,
					   iLquery : queryOrSp,
					   maxDateQuery : maxDateQuery,
					   packageId : packageId,
					   isIncrementalUpdate : isIncrementalUpdate
			   };
				 var url_updateILConnectionMapping = "/app/user/"+userID+"/package/updateIlSource";
                 showAjaxLoader(true);
				   var myAjax = common.postAjaxCall(url_updateILConnectionMapping,'POST', selectData,headers);
				    myAjax.done(function(result) {
				    	showAjaxLoader(false);
				    	  if(result != null){
				    		  if(result.hasMessages){
				    			  if(result.messages[0].code == "ERROR") {
						    		  validateBtn.closest("div.panel").find('.iLInputMessage').show().empty().append(result.messages[0].text);
						    		  setTimeout(function() {validateBtn.closest("div.panel").find(".iLInputMessage").hide().empty(); }, 10000);
					    			  return false;
					    			  } else if(result.messages[0].code == "SUCCESS") { 
							    		  validateBtn.closest("div.panel").find('.successIlMessage').show().empty().append(result.messages[0].text);
							    		  setTimeout(function() {validateBtn.closest("div.panel").find(".successIlMessage").hide().empty(); }, 10000);
							    			if(typeOfCommand  === "Query"){
												 validateBtn.closest("div.panel").find(".iLquery").prop("readonly","readonly");;
												 $("#maxDatequery",validateBtn.closest("div.panel")).prop("readonly","readonly");;
												 validateBtn.closest("div.panel").find(".typeOfCommand").prop("disabled",true);
												 $(".checkTablePreview").hide(); 
												 $(".checkQuerySyntax").hide(); 
												 $(".updateILConnectionMapping").hide();
											}
											  if(typeOfCommand  === "Stored Procedure"){
												  queryOrSp=validateBtn.closest("div.panel").find(".iLSp").prop("readonly","readonly");;
												  validateBtn.closest("div.panel").find(".typeOfCommand").prop("disabled",true);
												  $(".checkTablePreview").hide(); 
												  $(".checkQuerySyntax").hide(); 
												  $(".updateILConnectionMapping").hide();
											}
					    			  }
				    		  } 
				    	  }
				    });
				    $("#defualtVariableDbSchema,#replaceShemas,#replace,#replaceAll",mainDiv).hide();
				    mainDiv.find(".preBuildQuery,#il_incremental_update").prop("disabled",true);
		});
		$(document).on('keyup', '.iLSp', function(){
			$(this).closest("div.panel").find('#updateILConnectionMapping').hide();
			$(this).closest("div.panel").find('#checkTablePreview').hide();
		});
		$(document).on('keyup', '.iLquery', function(){
			$(this).closest("div.panel").find('#updateILConnectionMapping').hide();
			$(this).closest("div.panel").find('#checkTablePreview').hide();
		});
		
		$(document).on("click",".webservicePreviewStandard",function(){ 	
			
		var userID = $("#userID").val(); 
  		var webserviceConId = $(this).attr('data-webserviceconId');
  		var ilId = $("#ilId").val();
  	    var mappingId = $(this).attr('data-mappingid');
  		var packageId = $("#packageId").val();  
  		var url_previewWebService = '';
  			url_previewWebService ="/app/user/"+userID+"/package/standardWsPreview/"+webserviceConId+"/"+ilId+"/"+mappingId+"/"+packageId; 
  		showAjaxLoader(true);
		var myAjax =  common.loadAjaxCall(url_previewWebService,'GET','',headers);
	    myAjax.done(function(result) { 
	    	showAjaxLoader(false);
	    	  if(result != null && result.hasMessages){
	    			  if(result.messages[0].code == "ERROR") {
		    			  common.showErrorAlert(result.messages[0].text);
		    			  return false;
		    			  }  else if(result.messages[0].code == "SUCCESS"){
		    				  $("#viewDeatilsPreviewPopUp").modal('show');
		    				  var list = result.object;
					    	   
					    	  if(list != null && list.length > 0){

						    		  if(list.length == 1){
					    			  $(".viewDeatilsPreview").empty();
						    		  $(".viewDeatilsPreview").append("No records available in web service.");
						    		  return false;
					    		     }
					    		  
					    		  var tablePreview='';
					    		  var colCount = 0;
						    	  $.each(list, function (index, row) {

						    		  tablePreview+='<tr>';
						    		  $.each(row, function (index1, column) {
						    			 
						    			  tablePreview += (index == 0 ? '<th>'+index1+'</th>' : '<td>'+column+'</td>');
						    			  colCount = colCount + 1 ;
						    		 
						    		  });
						    		 tablePreview+='</tr>'; 
						    		});
						    	    $(".viewDeatilsPreview").empty();
						    	    if(list.length<2){
							    		  tablePreview+='<tr>';
							    		  tablePreview+='<td colspan = "'+colCount+'" >';
							    		  tablePreview+=globalMessage['anvizent.package.label.noRecordsAvailable'];
							    		  tablePreview+='</td>'; 
							    		  tablePreview+='</tr>'; 
							    	  }
							    	  $(".viewDeatilsPreview").append(tablePreview);
						    	  }
						    	  else{
						    		  $(".viewDeatilsPreview").empty();
						    		  $(".viewDeatilsPreview").append(globalMessage['anvizent.package.label.noRecordsAvailableInTable']);
						    	  } 
					    	  }  
		    			  
	    		  }else{
	  	    		var messages = [ {
		    			code : globalMessage['anvizent.message.error.code'],
		    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
		    		} ];
		    		common.displayMessages(messages);
		    	}
	    });
	  
  	});	
		var connectionMappingId = null;
		var ilWsQuery = null;
		$(document).on("click",".editMappedHeadersForWebService",function(){ 
			
			var userID = $("#userID").val(); 
	  		var webserviceConId = $(this).attr('data-webserviceconId');
	  		var ilId = $("#ilId").val();
	  		var packageId = $("#packageId").val();  
	  		connectionMappingId = $(this).attr('data-mappingid');
	  		ilWsQuery =  $(this).parents(".panel").find(".wsApiILQuery").val(); 
	  		var joinWebservice = $(this).attr('data-isjoinwebservice');
	  		var joinWebserviceStatus = (joinWebservice === 'true') ? true : false;
	  		ilWsQuery = joinWebserviceStatus ? ilWsQuery : null;
	  		var selectedData ={
	  				iLId : ilId,
	  				connectionMappingId:connectionMappingId,
	  				joinWebService:joinWebserviceStatus,
	  				packageId:packageId,
	  				iLquery:ilWsQuery,
	  				wsConId:webserviceConId
	  		}
	  		 
	  		viewIlSource.editMappedHeadersForWebService(userID,selectedData);
 			  
 		}); 
	 
		var editMappedQueryHeadersForWebServiceDiv = null;
		var checkWsQuerySyntaxDiv = null;
		$(document).on("click",".editMappedQueryHeadersForWebService",function(){ 
			editMappedQueryHeadersForWebServiceDiv = $(this);
			checkWsQuerySyntaxDiv = $(this).closest(".joinWsApiInfoDiv").find(".checkWsQuerySyntax");
			var userID = $("#userID").val(); 
	  		var webserviceConId = $(this).attr('data-webserviceconid');
	  		var ilId = $("#ilId").val();
	  		var joinWebservice = $(this).attr('data-isjoinwebservice');
	  		var packageId = $("#packageId").val();  
	  		ilWsQuery =  $(this).parents(".joinWsApiInfoDiv").find(".wsApiILQuery").val(); 
	  		connectionMappingId = $(this).attr('data-mappingid');
 			 
	  		var selectedData ={
	  				iLId : ilId,
	  				connectionMappingId:connectionMappingId,
	  				joinWebService:true,
	  				packageId:packageId,
	  				iLquery:ilWsQuery,
	  				wsConId:webserviceConId
	  		}
	  		 
	  		viewIlSource.editMappedHeadersForWebService(userID,selectedData);
 			  
 		}); 
		 
		$("#fileMappingWithILTable").on('change', 'select.fileHeader', function() {
			var obj = this;
			var ilColumnName = $($(obj).closest("tr")).find(".iLcolumnName").text().replace("*","").trim().toLowerCase();
			if ($(this).val() == ilColumnName) { 
				$(obj).removeClass("not-mapped").addClass("is-mapped");
			} else { 
				if ( $(obj).hasClass("is-mapped") ) { 
					$(obj).removeClass("is-mapped").addClass("not-mapped"); 
				} 
			}
			
			if($(this).val() != ''){
				$(this).closest('tr').find('input.default-dtype').val('');
			}
		});
		
	$("#updateMappingWithILForWebService").click(function(){
		 
	    var iLColumnNames = [];
	    var selectedFileHeaders = [];
	    var dafaultValues = [];
	    var packageId = $("#packageId").val();
		var industryId = $("#industryId").val();
	    var userID = $("#userID").val();
	    var fileType = $("#flatFileType").val();
	    var delimeter = $("#delimeter").val();
	    var isFirstRowHasColumnNames =$("input:radio[name='isFirstRowHasColumnNames']:checked","#fileUploadForm").val(); 
	    var originalFileName = $("#fileMappingWithILTable").find("thead").find("th.originalFileName").text();
	    var iLId = $("#ilId").val();
	    var dLId = $("#dLId").val();
	    var filePath = null;
	    var tablerows = $("#fileMappingWithILTable").find("tbody").find("tr");
	    var process = true;
	    tablerows.each(function() {
	    	var row = $(this);
	    	var ilcolumn = row.find('.iLcolumnName').clone();
	    	ilcolumn.children().remove("span");
	    	
	    	var iLColumn = ilcolumn.text();
	    	
	    	var fileHeader = row.find('.fileHeader'). val() != ''? row.find('.fileHeader option:selected').text() : " ";
	    	var notNull = row.find('.notNull').attr('data-notNull');
	    	var fileHeaderVal = row.find('.fileHeader'). val();
	    	if(notNull == "YES"){
	    		var defVal =  row.find(".default").val();
	    		if(fileHeaderVal == '' && defVal == ''){
	    			var id =  row.find('.fileHeader').attr('id');
	    			common.clearValidations(["#"+id]);
	    			common.showcustommsg("#"+id, globalMessage['anvizent.package.label.pleaseChooseMandatoryField'],"#"+id);
	    			process=false;
	    		}
	    	}
	    	var defaultValue = null;
	    	if(fileHeader.trim() == ''){
	    		defaultValue = row.find(".default").val() != ''? row.find('.default'). val() : " ";
	    		
	    		if (defaultValue !== " ") {
	    			var s = standardPackage.validateDefaultValue(row.find('input.default-dtype').get(0));
	    			if (process && !s) process = false;
	    		}
	    		
	    	}
	    	  iLColumnNames.push(iLColumn);
	    	  selectedFileHeaders.push(fileHeader);
	    	  dafaultValues.push(defaultValue);
	    });
	     
	    if (!process) return false;
	    
	    var selectData = {
	        	packageId : packageId,
	        	industryId : industryId,
	        	iLId : iLId,
	        	dLId : dLId,
	        	fileType : '',
	        	delimeter : '',
	        	isFirstRowHasColoumnNames : false,
	        	originalFileName : originalFileName,
	        	iLColumnNames : iLColumnNames.join(','),
	        	selectedFileHeaders : selectedFileHeaders.join(','),
	        	dafaultValues : dafaultValues.join(','),
	        	
	        }
	    
	    showAjaxLoader(true);
	     var url_processMappingFileWithIL = "/app/user/"+userID+"/package/saveMappingWithILForWebService";
		 var myAjax = common.postAjaxCallObject(url_processMappingFileWithIL,'POST', selectData,headers);
		    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	  if(result != null && result.hasMessages){
			    			  if(result.messages[0].code == "ERROR") {
									 var  messages=[{
										  code : result.messages[0].code,
										  text : result.messages[0].text
									  }];
					    			 common.displayMessages(messages);
				    				  return false;
				    			  }
			    			  if(result.messages[0].code == "SUCCESS") {
			    				  if(result.object != null){
				    				   var webserviceMappingHeaders = result.object;
				    				   
				    				   var selectData = {
				    					    	packageId : packageId,
				    					    	industryId : industryId,
				    					    	iLId : iLId,
				    					    	dLId : dLId,
				    					    	webserviceMappingHeaders : webserviceMappingHeaders,
				    					    	connectionMappingId : connectionMappingId,
				    					    	iLquery:ilWsQuery
				    					    }
				    					  viewIlSource.updateMappedHeadersForWebservice(selectData);
				    			  }
				    			  }
			    	  }else{
				    		var messages = [ {
				    			code : globalMessage['anvizent.message.error.code'],
				    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
				    		} ];
				    		common.displayMessages(messages);
				    	}
			    });
		});	  
		
		$(document).on('click', '.editIlWsApiQuery', function() {
		  
			var joinWsApiInfoDiv = $(this).parents(".joinWsApiInfoDiv");
			
			joinWsApiInfoDiv.find(".wsApiILQuery").removeAttr("readonly");
			joinWsApiInfoDiv.find(".checkWsQuerySyntax").show();
			
		});
		
		$(document).on('click', '.checkWsQuerySyntax', function() {
			
	        var panelBody =  $(this).parents(".panel-body");
			var joinWsApiInfoDiv = $(this).parents(".joinWsApiInfoDiv");
   
			var query = joinWsApiInfoDiv.find(".wsApiILQuery").val();
			
			if (query.replace(/\s+/g, '') === '') {
				joinWsApiInfoDiv.find(".wsApiILQuery").addClass("border-red");
			}
			else {
				var userID = $("#userID").val();
				var industryId = $("#industryId").val();
				var packageId = $("#packageId").val();
				var isstaging = true;
				var tables = [];
				tables.push("");
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				showAjaxLoader(true);
				var url = "/app/user/"+userID+"/package/validateCustomTempTablesQuery/"+industryId+"/"+packageId;
			    var myAjax = common.postAjaxCallObject(url,'POST', {"queryvalue" : query, "tables" : tables.join('::'), "isstaging": isstaging},headers);
			    
			    myAjax.done(function(result) {
			    	showAjaxLoader(false);
			    	if(result != null && result.hasMessages){
			    		if(result.messages[0].code == "SUCCESS"){
			    			var obj = result.object;
			    			var status = obj.isValid;
					    	if (status) {
					    		panelBody.find(".successIlMessage").text(globalMessage['anvizent.message.text.queryisOK']).show();
					     		setTimeout(function() { panelBody.find(".successIlMessage").hide().empty(); }, 10000);
					     		joinWsApiInfoDiv.find(".editMappedQueryHeadersForWebService").show();
					    	}
					    	else {
					    		panelBody.find(".iLInputMessage").text("Query is not OK.").show();
					     		setTimeout(function() { panelBody.find(".iLInputMessage").hide().empty(); }, 10000);
					    	}
			    		}else{
			    			panelBody.find(".iLInputMessage").text("Query is not valid.").show();
				     		setTimeout(function() { panelBody.find(".iLInputMessage").hide().empty(); }, 10000);
			    		}
			    	}else{
			    		common.showErrorAlert(globalMessage['anvizent.package.label.unableToProcessYourRequest']);
			    		return false;
			    	}
			    });
			}

		});
		$(document).on('keyup', '.wsApiILQuery', function() {
			$(this).parents(".joinWsApiInfoDiv").find(".editMappedQueryHeadersForWebService").hide();
		});
		$(document).on("click",".preBuildQuery",function(){
			common.clearValidations(["#replace_variable"]);
			var mainDiv = $($(this).closest("div.panel"));
			mainDiv.find("#checkTablePreview").hide();
			mainDiv.find("#updateILConnectionMapping").hide();
			mainDiv.find("#defualtVariableDbSchema").empty();
			mainDiv.find("#replace_variable").val("");
			mainDiv.find("#replace_with").val("");
			mainDiv.find(".max_date_query").hide();
			var isIncrementalUpdate = mainDiv.find("#il_incremental_update").is(":checked");
			var _this = $(this);
			var userID = $("#userID").val();
	  		var iLId = $("#ilId").val();
	  		var connector_id = mainDiv.find("#connectorType").attr("data-connectorid");
	  		var protocal = mainDiv.find("#connectorType").data("protocal");
	  		var connectionId = mainDiv.find("#existingConnections").attr("data-connectionId");
	  		if(connectionId != '' && iLId != ''){
  			showAjaxLoader(true);
		  		if(isIncrementalUpdate){
					 var url_defaultILincrementalquery = "/app/user/"+userID+"/package/defaultILIncrementalQuery/"+iLId+"/"+connectionId;
					 var myAjax = common.loadAjaxCall(url_defaultILincrementalquery,'GET','',headers);
					    myAjax.done(function(result) {
					    	showAjaxLoader(false);
					    	if(result != null && result.hasMessages){
					    		if(result.messages[0].code=="ERROR"){
			    					  common.showErrorAlert(result.messages[0].text);
				    			  }
				    			  if(result.messages[0].code=="SUCCESS"){
				    				  var queryScript = result.object.query;
				    				  var maxDateQuery = result.object.maxDateQuery;
				  		    			  
			    				  		  mainDiv.find(".iLquery").val(queryScript);
			    				  		  mainDiv.find("#maxDatequery").val(maxDateQuery);
			    				  		  mainDiv.find(".max_date_query").show();
			    				  		  $("#oldMaxDateQuery").val(maxDateQuery);
			    				  		  if(connector_id !=null ){
			    				  			//viewIlSource.displayDatabaseSchemaBasedOnConnectorId(connector_id,connectionId,mainDiv,protocal);
			        					  } 
						    			  var n = queryScript.split(/from|FROM/).pop().split('.').shift().trim().split(" ");
							    		  var schemaName = n[n.length - 1]; 
							    		  if(protocal.indexOf('sqlserver') == -1 || protocal.indexOf('odbc') == -1 ){
						            	   schemaName = schemaName.replace(/[[\]]/g,''); 
						    			  }
							    		  else{
							    			  if(schemaName.indexOf("[") < 0)
							    				  schemaName = '['+schemaName+']'; 
							    		  }	
				    			  }
					    	}
					    });	
		  		}	
		  		else{
		  			var url_preBuildQuery = "/app/user/"+userID+"/package/getIlEpicorQuery/"+iLId+"/"+connectionId;
					 var myAjax = common.loadAjaxCall(url_preBuildQuery,'GET','',headers);
						myAjax.done(function(result) {
							showAjaxLoader(false);
							if(result != null){						
					    		  if(result.hasMessages){
				    				  if(result.messages[0].code=="ERROR"){
						    			  	common.showErrorAlert(result.messages[0].text);
						    				  return false;
						    			  }  		    					  
					    			  }
				    				  else{	
				    					  mainDiv.find(".iLquery").val(result.object);
					    				  var query = result.object;
					    					  $("#replaceShemas",mainDiv).show(); 
				    				  		  if(connector_id !=null ){
				    				  			//viewIlSource.displayDatabaseSchemaBasedOnConnectorId(connector_id,connectionId,mainDiv,protocal);
				        					  } 
							    			  var n = query.split(/from|FROM/).pop().split('.').shift().trim().split(" ");
								    		  var schemaName = n[n.length - 1]; 
								    		  if(protocal.indexOf('sqlserver') == -1 || protocal.indexOf('odbc') == -1){
							            	   schemaName = schemaName.replace(/[[\]]/g,''); 
							    			  }
								    		  else{
								    			  if(schemaName.indexOf("[") < 0)
								    				  schemaName = '['+schemaName+']'; 
								    		  }	
					    			  }
					    		  }
							
						});
		  		}
			
	  		}
		});
		
		$(document).on("click",".addDbSchema",function(){
	  		  
    		var defualtVariableDbSchema = '';
    		var mainDiv = $($(this).closest("div.panel"));
    		var connector_id = mainDiv.find("#connectorType").attr("data-connectorid");
    		var protocal = mainDiv.find("#connectorType").data("protocal");
    		var dbSchemaSelection = $(this).parents("#dbSchemaSelection");
    		$(dbSchemaSelection).find(".dbAndSchemalabel").text("");
      		var dbVariables = dbSchemaSelection.find("#dbVariable > option").clone();
      		var dbnames = dbSchemaSelection.find('#dbName  > option').clone();
      		var schemaVariables = dbSchemaSelection.find('#schemaVariable > option').clone();
      		
      		var dbSchemaSelectionDivForSqlServer = $("#dbSchemaSelectionDivForSqlServer").clone().removeAttr('id').prop("id","dbSchemaSelection").show();
      		var dbSchemaSelectionDivForNotSqlServer = $("#dbSchemaSelectionDivForNotSqlServer").clone().removeAttr('id').prop("id","dbSchemaSelection").show();
      	
      		if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
      			
      			var dbVariable = dbSchemaSelectionDivForSqlServer.find("#dbVariable");
      			var dbName = dbSchemaSelectionDivForSqlServer.find("#dbName");
      			var schemaVariable =  dbSchemaSelectionDivForSqlServer.find("#schemaVariable");
      			
	  			dbVariable.empty().append(dbVariables);
				dbName.empty().append(dbnames);
	  			schemaVariable.empty().append(schemaVariables);
	  			dbSchemaSelectionDivForSqlServer.find(".labelForDbAndSchemaName").text("");
	  			dbSchemaSelectionDivForSqlServer.find(".deleteDbSchema").show();
				$("#defualtVariableDbSchema",mainDiv).append(dbSchemaSelectionDivForSqlServer);
				
      		}else if(protocal.indexOf('mysql') != -1 || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1 || protocal.indexOf('sforce') != -1   || protocal.indexOf('as400') != -1   || protocal.indexOf('postgresql') != -1  ||  protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1){
      			
      			var schemaVariable =  dbSchemaSelectionDivForNotSqlServer.find("#schemaVariable");
      			var dbName = dbSchemaSelectionDivForNotSqlServer.find("#dbName");
      			 
          		schemaVariable.empty().append(schemaVariables);
          		dbName.empty().append(dbnames);
          		
          		dbSchemaSelectionDivForNotSqlServer.find(".labelForDbAndSchemaName").text("");
          		dbSchemaSelectionDivForNotSqlServer.find(".deleteDbSchema").show();
          		$("#defualtVariableDbSchema",mainDiv).append(dbSchemaSelectionDivForNotSqlServer);
      		}
      		
      		});
		
	  	$(document).on("change","#dbName",function(){ 		
	  		var mainDiv = $($(this).closest("div.panel"));
	  		var connectionId = mainDiv.find("#existingConnections").attr("data-connectionId");
	  		var protocal = mainDiv.find("#connectorType").data("protocal");
			var userID = $("#userID").val();
			var $schemaName = $(this).parents('#dbSchemaSelection').find('#schemaName');
			var databaseName = $(this).val();
			if(databaseName === '{dbName}' ){
				common.showErrorAlert(globalMessage['anvizent.package.message.pleaseselectdatabase']);
				return false;
			}
			var connector_id = mainDiv.find("#connectorType").attr("data-connectorid");
		if(connectionId !=null ){
			if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
				showAjaxLoader(true);
				var url_getSchemaByDatabse ="/app/user/"+userID+"/package/getSchemaByDatabse/"+connectionId+"/"+databaseName+""; 
				var myAjax = common.loadAjaxCall(url_getSchemaByDatabse,'GET','',headers);
				    myAjax.done(function(result) {
				    	showAjaxLoader(false);
				    	  if(result != null){ 
				    		  
				    		  if(result.hasMessages){
				    			  if(result.messages[0].code == "ERROR") { 
				    				 
				    				  common.showErrorAlert(result.messages[0].text);
				    				  return false;
				    			  }  
				    		   else if(result.messages[0].code == "SUCCESS"){
				    			   
				    			   var schemaName='';
				    			  
				    			   if(result.object != null){
				    				   $schemaName.empty();
				    				      schemaName  = '<option value="{schemaName}">'+globalMessage['anvizent.package.label.selectschemavariable']+'</option>';
				    				   $.each( result.object, function( key, value ) {
				    					   schemaName += '<option value='+value+'>'+value+'</option>';
					    				 });
				    				  
				    				   $schemaName.append(schemaName);
				    				  
				    			   }else{
				    				   common.showErrorAlert(result.messages[0].text);
				    				   return false;
				    			   }
				    			 
				    			  } 
				    		  }	  
				    	  }    	  
				    }); 
			}
			
			}
		 	 
	 	});
	  	
	  	$(document).on("click","#replaceShemas",function(){ 	
	  		var mainDiv = $($(this).closest("div.panel"));
	  		var script = mainDiv.find(".iLquery").val();
	  		var maxDateQuery = mainDiv.find('#maxDatequery').val();
	  		var query='',
	  		maxQuery='';
	  		var connector_id = mainDiv.find("#connectorType").attr("data-connectorid");
	  		var protocal = mainDiv.find("#connectorType").data("protocal");
	  		showAjaxLoader(true);
		    $('#defualtVariableDbSchema',mainDiv).find('.dbSchemaSelection').each(function(i, obj) {
		    	
		    	var dbVariable=$(this).find(".dbVariable").val(); 
			    var dbName=$(this).find(".dbName").val();
			    var schemaVariable=$(this).find(".schemaVariable").val();
			    var schemaName=$(this).find(".schemaName").val();
			    common.clearValidations([$(obj).find('.dbVariable'), $(obj).find('.dbName'), $(obj).find('.schemaVariable'), $(obj).find('.schemaName')]);
			    
			  var validStatus = true;
			  if(dbVariable == '' || dbVariable == '{db0}'){
				    common.showcustommsg($(obj).find('.dbVariable'),globalMessage['anvizent.package.message.pleasechoosedatabasevariable'],$(obj).find('.dbVariable'));
			    	validStatus =false;
			  }
			  if(dbName == '' || dbName == '{dbName}'){
				    common.showcustommsg($(obj).find('.dbName'),globalMessage['anvizent.package.message.pleasechoosedatabasename'],$(obj).find('.dbName'));
			    	validStatus =false;
			  }
			  if(schemaVariable == '' || schemaVariable == '{schema0}'){
				    common.showcustommsg($(obj).find('.schemaVariable'),globalMessage['anvizent.package.message.pleasechooseschemavariable'],$(obj).find('.schemaVariable'));
			    	validStatus =false;
			  }
			  if(schemaName == '' || schemaName == '{schemaName}'){
				    common.showcustommsg($(obj).find('.schemaName'),globalMessage['anvizent.package.message.pleasechooseschemaname'],$(obj).find('.schemaName'));
			    	validStatus =false;
			  }
			 
			  if(!validStatus){
				 	return validStatus;
			   }
			  if(protocal.indexOf('sqlserver') != -1 || protocal.indexOf('odbc') != -1){
			    if ((script.indexOf(dbVariable) > -1 || script.indexOf(schemaVariable) > -1) || (maxDateQuery.indexOf(dbVariable) > -1 || maxDateQuery.indexOf(schemaVariable) > -1)) {
				    	query = script.split(dbVariable).join(dbName);
				    	query = query.split(schemaVariable).join(schemaName);
				    	
				    	maxQuery = maxDateQuery.split(dbVariable).join(dbName);
				    	maxQuery = maxQuery.split(schemaVariable).join(schemaName);
			    	}
			    	
			    else{
			    	query = script;
			    	maxQuery = maxDateQuery;
			    }
			  }
		     else if(protocal.indexOf('mysql') != -1 || protocal.indexOf('oracle') != -1 || protocal.indexOf('db2') != -1 || protocal.indexOf('sforce') != -1   || protocal.indexOf('as400') != -1   || protocal.indexOf('postgresql') != -1  ||  protocal.indexOf('vortex') != -1 || protocal.indexOf('openedge') != -1 ){
		    	 console.log(schemaVariable ,dbName);
		    	 if ((script.indexOf(schemaVariable) > -1) || (maxDateQuery.indexOf(schemaVariable) > -1)) {
		    		 	
		    		    query = script.split(schemaVariable).join(dbName);
		    		    maxQuery = maxDateQuery.split(schemaVariable).join(dbName);
			    	}
			    	
			    else{
			    	query = script;
			    	maxQuery = maxDateQuery;
			    }
	    	}
			    script = query;
			    maxDateQuery = maxQuery;
		    });
		    showAjaxLoader(false);
		    mainDiv.find(".iLquery").val(script);
		    mainDiv.find("#maxDatequery").val(maxDateQuery);
	  	});
	  	
	  	$(document).on("click",".deleteDbSchema",function(){ 		
	 		$(this).parents("#dbSchemaSelection").remove(); 		 			 
	 	});
	  	
	$(document).on("click","#checkQuerySyntax",function(){
		common.clearValidations(["#replace_variable,#maxDatequery"]);
		var mainDiv = $($(this).closest("div.panel"));
		var userID = $("#userID").val(); 
		mainDiv.find("#replace_variable").val("");
		mainDiv.find("#replace_with").val("");
		var connectionId = mainDiv.find("#existingConnections").attr("data-connectionId");
		var query = mainDiv.find(".iLquery").val();
		var userID = $("#userID").val(); 
		var typeOfCommand = mainDiv.find(".typeOfCommand").val(); 
		var isIncrementalUpdate = mainDiv.find("input[name='isIncrementalUpdate']").is(":checked") ? true : false; 
		var queryOrSp = '';
		var queryOrSpId = '';
		var maxDateQuery = mainDiv.find('#maxDatequery').val();
			if(typeOfCommand  === "Query"){
				queryOrSp=mainDiv.find(".iLquery").val();
			}
			  if(typeOfCommand  === "Stored Procedure"){
				  queryOrSp=mainDiv.find(".iLSp").val();
			}
			  
			  if( queryOrSp != '') {
				  
				  if ( isIncrementalUpdate ) {
						var dateIndex = query.indexOf("{date}");
						if ( dateIndex == -1) {
							common.showErrorAlert("{date} placeholder not found in query");
	 						return false;
						}
						var date_index = maxDateQuery.indexOf("{date}");
						if(maxDateQuery.trim().length == 0){
							common.showcustommsg(mainDiv.find("#maxDatequery"),globalMessage['anvizent.package.label.shouldNotBeEmpty'] ,mainDiv.find("#maxDatequery"));
							return false;
						}
						else if ( date_index == -1) {
							common.clearValidations(["#maxDatequery"]);
							common.showErrorAlert("{date} placeholder not found in query");
	 						return false;
						}
					}
				  
	                 if(typeOfCommand  === "Query"){
						 queryOrSpId=mainDiv.find(".iLquery").attr('id');
						 common.clearValidations(["#"+queryOrSpId]);
		             }
				  if(typeOfCommand  === "Stored Procedure"){
					  queryOrSpId=mainDiv.find(".iLSp").attr('id');
					  common.clearValidations(["#"+queryOrSpId]);
				}
				 var token = $("meta[name='_csrf']").attr("content");
				 var header = $("meta[name='_csrf_header']").attr("content");
				 headers[header] = token;
				var connector_id = mainDiv.find("#connectorType").attr("data-connectorid");
				var url_getDbSchemaVaribles ="/app/user/"+userID+"/package/getDbSchemaVaribles/"+connector_id+"";
				showAjaxLoader(true);
				 var myAjax = common.loadAjaxCall(url_getDbSchemaVaribles,'GET','',headers);
				    myAjax.done(function(result) {
				    	showAjaxLoader(false);
			    	  if(result != null && result.hasMessages){ 
			    			  if(result.messages[0].code == "ERROR") { 
			    				  common.showErrorAlert(result.messages[0].text);
			    				  return false;
			    			  }  
			    		   else if(result.messages[0].code == "SUCCESS"){
			    			   var unReplacedschemaAndDatabses = '';
			    			   if(result.object != null){ 
			    				   var schemaAndDatabses = [];
			    				   $.each( result.object, function( key, value ) {
			    					   schemaAndDatabses.push(value);
			    					   schemaAndDatabses.push(key);
				    				 });
			    				   for (i = 0; i < schemaAndDatabses.length; i++) {
			    						if (query.indexOf(schemaAndDatabses[i]) >= 0){
			    							unReplacedschemaAndDatabses += 	schemaAndDatabses[i] +',';
			    					    }  
			    					}
			    			   }
			   				    if(unReplacedschemaAndDatabses != ''){
			   						 var replacedWord = unReplacedschemaAndDatabses;
			   						 replacedWord = replacedWord.substring(0, replacedWord.length - 1);
			   						 common.showErrorAlert(globalMessage['anvizent.package.label.pleaseReplace']+" [ "+replacedWord+" ] "+globalMessage['anvizent.package.label.DatabasesOrSchemasInDefaultQuery']);
			   						return false;
			   						
			   					}else{
			   							var selectData ={
			   									iLConnection : {
			   										connectionId : connectionId
			   									},
			   									iLquery : queryOrSp,
			   									maxDateQuery : maxDateQuery,
			   									typeOfCommand : typeOfCommand,
			   									isIncrementalUpdate :isIncrementalUpdate
			   									
			   							}
			   							showAjaxLoader(true);
			   							var url_checkQuerySyntax = "/app/user/"+userID+"/package/checksQuerySyntax";
			   							 var myAjax = common.postAjaxCall(url_checkQuerySyntax,'POST', selectData,headers);
			   							    myAjax.done(function(result) {
			   							    	showAjaxLoader(false);
			   							    	  if(result != null && result.hasMessages){
			   							    		  if(result.messages[0].code == "SUCCESS") {
			   								    			  mainDiv.find('.successIlMessage').show().empty().append(result.messages[0].text);
			   								    			  setTimeout(function() { mainDiv.find(".successIlMessage").hide().empty(); }, 10000);
			   									    		  mainDiv.find("#checkTablePreview").show();
			   									    		  mainDiv.find("#updateILConnectionMapping").show();
			   							    		  }else{
			   								    			  if(result.messages[0].text === null){
			   								    				  mainDiv.find('.iLInputMessage').show().empty().append("Invalid Query/Sp");
			   										    		  setTimeout(function() { mainDiv.find(".iLInputMessage").hide().empty(); }, 10000);
			   										    		  mainDiv.find("#checkTablePreview").hide();
			   										    		  mainDiv.find("#updateILConnectionMapping").hide();
			   										    		  return false;
			   								    			  }else{
			   								    				  mainDiv.find('.iLInputMessage').show().empty().append(result.messages[0].text);
			   										    		  setTimeout(function() { mainDiv.find(".iLInputMessage").hide().empty(); }, 10000);
			   										    		  mainDiv.find("#checkTablePreview").hide();
			   										    		  mainDiv.find("#updateILConnectionMapping").hide();
			   										    		  if(result.object == "normal"){
			   										    			common.showcustommsg(mainDiv.find(".iLquery"), " ", mainDiv.find(".iLquery"));
			   										    		  }else{
			   										    			common.showcustommsg(mainDiv.find("#maxDatequery"), " ", mainDiv.find("#maxDatequery"));
			   										    		  }
			   										    		  return false;
			   								    			  }
			   												
			   								    		  }
			   							    	  }else{
			   											var messages = [ {
			   												code : globalMessage['anvizent.message.error.code'],
			   												text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
			   											} ];
			   								    		common.displayMessages(messages);
			   										}
			   							    });
			   					}
			    		   }
			    	  }else{
				    		var messages = [ {
				    			code : globalMessage['anvizent.message.error.code'],
				    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
				    		} ];
				    		common.displayMessages(messages);
				    	}
			    }); 
		} else {
				if(typeOfCommand  === "Query"){
					 queryOrSpId=mainDiv.find(".iLquery").attr('id');
					 common.clearValidations(["#"+queryOrSpId]);
					 common.showcustommsg("#"+queryOrSpId, globalMessage['anvizent.package.label.shouldNotBeEmpty'],"#"+queryOrSpId);
				}
				  if(typeOfCommand  === "Stored Procedure"){
					  queryOrSpId=mainDiv.find(".iLSp").attr('id');
					  common.clearValidations(["#"+queryOrSpId]);
					  common.showcustommsg("#"+queryOrSpId, globalMessage['anvizent.package.label.shouldNotBeEmpty'],"#"+queryOrSpId);
				}
			}
	});
	 $(document).on("click",".replaceAllVar",function(){
  		common.clearValidations(["#replace_variable"]);
  		var mainDiv = $($(this).closest("div.panel"));
  		var replaceVar = mainDiv.find('#replace_variable').val().toLowerCase();
  		var maxDateQuery = mainDiv.find("#maxDatequery").val().toLowerCase();
  		var replaceWith = mainDiv.find('#replace_with').val();
  		var collapseId = $(this).parents(".panel-collapse").attr("id");
  		$("#collapseId").val(collapseId);
  		$("#replaceVariable").val(replaceVar);
  		$("#replaceWith").val(replaceWith);
  		$("#maxDateQuerys").val(maxDateQuery);
  		if(replaceVar == ''){ 
	    	common.showcustommsg(mainDiv.find("#replace_variable"), globalMessage['anvizent.package.label.pleaseEnterVariable'] ,mainDiv.find("#replace_variable"));
			status = false;
  		}else{
  		    $("#replaceAllAlert,mainDiv").modal("show");
  		}
  	 });
	 $(document).on("click",".confirm_replace_all",function(){
	  		var mainDiv = $($(this).closest("div.panel"));
	  		var queryScript = $('.iLquery').val().toLowerCase();
	  		var maxDateQueryScript =  $('#maxDateQuerys').val().toLowerCase();
	  		var replaceVar = $('#replaceVariable').val().toLowerCase();
	  		var replaceWith = $('#replaceWith').val();
	  		var _this = "#"+$("#collapseId").val();
	  		var status = true;
	  		if(replaceVar == ''){ 
		    	common.showcustommsg($("#replaceVariable"), globalMessage['anvizent.package.label.pleaseEnterVariable'] ,$("#replaceVariable"));
				status = false;
	  		}
	  		if(!status){
	  			return false;
	  		}
	  		queryScript=queryScript.replace(new RegExp(replaceVar, 'g'), replaceWith);
	  		maxDateQueryScript=maxDateQueryScript.replace(new RegExp(replaceVar, 'g'), replaceWith);
	  		$(_this).find('.iLquery').val(queryScript);
	  		$(_this).find('#maxDatequery').val(maxDateQueryScript);
	  		$(_this).find("#undo").show();
	  		$("#replaceAllAlert").modal("hide");
	  	 });
	  	 
	 $(document).on("click","#undo",function(){
		 	var mainDiv = $($(this).closest("div.panel"));
	  		var queryScript = mainDiv.find('#oldQueryScript').val();
	  		var maxDateQuery = $('#oldMaxDateQuery').val();
	  		mainDiv.find('.iLquery').val(queryScript);
	  		mainDiv.find('#maxDatequery').val(maxDateQuery);
	  		$("#undo,mainDiv").hide();
	  		$('#replace_variable,mainDiv').val("");
	  	    $('#replace_with,mainDiv').val("");
	  	 });
	 
	 $(document).on('keyup', '.iLquery, #maxDatequery', function(){
		 	var mainDiv = $($(this).closest("div.panel"));
			$('#checkTablePreview, #updateILConnectionMapping',mainDiv).hide();
		});
	 
	 $(".back_btn").on("click",function(){
		 var from_val = $("#from").val();
				if(from_val == "standard"){
					var backstnd = $("#back_btnStandard").val();
					$(this).prop('href',adt.appContextPath+backstnd);
					
				}else{
					var backAddIl = $("#back_btnAddil").val();
					$(this).prop('href',adt.appContextPath+ backAddIl);
				}
	});
	$(document).on("click",".editDbDetails,.editFlatDetails",function(){
		var mainDiv = $($(this).closest("div.panel"));
		$(".dataSourceOther",mainDiv).prop("disabled",false);
		$(".ilSourceName",mainDiv).prop("disabled",false);
		$(".updateDb,.updateFlat",mainDiv).show();
	});
	$(document).on("click",".updateDbDetails,.updateFlatDetails",function(){
		common.clearValidations(["#dataSourceOtherName",".ilSourceName"])
		var mainDiv = $($(this).closest("div.panel"));
		connectionMappingId= $(this).attr("data-mappingDbId");
		var dataSourcename = mainDiv.find(".ilSourceName option:selected").val();
		var packageId= $("#packageId").val();
		var dataSourceOther = mainDiv.find("#dataSourceOtherName").val();
		var userID = $("#userID").val(); 
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		
		var status = true;
  		if(dataSourcename == '0'){ 
	    	common.showcustommsg(mainDiv.find(".ilSourceName"), globalMessage['anvizent.package.label.enterDataSourceName'] ,mainDiv.find(".ilSourceName"));
			status = false;
  		}
  		if(dataSourcename == "-1"){
  			if(dataSourceOther  == ''){ 
  		    	common.showcustommsg(mainDiv.find("#dataSourceOtherName"), globalMessage['anvizent.package.label.enterDataSource'] ,mainDiv.find("#dataSourceOtherName"));
  				status = false;
  	  		}
  		}
  		
  		if(!status){
  			return false;
  		}
		
		var selectData ={
				mappingId:connectionMappingId,
				packageId:packageId,
				dataSouceName:dataSourcename,
				dataSouceNameOther:dataSourceOther
		}
		
		showAjaxLoader(true);
		var url_updateDbDetails = "/app/user/"+userID+"/package/updateDataSourceDetails";
		   var myAjax = common.postAjaxCallObject(url_updateDbDetails,'POST',selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;
	    					  mainDiv.find('.iLInputMessage').show().empty().append(result.messages[0].text);
			    			  setTimeout(function() { mainDiv.find(".iLInputMessage").hide().empty(); }, 10000);
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  var message = result.messages[0].text; 
		    				  if (selectData.dataSouceName == "-1") {
		    					  $(".dataSourceOther",mainDiv).hide();
		    					  $(".dataSourceOther",mainDiv).val("");
		    					  common.addToSelectBox(dataSourceOther,"option.otherOption");
								  $(".ilSourceName",mainDiv).val(dataSourceOther);
							 }
		    				  $(".ilSourceName",mainDiv).prop("disabled",true);
							  $(".updateDb,.updateFlat",mainDiv).hide();
		    				  mainDiv.find('.successIlMessage').show().empty().append(result.messages[0].text);
			    			  setTimeout(function() { mainDiv.find(".successIlMessage").hide().empty(); }, 10000);
		    			  }
		    		  }
				}
		});
		
	});
	$(document).on("click",".editWsDetails",function(){
		var mainDiv = $($(this).closest("div.panel"));
		$(".dataSourceOther",mainDiv).prop("disabled",false);
		$(".ilSourceName",mainDiv).prop("disabled",false);
		$(".wsSourceUrl",mainDiv).prop("disabled",false);
		$(".updateWs",mainDiv).show();
	});
	$(document).on("click",".updateWsDetails",function(){
		common.clearValidations(["#dataSourceOtherName",".ilSourceName"])
		var mainDiv = $($(this).closest("div.panel"));
		connectionMappingId= $(this).attr("data-mappingDbId");
		var dataSourcename = mainDiv.find(".ilSourceName option:selected").val();
		var packageId= $("#packageId").val();
		var dataSourceOther = mainDiv.find("#dataSourceOtherName").val();
		var wsSourceUrl = mainDiv.find(".wsSourceUrl").val();
		var userID = $("#userID").val(); 
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		
		var status = true;
  		if(dataSourcename == '0'){ 
	    	common.showcustommsg(mainDiv.find(".ilSourceName"), globalMessage['anvizent.package.label.enterDataSourceName'] ,mainDiv.find(".ilSourceName"));
			status = false;
  		}
  		if(dataSourcename == "-1"){
  			if(dataSourceOther.trim().length == 0){ 
  		    	common.showcustommsg(mainDiv.find("#dataSourceOtherName"), globalMessage['anvizent.package.label.enterDataSource'] ,mainDiv.find("#dataSourceOtherName"));
  				status = false;
  	  		}
  		}
  		if(wsSourceUrl == '') {
  		  	common.showcustommsg(mainDiv.find(".wsSourceUrl").val(),"please ender ws api url.",mainDiv.find(".wsSourceUrl").val());
			status = false;
  			}
  		
  		if(!status){
  			return false;
  		}
		
		var selectData ={
				mappingId:connectionMappingId,
				packageId:packageId,
				dataSouceName:dataSourcename,
				dataSouceNameOther:dataSourceOther,
				wsApiUrl:wsSourceUrl
		}
		
		showAjaxLoader(true);
		var url_updateDbDetails = "/app/user/"+userID+"/package/updateWsDataSourceDetails";
		   var myAjax = common.postAjaxCallObject(url_updateDbDetails,'POST',selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;
	    					  mainDiv.find('.iLInputMessage').show().empty().append(result.messages[0].text);
			    			  setTimeout(function() { mainDiv.find(".iLInputMessage").hide().empty(); }, 10000);
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  var message = result.messages[0].text; 
		    				  if (selectData.dataSouceName == "-1") {
		    					  $(".dataSourceOther",mainDiv).hide();
		    					  $(".dataSourceOther",mainDiv).val("");
		    					  common.addToSelectBox(dataSourceOther,"option.otherOption");
								  $(".ilSourceName",mainDiv).val(dataSourceOther);
							 }
		    				  $(".ilSourceName",mainDiv).prop("disabled",true);
		    				  $(".wsSourceUrl",mainDiv).prop("disabled",true);
							  $(".updateWs",mainDiv).hide();
		    				  mainDiv.find('.successIlMessage').show().empty().append(result.messages[0].text);
			    			  setTimeout(function() { mainDiv.find(".successIlMessage").hide().empty(); }, 10000);
		    			  }
		    		  }
				}
		});
		
	});
	$(document).on("click",".editWsJoinDetails",function(){
		var mainDiv = $($(this).closest("div.panel"));
		$(".dataSourceOther",mainDiv).prop("disabled",false);
		$(".ilSourceName",mainDiv).prop("disabled",false);
		$(".wsJoinUrls",mainDiv).prop("disabled",false);
		$(".updateWsJoin",mainDiv).show();
	});
	$(document).on("click",".updateWsJoinDetails",function(){
		common.clearValidations(["#dataSourceOtherName",".ilSourceName"])
		var mainDiv = $($(this).closest("div.panel"));
		connectionMappingId= $(this).attr("data-mappingDbId");
		var dataSourcename = mainDiv.find(".ilSourceName option:selected").val();
		var packageId= $("#packageId").val();
		var dataSourceOther = mainDiv.find("#dataSourceOtherName").val();
		var wsJoinUrls = mainDiv.find(".wsJoinUrls").val();
		var userID = $("#userID").val(); 
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		
		var status = true;
  		if(dataSourcename == '0'){ 
	    	common.showcustommsg(mainDiv.find(".ilSourceName"), globalMessage['anvizent.package.label.enterDataSourceName'] ,mainDiv.find(".ilSourceName"));
			status = false;
  		}
  		if(dataSourcename == "-1"){
  			if(dataSourceOther.trim().length == 0){ 
  		    	common.showcustommsg(mainDiv.find("#dataSourceOtherName"), globalMessage['anvizent.package.label.enterDataSource'] ,mainDiv.find("#dataSourceOtherName"));
  				status = false;
  	  		}
  		}
  		if(wsJoinUrls == '') {
  		  	common.showcustommsg(mainDiv.find(".wsJoinUrls").val(),"please ender ws api join urls.",mainDiv.find(".wsJoinUrls").val());
			status = false;
  			}
  		
  		if(!status){
  			return false;
  		}
		
		var selectData ={
				mappingId:connectionMappingId,
				packageId:packageId,
				dataSouceName:dataSourcename,
				dataSouceNameOther:dataSourceOther,
				wsJoinUrls:wsJoinUrls
		}
		
		showAjaxLoader(true);
		var url_updateDbDetails = "/app/user/"+userID+"/package/updateWsJoinDataSourceDetails";
		   var myAjax = common.postAjaxCallObject(url_updateDbDetails,'POST',selectData,headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null){
		    		  if(result.hasMessages){
	    				  if(result.messages[0].code=="ERROR"){
	    					  common.showErrorAlert(result.messages[0].text);
	    					  var message = result.messages[0].text;
	    					  mainDiv.find('.iLInputMessage').show().empty().append(result.messages[0].text);
			    			  setTimeout(function() { mainDiv.find(".iLInputMessage").hide().empty(); }, 10000);
		    			  }
		    			  if(result.messages[0].code=="SUCCESS"){
		    				  var message = result.messages[0].text; 
		    				  if (selectData.dataSouceName == "-1") {
		    					  $(".dataSourceOther",mainDiv).hide();
		    					  $(".dataSourceOther",mainDiv).val("");
		    					  common.addToSelectBox(dataSourceOther,"option.otherOption");
								  $(".ilSourceName",mainDiv).val(dataSourceOther);
							 }
		    				  $(".ilSourceName",mainDiv).prop("disabled",true);
		    				  $(".wsJoinUrls",mainDiv).prop("disabled",true);
							  $(".updateWsJoin",mainDiv).hide();
		    				  mainDiv.find('.successIlMessage').show().empty().append(result.messages[0].text);
			    			  setTimeout(function() { mainDiv.find(".successIlMessage").hide().empty(); }, 10000);
		    			  }
		    		  }
				}
		});
		
	});
	$(document).on("change",".ilSourceName",function(){
		var mainDiv = $($(this).closest("div.panel"));
    	var dataSourceName= mainDiv.find(".ilSourceName option:selected").val().trim();
    	if(dataSourceName == "-1"){
	    	   $(".dataSourceOther",mainDiv).show();
	       }else{
	    	   $(".dataSourceOther",mainDiv).hide();
	       }
    });
	
	$(document).on('change', '.toggle-demo', function(){
		debugger
	    var userID = $("#userID").val();
	    var mainDiv = $($(this).closest("div.panel"));
	    var connectionMappingId= $(this).attr("data-mappingId");
	    var isActiveRequired = this.checked;
	    var enable = $(this).attr("data-on");
	    /*if(isActiveRequired == "true"){
	    	isActiveRequired = false;
	    }else{
	    	isActiveRequired = true;
	    }*/
	    showAjaxLoader(true);
	    var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
	    var url_updateIsActiveStatusIlSource = "/app/user/"+userID+"/package/updateIsActiveStatusIlSource/"+connectionMappingId+"/"+isActiveRequired+"";
	    var myAjax = common.loadAjaxCall(url_updateIsActiveStatusIlSource,'POST','',headers);
	    myAjax.done(function(result) {
	    	showAjaxLoader(false);
	    	  if(result != null){
	    		  if(result.hasMessages){
	    			  if(result.messages[0].code=="ERROR"){
    					  common.showErrorAlert(result.messages[0].text);
    					  var message = result.messages[0].text;
    					  mainDiv.find('.iLInputMessage').show().empty().append(result.messages[0].text);
		    			  setTimeout(function() { mainDiv.find(".iLInputMessage").hide().empty(); }, 10000);
	    			  }
	    			  if(result.messages[0].code=="SUCCESS"){
	    				  var message = result.messages[0].text; 
    					  mainDiv.find('.successIlMessage').show().empty().append(result.messages[0].text);
		    			  setTimeout(function() { mainDiv.find(".successIlMessage").hide().empty(); }, 10000);
		    			  if(isActiveRequired){
		    				  $(mainDiv).find(".databasePreviewStandard").prop("disabled",false);
		    				  $(mainDiv).find(".flatFilePreviewStandard").prop("disabled",false)
		    				  $(mainDiv).find(".deleteIlSource").prop("disabled",false)
		    			  }else{
		    				  $(mainDiv).find(".databasePreviewStandard").prop("disabled",true);
		    				  $(mainDiv).find(".flatFilePreviewStandard").prop("disabled",true);
		    				  $(mainDiv).find(".deleteIlSource").prop("disabled",true);
		    			  }
	    			  }
	    		  }
	    		  
	    		 
	    	  }
	    }); 
	 	}); 
			
}