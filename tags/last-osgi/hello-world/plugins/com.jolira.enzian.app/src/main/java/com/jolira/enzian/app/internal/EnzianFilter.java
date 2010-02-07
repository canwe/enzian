package com.jolira.enzian.app.internal;

import org.apache.wicket.protocol.http.WicketFilter;

import com.google.inject.Injector;

/**
 * A filter for Enzian so that the right injector can be added to the application by the activator.
 * 
 * @author Joachim Kainz
 *
 */
public abstract class EnzianFilter extends WicketFilter {
	public abstract Injector getInjector();
}
