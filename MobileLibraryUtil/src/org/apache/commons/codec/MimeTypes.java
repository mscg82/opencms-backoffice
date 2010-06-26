/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.commons.codec;

import com.mscg.util.net.URL;
import java.util.Hashtable;

/**
 *
 * @author Giuseppe Miscione
 */
public class MimeTypes {

    private static Hashtable mimeTypes;

    static {
        mimeTypes = new Hashtable();

        mimeTypes.put("xml",	"text/xml");
        mimeTypes.put("jpg",	"image/jpeg");
        mimeTypes.put("jpeg",	"image/jpeg");
        mimeTypes.put("gif",	"image/gif");
        mimeTypes.put("bmp",	"image/bmp");
        mimeTypes.put("png",	"image/png");
        mimeTypes.put("swf",	"application/x-shockwave-flash");
        mimeTypes.put("mp3",	"audio/mpeg3");
        mimeTypes.put("psd",	"application/octet-stream");
        mimeTypes.put("flv",	"application/octet-stream");
        mimeTypes.put("mpg",	"video/mpeg");
        mimeTypes.put("mpeg",	"video/mpeg");
        mimeTypes.put("mov",	"video/quicktime");
        mimeTypes.put("avi",	"video/avi");
        mimeTypes.put("wmv",	"audio/x-ms-wmv");
        mimeTypes.put("mp4",	"video/mp4");
        mimeTypes.put("wav", 	"audio/x-wav");
    }

    public static String getMimeType(String filename) {
        String ret = null;
        String ext = filename.substring(filename.lastIndexOf('.') + 1);
        if(mimeTypes.containsKey(ext))
            ret = (String) mimeTypes.get(ext);
        return ret;
    }

    public static String getMimeType(URL url) {
        return getMimeType(url.getFile());
    }
}
