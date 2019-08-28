# wenda
spring boot项目问题总结（牛客网高级项目）
## 项目介绍
使用spring boot搭建了仿知乎牛客类问答网站。
实现了基本的用户登录注册，发帖回帖，点赞关注等功能。
[GitHub项目](https://github.com/sssal/wenda)

## 项目技术栈
SpringBoot 框架
Thymeleaf 模板
Mybatis
MySQL数据库
Redis
python爬虫
版本控制工具Git
项目管理工具Maven

## 项目中技术介绍
### 登录注册
登录后产生一个ticket（UUID）,放入数据库中，通过Cookie发送到客户端中，用于识别登录用户。

[session和cookie的区别和联系](https://blog.csdn.net/wqh0830/article/details/87900394)

会话（Session）跟踪是Web程序中常用的技术，用来跟踪用户的整个会话。常用的会话跟踪技术是Cookie与Session。Cookie通过在客户端记录信息确定用户身份，Session通过在服务器端记录信息确定用户身份

在程序中，会话跟踪是很重要的事情。理论上，一个用户的所有请求操作都应该属于同一个会话，而另一个用户的所有请求操作则应该属于另一个会话，二者不能混淆。例如，用户A在超市购买的任何商品都应该放在A的购物车内，不论是用户A什么时间购买的，这都是属于同一个会话的，不能放入用户B或用户C的购物车内，这不属于同一个会话。

而Web应用程序是使用HTTP协议传输数据的。HTTP协议是无状态的协议。一旦数据交换完毕，客户端与服务器端的连接就会关闭，再次交换数据需要建立新的连接。这就意味着服务器无法从连接上跟踪会话。即用户A购买了一件商品放入购物车内，当再次购买商品时服务器已经无法判断该购买行为是属于用户A的会话还是用户B的会话了。要跟踪该会话，必须引入一种机制。

Cookie就是这样的一种机制。它可以弥补HTTP协议无状态的不足。在Session出现之前，基本上所有的网站都采用Cookie来跟踪会话。

除了使用Cookie，Web应用程序中还经常使用Session来记录客户端状态。Session是服务器端使用的一种记录客户端状态的机制，使用上比Cookie简单一些，相应的也增加了服务器的存储压力。

Session是另一种记录客户状态的机制，不同的是Cookie保存在客户端浏览器中，而Session保存在服务器上。客户端浏览器访问服务器的时候，服务器把客户端信息以某种形式记录在服务器上。这就是Session。客户端浏览器再次访问时只需要从该Session中查找该客户的状态就可以了。

如果说Cookie机制是通过检查客户身上的“通行证”来确定客户身份的话，那么Session机制就是通过检查服务器上的“客户明细表”来确认客户身份。Session相当于程序在服务器上建立的一份客户档案，客户来访的时候只需要查询客户档案表就可以了。



使用拦截器判断用户是否登录。


### 使用前缀树完成敏感词过滤，
利用字符串的公共前缀来减少查询时间，最大限度地减少无谓的字符串比较，查询效率比哈希树高。

根节点不包含字符，除根节点外每一个节点都只包含一个字符
从根节点到某一节点，路径上经过的字符连接起来，为该节点对应的字符串
每个节点的所有子节点包含的字符都不相同。
[前缀树](https://itcodemonkey.com/article/14759.html)

3、	异步框架，使用Redis实现简单的消息队列处理事务
异步编程提供了一个非堵塞的,基于事件驱动的编程模型.这种编程方式可以充分利用计算机的多核来同时执行并行任务,提高资源的利用效率.应用场景在我了解的也不多,可以想象应用场景是很广泛的.举个很简单的例子,我们在网站注册新的社交账号的话,严格一点的网站都是会有验证邮箱地址的邮件.这个就是一个异步的事件,你不可能说直接把邮件验证的流程嵌套在业务逻辑里面.应该是把这个事件发送给一个队列里面,然后队列专门处理这种事件.在一个网站中有各种各样的事情需要进行处理

事件消费者在代码中使用了单线程循环获取队列中的事件，并且寻找相应的handler进行处理。

简单设计流程:

首先定义一个定义一个枚举类型的EventType类,这里面列举各种各样将会出现的事件
再定义一个事件模型EventModel类,里面包含的是一个事件所应该具有的一些属性.例如事件类型,操作者的ID,操作的实体类型,操作的实体ID,操作的实体拥有者等等
再定义一个接口EventHandler,里面写上几个抽象的方法,具体的实现由继承的类进行实现
定义一个事件生产者EventProducer,这里就是用来生产各种各样的事件,如异常登录或者是注册邮件等等.本项目中这里就是把事件送到Redis中进行储存
定义一个事件消费者EventConsumer,这里就是需要继承InitializingBean跟ApplicationContextAware.继承InitializingBean是可以定义bean的初始化方式,继承InitializingBean是为了可以通过这个上下文对象得到我们想获取的bean.然后在这个类里面使用多线程一直去Redis里面读取出事件,然后进行处理
再继承EventHandler在方法里面写自己的实现

Spring自定义bean的初始化方法
在写事件处理的时候继承了Spring的InitializingBean接口,这个接口只有一个方法，我们要对某个bean进行自定义的初始化的时候,我们就可以让bean继承这个接口,然后在里面写上我们的业务逻辑,在Spring初始化bean的时候就会检查bean是否继承了InitializingBean接口,然后再执行afterPropertiesSet()方法

4、	使用redis实现赞踩功能，关注功能
开发点赞和点踩的功能直接调用之前写好的jedis的增删改查，放在util包内。
根据需求确定key字段，格式是like：实体类型：实体id和dislike：实体类型：实体id 这样可以将喜欢的人放在一个集合内，不喜欢的在一个集合内。

通过crud操作可以对应的获取粉丝列表和关注列表，由于关注成功和粉丝成功时同一个事务中的两个操作，可以使用redis的事务multi来包装事务并进行操作。

**点赞的操作是先修改redis的值并获取返回值，之后再异步修改mysql数据库的likecount数值。这样既可以保证点赞操作快速完成，也可以保证数据的一致性。**

5、	python爬虫实现数据抓取和导入,使用PySpider爬取数据放入数据库中
6、	实现推拉timeline，
7、	将项目部署到tomcat中

## 项目中遇到的问题
### Public Key Retrieval is not allowed
**正常的解决办法是：**
在添加MySQL数据库的地方加入**allowPublicKeyRetrieval=true**
也就是在spring boot项目中的application.properties的spring.datasource.url的末尾加上&allowPublicKeyRetrieval=true

但是我这里这么做没有用。
MySQL数据库和Mybatis一切正常，并且之前是可以正常使用的，但是仍然出现读取数据库错误。
第一次出现这种情况是我把 MySQL Workbenth开始菜单中的启动项移到了桌面上，之后启动Intellij Idea中的项目时，出现该异常，并且启动MySql Workbenth后发现数据库无法连接。

猜测与配置和代码无关，因为之前是可以正常使用的，网上找到一种可行的办法。
在管理工具中找到 服务 ，重启图中MySQL80服务，之后尝试重新连接，我在MySQL Workbenth中重新连接几次后解决该问题。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190804125931926.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4Njk0Njk1,size_16,color_FFFFFF,t_70)

### 使用mvean管理spring boot项目，resource中新添加的文件运行时不存在。
网上看到一个说法，**idea不像eclipse那样自动将新保存的文件或目录及其他资源更新到target目录中**[参考博客](https://www.cnblogs.com/caiba/p/8651791.html)

这里没有使用上述博客中的方法。
初学spring boot，很多地方都不懂，这个问题第一次出现时，是在templates中添加模板时，我记得一开始我使用freemaker时正常，后来改用themeleaf,突然就发现运行项目提示找不到我放在templates下的模板，当时以为是模板的问题，而resource文件下放新文件有时是灵的，重启后又正常了，当时没有太在意。

之后一次在读取resource目录下文件时，又出现这种情况，我以为是我代码出了问题，debug时终于找到了原因。

```java
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader(); //此处断点
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190806224037400.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4Njk0Njk1,size_16,color_FFFFFF,t_70)
如上图，类加载器默认的运行时的路径是在项目的 **/target/** 中的，而不是写的项目内，我又到target下去找，果然在外部resource内的文件target目录下不存在。
target文件是spring boot默认的输出目录，编译后启动的类是在这里开始的。

**解决方法：**
猜测是Intellij idea有自己的更新和保存时间，可能有时候刚好没有同步resource目录。我的解决办法是主动同步。
**右键 target 目录 -- Synchronize'target'**,这样之后target下文件就和外部项目内文件相同了。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190806225117532.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM4Njk0Njk1,size_16,color_FFFFFF,t_70)

### thymeleaf th:replace th:insert th:include 复用页面

```html
footer.html 页面(子页面)

<span style="font-family:SimHei;font-size:18px;">
<!--需要替换的片段内容：-->  
<footer th:fragment="copy">  
   <script type="text/javascript" th:src="@{/plugins/jquery/jquery-3.0.2.js}"></script>  
</footer>  

  
导入片段（放在主页面内）：  
  <!-- footer为导入片段所在文件 copy为导入的部分-->
  <div th:insert="footer :: copy"></div>  
  
  <div th:replace="footer :: copy"></div>  
  
  <div th:include="footer :: copy"></div>  
    
  
结果为：  
  
<div>  
    <footer>  
       <script type="text/javascript" th:src="@{/plugins/jquery/jquery-3.0.2.js}"></script>  
    </footer>    
</div>    
  
<footer>  
  <script type="text/javascript" th:src="@{/plugins/jquery/jquery-3.0.2.js}"></script>  
</footer>    
  
<div>  
  <script type="text/javascript" th:src="@{/plugins/jquery/jquery-3.0.2.js}"></script>  
</div>    
  
</span>
```
