/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.app;

import java.util.Collection;
import java.util.Set;

import org.apache.wicket.Page;
import org.apache.wicket.request.target.coding.IRequestTargetUrlCodingStrategy;
import org.apache.wicket.settings.IResourceSettings;
import org.apache.wicket.util.resource.locator.IResourceStreamLocator;

import com.google.code.joliratools.guicier.GuicierWebApplication;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.jolira.enzian.utils.Sorter;

/**
 * @author jfk
 * @date Sep 20, 2010 10:06:16 PM
 * @since 1.0
 */
public class EnzianWebApplication extends GuicierWebApplication {
    @Override
    public Class<? extends Page> getHomePage() {
        final TypeLiteral<Set<HomePageSelector>> literal = new TypeLiteral<Set<HomePageSelector>>() {
            // nothing
        };
        final Injector i = getInjector();
        final Key<Set<HomePageSelector>> key = Key.get(literal);
        final Set<HomePageSelector> selectors = i.getInstance(key);
        final Collection<HomePageSelector> sorted = Sorter.sort(selectors);

        for (final HomePageSelector selector : sorted) {
            final Class<? extends Page> page = selector.getHomePage();

            if (page != null) {
                return page;
            }
        }

        throw new Error("No home page selector registered.");
    }

    @Override
    protected void init() {
        super.init();

        final TypeLiteral<Set<IRequestTargetUrlCodingStrategy>> setOfStrategies = new TypeLiteral<Set<IRequestTargetUrlCodingStrategy>>() {
            //
        };
        final Key<Set<IRequestTargetUrlCodingStrategy>> setOfStrategiesKey = Key.get(setOfStrategies);
        final Injector i = getInjector();
        final Collection<IRequestTargetUrlCodingStrategy> strategies = i.getInstance(setOfStrategiesKey);

        for (final IRequestTargetUrlCodingStrategy strategy : strategies) {
            mount(strategy);
        }

        final IResourceSettings resourceSettings = getResourceSettings();
        final IResourceStreamLocator resourceStreamLocator = resourceSettings.getResourceStreamLocator();
        final StrippingResourceStreamLocator strippingLocator = new StrippingResourceStreamLocator(
                resourceStreamLocator);

        resourceSettings.setResourceStreamLocator(strippingLocator);
    }
}
