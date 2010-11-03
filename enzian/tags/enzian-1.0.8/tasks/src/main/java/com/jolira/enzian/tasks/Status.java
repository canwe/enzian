/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.tasks;

/**
 * The status of an task being executed.
 * 
 * @author jfk
 * @date Oct 30, 2010 10:10:32 PM
 * @since 1.0
 */
public enum Status {
    /**
     * not started yet
     */
    PENDING,
    /**
     * currently running
     */
    RUNNING,
    /**
     * Completed successfully
     */
    COMPLETED,
    /**
     * Failed with an error
     */
    FAILED,
    /**
     * trying to cancel as a result of a user action
     */
    CANCELLING,
    /**
     * cancelled as a result of a user action
     */
    CANCELED
}
