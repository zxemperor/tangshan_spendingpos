package vdpos.com.tcps.basicplug.implementation.priexcption.mifare;

import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.BytesUtil;

import java.io.IOException;

import vdpos.com.tcps.basicplug.basis.abstracts.function.M1CalculateMath;
import vdpos.com.tcps.basicplug.implementation.fixed.ConsumeRecord;
import vdpos.com.tcps.basicplug.implementation.fixed.DeskeyOpertion;

public class MiRestore {
    /**
     * 计算当前应取的扇区值
     * 用于读合法块的值
     *
     * @param RecordPoint 公共区指向记录区指针
     * @param micard      M1卡实例对象
     * @param calcul      计算密钥KEY操作实例
     * @return int 要去执行的块值
     * @throws RequestException
     */
    protected int getRecordBlockToSector(int RecordPoint, MifareCard micard, CalculateCardKeyFun calcul) throws RequestException {
        int base_augend = 11;//基础被加数
        int increase = 0; //自增的值
        byte validatblock = 0x03;
        System.out.println((increase + RecordPoint) % 4 + "<RecordPoint>:::" + RecordPoint);
        if (0 == calcul.validationSecKey(validatblock, micard)) { //验证的索引区正确进入内部处理
            if (0 == RecordPoint) { //24 25 记录指针最大值为0 最小值为1
                RecordPoint = 1;
                //   increase = (0 == ((increase + RecordPoint) % 4) ? ((increase + RecordPoint) / 4) : increase); //根据算法推断消费记录区的块数
            } else if (6 < RecordPoint) { //索引大于3的时候
                increase = 2;
                validatblock = 0x05;
                if (0 != calcul.validationSecKey(validatblock, micard)) {
                    return 99;
                }
                System.out.println("验证的扇区" + validatblock);
            } else if (3 < RecordPoint && 6 >= RecordPoint) {//索引大于7的时候
                increase = 1;
                validatblock = 0x04;
                if (0 != calcul.validationSecKey(validatblock, micard)) {
                    return 99;
                }
                System.out.println("验证的扇区" + validatblock);
            }
        }
        System.out.println("返回的块数" + (base_augend + RecordPoint + increase));
        return (base_augend + RecordPoint + increase);
    }


    /**
     * 无灰记录恢复函数
     *
     * @return 恢复结果
     */
    public boolean NoRecordRestore(byte[] blockTwf, MifareCard micard, CalculateCardKeyFun calcul) throws RequestException {
        System.out.println("有不确定消费记录,无消费记录进入卡自身判断.");
        if (0x03 == blockTwf[3]) {
            //去处理月票 卡恢复操作
            return RecordRestoreMonth(blockTwf, micard, calcul);
        }
        //去处理钱包 卡恢复操作
        return RecResWallet(blockTwf, micard, calcul);
    }

    /**
     * 有灰记录处理
     *
     * @return 恢复的结果
     */
    public boolean RecordRestore(byte[] blockTwf, MifareCard micard, CalculateCardKeyFun calcul) throws IOException, ClassNotFoundException {
        //获取灰记录内的消费钱原额 与 消费金额
        ConsumeRecordMi consumeMi = (ConsumeRecordMi) ConsumeRecordMi.getRecord(DeskeyOpertion.context.getFilesDir().getPath());
        M1CalculateMath miCalculate = new M1CalculateMath();
        int recOldMoney = miCalculate.getRecHistoryMoney(consumeMi.getHistoryMoney());


        //    CardMath cmath=new CardMath();
        //  cmath. getHistoryMonthNum();
        //  int RecOldMoney = consumeMi.getOldPayMoney()<<24;//获取上一次的消费前原额
        consumeMi.getPayMoney();//获取上一次的消费金额
        //与24 25内容做比较
        //以24 25为准 如果有出入则以24 25为准
        //判断 是钱包还是月票
        //对钱包去操作调用 RecResWallet
        //对月票去操作调用 RecordRestoreMonth

        return true;
    }

    /**
     * 针对M1卡钱包无灰记录情况下的恢复
     *
     * @param blockTwf 公共信息区信息，用来判断数据是否需要恢复
     * @param micard   M1卡实体
     * @param calcul   计算用辅助工具泪
     * @return 恢复是否成
     * @throws RequestException 与下层通讯异常控制
     */
    public boolean RecResWallet(byte[] blockTwf, MifareCard micard, CalculateCardKeyFun calcul) throws RequestException {
        boolean excuteFlag = false;
        int covblock = 10; //被覆盖块指针
        int sourblock = 9; //覆盖块指针
        int RecordPoint = (0 >= blockTwf[0] ? blockTwf[0] : (blockTwf[0] & 0xff));//通过公共指针获取当前索引
        int recordBlock = getRecordBlockToSector(RecordPoint, micard, calcul);//获取记录块数
        if (recordBlock >= 99) {
            return false;
        }
        byte[] recordConsume = micard.readCardInfo(recordBlock);//根据记录指针获取的记录
        M1CalculateMath cardMath = new M1CalculateMath();//获取M1专属计算类
        int HistoryMoney = ((int) micard.getHisWall_money() * 100);//换算钱包钱为分单位
        int HistoryMoneyBk = ((int) micard.getHisWall_moneyBK() * 100);//换算钱包备份为分单位
        blockTwf[3] = 0x02;//修改为钱包消费完成
        blockTwf[6] = 0x00;//修改为钱包消费完成
        int ReordHmoney = cardMath.CalculateM1RecordMoneyInt(recordConsume);//消费前原额
        int consumeHmoney = cardMath.CalcMiRecConMoneyInt(recordConsume);//消费金额
        System.out.println("上次卡内余额" + ReordHmoney);
        System.out.println("上次消费金额" + consumeHmoney);
        if (ReordHmoney == HistoryMoney || (ReordHmoney - consumeHmoney) == HistoryMoney) { //与消费记录数据不相符
            cardMath.M1CalRecordRCR(blockTwf);//计算校验位
            if (0 == calcul.validationSecKey((byte) 0x02, micard) && true == CoverRecord(covblock,
                    sourblock, micard)) {
                excuteFlag = true;
            }
            if (0 == calcul.validationSecKey((byte) 0x06, micard) &&
                    true == micard.writeCardInfo(24, blockTwf) && micard.writeCardInfo(25, blockTwf)
            ) {// true == micard.writeCardInfo(25, blockTwf)
                excuteFlag = true;
                System.out.println("恢复后的24块值:" + BytesUtil.bytes2HexString(micard.readCardInfo(24)));
                System.out.println("恢复后的25块值:" + BytesUtil.bytes2HexString(micard.readCardInfo(25)));
            }
        } else if (HistoryMoneyBk > HistoryMoney) {  //如果与消费记录不匹配 并且正本小于副本的时候进行 副本覆盖正本操作
            excuteFlag = CoverRecord(sourblock, covblock, micard);
        }
        return excuteFlag;
    }


    /**
     * 有灰记录处理模式
     *
     * @return 恢复结果
     */
    public boolean RecordRestoreMonth(byte[] blockTwf, MifareCard micard, CalculateCardKeyFun calcul) throws RequestException {
        return true;
    }

    /**
     * @param covBlock  被覆盖块
     * @param sourBlock 源块
     * @return 返回覆盖状态
     */
    private boolean CoverRecord(int covBlock, int sourBlock, MifareCard micard) throws RequestException {
        if (0 == micard.getM1driver().restoreRAM(sourBlock) &&
                0 == micard.getM1driver().transferRAM(covBlock)) {
            return true;
        }
        return false;
    }

}
