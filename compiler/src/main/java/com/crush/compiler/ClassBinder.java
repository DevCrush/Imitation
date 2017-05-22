package com.crush.compiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Crush on 5/22/17.
 */

public class ClassBinder {
    List<FieldInfo> fieldInfos = new ArrayList<>();
    List<MethodInfo> methodInfos = new ArrayList<>();

    public ClassBinder() {
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
