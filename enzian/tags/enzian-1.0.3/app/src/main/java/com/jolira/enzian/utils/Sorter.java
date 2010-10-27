/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Sorts ranked interfaces.
 * 
 * @author jfk
 * @date Sep 20, 2010 11:11:40 PM
 * @since 1.0
 */
public class Sorter {
    /**
     * Sort ranked interfaces.
     * 
     * @param <T>
     *            the type of interface to be ranked
     * @param unsorted
     *            the interfaces sort by rank
     * @return the sorted list
     */
    public static <T extends Ranked> Collection<T> sort(final Set<T> unsorted) {
        final ArrayList<T> list = new ArrayList<T>(unsorted);

        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(final T o1, final T o2) {
                final double r1 = o1.getRank();
                final double r2 = o2.getRank();

                if (r1 < r2) {
                    return -1;
                }

                if (r1 > r2) {
                    return 1;
                }

                return 0;
            }
        });

        return list;
    }
}
