package vdpos.com.tcps.basicplug.basis.callback;

import vdpos.com.tcps.basicplug.basis.abstracts.CardBasis;

interface FindCardCallBack {
    /**
     * 根据读卡内容返回寻卡对象
     */
     void getBasisCardObj(CardBasis cardBasis);
}
