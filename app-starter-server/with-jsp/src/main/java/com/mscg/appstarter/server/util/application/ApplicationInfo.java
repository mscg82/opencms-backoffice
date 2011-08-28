package com.mscg.appstarter.server.util.application;

public class ApplicationInfo {

    private int id;
    private String name;
    private String commandLine;

    public ApplicationInfo(int id, String name, String commandLine) {
        this.id = id;
        this.name = name;
        this.commandLine = commandLine;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the commandLine
     */
    public String getCommandLine() {
        return commandLine;
    }

}
