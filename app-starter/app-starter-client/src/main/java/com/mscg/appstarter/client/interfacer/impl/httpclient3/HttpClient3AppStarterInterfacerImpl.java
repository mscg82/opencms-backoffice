package com.mscg.appstarter.client.interfacer.impl.httpclient3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.oxm.XmlMappingException;

import com.mscg.appstarter.beans.jaxb.ApplicationInfo;
import com.mscg.appstarter.beans.jaxb.Response;
import com.mscg.appstarter.beans.jaxb.ServerMessage;
import com.mscg.appstarter.beans.jaxb.Wrapper;
import com.mscg.appstarter.client.interfacer.GenericAppStarterInterfacer;
import com.mscg.appstarter.client.interfacer.InterfacerUrl;
import com.mscg.appstarter.exception.InvalidRequestException;
import com.mscg.appstarter.exception.InvalidResponseException;
import com.mscg.appstarter.util.ResponseCode;

public class HttpClient3AppStarterInterfacerImpl extends GenericAppStarterInterfacer {

    protected HttpClient httpClient;

    public HttpClient3AppStarterInterfacerImpl() {
        this(null);
    }

    public HttpClient3AppStarterInterfacerImpl(HttpClient httpClient) {
        setHttpClient(httpClient);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public boolean login(String username, String password) throws InvalidRequestException,
                                                                  InvalidResponseException,
                                                                  IOException {
        boolean ret = false;

        String url = baseUrl + InterfacerUrl.LOGIN_URL.getRelativeUrl();

        //first request, get the nonce
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setRequest(objectFactory.createRequest());
        wrapper.getRequest().setLogin(objectFactory.createLogin());
        wrapper.getRequest().getLogin().setUsername(username);

        Response response = sendRequest(url, wrapper);
        if(response.getLogin() == null)
            throw new InvalidResponseException("Missing login node", ResponseCode.ERR_APPLICATION_ERROR);

        // second request, log with encrypted password
        String nonce = response.getLogin().getNonce();
        String tempSessID = response.getLogin().getSessionID();
        String identifier = DigestUtils.md5Hex(username + nonce + DigestUtils.md5Hex(password));
        wrapper.getRequest().getLogin().setSessionID(tempSessID);
        wrapper.getRequest().getLogin().setIdentifier(identifier);

        try {
            response = sendRequest(url, wrapper);
            if(response.getLogin() == null)
                throw new InvalidResponseException("Missing login node", ResponseCode.ERR_APPLICATION_ERROR);
            // correct login, store session ID and return true
            usernameToSessionID.put(username, response.getLogin().getSessionID());
            ret = true;
        } catch(InvalidResponseException e) {
            if(e.getResponseCode() == ResponseCode.ERR_UNAUTHORIZED_ACCESS) {
                // wrong credentials, return false
                ret = false;
            }
            else
                throw e;
        }

        return ret;
    }

    public void logout(String username) throws InvalidRequestException,
                                               InvalidResponseException,
                                               IOException {

        String sessionID = usernameToSessionID.get(username);
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setRequest(objectFactory.createRequest());
        wrapper.getRequest().setLogin(objectFactory.createLogin());
        wrapper.getRequest().getLogin().setUsername(username);
        wrapper.getRequest().getLogin().setSessionID(sessionID);

        String url = baseUrl + InterfacerUrl.LOGOUT_URL.getRelativeUrl();

        sendRequest(url, wrapper);
        usernameToSessionID.remove(username);
    }

    public boolean ping(String username) throws InvalidRequestException,
                                                InvalidResponseException,
                                                IOException {

        boolean ret = false;

        String sessionID = usernameToSessionID.get(username);
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setRequest(objectFactory.createRequest());
        wrapper.getRequest().setLogin(objectFactory.createLogin());
        wrapper.getRequest().getLogin().setUsername(username);
        wrapper.getRequest().getLogin().setSessionID(sessionID);

        String url = baseUrl + InterfacerUrl.PING_URL.getRelativeUrl();

        try {
            sendRequest(url, wrapper);
            ret = true;
        } catch(InvalidResponseException e) {
            if(e.getResponseCode() == ResponseCode.ERR_INVALID_USER_SESSION) {
                // session expired
                usernameToSessionID.remove(username);
                ret = false;
            }
            else
                throw e;
        }

        return ret;
    }

    public List<ApplicationInfo> listApplications(String username) throws InvalidRequestException,
                                                                          InvalidResponseException,
                                                                          IOException {
        List<ApplicationInfo> ret = null;

        String sessionID = usernameToSessionID.get(username);
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setRequest(objectFactory.createRequest());
        wrapper.getRequest().setLogin(objectFactory.createLogin());
        wrapper.getRequest().getLogin().setUsername(username);
        wrapper.getRequest().getLogin().setSessionID(sessionID);

        String url = baseUrl + InterfacerUrl.LIST_APP_URL.getRelativeUrl();

        Response response = sendRequest(url, wrapper);
        if(response.getApplications() == null)
            throw new InvalidRequestException("Missing applications list node",
                                              ResponseCode.ERR_APPLICATION_ERROR);

        ret = response.getApplications().getApplication();

        return ret;
    }

    public ApplicationInfo launchApplication(String username, int applicationID) throws InvalidRequestException,
                                                                                        InvalidResponseException,
                                                                                        IOException {
        ApplicationInfo ret = null;

        String sessionID = usernameToSessionID.get(username);
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setRequest(objectFactory.createRequest());
        wrapper.getRequest().setLogin(objectFactory.createLogin());
        wrapper.getRequest().getLogin().setUsername(username);
        wrapper.getRequest().getLogin().setSessionID(sessionID);

        String url = baseUrl + InterfacerUrl.LAUNCH_APP_URL.getRelativeUrl().replace("${appID}",
                                                                                     Integer.toString(applicationID));

        Response response = sendRequest(url, wrapper);
        if(response.getApplications() == null)
            throw new InvalidRequestException("Missing applications list node",
                                              ResponseCode.ERR_APPLICATION_ERROR);

        ret = response.getApplications().getApplication().get(0);

        return ret;
    }

    public ApplicationInfo closeApplication(String username, int applicationID) throws InvalidRequestException,
                                                                                       InvalidResponseException,
                                                                                       IOException {
        ApplicationInfo ret = null;

        String sessionID = usernameToSessionID.get(username);
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setRequest(objectFactory.createRequest());
        wrapper.getRequest().setLogin(objectFactory.createLogin());
        wrapper.getRequest().getLogin().setUsername(username);
        wrapper.getRequest().getLogin().setSessionID(sessionID);

        String url = baseUrl + InterfacerUrl.CLOSE_APP_URL.getRelativeUrl().replace("${appID}",
         Integer.toString(applicationID));

        Response response = sendRequest(url, wrapper);
        if(response.getApplications() == null)
            throw new InvalidRequestException("Missing applications list node",
                                              ResponseCode.ERR_APPLICATION_ERROR);

        ret = response.getApplications().getApplication().get(0);

        return ret;
    }

    protected Response sendRequest(String url, Wrapper wrapper) throws InvalidResponseException, InvalidRequestException,
                                                                       IOException, HttpException {
        Response response = null;

        if(LOG.isDebugEnabled())
            LOG.debug("Sending request to \"" + url + "\"...");

        StringWriter requestBody = new StringWriter();
        marshaller.marshal(wrapper, new StreamResult(requestBody));

        if(LOG.isTraceEnabled())
            LOG.trace("Request body:\n" + requestBody.toString());

        RequestEntity requestEntity = new StringRequestEntity(requestBody.toString(),
                                                              "text/xml",
                                                              "UTF-8");
        PostMethod post = new PostMethod(url);
        post.setRequestEntity(requestEntity);
        int responseCode = httpClient.executeMethod(post);

        if(LOG.isDebugEnabled())
            LOG.debug("Request sent. Http response code: " + responseCode);

        if(responseCode != HttpStatus.SC_OK)
            throw new HttpException("Http response code: " + response);

        InputStream is = null;
        try {
            is = post.getResponseBodyAsStream();

            if(LOG.isTraceEnabled()) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream((int)post.getResponseContentLength());
                byte buffer[] = new byte[10240];
                int bytesread = 0;
                while((bytesread = is.read(buffer)) > 0) {
                    bos.write(buffer, 0, bytesread);
                }
                is.close();
                byte responseBytes[] = bos.toByteArray();
                String responseStr = new String(responseBytes, "UTF-8");
                LOG.trace("Response body:\n" + responseStr);
                is = new ByteArrayInputStream(responseBytes);
            }

            response = ((Wrapper)unmarshaller.unmarshal(new StreamSource(is))).getResponse();

            if(response == null)
                throw new InvalidResponseException("Missing response node", ResponseCode.ERR_APPLICATION_ERROR);
            ResponseCode serverResponseCode = ResponseCode.fromStatus(response.getStatus());

            if(LOG.isDebugEnabled())
                LOG.debug("Server response code: " + serverResponseCode);

            if(serverResponseCode != ResponseCode.OK) {
                // propagate the server error
                ServerMessage message = response.getMessage();
                String excMessage = (message.getMessageBody() == null ? "" : message.getMessageBody()) +
                                    ": " + message.getExceptionClass();
                throw new InvalidResponseException(excMessage, serverResponseCode);
            }
        } catch (XmlMappingException e) {
            throw new InvalidRequestException("Cannot create request body", e,
                                              ResponseCode.ERR_APPLICATION_ERROR);
        } finally {
            try {
                is.close();
            } catch(Exception e){}
            post.releaseConnection();
        }

        return response;
    }

}
