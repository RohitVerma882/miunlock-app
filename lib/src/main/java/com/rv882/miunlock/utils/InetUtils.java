package com.rv882.miunlock.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

import com.rv882.miunlock.inet.EasyHttp;
import com.rv882.miunlock.inet.EasyResponse;
import com.rv882.miunlock.inet.CustomHttpException;

public class InetUtils {
    
    public static String urlEncode(String data) {
        try {
            return URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getRedirectUrl(String url) throws CustomHttpException {
        return getRedirectUrl(url, null);
    }

    public static String getRedirectUrl(String url, String referer) throws CustomHttpException {
        EasyHttp request = new EasyHttp().url(url).setHeadOnly();
        if (referer != null) {
            request = request.referer(referer);
        }
        EasyResponse response = request.exec();
        List<String> list = response.getHeaders().get("location");
        if (list == null) {
            return null;
        }
        return list.get(0);
    }
}

