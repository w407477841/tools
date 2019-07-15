package com.xywg.test.ceshi.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : wangyifei
 * Description
 * Date: Created in 14:25 2019/6/26
 * Modified By : wangyifei
 */
public class TestThreadFactory implements ThreadFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestThreadFactory.class);

    private final String namePrefix;
    private final AtomicInteger nextId = new AtomicInteger(1);

     TestThreadFactory(String whatFeaturOfGroup) {
        namePrefix = "From TestThreadFactory's " + whatFeaturOfGroup + "-Worker-";
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = namePrefix + nextId.getAndIncrement();
        System.out.println(name);
        Thread thread = new Thread(null, r, name, 0);
        return thread;
    }


    public static void main(String args[]){
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1000);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100,200,10000L,TimeUnit.MILLISECONDS,workQueue,new TestThreadFactory("haha"));
         final AtomicInteger atomicInteger =new AtomicInteger(1);
         int j = 0 ;
         while (j!=10000){
           System.out.println(j++);
            executor.execute(()->{
               int i=  atomicInteger.getAndIncrement();
                System.out.println("启动"+i);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("完毕"+i);
            });
        }
    }

}
