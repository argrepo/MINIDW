var headers = {};
var derivedTable = {
	initialPage : function() {
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		headers[header] = token;
	},
	arrangeSno : function() {
		{
			var i = 0;
			$('.targetTableDirectMappingInfoTable tr').each(function() {
				$(this).find(".sNo").html(i);
				i++;
			});

		}
	},
	showTableCreationPopUP : function(object) {
		$('.duplicateTargetTableName').empty();
		$("#targetTableCreationPopUp").find('.m-check-all').prop('checked',
				false);
		if (object != null) {
			var coulmns = object.fileHeaders.split(",");
			var table = $("#targetTable tbody");
			table.empty();
			var tableRow = customPackage.buildTableColumnPopup(coulmns);
			table.append(tableRow);
			$("#targetTableNameDiv").show();
			$('#targetTableName_creation').val('');
			$("#targetTableCreationPopUp").modal('show');
		}

	},
 
	showSuccessMessage : function(text, hidetick, time) {
		$(".successMessage").show();
		$(".messageText").empty();
		$(".successMessageText").empty();
		$(".successMessageText")
				.html(
						text
								+ (hidetick ? ''
										: '<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'));
		setTimeout(function() {
			$(".successMessage").hide();
		}, time && time > 0 ? time : 2500);
	},
	updateTargetTableStructureTable : function(result) {
		var table = $("#targetTableStructure tbody");
		table.empty();
		var tableRow = '';
		if (result.length > 0) {
			var i = 0;
			$
					.each(
							result,
							function(key, obj) {
								var isPrimaryKey = obj["isPrimaryKey"];
								var isNotNull = obj["isNotNull"];
								var isAutoIncrement = obj["isAutoIncrement"];
								var defaultValue = obj["defaultValue"] || '';
								isPrimaryKey == true ? isPrimaryKey = '<span class="glyphicon glyphicon-ok"></span>'
										: isPrimaryKey = '';
								isNotNull == false ? isNotNull = ''
										: isNotNull = '<span class="glyphicon glyphicon-ok"></span>';
								isAutoIncrement == true ? isAutoIncrement = '<span class="glyphicon glyphicon-ok"></span>'
										: isAutoIncrement = '';
								tableRow += "<tr>" + "<td>" + (i + 1) + "</td>"
										+ "<td>" + obj["columnName"] + "</td>"
										+ "<td>" + obj["dataType"] + "</td>"
										+ "<td>" + obj["columnSize"] + "</td>"
										+ "<td>" + isPrimaryKey + "</td>"
										+ "<td>" + isNotNull + "</td>" + "<td>"
										+ isAutoIncrement + "</td>" 
										+ "<td>"+defaultValue+"</td></tr>";
								i++;
							});
		}
		table.append(tableRow);
		$("#viewTargetTableStructurePopUp").modal('show');

	},
	updateConnectionPanel : function(result) {
		var connectionname = result["connectionName"];
		var databaseId = result["database"].id;
		var connectionType = result["connectionType"];
		var server = result["server"];
		var username = result["username"];
		var connectorId= result["database"].connector_id;
		var protocal= result["database"].protocal;
		var dateFormat = result["dateFormat"];
	    var timesZone = result["timeZone"];
	    
		$("#IL_database_connectionName").val(connectionname).attr("disabled", "disabled");
		$("#IL_database_databaseType").val(databaseId).attr("disabled", "disabled");
		$("#IL_database_connectionType").val(connectionType).attr("disabled", "disabled");
		$("#IL_database_serverName").val(server).attr("disabled", "disabled");
		$("#IL_database_username").val(username).attr("disabled", "disabled");
		$("#dateFormat").val(dateFormat).attr("disabled","disabled");
		$("#timesZone").val(timesZone).attr("disabled","disabled");
		
		if(protocal.indexOf('sforce') != -1){
			$("#typeOfCommand").empty();
			$("#typeOfCommand").append( '<option value="Query">' + globalMessage['anvizent.package.label.query'] + '</option>');
		} else {
			$("#typeOfCommand").empty();
			$("#typeOfCommand").append( '<option value="Query">'
									+ globalMessage['anvizent.package.label.query']
									+ '</option><option value="Stored Procedure">'
									+ globalMessage['anvizent.package.label.storedProcedure']
									+ '</option>');
		}
	},
 
	showCustomMessage : function(selector, msg) {
		$(selector).empty();
		var message = '<div class="alert ' + (msg.code === 'SUCCESS' ? 'alert-success' : 'alert-danger') + '">' + msg.text + '</div>';
		$(selector).append(message).show();
		setTimeout(function() { 	$(selector).hide().empty(); }, 5000);
	},
	clearFlatFileDatabaseValues : function() {
		$("#file_direct").val("");
	},
	updateFilesHavingSameColumns : function(userID, packageId,
			isEveryFileHavingSameColumnNames) {
		var url_updateFilesHavingSameColumns = "/app/user/"
				+ userID + "/package/updateFileHavingSameColumns/" + packageId
				+ "/" + isEveryFileHavingSameColumnNames;
		var myAjax = common.postAjaxCall(url_updateFilesHavingSameColumns,'POST','',headers);
		myAjax.done(function(result) {
			if (result != null && result.object == 1) {
				if (result.hasMessages) {
					if (result.messages[0].code == 'ERROR') {

						common.displayMessages(result.messages[0].text)
						return false;
					}
				}
			}
		});
	},
	updateIsFromDerivedTables : function(userID, packageId, isFromDerivedTables) {

		var url_updateIsFromDerivedTables = "/app/user/" + userID
				+ "/package/updateIsFromDerivedTables/" + packageId + "/"
				+ isFromDerivedTables;
		var myAjax = common.postAjaxCall(url_updateIsFromDerivedTables, 'POST','',headers);
		myAjax.done(function(result) {
			if (result != null && result.hasMessages) {
					if (result.messages[0].code == 'ERROR') {
						common.displayMessages(result.messages[0].text)
						return false;
					}
			}else{
	    		var messages = [ {
	    			code : globalMessage['anvizent.message.error.code'],
	    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
	    		} ];
	    		common.displayMessages(messages);
	    	}
		});

	},

	getTargetTableDirectMappingInfo : function() {
		$("#proceedForMapping").show();
		$("#targetTableSourceInfoDiv").show();
		$("#targetTablePanel").show();
		$("#targetTableDirectMappingInfoTable").show();

		var packageId = $("#packageId").val();
		var userID = $("#userID").val();

		var url_getPackageById = "/app/user/" + userID
				+ "/package/getPackagesById/" + packageId + "";
		var myAjax1 = common.loadAjaxCall(url_getPackageById, 'GET','',headers);
		myAjax1
				.done(function(result1) {
					if (result1 != null && result1.hasMessages) {
						var tableId = null;

						if (result1.table) {
							tableId = result1.table.tableId;
						}

						var url_getFileInfoByPackage = "/app/user/"
								+ userID
								+ "/package/getILsConnectionMappingInfoByPackage/"
								+ packageId + "";
						var myAjax = common.loadAjaxCall(url_getFileInfoByPackage, 'GET','',headers);
						myAjax.done(function(result) {
								if (result != null && result.hasMessages) {
									
									if(result.messages[0].code == "SUCCESS"){
										var table = $("#targetTableDirectMappingInfoTable tbody");
			    		    			table.empty();
			    		    			if(result.object.length > 0){
			    		    				 $("#targetTablePanel,#proceedForMapping").show(); 
			    		    			}else{
			    		    				 $("#targetTablePanel,#proceedForMapping").hide();
			    		    			}
										
										var tableRow = '';
										for (var i = 0; i < result.object.length; i++) {
	
											tableRow += "<tr class='sourceTable'>"
													+ "<td class='smalltd sNo'>"
													+ (i + 1) + "</td>";
	
											if (result.object[i].ilConnectionMapping.isFlatFile
													&& !result.object[i].ilConnectionMapping.isHavingParentTable) {
												tableRow += "<td>"
														+ globalMessage['anvizent.package.label.flatFile']
														+ "</td>";
												tableRow += "<td>"
														+ result.object[i].ilConnectionMapping.filePath.encodeHtml()
														+ "</td>";
												tableRow += "<td class='smalltd'><a class='btn btn-primary btn-sm startLoader' href='"
														+ adt.appContextPath
														+ "/adt/package/viewCustomPackageSource/"
														+ packageId
														+ "/"
														+ result.object[i].ilConnectionMapping.connectionMappingId
														+ "'>"
														+ globalMessage['anvizent.package.label.viewDetails']
														+ " </a></td>";
	
											} else if (!result.object[i].ilConnectionMapping.isFlatFile
													&& !result.object[i].ilConnectionMapping.isHavingParentTable) {
	
												tableRow += "<td>"
														+ globalMessage['anvizent.package.label.database']
														+ "</td>";
												tableRow += "<td>N/A</td>";
												tableRow += "<td class='smalltd'><a class='btn btn-primary btn-sm startLoader' href='"
														+ adt.appContextPath
														+ "/adt/package/viewCustomPackageSource/"
														+ packageId
														+ "/"
														+ result.object[i].ilConnectionMapping.connectionMappingId
														+ "'>"
														+ globalMessage['anvizent.package.label.viewDetails']
														+ " </a></td>";
											} else if (result.object[i].ilConnectionMapping.isHavingParentTable
													&& !result.object[i].ilConnectionMapping.isFlatFile) {
												tableRow += "<td>"
														+ globalMessage['anvizent.package.label.derivedTable']
														+ "</td>";
												tableRow += "<td class='parentTable' data-table='"+ result.object[i].ilConnectionMapping.parent_table_name.encodeHtml() + "'>"
														+ result.object[i].ilConnectionMapping.parent_table_name.encodeHtml()
														+ "</td>";
												tableRow += '<td class="smalltd"><input type="button" class="btn btn-primary btn-sm viewDerivedTableStructure" value="'
														+ globalMessage['anvizent.package.button.viewTableStructure']
														+ '"/></td>';
	
											}
											if (!result1.isScheduled) {
												tableRow += '<td class="smalltd"><button type="button" class="btn btn-primary btn-sm delete-mapping" data-id="'
														+ result.object[i].ilConnectionMapping.connectionMappingId
														+ '"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></button></td>';
											}
	
											tableRow += "</tr>"
										}
										table.append(tableRow);
										$("#targettables").multipleSelect(
												"uncheckAll");
										$("#targetTableSourceInfoDiv").show();
									}else{
										common.displayMessages(result.messages);
									}
									} else if (result.object.length == 0) {
										$("#targetTableSourceInfoDiv").hide();
										$("#targetTablePanel").hide();
										$("#proceedForMapping").hide();
										$("#targetTableDirectMappingInfoTable")
												.hide();
									}

							});
					}else{
			    		var messages = [ {
			    			code : globalMessage['anvizent.message.error.code'],
							text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
						} ];
			    		common.displayMessages(messages);
				}
				});

	},
	showMessage : function(text) {
		$(".messageText").empty();
		$(".successMessageText").empty();
		$(".messageText").html(text);
		$(".message").show();
		setTimeout(function() {
			$(".message").hide();
		}, 10000);
	},
	validateDateForamtAndTimeZone : function(){
	     var timeZone = $("#timesZone").val();
		      var validate = true;
		   	 if(timesZone == ''){
		   		 common.showcustommsg("#timesZone", "please select time zone","#timesZone");
		   		 validate = false;
		   	 }
		   	return validate;
	},
}

if ($('.derivedTable-page').length) {
	derivedTable.initialPage();
	
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	headers[header] = token;
	
	$(function() {

		$('#targettables').multipleSelect(
						{
							filter : true,
							placeholder : globalMessage['anvizent.package.label.selectExistingTables'],

						});
	});

	$(document).on("click", "#uncheckAllBtn", function(e) {

		$("#targettables").multipleSelect("uncheckAll");
		$(".derivedtable > tbody ").remove();

	});



	$(document).on( "click", ".viewDerivedTableStructure", function(e) {
	                	var table = $($(this).closest("tr")).find(".parentTable") .attr('data-table');
						console.log("table ---- > ",table);
						var industryId = $("#industryId").val();
						var userID = $("#userID").val();
						$("#viewTargetTableHeader").text(table.trim());
						if (table != '') {
							showAjaxLoader(true);
							var url_getTableStructure = "/app/user/" + userID + "/package/getTablesStructure/" + industryId + "/" + table.trim() + "";
							var myAjax = common.loadAjaxCall( url_getTableStructure, 'GET','',headers);
							myAjax.done(function(result) {
										showAjaxLoader(false);
								    	if(result != null && result.hasMessages ){
					 			    		if(result.messages[0].code == "SUCCESS"){
					 			    			derivedTable.updateTargetTableStructureTable(result.object);
									    	} else {
										    	common.displayMessages(result.messages);
									    	}
								    	} else {
								    		var messages = [ {
								    			code : globalMessage['anvizent.message.error.code'],
												text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
											} ];
								    		common.displayMessages(messages);
								    	}
								    });
										
						}else {
							var messages = [ {
								code : globalMessage['anvizent.message.error.code'],
								text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
							} ];
				    		common.displayMessages(messages);
						}
					});

	$(document).on( 'change', '#IL_database_databaseType', function() {
		var connectionId=$(this).find('option:selected').attr("data-connectorId");
	    var urlformat = $(this).find('option:selected').data("urlformat");
        $('.serverIpWithPort').empty().text("Format : "+urlformat);
	 });
	
	$(document).on('click', '#flatFiles', function() {
		$("#flatFilesLocationDetails").show();
		$("#databaseConnectionDetails").hide();
		$("#derivedTableDisplay").hide();
		$("#databaseConnectionPanel").hide();
		$("#deleteDatabaseTypeConnection").hide();
	});
	$(document).on('click', '#database', function() {

		$("#flatFilesLocationDetails").hide();
		$("#databaseConnectionDetails").show();
		$("#derivedTableDisplay").hide();
		$("#targettables").multipleSelect("uncheckAll");
		databaseConnection.getILDatabaseConnections('fromSpOrCp');
		$("#deleteDatabaseTypeConnection").hide();
	});
	$(document).on('click', '#derivedTables', function() {
		$("#derivedTableDisplay").show();
		$("#databaseConnectionDetails").hide();
		$("#flatFilesLocationDetails").hide();
		$("#targettables").multipleSelect("uncheckAll");
		$("#databaseConnectionPanel").hide();
		$("#deleteDatabaseTypeConnection").hide();

	});
	$(document).on('change', '#typeOfCommand', function() {
		$(".queryValidatemessageDiv").empty();
		common.clearValidations([ "#queryScript", "#procedureName" ]);
		if (this.value === "Stored Procedure") {
			$("#procedureName").show();
			$("#queryScript").hide();
			$('#checkTablePreview').hide();
			$('#saveILConnectionMapping').hide();
		}
		else {
			$("#procedureName").hide();
			$("#queryScript").show();
			$('#checkTablePreview').hide();
			$('#saveILConnectionMapping').hide();
		}
	});

	$(document).on( 'change', '#existingConnections', function() {

						$('#targetTableDirectMappingPopUp').css('overflow-y', 'scroll');
						if ($("#existingConnections").val() != '') {
							$("#deleteDatabaseTypeConnection").show();
						} else {
							$("#deleteDatabaseTypeConnection").hide();
						}
						$("#procedureName").val('');
						$(".serverIpWithPort").empty();
						$('#targetTableDirectMappingPopUp').addClass('dynamic').css('top', 0);
						$("#typeOfCommand").val('Query');
						$("#queryScript").empty();
						$("#queryScript").show();
						$("#checkTablePreview").hide();
						$("#procedureName").hide();
						$(".queryValidatemessageDiv").empty();
						common.clearValidations([ '#IL_database_connectionName', '#IL_database_serverName', '#IL_database_username', '#IL_database_password' ]);
						if ($(this).val() != '') {
							var connectionId = $(this).val();
							var userID = $("#userID").val();
							var url_getILConnectionById = "/app/user/" + userID+ "/package/getILsConnectionById/" + connectionId + "";
							if (connectionId != null) {
								showAjaxLoader(true);
								var myAjax = common.loadAjaxCall(url_getILConnectionById, 'GET','',headers);
								myAjax.done(function(result) {
									showAjaxLoader(false);
									if (result != null && result.hasMessages) {
										if(result.messages[0].code == "SUCCESS"){
											derivedTable.updateConnectionPanel(result.object);
											$("#queryScript").val("");
											$("#saveNewConnection_dataBaseType").hide();
											$("#testConnection").hide();
											$("#IL_database_password_div").hide();
											$("#databaseConnectionPanel").show();
											$("#checkQuerySyntax").show();
											$(".IL_queryCommand").show();
											$(".buildQuery").show();
											$("#saveILConnectionMapping").hide();
											$('html, body').animate( { scrollTop : $( '#databaseConnectionPanel') .offset().top }, 120);
										}else{
											common.displayMessages(result.messages);
										}
									}else{
										var messages = [ {
											code : globalMessage['anvizent.message.error.code'],
											text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
										} ];
							    		common.displayMessages(messages);
									}

								});
							}
							var packageId = $("#packageId").val();
							$('.buildQuery').attr('href',adt.appContextPath+ '/adt/package/easyQueryBuilderForDerivedTables/'+ packageId + '/'+ connectionId);

						} else {
							$("#databaseConnectionPanel").hide();
						}

					});
	$(document)
			.on(
					'click',
					'#checkQuerySyntax',
					function() {
						$(".queryValidatemessageDiv").empty();
						$("#saveILConnectionMapping").hide();
						$("#checkTablePreview").hide();
						var userID = $("#userID").val();
						var connectionId = $("#existingConnections").val();
						var typeOfCommand = $("#typeOfCommand").val();
						var query = typeOfCommand === "Query" ? $(
								"#queryScript").val() : $("#procedureName")
								.val();

						common.clearValidations([ "#queryScript",
								"#procedureName" ]);

						if (query != '') {
							var selectData = {
								iLConnection : {
									connectionId : connectionId
								},
								iLquery : query,
								typeOfCommand : typeOfCommand
							}
							var token = $("meta[name='_csrf']").attr("content");
							var header = $("meta[name='_csrf_header']").attr("content");
							headers[header] = token;
							showAjaxLoader(true);
							var url_checkQuerySyntax = "/app/user/"
									+ userID + "/package/checksQuerySyntax";
							var myAjax = common.postAjaxCall( url_checkQuerySyntax, 'POST', selectData,headers);
							myAjax.done(function(result) {
										showAjaxLoader(false);
										if (result != null && result.hasMessages) {
											var message = '';
											if(result.messages[0].code == "SUCCESS") {
												message += '<div class="alert alert-success alert-dismissible" role="alert">'
														+ ''
														+ result.messages[0].text
														+ ' <span class="glyphicon glyphicon-ok" aria-hidden="true"></span>'
														+ '</div>';
												$(".queryValidatemessageDiv")
														.append(message);
												setTimeout(function() {
													$(".alert-success").hide()
															.empty();
												}, 10000);
												$("#saveILConnectionMapping")
														.show()
												$("#checkTablePreview").show();
											} else {
												message += '<div class="alert alert-danger alert-dismissible" role="alert">'
														+ ''
														+ result.messages[0].text
														+ '' + '</div>';
												$(".queryValidatemessageDiv")
														.append(message)
												setTimeout(function() {
													$(".alert-danger").hide()
															.empty();
												}, 10000);
											}
										}else{
											var messages = [ {
												code : globalMessage['anvizent.message.error.code'],
												text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
											} ];
								    		common.displayMessages(messages);
										}
									});

						} else {
							common
									.showcustommsg(
											(typeOfCommand === "Query" ? "#queryScript"
													: "#procedureName"),
											typeOfCommand
													+ " "
													+ globalMessage['anvizent.package.label.shouldNotBeEmpty']);
						}
					});

	// validate query table Preview for custom package
	$(document)
			.on(
					'click',
					'#checkTablePreview',
					function() {
						$(".queryValidatemessageDiv").empty();
						$("#saveILConnectionMapping").show();
						var userID = $("#userID").val();
						var packageId = $("#packageId").val();
						var connectionId = $("#existingConnections").val();
						var typeOfCommand = $("#typeOfCommand").val();
						var previewSourceTitle = $("#existingConnections option:selected").text().trim();
						var query = typeOfCommand === "Query" ? $(
								"#queryScript").val() : $("#procedureName")
								.val();
						common.clearValidations([ "#queryScript",
								"#procedureName" ]);
						if (query != '') {
							var selectData = {
								packageId : packageId,
								iLConnection : {
									connectionId : connectionId
								},
								iLquery : query,
								typeOfCommand : typeOfCommand
							}
							if (typeOfCommand === "Stored Procedure") {
								var params = $("div.s-param-vals",
										"#databaseSettings");
								if (params && params.length) {
									var procedureParameters = [];
									params
											.each(function() {
												var param = $(this);
												var paramname = param.find(
														'input.s-param-name')
														.val(), paramvalue = param
														.find(
																'input.s-param-value')
														.val();
												if (paramname.replace(/\s+/g,
														'')
														&& paramvalue.replace(
																/\s+/g, ''))
													procedureParameters
															.push(paramname
																	+ '='
																	+ paramvalue);
											});
									if (procedureParameters.length)
										selectData.procedureParameters = procedureParameters
												.join('^');
								}
							}
							var token = $("meta[name='_csrf']").attr("content");
							var header = $("meta[name='_csrf_header']").attr("content");
							headers[header] = token;
							showAjaxLoader(true);
							var url_checkQuerySyntax = "/app/user/"
									+ userID + "/package/getTablePreview";
							var myAjax = common.postAjaxCall(
									url_checkQuerySyntax, 'POST', selectData,headers);
							myAjax
									.done(function(result) {
										showAjaxLoader(false);
										if (result != null) {
											if (result.hasMessages) {
												if (result.messages[0].code == "ERROR") {

													var message = '<div class="alert alert-danger alert-dismissible" role="alert">'
															+ ''
															+ result.messages[0].text
															+ '' + '</div>';
													$(
															".queryValidatemessageDiv")
															.append(message);
													setTimeout(
															function() {
																$(
																		".alert-danger")
																		.hide()
																		.empty();
															}, 10000);
													return false;
												}
											}

											$("#tablePreviewPopUp").modal('show');
											$("#tablePreviewPopUpHeader").text(previewSourceTitle);
											var list = result.object;
											if (list != null && list.length > 0) {
												var tablePreview = '';
												$
														.each(
																list,
																function(index,
																		row) {

																	tablePreview += '<tr>';
																	$
																			.each(
																					row,
																					function(
																							index1,
																							column) {

																						tablePreview += (index == 0 ? '<th>'
																								+ column
																								+ '</th>'
																								: '<td>'
																										+ column
																										+ '</td>');
																					});
																	tablePreview += '</tr>';
																});
												$(".tablePreview").empty();
												$(".tablePreview").append(
														tablePreview);
											} else {
												$(".tablePreview").empty();
												$(".tablePreview")
														.append(
																globalMessage['anvizent.package.label.noRecordsAvailableInTable']);
											}
										}

									});

						} else {
							common
									.showcustommsg(
											(typeOfCommand === "Query" ? "#queryScript"
													: "#procedureName"),
											typeOfCommand
													+ " "
													+ globalMessage['anvizent.package.label.shouldNotBeEmpty']);
						}
					});

	          $(document).on( 'click','#createNewConnection_dataBaseType', function() {
						databaseConnection.resetConnection();
						$(".buildQuery").hide();
						$("#deleteDatabaseTypeConnection").hide();
						$("#saveILConnectionMapping").hide();
						$(".IL_queryCommand").hide();
						$("#checkQuerySyntax").hide();
						$(".queryValidatemessageDiv").empty();
						$("#saveNewConnection_dataBaseType").show();
						$("#testConnection").show();
						$("#IL_database_password_div").show();
						$("#databaseConnectionPanel").show();
						$("#checkTablePreview").hide();
						 var connectionId=$("#IL_database_databaseType option:selected").attr("data-connectorId");
					     var urlformat = $("#IL_database_databaseType option:selected").data("urlformat");
					 	 $('.serverIpWithPort').empty().text("Format : "+urlformat);

				});

	$("#flatFileType_direct").on('change', function() {
		var fileType = $("#flatFileType_direct option:selected").val();
		if (fileType == "csv") {
			common.clearValidations([ "#delimeter_direct" ]);
			$("#delimeter_direct").val(",");
			$(".delimeter-block").show();
		} else {

			$("#delimeter_direct").val("");
			$(".delimeter-block").hide();
		}
	});
	$('#saveAndUpload_direct').on('click',function() {
						var isFlatFile = true;
						var flatFileType = $("#flatFileType_direct").val();
						var filePath = $("#file_direct").val();
						var delimeter = $("#delimeter_direct").val();
						var isFirstRowHasColumnNames = $(
								"input:radio[name='isFirstRowHasColumnNames']:checked",
								"#flatFilesLocationDetails").val();
						var packageId = $("#packageId").val();
						var userID = $("#userID").val();
						var targetTableId = $("#targetTableId").val();
						var isHavingParentTable = false;
						var isEveryFileHavingSameColumnNames = false;
						var isFromDerivedTables = true;
						// validations
						common.clearValidations([ "#delimeter_direct",
								"input:radio[name='isFirstRowHasColumnNames']",
								"#firstrowcolsvalidation", "#file_direct" ]);

						if (flatFileType == 'csv') {
							if (delimeter == '' || delimeter.match(/^\s+$/)) {
								common.showcustommsg("#delimeter_direct",globalMessage['anvizent.package.label.thisFieldRequired']);
								return false;
							} else if (delimeter.length > '1') {
								common.showcustommsg("#delimeter_direct",globalMessage['anvizent.package.label.delimeterShouldContainOnlyOneCharacter']);
								return false;
							}
						}

						if (filePath == '') {
							common.showcustommsg("#file_direct",globalMessage['anvizent.package.label.pleaseChooseaFile']);
							return false;
						}
						var fileExtension = filePath.replace(/^.*\./, '');
						if (fileExtension != flatFileType) {
							common.showcustommsg("#file_direct",globalMessage['anvizent.package.label.fileExtensionIsNotMatchingWithFileTypeSelected']);
							return false;
						}

						// to show loader.
						showAjaxLoader(true);

						setTimeout(function() {

									// submit the file upload form
									$("#packageIdForFileUpload_direct").val(packageId);
									var formData = new FormData($("#fileUploadForm_direct")[0]);
									var token = $("meta[name='_csrf']").attr("content");
									var header = $("meta[name='_csrf_header']").attr("content");
									headers[header] = token;
									var url_uploadFileIntoS3 = "/app/user/"+ userID+ "/package/uploadsFileIntoS3";
									var myAjax = common.postAjaxCallForFileUpload(url_uploadFileIntoS3,'POST', formData,headers);
									myAjax.done(function(result) {
												if (result != null) {
													if (result.hasMessages) {
														if (result.messages[0].code == "ERROR") {
															var messages = [ {
																code : result.messages[0].code,
																text : result.messages[0].text
															} ];
															showAjaxLoader(false);

															common.showcustommsg("#file_direct",result.messages[0].text);
															return false;
														}
														if (result.messages[0].code == "DUPL_FILE_NAME") {
															var messages = [ {
																code : result.messages[0].code,
																text : result.messages[0].text
															} ];
															showAjaxLoader(false);
															common.showcustommsg("#file_direct",result.messages[0].text);
															return false;
														}

													}

												}
												if (result != null) {
													if (result.object != null) {
														var filePathLocationInServer = result.object;
														// save the mapping
														var selectData = {
															isMapped : true,
															isFlatFile : isFlatFile,
															fileType : flatFileType,
															filePath : filePathLocationInServer,
															delimeter : delimeter,
															isFirstRowHasColoumnNames : isFirstRowHasColumnNames,
															packageId : packageId,
															targetTableId : targetTableId,
															isHavingParentTable : isHavingParentTable,
															isWebservice:false
														};
														var token = $("meta[name='_csrf']").attr("content");
														var header = $("meta[name='_csrf_header']").attr("content");
														headers[header] = token;
														var url_saveILConnectionMapping = "/app/user/"+ userID+ "/package/saveILsConnectionMapping";
														var myAjax = common.postAjaxCall(url_saveILConnectionMapping,'POST',selectData,headers);
														myAjax.done(function(result) {

															// to hide loader.
															showAjaxLoader(false);
															if (result != null && result.hasMessages) {
																if(result.messages[0].code == "SUCCESS"){
																	var messages;
																	$.each(result,function(key,value) {
																		if (key == 'messages') {
																			messages = [ {
																				code : value[0].code,
																				text : value[0].text
																			} ];
	
																		}
																	});
																	derivedTable.showCustomMessage("#flatfilemessage",messages[0]);
																	derivedTable.clearFlatFileDatabaseValues();
																	derivedTable.getTargetTableDirectMappingInfo();
																	derivedTable.updateFilesHavingSameColumns(userID,packageId,isEveryFileHavingSameColumnNames);
																	derivedTable.updateIsFromDerivedTables(userID,packageId,isFromDerivedTables);
																}else{
																	common.displayMessages(result.messages);
																}
																	
															} else {
														    		var messages = [ {
														    			code : globalMessage['anvizent.message.error.code'],
																		text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
																	} ];
														    		common.displayMessages(messages);
															}
															
														});
													}
												}
											});
								}, 150);

					});

	$(document).on('click', '#saveNewConnection_dataBaseType,#testConnection', function() {
						var elementId = this.id;
						var IL_database_connectionName = $("#IL_database_connectionName").val().trim();
						var IL_database_databaseType = $("#IL_database_databaseType").val();
						var IL_database_databaseName = $("#IL_database_databaseType option:selected").text();
						var IL_database_connectionType = $("#IL_database_connectionType").val();
						var IL_database_serverName = $( "#IL_database_serverName").val();
						var IL_database_username = $("#IL_database_username").val();
						var IL_database_password = $("#IL_database_password") .val();
						var userID = $("#userID").val();
						var connector_Id = $( "#IL_database_databaseType option:selected").attr("data-connectorId");
						var protocal = $("#IL_database_databaseType option:selected").data("protocal");
						common.clearValidations([ '#IL_database_connectionName', '#IL_database_serverName', '#IL_database_username', '#IL_database_password' ,"#dateFormat","timesZone"]);
						var dateFormat = $("#dateFormat").val();
					    var timeZone = $("#timesZone").val();
					       
						var selectors = [];
						selectors.push('#IL_database_connectionName');
						selectors.push('#IL_database_serverName');
						if (protocal.indexOf('ucanaccess') == -1) {
							   if(protocal.indexOf('postgresql') == -1){
								   selectors.push('#IL_database_username');
								   selectors.push('#IL_database_password');
							   }else{
								   selectors.push('#IL_database_username');
							   }
						}

						var token = $("meta[name='_csrf']").attr("content");
						var header = $("meta[name='_csrf_header']").attr("content");
						headers[header] = token;
						var valid = common.validate(selectors);
						/*if (connector_Id != 6 && connector_Id != 7 && connector_Id != 3 && connector_Id != 2) {
							if (valid && IL_database_serverName.indexOf(":") == -1) {
								var message = globalMessage['anvizent.package.label.pleaseMentionPortNumberAfterServerIPAddressSeperatedby'] + '<b>:</b> Eg: 127.0.0.1:3306';
								common.showcustommsg("#IL_database_serverName",
										message);
								return false;
							}

						}*/

						if (!valid) {
							return false;
						}
						if(elementId == "saveNewConnection_dataBaseType" || elementId == "updateConnection"){
				           	var validate =  derivedTable.validateDateForamtAndTimeZone();
				           	  if (!validate) {
				      	           return false;
				      	        }
					       }
						var selectData = {
							clientId : userID,
							database : {
								id : IL_database_databaseType,
								name : IL_database_databaseName
							},
							connectionType : IL_database_connectionType,
							server : IL_database_serverName,
							username : IL_database_username,
							password : IL_database_password,
							connectionName : IL_database_connectionName,
							dateFormat:dateFormat,
				           	timeZone:timeZone,
				            dataSourceName:"",
				           	dataSourceNameOther:""
						};
						databaseConnection.testAndSaveDbConnection(elementId,headers,selectData,dataSourceNameOther);

					});
	// delete Database Type Connection
	$(document).on('click', '#deleteDatabaseTypeConnection', function() {
		$("#deleteIlConnection").modal('show');
	});
	$(document).on('click', '#closeDeleteConnectionMapping', function() {
		$("#deleteIlConnection").modal('hide');
		$('#targetTableDirectMappingPopUp').css('overflow-y', 'scroll');
	});
	$(document).on('click', '#closeDeleteConnection', function() {
		$("#deleteIlConnection").modal('hide');
		$('#targetTableDirectMappingPopUp').css('overflow-y', 'scroll');
	});
	$(document).on( 'click', '#confirmDeleteIlConnection', function() {
		
				var token = $("meta[name='_csrf']").attr("content");
				var header = $("meta[name='_csrf_header']").attr("content");
				headers[header] = token;
				
				var userID = $("#userID").val();
				var connectionId = $('#existingConnections').val();
				var connectionName = $("#existingConnections option:selected") .text();
				common.clearValidations([ '#existingConnections' ]);
				var url_deleteILConnection = "/app/user/" + userID + "/package/deleteILConnection/" + connectionId;
				var myAjax = common.postAjaxCall(url_deleteILConnection, 'POST','',headers);
				myAjax.done(function(result) {
					if (result != null && result.hasMessages) {
							if (result.messages[0].code == "ERROR") {
								$('.message').show();
								$('.messageText').empty();
								$(".messageText").append( result.messages[0].text);
								setTimeout(function() { $(".message").hide().empty(); }, 10000);
								return false;
							} else if (result.messages[0].code == "SUCCESS") {

								derivedTable.showSuccessMessage(connectionName + " " + result.messages[0].text, true, 5000);
								databaseConnection.getILDatabaseConnections('fromSpOrCp');
								$('#databaseConnectionPanel').hide();
								$('#deleteDatabaseTypeConnection').hide();
								$("#deleteIlConnection").modal('hide');
							}
					}else{
			    		var messages = [ {
			    			code : globalMessage['anvizent.message.error.code'],
			    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
			    		} ];
			    		common.displayMessages(messages);
			    	}

				});
			});

	$('#saveILConnectionMapping') .on( 'click', function() {
						var isFlatFile = null;
						var flatFileType = null;
						var filePath = null;
						var delimeter = null;
						var isFirstRowHasColumnNames = false;
						var connectionId = null;
						var typeOfCommand = null;
						var queryScript = null;
						var targetTableId = null;
						var isHavingParentTable = null;
						var isEveryFileHavingSameColumnNames = false;
						var isFromDerivedTables = true;
						var clientDbTableNames = '';
						var isWebservice = null;
						var clientDbTables = $("#targettables").multipleSelect( "getSelects", "text");
						jQuery.each(clientDbTables, function(i, val) {
							clientDbTableNames += val.trim() + ",";
						});
						var packageId = null;
						var userID = null;

						if ($('#flatFiles').is(':checked')) {
							isFlatFile = true;
							isWebservice = false;
							flatFileType = $("#flatFileType").val();
							filePath = $("#filePath").val();
							delimeter = $("#delimeter").val();
							if (filePath == '' || delimeter == '') {
								derivedTable .showMessage(globalMessage['anvizent.package.label.pleaseFillRequiredFields']);
								return false;
							}
							if ($("input:radio[name='isFirstRowHasColumnNames']") .is(":checked")) {
								if ($(this).val() == 'Yes') {
									isFirstRowHasColumnNames = true;
								} else {
									isFirstRowHasColumnNames = false;
								}
							} else {
								derivedTable.showMessage(globalMessage['anvizent.package.label.pleaseSelectFirstRowHasColumnNames']);
								return false;
							}

						}
						if ($('#database').is(':checked')) {
							isFlatFile = false;
							isHavingParentTable = false;
							isWebservice = false;
							connectionId = $("#existingConnections").val();
							typeOfCommand = $("#typeOfCommand").val();

							queryScript = typeOfCommand == "Query" ? $("#queryScript").val() : $("#procedureName").val();
							if (queryScript == '') {
								derivedTable.showMessage(globalMessage['anvizent.package.label.pleaseFillRequiredFields']);
								return false;
							}
						}
						if ($('#derivedTables').is(':checked')) {
							isFlatFile = false;
							isHavingParentTable = true;
							isWebservice = false;
						}

						targetTableId = $("#targetTableId").val();
						packageId = $("#packageId").val();
						userID = $("#userID").val();

						var selectData = {
							isMapped : true,
							isFlatFile : isFlatFile,
							fileType : flatFileType,
							filePath : filePath,
							delimeter : delimeter,
							isFirstRowHasColoumnNames : isFirstRowHasColumnNames,
							iLConnection : {
								connectionId : connectionId,
							},
							typeOfCommand : typeOfCommand,
							iLquery : queryScript,
							packageId : packageId,
							targetTableId : targetTableId,
							isHavingParentTable : isHavingParentTable,
							parent_table_name : clientDbTableNames,
							isWebservice : isWebservice 
						};
						var token = $("meta[name='_csrf']").attr("content");
						var header = $("meta[name='_csrf_header']").attr("content");
						headers[header] = token;
						showAjaxLoader(true);

						var url_saveILConnectionMapping = "/app/user/" + userID + "/package/saveILsConnectionMapping";
						var myAjax = common .postAjaxCall(url_saveILConnectionMapping, 'POST', selectData,headers);
						myAjax .done(function(result) {
									showAjaxLoader(false);
										if (result != null && result.hasMessages) {
											if(result.messages[0].code == "SUCCESS"){
											var messages = '';
											$.each( result, function(key, value) {
																if (key == 'messages') {
																	messages = [ {
																		code : value[0].code,
																		text : value[0].text
																	} ];

																}
															});

											common.displayMessages(messages);
											derivedTable.getTargetTableDirectMappingInfo();
											derivedTable.updateFilesHavingSameColumns( userID, packageId, isEveryFileHavingSameColumnNames);
											derivedTable.updateIsFromDerivedTables( userID, packageId, isFromDerivedTables);
											$("#saveILConnectionMapping") .hide()
											$("#targetTableDirectMappingPopUp") .modal('hide');
											$("#queryScript").val("");
											$("#checkTablePreview").hide();
										}else{
											common.displayMessages(result.messages);
										}
										} else {
											alert(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
										}
									
								});

					});

	$(document) .on( 'click', '#saveDerivedTableMapping', 	function() {
						common.clearValidations([ '#targettables' ]);
						var isFlatFile = null;
						var flatFileType = null;
						var filePath = null;
						var delimeter = null;
						var isFirstRowHasColumnNames = false;
						var connectionId = null;
						var typeOfCommand = null;
						var queryScript = null;
						var targetTableId = null;
						var isHavingParentTable = null;
						var filesHavingSameColumns = false;
						var isEveryFileHavingSameColumnNames = false;
						var isFromDerivedTables = true;
						var clientDbTableNames = '';
						var isWebservice = null;
						var clientDbTables = $("#targettables").multipleSelect( 	"getSelects", "text");
						var newTables = [], alreadyAddedTables = [];
						var returnVal = true;

						jQuery.each(clientDbTables, function(i, val) {
							if ($("#targetTableDirectMappingInfoTable").find( ".parentTable").length > 0) {
								$("#targetTableDirectMappingInfoTable").find( ".parentTable").each( function() {
											if ($(this).text() == val.trim()) {
												alreadyAddedTables.push($(this).text());
												return false;
											} else {
												newTables.push(val.trim());
											}
										});
							} else {
								newTables.push(val.trim());
							}
						});

						var uniqueTables = [];
						$.each(newTables, function(i, el) {
							if ($.inArray(el, uniqueTables) === -1)
								uniqueTables.push(el);
						});

						var needToAddTables = uniqueTables
								.filter(function(obj) {
									return alreadyAddedTables.indexOf(obj) == -1;
								});

						var alertString = "";
						$.each(alreadyAddedTables, function(i, val) {
							alertString += val.trim() + ", ";
						});
						var pos = alertString.lastIndexOf(",");
						alertString = alertString.substring(0, pos)

						if (alertString != "") {
							var message = common
									.showcustommsg(
											"#targettables",
											alertString
													+ " "
													+ globalMessage['anvizent.package.label.alreadyAdded'],
											"#targettables");
						}
						if (needToAddTables != "") {
							// add not added Tables
							$.each(needToAddTables, function(i, val) {
								clientDbTableNames += val.trim() + ",";
							});
						}
						if (clientDbTableNames == "" && alertString == "") {
							common
									.showcustommsg(
											"#targettables",
											globalMessage['anvizent.package.label.pleaseSelectExistingTables'],
											"#targettables");
							returnVal = false;
						}
						if (alreadyAddedTables != "" && needToAddTables == "") {
							returnVal = false;
						}

						if (!returnVal) {
							return false;
						}

						var packageId = null;
						var userID = null;

						if ($('#flatFiles').is(':checked')) {
							isFlatFile = true;
							isWebservice = false;
							flatFileType = $("#flatFileType").val();
							filePath = $("#filePath").val();
							delimeter = $("#delimeter").val();
							if (filePath == '' || delimeter == '') {
								derivedTable
										.showMessage(globalMessage['anvizent.package.label.pleaseFillRequiredFields']);
								return false;
							}
							if ($(
									"input:radio[name='isFirstRowHasColumnNames']")
									.is(":checked")) {
								if ($(this).val() == 'Yes') {
									isFirstRowHasColumnNames = true;
								} else {
									isFirstRowHasColumnNames = false;
								}
							} else {
								derivedTable
										.showMessage(globalMessage['anvizent.package.label.pleaseSelectFirstRowHasColumnNames']);
								return false;
							}

						}
						if ($('#database').is(':checked')) {
							isFlatFile = false;
							isHavingParentTable = false;
							isWebservice = false;
							connectionId = $("#existingConnections").val();
							typeOfCommand = $("#typeOfCommand").val();
							queryScript = typeOfCommand == "Query" ? $(
									"#queryScript").val() : $("#procedureName")
									.val();
							if (queryScript == '') {
								derivedTable
										.showMessage(globalMessage['anvizent.package.label.pleaseFillRequiredFields']);
								return false;
							}
						}
						if ($('#derivedTables').is(':checked')) {
							isFlatFile = false;
							isHavingParentTable = true;
							isWebservice = false;
						}

						targetTableId = $("#targetTableId").val();
						packageId = $("#packageId").val();
						userID = $("#userID").val();

						var selectData = {
							isMapped : true,
							isFlatFile : isFlatFile,
							fileType : flatFileType,
							filePath : filePath,
							delimeter : delimeter,
							isFirstRowHasColoumnNames : isFirstRowHasColumnNames,
							iLConnection : {
								connectionId : connectionId,
							},
							typeOfCommand : typeOfCommand,
							iLquery : queryScript,
							packageId : packageId,
							targetTableId : targetTableId,
							isHavingParentTable : isHavingParentTable,
							parent_table_name : clientDbTableNames,
							isWebservice : isWebservice
						};
						var token = $("meta[name='_csrf']").attr("content");
						var header = $("meta[name='_csrf_header']").attr("content");
						headers[header] = token;
						showAjaxLoader(true);
						var url_saveILConnectionMapping = "/app/user/"
								+ userID + "/package/saveILsConnectionMapping";
						var myAjax = common
								.postAjaxCall(url_saveILConnectionMapping,
										'POST', selectData,headers);
						myAjax
								.done(function(result) {
									showAjaxLoader(false);
										if (result != null && result.hasMessages) {
											if(result.messages[0].code == "SUCCESS"){
											var messages = '';
											$
													.each(
															result,
															function(key, value) {
																if (key == 'messages') {
																	messages = [ {
																		code : value[0].code,
																		text : value[0].text
																	} ];

																}
															});

											common.displayMessages(messages);
											derivedTable
													.getTargetTableDirectMappingInfo();
											derivedTable
													.updateFilesHavingSameColumns(
															userID, packageId,
															isEveryFileHavingSameColumnNames);
											derivedTable
													.updateIsFromDerivedTables(
															userID, packageId,
															isFromDerivedTables);
											$("#targetTableDirectMappingPopUp")
													.modal('hide');
											}else{
												common.displayMessages(result.messages);
											}
										} else {
											alert(globalMessage['anvizent.package.label.operationFailedPleaseTryAgain']);
										}
									
								});

					});

	var mappingId = "";
	$(document).on('click', '.delete-mapping', function() {
		mappingId = $(this).attr("data-id");
		$("#deleteIlConnectionSource").modal('show');
	});

	$(document).on('click','#confirmDeleteIlConnectionSource',function() {
						common.clearValidations([ '#targettables' ]);
						$("#targettables").multipleSelect("uncheckAll");
						$("#deleteIlConnectionSource").modal('hide');
						var packageId = $("#packageId").val();
						var userID = $("#userID").val();
						var token = $("meta[name='_csrf']").attr("content");
						var header = $("meta[name='_csrf_header']").attr("content");
						headers[header] = token;
						var url_getPackageById = "/app/user/"+ userID + "/package/deleteConnectionMapping/"+ packageId + "/" + mappingId;
						var myAjax = common.loadAjaxCall(url_getPackageById,'POST','',headers);
						showAjaxLoader(true);
						myAjax.done(function(result) {
							showAjaxLoader(false);
							if (result != null && result.hasMessages) {
								if (result.messages[0].code == "ERROR") {
									var messages = [ {
										code : result.messages[0].code,
										text : result.messages[0].text
									} ];
									common.displayMessages(messages);
								}
								else{
									if (result.object) {
										derivedTable.getTargetTableDirectMappingInfo();
										$('.successDeleteSourceMessage').empty();
										$('.successDeleteSourceMessage').show();
										$('.successDeleteSourceMessage').append('Il source deleted');
										setTimeout(function() {$(".successDeleteSourceMessage")
													.hide().empty();
										}, 10000);
									}else {
										common.showErrorAlert(globalMessage['anvizent.package.label.sourceDeletionFailed']);
									}
								}
						}
						else{
				    		var messages = [ {
				    			code : globalMessage['anvizent.message.error.code'],
				    			text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
				    		} ];
				    		common.displayMessages(messages);
				    	}
					});
	});
	$(document)
			.on(
					'click',
					'#proceedForMapping',
					function() {

						var packageId = $("#packageId").val();
						var userID = $("#userID").val();
						var isEveryFileHavingSameColumnNames = $(
								'input[type=radio][name="isFileHavingSameColumnNames"]:checked')
								.val();
						var token = $("meta[name='_csrf']").attr("content");
						var header = $("meta[name='_csrf_header']").attr("content");
						headers[header] = token;
						if (isEveryFileHavingSameColumnNames === "true") {

							var url_getFileInfoByPackage = "/app/user/"
									+ userID
									+ "/package/getColumnHeaders/"
									+ packageId + "";
							var myAjax = common.loadAjaxCall(url_getFileInfoByPackage, 'GET','',headers);

							myAjax.done(function(result) {

										if (result != null && result.hasMessages) {
												if (result.messages[0].code == 'ERROR') {

													common
															.displayMessages(result.messages)
													return false;
												}
											

											// because every file having same
											// columns
											derivedTable
													.showTableCreationPopUP(result.object);
										}
									});

						} else {

							var packageId = $("#packageId").val();
							var industryId = $("#industryId").val();
							var userID = $("#userID").val();
							showAjaxLoader(true);
							
							
							var url_uploadingFilesFromClientDatabase = "/app/user/"
									+ userID
									+ "/package/uploadingFilesFromClientDatabaseForNoOption/"
									+ packageId;
							var myAjax1 = common.loadAjaxCall( url_uploadingFilesFromClientDatabase, 'GET','',headers);

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
											var url_saveMappingTableInfo = "/app/user/"
												+ userID
												+ "/package/saveMultipleTablesMappingInfo/"
												+ packageId + "/" + industryId;
										var myAjax = common.postAjaxCall( url_saveMappingTableInfo, 'POST', '',headers);

										myAjax .done(function(result1) {
													showAjaxLoader(false);

												if(result1 != null && result1.hasMessages){
													if (result1.messages[0].code === "SUCCESS") {
														setTimeout(
																function() {
																	window.location = adt.appContextPath
																			+ "/adt/package/queryBuilder/"
																			+ packageId
																			+ "/"
																			+ industryId
																			+ "";

																}, 50);
													}else{
														common.displayMessages(result1.messages);
													}
												}else{
													var messages = [ {
														code : globalMessage['anvizent.message.error.code'],
														text : globalMessage['anvizent.package.label.unableToProcessYourRequest']
													} ];
										    		common.displayMessages(messages);
												}
												});
										}
										

									});

						}
					});
	$("#queryScript").on('keyup', function() {
		$("#saveILConnectionMapping").hide();
		$("#checkTablePreview").hide();
		$(".queryValidatemessageDiv").empty();
	});
	$("#procedureName").on('keyup', function() {
		$("#saveILConnectionMapping").hide();
		$("#checkTablePreview").hide();
		$(".queryValidatemessageDiv").empty();
	});
	$(".buildQuery").on('click',function(e) {
				$(this).attr("disabled", true).text(
						globalMessage['anvizent.package.label.loading']);
	});
	function getTimezoneName() {
	    timezone = jstz.determine()
	    return timezone.name();
	} 
}
