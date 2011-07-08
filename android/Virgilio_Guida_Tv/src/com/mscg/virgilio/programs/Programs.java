/**
 *
 */
package com.mscg.virgilio.programs;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * This class stores all channels informations retrieved in the XML file.
 * 
 * @author Giuseppe Miscione
 * 
 */
public class Programs implements Serializable {

    private static final long serialVersionUID = -134251263780931920L;

    private long id;
    private Date date;
    private Date lastUpdate;
    private List<Channel> channels;

    public Programs() {
        this(-1);
    }

    public Programs(long id) {
        setId(id);
        channels = new LinkedList<Channel>();
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
        channel.setPrograms(this);
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public Date getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((channels == null) ? 0 : channels.hashCode());
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Programs other = (Programs) obj;
        if (channels == null) {
            if (other.channels != null)
                return false;
        } else if (!channels.equals(other.channels))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (id != other.id)
            return false;
        if (lastUpdate == null) {
            if (other.lastUpdate != null)
                return false;
        } else if (!lastUpdate.equals(other.lastUpdate))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Programs [channels=" + channels + ", date=" + date + ", id=" + id + ", lastUpdate=" + lastUpdate + "]";
    }
}
