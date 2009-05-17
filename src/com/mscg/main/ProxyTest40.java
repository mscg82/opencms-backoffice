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
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.mscg.config.ConfigLoader;
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

		AbstractHttpClient client = null;
		if(args.length >= 2){
			try {
				ConfigLoader.initInstance();

				// general setup
		        SchemeRegistry supportedSchemes = new SchemeRegistry();

		        // Register the "http" and "https" protocol schemes, they are
		        // required by the default operator to look up socket factories.
		        supportedSchemes.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		        supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

		        // prepare parameters
		        HttpParams params = new BasicHttpParams();
		        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		        HttpProtocolParams.setContentCharset(params, "UTF-8");
		        HttpProtocolParams.setUseExpectContinue(params, true);

		        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, supportedSchemes);

				client = new DefaultHttpClient(ccm, params);

				if(args.length == 4){
					HttpHost proxy = new HttpHost(args[2], Integer.parseInt(args[3]), "http");
					client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
				}

				Class authProviderClass = Class.forName((String) ConfigLoader.getInstance().get("auth-provider.class"));
	        	CredentialsProvider provider = (CredentialsProvider)authProviderClass.getConstructor().newInstance();

	        	 // Create local HTTP context
	            HttpContext localContext = new BasicHttpContext();
	            localContext.setAttribute(ClientContext.CREDS_PROVIDER, provider);

				client.getAuthSchemes().register(AuthPolicy.NTLM, new NTLMSchemeFactory());

				HttpGet httpget = new HttpGet(args[0]);

		        File outFile = new File(args[1]);
		        OutputStream os = null;
		        InputStream is = null;

		        try{
		        	os = new FileOutputStream(outFile);

		        	System.out.println("Downloading file...");
		        	// execute the GET
		            HttpResponse response = client.execute(httpget, localContext);
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

			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(client != null){
					client.getConnectionManager().shutdown();
				}
			}
		}
		else{
			System.out.println("Usage: java -jar proxy-test40.jar <complete_url_to_download> <outfile> [<proxy_server> <proxy_port>]");
		}
	}

}
