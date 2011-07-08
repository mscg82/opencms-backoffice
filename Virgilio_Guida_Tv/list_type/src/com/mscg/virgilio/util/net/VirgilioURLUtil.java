package com.mscg.virgilio.util.net;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class VirgilioURLUtil {

    public static final Map<Integer, String> URLS;
    public static final String DETAILS_URL = "http://cinema-tv.virgilio.it/services/guida-tv/flash/flash-${programID}.xml";

    static {
        URLS = new LinkedHashMap<Integer, String>(7);
        URLS.put(Calendar.MONDAY, "http://cinema-tv.virgilio.it/services/guida-tv/flash/palinsesto-lunedi.xml");
        URLS.put(Calendar.TUESDAY, "http://cinema-tv.virgilio.it/services/guida-tv/flash/palinsesto-martedi.xml");
        URLS.put(Calendar.WEDNESDAY, "http://cinema-tv.virgilio.it/services/guida-tv/flash/palinsesto-mercoledi.xml");
        URLS.put(Calendar.THURSDAY, "http://cinema-tv.virgilio.it/services/guida-tv/flash/palinsesto-giovedi.xml");
        URLS.put(Calendar.FRIDAY, "http://cinema-tv.virgilio.it/services/guida-tv/flash/palinsesto-venerdi.xml");
        URLS.put(Calendar.SATURDAY, "http://cinema-tv.virgilio.it/services/guida-tv/flash/palinsesto-sabato.xml");
        URLS.put(Calendar.SUNDAY, "http://cinema-tv.virgilio.it/services/guida-tv/flash/palinsesto-domenica.xml");
    }

}
