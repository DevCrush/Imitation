package com.crush.bus;

import android.os.Handler;
import android.os.Looper;

import com.crush.bus.annotation.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Crush on 5/22/17.
 */

public class Bus {
    private class BusBean {
        Method method;
        Object subscriber;

        public BusBean(Method method, Object subscriber) {
            this.method = method;
            this.subscriber = subscriber;
        }

        public Object getSubscriber() {
            return subscriber;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }
    }

    private Map<String, List<BusBean>> subscribers = new HashMap<>();

    public static Bus createNewBus() {
        return new Bus();
    }

    public void register(Object subscriber) {
        Class clazz = subscriber.getClass();
        Method[] method = clazz.getDeclaredMethods();
        boolean hasMethod = false;
        for (Method m : method) {
            if (m.isAnnotationPresent(Subscribe.class)) {
                Class[] classes = m.getParameterTypes();
                if (classes.length != 1) {
                    throw new RuntimeException("must have one parameter");
                }
                String parameterTypeName = classes[0].getCanonicalName();
                List<BusBean> registeredBeans = subscribers.get(parameterTypeName);
                if (null == registeredBeans) {
                    registeredBeans = new ArrayList<>();
                }
                registeredBeans.add(new BusBean(m, subscriber));
                subscribers.put(parameterTypeName, registeredBeans);
                hasMethod = true;
            }
        }
        if (!hasMethod) {
            throw new RuntimeException("register error, Must have method with annotation " + Subscribe.class.getCanonicalName() + " in subscriber:");
        }

    }

    public void unregister(Object subscriber) {
        for (String s : subscribers.keySet()) {
            List<BusBean> beans = subscribers.get(s);
            for (BusBean b : beans) {
                if (b.getSubscriber().equals(subscriber)) {
                    beans.remove(b);
                }
            }
            if (beans.isEmpty()) {
                subscribers.remove(s);
            }
        }
    }

    Handler handler = new Handler(Looper.getMainLooper());

    public synchronized void postOnMainThread(final Object event) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                postEvent(event);
            }
        });
    }

    public synchronized void postOnNewThread(final Object event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                postEvent(event);
            }
        }).start();
    }

    public synchronized void postEvent(Object event) {
        String parameterTypeName = event.getClass().getCanonicalName();
        List<BusBean> beenList = subscribers.get(parameterTypeName);
        if (null == beenList)
            return;
        for (BusBean b : beenList) {
            try {
                b.getMethod().invoke(b.getSubscriber(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                beenList.remove(b);
            }
        }
    }
}
