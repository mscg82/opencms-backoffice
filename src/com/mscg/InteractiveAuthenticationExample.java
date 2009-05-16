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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.methods.GetMethod;

import com.mscg.config.ConfigLoader;

/**
 * A simple example that uses HttpClient to perform interactive
 * authentication.
 *
 * @author Oleg Kalnichevski
 */
public class InteractiveAuthenticationExample {

	/**
     * Constructor for InteractiveAuthenticationExample.
     */
    public InteractiveAuthenticationExample() {
        super();
    }

    public static void main(String[] args) throws Exception {

        InteractiveAuthenticationExample demo = new InteractiveAuthenticationExample();

        ConfigLoader.initInstance();

        demo.doDemo(args);
    }

    private void doDemo(String[] args) throws IOException {

    	if(args.length >= 2){

    		HttpClient client = new HttpClient();
	        if(args.length == 4){
	        	client.getHostConfiguration().setProxy(args[2], Integer.parseInt(args[3]));
	        }

	        File outFile = new File(args[1]);
	        OutputStream os = new FileOutputStream(outFile);
	        InputStream is = null;
	        GetMethod httpget = null;

	        try {
	        	Class authProviderClass = Class.forName((String) ConfigLoader.getInstance().get("auth-provider.class"));
	        	CredentialsProvider provider = (CredentialsProvider)authProviderClass.getConstructor().newInstance();

		        client.getParams().setParameter(CredentialsProvider.PROVIDER, provider);

		        httpget = new GetMethod(args[0]);
		        httpget.setDoAuthentication(true);

	        	System.out.println("Downloading file...");
	        	// execute the GET
	            int status = client.executeMethod(httpget);
	            // print the status and response
	            System.out.println("Response code:   " + status);
	            System.out.println("Response status: " + httpget.getStatusLine().toString());

	            if(status != HttpStatus.SC_OK){
	            	System.out.println("Cannot download file from server.");
	            }
	            else{
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
	            }

	        } catch(Exception e){
	        	e.printStackTrace();
	        } finally {
	        	if(is != null){
	            	try{
	            		is.close();
	            	} catch(IOException e){ }
	            }

	        	if(httpget != null){
		            // release any connection resources used by the method
		            httpget.releaseConnection();
	        	}

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
}

