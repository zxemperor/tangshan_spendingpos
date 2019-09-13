package cn.eas.national.deviceapisample.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.presenter.IIDCardPresenter;
import cn.eas.national.deviceapisample.presenter.impl.IDCardPresenterImpl;

/**
 * @author caizl
 */
public class IDCardActivity extends BaseDeviceActivity {
	private IIDCardPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idcard);

		initView();
    }

	@Override
	protected void initView() {
		super.initView();
		findViewById(R.id.btnSearch).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				displayInfo(" ------------ start search  ------------ ");
				presenter.searchCard();
			}
		});

		findViewById(R.id.btnStopSearch).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				displayInfo(" ------------ stop search ------------ ");
				presenter.stopSearch();
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
		presenter = new IDCardPresenterImpl(this);
	}
	
	// Sometimes you need to release the right of using device before other application 'onStart'.
	@Override
	protected void onPause() {
		super.onPause();
		unbindDeviceService();
	}

	@Override
	public String getModuleDescription() {
		String desc = getResources().getString(R.string.IDCard_module_desc);
		return desc;
	}
}
