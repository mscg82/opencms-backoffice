package com.mscg.virgilio.programs;

import java.io.Serializable;
import java.util.Date;

public class ProgramDetails implements Serializable {

    private static final long serialVersionUID = -307399456122858704L;

    public static final int GREEN = 0;
    public static final int YELLOW = 1;
    public static final int RED = 2;

    private String strId;
    private Date lastUpdate;
    private String title;
    private String description;
    private int level;
    private String url;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProgramDetails other = (ProgramDetails) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (lastUpdate == null) {
            if (other.lastUpdate != null)
                return false;
        } else if (!lastUpdate.equals(other.lastUpdate))
            return false;
        if (level != other.level)
            return false;
        if (strId == null) {
            if (other.strId != null)
                return false;
        } else if (!strId.equals(other.strId))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

    public String getDescription() {
        return description;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public int getLevel() {
        return level;
    }

    public String getStrId() {
        return strId;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
        result = prime * result + level;
        result = prime * result + ((strId == null) ? 0 : strId.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ProgramDetails [description=" + description + ", lastUpdate=" + lastUpdate + ", level=" + level + ", strId="
               + strId + ", title=" + title + ", url=" + url + "]";
    }
}
