/**
 * 
 */
package it.virgilio.guidatv.programs;

import java.util.Date;

import com.mscg.util.LinkedList;
import com.mscg.util.List;

/**
 * This class stores all channels
 * informations retrieved in the XML file.
 * 
 * @author Giuseppe Miscione
 *
 */
public class Programs {

	private Date date;
	private Date lastUpdate;
	private List channels;
	
	public Programs() {
		channels = new LinkedList();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public void addChannel(Channel channel) {
		channels.add(channel);
	}

	public List getChannels() {
		return channels;
	}

	public void setChannels(List channels) {
		this.channels = channels;
	}
}
