package com.crush.bus;

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
    class Bean {
        Method m;
        Object o;

        public Bean(Method m, Object o) {
            this.m = m;
            this.o = o;
        }

        public Object getO() {
            return o;
        }

        public Method getM() {
            return m;
        }

        public void setM(Method m) {
            this.m = m;
        }
    }

    private Map<String, List<Bean>> subscribers = new HashMap<>();

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
                List<Bean> registeredBeans = subscribers.get(parameterTypeName);
                if (null == registeredBeans) {
                    registeredBeans = new ArrayList<>();
                }
                registeredBeans.add(new Bean(m, subscriber));
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
            List<Bean> beans = subscribers.get(s);
            for (Bean b : beans) {
                if (b.getO().equals(subscriber)) {
                    beans.remove(b);
                }
            }
            if (beans.isEmpty()) {
                subscribers.remove(s);
            }
        }
    }

    public void postEvent(Object event) {
        String parameterTypeName = event.getClass().getCanonicalName();
        List<Bean> beenList = subscribers.get(parameterTypeName);
        if (null == beenList)
            return;
        for (Bean b : beenList) {
            try {
                b.getM().invoke(b.getO(), event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                beenList.remove(b);
            }
        }
    }
}
