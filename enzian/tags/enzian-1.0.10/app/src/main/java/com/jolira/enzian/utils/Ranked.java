/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.utils;

/**
 * Ranked interfaces can be sorted by the {@link Sorter}.
 * 
 * @author jfk
 * @date Sep 20, 2010 11:12:42 PM
 * @since 1.0
 */
public interface Ranked {
    /**
     * Return the rank of the implementation. The implementations are sorted by the rank. The implementation with the
     * highest rank is used first.
     * 
     * @return the rank of the page
     */
    public double getRank();
}
