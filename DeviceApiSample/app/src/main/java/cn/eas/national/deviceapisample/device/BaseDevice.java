package cn.eas.national.deviceapisample.device;

import android.os.Handler;
import android.os.Looper;

public abstract class BaseDevice {
    protected Handler uiHandler = new Handler(Looper.getMainLooper());

	protected abstract void onDeviceServiceCrash();

	protected abstract void displayInfo(String info);

	protected  abstract  void toast(String msg);
}
