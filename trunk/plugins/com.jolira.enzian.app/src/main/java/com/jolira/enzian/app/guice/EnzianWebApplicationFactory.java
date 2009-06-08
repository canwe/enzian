package com.jolira.enzian.app.guice;

import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;

import com.google.inject.Injector;
import com.jolira.enzian.app.internal.EnzianFilter;

/**
 * A factory for creating applications using an {@link EnzianFilter}. This class creates
 * the {@link WebApplication) used by Wicket. It uses the injector returned by the
 * {@link EnzianFilter} to create the class.
 * 
 * @author Joachim Kainz
 *
 */
public class EnzianWebApplicationFactory implements IWebApplicationFactory {

  public WebApplication createApplication(final WicketFilter filter) {
    if (!(filter instanceof EnzianFilter)) {
      throw new IllegalArgumentException("Filter must be a WicketFilter");
    }

    final EnzianFilter enzianFilter = (EnzianFilter)filter;
    final Injector injector = enzianFilter.getInjector();
    final WebApplication app = injector.getInstance(WebApplication.class);

    app.addComponentInstantiationListener(new GuiceComponentInjector(app, injector));

    return app;
  }

}
