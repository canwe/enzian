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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import com.jolira.enzian.app.authentication.EnzianSignOutPage;

/**
 * @author Gabriel Hopper
 */
public class TopNavigationPanel extends Panel {

    public TopNavigationPanel(String id) {
        super(id);

        // TODO: iterate through list of HTML markup and components.

        // TODO: get user name from sign-in.
        add(new Label("userName", "User Name HERE"));

        add(new Link("signOutLink") {
            @Override
            public void onClick() {
                setResponsePage(EnzianSignOutPage.class);
            }
        });

    }
}