/**
 *
 */
package com.mscg.virgilio.programs;

import java.io.Serializable;
import java.util.Date;

/**
 * This class stores informations about a specific TV program.
 * 
 * @author Giuseppe Miscione
 * 
 */
public class TVProgram implements Serializable {

    private static final long serialVersionUID = -749974415377776435L;

    private long id;
    private String strId;
    private String name;
    private Date startTime;
    private Date endTime;
    private String category;
    private Channel channel;
    private ProgramDetails programDetails;

    public TVProgram(long id, String strId, String name, Date startTime, Date endTime, String category) {
        setId(id);
        setStrId(strId);
        setName(name);
        setStartTime(startTime);
        setEndTime(endTime);
        setCategory(category);
    }

    public TVProgram(String strId, String name, Date startTime, Date endTime, String category) {
        this(-1, strId, name, startTime, endTime, category);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TVProgram other = (TVProgram) obj;
        if (category == null) {
            if (other.category != null)
                return false;
        } else if (!category.equals(other.category))
            return false;
        if (channel == null) {
            if (other.channel != null)
                return false;
        } else if (!channel.equals(other.channel))
            return false;
        if (endTime == null) {
            if (other.endTime != null)
                return false;
        } else if (!endTime.equals(other.endTime))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (startTime == null) {
            if (other.startTime != null)
                return false;
        } else if (!startTime.equals(other.startTime))
            return false;
        if (strId == null) {
            if (other.strId != null)
                return false;
        } else if (!strId.equals(other.strId))
            return false;
        return true;
    }

    public String getCategory() {
        return category;
    }

    public Channel getChannel() {
        return channel;
    }

    public Date getEndTime() {
        return endTime;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProgramDetails getProgramDetails() {
        return programDetails;
    }

    public Date getStartTime() {
        return startTime;
    }

    public String getStrId() {
        return strId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((channel == null) ? 0 : channel.hashCode());
        result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result + ((strId == null) ? 0 : strId.hashCode());
        return result;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProgramDetails(ProgramDetails programDetails) {
        this.programDetails = programDetails;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    @Override
    public String toString() {
        return "TVProgram [category=" + category + ", endTime=" + endTime + ", id=" + id + ", name=" + name + ", startTime="
               + startTime + ", strId=" + strId + "]";
    }
}
