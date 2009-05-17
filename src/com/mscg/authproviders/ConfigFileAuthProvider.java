package com.mscg.authproviders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.CredentialException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.params.AuthPolicy;

import com.mscg.authproviders.bean.CredentialBean;
import com.mscg.config.ConfigLoader;

public class ConfigFileAuthProvider implements CredentialsProvider {

	private Map<String, CredentialBean> credentials;

	public ConfigFileAuthProvider() {
        super();

        Map<String, Object> configs = ConfigLoader.getInstance();

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

	public void clear() {
		// do nothing
	}

	public Credentials getCredentials(AuthScope authscheme) {
		if (authscheme == null) {
            return null;
        }

		try{
	    	CredentialBean credential = credentials.get(authscheme.getHost() + ":" + authscheme.getPort());
	    	if(credential == null){
	    		throw new CredentialException("Credentials for host \"" + authscheme.getHost() + ":" +
	    				authscheme.getPort() + "\" not availables.");
	    	}

	        if (authscheme.getScheme().equalsIgnoreCase(AuthPolicy.NTLM)) {
	            return new NTCredentials(credential.getUsername(), credential.getPassword(), authscheme.getHost(), credential.getDomain());
	        } else
	        if (authscheme.getScheme().equalsIgnoreCase(AuthPolicy.DIGEST)) {
	            return new UsernamePasswordCredentials(credential.getUsername(), credential.getPassword());
	        }if (authscheme.getScheme().equalsIgnoreCase(AuthPolicy.BASIC)) {
	            return new UsernamePasswordCredentials(credential.getUsername(), credential.getPassword());
	        } else {
	            throw new CredentialException("Unsupported authentication scheme: " +
	                authscheme.getScheme());
	        }
		} catch(CredentialException e){
			e.printStackTrace();
			return null;
		}
	}

	public void setCredentials(AuthScope authscheme, Credentials credentials) {

		// do nothing

	}

}
