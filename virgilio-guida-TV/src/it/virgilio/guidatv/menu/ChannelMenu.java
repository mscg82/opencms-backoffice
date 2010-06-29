/**
 * 
 */
package it.virgilio.guidatv.menu;

import it.virgilio.guidatv.item.ProgramItem;
import it.virgilio.guidatv.programs.Channel;
import it.virgilio.guidatv.programs.TVProgram;

import org.j4me.ui.DeviceScreen;

import com.mscg.util.Iterator;

/**
 * @author Giuseppe Miscione
 *
 */
public class ChannelMenu extends BaseMenu {

	private Channel channel;
	private boolean drawInterface;
	
	public ChannelMenu(Channel channel, DeviceScreen previous) {
		super(channel.getName(), previous);
		
		setMenuText(getLeftMenuText(), null);
		
		this.drawInterface = true;
		this.channel = channel;
	
	}

	public void showNotify() {
		super.showNotify();
		if(drawInterface) {
			// lazily draw the interface only when the channel
			// is shown for the first time
			
			drawInterface = false;
			int index = 0;
//			Calendar now = Calendar.getInstance();
//			int hh = now.get(Calendar.HOUR_OF_DAY);
//			int mm = now.get(Calendar.MINUTE);
			for(Iterator it = this.channel.getTVPrograms().iterator(); it.hasNext();) {
				TVProgram pr = (TVProgram) it.next();
//				try {
//					List st = Util.splitStringAsList(pr.getStartTime(), ":");
//					int stHH = Integer.parseInt((String)st.get(0), 10);
//					int stMM = Integer.parseInt((String)st.get(1), 10);
//					List et = Util.splitStringAsList(pr.getEndTime(), ":");
//					int etHH = Integer.parseInt((String)et.get(0), 10);
//					int etMM = Integer.parseInt((String)et.get(1), 10);
//					
//					if(stHH <= hh && etHH >= hh && stMM <= mm && etMM >= mm) {
//						setSelected(index);
//					}
//					
//				} catch(Exception e){}
				append(new ProgramItem(pr));
				index++;
			}
		}
	}

}
