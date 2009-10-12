package com.mashfrog.backoffice.project.beans;

public class ActionBean extends GroupOUAssociableBean{

    private static final long serialVersionUID = -1200137306955152290L;

    private String className;
    private String jspPath;
    private String additionalConfigurationFilePath;

    public ActionBean() {
    	super();
    }

    public ActionBean(String className, String jspPath, String additionalConfigurationFilePath) {
		setClassName(className);
		setJspPath(jspPath);
		setAdditionalConfigurationFilePath(additionalConfigurationFilePath);
	}

	public String getClassName() {
		return className;
	}

    public void setClassName(String className) {
		this.className = className;
	}

    public String getJspPath() {
		return jspPath;
	}

    public void setJspPath(String jspPath) {
		this.jspPath = jspPath;
	}

    public String getAdditionalConfigurationFilePath() {
		return additionalConfigurationFilePath;
	}

    public void setAdditionalConfigurationFilePath(String additionalConfigurationFilePath) {
		this.additionalConfigurationFilePath = additionalConfigurationFilePath;
	}

	@Override
	public String toString() {
		return "{Class name: " + getClassName() + "; Jsp path: " + getJspPath() + "; " +
				"additional configuration: " + getAdditionalConfigurationFilePath() + "; groups/OU: " + super.toString() + "}";
	}
}
