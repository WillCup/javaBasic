package com.will.tooljars.jopt;
import static org.junit.Assert.*;

import org.junit.Test;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class ShortOptionsTest {
    @Test
    public void supportsShortOptions() {
        OptionParser parser = new OptionParser( "aB?*." );

        OptionSet options = parser.parse( "-a", "-B", "-?" );

        assertTrue( options.has( "a" ) );
        assertTrue( options.has( "B" ) );
        assertTrue( options.has( "?" ) );
        assertFalse( options.has( "." ) );
    }
}