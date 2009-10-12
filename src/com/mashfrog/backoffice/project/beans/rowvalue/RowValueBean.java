package com.mashfrog.backoffice.project.beans.rowvalue;

import java.io.Serializable;

public interface RowValueBean extends Serializable{

    public String getType();

    public void setType(String type);

    public boolean getEnabled();

    public void setEnabled(boolean enabled);

}
