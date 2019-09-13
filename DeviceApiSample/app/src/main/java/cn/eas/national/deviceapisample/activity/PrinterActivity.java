package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.IPrinterPresenter;
import cn.eas.national.deviceapisample.presenter.impl.PrinterPresenterImpl;

/**
 * @author caizl
 */
public class PrinterActivity extends BaseDeviceActivity {
	private IPrinterPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printer);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.btnStartPrint).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ start print ------------ ");
				presenter.start();
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
		presenter = new PrinterPresenterImpl(this);
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
		String desc = getResources().getString(R.string.Printer_module_desc);
		return desc;
	}
}
