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

        } catch(Exception e) {
            e.printStackTrace();
            LOG.error("An error occurred during the application execution", e);
        }
    }

}
