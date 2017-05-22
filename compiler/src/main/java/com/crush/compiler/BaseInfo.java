package com.crush.compiler;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class BaseInfo {
    Elements elementUtils;

    Element element;


    ClassName CLASS_NAME;
    String className;

    PackageElement packageElement;
    String packageName;

    TypeElement classElement;


    public BaseInfo(Element element, Elements elementUtils) {
        this.element = element;
        this.elementUtils = elementUtils;
        this.classElement = (TypeElement) element.getEnclosingElement();
        this.className = classElement.getSimpleName().toString();
        this.packageElement = elementUtils.getPackageOf(classElement);
        this.packageName = packageElement.getQualifiedName().toString();
        this.CLASS_NAME = ClassName.get(packageName, className);
    }

    public Elements getElementUtils() {
        return elementUtils;
    }

    public void setElementUtils(Elements elementUtils) {
        this.elementUtils = elementUtils;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public ClassName getCLASS_NAME() {
        return CLASS_NAME;
    }

    public void setCLASS_NAME(ClassName CLASS_NAME) {
        this.CLASS_NAME = CLASS_NAME;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public PackageElement getPackageElement() {
        return packageElement;
    }

    public void setPackageElement(PackageElement packageElement) {
        this.packageElement = packageElement;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Element getClassElement() {
        return classElement;
    }

    public void setClassElement(TypeElement classElement) {
        this.classElement = classElement;
    }

    public String getFullName() {
        return packageName + "." + className;
    }
}