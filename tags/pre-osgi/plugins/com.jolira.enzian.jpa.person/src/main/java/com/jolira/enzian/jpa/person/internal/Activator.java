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
package com.jolira.enzian.jpa.person.internal;

import static org.ops4j.peaberry.Peaberry.service;

import javax.persistence.EntityManager;

import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.jolira.enzian.jpa.person.PersonService;

/**
 * Bundle Activator
 * 
 * @author r.roelofsen@prosyst.com (Roman Roelofsen)
 */
public class Activator implements BundleActivator {

	public void start(final BundleContext context) {
		// Create a Guice Module
		Module module = new AbstractModule() {
			protected void configure() {
				// Use the Guice Peaberry extension
				install(Peaberry.osgiModule(context));

				// Bind a OSGi service reference in this module
				bind(EntityManager.class).toProvider(service(EntityManager.class).single());

				// Bind the PersonService
				bind(PersonService.class).to(PersonServiceImpl.class);
			}
		};
		Injector injector = Guice.createInjector(module);

		// Get the PersonService from the injector and register it in
		// the OSGi service registry
		PersonService instance = injector.getInstance(PersonService.class);
		context.registerService(PersonService.class.getName(), instance, null);
	}

	public void stop(BundleContext context) throws Exception {
	}
}
