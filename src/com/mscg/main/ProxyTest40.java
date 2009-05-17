/**
 *
 */
package com.mscg.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.mscg.scheme.NTLMSchemeFactory;

/**
 * @author Giuseppe Miscione
 *
 */
public class ProxyTest40 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String server = "62.173.184.8";
		int port = 80;
		String domain = "cem";
		String username = "telecomxml";
		String password = "EwDm1DiH";
		String file = "telecomxml/capoluoghi.zip";

		AbstractHttpClient client = new DefaultHttpClient();
		client.getAuthSchemes().register(AuthPolicy.NTLM, new NTLMSchemeFactory());
		client.getCredentialsProvider().setCredentials(
		    new AuthScope(server, port),
			new NTCredentials(username, password, server, domain));

		HttpGet httpget = new HttpGet("http://" + server + ":" + port + "/" + file);

        File outFile = new File("capoluoghi.zip");
        OutputStream os = null;
        InputStream is = null;

        try{
        	os = new FileOutputStream(outFile);

        	System.out.println("Downloading file...");
        	// execute the GET
            HttpResponse response = client.execute(httpget);
            // print the status and response
            int code = response.getStatusLine().getStatusCode();
            System.out.println("Response code:   " + code);
            System.out.println("Response status: " + response.getStatusLine().toString());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();

	        if(entity == null || code != HttpStatus.SC_OK){
	        	System.out.println("Cannot download file from server.");
	        }
	        else{
	            is = entity.getContent();
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

        	if(httpget != null){
	            // release any connection resources used by the method
	            httpget.abort();
        	}
        } finally {
        	if(is != null){
            	try{
            		is.close();
            	} catch(IOException e){ }
            }

        	if(os != null){
	            try{
	            	os.flush();
	            } catch(IOException e){ }

	            try{
	            	os.close();
	            } catch(IOException e){ }
        	}
        }


	}

}
