package com.jolira.enzian.hibernate.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.MappingException;
import org.hibernate.ejb.Ejb3Configuration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityManagerFactoryProxy implements EntityManagerFactory,
		ManagedService {
	final Logger logger = LoggerFactory
			.getLogger(EntityManagerFactoryProxy.class);
	private BundleContext context;

	private static String ENTITIES = "/OSGI-INF/jpa/classes";

	private final Properties properties = new Properties();

	private EntityManagerFactory emFactory = null;

	@Override
	public void close() {
		final EntityManagerFactory factory = emFactory;

		if (factory != null) {
			factory.close();
		}
	}

	@Override
	public EntityManager createEntityManager() {
		final EntityManagerFactory factory = getFactory();

		return factory.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	@Override
	public EntityManager createEntityManager(final Map params) {
		final EntityManagerFactory factory = getFactory();

		return factory.createEntityManager(params);
	}

	private EntityManagerFactory createNewEntityManagerFactory() {
		final Ejb3Configuration c = new Ejb3Configuration();
		c.setProperties(properties);

		for (final Bundle b : context.getBundles()) {
			final URL entities = b.getEntry(ENTITIES);
			if (entities == null) {
				continue;
			}

			logger.info("Adding entity classes from bundle '{}'", b
					.getSymbolicName());
			try {
				final InputStreamReader isr = new InputStreamReader(entities
						.openStream());
				final BufferedReader br = new BufferedReader(isr);
				String entity = null;
				while ((entity = br.readLine()) != null) {
					entity = entity.trim();
					if (entity.startsWith("#")) {
						continue;
					}

					logger
							.trace(
									"Adding entity class '{}' from bundle '{}' to Hibernate configuration",
									entity, b.getSymbolicName());
					try {
						c.addAnnotatedClass(b.loadClass(entity));
					} catch (final MappingException e) {
						logger.error("Error mapping entity class", e);
					} catch (final ClassNotFoundException e) {
						logger.error("Error loading entity class", e);
					}
				}
			} catch (final IOException e1) {
				logger.error("Error reading from bundle", e1);
			}

		}
		return c.buildEntityManagerFactory();
	}

	private EntityManagerFactory getFactory() {
		EntityManagerFactory factory = emFactory;

		if (factory != null) {
			return factory;
		}

		emFactory = factory = createNewEntityManagerFactory();

		return factory;
	}

	@Override
	public boolean isOpen() {
		final EntityManagerFactory factory = emFactory;

		if (factory == null) {
			return false;
		}

		return factory.isOpen();
	}

	/**
	 * ] * @see ManagedService#updated(Dictionary)
	 */
	@SuppressWarnings("unchecked")
	public void updated(final Dictionary dic) throws ConfigurationException {
		if (dic != null) {
			synchronized (properties) {
				final Enumeration<?> keys = dic.keys();

				while (keys.hasMoreElements()) {
					final String key = (String) keys.nextElement();
					properties.setProperty(key, (String) dic.get(key));
					logger.trace("Setting Hibernate property '{}' to '{}'",
							key, dic.get(key));
				}
				emFactory = null;
				logger
						.info("New JPA configuration. Marking SessionFactory out-of-date.");
			}
		}
	}

}
