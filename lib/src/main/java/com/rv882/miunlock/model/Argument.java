package com.rv882.miunlock.model;

import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.HashMap;
import com.rv882.miunlock.unlock.XiaomiServiceEntry;
import com.rv882.miunlock.unlock.XiaomiProcedureException;
import com.rv882.miunlock.inet.CustomHttpException;
import com.rv882.miunlock.utils.Utils;

public class Argument {
    private boolean verbose = false;
    private String user = null, password = null, token = null, product = null, userId = null, deviceId = null, passToken = null;
	private String pcId = null, deviceName = null, boardVer = null, socId = null;
	private HashMap<String, XiaomiServiceEntry> serviceMap = new HashMap<>();
	
	private static Argument instance;

    public static synchronized Argument getInstance() {
        if (instance == null) {
            instance = new Argument();
        }
        return instance;
    }
	
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getProduct() {
		return product;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceId() {
        if (deviceId == null) {
            deviceId = ("wb_" + UUID.randomUUID().toString());
        }
        return deviceId;
    }

	public void setPassToken(String passToken) {
		this.passToken = passToken;
	}

	public String getPassToken() {
		return passToken;
	}
	
	public String getPcId() {
        if (pcId == null) {
            pcId = DigestUtils.md5Hex(getDeviceId());
        }
        return pcId;
    }
	
	public String getBoardVer() {
        if (boardVer == null) {
            boardVer = "";
        }
        return boardVer;
    }
	
	public String getSocId() {
        if (socId == null) {
            socId = "";
        }
        return socId;
    }
	
	public String getDeviceName() {
        if (deviceName == null) {
            String name = Utils.getDeviceCodenames().get(getProduct());
			if (name == null) {
				return "";
			} else {
				return name;
			}
        }
        return deviceName;
    }
	
	public String[] requireServiceKeyAndToken(String sid) throws XiaomiProcedureException, CustomHttpException {
        XiaomiServiceEntry entry = serviceMap.get(sid);
        if (entry == null) {
            entry = new XiaomiServiceEntry(sid, this);
            serviceMap.put(sid, entry);
        }
        return entry.requireSSandST();
    }

    public HashMap<String, String> requireServiceCookies(String sid) throws XiaomiProcedureException, CustomHttpException {
        XiaomiServiceEntry entry = serviceMap.get(sid);
        if (entry == null) {
            entry = new XiaomiServiceEntry(sid, this);
            entry.requireSSandST();
            serviceMap.put(sid, entry);
        }
        return entry.getCookies();
    }
}
