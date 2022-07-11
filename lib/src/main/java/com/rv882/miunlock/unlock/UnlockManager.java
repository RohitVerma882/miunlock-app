package com.rv882.miunlock.unlock;

import java.util.HashMap;

import org.json.JSONObject;

import com.rv882.miunlock.inet.CustomHttpException;
import com.rv882.miunlock.model.Argument;
import com.rv882.miunlock.inet.EasyHttp;

public class UnlockManager {
    public static final String TAG = UnlockManager.class.getSimpleName();

    public static void proccess() {
        try {
			System.out.println("started...\n");
			String body = UnlockCommonRequests.ahaUnlock();
			System.out.println(body);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
    }
}
