package com.mscg.virgilio.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.mscg.virgilio.database.ChannelsManagement;
import com.mscg.virgilio.database.ProgramsDB;
import com.mscg.virgilio.database.ProgramsManagement;
import com.mscg.virgilio.programs.Channel;
import com.mscg.virgilio.programs.Programs;

public class CacheManager implements Closeable, ProgramsManagement, ChannelsManagement {

    private static CacheManager instance;

    public synchronized static CacheManager getInstance() {
        return instance;
    }

    public synchronized static void init(Context context) {
        instance = new CacheManager(context);
    }

    private ProgramsDB db;

    private LRUMap programsCache;

    private LRUMap channelsCache;

    protected CacheManager(Context context) {
        db = new ProgramsDB(context, true);
        programsCache = new LRUMap(4);
        channelsCache = new LRUMap(80);
    }

    public synchronized void clearChannelsCache() {
        channelsCache.clear();
    }

    public synchronized void clearProgramsCache() {
        programsCache.clear();
    }

    @Override
    public synchronized void close() throws IOException {
        IOUtils.closeQuietly(db);
        db = null;
        programsCache.clear();
        programsCache = null;
        channelsCache.clear();
        channelsCache = null;
        instance = null;
    }

    @Override
    public synchronized boolean containsProgramsForDay(Date day) throws SQLException {
        // search in cache
        boolean ret = programsCache.containsKey(Util.adaptDate(day));
        if (!ret) {
            // search in the DB
            ret = db.containsProgramsForDay(day);
        }
        return ret;
    }

    @Override
    public synchronized Channel getChannel(long programsID, long channelID) {
        return getChannel(programsID, channelID, false);
    }

    @Override
    public Channel getChannel(long programsID, long channelID, boolean removeElement) {
        String key = Long.toString(programsID) + channelID;
        Channel ret = null;
        if (channelsCache.containsKey(key)) {
            ret = (Channel) channelsCache.get(key);
            if (removeElement)
                removeChannel(programsID, channelID);
        } else {
            ret = db.getChannel(programsID, channelID, removeElement);
            if (ret != null && !removeElement)
                channelsCache.put(key, ret);
        }
        return ret;
    }

    public synchronized int getChannelsCacheSize() {
        if (channelsCache == null)
            return 0;
        return channelsCache.size();
    }

    public Cursor getPrograms() throws SQLException {
        return db.getPrograms();
    }

    public synchronized int getProgramsCacheSize() {
        if (programsCache == null)
            return 0;
        return programsCache.size();
    }

    @Override
    public synchronized long getProgramsCount() throws SQLException {
        return db.getProgramsCount();
    }

    @Override
    public synchronized Programs getProgramsForDay(Date day) throws SQLException {
        // search in cache
        Programs ret = (Programs) programsCache.get(Util.adaptDate(day));
        if (ret == null) {
            // search in the DB
            ret = db.getProgramsForDay(day);
            saveProgramsInCache(ret);
        }
        return ret;
    }

    @Override
    public synchronized void removeAllPrograms() throws SQLException {
        clearChannelsCache();
        clearProgramsCache();
        db.removeAllPrograms();
    }

    @Override
    public synchronized boolean removeChannel(long programsID, long channelID) {
        db.removeChannel(programsID, channelID);
        String key = Long.toString(programsID) + channelID;
        return (channelsCache.remove(key) != null);
    }

    @Override
    public synchronized void removeOlderPrograms(int numDays) throws SQLException {
        Calendar limit = new GregorianCalendar();
        limit.add(Calendar.DAY_OF_MONTH, -numDays);

        // cycle through cache and remove older elements
        boolean clearCache = false;
        Set entries = programsCache.entrySet();
        for (Iterator it = entries.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Programs programs = (Programs) entry.getValue();
            if (programs.getDate().getTime() < limit.getTimeInMillis()) {
                it.remove();
                clearCache = true;
            }
        }
        if (clearCache)
            channelsCache.clear();

        // remove also programs from DB
        db.removeOlderPrograms(numDays);
    }

    @Override
    public synchronized void saveChannel(long programsID, long channelID, Channel channel) {
        channelsCache.put(Long.toString(programsID) + channelID, channel);
        db.saveChannel(programsID, channelID, channel);
    }

    @Override
    public void removeProgramById(long id) throws SQLException {
        Set<Map.Entry> entrySet = programsCache.entrySet();
        for (Map.Entry entry : entrySet) {
            Programs p = (Programs) entry.getValue();
            if (p.getId() == id) {
                programsCache.remove(entry.getKey());
                break;
            }
        }
        db.removeProgramById(id);
    }

    @Override
    public synchronized Programs savePrograms(Programs programs) throws SQLException {
        Programs ret = db.savePrograms(programs);
        saveProgramsInCache(ret);
        return ret;
    }

    protected void saveProgramsInCache(Programs programs) {
        programsCache.put(Util.adaptDate(programs.getDate()), programs);
    }

}
