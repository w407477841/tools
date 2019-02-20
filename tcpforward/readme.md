配置文件列表
9001
9011
9012
9013
9060
9071
9072
9095
9096
启动 参数:
--spring.profiles.active  切换配置文件  
--netty.iotNettyUrl  转发主机ip

示例  监听本地9001  转发到 192.168.1.64的9001
java -jar tcpforward-0.0.1-SNAPSHOT.jar  --spring.profiles.active=9001  --netty.iotNettyUrl =192.168.1.64