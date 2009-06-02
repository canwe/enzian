package com.jolira.enzian.loader.internal;

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.jolira.enzian.loader.ExampleService;

/**
 * @author jfk
 * @date Apr 26, 2009 9:15:51 PM
 * @since 1.0
 */
public class ExampleServiceImplTest extends TestCase {
    /**
     * 
     */
    public void testExampleServiceScramble() {
        ExampleService anExampleService = new ExampleServiceImpl();

        String in = "This is a test of the text scrambling service";
        String out = anExampleService.scramble(in);

        char[] inChars = in.toCharArray();
        char[] outChars = out.toCharArray();

        Arrays.sort(inChars);
        Arrays.sort(outChars);

        Assert.assertEquals("Uses same letters", new String(inChars),
                new String(outChars));
    }
}
