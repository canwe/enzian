package com.jolira.enzian.app.internal.osgi;

import static com.google.inject.Guice.createInjector;
import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;

import java.util.Map;

import org.ops4j.pax.web.service.WebContainer;
import org.ops4j.peaberry.Import;
import org.ops4j.peaberry.builders.DecoratedServiceBuilder;
import org.ops4j.peaberry.builders.ProxyProvider;
import org.ops4j.peaberry.builders.ServiceBuilder;
import org.ops4j.peaberry.util.AbstractWatcher;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Extension of the default OSGi bundle activator
 */
public final class Activator implements BundleActivator {
	public static final Logger LOG = LoggerFactory.getLogger(Activator.class);

	@Inject
	private transient WebContainer webContainer;

	protected void register(final WebContainer instance) {
		LOG.info("registering " + instance);
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
					protected WebContainer adding(final Import<WebContainer> service) {
						// the returned object is used in the modified and removed calls
						final WebContainer instance = service.get();
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

				bind(WebContainer.class).toProvider(single);
			}
		};
		final Injector injector = createInjector(osgiModule, importModule);

		injector.injectMembers(this);
	}

	/**
	 * Called whenever the OSGi framework stops our bundle
	 */
	public void stop(final BundleContext context)	{
		LOG.info("stopping " + Activator.class.getName());

		unregister(webContainer);
	}

	protected void unregister(final WebContainer instance) {
		LOG.info("unregistering " + instance);
	}
}
