package com.mashfrog.backoffice.project.beans;


public class BaseCommandMenuBean extends GroupOUAssociableBean{

	private static final long serialVersionUID = 6239497483928634708L;

	protected String label;
	protected boolean actual;

    public BaseCommandMenuBean(){
    	this(null);
    }

	public BaseCommandMenuBean(String label){
    	super();
    	setActual(false);
    	setLabel(label);
    }

	public String getLabel(){
        return label;
    }

    public boolean isActual() {
		return actual;
	}

    public void setActual(boolean actual) {
		this.actual = actual;
	}

    public void setLabel(String label){
    	this.label = label;
    }

}
