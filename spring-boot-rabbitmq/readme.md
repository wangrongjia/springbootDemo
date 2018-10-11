https://blog.csdn.net/roykingw/article/details/78404956

### 基本概念
通常我们谈到队列服务, 会有三个概念： 发消息者、队列、收消息者，RabbitMQ 在这个基本概念之上, 多做了一层抽象, 在发消息者和 队列之间, 加入了交换器 (Exchange). 这样发消息者和队列就没有直接联系, 转而变成发消息者把消息给交换器, 交换器根据调度策略再把消息再给队列。

生产者往rabbitmq发送消息，rabbitmq包括交换机和序列，消费者从rabbitmq拿消息

那么，其中比较重要的概念有 4 个，分别为：虚拟主机，交换机，队列，和绑定。

>**虚拟主机**：一个虚拟主机持有一组交换机、队列和绑定。为什么需要多个虚拟主机呢？很简单，RabbitMQ当中，用户只能在虚拟主机的粒度进行权限控制。 因此，如果需要禁止A组访问B组的交换机/队列/绑定，必须为A和B分别创建一个虚拟主机。每一个RabbitMQ服务器都有一个默认的虚拟主机“/”。   
>**交换机**：Exchange 用于转发消息，但是它不会做存储 ，如果没有 Queue bind 到 Exchange 的话，它会直接丢弃掉 Producer 发送过来的消息。   
这里有一个比较重要的概念：路由键 。消息到交换机的时候，交互机会转发到对应的队列中，那么究竟转发到哪个队列，就要根据该路由键。   
>**绑定**：也就是交换机需要和队列相绑定，这其中如上图所示，是多对多的关系。

交换机的功能主要是接收消息并且转发到绑定的队列，交换机不存储消息，在启用ack模式后，交换机找不到队列会返回错误。交换机有四种类型：Direct, topic, Headers and Fanout

>**Direct**：direct 类型的行为是"先匹配, 再投送". 即在绑定时设定一个 routing_key, 消息的routing_key 匹配时, 才会被交换器投送到绑定的队列中去.  
>**Topic**：按规则转发消息（最灵活）   
>**Headers**：设置header attribute参数类型的交换机  
>**Fanout**：转发消息到所有绑定队列  


pom.xml
```xml
   	<!-- amqp支持 -->
   		<dependency>
        	<groupId>org.springframework.boot</groupId>
        	<artifactId>spring-boot-starter-amqp</artifactId>
		</dependency>
```

application.properties
```properties
spring.rabbitmq.host=127.0.0.1
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```
### direct

配置
```java
@Configuration
public class DirectRabbitConfig {

    @Bean
    public Queue helloQueue() {
        return new Queue("hello");
    }

    @Bean
    public Queue neoQueue() {
        return new Queue("codinger");
    }

}
```

消息发送
```java
@Component
public class HelloSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send() {
		User user = new User("xiaoming",15);
		System.out.println("Sender : " + user);
		this.rabbitTemplate.convertAndSend("hello", user);
	}

}
```
rabbitTemplate是springboot 提供的默认实现

调用convertAndSort方法 向rabbitmq发送消息 
```java
void convertAndSend(Object message) throws AmqpException;
void convertAndSend(String routingKey, Object message) throws AmqpException;
void convertAndSend(String exchange, String routingKey, Object message) throws AmqpException;
void convertAndSend(Object message, MessagePostProcessor messagePostProcessor) throws AmqpException;
void convertAndSend(String routingKey, Object message, MessagePostProcessor messagePostProcessor)
			throws AmqpException;
void convertAndSend(String exchange, String routingKey, Object message, MessagePostProcessor messagePostProcessor)
			throws AmqpException;			
```

没有指定交换机会发送到默认的交换机(AMQP default)

当发送的消息体为对象时，对象需要可持久化(implements Serializable)

消息接收
```java
@Component
@RabbitListener(queues = "hello")
public class HelloReceiver {

    @RabbitHandler
    public void process(User user) {
        System.out.println("Receiver  : " + user);
    }

}
```

@RabbitListener 注解标注该类为RabbitMQ的消息处理类

使用@RabbitHandler注解标注在方法上，表示当有收到消息的时候，就交给带有@RabbitHandler的方法处理

参考：[https://blog.csdn.net/robertohuang/article/details/79543953](https://blog.csdn.net/robertohuang/article/details/79543953)

测试
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloTest {

	@Autowired
	private HelloSender helloSender;

	@Test
	public void hello() throws Exception {
		helloSender.send();
	}

}
```

多对多的验证

接受者会均匀的接收到消息

### topic

配置
```java
@Configuration
public class TopicRabbitConfig {

    final static String message = "topic.message";
    final static String messages = "topic.messages";

    @Bean
    public Queue queueMessage() {
        return new Queue(TopicRabbitConfig.message);
    }

    @Bean
    public Queue queueMessages() {
        return new Queue(TopicRabbitConfig.messages);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
    }

    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");
    }
}
```

配置了一个叫做 “topicExchange”的topic类型的交换机

将该交换机和 队列(routing_key:topic.mseeage) 以 “topic.message”绑定

将该交换机和 队列(routing_key:topic.mseeages) 以 “topic.#”绑定

>*表示一个词.   
>#表示零个或多个词.

发送，接收类略

测试
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class TopicTest {

	@Autowired
	private TopicSender sender;

	@Test
	public void topic() throws Exception {
		sender.send();
	}

	@Test
	public void topic1() throws Exception {
		sender.send1();
	}

	@Test
	public void topic2() throws Exception {
		sender.send2();
	}

}
```
队列(routing_key:topic.mseeages) 可以接收到 “topic.#”

队列(routing_key:topic.mseeage) 只能接收到 "topic.message"

### fanout

Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。

配置
```java
@Configuration
public class FanoutRabbitConfig {

    @Bean
    public Queue AMessage() {
        return new Queue("fanout.A");
    }

    @Bean
    public Queue BMessage() {
        return new Queue("fanout.B");
    }

    @Bean
    public Queue CMessage() {
        return new Queue("fanout.C");
    }

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    Binding bindingExchangeA(Queue AMessage,FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(AMessage).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeB(Queue BMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(BMessage).to(fanoutExchange);
    }

    @Bean
    Binding bindingExchangeC(Queue CMessage, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(CMessage).to(fanoutExchange);
    }

}
```

将几个队列绑定到 类型为 fanout 的交换机上，无论交换机发送什么消息，队列都能接收到

测试
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class FanoutTest {

	@Autowired
	private FanoutSender sender;

	@Test
	public void fanoutSender() throws Exception {
		sender.send();
	}
}
```











