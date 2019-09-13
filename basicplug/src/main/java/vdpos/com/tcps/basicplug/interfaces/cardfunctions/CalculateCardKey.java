package vdpos.com.tcps.basicplug.interfaces.cardfunctions;

import com.landicorp.android.eptapi.exception.RequestException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import vdpos.com.tcps.basicplug.implementation.priexcption.mifare.MifareCard;
import vdpos.com.tcps.basicplug.implementation.fixed.DeskeyInfo;
import vdpos.com.tcps.basicplug.implementation.priexcption.LogicException;

public interface CalculateCardKey {
     byte[] zero_secort={(byte) 0xA0, (byte) 0xA1, (byte) 0xA2, (byte) 0xA3, (byte) 0xA4, (byte) 0xA5};

    /**计算M1卡全部密钥*/
    void CalculateCardKeyAll(byte[] TK, DeskeyInfo desinf,MifareCard mifareCard) throws LogicException;

    /**
     * 根据扇区ID 计算扇区密钥
     * 并返回以byte数组形式返回的密钥串
     */
    byte[] CalculateCardKey(byte sectorNo, int bufferLen) throws RequestException;

    /**
     * 计算其它扇区的根密钥
     * @return byte[] 自发密钥KEY
     * 只计算出加密参
     */
  void CalculatBasisKey(MifareCard mifareCard) throws RequestException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, ClassNotFoundException, LogicException;



}
