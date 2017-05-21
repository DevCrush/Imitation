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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

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

        Map<String, List<ViewBinder>> map = new HashMap<>();

        for (Element element : env.getElementsAnnotatedWith(BindView.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            logv(element.toString());
            ViewBinder binder = new ViewBinder(element, elementUtils);
            if (null == map.get(binder.getFullName())) {
                List<ViewBinder> tmp = new ArrayList<>();
                tmp.add(binder);
                map.put(binder.getFullName(), tmp);
            } else {
                map.get(binder.getFullName()).add(binder);
            }
        }

        MethodSpec.Builder bt = null;
        for (String key : map.keySet()) {
            for (int i = 0; i < map.get(key).size(); i++) {
                bt = generateBindView(bt, map.get(key).get(i));
            }
            ViewBinder b0 = map.get(key).get(0);
            writeCLass(b0.className, b0.packageName, b0.CLASS_NAME, bt);
            bt = null;
        }

        return true;
    }


    private static final ClassName UI_THREAD = ClassName.get("android.support.annotation", "UiThread");
    private static final ClassName VIEW = ClassName.get("android.view", "View");

    private MethodSpec.Builder generateBindView(MethodSpec.Builder builder, ViewBinder viewBinder) {
//        //ElementType.FIELD注解可以直接强转VariableElement
//        VariableElement variableElement = (VariableElement) element;
//        TypeElement classElement = (TypeElement) element.getEnclosingElement();
//        PackageElement packageElement = elementUtils.getPackageOf(classElement);
//        //类名
//        String className = classElement.getSimpleName().toString();
//        //包名
//        String packageName = packageElement.getQualifiedName().toString();
//        //类成员名
//        String variableName = variableElement.getSimpleName().toString();
//        //类成员类型
//        TypeMirror typeMirror = variableElement.asType();
//        String type = typeMirror.toString();
        if (null == builder) {
            builder = MethodSpec.methodBuilder("bindView") //方法名
                    .addModifiers(PUBLIC)//Modifier 修饰的关键字
                    .addAnnotation(UI_THREAD);
        }

        return builder.addStatement("activity.$N = ($L)activity.findViewById($L)", viewBinder.variableName, viewBinder.type, viewBinder.element.getAnnotation(BindView.class).value());


    }

    private void writeCLass(String className, String packageName, ClassName CLASS_NAME, MethodSpec.Builder methodSpec) {
        JavaFile javaFile = JavaFile.builder(packageName, generateClass(className, CLASS_NAME, methodSpec.build())).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TypeSpec generateClass(String className, ClassName CLASS_NAME, MethodSpec bindView) {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC)
                .addParameter(CLASS_NAME, "activity")
                .addParameter(VIEW, "view")
                .addStatement("this.$N = $N", "activity", "activity")
                .addStatement("this.$N = $N", "view", "view")
                .addStatement("bindView()")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(className + "$$ViewBinding")
                .addModifiers(PUBLIC, FINAL)
                .addField(CLASS_NAME, "activity")
                .addField(VIEW, "view")
                .addMethod(constructor)
                .addMethod(bindView)
                .build();
        return typeSpec;
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
