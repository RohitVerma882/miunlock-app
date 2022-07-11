package com.rv882.miunlock.unlock;

import java.util.Base64;
import java.util.HashMap;
import com.rv882.miunlock.inet.HttpQuery;
import com.rv882.miunlock.inet.CustomHttpException;
import com.rv882.miunlock.model.Argument;
import com.rv882.miunlock.inet.EasyResponse;
import com.rv882.miunlock.inet.EasyHttp;
import org.json.JSONObject;

public class UnlockRequest {
    private static final String SERVICE_NAME = "unlockApi";
	private static final String HOST = "https://in-unlock.update.intl.miui.com";
    private final HashMap<String, String> headers = new HashMap<>();
    private String path;
    private HttpQuery params = new HttpQuery();
    private String signHmac, signSha;
    private boolean encrypt;

    public UnlockRequest(String path) {
        this(path, true);
    }

    public UnlockRequest(String path, boolean encrypt) {
        this.path = path;
        this.encrypt = encrypt;
    }

    public String exec() throws XiaomiProcedureException, CustomHttpException {
        String[] keyToken = Argument.getInstance().requireServiceKeyAndToken(SERVICE_NAME);
        signHmac = XiaomiCrypto.cloudService_signHmac(XiaomiCrypto.UNLOCK_HMAC_KEY, "POST", path, params.sorted().toString());
        params.put("sign", signHmac);
        String key = keyToken[0];
        String serviceToken = keyToken[1];
        params = params.sorted();
        if (this.encrypt) {
            try {
                XiaomiCrypto.cloudService_encryptRequestParams(params, key);
            } catch (Exception e) {
                throw new XiaomiProcedureException("[UnlockRequest.exec] Cannot encrypt post params: " + e.getMessage());
            }
            signSha = XiaomiCrypto.cloudService_signSha1(key, "POST", path, params.toString());
            params.put("signature", signSha);
        }

        EasyResponse response = new EasyHttp().url(HOST + path).fields(params).headers(headers).userAgent("XiaomiPCSuite").cookies(Argument.getInstance().requireServiceCookies(SERVICE_NAME)).exec();
        if (!response.isAllRight()) {
            throw new XiaomiProcedureException("[UnlockRequest.exec] Invalid server respose: code: " + response.getCode() + ", lenght: " + response.getBody().length());
        }
        String body = response.getBody();
        if (encrypt) {
            try {
                body = XiaomiCrypto.cloudService_decrypt(body, key);
            } catch (Exception e) {
                throw new XiaomiProcedureException("[UnlockRequest.exec] Cannot decrypt response data: " + e.getMessage());
            }
            try {
                body = new String(Base64.getDecoder().decode(body));
            } catch (Throwable t) {
            }
        }
        return body;
    }

    public void addParam(String key, Object value) {
        params.put(key, value);
    }

    public void addNonce() throws XiaomiProcedureException, CustomHttpException {
        String json = UnlockCommonRequests.nonceV2();
        try {
            JSONObject obj = new JSONObject(json);
            int code = obj.getInt("code");
            if (code != 0) {
                throw new XiaomiProcedureException("[UnlockRequest.addNonce] Response code of nonce request is not zero: " + code);
            }
            String nonce = obj.getString("nonce");
            params.put("nonce", nonce);
        } catch (Exception e) {
            throw new XiaomiProcedureException("[UnlockRequest.addNonce] Exception while parsing nonce response: " + e.getMessage());
        }
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }
}

