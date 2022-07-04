package com.rv882.miunlock;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class UnlockApp extends Application {
    private final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();

	@Override
	public void onCreate() {
		super.onCreate();

		// Crash handler
		setupCrashHandler();
	}

	private void setupCrashHandler() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread p1, Throwable p2) {
					try {
						File file = new File(getExternalFilesDir(null), "last_crash_logs.txt");
						if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
						
						String stackTraceString = Log.getStackTraceString(p2);

						FileWriter writer = new FileWriter(file);
						writer.append(stackTraceString);
						writer.flush();
						writer.close();
					} catch (IOException e) {
						// ignore
					}

					if (oldHandler != null) {
						oldHandler.uncaughtException(p1, p2);
					}
					android.os.Process.killProcess(android.os.Process.myPid());
					System.exit(10);
				}
			});
	}
}
