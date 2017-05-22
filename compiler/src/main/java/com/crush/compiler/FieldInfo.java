package com.crush.compiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class FieldInfo extends BaseInfo {
    VariableElement variableElement;
    String variableName;
    TypeMirror typeMirror;
    String type;


    public FieldInfo(Element element, Elements elementUtils) {
        super(element, elementUtils);
        this.variableElement = (VariableElement) element;
        this.variableName = variableElement.getSimpleName().toString();
        this.typeMirror = element.asType();
        this.type = typeMirror.toString();
    }

    public TypeMirror getTypeMirror() {
        return typeMirror;
    }

    public void setTypeMirror(TypeMirror typeMirror) {
        this.typeMirror = typeMirror;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}