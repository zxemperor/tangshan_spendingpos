package cn.eas.national.deviceapisample.device;

import com.landicorp.android.eptapi.file.ParameterFile;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;

import static cn.eas.national.deviceapisample.activity.ParActivity.KEY_TYPE_STRING;

/**
 * par参数文件操作接口。
 */

public abstract class ParManagerImpl extends BaseDevice {
    private ParameterFile file;
    private String moduleName;
    private String fileName;

    public ParManagerImpl(IDeviceView view, String moduleName, String fileName) {
        file = new ParameterFile(moduleName, fileName);
        this.moduleName = moduleName;
        this.fileName = fileName;
    }

    public void isExist() {
        boolean exist = file.isExists();
        displayInfo("par file[moduleName = " + moduleName + ", fileName = " + fileName + "] " + (exist ? "existed" : "unexist"));
    }

    public void isFirstRun() {
        boolean isFirstRun = file.isFirstRun();
        displayInfo("par file[moduleName = " + moduleName + ", fileName = " + fileName + "] " + (isFirstRun ? "did read before" : "did not read before"));
    }

    public void readParam(int keyType, String key) {
        if (keyType == KEY_TYPE_STRING) {
            String value = file.getString(key, "");
            displayInfo("parameter[" + key + "] reads result：" + value);
        } else {
            boolean value = file.getBoolean(key, false);
            displayInfo("parameter[" + key + "] reads result：" + value);
        }
    }

    public void writeParam(int keyType, String key, Object value) {
        boolean result = false;
        if (keyType == KEY_TYPE_STRING) {
            result = file.setString(key, (String) value);
        } else {
            result = file.setBoolean(key, (boolean) value);
        }
        displayInfo("parameter[" + key + "] sets result：" + result);
    }
}
