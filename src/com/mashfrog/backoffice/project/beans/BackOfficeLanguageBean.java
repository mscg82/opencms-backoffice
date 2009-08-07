package com.mashfrog.backoffice.project.beans;

public class BackOfficeLanguageBean {
    private String languageId;
    private String languageName;
    private boolean defaultLanguage;

    public BackOfficeLanguageBean(String languageId, String languageName){
    	this(languageId, languageId, false);
    }

    public BackOfficeLanguageBean(String languageId, String languageName, boolean defaultLanguage){
    	setLanguageId(languageId);
    	setLanguageName(languageName);
    	setDefaultLanguage(defaultLanguage);
    }

    public boolean equals(Object obj){
    	if(obj == null)
    		return false;
    	if(obj == this)
    		return true;
    	if(!(obj instanceof BackOfficeLanguageBean))
    		return false;
    	BackOfficeLanguageBean lang2 = (BackOfficeLanguageBean) obj;
        return
            lang2.getLanguageId().equals(languageId) &&
            lang2.getLanguageName().equals(languageName) &&
            (lang2.isDefaultLanguage() == defaultLanguage);
    }

    public String getLanguageId(){
        return languageId;
    }

    public String getLanguageName(){
        return languageName;
    }

    public int hashCode(){
    	String tmp = languageId + "_" + languageName + "_" + defaultLanguage;
        return tmp.hashCode();
    }

    public boolean isDefaultLanguage(){
        return defaultLanguage;
    }

    public void setDefaultLanguage(boolean defaultLanguage){
    	this.defaultLanguage = defaultLanguage;
    }

    public void setLanguageId(String languageId){
    	this.languageId = languageId;
    }

    public void setLanguageName(String languageName){
    	this.languageName = languageName;
    }

}
