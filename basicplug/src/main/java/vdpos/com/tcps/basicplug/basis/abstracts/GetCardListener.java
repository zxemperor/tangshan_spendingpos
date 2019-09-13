package vdpos.com.tcps.basicplug.basis.abstracts;

import vdpos.com.tcps.basicplug.interfaces.external.CustomGetCardListener;

public abstract class GetCardListener implements CustomGetCardListener {
    public GetCardListener() {
    }

    /**
     * 功能接口 通过寻卡后返回操作卡实例
     */
    public abstract void getFindCardInit(Object CardInit);

    /**
     * 返回寻卡中发生的错误ID
     */
    public abstract void FindCardError(int ErrorID);

}
