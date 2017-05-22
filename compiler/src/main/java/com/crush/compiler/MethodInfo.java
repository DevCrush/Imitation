package com.crush.compiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;

public class MethodInfo extends BaseInfo {
    ExecutableElement executableElement;
    String methodName;
    List<? extends VariableElement> methodParameters;
    Map<String, String> parameter = new HashMap<>();

    public MethodInfo(Element element, Elements elementUtils) {
        super(element, elementUtils);
        this.executableElement = (ExecutableElement) element;
        this.methodName = executableElement.getSimpleName().toString();
        this.methodParameters = executableElement.getParameters();
        //参数类型列表
        for (VariableElement variableElement : methodParameters) {
            TypeMirror methodParameterType = variableElement.asType();
            if (methodParameterType instanceof TypeVariable) {
                TypeVariable typeVariable = (TypeVariable) methodParameterType;
                methodParameterType = typeVariable.getUpperBound();
            }
            //参数名
            String parameterName = variableElement.getSimpleName().toString();
            //参数类型
            String parameteKind = methodParameterType.toString();
            parameter.put(parameterName, parameteKind);
        }

    }

    public ExecutableElement getExecutableElement() {
        return executableElement;
    }

    public void setExecutableElement(ExecutableElement executableElement) {
        this.executableElement = executableElement;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<? extends VariableElement> getMethodParameters() {
        return methodParameters;
    }

    public void setMethodParameters(List<? extends VariableElement> methodParameters) {
        this.methodParameters = methodParameters;
    }

    public Map<String, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }
}