package com.mscg.appstarter.client.interfacer;

import java.io.IOException;
import java.util.List;

import com.mscg.appstarter.beans.jaxb.ApplicationInfo;
import com.mscg.appstarter.exception.ApplicationAlreadyRunningException;
import com.mscg.appstarter.exception.ApplicationNotConfiguredException;
import com.mscg.appstarter.exception.ApplicationNotRunningException;
import com.mscg.appstarter.exception.InvalidRequestException;
import com.mscg.appstarter.exception.InvalidResponseException;

public interface AppStarterInterfacer {

    public String getBaseUrl();

    public void setBaseUrl(String baseUrl);

    public boolean login(String username, String password) throws InvalidRequestException,
                                                                  InvalidResponseException,
                                                                  IOException,
                                                                  Exception;

    public void logout(String username) throws InvalidRequestException,
                                               InvalidResponseException,
                                               IOException,
                                               Exception;

    public boolean ping(String username) throws InvalidRequestException,
                                                InvalidResponseException,
                                                IOException,
                                                Exception;

    public List<ApplicationInfo> listApplications(String username) throws InvalidRequestException,
                                                                          InvalidResponseException,
                                                                          IOException,
                                                                          Exception;

    public ApplicationInfo launchApplication(String username, int applicationID) throws InvalidRequestException,
                                                                                        InvalidResponseException,
                                                                                        ApplicationNotConfiguredException,
                                                                                        ApplicationAlreadyRunningException,
                                                                                        IOException,
                                                                                        Exception;

    public ApplicationInfo closeApplication(String username, int applicationID) throws InvalidRequestException,
                                                                                       InvalidResponseException,
                                                                                       ApplicationNotConfiguredException,
                                                                                       ApplicationNotRunningException,
                                                                                       IOException,
                                                                                       Exception;

}
