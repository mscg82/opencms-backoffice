/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.util.connection.cookie;

import com.mscg.util.lang.LangUtils;
import com.mscg.util.connection.NameValuePair;
import java.util.Date;

/**
 *
 * @author Giuseppe Miscione
 */
public class Cookie extends NameValuePair {

    private String domain;
    private String path;
    private Date expiryDate;
    private boolean secure;
    private int cookieVersion;

    public Cookie() {
        this(null, null, null, null, null, false);
    }

    public Cookie(String domain, String name, String value, String path) {
        this(domain, name, value, path, null, false);
    }

    public Cookie(String domain, String name, String value, String path, Date date, boolean secure) {
        super(name, value);
        setDomain(domain);
        setPath(path);
        setExpiryDate(date);
        setSecure(secure);
    }

    public Cookie(String cookieString) {
        super(null, null);
        int start = 0;
        while(start < cookieString.length()) {
            int end = cookieString.indexOf(";", start);
            if(end == -1) {
                end = cookieString.length();
            }
            String part = cookieString.substring(start, end);
            int eqIndex = part.indexOf("=");
            if(eqIndex == -1) {
                eqIndex = part.length();
            }
            String partName = part.substring(0, eqIndex).trim();
            String partValue = part.substring(eqIndex + 1);

            if(partName.equals("expires")) {
                // set expire date
            }
            else if(partName.equals("domain")) {
                // set the cookie domain
                this.setDomain(partValue);
            }
            else if(partName.equals("path")) {
                // set the cookie path
                this.setPath(partValue);
            }
            else if(partName.equals("secure")) {
                // set the cookie as secure
                this.setSecure(true);
            }
            else {
                this.setName(partName);
                this.setValue(partValue);
            }

            start = end + 1;
        }
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date date) {
        this.expiryDate = date;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    /**
     * Returns the version of the cookie specification to which this
     * cookie conforms.
     *
     * @return the version of the cookie.
     *
     * @see #setVersion(int)
     *
     */
    public int getVersion() {
        return cookieVersion;
    }

    /**
     * Sets the version of the cookie specification to which this
     * cookie conforms.
     *
     * @param version the version of the cookie.
     *
     * @see #getVersion
     */
    public void setVersion(int version) {
        cookieVersion = version;
    }

    /**
     * Returns true if this cookie has expired.
     *
     * @return <tt>true</tt> if the cookie has expired.
     */
    public boolean isExpired() {
        return (expiryDate != null  && expiryDate.getTime() <= System.currentTimeMillis());
    }

    /**
     * Returns true if this cookie has expired according to the time passed in.
     *
     * @param now The current time.
     *
     * @return <tt>true</tt> if the cookie expired.
     */
    public boolean isExpired(Date now) {
        return (expiryDate != null && expiryDate.getTime() <= now.getTime());
    }

    /**
     * Returns <tt>false</tt> if the cookie should be discarded at the end
     * of the "session"; <tt>true</tt> otherwise.
     *
     * @return <tt>false</tt> if the cookie should be discarded at the end
     *         of the "session"; <tt>true</tt> otherwise
     */
    public boolean isPersistent() {
        return (null != getExpiryDate());
    }


    /**
     * Returns a hash code in keeping with the
     * {@link Object#hashCode} general hashCode contract.
     * @return A hash code
     */
    public int hashCode() {
        int hash = LangUtils.HASH_SEED;
        hash = LangUtils.hashCode(hash, this.getName());
        hash = LangUtils.hashCode(hash, this.getDomain());
        hash = LangUtils.hashCode(hash, this.getPath());
        return hash;
    }


    /**
     * Two cookies are equal if the partName, path and domain match.
     * @param obj The object to compare against.
     * @return true if the two objects are equal.
     */
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj instanceof Cookie) {
            Cookie that = (Cookie) obj;
            return LangUtils.equals(this.getName(), that.getName())
                  && LangUtils.equals(this.getDomain(), that.getDomain())
                  && LangUtils.equals(this.getPath(), that.getPath());
        } else {
            return false;
        }
    }

    public int compareTo(Object o2) {
        if (!(o2 instanceof Cookie)) {
            throw new ClassCastException(o2.getClass().getName());
        }
        Cookie c2 = (Cookie) o2;
        if (this.getPath() == null && c2.getPath() == null) {
            return 0;
        } else if (this.getPath() == null) {
            // null is assumed to be "/"
            if (c2.getPath().equals(CookieSpec.PATH_DELIM)) {
                return 0;
            } else {
                return -1;
            }
        } else if (c2.getPath() == null) {
            // null is assumed to be "/"
            if (this.getPath().equals(CookieSpec.PATH_DELIM)) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return this.getPath().compareTo(c2.getPath());
        }
    }

    public String toString() {
        return getName() + "=" + getValue();
    }




}
