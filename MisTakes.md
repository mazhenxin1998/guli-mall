#### 在写该项目中遇见过的难题. 
##### 一、 Feign远程调用出现过的错误. 
###### 1.1 Feign远程调用出现`FeignException$NotFound: status 404 reading...`: 
1. 远程服务路径匹配上有空值出现: 比如在调用MemberServiceFeign的时候有个路径占位符参数, 但是在当前服务中传进去的参数
没有进行空校验，导致了当前服务远程调用的时候一直出现`FeignException$NotFound: status 404 reading...`异常.

###### 1.2 Feign远程调用出现请求头数据丢失问题
1. 向IOC容器中添加实现Feign中的RequestInterceptor接口的组件,并且将当前请求头中的参数赋给RequestTemplate.


###### 1.3 Feign远程调用参数是数组问题.  improvement
1. 问题概述: 
    `Feign`远程调用参数不能是`List`否则`Feign`将会报出找不到`java.lang.List.init`方法.
2. 解决办法: 
    `Feign`只能接受String[]类型,并且必须加上`@ReuqestBody`注解,这样`Feign`才能成功解析成功. 
    
###### 1.4 Feign远程请求出现JSON反序列化出现错误. 
1. 问题概述: 出现的问题是: org.springframework.http.converter.HttpMessageNotReadableException: JSON parse Data. 
    也就是JSON反序列化日期时间格式转换的时候出现问题. 
2. 解决问题: 我的思路是在`product`服务中将返回结果直接以JSON形式的返回，也就是我的JSON序列化不直接通过Spring提供的序列化和反序列化来进行序列化,
    也就是绕过Spring的序列化,在当前服务中将JsonString转换成对应的对象.
   
###### 1.5 Feign远程请求明明是一个对象,但是到我的这个服务中确实一个LinkedHashMap数据结构. 
    
##### 二、使用SpringBoot遇到的问题.
###### 2.1. SpringBoot启动的时候找不到主启动类或者无法加载. 
1. 解决办法: 出现这种情况可能就是jar包发生冲突，所以使用Maven的clear命令进行清除. 然后重新启动project即可.


###### 2.2 JSON数组转换List.
1. 使用fastjson封住的对象`JSONObject.parseArray`进行转换. 
    `List<Item> itemList = JSONObject.parseArray(data.toString(), Item.class);`
    
    
##### 三、事务相关. 
###### 3.1 SpringBoot事务相关. 
1. 在`SpringBoot`中如果在方法体内进行异常的捕获, 那么SpringBoot是不会感知到发生异常了的,那么相应的事务就不会进行回滚操作.
2. 只有在方法上抛出异常，`SpringBoot`事务管理器才能感知到异常,并进行事务的回滚. 

###### 3.2 SpringBoot中使用事务的坑. 
1. 问题:  如果要在`SpringBoot`环境下,在一个`Service`中,当前`Service`的事务方法**A**调用了当前`Service`的事务方法**B**, 那么方法**B**的事务控制将会失效. 也就是说，
在该情况下方法**B**的事务就会和方法A的事务一样，换句话说就是方法覆盖了方法**B**的事务.
2. 解决: 我们知道`SpringBoot`中事务是使用**JDK**代理实现的, 而如果我们使用**CGLIB**进行代理, 并且将代理类对外暴露出来，每次调用方法**B**的时候都通过当前**Service**
的代理类来进行方法**B**的代用.