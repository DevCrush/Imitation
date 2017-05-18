package com.crush.compiler;

import com.crush.annotation.BindView;
import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.Modifier.PRIVATE;
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

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {

        for (Element element : env.getElementsAnnotatedWith(BindView.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            logv(element.toString());
            generateBindView(  element.getEnclosingElement());
        }
        return true;
    }

    private void generateBindView(Element element) {
//        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        String name = element.getSimpleName().toString();
        boolean hasError = isInaccessibleViaGeneratedCode(BindView.class, "fields", element)
                || isBindingInWrongPackage(BindView.class, element);

        // Verify that the target type extends from View.
        TypeMirror elementType = element.asType();
        if (elementType.getKind() == TypeKind.TYPEVAR) {
            TypeVariable typeVariable = (TypeVariable) elementType;
            elementType = typeVariable.getUpperBound();
        }
//        TypeSpec.Builder typeSpec = TypeSpec.classBuilder();//HelloWorld是类名
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
