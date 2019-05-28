var scheduleResult = {
	initialPage : function() {
		var scheduledResultsTable = $("#scheduledResultsTable").DataTable( {
				"order": [[ 1, "desc" ]],
		        "bDestroy": true //to allow multilple intializations
		        ,"language": {
	                "url": selectedLocalePath
	            }
		    } );		
		var allPages = scheduledResultsTable.cells().nodes();	
		$(allPages).find(".tool").tooltip({
	          content: function () {
	              return $(allPages).find($(this)).prop('title');
	             
	          }
	    });

	}
	
};

if ($('.scheduledResults-page').length) {
	scheduleResult.initialPage();
	
	$(document).on("click",".scheduleInfo",function(){
		var scheduleStartTime = $(this).attr("data-scheduleTime");
		$("input[name='scheduleStartTime']").val(scheduleStartTime);
		document.forms[0].submit();
	})
	
}