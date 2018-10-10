
pom.xml 同单数据源
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
不需要写mybatis.config-locations 和 mybatis.mapper-locations 这两项
```properties
spring.datasource.test1.driverClassName = com.mysql.jdbc.Driver
spring.datasource.test1.jdbc-url = jdbc:mysql://localhost:3306/test1?useUnicode=true&characterEncoding=utf-8
spring.datasource.test1.username = root
spring.datasource.test1.password = 123456

spring.datasource.test2.driverClassName = com.mysql.jdbc.Driver
spring.datasource.test2.jdbc-url = jdbc:mysql://localhost:3306/test2?useUnicode=true&characterEncoding=utf-8
spring.datasource.test2.username = root
spring.datasource.test2.password = 123456
```

springboot 2.x 中 多数据源使用时，需要将spring.datasource.xxx.url改成spring.datasource.xxx.jdbc-url,否则会有异常  
参考：  
[https://my.oschina.net/chinesedragon/blog/1647846](https://my.oschina.net/chinesedragon/blog/1647846)

这两项放到配置类中配置
```java
@Configuration
@MapperScan(basePackages = "com.codinger.mapper.test1", sqlSessionTemplateRef  = "test1SqlSessionTemplate")
public class DataSource1Config {

    @Bean(name = "test1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.test1")
    @Primary
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "test1SqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("test1DataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/test1/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "test1TransactionManager")
    @Primary
    public DataSourceTransactionManager testTransactionManager(@Qualifier("test1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "test1SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("test1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
```
**springboot 1.x 和 springboot 2.x 中 DataSourceBuilder 包名不同**
DataSource2Config略

mybatis-generator和单数据源基本相同，要根据不同的数据源切换

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

        <!-- jdbc连接 test1-->
         <jdbcConnection driverClass="${jdbc_driver}" connectionURL="${jdbc_url.test1}" userId="${jdbc_user.test1}" password="${jdbc_password.test1}" />  
        
        <!-- jdbc连接 test2-->
         <!-- <jdbcConnection driverClass="${jdbc_driver}" connectionURL="${jdbc_url.test2}" userId="${jdbc_user.test2}" password="${jdbc_password.test2}" /> -->
        
        <!-- 类型转换 -->
        <javaTypeResolver>
            <!-- 是否使用bigDecimal， false可自动转化以下类型（Long, Integer, Short, etc.） -->
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>

        <!-- 生成实体类地址 因为两个数据源表结构相同，所以只要一个实体类就可以-->
        <javaModelGenerator targetPackage="com.codinger.entity" targetProject="${project}" >
            <property name="enableSubPackages" value="false"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
        <!-- 生成mapxml文件 test1-->
          <sqlMapGenerator targetPackage="mybatis/mapper/test1" targetProject="${resources}" >
            <property name="enableSubPackages" value="false" />
        </sqlMapGenerator>  
        
		<!-- 生成mapxml文件 test2-->
         <!-- <sqlMapGenerator targetPackage="mybatis/mapper/test2" targetProject="${resources}" >
            <property name="enableSubPackages" value="false" />
        </sqlMapGenerator> --> 
        
        <!-- 生成mapxml对应client，也就是接口dao test1-->
         <javaClientGenerator targetPackage="com.codinger.mapper.test1" targetProject="${project}" type="XMLMAPPER" >
            <property name="enableSubPackages" value="false" />
        </javaClientGenerator> 
        
        <!-- 生成mapxml对应client，也就是接口dao test2-->
         <!-- <javaClientGenerator targetPackage="com.codinger.mapper.test2" targetProject="${project}" type="XMLMAPPER" >
            <property name="enableSubPackages" value="false" />
        </javaClientGenerator>  -->
        
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

```properties
#Mybatis Generator configuration
#dao类和实体类的位置
project =src/main/java
#mapper文件的位置
resources=src/main/resources
#根据数据库中的表生成对应的pojo类、dao、mapper
jdbc_driver =com.mysql.jdbc.Driver

jdbc_url.test1=jdbc:mysql://localhost:3306/test
jdbc_user.test1=root
jdbc_password.test1=123456

jdbc_url.test2=jdbc:mysql://localhost:3306/test
jdbc_user.test2=root
jdbc_password.test2=123456
```

分两次分别生成不同的数据源对应的文件

测试，同单数据源








