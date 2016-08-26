# Koper
Koper是一个基于消息队列和分布式事件驱动计算的框架。

 * Koper提供了简化的分布式监听器模型和数据监听器模型，它可以帮你建立异步化应用(in a simple way)。

    [[快速启动]](https://github.com/KoperGroup/koper/wiki/%E5%BF%AB%E9%80%9F%E5%90%AF%E5%8A%A8)
    [[用户指南]](https://github.com/KoperGroup/koper/wiki/%E7%94%A8%E6%88%B7%E6%8C%87%E5%8D%97)

# Concept

# 概念
 * 核心架构： 消息架构， 事件驱动架构(EDA)
 * 核心概念： 生产者，消费者，消息，消息队列，主题，订阅
 * 核心组件： 消息发送者(MessageSender), 消息监听者(MessageListener), 数据事件监听者(DataEventListener)
 * 高层概念： 消费者群组, 消息分区

![](https://raw.githubusercontent.com/wiki/KoperGroup/koper/images/arch1.png)


# 特性
 * 简化的消息队列监听器模型和API。
 * 简化的数据事件模型和API。
 * 独立消息队列提供者。
     * Koper默认支持Kafka，也支持其他消息队列作为提供者，例如 RabbitMQ，RocketMQ
 * 高性能、高吞吐量
 * 基于消息队列的高可伸缩性
 * 高级特性： 时间点记录、消息追踪

# 编程模型

### 1. 监听器(Listener)模型
用户注册的例子。
当一个用户注册， `MessageSender` 会向MQ发送一个消息。

``` Go
   messageSender.send("koper.memberSignup", "Signed up successfully! " + member.getPhoneNo());
```
同时，消费者订阅了这个主题然后处理消息(发送消息通知用户)。
 ``` java
 @Component
 @Listen(topic = "koper.memberSignup")
 public class MemberSignupListener {

    @Autowired
    private SmsService smsService;

    public void onMessage(String msg) {
        smsService.sendSms(msg);
    }

 }
 ```

### 2. 数据事件 & 数据监听器模型
订单例子。

Koper支持事件驱动编程。数据事件的机制是拦截方法调用，并把数据事件发送给消息队列。
```Java
orderDao.insertOrder( order);
orderDao.updateOrder( order);
```
响应事件的监听器（DataListener）
 ``` java
 @Component
 @DataListener(dataObject = "koper.demo.dataevent.dao.impl.OrderDaoImpl")
 public class OrderListener {
    // data event: onInsertOrder
    public void onInsertOrder(Order order) {
        System.out.println("orderNo : " + order.getOrderNo());
        System.out.println("create time : " + order.getCreatedTime());
        // do some other operations
    }
    //data event: onUpdateOrder
    public void onUpdateOrder(Order order) {
        System.out.println("orderNo : " + order.getOrderNo());
        // do some other operations such as cache refresh
    }
   //data event: exception on updateOrder
    public void onUpdateOrder_X(Order order, DataEvent event) {
       String ex = event.getException();
       System.out.println("onUpdateOrder exception :" +ex);
    }
 }
 ```

# Koper 可以用来做什么？
Koper是基于消息队列和事件驱动架构进行分布式计算的框架，它适用于Web应用，业务监控，数据统计和大数据等场景。

在一个高可伸缩的应用里，系统架构和事件驱动架构如下：

![](https://raw.githubusercontent.com/wiki/KoperGroup/koper/images/eda.png)

#### 典型的使用场景
   * 异步业务处理
   * 分布式数据更新或缓存更新
   * 数据日志
   * 业务监控和告警

详情参见 [Async Scenarios and examples](https://github.com/KoperGroup/koper/wiki/Async-Scenarios-and-examples).

# 如何参与贡献

#### 1 修改BUG 或 改进Koper
你可以自由 `Fork` 源代码，提交你的Pull Request给我们。

#### 2 贡献其他的消息队列实现
Koper提供了Kafka Provider作为缺省实现。 而且Koper有着良好的扩展性，你可以轻松的实现其他消息队列的Provider，比如说 RabbitMQ, RocketMQ, ActiveMQ 等等。

详情参见 [Developer Guide](https://github.com/KoperGroup/koper/wiki/Developer%20Guide) .
