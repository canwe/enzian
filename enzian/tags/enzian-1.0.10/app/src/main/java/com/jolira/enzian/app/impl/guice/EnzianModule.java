/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.app.impl.guice;

import org.apache.wicket.request.target.coding.IRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.coding.SharedResourceRequestTargetUrlCodingStrategy;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;
import com.jolira.enzian.app.HomePageSelector;
import com.jolira.enzian.app.impl.home.DefaultHomePageSelector;

/**
 * Create the basic injections for Enzian.
 * 
 * @author jfk
 * @date Oct 18, 2010 4:12:52 PM
 * @since 1.0
 */
public class EnzianModule implements Module {
    @Override
    public void configure(final Binder binder) {
        final Multibinder<HomePageSelector> homeBinder = Multibinder.newSetBinder(binder, HomePageSelector.class);

        homeBinder.addBinding().to(DefaultHomePageSelector.class);

        final Multibinder<IRequestTargetUrlCodingStrategy> strategiesBinder = Multibinder.newSetBinder(binder,
                IRequestTargetUrlCodingStrategy.class);

        strategiesBinder.addBinding().toInstance(
                new SharedResourceRequestTargetUrlCodingStrategy("jolira.css", EnzianModule.class.getName()
                        + "/jolira.css"));
        strategiesBinder.addBinding().toInstance(
                new SharedResourceRequestTargetUrlCodingStrategy("favicon.ico", EnzianModule.class.getName()
                        + "/favicon.ico"));
    }
}
