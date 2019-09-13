package vdpos.com.tcps.basicplug.util.error.status;

/**
 * PSAM指令&错误指令返回集合类
 */
public class PsamAPDUS {
    /**
     * 错误集合类
     */
    public static class PsamError {
        public static final byte[] APP_GET_RESPONSE = {0x61, 0x14};//获取异步消息
        public static final byte[] APP_NO_FIND = {0x6A, (byte) 0x82};//PSAM6A82 出错 该文件未找到
        public static final byte[] PSAM_EXCEPTION = {0x00, (byte) 0xFF};//异常错误需要查看logcat
        public static final byte[] PSAM_SUCESS = {(byte) 0x90, 0x00};//执行成功
    }

    /**
     * 固定APDU 指令
     */
    public static final byte[] JTB_apdu = {0x00, (byte) 0xa4, 0x00, 0x00, 0x02, (byte) 0x80, 0x11};//标准交通部统一 交通部选择应用APDU指令 PSAM初始化流程第二步
    public static final byte[] ZJB_apdu = {0x00, (byte) 0xa4, 0x00, 0x00, 0x02, (byte) 0xdf, 0x01};//标准住建部指令(适应于住建部) PSAM初始化流程第二步
    public static final byte[] PSAM_CARDNUM = {0x00, (byte) 0xb0, (byte) 0x96, 0x00, 0x06};//读取PSAM卡号 PSAM初始化流程第一步
    public static final byte[] GET_RESPONSE = {0x00, (byte) 0xc0, 0x00, 0x00};//当系统返回61XX指令时 需要异步二次获取 二次获取指令为00C00000
}
