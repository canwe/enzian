/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.tasks.impl;

import static com.jolira.enzian.tasks.Status.CANCELED;
import static com.jolira.enzian.tasks.Status.CANCELLING;
import static com.jolira.enzian.tasks.Status.COMPLETED;
import static com.jolira.enzian.tasks.Status.FAILED;
import static com.jolira.enzian.tasks.Status.PENDING;
import static com.jolira.enzian.tasks.Status.RUNNING;

import com.jolira.enzian.tasks.ProgressIndiator;
import com.jolira.enzian.tasks.Status;
import com.jolira.enzian.tasks.Task;

/**
 * @author jfk
 * @date Oct 30, 2010 11:06:48 PM
 * @since 1.0
 */
final class TaskImpl implements Task, Runnable {
    private final long submitted = System.currentTimeMillis();
    private long started = -1;
    private long ended = -1;
    private final String name;
    private final Runnable runnable;
    private final ProgressIndiator indicator;
    private Status status = PENDING;
    private Thread thread = null;
    private Throwable failure;

    TaskImpl(final String name, final Runnable runnable, final ProgressIndiator indicator) {
        this.name = name;
        this.runnable = runnable;
        this.indicator = indicator;
    }

    @Override
    public void cancel() {
        final Status s = status;

        if (!RUNNING.equals(s) && !PENDING.equals(s)) {
            return;
        }

        status = CANCELLING;

        final Thread current = thread;

        if (current != null) {
            current.interrupt();
        }
    }

    /**
     * @return the ended
     */
    @Override
    public long getEnded() {
        return ended;
    }

    /**
     * @return the failure
     */
    @Override
    public Throwable getFailure() {
        return failure;
    }

    /**
     * @return the indicator
     */
    @Override
    public ProgressIndiator getIndicator() {
        return indicator;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the started
     */
    @Override
    public long getStarted() {
        return started;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    /**
     * @return the submitted
     */
    @Override
    public long getSubmitted() {
        return submitted;
    }

    @Override
    public void run() {
        switch (status) {
        case CANCELLING:
            status = CANCELED;
            return;

        case CANCELED:
        case COMPLETED:
        case FAILED:
        case RUNNING:
            throw new Error("invalid state " + status);

        case PENDING:
            break;
        }

        status = RUNNING;
        started = System.currentTimeMillis();
        thread = Thread.currentThread();

        try {
            runnable.run();
        } catch (final Throwable e) {
            status = FAILED;
            failure = e;
            return;
        } finally {
            ended = System.currentTimeMillis();
            thread = null;
        }

        status = COMPLETED;
    }
}
