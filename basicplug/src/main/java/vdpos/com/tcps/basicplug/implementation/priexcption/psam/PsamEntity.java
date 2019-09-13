package vdpos.com.tcps.basicplug.implementation.priexcption.psam;


import vdpos.com.tcps.basicplug.util.psam.PsamBasis;
import vdpos.com.tcps.basicplug.util.error.status.PsamAPDUS;
import vdpos.com.tcps.basicplug.util.error.status.ExcuteStatus;

import com.landicorp.android.eptapi.card.InsertCpuCardDriver;
import com.landicorp.android.eptapi.card.PSamCard;
import com.landicorp.android.eptapi.utils.BytesUtil;
import com.landicorp.android.eptapi.utils.BytesBuffer;
import com.landicorp.android.eptapi.utils.IntegerBuffer;
import com.landicorp.android.eptapi.exception.RequestException;

/**
 * PSAM 实体类 用于生成 住建部 与 交通部PSAM实例
 *
 * @author zhaoxin 2019-07-19 16:26
 * @see vdpos.com.tcps.basicplug.util.psam.PsamBasis
 */
public class PsamEntity extends PsamBasis {

    /**
     * 存储当前对象实例的业务类型
     * 默认为交通部状态值{0 | 1}住建部
     */
    private int business;

    /**
     * 通过获得PSAM调用该函数获取当前实例的卡业务类型
     */
    public int getBusiness() {
        return business;
    }

    /**
     * 记录该实例的卡槽位置
     */
    private int CardSlot;

    /**
     * 通过当前Psam卡实例 获取其所在卡槽位置
     */
    public int getCardSlot() {
        return CardSlot;
    }

    /**
     * 通过PSAM卡实例 获取该实例的卡号
     */
    public String getPsamNo() {
        return psam_No;
    }

    public byte[] Psam_No() {
        return BytesUtil.hexString2Bytes(psam_No);
    }


    /**
     * 期望获取的业务类型实例 目前限定为 交通部与住建部双卡模式
     *
     * @param slotCard (int) 值导入上电卡槽ID,用于给指定卡槽上电。
     */
    public PsamEntity(int slotCard) {
        psc_Low = PSamCard.getCard(slotCard); //获取PSAM卡槽内PSAM卡对象
        CardSlot = slotCard;//设置卡槽
        psc_Low.setPowerupMode(InsertCpuCardDriver.MODE_ISO);/*设定通信波特率*/
        psc_Low.setPowerupVoltage(InsertCpuCardDriver.VOL_3);/*目前标准上电电压标准3V*/
        PowerUp();/*执行上电操作*/
    }//PsamEntity END

    /**
     * 判断PSAM卡是否脱离卡槽
     */
    public boolean exists() {
        try {
            return psc_Low.exists();
        } catch (RequestException e) {
            return false;
        }
    }

    /**
     * PSAM卡实例对象上电操作，并返回相应数据
     * <p>
     * <br>if(ret != ExcuteStatus.RF_SUCESS.getStatus()){</br>
     * <br>上电失败</br>
     * <br>}else{</br>
     * <br>上电成功</br>
     * <br>}</br>
     * </p>
     */
    private int PowerUp() {
        BytesBuffer atr = new BytesBuffer();//字节码-缓冲数组
        IntegerBuffer protocol = new IntegerBuffer();//协议编号
        try {
            int ret = psc_Low.powerup(atr, protocol);
            if (ret != ExcuteStatus.RF_SUCESS.getStatus()) {
                //  System.out.println("PSAM上电异常信息" +BytesUtil.bytes2HexString(atr.getData()));//在调试时修改成非注释模式
                return ExcuteStatus.ERROR_ATRERR.getStatus(); //上电失败
            } else {
                //   System.out.println("PSAM上电成功" +BytesUtil.bytes2HexString(atr.getData()));//在调试时修改成非注释模式
                return ExcuteStatus.RF_SUCESS.getStatus();//上电成功
            }
        } catch (RequestException e) {
            System.out.println("PSAM上电异常信息" + e.getMessage());//在调试时修改成非注释模式
            return ExcuteStatus.REQUESTERROR.getStatus();//上电异常
        }
    }//PowerUp END

    /**
     * PSAM执行相关指令 (后期这里一定要用 注解方式进行处理)
     * 交通部PSAM测试指令:00a40000028011
     * 住建部PSAM测试指令:00a40000021003
     */
    public int SelectBusiness() {
        BytesBuffer response = new BytesBuffer();//响应的字节码
        int ret;
        try {
            ret = psc_Low.exchangeApdu(PsamAPDUS.JTB_apdu, response); //执行获取PSAM卡号指令(交通部)
            byte[] getResponse = response.getData();
            if (null == getResponse || 0 == getResponse.length) {
                return ExcuteStatus.REQUESTERROR.getStatus();
            }
            if (PsamAPDUS.PsamError.APP_NO_FIND[0] == getResponse[0] &&
                    PsamAPDUS.PsamError.APP_NO_FIND[1] == getResponse[1]) {
                ret = psc_Low.exchangeApdu(PsamAPDUS.ZJB_apdu, response);//执行获取PSAM卡号指令(住建部)
                getResponse = response.getData();
                if (ExcuteStatus.RF_SUCESS.getStatus() == ret &&
                        PsamAPDUS.PsamError.PSAM_SUCESS[0] == getResponse[getResponse.length - 2] &&
                        PsamAPDUS.PsamError.PSAM_SUCESS[1] == getResponse[getResponse.length - 1]) { //判断执行后状态
                    this.business = ExcuteStatus.PSAM_LBUILD.getStatus();//住建部
                    System.out.println("执行成功住建部" + BytesUtil.bytes2HexString(getResponse));
                }
            }// if end 判断6A82
            else if (ExcuteStatus.RF_SUCESS.getStatus() == ret &&
                    PsamAPDUS.PsamError.PSAM_SUCESS[0] == getResponse[getResponse.length - 2] &&
                    PsamAPDUS.PsamError.PSAM_SUCESS[1] == getResponse[getResponse.length - 1]) {//判断执行后状态
                this.business = ExcuteStatus.PSAM_TRAFFIC.getStatus();//交通部

                System.out.println("执行成功交通部" + BytesUtil.bytes2HexString(getResponse));
            }
        } catch (RequestException e) {
            System.out.println("异常" + e.getMessage());
            return ExcuteStatus.REQUESTERROR.getStatus();
        }
        return ExcuteStatus.RF_SUCESS.getStatus();
    }//ExecutePsamApdu END

    /**
     * 获取PSAM卡号
     */
    public int ReadPsamCard() {
        BytesBuffer response = new BytesBuffer();//响应的字节码
        int ret;
        int resLen;
        try {
            ret = psc_Low.exchangeApdu(PsamAPDUS.PSAM_CARDNUM, response);
            byte[] resp_bytes = response.getData();
            if (null == resp_bytes || 0 == resp_bytes.length) {
                return ExcuteStatus.REQUESTERROR.getStatus();
            }
            resLen = resp_bytes.length;
            if (ExcuteStatus.RF_SUCESS.getStatus() == ret &&
                    PsamAPDUS.PsamError.PSAM_SUCESS[0] == resp_bytes[resLen - 2] &&
                    PsamAPDUS.PsamError.PSAM_SUCESS[1] == resp_bytes[resLen - 1]) {
                psam_No = BytesUtil.bytes2HexString(resp_bytes).substring(0, (resp_bytes.length - 2) * 2); //正确
                System.out.println("获取的PSAM卡号" + psam_No);
            } else {
                psam_No = BytesUtil.bytes2HexString(resp_bytes).substring(0, (resp_bytes.length - 2) * 2); //失败
                System.out.println("获取的PSAM卡号失败" + psam_No);
            }
        } catch (RequestException e) {
            System.out.println("读取卡号异常" + e.getMessage());
            return ExcuteStatus.REQUESTERROR.getStatus();
        }
        return ExcuteStatus.RF_SUCESS.getStatus();
    }
}
