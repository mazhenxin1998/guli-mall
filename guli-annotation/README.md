##### 注意事项:
1. 引入guli-annotation服务的前提条件是，那个服务必须有Redis和Redisson的完整环境.
2. guli-annotation服务没有在guli-common服务中引入，所以需要单独在服务内部引用. 
3. 如果配置好了Redis和Redisson那么@AccessLimt和@CurrentCache可以直接使用. 

##### 注解详情: 
###### 一、接口防刷注解 `@AccessLimit()`的使用:
1. 指定好在限定时间`seconds()`通过请求的次数`maxCount`.
2. 该注解的原理使用的是拦截器`PreventBrushInterceptor`, 如果要使用该注解,那么就就需要在使用该注解服务中通过`WebMvcConfigurer`
配置类，将拦截器`PreventBrushInterceptor`注册到配置中并起作用.
3. 该注解使用需要Redis环境, 所以说引用该服务的服务要先配置好Redis环境. 
3. 如果上面两个步骤完成, 那么注解`@AccessLimit()`就已经起作用了. 

###### 二、缓存接口: `@CurrentCache()`的使用:
1. 该接口在底层实现是通过Redisson分布式锁来实现的，而guli-annotation中没有配置Redisson的环境, 所以引用该服务中的服务必须要有
Redis和Redisson的完整环境. 
2. 该缓存注解在Redis中默认的缓存Key是该方法的第一个参数(如果第一个参数是数组,那么就用这个数组的字符串来作为Key),如果方法内没有参数,那么就使用
注解属性`key`表示缓存的key. 
3. 参数详解:
    1. `prefix`表示当前方法缓存Key的前缀. 
    2. `timeout`表示缓存的过期值.
    3. `random` 为了解决缓存雪崩带来的影响,为每个Key添加一个在`random`范围内的随机值. 
    4. `lock` 为了解决缓存击穿导致瞬间过多请求访问DB设置的分布式锁的key.
    5. `key` 解决了没有参数的方法进行缓存的时候没有默认的key, 这个key为没有参数的方法进行缓存指定的key. 
