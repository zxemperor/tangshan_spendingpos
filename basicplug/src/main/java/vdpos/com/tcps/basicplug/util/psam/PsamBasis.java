package vdpos.com.tcps.basicplug.util.psam;

import com.landicorp.android.eptapi.card.PSamCard;
import com.landicorp.android.eptapi.card.InsertCpuCardDriver;
import com.landicorp.android.eptapi.exception.RequestException;

public class PsamBasis {

    protected PSamCard psc_Low = null;
    /**
     * psam 卡终端号
     */
    protected String psam_No = null;

    /**
     * 密钥索引
     */
    protected String Secret_index = null;

    /**
     * 为PSAM卡进行上电操作 --必须操作
     */
    public void PsamOnPower() {
        PSamCard psc_Low = PSamCard.getCard(1);
        try {
            System.out.println("卡存在进行上电操作");
            psc_Low.setPowerupMode(InsertCpuCardDriver.MODE_DEFAULT);
            psc_Low.setPowerupVoltage(InsertCpuCardDriver.VOL_3);
            psc_Low.powerup(onPsamPowerUp);
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }//PsamOnPower end

    /**
     * 为PSAM卡进行下电操作 --- 必须操作
     *
     * @deprecated
     */
    public void PsamDownPower() {

    }//PsamDownPower


    PSamCard.OnExchangeListener onExchangeListener = new InsertCpuCardDriver.OnExchangeListener() {
        @Override
        public void onFail(int i) {
            System.out.println("PSAM 变更失败");
        }

        @Override
        public void onSuccess(byte[] bytes) {
            System.out.println("Psam变更成功");
        }

        @Override
        public void onCrash() {
            System.out.println("服务崩坏");
        }
    };

    /**
     * 针对PSAM卡进行上电
     */
    protected PSamCard.OnPowerupListener onPsamPowerUp = new InsertCpuCardDriver.OnPowerupListener() {
        @Override
        public void onPowerup(int i, byte[] bytes) {
            System.out.println("PSAM卡上电成功");
        }

        @Override
        public void onFail(int i) {
            System.out.println("PSAM卡上电失败" + i);
            switch (i) {
                case ERROR_ATRERR: //上电时读卡片回送 ATR 错误
                    System.out.println("上电ATR失败");
                    break;
                case ERROR_NOPOWER:
                    System.out.println("硬件错误");
                    break;
                case ERROR_ATRERR_S:
                    System.out.println("社保模式上电时卡片回送 ATR 错");
                    break;
                case ERROR_NOCARD:
                    System.out.println("缺卡(SAM 卡无此返回)");
                    break;
                case ERROR_FAILED:
                    System.out.println("其他错误(系统错误)");
                    break;
                case ERROR_ERRTYPE:
                    System.out.println("卡类型错误");
                    break;
                case ERROR_TIMEOUT:
                    System.out.println("与外置读卡器通信错误");
                    break;
                default:
                    System.out.println("未知错误");
                    break;
            }
        }

        @Override
        public void onCrash() {
            System.out.println("PSAM服务崩坏");
        }
    };

}
