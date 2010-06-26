/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mscg.util.pim;

import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMList;

/**
 *
 * @author Giuseppe Miscione
 */
public class PIMSupportTester {

    /**
     * Test if the PIM API is supported.
     * @return true if the PIM API is supported, false otherwise
     */
    static public boolean isPIMApiSupported() {
        boolean isSupported = true;
        String pimApiVersion =
                System.getProperty("microedition.pim.version");
        if (pimApiVersion == null) {
            isSupported = false;
        }
        return isSupported;
    }

    /**
     * Helper method to test if calendar events database types are supported.
     *
     * @return true if calendar/events databases are supported, and false
     * if event databases are not supported or if permission to use use the
     * PIM API is denied.
     */
    static public boolean isEventListSupported() {
        boolean retVal;
        PIMList el = null;
        try {
            // Try to open the event list; this will tell us if it is supported
            el = PIM.getInstance().openPIMList(PIM.EVENT_LIST, PIM.READ_WRITE);
            retVal = true;
        } catch (SecurityException e) {
            retVal = false; // Unknown since access to API was denied
        } catch (PIMException e) {
            retVal = false;
        } finally {
            if (el != null) {
                try {
                    // Close the event list since we only opened it to
                    // see if it is supported.
                    el.close();
                } catch (PIMException ignore) {
                    // ignore
                }
            }
        }
        return retVal;
    }

    /**
     * Helper method to test if contacts database types are supported.
     *
     * @return true if contacts database is supported, and false
     * if contacts database is not supported or if permission to use use the
     * PIM API is denied.
     */
    static public boolean isContactsListSupported() {
        boolean retVal;
        PIMList el = null;
        try {
            // Try to open the contacts list; this will tell us if it is supported
            el = PIM.getInstance().openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);
            retVal = true;
        } catch (SecurityException e) {
            retVal = false; // Unknown since access to API was denied
        } catch (PIMException e) {
            retVal = false;
        } finally {
            if (el != null) {
                try {
                    // Close the event list since we only opened it to
                    // see if it is supported.
                    el.close();
                } catch (PIMException ignore) {
                    // ignore
                }
            }
        }
        return retVal;
    }
    
}
