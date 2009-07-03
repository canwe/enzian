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
package com.jolira.enzian.hibernate.internal.osgi;

import static org.ops4j.peaberry.Peaberry.osgiModule;

import javax.persistence.EntityManagerFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.jolira.enzian.hibernate.internal.EntityManagerFactoryProxy;

public class Activator implements BundleActivator {

    @Override
    public void start(final BundleContext context) throws Exception {

        // Create a Guice Module
        final Module module = new AbstractModule() {
            @Override
            protected void configure() {
                // Use the Guice Peaberry extension
                final Module osgiModule = osgiModule(context);

                install(osgiModule);

                // Bind a OSGi service reference in this module
                final AnnotatedBindingBuilder<EntityManagerFactory> builder = bind(EntityManagerFactory.class);

                builder.to(EntityManagerFactoryProxy.class);

            }
        };

        Guice.createInjector(module);
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        // TODO Auto-generated method stub

    }

}
