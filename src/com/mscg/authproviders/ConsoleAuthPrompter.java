package com.mscg.authproviders;

import java.io.IOException;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.auth.NTLMScheme;
import org.apache.commons.httpclient.auth.RFC2617Scheme;

import com.mscg.console.ConsoleReader;

public class ConsoleAuthPrompter implements CredentialsProvider {

    public ConsoleAuthPrompter() {
        super();
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
        try{
            if (authscheme instanceof NTLMScheme) {
                System.out.println(host + ":" + port + " requires Windows authentication");
                System.out.print("Enter domain: ");
                String domain = ConsoleReader.readConsole();
                System.out.print("Enter username: ");
                String user = ConsoleReader.readConsole();
                System.out.print("Enter password: ");
                String password = ConsoleReader.readConsole();
                return new NTCredentials(user, password, host, domain);
            } else
            if (authscheme instanceof RFC2617Scheme) {
                System.out.println(host + ":" + port + " requires authentication with the realm '"
                    + authscheme.getRealm() + "'");
                System.out.print("Enter username: ");
                String user = ConsoleReader.readConsole();
                System.out.print("Enter password: ");
                String password = ConsoleReader.readConsole();
                return new UsernamePasswordCredentials(user, password);
            } else {
                throw new CredentialsNotAvailableException("Unsupported authentication scheme: " +
                    authscheme.getSchemeName());
            }
        } catch (IOException e) {
            throw new CredentialsNotAvailableException(e.getMessage(), e);
        }
    }
}
