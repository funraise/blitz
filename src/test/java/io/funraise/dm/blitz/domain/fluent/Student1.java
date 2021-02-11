package io.funraise.dm.blitz.domain.fluent;

public class Student1 {

    private Integer id;

    private String name;

    private Integer age;

    private Double gpa;

    public Integer getId() {
        return id;
    }

    public Student1 setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Student1 setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public double getGpa() {
        return gpa;
    }

    public Student1 setGpa(double gpa) {
        this.gpa = gpa;
        return this;
    }

    public Student1 setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String toString() {
        return "[name: " + name + ", age: " + age + ", gpa: " +  gpa + ", id: " + id +"]";
    }

}
