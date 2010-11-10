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

import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolira.enzian.tasks.ProgressIndiator;
import com.jolira.enzian.tasks.Status;
import com.jolira.enzian.tasks.Task;

/**
 * @author jfk
 * @date Oct 30, 2010 11:06:48 PM
 * @since 1.0
 */
final class TaskImpl implements Task, Runnable {
    private final static Logger LOG = LoggerFactory.getLogger(TaskImpl.class);

    private static Throwable getRootCause(final Throwable e) {
        final Throwable cause = e.getCause();

        if (cause == null) {
            return e;
        }

        if (cause == e) {
            return e;
        }

        return getRootCause(cause);
    }

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
        LOG.info("cancelling " + this);

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
     * @throws Error
     */
    private void doRun() throws Error {
        switch (status) {
        case CANCELLING:
            status = CANCELED;
            return;

        case CANCELED:
        case COMPLETED:
        case FAILED:
        case RUNNING:
            status = FAILED;
            failure = new Error("invalid state " + status);
            return;

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
        LOG.info("running task " + this);

        try {
            doRun();
        } finally {
            if (failure != null) {
                LOG.error("failure while running " + this, getRootCause(failure));
            } else {
                LOG.info("exiting task " + this);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TaskImpl [submitted=");
        builder.append(new Date(submitted));

        if (started != -1) {
            builder.append(", started=");
            builder.append(new Date(started));
        }

        if (ended != -1) {
            builder.append(", ended=");
            builder.append(ended);
        }

        builder.append(", name=");
        builder.append(name);
        builder.append(", runnable=");
        builder.append(runnable);
        builder.append(", ");

        if (indicator != null) {
            builder.append(", indicator=");
            builder.append(indicator);
        }

        builder.append(", status=");
        builder.append(status);

        if (thread != null) {
            builder.append(", thread=");
            builder.append(thread);
        }

        if (failure != null) {
            builder.append(", failure=");
            builder.append(failure);
        }

        builder.append("]");

        return builder.toString();
    }
}
