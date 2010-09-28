/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.starter;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import org.eclipse.jetty.ajp.Ajp13SocketConnector;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * A standalone starter for the Enzian application.
 * 
 * @author jfk
 * @date Sep 26, 2010 10:18:31 AM
 * @since 1.0
 */
public class EzianStarter {
    private static File getBaseDir(final Class<?> clazz) {
        final String basedir = System.getProperty("basedir");

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

    /**
     * Start up the application
     * 
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        final Server server = new Server();
        final SelectChannelConnector httpConnector = new SelectChannelConnector();
        final Ajp13SocketConnector ajp13Connector = new Ajp13SocketConnector();

        httpConnector.setPort(80);
        ajp13Connector.setPort(8009);

        server.setConnectors(new Connector[] { httpConnector, ajp13Connector });

        final WebAppContext context = new WebAppContext();
        final File baseDir = getBaseDir(EzianStarter.class);
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
}
