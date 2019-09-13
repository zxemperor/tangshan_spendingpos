package cn.eas.national.deviceapisample.device;

import com.landicorp.android.eptapi.card.Sim4428Driver;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.BytesBuffer;
import com.landicorp.android.eptapi.utils.IntegerBuffer;

import cn.eas.national.deviceapisample.data.SyncCardError;
import cn.eas.national.deviceapisample.util.ByteUtil;

public abstract class SIM4428CardReaderImpl extends BaseDevice {
    private Sim4428Driver driver;

    public SIM4428CardReaderImpl() {
        driver = new Sim4428Driver();
    }

    public void cardPower() {
        int ret = powerUp();
        if (ret == SyncCardError.SUCCESS) {
            displayInfo("card power success");
        } else {
            displayInfo("card power fail [ret = " + ret + "]");
        }
    }

    private int powerUp() {
        int ret =SyncCardError.FAIL;
        try {
            boolean exist = driver.exists();
            if (!exist) {
                displayInfo("card not exist");
                return ret;
            }
            BytesBuffer atr = new BytesBuffer();
            ret = driver.powerUp(Sim4428Driver.VOL_5, atr);
            if (ret != SyncCardError.SUCCESS) {
                displayInfo("power up fail, error = " + ret);
            } else {
                displayInfo("power up success, atr = " + ByteUtil.bytes2HexString(atr.getData()));
            }
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
        return ret;
    }

    public void cardHalt() {
        int ret = powerDown();
        halt();
        if (ret == SyncCardError.SUCCESS) {
            displayInfo("card halt success");
        } else {
            displayInfo("card halt fail, error = " + ret);
        }
    }

    private int powerDown() {
        if (driver != null) {
            try {
                return driver.powerDown();
            } catch (RequestException e) {
                e.printStackTrace();
                displayInfo("request exception has ocurred");
                return SyncCardError.FAIL;
            }
        }
        return SyncCardError.SUCCESS;
    }

    private void halt() {
        if (driver != null) {
            try {
                driver.halt();
            } catch (RequestException e) {
                e.printStackTrace();
                displayInfo("request exception has ocurred");
            }
        }
    }

    public byte[] read(byte[] pwd, int address, int len) {
        byte[] result = null;
        int ret = SyncCardError.FAIL;
        try {

            /*
            if (pwd != null && pwd.length > 0) {
                IntegerBuffer pwdTimes = new IntegerBuffer();
                ret = driver.verify(pwd, pwdTimes);
                if (ret != SyncCardError.SUCCESS) {
                    displayInfo("verify fail, error = " + ret);
                    return null;
                }
            }
            */
            BytesBuffer data = new BytesBuffer();
            ret = driver.read(address, len, data);
            if (ret != SyncCardError.SUCCESS) {
                displayInfo("read fail, error = " + ret);
                return null;
            }
            return data.getData();
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
        return result;
    }

    public int write(byte[] pwd, int address, byte[] data) {
        int ret = SyncCardError.FAIL;
        try {
            if (pwd != null && pwd.length > 0) {
                IntegerBuffer pwdTimes = new IntegerBuffer();
                ret = driver.verify(pwd, pwdTimes);
                if (ret != SyncCardError.SUCCESS) {
                    displayInfo("verify fail, error = " + ret);
                    return ret;
                }
            }
            return driver.write(Sim4428Driver.MODE_ENABLE, address, data);
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
        return ret;
    }

    public int changePassword(byte[] originalPwd, byte[] newPwd) {
        int ret = SyncCardError.FAIL;
        try {
            if (originalPwd != null && originalPwd.length > 0) {
                IntegerBuffer pwdTimes = new IntegerBuffer();
                ret = driver.verify(originalPwd, pwdTimes);
                if (ret != SyncCardError.SUCCESS) {
                    displayInfo("verify fail, error = " + ret);
                    return ret;
                }
                return driver.changeKey(newPwd);
            } else {
                return ret;
            }
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
        return ret;
    }
}
