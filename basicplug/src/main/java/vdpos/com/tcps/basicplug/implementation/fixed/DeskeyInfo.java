package vdpos.com.tcps.basicplug.implementation.fixed;

import java.io.Serializable;

public class DeskeyInfo implements Serializable {
    private byte[] TkeyVal;

    public DeskeyInfo(byte[] TkeyV) {
        this.TkeyVal = TkeyV;
    }

    public byte[] GetTkeyVal() {
        return TkeyVal;
    }

    public boolean IsTkeyEmpty() {
        return (null == TkeyVal ? true : false);
    }

}
