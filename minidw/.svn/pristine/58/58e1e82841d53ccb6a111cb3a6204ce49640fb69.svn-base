var  appDbVersionTableScript = {
		initialPage : function() {
			$("#appDbVersionTableScriptTable").dataTable();
			setTimeout(function() { $("#pageErrors").hide().empty(); }, 5000);	 
		},
		
		appDbVersionFormValidation : function(){
	  	       	var version = $("#version").val();
	  	        var appScript = $("#appDbScript").val();
	  	        var minidwScript = $("#minidwScript").val();
	  	        common.clearValidations(["#version, #appDbScript,#minidwScript"]);
	  	        var validStatus=true;
	      	    if(version == 0 ){
		  	    	common.showcustommsg("#version", globalMessage['anvizent.package.label.pleaseEnterVersionNumber'],"#version");
		  	    	validStatus=false;
	      	    }
	      	    if(appScript == ''){
		  	    	common.showcustommsg("#appDbScript", globalMessage['anvizent.package.label.PleaseChoosedescription'],"#appDbScript");
		  	    	validStatus=false;
	      	    }
		      	if(minidwScript == ''){
		  	    	common.showcustommsg("#minidwScript", globalMessage['anvizent.package.label.PleaseEnterFilePath'],"#minidwScript");
		  	    	validStatus=false;
	      	    }
	      	    return validStatus;
		 },
			
		 	showDBScript : function(type,result){	
				var sqlScript = result.object;
				
				var params = "";
			 	var ua = window.navigator.userAgent;
			    var msie = ua.indexOf("MSIE ");
			    
			    if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./))  // If Internet Explorer, return version number
			    {
			    	params = [
				              //'height='+screen.height,
				              'width='+screen.width,
				              'fullscreen=no' // only works in IE, but here for completeness
				          ].join(',');
			    } else {
			    	params = [
				              'height='+screen.height,
				              'width='+screen.width,
				          ].join(',');
			    }
		          popup = window.open('about:blank', '_blank', params);
		          console.log(popup);
		          if (popup) {
		        	  popup.moveTo(0,0);
			          popup.document.title = (type == "m"? "MINI-DW Script":"AppDB Script");
			          popup.document.body.innerHTML = "<pre>"+sqlScript+"</pre>";
			          if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
			        	  popup.addEventListener (
				        	        "load",
				        	        function () {
				        	            var destDoc = popup.document;
				        	            destDoc.open ();
				        	            destDoc.title = "Table script";
				        	            destDoc.write ('<html><head></head><body><pre>'+sqlScript+'</pre></body></html>');
				        	            destDoc.close ();
				        	        },
				        	        false
				        	    );
			          }
		          } else {
		        	  alert("Please enable pop-ups for this website ");
		          }
				
			},
}
if($('.appDbVersionTableScripts-page').length){
	appDbVersionTableScript.initialPage();
	 $("#updateAppDBVersion,#addAppDBVersion").on('click',function() {
			var status= appDbVersionTableScript.appDbVersionFormValidation();
			if(!status){
				return false;
			}
			else{
				$("#appDBVersionTableScripts").prop("action",$("#addUrl").val()); 
				this.form.submit();
			    showAjaxLoader(true);
			}
	 });
	  
	 $(document).on("click",".viewAppDBScript",function(){
			var userID = $("#userID").val();
			var id = $(this).val();
			var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
			showAjaxLoader(true);
			var url_getAppDbScriptById = "/app_Admin/user/"+userID+"/etlAdmin/getAppDBScriptById/"+id;
			   var myAjax = common.postAjaxCallObject(url_getAppDbScriptById,'GET','',headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;		    				
			    			  }
			    			  if(result.messages[0].code=="SUCCESS"){		    				
			    				  appDbVersionTableScript.showDBScript("a",result);
			    			  }
			    		  }
					}
			});
		});
	 
	 
	 $(document).on("click",".viewMinidwDBScript",function(){
			var userID = $("#userID").val();
			var id = $(this).val();
			var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
			showAjaxLoader(true);
			var url_getMinidwDBScriptById = "/app_Admin/user/"+userID+"/etlAdmin/getMinidwDBScriptById/"+id;
			   var myAjax = common.postAjaxCallObject(url_getMinidwDBScriptById,'GET','',headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;		    				
			    			  }
			    			  if(result.messages[0].code=="SUCCESS"){		    				
			    				  appDbVersionTableScript.showDBScript("m",result);
			    			  }
			    		  }
					}
			});
		});
	 
	 if($("#pageMode").val() == 'edit'){
		 var userID = $("#userID").val();
			var id = $("#id").val();
			var token = $("meta[name='_csrf']").attr("content");
	 		var header = $("meta[name='_csrf_header']").attr("content");
	 		headers[header] = token;
			showAjaxLoader(true);
			var url_getMinidwDBScriptById = "/app_Admin/user/"+userID+"/etlAdmin/getMinidwDBScriptById/"+id;
			   var myAjax = common.postAjaxCallObject(url_getMinidwDBScriptById,'GET','',headers);
				myAjax.done(function(result) {
					showAjaxLoader(false);
					if(result != null){
			    		  if(result.hasMessages){
		    				  if(result.messages[0].code=="ERROR"){
		    					  common.showErrorAlert(result.messages[0].text);
		    					  var message = result.messages[0].text;		    				
			    			  }
			    			  if(result.messages[0].code=="SUCCESS"){		    				
			    				  var sqlScript = result.object;
			    				  $("#minidwScript").val(sqlScript);
			    			  }
			    		  }
					}
			}); 
				
				var url_getAppDbScriptById = "/app_Admin/user/"+userID+"/etlAdmin/getAppDBScriptById/"+id;
				   var myAjax = common.postAjaxCallObject(url_getAppDbScriptById,'GET','',headers);
					myAjax.done(function(result) {
						showAjaxLoader(false);
						if(result != null){
				    		  if(result.hasMessages){
			    				  if(result.messages[0].code=="ERROR"){
			    					  common.showErrorAlert(result.messages[0].text);
			    					  var message = result.messages[0].text;		    				
				    			  }
				    			  if(result.messages[0].code=="SUCCESS"){
				    				  var sqlScript = result.object;
				    				  $("#appDbScript").val(sqlScript);
				    			  }
				    		  }
						}
				});
	 }
}