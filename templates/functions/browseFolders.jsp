<%@taglib uri="http://java.sun.com/jsp/jstl/core"         prefix="c"%>
<%@taglib uri="http://www.opencms.org/taglib/cms"         prefix="cms"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"          prefix="fmt"%>
<%@taglib uri="http://www.mashfrog.org/taglib/backoffice" prefix="bko" %>

<fmt:setLocale value="${cms.requestContext.locale}"/>
<fmt:bundle basename="com.mashfrog.backoffice.v2x.workplace">
        
            <c:if test="${bko:isNotEmptyOrWhiteSpaceOnly(cms.currentAction.errorMessage)}">
                <div><p><strong>${cms.currentAction.errorMessage}</strong></p></div>
            </c:if>
            <div>
                <p>Funzione browse folders</p>
            </div>

</fmt:bundle>