package com.xywg.test.ceshi.thread;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 8:33 2019/3/14
 * Modified By : wangyifei
 */
public abstract class BaseThread extends Thread {

    protected final String sn;
    protected  final long sleep;

    public BaseThread(String sn, long sleep) {
        this.sn = sn;
        this.sleep = sleep;
    }
}
