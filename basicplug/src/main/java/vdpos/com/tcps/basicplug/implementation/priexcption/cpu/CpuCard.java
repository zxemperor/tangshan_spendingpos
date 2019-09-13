package vdpos.com.tcps.basicplug.implementation.priexcption.cpu;

import com.landicorp.android.eptapi.exception.RequestException;

import vdpos.com.tcps.basicplug.basis.abstracts.CardBasis;

public class CpuCard extends CardBasis {
    /**
     * 设置M1卡类型函数
     *
     * @param cardType
     */
    @Override
    protected void setCardType(byte cardType) {
    }

    /**
     * 设置M1卡类型函数
     *
     * @param cardModel
     */
    @Override
    public void setCardModel(String cardModel) {
    }

    /**
     * 读取卡内容数据抽象功能。
     *
     * @param block
     * @return String
     */
    @Override
    protected byte[] readCardInfo(int block) throws RequestException {
        return new byte[0];
    }

    /**
     * 读取卡内容数据抽象功能。
     *
     * @return String
     */
    @Override
    protected byte[] readCardInfo() {
        return null;
    }

    /**
     * 写入卡内容数据抽象功能
     *
     * @param block
     * @param val
     * @return String
     */
    @Override
    protected boolean writeCardInfo(int block, byte[] val) {
        return false;
    }

    /**
     * 写入卡内容数据抽象功能
     *
     * @return String
     */
    @Override
    protected String writeCardInfo() {
        return null;
    }

    @Override
    public String getRecordConsume() {
        return null;
    }
}
