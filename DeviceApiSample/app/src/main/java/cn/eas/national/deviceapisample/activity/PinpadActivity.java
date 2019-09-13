package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.data.Constants;
import cn.eas.national.deviceapisample.presenter.IPinpadPresenter;
import cn.eas.national.deviceapisample.presenter.impl.PinpadPresenterImpl;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * @author caizl
 */
public class PinpadActivity extends BaseDeviceActivity {
	private IPinpadPresenter presenter;
    private RadioGroup rgDeviceType;
	private RadioButton rbInner, rbExternal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.eas.national.deviceapisample.R.layout.pinpad);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		rbInner = (RadioButton) findViewById(R.id.rbInner);
		rbExternal = (RadioButton) findViewById(R.id.rbExternal);

		rgDeviceType = (RadioGroup) findViewById(R.id.rgDeviceType);
		rgDeviceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
				if (id == rbInner.getId()) {
					presenter = new PinpadPresenterImpl(PinpadActivity.this, Constants.Pinpad.DEVICE_INNER);
				} else {
					presenter = new PinpadPresenterImpl(PinpadActivity.this, Constants.Pinpad.DEVICE_EXTERNAL);
				}
			}
		});

		findViewById(cn.eas.national.deviceapisample.R.id.btnLoadKey).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ load key  ------------ ");
				presenter.loadKey();
			}
		});
		findViewById(cn.eas.national.deviceapisample.R.id.btnInputPin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ input pin ------------ ");
				presenter.startOnlinePinEntry("6214123443211234");
			}
		});
		findViewById(cn.eas.national.deviceapisample.R.id.btnCalcMac).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ calc mac ------------ ");
				presenter.calcMac(ByteUtil.hexString2Bytes("11111111111111111111111111111111"));
			}
		});
		findViewById(cn.eas.national.deviceapisample.R.id.btnEncryptTdData).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ encrypt td data ------------ ");
				presenter.encryptMagTrack(ByteUtil.hexString2Bytes("22222222222222222222222222222222"));
			}
		});
		findViewById(cn.eas.national.deviceapisample.R.id.btnEncryptData).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ encrypt data ------------ ");
				presenter.encryptData(ByteUtil.hexString2Bytes("00000000000000000000000000000000"));
			}
		});
		findViewById(cn.eas.national.deviceapisample.R.id.btnDecryptData).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ decrypt data ------------ ");
				presenter.decryptData(ByteUtil.hexString2Bytes("74D669C708972B1A74D669C708972B1A"));
			}
		});
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(cn.eas.national.deviceapisample.R.menu.activity_main, menu);
        return true;
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		bindDeviceService();
        if (rbInner.isChecked()) {
            presenter = new PinpadPresenterImpl(this, Constants.Pinpad.DEVICE_INNER);
        } else {
            presenter = new PinpadPresenterImpl(this, Constants.Pinpad.DEVICE_EXTERNAL);
        }
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
		String desc = getResources().getString(R.string.Pinpad_module_desc);
		return desc;
	}
}
