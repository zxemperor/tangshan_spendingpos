package cn.eas.national.deviceapisample.device;

import android.app.Activity;
import android.content.Context;

import com.landicorp.android.scan.scanDecoder.ScanDecoder;

import cn.eas.national.deviceapisample.data.CameraScannerError;
import cn.eas.national.deviceapisample.data.Constants;

/**
 * 使用摄像头进行扫码。A8终端有前后置摄像可进行前后置不同的扫码，对于诸如A7/W280P/C10等终端，
 * 因只有后置，故只能使用后置扫码功能。另外可进行扫码的前提条件是终端已集成扫码库。
 */

public abstract class CameraScannerImpl extends BaseDevice {

    private ScanDecoder scanDecoder;
    private ScanDecoder.ResultCallback callback = new ScanDecoder.ResultCallback() {
        @Override
        public void onResult(String s) {
            displayInfo("scan success, code = " + s);
            runOnUI(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            });
        }

        @Override
        public void onCancel() {
            displayInfo("scan cancel");
            runOnUI(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            });
        }

        @Override
        public void onTimeout() {
            displayInfo("scan timeout");
            runOnUI(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            });
        }
    };

    public CameraScannerImpl(Context context) {
        scanDecoder = new ScanDecoder(context);
    }

    private int openCamera(int cameraId) {
        int id = ScanDecoder.CAMERA_ID_FRONT;
        if (cameraId == Constants.Scanner.CAMERA_BACK) {
            id = ScanDecoder.CAMERA_ID_BACK;
        }
        return scanDecoder.Create(id, callback);
    }

    public void startScan(Activity activity, int cameraId) {
        int ret = openCamera(cameraId);
        if (ret != CameraScannerError.SUCCESS) {
            displayInfo("open camera fail: " + getDescribe(ret));
        } else {
            ret = scanDecoder.startScanDecode(activity, null);
            if (ret != CameraScannerError.SUCCESS) {
                String errorDes = getDescribe(ret);
                displayInfo("start scan fail: " + errorDes);
            }
        }
    }

    public void close() {
        scanDecoder.Destroy();
    }

    private String getDescribe(int error) {
        switch (error) {
            case CameraScannerError.INIT_DECODER_FAIL:
                return "init decoder failed";
            case CameraScannerError.HAS_CREATED:
                return "it created before";
            case CameraScannerError.OPEN_CAMERA_FAIL:
                return "open camera failed";
            case CameraScannerError.LICENSE_FAIL:
                return "license certified failed";
            case CameraScannerError.NOT_FOUND_DECODRE:
                return "not found decoder";
            default:
                return "unknown error";
        }
    }

    private void runOnUI(Runnable runnable) {
        uiHandler.post(runnable);
    }
}
