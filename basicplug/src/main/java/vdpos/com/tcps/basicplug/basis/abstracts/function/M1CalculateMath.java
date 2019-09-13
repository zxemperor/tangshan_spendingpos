package vdpos.com.tcps.basicplug.basis.abstracts.function;

import com.landicorp.android.eptapi.utils.BytesUtil;

import java.text.ParseException;

import vdpos.com.tcps.basicplug.implementation.priexcption.mifare.MifareCard;
import vdpos.com.tcps.basicplug.util.math.CardMath;

public class M1CalculateMath extends CardMath {
    /**
     * M1卡计算钱包内钱数
     *
     * @param WalletNum 卡消费前的16进制钱数
     * @return double 格式钱 因为可能会有消费级别到分的可能 例如:含有则扣率的情况
     */
    @Override
    public double CalculateM1WalletInt(byte[] WalletNum) {
        return super.CalculateM1WalletInt(WalletNum);
    }

    /**
     * @param consumeMoney 消费的金额 单位:分
     * @param micard       消费卡实体
     * @return 消费的金额分
     */
    @Override
    public int CalculateM1ConsumeMoney(double consumeMoney, MifareCard micard) {
        return super.CalculateM1ConsumeMoney(consumeMoney, micard);
    }

    /**
     * M1卡计算月票次数
     *
     * @param MonthHexNum 卡内消费前的月票16进制数
     * @return int  卡消费前的钱数数据
     */
    @Override
    public int getHistoryMonthNum(byte[] MonthHexNum) {
        return super.getRecHistoryMoney(MonthHexNum);
    }


    /**
     * @param time
     * @return 消费的UTC时间
     * @throws ParseException
     */
    @Override
    public byte[] wRecordTime(byte[] time) throws ParseException {
        return super.wRecordTime(time);
    }

    @Override
    public int getRecHistoryMoney(byte[] MonthHexNum) {
        return super.getRecHistoryMoney(MonthHexNum);
    }

    /**
     * @param recArr 传入计算校验的数据
     * @return 返回校验串
     */
    public byte[] M1CalRecordRCR(byte[] recArr) {
        int judge = 0;
        for (int i = 0; i < recArr.length - 1; i++) {
            judge ^= (recArr[i] & 0xff);
        }
        recArr[recArr.length - 1] = (judge < 0 ? (byte) (judge & 0xff) : (byte) judge);
        return recArr;
    }

    /**
     * @param RecordBL 计算消费记录文件的消费前原额度
     * @return 消费单元分或次数
     */
    @Override
    public int CalculateM1RecordMoneyInt(byte[] RecordBL) {
        System.out.println("消费记录区的值" + BytesUtil.bytes2HexString(RecordBL));
        return super.CalculateM1RecordMoneyInt(RecordBL);
    }

    /**
     * 计算消费记录里的消费额度
     *
     * @param RecordBL 传入消费记录块值
     * @return 返回以分为单位的钱数或次数
     */
    @Override
    public int CalcMiRecConMoneyInt(byte[] RecordBL) {
        return super.CalcMiRecConMoneyInt(RecordBL);
    }
}
