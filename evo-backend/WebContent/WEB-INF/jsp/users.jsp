<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s"   uri="/struts-tags" %>
<fmt:setLocale value="${pageLocale}"/>

<fmt:bundle basename="eni.virtualoffice.translation">

<html>
<c:import url="common/header.jsp" />

<body>
    <c:import url="common/logout.jsp" />
    <div class="mainWrapper">
        The users page
    </div>
    <c:import url="common/footer.jsp" />
</body>
</html>
</fmt:bundle>