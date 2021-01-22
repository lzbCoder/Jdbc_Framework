package jdbc.annocations;

import java.lang.annotation.*;

//可以用在方法上
@Target(ElementType.METHOD)
//指定注解的生命周期(1在源文件中存在、2在字节码中存在、3在jvm虚拟机中存在)。
@Retention(RetentionPolicy.RUNTIME) // 在jvm中存在，可以通过反射获得注解信息
//注解可继承 我们在接口方法定义的注解，在实现类的方法上也可以获得
@Inherited
public @interface Select {

    /**
     * 存储sql语句
     * @return
     */
    public String value();

}
