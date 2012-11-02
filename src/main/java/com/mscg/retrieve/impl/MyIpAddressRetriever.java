package com.mscg.retrieve.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.mscg.httpinterface.MyIpAddressInterface;
import com.mscg.retrieve.IPRetriever;
import com.mscg.util.Util;

public class MyIpAddressRetriever implements IPRetriever {

    private static Logger log = Logger.getLogger(MyIpAddressRetriever.class);

    private final Pattern pattern;

    public MyIpAddressRetriever() {
        super();
        pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    }

    public List<String> retrieveIPs() {
        List<String> IPs = new LinkedList<String>();
        try {
            MyIpAddressInterface ipaddrintrf = new MyIpAddressInterface();
            String resp = ipaddrintrf.getMyIpResponse();
            Matcher matcher = pattern.matcher(resp);
            while(matcher.find()){
                IPs.add(matcher.group());
            }

        } catch (Exception e) {
            log.error("Error found while reading IP (" + e.getClass().getCanonicalName() + "): " + e.getMessage());
            Util.logStackTrace(e, log);
            IPs.clear();
        }
        return IPs;
    }

}
