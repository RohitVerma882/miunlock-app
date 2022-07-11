package com.rv882.miunlock.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	public static HashMap<String, String> getDeviceCodenames() {
        HashMap<String, String> map = new HashMap<>();
        map.put("aquila", "AQUILA");
        map.put("begonia_p70", "BEGONIA-P70-GLOBAL");
        map.put("comet", "COMET-GLOBAL");
        map.put("equuleus", "EQUULEUS");
        map.put("gemini_android", "gemini_android");
        map.put("hammerhead", "Google Nexus 5");
        map.put("armani", "HM 1S");
        map.put("dior", "HM NOTE 1LTE");
        map.put("gucci", "HM NOTE 1S");
        map.put("laurus", "HM-LAURUS");
        map.put("cactus", "HM6A");
        map.put("leo_android", "leo_android");
        map.put("lte26007", "LTE26007-GLOBAL");
        map.put("cmi", "Mi 10 Pro");
        map.put("taurus", "MI 2A");
        map.put("aries", "MI 2S");
        map.put("pisces", "MI 3");
        map.put("ferrari", "Mi 4i");
        map.put("aqua", "MI 4S");
        map.put("cancro", "MI 4W");
        map.put("gemini", "MI 5");
        map.put("meri", "MI 5C");
        map.put("capricorn", "MI 5s");
        map.put("natrium", "MI 5s Plus");
        map.put("tiffany", "MI 5X");
        map.put("sagit", "Mi 6");
        map.put("wayne", "MI 6X");
        map.put("dipper", "MI 8");
        map.put("sirius", "MI 8 SE");
        map.put("cepheus", "MI 9");
        map.put("grus", "MI 9 SE");
        map.put("crux", "Mi 9Pro 5G");
        map.put("davinci", "MI 9T RU");
        map.put("tissot", "MI A1");
        map.put("laurel_sprout", "MI A3");
        map.put("pyxis", "MI CC 9");
        map.put("tucana", "MI CC 9 Pro");
        map.put("vela", "MI CC9 MEITU");
        map.put("hydrogen", "MI MAX");
        map.put("oxygen", "MI MAX 2");
        map.put("helium", "Mi Max Prime");
        map.put("nitrogen", "MI MAX3");
        map.put("chiron", "Mi MIX 2");
        map.put("perseus", "MI MIX3");
        map.put("virgo_android", "MI Note");
        map.put("scorpio", "Mi Note 2");
        map.put("jason", "Mi Note 3");
        map.put("virgo", "MI NOTE LTE");
        map.put("leo", "MI Note Pro");
        map.put("mocha", "MI PAD");
        map.put("latte", "MI PAD 2");
        map.put("cappu", "MI PAD 3");
        map.put("clover", "MI PAD 4");
        map.put("lotus", "MI Play");
        map.put("libra", "Mi-4c");
        map.put("mione_plus", "MI-ONE Plus");
        map.put("umi", "Mi10");
        map.put("ursa", "MI8Explorer");
        map.put("polaris", "MIMIX2S");
        map.put("lithium", "MIX");
        map.put("peony", "PEONY");
        map.put("platina", "PLATINA");
        map.put("beryllium", "POCO F1");
        map.put("atom", "Redmi 10X");
        map.put("merlin", "Redmi 10X 4G");
        map.put("bomb", "Redmi 10X Pro");
        map.put("ido_xhdpi", "Redmi 3");
        map.put("land", "Redmi 3S");
        map.put("prada", "Redmi 4");
        map.put("markw", "Redmi 4 Prime");
        map.put("rolex", "Redmi 4A");
        map.put("santoni_liuniu", "Redmi 4X");
        map.put("santoni", "Redmi 4X");
        map.put("rosy", "Redmi 5");
        map.put("vince", "Redmi 5 Plus");
        map.put("riva", "Redmi 5A");
        map.put("riva_fs", "Redmi 5A FS");
        map.put("cereus", "Redmi 6");
        map.put("sakura", "Redmi 6 Pro");
        map.put("onclite", "Redmi 7");
        map.put("pine", "Redmi 7A");
        map.put("olive", "Redmi 8");
        map.put("olivelite", "Redmi 8A");
        map.put("lancelot", "Redmi 9");
        map.put("davinciin", "Redmi K20  INDIA");
        map.put("raphael", "Redmi K20 Pro");
        map.put("raphaelin", "Redmi K20 Pro  india");
        map.put("raphaels", "Redmi K20 Pro Premium Edition");
        map.put("phoenix", "Redmi K30");
        map.put("picasso", "Redmi K30  5G");
        map.put("lmi", "Redmi K30 Pro");
        map.put("picasso_48m", "Redmi K30i 5G");
        map.put("lcsh92_wet_jb9", "Redmi Note");
        map.put("hermes", "Redmi Note 2");
        map.put("kenzo", "Redmi Note 3");
        map.put("nikel", "Redmi Note 4");
        map.put("mido", "Redmi Note 4X/Redmi Note 4");
        map.put("whyred", "Redmi Note 5");
        map.put("ugg", "Redmi Note 5A");
        map.put("ugglite", "Redmi Note 5A");
        map.put("violet", "Redmi Note 7 Pro");
        map.put("ginkgo", "Redmi Note 8");
        map.put("begonia", "Redmi Note 8 Pro");
        map.put("lavender", "Redmi Note7");
        map.put("omega", "Redmi Pro");
        map.put("ysl", "Redmi S2");
        map.put("HM2013023_sg", "Redmi1");
        map.put("wt86047", "WT86047");
        map.put("wt98007", "WT98007");
        map.put("vangogh", "XM-VANGOGH");
        map.put("virgo_lte_ct", "小米Note 全网通版");
        map.put("cancro_lte_ct", "小米手机4 电信4G版");
        map.put("lcsh92_wet_tdd", "红米Note TD版");
        map.put("hermes_pro", "红米Note2 Pro 高配版");
        map.put("hennessy", "红米Note3");
        map.put("HM2013022", "红米手机 TD版");
        map.put("HM2013023", "红米手机 WCDMA版");
        map.put("HM2014011", "红米手机1S TD 3G版");
        map.put("HM2014501", "红米手机1S TD 4G版");
        map.put("wt88047", "红米手机2 国际版");
        map.put("wt86047_pro", "红米手机2 移动增强版 / 红米手机2A 高配版");
        map.put("wt88047_pro", "红米手机2 高配 国际版");
        return map;
    }
	
	public static String snToString(Integer serialNumber) {
        return String.format("0x%08x", serialNumber);
    }
	
	public static String findJsonStart(String data) {
        char[] d = data.toCharArray();
        for (int i = 0; i < d.length; ++i) {
            if (d[i] == '{') {
                return data.substring(i);
            }
        }
        return null;
    }

    public static Charset interalCharset() {
        return StandardCharsets.UTF_8;
    }
}
