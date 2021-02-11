package io.funraise.dm.blitz.service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.funraise.dm.blitz.BlackholeConverter;
import io.funraise.dm.blitz.Converter;
import io.funraise.dm.blitz.CustomMappingWrapper;
import io.funraise.dm.blitz.MappingFunction;
import io.funraise.dm.blitz.MappingType;
import io.funraise.dm.blitz.SafeConverterFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.MappingContext.Factory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.impl.UtilityResolver;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.TypeFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DomainMapper uses Map of String, Function to map one pojo to another
 */
@Singleton
public class DomainMapper {

    private static final Logger log = LoggerFactory.getLogger(DomainMapper.class);

    private static final String EMPTY = "";

    private final MapperFactory mapperFactory;

    private final MappingType defaultEmbeddedMapping;
    private final boolean autoMapUsingOrika;
    private final boolean parallelProcessEmbeddedList;
    private final boolean ignoreNullPointerException;
    private final boolean failOnException;

    private final Table<Type, Type, Converter<?, ?>> converterTable;

    private final MappingService mappingService;

    @Inject
    private DomainMapper(DomainMapperConfig config, MappingService mappingService) {

        //TODO elimiate these properties entirely and just use config.
        this.defaultEmbeddedMapping = config.getDefaultEmbeddedMapping();
        this.autoMapUsingOrika = config.isAutoMapUsingOrika();
        this.parallelProcessEmbeddedList = config.isParallelProcessEmbeddedList();
        this.ignoreNullPointerException = config.isIgnoreNullPointerException();
        this.failOnException = config.isFailOnException();

        this.mapperFactory = new DefaultMapperFactory.Builder()
            .mappingContextFactory(new Factory() {
                @Override
                public MappingContext getContext() {
                    Thread.currentThread().setContextClassLoader(config.getClassLoader());
                    return super.getContext();
                }
            })
            .converterFactory(new SafeConverterFactory(UtilityResolver.getDefaultConverterFactory()))
            .build();

        config.getConverters()
            .forEach(converter -> this.mapperFactory.getConverterFactory().registerConverter(converter)
        );

        this.mapperFactory.getConverterFactory().registerConverter(new BlackholeConverter());

        converterTable = config.getConverters()
            .stream()
            .collect(
                HashBasedTable::create,
                (table, converter) -> table.put(converter.getAType(), converter.getBType(), converter),
                (tableA, tableB) -> tableA.putAll(tableB)
            );

        try {
            mappingService.loadMappings(config.getSearchPrefix(), config.getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.mappingService = mappingService;
    }

    public <From, To> List<To> mapList(Class<To> toClass, Collection<From> list) {
        return this.mapList(toClass, list, EMPTY, MappingType.NORMAL);
    }


    public <From, To> List<To> mapList(Class<To> toClass, Collection<From> list, MappingType mappingType) {
        return this.mapList(toClass, list, EMPTY, mappingType);
    }

    public <From, To> List<To> mapList(Class<To> toClass, Collection<From> list, String mappingName) {
        return this.mapList(toClass, list, mappingName, MappingType.NORMAL);
    }

    public <From, To> List<To> mapList(Class<To> toClass, Collection<From> list, String mappingName, MappingType mappingType) {
        return mapList(toClass, list, mappingName, mappingType, true);
    }
    public <From, To> List<To> mapList(Class<To> toClass, Collection<From> list, String mappingName, MappingType mappingType, boolean needsOrikaMapping) {
        return list.stream().map(o -> doMapping(toClass, o, mappingName, mappingType, needsOrikaMapping, null)).collect(Collectors.toList());
    }

    public <From, To> List<To> mapListParallel(Class<To> toClass, Collection<From> list) {
        return this.mapListParallel(toClass, list, EMPTY, MappingType.NORMAL);
    }


    public <From, To> List<To> mapListParallel(Class<To> toClass, Collection<From> list, MappingType mappingType) {
        return this.mapListParallel(toClass, list, EMPTY, mappingType);
    }

    public <From, To> List<To> mapListParallel(Class<To> toClass, Collection<From> list, String mappingName) {
        return this.mapListParallel(toClass, list, mappingName, MappingType.NORMAL);
    }

    public <From, To> List<To> mapListParallel(Class<To> toClass, Collection<From> list, String mappingName, MappingType mappingType) {
        return mapListParallel(toClass, list, mappingName, mappingType, true);
    }
    public <From, To> List<To> mapListParallel(Class<To> toClass, Collection<From> list, String mappingName, MappingType mappingType, boolean needsOrikaMapping) {
        return list.parallelStream().map(o -> doMapping(toClass, o, mappingName, mappingType, needsOrikaMapping, null)).collect(Collectors.toList());
    }



    public <From, To> To map(Class<To> toClass, From from) {
        return this.map(toClass, from, EMPTY, MappingType.NORMAL);
    }

    public <From, To> To map(Class<To> toClass, From from, MappingType mappingType) {
        return this.map(toClass, from, EMPTY, mappingType);
    }

    public <From, To> To map(Class<To> toClass, From from, String mappingName) {
        return this.map(toClass, from, mappingName, MappingType.NORMAL);
    }

    public <From, To> To map(Class<To> toClass, From from, String mappingName, MappingType mappingType) {
        return this.doMapping(toClass, from, mappingName, mappingType, true, null);
    }



    public <From, To> To merge(Class<To> toClass, From from, To to) {
        return this.merge(toClass, from, to, EMPTY, MappingType.NORMAL);
    }

    public <From, To> To merge(Class<To> toClass, From from, To to, MappingType mappingType) {
        return this.merge(toClass, from, to, EMPTY, mappingType);
    }

    public <From, To> To merge(Class<To> toClass, From from, To to, String mappingName) {
        return this.merge(toClass, from, to, mappingName, MappingType.NORMAL);
    }

    public <From, To> To merge(Class<To> toClass, From from, To to, String mappingName, MappingType mappingType) {
        return this.doMapping(toClass, from, mappingName, mappingType, true, to);
    }


    @SuppressWarnings("unchecked")
    private <From, To> To doMapping(final Class<To> toClass, From from, String mappingName, MappingType mappingType, boolean needsOrikaMapping, To to) {

        CustomMappingWrapper.Orika orikaOverrideSetting = CustomMappingWrapper.Orika.DEFAULT;

        if (from == null) return null;

        if (from instanceof CustomMappingWrapper) {
            CustomMappingWrapper cmo = (CustomMappingWrapper) from;
            from = (From)cmo.getObject();
            mappingType = cmo.getMappingType();
            mappingName = cmo.getMappingName();
            orikaOverrideSetting = cmo.getOrika();

            if (from == null) return null;

        }

        Class fromClass = from.getClass();
        ma.glasnost.orika.metadata.Type<From> fromType = TypeFactory.valueOf(fromClass);
        ma.glasnost.orika.metadata.Type<To> toType = TypeFactory.valueOf(toClass);
        Converter<?, ?> converter = converterTable.get(fromType, toType);

        // Primitives and immutables should not attempt to map their fields
        if (to != null && (toType.isImmutable() || toType.isPrimitiveWrapper())) {
            return to;
        }

        if (orikaOverrideSetting == CustomMappingWrapper.Orika.FORCE_ON || (orikaOverrideSetting == CustomMappingWrapper.Orika.DEFAULT && (needsOrikaMapping && autoMapUsingOrika))) {

            //TODO move to cache
            MapperFacade orikaMapper;

            if (!mapperFactory.existsRegisteredMapper(fromType, toType, false)) {

                ClassMapBuilder<?, ?> classMapBuilder = mapperFactory.classMap(fromClass, toClass);

                Map<Class<?>, Set<String>> toClassExcludes = mappingService.getOrikaExcludes().get(fromClass);
                if (toClassExcludes != null && toClassExcludes.size() > 0) {
                    Set<String> properties = toClassExcludes.get(toClass);
                    if (properties != null && properties.size() > 0) {
                        properties.forEach(classMapBuilder::exclude);
                    } else {
                        classMapBuilder.byDefault();
                    }
                } else {
                    classMapBuilder.byDefault();
                }

                classMapBuilder.register();
            }

            orikaMapper = mapperFactory.getMapperFacade();

            if (to == null || (converter != null && converter.skipMapping())) {
                to = orikaMapper.map(from, toClass);
            } else {
                orikaMapper.map(from, to);
            }

        }

        if (to == null) {
            try {
                to = toClass.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException("Error creating instance of " + toClass, e);
            }
        }

        if (converter == null || !converter.skipMapping()) {
            doAudibleMapping(toClass, from, mappingName, mappingType, fromClass, to);
        }

        handlePostProcessor(to, toClass, from, mappingName);

        return to;
    }

    private <To> void doAudibleMapping(Class<To> toClass, Object from, String mappingName, MappingType mappingType, Class fromClass, To to) {

        MappingFunction mappingFunction = mappingService.getMappingFunction(toClass, fromClass, mappingName, mappingType);

        Map<String, Function> mapping = mappingFunction.getMapping();

        Stream<String> mappedFields = mapping == null ? Stream.empty() : mapping.keySet().stream();
        Stream<String> declaredFields = Arrays.stream(toClass.getDeclaredFields()).map(Field::getName);

        Set<String> fieldsToConsider = Stream.concat(mappedFields, declaredFields).collect(Collectors.toSet());

        fieldsToConsider
            .forEach(toPropertyName -> {
                Function fromFunction;
                boolean needsOrikaMapping = false;
                if(mapping != null && mapping.containsKey(toPropertyName)) {
                    fromFunction = mapping.get(toPropertyName);

                    //---Fill-in gaps missed by orika mapper when it stopped mapping since it
                    //---could not resolve the field without this custom mapper
                    needsOrikaMapping = true;
                } else {
                    try {
                        //---Still try to evaluate non-mapped fields for nested custom mappers
                        Object fromPropertyValue = PropertyUtils.getProperty(from, toPropertyName);
                        fromFunction = fromClassObj -> fromPropertyValue;
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        //---No valid getter available in the 'from' class for this 'to' property
                        return;
                    }
                }

                try {
                    Class toPropertyType = PropertyUtils.getPropertyType(to, toPropertyName);

                    //If the field is collection
                    if (Collection.class.isAssignableFrom(toPropertyType)) {
                        handleCollections(to, toClass, toPropertyType, fromFunction, from, toPropertyName, needsOrikaMapping);
                    } else {

                        Object value = eval(toPropertyType, fromFunction, from, needsOrikaMapping, to, toPropertyName);

                        PropertyUtils.setNestedProperty(to, toPropertyName, value);
                    }

                } catch (Exception e) {
                    log.debug("unable to set " + toPropertyName + " on " + toClass, e);
                    if (failOnException) {
                        throw new RuntimeException(e);
                    }
                }
            }
        );
    }

    private void handleCollections(Object to, Class toClass, Class toPropertyType, Function fromFunction, Object finalFrom, String toPropertyName, boolean needsOrikaMapping) throws Exception {
        Field field = toClass.getDeclaredField(toPropertyName);

        Type type = field.getGenericType();

        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            Class typeForCollection = (Class) pt.getActualTypeArguments()[0];

            Object object = safelyEvaluateClosure(fromFunction, finalFrom);

            List fromList = null;

            MappingType overrideMappingTypeNested = null;

            if (object != null) {
                if (object instanceof CustomMappingWrapper) {
                    CustomMappingWrapper cmo = (CustomMappingWrapper) object;
                    overrideMappingTypeNested = cmo.getMappingType();
                    fromList = new ArrayList((Collection) cmo.getObject());
                } else {

                    fromList = new ArrayList((Collection) object);
                }
            }

            if(fromList != null) {
                if (parallelProcessEmbeddedList) {
                    fromList = mapListParallel(typeForCollection, fromList, overrideMappingTypeNested != null ? overrideMappingTypeNested : defaultEmbeddedMapping);
                } else {
                    fromList = mapList(typeForCollection, fromList, overrideMappingTypeNested != null ? overrideMappingTypeNested : defaultEmbeddedMapping);
                }
            }

            if (fromList == null) {
                PropertyUtils.setProperty(to, toPropertyName, null);

            } else { //Already provided by the fromList
                if (toPropertyType.isAssignableFrom(fromList.getClass())) { //Collection type is assignable

                    PropertyUtils.setProperty(to, toPropertyName, fromList);

                } else { //Explicit collection type
                    if (List.class.isAssignableFrom(toPropertyType)) {
                        List list = new ArrayList();
                        list.addAll(fromList);
                        PropertyUtils.setProperty(to, toPropertyName, list);

                    } else if (Set.class.isAssignableFrom(toPropertyType)) {
                        Set set = new HashSet();
                        set.addAll(fromList);
                        PropertyUtils.setProperty(to, toPropertyName, set);
                    } else {
                        throw new Exception("Unable to find find property collection type on " + toPropertyType);
                    }


                }


            }


        }
    }

    private void handlePostProcessor(Object to, Class toClass, Object from, String mappingName) {
        List<Method> methodsList =  mappingService.getPostProcessors(toClass, from.getClass(), mappingName);

        if (methodsList == null) {
            return;
        }

        List<Object> objectList = new ArrayList<>();
        objectList.add(to);
        objectList.add(from);

        Object[] objectArray = new Object[objectList.size()];

        objectArray = objectList.toArray(objectArray);

        Object mappingObject = mappingService.getMappingClassObject(toClass);


        for (Method method : methodsList) {
            try {
                method.invoke(mappingObject, objectArray);
            } catch (Exception e) {
                log.warn("Problem calling post processor: " + mappingObject.getClass().getTypeName() + " " + method.getName(), e);
                if (failOnException) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @SuppressWarnings("unchecked")

    private <From> Object eval(Class toPropertyClass, Function fromExpression, From from, boolean needsOrikaMapping, Object to, String toPropertyName) throws Exception {
        Object rhs = safelyEvaluateClosure(fromExpression, from);

        if (rhs != null) {
            if (!isAlreadyProvided(toPropertyClass, rhs)) {

                Object toProperty = PropertyUtils.getProperty(to, toPropertyName);

                return doMapping(toPropertyClass, rhs, EMPTY, defaultEmbeddedMapping, needsOrikaMapping, toProperty);
            } else if (rhs instanceof CustomMappingWrapper) {
                Object toProperty = PropertyUtils.getProperty(to, toPropertyName);

                return doMapping(toPropertyClass, rhs, EMPTY, defaultEmbeddedMapping, needsOrikaMapping, toProperty);

            }
        }

        return rhs;
    }

    @SuppressWarnings("unchecked")
    private boolean isAlreadyProvided(Class toPropertyClass, Object rhs) {
        return ClassUtils.isAssignable(rhs.getClass(), toPropertyClass);
    }

    @SuppressWarnings("unchecked")
    private <From> Object safelyEvaluateClosure(Function fromExpression, From from) {
        try {

            return fromExpression.apply(from);

        } catch (NullPointerException npe) {

            if (ignoreNullPointerException) {
                return null;
            } else {
                throw npe;
            }

        }

    }

    public MappingType getDefaultEmbeddedMapping() {
        return defaultEmbeddedMapping;
    }

    public boolean isAutoMapUsingOrika() {
        return autoMapUsingOrika;
    }

    public boolean isParallelProcessEmbeddedList() {
        return parallelProcessEmbeddedList;
    }

    public boolean isIgnoreNullPointerException() {
        return ignoreNullPointerException;
    }
}
