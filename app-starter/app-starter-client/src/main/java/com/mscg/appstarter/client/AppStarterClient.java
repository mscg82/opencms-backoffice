package com.mscg.appstarter.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

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

//            AppStarterInterfacer appStarterInterfacer = (AppStarterInterfacer)xmlApplicationContext.getBean("appStarterInterfacer");
//            if(appStarterInterfacer.login("mscg", "mscgpp82")) {
//                System.out.println("Logged successfully, logging out...");
//                appStarterInterfacer.logout("mscg");
//                System.out.println("Logged out.");
//            }
//            else {
//                System.out.println("Wrong credentials");
//            }

        } catch(Exception e) {
            e.printStackTrace();
            LOG.error("An error occurred during the application execution", e);
        }
    }

}
