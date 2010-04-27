package main.settings;

// SpaceHulkME  Copyright (C) 2008  Adam Gates

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import main.SpaceHulk;

// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

public class Settings
{
    public static final String SETTINGS_RECORD_PREFIX = "[[SETTINGS]]";

    private static final String SOUND_PARAM       = "sound=";
    private static final String FULLSCREEN_PARAM  = "fullscreen=";
    private static final String ROTATE_PARAM      = "rotate=";
    private static final String INVERT_KEYS_PARAM = "invert=";

    public boolean sound;
    public boolean fullscreen;
    public boolean rotate;
    public boolean invertKeys;

    private RecordStore settingsDB;

    private static Settings instance;

    public static Settings getInstance() {
        if(instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    private Settings() {
        // default settings
        setDefaults();

        loadSettings();
    }

    public void setDefaults() {
        setDefaults(false);
    }

    public void setDefaults(boolean autoSave) {
        sound = true;
        fullscreen = true;
        rotate = false;
        invertKeys = false;
        if(System.getProperty("microedition.platform").equals("SunMicrosystems_wtk"))
            sound = false;
        if(System.getProperty("microedition.media.version") == null)
            sound = false;

        if(autoSave)
            saveSettings();
    }

    public void close() {
        if(settingsDB != null) {
            // close record store
            try {
                settingsDB.closeRecordStore();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            settingsDB = null;
        }
    }

    private void loadSettings() {
        // Open the settings record store
        RecordEnumeration records = null;
        try {
            if(settingsDB == null)
                settingsDB = RecordStore.openRecordStore("settings", true);
            records = settingsDB.enumerateRecords(new SettingsRecordFilter(), null, true);
            while(records.hasNextElement()) {
                ByteArrayInputStream bais1 = new ByteArrayInputStream(records.nextRecord());
                DataInputStream dis1 = new DataInputStream(bais1);
                String record = dis1.readUTF();

                // strip prefix
                record = record.substring(SETTINGS_RECORD_PREFIX.length());
                if(record.startsWith(SOUND_PARAM)) {
                    sound = record.substring(SOUND_PARAM.length()).equalsIgnoreCase("true");
                }
                else if(record.startsWith(FULLSCREEN_PARAM)) {
                    fullscreen = record.substring(FULLSCREEN_PARAM.length()).equalsIgnoreCase("true");
                }
                else if(record.startsWith(ROTATE_PARAM)) {
                    rotate = record.substring(ROTATE_PARAM.length()).equalsIgnoreCase("true");
                }
                else if(record.startsWith(INVERT_KEYS_PARAM)) {
                    invertKeys = record.substring(INVERT_KEYS_PARAM.length()).equalsIgnoreCase("true");
                }
            }

        } catch (Exception ex) {
            SpaceHulk.instance.showException("Settings::loadSettings", ex);
        } finally {
            if(records != null) {
                records.destroy();
            }
        }
    }

    public void saveSettings() {
        RecordEnumeration records = null;
        try {
            if(settingsDB == null)
                settingsDB = RecordStore.openRecordStore("settings", true);
            ByteArrayOutputStream baos = null;
            DataOutputStream outputStream = null;
            String record = null;

            byte soundRecord[] = null;
            byte fullscreenRecord[] = null;
            byte rotateRecord[] = null;
            byte invertRecord[] = null;

            // build sound record
            baos = new  ByteArrayOutputStream();
            outputStream = new DataOutputStream(baos);
            try {
                record = SETTINGS_RECORD_PREFIX + SOUND_PARAM + (sound ? "true" : "false");
                outputStream.writeUTF(record);
                soundRecord = baos.toByteArray();
            } catch(Exception e){
            } finally {
                try {
                    outputStream.close();
                } catch(Exception e){}
                try {
                    baos.close();
                } catch(Exception e){}
            }

            // build fullscreen record
            baos = new  ByteArrayOutputStream();
            outputStream = new DataOutputStream(baos);
            try {
                record = SETTINGS_RECORD_PREFIX + FULLSCREEN_PARAM + (fullscreen ? "true" : "false");
                outputStream.writeUTF(record);
                fullscreenRecord = baos.toByteArray();
            } catch(Exception e){
            } finally {
                try {
                    outputStream.close();
                } catch(Exception e){}
                try {
                    baos.close();
                } catch(Exception e){}
            }

            // build rotate record
            baos = new  ByteArrayOutputStream();
            outputStream = new DataOutputStream(baos);
            try {
                record = SETTINGS_RECORD_PREFIX + ROTATE_PARAM + (rotate ? "true" : "false");
                outputStream.writeUTF(record);
                invertRecord = baos.toByteArray();
            } catch(Exception e){
            } finally {
                try {
                    outputStream.close();
                } catch(Exception e){}
                try {
                    baos.close();
                } catch(Exception e){}
            }

            // build invert key record
            baos = new  ByteArrayOutputStream();
            outputStream = new DataOutputStream(baos);
            try {
                record = SETTINGS_RECORD_PREFIX + INVERT_KEYS_PARAM + (invertKeys ? "true" : "false");
                outputStream.writeUTF(record);
                rotateRecord = baos.toByteArray();
            } catch(Exception e){
            } finally {
                try {
                    outputStream.close();
                } catch(Exception e){}
                try {
                    baos.close();
                } catch(Exception e){}
            }

            // update or save records
            int soundIdx = -1;
            int fullscreenIdx = -1;
            int rotateIdx = -1;
            int invertIdx = -1;
            records = settingsDB.enumerateRecords(new SettingsRecordFilter(), null, true);
            while(records.hasNextElement()) {
                int index = records.nextRecordId();
                ByteArrayInputStream bais1 = new ByteArrayInputStream(settingsDB.getRecord(index));
                DataInputStream dis1 = new DataInputStream(bais1);
                String actRecord = dis1.readUTF();

                // strip prefix
                actRecord = actRecord.substring(SETTINGS_RECORD_PREFIX.length());
                if(actRecord.startsWith(SOUND_PARAM)) {
                    soundIdx = index;
                }
                else if(actRecord.startsWith(FULLSCREEN_PARAM)) {
                    fullscreenIdx = index;
                }
                else if(actRecord.startsWith(ROTATE_PARAM)) {
                    rotateIdx = index;
                }
                else if(actRecord.startsWith(INVERT_KEYS_PARAM)) {
                    invertIdx = index;
                }
            }

            if(soundIdx < 0) {
                settingsDB.addRecord(soundRecord, 0, soundRecord.length);
            }
            else {
                settingsDB.setRecord(soundIdx, soundRecord, 0, soundRecord.length);
            }
            if(fullscreenIdx < 0) {
                settingsDB.addRecord(fullscreenRecord, 0, fullscreenRecord.length);
            }
            else {
                settingsDB.setRecord(fullscreenIdx, fullscreenRecord, 0, fullscreenRecord.length);
            }
            if(rotateIdx < 0) {
                settingsDB.addRecord(rotateRecord, 0, rotateRecord.length);
            }
            else {
                settingsDB.setRecord(rotateIdx, rotateRecord, 0, rotateRecord.length);
            }
            if(invertIdx < 0) {
                settingsDB.addRecord(invertRecord, 0, invertRecord.length);
            }
            else {
                settingsDB.setRecord(invertIdx, invertRecord, 0, invertRecord.length);
            }

        } catch (Exception ex) {
            SpaceHulk.instance.showException("Settings::saveSettings", ex);
        } finally {
            if(records != null) {
                records.destroy();
            }
        }
    }
}
