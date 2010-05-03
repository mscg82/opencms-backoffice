package main.settings;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.rms.RecordComparator;

/**
 *
 * @author Giuseppe Miscione
 */
public class GenericRecordComparator implements RecordComparator {

    public int compare(byte[] rec1, byte[] rec2) {
        ByteArrayInputStream bais1 = new ByteArrayInputStream(rec1);
        DataInputStream dis1 = new DataInputStream(bais1);
        ByteArrayInputStream bais2 = new ByteArrayInputStream(rec2);
        DataInputStream dis2 = new DataInputStream(bais2);

        try {
            String name1 = dis1.readUTF();
            String name2 = dis2.readUTF();
            int num = name1.compareTo(name2);
            if (num > 0) {
                return RecordComparator.FOLLOWS;
            } else if (num < 0) {
                return RecordComparator.PRECEDES;
            } else {
                return RecordComparator.EQUIVALENT;
            }
        } catch(IOException e) {
            return RecordComparator.PRECEDES;
        }

    }
}
