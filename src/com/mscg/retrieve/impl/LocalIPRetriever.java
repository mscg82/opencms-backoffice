package com.mscg.retrieve.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.mscg.retrieve.IPRetriever;
import com.mscg.util.Util;

public class LocalIPRetriever implements IPRetriever {

    private static Logger log = Logger.getLogger(LocalIPRetriever.class);

    public List<String> retrieveIPs() {
        List<String> IPs = new LinkedList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()) {
                NetworkInterface interfaze = interfaces.nextElement();
                Enumeration<InetAddress> addresses = interfaze.getInetAddresses();
                while(addresses.hasMoreElements()){
                    InetAddress address = addresses.nextElement();
                    IPs.add(address.getHostAddress());
                }
            }
        } catch (IOException e) {
            log.error("Error found while reading IP(" + e.getClass().getCanonicalName() + "): " + e.getMessage());
            Util.logStackTrace(e, log);
            IPs.clear();
        }

        return IPs;
    }

}
