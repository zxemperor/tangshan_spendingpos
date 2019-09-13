package cn.eas.national.deviceapisample;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.eas.national.deviceapisample.activity.AlgorithmActivity;
import cn.eas.national.deviceapisample.activity.BeeperActivity;
import cn.eas.national.deviceapisample.activity.C10DigledDeviceActivity;
import cn.eas.national.deviceapisample.activity.C10SubscreenDeviceActivity;
import cn.eas.national.deviceapisample.activity.CameraScannerActivity;
import cn.eas.national.deviceapisample.activity.CashBoxActivity;
import cn.eas.national.deviceapisample.activity.ICCpuCardActivity;
import cn.eas.national.deviceapisample.activity.IDCardActivity;
import cn.eas.national.deviceapisample.activity.InnerScannerActivity;
import cn.eas.national.deviceapisample.activity.LedActivity;
import cn.eas.national.deviceapisample.activity.MagCardActivity;
import cn.eas.national.deviceapisample.activity.MifareCardActivity;
import cn.eas.national.deviceapisample.activity.ParActivity;
import cn.eas.national.deviceapisample.activity.PinpadActivity;
import cn.eas.national.deviceapisample.activity.PrinterActivity;
import cn.eas.national.deviceapisample.activity.PsamCardActivity;
import cn.eas.national.deviceapisample.activity.RFCpuCardActivity;
import cn.eas.national.deviceapisample.activity.ScannerActivity;
import cn.eas.national.deviceapisample.activity.SerialPortActivity;
import cn.eas.national.deviceapisample.activity.SignPanelActivity;
import cn.eas.national.deviceapisample.activity.SyncCardActivity;
import cn.eas.national.deviceapisample.activity.SystemDeviceActivity;
import cn.eas.national.deviceapisample.activity.base.BaseActivity;
import cn.eas.national.deviceapisample.adapter.DeviceModule;
import cn.eas.national.deviceapisample.adapter.DeviceModuleAdapter;
import cn.eas.national.deviceapisample.data.Constants;
import cn.eas.national.deviceapisample.view.DialogUtil;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE_PERMISSION = 0x01; // 权限请求码
    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.VIBRATE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.READ_PHONE_STATE,
    };

    private List<DeviceModule> listDevices = new ArrayList<DeviceModule>();
    private boolean hasRequestPermission = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cn.eas.national.deviceapisample.R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasRequestPermission) {
            hasRequestPermission = true;
            requestPermissions();
        }
    }

    @Override
    protected void initView() {
        super.initView();
        initListView();
    }

    private void initListView(){
        ListView list = (ListView) findViewById(cn.eas.national.deviceapisample.R.id.listView1);
        initData();

        DeviceModuleAdapter adapter = new DeviceModuleAdapter(this, cn.eas.national.deviceapisample.R.layout.listview_item, listDevices);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                itemClickListener(listDevices.get(position).type);
            }
        });
    }

    private void initData() {
        listDevices.clear();
        listDevices.add(new DeviceModule(getResources().getString(R.string.MagCard), Constants.Device.DEVICE_MODULE_MAG_CARD));
        listDevices.add(new DeviceModule(getResources().getString(R.string.ICCpuCard), Constants.Device.DEVICE_MODULE_IC_CPU_CARD));
        listDevices.add(new DeviceModule(getResources().getString(R.string.SyncCard), Constants.Device.DEVICE_MODULE_SYNC_CARD));
        listDevices.add(new DeviceModule(getResources().getString(R.string.PsamCard), Constants.Device.DEVICE_MODULE_PSAM_CARD));
        listDevices.add(new DeviceModule(getResources().getString(R.string.RfCpuCard), Constants.Device.DEVICE_MODULE_RF_CPU_CARD));
        listDevices.add(new DeviceModule(getResources().getString(R.string.MifareCard), Constants.Device.DEVICE_MODULE_RF_M1_CARD));
        listDevices.add(new DeviceModule(getResources().getString(R.string.IDCard), Constants.Device.DEVICE_MODULE_ID_CARD));
        listDevices.add(new DeviceModule(getResources().getString(R.string.Printer), Constants.Device.DEVICE_MODULE_PRINTER));
        listDevices.add(new DeviceModule(getResources().getString(R.string.Pinpad), Constants.Device.DEVICE_MODULE_PINPAD));
        listDevices.add(new DeviceModule(getResources().getString(R.string.SerialPort), Constants.Device.DEVICE_MODULE_SERIALPORT));
        listDevices.add(new DeviceModule(getResources().getString(R.string.CameraScanner), Constants.Device.DEVICE_MODULE_CAMERA_SCANNER));
        listDevices.add(new DeviceModule(getResources().getString(R.string.InnerScanner), Constants.Device.DEVICE_MODULE_INNERSCANNER));
        listDevices.add(new DeviceModule(getResources().getString(R.string.Scanner), Constants.Device.DEVICE_MODULE_SCANNER));
        listDevices.add(new DeviceModule(getResources().getString(R.string.Led), Constants.Device.DEVICE_MODULE_LED));
        listDevices.add(new DeviceModule(getResources().getString(R.string.Beeper), Constants.Device.DEVICE_MODULE_BEEPER));
        listDevices.add(new DeviceModule(getResources().getString(R.string.CashBox), Constants.Device.DEVICE_MODULE_CASHBOX));
        listDevices.add(new DeviceModule(getResources().getString(R.string.SignPanel), Constants.Device.DEVICE_MODULE_SIGNPANEL));
        listDevices.add(new DeviceModule(getResources().getString(R.string.C10SubscreenDevice), Constants.Device.DEVICE_MODULE_C10_SUBSCREEN_DEVICE));
        listDevices.add(new DeviceModule(getResources().getString(R.string.C10DigledDevice), Constants.Device.DEVICE_MODULE_C10_DIGLED_DEVICE));
        listDevices.add(new DeviceModule(getResources().getString(R.string.Algorithm), Constants.Device.DEVICE_MODULE_ALGORITHM));
        listDevices.add(new DeviceModule(getResources().getString(R.string.System), Constants.Device.DEVICE_MODULE_SYSTEM));
        listDevices.add(new DeviceModule(getResources().getString(R.string.Par), Constants.Device.DEVICE_MODULE_PAR));
    }

    private void itemClickListener(int index) {
        int support = Constants.ModelSupport.SUPPORT_MODEL_NONE;
        switch (index){
            case Constants.Device.DEVICE_MODULE_MAG_CARD:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(MagCardActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_IC_CPU_CARD:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(ICCpuCardActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_SYNC_CARD:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(SyncCardActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_PSAM_CARD:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(PsamCardActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_RF_CPU_CARD:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(RFCpuCardActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_RF_M1_CARD:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(MifareCardActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_ID_CARD:
                support = Constants.ModelSupport.SUPPORT_MODEL_W280PV2
                        | Constants.ModelSupport.SUPPORT_MODEL_W280PV3;
                if (canSupport(support)) {
                    startActivity(IDCardActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_PRINTER:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(PrinterActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_PINPAD:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(PinpadActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_SERIALPORT:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(SerialPortActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_CAMERA_SCANNER:
                support = Constants.ModelSupport.SUPPORT_MODEL_W280PV2
                        | Constants.ModelSupport.SUPPORT_MODEL_W280PV3
                        | Constants.ModelSupport.SUPPORT_MODEL_P950
                        | Constants.ModelSupport.SUPPORT_MODEL_P960
                        | Constants.ModelSupport.SUPPORT_MODEL_P990
                        | Constants.ModelSupport.SUPPORT_MODEL_P990V2
                        | Constants.ModelSupport.SUPPORT_MODEL_APOS_A8
                        | Constants.ModelSupport.SUPPORT_MODEL_AECR_C10;
                if (canSupport(support)) {
                    startActivity(CameraScannerActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_INNERSCANNER:
                support = Constants.ModelSupport.SUPPORT_MODEL_W280PV2
                        | Constants.ModelSupport.SUPPORT_MODEL_W280PV3
                        | Constants.ModelSupport.SUPPORT_MODEL_P950
                        | Constants.ModelSupport.SUPPORT_MODEL_P960
                        | Constants.ModelSupport.SUPPORT_MODEL_P990
                        | Constants.ModelSupport.SUPPORT_MODEL_P990V2;
                if (canSupport(support)) {
                    startActivity(InnerScannerActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_SCANNER:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(ScannerActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_LED:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(LedActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_BEEPER:
                startActivity(BeeperActivity.class);
                break;
            case Constants.Device.DEVICE_MODULE_CASHBOX:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_AECR_C10)) {
                    startActivity(CashBoxActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_MODEM:
//                startActivity(ModemActivity.class);
                break;
            case Constants.Device.DEVICE_MODULE_C10_SUBSCREEN_DEVICE:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_AECR_C10)) {
                    startActivity(C10SubscreenDeviceActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_C10_DIGLED_DEVICE:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_AECR_C10)) {
                    startActivity(C10DigledDeviceActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_ALGORITHM:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(AlgorithmActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_SYSTEM:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(SystemDeviceActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_PAR:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(ParActivity.class);
                }
                break;
            case Constants.Device.DEVICE_MODULE_SIGNPANEL:
                if (canSupport(Constants.ModelSupport.SUPPORT_MODEL_ALL)) {
                    startActivity(SignPanelActivity.class);
                }
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
    public String getModuleDescription() {
        String desc = getResources().getString(R.string.main_module_desc);
        return desc;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    final String tipContent = getResources().getString(R.string.permission_tip);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtil.showDialog(MainActivity.this, "TIP", tipContent,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            DialogUtil.hide();
                                            finish();
                                        }
                                    });
                        }
                    });
                    return;
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean canSupport(int support) {
        String model = getModel();
        int value = getModelValue(model);
        if ((support & value) == value) {
            return true;
        }
        toast(" this module is not supported for [" + model + "] device");
        return false;
    }

    private int getModelValue(String model) {
        if (model.equalsIgnoreCase(Constants.TerminalType.MODEL_W280P)) {
            return Constants.ModelSupport.SUPPORT_MODEL_W280P;
        } else if (model.equalsIgnoreCase(Constants.TerminalType.MODEL_W280PV2)) {
            return Constants.ModelSupport.SUPPORT_MODEL_W280PV2;
        } else if (model.equalsIgnoreCase(Constants.TerminalType.MODEL_W280PV3)) {
            return Constants.ModelSupport.SUPPORT_MODEL_W280PV3;
        } else if (model.equalsIgnoreCase(Constants.TerminalType.MODEL_P950)) {
            return Constants.ModelSupport.SUPPORT_MODEL_P950;
        } else if (model.equalsIgnoreCase(Constants.TerminalType.MODEL_P960)) {
            return Constants.ModelSupport.SUPPORT_MODEL_P960;
        } else if (model.equalsIgnoreCase(Constants.TerminalType.MODEL_P990)) {
            return Constants.ModelSupport.SUPPORT_MODEL_P990;
        } else if (model.equalsIgnoreCase(Constants.TerminalType.MODEL_P990V2)) {
            return Constants.ModelSupport.SUPPORT_MODEL_P990V2;
        } else if (model.equalsIgnoreCase(Constants.TerminalType.MODEL_APOS_A8)) {
            return Constants.ModelSupport.SUPPORT_MODEL_APOS_A8;
        } else if (model.equalsIgnoreCase(Constants.TerminalType.MODEL_AECR_C10)) {
            return Constants.ModelSupport.SUPPORT_MODEL_AECR_C10;
        } else {
            return Constants.ModelSupport.SUPPORT_MODEL_NONE;
        }
    }

    private void requestPermissions() {
        String model = getModel();
        if (model.equalsIgnoreCase(Constants.TerminalType.MODEL_AECR_C10)) {
            List<String> permissions = getNeedPermission();
            if (permissions != null && !permissions.isEmpty()) {
                int size = permissions.size();
                ActivityCompat.requestPermissions(this, (String[]) permissions.toArray(new String[size]), REQUEST_CODE_PERMISSION);
            }
        }
    }

    private List<String> getNeedPermission() {
        List<String> permissions = null;
        for (String perm : PERMISSIONS) {
            int result = ContextCompat.checkSelfPermission(this, perm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                if (permissions == null) {
                    permissions = new ArrayList<>();
                }
                permissions.add(perm);
            }
        }
        return permissions;
    }
}
