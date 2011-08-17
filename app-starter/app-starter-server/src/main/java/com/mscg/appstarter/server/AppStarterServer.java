package com.mscg.appstarter.server;

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

public class AppStarterServer {

    private static final Logger LOG = LoggerFactory.getLogger(AppStarterServer.class);

    public static void main(String[] args) {
        try {
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
            ServletHolder servletHolder = new ServletHolder(new DispatcherServlet());
            servletHolder.setInitParameter("contextConfigLocation", "classpath:/spring.xml");
            context.addServlet(servletHolder, "/*");
            LOG.debug("Spring context created");

            server.setStopAtShutdown(true);
            server.setGracefulShutdown(5000);
            server.start();
            LOG.info("Server started on " + connector.getHost() + ":" + connector.getPort());

        } catch(Exception e) {
            LOG.error("An error occurred", e);
            e.printStackTrace();
        }
    }

}
