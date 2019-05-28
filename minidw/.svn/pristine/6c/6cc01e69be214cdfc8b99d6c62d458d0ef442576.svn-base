var  eltJobTagConfig = {
		initialPage : function() {
			$(".keyValueConfigPairsDiv, .createTagsInfoDiv ,.tblJobMappingConfig, #saveStgPairConfig").hide();
			$('.createTagsInfoDiv').hide();
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);	

			$('#configJobMappingTagsTbl tbody').sortable();

			$("#globalValues").select2({               
                allowClear: false,
                theme: "classic"
			});
			$("#configProp,#valuesProp,#statsProp").select2({               
                allowClear: false,
                theme: "classic"
			});
			
			$("#configTagsTbl").DataTable( {
		        "order": [[ 0, "desc" ]],"language": {
	                "url": selectedLocalePath
	            }
		    });
		    $("#configJobMappingTagsTbl").DataTable( {
		        "order": [[ 0, "desc" ]],"language": {
	                "url": selectedLocalePath
	            }
		    });
		},
		
		displayCreateConfigTagWindow : function() {
			$("#selectedJobId").val("");
			$("#tagName").val("");
			 $("#globalValues").val("0");
			 $("input[name='active'][value='true']").prop("checked",true);
			$("#configTagsTable,.tblJobMappingConfig").hide();
			$(".createTagsInfoDiv").show();
		},
		
		
		getEltJobTagsMappingInfo : function(result){
			debugger
			if(result != null){
				var table =$("#configJobMappingTagsTbl tbody");
				//var table = $('#configJobMappingTagsTbl').find("tbody");
				table.empty();
				var tableRow = '';
				var jobInfo = result.object;
				var id = result.object.tagId;
				var tagname = jobInfo.tagName;
				var tagId = jobInfo.globalValues.tagId;
				var active = jobInfo.active;
				
				var jobs = result.object.jobsList;
				for(var i=0;i<jobs.length;i++){
					var jobList = result.object.jobsList[i];
					var jopMappingId = jobList.id;
					var jobName = jobList.jobName;
					var activeStatus = jobList.activeStatus;
					var configtagId = jobList.configProp.tagId
					var configPropName = jobList.configProp.tagName;
					var valuesProptagId = jobList.valuesProp.tagId;
					var valuesPropTagName = jobList.valuesProp.tagName;
					var statProptagId = jobList.statsProp.tagId;
					var statPropTagName = jobList.statsProp.tagName;
					
					var derivedComponents = jobs[i].derivedComponent;
					var derivedTagIdArray = [];
					var derivedTagNameArray = [];
					for(var j=0;j<derivedComponents.length;j++){
						var derived = derivedComponents[j];
						derivedTagIdArray.push(derived.tagId)
						derivedTagNameArray.push(derived.tagName)
					}
					
					 var edit = "<button class='btn btn-primary btn-sm tablebuttons editEltJobMappingListById' data-jobName='"+jobName+"'  data-configprop='"+configtagId+"'  data-valueprop='"+valuesProptagId+"' " +
					 		" data-statusprop='"+statProptagId+"' data-jobmappingid='"+jopMappingId+"' data-derivedcomp='"+derivedTagIdArray+"' " +
					 		"data-activeStatus='"+activeStatus+"' data-derivedcomp='"+derivedTagIdArray+"' title='"+globalMessage['anvizent.package.label.edit']+"'>"+
						"<i class='fa fa-pencil' aria-hidden='true'></i></button>";
					 activeStatus ? 'Yes':'No';
					 tableRow += "<tr style='cursor: all-scroll;' class='configIds' data-mappingid='"+jopMappingId+"'>" + "<td>" + (i + 1) + "</td>"
					 	+ "<td>" +jopMappingId+ "</td>"
					 	+ "<td>" +jobName+ "</td>"
						+ "<td>" +(configPropName || '-')+ "</td>"
						+ "<td>" + ( valuesPropTagName || '-') + "</td>"
						+ "<td>" + (statPropTagName || '-') + "</td>"
						+ "<td>" + (derivedTagNameArray.length != 0 ? derivedTagNameArray.join('\n') :'-') + "</td>"
						+ "<td>" +activeStatus+ "</td>"
						+ "<td>" + edit + "</td>" + "<td></tr>"
					 
				}
				$("#tagName").val(tagname);
				$("#globalValues").val(tagId).change();
				active ? $('input[name=active][value=true]').prop('checked',true) : $('input[name=active][value=false]').prop('checked',true);
				table.append(tableRow);
			}
		},
		
		saveTagInfo: function(){
			debugger
			var tagName = $("#tagName").val();
			var tagId = $("#globalValues option:selected").val();
			if (tagId == 0) {
				tagId = null;
			}
			var active = $("input[name=active]:checked").val();
			var userId = $("#userID").val();
			var id = $("#selectedJobId").val();
			var selectedData = {
					tagId : id,
					tagName : tagName,
					globalValues :{
						tagId : tagId
					},
					active : active
				}
			var url_saveEltJobTagInfo = "/app_Admin/user/"+userId+"/eltconfig/saveEltJobTagInfo";
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCall(url_saveEltJobTagInfo,'POST',selectedData,common.getcsrfHeader());
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
		    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
							var id = result.object;
							$("#selectedJobId").val(id);
							$("#selectedTblJobId").val(id);
							$(".tblJobMappingConfig").show();
							$("#configJobMappingTagsTbl tbody").empty();
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		
		
		displayJobTagsInfo: function(id) {
			debugger
			$("#configTagsTable").hide();
			$(".createTagsInfoDiv,.tblJobMappingConfig,#saveConfigBtn").show();
			$("#selectedJobId").val(id);
			var userId = $("#userID").val();
			var tagId = $("#selectedJobId").val();
			
			var url_getEltConfigByTagId = "/app_Admin/user/"+userId+"/eltconfig/getEltJobById/"+tagId;
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCallObject(url_getEltConfigByTagId,'GET','',common.getcsrfHeader());
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
		    				eltJobTagConfig.getEltJobTagsMappingInfo(result);
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
	
		addDerivedComponentDiv: function(_this) {
			debugger
			
			var thisDiv = $("#derivedCompTblTags tbody");
			var trDiv =  $("#derivedCompTblTags tfoot tr.clonedDerivedTr").clone().removeClass('hidden');
			thisDiv.append($(trDiv))
		},
		addDerivedJobTags: function(_this) {
			debugger
			
			var thisDiv = $("#derivedCompTblTags tbody");
			var trDiv = $("#derivedCompTblTags tfoot tr.clonedDerivedTr").clone().removeClass('hidden');
			trDiv.find(".derivedComponent").val('0');
			thisDiv.append($(trDiv))
			
		},
		
		deleteSelectedDerivedDiv : function(_this) {
			debugger
			var deleteDiv = $(_this).parents(".clonedDerivedTr");
			$(deleteDiv).remove();
		},
		
		saveJobMappingInfo: function(){
			debugger
			var derivedComponentList = [];
			var tagName = $("#tagName").val();
			var configPropId = $("#configProp").val();
			var valuesPropId = $("#valuesProp").val();
			var statsPropId = $("#statsProp").val();
			if (configPropId == '0') {
				configPropId = null;
			}
			if (valuesPropId == '0') {
				valuesPropId = null;
			}
			if (statsPropId == '0') {
				statsPropId = null;
			}
			var jobTagId = $("#selectedJobId").val();
			var jobName = $("#jobName").val();
			var activeStatus = $("input[name='activeStatus']:checked").val();
			var derivedComponent = $(".derivedComponent",$("#derivedCompTblTags tbody"));
			$(derivedComponent).each(function(){
				if (this.value != '0') {
					derivedComponentList.push({"tagId":this.value})	
				}
			})
			var userId = $("#userID").val();
			var id = $("#selectedJobMappingId").val();
			
			if(id == null || id == "")
				var jobSeq = parseInt($("#seqRowCount").val())+1;
			else
				var jobSeq = $("#indexId").val();
			var selectedData = {
					id : id,
					jobName : jobName,
					jobTagId :jobTagId, 
					jobSeq : jobSeq,
					activeStatus : activeStatus,
					configProp :{
						tagId : configPropId
					},
					valuesProp :{
						tagId : valuesPropId
					},
					statsProp :{
						tagId : statsPropId
					},
					derivedComponent : derivedComponentList
					
				}
			var url_saveEltJobMappingInfo = "/app_Admin/user/"+userId+"/eltconfig/saveEltJobMappingInfo";
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCall(url_saveEltJobMappingInfo,'POST',selectedData,common.getcsrfHeader());
	 	    myAjax.done(function(result) {
		 	    	showAjaxLoader(false); 
		 	    	if(result.hasMessages){		    			  
		    			if(result.messages[0].code == "ERROR") {		    				  
							var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							$("#jobDataMaippingEditPopUp").modal('hide');
							setTimeout(function() { $(".message-class").hide(); }, 5000);
		    				return false;
		    			} 
		    			if(result.messages[0].code == "SUCCESS") {
		    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
							$("#jobDataMaippingEditPopUp").modal('hide');
							$(".refreshBtn").click();
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		
		
		getJobMAppingInfoByMappingId: function(_this,id) {
			debugger
			$("#configTagsTable").hide();
			$(".createTagsInfoDiv,.tblJobMappingConfig,#saveConfigBtn").show();
			
			$("#selectedJobMappingId").val(id);
			
			var userId = $("#userID").val();
			var jobMappingId = $("#selectedJobMappingId").val();
			
			var jobName = $(_this).attr('data-jobName');
			var configPropID = $(_this).attr('data-configprop');
			var valueProp = $(_this).attr('data-valueprop');
			var statusProp = $(_this).attr('data-statusprop');
			var derivedcomp = $(_this).attr('data-derivedcomp');
			var activeStatus = $(_this).attr('data-activeStatus');
			
			$("#jobName").val(jobName);
			$("#configProp").val(configPropID).change();
			$("#valuesProp").val(valueProp).change();
			$("#statsProp").val(statusProp).change();
			activeStatus == "true" ? $("input[name='activeStatus'][value='true']").prop("checked",true) :  $("input[name='activeStatus'][value='false']").prop("checked",true)
			
			var derivedCompTbl = $('#derivedCompTblTags').find("tbody");
			derivedCompTbl.empty();
			var derivedVal = [];
			if (derivedcomp.trim() != "") {
				derivedVal = derivedcomp.split(',');
				 for(var i=0;i<derivedVal.length;i++){
					 var trDiv = $("#derivedCompTblTags tfoot tr.clonedDerivedTr").clone().removeClass('hidden');
					 trDiv.find(".derivedComponent").val(derivedVal[i]).select2({               
			                allowClear: false,
			                theme: "classic"
						});;
						derivedCompTbl.append($(trDiv)) 
				 }
			}
			
				 
		},
		
		saveEltJobSequenceById: function(){
			debugger
			var jobSequences = []﻿;
			var userId = $("#userID").val();
			
			$("#configJobMappingTagsTbl tbody tr.configIds").each(function(index,object){
				console.log(index,object);
				var mappingId= $(object).attr('data-mappingid');
				console.log(index,mappingId);
				var selectedData = {
						id : mappingId,
						jobSeq : index
				}
				jobSequences.push(selectedData);
			});
			if (jobSequences.length == 0) {
				var message = '<div class="alert alert-danger alert-dismissible" role="alert">Job details not found</div>';
				$(".message-class").empty().append(message).show();
				setTimeout(function() { $(".message-class").hide(); }, 5000);
				return false;
			}
			var url_saveEltStgKeyConfig = "/app_Admin/user/"+userId+"/eltconfig/saveEltJobSequenceInfo";
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCall(url_saveEltStgKeyConfig,'POST',jobSequences,common.getcsrfHeader());
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
		    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
							$('.refreshBtn').click();
							
		    			}		    			  		    			  	
			    	}
	 	    	});
		}

		
		
}
if($('.eltJobTags-page').length){
	eltJobTagConfig.initialPage();
	
	$(document).on("click",".addJobDerivedCompDiv",function(){
		debugger
		eltJobTagConfig.addDerivedJobTags(this);
	});
		
		$(document).on("click",".deleteJobDerivedCompDiv",function(){
			debugger
			eltJobTagConfig.deleteSelectedDerivedDiv(this);
		});
		
		$(document).on("click","#saveConfigBtn",function(){
			debugger
			eltStgConfig.saveConfiKeyValuePairs();
		});
		
		$("#addDerivedCompDiv").on("click",function(){
			eltJobTagConfig.addDerivedComponentDiv(this);
		});
		
		$(".addJobTagsBtn").on("click",function(){
			eltJobTagConfig.displayCreateConfigTagWindow();
		});
		
		$("#saveTagInfo").on("click",function(){
			debugger
			eltJobTagConfig.saveTagInfo();
		});
		
		$(".jobTagById").on("click",function(){
			debugger
			 $("#tagName").val("");
			 $("#globalValues").val("0");
			var id = this.value;
			$("#selectedTblJobId").val(id);
			eltJobTagConfig.displayJobTagsInfo(id);
			
		})
		
		$("#tagsBack").on("click",function(){
			window.location.reload();
			$("#configTagsTable").show();
			$(".createTagsInfoDiv,.tblJobMappingConfig").hide();
			
		});
		
		$(document).on("click",".addJobMappingBtn",function(){
			debugger
			var rowCount = $('#configJobMappingTagsTbl tbody tr').length;
			$("#seqRowCount").val(rowCount);
			$("#jobName").val("");
			$("#configProp").val("0").change();
			$("#valuesProp").val("0").change();
			$("#statsProp").val("0").change();
			$("#derivedCompTblTags tbody").empty();
			$("#selectedJobMappingId").val("");
			$('input[name="active"]').prop('checked',false);
			$("#jobDataMaippingEditPopUp").modal('show');
		})

		$("#saveJobMappingData").on("click",function(){
			debugger
			eltJobTagConfig.saveJobMappingInfo();
		});
		
		$(document).on("click",".editEltJobMappingListById",function(){
			debugger
			$("#jobDataMaippingEditPopUp").modal('show');
			var id = $(this).attr('data-jobmappingid');
			 var row_index = $(this).parents('tr').index();
			$("#indexId").val(row_index);
			eltJobTagConfig.getJobMAppingInfoByMappingId(this,id);
		})
		
		$(document).on("click",".deleteJobDerivedCompDiv",function(){
			var deleteDiv = $(this).parents(".clonedDerivedTr");
			$(deleteDiv).remove();
		});
		$(document).on("click",".refreshBtn",function(){
			var id = $('#selectedTblJobId').val();
			eltJobTagConfig.displayJobTagsInfo(id);
		})
		$(document).on("click",".updateSeq",function(){
			eltJobTagConfig.saveEltJobSequenceById();
		})
		
}