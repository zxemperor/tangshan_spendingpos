package vdpos.com.tcps.basicplug.util.error.status;

/**
 * 在运行中变更为不可变参数,防止外部注入型侵犯动作发生。
 * @author zhaoxin 2019-07-19 14:37
 * {@see java.lang.Enum}
 * */
public enum ExcuteStatus {
    /**
     * 执行操作后的各种状态值
     */
    RF_SUCESS(0x00),//执行成功 属于BASE状态
    RF_COLLAPSE(0x01), //扫描崩溃
    REQUESTERROR(0x02), //响应错误
    ERROR_ATRERR(0x03),//上电响应ATR错误
    ERROR_NOPOWER(0x04),//硬件错误
    ERROR_ATRERR_S(0x05),//社保卡错误
    ERROR_NOCARD(0x06),//缺少SAM卡 无响应
    ERROR_FAILED(0x07),//程序其它错误
    ERROR_ERRTYPE(0x08),//卡类型错误
    ERROR_TIMEOUT(0x09),//与外置读卡通讯错误
    NOWAY_ERROR(0x0A),//结果未知错误
    ON_CRASH(0x0B),//模块崩坏错误
    UNSUPPORT_EXCEPT(0x0C),//系统权限拒绝
    NUPOINT_EXCEPT(0x0D),//NULLpointerException e
    GENER_EXCEPT(0x0E),//generalized 广义 Exception e
    FAILED_KEYOP(0x0F),//获取密钥卡基础分散值失败
    VALIDA_ERROR(0x10),//扇区验证失败
    INVALID_KEY_ERROR(0x11),//key无效错误 InvalidKeyException
    CARDERROR(0x12),//刷卡错误


    /**
     * 限定卡槽ID (1-4)
     * 现在启用了 1-2卡槽针对 POS 如有其它设备将会启用另外两个接口
     */
    SLOT_IDONE(1),
    SLOT_IDTWO(2),
   // SLOT_THREE(3),
   // SLOT_FOUR(4);
    /**卡类型 CPU = 0xC0 M1 = 0xC1*/
    CPU_TYPE(0xC0),
    MI_TYPE(0xC1),
    /**Psam 卡槽业务区分 0XFE-0XFF*/
    PSAM_TRAFFIC((byte)0xFE),
    PSAM_LBUILD((byte)0xFF);

    /**接受编译过程初始化赋值的变量*/
    private int Error_status;
    /**编译时接受初始化构造函数
     * @param status
     * 该参数为10进制数 编码过程中进行了确定值初始化
     * */
    ExcuteStatus(int status) {
        Error_status = status;
   }

    /**
     * @return int 返回指定错误信息
     */
    public  int getStatus(){
        return Error_status&0xff;
    }
}
