package vdpos.com.tcps.basicplug.util.external;

import android.content.Context;
import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;
import vdpos.com.tcps.basicplug.basis.extend.PsamOpertion;
import vdpos.com.tcps.basicplug.implementation.fixed.DeskeyOpertion;

/**
 * @author zhaoxin
 * */
public class ExternalSdk {
    /**
     * 绑定POS机的金融服务
     * @param context 执行该函数的Activity实例
     * @return int 类型值
     * <p>
     * <br> 0: 成功     -1: 服务占用异常 </br>
     * <br>-2: 重复登陆 -3: 权限拒绝处理 </br>
     * -4: 请求失败
     * </p>
     * */
    public static int BindPosDervice(Context context){
        try {
            DeviceService.login(context);
            DeskeyOpertion.context = context;
        } catch (ServiceOccupiedException e) {//有服务占用 未被释放
            e.printStackTrace();
            return -1;
        } catch (ReloginException e) {//程序开发错误,属于程序异常
            e.printStackTrace();
            return -2;
        } catch (UnsupportMultiProcess unsupportMultiProcess) {
            unsupportMultiProcess.printStackTrace();
            return -3;
        } catch (RequestException e) {
            e.printStackTrace();
            return -4;
        }
        PsamOpertion.getInstance().Select_Businees();//对PSAM 做上电与初始化
        return 0;
    }//InitPosDervice end

    /**
     *解除金融服务的注册功能
     * 程序退出前调用
     */
    public static void unBindPosDervice(){
        DeviceService.logout();
    }//unBindPosDervice End
}
