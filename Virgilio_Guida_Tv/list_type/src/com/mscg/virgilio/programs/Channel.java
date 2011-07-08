package com.mscg.virgilio.programs;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class stores informations about all programs for a specific TV channel.
 *
 * @author Giuseppe Miscione
 *
 */
public class Channel implements Serializable {

    private static final long serialVersionUID = 6460693407420721917L;

    private long id;
    private String strId;
    private String name;
    private List<String> types;
    private List<TVProgram> tvPrograms;
    private Programs programs;

    public Channel(long id, String strId, String name) {
        this(id, strId, name, null);
    }

    public Channel(long id, String strId, String name, List<String> types) {
        setId(id);
        setStrId(strId);
        setName(name);
        setTypes(types == null ? new LinkedList<String>() : types);
        tvPrograms = new LinkedList<TVProgram>();
    }

    public Channel(String strId, String name) {
        this(-1, strId, name, null);
    }

    public Channel(String strId, String name, List<String> types) {
        this(-1, strId, name, types);
    }

    public void addTVProgram(TVProgram tvProgram) {
        tvPrograms.add(tvProgram);
        tvProgram.setChannel(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Channel other = (Channel) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (strId == null) {
            if (other.strId != null)
                return false;
        } else if (!strId.equals(other.strId))
            return false;
        if (tvPrograms == null) {
            if (other.tvPrograms != null)
                return false;
        } else if (!tvPrograms.equals(other.tvPrograms))
            return false;
        if (types == null) {
            if (other.types != null)
                return false;
        } else if (!types.equals(other.types))
            return false;
        return true;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Programs getPrograms() {
        return programs;
    }

    public String getStrId() {
        return strId;
    }

    public List<TVProgram> getTVPrograms() {
        return tvPrograms;
    }

    public List<String> getTypes() {
        return types;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((strId == null) ? 0 : strId.hashCode());
        result = prime * result + ((tvPrograms == null) ? 0 : tvPrograms.hashCode());
        result = prime * result + ((types == null) ? 0 : types.hashCode());
        return result;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrograms(Programs programs) {
        this.programs = programs;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public void setTVPrograms(List<TVProgram> tvPrograms) {
        this.tvPrograms = tvPrograms;
        for (TVProgram program : tvPrograms) {
            program.setChannel(this);
        }
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return name;
    }

    public String toString2() {
        return "Channel [id=" + id + ", name=" + name + ", strId=" + strId + ", tvPrograms=" + tvPrograms + ", types=" + types
               + "]";
    }
}
