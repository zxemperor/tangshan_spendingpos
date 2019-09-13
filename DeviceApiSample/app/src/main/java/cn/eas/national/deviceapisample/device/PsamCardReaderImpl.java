package cn.eas.national.deviceapisample.device;

import com.landicorp.android.eptapi.card.InsertCpuCardDriver;
import com.landicorp.android.eptapi.card.PSamCard;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.BytesBuffer;
import com.landicorp.android.eptapi.utils.IntegerBuffer;

import cn.eas.national.deviceapisample.data.ICCardError;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * Created by Czl on 2017/7/26.
 */

public abstract class PsamCardReaderImpl extends BaseDevice {
    private PSamCard reader;

    public PsamCardReaderImpl(int slot) {
        reader = PSamCard.getCard(slot);
    }

    private void init(int mode, int voltage) {
        reader.setPowerupMode(mode);
        reader.setPowerupVoltage(voltage);
    }

    public void cardPower() {
        init(InsertCpuCardDriver.MODE_BPS_576, InsertCpuCardDriver.VOL_5);
        powerUp();
    }

    public void cardHalt() {
        powerDown();
        halt();
    }

    private void powerUp() {
        try {
            BytesBuffer atr = new BytesBuffer();
            IntegerBuffer protocol = new IntegerBuffer();
            int ret = reader.powerup(atr, protocol);
            if (ret != ICCardError.SUCCESS) {
                displayInfo("power up fail, error = " + ret);
            } else {
                displayInfo("power up success, atr = " + ByteUtil.bytes2HexString(atr.getData())
                        + ", protocol = " + protocol.getData());
            }
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
    }

    private void powerDown() {
        try {
            reader.powerDown();
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
    }

    public void exchangeApdu(byte[] apdu) {
        BytesBuffer response = new BytesBuffer();
        try {
            int ret = reader.exchangeApdu(apdu, response);
            if (ret != ICCardError.SUCCESS) {
                displayInfo("exchange apdu fail, error = " + ret);
            } else {
                displayInfo("exchange apdu success, response = " + ByteUtil.bytes2HexString(response.getData()));
            }
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
    }

    public boolean exist() {
        try {
            return  reader.exists();
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
            return false;
        }
    }

    private void halt() {
        try {
            reader.halt();
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
    }
}
