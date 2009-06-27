/*
 * Copyright (C) 2008 ProSyst Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jolira.enzian.guice;

import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.export;

import java.util.Map;

import org.ops4j.peaberry.Peaberry;
import org.ops4j.peaberry.builders.DecoratedServiceBuilder;
import org.osgi.framework.BundleContext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Simple utility class to help creating Guice modules in an OSGi context.
 * Subclasses need to call the <code>BundleModule(BundleContext context)</code>
 * constructor.
 * 
 * @author r.roelofsen@prosyst.com (Roman Roelofsen)
 */
public abstract class BundleModule extends AbstractModule {

	protected final BundleContext context;

	public BundleModule(BundleContext context) {
		this.context = context;
	}

	public BundleContext getContext() {
		return context;
	}

	@Override
	protected void configure() {
		install(Peaberry.osgiModule(context));
		createModule();
	}

	/**
	 * Overrride to configure the binder.
	 * 
	 * @param context
	 *            The BundleContext
	 */
	protected abstract void createModule();

	/**
	 * Utility method to create an injector based on this module.
	 * 
	 * @return The injector
	 */
	public Injector createInjector() {
		return Guice.createInjector(this);
	}

	protected <A extends Object> void importService(Class<A> iface) {
		bind(iface).toProvider(Peaberry.service(iface).single());
	}

	protected <A extends Object> void exportService(Class<A> clazz) {
		exportService(clazz, null);
	}

	protected <A extends Object> void exportService(Class<A> clazz, Map<String, ?> props) {
		exportService(clazz, Key.get(clazz), props);
	}

	protected <A extends Object, B extends A> void exportService(Class<A> iface, Key<B> key,
			Map<String, ?> props) {
		
		DecoratedServiceBuilder<B> provider = service(key);
		if (props != null)
			provider.attributes(props);
		
		bind(export(iface)).toProvider(provider.export()).asEagerSingleton();
	}

}
