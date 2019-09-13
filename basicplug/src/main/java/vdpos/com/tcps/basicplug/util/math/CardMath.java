package vdpos.com.tcps.basicplug.util.math;

import com.landicorp.android.eptapi.utils.BytesUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import vdpos.com.tcps.basicplug.implementation.priexcption.mifare.MifareCard;

/**
 *
 */
public class CardMath {

//************************************M1卡操作===BEGIN************************************/

    /**
     * M1卡计算钱包内钱数
     *
     * @param WalletNum 卡消费前的16进制钱数
     * @return double 格式钱 因为可能会有消费级别到分的可能 例如:含有则扣率的情况
     */
    protected double CalculateM1WalletInt(byte[] WalletNum) {
        int moneyInt = 0;
        //  System.out.println("计算的钱数" + BytesUtil.bytes2HexString(WalletNum));
        for (int begf = WalletNum.length - 1; begf >= 0; begf--) {
            int tmpInt = ((WalletNum[begf] << (begf * 8)));
            moneyInt += (((WalletNum[begf] << (begf * 8))) < 0 ? tmpInt & 0xff : tmpInt);
            //   System.out.println("计算序列" + begf + "值::" + moneyInt + "位移的值" + (WalletNum[begf] << (begf * 8)));
        }
        //   System.out.println("计算结果" + moneyInt);
        return (double) (moneyInt / 100);
    }

    /**
     * @param RecordBL 计算消费记录文件的消费前原额度
     * @return 消费单元分或次数
     */
    protected int CalculateM1RecordMoneyInt(byte[] RecordBL) {
        int recordHmoney = 0;
        for (int i = 4; i < 8; i++) {
            int tmp = (RecordBL[i] << (8 - i - 1) * 8);
            recordHmoney += (0 > (RecordBL[i] << (8 - i - 1) * 8) ? tmp & 0xff : tmp);
        }
        return recordHmoney;
    }

    /**
     * 计算消费记录里的消费额度
     *
     * @param RecordBL 传入消费记录块值
     * @return 返回以分为单位的钱数或次数
     */
    protected int CalcMiRecConMoneyInt(byte[] RecordBL) {
        int recordHmoney = 0;
        for (int i = 8; i < 11; i++) {
            int tmp = (RecordBL[i] << (11 - i - 1) * 8);
            recordHmoney += (0 > (RecordBL[i] << (11 - i - 1) * 8) ? tmp & 0xff : tmp);
        }
        return recordHmoney;
    }

    /**
     * @param consumeMoney 消费的金额 单位:分
     * @param micard       消费卡实体
     * @return 消费的金额分
     */
    protected int CalculateM1ConsumeMoney(double consumeMoney, MifareCard micard) {
        double HisMoney = micard.getHisWall_money();
        System.out.println(consumeMoney + "卡内历史余额度" + HisMoney);
        if (HisMoney >= consumeMoney) {
            return (int) (consumeMoney * 100); //计量单位最小为分，*100 变成整数分
        }
        return 0;//如果历史金额 小于消费金额返回0;
    }

    /**
     * M1卡计算月票次数
     *
     * @param MonthHexNum 卡内消费前的月票16进制数
     * @return int  卡消费前的钱数数据
     */
    protected int getHistoryMonthNum(byte[] MonthHexNum) {
        System.out.println("传入的月票次数" + BytesUtil.bytes2HexString(MonthHexNum));
        int monthInt = 0;
        for (int begf = MonthHexNum.length - 1; begf >= 0; begf--) {
            int tmpInt = ((MonthHexNum[begf] << (begf * 8)));
            monthInt += (((MonthHexNum[begf] << (begf * 8))) < 0 ? tmpInt & 0xff : tmpInt);
            System.out.println("计算序列" + begf + "值::" + monthInt + "位移的值" + (MonthHexNum[begf] << (begf * 8)));
        }
        System.out.println("月票当前剩余次数:" + monthInt);
        return monthInt;
    }

    /**
     * @return 消费的UTC时间
     * @throws ParseException
     */
    public byte[] wRecordTime(byte[] time) throws ParseException {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//格式化日期数据
        long nowtime = dateFormat.parse(year.substring(0, 2) + BytesUtil.bytes2HexString(time) + "00").getTime();//通过格式化后获取时间
        long millionSeconds = dateFormat.parse("19700101000000").getTime();//毫秒
        long NowTime = nowtime - millionSeconds;//获取当前时间
        String wRecordTimes = Long.toHexString(NowTime / 1000);//
        return BytesUtil.hexString2Bytes(wRecordTimes);
    }

    protected int getRecHistoryMoney(byte[] MonthHexNum) {
        System.out.println("传入的消费前原额" + BytesUtil.bytes2HexString(MonthHexNum));
        int oldmoney = 0;
        for (int begf = MonthHexNum.length - 1; begf >= 0; begf--) {
            int tmpInt = ((MonthHexNum[begf] << (begf * 8)));
            oldmoney += (((MonthHexNum[begf] << (begf * 8))) < 0 ? tmpInt & 0xff : tmpInt);
            System.out.println("计算序列" + begf + "值::" + oldmoney + "位移的值" + (MonthHexNum[begf] << (begf * 8)));
        }
        System.out.println("记录消费前原额:" + oldmoney);
        return oldmoney;
    }
//************************************M1卡操作===BEGIN************************************/
}
