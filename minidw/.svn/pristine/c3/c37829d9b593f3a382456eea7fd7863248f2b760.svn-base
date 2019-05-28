<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="left-area nopad">
<div class="toggle-panel">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </div>
    <nav class="navbar navbar-default navbar-static-top">
         <div class="navbar-header">
	          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">   <span class="sr-only">Toggle navigation</span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </button>
	       </div> 
         <div id="navbar">
         <ul class="nav navbar-nav dev-page-navigation">
           		
           		<c:if test="${principal.roleId == -300}">
           		
           				<li class="navLeftTabLink ${activePage == 'scheduler' ? ' activemenu_link':'' }" >
                           <a class="startLoader" href="<c:url value="/sadmin"/>">
                            	<span class="allmappingicon icon_sprite" aria-hidden="true"></span>
                           Schedulers</a></li>
           		</c:if>
           		
           		
               <%--  UI Changes --%> 
               	<c:if test="${principal.roleId == -200 || principal.roleId == -400 || principal.roleId == 1}">
                           <c:if test="${principal.roleId == -200 || principal.roleId == -400 }">
                           <li class="navLeftTabLink ${activePage == 'clientchange' ? ' activemenu_link':'' }" >
                           <a class="startLoader" href="<c:url value="/admin/clientchange"/>">
                            	<span class="homeicon icon_sprite" aria-hidden="true"></span>
                             Change Client ( ${ clientId == '-1'?'Master':clientId } ) </a>
                           </li>
                           </c:if>
                           
                            <c:if test="${ clientId == '-1' }">
                            	<li class="navLeftTabLink ${activePage == 'ETLAdmin' ? ' activemenu_link':'' }" >
	                           <a class="startLoader" href="<c:url value="/admin/ETLAdmin"/>">
	                            	<span class="homeicon icon_sprite" aria-hidden="true"></span>
	                           <spring:message code="anvizent.navLeftTabLink.label.home"/></a></li>
                           </c:if>
                           
                           <c:if test="${ clientId != '-1'}">
                          		<li class="navLeftTabLink ${activePage == 'ETLAdminClient' ? ' activemenu_link':'' }" >
	                          	<a class="startLoader" href="<c:url value="/admin/ETLAdminClient"/>">
                            	<span class="homeicon icon_sprite" aria-hidden="true"></span>
                         	  <spring:message code="anvizent.navLeftTabLink.label.home"/></a></li>
                   		   </c:if>
                		  
                   
                           <li class="navLeftTabLink ${activePage == 'allMappingInfo' ? ' activemenu_link':'' }${clientId == '-1' ? ' hidden':'' }" >
                           <a class="startLoader" href="<c:url value="/admin/allMappingInfo"/>">
                            	<span class="allmappingicon icon_sprite" aria-hidden="true"></span>
                           <spring:message code="anvizent.navLeftTabLink.label.mappingInfo"/></a></li>
                     
                    
                           <li class="navLeftTabLink ${activePage == 'defaultTemplates' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }" >
                           <a class="startLoader" href="<c:url value="/admin/defaultTemplates"/>">
                            	<span class="defaultmappingicon icon_sprite" aria-hidden="true"></span>
                           <spring:message code="anvizent.navLeftTabLink.label.defaultMappingConfiguration"/></a></li>
                   
                 
                  <li>
		              	<a class="navLeftTabLink activep  dropdown-toggle" data-toggle="dropdown">
		              		<span class="verticalicon icon_sprite" aria-hidden="true"></span>
		              		<span><spring:message code="anvizent.navLeftTabLink.label.master"/></span>
		              	</a> 
	               		<ul class="navLeft_submenu" role="menu">
	               			<%-- <li class="navLeftTabLink ${activePage == 'serverConfiguration' ? ' activemenu_link':'' }" ><a href="<c:url value="/admin/serverConfigurations"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Server Configuration</a></li> --%>
                            <li class="navLeftTabLink ${activePage == 'vertical' ? ' activemenu_link':'' }" ><a href="<c:url value="/admin/vertical"/>"><i class="startLoader" class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.vertical"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'clientTableScripts' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }" ><a class="startLoader" href="<c:url value="/admin/clientTableScripts"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.tablescripts"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'database' ? ' activemenu_link':'' }" ><a class="startLoader" href="<c:url value="/admin/database"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.database"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'connector' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/connector"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.connector"/></a></li>
                           	<li class="navLeftTabLink ${activePage == 'kpi' ? ' activemenu_link':'' }"><a  href="<c:url value="/admin/kpi"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.kPI"/></a></li>
                           	<li class="navLeftTabLink ${activePage == 'iLInfo' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/iLInfo"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.iLInfo"/></a></li>
                           	<li class="navLeftTabLink ${activePage == 'defaultQueries' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/defaultQueries"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.ilPreBuildQueries"/></a></li>
                           	<li class="navLeftTabLink ${activePage == 'dashBoardLayoutMaster' ? ' activemenu_link':'' }" ><a class="startLoader" href="<c:url value="/admin/dashBoardLayoutMaster"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.dLInfo"/></a></li>
                           	
                           	<%-- <li class="navLeftTabLink "><a  href="<c:url value="/admin/contextparameters"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.createOrViewContextParameter"/></a></li> --%>
                      	</ul>
                  </li>
	              
                  <li class="${clientId == '-1' ? ' hidden':'' }">
		              	<a class="navLeftTabLink activep  dropdown-toggle" data-toggle="dropdown">
		              		<span class="tablescriptsicon icon_sprite" aria-hidden="true"></span>
		              		<span><spring:message code="anvizent.navLeftTabLink.label.clientSettings"/></span>
		              	</a> 
	               		<ul class="navLeft_submenu" role="menu">
	               		 	<%-- <li class="navLeftTabLink ${activePage == 'verticalclientMapping' ? ' activemenu_link':'' }"><a  href="<c:url value="/admin/vertical/clientMapping"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.clientVerticalMapping"/></a></li> --%>
	               		 	<li class="navLeftTabLink ${activePage == 'clientTableScriptsmapping' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/clientTableScripts/mapping"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.executeScripts"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'instantExecution' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/clientTableScripts/instantExecution"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.instantExecution"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'connectorclientMapping' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/connector/clientMapping"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.clientConnectorMapping"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'dashBoardLayoutMasterclientMapping' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/dashBoardLayoutMaster/clientMapping"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.clientDLMapping"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'specificIL' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/iLInfo/clientMapping/specificIL"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.clientSpecificIL"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'specificDL' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/dashBoardLayoutMaster/clientMapping/specificDL"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.clientSpecificDL"/></a></li>
                            <%-- <li class="navLeftTabLink ${activePage == 'generalSettings' ? ' activemenu_link':'' }"><a  href="<c:url value="/admin/generalSettings"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.generalSettings"/></a></li> --%>
                            <%-- <li class="navLeftTabLink ${activePage == 'clientConfigurationSettings' ? ' activemenu_link':'' }"><a  href="<c:url value="/admin/clientConfigurationSettings"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.clientConfigurationSettings"/></a></li> --%>
                      		<%-- <li class="navLeftTabLink ${activePage == 'migrateTemplate' ? ' activemenu_link':'' }"><a  href="<c:url value="/admin/migrateTemplate"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.templateMigration"/></a></li>--%>
                      	    <%-- <li class="navLeftTabLink ${activePage == 'templateMigration' ? ' activemenu_link':'' }"><a  href="<c:url value="/admin/templateMigration"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.templateMigration"/></a></li> --%>
                      		<li class="navLeftTabLink ${activePage == 'clientJobExecutionParameters' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/clientJobExecutionParameters"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Client Job Execution Parameters</a></li>
                      		<li class="navLeftTabLink ${activePage == 'fileSettings' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/fileSettings"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.FileSettings"/></a></li>
                      		
                      	</ul>
                  </li>
                  <li>
	              	<a class="navLeftTabLink activep  dropdown-toggle" data-toggle="dropdown">
	              	<span class="webserviceicon icon_sprite" aria-hidden="true"></span>
	              	<span><spring:message code="anvizent.navLeftTabLink.label.webService"/></span></a> 
	               		<ul class="navLeft_submenu" role="menu">
                      		<li class="navLeftTabLink ${activePage == 'webServiceTemplate' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/webservice/webServiceTemplate"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.webServiceTemplate"/></a></li>
	               		 	<li class="navLeftTabLink ${activePage == 'webServiceILMapping' ? ' activemenu_link':'' }" ><a class="startLoader" href="<c:url value="/admin/webServiceILMapping"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.admin.label.webServiceILMapping"/></a></li>
                          	<li class="navLeftTabLink ${activePage == 'clientWebserviceMapping' ? ' activemenu_link':'' }${clientId == '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/clientWebserviceMapping"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.clientWebServiceMapping"/></a></li>
                      	</ul>
                  </li>

				<li>
	              	<a class="navLeftTabLink activep  dropdown-toggle" data-toggle="dropdown">
	              	<span class="datavalidationicon icon_sprite" aria-hidden="true"></span>
	              	<span><spring:message code="anvizent.navLeftTabLink.label.datavalidation"/></span></a> 
	               		<ul class="navLeft_submenu" role="menu">
	               		    <li class="navLeftTabLink startLoader${activePage == 'dataValidationType' ? ' activemenu_link':'' }" ><a href="<c:url value="/admin/adminDataValidation/getDataValidationType"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.dataValidationType"/></a></li>
	               		    <li class="navLeftTabLink startLoader${activePage == 'addPreloadValidation' ? ' activemenu_link':'' }" ><a href="<c:url value="/admin/adminDataValidation/getPreloadValidations"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.preload"/></a></li>
                      		<li class="navLeftTabLink startLoader${activePage == 'addBusinessCaseValidation' ? ' activemenu_link':'' }" ><a href="<c:url value="/admin/adminDataValidation/getBusinessCasesValidation"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.businesscases"/></a></li>
                      	</ul>
                  </li>
                  
	              <li>
	              	<a class="navLeftTabLink activep  dropdown-toggle" data-toggle="dropdown">
	              	<span class="logsicon icon_sprite" aria-hidden="true"></span>
	              	<span><spring:message code="anvizent.navLeftTabLink.label.logs"/></span></a> 
	               		<ul class="navLeft_submenu" role="menu">
                            <li class="navLeftTabLink ${activePage == 'errorLogs' ? ' activemenu_link':'' }" ><a class="startLoader" href="<c:url value="/admin/errorLogs"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.errorlogs"/></a></li>
                      	</ul>
                  </li>
                  
	              <li>
	              	<a class="navLeftTabLink activep  dropdown-toggle" data-toggle="dropdown">
	              	<span class="miscellaneousicon icon_sprite" aria-hidden="true"></span>
	              	<span>ELT Configurations</span></a> 
	               		<ul class="navLeft_submenu" role="menu">
                      		<li class="navLeftTabLink ${activePage == 'eltStgKeyConfig' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/elt/eltConfigTags"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.configTags"/></a></li>
                      		<li class="navLeftTabLink ${activePage == 'eltJobTags' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/elt/jobtag/eltJobTags"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.jobTags"/></a></li>
                      		<li class="navLeftTabLink ${activePage == 'eltMasterConfiguration' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/elt/masterconfig/eltMasterConfiguration"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.masterConfiguration"/></a></li>
                      		<li class="navLeftTabLink ${activePage == 'eltLoadParameters' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/elt/loadParameters/eltLoadParameters"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.loadParameters"/></a></li>
                      	</ul>
                  </li>
                  
                   <li>
	              	<a class="navLeftTabLink activep  dropdown-toggle" data-toggle="dropdown">
	              	<span class="miscellaneousicon icon_sprite" aria-hidden="true"></span>
	              	<span><spring:message code="anvizent.navLeftTabLink.label.miscellaneous"/></span></a> 
	               		<ul class="navLeft_submenu" role="menu">
	               		<li class="navLeftTabLink ${activePage == 'commonJob' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/commonJob"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.commonJob"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'existingJars' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/admin/existingJars"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.existingjars"/></a></li>
                      		<li class="navLeftTabLink ${activePage == 'currencyIntegration' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/currencyIntegration"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Currency Integration in Mini-DW</a></li>
                      		<li class="navLeftTabLink ${activePage == 'clientCurrencyMapping' ? ' activemenu_link':'' }${clientId == '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/currencyIntegration/clientCurrencyMapping"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Client Currency Mapping</a></li>
                      		<c:if test="${principal.roleId != -400 }">
                      	    <li class="navLeftTabLink ${activePage == 'versionUpgrade' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/versionUpgrade"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Version Upgrade</a></li>
                      		</c:if>
                      		<li class="navLeftTabLink ${activePage == 'internalization' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/internalization"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Internalization</a>		</li>
                      	    <c:if test="${principal.roleId != -400 }">
                      		<li class="navLeftTabLink ${activePage == 's3BucketInfo' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/s3BucketInfo"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>S3Bucket Info</a>		</li>
                      		</c:if>
                      		<li class="navLeftTabLink ${activePage == 'clientS3Mapping' ? ' activemenu_link':'' }${clientId == '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/clientS3Mapping"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>client S3Bucket Mapping</a>		</li>
                      		<li class="navLeftTabLink ${activePage == 'webServiceAuthParamsEncryption' ? ' activemenu_link':'' }${clientId == '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/webServiceAuthParamsEncryption"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Web service Auth Params Encyption</a>		</li>
                      		<c:if test="${principal.roleId != -400 }">
                      		<li class="navLeftTabLink ${activePage == 'middleLevelManager' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/middleLevelManager"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Middle Level Manager</a>		</li>
                      		<li class="navLeftTabLink ${activePage == 'hybridClientsGrouping' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/hybridClientsGrouping"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Hybrid Clients Grouping</a>		</li>
                      		<li class="navLeftTabLink ${activePage == 'appDbVersionTableScripts' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/appDbVersionTableScripts"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>App DB Version Table Scripts</a>		</li>
                      		<li class="navLeftTabLink ${activePage == 'multiClientTableScriptExecution' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/clientsInstantScriptExecution"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.multiClientTableScriptExecution"/></a></li>
                      		</c:if>
                      		<li class="navLeftTabLink ${activePage == 'multiClientInsertScriptExecution' ? ' activemenu_link':'' }${clientId != '-1' ? ' hidden':'' }"><a class="startLoader"  href="<c:url value="/admin/multiClientScriptExecution"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Multi Client Insert Query Execution</a></li>
                      	</ul>
                  </li>
                   
	            </c:if>
	            <c:if test="${principal.roleId == 7}">
	            <c:if test="${appType == '1' || appType == '0'}">
	              	<li>
	              	<a class="navLeftTabLink activep  dropdown-toggle" data-toggle="dropdown">
	              	<span class="datasetsicon icon_sprite" aria-hidden="true"></span>
	              	<span><spring:message code="anvizent.navLeftTabLink.label.dataSets"/></span></a> 
	               	<ul class="navLeft_submenu" role="menu">
                            <li class="activepage ${activePage == 'standardpackage' ? ' activemenu_link':'' }" ><a class="startLoader" href="<c:url value="/adt/standardpackage"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.standardPackage"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'custompackage' ? ' activemenu_link':'' }"><a class="startLoader" href="<c:url value="/adt/package/custompackage"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.customPackage"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'ddLayout' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/adt/package/ddLayout"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.customDataSets"/></a></li>
                            <%-- <li class="activepage ${activePage == 'crossReference' ? ' activemenu_link':'' }" ><a class="startLoader" href="<c:url value="/adt/crossReference/getIlList"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.xReference"/></a></li> --%>
                            <li class="activepage ${activePage == 'crossReference' ? ' activemenu_link':'' }" ><a class="startLoader" href="<c:url value="/adt/crossReference"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.xReference"/></a></li>
                          	<li class="isactivepage ${activePage == 'archivedPackages' ? ' activemenu_link':'' }" ><a class="startLoader" href="<c:url value="/adt/package/archivedPackages"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.archivedPackages"/></a></li>
                          	<c:if test="${principal.isTrailUser == false}">
                            	<li class="navLeftTabLink ${activePage == 'purge' ? ' activemenu_link':'' }"><a class="startLoader" href="<c:url value="/adt/package/purge"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.purge"/></a></li>
                            </c:if>
                            <li class="navLeftTabLink ${activePage == 'databaseConnection' ? ' activemenu_link':'' }"><a class="startLoader" href="<c:url value="/adt/package/databaseConnection"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.databaseConnection"/></a></li>
                     		<li class="navLeftTabLink ${activePage == 'webServiceConnection' ? ' activemenu_link':'' }"><a class="startLoader" href="<c:url value="/adt/package/webServiceConnection"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.webseviceConnection"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'historicalLoad' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/adt/package/historicalLoad"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.admin.button.historicalload"/></a></li>
                            <li class="navLeftTabLink ${activePage == 'currencyNode' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/adt/currencyLoad"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.admin.button.currencyLoad"/></a></li>
							<%-- <li class="navLeftTabLink ${activePage == 'hierarchicalData' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/adt/hierarchical"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.admin.button.hierarchicalData"/></a></li> --%>
                     		<%-- <li class="navLeftTabLink ${activePage == 'apisData' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/adt/package/apisDataInfo"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Data APIs</a></li> --%>
                     		<%-- <li class="navLeftTabLink"><a href="<%= session.getAttribute("ELTURL") %>" target="_blank" ><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.ELT"/></a></li> --%>
                     </ul>  
	               </li>
	               <li>
	              	<a class="navLeftTabLink activep  dropdown-toggle" data-toggle="dropdown">
	              	<span class="datavalidationicon icon_sprite" aria-hidden="true"></span>
	              	<span><spring:message code="anvizent.navLeftTabLink.label.datavalidation"/></span></a> 
	               		<ul class="navLeft_submenu" role="menu">
	               		    <li class="navLeftTabLink startLoader${activePage == 'validatePreload' ? ' activemenu_link':'' }" ><a href="<c:url value="/adt/package/dataValidation/preloadInfo"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.preload"/></a></li>
                      		<li class="navLeftTabLink startLoader${activePage == 'validateBusinessCases' ? ' activemenu_link':'' }" ><a href="<c:url value="/adt/package/dataValidation/businessCasesInfo"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.businesscases"/></a></li>
                      	</ul>
                  </li>
                  </c:if>
                  <c:if test="${appType == '2' || appType == '0'}">
                  <li>
	              	<a class="navLeftTabLink activep  dropdown-toggle" data-toggle="dropdown">
	              	<span class="webserviceicon icon_sprite" aria-hidden="true"></span>
	              	<span><spring:message code="anvizent.navLeftTabLink.label.ai"/></span></a> 
	               		<ul class="navLeft_submenu" role="menu">
	               		   	<li class="navLeftTabLink ${activePage == 'aiContextParameters' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/adt/package/ai/aiContextParameters"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Context Parameters</a></li>
	               	     	<li class="navLeftTabLink ${activePage == 'aiCommonJobs' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/adt/package/ai/aiCommonJobs"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Common Jobs</a></li>
                      		<li class="navLeftTabLink ${activePage == 'aiModel' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/adt/package/ai/aiModeList"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Model</a></li>
                      		<li class="navLeftTabLink ${activePage == 'businessModel' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/adt/package/ai/businessModels"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Business Problem</a></li>
                      		<li class="navLeftTabLink ${activePage == 'aiSourceDefinition' ? ' activemenu_link':'' }" ><a class="startLoader" href="<c:url value="/adt/package/ai/aiSourceDefinitionList"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.aiSourceDefinition"/></a></li>
                      		<li class="navLeftTabLink ${activePage == 'aiJobsUpload' ? ' activemenu_link':'' }"><a class="startLoader"  href="<c:url value="/adt/package/ai/aiJobsUpload"/>"><i class="fa fa-angle-right" aria-hidden="true"></i><spring:message code="anvizent.navLeftTabLink.label.AISourceUpload"/></a></li>
                      		
                      	</ul>
                  </li>
                  </c:if>  
                  <c:if test="${appType == '1' || appType == '0'}">
	              <li>
	              	<a class="navLeftTabLink ${activePage == 'schedule' ? ' activemenu_link':'' }"  href="<c:url value="/adt/package/schedule"/>">
	              	<span class="scheduleicon icon_sprite" aria-hidden="true"></span> 
	              	<span><spring:message code="anvizent.navLeftTabLink.label.schedule"/></span>
	              	</a>
	              </li>
	              </c:if>
	              <%-- <c:if test="${isWebApp == false}">
	              	<li class="navLeftTabLink ${activePage == 'scheduleStatus' ? ' activemenu_link':'' }"><a  href="<c:url value="/adt/package/scheduleStatus"/>"><i class="fa fa-angle-right" aria-hidden="true"></i>Schedule Status</a></li>
	              </c:if> --%>
	            </c:if> 
         </ul>
        </div><!--/.nav-collapse -->
	</nav>
</div>

