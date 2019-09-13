package vdpos.com.tcps.basicplug.implementation.priexcption.mifare;

import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.BytesUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import vdpos.com.tcps.basicplug.basis.abstracts.function.M1CalculateMath;
import vdpos.com.tcps.basicplug.basis.extend.PsamOpertion;
import vdpos.com.tcps.basicplug.implementation.fixed.ConsumeRecord;
import vdpos.com.tcps.basicplug.implementation.fixed.DeskeyOpertion;
import vdpos.com.tcps.basicplug.implementation.priexcption.LogicException;
import vdpos.com.tcps.basicplug.util.error.status.CardPayFixed;
import vdpos.com.tcps.basicplug.util.math.CardMath;
import vdpos.com.tcps.basicplug.util.payutil.PayProcess;

public class PayMiProcess extends PayProcess {
    private MifareCard micard;

    public PayMiProcess(MifareCard inMicard) {
        this.micard = inMicard;
    }

    /**
     * 消费钱包 M1
     * 消费步骤
     * 1.验证扇区:2sector
     * 判断是否可消费 消费钱数是否溢出如果溢出不可消费
     * 2.卡恢复流程
     * 如果卡内第9块钱数少于10块，先将9块值覆盖到10块
     * 并恢复24 25卡消费记录为白名单序列再进行消费
     * 3.写消费记录
     * 用来防止灰记录产出后无记录做防拔处理
     * 4.消费记录黑名单设置(如果卡内消费记录与卡消费记录属性可匹配则进入灰记录处理)
     * 写卡内消费记录区 首先将卡打入黑名单序列
     * 5.对卡内余额度值改写
     * 6.如果流程完成原子性操作即回复消费记录区 并恢复为白名单序列
     * pw = pay wallet
     *
     * @param payMoney 消费的金额 例如消费150分 = 1.5元 151分= 1.51元
     * @author Xin Zhao 2019-08-28 13:52
     * vdpos.com.tcps.basicplug.util.payutil.PayProcess.RestorePayStatus()
     */
    @Override
    protected void payWallet(double payMoney) throws RequestException, LogicException, ParseException, IOException, ClassNotFoundException {
        M1CalculateMath cmath = new M1CalculateMath();
        int consumeMoney;
        //恢复流程
        if (true == RestorePayStatus()) {
            //写消费记录指针 与 消费记录文件 钱包
            if (0 == calcul.validationSecKey((byte) 0x02, micard)) {//消费钱包要验证第二扇区 TS = two sector
                consumeMoney = cmath.CalculateM1ConsumeMoney(payMoney, micard);
                if (0 == consumeMoney) { //计算后的钱数如果为0 那么就是卡内余额度绝对小于消费额度不可消费
                    System.out.println("pw1:卡内余额度不足,请确认消费额度。");
                    throw new LogicException("pw1:卡内余额度不足,请确认消费额度。");
                }
                System.out.println("开始消费");
                //写入消费记录与公共区域记录
                ConsumeRecordMi crecord = (ConsumeRecordMi) ConsumeRecord.getRecord(DeskeyOpertion.context.getFilesDir().getPath());
                crecord.swtCardInfo(micard, (byte) CardPayFixed.MicardWalletPay.getType());
                writeWalletRecordBlock(consumeMoney, crecord);
                //写消费上传记录
                crecord.saveRecord(DeskeyOpertion.context.getFilesDir().getPath());

                //写卡钱包
                System.out.println("写卡钱包");
                micard.DecrementWalletPub((int) payMoney);
                //恢复记录指针
                System.out.println("恢复记录");
                writeRecordWalletPblock(0, (byte) 0x01, (byte) 0x04);

                System.out.println("消费结束" + BytesUtil.bytes2HexString(crecord.getSendRecord()));
                System.out.println("---------------------------END------------------------------------");
                //消费完结
            } else {
                throw new LogicException("pw2:校验余额失败,请重刷。");
            }
        } else {
            throw new LogicException("pw3:卡纠错失败");
        }
    }

    /**
     * 卡状态恢复函数用于检测卡内消费完成度与灰记录处理
     *
     * @return 恢复卡的状态值
     * true 恢复成功 false恢复失败
     * 交易类型
     * 01	本地钱包消费		81	透支消费
     * 02	公交乘次消费		88	充值
     * 03	预留钱包消费		8A	退卡
     * 04	非计次非钱包消费	8B	圈存到公交月票
     * 51	异地钱包消费		99	锁卡交易
     */
    @Override
    protected boolean RestorePayStatus() throws RequestException, NullPointerException, IOException {
        boolean excuteFlag = false;
        try {
            ConsumeRecordMi crecord = (ConsumeRecordMi) ConsumeRecord.getRecord( DeskeyOpertion.context.getFilesDir().getPath() );//获取M1专属消费记录实例
            byte[] sendrecord = crecord.getSendRecord();//将缓存的最后一条记录加载出来
            if (0 == calcul.validationSecKey((byte) 0x06, micard)) {//验证第6扇区 公共信息区
                byte[] blockTwf = this.micard.readCardInfo(24);//获取第24块值 block twenty four
                byte[] blockTwfi = this.micard.readCardInfo(25);//获取第24块值 block twenty five
                if (0x04 == (blockTwf[3] + blockTwfi[3]) || 0x08 == (blockTwf[3] + blockTwfi[3]) || 0x00 == blockTwf[3]) {//24 25为全完成状态或24 25为全新未消费状态
                    excuteFlag = true;
                } else if ((blockTwf[3] != blockTwfi[3])) {//当第24块的为完成状态 并将完成状态覆盖至未完成状态
                    excuteFlag = (blockTwf[3] > blockTwfi[4] ? micard.writeCardInfo(25, blockTwf) : micard.writeCardInfo(24, blockTwfi));
                } else if (blockTwf[3] == blockTwfi[3]) {//钱包为双未完成状态
                    MiRestore miRestore = new MiRestore();//恢复卡状态类实体化
                    String PayCardNo = micard.getCardNoStr();//卡自身卡号
                    String RecCardNo = crecord.getCardNoStr();//消费记录内卡号
                    if (sendrecord[64] == 0x00 || !PayCardNo.equals(RecCardNo)) {//　｜｜没有查询到钱包消费灰记录,做无卡恢复钱包&月票
                        System.out.println("无消费记录");
                        excuteFlag = miRestore.NoRecordRestore(blockTwf, micard, calcul); //此为无灰记录操作
                    } else if (PayCardNo.equals(crecord.getCardNoStr()) && CardPayFixed.RecWalletGray.getType() == crecord.getPayType()[0]) {//查询到有记录消费做有卡消费处理
                        //根据灰色记录处理消费记录
                        System.out.println("有灰色消费记录" + BytesUtil.bytes2HexString(crecord.getPayType()));
                        miRestore.RecordRestore(blockTwf, micard, calcul);//此为有灰记录操作
                        excuteFlag = true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("回复记录 第一次读取时异常" + e.getMessage());
            ConsumeRecord crecord = new ConsumeRecordMi();
            crecord.saveRecord(DeskeyOpertion.context.getFilesDir().getPath());
            excuteFlag = this.RestorePayStatus();
        } catch (ClassNotFoundException e) {
            //  System.out.println("回复记录 第一次读取时异常" + e.getMessage());
            excuteFlag = false;
        }
        return excuteFlag;
    }

    /**
     * 写消费记录区信息
     *
     * @param money 消费钱数为整型
     * @return 返回是否写卡成功
     * @throws RequestException 操作写卡动作异常
     * @throws ParseException   转换格式异常
     */
    private boolean writeWalletRecordBlock(int money, ConsumeRecordMi crecord) throws RequestException, ParseException, NullPointerException {
        if (0 == calcul.validationSecKey((byte) 0x06, micard)) {
            M1CalculateMath cmath = new M1CalculateMath();
            byte[] blockTwF = micard.readCardInfo(24);
            int RecordPoint = (0 >= blockTwF[0] ? blockTwF[0] : (blockTwF[0] & 0xff));//获取块索引值
            int writeBlockold = new MiRestore().getRecordBlockToSector(RecordPoint - 1, micard, calcul);
            byte[] oldrecord = micard.readCardInfo(writeBlockold);//获取上一次的旧记录
            crecord.setOldRecord(oldrecord);//返回交易记录
            System.out.println("卡的旧记录" + BytesUtil.bytes2HexString(oldrecord));
            int writeBlock = new MiRestore().getRecordBlockToSector(RecordPoint, micard, calcul);
            System.out.println("UTC:" + wRecordTime());//消费记录时间
            int historyMoney = (int) micard.getHisWall_money() * 100;//历史金额
            byte[] writeRecord = new byte[16];
            byte[] payTime = BytesUtil.hexString2Bytes(wRecordTime());
            crecord.setPayTime(payTime); // 消费记录消费时间
            System.arraycopy(payTime, 0, writeRecord, 0, 4);
            writeRecord[4] = (byte) (historyMoney >> 24); //卡内历史钱数
            writeRecord[5] = (byte) (historyMoney >> 16); //卡内历史钱数
            writeRecord[6] = (byte) (historyMoney >> 8); //卡内历史钱数
            writeRecord[7] = (byte) (historyMoney);//历史消费金额
            writeRecord[8] = (byte) (money >> 16);//当前消费金额
            writeRecord[9] = (byte) (money >> 8);//当前消费金额
            writeRecord[10] = (byte) (money);//当前消费金额
            writeRecord[11] = 0x01;//消费类型 0x01 钱包
            crecord.setPayMoney(writeRecord);//卡交易金额
            byte[] payConut = new byte[2];
            System.arraycopy(blockTwF, 1, payConut, 0, 2);
            /**消费记录*/
            crecord.setRecordType(new byte[]{0x01});//记录类型
            crecord.setPayCount(payConut);//交易前卡交易次数
            crecord.setHistoryMoney(micard.getHisWall_moneyb());//交易前原额（分）
            crecord.setWalletCount(micard.getWallRecCount());//钱包充值计数器
            crecord.setRechargeMoney(micard.getCurrentRechargeMoney());//充值金额（分）
            crecord.setRechargeDate(new CardMath().wRecordTime(micard.getRechargeDate()));//充值日期
            /**消费记录*/
            System.arraycopy(BytesUtil.hexString2Bytes(PsamOpertion.getInstance().getPsamCardNo(0xFE)),
                    1, writeRecord, 12, 4);//消费终端编号 有可能会变成POS机编号
            //M1CalRecordRCR(byte[] recArr)
            crecord.setSendRecord(cmath.M1CalRecordRCR(crecord.getSendRecord()));
            System.out.println("消费记录区内容" + writeBlock + "块,值" + BytesUtil.bytes2HexString(writeRecord));
            if (true == micard.writeCardInfo(writeBlock, writeRecord)) {
                System.out.println(BytesUtil.bytes2HexString(micard.readCardInfo(writeBlock)) + "::写卡公共区开始");
                return writeRecordWalletPblock(1, (byte) 0x01, (byte) 0x04);//写入公共区信息 记录为黑记录
            }
        }
        return false;
    }

    /**
     * 写入公共信息区消费记录指针
     *
     * @param addPoint   公共区记录块指针的增长值
     * @param payproFlag 交易进程标识
     * @param blOrWh     黑名单 白名单
     * @return 写入是否成功
     * @throws RequestException
     */
    private boolean writeRecordWalletPblock(int addPoint, byte payproFlag, byte blOrWh) throws RequestException {
        if (0 == calcul.validationSecKey((byte) 0x06, micard)) {
            M1CalculateMath cmath = new M1CalculateMath();
            byte[] blockTwF = micard.readCardInfo(24);
            System.out.println("历史版本记录" + BytesUtil.bytes2HexString(blockTwF));
            int RecordPoint = (0 >= blockTwF[0] ? blockTwF[0] : (blockTwF[0] & 0xff));
            System.out.println("换算前块值" + RecordPoint);
            blockTwF[0] = (byte) (RecordPoint + addPoint == 10 ? 1 : RecordPoint + addPoint);//判断公共信息区索引是否超出了边界
            int walletCount = (blockTwF[1] >> 8) + (blockTwF[2]) + 1;
            blockTwF[1] = (byte) (walletCount << 8);//钱包计数器
            blockTwF[2] = (byte) walletCount;//钱包计数器
            blockTwF[3] = payproFlag; //消费标识
            blockTwF[6] = blOrWh;
            blockTwF[7] = 0x01;
            System.out.println("准备写入的记录" + BytesUtil.bytes2HexString(cmath.M1CalRecordRCR(blockTwF)));
            if (true == micard.writeCardInfo(24, cmath.M1CalRecordRCR(blockTwF)) &&
                    true == micard.writeCardInfo(25, cmath.M1CalRecordRCR(blockTwF))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return 消费的UTC时间
     * @throws ParseException
     */
    private String wRecordTime() throws ParseException {
        Date wRecordTime = new Date();//获取日期实例
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");//格式化日期数据
        long nowtime = dateFormat.parse(dateFormat.format(wRecordTime)).getTime();//通过格式化后获取时间
        long millionSeconds = dateFormat.parse("19700101000000").getTime();//毫秒
        long NowTime = nowtime - millionSeconds;//获取当前时间
        String wRecordTimes = Long.toHexString(NowTime / 1000);//
        return wRecordTimes;
    }


    private boolean WriteCardRecord() {
        return false;
    }

    /**
     * 处理交易记录文件-24block
     *首先查看交易进程是否为 01交易开始 02交易结束
     * 再去查看黑名单状态是否为04 如果是04 有两个分支
     * 1.交易进程为02 则直接修改黑名单为白
     * 2.交易进程为01 则去判断9 与 10值是否相等
     *
     */

    /**
     * 消费月票 M1
     * 消费步骤
     * 1.验证扇区:
     *
     * @param payNum 消费的次数 1=1次 2=2次 不可为0次
     */
    @Override
    protected void payMiMonth(int payNum) {


    }
}
