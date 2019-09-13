package vdpos.com.tcps.basicplug.implementation.priexcption.mifare;

import java.io.IOException;

import vdpos.com.tcps.basicplug.implementation.fixed.ConsumeRecord;

public class ConsumeRecordMi extends ConsumeRecord {
    public ConsumeRecordMi() {
        super();
    }

    @Override
    protected void setOldRecord(byte[] recordBlock) {
        super.setOldRecord(recordBlock);
    }

    /**
     * @param GreyE 如果灰色记录状态由灰变白则传入true 标记为该记录为正常记录
     *              反之false 标记为灰记录
     */
    @Override
    protected void setGreyExcute(boolean GreyE) {
        super.setGreyExcute(GreyE);
    }

    /**
     * @return 如果返回TRUE 则为灰色记录做过处理 并成功处理 否则返回false
     */
    @Override
    protected boolean getGreyExcute() {
        return super.getGreyExcute();
    }

    @Override
    public byte[] getFixed() {
        return super.getFixed();
    }

    @Override
    public void setFixed(byte[] fixed) {
        super.setFixed(fixed);
    }

    @Override
    public byte[] getRecordType() {
        return super.getRecordType();
    }

    @Override
    public void setRecordType(byte[] recordType) {
        super.setRecordType(recordType);
    }

    @Override
    public byte[] getPayType() {
        return super.getPayType();
    }

    @Override
    public void setPayType(byte[] payType) {
        super.setPayType(payType);
    }

    @Override
    public byte[] getMachinesNo() {
        return super.getMachinesNo();
    }

    @Override
    public void setMachinesNo() {
        super.setMachinesNo();
    }

    @Override
    public byte[] getCardNo() {
        return super.getCardNo();
    }

    @Override
    public String getCardNoStr() {
        return super.getCardNoStr();
    }

    @Override
    public void setCardNo(byte[] cardNo) {
        super.setCardNo(cardNo);
    }

    @Override
    public byte[] getCityNo() {
        return super.getCityNo();
    }

    @Override
    public void setCityNo(byte[] cityNo) {
        super.setCityNo(cityNo);
    }

    @Override
    public byte[] getIndustry() {
        return super.getIndustry();
    }

    @Override
    public void setIndustry(byte[] industry) {
        super.setIndustry(industry);
    }

    @Override
    public byte[] getIssueNo() {
        return super.getIssueNo();
    }

    @Override
    public void setIssueNo(byte[] issueNo) {
        super.setIssueNo(issueNo);
    }

    @Override
    public byte[] getValidationNo() {
        return super.getValidationNo();
    }

    @Override
    public void setValidationNo(byte[] validationNo) {
        super.setValidationNo(validationNo);
    }

    @Override
    public byte[] getCardType() {
        return super.getCardType();
    }

    @Override
    public void setCardType(byte[] cardType) {
        super.setCardType(cardType);
    }

    @Override
    public byte[] getPayTime() {
        return super.getPayTime();
    }

    @Override
    public void setPayTime(byte[] payTime) {
        super.setPayTime(payTime);
    }

    @Override
    public byte[] getPayMoney() {
        return super.getPayMoney();
    }

    @Override
    public void setPayMoney(byte[] payMoney) {
        super.setPayMoney(payMoney);
    }

    @Override
    public byte[] getPayCount() {
        return super.getPayCount();
    }

    @Override
    public void setPayCount(byte[] payCount) {
        super.setPayCount(payCount);
    }

    @Override
    public byte[] getHistoryMoney() {
        return super.getHistoryMoney();
    }

    @Override
    public void setHistoryMoney(byte[] historyMoney) {
        super.setHistoryMoney(historyMoney);
    }

    @Override
    public byte[] getRechargePosNo() {
        return super.getRechargePosNo();
    }

    @Override
    public void setRechargePosNo(byte[] rechargePosNo) {
        super.setRechargePosNo(rechargePosNo);
    }

    @Override
    public byte[] getWalletCount() {
        return super.getWalletCount();
    }

    @Override
    public void setWalletCount(byte[] walletCount) {
        super.setWalletCount(walletCount);
    }

    @Override
    public byte[] getFixedNum() {
        return super.getFixedNum();
    }

    @Override
    public void setFixedNum(byte[] fixedNum) {
        super.setFixedNum(fixedNum);
    }

    @Override
    public byte[] getRechargeMoney() {
        return super.getRechargeMoney();
    }

    @Override
    public void setRechargeMoney(byte[] rechargeMoney) {
        super.setRechargeMoney(rechargeMoney);
    }

    @Override
    public byte[] getRechargeDate() {
        return super.getRechargeDate();
    }

    @Override
    public void setRechargeDate(byte[] rechargeDate) {
        super.setRechargeDate(rechargeDate);
    }

    @Override
    public byte[] getTagNo() {
        return super.getTagNo();
    }

    @Override
    public void setTagNo(byte[] tagNo) {
        super.setTagNo(tagNo);
    }

    @Override
    public byte[] getPayCityCode() {
        return super.getPayCityCode();
    }

    @Override
    public void setPayCityCode(byte[] payCityCode) {
        super.setPayCityCode(payCityCode);
    }

    @Override
    public byte[] getHisPosNo() {
        return super.getHisPosNo();
    }

    @Override
    public void setHisPosNo(byte[] hisPosNo) {
        super.setHisPosNo(hisPosNo);
    }

    @Override
    public byte[] getHisPayDate() {
        return super.getHisPayDate();
    }

    @Override
    public void setHisPayDate(byte[] hisPayDate) {
        super.setHisPayDate(hisPayDate);
    }

    @Override
    public byte[] getOldPayMoney() {
        return super.getOldPayMoney();
    }

    @Override
    public void setOldPayMoney(byte[] OldPayMoney) {
        super.setOldPayMoney(OldPayMoney);
    }

    @Override
    public byte[] getSiteCode() {
        return super.getSiteCode();
    }

    @Override
    public void setSiteCode(byte[] SiteCode) {
        super.setSiteCode(SiteCode);
    }

    @Override
    public byte[] getCheckCode() {
        return super.getCheckCode();
    }

    @Override
    public void setCheckCode(byte[] CheckCode) {
        super.setCheckCode(CheckCode);
    }

    @Override
    public byte[] getSendRecord() {
        return super.getSendRecord();
    }


    @Override
    public void setSendRecord(byte[] SendRecord) {
        super.setSendRecord(SendRecord);
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
    @Override
    public void saveRecord(String storagePath) throws IOException {
        super.saveRecord(storagePath);
    }

    @Override
    protected void swtCardInfo(MifareCard micard) {
        super.swtCardInfo(micard);
    }

    /**
     * @param micard  M1卡实例
     * @param payType 交易类型
     */
    @Override
    protected void swtCardInfo(MifareCard micard, byte payType) {
        setMachinesNo();//机具消费记录计数器
        setCardNo(micard.getCardNo());//消费卡号
        setCityNo(micard.getCityNo());//城市代码
        setPayType(new byte[]{payType});//交易类型
        setIndustry(new byte[]{0x00, micard.getAppNo()});//应用行业代码
        setIssueNo(micard.getIssuance());//发行流水号
        setValidationNo(micard.getCardVaildNo());//卡认证码
        setCardType(new byte[]{micard.getBussiness_type()});//卡主类型
    }


}
