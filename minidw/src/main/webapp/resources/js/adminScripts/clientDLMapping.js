var clientDLMapping = {
		initialPage : function() {
			$("#dLs").multipleSelect({
				filter : true,
				placeholder : globalMessage['anvizent.package.label.selectDL'],
			    enableCaseInsensitiveFiltering: true
			});
			$("#clientId").select2({               
                allowClear: true,
                theme: "classic"
			}); 
			setTimeout(function() { $("#pageErrors").hide(); }, 5000);
		},
		
	}
if($('.clientDLMapping-page').length){
	
	clientDLMapping.initialPage();
	
	$(document).on("change","#clientId",function(){
		this.form.submit();
		showAjaxLoader(true);
	});
	

	 $(document).on('change', '#clientId', function(){
			 showAjaxLoader(true);
			 this.form.submit(); 
		   });
	 
	 if ( $("#clientId").length > 0 ) {
			if ( $("#clientId").children().length > 1 &&  $("#clientId").val() == '0') {
				$("#clientId").val($("#clientId option").eq(1).val())
				$("#clientId").change();
			}
		}
	
	 $(document).on("click",".selectAll",function () {
			var allPages = $(".dlsInfo");	
			$(allPages).find("input:checkbox").prop('checked', $(this).prop("checked"));
		});
	$(document).on("click",".dlInfoByDlId",function(){
		if($(".dlInfoByDlId").length == $(".dlInfoByDlId:checked").length){
			$(this).parents(".dlsInfo").find(".selectAll").prop("checked",true);
		}else{
			$(this).parents(".dlsInfo").find(".selectAll").prop("checked",false);
		}
	});
	 
	 $(document).on("click","#save",function(){
			
			common.clearcustomsg("#selectOneTableAlert");
			var selCols = $("#dlsAlert").find("input.dlInfoByDlId:checked");
			if (selCols.length === 0) {
				var message = globalMessage['anvizent.package.label.pleaseSelectAtleastOneTable']
				common.showcustommsg("#selectOneTableAlert", message);
				return false;
			}
			var dlid=$(".dlInfoByDlId").attr("data-dLid");
			var dlNames = [];
			$(".dlsInfo").find(".dlInfoByDlId:checked").each(function(){
				dlNames.push({"dlsInfo":$(this).val(), "dL_id":$(this).data("dLid")});
			});
			$("#clientDLMappingForm").prop("action",$("#saveUrl").val());
			this.form.submit();
			showAjaxLoader(true);
		});
} 