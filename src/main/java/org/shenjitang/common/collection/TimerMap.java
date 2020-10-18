/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shenjitang.common.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xiaolie33
 */
public class TimerMap<T> implements Runnable {
    private static final Log logger = LogFactory.getLog(TimerMap.class);
    private long interval = 5000L; //检查周期
    private Map<T, Item> map = new HashMap(); //集合
    private Thread th;
    

    public TimerMap() {
    }
    
    public void start() {
        th = new Thread(this, "TimerMap-check");
        th.start();
    }
    
    public synchronized void add(T obj, long lifetime, RejectListener<T> rejectlistener)  {
        Item item = new Item(obj, lifetime, rejectlistener);
        map.put(obj, item);
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
                    Iterator<Map.Entry<T, Item>> it = map.entrySet().iterator(); 
                    while(it.hasNext()) { 
                        Map.Entry<T, Item> entry= it.next();
                        Item item= entry.getValue();
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
        TimerMap<String> tList = new TimerMap();
        tList.setInterval(1000L);
        tList.start();
        tList.add("3", 1000, TimerList::reject);
        tList.add("1", 2000, TimerList::reject);
        tList.add("2", 3000, TimerList::reject);
        tList.add("3", 4000, TimerList::reject);
    }
    
    public static String reject(String str, long lifetime) {
        System.out.println(str + " is registed . lifetime:" + lifetime);
        return str;
    }    
}
