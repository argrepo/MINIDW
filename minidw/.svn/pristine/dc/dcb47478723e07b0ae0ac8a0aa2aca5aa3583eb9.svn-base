var hybridClientsGrouping = {
	initialPage : function() {
		$("#hybridClientsGroupingTable").dataTable();
		setTimeout(function() {
			$("#pageErrors").hide().empty();
		}, 5000);

		$("#clientId").multipleSelect({
			filter : true,
			placeholder : 'Select Clients',
			enableCaseInsensitiveFiltering : true
		});

	},
	commonRetrievalIntervalValidation : function(from, to) {
		$(".packageIntervalTime").empty();
		var intervalTime = $("#interval").val();
		for (var i = from; i <= to; i++) {
			
			$("<option/>", {
				value : i,
				text : i
			}).appendTo(".packageIntervalTime");
		}
		
		if (intervalTime != "0") {
			$(".packageIntervalTime").val(intervalTime);
		}else{
			$(".packageIntervalTime").val("1");
		}
	},
	loadThreadCount : function() {
		var threadCount = $("#threadCount").val();
		for (var i = 0; i <= 100; i++) {
			$("<option/>", {
				value : i,
				text : i
			}).appendTo(".threadCount");
		}
		if (threadCount != "0") {
			$(".threadCount").val(threadCount)
		}
	},
	changeInterval: function() {
		
		interval = $(".packageRetrievalInterval").val();
		var from, to;
		if (interval == 'seconds' || interval == 'minute') {
			from = 1;
			to = 60;
		} else {
			from = 1;
			to = 24;
		}
		hybridClientsGrouping.commonRetrievalIntervalValidation(from, to);
	},
	validationHybridClientGrupingInfo : function(){
		var name = $("#name").val();
		var description = $("#description").val();
		var clientId = $("#clientId").val();
		var threadCount = $("#packageThreadCount").val();
		var validStatus = true;
		if(name == ""){
			common.showcustommsg("#name", "Please enter name","#name");
  	    	validStatus=false;
		}
		if(description == ""){
			common.showcustommsg("#description","Please enter description","#description");
  	    	validStatus=false;
		}
		if(clientId == null){
			common.showcustommsg("#clientId", "Please Choose client Id","#clientId");
  	    	validStatus=false;
		}
		if(!$("input[name='active']").is(":checked")){
			common.showcustommsg(".activeStatus", "Please choose active status",".activeStatus");
  	    	validStatus=false;
		}
		if(threadCount == "0"){
			common.showcustommsg("#packageThreadCount", "Please enter thread count","#packageThreadCount");
  	    	validStatus=false;
		}
		return validStatus;
	},

}
if ($('.hybridClientsGrouping-page').length) {
	hybridClientsGrouping.initialPage();
	hybridClientsGrouping.loadThreadCount();
	hybridClientsGrouping.changeInterval();
	var interval = $(".packageRetrievalInterval").val();
	$("#addHybridClientGrouping,#updateHybridClientGrouping").on('click',function() {
		common.clearValidations(["#name,#description,#clientId,#packageThreadCount"])
		var status = hybridClientsGrouping.validationHybridClientGrupingInfo();
			if(!status){
				return false;
			}else{
				$("#hybridClientsGrouping").prop("action", $("#addUrl").val());
				this.form.submit();
				showAjaxLoader(true);
			}
		});

	$(".packageRetrievalInterval").on("change", function() {
		$("#interval").val("1");
		hybridClientsGrouping.changeInterval();
	});

}