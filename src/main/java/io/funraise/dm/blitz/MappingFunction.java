package io.funraise.dm.blitz;

import java.util.Map;
import java.util.function.Function;


public class MappingFunction {

    MappingType mappingType;
    Map<String, Function> mapping;

    public MappingType getMappingType() {
        return mappingType;
    }

    public void setMappingType(MappingType mappingType) {
        this.mappingType = mappingType;
    }

    public Map<String, Function> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, Function> mapping) {
        this.mapping = mapping;
    }

}
