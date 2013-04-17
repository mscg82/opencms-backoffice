package com.mscg.dyndns.main.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

public class CommandLineOptions {

    private static Logger LOG = Logger.getLogger(CommandLineOptions.class);

    public static final String COMMAND_LINE_HELP        = "h";
    public static final String COMMAND_LINE_HELP_LONG   = "help";
    public static final String COMMAND_LINE_ENCODE      = "e";
    public static final String COMMAND_LINE_ENCODE_LONG = "encode";
    public static final String COMMAND_LINE_DECODE      = "d";
    public static final String COMMAND_LINE_DECODE_LONG = "decode";
    public static final String COMMAND_LINE_NOWAIT      = "nw";
    public static final String COMMAND_LINE_NOWAIT_LONG = "nowait";

    private String[] args;
    private Options options;
    private CommandLineParser parser;
    private CommandLine commandLine;
    private boolean printHelp;

    public boolean isPrintHelp() {
        return printHelp;
    }

    public void setPrintHelp(boolean printHelp) {
        this.printHelp = printHelp;
    }

    public CommandLineOptions(String[] args) {
        this.args = args;
        init();
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }

    public void init() {
        buildCommandLineOptions();

        // create the command line parser
        parser = new PosixParser();
        try {
            // parse the command line arguments
            commandLine = parser.parse(options, args);
            printHelp = commandLine.hasOption(COMMAND_LINE_HELP);
        } catch(ParseException e) {
            LOG.warn("Wrong parameter provided: " + e.getMessage() + "\n");

            // if there is a parse error, print the help
            printHelp = true;
        }
    }

    public Options buildCommandLineOptions() {
        // create command line options
        options = new Options();

        Option help = new Option(COMMAND_LINE_HELP, COMMAND_LINE_HELP_LONG, false, "Show usage");
        help.setRequired(false);
        options.addOption(help);

        Option encode = new Option(COMMAND_LINE_ENCODE, COMMAND_LINE_ENCODE_LONG, true, "Encode password");
        encode.setArgName("password");
        encode.setRequired(false);
        options.addOption(encode);

        Option decode = new Option(COMMAND_LINE_DECODE, COMMAND_LINE_DECODE_LONG, true, "Decode password");
        decode.setArgName("encoded password");
        decode.setRequired(false);
        options.addOption(decode);

        Option nowait = new Option(COMMAND_LINE_NOWAIT, COMMAND_LINE_NOWAIT_LONG, false, "Skip waiting for user interaction when no process will be launched");
        nowait.setRequired(false);
        options.addOption(nowait);

        return options;
    }

    public void printHelp() {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <jar_name> [<options>]", options);
    }

}
