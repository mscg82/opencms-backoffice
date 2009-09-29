package com.mashfrog.backoffice.actions.constants;

import com.mashfrog.backoffice.actions.impl.BackofficeDefaultAction;
import com.mashfrog.backoffice.actions.impl.BackofficeLoginAction;
import com.mashfrog.backoffice.actions.impl.BackofficeLogoutAction;

public class Constants {

    public static final String ACTION_PARAM = "action";
    public static final String PAGE_PARAM  = "page";

    public static final String PREVIOUS_ACTION_SESSION_PARAM = "prev_action";
    public static final String ACTUAL_ACTION_SESSION_PARAM   = "act_action";

    public static final String BACKOFFFICE_PROJECT_FOLDER = "project";

    public static final String BACKOFFICE_RESOURCE_NAME = "BackofficeProject";

    public static final String LOGIN_DEFAULT_NAME = "login";
    public static final String LOGIN_DEFAULT_ACTION_CLASS = BackofficeLoginAction.class.getName();
    public static final String LOGIN_DEFAULT_JSP_PATTERN = "/system/modules/${module.name}/template/actions/login.jsp";

    public static final String LOGOUT_DEFAULT_NAME = "logout";
    public static final String LOGOUT_DEFAULT_ACTION_CLASS = BackofficeLogoutAction.class.getName();
    public static final String LOGOUT_DEFAULT_JSP_PATTERN = "/system/modules/${module.name}/template/actions/logout.jsp";

    public static final String NOACTION_DEFAULT_NAME = "default";
    public static final String NOACTION_DEFAULT_ACTION_CLASS = BackofficeDefaultAction.class.getName();
    public static final String NOACTION_DEFAULT_JSP_PATTERN = "/system/modules/${module.name}/template/actions/default.jsp";

}
