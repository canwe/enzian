package com.jolira.enzian.app.internal.osgi;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.Stage.DEVELOPMENT;
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

import org.apache.wicket.protocol.http.WebApplication;
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
import com.jolira.enzian.app.internal.EnzianAppliationImpl;
import com.jolira.enzian.app.internal.EnzianFilter;

/**
 * Extension of the default OSGi bundle activator
 */
public final class Activator implements BundleActivator {
  private static final String APP_FACTORY_CLASS_NAME = EnzianWebApplicationFactory.class.getName();
  private static final Integer SESSION_TIMEOUT = Integer.valueOf(15); // TODO
  protected static final Logger LOG = LoggerFactory.getLogger(Activator.class);

  @Inject
  private transient WebContainer webContainer;
  protected Injector injector = null;
  private transient final Filter wicketFilter = new EnzianFilter() {
    @Override
    public Injector getInjector() {
      return injector;
    }
  };

  private final Servlet dummyServlet = new Servlet(){
    public void destroy() {
      LOG.info("Servlet.destroy()");
    }

    public ServletConfig getServletConfig() {
      LOG.info("Servlet.getServletConfig()");
      return null;
    }

    public String getServletInfo() {
      LOG.info("Servlet.getServletInfo()");
      return null;
    }

    public void init(final ServletConfig arg0) throws ServletException {
      LOG.info("Servlet.init(..)");
    }

    public void service(final ServletRequest arg0, final ServletResponse arg1)
    throws ServletException, IOException {
      LOG.info("Servlet.service(..)");
    }
  };

  protected void register(final WebContainer container) {
    LOG.info("registering " + container);

    final HttpContext defaultContext = container.createDefaultHttpContext();
    final HttpContext httpContext = new HttpContext() {
      public String getMimeType(final String name) {
        final String value = defaultContext.getMimeType(name);

        // TODO: Remove
        LOG.info("HttpContext.getMimeType(\"" + name + "\") returned: " + value);

        return value;
      }

      public URL getResource(final String name) {
        final URL value = defaultContext.getResource(name);

        // TODO: Remove
        LOG.info("HttpContext.getResource(\"" + name + "\") returned: " + value);

        return value;
      }

      public boolean handleSecurity(final HttpServletRequest request,
          final HttpServletResponse response) throws IOException {
        final boolean value = defaultContext.handleSecurity(request, response);

        // TODO: Remove
        LOG.info("HttpContext.handleSecurity(" + request + ", "+ response + " ) returned: " + value);

        return value;
      }
    };
    final Dictionary<String,String> containerParams = new Hashtable<String, String>();

    containerParams.put("configuration" , "development"); // TODO
    container.setContextParam(containerParams, httpContext);
    container.setSessionTimeout(SESSION_TIMEOUT, httpContext);

    final Dictionary<String,String> filterParams = new Hashtable<String, String>();

    filterParams.put("applicationFactoryClassName", APP_FACTORY_CLASS_NAME);

    final String[] as = new String[] {"/", "/*"};

    container.registerFilter(wicketFilter, as, null, filterParams, httpContext);

    try {
      container.registerServlet(dummyServlet, as, new Hashtable<String, String>(), httpContext);
    } catch (final ServletException e) {
      LOG.error("Error registering dummy sevlet", e);
    }
  }

  /**
   * Called whenever the OSGi framework starts our bundle
   */
  public void start(final BundleContext context) {
    LOG.info("starting " + Activator.class.getName());

    final Module osgiModule = osgiModule(context);
    final Module importModule = new AbstractModule(){
      @Override
      protected void configure() {
        final DecoratedServiceBuilder<WebContainer> service = service(WebContainer.class);
        final AbstractWatcher<WebContainer> watcher = new AbstractWatcher<WebContainer>() {
          @Override
          protected WebContainer adding(final Import<WebContainer> svc) {
            // the returned object is used in the modified and removed calls
            final WebContainer instance = svc.get();

            register(instance);

            return instance;
          }

          @Override
          protected void modified(final WebContainer instance, final Map<String, ?> attributes) {
            unregister(instance);
            register(instance);
          }

          @Override
          protected void removed(final WebContainer instance) {
            unregister(instance);
          }
        };
        final ServiceBuilder<WebContainer> out = service.out(watcher);
        final ProxyProvider<WebContainer> single = out.single();
        final EnzianAppliationImpl app = new EnzianAppliationImpl();
        final AnnotatedBindingBuilder<WebContainer> containerBind = bind(WebContainer.class);
        final AnnotatedBindingBuilder<WebApplication> appBinder = bind(WebApplication.class);

        appBinder.toInstance(app);
        containerBind.toProvider(single);
      }
    };

    final Stage stage = DEVELOPMENT; // TODO: should be configurable

    injector = createInjector(stage, osgiModule, importModule);

    injector.injectMembers(this);
  }

  /**
   * Called whenever the OSGi framework stops our bundle
   */
  public void stop(final BundleContext context)	{
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
