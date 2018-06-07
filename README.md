
## 基本框架 ##
基础的SSM框架，集成了shiro作为登陆验证和权限管理和swagger作为开接口文档，让后端程序员专注于业务的开发

## 前后端分离 ##
采用了前后端分离，后端提供restful的接口的写法。
目前在开发中流行后端写接口来传递json数据，根据需要来完成web浏览器或APP等前端展示，这样后端只需要一次开发，就可以完成不同前端的需求

## 关于shiro ##
shiro是目前流程轻便的安全框架，对比Spring Security，可能没有Spring Security做的功能强大，但是在实际工作时可能并不需要那么复杂的东西，所以使用小而简单的Shiro就足够了。对于它俩到底哪个好，这个不必纠结，能更简单的解决项目问题就好了。

## shiro中的会话管理和权限管理 ##
用户第一次访问受限的资源时，shiro会去加载用户能访问的所有权限标识。默认情况下，shiro并未缓存这些权限标识。当再次访问受限的资源时，还会去加载用户能访问的权限标识。 
当请求多时，这样处理显然会影响性能，因此需要为shiro加缓存。shiro本身内置有缓存功能，需要配置启用它。shiro为我们提供了两个缓存实现，一个是基于本地内存（org.apache.shiro.cache.MemoryConstrainedCacheManager），另一个是基于EhCache（org.apache.shiro.cache.ehcache.EhCacheManager）。这两套实现都只适合单机玩，当在分布式环境下效果就不理想了。
于是采用了一套基于redis的shiro缓存实现。，便于分布式的需求
