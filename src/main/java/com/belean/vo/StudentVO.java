package com.belean.vo;

/**
 * @author belean
 * @date 2021/11/27
 */
public class StudentVO {
    private String name;
    private Integer age;
    private String gender;
    private String address;
    private String classes;

    public StudentVO(String name, Integer age, String gender, String address, String classes) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.classes = classes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }
}
