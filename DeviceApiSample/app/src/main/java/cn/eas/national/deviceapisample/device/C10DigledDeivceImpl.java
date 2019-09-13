package cn.eas.national.deviceapisample.device;

import android.content.Context;

import com.landicorp.android.eptapi.device.Digled;

/**
 * C10设备专用接口，用于与客屏的交互操作，只能在C10终端上使用。
 */

public abstract class C10DigledDeivceImpl extends BaseDevice {
    private Context context;
    private Digled digled = Digled.getInstance();
    private int flashStatus = 0; // 当前闪烁状态，0：金额灯亮，余额灯灭；1：金额灯灭，余额灯亮
    private Runnable flashLightRun = new Runnable() {
        @Override
        public void run() {
            flash();
        }
    };

    public C10DigledDeivceImpl(Context context) {
        this.context = context;
    }

    public void getDigledInfo() {
        // 获取当前机器存在哪些特殊指示灯
        displayInfo("=======>");
        int[] types = digled.getLightType();
        if (types != null && types.length > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("Indicator Light Type：");
            for (int type : types) {
                // 1：金额指示灯；2：余额指示灯；其他：其他指示灯
                if (type == 1) {
                    builder.append("Amount Indicator Light, ");
                } else if (type == 2) {
                    builder.append("Balance Indicator Light , ");
                } else {
                    builder.append("Other Indicator Light , ");
                }
            }
            String msg = builder.toString();
            displayInfo(msg.substring(0, msg.length() - 2));
        } else {
            displayInfo("this device doesn't exist indicator light ");
        }

        // 获取当前指示灯个数
        displayInfo("=======>");
        int number = digled.getLightNumber();
        displayInfo("Indicator Light Num：" + number);
        if (number > 0) {
            // 获取当前指定特殊指示灯状态，0：指示灯熄灭；1：指示灯点亮；其他：获取状态失败
            int status = digled.getLightStatus(Digled.LIGHT_AMOUT);
            displayInfo("Amount Indicator Light Status：" + getLightStatusDesc(status));
            status = digled.getLightStatus(Digled.LIGHT_BALANCE);
            displayInfo("Balance Indicator Light Status：" + getLightStatusDesc(status));
        }

        // 获取当前机器的数码管行数
        displayInfo("=======>");
        number = digled.getLineNumber();
        displayInfo("Digled Line Num：" + number);
        if (number > 0) {
            // 获取当前机器对应行号的数码管最大个数
            for (int i = 1; i <= number; i++) {
                int max = digled.getLineMax(i);
                displayInfo("the max num of line" + i + " digled" + (max > 0 ? "is：" + max : "get failed"));
            }
        }
    }

    private String getLightStatusDesc(int status) {
        if (status == 0) {
            return "off";
        } else if (status == 1) {
            return "on";
        }
        return "get failed";
    }

    public void display() {
        int number = digled.getLineNumber();
        if (number > 0) {
            // 左对齐反向显示
            for (int i = 1; i <= number; i++) {
                boolean result = digled.dispaly(i, Digled.ALIGN_LEFT, "%0.2f", 1234.56f);
                displayInfo("display line" + i + " result：" + (result ? "successful" : "failure"));
            }
            // 右对齐反向
//            for (int i = 1; i <= number; i++) {
//                boolean result = digled.dispaly(i, Digled.ALIGN_RIGHT, "%0.2f", 0.01f);
//                displayInfo("display line" + i + " result：" + (result ? "successful" : "failure"));
//            }
            // 数码管段显示
//            boolean result = digled.displaySeg(1, Digled.ALIGN_LEFT, new int[] { 0x76, 0x79, 0x38, 0x38, 0x3F });
//            displayInfo("display \"HELLO\" text result：" + (result ? "successful" : "failure"));
        } else {
            displayInfo("the line num of digled is 0, can't display");
        }
    }

    public void clear() {
        int number = digled.getLineNumber();
        if (number > 0) {
            for (int i = 1; i <= number; i++) {
                digled.clearLine(i);
            }
        }
    }

    public void flashLight() {
        int number = digled.getLightNumber();
        if (number > 0) {
            flash();
        } else {
            displayInfo("None of Indicator Light");
        }
    }

    private void flash() {
        if (flashStatus == 0) {
            digled.setLightStatus(Digled.LIGHT_AMOUT, 1);
            digled.setLightStatus(Digled.LIGHT_BALANCE, 0);
            flashStatus = 1;
        } else {
            digled.setLightStatus(Digled.LIGHT_AMOUT, 0);
            digled.setLightStatus(Digled.LIGHT_BALANCE, 1);
            flashStatus = 0;
        }
        uiHandler.postDelayed(flashLightRun, 300);
    }

    public void stopFlash() {
        // 0：熄灭；1：点亮
        digled.setLightStatus(Digled.LIGHT_AMOUT, 0);
        digled.setLightStatus(Digled.LIGHT_AMOUT, 0);
        uiHandler.removeCallbacks(flashLightRun);
    }

}
