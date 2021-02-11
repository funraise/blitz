package io.funraise.dm.blitz.domain.fluent;

public class Numbers {

    private Integer age;

    private Double gpa;

    public Integer getAge() {
        return age;
    }

    public Numbers setAge(Integer age) {
        this.age = age;
        return this;
    }

    public Double getGpa() {
        return gpa;
    }

    public Numbers setGpa(Double gpa) {
        this.gpa = gpa;
        return this;
    }

    public String toString() {
        return "[age: " + age + ", gpa: " + gpa + "]";
    }
}
