
	$(".toggle-panel").click(function(){
		if($('.toggle-panel').hasClass('closepanel') == true)
		{
			 $(".left-area").css("width", "220px")
			    $(".navLeft_submenu").css("display", "block")
			    $(".rightdiv").css({'paddingLeft': '220px'})
			    $( ".toggle-panel" ).removeClass( "closepanel" );
		}else{
			$( ".toggle-panel" ).addClass( "closepanel" );
			    $(".left-area").css("width", "55px")
			    $(".navLeft_submenu").css("display", "none")
			    $(".rightdiv").css({'paddingLeft': '55px'})
		}
	});
	$( document ).ready(function() {
		$('#navbar').slimScroll({
		    height: '100%',
		    start: $('.activemenu_link'),
		    size: '12px',
		   alwaysVisible: false,
		});
		});

var common = {
		
		validateDataResponse: function(result) {
			var status = false;
			if (result != null && result.hasMessages) {
				if (result.messages[0].code == "SUCCESS") {
					status = true;
				} else {
					this.displayMessages(result.messages);
				}
			} else {
				var messages = [ {
					code : globalMessage['anvizent.message.error.code'],
					text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
				} ];
				this.displayMessages(messages);
			}
			return status;
		},
		
		loadAjaxCall : function(url, type, data) {
			var header = this.getcsrfHeader();
			return this.loadAjaxCall(url, type, data,header);
		},
		
		loadAjaxCall : function(url, type, data,header) {
			if (!header) {
				header = this.getcsrfHeader();
			}
			var contextPath = adt.contextPath;
			
			var myAjax =$.ajax({
		        url: contextPath+url,
		        headers:header,
		        type: type,
		        data: (type.toLowerCase()=="get" ? "":JSON.stringify(data)),
		        cache: false,
		        contentType: "application/json; charset=utf-8",
			    error: function (jqXHR, exception) {
			    	common.errorMessages(jqXHR, exception);
			        }
		    });
			return myAjax;
		},
	
		postAjaxCall : function(url, type, data) {
			var header = this.getcsrfHeader();
			return this.postAjaxCall(url, type, data,header);
		},
		
	postAjaxCall : function(url, type, data,header) {
		if (!header) {
			header = this.getcsrfHeader();
		}
		var contextPath = adt.contextPath;
		
		var myAjax =$.ajax({
	        url: contextPath+url,
	        type: type,
	        headers:header,
	        data: (type.toLowerCase()=="get" ? "":JSON.stringify(data)),
	        dataType: "json",
	        contentType: "application/json; charset=utf-8",
		    error: function (jqXHR, exception) {
		    	common.errorMessages(jqXHR, exception);
	        }
	    });
		return myAjax;
	},
	

	postAjaxCallObject : function(url, type, data) {
		var header = this.getcsrfHeader();
		return this.postAjaxCallObject(url, type, data,header);
	},
	
	
	postAjaxCallObject : function(url, type, data,header) {
		if (!header) {
			header = this.getcsrfHeader();
		}
		var myAjax = $.ajax({
	    	 url: adt.contextPath+url,
	        type: type,
	        headers:header,
	        data: (type.toLowerCase()=="get" ? "":data),
	        dataType: "json",
	        error: function(jqXHR, exception) {
	        	common.errorMessages(jqXHR, exception);
	        } 
	    });
		
		return myAjax;
	},
	
	postAjaxCallForFileUpload : function(url, type, data) {
		var header = this.getcsrfHeader();
		return this.postAjaxCallForFileUpload(url, type, data,header);
	},
	
	postAjaxCallForFileUpload : function(url, type, data,header) {
		
		var contextPath = adt.contextPath;
		
		var myAjax =$.ajax({
	        url: contextPath+url,
	        type: type,
	        headers:header,
	        data: data,
	        async: true,
            cache: false,
            contentType: false,
            processData: false,
		    error: function (jqXHR, exception) {
		    	common.errorMessages(jqXHR, exception);
		        }
	    });
		return myAjax;
	},
	
	 errorMessages : function (jqXHR, exception) {
		 showAjaxLoader(false);
		 console.log("jqXHR",jqXHR,exception);
		 var msg = '';
	        if (jqXHR.status === 0) {
	        	console.log("jqXHR",jqXHR);
	            msg = globalMessage['anvizent.package.label.unableToConnectPleaseVerifyNetwork'];
	        } else if (jqXHR.status == 401 || jqXHR.status == 403) {
	            msg = "session expired";
	            window.location.reload();
	        } else if (jqXHR.status == 404) {
	        	window.location = adt.appContextPath+"/errors/404";
	        	return false;
	            msg = 'Requested page not found. [404]';
	        } else if (jqXHR.status == 405) {
	            msg = 'Request method not allowed [405]';
	        } else if (jqXHR.status == 500) {
	        	alert(jqXHR.statusText);
	        	console.log(jqXHR.responseText);
	        	//window.location = adt.appContextPath+"/errors/500";
	        	return false;
	        } else if (exception === 'parsererror') {
	            msg = globalMessage['anvizent.package.label.requestedJSONParseFailed'];
	        } else if (exception === 'timeout') {
	            msg = 'Time out error.';
	        } else if (exception === 'abort') {
	            msg = globalMessage['anvizent.package.label.ajaxRequestAborted'];
	        } else {
	            msg = globalMessage['anvizent.package.label.uncaughtError']+' ' + jqXHR.responseText;
	        }
	        alert(msg);
	},
	displayMessages: function(messages) {
		$('#pageErrors').empty();
		   $.each(messages, function(key, message) {
		    if (message.property) {
		     var field = '#'+message.property;
		     var errorField = '#' + message.property + '.errors';
		     $(field).addClass('errorField');
		     $(field).after('<span id="' + message.property + '.errors" class="error">' + message.text +" "+message.code+ '</span>');
		    } else {
		    	
		     $('#pageErrors').append('<div class="alert '+(message.code === 'SUCCESS' ? 'alert-success' : 'alert-danger')+'">' +message.text+ '</div>');
		     $('#pageErrors').show();
		    }
		   });
		   setTimeout(function() { $("#pageErrors").hide().empty(); }, 20000);
		},
		validate : function(selectors) {
			var sels = selectors;
			var valid = true;
			if (sels && sels.length>0) {
				var len = sels.length;
				$.each(sels, function(i, o) {
					var sel = $(o),
					parent = sel.parent();
					
					parent.removeClass('has-error');
					parent.find('span.help-block').remove();
					var connectionName = ""; 
					if(sel.attr("id") == "database_connectionName" || sel.attr("id") == "IL_database_connectionName"){
						connectionName = $(sels[0]).val();
					}
					
					if ($.trim(sel.val()) === "") {
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+globalMessage['anvizent.package.label.thisFieldRequired']+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					}else if ($.trim(sel.val()) === '0') {
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+globalMessage['anvizent.package.label.thisFieldRequired']+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					}else if(/^[a-zA-Z0-9/ /_/-/(/)]*$/.test(connectionName) == false && i==0){
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+globalMessage['anvizent.package.label.connectionNameContainsIllegalSpecialCharacters']+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					}else if ( sel.data("minlength") && sel.val().length < sel.data("minlength") ) {
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+"Minimum length of " + sel.data("minlength") + " is required"+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					} else if ( sel.data("maxlength") && sel.val().length > sel.data("maxlength") ) {
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+"Maximum length of " + sel.data("maxlength") + " is allowed"+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					}
				}); 
			}
			return valid;
		},
		validateMinMaxEmptySpecialCharacters : function(selectors) {
			var sels = selectors;
			var valid = true;
			if (sels && sels.length>0) {
				var len = sels.length;
				$.each(sels, function(i, o) {
					var sel = $(o),
					parent = sel.parent();
					
					parent.removeClass('has-error');
					parent.find('span.help-block').remove();
					var connectionName = ""; 
					var regex = /^[a-zA-Z0-9/ /_/-/(/)]*$/ ;
					
					if ($.trim(sel.val()) === "") {
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+globalMessage['anvizent.package.label.thisFieldRequired']+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					} else if (sel.data("minlength") && sel.val().length < sel.data("minlength") ) {
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+"Minimum length of " + sel.data("minlength") + " is required"+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					} else if ( sel.data("maxlength") && sel.val().length > sel.data("maxlength") ) {
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+"Maximum length of " + sel.data("maxlength") + " is allowed"+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					} else if(!regex.test(sel.val())){
			   	    	parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets']+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
			   	     }
				}); 
			}
			return valid;
		},
		showcustommsg : function(sel, msg, append,isBold) {
			if (sel) {
				var s = $(sel),
				parent = s.parent();
				
				parent.removeClass('has-error');
				parent.find('span.help-block').remove();
				
				parent.addClass('has-error');
				if ( !isBold ) {
					if (!append)
						$('<span class="help-block" style="font-size:12px;clear: both;">'+(msg? msg : globalMessage['anvizent.package.label.thisFieldRequired'])+'</span>').insertAfter(s);
					else
						$('<span class="help-block" style="font-size:12px;clear: both;">'+(msg? msg : globalMessage['anvizent.package.label.thisFieldRequired'])+'</span>').appendTo(parent);
				} else {
					$('<span class="help-block" style="font-size:20px;clear: both;font-weight:bold;">'+(msg? msg : globalMessage['anvizent.package.label.thisFieldRequired'])+'</span>').appendTo(parent);
				}
				
			}
		},
		clearcustomsg : function(sel) {
			if (sel) {
				var s = $(sel),
				parent = s.parent();
				parent.removeClass('has-error');
				parent.find('span.help-block').remove();
			}
		},
		clearValidations : function(selectors) {
			var sels = selectors;
			
			if (sels && sels.length>0) {
				$.each(sels, function(i, o) {
					var sel = $(o),
					parent = sel.parent();
					if (parent.hasClass('has-error')) {
						parent.removeClass('has-error');
						parent.find('span.help-block').remove();
					}
				});
			}
		},
		isInteger : function(val) {
			if (!val) {
			    return false;
			  }
			  
			 return /^-?[\d]+(?:e-?\d+)?$/i.test(val);
		},
		isNumeric : function(val) {
		  if (!val) {
		    return false;
		  }
		  return /^-?[\d.]+(?:e-?\d+)?$/i.test(val);
		},
		showErrorAlert: function(msg) {
			var message = '<div class="alert alert-danger">'+msg+'</div>';
			showAlert(message);
		},
		showSuccessAlert: function(msg) {
			var message = '<div class="alert alert-success">'+msg+'</div>';
			showAlert(message);
		},
		refreshSessionURL : function(){
			var userid = adt.session_userID;
			var url = adt.appContextPath+'/refresh';
            $.get( url );
		},
		refreshSessionURLDummy : function(){
			var sessionInterval = setInterval(function(){
				common.refreshSessionURL()
			},4*1000);
		},
		refreshSessionURLLoop : function(){
			var sessionInterval = setInterval(function(){
				common.refreshSessionURL()
			},15*60*1000);
		},
		validateWithCustomMessages : function(selectorsArray) {
			var valid = true;
			if (selectorsArray && selectorsArray.length > 0) {
				var len = selectorsArray.length;
				$.each(selectorsArray, function(i, obj) {
					var sel = $(obj.selector),
						msg = obj.message,
						regex = obj.regex,
						regexMsg = obj.regexMsg, 
						parent = sel.parent();
					
					parent.removeClass('has-error');
					parent.find('span.help-block').remove();
					
					if ($.trim(sel.val()) === "") {
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+msg+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					}
					else if ( sel.data("minlength") && sel.val().length < sel.data("minlength") ) {
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+"Minimum length of " + sel.data("minlength") + " is required"+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					}
					else if(regex != "" &&  !regex.test(sel.val())){
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+regexMsg+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					}
					else if ( sel.data("maxlength") && sel.val().length > sel.data("maxlength") ) {
						parent.addClass('has-error');
						$('<span class="help-block" style="font-size:12px;">'+"Maximum length of " + sel.data("maxlength") + " is allowed"+'</span>').insertAfter(sel);
						if (valid)
							valid = false;
					}
				}); 
			}
			return valid;
		},
	  validURL: function(url) { 
		  var r = new RegExp('^(?:[a-z]+:)?//', 'i');
		  return r.test(url);
	  },
	  addToSelectBox: function(value, insertAfterElement){
	    	$("<option>",{text:value,value:value}).insertBefore($(insertAfterElement));
	  },
	  getTimezoneName: function () {
	        timezone = jstz.determine()
	        return timezone.name();
	  },
	  transform: function(value, limit) {
		  console.log(value);
	        if ( value && value.length > limit ) {
	            return value.substr(0,limit) +'...';
	        } else {
	            return value;
	        }
	  },
	  getcsrfHeader: function() {
		  var token = $("meta[name='_csrf']").attr("content");
		  var headerToken = $("meta[name='_csrf_header']").attr("content");
		  var headers = {};
		  headers[headerToken] = token;
		  return headers;
	  },
	  populateComboBox : function(ele,value,text) { 
		  ele.append($("<option>", {
			  value : value,
			  text : text
			}));
	  }
	  
		  
};