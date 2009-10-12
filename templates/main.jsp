<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.opencms.org/taglib/cms" prefix="cms"%>

<%@page import="com.mashfrog.backoffice.CmsBackofficeActionElement"%>
<%@page import="com.mashfrog.backoffice.actions.RedirectingAction"%>
<%
    CmsBackofficeActionElement cms = new CmsBackofficeActionElement(pageContext, request, response);
    request.setAttribute("cms", cms);
    
    if(cms.getCurrentAction() instanceof RedirectingAction && ((RedirectingAction)cms.getCurrentAction()).sendRedirect()){
        // The action must send a redirect before writing any content
        ((RedirectingAction)cms.getCurrentAction()).redirect();
    }
    else if(!cms.getCurrentAction().hasBody()){
    	cms.include(cms.getResultJsp());
    }
    else{
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <title>${cms.backofficeProject.description}</title>

    <c:set var="rendering" value="${cms.backofficeProject.rendering}"/>
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
<%  }%>