package vdpos.com.tcps.basicplug.util.error.status;

public enum CardPayFixed {
    MicardWalletPay(0x01),//本地钱包消费
    MiNumberPay(0x02),//02	公交乘次消费
    MiReserved(0x03),//03	预留钱包消费
    MiNoWalletPay(0x04),//04	非计次非钱包消费
    MiAnotherWalletPay(0x51),//51	异地钱包消费
    MiOverdraft(0x81),//81	透支消费
    //============================卡自身状态黑白名单记录END======================================
    RecWalletGray(0x01),//记录灰名单钱包
    RecWalletWhite(0x00),//记录钱包正常
    RecCityMonWhite(0x02),//市区月票记录	0x02
    RecCityMonGray(0x03),// 市区月票灰记录	0x03
    RecOutskirtsWhite(0x0A),//郊区月票记录	0x0A
    RecOutskirtsGray(0x0B),//郊区月票灰记录	0x0B
    RecQuarterWhite(0x04),//季票记录	0x04
    RecQuarterGray(0x05),//季票灰记录	0x05
    RecYearWhite(0x06),//    年票半年票记录	0x06
    RecYearGray(0x07),//    年票半年票灰记录	0x07
    RecDayWhite(0x08),//    日票记录	0x08
    RecDayGray(0x09);//    日票灰记录	0x09
    //=========================消费记录状态END================================================
    private int payType;

    CardPayFixed(int i) {
        this.payType = i;
    }

    public int getType() {
        return payType & 0xff;
    }
}
