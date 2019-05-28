var  eltStgConfig = {
		initialPage : function() {
			$(".keyValueConfigPairsDiv, .createTagsInfoDiv , #saveStgPairConfig").hide();
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
			$('#eltStgConfigTable tbody').sortable();
			$("#configTagsTbl").DataTable( {
		        "order": [[ 0, "desc" ]],"language": {
	                "url": selectedLocalePath
	            }
		    });
		},
		
		validateTableLength : function(){
			if($("#eltStgConfigTable tbody tr.stgRow").length == '0')
				 $("#saveConfigBtn,#deleteStgKeyDiv").hide(); 
			else
				$("#saveConfigBtn,#deleteStgKeyDiv").show(); 
		},
		
		getEltConfigKeyValuePairs : function(result){
			if(result != null){
				var confiKeyValuePairTbl = $('#eltStgConfigTable tbody').empty();
				for(var i=0;i<result.object.length;i++){
					var id = result.object[i].id;
					var key = result.object[i].key;
					var value = result.object[i].value;
					this.populateKeyValue(confiKeyValuePairTbl,id,key,value);
				}
				this.validateTableLength();
				//this.showHideMoveArrowButtons();
			}
		},
		populateProperiesFileKeyValuePairs : function(result){
			if(result != null){
				var _thisObj = this;
				var confiKeyValuePairTbl = $('#eltStgConfigTable').find("tbody");
				for(var i=0 ; i<result.object.length;i++){
					var resultinfo = result.object[i];
						_thisObj.populateKeyValue(confiKeyValuePairTbl,0,resultinfo.key,resultinfo.value);
				}
				//this.showHideMoveArrowButtons();
			}
		},
		populateKeyValue: function(confiKeyValuePairTbl,id,key,value) {
			var trDiv = $(".cloneCopystgRow").clone().addClass('stgRow').removeClass('cloneCopystgRow hidden');
			trDiv.find(".stg_key").val(key || '');
			trDiv.find(".stg_value").val(value || '');
			trDiv.find(".keyId").val(id || '');
			confiKeyValuePairTbl.append($(trDiv));
			this.validateTableLength();
		},
		
		addKeyValuePairAttheBeginning: function() {
			var confiKeyValuePairTbl = $("#eltStgConfigTable tbody");
			var trDiv = $(".cloneCopystgRow").clone().addClass('stgRow').removeClass('cloneCopystgRow hidden');
			confiKeyValuePairTbl.prepend(trDiv);
			$("#saveConfigBtn").show();
		},
		addKeyValuePair: function(_this) {
			var thisRow = $(_this).parents("tr");
			var trDiv = $(".cloneCopystgRow").clone().addClass('stgRow').removeClass('cloneCopystgRow hidden');
			trDiv.insertAfter($(thisRow));
			trDiv.find("input.stg_key").val("").focus();
		},
		
		displayConfigTagsInfo: function(id) {
			$("#configTagsTable").hide();
			$(".keyValueConfigPairsDiv,#saveConfigBtn,.createTagsInfoDiv").show();
			$("#selectedTagId").val(id);
			
			var userId = $("#userID").val();
			
			var url_getEltConfigTagsByID = "/app_Admin/user/"+userId+"/eltconfig/getEltConfigTagsByID/"+id;
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCallObject(url_getEltConfigTagsByID,'GET','',common.getcsrfHeader());
	 	    myAjax.done(function(result) {
		 	    	showAjaxLoader(false); 
		 	    	if(result.hasMessages){		    			  
		    			if(result.messages[0].code == "ERROR") {		    				  
							var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
		    				return false;
		    			} 
		    			if(result.messages[0].code == "SUCCESS") {
		    				var resultinfo = result.object;
		    				$("#tagName").val(resultinfo.tagName);
		    				resultinfo.active ? $("input[name='active'][value='true']").prop("checked",true) :  $("input[name='active'][value='false']").prop("checked",true)
		    				
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		
		displayConfigTagProperties: function(id) {
			$("#configTagsTable").hide();
			$(".keyValueConfigPairsDiv,#saveConfigBtn,.createTagsInfoDiv").show();
			$("#selectedTagId").val(id);
			
			var userId = $("#userID").val();
			var tagId = $("#selectedTagId").val();
				var selectedData = { tagId : id}
			
			var url_getEltConfigByTagId = "/app_Admin/user/"+userId+"/eltconfig/getEltConfigByTagId";
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCallObject(url_getEltConfigByTagId,'POST',selectedData,common.getcsrfHeader());
	 	    myAjax.done(function(result) {
		 	    	showAjaxLoader(false); 
		 	    	if(result.hasMessages){		    			  
		    			if(result.messages[0].code == "ERROR") {		    				  
							var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
		    				return false;
		    			} 
		    			if(result.messages[0].code == "SUCCESS") {
		    				eltStgConfig.getEltConfigKeyValuePairs(result);
		    			}		    			  		    			  	
			    	}
	 	    	});
			
			
		},
		displayCreateConfigTagWindow : function() {
			$("#selectedTagId").val("");
			$("#configTagsTable").hide();
			$(".createTagsInfoDiv").show();
		},
		moveUpDown: function(_obj) {
			var row = $(_obj).parents("tr:first");
	        if ($(_obj).is(".moveUpBtn")) {
	            row.insertBefore(row.prev());
	        } else {
	            row.insertAfter(row.next());
	        }
	        //this.showHideMoveArrowButtons();
		},
		showHideMoveArrowButtons: function() {
			var confiKeyValuePairTbl = $("#eltStgConfigTable tbody").children();
			if ( confiKeyValuePairTbl.length > 0) {
				var keysLength = confiKeyValuePairTbl.length -1 ;
				confiKeyValuePairTbl.each(function(index,_tr){
					if (index == 0) {
						$(_tr).find(".moveUpBtn").hide();
					}
					if (index == keysLength) {
						$(_tr).find(".moveDownBtn").hide();
					}
				});
			}
			
		},
		deleteSelectedKeyPair: function(_this){
			var userId = $("#userID").val();
			var deleteDiv = $(_this).parents(".stgRow");
			var id = deleteDiv.find(".keyId").val();
			selectData = {
					id:id	
			}
			if(id == null || id == ""){
				$(deleteDiv).remove();
			}else{
				var url_deleteEltStgKeyConfig = "/app_Admin/user/"+userId+"/eltconfig/deleteEltStgKeyConfigById";
				showAjaxLoader(true); 				
		 		var myAjax = common.postAjaxCallObject(url_deleteEltStgKeyConfig,'POST',selectData,common.getcsrfHeader());
		 	    myAjax.done(function(result) {
			 	    	showAjaxLoader(false); 
			 	    	if(result.hasMessages){		    			  
			    			if(result.messages[0].code == "ERROR") {		    				  
								var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
								$(".message-class").empty().append(message).show();
								setTimeout(function() { $(".message-class").hide(); }, 5000);
			    				return false;
			    			} 
			    			if(result.messages[0].code == "SUCCESS") {
			    				$(deleteDiv).remove();
			    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
								$(".message-class").empty().append(message).show();
								setTimeout(function() { $(".message-class").hide(); }, 5000);
								eltStgConfig.validateTableLength();
			    			}		    			  		    			  	
				    	}
		 	    	});
			}
			
			
		},
		saveTagInfo: function(){
			common.clearValidations(["#tagName,.activeMsg"]);
			var tagName = $("#tagName").val();
			var active = $("input[name='active']:checked").val();
			var userId = $("#userID").val();
			var tagId = $("#selectedTagId").val();
			var status = true;
			if(tagName == ""){
				common.showcustommsg("#tagName",globalMessage['anvizent.package.label.pleaseEnterTagName'],"#tagName");
				status = false;
			}
			if(!active){
				common.showcustommsg(".activeMsg",globalMessage['anvizent.package.label.PleaseChooseActiveStatus'],".activeMsg");
				status = false;
			}
			
			if(!status){
				return false;
			}else{
				var selectedData = {
						tagId : tagId,
						tagName : tagName,
						active : active
					}
				var url_saveEltConfigPairInfo = "/app_Admin/user/"+userId+"/eltconfig/saveEltConfigPairInfo";
				showAjaxLoader(true); 				
		 		var myAjax = common.postAjaxCall(url_saveEltConfigPairInfo,'POST',selectedData,common.getcsrfHeader());
		 	    myAjax.done(function(result) {
			 	    	showAjaxLoader(false); 
			 	    	if(result.hasMessages){		    			  
			    			if(result.messages[0].code == "ERROR") {		    				  
								var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
								$(".message-class").empty().append(message).show();
								setTimeout(function() { $(".message-class").hide(); }, 5000);
			    				return false;
			    			} 
			    			if(result.messages[0].code == "SUCCESS") {
			    				var resultId = result.object;
			    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
								$(".message-class").empty().append(message).show();
								setTimeout(function() { $(".message-class").hide(); }, 5000);
								$(".keyValueConfigPairsDiv,#saveConfigBtn,.createTagsInfoDiv").show();
								$("#selectedTagId").val(resultId);
			    			}		    			  		    			  	
				    	}
		 	    	});
			}
		},
		uploadPropertiesBuldData: function() {
			var fileupload = $('.configFile').val();
			if(fileupload == ''){
				common.showcustommsg(".configFile", globalMessage['anvizent.package.label.pleaseChooseaFile']);
				return false;
			}
			var fileExtension = fileupload.replace(/^.*\./, '');
		    if(fileExtension != 'properties') {
		    	common.showcustommsg(".configFile",globalMessage['anvizent.package.label.pleaseChoosePropertiesFile']);
		        return false;
		    }
			var configFile = $(".configFile").val();
			var formData = new FormData($("#fileUploadForm_direct")[0]);
			var userId = $("#userID").val();
			var url_uploadBulkConfigInfo = "/app_Admin/user/"+userId+"/eltconfig/uploadBulkConfigInfo";
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCallForFileUpload(url_uploadBulkConfigInfo,'POST',formData,common.getcsrfHeader());
	 	    myAjax.done(function(result) {
		 	    	showAjaxLoader(false); 
		 	    	if(result.hasMessages){		    			  
		    			if(result.messages[0].code == "ERROR") {		    				  
							var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
		    				return false;
		    			} 
		    			if(result.messages[0].code == "SUCCESS") {
		    				eltStgConfig.populateProperiesFileKeyValuePairs(result);		    				
		    				$("#bulkDataPopUp").modal('hide');
		    				$("#saveConfigBtn").show();
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		saveConfiKeyValuePairs: function(){
			var keysValuePairs = []﻿;
			var userId = $("#userID").val();
			var tagId = $("#selectedTagId").val();
			$("#eltStgConfigTable tbody tr.stgRow").each(function(index,object){
				var keysVal = $(object).find("input.stg_key").val();
				var valueVal = $(object).find("input.stg_value").val();
				var id = $(object).find(".keyId").val();
				var selectedData = {
						id : id,
						tagId : tagId,
						seqId : index,
						key : keysVal,
						value : valueVal
					}
				keysValuePairs.push(selectedData);
	        });
			
			var url_saveEltStgKeyConfig = "/app_Admin/user/"+userId+"/eltconfig/saveEltStgKeyConfig";
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCall(url_saveEltStgKeyConfig,'POST',keysValuePairs,common.getcsrfHeader());
	 	    myAjax.done(function(result) {
		 	    	showAjaxLoader(false); 
		 	    	if(result.hasMessages){		    			  
		    			if(result.messages[0].code == "ERROR") {		    				  
							var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
		    				return false;
		    			} 
		    			if(result.messages[0].code == "SUCCESS") {
		    				setTimeout(function(){ window.location.reload(); }, 1000);
		    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		deleteALLSelectedKeyPair: function(_this){
			var userId = $("#userID").val();
			var tagId = $("#selectedTagId").val();
			var id = $(".keyId").val();
			if(id == null || id == ""){
				$("#eltStgConfigTable tbody tr.stgRow").remove();
				 eltStgConfig.validateTableLength();
			}else{
				selectData = {
						tagId:tagId	
				}
				var url_deleteEltStgKeyConfig = "/app_Admin/user/"+userId+"/eltconfig/deleteALLEltStgKeyConfigById";
				showAjaxLoader(true); 				
		 		var myAjax = common.postAjaxCallObject(url_deleteEltStgKeyConfig,'POST',selectData,common.getcsrfHeader());
		 	    myAjax.done(function(result) {
			 	    	showAjaxLoader(false); 
			 	    	if(result.hasMessages){		    			  
			    			if(result.messages[0].code == "ERROR") {		    				  
								var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
								$(".message-class").empty().append(message).show();
								setTimeout(function() { $(".message-class").hide(); }, 5000);
			    				return false;
			    			} 
			    			if(result.messages[0].code == "SUCCESS") {
			    				$("#eltStgConfigTable tbody tr.stgRow").remove(); 
			    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
								$(".message-class").empty().append(message).show();
								setTimeout(function() { $(".message-class").hide(); }, 5000);
								eltStgConfig.validateTableLength();
			    			}		    			  		    			  	
				    	}
		 	    	});
				
			}
		},
		
		saveCopyiedConfigTagInfo : function(){
			 var id = $("#cloneTagId").val();
			 var tagName = $("#cloneTagName").val();
			 var userId = $("#userID").val();
			 var active = $("#activeTag").val();
			 var selectedData = {
					tagId : id,
					tagName : tagName,
					active : active
				}
			var url_saveEltConfigPairInfo = "/app_Admin/user/"+userId+"/eltconfig/saveEltCloneTagInfo";
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCall(url_saveEltConfigPairInfo,'POST',selectedData,common.getcsrfHeader());
	 	    myAjax.done(function(result) {
		 	    	showAjaxLoader(false); 
		 	    	if(result.hasMessages){		    			  
		    			if(result.messages[0].code == "ERROR") {
		    				common.showcustommsg("#cloneTagName", result.messages[0].text);
		    				return false;
		    			} 
		    			if(result.messages[0].code == "SUCCESS") {
		    				var resultId = result.object;
		    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
							$("#CloneTagPopUp").modal('hide');
							window.location.reload();
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		
	
}
if($('.eltConfigTags-page').length){
	eltStgConfig.initialPage();
	$(document).on("click",".addKeyPair",function(){
		eltStgConfig.addKeyValuePair(this);
	});
		
		$(document).on("click",".deleteKeyPair",function(){
			eltStgConfig.deleteSelectedKeyPair(this);
			eltStgConfig.validateTableLength();
		});
		
		$(document).on("click","#saveConfigBtn",function(){
			eltStgConfig.saveConfiKeyValuePairs();
		});
		
		$("#addStgKeyDiv").on("click",function(){
			eltStgConfig.addKeyValuePairAttheBeginning();
			eltStgConfig.validateTableLength();
		});
		
		$(".addConfigKeysBtn").on("click",function(){
			eltStgConfig.displayCreateConfigTagWindow();
		});
		
		$("#saveTagInfo").on("click",function(){
			eltStgConfig.saveTagInfo(); 
		});
		
		$(".tagsKeyPairs").on("click",function(){
			var id = this.value;
			eltStgConfig.displayConfigTagsInfo(id);
			eltStgConfig.displayConfigTagProperties(id);
		})
		
		$(".bulkData").on("click",function(){
			$("#bulkDataPopUp").modal('show');
			common.clearValidations([".configFile"]);
			$('.configFile').val('');
		});
		
		$("#uploadBulkData").on("click",function(){
			eltStgConfig.uploadPropertiesBuldData();
		});
		
		$("#tagsBack").on("click",function(){
			window.location.reload(); 
			$(".keyValueConfigPairsDiv,.createTagsInfoDiv").hide();
			$("#configTagsTable").show();
			
		});
		
		 $(document).on("click",".moveUpBtn,.moveDownBtn",function(){
			 eltStgConfig.moveUpDown(this)
		  });
		 
		 $('#deleteStgKeyDiv').on("click",function(){
			 eltStgConfig.deleteALLSelectedKeyPair(this);
			 
		 });
		 $(".cloneTag").on("click",function(){
			$("#CloneTagPopUp").modal('show');
			var tagName = $(this).attr("data-tagname");
			var tagId = $(this).attr("data-tagid");
			var active = $(this).attr("data-active")
			$("#cloneTagName").val(tagName+"_copy");
			$("#cloneTagId").val(tagId);
			$("#activeTag").val(active);
			common.clearValidations(["#cloneTagName"])
		 });
		 
		 $("#saveCloneTagData").on("click",function(){
			 eltStgConfig.saveCopyiedConfigTagInfo();
		 })
		 
}