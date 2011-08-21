package com.mscg.appstarter.client.interfacer;

import java.io.IOException;

import com.mscg.appstarter.client.interfacer.exception.InvalidRequestException;
import com.mscg.appstarter.client.interfacer.exception.InvalidResponseException;

public interface AppStarterInterfacer {

    public String getBaseUrl();

    public void setBaseUrl(String baseUrl);

    public boolean login(String username, String password) throws InvalidRequestException,
                                                                  InvalidResponseException,
                                                                  IOException;

    public void logout(String username) throws InvalidRequestException,
                                               InvalidResponseException,
                                               IOException;

    public boolean ping(String username) throws InvalidRequestException,
                                                InvalidResponseException,
                                                IOException;

}
