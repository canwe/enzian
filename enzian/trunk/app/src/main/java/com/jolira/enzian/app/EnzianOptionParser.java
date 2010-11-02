/**
 * Copyright (c) 2010 jolira. All rights reserved. This program and the accompanying materials are made available under
 * the terms of the GNU Public License 2.0 which is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package com.jolira.enzian.app;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.PrintStream;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.OptionSpecBuilder;

/**
 * A command line passer for Enzian.
 * 
 * @author jfk
 * @date Oct 31, 2010 11:54:40 AM
 * @since 1.0
 */
class EnzianOptionParser {
    private static final String PORT_ARG = "p";
    private final OptionSpec<Integer> portOption;
    private final OptionSpec<?> helpOption;
    private final OptionParser parser = new OptionParser();
    private OptionSet options = null;

    EnzianOptionParser() {
        portOption = createPortOption(parser);
        helpOption = createHelpOption(parser);
    }

    /**
     * Create the help option.
     * 
     * @param p
     *            the parser to be used
     * @return the help option
     */
    protected OptionSpec<?> createHelpOption(final OptionParser p) {
        return p.acceptsAll(asList("h", "?"), "show help");
    }

    /**
     * Creates the port option. Override for different default ports, etc.
     * 
     * @param p
     *            the parser
     * @return the option.
     */
    protected OptionSpec<Integer> createPortOption(final OptionParser p) {
        final OptionSpecBuilder b1 = p.accepts(PORT_ARG);
        final ArgumentAcceptingOptionSpec<String> b2 = b1.withRequiredArg();
        final ArgumentAcceptingOptionSpec<Integer> b3 = b2.ofType(Integer.class);
        final ArgumentAcceptingOptionSpec<Integer> b4 = b3.describedAs("listen port");

        return b4.defaultsTo(Integer.valueOf(9876));
    }

    /**
     * @return the port specified by the user.
     */
    public int getPort() {
        final Integer port = options.valueOf(portOption);

        return port.intValue();
    }

    /**
     * @return {@literal true} to indicate that the user requested to see help.
     * @param args
     *            parse the arguments
     */
    public boolean parse(final String[] args) {
        options = parser.parse(args);

        return options.has(helpOption);
    }

    /**
     * Print help information.
     * 
     * @param out
     *            the target
     * @throws IOException
     *             could not print
     */
    public void printHelp(final PrintStream out) throws IOException {
        parser.printHelpOn(out);
    }
}
