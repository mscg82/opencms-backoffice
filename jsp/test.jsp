<%@ page import="java.util.*"%>
<%@ page import="com.mscg.taglib.test.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="m" uri="http://www.mscg.com/taglib" %>
<%
    List<String> test = new LinkedList<String>();
    test.add("a");
    test.add("b");
    test.add("c");
    pageContext.setAttribute("test", test);
    pageContext.setAttribute("newValue", "new value");
%>
<html>
    <head>
        <title>MscG taglib test</title>
    </head>
    <body>
        <m:method object="${test}" var="size" method="size" />
        The size of the list is ${size}<br/>
        <c:forEach var="item" items="${test}">
            ${item}<br/>
        </c:forEach>
        
        <m:method object="${test}" method="add">
            <m:param>by hand <c:out value="${newValue}" /></m:param>
        </m:method>
        
        The size of the list is <m:method object="${test}" output="true" method="size" /><br/>
        <c:forEach var="item" items="${test}">
            ${item}<br/>
        </c:forEach>
        
        <m:constructor var="testObj2" className="com.mscg.taglib.test.TestObject">
            <m:param type="int">3</m:param>
            <m:param type="java.lang.String">ciao</m:param>
        </m:constructor>
        
        <m:constructor var="testObj1" className="com.mscg.taglib.test.TestObject">
            <m:param type="int">5</m:param>
            <m:param type="java.lang.String">external</m:param>
            <m:param type="com.mscg.taglib.test.TestObject" value="${testObj2}"/>
        </m:constructor>
        
        ${testObj1} <br/>
        
        <m:method object="com.mscg.taglib.test.TestObject" method="setDoubleVal">
            <m:param type="java.lang.Double" value="3.452644"/>
        </m:method>
        
        Static double value is <m:method object="com.mscg.taglib.test.TestObject" output="true" method="getDoubleVal"/>
    </body>
</html>