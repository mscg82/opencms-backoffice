package com.mscg.authproviders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.auth.NTLMScheme;
import org.apache.commons.httpclient.auth.RFC2617Scheme;

import com.mscg.authproviders.bean.CredentialBean;
import com.mscg.config.ConfigLoader;

public class ConfigFileAuthProvider implements CredentialsProvider {

	private Map<String, CredentialBean> credentials;
	private int maxRequests;
	private int requests;

	public ConfigFileAuthProvider() {
        super();

        Map<String, Object> configs = ConfigLoader.getInstance();

        requests = 0;
        maxRequests = 5;
        try{
        	maxRequests = Integer.parseInt((String)configs.get("auth-provider.max-requests"));
        } catch(NumberFormatException e){}

        credentials = new HashMap<String, CredentialBean>();

        List<String> hostnames = null;
        List<String> domains = null;
        List<String> usernames = null;
        List<String> passwords = null;

        try{
        	hostnames = (List<String>) configs.get("auth-provider.hosts.host.name");
        } catch(ClassCastException e){
        	hostnames = new ArrayList<String>(1);
        	hostnames.add((String) configs.get("auth-provider.hosts.host.name"));
        }

        try{
        	domains = (List<String>) configs.get("auth-provider.hosts.host.domain");
        } catch(ClassCastException e){
        	domains = new ArrayList<String>(1);
        	domains.add((String) configs.get("auth-provider.hosts.host.domain"));
        }

        try{
        	usernames = (List<String>) configs.get("auth-provider.hosts.host.username");
        } catch(ClassCastException e){
        	usernames = new ArrayList<String>(1);
        	usernames.add((String) configs.get("auth-provider.hosts.host.username"));
        }

        try{
        	passwords = (List<String>) configs.get("auth-provider.hosts.host.password");
        } catch(ClassCastException e){
        	passwords = new ArrayList<String>(1);
        	passwords.add((String) configs.get("auth-provider.hosts.host.password"));
        }

        for(int i = 0, l = hostnames.size(); i < l; i++){
        	CredentialBean bean = new CredentialBean();
        	bean.setHostName(hostnames.get(i));
        	bean.setDomain(domains.get(i));
        	bean.setUsername(usernames.get(i));
        	bean.setPassword(passwords.get(i));

        	credentials.put(bean.getHostName(), bean);
        }
    }

    public Credentials getCredentials(
        final AuthScheme authscheme,
        final String host,
        int port,
        boolean proxy)
        throws CredentialsNotAvailableException
    {
        if (authscheme == null) {
            return null;
        }

    	CredentialBean credential = credentials.get(host + ":" + port);
    	if(credential == null){
    		throw new CredentialsNotAvailableException("Credentials for host \"" + host + ":" +
    			port + "\" not availables.");
    	}
    	else{
    		requests++;
    		if(requests >= maxRequests)
    			throw new CredentialsNotAvailableException("Credentials for host \"" + host + ":" +
    				port + " don't allow authentication.");
    	}

        if (authscheme instanceof NTLMScheme) {
            return new NTCredentials(credential.getUsername(), credential.getPassword(), host, credential.getDomain());
        } else
        if (authscheme instanceof RFC2617Scheme) {
            return new UsernamePasswordCredentials(credential.getUsername(), credential.getPassword());
        } else {
            throw new CredentialsNotAvailableException("Unsupported authentication scheme: " +
                authscheme.getSchemeName());
        }

    }

}
