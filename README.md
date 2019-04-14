
# 基本框架 #
基础的SSM框架，集成了shiro作为登陆验证和权限管理和swagger作为开接口文档，让后端程序员专注于业务的开发
***

## 1、前后端分离思想
其实前后端分离并不只是开发模式，而是web应用的一种架构模式，前后端之前使用HTTP或者其他协议进行交互请求。后端负责业务/数据接口，前端负责展现/交互逻辑，前后端开发并行且独立。
随着发展，前后端分离也不局限于web开发，越来越向一对多的方向发展。对于同一份数据接口，我们可以定制开发多个客户端，比如web和app就可以使用同一个接口，多个前端展示。
***

## 2、如何实现前后端分离 
简单来说，前端使用AJAX请求后台接口，后台都数据进行处理后返回给前端，这个过程我们多半使用json格式来传递数据（也可以使用XML等），而对于前端使用Vue、React、Angular甚至Jquery都是没有关系的，同样对后端的开发语言也没有限制
***

## 3、RESTful风格 
首先，REST不是休息的意思，是一种接口开发设计规范，是Representational State Transfer的缩写，其意为“表现层状态转化”，省略了主语。"表现层"其实指的是"资源"（Resources）的"表现层"。REST认为，每一个URL都是一种资源，所有的操作都是对资源的操作，而不同的操作主要使用HTTP动词来表示，我们都知道HTTP有get和post方法，并理解他们的简单区别，但是实际上，http远不止这两种，还有其他的方法：
 
| 方法          | 含义                         |
|---------------|------------------------------|
| GET（SELECT） | 从服务器取出资源（一项或多项） |
| POST（CREATE） | 在服务器新建一个资源 |
| PUT（UPDATE） | 在服务器更新资源（客户端提供改变后的完整资源） |
| DELETE（DELETE） | 从服务器删除资源 |
| HEAD | 获取资源的元数据 |
| OPTIONS | 获取信息，关于资源的哪些属性是客户端可以改变的 |



主要使用的前4种方法，分别对应着“查、增、改、删”。每一项都是对资源的操作。具体的详细解释可以参考阮一峰先生所讲的[《理解RESTful架构》](http://www.ruanyifeng.com/blog/2011/09/restful.html "点击前往阮一峰先生的博客")和[
《RESTful API 设计指南》](http://www.ruanyifeng.com/blog/2014/05/restful_api.html "点击前往阮一峰先生的博客")
下面给出一些例子：
 
| 方法          | 含义                         |
|---------------|------------------------------|
|GET /zoos|列出所有动物园|
| POST /zoos|新建一个动物园|
| GET /zoos/ID|获取某个指定动物园的信息|
| PUT /zoos/ID|更新某个指定动物园的信息（提供该动物园的全部信息）|
| PATCH /zoos/ID|更新某个指定动物园的信息（提供该动物园的部分信息）|
| DELETE /zoos/ID|删除某个动物园|
| GET /zoos/ID/animals|列出某个指定动物园的所有动物|
| DELETE /zoos/ID/animals/ID|删除某个指定动物园的指定动物|
***

# 下面就说一下具体的实现与配置
## 4、统一响应结构
使用REST框架实现前后端分离架构，我们需要首先确定返回的JSON响应结构是统一的，也就是说，每个REST请求将返回相同结构的JSON响应结构。定义一个相对通用的JSON响应结构，其中包含两部分：元数据与返回值，其中，元数据表示操作是否成功与返回值消息等，返回值对应服务端方法所返回的数据。该JSON响应结构如下：
``` json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```
可以在每次返回时候根据实际情况返回数据
``` json
{
  "code": 200,
  "message": "成功查询用户",
  "data": {
    "user": {
      "id": 1,
      "account": "test_user",
      "password": "123456"
          }
  }
}
```
下面给出JAVA中的代码
``` java
public class Msg {

	private int code;

	private String message;

	private Map<String,Object> data = new HashMap<String, Object>();

	private Msg(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public static Msg message(int code,String message){
		Msg result = new Msg(code,message);
		return result;
	}
	/**
	 * 处理成功时返回的数据
	 * @return
	 */
	public static Msg success(){
		Msg result = new Msg(200,"处理成功");
		return result;
	}

	public static Msg success(String message){
		Msg result = new Msg(200,message);
		return result;
	}

	public static Msg error(){
		Msg result = new Msg(400,"处理失败");
		return result;
	}
	public static Msg error(String message){
		Msg result = new Msg(400,message);
		return result;
	}

	/**
	 * 添加封装的数据，实现链式编程
	 * @param key
	 * @param value
	 * @return
	 */
	public Msg add(String key,Object value){
		this.data.put(key,value);
		return this;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Msg{" +
				"code=" + code +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}
```
值得一提的是，我在Msg中没有完全封装data返回值，而是在每次返回的时候手动添加“data”，这是因为我采用了HashMap的结构，可以使用add方法可以不断添加需要的返回值，实现链式编程

对于前后端之间如何传输json数据，在spring中有相应的注解来解决这一问题，在返回的时候可以使用@ResponseBody来将返回的数据序列化成json，在使用Post方法或Put方法时，也可以使用@RequestBody来将json数据转换成JAVA中的对象。
顺便提一下 可以采用@RestController实现@Controller和@ResponseBody的合并效果
***

## 5、统一异常处理
在代码中我们经常需要捕获异常，如果我们在每一个方法里面进行try catch来捕获可能出现的异常是十分麻烦的，所以我们可以采用@ControllerAdvice定义一个增强的控制器来处理全局异常。
``` java
/**
 *
 * 全局异常处理,所有的异常都放在这里进行处理,无需在每个地方try catch
 *
 * Copyright(C) 2018-2018
 * Author:wanhaoran
 * Date: 2018/6/1 8:37
 */
@RestControllerAdvice
public class ExceptionAdvice {

	private static final Logger LOGGER = LogManager.getLogger(UserRealm.class);
	/**
	 * 信息无法读取
	 * @param e
	 * @return
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Msg handleHttpMessageNotReadableException(Exception e){
		e.printStackTrace();
		return Msg.message(400,"无法读取");
	}

	/**
	 * 处理参数异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Msg handleMethodArgumentNotValidException(Exception e){
		return Msg.message(400,"参数验证失败");
	}

	/**
	 * 处理自定义异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(IException.class)
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	public Msg handleIException(IException e){
		return Msg.message(417,"自定义异常");
	}

	/**
	 * 数学计算错误
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ArithmeticException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Msg handleArithmeticException(ArithmeticException e){
		return Msg.message(500,"服务器内部错误");
	}

	/**
	 * 登陆错误
	 * @param e
	 * @return
	 */
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Msg handleAuthenticationException(AuthenticationException e){
		LOGGER.error(e);
		return Msg.message(401,"登陆错误");
	}

	@ExceptionHandler(UnknownAccountException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public Msg handleUnknownAccountException(UnknownAccountException e){
		LOGGER.error(e);
		return Msg.message(401,"请登录");
	}


	/**
	 * 没有权限——shiro
	 * @param e
	 * @return
	 */
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Msg handleUnauthorizedException(UnauthorizedException e){
		return Msg.message(403,"没有权限");
	}
}
```

定义个带有@ControllerAdvice的这个类之后，在其中可以写对于异常的处理，在方法上使用@ExceptionHandler注解决定处理哪一种异常，而@ResponseStatus用来决定返回的HTTP状态码，之后我们返回给前端的数据也还是Msg的结构，里面包含了发生的异常的相关信息，前端就可以进行相应的处理，提醒用户进行正确的操作，而不是单纯的报错，提升用户体验。
此外：在这个类中@ExceptionHandler的位置，是从上到下依次检查的，如果你在前面定义了@ExceptionHandler（Exception.class)进行处理，那么后面定义的对于Exception的子类的处理就全部无效了。
#### 我们还可以根据需要设计自己的异常类。
```java
/**
 *
 * 自定义异常
 *
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/6/1 8:29
 */
public class IException extends RuntimeException{
	private static final long serialVersionUID = 7144771828212718116L;
	private String message;

	public IException(String message){
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
```
可以在相应的地方抛出自定义异常然后被ControllerAdvice定义的类中的方法捕获。
***

## 6、参数验证
```java
  @RequiresPermissions("user:add")
	@ApiOperation(value = "新增用户", notes = "根据用户对象来新增")
	@PostMapping()
	public Msg addUser(@RequestBody @Valid SysUser sysUser) throws Exception{
		sysUserService.addUser(sysUser);
		return Msg.message(201,"成功新增用户");
	}
```
比如对于这个方法，我们需要获取一个SysUser，但是前端传来的并不一定是合理的数据，虽然前端也可以做一些验证，但并不可靠，对于隐私数据，后端还需要再做一次验证。那我们需要一个个判断吗，当然不是，我们可以使用Hibernate Validator来实现（此处的Hibernate和SSH中的Hibernate不是同一个东西）。例如我们需要这个前端传来的SysUser的账户名不能为空、密码不能少于6位等需求。就可以在获取的对象前面加上@Valid注解，同时在SysUser中对于的成员变量加入注解
``` java
@NotEmpty(message = "姓名不能为空")
private String account;

@Length(min = 6,message = "密码不能少于6位")
private String password;
```
就可以自动的进行验证
然后在前面所说的全局异常处理器中处理这个异常
```java
	/**
	 * 处理参数异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Msg handleMethodArgumentNotValidException(Exception e){
		return Msg.message(400,"参数验证失败");
	}
  ```
  如果我们提供不符合要求的数据，那么就会返回如下结果
  ```json
  {
  "code": 400,
  "message": "参数验证失败",
  "data": {}
}
```
***

## 7、接口文档自动生成
每次写完接口之后，都还要写相应的接口文档与他人协调与测试，这个过程是十分繁琐切复杂的，而且对于每一次接口的修改都要修改相应的接口文档。为了解决这个问题，我们可以集成spring和swagger，swagger可以帮助我们根据Controller自动生成相应的接口文档，我们只需要使用注解来对接口文档进行说明就可以了。这样可以省下一大笔时间，而且每次修改后swagger也会相应的更改，再也不用担心接口更改但是接口文档没有更改的情况了。
我们需要定义一个swagger-config类来设计swagger的相关配置
```java
**
 *
 * 集成Swagger2的页面配置
 *
 * Copyright(C) 2018-2018
 * Author: wanhaoran
 * Date: 2018/5/31 16:05
 */
//@Configuration //这里需要注意，如果项目架构是SSM，那就不要加这个注解，如果是 spring boot 架构类型的项目，就必须加上这个注解，让 spring 加载该配置。
@EnableWebMvc // spring boot 项目不需要添加此注解，SSM 项目需要加上此注解，否则将会报错。
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket buildDocket(){
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(buildApiInfo())
				.select().apis(RequestHandlerSelectors.basePackage("controller"))// controller路径。
				.paths(PathSelectors.any())
				.build();
	}

	// 配置 API 文档标题、描述、作者等等相关信息。
	private ApiInfo buildApiInfo(){
		return new ApiInfoBuilder()
				.title("前后端分离系统API接口文档")
				.termsOfServiceUrl("")
				.description("Spring MVC中使用Swagger2构建Restful API")
				.build();

	}
}
```
然后将其加载到容器当中，在springmvc.xml中输入
<beanclass="common.SwaggerConfig"/>
具体的swagger注解如何使用可以参考：[注解的使用说明](https://my.oschina.net/zzuqiang/blog/793606)

![swagger页面](https://github.com/xhwanhaoran/ssm/blob/master/swagger2.jpg?raw=true)


## 8、shiro的集成
### 关于shiro ###
shiro是目前流程轻便的安全框架，用来解决登陆认证和权限管理等问题，具体的解释小伙伴们可以自行google，就不对shiro的含义展开详细论述了
对比Spring Security，可能没有Spring Security做的功能强大，但是在实际工作时可能并不需要那么复杂的东西，所以使用小而简单的Shiro就足够了。对于它俩到底哪个好，这个不必纠结，能更简单的解决项目问题就好了。

### shiro中的会话管理和权限管理 ###
用户第一次访问受限的资源时，shiro会去加载用户能访问的所有权限标识。默认情况下，shiro并未缓存这些权限标识。当再次访问受限的资源时，还会去加载用户能访问的权限标识。 
当请求多时，这样处理显然会影响性能，因此需要为shiro加缓存。shiro本身内置有缓存功能，需要配置启用它。shiro为我们提供了两个缓存实现，一个是基于本地内存（org.apache.shiro.cache.MemoryConstrainedCacheManager），另一个是基于EhCache（org.apache.shiro.cache.ehcache.EhCacheManager）。这两套实现都只适合单机玩，当在分布式环境下效果就不理想了。
于是采用了一套基于redis的shiro缓存实现，便于分布式的需求，具体的配置可以直接查看源代码文件
***

# 说明
* 在源代码里，所有的配置文件中都有详细的注释，如果有需求的话可以直接查看和学习
* 有什么疑问或者发现了项目中的问题可以直接和我发email练习
* 如果项目对你有所帮助，请点击一个star
