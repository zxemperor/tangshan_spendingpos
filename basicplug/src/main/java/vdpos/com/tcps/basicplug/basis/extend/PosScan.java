package vdpos.com.tcps.basicplug.basis.extend;

import com.landicorp.android.eptapi.card.CpuCardDriver;
import com.landicorp.android.eptapi.card.RFDriver;
import com.landicorp.android.eptapi.device.RFCardReader;
import com.landicorp.android.eptapi.device.InsertCardReader;
import com.landicorp.android.eptapi.exception.RequestException;

import vdpos.com.tcps.basicplug.basis.abstracts.CardBasis;
import vdpos.com.tcps.basicplug.basis.abstracts.GetCardListener;
import vdpos.com.tcps.basicplug.implementation.priexcption.mifare.MifareCard;
import vdpos.com.tcps.basicplug.interfaces.cardconstantsinter.Constants;
import vdpos.com.tcps.basicplug.util.error.status.ExcuteStatus;

/**
 * 该函数集成了POS扫描的全部功能，进行高内聚集合。
 * 使代码呈现高可用性，待解决时间复杂度与空间复杂度操作
 * 应集成注操作
 *
 * @author zhaoxin zx_emails@126.com 2019-7-16
 */
public class PosScan {
    /**
     * <p>
     * 使RF天线模式进行扫描，并侦测卡后的回调函数。
     * 激活
     * 集成于RFCardRead类
     *
     * @see com.landicorp.android.eptapi.device.RFCardReader
     * </p>
     */

    private static String CardModel = null;

    public static FindCardCallBack findCardCB = null;

    /**
     * M1卡实例
     */
    static MifareCard MiCardObj = null;

    public static MifareCard getMiCard() {
        return MiCardObj;
    }

    static CpuCardDriver Cpudriver = null;
    /**
     * -----------------寻卡操作监听-------------------- step1
     * 寻卡操作监听事件 第一步(对非接触 与 接触卡 的操作第一步 (排除PSAM卡))
     * 根据onCardPass(int CardType)该函数中的CardType来判断以下几种类型卡
     * Mifare: S50 S50_PRO S70 S70_PRO (俗称M1卡)
     * CPU: CPU_CARD
     * 特殊卡种类:PRO_CARD 暂未涉及
     * onCrash() 系统级别崩坏
     * onFail(int i) 返回寻卡错误的信息ID 进行特殊判断
     * 外部关键字 索引 step2---onActiveListener
     */
    private static RFCardReader.OnSearchListener onSearchListener = new RFCardReader.OnSearchListener() {  //寻卡回调监听
        @Override
        public void onCrash() {
            System.out.println("服务崩坏");
            try {
                RFCardReader.getInstance().stopSearch();
            } catch (RequestException e) {
                //e.printStackTrace();
                findCardCB.FindCardException("fc4:" + e.getMessage(), ExcuteStatus.REQUESTERROR.getStatus());
            }
            findCardCB.FindCardException("fc5:服务崩坏", ExcuteStatus.ON_CRASH.getStatus());
        }

        @Override
        public void onCardPass(int CardType) {
            CardModel = null;
            switch (CardType) {
                case S50_CARD:
                case S50_PRO_CARD:
                    CardModel = Constants.RFCard.DRIVER_NAME_S50;
                    System.out.println("S50 or S50PRO");
                    break;
                case S70_CARD:
                case S70_PRO_CARD:
                    CardModel = Constants.RFCard.DRIVER_NAME_S70;
                    System.out.println("S70 or S70PRO");
                    break;
                case PRO_CARD:
                    CardModel = Constants.RFCard.DRIVER_NAME_PRO;
                    System.out.println("PRO_CARD");
                    break;
                case CPU_CARD:
                    CardModel = Constants.RFCard.DRIVER_NAME_CPU;
                    System.out.println("CPU_CARD");
                    break;
            }
            try {
                if (null != CardModel && true != "".equals(CardModel)) {
                    RFCardReader.getInstance().activate(CardModel, onActiveListener);//通过获取RFCardReader单例实例 传入卡模型对卡进行激活 step2
                }
            } catch (RequestException e) {
                findCardCB.FindCardException("fc6:" + e.getMessage(), ExcuteStatus.REQUESTERROR.getStatus());
              //  System.out.println("寻卡错误信息" + e.getMessage());
            }
        }

        @Override
        public void onFail(int i) {
            findCardCB.FindCardException("fc7:寻卡失败" + i, ExcuteStatus.ERROR_FAILED.getStatus());
            System.out.println("寻卡失败" + i);
        }
    };

    /**
     * ---------寻卡操作监听服务函数--------- 第一步的被服务函数 step 1.1
     * 服务类只针对非接触目前未出现公交行业的接触乘车卡
     * 由 onSearchListener 该寻卡监听实例为此函数提供监听服务
     * <p>
     * 目前尚未决定是否启用返回值操作
     * <p/>
     */
    public static void findCard() {
        try {
            RFCardReader.getInstance().searchCard(onSearchListener);
        } catch (RequestException e) {
            System.out.println("程序异常");
        }
    }

    /**
     * ---------寻卡操作监听服务函数--------- 第一步的被服务函数 step 1.1
     * 服务类只针对非接触目前未出现公交行业的接触乘车卡
     * 由 onSearchListener 该寻卡监听实例为此函数提供监听服务
     * <p>
     * 目前尚未决定是否启用返回值操作
     * <p/>
     * 参数监听器 FindCardCallBack
     * 外部支持 索引关键字:step1----onSearchListener
     */
    public static void findCard(PosScan.FindCardCallBack fcb) {
        try {
            if (null != findCardCB) {
                synchronized (findCardCB) {
                    findCardCB = null;
                }
            }
            findCardCB = fcb;
            RFCardReader.getInstance().searchCard(onSearchListener);
        } catch (RequestException e) {
            //  RFCardReader.getInstance().stopSearch();//出现异常应当停止寻卡
            findCardCB.FindCardException("fc1:" + e.getMessage(), ExcuteStatus.REQUESTERROR.getStatus());
        } catch (NullPointerException e) {
            //   RFCardReader.getInstance().stopSearch();//出现异常应当停止寻卡
            findCardCB.FindCardException("fc2:" + e.getMessage(), ExcuteStatus.NUPOINT_EXCEPT.getStatus());
        } catch (Exception e) {
            // RFCardReader.getInstance().stopSearch();//出现异常应当停止寻卡
            findCardCB.FindCardException("fc3:" + e.getMessage(), ExcuteStatus.GENER_EXCEPT.getStatus());
        }
    }


    /**
     * 扫描寻卡监听事件
     * parcel 为序列化包裹 容器用来使多进程共享内存对象
     */
    public abstract static class OnFindCardListener extends GetCardListener {
        //   CardBasis cb_cardob = null;

        public void OnFindCardListener() {

        }

        protected void setObjectCard() {

        }
    }

    /**
     * -------------激活监听服务------------- step 2
     * -----------------描述-----------------
     * 由 onSearchListener 函数中调用该监听实例进行承上启下操作,
     * 用来激活寻到的卡准备交互状态。
     * {@see onSearchListener}
     * ---------------------------------------
     * onCrash() 系统级别崩坏
     * onCardActivate(RFDriver rfDriver) 用来激活卡准备状态
     * onActivateError(int i) 激活错误 根据 int i 根据返回值来判断是什么错误
     * onUnsupport(String s) 权限拒绝 错误消息信息 String 具体权限拒绝消息
     */
    private static RFCardReader.OnActiveListener onActiveListener = new RFCardReader.OnActiveListener() {
        @Override
        public void onCrash() {//服务崩坏
            try {
                System.out.println("激活功能系统级别崩坏");
                RFCardReader.getInstance().stopSearch();
                findCardCB.FindCardException("oa1:系统级别崩坏", ExcuteStatus.ON_CRASH.getStatus());
            } catch (RequestException e) {
                findCardCB.FindCardException("oa2:" + e.getMessage(), ExcuteStatus.REQUESTERROR.getStatus());
            }
        }

        @Override
        public void onCardActivate(RFDriver rfDriver) { // 激活成功
            String CardName = rfDriver.getDeviceName();
//            System.out.println("获取的卡名称:" + CardName);
//            System.out.println("卡存在");
            CheckActivateCard(rfDriver);//通过RFDriver对象集合 为卡进行上电唤醒
        }

        @Override
        public void onActivateError(int i) { //激活错误
            System.out.println("激活功能错误" + i);
            try {
                RFCardReader.getInstance().stopSearch();
                findCardCB.FindCardException("oa3:激活错误" + i, ExcuteStatus.ERROR_FAILED.getStatus());
            } catch (RequestException e) {
                findCardCB.FindCardException("oa4:" + e.getMessage(), ExcuteStatus.REQUESTERROR.getStatus());
            }
        }

        @Override
        public void onUnsupport(String s) { //权限拒绝
            System.out.println("激活功能权限问题" + s);
            try {
                RFCardReader.getInstance().stopSearch();
                findCardCB.FindCardException("oa5:" + s, ExcuteStatus.UNSUPPORT_EXCEPT.getStatus());
            } catch (RequestException e) {
                findCardCB.FindCardException("oa6:" + e.getMessage(), ExcuteStatus.REQUESTERROR.getStatus());
            }

        }
    };

    /**
     * 激活卡的活跃状态
     * 目前只配置了M1卡与CPU卡两种卡状态
     * 其中判断M1卡条件为
     * Constants.RFCard.DRIVER_NAME_S50.equals(CardModel)
     * Constants.RFCard.DRIVER_NAME_S70.equals(CardModel)
     * 其中判断CPU卡条件为
     * Constants.RFCard.DRIVER_NAME_CPU.equals(CardModel)
     * Constants.RFCard.DRIVER_NAME_PRO.equals(CardModel)
     * 当前函数包含于的外部函数如下
     * vdpos.com.tcps.basicplug.basis.extend
     * ..com.landicorp.android.eptapi.device.RFCardReader.OnActiveListener onActiveListener
     * ...onCardActivate(RFDriver rfDriver)
     * 改动日期:2019-08-07 13:42
     *
     * @author ZHAOXIN
     */
    private static void CheckActivateCard(RFDriver rfDriver) {

        if (true == Constants.RFCard.DRIVER_NAME_S50.equals(CardModel) ||
                true == Constants.RFCard.DRIVER_NAME_S70.equals(CardModel)) {
            //实例化M1卡
            MiCardObj = new MifareCard(rfDriver, onActiveListener.getLastCardSerialNo(), CardModel);
            System.out.println("M1DerviceName" + CardModel);
            if (MiCardObj.getActivity()) {
                findCardCB.getBasisCardObj(MiCardObj);//返回给调用寻卡的activity的卡片实例
            }
        } else if (Constants.RFCard.DRIVER_NAME_CPU.equals(CardModel) ||
                Constants.RFCard.DRIVER_NAME_PRO.equals(CardModel)) {
            //实例化CPU卡
            System.out.println("CPUDerviceName" + CardModel);
            Cpudriver = (CpuCardDriver) rfDriver;
//            findCardCB.getBasisCardObj(MiCardObj);
        }
    }

    /**
     * 内部接口实现块
     */
    public interface FindCardCallBack {
        /**
         * 根据读卡内容返回寻卡对象
         */
        void getBasisCardObj(CardBasis cardBasis);

        void FindCardException(String ExMsg, int stepId);
    }
/*
----------------------------------------
    以下函数封存用于服务接触卡操作
    封存日期 2019-07-25 14:16
    封存人: zhaoxin
    进度说明: 基础测试通过
    功能说明:用于卡槽读卡 与 磁条刷卡等操作
    后期是否需要完成:根据城市要求是否开启
----------------------------------------
*/
    /**
     * 接触式寻卡
     */
    @Deprecated
    private static InsertCardReader.OnSearchListener InsertSerarchListener = new InsertCardReader.OnSearchListener() {
        @Override
        public void onCardInsert() {//卡槽寻卡成功
            System.out.println("进入卡槽寻卡");
//            System.out.println("读取到卡槽数据:"+getReader().getDriver("CPU").getDeviceName());
//            System.out.println("读取AT"+getReader().getDriver("AT102").getDeviceName());
//            System.out.println("读取SIM4442"+getReader().getDriver("SIM4442").getDeviceName());
        }

        @Override
        public void onFail(int i) {//寻卡失败
            System.out.println("卡槽寻卡失败" + i);
        }

        @Override
        public void onCrash() {//服务崩坏
            System.out.println("服务崩坏");
        }
    };


    @Deprecated
    private void UpElectricity() {
    }

    @Deprecated
    public static int InsertFindCard() {
        System.out.println("卡槽寻卡");
        try {
            InsertCardReader.getInstance().searchCard(InsertSerarchListener);
        } catch (RequestException e) {
            e.printStackTrace();
            System.out.println("卡槽寻卡异常" + e.getMessage());
            return -1;
        }
        return 0;
    }
}
