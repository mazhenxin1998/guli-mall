package com.mzx.gulimall.util;

/**
 * @author ZhenXinMa
 * @date 2020/9/4 18:48
 */
class Base{
    public void method(){
        System.out.println("Base");
    }
}
class Son extends Base{

    @Override
    public void method(){
        System.out.println("Base");
    }

    public void methodB(){
        System.out.println("Base");
    }
}

public class Test{

    //  这个时候是不会进行编译的,因为编译会出现错误,
    public static void main(String... args){

        // 在编译时期, 方法调用是通过
        Base base = new Son();
        base.method();
        //  编译都不会通过.
//        base.methodB();

    }

}
