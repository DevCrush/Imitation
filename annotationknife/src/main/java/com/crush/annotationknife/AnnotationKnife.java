package com.crush.annotationknife;

import android.app.Activity;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Crush on 2017/5/21.
 */

public class AnnotationKnife {
    @NonNull
    @UiThread
    public static void bind(@NonNull Activity target) {
        View sourceView = target.getWindow().getDecorView();
        createBinding(target, sourceView);
    }

    @NonNull
    @UiThread
    public static void bind(@NonNull View sourceView, Object target) {
        createBinding(target, sourceView);
    }

    private static void createBinding(@NonNull Object target, @NonNull View source) {
        Class<?> targetClass = target.getClass();
        Constructor constructor = findBindingConstructorForClass(targetClass);
        //noinspection TryWithIdenticalCatches Resolves to API 19+ only type.
        try {
            constructor.newInstance(target, source);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to invoke " + constructor, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException("Unable to create binding instance.", cause);
        }
    }

    @Nullable
    @CheckResult
    @UiThread
    private static Constructor findBindingConstructorForClass(Class<?> cls) {
        Constructor bindingCtor;
        String clsName = cls.getName();
        try {
            Class<?> bindingClass = cls.getClassLoader().loadClass(clsName + "$$ViewBinding");
            //noinspection unchecked
            bindingCtor = bindingClass.getConstructor(cls, View.class);
        } catch (ClassNotFoundException e) {
            bindingCtor = findBindingConstructorForClass(cls.getSuperclass());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + clsName, e);
        }
//        BINDINGS.put(cls, bindingCtor);
        return bindingCtor;
    }
}
