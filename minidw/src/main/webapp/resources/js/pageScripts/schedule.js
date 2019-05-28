var headers = {};
var localTimeZone = common.getTimezoneName();
var schedulePage = {
	initialPage : function() {
		var packageScheduleTable = $("#packageScheduleTable").DataTable( {
				"order": [[ 0, "desc" ]],
		        "bDestroy": true //to allow multilple intializations
		        ,"language": {
	                "url": selectedLocalePath
	            }
		    } );		
		var allPages = packageScheduleTable.cells().nodes();	
		$(allPages).find(".tool").tooltip({
	          content: function () {
	              return $(allPages).find($(this)).prop('title');
	             
	          }
	    });
		var sessionInterval = setInterval(function(){
				common.refreshSessionURL()
		},15*60*1000);
		 $("#timeZone").select2({               
             allowClear: false,
             theme: "classic"
			});
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		
		
		$("#selectedhours").multipleSelect({
			filter : true,
			placeholder : 'Select Hours',
			enableCaseInsensitiveFiltering : true
		});

		var package_id = "";//$("#packageId").val();
		if (package_id != "") {
			var packageScheduleTbl = $("#packageScheduleTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
			var allPages = packageScheduleTbl.cells().nodes();
			//setTimeout(function(){$(allPages).find(".schedulePackageId"+packageId).click()},100);
			$.each(allPages, function(){
				if($(this).find(".schedulePackageId"+package_id).attr("data-packgeId") == package_id){
					schedulePage.schedulePackageOpenPopup($(this).find(".schedulePackageId"+package_id));
				}
			});

		}
		
		
		/*packageScheduleTable.rows().eq(0).each( function ( index ) {
		    var row = packageScheduleTable.row( index );
		 
		    var data = row.data();
		    if ($("#packageId").val() != "") {
				setTimeout(function(){$(".schedulePackageId"+$("#packageId").val(),data).click()},100);
			}
		});*/
		
	},
	
	
	updatePackageScheduleTable : function(result, filterBy) {
		$(".schedulePackages").hide();
		$("#packageScheduleTable input[name='selectAllPackages']").prop("checked",false);
		
		var packageScheduleTable = $("#packageScheduleTable").DataTable({"order": [[ 0, "desc" ]],"bDestroy": true,"language": {
            "url": selectedLocalePath
        }});
		packageScheduleTable.clear();
		var tableRow = '';
			if (result != null) {
				$.each(result, function(key, obj) {
					var isStandardPackage = obj["isStandard"] ? "standard" : "custom";
					if(isStandardPackage == filterBy && obj["isStandard"] || (filterBy == "all" && obj["isStandard"])){
						var packageId = obj["packageId"];
						var packageName = obj["packageName"].encodeHtml();
						var scheduleType = obj["scheduleType"]
						var isScheduled = obj["isScheduled"];
						var scheduleRecurrence = obj["scheduleRecurrence"];
						var scheduleStartTime = obj["scheduleStartTime"];
						var scheduleRange = obj["scheduleRange"];
						var industryId = obj["industry"]["id"];
						var scheduleId = obj["scheduleId"];
						var isStandard = obj["isStandard"];
						var isClientDbTables = obj["isClientDbTables"];
						
						var selectPackage = "";
						var schedule = '';
						var runNow = '';
						var runwithscheduler = '';
						if (isScheduled == false) {
							schedule = "<a href='#' class='schedulePackage schedulePackageId'"+packageId+"'' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-industryId='"+ industryId + "' data-packgeId='" + packageId + "'" + "data-packgeName='" + packageName + "' data-isClientDbTables='"+obj["isClientDbTables"]+"'>"+globalMessage['anvizent.package.label.schedule']+"</a>";
							selectPackage = "<input type='checkbox' name='selectPackage' data-packgeId='"+packageId+"' data-packgeName='"+packageName+"' data-isClientDbTables='"+isClientDbTables+"' data-industryId='"+industryId+"' data-reSchedule='firstRun'>";
							runNow = "<a href='#' class='runPackage' data-industryId='"
							+ industryId
							+ "' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-packgeId='"
							+ packageId + "'"
							+ "data-packgeName='"
							+ packageName + "'" 
							+ " data-schedulerType='runnow' "
							+ " data-isClientDbTables='"+obj["isClientDbTables"]+"' data-scheduleId ='"+scheduleId+"'  data-reSchedule='reRun' >"+globalMessage['anvizent.package.label.runNow']+"</a>";
						    
							runwithscheduler = "<a href='#' class='runwithSchedulerPackage' data-industryId='"
								+ industryId
								+ "' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-packgeId='"
								+ packageId + "'"
								+ "data-packgeName='"
								+ packageName + "'" 
								+ " data-schedulerType='runwithscheduler'"
								+ " data-isClientDbTables='"+obj["isClientDbTables"]+"' data-scheduleId ='"+scheduleId+"'  data-reSchedule='reRun' >"+globalMessage['anvizent.package.label.runNow']+"</a>";
						
						}else {
							var tooltip="";
							if (scheduleRecurrence == 'Run Now') {
								tooltip = "<b>"+globalMessage['anvizent.package.label.recurrence']+"</b> "
										+ scheduleRecurrence + " <br>"
										+"<b>"+globalMessage['anvizent.package.label.startTime']+"</b> "
										+ scheduleStartTime
										+ " <br>"
										+ "<b>"+globalMessage['anvizent.package.label.endTime']+"</b> "
										+ scheduleRange
										+ " <br>";
										
							}else{
								tooltip =  "<b>"+globalMessage['anvizent.package.label.recurrence']+"</b> "
									+ scheduleRecurrence + " <br>"
									+"<b>"+globalMessage['anvizent.package.label.startTime']+"</b> "
									+ scheduleStartTime + " <br>"
									+ "<b>"+globalMessage['anvizent.package.label.range']+"</b> "
									+ (scheduleRange || '-') + "<br>"
									+ "<b>"+globalMessage['anvizent.package.label.timeZone']+"</b> "
									+ (obj["timeZone"] || '-') + "<br>";
							}
							schedule += "<a href='#' class='schedulePackage tool schedulePackageId'"+packageId+"'' data-industryId='"
								+ industryId
								+ "' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-packgeId='"
								+ packageId + "'"
								+ "data-packgeName='"
								+ packageName
								+ "'  data-isClientDbTables='"+obj["isClientDbTables"]+"' data-scheduleId ='"+scheduleId+"'  data-reSchedule='reRun' title='"+tooltip+"'>"+globalMessage['anvizent.package.link.reSchedule']+"</a>";
							selectPackage = "<input type='checkbox' name='selectPackage' data-packgeId='"+packageId+"' data-packgeName='"+packageName+"' data-scheduleId='"+scheduleId+"' data-reSchedule='reRun' data-isClientDbTables='"+isClientDbTables+"' data-industryId='"+industryId+"'>";
							runNow = "<a href='#' class='runPackage' data-industryId='"
							+ industryId
							+ "' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-packgeId='"
							+ packageId + "'"
							+ "data-packgeName='"
							+ packageName +"'" 
							+ " data-schedulerType = 'runnow' "
							+ " data-isClientDbTables='"+obj["isClientDbTables"]+"' data-scheduleId ='"+scheduleId+"'  data-reSchedule='reRun' >"+globalMessage['anvizent.package.label.runNow']+"</a>";
							
							runwithscheduler = "<a href='#' class='runwithSchedulerPackage' data-industryId='"
								+ industryId
								+ "' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-packgeId='"
								+ packageId + "'"
								+ "data-packgeName='"
								+ packageName + "'" 
								+ " data-schedulerType='runwithscheduler'"
								+ " data-isClientDbTables='"+obj["isClientDbTables"]+"' data-scheduleId ='"+scheduleId+"'  data-reSchedule='reRun' >"+globalMessage['anvizent.package.label.runNow']+"</a>";
						}
						
						var viewResultsUrl = "";
						var packageType = "";
						var viewSourceDetails = ""; 
						if(isStandard){
							packageType = globalMessage['anvizent.package.label.standard'];
							viewResultsUrl = adt.appContextPath+"/adt/package/jobResults/"+packageId+"?source=schedule";
							viewSourceDetails = "<a class='btn btn-primary btn-sm tablebuttons startLoader' href='"+adt.appContextPath+"/adt/package/standardPackage/edit/"+packageId+"?source=schedule'>" +
									"<span class='glyphicon glyphicon-eye-open' title="+globalMessage['anvizent.package.label.view']+" aria-hidden='true'></span></a>";
						}
						else{
							packageType = globalMessage['anvizent.package.label.custom'];
							viewResultsUrl = adt.appContextPath+"/adt/package/viewResultsForCustomPackage/"+packageId+"?source=schedule";
							if(isScheduled){
								viewSourceDetails = "<a class='btn btn-primary btn-sm tablebuttons startLoader' href='"+adt.appContextPath+"/adt/package/customPackage/edit/"+packageId+"?source=schedule'>" +
								"<span class='glyphicon glyphicon-eye-open' title="+globalMessage['anvizent.package.label.view']+" aria-hidden='true'></span></a>";
							}
							else{
								viewSourceDetails = "<a class='btn btn-primary btn-sm tablebuttons startLoader' href='"+adt.appContextPath+"/adt/package/customPackage/edit/"+packageId+"?source=schedule'>" +
								"<span class='glyphicon glyphicon-eye-open' title="+globalMessage['anvizent.package.label.view']+" aria-hidden='true'></span></a>";
							}
						}
						var viewResults = "";
						var viewResultsUrl = adt.appContextPath+"/adt/package/packageExecute/"+packageId+"?packageName="+packageName;
						viewResults = "<a href ='"+viewResultsUrl+"'>"+globalMessage['anvizent.package.label.viewExecutionResults']+"</a>"
						
						var scheduleResultsUrl = adt.appContextPath+"/adt/package"+( isStandard ?'/jobResults/':'/viewResultsForCustomPackage/')+packageId+"?source=schedule";
						var scheduleResults = "<a  href='"+scheduleResultsUrl+"'>"+globalMessage['anvizent.package.label.viewResults']+"</a>"
						
						
						var row = [];
						//row.push(selectPackage);
						row.push(packageId);
						row.push(packageType);
						row.push(packageName);
						row.push(viewSourceDetails);
						//row.push(runNow);
						row.push(runwithscheduler);
						row.push(schedule);
						row.push(scheduleResults);
						row.push(viewResults);
						packageScheduleTable.row.add(row);	
					}
					else if((isStandardPackage == filterBy && !obj["isStandard"]) || (filterBy == "all" && !obj["isStandard"])){
						var packageId = obj["packageId"];
						var packageName = obj["packageName"].encodeHtml();
						var isScheduled = obj["isScheduled"];
						var scheduleStatus = obj["scheduleStatus"].encodeHtml();
						var scheduleRecurrence = obj["scheduleRecurrence"];
						var scheduleStartTime = obj["scheduleStartTime"];
						var scheduleRange = obj["scheduleRange"];
						var industryId = obj["industry"]["id"];
						var scheduleId = obj["scheduleId"];
						var isStandard = obj["isStandard"];
						var isClientDbTables = obj["isClientDbTables"];
						var selectPackage = "";
						var schedule = '';
						var runwithscheduler ='';
						if (isScheduled == false) {
							schedule = "<a href='#' class='schedulePackage schedulePackageId'"+packageId+"'' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-industryId='"+ industryId + "' data-packgeId='" + packageId + "'" + "data-packgeName='" + packageName + "' data-isClientDbTables='"+obj["isClientDbTables"]+"'>"+globalMessage['anvizent.package.label.schedule']+"</a>";
							selectPackage = "<input type='checkbox' name='selectPackage' data-packgeId='"+packageId+"' data-packgeName='"+packageName+"' data-isClientDbTables='"+isClientDbTables+"' data-industryId='"+industryId+"' data-reSchedule='firstRun'>";
							runNow = "<a href='#' class='runPackage' data-industryId='"
							+ industryId
							+ "' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-packgeId='"
							+ packageId + "'"
							+ "data-packgeName='"
							+ packageName + "'" 
							+ " data-schedulerType='runnow'"
							+ "  data-isClientDbTables='"+obj["isClientDbTables"]+"' data-scheduleId ='"+scheduleId+"'  data-reSchedule='reRun' >"+globalMessage['anvizent.package.label.runNow']+"</a>";
							
							runwithscheduler = "<a href='#' class='runwithSchedulerPackage' data-industryId='"
								+ industryId
								+ "' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-packgeId='"
								+ packageId + "'"
								+ "data-packgeName='"
								+ packageName + "'" 
								+ " data-schedulerType='runwithscheduler'"
								+ "  data-isClientDbTables='"+obj["isClientDbTables"]+"' data-scheduleId ='"+scheduleId+"'  data-reSchedule='reRun' >"+globalMessage['anvizent.package.label.runNow']+"</a>";
						}else {
							var tooltip="";
							if (scheduleRecurrence == 'Run Now') {
								tooltip = "<b>"+globalMessage['anvizent.package.label.recurrence']+"</b> "
										+ scheduleRecurrence + " <br>"
										+"<b>"+globalMessage['anvizent.package.label.startTime']+"</b> "
										+ scheduleStartTime
										+ " <br>"
										+ "<b>"+globalMessage['anvizent.package.label.endTime']+"</b> "
										+ scheduleRange
										+ " <br>";
										
							}else{
								tooltip =  "<b>"+globalMessage['anvizent.package.label.recurrence']+"</b> "
									+ scheduleRecurrence + " <br>"
									+"<b>"+globalMessage['anvizent.package.label.startTime']+"</b> "
									+ scheduleStartTime + " <br>"
									+ "<b>"+globalMessage['anvizent.package.label.range']+"</b> "
									+ scheduleRange + "<br>";
							}
							schedule += "<a href='#' class='schedulePackage tool schedulePackageId'"+packageId+"'' data-industryId='"
								+ industryId
								+ "' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-packgeId='"
								+ packageId + "'"
								+ "data-packgeName='"
								+ packageName
								+ "'  data-isClientDbTables='"+obj["isClientDbTables"]+"' data-scheduleId ='"+scheduleId+"'  data-reSchedule='reRun' title='"+tooltip+"'>"+globalMessage['anvizent.package.link.reSchedule']+"</a>";
							
							selectPackage = "<input type='checkbox' name='selectPackage' data-packgeId='"+packageId+"' data-packgeName='"+packageName+"' data-scheduleId='"+scheduleId+"' data-reSchedule='reRun' data-isClientDbTables='"+isClientDbTables+"' data-industryId='"+industryId+"'>";
							runNow = "<a href='#' class='runPackage' data-industryId='"
							+ industryId
							+ "' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-packgeId='"
							+ packageId + "'"
							+ "data-packgeName='"
							+ packageName + "'" 
							+ " data-schedulerType='runnow'"
							+ "  data-isClientDbTables='"+obj["isClientDbTables"]+"' data-scheduleId ='"+scheduleId+"'  data-reSchedule='reRun' >"+globalMessage['anvizent.package.label.runNow']+"</a>";
							
							runwithscheduler = "<a href='#' class='runwithSchedulerPackage' data-industryId='"
								+ industryId
								+ "' data-packagetype=\""+(isStandard?"standard":"custom")+"\" data-packgeId='"
								+ packageId + "'"
								+ "data-packgeName='"
								+ packageName + "'" 
								+ " data-schedulerType='runwithscheduler'"
								+ "  data-isClientDbTables='"+obj["isClientDbTables"]+"' data-scheduleId ='"+scheduleId+"'  data-reSchedule='reRun' >"+globalMessage['anvizent.package.label.runNow']+"</a>";
						
						}
						
						var viewResultsUrl = "";
						var packageType = "";
						var viewSourceDetails = ""; 
						if(isStandard){
							packageType = globalMessage['anvizent.package.label.standard'];
							viewResultsUrl = adt.appContextPath+"/adt/package/jobResults/"+packageId+"?source=schedule";
							viewSourceDetails = "<a class='btn btn-primary btn-sm tablebuttons startLoader' href='"+adt.appContextPath+"/adt/package/standardPackage/edit/"+packageId+"?source=schedule'>" +
								"<span class='glyphicon glyphicon-eye-open' title="+globalMessage['anvizent.package.label.view']+" aria-hidden='true'></span></a>";
						}
						else{
							packageType = globalMessage['anvizent.package.label.custom'];
							viewResultsUrl = adt.appContextPath+"/adt/package/viewResultsForCustomPackage/"+packageId+"?source=schedule";
							if(isScheduled){
								viewSourceDetails = "<a class='btn btn-primary btn-sm tablebuttons startLoader' href='"+adt.appContextPath+"/adt/package/customPackage/edit/"+packageId+"?source=schedule'>" +
								"<span class='glyphicon glyphicon-eye-open' title="+globalMessage['anvizent.package.label.view']+" aria-hidden='true'></span></a>";
							}
							else{
								viewSourceDetails = "<a class='btn btn-primary btn-sm tablebuttons startLoader' href='"+adt.appContextPath+"/adt/package/customPackage/edit/"+packageId+"?source=schedule'>" +
								"<span class='glyphicon glyphicon-eye-open' title="+globalMessage['anvizent.package.label.view']+" aria-hidden='true'></span></a>";
							}
						}
						var viewResults = "";
						var viewResultsUrl = adt.appContextPath+"/adt/package/packageExecute/"+packageId+"?packageName="+packageName;
						viewResults = "<a href ='"+viewResultsUrl+"'>"+globalMessage['anvizent.package.label.viewExecutionResults']+"</a>"
						var scheduleResultsUrl = adt.appContextPath+"/adt/package"+( isStandard ?'/jobResults/':'/viewResultsForCustomPackage/')+packageId+"?source=schedule";
						var scheduleResults = "<a href='"+scheduleResultsUrl+"'>"+globalMessage['anvizent.package.label.viewResults']+"</a>"
						
						
						
						var row = [];
						//row.push(selectPackage);
						row.push(packageId);
						row.push(packageType);
						row.push(packageName);
						row.push(viewSourceDetails);
						//row.push(runNow);
						row.push(runwithscheduler);
						row.push(schedule);
						row.push(scheduleResults);
						row.push(viewResults);
						packageScheduleTable.row.add(row);
					}
					});
			}
		packageScheduleTable.draw(true);
		var allPages = packageScheduleTable.cells().nodes();	
		$(allPages).find(".tool").tooltip({
	          content: function () {
	              return $(allPages).find($(this)).prop('title');
	             
	          }
	      });
	},
	getPackageListForSchedule : function() {
	    var userID = $("#userID").val();
		var url_getPackagesForScheduling = "/app/user/" + userID + "/package/schedule/getPackagesForScheduling";
		var myAjax = common.loadAjaxCall(url_getPackagesForScheduling, 'GET','',headers);
		myAjax.done(function(result) {
			if (result != null) {
				var filterBy = $("#filterSchedulePackages").val();
				schedulePage.updatePackageScheduleTable(result, filterBy);
				schedulePage.updateScheduelePopUp(result);
			}
		});
	},
	updateScheduelePopUp : function(obj){
		var scheduleRecurrence = obj["scheduleRecurrence"];
		var scheduleStartTime = obj["scheduleStartTime"];
		var scheduleRange = obj["scheduleRange"];
	},
	showMessage:function(text){
		$(".messageText").empty();
		$(".successMessageText").empty();
		$(".messageText").html(text);
	    $(".message").show();
	    setTimeout(function() { $(".message").hide(); }, 10000);
	},
	schedulePackage : function(uploadStatus){
		common.clearValidations(["#scheduleStartDateValidation, #recurrencePatternValidation, #scheduleEndDateVaLidation, #rangeofRecurrenceValidation","#timeZone"]);
		if(packageId == null){
			packageId = $("#packageId").val();
		}
		var scheduleStartDateVal = $("#scheduleStartDate").val();
		var reloadUrl = $("#reloadUrl").val();
		var rangeOfRecurrenceValidation = $('input:radio[name=rangeOfRecurrence]:checked').val();
		var scheduleOnce = $('input:radio[name=recurrencePattern]:checked').val();
		
		if(scheduleOnce != 'once'){
			
		if (scheduleStartDateVal.length == 0) {
			common.showcustommsg("#scheduleStartDateValidation", globalMessage['anvizent.package.label.pleaseFillScheduleStartDate']);
		}
		
		if ($('input:radio[name=rangeOfRecurrence]').is(":checked")) {
			if (rangeOfRecurrenceValidation == 'ScheduleEndDate') {
				var scheduleEndDateVal = $("#scheduleEndDate").val();
				if (scheduleEndDateVal.length == 0) {
					common.showcustommsg("#scheduleEndDateVaLidation", globalMessage['anvizent.package.label.pleaseFillScheduleEndDate']);
				}
			}
		} else {
			common.showcustommsg("#rangeofRecurrenceValidation", globalMessage['anvizent.package.label.pleaseChooseAnyOneOfRangeOfRecurrence']);
		}
		}
		var scheduleStartDate = null;
		var scheduleStartTime = null;
		var recurrencePattern = null;
		var daysToRun = null;
		var weeksToRun = null;
		var dayOfMonth = null;
		var monthsToRun = null;
		var dayOfYear = null;
		var monthOfYear = null;
		var yearsToRun = null;
		var isNoEndDate = false;
		var scheduleEndDate = null;
		var noOfMaxOccurences = null;
		var timeZone=null;
		var typeOfHours=null;
		var hoursToRun=null;
		var minutesToRun = null;
		var runNowOrSchedule = $("input[type=radio][name='runNowOrSchedule']:checked").val();
		if (runNowOrSchedule == 'runnow') {
			recurrencePattern = runNowOrSchedule;
		}
		// TODO validation for all fields
		if (runNowOrSchedule == 'schedule') {
			if(scheduleOnce != 'once'){
				var validation = checkValidation();
				if (!validation)
					return false;
			}
			scheduleStartDate = $("#scheduleStartDate").val();
			var scheduleStartHours = $("#scheduleStartHours").val();
			var scheduleStartMinutes = $("#scheduleStartMinutes").val();
			scheduleStartTime = scheduleStartHours + ":"+ scheduleStartMinutes;
			
			var recurrencePatternSelector = 'input[name="recurrencePattern"]';
			$(recurrencePatternSelector).each(function() {
				if ($(this).is(":checked")) {
					
					if ($(this).val() == "once") {
						recurrencePattern = $(this).val();
					}
					
					if ($(this).val() == "runnow") {
						// no options for run
						// now
						recurrencePattern = $(this).val();
					}
					if ($(this).val() == "minutes") {
						// no options for minutes
						// recurrence
						recurrencePattern = $(this).val();
						minutesToRun = $("#minutesToRun").val();
					}
					if ($(this).val() == "hourly") {
						// no options for hourly
						// recurrence
						recurrencePattern = $(this).val();
						if ($("#everyhourlyRadios").is(":checked")) { 
							typeOfHours=$("#everyhourlyRadios").val();
							hoursToRun = $("#everyhour").val();	
						}
					
						if ($("#selectedhourlyRadios").is(":checked")) {
							typeOfHours= $("#selectedhourlyRadios").val();
							hoursToRun = $("#selectedhours").val().join(",");
						}
						
					}
					if ($(this).val() == "daily") {
						// no options for daily
						// recurrence
						recurrencePattern = $(this).val();
					}
					if ($(this).val() == "weekly") {
						recurrencePattern = $(this).val();
						daysToRun = $('input[name=weekDayCheckBox]:checked') .map(function() {
											return $(this).val();
										}).get().join(",");
						weeksToRun = $("#weeksToRun").val();
						console.log("daysToRun>>>" + daysToRun)
					}
					if ($(this).val() == "monthly") {
						recurrencePattern = $(this).val();
						$('input[name=monthlyRadios]').each(function() {
											if ($(this).is(":checked")) { 
												if ($(this).val() == 'monthlyOption_first') {
													dayOfMonth = $("#dayOfMonth").val();
													monthsToRun = $("#monthsToRun").val();
												}
											}
										});
					}
					if ($(this).val() == "yearly") {
						recurrencePattern = $(this).val();
						$('input[name=yearlyRadios]').each(function() {
											if ($(this).is(":checked")) {
												if ($(this).val() == 'yearlyOptions_first') {
													dayOfYear = $("#dayOfYear").val();
													monthOfYear = $("#monthOfYear").val();
													yearsToRun = $("#yearsToRun").val();
												}
											}
										});
					}
					
				}
			});

			var rangeOfRecurrence = 'input[name="rangeOfRecurrence"]';
			$(rangeOfRecurrence).each(function() {
				if ($(this).is(":checked")) {
					if ($(this).val() == 'NoEndDate') {
						isNoEndDate = true;
					}
					if ($(this).val() == 'MaxOccurences') {
						noOfMaxOccurences = $( "#noOfMaxOccurences").val();
					}
					if ($(this).val() == 'ScheduleEndDate') {
						scheduleEndDate = $("#scheduleEndDate").val();

					}
				}
			});
			timeZone = $("#timeZone").val();
			if(timeZone == "select"){
				common.showcustommsg("#timeZone", globalMessage['anvizent.package.msg.pleaseChoosetimezone'],"#timeZone");
				return false;
			}
		}
		showAjaxLoader(true);
		if (runNowOrSchedule == 'runnow') {
			var runJobType = $("[name='runJobType']:checked").val();
			var runOnlyDL = false ;
			if ( packageType == 'standard' ) {
				if (!runJobType) {
					runJobType = "all";
				}
				runOnlyDL =  runJobType == "dl";
			} else {
				packageType == 'all'
			}
			
			if ( runOnlyDL || (uploadStatus && uploadStatus == "completed") ) {
				var selectData = {
						userPackage : {
							packageId : packageId,
							isClientDbTables:isClientDbTables,
							industry : {
								id : industryId
							}
						},
						schedule : {
							scheduleId : scheduleId,
							scheduleType : runNowOrSchedule,
							scheduleStartDate : scheduleStartDate,
							scheduleStartTime : scheduleStartTime,
							recurrencePattern : recurrencePattern,
							daysToRun : daysToRun,
							weeksToRun : weeksToRun,
							dayOfMonth : dayOfMonth,
							monthsToRun : monthsToRun,
							dayOfYear : dayOfYear,
							monthOfYear : monthOfYear,
							yearsToRun : yearsToRun,
							isNoEndDate : isNoEndDate,
							scheduleEndDate : scheduleEndDate,
							noOfMaxOccurences : noOfMaxOccurences,
							minutesToRun : minutesToRun,
    						typeOfHours:typeOfHours,
    						hoursToRun:hoursToRun,
						}

					};
					$('.loader').fadeIn('fast');
					$('body').addClass('cursor-wait');
					var token = $("meta[name='_csrf']").attr("content");
					var header = $("meta[name='_csrf_header']").attr("content");
					headers[header] = token;
					var url_saveSchedule = "/app/user/" + userID + "/package/schedule/savePackageSchedule?reloadUrl="+reloadUrl + "&jobType="+runJobType ;
					var myAjax = common.postAjaxCall(url_saveSchedule, 'POST', selectData,headers);
					myAjax.done(function(result1) {
						$('.loader').fadeOut('fast');
						$('body').removeClass('cursor-wait');

						if (result1.hasMessages) {
							var messages = "";
							$.each(result1, function(key, value) {
								if (key == 'messages') {
									messages = [ {
										code : value[0].code,
										text : value[0].text
									} ];

								}
							});
							$("#schedulePackagePopUp, #showAddedSourcesPopUp").modal('hide');
							common.displayMessages(messages);
							schedulePage.getPackageListForSchedule();
							setTimeout(function(){ window.location.reload(); }, 1000);
						} else {
							standardPackage.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
							setTimeout(function(){ window.location.reload(); }, 1000);
						}
					});
					
			
			} else {

				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				var url_uploadingFilesFromClientDatabase = "/app/user/"+userID+"/package/uploadingFilesFromClientDatabase/"+packageId;
				var myAjax1 = common.loadAjaxCall(url_uploadingFilesFromClientDatabase,'GET','',headers);
				    myAjax1.done(function(result) {
				    	if(result.hasMessages){
							for(var i=0; i < result.messages.length; i++){
								if (result.messages[i].code === "ERROR") {
									common.showErrorAlert(result.messages[i].text);
									showAjaxLoader(false);
									return false;
								}
							}
							 					
						}else{
							var selectData = {
									userPackage : {
										packageId : packageId,
										isClientDbTables:isClientDbTables,
										industry : {
											id : industryId
										}
									},
									schedule : {
										scheduleId : scheduleId,
										scheduleStartDate : scheduleStartDate,
										scheduleStartTime : scheduleStartTime,
										recurrencePattern : recurrencePattern,
										daysToRun : daysToRun,
										weeksToRun : weeksToRun,
										dayOfMonth : dayOfMonth,
										monthsToRun : monthsToRun,
										dayOfYear : dayOfYear,
										monthOfYear : monthOfYear,
										yearsToRun : yearsToRun,
										isNoEndDate : isNoEndDate,
										scheduleEndDate : scheduleEndDate,
										noOfMaxOccurences : noOfMaxOccurences,
										minutesToRun : minutesToRun,
			    						typeOfHours:typeOfHours,
			    						hoursToRun:hoursToRun,
									}

								};
								$('.loader').fadeIn('fast');
								$('body').addClass('cursor-wait');

								var token = $("meta[name='_csrf']").attr("content");
								var header = $("meta[name='_csrf_header']").attr("content");
								headers[header] = token;
								var url_saveSchedule = "/app/user/" + userID + "/package/schedule/savePackageSchedule?reloadUrl="+reloadUrl+"&jobType="+runJobType;
								var myAjax = common.postAjaxCall(url_saveSchedule, 'POST', selectData,headers);
								myAjax.done(function(result1) {
									$('.loader').fadeOut('fast');
									$('body').removeClass('cursor-wait');

									if (result1.hasMessages) {
										var messages = "";
										$.each(result1, function(key, value) {
											if (key == 'messages') {
												messages = [ {
													code : value[0].code,
													text : value[0].text
												} ];

											}
										});
										$("#schedulePackagePopUp").modal('hide');
										common.displayMessages(messages);

										schedulePage.getPackageListForSchedule();
									} else {
										standardPackage.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
									}
								});
						}
				    	
					});
				
			}
			
		}else{
			//schedule
			var selectData = {
					userPackage : {
						packageId : packageId,
						isClientDbTables:isClientDbTables,
						industry : {
							id : industryId
						}
					},
					schedule : {
						scheduleId : scheduleId,
						scheduleType : runNowOrSchedule,
						scheduleStartDate : scheduleStartDate,
						scheduleStartTime : scheduleStartTime,
						recurrencePattern : recurrencePattern,
						daysToRun : daysToRun,
						weeksToRun : weeksToRun,
						dayOfMonth : dayOfMonth,
						monthsToRun : monthsToRun,
						dayOfYear : dayOfYear,
						monthOfYear : monthOfYear,
						yearsToRun : yearsToRun,
						isNoEndDate : isNoEndDate,
						scheduleEndDate : scheduleEndDate,
						noOfMaxOccurences : noOfMaxOccurences,
						timeZone: timeZone,
						minutesToRun : minutesToRun,
						typeOfHours:typeOfHours,
						hoursToRun:hoursToRun,
					}

				};
     			$('.loader').fadeIn('fast');
				$('body').addClass('cursor-wait');

				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				var url_saveSchedule = "/app/user/" + userID + "/package/schedule/savePackageSchedule?reloadUrl="+reloadUrl;
				var myAjax = common.postAjaxCall(url_saveSchedule, 'POST', selectData,headers);
				myAjax.done(function(result1) {
					$('.loader').fadeOut('fast');
					$('body').removeClass('cursor-wait');

					if (result1.hasMessages) {
						var messages = "";
						$.each(result1, function(key, value) {
							if (key == 'messages') {
								messages = [ {
									code : value[0].code,
									text : value[0].text
								} ];

							}
						});
						$("#schedulePackagePopUp").modal('hide');
						common.displayMessages(messages);

						//schedulePage.getPackageListForSchedule();
						setTimeout(function(){ window.location.reload(); }, 1500);
					} else {
						standardPackage.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
						setTimeout(function(){ window.location.reload(); }, 1000);
					}
				});
		}
	},
	buildAddedSourcesTable : function(resultObj){
		var table = $("#addedSourceDetails");
		table.find("tbody").empty();
		
		if(resultObj != null){
			table.find(".ilName").show();
			if(packageType == "custom")
				table.find(".ilName").hide();
			
			$.each(resultObj, function(i, obj){
				var isFilePathNull = obj.ilConnectionMapping.originalFileName == null ? true : false;
				var isFlatFile = obj.ilConnectionMapping.isFlatFile;
				var isWebservice = obj.ilConnectionMapping.isWebservice;
				var isHavingParentTable = obj.ilConnectionMapping.isHavingParentTable;
				var uploadStatus = "-";
				
				/*
				 isFlatFile 1  flat file
				 isFlatFile 0 and isWebservice 1 mean webservice
				 isFlatFile 0 and isWebservice 0 means database 
				 */
				
				var sourceFileType = isFlatFile ? "Flat File" : ( !isFlatFile && isWebservice && !isHavingParentTable ? "Webservice" : ( !isFlatFile && !isWebservice && !isHavingParentTable ? "Database" : " Derived Table " ));
				var sourceType = $(".sourceOrArchiveDiv").clone();
				$(sourceType).find("input[name='sourceType']").prop("name","sourceType"+(i+1));
				if(isFilePathNull){
					$(sourceType).find("input.archived").prop("disabled", true);
					$(sourceType).find("input.archived").removeAttr("checked");
					$(sourceType).find("input.source").attr("checked", "checked");
					uploadStatus = "Pending";
				}
				var mappingId = "<input type='hidden' value='"+obj.ilConnectionMapping.connectionMappingId+"' name='mappingId'>"
				
				var row = "<tr>";
					row+="<td>"+(i+1)+mappingId+"</td>";
					if(packageType == "standard")
						row+="<td>"+obj.ilInfo.iL_name+"</td>";
					
					row+="<td>"+sourceFileType+"</td>";
					
					row+="<td>"+(isFilePathNull ? '-' : obj.ilConnectionMapping.originalFileName)+"</td>";
					row+="<td>"+(isFilePathNull ? '-' : obj.ilConnectionMapping.fileSize)+"</td>";
					row+="<td>"+(isFilePathNull ? '-' : obj.ilConnectionMapping.rowCount)+"</td>";
					row+="<td>"+(isFilePathNull ? '-' : obj.ilConnectionMapping.modification.createdTime)+"</td>";
					if ( sourceFileType == "Database" ) {
						row+="<td>"+$(sourceType).html()+"</td>";
						row+="<td class='status status"+obj.ilConnectionMapping.connectionMappingId+"'>"+uploadStatus+"</td>";
					} else {
						row+="<td>N/A</td>";
						row+="<td class='status'>N/A</td>";
					}
					
					row+="</tr>"
					table.find("tbody").append(row);
			});
		}
	},
	showOrHideAdvanceOptions : function(){
		$("#runALL, #source").prop("checked", true);
		$(".fetchTypeDiv").hide();
		if(packageType == "standard"){
			$(".jobRunTypeDiv").show();
		}
		else if(packageType == "custom"){
			$(".jobRunTypeDiv").hide();
		}
		if(packageType == "standard" || (packageType == "custom" || reRunVal == "reRun")){
			$(".fetchTypeDiv").show();
		}
	},
	
	schedulePackageOpenPopup : function(_this){
		common.clearValidations(["#weeklyRecurrencePatternValidation","#recurrencePatternValidation","#scheduleStartDateValidation","#rangeofRecurrenceValidation","#scheduleEndDateVaLidation","#scheduleStartDateValidation,.minutesValidation"]);
		packageId = $(_this).attr("data-packgeId");
		$("#packageId").val(packageId);
		packageName = $(_this).attr("data-packgeName");
		industryId = $(_this).attr("data-industryId");
		reRunVal = $(_this).attr("data-reSchedule");
		scheduleId = $(_this).attr("data-scheduleId");
		isClientDbTables = $(_this).attr("data-isClientDbTables");
		packageType = $(_this).attr("data-packagetype");
		schedulerType = $(_this).attr("data-schedulerType");
		//schedulerId = $(_this).attr("data-scheduleId");
		$("#runNowOptionsDiv").hide();
		$("#schedulePackagePopUpHeader").text(packageName);
		var runOrSchedule = $('input[name=runNowOrSchedule]:checked').val();
		if (runOrSchedule == 'schedule') {
			$('.checkedFlaseTrue').attr('checked', false);
			$('#weeklyRecurrencePattern').hide();
			$('#yearlyRecurrencePattern').hide();
			$('#monthlyRecurrencePattern').hide();
			$("#minutesPatternValidation").hide();
			$("#hourlyRecurrencePattern").hide();
			$('input[name=recurrencePattern]').attr("checked", false);
			$('#weeksToRun').val('');
			$('input[name=weekDayCheckBox]').attr("checked", false);
			$('.datepicker').datepicker('setDate', null);
			$("#scheduleStartHours").prop('selectedIndex', 0);
			$("#scheduleStartMinutes").prop('selectedIndex', 0);
			$('#scheduleEndDate').datepicker('setDate', null);
			$("#dayOfMonth").prop('selectedIndex', 30);
			$("#monthsToRun").prop('selectedIndex', 5);
			$('#yearsToRun').val('');
			
			$('#everyhour').val('');
			$("#selectedhours").val([]);
			$("#selectedhours").multipleSelect('refresh');
			$('input[name=hourlyRadios]').attr("checked", false);
			$("#hourlyRecurrencePattern").attr("checked", false);
			
			$("#monthOfYear option:first").attr('selected', 'selected');
			$('#dayOfYear').val('1');
			$('input[name=rangeOfRecurrence]').attr("checked", false);
			$("#RecurrencePattern").hide();
			$("#scheduleTime").hide();
			$("#RangeofRecurrence,#timeZoneDivPanel").hide();
			$("#yearsToRun").val("1");
			$("#weeksToRun").val("1");

		}
		//$('input[name="runNowOrSchedule"]').prop("checked", false);
		common.clearcustomsg("#runNowOrSchedulevalidation");
		$("#schedulePackagePopUp").modal('show');
		if (runOrSchedule == 'schedule') {
			$("input[name='runNowOrSchedule'][value='schedule']").click()
		}else if(runOrSchedule == 'runwithscheduler'){
			$("input[name='runNowOrSchedule'][value='runwithscheduler']").click()
		}else {
			$("input[name='runNowOrSchedule'][value='runnow']").get(0).click()
		}
		$("#confirmSchedule").prop("disabled", false);

	},
	
 initiatePackageRunning : function(runJobType,packageId,localTimeZone,packageType,executionKey,runNowOrSchedule,scheduleId){ 
	 console.log("executionKey -->",executionKey);
    var userID = $("#userID").val();
    var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	headers[header] = token;
	showAjaxLoader(true);
	var url_saveSchedule = "/app/user/" + userID + "/package/packageUploadExecutor/initiatePackageRunning?jobType="+runJobType +"&packageId="+packageId+"&timeZone="+localTimeZone+"&packageType="+packageType+"&executionKey="+executionKey+"&schedulerType="+runNowOrSchedule+"&schedulerId="+scheduleId;
	var myAjax = common.postAjaxCall(url_saveSchedule, 'POST', null,headers);
	myAjax.done(function(result1) {
		showAjaxLoader(false);
		if (result1.hasMessages) {
			var messages = "";
			$.each(result1, function(key, value) {
				if (key == 'messages') {
					messages = [ {
						code : value[0].code,
						text : value[0].text
					} ];

				}
			});
			$("#schedulePackagePopUp, #showAddedSourcesPopUp").modal('hide');
			common.displayMessages(messages);
			if (scheduleId == 0) {
				setTimeout(function(){location.reload()}, 3000)
			}
			//schedulePage.getPackageListForSchedule();
			//setTimeout(function(){location.reload()}, 3000);
		} else {
			standardPackage.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		}
	});
  },
  viewExecutionStatusComments :  function(result){
	  if(result.messages[0].code == "SUCCESS") {
			 var  messages=[{
				  code : result.messages[0].code,
				  text : result.messages[0].text
			  }];
			 var executionComments = result.object;
				
			 
			 if(executionComments === "" || executionComments === null){
				 executionComments = "Execution comments not found.";
			} 
				
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
		          popup.moveTo(0,0);
		          popup.document.title = "Upload Or Execution Status Comments";
		          popup.document.body.innerHTML = "<pre>"+executionComments+"</pre>";
		          if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
		        	  popup.addEventListener (
			        	        "load",
			        	        function () {
			        	            var destDoc = popup.document;
			        	            destDoc.open ();
			        	            destDoc.title = "DD Layout";
			        	            destDoc.write ('<html><head></head><body><pre>'+executionComments+'</pre></body></html>');
			        	            destDoc.close ();
			        	        },
			        	        false
			        	    );
		          }

		          
		          
		  }else {
			  console.log("result.messages-->",result.messages);
			   $("#packageExecutionAlreadyStartedPopUp").modal('hide');
	    		common.displayMessages(result.messages);
	    	}
  
}
};

if ($('.schedule-page').length) {
	schedulePage.initialPage();
	var userID = $("#userID").val();
	var packageId = null;
	var packageName = null;
	var industryId = null;
    var reRunVal=null;
    var scheduleId= null;
    var isClientDbTables= null;
    var packageType = null;
    var isContinueProcess = true;
    var successCount = [];
    var runNowOrSchedule = null;
	var runJobType =null;
	var executionKey = '';
	var schedulerType ='';
	var schedulerId ='';
     
	$(document).on('click', '.schedulePackage', function() {
		var _this = $(this);
		$("input[name='runNowOrSchedule'][value='schedule']").click()
		schedulePage.schedulePackageOpenPopup(_this);
	});
	
	$(document).on('click', '.schedulePackageRunnow', function() {
		var _this = $(this);
		$("input[name='runNowOrSchedule'][value='runnow']").get(0).click()
		//schedulePage.schedulePackageOpenPopupForSchedule(_this);
		schedulePage.schedulePackageOpenPopup(_this);
		$('input[name="runNowOrSchedule"]').get(1).click();
	});
	
	$(document).on('click', '.runPackage', function() {
		var _this = $(this);
		schedulePage.schedulePackageOpenPopup(_this);
		$('input[name="runNowOrSchedule"]').get(0).click();
	});
	
	$(document).on('click', '.runwithSchedulerPackage', function() {
		
		var _this = $(this);
		var schedulerType = $(_this).attr("data-schedulerType");
		if (schedulerType == 'runwithscheduler') {
			$("input[name='runNowOrSchedule'][value='runwithscheduler']").click()
		} else {
			$("input[name='runNowOrSchedule'][value='runnow']").click()
		}
		schedulePage.schedulePackageOpenPopup(_this);
	});
	
	$(' input[type=radio][name="runNowOrSchedule"]').click(function() {
		common.clearcustomsg("#runNowOrSchedulevalidation");
		common.clearValidations(["#weeklyRecurrencePatternValidation","#recurrencePatternValidation","#scheduleStartDateValidation","#rangeofRecurrenceValidation","#scheduleEndDateVaLidation","#scheduleStartDateValidation"]);
		$("#RecurrencePattern").show();
		$("#scheduleTime").show();
		$("#RangeofRecurrence,#timeZoneDivPanel").show();
		var _this = $(this);
		/*
		 * if(_this.val() == 'runnow'){ $("#scheduleOptionsDiv").hide(); }
		 */
		$("#scheduleOptionsDiv").hide();
		$("#runNowOptionsDiv").hide();
		
		if (_this.val() == 'schedule') {
			$("#confirmSchedule").prop("disabled", false);
			$("#scheduleOptionsDiv").show();
			$("input[name=recurrencePattern]").attr("checked",false);
			$("#dailyRecurrencePattern").hide();
			$("#weeklyRecurrencePattern").hide();
			$("#monthlyRecurrencePattern").hide();
			$("#yearlyRecurrencePattern").hide();
			$("#hourlyRecurrencePattern").hide();
			$("#scheduleStartDate").val("");
			$("#scheduleStartHours").val("00");
			$("#scheduleStartMinutes").val("00");
			$("input[name=rangeOfRecurrence]").attr("checked",false);
			$("#scheduleEndDate").val("");
			$("#scheduleEndDate").removeAttr("disabled");
			$("#scheduleEndDate").datepicker('option','minDate',0);
			$("#timeZone").val(common.getTimezoneName()).trigger("change");
		} else if (_this.val() == 'runnow') {
			if((packageType == "custom" || reRunVal == "reRun") || packageType == "standard"){
				$("#runNowOptionsDiv").show();
				_this.parents("#schedulePackagePopUp").find(".collapseAndExpandDiv").removeClass("collapse").addClass("collapse in");
				schedulePage.showOrHideAdvanceOptions();
			}
		} else if(_this.val() == 'runwithscheduler'){
			if((packageType == "custom" || reRunVal == "reRun") || packageType == "standard"){
				$("#runNowOptionsDiv").show();
				_this.parents("#schedulePackagePopUp").find(".collapseAndExpandDiv").removeClass("collapse").addClass("collapse in");
				schedulePage.showOrHideAdvanceOptions();
			}
		}
	});
	$(' input[name="recurrencePattern"]').click(function() {
						 common.clearcustomsg("#recurrencePatternValidation");
						 $("#scheduleTime,#timeZoneDivPanel,#RangeofRecurrence").show();
						if ($(this).val() == "runnow" || $(this).val() == "hourly" || $(this).val() == "daily") {
							// empty other fields
							$("#dailyRecurrencePattern").show();
							$("#weeklyRecurrencePattern").hide();
							$("#monthlyRecurrencePattern").hide();
							$("#yearlyRecurrencePattern").hide();
							$("#hourlyRecurrencePattern").hide();
							$("#minutesPatternValidation").hide();
						}
						if ($(this).val() == "once") {
							// empty other fields
							$("#dailyRecurrencePattern").hide();
							$("#weeklyRecurrencePattern").hide();
							$("#monthlyRecurrencePattern").hide();
							$("#yearlyRecurrencePattern").hide();
							$("#hourlyRecurrencePattern").hide();
							$("#scheduleTime,#timeZoneDivPanel,#RangeofRecurrence").hide();
						}
						if ($(this).val() == "minutes") {
							// empty other fields
							$("#minutesPatternValidation").show();
							$("#dailyRecurrencePattern").hide();
							$("#weeklyRecurrencePattern").hide();
							$("#monthlyRecurrencePattern").hide();
							$("#yearlyRecurrencePattern").hide();
							$("#hourlyRecurrencePattern").hide();
						}
						if ($(this).val() == "hourly") {
							// empty other fields
							$("#hourlyRecurrencePattern").show();
							$("#minutesPatternValidation").hide();
							$("#dailyRecurrencePattern").hide();
							$("#weeklyRecurrencePattern").hide();
							$("#monthlyRecurrencePattern").hide();
							$("#yearlyRecurrencePattern").hide();
						}
						if ($(this).val() == "weekly") {
							// empty other fields
							$("#weeksToRun").val("1");
							$('input[name="weekDayCheckBox"]').each(function() {
								if ($(this).is(":checked")) {
									$(this).attr('checked', false);
								}
							});
							$("#minutesPatternValidation").hide();
							$("#dailyRecurrencePattern").hide();
							$("#weeklyRecurrencePattern").show();
							$("#monthlyRecurrencePattern").hide();
							$("#yearlyRecurrencePattern").hide();
							$("#hourlyRecurrencePattern").hide();
						}
						if ($(this).val() == "monthly") {
							// empty other fields

							$("#dayOfMonth").val($("#dayOfMonth option:last").val());
							$("#monthsToRun").val($("#monthsToRun option:first").val());
							$("#minutesPatternValidation").hide();
							$("#dailyRecurrencePattern").hide();
							$("#weeklyRecurrencePattern").hide();
							$("#monthlyRecurrencePattern").show();
							$("#yearlyRecurrencePattern").hide();
							$("#hourlyRecurrencePattern").hide();
						}
						if ($(this).val() == "yearly") {
							// empty other fields
							$("#minutesPatternValidation").hide();
							$("#monthOfYear").val("1");
							$("#dayOfYear").val("1");
							$("#dailyRecurrencePattern").hide();
							$("#weeklyRecurrencePattern").hide();
							$("#monthlyRecurrencePattern").hide();
							$("#yearlyRecurrencePattern").show();
							$("#hourlyRecurrencePattern").hide();
						}
					});

	$(' input[name="rangeOfRecurrence"]').click(function() {
		 common.clearcustomsg("#rangeofRecurrenceValidation");
		if ($(this).val() == "NoEndDate") {
			// empty other fields
			$("#noOfMaxOccurences").val("");
			$("#scheduleEndDate").val("");
			$("#scheduleEndDate").attr("disabled","disabled");
		}
		if ($(this).val() == "MaxOccurences") {
			// empty other fields
			$("#scheduleEndDate").val("");
			$("#scheduleEndDate").attr("disabled","disabled");

		}
		if ($(this).val() == "ScheduleEndDate") {
			// empty other fields
			$("#noOfMaxOccurences").val("");
			$("#scheduleEndDate").removeAttr("disabled");

		}
	});

	$("#monthOfYear").on( "change", function() {
				var _this = $(this);
				if (_this.val() == 2) {
					$("#dayOfYear option[value=30]").attr('disabled', true);
					$("#dayOfYear option[value=31]").attr('disabled', true);
				} else if (_this.val() == 4 || _this.val() == 6 || _this.val() == 9 || _this.val() == 11) {
					$("#dayOfYear option[value=30]").attr('disabled', false);
					$("#dayOfYear option[value=31]").attr('disabled', true);
				} else {
					$("#dayOfYear option[value=30]").attr('disabled', false);
					$("#dayOfYear option[value=31]").attr('disabled', false);

				}
			});
	$('.datepicker').datepicker({
		onSelect : function(date) {
			var toDate =  $("#scheduleEndDate"),
				minDate = $("#scheduleStartDate").val();
			var minDateVar = new Date((minDate));
			minDateVar.setDate(minDateVar.getDate() + 1);
			toDate.datepicker('option','minDate',minDateVar);
		},
		dateFormat : 'yy-mm-dd',
		defaultDate : new Date(),
		minDate : 0,
		changeMonth : true,
		changeYear : true,
		yearRange : "0:+20",
		numberOfMonths : 1
	});

	function checkValidation() {
		
			var recurrencePatternvalidation = $('input:radio[name=recurrencePattern]:checked').val();
				if ($('input:radio[name=recurrencePattern]').is(":checked")) {
					if (recurrencePatternvalidation == 'weekly') {
						 var weeksToRun = $("#weeksToRun").val();
						 common.clearValidations(["#weeksToRun","#weeklyRecurrencePatternValidation,.minutesValidation"]);
						 if(weeksToRun.match(/[^0-9]/) || weeksToRun == ""){
							common.showcustommsg("#weeksToRun", "Please enter week(s) in number.");
							return false;
						 }		 
						 if ($('input[type=checkbox]:checked').length == 0) {
							common.showcustommsg("#weeklyRecurrencePatternValidation", globalMessage['anvizent.package.label.pleaseChooseAtLeastOneDay']);
							return false;
						 }
					  }else if(recurrencePatternvalidation == 'hourly'){
							common.clearcustomsg("#scheduleStartDateValidation","[name='hourlyRadios']","#everyhour","#selectedhours + div");
							var scheduleStartDateVal = $("#scheduleStartDate").val();
							if (scheduleStartDateVal.length == 0) {
								common.showcustommsg("#scheduleStartDateValidation", globalMessage['anvizent.package.label.pleaseFillScheduleStartDate']);
								return false;
							}
							
							var typeOfHourRecurrence = $('input:radio[name=hourlyRadios]:checked').val();
							if (typeOfHourRecurrence) {
								
								if (typeOfHourRecurrence == 'every') {
									var noOfHours =  parseInt($("#everyhour").val(),10);
									if ( noOfHours ) {
										$("#everyhour").val(noOfHours);
									} else {
										common.showcustommsg("#everyhour", globalMessage['anvizent.package.message.invalidData']);
										return false;
									}
								}
								if (typeOfHourRecurrence == 'selected') {
									var selectedHours = $("#selectedhours").val();
									if ( !selectedHours ) {
										common.showcustommsg("#selectedhours + div", "Select hours");
										return false;
									}
								
								}
							} else {
								common.showcustommsg("[name='hourlyRadios']", globalMessage['anvizent.package.label.pleaseChooseOption']);
								return false;
							}
					   }else if(recurrencePatternvalidation == 'minutes'){
						   var minutesVal = $("#minutesToRun").val();
						   if (minutesVal == '0') {
								common.showcustommsg(".minutesValidation", globalMessage['anvizent.package.label.pleaseChooseMinutes']);
								return false;
							}
					   }
					} else {
					common.clearValidations(["#recurrencePatternValidation"]);
					common.showcustommsg("#recurrencePatternValidation", globalMessage['anvizent.package.label.pleaseChooseAnyOneOfRecurrencePattern']);
					return false;
				 }
				var rangeOfRecurrenceValidation = $('input:radio[name=rangeOfRecurrence]:checked').val();
				if ($('input:radio[name=rangeOfRecurrence]').is(":checked")) {
					common.clearValidations(["#rangeofRecurrenceValidation","#scheduleEndDateVaLidation"]);
					if (rangeOfRecurrenceValidation == 'ScheduleEndDate') {
						var scheduleEndDateVal = $("#scheduleEndDate").val();
						if (scheduleEndDateVal.length == 0) {
							common.showcustommsg("#scheduleEndDateVaLidation", globalMessage['anvizent.package.label.pleaseFillScheduleEndDate']);
							return false;
						}
						else{
							return true;
						}
					}
					return true;
				} else {
					common.clearValidations(["#rangeofRecurrenceValidation"]);
					common.showcustommsg("#rangeofRecurrenceValidation", globalMessage['anvizent.package.label.pleaseChooseAnyOneOfRangeOfRecurrence']);
					return false;
				}
			
		}	
	
	$(document).on("change","#filterSchedulePackages",function(){
		schedulePage.getPackageListForSchedule();
	});
		
	$(document).on("click","input[name='selectAllPackages']",function(){
		 var packageScheduleTable = $('#packageScheduleTable').DataTable({"language": {
	                "url": selectedLocalePath
	            }});
		 var allPages = packageScheduleTable.cells().nodes();
		 
		 if (!$(this).is(":checked")) {
	            $(allPages).find('input[type="checkbox"]').prop('checked', false);
	            $(".schedulePackages").hide();
	            $(allPages).find(".schedulePackage").show();
	     } 
		 else{
	            $(allPages).find('input[type="checkbox"]').prop('checked', true);
	            $(".schedulePackages").show();
	            $(allPages).find(".schedulePackage").hide();
	     }
	});
	
	
	$(document).on("click","input[name='selectPackage']",function(){
		var packageScheduleTable = $('#packageScheduleTable').DataTable({"language": {
	                "url": selectedLocalePath
	            }});
		var allPages = packageScheduleTable.cells().nodes();
		 
		$("input[name='selectAllPackages']").prop("checked",false);
		
		if($(allPages).find("input[name='selectPackage']:checked").length >= 1){
			$(".schedulePackages").show();
			$(allPages).find(".schedulePackage").hide();
		}
		else if($(allPages).find("input[name='selectPackage']:checked").length < 1){
			$(".schedulePackages").hide();
			$(allPages).find(".schedulePackage").show();
		}		
	});
	
	$(document).on("click",".schedulePackages",function(){
		var packageScheduleTable = $('#packageScheduleTable').DataTable({"language": {
	                "url": selectedLocalePath
	            }});
		var allPages = packageScheduleTable.cells().nodes();
		
		$("#schedulePackagesPopUp").modal("show");		
		common.clearValidations(["#runNowOrSchedulePackagesvalidation"]);		
		$("#schedulePackagesPopUp input[name='runNowOrSchedule']").prop("checked",false);
		
		var packagesScheduleTable = $("#packagesScheduleTable").DataTable({"language": {
	                "url": selectedLocalePath
	            }});
		packagesScheduleTable.clear();
		
		var noOfSelectedPackages = $(allPages).find("input[name='selectPackage']:checked").length;
			
		var count = 1;
		$(allPages).find("input[name='selectPackage']:checked").each(function(){
			var selectOrder = "<select class='selectOrder form-control'>";
			for(var i=1;i<=noOfSelectedPackages;i++){
				if(count==i){
					selectOrder += "<option value='"+i+"' selected='selected' data-orderno='"+count+"'>"+i+"</option>";
				}else{
					selectOrder += "<option value='"+i+"' data-orderno='"+count+"'>"+i+"</option>";
				}
			}
			selectOrder+="</select>";
			var packageId = $(this).attr("data-packgeid"),
				packageName = $(this).attr("data-packgename"),
				packageType = $(this).parents("tr").find("td:eq(2)").text().trim(),
				isClientDbTables = $(this).attr("data-isClientDbTables"),
				industryId = $(this).attr("data-industryId"),
				reSchedule = $(this).attr("data-reSchedule"),
				scheduleId = $(this).attr("data-scheduleId");
			
			var schedulingData = "";
			if(reSchedule == "firstRun")
				schedulingData = "<input type='hidden' name='packageData' data-packgeId='"+packageId+"' data-packgeName='"+packageName+"' data-isClientDbTables='"+isClientDbTables+"' data-industryId='"+industryId+"'>";
			if(reSchedule == "reRun")
				schedulingData = "<input type='hidden' name='packageData' data-packgeId='"+packageId+"' data-packgeName='"+packageName+"' data-isClientDbTables='"+isClientDbTables+"' data-industryId='"+industryId+"' data-reSchedule='reRun' data-scheduleId = '"+scheduleId+"'>";
			selectOrder+=schedulingData;
			
			var row = [];
			row.push(selectOrder);
			row.push(packageId);
			row.push(packageType);
			row.push(packageName);
			packagesScheduleTable.row.add(row);
			
			count++;
		});
		
		packagesScheduleTable.draw(true);
		
		
	});
	 
	$(document).on("change",".selectOrder",function(){
		var newOrder = $(this).val();
		var prevOrder = $(this).find("option:selected").attr("data-orderno");
		
		var packagesScheduleTable = $('#packagesScheduleTable').DataTable({"language": {
	                "url": selectedLocalePath
	            }});
		var allPages = packagesScheduleTable.cells().nodes();
		
		var toBeChangedSelectBox;
		$(allPages).find(".selectOrder").each(function(){
			if(newOrder == $(this).find("option:selected").attr("data-orderno")){
				toBeChangedSelectBox = $(this);
			}
		});
		
		$(toBeChangedSelectBox).find("option").attr("data-orderno",prevOrder);
		$(toBeChangedSelectBox).val(prevOrder);
		$(this).find("option").attr("data-orderno",newOrder);
		
	});
	
	$(document).on("click","#confirmSchedulePackages",function(){			
		common.clearcustomsg("#runNowOrSchedulePackagesvalidation");
		if (!$("input:radio[name='runNowOrSchedule']").is(":checked")) {
			common.showcustommsg("#runNowOrSchedulePackagesvalidation", "Please choose RunNow");
			return false;
		}
		
		var userId = $("#userID").val();
		var reloadUrl = $("#reloadUrl").val();
		var packagesScheduleTable = $('#packagesScheduleTable').DataTable({"language": {
	                "url": selectedLocalePath
	            }});
		var allPages = packagesScheduleTable.cells().nodes();
		var noOfSelectedPackages = $(allPages).find("input[name='packageData']").length;
		var runNowOrSchedule = $("input[type=radio][name='runNowOrSchedule']:checked").val();
		var scheduleStartDate = null;
		var scheduleStartTime = null;
		var recurrencePattern = null;
		var daysToRun = null;
		var weeksToRun = null;
		var dayOfMonth = null;
		var monthsToRun = null;
		var dayOfYear = null;
		var monthOfYear = null;
		var yearsToRun = null;
		var isNoEndDate = false;
		var scheduleEndDate = null;
		var noOfMaxOccurences = null;
		if (runNowOrSchedule == 'runnow') {
			recurrencePattern = runNowOrSchedule;
		}
		var selectedData=[];
			
		for(var i=1;i<=noOfSelectedPackages;i++){
			$(allPages).find(".selectOrder").each(function(){
				if(i == $(this).find("option:selected").val()){
					var scheduleData =  $(this).parents("td").find("input[name='packageData']");
					var packageId = $(scheduleData).attr("data-packgeid"),
					isClientDbTables = $(scheduleData).attr("data-isclientdbtables")
					industryId = $(scheduleData).attr("data-industryid"),
					scheduleId = $(scheduleData).attr("data-scheduleid");
					
					var data = {
							userPackage : {
								packageId : packageId,
								isClientDbTables:isClientDbTables,
								industry : {
									id : industryId
								}
							},								
							schedule : {
								scheduleId : scheduleId,
								scheduleStartDate : scheduleStartDate,
								scheduleStartTime : scheduleStartTime,
								recurrencePattern : recurrencePattern,
								daysToRun : daysToRun,
								weeksToRun : weeksToRun,
								dayOfMonth : dayOfMonth,
								monthsToRun : monthsToRun,
								dayOfYear : dayOfYear,
								monthOfYear : monthOfYear,
								yearsToRun : yearsToRun,
								isNoEndDate : isNoEndDate,
								scheduleEndDate : scheduleEndDate,
								noOfMaxOccurences : noOfMaxOccurences
							}	
					}
					selectedData.push(data);
				}
			});    
		}
		
		showAjaxLoader(true);		
		var count = 0;
		$.each(selectedData,function(i,obj){			 
			$('#pageErrors').empty().hide();
			var packageId = obj.userPackage.packageId;
			var recurrencePattern = obj.schedulePage.recurrencePattern;
					var token = $("meta[name='_csrf']").attr("content");
		  			var header = $("meta[name='_csrf_header']").attr("content");
		  			headers[header] = token;
		    		  if(recurrencePattern == 'runnow'){
				    		  var url_uploadingFilesFromClientDatabase = "/app/user/"+userID+"/package/uploadingFilesFromClientDatabase/"+packageId;
				    		  var myAjax1 = common.loadAjaxCall(url_uploadingFilesFromClientDatabase,'GET','',headers);
				    		  myAjax1.done(function(result) {
				    			  if(result.hasMessages){
				  					for(var i=0; i < result.messages.length; i++){
				  						if (result.messages[i].code === "ERROR") {
				  							common.showErrorAlert(result.messages[i].text);
				  							showAjaxLoader(false);
				  							return false;
				  						}
				  					}
				  					 					
				  				}else{
				  					var url_saveSchedule = "/app/user/" + userId + "/package/schedule/savePackageSchedule?reloadUrl="+reloadUrl;
							   		var myAjax = common.postAjaxCall(url_saveSchedule, 'POST', obj,headers);
							   		myAjax.done(function(result2) {
							   				count++;
							   				if(count == selectedData.length){
							   						showAjaxLoader(false);					   				
							   						schedulePage.getPackageListForSchedule();
							   						$("#pageErrors").show();
							   				}
							   				
 							   				if (result2.hasMessages) {
							   					$("#schedulePackagesPopUp").modal('hide');
							   					var messages=[{
					   								  code : result2.messages[0].code,
					   								  text : result2.messages[0].text
					   							}];
							   					$('#pageErrors').append('<div class="alert '+(result2.messages[0].code === 'SUCCESS' ? 'alert-success' : 'alert-danger')+'">' +result2.messages[0].text+ '</div>');				   					
							   					setTimeout(function() { $("#pageErrors").hide().empty(); }, 10000);
							   				} else {
							   					schedulePage.showMessage("Operation failed.Please Try Again.");
							   				}
							   				
							   				
							   		});
				  				}
				    			  
				    			  	
						    });
		    		  }
		    		  else{
		    			  	var url_saveSchedule = "/app/user/" + userId + "/package/schedule/savePackageSchedule?reloadUrl="+reloadUrl;
					   		var myAjax = common.postAjaxCall(url_saveSchedule, 'POST', obj,headers);
					   		myAjax.done(function(result) {
					   				count++;
					   				if (result.hasMessages) {
					   					$("#schedulePackagesPopUp").modal('hide');
					   					var messages=[{
			   								  code : result.messages[0].code,
			   								  text : result.messages[0].text
			   							}];
					   					$('#pageErrors').append('<div class="alert '+(result.messages[0].code === 'SUCCESS' ? 'alert-success' : 'alert-danger')+'">' +result.messages[0].text+ '</div>');
					   					
					   					if(count == selectedData.length){
					   						showAjaxLoader(false);					   				
					   						schedulePage.getPackageListForSchedule();
					   						$("#pageErrors").show();
					   					}
					   					
					   					setTimeout(function() { $("#pageErrors").hide().empty(); }, 10000);
					   				} else {
					   					schedulePage.showMessage("Operation failed.Please Try Again.");
					   				}
					   				
					   				
					   				
					   		});
		    		  }
		});
	});
	
	$(document).on('show.bs.collapse', '#accordion', function (e) {
		schedulePage.showOrHideAdvanceOptions();
	});
	
	$("#confirmSchedule").on("click",function(){
		var jobRunType = $("input[name='runJobType']:checked").val(),
			fetchFrom = $("input[name='fetchSourceType']:checked").val(),
			runNowOrSchedule = $("input:radio[name='runNowOrSchedule']:checked").val();
		
		 if (runNowOrSchedule == "runnow") {
		    runJobType = $("[name='runJobType']:checked").val();
			var runOnlyDL = false ;
			if ( packageType == 'standard' ) {
				if (!runJobType) {
					runJobType = "all";
				}
			}
			
			if(packageId == null){
				packageId = $("#packageId").val();
			}
	 
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			
			showAjaxLoader(true);
			var url_saveSchedule = "/app/user/" + userID + "/package/packageUploadExecutor/checkPackageExecutionStarted?packageId="+packageId;
			var myAjax = common.postAjaxCall(url_saveSchedule, 'POST', null,headers);
			myAjax.done(function(result) { 
		    showAjaxLoader(false);
			if(result != null && result.hasMessages){
  			  if(result.messages[0].code == "SUCCESS") {
  				executionKey='';
  				executionKey = result.object;
  				$("#schedulePackagePopUp, #showAddedSourcesPopUp").modal('hide');
  				$("#packageExecutionAlreadyStartedPopUp").modal('show');
    		  }	else{
    			  $("#schedulePackagePopUp, #showAddedSourcesPopUp").modal('hide');
    			  schedulePage.initiatePackageRunning(runJobType,packageId,localTimeZone,packageType,executionKey,runNowOrSchedule,scheduleId);
    		  }	    			  		    			  	
		     }else{
	    		var messages = [ {
	    			code : globalMessage['anvizent.message.error.code'],
	    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
	    		} ];
	    		common.displayMessages(messages);
	    	}
			});
			
			
		} else if(runNowOrSchedule == "runwithscheduler"){

		    runJobType = $("[name='runJobType']:checked").val();
			var runOnlyDL = false ;
			if ( packageType == 'standard' ) {
				if (!runJobType) {
					runJobType = "all";
				}
			}
			
			if(packageId == null){
				packageId = $("#packageId").val();
			}
	 
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			headers[header] = token;
			
			showAjaxLoader(true);
			var url_saveSchedule = "/app/user/" + userID + "/package/packageUploadExecutor/checkPackageExecutionStarted?packageId="+packageId;
			var myAjax = common.postAjaxCall(url_saveSchedule, 'POST', null,headers);
			myAjax.done(function(result) { 
		    showAjaxLoader(false);
			if(result != null && result.hasMessages){
  			  if(result.messages[0].code == "SUCCESS") {
  				executionKey='';
  				executionKey = result.object;
  				$("#schedulePackagePopUp, #showAddedSourcesPopUp").modal('hide');
  				$("#packageExecutionAlreadyStartedPopUp").modal('show');
    		  }	else{
    			  $("#schedulePackagePopUp, #showAddedSourcesPopUp").modal('hide');
    			  schedulePage.initiatePackageRunning(runJobType,packageId,localTimeZone,packageType,executionKey,runNowOrSchedule,scheduleId);
    		  }	    			  		    			  	
		     }else{
	    		var messages = [ {
	    			code : globalMessage['anvizent.message.error.code'],
	    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
	    		} ];
	    		common.displayMessages(messages);
	    	}
			});
			
			
		
		} else if(reRunVal != "reRun" || runNowOrSchedule == "schedule" || (reRunVal == "reRun" && jobRunType == "dl") || fetchFrom == "source"){
			schedulePage.schedulePackage();
		}
	});
	
	$("#terminateAndStart").on("click",function(){
		$("#packageExecutionAlreadyStartedPopUp").modal('hide');
		 schedulePage.initiatePackageRunning(runJobType,packageId,localTimeZone,packageType,executionKey,schedulerType,scheduleId);
	});
	
	//unSchedulePackage
	var selectedUnschedulePackage = null;
	$(document).on("click",'.unSchedulePackage',function(){
		selectedUnschedulePackage = $(this);
		$("#alertForUnSchedulePopUp").modal("show");	
	});
	
	$(document).on("click",'.confirmUnSchedulePackage',function(){
		debugger;
		var scheduleid = selectedUnschedulePackage.data("scheduleid");
		var packageid = selectedUnschedulePackage.data("packageid");
		var userId = $("#userID").val();
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
 		var unSchedule = "UNSCHEDULED"
 		var schedule={
 				scheduleId:scheduleid,
 				packageId:packageid,
 				scheduleType:unSchedule
 		}
 		var url_getSourceDetails = "/app/user/" + userId + "/package/schedule/unSchedule";
 		
		var myAjax = common.loadAjaxCall(url_getSourceDetails, 'POST',schedule,headers);
		myAjax.done(function(result) {
			showAjaxLoader(false);
			
			if(result != null && result.hasMessages){
				if(result.messages[0].code == "SUCCESS"){
					var message = "UnScheduled Successfully"
					$("#alertForUnSchedulePopUp").modal("hide");
					$("#successOrErrorMessage").empty().append("<div class='alert alert-success'>"+message+"</div>").show();
  				  	setTimeout(function() { $("#successOrErrorMessage").hide().empty(); }, 10000);  					  
  				    //schedulePage.getPackageListForSchedule();
  				    setTimeout(function(){ window.location.reload(); }, 2000);
				}
				else{
					var errorMsg = "Unable to UnSchedule the package"
					 $("#successOrErrorMessage").empty().append("<div class='alert alert-danger'>"+message+"</div>");
					 setTimeout(function() { $("#successOrErrorMessage").hide(); }, 10000);
				}
			}
			else{
				common.showErrorAlert(globalMessage['anvizent.package.label.unableToProcessYourRequest']);
			}
		});
	});
	
	
	$("input[name='runJobType']").on("click",function(){
		if(packageType == "standard"){
			if($(this).val() == "dl"){
				$(".fetchTypeDiv").hide();
				$("#source").prop("checked",true);
				$("#confirmSchedule").prop("disabled", false);
			}
			else{
				$(".fetchTypeDiv").show();
				if($("input[name='fetchSourceType']:checked").val() == "source"){
					$("#confirmSchedule").prop("disabled", false);
				}else{
					$("#confirmSchedule").prop("disabled", true);
				}
			}
		}
	});
	
	$("input[name='fetchSourceType']").on("click",function(){
		$("#confirmUploadSources, .cancelUploadingSources").prop("disabled", false);
		isContinueProcess = true;
		$(".cancelUploadingSources").val("closeModal");
		if($(this).attr("id") == "archived"){
			$("#confirmSchedule").prop("disabled", true);
			getILConnectionMappingS3DetailsInfoByPackage();
		}
		else{
			$("#confirmSchedule").prop("disabled", false);
		}
	});
	
	$("#confirmUploadSources").on("click", function(){
		$(".cancelUploadingSources").val("");
		successCount = [];
		var userId = $("#userID").val(),
			package_Id = packageId;
		
		var ilConMappingIds = [];
		$("#addedSourceDetails tbody tr").each(function(){
			var mappingId = $(this).find("input[name='mappingId']").val(),
				sourceType = $(this).find(".sourceType:checked").val(),
				uploadStatus = $(this).find(".status").text().trim();
			if ( uploadStatus != "N/A" && sourceType == "source" ) {
				ilConMappingIds.push(mappingId);
			}
		});
		if ( ilConMappingIds.length ) {
			// start animation/loader here
			$("#confirmUploadSources").prop("disabled", true);
			uploadIndividualSources(package_Id, userId, ilConMappingIds, -1);
		}
		else{
			$("#showAddedSourcesPopUp").modal("hide");	
			schedulePage.schedulePackage("completed");
		}
	});
	 
	$(".cancelUploadingSources").on("click", function(){
		$(this).prop("disabled", true);
		isContinueProcess = false;
		if($(this).val() == "closeModal"){
			$("#showAddedSourcesPopUp").modal("hide");
		}
	});
	
	$("#addedSourceDetails").on("click", '.sourceType', function(){
		var $tr = $( $(this).closest("tr") );
		if (this.value == "archived") {
			$tr.find(".status").text("-");
		} else {
			$tr.find(".status").text("Pending");
		}
	});
	
	$("#packageScheduleTable").on("click", ".tablebuttons", function(){
		$("#packageId").val("");
	});
	
	
	
	function uploadIndividualSources(package_Id, userId, mappingIds, arrIndex) {
		arrIndex++;
		
		if (mappingIds.length == arrIndex) {
			// exit
			// stop animation/loader here
			// once every thing is done call save schedule block
			if(successCount.length == mappingIds.length){
				schedulePage.schedulePackage("completed");
			}
			return;
		}
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
		var url_saveSourceDetails = "/app/user/" + userId + "/package/saveModifiedSourceDetails/"+package_Id+"/"+mappingIds[arrIndex];
		var myAjax = $.ajax({
	        url: adt.contextPath+url_saveSourceDetails,
	        type: "POST",
	        data: JSON.stringify(""),
	        headers:headers,
	        cache: false,
	        contentType: "application/json; charset=utf-8",
	        beforeSend : function(){
	        	$(".status"+mappingIds[arrIndex]).empty().append("<span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span>");
	        },
		    error: function (jqXHR, exception) {
		    	common.errorMessages(jqXHR, exception);
		    }
	    });
		myAjax.done(function(result) {
			if(result != null && result.hasMessages){
				if(result.messages[0].code == "SUCCESS"){
					successCount.push("1");
					$(".status"+mappingIds[arrIndex]).empty().append("Done <span class='glyphicon glyphicon-ok' style='color:green'></span>");
					if(isContinueProcess)
						uploadIndividualSources(package_Id, userId, mappingIds, arrIndex);
					else{
						$("#confirmUploadSources, .cancelUploadingSources").prop("disabled", false);
						$("#showAddedSourcesPopUp").modal("hide");
					}
				}
				else{
					$(".status"+mappingIds[arrIndex]).css("color","red").text(result.messages[0].text);
					$(".cancelUploadingSources").val("closeModal");
				}
			}
			else{
				$(".status"+mappingIds[arrIndex]).css("color","red").text("ERROR");
				$(".cancelUploadingSources").val("closeModal");
			}
		});
	}

	function getILConnectionMappingS3DetailsInfoByPackage(){
		var userId = $("#userID").val(),
		package_Id = packageId;
		if(package_Id != ''){
			showAjaxLoader(true);
			var url_getSourceDetails = "/app/user/" + userId + "/package/schedule/getILConnectionMappingS3DetailsInfoByPackage/"+package_Id;
			var myAjax = common.loadAjaxCall(url_getSourceDetails, 'GET','',headers);
			myAjax.done(function(result) {
				showAjaxLoader(false);
				if(result != null && result.hasMessages){
					if(result.messages[0].code == "SUCCESS"){
						schedulePage.buildAddedSourcesTable(result.object);
						$("#showAddedSourcesPopUp").modal("show");
					}
					else{
						common.showErrorAlert(globalMessage['anvizent.package.label.noDataFound']);
					}
				}
				else{
					common.showErrorAlert(globalMessage['anvizent.package.label.unableToProcessYourRequest']);
				}
			});
		}
		else{
			common.showErrorAlert("Package name cannot be empty or null.");
		}
	}
	
	
	var popup = null;
	$(document).on('click', '#executionStatus', function(){
		 
		var resultid = executionKey.split("_");
		var executionId  = resultid [resultid.length-1];
		var uploadOrExecution =  "all";  
		
		var userID = $("#userID").val();
		var url_getExecutionStatus = "/app/user/"+userID+"/package/getUploadAndExecutionStatusComments";
		var selectData = {
				executionId : executionId,
				uploadOrExecution:uploadOrExecution
		};
		var token = $("meta[name='_csrf']").attr("content");
 		var header = $("meta[name='_csrf_header']").attr("content");
 		headers[header] = token;
		 showAjaxLoader(true);
		   var myAjax = common.postAjaxCall(url_getExecutionStatus,'POST', selectData,headers);
		    myAjax.done(function(result) {
		    	showAjaxLoader(false);
		    		  if(result != null && result.hasMessages){ 
		    			  schedulePage.viewExecutionStatusComments(result); 
		    		  }else{
		    			  schedulePage.showMessage(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
		    		  }
		    });
	});
	
}