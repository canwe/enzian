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
package com.jolira.enzian.app.internal;

import org.apache.wicket.Page;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import com.jolira.enzian.app.DefaultHomePage;
import com.jolira.enzian.app.EnzianApplication;
import com.jolira.enzian.app.EnzianSession;
import com.jolira.enzian.app.authentication.EnzianSignInPage;

/**
 * @author Joachim Kainz
 */
public final class EnzianApplicationImpl extends EnzianApplication {
    /**
     * @see EnzianApplication#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return DefaultHomePage.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return EnzianSignInPage.class;
    }

    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        // TODO Inject Session?
        return EnzianSessionImpl.class;
    }

}
