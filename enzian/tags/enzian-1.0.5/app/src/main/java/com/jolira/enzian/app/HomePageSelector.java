/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.app;

import org.apache.wicket.markup.html.WebPage;

import com.jolira.enzian.utils.Ranked;

/**
 * This interface allows plugins to add a home page. Implementations may register implementation using a multibinder.
 * 
 * @author jfk
 * @date Sep 20, 2010 10:42:57 PM
 * @since 1.0
 */
public interface HomePageSelector extends Ranked {
    /**
     * Returns the home page or {@literal null}, if this selector does not provide a home page.
     * 
     * @return the home page or {@literal null}.
     */
    public Class<? extends WebPage> getHomePage();
}
