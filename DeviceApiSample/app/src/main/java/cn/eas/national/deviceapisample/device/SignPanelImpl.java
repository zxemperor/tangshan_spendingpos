package cn.eas.national.deviceapisample.device;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.landicorp.android.eptapi.device.Printer;
import com.landicorp.android.eptapi.device.SignPanel;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.BytesBuffer;
import com.landicorp.android.eptapi.utils.ImageTransformer;
import com.landicorp.android.eptapi.utils.IntegerBuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import cn.eas.national.deviceapisample.util.ByteUtil;

import static android.content.ContentValues.TAG;

/**
 * 支持外接签名板（如S160键盘）且主控版本及签名板驱动模块达到要求的终端。
 */

public abstract class SignPanelImpl extends BaseDevice {

    private SignPanel signPanel;
    private Printer printer = Printer.getInstance();

    public SignPanelImpl() {
        this.signPanel = SignPanel.getInstance("USBH");
    }

    public void startSign() {
        boolean result = signPanel.openDevice();
        if (!result) {
            displayInfo("open sign panel failed");
            return;
        }
        SignPanel.OnSignListener onSignListener = new SignPanel.OnSignListener() {

            @Override
            public void onCrash() {
                displayInfo("sign panel servie crash");
            }

            @Override
            public void onSigned() {
                Log.w(TAG, "/// startSign | onSigned");
                final BytesBuffer signData = new BytesBuffer();
                IntegerBuffer seqNum = new IntegerBuffer();
                boolean result = signPanel.getSignData(signData, seqNum);
                if (result) {
                    displayInfo("get sign data successful, data：");
                    displayInfo(ByteUtil.bytes2HexString(signData.getData()));
                    // 打印签字图片
                    runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = getSignBitmap(signData);
                            if (bitmap != null) {
                                printBitmap(bitmap);
                            }
                        }
                    });
                } else {
                    displayInfo("get sign data failed");
                }
                exitSign();
            }

            @Override
            public void onCancel() {
                exitSign();
                displayInfo("sign cancel");
            }

            @Override
            public void onTimeout() {
                exitSign();
                displayInfo("sign timeout");
            }

            @Override
            public void onCmdResp(int cmd, int state) {
                exitSign();
                displayInfo("other sign cmd responds：cmd = " + cmd + ", state = " + state);
            }

            @Override
            public void onDisconnected() {
                exitSign();
                displayInfo("sign panel disconnected");
            }

            @Override
            public void onFail(int error) {
                exitSign();
                displayInfo("sign failed，error = " + error);
            }
        };
        try {
            // pecialCode（特征码）不能大于8字节
            signPanel.startSign("12345678", 60, onSignListener);
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
    }

    private Bitmap getSignBitmap(BytesBuffer signData) {
        byte[] data = decompressSignData(signData);
        if (data != null && data.length > 0) {
            displayInfo("decompress sign data result：");
            displayInfo(ByteUtil.bytes2HexString(data));
            Bitmap bitmap = getBitmap(data);
            if (bitmap == null) {
                displayInfo("transfer bitmap failed");
                return null;
            }
            return bitmap;
        } else {
            displayInfo("decompress sign data failed");
        }
        return null;
    }

    private byte[] decompressSignData(BytesBuffer signData) {
        if (signData.getData() == null || signData.getData().length == 0) {
            displayInfo("sign data is null");
            return null;
        }
        BytesBuffer buffer = new BytesBuffer();
        boolean result = SignPanel.jbigDecompression(signData.getData(), SignPanel.DataType.BMP, buffer);
        if (result) {
            return buffer.getData();
        } else {
            return null;
        }
    }

    private Bitmap getBitmap(byte[] data) {
        if (data == null || data.length == 0) {
            displayInfo("bitmap data is null");
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bitmap != null) {
            displayInfo("sign bitmap：width = " + bitmap.getWidth());
            displayInfo("sign bitmap：height = " + bitmap.getHeight());
            displayInfo("sign bitmap：size = " + bitmap.getByteCount());
        }
        return bitmap;
    }

    private void printBitmap(final Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        try {
            int status = printer.getStatus();
            if (status == Printer.ERROR_NONE) {
                Printer.Progress progress = new Printer.Progress() {
                    @Override
                    public void doPrint(Printer printer) throws Exception {
                        ByteArrayOutputStream outputStream = ImageTransformer.convert1BitBmp(bitmap);
                        InputStream stream = new ByteArrayInputStream(outputStream.toByteArray());
                        printer.printImage(0, stream);
                        stream.close();
                        outputStream.close();

                        printer.feedLine(5);
                        printer.autoCutPaper();
                    }

                    @Override
                    public void onFinish(int error) {
                        if (error == Printer.ERROR_NONE) {
                            displayInfo("print successful");
                        } else {
                            displayInfo("print failed，error = " + error);
                        }
                    }

                    @Override
                    public void onCrash() {
                        displayInfo("printer crashed");
                    }
                };
                progress.start();
            } else {
                displayInfo("the status of printer is abnormal，status = " + status);
            }
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
    }

    private void exitSign() {
        signPanel.endSign();
        signPanel.closeDevice();
    }

    private void runOnUIThread(Runnable runnable) {
        uiHandler.post(runnable);
    }
}
