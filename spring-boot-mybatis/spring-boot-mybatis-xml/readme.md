
添加依赖
```xml
 	<!-- mysql connector -->
  		<dependency>
	        <groupId>mysql</groupId>
	        <artifactId>mysql-connector-java</artifactId>
	    </dependency>
	<!-- mybatis -->
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>1.3.1</version>
		</dependency>
```

因为
```xml
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.5.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
```

所以mysql的依赖不需要写版本号，parent已经依赖了版本号，显示的写会覆盖，不推荐覆盖

添加mybatis-generator支持
```xml
<!-- mybatis generator -->
			<plugin>
               <groupId>org.mybatis.generator</groupId>
               <artifactId>mybatis-generator-maven-plugin</artifactId>
               <version>1.3.2</version>
               <executions>
                   <execution>
                       <id>Generate MyBatis Artifacts</id>
                       <phase>deploy</phase>
                       <goals>
                           <goal>generate</goal>
                       </goals>
                   </execution>
               </executions>
               <configuration>
                   <!-- generator 工具配置文件的位置 -->
                   <configurationFile>src/main/resources/mybatis-generator/generatorConfig.xml</configurationFile>
                   <verbose>true</verbose>
                   <overwrite>true</overwrite>
               </configuration>
               <dependencies>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>5.1.34</version>
                    </dependency>
                    <dependency>
                        <groupId>org.mybatis.generator</groupId>
                        <artifactId>mybatis-generator-core</artifactId>
                        <version>1.3.2</version>
                    </dependency>
               </dependencies>
           </plugin>
           <!-- mybatis generator -->
           <plugin>
               <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-maven-plugin</artifactId>
               <configuration>
                   <classifier>exec</classifier>
               </configuration>
       </plugin>
```

application.properties
```properties
mybatis.config-locations=classpath:mybatis/mybatis-config.xml
mybatis.mapper-locations=classpath:mybatis/mapper/*.xml
#扫描pojo类的位置,在此处指明扫描实体类的包，在mapper中就可以不用写pojo类的全路径名了
mybatis.type-aliases-package=com.codinger.entity

spring.datasource.url=jdbc:mysql://localhost:3306/test
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

mybatis配置文件 mybatis-config.xml(可选)
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<typeAliases>
		<typeAlias alias="integer" type="java.lang.Integer" />
		<typeAlias alias="long" type="java.lang.Long" />
		<typeAlias alias="hashMap" type="java.util.HashMap" />
		<typeAlias alias="linkedHashMap" type="java.util.LinkedHashMap" />
		<typeAlias alias="arrayList" type="java.util.ArrayList" />
		<typeAlias alias="linkedList" type="java.util.LinkedList" />
	</typeAliases>
</configuration>
```

users.sql
```sql
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `userName` varchar(32) DEFAULT NULL COMMENT '用户名',
  `passWord` varchar(32) DEFAULT NULL COMMENT '密码',
  `user_sex` varchar(32) DEFAULT NULL,
  `nick_name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

在resurces目录下新建mybatis-generator路径  
mybatis-generator配置文件  
generatorConfig.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<!-- 配置生成器 -->
<generatorConfiguration>
    <!--执行generator插件生成文件的命令： call mvn mybatis-generator:generate -e -->
    <!-- 引入配置文件 -->
    <properties resource="mybatis-generator/mybatisGeneratorinit.properties"/>
    <!--classPathEntry:数据库的JDBC驱动,换成你自己的驱动位置 可选 -->
    <!--<classPathEntry location="D:\generator_mybatis\mysql-connector-java-5.1.24-bin.jar" /> -->

    <!-- 一个数据库一个context -->
    <!--defaultModelType="flat" 大数据字段，不分表 -->
    <context id="MysqlTables" targetRuntime="MyBatis3Simple" defaultModelType="flat">
        <!-- 自动识别数据库关键字，默认false，如果设置为true，根据SqlReservedWords中定义的关键字列表；
        一般保留默认值，遇到数据库关键字（Java关键字），使用columnOverride覆盖 -->
        <property name="autoDelimitKeywords" value="true" />
        <!-- 生成的Java文件的编码 -->
        <property name="javaFileEncoding" value="utf-8" />
        <!-- beginningDelimiter和endingDelimiter：指明数据库的用于标记数据库对象名的符号，比如ORACLE就是双引号，MYSQL默认是`反引号； -->
        <property name="beginningDelimiter" value="`" />
        <property name="endingDelimiter" value="`" />

        <!-- 格式化java代码 -->
        <property name="javaFormatter" value="org.mybatis.generator.api.dom.DefaultJavaFormatter"/>
        <!-- 格式化XML代码 -->
        <property name="xmlFormatter" value="org.mybatis.generator.api.dom.DefaultXmlFormatter"/>
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin" />

        <plugin type="org.mybatis.generator.plugins.ToStringPlugin" />

        <!-- 注释 -->
        <commentGenerator >
            <property name="suppressAllComments" value="false"/><!-- 是否取消注释 -->
            <property name="suppressDate" value="true" /> <!-- 是否生成注释代时间戳-->
        </commentGenerator>

        <!-- jdbc连接 -->
        <jdbcConnection driverClass="${jdbc_driver}" connectionURL="${jdbc_url}" userId="${jdbc_user}" password="${jdbc_password}" />
        <!-- 类型转换 -->
        <javaTypeResolver>
            <!-- 是否使用bigDecimal， false可自动转化以下类型（Long, Integer, Short, etc.） -->
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!-- 生成实体类地址 -->
        <javaModelGenerator targetPackage="com.codinger.entity" targetProject="${project}" >
            <property name="enableSubPackages" value="false"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
        <!-- 生成mapxml文件 -->
        <sqlMapGenerator targetPackage="mybatis/mapper" targetProject="${resources}" >
            <property name="enableSubPackages" value="false" />
        </sqlMapGenerator>
        <!-- 生成mapxml对应client，也就是接口dao -->
        <javaClientGenerator targetPackage="com.codinger.mapper" targetProject="${project}" type="XMLMAPPER" >
            <property name="enableSubPackages" value="false" />
        </javaClientGenerator>
        <!-- table可以有多个,每个数据库中的表都可以写一个table，tableName表示要匹配的数据库表,
        	也可以在tableName属性中通过使用%通配符来匹配所有数据库表,只有匹配的表才会自动生成文件
        	domianOejectName 可以自定义生成文件的名字 -->
        <table tableName="users" domainObjectName="User" enableCountByExample="true" 
            enableUpdateByExample="true" enableDeleteByExample="true" enableSelectByExample="true" 
            selectByExampleQueryId="true">
            <property name="useActualColumnNames" value="false" />
            <!-- 数据库表主键 -->
            <generatedKey column="id" sqlStatement="Mysql" identity="true" />
        </table>
    </context>
</generatorConfiguration>
```
**需要注意此处配置的mapper xml 文件生成位置 mybatis/mapper 需要和application.properties 中mybatis.mapper-locations 的值完全一致**

mybatisGeneratorinit.properties
```xml
#Mybatis Generator configuration
#dao类和实体类的位置
project =src/main/java
#mapper文件的位置
resources=src/main/resources
#根据数据库中的表生成对应的pojo类、dao、mapper
jdbc_driver =com.mysql.jdbc.Driver
jdbc_url=jdbc:mysql://localhost:3306/test
jdbc_user=root
jdbc_password=123456
```

选中项目  run as --> maven build 键入: mybatis-generator:generate  
生成mapper xml文件  mapper文件  entity文件

测试类：
```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

	@Autowired
	private UserMapper UserMapper;

	@Test
	public void testInsert() throws Exception {
		UserMapper.insert(new User("a1","111111","man","a1"));
		UserMapper.insert(new User("a2","222222","man","a2"));
		UserMapper.insert(new User("a3","333333","man","a3"));

		Assert.assertEquals(3, UserMapper.selectAll().size());
	}

	@Test
	public void testQuery() throws Exception {
		List<User> users = UserMapper.selectAll();
		if(users==null || users.size()==0){
			System.out.println("is null");
		}else{
			System.out.println(users.toString());
		}
	}
	
	
	@Test
	public void testUpdate() throws Exception {
		User user = UserMapper.selectByPrimaryKey(2l);
		System.out.println(user.toString());
		user.setNickName("codinger");
		UserMapper.updateByPrimaryKey(user);
		Assert.assertTrue(("codinger".equals(UserMapper.selectByPrimaryKey(2l).getNickName())));
	}

}
```

参考：
https://blog.csdn.net/travellersy/article/details/78620247








