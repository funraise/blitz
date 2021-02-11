package io.funraise.dm.blitz.service;

import io.funraise.dm.blitz.Converter;
import io.funraise.dm.blitz.MappingType;
import java.util.ArrayList;
import java.util.Collection;

public class DomainMapperConfig {

    private String searchPrefix;
    private MappingType defaultEmbeddedMapping = MappingType.EMBEDDED;
    private boolean autoMapUsingOrika = true;
    private boolean parallelProcessEmbeddedList = false;
    private boolean ignoreNullPointerException = false;
    private boolean failOnException = false;
    private final ClassLoader classLoader;
    private final Collection<Converter<?, ?>> converters;

    public DomainMapperConfig(String searchPrefix, ClassLoader classLoader) {
        this(searchPrefix, classLoader, new ArrayList<>());
    }

    public DomainMapperConfig(String searchPrefix, ClassLoader classLoader, Collection<Converter<?, ?>> converters) {
        this.searchPrefix = searchPrefix;
        this.classLoader = classLoader;
        this.converters = converters;
    }

    public String getSearchPrefix() {
        return searchPrefix;
    }

    public void setSearchPrefix(String searchPrefix) {
        this.searchPrefix = searchPrefix;
    }

    public MappingType getDefaultEmbeddedMapping() {
        return defaultEmbeddedMapping;
    }

    public void setDefaultEmbeddedMapping(MappingType defaultEmbeddedMapping) {
        this.defaultEmbeddedMapping = defaultEmbeddedMapping;
    }

    public boolean isAutoMapUsingOrika() {
        return autoMapUsingOrika;
    }

    public void setAutoMapUsingOrika(boolean autoMapUsingOrika) {
        this.autoMapUsingOrika = autoMapUsingOrika;
    }

    public boolean isParallelProcessEmbeddedList() {
        return parallelProcessEmbeddedList;
    }

    public void setParallelProcessEmbeddedList(boolean parallelProcessEmbeddedList) {
        this.parallelProcessEmbeddedList = parallelProcessEmbeddedList;
    }

    public boolean isIgnoreNullPointerException() {
        return ignoreNullPointerException;
    }

    public void setIgnoreNullPointerException(boolean ignoreNullPointerException) {
        this.ignoreNullPointerException = ignoreNullPointerException;
    }

    public boolean isFailOnException() {
        return failOnException;
    }

    public void setFailOnException(boolean failOnException) {
        this.failOnException = failOnException;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public Collection<Converter<?, ?>> getConverters() {
        return converters;
    }
}
