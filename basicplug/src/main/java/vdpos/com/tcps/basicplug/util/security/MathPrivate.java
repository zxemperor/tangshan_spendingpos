package vdpos.com.tcps.basicplug.util.security;

import java.security.SecureRandom;
/**
 * Created by zhaoxin on 2017-11-8.
 */

public class MathPrivate {

    public static String getRandom() {
        String returnNum = "";
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 4; i++) {
            returnNum += random.nextInt(9);
        }
        return returnNum;
    }

    /**
     * 根据卡芯片号获取
     */
//    public static CalculateBasisKey(){
//
//    }

    public static String DoubleBlock(int block) {
        if (block < 10) {
            return "0" + Integer.toString(block);
        }
        return Integer.toString(block);
    }

    public static String completionFF(String val) {
        int len = 8 - (val.length() / 2) % 8;
        for (int i = 0; i < len + (8 - 2); i++) {
            val += "FF";
        }
        return val;
    }

    public static String addZero(int needLen, int num) {
        if (num < 10) {
            return "0" + Integer.toString(num, 10);
        }
        return Integer.toString(num, 10);
    }


    /**
     * 2018-5-23 M1脱机密钥装载存储函数
     */
//    public static void InLoadPsamKey(String InloadKey, int type) {
//        String[] keys = InloadKey.split("\\|");
//        /**要存储的单位集合总数 2 3 4 5 6 7 8=7*/
//        //0+2=2 1+2=3 2+2=4 4+2=6 5+2=7 6+2=8
//        //0+2*6=12 1+3*6=24 2+4*6=36 3+5*6=48 4+6*6=60
//        if (type == 1) {
//            SecretKeyKit.addIteamTPRSK(InloadKey.substring(16, InloadKey.length()), InloadKey.substring(0, 12));
//        } else {
//            for (int i = 0; i < keys[1].length() / 12; i++) {
//                int tmp = (i + (i + 2)) * 6;
//                if (i == 0) {
//                    SecretKeyKit.addIteamTPSK((i + 2) + "", keys[1].substring(tmp - 12, tmp));
//                } else if ((i + 2) == 3) {//因为3 4 5 共享一个密钥所以加此判断
//                    SecretKeyKit.addIteamTPSK((i + 2) + "", keys[1].substring(tmp - 12, tmp));
//                    SecretKeyKit.addIteamTPSK((i + 3) + "", keys[1].substring(tmp - 12, tmp));
//                    SecretKeyKit.addIteamTPSK((i + 4) + "", keys[1].substring(tmp - 12, tmp));
//                } else {
//                    SecretKeyKit.addIteamTPSK((i + 4) + "", keys[1].substring(tmp - 12, tmp));
//                }
//            }
//        }
//    }

    /**
     * 判定所读取内容是否是合法的
     */
//    public static boolean judgementRec(String rec) {
//        byte[] recArr = StringUtils.hex2bytes(rec);//转换为byte数组
//        int recJudge = recArr[recArr.length - 1];
//        int judge = 0;
//
//        for (int i = 0; i < recArr.length - 4; i++) {
//            judge ^= (recArr[i] & 0xff);
//        }
//        if ((judge & 0xff) == (recJudge & 0xff)) {
//            return true;
//        }
//        return false;
//    }

    public static byte CalRecordRCR(byte[] b) {
        byte[] recArr = b;//转换为byte数组
        int judge = 0;
        for (int i = 0; i < recArr.length - 3; i++) {
            judge ^= (recArr[i] & 0xff);
        }
        return (byte) judge;
    }

    public static byte M1CalRecordRCR(byte[] b) {
        byte[] recArr = b;//转换为byte数组
        int judge = 0;
        for (int i = 0; i < recArr.length - 1; i++) {
            judge ^= (recArr[i] & 0xff);
        }
        return (byte) Math.abs(judge);
    }

//    public static String STR_CalRecordRCR(String b) {//只针对上层服务
//        byte[] recArr =new byte[b.length()/2+1]; // StringUtils.hex2bytes(b);//转换为byte数组
//        System.arraycopy(StringUtils.hex2bytes(b),0,recArr,0,recArr.length-2);
//        int judge = 0;
//        for (int i = 0; i < recArr.length-1; i++) {
//            judge ^= (recArr[i] & 0xff);
//        }
//        recArr[recArr.length-1]=(byte)judge;
//        return StringUtils.bytes2HexString(recArr);
//    }

    /**
     * M1钱转换为正常钱数
     **/
    public static String reMoney(String HexMoney) {
        //  String TmpHexMoney=Integer.toHexString( Integer.parseInt(HexMoney,16));
       // System.out.println("M1卡余额度"+HexMoney);
        String TmpHexMoney = HexMoney;
        String TmpVal = "";
        for (int i = 0; i < TmpHexMoney.length() / 2; i++) {
            TmpVal += TmpHexMoney.substring(TmpHexMoney.length() - ((i * 2) + 2), TmpHexMoney.length() - i * 2);
        }
        double retVal = Integer.parseInt(TmpVal, 16);
        retVal = retVal / 100;
        return retVal + "";
    }

    public static int reXMoney(String HexMoney) {
        //  String TmpHexMoney=Integer.toHexString( Integer.parseInt(HexMoney,16));
        String TmpHexMoney = HexMoney;
        String TmpVal = "";
        for (int i = 0; i < TmpHexMoney.length() / 2; i++) {
            TmpVal += TmpHexMoney.substring(TmpHexMoney.length() - ((i * 2) + 2), TmpHexMoney.length() - i * 2);
        }
        int retVal = Integer.parseInt(TmpVal, 16);
        return retVal;
    }

    /**
     * M1钱转换为正常钱数
     **/
    public static String reMothNum(String HexMoney) {
     //    String TmpHexMoney=Integer.toHexString( Integer.parseInt(HexMoney,16));
        String TmpHexMoney = HexMoney;
        String TmpVal = "";
        for (int i = 0; i < TmpHexMoney.length() / 2; i++) {
            TmpVal += TmpHexMoney.substring(TmpHexMoney.length() - ((i * 2) + 2), TmpHexMoney.length() - i * 2);
        }
       System.out.println("月票剩余次数"+TmpVal);
        int retVal = Integer.parseInt(TmpVal, 16);
       System.out.println("月票16进制剩余次数"+TmpVal);
        return retVal + "";
    }

    public static int reXMothNum(String HexMoney) {
        //  String TmpHexMoney=Integer.toHexString( Integer.parseInt(HexMoney,16));
        String TmpHexMoney = HexMoney;
        String TmpVal = "";
        for (int i = 0; i < TmpHexMoney.length() / 2; i++) {
            TmpVal += TmpHexMoney.substring(TmpHexMoney.length() - ((i * 2) + 2), TmpHexMoney.length() - i * 2);
        }
        int retVal = Integer.parseInt(TmpVal, 16);
        return retVal ;
    }

    public static String reXRecordMothNum(String HexMoney) {
        //  String TmpHexMoney=Integer.toHexString( Integer.parseInt(HexMoney,16));
        String TmpHexMoney = HexMoney;
        String TmpVal = "";
        for (int i = 0; i < TmpHexMoney.length() / 2; i++) {
            TmpVal += TmpHexMoney.substring(TmpHexMoney.length() - ((i * 2) + 2), TmpHexMoney.length() - i * 2);
        }
    //    int retVal = Integer.parseInt(TmpVal, 16);
        return TmpVal ;
    }

//    public static String getM1DQY_YC() {  //月票 年票 次数 读卡
//        int CanConMoney = 0;
//        String R28B = StringUtils.bytes2HexString(Record.getByteBlock28());
//        String R29B = StringUtils.bytes2HexString(Record.getByteBlock29());
//        int standMonthNum = Integer.parseInt(R28B.substring(0, 4), 16);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyMM", Locale.US);
//        int DateData = Integer.parseInt(sdf.format(new Date()));//获取当前月
//        int CardStarDate = Integer.parseInt(R28B.substring(4, 8));//卡内起始日期
//        int CardEndDate = Integer.parseInt(R28B.substring(10, 14));//卡内截至日期
//        //System.out.println(DateData+"||"+CardEndDate+"||"+standMonthNum);
//        byte []  truemoneybyte = StringUtils.hex2bytes(R29B.substring(0, 8));
//        int trueMoney = Integer.parseInt(reMothNum(R29B.substring(0, 8)));
//
//        System.out.println(DateData+"||"+CardEndDate+"||"+standMonthNum+"||"+trueMoney);
//        /*年票*/
//        if(CardStarDate%100 == 17&&CardEndDate%100 == 17){
//            int knczns= (CardEndDate/100)-(CardStarDate/100) + 1; //卡内充值年数
//            int dqsyns=(CardEndDate/100)-(DateData/100) + 1;//当前剩余年数
//            if(trueMoney>knczns*standMonthNum){
//                int returnval=knczns*standMonthNum;
//                return returnval+"|"+trueMoney;
//            }else if(dqsyns>1&&trueMoney>(dqsyns-1)*standMonthNum){
//                int returnval=trueMoney-(dqsyns-1)*standMonthNum;
//                return returnval+"|"+trueMoney;
//            }else if(dqsyns==1&&trueMoney<=standMonthNum&&trueMoney>=0){
//                return trueMoney+"|"+trueMoney;
//            }
//            return 0+"|"+trueMoney;
//        }else{
//            int NowSurplusYM = surplus_Months(DateData,CardEndDate);
//            int SurplusStandNum= NowSurplusYM * standMonthNum;
//            System.out.println(DateData+"||"+CardEndDate+"||"+NowSurplusYM+"||"+SurplusStandNum+"||"+standMonthNum+"||"+trueMoney);
//            if(CardEndDate<DateData){
//            } else if(trueMoney >= SurplusStandNum){
//                return standMonthNum+"|"+trueMoney;
//            }else if(trueMoney < SurplusStandNum && trueMoney > SurplusStandNum - standMonthNum){
//                return ( standMonthNum -( SurplusStandNum - trueMoney ) )+"|"+ trueMoney ;
//            }else if(trueMoney <= 0 || trueMoney <= SurplusStandNum - standMonthNum ){
//                return 0 + "|" + trueMoney;
//            }
//            return 0+"|"+0;
//        }
//        /*年票*/
//
////        if (CardEndDate - DateData >= 0) {
////            if (DateData <= CardEndDate) {
////                int OutTimeY = (CardEndDate - DateData);
////                if (OutTimeY >= 100) {
////                    OutTimeY = OutTimeY / 100 * 13 + OutTimeY%100;
////                } else if (OutTimeY == 0) {
////                    OutTimeY = 1;
////                }
////                int OutTimeM = OutTimeY ;
////                if (trueMoney > OutTimeM * standMonthNum) {
////                    return (trueMoney-(OutTimeM * standMonthNum)) + "|"+trueMoney;
////                } else if (trueMoney > OutTimeM * standMonthNum - standMonthNum) {
////                    return (trueMoney - (OutTimeM * standMonthNum - standMonthNum)) + "|"+trueMoney;
////                } else  if(CardStarDate%100 == 17&&CardEndDate%100 == 17&&CardStarDate/100<=DateData/100&&CardEndDate/100>DateData/100){
////                    OutTimeM=13-(DateData%100);
////                    if (trueMoney > OutTimeM * standMonthNum) {
////                        return standMonthNum + "";
////                    } else if (trueMoney > OutTimeM * standMonthNum - standMonthNum) {
////                        return (trueMoney - (OutTimeM * standMonthNum - standMonthNum)) + "|"+trueMoney;
////                    }
////                }
////            }
////        }
////        return 0 + "|"+0;
//    }

    /**
     * 判断M1月票剩余额度是否需要清空
     * 用来判断 月票是否有过期现象 如果出现那就彻底销毁之前残留月票并消费掉本次应当消费的次数
     * 如果符合正常消费那就消费单次
     * 如果已消费完当月次数了 那么就不允许再消费
     */
//    public static int ConsumeNum(byte[] read28B, byte[] read29B, String num) {
//        Record.setByteBlock29(read29B);
//        int CanConMoney = 0;
//        String R28B = StringUtils.bytes2HexString(read28B);
//        String R29B = StringUtils.bytes2HexString(read29B);
//        /****该卡标准次数****/
//        int standMonthNum = Integer.parseInt(R28B.substring(0, 4), 16);
//        /****该卡标准次数****/
//        int conNum = Integer.parseInt(num);
//        /**开始计算标准第28块月票起始期与截至期**/
//        SimpleDateFormat sdf = new SimpleDateFormat("yyMM", Locale.US);
//        int DateData = Integer.parseInt(sdf.format(new Date()));//获取当前月
//         //  System.out.println("POS当前月" + DateData);
//        /**1.比较-----截至月是否超时*/
//        int CardStarDate = Integer.parseInt(R28B.substring(4, 8));//卡内起始日期
//        int CardEndDate = Integer.parseInt(R28B.substring(10, 14));//卡内截至日期
//
//        if (CardEndDate - DateData >= 0) {
//            if (DateData <= CardEndDate) {
//                /**合法消费**/
//                int OutTimeY = (CardEndDate - CardStarDate);
//                if (OutTimeY >= 100) {
//                    OutTimeY = OutTimeY / 100 * 13;
//                } else if (OutTimeY != 0) {
//                    OutTimeY = 13;
//                }
//
//                int OutTimeM = OutTimeY + ((CardEndDate % 100 - DateData % 100));
//                if (OutTimeM == 0) {
//                    OutTimeM = 1;
//                }
//                /**年票*/
//                int trueMoney = Integer.parseInt(reMothNum(R29B.substring(0, 8)));
//                if(CardStarDate%100 == 17&&CardEndDate%100 == 17){
//                    OutTimeM=13-(DateData%100);
////                    System.out.println("年票超出月"+OutTimeM);
////                    System.out.println("年票起始"+CardStarDate/100);
////                    System.out.println("年票结束"+CardEndDate/100);
////                    System.out.println("当前年"+DateData/100);
////                    System.out.println("当前月"+DateData%100);
////                    System.out.println("标准月次"+standMonthNum);
//                    if((CardStarDate/100)<=(DateData/100)&&(DateData/100)<=(CardEndDate/100)){
//                       int knczns= (CardEndDate/100)-(CardStarDate/100) + 1; //卡内充值年数
//                        int dqsyns=(CardEndDate/100)-(DateData/100) + 1;//当前剩余年数
//                            if(trueMoney>knczns*standMonthNum){
//                                CanConMoney=conNum+trueMoney-knczns*standMonthNum; //大于卡内最大充值额度
//                                int xiaofeiqianyuane=knczns*standMonthNum;
//                                byte b[]=new byte[4];
//                                b[0]= (byte) ((xiaofeiqianyuane>>32) & 0x00 );
//                                b[1]= (byte) ((xiaofeiqianyuane>>16) & 0xff);
//                                b[2]= (byte) ((xiaofeiqianyuane>>8) & 0xff);
//                                b[3]= (byte) ((xiaofeiqianyuane) & 0xff);
//                                Record.ContiSetRecord(b, 35, 4, 0);//交易原额
//                                CardOpertions.getSingleton(null).setCannotM1Consume(false);//能消费
//                            }else if(dqsyns>1&&trueMoney>(dqsyns-1)*standMonthNum+conNum){
//                                CanConMoney=conNum;
//                                int xiaofeiqianyuane=trueMoney;
//                                byte b[]=new byte[4];
//                                b[0]= (byte) ((xiaofeiqianyuane>>32) & 0x00 );
//                                b[1]= (byte) ((xiaofeiqianyuane>>16) & 0xff);
//                                b[2]= (byte) ((xiaofeiqianyuane>>8) & 0xff);
//                                b[3]= (byte) ((xiaofeiqianyuane) & 0xff);
//                                Record.ContiSetRecord(b, 35, 4, 0);//交易原额
//                                CardOpertions.getSingleton(null).setCannotM1Consume(false);//能消费
//                            }else if(dqsyns==1&&trueMoney<=standMonthNum&&trueMoney>0){
//                                CanConMoney=conNum;
//                                int xiaofeiqianyuane=trueMoney;
//                                byte b[]=new byte[4];
//                                b[0]= (byte) ((xiaofeiqianyuane>>32) & 0x00 );
//                                b[1]= (byte) ((xiaofeiqianyuane>>16) & 0xff);
//                                b[2]= (byte) ((xiaofeiqianyuane>>8) & 0xff);
//                                b[3]= (byte) ((xiaofeiqianyuane) & 0xff);
//                                Record.ContiSetRecord(b, 35, 4, 0);//交易原额
//                                CardOpertions.getSingleton(null).setCannotM1Consume(false);//能消费
//                            }else{
//                                CardOpertions.getSingleton(null).setCannotM1Consume(true);//不能消费
//                            }
//                        return CanConMoney;
//                    }
//                }else{
////                    System.out.println("年票起始"+CardStarDate/100);
////                    System.out.println("年票结束"+CardEndDate/100);
////                    System.out.println("年票结束月"+CardEndDate%100);
////                    System.out.println("当前年"+DateData/100);
////                    System.out.println("当前月"+DateData%100);
////                    System.out.println("标准月次"+standMonthNum);
//                   int NowSurplusYM = surplus_Months(DateData,CardEndDate);
//            //       System.out.println("年月差额"+NowSurplusYM);
//                   int SurplusStandNum= NowSurplusYM * standMonthNum;
//              //      System.out.println("应剩额"+NowSurplusYM);
//                   if(trueMoney > SurplusStandNum){ //卡内剩余次数大于结余次数
//                       CanConMoney = trueMoney - SurplusStandNum + conNum;
//                       byte b[]=new byte[4];
//                    b[0]= (byte)( (standMonthNum>>32) & 0x00);
//                    b[1]= (byte)( (standMonthNum>>16)& 0xff);
//                    b[2]= (byte) ((standMonthNum>>8)& 0xff);
//                    b[3]= (byte) ((standMonthNum)& 0xff);
//                    Record.ContiSetRecord(b, 35, 4, 0);//交易原额
//                    CardOpertions.getSingleton(null).setCannotM1Consume(false);//能消费
//                   }else if(trueMoney <= SurplusStandNum && trueMoney > ( SurplusStandNum-standMonthNum+conNum ) ){ //符合消费次数 合理月次区间消费
//                    byte b[]=new byte[4];
//                      int xiaofeiqianshu = trueMoney - ( SurplusStandNum-standMonthNum );
//                    b[0]= (byte) ((xiaofeiqianshu>>32) & 0x00 );
//                    b[1]= (byte) ((xiaofeiqianshu>>16) & 0xff);
//                    b[2]= (byte) ((xiaofeiqianshu>>8) & 0xff);
//                    b[3]= (byte) ((xiaofeiqianshu) & 0xff);
//                    Record.ContiSetRecord(b, 35, 4, 0);//交易原额
//                    CardOpertions.getSingleton(null).setCannotM1Consume(false);//能消费
//                    CanConMoney = conNum;
//                   }else{
//                       CanConMoney = 0;
//                       CardOpertions.getSingleton(null).setCannotM1Consume(true);//不能消费
//                   }
//                }
//            }
//        }
//        /**2.比较-----当前月之前是否有过期未消费月票次数*/
//        return CanConMoney;
//    }
    /**
     * @Description 获取M1卡月票的剩余可用的月
     * @Parm CardStarDate CardEndDate
     * @return 差额月
     * 算法描述:由结束年减去起始年 结束月减起始月
     * 结束年减去当前年 结束月减去当前月
     * 获取当前剩余可用月
     * @link_method ConsumeNum
     * */
    public static int surplus_Months(int CardStarDate,int CardEndDate){
        int CardSurplusYear=(CardEndDate/100) - (CardStarDate/100);
        int CardSurplusMonth=(CardEndDate%100)-(CardStarDate%100);
      //  System.out.println("年差额"+CardSurplusYear);
      //  System.out.println("月差额"+CardSurplusMonth);
        CardSurplusYear = CardSurplusYear * 13;
        int CardSurplusYM = 0;
        if(CardSurplusYear > 0 ){
            CardSurplusYM = CardSurplusYear + CardSurplusMonth; // 卡内自身所剩月份
        }else{
            CardSurplusYM = CardSurplusYear + CardSurplusMonth + 1; // 卡内自身所剩月份
        }
         return CardSurplusYM;
    }

//    public static int ConsumeMoney(byte[] read9B, int num) {
//        Record.setByteBlock29(read9B);
//        String R9B = StringUtils.bytes2HexString(read9B);//获取第9块数据
//        int surplus = MathPrivate.reXMoney(R9B.substring(0, 8));
//        if (num <= surplus) {
//            CardOpertions.getSingleton(null).setCannotM1Consume(false);//能消费
//            return num;
//        }
//        CardOpertions.getSingleton(null).setCannotM1Consume(true);//不能消费
//        return 0;
//    }
//
//    public static String byteToBit(byte b) {
////        return ""
////                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
////                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
////                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
////                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
//        String rebate = "";
//        for (int i = 0; i < 8; i++) {
//            rebate += ((byte) ((b & 0xff) >> i) & 0x01);
//        }
//        return rebate;
//    }
//
//    public static String CpubyteToBit(byte[] b, int CalLen, int copyIndex) {
//        long auth = 0;
//        long moveIndex = 16;
//        for (int i = 0; i < CalLen; i++) {
//            auth += ((b[copyIndex + i] & 0xff) << moveIndex);
//            moveIndex = moveIndex - moveIndex / 2;
//        }
//
//        String rebate = "";
//        for (int i = CalLen * 8 - 1; i >= 0; i--) {
//            rebate += ((byte) (auth >> i) & 0x01);
//        }
//        return rebate;
//    }
//    public static int CalCpuMonthC(byte[] File15V, byte[] F5CV, int money) {
//        int standMoney = ((File15V[12] & 0xff) << 8) + (File15V[13] & 0xff);//卡内基准月---每月标准次数(适用于月票)
//        SimpleDateFormat sdf = new SimpleDateFormat("yyMM", Locale.US);//获取POS机本地时间
//        Date date = new Date();//启用数据格式
//        int standDate = Integer.parseInt(sdf.format(date));//根据简单数据格式将date数据转换为无符号int数
//        int NewYEYAM = (standDate / 100 * 18 + (standDate % 100));//新的余额年月
//        int ke_nei_yu_e_ri_qi=((~F5CV[1])<<4)+((~F5CV[2]>>4)&0x0f);
//      //  System.out.println("F5CVStr"+StringUtils.bytes2HexString(F5CV));
//        int starDate = Integer.parseInt(Record.TmpRecoard(File15V, 2, 5));//根据15文件读取起始月
//        int endDate = Integer.parseInt(Record.TmpRecoard(File15V, 2, 9));//根据15文件读取结束月
//        int ConsumeMoney = 0; //初始化一个临时局部变量 为消费金额
//        if (standDate - starDate < 0) {
//            CardOpertions.getSingleton(null).setCannotCPUConsume(true);
//        }
//        int standSYC = 0;//标准月满状态
//        int TimeOut = endDate / 100 - standDate / 100;
//        if (TimeOut > 0) {//大于0为满足1正年的条件
//            TimeOut = TimeOut * 13;//截至年大于标准年可以消费  计数为13个月 上一年6月-下一年6月
//        }
//        if (TimeOut <= 0) {//如果小于1则为不满足1正年的条件
//            TimeOut = 0;
//        }
//        if (TimeOut >= 13 && (endDate % 100 - standDate % 100) < 0) {//余数
//            TimeOut = TimeOut + (endDate % 100 - standDate % 100);
//            standSYC = TimeOut * standMoney;//标准月满状态
//        } else if (TimeOut >= 0 && (endDate % 100 - standDate % 100) > 0) {
//            TimeOut = TimeOut + (endDate % 100 - standDate % 100);
//            standSYC = TimeOut * standMoney;//标准月满状态
//        } else if (TimeOut <= 0 && (endDate % 100 - standDate % 100) == 0) {
//            TimeOut = TimeOut + 1;
//            standSYC = TimeOut * standMoney;//标准月满状态
//        }
//        byte[] NowYAM = new byte[2];
//        NowYAM[0] = (byte) ((F5CV[1] & 0xff) >> 4);//从去除0位无效字段后截取
//        NowYAM[1] = (byte) (((F5CV[1] & 0xff) << 4) + ((F5CV[2] & 0xff) >> 4));
////00EE212C
//        byte[] NowMoney = new byte[2];
//        NowMoney[0] = (byte) ((F5CV[2] & 0x0f) << 4);
//        NowMoney[0] = (byte) ((NowMoney[0] >> 4) & 0x0f);
//        NowMoney[1] = (byte) (F5CV[3] & 0xff);
//        int IntNowMoney = ((NowMoney[0] & 0xff) << 8) + (NowMoney[1] & 0xff);
//        byte [] yearType=Record.getReturnRIteam(1,18);
//        if(yearType[0] == 0x08){//判断年票
//            NewYEYAM = NewYEYAM - (NewYEYAM-NewYEYAM/18*18) + 17;
//            Record.setReturnRecord((byte) 0x06, 2);//记录类型
//            int tmpconsumeMOney =0;//standSYC - money;
//            if(IntNowMoney == 4095){
//                NewCardMonthConsume(standMoney);
//                tmpconsumeMOney = standMoney - money;
//            }else{
////                System.out.println("卡内日期"+ke_nei_yu_e_ri_qi+"POS时间"+NewYEYAM+"卡内余年"+(ke_nei_yu_e_ri_qi/18)
////                        +"POS余年"+(NewYEYAM/18)+"卡余月"+(ke_nei_yu_e_ri_qi-(ke_nei_yu_e_ri_qi/18*18))+"POS余月"+(NewYEYAM-NewYEYAM/18*18));
//                if((ke_nei_yu_e_ri_qi/18)<(NewYEYAM/18) ){
//                    NewCardMonthConsume(standMoney);
//                    tmpconsumeMOney =  standMoney-money;
//                }else {
//                    NewCardMonthConsume(IntNowMoney);
//                    tmpconsumeMOney = IntNowMoney - money;
//                }
//            }
//            byte[] ConsumeNY = new byte[4];
//            ConsumeNY[1] = (byte) ((~NewYEYAM >> 4) & 0xff);
//            ConsumeNY[2] = (byte) (((~NewYEYAM << 4) & 0xff) + (tmpconsumeMOney >> 8) & 0xff);
//            ConsumeNY[3] = (byte) ((tmpconsumeMOney) & 0xff);
//            //   System.out.println("40961当前消费的年月钱数" + StringUtils.bytes2HexString(ConsumeNY));
//            int cardYMM = ((F5CV[1] & 0xff) << 16) + ((F5CV[2] & 0xff) << 8) + ((F5CV[3] & 0xff));
//            int POSYMM = ((ConsumeNY[1] & 0xff) << 16) + ((ConsumeNY[2] & 0xff) << 8) + ((ConsumeNY[3] & 0xff));
//            ConsumeMoney = cardYMM - POSYMM;
//         //   System.out.println(cardYMM+"|"+POSYMM+"当前消费的年月钱数" +StringUtils.bytes2HexString(ConsumeNY));
//            CardOpertions.getSingleton(null).setCannotCPUConsume(false);
//        }else if (IntNowMoney > standSYC&&yearType[0] != 0x08) { //卡内余次 大于 新发充值起始日至戒指日的总次数合的处理
//            Record.setReturnRecord((byte) 0x02, 2);//记录类型
//            int tmpconsumeMOney =0;//standSYC - money;
//            if(IntNowMoney == 4095){
//                NewCardMonthConsume(standMoney);
//                tmpconsumeMOney = standMoney - money;
//            }else{
//                if(ke_nei_yu_e_ri_qi<NewYEYAM ){
//                    NewCardMonthConsume(standMoney);
//                    tmpconsumeMOney =  standMoney-money;
//                }else {
//                    NewCardMonthConsume(IntNowMoney);
//                    tmpconsumeMOney = IntNowMoney - money;
//                }
//            }
//            byte[] ConsumeNY = new byte[4];
//            ConsumeNY[1] = (byte) ((~NewYEYAM >> 4) & 0xff);
//            ConsumeNY[2] = (byte) (((~NewYEYAM << 4) & 0xff) + (tmpconsumeMOney >> 8) & 0xff);
//            ConsumeNY[3] = (byte) ((tmpconsumeMOney) & 0xff);
//            //   System.out.println("40961当前消费的年月钱数" + StringUtils.bytes2HexString(ConsumeNY));
//            int cardYMM = ((F5CV[1] & 0xff) << 16) + ((F5CV[2] & 0xff) << 8) + ((F5CV[3] & 0xff));
//            int POSYMM = ((ConsumeNY[1] & 0xff) << 16) + ((ConsumeNY[2] & 0xff) << 8) + ((ConsumeNY[3] & 0xff));
//            ConsumeMoney = cardYMM - POSYMM;
//            CardOpertions.getSingleton(null).setCannotCPUConsume(false);
//            //        System.out.println(StringUtils.bytes2HexString(F5CV) + "1进入钱数复制" + cardYMM + ">>" + POSYMM);
//        }else if (IntNowMoney > standSYC - standMoney && IntNowMoney <= standSYC&&yearType[0] != 0x08) {//这个是最后一个月的次数
//            Record.setReturnRecord((byte) 0x02, 2);//记录类型
//            int tmpconsumeMOney = 0;
//            if(IntNowMoney == 4095){
//                NewCardMonthConsume(standMoney);
//                tmpconsumeMOney = standMoney - money;
//            }else{
//                if(ke_nei_yu_e_ri_qi<NewYEYAM ){
//                    NewCardMonthConsume(standMoney);
//                    tmpconsumeMOney =  standMoney-money;
//                }else {
//                    NewCardMonthConsume(IntNowMoney);
//                    tmpconsumeMOney = IntNowMoney - money;
//                }
//            }
//            byte[] ConsumeNY = new byte[4];
//            ConsumeNY[1] = (byte) ((~NewYEYAM >> 4) & 0xff);
//            ConsumeNY[2] = (byte) (((~NewYEYAM << 4) & 0xff) + (tmpconsumeMOney >> 8) & 0xff);
//            ConsumeNY[3] = (byte) ((tmpconsumeMOney) & 0xff);
//            //    System.out.println("40962当前消费的年月钱数" + StringUtils.bytes2HexString(ConsumeNY));
//            int cardYMM = ((F5CV[1] & 0xff) << 16) + ((F5CV[2] & 0xff) << 8) + ((F5CV[3] & 0xff));
//            int POSYMM = ((ConsumeNY[1] & 0xff) << 16) + ((ConsumeNY[2] & 0xff) << 8) + ((ConsumeNY[3] & 0xff));
//            //   System.out.println(StringUtils.bytes2HexString(F5CV) + "2进入钱数复制" + cardYMM + ">>" + POSYMM);
//            ConsumeMoney = cardYMM - POSYMM;
//            CardOpertions.getSingleton(null).setCannotCPUConsume(false);
//        } else if (IntNowMoney < standSYC -standMoney && IntNowMoney >= money&&yearType[0] != 0x08) {//这个是最后一个月的次数
//            Record.setReturnRecord((byte) 0x02, 2);//记录类型
//            int tmpconsumeMOney = 0;
//            if(IntNowMoney == 4095){
//                NewCardMonthConsume(standMoney);
//                tmpconsumeMOney = standMoney - money;
//            }else{
//                if(ke_nei_yu_e_ri_qi<NewYEYAM ){
//                    NewCardMonthConsume(standMoney);
//                    tmpconsumeMOney =  standMoney-money;
//                }else {
//                    NewCardMonthConsume(IntNowMoney);
//                    tmpconsumeMOney = IntNowMoney - money;
//                }
//            }
//            byte[] ConsumeNY = new byte[4];
//            ConsumeNY[1] = (byte) ((~NewYEYAM >> 4) & 0xff);
//            ConsumeNY[2] = (byte) (((~NewYEYAM << 4) & 0xff) + (tmpconsumeMOney >> 8) & 0xff);
//            ConsumeNY[3] = (byte) ((tmpconsumeMOney) & 0xff);
//            int cardYMM = ((F5CV[1] & 0xff) << 16) + ((F5CV[2] & 0xff) << 8) + ((F5CV[3] & 0xff));
//            int POSYMM = ((ConsumeNY[1] & 0xff) << 16) + ((ConsumeNY[2] & 0xff) << 8) + ((ConsumeNY[3] & 0xff));
//            ConsumeMoney = cardYMM - POSYMM;
//            CardOpertions.getSingleton(null).setCannotCPUConsume(false);
//        } else if(yearType[0] != 0x08){
//            Record.setReturnRecord((byte) 0x02, 2);//记录类型
//            CardOpertions.getSingleton(null).setCannotCPUConsume(true);
//        }
//       return ConsumeMoney;
//    }
//
//    public static void NewCardMonthConsume(int standMoney){
//     //  System.out.print("消费前原额"+standMoney);
//        byte [] standnum=new byte[4];
//        standnum[0]= (byte) ((standMoney>>32)&0x00);
//        standnum[1]= (byte) ((standMoney>>16)&0xff);
//        standnum[2]= (byte) ((standMoney>>8)&0xff);
//        standnum[3]= (byte) ((standMoney)&0xff);
//    //    System.out.println("消费钱原额"+StringUtils.bytes2HexString(standnum));
//        Record.ContiSetRecord(standnum, 35, 4, 0);
//    }
//
//    public static void NewCardMonthConsumeByteHex(byte [] standMoney){
//        //  System.out.print("消费前原额"+standMoney);
//    //    System.out.println("消费钱原额"+StringUtils.bytes2HexString(standMoney));
//        Record.ContiSetRecord(standMoney, 35, 4, 0);
//    }
//    /**写消费记录的函数--截取第9块的起始4字节逆序为顺序余额*/
//    public static String reWMoney(String HexMoney) {
//        //  String TmpHexMoney=Integer.toHexString( Integer.parseInt(HexMoney,16));
//      //  System.out.println("M1卡余额度" + HexMoney);
//        String TmpHexMoney = HexMoney;
//        String TmpVal = "";
//        for (int i = 0; i < TmpHexMoney.length() / 2; i++) {
//            TmpVal += TmpHexMoney.substring(TmpHexMoney.length() - ((i * 2) + 2), TmpHexMoney.length() - i * 2);
//        }
//        return TmpVal;
//    }
//    /**写消费记录的函数--传入的金额变成消费金额写入消费记录*/
//    public static String wRecordPayMoney(String Money){
//        byte [] wpm=StringUtils.hex2bytes(Money);
//        byte [] tmpRearr=new byte[3];
//        for(int i=0;i<wpm.length;i++){
//            tmpRearr[tmpRearr.length-(i+1)]=wpm[wpm.length-(i+1)];
//        }
//        //System.out.println("消费金额"+StringUtils.bytes2HexString(tmpRearr));
//        return StringUtils.bytes2HexString(tmpRearr);
//    }
//
//    public static String wRecordTime(String wRecordTime) throws ParseException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        long millionSeconds = dateFormat.parse("19700101000000").getTime();//毫秒
//        long NowTime = dateFormat.parse(wRecordTime).getTime() - millionSeconds;
//        String wRecordTimes=Long.toHexString(NowTime / 1000);
//        return wRecordTimes;
//    }
}
