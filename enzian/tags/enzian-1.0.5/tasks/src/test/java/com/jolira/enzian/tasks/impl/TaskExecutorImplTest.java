/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.tasks.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.jolira.enzian.tasks.Task;

/**
 * @author jfk
 * @date Oct 31, 2010 12:18:05 AM
 * @since 1.0
 */
public class TaskExecutorImplTest {
    /**
     */
    @Test
    public void testTaskExecutorImpl() {
        final TaskExecutorImpl executor = new TaskExecutorImpl();

        executor.execute("test", new Runnable() {
            @Override
            public void run() {
                // nothing
            }
        }, null);

        final Task[] tasks = executor.getTasks();

        assertEquals(1, tasks.length);
    }
}
