/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.tasks.impl;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.code.joliratools.plugins.ManagedSingleton;
import com.jolira.enzian.tasks.ProgressIndiator;
import com.jolira.enzian.tasks.Task;
import com.jolira.enzian.tasks.TaskExecutor;

/**
 * Create a new executor.
 * 
 * @author jfk
 * @date Oct 30, 2010 10:40:57 PM
 * @since 1.0
 */
@ManagedSingleton
public class TaskExecutorImpl implements TaskExecutor {
    private final LinkedList<Task> tasks = new LinkedList<Task>();
    private final Executor executor;

    @Inject
    TaskExecutorImpl(@Named("TaskExecutorThreadCount") final int nThreads) {
        executor = Executors.newFixedThreadPool(nThreads);
    }

    @Override
    public void execute(final String name, final Runnable runnable, final ProgressIndiator indicator) {
        final TaskImpl task = new TaskImpl(name, runnable, indicator);

        executor.execute(task);

        synchronized (tasks) {
            tasks.addFirst(task);
        }
    }

    @Override
    public Task[] getTasks() {
        synchronized (tasks) {
            final int size = tasks.size();

            return tasks.toArray(new Task[size]);
        }
    }
}
