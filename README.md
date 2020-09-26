#### 小苹果校园超市助手(原型模板采用的是尚硅谷谷粒商城项目). 
##### 一、项目背景. 
###### 1. 你为什么要开始写你这个项目? 
1. 我是在2020年7月份中旬开始写小苹果校园超市助手项目的,那个时候还在家里(因为疫情的原因),在那个时候能从网上得知去哪里都得戴上口罩，
并且人和人的间隔都得1.5m, 于是我就想，我是学软件的,我能不能自己写个东西来减少人员流动? 一开始这个念头还不是很大, 但是到9月份开学之后,
这个念头变得越来越大,最后自己就动手开始写了, 一开始之前,想的是自己写的这个项目能不能和学校里面的超市进行合作,但是在这个项目后期,
才发现自己有点天真,因为仅仅是一个项目所需要的服务器自己就承受不起, 并且还需要定期的维护,有了Bug还的去解决等, 所以小苹果校园超时助手
在写完之后也没有进行推广, 虽然没有进行推广, 但是还是从这个项目中学到了很多东西. 比如说`SpringCloud`技术栈,`Redis`作为项目的缓存
服务器,并且也解决了缓存穿透、缓存雪崩、缓存击穿，`RabbitMQ`实现对流量的晓峰以及异步、解耦, 通过AOP实现自定义注解来解决
`SpringCache`不能解决的缓存击穿问题, 并且还自定义了一个接口防刷的接口,通过拦截器进行实现. 在该项目中还通过一个公共的注解服务
来提供`@CurrentCache`和`@AccessLimit`两个注解的功能.

##### 二、项目技术栈
###### 明天完成. 