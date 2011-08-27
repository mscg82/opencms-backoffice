package com.mscg.appstarter.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.mscg.appstarter.beans.jaxb.ApplicationInfo;
import com.mscg.appstarter.client.interfacer.AppStarterInterfacer;

public class AppStarterClient {

    private static final Logger LOG = LoggerFactory.getLogger(AppStarterClient.class);

    public static void main(String[] args) {
        try {
            GenericApplicationContext base = new GenericApplicationContext();
            base.getBeanFactory().registerSingleton("args", args);
            base.refresh();

            String[] contextPaths = new String[] {"classpath:springconfig/app-config.xml",
                                                  "classpath:springconfig/httpclient.xml"};
            ApplicationContext xmlApplicationContext = new ClassPathXmlApplicationContext(contextPaths, true, base);
            LOG.info("Spring context started with " + xmlApplicationContext.getBeanDefinitionCount() + " bean definitions");

            test(xmlApplicationContext);

        } catch(Exception e) {
            e.printStackTrace();
            LOG.error("An error occurred during the application execution", e);
        }
    }

    private static void test(ApplicationContext xmlApplicationContext) throws Exception {
        boolean logged = false;
        AppStarterInterfacer appStarterInterfacer = null;
        String username = "mscg";

        try {
            appStarterInterfacer = (AppStarterInterfacer)xmlApplicationContext.getBean("appStarterInterfacer");
            if(appStarterInterfacer.login(username, "mscgpp82")) {
                System.out.println("Logged successfully, logging out...");
                logged = true;

                while(true) {
                    try {
                        List<ApplicationInfo> listApplications = appStarterInterfacer.listApplications(username);
                        System.out.println("Configured applications:");
                        for(ApplicationInfo applicationInfo : listApplications) {
                            System.out.format("    %3d) %s - Running: %s%n", applicationInfo.getId(),
                                                                             applicationInfo.getName(),
                                                                             applicationInfo.isRunning());
                        }

                        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                        String op = null;
                        Integer app = null;

                        do {
                            System.out.print("Choose operation: (L)aunch, (C)lose, (Q)uit: ");
                            op = in.readLine();
                        } while(!"l".equalsIgnoreCase(op) && !"c".equalsIgnoreCase(op) && !"q".equalsIgnoreCase(op));

                        if("q".equalsIgnoreCase(op))
                            return;

                        do {
                            System.out.print("Choose an application ID (-1 to abort): ");
                            try {
                                app = Integer.parseInt(in.readLine());
                            } catch(Exception e){}
                        } while(app == null);

                        if(-1 == app)
                            continue;

                        if("l".equalsIgnoreCase(op))
                            appStarterInterfacer.launchApplication(username, app);
                        else if("c".equalsIgnoreCase(op))
                            appStarterInterfacer.closeApplication(username, app);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            else {
                System.out.println("Wrong credentials");
            }

        } finally {
            if(logged) {
                try {
                    appStarterInterfacer.logout(username);
                    System.out.println("Logged out.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
