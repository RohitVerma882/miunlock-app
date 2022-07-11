package com.rv882.miunlock.inet;

import com.rv882.miunlock.utils.Utils;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import com.rv882.miunlock.utils.InetUtils;

public class HttpQuery extends LinkedHashMap<String, Object> {
    @Override
    public Object put(String key, Object value) {
        if (value == null) {
            value = "null";
        }
        return super.put(key, value);
    }

    public HttpQuery sorted() {
        final HttpQuery res = new HttpQuery();

        this.entrySet().stream()
                .sorted(new Comparator<Map.Entry<String, Object>>() {
                    @Override
                    public int compare(Map.Entry<String, Object> p1, Map.Entry<String, Object> p2) {
                        return p1.getKey().compareTo(p1.getKey());
                    }
                })
                .forEach(new Consumer<Map.Entry<String, Object>>() {
                    public void accept(Map.Entry<String, Object> entry) {
                        res.put(entry.getKey(), entry.getValue());
                    }
                });

        return res;
    }

    public String toEncodedString(boolean encode) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : super.entrySet()) {
            builder.append(entry.getKey())
                    .append('=')
                    .append(
                            encode
                                    ? InetUtils.urlEncode(entry.getValue().toString())
                                    : entry.getValue().toString())
                    .append('&');
        }
        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    @Override
    public String toString() {
        return toEncodedString(false);
    }
}
