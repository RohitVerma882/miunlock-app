package com.rv882.miunlock;

import com.rv882.miunlock.unlock.UnlockManager;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import com.rv882.miunlock.model.Argument;

public class MiUnlock {
    public static final String TAG = MiUnlock.class.getSimpleName();

    public static void main(String... args) {
        Options options = new Options();
        options.addOption("v", "verbose", false, "Enable verbose logging");
        options.addRequiredOption("u", "user", true, "Mi account user (like email and mobile no.)");
        options.addRequiredOption("p", "password", true, "Mi account password");
        options.addRequiredOption("t", "token", true, "Device var token id");
        options.addRequiredOption("pr", "product", true, "Device var product id");
		options.addRequiredOption("uid", "userId", true, "Mi account user id");
        options.addRequiredOption("pt", "passToken", true, "Mi account passToken");
		options.addOption("d", "deviceId", true, "Mi account deviceId");

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();

        if (args.length == 0) {
            formatter.printHelp(TAG, options, true);
            System.exit(0);
        }

        Argument arg = Argument.getInstance();
        CommandLine cmd = null;
		
        try {
            cmd = parser.parse(options, args, true);
            if (cmd.hasOption("v")) {
                arg.setVerbose(true);
            }

            if (cmd.hasOption("u")) {
                arg.setUser(cmd.getOptionValue("u"));
            }

            if (cmd.hasOption("p")) {
                arg.setPassword(cmd.getOptionValue("p"));
            }

            if (cmd.hasOption("t")) {
                arg.setToken(cmd.getOptionValue("t"));
            }

            if (cmd.hasOption("pr")) {
                arg.setProduct(cmd.getOptionValue("pr"));
            }

			if (cmd.hasOption("uid")) {
                arg.setUserId(cmd.getOptionValue("uid"));
            }

            if (cmd.hasOption("pt")) {
                arg.setPassToken(cmd.getOptionValue("pt"));
            }

            if (cmd.hasOption("d")) {
                arg.setDeviceId(cmd.getOptionValue("d"));
            }
			
            UnlockManager.proccess();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp(TAG, options, true);
            System.exit(1);
        }
    }
}
