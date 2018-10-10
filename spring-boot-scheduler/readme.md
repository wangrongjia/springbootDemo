
springboot-starter中默认实现了定时任务

在启动程序上添加注解开启定时任务
```java
@SpringBootApplication
@EnableScheduling
public class Application {
```

定时任务
```java
@Component
public class SchedulerTask1 {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron="*/6 * * * * ?")
    public void reportCurrentTime() {
        System.out.println("SchedulerTask1：" + dateFormat.format(new Date()));
    }
}
```

在线生成cron表达式
[http://cron.qqe2.com/](http://cron.qqe2.com/)

启动主程序，定时任务就会持续的执行
