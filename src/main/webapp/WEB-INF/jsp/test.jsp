<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Test page</title>
</head>
<body>
    <h1>Test page</h1>
    <p>
        <b>Model value:</b> ${testValue} <br/>
        <%= new java.util.Date() %>
    </p>
</body>
</html>