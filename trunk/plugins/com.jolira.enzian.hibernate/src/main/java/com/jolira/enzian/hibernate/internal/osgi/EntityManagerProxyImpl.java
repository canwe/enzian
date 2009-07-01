package com.jolira.enzian.hibernate.internal.osgi;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Properties;

import com.jolira.enzian.hibernate.EntityManagerProxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


import org.hibernate.MappingException;
import org.hibernate.ejb.Ejb3Configuration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityManagerProxyImpl implements EntityManagerProxy, ManagedService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EntityManagerProxyImpl.class);

	private BundleContext context;

	private static String entities = "/OSGI-INF/jpa/classes";

	private Properties properties = new Properties();

	private EntityManagerFactory entityManagerFactory = null;
	
	BundleListener bundleListener = new BundleListener() {
		public void bundleChanged(BundleEvent event) {
			if (event.getType() == BundleEvent.RESOLVED
					|| event.getType() == BundleEvent.UNRESOLVED) {
				final Bundle bundle = event.getBundle();
				URL entry = bundle.getEntry(entities);
				if (entry != null) {
					entityManagerFactory = null;
					LOGGER.info("Bundle '{}' with JPA configuration changed status. "
							+ "Marking SessionFactory out-of-date.", bundle);
				}
			}
		}
	};
	
	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		
		if (entityManagerFactory == null) {
			LOGGER.trace("SessionFactory is out-of-date. Creating a new one.");
			entityManagerFactory = createNewEntityManagerFactory();
		}
		return entityManagerFactory;
	}

	/**
	 * @return
	 */
	private EntityManagerFactory createNewEntityManagerFactory() {
		Ejb3Configuration c = new Ejb3Configuration();
		c.setProperties(properties);

		for (Bundle b : context.getBundles()) {
			URL url = b.getEntry(entities);
			if (url == null)
				continue;

			LOGGER.info("Adding entity classes from bundle '{}'", b.getSymbolicName());
			try {
				InputStreamReader isr = new InputStreamReader(url.openStream());
				BufferedReader br = new BufferedReader(isr);
				String entity = null;
				while ((entity = br.readLine()) != null) {
					entity = entity.trim();
					if (entity.startsWith("#"))
						continue;

					LOGGER.trace(
							"Adding entity class '{}' from bundle '{}' to Hibernate configuration",
							entity, b.getSymbolicName());
					try {
						c.addAnnotatedClass(b.loadClass(entity));
					} catch (MappingException e) {
						LOGGER.error("Error mapping entity class", e);
					} catch (ClassNotFoundException e) {
						LOGGER.error("Error loading entity class", e);
					}
				}
			} catch (IOException e1) {
				LOGGER.error("Error reading from bundle", e1);
			}

		}
		return c.buildEntityManagerFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	@SuppressWarnings("unchecked")
	public void updated(final Dictionary dic) throws ConfigurationException {
		if (dic != null) {
			synchronized (properties) {
				Enumeration<?> keys = dic.keys();
				while (keys.hasMoreElements()) {
					String key = (String) keys.nextElement();
					properties.setProperty(key, (String) dic.get(key));
					LOGGER.trace("Setting Hibernate property '{}' to '{}'", key, dic.get(key));
				}
				entityManagerFactory = null;
				LOGGER.info("New JPA configuration. Marking SessionFactory out-of-date.");
			}
		}
	}

	public EntityManager getEntityManager() {
		EntityManagerFactory emFactory = getEntityManagerFactory();
		return emFactory.createEntityManager();
	}
	
}
