package vdpos.com.tcps.basicplug.implementation.priexcption.mifare;

import com.landicorp.android.eptapi.card.MifareDriver;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.BytesBuffer;
import com.landicorp.android.eptapi.utils.BytesUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import vdpos.com.tcps.basicplug.basis.extend.PosScan;
import vdpos.com.tcps.basicplug.implementation.fixed.DeskeyInfo;
import vdpos.com.tcps.basicplug.implementation.fixed.DeskeyOpertion;
import vdpos.com.tcps.basicplug.implementation.priexcption.LogicException;
import vdpos.com.tcps.basicplug.interfaces.cardfunctions.CalculateCardKey;
import vdpos.com.tcps.basicplug.util.error.status.ExcuteStatus;
import vdpos.com.tcps.basicplug.util.security.Des3_ecb;

public class CalculateCardKeyFun implements CalculateCardKey {


    /**
     * 计算全部扇区密钥并读取卡片内基础的不变化信息 用于后续去快速读取
     *
     * @param TK desinf 错误序列号:ck6-14
     */
    @Override
    public void CalculateCardKeyAll(byte[] TK, DeskeyInfo desinf, MifareCard mifareCard) throws LogicException {
        try {
            if (null == desinf.GetTkeyVal()) {
                PosScan.findCardCB.FindCardException("ck6:请刷密钥卡", ExcuteStatus.FAILED_KEYOP.getStatus());
            }
            mifareCard.setKeyStorge((byte) 1, ByteBuffer.allocate(6).put(mifareCard.getCsnNo()).
                    put(mifareCard.getCsnNo()[0]).put(mifareCard.getCsnNo()[1]));//保存1扇区内容
            for (int i = 2; i < mifareCard.getSectorID().length; i++) {
                ByteBuffer bb = ByteBuffer.allocate(TK.length + 1);
                bb.put(TK);
                bb.put(mifareCard.getSectorID()[i]);
                String AllKEY = Des3_ecb.ECB_TripENDESencrypt(BytesUtil.bytes2HexString(desinf.GetTkeyVal()), BytesUtil.bytes2HexString(bb.array()));
                mifareCard.setKeyStorge((byte) (i & 0xff), ByteBuffer.allocate(6).put(BytesUtil.hexString2Bytes(AllKEY.substring(0, 12))));
            }
//            for (byte key : mifareCard.getKeyStorge().keySet()) {
//                System.out.print("KEY扇区:" + key);
//                System.out.println("密钥值:" + BytesUtil.bytes2HexString(mifareCard.getKeyStorge().get(key).array()));
//            }
            if (0 == validationSecKey((byte) 0x02, mifareCard, mifareCard.getKeyStorge().get((byte) 0x02).array())) {//2sector 9block
                mifareCard.setWallet_money(mifareCard.readCardInfo(9));//设置历史钱包值
                mifareCard.setWallet_moneyBK(mifareCard.readCardInfo(10));//设置历史钱包值
            } else {
                mifareCard.setActivity(false);
                throw new LogicException("ck12:Validation Failed.");
            }
            if (0 == validationSecKey((byte) 0x06, mifareCard, mifareCard.getKeyStorge().get((byte) 0x06).array())) {//6sector 26block
                mifareCard.setTwentySixBlockAttr(mifareCard.readCardInfo(26));//6sector 26block
            } else {
                mifareCard.setActivity(false);
                throw new LogicException("ck13:Validation Failed.");
            }
            if (0 == validationSecKey((byte) 0x07, mifareCard, mifareCard.getKeyStorge().get((byte) 0x07).array())) {//7sector 28 29block
                mifareCard.setTwentyEightBlockAttr(mifareCard.readCardInfo(28));
                mifareCard.setMonth_num(mifareCard.readCardInfo(29));//设置月票次数
            } else {
                mifareCard.setActivity(false);
                throw new LogicException("ck14:Validation Failed.");
            }
        } catch (InvalidKeyException e) {
            PosScan.findCardCB.FindCardException("ck6:" + e.getMessage(), ExcuteStatus.INVALID_KEY_ERROR.getStatus());
        } catch (BadPaddingException e) {
            PosScan.findCardCB.FindCardException("ck7:" + e.getMessage(), ExcuteStatus.NUPOINT_EXCEPT.getStatus());
        } catch (IllegalBlockSizeException e) {
            PosScan.findCardCB.FindCardException("ck8:" + e.getMessage(), ExcuteStatus.NUPOINT_EXCEPT.getStatus());
        } catch (NoSuchPaddingException e) {
            PosScan.findCardCB.FindCardException("ck9:" + e.getMessage(), ExcuteStatus.NUPOINT_EXCEPT.getStatus());
        } catch (NoSuchAlgorithmException e) {
            PosScan.findCardCB.FindCardException("ck10:" + e.getMessage(), ExcuteStatus.NUPOINT_EXCEPT.getStatus());
        } catch (RequestException e) {
            PosScan.findCardCB.FindCardException("ck11:" + e.getMessage(), ExcuteStatus.REQUESTERROR.getStatus());
        }
    }

    /**
     * 根据扇区ID 计算扇区密钥
     * 并返回以byte数组形式返回的密钥串
     *
     * @param sectorNo  计算的扇区
     * @param bufferLen
     */
    @Override
    public byte[] CalculateCardKey(byte sectorNo, int bufferLen) throws RequestException {
        return new byte[0];
    }

    /**
     * 获取0扇区 第一块的所有扇区的分散值
     * 错误序列号 ck1-ck3
     */
    protected void setKeyBasis(MifareCard mifareCard) throws LogicException {
        try {
            int auth = mifareCard.getM1driver().authSector(0, MifareDriver.KEY_A, CalculateCardKey.zero_secort);
            if (0 == auth) {
                BytesBuffer buffer = new BytesBuffer();
                mifareCard.getM1driver().readBlock(1, buffer);
                mifareCard.setSectorID(buffer.getData());
            } else {
                mifareCard.setActivity(false);
                throw new LogicException("ck1:Validation Failed.");
            }
        } catch (NullPointerException e) {
            mifareCard.setActivity(false);
            PosScan.findCardCB.FindCardException("ck2:" + e.getMessage(), ExcuteStatus.NUPOINT_EXCEPT.getStatus());
        } catch (RequestException e) {
            mifareCard.setActivity(false);
            PosScan.findCardCB.FindCardException("ck3:" + e.getMessage(), ExcuteStatus.REQUESTERROR.getStatus());
        }
    }

    /**
     * 计算其它扇区的根密钥
     * <p>
     * 计算0扇区 0xA0, (byte) 0xA1, (byte) 0xA2, (byte) 0xA3, (byte) 0xA4, (byte) 0xA5 KEYA
     * 如果以后寻卡不返回芯片号可以通过此方式进行获取
     * 有调用CalculateCardKeyAll函数 对所有扇区进行密钥计算
     * 考虑到为自身操作 取消了入参的模式
     * 错误序列号;ck4-5
     */
    @Override
    public void CalculatBasisKey(MifareCard mifareCard) throws RequestException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, IOException, ClassNotFoundException, LogicException {
        DeskeyOpertion desk_op = new DeskeyOpertion();
        //调用CalculateCardKeyFun类内的vailationBasicKey函数对1扇区进行验证
        int auth = validationBasicKey((byte) 0x01, mifareCard, (mifareCard.getCsnNo().length + 2)); //验证1
        if (0 == auth) {//判断验证是否成功
            byte[] block_four = mifareCard.readCardInfo(4);
            byte[] block_six = mifareCard.readCardInfo(6);//第六块
            mifareCard.setFourBlockAttr(block_four);//将第四块所有值进行属性存储
            if (0x93 == mifareCard.getBusinessType()) {//密钥卡的设置
                System.out.println("密钥卡第4块数据" + BytesUtil.bytes2HexString(block_four));
                ConsumeRecordMi crm = new ConsumeRecordMi();//消费记录 消费城市代码
                crm.setPayCityCode(block_four);//根据M1密钥卡 设置消费城市代码
                calculate_FixeddesKey(mifareCard, block_six);
                crm.saveRecord(DeskeyOpertion.context.getFilesDir().getPath());//记录保存

            } else {
                mifareCard.setBlockSixAttr(block_six);
                DeskeyInfo desinf = desk_op.getFixed_Deskey();
                if (true == desinf.IsTkeyEmpty()) {
                    mifareCard.setActivity(false);
                    PosScan.findCardCB.FindCardException(desinf.IsTkeyEmpty() + "ck5:需要密钥卡前置设置", ExcuteStatus.ERROR_ERRTYPE.getStatus());
                } else {
                    //TK结构 CSNO + 卡流水号后两字节 + 卡认证码首字节 + 扇区分散号
                    mifareCard.setFiveBlockAttr(mifareCard.readCardInfo(5));//设置第5块值属性
                    ByteBuffer TK = ByteBuffer.allocate(mifareCard.getCsnNo().length + 3);
                    TK.put(mifareCard.getCsnNo());
                    TK.put(block_four[6]);
                    TK.put(block_four[7]);
                    TK.put(block_four[8]);
                    CalculateCardKeyAll(TK.array(), desinf, mifareCard);//获取全部密钥
                }
            }
        } else { //InvalidKey
            mifareCard.setActivity(false);
            throw new LogicException("ck4:Validation Error.");
        }
    }

    /**
     * @return 验证的值
     */
    public int validationBasicKey(byte sectorNo, MifareCard M1driver, int bufferLen) throws RequestException {
        ByteBuffer csnkey = ByteBuffer.allocate(bufferLen);
        csnkey.put(M1driver.getCsnNo());
        csnkey.put(new byte[]{M1driver.getCsnNo()[0]});
        csnkey.put(new byte[]{M1driver.getCsnNo()[1]});
        csnkey.flip(); //至此先计算扇区的密钥值
        //  System.out.println("验证M1卡基础扇区密钥KEYA:" + BytesUtil.bytes2HexString(csnkey.array()));
        return M1driver.getM1driver().authSector(sectorNo, MifareDriver.KEY_A, csnkey.array());
    }

    /**
     * 自定义扇区验证
     *
     * @return int 0 成功 其他失败
     */
    private int validationSecKey(byte sectorNo, MifareCard m1driver, byte[] keyarr) throws RequestException {
//        System.out.println("验证的扇区" + (sectorNo & 0xff) + "验证的key:::::" + BytesUtil.bytes2HexString(keyarr));
        return m1driver.getM1driver().authSector(sectorNo, MifareDriver.KEY_A, keyarr);
    }

    public int validationSecKey(byte sectorNo, MifareCard m1driver) throws RequestException {
//        System.out.println("验证的扇区" + (sectorNo & 0xff) + "验证的key:::::" + BytesUtil.bytes2HexString(m1driver.getKeyStorge().get(sectorNo).array()));
        return m1driver.getM1driver().authSector(sectorNo, MifareDriver.KEY_A, m1driver.getKeyStorge().get(sectorNo).array());
    }

    /**
     * 通过第6块值进行解码操作进行逆操作
     *
     * @return 返回计算后用于计算其它扇区密钥的 根DES密钥
     */
    public byte[] calculate_FixeddesKey(MifareCard M1driver, byte[] sixblock) throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        System.out.println("获取的第6块值" + sixblock);
        ByteBuffer des_key = ByteBuffer.allocate(M1driver.getCsnNo().length * 2);
        des_key.put(M1driver.getCsnNo());
        des_key.put(M1driver.getCsnNo());
        String AllKEY = Des3_ecb.ECB_TripDEDESencrypt(BytesUtil.bytes2HexString(des_key.array()), BytesUtil.bytes2HexString(sixblock));
        DeskeyOpertion desk_op = new DeskeyOpertion();
        DeskeyInfo desinf = new DeskeyInfo(BytesUtil.hexString2Bytes(AllKEY));
        desk_op.Fixed_Deskey(desinf);
        return BytesUtil.hexString2Bytes(AllKEY);
    }
}
