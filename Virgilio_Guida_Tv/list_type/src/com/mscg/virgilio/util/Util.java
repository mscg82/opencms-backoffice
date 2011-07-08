package com.mscg.virgilio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.collections.map.LazyMap;

import android.util.Log;

public class Util {

    public static final SimpleDateFormat programsDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat programsLastUpdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat programTimeFormat = new SimpleDateFormat("HH:mm");

    private static Map adaptedDates = LazyMap.decorate(new LRUMap(10), new DateAdapterTransformer());

    public static String capitalize(String orig) {
        return orig.substring(0, 1).toUpperCase() + orig.substring(1);
    }

    public static Date adaptDate(Date orig) {
        return (Date) adaptedDates.get(orig);
    }

    private static class DateAdapterTransformer implements Transformer {

        @Override
        public Object transform(Object input) {
            Date ret = null;
            Date orig = null;

            if (input instanceof Date)
                orig = (Date) input;
            if (input instanceof Calendar)
                orig = ((Calendar) input).getTime();

            if (orig != null) {
                String adapted = programsDateFormat.format(orig);
                try {
                    ret = programsDateFormat.parse(adapted);
                } catch (ParseException e) {
                    Log.e("mscg.Util", "Cannot parse date string", e);
                }
            }

            return ret;
        }

    }

}
