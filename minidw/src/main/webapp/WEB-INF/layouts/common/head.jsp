<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="Anvizent">
<c:if test="${not empty _csrf}">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/> <!-- default header name is X-CSRF-TOKEN -->
</c:if>
<c:if test="${appType == '1' || appType == '0'}">
<title>MINI-DW</title>
</c:if>
<c:if test="${appType == '2'}">
<title>Artificial intelligence</title>
</c:if>
<link rel="icon" type="image/png" href="<c:url value="/resources/images/anvizent_icon.png"/>" style="width:16px; height: 16px">

<link href="<c:url value="/resources/css/jquery-ui-1.10.3.custom.min.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" />	
<link href="<c:url value="/resources/css/dataTables.bootstrap.css"/>" rel="stylesheet" />
<link href="<c:url value="/resources/font-awesome-4.4.0/css/font-awesome.min.css"/>" type="text/css"  rel="stylesheet"/>
<link href='https://fonts.googleapis.com/css?family=Source+Sans+Pro' rel='stylesheet' type='text/css'>
 <link href="<c:url value="/resources/css/custom.css?${build}"/>" rel="stylesheet">
 <link href="<c:url value="/resources/css/select2.min.css"/>" rel="stylesheet">
 <link href="<c:url value="/resources/css/bootstrap-multiselect.css"/>" rel="stylesheet">
 <link href="<c:url value="/resources/css/multiple-select.css"/>" rel="stylesheet">
