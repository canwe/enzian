/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.app;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;

/**
 * @author jfk
 * @date Sep 26, 2010 10:48:03 PM
 * @since 1.0
 */
public class EnzianBase extends WebPage {
    /**
     *
     */
    public EnzianBase() {
        // nothing
    }

    /**
     * Create a new base.
     * 
     * @param params
     *            wicket parameters
     */
    public EnzianBase(final PageParameters params) {
        super(params);
    }
}
