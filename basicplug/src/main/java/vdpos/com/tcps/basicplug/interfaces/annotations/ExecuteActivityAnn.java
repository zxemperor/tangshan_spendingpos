package vdpos.com.tcps.basicplug.interfaces.annotations;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Documented

/**@author 赵鑫*/
public @interface ExecuteActivityAnn{
    /**
     * 自定义注解类 方便自动维护可变变量参数传递
     * @return Class Object
     *
     */
    Class ImplActivity();
}
