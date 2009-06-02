package com.jolira.enzian.loader.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jolira.enzian.loader.ExampleService;

/**
 * Internal implementation of our example OSGi service
 */
public final class ExampleServiceImpl
    implements ExampleService
{
    // implementation methods go here...

    public String scramble( String text )
    {
        List<Character> charList = new ArrayList<Character>();

        char[] textChars = text.toCharArray();
        for( int i = 0; i < textChars.length; i++ )
        {
            charList.add( new Character( textChars[i] ) );
        }

        Collections.shuffle( charList );

        char[] mixedChars = new char[text.length()];
        for( int i = 0; i < mixedChars.length; i++ )
        {
            mixedChars[i] = charList.get( i ).charValue();
        }

        return new String( mixedChars );
    }
}

