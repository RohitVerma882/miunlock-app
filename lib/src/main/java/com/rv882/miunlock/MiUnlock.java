package com.rv882.miunlock;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.rv882.miunlock.model.Argument;
import com.rv882.miunlock.unlock.UnlockManager;

public class MiUnlock {
    public static final String TAG = MiUnlock.class.getSimpleName();

    public static void main(String... args) {
        Options options = new Options();
        options.addRequiredOption("t", "token", true, "Device token id");
        options.addRequiredOption("p", "product", true, "Device product id");
		options.addRequiredOption("uid", "userId", true, "Mi account user id");
        options.addRequiredOption("pt", "passToken", true, "Mi account passToken");
		options.addOption("did", "deviceId", true, "Mi account deviceId");

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
            if (cmd.hasOption("t")) {
                arg.setToken(cmd.getOptionValue("t"));
            }

            if (cmd.hasOption("p")) {
                arg.setProduct(cmd.getOptionValue("p"));
            }

			if (cmd.hasOption("uid")) {
                arg.setUserId(cmd.getOptionValue("uid"));
            }

            if (cmd.hasOption("pt")) {
                arg.setPassToken(cmd.getOptionValue("pt"));
            }

            if (cmd.hasOption("did")) {
                arg.setDeviceId(cmd.getOptionValue("did"));
            }
			
            UnlockManager.proccess();
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp(TAG, options, true);
            System.exit(1);
        }
    }
}
