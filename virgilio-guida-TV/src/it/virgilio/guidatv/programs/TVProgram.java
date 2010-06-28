/**
 * 
 */
package it.virgilio.guidatv.programs;

/**
 * This class stores informations about a specific
 * TV program.
 * 
 * @author Giuseppe Miscione
 *
 */
public class TVProgram {

	private String id;
	private String name;
	private String startTime;
	private String endTime;
	private String category;
	private Channel channel;
	
	public TVProgram(String id, String name, String startTime, String endTime,
			String category) {
		super();
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public Channel getChannel() {
		return channel;
	}
	
	public String getEndTime() {
		return endTime;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getStartTime() {
		return startTime;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
}
