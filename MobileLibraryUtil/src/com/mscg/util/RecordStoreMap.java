/**
 * 
 */
package com.mscg.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import com.mscg.io.Closeable;

/**
 * @author Giuseppe Miscione
 *
 */
public class RecordStoreMap implements Map, Closeable {

	private Map innerMap;
	private RecordStore recordStore;

	public RecordStoreMap(String recordStoreName) throws RecordStoreException {
		this(recordStoreName, true);
	}
	
	public RecordStoreMap(String recordStoreName, boolean createIfNecessary) throws RecordStoreException {
		innerMap = new LinkedHashMap();
		recordStore = RecordStore.openRecordStore(recordStoreName, createIfNecessary);
		try {
			RecordEnumeration records = recordStore.enumerateRecords(null, null, true);
			while(records.hasNextElement()) {
				int index = records.nextRecordId();
				byte buffer[] = recordStore.getRecord(index);
				ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
				DataInputStream dis = new DataInputStream(bis);
				RecordStoreMapValue value = new RecordStoreMapValue(this, index, buffer);
				innerMap.put(dis.readUTF(), value);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @see com.mscg.util.Map#clear()
	 */
	public void clear() {
		try {
			RecordEnumeration records = recordStore.enumerateRecords(null, null, true);
			while(records.hasNextElement()) {
				int index = records.nextRecordId();
				recordStore.deleteRecord(index);
			}
			innerMap.clear();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void close() throws IOException {
		if(recordStore != null) {
			try {
				recordStore.closeRecordStore();
				innerMap.clear();
			} catch (RecordStoreNotOpenException e) {
				throw new IOException(
					"RecordStoreNotOpenException thrown " + e.getMessage());
			} catch (RecordStoreException e) {
				throw new IOException(
					"RecordStoreException thrown " + e.getMessage());
			}
		}
	}

	/**
	 * @param key
	 * @return
	 * @see com.mscg.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return innerMap.containsKey(key);
	}

	/**
	 * @param value
	 * @return
	 * @see com.mscg.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return innerMap.containsValue(value);
	}

	/**
	 * @return
	 * @see com.mscg.util.Map#entrySet()
	 */
	public Set entrySet() {
		return innerMap.entrySet();
	}

	/**
	 * @param o
	 * @return
	 * @see com.mscg.util.Map#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return innerMap.equals(o);
	}

	/**
	 * @param key
	 * @return
	 * @see com.mscg.util.Map#get(java.lang.Object)
	 */
	public Object get(Object key) {
		return innerMap.get(key);
	}

	/**
	 * @return
	 * @see com.mscg.util.Map#hashCode()
	 */
	public int hashCode() {
		return innerMap.hashCode();
	}

	/**
	 * @return
	 * @see com.mscg.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return innerMap.isEmpty();
	}

	/**
	 * @return
	 * @see com.mscg.util.Map#keySet()
	 */
	public Set keySet() {
		return innerMap.keySet();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see com.mscg.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public Object put(Object key, Object value) {
		try {
			RecordStoreMapValue mapValue = null;
			if(innerMap.containsKey(key)) {
				// replace element in the record store
				mapValue = (RecordStoreMapValue)innerMap.get(key);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(bos);
				dos.writeUTF(key.toString());
				dos.writeUTF(value.toString());
				dos.flush();
				byte buffer[] = bos.toByteArray();
				recordStore.setRecord(mapValue.getIndex(), buffer, 0, buffer.length);
				mapValue.setData(buffer);
			}
			else {
				// add new element in the record store
				mapValue = new RecordStoreMapValue(this, 0, null);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(bos);
				dos.writeUTF(key.toString());
				dos.writeUTF(value.toString());
				dos.flush();
				byte buffer[] = bos.toByteArray();
				int index = recordStore.addRecord(buffer, 0, buffer.length);
				mapValue.setIndex(index);
				mapValue.setData(buffer);
			}
			return innerMap.put(key, mapValue);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param m
	 * @see com.mscg.util.Map#putAll(com.mscg.util.Map)
	 */
	public void putAll(Map m) {
		for(Iterator it = m.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * @param key
	 * @return
	 * @see com.mscg.util.Map#remove(java.lang.Object)
	 */
	public Object remove(Object key) {
		try {
			RecordStoreMapValue mapValue = null;
			if(innerMap.containsKey(key)) {
				mapValue = (RecordStoreMapValue)innerMap.get(key);
				recordStore.deleteRecord(mapValue.getIndex());
				return innerMap.remove(key);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * @return
	 * @see com.mscg.util.Map#size()
	 */
	public int size() {
		return innerMap.size();
	}

	/**
	 * @return
	 * @see com.mscg.util.Map#values()
	 */
	public Collection values() {
		return innerMap.values();
	}
	
	public class RecordStoreMapValue {
		private RecordStoreMap map;
		private int index;
		private byte[] data;
		
		public RecordStoreMapValue(RecordStoreMap map, int index, byte[] data) {
			super();
			setMap(map);
			setIndex(index);
			setData(data);
		}

		/**
		 * @return the index
		 */
		public int getIndex() {
			return index;
		}
		
		/**
		 * @param index the index to set
		 */
		public void setIndex(int index) {
			this.index = index;
		}
		
		/**
		 * @return the data
		 */
		public byte[] getData() {
			return data;
		}
		
		/**
		 * @return the map
		 */
		public RecordStoreMap getMap() {
			return map;
		}

		/**
		 * @param map the map to set
		 */
		public void setMap(RecordStoreMap map) {
			this.map = map;
		}

		/**
		 * @param data the data to set
		 */
		public void setData(byte[] data) {
			this.data = data;
		}
		
		public String[] getStrings() {
			String ret[] = new String[2];
			ByteArrayInputStream bis = new ByteArrayInputStream(this.data);
			DataInputStream dis = new DataInputStream(bis);
			try {
				ret[0] = dis.readUTF();
				ret[1] = dis.readUTF();
			} catch(Exception e) {
				e.printStackTrace();
				ret = null;
			}
			return ret;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			if(obj == null)
				return false;
			if(!(obj instanceof RecordStoreMapValue))
				return false;
			RecordStoreMapValue other = (RecordStoreMapValue)obj;
			return
				this.getMap() == other.getMap() &&
				this.getIndex() == other.getIndex();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return
				(Integer.toString(this.getIndex()) + this.getMap().hashCode()).hashCode();
		}
	}

}
