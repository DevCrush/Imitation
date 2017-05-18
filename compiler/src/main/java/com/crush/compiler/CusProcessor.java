package com.crush.compiler;

import com.crush.annotation.BindView;
import com.google.auto.service.AutoService;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

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

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }


}
