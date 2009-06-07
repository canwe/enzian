package com.jolira.enzian.app.internal.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator
 */
public final class Activator implements BundleActivator {
	public static final Logger LOG = LoggerFactory.getLogger(Activator.class);
	/**
	 * Called whenever the OSGi framework starts our bundle
	 */
	public void start(final BundleContext context) {
		LOG.info("Starting " + Activator.class.getName());
	}

	/**
	 * Called whenever the OSGi framework stops our bundle
	 */
	public void stop(final BundleContext context)	{
		LOG.info("Stopping " + Activator.class.getName());
	}
}
