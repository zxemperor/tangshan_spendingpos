package vdpos.com.tcps.basicplug.implementation.fixed;

import android.os.Parcelable;

import com.landicorp.android.eptapi.utils.BytesUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import vdpos.com.tcps.basicplug.implementation.priexcption.mifare.MifareCard;

public abstract class ConsumeRecord implements Parcelable {

    /**
     * 标记为记录状态是否为灰色记录
     */
    protected boolean GreyExcute = false;

    /**
     * @param GreyE 如果灰色记录状态由灰变白则传入true 标记为该记录为正常记录
     *              反之false 标记为灰记录
     */
    protected void setGreyExcute(boolean GreyE) {
        this.GreyExcute = GreyE;
    }

    /**
     * @return 如果返回TRUE 则为灰色记录做过处理 并成功处理 否则返回false
     */
    protected boolean getGreyExcute() {
        return this.GreyExcute;
    }

    /**
     * 前次交易金额（分）
     * 站点号
     * 校验码
     */
    //固定值（0x00） 1
    private byte[] fixed = {0x00};

    protected byte[] getFixed() { //1byte
        return fixed;
    }

    protected void setFixed(byte[] fixed) {
        this.fixed = fixed;
        System.arraycopy(this.fixed, 0, sendRecord, 0, 1);
    }

    //记录种类（0x01/0x11） 1
    private byte[] recordType = null;

    protected byte[] getRecordType() { //1byte
        return recordType;
    }

    protected void setRecordType(byte[] recordType) {
        this.recordType = recordType;
        System.arraycopy(recordType, 0, sendRecord, 1, 1);
    }

    //交易类型(0x00,0x01) 1
    private byte[] payType = null;

    public byte[] getPayType() { //1byte
        return payType;
    }

    public void setPayType(byte[] payType) {
        this.payType = payType;
        System.arraycopy(this.payType, 0, sendRecord, 2, 1);
    }

    //机具交易流水号 2
    private byte[] MachinesNo = new byte[2];
    private int macAddNum = 0;

    public byte[] getMachinesNo() {//2byte
        return MachinesNo;
    }

    public void setMachinesNo() {
        MachinesNo[0] = (byte) (macAddNum >> 8);
        MachinesNo[1] = (byte) macAddNum;
        if (macAddNum >= 65535) {
            macAddNum = 0;
        } else {
            macAddNum = macAddNum + 1;
        }
        System.out.println("计数器" + macAddNum + "换算后数组" + BytesUtil.bytes2HexString(MachinesNo));
        System.arraycopy(MachinesNo, 0, sendRecord, 3, 2);

    }

    //消费卡卡号 4
    private byte[] CardNo = new byte[4];

    public byte[] getCardNo() {
        return CardNo;
    }

    public String getCardNoStr() {
        byte[] cardNo = new byte[8];
        System.arraycopy(this.sendRecord, 9, cardNo, 0, 8);
        return BytesUtil.bytes2HexString(cardNo);
    }

    public void setCardNo(byte[] cardNo) {//4byte
        System.arraycopy(cardNo, 4, CardNo, 0, 4);
        System.arraycopy(CardNo, 0, sendRecord, 5, 4);

    }

    //发卡城市代码 2
    private byte[] cityNo = null;

    public byte[] getCityNo() {//2byte

        return cityNo;
    }

    public void setCityNo(byte[] cityNo) {
        this.cityNo = cityNo;
        System.arraycopy(this.cityNo, 0, sendRecord, 9, 2);

    }

    //应用行业代码 2
    private byte[] industry = new byte[2];

    public byte[] getIndustry() {
        return industry;
    }

    public void setIndustry(byte[] industry) {
        this.industry = industry;
        System.arraycopy(this.industry, 0, sendRecord, 11, 2);

    }

    //发卡流水号 4
    private byte[] issueNo = null;

    public byte[] getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(byte[] issueNo) {
        this.issueNo = issueNo;
        System.arraycopy(this.issueNo, 0, sendRecord, 13, 4);
    }

    //卡认证码 4
    private byte[] validationNo = null;

    public byte[] getValidationNo() {
        return validationNo;
    }

    public void setValidationNo(byte[] validationNo) {
        this.validationNo = validationNo;
        System.arraycopy(this.validationNo, 0, sendRecord, 17, 4);
    }

    //卡类型 1
    private byte[] CardType = null;

    public byte[] getCardType() {
        return CardType;
    }

    public void setCardType(byte[] cardType) {
        CardType = cardType;
        System.arraycopy(CardType, 0, sendRecord, 21, 1);
    }

    //交易时间 4
    private byte[] payTime = null;

    public byte[] getPayTime() {
        return payTime;
    }

    public void setPayTime(byte[] payTime) {
        this.payTime = payTime;
        System.arraycopy(this.payTime, 0, sendRecord, 22, 4);
    }

    //交易金额（分） 3
    private byte[] payMoney = new byte[3];

    public byte[] getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(byte[] payMoney) {
        System.arraycopy(payMoney, 8, this.payMoney, 0, 3);
        System.arraycopy(this.payMoney, 0, sendRecord, 26, 3);
    }

    // 交易前卡交易次数 2
    private byte[] payCount = null;

    public byte[] getPayCount() {
        return payCount;
    }

    public void setPayCount(byte[] payCount) {
        this.payCount = payCount;
        System.arraycopy(this.payCount, 0, sendRecord, 29, 2);
    }

    // 交易前原额（分） 4
    private byte[] HistoryMoney = null;

    public byte[] getHistoryMoney() {
        return HistoryMoney;
    }

    public void setHistoryMoney(byte[] historyMoney) {
        HistoryMoney = historyMoney;
        System.arraycopy(HistoryMoney, 0, sendRecord, 31, 4);
    }

    // 充值终端编号 4
    private byte[] rechargePosNo = null;

    public byte[] getRechargePosNo() {
        return rechargePosNo;
    }

    public void setRechargePosNo(byte[] rechargePosNo) {
        this.rechargePosNo = rechargePosNo;
        System.arraycopy(this.rechargePosNo, 0, sendRecord, 35, 4);
    }

    //钱包充值计数器 2
    private byte[] walletCount = null;

    public byte[] getWalletCount() {
        return walletCount;
    }

    public void setWalletCount(byte[] walletCount) {
        this.walletCount = walletCount;
        System.arraycopy(this.walletCount, 0, sendRecord, 39, 2);
    }

    // 固定数（0x00） 1
    private byte[] FixedNum = {0x00};

    public byte[] getFixedNum() {
        return FixedNum;
    }

    public void setFixedNum(byte[] fixedNum) {
        FixedNum = fixedNum;
        System.arraycopy(FixedNum, 0, sendRecord, 41, 1);
    }

    // 充值金额（分） 3
    private byte[] rechargeMoney = null;

    public byte[] getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(byte[] rechargeMoney) {
        this.rechargeMoney = new byte[3];
        System.arraycopy(rechargeMoney, 0, this.rechargeMoney, 0, 2);
        System.arraycopy(this.rechargeMoney, 0, sendRecord, 42, 3);
    }

    //充值日期 4
    private byte[] rechargeDate = null;

    public byte[] getRechargeDate() {
        return rechargeDate;
    }

    public void setRechargeDate(byte[] rechargeDate) {
        this.rechargeDate = rechargeDate;
        System.arraycopy(this.rechargeDate, 0, sendRecord, 45, 4);
    }

    //TAC交易认证码 4
    private byte[] TagNo = null;

    public byte[] getTagNo() {
        return TagNo;
    }

    public void setTagNo(byte[] tagNo) {
        TagNo = tagNo;
        System.arraycopy(TagNo, 0, sendRecord, 49, 4);
    }

    //消费城市代码 2
    private byte[] payCityCode = null;

    public byte[] getPayCityCode() {
        return payCityCode;
    }

    public void setPayCityCode(byte[] PayCityCode) {
        this.payCityCode = new byte[2];
        System.arraycopy(PayCityCode, 0, this.payCityCode, 0, 2);
        System.arraycopy(payCityCode, 0, sendRecord, 53, 2);
    }

    //前次交易终端代码 2
    private byte[] HisPosNo = null;

    public byte[] getHisPosNo() {
        return HisPosNo;
    }

    public void setHisPosNo(byte[] recordBlock) {
        HisPosNo = new byte[2];
        System.arraycopy(recordBlock, 12, HisPosNo, 0, 2);
        System.arraycopy(HisPosNo, 0, sendRecord, 55, 2);
    }

    //前次交易日期时间 4
    private byte[] HisPayDate = null;

    public byte[] getHisPayDate() {
        return HisPayDate;
    }

    public void setHisPayDate(byte[] recordBlock) {
        HisPayDate = new byte[4];
        System.arraycopy(recordBlock, 0, HisPayDate, 0, 4);
        System.arraycopy(HisPayDate, 0, sendRecord, 57, 4);
    }

    //前次交易金额（分） 2
    private byte[] oldPayMoney = null;

    public byte[] getOldPayMoney() {
        return oldPayMoney;
    }

    public void setOldPayMoney(byte[] OldPayMoney) {
        oldPayMoney = new byte[2];
        System.arraycopy(OldPayMoney, 8, oldPayMoney, 0, 2);
        System.arraycopy(oldPayMoney, 0, sendRecord, 61, 2);
    }

    protected void setOldRecord(byte[] recordBlock) {
        setHisPosNo(recordBlock);
        setHisPayDate(recordBlock);
        setOldPayMoney(recordBlock);
    }

    //站点号 1
    private byte[] siteCode = null;

    public byte[] getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(byte[] SiteCode) {
        siteCode = SiteCode;
        System.arraycopy(siteCode, 0, sendRecord, 63, 1);
    }

    //校验码 1
    private byte[] checkCode = {0x00};

    public byte[] getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(byte[] CheckCode) {
        checkCode = CheckCode;
        System.arraycopy(checkCode, 0, sendRecord, 64, 1);
    }

    //64字节传输 消费记录
    private byte[] sendRecord = new byte[65];

    public byte[] getSendRecord() {
        return sendRecord;
    }

    public void setSendRecord(byte[] SendRecord) {
        sendRecord = SendRecord;
    }


    /**
     * 保存 消费记录用于连续级别操作
     * File fileObj = new File(context.getFilesDir().getPath().toString()+"Deskey.fixed");
     * ObjectOutputStream file_io = new ObjectOutputStream(new FileOutputStream(fileObj));
     * file_io.writeObject(IDeskey);
     * file_io.close();
     *
     * @param storagePath 存储路径
     */
    public void saveRecord(String storagePath) throws IOException {
        File Record = new File(storagePath + "ConsumeRecord.record");
        ObjectOutputStream obj_out = new ObjectOutputStream(new FileOutputStream(Record));
        obj_out.writeObject(this);
        obj_out.close();
    }

    /**
     * @param path
     * @return File fileObj = new File(context.getFilesDir().getPath().toString()+"Deskey.fixed");
     * ObjectInputStream file_io = new ObjectInputStream(new FileInputStream(fileObj));
     * DeskeyInfo deskeyObj = (DeskeyInfo) file_io.readObject();
     * System.out.println("固化key：："+ BytesUtil.bytes2HexString(deskeyObj.GetTkeyVal()));
     * file_io.close();
     * return deskeyObj;
     */
    public static ConsumeRecord getRecord(String path) throws IOException, ClassNotFoundException {
        File fileObj = new File(path + "ConsumeRecord.record");
        ObjectInputStream file_io = new ObjectInputStream(new FileInputStream(fileObj));
        ConsumeRecord cr = (ConsumeRecord) file_io.readObject();
        file_io.close();
        return cr;
    }

    protected void swtCardInfo(MifareCard micard) {

    }


    protected abstract void swtCardInfo(MifareCard micard, byte payType);


}
