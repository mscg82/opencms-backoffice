package main.settings;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.rms.RecordFilter;

/**
 *
 * @author Giuseppe Miscione
 */
public class SettingsRecordFilter implements RecordFilter {

    public boolean matches(byte[] candidate) {
        try {
            ByteArrayInputStream bais1 = new ByteArrayInputStream(candidate);
            DataInputStream dis1 = new DataInputStream(bais1);
            String record = dis1.readUTF();

            return record.startsWith(Settings.SETTINGS_RECORD_PREFIX);
        } catch (IOException ex) {
            return false;
        }
    }

}
