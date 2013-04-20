<html>
<head><title>Test jsp for dynamic generation</title></head>
<body>
  This is a dynamic generated jsp. The request url is: <%=request.getRequestURL()%><br/>
  <jsp:include page="test_include.jsp" /><br/>
  <jsp:include page="test_include.jsp" /><br/>
  <%@include file="test_include2.jsp" %>
</body>
</html>