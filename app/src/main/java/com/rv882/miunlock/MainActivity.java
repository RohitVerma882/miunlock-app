package com.rv882.miunlock;

import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.rv882.fastbootjava.FastbootDeviceContext;
import com.rv882.fastbootjava.FastbootDeviceManager;
import com.rv882.fastbootjava.FastbootResponse;
import com.rv882.fastbootjava.FastbootDeviceManagerListener;
import com.rv882.fastbootjava.FastbootCommand;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.DecoderException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity implements FastbootDeviceManagerListener {
	
	private TextView deviceTextview;
	private TextView responseTextview;
	private EditText cmdEditText;
	
	private Button rebootButton;
	private Button runButton;
	private Button writeButton;
	private Button varButton;
	
    private String deviceId;
	private FastbootDeviceContext deviceContext;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		deviceTextview = findViewById(R.id.deviceTextview);
		responseTextview = findViewById(R.id.responseTextview);
		cmdEditText = findViewById(R.id.cmdEditText);
		
		rebootButton = findViewById(R.id.rebootButton);
		rebootButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					reboot();
				}
			});
			
		runButton = findViewById(R.id.runButton);
		runButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					run();
				}
			});
			
		writeButton = findViewById(R.id.writeButton);
		writeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					write();
				}
			});
			
		varButton = findViewById(R.id.varButton);
		varButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View p1) {
					var();
				}

				
			});
		
		FastbootDeviceManager.Instance.addFastbootDeviceManagerListener(this);
    }
	
	private void var() {
		if (deviceContext != null) {
			FastbootResponse response = deviceContext.sendCommand(FastbootCommand.getVar("token"));
			responseTextview.setText(response.getData());
		}
	}
	
	private void run() {
		String data = cmdEditText.getText().toString();
		if (!data.isEmpty() && deviceContext != null) {
			FastbootResponse response = deviceContext.sendCommand(FastbootCommand.download(hexToBytes(data)));
			responseTextview.setText(response.getData() + "\n" + response.getStatus().toString());
		}
	}
	
	private void reboot() {
		if (deviceContext != null) {
			FastbootResponse response = deviceContext.sendCommand(FastbootCommand.oem("unlock"));
			responseTextview.setText(response.getData());
		}
	}
	
	private void write(){
		if (deviceContext != null) {
			FastbootResponse response = deviceContext.sendCommand(hexToBytes(null));
			responseTextview.setText(response.getData() + "\n" + response.getStatus().toString());
		}
	}
	
	private void closeDeviceContext() {
		if (deviceContext != null) {
			deviceContext.close();
			deviceContext = null;
		}
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
		FastbootDeviceManager.Instance.connectToDevice(deviceId);
	}

	@Override
	public void onFastbootDeviceDetached(String deviceId) {
		closeDeviceContext();
		
		deviceTextview.setText("No connected device");
	}

	@Override
	public void onFastbootDeviceConnected(String deviceId, FastbootDeviceContext deviceContext) {
		this.deviceContext = deviceContext;
		
		deviceTextview.setText("Connected device: " + deviceId);
	}

	@Override
	protected void onDestroy() {
		FastbootDeviceManager.Instance.removeFastbootDeviceManagerListener(this);
		closeDeviceContext();
		super.onDestroy();
	}
}
