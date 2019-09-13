package vdpos.com.tcps.basicplug.implementation.priexcption.mifare;

import com.landicorp.android.eptapi.card.MifareDriver;
import com.landicorp.android.eptapi.card.RFDriver;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.BytesBuffer;
import com.landicorp.android.eptapi.utils.BytesUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import vdpos.com.tcps.basicplug.basis.abstracts.CardBasis;
import vdpos.com.tcps.basicplug.basis.abstracts.function.M1CalculateMath;
import vdpos.com.tcps.basicplug.basis.extend.PosScan;
import vdpos.com.tcps.basicplug.implementation.priexcption.LogicException;
import vdpos.com.tcps.basicplug.util.error.status.ExcuteStatus;

public class MifareCard extends CardBasis {
    /**
     * 卡私密信息 不对外提供数据
     */
    private M1CalculateMath cardMath = new M1CalculateMath();
    private MifareDriver M1driver = null;
    private byte CsnNo[] = null;//卡芯片号
    private byte SectorID[] = null;//存入扇区分散值

    /**
     * M1卡实例的构造函数
     * 执行步骤
     * 1.获取MifareDriver 实例对象进行赋值
     * 2.获取卡芯片号赋值
     * 3.计算KEY 01扇区KEY值
     * 错误序号:ck12-14
     */
    public MifareCard(RFDriver rfDriver, byte[] cardCSN, String cardModel) {
        try {
            CalculateCardKeyFun calculate = new CalculateCardKeyFun();
            this.M1driver = (MifareDriver) rfDriver;
            this.cardType = ExcuteStatus.MI_TYPE.getStatus();
            this.CsnNo = cardCSN;//获取卡芯片号
            this.cardModel = cardModel;
            calculate.setKeyBasis(this);
            calculate.CalculatBasisKey(this);
        } catch (RequestException e) {
            this.activity = false;
            PosScan.findCardCB.FindCardException("ck12:" + e.getMessage(), ExcuteStatus.REQUESTERROR.getStatus());
        } catch (LogicException e) {
            this.activity = false;
            PosScan.findCardCB.FindCardException(e.getMessage(), ExcuteStatus.VALIDA_ERROR.getStatus());
        } catch (Exception e) {
            this.activity = false;
            e.printStackTrace();
            PosScan.findCardCB.FindCardException("ck15:" + e.getMessage(), ExcuteStatus.GENER_EXCEPT.getStatus());
        }
    }

    protected void setSectorID(byte[] value) {
        this.SectorID = value;
    }

    protected byte[] getSectorID() {
        return this.SectorID;
    }

    private byte Record[] = null; //消费记录
    private boolean activity = true;

    protected void setActivity(boolean b) {
        this.activity = b;
    }

    /**
     * 判断当前M1卡实例是否为活跃状态
     *
     * @return boolean
     */
    public boolean getActivity() {
        return this.activity;
    }

    //状态各扇区KEYA值
    private HashMap<Byte, ByteBuffer> KeyStorge = new HashMap();//存储装载垕的卡密钥

    protected void setKeyStorge(byte key, ByteBuffer bbufer) {
        this.KeyStorge.put(key, bbufer);
    }

    protected HashMap<Byte, ByteBuffer> getKeyStorge() {
        return this.KeyStorge;
    }

    @Override
    protected void setCardType(byte cardType) {
        super.cardType = cardType;
    }

    /**
     * 设置M1卡类型函数
     *
     * @param IncardType CPU /M1 S50 S70 S70_PRO S50_PRO等标注的卡类型
     */
    @Override
    protected void setCardModel(String IncardType) {
        super.cardModel = IncardType;
    }
    /**-----------------------------------------------------------------------*/
    /**
     * 卡的基本信息 对外提供数据
     */
    private byte[] CardNo = null;//1卡号

    private void setCardNo(byte[] FblockVal) { //此处传递为第四块值
        this.CardNo = new byte[8];
        System.arraycopy(FblockVal, 0, this.CardNo, 0, 8);
    }

    protected byte[] getCardNo() { //此处传递为第四块值
        return CardNo;
    }

    protected String getCardNoStr() { //此处传递为第四块值
        return BytesUtil.bytes2HexString(CardNo);
    }

    private byte[] CityNo = null;//2城市代码

    //发卡城市代码 2 0x00 1	应用代码1 	发行流水号4	卡认证码4	启用标志1	卡类1	押金1	校验1
    private void setCityNo(byte[] FblockVal) {
        this.CityNo = new byte[2];
        System.arraycopy(FblockVal, 0, this.CityNo, 0, 2);
    }

    public byte[] getCityNo() {
        return CityNo;
    }

    private byte AppNo = 0x00;//3应用代码

    private void setAppNo(byte[] FblockVal) {
        this.AppNo = FblockVal[3];
    }

    protected byte getAppNo() {
        return AppNo;
    }

    private byte[] Issuance = null;//4发行流水号

    private void setIssuance(byte[] FblockVal) {
        this.Issuance = new byte[4];
        System.arraycopy(FblockVal, 4, this.Issuance, 0, 4);
    }

    protected byte[] getIssuance() {
        return Issuance;
    }

    private byte[] CardVaildNo = null;//5卡认证码

    private void setCardVaildNo(byte[] FblockVal) {
        this.CardVaildNo = new byte[4];
        System.arraycopy(FblockVal, 8, this.CardVaildNo, 0, 4);
    }

    protected byte[] getCardVaildNo() {
        return CardVaildNo;
    }

    private byte enableTag = 0x00;//6卡启用标识

    private void setEnableTag(byte[] FblockVal) {
        this.enableTag = FblockVal[12];
    }

    private byte business_type = 0x00;//7业务类型 卡主类型

    private void setBusiness_type(byte[] FblockVal) {
        this.business_type = FblockVal[13];
    }

    protected byte getBussiness_type() {
        return business_type;
    }

    protected void setFourBlockAttr(byte[] Fblockval) {
        //  System.out.println("赋值用4块值" + BytesUtil.bytes2HexString(Fblockval));
        this.setCardNo(Fblockval);
        this.setCityNo(Fblockval);
        this.setAppNo(Fblockval);
        this.setIssuance(Fblockval);
        this.setCardVaildNo(Fblockval);
        this.setEnableTag(Fblockval);
        this.setBusiness_type(Fblockval);
    }

    //==========================================块4数据END============================================
    private byte[] issuance_date = null;//8发行日期

    private void setIssuance_date(byte[] FiblockVal) {
        this.issuance_date = new byte[4];
        System.arraycopy(FiblockVal, 0, this.issuance_date, 0, 4);
    }

    private byte[] vaild_date = null;//9有效日期

    private void setVaild_date(byte[] FiblockVal) {
        this.vaild_date = new byte[4];
        System.arraycopy(FiblockVal, 4, this.vaild_date, 0, 4);
    }

    private byte[] enable_date = null;//10启用日期

    private void setEnable_date(byte[] FiblockVal) {
        this.enable_date = new byte[4];
        System.arraycopy(FiblockVal, 0, this.enable_date, 0, 4);
    }

    private byte wallet_enable = 0x00;//11钱包启用标识

    private void setWallet_enable(byte[] FiblockVal) {
        this.wallet_enable = FiblockVal[14];
    }

    private byte[] walletRechargeCount = null;//钱包充值计数器

    private void setWalletRechargeCount(byte[] FiveBlock) {
        walletRechargeCount = new byte[2];
        System.arraycopy(FiveBlock, 12, walletRechargeCount, 0, 2);
    }

    protected byte[] getWallRecCount() {
        return this.walletRechargeCount;
    }

    protected void setFiveBlockAttr(byte[] FiveBlock) { //第5块
        //  System.out.println("赋值用5块值" + BytesUtil.bytes2HexString(FiveBlock));
        setIssuance_date(FiveBlock);
        setVaild_date(FiveBlock);
        setEnable_date(FiveBlock);
        setWallet_enable(FiveBlock);
        setWalletRechargeCount(FiveBlock);
        StringBuffer str = new StringBuffer();
        str.append(BytesUtil.bytes2HexString(this.issuance_date)).
                append("|").
                append(BytesUtil.bytes2HexString(this.vaild_date)).
                append("|").
                append(BytesUtil.bytes2HexString(this.enable_date)).
                append("|").
                append(BytesUtil.bytes2HexString(new byte[]{this.wallet_enable}));
        //    System.out.println("获取的第5块拼接值" + str.toString());
    }

    //==========================================块5数据END==============================================
    private byte[] rechargeDate = null; //充值时间（年月日时分)

    protected void setRechargeDate(byte[] blockSix) {
        this.rechargeDate = new byte[5];
        System.arraycopy(blockSix, 0, rechargeDate, 0, 5);
    }

    protected byte[] getRechargeDate() {
        return this.rechargeDate;
    }

    private byte[] RechargeOriginalMoney = null;//充值前原额

    protected void setRechargeOriginalMoney(byte[] blockSix) {
        this.RechargeOriginalMoney = new byte[4];
        System.arraycopy(blockSix, 5, RechargeOriginalMoney, 0, 4);
    }

    private byte[] currentRechargeMoney = null;//本次充值额

    protected void setCurrentRechargeMoney(byte[] blockSix) {
        this.currentRechargeMoney = new byte[3];
        System.arraycopy(blockSix, 9, currentRechargeMoney, 0, 2);
    }

    protected byte[] getCurrentRechargeMoney() {
        return currentRechargeMoney;
    }

    protected void setBlockSixAttr(byte[] blocksix) {
        setRechargeDate(blocksix);
        setRechargeOriginalMoney(blocksix);
        setCurrentRechargeMoney(blocksix);
    }
//==========================================块6数据END==============================================

    private byte[] wallet_money = null;//12历史钱包余额

    protected void setWallet_money(byte[] nblockVal) {
        this.wallet_money = new byte[4];
        System.arraycopy(nblockVal, 0, this.wallet_money, 0, 4);
    }

    public double getHisWall_money() {
        return cardMath.CalculateM1WalletInt(this.wallet_money);
    }

    protected byte[] getHisWall_moneyb() {
        return wallet_money;
    }

    private byte[] wallet_moneyBK = null;//历史钱包备份

    protected void setWallet_moneyBK(byte[] tblockVal) {
        this.wallet_moneyBK = new byte[4];
        System.arraycopy(tblockVal, 0, this.wallet_moneyBK, 0, 4);
    }

    public double getHisWall_moneyBK() {
        return cardMath.CalculateM1WalletInt(this.wallet_moneyBK);
    }
//====================================第9块第10块END================================================

    //====================================第24 25块END==================================================
    private byte[] month_num = null;//13历史月票次数

    protected void setMonth_num(byte[] teblockVal) { //28块
        this.month_num = new byte[4];
        System.arraycopy(teblockVal, 0, this.month_num, 0, 4);
    }

    public int getHisMonth_num() {
        return cardMath.getHistoryMonthNum(this.month_num);
    }

    private byte[] standMonthNum = null; //月票每月标准限次

    public void setStandMonthNum(byte[] teblockVal) {
        this.standMonthNum = new byte[2];
        System.arraycopy(teblockVal, 0, this.standMonthNum, 0, 2);
    }

    public int getStandMonthNum() {
        return 0;
    }


    private byte[] monEnable_date = null;//14月票启用日期

    protected void setMonEnable_date(byte[] teblockVal) {
        this.monEnable_date = new byte[3];
        System.arraycopy(teblockVal, 3, this.monEnable_date, 0, 3);
    }

    private byte[] monEnd_date = null;//15月票结束日期

    protected void setMonEnd_date(byte[] teblockVal) {
        this.monEnd_date = new byte[3];
        System.arraycopy(teblockVal, 5, this.monEnd_date, 0, 3);
    }

    protected void setTwentyEightBlockAttr(byte[] tweblock) { //设置28块数据
        setMonEnable_date(tweblock);
        setMonEnd_date(tweblock);
    }

    private byte[] card_use_permiss = null;//16卡使用权限

    private void setCard_use_permiss(byte[] tsblockVal) {//26块
        this.card_use_permiss = new byte[2];
        System.arraycopy(tsblockVal, 9, this.card_use_permiss, 0, 2);
    }

    private byte[] year_check_date = null;//17年检日期

    private void setYear_check_date(byte[] tsblockVal) {
        this.year_check_date = new byte[4];
        System.arraycopy(tsblockVal, 11, this.year_check_date, 0, 4);
    }


    protected void setTwentySixBlockAttr(byte[] TwentySixBlock) {
        System.out.println("赋值用26块值" + BytesUtil.bytes2HexString(TwentySixBlock));
        setCard_use_permiss(TwentySixBlock);
        setYear_check_date(TwentySixBlock);
        StringBuffer str = new StringBuffer();
        str.append(BytesUtil.bytes2HexString(this.issuance_date) + "|").
                append(BytesUtil.bytes2HexString(this.vaild_date));
        System.out.println("获取的第211Create and use a repository6块拼接值" + str.toString());
    }

/**-----------------------------------------------------------------------*/
    /**
     * 获取卡主类型
     *
     * @return byte : businessType 卡主类型
     */
    public int getBusinessType() {
        return this.business_type & 0xff;
    }


    public byte[] getCsnNo() {
        return this.CsnNo;
    }

    public MifareDriver getM1driver() {
        return this.M1driver;
    }


    /**
     * 读取卡内容数据抽象功能。
     *
     * @return String
     */
    @Override
    protected byte[] readCardInfo(int block) throws RequestException {
        BytesBuffer getbuffer = new BytesBuffer();//获取BYTE缓存
        M1driver.readBlock(block, getbuffer);
        return getbuffer.getData();
    }

    @Override
    protected byte[] readCardInfo() {
        return new byte[0];
    }

    /**
     * 获取卡的基本需求属性
     * 对应-----卡的基本信息 对外提供数据
     */
    private void getBaseInfo() {
    }

    /**
     * 写入卡内容数据抽象功能
     *
     * @return String
     */
    @Override
    protected boolean writeCardInfo(int block, byte[] val) {
        try {
            System.out.println("写入的块数" + block + "写入的值" + BytesUtil.bytes2HexString(val));
            if (0 != this.M1driver.writeBlock(block, val)) {
                System.out.println("写卡失败" + this.M1driver.writeBlock(block, val));
                return false;
            }
        } catch (RequestException e) {
            System.out.println("写卡失败2" + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    protected String writeCardInfo() {

        return null;
    }

    @Override
    public String getRecordConsume() {
        return null;
    }




    /*-------------------------------------------------功能函数--------------------------------------------------------*/

    /**
     * 获取卡的基本信息
     * 返回有序的值序列
     */
    public ArrayList<String> getCardBasicInfoList() {
        ArrayList<String> listParm = new ArrayList<>();
        listParm.add(BytesUtil.bytes2HexString(this.CardNo));
        listParm.add(BytesUtil.bytes2HexString(this.CityNo));
        listParm.add(BytesUtil.bytes2HexString(new byte[]{this.AppNo}));
        listParm.add(BytesUtil.bytes2HexString(this.Issuance));
        listParm.add(BytesUtil.bytes2HexString(this.CardVaildNo));
        listParm.add(BytesUtil.bytes2HexString(new byte[]{this.enableTag}));
        listParm.add(BytesUtil.bytes2HexString(new byte[]{this.business_type}));
        listParm.add(BytesUtil.bytes2HexString(this.issuance_date));
        listParm.add(BytesUtil.bytes2HexString(this.vaild_date));
        listParm.add(BytesUtil.bytes2HexString(this.enable_date));
        listParm.add(BytesUtil.bytes2HexString(new byte[]{this.wallet_enable}));
        listParm.add(BytesUtil.bytes2HexString(this.card_use_permiss));
        listParm.add(BytesUtil.bytes2HexString(this.year_check_date));
        listParm.add(BytesUtil.bytes2HexString(this.monEnable_date));
        listParm.add(BytesUtil.bytes2HexString(this.monEnd_date));
        return listParm;
    }

    /**
     * @return HashMap<String, String> 结构实例对象
     */
    public HashMap<String, String> getCardBasicInfoMap() {
        HashMap<String, String> returnMap = new HashMap();
        returnMap.put("CardNo", BytesUtil.bytes2HexString(this.CardNo));
        returnMap.put("CityNo", BytesUtil.bytes2HexString(this.CityNo));
        returnMap.put("AppNo", BytesUtil.bytes2HexString(new byte[]{this.AppNo}));
        returnMap.put("Issuance", BytesUtil.bytes2HexString(this.Issuance));
        returnMap.put("CardVaildNo", BytesUtil.bytes2HexString(this.CardVaildNo));
        returnMap.put("enableTag", BytesUtil.bytes2HexString(new byte[]{this.enableTag}));
        returnMap.put("businType", BytesUtil.bytes2HexString(new byte[]{this.business_type}));
        returnMap.put("issuDate", BytesUtil.bytes2HexString(this.issuance_date));
        returnMap.put("vaildDate", BytesUtil.bytes2HexString(this.vaild_date));
        returnMap.put("enabDate", BytesUtil.bytes2HexString(this.enable_date));
        returnMap.put("walletEnab", BytesUtil.bytes2HexString(new byte[]{this.wallet_enable}));
        returnMap.put("cardsePerm", BytesUtil.bytes2HexString(this.card_use_permiss));
        returnMap.put("yearCheckDate", BytesUtil.bytes2HexString(this.year_check_date));
        returnMap.put("monEnableDate", BytesUtil.bytes2HexString(this.monEnable_date));
        returnMap.put("monEndDate", BytesUtil.bytes2HexString(this.monEnd_date));
        return returnMap;
    }

    /**
     * 获取以|分割的数据串值
     *
     * @return String
     */
    public String getCardBasicInfoStr() {
        StringBuffer basicInfo = new StringBuffer();
        basicInfo.append("9000|").append(BytesUtil.bytes2HexString(this.CardNo)). //卡号
                append("|").
                append(BytesUtil.bytes2HexString(this.CityNo)). //城市代码
                append("|").
                append(BytesUtil.bytes2HexString(new byte[]{this.AppNo})).//应用代码
                append("|").
                append(BytesUtil.bytes2HexString(this.Issuance)). //发行流水号
                append("|").
                append(BytesUtil.bytes2HexString(this.CardVaildNo)).//卡认证码
                append("|").
                append(BytesUtil.bytes2HexString(new byte[]{this.enableTag})).//卡启用标识
                append("|").
                append(BytesUtil.bytes2HexString(new byte[]{this.business_type})).//卡主类型
                append("|").
                append(BytesUtil.bytes2HexString(this.issuance_date)).//发行日期
                append("|").
                append(BytesUtil.bytes2HexString(this.vaild_date)).//有效日期
                append("|").
                append(BytesUtil.bytes2HexString(this.enable_date)).//启用日期
                append("|").
                append(BytesUtil.bytes2HexString(new byte[]{this.wallet_enable})).//钱包启用标识
                append("|").
                append(BytesUtil.bytes2HexString(this.card_use_permiss))//卡使用权限
                .append("|").
                append(BytesUtil.bytes2HexString(this.year_check_date))//年检日期
                .append("|").
                append(BytesUtil.bytes2HexString(this.monEnable_date))//月票启用日期
                .append("|").
                append(BytesUtil.bytes2HexString(this.monEnd_date));//月票结束日期
        System.out.println("带分割符号:" + basicInfo.toString());
        return basicInfo.toString();
    }

    /**
     * @param money 钱包消费
     */
    public void Consume(String money) {
        try {
            PayMiProcess paymi = new PayMiProcess(this);//获取消费类
            int flag = (int) ((Double.parseDouble(money) * 100) % 100);
            if (flag == 0) {//消费月票
                //"月票消费失败了 则直接去消费钱包"
            } else {//消费钱包
                paymi.payWallet(Double.parseDouble(money));
            }
        } catch (RequestException e) {
            e.printStackTrace();
        } catch (LogicException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //decrement
    protected void DecrementWallet(int consumeMoney) throws RequestException {
        this.M1driver.decrease(9, consumeMoney);
        this.M1driver.decrease(10, consumeMoney);
    }

    protected void DecrementWalletPub(int consumeMoney) throws RequestException {
        DecrementWallet(consumeMoney);
    }
}

