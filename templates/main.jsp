<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.opencms.org/taglib/cms" prefix="cms"%>

<%@page import="com.mashfrog.backoffice.CmsBackofficeActionElement"%>
<%
    CmsBackofficeActionElement cms = new CmsBackofficeActionElement(pageContext, request, response);
    request.setAttribute("cms", cms);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <c:set var="rendering" value="${cms.backofficeProject.rendering.cssList}"/>
    <!--[if IE 7]>
        <style type="text/css">#menu { margin-left: 1px; }  </style>
    <![endif]-->
    <!--[if lte IE 7]>
        <style type="text/css">
            @import url('<cms:link>/system/modules/${cms.module.name}/resources/common/css/tabs-ie.css</cms:link>');
        </style>
        <script type="text/javascript" src="<cms:link>/system/modules/${cms.module.name}/resources/common/js/dropdown.js</cms:link>"></script>
    <![endif]-->
    
    <!-- CSS LIST -->
    <c:forEach var="css" items="${rendering.cssList}">
        <link href="<cms:link>${css}</cms:link>" rel="stylesheet" type="text/css" />
    </c:forEach>
    
    <!-- JavaScript LIST -->
    <c:forEach var="js" items="${rendering.javascriptList}">
        <script type="text/javascript" src="<cms:link>${js}</cms:link>"></script>
    </c:forEach>
</head>
<body>
	<div id="wrap">
	    <cms:include file="${cms.resultJsp}"></cms:include>   
	</div>
</body>
</html>