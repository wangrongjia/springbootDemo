这是一个由 SPRING INITIALIZR([https://start.spring.io])在线生成的maven project with java and springboot 2.0.5
其基础结构共三个模块:

>src/main/java  程序开发以及主程序入口

>src/main/resources 配置文件

>src/test/java  测试程序

### step1 pom.xml添加web依赖

```xml
		<dependency>
	        <groupId>org.springframework.boot</groupId>
	        <artifactId>spring-boot-starter-web</artifactId>
 		</dependency>
```

### step2 添加控制层

```java 
@RestController
public class HelloController {

	@RequestMapping(value="/hello")
	public String hello() {
		return "hello!";
	}
}
```

启动应用程序，访问 [http://localhost:8080/hello] 

tips:

所有java文件需要在主启动程序的包内；例如：主程序SpringBootHelloApplication在com.xxx包下，HelloController应该在com.xxx.controller类似这样的包下

### step3 热启动支持

```xml
	    <dependency>
	        <groupId>org.springframework.boot</groupId>
	        <artifactId>spring-boot-devtools</artifactId>
	        <optional>true</optional>
   		</dependency>
```

```xml
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<!-- 热启动支持 -->
	            <configuration>
                	<fork>true</fork>
            	</configuration>
			</plugin>
```





