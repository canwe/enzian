/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */
package com.jolira.enzian.tasks.impl;

import static com.jolira.enzian.tasks.Status.CANCELED;
import static com.jolira.enzian.tasks.Status.CANCELLING;
import static com.jolira.enzian.tasks.Status.PENDING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.jolira.enzian.tasks.ProgressIndiator;

/**
 * @author jfk
 * @date Oct 30, 2010 11:49:29 PM
 * @since 1.0
 */
public class TaskImplTest {
    /**
    *
    */
    @Test
    public void testCancel() {
        final TaskImpl task = new TaskImpl("test", new Runnable() {
            @Override
            public void run() {
                // nothing
            }
        }, new ProgressIndiator() {
            // nothing
        });

        task.cancel();
        assertEquals(CANCELLING, task.getStatus());
        task.run();
        assertEquals(CANCELED, task.getStatus());
    }

    /**
    *
    */
    @Test(expected = Error.class)
    public void testError() {
        final TaskImpl task = new TaskImpl("test", new Runnable() {
            @Override
            public void run() {
                //
            }
        }, new ProgressIndiator() {
            // nothing
        });

        task.run();
        task.cancel();
        task.run();
    }

    /**
    *
    */
    @Test
    public void testFailure() {
        final TaskImpl task = new TaskImpl("test", new Runnable() {
            @Override
            public void run() {
                throw new Error();
            }
        }, new ProgressIndiator() {
            // nothing
        });

        task.run();
        assertNotNull(task.getFailure());
    }

    /**
     *
     */
    @Test
    public void testTaskImpl() {
        final TaskImpl task = new TaskImpl("test", new Runnable() {
            @Override
            public void run() {
                // nothing
            }
        }, new ProgressIndiator() {
            // nothing
        });

        task.getSubmitted();
        assertEquals(PENDING, task.getStatus());
        assertEquals(-1, task.getEnded());
        assertEquals(-1, task.getStarted());
        assertEquals("test", task.getName());
        assertNull(task.getFailure());
        assertNotNull(task.getIndicator());
        task.run();
    }
}
