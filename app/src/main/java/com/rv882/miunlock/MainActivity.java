package com.rv882.miunlock;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

import java.nio.charset.StandardCharsets;

import androidx.appcompat.app.AppCompatActivity;

import com.rv882.fastbootjava.FastbootDeviceContext;
import com.rv882.fastbootjava.FastbootDeviceManager;
import com.rv882.fastbootjava.FastbootResponse;
import com.rv882.fastbootjava.FastbootDeviceManagerListener;
import com.rv882.fastbootjava.FastbootCommand;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.DecoderException;

public class MainActivity extends AppCompatActivity implements FastbootDeviceManagerListener {
	
	private TextView deviceTextview;
	private TextView responseTextview;
	private EditText dataEditText;
	
	private Button rebootButton;
	private Button unlockButton;
    private Button lockButton;
	private Button serialButton;
	private Button varButton;
	private Button loginButton;
	
    private String deviceId;
	private FastbootDeviceContext deviceContext;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        FastbootDeviceManager.Instance.addFastbootDeviceManagerListener(this);
		
		deviceTextview = findViewById(R.id.deviceTextview);
		responseTextview = findViewById(R.id.responseTextview);
		dataEditText = findViewById(R.id.dataEditText);
			
		unlockButton = findViewById(R.id.unlockButton);
		unlockButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					String hex_str = dataEditText.getText().toString().trim();
                    if (!hex_str.isEmpty() && deviceContext != null) {
                        byte[] data = hexToBytes(hex_str);
                        if (data != null) {
                            FastbootResponse response = deviceContext.sendCommand(FastbootCommand.download(data));
                            if (response.getStatus() == FastbootResponse.ResponseStatus.DATA) {
                                response = deviceContext.sendCommand(data);
                                if (response.getStatus() == FastbootResponse.ResponseStatus.OKAY) {
                                    response = deviceContext.sendCommand(FastbootCommand.oem("unlock"));
                                }
                            }
                            responseTextview.setText(responseToString(response));
                        }
                    }
				}
			});
            
        lockButton = findViewById(R.id.lockButton);
        lockButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View p1) {
                    String hex_str = dataEditText.getText().toString().trim();
                    if (!hex_str.isEmpty() && deviceContext != null) {
                        byte[] data = hexToBytes(hex_str);
                        if (data != null) {
                            FastbootResponse response = deviceContext.sendCommand(FastbootCommand.download(data));
                            if (response.getStatus() == FastbootResponse.ResponseStatus.DATA) {
                                response = deviceContext.sendCommand(data);
                                if (response.getStatus() == FastbootResponse.ResponseStatus.OKAY) {
                                    response = deviceContext.sendCommand(FastbootCommand.oem("lock"));
                                }
                            }
                            responseTextview.setText(responseToString(response));
                        }
                    }
                }
			});
            
        rebootButton = findViewById(R.id.rebootButton);
        rebootButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View p1) {
                    if (deviceContext != null) {
                        FastbootResponse response = deviceContext.sendCommand(FastbootCommand.reboot());
                        responseTextview.setText(responseToString(response));
                    }
                }
			});
			
		serialButton = findViewById(R.id.serialButton);
	    serialButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					if (deviceContext != null) {
                        FastbootResponse response = deviceContext.sendCommand(FastbootCommand.getVar("serialno"));
                        responseTextview.setText(responseToString(response));
                    }
				}
			});
			
		varButton = findViewById(R.id.varButton);
		varButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					if (deviceContext != null) {
                        FastbootResponse response = deviceContext.sendCommand(FastbootCommand.getVar("token"));
                        responseTextview.setText(responseToString(response));
                    }
				}
			});
            
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View p1) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
			});
    }
    
    private String responseToString(FastbootResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append("Status: ").append(response.getStatus().toString());
        sb.append('\n');
        sb.append("Data: ").append(response.getData());
        return sb.toString();
    }
	
	private byte[] hexToBytes(String hex) {
		try {
			return Hex.decodeHex(hex);
		} catch (DecoderException e) {
			return null;
		}
	}

	@Override
	public void onFastbootDeviceAttached(String deviceId) {
		if (deviceContext == null) {
            this.deviceId = deviceId;
            FastbootDeviceManager.Instance.connectToDevice(deviceId);
        }
	}

	@Override
	public void onFastbootDeviceDetached(String deviceId) {
		if (deviceId != null && deviceId.equals(this.deviceId)) {
            if (deviceContext != null) {
                deviceContext.close();
                deviceContext = null;
            }
            
            deviceTextview.setText("No connected Device");
		}
	}

	@Override
	public void onFastbootDeviceConnected(String deviceId, FastbootDeviceContext deviceContext) {
		this.deviceContext = deviceContext;
		
		deviceTextview.setText("Connected Device: " + deviceId);
	}

	@Override
	protected void onDestroy() {
		FastbootDeviceManager.Instance.removeFastbootDeviceManagerListener(this);
		if (deviceContext != null) {
            deviceContext.close();
            deviceContext = null;
        }
        super.onDestroy();
	}
}
