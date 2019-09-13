package cn.eas.national.deviceapisample.device;

import com.landicorp.android.eptapi.device.IDCardReader;
import com.landicorp.android.eptapi.exception.RequestException;

import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * 该模块可用于已集成SAMV模块的终端。目前可适配W280PV2/W280PV3终端，其他终端暂不支持。
 */

public abstract class IDCardReaderImpl extends BaseDevice {
    private IDCardReader reader = IDCardReader.getInstance();

    public IDCardReaderImpl() {
    }

    public void searchCard() {
        try {
            reader.searchCard(new IDCardReader.OnSearchListener() {
                @Override
                public void onCardPass(byte[] bytes) {
                    displayInfo("card pass, data = " + ByteUtil.bytes2HexString(bytes));
                    try {
                        int ret = reader.selectCard();
                        if (ret != IDCardReader.ERROR_NONE) {
                            displayInfo("select card fail, error = " + ret);
                            return;
                        }
                        IDCardReader.IDCardInfo info = reader.readCardInfo();
                        if (info != null) {
                            int error = info.getErrorCode();
                            if (error == IDCardReader.ERROR_NONE) {
                                String name = info.getName();
                                String sex = info.getSex();
                                String birthDay = info.getBirthday();
                                String address = info.getAddress();
                                String expiredDate = info.getExpiredDate();
                                displayInfo("name = " + name);
                                displayInfo("sex = " + sex);
                                displayInfo("birthDay = " + birthDay);
                                displayInfo("address = " + address);
                                displayInfo("expiredDate = " + expiredDate);
                            } else {
                                displayInfo("search card fail, error = " + getDescription(error));
                            }
                        } else {
                            displayInfo("card info is null");
                        }
                    } catch (RequestException e) {
                        e.printStackTrace();
                        displayInfo("request exception has ocurred");
                    }
                }

                @Override
                public void onFail(int error) {
                    displayInfo("search card fail, error = " + getDescription(error));
                }

                @Override
                public void onCrash() {
                    onDeviceServiceCrash();
                }

                private String getDescription(int error) {
                    switch (error) {
                        case ERROR_FAILED:
                            return "other error(such as system error, etc.)[" + error + "]";
                        case ERROR_TIMEOUT:
                            return "operate timeout[" + error + "]";
                        case ERROR_TRANERR:
                            return "data transfered error[" + error + "]";
                        default:
                            return "unknown error[" + error + "]";
                    }
                }
            });
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
    }

    public void stopSearch() {
        try {
            reader.stopSearch();
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("request exception has ocurred");
        }
    }
}
