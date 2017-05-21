package com.crush.compiler;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

public class ViewBinder {
    String className;
    String packageName;
    ClassName CLASS_NAME;
    TypeElement classElement;
    PackageElement packageElement;
    Element element;
    Elements elementUtils;
    String variableName;
    TypeMirror typeMirror;
    String type;


    public ViewBinder(Element element, Elements elementUtils) {
        this.element = element;
        this.elementUtils = elementUtils;
        this.classElement = (TypeElement) element.getEnclosingElement();
        this.packageElement = elementUtils.getPackageOf(classElement);
        this.className = classElement.getSimpleName().toString();
        this.packageName = packageElement.getQualifiedName().toString();
        this.CLASS_NAME = ClassName.get(packageName, className);
        this.variableName = element.getSimpleName().toString();
        this.typeMirror = element.asType();
        this.type = typeMirror.toString();
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

    public TypeElement getClassElement() {
        return classElement;
    }

    public void setClassElement(TypeElement classElement) {
        this.classElement = classElement;
    }

    public PackageElement getPackageElement() {
        return packageElement;
    }

    public void setPackageElement(PackageElement packageElement) {
        this.packageElement = packageElement;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public String getFullName() {
        return packageName + "." + className;
    }
}