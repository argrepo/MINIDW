
var submitModeVsMasters = {
	"local" : "local",
	"yarn" : "yarn",
	"standalone" : "STAND_ALONE",
	"mesos" : "mesos://",
	"kubernetes" : "k8s://",
}

var submitModeVsHostAndPortAvailability = {
		"local" : false,
		"yarn" : false,
		"standalone" : true,
		"mesos" : true,
		"kubernetes" : true,
}

var  eltMasterConfiguration = {
		initialPage : function() {
			$(".createMasterDiv").hide();
			$("#configTagsTbl").dataTable();
			
			$(".clusterMode, .hostAndPortDivs").addClass('hidden');
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);
		},
		
		
		addKeyValuePairAttheBeginning: function() {
			var confiKeyValuePairTbl = $("#eltStgConfigTable tbody");
			var trDiv = $(".cloneCopystgRow").clone().addClass('stgRow').removeClass('cloneCopystgRow hidden');
			confiKeyValuePairTbl.prepend(trDiv);
		},
		
		addKeyValuePair: function(_this) {
			var thisRow = $(_this).parents("tr");
			var trDiv = $(".cloneCopystgRow").clone().addClass('stgRow').removeClass('cloneCopystgRow hidden');
			trDiv.find("input").val("");
			trDiv.insertAfter($(thisRow))
			
		},
		
		deleteSelectedKeyPair : function(_this){
			var deleteDiv = $(_this).parents(".stgRow");
			$(deleteDiv).remove();
		},
		
		
		saveMasterConfig : function(){
			var keysValuePairs = []﻿;
			var userId = $("#userID").val();
			
			var name = $("#name").val();
			var sparkJobPath = $("#sparkJobPath").val();
			var eltClassPath = $("#eltClassPath").val();
			var eltLibraryPath = $("#eltLibraryPath").val();
			
			
			var sparkSubmitMode = $("#sparkSubmitMode").val();
			
			var master = $("#master").val();
			var sparkMaster = sparkSubmitMode;
			
			var host = $("#host").val();
			var port = $("#port").val();
			
			var deployMode = $("#deployMode").val();
			var userName = $("#userName").val();
			var authenticationType = $("input[name='authenticationType']:checked").val();
			var password = $("#password").val();
			
			
			var jobSubmitMode = $(".jobSubmitMode").val();
			
			var masterDefault =  $("input[name='masterDefault']:checked").val();
			var sourceType = $("input[name='sourceType']:checked").val();
			var ppkFile = $("#ppkFile").val();
			
			var environmentVar = $("#eltStgConfigTable tbody tr.stgRow");
			environmentVar.each(function(index,object){
				var keysVal = $(object).find("input.stg_key").val();
				var valueVal = $(object).find("input.stg_value").val();
				
				keysValuePairs.push(({"key":keysVal,"value":valueVal}));
	        });
			
			
			var id = $("#selectedId").val();
			var selectedData = {
					id : id,
					name : name,
					sparkJobPath : sparkJobPath,
					eltClassPath : eltClassPath,
					eltLibraryPath : eltLibraryPath,
					sparkSubmitMode : sparkSubmitMode,
					master : master,
					sparkMaster : sparkMaster,
					deployMode : deployMode,
					host :host,
					port : port,
					authenticationType : authenticationType,
					password : password,
					userName : userName,
					ppkFile : ppkFile,
					
					sourceType : sourceType,
					jobSubmitMode : jobSubmitMode,
					masterDefault : masterDefault,
					environmentVariables : keysValuePairs,
					active:true

			}
			var url_saveEltMasterConfig = "/app_Admin/user/"+userId+"/eltconfig/saveEltMasterConfig";
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCall(url_saveEltMasterConfig,'POST',selectedData,common.getcsrfHeader());
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
		    				window.location.reload();
		    				var message = '<div class="alert alert-success alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
							$(".message-class").empty().append(message).show();
							setTimeout(function() { $(".message-class").hide(); }, 5000);
							$("#masterConfigTable").show();
							$(".createMasterDiv").hide();
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		
		uploadPPKFileOld: function(){
			var formData = new FormData($("#fileUploadForm_direct")[0]);
			var userId = $("#userID").val();
			var url_uploadELTPPkFile = "/app_Admin/user/"+userId+"/eltconfig/uploadELTPPkFile";
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCallForFileUpload(url_uploadELTPPkFile,'POST',formData,common.getcsrfHeader());
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
		    				ppkFile = result.object;
		    				if(ppkFile != null && ppkFile != ""){
		    					eltMasterConfiguration.saveMasterConfig(ppkFile);
		    				}
		    				
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		
		uploadPPKFile: function(){
			eltMasterConfiguration.saveMasterConfig();
		},
		
		populateKeyValue: function(confiKeyValuePairTbl,key,value) {
			var trDiv = $(".cloneCopystgRow").clone().addClass('stgRow').removeClass('cloneCopystgRow hidden');
			trDiv.find(".stg_key").val(key || '');
			trDiv.find(".stg_value").val(value || '');
			confiKeyValuePairTbl.append($(trDiv));
		},
		
		getMasterConfigurationInfo : function(result){
			if(result != null){
				var resultInfo = result.object;
				var id =  resultInfo.id;
				var name = resultInfo.name;
				var sparkJobPath = resultInfo.sparkJobPath;
				var eltClassPath = resultInfo.eltClassPath;
				var eltLibraryPath = resultInfo.eltLibraryPath;
				var master = resultInfo.master;
				var deployMode = resultInfo.deployMode;
				var sparkMaster = resultInfo.sparkMaster;
				var makeDefault = resultInfo.masterDefault;
				var sourceType = resultInfo.sourceType;
				var sparkSubmitMode = resultInfo.sparkSubmitMode;
				var protocol = resultInfo.protocol;
				var host = resultInfo.host;
				var port = resultInfo.port;
				var username = resultInfo.userName;
				var jobSubmitMode = resultInfo.jobSubmitMode;
			
				var authenticationType = resultInfo.authenticationType;
				var password = resultInfo.password;
				var ppkFile = resultInfo.ppkFile;
				
				
				if(master != 'local') {
					$(".masterDeployMode").removeClass('hidden')
				} else {
					$(".masterDeployMode").addClass('hidden')
				}
				var isHostAndPosrtAvailable = submitModeVsHostAndPortAvailability[sparkSubmitMode];
				
				if (isHostAndPosrtAvailable || ( deployMode == 'cluster' && sparkSubmitMode == "yarn")) {
					$(".hostAndPortDivs").removeClass("hidden");
				} else {
					$(".hostAndPortDivs").addClass("hidden");
				}
				
				var confiKeyValuePairTbl = $('#eltStgConfigTable').find("tbody");
				
					for(var i=0;i<result.object.environmentVariables.length;i++){
						var key = result.object.environmentVariables[i].key;
						var value = result.object.environmentVariables[i].value;
						this.populateKeyValue(confiKeyValuePairTbl,key,value);
					}
				}
				
				$("#selectedId").val(id);
				$("#name").val(name);
				$("#sparkJobPath").val(sparkJobPath);
				$("#eltClassPath").val(eltClassPath);
				$("#eltLibraryPath").val(eltLibraryPath);
				$("#master").val(master);
				$("#deployMode").val(deployMode);
				$(".sparkMaster").val(sparkMaster);
				$("#sparkSubmitMode").val(sparkSubmitMode);
				$(".jobSubmitMode").val(jobSubmitMode);
				
				$("#host").val(host); 
				$("#port").val(port);
				
				if(deployMode == 'cluster'){
					$(".clusterMode").removeClass('hidden');
					$("#userName").val(username);
					if(authenticationType == 'password'){
						$("input[name='authenticationType'][value='password']").prop("checked",true);
						$(".authTypePassword").removeClass('hidden');
						$(".authTypePpkFile").addClass('hidden');
						$("#password").val(password);
					} else{
						$("input[name='authenticationType'][value='ppkfile']").prop("checked",true);
						$(".authTypePpkFile").removeClass('hidden');
						$(".authTypePassword").addClass('hidden');
						$("#ppkFile").val(ppkFile);
					}
				}
				
				
				if(sparkSubmitMode != 'local')
					$(".masterDeployMode").removeClass('hidden');
				
				makeDefault ?  $('input[name="masterDefault"][value="true"]').prop('checked',true) : $('input[name="masterDefault"][value="false"]').prop('checked',true);
				sourceType == "local" ? $('input[name="sourceType"][value="local"]').prop('checked',true) : $('input[name="sourceType"][value="s3"]').prop('checked',true);
		},
		
		displayMasterConfigInfo: function(id) {
			var userId = $("#userID").val();
			var url_getMasterConfigById = "/app_Admin/user/"+userId+"/eltconfig/masterConfig/"+id;
			showAjaxLoader(true); 				
	 		var myAjax = common.postAjaxCallObject(url_getMasterConfigById,'GET','',common.getcsrfHeader());
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
		    				eltMasterConfiguration.getMasterConfigurationInfo(result);
		    			}		    			  		    			  	
			    	}
	 	    	});
		},
		
		downloadPPKFile : function(filePath){
			var userId = $("#userID").val();
			window.open(adt.contextPath+"/app_Admin/user/"+userId+"/eltconfig/downloadPPkFile/"+filePath,"_blank","");
		}
		
}
if($('.eltMasterConfiguration-page').length){
	eltMasterConfiguration.initialPage();
	
	$('.addMasterInfoBtn').on("click",function(){
		$("#masterConfigTable").hide();
		$(".createMasterDiv").show();
	});
	
	$("#sparkSubmitMode").on("change",function(){
		$("#deployMode").val(0);
		$("#deployMode").trigger("change");
		
		var _this = $(this).val();
		$("#master").val(submitModeVsMasters[_this]);
		$("#master").prop("readonly",true);
		
		var isHostAndPosrtAvailable = submitModeVsHostAndPortAvailability[_this];
		
		
		if (isHostAndPosrtAvailable) {
			$(".hostAndPortDivs").removeClass("hidden");
			$("#host,#port").val("");
		} else {
			$(".hostAndPortDivs").addClass("hidden");
		}
		
		if(_this == 'local'){
			$(".masterDeployMode").addClass('hidden');
		} else{
			$(".masterDeployMode").removeClass('hidden');
		}
		
		if(_this == 'mesos' || _this == 'kubernetes'){
			$("#deplymentModeClient").hide();
		} else {
			$("#deplymentModeClient").show();
		}
	});
		
	$("#addStgKeyDiv").on("click",function(){
		eltMasterConfiguration.addKeyValuePairAttheBeginning();
	});	
	
	$(document).on("click",".addKeyPair",function(){
		eltMasterConfiguration.addKeyValuePair(this);
	});
	

	$(document).on("click",".deleteKeyPair",function(){
		eltMasterConfiguration.deleteSelectedKeyPair(this);
	});
	
	$("#saveConfigBtn").on("click",function(){
		var authenticationType = $("input[name='authenticationType']:checked").val();
		eltMasterConfiguration.saveMasterConfig();
	});
	$(document).on("click",".masterById",function(){
		id = this.value;
		$('#masterConfigTable').hide();
		$('.createMasterDiv').show();
		eltMasterConfiguration.displayMasterConfigInfo(id);
	});
	$("#masterBack").on("click",function(){
		window.location.reload();
		$('#masterConfigTable').show();
		$('.createMasterDiv').hide();
	});
		
	$("#deployMode").on('change',function(){
		var _this = $(this).val();
		var sparkSubmitMode = $("#sparkSubmitMode").val();
		
		if(_this == 'cluster'){
			if (sparkSubmitMode == "yarn") {
				$(".hostAndPortDivs").removeClass("hidden");
			}
			$(".clusterMode").removeClass('hidden');
			
		} else {
			if (sparkSubmitMode == "yarn") {
				$(".hostAndPortDivs").addClass("hidden");
				$("#host,#port").val("");
			}
			$(".clusterMode").addClass('hidden');
			$("#userName,#password,#ppkFile").val("");
			$("input[name='authenticationType']").prop("checked",false);
			$("input[name='authenticationType']").trigger("change");
		}
	});	
	
	//$("[name='authenticationType']").prop("checked",false);
	$("input[name='authenticationType']").on('change',function(){
		var _this = $(this).val();
		$("#password,#ppkFile").val("");
		$(".authTypePpkFile,.authTypePassword").addClass('hidden');
		if($(this).is(":checked")){
			if(_this == 'password'){
				$(".authTypePassword").removeClass('hidden');
			}else{
				$(".authTypePpkFile").removeClass('hidden');
			}
		}
		
		
	});	
	
	$(document).on('click', '#downloadPPkFile', function() {
		var filePath = $("#ppkFile").val();
		eltMasterConfiguration.downloadPPKFile(filePath)
	});
}