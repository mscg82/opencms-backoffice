<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.mscg.dyndns.dnsfactory.DnsProvider"%>
<%@ page import="com.mscg.dyndns.dnsfactory.DnsFactory"%>
<%@ page import="com.mscg.dyndns.servlet.IPReadServlet"%>
<%
    DnsProvider dns = DnsFactory.getProvider();
    pageContext.setAttribute("dns", dns);
    pageContext.setAttribute("formParams", new IPReadServlet.Parameters());
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
    </style>
</head>
<body>
    <div class="header">
	    Choose an application, a service and a protocol,
	    to see a page with links to the choosen data.
    </div>
    <div class="form">
	    <form action="list.jsp" method="get">
	        <table>
	            <tr>
	                <td><label for="application">Application:</label></td>
	                <td><input type="text" name="${formParams.applicationParam}" id="application" value="emule"/></td>
	            </tr>
	            <tr>
	                <td><label for="service">Service:</label></td>
	                <td>
	                    <select name="${formParams.serviceParam}" id="service">
	                        <c:forEach var="serv" items="${dns.servicesList}">
	                            <option value="${serv}">${serv}</option>
	                        </c:forEach>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	                <td><label for="protocol">Protocol:</label></td>
	                <td>
	                    <select name="${formParams.protocolParam}" id="protocol">
	                        <option value="http" selected="selected">http</option>
	                        <option value="https">https</option>
	                    </select>
	                </td>
	            </tr>
	            <tr>
	               <td>&nbsp;</td>
	               <td><input type="submit" value="See mapped IPs"/></td>
	            </tr>
	        </table>
	    </form>
    </div>
</body>
</html>