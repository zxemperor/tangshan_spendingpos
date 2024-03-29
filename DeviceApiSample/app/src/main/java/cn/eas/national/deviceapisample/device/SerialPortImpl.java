package cn.eas.national.deviceapisample.device;

import com.landicorp.android.eptapi.device.SerialPort;

/**
 * Created by Czl on 2017/7/27.
 */

public abstract class SerialPortImpl extends BaseDevice {
    private SerialPort serialPort;

    public SerialPortImpl(String deviceName) {
        this.serialPort = new SerialPort(deviceName);
    }

    public int init(int baud, int parity, int dataBits) {
        return serialPort.init(baud, parity, dataBits);
    }

    public int open() {
        return serialPort.open();
    }

    public int read(byte[] buffer, int timeout) {
        return serialPort.read(buffer, timeout);
    }

    public int write(byte[] data, int timeout) {
        return serialPort.write(data, timeout);
    }

    public int close() {
        return serialPort.close();
    }
}
