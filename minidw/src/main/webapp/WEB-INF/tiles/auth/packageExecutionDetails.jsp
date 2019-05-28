<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="utf-8">
<title>MINI-DW</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js" language="javascript"></script>
<style type="text/css">
body {
	font-family: 'Open Sans', sans-serif;
}
.package-upload .form-group {
	overflow: auto;
	background: #fbfbfb;
	border: 1px solid #f7f5f5;
}
.brand-logo {
	padding: 3px 15px;
}
.package-upload label {
	font-weight: normal;
	margin: 0;
	vertical-align: baseline;
	line-height: 28px;
}
.package-upload .panel {
	margin-top: 50px;
}
.package-upload .panel-heading {
	font-size: 17px;
	font-weight: bold;
}
.no-pad {
	padding: 0!important;
}
</style>
<script type="text/javascript">
$(document).ready( function () {
	 var messagecode = '${messagecode}';
	 if(messagecode == "SUCCESS"){
		 $("#executiondtldiv").show();
	 }else{
		 $("#executiondtldiv").hide();
	 }
	});
</script>
</head>

<body>
<nav class="navbar navbar-default navigation-clean-button" style="margin-bottom:0;">
  <div class="container">
    <div class="navbar-header"><a class="navbar-brand brand-logo" href="#"><img src="/minidw/resources/images/anvizent-logo.png" alt="Anvizent" title="Anvizent"></a>
      <button class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navcol-1"><span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button>
    </div>
  </div>
</nav>
<div id ="pageErrors">
	
	<c:if test="${not empty errors}">
		<c:choose>
			<c:when test="${not empty messagecode}">
				<c:if test="${messagecode == 'FAILED' || messagecode == 'ERROR'}">
					<div class="alert alert-danger alert-dismissible" role="alert">
					<button type="button" class="close" data-dismiss="alert">
			        <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		   	         </button>
	    	        <div>${errors}</div>
					</div>
				</c:if>
			</c:when>
			<c:otherwise>
				
			</c:otherwise>
		</c:choose>
			
	    	</c:if>
		</div>
<div class="container package-upload" id="executiondtldiv" style="display:none">
  <div class="panel panel-default">
    <div class="panel-heading"> Package Execution Details </div>
    <div class="panel-body">
      <form>
        <div class="col-md-6">
          <div class="form-group">
            <label for="" class="col-md-5"> <b> Name: </b></label>
            <div class="col-md-7">
              <label for="">${packageExecution.userPackage.packageName}</label>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group">
            <label for="" class="col-md-5" > <b>Type:</b></label>
            <div class="col-md-7">
              <label for="">${packageExecution.userPackage.isStandard=='true'?'Standard':'Custom'}</label>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group">
            <label for="" class="col-md-5"><b>No of Sources:</b></label>
            <div class="col-md-7">
              <label for="">${packageExecution.userPackage.sourceCount}</label>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group">
            <label for="" class="col-md-5"><b>No of increamental loads:</b></label>
            <div class="col-md-7">
              <label for="">${packageExecution.incrementalLoadCount}</label>
            </div>
          </div>
        </div>
       <!--  <div class="col-md-6">
          <div class="form-group">
            <label for="" class="col-md-5">No of normal loads:</label>
            <div class="col-md-7">
              <label for="">No. of normal loads:</label>
            </div>
          </div>
        </div> -->
        <div class="clearfix"></div>
        <div class="col-md-6">
          <div class="form-group">
            <label for="" class="col-md-5"><b>S3 upload status:</b></label>
            <div class="col-md-7">
              <label for="">${packageExecution.uploadStatus}</label>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group">
            <label for="" class="col-md-5"><b>S3 upload Time:</b></label>
            <div class="col-md-7">
              <div class="col-md-4 no-pad">
                <label for="">Start Date:</label>
                <label for="">End Date:</label>
              </div>
              <div class="col-md-7 no-pad">
                <label for=""> ${packageExecution.uploadStartDate}</label>
                <label for=""> ${packageExecution.lastUploadedDate}</label>
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group">
            <label for="" class="col-md-5"><b>Execution status:</b></label>
            <div class="col-md-7">
              <label for="">${packageExecution.executionStatus}</label>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group">
            <label for="" class="col-md-5"><b>Execution Time:</b></label>
            <div class="col-md-7">
              <div class="col-md-4 no-pad">
                <label for="">Start Date:</label>
                <label for=""> End Date:</label>
              </div>
              <div class="col-md-7 no-pad">
                <label for=""> ${packageExecution.executionStartDate}</label>
                <label for=""> ${packageExecution.lastExecutedDate}</label>
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group">
            <label for="" class="col-md-5"><b>Druid status:</b></label>
            <div class="col-md-7">
              <label for="">${packageExecution.druidStatus}</label>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="form-group">
            <label for="" class="col-md-5"><b>Druid Time:</b></label>
            <div class="col-md-7">
              <div class="col-md-4 no-pad">
                <label for="">Start Date:</label>
                <label for=""> End Date:</label>
              </div>
              <div class="col-md-7 no-pad">
                <label for=""> ${packageExecution.druidStartDate}</label>
                <label for=""> ${packageExecution.druidEndDate}</label>
              </div>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading"> Comments </div>
    <div class="panel-body">
      <label for="" class="col-md-12"><b> Upload comments: </b></label>
      <div class=" col-md-12">
        <textarea class="form-control" rows="10" id="comment">${packageExecution.uploadComments}</textarea>
      </div>
    </div>
    <div class="panel-body">
      <label for="" class="col-md-12"> <b>Execution comments:</b></label>
      <div class=" col-md-12">
        <textarea class="form-control" rows="10" id="comment">${packageExecution.executionComments}</textarea>
      </div>
    </div>
    <div class="panel-body">
      <label for="" class="col-md-12"><b> Druid comments:</b></label>
      <div class=" col-md-12">
        <textarea class="form-control" rows="10" id="comment">${packageExecution.druidComments}</textarea>
      </div>
    </div>
  </div>
</div>
</body>
</html>
