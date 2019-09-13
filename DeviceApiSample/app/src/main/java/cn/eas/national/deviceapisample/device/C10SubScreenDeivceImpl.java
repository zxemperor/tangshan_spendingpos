package cn.eas.national.deviceapisample.device;

import android.content.Context;
import android.content.Intent;

import com.android.dualscreenmanager.aidl.Constants;
import com.landicorp.android.eptapi.dualscreen.DSMConnectListener;
import com.landicorp.android.eptapi.dualscreen.DualScreenHelper;
import com.landicorp.android.eptapi.dualscreen.DualScreenManager;

import java.util.ArrayList;
import java.util.List;

import cn.eas.national.deviceapisample.activity.SubscreenActivity;
import cn.eas.national.deviceapisample.data.DualScreenError;

/**
 * C10设备专用接口，用于与客屏的交互操作，只能在C10终端上使用。
 */

public abstract class C10SubScreenDeivceImpl extends BaseDevice {
    private static final String SUBSCREEN_APP_PGK_NAME = "cn.eas.national.subscreenapp";
    private static final String SUBSCREEN_APP_CLASS_NAME = "cn.eas.national.subscreenapp.MainActivity";
    private static final String CALLBACK_ID = "C10_DSM_HOST_TEST";
    private static final String SENDDATA_ID = "C10_DSM_CLIENT_TEST";

    private Context context;
    private DualScreenHelper helper = DualScreenHelper.getInstance();
    private DualScreenManager screenManager;
    private DSMConnectListener connectListener = new DSMConnectListener() {
        @Override
        public void onReady(DualScreenManager dualScreenManager) {
            screenManager = dualScreenManager;
            runOnUi(new Runnable() {
                @Override
                public void run() {
                    displayInfo("connect to subscreen success");
                    int ret = registDataListener();
                    if (ret != DualScreenError.SUCCESS) {
                        displayInfo("regist data listener fail[ret = " + ret + "]");
                    } else {
                        displayInfo("regist data listener success");
                    }
                }
            });
        }

        @Override
        public void onError() {
            displayInfo("connect to subscreen fail");
        }

        @Override
        public void onDisconnect() {
            displayInfo("disconnect to subscreen");
        }
    };
    private DualScreenManager.DataListener dataListener = new DualScreenManager.DataListener() {
        @Override
        public void onReceive(byte[] bytes) {
            displayInfo("receive message：" + new String(bytes));
        }
    };

    public C10SubScreenDeivceImpl(Context context) {
        this.context = context;
    }

    public void connect() {
        helper.init(context.getApplicationContext());
        helper.connect(connectListener);
    }

    public void disconnect() {
        int ret = unregistDataListener();
        if (ret != DualScreenError.SUCCESS) {
            displayInfo("unregist data listener fail[ret = " + ret + "]");
        } else {
            displayInfo("unregist data listener success");
        }
        helper.disconnect();
    }

    public void startActivityOnSubScreen() {
        if (screenManager == null) {
            displayInfo("didn't connected to subscreen");
            return;
        }
        Intent intent = new Intent(context, SubscreenActivity.class);
        int ret = screenManager.startActivityOnSubScreen(intent);
        if (ret != DualScreenError.SUCCESS) {
            displayInfo("start activity on subscreen fail[ret = " + ret + "]");
        } else {
            displayInfo("start activity on subscreen success");
        }
    }

    public void startAppOnSubScreen() {
            if (screenManager == null) {
                displayInfo("didn't connected to subscreen");
                return;
            }
            int ret = screenManager.startOnSubScreen(SUBSCREEN_APP_PGK_NAME,
                    SUBSCREEN_APP_CLASS_NAME, null);
            if (ret != DualScreenError.SUCCESS) {
                displayInfo("start app on subscreen fail[ret = " + ret + "]");
            } else {
                displayInfo("start app on subscreen success");
            }
    }

    public void setSubScreenApp() {
        if (screenManager == null) {
            displayInfo("didn't connected to subscreen");
            return;
        }
        int ret = screenManager.setSubScreenApp(SUBSCREEN_APP_PGK_NAME);
        if (ret != DualScreenError.SUCCESS) {
            displayInfo("set subscreen app fail[ret = " + ret + "]");
        } else {
            displayInfo("set subscreen app success");
        }
    }

    public void removeSubScreenApp() {
        if (screenManager == null) {
            displayInfo("didn't connected to subscreen");
            return;
        }
        int ret = screenManager.removeSubScreenApp(SUBSCREEN_APP_PGK_NAME);
        if (ret != DualScreenError.SUCCESS) {
            displayInfo("remove subscreen app fail[ret = " + ret + "]");
        } else {
            displayInfo("remove subscreen app success");
        }
    }

    public void getSubScreenInfo() {
        if (screenManager == null) {
            displayInfo("didn't connected to subscreen");
            return;
        }
        // 当前双屏模式（MODE_SINGLE_SYS：双屏机；MODE_NONE：非双屏机）
        int mode = screenManager.getCurrentMode();
        if (mode == Constants.MODE_SINGLE_SYS) {
            displayInfo("mode：MODE_SINGLE_SYS");
        } else if (mode == Constants.MODE_NONE) {
            displayInfo("mode：MODE_NONE");
        } else {
            displayInfo("mode：Illegal");
        }
        // 客屏是否可以点击（0：不可点击；1：可以点击）
        int touchable = screenManager.getSubScreenTouchable();
        if (touchable == cn.eas.national.deviceapisample.data.Constants.DualScreen.SCREEN_UNTOUCHABLE) {
            displayInfo("touchable：disable");
        } else if (touchable == cn.eas.national.deviceapisample.data.Constants.DualScreen.SCREEN_TOUCHABLE) {
            displayInfo("touchable：enable");
        } else {
            displayInfo("touchable：Illegal");
        }
        // 获取客屏按键（menu/home/back）是否启用（0：已禁用；1：已启用）
        int buttonEnable = screenManager.getSubScreenButtonEnable();
        if (buttonEnable == cn.eas.national.deviceapisample.data.Constants.DualScreen.BUTTON_DISABLE) {
            displayInfo("Subscreen Button Status：disable");
        } else if (buttonEnable == cn.eas.national.deviceapisample.data.Constants.DualScreen.BUTTON_ENABLE) {
            displayInfo("Subscreen Button Status：enable");
        } else {
            displayInfo("Subscreen Button Status：get failed[ret = " + buttonEnable + "]");
        }
        // 获取当前客屏是否有输入焦点（接收按键消息、使用输入法）（0：没有焦点，1：有焦点)
        int focus = screenManager.getSubScreenFocus();
        if (focus == cn.eas.national.deviceapisample.data.Constants.DualScreen.SCREEN_UNFOCUS) {
            displayInfo("Subscreen focus Status：unexist");
        } else if (focus == cn.eas.national.deviceapisample.data.Constants.DualScreen.SCREEN_FOCUS) {
            displayInfo("Subscreen focus Status：exist");
        } else {
            displayInfo("Subscreen focus Status：get failed[ret = " + focus + "]");
        }
        // 获取所有客屏应用
        List<String> subScreenApps = new ArrayList<>();
        int ret = screenManager.getSubScreenApps(subScreenApps);
        if (ret != DualScreenError.SUCCESS) {
            displayInfo("get subscreen app list failed[ret = " + ret + "]");
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("get subscreen app list successful：");
            if (subScreenApps.isEmpty()) {
                builder.append("none apps of subscreen \n");
            } else {
                builder.append("\n");
                for (String pkgName : subScreenApps) {
                    builder.append(pkgName + "\n");
                }
            }
            displayInfo(builder.toString());
        }
    }

    public int registDataListener() {
        return screenManager.registerDataListener(CALLBACK_ID, dataListener);
    }

    public int sendData() {
        return screenManager.sendData(SENDDATA_ID, "data of main screen sends".getBytes());
    }

    public int unregistDataListener() {
        return screenManager.unregisterDataListener(CALLBACK_ID);
    }

    private void runOnUi(Runnable runnable) {
        uiHandler.post(runnable);
    }
}
