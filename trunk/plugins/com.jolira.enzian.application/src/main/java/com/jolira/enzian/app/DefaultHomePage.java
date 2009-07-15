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
package com.jolira.enzian.app;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import com.jolira.enzian.app.admin.AdminPage;

/**
 * @author Joachim Kainz
 */
public class DefaultHomePage extends WebPage {

    public DefaultHomePage() {
        super();
        add(new Link("adminLink") {

            @Override
            public void onClick() {
                setResponsePage(AdminPage.class);

            }

        });
    }

    // nothing yet
}
