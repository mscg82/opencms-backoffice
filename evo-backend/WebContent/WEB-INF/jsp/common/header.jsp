<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<head>
    <title><s:property value="title" escape="true"/></title>

    <c:set var="themeBean" value="${themeInfo}" />
    <c:forEach items="${themeBean.css}" var="css">
        <link rel="stylesheet" type="text/css" href="${css.sourceFile}" />
    </c:forEach>
    
    <c:forEach items="${themeBean.js}" var="js">
        <script type="text/javascript" src="${js.sourceFile}"></script>
    </c:forEach>
</head>