/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.app;

import static java.util.Arrays.asList;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
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
    private static final String PORT_ARG = "p";
    private static final String TARGET_TEST_CLASSES = "/target/test-classes";
    private static final String TARGET_CLASSES = "/target/classes";
    private static final Logger LOG = LoggerFactory.getLogger(EnzianStarter.class);
    private static final String BASEDIR_PROP = "basedir";

    private static void addURL(final Collection<URL> result, final String directory) {
        final File dir = new File(directory);

        if (!dir.isDirectory()) {
            return;
        }

        final URL url = toURL(dir);

        LOG.info("adding " + url + " to the classpath");
        result.add(url);
    }

    private static File getBaseDir(final Class<?> clazz) {
        final String basedir = System.getProperty(BASEDIR_PROP);

        if (basedir != null) {
            return new File(basedir);
        }

        final ProtectionDomain pd = clazz.getProtectionDomain();
        final CodeSource cs = pd.getCodeSource();
        final URL location = cs.getLocation();
        final String protocol = location.getProtocol();

        if (!"file".equals(protocol)) {
            throw new Error("coude source for class " + clazz
                    + " is not a directory and cannot be used to determine the project dir: " + location);
        }

        final String classes = location.getFile();
        final File _classes = new File(classes);
        final File target = _classes.getParentFile();

        return target.getParentFile();
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
        final OptionParser parser = new OptionParser();
        final OptionSpec<Integer> portOption = parser.accepts(PORT_ARG).withRequiredArg().ofType(Integer.class)
                .describedAs("listen port").defaultsTo(Integer.valueOf(8081));

        parser.acceptsAll(asList("h", "?"), "show help");

        final OptionSet options = parser.parse(args);

        if (options.has("?")) {
            parser.printHelpOn(System.out);
            return;
        }

        updateClassPath();

        final Server server = new Server();
        final SelectChannelConnector httpConnector = new SelectChannelConnector();
        final Integer port = options.valueOf(portOption);

        httpConnector.setPort(port.intValue());
        server.setConnectors(new Connector[] { httpConnector });

        final WebAppContext context = new WebAppContext();
        final File baseDir = getBaseDir(EnzianStarter.class);
        final File webappDir = new File(baseDir, "src/main/webapp");
        final File webxml = new File(webappDir, "WEB-INF/web.xml");
        final String resourceBase = webappDir.getAbsolutePath();
        final String descriptor = webxml.getAbsolutePath();

        context.setContextPath("/");
        context.setDescriptor(descriptor);
        context.setResourceBase(resourceBase);
        server.setHandler(context);
        server.start();
        server.join();
    }

    private static URL toURL(final File dir) {
        final URI uri = dir.toURI();

        try {
            return uri.toURL();
        } catch (final MalformedURLException e) {
            throw new Error(e);
        }
    }

    private static void updateClassPath() {
        final URL[] urls = getExtendedURLs();

        if (urls.length == 0) {
            return;
        }

        final Thread currentThread = Thread.currentThread();
        final ClassLoader currentThreadClassLoader = currentThread.getContextClassLoader();
        final URLClassLoader urlClassLoader = new URLClassLoader(urls, currentThreadClassLoader);

        currentThread.setContextClassLoader(urlClassLoader);
    }
}
