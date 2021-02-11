package io.funraise.dm.blitz.domain.fluent;

import io.funraise.dm.blitz.MappingType;
import io.funraise.dm.blitz.annotation.Mapping;
import io.funraise.dm.blitz.annotation.MappingTo;
import io.funraise.dm.blitz.annotation.PostProcessor;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@MappingTo(Student2.class)
public class Student2Mapping {

    @PostProcessor(originalClass = Student1.class)
    public void postProcess1(Student2 s2, Student1 s1) {
        System.out.println("x");
    }

    @PostProcessor(originalClass = Student1.class)
    public void postProcess2(Student2 s2, Student1 s1) {
        System.out.println("y");
    }

    @Mapping(originalClass = Student1.class)
    public Map<String, Function<Student1, ?>> getMapping() {
        return getStudent1Mapping();
    }

    @Mapping(originalClass = Student1.class, name = "reverse")
    public Map<String, Function<Student1, ?>> reverseMapping() {
        Map<String, Function<Student1, ?>> map = new HashMap<>();
        map.put("lastName", s -> s.getName().split(" ")[0]);
        map.put("firstName", s -> {
            try {
                return s.getName().split(" ")[1];
            } catch (Exception e) {
                return null;
            }
        });
        map.put("nums.age", Student1::getAge);
        map.put("nums.gpa", Student1::getGpa);

        return map;
    }

    @Mapping(type = MappingType.EMBEDDED, originalClass = Student1.class, name = "min_add")
    public Map<String, Function<Student1, ?>> minMapping() {
       return getStudent1Mapping();
    }

    private Map<String, Function<Student1, ?>> getStudent1Mapping() {
        Map<String, Function<Student1, ?>> map = new HashMap<>();
        map.put("firstName", s -> s.getName().split(" ")[0]);
        map.put("lastName", s -> {
            try {
                return s.getName().split(" ")[1];
            } catch (Exception e) {
                return null;
            }
        });
        map.put("nums.age", Student1::getAge);
        map.put("nums.gpa", Student1::getGpa);

        return map;
    }
}

