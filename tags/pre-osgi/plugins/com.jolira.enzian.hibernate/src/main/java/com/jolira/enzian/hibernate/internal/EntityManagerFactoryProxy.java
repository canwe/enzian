/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General
 * Public License, Version 3.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.jolira.enzian.hibernate.internal;

import static org.slf4j.LoggerFactory.getLogger;

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

public class EntityManagerFactoryProxy implements EntityManagerFactory,
        ManagedService {

    private static final Logger LOG = getLogger(EntityManagerFactoryProxy.class);

    private BundleContext context;

    private static String entities = "/OSGI-INF/jpa/classes";

    private final Properties properties = new Properties();

    private EntityManagerFactory factory = null;

// BundleListener bundleListener = new BundleListener() {
// public void bundleChanged(final BundleEvent event) {
// if (event.getType() == BundleEvent.RESOLVED
// || event.getType() == BundleEvent.UNRESOLVED) {
// final Bundle bundle = event.getBundle();
// URL entry = bundle.getEntry(entities);
// if (entry != null) {
// factory = null;
// LOGGER.info(
// "Bundle '{}' with JPA configuration changed status. "
// + "Marking SessionFactory out-of-date.",
// bundle);
// }
// }
// }
// };

    @Override
    public void close() {
        final EntityManagerFactory real = factory;

        if (real != null) {
            real.close();
        }
    }

    @Override
    public EntityManager createEntityManager() {
        EntityManagerFactory real = getEntityManagerFactory();

        return real.createEntityManager();
    }

    @SuppressWarnings("unchecked")
    @Override
    public EntityManager createEntityManager(final Map map) {
        EntityManagerFactory real = getEntityManagerFactory();

        return real.createEntityManager(map);
    }

    private EntityManagerFactory createNewEntityManagerFactory() {
        Ejb3Configuration c = new Ejb3Configuration();
        c.setProperties(properties);

        for (Bundle b : context.getBundles()) {
            URL url = b.getEntry(entities);
            if (url == null) {
                continue;
            }

            LOG.info("Adding entity classes from bundle '{}'", b
                    .getSymbolicName());
            try {
                InputStreamReader isr = new InputStreamReader(url.openStream());
                BufferedReader br = new BufferedReader(isr);
                String entity = null;
                while ((entity = br.readLine()) != null) {
                    entity = entity.trim();
                    if (entity.startsWith("#")) {
                        continue;
                    }

                    //LOG.trace("Adding entity class '{}' from bundle '{}' "
                    //        + "to Hibernate configuration", entity, b
                    //        .getSymbolicName());
                    try {
                        c.addAnnotatedClass(b.loadClass(entity));
                    }
                    catch (MappingException e) {
                        LOG.error("Error mapping entity class", e);
                    }
                    catch (ClassNotFoundException e) {
                        LOG.error("Error loading entity class", e);
                    }
                }
            }
            catch (IOException e1) {
                LOG.error("Error reading from bundle", e1);
            }

        }
        return c.buildEntityManagerFactory();
    }

    public EntityManager getEntityManager() {
        EntityManagerFactory real = getEntityManagerFactory();

        return real.createEntityManager();
    }

    private synchronized EntityManagerFactory getEntityManagerFactory() {
        EntityManagerFactory real = factory;

        if (real != null) {
            return real;
        }

        //LOG.trace("Creating a new SessionFactory.");

        factory = real = createNewEntityManagerFactory();

        return real;
    }

    @Override
    public boolean isOpen() {
        final EntityManagerFactory real = factory;

        if (real != null) {
            return real.isOpen();
        }

        return false;
    }

    public void reset() {
        //LOG.trace("Resetting EntityManagerFactory");
        factory = null;
    }

    /**
     * @see ManagedService#updated(java.util.Dictionary)
     */
    @SuppressWarnings("unchecked")
    public void updated(final Dictionary dic) throws ConfigurationException {
        if (dic != null) {
            synchronized (properties) {
                Enumeration<?> keys = dic.keys();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    properties.setProperty(key, (String) dic.get(key));
                    //LOG.trace("Setting Hibernate property '{}' to '{}'", key,
                    //        dic.get(key));
                }
                factory = null;
                LOG
                        .info("New JPA configuration. Marking SessionFactory out-of-date.");
            }
        }
    }

}
