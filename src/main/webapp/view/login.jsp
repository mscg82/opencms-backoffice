<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>
<%@ page import="com.mscg.config.ConfigLoader"%>
<%@ page import="com.mscg.dyndns.servlet.IPReadServlet"%>
<%
    pageContext.setAttribute("errorMessage", null);

    String username = request.getParameter("username");
    String password = request.getParameter("password");

    String redirPage = (String)session.getAttribute(IPReadServlet.SessionParameters.lastCalledPage);
    if(redirPage == null) redirPage = "index.jsp";
    
    Boolean logged = (Boolean)session.getAttribute(IPReadServlet.SessionParameters.loggedUserParam);
    if(logged != null && logged) {
    	response.sendRedirect(redirPage);
        return;
    }
    
    if(username != null || password != null) {
        List<String> users = ConfigLoader.getParameterAsList("users.username");
        List<String> passwords = ConfigLoader.getParameterAsList("users.password");
        
        try {
	        Iterator<String> uIt = users.iterator();
	        Iterator<String> pIt = passwords.iterator();
	        while(uIt.hasNext()) {
	        	String uname = uIt.next();
	        	String pwd = pIt.next();
	        	if(uname.equals(username) && pwd.equals(password)) {
	        		session.setAttribute(IPReadServlet.SessionParameters.loggedUserParam, Boolean.TRUE);
	        		response.sendRedirect(redirPage);
	        		return;
	        	}
	        }
	        pageContext.setAttribute("errorMessage", "Invalid username and/or password");
        } catch(Exception e) {
        	pageContext.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
        }
        
    }
%>

<html>
<head>
    <title>MscG's Dynamic DNS</title>
    <style type="text/css">
        .header {
            font-weight: bold;
            margin: 10px 10px 10px 0px;
        }
        
        .form {
        }
        
        .form select {
            width: 100%;
        }
        
        .errorMessage {
            border: 2px solid #BB0000;
            background-color: #DD0000;
            padding: 0.5em;
            margin: 0em 0.5em 0.5em 0.5em;
            display: inline;
            font-weight: bold;
            font-size: 15pt;
            color: #FFFFFF;
        }
        
        .clear {
            clear: both;
        }
    </style>
</head>
<body>
    <div class="header">
        This is the login page. Please provide valid credentials to see
        reserved informations.
    </div>
    
    <c:if test="${not empty errorMessage}">
        <div class="clear">&nbsp;</div>
        <div class="errorMessage">
            ${errorMessage}
        </div>
        <div class="clear">&nbsp;</div>
    </c:if>
    
    <div class="form">
        <form action="login.jsp" method="post">
            <table>
                <tr>
                    <td><label for="username">Username:</label></td>
                    <td><input type="text" name="username" id="username" value=""/></td>
                </tr>
                <tr>
                    <td><label for="password">Password:</label></td>
                    <td><input type="password" name="password" id="password" value=""/></td>
                </tr>
                <tr>
                   <td>&nbsp;</td>
                   <td><input type="submit" value="Login"/></td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>