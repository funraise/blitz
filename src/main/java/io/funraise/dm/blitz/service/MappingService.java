package io.funraise.dm.blitz.service;

import com.google.inject.Injector;
import io.funraise.dm.blitz.ClassMappings;
import io.funraise.dm.blitz.MappingFunction;
import io.funraise.dm.blitz.MappingType;
import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.annotation.PostProcessor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.beanutils.FluentPropertyBeanIntrospector;
import org.apache.commons.beanutils.PropertyUtils;
import org.reflections.Reflections;

/**
 * Created by jackson.brodeur on 8/3/15.
 */

@Singleton
public class MappingService  {

    private Map<Class, ClassMappings> cacheMap = new HashMap<>();
    // class -> object of corresponding mapping class
    private Map<Class, Object> mappingInstanceMap = new HashMap<>();
    private Map<Class<?>, Map<Class<?>, Set<String>>> orikaExcludes = new HashMap<>();

    private Injector inject;

    @Inject
    private MappingService(Injector inject) {
        this.inject = inject;
    }

    public void loadMappings(String packagePrefix, ClassLoader classLoader) throws Exception {

        Set<Class<?>> beansWithAnnotation = new Reflections(packagePrefix, classLoader)
                .getTypesAnnotatedWith(MappingTo.class);
        PropertyUtils.addBeanIntrospector(new FluentPropertyBeanIntrospector());

        for (Class<?> mappingClass : beansWithAnnotation) {

            MappingTo mappingTo = mappingClass.getAnnotation(MappingTo.class);

            Class toClass = mappingTo.value();
            ClassMappings value = new ClassMappings(toClass);
            value.setMappingClass(mappingClass);

            Object mappingInstance = inject.getInstance(mappingClass);

            mappingInstanceMap.put(toClass, mappingInstance);

            for (Method method : mappingClass.getMethods()) {
                if (method.isAnnotationPresent(Mapping.class)) {
                    Mapping mapping = method.getAnnotation(Mapping.class);

                    MappingType type = mapping.type();
                    Class originalClass = mapping.originalClass();
                    String name = mapping.name();

                    Map<String, Function> functionMapping = (Map<String, Function>) method.invoke(mappingInstance);

                    final Object to = toClass.newInstance();
                    for (Map.Entry<String, Function> entry : functionMapping.entrySet()) {
                        try {
                            PropertyUtils.setNestedProperty(to, entry.getKey(), null);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException("Invalid Mapping in " + mappingClass.getName() + "." + method.getName() + ": Cannot set field '" + entry.getKey() + "' in class " + toClass.getName());
                        } catch (IllegalArgumentException e) {
                            //Do nothing setting nulls on primitives //TODO: Fix
                        }

                    }

                    value.addMapping(originalClass, name, type, functionMapping);
                } else if (method.isAnnotationPresent(PostProcessor.class)) {
                    PostProcessor postProcessor = method.getAnnotation(PostProcessor.class);

                    if (!method.getReturnType().equals(Void.TYPE)) {
                        throw new RuntimeException("Post Processor's return type must be void");
                    }

                    Class<?>[] parameterTypes = method.getParameterTypes();

                    if (parameterTypes.length != 2) {
                        throw new RuntimeException("Post Processor must have 2 parameters of type toClass, fromClass");
                    }

                    if (!parameterTypes[0].equals(toClass) || !parameterTypes[1].equals(postProcessor.originalClass())) {
                        throw new RuntimeException("Post Processor originalClass mismatches the parameter types");
                    }

                    value.addPostProcessors(method, postProcessor);
                }
            }
            cacheMap.put(toClass, value);
        }
    }


    public boolean hasMappingForClass(Class toClass) {
        return cacheMap.containsKey(toClass);
    }

    public Map<Class<?>, Map<Class<?>, Set<String>>> getOrikaExcludes() {
        return orikaExcludes;
    }

    public Object getMappingClassObject(Class toClass) {
        return mappingInstanceMap.get(toClass);
    }

    public MappingFunction getMappingFunction(Class toClass, Class originalClass, String name, MappingType type) {

        MappingFunction mappingFunction = new MappingFunction();

        mappingFunction.setMappingType(type);

        ClassMappings classMappings = cacheMap.get(toClass);

        if (classMappings == null) {
            mappingFunction.setMapping(new HashMap<>());
        } else {
            mappingFunction.setMapping(classMappings.getMapping(originalClass, name, type));
        }

        return mappingFunction;
    }

    public List<Method> getPostProcessors(Class toClass, Class originalClass, String name) {
        ClassMappings classMappings = cacheMap.get(toClass);
        if (classMappings == null) {
            return null;
        }
        return classMappings.getPostProcessors(originalClass, name);
    }
}
