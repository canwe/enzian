/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.tasks.impl.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.jolira.enzian.tasks.TaskExecutor;
import com.jolira.enzian.tasks.impl.TaskExecutorImpl;

/**
 * @author jfk
 * @date Oct 30, 2010 10:43:47 PM
 * @since 1.0
 */
public class EnzianModule implements Module {
    @Override
    public void configure(final Binder binder) {
        binder.bind(TaskExecutor.class).to(TaskExecutorImpl.class);
    }
}
