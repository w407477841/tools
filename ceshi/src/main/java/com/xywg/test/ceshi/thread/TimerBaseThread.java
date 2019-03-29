package com.xywg.test.ceshi.thread;

import com.xywg.test.ceshi.common.Const;
import com.xywg.test.ceshi.common.XySession;
import io.netty.channel.Channel;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:35 2019/3/14
 * Modified By : wangyifei
 */
public class TimerBaseThread extends BaseThread {

    private static final String data = "sdsyr:##,a:057,b:068,c:09.4,d:29.8,e:450,f:,g:,h:0,i:0,j:01.0,k:016,l:0,m:0";

    public TimerBaseThread(String sn, long sleep) {
        super(sn, sleep);
    }

    @Override
    public void run() {
        super.run();
        while (true){
            Channel channel = Const.clientChannels.get(sn);
            if(channel!=null){
                XySession xySession =  channel.attr(Const.NETTY_CHANNEL_KEY).get();
                if(xySession.isOk()){
                    Const.send(channel,data.replace("##",sn));
                }
            }
            try {
                Thread.sleep(sleep*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }





    }
}
