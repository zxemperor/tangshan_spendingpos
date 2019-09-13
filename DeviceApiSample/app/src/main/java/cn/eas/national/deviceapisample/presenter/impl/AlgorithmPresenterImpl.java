package cn.eas.national.deviceapisample.presenter.impl;

import com.landicorp.android.eptapi.algorithm.Algorithm;
import com.landicorp.android.eptapi.algorithm.RSAPrivateKey;
import com.landicorp.android.eptapi.utils.BytesBuffer;

import cn.eas.national.deviceapisample.activity.interfaces.IDeviceView;
import cn.eas.national.deviceapisample.device.AlgorithmImpl;
import cn.eas.national.deviceapisample.presenter.IAlgorithmPresenter;
import cn.eas.national.deviceapisample.util.ByteUtil;

/**
 * Created by Czl on 2017/7/23.
 */

public class AlgorithmPresenterImpl implements IAlgorithmPresenter{
    private static final byte[] DATA = ByteUtil.hexString2Bytes("11111111111111111111111111111111");
    private static final byte[] KEY = ByteUtil.hexString2Bytes("31313131313131313131313131313131");

    private IDeviceView view;
    private AlgorithmImpl algorithm;

    public AlgorithmPresenterImpl(final IDeviceView deviceView) {
        this.view = deviceView;
        this.algorithm = new AlgorithmImpl() {
            @Override
            protected void onDeviceServiceCrash() {
                view.displayInfo("device service crash");
            }

            @Override
            protected void displayInfo(String info) {
                view.displayInfo(info);
            }

            @Override
            protected void toast(String msg) {
                view.toast(msg);
            }
        };
    }

    @Override
    public void calcData() {
        view.displayInfo("plain data：" + ByteUtil.bytes2HexString(DATA));
        view.displayInfo("plain key：" + ByteUtil.bytes2HexString(KEY));
        view.displayInfo("--------------------------------------");
        // 计算mac
        byte[] result = algorithm.calcMac(Algorithm.EM_alg_MACALGORITHMDEFAULT, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("calculate MAC，default mode(mode 1)：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        view.displayInfo("--------------------------------------");
        // AES算法
        result = algorithm.calcWithAES(Algorithm.EM_alg_AES_CBCMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("AES algorithm CBC mode：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithAES(Algorithm.EM_alg_AES_ECBMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("AES algorithm ECB mode：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithAES(Algorithm.EM_alg_AES_MACMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("AES algorithm MAC mode：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithAES(Algorithm.EM_alg_AES_ENCRYPT, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("AES algorithm encrypt：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithAES(Algorithm.EM_alg_AES_DECRYPT, KEY, result);
        if (result != null && result.length != 0) {
            view.displayInfo("AES algorithm decrypt：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        view.displayInfo("--------------------------------------");
        // TDES算法
        result = algorithm.calcWithTDES(Algorithm.EM_alg_TDESTCBCMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("TDES algorithm CBC mode：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithTDES(Algorithm.EM_alg_TDESTECBMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("TDES algorithm ECB mode：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithTDES(Algorithm.EM_alg_TDESENCRYPT, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("TDES algorithm encrypt：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithTDES(Algorithm.EM_alg_TDESDECRYPT, KEY, result);
        if (result != null && result.length != 0) {
            view.displayInfo("TDES algorithm decrypt：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        view.displayInfo("--------------------------------------");
        // SM4算法
        result = algorithm.calcWithSM4(Algorithm.EM_alg_SMS4ENCRYPT, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4 key encrypt：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithSM4(Algorithm.EM_alg_SMS4DECRYPT, KEY, result);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4 key decrypt：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithSM4(Algorithm.EM_alg_SMS4ENCRYPTMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4 algorithm encrypt：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithSM4(Algorithm.EM_alg_SMS4TCBCMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4 algorithm TCBC mode：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcWithSM4(Algorithm.EM_alg_SMS4TECBMODE, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4 algorithm TECB mode：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        result = algorithm.calcMacWithSM4(Algorithm.EM_alg_MACALGORITHMDEFAULT, KEY, DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM4 specification, calculate MAC：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        view.displayInfo("--------------------------------------");
        // SM3算法
        result = algorithm.sm3(DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SM3 calc hash：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        view.displayInfo("--------------------------------------");
        // SM2算法
        BytesBuffer priKey = new BytesBuffer();
        BytesBuffer pubKey = new BytesBuffer();
        int ret = algorithm.genSm2Keys(32, pubKey, priKey);
        if (ret == 0) {
            view.displayInfo("sm2 public key：" + ByteUtil.bytes2HexString(pubKey.getData()));
            view.displayInfo("sm2 private key：" + ByteUtil.bytes2HexString(priKey.getData()));
            ret = algorithm.sm2SetVersion(Algorithm.EM_alg_SMS2VERDEFAULT);
            view.displayInfo("sm2 set version[default]" + (ret == 0 ? "successful" : "failure"));
            result = algorithm.sm2Encrypt(DATA, pubKey.getData());
            if (result != null && result.length != 0) {
                view.displayInfo("sm2 public key encrypt：");
                view.displayInfo("result：" + ByteUtil.bytes2HexString(result));
                result = algorithm.sm2Decrypt(result, priKey.getData());
                if (result != null && result.length != 0) {
                    view.displayInfo("sm2 private key decrypt：");
                    view.displayInfo("result：" + ByteUtil.bytes2HexString(result));
                }
            }
            byte[] hash = algorithm.sm2SignHash(Algorithm.EM_DEFAULT_SM2_IDA, DATA, pubKey.getData());
            if (hash != null && hash.length != 0) {
                view.displayInfo("sm2 get hash value：");
                view.displayInfo("hash：" + ByteUtil.bytes2HexString(hash));
                result = algorithm.sm2SignEnd(hash, priKey.getData());
                if (result != null && result.length != 0) {
                    view.displayInfo("sm2 sign result：");
                    view.displayInfo("result：" + ByteUtil.bytes2HexString(result));
                    ret = algorithm.sm2VerifyEnd(hash, result, pubKey.getData());
                    view.displayInfo("sm2 sign verify result：" + (ret == 0 ? "successful" : "failure"));
                }
            } else {
                view.displayInfo("sm2 get hash value failed，ret = " + ret);
            }
        } else {
            view.displayInfo("create sm2 public key and private key failed，ret = " + ret);
        }
        view.displayInfo("--------------------------------------");
        // RSA算法
        RSAPrivateKey key = algorithm.generateRsaPrivateKey();
        if (key != null) {
            result = algorithm.calcWithRSAPrivateKey(key, DATA);
            if (result != null && result.length != 0) {
                view.displayInfo("RSA algorithm private key encrypt：");
                view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
            }
        }
        view.displayInfo("--------------------------------------");
        // SHA1算法
        result = algorithm.calcWithSHA1(DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SHA1 algorithm：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        // SHA256算法
        result = algorithm.calcWithSHA256(DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SHA256 algorithm：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
        // SHA512算法
        result = algorithm.calcWithSHA512(DATA);
        if (result != null && result.length != 0) {
            view.displayInfo("SHA512 algorithm：");
            view.displayInfo("result : " + ByteUtil.bytes2HexString(result));
        }
    }
}
