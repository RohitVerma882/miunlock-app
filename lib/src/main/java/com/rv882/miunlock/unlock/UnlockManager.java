package com.rv882.miunlock.unlock;

import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONObject;

import com.rv882.miunlock.inet.CustomHttpException;
import com.rv882.miunlock.model.Argument;
import com.rv882.miunlock.inet.EasyHttp;

public class UnlockManager {
    public static final String TAG = UnlockManager.class.getSimpleName();

    public static void proccess() {
        try {
            System.out.println("started...\n");
            
			String body = UnlockCommonRequests.userInfo();
            JSONObject o = new JSONObject(body);
            boolean shouldApply = o.getBoolean("shouldApply");
            if (!shouldApply) {
                System.err.println(String.format("Xiaomi server says shouldApply false, status %d\n", o.getInt("applyStatus")));
                System.out.println("...\n");
            }
			
            body = UnlockCommonRequests.deviceClear();
            o = new JSONObject(body);
            System.out.println(String.format("Xiaomi server says: %sIt says that the unlock will %s wipe data.", o.getString("notice"), (o.getInt("cleanOrNot") == 0 ? "not " : "")));
            System.out.println("...\n");
            
            body = UnlockCommonRequests.ahaUnlock();
            o = new JSONObject(body);
            int code = o.getInt("code");
            if (code  == 0) {
                System.out.println(o.getString("encryptData"));
            } else {
                System.err.println(String.format("message: %s, code: %d", o.getString("descEN"), o.getInt("code")));
            }
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
    }
}
