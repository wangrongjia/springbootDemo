### json 接口开发
在以前的spring 开发的时候需要我们提供json接口的时候需要做那些配置呢

>添加 jackjson 等相关jar包   
>配置spring controller扫描   
>对接的方法添加@ResponseBody   

就这样我们会经常由于配置错误，导致406错误等等，spring boot如何做呢，只需要类添加 @RestController 即可，默认类中的方法都会以json的格式返回

### 配置监听器、过滤器和拦截器

>实现Filter接口，实现Filter方法   
>添加@Configuration 注解，将自定义Filter加入过滤链

自定义filter接口
```java
public class MyFilter implements Filter {
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		// 将请求转换成HttpServletRequest 请求 
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        System.out.println("this is MyFilter,url :"+req.getRequestURI());
        String path = req.getContextPath();
        String basePath = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+path;
        HttpSession session = req.getSession(true);
        String username = (String) session.getAttribute("username");
        if (req.getRequestURI().indexOf("index.html") == -1 &&(username == null || "".equals(username))) {
            resp.setHeader("Cache-Control", "no-store");
            resp.setDateHeader("Expires", 0);
            resp.setHeader("Prama", "no-cache");
            //此处设置了访问静态资源类
            resp.sendRedirect(basePath+"/index.html");
        } else {
            // Filter 只是链式处理，请求依然转发到目的地址。 
            filterChain.doFilter(req, resp);
        }
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
}
```

配置注解类
```java
@Configuration
public class WebConfiguration {
    
    @Bean
    public FilterRegistrationBean<MyFilter> testFilterRegistration() {

        FilterRegistrationBean<MyFilter> registration = new FilterRegistrationBean<MyFilter>();
        registration.setFilter(new MyFilter());
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("MyFilter");
        registration.setOrder(1);
        return registration;
    }
    
}
```

### 配置属性的自定义和取值

application.properties
```xml
com.codinger.name=123
```

取值示例
```java
@Component
public class MyProperties {

	@Value("${com.codinger.name}")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
```

### springboot使用spring data jpa

添加依赖
```xml
  	<!-- spring data jpa -->
 		<dependency>
	        <groupId>org.springframework.boot</groupId>
	        <artifactId>spring-boot-starter-data-jpa</artifactId>
    	</dependency>
   	<!-- mysql connector -->
	    <dependency>
	        <groupId>mysql</groupId>
	        <artifactId>mysql-connector-java</artifactId>
	    </dependency>
```	    

application.properties配置
```xml
spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.jpa.properties.hibernate.hbm2ddl.auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.show-sql= true
```

其实这个**hibernate.hbm2ddl.auto**参数的作用主要用于：自动创建|更新|验证数据库表结构,有四个值：
>**create**： 每次加载hibernate时都会删除上一次的生成的表，然后根据你的model类再重新来生成新表，哪怕两次没有任何改变也要这样执行，这就是导致数据库表数据丢失的一个重要原因。   
>**create-drop** ：每次加载hibernate时根据model类生成表，但是sessionFactory一关闭,表就自动删除。   
>**update**：最常用的属性，第一次加载hibernate时根据model类会自动建立起表的结构（前提是先建立好数据库），以后加载hibernate时根据 model类自动更新表结构，即使表结构改变了但表中的行仍然存在不会删除以前的行。要注意的是当部署到服务器后，表结构是不会被马上建立起来的，是要等 应用第一次运行起来后才会。   
>**validate** ：每次加载hibernate时，验证创建数据库表结构，只会和数据库中的表进行比较，不会创建新表，但是会插入新值。   

添加实体类和dao
```java
@Entity
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false, unique = true)
	private String userName;
	@Column(nullable = false)
	private String passWord;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = true, unique = true)
	private String nickName;
	@Column(nullable = false)
	private String regTime;

    ...
}
```

```java
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findByUserNameOrEmail(String username, String email);
}
```

springboot初始化的时候，会根据User类在mysql数据库中生成user表，jpa的另一个好处是根据方法名直接生成sql

测试

创建测试类
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void test() throws Exception {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);        
		String formattedDate = dateFormat.format(date);
		
		userRepository.save(new User("a1", "111111", "a1@163.com", "a1",formattedDate));
		userRepository.save(new User("a2", "222222", "a2@263.com", "a2",formattedDate));
		userRepository.save(new User("a3", "333333", "a3@363.com", "a3",formattedDate));
		userRepository.save(new User("a4", "444444", "a4@463.com", "a4",formattedDate));
		userRepository.save(new User("a5", "555555", "a5@563.com", "a5",formattedDate));
		userRepository.save(new User("a6", "666666", "a6@663.com", "a6",formattedDate));

		Assert.assertEquals(6, userRepository.findAll().size());
		Assert.assertEquals("a2", userRepository.findByUserNameOrEmail("a2", "a2@163.com").getNickName());
		userRepository.delete(userRepository.findByUserName("a1"));
	}
}
```














