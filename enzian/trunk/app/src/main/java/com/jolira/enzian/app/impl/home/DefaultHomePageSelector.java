package com.jolira.enzian.app.impl.home;

import org.apache.wicket.Page;

import com.jolira.enzian.app.HomePageSelector;

/**
 * @author jfk
 * @date Sep 26, 2010 10:09:44 PM
 * @since 1.0
 */
public class DefaultHomePageSelector implements HomePageSelector {

    @Override
    public Class<? extends Page> getHomePage() {
        return DefaultHome.class;
    }

    /**
     * @return always s{@literal 1.0}
     */
    @Override
    public double getRank() {
        return 1.0;
    }
}
