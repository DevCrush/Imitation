package com.crush.compiler;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Crush on 5/22/17.
 */

public class ClassBinder {
    String className;
    String packageName;
    ClassName CLASS_NAME;

    List<FieldInfo> fieldInfos = new ArrayList<>();
    List<MethodInfo> methodInfos = new ArrayList<>();

//    public ClassBinder() {
//    }

    public ClassBinder(String className, String packageName, ClassName CLASS_NAME) {
        this.className = className;
        this.packageName = packageName;
        this.CLASS_NAME = CLASS_NAME;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ClassName getCLASS_NAME() {
        return CLASS_NAME;
    }

    public void setCLASS_NAME(ClassName CLASS_NAME) {
        this.CLASS_NAME = CLASS_NAME;
    }

    public ClassBinder(List<FieldInfo> fieldInfos, List<MethodInfo> methodInfos) {
        this.fieldInfos = fieldInfos;
        this.methodInfos = methodInfos;
    }

    public List<FieldInfo> getFieldInfos() {
        return fieldInfos;
    }

    public void setFieldInfos(List<FieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    public List<MethodInfo> getMethodInfos() {
        return methodInfos;
    }

    public void setMethodInfos(List<MethodInfo> methodInfos) {
        this.methodInfos = methodInfos;
    }
}
