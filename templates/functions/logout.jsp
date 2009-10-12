<%@page import="com.mashfrog.backoffice.CmsBackofficeActionElement"%>
<%@page import="com.mashfrog.backoffice.actions.RedirectingAction"%>
<%
    CmsBackofficeActionElement cms = (CmsBackofficeActionElement)request.getAttribute("cms");
    ((RedirectingAction)cms.getCurrentAction()).redirect();
%>