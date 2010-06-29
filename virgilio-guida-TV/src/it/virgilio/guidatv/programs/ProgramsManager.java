/**
 * 
 */
package it.virgilio.guidatv.programs;

import com.mscg.util.Iterator;
import com.mscg.util.LinkedHashMap;
import com.mscg.util.Map;

/**
 * This class manages the retrieval of
 * programs data
 * 
 * @author Giuseppe Miscione
 *
 */
public class ProgramsManager {

	public static final int MAX_BUFFER_SIZE = 2;
	
	protected static ProgramsManager instance;
	
	/**
	 * Returns the singleton instance of this class.
	 * 
	 * @return The singleton instance of this class.
	 */
	public static ProgramsManager getInstance() {
		if(instance == null) {
			instance = new ProgramsManager();
		}
		return instance;
	}
	
	
	protected LinkedHashMap programsBuffer;
	
	protected ProgramsManager() {
		programsBuffer = new LinkedHashMap(MAX_BUFFER_SIZE);
	}
	
	/**
	 * Checks if the buffer is full. In this case, the oldest element
	 * in the buffer is removed. 
	 */
	public void checkForFreeSpace() {
		if(programsBuffer.size() == MAX_BUFFER_SIZE) {
			// remove the first element in the buffer
			Iterator head = programsBuffer.entrySet().iterator();
			Map.Entry entry = (Map.Entry)head.next();
			Programs p = (Programs)entry.getValue();
			clearPrograms(p);
			head.remove();
			
			// free memory
			System.gc();
			
			// give the GC some time
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) { }
		}
	}
	
	/**
	 * Removes all entries from memory cache and run the garbage collector
	 * to free memory as soon as possible.
	 */
	public void cleanMemoryCache() {
		for(Iterator it = programsBuffer.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			Programs p = (Programs)entry.getValue();
			clearPrograms(p);
		}
		programsBuffer.clear();
		
		// free memory
		System.gc();
		
		// give the GC some time
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) { }
	}
	
	/**
	 * Internal cleanup function.
	 * 
	 * @param p The {@link Programs} object that needs to be
	 * removed from memory.
	 */
	protected void clearPrograms(Programs p) {
		// clean up programs object so that the GC can free memory
		for(Iterator it1 = p.getChannels().iterator(); it1.hasNext();) {
			Channel c = (Channel) it1.next();
			for(Iterator it2 = c.getTVPrograms().iterator(); it2.hasNext();) {
				it2.next();
				it2.remove();
			}
			it1.remove();
		}
	}
	
	/**
	 * Retrieves the buffered {@link Programs} object
	 * associated with the porvided name.
	 * 
	 * @param dayName The name bounded to the {@link Programs} object.
	 * @return The {@link Programs} object associated with the name
	 * or <code>null</code> if no object is associated to the name.
	 */
	public Programs getProgramsByDayName(String dayName) {
		Programs ret = (Programs) programsBuffer.get(dayName);
		return ret;
	}
	
	/**
	 * Returns the number of items in the
	 * memory cache. 
	 * 
	 * @return The number of items in the
	 * memory cache.
	 */
	public int getProgramsInMemory() {
		return programsBuffer.size();
	}
	
	/**
	 * Returns the number of items in the
	 * disk cache. 
	 * 
	 * @return The number of items in the
	 * disk cache.
	 */
	public int getProgramsOnDisk() {
		return 0;
	}
	
	/**
	 * Adds a {@link Programs} object in the buffer. If the 
	 * buffer is full, the oldest element in the buffer is removed.
	 * 
	 * @param dayName The name bounded to the {@link Programs} object.
	 * @param programs The {@link Programs} object.
	 */
	public void saveProgramsByDayName(String dayName, Programs programs) {
		checkForFreeSpace();
		programsBuffer.put(dayName, programs);
	}

}
