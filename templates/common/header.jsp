<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.opencms.org/taglib/cms" prefix="cms"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>

<%@page import="java.util.Calendar"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="com.mashfrog.backoffice.actions.constants.Constants"%>
<%@page import="com.mashfrog.backoffice.CmsBackofficeActionElement"%>
<%@page import="org.opencms.file.CmsUser"%>

<%
    Calendar now = GregorianCalendar.getInstance();
    CmsBackofficeActionElement cms = (CmsBackofficeActionElement) request.getAttribute("cms");
    if(cms == null){
    	cms = new CmsBackofficeActionElement(pageContext, request, response);
        request.setAttribute("cms", cms);     
    }
    CmsUser currUser = cms.getRequestContext().currentUser();
    pageContext.setAttribute("user", currUser);
    pageContext.setAttribute("additionalInfo", currUser.getAdditionalInfo(Constants.USER_ADDITIONAL_INFO_PARAM));
    pageContext.setAttribute("hour", now.get(Calendar.HOUR_OF_DAY));
%>
<fmt:setLocale value="${cms.requestContext.locale}"/>
<fmt:bundle basename="com.mashfrog.backoffice.v2x.workplace">      
       <div id="header">
           <a id="logo" href="<%=cms.getActionLink("default")%>"><img src="<cms:link>${rendering.logo}</cms:link>" alt="<fmt:message key="header.logo.gohome" />" /></a>
           <c:if test="${not user.guestUser}">
               <ul class="menuTop">
                   <li class="edit"><a href="<%=cms.getActionLink("changeInfo")%>" title="<fmt:message key="header.menu.changedata" />"><fmt:message key="header.menu.changedata" /></a></li>
                   <%-- <li class="cambia"><a href="actions/reload.jsp" title="<fmt:message key="header.menu.changebackoffice" />"><fmt:message key="header.menu.changebackoffice" /></a></li> --%>
                   <li class="out"><a href="<%=cms.getActionLink(Constants.LOGOUT_DEFAULT_NAME)%>" title="<fmt:message key="header.menu.exit" />"><fmt:message key="header.menu.exit" /></a></li>
               </ul>
           </c:if>
           
           <div>
               <ul class="user">
                   <c:set var="class" value="man"/>
                   <c:if  test="${not empty additionalInfo and additionalInfo.sex == 'F'}">
                       <c:set var="class" value="woman"/>
                   </c:if>
                   
                   <c:set var="greeting"><fmt:message key="header.user.goodevening" /></c:set>
                   <c:if test="${hour > 13}">
                       <c:set var="greeting"><fmt:message key="header.user.goodafternoon" /></c:set>
                   </c:if>
                       
                   <li class="${class}"><p><strong>${greeting}</strong> ${user.firstname} ${user.lastname}</p></li>
               </ul>
           </div>
       </div>
</fmt:bundle>