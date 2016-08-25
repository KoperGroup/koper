# Koper
 Koper is a MQ-based and event-driven distributed programming framework. [[中文README]](https://github.com/raymondhekk/koper/blob/master/README-CN.md)

 * Koper provides a simplified distributed listener and data listener model,which can help you build your async application in a simple way.


    [[Quick Start]](https://github.com/ZhaimeGroup/koper/wiki/Quick%20Start) [[快速指南]](https://github.com/KoperGroup/koper/wiki/%E5%BF%AB%E9%80%9F%E5%90%AF%E5%8A%A8)  
    [[User Guide]](https://github.com/ZhaimeGroup/koper/wiki/User%20Guide)  [[用户指南]](https://github.com/KoperGroup/koper/wiki/%E7%94%A8%E6%88%B7%E6%8C%87%E5%8D%97)

# Concept 
 * Core architecture:  Message Architeture， Event Driven Architecture（EDA）
 * Core concept:       producer, consumer, message, Message Queue(MQ), topic, subscribe
 * Core component:     MessageSender, MessageListener, DataEventListener.
 * High-level concept: consumer group, message partition

![](https://raw.githubusercontent.com/wiki/ZhaimeGroup/koper/images/arch1.png)


# Feature
 *  Simplified MQ-based listener model and API.
 *  Simplified Data Event model and API.
 *  MQ provider independent. 
     *  Koper supports Kafka by default, but also supports other message queue as provider, e.g.  RabbitMQ, RocketMQ.
 *  High performance and throughput.
 *  High scalability based on MQ.
 *  High-level feature: time spot recording, message tracking.
 
# Programming Demo

### 1. Listener Model
  Member signup example.
  When a member signs up, a message is sent to MQ by messageSender.
```Go
   messageSender.send("koper.memberSignup", "Signed up successfully! " + member.getPhoneNo());
```
 On the other hand, a consumer subscribes the topic and handle the message, e.g. send a SMS to notify member.
 ``` java
 @Component
 @Listen(topic = "koper.memberSignup")
 public class MemberSignupListener extends AbstractMessageListener {

    @Autowired
    private SmsService smsService;
    
    public void onMessage(String msg) {
        smsService.sendSms(msg);
    }
    
 }
 ```

### 2. Data Event & Data Listener Model
 Order example. 
 
 Koper support EDA programming. The Data Event machanism can intercept method invocation of object(such as DAO) and send it to MQ as data event. 
 ```Java
orderDao.insertOrder( order);
orderDao.updateOrder( order);
 ```
 DataListener responds to the event.
 ``` java
 @Component
 @DataListener(dataObject = "koper.message.demo.dataevent.dao.impl.OrderDaoImpl")
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

# What can we do with Koper?
 Koper is designed to support distributed asynchornous programming based on Message Queue and Event Driven Ahchitecture. It's suitable for scenarios such as Web Application, Business Monitor, Data statistics or Big Data etc.

In a large-scale application, the system architecture and event driven architecture look like something below.

![](https://raw.githubusercontent.com/wiki/ZhaimeGroup/koper/images/eda.png)

#### Typical use cases
  * Asynchronous Business Process
  * Distributed Data Update or Cache Update
  * Data Log
  * Business Monitor and Alarm
  
 Refer to [Async Scenarios and examples](https://github.com/ZhaimeGroup/koper/wiki/Async-Scenarios-and-examples) for more demos.

# Contribute
#### 1  Fix bug or enhance Koper
 Feel free to `Fork` the source code and commit bug fix or enhancement. And then make a pull request to us.
 
#### 2 Contribute other MQ provider
 Koper provides a Kafka provider implementation by default, but also provides high extensibility. You can implement other MQ provider easily, such as RabbitMQ, RocketMQ, ActiveMQ etc.
 
Refer to [Developer Guide](https://github.com/ZhaimeGroup/koper/wiki/Developer%20Guide) for details.

