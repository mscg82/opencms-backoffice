/**
 * 
 */
package it.virgilio.guidatv.menu;

import it.virgilio.guidatv.programs.Channel;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.components.Label;

/**
 * @author Giuseppe Miscione
 *
 */
public class ChannelMenu extends BaseMenu {

	private Channel channel;
	
	/**
	 * @param name
	 * @param previous
	 */
	public ChannelMenu(Channel channel, DeviceScreen previous) {
		super(channel.getName(), previous);
		this.channel = channel;
		
		append(new Label("Programmi per questo canale: " + channel.getTVPrograms().size()));
	}

}
