package com.rv882.miunlock.unlock;

import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import com.rv882.miunlock.utils.StrUtils;
import com.rv882.miunlock.inet.CustomHttpException;
import com.rv882.miunlock.model.Argument;
import com.rv882.miunlock.utils.Utils;

import org.json.JSONObject;

public class UnlockCommonRequests {
    private static final HashMap<Integer, String> UNLOCK_CODE_MEANING = buildUnlockCodeMeaning();
    private static final String SID = "miui_unlocktool_client";
    private static final String CLIENT_VERSION = "5.5.224.55";
    private static final String NONCEV2 = "/api/v2/nonce";
    private static final String USERINFOV3 = "/api/v3/unlock/userinfo";
    private static final String DEVICECLEARV3 = "/api/v2/unlock/device/clear";
    private static final String AHAUNLOCKV3 = "/api/v3/ahaUnlock";
    private static final String AGREE = "/v1/unlock/agree";

    public static String getUnlockCodeMeaning(int code, JSONObject object) {
        String code_meaning = UNLOCK_CODE_MEANING.get(code);
        if (code_meaning == null) {
            return String.format("Unknown error: %1$d", code);
        }
        String toReturn;
        if (code == 20036) {
            int hours = -1;
            {
                try {
                    hours = object.getJSONObject("data").getInt("waitHour");
                } catch (Throwable t) {
                    
                }
                if (hours >= 0) {
                    int days = hours / 24;
                    int leftHours = hours % 24;
                    toReturn = String.format(code_meaning, days, leftHours);
                } else {
                    toReturn = ("You have to wait some days before you can unlock your device");
                }
            }
        } else {
            toReturn = code_meaning;
        }
        return toReturn;
    }

    private static HashMap<Integer, String> buildUnlockCodeMeaning() {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(10000, "10000:Request parameter error");
        map.put(10001, "10001:Signature verification failed");
        map.put(10002, "10002:The same IP request too often");
        map.put(10003, "10003:Internal server error");
        map.put(10004, "10004:Request has expired");
        map.put(10005, "10005:Invalid Nonce request");
        map.put(10006, "10006:Client version is too low");
        map.put(20030, "You have already unlocked a device recently\nYou should wait at least 30 days from the first unlock to unlock another device");
        map.put(20031, "This device is not bound to your account\nTurn on your device and bind your account to the device by going in MIUI's Settings > Developer options > Mi Unlock status");
        map.put(20032, "Failed to generate signature value required to unlock");
        map.put(20033, "User portrait scores too low or black");
        map.put(20034, "Current account cannot unlock this device");
        map.put(20035, "This tool is outdated, if you want to unlock your device then go to unlock.update.miui.com to download the latest version of MiUnlock");
        map.put(20036, "Your account has been bound to this device for not enough time\nYou have to wait %1$d days and %2$d hours before you can unlock this device");
        map.put(20037, "Unlock number has reached the upper limit");
        map.put(20041, "Your Xiaomi account isn't associated with a phone number\nGo to account.xiaomi.com and associate it with your phone number");
        return map;
    }

    public static String nonceV2() throws XiaomiProcedureException, CustomHttpException {
        UnlockRequest request = new UnlockRequest(NONCEV2);
        request.addParam("r", new String(StrUtils.randomWord(16).toLowerCase().getBytes(Utils.interalCharset()), Utils.interalCharset()));
        request.addParam("sid", SID);
        return request.exec();
    }

    public static String userInfo() throws XiaomiProcedureException, CustomHttpException {
        Argument arg = Argument.getInstance();
        UnlockRequest request = new UnlockRequest(USERINFOV3);
        HashMap<String, String> pp = new LinkedHashMap<>();
        pp.put("clientId", "1");
        pp.put("clientVersion", CLIENT_VERSION);
        pp.put("language", "en");
        pp.put("pcId", arg.getPcId());
        pp.put("region", "");
        pp.put("uid", arg.getUserId());
        String data = new JSONObject(pp).toString(3);
        data = Base64.getEncoder().encodeToString(data.getBytes(Utils.interalCharset()));
        request.addParam("data", data);
        request.addNonce();
        request.addParam("sid", SID);
        return request.exec();
    }

    public static String agreeRequest() throws XiaomiProcedureException, CustomHttpException {
        Argument arg = Argument.getInstance();
        UnlockRequest request = new UnlockRequest(AGREE, false);
        HashMap<String, String> pp = new LinkedHashMap<>();
        request.addParam("uid", arg.getUserId());
        request.addParam("type", "UnlockTool");
        request.addParam("sid", SID);
        return request.exec();
    }

    public static String deviceClear() throws XiaomiProcedureException, CustomHttpException {
        Argument arg = Argument.getInstance();
        UnlockRequest request = new UnlockRequest(DEVICECLEARV3);
        HashMap<String, String> pp = new LinkedHashMap<>();
        pp.put("clientId", "1");
        pp.put("clientVersion", CLIENT_VERSION);
        pp.put("language", "en");
        pp.put("pcId", arg.getPcId());
        pp.put("product", arg.getProduct());
        pp.put("region", "");
        String data = new JSONObject(pp).toString(3);
        data = Base64.getEncoder().encodeToString(data.getBytes(Utils.interalCharset()));
        request.addParam("appId", "1");
        request.addParam("data", data);
        request.addNonce();
        request.addParam("sid", SID);
        return request.exec();
    }

    public static String ahaUnlock() throws XiaomiProcedureException, CustomHttpException {
        Argument arg = Argument.getInstance();
        UnlockRequest request = new UnlockRequest(AHAUNLOCKV3);
        HashMap<String, String> p2 = new LinkedHashMap<>();
        p2.put("boardVersion", arg.getBoardVer());
        p2.put("deviceName", arg.getDeviceName());
        p2.put("product", arg.getProduct());
        p2.put("socId", arg.getSocId());
        HashMap<String, Object> pp = new LinkedHashMap<>();
        pp.put("clientId", "2");
        pp.put("clientVersion", CLIENT_VERSION);
        pp.put("deviceInfo", p2);
        pp.put("deviceToken", arg.getToken());
        pp.put("language", "en");
        pp.put("operate", "unlock");
        pp.put("pcId", arg.getPcId());
        pp.put("region", "");
        pp.put("uid", arg.getUserId());
		String data = StrUtils.map2json(pp, 3);
        data = Base64.getEncoder().encodeToString(data.getBytes(Utils.interalCharset()));
        request.addParam("appId", "1");
        request.addParam("data", data);
        request.addNonce();
        request.addParam("sid", SID);
        return request.exec();
    }
}

