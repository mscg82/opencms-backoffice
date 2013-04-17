package com.mscg.retrieve.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.mscg.httpinterface.IpAddressInterface;
import com.mscg.retrieve.IPRetriever;
import com.mscg.util.Util;

public abstract class DelegatingAddressRetriever implements IPRetriever {

    protected final Logger log;

    protected Pattern pattern;
    protected Pattern localIPPattern;
    protected IpAddressInterface ipaddrintrf;

    protected DelegatingAddressRetriever() {
        log = Logger.getLogger(this.getClass());
        localIPPattern = Pattern.compile("(^127\\.)|(^192\\.168\\.)|(^10\\.)|(^172\\.1[6-9]\\.)|(^172\\.2[0-9]\\.)|(^172\\.3[0-1]\\.)|(^::1$)");
        initPattern();
        try {
            initIPAddressInterface();
        } catch(Exception e) {
            log.error("Cannot init IP address interface (" + e.getClass().getCanonicalName() + "): " + e.getMessage());
            Util.logStackTrace(e, log);
        }
    }

    protected abstract void initPattern();
    protected abstract int getPatternGroupIndex();

    protected abstract void initIPAddressInterface() throws Exception;

    public List<String> retrieveIPs() {
        List<String> IPs = new LinkedList<String>();
        if(ipaddrintrf != null) {
            try {
                String resp = ipaddrintrf.getRetrievedIpPageContent();
                Matcher matcher = pattern.matcher(resp);
                Matcher localIpMatcher = null;
                while(matcher.find()){
                    String ipaddress = matcher.group(getPatternGroupIndex());
                    localIpMatcher = localIPPattern.matcher(ipaddress);
                    if(!localIpMatcher.find())
                        IPs.add(ipaddress);
                }

            } catch (Exception e) {
                log.error("Error found while reading IP (" + e.getClass().getCanonicalName() + "): " + e.getMessage());
                Util.logStackTrace(e, log);
                IPs.clear();
            }
        }
        return IPs;
    }

}
