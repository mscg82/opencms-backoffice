/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.util.connection;

import com.mscg.util.connection.cookie.Cookie;
import java.util.Vector;

/**
 *
 * @author Giuseppe Miscione
 */
public class HttpState {

    private Vector cookies;

    public HttpState() {
        cookies = new Vector();
    }

    public synchronized void addCookie(Cookie cookie) {
        if (cookie != null) {
            // first remove any old cookie that is equivalent
            for (int i = 0, l = cookies.size(); i < l; i++) {
                Cookie tmp = (Cookie) cookies.elementAt(i);
                if (cookie.equals(tmp)) {
                    cookies.removeElementAt(i);
                    break;
                }
            }
            if (!cookie.isExpired()) {
                cookies.addElement(cookie);
            }
        }

    }

    public synchronized Cookie[] getCookies() {
        Cookie ret[] = new Cookie[cookies.size()];
        for(int i = 0, l = cookies.size(); i < l; i++) {
            ret[i] = (Cookie) cookies.elementAt(i);
        }
        return ret;
    }

    public synchronized void clearCookies() {
        cookies.removeAllElements();
    }
}
