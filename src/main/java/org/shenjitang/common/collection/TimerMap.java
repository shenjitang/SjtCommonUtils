/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shenjitang.common.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xiaolie33
 */
public class TimerMap<K, T> implements Runnable, Map<K, T> {
    private static final Log logger = LogFactory.getLog(TimerMap.class);
    private long interval = 5000L; //检查周期
    private final Map<K, Item> map = new HashMap(); //集合
    private final Thread th;
    private final RejectListener<K, T> rejectlistener;
    private final Long lifetime;
    

    public TimerMap(RejectListener<K, T> rejectlistener, Long lifetime) {
        this.rejectlistener = rejectlistener;
        this.lifetime = lifetime;
        this.th = new Thread(this, "TimerMap-check");
    }
    
    public void start() {
        th.start();
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
                List<Map.Entry<K, Item>> matchList = new ArrayList();
                synchronized(this) {
                    Iterator<Map.Entry<K, Item>> it = map.entrySet().iterator(); 
                    while(it.hasNext()) { 
                        Map.Entry<K, Item> entry= it.next();
                        Item item= entry.getValue();
                        if (System.currentTimeMillis() - item.inTime > lifetime) {
                            matchList.add(entry);
                            it.remove();
                        }
                    }
                }
                for (Map.Entry<K, Item> entry : matchList) {
                    Item item = entry.getValue();
                    try {
                        if (rejectlistener != null) {
                            rejectlistener.reject(entry.getKey(), item.obj);
                        }
                    } catch (Exception e) {
                        logger.warn("", e);
                    }
                }
            } catch (Exception e) {
                logger.warn("", e);
            }
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object arg0) {
        return map.containsKey(arg0);
    }

    @Override
    public boolean containsValue(Object arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        /*
        Iterator<Map.Entry<K, Item>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, Item> entry = it.next();
            Item item = entry.getValue();
            if (item.obj.equals(arg0)) {
                return true;
            }
        }
        return false;
        */
    }



    @Override
    public synchronized Object put(Object arg0, Object arg1) {
        Item item = new Item((T)arg1);
        map.put((K)arg0, item);
        return arg1;
    }


    @Override
    public void putAll(Map arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set keySet() {
        return map.keySet();
    }

    @Override
    public Collection values() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T get(Object arg0) {
        Item item = map.get(arg0);
        if (item != null) {
            return map.get(arg0).obj;
        }
        return null;
    }

    @Override
    public T remove(Object arg0) {
        return map.remove(arg0).obj;
    }
    
    public interface RejectListener<K, T> {
        T reject(K key, T obj);
    }
    
    public class Item {
        public T obj;
        public long inTime = System.currentTimeMillis();

        public Item(T obj) {
            this.obj = obj;
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        TimerMap<String, String> tMap = new TimerMap(TimerMap::reject, 3000L);
        tMap.setInterval(1000L);
        tMap.start();
        tMap.put("3", "three");
        Thread.sleep(1000l);
        tMap.put("1", "one");
        Thread.sleep(1000l);
        tMap.put("2", "two");
        Thread.sleep(1000l);
        tMap.put("3", "three");
        Thread.sleep(1000l);
        System.out.println("===" + tMap.get("1"));
        System.out.println("===" + tMap.get("2"));
        System.out.println("===" + tMap.get("3"));
        Thread.sleep(1000l);
        System.out.println("===" + tMap.get("1"));
        System.out.println("===" + tMap.get("2"));
        System.out.println("===" + tMap.get("3"));
        Thread.sleep(1000l);
        System.out.println("===" + tMap.get("1"));
        System.out.println("===" + tMap.get("2"));
        System.out.println("===" + tMap.get("3"));
    }
    
    public static Object reject(Object key, Object str) {
        System.out.println(key + ":" + str + " is registed .");
        return str;
    }    
}
