/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shenjitang.common.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 根据寿命丢出集合的集合。本类只有一个检查线程，没有工作线程，所以谨慎设计回调函数，如有必要，在回调函数中自行实现异步处理。
 * @author xiaolie33
 */
public class TimerList<T> implements Runnable {
    private static final Log logger = LogFactory.getLog(TimerList.class);
    private long interval = 5000L; //检查周期
    private List<Item> list = new ArrayList(); //集合
    private Thread th;
    

    public TimerList() {
    }
    
    public void start() {
        th = new Thread(this, "TimerList-check");
        th.start();
    }
    
    public synchronized void add(T obj, int lifetime, RejectListener<String> rejectlistener)  {
        Item item = new Item(obj, lifetime, rejectlistener);
        list.add(item);
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(interval);
                List<Item> matchList = new ArrayList();
                synchronized(this) {
                    Iterator<Item> it = list.iterator(); 
                    while(it.hasNext()) { 
                        Item item= it.next();
                        if (System.currentTimeMillis() - item.inTime > item.lifetime) {
                            matchList.add(item);
                            it.remove();
                        }
                    }
                }
                for (Item item : matchList) {
                    try {
                        item.listener.reject(item.obj, item.lifetime);
                    } catch (Exception e) {
                        logger.warn("", e);
                    }
                }
            } catch (Exception e) {
                logger.warn("", e);
            }
        }
    }
    
    public interface RejectListener<T> {
        T reject(T obj, long lifetime);
    }
    
    public class Item {
        public T obj;
        public long inTime = System.currentTimeMillis();
        public long lifetime;
        public RejectListener listener;

        public Item(T obj, long lifetime, RejectListener listener) {
            this.obj = obj;
            this.lifetime = lifetime;
            this.listener = listener;
        }
    }
    
    public static void main(String[] args) {
        TimerList<String> tList = new TimerList();
        tList.setInterval(1000L);
        tList.start();
        tList.add("3", 4000, TimerList::reject);
        tList.add("1", 2000, TimerList::reject);
        tList.add("2", 3000, TimerList::reject);
    }
    
    public static String reject(String str, long lifetime) {
        System.out.println(str + " is registed . lifetime:" + lifetime);
        return str;
    }
    
}
