package vdpos.com.tcps.basicplug.basis.extend;

import java.util.HashMap;
import java.util.AbstractMap;

import com.landicorp.android.eptapi.utils.BytesUtil;

import vdpos.com.tcps.basicplug.implementation.priexcption.psam.PsamEntity;
import vdpos.com.tcps.basicplug.util.error.status.PsamAPDUS;
import vdpos.com.tcps.basicplug.util.error.status.ExcuteStatus;

public class PsamOpertion {
    private static class Holder { //静态内部类
        /**
         * 程序一旦被系统加载 则实例化PsamOpertion实例
         * ，用于在饿汉模式的单例模式:
         * 由于在加载时进行创建提高程序运行效率，
         * 而懒汉模式则在需要时创建造成首次加载程序变慢。
         */
        private static final PsamOpertion INSTANCE = new PsamOpertion();
    }

    /**
     * 存储PSAM实例后的对象：
     * 存放PSAM卡的对象 该对象内部有卡槽等私有的属性
     */
    private AbstractMap<Integer, PsamEntity> PsamCollection = new HashMap<>();//对象

    /**
     * 单例化操作类
     * 私有构造 无参类型
     */
    private PsamOpertion() {
    }

    /**
     * 返回单例的实例对象
     * 对外使用
     */
    public static PsamOpertion getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 分类查询业务类型并初始化psam卡实例
     * 确定当前对象的业务类型 254是交通部 255是住建部
     * 状态定义类 error.status.ExcuteStatus
     * SLOT_IDONE(1) ExcuteStatus.SLOT_IDTWO.getStatus(),
     * SLOT_IDTWO(2) ExcuteStatus.SLOT_IDONE.getStatus(),
     * PSAM_TRAFFIC((byte)0xFE), PSAM_LBUILD((byte)0xFF);
     */
    public void Select_Businees() {
        PsamEntity psamSlotF = new PsamEntity(ExcuteStatus.SLOT_IDTWO.getStatus()); //传入当前POS支持最大卡槽ID
        PsamEntity psamSlotL = new PsamEntity(ExcuteStatus.SLOT_IDONE.getStatus()); //传入当前POS支持最大卡槽ID
        if (null != psamSlotF) { //判断Slot1卡槽内是否为空置卡
            psamSlotF.ReadPsamCard(); //读取卡号
            psamSlotF.SelectBusiness(); //确认业务类型
            PsamCollection.put((psamSlotF.getBusiness()), psamSlotF); //将Slot1卡实例 put到持久化集合内
        }
        if (null != psamSlotL) { //判断Slot2卡槽内是否为空置卡
            psamSlotL.ReadPsamCard(); //读取卡号
            psamSlotL.SelectBusiness(); //确认业务类型
            PsamCollection.put((psamSlotL.getBusiness()), psamSlotL); //将Slot2卡实例 put到持久化集合内
        }
    }

    /**
     * 获取对应业务的PSAM 卡号
     * 通过传入 error.status.ExcuteStatus
     * 参数Business ->>PSAM_TRAFFIC((byte)0xFE), PSAM_LBUILD((byte)0xFF);
     * 来获取对应的卡槽内的卡号并返回字符串
     */
    public String getPsamCardNo(int Business) {
        System.out.println("获取集合的长度" + PsamCollection.size() + "KEY:" + Business);
        PsamEntity psamSlotInit = PsamCollection.get(Business);
        //    System.out.println("Activity获取的PSAM卡号" + PsamCollection.get(Business).getPsamNo());
        if (null != psamSlotInit) {
            return psamSlotInit.getPsamNo();
        }
        return BytesUtil.bytes2HexString(PsamAPDUS.PsamError.PSAM_EXCEPTION);
    }
}
