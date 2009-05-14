package com.mscg;

/*
 * $HeadURL$
 * $Revision$
 * $Date$
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.auth.NTLMScheme;
import org.apache.commons.httpclient.auth.RFC2617Scheme;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * A simple example that uses HttpClient to perform interactive
 * authentication.
 *
 * @author Oleg Kalnichevski
 */
public class InteractiveAuthenticationExample {

	private BufferedReader in = null;

	/**
     * Constructor for InteractiveAuthenticationExample.
     */
    public InteractiveAuthenticationExample() {
        super();
    }

    public static void main(String[] args) throws Exception {

        InteractiveAuthenticationExample demo = new InteractiveAuthenticationExample();
        demo.doDemo(args);
    }

    private void doDemo(String[] args) throws IOException {

    	if(args.length >= 2){

    		HttpClient client = new HttpClient();
	        if(args.length == 4){
	        	client.getHostConfiguration().setProxy(args[2], Integer.parseInt(args[3]));
	        }
	        client.getParams().setParameter(CredentialsProvider.PROVIDER, new ConsoleAuthPrompter());
	        GetMethod httpget = new GetMethod(args[0]);
	        httpget.setDoAuthentication(true);

	        File outFile = new File(args[1]);
	        OutputStream os = new FileOutputStream(outFile);
	        InputStream is = null;

	        try {
	        	System.out.println("Downloading file...");
	        	// execute the GET
	            int status = client.executeMethod(httpget);
	            // print the status and response
	            System.out.println("Response code:   " + status);
	            System.out.println("Response status: " + httpget.getStatusLine().toString());

	            is = httpget.getResponseBodyAsStream();
	            byte buffer[] = new byte[4096];
	            int byteRead = 0;
	            int totalBytes = 0;
	            do{
	            	byteRead = is.read(buffer);
	            	if(byteRead > 0){
	            		os.write(buffer, 0, byteRead);
	            		totalBytes += byteRead;
	            	}
	            } while(byteRead > 0);

	            System.out.println("File downloaded successfully. " + totalBytes + " bytes transferred from server.");

	        } catch(Exception e){
	        	e.printStackTrace();
	        } finally {
	        	if(is != null){
	            	try{
	            		is.close();
	            	} catch(IOException e){ }
	            }

	            // release any connection resources used by the method
	            httpget.releaseConnection();

	            try{
	            	os.flush();
	            } catch(IOException e){ }

	            try{
	            	os.close();
	            } catch(IOException e){ }
	        }
    	}
    	else{
    		System.out.println("Usage: java -jar proxy-test.jar <complete_url_to_download> <outfile> [<proxy_server> <proxy_port>]");
    	}
    }

    private String readConsole() throws IOException {
        return in.readLine();
    }

    public class ConsoleAuthPrompter implements CredentialsProvider {

        public ConsoleAuthPrompter() {
            super();
            in = new BufferedReader(new InputStreamReader(System.in));
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
                    String domain = readConsole();
                    System.out.print("Enter username: ");
                    String user = readConsole();
                    System.out.print("Enter password: ");
                    String password = readConsole();
                    return new NTCredentials(user, password, host, domain);
                } else
                if (authscheme instanceof RFC2617Scheme) {
                    System.out.println(host + ":" + port + " requires authentication with the realm '"
                        + authscheme.getRealm() + "'");
                    System.out.print("Enter username: ");
                    String user = readConsole();
                    System.out.print("Enter password: ");
                    String password = readConsole();
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
}

