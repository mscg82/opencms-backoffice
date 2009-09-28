package com.mashfrog.backoffice.actions.constants;

import com.mashfrog.backoffice.actions.impl.BackofficeLoginAction;

public class Constants {

    public static final String ACTION_PARAM = "action";
    public static final String PAGE_PARAM  = "page";

    public static final String BACKOFFFICE_PROJECT_FOLDER = "project";

    public static final String BACKOFFICE_RESOURCE_NAME = "BackofficeProject";

    public static final String LOGIN_DEFAULT_NAME = "login";
    public static final String LOGIN_DEFAULT_ACTION_CLASS = BackofficeLoginAction.class.getName();
    public static final String LOGIN_DEFAULT_JSP_PATTERN = "/system/modules/${module.name}/template/actions/login.jsp";

    public static final String NOACTION_DEFAULT_NAME = "default";
    public static final String NOACTION_DEFAULT_ACTION_CLASS = BackofficeLoginAction.class.getName();
    public static final String NOACTION_DEFAULT_JSP_PATTERN = "/system/modules/${module.name}/template/actions/default.jsp";

}
