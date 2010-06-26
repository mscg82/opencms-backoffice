/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.util.connection.cookie;

import com.mscg.util.net.URL;
import java.util.Date;

/**
 *
 * @author Giuseppe Miscione
 */
public class CookieSpec {

    /** Path delimiter */
    static final public String PATH_DELIM = "/";

    /** Path delimiting charachter */
    static final public char PATH_DELIM_CHAR = PATH_DELIM.charAt(0);

    public static boolean match(Cookie cookie, String uri) {
        boolean ret = false;
        try {
            if(!cookie.isExpired(new Date())) {
                URL url = new URL(uri);
                String domain = url.getHost();
                String path = url.getPath();
                if(path == null) {
                    path = "";
                }
                if(!path.startsWith("/")) {
                    path = "/" + path;
                }
                ret = domain.equals(cookie.getDomain()) && path.startsWith(cookie.getPath());
            }
        } catch(Exception e) {

        }
        return ret;
    }

}
