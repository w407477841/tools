
启动 参数:
--netty.port= 本地端口
--netty.iotNettyUrl  转发主机ip
--netty.iotNettyPort 转发主机端口

示例  监听本地9999  转发到 192.168.1.64的9001
java -jar tcpforward-0.0.1-SNAPSHOT.jar  --netty.port=9999  --netty.iotNettyUrl =192.168.1.64 --netty.iotNettyPort = 9001