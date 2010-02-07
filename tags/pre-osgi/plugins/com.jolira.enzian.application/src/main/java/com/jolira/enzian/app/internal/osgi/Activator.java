/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General
 * Public License, Version 3.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.jolira.enzian.app.internal.osgi;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.Stage.PRODUCTION;
import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;

import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.ops4j.pax.web.service.WebContainer;
import org.ops4j.peaberry.Import;
import org.ops4j.peaberry.builders.DecoratedServiceBuilder;
import org.ops4j.peaberry.builders.ProxyProvider;
import org.ops4j.peaberry.builders.ServiceBuilder;
import org.ops4j.peaberry.util.AbstractWatcher;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.jolira.enzian.app.guice.EnzianWebApplicationFactory;
import com.jolira.enzian.app.internal.EnzianApplicationImpl;
import com.jolira.enzian.app.internal.EnzianFilter;

/**
 * Extension of the default OSGi bundle activator
 */
public final class Activator implements BundleActivator {
    private static final String MODE_PROPERTY = "com.jolira.enzian.app.mode";
    private static final String APP_FACTORY_CLASS_NAME = EnzianWebApplicationFactory.class
            .getName();
    protected static final Logger LOG = LoggerFactory
            .getLogger(Activator.class);

    private static Stage getInjectorMode(final BundleContext context) {
        final String mode = getMode(context);

        if (mode == null || mode.length() < 1) {
            return PRODUCTION;
        }

        final String upperMode = mode.toUpperCase();
        final Stage stage = Stage.valueOf(upperMode);

        return stage == null ? PRODUCTION : stage;
    }

    private static String getMode(final BundleContext context) {
        return context.getProperty(MODE_PROPERTY);
    }

    @Inject
    private transient WebContainer webContainer;

    protected Injector injector = null;

    private transient final Filter wicketFilter = new EnzianFilter() {
        @Override
        public Injector getInjector() {
            return injector;
        }
    };

    private final Servlet dummyServlet = new Servlet() {
        public void destroy() {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Servlet.destroy()");
            }
        }

        public ServletConfig getServletConfig() {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Servlet.getServletConfig()");
            }
            return null;
        }

        public String getServletInfo() {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Servlet.getServletInfo()");
            }
            return null;
        }

        public void init(final ServletConfig arg0) throws ServletException {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Servlet.init(..)");
            }
        }

        public void service(final ServletRequest req, final ServletResponse resp)
                throws ServletException, IOException {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Servlet.service(..)");
            }
        }
    };

    protected void register(final WebContainer container,
            final BundleContext context) {
        LOG.info("registering " + container + " for " + context);

        final HttpContext defaultContext = container.createDefaultHttpContext();
        final HttpContext httpContext = new HttpContext() {
            public String getMimeType(final String name) {
                final String value = defaultContext.getMimeType(name);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("HttpContext.getMimeType(\"" + name
                            + "\") returned: " + value);
                }

                return value;
            }

            public URL getResource(final String name) {
                final URL value = defaultContext.getResource(name);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("HttpContext.getResource(\"" + name
                            + "\") returned: " + value);
                }
                return value;
            }

            public boolean handleSecurity(final HttpServletRequest request,
                    final HttpServletResponse response) throws IOException {
                final boolean value = defaultContext.handleSecurity(request,
                        response);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("HttpContext.handleSecurity(" + request + ", "
                            + response + " ) returned: " + value);
                }
                return value;
            }
        };
        final Dictionary<String, String> containerParams = new Hashtable<String, String>();
        final String mode = getMode(context);

        if (mode != null && mode.length() < 1) {
            containerParams.put("configuration", mode);
        }

        container.setContextParam(containerParams, httpContext);

        final Dictionary<String, String> filterParams = new Hashtable<String, String>();

        filterParams.put("applicationFactoryClassName", APP_FACTORY_CLASS_NAME);

        final String[] as = new String[] { "/*" };

        container.registerFilter(wicketFilter, as, null, filterParams,
                httpContext);

        try {
            container.registerServlet(dummyServlet, as,
                    new Hashtable<String, String>(), httpContext);
        }
        catch (final ServletException e) {
            LOG.error("Error registering dummy sevlet", e);
        }
    }

    /**
     * Called whenever the OSGi framework starts our bundle
     */
    public void start(final BundleContext context) {
        LOG.info("starting " + Activator.class.getName());

        final Module osgiModule = osgiModule(context);
        final Module importModule = new AbstractModule() {
            @Override
            protected void configure() {
                install(osgiModule);

                final DecoratedServiceBuilder<WebContainer> service = service(WebContainer.class);
                final AbstractWatcher<WebContainer> watcher = new AbstractWatcher<WebContainer>() {
                    @Override
                    protected WebContainer adding(final Import<WebContainer> svc) {
                        // the returned object is used in the modified and
                        // removed calls
                        final WebContainer instance = svc.get();

                        register(instance, context);

                        return instance;
                    }

                    @Override
                    protected void modified(final WebContainer instance,
                            final Map<String, ?> attributes) {
                        unregister(instance);
                        register(instance, context);
                    }

                    @Override
                    protected void removed(final WebContainer instance) {
                        unregister(instance);
                    }
                };
                final ServiceBuilder<WebContainer> out = service.out(watcher);
                final ProxyProvider<WebContainer> single = out.single();
                final EnzianApplicationImpl app = new EnzianApplicationImpl();
                final AnnotatedBindingBuilder<WebContainer> containerBind = bind(WebContainer.class);
                final AnnotatedBindingBuilder<AuthenticatedWebApplication> appBinder = bind(AuthenticatedWebApplication.class);

                appBinder.toInstance(app);
                containerBind.toProvider(single);
                
            }
        };

        final Stage stage = getInjectorMode(context);

        LOG.info("creating an injector with mode " + stage);

        injector = createInjector(stage, osgiModule, importModule);

        injector.injectMembers(this);
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     */
    public void stop(final BundleContext context) {
        LOG.info("stopping " + Activator.class.getName());

        unregister(webContainer);
    }

    protected void unregister(final WebContainer container) {
        LOG.info("unregistering " + container);

        container.unregisterServlet(dummyServlet);
        container.unregisterFilter(wicketFilter);

        injector = null;
    }
}
