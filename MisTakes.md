#### 在写该项目中遇见过的难题. 
##### 一、 Feign远程调用出现过的错误. 
###### 1. Feign远程调用出现`FeignException$NotFound: status 404 reading...`: 
1. 远程服务路径匹配上有空值出现: 比如在调用MemberServiceFeign的时候有个路径占位符参数, 但是在当前服务中传进去的参数
没有进行空校验，导致了当前服务远程调用的时候一直出现`FeignException$NotFound: status 404 reading...`异常.

###### 2. Feign远程调用出现请求头数据丢失问题
1. 暂时没有解决.  