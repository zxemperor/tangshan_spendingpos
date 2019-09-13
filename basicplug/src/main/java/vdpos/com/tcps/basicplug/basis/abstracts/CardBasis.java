package vdpos.com.tcps.basicplug.basis.abstracts;

import com.landicorp.android.eptapi.exception.RequestException;

import vdpos.com.tcps.basicplug.interfaces.cardfunctions.RecordInterface;
import vdpos.com.tcps.basicplug.util.error.status.ExcuteStatus;

/**
 * 卡基类
 *
 * @author zhaoxin zx_emails@126.com
 * @version test version V0.1
 * @since jre1.8 jdk 1.8
 */
public abstract class CardBasis implements RecordInterface {
    /**
     * 卡的泛称类型 例如:Cpu/M1 S50 S50_pro S70 S70_pro
     * 统一划分为CPU = 0xC0  M1 = 0xC1
     * 默认为CPU卡 = 0xC0
     * 类型值关联类: 暂无
     */
    protected int cardType = ExcuteStatus.CPU_TYPE.getStatus();

    protected abstract void setCardType(byte cardType);

    public int getCardType() {
        return this.cardType;
    }

    /**
     * 卡自身的类型 例如 CPU /M1 S50 S70 S70_PRO S50_PRO等标注的卡类型
     */
    protected String cardModel;

    /**
     * 设置M1卡类型函数
     *
     * @param cardModel  CPU /M1 S50 S70 S70_PRO S50_PRO等标注的卡类型
     */
    protected abstract void setCardModel(String cardModel);

    public String getCardModel() {
        return cardModel;
    }

    /**
     * 广义基本信息-卡号
     */
    protected byte[] Card_No;
    /**
     * 广义基本信息-卡芯片号
     */
    protected byte[] Card_Chip;

    /**
     * 读取卡内容数据抽象功能。
     *
     * @return String
     */
    protected abstract byte[] readCardInfo(int block) throws RequestException;

    protected abstract byte[] readCardInfo();

    /**
     * 写入卡内容数据抽象功能
     *
     * @return String
     */
    protected abstract boolean writeCardInfo(int block, byte[] val);

    protected abstract String writeCardInfo( );
}
