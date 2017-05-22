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
        Map<String, ClassBinder> mapClassBinder = new HashMap<>();

        for (Element element : env.getElementsAnnotatedWith(BindView.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            logv(element.toString());
            FieldInfo fieldInfo = new FieldInfo(element, elementUtils);
            if (null == mapClassBinder.get(fieldInfo.getFullName())) {
                ClassBinder cb = new ClassBinder(fieldInfo.getClassName(), fieldInfo.getPackageName(), fieldInfo.getCLASS_NAME());
                cb.getFieldInfos().add(fieldInfo);
                mapClassBinder.put(fieldInfo.getFullName(), cb);
            } else {
                mapClassBinder.get(fieldInfo.getFullName()).getFieldInfos().add(fieldInfo);
            }
        }
        for (Element element : env.getElementsAnnotatedWith(OnClick.class)) {
            if (!SuperficialValidation.validateElement(element)) continue;
            logv(element.toString());
            MethodInfo methodInfo = new MethodInfo(element, elementUtils);
            if (null == mapClassBinder.get(methodInfo.getFullName())) {
                ClassBinder cb = new ClassBinder(methodInfo.getClassName(), methodInfo.getPackageName(), methodInfo.getCLASS_NAME());
                cb.getMethodInfos().add(methodInfo);
                mapClassBinder.put(methodInfo.getFullName(), cb);
            } else {
                mapClassBinder.get(methodInfo.getFullName()).getMethodInfos().add(methodInfo);
            }
        }


        MethodSpec.Builder bt = null;
        for (String key : mapClassBinder.keySet()) {
            ClassBinder classBinder = mapClassBinder.get(key);//获取类中需要绑定的所有对象
            int fieldSize = classBinder.getFieldInfos().size();
            for (int i = 0; i < fieldSize; i++) {
                FieldInfo b = classBinder.getFieldInfos().get(i);

                bt = generateBindView(bt, b);
            }

            int methodSize = classBinder.getMethodInfos().size();
            for (int i = 0; i < methodSize; i++) {
                MethodInfo b = classBinder.getMethodInfos().get(i);
                bt = generateOnClickMethod(bt, b);
            }

            writeCLass(classBinder.getClassName(), classBinder.getPackageName(), classBinder.getCLASS_NAME(), bt);
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
