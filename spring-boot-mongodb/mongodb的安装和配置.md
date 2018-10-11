[mongodb下载地址 ](https://www.mongodb.com/download-center/v2/community)

解压后不需要安装，配置bin目录到系统环境变量Path

```bat
mongod -help
```

选择一个盘符，新建一个文件夹用于存放数据库文件，在这里我选择的是在 E 盘的根目录下新建一个名为 MongoDB_Data 的文件夹，然后再在这个文件夹下建立一个名为 db 和一个名为 log 的文件夹，最后在 log 文件夹下建立一个名为 mongodb.log 的文件

cmd 键入:
```bat
mongod --logpath "E:\MongoDB_Data\log\mongodb.log" --logappend --dbpath "E:\MongoDB_Data\db" --directoryperdb --install
```

```bat
net start mongodb
```

mongodb默认端口 27017

在浏览器输入[http://localhost:27017](http://localhost:27017)

参考:[MongoDB下载、安装和配置教程](https://blog.csdn.net/winstonlau/article/details/79439223)

可视化工具：[Robo 3T下载地址](https://robomongo.org/download)

