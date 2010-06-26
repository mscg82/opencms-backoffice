/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.util.connection;

/**
 *
 * @author Giuseppe Miscione
 */
public class NameValuePair {

    protected String name;
    protected String value;

    public NameValuePair(String name, String value) {
        setName(name);
        setValue(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
