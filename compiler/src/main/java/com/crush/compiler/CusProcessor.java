package com.crush.compiler;


import com.crush.annotation.BindView;
import com.crush.annotation.OnClick;
import com.google.auto.common.SuperficialValidation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedHashSet;
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

        Map<String, ClassBinder> mapField = new HashMap<>();

        for (Element element : env.getElementsAnnotatedWith(BindView.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            logv(element.toString());
            FieldInfo binder = new FieldInfo(element, elementUtils);
            if (null == mapField.get(binder.getFullName())) {
                ClassBinder cb = new ClassBinder();
                cb.getFieldInfos().add(binder);
                mapField.put(binder.getFullName(), cb);
            } else {
                mapField.get(binder.getFullName()).getFieldInfos().add(binder);
            }
        }
        for (Element element : env.getElementsAnnotatedWith(OnClick.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            logv(element.toString());
            MethodInfo binder = new MethodInfo(element, elementUtils);
            if (null == mapField.get(binder.getFullName())) {
                ClassBinder cb = new ClassBinder();
                cb.getMethodInfos().add(binder);
                mapField.put(binder.getFullName(), cb);
            } else {
                mapField.get(binder.getFullName()).getMethodInfos().add(binder);
            }
        }


        MethodSpec.Builder bt = null;
        for (String key : mapField.keySet()) {
            ClassBinder classBinder = mapField.get(key);//获取类中需要绑定的所有对象

            String className = null, packageName = null;
            ClassName CLASS_NAME = null;

            int fieldSize = classBinder.getFieldInfos().size();
            for (int i = 0; i < fieldSize; i++) {
                FieldInfo b = classBinder.getFieldInfos().get(i);
                if (null == className) {
                    className = b.getClassName();
                    packageName = b.getPackageName();
                    CLASS_NAME = b.getCLASS_NAME();
                }
                bt = generateBindView(bt, b);
            }

            int methodSize = classBinder.getMethodInfos().size();
            for (int i = 0; i < methodSize; i++) {
                MethodInfo b = classBinder.getMethodInfos().get(i);
                if (null == className) {
                    className = b.getClassName();
                    packageName = b.getPackageName();
                    CLASS_NAME = b.getCLASS_NAME();
                }
                bt = generateOnClickMethod(bt, b);
            }

            writeCLass(className, packageName, CLASS_NAME, bt);
            bt = null;
        }

        return true;
    }


    private static final ClassName UI_THREAD = ClassName.get("android.support.annotation", "UiThread");
    private static final ClassName VIEW = ClassName.get("android.view", "View");
    private static final ClassName ON_CLICK = ClassName.get("android.view.View", "OnClickListener");

    private MethodSpec.Builder generateBindView(MethodSpec.Builder builder, FieldInfo viewBinder) {
        if (null == builder) {
            builder = MethodSpec.methodBuilder("bindView") //方法名
                    .addModifiers(PUBLIC)//Modifier 修饰的关键字
                    .addAnnotation(UI_THREAD);
        }

        return builder.addStatement("target.$N = ($L)sourceView.findViewById($L)", viewBinder.variableName, viewBinder.type, viewBinder.element.getAnnotation(BindView.class).value());
    }


    private MethodSpec.Builder generateOnClickMethod(MethodSpec.Builder builder, MethodInfo viewBinder) {
        if (null == builder) {
            builder = MethodSpec.methodBuilder("bindView") //方法名
                    .addModifiers(PUBLIC)//Modifier 修饰的关键字
                    .addAnnotation(UI_THREAD);
        }
        String method = viewBinder.methodName + "(";
        for (String name : viewBinder.getParameter().keySet()) {
            if (viewBinder.getParameter().get(name).equals("android.view.View")) {
                method += "v";
            }
        }
        method += ")";
        builder.addStatement("sourceView.findViewById($L).setOnClickListener(new $T(){" +
                "@Override\n" +
                "public void onClick(View v) {" +
                "target.$L;" +
                "}" +
                "});", viewBinder.element.getAnnotation(OnClick.class).value(), ON_CLICK, method);
        return builder;
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
                .addParameter(CLASS_NAME, "target")
                .addParameter(VIEW, "sourceView")
                .addStatement("this.$N = $N", "target", "target")
                .addStatement("this.$N = $N", "sourceView", "sourceView")
                .addStatement("bindView()")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(className + "$$ViewBinding")
                .addModifiers(PUBLIC, FINAL)
                .addField(CLASS_NAME, "target")
                .addField(VIEW, "sourceView")
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
