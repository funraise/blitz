package io.funraise.dm.blitz.domain.fluent;

public class Student2 {

    private Integer id;

    private String firstName;

    private String lastName;

    private Numbers nums = new Numbers();

    public Integer getId() {
        return id;
    }

    public Student2 setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Student2 setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Student2 setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Numbers getNums() {
        return nums;
    }

    public Student2 setNums(Numbers nums) {
        this.nums = nums;
        return this;
    }

    public String toString() {
        return "[firstName: " + firstName + ", lastName: " + lastName + ", Nums: " + nums.toString() + ", id: " + id + "]";
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Student2)) {
            return false;
        }
        Student2 s = (Student2) other;
        return s.getFirstName().equals(this.getFirstName())
                && s.getLastName().equals(this.getLastName())
                && s.getNums().getAge().equals(this.getNums().getAge())
                && s.getNums().getGpa().equals(this.getNums().getGpa());
    }
}
