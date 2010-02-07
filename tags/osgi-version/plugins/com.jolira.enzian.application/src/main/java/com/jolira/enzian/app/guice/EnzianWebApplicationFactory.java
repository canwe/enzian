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

import org.apache.wicket.authentication.AuthenticatedWebApplication;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.IWebApplicationFactory;
import org.apache.wicket.protocol.http.WicketFilter;

import com.google.inject.Injector;
import com.jolira.enzian.app.internal.EnzianFilter;

/**
 * A factory for creating applications using an {@link EnzianFilter}. This class
 * creates the {@link WebApplication) used by Wicket. It uses the injector
 * returned by the {@link EnzianFilter} to create the class.
 * 
 * @author Joachim Kainz
 */
public class EnzianWebApplicationFactory implements IWebApplicationFactory {
    public AuthenticatedWebApplication createApplication(final WicketFilter filter) {
        if (!(filter instanceof EnzianFilter)) {
            throw new IllegalArgumentException("Filter must be a WicketFilter");
        }

        final EnzianFilter enzianFilter = (EnzianFilter) filter;
        final Injector injector = enzianFilter.getInjector();
        final AuthenticatedWebApplication app = injector.getInstance(AuthenticatedWebApplication.class);

        app.addComponentInstantiationListener(new GuiceComponentInjector(app,
                injector));

        return app;
    }

}
