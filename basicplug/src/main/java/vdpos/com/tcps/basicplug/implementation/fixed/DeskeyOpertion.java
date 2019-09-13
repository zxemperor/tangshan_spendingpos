package vdpos.com.tcps.basicplug.implementation.fixed;

import android.content.Context;

import com.landicorp.android.eptapi.utils.BytesUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 */
public class DeskeyOpertion {
    public static Context context=null;


    /**
     * 保存Tkey操作
     *
     * @param IDeskey
     * @throws IOException
     */
    public void Fixed_Deskey(DeskeyInfo IDeskey) throws IOException {
        File fileObj = new File(context.getFilesDir().getPath().toString()+"Deskey.fixed");
        ObjectOutputStream file_io = new ObjectOutputStream(new FileOutputStream(fileObj));
        file_io.writeObject(IDeskey);
        file_io.close();
    }

    /**
     * @return Tkey实例
     * @throws IOException
     */
    public DeskeyInfo getFixed_Deskey() throws IOException, ClassNotFoundException {
        File fileObj = new File(context.getFilesDir().getPath().toString()+"Deskey.fixed");
        ObjectInputStream file_io = new ObjectInputStream(new FileInputStream(fileObj));
        DeskeyInfo deskeyObj = (DeskeyInfo) file_io.readObject();
        System.out.println("固化key：："+ BytesUtil.bytes2HexString(deskeyObj.GetTkeyVal()));
        file_io.close();
        return deskeyObj;
    }

}
