package com.mashfrog.backoffice.project.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExportDestinationFolderBean implements Serializable{

	private static final long serialVersionUID = 5669958506160553146L;

	private String description;
    private List<String> folders;

    public ExportDestinationFolderBean(String description){
    	this(description, null);
    }

    public ExportDestinationFolderBean(String description, String folder){
    	setDescription(description);
    	folders = new ArrayList<String>();
    	addFolder(folder);
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
    	this.description = description;
    }

    public List<String> getFoldersList(){
        return folders;
    }

    public String getFolders(){
        return folders.toString();
    }

    public void addFolder(String folder){
    	folders.add(folder);
    }

    public String toString(){
        return "description: " + description + "; folders: + " + folders;
    }

}
