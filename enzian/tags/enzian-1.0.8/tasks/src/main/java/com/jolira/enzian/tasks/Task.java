/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.tasks;

/**
 * @author jfk
 * @date Oct 30, 2010 10:06:37 PM
 * @since 1.0
 */
public interface Task {
    /**
     * Cancel the task.
     */
    void cancel();

    /**
     * @return the time in milliseconds when the task ended or {@literal -1} if it is still pending or in progress.
     */
    long getEnded();

    /**
     * @return {@literal null} unless the status if {@link Status#FAILED}.
     */
    Throwable getFailure();

    /**
     * @return the progress indicator for the task or {@literal null}
     */
    ProgressIndiator getIndicator();

    /**
     * @return the name
     */
    String getName();

    /**
     * @return the time in milliseconds when the task was started or {@literal -1} if it is not running yet.
     */
    long getStarted();

    /**
     * @return the status
     */
    public Status getStatus();

    /**
     * @return the time in milliseconds when the task was submitted.
     */
    long getSubmitted();
}
