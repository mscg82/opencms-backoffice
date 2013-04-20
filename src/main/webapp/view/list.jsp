<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.mscg.dyndns.servlet.IPReadServlet"%>
<%
    Boolean logged = (Boolean)session.getAttribute(IPReadServlet.SessionParameters.loggedUserParam);
    if(logged == null || !logged) {
    	String url = request.getRequestURI() +
    	    (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        session.setAttribute(IPReadServlet.SessionParameters.lastCalledPage, url);
        response.sendRedirect("login.jsp");
        return;
    }
    IPReadServlet serv = new IPReadServlet(pageContext, request, response);
    pageContext.setAttribute("serv", serv);
%>
<html>
<head>
    <title>MscG's Dynamic DNS</title>
</head>
<body>
    Service <b>${serv.service}</b>
    <ul>
        <c:forEach var="url" items="${serv.urls}">
            <li><a href="${url}">${url}</a></li>
        </c:forEach>
    </ul>
</body>
</html>