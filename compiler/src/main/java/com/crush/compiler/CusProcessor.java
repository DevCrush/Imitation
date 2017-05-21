package com.crush.compiler;


import com.crush.annotation.BindView;
import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * Debug command
 * ./gradlew --no-daemon -Dorg.gradle.debug=true :app:clean :app:compileDebugJavaWithJavac
 */
@AutoService(Processor.class)
public class CusProcessor extends AbstractProcessor {
    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> a : getSupportedAnnotations()) {
            types.add(a.getCanonicalName());
        }
        return types;
    }

    public Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> types = new LinkedHashSet<>();
        types.add(BindView.class);
        return types;
    }

    Messager messager;
    Elements elementUtils;
    Filer filer;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
//        for (TypeElement te : set) {
//            if (!SuperficialValidation.validateElement(te)) continue;
//            logv(te.toString());
//            generateBindView(te.getEnclosingElement());
//        }
        for (Element element : env.getElementsAnnotatedWith(BindView.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            logv(element.toString());
            generateBindView(element);
        }
        return true;
    }

    private static final ClassName UI_THREAD = ClassName.get("android.support.annotation", "UiThread");
    //    private static final ClassName ACTIVITY = ClassName.get("android.app", "Activity");
    private static final ClassName VIEW = ClassName.get("android.view", "View");

    private void generateBindView(Element element) {
        //ElementType.FIELD注解可以直接强转VariableElement
        VariableElement variableElement = (VariableElement) element;
        TypeElement classElement = (TypeElement) element.getEnclosingElement();
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        //类名
        String className = classElement.getSimpleName().toString();
        //包名
        String packageName = packageElement.getQualifiedName().toString();
        //类成员名
        String variableName = variableElement.getSimpleName().toString();
        //类成员类型
        TypeMirror typeMirror = variableElement.asType();
        String type = typeMirror.toString();

        final ClassName ACTIVITY = ClassName.get(packageName, className);


        MethodSpec methodSpec = MethodSpec.methodBuilder("bindView") //方法名
                .addModifiers(PUBLIC)//Modifier 修饰的关键字
                .addAnnotation(UI_THREAD)
//                .addParameter(TypeName.get(classElement.asType()), "activity")
                .addStatement("activity." + variableName + " = (" + type + ")activity.findViewById($L)", variableElement.getAnnotation(BindView.class).value())
                .build();
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC)
                .addParameter(ACTIVITY, "activity")
                .addParameter(VIEW, "view")
                .addStatement("this.$N = $N", "activity", "activity")
                .addStatement("this.$N = $N", "view", "view")
                .addStatement("bindView()")
                .build();
        TypeSpec typeSpec = TypeSpec.classBuilder(className + "$$ViewBinding")
                .addModifiers(PUBLIC, FINAL)
                .addField(ACTIVITY, "activity")
                .addField(VIEW, "view")
                .addMethod(constructor)
                .addMethod(methodSpec)
                .build();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> annotationClass,
                                                   String targetThing, Element element) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

        // Verify method modifiers.
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
//            error(element, "@%s %s must not be private or static. (%s.%s)",
//                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
//                    element.getSimpleName());
            hasError = true;
        }

        // Verify containing type.
        if (enclosingElement.getKind() != CLASS) {
//            error(enclosingElement, "@%s %s may only be contained in classes. (%s.%s)",
//                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
//                    element.getSimpleName());
            hasError = true;
        }

        // Verify containing class visibility is not private.
        if (enclosingElement.getModifiers().contains(PRIVATE)) {
//            error(enclosingElement, "@%s %s may not be contained in private classes. (%s.%s)",
//                    annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(),
//                    element.getSimpleName());
            hasError = true;
        }

        return hasError;
    }

    private boolean isBindingInWrongPackage(Class<? extends Annotation> annotationClass,
                                            Element element) {
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        String qualifiedName = enclosingElement.getQualifiedName().toString();

        if (qualifiedName.startsWith("android.")) {
//            error(element, "@%s-annotated class incorrectly in Android framework package. (%s)",
//                    annotationClass.getSimpleName(), qualifiedName);
            return true;
        }
        if (qualifiedName.startsWith("java.")) {
//            error(element, "@%s-annotated class incorrectly in Java framework package. (%s)",
//                    annotationClass.getSimpleName(), qualifiedName);
            return true;
        }

        return false;
    }

    public void logv(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    public void logw(String msg) {
        messager.printMessage(Diagnostic.Kind.WARNING, msg);
    }

    public void logi(String msg) {
        messager.printMessage(Diagnostic.Kind.OTHER, msg);
    }

    public void loge(String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, msg);
    }

}
