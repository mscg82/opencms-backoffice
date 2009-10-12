package com.mashfrog.backoffice.project.beans.rowvalue;

public class ButtonedComboBoxRowValueBean extends ComboBoxRowValueBean {

	private static final long serialVersionUID = 6871600679401991631L;

	protected String buttonText;
    protected String buttonAction;

    public ButtonedComboBoxRowValueBean(){
    	this(null, null);
    }

    public ButtonedComboBoxRowValueBean(String buttonText, String buttonAction){
    	super();
    	setButtonText(buttonText);
    	setButtonAction(buttonAction);
    }

    public String getButtonText(){
        return buttonText;
    }

    public String getButtonAction(){
    	return buttonAction;
    }

    public void setButtonText(String buttonText){
    	this.buttonText = buttonText;
    }

    public void setButtonAction(String buttonAction){
    	this.buttonAction = buttonAction;
    }

}
