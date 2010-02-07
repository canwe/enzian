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

import org.apache.wicket.IPageMap;
import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.jolira.enzian.app.authentication.EnzianSignOutPage;

/**
 * @author Gabriel Hopper
 */
@AuthorizeInstantiation("ADMIN")
public class BaseAdminPage extends WebPage {

    public BaseAdminPage() {
        super();

        add(new TopNavigationPanel("topNavigationPanel"));
        add(new HeaderPanel("headerPanel"));
        // add(new DefaultAdminPage("tabPanel"));

    }

    public BaseAdminPage(IModel<?> model) {
        super(model);
        // TODO Auto-generated constructor stub
    }

    public BaseAdminPage(IPageMap pageMap, IModel<?> model) {
        super(pageMap, model);
        // TODO Auto-generated constructor stub
    }

    public BaseAdminPage(IPageMap pageMap, PageParameters parameters) {
        super(pageMap, parameters);
        // TODO Auto-generated constructor stub
    }

    public BaseAdminPage(IPageMap pageMap) {
        super(pageMap);
        // TODO Auto-generated constructor stub
    }

    public BaseAdminPage(PageParameters parameters) {
        super(parameters);
        // TODO Auto-generated constructor stub
    }

}
