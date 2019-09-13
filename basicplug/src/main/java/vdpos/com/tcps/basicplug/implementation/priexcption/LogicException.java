package vdpos.com.tcps.basicplug.implementation.priexcption;

public class LogicException extends Exception {
    String message; //定义String类型变量

    public LogicException(String ErrorMessagr) {  //父类方法
        message = ErrorMessagr;
    }

    public String getMessage() {   //覆盖getMessage()方法
        return message;
    }
}
