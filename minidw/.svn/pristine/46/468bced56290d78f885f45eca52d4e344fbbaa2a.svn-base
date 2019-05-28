<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${ not empty principal}">
<script> 
var globalMessage = {};

globalMessage['anvizent.package.label.addSource'] = '<spring:message code = "anvizent.package.label.addSource"/>';
globalMessage['anvizent.package.link.viewSourceDetails'] = '<spring:message code = "anvizent.package.link.viewSourceDetails"/>';
globalMessage['anvizent.package.button.viewTableStructure'] = '<spring:message code = "anvizent.package.button.viewTableStructure"/>';
globalMessage['anvizent.package.label.pathParams'] = '<spring:message code = "anvizent.package.label.pathParams"/>';
globalMessage['anvizent.package.label.enterKey'] = '<spring:message code = "anvizent.package.label.enterKey"/>';
globalMessage['anvizent.package.label.enterValue'] = '<spring:message code = "anvizent.package.label.enterValue"/>';
globalMessage['anvizent.package.label.pleaseChooseMandatoryField'] = '<spring:message code = "anvizent.package.label.pleaseChooseMandatoryField"/>';
globalMessage['anvizent.package.label.thisFieldRequired'] = '<spring:message code = "anvizent.package.label.thisFieldRequired"/>';
globalMessage['anvizent.package.label.pleaseChooseaFile'] = '<spring:message code = "anvizent.package.label.pleaseChooseaFile"/>';
globalMessage['anvizent.package.label.thereisNoDlFor'] = '<spring:message code = "anvizent.package.label.thereisNoDlFor"/>';
globalMessage['anvizent.package.label.pleasechooseClientName'] = '<spring:message code = "anvizent.package.label.pleasechooseClientName"/>';
globalMessage['anvizent.package.label.PleasechooseDlNames'] = '<spring:message code = "anvizent.package.label.PleasechooseDlNames"/>';
globalMessage['anvizent.package.label.paramNameShoudnotbeEmpty'] = '<spring:message code = "anvizent.package.label.paramNameShoudnotbeEmpty"/>';
globalMessage['anvizent.package.label.dLCreatedSuccesfully'] = '<spring:message code = "anvizent.package.label.dLCreatedSuccesfully"/>';
globalMessage['anvizent.package.label.pleaseEnterDLName'] = '<spring:message code = "anvizent.package.label.pleaseEnterDLName"/>';
globalMessage['anvizent.package.label.pleaseEnterTableName'] = '<spring:message code = "anvizent.package.label.pleaseEnterTableName"/>';
globalMessage['anvizent.package.label.pleaseEnterDLDescription'] = '<spring:message code = "anvizent.package.label.pleaseEnterDLDescription"/>';
globalMessage['anvizent.package.label.pleaseEnterJobName'] = '<spring:message code = "anvizent.package.label.pleaseEnterJobName"/>';
globalMessage['anvizent.package.label.pleaseChooseFile'] = '<spring:message code = "anvizent.package.label.pleaseChooseFile"/>';
globalMessage['anvizent.package.label.pleaseChooseEitherTxtorJarFile'] = '<spring:message code = "anvizent.package.label.pleaseChooseEitherTxtorJarFile"/>';
globalMessage['anvizent.package.label.pleasechooseContextParameters'] = '<spring:message code = "anvizent.package.label.pleasechooseContextParameters"/>';
globalMessage['anvizent.package.label.sameAsDefaultJobPleaseChangeTheJob'] = '<spring:message code = "anvizent.package.label.sameAsDefaultJobPleaseChangeTheJob"/>';
globalMessage['anvizent.package.label.sameAsDefaultJobJarFilesChangeTheJarFiles'] = '<spring:message code = "anvizent.package.label.sameAsDefaultJobJarFilesChangeTheJarFiles"/>';

globalMessage['anvizent.package.label.pleaseEnterILName'] = '<spring:message code = "anvizent.package.label.pleaseEnterILName"/>';
globalMessage['anvizent.package.label.pleaseEnterTableName'] = '<spring:message code = "anvizent.package.label.pleaseEnterTableName"/>';
globalMessage['anvizent.package.label.pleaseEnterILDescription'] = '<spring:message code = "anvizent.package.label.pleaseEnterILDescription"/>';

globalMessage['anvizent.package.label.selectConnection'] = '<spring:message code = "anvizent.package.label.selectConnection"/>';
globalMessage['anvizent.package.label.query'] = '<spring:message code = "anvizent.package.label.query"/>';
globalMessage['anvizent.package.label.storedProcedure'] = '<spring:message code = "anvizent.package.label.storedProcedure"/>';
globalMessage['anvizent.package.label.selectSchema'] = '<spring:message code = "anvizent.package.label.selectSchema"/>';
globalMessage['anvizent.package.label.lengthShouldNotBeEmptyforVARCHARBIT'] = '<spring:message code = "anvizent.package.label.lengthShouldNotBeEmptyforVARCHARBIT"/>';
globalMessage['anvizent.package.label.selectColumnsToProcess'] = '<spring:message code = "anvizent.package.label.selectColumnsToProcess"/>';
globalMessage['anvizent.package.label.flatFile'] = '<spring:message code = "anvizent.package.label.flatFile"/>';
globalMessage['anvizent.package.label.viewDetails'] = '<spring:message code = "anvizent.package.label.viewDetails"/>';
globalMessage['anvizent.package.label.note'] = '<spring:message code = "anvizent.package.label.note"/>';
globalMessage['anvizent.package.label.targetTableAlreadyCreatedForThisPackageIfyouDeletetheSourceallmappingswillbeDestroyed'] = '<spring:message code = "anvizent.package.label.targetTableAlreadyCreatedForThisPackageIfyouDeletetheSourceallmappingswillbeDestroyed"/>';
globalMessage['anvizent.package.label.sourceDeletedSuccessfully'] = '<spring:message code = "anvizent.package.label.sourceDeletedSuccessfully"/>';
globalMessage['anvizent.package.label.sourceDeletionFailed'] = '<spring:message code = "anvizent.package.label.sourceDeletionFailed"/>';
globalMessage['anvizent.package.label.pleaseMentionPortNumberAfterServerIPAddressSeperatedby'] = '<spring:message code = "anvizent.package.label.pleaseMentionPortNumberAfterServerIPAddressSeperatedby"/>';
globalMessage['anvizent.package.label.pleaseSelectTheConnectionToCompleteTheProcess'] = '<spring:message code = "anvizent.package.label.pleaseSelectTheConnectionToCompleteTheProcess"/>';
globalMessage['anvizent.package.label.pleaseSelectFirstRowHasColumnNames'] = '<spring:message code = "anvizent.package.label.pleaseSelectFirstRowHasColumnNames"/>';
globalMessage['anvizent.package.label.pleaseFillRequiredFields'] = '<spring:message code = "anvizent.package.label.pleaseFillRequiredFields"/>';
globalMessage['anvizent.package.label.operationFailedPleaseTryAgain'] = '<spring:message code = "anvizent.package.label.operationFailedPleaseTryAgain"/>';
globalMessage['anvizent.package.label.delimeterShouldContainOnlyOneCharacter'] = '<spring:message code = "anvizent.package.label.delimeterShouldContainOnlyOneCharacter"/>';
globalMessage['anvizent.package.label.fileExtensionIsNotMatchingWithFileTypeSelected'] = '<spring:message code = "anvizent.package.label.fileExtensionIsNotMatchingWithFileTypeSelected"/>';
globalMessage['anvizent.package.label.pleaseEnterTargetTableName'] = '<spring:message code = "anvizent.package.label.pleaseEnterTargetTableName"/>';
globalMessage['anvizent.package.label.tableNameShouldNotContainSpace'] = '<spring:message code = "anvizent.package.label.tableNameShouldNotContainSpace"/>';
globalMessage['anvizent.package.label.tableNameContainsIllegalSpecialCharacters'] = '<spring:message code = "anvizent.package.label.tableNameContainsIllegalSpecialCharacters"/>';
globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInTableName'] = '<spring:message code = "anvizent.package.label.onlyUnderscoreIsAllowedInTableName"/>';
globalMessage['anvizent.package.label.columNameShouldntBeEmpty'] = '<spring:message code = "anvizent.package.label.columNameShouldntBeEmpty"/>';
globalMessage['anvizent.package.label.columnNameShouldNotContainSpace'] = '<spring:message code = "anvizent.package.label.columnNameShouldNotContainSpace"/>';
globalMessage['anvizent.package.label.columNameContainsIllegalSpecialCharacters'] = '<spring:message code = "anvizent.package.label.columNameContainsIllegalSpecialCharacters"/>';
globalMessage['anvizent.package.label.onlyUnderscoreIsAllowedInColumnName'] = '<spring:message code = "anvizent.package.label.onlyUnderscoreIsAllowedInColumnName"/>';
globalMessage['anvizent.package.label.tableDoesNotExist'] = '<spring:message code = "anvizent.package.label.tableDoesNotExist"/>';
globalMessage['anvizent.package.label.noRecordsAvailableInTable'] = '<spring:message code = "anvizent.package.label.noRecordsAvailableInTable"/>';
globalMessage['anvizent.package.label.shouldNotBeEmpty'] = '<spring:message code = "anvizent.package.label.shouldNotBeEmpty"/>';
globalMessage['anvizent.package.label.noRecordsAvailableInTable'] = '<spring:message code = "anvizent.package.label.noRecordsAvailableInTable"/>';
globalMessage['anvizent.package.label.noRecordsAvailable'] = '<spring:message code = "anvizent.package.label.noRecordsAvailable"/>';
globalMessage['anvizent.package.label.pleaseSelectPrimaryFile'] = '<spring:message code = "anvizent.package.label.pleaseSelectPrimaryFile"/>';
globalMessage['anvizent.package.label.pleaseSelectOnePrimaryFile'] = '<spring:message code = "anvizent.package.label.pleaseSelectOnePrimaryFile"/>';
globalMessage['anvizent.package.label.database'] = '<spring:message code = "anvizent.package.label.database"/>';
globalMessage['anvizent.package.label.selectYesOrNo'] = '<spring:message code = "anvizent.package.label.selectYesOrNo"/>';

globalMessage['anvizent.package.label.derivedTable'] = '<spring:message code = "anvizent.package.label.derivedTable"/>';
globalMessage['anvizent.package.label.selectExistingTables'] = '<spring:message code = "anvizent.package.label.selectExistingTables"/>';
globalMessage['anvizent.package.label.alreadyAdded'] = '<spring:message code = "anvizent.package.label.alreadyAdded"/>';
globalMessage['anvizent.package.label.pleaseSelectExistingTables'] = '<spring:message code = "anvizent.package.label.pleaseSelectExistingTables"/>';

globalMessage['anvizent.package.label.search'] = '<spring:message code = "anvizent.package.label.search"/>';
globalMessage['anvizent.package.label.addColumns'] = '<spring:message code = "anvizent.package.label.addColumns"/>';
globalMessage['anvizent.package.label.add'] = '<spring:message code = "anvizent.package.label.add"/>';
globalMessage['anvizent.package.label.remove'] = '<spring:message code = "anvizent.package.label.remove"/>';
globalMessage['anvizent.package.label.columnName'] = '<spring:message code = "anvizent.package.label.columnName"/>';
globalMessage['anvizent.package.label.aliasName'] = '<spring:message code = "anvizent.package.label.aliasName"/>';
globalMessage['anvizent.package.label.dataType'] = '<spring:message code = "anvizent.package.label.dataType"/>';
globalMessage['anvizent.package.label.aggregation'] = '<spring:message code = "anvizent.package.label.aggregation"/>';
globalMessage['anvizent.package.label.createsDuplicateRow'] = '<spring:message code = "anvizent.package.label.createsDuplicateRow"/>';
globalMessage['anvizent.package.label.createsDuplicateColumn'] = '<spring:message code = "anvizent.package.label.createsDuplicateColumn"/>';
globalMessage['anvizent.package.label.delete'] = '<spring:message code = "anvizent.package.label.delete"/>';
globalMessage['anvizent.package.label.whereConditions'] = '<spring:message code = "anvizent.package.label.whereConditions"/>';
globalMessage['anvizent.package.label.addcustomColumn'] = '<spring:message code = "anvizent.package.label.addcustomColumn"/>';
globalMessage['anvizent.package.label.newColumnName'] = '<spring:message code = "anvizent.package.label.newColumnName"/>';
globalMessage['anvizent.package.label.customColumnFormula'] = '<spring:message code = "anvizent.package.label.customColumnFormula"/>';
globalMessage['anvizent.package.label.tableName'] = '<spring:message code = "anvizent.package.label.tableName"/>';
globalMessage['anvizent.package.label.clauses'] = '<spring:message code = "anvizent.package.label.clauses"/>';
globalMessage['anvizent.package.label.inputValue'] = '<spring:message code = "anvizent.package.label.inputValue"/>';
globalMessage['anvizent.package.label.selectTable'] = '<spring:message code = "anvizent.package.label.selectTable"/>';
globalMessage['anvizent.package.label.selectColumn'] = '<spring:message code = "anvizent.package.label.selectColumn"/>';
globalMessage['anvizent.package.label.clause'] = '<spring:message code = "anvizent.package.label.clause"/>';
globalMessage['anvizent.package.label.innerJoin'] = '<spring:message code = "anvizent.package.label.innerJoin"/>';
globalMessage['anvizent.package.label.leftOuterJoin'] = '<spring:message code = "anvizent.package.label.leftOuterJoin"/>';
globalMessage['anvizent.package.label.rightOuterJoin'] = '<spring:message code = "anvizent.package.label.rightOuterJoin"/>';
globalMessage['anvizent.package.label.with'] = '<spring:message code = "anvizent.package.label.with"/>';
globalMessage['anvizent.package.label.orderByColumnSorting'] = '<spring:message code = "anvizent.package.label.orderByColumnSorting"/>';
globalMessage['anvizent.package.label.orderBy'] = '<spring:message code = "anvizent.package.label.orderBy"/>';
globalMessage['anvizent.package.label.pleaseSelectTableToJoin'] = '<spring:message code = "anvizent.package.label.pleaseSelectTableToJoin"/>';
globalMessage['anvizent.package.label.pleaseSelectJoinType'] = '<spring:message code = "anvizent.package.label.pleaseSelectJoinType"/>';
globalMessage['anvizent.package.label.selectClause'] = '<spring:message code = "anvizent.package.label.selectClause"/>';
globalMessage['anvizent.package.label.queryShouldNotBeEmpty'] = '<spring:message code = "anvizent.package.label.queryShouldNotBeEmpty"/>';
globalMessage['anvizent.package.label.connectionIsisEmpty'] = '<spring:message code = "anvizent.package.label.connectionIsisEmpty"/>';
globalMessage['anvizent.package.label.clearSelection'] = '<spring:message code = "anvizent.package.label.clearSelection"/>';

globalMessage['anvizent.package.label.maxFIleSizeEmpty'] = '<spring:message code = "anvizent.package.label.maxFIleSizeEmpty"/>';
globalMessage['anvizent.package.label.maxFIleSizeshouldnot'] = '<spring:message code = "anvizent.package.label.maxFIleSizeshouldnot"/>';
globalMessage['anvizent.package.label.allowsOnlyNumeric'] = '<spring:message code = "anvizent.package.label.allowsOnlyNumeric"/>';

globalMessage['anvizent.package.label.thereisNoIlFor'] = '<spring:message code = "anvizent.package.label.thereisNoIlFor"/>';
globalMessage['anvizent.package.label.pleaseChooseDlName'] = '<spring:message code = "anvizent.package.label.pleaseChooseDlName"/>';
globalMessage['anvizent.package.label.pleaseChooseilNames'] = '<spring:message code = "anvizent.package.label.pleaseChooseilNames"/>';

globalMessage['anvizent.package.label.sourceampMapping'] = '<spring:message code = "anvizent.package.label.sourceampMapping" htmlEscape="false"/>';
globalMessage['anvizent.package.label.xReference'] = '<spring:message code = "anvizent.package.label.xReference"/>';
globalMessage['anvizent.package.label.splitting'] = '<spring:message code = "anvizent.package.label.splitting"/>';
globalMessage['anvizent.package.label.startMapping'] = '<spring:message code = "anvizent.package.label.startMapping"/>';
globalMessage['anvizent.package.label.xReference'] = '<spring:message code = "anvizent.package.label.xReference"/>';
globalMessage['anvizent.package.label.selectXRefKey'] = '<spring:message code = "anvizent.package.label.selectXRefKey"/>';
globalMessage['anvizent.package.label.thisFieldIsRequired'] = '<spring:message code = "anvizent.package.label.thisFieldIsRequired"/>';

globalMessage['anvizent.package.label.viewResults'] = '<spring:message code = "anvizent.package.label.viewResults"/>';
globalMessage['anvizent.package.label.viewExecutionResults'] = '<spring:message code = "anvizent.package.label.viewExecutionResults"/>';
globalMessage['anvizent.package.label.targetTable'] = '<spring:message code = "anvizent.package.label.targetTable"/>';
globalMessage['anvizent.package.label.status'] = '<spring:message code = "anvizent.package.label.status"/>';
globalMessage['anvizent.package.link.done'] = '<spring:message code = "anvizent.package.link.done"/>';
globalMessage['anvizent.package.link.completed'] = '<spring:message code = "anvizent.package.link.completed"/>';
globalMessage['anvizent.package.label.pending'] = '<spring:message code = "anvizent.package.label.pending"/>';
globalMessage['anvizent.package.label.edit'] = '<spring:message code = "anvizent.package.label.edit"/>';
globalMessage['anvizent.package.label.view'] = '<spring:message code = "anvizent.package.label.view"/>';
globalMessage['anvizent.package.label.archive'] = '<spring:message code = "anvizent.package.label.archive"/>';

globalMessage['anvizent.package.label.varchar'] = '<spring:message code = "anvizent.package.label.varchar"/>';
globalMessage['anvizent.package.label.int'] = '<spring:message code = "anvizent.package.label.int"/>';
globalMessage['anvizent.package.label.bigInt'] = '<spring:message code = "anvizent.package.label.bigInt"/>';
globalMessage['anvizent.package.label.float'] = '<spring:message code = "anvizent.package.label.float"/>';
globalMessage['anvizent.package.label.decimal'] = '<spring:message code = "anvizent.package.label.decimal"/>';
globalMessage['anvizent.package.label.bit'] = '<spring:message code = "anvizent.package.label.bit"/>';
globalMessage['anvizent.package.label.dateTime'] = '<spring:message code = "anvizent.package.label.dateTime"/>';
globalMessage['anvizent.package.label.date'] = '<spring:message code = "anvizent.package.label.date"/>';
globalMessage['anvizent.package.label.pleaseEnterTargetTableName'] = '<spring:message code = "anvizent.package.label.pleaseEnterTargetTableName"/>';
globalMessage['anvizent.package.label.lengthShouldNotBeEmptyFor'] = '<spring:message code = "anvizent.package.label.lengthShouldNotBeEmptyFor"/>';
globalMessage['anvizent.package.label.selectColumns'] = '<spring:message code = "anvizent.package.label.selectColumns"/>';

globalMessage['anvizent.package.label.schedule'] = '<spring:message code = "anvizent.package.label.schedule"/>';
globalMessage['anvizent.package.label.recurrence'] = '<spring:message code = "anvizent.package.label.recurrence"/>';
globalMessage['anvizent.package.label.startTime'] = '<spring:message code = "anvizent.package.label.startTime"/>';
globalMessage['anvizent.package.label.endTime'] = '<spring:message code = "anvizent.package.label.endTime"/>';
globalMessage['anvizent.package.label.range'] = '<spring:message code = "anvizent.package.label.range"/>';
globalMessage['anvizent.package.link.reSchedule'] = '<spring:message code = "anvizent.package.link.reSchedule"/>';
globalMessage['anvizent.package.label.pleaseChooseAtLeastOneDay'] = '<spring:message code = "anvizent.package.label.pleaseChooseAtLeastOneDay"/>';
globalMessage['anvizent.package.label.pleaseFillScheduleStartDate'] = '<spring:message code = "anvizent.package.label.pleaseFillScheduleStartDate"/>';
globalMessage['anvizent.package.label.pleaseChooseAnyOneOfRecurrencePattern'] = '<spring:message code = "anvizent.package.label.pleaseChooseAnyOneOfRecurrencePattern"/>';
globalMessage['anvizent.package.label.pleaseFillScheduleEndDate'] = '<spring:message code = "anvizent.package.label.pleaseFillScheduleEndDate"/>';
globalMessage['anvizent.package.label.pleaseChooseAnyOneOfRangeOfRecurrence'] = '<spring:message code = "anvizent.package.label.pleaseChooseAnyOneOfRangeOfRecurrence"/>';
globalMessage['anvizent.package.label.pleaseChooseRunNowOrSchedule'] = '<spring:message code = "anvizent.package.label.pleaseChooseRunNowOrSchedule"/>';

globalMessage['anvizent.package.label.fileType'] = '<spring:message code = "anvizent.package.label.fileType"/>';
globalMessage['anvizent.package.label.delimeter'] = '<spring:message code = "anvizent.package.label.delimeter"/>';
globalMessage['anvizent.package.label.firstRowHasColumnNames'] = '<spring:message code = "anvizent.package.label.firstRowHasColumnNames"/>';
globalMessage['anvizent.package.label.file'] = '<spring:message code = "anvizent.package.label.file"/>';
globalMessage['anvizent.package.label.connectionName'] = '<spring:message code = "anvizent.package.label.connectionName"/>';
globalMessage['anvizent.package.label.databaseType'] = '<spring:message code = "anvizent.package.label.databaseType"/>';
globalMessage['anvizent.package.label.connectorType'] = '<spring:message code = "anvizent.package.label.connectorType"/>';
globalMessage['anvizent.package.label.connectionType'] = '<spring:message code = "anvizent.package.label.connectionType"/>';
globalMessage['anvizent.package.label.serverName'] = '<spring:message code = "anvizent.package.label.serverName"/>';
globalMessage['anvizent.package.label.userName'] = '<spring:message code = "anvizent.package.label.userName"/>';
globalMessage['anvizent.package.label.typeOfCommand'] = '<spring:message code = "anvizent.package.label.typeOfCommand"/>';
globalMessage['anvizent.package.label.selectFileColumn'] = '<spring:message code = "anvizent.package.label.selectFileColumn"/>';
globalMessage['anvizent.package.label.yyyyMMdd'] = '<spring:message code = "anvizent.package.label.yyyyMMdd"/>';
globalMessage['anvizent.package.label.newPackageName'] = '<spring:message code = "anvizent.package.label.newPackageName"/>';
globalMessage['anvizent.package.button.update'] = '<spring:message code = "anvizent.package.button.update"/>';
globalMessage['anvizent.package.label.activate'] = '<spring:message code = "anvizent.package.label.activate"/>';
globalMessage['anvizent.package.label.noSourceIsAdded'] = '<spring:message code = "anvizent.package.label.noSourceIsAdded"/>';
globalMessage['anvizent.message.text.editMappedHeaders'] = '<spring:message code = "anvizent.message.text.editMappedHeaders"/>';
globalMessage['anvizent.message.text.queryisOK'] = '<spring:message code = "anvizent.message.text.queryisOK"/>';
globalMessage['anvizent.package.button.close'] = '<spring:message code = "anvizent.package.button.close"/>';

globalMessage['anvizent.package.label.delimeterShouldBeOneCharacter'] = '<spring:message code = "anvizent.package.label.delimeterShouldBeOneCharacter"/>';
globalMessage['anvizent.package.label.fileMustHaveColumnNamesInFirstRow'] = '<spring:message code = "anvizent.package.label.fileMustHaveColumnNamesInFirstRow"/>';
globalMessage['anvizent.package.label.pleaseChooseMandatoryField'] = '<spring:message code = "anvizent.package.label.pleaseChooseMandatoryField"/>';
globalMessage['anvizent.package.label.notFoundInAuthorisationKeys'] = '<spring:message code = "anvizent.package.label.notFoundInAuthorisationKeys"/>';
globalMessage['anvizent.package.label.pleaseEnterAPIName'] = '<spring:message code = "anvizent.package.label.pleaseEnterAPIName"/>';
globalMessage['anvizent.package.label.pleaseChooseIsAuthenticationRequired'] = '<spring:message code = "anvizent.package.label.pleaseChooseIsAuthenticationRequired"/>';
globalMessage['anvizent.package.label.pleaseEnterAuthenticationURL'] = '<spring:message code = "anvizent.package.label.pleaseEnterAuthenticationURL"/>';
globalMessage['anvizent.package.label.pleaseChooseCookieRequired'] = '<spring:message code = "anvizent.package.label.pleaseChooseCookieRequired"/>';
globalMessage['anvizent.package.label.pleaseEnterAuthenticationDetails'] = '<spring:message code = "anvizent.package.label.pleaseEnterAuthenticationDetails"/>';
globalMessage['anvizent.package.label.pleaseChooseMethodType'] = '<spring:message code = "anvizent.package.label.pleaseChooseMethodType"/>';
globalMessage['anvizent.package.label.pleaseEnterAPIURL'] = '<spring:message code = "anvizent.package.label.pleaseEnterAPIURL"/>';
globalMessage['anvizent.package.label.pleaseEnterHeaderDetails'] = '<spring:message code = "anvizent.package.label.pleaseEnterHeaderDetails"/>';

globalMessage['anvizent.package.label.forCustomFormulaEgColumn'] = '<spring:message code = "anvizent.package.label.forCustomFormulaEgColumn"/>';
globalMessage['anvizent.package.label.setDefaultValueforaColumn'] = '<spring:message code = "anvizent.package.label.setDefaultValueforaColumn"/>';
globalMessage['anvizent.package.label.pleaseEnterColumnName'] = '<spring:message code = "anvizent.package.label.pleaseEnterColumnName"/>';
globalMessage['anvizent.package.label.pleaseSelectValueType'] = '<spring:message code = "anvizent.package.label.pleaseSelectValueType"/>';
globalMessage['anvizent.package.label.pleaseEnterValue'] = '<spring:message code = "anvizent.package.label.pleaseEnterValue"/>';
globalMessage['anvizent.package.label.pleaseSelectValues'] = '<spring:message code = "anvizent.package.label.pleaseSelectValues"/>';
globalMessage['anvizent.package.label.duplicateColumnName'] = '<spring:message code = "anvizent.package.label.duplicateColumnName"/>';
globalMessage['anvizent.package.label.selectAtleastOneColumnFromTargetTable'] = '<spring:message code = "anvizent.package.label.selectAtleastOneColumnFromTargetTable"/>';
globalMessage['anvizent.package.label.successfullyValidated'] = '<spring:message code = "anvizent.package.label.successfullyValidated"/>';
globalMessage['anvizent.package.label.errorWhileValidatingSelectedColumns'] = '<spring:message code = "anvizent.package.label.errorWhileValidatingSelectedColumns"/>';
globalMessage['anvizent.package.label.pleaseEnterTableName'] = '<spring:message code = "anvizent.package.label.pleaseEnterTableName"/>';
globalMessage['anvizent.package.label.errorOcucredPleaseTryAgain'] = '<spring:message code = "anvizent.package.label.errorOcucredPleaseTryAgain"/>';
globalMessage['anvizent.package.label.tableCreated'] = '<spring:message code = "anvizent.package.label.tableCreated"/>';
globalMessage['anvizent.package.label.Selected'] = '<spring:message code = "anvizent.package.label.Selected"/>';
globalMessage['anvizent.package.label.pleaseEnterValidColumnLength'] = '<spring:message code = "anvizent.package.label.pleaseEnterValidColumnLength"/>';

globalMessage['anvizent.package.label.selectIL'] = '<spring:message code = "anvizent.package.label.selectIL"/>';
globalMessage['anvizent.package.label.selectDL'] = '<spring:message code = "anvizent.package.label.selectDL"/>';
globalMessage['anvizent.package.label.pleaseChooseIL'] = '<spring:message code = "anvizent.package.label.pleaseChooseIL"/>';
globalMessage['anvizent.package.label.pleaseChooseDL'] = '<spring:message code = "anvizent.package.label.pleaseChooseDL"/>';
globalMessage['anvizent.package.label.paramName'] = '<spring:message code = "anvizent.package.label.paramName"/>';
globalMessage['anvizent.package.label.paramValue'] = '<spring:message code = "anvizent.package.label.paramValue"/>';
globalMessage['anvizent.package.label.pleaseEnterILQuery'] = '<spring:message code = "anvizent.package.label.pleaseEnterILQuery"/>';
globalMessage['anvizent.package.label.querySaved'] = '<spring:message code = "anvizent.package.label.querySaved"/>';
globalMessage['anvizent.package.label.iLIsupdated'] = '<spring:message code = "anvizent.package.label.iLIsupdated"/>';
globalMessage['anvizent.package.label.dLIsUpdated'] = '<spring:message code = "anvizent.package.label.dLIsUpdated"/>';

 
globalMessage['anvizent.package.button.preview'] = '<spring:message code = "anvizent.package.button.preview"/>';
globalMessage['anvizent.package.label.pleaseSelectColumnName'] = '<spring:message code = "anvizent.package.label.pleaseSelectColumnName"/>';
globalMessage['anvizent.package.label.conditionPairAlreadyExistInThisGroup'] = '<spring:message code = "anvizent.package.label.conditionPairAlreadyExistInThisGroup"/>';
globalMessage['anvizent.package.label.conditionNameShouldNotBeEmpty'] = '<spring:message code = "anvizent.package.label.conditionNameShouldNotBeEmpty"/>';
globalMessage['anvizent.package.label.conditionGroupsNotFound'] = '<spring:message code = "anvizent.package.label.conditionGroupsNotFound"/>';
globalMessage['anvizent.package.label.mapping'] = '<spring:message code = "anvizent.package.label.mapping"/>';
globalMessage['anvizent.package.label.newRecordsInXReferenceTable'] = '<spring:message code = "anvizent.package.label.newRecordsInXReferenceTable"/>';
globalMessage['anvizent.package.label.newRecordsInILTable'] = '<spring:message code = "anvizent.package.label.newRecordsInILTable"/>';
globalMessage['anvizent.package.label.updatedRecordsInXReferenceTable'] = '<spring:message code = "anvizent.package.label.updatedRecordsInXReferenceTable"/>';
globalMessage['anvizent.package.label.success'] = '<spring:message code = "anvizent.package.label.success"/>';
globalMessage['anvizent.package.label.xRef'] = '<spring:message code = "anvizent.package.label.xRef"/>';
globalMessage['anvizent.package.button.validateQuerySP'] = '<spring:message code = "anvizent.package.button.validateQuerySP"/>';
globalMessage['anvizent.package.label.loading'] = '<spring:message code = "anvizent.package.label.loading"/>';
globalMessage['anvizent.package.label.unableToConnectPleaseVerifyNetwork'] = '<spring:message code = "anvizent.package.label.unableToConnectPleaseVerifyNetwork"/>';
globalMessage['anvizent.package.label.requestedJSONParseFailed'] = '<spring:message code = "anvizent.package.label.requestedJSONParseFailed"/>';
globalMessage['anvizent.package.label.ajaxRequestAborted'] = '<spring:message code = "anvizent.package.label.ajaxRequestAborted"/>';
globalMessage['anvizent.package.label.uncaughtError'] = '<spring:message code = "anvizent.package.label.uncaughtError"/>';
globalMessage['anvizent.package.label.unselectAll'] = '<spring:message code = "anvizent.package.label.unselectAll"/>';
globalMessage['anvizent.package.label.selectAll'] = '<spring:message code = "anvizent.package.label.selectAll"/>';
globalMessage['anvizent.package.button.yes'] = '<spring:message code = "anvizent.package.button.yes"/>';
globalMessage['anvizent.package.button.no'] = '<spring:message code = "anvizent.package.button.no"/>';
globalMessage['anvizent.package.label.entitiesAndAtrributes'] = '<spring:message code = "anvizent.package.label.entitiesAndAtrributes"/>';
globalMessage['anvizent.package.label.pleaseChooseTables'] = '<spring:message code = "anvizent.package.label.pleaseChooseTables"/>';
globalMessage['anvizent.package.label.package'] = '<spring:message code = "anvizent.package.label.package"/>';
globalMessage['anvizent.package.label.isDeleted'] = '<spring:message code = "anvizent.package.label.isDeleted"/>';
globalMessage['anvizent.package.label.standard'] = '<spring:message code = "anvizent.package.label.standard"/>';
globalMessage['anvizent.package.label.custom'] = '<spring:message code = "anvizent.package.label.custom"/>';
globalMessage['anvizent.package.label.pleaseSelectAtleastOneTable'] = '<spring:message code = "anvizent.package.label.pleaseSelectAtleastOneTable"/>';
globalMessage['anvizent.package.label.packageName'] = '<spring:message code = "anvizent.package.label.packageName"/>';
globalMessage['anvizent.package.button.cancel'] = '<spring:message code = "anvizent.package.button.cancel"/>';
globalMessage['anvizent.package.button.createNewConnection'] = '<spring:message code = "anvizent.package.button.createNewConnection"/>';
globalMessage['anvizent.package.button.editConnection'] = '<spring:message code = "anvizent.package.button.editConnection"/>';
globalMessage['anvizent.package.label.mappingInfo'] = '<spring:message code = "anvizent.package.label.mappingInfo"/>';

globalMessage['anvizent.package.label.pleaseEnterTokenName'] = '<spring:message code = "anvizent.package.label.pleaseEnterTokenName"/>';
globalMessage['anvizent.package.label.pleaseChoosemethodTypeAuthSelection'] = '<spring:message code = "anvizent.package.label.pleaseChoosemethodTypeAuthSelection"/>';
globalMessage['anvizent.package.label.pleaseEnterResponseObject'] = '<spring:message code = "anvizent.package.label.pleaseEnterResponseObject"/>';
globalMessage['anvizent.package.label.pleaseEnterKeyvalueForAuthentication'] = '<spring:message code = "anvizent.package.label.pleaseEnterKeyvalueForAuthentication"/>';
globalMessage['anvizent.package.message.invalidData'] = '<spring:message code = "anvizent.package.message.invalidData"/>';
globalMessage['anvizent.package.message.defaultValueLengthIsExceeded'] = '<spring:message code = "anvizent.package.message.defaultValueLengthIsExceeded"/>';
globalMessage['anvizent.package.message.constantValueLengthIsExceeded'] = '<spring:message code = "anvizent.package.message.constantValueLengthIsExceeded"/>';
globalMessage['anvizent.package.label.tableNameShouldNotContainILsandDLsNames'] = '<spring:message code = "anvizent.package.label.tableNameShouldNotContainILsandDLsNames"/>';
globalMessage['anvizent.package.label.pleaseChooseCSVFile'] = '<spring:message code = "anvizent.package.label.pleaseChooseCSVFile"/>';


globalMessage['anvizent.package.label.webserviceName'] = '<spring:message code="anvizent.package.label.webserviceName"/>';
globalMessage['anvizent.package.label.apiname'] =  '<spring:message code="anvizent.package.label.apiname"/>';
globalMessage['anvizent.anvizent.package.label.url'] = '<spring:message code="anvizent.package.label.url"/>';

globalMessage['anvizent.package.message.pleaseSelectAtleastOneSchema'] = '<spring:message code="anvizent.package.message.pleaseSelectAtleastOneSchema"/>';
globalMessage['anvizent.package.label.connectionNameContainsIllegalSpecialCharacters'] = '<spring:message code = "anvizent.package.label.connectionNameContainsIllegalSpecialCharacters"/>';
globalMessage['anvizent.package.label.enterWebServiceName'] = '<spring:message code="anvizent.package.label.enterWebServiceName"/>';
globalMessage['anvizent.package.label.selectVerticals'] = '<spring:message code="anvizent.package.label.selectVerticals"/>';
globalMessage['anvizent.package.label.pleasechoosewebservice'] = '<spring:message code="anvizent.package.label.pleasechoosewebservice"/>';
globalMessage['anvizent.package.label.pleasechooseclients'] = '<spring:message code="anvizent.package.label.pleasechooseclients"/>';
globalMessage['anvizent.package.label.pleaseChooseAtLeastOneVertical'] = '<spring:message code="anvizent.package.label.pleaseChooseAtLeastOneVertical"/>';
globalMessage['anvizent.package.label.pleaseChooseAtLeastOneDatabse'] = '<spring:message code="anvizent.package.label.pleaseChooseAtLeastOneDatabse"/>';
globalMessage['anvizent.package.label.pleaseChooseAtLeastOneClient'] = '<spring:message code="anvizent.package.label.pleaseChooseAtLeastOneClient"/>';
globalMessage['anvizent.package.label.pleaseChooseAtLeastOneConnector'] = '<spring:message code="anvizent.package.label.pleaseChooseAtLeastOneConnector"/>';
globalMessage['anvizent.package.label.pleaseChooseAtLeastOneDL'] = '<spring:message code="anvizent.package.label.pleaseChooseAtLeastOneDL"/>';
globalMessage['anvizent.package.label.selectDatabases'] = '<spring:message code="anvizent.package.label.selectDatabases"/>';
globalMessage['anvizent.package.label.selectClients'] = '<spring:message code="anvizent.package.label.selectClients"/>';
globalMessage['anvizent.package.label.selectDL'] = '<spring:message code="anvizent.package.label.selectDL"/>';
globalMessage['anvizent.package.label.selectConnectors'] = '<spring:message code="anvizent.package.label.selectConnectors"/>';
globalMessage['anvizent.package.label.pleasechooseDl'] = '<spring:message code="anvizent.package.label.pleasechooseDl"/>';
globalMessage['anvizent.package.label.pleasechooseKpi'] = '<spring:message code="anvizent.package.label.pleasechooseKpi"/>'; 
globalMessage['anvizent.package.label.pleaseEnterILTableName'] = '<spring:message code="anvizent.package.label.pleaseEnterILTableName"/>';
globalMessage['anvizent.package.label.pleaseEnterXrefILTableName'] = '<spring:message code = "anvizent.package.label.pleaseEnterXrefILTableName"/>';
globalMessage['anvizent.package.label.pleaseChooseJarFile'] = '<spring:message code = "anvizent.package.label.pleaseChooseJarFile"/>';
globalMessage['anvizent.package.label.pleaseEnterPurgeScript'] = '<spring:message code = "anvizent.package.label.pleaseEnterPurgeScript"/>';
globalMessage['anvizent.package.label.xrefTableNotAllowedForILType'] = '<spring:message code = "anvizent.package.label.xrefTableNotAllowedForILType"/>';
globalMessage['anvizent.package.label.pleaseAddAtLeastOneJobFile'] = '<spring:message code = "anvizent.package.label.pleaseAddAtLeastOneJobFile"/>';
globalMessage['anvizent.package.label.duplicateJobFiles'] = '<spring:message code = "anvizent.package.label.duplicateJobFiles"/>';
globalMessage['anvizent.package.label.pleaseEnterDatabaseName'] = '<spring:message code="anvizent.package.label.pleaseEnterDatabaseName"/>';
globalMessage['anvizent.package.label.pleaseEnterDatabaseDriverName'] = '<spring:message code="anvizent.package.label.pleaseEnterDatabaseDriverName"/>';
globalMessage['anvizent.package.label.pleaseEnterDatabaseProtocal'] = '<spring:message code="anvizent.package.label.pleaseEnterDatabaseProtocal"/>';
globalMessage['anvizent.package.label.pleaseEnterDatabaseUrlFormat'] = '<spring:message code="anvizent.package.label.pleaseEnterDatabaseUrlFormat"/>';
globalMessage['anvizent.package.label.PleaseChooseActiveStatus'] = '<spring:message code="anvizent.package.label.PleaseChooseActiveStatus"/>';
globalMessage['anvizent.package.label.pleaseEnterConnectorName'] = '<spring:message code="anvizent.package.label.pleaseEnterConnectorName"/>';
globalMessage['anvizent.package.label.pleaseChooseDatabase'] = '<spring:message code="anvizent.package.label.pleaseChooseDatabase"/>';
globalMessage['anvizent.package.label.pleaseEnterVersion'] = '<spring:message code="anvizent.package.label.pleaseEnterVersion"/>';
globalMessage['anvizent.package.label.JobFile_ExistedJarFile= Either Job File or Existed Jar Files need to be Uploaded.'] = '<spring:message code="anvizent.package.label.JobFile_ExistedJarFile"/>';
globalMessage['anvizent.package.message.specialCharacters_and-and.AreOnlyAllowed'] = '<spring:message code="anvizent.package.message.specialCharacters_and-and.AreOnlyAllowed"/>';

/*defaultQueries*/
globalMessage['anvizent.package.label.pleaseSelectIl'] = '<spring:message code="anvizent.package.label.pleaseSelectIl"/>';
globalMessage['anvizent.package.label.pleaseEnterILQuery'] = '<spring:message code="anvizent.package.label.pleaseEnterILQuery"/>';
globalMessage['anvizent.package.label.pleaseSelectConnectorType'] = '<spring:message code="anvizent.package.label.pleaseSelectConnectorType"/>';

/* clientTableScriptExecution.jsp */
globalMessage['anvizent.package.label.pleasechoosetablescripts'] = '<spring:message code="anvizent.package.label.pleasechoosetablescripts"/>';
globalMessage['anvizent.package.label.pleasechooseatleastonependingscript'] = '<spring:message code="anvizent.package.label.pleasechooseatleastonependingscript"/>';
globalMessage['anvizent.package.label.pleasechooseclientid'] = '<spring:message code="anvizent.package.label.pleasechooseclientid"/>';

/* clientTableScriptsMapping.jsp */
globalMessage['anvizent.package.label.pleasechoosepriority'] = '<spring:message code="anvizent.package.label.pleasechoosepriority"/>';
globalMessage['anvizent.package.label.pleasedontusesamepriority'] = '<spring:message code="anvizent.package.label.pleasedontusesamepriority"/>';
globalMessage['anvizent.package.label.noqueryfound'] = '<spring:message code="anvizent.package.label.noqueryfound"/>';
globalMessage['anvizent.package.label.pleasechooseatleastonenotmappedscript'] = '<spring:message code="anvizent.package.label.pleasechooseatleastonenotmappedscript"/>';


/* clientTableScripts.jsp */
globalMessage['anvizent.package.label.pleasechoosescriptname'] = '<spring:message code="anvizent.package.label.pleasechoosescriptname"/>';
globalMessage['anvizent.package.label.pleasechoosesqlscript'] = '<spring:message code="anvizent.package.label.pleasechoosesqlscript"/>';
globalMessage['anvizent.package.label.pleasechoosescripttype'] = '<spring:message code="anvizent.package.label.pleasechoosescripttype"/>';
globalMessage['anvizent.package.label.pleasechoosetargetschema'] = '<spring:message code="anvizent.package.label.pleasechoosetargetschema"/>';
globalMessage['anvizent.package.label.PleaseChoosedescription'] = '<spring:message code="anvizent.package.label.PleaseChoosedescription"/>';

/* standaedPackage.js */
globalMessage['anvizent.package.message.pleasechoosefromdate'] = '<spring:message code="anvizent.package.message.pleasechoosefromdate"/>';
globalMessage['anvizent.package.message.pleasechoosetodate'] = '<spring:message code="anvizent.package.message.pleasechoosetodate"/>';
globalMessage['anvizent.package.message.pleasechooseloadinterval'] = '<spring:message code="anvizent.package.message.pleasechooseloadinterval"/>';
globalMessage['anvizent.package.message.variblesnotfound'] = '<spring:message code="anvizent.package.message.variblesnotfound"/>';
globalMessage['anvizent.package.message.datevariablenotfound'] = '<spring:message code="anvizent.package.message.datevariablenotfound"/>';
globalMessage['anvizent.package.message.pleasechoosedatabasevariable'] = '<spring:message code="anvizent.package.message.pleasechoosedatabasevariable"/>';
globalMessage['anvizent.package.message.pleasechoosedatabasename'] = '<spring:message code="anvizent.package.message.pleasechoosedatabasename"/>';
globalMessage['anvizent.package.message.pleasechoosedatabasename'] = '<spring:message code="anvizent.package.message.pleasechoosedatabasename"/>';
globalMessage['anvizent.package.message.pleasechooseschemavariable'] = '<spring:message code="anvizent.package.message.pleasechooseschemavariable"/>';
globalMessage['anvizent.package.message.pleasechooseschemaname'] = '<spring:message code="anvizent.package.message.pleasechooseschemaname"/>';
globalMessage['anvizent.package.message.pleaseselectdatabase'] = '<spring:message code="anvizent.package.message.pleaseselectdatabase"/>';
globalMessage['anvizent.package.message.pleaseselectwebservice'] = '<spring:message code="anvizent.package.message.pleaseselectwebservice"/>';
globalMessage['anvizent.package.label.pleaseEnterVariable'] = '<spring:message code="anvizent.package.label.pleaseEnterVariable"/>';

/* dashboardLayoutmaster.js */
globalMessage['anvizent.package.label.pleasechooseils'] = '<spring:message code="anvizent.package.label.pleasechooseils"/>';
globalMessage['anvizent.package.label.pleasechoosekpis'] = '<spring:message code="anvizent.package.label.pleasechoosekpis"/>';
globalMessage['anvizent.package.label.pleasechooseparameters'] = '<spring:message code="anvizent.package.label.pleasechooseparameters"/>';
globalMessage['anvizent.package.label.pleasechooseverticalname'] = '<spring:message code="anvizent.package.label.pleasechooseverticalname"/>';
globalMessage['anvizent.package.label.pleaseChooseAtLeastOneWebservice'] = '<spring:message code="anvizent.package.label.pleaseChooseAtLeastOneWebservice"/>';
globalMessage['anvizent.package.label.unableToProcessYourRequest'] = '<spring:message code="anvizent.package.label.unableToProcessYourRequest"/>';
globalMessage['anvizent.package.label.mapFileHeadersUpload'] = '<spring:message code="anvizent.package.label.mapFileHeadersUpload" htmlEscape="false"/>';
globalMessage['anvizent.message.error.code'] = 'ERROR';
  
globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets'] = '<spring:message code="anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsandalphabets"/>' ;
globalMessage['anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsdotsandalphabets'] = '<spring:message code="anvizent.package.label.specialcharactersnotallowedexceptunderscoredigitsdotsandalphabets"/>' ;

/* historicalLoad */
globalMessage['anvizent.package.label.pleasechooseilid'] =  '<spring:message code="anvizent.package.label.pleasechooseilid"/>' ;
globalMessage['anvizent.package.label.pleasechooseconnectorid'] =  '<spring:message code="anvizent.package.label.pleasechooseconnectorid"/>' ;
globalMessage['anvizent.package.label.pleasechoosehistoricalfromdate'] =  '<spring:message code="anvizent.package.label.pleasechoosehistoricalfromdate"/>' ;
globalMessage['anvizent.package.label.pleasechoosehistoricaltodate'] =  '<spring:message code="anvizent.package.label.pleasechoosehistoricaltodate"/>' ;
globalMessage['anvizent.package.label.pleasechooseloadinterval']=  '<spring:message code="anvizent.package.label.pleasechooseloadinterval"/>' ;
globalMessage['anvizent.package.label.pleasechoosehistoricalqueryscript']=  '<spring:message code="anvizent.package.label.pleasechoosehistoricalqueryscript"/>' ;
globalMessage['anvizent.package.label.noDataFound']=  '<spring:message code="anvizent.package.label.noDataFound"/>' ; 

/* crossReference */
globalMessage['anvizent.package.label.pleaseSelectAtLeastOneRecordForSplit']=  '<spring:message code="anvizent.package.label.pleaseSelectAtLeastOneRecordForSplit"/>' ; 
globalMessage['anvizent.package.label.pleaseSelectMoreThanOneRecordForMerge']=  '<spring:message code="anvizent.package.label.pleaseSelectMoreThanOneRecordForMerge"/>' ;
globalMessage['anvizent.package.label.pleaseSelectAtLeastOneRecordForMerge']=  '<spring:message code="anvizent.package.label.pleaseSelectAtLeastOneRecordForMerge"/>' ;
globalMessage['anvizent.package.label.pleaseEnterXRefValue']=  '<spring:message code="anvizent.package.label.pleaseEnterXRefValue"/>' ; 

/* DefaultMappingConfiguration*/
globalMessage['anvizent.package.label.pleaseChooseAtLeastOneTableScript']=  '<spring:message code="anvizent.package.label.pleaseChooseAtLeastOneTableScript"/>' ; 
globalMessage['anvizent.package.label.pleaseChooseAtLeastOneDL']=  '<spring:message code="anvizent.package.label.pleaseChooseAtLeastOneDL"/>' ;
globalMessage['anvizent.package.label.pleaseChooseAtLeastOneConnector']=  '<spring:message code="anvizent.package.label.pleaseChooseAtLeastOneConnector"/>' ;
globalMessage['anvizent.package.label.pleaseChooseAtLeastOneVertical']=  '<spring:message code="anvizent.package.label.pleaseChooseAtLeastOneVertical"/>' ;
globalMessage['anvizent.package.label.pleaseEnterTemplateName']=  '<spring:message code="anvizent.package.label.pleaseEnterTemplateName"/>' ;

globalMessage['anvizent.package.label.pleaseChooseBucket']=  '<spring:message code="anvizent.package.label.pleaseChooseBucket"/>' ;
globalMessage['anvizent.package.label.pleaseChooseSchedulerMasterClient']=  '<spring:message code="anvizent.package.label.pleaseChooseSchedulerMasterClient"/>' ;
globalMessage['anvizent.package.label.pleaseChooseFileMultiPart']=  '<spring:message code="anvizent.package.label.pleaseChooseFileMultiPart"/>' ;

globalMessage['anvizent.package.label.responseobjectname']=  '<spring:message code="anvizent.package.label.responseobjectname"/>' ; 
globalMessage['anvizent.package.label.url']=  '<spring:message code="anvizent.package.label.url"/>' ; 
globalMessage['anvizent.package.label.methodType']=  '<spring:message code="anvizent.package.label.methodType"/>' ; 


globalMessage['anvizent.package.label.get'] = '<spring:message code="anvizent.package.label.get" />' ;
globalMessage['anvizent.package.label.post'] = '<spring:message code="anvizent.package.label.post" />';
globalMessage['anvizent.package.label.put'] = '<spring:message code="anvizent.package.label.put" />'; 
globalMessage['anvizent.package.label.DELETE'] = '<spring:message code="anvizent.package.label.DELETE" />';
globalMessage['anvizent.package.label.options'] = '<spring:message code="anvizent.package.label.options" />';
globalMessage['anvizent.package.button.validate'] = '<spring:message code="anvizent.package.button.validate" />';

globalMessage['anvizent.package.label.enterUrl'] = '<spring:message code = "anvizent.package.label.enterUrl"/>';
globalMessage['anvizent.package.label.enterresponseobjectname'] = '<spring:message code = "anvizent.package.label.enterresponseobjectname"/>';

globalMessage['anvizent.package.label.apiName'] = '<spring:message code = "anvizent.package.label.apiName"/>';
globalMessage['anvizent.package.label.enterAPIName'] = '<spring:message code = "anvizent.package.label.enterAPIName"/>';
globalMessage['anvizent.package.label.mapHeaders'] = '<spring:message code = "anvizent.package.label.mapHeaders"/>';
 

/*WebServiceAuthentication*/
globalMessage['anvizent.package.label.pleaseEnterWebServiceName'] = '<spring:message code = "anvizent.package.label.pleaseEnterWebServiceName"/>';
globalMessage['anvizent.package.label.enterCallBackUrl'] = '<spring:message code = "anvizent.package.label.enterCallBackUrl"/>';
globalMessage['anvizent.package.label.enterAccessTokenUrl'] = '<spring:message code = "anvizent.package.label.enterAccessTokenUrl"/>';
globalMessage['anvizent.package.label.enterGrantType'] = '<spring:message code = "anvizent.package.label.enterGrantType"/>';
globalMessage['anvizent.package.label.enterReqparams'] = '<spring:message code = "anvizent.package.label.enterReqparams"/>';
globalMessage['anvizent.package.label.duplicateName'] = '<spring:message code = "anvizent.package.label.duplicateName"/>';
globalMessage['anvizent.package.label.pleaseEnterwebServiceConnectionName'] = '<spring:message code = "anvizent.package.label.pleaseEnterwebServiceConnectionName"/>';

/*WebService Validation*/
globalMessage['anvizent.package.message.pleaseEnterWebserviceApiName'] = '<spring:message code="anvizent.package.message.pleaseEnterWebserviceApiName"/>';
globalMessage['anvizent.package.message.pleaseEnterWebserviceApiUrl'] = '<spring:message code="anvizent.package.message.pleaseEnterWebserviceApiUrl"/>';
globalMessage['anvizent.package.message.pleaseChooseMethodtype'] = '<spring:message code="anvizent.package.message.pleaseChooseMethodtype"/>';
globalMessage['anvizent.package.message.pleaseEnterWebServiceResponseObjecName'] = '<spring:message code="anvizent.package.message.pleaseEnterWebServiceResponseObjecName"/>';
globalMessage['anvizent.package.message.pleaseEnterWebServiceResponseColumnObjecName'] = '<spring:message code="anvizent.package.message.pleaseEnterWebServiceResponseColumnObjecName"/>';
globalMessage['anvizent.package.message.pleaseEnterRequestParamName'] = '<spring:message code="anvizent.package.message.pleaseEnterRequestParamName"/>';
globalMessage['anvizent.package.message.pleaseEnterRequestParamValue'] = '<spring:message code="anvizent.package.message.pleaseEnterRequestParamValue"/>';
globalMessage['anvizent.package.message.pleaseEnterPathParam'] = '<spring:message code="anvizent.package.message.pleaseEnterPathParam"/>';
globalMessage['anvizent.package.message.pleaseEnterParamValue'] = '<spring:message code="anvizent.package.message.pleaseEnterParamValue"/>';
globalMessage['anvizent.package.message.pleaseEnterSubApiUrl'] = '<spring:message code="anvizent.package.message.pleaseEnterSubApiUrl"/>';
globalMessage['anvizent.package.message.pleaseChooseSubApiMethodtype'] = '<spring:message code="anvizent.package.message.pleaseChooseSubApiMethodtype"/>';
globalMessage['anvizent.package.message.pleaseEnterSubApiResponseTypeObject'] = '<spring:message code="anvizent.package.message.pleaseEnterSubApiResponseTypeObject"/>';
globalMessage['anvizent.package.label.pleaseChooseDateformat'] = '<spring:message code="anvizent.package.label.pleaseChooseDateformat"/>';
globalMessage['anvizent.package.label.pleaseEnterResponseColumnObject'] = '<spring:message code="anvizent.package.label.pleaseEnterResponseColumnObject"/>';
globalMessage['anvizent.package.message.pleaseEnterRequestParamName'] = '<spring:message code="anvizent.package.message.pleaseEnterRequestParamName"/>';
globalMessage['anvizent.package.message.pleaseEnterRequestParamValue'] = '<spring:message code="anvizent.package.message.pleaseEnterRequestParamValue"/>';
globalMessage['anvizent.package.label.enterClientIdentifier'] = '<spring:message code="anvizent.package.label.enterClientIdentifier"/>';
globalMessage['anvizent.package.label.enterClientSecret'] = '<spring:message code="anvizent.package.label.enterClientSecret"/>';
globalMessage['anvizent.package.label.enterScope'] = '<spring:message code="anvizent.package.label.enterScope"/>';
globalMessage['anvizent.package.label.enterState'] = '<spring:message code="anvizent.package.label.enterState"/>';
globalMessage['anvizent.package.label.enterParamValue'] = '<spring:message code="anvizent.package.label.enterParamValue"/>';

/*General Settings*/
globalMessage['anvizent.package.label.pleaseChooseOption'] = '<spring:message code="anvizent.package.label.pleaseChooseOption"/>';
globalMessage['anvizent.package.label.pleaseChooseFileSize'] = '<spring:message code="anvizent.package.label.pleaseChooseFileSize"/>';
globalMessage['anvizent.package.label.pleaseChooseTimeZone'] = '<spring:message code="anvizent.package.label.pleaseChooseTimeZone"/>';
globalMessage['anvizent.package.label.selectTimeZone'] = '<spring:message code="anvizent.package.label.selectTimeZone"/>';
globalMessage['anvizent.package.label.timeZone'] = '<spring:message code="anvizent.package.label.timeZone"/>';

globalMessage['anvizent.package.label.pleaseEnterJobVersion'] = '<spring:message code="anvizent.package.label.pleaseEnterJobVersion"/>';
globalMessage['anvizent.package.label.pleaseEnterKpiName'] = '<spring:message code="anvizent.package.label.pleaseEnterKpiName"/>';
globalMessage['anvizent.package.label.pleaseEnterKpiDescription'] = '<spring:message code="anvizent.package.label.pleaseEnterKpiDescription"/>';
globalMessage['anvizent.package.label.pleaseEnterVerticalName'] = '<spring:message code="anvizent.package.label.pleaseEnterVerticalName"/>';
globalMessage['anvizent.package.label.pleaseEnterVerticalDescription'] = '<spring:message code="anvizent.package.label.pleaseEnterVerticalDescription"/>'; 

globalMessage['anvizent.package.label.pathParameters'] = '<spring:message code="anvizent.package.label.pathParameters"/>'; 
globalMessage['anvizent.package.label.headerKeyValues'] = '<spring:message code="anvizent.package.label.headerKeyValues"/>';
globalMessage['anvizent.package.label.requestBodyParameters'] = '<spring:message code="anvizent.package.label.requestBodyParameters"/>';
globalMessage['anvizent.package.label.requestParameters'] = '<spring:message code="anvizent.package.label.requestParameters"/>';

globalMessage['anvizent.package.message.pleasechoosewebservice'] = '<spring:message code="anvizent.package.message.pleasechoosewebservice"/>';
globalMessage['anvizent.package.message.invalidurl'] = '<spring:message code="anvizent.package.message.invalidurl"/>';
globalMessage['anvizent.package.message.nopathparamfound'] = '<spring:message code="anvizent.package.message.nopathparamfound"/>';

globalMessage['anvizent.package.label.selectdbvariable'] = '<spring:message code="anvizent.package.label.selectdbvariable"/>';
globalMessage['anvizent.package.label.selectschemavariable'] = '<spring:message code="anvizent.package.label.selectschemavariable"/>';
globalMessage['anvizent.package.label.selectschemaname'] = '<spring:message code="anvizent.package.label.selectschemaname"/>';
globalMessage['anvizent.package.label.selectdbname'] = '<spring:message code="anvizent.package.label.selectdbname"/>';

globalMessage['anvizent.package.msg.pleaseChoosetimezone'] = '<spring:message code="anvizent.package.msg.pleaseChoosetimezone"/>';
globalMessage['anvizent.package.msg.datePlaceholderNotFoundInQuery'] = '<spring:message code="anvizent.package.msg.datePlaceholderNotFoundInQuery"/>';
globalMessage['anvizent.package.label.columnNameShouldNotContain'] = '<spring:message code="anvizent.package.label.columnNameShouldNotContain"/>';
globalMessage['anvizent.package.label.enterDataSourceName'] = '<spring:message code="anvizent.package.label.enterDataSourceName"/>';
globalMessage['anvizent.package.label.PleaseEnterFilePath'] = '<spring:message code="anvizent.package.label.PleaseEnterFilePath"/>';
globalMessage['anvizent.package.label.pleaseEnterVersionNumber'] = '<spring:message code="anvizent.package.label.pleaseEnterVersionNumber"/>';
globalMessage['anvizent.package.label.enterDataSource'] = '<spring:message code="anvizent.package.label.enterDataSource"/>';
globalMessage['anvizent.package.label.pleaseChooseStartDate'] = '<spring:message code="anvizent.package.label.pleaseChooseStartDate"/>';
globalMessage['anvizent.package.label.pleaseChooseEndDate'] = '<spring:message code="anvizent.package.label.pleaseChooseEndDate"/>';
globalMessage['anvizent.package.label.runNow'] = '<spring:message code="anvizent.package.label.runNow"/>';
globalMessage['anvizent.package.label.runwithscheduler'] = '<spring:message code="anvizent.package.label.runwithscheduler"/>';
globalMessage['anvizent.package.label.selectClientIds'] = '<spring:message code="anvizent.package.label.selectClientIds"/>';

globalMessage['anvizent.package.label.availableColumns'] = '<spring:message code="anvizent.package.label.availableColumns"/>';
globalMessage['anvizent.package.label.selectSchemas'] = '<spring:message code="anvizent.package.label.selectSchemas"/>';
globalMessage['anvizent.package.label.databaseandschema'] = '<spring:message code="anvizent.package.label.databaseandschema"/>';
globalMessage['anvizent.package.label.noSchemaSelected'] = '<spring:message code="anvizent.package.label.noSchemaSelected"/>';
globalMessage['anvizent.package.label.selectedAll'] = '<spring:message code="anvizent.package.label.selectedAll"/>';
globalMessage['anvizent.package.label.selectALLOpt'] = '<spring:message code="anvizent.package.label.selectALLOpt"/>';
globalMessage['anvizent.package.link.viewResults'] = '<spring:message code="anvizent.package.link.viewResults"/>';
globalMessage['anvizent.package.button.viewQuery'] = '<spring:message code="anvizent.package.button.viewQuery"/>';
globalMessage['anvizent.package.label.run'] = '<spring:message code="anvizent.package.label.run"/>';
globalMessage['anvizent.package.label.ViewErrorLog'] = '<spring:message code="anvizent.package.label.ViewErrorLog"/>';
globalMessage['anvizent.package.label.joinQuery'] = '<spring:message code="anvizent.package.label.joinQuery"/>';
globalMessage['anvizent.package.label.editQuery'] = '<spring:message code="anvizent.package.label.editQuery"/>';
globalMessage['anvizent.package.label.joinUrls'] = '<spring:message code="anvizent.package.label.joinUrls"/>';
globalMessage['anvizent.package.label.viewX-REFColumnValue'] = '<spring:message code="anvizent.package.label.viewX-REFColumnValue"/>';
globalMessage['anvizent.package.label.autoMergeQueries'] = '<spring:message code="anvizent.package.label.autoMergeQueries"/>';
globalMessage['anvizent.package.label.defualtMappingNotFoundForSelectedIl'] = '<spring:message code="anvizent.package.label.defualtMappingNotFoundForSelectedIl"/>';
globalMessage['anvizent.package.label.pleaseSelectAtLeastoneColumn'] = '<spring:message code="anvizent.package.label.pleaseSelectAtLeastoneColumn"/>';
globalMessage['anvizent.package.label.pleaseChooseAuthenticationType'] = '<spring:message code="anvizent.package.label.pleaseChooseAuthenticationType"/>';
globalMessage['anvizent.package.label.pleaseEnterParamName'] = '<spring:message code="anvizent.package.label.pleaseEnterParamName"/>';
globalMessage['anvizent.package.label.errorLog'] = '<spring:message code="anvizent.package.label.errorLog"/>';
globalMessage['anvizent.package.label.BodyParameters'] = '<spring:message code="anvizent.package.label.BodyParameters"/>';
/* #date:29/11/2017 @mahender */
globalMessage['anvizent.package.label.pleaseSelectdaterange'] = '<spring:message code="anvizent.package.label.pleaseSelectdaterange"/>'; 
globalMessage['anvizent.package.label.pleasechoosestartdate'] = '<spring:message code="anvizent.package.label.pleasechoosestartdate"/>'; 
globalMessage['anvizent.package.label.pleasechooseenddate'] = '<spring:message code="anvizent.package.label.pleasechooseenddate"/>'; 
globalMessage['anvizent.package.message.EnddateshouldbegreaterthanStartdate'] = '<spring:message code="anvizent.package.message.EnddateshouldbegreaterthanStartdate"/>'; 

/* #date:12/12/2017 @mahender */
globalMessage['anvizent.package.label.pleaseentersuburloffsetparamname'] = '<spring:message code="anvizent.package.label.pleaseentersuburloffsetparamname"/>'; 
globalMessage['anvizent.package.label.pleaseentersuburloffsetparamvalue'] = '<spring:message code="anvizent.package.label.pleaseentersuburloffsetparamvalue"/>'; 
globalMessage['anvizent.package.label.pleaseentersuburllimitparamname'] = '<spring:message code="anvizent.package.label.pleaseentersuburllimitparamname"/>'; 
globalMessage['anvizent.package.label.pleaseentersuburllimitparamvalue'] = '<spring:message code="anvizent.package.label.pleaseentersuburllimitparamvalue"/>'; 
globalMessage['anvizent.package.label.pleaseentersuburlfromdateparamname'] = '<spring:message code="anvizent.package.label.pleaseentersuburlfromdateparamname"/>'; 
globalMessage['anvizent.package.label.pleaseentersuburltodateparamname'] = '<spring:message code="anvizent.package.label.pleaseentersuburltodateparamname"/>'; 
globalMessage['anvizent.package.label.pleaseentersuburlstartdate'] = '<spring:message code="anvizent.package.label.pleaseentersuburlstartdate"/>'; 
globalMessage['anvizent.package.label.pleaseentersuburlenddate'] = '<spring:message code="anvizent.package.label.pleaseentersuburlenddate"/>'; 
globalMessage['anvizent.package.label.pleasechoosedaterange'] = '<spring:message code="anvizent.package.label.pleasechoosedaterange"/>'; 
globalMessage['anvizent.package.label.pleaseenternumericvalueexcept0'] = '<spring:message code="anvizent.package.label.pleaseenternumericvalueexcept0"/>';
globalMessage['anvizent.package.label.pleaseenternumericvalue'] = '<spring:message code="anvizent.package.label.pleaseenternumericvalue"/>';
globalMessage['anvizent.package.label.pleaseenterRequestParamValue'] = '<spring:message code="anvizent.package.label.pleaseenterRequestParamValue"/>';
globalMessage['anvizent.package.label.pleasechoosepathparamvaluetype'] = '<spring:message code="anvizent.package.label.pleasechoosepathparamvaluetype"/>';
globalMessage['anvizent.package.label.pleaseenterauthenticationurl'] = '<spring:message code="anvizent.package.label.pleaseenterauthenticationurl"/>';
globalMessage['anvizent.package.label.pleaseenteraccesstokenurl'] = '<spring:message code="anvizent.package.label.pleaseenteraccesstokenurl"/>';
globalMessage['anvizent.package.label.pleaseenterclientIdentifier'] = '<spring:message code="anvizent.package.label.pleaseenterclientIdentifier"/>';
globalMessage['anvizent.package.label.pleaseentercallbackuri'] = '<spring:message code="anvizent.package.label.pleaseentercallbackuri"/>';
globalMessage['anvizent.package.label.pleaseenterclientsecret'] = '<spring:message code="anvizent.package.label.pleaseenterclientsecret"/>';
globalMessage['anvizent.package.label.pleaseentersuburl'] = '<spring:message code="anvizent.package.label.pleaseentersuburl"/>';
globalMessage['anvizent.package.label.Pleaseentersuburlresponseobject'] = '<spring:message code="anvizent.package.label.Pleaseentersuburlresponseobject"/>';
globalMessage['anvizent.package.label.pleasechoosesuburlpaginationtype'] = '<spring:message code="anvizent.package.label.pleasechoosesuburlpaginationtype"/>';
globalMessage['anvizent.package.label.pleasechooseauthenticationtype'] = '<spring:message code="anvizent.package.label.pleasechooseauthenticationtype"/>';
globalMessage['anvizent.package.label.pleaseReplace'] = '<spring:message code="anvizent.package.label.pleaseReplace"/>';
globalMessage['anvizent.package.label.DatabasesOrSchemasInDefaultQuery'] = '<spring:message code="anvizent.package.label.DatabasesOrSchemasInDefaultQuery"/>';
globalMessage['anvizent.package.label.pleaseEnterBaseUrl'] = '<spring:message code="anvizent.package.label.pleaseEnterBaseUrl"/>';
globalMessage['anvizent.package.label.pleaseentervalidurl'] = '<spring:message code="anvizent.package.label.pleaseentervalidurl"/>';
globalMessage['anvizent.package.label.pleaseEnterEndPointUrl'] = '<spring:message code="anvizent.package.label.pleaseEnterEndPointUrl"/>';
globalMessage['anvizent.package.label.pleaseEnterApiDescription'] = '<spring:message code="anvizent.package.label.pleaseEnterApiDescription"/>';
globalMessage['anvizent.package.label.pleaseEnterQuery'] = '<spring:message code="anvizent.package.label.pleaseEnterQuery"/>';

/* #date 12/01/2018 @Vinod */
globalMessage['anvizent.package.label.pleaseEnterValidationName'] = '<spring:message code="anvizent.package.label.pleaseEnterValidationName"/>';
globalMessage['anvizent.package.label.pleaseEnterValidationScripts'] = '<spring:message code="anvizent.package.label.pleaseEnterValidationScripts"/>';
globalMessage['anvizent.package.label.pleaseChoosedls'] = '<spring:message code="anvizent.package.label.pleaseChoosedls"/>';
globalMessage['anvizent.package.label.selectAtleastonescript'] = '<spring:message code="anvizent.package.label.selectAtleastonescript"/>';
globalMessage['anvizent.package.label.pleaseChooseil'] = '<spring:message code="anvizent.package.label.pleaseChooseil"/>';
globalMessage['anvizent.package.label.pleaseChooseconnector'] = '<spring:message code="anvizent.package.label.pleaseChooseconnector"/>';
globalMessage['anvizent.package.label.pleaseChooseValidationtype'] = '<spring:message code="anvizent.package.label.pleaseChooseValidationtype"/>';
globalMessage['anvizent.package.label.selectXref'] = '<spring:message code="anvizent.package.label.selectXref"/>';
globalMessage['anvizent.package.label.unMerge'] = '<spring:message code="anvizent.package.label.unMerge"/>';
globalMessage['anvizent.package.label.select'] = '<spring:message code="anvizent.package.label.select"/>';


globalMessage['anvizent.package.label.noInputlayoutMapped'] = '<spring:message code="anvizent.package.label.noInputlayoutMapped"/>';
globalMessage['anvizent.package.label.ViewResults'] = '<spring:message code="anvizent.package.label.ViewResults"/>';
/* #date 28/03/2018 */
globalMessage['anvizent.package.label.pleaseEnterTagName'] = '<spring:message code="anvizent.package.label.pleaseEnterTagName"/>';
globalMessage['anvizent.package.label.pleaseChoosePropertiesFile'] = '<spring:message code="anvizent.package.label.pleaseChoosePropertiesFile"/>';

globalMessage['anvizent.package.label.selectColumnNames'] = '<spring:message code="anvizent.package.label.selectColumnNames"/>';
globalMessage['anvizent.package.label.enterConditionName'] = '<spring:message code="anvizent.package.label.enterConditionName"/>';
globalMessage['anvizent.package.label.selectApplicableFromDate'] = '<spring:message code="anvizent.package.label.selectApplicableFromDate"/>';
globalMessage['anvizent.package.label.pleaseSelectReferenceColumns'] = '<spring:message code="anvizent.package.label.pleaseSelectReferenceColumns"/>';
globalMessage['anvizent.package.label.pleaseSelectXReferenceColumns'] = '<spring:message code="anvizent.package.label.pleaseSelectXReferenceColumns"/>';
globalMessage['anvizent.package.label.pleaseEnterKeyParam'] = '<spring:message code="anvizent.package.label.pleaseEnterKeyParam"/>'
globalMessage['anvizent.package.label.pleaseentersuburl.hypermedia.pattern'] = '<spring:message code="anvizent.package.label.pleaseentersuburl.hypermedia.pattern"/>'
globalMessage['anvizent.package.label.pleaseentersuburl.hypermedia.record.limit'] = '<spring:message code="anvizent.package.label.pleaseentersuburl.hypermedia.record.limit"/>'
globalMessage['anvizent.package.label.clone'] = '<spring:message code="anvizent.package.label.clone"/>'
globalMessage['anvizent.package.message.packageNameShouldNotBeEmpty'] = '<spring:message code="anvizent.package.message.packageNameShouldNotBeEmpty"/>'
globalMessage['anvizent.package.message.specialCharactersandOnlyAllowed'] = '<spring:message code="anvizent.package.message.specialCharactersandOnlyAllowed"/>'
globalMessage['anvizent.package.label.alreadyMapped'] = '<spring:message code="anvizent.package.label.alreadyMapped"/>'
globalMessage['anvizent.package.label.pleaseEnterValidationDesc'] = '<spring:message code="anvizent.package.label.pleaseEnterValidationDesc"/>'
globalMessage['anvizent.package.message.specialCharacterunderscoreonlyAllowed'] = '<spring:message code="anvizent.package.message.specialCharacterunderscoreonlyAllowed"/>'

globalMessage['anvizent.message.error.text.noResultsFoundToDownload'] = '<spring:message code="anvizent.message.error.text.noResultsFoundToDownload"/>';
globalMessage['anvizent.message.error.text.scriptsExecutionFailed'] = '<spring:message code="anvizent.message.error.text.scriptsExecutionFailed"/>';
globalMessage['anvizent.message.error.text.noscriptIdFound'] = '<spring:message code="anvizent.message.error.text.noscriptIdFound"/>';
globalMessage['anvizent.message.error.text.unableToRetrieveDataValidationInfo'] = '<spring:message code="anvizent.message.error.text.unableToRetrieveDataValidationInfo"/>';
globalMessage['anvizent.message.success.text.scriptsExecutedSuccessfully'] = '<spring:message code="anvizent.message.success.text.scriptsExecutedSuccessfully"/>';
globalMessage['anvizent.message.error.text.unabletodownloadFile'] = '<spring:message code="anvizent.message.error.text.unabletodownloadFile"/>';
globalMessage['anvizent.message.error.text.unableToProcessYourRequest'] = '<spring:message code="anvizent.message.error.text.unableToProcessYourRequest"/>';
globalMessage['anvizent.message.error.text.jarFilesAreNotAvailable'] = '<spring:message code="anvizent.message.error.text.jarFilesAreNotAvailable"/>';
globalMessage['anvizent.package.success.dvJobIsExecuted'] = '<spring:message code="anvizent.package.success.dvJobIsExecuted"/>';
globalMessage['anvizent.package.error.dvJobExecutionFailed'] = '<spring:message code="anvizent.package.error.dvJobExecutionFailed"/>';
globalMessage['anvizent.package.error.dvJobFailed'] = '<spring:message code="anvizent.package.error.dvJobFailed"/>';
globalMessage['anvizent.package.error.jobnotfound'] = '<spring:message code="anvizent.package.error.jobnotfound"/>';
globalMessage['anvizent.package.message.text.pleasesettoselectfilecolumn'] = '<spring:message code="anvizent.package.message.text.pleasesettoselectfilecolumn"/>';
globalMessage['anvizent.package.label.pleaseChooseMinutes'] = '<spring:message code="anvizent.package.label.pleaseChooseMinutes"/>';
globalMessage['anvizent.package.label.pleasechoosepagination'] = '<spring:message code="anvizent.package.label.pleasechoosepagination"/>';

globalMessage['anvizent.package.label.pleaseEnterParamName'] = '<spring:message code="anvizent.package.label.pleaseEnterParamName"/>';
globalMessage['anvizent.package.label.pleaseEnterParamValue'] = '<spring:message code="anvizent.package.label.pleaseEnterParamValue"/>';
globalMessage['anvizent.package.label.pleaseSelectBusinessModal'] = '<spring:message code="anvizent.package.label.pleaseSelectBusinessModal"/>';
globalMessage['anvizent.package.label.pleaseSelectAiModel'] = '<spring:message code="anvizent.package.label.pleaseSelectAiModel"/>';
globalMessage['anvizent.package.label.pleaseSelectStagingTable'] = '<spring:message code="anvizent.package.label.pleaseSelectStagingTable"/>';
globalMessage['anvizent.package.label.pleaseSelectGenericContextParams'] = '<spring:message code="anvizent.package.label.pleaseSelectGenericContextParams"/>';

globalMessage['anvizent.package.label.pleaseChooseRFile'] = '<spring:message code="anvizent.package.label.pleaseChooseRFile"/>';
globalMessage['anvizent.package.label.pleaseEnterBusinessProblem'] = '<spring:message code="anvizent.package.label.pleaseEnterBusinessProblem"/>';
globalMessage['anvizent.package.label.pleaseSelectModelName'] = '<spring:message code="anvizent.package.label.pleaseSelectModelName"/>';
globalMessage['anvizent.package.label.pleaseEnterStagingTableScript'] = '<spring:message code="anvizent.package.label.pleaseEnterStagingTableScript"/>';
globalMessage['anvizent.package.label.pleaseEnterAlTableScript'] = '<spring:message code="anvizent.package.label.pleaseEnterAlTableScript"/>';
globalMessage['anvizent.package.label.pleaseEnterOlTableScript'] = '<spring:message code="anvizent.package.label.pleaseEnterOlTableScript"/>';
globalMessage['anvizent.package.label.tableScriptTablesAreDifferent'] = '<spring:message code="anvizent.package.label.tableScriptTablesAreDifferent"/>';
globalMessage['anvizent.package.label.pleaseEnterModelName'] = '<spring:message code="anvizent.package.label.pleaseEnterModelName"/>';
globalMessage['anvizent.package.label.pleaseEnterSourceQuery'] = '<spring:message code="anvizent.package.label.pleaseEnterSourceQuery"/>';
globalMessage['anvizent.package.button.viewExecutionComments'] = '<spring:message code="anvizent.package.button.viewExecutionComments"/>';
globalMessage['anvizent.package.label.allowedfilesizeexceededmax5mb'] = '<spring:message code="anvizent.package.label.allowedfilesizeexceededmax5mb"/>';
globalMessage['anvizent.package.label.pleasechoosepemfile'] = '<spring:message code="anvizent.package.label.pleasechoosepemfile"/>';
globalMessage['anvizent.package.label.dbvariablekeystartwithandendswith'] = '<spring:message code="anvizent.package.label.dbvariablekeystartwithandendswith"/>';

</script> 
</c:if>