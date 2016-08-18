# Koper
 Koper is a MQ-based and event-driven distributed programming framework.
 * Koper provides a simplified distributed listener and data listener model,which can help you build your async application quickly and easily.

    [[Quick Start]](https://github.com/ZhaimeGroup/koper/wiki/Quick%20Start)   
    [[User Guide]](https://github.com/ZhaimeGroup/koper/wiki/User%20Guide)  

# Concept 
 * Core architecture:  Message Architeture， Event Driven Architecture（EDA）
 * Core concept:       producer, consumer, message, Message Queue(MQ), topic, subscribe
 * Core component:     MessageSender, MessageListener, DataEventListener.
 * High-level concept: consumer group, message partition

<img src="image/arch1.png" />


# Feature
 *  Simplified MQ-based listener model and API.
 *  Simplified Data Event model and API.
 *  MQ component independent. 
     *  Koper supports Kafka by default, but also supports other message queue as component, e.g.  RabbitMQ, RocketMQ.
 *  High performance and throughput.
 *  High scalability based on MQ.
 *  High-level feature: time spot recording, message tracking.
 
# Programming Demo

### 1. Listener Model
  Member signup example.
  When a member signs up, a message is sented to MQ by messageSender.
```Go
   messageSender.send("zhaimi.memberSignup", "Signed up successfully! " + member.getPhoneNo());
```
 On the other hand, a consumer subscribes the topic and handle the message, e.g. send a SMS to a member notifier.
 ``` java
 @Component
 public class MemberSignupListener extends AbstractMessageListener {

    @Autowired
    private SmsService smsService;

    @Listen(topic = "zhaimi.memberSignup")
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
 @DataListener(dataObject = "com.zhaimi.message.demo.dataevent.dao.impl.OrderDaoImpl")
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
<img src="https://github.com/ZhaimeGroup/koper/blob/master/image/eda.png">

#### Typical use cases
  * Asynchronous Business Process
  * Distributed Data Update or Cache Update
  * Data Log
  * Business Monitor and Alarm
  
 Refer to [Async Scenarios and examples](https://github.com/ZhaimeGroup/koper/wiki/Async-Scenarios-and-examples) for more demos.

# Contribute
####  Fix bug or enhance Koper
 Any bug fix or function enhancement is welcomed. Feel free to Fork the source code and commit your pull request.
 
 Refer to [Developer Guide](https://github.com/ZhaimeGroup/koper/wiki/Developer%20Guide) for details.
 
####  Contribute other MQ component
 Koper provides a Kafka component implementation by default, but also provides high extensibility. You can implement other MQ provider easily, such as RabbitMQ, RocketMQ, ActiveMQ etc.
 
 <img src="https://github.com/ZhaimeGroup/koper/blob/master/image/koper-extend.png"/>

 For examele, if you need to integrated with legacy RabbitMQ, you just need to write two provider classes 
 ```RabbitSender``` and ```RabbitReceiver``` by implementing interfaces

```Java
 MessageSender :   public interface MessageSender 
 MessageReceiver : public interface MessageReceiver 
```

 Write another provider, refer to the source code of Kafka provider for more details. [KafkaSender](https://github.com/ZhaimeGroup/koper/blob/master/koper-core/src/main/java/com/zhaimi/message/sender/MessageSender.java), [KafkaReceiver](https://github.com/ZhaimeGroup/koper/blob/master/koper-core/src/main/java/com/zhaimi/message/client/MessageReceiver.java).
 
 
