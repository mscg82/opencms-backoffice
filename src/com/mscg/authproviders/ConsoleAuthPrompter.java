package com.mscg.authproviders;

import java.io.IOException;

import javax.security.auth.login.CredentialNotFoundException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.params.AuthPolicy;

import com.mscg.console.ConsoleReader;

public class ConsoleAuthPrompter implements CredentialsProvider {

    public ConsoleAuthPrompter() {
        super();
    }

    public Credentials getCredentials(
        final AuthScope authscheme,
        final String host,
        int port,
        boolean proxy)
        throws CredentialNotFoundException
    {
        if (authscheme == null) {
            return null;
        }
        try{
            if (authscheme.getScheme().equalsIgnoreCase(AuthPolicy.NTLM)) {
                System.out.println(host + ":" + port + " requires Windows authentication");
                System.out.print("Enter domain: ");
                String domain = ConsoleReader.readConsole();
                System.out.print("Enter username: ");
                String user = ConsoleReader.readConsole();
                System.out.print("Enter password: ");
                String password = ConsoleReader.readConsole();
                return new NTCredentials(user, password, host, domain);
            } else
            if(authscheme.getScheme().equalsIgnoreCase(AuthPolicy.DIGEST) ||
               authscheme.getScheme().equalsIgnoreCase(AuthPolicy.BASIC)) {
                System.out.println(host + ":" + port + " requires authentication with the realm '"
                    + authscheme.getRealm() + "'");
                System.out.print("Enter username: ");
                String user = ConsoleReader.readConsole();
                System.out.print("Enter password: ");
                String password = ConsoleReader.readConsole();
                return new UsernamePasswordCredentials(user, password);
            } else {
                throw new CredentialNotFoundException("Unsupported authentication scheme: " +
                    authscheme.getScheme());
            }
        } catch (IOException e) {
            throw new CredentialNotFoundException(e.getMessage());
        }
    }

	public void clear() {
		// do nothing
	}

	public Credentials getCredentials(AuthScope authscheme) {
		try {
			return getCredentials(authscheme, authscheme.getHost(), authscheme.getPort(), false);
		} catch (CredentialNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setCredentials(AuthScope authscheme, Credentials credentials) {
		// do nothing
	}
}
