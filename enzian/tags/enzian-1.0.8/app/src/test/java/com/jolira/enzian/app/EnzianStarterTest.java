/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.app;

import org.junit.Test;

/**
 * @author jfk
 * @date Oct 18, 2010 2:41:27 PM
 * @since 1.0
 */
public class EnzianStarterTest {
    /**
     * Test method for {@link EnzianStarter#main(java.lang.String[])}.
     * 
     * @throws Exception
     */
    @Test
    public void testHelpMessage() throws Exception {
        EnzianStarter.main(new String[] { "-?" });
    }

    /**
     * Test method for {@link EnzianStarter#main(java.lang.String[])}.
     * 
     * @throws Exception
     */
    @Test(expected = InterruptedException.class)
    public void testStartMessage() throws Exception {
        final Thread current = Thread.currentThread();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    throw new Error(e);
                }
                current.interrupt();
            }
        }).start();

        EnzianStarter.main(new String[] {});
    }
}
