
添加依赖

```xml
   	<!-- reddis springboot 2.x -->
		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-data-redis</artifactId>
		</dependency>
```

配置文件
```xml
# REDIS (RedisProperties)
# Redis数据库索引（默认为0）
spring.redis.database=0  
# Redis服务器地址
spring.redis.host=192.168.0.58
# Redis服务器连接端口
spring.redis.port=6379  
# Redis服务器连接密码（默认为空）
spring.redis.password=  
# 连接池最大连接数（使用负值表示没有限制）
spring.redis.pool.max-active=8  
# 连接池最大阻塞等待时间（使用负值表示没有限制）
spring.redis.pool.max-wait=-1  
# 连接池中的最大空闲连接
spring.redis.pool.max-idle=8  
# 连接池中的最小空闲连接
spring.redis.pool.min-idle=0  
# 连接超时时间（毫秒）
spring.redis.timeout=0s  
```

或者用yml
```yml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    database: 0
    timeout: 60s  # 数据库连接超时时间，2.0 中该参数的类型为Duration，这里在配置的时候需要指明单位
    # 连接池配置，2.0中直接使用jedis或者lettuce配置连接池
    jedis:
      pool:
        # 最大空闲连接数
        max-idle: 500
        # 最小空闲连接数
        min-idle: 50
        # 等待可用连接的最大时间，负数为不限制
        max-wait:  -1s
        # 最大活跃连接数，负数为不限制
        max-active: -1
```

添加cache的配置类
```java
@Configuration		//声明注解类
@EnableCaching		// 开启缓存支持
public class RedisConfig extends CachingConfigurerSupport{
    
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    //springboot 1.x
//    @SuppressWarnings("rawtypes")
//    @Bean
//    public CacheManager cacheManager(RedisOperations redisOperations) {
//    	RedisCacheManager rcm = new RedisCacheManager(redisOperations);
//        //设置缓存过期时间
//        //rcm.setDefaultExpiration(60);//秒
//        return rcm;
//    }
    
    //springboot 2.x
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheManager cacheManager = RedisCacheManager.create(factory);
        return cacheManager;
    }
    
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}

```

测试类
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
	@Autowired
	private RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        stringRedisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
    }
    
    @Test
    public void testObj() throws Exception {
        User user=new User("aa", "aa", "aa@163.com");
        ValueOperations<String, User> operations=redisTemplate.opsForValue();
        operations.set("com.codinger", user);
    }
    
    
}
class User {
	private String userName;
	private String passWord;
	private String email;
	
	public User(String userName, String passWord, String email) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.email = email;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
```

下载reddis desktop manager,打开，可以看到生成了string类型的键值对   aaa - 111 和 com.codinger - user    
上面是主动向reddis写缓存  

调用方法的时候自动缓存
```java
@RestController
public class RedisController {

    @RequestMapping("/testCache")
    @Cacheable(value="testCache")
    //@Cacheable  方法执行前先查看缓存中是否有数据，有，不执行方法体，直接返回缓存数据；
    //没有，调用方法体返回结果并将结果存入缓存
    public String testCache() {
    	System.out.println("方法体被执行");  
        return "123";
    }
}
```

浏览器第一次访问/testcache 路径时，可以看到缓存的生成
![](_v_images/_1539068226_18709.png)










