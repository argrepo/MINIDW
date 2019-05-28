var headers = {};
var createContextParameter = {
		initialPage : function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
	},
	getContextParameters : function(userID){
	    var url_getContextparameter = "/app_Admin/user/"+userID+"/etlAdmin/getContextParameters";
		   var myAjax = common.loadAjaxCall(url_getContextparameter,'GET','',headers);
		    myAjax.done(function(result) {
		    	  if(result != null){ 
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") {
		    				  $('.createParamSucessFailure').empty();
			    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
			    			  $(".createParamSucessFailure").append(message);
			    			  setTimeout(function() { $(".createParamSucessFailure").hide().empty(); }, 10000);
			    			  return false;
			    			  } else if(result.messages[0].code == "SUCCESS") {
						    	  if(result.object != null && result.object.length > 0){
						    		  var tableRow='';
							    	  $.each(result.object, function (index, obj) {
							    			  tableRow += '<tr><td>'+obj.param_id+'</td><td>'+obj.param_name+'</td></tr>';
							    		});
							    	  $("#addContextParams").empty();
							    	  $('#addContextParams').append(tableRow); 
							    	  }
						    	      $("#parameterName").val('');
			    			  } 
		    		    }
		    		 }
		    });
	}
}
if($('.createContextParameter-page').length){
	createContextParameter.initialPage();
	$(document).on('click', '#createContextParameter', function(){
		$(".createParamSucessFailure").show();
		var userID = $("#userID").val();
		var parameterName = $("#parameterName").val();
		  if(parameterName.trim().length == 0){
			   common.showcustommsg("#parameterName", globalMessage['anvizent.package.label.paramNameShoudnotbeEmpty'],"#parameterName","#parameterName");
			   return false;
			  }
		  common.clearValidations(['#parameterName']);
		 showAjaxLoader(true);
		 var url_createContextparameter = "/app_Admin/user/"+userID+"/etlAdmin/createContextParameter/"+parameterName;
		   var myAjax = common.postAjaxCall(url_createContextparameter,'POST','',headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    	  if(result != null){
		    		  if(result.hasMessages){
		    			  if(result.messages[0].code == "ERROR") {
		    				  $('.createParamSucessFailure').empty();
			    			  var message = '<div class="alert alert-danger alert-dismissible" role="alert">'+''+result.messages[0].text+''+'</div>';
			    			  $(".createParamSucessFailure").append(message);
			    			  setTimeout(function() { $(".createParamSucessFailure").hide().empty(); }, 10000);
			    			  return false;
			    			  } else if(result.messages[0].code == "SUCCESS") {
			    				  if(result.object == 1){
					    			   $('.createParamSucessFailure').empty();
						    			var  message = '<div class="alert alert-success alert-dismissible" role="alert">'+
				  							''+''+result.messages[0].text+''+' <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'+
				  							'</div>';
						    			$('.createParamSucessFailure').append(message);
						    			setTimeout(function() { $(".createParamSucessFailure").hide().empty(); }, 10000);
						    			createContextParameter.getContextParameters(userID);
					    		  }else {
					    			    common.showcustommsg("#parameterName", result.messages[0].text,"#parameterName"); 
					    		  }
			    			  }
		    		    }
		    		 }
		    });
	
	});
	 
}