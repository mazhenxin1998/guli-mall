#### 在写该项目中遇见过的难题. 
##### 一、 Feign远程调用出现过的错误. 
###### 1. Feign远程调用出现`FeignException$NotFound: status 404 reading...`: 
1. 远程服务路径匹配上有空值出现: 比如在调用MemberServiceFeign的时候有个路径占位符参数, 但是在当前服务中传进去的参数
没有进行空校验，导致了当前服务远程调用的时候一直出现`FeignException$NotFound: status 404 reading...`异常.

###### 2. Feign远程调用出现请求头数据丢失问题
1. 向IOC容器中添加实现Feign中的RequestInterceptor接口的组件,并且将当前请求头中的参数赋给RequestTemplate.


###### 3. Feign远程调用参数是数组问题.  improvement
1. 问题概述: 
    `Feign`远程调用参数不能是`List`否则`Feign`将会报出找不到`java.lang.List.init`方法.
2. 解决办法: 
    `Feign`只能接受String[]类型,并且必须加上`@ReuqestBody`注解,这样`Feign`才能成功解析成功. 