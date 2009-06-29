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
package com.jolira.enzian.hibernate.internal.osgi;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.Stage.PRODUCTION;
import static org.ops4j.peaberry.Peaberry.osgiModule;
import static org.ops4j.peaberry.Peaberry.service;
import static org.ops4j.peaberry.util.TypeLiterals.export;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.jolira.enzian.hibernate.internal.EntityManagerFactoryProxy;

public class Activator implements BundleActivator {
	private static final String MODE_PROPERTY = "com.jolira.enzian.app.mode";
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

	public void start(final BundleContext context) throws Exception {
		LOG.info("starting " + Activator.class.getName());
		final Module osgiModule = osgiModule(context);
		final Module importModule = new AbstractModule() {
			@Override
			protected void configure() {
				bind(EntityManagerFactory.class).to(
						EntityManagerFactoryProxy.class).asEagerSingleton();
				final Map<String, String> map = new HashMap<String, String>();
				map.put(Constants.SERVICE_PID,
						"org.modulefusion.hibernate.jpaservicehibernate");
				bind(export(EntityManagerFactory.class)).toProvider(
						service(new EntityManagerFactoryProxy()).export());
				// TODO: need to incorporate the maps parameter
			}
		};

		final Stage stage = getInjectorMode(context);

		LOG.info("creating an injector with mode " + stage);

		final Injector injector = createInjector(stage, osgiModule,
				importModule);

		injector.injectMembers(this);
	}

	public void stop(final BundleContext context) throws Exception {
		LOG.info("stopping " + Activator.class.getName());
	}
}
