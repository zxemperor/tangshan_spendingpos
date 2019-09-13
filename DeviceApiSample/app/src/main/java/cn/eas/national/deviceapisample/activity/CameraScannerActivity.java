package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.data.Constants;
import cn.eas.national.deviceapisample.presenter.ICameraScannerPresenter;
import cn.eas.national.deviceapisample.presenter.impl.CameraScannerPresenterImpl;

/**
 * @author caizl
 */
public class CameraScannerActivity extends BaseDeviceActivity {
	private ICameraScannerPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_scanner);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.btnFrontScan).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ front scan  ------------ ");
				presenter.startScan(Constants.Scanner.CAMERA_FRONT);
			}
		});

		findViewById(R.id.btnBackScan).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ back scan  ------------ ");
				presenter.startScan(Constants.Scanner.CAMERA_BACK);
			}
		});
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		bindDeviceService();
        presenter = new CameraScannerPresenterImpl(this);
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
		String desc = getResources().getString(R.string.CameraScanner_module_desc);
		return desc;
	}
}
