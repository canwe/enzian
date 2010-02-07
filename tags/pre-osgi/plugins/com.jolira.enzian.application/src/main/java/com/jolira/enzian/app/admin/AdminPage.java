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
package com.jolira.enzian.app.admin;

import org.apache.wicket.authentication.pages.SignOutPage;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

import com.jolira.enzian.app.DefaultHomePage;

/**
 * @author Gabriel Hopper
 */
@AuthorizeInstantiation("ADMIN")
public class AdminPage extends WebPage {

    public AdminPage() {
        super();
        add(new Link("homeLink") {

            @Override
            public void onClick() {
                setResponsePage(DefaultHomePage.class);

            }

        });
        add(new Link("signOutLink") {

            @Override
            public void onClick() {
                setResponsePage(SignOutPage.class);

            }

        });
    }

}
