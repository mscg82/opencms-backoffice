package com.mscg.httpinterface;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

import com.mscg.config.ConfigLoader;
import com.mscg.util.Util;
import com.mscg.util.passwordreader.PasswordReader;
import com.mscg.util.passwordreader.impl.PlainPasswordReader;

public class NetgearRouterAddressInterface extends AbstractIpAddressInterface {

    private String routerPageUrl;

    public NetgearRouterAddressInterface() throws ConfigurationException {
        super();

        Map<String, Object> params = ConfigLoader.getInstance();

        PasswordReader pwdReader = (PasswordReader) Util.loadClass((String) config.get(ConfigLoader.NETGEAR_ROUTER_PASSWORD_READER),
                                                    this.getClass().getClassLoader());
        if(pwdReader == null){
            // default password reader
            pwdReader = new PlainPasswordReader();
        }

        routerPageUrl = (String)params.get(ConfigLoader.NETGEAR_ROUTER_URL);
        String username = (String)params.get(ConfigLoader.NETGEAR_ROUTER_USERNAME);
        String password = pwdReader.readPassword((String)params.get(ConfigLoader.NETGEAR_ROUTER_PASSWORD));
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM), defaultcreds);
    }

    public String getRetrievedIpPageContent() throws HttpException, IOException {
        return getRetrievedIpPageContent(routerPageUrl);
    }
}
