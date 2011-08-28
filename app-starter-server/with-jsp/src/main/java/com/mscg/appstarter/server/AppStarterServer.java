package com.mscg.appstarter.server;

import java.io.File;

import org.apache.jasper.servlet.JspServlet;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

import com.mscg.appstarter.server.util.Constants;
import com.mscg.appstarter.server.util.Settings;

public class AppStarterServer {

    private static final Logger LOG = LoggerFactory.getLogger(AppStarterServer.class);

    public static void main(String[] args) {
        File runningFile = new File(Constants.RUNNING_FILE);
        try {
            if(!runningFile.exists())
                runningFile.createNewFile();
            runningFile.deleteOnExit();

            Server server = new Server();

            QueuedThreadPool threadPool = new QueuedThreadPool();
            threadPool.setMinThreads(Settings.getConfig().getInt(Constants.MIN_THREADS));
            threadPool.setMaxThreads(Settings.getConfig().getInt(Constants.MAX_THREADS));
            threadPool.setLowThreads(Settings.getConfig().getInt(Constants.LOW_THREADS));
            threadPool.setSpawnOrShrinkAt(Settings.getConfig().getInt(Constants.SHRINK_THREADS));
            server.setThreadPool(threadPool);

            Connector connector = new SelectChannelConnector();
            connector.setHost(Settings.getConfig().getString(Constants.HOST_NAME_PARAM));
            connector.setPort(Settings.getConfig().getInt(Constants.PORT_PARAM));
            connector.setMaxIdleTime(30000);
            ((SelectChannelConnector)connector).setAcceptors(1);

            LOG.debug("Server will start on " + connector.getHost() + ":" + connector.getPort() + "...");
            server.addConnector(connector);

            LOG.debug("Binding Spring servlet to root context...");
            Context context = new Context(server, "/", Context.SESSIONS);

            ServletHolder jspServletHolder = new ServletHolder(new JspServlet());
            jspServletHolder.setInitParameter("fork", "false");
            jspServletHolder.setInitParameter("keepgenerated", "true");
            context.addServlet(jspServletHolder, "*.jsp");

            ServletHolder servletHolder = new ServletHolder(new DispatcherServlet());
            servletHolder.setInitParameter("contextConfigLocation", "classpath:/springconfig/main.xml,classpath:/springconfig/jobs.xml");
            context.addServlet(servletHolder, "/*");
            LOG.debug("Spring context created");

            server.setStopAtShutdown(true);
            server.setGracefulShutdown(5000);
            server.start();
            LOG.info("Server started on " + connector.getHost() + ":" + connector.getPort());

            checkForExit();

        } catch(Exception e) {
            LOG.error("An error occurred", e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void checkForExit() throws Exception {
        while(!Thread.interrupted()) {
            try {
                Thread.sleep(500l);
            } catch (InterruptedException e) { }
            File runningFile = new File(Constants.RUNNING_FILE);
            if(!runningFile.exists()) {
                LOG.info("Running file doesn't exist any more, shutting down");
                System.exit(0);
                break;
            }
        }
    }

}
