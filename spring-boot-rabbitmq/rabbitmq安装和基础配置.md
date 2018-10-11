### 1. 安装erlang

[http://www.erlang.org/downloads](http://www.erlang.org/downloads)
环境变量 ERLANG_HOME

### 2. 安装rabbitmq

[http://www.rabbitmq.com/download.html](http://www.rabbitmq.com/download.html)  
rabbitmq默认监听端口 5672

### 3. 配置
#### 激活 RabbitMQ's Management Plugin
 进入sbin目录
 命令行键入
```bat
"rabbitmq-plugins.bat"   enable rabbitmq_management
```
重启服务
```bat
net stop RabbitMQ && net start RabbitMQ
```

查看已有用户
```bat
rabbitmqctl.bat list_users
```
新增用户
```bat
rabbitmqctl.bat add_user username password
```
授予用户角色
```bat
rabbitmqctl.bat set_user_tags username administrator
```
默认账号 guest密码是guest ，更改密码
```bat
rabbitmqctl.bat change_password userName newPassword
```
删除用户
```bat
rabbitmqctl.bat delete_user username
```
授予用户权限

按照官方文档，用户权限指的是用户对exchange，queue的操作权限，包括配置权限，读写权限。

我们配置权限会影响到exchange、queue的声明和删除。

读写权限影响到从queue里取消息、向exchange发送消息以及queue和exchange的绑定(binding)操作。

例如： 将queue绑定到某exchange上，需要具有queue的可写权限，以及exchange的可读权限；向exchange发送消息需要具有exchange的可写权限；从queue里取数据需要具有queue的可读权限

设置权限
```bat
rabbitmqctl  set_permissions  -p  VHostPath  User  ConfP  WriteP  ReadP
```

我们直接授予所有路径下的所有权限(配置，读，写)
```bat
rabbitmqctl  set_permissions  -p  /  admin  '.*'  '.*'  '.*'
```

使用浏览器打开 [http://localhost:15672](http://localhost:15672) 访问Rabbit Mq的管理控制台，使用刚才创建的账号登陆系统：

参考：
[https://www.cnblogs.com/ericli-ericli/p/5902270.html](https://www.cnblogs.com/ericli-ericli/p/5902270.html)



