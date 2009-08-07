package com.mashfrog.backoffice.actions;

import com.mashfrog.backoffice.project.beans.ValuedNavigationMenuBean;

public abstract class BackofficeNavMenuAction extends A_BackofficeAction {
    protected ValuedNavigationMenuBean navigationMenu;

    public ValuedNavigationMenuBean getNavigationMenu(){
        return navigationMenu;
    }

}
