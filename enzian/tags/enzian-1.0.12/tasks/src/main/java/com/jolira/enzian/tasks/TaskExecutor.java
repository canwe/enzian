/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.tasks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Execute and manage tasks.
 * 
 * @author jfk
 * @date Oct 30, 2010 10:00:05 PM
 * @since 1.0
 */
public interface TaskExecutor {
    /**
     * Execute a task.
     * 
     * @param name
     *            the name to be displayed to users
     * @param runnable
     *            the runnable be executed
     * @param indicator
     *            an interface providing information about progress of the task; may be {@literal null}.
     * @return the newly created task
     */
    public Task execute(@Nonnull String name, @Nonnull Runnable runnable, @Nullable ProgressIndiator indicator);

    /**
     * @return all currently known tasks
     */
    public Task[] getTasks();
}
