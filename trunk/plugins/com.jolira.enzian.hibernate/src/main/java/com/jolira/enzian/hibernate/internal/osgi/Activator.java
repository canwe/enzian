package com.jolira.enzian.hibernate.internal.osgi;

import static org.ops4j.peaberry.Peaberry.service;

import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.jolira.enzian.hibernate.EntityManagerProxy;

public class Activator implements BundleActivator {

	@Override
	public void start(final BundleContext context) throws Exception {
		
		// Create a Guice Module
		Module module = new AbstractModule() {
			protected void configure() {
				// Use the Guice Peaberry extension
				install(Peaberry.osgiModule(context));

				// Bind a OSGi service reference in this module
				bind(EntityManagerProxy.class).to(EntityManagerProxyImpl.class);

			}
		};
		Guice.createInjector(module);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub

	}

}
