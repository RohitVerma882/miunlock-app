package com.rv882.miunlock.unlock;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.rv882.miunlock.model.Argument;
import com.rv882.miunlock.inet.EasyHttp;
import com.rv882.miunlock.inet.EasyResponse;
import com.rv882.miunlock.inet.CustomHttpException;
import com.rv882.miunlock.utils.Utils;
import com.rv882.miunlock.utils.InetUtils;
import com.rv882.miunlock.utils.Hash;
import org.json.JSONObject;
import org.json.JSONException;


public class XiaomiServiceEntry {
    private static String URL_FIRST = "https://account.xiaomi.com/pass/serviceLogin?sid=%s&_json=true&passive=true&hidden=false";
    private Argument arg;
    private String id;
    private String serviceToken;
    private String ssecurity;
    private String psecurity;
    private String slh_key;
    private String ph_key;
    private String location;
    private String nonce;
    private String cUserId;
    private int code;

    public XiaomiServiceEntry(String id, Argument arg) {
        this.id = id;
        this.arg = arg;
    }

    public Argument getArgument() {
        return this.arg;
    }

    public String getServiceId() {
        return this.id;
    }

    private void httpGetSSecurity() throws XiaomiProcedureException, CustomHttpException {
        Argument arg = this.getArgument();
        String url = String.format(URL_FIRST, this.getServiceId());
        HashMap<String, String> cookies = new LinkedHashMap<>();
        cookies.put("passToken", arg.getPassToken());
        cookies.put("userId", arg.getUserId());
        cookies.put("deviceId", arg.getDeviceId());
        EasyHttp request = new EasyHttp().url(url).cookies(cookies);
        EasyResponse response;
        response = request.exec();
        String body = response.getBody();
		body = Utils.findJsonStart(body);
        if (body == null) {
            throw new XiaomiProcedureException("[getSSecurity] Failed to find SSecurity json");
        }
        try {
            JSONObject json = new JSONObject(body);
            this.ssecurity = json.getString("ssecurity");
            this.psecurity = json.getString("psecurity");
            this.cUserId = json.getString("cUserId");
            this.code = json.getInt("code");
            BigDecimal nonce = new BigDecimal(json.getInt("nonce"));
            this.nonce = nonce.toPlainString();
            this.location = json.getString("location");
        } catch (JSONException e) {
            throw new XiaomiProcedureException("[getSSecurity] Failed to parse SSecurity json: " + e.getMessage() + System.lineSeparator() + body.substring(0, 100));
        }
    }

    private void httpGetServiceToken() throws XiaomiProcedureException, CustomHttpException {
        String url = signedLocation();
        if (url == null) {
            throw new XiaomiProcedureException("[getServiceToken] Cannot sign location, maybe missing parameters or failed hash");
        }
        EasyResponse response;
        response = EasyHttp.get(url);
        HashMap<String, String> cookies = response.getCookies();
        serviceToken = cookies.get("serviceToken");
        if (serviceToken == null) {
            throw new XiaomiProcedureException("[getServiceToken] Missing serviceToken cookie");
        }
        slh_key = cookies.get(id + "_slh");
        ph_key = cookies.get(id + "_ph");
    }

    private void httpGetSSAndST() throws XiaomiProcedureException, CustomHttpException {
        httpGetSSecurity();
        httpGetServiceToken();
    }

    private String signedLocation() {
        if (this.location == null || this.nonce == null || this.ssecurity == null) {
            return null;
        }
        String sign = InetUtils.urlEncode(Hash.sha1Base64("nonce=" + nonce + "&" + ssecurity));
        if (sign == null) {
            return null;
        }
        return location + "&clientSign=" + sign;
    }

    public String getSSecurity() {
        return ssecurity;
    }

    public String getServiceToken() {
        return serviceToken;
    }

    public String[] getSSandST() {
        return new String[]{ssecurity, serviceToken};
    }

    public String[] requireSSandST() throws XiaomiProcedureException, CustomHttpException {
        if (ssecurity == null) {
            httpGetSSecurity();
        }
        if (serviceToken == null) {
            httpGetServiceToken();
        }
        if (serviceToken == null || ssecurity == null) {
            throw new XiaomiProcedureException(String.format("[requireSSandSt] Cannot fetch ssecurity (%s) or serviceToken (%s)", ssecurity, serviceToken));
        }
        return getSSandST();
    }

    public HashMap<String, String> getCookies() {
        HashMap<String, String> map = new LinkedHashMap<>();
        map.put("serviceToken", serviceToken);
        map.put("userId", arg.getUserId());
        map.put(id + "_slh", slh_key);
        map.put(id + "_ph", ph_key);
        return map;
    }
}

