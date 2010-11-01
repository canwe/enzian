/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.app;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServlet;

import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Start an enzian appliation using Jetty.
 * 
 * @author jfk
 * @date Sep 20, 2010 10:06:16 PM
 * @since 1.0
 */
public class EnzianStarter {
    private static final String TARGET_TEST_CLASSES = "/target/test-classes";
    private static final String TARGET_CLASSES = "/target/classes";
    private static final Logger LOG = LoggerFactory.getLogger(EnzianStarter.class);

    private static void addURL(final Collection<URL> result, final String directory) {
        final File dir = new File(directory);

        if (!dir.isDirectory()) {
            return;
        }

        final URL url = toURL(dir);

        LOG.info("adding " + url + " to the classpath");
        result.add(url);
    }

    private static ClassLoader getClassLoader() {
        final URL[] urls = getExtendedURLs();
        final Thread currentThread = Thread.currentThread();
        final ClassLoader currentThreadClassLoader = currentThread.getContextClassLoader();

        if (urls.length == 0) {
            return currentThreadClassLoader;
        }

        return new URLClassLoader(urls, currentThreadClassLoader);
    }

    private static URL[] getExtendedURLs() {
        final String classpath = System.getProperty("java.class.path");
        final Collection<URL> result = new ArrayList<URL>();
        final StringTokenizer izer = new StringTokenizer(classpath, ":");

        while (izer.hasMoreTokens()) {
            final String token = izer.nextToken();

            if (token.endsWith(TARGET_TEST_CLASSES)) {
                final int tokenLen = token.length();
                final int postfixLen = TARGET_TEST_CLASSES.length();
                final String base = token.substring(0, tokenLen - postfixLen);

                addURL(result, base + "/src/test/resources");
            } else if (token.endsWith(TARGET_CLASSES)) {
                final int tokenLen = token.length();
                final int postfixLen = TARGET_CLASSES.length();
                final String base = token.substring(0, tokenLen - postfixLen);

                addURL(result, base + "/src/main/resources");
            }
        }

        final int size = result.size();

        return result.toArray(new URL[size]);
    }

    /**
     * Start up the server.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        final EnzianStarter starter = new EnzianStarter();

        starter.execute(args);
    }

    private static URL toURL(final File dir) {
        final URI uri = dir.toURI();

        try {
            return uri.toURL();
        } catch (final MalformedURLException e) {
            throw new Error(e);
        }
    }

    /**
     * @return a parser for the command line
     */
    protected EnzianOptionParser createOptionParser() {
        return new EnzianOptionParser();
    }

    /**
     * Execute with command line arguments.
     * 
     * @param args
     *            the arguments passed by the user
     * @throws Exception
     *             stuff happened
     */
    public void execute(final String[] args) throws Exception {
        final EnzianOptionParser parser = createOptionParser();

        if (!parser.parse(args)) {
            parser.printHelp(System.err);
            return;
        }

        final Server server = new Server();
        final SelectChannelConnector httpConnector = new SelectChannelConnector();
        final int port = parser.getPort();
        final String applicationClassName = getApplicationClassName();
        final ServletContextHandler context = new ServletContextHandler();
        final FilterHolder holder = new FilterHolder(WicketFilter.class);
        final ClassLoader cl = getClassLoader();

        context.setSessionHandler(new SessionHandler());
        context.addServlet(HttpServlet.class, "/*");
        holder.setInitParameter("applicationClassName", applicationClassName);
        httpConnector.setPort(port);
        server.setConnectors(new Connector[] { httpConnector });
        context.addFilter(holder, "/*", FilterMapping.ALL);
        context.setContextPath("/");
        server.setHandler(context);
        context.setClassLoader(cl);
        server.start();
        server.join();
    }

    /**
     * @return
     */
    protected String getApplicationClassName() {
        return EnzianWebApplication.class.getName();
    }
}
