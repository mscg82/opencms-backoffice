<%@taglib uri="http://java.sun.com/jsp/jstl/core"         prefix="c"%>
<%@taglib uri="http://www.opencms.org/taglib/cms"         prefix="cms"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"          prefix="fmt"%>
<%@taglib uri="http://www.mashfrog.org/taglib/backoffice" prefix="bko" %>

<%@page import="org.opencms.workplace.CmsLogin"%>
<%@page import="com.mashfrog.backoffice.CmsBackofficeActionElement"%>
<%@page import="com.mashfrog.backoffice.actions.constants.Constants"%>
<%
    CmsBackofficeActionElement cms = (CmsBackofficeActionElement) request.getAttribute("cms");
    if(cms == null){
        cms = new CmsBackofficeActionElement(pageContext, request, response);
        request.setAttribute("cms", cms);     
    }
    CmsLogin login = new CmsLogin(pageContext, request, response);
%>
        
<fmt:setLocale value="${cms.requestContext.locale}"/>
<fmt:bundle basename="com.mashfrog.backoffice.v2x.workplace">    
        
            <c:if test="${bko:isNotEmptyOrWhiteSpaceOnly(cms.currentAction.errorMessage)}">
                <div><p><strong>${cms.currentAction.errorMessage}</strong></p></div>
            </c:if>
            <div>
                <p>
                    <fmt:message key="login.header" />
                </p>
                <form method="post" action="<%=cms.getActionLink(Constants.LOGIN_DEFAULT_NAME)%>" class="formSelectCat">
                    <fieldset>
                        <table border="0" cellpadding="8" cellspacing="8">
                            <caption><fmt:message key="login.form.caption"/></caption>
                            <tbody>
                            <tr>
                                <td><label for="username"><fmt:message key="login.form.username"/></label></td><td><input type="text" name="${cms.currentAction.usernameParam}" id="username" value=""/></td>
                            </tr>
                            <tr>
                                <td><label for="password"><fmt:message key="login.form.password"/></label></td><td><input type="password" name="${cms.currentAction.passwordParam}" id="password" value=""/></td>
                            </tr>
                            <tr>
                                 <td><label for="siteSelect"><fmt:message key="login.form.orgunit"/></label></td>
                                 <td><%= login.buildOrgUnitSelector() %></td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td><td><input type="submit" value="<fmt:message key="login.form.submit"/>"/></td>
                            </tr>
                            </tbody>
                        </table>
                        <input type="hidden" name="${cms.currentAction.executeParam}" value="true"/>
                    </fieldset>
                </form>
            </div>

</fmt:bundle>