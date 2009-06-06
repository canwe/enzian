package com.jolira.enzian.app.internal;

import com.jolira.enzian.app.Application;

/**
 * Internal implementation of our example OSGi service
 */
public final class ApplicationImpl implements Application {
	public String scramble( final String text )	{
		return text;
	}
}

