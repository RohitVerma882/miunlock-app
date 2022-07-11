package com.rv882.miunlock.unlock;

import com.rv882.miunlock.model.Argument;
import com.rv882.miunlock.inet.EasyHttp;
import java.util.HashMap;
import com.rv882.miunlock.inet.CustomHttpException;
import org.json.JSONObject;

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
