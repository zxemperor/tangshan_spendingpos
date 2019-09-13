package vdpos.com.tcps.tangshan_spendingpos;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import vdpos.com.tcps.basicplug.basis.abstracts.CardBasis;
import vdpos.com.tcps.basicplug.basis.extend.PosScan;
import vdpos.com.tcps.basicplug.basis.extend.PsamOpertion;
import vdpos.com.tcps.basicplug.implementation.priexcption.mifare.MifareCard;
import vdpos.com.tcps.basicplug.util.external.ExternalSdk;
import vdpos.com.tcps.basicplug.util.error.status.ExcuteStatus;

public class MainActivity extends AppCompatActivity {
//    PsamOpertion poi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //step1 启动注册全局服务
        if (0 == ExternalSdk.BindPosDervice(this)) {
            //获取PSAM卡号
//            poi = PsamOpertion.getInstance();
            System.out.println("卡号" + PsamOpertion.getInstance().getPsamCardNo(ExcuteStatus.PSAM_TRAFFIC.getStatus()));
            System.out.println("卡号" + PsamOpertion.getInstance().getPsamCardNo(ExcuteStatus.PSAM_LBUILD.getStatus()));
            th.start();
        }
    }
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    Thread th = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    PosScan.findCard(new PosScan.FindCardCallBack() {
                        @Override
                        public void getBasisCardObj(CardBasis cardBasis) {
                            System.out.println("卡片的类型" + cardBasis.getCardModel());
                            /**获取卡标识名称 0xC1 = M1卡*/
                            if (ExcuteStatus.MI_TYPE.getStatus() == cardBasis.getCardType()) {
                                MifareCard micard = (MifareCard) cardBasis;
                                if (0x93 == micard.getBusinessType()) {//为0x93密钥卡类型 需要获取其它卡密钥分散值 Sa-b = f(x)ab;
                                    System.out.println("密钥卡不做暴露操作,私有化处理。");
                                } else {
                                    System.out.println("获取了M1卡的实例");
                                    //          micard.getCardBasicInfoMap();
                                    micard.getCardBasicInfoStr();
                                    //    micard.getHisWall_money();
//                                            micard.getHisMonth_num();
                                    micard.Consume("0.01");
                                    /**消费*/
                                }

                            } else if (ExcuteStatus.CPU_TYPE.getStatus() == cardBasis.getCardType()) {
                                System.out.println("获取了CPU卡实例");
                            }
                        }

                        @Override
                        public void FindCardException(String ExMsg, int stepId) {
                            System.out.println("寻卡异常ID" + stepId + ExMsg);
                        }
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
}
