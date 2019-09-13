package vdpos.com.tcps.basicplug.util.payutil;

import com.landicorp.android.eptapi.exception.RequestException;

import java.io.IOException;
import java.text.ParseException;

import vdpos.com.tcps.basicplug.implementation.priexcption.mifare.CalculateCardKeyFun;
import vdpos.com.tcps.basicplug.implementation.priexcption.LogicException;

public class PayProcess {
    protected CalculateCardKeyFun calcul = new CalculateCardKeyFun(); //计算

    public PayProcess() {

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
     *
     * @param payMoney 消费的金额 例如消费150分 = 1.5元 151分= 1.51元
     * @author Xin Zhao 2019-08-28 13:52
     * vdpos.com.tcps.basicplug.util.payutil.PayProcess.RestorePayStatus()
     */
    protected void payWallet(double payMoney) throws RequestException, LogicException, ParseException, IOException, ClassNotFoundException {

    }

    /**
     * @return 卡消费状态是否正常
     */
    protected boolean RestorePayStatus() throws RequestException, IOException, ClassNotFoundException {
        return false;
    }

    /**
     * 消费月票 M1
     * 消费步骤
     * 1.验证扇区:
     *
     * @param payNum 消费的次数 1=1次 2=2次 不可为0次
     */
    protected void payMiMonth(int payNum) {

    }


}
