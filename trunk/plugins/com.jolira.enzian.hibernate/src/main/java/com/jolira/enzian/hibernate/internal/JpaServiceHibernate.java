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
package com.jolira.enzian.hibernate.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;

import org.hibernate.MappingException;
import org.hibernate.ejb.Ejb3Configuration;
import com.jolira.enzian.hibernate.JpaService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class JpaServiceHibernate implements ManagedService, JpaService {

	BundleListener bundleListener = new BundleListener() {
		public void bundleChanged(BundleEvent event) {
			if (event.getType() == BundleEvent.RESOLVED
					|| event.getType() == BundleEvent.UNRESOLVED) {
				URL entry = event.getBundle().getEntry(ENTITIES);
				if (entry != null) {
					entityManagerFactory = null;
					logger.info("Bundle '{}' with JPA configuration changed status. "
							+ "Marking SessionFactory out-of-date.", event.getBundle());
				}
			}
		}
	};

	final Logger logger = LoggerFactory.getLogger(JpaServiceHibernate.class);

	private BundleContext context;

	private static String ENTITIES = "/OSGI-INF/jpa/classes";

	private Properties properties = new Properties();

	private EntityManagerFactory entityManagerFactory = null;

	@Inject
	public void setDependencies(BundleContext context) {
		this.context = context;
		context.addBundleListener(bundleListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	@SuppressWarnings("unchecked")
	public void updated(Dictionary dic) throws ConfigurationException {
		if (dic != null) {
			synchronized (properties) {
				Enumeration<?> keys = dic.keys();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					properties.setProperty(key, (String) dic.get(key));
					logger.trace("Setting Hibernate property '{}' to '{}'", key, dic.get(key));
				}
				entityManagerFactory = null;
				logger.info("New JPA configuration. Marking SessionFactory out-of-date.");
			}
		}
	}

	/**
	 * @return
	 */
	private EntityManagerFactory createNewEntityManagerFactory() {
		Ejb3Configuration c = new Ejb3Configuration();
		c.setProperties(properties);

		for (Bundle b : context.getBundles()) {
			URL entities = b.getEntry(ENTITIES);
			if (entities == null)
				continue;

			logger.info("Adding entity classes from bundle '{}'", b.getSymbolicName());
			try {
				InputStreamReader isr = new InputStreamReader(entities.openStream());
				BufferedReader br = new BufferedReader(isr);
				String entity = null;
				while ((entity = br.readLine()) != null) {
					entity = entity.trim();
					if (entity.startsWith("#"))
						continue;

					logger.trace(
							"Adding entity class '{}' from bundle '{}' to Hibernate configuration",
							entity, b.getSymbolicName());
					try {
						c.addAnnotatedClass(b.loadClass(entity));
					} catch (MappingException e) {
						logger.error("Error mapping entity class", e);
					} catch (ClassNotFoundException e) {
						logger.error("Error loading entity class", e);
					}
				}
			} catch (IOException e1) {
				logger.error("Error reading from bundle", e1);
			}

		}
		return c.buildEntityManagerFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.modulefusion.api.JpaService#getEntityManagerFactory()
	 */
	public EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null) {
			logger.trace("SessionFactory is out-of-date. Creating a new one.");
			entityManagerFactory = createNewEntityManagerFactory();
		}
		return entityManagerFactory;
	}

}
