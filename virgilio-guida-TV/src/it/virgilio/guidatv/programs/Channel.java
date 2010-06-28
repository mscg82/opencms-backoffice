package it.virgilio.guidatv.programs;

import com.mscg.util.Iterator;
import com.mscg.util.LinkedList;
import com.mscg.util.List;

/**
 * This class stores informations about
 * all programs for a specific TV channel. 
 * 
 * @author Giuseppe Miscione
 *
 */
public class Channel {

	private String id;
	private String name;
	private List types;
	private List tvPrograms;
	
	public Channel(String id, String name) {
		setId(id);
		setName(name);
		tvPrograms = new LinkedList();
	}
	
	public Channel(String id, String name, List types) {
		setId(id);
		setName(name);
		setTypes(types);
		tvPrograms = new LinkedList();
	}

	public void addTVProgram(TVProgram tvProgram) {
		tvPrograms.add(tvProgram);
		tvProgram.setChannel(this);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List getTVPrograms() {
		return tvPrograms;
	}

	public List getTypes() {
		return types;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTVPrograms(List tvPrograms) {
		this.tvPrograms = tvPrograms;
		for(Iterator it = this.tvPrograms.iterator(); it.hasNext();) {
			TVProgram program = (TVProgram) it.next();
			program.setChannel(this);
		}
	}
	
	public void setTypes(List types) {
		this.types = types;
	}
}
