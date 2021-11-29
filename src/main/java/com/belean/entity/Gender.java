package com.belean.entity;

/**
 * @author belean
 * @date 2021/11/27
 */
public enum Gender {
    MAN("男"),
    WOMAN("女");

    private String name;
    Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
