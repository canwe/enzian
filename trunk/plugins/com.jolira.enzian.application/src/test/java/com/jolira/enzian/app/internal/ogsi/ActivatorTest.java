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
package com.jolira.enzian.app.internal.ogsi;

import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.container.def.PaxRunnerOptions.logProfile;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

@RunWith(JUnit4TestRunner.class)
public class ActivatorTest {
    @Configuration
    public static Option[] configure() {
        return options(logProfile());
    }

    @org.ops4j.pax.exam.Inject
    BundleContext bundleContext = null;

    /**
     * You will get a list of bundles installed by default plus your testcase,
     * wrapped into a bundle called pax-exam-probe
     */
    @Test
    public void listBundles() {
        System.out.println("Hello World from "
                + bundleContext.getBundle().getSymbolicName());

        final Bundle[] bundles = bundleContext.getBundles();

        for (Bundle b : bundles) {
            System.out.println("Bundle " + b.getBundleId() + " : "
                    + b.getSymbolicName());
        }
    }

    @Test
    public void testLifeCycle() {
        // TODO: Test something
    }
}
