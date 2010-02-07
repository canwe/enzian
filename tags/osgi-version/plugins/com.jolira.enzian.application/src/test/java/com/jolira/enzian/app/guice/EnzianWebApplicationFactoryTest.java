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

package com.jolira.enzian.app.guice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.junit.Test;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.jolira.enzian.app.internal.EnzianFilter;

/**
 * @author Joachim F. Kainz
 */
public class EnzianWebApplicationFactoryTest {
    protected abstract class InjectorImplementation implements Injector {
        @Override
        public Injector createChildInjector(
                final Iterable<? extends Module> arg0) {
            fail();
            return null;
        }

        @Override
        public Injector createChildInjector(final Module... arg0) {
            fail();
            return null;
        }

        @Override
        public <T> List<Binding<T>> findBindingsByType(final TypeLiteral<T> arg0) {
            fail();
            return null;
        }

        @Override
        public <T> Binding<T> getBinding(final Class<T> arg0) {
            fail();
            return null;
        }

        @Override
        public <T> Binding<T> getBinding(final Key<T> arg0) {
            fail();
            return null;
        }

        @Override
        public Map<Key<?>, Binding<?>> getBindings() {
            fail();
            return null;
        }

        @Override
        public <T> T getInstance(final Key<T> arg0) {
            fail();
            return null;
        }

        @Override
        public <T> MembersInjector<T> getMembersInjector(final Class<T> arg0) {
            fail();
            return null;
        }

        @Override
        public <T> MembersInjector<T> getMembersInjector(
                final TypeLiteral<T> arg0) {
            fail();
            return null;
        }

        @Override
        public Injector getParent() {
            fail();
            return null;
        }

        @Override
        public <T> Provider<T> getProvider(final Class<T> arg0) {
            fail();
            return null;
        }

        @Override
        public <T> Provider<T> getProvider(final Key<T> arg0) {
            fail();
            return null;
        }

        @Override
        public void injectMembers(final Object arg0) {
            fail();

        }
    }

    /**
     * Test method for
     * {@link EnzianWebApplicationFactory#createApplication(WicketFilter)} .
     */
    @Test
    public void testCreateApplication() {
        final EnzianWebApplicationFactory factory = new EnzianWebApplicationFactory();
        final WebApplication expected = new WebApplication() {

            @Override
            public Class<? extends Page> getHomePage() {
                fail();
                return null;
            }
        };

        final WebApplication app = factory
                .createApplication(new EnzianFilter() {
                    @Override
                    public Injector getInjector() {
                        return new InjectorImplementation() {

                            @SuppressWarnings("unchecked")
                            @Override
                            public <T> T getInstance(final Class<T> cls) {
                                assertEquals(WebApplication.class, cls);

                                return (T) expected;
                            }
                        };
                    }
                });

        assertEquals(expected, app);
    }

    /**
     * Test method for
     * {@link EnzianWebApplicationFactory#createApplication(WicketFilter)}
     * Verify that a non-enzian filter creates an invalid argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateApplicationInvalidArg() {
        EnzianWebApplicationFactory factory = new EnzianWebApplicationFactory();

        factory.createApplication(new WicketFilter() {
            // nothing
        });
    }
}
