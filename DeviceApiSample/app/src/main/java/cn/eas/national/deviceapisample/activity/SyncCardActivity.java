package cn.eas.national.deviceapisample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.eas.national.deviceapisample.R;
import cn.eas.national.deviceapisample.activity.base.BaseDeviceActivity;
import cn.eas.national.deviceapisample.adapter.DeviceModule;
import cn.eas.national.deviceapisample.adapter.DeviceModuleAdapter;
import cn.eas.national.deviceapisample.data.Constants;

/**
 * @author caizl
 */
public class SyncCardActivity extends BaseDeviceActivity {
    public static final String PARAMS_MODULE = "module";

    private List<DeviceModule> listCardModules = new ArrayList<DeviceModule>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.synccard);
        initView();
    }

    @Override
    protected void initView() {
        super.initView();
        initListView();
    }

    private void initListView(){
        ListView list = (ListView) findViewById(cn.eas.national.deviceapisample.R.id.listView1);
        initData();

        DeviceModuleAdapter adapter = new DeviceModuleAdapter(this, cn.eas.national.deviceapisample.R.layout.listview_item, listCardModules);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                itemClickListener(listCardModules.get(position).type);
            }
        });
    }

    private void initData() {
        listCardModules.clear();
        listCardModules.add(new DeviceModule(getResources().getString(R.string.AT1604Card), Constants.Device.MODULE_CARD_AT1604));
        listCardModules.add(new DeviceModule(getResources().getString(R.string.AT1608Card), Constants.Device.MODULE_CARD_AT1608));
        listCardModules.add(new DeviceModule(getResources().getString(R.string.AT24CxxCard), Constants.Device.MODULE_CARD_AT24Cxx));
        listCardModules.add(new DeviceModule(getResources().getString(R.string.SIM4428Card), Constants.Device.MODULE_CARD_SIM4428));
        listCardModules.add(new DeviceModule(getResources().getString(R.string.SIM4442Card), Constants.Device.MODULE_CARD_SIM4442));
    }

    private void itemClickListener(int index) {
        switch (index){
            case Constants.Device.MODULE_CARD_AT1604:
                startActivity(AT1604CardActivity.class);
                break;
            case Constants.Device.MODULE_CARD_AT1608:
                startActivity(AT1608CardActivity.class);
                break;
            case Constants.Device.MODULE_CARD_AT24Cxx:
                startActivity(AT24CxxCardActivity.class);
                break;
            case Constants.Device.MODULE_CARD_SIM4428:
                Intent sim4428Intent = new Intent(this, SIMCardActivity.class);
                sim4428Intent.putExtra(PARAMS_MODULE, Constants.Device.MODULE_CARD_SIM4428);
                startActivity(sim4428Intent);
                break;
            case Constants.Device.MODULE_CARD_SIM4442:
                Intent sim4442Intent = new Intent(this, SIMCardActivity.class);
                sim4442Intent.putExtra(PARAMS_MODULE, Constants.Device.MODULE_CARD_SIM4442);
                startActivity(sim4442Intent);
                break;
            default:
                break;
        }
    }

    public void startActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public String getModuleDescription() {
        String desc = getResources().getString(R.string.SyncCard_module_desc);
		return desc;
	}
}
