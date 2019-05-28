var home={
		initPage: function(){
			/*page initialization code here*/
		}
}
if ($('.root-home-page').length) {
	home.initPage();
	$("#productionReporting,#priceInquiry,#itemInquiry,#quotes").click(function(){
		
		url = $(this).attr("data-link");
		var width = screen.width - 200;
		var height = screen.height - 200;
		var left = (screen.width/2)-(width/2);
		var top = (screen.height/2)-(height/2);
		var win = window.open(url, '', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, copyhistory=no, width='+width+', height='+height+', top='+top+', left='+left);
	});

}